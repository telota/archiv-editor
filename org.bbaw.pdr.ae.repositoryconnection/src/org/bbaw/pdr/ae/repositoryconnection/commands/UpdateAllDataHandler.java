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

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;

import javax.xml.stream.XMLStreamException;

import org.bbaw.pdr.ae.common.NLMessages;
import org.bbaw.pdr.ae.control.core.UserRichtsChecker;
import org.bbaw.pdr.ae.control.facade.Facade;
import org.bbaw.pdr.ae.control.interfaces.IUpdateManager;
import org.bbaw.pdr.ae.model.User;
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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.progress.UIJob;

/**
 * update all data handler for corresponding command.
 * @author Christoph Plutte
 */
public class UpdateAllDataHandler implements IHandler
{
	/** user rights checker. */
	private UserRichtsChecker _urChecker = new UserRichtsChecker();

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

			ProgressMonitorDialog dialog = new ProgressMonitorDialog(HandlerUtil.getActiveShell(event).getShell());
			dialog.setCancelable(true);

			try
			{
				dialog.run(true, true, new IRunnableWithProgress()
				{
					private Object _updateStatus;

					@Override
					public void run(final IProgressMonitor monitor)
					{
						// Activator.getDefault().getPreferenceStore().getString("REPOSITORY_URL"));
						if (monitor.isCanceled())
						{
							monitor.setCanceled(true);
						}
						final User user = Facade.getInstanz().getCurrentUser();

						IUpdateManager[] updateManagers = Facade.getInstanz().getUpdateManagers();
						for (IUpdateManager manager : updateManagers)
						{
							try
							{
								if (user != null)
								{
									_updateStatus = manager.updateAllData(user.getPdrId().toString(), user
											.getAuthentication()
											.getPassword(), monitor);
								}
							}
							catch (final XMLStreamException e)
							{
								e.printStackTrace();

								UIJob job = new UIJob("Feedbackup")
								{
									@Override
									public IStatus runInUIThread(final IProgressMonitor monitor)
									{
										IWorkbench workbench = PlatformUI.getWorkbench();
										Display display = workbench.getDisplay();
										Shell shell = new Shell(display);
										String info = NLMessages.getString("Command_update_error") + "\n\n"
												+ e.getMessage();
										MessageDialog infoDialog = new MessageDialog(shell, NLMessages
												.getString("Command_update_error"), null, info, MessageDialog.ERROR,
												new String[]
												{"OK"}, 0); //$NON-NLS-1$
										infoDialog.open();

										return Status.OK_STATUS;
									}
								};
								job.setUser(true);
								job.schedule();
							}
							catch (final UnsupportedEncodingException e)
							{
								e.printStackTrace();

								UIJob job = new UIJob("Feedbackup")
								{
									@Override
									public IStatus runInUIThread(final IProgressMonitor monitor)
									{
										IWorkbench workbench = PlatformUI.getWorkbench();
										Display display = workbench.getDisplay();
										Shell shell = new Shell(display);
										String info = NLMessages.getString("Command_update_error") + "\n\n"
												+ e.getMessage();
										MessageDialog infoDialog = new MessageDialog(shell, NLMessages
												.getString("Command_update_error"), null, info, MessageDialog.ERROR,
												new String[]
												{"OK"}, 0); //$NON-NLS-1$
										infoDialog.open();

										return Status.OK_STATUS;
									}
								};
								job.setUser(true);
								job.schedule();
							}
							catch (final Exception e)
							{
								e.printStackTrace();

								UIJob job = new UIJob("Feedbackup")
								{
									@Override
									public IStatus runInUIThread(final IProgressMonitor monitor)
									{
										IWorkbench workbench = PlatformUI.getWorkbench();
										Display display = workbench.getDisplay();
										Shell shell = new Shell(display);
										String info = NLMessages.getString("Command_update_error") + "\n\n"
												+ e.getMessage();
										MessageDialog infoDialog = new MessageDialog(shell, NLMessages
												.getString("Command_update_error"), null, info, MessageDialog.ERROR,
												new String[]
												{"OK"}, 0); //$NON-NLS-1$
										infoDialog.open();
										return Status.OK_STATUS;
									}
								};
								job.setUser(true);
								job.schedule();
							}
						} // for-loop

						UIJob job = new UIJob("Feedbackup")
						{
							@Override
							public IStatus runInUIThread(final IProgressMonitor monitor)
							{
								Facade.getInstanz().refreshAllData();

								IWorkbench workbench = PlatformUI.getWorkbench();
								Display display = workbench.getDisplay();
								Shell shell = new Shell(display);
								String info = null;
								if (_updateStatus != null && _updateStatus.equals(Status.OK_STATUS))
								{
									info = NLMessages.getString("Command_update_successful");
								}
								else
								{
									info = NLMessages.getString("Command_update_error");
								}
								MessageDialog infoDialog = new MessageDialog(shell, info, null, info,
										MessageDialog.INFORMATION,
										new String[]
										{"OK"}, 0); //$NON-NLS-1$
								infoDialog.open();
								return Status.OK_STATUS;
							}
						};
						job.setUser(true);
						job.schedule();

						monitor.done();
					}
				});
			}
			catch (final InvocationTargetException e)
			{
				e.printStackTrace();

				UIJob job = new UIJob("Feedbackup")
				{
					@Override
					public IStatus runInUIThread(final IProgressMonitor monitor)
					{
						IWorkbench workbench = PlatformUI.getWorkbench();
						Display display = workbench.getDisplay();
						Shell shell = new Shell(display);
						String info = NLMessages.getString("Command_update_error") + e.toString() + "\n\n"
								+ e.getMessage();
						MessageDialog infoDialog = new MessageDialog(shell,
								NLMessages.getString("Command_update_error"), null, info, MessageDialog.ERROR,
								new String[]
								{"OK"}, 0); //$NON-NLS-1$
						infoDialog.open();
						return Status.OK_STATUS;
					}
				};
				job.setUser(true);
				job.schedule();
			}
			catch (final InterruptedException e)
			{
				e.printStackTrace();

				UIJob job = new UIJob("Feedbackup")
				{
					@Override
					public IStatus runInUIThread(final IProgressMonitor monitor)
					{
						IWorkbench workbench = PlatformUI.getWorkbench();
						Display display = workbench.getDisplay();
						Shell shell = new Shell(display);
						String info = NLMessages.getString("Command_update_error") + e.toString() + "\n\n"
								+ e.getMessage();
						MessageDialog infoDialog = new MessageDialog(shell,
								NLMessages.getString("Command_update_error"), null, info, MessageDialog.ERROR,
								new String[]
								{"OK"}, 0); //$NON-NLS-1$
						infoDialog.open();
						return Status.OK_STATUS;
					}
				};
				job.setUser(true);
				job.schedule();
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
