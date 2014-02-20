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

import org.bbaw.pdr.ae.model.Aspect;
import org.bbaw.pdr.ae.model.ValidationStm;

/**
 * comparator for aspects by reference.
 * @author Christoph Plutte
 */
public class AspectsByReferenceComparator implements Comparator<Aspect>
{
	/** ascending flag. */
	private boolean _ascending;

	/**
	 * constructor with ascending flag.
	 * @param ascending ascending flag.
	 */
	public AspectsByReferenceComparator(final boolean ascending)
	{
		this._ascending = ascending;
	}

	@Override
	public final int compare(final Aspect a1, final Aspect a2)
	{
		int diff = 0;
		String refId1 = null;
		String refId2 = null;
		if (a1 != null && a1.getValidation() != null && a1.getValidation().getValidationStms() != null
				&& !a1.getValidation().getValidationStms().isEmpty())
		{
			if (a1.getValidation().getValidationStms().firstElement().getReference() != null
					&& a1.getValidation().getValidationStms().firstElement().getReference().getSourceId() != null)
			{
				refId1 = a1.getValidation().getValidationStms().firstElement().getReference().getSourceId().toString();
			}
			else
			{
				for (ValidationStm vStm : a1.getValidation().getValidationStms())
				{
					if (vStm.getReference() != null && vStm.getReference().getSourceId() != null)
					{
						refId1 = vStm.getReference().getSourceId().toString();
						break;
					}
				}
			}
		}
		if (a2 != null && a2.getValidation() != null && a2.getValidation().getValidationStms() != null
				&& !a2.getValidation().getValidationStms().isEmpty())
		{
			if (a2.getValidation().getValidationStms().firstElement().getReference() != null
					&& a2.getValidation().getValidationStms().firstElement().getReference().getSourceId() != null)
			{
				refId2 = a2.getValidation().getValidationStms().firstElement().getReference().getSourceId().toString();
			}
			else
			{
				for (ValidationStm vStm : a2.getValidation().getValidationStms())
				{
					if (vStm.getReference() != null && vStm.getReference().getSourceId() != null)
					{
						refId2 = vStm.getReference().getSourceId().toString();
						break;
					}
				}
			}
		}
		if (refId1 != null && refId2 != null)
		{
			diff = refId1.compareTo(refId2);
		}
		else if (refId1 != null)
		{
			diff = -1;
		}
		else if (refId2 != null)
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
