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
import org.bbaw.pdr.ae.model.ReferenceMods;
import org.bbaw.pdr.ae.model.ValidationStm;
import org.bbaw.pdr.ae.model.view.OrderingHead;
import org.bbaw.pdr.ae.view.control.PDRObjectsOrderer;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * The Class AspectsByReferenceOrderer.
 * @author Christoph Plutte
 */
public class AspectsByReferenceOrderer implements PDRObjectsOrderer
{

	/** The references. */
	private HashSet<String> _references;

	/** The ordered objects. */
	private HashMap<String, OrderingHead> _orderedObjects;

	/**
	 * Instantiates a new aspects by reference orderer.
	 */
	public AspectsByReferenceOrderer()
	{
	}

	@Override
	public final Vector<OrderingHead> orderedObjects(final Vector<Aspect> aspects, IProgressMonitor monitor)
	{
		_references = new HashSet<String>();
		_orderedObjects = new HashMap<String, OrderingHead>();
		HashSet<PdrId> chosenAspects = new HashSet<PdrId>();
		HashSet<Aspect> rejectedAspets = new HashSet<Aspect>();
		Aspect a;
		OrderingHead oh = null;
		String reference = null;
		ValidationStm valStm;
		for (int i = 0; i < aspects.size(); i++)
		{
			a = aspects.get(i);
			if (a.getValidation() != null && !a.getValidation().getValidationStms().isEmpty())
			{
				for (int j = 0; j < a.getValidation().getValidationStms().size(); j++)
				{
					valStm = a.getValidation().getValidationStms().get(j);
					if (valStm.getReference() != null && valStm.getReference().getSourceId() != null)
					{
						reference = valStm.getReference().getSourceId().toString();
					}
					else
					{
						reference = "undefined";
					}
					if (!_references.contains(reference))
					{
						_references.add(reference);
						oh = new OrderingHead();
						String label;
						ReferenceMods ref = Facade.getInstanz().getReference(new PdrId(reference));
						if (ref != null)
						{
							label = ref.getDisplayName();
						}
						else
						{
							label = reference;
						}
						if (label.length() > 17)
						{
							label = label.substring(0, 15) + "...";
						}
						oh.setLabel(label);
						oh.setValue(reference);
						oh.setImageString(IconsInternal.REFERENCE);
						_orderedObjects.put(reference, oh);
					}
					_orderedObjects.get(reference).addAspect(a);
					chosenAspects.add(a.getPdrId());
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
			oh.setLabel(NLMessages.getString("Orderer_without_reference"));
			oh.setValue("uncategorized");
			oh.getAspects().addAll(rejectedAspets);
			_orderedObjects.put(oh.getValue(), oh);
		}
		Vector<OrderingHead> result = new Vector<OrderingHead>(_orderedObjects.values());
		Collections.sort(result, new CronOrderComparator());
		return result;
	}

}
