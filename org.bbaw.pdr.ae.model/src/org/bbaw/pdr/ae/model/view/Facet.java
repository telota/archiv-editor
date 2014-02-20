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
package org.bbaw.pdr.ae.model.view;

import org.bbaw.pdr.ae.metamodel.IAEPresentable;

/**
 * The Class Facet.
 * @author Christoph Plutte
 */
public class Facet implements IAEPresentable
{

	/** The type. */
	private int _type;

	/** The value. */
	private String _value;

	/** The key. */
	private String _key;

	/** The value2. */
	private String _value2;

	/**
	 * Instantiates a new facet.
	 */
	public Facet()
	{

	}

	@Override
	public final int compareTo(final IAEPresentable f)
	{
		if (f != null)
		{
			if (this._value != null && f.getValue() != null)
			{
				return (this._value.compareToIgnoreCase(f.getValue()));
			}
			else if (this._value != null)
			{
				return -1;
			}
			else if (f.getValue() != null)
			{
				return 1;
			}
			else
			{
				return 0;
			}
		}
		return -1;
	}

	@Override
	public final boolean equals(final Object o)
	{
		if (o instanceof Facet)
		{
			Facet f = (Facet) o;
			if (this._type == f.getType() && this._value != null && this._value.equals(f.getValue()))
			{
				if (this._key != null && f.getKey() != null)
				{
					if (this._key.equals(f.getKey()))
					{
						return true;
					}
					else
					{
						return false;
					}
				}
				else
				{
					return true;
				}
			}
			else
			{
				return false;
			}
		}
		else
		{
			return false;
		}

	}

	@Override
	public final String getContent()
	{
		return _value;
	}

	@Override
	public final int getCursorPosition()
	{
		return 0;
	}

	@Override
	public final String getDescription()
	{
		return null;
	}

	@Override
	public final String getImageString()
	{
		return null;
	}

	/**
	 * Gets the key.
	 * @return the key
	 */
	public final String getKey()
	{
		return _key;
	}

	@Override
	public final String getLabel()
	{
		return _value + " " + _type;
	}

	@Override
	public final int getPriority()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * Gets the type.
	 * @return the type
	 */
	public final int getType()
	{
		return _type;
	}

	@Override
	public final String getValue()
	{
		return _value;
	}

	/**
	 * Gets the value2.
	 * @return the value2
	 */
	public final String getValue2()
	{
		return _value2;
	}

	@Override
	public final int hashCode()
	{
		return super.hashCode();
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
	 * Sets the type.
	 * @param type the new type
	 */
	public final void setType(final int type)
	{
		this._type = type;
	}

	@Override
	public final void setValue(final String value)
	{
		this._value = value;
	}

	/**
	 * Sets the value2.
	 * @param value2 the new value2
	 */
	public final void setValue2(final String value2)
	{
		this._value2 = value2;
	}
}
