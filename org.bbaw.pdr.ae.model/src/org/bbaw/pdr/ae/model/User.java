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

import org.bbaw.pdr.ae.metamodel.PdrId;
import org.bbaw.pdr.ae.metamodel.PdrMetaObject;

/**
 * The Class User.
 * @author Christoph Plutte
 */
public class User extends PdrMetaObject implements Cloneable
{

	/** The authentication. */
	private Authentication _authentication;

	/** The user information. */
	private UserInformation _userInformation;

	/**
	 * Instantiates a new user.
	 * @param pdrId the pdr id
	 */
	public User(final PdrId pdrId)
	{
		super(pdrId);
	}

	/**
	 * Instantiates a new user.
	 * @param pdrId the pdr id
	 */
	public User(final String pdrId)
	{
		super(pdrId);
	}

	/**
	 * @return cloned user
	 * @see org.bbaw.pdr.ae.metamodel.PdrMetaObject#clone()
	 */
	@Override
	public final User clone()
	{
		User clone = (User) super.clone();
		if (this._authentication != null)
		{
			clone._authentication = this._authentication.clone();
		}
		if (this._userInformation != null)
		{
			clone._userInformation = this._userInformation.clone();
		}
		clone._displayName = null;
		return clone;
	}

	/**
	 * Equals.
	 * @param u the u
	 * @return true, if successful
	 */
	public final boolean equals(final User u)
	{
		boolean equals = true;
		if (this.getPdrId() != null && u.getPdrId() != null)
		{
			equals = this.getPdrId().equals(u.getPdrId());
		}
		return equals;
	}

	/**
	 * Gets the authentication.
	 * @return the authentication
	 */
	public final Authentication getAuthentication()
	{
		return _authentication;
	}

	/**
	 * @return the displayName
	 */
	@Override
	public final String getDisplayName()
	{

		if (_displayName == null)
		{
			processDisplayName();
		}
		return _displayName;
	}

	@Override
	public final String getDisplayNameWithID()
	{
		return getDisplayName() + " (" + super.getPdrId().toString() + ")";
	}

	/**
	 * Gets the user information.
	 * @return the user information
	 */
	public final UserInformation getUserInformation()
	{
		return _userInformation;
	}

	/**
	 * Process display name.
	 */
	private void processDisplayName()
	{
		_displayName = "";
		if (_userInformation != null)
		{
			if (_userInformation.getForename() != null && _userInformation.getForename().trim().length() > 0)
			{
				_displayName = _userInformation.getForename();
			}

			if (_userInformation.getSurname() != null && _userInformation.getSurname().trim().length() > 0)
			{
				if (_displayName.trim().length() > 0)
				{
					_displayName += " ";
				}
				_displayName += _userInformation.getSurname();
			}
		}
		if (_displayName.trim().length() == 0)
		{
			if (_authentication != null && _authentication.getUserName() != null)
			{
				_displayName = _authentication.getUserName();
			}
			else
			{
				_displayName = super.getPdrId().toString();
			}
		}

	}

	/**
	 * Sets the authentication.
	 * @param authentication the new authentication
	 */
	public final void setAuthentication(final Authentication authentication)
	{
		this._authentication = authentication;
	}

	/**
	 * Sets the user information.
	 * @param userInformation the new user information
	 */
	public final void setUserInformation(final UserInformation userInformation)
	{
		this._userInformation = userInformation;
	}
}
