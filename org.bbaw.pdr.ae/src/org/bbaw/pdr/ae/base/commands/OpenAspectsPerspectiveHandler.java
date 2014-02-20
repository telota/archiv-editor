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
package org.bbaw.pdr.ae.base.commands;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;

/** Command Handler for opening AspectsPerspective.
 * @author Christoph Plutte
 *
 */
public class OpenAspectsPerspectiveHandler implements IHandler
{

	@Override
	public void addHandlerListener(final IHandlerListener handlerListener)
	{
	}

	@Override
	public void dispose()
	{
	}

	/** execut.
	 * @param event ex. event.
	 * @return null.
	 * @throws ExecutionException ex.
	 * @see org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 */
	@Override
	public final Object execute(final ExecutionEvent event) throws ExecutionException
	{

		IWorkbench workbench = PlatformUI.getWorkbench();

		IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();

		try
		{
			workbench.showPerspective("org.bbaw.pdr.ae.base.aspectsPerspective", window);
		}
		catch (WorkbenchException e)
		{
			e.printStackTrace();
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
