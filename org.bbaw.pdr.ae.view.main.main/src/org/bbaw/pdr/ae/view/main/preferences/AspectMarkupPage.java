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
import java.util.List;

import org.bbaw.pdr.ae.common.CommonActivator;
import org.bbaw.pdr.ae.common.NLMessages;
import org.bbaw.pdr.ae.view.control.ControlExtensions;
import org.bbaw.pdr.ae.view.control.interfaces.IMarkupFactory;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * The Class AspectViewPage.
 * @author Christoph Plutte
 */
public class AspectMarkupPage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage
{

	private ComboFieldEditor _editor;

	private final String[][] _markupEditors;

	private final String[][] _markupPresentations;

	private final String[][] _referencePresentations;

	private ComboFieldEditor _presentation;

	private ComboFieldEditor _reference;

	{
		IConfigurationElement[] factory = Platform.getExtensionRegistry().getConfigurationElementsFor(
				ControlExtensions.MARKUP_ID);
		List<String> factories = new ArrayList<String>();
		try
		{
			for (IConfigurationElement e : factory)
			{
				// System.out.println("Evaluating extension");
				final Object o = e.createExecutableExtension("class");
				if (o instanceof IMarkupFactory)
				{
					IMarkupFactory markupFactory = (IMarkupFactory) o;
					if (!factories.contains(markupFactory.getMarkupFactoryName()))
					{
						factories.add(markupFactory.getMarkupFactoryName());
					}

				}
			}
		}
		catch (CoreException ex)
		{
			// System.out.println(ex.getMessage());
		}
		String[] facs = (String[]) factories.toArray(new String[factories.size()]);
		ArrayList<String[]> l = new ArrayList<String[]>();
		for (String str : facs)
		{
			l.add(new String[]
			{str, str});
		}
		_markupEditors = l.toArray(new String[l.size()][]);
		_markupPresentations = l.toArray(new String[l.size()][]);
		_referencePresentations = l.toArray(new String[l.size()][]);

	}

	/**
	 * Instantiates a new aspect view page.
	 */
	public AspectMarkupPage()
	{
		super(GRID);

	}

	@Override
	public final void createFieldEditors()
	{

		_editor = new ComboFieldEditor("MARKUP_EDITOR", //$NON-NLS-1$
				"Markup Editor", _markupEditors, getFieldEditorParent());
		addField(_editor);
		_presentation = new ComboFieldEditor("MARKUP_PRESENTATION", //$NON-NLS-1$
				"Markup Presentation", _markupPresentations, getFieldEditorParent());
		addField(_presentation);
		_reference = new ComboFieldEditor("REFERENCE_PRESENTATION", //$NON-NLS-1$
				"Reference Presentation", _referencePresentations, getFieldEditorParent());
		addField(_reference);

		addField(new BooleanFieldEditor("ASPECT_LITE_EDIT_ANA_KEY", //$NON-NLS-1$
				NLMessages.getString("Preference_edit_ana_key"), getFieldEditorParent()));

	}

	@Override
	public final void init(final IWorkbench workbench)
	{
		setPreferenceStore(CommonActivator.getDefault().getPreferenceStore());
		setDescription(NLMessages.getString("Preference_aspect_view_pref_descrition"));
	}
}
