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
import org.bbaw.pdr.ae.control.interfaces.IDBManager;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.PlatformUI;

/**
 * @author cplutte .
 */
public class DeleteReferenceHandler extends AbstractHandler implements IHandler
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

		if (_facade.getCurrentReference() != null)
		{
			if (new UserRichtsChecker().mayDelete(_facade.getCurrentReference()))
			{

				String message = NLMessages.getString("DeleteReferenceHandler_warning0");
				message += NLMessages.getString("DeleteReferenceHandler_warning1");
				message += NLMessages.getString("DeleteReferenceHandler_warning2")
						+ _facade.getCurrentReference().getDisplayNameLong();
				message += NLMessages.getString("DeleteReferenceHandler_ln_id")
						+ _facade.getCurrentReference().getPdrId().toString();
				MessageDialog messageDialog = new MessageDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
						NLMessages.getString("DeleteReferenceHandler_title"), null, message, MessageDialog.WARNING,
						new String[]
						{NLMessages.getString("Handler_delete"), NLMessages.getString("Handler_cancel")}, 1);
				if (messageDialog.open() == 0)
				{
					IDBManager dbm = _facade.getDBManager();
					try
					{
						dbm.delete(_facade.getCurrentReference().getPdrId(), "reference");
					}
					catch (Exception e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					} //$NON-NLS-1$ //$NON-NLS-2$
					_facade.deleteReference(_facade.getCurrentReference());
				}
			}
			else
			{
				MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
						NLMessages.getString("Commands_no_rights_delete"),
						NLMessages.getString("Command_no_rights_delete_reference_message")); //$NON-NLS-1$
			}
		}

		return null;
	}

}
