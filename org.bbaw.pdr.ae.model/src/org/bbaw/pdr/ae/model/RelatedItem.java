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
 * The Class RelatedItem.
 * @author Christoph Plutte
 */
public class RelatedItem implements Cloneable
{

	/** The type. */
	private String _type;

	/** The id. */
	private String _id;

	/** The part. */
	private PartMods _part = new PartMods();

	/**
	 * @return cloned relItem
	 * @see java.lang.Object#clone()
	 */
	@Override
	public final RelatedItem clone()
	{
		RelatedItem clone = null;
		try
		{
			clone = (RelatedItem) super.clone();
			if (this._id != null)
			{
				clone._id = new String(this._id);
			}
			if (this._type != null)
			{
				clone._type = new String(this._type);
			}
			if (this._part != null)
			{
				clone._part = this._part.clone();
			}

			return clone;
		}
		catch (CloneNotSupportedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return clone;

	}

	/**
	 * Gets the id.
	 * @return the id
	 */
	public final String getId()
	{
		return _id;
	}

	/**
	 * Gets the part.
	 * @return the part
	 */
	public final PartMods getPart()
	{
		return _part;
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
	 * Sets the id.
	 * @param id the new id
	 */
	public final void setId(final String id)
	{
		this._id = id;
	}

	/**
	 * Sets the part.
	 * @param part the new part
	 */
	public final void setPart(final PartMods part)
	{
		this._part = part;
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
