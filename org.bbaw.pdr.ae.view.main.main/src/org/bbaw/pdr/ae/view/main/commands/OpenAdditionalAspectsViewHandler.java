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

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

/**
 * The Class OpenAdditionalAspectsViewHandler.
 * @author Christoph Plutte
 */
public class OpenAdditionalAspectsViewHandler implements IHandler
{

	@Override
	public final void addHandlerListener(final IHandlerListener handlerListener)
	{
		// TODO Auto-generated method stub

	}

	/**
	 * @see org.eclipse.core.commands.IHandler#dispose()
	 */
	@Override
	public final void dispose()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public final Object execute(final ExecutionEvent event) throws ExecutionException
	{
		Event ev = (Event) event.getTrigger();
		String id = (String) ev.data;

		IViewPart view = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.findView("org.bbaw.pdr.ae.view.main.views.AspectsView:" + id);
		if (view != null)
		{
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().activate(view);
		}
		else
		{
			try
			{
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
						.showView("org.bbaw.pdr.ae.view.main.views.AspectsView", id, IWorkbenchPage.VIEW_VISIBLE);
			}
			catch (PartInitException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
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
	public final void removeHandlerListener(final IHandlerListener handlerListener)
	{
		// TODO Auto-generated method stub

	}

}
