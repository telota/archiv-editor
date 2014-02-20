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
package org.bbaw.pdr.ae.collections.commands;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;

import javax.xml.stream.XMLStreamException;

import org.bbaw.pdr.ae.collections.control.CollectionXMLProcessor;
import org.bbaw.pdr.ae.collections.control.CollectionsFacade;
import org.bbaw.pdr.ae.collections.model.PDRCollection;
import org.bbaw.pdr.ae.common.AEConstants;
import org.bbaw.pdr.ae.common.CommonActivator;
import org.bbaw.pdr.ae.common.CommonExtensions;
import org.bbaw.pdr.ae.common.NLMessages;
import org.bbaw.pdr.ae.common.interfaces.IFileSaveHandling;
import org.bbaw.pdr.ae.control.facade.Facade;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

/** handler for command to save clipboard to filesystem.
 * @author Christoph Plutte
 *
 */
public class SaveClipboardHandler implements IHandler
{

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
		PDRCollection coll = CollectionsFacade.getInstance().getClipboard();
		if (coll != null)
		{
			String message = "Save and Write Clipboard to File";
			IWorkbench workbench = PlatformUI.getWorkbench();
			Display display = workbench.getDisplay();
			Shell shell = new Shell(display);
			MessageDialog messageDialog = new MessageDialog(shell, message, null, //$NON-NLS-1$
					message, MessageDialog.INFORMATION, new String[]
					{NLMessages.getString("Handler_yes"), NLMessages.getString("Handler_no")}, 1); //$NON-NLS-1$ //$NON-NLS-2$
			if (messageDialog.open() == 0)
			{
				IFileSaveHandling fileHandling = CommonExtensions.getFileSaveAndLoadFactory().createFileSaveHandling();
				String selectedDirectory = null;
				SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
				String fileName = coll.getName()
						+ dateFormate.format(Facade.getInstanz().getCurrentDate());
				coll.setName(fileName);
				fileName += ".xml";
				if (fileHandling.isLocalFileSystem())
				{
					String lastLoc = Platform.getPreferencesService().getString(CommonActivator.PLUGIN_ID,
							"LAST_EXPORT_DIR", null, null);
					selectedDirectory = fileHandling.createFileSaveDialog(shell, fileName, lastLoc, message, message);
				}
				else
				{
					selectedDirectory = AEConstants.TEMP_DOWNLOAD_DIR + AEConstants.FS;
				}
				if (selectedDirectory != null)
				{
					CommonActivator.getDefault().getPreferenceStore().setValue("LAST_EXPORT_DIR", selectedDirectory);
					CollectionXMLProcessor xmlProc = new CollectionXMLProcessor();
					String xml = null;
					try
					{
						xml = xmlProc.writeToXML(coll);
					}
					catch (XMLStreamException e1)
					{
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					try
					{
						selectedDirectory = selectedDirectory + AEConstants.FS + fileName;
						// File f;
						// f = new File(selectedDirectory);
						Writer out = new OutputStreamWriter(new FileOutputStream(selectedDirectory), "UTF-8");
						try
						{
							out.write(xml);
						}
						finally
						{
							out.close();
						}
						String info = "Save and Write Clipboard to File Successful";
						MessageDialog infoDialog = new MessageDialog(shell, info, null, info,
								MessageDialog.INFORMATION, new String[]
								{NLMessages.getString("Handler_ok")}, 0); //$NON-NLS-1$
						infoDialog.open();
						if (!fileHandling.isLocalFileSystem())
						{
							fileHandling.createFileDownloadDialog(selectedDirectory + fileName, fileName, message,
									"Click link to download exported Collection");
						}
					}
					catch (Exception e)
					{
						e.printStackTrace();

						String info = "Save and Write Clipboard to File Unsuccessful";
						MessageDialog infoDialog = new MessageDialog(shell, info, null, info, MessageDialog.WARNING,
								new String[]
								{NLMessages.getString("Handler_ok")}, 0); //$NON-NLS-1$
						infoDialog.open();
					}
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
	}

}
