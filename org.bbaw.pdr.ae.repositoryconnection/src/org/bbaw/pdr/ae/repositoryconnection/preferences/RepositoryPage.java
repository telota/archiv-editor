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
package org.bbaw.pdr.ae.repositoryconnection.preferences;

import org.bbaw.pdr.ae.common.AEConstants;
import org.bbaw.pdr.ae.common.CommonActivator;
import org.bbaw.pdr.ae.common.NLMessages;
import org.bbaw.pdr.ae.control.facade.Facade;
import org.bbaw.pdr.ae.control.interfaces.IPdrIdService;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * The Class RepositoryPage.
 * @author Christoph Plutte
 */
public class RepositoryPage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage
{
	/** The repository url text. */
	private Text _repositoryURLText;

	/** The repo id. */
	private Text _repoID;

	/** The project id. */
	private Text _projectID;

	/** The id service. */
	private IPdrIdService _idService = Facade.getInstanz().getIdService();

	/**
	 * Instantiates a new repository page.
	 */
	public RepositoryPage()
	{
		super(GRID);

	}

	@Override
	public final void createFieldEditors()
	{

		addField(new StringFieldEditor("REPOSITORY_NAME", NLMessages.getString("Preference_name_of_repository"), //$NON-NLS-1$
				getFieldEditorParent()));

		Label repositoryURL = new Label(getFieldEditorParent(), SWT.NONE);
		repositoryURL.setText(NLMessages.getString("Preference_repository_url"));
		_repositoryURLText = new Text(getFieldEditorParent(), SWT.BORDER | SWT.NO_BACKGROUND | SWT.READ_ONLY
				| SWT.LEFT_TO_RIGHT);
		_repositoryURLText.setText(CommonActivator.getDefault().getPreferenceStore().getString("REPOSITORY_URL")); //$NON-NLS-1$ //$NON-NLS-2$

		Label repo = new Label(getFieldEditorParent(), SWT.NONE);
		repo.setText(NLMessages.getString("Preference_instance_id"));
		_repoID = new Text(getFieldEditorParent(), SWT.BORDER | SWT.NO_BACKGROUND | SWT.READ_ONLY);
		_repoID.setText("" + CommonActivator.getDefault().getPreferenceStore().getInt("REPOSITORY_ID")); //$NON-NLS-1$ //$NON-NLS-2$

		Label project = new Label(getFieldEditorParent(), SWT.NONE);
		project.setText(NLMessages.getString("Preference_project_id"));
		_projectID = new Text(getFieldEditorParent(), SWT.BORDER | SWT.NO_BACKGROUND | SWT.READ_ONLY);
		_projectID.setText("" + CommonActivator.getDefault().getPreferenceStore().getInt("PROJECT_ID")); //$NON-NLS-1$ //$NON-NLS-2$

		Label lastUpdate = new Label(getFieldEditorParent(), SWT.NONE);
		lastUpdate.setText(NLMessages.getString("Preference_last_update"));
		Text lastUpdateT = new Text(getFieldEditorParent(), SWT.BORDER | SWT.NO_BACKGROUND | SWT.READ_ONLY);
		try
		{
			lastUpdateT.setText(AEConstants.ADMINDATE_FORMAT.format(_idService.getUpdateTimeStamp()));
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} //$NON-NLS-1$ //$NON-NLS-2$

	}

	@Override
	public final void init(final IWorkbench workbench)
	{
		setPreferenceStore(CommonActivator.getDefault().getPreferenceStore());
		setDescription("Repository Connection Settings");
	}

	@Override
	public final boolean performOk()
	{
		super.performOk();

		return true;
	}

}
