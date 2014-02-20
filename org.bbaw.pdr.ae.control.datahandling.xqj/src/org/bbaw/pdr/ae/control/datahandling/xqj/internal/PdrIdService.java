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

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import java.util.regex.Matcher;

import javax.xml.xquery.XQConnection;
import javax.xml.xquery.XQException;
import javax.xml.xquery.XQPreparedExpression;
import javax.xml.xquery.XQResultSequence;

import org.bbaw.pdr.ae.common.AEConstants;
import org.bbaw.pdr.ae.common.CommonActivator;
import org.bbaw.pdr.ae.control.interfaces.IPdrIdService;
import org.bbaw.pdr.ae.db.basex711.DBConnector;
import org.bbaw.pdr.ae.metamodel.PdrId;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;

/**
 * The Class PdrIdService.
 * @author Christoph Plutte
 */
public class PdrIdService implements IPdrIdService
{

	private IDSingleton _idSingleton = IDSingleton.getInstance();
	/** The db con. */
	private DBConnector _dbCon = DBConnector.getInstance();
	/** Logger. */
	private static ILog iLogger = AEConstants.ILOGGER;
	/** status. */
	private IStatus _log;

	@Override
	public final void clearAllUpdateStates() throws Exception
	{
		String type = "pdrPo";
		clearUpdateState(type);
		type = "pdrAo";
		clearUpdateState(type);
		type = "pdrRo";
		clearUpdateState(type);
		type = "pdrUo";
		clearUpdateState(type);
		type = "config";
		clearUpdateState(type);
		_dbCon.optimize("management");
	}

	/**
	 * clear update state.
	 * @param type type of objects
	 * @throws XQException exc.
	 */
	private void clearUpdateState(final String type) throws Exception
	{
		synchronized (_dbCon)
		{
			XQConnection con = _dbCon.getConnection();
			XQPreparedExpression xqp;
			String remove = "delete node collection(\"management\")//" + type;
			// System.out.println(remove);
			xqp = con.prepareExpression(remove);
			XQResultSequence xqs = xqp.executeQuery();
			String insert = "insert nodes <" + type
					+ "><updated></updated><new></new><modified></modified><deleted></deleted></" + type
					+ "> into collection(\"management\")/management";
			// System.out.println(insert);
			xqp = con.prepareExpression(insert);
			xqs = xqp.executeQuery();
			xqs.close();
			_dbCon.optimize("management");
			con.close();
		}

	}

	@Override
	public final void clearUserUpdateStates() throws Exception
	{
		clearUpdateState("pdrUo");

	}

	@Override
	public final Vector<String> getModifiedAspectIds() throws Exception
	{
		String query = null;
		query = "for $id in collection(\"management\")//pdrAo/modified/id\n" + "return $id/text()";
		// System.out.println(query);
		Vector<String> ids = new Vector<String>();
		String id = null;
		synchronized (_dbCon)
		{
			XQConnection con = _dbCon.getConnection();
			XQPreparedExpression xqp;
			xqp = con.prepareExpression(query);

			XQResultSequence xqs = xqp.executeQuery();
			// execute the XQuery Expression

			while (xqs.next())
			{
				id = xqs.getItemAsString(null);
				if (id.matches("pdr[APRU]o\\.\\d{3}\\.\\d{3}\\.\\d{9}"))
				{
					ids.add(id);
				}
			}
			_dbCon.optimize("management");
			con.close();
		}
		return ids;
	}

	@Override
	public final Vector<String> getModifiedConfigs() throws Exception
	{
		String query = null;
		query = "for $id in collection(\"management\")//config/modified/id\n" + "return $id/text()";
		// System.out.println(query);
		Vector<String> ids = new Vector<String>();
		String id = null;
		synchronized (_dbCon)
		{
			XQConnection con = _dbCon.getConnection();
			XQPreparedExpression xqp;
			xqp = con.prepareExpression(query);

			XQResultSequence xqs = xqp.executeQuery();
			// execute the XQuery Expression

			while (xqs.next())
			{
				id = xqs.getItemAsString(null);
				if (id != null && id.trim().length() > 0 && !ids.contains(id))
				{
					ids.add(id);
				}
			}
			_dbCon.optimize("management");
			con.close();
		}
		return ids;
	}

	@Override
	public final Vector<String> getModifiedObjects() throws Exception
	{
		String query = null;
		query = "for $id in collection(\"management\")//config/modified/id\n" + "return $id/text()";
		// System.out.println(query);
		Vector<String> ids = new Vector<String>();
		String id = null;
		synchronized (_dbCon)
		{
			XQConnection con = _dbCon.getConnection();
			XQPreparedExpression xqp;
			xqp = con.prepareExpression(query);

			XQResultSequence xqs = xqp.executeQuery();
			// execute the XQuery Expression

			while (xqs.next())
			{
				id = xqs.getItemAsString(null);
				if (id != null && id.trim().length() > 0 && !ids.contains(id))
				{
					ids.add(id);
				}
			}
			_dbCon.optimize("management");
			con.close();
		}
		return ids;
	}

	@Override
	public final Vector<String> getModifiedPersonIds() throws Exception
	{
		String query = null;
		query = "for $id in collection(\"management\")//pdrPo/modified/id\n" + "return $id/text()";
		// System.out.println(query);
		Vector<String> ids = new Vector<String>();
		String id = null;
		synchronized (_dbCon)
		{
			XQConnection con = _dbCon.getConnection();
			XQPreparedExpression xqp;
			xqp = con.prepareExpression(query);

			XQResultSequence xqs = xqp.executeQuery();
			// execute the XQuery Expression

			while (xqs.next())
			{
				id = xqs.getItemAsString(null);
				if (id.matches("pdr[APRU]o\\.\\d{3}\\.\\d{3}\\.\\d{9}") && !ids.contains(id))
				{
					ids.add(id);
				}
			}
			_dbCon.optimize("management");
			con.close();
		}
		return ids;
	}

	@Override
	public final Vector<String> getModifiedReferenceIds() throws Exception
	{
		String query = null;
		query = "for $id in collection(\"management\")//pdrRo/modified/id\n" + "return $id/text()";
		// System.out.println(query);
		Vector<String> ids = new Vector<String>();
		String id = null;
		synchronized (_dbCon)
		{
			XQConnection con = _dbCon.getConnection();
			XQPreparedExpression xqp;
			xqp = con.prepareExpression(query);

			XQResultSequence xqs = xqp.executeQuery();
			// execute the XQuery Expression

			while (xqs.next())
			{
				id = xqs.getItemAsString(null);
				if (id.matches("pdr[APRU]o\\.\\d{3}\\.\\d{3}\\.\\d{9}") && !ids.contains(id))
				{
					ids.add(id);
				}
			}
			_dbCon.optimize("management");
			con.close();
		}
		return ids;
	}

	@Override
	public final Vector<String> getModifiedUserIds() throws Exception
	{
		String query = null;
		query = "for $id in collection(\"management\")//pdrUo/modified/id\n" + "return $id/text()";
		// System.out.println(query);
		Vector<String> ids = new Vector<String>();
		String id = null;
		synchronized (_dbCon)
		{
			XQConnection con = _dbCon.getConnection();
			XQPreparedExpression xqp;
			xqp = con.prepareExpression(query);

			XQResultSequence xqs = xqp.executeQuery();
			// execute the XQuery Expression

			while (xqs.next())
			{
				id = xqs.getItemAsString(null);
				if (id.matches("pdr[APRU]o\\.\\d{3}\\.\\d{3}\\.\\d{9}") && !ids.contains(id))
				{
					ids.add(id);
				}
			}
			_dbCon.optimize("management");
			con.close();
		}
		return ids;
	}

	@Override
	public final Vector<String> getNewConfigs() throws Exception
	{
		String query = null;
		query = "for $id in collection(\"management\")//config/new/id\n" + "return $id/text()";
		// System.out.println(query);
		Vector<String> ids = new Vector<String>();
		String id = null;
		synchronized (_dbCon)
		{
			XQConnection con = _dbCon.getConnection();
			XQPreparedExpression xqp;
			xqp = con.prepareExpression(query);

			XQResultSequence xqs = xqp.executeQuery();
			// execute the XQuery Expression

			while (xqs.next())
			{
				id = xqs.getItemAsString(null);
				if (id != null && id.trim().length() > 0)
				{
					ids.add(id);
				}
			}
			_dbCon.optimize("management");
			con.close();
		}
		return ids;
	}

	@Override
	public final PdrId getNewId(final String type) throws Exception
	{
		// String query = "let $id := collection(\"management\")//" + type +
		// "/assignedIds/id[./@used='false']/text()\n" +
		// "order by $id\n" +
		// "return $id[1]\n";

		String query = null;
		int repository = Platform.getPreferencesService().getInt(CommonActivator.PLUGIN_ID, "REPOSITORY_ID",
				AEConstants.REPOSITORY_ID, null);
		int project = Platform.getPreferencesService().getInt(CommonActivator.PLUGIN_ID, "PROJECT_ID",
				AEConstants.PROJECT_ID, null);

		PdrId lastID = null;
		PdrId newID;
		if (type.equals("pdrAo"))
		{
			if (_idSingleton.getLastAspectId() == null)
			{
				proceceeLastID(type);
			}
			lastID = _idSingleton.getLastAspectId();
		}
		else if (type.equals("pdrPo"))
		{
			if (_idSingleton.getLastPersonID() == null)
			{
				proceceeLastID(type);
			}
			lastID = _idSingleton.getLastPersonID();
		}
		else if (type.equals("pdrRo"))
		{
			if (_idSingleton.getLastReferenceID() == null)
			{
				proceceeLastID(type);
			}
			lastID = _idSingleton.getLastReferenceID();
		}
		else if (type.equals("pdrUo"))
		{
			if (_idSingleton.getLastUserID() == null)
			{
				proceceeLastID(type);
			}
			lastID = _idSingleton.getLastUserID();
		}


		
		if (lastID != null)
		{
			if (lastID.getId() < 100000000)
			{
				newID = new PdrId(type, repository, project, 100000001);
			}
			else
			{
				newID = new PdrId(type, repository, project, lastID.getId() + 1);
			}

		}
		else
		{
			newID = new PdrId(type, repository, project, 100000001);
		}

		// System.out.println("neue Id " + pdrId.toString());

		query = null;
		query = "for $id in collection(\"management\")//" + type + "/new/id\n" + "return $id/text()";
		// System.out.println(query);
		Vector<String> ids = new Vector<String>();
		String secondId = null;
		synchronized (_dbCon)
		{
			XQConnection con = _dbCon.getConnection();
			XQPreparedExpression xqp;
			xqp = con.prepareExpression(query);

			XQResultSequence xqs = xqp.executeQuery();
			// execute the XQuery Expression

			while (xqs.next())
			{
				secondId = xqs.getItemAsString(null);
				if (secondId != null && secondId.trim().length() > 0 && !ids.contains(secondId))
				{
					ids.add(secondId);
				}
			}
			_dbCon.optimize("management");
			con.close();
		}
		if (!ids.contains(newID.toString()))
		{
			insertIdNewObject(newID);
			// System.out.println("NEW ID " + pdrId.toString());
			return newID;
		}
		else
		{
			while (ids.contains(newID.toString()))
			{
				newID.setId(newID.getId() + 1);
			}
			insertIdNewObject(newID);
			// System.out.println("NEW ID " + pdrId.toString());
			return newID;
		}
		// String replace = "for $id in collection(\"management\")//" + type +
		// "/assignedIds/id[./text()='" + id + "']" +
		// "return replace  node $id  with <id used=\"true\">" + id + "</id>";
		// System.out.println(replace);
		// xqp = con.prepareExpression(replace);
		// xqs = xqp.executeQuery();
		// xqs.close();

	}

	private void proceceeLastID(String type) throws Exception
	{
		String query = null;
		int repository = Platform.getPreferencesService().getInt(CommonActivator.PLUGIN_ID, "REPOSITORY_ID",
				AEConstants.REPOSITORY_ID, null);
		int project = Platform.getPreferencesService().getInt(CommonActivator.PLUGIN_ID, "PROJECT_ID",
				AEConstants.PROJECT_ID, null);
		if (type.equals("pdrAo"))
		{

			query = "declare namespace aodl=\"http://pdr.bbaw.de/namespaces/aodl/\";\n"
					+ "declare ft-option using case sensitive using stemming;\n"
					+ "for $id in collection(\"aspect\")/aodl:aspect[./@id contains text 'pdrAo."
					+ String.format("%03d", repository) + "." + String.format("%03d", project) + "']/@id\n"
					+ "order by $id descending\n" + "return string($id[1])\n";
		}
		else if (type.equals("pdrPo"))
		{
			query = "declare namespace podl=\"http://pdr.bbaw.de/namespaces/podl/\";\n"
					+ "declare ft-option using case sensitive using stemming;\n"
					+ "for $id in collection(\"person\")/podl:person[./@id contains text 'pdrPo."
					+ String.format("%03d", repository) + "." + String.format("%03d", project) + "']/@id\n"
					+ "order by $id descending\n" + "return string($id[1])\n";
		}
		else if (type.equals("pdrRo"))
		{
			query = "declare namespace mods=\"http://www.loc.gov/mods/v3\";\n"
					+ "declare ft-option using case sensitive using stemming;\n"
					+ "for $id in collection(\"reference\")/mods:mods[./@ID contains text 'pdrRo."
					+ String.format("%03d", repository) + "." + String.format("%03d", project) + "']/@ID\n"
					+ "order by $id descending\n" + "return string($id[1])\n";
		}
		else if (type.equals("pdrUo"))
		{
			query = "declare namespace uodl=\"http://pdr.bbaw.de/namespaces/uodl/\";\n"
					+ "declare ft-option using case sensitive using stemming;\n"
					+ "for $id in collection(\"users\")/uodl:user[./@id contains text 'pdrUo."
					+ String.format("%03d", repository) + "." + String.format("%03d", project) + "']/@id\n"
					+ "order by $id descending\n" + "return string($id[1])\n";
		}
		_log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "MainSearcher query: " + query);
		iLogger.log(_log);
		String id = "";
		synchronized (_dbCon)
		{
			XQConnection con = _dbCon.getConnection();
			XQPreparedExpression xqp;
			xqp = con.prepareExpression(query);

			XQResultSequence xqs = xqp.executeQuery();
			// execute the XQuery Expression

			while (xqs.next())
			{
				id = xqs.getItemAsString(null);
				Matcher m = AEConstants.PDR_ID_PATTERN.matcher(id);
				if (m.find())
				{
					break;
				}
			}
			_dbCon.optimize("management");
			con.close();
		}
		PdrId lastid = new PdrId(id);
		if (type.equals("pdrAo"))
		{
			_idSingleton.setLastAspectId(lastid);
		}
		else if (type.equals("pdrPo"))
		{
			_idSingleton.setLastPersonID(lastid);

		}
		else if (type.equals("pdrRo"))
		{
			_idSingleton.setLastReferenceID(lastid);
		}
		else if (type.equals("pdrUo"))
		{
			_idSingleton.setLastUserID(lastid);
		}

	}

	@Override
	public final Vector<String> getNewUserIds() throws Exception
	{
		String query = null;
		query = "for $id in collection(\"management\")//pdrUo/new/id\n" + "return $id/text()";
		// System.out.println(query);
		Vector<String> ids = new Vector<String>();
		String id = null;
		synchronized (_dbCon)
		{
			XQConnection con = _dbCon.getConnection();
			XQPreparedExpression xqp;
			xqp = con.prepareExpression(query);

			XQResultSequence xqs = xqp.executeQuery();
			// execute the XQuery Expression

			while (xqs.next())
			{
				id = xqs.getItemAsString(null);
				if (id.matches("pdr[APRU]o\\.\\d{3}\\.\\d{3}\\.\\d{9}") && !ids.contains(id))
				{
					//auskommentiert um auch standard user holen zu können.
//					if (new Integer(id.substring(14)) >= 100000000)
//					{
						ids.add(id);
//					}
				}
			}
			// _dbCon.optimize("management");
			con.close();
		}
		return ids;
	}

	@Override
	public final Date getUpdateTimeStamp() throws Exception
	{
		Date date;
		try
		{
			synchronized (_dbCon)
			{
				XQConnection con = _dbCon.getConnection();
				XQPreparedExpression xqp;
				String query = "for $x in collection(\"management\")//management\n" + "return string($x/@lastUpdate)";
				// System.out.println(query);

				xqp = con.prepareExpression(query);
				XQResultSequence xqs = xqp.executeQuery();
				String d = "";

				while (xqs.next())
				{

					d = xqs.getItemAsString(null);

					break;
				}
				date = AEConstants.ADMINDATE_FORMAT.parse(d);
				xqs.close();
				con.close();
				return date;
			}
		}
		catch (XQException e)
		{

			e.printStackTrace();
			return AEConstants.FIRST_EVER_UPDATE_TIMESTAMP;
		}
		catch (ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return AEConstants.FIRST_EVER_UPDATE_TIMESTAMP;

		}
	}

	@Override
	public final void insertIdDeletedObject(final PdrId pdrId) throws Exception
	{
		boolean isNew;
		synchronized (_dbCon)
		{
			XQConnection con = _dbCon.getConnection();
			XQPreparedExpression xqp;

			String queryIsNew = "let $id := collection(\"management\")//" + pdrId.getType() + "/new/id[./text()='"
					+ pdrId.toString() + "']\n" + "return\n" + "if ($id)\n" + "then <isNew/>\n" + "else <notIsNew/>\n";
			xqp = con.prepareExpression(queryIsNew);
			XQResultSequence xqs = xqp.executeQuery();
			isNew = xqs.getSequenceAsString(null).equals("<isNew/>");
			xqs.close();
			_dbCon.optimize("management");
			con.close();

		}

		if (!isNew)
		{
			String insert = "insert nodes <id>" + pdrId.toString() + "</id> into collection(\"management\")//"
					+ pdrId.getType() + "/deleted";
			// System.out.println(insert);
			synchronized (_dbCon)
			{
				XQConnection con = _dbCon.getConnection();
				XQPreparedExpression xqp;
				xqp = con.prepareExpression(insert);
				XQResultSequence xqs = xqp.executeQuery();
				xqs.close();
				con.close();

			}
		}

		resetIdUnused(pdrId);
		removeIdFromNew(pdrId);
		removeIdFromModified(pdrId);
		removeIdFromUpdated(pdrId);
	}

	@Override
	public final void insertIdModifiedObject(final PdrId pdrId) throws Exception
	{
		boolean isNew;
		synchronized (_dbCon)
		{
			XQConnection con = _dbCon.getConnection();
			XQPreparedExpression xqp;

			String queryIsNew = "let $id := collection(\"management\")//" + pdrId.getType() + "/new/id[./text()='"
					+ pdrId.toString() + "']\n" + "return\n" + "if ($id)\n" + "then <isNew/>\n" + "else <notIsNew/>\n";
			xqp = con.prepareExpression(queryIsNew);
			XQResultSequence xqs = xqp.executeQuery();
			isNew = xqs.getSequenceAsString(null).equals("<isNew/>");
			xqs.close();
			_dbCon.optimize("management");
			con.close();
		}

		if (!isNew)
		{
			boolean isModified = false;
			XQConnection con = _dbCon.getConnection();
			XQPreparedExpression xqp;

			String queryIsNew = "let $id := collection(\"management\")//" + pdrId.getType() + "/modified/id[./text()='"
					+ pdrId.toString() + "']\n" + "return\n" + "if ($id)\n" + "then <isModified/>\n"
					+ "else <notModified/>\n";
			xqp = con.prepareExpression(queryIsNew);
			XQResultSequence xqs = xqp.executeQuery();
			isModified = xqs.getSequenceAsString(null).equals("<isModified/>");
			xqs.close();
			_dbCon.optimize("management");
			con.close();
			String insert = "insert nodes <id>" + pdrId.toString() + "</id> into collection(\"management\")//"
					+ pdrId.getType() + "/modified";
			// System.out.println(insert);
			if (!isModified)
			{
				synchronized (_dbCon)
				{
					con = _dbCon.getConnection();
					xqp = con.prepareExpression(insert);
					xqs = xqp.executeQuery();
					xqs.close();
					con.close();
				}
			}
		}
	}

	@Override
	public final void insertIdModifiedObject(final Vector<String> modifiedIds, final String type) throws Exception
	{
		String insert = "insert nodes  <modified> ";
		for (String id : modifiedIds)
		{
			// System.out.println(id);
			insert += "<id>" + id + "</id>\n";
		}
		insert += " </modified> into collection(\"management\")//" + type;

		String delete = "delete nodes  collection(\"management\")//" + type + "/modified";
		synchronized (_dbCon)
		{
			XQConnection con = _dbCon.getConnection();
			XQPreparedExpression xqp;
			// System.out.println(insert);
			xqp = con.prepareExpression(delete);
			XQResultSequence xqs = xqp.executeQuery();
			xqp = con.prepareExpression(insert);
			xqs = xqp.executeQuery();
			xqs.close();
			_dbCon.optimize("management");
			con.close();
		}

	}

	@Override
	public final void insertIdNewObject(final PdrId pdrId) throws Exception
	{
		boolean isNew;
		synchronized (_dbCon)
		{
			XQConnection con = _dbCon.getConnection();
			XQPreparedExpression xqp;

			String queryIsNew = "let $id := collection(\"management\")//" + pdrId.getType() + "/new/id[./text()='"
					+ pdrId.toString() + "']\n" + "return\n" + "if ($id)\n" + "then <isNew/>\n" + "else <notIsNew/>\n";
			xqp = con.prepareExpression(queryIsNew);
			XQResultSequence xqs = xqp.executeQuery();
			isNew = xqs.getSequenceAsString(null).equals("<isNew/>");
			xqs.close();
			_dbCon.optimize("management");
			con.close();
		}
		if (!isNew)
		{
			synchronized (_dbCon)
			{
				XQConnection con = _dbCon.getConnection();
				XQPreparedExpression xqp;
				String insert = "insert nodes <id>" + pdrId.toString() + "</id> into collection(\"management\")//"
						+ pdrId.getType() + "/new";
				// System.out.println(insert);
				xqp = con.prepareExpression(insert);
				XQResultSequence xqs = xqp.executeQuery();
				xqs.close();
				_dbCon.optimize("management");
				con.close();
			}
			
		}
	}

	@Override
	public final void insertIdUpdatedObject(final PdrId pdrId) throws Exception
	{
		synchronized (_dbCon)
		{
			XQConnection con = _dbCon.getConnection();
			XQPreparedExpression xqp;
			String insert = "insert nodes <id>" + pdrId.toString() + "</id> into collection(\"management\")//"
					+ pdrId.getType() + "/updated";
			// System.out.println(insert);
			xqp = con.prepareExpression(insert);
			XQResultSequence xqs = xqp.executeQuery();
			xqs.close();
			_dbCon.optimize("management");
			con.close();
		}
	}

	@Override
	public final void insertIdUpdatedObjects(final Vector<String> pdrIds, final String type) throws Exception
	{
		String insert = "insert nodes  <updated> ";
		for (String id : pdrIds)
		{
			insert += "<id>" + id + "</id>\n";
		}
		insert += " </updated> into collection(\"management\")//" + type;

		String delete = "delete nodes  collection(\"management\")//" + type + "/updated";
		synchronized (_dbCon)
		{
			XQConnection con = _dbCon.getConnection();
			XQPreparedExpression xqp;
			// System.out.println(insert);
			xqp = con.prepareExpression(delete);
			XQResultSequence xqs = xqp.executeQuery();
			xqp = con.prepareExpression(insert);
			xqs = xqp.executeQuery();
			xqs.close();
			_dbCon.optimize("management");
			con.close();
		}
	}

	@Override
	public final void insertModifiedConfig(final String provider) throws Exception
	{
		boolean isNew;
		synchronized (_dbCon)
		{
			XQConnection con = _dbCon.getConnection();
			XQPreparedExpression xqp;

			String queryIsNew = "let $id := collection(\"management\")//config/new/id[./text()='" + provider + "']\n"
					+ "return\n" + "if ($id)\n" + "then <isNew/>\n" + "else <notIsNew/>\n";
			xqp = con.prepareExpression(queryIsNew);
			XQResultSequence xqs = xqp.executeQuery();
			isNew = xqs.getSequenceAsString(null).equals("<isNew/>");
			xqs.close();
			_dbCon.optimize("management");
			con.close();
		}

		if (!isNew)
		{
			String insert = "insert nodes <id>" + provider + "</id> into collection(\"management\")//config/modified";
			// System.out.println(insert);
			synchronized (_dbCon)
			{
				XQConnection con = _dbCon.getConnection();
				XQPreparedExpression xqp;
				xqp = con.prepareExpression(insert);
				XQResultSequence xqs = xqp.executeQuery();
				xqs.close();
				con.close();
			}
		}

	}

	@Override
	public final void insertNewConfig(final String provider) throws Exception
	{
		synchronized (_dbCon)
		{
			XQConnection con = _dbCon.getConnection();
			XQPreparedExpression xqp;
			String insert = "insert nodes <id>" + provider + "</id> into collection(\"management\")//config/new";
			// System.out.println(insert);
			xqp = con.prepareExpression(insert);
			XQResultSequence xqs = xqp.executeQuery();
			xqs.close();
			_dbCon.optimize("management");
			con.close();
		}
	}

	@Override
	public final boolean isModifiedOrNewObject(final PdrId pdrId) throws Exception
	{
		boolean isModified = false;
		try
		{
			synchronized (_dbCon)
			{
				XQConnection con = _dbCon.getConnection();
				XQPreparedExpression xqp;

				String queryIsNew = "let $id := collection(\"management\")//" + pdrId.getType() + "/new/id[./text()='"
						+ pdrId.toString() + "']\n" + "return\n" + "if ($id)\n" + "then <isModified/>\n"
						+ "else <notIsModified/>\n";
				// System.out.println(queryIsNew);
				xqp = con.prepareExpression(queryIsNew);
				XQResultSequence xqs;

				xqs = xqp.executeQuery();

				isModified = xqs.getSequenceAsString(null).equals("<isModified/>");
				if (!isModified)

				{
					queryIsNew = "let $id := collection(\"management\")//" + pdrId.getType()
							+ "/modified/id[./text()='" + pdrId.toString() + "']\n" + "return\n" + "if ($id)\n"
							+ "then <isModified/>\n" + "else <notIsModified/>\n";
					// System.out.println(queryIsNew);
					xqp = con.prepareExpression(queryIsNew);
					xqs = xqp.executeQuery();

					isModified = xqs.getSequenceAsString(null).equals("<isModified/>");
				}
				xqs.close();
				_dbCon.optimize("management");
				con.close();
			}
			// System.out.println(isModified);

			return isModified;
		}
		catch (XQException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return isModified;
	}

	@Override
	public final HashMap<String, Integer> loadObjectsUpdateState(final String type) throws Exception
	{
		HashMap<String, Integer> objectsUpdateState = new HashMap<String, Integer>();
		synchronized (_dbCon)
		{
			XQConnection con = _dbCon.getConnection();
			XQPreparedExpression xqp;
			String query = "for $id in collection(\"management\")//" + type + "/updated/id\n" + "return $id/text()";
			// System.out.println(query);
			xqp = con.prepareExpression(query);
			XQResultSequence xqs = xqp.executeQuery();

			while (xqs.next())
			{
				objectsUpdateState.put(xqs.getItemAsString(null), 1);
			}
			xqs.close();

			query = "for $id in collection(\"management\")//" + type + "/new/id\n" + "return $id/text()";
			// System.out.println(query);
			xqp = con.prepareExpression(query);
			xqs = xqp.executeQuery();

			while (xqs.next())
			{
				objectsUpdateState.put(xqs.getItemAsString(null), 2);
			}
			xqs.close();

			con.close();
		}
		return objectsUpdateState;
	}

	private void removeIdFromModified(final PdrId pdrId) throws Exception
	{
		synchronized (_dbCon)
		{
			XQConnection con = _dbCon.getConnection();
			XQPreparedExpression xqp;
			String remove = "delete node collection(\"management\")//" + pdrId.getType() + "/modified/id[./text() ='"
					+ pdrId.toString() + "']";
			// System.out.println(remove);
			xqp = con.prepareExpression(remove);
			XQResultSequence xqs = xqp.executeQuery();
			xqs.close();

			con.close();
		}
	}

	private void removeIdFromNew(final PdrId pdrId) throws Exception
	{

		synchronized (_dbCon)
		{
			XQConnection con = _dbCon.getConnection();
			XQPreparedExpression xqp;
			String remove = "delete node collection(\"management\")//" + pdrId.getType() + "/new/id[./text() ='"
					+ pdrId.toString() + "']";
			// System.out.println(remove);
			xqp = con.prepareExpression(remove);
			XQResultSequence xqs = xqp.executeQuery();
			xqs.close();

			con.close();
		}

	}

	private void removeIdFromUpdated(final PdrId pdrId) throws Exception
	{
		synchronized (_dbCon)
		{
			XQConnection con = _dbCon.getConnection();
			XQPreparedExpression xqp;
			String remove = "delete node collection(\"management\")//" + pdrId.getType() + "/updated/id[./text() ='"
					+ pdrId.toString() + "']";
			// System.out.println(remove);
			xqp = con.prepareExpression(remove);
			XQResultSequence xqs = xqp.executeQuery();
			xqs.close();

			con.close();
		}
	}

	@Override
	public final void resetIdUnused(final PdrId pdrId) throws Exception
	{
		synchronized (_dbCon)
		{
			XQConnection con = _dbCon.getConnection();
			XQPreparedExpression xqp;
			String replace = "for $id in collection(\"management\")//" + pdrId.getType() + "/assignedIds/id[./text()='"
					+ pdrId.toString() + "']" + "return replace  node $id  with <id used=\"false\">" + pdrId.toString()
					+ "</id>";
			// System.out.println(replace);
			xqp = con.prepareExpression(replace);
			XQResultSequence xqs = xqp.executeQuery();
			xqs.close();
			_dbCon.optimize("management");
			con.close();
		}
	}

	@Override
	public final void setUpdateTimeStamp(final Date date) throws Exception
	{

		synchronized (_dbCon)
		{
			XQConnection con = _dbCon.getConnection();
			XQPreparedExpression xqp;
			_dbCon.openCollection("management");
			// String query =
			// "declare namespace functx = \"http://www.functx.com\";\n" +
			// "declare function functx:update-attributes\n" +
			// "( $elements as element()* ,\n" +
			// "$attrNames as xs:QName* ,\n" +
			// "$attrValues as xs:anyAtomicType* )  as element()? {\n" +
			//
			// "for $element in $elements\n" +
			// "return element { node-name($element)}\n" +
			// "{ for $attrName at $seq in $attrNames\n" +
			// "return if ($element/@*[node-name(.) = $attrName])\n" +
			// "then attribute {$attrName}\n" +
			// "{$attrValues[$seq]}\n" +
			// "else (),\n" +
			// "$element/@*[not(node-name(.) = $attrNames)],\n" +
			// "$element/node() }\n" +
			// "} ;\n" +
			//
			// "for $x in collection(\"management\")//management\n" +
			// "return functx:update-attributes(\n" +
			// "$x, xs:QName('lastUpdate'), '" +
			// AEConstants.ADMINDATE_FORMAT.format(date) + "')";

			String query = "for $x in collection(\"management\")//management\n" + "let $old := $x/@lastUpdate\n"
					+ "let $new := '" + AEConstants.ADMINDATE_FORMAT.format(date) + "'\n"
					+ "return replace value of node $old with $new";
			// System.out.println(query);

			xqp = con.prepareExpression(query);
			XQResultSequence xqs = xqp.executeQuery();
			xqs.close();
			_dbCon.optimize("management");
			con.close();
		}

	}
}
