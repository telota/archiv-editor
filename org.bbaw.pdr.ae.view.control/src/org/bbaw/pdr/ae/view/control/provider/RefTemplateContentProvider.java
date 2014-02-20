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
package org.bbaw.pdr.ae.view.control.provider;

import java.util.Collections;
import java.util.HashMap;
import java.util.Vector;

import org.bbaw.pdr.ae.model.ReferenceModsTemplate;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * The Class RefTemplateContentProvider.
 * @author Christoph Plutte
 */
public class RefTemplateContentProvider implements IStructuredContentProvider
{

	/** The editing. */
	private boolean _editing = false;

	/**
	 * Instantiates a new ref template content provider.
	 * @param editing the editing
	 */
	public RefTemplateContentProvider(final boolean editing)
	{
		this._editing = editing;
	}

	@Override
	public void dispose()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public final Object[] getElements(final Object inputElement)
	{
		if (inputElement instanceof HashMap<?, ?>)
		{
			@SuppressWarnings("unchecked")
			HashMap<String, ReferenceModsTemplate> map = (HashMap<String, ReferenceModsTemplate>) inputElement;
			Vector<ReferenceModsTemplate> templates = new Vector<ReferenceModsTemplate>(map.size());
			if (_editing)
			{
				for (ReferenceModsTemplate template : map.values())
				{
					templates.add(template);
				}
			}
			else
			{
				for (ReferenceModsTemplate template : map.values())
				{
					if (!template.isIgnore())
					{
						templates.add(template);
					}
				}
			}
			Collections.sort(templates);
			ReferenceModsTemplate[] tems = templates.toArray(new ReferenceModsTemplate[templates.size()]);
			return tems;

		}
		return null;
	}

	@Override
	public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput)
	{
		// TODO Auto-generated method stub

	}

}
