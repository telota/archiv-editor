/**
 * This file is part of Archiv-Editor.
 * 
 * The software Archiv-Editor serves as a client user interface for working with
 * the Person Data Repository. See: pdr.bbaw.de
 * 
 * The software Archiv-Editor was developed at the Berlin-Brandenburg Academy
 * of Sciences and Humanities, Jägerstr. 22/23, D-10117 Berlin.
 * www.bbaw.de
 * 
 * Copyright (C) 2010-2013  Berlin-Brandenburg Academy
 * of Sciences and Humanities
 * 
 * The software Archiv-Editor was developed by @author: Christoph Plutte.
 * 
 * Archiv-Editor is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Archiv-Editor is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Archiv-Editor.  
 * If not, see <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
/*
 * @author: Christoph Plutte
 */
package org.bbaw.pdr.ae.control.datahandling.xqj.internal;

import java.util.HashMap;
import java.util.Vector;

import javax.xml.stream.XMLStreamException;
import javax.xml.xquery.XQConnection;
import javax.xml.xquery.XQException;
import javax.xml.xquery.XQPreparedExpression;
import javax.xml.xquery.XQResultSequence;

import org.bbaw.pdr.ae.common.AEConstants;
import org.bbaw.pdr.ae.common.CommonActivator;
import org.bbaw.pdr.ae.control.core.UserRichtsChecker;
import org.bbaw.pdr.ae.control.core.UserXMLProcessor;
import org.bbaw.pdr.ae.control.facade.Facade;
import org.bbaw.pdr.ae.control.interfaces.IUserManager;
import org.bbaw.pdr.ae.control.saxHandler.UserSaxHandler;
import org.bbaw.pdr.ae.db.basex711.DBConnector;
import org.bbaw.pdr.ae.metamodel.PdrId;
import org.bbaw.pdr.ae.metamodel.Record;
import org.bbaw.pdr.ae.metamodel.Revision;
import org.bbaw.pdr.ae.model.Authentication;
import org.bbaw.pdr.ae.model.User;
import org.bbaw.pdr.ae.model.UserInformation;
import org.eclipse.core.runtime.Platform;

/**
 * The Class UserManager.
 * @author Christoph Plutte
 */
public class UserManager implements IUserManager
{

	/** The users. */
	private Vector<User> _users = null;

	/** The db con. */
	private DBConnector _dbCon = DBConnector.getInstance();

	/** The ur checker. */
	private UserRichtsChecker _urChecker = new UserRichtsChecker();


	@Override
	public final HashMap<String, User> getMapOfSavedUsers() throws Exception
	{
		int repositoryId = Platform.getPreferencesService().getInt(CommonActivator.PLUGIN_ID, "REPOSITORY_ID",
				AEConstants.REPOSITORY_ID, null);
		int projectId = Platform.getPreferencesService().getInt(CommonActivator.PLUGIN_ID, "PROJECT_ID",
				AEConstants.PROJECT_ID, null);
		String userId;
		User u;
		HashMap<String, User> savedUsers = new HashMap<String, User>();
		for (int i = 0; i < 12; i++)
		{
			userId = Platform.getPreferencesService().getString(CommonActivator.PLUGIN_ID, "LAST_USER" + i, "", null);
			if (userId != null && userId.length() == 23)
			{
				try
				{
					u = getUserById(userId);
					if (u != null)
					{
						if ((u.getPdrId().getInstance() == repositoryId && u.getPdrId().getProjectID() == repositoryId)
								|| _urChecker.isUserPDRAdmin(u))
						{
							if (!savedUsers.containsKey(u.getAuthentication().getUserName()))
							{
								savedUsers.put(u.getAuthentication().getUserName(), u);
							}
						}
					}
				}
				catch (XQException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else
			{
				// break;
			}
		}
		return savedUsers;
	}

	@Override
	public final User getUserById(final String id) throws Exception
	{
		synchronized (_dbCon)
		{
			XQConnection con = _dbCon.getConnection();
			XQPreparedExpression xqp;
			String query = "declare namespace uodl=\"http://pdr.bbaw.de/namespaces/uodl/\";\n"
					+ "for $user in collection(\"users\")//uodl:user[./@id='" + id + "']\n"
					+ "return <result>{$user}</result>\n";
			// log = new Status(IStatus.INFO, Activator.PLUGIN_ID,
			// "MainSearcher query: " + query);
			// iLogger.log(log);
			// System.out.println(query);
			xqp = con.prepareExpression(query);

			XQResultSequence xqs = xqp.executeQuery();
			UserSaxHandler saxHandler = new UserSaxHandler();
			try
			{
				xqs.writeSequenceToSAX(saxHandler);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			xqs.close();

			con.close();
			_users = (Vector<User>) saxHandler.getResultObject();
		}

		// while (users == null)
		// {
		// System.out.println(".");
		// }
		User result = null;
		if (_users != null && !_users.isEmpty())
		{
			result = _users.firstElement();
		}
		_users = null;

		return result;
	}

	@Override
	public final User getUsersByUserName(final String userName) throws Exception
	{
		int repositoryId = Platform.getPreferencesService().getInt(CommonActivator.PLUGIN_ID, "REPOSITORY_ID",
				AEConstants.REPOSITORY_ID, null);
		int projectId = Platform.getPreferencesService().getInt(CommonActivator.PLUGIN_ID, "PROJECT_ID",
				AEConstants.PROJECT_ID, null);
		String idPrefix = "pdrUo." + String.format("%03d", repositoryId) + "." + String.format("%03d", projectId);
		synchronized (_dbCon)
		{
			XQConnection con = _dbCon.getConnection();
			XQPreparedExpression xqp;

			String query = "declare namespace uodl=\"http://pdr.bbaw.de/namespaces/uodl/\";\n"
					+ "for $user in collection(\"users\")//uodl:user[./@id contains text '" + idPrefix
					+ "'][./uodl:authentication//@username='" + userName + "']\n" + "return <result>{$user}</result>\n";
			// log = new Status(IStatus.INFO, Activator.PLUGIN_ID,
			// "MainSearcher query: " + query);
			// iLogger.log(log);
			System.out.println(query);
			xqp = con.prepareExpression(query);

			XQResultSequence xqs = xqp.executeQuery();
			// execute the XQuery Expression
			UserSaxHandler saxHandler = new UserSaxHandler();
			try
			{
				xqs.writeSequenceToSAX(saxHandler);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			xqs.close();

			con.close();
			_users = (Vector<User>) saxHandler.getResultObject();
		}

		// while (users == null)
		// {
		// System.out.println(".");
		// }
		Vector<User> result = _users;
		_users = null;

		if (result != null)
		{
			return result.firstElement();
		}
		else
		{
			return null;
		}
	}

	@Override
	public final void saveUser(final User user) throws Exception
	{
		String xml = new UserXMLProcessor().writeToXML(user);
		synchronized (_dbCon)
		{
			_dbCon.store2DB(xml, "users", user.getPdrId().toString() + ".xml", true);
		}
		if (user.getPdrId().getId() < 100000000)
		{
			new PdrIdService().insertIdModifiedObject(user.getPdrId());
		}

	}

	@Override
	public final void setUsers(final Vector<User> users)
	{
		this._users = users;

	}

	@Override
	public final boolean verifyOrCreateUsers()
	{
		int repositoryId = Platform.getPreferencesService().getInt(CommonActivator.PLUGIN_ID, "REPOSITORY_ID",
				AEConstants.REPOSITORY_ID, null);
		int projectId = Platform.getPreferencesService().getInt(CommonActivator.PLUGIN_ID, "PROJECT_ID",
				AEConstants.PROJECT_ID, null);

		boolean usersExist = true;
		PdrIdService idService = new PdrIdService();
		try
		{
			if (getUsersByUserName("pdrAdmin") == null)
			{
				// System.out.println("creating new pdrAdmin");
				PdrId id = new PdrId("pdrUo", repositoryId, projectId, 000000001);
				User testUser = createUser(id, "pdrAdmin", "pdrrdp", new String[]
				{"pdrAdmin", "admin", "user"}, "pdrAdmin", "pdrAdmin", "PDR-Administrator");

				try
				{
					saveUser(testUser);
					idService.insertIdNewObject(id);
				}
				catch (XMLStreamException e)
				{
					usersExist = false;
					e.printStackTrace();
				}
			}
		}
		catch (Exception e1)
		{
			usersExist = false;
			e1.printStackTrace();
		}
		try
		{
			if (getUsersByUserName("admin") == null)
			{
				PdrId id = new PdrId("pdrUo", repositoryId, projectId, 000000002);
				User testUser = createUser(id, "admin", "admin", new String[]
				{"admin", "user"}, "admin", "admin", "Project-Administrator");

				try
				{
					saveUser(testUser);
					idService.insertIdNewObject(id);

				}
				catch (XMLStreamException e)
				{
					usersExist = false;
				}
			}
		}
		catch (Exception e1)
		{
			usersExist = false;
		}
		try
		{
			if (getUsersByUserName("user") == null)
			{
				PdrId id = new PdrId("pdrUo", repositoryId, projectId, 000000003);
				User testUser = createUser(id, "user", "user", new String[]
				{"user"}, "user", "user", "Project-User");

				try
				{
					saveUser(testUser);
					idService.insertIdNewObject(id);

				}
				catch (XMLStreamException e)
				{
					usersExist = false;
				}
			}
		}
		catch (Exception e)
		{
			usersExist = false;
		}
		try
		{
			if (getUsersByUserName("guest") == null)
			{
				PdrId id = new PdrId("pdrUo", repositoryId, projectId, 000000004);
				User testUser = createUser(id, "guest", "guest", new String[]
				{"guest"}, "guest", "guest", "Project-Guest");

				try
				{
					saveUser(testUser);
					idService.insertIdNewObject(id);

				}
				catch (XMLStreamException e)
				{
					usersExist = false;
				}
			}
		}
		catch (Exception e)
		{
			usersExist = false;
		}
		try
		{
			if (getUsersByUserName("computer") == null)
			{
				PdrId id = new PdrId("pdrUo", repositoryId, projectId, 000000005);
				User testUser = createUser(id, "computer", "computer", new String[]
				{"user"}, "computer", "computer", "computer");

				try
				{
					saveUser(testUser);
					idService.insertIdNewObject(id);

				}
				catch (XMLStreamException e)
				{
					usersExist = false;
				}
			}
		}
		catch (Exception e)
		{
			usersExist = false;
		}
		for (int i = 6; i < 10; i++)
		{
			try
			{
				if (getUserById(new PdrId("pdrUo", repositoryId, projectId, i).toString()) == null)
				{
					PdrId id = new PdrId("pdrUo", repositoryId, projectId, i);
					User testUser = createUser(id, "dummy", "dummy", new String[]
					{"dummy"}, "dummy", "dummy", "dummy");

					try
					{
						saveUser(testUser);
						idService.insertIdNewObject(id);

					}
					catch (XMLStreamException e)
					{
						usersExist = false;
					}
				}
			}
			catch (Exception e)
			{
				usersExist = false;
			}
		}
		return usersExist;
	}

	public User createUser(PdrId id, String userName, String password, String[] userRoles, String surName,
			String foreName, String projectName)
	{
		User testUser = new User(id);
		testUser.setAuthentication(new Authentication());
		testUser.getAuthentication().setUserName(userName);
		testUser.getAuthentication().setPassword(password);
		testUser.getAuthentication().setRoles(new Vector<String>(1));
		for (String role : userRoles)
		{
			testUser.getAuthentication().getRoles().add(role);
		}

		testUser.setUserInformation(new UserInformation());
		testUser.getUserInformation().setForename(foreName);
		testUser.getUserInformation().setSurname(surName);
		testUser.getUserInformation().setProjectName(projectName);
		testUser.setRecord(new Record());
		Revision revision = new Revision();
		revision.setAuthority(id);
		revision.setRef(0);
		revision.setTimeStamp(Facade.getInstanz().getCurrentDate());
		testUser.getRecord().getRevisions().add(revision);
		return testUser;
	}

	@Override
	public void clearLastUsers()
	{
		int i = 0;
		while (i <= 12)
		{
			CommonActivator.getDefault().getPreferenceStore().setValue("LAST_USER" + i, "");
			i++;
		}

	}
}
