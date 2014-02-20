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

import org.bbaw.pdr.ae.metamodel.PdrDate;
import org.bbaw.pdr.ae.model.ReferenceMods;

/**
 * comparator for References by chronology.
 * @author Christoph Plutte
 */
public class ReferenceCronComparator implements Comparator<ReferenceMods>
{

	/** ascending flag. */
	private boolean _ascending = true;

	/**
	 * constructor with descending flag.
	 * @param ascending ascending flag. Comparator will sort in descending order if false.
	 */
	public ReferenceCronComparator(final boolean ascending)
	{
		this._ascending = ascending;
	}

	@Override
	public final int compare(final ReferenceMods r1, final ReferenceMods r2)
	{
		int diff = 0;
		PdrDate d1 = null;
		PdrDate d2 = null;
		if (r1.getOriginInfo() != null && r1.getOriginInfo().getDateIssued() != null)
		{
			d1 = r1.getOriginInfo().getDateIssued();
		}
		else if (r1.getOriginInfo() != null && r1.getOriginInfo().getDateIssuedTimespan() != null
				&& r1.getOriginInfo().getDateIssuedTimespan().getDateFrom() != null)
		{
			d1 = r1.getOriginInfo().getDateIssuedTimespan().getDateFrom();
		}
		else if (r1.getOriginInfo() != null && r1.getOriginInfo().getDateCreated() != null)
		{
			d1 = r1.getOriginInfo().getDateCreated();
		}
		else if (r1.getOriginInfo() != null && r1.getOriginInfo().getDateCreatedTimespan() != null
				&& r1.getOriginInfo().getDateCreatedTimespan().getDateFrom() != null)
		{
			d1 = r1.getOriginInfo().getDateCreatedTimespan().getDateFrom();
		}
		else if (r1.getOriginInfo() != null && r1.getOriginInfo().getCopyrightDate() != null)
		{
			d1 = r1.getOriginInfo().getCopyrightDate();
		}
		else if (r1.getOriginInfo() != null && r1.getOriginInfo().getCopyrightDateTimespan() != null
				&& r1.getOriginInfo().getCopyrightDateTimespan().getDateFrom() != null)
		{
			d1 = r1.getOriginInfo().getCopyrightDateTimespan().getDateFrom();
		}
		else if (r1.getOriginInfo() != null && r1.getOriginInfo().getDateCaptured() != null)
		{
			d1 = r1.getOriginInfo().getDateCaptured();
		}
		else if (r1.getOriginInfo() != null && r1.getOriginInfo().getDateCapturedTimespan() != null
				&& r1.getOriginInfo().getDateCapturedTimespan().getDateFrom() != null)
		{
			d1 = r1.getOriginInfo().getDateCapturedTimespan().getDateFrom();
		}

		if (r2.getOriginInfo() != null && r2.getOriginInfo().getDateIssued() != null)
		{
			d2 = r2.getOriginInfo().getDateIssued();
		}
		else if (r2.getOriginInfo() != null && r2.getOriginInfo().getDateIssuedTimespan() != null
				&& r2.getOriginInfo().getDateIssuedTimespan().getDateFrom() != null)
		{
			d2 = r2.getOriginInfo().getDateIssuedTimespan().getDateFrom();
		}
		else if (r2.getOriginInfo() != null && r2.getOriginInfo().getDateCreated() != null)
		{
			d2 = r2.getOriginInfo().getDateCreated();
		}
		else if (r2.getOriginInfo() != null && r2.getOriginInfo().getDateCreatedTimespan() != null
				&& r2.getOriginInfo().getDateCreatedTimespan().getDateFrom() != null)
		{
			d2 = r2.getOriginInfo().getDateCreatedTimespan().getDateFrom();
		}
		else if (r2.getOriginInfo() != null && r2.getOriginInfo().getCopyrightDate() != null)
		{
			d2 = r2.getOriginInfo().getCopyrightDate();
		}
		else if (r2.getOriginInfo() != null && r2.getOriginInfo().getCopyrightDateTimespan() != null
				&& r2.getOriginInfo().getCopyrightDateTimespan().getDateFrom() != null)
		{
			d2 = r2.getOriginInfo().getCopyrightDateTimespan().getDateFrom();
		}
		else if (r2.getOriginInfo() != null && r2.getOriginInfo().getDateCaptured() != null)
		{
			d2 = r2.getOriginInfo().getDateCaptured();
		}
		else if (r2.getOriginInfo() != null && r2.getOriginInfo().getDateCapturedTimespan() != null
				&& r2.getOriginInfo().getDateCapturedTimespan().getDateFrom() != null)
		{
			d2 = r2.getOriginInfo().getDateCapturedTimespan().getDateFrom();
		}
		if (d1 != null && d2 != null)
		{
			diff = d1.compare(d2);
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
		return diff * (_ascending ? 1 : -1);
	}

}
