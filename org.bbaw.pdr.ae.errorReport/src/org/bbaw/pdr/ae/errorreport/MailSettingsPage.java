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
/*
 * @author: Christoph Plutte
 */
package org.bbaw.pdr.ae.errorreport;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * The Class MailSettingsPage.
 * @author Christoph Plutte
 */
public class MailSettingsPage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage
{

	/**
	 * Instantiates a new mail settings page.
	 */
	public MailSettingsPage()
	{
		super(GRID);

	}

	@Override
	public final void createFieldEditors()
	{
		addField(new StringFieldEditor("MAIL_SMTP_HOST_NAME", "SMTP Host Name:", getFieldEditorParent()));
		addField(new StringFieldEditor("MAIL_SMTP_AUTH_USER", "SMTP User Name:", getFieldEditorParent()));
		// addField(new StringFieldEditor("MAIL_SMTP_AUTH_PWD",
		// "SMTP User Password:",
		// getFieldEditorParent()));
		addField(new StringFieldEditor("MAIL_ADRESS_SENDER", "Sender Email Adress:", getFieldEditorParent()));

	}

	@Override
	public final void init(final IWorkbench workbench)
	{

		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("Settings of Email Account for Error Report");
	}
}
