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
package org.bbaw.pdr.ae.config.core;

import java.util.Comparator;

import org.bbaw.pdr.ae.config.model.ConfigData;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;

/**
 * The Class ConfigDataComparator.
 * @author Christoph Plutte
 */
public class ConfigDataComparator extends ViewerComparator implements Comparator<ConfigData>
{
	/** The ascending. */
	private boolean _ascending = false;

	/**
	 * Instantiates a new config data comparator.
	 */
	public ConfigDataComparator()
	{
	}

	/**
	 * Instantiates a new config data comparator.
	 * @param ascending the ascending
	 */
	public ConfigDataComparator(final boolean ascending)
	{
		this._ascending = ascending;
	}

	/**
	 * compare two configData objects. returns an int<0 if configData1 is
	 * smaller or int >0 if configData1 is greater than configData2. if sorting
	 * order is set to ascending, it returns the opposite.
	 * @param c1 configData1
	 * @param c2 configData2
	 * @return comparaison
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public final int compare(final ConfigData c1, final ConfigData c2)
	{
		String l1;
		String l2;
		int compare = 0;
		if (c1.getValue().equals("ALL"))
		{
			return -1;
		}
		if (c2.getValue().equals("ALL"))
		{
			return 10;
		}
		else
		{
			if (c1.getLabel() != null && c1.getLabel().trim().length() > 0)
			{
				l1 = c1.getLabel();
			}
			else
			{
				l1 = c1.getValue();
			}
			if (c2.getLabel() != null && c2.getLabel().trim().length() > 0)
			{
				l2 = c2.getLabel();
			}
			else
			{
				l2 = c2.getValue();
			}
		}
		if (l1 != null && l2 != null)
		{
			compare = l1.compareToIgnoreCase(l2);
		}
		else
		{
			compare = 0;
		}
		if (_ascending)
		{
			return -compare;
		}
		else
		{
			return compare;
		}
	}

	/**
	 * @param viewer viewer.
	 * @param e1 first object
	 * @param e2 second object
	 * @return comparision fo objects.
	 * @see org.eclipse.jface.viewers.ViewerComparator#compare(org.eclipse.jface.viewers.Viewer,
	 *      java.lang.Object, java.lang.Object)
	 */
	@Override
	public final int compare(final Viewer viewer, final Object e1, final Object e2)
	{
		ConfigData c1 = (ConfigData) e1;
		ConfigData c2 = (ConfigData) e2;

		String l1;
		String l2;
		if (c1.getValue().equals("ALL"))
		{
			return -1;
		}
		if (c2.getValue().equals("ALL"))
		{
			return 10;
		}
		else
		{
			if (c1.getLabel() != null && c1.getLabel().trim().length() > 0)
			{
				l1 = c1.getLabel();
			}
			else
			{
				l1 = c1.getValue();
			}
			if (c2.getLabel() != null && c2.getLabel().trim().length() > 0)
			{
				l2 = c2.getLabel();
			}
			else
			{
				l2 = c2.getValue();
			}
		}
		if (l1 != null && l2 != null)
		{
			return l1.compareToIgnoreCase(l2);
		}
		else
		{
			return 0;
			// return c1.getLabel().compareToIgnoreCase(c2.getLabel());
		}
	}

}
