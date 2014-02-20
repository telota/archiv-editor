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
package org.bbaw.pdr.ae.view.identifiers.internal;

import java.util.HashMap;

import org.bbaw.pdr.ae.view.identifiers.interfaces.IConcurrenceSearchService;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;

public final class IdentifiersExtension
{

	private static final String CONCURRENCE_SEARCH_SERVICE_ID = "org.bbaw.pdr.ae.identifier.ConcurrenceSearchService";
	private static HashMap<String, IConcurrenceSearchService> searchServices;

	/**
	 * get Implementation of fileSaveAndLoadFactory.
	 * @return implementation of factory.
	 */
	public static HashMap<String, IConcurrenceSearchService> getConcurrenceSearchServices()
	{
		if (searchServices == null)
		{
			searchServices = new HashMap<String, IConcurrenceSearchService>();
			IConfigurationElement[] factory = Platform.getExtensionRegistry().getConfigurationElementsFor(
					CONCURRENCE_SEARCH_SERVICE_ID);
			try
			{
				for (IConfigurationElement e : factory)
				{
					final Object o = e.createExecutableExtension("class");
					if (o instanceof IConcurrenceSearchService)
					{
						IConcurrenceSearchService service = (IConcurrenceSearchService) o;
						searchServices.put(service.getLabel(), service);
					}
					else
					{
					}
				}
			}
			catch (CoreException ex)
			{
			}

		}
		if (!searchServices.isEmpty())
		{
			return searchServices;
		}
		else
		{
			return null;
		}
	}

	/**
	 * constructor.
	 */
	private IdentifiersExtension()
	{
	}
}
