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
package org.bbaw.pdr.ae.config.editor.view;

import java.util.HashMap;

import org.bbaw.pdr.ae.config.model.ConfigData;
import org.bbaw.pdr.ae.config.model.ConfigTreeNode;
import org.bbaw.pdr.ae.config.model.DataType;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

/**
 * sorter for config tree in config editor.
 * @author Christoph Plutte
 */
public class ConfigTreeSorter extends ViewerSorter
{
	private static final String[] DATATYPES = new String[]{
		"personIdentifiers",
		"aodl:semanticStm",
		"aodl:relation",
		"aodl:persName",
		"aodl:orgName",
		"aodl:placeName",
		"aodl:name",
		"aodl:date",
		"aspectTemplates"
	};
	private HashMap<String, Integer> map;
	
	@Override
	public final int compare(final Viewer viewer, final Object e1, final Object e2)
	{
		ConfigTreeNode t1 = (ConfigTreeNode) e1;
		ConfigData c1 = t1.getConfigData();
		ConfigTreeNode t2 = (ConfigTreeNode) e2;
		ConfigData c2 = t2.getConfigData();
		if (c1 instanceof DataType && c2 instanceof DataType)
		{
			int i1 = getIndex(c1.getValue());
			int i2 = getIndex(c2.getValue());
			return i1 - i2;
		}
		else
		{
			return c1.getPriority() - c2.getPriority();
		}

	}

	private int getIndex(String value) {
		if (map == null)
		{
			map = new HashMap<String, Integer>(9);
			for (int i = 0; i< DATATYPES.length; i++)
			{
				String s = DATATYPES[i];
				map.put(s, i);
			}
		}
		if (map.containsKey(value))
		{
			return map.get(value);
		}
		return 10;
	}

}
