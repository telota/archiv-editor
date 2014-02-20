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

import org.bbaw.pdr.ae.control.facade.Facade;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.IHandlerService;

/**
 * @author cplutte handles the command to open a Person, Aspect, or Source.
 */
public class DeleteHandler extends AbstractHandler implements IHandler
{

	/** The _facade. */
	private Facade _facade = Facade.getInstanz();

	/**
	 * execute method.
	 * @param event to be executed.
	 * @throws ExecutionException ee.
	 * @return null.
	 */

	@Override
	public final Object execute(final ExecutionEvent event) throws ExecutionException
	{

		if (_facade.getCurrentPdrObject() != null)
		{
			if (_facade.getCurrentPdrObject().getPdrId().getType().equals("pdrAo")
					&& _facade.getCurrentAspect() != null)
			{
				IHandlerService handlerService = (IHandlerService) PlatformUI.getWorkbench().getService(
						IHandlerService.class);
				try
				{
					handlerService.executeCommand("org.bbaw.pdr.ae.view.commands.DeleteAspect", null); //$NON-NLS-1$
				}
				catch (ExecutionException e)
				{
					e.printStackTrace();
				}
				catch (NotDefinedException e)
				{
					e.printStackTrace();
				}
				catch (NotEnabledException e)
				{
					e.printStackTrace();
				}
				catch (NotHandledException e)
				{
					e.printStackTrace();
				}
			}
			else if (_facade.getCurrentPdrObject().getPdrId().getType().equals("pdrPo")
					&& _facade.getCurrentPerson() != null)
			{
				IHandlerService handlerService = (IHandlerService) PlatformUI.getWorkbench().getService(
						IHandlerService.class);
				try
				{
					handlerService.executeCommand("org.bbaw.pdr.ae.view.commands.DeletePerson", null); //$NON-NLS-1$
				}
				catch (ExecutionException e)
				{
					e.printStackTrace();
				}
				catch (NotDefinedException e)
				{
					e.printStackTrace();
				}
				catch (NotEnabledException e)
				{
					e.printStackTrace();
				}
				catch (NotHandledException e)
				{
					e.printStackTrace();
				}
			}
			else if (_facade.getCurrentPdrObject().getPdrId().getType().equals("pdrRo")
					&& _facade.getCurrentReference() != null)
			{
				IHandlerService handlerService = (IHandlerService) PlatformUI.getWorkbench().getService(
						IHandlerService.class);
				try
				{
					handlerService.executeCommand("org.bbaw.pdr.ae.view.commands.DeleteReference", null); //$NON-NLS-1$
				}
				catch (ExecutionException e)
				{
					e.printStackTrace();
				}
				catch (NotDefinedException e)
				{
					e.printStackTrace();
				}
				catch (NotEnabledException e)
				{
					e.printStackTrace();
				}
				catch (NotHandledException e)
				{
					e.printStackTrace();
				}
			}
		}
		return null;
	}
}
