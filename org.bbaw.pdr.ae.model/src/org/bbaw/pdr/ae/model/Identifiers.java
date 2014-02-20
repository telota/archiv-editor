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
 * The Class Identifiers.
 * @author Christoph Plutte
 */
public class Identifiers implements Cloneable
{

	/** The identifiers. */
	private Vector<Identifier> _identifiers;

	/**
	 * @return cloned identifiers
	 * @see java.lang.Object#clone()
	 */
	@Override
	public final Identifiers clone()
	{
		try
		{
			Identifiers clone = (Identifiers) super.clone();
			if (this._identifiers != null)
			{
				clone._identifiers = new Vector<Identifier>(this._identifiers.size());
				for (int i = 0; i < this._identifiers.size(); i++)
				{
					clone._identifiers.add(this._identifiers.get(i).clone());
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
	 * Gets the identifiers.
	 * @return the identifiers
	 */
	public final Vector<Identifier> getIdentifiers()
	{
		if (_identifiers == null)
		{
			_identifiers = new Vector<Identifier>(1);
		}
		return _identifiers;
	}

	/**
	 * Checks if is valid.
	 * @return true, if is valid
	 */
	public final boolean isValid()
	{
		if (_identifiers != null)
		{
			for (Identifier i : _identifiers)
			{
				if (!i.isValid())
				{
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Removes the.
	 * @param index the index
	 * @return true, if successful
	 */
	public final boolean remove(final Integer index)
	{
		_identifiers.removeElementAt(index);
		return true;

	}

	/**
	 * Sets the identifiers.
	 * @param identifiers the new identifiers
	 */
	public final void setIdentifiers(final Vector<Identifier> identifiers)
	{
		this._identifiers = identifiers;
	}
}
