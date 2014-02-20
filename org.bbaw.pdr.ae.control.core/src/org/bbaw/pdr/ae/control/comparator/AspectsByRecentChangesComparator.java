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
package org.bbaw.pdr.ae.control.comparator;

import java.util.Comparator;
import java.util.Date;

import org.bbaw.pdr.ae.model.Aspect;

/**
 * comparator for aspects according to last change date.
 * @author Christoph Plutte
 */
public class AspectsByRecentChangesComparator implements Comparator<Aspect>
{

	/** ascending flag. */
	private boolean _ascending;

	/**
	 * constructor with ascending flag.
	 * @param ascending ascending flag.
	 */
	public AspectsByRecentChangesComparator(final boolean ascending)
	{
		this._ascending = ascending;
	}

	@Override
	public final int compare(final Aspect a1, final Aspect a2)
	{
		int diff = 0;
		Date d1 = null;
		Date d2 = null;
		if (a1.getRecord() != null && a1.getRecord().getRevisions() != null && !a1.getRecord().getRevisions().isEmpty()
				&& a1.getRecord().getRevisions().lastElement().getTimeStamp() != null)
		{
			d1 = a1.getRecord().getRevisions().lastElement().getTimeStamp();
		}
		if (a2.getRecord() != null && a2.getRecord().getRevisions() != null && !a2.getRecord().getRevisions().isEmpty()
				&& a2.getRecord().getRevisions().lastElement().getTimeStamp() != null)
		{
			d2 = a2.getRecord().getRevisions().lastElement().getTimeStamp();
		}
		if (d1 != null && d2 != null)
		{
			diff = d1.compareTo(d2);
		}
		else if (d1 != null)
		{
			diff = -1;
		}
		else if (d2 != null)
		{
			diff = 1;
		}
		else
		{
			diff = 0;
		}
		if (_ascending)
		{
			return -diff;
		}
		else
		{
			return diff;
		}
	}

}
