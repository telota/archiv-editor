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
package org.bbaw.pdr.ae.view.editorlite.commands;

import org.bbaw.pdr.ae.common.AEConstants;
import org.bbaw.pdr.ae.common.CommonActivator;
import org.bbaw.pdr.ae.control.core.PDRObjectBuilder;
import org.bbaw.pdr.ae.control.facade.Facade;
import org.bbaw.pdr.ae.metamodel.PdrId;
import org.bbaw.pdr.ae.model.Aspect;
import org.bbaw.pdr.ae.model.Person;
import org.bbaw.pdr.ae.model.SemanticStm;
import org.bbaw.pdr.ae.view.editorlite.view.PersonAspectEditor;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.PlatformUI;

public class CreatePersonFromNameStringLiteHandler implements IHandler
{
	private Facade _facade = Facade.getInstanz();
	private PDRObjectBuilder _pdrObjectBuilder = new PDRObjectBuilder();
	@Override
	public void addHandlerListener(IHandlerListener handlerListener)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException
	{
		String personName = event.getParameter("org.bbaw.pdr.ae.view.main.param.personNameString");
		String aspectId = event.getParameter("org.bbaw.pdr.ae.view.main.param.originalAspectID");

		Aspect originalAspect = _facade.getAspect(new PdrId(aspectId));
		Person p = _pdrObjectBuilder.buildNewPerson();


		String provider = Platform
				.getPreferencesService()
				.getString(CommonActivator.PLUGIN_ID, "PRIMARY_SEMANTIC_PROVIDER",
						AEConstants.CLASSIFICATION_AUTHORITY, null).toUpperCase();
		SemanticStm sStm = new SemanticStm();
		sStm.setProvider(provider);
		sStm.setLabel(_facade.getPersonDisplayNameTags(sStm.getProvider()).firstElement());
		Aspect a = _pdrObjectBuilder.buildNewAspect(p.getPdrId(), sStm);
		if (originalAspect != null)
		{
			a.setValidation(originalAspect.getValidation().clone());
			a.setTimeDim(originalAspect.getTimeDim().clone());
			a.setSpatialDim(originalAspect.getSpatialDim().clone());
		}
		a.setNotification(personName);


		p.getAspectIds().add(a.getPdrId());

		PersonAspectEditor editor = new PersonAspectEditor(PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getShell(), p, a);

		if (editor.open() == 0)
		{
			_facade.setCurrentPerson(p);
			Facade.getInstanz().fireUpdateEvent("newPersonTreeRequiered");
		}
		return null;
	}

	@Override
	public boolean isEnabled()
	{
		return true;
	}

	@Override
	public boolean isHandled()
	{
		return true;
	}

	@Override
	public void removeHandlerListener(IHandlerListener handlerListener)
	{
		// TODO Auto-generated method stub

	}

}
