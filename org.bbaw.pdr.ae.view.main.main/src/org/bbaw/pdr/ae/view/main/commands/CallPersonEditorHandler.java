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
import org.bbaw.pdr.ae.model.Person;
import org.bbaw.pdr.ae.view.main.editors.PersonEditorDialog;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.ui.PlatformUI;

/**
 * @author cplutte Handler to call the PersonEditor with PersonEditorInput.
 */
public class CallPersonEditorHandler extends AbstractHandler implements IHandler
{

	/** The _facade. */
	private Facade _facade = Facade.getInstanz();

	@Override
	public final Object execute(final ExecutionEvent event) throws ExecutionException
	{
		if (_facade.getCurrentPerson() != null)
		{
			try
			{
				_facade.getMainSearcher().getPersonById(_facade.getCurrentPerson().getPdrId());
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

			Person p = _facade.getCurrentPerson().clone();
			PersonEditorDialog dialog = new PersonEditorDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
					p);
			dialog.open();
		}

		// Execute command f�r den alten, nicht Dialog-Editor.
		// try {
		// page.openEditor(input, PersonEditor.ID);
		//
		//
		// }
		// catch (PartInitException e) {
		// System.out.println(e.getStackTrace());
		// }
		// ISelection selection = view.getSite().getSelectionProvider()
		// .getSelection();
		// System.out.println("java ist ein kauderwelsch!");
		// if (selection != null && selection instanceof IStructuredSelection) {
		// Object obj = ((IStructuredSelection) selection).getFirstElement();
		// // If we had a selection lets open the editor
		// System.out.println("es gibt eine selection");
		// if (obj != null) {
		// Person person = (Person) obj;
		// PersonEditorInput input = new PersonEditorInput(person);
		// try {
		// page.openEditor(input, PersonEditor.ID);
		//
		// } catch (PartInitException e) {
		// System.out.println(e.getStackTrace());
		// }
		// }
		// }
		// else {
		// System.out.println("im else teil des cpe");
		// ISelection selected = view._treeViewer.getSelection();
		// Object obj = ((IStructuredSelection) selected).getFirstElement();
		// Person person = (Person) obj;
		//
		// PersonEditorInput input = new PersonEditorInput(person);
		// try {
		// page.openEditor(input, PersonEditor.ID);
		// }
		// catch (PartInitException e) {
		// System.out.println(e.getStackTrace());
		// }
		// }
		return null;
	}
}
