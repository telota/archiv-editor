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
package org.bbaw.pdr.ae.view.main.preferences.provider;

import java.util.Comparator;

import org.bbaw.pdr.ae.config.model.ConfigData;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;

/**
 * The Class FavoriteMarkupComparator.
 * @author Christoph Plutte
 */
public class FavoriteMarkupComparator extends ViewerComparator implements Comparator<ConfigData>
{

	/** The property index. */
	private int _propertyIndex;

	/** The Constant DESCENDING. */
	private static final int DESCENDING = 1;

	/** The direction. */
	private int _direction = DESCENDING;

	/** The viewer. */
	private TableViewer _viewer;

	/**
	 * Instantiates a new favorite markup comparator.
	 */
	public FavoriteMarkupComparator()
	{
		this._propertyIndex = 0;
		_direction = DESCENDING;
	}

	/**
	 * Instantiates a new favorite markup comparator.
	 * @param viewer the viewer
	 */
	public FavoriteMarkupComparator(final TableViewer viewer)
	{
		this._propertyIndex = 0;
		_direction = DESCENDING;
		this._viewer = viewer;
	}

	@Override
	public final int compare(final ConfigData o1, final ConfigData o2)
	{
		if (_viewer != null)
		{
			return compare(_viewer, o1, o2);
		}
		return 0;
	}

	@Override
	public final int compare(final Viewer viewer, final Object e1, final Object e2)
	{
		MarkupTableLabelProvider tlp = (MarkupTableLabelProvider) ((TableViewer) viewer).getLabelProvider();
		int rc = 0;
		rc = tlp.getColumnText(e1, _propertyIndex).compareTo(tlp.getColumnText(e2, _propertyIndex));

		// If descending order, flip the direction
		if (_direction == DESCENDING)
		{
			rc = -rc;
		}
		return rc;
	}

	/**
	 * Sets the column.
	 * @param column the new column
	 */
	public final void setColumn(final int column)
	{
		if (column == this._propertyIndex)
		{
			// Same column as last sort; toggle the direction
			_direction = 1 - _direction;
		}
		else
		{
			// New column; do an ascending sort
			this._propertyIndex = column;
			_direction = DESCENDING;
		}
	}

	/**
	 * Sets the viewer.
	 * @param viewer the new viewer
	 */
	public final void setViewer(final TableViewer viewer)
	{
		this._viewer = viewer;
	}

}
