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

import java.util.Vector;

import org.bbaw.pdr.ae.common.NLMessages;
import org.bbaw.pdr.ae.control.core.PDRObjectBuilder;
import org.bbaw.pdr.ae.control.core.UserRichtsChecker;
import org.bbaw.pdr.ae.control.facade.Facade;
import org.bbaw.pdr.ae.metamodel.PdrId;
import org.bbaw.pdr.ae.model.Aspect;
import org.bbaw.pdr.ae.model.Person;
import org.bbaw.pdr.ae.model.Reference;
import org.bbaw.pdr.ae.model.Validation;
import org.bbaw.pdr.ae.model.ValidationStm;
import org.bbaw.pdr.ae.view.main.editors.AspectEditorDialog;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.PlatformUI;

/**
 * @author cplutte this handlerClass opens the AspectEditor and adds a new
 *         Aspect to current object.
 */
public class AddNewAspectHandler extends AbstractHandler implements IHandler
{

	/** The _facade. */
	private Facade _facade = Facade.getInstanz();
	private PDRObjectBuilder _pdrObjectBuilder = new PDRObjectBuilder();

	/** The _ur checker. */
	private UserRichtsChecker _urChecker = new UserRichtsChecker();

	@Override
	public final Object execute(final ExecutionEvent event) throws ExecutionException
	{
		if (!_urChecker.isUserGuest())
		{
			PdrId owningObject = null;
			Person person = null;
			if (_facade.getCurrentPerson() != null
					&& _facade.getCurrentTreeObjects()[_facade.getCurrentTreeObjects().length - 1].getPdrId().equals(
							_facade.getCurrentPerson().getPdrId()))
			{
				owningObject = _facade.getCurrentPerson().getPdrId();
				person = _facade.getCurrentPerson();
			}
			else if (_facade.getCurrentAspect() != null
					&& _facade.getCurrentTreeObjects()[_facade.getCurrentTreeObjects().length - 1].getPdrId().equals(
							_facade.getCurrentAspect().getPdrId()))
			{
				owningObject = _facade.getCurrentAspect().getPdrId();
			}
			else if (_facade.getCurrentReference() != null
					&& _facade.getCurrentTreeObjects()[_facade.getCurrentTreeObjects().length - 1].getPdrId().equals(
							_facade.getCurrentReference().getPdrId()))
			{
				owningObject = _facade.getCurrentReference().getPdrId();
			}
			Aspect a = _pdrObjectBuilder.buildNewAspect(owningObject, null);

			if (owningObject == null && _facade.getCurrentReference() != null
					&& _facade.getCurrentTreeObjects()[_facade.getCurrentTreeObjects().length - 1].getPdrId().equals(
							_facade.getCurrentReference().getPdrId()))
			{
				a.setValidation(new Validation());
				a.getValidation().setValidationStms(new Vector<ValidationStm>());
				a.getValidation().getValidationStms().add(new ValidationStm());
				Reference r = new Reference();
				r.setSourceId(_facade.getCurrentReference().getPdrId());
				r.setQuality("certain");
				r.setAuthority(_facade.getCurrentUser().getPdrId());
				a.getValidation().getValidationStms().lastElement().setReference(r);
			}
			

			_facade.setCurrentAspect(a);

			AspectEditorDialog dialog = new AspectEditorDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
					person, a, NLMessages.getString("Editor_messagePleaseEnterNewAspectData"));
			dialog.open();
		}
		else
		{
			MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
					NLMessages.getString("Commandsr_guest_user"), NLMessages.getString("Commandsr_guest_user_denied")); //$NON-NLS-1$
		}

		return null;
	}

}
