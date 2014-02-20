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
package org.bbaw.pdr.ae.view.control.filters;

import java.util.Vector;

import org.bbaw.pdr.ae.common.interfaces.AEFilter;
import org.bbaw.pdr.ae.model.Aspect;
import org.bbaw.pdr.ae.model.ValidationStm;

/**
 * The Class AspectReferenceFilter.
 * @author Christoph Plutte
 */
public class AspectReferenceFilter implements AEFilter
{

	/** The reference ids. */
	private Vector<String> _referenceIds;

	/**
	 * Instantiates a new aspect reference filter.
	 * @param referenceIds the reference ids
	 */
	public AspectReferenceFilter(final Vector<String> referenceIds)
	{
		this._referenceIds = referenceIds;
	}

	/**
	 * Adds the reference id to filter.
	 * @param referenceId the reference id
	 */
	public final void addReferenceIdToFilter(final String referenceId)
	{
		if (_referenceIds == null)
		{
			_referenceIds = new Vector<String>();
		}
		if (!_referenceIds.contains(referenceId))
		{
			_referenceIds.add(referenceId);
		}
	}

	/**
	 * Gets the reference ids.
	 * @return the reference ids
	 */
	public final Vector<String> getReferenceIds()
	{
		return _referenceIds;
	}

	/**
	 * Removes the reference id to filter.
	 * @param referenceId the reference id
	 */
	public final void removeReferenceIdToFilter(final String referenceId)
	{
		if (_referenceIds != null)
		{
			_referenceIds.removeElement(referenceId);
		}
	}

	@Override
	public final boolean select(final Object element)
	{
		if (_referenceIds != null)
		{
			if (element instanceof Aspect)
			{
				Aspect a = (Aspect) element;
				if (a != null && a.getValidation() != null && a.getValidation().getValidationStms() != null
						&& !a.getValidation().getValidationStms().isEmpty())
				{
					for (ValidationStm vStm : a.getValidation().getValidationStms())
					{
						if (vStm.getReference() != null && vStm.getReference().getSourceId() != null)
						{
							if (_referenceIds.contains(vStm.getReference().getSourceId().toString()))
							{
								return true;
							}
						}
					}
					return false;
				}
			}
			return false;
		}
		else
		{
			return true;
		}
	}

	/**
	 * Sets the reference ids.
	 * @param referenceIds the new reference ids
	 */
	public final void setReferenceIds(final Vector<String> referenceIds)
	{
		this._referenceIds = referenceIds;
	}
}
