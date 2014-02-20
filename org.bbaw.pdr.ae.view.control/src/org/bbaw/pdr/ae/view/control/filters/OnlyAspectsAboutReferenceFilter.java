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

import java.util.Vector;

import org.bbaw.pdr.ae.common.interfaces.AEFilter;
import org.bbaw.pdr.ae.model.Aspect;
import org.bbaw.pdr.ae.model.Relation;
import org.bbaw.pdr.ae.model.RelationStm;

/**
 * The Class AspectExcludeObjectRelationsFilter.
 * @author Christoph Plutte
 */
public class OnlyAspectsAboutReferenceFilter implements AEFilter
{

	/** The person ids. */
	private Vector<String> _objectIds;

	/**
	 * Instantiates a new aspect exclude object relations filter.
	 * @param personIds the person ids
	 */
	public OnlyAspectsAboutReferenceFilter(final Vector<String> objectIds)
	{
		this.setObjectIds(objectIds);
	}

	/**
	 * Gets the person ids.
	 * @return the person ids
	 */
	public final Vector<String> getObjectIds()
	{
		return _objectIds;
	}

	@Override
	public final boolean select(final Object element)
	{
		if (_objectIds != null)
		{
			RelationStm relationStm;
			if (element instanceof Aspect)
			{
				Aspect a = (Aspect) element;
				if (a.getRelationDim() != null && !a.getRelationDim().getRelationStms().isEmpty())
				{
					for (int j = 0; j < a.getRelationDim().getRelationStms().size(); j++)
					{
						relationStm = a.getRelationDim().getRelationStms().get(j);
						if (relationStm.getSubject() != null)
						{
							for (String s : _objectIds)
							{
								if (s.startsWith("pdrRo") && s.equals(relationStm.getSubject().toString()))
								{
									return true;
								}
							}

							if (a.getPdrId().equals(relationStm.getSubject()) && relationStm.getRelations() != null)
							{
								for (Relation rel : relationStm.getRelations())
								{
									if (rel.getObject() != null)
									{
										for (String s : _objectIds)
										{
											if (s.startsWith("pdrRo") && s.equals(rel.getObject().toString()))
											{
												return true;
											}
										}

									}
								}
							}
						}

					}
					return false;
				}
			}
			return false;
		}

		else
		{
			return true;
		}
	}

	/**
	 * Sets the person ids.
	 * @param personIds the new person ids
	 */
	public final void setObjectIds(final Vector<String> objectIds)
	{
		this._objectIds = objectIds;
	}

}
