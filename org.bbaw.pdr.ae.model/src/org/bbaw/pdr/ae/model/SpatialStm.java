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

import java.util.Vector;

/**
 * The Class SpatialStm.
 * @author Christoph Plutte
 */
public class SpatialStm implements Cloneable
{

	/** The type. */
	private String _type;

	/** The places. */
	private Vector<Place> _places;

	/**
	 * Instantiates a new spatial stm.
	 */
	public SpatialStm()
	{
		this._type = "undefined";
	}

	/**
	 * @return cloned spatial statement
	 * @see java.lang.Object#clone()
	 */
	@Override
	public final SpatialStm clone()
	{
		try
		{
			SpatialStm clone = (SpatialStm) super.clone();
			if (this._type != null)
			{
				clone._type = new String(this._type);
			}
			if (this._places != null)
			{
				clone._places = new Vector<Place>(this._places.size());
				for (int i = 0; i < this._places.size(); i++)
				{
					clone._places.add(this._places.get(i).clone());
				}
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
	 * @param sStm the s stm
	 * @return true, if successful
	 */
	public final boolean equals(final SpatialStm sStm)
	{
		if (this.getPlaces() != null && sStm.getPlaces() != null)
		{
			if (!(this.getPlaces().size() == sStm.getPlaces().size()))
			{
				return false;
			}
			for (int i = 0; i < this.getPlaces().size(); i++)
			{
				if (!this.getPlaces().get(i).equals(sStm.getPlaces().get(i)))
				{
					return false;
				}
			}
		}
		else if ((this.getPlaces() == null && sStm.getPlaces() != null)
				|| (this.getPlaces() != null && sStm.getPlaces() == null))
		{
			return false;
		}
		if (this._type != null && sStm._type != null)
		{
			if (!this._type.equals(sStm._type))
			{
				return false;
			}
		}
		else if ((this._type == null && sStm._type != null) || (this._type != null && sStm._type == null))
		{
			return false;
		}
		return true;

	}

	/**
	 * Gets the places.
	 * @return the places
	 */
	public final Vector<Place> getPlaces()
	{
		if (_places == null)
		{
			_places = new Vector<Place>(1);
		}
		return _places;
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
		if (_places != null)
		{
			for (Place p : _places)
			{
				if (!p.isValid())
				{
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Sets the places.
	 * @param places the new places
	 */
	public final void setPlaces(final Vector<Place> places)
	{
		this._places = places;
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
