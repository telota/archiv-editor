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
package org.bbaw.pdr.ae.dateparser;

import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * The Class DateParserPage.
 * @author Christoph Plutte
 */
public class DateParserPage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage
{

	/**
	 * Instantiates a new date parser page.
	 */
	public DateParserPage()
	{
		super(GRID);

	}

	@Override
	public final void createFieldEditors()
	{

		addField(new ComboFieldEditor("DATE_PARSING_LANG", //$NON-NLS-1$
				Messages.getString("DateParser_language_of_parser"), //$NON-NLS-1$
				new String[][]
				{
				{"Deutsch", "de"}, {"Italiano", "it"}}, //$NON-NLS-2$ //$NON-NLS-1$
				getFieldEditorParent()));

	}

	@Override
	public final void init(final IWorkbench workbench)
	{
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription(Messages.getString("DateParser_date_parser_preferences"));
	}

}
