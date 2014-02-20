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
package org.bbaw.pdr.ae.view.main.editors;

import java.text.SimpleDateFormat;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import org.bbaw.pdr.ae.common.AEConstants;
import org.bbaw.pdr.ae.common.CommonActivator;
import org.bbaw.pdr.ae.common.NLMessages;
import org.bbaw.pdr.ae.common.icons.IconsInternal;
import org.bbaw.pdr.ae.config.model.ConfigData;
import org.bbaw.pdr.ae.control.core.UserRichtsChecker;
import org.bbaw.pdr.ae.control.facade.Facade;
import org.bbaw.pdr.ae.control.interfaces.IPdrIdService;
import org.bbaw.pdr.ae.metamodel.IAEPresentable;
import org.bbaw.pdr.ae.metamodel.PdrId;
import org.bbaw.pdr.ae.metamodel.Record;
import org.bbaw.pdr.ae.metamodel.Revision;
import org.bbaw.pdr.ae.model.Concurrence;
import org.bbaw.pdr.ae.model.Concurrences;
import org.bbaw.pdr.ae.model.Identifier;
import org.bbaw.pdr.ae.model.Identifiers;
import org.bbaw.pdr.ae.model.PdrObject;
import org.bbaw.pdr.ae.model.Person;
import org.bbaw.pdr.ae.model.Reference;
import org.bbaw.pdr.ae.model.User;
import org.bbaw.pdr.ae.model.ValidationStm;
import org.bbaw.pdr.ae.model.view.Facet;
import org.bbaw.pdr.ae.view.control.ViewHelper;
import org.bbaw.pdr.ae.view.control.customSWTWidges.RevisionHistoryToolTip;
import org.bbaw.pdr.ae.view.control.dialogs.SelectObjectDialog;
import org.bbaw.pdr.ae.view.control.provider.AutoCompleteNameLabelProvider;
import org.bbaw.pdr.ae.view.control.provider.FacetContentProposalProvider;
import org.bbaw.pdr.ae.view.control.provider.MarkupContentProvider;
import org.bbaw.pdr.ae.view.control.provider.MarkupLabelProvider;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.bindings.keys.KeyStroke;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.fieldassist.ContentProposalAdapter;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.jface.fieldassist.IContentProposalListener;
import org.eclipse.jface.fieldassist.TextContentAdapter;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.IHandlerService;

/**
 * The Class PersonEditorDialog.
 * @author Christoph Plutte
 */
public class PersonEditorDialog extends TitleAreaDialog implements Observer
{

	/** The Constant ID. */
	public static final String ID = "org.bbaw.pdr.ae.view.main.editors.PersonEditorDialog"; //$NON-NLS-1$
	/** date format of administrative dates in PDR. */
	private SimpleDateFormat _adminDateFormat = AEConstants.ADMINDATE_FORMAT;
	/** Instance of shared image registry. */
	private ImageRegistry _imageReg = CommonActivator.getDefault().getImageRegistry();

	/** The WHIT e_ color. */
	private static final Color WHITE_COLOR = Display.getDefault().getSystemColor(SWT.COLOR_WHITE);
	/** text of pdr identification number of person. */
	private Text _pdrID;

	/** The _facade. */
	private Facade _facade = Facade.getInstanz();

	/** The id service. */
	private IPdrIdService _idService = _facade.getIdService();
	/** local copy of current Person. */
	private Person _currentPerson;

	/** The creator name text. */
	private Text _creatorNameText;

	/** The creation time text. */
	private Text _creationTimeText;

	/** The revisor name text. */
	private Text _revisorNameText;

	/** The revision time text. */
	private Text _revisionTimeText;

	/** Label of which the tooltip shows the revision history. */
	private Label _historyLabel;

	/** text of concurring person ids. */
	private Text _conID1;

	/** The main tab folder. */
	private TabFolder _mainTabFolder;

	/** The front tab item. */
	private TabItem _frontTabItem;

	/** The identifier tab item. */
	private TabItem _identifierTabItem;

	/** The rights tab item. */
	private TabItem _rightsTabItem;
	/** composite for rights TabItem. */
	private Composite _rightsTableComposite;

	/** The front composite. */
	private Composite _frontComposite;
	/** composite for external person identifier. */
	private Composite _identifierComposite;
	/** composite for concurrences. */
	private Composite _conComposite;
	/** composite for rights management. */
	private Composite _rightsComposite;
	/** scroll composite for concurrences. */
	private ScrolledComposite _scrollCompCon;
	/** scroll composite for identifiers. */
	private ScrolledComposite _scrollCompIdentifier;

	/** The concurrence group. */
	private Group _concurrenceGroup;

	/** quality rating for external person identifier and concurrcence. */
	private final String[] _ratings = new String[]
	{"certain", "probable", "unsure"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

	/** buttons for rights settings. */
	private Button _rightsORCheckbox;

	/** The rights ow checkbox. */
	private Button _rightsOWCheckbox;

	/** The rights wgr checkbox. */
	private Button _rightsWGRCheckbox;

	/** The rights wgw checkbox. */
	private Button _rightsWGWCheckbox;

	/** The rights pgr checkbox. */
	private Button _rightsPGRCheckbox;

	/** The rights pgw checkbox. */
	private Button _rightsPGWCheckbox;

	/** The rights ar checkbox. */
	private Button _rightsARCheckbox;

	/** The rights aw checkbox. */
	private Button _rightsAWCheckbox;

	/** The add identifier. */
	private Button _addIdentifier;

	/** The add concurrence. */
	private Button _addConcurrence;

	/** The save button. */
	private Button _saveButton;

	/** String for quality of external person identifier. */
	private GridData _gridData;

	/** The grid data2. */
	private GridData _gridData2;

	/** The may write. */
	private boolean _mayWrite;
	private String _configProvider;

	/**
	 * Instantiates a new person editor dialog.
	 * @param parentShell the parent shell
	 * @param currentPerson the current person
	 */
	public PersonEditorDialog(final Shell parentShell, final Person currentPerson)
	{
		super(parentShell);
		this._currentPerson = currentPerson;
	}

	@Override
	public final void create()
	{
		super.create();
		// Set the title
		setTitle(NLMessages.getString("Editor_person_editor_title")); //$NON-NLS-1$
		_facade.addObserver(this);
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
		loadValues();

	}

	@Override
	protected final void createButtonsForButtonBar(final Composite parent)
	{
		_gridData = new GridData();
		_gridData.verticalAlignment = GridData.CENTER;
		_gridData.horizontalSpan = 3;
		_gridData.grabExcessHorizontalSpace = true;
		_gridData.grabExcessVerticalSpace = true;
		_gridData.horizontalAlignment = SWT.CENTER;

		parent.setLayoutData(_gridData);
		// Create Add button
		// Own method as we need to overview the SelectionAdapter
		createOkButton(parent, OK, NLMessages.getString("Editor_save"), true); //$NON-NLS-1$
		// Add a SelectionListener

		// Create Cancel button
		Button cancelButton = createButton(parent, CANCEL, NLMessages.getString("Editor_cancel"), false); //$NON-NLS-1$
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

	// /** meth. creates the TabItem for concurrence fields.
	// *
	// * @param _mainTabFolder main tab folder.
	// */
	// private void createConcurrenceTabItem(final TabFolder _mainTabFolder)
	// {
	// conTabItem = new TabItem(_mainTabFolder, SWT.NONE);
	// conTabItem.setText(NLMessages.getString("Editor_concurrences"));
	//
	// _conComposite = new Composite(_mainTabFolder, SWT.NONE);
	// _conComposite.setLayout(new GridLayout());
	//
	// conTabItem.setControl(_conComposite);
	//
	//
	// Button _addConcurrence = new Button(_conComposite, SWT.PUSH);
	// _addConcurrence.setText(NLMessages.getString("Editor_addConcurrence"));
	// _addConcurrence.setImage(_imageReg.get(IconsInternal.CONCURRENCE_ADD));
	// _addConcurrence.setLayoutData(_gridData);
	// _addConcurrence.addSelectionListener(new SelectionAdapter()
	// {
	// public void widgetSelected(final SelectionEvent event)
	// {
	// loadConcurrences(1, 0, 0);
	// } });
	//
	//
	//
	// }

	@Override
	protected final Control createDialogArea(final Composite parent)
	{

		_configProvider = Platform
				.getPreferencesService()
				.getString(CommonActivator.PLUGIN_ID, "PRIMARY_SEMANTIC_PROVIDER", AEConstants.CLASSIFICATION_AUTHORITY,
						null).toUpperCase();
		String standard = "PDR";
		if (!_facade.getConfigs().containsKey(standard))
		{
			for (String s : _facade.getConfigs().keySet())
			{
				standard = s;
				break;
			}
		}
		if (!_facade.getConfigs().containsKey(_configProvider))
		{
			_configProvider = standard;
		}
		GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.horizontalAlignment = GridData.FILL;
		gridData.horizontalSpan = 1;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;

		_gridData2 = new GridData();
		_gridData2.verticalAlignment = GridData.FILL;
		_gridData2.horizontalSpan = 4;
		_gridData2.grabExcessHorizontalSpace = true;
		_gridData2.grabExcessVerticalSpace = true;
		_gridData2.horizontalAlignment = SWT.FILL;
		//
		// _gridData.widthHint = 450;
		gridData.minimumWidth = 700;
		gridData.minimumHeight = 500;
		_mainTabFolder = new TabFolder(parent, SWT.TOP | SWT.FILL);
		_mainTabFolder.setLayoutData(gridData);
		_mainTabFolder.addSelectionListener(new SelectionListener()
		{
			@Override
			public void widgetDefaultSelected(final SelectionEvent e)
			{
				widgetSelected(e);
			}

			@Override
			public void widgetSelected(final SelectionEvent e)
			{
				if (_identifierTabItem != null)
				{
					validate();
				}
			}
		});

		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		layout.makeColumnsEqualWidth = true;
		parent.setLayout(layout);

		createFrontTabItem(_mainTabFolder);
		createIdentifierTabItem(_mainTabFolder);
		// createConcurrenceTabItem(_mainTabFolder);

		if (Platform.getPreferencesService().getBoolean(CommonActivator.PLUGIN_ID, "RIGHTS_GENERAL",
				AEConstants.RIGHTS_GENERAL, null))
		{
			createRightsTabItem(_mainTabFolder);
		}

		parent.pack();
		_mainTabFolder.layout();
		_mainTabFolder.pack();
		return parent;
	}

	/**
	 * meth. create the TabItem for front eg. general fields and context.
	 * @param mainTabFolder main tabFolder
	 */
	private void createFrontTabItem(final TabFolder mainTabFolder)
	{

		_frontTabItem = new TabItem(mainTabFolder, SWT.NONE);
		_frontTabItem.setText(NLMessages.getString("Editor_concurrences")); //$NON-NLS-1$
		_frontComposite = new Composite(mainTabFolder, SWT.NONE);
		_frontComposite.setLayoutData(new GridData());
		((GridData) _frontComposite.getLayoutData()).verticalAlignment = SWT.FILL;
		((GridData) _frontComposite.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) _frontComposite.getLayoutData()).horizontalAlignment = SWT.FILL;

		_frontComposite.setLayout(new GridLayout());
		_frontTabItem.setControl(_frontComposite);

		// GridData _gridData2 = new GridData();
		// _gridData2.verticalAlignment = GridData.FILL;
		// _gridData2.horizontalSpan = 4;
		// _gridData2.grabExcessHorizontalSpace = true;
		// _gridData2.grabExcessVerticalSpace = true;
		// _gridData2.horizontalAlignment = SWT.FILL;

		Group pdrIdGroup = new Group(_frontComposite, SWT.SHADOW_IN);
		pdrIdGroup.setText(NLMessages.getString("Editor_adminData")); //$NON-NLS-1$

		pdrIdGroup.setLayoutData(new GridData());
		((GridData) pdrIdGroup.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) pdrIdGroup.getLayoutData()).minimumHeight = 50;
		((GridData) pdrIdGroup.getLayoutData()).grabExcessHorizontalSpace = true;

		pdrIdGroup.setLayout(new GridLayout());
		((GridLayout) pdrIdGroup.getLayout()).numColumns = 10;
		((GridLayout) pdrIdGroup.getLayout()).makeColumnsEqualWidth = true;

		Label pdrLabel = new Label(pdrIdGroup, SWT.NONE);
		pdrLabel.setText(NLMessages.getString("Editor_PDRid")); //$NON-NLS-1$
		pdrLabel.setLayoutData(new GridData());
		((GridData) pdrLabel.getLayoutData()).horizontalSpan = 3;
		_pdrID = new Text(pdrIdGroup, SWT.NONE | SWT.READ_ONLY);
		_pdrID.setText("                                            "); //$NON-NLS-1$
		_pdrID.setLayoutData(new GridData());
		((GridData) _pdrID.getLayoutData()).horizontalSpan = 3;
		((GridData) pdrIdGroup.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) pdrIdGroup.getLayoutData()).grabExcessHorizontalSpace = true;
		_pdrID.pack();

		Label blancL = new Label(pdrIdGroup, SWT.NONE);
		blancL.setLayoutData(new GridData());
		((GridData) blancL.getLayoutData()).horizontalSpan = 3;
		_historyLabel = new Label(pdrIdGroup, SWT.SHADOW_IN);
		_historyLabel.setText(NLMessages.getString("Editor_revision_history")); //$NON-NLS-1$
		_historyLabel.setLayoutData(new GridData());

		Label creatorLabel = new Label(pdrIdGroup, SWT.NONE);
		creatorLabel.setText(NLMessages.getString("Editor_creator")); //$NON-NLS-1$
		creatorLabel.setLayoutData(new GridData());
		((GridData) creatorLabel.getLayoutData()).horizontalSpan = 3;

		_creatorNameText = new Text(pdrIdGroup, SWT.NONE | SWT.READ_ONLY);
		_creatorNameText.setText("                                            "); //$NON-NLS-1$
		_creatorNameText.setLayoutData(new GridData());
		((GridData) _creatorNameText.getLayoutData()).horizontalSpan = 3;

		Label creationTime = new Label(pdrIdGroup, SWT.NONE);
		creationTime.setText(NLMessages.getString("Editor_date")); //$NON-NLS-1$
		creationTime.setLayoutData(new GridData());
		_creationTimeText = new Text(pdrIdGroup, SWT.NONE | SWT.READ_ONLY);
		_creationTimeText.setText("                                            "); //$NON-NLS-1$
		_creationTimeText.setLayoutData(new GridData());
		((GridData) _creationTimeText.getLayoutData()).horizontalSpan = 3;

		Label revisorLabel = new Label(pdrIdGroup, SWT.NONE);
		revisorLabel.setText(NLMessages.getString("Editor_lastChangedBy")); //$NON-NLS-1$
		revisorLabel.setLayoutData(new GridData());
		((GridData) revisorLabel.getLayoutData()).horizontalSpan = 3;

		_revisorNameText = new Text(pdrIdGroup, SWT.NONE | SWT.READ_ONLY);
		_revisorNameText.setLayoutData(new GridData());
		((GridData) _revisorNameText.getLayoutData()).horizontalSpan = 3;
		_revisorNameText.setText("                                            "); //$NON-NLS-1$

		Label revisionTime = new Label(pdrIdGroup, SWT.NONE);
		revisionTime.setText(NLMessages.getString("Editor_date")); //$NON-NLS-1$
		revisionTime.setLayoutData(new GridData());
		_revisionTimeText = new Text(pdrIdGroup, SWT.NONE | SWT.READ_ONLY);
		_revisionTimeText.setLayoutData(new GridData());
		((GridData) _revisionTimeText.getLayoutData()).horizontalSpan = 3;
		_revisionTimeText.setText("                                            "); //$NON-NLS-1$
		pdrIdGroup.pack();
		pdrIdGroup.layout();

		Label reteste = new Label(pdrIdGroup, SWT.NONE);
		reteste.setText(NLMessages.getString("Editor_date")); //$NON-NLS-1$
		reteste.setLayoutData(new GridData());
		reteste.pack();
		_conComposite = new Composite(_frontComposite, SWT.NONE);
		_conComposite.setLayout(new GridLayout());
		_conComposite.setLayoutData(new GridData());
		((GridData) _conComposite.getLayoutData()).verticalAlignment = SWT.FILL;
		((GridData) _conComposite.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) _conComposite.getLayoutData()).horizontalAlignment = SWT.FILL;

		_addConcurrence = new Button(_conComposite, SWT.PUSH);
		_addConcurrence.setText(NLMessages.getString("Editor_addConcurrence")); //$NON-NLS-1$
		_addConcurrence.setToolTipText(NLMessages.getString("Editor_add_concurrence_tooltip"));
		_addConcurrence.setImage(_imageReg.get(IconsInternal.CONCURRENCE_ADD));
		_addConcurrence.setLayoutData(_gridData);
		_addConcurrence.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent event)
			{
				loadConcurrences(1, 0, 0);
				validate();
			}
		});

		_addConcurrence.pack();
		_conComposite.pack();
		_conComposite.layout();
		_frontComposite.pack();
		_frontComposite.layout();
	}

	/**
	 * meth. creates the TabItem for identifier fields such as PND, LCCN.
	 * @param mainTabFolder main tab folder.
	 */

	private void createIdentifierTabItem(final TabFolder mainTabFolder)
	{
		_identifierTabItem = new TabItem(mainTabFolder, SWT.NONE);
		_identifierTabItem.setText(NLMessages.getString("Editor_identifiers")); //$NON-NLS-1$
		_identifierComposite = new Composite(mainTabFolder, SWT.NONE);
		_identifierComposite.setLayout(new GridLayout());
		_identifierComposite.setLayoutData(new GridData());
		((GridData) _identifierComposite.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _identifierComposite.getLayoutData()).verticalAlignment = SWT.FILL;
		((GridData) _identifierComposite.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) _identifierComposite.getLayoutData()).grabExcessVerticalSpace = true;
		_identifierTabItem.setControl(_identifierComposite);

		_addIdentifier = new Button(_identifierComposite, SWT.PUSH);
		_addIdentifier.setText(NLMessages.getString("Editor_addIdentifier")); //$NON-NLS-1$
		_addIdentifier.setToolTipText(NLMessages.getString("Editor_add_identifier_tooltip"));
		_addIdentifier.setImage(_imageReg.get(IconsInternal.IDENTIFIER_ADD));
		_addIdentifier.setLayoutData(_gridData);
		_addIdentifier.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent event)
			{
				loadIdentifiers(true, null);
				// _identifierComposite.redraw();
				// _identifierComposite.pack();
				validate();

			}
		});

		_identifierComposite.pack();
	}

	/**
	 * Creates the ok button.
	 * @param parent the parent
	 * @param id the id
	 * @param label the label
	 * @param defaultButton the default button
	 * @return the button
	 */
	protected final Button createOkButton(final Composite parent, final int id, final String label,
			final boolean defaultButton)
	{
		// increment the number of columns in the button bar
		((GridLayout) parent.getLayout()).numColumns++;
		_saveButton = new Button(parent, SWT.PUSH);
		_saveButton.setText(label);
		_saveButton.setFont(JFaceResources.getDialogFont());
		_saveButton.setData(new Integer(id));
		_saveButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent event)
			{
				if (isValidInput())
				{
					okPressed();
				}
			}
		});
		if (defaultButton)
		{
			Shell shell = parent.getShell();
			if (shell != null)
			{
				shell.setDefaultButton(_saveButton);
			}
		}
		setButtonLayoutData(_saveButton);
		return _saveButton;
	}

	/**
	 * if general rights is activated tabitem for rights management of aspect is
	 * created.
	 * @param mainTabFolder main tabFolder
	 */
	private void createRightsTabItem(final TabFolder mainTabFolder)
	{
		_rightsTabItem = new TabItem(_mainTabFolder, SWT.NONE);
		_rightsTabItem.setText(""); //$NON-NLS-1$
		_rightsComposite = new Composite(_mainTabFolder, SWT.NONE);
		_rightsComposite.setLayout(new GridLayout());
		_rightsComposite.setLayoutData(new GridLayout());
		_rightsTabItem.setControl(_rightsComposite);

		Group rightsGroup = new Group(_rightsComposite, SWT.SHADOW_IN);
		rightsGroup.setLayout(new GridLayout());
		rightsGroup.setLayoutData(new GridData());
		((GridLayout) rightsGroup.getLayout()).numColumns = 3;
		((GridLayout) rightsGroup.getLayout()).makeColumnsEqualWidth = false;
		((GridData) rightsGroup.getLayoutData()).horizontalAlignment = GridData.FILL;
		((GridData) rightsGroup.getLayoutData()).verticalAlignment = SWT.FILL;
		((GridData) rightsGroup.getLayoutData()).grabExcessHorizontalSpace = true;

		((GridData) rightsGroup.getLayoutData()).minimumHeight = 60;
		((GridData) rightsGroup.getLayoutData()).widthHint = 600;

		_rightsTableComposite = new Composite(rightsGroup, SWT.NONE);
		_rightsTableComposite.setLayout(new GridLayout());
		((GridLayout) _rightsTableComposite.getLayout()).makeColumnsEqualWidth = false;
		((GridLayout) _rightsTableComposite.getLayout()).numColumns = 3;
		_rightsTableComposite.setLayoutData(new GridData());
		((GridData) _rightsTableComposite.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _rightsTableComposite.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) _rightsTableComposite.getLayoutData()).heightHint = 200;
		((GridData) _rightsTableComposite.getLayoutData()).grabExcessVerticalSpace = false;

		Label userRightsTitel = new Label(_rightsTableComposite, SWT.NONE);
		userRightsTitel.setText(""); //$NON-NLS-1$
		userRightsTitel.setLayoutData(new GridData());
		((GridData) userRightsTitel.getLayoutData()).horizontalSpan = 3;

		Label userRLabel = new Label(_rightsTableComposite, SWT.NONE);
		userRLabel.setText(""); //$NON-NLS-1$
		userRLabel.setLayoutData(new GridData());

		Label userReadLabel = new Label(_rightsTableComposite, SWT.NONE);
		userReadLabel.setText(""); //$NON-NLS-1$
		userReadLabel.setLayoutData(new GridData());

		Label userWriteLabel = new Label(_rightsTableComposite, SWT.NONE);
		userWriteLabel.setText(""); //$NON-NLS-1$
		userWriteLabel.setLayoutData(new GridData());

		Label userOwnerLabel = new Label(_rightsTableComposite, SWT.NONE);
		userOwnerLabel.setText(""); //$NON-NLS-1$
		userOwnerLabel.setLayoutData(new GridData());

		_rightsORCheckbox = new Button(_rightsTableComposite, SWT.CHECK);
		_rightsORCheckbox.setSelection(true);
		_rightsORCheckbox.setLayoutData(new GridData());
		_rightsORCheckbox.setEnabled(false);

		_rightsOWCheckbox = new Button(_rightsTableComposite, SWT.CHECK);
		_rightsOWCheckbox.setSelection(true);
		_rightsOWCheckbox.setLayoutData(new GridData());
		_rightsOWCheckbox.setEnabled(false);

		Label userWGroupLabel = new Label(_rightsTableComposite, SWT.NONE);
		userWGroupLabel.setText(""); //$NON-NLS-1$
		userWGroupLabel.setLayoutData(new GridData());

		_rightsWGRCheckbox = new Button(_rightsTableComposite, SWT.CHECK);
		_rightsWGRCheckbox.setSelection(true);
		_rightsWGRCheckbox.setLayoutData(new GridData());
		_rightsWGRCheckbox.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent e)
			{
				// workgroup_read = !workgroup_read;
			}
		});

		_rightsWGWCheckbox = new Button(_rightsTableComposite, SWT.CHECK);
		_rightsWGWCheckbox.setSelection(true);
		_rightsWGWCheckbox.setLayoutData(new GridData());
		_rightsWGWCheckbox.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent e)
			{
				// workgroup_write = !workgroup_write;
			}
		});

		Label userPGroupLabel = new Label(_rightsTableComposite, SWT.NONE);
		userPGroupLabel.setText(""); //$NON-NLS-1$
		userPGroupLabel.setLayoutData(new GridData());

		_rightsPGRCheckbox = new Button(_rightsTableComposite, SWT.CHECK);
		_rightsPGRCheckbox.setSelection(true);
		_rightsPGRCheckbox.setLayoutData(new GridData());
		_rightsPGRCheckbox.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent e)
			{
				// projectgroup_read = !projectgroup_read;
			}
		});

		_rightsPGWCheckbox = new Button(_rightsTableComposite, SWT.CHECK);
		_rightsPGWCheckbox.setSelection(true);
		_rightsPGWCheckbox.setLayoutData(new GridData());
		_rightsPGWCheckbox.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent e)
			{
				// projectgroup_write = !projectgroup_write;
			}
		});

		Label userAllLabel = new Label(_rightsTableComposite, SWT.NONE);
		userAllLabel.setText(""); //$NON-NLS-1$
		userAllLabel.setLayoutData(new GridData());

		_rightsARCheckbox = new Button(_rightsTableComposite, SWT.CHECK);
		_rightsARCheckbox.setSelection(true);
		_rightsARCheckbox.setLayoutData(new GridData());
		_rightsARCheckbox.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent e)
			{
				// all_read = !all_read;
			}
		});

		_rightsAWCheckbox = new Button(_rightsTableComposite, SWT.CHECK);
		_rightsAWCheckbox.setSelection(false);
		_rightsAWCheckbox.setLayoutData(new GridData());
		_rightsAWCheckbox.setEnabled(false);

		_rightsTableComposite.pack();
	}

	@Override
	protected final boolean isResizable()
	{
		return true;
	}

	/**
	 * checks whether input is correct. conditions:
	 * @return boolean valid
	 */
	private boolean isValidInput()
	{
		// Validator v = new Validator();
		// int error = v.isValid(_currentPerson);
		// if(error == 2000)
		// {
		// return true;
		// }
		// else if (error == 2210 || error == 2220 || error == 2230)
		// {
		// setMessage(NLMessages.getString("Editor_error2210"),
		// IMessageProvider.ERROR);
		// return false;
		// }
		// else if (error == 2310 || error == 2320 || error == 2350 || error ==
		// 2350)
		// {
		// setMessage(NLMessages.getString("Editor_error2310"),
		// IMessageProvider.ERROR);
		// return false;
		// }
		// else
		// {
		// setMessage(NLMessages.getString("Editor_error2400"),
		// IMessageProvider.ERROR);
		// return false;
		// }
		return true;
	}

	/**
	 * Load concurrences.
	 * @param type the type
	 * @param con the con
	 * @param ref the ref
	 */
	private void loadConcurrences(final int type, final Integer con, final Integer ref)
	{
		if (_scrollCompCon != null)
		{
			_scrollCompCon.dispose();
		}
		if (_concurrenceGroup != null)
		{
			_concurrenceGroup.dispose();
		}
		_scrollCompCon = new ScrolledComposite(_frontComposite, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		_scrollCompCon.setExpandHorizontal(true);
		_scrollCompCon.setExpandVertical(true);

		_scrollCompCon.setLayout(new GridLayout());
		_scrollCompCon.setLayoutData(new GridData());
		((GridData) _scrollCompCon.getLayoutData()).heightHint = 280;
		((GridData) _scrollCompCon.getLayoutData()).widthHint = 600;
		// ((GridData) _scrollCompCon.getLayoutData()).verticalAlignment =
		// SWT.FILL;
		((GridData) _scrollCompCon.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _scrollCompCon.getLayoutData()).grabExcessHorizontalSpace = true;
		// ((GridData) _scrollCompCon.getLayoutData()).grabExcessVerticalSpace =
		// true;
		_scrollCompCon.setMinHeight(1);
		_scrollCompCon.setMinWidth(1);

		Composite contentCompCon = new Composite(_scrollCompCon, SWT.NONE);
		contentCompCon.setLayout(new GridLayout());
		// contentCompCon.setSize(530, 550);

		switch (type)
		{
			case 0:
				break; // nix, normales laden
			case 1: // neue concurrence einfügen
				if (_currentPerson.getConcurrences() == null)
				{
					_currentPerson.setConcurrences(new Concurrences());
					_currentPerson.getConcurrences().setConcurrences(new Vector<Concurrence>());
				}
				Concurrence concurrence = new Concurrence();
				_currentPerson.getConcurrences().getConcurrences().add(concurrence);
				ValidationStm vs = new ValidationStm();
				vs.setAuthority(_facade.getCurrentUser().getPdrId());
				vs.setReference(new Reference());
				vs.getReference().setAuthority(_facade.getCurrentUser().getPdrId());
				concurrence.getReferences().add(vs);
				break;
			case 2: // concurrence löschen
				_currentPerson.getConcurrences().remove(con);
				break;
			case 3: // neue Reference einfügen
				ValidationStm validationStm = new ValidationStm();
				validationStm.setReference(new Reference());
				_currentPerson.getConcurrences().getConcurrences().get(con).getReferences().add(validationStm);
				break;
			case 4: // Reference löschen
				_currentPerson.getConcurrences().getConcurrences().get(con).getReferences().removeElementAt(ref);
				break;
			default:
				break;
		}

		for (int i = 0; i < _currentPerson.getConcurrences().getConcurrences().size(); i++)
		{
			int m = i + 1;
			final Concurrence concurrence = _currentPerson.getConcurrences().getConcurrences().get(i);
			_concurrenceGroup = new Group(contentCompCon, SWT.SHADOW_IN);
			_concurrenceGroup.setText(NLMessages.getString("Editor_concurrence") + m); //$NON-NLS-1$
			_concurrenceGroup.setLayoutData(new GridData());
			((GridData) _concurrenceGroup.getLayoutData()).grabExcessHorizontalSpace = true;
			((GridData) _concurrenceGroup.getLayoutData()).horizontalAlignment = SWT.FILL;
			GridLayout layout2 = new GridLayout();
			layout2.numColumns = 6;
			layout2.makeColumnsEqualWidth = false;
			_concurrenceGroup.setLayout(layout2);
			_concurrenceGroup.setData("con", i); //$NON-NLS-1$

			Label conLabel = new Label(_concurrenceGroup, SWT.NONE);
			conLabel.setText(NLMessages.getString("Editor_space_withPerson")); //$NON-NLS-1$
			conLabel.setLayoutData(new GridData());

			final Text conID = new Text(_concurrenceGroup, SWT.BORDER);
			conID.setEditable(_mayWrite);
			conID.setBackground(WHITE_COLOR);
			conID.setLayoutData(new GridData());
			((GridData) conID.getLayoutData()).horizontalAlignment = SWT.FILL;
			((GridData) conID.getLayoutData()).grabExcessHorizontalSpace = true;
			((GridData) conID.getLayoutData()).horizontalSpan = 2;
			((GridData) conID.getLayoutData()).horizontalIndent = 8;

			final ControlDecoration decoConId = new ControlDecoration(conID, SWT.LEFT | SWT.TOP);
			ControlDecoration decoConIdInfo = new ControlDecoration(conID, SWT.LEFT | SWT.BOTTOM);
			decoConIdInfo.setDescriptionText(NLMessages.getString("Editor_concurrence_deco"));
			decoConIdInfo.setImage(FieldDecorationRegistry.getDefault()
					.getFieldDecoration(FieldDecorationRegistry.DEC_INFORMATION).getImage());
			decoConIdInfo.setShowOnlyOnFocus(false);

			if (concurrence.getPersonId() != null)
			{
				Person p = _facade.getPerson(concurrence.getPersonId());

				if (p != null)
				{
					decoConId.setImage(null);
					if (p != null)
					{
						conID.setText(p.getDisplayNameWithID());
					}
				}
				else
				{
					conID.setText(concurrence.getPersonId().toString());
				}
			}
			else
			{
				conID.setText(""); //$NON-NLS-1$
				decoConId.setImage(FieldDecorationRegistry.getDefault()
						.getFieldDecoration(FieldDecorationRegistry.DEC_REQUIRED).getImage());
			}

			conID.addFocusListener(new FocusListener()
			{
				@Override
				public void focusGained(final FocusEvent e)
				{
					char[] autoActivationCharacters = new char[]
					{'.', '#'};
					KeyStroke keyStroke;

					try
					{
						keyStroke = KeyStroke.getInstance("Ctrl+Space"); //$NON-NLS-1$

						ContentProposalAdapter adapter = new ContentProposalAdapter(conID, new TextContentAdapter(),
								new FacetContentProposalProvider(_facade.getAllPersonsFacets()), keyStroke,
								autoActivationCharacters);
						adapter.setLabelProvider(new AutoCompleteNameLabelProvider());
						adapter.addContentProposalListener(new IContentProposalListener()
						{
							@Override
							public void proposalAccepted(final IContentProposal proposal)
							{
								conID.setText(proposal.getContent());
								if (((Facet) proposal).getKey() != null)
								{
									concurrence.setPersonId(new PdrId(((Facet) proposal).getKey()));
									decoConId.setImage(null);
								}
								validate();
							}
						});

					}
					catch (org.eclipse.jface.bindings.keys.ParseException e1)
					{

						e1.printStackTrace();
					}

				}

				@Override
				public void focusLost(final FocusEvent e)
				{
					if (conID.getText() != null
							&& (_facade.getPdrObject(new PdrId(conID.getText())) != null || concurrence.getPersonId() != null))
					{
						decoConId.setDescriptionText(""); //$NON-NLS-1$
						decoConId.setImage(null);
					}
					else
					{
						concurrence.setPersonId(null);
						decoConId.setImage(FieldDecorationRegistry.getDefault()
								.getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).getImage());
						decoConId.setDescriptionText(NLMessages.getString("Editor_missing_pdrObject"));
					}
					validate();

				}
			});

			conID.addKeyListener(new KeyListener()
			{

				@Override
				public void keyPressed(final KeyEvent e)
				{
				}

				@Override
				public void keyReleased(final KeyEvent e)
				{
					if (conID.getText().length() == 23)
					{
						PdrObject o = _facade.getPdrObject(new PdrId(conID.getText()));
						if (o != null)
						{
							decoConId.setImage(null);
							concurrence.setPersonId(new PdrId(conID.getText()));
							conID.setText(o.getDisplayNameWithID());
						}
					}
					else if (conID.getText().trim().length() == 0)
					{
						concurrence.setPersonId(null);
						decoConId.setImage(FieldDecorationRegistry.getDefault()
								.getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).getImage());
					}
					// if (conID.getText().length() == 23)
					// {
					// concurrence.setPersonId(new PdrId(conID.getText()));
					// if (concurrence.isValidId())
					// {
					// decoConId.setImage(null);
					// Person p = _facade.getPerson(concurrence.getPersonId());
					// if (p != null) conID.setText(p.getDisplayNameWithID());
					// }
					// else
					// {
					// decoConId.setImage(FieldDecorationRegistry.getDefault().getFieldDecoration(FieldDecorationRegistry.DEC_REQUIRED).getImage());
					// }
					// }
					// else if (conID.getText().length() == 0)
					// {
					// concurrence.setPersonId(null);
					// decoConId.setImage(FieldDecorationRegistry.getDefault().getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).getImage());
					// }

					validate();
				}
			});

			Button setConcurrence = new Button(_concurrenceGroup, SWT.PUSH);
			setConcurrence.setText(NLMessages.getString("Editor_select_dots")); //$NON-NLS-1$
			setConcurrence.setToolTipText(NLMessages.getString("Editor_open_selectObjectDialog_concurrence"));
			setConcurrence.setImage(_imageReg.get(IconsInternal.SEARCH));
			setConcurrence.setEnabled(_mayWrite);
			setConcurrence.setLayoutData(new GridData());
			setConcurrence.addSelectionListener(new SelectionAdapter()
			{
				@Override
				public void widgetSelected(final SelectionEvent event)
				{
					IWorkbench workbench = PlatformUI.getWorkbench();
					Display display = workbench.getDisplay();
					Shell shell = new Shell(display);
					SelectObjectDialog dialog = new SelectObjectDialog(shell, 1);
					dialog.open();
					if (_facade.getRequestedId() != null)
					{
						concurrence.setPersonId(_facade.getRequestedId()); //$NON-NLS-1$
						decoConId.setImage(null);
						PdrObject o = _facade.getPerson(concurrence.getPersonId());
						if (o != null)
						{
							conID.setText(o.getDisplayNameWithID());
						}
					}
					validate();

				}
			});

			final Button delConcurrence = new Button(_concurrenceGroup, SWT.PUSH);
			delConcurrence.setToolTipText(NLMessages.getString("Editor_delete")); //$NON-NLS-1$
			delConcurrence.setImage(_imageReg.get(IconsInternal.CONCURRENCE_REMOVE));
			delConcurrence.setLayoutData(new GridData());
			delConcurrence.setData("num", i); //$NON-NLS-1$
			delConcurrence.setEnabled(_mayWrite);
			delConcurrence.addSelectionListener(new SelectionAdapter()
			{
				@Override
				public void widgetSelected(final SelectionEvent event)
				{
					//					System.out.println("del concurrence " + (Integer) delConcurrence.getData("num")); //$NON-NLS-1$ //$NON-NLS-2$
					loadConcurrences(2, (Integer) delConcurrence.getData("num"), null); //$NON-NLS-1$
					validate();

				}
			});
			delConcurrence.setLayoutData(new GridData());

			final Button addReference = new Button(_concurrenceGroup, SWT.PUSH);
			addReference.setToolTipText(NLMessages.getString("Editor_addReference")); //$NON-NLS-1$
			addReference.setImage(_imageReg.get(IconsInternal.REFERENCE_ADD));
			addReference.setLayoutData(new GridData());
			addReference.setData("num", i); //$NON-NLS-1$
			addReference.setEnabled(_mayWrite);
			addReference.addSelectionListener(new SelectionAdapter()
			{
				@Override
				public void widgetSelected(final SelectionEvent event)
				{
					//					System.out.println("add reference " + (Integer) addReference.getData("num")); //$NON-NLS-1$ //$NON-NLS-2$
					loadConcurrences(3, (Integer) addReference.getData("num"), null); //$NON-NLS-1$
					validate();

				}
			});
			addReference.setLayoutData(new GridData());

			if (concurrence.getReferences() != null)
			{
				for (int j = 0; j < concurrence.getReferences().size(); j++)

				{

					final ValidationStm validationStm = concurrence.getReferences().get(j);
					if (validationStm.getReference() != null)
					{
						Composite refComposite = new Composite(_concurrenceGroup, SWT.NONE);
						refComposite.setLayoutData(new GridData());
						((GridData) refComposite.getLayoutData()).horizontalAlignment = SWT.FILL;
						((GridData) refComposite.getLayoutData()).grabExcessHorizontalSpace = true;
						((GridData) refComposite.getLayoutData()).horizontalSpan = 6;

						refComposite.setLayout(new GridLayout(9, false));
						// ((GridLayout) refComposite.getLayout()).numColumns =
						// 9;
						refComposite.setData("con", i); //$NON-NLS-1$
						refComposite.setData("ref", j); //$NON-NLS-1$

						Label conRef = new Label(refComposite, SWT.NONE);
						conRef.setText(NLMessages.getString("Editor_reference")); //$NON-NLS-1$
						conRef.setLayoutData(new GridData());

						final Text conRefID = new Text(refComposite, SWT.BORDER);
						conRefID.setData("con", i); //$NON-NLS-1$
						conRefID.setData("ref", j); //$NON-NLS-1$
						conRefID.setEditable(_mayWrite);
						conRefID.setBackground(WHITE_COLOR);
						conRefID.setLayoutData(new GridData());
						((GridData) conRefID.getLayoutData()).horizontalAlignment = SWT.FILL;
						((GridData) conRefID.getLayoutData()).grabExcessHorizontalSpace = true;
						((GridData) conRefID.getLayoutData()).horizontalSpan = 6;
						final ControlDecoration decoConRefId = new ControlDecoration(conRefID, SWT.LEFT | SWT.TOP);
						final ControlDecoration decoConRefIdInfo = new ControlDecoration(conRefID, SWT.LEFT
								| SWT.BOTTOM);
						decoConRefIdInfo.setDescriptionText(NLMessages.getString("Editor_reference_deco"));
						decoConRefIdInfo.setImage(FieldDecorationRegistry.getDefault()
								.getFieldDecoration(FieldDecorationRegistry.DEC_INFORMATION).getImage());
						decoConRefIdInfo.setShowOnlyOnFocus(false);

						if (validationStm.getReference().getSourceId() != null)
						{
							PdrObject o = _facade.getReference(validationStm.getReference().getSourceId());
							if (o != null)
							{
								conRefID.setText(o.getDisplayNameWithID());
							}
						}
						else
						{
							conRefID.setText(""); //$NON-NLS-1$
							decoConRefId.setImage(FieldDecorationRegistry.getDefault()
									.getFieldDecoration(FieldDecorationRegistry.DEC_REQUIRED).getImage());
						}

						conRefID.addFocusListener(new FocusListener()
						{

							@Override
							public void focusGained(final FocusEvent e)
							{
								char[] autoActivationCharacters = new char[]
								{'.', '#'};
								KeyStroke keyStroke;

								try
								{
									keyStroke = KeyStroke.getInstance("Ctrl+Space"); //$NON-NLS-1$
									ContentProposalAdapter adapter = new ContentProposalAdapter(conRefID,
											new TextContentAdapter(), new FacetContentProposalProvider(_facade
													.getAllReferenceFacets()), keyStroke, autoActivationCharacters);
									adapter.setLabelProvider(new AutoCompleteNameLabelProvider());
									adapter.addContentProposalListener(new IContentProposalListener()
									{

										@Override
										public void proposalAccepted(final IContentProposal proposal)
										{
											conRefID.setText(proposal.getContent());
											if (((Facet) proposal).getKey() != null)
											{
												validationStm.getReference().setSourceId(
														new PdrId(((Facet) proposal).getKey()));
												decoConRefId.setImage(null);
												validate();
											}
										}
									});
								}
								catch (org.eclipse.jface.bindings.keys.ParseException e1)
								{

									e1.printStackTrace();
								}

							}

							@Override
							public void focusLost(final FocusEvent e)
							{
								if (validationStm.getReference().getSourceId() != null
										&& _facade.getReference(validationStm.getReference().getSourceId()) != null)
								{
									decoConRefId.setDescriptionText(""); //$NON-NLS-1$
									decoConRefId.setImage(null);
								}
								else
								{
									validationStm.getReference().setSourceId(null);
									decoConRefId.setImage(FieldDecorationRegistry.getDefault()
											.getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).getImage());
									decoConRefId.setDescriptionText(NLMessages.getString("Editor_missing_pdrObject"));
								}
								validate();

							}
						});
						conRefID.addKeyListener(new KeyListener()
						{

							@Override
							public void keyPressed(final KeyEvent e)
							{
							}

							@Override
							public void keyReleased(final KeyEvent e)
							{
								if (conRefID.getText().length() == 23)
								{
									PdrObject o = _facade.getPdrObject(new PdrId(conRefID.getText()));
									if (o != null)
									{
										decoConRefId.setImage(null);
										validationStm.getReference().setSourceId(new PdrId(conRefID.getText()));
										conRefID.setText(o.getDisplayNameWithID());
									}
								}
								else if (conRefID.getText().trim().length() == 0)
								{
									validationStm.getReference().setSourceId(null);
								}

								validate();
							}
						});

						Button setReference = new Button(refComposite, SWT.PUSH);
						setReference.setText(NLMessages.getString("Editor_select_dots")); //$NON-NLS-1$
						setConcurrence.setToolTipText(NLMessages.getString("Editor_open_selectObjectDialog_reference"));
						setReference.setImage(_imageReg.get(IconsInternal.SEARCH));
						setReference.setEnabled(_mayWrite);
						setReference.setLayoutData(new GridData());
						setReference.addSelectionListener(new SelectionAdapter()
						{

							@Override
							public void widgetSelected(final SelectionEvent event)
							{
								IWorkbench workbench = PlatformUI.getWorkbench();
								Display display = workbench.getDisplay();
								Shell shell = new Shell(display);
								SelectObjectDialog dialog = new SelectObjectDialog(shell, 2);
								dialog.open();
								if (_facade.getRequestedId() != null)
								{
									conRefID.setText(_facade.getRequestedId().toString());
									validationStm.getReference().setSourceId(_facade.getRequestedId()); //$NON-NLS-1$
									if (validationStm.getReference().isValidId())
									{
										decoConRefId.setImage(null);
										decoConRefIdInfo
												.setImage(FieldDecorationRegistry.getDefault()
														.getFieldDecoration(FieldDecorationRegistry.DEC_INFORMATION)
														.getImage());
										ViewHelper.setDecoInfo(decoConRefIdInfo, validationStm.getReference()
												.getSourceId());
									}
								}
								else
								{
									decoConRefId.setImage(FieldDecorationRegistry.getDefault()
											.getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).getImage());
									decoConRefIdInfo.setImage(null);
								}
								validate();

							}
						});
						// Label bl = new Label (refComposite, SWT.NONE);
						// bl.setLayoutData(new GridData());
						// ((GridData) bl.getLayoutData()).horizontalSpan = 1;

						if (j > 0)
						{
							final Button delReference = new Button(refComposite, SWT.PUSH);
							delReference.setToolTipText(NLMessages.getString("Editor_deleteRef")); //$NON-NLS-1$
							delReference.setImage(_imageReg.get(IconsInternal.REFERENCE_REMOVE));
							delReference.setLayoutData(new GridData());
							delReference.setData("con", i); //$NON-NLS-1$
							delReference.setData("ref", j); //$NON-NLS-1$
							delReference.setEnabled(_mayWrite);
							delReference.addSelectionListener(new SelectionAdapter()
							{
								@Override
								public void widgetSelected(final SelectionEvent event)
								{
									loadConcurrences(
											4,
											(Integer) delReference.getData("con"), (Integer) delReference.getData("ref")); //$NON-NLS-1$ //$NON-NLS-2$
									validate();

								}
							});
							delReference.setLayoutData(new GridData());
						}
						else
						{
							((GridData) setReference.getLayoutData()).horizontalSpan = 2;
						}

						Label conInternalLabel = new Label(refComposite, SWT.NONE);
						conInternalLabel.setText(NLMessages.getString("Editor_internal")); //$NON-NLS-1$
						conInternalLabel.setLayoutData(new GridData());

						final Text conInternal = new Text(refComposite, SWT.BORDER);
						conInternal.setEditable(_mayWrite);
						conInternal.setBackground(WHITE_COLOR);
						if (validationStm.getReference().getInternal() != null)
						{
							conInternal.setText(validationStm.getReference().getInternal());
						}
						else
						{
							conInternal.setText(""); //$NON-NLS-1$
						}

						conInternal.setLayoutData(new GridData());
						((GridData) conInternal.getLayoutData()).horizontalAlignment = SWT.FILL;
						((GridData) conInternal.getLayoutData()).grabExcessHorizontalSpace = true;
						((GridData) conInternal.getLayoutData()).horizontalSpan = 8;
						conInternal.addFocusListener(new FocusAdapter()
						{
							@Override
							public void focusLost(final FocusEvent e)
							{
								validationStm.getReference().setInternal(conInternal.getText()); //$NON-NLS-1$
							}
						});

						Label conQuallabel = new Label(refComposite, SWT.NONE);
						conQuallabel.setText(NLMessages.getString("Editor_quality")); //$NON-NLS-1$
						conQuallabel.setLayoutData(new GridData());
						final ControlDecoration decoConQual = new ControlDecoration(conQuallabel, SWT.LEFT | SWT.TOP);

						SelectionListener conListener = new SelectionAdapter()
						{
							@Override
							public void widgetDefaultSelected(final SelectionEvent e)
							{
							}

							@Override
							public void widgetSelected(final SelectionEvent e)
							{
								final String qual = (String) ((Button) e.getSource()).getData("text");
								//			                 System.out.println("ref qual: " + qual); //$NON-NLS-1$
								validationStm.getReference().setQuality(qual); //$NON-NLS-1$
								if (validationStm.getReference().isValidQuality())
								{
									decoConQual.setImage(null);
								}
								else
								{
									decoConQual.setImage(FieldDecorationRegistry.getDefault()
											.getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).getImage());
								}
								validate();
							}
						};
						final Button[] conRadios = new Button[AEConstants.REFRENCEQUALITIES.length];

						for (int k = 0; k < AEConstants.REFRENCEQUALITIES.length; k++)
						{
							conRadios[k] = new Button(refComposite, SWT.RADIO);
							conRadios[k].setText(NLMessages.getString("Editor_" + AEConstants.REFRENCEQUALITIES[k]));
							conRadios[k].setData("text", AEConstants.REFRENCEQUALITIES[k]);

							conRadios[k].addSelectionListener(conListener);
							conRadios[k].setEnabled(_mayWrite);
							conRadios[k].setLayoutData(new GridData());
						}

						if (validationStm.getReference().getQuality() != null)
						{
							ViewHelper.setRadioByString(conRadios, validationStm.getReference().getQuality());
						}
						else
						{
							decoConQual.setImage(FieldDecorationRegistry.getDefault()
									.getFieldDecoration(FieldDecorationRegistry.DEC_REQUIRED).getImage());
						}

						Label blancLabel = new Label(refComposite, SWT.NONE);
						blancLabel.setLayoutData(new GridData());
						blancLabel.setText(""); //$NON-NLS-1$
						((GridData) blancLabel.getLayoutData()).horizontalSpan = 2;
						((GridData) blancLabel.getLayoutData()).horizontalAlignment = SWT.FILL;

						Label conAuthorityLabel = new Label(refComposite, SWT.NONE);
						conAuthorityLabel.setText(NLMessages.getString("Editor_authority")); //$NON-NLS-1$
						conAuthorityLabel.setLayoutData(new GridData());
						((GridData) conAuthorityLabel.getLayoutData()).horizontalAlignment = SWT.RIGHT;

						final Text conAuthority = new Text(refComposite, SWT.BORDER | SWT.READ_ONLY);
						conAuthority.setEditable(false);

						if (validationStm.getAuthority() != null)
						{
							User u = null;
							try
							{
								u = _facade.getUserManager().getUserById(validationStm.getAuthority().toString());
							}
							catch (Exception e1)
							{
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							if (u != null)
							{
								conAuthority.setText(u.getDisplayName());
							}
							else
							{
								conAuthority.setText(validationStm.getAuthority().toString());
							}
						}
						else
						{
							conAuthority.setText(_facade.getCurrentUser().getPdrId().toString());
							validationStm.setAuthority(_facade.getCurrentUser().getPdrId());
						}

						conAuthority.setLayoutData(new GridData());
						((GridData) conAuthority.getLayoutData()).horizontalAlignment = SWT.FILL;
						((GridData) conAuthority.getLayoutData()).horizontalSpan = 2;

						Label refCitLabel = new Label(refComposite, SWT.NONE);
						refCitLabel.setText(NLMessages.getString("Editor_interpretation")); //$NON-NLS-1$
						refCitLabel.setLayoutData(new GridData());
						((GridData) refCitLabel.getLayoutData()).horizontalSpan = 3;

						Label refauthority = new Label(refComposite, SWT.NONE);

						if (validationStm.getAuthority() != null)
						{
							User u = null;
							try
							{
								u = _facade.getUserManager().getUserById(validationStm.getAuthority().toString());
							}
							catch (Exception e1)
							{
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							if (u != null)
							{
								refauthority.setText(NLMessages.getString("Editor_user") + ": " + u.getDisplayName());
							}
							else
							{
								refauthority.setText(NLMessages.getString("Editor_user") + ": "
										+ validationStm.getAuthority().toString());
							}
						}
						else
						{
							refauthority.setText(NLMessages.getString("Editor_user")
 + ": "
									+ _facade.getCurrentUser().getPdrId().toString());
							validationStm.setAuthority(_facade.getCurrentUser().getPdrId());
						}

						refauthority.setLayoutData(new GridData());
						((GridData) refauthority.getLayoutData()).horizontalSpan = 3;

						final Text refCitation = new Text(refComposite, SWT.MULTI | SWT.BORDER | SWT.WRAP
								| SWT.V_SCROLL);
						refCitation.setEditable(_mayWrite);
						refCitation.setBackground(WHITE_COLOR);
						refCitation.setLayoutData(new GridData());
						((GridData) refCitation.getLayoutData()).horizontalAlignment = SWT.FILL;
						((GridData) refCitation.getLayoutData()).grabExcessHorizontalSpace = true;
						((GridData) refCitation.getLayoutData()).heightHint = 36;
						((GridData) refCitation.getLayoutData()).horizontalSpan = 9;

						if (validationStm.getInterpretation() != null)
						{
							refCitation.setText(validationStm.getInterpretation());
						}

						refCitation.addFocusListener(new FocusAdapter()
						{
							@Override
							public void focusLost(final FocusEvent e)
							{
								validationStm.setInterpretation(refCitation.getText());
							}
						});
						refComposite.layout();
						refComposite.pack();

					}
				}
				_concurrenceGroup.pack();
				_concurrenceGroup.layout();
			}

		}
		contentCompCon.layout();

		_scrollCompCon.setContent(contentCompCon);
		Point point = contentCompCon.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
		Point mp = _mainTabFolder.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
		if (point.x > mp.x - 20)
		{
			point.x = mp.x - 20;
		}
		_scrollCompCon.setMinSize(point);
		_scrollCompCon.layout();
		_conComposite.pack();
		_conComposite.layout();
		_frontComposite.redraw();
		_frontComposite.layout();

	}

	/**
	 * Load identifiers.
	 * @param add the add
	 * @param del the del
	 */
	private void loadIdentifiers(final boolean add, final Integer del)
	{
		if (_scrollCompIdentifier != null)
		{
			_scrollCompIdentifier.dispose();
		}
		_scrollCompIdentifier = new ScrolledComposite(_identifierComposite, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		_scrollCompIdentifier.setExpandHorizontal(true);
		_scrollCompIdentifier.setExpandVertical(true);
		_scrollCompIdentifier.setMinSize(SWT.DEFAULT, SWT.DEFAULT);
		_scrollCompIdentifier.setLayoutData(new GridData());
		((GridData) _scrollCompIdentifier.getLayoutData()).heightHint = 400;
		((GridData) _scrollCompIdentifier.getLayoutData()).widthHint = 580;

		((GridData) _scrollCompIdentifier.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _scrollCompIdentifier.getLayoutData()).grabExcessHorizontalSpace = true;
		_scrollCompIdentifier.setMinHeight(1);
		_scrollCompIdentifier.setMinWidth(1);

		_scrollCompIdentifier.setLayout(new GridLayout());

		Composite contentCompIdentifier = new Composite(_scrollCompIdentifier, SWT.NONE);
		contentCompIdentifier.setLayout(new GridLayout());
		_scrollCompIdentifier.setContent(contentCompIdentifier);

		if (add && _currentPerson.getIdentifiers() == null)
		{
			_currentPerson.setIdentifiers(new Identifiers());
			_currentPerson.getIdentifiers().setIdentifiers(new Vector<Identifier>());
			_currentPerson.getIdentifiers().getIdentifiers().add(new Identifier());
		}
		else if (add)
		{
			_currentPerson.getIdentifiers().getIdentifiers().add(new Identifier());

		}
		if (del != null)
		{
			//			System.out.println("old size " + _currentPerson.getIdentifiers().getIdentifiers().size()); //$NON-NLS-1$
			_currentPerson.getIdentifiers().remove(del);
		}

		for (int i = 0; i < _currentPerson.getIdentifiers().getIdentifiers().size(); i++)
		{
			int m = i + 1;
			final Identifier identifier = _currentPerson.getIdentifiers().getIdentifiers().get(i);
			final Group idGroup = new Group(contentCompIdentifier, SWT.SHADOW_IN);
			idGroup.setText(NLMessages.getString("Editor_externalIdentifiers") + m); //$NON-NLS-1$
			idGroup.setLayoutData(new GridData());
			idGroup.setLayout(new GridLayout());
			idGroup.setData("num", i); //$NON-NLS-1$
			((GridData) idGroup.getLayoutData()).horizontalAlignment = SWT.FILL;
			((GridData) idGroup.getLayoutData()).grabExcessHorizontalSpace = true;
			((GridLayout) idGroup.getLayout()).numColumns = 5;
			((GridLayout) idGroup.getLayout()).makeColumnsEqualWidth = false;

			// final Combo externalIdentifierCombo = new Combo(idGroup,
			// SWT.DROP_DOWN | SWT.READ_ONLY);
			// externalIdentifierCombo.setLayoutData(new GridData());
			// externalIdentifierCombo.setEnabled(_mayWrite);
			// externalIdentifierCombo.setBackground(WHITE_COLOR);
			// // ((GridData)
			// // externalIdentifierCombo.getLayoutData()).horizontalAlignment
			// // = SWT.FILL;
			// // ((GridData)
			// //
			// externalIdentifierCombo.getLayoutData()).grabExcessHorizontalSpace
			// // = true;
			// for (String sdt : AEConstants.EXTERNAL_IDENTIFIER_PROVIDER)
			// {
			// externalIdentifierCombo.add(sdt);
			// }

			final Combo idProviderCombo = new Combo(idGroup, SWT.DROP_DOWN | SWT.READ_ONLY);
			idProviderCombo.setEnabled(_mayWrite);
			idProviderCombo.setBackground(WHITE_COLOR);
			final ComboViewer idProviderComboViewer = new ComboViewer(idProviderCombo);
			idProviderComboViewer.setContentProvider(new MarkupContentProvider());
			idProviderComboViewer.setLabelProvider(new MarkupLabelProvider());

			idProviderCombo.setLayoutData(new GridData());
			((GridData) idProviderCombo.getLayoutData()).grabExcessHorizontalSpace = true;
			((GridData) idProviderCombo.getLayoutData()).horizontalAlignment = SWT.FILL;
			((GridData) idProviderCombo.getLayoutData()).horizontalIndent = 6;
			if (_facade.getConfigs().get(_configProvider) != null
					&& _facade.getConfigs().get(_configProvider).getUsage() != null
					&& !_facade.getConfigs().get(_configProvider).getUsage().getIdentifiers().getChildren().isEmpty())
			{
				ConfigData cd = _facade.getConfigs().get(_configProvider).getUsage().getIdentifiers();
				idProviderComboViewer.setInput(cd.getChildren());

			}
			idProviderComboViewer.addSelectionChangedListener(new ISelectionChangedListener()
			{
				@Override
				public void selectionChanged(final SelectionChangedEvent event)
				{
					ISelection iSelection = event.getSelection();
					Object obj = ((IStructuredSelection) iSelection).getFirstElement();
					IAEPresentable cp = (IAEPresentable) obj;
					if (cp != null)
					{
						identifier.setProvider(cp.getValue());
						validate();
					}

				}

			});

			if (identifier.getProvider() != null)
			{
				ViewHelper.setComboViewerByString(idProviderComboViewer, identifier.getProvider(), true);
			}
			else if (idProviderCombo.getItemCount() > 0)
			{
				idProviderComboViewer.setSelection(new StructuredSelection(idProviderComboViewer.getElementAt(0)));
			}


			final Text idText = new Text(idGroup, SWT.BORDER);
			idText.setLayoutData(new GridData());
			idText.setEditable(_mayWrite);
			idText.setBackground(WHITE_COLOR);
			((GridData) idText.getLayoutData()).horizontalSpan = 3;
			((GridData) idText.getLayoutData()).horizontalAlignment = SWT.FILL;
			((GridData) idText.getLayoutData()).grabExcessHorizontalSpace = true;
			((GridData) idText.getLayoutData()).horizontalIndent = 8;

			final ControlDecoration decoIdent = new ControlDecoration(idText, SWT.LEFT | SWT.TOP);
			if (identifier.getIdentifier() != null)
			{
				idText.setText(identifier.getIdentifier()); //$NON-NLS-1$
			}
			else
			{
				idText.setText(""); //$NON-NLS-1$
				decoIdent.setImage(FieldDecorationRegistry.getDefault()
						.getFieldDecoration(FieldDecorationRegistry.DEC_REQUIRED).getImage());
			}

			idText.addFocusListener(new FocusAdapter()
			{
				@Override
				public void focusLost(final FocusEvent e)
				{
					identifier.setIdentifier(idText.getText()); //$NON-NLS-1$
					if (identifier.isValidId())
					{
						decoIdent.setImage(null);
					}
					else
					{
						decoIdent.setImage(FieldDecorationRegistry.getDefault()
								.getFieldDecoration(FieldDecorationRegistry.DEC_REQUIRED).getImage());
					}
					validate();
				}
			});
			idText.addKeyListener(new KeyListener()
			{

				@Override
				public void keyPressed(final KeyEvent e)
				{
				}

				@Override
				public void keyReleased(final KeyEvent e)
				{
					//					System.out.println("key released"); //$NON-NLS-1$
					identifier.setIdentifier(idText.getText()); //$NON-NLS-1$
					if (identifier.isValidId())
					{
						decoIdent.setImage(null);
					}
					else
					{
						decoIdent.setImage(FieldDecorationRegistry.getDefault()
								.getFieldDecoration(FieldDecorationRegistry.DEC_REQUIRED).getImage());
					}
					validate();
				}
			});

			final Button showData = new Button(idGroup, SWT.PUSH);
			showData.setText(NLMessages.getString("Editor_showData")); //$NON-NLS-1$
			showData.setToolTipText(NLMessages.getString("Editor_showData_tooltip"));
			showData.setImage(_imageReg.get(IconsInternal.BROWSER));
			showData.setLayoutData(new GridData());
			showData.setData("num", i); //$NON-NLS-1$
			showData.addSelectionListener(new SelectionAdapter()
			{
				@Override
				public void widgetSelected(final SelectionEvent event)
				{

					IHandlerService handlerService = (IHandlerService) PlatformUI.getWorkbench().getService(
							IHandlerService.class);
					_facade.setRequestedIdentifierType(identifier.getProvider()); //$NON-NLS-1$
					_facade.setRequestedIdentifier(identifier.getIdentifier()); //$NON-NLS-1$
					try
					{
						handlerService.executeCommand("org.bbaw.pdr.ae.view.identifiers.commands" + //$NON-NLS-1$
								".OpenBrowserDialog", null); //$NON-NLS-1$
					}
					catch (ExecutionException e)
					{
						e.printStackTrace();
					}
					catch (NotDefinedException e)
					{
						e.printStackTrace();
					}
					catch (NotEnabledException e)
					{
						e.printStackTrace();
					}
					catch (NotHandledException e)
					{
						e.printStackTrace();
					}
				}
			});

			Label qualityLabel = new Label(idGroup, SWT.NONE);
			qualityLabel.setText(""); //$NON-NLS-1$
			qualityLabel.setLayoutData(new GridData());
			final ControlDecoration decoIdentQual = new ControlDecoration(qualityLabel, SWT.LEFT | SWT.TOP);
			// final String qual = "";
			SelectionListener idListener = new SelectionAdapter()
			{
				@Override
				public void widgetDefaultSelected(final SelectionEvent e)
				{

					validate();

				}

				@Override
				public void widgetSelected(final SelectionEvent e)
				{
					final String qual = (String) ((Button) e.getSource()).getData("text");
					//                 System.out.println("pnd qual: " + pndQual); //$NON-NLS-1$
					identifier.setQuality(qual); //$NON-NLS-1$
					if (identifier.isValidQuality())
					{
						decoIdentQual.setImage(null);
					}
					else
					{
						decoIdentQual.setImage(FieldDecorationRegistry.getDefault()
								.getFieldDecoration(FieldDecorationRegistry.DEC_REQUIRED).getImage());
					}
					validate();
				}
			};

			final Button[] radios = new Button[_ratings.length];

			for (int j = 0; j < _ratings.length; j++)
			{
				radios[j] = new Button(idGroup, SWT.RADIO);
				radios[j].setText(NLMessages.getString("Editor_" + _ratings[j]));
				radios[j].setData("text", _ratings[j]);
				radios[j].setEnabled(_mayWrite);
				radios[j].setLayoutData(new GridData());
				radios[j].addSelectionListener(idListener);
			}

			if (identifier.getQuality() != null)
			{
				ViewHelper.setRadioByString(radios, identifier.getQuality());
			}
			else
			{
				decoIdentQual.setImage(FieldDecorationRegistry.getDefault()
						.getFieldDecoration(FieldDecorationRegistry.DEC_REQUIRED).getImage());

			}
			Label b = new Label(idGroup, SWT.NONE);
			b.setText(""); //$NON-NLS-1$
			b.setLayoutData(new GridData());
			Label idAuthorityLabel = new Label(idGroup, SWT.NONE);
			idAuthorityLabel.setText(NLMessages.getString("Editor_createdBy")); //$NON-NLS-1$
			idAuthorityLabel.setLayoutData(new GridData());

			final Text idAuthorityText = new Text(idGroup, SWT.BORDER | SWT.READ_ONLY);
			idAuthorityText.setData("num", i); //$NON-NLS-1$

			if (identifier.getAuthority() != null)
			{
				User u = null;
				try
				{
					u = _facade.getUserManager().getUserById(identifier.getAuthority().toString());
				}
				catch (Exception e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if (u != null)
				{
					idAuthorityText.setText(u.getDisplayName());
				}
				else
				{
					idAuthorityText.setText(identifier.getAuthority().toString());
				}
			}
			else
			{
				idAuthorityText.setText(_facade.getCurrentUser().getPdrId().toString());
				identifier.setAuthority(_facade.getCurrentUser().getPdrId());
			}

			idAuthorityText.setLayoutData(new GridData());
			((GridData) idAuthorityText.getLayoutData()).horizontalSpan = 3;
			((GridData) idAuthorityText.getLayoutData()).horizontalAlignment = SWT.FILL;
			((GridData) idAuthorityText.getLayoutData()).grabExcessHorizontalSpace = true;
			((GridData) idAuthorityText.getLayoutData()).horizontalIndent = 8;
			final Button delIdentifier = new Button(idGroup, SWT.PUSH);
			delIdentifier.setText(NLMessages.getString("Editor_delete")); //$NON-NLS-1$
			delIdentifier.setToolTipText(NLMessages.getString("Editor_remove_identifier_tooltip"));
			delIdentifier.setImage(_imageReg.get(IconsInternal.IDENTIFIER_REMOVE));
			delIdentifier.setLayoutData(new GridData());
			delIdentifier.setData("num", i); //$NON-NLS-1$
			delIdentifier.setEnabled(_mayWrite);

			delIdentifier.addSelectionListener(new SelectionAdapter()
			{
				@Override
				public void widgetSelected(final SelectionEvent event)
				{
					//					System.out.println("del identifier " + (Integer) delIdentifier.getData("num")); //$NON-NLS-1$ //$NON-NLS-2$
					loadIdentifiers(false, (Integer) delIdentifier.getData("num")); //$NON-NLS-1$
					validate();

				}
			});
			idGroup.layout();
			idGroup.pack();
		} // idGroup
		contentCompIdentifier.redraw();
		contentCompIdentifier.layout();

		_scrollCompIdentifier.setContent(contentCompIdentifier);
		Point point = contentCompIdentifier.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
		Point mp = _mainTabFolder.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
		if (point.x > mp.x - 20)
		{
			point.x = mp.x - 20;
		}
		_scrollCompIdentifier.setMinSize(point);
		_scrollCompIdentifier.layout();
		_identifierComposite.redraw();
		_identifierComposite.layout();

		// _scrollCompIdentifier.setSize(DIALOG_DEFAULT_BOUNDS,
		// DIALOG_DEFAULT_BOUNDS);
		_identifierComposite.update();
	}

	/**
	 * Load relation p.
	 * @param selectedPerson the selected person
	 */
	private void loadRelationP(final Person selectedPerson)
	{
		_conID1.setText(selectedPerson.getPdrId().toString());
	}

	/**
	 * loads values into fields.
	 * @throws XQException
	 */
	private void loadValues()
	{
		Revision revision = new Revision();
		revision.setRevisor(new String(_facade.getCurrentUser().getDisplayName()));
		revision.setTimeStamp(_facade.getCurrentDate());
		revision.setAuthority(_facade.getCurrentUser().getPdrId().clone());
		if (_currentPerson.isNew())
		{
			revision.setRef(0);
			Record record = new Record();
			record.getRevisions().add(revision);
			_currentPerson.setRecord(record);
		}

		_mayWrite = new UserRichtsChecker().mayWrite(_currentPerson);
		_addConcurrence.setEnabled(_mayWrite);
		_addIdentifier.setEnabled(_mayWrite);
		if (!_mayWrite)
		{
			setMessage(NLMessages.getString("Editor_message_noWriting_person"));
		}
		if (_currentPerson != null)
		{

			//			System.out.println("im person editor person not null"); //$NON-NLS-1$
			// Front
			if (_currentPerson.getPdrId() != null)
			{
				_pdrID.setText(_currentPerson.getPdrId().toString());
				// _pdrID.pack();
			}

			if ((_currentPerson.getRecord() != null) && !_currentPerson.getRecord().getRevisions().isEmpty())
			{
				if (_currentPerson.getRecord().getRevisions().get(0).getRevisor() != null)
				{
					_creatorNameText.setText(_currentPerson.getRecord().getRevisions().get(0).getRevisor());
				}
				else
				{
					_creatorNameText.setText(_facade.getObjectDisplayName(_currentPerson.getRecord().getRevisions()
							.get(0).getAuthority()));
				}
				_creationTimeText.setText(_adminDateFormat.format(_currentPerson.getRecord().getRevisions().get(0)
						.getTimeStamp()));
				// _creatorNameText.pack();
				// _creationTimeText.pack();

				if (_currentPerson.getRecord().getRevisions().size() > 1)
				{
					if (_currentPerson.getRecord().getRevisions().lastElement().getAuthority() != null)
					{

						_creatorNameText.setText(_facade.getObjectDisplayName(_currentPerson.getRecord().getRevisions()
								.lastElement().getAuthority()));

					}
					else
					{
						_revisorNameText.setText(NLMessages.getString("Editor_revisor_name_notFound"));
					}
					_revisionTimeText.setText(_adminDateFormat.format(_currentPerson.getRecord().getRevisions()
							.lastElement().getTimeStamp()));
					// revisorName.pack();
					// _revisionTimeText.pack();

					// String ttHist = NLMessages.getString("Editor_createdBy")
					//					+ _currentPerson.getRecord().getRevisions().firstElement().getAuthority().toString() //$NON-NLS-1$
					// + NLMessages.getString("Editor_date")
					// + adminDateFormat.format(_currentPerson.getRecord()
					//					.getRevisions().firstElement().getTimeStamp()) + "\n"; //$NON-NLS-1$ //$NON-NLS-2$
					// for (int i = 1; i <
					// _currentPerson.getRecord().getRevisions().size(); i++)
					// {
					// if
					// (_currentPerson.getRecord().getRevisions().get(i).getAuthority()
					// != null
					// &&
					// _currentPerson.getRecord().getRevisions().get(i).getTimeStamp()
					// != null)
					// {
					//			   				ttHist = ttHist + i + NLMessages.getString("Editor_revisionedBy"); //$NON-NLS-1$
					// String id =
					// _currentPerson.getRecord().getRevisions().get(i).getAuthority().toString();
					// User user = null;
					// try {
					// user = userManager.getUserById(id);
					// } catch (XQException e) {
					// id =
					// NLMessages.getString("Editor_user_name_notFound + id");
					// e.printStackTrace();
					// }
					// if (user != null) ttHist += user.getDisplayName();
					// else ttHist += id;
					// ttHist += NLMessages
					// .getString("Editor_space_date_space") + adminDateFormat
					//					.format(_currentPerson.getRecord().getRevisions().get(i).getTimeStamp()) + "\n"; //$NON-NLS-1$ //$NON-NLS-2$
					// }
					// }
					final RevisionHistoryToolTip historyToolTip = new RevisionHistoryToolTip(_historyLabel,
							_currentPerson.getRecord());
					historyToolTip.setShift(new Point(-25, -25));
					historyToolTip.setPopupDelay(0);
					historyToolTip.setHideOnMouseDown(true);
					historyToolTip.activate();
					_historyLabel.addMouseListener(new MouseListener()
					{

						@Override
						public void mouseDoubleClick(final MouseEvent e)
						{
						}

						@Override
						public void mouseDown(final MouseEvent e)
						{
							historyToolTip.show(new Point(e.x, e.y));
						}

						@Override
						public void mouseUp(final MouseEvent e)
						{
							historyToolTip.show(new Point(e.x, e.y));
						}
					});
					// _historyLabel.setToolTipText(ttHist);

				}
			}
			else
			{
				_creatorNameText.setText(_facade.getCurrentUser().getDisplayName());
				_creationTimeText.setText(_adminDateFormat.format(_facade.getCurrentDate()));
				_currentPerson.setNew(true);

			}
			// _creationTimeText.setText(cp.getRecord().getRevisions().get(0).getTimeStamp());

			// Identifier
			if (_currentPerson.getIdentifiers() != null)
			{
				loadIdentifiers(false, null);
			}

			// Concurrence
			if (_currentPerson.getConcurrences() != null)
			{
				loadConcurrences(0, null, null);

			}

			// new RightsChecker();
			// if(!rc._mayWrite(_currentPerson)){
			// if(false){
			//
			// pndID.setEnabled(false);
			//
			// for(int i=0;i<3;i++){
			// pndRadios[i].setEnabled(false);
			// }
			// lccnID.setEnabled(false);
			// for(int i=0;i<3;i++){
			// lccnRadios[i].setEnabled(false);
			// }
			// iccuID.setEnabled(false);
			// for(int i=0;i<3;i++){
			// iccuRadios[i].setEnabled(false);
			// }
			// viafID.setEnabled(false);
			// for(int i=0;i<3;i++){
			// viafRadios[i].setEnabled(false);
			// }
			// _conComposite.setEnabled(false);
			// _rightsComposite.setEnabled(false);
			// setMessage(NLMessages.getString("Editor_errorMessageNoWritingRights"),
			// IMessageProvider.INFORMATION);
			// }else{
			// _identifierComposite.setEnabled(true);
			// _conComposite.setEnabled(true);
			// _rightsComposite.setEnabled(true);
			// }

		}
		else
		{ // if currentPerson = null, create new person.
			_creatorNameText.setText(_facade.getCurrentUser().getDisplayName()); //$NON-NLS-1$
			_creationTimeText.setText(_facade.getCurrentDateAsString());
			setMessage("", IMessageProvider.INFORMATION); //$NON-NLS-1$

		}

		validate();

	}

	@Override
	protected final void okPressed()
	{
		saveInput();
		super.okPressed();
	}

	// We need to have the textFields into Strings because the UI gets disposed
	// and the Text Fields are not accessible any more.
	/**
	 * Save input.
	 */
	private void saveInput()
	{
		Revision revision = new Revision();
		revision.setRevisor(new String(_facade.getCurrentUser().getDisplayName()));
		revision.setTimeStamp(_facade.getCurrentDate());
		revision.setAuthority(_facade.getCurrentUser().getPdrId().clone());
		boolean isModifiedOrNew = false;
		try
		{
			isModifiedOrNew = _idService.isModifiedOrNewObject(_currentPerson.getPdrId());
		}
		catch (Exception e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (!_currentPerson.isNew() && !isModifiedOrNew)
		{
			revision.setRef(_currentPerson.getRecord().getRevisions().size());
			_currentPerson.getRecord().getRevisions().add(revision);
			_currentPerson.setDirty(true);
		}
		else
		{
			_currentPerson.getRecord().getRevisions().lastElement().setTimeStamp(_facade.getCurrentDate());
			_currentPerson.setDirty(true);
		}

		_currentPerson.setDirty(true);

		// hier wird injestet
		try
		{
			_facade.savePerson(_currentPerson);
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// hier wird die zuvor geclonte person für die person mit der gleichen
		// id im allperson
		// vector eingetauscht und außerdem als currentPerson gesetzt.

	}





	// //////////////////////////// Observer ////////////////////////////////

	@Override
	public final void update(final Observable o, final Object arg)
	{
		if (arg.equals("newSelectedPerson")) //$NON-NLS-1$
		{
			if (_facade.getSelectedPerson() != null)
			{
				loadRelationP(_facade.getSelectedPerson());
			}
		}
	}

	/**
	 * Validate.
	 */
	private void validate()
	{
		boolean valid = true;

		if (_currentPerson.getConcurrences() != null && _currentPerson.getConcurrences().isValid())
		{
			_frontTabItem.setImage(_imageReg.get(IconsInternal.CONCURRENCE));

		}
		else if (_currentPerson.getConcurrences() != null && !_currentPerson.getConcurrences().isValid())
		{
			_frontTabItem.setImage(_imageReg.get(IconsInternal.CONCURRENCE_ERROR));
			valid = false;
		}
		else
		{
			_frontTabItem.setImage(_imageReg.get(IconsInternal.CONCURRENCE));
		}
		if (_currentPerson.getIdentifiers() != null && _currentPerson.getIdentifiers().isValid())
		{
			_identifierTabItem.setImage(_imageReg.get(IconsInternal.IDENTIFIER));

		}
		else if (_currentPerson.getIdentifiers() != null && !_currentPerson.getIdentifiers().isValid())
		{
			_identifierTabItem.setImage(_imageReg.get(IconsInternal.IDENTIFIER_ERROR));
			valid = false;
		}
		else
		{
			_identifierTabItem.setImage(_imageReg.get(IconsInternal.IDENTIFIER));
		}
		_saveButton.setEnabled(valid && _mayWrite);
	}


}
