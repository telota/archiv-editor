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
 * The Class Note.
 * @author Christoph Plutte
 */
public class Note implements Cloneable
{

	/** The note. */
	private String _note;

	/** The type. */
	private String _type;

	/**
	 * @return cloned note
	 * @see java.lang.Object#clone()
	 */
	@Override
	public final Note clone()
	{
		try
		{
			Note clone = (Note) super.clone();
			if (this._note != null)
			{
				clone._note = new String(this._note);
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
	 * @return the note
	 */
	public final String getNote()
	{
		return _note;
	}

	/**
	 * @return the type
	 */
	public final String getType()
	{
		return _type;
	}

	/**
	 * @param note the note to set
	 */
	public final void setNote(final String note)
	{
		this._note = note;
	}

	/**
	 * @param type the type to set
	 */
	public final void setType(final String type)
	{
		this._type = type;
	}
}
