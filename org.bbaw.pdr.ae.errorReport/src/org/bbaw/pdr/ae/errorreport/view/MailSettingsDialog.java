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
package org.bbaw.pdr.ae.errorreport.view;

import org.bbaw.pdr.ae.errorreport.Activator;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
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
 * @author cplutte Class creates login dialog for entering the user name. TODO
 *         it has to be adapted to dynamic data TODO vielleicht ist es besser,
 *         wenn der dialog nicht schon beim Start öffnet, sondern erst nachdem
 *         das Programm geladen ist und wenn der Benutzer eine erste Aktion
 *         ausführen möchte.
 */
public class MailSettingsDialog extends TitleAreaDialog
{
	/** singleton instace of facade. */
	// private Facade facade = Facade.getInstanz();

	private Text _smtpHostName;

	/** The user name. */
	private Text _userName;

	/** The user password. */
	private Text _userPassword;

	/** The user adress. */
	private Text _userAdress;

	/** Logger. */
	// private static ILog iLogger =
	// org.bbaw.pdr.ae.view.Activator.getILogger();

	// public static String getuserName() {
	// return userName;
	// }
	//
	// public void setUserName(String userName) {
	// this.userName = userName;
	// }

	public MailSettingsDialog(final Shell parentShell)
	{
		super(parentShell);
	}

	@Override
	public final void create()
	{
		super.create();

		// Set the title
		setTitle(Messages.getString("ErrorDialog_mail_dialog_title"));
		// Set the message
		setMessage(Messages.getString("ErrorDialog_mail_dialog_message"), IMessageProvider.INFORMATION);

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
		createOkButton(parent, OK, Messages.getString("ErrorDialog_save"), true);
		// Add a SelectionListener

		// Create Cancel button
		Button cancelButton = createButton(parent, CANCEL, Messages.getString("ErrorDialog_cancel"), false);
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
		((GridData) mainComposite.getLayoutData()).grabExcessHorizontalSpace = true;

		mainComposite.setLayout(new GridLayout());
		((GridLayout) mainComposite.getLayout()).makeColumnsEqualWidth = false;
		((GridLayout) mainComposite.getLayout()).numColumns = 2;

		Label titleLable = new Label(mainComposite, SWT.NONE);
		titleLable.setText(Messages.getString("ErrorDialog_mail_dialog_subtitle"));
		titleLable.setLayoutData(new GridData());
		((GridData) titleLable.getLayoutData()).horizontalSpan = 2;

		Label smtpHostLabel = new Label(mainComposite, SWT.NONE);
		smtpHostLabel.setText(Messages.getString("ErrorDialog_smtp_host"));
		smtpHostLabel.setLayoutData(new GridData());

		_smtpHostName = new Text(mainComposite, SWT.BORDER);
		_smtpHostName.setLayoutData(new GridData());
		((GridData) _smtpHostName.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _smtpHostName.getLayoutData()).grabExcessHorizontalSpace = true;
		_smtpHostName.setText(Activator.getDefault().getPreferenceStore().getString("MAIL_SMTP_HOST_NAME")); //$NON-NLS-1$

		Label userAdressLabel = new Label(mainComposite, SWT.NONE);
		userAdressLabel.setText(Messages.getString("ErrorDialog_sender_email"));
		userAdressLabel.setLayoutData(new GridData());

		_userAdress = new Text(mainComposite, SWT.BORDER);
		_userAdress.setLayoutData(new GridData());
		((GridData) _userAdress.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _userAdress.getLayoutData()).grabExcessHorizontalSpace = true;
		_userAdress.setText(Activator.getDefault().getPreferenceStore().getString("MAIL_ADRESS_SENDER")); //$NON-NLS-1$

		Label userNameLabel = new Label(mainComposite, SWT.NONE);
		userNameLabel.setText(Messages.getString("ErrorDialog_user_name"));
		userNameLabel.setLayoutData(new GridData());

		_userName = new Text(mainComposite, SWT.BORDER);
		_userName.setLayoutData(new GridData());
		((GridData) _userName.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _userName.getLayoutData()).grabExcessHorizontalSpace = true;
		_userName.setText(Activator.getDefault().getPreferenceStore().getString("MAIL_SMTP_AUTH_USER")); //$NON-NLS-1$

		Label userPasswordLabel = new Label(mainComposite, SWT.NONE);
		userPasswordLabel.setText(Messages.getString("ErrorDialog_password_not_saved"));
		userPasswordLabel.setLayoutData(new GridData());

		_userPassword = new Text(mainComposite, SWT.BORDER | SWT.PASSWORD);
		_userPassword.setLayoutData(new GridData());
		((GridData) _userPassword.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _userPassword.getLayoutData()).grabExcessHorizontalSpace = true;
		_userPassword.setText(""); //$NON-NLS-1$

		parent.pack();

		return parent;
	}

	/**
	 * creates OKButton.
	 * @param parent parent composite
	 * @param id button id
	 * @param label button label
	 * @param defaultButton is default
	 * @return button
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

	@Override
	protected final boolean isResizable()
	{
		return false;
	}

	/**
	 * checks if input is valid.
	 * @return boolean valid TODO die Überprüfung des Benutzernamens und
	 *         Passwortes soll nicht hier, sondern in der Controller-Schicht
	 *         oder sogar in der DAtenhaltung aus geführt werden. TODO
	 *         User-datenbank einbauen.
	 */
	private boolean isValidInput()
	{

		return true;
	}

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
		Activator.getDefault().getPreferenceStore().setValue("MAIL_ADRESS_SENDER", _userAdress.getText()); //$NON-NLS-1$
		Activator.getDefault().getPreferenceStore().setValue("MAIL_SMTP_HOST_NAME", _smtpHostName.getText()); //$NON-NLS-1$
		Activator.getDefault().getPreferenceStore().setValue("MAIL_SMTP_AUTH_USER", _userName.getText()); //$NON-NLS-1$
		Activator.getDefault().getPreferenceStore().setValue("MAIL_SMTP_AUTH_PWD", _userPassword.getText()); //$NON-NLS-1$
	}
}
