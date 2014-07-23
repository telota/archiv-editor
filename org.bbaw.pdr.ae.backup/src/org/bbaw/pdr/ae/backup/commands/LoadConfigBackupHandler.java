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
package org.bbaw.pdr.ae.backup.commands;

import org.bbaw.pdr.ae.common.NLMessages;
import org.bbaw.pdr.ae.config.core.ConfigFactory;
import org.bbaw.pdr.ae.config.core.IConfigFacade;
import org.bbaw.pdr.ae.config.core.IConfigRightsChecker;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

/** handler for load config backup command.
 * @author Christoph Plutte
 * .
 */
public class LoadConfigBackupHandler extends AbstractHandler implements
		IHandler
{
	/** singleton instance of config facade.*/
	private IConfigFacade _configFacade = ConfigFactory.getConfigFacade();

	/** user rights checker.*/
	private IConfigRightsChecker _urChecker = _configFacade.getConfigRichtsChecker();

	/** execute method.
	 * @param event to be executed.
	 * @throws ExecutionException ee.
	 * @return null. */
	public final Object execute(final ExecutionEvent event) throws ExecutionException
	{

		if (_urChecker.mayEditConfig())
		{
			String message = NLMessages.getString("Commands_load_config_backup_warning1");
			message += "\n\n" + NLMessages.getString("Commands_load_config_backup_warning2");
			MessageDialog messageDialog = new MessageDialog(HandlerUtil.getActiveWorkbenchWindow(
					event).getShell(), NLMessages.getString("Commands_load_config_backup_warning3"), null,
			        message, MessageDialog.WARNING,
			        new String[] {NLMessages.getString("Handler_yes"), NLMessages.getString("Handler_no") }, 1); //$NON-NLS-1$ //$NON-NLS-2$
			if (messageDialog.open() == 0)
			{
				DirectoryDialog directoryDialog = new DirectoryDialog(PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getShell());
				directoryDialog.setFilterPath("/"); //$NON-NLS-1$
				directoryDialog.setMessage(NLMessages.getString("LoadLocalBackupHandler_message")); //$NON-NLS-1$
				directoryDialog.setText(NLMessages.getString("LoadLocalBackupHandler_title2")); //$NON-NLS-1$
				String selectedDirectory = directoryDialog.open();
				if (selectedDirectory != null)
				{
	//			    System.out.println(selectedDirectory + " was selected."); //$NON-NLS-1$
					_configFacade.loadLocalConfigBackup(selectedDirectory);
					String info = NLMessages.getString("Commands_message_load_config_backup_success");
					MessageDialog infoDialog = new MessageDialog(HandlerUtil.getActiveWorkbenchWindow(
							event).getShell(), NLMessages.getString("Commands_load_config_backup_successful_title"), null,
					        info, MessageDialog.INFORMATION,
					        new String[] {NLMessages.getString("Handler_ok") }, 0); //$NON-NLS-1$
					infoDialog.open();
				}
				
			}
		}
        else
		{
			MessageDialog.openInformation(HandlerUtil.getActiveWorkbenchWindow(
					event).getShell(),
					NLMessages.getString("Commandsr_guest_user"),
					NLMessages.getString("Commandsr_guest_user_denied")); //$NON-NLS-1$
		}

		return null;
	}

}
