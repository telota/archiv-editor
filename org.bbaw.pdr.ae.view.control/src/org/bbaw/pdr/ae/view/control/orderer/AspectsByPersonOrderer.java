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
package org.bbaw.pdr.ae.view.control.orderer;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;

import org.bbaw.pdr.ae.common.NLMessages;
import org.bbaw.pdr.ae.common.icons.IconsInternal;
import org.bbaw.pdr.ae.control.comparator.CronOrderComparator;
import org.bbaw.pdr.ae.control.facade.Facade;
import org.bbaw.pdr.ae.metamodel.PdrId;
import org.bbaw.pdr.ae.model.Aspect;
import org.bbaw.pdr.ae.model.PdrObject;
import org.bbaw.pdr.ae.model.Person;
import org.bbaw.pdr.ae.model.Relation;
import org.bbaw.pdr.ae.model.RelationStm;
import org.bbaw.pdr.ae.model.view.OrderingHead;
import org.bbaw.pdr.ae.view.control.PDRObjectsOrderer;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * The Class AspectsByPersonOrderer.
 * @author Christoph Plutte
 */
public class AspectsByPersonOrderer implements PDRObjectsOrderer
{

	/** The facade. */
	private Facade _facade = Facade.getInstanz();

	/** The objects. */
	private HashSet<String> _objects;

	/** The ordered objects. */
	private HashMap<String, OrderingHead> _orderedObjects;

	/**
	 * Instantiates a new aspects by person orderer.
	 */
	public AspectsByPersonOrderer()
	{
	}

	@Override
	public final Vector<OrderingHead> orderedObjects(final Vector<Aspect> aspects, IProgressMonitor monitor)
	{
		_objects = new HashSet<String>();
		_orderedObjects = new HashMap<String, OrderingHead>();
		HashSet<PdrId> chosenAspects = new HashSet<PdrId>();
		HashSet<Aspect> rejectedAspets = new HashSet<Aspect>();
		Aspect a;
		OrderingHead oh = null;
		String object = null;
		RelationStm relationStm;
		for (int i = 0; i < aspects.size(); i++)
		{
			a = aspects.get(i);
			if (a.getRelationDim() != null && !a.getRelationDim().getRelationStms().isEmpty())
			{
				for (int j = 0; j < a.getRelationDim().getRelationStms().size(); j++)
				{
					relationStm = a.getRelationDim().getRelationStms().get(j);
					if (!relationStm.getSubject().equals(a.getPdrId()))
					{
						if (!_objects.contains(object))
						{
							_objects.add(object);
							oh = new OrderingHead();
							String label = object;
							if (object.startsWith("pdrPo"))
							{
								Person p = _facade.getPerson(new PdrId(object));
								if (p != null)
								{
									label = p.getDisplayName();
								}
								else
								{
									label = object;
								}
								oh.setImageString(IconsInternal.PERSON);
							}
							else if (object.startsWith("pdrAo"))
							{
								oh.setImageString(IconsInternal.ASPECT);
								if (_facade.getLoadedAspects().containsKey(object))
								{
									Aspect aa = _facade.getLoadedAspects().get(object);
									if (aa != null)
									{
										label = aa.getDisplayName();
									}
									else
									{
										label = object;
									}
								}
							}
							if (label.length() > 17)
							{
								label = label.substring(0, 15) + "...";
							}
							oh.setLabel(label);
							oh.setValue(object);
							_orderedObjects.put(object, oh);
						}
						_orderedObjects.get(object).addAspect(a);
						chosenAspects.add(a.getPdrId());
					}
					if (relationStm.getRelations() != null)
					{
						for (Relation rel : relationStm.getRelations())
						{
							object = rel.getObject().toString();
							if (!_objects.contains(object))
							{
								_objects.add(object);
								oh = new OrderingHead();
								String label = object;
								PdrObject obj = _facade.getPdrObject(new PdrId(object));
								if (obj != null)
								{
									label = obj.getDisplayName();
									if (object.startsWith("pdrPo"))
									{
										oh.setImageString(IconsInternal.PERSON);
									}
									else if (object.startsWith("pdrAo"))
									{
										oh.setImageString(IconsInternal.ASPECT);
									}
								}
								else
								{
									label = object;
								}
								if (label.length() > 17)
								{
									label = label.substring(0, 15) + "...";
								}
								oh.setLabel(label);
								oh.setValue(object);
								_orderedObjects.put(object, oh);
							}
							_orderedObjects.get(object).addAspect(a);
							chosenAspects.add(a.getPdrId());
						}
					}
				}
			}
			else
			{
				rejectedAspets.add(a);
			}
			if (monitor != null)
			{
				monitor.worked(1);
				if (monitor.isCanceled())
				{
					break;
				}
			}
		}

		if (!rejectedAspets.isEmpty())
		{
			oh = new OrderingHead();
			oh.setLabel(NLMessages.getString("Orderer_uncategorized"));
			oh.setValue("uncategorized");
			oh.getAspects().addAll(rejectedAspets);
			_orderedObjects.put(oh.getValue(), oh);
		}
		Vector<OrderingHead> result = new Vector<OrderingHead>(_orderedObjects.values());
		Collections.sort(result, new CronOrderComparator());
		return result;
	}

}
