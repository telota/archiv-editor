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
package org.bbaw.pdr.ae.repositoryconnection.view;

import org.bbaw.pdr.ae.common.AEConstants;
import org.bbaw.pdr.ae.common.CommonActivator;
import org.bbaw.pdr.ae.common.NLMessages;
import org.bbaw.pdr.ae.repositoryconnection.internal.Activator;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * @author cplutte Class creates login dialog for entering the repository
 *         settings.
 */
public class RepositoryLogin extends TitleAreaDialog
{

	/** entered username and password. */
	private Text _repositoryURL;
	// private Text repositoryPassword;
	/** The project id. */
	private int _projectID = 0;

	/** The repo instance id. */
	private int _repoInstanceID = 0;

	/** The parent shell. */
	private Shell _parentShell;
	/** Logger. */
	private static ILog iLogger = AEConstants.ILOGGER;

	/** The on startup. */
	private boolean _onStartup;

	/**
	 * Instantiates a new repository login.
	 * @param parentShell the parent shell
	 * @param onStartup the on startup
	 */
	public RepositoryLogin(final Shell parentShell, final boolean onStartup)
	{
		super(parentShell);
		this._onStartup = onStartup;
	}

	@Override
	public final void create()
	{
		super.create();

		// Set the title
		setTitle(NLMessages.getString("Dialog_repository_login_title"));
		// Set the message
		setMessage(NLMessages.getString("Dialog_repository_login_message"), IMessageProvider.INFORMATION);

	}

	@Override
	protected final void createButtonsForButtonBar(final Composite parent)
	{
		GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.horizontalSpan = 3;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalAlignment = SWT.CENTER;

		parent.setLayoutData(gridData);
		// Create Add button
		// Own method as we need to overview the SelectionAdapter
		createOkButton(parent, OK, NLMessages.getString("Dialog_save"), true); //$NON-NLS-1$
		// Add a SelectionListener

		// Create Cancel button
		Button cancelButton = createButton(parent, CANCEL, NLMessages.getString("Dialog_cancel"), false); //$NON-NLS-1$
		// Add a SelectionListener
		cancelButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent e)
			{
				setReturnCode(CANCEL);
				close();
			}
		});
	}
	

	@Override
	protected final Control createDialogArea(final Composite parent)
	{
		Composite mainComposite = new Composite(parent, SWT.NONE);
		mainComposite.setLayoutData(new GridData());
		((GridData) mainComposite.getLayoutData()).verticalAlignment = SWT.FILL;
		((GridData) mainComposite.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) mainComposite.getLayoutData()).minimumHeight = 90;
		((GridData) mainComposite.getLayoutData()).minimumWidth = 630;
		((GridData) mainComposite.getLayoutData()).grabExcessHorizontalSpace = true;

		mainComposite.setLayout(new GridLayout());
		((GridLayout) mainComposite.getLayout()).makeColumnsEqualWidth = true;
		((GridLayout) mainComposite.getLayout()).numColumns = 2;

		Label repoUrl = new Label(mainComposite, SWT.NONE);
		repoUrl.setText(NLMessages.getString("Dialog_repository_url") + "*");
		repoUrl.setLayoutData(new GridData());

		_repositoryURL = new Text(mainComposite, SWT.BORDER);
		_repositoryURL.setLayoutData(new GridData());
		_repositoryURL.setText(Platform.getPreferencesService().getString(CommonActivator.PLUGIN_ID,
				"REPOSITORY_URL", AEConstants.REPOSITORY_URL, null)); //$NON-NLS-1$
		((GridData) _repositoryURL.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _repositoryURL.getLayoutData()).grabExcessHorizontalSpace = true;

		// Label repoPassword = new Label(mainComposite, SWT.NONE);
		// repoPassword.setText(NLMessages.getString("Dialog_repository_password")
		// + "*");
		// repoPassword.setLayoutData(new GridData());

		// repositoryPassword = new Text(mainComposite, SWT.BORDER |
		// SWT.PASSWORD);
		// repositoryPassword.setLayoutData(new GridData());
		//		repositoryPassword.setText(""); //$NON-NLS-1$
		// ((GridData) repositoryPassword.getLayoutData()).horizontalAlignment =
		// SWT.FILL;
		// ((GridData)
		// repositoryPassword.getLayoutData()).grabExcessHorizontalSpace = true;
		//
		Label repoInstance = new Label(mainComposite, SWT.NONE);
		repoInstance.setText(NLMessages.getString("Dialog_repository_id"));
		repoInstance.setLayoutData(new GridData());

		final Text repoInstanceText = new Text(mainComposite, SWT.BORDER);
		repoInstanceText.setLayoutData(new GridData());
		_repoInstanceID = Platform.getPreferencesService().getInt(CommonActivator.PLUGIN_ID, "REPOSITORY_ID",
				AEConstants.REPOSITORY_ID, null);
		repoInstanceText.setText("" + _repoInstanceID); //$NON-NLS-1$
		((GridData) repoInstanceText.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) repoInstanceText.getLayoutData()).grabExcessHorizontalSpace = true;
		repoInstanceText.addFocusListener(new FocusListener()
		{

			@Override
			public void focusGained(final FocusEvent e)
			{
			}

			@Override
			public void focusLost(final FocusEvent e)
			{
				try
				{
					if (repoInstanceText.getText().length() > 0)
					{
						_repoInstanceID = Integer.parseInt(repoInstanceText.getText());
					}
					else
					{
						_repoInstanceID = 0;
					}
				}
				catch (NumberFormatException ex)
				{
					_repoInstanceID = 0;
					String message = NLMessages.getString("View_pleaseEnterNumber"); //$NON-NLS-1$
					MessageDialog.openInformation(_parentShell, NLMessages.getString("View_error"), message); //$NON-NLS-1$
				}
			}
		});

		Label projectIDLabel = new Label(mainComposite, SWT.NONE);
		projectIDLabel.setText(NLMessages.getString("Dialog_project_id"));
		projectIDLabel.setLayoutData(new GridData());

		final Text projectIDText = new Text(mainComposite, SWT.BORDER);
		projectIDText.setLayoutData(new GridData());
		_projectID = Platform.getPreferencesService().getInt(CommonActivator.PLUGIN_ID, "PROJECT_ID",
				AEConstants.PROJECT_ID, null);
		projectIDText.setText("" + _projectID); //$NON-NLS-1$
		((GridData) projectIDText.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) projectIDText.getLayoutData()).grabExcessHorizontalSpace = true;

		projectIDText.addFocusListener(new FocusListener()
		{

			@Override
			public void focusGained(final FocusEvent e)
			{
			}

			@Override
			public void focusLost(final FocusEvent e)
			{
				try
				{
					if (projectIDText.getText().length() > 0)
					{
						_projectID = Integer.parseInt(projectIDText.getText());
					}
					else
					{
						_projectID = 0;
					}
				}
				catch (NumberFormatException ex)
				{
					_projectID = 0;
					String message = NLMessages.getString("View_pleaseEnterNumber"); //$NON-NLS-1$
					MessageDialog.openInformation(_parentShell, NLMessages.getString("View_error"), message); //$NON-NLS-1$
				}
			}
		});

		Button restoreButton = new Button(mainComposite, SWT.PUSH);
		restoreButton.setLayoutData(new GridData());
		restoreButton.setText(NLMessages.getString("Dialog_restore_default_values")); //$NON-NLS-1$
		restoreButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent e)
			{
				CommonActivator.getDefault().getPreferenceStore()
						.putValue("REPOSITORY_ID", new Integer(AEConstants.REPOSITORY_ID).toString());
				CommonActivator.getDefault().getPreferenceStore()
						.putValue("PROJECT_ID", new Integer(AEConstants.PROJECT_ID).toString());
				if (AEConstants.REPOSITORY_URL.trim().length() > 0)
				{
					CommonActivator.getDefault().getPreferenceStore()
							.putValue("REPOSITORY_URL", AEConstants.REPOSITORY_URL);
				}
				_repositoryURL.setText(AEConstants.REPOSITORY_URL); //$NON-NLS-1$ //$NON-NLS-2$
				_repoInstanceID = AEConstants.REPOSITORY_ID;
				repoInstanceText.setText("" + new Integer(AEConstants.REPOSITORY_ID)); //$NON-NLS-1$ //$NON-NLS-2$
				_projectID = AEConstants.PROJECT_ID;
				projectIDText.setText("" + AEConstants.PROJECT_ID); //$NON-NLS-1$ //$NON-NLS-2$

			}
		});
		parent.pack();

		return parent;
	}

	/**
	 * creates OKButton.
	 * @param parent parent composite
	 * @param id id
	 * @param label label of button
	 * @param defaultButton is default
	 * @return button button
	 */
	protected final Button createOkButton(final Composite parent, final int id, final String label,
			final boolean defaultButton)
	{
		// increment the number of columns in the button bar
		((GridLayout) parent.getLayout()).numColumns++;
		Button button = new Button(parent, SWT.PUSH);
		button.setText(label);
		button.setFont(JFaceResources.getDialogFont());
		button.setData(new Integer(id));
		button.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent event)
			{
				if (isValidInput())
				{
					okPressed();
					saveInput();
					close();

				}
			}
		});
		if (defaultButton)
		{
			Shell shell = parent.getShell();
			if (shell != null)
			{
				shell.setDefaultButton(button);
			}
		}
		setButtonLayoutData(button);
		return button;
	}

	// We do not allow the user to resize this dialog
	/**
	 * @return false
	 * @see org.eclipse.jface.dialogs.Dialog#isResizable()
	 */
	@Override
	protected final boolean isResizable()
	{
		return false;
	}

	/**
	 * checks if input is valid.
	 * @return is valid
	 */
	private boolean isValidInput()
	{
		if (_onStartup)
		{
		}
		if (_repositoryURL.getText().trim().length() > 0
		// && repositoryPassword.getText().trim().length() > 0
				)
		{
			// XXX url validator method outsourcen!!!
			// try {
			// URL url = new URL(repositoryURL.getText().trim());
			// Utilities.setAxis2Base(url);
			// try {
			// System.out.println(Utilities.getAxis2VersionResponse());
			// System.out.println(Utilities.getPDRAlliesVersionResponse());
			// System.out.println(Utilities.getPDRAlliesEchoResponse("hallo welt"));
			// return true;
			//
			// } catch (PDRAlliesClientException e) {
			// String info = NLMessages.getString("Dialog_no_valid_url") +
			// "\nURL: "
			// + repositoryURL.getText().trim();
			// MessageDialog infoDialog = new MessageDialog(parentShell,
			// NLMessages.getString("Dialog_no_valid_url"), null,
			// info, MessageDialog.ERROR,
			//					        new String[] { "OK" }, 0); //$NON-NLS-1$
			// infoDialog.open();
			// return false;
			// }
			// } catch (MalformedURLException e) {
			// String info = NLMessages.getString("Dialog_no_valid_url") +
			// "\nURL: "
			// + repositoryURL.getText().trim();
			// MessageDialog infoDialog = new MessageDialog(parentShell,
			// NLMessages.getString("Dialog_no_valid_url"), null,
			// info, MessageDialog.ERROR,
			//				        new String[] { "OK" }, 0); //$NON-NLS-1$
			// infoDialog.open();
			// return false;
			// }
		}
		else
		{
			return false;
		}
		return true;
	}

	/**
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	@Override
	protected final void okPressed()
	{
		saveInput();
		// super.okPressed();
	}

	/**
	 * if user name and password are correct the identified current user is
	 * saved as currentUser in facade.
	 */
	private void saveInput()
	{
		IStatus sname = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Repository Connection set to: "
				+ _repositoryURL.getText().trim() + " repoInstanceID " + _repoInstanceID + " projectID " + _projectID); //$NON-NLS-1$
		iLogger.log(sname);
		// http://pdrdev.bbaw.de:8080/axis2

		// Platform.getPreferencesService().(CommonActivator.PLUGIN_ID,
		// "REPOSITORY_URL", AEConstants.REPOSITORY_URL, null)
		CommonActivator.getDefault().getPreferenceStore().setValue("REPOSITORY_URL", _repositoryURL.getText().trim()); //$NON-NLS-1$
		//		Activator.getDefault().getPreferenceStore().setValue("REPOSITORY_PASSWORD", repositoryPassword.getText().trim()); //$NON-NLS-1$
		CommonActivator.getDefault().getPreferenceStore().setValue("PROJECT_ID", _projectID);
		CommonActivator.getDefault().getPreferenceStore().setValue("REPOSITORY_ID", _repoInstanceID);
		// System.out.println("url set to " +
		// Activator.getDefault().getPreferenceStore().getString("REPOSITORY_URL"));
	}
}
