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
import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * The Class AspectViewPage.
 * @author Christoph Plutte
 */
public class AspectViewPage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage
{

	/**
	 * Instantiates a new aspect view page.
	 */
	public AspectViewPage()
	{
		super(GRID);

	}

	@Override
	public final void createFieldEditors()
	{

		addField(new BooleanFieldEditor("ASPECT_VIEW_ID", //$NON-NLS-1$
				NLMessages.getString("Preference_show_aspect_id"), getFieldEditorParent()));

		addField(new BooleanFieldEditor("ASPECT_VIEW_USERID", //$NON-NLS-1$
				NLMessages.getString("Preference_show_user_id"), getFieldEditorParent()));

		addField(new BooleanFieldEditor("ASPECT_VIEW_RELATIONS", //$NON-NLS-1$
				NLMessages.getString("Preference_show_other_relations"), getFieldEditorParent()));

		addField(new BooleanFieldEditor("ASPECT_VIEW_REFERENCE", //$NON-NLS-1$
				NLMessages.getString("Preference_show_reference"), getFieldEditorParent()));

		addField(new ColorFieldEditor(
				"ASPECT_COLOR_PERSNAME", NLMessages.getString("Preference_markup_color_persName"), //$NON-NLS-1$
				getFieldEditorParent()));
		addField(new ColorFieldEditor("ASPECT_COLOR_ORGNAME", NLMessages.getString("Preference_markup_color_orgName"), //$NON-NLS-1$
				getFieldEditorParent()));
		addField(new ColorFieldEditor(
				"ASPECT_COLOR_PLACENAME", NLMessages.getString("Preference_markup_color_placeName"), //$NON-NLS-1$
				getFieldEditorParent()));
		addField(new ColorFieldEditor("ASPECT_COLOR_DATE", NLMessages.getString("Preference_markup_color_date"), //$NON-NLS-1$
				getFieldEditorParent()));
		addField(new ColorFieldEditor("ASPECT_COLOR_NAME", NLMessages.getString("Preference_markup_color_name"), //$NON-NLS-1$
				getFieldEditorParent()));
	}

	@Override
	public final void init(final IWorkbench workbench)
	{
		setPreferenceStore(CommonActivator.getDefault().getPreferenceStore());
		setDescription(NLMessages.getString("Preference_aspect_view_pref_descrition"));
	}
}
