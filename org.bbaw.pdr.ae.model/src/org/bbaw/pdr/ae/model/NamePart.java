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
 * The Class NamePart.
 * @author Christoph Plutte
 */
public class NamePart implements Cloneable, Comparable<NamePart>
{

	/** The name part. */
	private String _namePart;

	/** The type. */
	private String _type;

	/**
	 * Instantiates a new name part.
	 */
	public NamePart()
	{
	}

	/**
	 * Instantiates a new name part.
	 * @param type the type
	 */
	public NamePart(final String type)
	{
		this._type = type;
	}

	/**
	 * @return cloned namepart
	 * @see java.lang.Object#clone()
	 */
	@Override
	public final NamePart clone()
	{
		try
		{
			NamePart clone = (NamePart) super.clone();
			if (this._namePart != null)
			{
				clone._namePart = new String(this._namePart);
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
	 * @param o name part
	 * @return <0 if this namepart is in alphabetical order before the second
	 *         one >0 otherwise.
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public final int compareTo(final NamePart o)
	{
		if (o == null)
		{
			return -1;
		}
		else if (this.getType() != null && o.getType() != null)
		{
			return this.getType().compareTo(o.getType());
		}
		else if (this.getType() == null)
		{
			return 1;
		}
		else if (o.getType() == null)
		{
			return -1;
		}
		return 0;
	}

	/**
	 * @return the namePart
	 */
	public final String getNamePart()
	{
		return _namePart;
	}

	/**
	 * @return the type
	 */
	public final String getType()
	{
		return _type;
	}

	/**
	 * @param namePart the namePart to set
	 */
	public final void setNamePart(final String namePart)
	{
		this._namePart = namePart;
	}

	/**
	 * @param type the type to set
	 */
	public final void setType(final String type)
	{
		this._type = type;
	}
}
