///**
// * This file is part of Archiv-Editor.
// * 
// * The software Archiv-Editor serves as a client user interface for working with
// * the Person Data Repository. See: pdr.bbaw.de
// * 
// * The software Archiv-Editor was developed at the Berlin-Brandenburg Academy
// * of Sciences and Humanities, Jägerstr. 22/23, D-10117 Berlin.
// * www.bbaw.de
// * 
// * Copyright (C) 2010-2013  Berlin-Brandenburg Academy
// * of Sciences and Humanities
// * 
// * The software Archiv-Editor was developed by @author: Christoph Plutte.
// * 
// * Archiv-Editor is free software: you can redistribute it and/or modify
// * it under the terms of the GNU Lesser General Public License as published by
// * the Free Software Foundation, either version 3 of the License, or
// * (at your option) any later version.
// * 
// * Archiv-Editor is distributed in the hope that it will be useful,
// * but WITHOUT ANY WARRANTY; without even the implied warranty of
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// * GNU Lesser General Public License for more details.
// * 
// * You should have received a copy of the GNU Lesser General Public License
// * along with Archiv-Editor.  
// * If not, see <http://www.gnu.org/licenses/lgpl-3.0.html>.
// */
//package org.bbaw.pdr.ae.rap2;
//
//import java.net.MalformedURLException;
//import java.net.URL;
//
//import org.bbaw.pdr.ae.common.AEConstants;
//import org.bbaw.pdr.ae.common.CommonActivator;
//import org.bbaw.pdr.ae.control.facade.Facade;
//import org.bbaw.pdr.ae.control.interfaces.IUpdateManager;
//import org.bbaw.pdr.ae.control.interfaces.IUserManager;
//import org.bbaw.pdr.ae.view.main.dialogs.LoginDialog;
//import org.eclipse.core.runtime.Platform;
//import org.eclipse.equinox.app.IApplication;
//import org.eclipse.equinox.app.IApplicationContext;
//import org.eclipse.swt.widgets.Display;
//import org.eclipse.ui.PlatformUI;
//import org.eclipse.ui.application.WorkbenchAdvisor;
//
///**
// * This class controls all aspects of the application's execution and is
// * contributed through the plugin.xml.
// */
//public class AERAPApplication implements IApplication
//{
//
//	/**
//	 * Login repository.
//	 * @return true, if successful
//	 */
//	private boolean loginRepository()
//	{
//		return false;
//	}
//
//	/**
//	 * Start.
//	 * @param context the context
//	 * @return the object
//	 * @throws Exception the exception
//	 */
//	@Override
//	public Object start(final IApplicationContext context) throws Exception
//	{
//		Display display = PlatformUI.createDisplay();
//
//		boolean testUser = false;
//		System.out.println(AEConstants.REPOSITORY_URL.trim());
//
//		if (Platform.getPreferencesService().getBoolean(CommonActivator.PLUGIN_ID, "FIRST_LOGIN", false, null))
//		{
//			boolean defaultRepSettings = false;
//			if (AEConstants.REPOSITORY_URL.trim().length() > 0 && AEConstants.REPOSITORY_ID > 0
//					&& AEConstants.PROJECT_ID > 0)
//			{
//				try
//				{
//					new URL(AEConstants.REPOSITORY_URL.trim());
//					defaultRepSettings = true;
//					CommonActivator.getDefault().getPreferenceStore()
//							.setValue("REPOSITORY_URL", AEConstants.REPOSITORY_URL.trim()); //$NON-NLS-1$
//					CommonActivator.getDefault().getPreferenceStore().setValue("REPOSITORY_PASSWORD", ""); //$NON-NLS-1$
//					CommonActivator.getDefault().getPreferenceStore().setValue("PROJECT_ID", AEConstants.PROJECT_ID);
//					CommonActivator.getDefault().getPreferenceStore()
//							.setValue("REPOSITORY_ID", AEConstants.REPOSITORY_ID);
//				}
//				catch (MalformedURLException e)
//				{
//					defaultRepSettings = false;
//					e.printStackTrace();
//				}
//			}
//			else
//			{
//				defaultRepSettings = false;
//			}
//			if (!defaultRepSettings)
//			{
//				if (loginRepository())
//				{
//					IUpdateManager[] rums = Facade.getInstanz().getUpdateManagers();
//					for (IUpdateManager rum : rums)
//					{
//						try
//						{
//							rum.updateUsers(null, null, null);
//						}
//						catch (Exception e)
//						{
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//					}
//				}
//				else
//				{
//					CommonActivator.getDefault().getPreferenceStore().setValue("REPOSITORY_URL", ""); //$NON-NLS-1$
//					CommonActivator.getDefault().getPreferenceStore().setValue("REPOSITORY_PASSWORD", ""); //$NON-NLS-1$
//					CommonActivator.getDefault().getPreferenceStore().setValue("PROJECT_ID", 0);
//					CommonActivator.getDefault().getPreferenceStore().setValue("REPOSITORY_ID", 0);
//					IUserManager um = Facade.getInstanz().getUserManager();
//					um.verifyOrCreateUsers();
//
//					try
//					{
//						Facade.getInstanz().setCurrentUser(um.getUsersByUserName("pdrAdmin"));
//					}
//					catch (Exception e)
//					{
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					testUser = true;
//				}
//			}
//			CommonActivator.getDefault().getPreferenceStore().setValue("FIRST_LOGIN", false);
//		}
//
//		if (!testUser && CommonActivator.getDefault().getPreferenceStore().getBoolean("USER_SAVE_LOGIN"))
//		{
//			System.out.println("user save");
//
//			String id = CommonActivator.getDefault().getPreferenceStore().getString("USER_SAVE_ID");
//			if (id.length() == 23)
//			{
//				System.out.println("user id " + id);
//
//				IUserManager um = Facade.getInstanz().getUserManager();
//				um.verifyOrCreateUsers();
//				try
//				{
//					Facade.getInstanz().setCurrentUser(um.getUserById(id));
//				}
//				catch (Exception e)
//				{
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//
//			}
//		}
//		// test login
//		Facade.getInstanz().setCurrentUser(Facade.getInstanz().getUserManager().getUsersByUserName("pdrAdmin"));
//
//		if (Facade.getInstanz().getCurrentUser() == null)
//		{
//
//			IUserManager um = Facade.getInstanz().getUserManager();
//			um.verifyOrCreateUsers();
//
//
//			IUpdateManager[] rums = Facade.getInstanz().getUpdateManagers();
//			for (IUpdateManager rum : rums)
//			{
//				try
//				{
//					rum.updateUsers(Facade.getInstanz().getCurrentUser().getPdrId().toString(), Facade.getInstanz().getCurrentUser().getAuthentication().getPassword(), null);
//				}
//				catch (Exception e)
//				{
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//
//			//dev auskommentiert cp
////			LoginDialog dialog = new LoginDialog(null, true);
////			dialog.create();
////			dialog.open();
//		}
//		WorkbenchAdvisor advisor = new ApplicationWorkbenchAdvisor();
//		PlatformUI.createAndRunWorkbench(display, advisor);
//		return 0;
//	}
//
//	/**
//	 * Stop.
//	 */
//	@Override
//	public void stop()
//	{
//		// Do nothing
//	}
//}
