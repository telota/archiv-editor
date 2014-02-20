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

import org.bbaw.pdr.ae.common.NLMessages;
import org.bbaw.pdr.ae.control.core.UserRichtsChecker;
import org.bbaw.pdr.ae.control.facade.Facade;
import org.bbaw.pdr.ae.control.interfaces.IPdrIdService;
import org.bbaw.pdr.ae.metamodel.PdrId;
import org.bbaw.pdr.ae.view.main.editors.SourceEditorDialog;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * The Class NewReferenceHandler.
 * @author Christoph Plutte
 */
public class NewReferenceHandler extends AbstractHandler implements IHandler
{

	/** The ur checker. */
	private UserRichtsChecker _urChecker = new UserRichtsChecker();

	@Override
	public final Object execute(final ExecutionEvent event) throws ExecutionException
	{
		if (!_urChecker.isUserGuest())
		{
			IPdrIdService idService = Facade.getInstanz().getIdService();
			PdrId id = null;
			try
			{
				id = idService.getNewId("pdrRo");
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			PdrId callingRefId;
			if (event != null && event.getTrigger() != null)
			{
				Event ev = (Event) event.getTrigger();
				if (ev.data != null)
				{
					callingRefId = (PdrId) ev.data;
					if (callingRefId != null && id.equalsOrSmalerThan(callingRefId))
					{
						id = new PdrId(id.getType(), id.getInstance(), id.getProjectID(), callingRefId.getId() + 1);
						try
						{
							idService.insertIdNewObject(id);
						}
						catch (Exception e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
			Facade.getInstanz().setCurrentReferenceToNull();

			if (event != null && HandlerUtil.getActiveWorkbenchWindow(event) != null)
			{
				SourceEditorDialog dialog = new SourceEditorDialog(HandlerUtil.getActiveWorkbenchWindow(event)
						.getShell(), id);
				dialog.open();
			}
			else
			{
				IWorkbench workbench = PlatformUI.getWorkbench();
				Display display = workbench.getDisplay();
				Shell shell = new Shell(display);
				SourceEditorDialog dialog = new SourceEditorDialog(shell, id);
				dialog.open();
			}
		}
		else
		{
			Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
			MessageDialog.openInformation(shell,
					NLMessages.getString("Commandsr_guest_user"), NLMessages.getString("Commandsr_guest_user_denied")); //$NON-NLS-1$
		}

		return null;
	}

}
