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
 * The Class DetailMods.
 * @author Christoph Plutte
 */
public class DetailMods implements Cloneable
{

	/** The number. */
	private String _number;

	/** The caption. */
	private String _caption;

	/** The type. */
	private String _type;

	/**
	 * @return cloned DetailMods
	 * @see java.lang.Object#clone()
	 */
	@Override
	public final DetailMods clone()
	{
		try
		{
			DetailMods clone = (DetailMods) super.clone();
			if (this._caption != null)
			{
				clone._caption = new String(this._caption);
			}
			if (this._number != null)
			{
				clone._number = new String(this._number);
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
	 * Gets the caption.
	 * @return the caption
	 */
	public final String getCaption()
	{
		return _caption;
	}

	/**
	 * Gets the number.
	 * @return the number
	 */
	public final String getNumber()
	{
		return _number;
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
	 * Sets the caption.
	 * @param caption the new caption
	 */
	public final void setCaption(final String caption)
	{
		this._caption = caption;
	}

	/**
	 * Sets the number.
	 * @param number the new number
	 */
	public final void setNumber(final String number)
	{
		this._number = number;
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
