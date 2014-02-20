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
package org.bbaw.pdr.ae.view.main.commands;

import org.bbaw.pdr.ae.common.AEConstants;
import org.bbaw.pdr.ae.common.CommonActivator;
import org.bbaw.pdr.ae.common.CommonExtensions;
import org.bbaw.pdr.ae.common.NLMessages;
import org.bbaw.pdr.ae.common.interfaces.IFileSaveHandling;
import org.bbaw.pdr.ae.control.facade.Facade;
import org.bbaw.pdr.ae.control.interfaces.IDBManager;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

/**
 * The Class ExportReferenceModsTemplatesHandler.
 * @author Christoph Plutte
 */
public class ExportReferenceModsTemplatesHandler implements IHandler
{

	@Override
	public final void addHandlerListener(final IHandlerListener handlerListener)
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
		String message = "Export Reference Templates";
		IWorkbench workbench = PlatformUI.getWorkbench();
		Display display = workbench.getDisplay();
		Shell shell = new Shell(display);
		MessageDialog messageDialog = new MessageDialog(shell, "Export Reference Templates", null, //$NON-NLS-1$
				message, MessageDialog.INFORMATION, new String[]
				{NLMessages.getString("Handler_yes"), NLMessages.getString("Handler_no")}, 1); //$NON-NLS-1$ //$NON-NLS-2$
		if (messageDialog.open() == 0)
		{
			IFileSaveHandling fileHandling = CommonExtensions.getFileSaveAndLoadFactory().createFileSaveHandling();
			String selectedDirectory = null;
			String fileName = "ReferenceTemplate" + Facade.getInstanz().getCurrentDateAsString();
			if (fileHandling.isLocalFileSystem())
			{
				String lastLoc = Platform.getPreferencesService().getString(CommonActivator.PLUGIN_ID,
						"LAST_EXPORT_DIR", null, null);
				selectedDirectory = fileHandling.createFileSaveDialog(shell, fileName, lastLoc,
						"Export Reference Templates", "Export Reference Templates");
				CommonActivator.getDefault().getPreferenceStore().setValue("LAST_EXPORT_DIR", selectedDirectory);
			}
			else
			{
				selectedDirectory = AEConstants.TEMP_DOWNLOAD_DIR + AEConstants.FS;
			}
			if (selectedDirectory != null)
			{
				IDBManager dbm = Facade.getInstanz().getDBManager();
				try
				{
					dbm.writeToLocalReferenceTemplateBackup(selectedDirectory);
					String info = "Export Reference Templates Successful";
					MessageDialog infoDialog = new MessageDialog(shell, "Export Reference Templates Successful", null,
							info, MessageDialog.INFORMATION, new String[]
							{NLMessages.getString("Handler_ok")}, 0); //$NON-NLS-1$
					infoDialog.open();
					if (!fileHandling.isLocalFileSystem())
					{
						fileHandling.createFileDownloadDialog(selectedDirectory + fileName, fileName,
								"Export Reference Templates", "Click link to download exported Reference Templates");
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();

					String info = "Export Reference Templates Unsuccessful";
					MessageDialog infoDialog = new MessageDialog(shell, "Export Reference Templates Unsuccessful",
							null, info, MessageDialog.WARNING, new String[]
							{NLMessages.getString("Handler_ok")}, 0); //$NON-NLS-1$
					infoDialog.open();
					return Status.OK_STATUS;
				}

			}

		}
		return null;
	}

	@Override
	public final boolean isEnabled()
	{
		return true;
	}

	@Override
	public final boolean isHandled()
	{
		return true;
	}

	@Override
	public void removeHandlerListener(final IHandlerListener handlerListener)
	{
		// TODO Auto-generated method stub

	}

}
