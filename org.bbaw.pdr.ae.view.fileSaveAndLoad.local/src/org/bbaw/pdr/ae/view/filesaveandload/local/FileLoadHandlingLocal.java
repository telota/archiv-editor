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

import org.bbaw.pdr.ae.common.interfaces.IFileLoadHandling;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

/**
 * The Class FileLoadHandlingLocal.
 * @author Christoph Plutte
 */
public class FileLoadHandlingLocal implements IFileLoadHandling
{

	/** The directory. */
	private String _directory;

	/** The extensions. */
	private String[] _extensions;

	/** The file name. */
	private String _fileName;

	@Override
	public final void createFileLoadDialog(final String fileName)
	{
		IWorkbench workbench = PlatformUI.getWorkbench();
		Display display = workbench.getDisplay();
		Shell shell = new Shell(display);
		FileDialog dialog = new FileDialog(shell);
		dialog.setFileName(fileName);
		dialog.setFilterExtensions(_extensions);
		dialog.setFilterPath(_directory);
		this._fileName = dialog.open();

	}

	@Override
	public final String getFileName()
	{
		return this._fileName;
	}

	@Override
	public final void setFilterExtensions(final String[] extensions)
	{
		this._extensions = extensions;

	}

	@Override
	public final void setFilterPath(final String directory)
	{

		this._directory = directory;
	}

}
