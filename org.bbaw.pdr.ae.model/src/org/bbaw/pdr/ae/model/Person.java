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

import org.bbaw.pdr.ae.metamodel.PdrId;

/**
 * The Class Person.
 * @author Christoph Plutte
 */
public class Person extends PdrObject implements Cloneable
{

	/**
	 * basic person data contains abstract data such as surname, forename, name
	 * link.
	 */
	private BasicPersonData _basicPersonData;

	/** The concurrences. */
	private Concurrences _concurrences;

	/** The identifiers. */
	private Identifiers _identifiers;

	/**
	 * Instantiates a new person.
	 * @param pdrId the pdr id
	 */
	public Person(final PdrId pdrId)
	{
		super(pdrId);
	}

	/**
	 * Instantiates a new person.
	 * @param pdrId the pdr id
	 */
	public Person(final String pdrId)
	{
		super(pdrId);
	}

	/**
	 * @return cloned person
	 * @see org.bbaw.pdr.ae.model.PdrObject#clone()
	 */
	@Override
	public final Person clone()
	{
		Person clone = (Person) super.clone();
		if (this._basicPersonData != null)
		{
			clone._basicPersonData = this._basicPersonData.clone();
		}
		if (this._concurrences != null)
		{
			clone._concurrences = this._concurrences.clone();
		}
		if (this._identifiers != null)
		{
			clone._identifiers = this._identifiers.clone();
		}
		return clone;
	}

	/**
	 * Gets the basic person data.
	 * @return the basic person data
	 */
	public final BasicPersonData getBasicPersonData()
	{
		return _basicPersonData;
	}

	/**
	 * Gets the concurrences.
	 * @return the concurrences
	 */
	public final Concurrences getConcurrences()
	{
		return _concurrences;
	}

	/**
	 * Gets the identifiers.
	 * @return the identifiers
	 */
	public final Identifiers getIdentifiers()
	{
		return _identifiers;
	}

	/**
	 * Checks if is valid.
	 * @return true, if is valid
	 */
	public final boolean isValid()
	{
		boolean valid = true;
		if (_concurrences != null)
		{
			valid = _concurrences.isValid();
		}
		if (_identifiers != null)
		{
			if (!_identifiers.isValid())
			{
				valid = false;
			}
		}
		return valid;
	}

	/**
	 * Sets the basic person data.
	 * @param basicPersonData the new basic person data
	 */
	public final void setBasicPersonData(final BasicPersonData basicPersonData)
	{
		this._basicPersonData = basicPersonData;
	}

	/**
	 * Sets the concurrences.
	 * @param concurrences the new concurrences
	 */
	public final void setConcurrences(final Concurrences concurrences)
	{
		this._concurrences = concurrences;
	}

	/**
	 * Sets the identifiers.
	 * @param identifiers the new identifiers
	 */
	public final void setIdentifiers(final Identifiers identifiers)
	{
		this._identifiers = identifiers;
	}

}
