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
package org.bbaw.pdr.ae.backup.commands;

import java.lang.reflect.InvocationTargetException;

import org.bbaw.pdr.ae.common.NLMessages;
import org.bbaw.pdr.ae.control.core.UserRichtsChecker;
import org.bbaw.pdr.ae.control.facade.Facade;
import org.bbaw.pdr.ae.control.interfaces.IDBManager;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.handlers.IHandlerService;

/** handler for load local backup command.
 * @author Christoph Plutte
 * .
 */
public class LoadLocalBackupHandler extends AbstractHandler implements
		IHandler
{
	/** user rights checker.*/
	private UserRichtsChecker _urChecker = new UserRichtsChecker();

	/** execute method.
	 * @param event to be executed.
	 * @throws ExecutionException ee.
	 * @return null. */
	public final Object execute(final ExecutionEvent event) throws ExecutionException
	{
		if (!_urChecker.isUserGuest())
		{
			String message = NLMessages.getString("LoadLocalBackupHandler_warning0");
			message += NLMessages.getString("LoadLocalBackupHandler_warning1");
			MessageDialog messageDialog = new MessageDialog(HandlerUtil.getActiveWorkbenchWindow(
					event).getShell(), NLMessages.getString("LoadLocalBackupHandler_title"), null,
			        message, MessageDialog.WARNING,
			        new String[] {NLMessages.getString("Handler_yes"), NLMessages.getString("Handler_no") }, 1);
			if (messageDialog.open() == 0)
			{
				DirectoryDialog directoryDialog = new DirectoryDialog(PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getShell());
				directoryDialog.setFilterPath("/"); //$NON-NLS-1$
				directoryDialog.setMessage(NLMessages.getString("LoadLocalBackupHandler_message"));
				directoryDialog.setText(NLMessages.getString("LoadLocalBackupHandler_title2"));
				final String selectedDirectory = directoryDialog.open();
				if (selectedDirectory != null)
				{
	//			    System.out.println(selectedDirectory + " was selected."); //$NON-NLS-1$
					final IDBManager dbm = Facade.getInstanz().getDBManager();

					final ProgressMonitorDialog dialog = new ProgressMonitorDialog(new Shell());
					dialog.setCancelable(false);

					try
					{
						dialog.run(true, true, new IRunnableWithProgress()
						{

							@Override
							public void run(final IProgressMonitor monitor)
							{
								monitor.beginTask("Load Local Backup", 100);
								// if (monitor.isCanceled())
								// {
								// return Status.CANCEL_STATUS;
								// }
								dbm.loadLocalBackup(selectedDirectory, monitor);
								monitor.done();


							}
						});
					}
					catch (InvocationTargetException e)
					{
						e.printStackTrace();
					}
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}

					IHandlerService handlerService = (IHandlerService) PlatformUI.getWorkbench().getService(
							IHandlerService.class);
					try
					{
						handlerService.executeCommand("org.bbaw.pdr.ae.base.commands.RefreshFromDB", null); //$NON-NLS-1$
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

			}
		}
        else
		{
			MessageDialog.openInformation(HandlerUtil.getActiveWorkbenchWindow(
					event).getShell(),
					NLMessages.getString("Commandsr_guest_user"),
					NLMessages.getString("Commandsr_guest_user_denied")); //$NON-NLS-1$
		}
		return null;
	}

}
