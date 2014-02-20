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
import java.util.Vector;

import org.bbaw.pdr.ae.common.NLMessages;
import org.bbaw.pdr.ae.common.icons.IconsInternal;
import org.bbaw.pdr.ae.control.comparator.CronOrderComparator;
import org.bbaw.pdr.ae.model.Aspect;
import org.bbaw.pdr.ae.model.Place;
import org.bbaw.pdr.ae.model.SpatialStm;
import org.bbaw.pdr.ae.model.view.OrderingHead;
import org.bbaw.pdr.ae.view.control.PDRObjectsOrderer;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * The Class AspectsByPlaceOrderer.
 * @author Christoph Plutte, Jakob Höper
 */
public class AspectsByPlaceOrderer implements PDRObjectsOrderer
{

	/** The ordered objects. */
	private HashMap<String, OrderingHead> _orderedObjects;

	/**
	 * Instantiates a new aspects by place orderer.
	 */
	public AspectsByPlaceOrderer()
	{
	}

	@Override
	public Vector<OrderingHead> orderedObjects(final Vector<Aspect> aspects, IProgressMonitor monitor) {
		Vector<Aspect> independents = new Vector<Aspect>(aspects);
		_orderedObjects = new HashMap<String, OrderingHead>();
		// process aspect list
		for (Aspect a : aspects) {
			Vector<String> labels = retrieveLabels(a);
			if (labels.size()>0) {
				independents.remove(a);
				OrderingHead group;
				for (String label : labels) {
					if (_orderedObjects.containsKey(label)) {
						 group = _orderedObjects.get(label);
					} else {
						group = new OrderingHead(label);
						 _orderedObjects.put(label, group);
						group.setImageString(IconsInternal.PLACE);
					}
					group.addAspect(a);
				}
			}
		}		
		// aggregate all aspects that couldn't be assigned to any labels
		if (independents.size()<1) {
			String type = NLMessages.getString("Orderer_uncategorized");
			OrderingHead group;
			if (_orderedObjects.containsKey(type)) {
				group = _orderedObjects.get(type);
			} else {
				group = new OrderingHead(type);
				 _orderedObjects.put(type, group);
				group.setImageString(IconsInternal.PLACE);
			}
			group.addAll(independents);
		}
		// return list of ordering head nodes sorted chronologically
		Vector<OrderingHead> result = new Vector<OrderingHead>(_orderedObjects.values());
		Collections.sort(result, new CronOrderComparator());
		//for (OrderingHead h : result)
			//System.out.println("spatial category: "+h.getLabel()+" ("+h.getValue()+")");
		return result;
	}
	
	
	/**
	 * Attempts to find one or more labels by which a given aspect can be classified.
	 * Prefered labels are names of places referred by this aspect. On failure on 
	 * getting place names, try attributes of spatial statement instead. 
	 * @return
	 */
	private Vector<String> retrieveLabels(Aspect a) {
		Vector<String> labels = new Vector<String>();
		if (a.getSpatialDim() != null && a.getSpatialDim().getSpatialStms() != null) 
			for (SpatialStm stm : a.getSpatialDim().getSpatialStms())
				// if this aspect refers to any places, classify it accordingly
				if (stm.getPlaces() != null && stm.getPlaces().size() > 0) {
					for (Place place : stm.getPlaces()) {
						String name = place.getPlaceName();
						// if no name for a referred place can retrieved, try alternatives
						if (name == null || name.length() < 1) {
							name = place.getKey();
							if (name == null) {
								name = place.getSubtype();
								if (name == null) {
									name = place.getType();
									if (name == null)
										name = "undefined_place";
								}
							}
						}
						if (name != null && name.trim().length()>0)
							labels.add(name);
					}
				} else if (stm.getType() != null){
					// if no places are referenced, but a spatial statement is present, use
					// type information of spatial statement as classifier
					String type = NLMessages.getString("Editor_spatialdim_types_" + stm.getType());
					if (type == null) 
						type = stm.getType();
					if (type != null && type.trim().length()>0)
						labels.add(type);
				}
		return labels;
	}

}
