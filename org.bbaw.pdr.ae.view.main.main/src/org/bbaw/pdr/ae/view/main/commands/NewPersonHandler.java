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
import org.bbaw.pdr.ae.metamodel.PdrId;
import org.bbaw.pdr.ae.model.Person;
import org.bbaw.pdr.ae.view.main.editors.PersonEditorDialog;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.PlatformUI;

/**
 * @author cplutte Handler to call the PersonEditor with PersonEditorInput.
 */
public class NewPersonHandler extends AbstractHandler implements IHandler
{

	/** The ur checker. */
	private UserRichtsChecker _urChecker = new UserRichtsChecker();

	@Override
	public final Object execute(final ExecutionEvent event) throws ExecutionException
	{
		if (!_urChecker.isUserGuest())
		{
			PdrId id = new PdrId("");
			try
			{
				id = Facade.getInstanz().getIdService().getNewId("pdrPo");
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			Person p = new Person(id);
			p.setNew(true);
			Facade.getInstanz().setCurrentPerson(p);
			// System.out.println("neue person mit id von p geholt" +
			// p.getPdrId().toString());
			PersonEditorDialog dialog = new PersonEditorDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
					p);
			if (dialog.open() == 0)
			{
				Facade.getInstanz().fireUpdateEvent("newPersonTreeRequiered");
			}

		}
		else
		{
			MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
					NLMessages.getString("Commandsr_guest_user"), NLMessages.getString("Commandsr_guest_user_denied")); //$NON-NLS-1$
		}
		return null;
	}
}
