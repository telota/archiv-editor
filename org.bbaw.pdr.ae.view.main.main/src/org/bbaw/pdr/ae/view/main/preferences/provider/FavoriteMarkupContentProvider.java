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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.bbaw.pdr.ae.config.model.ConfigData;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * The Class FavoriteMarkupContentProvider.
 * @author Christoph Plutte
 */
public class FavoriteMarkupContentProvider implements IStructuredContentProvider
{

	@Override
	public void dispose()
	{

	}

	@Override
	public final Object[] getElements(final Object inputElement)
	{
		@SuppressWarnings("unchecked")
		HashMap<String, ConfigData> configs = (HashMap<String, ConfigData>) inputElement;
		ConfigData[] result = new ConfigData[configs.size()];
		List<String> keys = new ArrayList<String>(configs.keySet());
		Collections.sort(keys);
		int i = 0;
		for (String key : keys)
		{
			result[i] = configs.get(key);
			i++;
		}
		return result;

	}

	@Override
	public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput)
	{

	}

}
