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
package org.bbaw.pdr.ae.view.control.comparator;

import java.util.Comparator;

import org.bbaw.pdr.ae.model.PdrObject;

/**
 * The Class PdrObjectsByDisplayNameComparator.
 * @author Christoph Plutte
 */
public class PdrObjectsByDisplayNameComparator implements Comparator<PdrObject>
{

	/** The ascending. */
	private boolean _ascending;

	/**
	 * Instantiates a new pdr objects by display name comparator.
	 */
	public PdrObjectsByDisplayNameComparator()
	{
		this._ascending = false;
	}

	/**
	 * Instantiates a new pdr objects by display name comparator.
	 * @param ascending the ascending
	 */
	public PdrObjectsByDisplayNameComparator(final boolean ascending)
	{
		this._ascending = ascending;
	}

	@Override
	public final int compare(final PdrObject o1, final PdrObject o2)
	{
		int c = 0;
		if (o1 != null && o2 != null)
		{
			c = o1.getDisplayName().compareToIgnoreCase(o2.getDisplayName());
		}
		else if (o1 != null)
		{
			c = -1;
		}
		else if (o2 != null)
		{
			c = 1;
		}
		if (_ascending)
		{
			return -c;
		}
		else
		{
			return c;
		}
	}

}
