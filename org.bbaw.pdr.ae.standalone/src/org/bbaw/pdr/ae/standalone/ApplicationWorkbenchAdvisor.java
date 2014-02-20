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
import org.bbaw.pdr.ae.control.facade.Facade;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.preference.IPreferenceNode;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.eclipselabs.p2.rcpupdate.utils.P2Util;

public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor
{

	private static final String PERSPECTIVE_ID = AEPluginIDs.PERSPECTIVE_ASPECTS;

	@Override
	public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer)
	{
		// configurer.setShowPerspectiveBar(true);
		return new ApplicationWorkbenchWindowAdvisor(configurer);
	}

	@Override
	public String getInitialWindowPerspectiveId()
	{
		return PERSPECTIVE_ID;
	}

	@Override
	public void postStartup()
	{
		// TODO Auto-generated method stub
		super.postStartup();
		// set source parameter with user role.
		Facade.getInstanz().processUserRole();
		
		//remove unwanted preference pages
		PreferenceManager pm = PlatformUI.getWorkbench().getPreferenceManager( );
		IPreferenceNode[] arr = pm.getRootSubNodes();
        
		for(IPreferenceNode pn:arr){
		    if (pn.getId().equals("org.eclipse.ui.preferencePages.Workbench"))
		    {
		    	pn.remove( "org.eclipse.equinox.security.ui.category" );
			    pn.remove( "org.eclipse.ui.preferencePages.Editors" );

			    pn.remove( "org.eclipse.ui.preferencePages.Workspace" );

			    pn.remove( "org.eclipse.ui.preferencePages.ContentTypes" );
			    break;
		    }
		}
	    


	}

	@Override
	public void preStartup()
	{
		if (Platform.getPreferencesService().getBoolean(CommonActivator.PLUGIN_ID, "AUTOMATED_UPDATE",
				AEConstants.AUTOMATED_UPDATE, null))
		{
			try {
				P2Util.checkForUpdates();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
