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
package org.bbaw.pdr.ae.config.editor.internal;

import org.bbaw.pdr.ae.common.NLMessages;
import org.bbaw.pdr.ae.config.core.ConfigFactory;
import org.bbaw.pdr.ae.config.core.IConfigFacade;
import org.bbaw.pdr.ae.config.core.IConfigManager;
import org.bbaw.pdr.ae.config.editor.view.ConfigEditor;
import org.bbaw.pdr.ae.config.model.DatatypeDesc;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;




/**
 * @author cplutte
 * Class creates login dialog for entering the user name.
 */
public class CreateConfigDialog extends TitleAreaDialog
{
	/** singleton instace of facade. */
	private IConfigFacade _configFacade = ConfigFactory.getConfigFacade();
	/** The provider text. */
	private Text _providerText;
	/** The new provider combo. */
	private Combo _newProviderCombo;
	/** The okbutton. */
	private Button _okbutton;
	/** The as config as default. */
	private Button _asConfigAsDefault;
    /** The config as default. */
    private boolean _configAsDefault = true;
	/** The config editor. */
	private ConfigEditor _configEditor;
//	/** Logger */
//	private static ILog iLogger = org.bbaw.pdr.ae.view.Activator.getILogger();
	/** The cfg manager. */
	private IConfigManager _cfgManager = _configFacade.getConfigManager();


	/**
	 * Instantiates a new creates the config dialog.
	 *
	 * @param parentShell
	 *            the parent shell
	 * @param configEditor
	 *            the config editor
	 */
	public CreateConfigDialog(final Shell parentShell, final ConfigEditor configEditor)
	{
		super(parentShell);
		this._configEditor = configEditor;
	}

	/**
	 *
	 * @see org.eclipse.jface.dialogs.Dialog#create()
	 */
	@Override
	public final void create()
	{
		super.create();

		// Set the title
		setTitle(NLMessages.getString("Config_new_classification_title")); //$NON-NLS-1$
		// Set the message
		setMessage(NLMessages.getString("Config_new_classification_message"), IMessageProvider.INFORMATION); //$NON-NLS-1$
	}


	/**
	 * create buttons for button bar.
	 * @param parent
	 *            parent composite.
	 * @see org.eclipse.jface.dialogs.Dialog#createButtonsForButtonBar(org.eclipse.swt.widgets.Composite)
	 */
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
		createOkButton(parent, OK, NLMessages.getString("Config_new"), true);
		// Add a SelectionListener

		// Create Cancel button
		Button cancelButton = createButton(parent, CANCEL, NLMessages.getString("Config_cancel"), false);
		// Add a SelectionListener
		cancelButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e)
			{
				setReturnCode(CANCEL);
				close();
			}
		});
	}


	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.TitleAreaDialog#createDialogArea(org.eclipse.swt.widgets.Composite)
	 * TODO the ComboBox has to be dynamized to adapted to an editable list of Identifiers.
	 * the list has to be editable trough the UserPreferenceDialog.
	 */
	/** create dialog area.
	 * @param parent parent composite.
	 * @return parent
	 * @see org.eclipse.jface.dialogs.TitleAreaDialog#createDialogArea(org.eclipse.swt.widgets.Composite)
	 */
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
		((GridLayout) mainComposite.getLayout()).makeColumnsEqualWidth = true;
		((GridLayout) mainComposite.getLayout()).numColumns = 2;

		Label newLabel = new Label(mainComposite, SWT.NONE);
		newLabel.setLayoutData(new GridData());
		newLabel.setText(NLMessages.getString("Config_new_provider"));

		_providerText = new Text(mainComposite, SWT.BORDER);
		_providerText.setLayoutData(new GridData());
		((GridData) _providerText.getLayoutData()).grabExcessHorizontalSpace = true;
   		((GridData) _providerText.getLayoutData()).horizontalAlignment = SWT.FILL;
//   		@SuppressWarnings({ "unchecked", "rawtypes" })
//		final Vector<String> genres = new Vector(Arrays.asList(_facade.getReferenceGenres()));
   		_providerText.addKeyListener(new KeyListener()
   		{
			@Override
			public void keyPressed(final KeyEvent e)
			{
			}

			@Override
			public void keyReleased(final KeyEvent e)
			{
//				System.out.println("genre text " + providerText.getText()); //$NON-NLS-1$
				if (_configFacade.getConfigs().containsKey(_providerText.getText().toUpperCase()))
				{
//					System.out.println("checking zwei"); //$NON-NLS-1$
					setMessage(NLMessages.getString("Config_message_same_provider_name"), SWT.ERROR);
					_okbutton.setEnabled(false);
				}
				else
				{
					setMessage(""); //$NON-NLS-1$
					_okbutton.setEnabled(true);
				}


			}

   		});

   		Label prov = new Label(mainComposite, SWT.NONE);
        prov.setLayoutData(new GridData());
        prov.setText(NLMessages.getString("Config_based_on"));
        _newProviderCombo = new Combo(mainComposite, SWT.BORDER);
        _newProviderCombo.setLayoutData(new GridData());
		((GridData) _newProviderCombo.getLayoutData()).grabExcessHorizontalSpace = true;
   		((GridData) _newProviderCombo.getLayoutData()).horizontalAlignment = SWT.FILL;
//        if (_facade.getConfigs() == null)
//		{
//        	System.out.println("configs = null");
//			try {
//				_facade.setConfigs(cfgManager.getConfigs());
//			} catch (XQException e1) {
//				e1.printStackTrace();
//			}
//		}
        if (_configFacade.getConfigs() == null)
		{
		}

		for (String s : _configFacade.getConfigs().keySet())
        {
        	_newProviderCombo.add(s);
        }
        _newProviderCombo.select(0);
        _newProviderCombo.addSelectionListener(new SelectionAdapter() {
    	@Override
			public void widgetSelected(final SelectionEvent e)
			{
    	}
        });

		new Label(mainComposite, SWT.NONE).setText("");
        _asConfigAsDefault = new Button(mainComposite, SWT.CHECK);
        _asConfigAsDefault.setSelection(_configAsDefault);
        _asConfigAsDefault.setLayoutData(new GridData());
        _asConfigAsDefault.setText(NLMessages.getString("Config_classification_as_default"));
        ((GridData) _asConfigAsDefault.getLayoutData()).horizontalAlignment = GridData.FILL;
        ((GridData) _asConfigAsDefault.getLayoutData()).grabExcessHorizontalSpace = true;
        _asConfigAsDefault.addSelectionListener(new SelectionAdapter() {
     	   @Override
			public void widgetSelected(final SelectionEvent event)
			{
     		   _configAsDefault = !_configAsDefault;
			};
		});
		parent.pack();

		return parent;
	}

	/**
	 * Creates the new config.
	 *
	 * @param newProvider
	 *            the new provider
	 * @param baseProvider
	 *            the base provider
	 */
	protected final void createNewConfig(final String newProvider, final String baseProvider)
	{
		DatatypeDesc newConfig = null;
		try
		{
			newConfig = _cfgManager.getDatatypeDesc(baseProvider);
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (newConfig != null)
		{
			newConfig.setProvider(newProvider);
			_configFacade.getConfigs().put(newProvider, newConfig);
			try
			{
				_cfgManager.saveConfig(newConfig);
			}
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (_configAsDefault)
	    	{
	    		Activator.getDefault().getPreferenceStore().setValue("PRIMARY_SEMANTIC_PROVIDER", newConfig.getProvider()); //$NON-NLS-1$
				Activator.getDefault().getPreferenceStore()
						.setValue("PRIMARY_TAGGING_PROVIDER", newConfig.getProvider()); //$NON-NLS-1$
	     		Activator.getDefault().getPreferenceStore().setValue("PRIMARY_RELATION_PROVIDER", newConfig.getProvider()); //$NON-NLS-1$
	    	}
			_configEditor.setNewConfiguration(newConfig);
		}
	}

	/**
	 * creates OKButton.
	 *
	 * @param parent
	 *            parent composite
	 * @param id
	 *            id
	 * @param label
	 *            label of button
	 * @param defaultButton
	 *            is default
	 * @return button
	 */
	protected final Button createOkButton(final Composite parent, final int id, final String label,
			final boolean defaultButton)
	{
		// increment the number of columns in the button bar
		((GridLayout) parent.getLayout()).numColumns++;
		_okbutton = new Button(parent, SWT.PUSH);
		_okbutton.setText(label);
		_okbutton.setFont(JFaceResources.getDialogFont());
		_okbutton.setData(new Integer(id));
		_okbutton.addSelectionListener(new SelectionAdapter() {
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
				shell.setDefaultButton(_okbutton);
			}
		}
		setButtonLayoutData(_okbutton);
		return _okbutton;
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
	 * @return boolean valid TODO die Überprüfung des Benutzernamens und
	 *         Passwortes soll nicht hier, sondern in der Controller-Schicht
	 *         oder sogar in der DAtenhaltung aus geführt werden. TODO
	 *         User-datenbank einbauen.
	 */
	private boolean isValidInput()
	{
		boolean valid = true;

		// else-clause
		return valid;
	}
	/**
	 *
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	@Override
	protected final void okPressed()
	{
		saveInput();
//			super.okPressed();
	}

	/**
	 * if user name and password are correct the identified current user
	 * is saved as currentUser in facade.
	 */
	private void saveInput()
	{

		createNewConfig(_providerText.getText().trim().toUpperCase(), _newProviderCombo.getItem(_newProviderCombo.getSelectionIndex()));

	}

}


