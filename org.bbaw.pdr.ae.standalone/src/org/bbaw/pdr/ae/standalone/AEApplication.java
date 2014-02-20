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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import java.util.Vector;

import org.bbaw.pdr.ae.common.AEConstants;
import org.bbaw.pdr.ae.common.CommonActivator;
import org.bbaw.pdr.ae.common.utils.CopyDirectory;
import org.bbaw.pdr.ae.control.facade.Facade;
import org.bbaw.pdr.ae.control.interfaces.IDBManager;
import org.bbaw.pdr.ae.control.interfaces.IUserManager;
import org.bbaw.pdr.ae.model.Aspect;
import org.bbaw.pdr.ae.model.Person;
import org.bbaw.pdr.ae.model.ReferenceMods;
import org.bbaw.pdr.ae.model.User;
import org.bbaw.pdr.ae.repositoryconnection.view.RepositoryLogin;
import org.bbaw.pdr.ae.standalone.internal.InstallationDialog;
import org.bbaw.pdr.ae.view.main.dialogs.LoginDialog;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.osgi.service.prefs.BackingStoreException;

/**
 * This class controls all aspects of the application's execution.
 */
public class AEApplication implements IApplication
{
	// /** Facade. */
	// private Facade _facade = Facade.getInstanz();
	/** Logger. */
	private static ILog iLogger = AEConstants.ILOGGER;
	/** Constructor of Application. */

	public static String PLUGIN_ID = "org.bbaw.pdr.ae.standalone";

	/** constructor. */
	public AEApplication()
	{
	}

	/**
	 * login to repository connection.
	 * @return true if connection is valid.
	 */
	private boolean loginRepository()
	{
		// boolean firstTry = true;
		 RepositoryLogin dialog = new RepositoryLogin(null, true);
		 if (dialog.open() != Window.OK)
		 {
			return false;
		 }
		return true;
	}

	/**
	 * save working data to persistend data storage.
	 */
	private void saveWorkingData()
	{
		Vector<Person> lastPersons = Facade.getInstanz().getLastPersons();
		for (int i = 0; i < lastPersons.size(); i++)
		{
			if (lastPersons.get(i) != null && lastPersons.get(i).getPdrId() != null)
			{
				AEActivator.getDefault().getPreferenceStore()
						.putValue("lastPerson" + i, lastPersons.get(i).getPdrId().toString());
			}
			if (i >= 12)
			{
				break;
			}
		}

		Vector<Aspect> lastAspects = Facade.getInstanz().getLastAspects();
		for (int i = 0; i < lastAspects.size(); i++)
		{
			// System.out.println("saving aspect nr " + i);
			AEActivator.getDefault().getPreferenceStore()
					.putValue("lastAspect" + i, lastAspects.get(i).getPdrId().toString());
			if (i >= 12)
			{
				break;
			}

		}

		Vector<ReferenceMods> lastReferences = Facade.getInstanz().getLastReferences();
		for (int i = 0; i < lastReferences.size(); i++)
		{
			AEActivator.getDefault().getPreferenceStore()
					.putValue("lastReference" + i, lastReferences.get(i).getPdrId().toString());
			if (i >= 12)
			{
				break;
			}
		}
		System.out.println("working data saved");

	}

	/**
	 * Starts application.
	 * @param context bundle context.
	 * @return IApplication.EXIT_OK
	 */
	@Override
	public final Object start(final IApplicationContext context)
	{
		IStatus sLocale = new Status(IStatus.INFO, AEActivator.PLUGIN_ID, "Application set locale to: "
				+ AEConstants.getCurrentLocale());
		iLogger.log(sLocale);
		Locale.setDefault(AEConstants.getCurrentLocale());
		Display display = PlatformUI.createDisplay();
		boolean testUser = false;
		// System.out.println(AEConstants.REPOSITORY_URL.trim());
		// InstallationDialog dia2 = new InstallationDialog(null);
		// dia2.open();
		if (Platform.getPreferencesService().getBoolean(CommonActivator.PLUGIN_ID, "FIRST_LOGIN", false, null))
		{
			CommonActivator.getDefault().getPreferenceStore().setValue("FIRST_LOGIN", false);
			CommonActivator.getDefault().getPreferenceStore().setValue("AE_INSTALLATION_DIR", AEConstants.AE_HOME);
			sLocale = new Status(IStatus.INFO, AEActivator.PLUGIN_ID, "AE_INSTALLATION_DIR: " + AEConstants.AE_HOME);
			iLogger.log(sLocale);
			int returnCode = 0;
			if (AEConstants.SHOW_INSTALLATION_DIALOG)
			{
				InstallationDialog dia = new InstallationDialog(null);
				returnCode = dia.open();
			}
			sLocale = new Status(IStatus.INFO, AEActivator.PLUGIN_ID, "returnCode: " + returnCode);
			iLogger.log(sLocale);


			boolean defaultRepSettings = false;
			if (AEConstants.REPOSITORY_URL.trim().length() > 0 && AEConstants.REPOSITORY_ID > 0
					&& AEConstants.PROJECT_ID > 0)
			{
				try
				{
					new URL(AEConstants.REPOSITORY_URL.trim());
					defaultRepSettings = true;
					CommonActivator.getDefault().getPreferenceStore()
							.setValue("REPOSITORY_URL", AEConstants.REPOSITORY_URL.trim()); //$NON-NLS-1$
					CommonActivator.getDefault().getPreferenceStore().setValue("REPOSITORY_PASSWORD", ""); //$NON-NLS-1$
					CommonActivator.getDefault().getPreferenceStore().setValue("PROJECT_ID", AEConstants.PROJECT_ID);
					CommonActivator.getDefault().getPreferenceStore()
							.setValue("REPOSITORY_ID", AEConstants.REPOSITORY_ID);
				}
				catch (MalformedURLException e)
				{
					defaultRepSettings = false;
					e.printStackTrace();
				}
			}
			else
			{
				defaultRepSettings = false;
			}
			if (!defaultRepSettings)
			{
				if (loginRepository())
				{
					// IUpdateManager[] rums =
					// Facade.getInstanz().getUpdateManagers();
					// for (IUpdateManager rum : rums)
					// {
					// try
					// {
					// rum.updateUsers(null);
					// }
					// catch (Exception e)
					// {
					// // TODO Auto-generated catch block
					// e.printStackTrace();
					// }
					// }
				}
				else
				{
					CommonActivator.getDefault().getPreferenceStore().setValue("REPOSITORY_URL", ""); //$NON-NLS-1$
					CommonActivator.getDefault().getPreferenceStore().setValue("REPOSITORY_PASSWORD", ""); //$NON-NLS-1$
					CommonActivator.getDefault().getPreferenceStore().setValue("PROJECT_ID", 0);
					CommonActivator.getDefault().getPreferenceStore().setValue("REPOSITORY_ID", 0);
					IUserManager um = Facade.getInstanz().getUserManager();
					um.verifyOrCreateUsers();

//					try
//					{
//						Facade.getInstanz().setCurrentUser(um.getUsersByUserName("admin"));
//					}
//					catch (Exception e)
//					{
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					testUser = true;
				}
			}
			try
			{
				Platform.getPreferencesService().getRootNode().flush();
			}
			catch (BackingStoreException e1)
			{
				e1.printStackTrace();
			}

			if (returnCode == 1)
			{
				try
				{
					checkAndCopyWorkingDir();
					return IApplication.EXIT_RESTART;
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}

		}

		if (!testUser
				&& Platform.getPreferencesService().getBoolean(CommonActivator.PLUGIN_ID, "USER_SAVE_LOGIN", false,
						null))
		{
			System.out.println("user save");

			String id = Platform.getPreferencesService().getString(CommonActivator.PLUGIN_ID, "USER_SAVE_ID", null,
					null);
			if (id != null && id.length() == 23)
			{
				System.out.println("user id " + id);

				IUserManager um = Facade.getInstanz().getUserManager();
				um.verifyOrCreateUsers();
				try
				{
					Facade.getInstanz().setCurrentUser(um.getUserById(id));
				}
				catch (Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
		if (Facade.getInstanz().getCurrentUser() == null)
		{
			LoginDialog dialog = new LoginDialog(null, true);
			dialog.create();
			dialog.open();
		}

		try
		{
			// TODO logindialog einkommentieren
			User user = Facade.getInstanz().getCurrentUser();

			if (user == null)
			{
				// Shutdown
				return IApplication.EXIT_OK;
			}

			int returnCode = PlatformUI.createAndRunWorkbench(display, new ApplicationWorkbenchAdvisor());

			if (returnCode == PlatformUI.RETURN_RESTART)
			{
				return IApplication.EXIT_RESTART;
			}
			saveWorkingData();
			optimizeDB();
			return IApplication.EXIT_OK;
		}
		finally
		{
			display.dispose();
		}
	}

	private void optimizeDB()
	{
		final IDBManager dbManager = Facade.getInstanz().getDBManager();
		if (dbManager.isOptimizationRequired())
		{
			ProgressMonitorDialog dialog = new ProgressMonitorDialog(new Shell());
			dialog.setCancelable(true);

			try
			{
				dialog.run(true, true, new IRunnableWithProgress()
				{
					@Override
					public void run(final IProgressMonitor monitor)
					{
						dbManager.optimizeAll(monitor);
						monitor.done();
					}
				});
			}
			catch (InvocationTargetException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}


	private void checkAndCopyWorkingDir() throws IOException
	{
		String dbUserHome = System.getProperty("user.home");
		dbUserHome = dbUserHome + AEConstants.FS + ".ae";
		File f = new File(dbUserHome);
		String baseXUserHome = dbUserHome + AEConstants.FS + "baseXHOME";
		if (!f.exists())
		{
			f.mkdir();
			f = new File(baseXUserHome);
			f.mkdir();
			File ff = new File(AEConstants.AE_HOME + AEConstants.FS + "baseXHOME");
			if (ff.exists())
			{
				try
				{
					CopyDirectory.copyDirectory(ff, f);
				}
				catch (IOException e2)
				{
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
			}
			IStatus sLocale = new Status(IStatus.INFO, AEActivator.PLUGIN_ID, "baseXHOME copied");
			iLogger.log(sLocale);
			String configHome = dbUserHome + AEConstants.FS + "AEConfig";
			f = new File(configHome);
			f.mkdir();
			File fConfig = new File(AEConstants.AE_HOME + AEConstants.FS + "AEConfig");
			if (fConfig.exists())
			{
				try
				{
					CopyDirectory.copyDirectory(fConfig, f);
				}
				catch (IOException e2)
				{
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
			}
			sLocale = new Status(IStatus.INFO, AEActivator.PLUGIN_ID, "AEConfig copied");
			iLogger.log(sLocale);
			String exportHome = dbUserHome + AEConstants.FS + "export-stylesheets";
			f = new File(exportHome);
			f.mkdir();
			File fExport = new File(AEConstants.AE_HOME + AEConstants.FS + "export-stylesheets");
			if (fConfig.exists())
			{
				try
				{
					CopyDirectory.copyDirectory(fExport, f);
				}
				catch (IOException e2)
				{
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
			}
			sLocale = new Status(IStatus.INFO, AEActivator.PLUGIN_ID, "export-stylesheets copied");
			iLogger.log(sLocale);
			String workspaceHome = dbUserHome + AEConstants.FS + "workspace";
			f = new File(workspaceHome);
			f.mkdir();
			File fWorkspace = new File(AEConstants.AE_HOME + AEConstants.FS + "workspace");
			if (fConfig.exists())
			{
				try
				{
					CopyDirectory.copyDirectory(fWorkspace, f);
				}
				catch (IOException e2)
				{
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
			}
			sLocale = new Status(IStatus.INFO, AEActivator.PLUGIN_ID, "workspace");
			iLogger.log(sLocale);
			String fileName = AEConstants.AE_HOME + AEConstants.FS + "Archiv-Editor.ini";
			String aeIniString;
			aeIniString = readFileAsString(fileName);
			aeIniString += " \n" + "-data" + " \n" + dbUserHome + AEConstants.FS + "workspace";
			// Write properties file.

			// log("Writing to file named " + fFileName +
			// ". Encoding: " + fEncoding);

			sLocale = new Status(IStatus.INFO, AEActivator.PLUGIN_ID, "aeIniString " + aeIniString);
			iLogger.log(sLocale);
			Writer out = new OutputStreamWriter(new FileOutputStream(fileName), "UTF-8");
			try
			{
				out.write(aeIniString);
			}
			finally
			{
				out.close();
			}

		}

	}

	private String readFileAsString(String filePath) throws java.io.IOException
	{
		StringBuffer fileData = new StringBuffer(1000);
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
		char[] buf = new char[1024];
		int numRead = 0;
		while ((numRead = reader.read(buf)) != -1)
		{
			fileData.append(buf, 0, numRead);
		}
		reader.close();
		return fileData.toString();
	}
	/**
	 * Stop method of application.
	 */
	@Override
	public final void stop()
	{
		final IWorkbench workbench = PlatformUI.getWorkbench();
		if (workbench == null)
		{
			return;
		}
		final Display display = workbench.getDisplay();
		display.syncExec(new Runnable()
		{
			@Override
			public void run()
			{
				if (!display.isDisposed())
				{
					workbench.close();
				}
			}
		});
	}
}
