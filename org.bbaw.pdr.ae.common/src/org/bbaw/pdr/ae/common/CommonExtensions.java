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
package org.bbaw.pdr.ae.common;

import org.bbaw.pdr.ae.common.interfaces.IFileSaveAndLoadFactory;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;

/** Extension class to get common fileSaveAndLoadFactory-extension.
 * @author plutte
 *
 */
public final class CommonExtensions
{
	/** FileSave and Load factory Interface.	 */
	private static IFileSaveAndLoadFactory fileSaveAndLoadFactory;
	/** FileSave and Load factory ID.	 */
	private static final String FILESAVEANDLOADFACTORY_ID = "org.bbaw.pdr.ae.common.fileSaveAndLoad";
	/** get Implementation of fileSaveAndLoadFactory.
	 * @return implementation of factory.
	 */
	public static IFileSaveAndLoadFactory getFileSaveAndLoadFactory()
	{
		if (fileSaveAndLoadFactory == null)
		{
			IConfigurationElement[] factory = Platform.getExtensionRegistry()
			.getConfigurationElementsFor(FILESAVEANDLOADFACTORY_ID);
			try
			{
				for (IConfigurationElement e : factory)
				{
					final Object o = e.createExecutableExtension("class");
					if (o instanceof IFileSaveAndLoadFactory)
					{
						fileSaveAndLoadFactory = (IFileSaveAndLoadFactory) o;
						return fileSaveAndLoadFactory;
					}
					else
					{
//						System.out.println("no factory");
					}
				}
			}
			catch (CoreException ex)
			{
//				System.out.println("CoreException");
//				System.out.println(ex.getMessage());
			}

		}
		else
		{
			return fileSaveAndLoadFactory;
		}
		return fileSaveAndLoadFactory;
	}

	/**
	 * constructor.
	 */
	private CommonExtensions()
	{
	}
}
