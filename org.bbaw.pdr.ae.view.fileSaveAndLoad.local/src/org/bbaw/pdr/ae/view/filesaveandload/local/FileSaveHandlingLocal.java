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
package org.bbaw.pdr.ae.view.filesaveandload.local;

import org.bbaw.pdr.ae.common.interfaces.IFileSaveHandling;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

/**
 * The Class FileSaveHandlingLocal.
 * @author Christoph Plutte
 */
public class FileSaveHandlingLocal implements IFileSaveHandling
{

	/**
	 * Creates the file download dialog.
	 * @param fileName the file name
	 * @param linkName the link name
	 * @param title the title
	 * @param message the message
	 */
	@Override
	public void createFileDownloadDialog(final String fileName, final String linkName, final String title,
			final String message)
	{

	}

	/**
	 * Creates the file save dialog.
	 * @param composite the composite
	 * @param fileName the file name
	 * @param directory the directory
	 * @param title the title
	 * @param message the message
	 * @return the string
	 */
	@Override
	public final String createFileSaveDialog(final Composite composite, final String fileName, String directory,
			final String title, final String message)
	{
		IWorkbench workbench = PlatformUI.getWorkbench();
		Display display = workbench.getDisplay();
		Shell shell = new Shell(display);
		// FileSaveDialog dialog = new FileSaveDialog(shell, fileName,
		// directory, title, message);
		DirectoryDialog dirDialog = new DirectoryDialog(shell);
		dirDialog.setFilterPath(directory); //$NON-NLS-1$
		dirDialog.setMessage(message);
		dirDialog.setText(title);
		// fileDialog.setFilterExtensions(extensions);
		directory = dirDialog.open();
		// if (extensions != null)
		// {
		// directoryDialog.set
		// }
		// String dir = dirDialog.open();
		// String dir = directoryDialog.getDirectory();
		return directory;
	}

	/**
	 * Checks if is local file system.
	 * @return true, if is local file system
	 */
	@Override
	public final boolean isLocalFileSystem()
	{
		return true;
	}

	/**
	 * Sets the filter extensions.
	 * @param extensions the new filter extensions
	 */
	@Override
	public void setFilterExtensions(final String[] extensions)
	{

	}

}
