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
package org.bbaw.pdr.ae.view.main.preferences;

import org.bbaw.pdr.ae.common.CommonActivator;
import org.bbaw.pdr.ae.common.NLMessages;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * The Class ReferencePage.
 * @author Christoph Plutte
 */
public class ReferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage
{

	/**
	 * Instantiates a new reference page.
	 */
	public ReferencePage()
	{
		super(GRID);

	}

	@Override
	public final void createFieldEditors()
	{

		addField(new BooleanFieldEditor("REFERENCE_VIEW_AUTHOR_SURNAME", //$NON-NLS-1$
				NLMessages.getString("Preference_ref_displayName_surname"), getFieldEditorParent()));

		addField(new BooleanFieldEditor("REFERENCE_VIEW_AUTHOR_FORENAME", //$NON-NLS-1$
				NLMessages.getString("Preference_ref_displayName_forename"), getFieldEditorParent()));

		addField(new BooleanFieldEditor("REFERENCE_VIEW_OTHER_SURNAME", //$NON-NLS-1$
				NLMessages.getString("Preference_ref_displayName_other_names"), getFieldEditorParent()));

		addField(new BooleanFieldEditor("REFERENCE_VIEW_TITLE", //$NON-NLS-1$
				NLMessages.getString("Preference_ref_displayName_title"), getFieldEditorParent()));
		addField(new BooleanFieldEditor("REFERENCE_VIEW_TITLE_PARTNAME", //$NON-NLS-1$
				NLMessages.getString("Preference_ref_displayName_partname"), getFieldEditorParent()));
		addField(new BooleanFieldEditor("REFERENCE_VIEW_TITLE_PARTNUMBER", //$NON-NLS-1$
				NLMessages.getString("Preference_ref_displayName_partnumber"), getFieldEditorParent()));
		addField(new BooleanFieldEditor("REFERENCE_VIEW_SIGNATUR", //$NON-NLS-1$
				NLMessages.getString("Preference_ref_displayName_signatur"), getFieldEditorParent()));
		addField(new BooleanFieldEditor("REFERENCE_VIEW_YEAR", //$NON-NLS-1$
				NLMessages.getString("Preference_ref_displayName_date"), getFieldEditorParent()));
		addField(new BooleanFieldEditor("REFERENCE_VIEW_LOCATION", //$NON-NLS-1$
				NLMessages.getString("Preference_ref_displayName_location"), getFieldEditorParent()));

		Label o = new Label(getFieldEditorParent(), SWT.NONE);
		o.setText("");
		o.setLayoutData(new GridData());
		((GridData) o.getLayoutData()).horizontalSpan = 2;
		addField(new BooleanFieldEditor("REFERENCE_TREE_ONLY_BYHOST", //$NON-NLS-1$
				NLMessages.getString("Preference_show_ref_by_host"), getFieldEditorParent()));
	}

	@Override
	public final void init(final IWorkbench workbench)
	{
		setPreferenceStore(CommonActivator.getDefault().getPreferenceStore());
		setDescription(NLMessages.getString("Preference_reference_preference_title"));
	}
}
