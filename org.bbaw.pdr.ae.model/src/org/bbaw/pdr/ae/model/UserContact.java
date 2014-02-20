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

/**
 * The Class UserContact.
 * @author Christoph Plutte
 */
public class UserContact implements Cloneable
{

	/** The type. */
	private String _type;

	/** The contact. */
	private String _contact;

	/**
	 * @return cloned user contact
	 * @see java.lang.Object#clone()
	 */
	@Override
	public final UserContact clone()
	{
		UserContact clone = null;
		try
		{
			clone = (UserContact) super.clone();
		}
		catch (CloneNotSupportedException e)
		{
			e.printStackTrace();
		}
		if (this._contact != null)
		{
			clone._contact = new String(this._contact);
		}
		if (this._type != null)
		{
			clone._type = new String(this._type);
		}
		return clone;
	}

	/**
	 * Gets the contact.
	 * @return the contact
	 */
	public final String getContact()
	{
		return _contact;
	}

	/**
	 * Gets the type.
	 * @return the type
	 */
	public final String getType()
	{
		return _type;
	}

	/**
	 * Sets the contact.
	 * @param contact the new contact
	 */
	public final void setContact(final String contact)
	{
		this._contact = contact;
	}

	/**
	 * Sets the type.
	 * @param type the new type
	 */
	public final void setType(final String type)
	{
		this._type = type;
	}
}
