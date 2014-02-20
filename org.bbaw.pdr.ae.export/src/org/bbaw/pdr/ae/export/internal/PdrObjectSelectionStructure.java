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
package org.bbaw.pdr.ae.export.internal;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Vector;

import org.bbaw.pdr.ae.export.pluggable.AeExportCoreProvider;
import org.bbaw.pdr.ae.metamodel.PdrId;
import org.bbaw.pdr.ae.model.Aspect;
import org.bbaw.pdr.ae.model.PdrObject;
import org.bbaw.pdr.ae.model.view.OrderingHead;
import org.bbaw.pdr.ae.view.control.PDRObjectsProvider;

@Deprecated
public class PdrObjectSelectionStructure {
	
	private PDRObjectsProvider provider;
	private PdrObject[] pdrObjects;
	
	
	public PdrObjectSelectionStructure() {
		System.out.println("  setting up virtual structure for pdr object selection");
		this.provider = AeExportCoreProvider.getInstance().getPdrObjectsProvider();
		this.pdrObjects = AeExportCoreProvider.getInstance().getPdrObjects();
		this.setInput(pdrObjects);
/*		System.out.println("   pdr objects provider ready:");
		System.out.println("    "+(this.provider != null));*/
	}
	
	public void setInput(PdrObject[] pdrObjects) {
//		System.out.println("  updating pdr objects virtual structure:");
		this.pdrObjects = pdrObjects;
//		System.out.println("   passing pdr objects as input to our pdr objects provider");
		this.provider.setInput(pdrObjects);
/*		System.out.println("   # of pdr objects: "+this.pdrObjects.length);
		System.out.println("   can we retrieve any arranged aspect objects from our pdr"+
							" objects provider?");*/
/*		System.out.println("    "+(this.provider.getArrangedAspects() != null));
		try {
			System.out.println("   # of arrangements of aspects known to object provider: "+
								this.provider.getArrangedAspects().size());
		} catch (Exception e) {
			System.out.println("     retrieving aspect objects from pdr objects"+
								" provider failed!");
			System.out.println("      "+e);
		}*/
	}
	
	public PdrObject[] getObjects() {
		return this.pdrObjects;
	}
	
	public PdrObject[] getChildren(PdrObject parent) {
//		System.out.println("parent is of class: "+parent.getClass().getCanonicalName());
//		System.out.println("    identify child nodes of pdr object: "
//								+parent.getDisplayNameWithID());
		PdrId pdrId = parent.getPdrId();
		if (pdrId != null)
			if (pdrId.isValid()) {
//				System.out.println("     pdr id is valid: "+pdrId.getId());
				String pdrType = pdrId.getType();
				if (pdrType.equals("pdrPo")) {
//					System.out.println("     object is of type pdr person.");
					return getPersonsAspects(parent);
				}
			}
		return null;
	}
	
	public PdrObject[] getPersonsAspects(PdrObject person) {
//		System.out.println("      identify aspects of person: "+person.getDisplayNameWithID());
		Vector<PdrObject> results = new Vector<PdrObject>();
		HashSet<PdrId> aspectIds = person.getAspectIds();
/*		System.out.println("      aspects likely to have the ids: "+
						aspectIds.toArray(new PdrId[aspectIds.size()]));*/
		Vector<OrderingHead> arrangedAspects = this.provider.getArrangedAspects();
		if (arrangedAspects != null) {
//			System.out.println("     aspects known to be arranged in sets: "+arrangedAspects.size());
			for (OrderingHead arrangement : arrangedAspects) {
				Vector<Aspect> aspects = arrangement.getAspects();
/*				System.out.println("       processing set with as many aspects as: "+
									aspects.size());*/
				for (Aspect aspect : aspects) {
					if (aspectIds.contains(aspect.getPdrId())) {
//						System.out.println("      id of this aspect is one we are looking for: "
//										+aspect.getPdrId().getId());
						results.add((PdrObject)aspect);
					}
				}
			}
		} //else
//			System.out.println("      No Aspect groups available for person: "+
//					person.getDisplayNameWithID());
//		System.out.println("      return aspects: "+results.size());
		return results.toArray(new PdrObject[results.size()]);
	}
	/**
	 * Tries to identify the parent of this element according to the 
	 * deliberite PDR object hierarchy used in this model 
	 * @param element hopefully a {@link PdrObject}
	 * @return the Person object the element describes when it is an Aspect,
	 * otherwise the element itself 
	 */
	public PdrObject getParent(Object element) {
		PdrObject pdrObj = (PdrObject)element;
		if (pdrObj instanceof Aspect) {
			for (PdrObject person : pdrObjects)
				if (Arrays.asList(getPersonsAspects(person)).contains(pdrObj))
					return person;
		}
		return pdrObj;
	}

}
