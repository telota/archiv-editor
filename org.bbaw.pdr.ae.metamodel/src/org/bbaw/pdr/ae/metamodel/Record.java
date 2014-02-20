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
package org.bbaw.pdr.ae.metamodel;

import java.util.Vector;

/**
 * The Class Record.
 * @author Christoph Plutte
 */
public class Record implements Cloneable
{

	/** The revisions. */
	private Vector<Revision> _revisions;

	/**
	 * Instantiates a new record.
	 */
	public Record()
	{
	}

	/**
	 * Instantiates a new record.
	 * @param record the record
	 */
	public Record(final Record record)
	{
		this._revisions = record.getRevisions();
	}

	@Override
	public Record clone()
	{
		try
		{
			Record clone = (Record) super.clone();
			if (this._revisions != null)
			{
				clone._revisions = new Vector<Revision>(this._revisions.size());
				for (int i = 0; i < this._revisions.size(); i++)
				{
					clone._revisions.add(this._revisions.get(i).clone());
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
	 * Gets the revisions.
	 * @return the revisions
	 */
	public final Vector<Revision> getRevisions()
	{
		if (_revisions == null)
		{
			_revisions = new Vector<Revision>(2);
		}
		return _revisions;
	}
}
