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
package org.bbaw.pdr.ae.standalone.internal;

import org.bbaw.pdr.ae.common.AEConstants;
import org.bbaw.pdr.ae.common.CommonActivator;
import org.bbaw.pdr.ae.common.NLMessages;
import org.bbaw.pdr.ae.common.utils.OpenExternalBrowser;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class InstallationDialog extends TitleAreaDialog
{
	private Text text;
	private Button _btnUnselectThisTo;
	private Button _btnAdvancedVersion;

	private boolean _isAdvanced = false;

	private boolean _standardDir = true;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public InstallationDialog(Shell parentShell)
	{
		super(parentShell);
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent)
	{
		setTitle(NLMessages.getString("InstallationDialog_title"));

		setMessage(NLMessages.getString("InstallationDialog_message"));

		// Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayoutData(new GridData(GridData.FILL_BOTH));



		Label lblChooseArchiveditorVersion = new Label(container, SWT.NONE);
		lblChooseArchiveditorVersion.setBounds(10, 10, 424, 13);
		lblChooseArchiveditorVersion.setText(NLMessages.getString("InstallationDialog_choose_version"));

		_btnAdvancedVersion = new Button(container, SWT.CHECK);
		_btnAdvancedVersion.setBounds(10, 28, 424, 16);
		_btnAdvancedVersion.setText(NLMessages.getString("InstallationDialog_advanced_version"));
		_btnAdvancedVersion.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent event)
			{
				_isAdvanced = !_isAdvanced;
			}
		}); // SelectionListener

		_btnAdvancedVersion.setSelection(AEConstants.AE_ADVANCED_VERSION);
		_isAdvanced = AEConstants.AE_ADVANCED_VERSION;
		CommonActivator.getDefault().getPreferenceStore()
		.setValue("AE_ADVANCED_VERSION", AEConstants.AE_ADVANCED_VERSION); //$NON-NLS-1$
		
		Label lblDatabaseLocation = new Label(container, SWT.NONE);
		lblDatabaseLocation.setBounds(10, 66, 424, 13);
		lblDatabaseLocation.setText(NLMessages.getString("InstallationDialog_installation_dir"));
		
		Label lblStandardLocation = new Label(container, SWT.NONE);
		lblStandardLocation.setBounds(10, 88, 105, 13);
		lblStandardLocation.setText(NLMessages.getString("InstallationDialog_standard_dir"));
		
		text = new Text(container, SWT.BORDER);
		text.setBounds(121, 85, 313, 19);
		text.setEditable(false);
		text.setText(AEConstants.AE_HOME);
		
		_btnUnselectThisTo = new Button(container, SWT.CHECK);
		_btnUnselectThisTo.setBounds(10, 105, 424, 16);
		_btnUnselectThisTo.setText(NLMessages.getString("InstallationDialog_select_standard"));
		_btnUnselectThisTo.setSelection(AEConstants.SAVE_DB_IN_INSTALLATION_DIR);
		_standardDir = AEConstants.SAVE_DB_IN_INSTALLATION_DIR;
		CommonActivator.getDefault().getPreferenceStore().setValue("SAVE_DB_IN_INSTALLATION_DIR", AEConstants.SAVE_DB_IN_INSTALLATION_DIR); //$NON-NLS-1$
		_btnUnselectThisTo.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent event)
			{
				_standardDir = !_standardDir;
			}
		}); // SelectionListener


		Label lblPleaseSubscribteTo = new Label(container, SWT.NONE);
		lblPleaseSubscribteTo.setBounds(10, 174, 424, 13);
		lblPleaseSubscribteTo.setText(NLMessages.getString("InstallationDialog_subscribe_mainlinglist"));

		Link link = new Link(container, SWT.NONE);
		link.setBounds(10, 193, 353, 13);
		link.setText("<a>Archiv-Editor-Mainlingslist</a>"); //$NON-NLS-1$
		
		Label lblNewLabel = new Label(container, SWT.NONE);
		lblNewLabel.setBounds(10, 127, 424, 13);
		lblNewLabel.setText(NLMessages.getString("InstallationDialog_installation_dir_explanation"));

		
		
		link.addSelectionListener(new SelectionListener()
		{

			@Override
			public void widgetDefaultSelected(SelectionEvent e)
			{

			}

			@Override
			public void widgetSelected(SelectionEvent e)
			{
				OpenExternalBrowser.openURL("https://mailman.bbaw.de/mailman/listinfo/pdr-l"); //$NON-NLS-1$

			}
		});

		return parent;
	}


	/**
	 * Create contents ofblNewLabel = new Label(container, SWT.NONE);
	 * lblNewLabel.setBounds(10, 10, 49, 13); lblNewLttonsForButtonBar(Composite
	 * parent) { createButton(parent, IDialogConstants.OK_ID,
	 * IDialogConstants.OK_LABEL, true); createButton(parent,
	 * IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false); } /**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize()
	{
		return new Point(450, 360);
	}
	
	@Override
	protected void okPressed()
	{

		super.okPressed();
	}

	@Override
	public final void createButtonsForButtonBar(final Composite parent)
	{
		GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.horizontalSpan = 3;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalAlignment = SWT.CENTER;

		parent.setLayoutData(gridData);
		// Create Add button

		// Create Cancel button
		Button okButton = createButton(parent, OK, NLMessages.getString("Dialog_save"), true); //$NON-NLS-1$
		// Add a SelectionListener
		okButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent e)
			{
				CommonActivator.getDefault().getPreferenceStore()
.setValue("SAVE_DB_IN_INSTALLATION_DIR", _standardDir); //$NON-NLS-1$
				CommonActivator.getDefault().getPreferenceStore()
.setValue("AE_ADVANCED_VERSION", _isAdvanced); //$NON-NLS-1$


				okPressed();
				if (_standardDir)
				{
					setReturnCode(OK);
				}
				else
				{
					setReturnCode(CANCEL);
				}

				close();
			}
		});
	}


}
