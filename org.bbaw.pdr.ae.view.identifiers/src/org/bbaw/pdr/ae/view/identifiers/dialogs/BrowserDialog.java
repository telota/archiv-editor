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
package org.bbaw.pdr.ae.view.identifiers.dialogs;

import org.bbaw.pdr.ae.common.AEConstants;
import org.bbaw.pdr.ae.common.CommonActivator;
import org.bbaw.pdr.ae.common.NLMessages;
import org.bbaw.pdr.ae.config.model.ConfigData;
import org.bbaw.pdr.ae.config.model.ConfigItem;
import org.bbaw.pdr.ae.config.model.DatatypeDesc;
import org.bbaw.pdr.ae.config.model.IdentifierConfig;
import org.bbaw.pdr.ae.control.facade.Facade;
import org.bbaw.pdr.ae.model.Person;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

/**
 * @author cplutte
 *
 * class creates the user-preference dialog for setting user preferences.
 */
/** */
public class BrowserDialog extends TitleAreaDialog
{
	/** Browser. */
	private Browser _browser;

	/** The url. */
	private String _url;

	/** The message. */
	private String _message;

	/** The Id text. */
	private Text _idText;

	/** The Id no. */
	private Text _idNo;

	/** The Url text. */
	private Text _urlText;

	/** composite of generalTabItem. */
	private Composite _generalComposite;
	/** singleton facade. */
	private Facade _facade = Facade.getInstanz();

	/** The current person. */
	private Person _currentPerson;

	/** The request type. */
	private String _requestType;

	private String _configProvider = Platform
			.getPreferencesService()
			.getString(CommonActivator.PLUGIN_ID, "PRIMARY_SEMANTIC_PROVIDER", AEConstants.CLASSIFICATION_AUTHORITY,
					null).toUpperCase();

	/**
	 * Instantiates a new browser dialog.
	 * @param url
	 * @param parentShell the parent shell
	 */
	public BrowserDialog(String url, final Shell parentShell)
	{
		super(parentShell);
		this._url = url;
	}

	@Override
	public final void create()
	{
		super.create();

		_currentPerson = _facade.getCurrentPerson();
		_requestType = _facade.getRequestedIdentifierType();

		// System.out.println("browser requestType " + _requestType);
		// choose what kind of identifier is requested.
		if (_requestType != null)
		{
			if (_requestType.equalsIgnoreCase("pnd")) //$NON-NLS-1$
			{
				_message = NLMessages.getString("BrowserDialog_PNDnameFor");
			}
			else if (_requestType.equalsIgnoreCase("lccn")) //$NON-NLS-1$
			{
				_message = NLMessages.getString("BrowserDialog_LCCNnameFor");
			}
			else if (_requestType.equalsIgnoreCase("iccu")) //$NON-NLS-1$
			{
				_message = NLMessages.getString("BrowserDialog_ICCUnameFor");
			}
			else if (_requestType.equalsIgnoreCase("viaf")) //$NON-NLS-1$
			{
				_message = NLMessages.getString("BrowserDialog_VIAFnameFor");
			}
			else
			{
				_message = NLMessages.getString("BrowserDialog_errorMessageError");
			}
		}

		if (_currentPerson.getBasicPersonData() != null
				&& _currentPerson.getBasicPersonData().getComplexNames() != null
				&& _currentPerson.getBasicPersonData().getComplexNames().firstElement() != null)
		{
			if (_currentPerson.getBasicPersonData().getComplexNames().firstElement().getSurName() != null)
			{
				_message += _currentPerson.getBasicPersonData().getComplexNames().firstElement().getSurName() + ", ";
			}
			if (_currentPerson.getBasicPersonData().getComplexNames().firstElement().getForeName() != null)
			{
				_message += _currentPerson.getBasicPersonData().getComplexNames().firstElement().getForeName();
			}
		}
		// Set the title
		setTitle(NLMessages.getString("BrowserDialog_titleExternalPersonIdentifier"));
		// set message
		setMessage(_message, IMessageProvider.INFORMATION);

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
		// createOkButton(parent, OK,
		// NLMessages.getString("BrowserDialog_save"), true);
		// Add a SelectionListener

		// Create Cancel button
		Button cancelButton = createButton(parent, CANCEL, NLMessages.getString("BrowserDialog_close"), false);
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

		// System.out.println("requested type : " + facade.getRequestedId());

		_generalComposite = new Composite(parent, SWT.NONE);
		_generalComposite.setLayoutData(new GridData());
		((GridData) _generalComposite.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _generalComposite.getLayoutData()).grabExcessHorizontalSpace = true;
		_generalComposite.setLayout(new GridLayout());
		((GridLayout) _generalComposite.getLayout()).makeColumnsEqualWidth = false;
		((GridLayout) _generalComposite.getLayout()).numColumns = 5;

		Label idLabel = new Label(_generalComposite, SWT.NONE);
		idLabel.setText(NLMessages.getString("BrowserDialog_identifier"));

		_idText = new Text(_generalComposite, SWT.READ_ONLY);
		_idText.setText(""); //$NON-NLS-1$

		_idNo = new Text(_generalComposite, SWT.BORDER);
		_idNo.setText(""); //$NON-NLS-1$

		Label urlLabel = new Label(_generalComposite, SWT.NONE);
		urlLabel.setText(NLMessages.getString("BrowserDialog_url"));

		_urlText = new Text(_generalComposite, SWT.READ_ONLY);
		_urlText.setText(""); //$NON-NLS-1$

		loadValues();

		_generalComposite.pack();

		_browser = new Browser(parent, SWT.FILL | SWT.BORDER);
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
		ISelection selection = window.getSelectionService().getSelection();
		setUrlFromSelection(selection);
		createSelectionListener();
		GridData layoutData = new GridData(GridData.FILL_BOTH);
		layoutData.horizontalAlignment = SWT.FILL;
		layoutData.widthHint = 800;
		layoutData.heightHint = 400;
		_browser.setLayoutData(layoutData);

		final ProgressBar progressBar = new ProgressBar(parent, SWT.NONE);
		GridData data = new GridData();
		data.horizontalSpan = 3;
		data = new GridData();
		data.horizontalAlignment = GridData.BEGINNING;
		data.verticalAlignment = GridData.END;
		progressBar.setLayoutData(data);

		_browser.addProgressListener(new ProgressListener()
		{
			@Override
			public void changed(final ProgressEvent event)
			{
				if (event.total == 0)
				{
					return;
				}
				int ratio = event.current * 100 / event.total;
				progressBar.setSelection(ratio);
			}

			@Override
			public void completed(final ProgressEvent event)
			{
				progressBar.setSelection(100);
				progressBar.setSelection(0);

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
	 * @param defaultButton is default.
	 * @return button.
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
				}
				close();
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
	 * Creates the selection listener.
	 */
	private void createSelectionListener()
	{
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
		ISelectionService selectionService = window.getSelectionService();
		selectionService.addSelectionListener(new ISelectionListener()
		{

			@Override
			public void selectionChanged(final IWorkbenchPart part, final ISelection selection)
			{
				setUrlFromSelection(selection);
			}
		});
	}

	@Override
	protected final boolean isResizable()
	{
		return true;
	}

	/**
	 * checks if input is valid.
	 * @return true
	 */
	private boolean isValidInput()
	{
		boolean valid = true;
		// if (langText.getText().length() == 0) {
		// setErrorMessage("Bitte");
		// valid = false;
		// }
		// if (lastNameText.getText().length() == 0) {
		// setErrorMessage("Please maintain the last name");
		// valid = false;
		// }
		return valid;
	}

	/**
	 * Load values.
	 */
	private void loadValues()
	{
		_facade = Facade.getInstanz();
		_currentPerson = _facade.getCurrentPerson();
		if (_url != null)
		{

		}
		else
		{
			_requestType = _facade.getRequestedIdentifierType();
			String identifier = _facade.getRequestedIdentifier();

			if (_facade.getConfigs().get(_configProvider) != null
					&& _facade.getConfigs().get(_configProvider).getUsage() != null
					&& !_facade.getConfigs().get(_configProvider).getUsage().getIdentifiers().getChildren().isEmpty())
			{
				DatatypeDesc dtd = (DatatypeDesc) _facade.getConfigs().get(_configProvider);
				ConfigData ids = (ConfigData) dtd.getUsage().getIdentifiers();
				ConfigData ci = (ConfigData) ids.getChildren().get(_requestType);
				if (ci == null)
				{
					ci = (IdentifierConfig) _facade.getConfigs().get(_configProvider).getUsage().getIdentifiers()
							.getChildren().get(_requestType.toUpperCase());
				}
				if (ci != null && ci instanceof IdentifierConfig)
				{
					IdentifierConfig ic = (IdentifierConfig) ci;
					_url = ic.getUrl();
					if (ic.getPrefix() != null)
					{
						_url += ic.getPrefix();
					}
					_url += identifier;
					if (ic.getSuffix() != null)
					{
						_url += ic.getSuffix();
					}
				}
			}
			if (identifier != null)
			{
				_idNo.setText(identifier);
			}
		}
		_idText.setText(NLMessages.getString("BrowserDialog_PDR"));

		if (_url != null)
		{
			_urlText.setText(_url);
		}
	}

	@Override
	protected final void okPressed()
	{
		saveInput();
		// super.okPressed();
	}

	// We need to have the textFields into Strings because the UI gets disposed
	// and the Text Fields are not accessible any more.
	/**
	 * Save input.
	 */
	private void saveInput()
	{

		// firstName = firstNameText.getText();
		// lastName = lastNameText.getText();

	}

	/**
	 * Sets the url from selection.
	 * @param selection the new url from selection
	 */
	private void setUrlFromSelection(final ISelection selection)
	{

		if (!_browser.isDisposed() && _url != null)
		{
			_browser.setUrl(_url);
		}
	}

}
