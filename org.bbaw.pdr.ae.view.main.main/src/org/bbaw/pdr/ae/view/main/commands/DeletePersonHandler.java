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
import org.bbaw.pdr.ae.control.core.UserRichtsChecker;
import org.bbaw.pdr.ae.control.facade.Facade;
import org.bbaw.pdr.ae.control.interfaces.IDBManager;
import org.bbaw.pdr.ae.model.Aspect;
import org.bbaw.pdr.ae.model.Person;
import org.bbaw.pdr.ae.model.RelationStm;
import org.bbaw.pdr.ae.model.SemanticStm;
import org.bbaw.pdr.ae.view.control.PDRObjectsProvider;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.PlatformUI;

/**
 * @author cplutte .
 */
public class DeletePersonHandler extends AbstractHandler implements IHandler
{

	/** _facade singleton instance. */
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
		if (_facade.getCurrentPerson() != null)
		{
			if (new UserRichtsChecker().mayDelete(_facade.getCurrentPerson()))
			{
				Person p = _facade.getCurrentPerson();
				Vector<Aspect> belongingToAspects = new Vector<Aspect>();
				boolean toOtherPerson = false;
				PDRObjectsProvider oProvider = new PDRObjectsProvider();
				oProvider.setInput(p);

				for (Aspect a : oProvider.getAspects())
				{
					toOtherPerson = false;
					if (a != null && a.getRelationDim() != null && a.getRelationDim().getRelationStms() != null)
					{
						for (RelationStm rStm : a.getRelationDim().getRelationStms())
						{
							if (rStm.getRelations() != null
									&& (!rStm.getSubject().equals(a.getPdrId()) && !rStm.getRelations().firstElement()
											.getObject().equals(p.getPdrId())))
							{
								toOtherPerson = true;
								break;
							}
						}
						if (!toOtherPerson)
						{
							belongingToAspects.add(a);
						}
					}
				}

				String message = NLMessages.getString("DeletePersonHandler_warning0");
				message += "\n";
				message += belongingToAspects.size() + " Aspects belong only to this person."
						+ "\nDo you really want to delete this person and all aspects that belong only to this person?";
				message += NLMessages.getString("DeletePersonHandler_warning2") + p.getDisplayName();
				message += NLMessages.getString("DeletePersonHandler_lb_id") + p.getPdrId().toString();
				MessageDialog messageDialog = new MessageDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
						NLMessages.getString("DeletePersonHandler_title"), null, message, MessageDialog.WARNING,
						new String[]
						{NLMessages.getString("DeletePersonHandler_delete_person_allaspects"),
								NLMessages.getString("DeletePersonHandler_delete_only_person"),
								NLMessages.getString("Handler_cancel")}, 2);
				int returnCode = messageDialog.open();
				if (returnCode == 0)
				{
					IDBManager dbm = _facade.getDBManager();
					for (Aspect a : belongingToAspects)
					{
						try
						{
							dbm.delete(a.getPdrId(), "aspect");
						}
						catch (Exception e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						_facade.deleteAspectFromLoadedAspects(a);
					}
					try
					{
						dbm.delete(p.getPdrId(), "person");
					}
					catch (Exception e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					} //$NON-NLS-1$ //$NON-NLS-2$
					_facade.deletePersonFromAllPersons(p);
				}
				else if (returnCode == 1)
				{
					IDBManager dbm = _facade.getDBManager();
					// find and delete all aspects that contain the person's
					// name.
					for (Aspect a : belongingToAspects)
					{
						if (a != null && a.getSemanticDim() != null && a.getSemanticDim().getSemanticStms() != null)
						{
							for (SemanticStm sStm : a.getSemanticDim().getSemanticStms())
							{
								if (_facade.isPersonNameTag(sStm.getLabel()))
								{
									try
									{
										dbm.delete(a.getPdrId(), "aspect");
									}
									catch (Exception e)
									{
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									_facade.deleteAspectFromLoadedAspects(a);
									break;
								}
							}

						}
					}
					try
					{
						dbm.delete(p.getPdrId(), "person");
					}
					catch (Exception e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					} //$NON-NLS-1$ //$NON-NLS-2$
					_facade.deletePersonFromAllPersons(p);

				}
			}
			else
			{
				MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
						NLMessages.getString("Commands_no_rights_delete"),
						NLMessages.getString("Command_no_rights_delete_person_message")); //$NON-NLS-1$
			}
		}
		return null;
	}

}
