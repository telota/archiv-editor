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
package org.bbaw.pdr.ae.view.editorlite.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import org.bbaw.pdr.ae.common.AEConstants;
import org.bbaw.pdr.ae.common.AEVIEWConstants;
import org.bbaw.pdr.ae.common.CommonActivator;
import org.bbaw.pdr.ae.common.NLMessages;
import org.bbaw.pdr.ae.common.icons.IconsInternal;
import org.bbaw.pdr.ae.config.model.ComplexSemanticTemplate;
import org.bbaw.pdr.ae.config.model.ConfigData;
import org.bbaw.pdr.ae.config.model.ConfigItem;
import org.bbaw.pdr.ae.config.model.SemanticTemplate;
import org.bbaw.pdr.ae.control.core.PDRObjectBuilder;
import org.bbaw.pdr.ae.control.core.UserRichtsChecker;
import org.bbaw.pdr.ae.control.facade.Facade;
import org.bbaw.pdr.ae.control.facade.RightsChecker;
import org.bbaw.pdr.ae.control.interfaces.IDBManager;
import org.bbaw.pdr.ae.control.interfaces.IPdrIdService;
import org.bbaw.pdr.ae.metamodel.IAEPresentable;
import org.bbaw.pdr.ae.metamodel.PdrId;
import org.bbaw.pdr.ae.metamodel.Revision;
import org.bbaw.pdr.ae.model.Aspect;
import org.bbaw.pdr.ae.model.Concurrence;
import org.bbaw.pdr.ae.model.Concurrences;
import org.bbaw.pdr.ae.model.Identifier;
import org.bbaw.pdr.ae.model.Identifiers;
import org.bbaw.pdr.ae.model.PdrObject;
import org.bbaw.pdr.ae.model.Person;
import org.bbaw.pdr.ae.model.Reference;
import org.bbaw.pdr.ae.model.SemanticStm;
import org.bbaw.pdr.ae.model.User;
import org.bbaw.pdr.ae.model.ValidationStm;
import org.bbaw.pdr.ae.model.view.Facet;
import org.bbaw.pdr.ae.model.view.OrderingHead;
import org.bbaw.pdr.ae.view.control.ControlExtensions;
import org.bbaw.pdr.ae.view.control.PDRObjectsProvider;
import org.bbaw.pdr.ae.view.control.PDROrdererFactory;
import org.bbaw.pdr.ae.view.control.ViewHelper;
import org.bbaw.pdr.ae.view.control.customSWTWidges.MarkupTooltip;
import org.bbaw.pdr.ae.view.control.dialogs.SelectObjectDialog;
import org.bbaw.pdr.ae.view.control.interfaces.IAEAspectSemanticEditorTemplateController;
import org.bbaw.pdr.ae.view.control.interfaces.IAEBasicEditor;
import org.bbaw.pdr.ae.view.control.interfaces.IComplexAspectTemplateEditor;
import org.bbaw.pdr.ae.view.control.interfaces.IEasyAspectEditor;
import org.bbaw.pdr.ae.view.control.provider.AutoCompleteNameLabelProvider;
import org.bbaw.pdr.ae.view.control.provider.FacetContentProposalProvider;
import org.bbaw.pdr.ae.view.control.provider.MarkupContentProvider;
import org.bbaw.pdr.ae.view.control.provider.MarkupLabelProvider;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.MessageDialog;
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
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
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
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.progress.UIJob;

public class PersonAspectEditor extends TitleAreaDialog implements IAEBasicEditor
{

	private CTabFolder _tabFolder;

	private CTabItem _selectedTabItem;

	/** Instance of shared image registry. */
	private ImageRegistry _imageReg = CommonActivator.getDefault().getImageRegistry();
	private Person _currentPerson;

	private Aspect _editingAspect;

	private int _editingAspectTabItem;

	private int _editingAspectIndex;

	private Vector<Aspect> _currentAspects;

	private Vector<Aspect> _dirtyAspects = new Vector<Aspect>(5);

	private Aspect _selectedAspect;

	private IEasyAspectEditor _selectedAspectEditor;

	private Facade _facade = Facade.getInstanz();

	private PDRObjectBuilder _pdrObjectBuilder = new PDRObjectBuilder();

	private String _configProvider;

	/** instance of pdrIdService. */
	private IPdrIdService _idService = _facade.getIdService();

	/** The pdr objects _provider. */
	private PDRObjectsProvider _pdrObjectsProvider = new PDRObjectsProvider();

	/** The _orderer factory. */
	private PDROrdererFactory _ordererFactory = new PDROrdererFactory();

	private SelectionListener _selectionListener;

	private PaintListener _paintListener;

	private int _numberOfNoWritingAspects = 0;

	/** The WHIT e_ color. */
	private static final Color WHITE_COLOR = Display.getDefault().getSystemColor(SWT.COLOR_WHITE);

	private IAEAspectSemanticEditorTemplateController _aspectTemplateController = ControlExtensions
			.getAspectSemanticTemplateController();

	private Vector<IComplexAspectTemplateEditor> _complexAspectEditors = new Vector<IComplexAspectTemplateEditor>();

	private ConfigData _aspectTemplates;

	private CTabItem _concurrenceTI;

	private CTabItem _identifierTabItem;

	private ScrolledComposite _scrollCompCon;

	private Group _concurrenceGroup;

	private boolean _mayWritePerson;

	/** quality rating for external person identifier and concurrcence. */
	private final String[] _ratings = new String[]
	{"certain", "probable", "unsure"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

	private ScrolledComposite _scrollCompIdentifier;

	private Shell _parentShell;

	private RightsChecker _rightsChecker = new RightsChecker();

	private Button _saveButton;

	private Vector<Aspect> _invalidAspects = new Vector<Aspect>();

	/**
	 * Create the dialog.
	 * @param parentShell
	 * @param p
	 */
	public PersonAspectEditor(Shell parentShell, Person p, Aspect aspectToEdit)
	{
		super(parentShell);
		this._parentShell = parentShell;
		_currentPerson = p;
		_editingAspect = aspectToEdit;
		_mayWritePerson = new RightsChecker().mayWrite(p);
		
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
		if (_configProvider == null && !_facade.getConfigs().containsKey(_configProvider))
		{
			_configProvider = standard;
		}
		

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
				try
				{
					reloadEditedObjects();
				}
				catch (Exception e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				setReturnCode(CANCEL);
				close();
			}

		});
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent)
	{
		parent.addListener(SWT.Traverse, new Listener()
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

		setTitle(_currentPerson.getDisplayNameWithID());

//		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		container.setLayout(new GridLayout(1, false));
		_tabFolder = new CTabFolder(container, SWT.BORDER);
		ViewHelper.setTabfolderSimple(_tabFolder, false);
		_tabFolder.setLayoutData(new GridData());
		((GridData) _tabFolder.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _tabFolder.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) _tabFolder.getLayoutData()).verticalAlignment = SWT.FILL;
		((GridData) _tabFolder.getLayoutData()).grabExcessVerticalSpace = true;
		// _tabFolder.setBounds(0, 0, 845, 455);
		_tabFolder.setSelectionBackground(Display.getCurrent().getSystemColor(
				SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));

		_tabFolder.addSelectionListener(new SelectionListener()
		{

			@Override
			public void widgetDefaultSelected(SelectionEvent e)
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void widgetSelected(SelectionEvent e)
			{
				_selectedTabItem = _tabFolder.getSelection();
				boolean loaded = (Boolean) _selectedTabItem.getData("loaded");
				if (!loaded)
				{
					loadAspects(_selectedTabItem, 0, 10);
					_selectedTabItem.setData("loaded", true);
				}
				enableAspectButtons(_selectedTabItem, true);
				if (System.getProperty("os.name").toLowerCase().contains("mac") && _selectedTabItem.getData("complex") != null && _selectedTabItem.getData("complex").equals(true))
				 {
					Object c = _selectedTabItem.getData("complexEditor");
					if (c instanceof IComplexAspectTemplateEditor)
					{

						((IComplexAspectTemplateEditor) c).doExtraLayoutRefresh();
					}	
					
					
					
					

				 }
				
			
				
			

			}

		});


		// createPersonTabItem();
		createListener();

		if (_aspectTemplateController != null
				&& _aspectTemplateController.getComplexAspectTemplateEditorLabels() != null)
		{
			createComplexTemplateTabs();
		}
		loadValues();
		loadPersonValues();
		if (_aspectTemplateController != null && _complexAspectEditors != null)
		{
			// for (IComplexAspectTemplateEditor ed : _complexAspectEditors)
			// {
			// ed.createDefaultInput();
			//
			// }
		}
		final MarkupTooltip tfTooltipLabel = new MarkupTooltip(_tabFolder, NLMessages.getString("EditorLite_tipp"));
		tfTooltipLabel.setPopupDelay(2);
		tfTooltipLabel.setHideOnMouseDown(true);
		tfTooltipLabel.activate();
		ViewHelper.equipeTabFolderToolTip(_tabFolder, tfTooltipLabel);
		ViewHelper.equipWithMouseExitListener(_tabFolder, tfTooltipLabel);
		_tabFolder.redraw();
		_tabFolder.layout();
		_tabFolder.update();
		
		return container;
	}

	private void loadPersonValues()
	{
		createConcurrencesTabItem(_tabFolder);
		createIdentifierTabItem(_tabFolder);
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
		_identifierTabItem.setData("loaded", true);
		_concurrenceTI.setData("loaded", true);

	}

	/**
	 * meth. create the TabItem for front eg. general fields and context.
	 * @param mainTabFolder main tabFolder
	 */
	private void createConcurrencesTabItem(final CTabFolder mainTabFolder)
	{

		_concurrenceTI = new CTabItem(mainTabFolder, SWT.NONE);
		_concurrenceTI.setText(NLMessages.getString("Editor_concurrences")); //$NON-NLS-1$
		_concurrenceTI.setImage(_imageReg.get(IconsInternal.CONCURRENCE));
		_concurrenceTI.setData("img", IconsInternal.CONCURRENCE);

		Composite concurrenceComposite = new Composite(mainTabFolder, SWT.NONE);
		concurrenceComposite.setLayoutData(new GridData());
		((GridData) concurrenceComposite.getLayoutData()).verticalAlignment = SWT.FILL;
		((GridData) concurrenceComposite.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) concurrenceComposite.getLayoutData()).horizontalAlignment = SWT.FILL;

		concurrenceComposite.setLayout(new GridLayout());
		_concurrenceTI.setControl(concurrenceComposite);

		Button addConcurrence = new Button(concurrenceComposite, SWT.PUSH);
		addConcurrence.setText(NLMessages.getString("Editor_addConcurrence")); //$NON-NLS-1$
		addConcurrence.setToolTipText(NLMessages.getString("Editor_add_concurrence_tooltip"));
		addConcurrence.setImage(_imageReg.get(IconsInternal.CONCURRENCE_ADD));
		addConcurrence.setLayoutData(new GridData());
		addConcurrence.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent event)
			{
				loadConcurrences(1, 0, 0);
				validate();
			}

		});

		addConcurrence.pack();
		concurrenceComposite.pack();
		concurrenceComposite.layout();
	}

	/**
	 * meth. creates the TabItem for identifier fields such as PND, LCCN.
	 * @param mainTabFolder main tab folder.
	 */

	private void createIdentifierTabItem(final CTabFolder mainTabFolder)
	{
		_identifierTabItem = new CTabItem(mainTabFolder, SWT.NONE);
		_identifierTabItem.setText(NLMessages.getString("Editor_identifiers")); //$NON-NLS-1$
		_identifierTabItem.setImage(_imageReg.get(IconsInternal.IDENTIFIER));
		_identifierTabItem.setData("img", IconsInternal.IDENTIFIER);
		Composite identifierComposite = new Composite(mainTabFolder, SWT.NONE);
		identifierComposite.setLayout(new GridLayout());
		identifierComposite.setLayoutData(new GridData());
		((GridData) identifierComposite.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) identifierComposite.getLayoutData()).verticalAlignment = SWT.FILL;
		((GridData) identifierComposite.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) identifierComposite.getLayoutData()).grabExcessVerticalSpace = true;
		_identifierTabItem.setControl(identifierComposite);

		Button addIdentifier = new Button(identifierComposite, SWT.PUSH);
		addIdentifier.setText(NLMessages.getString("Editor_addIdentifier")); //$NON-NLS-1$
		addIdentifier.setToolTipText(NLMessages.getString("Editor_add_identifier_tooltip"));
		addIdentifier.setImage(_imageReg.get(IconsInternal.IDENTIFIER_ADD));
		addIdentifier.setLayoutData(new GridData());
		addIdentifier.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent event)
			{
				loadIdentifiers(true, null);
				validate();

			}
		});

		identifierComposite.pack();
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
		_scrollCompIdentifier = new ScrolledComposite((Composite) _identifierTabItem.getControl(), SWT.BORDER
				| SWT.V_SCROLL | SWT.H_SCROLL);
		_identifierTabItem.setData("sc", _scrollCompIdentifier);

		_scrollCompIdentifier.setExpandHorizontal(true);
		_scrollCompIdentifier.setExpandVertical(true);
		_scrollCompIdentifier.setMinSize(SWT.DEFAULT, SWT.DEFAULT);
		_scrollCompIdentifier.setLayoutData(new GridData());
		((GridData) _scrollCompIdentifier.getLayoutData()).heightHint = 380;
		((GridData) _scrollCompIdentifier.getLayoutData()).widthHint = 630;

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

			final Combo idProviderCombo = new Combo(idGroup, SWT.DROP_DOWN | SWT.READ_ONLY);
			idProviderCombo.setEnabled(_mayWritePerson);
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
			idText.setEditable(_mayWritePerson);
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
				radios[j].setEnabled(_mayWritePerson);
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
			delIdentifier.setEnabled(_mayWritePerson);

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
		Point mp = _tabFolder.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
		if (point.x > mp.x - 20)
		{
			point.x = mp.x - 20;
		}
		if (point.y > mp.y - 20)
		{
			point.y = mp.y - 20;
		}
		_scrollCompIdentifier.setMinSize(point);
		_scrollCompIdentifier.layout();
		_identifierTabItem.getControl().redraw();
		((Composite) _identifierTabItem.getControl()).layout();
		_identifierTabItem.getControl().update();
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
		_scrollCompCon = new ScrolledComposite((Composite) _concurrenceTI.getControl(), SWT.BORDER | SWT.V_SCROLL
				| SWT.H_SCROLL);
		_scrollCompCon.setExpandHorizontal(true);
		_scrollCompCon.setExpandVertical(true);
		_concurrenceTI.setData("sc", _scrollCompCon);
		_scrollCompCon.setLayout(new GridLayout());
		_scrollCompCon.setLayoutData(new GridData());
		((GridData) _scrollCompCon.getLayoutData()).heightHint = 380;
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
			conID.setEditable(_mayWritePerson);
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

					ContentProposalAdapter adapter = new ContentProposalAdapter(conID, new TextContentAdapter(),
							new FacetContentProposalProvider(_facade.getAllPersonsFacets()), null, null);
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
					validate();
				}
			});

			Button setConcurrence = new Button(_concurrenceGroup, SWT.PUSH);
			setConcurrence.setText(NLMessages.getString("Editor_select_dots")); //$NON-NLS-1$
			setConcurrence.setToolTipText(NLMessages.getString("Editor_open_selectObjectDialog_concurrence"));
			setConcurrence.setImage(_imageReg.get(IconsInternal.SEARCH));
			setConcurrence.setEnabled(_mayWritePerson);
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
			delConcurrence.setEnabled(_mayWritePerson);
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
			addReference.setEnabled(_mayWritePerson);
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
						conRefID.setEditable(_mayWritePerson);
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
								ContentProposalAdapter adapter = new ContentProposalAdapter(conRefID,
										new TextContentAdapter(), new FacetContentProposalProvider(_facade
												.getAllReferenceFacets()), null, null);
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
						setReference.setEnabled(_mayWritePerson);
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
										// setDecoInfo(decoConRefIdInfo,
										// validationStm.getReference().getSourceId());
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
							delReference.setEnabled(_mayWritePerson);
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
						conInternal.setEditable(_mayWritePerson);
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
							conRadios[k].setText(NLMessages.getString("Editor_" + AEConstants.REFRENCEQUALITIES[j]));
							conRadios[k].setData("text", AEConstants.REFRENCEQUALITIES[j]);

							conRadios[k].addSelectionListener(conListener);
							conRadios[k].setEnabled(_mayWritePerson);
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
							refauthority.setText(NLMessages.getString("Editor_user") + ": "
									+ _facade.getCurrentUser().getPdrId().toString());
							validationStm.setAuthority(_facade.getCurrentUser().getPdrId());
						}

						refauthority.setLayoutData(new GridData());
						((GridData) refauthority.getLayoutData()).horizontalSpan = 3;

						final Text refCitation = new Text(refComposite, SWT.MULTI | SWT.BORDER | SWT.WRAP
								| SWT.V_SCROLL);
						refCitation.setEditable(_mayWritePerson);
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
		contentCompCon.pack();
		_scrollCompCon.setContent(contentCompCon);
		Point point = contentCompCon.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
		Point mp = _tabFolder.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
		if (point.x > mp.x - 20)
		{
			point.x = mp.x - 20;
		}
		if (point.y > mp.y - 20)
		{
			point.y = mp.y - 20;
		}
		_scrollCompCon.setMinSize(point);
		_scrollCompCon.layout();
		_concurrenceTI.getControl().redraw();
		((Composite) _concurrenceTI.getControl()).layout();
		_concurrenceTI.getControl().update();
	}

	private void createComplexTemplateTabs()
	{
		for (String label : _aspectTemplateController.getComplexAspectTemplateEditorLabels())
		{
			if (label != null)
			{
				CTabItem tabItem = new CTabItem(_tabFolder, SWT.NONE);
				tabItem.setText(label);
				tabItem.setImage(_imageReg.get(IconsInternal.TEMPLATES));
				tabItem.setData("img", IconsInternal.TEMPLATES);
				tabItem.setData("loaded", true);
				tabItem.setData("complex", true);
				_selectedTabItem = tabItem;
				ComplexSemanticTemplate cst = loadComplexSemanticTemplate(label);
				if (cst != null)
				{
					tabItem.setData("tip", cst.getDescription());
				}
				Composite complexEditorComposite = new Composite(_tabFolder, SWT.NONE);
				tabItem.setControl(complexEditorComposite);
				complexEditorComposite.setLayout(new GridLayout(1, false));
				complexEditorComposite.setBackground(WHITE_COLOR);

				ScrolledComposite scrollComp;

				scrollComp = new ScrolledComposite(complexEditorComposite, SWT.V_SCROLL);
				scrollComp.setExpandHorizontal(true);
				scrollComp.setExpandVertical(true);
				scrollComp.setMinSize(SWT.DEFAULT, SWT.DEFAULT);
				scrollComp.setAlwaysShowScrollBars(true);
				tabItem.setData("sc", scrollComp);
				scrollComp.setLayoutData(new GridData());
				((GridData) scrollComp.getLayoutData()).heightHint = 375;
				((GridData) scrollComp.getLayoutData()).widthHint = 900;
				((GridData) scrollComp.getLayoutData()).horizontalAlignment = SWT.FILL;
				((GridData) scrollComp.getLayoutData()).verticalAlignment = SWT.FILL;
				((GridData) scrollComp.getLayoutData()).grabExcessHorizontalSpace = true;
				((GridData) scrollComp.getLayoutData()).grabExcessVerticalSpace = true;

				GridLayout layout;
				layout = new GridLayout();
				layout.numColumns = 1;
				layout.verticalSpacing = 0;
				scrollComp.setLayout(layout);

				Composite contentComp = new Composite(scrollComp, SWT.NONE);
				contentComp.setLayout(layout);
				contentComp.setLayoutData(new GridData());
				((GridData) contentComp.getLayoutData()).horizontalAlignment = SWT.FILL;
				((GridData) contentComp.getLayoutData()).verticalAlignment = SWT.FILL;
				((GridData) contentComp.getLayoutData()).grabExcessHorizontalSpace = true;
				((GridData) contentComp.getLayoutData()).grabExcessVerticalSpace = true;
				contentComp.setBackground(WHITE_COLOR);
				scrollComp.setContent(contentComp);
				scrollComp.setMinHeight(1);
				scrollComp.setMinWidth(1);
				ViewHelper.accelerateScrollbar(scrollComp, 20);
				scrollComp.setFocus();

				IComplexAspectTemplateEditor editor = _aspectTemplateController.getComplexAspectTemplateEditor(
						PersonAspectEditor.this, label, _currentPerson,
						contentComp, SWT.NONE);
				if (editor != null)
				{
					_complexAspectEditors.add(editor);
					editor.addCustomPaintListener(_paintListener);
					editor.addEasyEditorSelectionListener(_selectionListener);
				}
				tabItem.setData("complexEditor", editor);
				complexEditorComposite.layout();
				scrollComp.layout(true, true);

			}
			
		}

	}

	private void createListener()
	{
		_selectionListener = new SelectionAdapter()
		{

			@Override
			public void widgetSelected(SelectionEvent e)
			{
				_selectedTabItem = _tabFolder.getSelection();

				if (e.data != null && e.data instanceof IEasyAspectEditor)
				{

					IEasyAspectEditor newEditor = (IEasyAspectEditor) e.data;
					_selectedAspect = newEditor.getAspect();
					if (_selectedAspectEditor != null && !_selectedAspectEditor.equals(newEditor))
					{
						_selectedAspectEditor.setSelected(false, _selectedAspectEditor.isValid());
						_selectedAspectEditor = newEditor;
						_selectedAspectEditor.setSelected(true, _selectedAspectEditor.isValid());
					}
					else if (_selectedAspectEditor != null && _selectedAspectEditor.equals(newEditor))
					{
					}
					else
					{
						_selectedAspectEditor = newEditor;
						_selectedAspectEditor.setSelected(true, _selectedAspectEditor.isValid());
					}
					enableAspectButtons(_selectedTabItem, false);
				}

			}

		};
		_paintListener = new PaintListener()
		{

			@Override
			public void paintControl(PaintEvent e)
			{
				// System.out.println("call paintlistener in person aspect editor ");
				Object o = _selectedTabItem.getData("sc");
				if (o != null && o instanceof ScrolledComposite)
				{
					ScrolledComposite c = (ScrolledComposite) o;
//
					Composite cc = (Composite) c.getContent();

					Point point = cc.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
					cc.setSize(point.x, point.y + 100);
					cc.redraw();
					cc.layout();
					cc.update();
//
//					c.redraw();
//					c.layout();
//					c.update();

					Point mp = _tabFolder.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
					if (point.x > mp.x - 20)
					{
						point.x = mp.x - 20;
					}
					c.setMinSize(point);
					c.redraw();
					c.layout();
					c.update();
				}
			}

		};
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
					close();

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

	private void createTabItem(ConfigData cf, Vector<Aspect> aspects)
	{
		int num = 0;
		if (aspects != null)
		{
			num = aspects.size();
		}
		final CTabItem tabItem = new CTabItem(_tabFolder, SWT.NONE);
		tabItem.setText(cf.getLabel() + "(" + num + ")");
		tabItem.setData("config", cf);
		tabItem.setData("aspects", aspects);
		tabItem.setData("loaded", false);
		tabItem.setData("editors", new Vector<IEasyAspectEditor>());
		tabItem.setData("tip", cf.getDescription());
		tabItem.setToolTipText("");
		// tabItem.addListener(SWT.MouseEnter, new Listener()
		// {
		// public void handleEvent(Event event)
		// {
		// switch (event.type)
		// {
		// case SWT.MouseEnter:
		// case SWT.MouseMove:
		// System.out.println("########################################### " +
		// tabItem.getText());
		// break;
		// }
		// }
		// });
		if (_editingAspect != null && aspects != null && aspects.contains(_editingAspect))
		{
			_editingAspectTabItem = _tabFolder.indexOf(tabItem);
		}

		// FIXME erneuern.
		// scrollComp.getVerticalBar().setIncrement(scrollComp.getVerticalBar().getIncrement()*20);
		// scrollComp.setFocus();






		// scrollComp.setContent(contentComp);
		// scrollComp.setMinSize(contentComp.computeSize(SWT.DEFAULT,
		// SWT.DEFAULT));
		// contentComp.layout();
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize()
	{
		return new Point(1070, 600);
	}

	@Override
	public boolean isDirty()
	{
		return false;
	}

	@Override
	protected boolean isResizable()
	{
		return true;
	}

	@Override
	public boolean isValid()
	{
		return false;
	}

	private boolean isValidInput()
	{
		return true;
	}

	private void enableAspectButtons(CTabItem tabItem, boolean resetDisable)
	{
		boolean enable = false;
		if (!resetDisable)
		{
			enable = _selectedAspect != null;
		}
		Button cB = (Button) tabItem.getData("copy");
		if (cB != null)
		{
			cB.setEnabled(enable);
		}
		Button dB = (Button) tabItem.getData("del");
		if (dB != null)
		{
			dB.setEnabled(enable && _rightsChecker.mayWrite(_selectedAspect));
		}
	}


	private void loadAspects(final CTabItem tabItem, final int startIndex, final int number)
	{
		final int start;
		Composite composite = new Composite(_tabFolder, SWT.NONE);
		tabItem.setControl(composite);
		composite.setLayout(new GridLayout(1, false));
		((GridLayout) composite.getLayout()).marginHeight = 0;

		boolean valid = true;

		Composite compButtons = new Composite(composite, SWT.NONE);
		compButtons.setLayoutData(new GridData());
		((GridData) compButtons.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) compButtons.getLayoutData()).grabExcessHorizontalSpace = true;
		compButtons.setLayout(new GridLayout(3, true));
		((GridLayout) compButtons.getLayout()).marginHeight = 0;

		Button newAspButton = new Button(compButtons, SWT.PUSH);
		newAspButton.setText(NLMessages.getString("EditorLite_label_new_Aspect"));
		newAspButton.setToolTipText(NLMessages.getString("EditorLite_label_new_Aspect_tooltip"));
		newAspButton.setImage(_imageReg.get(IconsInternal.ASPECT_NEW));
		newAspButton.setLayoutData(new GridData());
		((GridData) newAspButton.getLayoutData()).horizontalSpan = 1;
		((GridData) newAspButton.getLayoutData()).horizontalAlignment = SWT.LEFT;

		newAspButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent event)
			{
				if (_selectedAspectEditor != null)
				{
					_selectedAspectEditor.saveInput();
				}
				SemanticStm sStm = new SemanticStm();
				sStm.setProvider(_configProvider);
				ConfigData cf = (ConfigData) tabItem.getData("config");
				if (cf != null)
				{
					sStm.setLabel(cf.getValue());
				}
				Aspect a = _pdrObjectBuilder.buildNewAspect(_currentPerson.getPdrId(), sStm);
				_editingAspect = a;
				_currentAspects.add(a);
				_currentPerson.getAspectIds().add(a.getPdrId());
				@SuppressWarnings("unchecked")
				Vector<Aspect> tabAspects = (Vector<Aspect>) tabItem.getData("aspects");
				if (tabAspects == null)
				{
					tabAspects = new Vector<Aspect>(1);
				}
				tabAspects.add(0, a);
				tabItem.setData("aspects", tabAspects);
				loadAspects(tabItem, 0, 10);
				_selectedAspect = a;
				enableAspectButtons(tabItem, false);
				validate();
			}
		});

		Button copyAspButton = new Button(compButtons, SWT.PUSH);
		copyAspButton.setText(NLMessages.getString("CategoryView_copyAspect"));
		copyAspButton.setToolTipText(NLMessages.getString("CategoryView_copyAspect"));
		copyAspButton.setImage(_imageReg.get(IconsInternal.ASPECT_COPY));
		copyAspButton.setLayoutData(new GridData());
		((GridData) copyAspButton.getLayoutData()).horizontalSpan = 1;
		((GridData) copyAspButton.getLayoutData()).horizontalAlignment = SWT.LEFT;
		tabItem.setData("copy", copyAspButton);
		copyAspButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent event)
			{
				if (_selectedAspectEditor != null)
				{
					_selectedAspectEditor.saveInput();
				}
				if (_selectedAspect != null)
				{
					Aspect a = _pdrObjectBuilder.buildCopyAspect(_selectedAspect);
					_editingAspect = a;
					_currentAspects.add(a);
					_currentPerson.getAspectIds().add(a.getPdrId());
					@SuppressWarnings("unchecked")
					Vector<Aspect> tabAspects = (Vector<Aspect>) tabItem.getData("aspects");
					if (tabAspects == null)
					{
						tabAspects = new Vector<Aspect>(1);
					}
					_selectedAspect = a;
					tabAspects.add(0, a);
					tabItem.setData("aspects", tabAspects);
					loadAspects(tabItem, 0, 10);
					enableAspectButtons(tabItem, false);
					validate();
				}
			}
		});

		Button deleteAspect = new Button(compButtons, SWT.PUSH);
		deleteAspect.setText(NLMessages.getString("DeleteAspectHandler_title"));
		deleteAspect.setImage(_imageReg.get(IconsInternal.ASPECT_DELETE));
		deleteAspect.setLayoutData(new GridData());
		((GridData) deleteAspect.getLayoutData()).horizontalSpan = 1;
		((GridData) deleteAspect.getLayoutData()).horizontalAlignment = SWT.RIGHT;
		tabItem.setData("del", deleteAspect);
		deleteAspect.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				if (deleteAspect(_selectedAspect))
				{
					@SuppressWarnings("unchecked")
					Vector<Aspect> tabAspects = (Vector<Aspect>) _selectedTabItem.getData("aspects");
					if (tabAspects == null)
					{
						tabAspects = new Vector<Aspect>(1);
					}
					tabAspects.remove(_selectedAspect);
					_selectedTabItem.setData("aspects", tabAspects);
					_selectedAspect = null;
					_editingAspect = null;
					_selectedAspectEditor = null;
					loadAspects(_selectedTabItem, 0, 10);
				}

				enableAspectButtons(tabItem, false);
				validate();
			}


		});
		ScrolledComposite scrollComp;

		scrollComp = new ScrolledComposite(composite, SWT.V_SCROLL);
		scrollComp.setExpandHorizontal(true);
		scrollComp.setExpandVertical(true);
		scrollComp.setMinSize(SWT.DEFAULT, SWT.DEFAULT);
		scrollComp.setAlwaysShowScrollBars(true);
		tabItem.setData("sc", scrollComp);
		scrollComp.setLayoutData(new GridData());
		((GridData) scrollComp.getLayoutData()).heightHint = 375;
		((GridData) scrollComp.getLayoutData()).widthHint = 740;
		((GridData) scrollComp.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) scrollComp.getLayoutData()).verticalAlignment = SWT.FILL;
		((GridData) scrollComp.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) scrollComp.getLayoutData()).grabExcessVerticalSpace = true;

		IEasyAspectEditor easyEditor = null;
		GridLayout layout;
		layout = new GridLayout();
		layout.numColumns = 1;
		layout.verticalSpacing = 25;
		scrollComp.setLayout(layout);

		Composite contentComp = new Composite(scrollComp, SWT.NONE);
		contentComp.setLayout(layout);
		contentComp.setLayoutData(new GridData());
		((GridData) contentComp.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) contentComp.getLayoutData()).verticalAlignment = SWT.FILL;
		((GridData) contentComp.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) contentComp.getLayoutData()).grabExcessVerticalSpace = true;
		contentComp.setBackground(WHITE_COLOR);
		scrollComp.setContent(contentComp);
		scrollComp.setMinHeight(1);
		scrollComp.setMinWidth(1);
		ViewHelper.accelerateScrollbar(scrollComp, 20);
		scrollComp.setFocus();
		// scrollComp.pack();

		@SuppressWarnings("unchecked")
		Vector<Aspect> aspects = (Vector<Aspect>) tabItem.getData("aspects");

		@SuppressWarnings("unchecked")
		Vector<IEasyAspectEditor> editors = (Vector<IEasyAspectEditor>) tabItem.getData("editors");
		int num = 0;
		if (aspects != null)
		{
			num = aspects.size();
		}
		ConfigData cf = (ConfigData) tabItem.getData("config");
		if (cf != null)
		{
			tabItem.setText(cf.getLabel() + "(" + num + ")");
		}

		if (aspects != null)
		{

			final int size = aspects.size(); //$NON-NLS-1$

			if (size > 10)
			{

				start = startIndex;

				Group eventNavBar = new Group(contentComp, SWT.NONE);
				eventNavBar.setText(NLMessages.getString("View_scroll")); //$NON-NLS-1$
				eventNavBar.setLayout(new RowLayout());

				Label eventNumber = new Label(eventNavBar, SWT.NONE);
				int endIndex = startIndex + 10;
				if (endIndex > size)
				{
					endIndex = size;
				}
				eventNumber
						.setText(NLMessages.getString("View_allTogether") + size + NLMessages.getString("View_aspects") + //$NON-NLS-1$ //$NON-NLS-2$
								(startIndex + 1) + " - " + endIndex); //$NON-NLS-1$

				Button toStart = new Button(eventNavBar, SWT.PUSH);
				toStart.setText(" |< "); //$NON-NLS-1$
				toStart.addSelectionListener(new SelectionAdapter()
				{

					@Override
					public void widgetSelected(final SelectionEvent arg0)
					{
						loadAspects(tabItem, 0, number);
					}

				});
				final Button minusFifty = new Button(eventNavBar, SWT.PUSH);
				minusFifty.setText(" -50 "); //$NON-NLS-1$
				if (start - 50 < 0)
				{
					minusFifty.setEnabled(false);
				}
				else
				{
					minusFifty.setEnabled(true);
				}
				minusFifty.addSelectionListener(new SelectionAdapter()
				{

					@Override
					public void widgetSelected(final SelectionEvent arg0)
					{
						int nextStart = start - 50;

						loadAspects(tabItem, nextStart, number);
					}

				});
				final Button minusTwen = new Button(eventNavBar, SWT.PUSH);
				minusTwen.setText(" -20 "); //$NON-NLS-1$
				if (start - 20 < 0)
				{
					minusTwen.setEnabled(false);
				}
				else
				{
					minusTwen.setEnabled(true);
				}
				minusTwen.addSelectionListener(new SelectionAdapter()
				{

					@Override
					public void widgetSelected(final SelectionEvent arg0)
					{
						int nextStart = start - 20;

						loadAspects(tabItem, nextStart, number);
					}

				});
				final Button minusTen = new Button(eventNavBar, SWT.PUSH);
				minusTen.setText(" -10 "); //$NON-NLS-1$
				if (start - 10 < 0)
				{
					minusTen.setEnabled(false);
				}
				else
				{
					minusTen.setEnabled(true);
				}
				minusTen.addSelectionListener(new SelectionAdapter()
				{

					@Override
					public void widgetSelected(final SelectionEvent arg0)
					{
						int nextStart = start - 10;

						loadAspects(tabItem, nextStart, number);
					}

				});

				final Text jumpTo = new Text(eventNavBar, SWT.BORDER);
				jumpTo.setSize(15, 20);

				Button okButton = new Button(eventNavBar, SWT.PUSH);
				okButton.setText(NLMessages.getString("View_ok")); //$NON-NLS-1$
				okButton.setToolTipText(NLMessages.getString("View_jump_to_aspect_tooltip"));
				okButton.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent arg0)
					{
						try
						{
							if (jumpTo.getText().length() > 0)
							{
								int n = Integer.parseInt(jumpTo.getText());

								if (n > 0 && n <= size)
								{
									loadAspects(tabItem, --n, number);
								}
							}
						}
						catch (NumberFormatException ex)
						{
							String message = NLMessages.getString("View_pleaseEnterNumber"); //$NON-NLS-1$
							MessageDialog.openInformation(_parentShell, NLMessages.getString("View_error"), message); //$NON-NLS-1$
							//
						}
					}
				});

				final Button plusTen = new Button(eventNavBar, SWT.PUSH);
				plusTen.setText(" +10 "); //$NON-NLS-1$
				if (start + 10 >= size)
				{
					plusTen.setEnabled(false);
				}
				else
				{
					plusTen.setEnabled(true);
				}
				plusTen.addSelectionListener(new SelectionAdapter()
				{

					@Override
					public void widgetSelected(final SelectionEvent arg0)
					{
						int nextStart = start + 10;

						loadAspects(tabItem, nextStart, number);
					}

				});
				final Button plusTwen = new Button(eventNavBar, SWT.PUSH);
				plusTwen.setText(" +20 "); //$NON-NLS-1$
				if (start + 20 >= size)
				{
					plusTwen.setEnabled(false);
				}
				else
				{
					plusTwen.setEnabled(true);
				}
				plusTwen.addSelectionListener(new SelectionAdapter()
				{

					@Override
					public void widgetSelected(final SelectionEvent arg0)
					{
						int nextStart = start + 20;

						loadAspects(tabItem, nextStart, number);
					}
				});
				final Button plusFifty = new Button(eventNavBar, SWT.PUSH);
				plusFifty.setText(" +50 "); //$NON-NLS-1$
				if (start + 50 >= size)
				{
					plusFifty.setEnabled(false);
				}
				else
				{
					plusFifty.setEnabled(true);
				}
				plusFifty.addSelectionListener(new SelectionAdapter()
				{

					@Override
					public void widgetSelected(final SelectionEvent arg0)
					{
						int nextStart = start + 50;

						loadAspects(tabItem, nextStart, number);
					}

				});
			}
			else
			{
				start = 0;
			}

			int i = start;
			// int end = i + number;

			while (i < start + number)
			{
				if (size > i)
				{

					Aspect ca = aspects.get(i);				
					if (!ca.isValid())
					{
						valid = false;
					}
					boolean template = false;
					if (_aspectTemplateController != null)
					{
						for (String sem : _aspectTemplateController.getSemanticsOfTemplates())
						{
							if (sem.equals(cf.getValue()))
							{
								template = true;
								SemanticTemplate semanticTemplate = loadSemanticTemplate(cf.getValue());
								easyEditor = _aspectTemplateController.getEasyAspectEditor(PersonAspectEditor.this,
										semanticTemplate, _currentPerson, ca, contentComp, SWT.BORDER);
							}
						}
					}
					if (!template)
					{
						easyEditor = new EasyAspectEditor(_currentPerson, ca, PersonAspectEditor.this, contentComp,
								SWT.NONE);
					}
					// GridData gd = new GridData();
					// gd.grabExcessHorizontalSpace = true;
					// gd.horizontalAlignment = SWT.FILL;
					// easyEditor.setLayoutData(gd);
					easyEditor.addSelectionListener(_selectionListener);
					easyEditor.addCustomPaintListener(_paintListener);
					easyEditor.setEditable(_rightsChecker.mayWrite(ca));
					editors.add(easyEditor);
					if (_editingAspect != null && _editingAspect.equals(ca))
					{
						_editingAspectIndex = new Integer(i);
						easyEditor.setSelected(true, easyEditor.isValid());
					}
					else
					{
						easyEditor.setSelected(i == 0, easyEditor.isValid());
					}

					
					i++;
				}
				else
				{
					break;
				}
			}
		}

		if (!valid)
		{
			tabItem.setImage(_imageReg.get(IconsInternal.ERROR));
		}
		enableAspectButtons(tabItem, false);
		contentComp.layout();
		Point point = contentComp.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
		contentComp.setSize(point.x, point.y + 100);
		Point mp = _tabFolder.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
		if (point.x > mp.x - 20)
		{
			point.x = mp.x - 20;
		}
		scrollComp.setMinSize(point);
		scrollComp.layout();
		Composite comp = (Composite) tabItem.getControl();
		comp.redraw();
		comp.layout();
		comp.update();
		_tabFolder.redraw();
		_tabFolder.layout();
		_tabFolder.update();

		if (_numberOfNoWritingAspects > 0)
		{
			showRestrictedWrtingRightsMessage();
		}
	}

	public boolean deleteAspect(Aspect deleteAspect)
	{
		if (deleteAspect != null)
		{
			if (new UserRichtsChecker().mayDelete(deleteAspect))
			{

				String message = NLMessages.getString("DeleteAspectHandler_warning1");
				message += NLMessages.getString("DeleteAspectHandler_warning2");
				message += NLMessages.getString("DeleteAspectHandler_warning3")
						+ deleteAspect.getNotification();
				message += NLMessages.getString("DeleteAspectHandler_lb_id")
						+ deleteAspect.getPdrId().toString();
				MessageDialog messageDialog = new MessageDialog(PersonAspectEditor.this.getParentShell(),
						NLMessages.getString("DeleteAspectHandler_title"), null, message,
						MessageDialog.WARNING, new String[]
						{NLMessages.getString("Handler_delete"), NLMessages.getString("Handler_cancel")}, 1);
				if (messageDialog.open() == 0)
				{
					IDBManager dbm = _facade.getDBManager();
					try
					{
						dbm.delete(deleteAspect.getPdrId(), "aspect");
					}
					catch (Exception ee)
					{
						// TODO Auto-generated catch block
						ee.printStackTrace();
					} //$NON-NLS-1$ //$NON-NLS-2$
					_facade.deleteAspect(deleteAspect);
					_currentAspects.remove(deleteAspect);
					_currentPerson.getAspectIds().remove(deleteAspect.getPdrId());
					_invalidAspects.remove(deleteAspect);
					_selectedAspectEditor.setSelected(false, true);
					_selectedAspectEditor = null;
					validate();

					return true;

				}
			}
			else
			{

				MessageDialog.openInformation(PersonAspectEditor.this.getParentShell(),
						NLMessages.getString("Commands_no_rights_delete"),
						NLMessages.getString("Command_no_rights_delete_aspect_message")); //$NON-NLS-1$
			}
		}

		return false;
	}

	private SemanticTemplate loadSemanticTemplate(String value)
	{
		if (_aspectTemplates == null)
		{
			_aspectTemplates = _facade.getConfigs().get(_configProvider).getUsage().getTemplates().getChildren()
					.get("aspectTemplates");
		}
		if (_aspectTemplates != null)
			{
			ConfigData semanticTemplates = _aspectTemplates.getChildren().get("semanticTemplates");
			if (semanticTemplates != null && semanticTemplates.getChildren().containsKey(value)
					&& (semanticTemplates.getChildren().get(value) instanceof SemanticTemplate))
				{
				return (SemanticTemplate) semanticTemplates.getChildren().get(value);
				}
		}
		return null;
	}

	private ComplexSemanticTemplate loadComplexSemanticTemplate(String value)
	{
		if (_aspectTemplates == null && _facade.getConfigs().containsKey(_configProvider)
				&& _facade.getConfigs().get(_configProvider).getUsage() != null
				&& _facade.getConfigs().get(_configProvider).getUsage().getTemplates() != null
				&& _facade.getConfigs().get(_configProvider).getUsage().getTemplates().getChildren().containsKey("aspectTemplates"))
		{
			_aspectTemplates = _facade.getConfigs().get(_configProvider).getUsage().getTemplates().getChildren()
					.get("aspectTemplates");
		}
		if (_aspectTemplates != null)
		{
			ConfigData cd = _aspectTemplates.getChildren().get("complexTemplates");
			if (cd != null)
			{
				if (cd.getChildren().containsKey(value) && (cd.getChildren().get(value) instanceof ComplexSemanticTemplate))
				{
					return (ComplexSemanticTemplate) cd.getChildren().get(value);
				}
				else if (!cd.getChildren().isEmpty())
				{
					for (ConfigData c : cd.getChildren().values())
					{
 						if ((c.getValue() != null && c.getValue().equals(value)) || (c.getLabel() != null && c.getLabel().equals(value)) && c instanceof ComplexSemanticTemplate)
						{
							ComplexSemanticTemplate cst = (ComplexSemanticTemplate) c;
							return cst;
						}
					}
				}
			}
		}
		
		return null;
	}

	private void showRestrictedWrtingRightsMessage()
	{
		setMessage(NLMessages.getString("EditorLite_message_restricted_rights"));

	}



	void loadValues()
	{
		if (_currentPerson != null)
		{
			_pdrObjectsProvider.setOrderer(_ordererFactory.createAspectOrderer("aspect.semantic"));
			_pdrObjectsProvider.setInput(_currentPerson);
			Vector<OrderingHead> orderedAspects = _pdrObjectsProvider.getArrangedAspects();
			
			_currentAspects = _pdrObjectsProvider.getAspects();

			if (_editingAspect != null && !_currentAspects.contains(_editingAspect))
			{
				String sem = "error";
				boolean found = false;
				if (_editingAspect.getSemanticDim() != null
						&& _editingAspect.getSemanticDim().getSemanticStms() != null
						&& _editingAspect.getSemanticDim().getSemanticLabelByProvider(_configProvider) != null)
				{
					sem = _editingAspect.getSemanticDim().getSemanticLabelByProvider(_configProvider).firstElement();
				}
				for (OrderingHead oh : orderedAspects)
				{
					if (oh.getValue() != null)
					{
						if (oh.getValue().equals(sem))
						{
							oh.addAspect(_editingAspect);
							found = true;
							break;
						}
					}
				}
				if (!found)
				{
					OrderingHead oh = new OrderingHead();
					oh.setValue(sem);
					oh.addAspect(_editingAspect);
					orderedAspects.add(oh);
				}
				_currentAspects.add(_editingAspect);
				// FIXME mehr steuerung!
			}
			if (_facade.getConfigs().containsKey(_configProvider))
			{
				List<ConfigData> semantics = new ArrayList<ConfigData>(_facade.getConfigs().get(_configProvider)
						.getChildren().get("aodl:semanticStm").getChildren().values());
				Collections.sort(semantics);
				List<ConfigData> emptySemantics = new ArrayList<ConfigData>(semantics.size());
				for (ConfigData cf : semantics)
				{
					if (cf instanceof ConfigItem && !((ConfigItem) cf).isIgnore())
					{
						boolean isComplex = false;
						if (_aspectTemplateController != null
								&& _aspectTemplateController.getComplexAspectTemplateSemantics() != null)
						{
							for (String s : _aspectTemplateController.getComplexAspectTemplateSemantics())
							{
								if (cf.getValue().equals(s))
								{
									isComplex = true;
									break;
								}
							}
						}

						boolean exists = false;
						for (OrderingHead oh : orderedAspects)
						{
							if (oh.getValue() != null)
							{
								if (isComplex && loadComplexAspecEditor(oh))
								{

								}
								else if (oh.getValue().equals(cf.getValue()))
								{
									createTabItem(cf, oh.getAspects());
									exists = true;
									break;
								}
							}
						}
						if (!exists && !isComplex)
						{
							emptySemantics.add(cf);
						}
					}
				}
				for (ConfigData cf : emptySemantics)
				{
					createTabItem(cf, null);
				}
			}

		}
		
		_tabFolder.setSelection(0);
		_tabFolder.setSelection(1);
		_tabFolder.setSelection(0);
		_selectedTabItem = _tabFolder.getItem(0);
		_selectedTabItem.getControl().setFocus();
		if (_editingAspect != null)
		{
			selectAspect(_editingAspect, _editingAspectTabItem);
		}
		else if (((Boolean) _selectedTabItem.getData("loaded")) == false)
		{

			loadAspects(_selectedTabItem, 0, 10);
			_selectedTabItem.setData("loaded", true);
			enableAspectButtons(_selectedTabItem, true);
		}

		validate();
		_tabFolder.layout();
	}

	


	private boolean loadComplexAspecEditor(OrderingHead oh)
	{
		for (IComplexAspectTemplateEditor editor : _complexAspectEditors)
		{
			// System.out.println("testing semantic " + oh.getValue());
			if (editor.getHandledSemantics().contains(oh.getValue()))
			{
				editor.setInput(oh);
				((Composite) editor).getParent().getParent().layout(true,true);
				Composite c = ((Composite) editor).getParent().getParent();
				Point p = c.getSize();
				c.setSize(p.x+1, p.y+1);
				Composite cc =c.getParent();
				cc.layout(true,true);
				p = cc.getSize();
				cc.setSize(p.x+1, p.y+1);
				return true;
			}
		}
		return false;

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

	private void reloadEditedObjects() throws Exception
	{
		if (_currentPerson.isDirty())
		{
			_currentPerson = _facade.getMainSearcher().getPersonById(_currentPerson.getPdrId());
			_facade.getAllPersons().put(_currentPerson.getPdrId(), _currentPerson);
		}
		Vector<Aspect> secureAspects = processDirtyAspects();

		if (secureAspects != null)
		{
			for (Aspect a : secureAspects)
			{
				a = _facade.getMainSearcher().searchAspect(a.getPdrId());
				if (a != null)
				{
					_facade.getLoadedAspects().put(a.getPdrId(), a);
				}
			}
		}
	}

	public void saveInput()
	{

		if (_selectedAspectEditor != null)
		{
			_selectedAspectEditor.saveInput();
		}
		if (_currentPerson.isDirty() || _currentPerson.isNew())
		{
			savePerson();
		}

		Vector<Aspect> secureAspects = processDirtyAspects();
		try
		{
			_facade.savePdrObjects(secureAspects);
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// _facade.setCurrentTreeObjects(new PdrObject[]
		// {_currentPerson});

	}

	private Vector<Aspect> processDirtyAspects()
	{
		for (Aspect a : _currentAspects)
		{
			if (a.isDirty())
			{
				_dirtyAspects.add(a);
			}
		}
		if (_complexAspectEditors != null && !_complexAspectEditors.isEmpty())
		{
			for (IComplexAspectTemplateEditor ed : _complexAspectEditors)
			{
				_dirtyAspects.addAll(ed.getDirtyAspects());
			}
		}

		Vector<Aspect> secureAspects = new Vector<Aspect>(_dirtyAspects.size());
		if (!_dirtyAspects.isEmpty())
		{

			for (Aspect a : _dirtyAspects)
			{
				if (!secureAspects.contains(a))
				{
					secureAspects.add(a);
					// System.out.println("secureAspects.add(a); " +
					// a.getPdrId());
				}
			}

		}
		return secureAspects;
	}

	private void savePerson()
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


	}



	private void selectAspect(Aspect aspect, int tabItemIndex)
	{
		_selectedTabItem = _tabFolder.getItem(tabItemIndex);

		if (_selectedTabItem != null && _selectedTabItem.getData("aspects") != null)
		{
			@SuppressWarnings("unchecked")
			Vector<Aspect> aspects = (Vector<Aspect>) _selectedTabItem.getData("aspects");
			if (aspects != null)
			{
				for (int i = 0; i < aspects.size(); i++)
				{
					Aspect a = aspects.get(i);
					if (a != null && a.equals(aspect))
					{
						_editingAspectIndex = i;
						break;
					}

				}
			}
			if (tabItemIndex >= 0)
			{
				loadAspects(_selectedTabItem, _editingAspectIndex, 10);
			}
			_tabFolder.setSelection(tabItemIndex);
			Vector<IEasyAspectEditor> editors = (Vector<IEasyAspectEditor>) _selectedTabItem.getData("editors");

			if (editors != null && editors.size() > _editingAspectIndex)
			{
				_tabFolder.setSelection(tabItemIndex);
				IEasyAspectEditor edit = editors.get(_editingAspectIndex);
				edit.setSelected(true, edit.isValid());
				ScrolledComposite sc = (ScrolledComposite) _selectedTabItem.getData("sc");
				sc.setShowFocusedControl(true);
				edit.setFocus();
			}
		}
	}

	private void setBackground(Color color)
	{
		// _personGroup.setBackground(color);
		// _lblNachname.setBackground(color);
		// _lblVorname.setBackground(color);
		// _lbl2Vorname.setBackground(color);
		// _lblGeschlecht.setBackground(color);
		// _lblAcadTitle.setBackground(color);
		// _lblPnd.setBackground(color);

	}

	@Override
	public void setDirty(boolean isDirty)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void setSelected(boolean isSelected, boolean contextIsValid)
	{
		if (contextIsValid && isSelected)
		{
			setBackground(AEVIEWConstants.VIEW_BACKGROUND_SELECTED_COLOR);
		}
		else if (contextIsValid)
		{
			setBackground(AEVIEWConstants.VIEW_BACKGROUND_DESELECTED_COLOR);
		}
		else
		{
			setBackground(AEVIEWConstants.VIEW_BACKGROUND_INVALID_COLOR);
		}
	}


	@Override
	public void validate()
	{

		if (_selectedTabItem != null)
		{
			boolean valid = true;
			if (_selectedAspectEditor != null)
			{
				valid = _selectedAspectEditor.isValid();
				_selectedAspectEditor.setSelected(true, valid);
			}
			else if (_selectedTabItem != null && _selectedTabItem.equals(_concurrenceTI))
			{
				valid = _currentPerson.isValid();
			}
			else if (_selectedTabItem != null && _selectedTabItem.equals(_identifierTabItem))
			{
				valid = _currentPerson.isValid();
			}
			else if (_selectedTabItem != null && _selectedTabItem.getData("aspects") == null)
			{
				valid = true;
			}
			else if (_selectedAspect != null)
			{
				valid = _selectedAspect.isValid();
			}
			if (valid)
			{
				Object oo = _selectedTabItem.getData("img");
				if (oo != null && oo instanceof String)
				{
					_selectedTabItem.setImage(_imageReg.get((String) oo));
				}
				else
				{
					_selectedTabItem.setImage(null);
				}
				_invalidAspects.remove(_selectedAspect);

			}
			else
			{
				if (!_invalidAspects.contains(_selectedAspect))
				{
					_invalidAspects.add(_selectedAspect);
				}
				_selectedTabItem.setImage(_imageReg.get(IconsInternal.ERROR));
			}
			if (_saveButton != null)
			{
				_saveButton.setEnabled(_invalidAspects.isEmpty() && valid);
			}
		}

	}

	@Override
	public void addSelectionListener(SelectionListener sl) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setInput(Object input) {
		if (input instanceof Person)
		{
			_currentPerson = (Person) input;
			loadValues();
		}
		
	}


	@Override
	public void setEditable(boolean editable) {
		// TODO Auto-generated method stub
		
	}


}
