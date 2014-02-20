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

import org.bbaw.pdr.ae.common.interfaces.AEFilter;
import org.bbaw.pdr.ae.model.Aspect;

/**
 * The Class AspectSearchTextFilter.
 * @author Christoph Plutte
 */
public class AspectSearchTextFilter implements AEFilter
{

	/** The search text. */
	private String _searchText;

	/**
	 * Instantiates a new aspect search text filter.
	 * @param searchText the search text
	 */
	public AspectSearchTextFilter(final String searchText)
	{
		this.setSearchText(searchText);
		// System.out.println("searchfilter " + searchText);
	}

	/**
	 * Gets the search text.
	 * @return the search text
	 */
	public final String getSearchText()
	{
		return _searchText;
	}

	@Override
	public final boolean select(final Object element)
	{
		if (_searchText != null && _searchText.trim().length() > 0)
		{
			if (element instanceof Aspect)
			{
				Aspect a = (Aspect) element;
				if (a != null && a.getNotification() != null)
				{
					// System.out.println("searchfilter noti text" +
					// a.getNotification().toLowerCase());

					if (a.getNotification().toLowerCase().contains(_searchText.toLowerCase()))
					{
						// System.out.println("select");
						return true;
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
	 * Sets the search text.
	 * @param searchText the new search text
	 */
	public final void setSearchText(final String searchText)
	{
		this._searchText = searchText;
	}

}
