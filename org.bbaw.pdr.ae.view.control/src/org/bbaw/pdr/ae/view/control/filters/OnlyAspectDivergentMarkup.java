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
import org.bbaw.pdr.ae.control.core.PDRConfigProvider;
import org.bbaw.pdr.ae.model.Aspect;
import org.bbaw.pdr.ae.model.PdrObject;
import org.bbaw.pdr.ae.model.TaggingRange;
import org.bbaw.pdr.ae.model.view.TreeNode;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

/**
 * The Class OnlyAspectDivergentMarkup.
 * @author Christoph Plutte
 */
public class OnlyAspectDivergentMarkup extends ViewerFilter implements AEFilter
{

	/**
	 * Markup divergent.
	 * @param a the a
	 * @return true, if successful
	 */
	private boolean markupDivergent(final Aspect a)
	{
		if (a.getNotification() != null && a.getNotification().trim().length() > 0 && a.getRangeList() != null
				&& !a.getRangeList().isEmpty() && a.getRangeList().size() > 1)
		{
			String content = "";
			int start = 0;
			int end = 0;
			boolean divergent = false;
			boolean div = true;
			for (TaggingRange tr : a.getRangeList())
			{
				start = tr.getStart();
				end = start + tr.getLength();
				div = true;
				if (a.getNotification().length() <= end)
				{
					content = a.getNotification().substring(start, end);
					if (tr.getType() != null)
					{
						if (tr.getType().equalsIgnoreCase(content)
								|| PDRConfigProvider.getLabelOfMarkup(tr.getName(), tr.getType(), null, null)
										.equalsIgnoreCase(content))
						{
							div = false;
						}
					}
					if (tr.getType() != null && tr.getSubtype() != null)
					{
						if (tr.getSubtype().equalsIgnoreCase(content)
								|| PDRConfigProvider
										.getLabelOfMarkup(tr.getName(), tr.getType(), tr.getSubtype(), null)
										.equalsIgnoreCase(content))
						{
							div = false;
						}
					}
					if (tr.getType() != null && tr.getSubtype() != null && tr.getRole() != null)
					{
						if (tr.getRole().equalsIgnoreCase(content)
								|| PDRConfigProvider.getLabelOfMarkup(tr.getName(), tr.getType(), tr.getSubtype(),
										tr.getRole()).equalsIgnoreCase(content))
						{
							div = false;
						}
					}
				}
				if (div)
				{
					divergent = true;
				}
			}
			return divergent;
		}
		return false;
	}

	@Override
	public final boolean select(final Object object)
	{
		if (object instanceof Aspect)
		{
			Aspect a = (Aspect) object;
			return markupDivergent(a);
		}
		return false;
	}

	@Override
	public final boolean select(final Viewer viewer, final Object parentElement, final Object element)
	{
		if (element instanceof TreeNode)
		{
			TreeNode tn = (TreeNode) element;
			if (tn.getPdrObject() != null)
			{
				PdrObject obj = tn.getPdrObject();
				if (obj instanceof Aspect)
				{
					Aspect a = (Aspect) obj;
					return markupDivergent(a);
				}
				else
				{
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
		else if (element instanceof PdrObject)
		{
			if (element instanceof Aspect)
			{
				Aspect a = (Aspect) element;
				return markupDivergent(a);
			}
			else
			{
				return true;
			}
		}
		return false;
	}

}
