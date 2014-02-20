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
package org.bbaw.pdr.ae.model.view;

import java.util.Vector;

import org.bbaw.pdr.ae.model.PdrObject;
import org.bbaw.pdr.ae.model.search.PdrQuery;

/**
 * The Class FacetResultNode.
 * @author Christoph Plutte
 */
public class FacetResultNode extends TreeNode implements Comparable<FacetResultNode>
{

	/** The pdr query. */
	private PdrQuery _pdrQuery;

	/** The objects. */
	private Vector<PdrObject> _objects;

	/**
	 * Instantiates a new facet result node.
	 * @param id the id
	 * @param type the type
	 */
	public FacetResultNode(final String id, final String type)
	{
		super(id, type);
	}

	@Override
	public final int compareTo(final FacetResultNode o)
	{
		if (this.getId() != null && o.getId() != null)
		{
			return this.getId().compareToIgnoreCase(o.getId());
		}
		else if (this.getId() != null)
		{
			return -1;
		}
		else if (o.getId() != null)
		{
			return 1;
		}
		return 0;
	}

	/**
	 * Gets the objects.
	 * @return the objects
	 */
	public final Vector<PdrObject> getObjects()
	{
		return _objects;
	}

	@Override
	public final PdrQuery getPdrQuery()
	{
		return _pdrQuery;
	}

	/**
	 * Sets the objects.
	 * @param objects the new objects
	 */
	@SuppressWarnings("unchecked")
	public final void setObjects(final Vector<? extends PdrObject> objects)
	{
		if (objects instanceof Vector<?> && objects != null && !objects.isEmpty()
				&& objects.firstElement() instanceof PdrObject)
		{
			this._objects = (Vector<PdrObject>) objects;
		}
	}

	@Override
	public final void setPdrQuery(final PdrQuery pdrQuery)
	{
		this._pdrQuery = pdrQuery;
	}

}
