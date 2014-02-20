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
package org.bbaw.pdr.ae.view.identifiers.commands;

import org.bbaw.pdr.ae.view.identifiers.dialogs.BrowserDialog;
import org.bbaw.pdr.ae.view.identifiers.view.BrowserViewPart;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

/**
 * opens the dialog for the identifier browser to look for eg. PND.
 * @author cplutte
 */
public class OpenBrowserDialogHandler extends AbstractHandler implements IHandler
{

	@Override
	public final Object execute(final ExecutionEvent event) throws ExecutionException
	{
		String url = event.getParameter("org.bbaw.pdr.ae.view.identifiers.url");
		IWorkbench workbench = PlatformUI.getWorkbench();
		Display display = workbench.getDisplay();
		IViewPart view = workbench.getActiveWorkbenchWindow().getActivePage()
				.findView("org.bbaw.pdr.ae.view.identifiers.view.BrowserView");
		if (view != null && view instanceof BrowserViewPart)
		{
			((BrowserViewPart) view).openURL(url);
		}
		else
		{
			Shell shell = new Shell(display);
			BrowserDialog dialog = new BrowserDialog(url, shell);
			dialog.open();
		}

		return null;

	}

}
