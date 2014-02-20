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

import org.bbaw.pdr.ae.model.Person;
import org.bbaw.pdr.ae.model.view.TreeNode;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

/**
 * The Class PersonConcurrenceFilter.
 * @author Christoph Plutte
 */
public class PersonConcurrenceFilter extends ViewerFilter
{

	/**
	 * Checks for child concurrence.
	 * @param tn the tn
	 * @return true, if successful
	 */
	private boolean hasChildConcurrence(final TreeNode tn)
	{
		if (tn.getPdrObject() != null && (tn.getPdrObject() instanceof Person))
		{
			Person p = (Person) tn.getPdrObject();
			return (p.getConcurrences() != null && !p.getConcurrences().getConcurrences().isEmpty());

		}
		else if (tn.hasChildren())
		{
			for (TreeNode tn2 : tn.getChildren())
			{
				return hasChildConcurrence(tn2);
			}
		}
		return false;
	}

	@Override
	public final boolean select(final Viewer viewer, final Object parentElement, final Object element)
	{
		if (element instanceof TreeNode)
		{
			TreeNode tn = (TreeNode) element;
			if (tn.getPdrObject() != null && (tn.getPdrObject() instanceof Person))
			{
				Person p = (Person) tn.getPdrObject();
				if (p.getConcurrences() != null && !p.getConcurrences().getConcurrences().isEmpty())
				{
					return true;
				}
				else
				{
					return false;
				}
			}
			else if (tn.hasChildren())
			{
				return hasChildConcurrence(tn);
			}
		}
		else if (element instanceof Person)
		{
			Person p = (Person) element;
			if (p.getConcurrences() != null && !p.getConcurrences().getConcurrences().isEmpty())
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		return false;
	}

}
