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
package org.bbaw.pdr.ae.config.editor.commands;

import org.bbaw.pdr.ae.config.editor.view.ConfigEditor;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.ui.PlatformUI;

/**
 * The Class OpenConfigEditorHandler.
 *
 * @author Christoph Plutte
 */
public class OpenConfigEditorHandler implements IHandler
{

	/**
	 * @param handlerListener listener
	 * @see org.eclipse.core.commands.IHandler#addHandlerListener(org.eclipse.core.commands.IHandlerListener)
	 */
	public void addHandlerListener(final IHandlerListener handlerListener)
	{

	}

	/**
	 *
	 * @see org.eclipse.core.commands.IHandler#dispose()
	 */
	public void dispose()
	{

	}

	/**
	 * @param event execution event.
	 * @return null
	 * @throws ExecutionException exc.
	 * @see org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 */
	public final Object execute(final ExecutionEvent event) throws ExecutionException
	{
//		System.out.println("open config enditor");
		ConfigEditor dialog = new ConfigEditor(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());

//		System.out.println("ConfigEditor. open!!!");

		dialog.open();
		return null;
	}

	/**
	 * @return true
	 * @see org.eclipse.core.commands.IHandler#isEnabled()
	 */
	public final boolean isEnabled()
	{
		return true;
	}

	/**
	 * @return true
	 * @see org.eclipse.core.commands.IHandler#isHandled()
	 */
	public final boolean isHandled()
	{
		return true;
	}

	/**
	 * @param handlerListener listener
	 * @see org.eclipse.core.commands.IHandler#removeHandlerListener(org.eclipse.core.commands.IHandlerListener)
	 */
	public void removeHandlerListener(final IHandlerListener handlerListener)
	{

	}

}
