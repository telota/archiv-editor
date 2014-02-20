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

import org.bbaw.pdr.ae.common.NLMessages;
import org.bbaw.pdr.ae.control.facade.Facade;
import org.bbaw.pdr.ae.control.interfaces.IDBManager;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.progress.UIJob;

/**
 * @author cplutte
 * .
 */
public class WriteLocalBackupHandler extends AbstractHandler implements
		IHandler
{
	/** backup successfull.*/
	private boolean _successfull;
	/** execute method.
	 * @param event to be executed.
	 * @throws ExecutionException ee.
	 * @return null. */
	public final Object execute(final ExecutionEvent event) throws ExecutionException
	{

		String message = NLMessages.getString("WriteLocalBackupHandler_warning0"); //$NON-NLS-1$
		message += NLMessages.getString("WriteLocalBackupHandler_warning1"); //$NON-NLS-1$
		MessageDialog messageDialog = new MessageDialog(HandlerUtil.getActiveWorkbenchWindow(
				event).getShell(), NLMessages.getString("WriteLocalBackupHandler_title"), null, //$NON-NLS-1$
		        message, MessageDialog.WARNING,
		        new String[] {NLMessages.getString("Handler_yes"), NLMessages.getString("Handler_no") }, 1); //$NON-NLS-1$ //$NON-NLS-2$
		if (messageDialog.open() == 0)
		{
			DirectoryDialog directoryDialog = new DirectoryDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow()
					.getShell());
			directoryDialog.setFilterPath("/"); //$NON-NLS-1$
			directoryDialog.setMessage(NLMessages.getString("WriteLocalBackupHandler_message")); //$NON-NLS-1$
			directoryDialog.setText(NLMessages.getString("WriteLocalBackupHandler_title2")); //$NON-NLS-1$
			final String selectedDirectory = directoryDialog.open();
			if (selectedDirectory != null)
			{
				Job job = new Job("Backup") {
					@Override
					protected IStatus run(final IProgressMonitor monitor)
					{
						IDBManager dbm = Facade.getInstanz().getDBManager();
						try
						{
							dbm.writeToLocalBackup(selectedDirectory);
							_successfull = true;
							UIJob job = new UIJob("Backup Information") {
								@Override
								public IStatus runInUIThread(final IProgressMonitor monitor)
								{
									IWorkbench workbench = PlatformUI.getWorkbench();
									Display display = workbench.getDisplay();
									Shell shell = new Shell(display);
									String info = NLMessages.getString("Commands_message_local_backup_success");
									MessageDialog infoDialog = new MessageDialog(shell,
											NLMessages.getString("Commands_title_local_backup_success"), null, //$NON-NLS-1$
									        info, MessageDialog.INFORMATION,
									        new String[] {NLMessages.getString("Handler_ok") }, 0); //$NON-NLS-1$
									infoDialog.open();
									return Status.OK_STATUS;
								}
							};
							job.setUser(true);
							job.schedule();
						}
						catch (Exception e)
						{
							UIJob job = new UIJob("Backup Information") {
								@Override
								public IStatus runInUIThread(final IProgressMonitor monitor)
								{
									IWorkbench workbench = PlatformUI.getWorkbench();
									Display display = workbench.getDisplay();
									Shell shell = new Shell(display);
									String info = NLMessages.getString("Handler_backup_error");
									MessageDialog infoDialog = new MessageDialog(shell, NLMessages.getString("Handler_backup_error_title"), null,
									        info, MessageDialog.INFORMATION,
									        new String[] {NLMessages.getString("Handler_ok") }, 0); //$NON-NLS-1$
									infoDialog.open();
									return Status.CANCEL_STATUS;
								}
							};
							job.setUser(true);
							job.schedule();
							e.printStackTrace();
						}
						if (monitor.isCanceled())
						{
							return Status.CANCEL_STATUS;
						}
						return Status.CANCEL_STATUS;
					}
				};
				job.setUser(true);
				job.schedule();

			}

		}

		// System.out.println("write local bakcu2 " + _successfull);

		return new Boolean(_successfull);
	}

}
