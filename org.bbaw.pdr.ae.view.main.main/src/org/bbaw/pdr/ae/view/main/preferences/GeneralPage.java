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
package org.bbaw.pdr.ae.view.main.preferences;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import org.bbaw.pdr.ae.common.AEConstants;
import org.bbaw.pdr.ae.common.CommonActivator;
import org.bbaw.pdr.ae.common.NLMessages;
import org.bbaw.pdr.ae.view.main.internal.Activator;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.progress.UIJob;

/**
 * The Class GeneralPage.
 * @author Christoph Plutte
 */
public class GeneralPage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage
{
	/** Logger. */
	private static ILog iLogger = AEConstants.ILOGGER;
	private boolean _restartRequiered = false;
	private boolean _langSetting = Platform.getPreferencesService().getBoolean(CommonActivator.PLUGIN_ID,
			"USER_SELECTED_LANG_OK", false, null);
	private String _lang = Platform.getPreferencesService().getString(CommonActivator.PLUGIN_ID,
			"USER_SELECTED_LANG", "de", null);
	private BooleanFieldEditor _customLangEd;
	private ComboFieldEditor _langEd;
	/**
	 * Instantiates a new general page.
	 */
	public GeneralPage()
	{
		super(GRID);

	}

	@Override
	public final void createFieldEditors()
	{

		_customLangEd = new BooleanFieldEditor("USER_SELECTED_LANG_OK", //$NON-NLS-1$
				NLMessages.getString("Preference_custom_language_setting"), getFieldEditorParent());
		addField(_customLangEd);
		_langEd = new ComboFieldEditor(
				"USER_SELECTED_LANG", //$NON-NLS-1$
				NLMessages.getString("Preference_language"), toStringArrayArray(AEConstants.LANGUAGES_AE),
				getFieldEditorParent());
		addField(_langEd);
		_langEd.setEnabled(_langSetting, getFieldEditorParent());
		Platform.getPreferencesService().getString(CommonActivator.PLUGIN_ID,
				"USER_SELECTED_LANG", "", null);
		addField(new BooleanFieldEditor("USER_SAVE_LOGIN", //$NON-NLS-1$
				NLMessages.getString("Preference_login_user_automatically"), getFieldEditorParent()));
		addField(new BooleanFieldEditor("AUTOMATED_UPDATE", //$NON-NLS-1$
				NLMessages.getString("Preference_automatically_update"), getFieldEditorParent()));



	}
	
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		super.propertyChange(event);
		// if checkbox for custom language activation has changed, update language selector accordingly
		if (event.getSource().equals(_customLangEd))
			_langEd.setEnabled((Boolean)event.getNewValue(), getFieldEditorParent());
	}

	@Override
	public final void init(final IWorkbench workbench)
	{
		setPreferenceStore(CommonActivator.getDefault().getPreferenceStore());
		setDescription(NLMessages.getString("Preference_general_preference_title"));
	}

	@Override
	public final boolean performOk()
	{
		String aeInstallation = Platform.getPreferencesService().getString(CommonActivator.PLUGIN_ID,
				"AE_INSTALLATION_DIR", AEConstants.AE_HOME, null);

		if (_langSetting != _customLangEd.getBooleanValue())
		{
			_restartRequiered = true;
		}
		else if (_customLangEd.getBooleanValue())
		{
			_langEd.store();
			String newLang = _langEd.getPreferenceStore().getString("USER_SELECTED_LANG");
			if (!_lang.equals(newLang))
			{
				_restartRequiered = true;
			}
		}
		else
		{
			_restartRequiered = false;
		}

		if (_customLangEd.getBooleanValue())
		{
			String fileName = aeInstallation + AEConstants.FS
					+ "configuration" + AEConstants.FS + "config.ini";
			IStatus status = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Preferences General Custom load config.ini: " //$NON-NLS-1$
					+ fileName);
			iLogger.log(status);
			File file = new File(fileName);
			Properties props = new Properties();
			try
			{
				props.load(new FileInputStream(file));
			}
			catch (FileNotFoundException e)
			{
				status = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Preferences General Custom error loading properties from config.ini: " //$NON-NLS-1$
						+ e.getMessage());
				iLogger.log(status);
				e.printStackTrace();
			}
			catch (IOException e)
			{
				status = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Preferences General Custom error loading properties from config.ini: " //$NON-NLS-1$
						+ e.getMessage());
				iLogger.log(status);
				e.printStackTrace();
			}
			String lang = props.getProperty("osgi.nl");
			// if (lang == null)
			// {
			// props.setProperty("osgi.nl", lang);
			// }
			status = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Preferences General Custom langeuage old: " //$NON-NLS-1$
					+ lang);
			iLogger.log(status);
			String newLang = Platform.getPreferencesService().getString(CommonActivator.PLUGIN_ID,
					"USER_SELECTED_LANG", "", null);
			if (newLang.equals("Deutsch"))
			{
				lang = "de";
			}
			else if (newLang.equals("English"))
			{
				lang = "en";
			}
			else if (newLang.equals("Francais"))
			{
				lang = "fr";
			}
			else if (newLang.equals("Italiano"))
			{
				lang = "it";
			}
			else
			{
				lang = "en";
			}
			props.setProperty("osgi.nl", lang);
			status = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Preferences Gener Custom langeuage new: " //$NON-NLS-1$
					+ lang);
			iLogger.log(status);

			// Write properties file.
			FileOutputStream out;
			try
			{
				out = new FileOutputStream(fileName);
				props.store(out, "Custom language settings");
				out.flush();
				out.close();
			}
			catch (FileNotFoundException e1)
			{
				e1.printStackTrace();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}

		}
		else
		{
			String fileName = aeInstallation + AEConstants.FS
					+ "configuration" + AEConstants.FS + "config.ini";
			IStatus status = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Preferences General Custom load config.ini: " //$NON-NLS-1$
					+ fileName);
			iLogger.log(status);
			File file = new File(fileName);
			if (file.exists())
			{
				Properties props = new Properties();
				try
				{
					props.load(new FileInputStream(file));
				}
				catch (FileNotFoundException e)
				{
					status = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Preferences General Custom error loading properties from config.ini: " //$NON-NLS-1$
							+ e.getMessage());
					iLogger.log(status);
					e.printStackTrace();
				}
				catch (IOException e)
				{
					status = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Preferences General Custom error loading properties from config.ini: " //$NON-NLS-1$
							+ e.getMessage());
					iLogger.log(status);
					e.printStackTrace();
				}
				String lang = "";
				props.setProperty("osgi.nl", lang);
				status = new Status(IStatus.INFO, Activator.PLUGIN_ID,
						"Preferences User Custom language deactivated: " //$NON-NLS-1$
								+ lang);
				iLogger.log(status);
	
				// Write properties file.
				FileOutputStream out;
				try
				{
					out = new FileOutputStream(fileName);
					props.store(out, "Custom language settings");
					out.flush();
					out.close();
				}
				catch (FileNotFoundException e1)
				{
					e1.printStackTrace();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
		super.performOk();
		if (_restartRequiered)
		{
			UIJob job = new UIJob("timer")
			{
				@Override
				public IStatus runInUIThread(IProgressMonitor monitor)
				{
					checkRestart();
					return Status.OK_STATUS;
				}
			};
			job.schedule(500);
		}
		return true;
	}

	/**
	 * To string array array.
	 * @param strArray the str array
	 * @return the string[][]
	 */
	private String[][] toStringArrayArray(final String[] strArray)
	{
		ArrayList<String[]> l = new ArrayList<String[]>();
		for (String str : strArray)
		{
			l.add(new String[]
			{str, str});
		}
		return l.toArray(new String[l.size()][]);
	}


	private boolean checkRestart()
	{


		MessageDialog messageDialog = new MessageDialog(null, NLMessages.getString("Handler_restart_titel"), null, NLMessages.getString("Handler_restart_message"),
				MessageDialog.WARNING, new String[]
				{NLMessages.getString("Handler_restart"), NLMessages.getString("Handler_cancel")}, 0);
		if (messageDialog.open() == 0)
		{
			IHandlerService handlerService = (IHandlerService) PlatformUI.getWorkbench().getService(
					IHandlerService.class);
			try
			{
				handlerService.executeCommand("org.eclipse.ui.file.restartWorkbench", null); //$NON-NLS-1$
			}
			catch (ExecutionException e)
			{
				e.printStackTrace();
			}
			catch (NotDefinedException e)
			{
				e.printStackTrace();
			}
			catch (NotEnabledException e)
			{
				e.printStackTrace();
			}
			catch (NotHandledException e)
			{
				e.printStackTrace();
			}
		}

		return true;
	}
@Override
public String getTitle() {
	// TODO Auto-generated method stub
	return super.getTitle();
}

}
