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
 * The Class Place.
 * @author Christoph Plutte
 */
public class Place implements Cloneable
{
	/** type attribute of place. */
	private String _type;
	/** subtype attribute of place. */
	private String _subtype;
	/** key attribute of place. */
	private String _key;
	/** value of place element of place. */
	private String _placeName;

	/**
	 * @return cloned place
	 * @see java.lang.Object#clone()
	 */
	@Override
	public final Place clone()
	{
		try
		{
			Place clone = (Place) super.clone();
			if (this._key != null)
			{
				clone._key = new String(this._key);
			}
			if (this._placeName != null)
			{
				clone._placeName = new String(this._placeName);
			}
			if (this._subtype != null)
			{
				clone._subtype = new String(this._subtype);
			}
			if (this._type != null)
			{
				clone._type = new String(this._type);
			}
			return clone;
		}
		catch (CloneNotSupportedException e)
		{
			// this shouldn't happen, since we are Cloneable
			throw new InternalError();
		}
	}

	/**
	 * Equals.
	 * @param place the place
	 * @return true, if successful
	 */
	public final boolean equals(Place place)
	{
		if (this._type != null && place._type != null)
		{
			if (!this._type.equals(place._type))
			{
				return false;
			}
		}
		else if ((this._type == null && place._type != null) || (this._type != null && place._type == null))
		{
			return false;
		}
		if (this._subtype != null && place._subtype != null)
		{
			if (!this._subtype.equals(place._subtype))
			{
				return false;
			}
		}
		else if ((this._subtype == null && place._subtype != null) || (this._subtype != null && place._subtype == null))
		{
			return false;
		}
		if (this._key != null && place._key != null)
		{
			if (!this._key.equals(place._key))
			{
				return false;
			}
		}
		else if ((this._key == null && place._key != null) || (this._key != null && place._key == null))
		{
			return false;
		}
		if (this._placeName != null && place._placeName != null)
		{
			if (!this._placeName.equals(place._placeName))
			{
				return false;
			}
		}
		else if ((this._placeName == null && place._placeName != null)
				|| (this._placeName != null && place._placeName == null))
		{
			return false;
		}
		return true;

	}

	/**
	 * Gets the key.
	 * @return the key
	 */
	public final String getKey()
	{
		return _key;
	}

	/**
	 * Gets the place name.
	 * @return the place name
	 */
	public final String getPlaceName()
	{
		return _placeName;
	}

	/**
	 * Gets the subtype.
	 * @return the subtype
	 */
	public final String getSubtype()
	{
		return _subtype;
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
	 * Checks if is valid.
	 * @return true, if is valid
	 */
	public final boolean isValid()
	{
		if (_placeName != null && _placeName.trim().length() > 0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * Sets the key.
	 * @param key the new key
	 */
	public final void setKey(final String key)
	{
		this._key = key;
	}

	/**
	 * Sets the place name.
	 * @param placeName the new place name
	 */
	public final void setPlaceName(final String placeName)
	{
		this._placeName = placeName;
	}

	/**
	 * Sets the subtype.
	 * @param subtype the new subtype
	 */
	public final void setSubtype(final String subtype)
	{
		this._subtype = subtype;
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
