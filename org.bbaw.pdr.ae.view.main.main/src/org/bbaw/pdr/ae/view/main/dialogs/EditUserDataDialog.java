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
package org.bbaw.pdr.ae.view.main.dialogs;

import java.util.Vector;

import javax.xml.stream.XMLStreamException;

import org.bbaw.pdr.ae.common.NLMessages;
import org.bbaw.pdr.ae.control.facade.Facade;
import org.bbaw.pdr.ae.control.interfaces.IUserManager;
import org.bbaw.pdr.ae.model.User;
import org.bbaw.pdr.ae.model.UserContact;
import org.bbaw.pdr.ae.model.UserInformation;
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
 * Class creates login dialog for entering the user name. TODO it has to be
 * adapted to dynamic data TODO vielleicht ist es besser, wenn der dialog nicht
 * schon beim Start öffnet, sondern erst nachdem das Programm geladen ist und
 * wenn der Benutzer eine erste Aktion ausführen möchte.
 * @author Christoph Plutte
 */
public class EditUserDataDialog extends TitleAreaDialog
{

	/** singleton instace of facade. */
	private Facade _facade = Facade.getInstanz();
	/** user name and password. */
	private User _user;
	/** entered username and password . */
	private Text _userNameText;

	/** The user password text. */
	private Text _userPasswordText;

	/** The main composite. */
	private Composite _mainComposite;

	/** The data composite. */
	private Composite _dataComposite;

	/** The contact composite. */
	private Composite _contactComposite = null;

	/** The parent. */
	private Composite _parent;

	/** The user manager. */
	private IUserManager _userManager = _facade.getUserManager();

	/** The CONTAC t_ types. */
	private static final String[] CONTACT_TYPES = new String[]
	{NLMessages.getString("Dialog_phone"), NLMessages.getString("Dialog_email"), NLMessages.getString("Dialog_mobile")};

	// /** Logger */
	// private static ILog iLogger =
	// org.bbaw.pdr.ae.view.main.Activator.getILogger();

	/**
	 * Instantiates a new edits the user data dialog.
	 * @param parentShell the parent shell
	 */
	public EditUserDataDialog(final Shell parentShell)
	{
		super(parentShell);
	}

	@Override
	public final void create()
	{
		super.create();

		// Set the title
		setTitle(NLMessages.getString("Dialog_edit_userData_dialog_title"));
		// Set the message
		dialogArea.addListener(SWT.Traverse, new Listener()
		{
			@Override
			public void handleEvent(final Event e)
			{
				if (e.detail == SWT.TRAVERSE_ESCAPE)
				{
					e.doit = false;
				}
			}
		});

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
	protected final Control createDialogArea(final Composite p)
	{
		this._parent = p;
		_mainComposite = new Composite(_parent, SWT.NONE);

		_mainComposite.setLayoutData(new GridData());
		((GridData) _mainComposite.getLayoutData()).verticalAlignment = SWT.FILL;
		((GridData) _mainComposite.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _mainComposite.getLayoutData()).minimumHeight = 600;
		((GridData) _mainComposite.getLayoutData()).grabExcessHorizontalSpace = true;

		_mainComposite.setLayout(new GridLayout());
		((GridLayout) _mainComposite.getLayout()).numColumns = 1;

		loadValues();

		_parent.pack();

		return _parent;
	}

	/**
	 * create OKbutten.
	 * @param parent parent composite
	 * @param id id
	 * @param label label of button
	 * @param defaultButton is default.
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
	 * @return boolean valid TODO die Überprüfung des Benutzernamens und
	 *         Passwortes soll nicht hier, sondern in der Controller-Schicht
	 *         oder sogar in der DAtenhaltung aus geführt werden. TODO
	 *         User-datenbank einbauen.
	 */
	private boolean isValidInput()
	{
		if (_user.getAuthentication() == null || _user.getAuthentication().getUserName() == null
				|| _user.getAuthentication().getUserName().trim().length() == 0)
		{
			setMessage(NLMessages.getString("Dialog_message_username_requiered"), SWT.ERROR);
			return false;
		}
		else if (_user.getAuthentication().getPassword() == null
				|| _user.getAuthentication().getPassword().trim().length() == 0)
		{
			setMessage(NLMessages.getString("Dialog_message_password_requiered"), SWT.ERROR);
			return false;
		}
		else if (_user.getAuthentication().getUserName().contains(" ")) //$NON-NLS-1$
		{
			setMessage(NLMessages.getString("Dialog_message_username_no_whitespace"), SWT.ERROR);
			return false;
		}
		else if (_user.getAuthentication().getPassword().contains(" ")) //$NON-NLS-1$
		{
			setMessage(NLMessages.getString("Dialog_message_password_no_whitespace"), SWT.ERROR);
			return false;
		}
		else
		{
			return true;
		}
	}

	/**
	 * Load values.
	 */
	private void loadValues()
	{
		if (_contactComposite != null)
		{
			_contactComposite.dispose();
		}
		if (_dataComposite != null)
		{
			_dataComposite.dispose();
		}
		_dataComposite = new Composite(_mainComposite, SWT.NONE);
		_dataComposite.setLayoutData(new GridData());
		((GridData) _dataComposite.getLayoutData()).verticalAlignment = SWT.FILL;
		((GridData) _dataComposite.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _dataComposite.getLayoutData()).minimumHeight = 90;
		((GridData) _dataComposite.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) _dataComposite.getLayoutData()).horizontalSpan = 2;

		_dataComposite.setLayout(new GridLayout());
		((GridLayout) _dataComposite.getLayout()).makeColumnsEqualWidth = false;
		((GridLayout) _dataComposite.getLayout()).numColumns = 2;

		_user = _facade.getCurrentUser().clone();

		Label userIDLabel = new Label(_dataComposite, SWT.NONE);
		userIDLabel.setText("ID"); //$NON-NLS-1$ //$NON-NLS-2$
		userIDLabel.setLayoutData(new GridData());

		Text userIDText = new Text(_dataComposite, SWT.BORDER);
		userIDText.setLayoutData(new GridData());
		((GridData) userIDText.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) userIDText.getLayoutData()).grabExcessHorizontalSpace = true;
		if (_user.getPdrId() != null)
		{
			userIDText.setText(_user.getPdrId().toString());
		}
		else
		{
			userIDText.setText(""); //$NON-NLS-1$
		}
		userIDText.setEditable(false);

		Label userNameLabel = new Label(_dataComposite, SWT.NONE);
		userNameLabel.setText(NLMessages.getString("LoginDialog_userName") + "*"); //$NON-NLS-1$ //$NON-NLS-2$
		userNameLabel.setLayoutData(new GridData());

		_userNameText = new Text(_dataComposite, SWT.BORDER);
		_userNameText.setLayoutData(new GridData());
		((GridData) _userNameText.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _userNameText.getLayoutData()).grabExcessHorizontalSpace = true;
		if (_user.getAuthentication() != null && _user.getAuthentication().getUserName() != null)
		{
			_userNameText.setText(_user.getAuthentication().getUserName());
		}
		else
		{
			_userNameText.setText(""); //$NON-NLS-1$
		}
		_userNameText.addListener(SWT.Modify, new Listener()
		{
			@Override
			public void handleEvent(final Event event)
			{
				_user.getAuthentication().setUserName(_userNameText.getText());
			}
		});
		Label userPasswordLabel = new Label(_dataComposite, SWT.NONE);
		userPasswordLabel.setText(NLMessages.getString("LoginDialog_password") + "*"); //$NON-NLS-1$ //$NON-NLS-2$
		userPasswordLabel.setLayoutData(new GridData());

		_userPasswordText = new Text(_dataComposite, SWT.BORDER | SWT.PASSWORD);
		_userPasswordText.setLayoutData(new GridData());
		((GridData) _userPasswordText.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _userPasswordText.getLayoutData()).grabExcessHorizontalSpace = true;
		if (_user.getAuthentication() != null && _user.getAuthentication().getPassword() != null)
		{
			_userPasswordText.setText(_user.getAuthentication().getUserName());
		}
		else
		{
			_userPasswordText.setText(""); //$NON-NLS-1$
		}
		_userPasswordText.addListener(SWT.Modify, new Listener()
		{
			@Override
			public void handleEvent(final Event event)
			{
				_user.getAuthentication().setPassword(_userPasswordText.getText());
			}
		});

		Label userRoleLabel = new Label(_dataComposite, SWT.NONE);
		userRoleLabel.setText(NLMessages.getString("Dialog_user_role"));
		userRoleLabel.setLayoutData(new GridData());

		Text userRole = new Text(_dataComposite, SWT.BORDER);
		userRole.setEditable(false);
		userRole.setLayoutData(new GridData());
		((GridData) userRole.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) userRole.getLayoutData()).grabExcessHorizontalSpace = true;
		if (_user.getAuthentication() != null && _user.getAuthentication().getRoles() != null)
		{
			String roles = ""; //$NON-NLS-1$
			for (String r : _user.getAuthentication().getRoles())
			{
				roles += r + " "; //$NON-NLS-1$
			}
			userRole.setText(roles);
		}
		else
		{
			userRole.setText(""); //$NON-NLS-1$
		}

		Label userFornameLabel = new Label(_dataComposite, SWT.NONE);
		userFornameLabel.setText(NLMessages.getString("Dialog_user_forename"));
		userFornameLabel.setLayoutData(new GridData());

		final Text userForenameText = new Text(_dataComposite, SWT.BORDER);
		userForenameText.setLayoutData(new GridData());
		((GridData) userForenameText.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) userForenameText.getLayoutData()).grabExcessHorizontalSpace = true;
		if (_user.getUserInformation() != null && _user.getUserInformation().getForename() != null)
		{
			userForenameText.setText(_user.getUserInformation().getForename());
		}
		else
		{
			userForenameText.setText(""); //$NON-NLS-1$
		}
		userForenameText.addListener(SWT.Modify, new Listener()
		{
			@Override
			public void handleEvent(final Event event)
			{
				_user.getUserInformation().setForename(userForenameText.getText());
			}
		});

		Label userSurnameLabel = new Label(_dataComposite, SWT.NONE);
		userSurnameLabel.setText(NLMessages.getString("Dialog_user_surname"));
		userSurnameLabel.setLayoutData(new GridData());

		final Text userSurnameText = new Text(_dataComposite, SWT.BORDER);
		userSurnameText.setLayoutData(new GridData());
		((GridData) userSurnameText.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) userSurnameText.getLayoutData()).grabExcessHorizontalSpace = true;
		if (_user.getUserInformation() != null && _user.getUserInformation().getUserProjectPosition() != null)
		{
			userSurnameText.setText(_user.getUserInformation().getSurname());
		}
		else
		{
			userSurnameText.setText(""); //$NON-NLS-1$
		}
		userSurnameText.addListener(SWT.Modify, new Listener()
		{
			@Override
			public void handleEvent(final Event event)
			{
				_user.getUserInformation().setUserProjectPosition(userSurnameText.getText());
			}
		});

		Label userProjectLabel = new Label(_dataComposite, SWT.NONE);
		userProjectLabel.setText(NLMessages.getString("Dialog_user_project"));
		userProjectLabel.setLayoutData(new GridData());

		final Text userProjectText = new Text(_dataComposite, SWT.BORDER);
		userProjectText.setLayoutData(new GridData());
		((GridData) userProjectText.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) userProjectText.getLayoutData()).grabExcessHorizontalSpace = true;
		if (_user.getUserInformation() != null && _user.getUserInformation().getProjectName() != null)
		{
			userProjectText.setText(_user.getUserInformation().getProjectName());
		}
		else
		{
			userProjectText.setText(""); //$NON-NLS-1$
		}
		userProjectText.addListener(SWT.Modify, new Listener()
		{
			@Override
			public void handleEvent(final Event event)
			{
				_user.getUserInformation().setProjectName(userProjectText.getText());
			}
		});

		Label userPositionLabel = new Label(_dataComposite, SWT.NONE);
		userPositionLabel.setText(NLMessages.getString("Dialog_user_position"));
		userPositionLabel.setLayoutData(new GridData());

		final Text userPositionText = new Text(_dataComposite, SWT.BORDER);
		userPositionText.setLayoutData(new GridData());
		((GridData) userPositionText.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) userPositionText.getLayoutData()).grabExcessHorizontalSpace = true;
		if (_user.getUserInformation() != null && _user.getUserInformation().getUserProjectPosition() != null)
		{
			userPositionText.setText(_user.getUserInformation().getUserProjectPosition());
		}
		else
		{
			userPositionText.setText(""); //$NON-NLS-1$
		}
		userPositionText.addListener(SWT.Modify, new Listener()
		{
			@Override
			public void handleEvent(final Event event)
			{
				_user.getUserInformation().setUserProjectPosition(userPositionText.getText());
			}
		});

		Label userContactLabel = new Label(_dataComposite, SWT.NONE);
		userContactLabel.setText(NLMessages.getString("Dialog_user_contact"));
		userContactLabel.setLayoutData(new GridData());

		Label userContactLabelblanc = new Label(_dataComposite, SWT.NONE);
		userContactLabelblanc.setText(""); //$NON-NLS-1$
		userContactLabelblanc.setLayoutData(new GridData());
		if (_user.getUserInformation() == null)
		{
			_user.setUserInformation(new UserInformation());
			_user.getUserInformation().setUserContacts(new Vector<UserContact>(2));
		}
		
		if (_user.getUserInformation().getUserContacts() != null)
		{
			_contactComposite = new Composite(_dataComposite, SWT.NONE);
			_contactComposite.setLayoutData(new GridData());
			((GridData) _contactComposite.getLayoutData()).verticalAlignment = SWT.FILL;
			((GridData) _contactComposite.getLayoutData()).horizontalAlignment = SWT.FILL;
			((GridData) _contactComposite.getLayoutData()).minimumHeight = 90;
			((GridData) _contactComposite.getLayoutData()).grabExcessHorizontalSpace = true;
			((GridData) _contactComposite.getLayoutData()).horizontalSpan = 2;

			_contactComposite.setLayout(new GridLayout());
			((GridLayout) _contactComposite.getLayout()).makeColumnsEqualWidth = false;
			((GridLayout) _contactComposite.getLayout()).numColumns = 2;

			for (int j = 0; j < 3; j++)
			{
				final UserContact contact;
				if (j < _user.getUserInformation().getUserContacts().size())
				{
					contact = _user.getUserInformation().getUserContacts().get(j);
				}
				else
				{
					contact = new UserContact();
					_user.getUserInformation().getUserContacts().add(contact);
				}
				final Combo contactType = new Combo(_contactComposite, SWT.NONE | SWT.READ_ONLY);
				userPositionLabel.setLayoutData(new GridData());
				contactType.setItems(CONTACT_TYPES);
				if (contact.getType() != null)
				{
					contactType.select(contactType.indexOf(contact.getType()));
				}
				else
				{
					contactType.select(j);
					contact.setType(contactType.getItem(j));
				}
				contactType.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent se)
					{
						contact.setType(contactType.getItem(contactType.getSelectionIndex()));
					}
				});

				final Text contactText = new Text(_contactComposite, SWT.BORDER);
				contactText.setLayoutData(new GridData());
				((GridData) contactText.getLayoutData()).horizontalAlignment = SWT.FILL;
				((GridData) contactText.getLayoutData()).grabExcessHorizontalSpace = true;
				if (contact.getContact() != null)
				{
					contactText.setText(contact.getContact());
				}
				else
				{
					contactText.setText(""); //$NON-NLS-1$
				}
				contactText.addListener(SWT.Modify, new Listener()
				{
					@Override
					public void handleEvent(final Event event)
					{
						contact.setContact(contactText.getText());
					}
				});

				// final Button deleteContact = new Button(contactComposite,
				// SWT.PUSH);
				//				deleteContact.setText("-"); //$NON-NLS-1$
				// deleteContact.setToolTipText("Remove this Field");
				// deleteContact.setLayoutData(new GridData());
				//				deleteContact.setData("id", j); //$NON-NLS-1$
				// deleteContact.addSelectionListener(new SelectionAdapter()
				// {
				// public void widgetSelected(final SelectionEvent event)
				// {
				// user.getUserInformation().getUserContacts()
				//				.removeElementAt((Integer) deleteContact.getData("id")); //$NON-NLS-1$
				// loadValues();
				//
				//
				// } });
				// deleteContact.setLayoutData(new GridData());
				//
				// if (j == 0)
				// {
				// final Button addContact = new Button(contactComposite,
				// SWT.PUSH);
				//					addContact.setText("+"); //$NON-NLS-1$
				// addContact.setToolTipText("Add Contact Field");
				// addContact.setLayoutData(new GridData());
				// addContact.addSelectionListener(new SelectionAdapter()
				// {
				// public void widgetSelected(final SelectionEvent event)
				// {
				// UserContact c = new UserContact();
				// user.getUserInformation().getUserContacts().add(c);
				// loadValues();
				//
				// } });
				// addContact.setLayoutData(new GridData());
				// }
				// else
				// {
				// Label labelblanc = new Label(contactComposite, SWT.NONE);
				// labelblanc.setText("");
				// labelblanc.setLayoutData(new GridData());
				// }

			}
		}
		_contactComposite.redraw();
		_contactComposite.layout();
		_contactComposite.pack();
		_dataComposite.redraw();
		_dataComposite.layout();
		_dataComposite.pack();
		_mainComposite.redraw();
		_mainComposite.layout();
		_mainComposite.pack();
		_parent.redraw();
		_parent.layout();
		_parent.pack();
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
		_facade.setCurrentUser(_user);
		try
		{
			_userManager.saveUser(_user);
		}
		catch (XMLStreamException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
