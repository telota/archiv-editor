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
package org.bbaw.pdr.ae.rap2;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

import org.bbaw.pdr.ae.common.AEConstants;
import org.bbaw.pdr.ae.common.CommonActivator;
import org.bbaw.pdr.ae.control.facade.Facade;
import org.bbaw.pdr.ae.metamodel.PdrId;
import org.bbaw.pdr.ae.metamodel.Record;
import org.bbaw.pdr.ae.metamodel.Revision;
import org.bbaw.pdr.ae.model.Authentication;
import org.bbaw.pdr.ae.model.User;
import org.bbaw.pdr.ae.model.UserInformation;
import org.bbaw.pdr.ae.rap2.internal.AERAPConstants;
import org.bbaw.pdr.ae.rap2.internal.LoginDialogRAP;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.rap.rwt.application.EntryPoint;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.progress.UIJob;

public class AEEntryPoint implements EntryPoint {
	private User _currentUser = null;
	private boolean dev = true;

	public AEEntryPoint() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int createUI() {
		Display display = PlatformUI.createDisplay();
		System.out.println(AERAPConstants.REPOSITORY_URL.trim());
		LoginDialogRAP dialog = new LoginDialogRAP(null);
		dialog.create();
		int result = 0;
		
		if (dev == true)
		{
			_currentUser = createUser(new PdrId("pdrUo", 1, 10, 422), "pdrrdp", "pdrAdmin", new String[]{"admin", "pdrAdmin"}, "pdrAdmin", "pdrAdmin", "pdrAdmin");
			String projectDir = AERAPConstants.AE_RAP_HOME + AERAPConstants.FS + _currentUser.getPdrId().getProjectID();
			String userDir = projectDir + AERAPConstants.FS + _currentUser.getPdrId().toString();

			org.bbaw.pdr.ae.common.CommonActivator.setAEHome(userDir);
			org.bbaw.pdr.ae.common.CommonActivator.setProjectID(_currentUser.getPdrId().getProjectID());
			WorkbenchAdvisor advisor = new ApplicationWorkbenchAdvisor();
			Facade.getInstanz().setCurrentUser(_currentUser);

		    result = PlatformUI.createAndRunWorkbench( display, advisor );
		}
		
		else if (dialog.open()  == 0)
		{
			if (dialog.getUser() != null)
			{
				boolean projectExists = true;
				boolean userExists = true;
				String[] dirs = new String[]{"AEConfig", "baseXHome", "export-stylesheets"};

				_currentUser = dialog.getUser();
				String projectDir = AERAPConstants.AE_RAP_HOME + AERAPConstants.FS + _currentUser.getPdrId().getProjectID();
				File f = new File(projectDir);
				if (!f.exists())
				{
					f.mkdir();
					projectExists = false;
				}
				else
				{
					for (String d : dirs)
					{
						f = new File(projectDir + AERAPConstants.FS + d);
						if (!f.exists())
						{
							projectExists = false;
						}
					}
				}
				String userDir = projectDir + AERAPConstants.FS + _currentUser.getPdrId().toString();
				//AE_HOME in AEconstants setzten!!!
				org.bbaw.pdr.ae.common.CommonActivator.setAEHome(userDir);
				org.bbaw.pdr.ae.common.CommonActivator.setProjectID(_currentUser.getPdrId().getProjectID());
				File userDirFile = new File(userDir);
				if (!userDirFile.exists())
				{
					userDirFile.mkdir();
					userExists = false;
				}
				else
				{
					for (String d : dirs)
					{
						f = new File(userDir + AERAPConstants.FS + d);
						if (!f.exists())
						{
							userExists = false;
						}
					}
				}
				//evtl. dateien copieren
				File targetFDir;
				if (!userExists)
				{
					if (projectExists)
					{
						for (String d : dirs)
						{
							f = new File(projectDir + AERAPConstants.FS + d);
							targetFDir = new File(userDir + AERAPConstants.FS + d);
							targetFDir.mkdir();
							if (f.exists())
							{
								try
								{
									org.bbaw.pdr.ae.common.utils.CopyDirectory.copyDirectory(f, targetFDir);
								}
								catch (IOException e)
								{
									e.printStackTrace();
								}
							}
						}
					}
					else
					{
						for (String d : dirs)
						{
							f = new File(AERAPConstants.AE_RAP_HOME + AERAPConstants.FS + d);
							targetFDir = new File(userDir + AERAPConstants.FS + d);
							targetFDir.mkdir();
							if (f.exists())
							{
								try
								{
									org.bbaw.pdr.ae.common.utils.CopyDirectory.copyDirectory(f, targetFDir);
								}
								catch (IOException e)
								{
									e.printStackTrace();
								}
							}
						}
					}
				}
				
				
				WorkbenchAdvisor advisor = new ApplicationWorkbenchAdvisor();
				Facade.getInstanz().setCurrentUser(_currentUser);

				
//				UIJob job = new UIJob("initialize")
//				{
//
//					@Override
//					public IStatus runInUIThread(final IProgressMonitor monitor)
//					{
//						CommonActivator.getDefault().getPreferenceStore().setValue("MARKUP_PRESENTATION", "Markup Vex Editor");
//						CommonActivator.getDefault().getPreferenceStore().setValue("MARKUP_EDITOR", "Markup Vex Editor");
//						CommonActivator.getDefault().getPreferenceStore().setValue("REFERENCE_PRESENTATION", "Markup Vex Editor");
//						return Status.OK_STATUS;
//					}
//				};
//				job.setUser(true);
//				job.schedule();

			    result = PlatformUI.createAndRunWorkbench( display, advisor );
			    
			}
			else
			{
				result  = PlatformUI.RETURN_EMERGENCY_CLOSE;
			}
		}
		else
		{
			result  = PlatformUI.RETURN_EMERGENCY_CLOSE;
		}
		

			
		
	  
	   
	    return result;
	}
	public User createUser(PdrId id, String userName, String password, String[] userRoles, String surName,
			String foreName, String projectName)
	{
		User testUser = new User(id);
		testUser.setAuthentication(new Authentication());
		testUser.getAuthentication().setUserName(userName);
		testUser.getAuthentication().setPassword(password);
		testUser.getAuthentication().setRoles(new Vector<String>(1));
		for (String role : userRoles)
		{
			testUser.getAuthentication().getRoles().add(role);
		}

		testUser.setUserInformation(new UserInformation());
		testUser.getUserInformation().setForename(foreName);
		testUser.getUserInformation().setSurname(surName);
		testUser.getUserInformation().setProjectName(projectName);
		testUser.setRecord(new Record());
		Revision revision = new Revision();
		revision.setAuthority(id);
		revision.setRef(0);
		revision.setTimeStamp(new java.util.Date());
		testUser.getRecord().getRevisions().add(revision);
		return testUser;
	}
}
