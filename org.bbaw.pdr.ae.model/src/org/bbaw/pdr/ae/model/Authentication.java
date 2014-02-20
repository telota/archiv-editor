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
 * The Class Authentication.
 * @author Christoph Plutte
 */
public class Authentication implements Cloneable
{

	/** The user name. */
	private String _userName;

	/** The password. */
	private String _password;

	/** The roles. */
	private Vector<String> _roles = new Vector<String>(3);

	/**
	 * @return cloned authentication.
	 * @see java.lang.Object#clone()
	 */
	@Override
	public final Authentication clone()
	{
		Authentication clone = null;
		try
		{
			clone = (Authentication) super.clone();
		}
		catch (CloneNotSupportedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (this._password != null)
		{
			clone._password = new String(this._password);
		}
		if (this._userName != null)
		{
			clone._userName = new String(this._userName);
		}
		if (this._roles != null)
		{
			clone._roles = new Vector<String>();
			for (int i = 0; i < this._roles.size(); i++)
			{
				clone._roles.add(new String(this._roles.get(i)));
			}
		}
		return clone;
	}

	/**
	 * Gets the password.
	 * @return the password
	 */
	public final String getPassword()
	{
		return _password;
	}

	/**
	 * Gets the roles.
	 * @return the roles
	 */
	public final Vector<String> getRoles()
	{
		return _roles;
	}

	/**
	 * Gets the user name.
	 * @return the user name
	 */
	public final String getUserName()
	{
		return _userName;
	}

	/**
	 * Sets the password.
	 * @param password the new password
	 */
	public final void setPassword(final String password)
	{
		this._password = password;
	}

	/**
	 * Sets the roles.
	 * @param roles the new roles
	 */
	public final void setRoles(final Vector<String> roles)
	{
		this._roles = roles;
	}

	/**
	 * Sets the user name.
	 * @param userName the new user name
	 */
	public final void setUserName(final String userName)
	{
		this._userName = userName;
	}

}
