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
package org.bbaw.pdr.ae.control.saxHandler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Vector;

import org.bbaw.pdr.ae.metamodel.PdrId;
import org.bbaw.pdr.ae.metamodel.Record;
import org.bbaw.pdr.ae.metamodel.Revision;
import org.bbaw.pdr.ae.model.Authentication;
import org.bbaw.pdr.ae.model.User;
import org.bbaw.pdr.ae.model.UserContact;
import org.bbaw.pdr.ae.model.UserInformation;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/**
 * The Class UserSaxHandler.
 * @author Christoph Plutte
 */
public class UserSaxHandler implements ContentHandler
{

	/** The users. */
	private Vector<User> _users = new Vector<User>();

	/** The user. */
	private User _user;

	/** The authentication. */
	private Authentication _authentication;

	/** The revision. */
	private Revision _revision = null;

	/** The record. */
	private Record _record = null;

	/** The user information. */
	private UserInformation _userInformation = null;

	/** The user contact. */
	private UserContact _userContact = null;

	/** The role. */
	private boolean _role;

	/** The project. */
	private boolean _project;

	/** The contact. */
	private boolean _contact;

	/** The admin data format. */
	private SimpleDateFormat _adminDataFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");

	/** The _result object. */
	private Object _resultObject;

	@Override
	public final void characters(final char[] ch, final int start, final int length) throws SAXException
	{
		String str = new String(ch, start, length);
		if (_role)
		{
			_authentication.getRoles().add(str);
			_role = false;
		}
		else if (_project)
		{
			_userInformation.setProjectName(str);
			_project = false;
		}
		else if (_contact)
		{
			_userContact.setContact(str);
			_contact = false;
		}
	}

	@Override
	public void endDocument() throws SAXException
	{
		setResultObject(_user);

	}

	@Override
	public final void endElement(final String uri, final String localName, final String qName) throws SAXException
	{
		if (localName.equals("user") || localName.equals("uodl:user"))
		{
			_users.add(_user);
		}
		else if (localName.equals("record") || localName.equals("uodl:record"))
		{
			_user.setRecord(_record);
		}
		else if (localName.equals("revision") || localName.equals("uodl:revision"))
		{
			_record.getRevisions().add(_revision);

		}
		else if (localName.equals("authentication") || localName.equals("uodl:authentication"))
		{
			_user.setAuthentication(_authentication);

		}

		else if (localName.equals("information") || localName.equals("uodl:information"))
		{
			_user.setUserInformation(_userInformation);
		}
		else if (localName.equals("project") || localName.equals("uodl:project"))
		{
			_project = false;
		}
		else if (localName.equals("contact") || localName.equals("uodl:contact"))
		{
			_userInformation.getUserContacts().add(_userContact);
		}
		else if (localName.equals("result"))
		{
			setResultObject(_users);
		}
	}

	@Override
	public void endPrefixMapping(final String prefix) throws SAXException
	{

	}

	/**
	 * get result object.
	 * @return result object.
	 */
	public final Object getResultObject()
	{
		return _resultObject;
	}

	@Override
	public void ignorableWhitespace(final char[] ch, final int start, final int length) throws SAXException
	{

	}

	@Override
	public void processingInstruction(final String target, final String data) throws SAXException
	{

	}

	@Override
	public void setDocumentLocator(final Locator locator)
	{

	}

	/**
	 * set result object.
	 * @param resultObject result object.
	 */
	private void setResultObject(final Object resultObject)
	{
		this._resultObject = resultObject;
	}

	@Override
	public void skippedEntity(final String name) throws SAXException
	{

	}

	@Override
	public void startDocument() throws SAXException
	{

	}

	@Override
	public final void startElement(final String uri, final String localName, final String qName, final Attributes atts)
			throws SAXException
	{
		if (localName.equals("user") || localName.equals("uodl:user"))
		{
			for (int i = 0; i < atts.getLength(); i++)
			{
				if (atts.getQName(i).equals("id"))
				{
					_user = new User(atts.getValue(i));
				}
			}
		}
		else if (localName.equals("record") || localName.equals("uodl:record"))
		{
			_record = new Record();
		}
		else if (localName.equals("revision") || localName.equals("uodl:revision"))
		{
			_revision = new Revision();
			for (int i = 0; i < atts.getLength(); i++)
			{
				// System.out.println("Attribut: " + a.getQName(i)
				// + " Wert: " + a.getValue(i));
				if (atts.getQName(i).equals("ref"))
				{
					_revision.setRef(Integer.valueOf(atts.getValue(i)).intValue());
				}
				else if (atts.getQName(i).equals("timestamp"))
				{
					try
					{
						_revision.setTimeStamp(_adminDataFormat.parse(atts.getValue(i)));
					}
					catch (ParseException e)
					{
						e.printStackTrace();
					}
				}
				else if (atts.getQName(i).equals("authority"))
				{
					_revision.setAuthority(new PdrId(atts.getValue(i)));
				}
			}
		}
		else if (localName.equals("authentication") || localName.equals("uodl:authentication"))
		{
			_authentication = new Authentication();
			for (int i = 0; i < atts.getLength(); i++)
			{
				if (atts.getQName(i).equals("username"))
				{
					_authentication.setUserName(atts.getValue(i));
				}
				if (atts.getQName(i).equals("password"))
				{
					_authentication.setPassword(atts.getValue(i));
				}
			}
		}
		else if (localName.equals("role") || localName.equals("uodl:role"))
		{
			_role = true;
		}
		else if (localName.equals("information") || localName.equals("uodl:information"))
		{
			_role = false;
			_userInformation = new UserInformation();
		}
		else if (localName.equals("fullname") || localName.equals("uodl:fullname"))
		{
			for (int i = 0; i < atts.getLength(); i++)
			{
				if (atts.getQName(i).equals("forename"))
				{
					_userInformation.setForename(atts.getValue(i));
				}
				if (atts.getQName(i).equals("surname"))
				{
					_userInformation.setSurname(atts.getValue(i));
				}
			}
		}
		else if (localName.equals("project") || localName.equals("uodl:project"))
		{
			_project = true;
			for (int i = 0; i < atts.getLength(); i++)
			{
				if (atts.getQName(i).equals("position"))
				{
					_userInformation.setUserProjectPosition(atts.getValue(i));
				}
			}
		}
		else if (localName.equals("contact") || localName.equals("uodl:contact"))
		{
			_userContact = new UserContact();
			_contact = true;
			for (int i = 0; i < atts.getLength(); i++)
			{
				if (atts.getQName(i).equals("type"))
				{
					_userContact.setType(atts.getValue(i));
				}
			}
		}
	}

	@Override
	public void startPrefixMapping(final String prefix, final String uri) throws SAXException
	{

	}

}
