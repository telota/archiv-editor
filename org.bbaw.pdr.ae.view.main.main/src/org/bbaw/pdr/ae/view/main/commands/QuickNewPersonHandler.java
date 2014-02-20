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

import org.bbaw.pdr.ae.common.AEConstants;
import org.bbaw.pdr.ae.common.CommonActivator;
import org.bbaw.pdr.ae.common.NLMessages;
import org.bbaw.pdr.ae.control.core.UserRichtsChecker;
import org.bbaw.pdr.ae.control.facade.Facade;
import org.bbaw.pdr.ae.metamodel.PdrId;
import org.bbaw.pdr.ae.metamodel.Record;
import org.bbaw.pdr.ae.metamodel.Revision;
import org.bbaw.pdr.ae.model.Aspect;
import org.bbaw.pdr.ae.model.Person;
import org.bbaw.pdr.ae.model.Relation;
import org.bbaw.pdr.ae.model.RelationDim;
import org.bbaw.pdr.ae.model.RelationStm;
import org.bbaw.pdr.ae.model.SemanticDim;
import org.bbaw.pdr.ae.model.SemanticStm;
import org.bbaw.pdr.ae.view.main.editors.AspectEditorDialog;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * @author cplutte Handler to call the PersonEditor with PersonEditorInput.
 */
public class QuickNewPersonHandler extends AbstractHandler implements IHandler
{

	/** The ur checker. */
	private UserRichtsChecker _urChecker = new UserRichtsChecker();

	/** The _facade. */
	private Facade _facade = Facade.getInstanz();

	@Override
	public final Object execute(final ExecutionEvent event) throws ExecutionException
	{
		if (!_urChecker.isUserGuest())
		{
			PdrId personId = new PdrId(""); //$NON-NLS-1$
			try
			{
				personId = _facade.getIdService().getNewId("pdrPo"); //$NON-NLS-1$
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			Person p = new Person(personId);
			// System.out.println("neue person mit id von p geholt" +
			// p.getPdrId().toString());

			Revision revision = new Revision();
			revision.setRevisor(_facade.getCurrentUser().getDisplayName());
			revision.setTimeStamp(_facade.getCurrentDate());
			revision.setAuthority(_facade.getCurrentUser().getPdrId());
			revision.setRef(0);
			Record record = new Record();
			record.getRevisions().add(revision);
			p.setRecord(record);
			p.setNew(true);



			PdrId id = new PdrId(""); //$NON-NLS-1$
			try
			{
				id = _facade.getIdService().getNewId("pdrAo"); //$NON-NLS-1$
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			Aspect a = new Aspect(id);

			a.setNew(true);

			a.setRelationDim(new RelationDim());
			RelationStm rs = new RelationStm();
			rs.setSubject(a.getPdrId());
			Relation r = new Relation();
			r.setRelation("aspect_of"); //$NON-NLS-1$
			r.setProvider("PDR"); //$NON-NLS-1$
			r.setObject(personId);
			rs.setRelations(new Vector<Relation>(1));
			rs.getRelations().add(r);
			a.getRelationDim().getRelationStms().add(rs);

			a.setSemanticDim(new SemanticDim());
			a.getSemanticDim().setSemanticStms(new Vector<SemanticStm>());
			SemanticStm sStm = new SemanticStm();
			String provider = Platform
					.getPreferencesService()
					.getString(CommonActivator.PLUGIN_ID, "PRIMARY_SEMANTIC_PROVIDER",
							AEConstants.CLASSIFICATION_AUTHORITY, null).toUpperCase();

			sStm.setProvider(provider); //$NON-NLS-1$
			sStm.setLabel(_facade.getPersonDisplayNameTags(sStm.getProvider()).firstElement());
			a.getSemanticDim().getSemanticStms().add(sStm);

			String message = (NLMessages.getString("Commands_message_quick_person"));
			AspectEditorDialog dialogAspect = new AspectEditorDialog(HandlerUtil.getActiveWorkbenchWindow(event)
					.getShell(), p, a, message);

			if (dialogAspect.open() == 0)
			{
				_facade.setCurrentTreeObjects(new Person[]
				{p});

				_facade.setCurrentPerson(_facade.getPerson(personId));
				Facade.getInstanz().fireUpdateEvent("newPersonTreeRequiered");
			}
			// hier wird injestet
		}
		else
		{
			MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
					NLMessages.getString("Commandsr_guest_user"), NLMessages.getString("Commandsr_guest_user_denied")); //$NON-NLS-1$
		}
		return null;

	}
}
