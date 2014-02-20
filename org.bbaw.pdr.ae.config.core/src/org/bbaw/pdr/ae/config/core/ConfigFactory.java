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
package org.bbaw.pdr.ae.config.core;

import org.bbaw.pdr.ae.common.AEPluginIDs;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;

/**
 * A factory for creating Config objects.
 * @author Christoph Plutte
 */
public class ConfigFactory
{
	// This is the ID from your extension point
	/** The Constant ICONFIGMANAGER_ID. */
	private static final String ICONFIGMANAGER_ID = "org.bbaw.pdr.ae.config.configManager";

	/** The Constant ICONFIGFACADE_ID. */
	private static final String ICONFIGFACADE_ID = "org.bbaw.pdr.ae.config.configFacade";


	/**
	 * Creates a new Config object.
	 *
	 * @return the i config manager
	 */
	public static final IConfigManager createConfigManager()
	{
		IConfigurationElement[] config = Platform.getExtensionRegistry()
				.getConfigurationElementsFor(ICONFIGMANAGER_ID);
		try
		{
			for (IConfigurationElement e : config)
			{
				// System.out.println("Evaluating extension config factory");
				final Object o = e.createExecutableExtension("class");
				if (o instanceof IConfigManager)
				{
					return (IConfigManager) o;
				}
			}
		}
		catch (CoreException ex)
		{
			// System.out.println(ex.getMessage());
		}
		return null;
	}

	/**
	 * Gets the config facade.
	 *
	 * @return the config facade
	 */
	public final static IConfigFacade getConfigFacade()
	{
		IConfigurationElement[] config = Platform.getExtensionRegistry()
				.getConfigurationElementsFor(ICONFIGFACADE_ID);
		try
		{
			for (IConfigurationElement e : config)
			{
				// System.out.println("Evaluating extension config factory");
				final Object o = e.createExecutableExtension("class");
				if (o instanceof IConfigFacade)
				{
					return (IConfigFacade) o;
				}
			}
		}
		catch (CoreException ex)
		{
			// System.out.println(ex.getMessage());
		}
		return null;
	}

	public static IAEMarkupTemplateConfigEditor getMarkupTemplateConfigEditor()
	{
		IConfigurationElement[] config = Platform.getExtensionRegistry().getConfigurationElementsFor(
				AEPluginIDs.EXTENSION_ASPECT_SEMANTIC_TEMPLATE_CONFIGEDITOR);
		try
		{
			for (IConfigurationElement e : config)
			{
				// System.out.println("Evaluating extension config factory");
				final Object o = e.createExecutableExtension("class");
				if (o instanceof IAEMarkupTemplateConfigEditor)
				{
					return (IAEMarkupTemplateConfigEditor) o;
				}
			}
		}
		catch (CoreException ex)
		{
			// System.out.println(ex.getMessage());
		}
		return null;
	}

}
