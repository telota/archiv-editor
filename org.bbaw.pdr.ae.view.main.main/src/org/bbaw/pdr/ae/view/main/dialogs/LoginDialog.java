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

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observer;

import org.bbaw.pdr.ae.common.AEConstants;
import org.bbaw.pdr.ae.common.CommonActivator;
import org.bbaw.pdr.ae.common.NLMessages;
import org.bbaw.pdr.ae.control.core.UserRichtsChecker;
import org.bbaw.pdr.ae.control.facade.Facade;
import org.bbaw.pdr.ae.control.interfaces.IUpdateManager;
import org.bbaw.pdr.ae.control.interfaces.IUserManager;
import org.bbaw.pdr.ae.model.User;
import org.bbaw.pdr.ae.view.control.dialogs.TimeoutProgressMonitorDialog;
import org.bbaw.pdr.ae.view.main.internal.Activator;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
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
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * Class creates login dialog for entering the user name. TODO it has to be
 * adapted to dynamic data TODO vielleicht ist es besser, wenn der dialog nicht
 * schon beim Start öffnet, sondern erst nachdem das Programm geladen ist und
 * wenn der Benutzer eine erste Aktion ausführen möchte.
 * @author Christoph Plutte
 */
public class LoginDialog extends TitleAreaDialog
{
	/** singleton instace of facade. */
	private Facade _facade = Facade.getInstanz();

	/** The _ur checker. */
	private UserRichtsChecker _urChecker = new UserRichtsChecker();

	/** The _observers. */
	private ArrayList<Observer> _observers = new ArrayList<Observer>();
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

	/** The _user manager. */
	private IUserManager _userManager = _facade.getUserManager();

	/** The _saved users. */
	private HashMap<String, User> _savedUsers = new HashMap<String, User>(5);

	/** The _on start. */
	private boolean _onStart = false;

	/** The _add user to store. */
	private boolean _addUserToStore = true;

	private Button _OKButton;

	private String _userID;


	private int _projectId;

	private boolean usersInitialized;
	/** Logger. */
	private static ILog iLogger = AEConstants.ILOGGER;

	/**
	 * Instantiates a new login dialog.
	 * @param parentShell the parent shell
	 * @param onStart the on start
	 */
	public LoginDialog(final Shell parentShell, final boolean onStart)
	{
		super(parentShell);
		this._onStart = onStart;
	}

	/**
	 * Adds the observer.
	 * @param observer the observer
	 */
	public final void addObserver(Observer observer)
	{
		_observers.add(observer);
	}

	@Override
	public final void create()
	{
		super.create();

		// Set the title
		setTitle(NLMessages.getString("LoginDialog_enterUserName")); //$NON-NLS-1$
		// Set the message
		setMessage(NLMessages.getString("LoginDialog_messagePleaseEnterUserName"), IMessageProvider.INFORMATION); //$NON-NLS-1$

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
		if (_onStart && _savedUsers != null && _savedUsers.size() > 0)
		{
			Button deleteUser = createButton(parent, 3,
					NLMessages.getString("LoginDialog_login_delete_user_from_cache"), false);
			deleteUser.addSelectionListener(new SelectionAdapter()
			{
				@Override
				public void widgetSelected(final SelectionEvent e)
				{
					_savedUsers.remove(_userNameCombo.getText());
					try
					{
						initializeUsers();
					}
					catch (Exception e1)
					{
						e1.printStackTrace();
					}
				}

			});
		}
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
		verifyAndEnableOkButten();
	}

	@Override
	public final Control createDialogArea(final Composite parent)
	{

		if (_onStart)
		{
			try
			{
				_savedUsers = _userManager.getMapOfSavedUsers();
			}
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		if (_savedUsers == null)
		{
			_savedUsers = new HashMap<String, User>();
		}

		Composite mainComposite = new Composite(parent, SWT.NONE);

		mainComposite.setLayoutData(new GridData());
		((GridData) mainComposite.getLayoutData()).verticalAlignment = SWT.FILL;
		((GridData) mainComposite.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) mainComposite.getLayoutData()).minimumHeight = 90;
		((GridData) mainComposite.getLayoutData()).grabExcessHorizontalSpace = true;

		mainComposite.setLayout(new GridLayout());
		((GridLayout) mainComposite.getLayout()).makeColumnsEqualWidth = true;
		((GridLayout) mainComposite.getLayout()).numColumns = 2;

		Label userNameLabel = new Label(mainComposite, SWT.NONE);
		userNameLabel.setText(NLMessages.getString("LoginDialog_userName")); //$NON-NLS-1$
		userNameLabel.setLayoutData(new GridData());

		_userNameCombo = new Combo(mainComposite, SWT.BORDER);
		_userNameCombo.setLayoutData(new GridData());
		((GridData) _userNameCombo.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _userNameCombo.getLayoutData()).grabExcessHorizontalSpace = true;

		Label userPasswordLabel = new Label(mainComposite, SWT.NONE);
		userPasswordLabel.setText(NLMessages.getString("LoginDialog_password")); //$NON-NLS-1$
		userPasswordLabel.setLayoutData(new GridData());

		_userPasswordText = new Text(mainComposite, SWT.BORDER | SWT.PASSWORD);
		_userPasswordText.setLayoutData(new GridData());
		((GridData) _userPasswordText.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _userPasswordText.getLayoutData()).grabExcessHorizontalSpace = true;

		_userPasswordText.addListener(SWT.Modify, new Listener()
		{
			@Override
			public void handleEvent(final Event event)
			{
//				verifyAndEnableOkButten();
			}
		});

		_userNameCombo.addListener(SWT.Modify, new Listener()
		{
			@Override
			public void handleEvent(final Event event)
			{
				if (_onStart)
				{
					if (_userNameCombo.getText() != null && _userNameCombo.getText().trim().length() > 0)
					{
						User u = _savedUsers.get(_userNameCombo.getText().trim());
						if (u != null && u.getAuthentication() != null)
						{
							_userPasswordText.setText(u.getAuthentication().getPassword());
						}
					}
					else if (_userNameCombo.getText() == null || _userNameCombo.getText().trim().length() == 0)
					{
						_userPasswordText.setText("");
					}
				}
//				verifyAndEnableOkButten();
			}
		});
		if (_onStart)
		{
			User lastUser = null;
			try
			{
				lastUser = _userManager.getUserById(Platform.getPreferencesService().getString(
						CommonActivator.PLUGIN_ID, "USER_SAVE_ID", "", null));
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

			for (String userNames : _savedUsers.keySet())
			{
				_userNameCombo.add(userNames);
				// System.out.println("user added " + userNames);
			}
			int index = 0;
			if (lastUser != null
					&& ((lastUser.getPdrId().getInstance() == Platform.getPreferencesService().getInt(
							CommonActivator.PLUGIN_ID, "REPOSITORY_ID", AEConstants.REPOSITORY_ID, null) && lastUser
							.getPdrId().getProjectID() == Platform.getPreferencesService().getInt(
							CommonActivator.PLUGIN_ID, "PROJECT_ID", AEConstants.PROJECT_ID, null)) || _urChecker
							.isUserPDRAdmin(lastUser)))
			{
				index = Math.max(_userNameCombo.indexOf(lastUser.getAuthentication().getUserName()), 0);
			}
			_userNameCombo.select(index);
		}
		// userNameText.setText("");

		if (_onStart)
		{
			// userPasswordText.setText("");
			Label saveLogin = new Label(mainComposite, SWT.NONE);
			saveLogin.setText("Save Login Data");
			saveLogin.setLayoutData(new GridData());

			final Button saveLoginButton = new Button(mainComposite, SWT.CHECK);
			saveLoginButton.setLayoutData(new GridData());
			saveLoginButton.setSelection(_addUserToStore); //$NON-NLS-1$
			saveLoginButton.addSelectionListener(new SelectionAdapter()
			{
				@Override
				public void widgetSelected(final SelectionEvent event)
				{
					_addUserToStore = !_addUserToStore;
				}
			}); // SelectionListener

			Label saveUserLabel = new Label(mainComposite, SWT.NONE);
			saveUserLabel.setText(NLMessages.getString("LoginDialog_login_user_automatically"));
			saveUserLabel.setLayoutData(new GridData());

			final Button saveUserButton = new Button(mainComposite, SWT.CHECK);
			saveUserButton.setLayoutData(new GridData());
			saveUserButton.setSelection(Platform.getPreferencesService().getBoolean(CommonActivator.PLUGIN_ID,
					"USER_SAVE_LOGIN", false, null)); //$NON-NLS-1$
			saveUserButton.addSelectionListener(new SelectionAdapter()
			{
				@Override
				public void widgetSelected(final SelectionEvent event)
				{
					CommonActivator.getDefault().getPreferenceStore()
							.setValue("USER_SAVE_LOGIN", saveUserButton.getSelection()); //$NON-NLS-1$
				}
			}); // SelectionListener
		}


		parent.pack();

		return parent;
	}

	private void verifyAndEnableOkButten()
	{
		if (_OKButton != null)
		{
			boolean valid = isValidInput();
			_OKButton.setEnabled(true);
			if (valid)
			{
				setErrorMessage(null);
				setMessage(null);
			}
		}

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
		_OKButton = new Button(parent, SWT.PUSH);
		_OKButton.setText(label);
		_OKButton.setFont(JFaceResources.getDialogFont());
		_OKButton.setData(new Integer(id));
		_OKButton.addSelectionListener(new SelectionAdapter()
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
				shell.setDefaultButton(_OKButton);
			}
		}
		setButtonLayoutData(_OKButton);
		return _OKButton;
	}

	/**
	 * Gets the user.
	 * @return the user
	 */
	public final User getUser()
	{
		return _user;
	}

	/**
	 * Initialize users.
	 * @throws Exception the exception
	 */
	private void initializeUsers() throws Exception
	{

		User lastUser = _userManager.getUserById(Platform.getPreferencesService().getString(CommonActivator.PLUGIN_ID,
				"USER_SAVE_ID", "", null));

		_userNameCombo.removeAll();
		_userPasswordText.setText(""); //$NON-NLS-1$
		for (String userNames : _savedUsers.keySet())
		{
			_userNameCombo.add(userNames);
		}
		if (lastUser != null
				&& ((lastUser.getPdrId().getInstance() == Platform.getPreferencesService().getInt(
						CommonActivator.PLUGIN_ID, "REPOSITORY_ID", AEConstants.REPOSITORY_ID, null) && lastUser
						.getPdrId().getProjectID() == Platform.getPreferencesService().getInt(
						CommonActivator.PLUGIN_ID, "PROJECT_ID", AEConstants.PROJECT_ID, null)) || _urChecker
						.isUserPDRAdmin(lastUser)))
		{
			int index = Math.max(_userNameCombo.indexOf(lastUser.getAuthentication().getUserName()), 0);
			_userNameCombo.select(index);
		}
		_userNameCombo.layout();
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
		_projectId = Platform.getPreferencesService().getInt(CommonActivator.PLUGIN_ID, "PROJECT_ID",
				AEConstants.PROJECT_ID, null);
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
		if (!usersInitialized)
		{
			TimeoutProgressMonitorDialog dialog = new TimeoutProgressMonitorDialog(this.getShell(), 10000);
			dialog.setCancelable(false);

			try
			{
				dialog.run(true, true, new IRunnableWithProgress()
				{
					private Object _updateStatus;

					@Override
					public void run(final IProgressMonitor monitor)
					{
						try {
							IUpdateManager[] rums = Facade.getInstanz().getUpdateManagers();
							for (IUpdateManager rum : rums) {
								try {
									_userID = rum.getUserId(_userName, _projectId);
									monitor.setTaskName("Update Users from Repository");
									rum.loadInitialUsers(_userID, _userPassword, null);
									usersInitialized = true;
								} catch (Exception e) {
									_userManager.verifyOrCreateUsers();
									e.printStackTrace();
								}
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally{}
		}
		if (_onStart)
		{
			User u = null;
			try
			{
				u = _userManager.getUsersByUserName(_userName);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			if (u == null && loadUserData(_userName, _userPassword))
			{
				try
				{
					u = _userManager.getUsersByUserName(_userName);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				
			}
			if (u == null)
			{
				setErrorMessage(NLMessages.getString("LoginDialog_errorMessageUserNameUnknown")); //$NON-NLS-1$
				IStatus sname = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Login user name is valid"); //$NON-NLS-1$
				iLogger.log(sname);
				return false;
			}
			if (u.getAuthentication() != null && _userName.equals(u.getAuthentication().getUserName()))
			{
				IStatus sname = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Login user name is valid"); //$NON-NLS-1$
				iLogger.log(sname);
				if (_userPassword.equals(u.getAuthentication().getPassword()))
				{
					IStatus spw = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Login password is also valid"); //$NON-NLS-1$
					iLogger.log(spw);
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
		else
		{
			_user = _facade.getCurrentUser();
			if (_userName.equals(_user.getAuthentication().getUserName()))
			{
				if (_userPassword.equals(_user.getAuthentication().getPassword()))
				{
					valid = true;
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
				setErrorMessage(NLMessages.getString("LoginDialog_login_message_not_your_userName"));
				valid = false;
			}
		}

		// else-clause
		return valid;
	}

	/**
	 * Notify observers.
	 * @param string the string
	 */
	private void notifyObservers(final String string)
	{
		for (Observer o : _observers)
		{
			o.update(null, string);
		}
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
		if (_onStart)
		{
			_userName = _userNameCombo.getText();
			IStatus sun = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Login save user name: " + _userName); //$NON-NLS-1$
			iLogger.log(sun);
			_facade.setCurrentUser(_user);
			sun = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Login save user id: " + _user.getPdrId()); //$NON-NLS-1$
			iLogger.log(sun);
			//			if (Activator.getDefault().getPreferenceStore().getBoolean("USER_SAVE_LOGIN")) //$NON-NLS-1$
			// {
			//				Activator.getDefault().getPreferenceStore().setValue("USER_SAVE_ID", user.getPdrId().toString()); //$NON-NLS-1$
			// }
			CommonActivator.getDefault().getPreferenceStore().setValue("USER_SAVE_ID", _user.getPdrId().toString()); //$NON-NLS-1$
			if (_addUserToStore && !_savedUsers.containsKey(_userName))
			{
				_savedUsers.put(_userName, _user);
			}
			int i = 0;
			for (User u : _savedUsers.values())
			{
				if (u != null)
				{
					CommonActivator.getDefault().getPreferenceStore()
							.putValue("LAST_USER" + i, u.getPdrId().toString());
				}
				if (i >= 12)
				{
					break;
				}
				i++;

			}
			while (i <= 12)
			{
				CommonActivator.getDefault().getPreferenceStore().putValue("LAST_USER" + i, "");
				i++;
			}
		}

		notifyObservers(_user.getPdrId().toString());
		
	}

	private boolean loadUserData(String userName, String password)
	{
		IUserManager um = Facade.getInstanz().getUserManager();
		um.verifyOrCreateUsers();
		IUpdateManager[] rums = Facade.getInstanz().getUpdateManagers();
		String userID = null;
		// TODO update einkommentieren
		for (IUpdateManager rum : rums)
		{
			try
			{
				userID = rum.getUserId(
						userName,
						Platform.getPreferencesService().getInt(CommonActivator.PLUGIN_ID, "PROJECT_ID",
								AEConstants.PROJECT_ID, null));
			}
			catch (Exception e)
			{
				e.printStackTrace();
				return false;
			}
		}
		for (IUpdateManager rum : rums)
		{
			try
			{
				rum.updateUsers(userID, password, null);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}
}
