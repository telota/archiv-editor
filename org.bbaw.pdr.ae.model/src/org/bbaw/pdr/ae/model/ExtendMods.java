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
 * The Class ExtendMods.
 * @author Christoph Plutte
 */
public class ExtendMods implements Cloneable
{

	/** The start. */
	private String _start;

	/** The end. */
	private String _end;

	/** The unit. */
	private String _unit;

	/**
	 * @return cloned extendMods
	 * @see java.lang.Object#clone()
	 */
	@Override
	public final ExtendMods clone()
	{
		try
		{
			ExtendMods clone = (ExtendMods) super.clone();
			if (this._end != null)
			{
				clone._end = new String(this._end);
			}
			if (this._start != null)
			{
				clone._start = new String(this._start);
			}
			if (this._unit != null)
			{
				clone._unit = new String(this._unit);
			}
			return clone;
		}
		catch (CloneNotSupportedException e)
		{
			throw new InternalError();
		}
	}

	/**
	 * Gets the end.
	 * @return the end
	 */
	public final String getEnd()
	{
		return _end;
	}

	/**
	 * Gets the start.
	 * @return the start
	 */
	public final String getStart()
	{
		return _start;
	}

	/**
	 * Gets the unit.
	 * @return the unit
	 */
	public final String getUnit()
	{
		return _unit;
	}

	/**
	 * Sets the end.
	 * @param end the new end
	 */
	public final void setEnd(final String end)
	{
		this._end = end;
	}

	/**
	 * Sets the start.
	 * @param start the new start
	 */
	public final void setStart(final String start)
	{
		this._start = start;
	}

	/**
	 * Sets the unit.
	 * @param unit the new unit
	 */
	public final void setUnit(final String unit)
	{
		this._unit = unit;
	}
}
