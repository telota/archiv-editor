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

import java.util.ArrayList;

import org.bbaw.pdr.ae.common.CommonActivator;
import org.bbaw.pdr.ae.common.NLMessages;
import org.bbaw.pdr.ae.control.facade.Facade;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * The Class AspectPage.
 * @author Christoph Plutte
 */
public class AspectPage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage
{

	/** The _facade. */
	private Facade _facade = Facade.getInstanz();

	/**
	 * Instantiates a new aspect page.
	 */
	public AspectPage()
	{
		super(GRID);

	}

	@Override
	public final void createFieldEditors()
	{

		ArrayList<String> cpKeys = new ArrayList<String>(_facade.getConfigs().keySet());
		String[] configProviders = cpKeys.toArray(new String[cpKeys.size()]);
		Label l = new Label(getFieldEditorParent(), SWT.NONE);
		l.setText(NLMessages.getString("Preference_select_primary_provider"));
		Label bl = new Label(getFieldEditorParent(), SWT.NONE);
		bl.setText(""); //$NON-NLS-1$
		addField(new ComboFieldEditor(
				"PRIMARY_SEMANTIC_PROVIDER", //$NON-NLS-1$
				NLMessages.getString("Preference_select_primary_semantic_provider"),
				toStringArrayArray(configProviders), getFieldEditorParent()));
		addField(new ComboFieldEditor(
				"PRIMARY_TAGGING_PROVIDER", //$NON-NLS-1$
				NLMessages.getString("Preference_select_primary_markup_provider"), toStringArrayArray(configProviders),
				getFieldEditorParent()));
		addField(new ComboFieldEditor(
				"PRIMARY_RELATION_PROVIDER", //$NON-NLS-1$
				NLMessages.getString("Preference_select_primary_relation_provider"),
				toStringArrayArray(configProviders), getFieldEditorParent()));
		addField(new BooleanFieldEditor("ASPECT_VIEW_MARKUPSORT_BYPRIORITY", //$NON-NLS-1$
				NLMessages.getString("Preference_sort_markupSemantic_by_priority"), getFieldEditorParent()));
		addField(new IntegerFieldEditor("ASPECT_PRESELECTED_DATE_YEAR", //$NON-NLS-1$
				NLMessages.getString("Preference_preselect_year"), getFieldEditorParent()));

		addField(new BooleanFieldEditor("ASPECT_VIEW_NOTIFICATION_TEMPLATE", //$NON-NLS-1$
				NLMessages.getString("View_use_Notification_Template"), getFieldEditorParent()));

	}

	@Override
	public final void init(final IWorkbench workbench)
	{
		setPreferenceStore(CommonActivator.getDefault().getPreferenceStore());
		setDescription(NLMessages.getString("Preference_aspect_preferences_title"));
	}

	/**
	 * To string array array.
	 * @param strArray the str array
	 * @return the string[][]
	 */
	private String[][] toStringArrayArray(final String[] strArray)
	{
		ArrayList<String[]> l = new ArrayList<String[]>();
		for (String str : strArray)
		{
			l.add(new String[]
			{str, str});
		}
		return l.toArray(new String[l.size()][]);
	}
}
