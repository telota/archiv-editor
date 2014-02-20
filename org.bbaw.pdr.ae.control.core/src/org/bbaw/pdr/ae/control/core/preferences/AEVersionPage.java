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
package org.bbaw.pdr.ae.control.core.preferences;

import org.bbaw.pdr.ae.common.AEConstants;
import org.bbaw.pdr.ae.common.AEPluginIDs;
import org.bbaw.pdr.ae.common.CommonActivator;
import org.bbaw.pdr.ae.common.NLMessages;
import org.bbaw.pdr.ae.control.core.AEVersionProvider;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.progress.UIJob;
import org.eclipse.ui.services.ISourceProviderService;

/**
 * The Class PersonPage.
 * @author Christoph Plutte
 */
public class AEVersionPage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage
{

	private boolean _restartRequiered = false;
	private boolean _advanced = Platform.getPreferencesService().getBoolean(CommonActivator.PLUGIN_ID,
			"AE_ADVANCED_VERSION", false, null);;
	private BooleanFieldEditor _versionEd;
	/**
	 * Instantiates a new person page.
	 */
	public AEVersionPage()
	{
		super(GRID);

	}

	@Override
	public final void createFieldEditors()
	{

		_versionEd = new BooleanFieldEditor("AE_ADVANCED_VERSION", //$NON-NLS-1$
				"Archiv-Editor Advanced Version", getFieldEditorParent());
		addField(_versionEd);


	}

	@Override
	public final void init(final IWorkbench workbench)
	{
		setPreferenceStore(CommonActivator.getDefault().getPreferenceStore());
		setDescription(NLMessages.getString("Preference_aeversion"));
	}


	@Override
	public boolean performOk()
	{
		if (_advanced != _versionEd.getBooleanValue())
		{
			_restartRequiered = true;
		}
		else
		{
			_restartRequiered = false;
		}
		// TODO Auto-generated method stub
		boolean advanced = Platform.getPreferencesService().getBoolean(CommonActivator.PLUGIN_ID,
				"AE_ADVANCED_VERSION", AEConstants.AE_ADVANCED_VERSION, null);
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

				// Now get my service
				AEVersionProvider commandStateService = (AEVersionProvider) service
						.getSourceProvider(AEPluginIDs.SOURCE_PARAMETER_AE_ADVANCED_VERSION);
				commandStateService.setAEAdvancedVersion(advanced);
				// get our source provider by querying by the variable
				// name
				// UserRoleSourceProvider userRoleSourceProvider =
				// (UserRoleSourceProvider) service
				// .getSourceProvider(AEPluginIDs.SOURCE_PARAMETER_AE_ADVANCED_VERSION);
				//
				// String state = (String)
				// userRoleSourceProvider.getCurrentState().get(
				// "org.bbaw.pdr.ae.control.aeAdvancedVersion");
				// System.out.println("org.bbaw.pdr.ae.control.aeAdvancedVersion state: "
				// + state);
				// // set the value
				// userRoleSourceProvider.setAEAdvancedVersion(_versionEditor.getBooleanValue());
				//
				// state = (String)
				// userRoleSourceProvider.getCurrentState().get(
				// "org.bbaw.pdr.ae.control.aeAdvancedVersion");
				// System.out.println("org.bbaw.pdr.ae.control.aeAdvancedVersion state: "
				// + state);

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
	private boolean checkRestart()
	{

		String message = "It is requiered to restart the Archiv-Editor in order to activate changes. \n\nDo you want to restart the Archiv-Editor now?";

		MessageDialog messageDialog = new MessageDialog(null, "Restart Archiv-Editor", null, message,
				MessageDialog.WARNING, new String[]
				{"Restart", NLMessages.getString("Handler_cancel")}, 0);
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

}
