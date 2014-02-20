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
package org.bbaw.pdr.ae.standalone;

import org.eclipse.core.runtime.ILog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle.
 */
public class AEActivator extends AbstractUIPlugin
{

	/** plug-in id. */
	public static final String PLUGIN_ID = "org.bbaw.pdr.ae.standalone";

	/** shared instance. */
	private static AEActivator plugin;

	/**
	 * Returns the shared instance.
	 * @return the shared instance
	 */
	public static AEActivator getDefault()
	{
		return plugin;
	}

	/**
	 * zum testen, soll den logger zurückgeben.
	 * @return ILog.
	 */
	public static ILog getILogger()
	{
		return AEActivator.getDefault().getLog();
	}

	/**
	 * Returns an image descriptor for the image file at the given plug-in
	 * relative path.
	 * @param path the path.
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(final String path)
	{
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

	/**
	 * The constructor.
	 */
	public AEActivator()
	{

	}

	@Override
	public final void start(final BundleContext context) throws Exception
	{

		super.start(context);
		plugin = this;

		// IStatus status = new Status(IStatus.ERROR,Activator.PLUGIN_ID,
		// "AE_HOME: " + AE_HOME);
		// ILog iLogger = org.bbaw.pdr.ae.view.main.Activator.getILogger();
		// iLogger.log(status);

	}

	@Override
	public final void stop(final BundleContext context) throws Exception
	{

		plugin = null;
		super.stop(context);
	}

}
