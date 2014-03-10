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

import org.bbaw.pdr.ae.common.AEConstants;
import org.bbaw.pdr.ae.common.AEPluginIDs;
import org.bbaw.pdr.ae.common.CommonActivator;
import org.bbaw.pdr.ae.control.core.AEVersionProvider;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.eclipse.ui.services.ISourceProviderService;

public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor
{

	public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer)
	{
		super(configurer);
	}

	@Override
	public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer)
	{
		return new ApplicationActionBarAdvisor(configurer);
	}

	@Override
	public void postWindowOpen()
	{
		IStatusLineManager statusline = getWindowConfigurer().getActionBarConfigurer().getStatusLineManager();
		statusline.setMessage(null, "Ready");
		boolean advanced = Platform.getPreferencesService().getBoolean(CommonActivator.PLUGIN_ID,
				"AE_ADVANCED_VERSION",
				AEConstants.AE_ADVANCED_VERSION, null);
		boolean canSynch = true;
		String url = Platform.getPreferencesService().getString(CommonActivator.PLUGIN_ID, "REPOSITORY_URL",
				AEConstants.REPOSITORY_URL, null);
		int rep = Platform.getPreferencesService().getInt(CommonActivator.PLUGIN_ID, "REPOSITORY_ID",
				AEConstants.REPOSITORY_ID, null);
		int pro = Platform.getPreferencesService().getInt(CommonActivator.PLUGIN_ID, "PROJECT_ID",
				AEConstants.PROJECT_ID, null);
		if (url == null || "".equals(url.trim()) || "xxx".equals(url) || rep == 0 || pro == 0)
		{
			canSynch = false;
		}
		if (PlatformUI.isWorkbenchRunning())
		{
			IWorkbench w = PlatformUI.getWorkbench();
			// get the window (which is a IServiceLocator)
			if (w != null)
			{
				IWorkbenchWindow window = w.getActiveWorkbenchWindow();
				// get the service
				ISourceProviderService service = (ISourceProviderService) window
						.getService(ISourceProviderService.class);
				// get our source provider by querying by the variable
				// name
				AEVersionProvider aeVersionProvider = (AEVersionProvider) service
						.getSourceProvider(AEPluginIDs.SOURCE_PARAMETER_AE_ADVANCED_VERSION);

				// String state = (String)
				// aeVersionProvider.getCurrentState().get(
				// "org.bbaw.pdr.ae.control.aeAdvancedVersion");
				// System.out.println("org.bbaw.pdr.ae.control.aeAdvancedVersion state: "
				// + state);
				// set the value
				aeVersionProvider.setAEAdvancedVersion(advanced);
				aeVersionProvider.setCanSynchronize(canSynch);
				// state = (String) aeVersionProvider.getCurrentState().get(
				// "org.bbaw.pdr.ae.control.aeAdvancedVersion");
				// System.out.println("org.bbaw.pdr.ae.control.aeAdvancedVersion state: "
				// + state);

			}

		}
	}


	

	

	@Override
	public void preWindowOpen()
	{
		IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
		configurer.setInitialSize(new Point(1200, 750));
		configurer.setShowCoolBar(true);
		configurer.setShowStatusLine(true);
		configurer.setShowProgressIndicator(true);

		boolean advanced = Platform.getPreferencesService().getBoolean(CommonActivator.PLUGIN_ID,
				"AE_ADVANCED_VERSION", AEConstants.AE_ADVANCED_VERSION, null);

		System.out.println("advanced " + advanced);
		if (advanced)
		{
			configurer.setShowPerspectiveBar(false);

			configurer.setTitle("Archiv-Editor 2.3.8");
		}
		else
		{
			configurer.setShowPerspectiveBar(false);
			configurer.setTitle("Archiv-Editor 2.3.8 lite");

		}

		// IEvaluationService service = (IEvaluationService)
		// PlatformUI.getWorkbench()
		// .getService(IEvaluationService.class);
		// service.addSourceProvider(new AESourceProvider());
	}

}
