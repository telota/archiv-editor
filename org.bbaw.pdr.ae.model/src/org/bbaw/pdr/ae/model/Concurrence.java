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

import org.bbaw.pdr.ae.metamodel.PdrId;

/**
 * The Class Concurrence.
 * @author Christoph Plutte
 */
public class Concurrence implements Cloneable
{

	/** The person id. */
	private PdrId _personId;

	/**
	 * enhaelt die quellen, aus denen hervorgeht, dass beide personen identisch
	 * sein könnten.
	 */
	private Vector<ValidationStm> _references;

	/**
	 * Instantiates a new concurrence.
	 */
	public Concurrence()
	{

	}

	/**
	 * @return cloned concurrence
	 * @see java.lang.Object#clone()
	 */
	@Override
	public final Concurrence clone()
	{
		try
		{
			Concurrence clone = (Concurrence) super.clone();
			if (this._personId != null)
			{
				clone._personId = this._personId.clone();
			}
			if (this._references != null)
			{
				clone._references = new Vector<ValidationStm>(this._references.size());
				for (int i = 0; i < this._references.size(); i++)
				{
					clone._references.add(this._references.get(i).clone());
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
	 * Gets the person id.
	 * @return the person id
	 */
	public final PdrId getPersonId()
	{
		return _personId;
	}

	/**
	 * Gets the references.
	 * @return the references
	 */
	public final Vector<ValidationStm> getReferences()
	{
		if (_references == null)
		{
			_references = new Vector<ValidationStm>(1);
		}
		return _references;
	}

	/**
	 * Checks if is valid.
	 * @return true, if is valid
	 */
	public final boolean isValid()
	{
		if (_references == null || _references.isEmpty())
		{
			return false;
		}
		for (ValidationStm r : _references)
		{
			if (!r.isValid())
			{
				return false;
			}
		}
		return isValidId();
	}

	/**
	 * Checks if is valid id.
	 * @return true, if is valid id
	 */
	public final boolean isValidId()
	{
		if (_personId != null && _personId.isValid() && _personId.getType().equals("pdrPo"))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * Sets the person id.
	 * @param personId the new person id
	 */
	public final void setPersonId(final PdrId personId)
	{
		this._personId = personId;
	}
}
