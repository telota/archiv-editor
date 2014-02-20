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
 * The Class Concurrences.
 * @author Christoph Plutte
 */
public class Concurrences implements Cloneable
{

	/** The concurrences. */
	private Vector<Concurrence> _concurrences;

	/**
	 * @return cloned concurrences
	 * @see java.lang.Object#clone()
	 */
	@Override
	public final Concurrences clone()
	{
		try
		{
			Concurrences clone = (Concurrences) super.clone();
			if (this._concurrences != null)
			{
				clone._concurrences = new Vector<Concurrence>(this._concurrences.size());
				for (int i = 0; i < this._concurrences.size(); i++)
				{
					clone._concurrences.add(this._concurrences.get(i).clone());
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
	 * Gets the concurrences.
	 * @return the concurrences
	 */
	public final Vector<Concurrence> getConcurrences()
	{
		if (_concurrences == null)
		{
			_concurrences = new Vector<Concurrence>(1);
		}
		return _concurrences;
	}

	/**
	 * Checks if is valid.
	 * @return true, if is valid
	 */
	public final boolean isValid()
	{
		if (_concurrences != null)
		{
			for (Concurrence c : _concurrences)
			{
				if (!c.isValid())
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
		if (_concurrences != null)
		{
			_concurrences.removeElementAt(index);
		}
		return true;

	}

	/**
	 * Sets the concurrences.
	 * @param concurrences the new concurrences
	 */
	public final void setConcurrences(final Vector<Concurrence> concurrences)
	{
		this._concurrences = concurrences;
	}
}
