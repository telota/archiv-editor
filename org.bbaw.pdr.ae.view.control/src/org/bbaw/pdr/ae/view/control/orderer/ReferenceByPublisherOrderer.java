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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;

import org.bbaw.pdr.ae.common.icons.IconsInternal;
import org.bbaw.pdr.ae.control.comparator.CronOrderComparator;
import org.bbaw.pdr.ae.metamodel.PdrId;
import org.bbaw.pdr.ae.model.ReferenceMods;
import org.bbaw.pdr.ae.model.view.OrderingHead;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * The Class ReferenceByPublisherOrderer.
 * @author Christoph Plutte
 */
public class ReferenceByPublisherOrderer implements ReferencesOrderer
{

	/** The publishers. */
	private ArrayList<String> _publishers;

	/** The ordered objects. */
	private HashMap<String, OrderingHead> _orderedObjects;

	/**
	 * Instantiates a new reference by publisher orderer.
	 */
	public ReferenceByPublisherOrderer()
	{
	}

	@Override
	public final Vector<OrderingHead> orderedReferences(final Vector<ReferenceMods> references, IProgressMonitor monitor)
	{
		_publishers = new ArrayList<String>();
		_orderedObjects = new HashMap<String, OrderingHead>();
		HashSet<PdrId> chosenRefs = new HashSet<PdrId>();
		Vector<ReferenceMods> rejectedRefs = new Vector<ReferenceMods>();
		OrderingHead oh = null;
		String publisher = null;
		for (ReferenceMods reference : references)
		{
			if (reference.getOriginInfo() != null && reference.getOriginInfo().getPublisher() != null)
			{
				publisher = reference.getOriginInfo().getPublisher();
				if (!_publishers.contains(publisher))
				{
					_publishers.add(publisher);
					oh = new OrderingHead();
					String label = publisher;
					if (label.length() > 17)
					{
						label = label.substring(0, 15) + "...";
					}
					oh.setLabel(label);
					oh.setValue(publisher);
					oh.setImageString(IconsInternal.REFERENCE);
					_orderedObjects.put(publisher, oh);
				}
				_orderedObjects.get(publisher).getReferences().add(reference);
				chosenRefs.add(reference.getPdrId());
			}
			else
			{
				rejectedRefs.add(reference);
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

		if (!rejectedRefs.isEmpty())
		{
			oh = new OrderingHead();
			oh.setLabel("uncategorized");
			oh.setValue("uncategorized");
			oh.setReferences(rejectedRefs);
			_orderedObjects.put(oh.getValue(), oh);
		}
		Vector<OrderingHead> result = new Vector<OrderingHead>(_orderedObjects.values());
		Collections.sort(result, new CronOrderComparator());
		return result;
	}

}
