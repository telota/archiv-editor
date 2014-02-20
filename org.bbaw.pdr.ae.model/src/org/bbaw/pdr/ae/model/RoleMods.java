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
 * The Class RoleMods.
 * @author Christoph Plutte
 */
public class RoleMods implements Cloneable
{

	/** The role term. */
	private String _roleTerm;

	/** The authority. */
	private String _authority;

	/** The type. */
	private String _type;

	/**
	 * @return cloned roleMods
	 * @see java.lang.Object#clone()
	 */
	@Override
	public final RoleMods clone()
	{
		try
		{
			RoleMods clone = (RoleMods) super.clone();
			if (this._authority != null)
			{
				clone._authority = new String(this._authority);
			}
			if (this._roleTerm != null)
			{
				clone._roleTerm = new String(this._roleTerm);
			}
			if (this._type != null)
			{
				clone._type = new String(this._type);
			}
			return clone;
		}
		catch (CloneNotSupportedException e)
		{
			throw new InternalError();
		}
	}

	/**
	 * Gets the authority.
	 * @return the authority
	 */
	public final String getAuthority()
	{
		return _authority;
	}

	/**
	 * Gets the role term.
	 * @return the role term
	 */
	public final String getRoleTerm()
	{
		return _roleTerm;
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
	 * Sets the authority.
	 * @param authority the new authority
	 */
	public final void setAuthority(final String authority)
	{
		this._authority = authority;
	}

	/**
	 * Sets the role term.
	 * @param roleTerm the new role term
	 */
	public final void setRoleTerm(final String roleTerm)
	{
		this._roleTerm = roleTerm;
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
