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

/**
 * The Class AspectSemanticFilter.
 * @author Christoph Plutte
 */
public class AspectSemanticFilter implements AEFilter
{

	/** The semantics. */
	private Vector<String> _semantics;

	/**
	 * Instantiates a new aspect semantic filter.
	 * @param semantics the semantics
	 */
	public AspectSemanticFilter(final Vector<String> semantics)
	{
		this._semantics = semantics;
	}

	/**
	 * Adds the semantic to filter.
	 * @param semantic the semantic
	 */
	public final void addSemanticToFilter(final String semantic)
	{
		if (_semantics == null)
		{
			_semantics = new Vector<String>();
		}
		if (!_semantics.contains(semantic))
		{
			_semantics.add(semantic);
		}
	}

	/**
	 * Gets the semantics.
	 * @return the semantics
	 */
	public final Vector<String> getSemantics()
	{
		return _semantics;
	}

	/**
	 * Removes the semantic to filter.
	 * @param semantic the semantic
	 */
	public final void removeSemanticToFilter(final String semantic)
	{
		if (_semantics != null)
		{
			_semantics.removeElement(semantic);
		}
	}

	@Override
	public final boolean select(final Object element)
	{
		if (_semantics != null)
		{
			if (element instanceof Aspect)
			{
				Aspect a = (Aspect) element;
				if (a != null && a.getSemanticDim() != null && a.getSemanticDim().getSemanticStms() != null)
				{
					for (int j = 0; j < a.getSemanticDim().getSemanticStms().size(); j++)
					{
						if (_semantics.contains(a.getSemanticDim().getSemanticStms().get(j).getLabel()))
						{
							return true;
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
	 * Sets the semantics.
	 * @param semantics the new semantics
	 */
	public final void setSemantics(final Vector<String> semantics)
	{
		this._semantics = semantics;
	}
}
