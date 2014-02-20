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

import org.bbaw.pdr.ae.model.Identifier;
import org.bbaw.pdr.ae.model.Person;
import org.bbaw.pdr.ae.model.view.TreeNode;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

/**
 * The Class PersonWithoutVIAFFilter.
 * @author Christoph Plutte
 */
public class PersonWithoutVIAFFilter extends ViewerFilter
{

	@Override
	public final boolean select(final Viewer viewer, final Object parentElement, final Object element)
	{
		if (element instanceof TreeNode)
		{
			TreeNode tn = (TreeNode) element;
			if (tn.getPdrObject() != null && (tn.getPdrObject() instanceof Person))
			{
				Person p = (Person) tn.getPdrObject();
				if (p.getIdentifiers() == null || p.getIdentifiers().getIdentifiers() == null
						|| p.getIdentifiers().getIdentifiers().isEmpty())
				{
					return true;
				}
				else
				{
					for (Identifier i : p.getIdentifiers().getIdentifiers())
					{
						if (i.getProvider().equals("VIAF"))
						{
							return false;
						}
					}
					return true;
				}

			}
			else if (tn.hasChildren())
			{
				for (TreeNode tn2 : tn.getChildren())
				{
					if (select(viewer, tn, tn2))
					{
						return true;
					}
				}
				return false;
			}
		}
		else if (element instanceof Person)
		{
			Person p = (Person) element;
			if (p.getIdentifiers() == null || p.getIdentifiers().getIdentifiers() == null
					|| p.getIdentifiers().getIdentifiers().isEmpty())
			{
				return true;
			}
			else
			{
				for (Identifier i : p.getIdentifiers().getIdentifiers())
				{
					if (i.getProvider().equals("VIAF"))
					{
						return false;
					}
				}
				return true;
			}
		}
		return false;
	}

}
