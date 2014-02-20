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
 * The Class ComplexName.
 * @author Christoph Plutte
 */
public class ComplexName implements Cloneable
{

	/** The fore name. */
	private String _foreName;

	/** The sur name. */
	private String _surName;

	/** The name link. */
	private String _nameLink;

	/** The gen name. */
	private String _genName;

	/** The org name. */
	private String _orgName;

	/**
	 * Instantiates a new complex name.
	 */
	public ComplexName()
	{

	}

	/**
	 * @return cloned complex name
	 * @see java.lang.Object#clone()
	 */
	@Override
	public final ComplexName clone()
	{
		try
		{
			ComplexName clone = (ComplexName) super.clone();
			if (this._foreName != null)
			{
				clone._foreName = new String(this._foreName);
			}
			if (this._nameLink != null)
			{
				clone._nameLink = new String(this._nameLink);
			}
			if (this._surName != null)
			{
				clone._surName = new String(this._surName);
			}
			if (this._genName != null)
			{
				clone._genName = new String(this._genName);
			}
			if (this._orgName != null)
			{
				clone._orgName = new String(this._orgName);
			}

			return clone;
		}
		catch (CloneNotSupportedException e)
		{
			throw new InternalError();
		}
	}

	/**
	 * Equals.
	 * @param o the object to comepare with
	 * @return true, if successful
	 */
	@Override
	public final boolean equals(final Object o)
	{
		if (o instanceof ComplexName)
		{
			ComplexName cn = (ComplexName) o;
			boolean equal = true;
			if (this._foreName != null && !this._foreName.equals(cn.getForeName()))
			{
				equal = false;
			}
			if (this._nameLink != null && !this._nameLink.equals(cn.getNameLink()))
			{
				equal = false;
			}
			if (this._surName != null && !this._surName.equals(cn.getNameLink()))
			{
				equal = false;
			}
			if (this._genName != null && !this._genName.equals(cn.getGenName()))
			{
				equal = false;
			}
			if (this._orgName != null && !this._orgName.equals(cn.getOrgName()))
			{
				equal = false;
			}

			return equal;

		}
		else
		{
			return false;
		}
	}

	/**
	 * Gets the fore name.
	 * @return the fore name
	 */
	public final String getForeName()
	{
		return _foreName;
	}

	/**
	 * Gets the gen name.
	 * @return the gen name
	 */
	public final String getGenName()
	{
		return _genName;
	}

	/**
	 * Gets the name link.
	 * @return the name link
	 */
	public final String getNameLink()
	{
		return _nameLink;
	}

	/**
	 * Gets the org name.
	 * @return the org name
	 */
	public final String getOrgName()
	{
		return _orgName;
	}

	/**
	 * Gets the sur name.
	 * @return the sur name
	 */
	public final String getSurName()
	{
		return _surName;
	}

	/**
	 * Sets the fore name.
	 * @param foreName the new fore name
	 */
	public final void setForeName(final String foreName)
	{
		this._foreName = foreName;
	}

	/**
	 * Sets the gen name.
	 * @param genName the new gen name
	 */
	public final void setGenName(final String genName)
	{
		this._genName = genName;
	}

	/**
	 * Sets the name link.
	 * @param nameLink the new name link
	 */
	public final void setNameLink(final String nameLink)
	{
		this._nameLink = nameLink;
	}

	/**
	 * Sets the org name.
	 * @param orgName the new org name
	 */
	public final void setOrgName(final String orgName)
	{
		this._orgName = orgName;
	}

	/**
	 * Sets the sur name.
	 * @param surName the new sur name
	 */
	public final void setSurName(final String surName)
	{
		this._surName = surName;
	}

	@Override
	public final String toString()
	{
		String str = "";
		boolean surname = false;

		if (_surName != null && _surName.trim().length() > 0)
		{
			str = _surName.trim();
			surname = true;
		}

		if (_foreName != null && _foreName.trim().length() > 0)
		{
			if (surname)
			{
				str += ", " + _foreName.trim();
			}
			else
			{
				str = _foreName.trim();

			}

		}
		if (_nameLink != null && _nameLink.trim().length() > 0)
		{
			str += " " + _nameLink.trim();
		}
		if (_genName != null && _genName.trim().length() > 0)
		{
			str += " " + _genName.trim();
		}
		if (_orgName != null && _orgName.trim().length() > 0)
		{
			str += " " + _orgName.trim();
		}
		return str.trim();
	}
}
