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
package org.bbaw.pdr.ae.view.identifiers.internal;

import org.bbaw.pdr.ae.common.CommonActivator;
import org.bbaw.pdr.ae.common.NLMessages;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * preference page for person identifier settings.
 * @author Christoph Plutte
 */
public class IdentifierPage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage
{

	/**
	 * constructor.
	 */
	public IdentifierPage()
	{
		super(GRID);

	}

	@Override
	public final void createFieldEditors()
	{

		addField(new BooleanFieldEditor("IDENTIFIER_PND", //$NON-NLS-1$
				NLMessages.getString("Preference_pnd_name"), getFieldEditorParent()));

		addField(new StringFieldEditor("IDENTIFIER_PND_URL", NLMessages.getString("Preference_pnd_url"), //$NON-NLS-1$
				getFieldEditorParent()));

		addField(new BooleanFieldEditor("IDENTIFIER_LCCN", //$NON-NLS-1$
				NLMessages.getString("Preference_lccn_name"), getFieldEditorParent()));

		addField(new StringFieldEditor("IDENTIFIER_LCCN_URL", NLMessages.getString("Preference_lccn_url"), //$NON-NLS-1$
				getFieldEditorParent()));

		addField(new BooleanFieldEditor("IDENTIFIER_VIAF", //$NON-NLS-1$
				NLMessages.getString("Preference_viaf_name"), getFieldEditorParent()));

		addField(new StringFieldEditor("IDENTIFIER_VIAF_URL", NLMessages.getString("Preference_viaf_url"), //$NON-NLS-1$
				getFieldEditorParent()));

		addField(new BooleanFieldEditor("IDENTIFIER_ICCU", //$NON-NLS-1$
				NLMessages.getString("Preference_iccu_name"), getFieldEditorParent()));

		addField(new StringFieldEditor("IDENTIFIER_ICCU_URL", NLMessages.getString("Preference_iccu_url"), //$NON-NLS-1$
				getFieldEditorParent()));

	}

	@Override
	public final void init(final IWorkbench workbench)
	{
		setPreferenceStore(CommonActivator.getDefault().getPreferenceStore());
		setDescription(NLMessages.getString("Preference_identifier_preference_title"));
	}
}
