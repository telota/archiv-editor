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
package org.bbaw.pdr.ae.rap2.internal;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.bbaw.pdr.ae.db.basex711.DBConnector;
import org.bbaw.pdr.ae.model.User;
import org.bbaw.pdr.allies.client.Utilities;
import org.bbaw.pdr.allies.client.error.PDRAlliesClientException;
import org.bbaw.pdr.allies.client.Configuration;
import org.bbaw.pdr.allies.client.IDRange;
import org.bbaw.pdr.allies.client.PDRType;
import org.bbaw.pdr.allies.client.Repository;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;

public class RAPUserManager {

	private Integer _projectID = -1;
	private HashMap<String, String> _userMap;
	private Pattern _pattern = Pattern.compile("username=\".+\"");
	/** The MA x_ objec t_ number. */
	private static final int MAX_OBJECT_NUMBER = 99999999;
	/** package size. */
	private static final int PACKAGE_SIZE = 249;
	public static final Pattern PDR_ID_PATTERN = Pattern.compile("pdr[APRU]o\\.\\d{3}\\.\\d{3}\\.\\d{9}");
	
	public User getUsersByUserName(String userName, String userID, String password) {
		User u = null;
		if (_userMap == null)
		{
			try {
				loadUserMap(userID, password);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (_userMap != null)
		{
			String xml = _userMap.get(userName);
			u = createUserFromString(xml);
		}
		if (u == null)
		{
			return null;
		}
		if (AERAPConstants.USER_IDS_BLACK_LIST != null && AERAPConstants.USER_IDS_BLACK_LIST.length > 0)
		{
			for (String s : AERAPConstants.USER_IDS_BLACK_LIST)
			{
				if (u.getPdrId().toString().equals(s) || new Integer(u.getPdrId().getId()).toString().equals(s))
				{
					return null;
				}
			}
		}
		if (AERAPConstants.USER_IDS_WHITE_LIST!= null && AERAPConstants.USER_IDS_WHITE_LIST.length > 0)
		{
			boolean found = false;
			for (String s : AERAPConstants.USER_IDS_WHITE_LIST)
			{
				if (u.getPdrId().toString().equals(s) || new Integer(u.getPdrId().getId()).toString().equals(s))
				{
					found = true;
					break;
				}
			}
			if (!found)
			{
				return null;
			}
		}
		return u;
	}

	private User createUserFromString(String xml) {
//		Matcher m = _locPattern.matcher(xml);
//		xml = m.replaceAll("");
		xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + xml;
		UserSaxHandler saxHandler = new UserSaxHandler();
		 SAXParserFactory factory = SAXParserFactory.newInstance();
	     SAXParser saxParser = null;
		try {
			saxParser = factory.newSAXParser();
			XMLReader reader = saxParser.getXMLReader();
		     try
		     {
		    	  // Turn on validation
		    	 reader.setFeature("http://xml.org/sax/features/validation", false); //$NON-NLS-1$
		    	  // Ensure namespace processing is on (the default)
		    	 reader.setFeature("http://xml.org/sax/features/namespaces", false); //$NON-NLS-1$
		    	}
		     catch (SAXNotRecognizedException e)
		     {
		    	  System.err.println("Unknown feature specified: " + e.getMessage()); //$NON-NLS-1$
		    	}
		     catch (SAXNotSupportedException e)
		     {
		    	  System.err.println("Unsupported feature specified: " + e.getMessage()); //$NON-NLS-1$
		    }
			

		} catch (ParserConfigurationException e1) {
			e1.printStackTrace();
		} catch (SAXException e1) {
			e1.printStackTrace();
		}
		

		if (saxParser != null)
		{
			try {
				InputStream is = new ByteArrayInputStream(xml.getBytes());
				saxParser.parse(is, saxHandler);
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Object o = saxHandler.getResultObject();
		{
			if (o != null && o instanceof User)
			{
				return (User)o;
			}
		}
		return null;
	     

	}

	private void loadUserMap(String userID, String password){
		URL url = null;
		try {
			url = new URL(AERAPConstants.REPOSITORY_URL);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		if (url != null)
		{
			Configuration.getInstance().setAxis2BaseURL(url.toString());
			Configuration.getInstance().setPDRUser(userID, password);
			Vector<IDRange> ranges = null;
			try {
				ranges = Utilities.getOccupiedObjectIDRanges(PDRType.USER, AERAPConstants.REPOSITORY_ID, _projectID, 1,
						MAX_OBJECT_NUMBER);
			} catch (PDRAlliesClientException e) {
				e.printStackTrace();
			}
			if (ranges != null)
			{
				int lowerBound = 1;
				int upperBound = 1;
				String username;
				_userMap = new HashMap<String, String>();
				for (IDRange range : ranges)
				{
					System.out.println("range " + range.getLowerBound() + " upper b " + range.getUpperBound());
					lowerBound = range.getLowerBound();
		
					while (upperBound < range.getUpperBound())
					{
						if (range.getUpperBound() - lowerBound <= PACKAGE_SIZE)
						{
							upperBound = range.getUpperBound();
						}
						else
						{
							upperBound = lowerBound + PACKAGE_SIZE;
						}
		
						Vector<String> objs = null;
						try {
							objs = Utilities.getObjects(PDRType.USER, AERAPConstants.REPOSITORY_ID, _projectID, lowerBound,
									upperBound);
						} catch (PDRAlliesClientException e) {
							e.printStackTrace();
						}
						if (objs != null)
						{
							for (String s : objs)
							{
								System.out.println(s);
								username = extractUserName(s);
								_userMap.put(username, s);
								
							}
						}
						lowerBound = Math.min(lowerBound + PACKAGE_SIZE, range.getUpperBound());
					}
				}
			}
		}
		
		
	}

	private String extractUserName(String s) {
		
		String name = null;
		Matcher m = _pattern.matcher(s);
		if (m.find())
		{
			name = m.group();
		}
		System.out.println("name before: " + name);
		String after = name.substring(10, name.length());
		after = after.split("\"")[0];
		System.out.println("name after: " + after);

		return after;
	}

	public void setProjectID(Integer projectID) {
		if (!this._projectID.equals(projectID))
		{
			this._projectID = projectID;
			_userMap = null;
		}
		
		
	}
	public String getUserId(String userName, int projectID) throws PDRAlliesClientException
	{
		Configuration.getInstance().setAxis2BaseURL(AERAPConstants.REPOSITORY_URL);
		return Repository.getUserID(userName, projectID);
	}
	
	public void updateUsers(final String userID, final String password)
			throws Exception
	{
		String name;
		Configuration.getInstance().setAxis2BaseURL(AERAPConstants.REPOSITORY_URL);
		Configuration.getInstance().setPDRUser(userID, password);
		Vector<IDRange> ranges = Utilities.getOccupiedObjectIDRanges(PDRType.USER, AERAPConstants.REPOSITORY_ID, _projectID, 1,
				MAX_OBJECT_NUMBER);
		String col = "users";
		int lowerBound = 1;
		int upperBound = 1;
		DBConnector dbCon = DBConnector.getInstance();
		synchronized (dbCon)
		{
			dbCon.openCollection(col);
			for (IDRange range : ranges)
			{
				lowerBound = range.getLowerBound();

				while (upperBound < range.getUpperBound())
				{
					if (range.getUpperBound() - lowerBound <= PACKAGE_SIZE)
					{
						upperBound = range.getUpperBound();
					}
					else
					{
						upperBound = lowerBound + PACKAGE_SIZE;
					}

					Vector<String> objs = Utilities.getObjects(PDRType.USER, AERAPConstants.REPOSITORY_ID, _projectID, lowerBound,
							upperBound);
					for (String s : objs)
					{
						name = extractPdrId(s) + ".xml";
						if (isValidUser(s))
						{
							dbCon.storeQuick2DB(s, col, name);
						}
					}
					lowerBound = Math.min(lowerBound + PACKAGE_SIZE, range.getUpperBound());
				}
			}
			dbCon.openCollection(col);
			dbCon.closeDB(col);
//			_idService.clearUserUpdateStates();
		}
		
	}
	private String extractPdrId(final String objectString)
	{

		Matcher m = PDR_ID_PATTERN.matcher(objectString);
		String id = null;
		if (m.find())
		{
			id = m.group();
		}
		return id;
	}
	private boolean isValidUser(String s)
	{
		if (s.startsWith("<user xmlns=\"http://pdr.bbaw.de/namespaces/uodl/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://pdr.bbaw.de/namespaces/uodl/ http://pdr.bbaw.de/schema/uodl.xsd\""))
		{
			return true;
		}
		else if (s
				.startsWith("<uodl:user xmlns=\"http://pdr.bbaw.de/namespaces/uodl/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://pdr.bbaw.de/namespaces/uodl/ http://pdr.bbaw.de/schema/uodl.xsd\""))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}
