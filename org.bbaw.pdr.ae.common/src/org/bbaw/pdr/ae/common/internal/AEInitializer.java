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
package org.bbaw.pdr.ae.common.internal;

import org.bbaw.pdr.ae.common.AEConstants;
import org.bbaw.pdr.ae.common.CommonActivator;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

/** preference initializer. initializes default preference values from AEConstants which load these
 * values form config.properties file.
 * @author Christoph Plutte
 *
 */
public class AEInitializer extends AbstractPreferenceInitializer
{

	/**
	 * constructor.
	 */
	public AEInitializer()
	{
	}


	/**
	 * initializes default preferences.
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
	 */
	@Override
	public final void initializeDefaultPreferences()
	{
		IPreferenceStore store = CommonActivator.getDefault().getPreferenceStore();
		// System.out.println(AEConstants.CLASSIFICATION_AUTHORITY);
		store.setDefault("FIRST_LOGIN", true);
		store.setDefault("PRIMARY_SEMANTIC_PROVIDER", AEConstants.CLASSIFICATION_AUTHORITY);
		store.setDefault("PRIMARY_TAGGING_PROVIDER", AEConstants.TAGGING_LIST_PROVIDER);
		store.setDefault("PRIMARY_RELATION_PROVIDER", AEConstants.RELATION_CLASSIFICATION_PROVIDER);
		store.setDefault("ASPECT_VIEW_ID", AEConstants.ASPECT_VIEW_ID);
		store.setDefault("ASPECT_VIEW_REFERENCE", AEConstants.ASPECT_VIEW_REFERENCE);
		store.setDefault("ASPECT_VIEW_RELATIONS", AEConstants.ASPECT_VIEW_RELATIONS);

		store.setDefault("ASPECT_VIEW_MARKUPSORT_BYPRIORITY", AEConstants.ASPECT_VIEW_MARKUPSORT_BYPRIORITY);
		store.setDefault("ASPECT_VIEW_NOTIFICATION_TEMPLATE", AEConstants.ASPECT_VIEW_NOTIFICATION_TEMPLATE);

		store.setDefault("USER_SELECTED_LANG_OK", AEConstants.USER_LANGUAGE_OK);
		store.setDefault("USER_SELECTED_LANG", AEConstants.DEFAULT_LANGUAGE);
		// store.setDefault("IDENTIFIER_PND", AEConstants.PND);
		// store.setDefault("IDENTIFIER_PND_URL", AEConstants.PND_URL);
		//
		// store.setDefault("IDENTIFIER_LCCN", AEConstants.LCCN);
		// store.setDefault("IDENTIFIER_LCCN_URL", AEConstants.LCCN_URL);
		//
		// store.setDefault("IDENTIFIER_VIAF", AEConstants.VIAF);
		// store.setDefault("IDENTIFIER_VIAF_URL", AEConstants.VIAF_URL);
		//
		// store.setDefault("IDENTIFIER_ICCU", AEConstants.ICCU);
		// store.setDefault("IDENTIFIER_ICCU_URL", AEConstants.ICCU_URL);
		store.setDefault("RIGHTS_GENERAL", AEConstants.RIGHTS_GENERAL);
		store.setDefault("RIGHTS_WORKGROUP_READ", AEConstants.RIGHTS_WGR);
		store.setDefault("RIGHTS_WORKGROUP_WRITE", AEConstants.RIGHTS_WGW);
		store.setDefault("RIGHTS_PROJECTGROUP_READ", AEConstants.RIGHTS_PGR);
		store.setDefault("RIGHTS_PROJECTGROUP_WRITE", AEConstants.RIGHTS_PGW);
		store.setDefault("RIGHTS_ALL_READ", AEConstants.RIGHTS_AR);
		store.setDefault("RIGHTS_ALL_WRITE", false);

		store.setDefault("ASPECT_COLOR_PERSNAME", AEConstants.ASPECT_COLOR_PERSNAME);
		store.setDefault("ASPECT_COLOR_ORGNAME", AEConstants.ASPECT_COLOR_ORGNAME);
		store.setDefault("ASPECT_COLOR_PLACENAME", AEConstants.ASPECT_COLOR_PLACENAME);
		store.setDefault("ASPECT_COLOR_DATE", AEConstants.ASPECT_COLOR_DATE);
		store.setDefault("ASPECT_COLOR_NAME", AEConstants.ASPECT_COLOR_NAME);

		store.setDefault("REPOSITORY_NAME", AEConstants.REPOSITORY_NAME);
		store.setDefault("ASPECT_PRESELECTED_DATE_YEAR", AEConstants.ASPECT_PRESELECTED_DATE_YEAR);

		store.setDefault("REFERENCE_VIEW_AUTHOR_SURNAME", AEConstants.REFERENCE_VIEW_AUTHOR_SURNAME);
		store.setDefault("REFERENCE_VIEW_AUTHOR_FORENAME", AEConstants.REFERENCE_VIEW_AUTHOR_FORENAME);
		store.setDefault("REFERENCE_VIEW_OTHER_SURNAME", AEConstants.REFERENCE_VIEW_OTHER_SURNAME);
		store.setDefault("REFERENCE_VIEW_TITLE", AEConstants.REFERENCE_VIEW_TITLE);
		store.setDefault("REFERENCE_VIEW_TITLE_PARTNAME", AEConstants.REFERENCE_VIEW_TITLE_PARTNAME);
		store.setDefault("REFERENCE_VIEW_TITLE_PARTNUMBER", AEConstants.REFERENCE_VIEW_TITLE_PARTNUMBER);
		store.setDefault("REFERENCE_VIEW_SIGNATUR", AEConstants.REFERENCE_VIEW_SIGNATUR);
		store.setDefault("REFERENCE_VIEW_YEAR", AEConstants.REFERENCE_VIEW_YEAR);
		store.setDefault("REFERENCE_VIEW_LOCATION", AEConstants.REFERENCE_VIEW_LOCATION);
		store.setDefault("AUTOMATED_UPDATE", AEConstants.AUTOMATED_UPDATE);

		store.setDefault("REPOSITORY_ID", AEConstants.REPOSITORY_ID);
		store.setDefault("PROJECT_ID", AEConstants.PROJECT_ID);
		if (AEConstants.REPOSITORY_URL.trim().length() > 0)
		{
			store.setDefault("REPOSITORY_URL", AEConstants.REPOSITORY_URL);
		}

		store.setDefault("AE_ADVANCED_VERSION", AEConstants.AE_ADVANCED_VERSION);

		store.setDefault("MARKUP_EDITOR", AEConstants.MARKUP_EDITOR);

		store.setDefault("MARKUP_PRESENTATION", AEConstants.MARKUP_PRESENTATION);
		store.setDefault("REFERENCE_PRESENTATION", AEConstants.REFERENCE_PRESENTATION);

		store.setDefault("ASPECT_LITE_EDIT_ANA_KEY", AEConstants.ASPECT_LITE_EDIT_ANA_KEY);
	}


}
