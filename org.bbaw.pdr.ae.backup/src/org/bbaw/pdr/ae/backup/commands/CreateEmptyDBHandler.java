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

import org.bbaw.pdr.ae.common.AEConstants;
import org.bbaw.pdr.ae.common.NLMessages;
import org.bbaw.pdr.ae.control.core.UserRichtsChecker;
import org.bbaw.pdr.ae.control.facade.Facade;
import org.bbaw.pdr.ae.control.interfaces.IDBManager;
import org.bbaw.pdr.ae.control.interfaces.IPdrIdService;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.progress.UIJob;

/** handle for command create empty database.
 * @author Christoph Plutte
 *
 */
public class CreateEmptyDBHandler implements IHandler
{
	/** message return code.*/
	private int _messageReturnCode;
	/** selected directory.*/
	private String _selectedDirectory;
	/** user rights checker.*/
	private UserRichtsChecker _urChecker = new UserRichtsChecker();

	/** ten.*/
	private static final int TEN = 10;
	/** fifty.*/
	private static final int FIFTY = 50;
	/** hundred.*/
	private static final int HUNDRED = 100;
	@Override
	public void addHandlerListener(final IHandlerListener handlerListener)
	{
	}

	@Override
	public void dispose()
	{
	}

	@Override
	public final Object execute(final ExecutionEvent event) throws ExecutionException
	{
		if (!_urChecker.isUserGuest())
		{
			ProgressMonitorDialog dialog = new ProgressMonitorDialog(HandlerUtil
					.getActiveShell(event).getShell());
			try
			{
				dialog.run(true, true, new IRunnableWithProgress() {
					@Override
					public void run(final IProgressMonitor monitor)
					{
						monitor.beginTask("Delete Data from Database and create new and empty Databases",
								HUNDRED);
						boolean backupSuccessfull = false;
						UIJob job = new UIJob("Feedbackup")
						{
							@Override
							public IStatus runInUIThread(final IProgressMonitor monitor)
							{
								IWorkbench workbench = PlatformUI.getWorkbench();
								Display display = workbench.getDisplay();
								Shell shell = new Shell(display);
								String message = NLMessages.getString("Handler_empty_db_warning");
								message += "\n" + NLMessages.getString("Handler_empty_db_warning2");
								MessageDialog messageDialog = new MessageDialog(shell, NLMessages.getString("Handler_empty_db_title"), null,
							        message, MessageDialog.INFORMATION,
							        new String[] {NLMessages.getString("Handler_yes"), //$NON-NLS-1$
									NLMessages.getString("Handler_no") }, 1); //$NON-NLS-1$
								_messageReturnCode = messageDialog.open();
								return Status.OK_STATUS;
							}
						};
						job.setUser(true);
						job.schedule();
						try
						{
							job.join();
						}
						catch (InterruptedException e1)
						{
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						monitor.worked(TEN);
					if (_messageReturnCode == 0)
					{
						job = new UIJob("Feedbackup") {
							@Override
							public IStatus runInUIThread(final IProgressMonitor monitor)
							{
								IWorkbench workbench = PlatformUI.getWorkbench();
								Display display = workbench.getDisplay();
								Shell shell = new Shell(display);
								String message = NLMessages.getString("WriteLocalBackupHandler_warning0"); //$NON-NLS-1$
								message += NLMessages.getString("WriteLocalBackupHandler_warning1"); //$NON-NLS-1$
								MessageDialog messageDialog = new MessageDialog(shell,
										NLMessages.getString("WriteLocalBackupHandler_title"), null, //$NON-NLS-1$
								        message, MessageDialog.WARNING,
								        new String[] {NLMessages.getString("Handler_yes"), //$NON-NLS-1$
									NLMessages.getString("Handler_no") }, 1); //$NON-NLS-1$
								_messageReturnCode = messageDialog.open();
								return Status.OK_STATUS;
							}
						};
						job.setUser(true);
						job.schedule();
						try
						{
							job.join();
						}
						catch (InterruptedException e1)
						{
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						monitor.worked(TEN);
						if (_messageReturnCode == 0)
						{
							job = new UIJob("Feedbackup") {
								@Override
								public IStatus runInUIThread(final IProgressMonitor monitor)
								{
									IWorkbench workbench = PlatformUI.getWorkbench();
									Display display = workbench.getDisplay();
									Shell shell = new Shell(display);
									DirectoryDialog directoryDialog = new DirectoryDialog(shell);
									directoryDialog.setFilterPath("/"); //$NON-NLS-1$
									directoryDialog.setMessage(NLMessages.getString("WriteLocalBackupHandler_message")); //$NON-NLS-1$
									directoryDialog.setText(NLMessages.getString("WriteLocalBackupHandler_title2")); //$NON-NLS-1$
									_selectedDirectory = directoryDialog.open();
									return Status.OK_STATUS;
								}
							};
							job.setUser(true);
							job.schedule();
							try
							{
								job.join();
							}
							catch (InterruptedException e1)
							{
								e1.printStackTrace();
							}
							monitor.worked(TEN);
							if (_selectedDirectory != null)
							{
								IDBManager dbm = Facade.getInstanz().getDBManager();
								try
								{
									dbm.writeToLocalBackup(_selectedDirectory);
									monitor.worked(FIFTY);
									backupSuccessfull = true;
									job = new UIJob("Feedbackup") {
										@Override
										public IStatus runInUIThread(final IProgressMonitor monitor)
										{
											IWorkbench workbench = PlatformUI.getWorkbench();
											Display display = workbench.getDisplay();
											Shell shell = new Shell(display);
											String info = NLMessages.getString("Commands_message_local_backup_success");
											MessageDialog infoDialog = new MessageDialog(shell,
													NLMessages.getString("Commands_title_local_backup_success"), null,
											        info, MessageDialog.INFORMATION,
											        new String[] {NLMessages.getString("Handler_ok") }, 0); //$NON-NLS-1$
											infoDialog.open();
											return Status.OK_STATUS;
										}
									};
									job.setUser(true);
									job.schedule();
									try
									{
										job.join();
									}
									catch (InterruptedException e1)
									{
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
									monitor.worked(TEN);

								}
								catch (Exception e)
								{
									backupSuccessfull = false;
									showBackupErrorDialog();
									e.printStackTrace();
								}
							}
						}
						if (new UserRichtsChecker().isUserPDRAdmin() || backupSuccessfull)
						{
							IDBManager dbm = Facade.getInstanz().getDBManager();
								try
								{
									dbm.createEmptyDB("person");
									monitor.worked(TEN);
									dbm.createEmptyDB("aspect");
									monitor.worked(TEN);
									dbm.createEmptyDB("reference");
									monitor.worked(TEN);
									IPdrIdService idService = Facade.getInstanz().getIdService();
									try
									{
										idService.clearAllUpdateStates();
									}
									catch (Exception e1)
									{
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
									try
									{
										idService.setUpdateTimeStamp(AEConstants.FIRST_EVER_UPDATE_TIMESTAMP);
									}
									catch (Exception e1)
									{
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
									try
									{
										idService.clearAllUpdateStates();
									}
									catch (Exception e)
									{
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									job = new UIJob("Feedbackup") {
										@Override
										public IStatus runInUIThread(final IProgressMonitor monitor)
										{
											Facade.getInstanz().refreshAllData();
											IWorkbench workbench = PlatformUI.getWorkbench();
											Display display = workbench.getDisplay();
											Shell shell = new Shell(display);
											String info = NLMessages.getString("Handler_empty_db_successful");
											MessageDialog infoDialog = new MessageDialog(shell,
													NLMessages.getString("Handler_empty_db_successful"), null,
											        info, MessageDialog.INFORMATION,
											        new String[] {NLMessages.getString("Handler_ok") }, 0); //$NON-NLS-1$
											infoDialog.open();
											return Status.OK_STATUS;
										}
									};
									job.setUser(true);
									job.schedule();
									monitor.worked(TEN);
								}
								catch (Exception e)
								{
									showEmptyErrorDialog();
									e.printStackTrace();
								}
							}
						}
					monitor.done();
					}
				});
			}
			catch (InvocationTargetException e)
			{
				showEmptyErrorDialog();
				e.printStackTrace();
			}
			catch (InterruptedException e)
			{
				showEmptyErrorDialog();
				e.printStackTrace();
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

	/**
	 * show error dialog if error occured.
	 */
	protected final void showEmptyErrorDialog()
	{
		UIJob job = new UIJob("Feedbackup") {
			@Override
			public IStatus runInUIThread(final IProgressMonitor monitor)
			{
				Facade.getInstanz().refreshAllData();

				IWorkbench workbench = PlatformUI.getWorkbench();
				Display display = workbench.getDisplay();
				Shell shell = new Shell(display);
				String info = NLMessages.getString("Handler_empty_db_error");
				MessageDialog infoDialog = new MessageDialog(shell,
						NLMessages.getString("Handler_empty_db_error_title"), null,
				        info, MessageDialog.INFORMATION,
				        new String[] {NLMessages.getString("Handler_ok") }, 0); //$NON-NLS-1$
						infoDialog.open();
		return Status.OK_STATUS;
			}
		};
		job.setUser(true);
		job.schedule();
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

	/**
	 * show backup error dialog.
	 */
	private void showBackupErrorDialog()
	{
		UIJob job = new UIJob("Feedbackup") {
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
				return Status.OK_STATUS;
			}
		};
		job.setUser(true);
		job.schedule();
		try
		{
			job.join();
		}
		catch (InterruptedException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
