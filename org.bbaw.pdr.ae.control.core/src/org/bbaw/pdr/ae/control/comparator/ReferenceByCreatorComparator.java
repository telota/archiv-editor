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

import org.bbaw.pdr.ae.metamodel.PdrId;
import org.bbaw.pdr.ae.model.ReferenceMods;

/**
 * comparator for References by creator.
 * @author Christoph Plutte
 */
public class ReferenceByCreatorComparator implements Comparator<ReferenceMods>
{

	/** ascending flag. */
	private boolean _ascending;

	/**
	 * constructor with ascending flag.
	 * @param ascending ascending flag.
	 */
	public ReferenceByCreatorComparator(final boolean ascending)
	{
		this._ascending = ascending;
	}

	@Override
	public final int compare(final ReferenceMods r1, final ReferenceMods r2)
	{
		int diff = 0;
		PdrId creator1 = null;
		PdrId creator2 = null;
		if (r1.getRecord() != null && r1.getRecord().getRevisions() != null && !r1.getRecord().getRevisions().isEmpty()
				&& r1.getRecord().getRevisions().firstElement().getAuthority() != null)
		{
			creator1 = r1.getRecord().getRevisions().firstElement().getAuthority();
		}
		if (r2.getRecord() != null && r2.getRecord().getRevisions() != null && !r2.getRecord().getRevisions().isEmpty()
				&& r2.getRecord().getRevisions().firstElement().getAuthority() != null)
		{
			creator2 = r2.getRecord().getRevisions().firstElement().getAuthority();
		}
		if (creator1 != null && creator2 != null)
		{
			diff = creator1.compareTo(creator2);
		}
		else if (creator1 != null)
		{
			diff = -1;
		}
		else if (creator2 != null)
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
