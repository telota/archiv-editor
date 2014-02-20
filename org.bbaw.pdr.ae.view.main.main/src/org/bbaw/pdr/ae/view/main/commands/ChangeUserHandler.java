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
import org.bbaw.pdr.ae.control.facade.Facade;
import org.bbaw.pdr.ae.view.main.dialogs.LoginDialog;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.PlatformUI;

/**
 * @author cplutte handels the dialog for the edit-user-dialog.
 */
public class ChangeUserHandler extends AbstractHandler implements IHandler
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

		String message = NLMessages.getString("Commandsr_current_user") + _facade.getCurrentUser().getDisplayName();
		message += "\n" + NLMessages.getString("Commandsr_user_id") + _facade.getCurrentUser().getPdrId().toString(); //$NON-NLS-1$
		message += "\n\n" + NLMessages.getString("Commandsr_question_login_as_other"); //$NON-NLS-1$
		MessageDialog messageDialog = new MessageDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
				NLMessages.getString("Commandsr_current_user_data"), null, message, MessageDialog.CONFIRM, new String[]
				{NLMessages.getString("Commandsr_ok"), NLMessages.getString("Handler_cancel")}, 1); //$NON-NLS-2$
		if (messageDialog.open() == 0)
		{
			LoginDialog dialog = new LoginDialog(null, true);
			dialog.create();
			dialog.open();

		}
		// System.out.println("!!!!!!!!!!!!!!!!!!!!! bevor test");
		// IEvaluationService es = (IEvaluationService)
		// HandlerUtil.getActiveWorkbenchWindow(event).
		// getService(IEvaluationService.class);
		// es.requestEvaluation("org.bbaw.pdr.ae.view.rights.userIsAdmin");
		//
		// IEvaluationService service = (IEvaluationService)
		// PlatformUI.getWorkbench().getService(IEvaluationService.class);
		// service.requestEvaluation("org.bbaw.pdr.ae.view.rights.userIsAdmin");
		//
		// System.out.println("!!!!!!!!!!!!!!!!!!!!! nach test");

		return null;
	}

}
