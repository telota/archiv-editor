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
package org.bbaw.pdr.ae.view.network.internal;

import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;

import org.bbaw.pdr.ae.common.NLMessages;
import org.bbaw.pdr.ae.model.Aspect;
import org.bbaw.pdr.ae.model.Relation;
import org.bbaw.pdr.ae.model.RelationStm;
import org.bbaw.pdr.ae.model.view.OrderingHead;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class PersonAspectRelationContentProvider implements IStructuredContentProvider
{

	@Override
	public void dispose()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
	{


	}

	/**
	 * Returns the elements in the input, which must be either an array or a
	 * <code>Collection</code>.
	 */
	public Object[] getElements(Object inputElement)
	{
		if (inputElement instanceof Object[])
		{
			return (Object[]) inputElement;
		}
		if (inputElement instanceof Collection)
		{
			if (inputElement instanceof Vector<?>)
			{
				Vector<OrderingHead> arrangedAspects = (Vector<OrderingHead>) inputElement;
				Vector<String> columnValues = new Vector<String>(arrangedAspects.size() + 1);
				for (int i = 0; i <= arrangedAspects.size(); i++)
				{
					if (i == 0)
					{
						columnValues.add(NLMessages.getString("Editor_subject") + " \\ "
								+ NLMessages.getString("Editor_object"));
					}
					else
					{
						OrderingHead oh = arrangedAspects.get(i - 1);
						columnValues.add(oh.getValue());
					}
				}

				Object[] rows = new Object[arrangedAspects.size()];
				HashMap<String, Vector<Aspect>> bucketList;

				for (int i = 0; i < arrangedAspects.size(); i++)
				{

					OrderingHead oh = arrangedAspects.get(i);
					bucketList = new HashMap<String, Vector<Aspect>>();
					Object[] columns = new Object[arrangedAspects.size() + 1];
					for (Aspect a : oh.getAspects())
					{
						if (a.getRelationDim() != null && a.getRelationDim().getRelationStms() != null
								&& a.getRelationDim().getRelationStms().size() > 0)
						{
							for (RelationStm rStm : a.getRelationDim().getRelationStms())
							{
								if (!rStm.getSubject().toString().equals(oh.getValue()))
								{
									addToBucketList(bucketList, a, rStm.getSubject().toString());
								}
								if (rStm.getRelations() != null && rStm.getRelations().size() > 0)
								{
									for (Relation rel : rStm.getRelations())
									{
										if (!rel.getObject().toString().equals(oh.getValue()))
										{
											addToBucketList(bucketList, a, rel.getObject().toString());
										}
									}
								}
							}

						}
					}
					for (int j = 0; j <= arrangedAspects.size(); j++)
					{
						if (j == 0)
						{
							columns[0] = oh.getLabel();
						}
						else
						{
							String id = columnValues.get(j);
							Object o = bucketList.get(id);
							if (o != null)
							{
								columns[j] = o;
							}
							else
							{
								columns[j] = new Object();
							}
						}
					}
					rows[i] = columns;


				}
				return rows;

			}
		}
		return new Object[0];
	}

	private void addToBucketList(HashMap<String, Vector<Aspect>> bucketList, Aspect a, String id)
	{
		Vector<Aspect> aspects = bucketList.get(id);
		if (aspects == null)
		{
			aspects = new Vector<Aspect>(2);
			bucketList.put(id, aspects);
		}
		if (!aspects.contains(a))
		{
			aspects.add(a);
		}
	}
}
