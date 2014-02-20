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
package org.bbaw.pdr.ae.repositoryconnection.commands;

import org.bbaw.pdr.ae.common.AEConstants;
import org.bbaw.pdr.ae.common.AEPluginIDs;
import org.bbaw.pdr.ae.common.CommonActivator;
import org.bbaw.pdr.ae.common.NLMessages;
import org.bbaw.pdr.ae.control.core.AEVersionProvider;
import org.bbaw.pdr.ae.control.core.UserRichtsChecker;
import org.bbaw.pdr.ae.control.facade.Facade;
import org.bbaw.pdr.ae.control.interfaces.IUserManager;
import org.bbaw.pdr.ae.repositoryconnection.view.RepositoryLogin;
import org.bbaw.pdr.ae.view.main.dialogs.LoginDialog;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.services.ISourceProviderService;

/**
 * The Class ModifyRepositoryConnectionHandler.
 * @author Christoph Plutte
 */
public class ModifyRepositoryConnectionHandler implements IHandler
{

	/** The ur checker. */
	private UserRichtsChecker _urChecker = new UserRichtsChecker();

	/** The _facade. */
	private Facade _facade = Facade.getInstanz();

	@Override
	public void addHandlerListener(final IHandlerListener handlerListener)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public final Object execute(final ExecutionEvent event) throws ExecutionException
	{
		if (!_urChecker.isUserGuest())
		{
			// System.out.println("number of persons " +
			// dbCon.getDBNumberOfDocs("person"));
			// System.out.println("number of aspect " +
			// dbCon.getDBNumberOfDocs("aspect"));
			// System.out.println("number of reference " +
			// dbCon.getDBNumberOfDocs("reference"));
			// FIXME !!! true raus
			if (!_facade.getDBManager().dbIsEmpty())
			{
				String message = NLMessages.getString("Command_empty_db_requiered");
				MessageDialog messageDialog = new MessageDialog(HandlerUtil.getActiveWorkbenchWindow(event).getShell(),
						NLMessages.getString("Command_modify_repository_connection"), null, message,
						MessageDialog.WARNING, new String[]
						{NLMessages.getString("Command_backup_and_empty"), NLMessages.getString("Handler_cancel")}, 1);
				if (messageDialog.open() == 0)
				{
					IHandlerService handlerService = (IHandlerService) PlatformUI.getWorkbench().getService(
							IHandlerService.class);
					try
					{
						handlerService.executeCommand("org.bbaw.pdr.ae.backup.commands.CreateEmptyDB", null); //$NON-NLS-1$
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
				else
				{
					return null;
				}
			}

			else
			{
				int repoInstanceID = Platform.getPreferencesService().getInt(CommonActivator.PLUGIN_ID,
						"REPOSITORY_ID", AEConstants.REPOSITORY_ID, null); // Activator.getDefault().getPreferenceStore().getInt("REPOSITORY_ID");
				int projectID = Platform.getPreferencesService().getInt(CommonActivator.PLUGIN_ID, "PROJECT_ID",
						AEConstants.PROJECT_ID, null); // Activator.getDefault().getPreferenceStore().getInt("PROJECT_ID");

				RepositoryLogin dialog = new RepositoryLogin(HandlerUtil.getActiveWorkbenchWindow(event).getShell(),
						false);
				dialog.open();
				if (repoInstanceID != Platform.getPreferencesService().getInt(CommonActivator.PLUGIN_ID,
						"REPOSITORY_ID", AEConstants.REPOSITORY_ID, null)
						|| projectID != Platform.getPreferencesService().getInt(CommonActivator.PLUGIN_ID,
								"PROJECT_ID", AEConstants.PROJECT_ID, null))
				{
					IWorkbench w = PlatformUI.getWorkbench();
					// get the window (which is a IServiceLocator)
					if (w != null)
					{
						IWorkbenchWindow window = w.getActiveWorkbenchWindow();
						// get the service
						ISourceProviderService service = (ISourceProviderService) window
								.getService(ISourceProviderService.class);
						boolean canSynch = true;
						String url = Platform.getPreferencesService().getString(CommonActivator.PLUGIN_ID, "REPOSITORY_URL",
								AEConstants.REPOSITORY_URL, null);
						repoInstanceID = Platform.getPreferencesService().getInt(CommonActivator.PLUGIN_ID,
								"REPOSITORY_ID", AEConstants.REPOSITORY_ID, null); // Activator.getDefault().getPreferenceStore().getInt("REPOSITORY_ID");
						projectID = Platform.getPreferencesService().getInt(CommonActivator.PLUGIN_ID, "PROJECT_ID",
								AEConstants.PROJECT_ID, null); // Activator.getDefault().getPreferenceStore().getInt("PROJECT_ID");

					AEVersionProvider aeVersionProvider = (AEVersionProvider) service
							.getSourceProvider(AEPluginIDs.SOURCE_PARAMETER_AE_ADVANCED_VERSION);
					if (url == null || "".equals(url.trim()) || "xxx".equals(url) || repoInstanceID == 0 || projectID == 0)
					{
						canSynch = false;
					}
					aeVersionProvider.setCanSynchronize(canSynch);

					}
					// project settings changed
					_facade.setCurrentUser(null);
					IUserManager userManager = _facade.getUserManager();
					userManager.clearLastUsers();
					userManager.verifyOrCreateUsers();
					LoginDialog loginDialog = new LoginDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow()
							.getShell(),
							true);
					loginDialog.open();
				}
			}
		}
		else
		{
			MessageDialog.openInformation(HandlerUtil.getActiveWorkbenchWindow(event).getShell(),
					NLMessages.getString("Commandsr_guest_user"), NLMessages.getString("Commandsr_guest_user_denied")); //$NON-NLS-1$
		}

		return null;
	}

	@Override
	public final boolean isEnabled()
	{
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public final boolean isHandled()
	{
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void removeHandlerListener(final IHandlerListener handlerListener)
	{
		// TODO Auto-generated method stub

	}

}
