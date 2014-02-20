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

import java.util.HashMap;
import java.util.Vector;

import org.bbaw.pdr.ae.metamodel.PdrDate;

/**
 * The Class BasicPersonData.
 * @author Christoph Plutte
 */
public class BasicPersonData implements Cloneable
{

	/** The complex names. */
	private Vector<ComplexName> _complexNames;

	/** The display names. */
	private HashMap<String, ComplexName> _displayNames;

	/** The beginning of life. */
	private PdrDate _beginningOfLife;

	/** The end of life. */
	private PdrDate _endOfLife;

	/** The descriptions. */
	private Vector<String> _descriptions;

	/**
	 * Instantiates a new basic person data.
	 */
	public BasicPersonData()
	{
	}

	/**
	 * @return cloned basci person data
	 * @see java.lang.Object#clone()
	 */
	@Override
	public final BasicPersonData clone()
	{
		try
		{
			BasicPersonData clone = (BasicPersonData) super.clone();
			if (this._complexNames != null)
			{
				clone._complexNames = new Vector<ComplexName>(this._complexNames.size());
				for (int i = 0; i < this._complexNames.size(); i++)
				{
					clone._complexNames.add(this._complexNames.get(i).clone());
				}
			}
			if (this._displayNames != null)
			{
				clone._displayNames = new HashMap<String, ComplexName>();
				for (String key : this._displayNames.keySet())
				{
					clone._displayNames.put(key, this._displayNames.get(key).clone());
				}
			}
			return clone;
		}
		catch (CloneNotSupportedException e)
		{
			throw new InternalError();
		}
	}

	/**
	 * Gets the beginning of life.
	 * @return the beginning of life
	 */
	public final PdrDate getBeginningOfLife()
	{
		if (_beginningOfLife == null)
		{
			_beginningOfLife = new PdrDate(null);
		}
		return _beginningOfLife;
	}

	/**
	 * Gets the complex names.
	 * @return the complex names
	 */
	public final Vector<ComplexName> getComplexNames()
	{
		return _complexNames;
	}

	/**
	 * Gets the descriptions.
	 * @return the descriptions
	 */
	public final Vector<String> getDescriptions()
	{
		if (_descriptions == null)
		{
			_descriptions = new Vector<String>(2);
		}
		return _descriptions;
	}

	/**
	 * Gets the display names.
	 * @return the display names
	 */
	public final HashMap<String, ComplexName> getDisplayNames()
	{
		if (_displayNames == null)
		{
			_displayNames = new HashMap<String, ComplexName>(2);
		}
		return _displayNames;
	}

	/**
	 * Gets the end of life.
	 * @return the end of life
	 */
	public final PdrDate getEndOfLife()
	{
		if (_endOfLife == null)
		{
			_endOfLife = new PdrDate(null);
		}
		return _endOfLife;
	}

	/**
	 * Sets the beginning of life.
	 * @param beginningOfLife the new beginning of life
	 */
	public final void setBeginningOfLife(final PdrDate beginningOfLife)
	{
		this._beginningOfLife = beginningOfLife;
	}

	/**
	 * Sets the complex names.
	 * @param complexNames the new complex names
	 */
	public final void setComplexNames(final Vector<ComplexName> complexNames)
	{
		this._complexNames = complexNames;
	}

	/**
	 * Sets the descriptions.
	 * @param descriptions the new descriptions
	 */
	public final void setDescriptions(final Vector<String> descriptions)
	{
		this._descriptions = descriptions;
	}

	/**
	 * Sets the display names.
	 * @param displayNames the display names
	 */
	public final void setDisplayNames(final HashMap<String, ComplexName> displayNames)
	{
		this._displayNames = displayNames;
	}

	/**
	 * Sets the end of life.
	 * @param endOfLife the new end of life
	 */
	public final void setEndOfLife(final PdrDate endOfLife)
	{
		this._endOfLife = endOfLife;
	}
}
