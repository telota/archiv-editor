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
package org.bbaw.pdr.ae.view.control;

import org.bbaw.pdr.ae.view.control.orderer.AspectByYearOrderer;
import org.bbaw.pdr.ae.view.control.orderer.AspectsByMarkupOrderer;
import org.bbaw.pdr.ae.view.control.orderer.AspectsByPersonOrderer;
import org.bbaw.pdr.ae.view.control.orderer.AspectsByPlaceOrderer;
import org.bbaw.pdr.ae.view.control.orderer.AspectsByReferenceOrderer;
import org.bbaw.pdr.ae.view.control.orderer.AspectsByRelationOrderer;
import org.bbaw.pdr.ae.view.control.orderer.AspectsBySemanticOrderer;
import org.bbaw.pdr.ae.view.control.orderer.AspectsByUserOrderer;
import org.bbaw.pdr.ae.view.control.orderer.ReferenceByAuthorOrderer;
import org.bbaw.pdr.ae.view.control.orderer.ReferenceByCopyRightDateOrderer;
import org.bbaw.pdr.ae.view.control.orderer.ReferenceByDateCapturedOrderer;
import org.bbaw.pdr.ae.view.control.orderer.ReferenceByDateCreationOrderer;
import org.bbaw.pdr.ae.view.control.orderer.ReferenceByLocationOrderer;
import org.bbaw.pdr.ae.view.control.orderer.ReferenceByOriginPlaceOrderer;
import org.bbaw.pdr.ae.view.control.orderer.ReferenceByPublisherOrderer;
import org.bbaw.pdr.ae.view.control.orderer.ReferenceByTitleOrderer;
import org.bbaw.pdr.ae.view.control.orderer.ReferenceByUserOrderer;
import org.bbaw.pdr.ae.view.control.orderer.ReferencesByGenreOrderer;
import org.bbaw.pdr.ae.view.control.orderer.ReferencesOrderer;

/**
 * A factory for {@link PDRObjectsOrderer} instances.
 * @author Christoph Plutte
 */
public class PDROrdererFactory
{
	/**
	 * <ul><li>aspect.all</li>
	 * <li>aspect.semantic</li>
	 * <li>aspect.year</li>
	 * <li>aspect.place</li>
	 * <li>aspect.reference</li>
	 * <li>aspect.relation</li>
	 * <li>aspect.markup</li>
	 * <li>aspect.person</li>
	 * <li>aspect.user</li></ul> 
	 */
	public static final String[] ORDERER_IDs = new String[]{"aspect.all", "aspect.semantic", "aspect.year", "aspect.place",
		"aspect.reference", "aspect.relation", "aspect.markup", "aspect.user", "aspect.person"};

	/**
	 * Creates a new Aspects {@link PDRObjectsOrderer} instance of the respective
	 * implementation identified by the given String.
	 * @param id the id. Accepted arguments:
	 * <ul><li>aspect.semantic</li>
	 * <li>aspect.year</li>
	 * <li>aspect.place</li>
	 * <li>aspect.reference</li>
	 * <li>aspect.relation</li>
	 * <li>aspect.markup</li>
	 * <li>aspect.person</li>
	 * <li>aspect.user</li></ul>,
	 * which are also enlisted in the array {@link #ORDERER_IDs}.
	 * @return a new instance of the denoted {@link PDRObjectsOrderer} implementation,
	 * or null if identifier is unknown.
	 */
	public static final PDRObjectsOrderer createAspectOrderer(final String id)
	{
		String[] idc = id.split("\\.");
		if (idc.length == 2 && idc[0].equals("aspect")) {
			if (idc[1].equals("year")) {
				return new AspectByYearOrderer();
			}
			else if (idc[1].equals("markup")) {
				return new AspectsByMarkupOrderer();
			}
			else if (idc[1].equals("person")) {
				return new AspectsByPersonOrderer();
			}
			else if (idc[1].equals("place")) {
				return new AspectsByPlaceOrderer();
			}
			else if (idc[1].equals("reference")) {
				return new AspectsByReferenceOrderer();
			}
			else if (idc[1].equals("relation"))	{
				return new AspectsByRelationOrderer();
			}
			else if (idc[1].equals("semantic"))	{
				return new AspectsBySemanticOrderer();
			}
			else if (idc[1].equals("user"))	{
				return new AspectsByUserOrderer();
			}			
		}
		return null;
	}

	/**
	 * Creates a new References PDROrderer object.
	 * @param id the id
	 * @return the references orderer
	 */
	public final ReferencesOrderer createReferenceOrderer(final String id)
	{
		if (id.equals("reference.author"))
		{
			return new ReferenceByAuthorOrderer();
		}
		else if (id.equals("reference.date.capture"))
		{
			return new ReferenceByDateCapturedOrderer();
		}
		else if (id.equals("reference.date.creation"))
		{
			return new ReferenceByDateCreationOrderer();
		}
		else if (id.equals("reference.date.copyRight"))
		{
			return new ReferenceByCopyRightDateOrderer();
		}
		else if (id.equals("reference.location"))
		{
			return new ReferenceByLocationOrderer();
		}
		else if (id.equals("reference.origin.place"))
		{
			return new ReferenceByOriginPlaceOrderer();
		}
		else if (id.equals("reference.origin.publisher"))
		{
			return new ReferenceByPublisherOrderer();
		}
		else if (id.equals("reference.title"))
		{
			return new ReferenceByTitleOrderer();
		}
		else if (id.equals("reference.genre"))
		{
			return new ReferencesByGenreOrderer();
		}
		else if (id.equals("reference.user"))
		{
			return new ReferenceByUserOrderer();
		}

		return null;

	}

}
