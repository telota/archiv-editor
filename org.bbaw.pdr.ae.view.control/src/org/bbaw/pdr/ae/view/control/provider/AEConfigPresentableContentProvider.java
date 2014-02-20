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

import java.util.Arrays;
import java.util.HashMap;

import org.bbaw.pdr.ae.config.model.ConfigData;
import org.bbaw.pdr.ae.metamodel.IAEPresentable;
import org.bbaw.pdr.ae.model.ReferenceModsTemplate;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * The Class AEConfigPresentableContentProvider.
 * @author Christoph Plutte
 */
public class AEConfigPresentableContentProvider implements IStructuredContentProvider
{

	/** The _add all. */
	private boolean _addALL;

	/**
	 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
	 */
	@Override
	public void dispose()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public final Object[] getElements(final Object inputElement)
	{
		if (inputElement != null && inputElement instanceof HashMap<?, ?>)
		{
			@SuppressWarnings("unchecked")
			HashMap<String, ConfigData> children = (HashMap<String, ConfigData>) inputElement;
			for (String k : children.keySet())
			{
				if (children.get(k) instanceof IAEPresentable)
				{
					IAEPresentable[] cps = null;
					int i;

					if (_addALL)
					{
						cps = new IAEPresentable[children.size() + 1];
						ReferenceModsTemplate ciAll = new ReferenceModsTemplate();
						ciAll.setValue("ALL");
						ciAll.setLabel("ALL");
						ciAll.setIgnore(false);
						cps[0] = ciAll;
						i = 1;
					}
					else
					{
						cps = new IAEPresentable[children.size()];
						i = 0;
					}

					for (String key : children.keySet())
					{
						IAEPresentable cp = children.get(key);
						cps[i] = cp;
						i++;

					}
					Arrays.sort(cps);
					return cps;
				}

				break;
			}
		}

		return null;
	}

	@Override
	public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput)
	{
		// TODO Auto-generated method stub

	}

	/**
	 * Sets the adds the all.
	 * @param addALL the new adds the all
	 */
	public final void setAddALL(final boolean addALL)
	{
		this._addALL = addALL;
	}

}
