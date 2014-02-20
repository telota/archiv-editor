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
package org.bbaw.pdr.ae.rap2.internal;

import org.bbaw.pdr.ae.common.NLMessages;
import org.bbaw.pdr.ae.model.User;
import org.bbaw.pdr.allies.client.error.PDRAlliesClientException;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * Class creates login dialog for entering the user name. 
 * @author Christoph Plutte
 */
public class LoginDialogRAP extends TitleAreaDialog
{
	
	/** user name and password. */
	private String _userName;

	/** The _user. */
	private User _user;

	/** The _user password. */
	private String _userPassword;
	/** entered username and password. */
	private Combo _userNameCombo;

	/** The _user password text. */
	private Text _userPasswordText;

	
	private Integer _projectID;

	private Combo _projectIDCombo;

	private RAPUserManager _userManager = new RAPUserManager();

	private String _userID;

	/**
	 * Instantiates a new login dialog.
	 * @param parentShell the parent shell
	 * @param onStart the on start
	 */
	public LoginDialogRAP(final Shell parentShell)
	{
		super(parentShell);
			}

	
	@Override
	public final void create()
	{
		super.create();

		// Set the title
		setTitle("Login"); //$NON-NLS-1$
		// Set the message
		setMessage("Please login", IMessageProvider.INFORMATION); //$NON-NLS-1$

	}

	@Override
	public final void createButtonsForButtonBar(final Composite parent)
	{
		GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.horizontalSpan = 2;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalAlignment = SWT.CENTER;

		parent.setLayoutData(gridData);
		// Create Add button
		
		// Own method as we need to overview the SelectionAdapter
		createOkButton(parent, OK, "OK", true); //$NON-NLS-1$
		// Add a SelectionListener

		// Create Cancel button
		Button cancelButton = createButton(parent, CANCEL,"Cancel", false); //$NON-NLS-1$
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
	public final Control createDialogArea(final Composite parent)
	{

		

		Composite mainComposite = new Composite(parent, SWT.NONE);

		mainComposite.setLayoutData(new GridData());
		((GridData) mainComposite.getLayoutData()).verticalAlignment = SWT.FILL;
		((GridData) mainComposite.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) mainComposite.getLayoutData()).minimumHeight = 90;
		((GridData) mainComposite.getLayoutData()).grabExcessHorizontalSpace = true;

		mainComposite.setLayout(new GridLayout());
		((GridLayout) mainComposite.getLayout()).makeColumnsEqualWidth = true;
		((GridLayout) mainComposite.getLayout()).numColumns = 2;

		
		Label projectLabel = new Label(mainComposite, SWT.NONE);
		projectLabel.setText("Select Project ID"); //$NON-NLS-1$
		projectLabel.setLayoutData(new GridData());

		_projectIDCombo = new Combo(mainComposite, SWT.BORDER | SWT.READ_ONLY);
		_projectIDCombo.setLayoutData(new GridData());
		((GridData) _projectIDCombo.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _projectIDCombo.getLayoutData()).grabExcessHorizontalSpace = true;
		_projectIDCombo.setItems(AERAPConstants.PROJECT_IDS);
		_projectIDCombo.addListener(SWT.Modify, new Listener()
		{
			

			@Override
			public void handleEvent(final Event event)
			{
				 _projectID = new Integer(_projectIDCombo.getItem(_projectIDCombo.getSelectionIndex())); //$NON-NLS-1$
				 _userManager.setProjectID(_projectID);
			}
		});
		
		Label userNameLabel = new Label(mainComposite, SWT.NONE);
		userNameLabel.setText("User Name"); //$NON-NLS-1$
		userNameLabel.setLayoutData(new GridData());

		_userNameCombo = new Combo(mainComposite, SWT.BORDER);
		_userNameCombo.setLayoutData(new GridData());
		((GridData) _userNameCombo.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _userNameCombo.getLayoutData()).grabExcessHorizontalSpace = true;

		Label userPasswordLabel = new Label(mainComposite, SWT.NONE);
		userPasswordLabel.setText("Password"); //$NON-NLS-1$
		userPasswordLabel.setLayoutData(new GridData());

		_userPasswordText = new Text(mainComposite, SWT.BORDER | SWT.PASSWORD);
		_userPasswordText.setLayoutData(new GridData());
		((GridData) _userPasswordText.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _userPasswordText.getLayoutData()).grabExcessHorizontalSpace = true;

		_userNameCombo.addListener(SWT.Modify, new Listener()
		{
			@Override
			public void handleEvent(final Event event)
			{
				
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
	 * @return okButton
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
					setReturnCode(OK);
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

	/**
	 * Gets the user.
	 * @return the user
	 */
	public final User getUser()
	{
		return _user;
	}

	

	@Override
	protected final boolean isResizable()
	{
		return false;
	}

	/**
	 * checks if input is valid.
	 * @return boolean valid 
	 */
	private boolean isValidInput()
	{
		boolean valid = true;
		_userName = _userNameCombo.getText();
		_userPassword = _userPasswordText.getText();

		if (_userName.length() == 0)
		{
			setErrorMessage(NLMessages.getString("LoginDialog_errorMessageNoUserName")); //$NON-NLS-1$
			valid = false;
			return valid;
		}
		if (_userPassword.length() == 0)
		{
			setErrorMessage(NLMessages.getString("LoginDialog_errorMessageNoPassword")); //$NON-NLS-1$
			valid = false;
			return valid;

		}
		else 
		{
			User u = null;
			try {
				_userID = _userManager.getUserId(_userName, _projectID);
			} catch (PDRAlliesClientException e1) {
				e1.printStackTrace();
			}

			if (u == null && _userID != null)
			{
				try
				{
					u = _userManager.getUsersByUserName(_userName, _userID, _userPassword);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				
			}
			if (u == null)
			{
				setErrorMessage(NLMessages.getString("LoginDialog_errorMessageUserNameUnknown")); //$NON-NLS-1$
				return false;
			}
			if (u.getAuthentication() != null && _userName.equals(u.getAuthentication().getUserName()))
			{
				if (_userPassword.equals(u.getAuthentication().getPassword()))
				{
					valid = true;
					_user = u;
					return valid;
				}
				else
				{
					setErrorMessage(NLMessages.getString("LoginDialog_errorMessagePasswordInvalid")); //$NON-NLS-1$
					valid = false;
					return valid;
				}
			}
			else
			{
				setErrorMessage(NLMessages.getString("LoginDialog_errorMessageUserNameUnknown")); //$NON-NLS-1$
				valid = false;
			}
		}
		return valid;
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
		
		
	}

}
