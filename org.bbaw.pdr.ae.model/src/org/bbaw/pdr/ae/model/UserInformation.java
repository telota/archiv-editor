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
package org.bbaw.pdr.ae.model;

import java.util.Vector;

/**
 * The Class UserInformation.
 * @author Christoph Plutte
 */
public class UserInformation implements Cloneable
{

	/** The forename. */
	private String _forename;

	/** The surname. */
	private String _surname;

	/** The project name. */
	private String _projectName;

	/** The user project position. */
	private String _userProjectPosition;

	/** The user contacts. */
	private Vector<UserContact> _userContacts = new Vector<UserContact>(3);

	/**
	 * @return cloned user information
	 * @see java.lang.Object#clone()
	 */
	@Override
	public final UserInformation clone()
	{
		UserInformation clone = null;
		try
		{
			clone = (UserInformation) super.clone();
		}
		catch (CloneNotSupportedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (this._forename != null)
		{
			clone._forename = new String(this._forename);
		}
		if (this._projectName != null)
		{
			clone._projectName = new String(this._projectName);
		}
		if (this._surname != null)
		{
			clone._surname = new String(this._surname);
		}
		if (this._userProjectPosition != null)
		{
			clone._userProjectPosition = new String(this._userProjectPosition);
		}

		if (this._userContacts != null)
		{
			clone._userContacts = new Vector<UserContact>();
			for (int i = 0; i < this._userContacts.size(); i++)
			{
				clone._userContacts.add(this._userContacts.get(i).clone());
			}
		}
		return clone;
	}

	/**
	 * Gets the forename.
	 * @return the forename
	 */
	public final String getForename()
	{
		return _forename;
	}

	/**
	 * Gets the project name.
	 * @return the project name
	 */
	public final String getProjectName()
	{
		return _projectName;
	}

	/**
	 * Gets the surname.
	 * @return the surname
	 */
	public final String getSurname()
	{
		return _surname;
	}

	/**
	 * Gets the user contacts.
	 * @return the user contacts
	 */
	public final Vector<UserContact> getUserContacts()
	{
		return _userContacts;
	}

	/**
	 * Gets the user project position.
	 * @return the user project position
	 */
	public final String getUserProjectPosition()
	{
		return _userProjectPosition;
	}

	/**
	 * Sets the forename.
	 * @param forename the new forename
	 */
	public final void setForename(final String forename)
	{
		this._forename = forename;
	}

	/**
	 * Sets the project name.
	 * @param projectName the new project name
	 */
	public final void setProjectName(final String projectName)
	{
		this._projectName = projectName;
	}

	/**
	 * Sets the surname.
	 * @param surname the new surname
	 */
	public final void setSurname(final String surname)
	{
		this._surname = surname;
	}

	/**
	 * Sets the user contacts.
	 * @param userContacts the new user contacts
	 */
	public final void setUserContacts(final Vector<UserContact> userContacts)
	{
		this._userContacts = userContacts;
	}

	/**
	 * Sets the user project position.
	 * @param userProjectPosition the new user project position
	 */
	public final void setUserProjectPosition(final String userProjectPosition)
	{
		this._userProjectPosition = userProjectPosition;
	}
}
