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

import java.util.HashMap;
import java.util.Vector;

import org.bbaw.pdr.ae.metamodel.PdrId;
import org.bbaw.pdr.ae.model.Aspect;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * The Class AspectTableContentProvider.
 * @author Christoph Plutte
 */
public class AspectTableContentProvider implements IStructuredContentProvider
{

	@Override
	public void dispose()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public final Object[] getElements(final Object inputElement)
	{
		if (((HashMap<?, ?>) inputElement) != null && !((HashMap<?, ?>) inputElement).isEmpty())
		{
			@SuppressWarnings("unchecked")
			HashMap<PdrId, Aspect> map = (HashMap<PdrId, Aspect>) inputElement;
			Vector<Aspect> aspects = new Vector<Aspect>(map.size());
			for (PdrId key : map.keySet())
			{
				if (map.get(key) instanceof Aspect)
				{
					aspects = new Vector<Aspect>(map.size());
					for (PdrId k : map.keySet())
					{
						aspects.add(map.get(k));
					}
					break;
				}
			}
			// String help [] = (String []) facets.toArray (new String
			// [facets.size ()]);
			Aspect[] asps = aspects.toArray(new Aspect[aspects.size()]);
			return asps;
		}
		else
		{
			return null;
		}

	}

	@Override
	public final void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput)
	{
		// TODO Auto-generated method stub

	}

}
