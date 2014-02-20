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

import java.util.HashMap;

import org.bbaw.pdr.ae.common.AEConstants;
import org.bbaw.pdr.ae.common.AEPluginIDs;
import org.bbaw.pdr.ae.common.CommonActivator;
import org.bbaw.pdr.ae.view.control.interfaces.IAEAspectSemanticEditorTemplateController;
import org.bbaw.pdr.ae.view.control.interfaces.IDateParser;
import org.bbaw.pdr.ae.view.control.interfaces.IMarkupEditor;
import org.bbaw.pdr.ae.view.control.interfaces.IMarkupFactory;
import org.bbaw.pdr.ae.view.control.interfaces.IMarkupPresentation;
import org.bbaw.pdr.ae.view.control.interfaces.IReferencePresentation;
import org.bbaw.pdr.ae.view.control.interfaces.ISWTnotRWTHelper;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;

public class ControlExtensions
{
	/** The Constant DATEPARSER_ID. */
	private static final String DATEPARSER_ID = "org.bbaw.pdr.ae.view.control.dateParser";

	/** The _date parser. */
	private static IDateParser dateParser;

	private static IAEAspectSemanticEditorTemplateController _aspectSemanticTemplateController;

	private static ISWTnotRWTHelper swtHelper;

	private static final String SWTHELPER_ID = "org.bbaw.pdr.ae.view.control.swtnotrwthelper";
	/** This is the ID from your extension point. */
	public static final String MARKUP_ID = "org.bbaw.pdr.ae.view.control.markupFactory";

	/** The _markup factory. */
	private static HashMap<String, IMarkupFactory> _markupFactorys = new HashMap<String, IMarkupFactory>();
	/**
	 * Gets the date parser.
	 * @return the date parser
	 */
	public final IDateParser getDateParser()
	{
		if (dateParser == null)
		{
			IConfigurationElement[] factory = Platform.getExtensionRegistry()
					.getConfigurationElementsFor(DATEPARSER_ID);
			try
			{
				for (IConfigurationElement e : factory)
				{
					// System.out.println("Evaluating extension");
					final Object o = e.createExecutableExtension("class");
					if (o instanceof IDateParser)
					{
						dateParser = (IDateParser) o;
						return dateParser;
					}
				}
			}
			catch (CoreException ex)
			{
				// System.out.println(ex.getMessage());
			}
			return null;

		}
		else
		{
			return dateParser;
		}
	}

	public static final IAEAspectSemanticEditorTemplateController getAspectSemanticTemplateController()
	{
		if (_aspectSemanticTemplateController == null)
		{
			IConfigurationElement[] factory = Platform.getExtensionRegistry().getConfigurationElementsFor(
					AEPluginIDs.EXTENSION_ASPECT_SEMANTIC_TEMPLATE_CONTROLLER);
			try
			{
				for (IConfigurationElement e : factory)
				{
					// System.out.println("Evaluating extension");
					final Object o = e.createExecutableExtension("class");
					if (o instanceof IAEAspectSemanticEditorTemplateController)
					{
						_aspectSemanticTemplateController = (IAEAspectSemanticEditorTemplateController) o;
						return _aspectSemanticTemplateController;
					}
				}
			}
			catch (CoreException ex)
			{
				// System.out.println(ex.getMessage());
			}
			return null;

		}
		else
		{
			return _aspectSemanticTemplateController;
		}
	}

	public static ISWTnotRWTHelper getSWTnotRWTHelper()
	{
		if (swtHelper == null)
		{
			IConfigurationElement[] factory = Platform.getExtensionRegistry()
.getConfigurationElementsFor(SWTHELPER_ID);
			try
			{
				for (IConfigurationElement e : factory)
				{
					// System.out.println("Evaluating extension");
					final Object o = e.createExecutableExtension("class");
					if (o instanceof ISWTnotRWTHelper)
					{
						swtHelper = (ISWTnotRWTHelper) o;
						return swtHelper;
					}
				}
			}
			catch (CoreException ex)
			{
				// System.out.println(ex.getMessage());
			}
			return null;

		}
		else
		{
			return swtHelper;
		}
	}

	/**
	 * Gets the markup factory.
	 * @return the markup factory
	 */
	private static final IMarkupFactory getMarkupFactory(String factoryName)
	{
		if (_markupFactorys.isEmpty())
		{
			IConfigurationElement[] factory = Platform.getExtensionRegistry().getConfigurationElementsFor(MARKUP_ID);
			try
			{
				for (IConfigurationElement e : factory)
				{
					// System.out.println("IMarkupFactory Evaluating extension");
					final Object o = e.createExecutableExtension("class");
					if (o instanceof IMarkupFactory)
					{
						IMarkupFactory markupFactory = (IMarkupFactory) o;
						_markupFactorys.put(markupFactory.getMarkupFactoryName(), markupFactory);
						// System.out.println("IMarkupFactory name " +
						// markupFactory.getMarkupFactoryName());
					}
				}
			}
			catch (CoreException ex)
			{
				// System.out.println(ex.getMessage());
			}
		}
		return _markupFactorys.get(factoryName);
	}

	public static IMarkupEditor createMarkupEditor()
	{
		String editorName = Platform.getPreferencesService().getString(CommonActivator.PLUGIN_ID,
				"MARKUP_EDITOR", AEConstants.MARKUP_EDITOR, null); //$NON-NLS-1$;
		IMarkupFactory factory = getMarkupFactory(editorName);
		if (factory != null)
		{
			return factory.createMarkupEditor();
		}
		else
		{
			factory = getMarkupFactory("Markup StyledText");
			if (factory != null)
			{
				return factory.createMarkupEditor();
			}
			else
			{
				factory = getMarkupFactory("Markup Vex Editor");
				if (factory != null)
				{
					return factory.createMarkupEditor();
				}
			}
		}
		return null;
	}

	public static IMarkupPresentation createMarkupPresentation()
	{
		String editorName = Platform.getPreferencesService().getString(CommonActivator.PLUGIN_ID,
				"MARKUP_PRESENTATION", AEConstants.MARKUP_PRESENTATION, null); //$NON-NLS-1$;
		IMarkupFactory factory = getMarkupFactory(editorName);
		if (factory != null)
		{
			return factory.createMarkupPresentation();
		}
		else
		{
			factory = getMarkupFactory("Markup StyledText");
			if (factory != null)
			{
				return factory.createMarkupPresentation();
			}
			else
			{
				factory = getMarkupFactory("Markup Vex Editor");
				if (factory != null)
				{
					return factory.createMarkupPresentation();
				}
			}
		}
		return null;
	}

	public static IReferencePresentation createReferencePresentation()
	{

		String editorName = Platform.getPreferencesService().getString(CommonActivator.PLUGIN_ID,
				"REFERENCE_PRESENTATION", AEConstants.REFERENCE_PRESENTATION, null); //$NON-NLS-1$;
		IMarkupFactory factory = getMarkupFactory(editorName);
		if (factory != null)
		{
			return factory.createReferencePresentation();
		}
		else
		{
			factory = getMarkupFactory("Markup StyledText");
			if (factory != null)
			{
				return factory.createReferencePresentation();
			}
			else
			{
				factory = getMarkupFactory("Markup Vex Editor");
				if (factory != null)
				{
					return factory.createReferencePresentation();
				}
			}
		}
		return null;
	}
}
