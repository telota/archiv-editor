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
package org.bbaw.pdr.ae.view.filesaveandload.web;

import org.bbaw.pdr.ae.common.interfaces.IFileSaveHandling;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

public class FileSaveHandlingWeb implements IFileSaveHandling {

	@Override
	public boolean isLocalFileSystem() {
		return false;
	}

	@Override
	public void createFileDownloadDialog(String fileName, String linkName,
			String title, String message)
	{
		IWorkbench workbench = PlatformUI.getWorkbench();
		Display display = workbench.getDisplay();
		Shell shell = new Shell(display);
		FileDownloadDialog dialog = new FileDownloadDialog(shell, fileName, linkName, title, message);
				dialog.open();

	}

	@Override
	public String createFileSaveDialog(Composite composite, String fileName,
			String directory, String title, String message) {
		return null;
	}

	@Override
	public void setFilterExtensions(String[] extensions) {
		// TODO Auto-generated method stub
		
	}

}
