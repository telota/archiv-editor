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
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import org.bbaw.pdr.ae.common.AEConstants;
import org.bbaw.pdr.ae.common.CommonActivator;
import org.bbaw.pdr.ae.common.NLMessages;
import org.bbaw.pdr.ae.common.icons.IconsInternal;
import org.bbaw.pdr.ae.control.core.UserRichtsChecker;
import org.bbaw.pdr.ae.control.facade.Facade;
import org.bbaw.pdr.ae.control.interfaces.AMainSearcher;
import org.bbaw.pdr.ae.control.interfaces.IPdrIdService;
import org.bbaw.pdr.ae.metamodel.PdrId;
import org.bbaw.pdr.ae.metamodel.Record;
import org.bbaw.pdr.ae.metamodel.Revision;
import org.bbaw.pdr.ae.model.DetailMods;
import org.bbaw.pdr.ae.model.ExtendMods;
import org.bbaw.pdr.ae.model.Genre;
import org.bbaw.pdr.ae.model.IdentifierMods;
import org.bbaw.pdr.ae.model.LocationMods;
import org.bbaw.pdr.ae.model.NameMods;
import org.bbaw.pdr.ae.model.NamePart;
import org.bbaw.pdr.ae.model.OriginInfo;
import org.bbaw.pdr.ae.model.PdrObject;
import org.bbaw.pdr.ae.model.ReferenceMods;
import org.bbaw.pdr.ae.model.ReferenceModsTemplate;
import org.bbaw.pdr.ae.model.RelatedItem;
import org.bbaw.pdr.ae.model.view.Facet;
import org.bbaw.pdr.ae.view.control.customSWTWidges.ReferenceEditorAddFieldToolTip;
import org.bbaw.pdr.ae.view.control.customSWTWidges.RevisionHistoryToolTip;
import org.bbaw.pdr.ae.view.control.customSWTWidges.YearSpinner;
import org.bbaw.pdr.ae.view.control.dialogs.SelectObjectDialog;
import org.bbaw.pdr.ae.view.control.provider.AutoCompleteNameLabelProvider;
import org.bbaw.pdr.ae.view.control.provider.FacetContentProposalProvider;
import org.bbaw.pdr.ae.view.control.provider.RefTemplateContentProvider;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.jface.bindings.keys.KeyStroke;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.fieldassist.AutoCompleteField;
import org.eclipse.jface.fieldassist.ContentProposalAdapter;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.jface.fieldassist.IContentProposalListener;
import org.eclipse.jface.fieldassist.TextContentAdapter;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
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
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.IHandlerService;

/**
 * The Class SourceEditorDialog.
 * @author Christoph Plutte
 */
public class SourceEditorDialog extends TitleAreaDialog implements Observer
{

	/** The _titel. */
	private Text _titel;

	/** The sub _titel. */
	private Text _subTitel;
	/** text of administrative data. */
	private Text _pdrId;

	/** The creator name text. */
	private Text _creatorNameText;

	/** The creation time text. */
	private Text _creationTimeText;

	/** The revisor name. */
	private Text _revisorName;

	/** The revision time text. */
	private Text _revisionTimeText;

	/** The button new. */
	private Button _buttonNew;

	/** The _savebutton. */
	private Button _savebutton;

	/** The del title. */
	private Button _delTitle;

	/** The deco val ti. */
	private ControlDecoration _decoValTi;

	/** The may write. */
	private boolean _mayWrite;

	/** combo for selecting source genre. */
	private Combo _sourceGenreCombo;

	/** The source genre combo viewer. */
	private ComboViewer _sourceGenreComboViewer;
	/** main composite. */
	private Composite _mainComposite;

	/** main composite for stack layout. */
	private Composite _compositeSourcePanel;

	/** The source stack layout. */
	private StackLayout _sourceStackLayout;

	/** The _facade. */
	private Facade _facade = Facade.getInstanz();

	/** The id service. */
	private IPdrIdService _idService = _facade.getIdService();

	/** _gridData for textfields. */
	private GridData _gridData;
	/** _gridData for labels. */
	private GridData _gridDataRight;

	/** composites for each source type. */
	private Composite _compositeEmpty;

	/** The composite ref. */
	private Composite _compositeRef;

	/** The _scrolled composite main. */
	private ScrolledComposite _scrolledCompositeMain;

	/** The add tool tip. */
	private ReferenceEditorAddFieldToolTip _addToolTip;

	/** The composite admin data. */
	private Composite _compositeAdminData;

	/** The _current reference. */
	private ReferenceMods _currentReference;

	/** The _main searcher. */
	private AMainSearcher _mainSearcher = _facade.getMainSearcher();
	/** Instance of shared image registry. */
	private ImageRegistry _imageReg = CommonActivator.getDefault().getImageRegistry();

	/** The WHIT e_ color. */
	private static final Color WHITE_COLOR = Display.getDefault().getSystemColor(SWT.COLOR_WHITE);
	/** date format of administrative dates in PDR. */
	private SimpleDateFormat _adminDateFormat = AEConstants.ADMINDATE_FORMAT;

	/** The new reference id. */
	private PdrId _newReferenceId;

	/** The as genre editor. */
	private boolean _asGenreEditor = false;

	private PdrId _superReference;

	/**
	 * Instantiates a new source editor dialog.
	 * @param parentShell the parent shell
	 * @param newReferenceId the new reference id
	 */
	public SourceEditorDialog(final Shell parentShell, final PdrId newReferenceId)
	{
		super(parentShell);
		this._newReferenceId = newReferenceId;
	}

	/**
	 * Instantiates a new source editor dialog.
	 * @param parentShell the parent shell
	 * @param currentReference the current reference
	 * @param asGenreEditor the as genre editor
	 */
	public SourceEditorDialog(final Shell parentShell, final ReferenceMods currentReference, final boolean asGenreEditor)
	{
		super(parentShell);
		this._currentReference = currentReference;
		this._asGenreEditor = asGenreEditor;
	}

	public SourceEditorDialog(Shell parentShell, PdrId newReferenceId, PdrId superReference)
	{
		super(parentShell);
		this._newReferenceId = newReferenceId;
		this._superReference = superReference;
	}

	@Override
	public final void create()
	{
		super.create();
		// Set the title
		setTitle(NLMessages.getString("Editor_0")); //$NON-NLS-1$
		// Set the message
		setMessage("", IMessageProvider.INFORMATION); //$NON-NLS-1$
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

	/**
	 * Creates the add buttons.
	 * @param contentComp the content comp
	 */
	private void createAddButtons(final Composite contentComp)
	{
		final Button addNames = new Button(contentComp, SWT.PUSH);
		addNames.setText(NLMessages.getString("Editor_add_field"));
		// addNames.setToolTipText(NLMessages.getString("Editor_add_names_tooltip"));
		addNames.setImage(_imageReg.get(IconsInternal.ADD));
		_addToolTip = new ReferenceEditorAddFieldToolTip(addNames, _currentReference);
		_addToolTip.addObserver(SourceEditorDialog.this);
		_addToolTip.setShift(new Point(-25, -25));
		_addToolTip.setPopupDelay(0);
		_addToolTip.setHideOnMouseDown(false);
		_addToolTip.activate();
		addNames.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent event)
			{

				_addToolTip.show(new Point(addNames.getLocation().x - 25, addNames.getLocation().y - 55));
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

	@Override
	protected final Control createDialogArea(final Composite parent)
	{
		_mainComposite = new Composite(parent, SWT.NONE);
		_mainComposite.setLayout(new GridLayout());
		_mainComposite.setLayoutData(new GridData());
		((GridData) _mainComposite.getLayoutData()).heightHint = 550;
		((GridData) _mainComposite.getLayoutData()).widthHint = 650;
		// The text fields will grow with the size of the dialog
		_gridData = new GridData();
		_gridData.grabExcessHorizontalSpace = true;
		_gridData.horizontalAlignment = GridData.FILL;
		_gridDataRight = new GridData();
		_gridDataRight.grabExcessHorizontalSpace = false;
		_gridDataRight.horizontalAlignment = SWT.RIGHT;

		if (!_asGenreEditor)
		{
			Composite adminComposite = new Composite(_mainComposite, SWT.NONE);
			adminComposite.setLayout(new GridLayout());
			((GridLayout) adminComposite.getLayout()).numColumns = 3;
			adminComposite.setLayoutData(new GridData());
			((GridData) adminComposite.getLayoutData()).horizontalAlignment = SWT.FILL;
			((GridData) adminComposite.getLayoutData()).grabExcessHorizontalSpace = true;
			Label soureGenreLabel = new Label(adminComposite, SWT.NONE);
			soureGenreLabel.setText(NLMessages.getString("Editor_genre")); //$NON-NLS-1$
			soureGenreLabel.setLayoutData(new GridData());
			_sourceGenreCombo = new Combo(adminComposite, SWT.DROP_DOWN | SWT.READ_ONLY);
			final ControlDecoration decoGenreInfo = new ControlDecoration(_sourceGenreCombo, SWT.RIGHT | SWT.TOP);
			_sourceGenreCombo.setLayoutData(new GridData());
			((GridData) _sourceGenreCombo.getLayoutData()).horizontalAlignment = SWT.FILL;
			((GridData) _sourceGenreCombo.getLayoutData()).grabExcessHorizontalSpace = true;
			_sourceGenreComboViewer = new ComboViewer(_sourceGenreCombo);
			_sourceGenreComboViewer.setContentProvider(new RefTemplateContentProvider(false));
			_sourceGenreComboViewer.setLabelProvider(new LabelProvider()
			{

				@Override
				public String getText(final Object element)
				{
					ReferenceModsTemplate template = (ReferenceModsTemplate) element;
					return template.getLabel();
				}

			});

			_sourceGenreComboViewer.setInput(_facade.getReferenceModsTemplates());
			_sourceGenreComboViewer.addSelectionChangedListener(new ISelectionChangedListener()
			{

				@Override
				public void selectionChanged(final SelectionChangedEvent event)
				{
					ISelection selection = event.getSelection();
					Object obj = ((IStructuredSelection) selection).getFirstElement();
					ReferenceModsTemplate template = (ReferenceModsTemplate) obj;
					_compositeSourcePanel.layout();
					_compositeSourcePanel.pack();
					_mainComposite.layout();
					if (template != null)
					{
						_buttonNew.setEnabled(true);
						if (template.getDocumentation() != null)
						{
							if (template.getDocumentation().containsKey(AEConstants.getCurrentLocale().getLanguage()))
							{
								decoGenreInfo.setDescriptionText(template.getDocumentation().get(
										AEConstants.getCurrentLocale().getLanguage()));
								decoGenreInfo.setImage(FieldDecorationRegistry.getDefault()
										.getFieldDecoration(FieldDecorationRegistry.DEC_INFORMATION).getImage());
							}
						}
						else
						{
							decoGenreInfo.setImage(null);
						}
						if (_currentReference != null)
						{
							_currentReference.getGenre().setGenre(template.getValue());

						}
					}
				}

			});

			_buttonNew = new Button(adminComposite, SWT.PUSH);

			_buttonNew.setText(NLMessages.getString("Editor_new")); //$NON-NLS-1$
			_buttonNew.setToolTipText(NLMessages.getString("Editor_new_tooltip"));
			_buttonNew.setImage(_imageReg.get(IconsInternal.REFERENCE_NEW));
			_buttonNew.setLayoutData(new GridData());
			((GridData) _buttonNew.getLayoutData()).horizontalIndent = 10;
			_buttonNew.addSelectionListener(new SelectionAdapter()
			{
				@Override
				public void widgetSelected(final SelectionEvent e)
				{
					IStructuredSelection selection = (IStructuredSelection) _sourceGenreComboViewer.getSelection();
					Object obj = selection.getFirstElement();
					ReferenceModsTemplate template = (ReferenceModsTemplate) obj;
					loadRefFormat(template.getRefTemplate());
				}
			});
			_buttonNew.setEnabled(false);
			_buttonNew.pack();
			adminComposite.layout();
			adminComposite.pack();
			// adminComposite
		}

		_compositeSourcePanel = new Composite(_mainComposite, SWT.NONE);
		_compositeSourcePanel.setLayoutData(new GridData());
		((GridData) _compositeSourcePanel.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _compositeSourcePanel.getLayoutData()).grabExcessHorizontalSpace = true;
		_compositeSourcePanel.setLayout(new GridLayout());
		((GridLayout) _compositeSourcePanel.getLayout()).numColumns = 1;
		((GridLayout) _compositeSourcePanel.getLayout()).makeColumnsEqualWidth = false;
		_sourceStackLayout = new StackLayout();
		_compositeSourcePanel.setLayout(_sourceStackLayout);

		// empty composite for stack

		_compositeEmpty = new Composite(_compositeSourcePanel, SWT.NONE);
		_compositeEmpty.layout();
		_compositeEmpty.pack();

		_sourceStackLayout.topControl = _compositeEmpty;
		if (_currentReference != null)
		{
			loadReference();
		}

		return parent;
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
		_savebutton = new Button(parent, SWT.PUSH);
		_savebutton.setText(label);
		_savebutton.setFont(JFaceResources.getDialogFont());
		_savebutton.setData(new Integer(id));
		_savebutton.setEnabled(_mayWrite);
		_savebutton.addSelectionListener(new SelectionAdapter()
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
				shell.setDefaultButton(_savebutton);
			}
		}
		setButtonLayoutData(_savebutton);
		return _savebutton;
	}

	/**
	 * Gets the current reference.
	 * @return the current reference
	 */
	public final ReferenceMods getCurrentReference()
	{
		return _currentReference;
	}

	@Override
	protected final boolean isResizable()
	{
		return false;
	}

	/**
	 * Checks if is valid input.
	 * @return true, if is valid input
	 */
	private boolean isValidInput()
	{
		return true;
		// Validator v = new Validator();
		// int error = v.isValid(_currentReference);
		////		System.out.println("Error: " + error); //$NON-NLS-1$
		// if(error == 3000)
		// {
		// return true;
		// }
		// else if (error == 3200 || error == 3210)
		// {
		//			setMessage(NLMessages.getString("Editor_error3200"), IMessageProvider.ERROR); //$NON-NLS-1$
		// return false;
		// }
		// else if (error == 3300 || error == 3310 || error == 3320)
		// {
		//			setMessage(NLMessages.getString("Editor_3300"), IMessageProvider.ERROR); //$NON-NLS-1$
		// return false;
		// }
		// else
		// {
		//			setMessage(NLMessages.getString("Editor_error3400"), IMessageProvider.ERROR); //$NON-NLS-1$
		// return false;
		// }
	}

	/**
	 * Load access condition.
	 * @param contentComp the content comp
	 */
	private void loadAccessCondition(final Composite contentComp)
	{
		Composite accConComp = new Composite(contentComp, SWT.NONE);
		accConComp.setLayoutData(new GridData());
		((GridData) accConComp.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) accConComp.getLayoutData()).grabExcessHorizontalSpace = true;
		accConComp.setLayout(new GridLayout());
		((GridLayout) accConComp.getLayout()).numColumns = 6;
		((GridLayout) accConComp.getLayout()).makeColumnsEqualWidth = false;

		Label label1 = new Label(accConComp, SWT.NONE);
		label1.setText(NLMessages.getString("Editor_accessCon")); //$NON-NLS-1$

		if (_currentReference.getAccessCondition().getType() != null)
		{
			Label label29 = new Label(accConComp, SWT.NONE);
			label29.setText(NLMessages.getString("Editor_type")); //$NON-NLS-1$
			final Text type = new Text(accConComp, SWT.BORDER);
			type.setEditable(_mayWrite);
			type.setBackground(WHITE_COLOR);
			type.setLayoutData(_gridData);
			type.setText(_currentReference.getAccessCondition().getType().trim());
			type.addFocusListener(new FocusListener()
			{
				@Override
				public void focusGained(final FocusEvent e)
				{

				}

				@Override
				public void focusLost(final FocusEvent e)
				{
					_currentReference.getAccessCondition().setType(type.getText());
				}
			});
		}
		if (_currentReference.getAccessCondition().getAccessCondition() != null)
		{
			Label label30 = new Label(accConComp, SWT.NONE);
			label30.setText(NLMessages.getString("Editor_condition")); //$NON-NLS-1$
			final Text acc = new Text(accConComp, SWT.BORDER);
			acc.setEditable(_mayWrite);
			acc.setBackground(WHITE_COLOR);
			acc.setLayoutData(_gridData);
			acc.setText(_currentReference.getAccessCondition().getAccessCondition().trim());
			acc.addFocusListener(new FocusListener()
			{
				@Override
				public void focusGained(final FocusEvent e)
				{
					String[] vals = new String[]
					{"test", "test2"}; //$NON-NLS-1$ //$NON-NLS-2$
					try
					{
						vals = _mainSearcher.getFacets("reference", "accessCondition", null, null, null); //$NON-NLS-1$ //$NON-NLS-2$
					}
					catch (Exception e1)
					{

						e1.printStackTrace();
					}
					new AutoCompleteField(acc, new TextContentAdapter(), vals);
				}

				@Override
				public void focusLost(final FocusEvent e)
				{
					_currentReference.getAccessCondition().setAccessCondition(acc.getText());
				}
			});
			final Button delAcc = new Button(accConComp, SWT.PUSH);
			delAcc.setText("-"); //$NON-NLS-1$
			delAcc.setToolTipText(NLMessages.getString("Editor_remove_field"));
			delAcc.setEnabled(_mayWrite);
			delAcc.setLayoutData(_gridData);
			delAcc.addSelectionListener(new SelectionAdapter()
			{
				@Override
				public void widgetSelected(final SelectionEvent event)
				{
					_currentReference.setAccessCondition(null); //$NON-NLS-1$
					loadValues(_currentReference);

				}
			});
			delAcc.setLayoutData(new GridData());
		}
	}

	/**
	 * Load admin data.
	 */
	private void loadAdminData()
	{
		_compositeAdminData = new Composite(_compositeRef, SWT.NONE);
		_compositeAdminData.setLayoutData(new GridData());
		((GridData) _compositeAdminData.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _compositeAdminData.getLayoutData()).minimumHeight = 50;
		((GridData) _compositeAdminData.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) _compositeAdminData.getLayoutData()).widthHint = 600;
		_compositeAdminData.setLayout(new GridLayout());
		((GridLayout) _compositeAdminData.getLayout()).marginHeight = 0;
		Group pdrIdGroup = new Group(_compositeAdminData, SWT.SHADOW_IN);
		pdrIdGroup.setText(NLMessages.getString("Editor_adminDataRef")); //$NON-NLS-1$

		pdrIdGroup.setLayoutData(new GridData());
		((GridData) pdrIdGroup.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) pdrIdGroup.getLayoutData()).minimumHeight = 50;
		((GridData) pdrIdGroup.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) pdrIdGroup.getLayoutData()).widthHint = 590;

		pdrIdGroup.setLayout(new GridLayout());
		((GridLayout) pdrIdGroup.getLayout()).numColumns = 5;
		((GridLayout) pdrIdGroup.getLayout()).marginHeight = 0;

		Label pdrLabel = new Label(pdrIdGroup, SWT.NONE);
		pdrLabel.setText(NLMessages.getString("Editor_PDRid")); //$NON-NLS-1$
		pdrLabel.setLayoutData(new GridData());
		_pdrId = new Text(pdrIdGroup, SWT.NONE | SWT.READ_ONLY);
		_pdrId.setLayoutData(new GridData());
		((GridData) _pdrId.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _pdrId.getLayoutData()).grabExcessHorizontalSpace = true;
		_pdrId.setText(_currentReference.getPdrId().toString());

		Label bl = new Label(pdrIdGroup, SWT.NONE);
		bl.setText("");
		bl.setLayoutData(new GridData());
		((GridData) bl.getLayoutData()).horizontalSpan = 2;

		Label historyLabel = new Label(pdrIdGroup, SWT.NONE);
		historyLabel.setText(NLMessages.getString("Editor_revision_history")); //$NON-NLS-1$
		historyLabel.setLayoutData(new GridData());

		Label creatorLabel = new Label(pdrIdGroup, SWT.NONE);
		creatorLabel.setText(NLMessages.getString("Editor_creator")); //$NON-NLS-1$
		creatorLabel.setLayoutData(new GridData());

		_creatorNameText = new Text(pdrIdGroup, SWT.NONE | SWT.READ_ONLY);
		_creatorNameText.setLayoutData(new GridData());
		((GridData) _creatorNameText.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _creatorNameText.getLayoutData()).grabExcessHorizontalSpace = true;
		if (_currentReference.getRecord() != null && _currentReference.getRecord().getRevisions() != null
				&& !_currentReference.getRecord().getRevisions().isEmpty()
				&& _currentReference.getRecord().getRevisions().firstElement().getAuthority() != null)
		{
			_creatorNameText.setText(_facade.getObjectDisplayName(_currentReference.getRecord().getRevisions().get(0)
					.getAuthority()));
		}
		else
		{
			_creatorNameText.setText(_facade.getCurrentUser().getPdrId().toString());
		}

		Label creationTime = new Label(pdrIdGroup, SWT.NONE);
		creationTime.setText(NLMessages.getString("Editor_date")); //$NON-NLS-1$
		creationTime.setLayoutData(new GridData());
		_creationTimeText = new Text(pdrIdGroup, SWT.NONE | SWT.READ_ONLY);
		_creationTimeText.setLayoutData(new GridData());

		if (_currentReference.getRecord() != null && _currentReference.getRecord().getRevisions() != null
				&& !_currentReference.getRecord().getRevisions().isEmpty()
				&& _currentReference.getRecord().getRevisions().firstElement().getTimeStamp() != null)
		{
			_creationTimeText.setText(_adminDateFormat.format(_currentReference.getRecord().getRevisions()
					.firstElement().getTimeStamp())); //$NON-NLS-1$

		}
		else
		{
			_creationTimeText.setText(_adminDateFormat.format(_facade.getCurrentDate()));
		}

		if (_currentReference.getRecord().getRevisions().size() > 1)
		{
			Label revisorLabel = new Label(pdrIdGroup, SWT.NONE);
			revisorLabel.setText(NLMessages.getString("Editor_lastChanged")); //$NON-NLS-1$
			revisorLabel.setLayoutData(new GridData());
			_revisorName = new Text(pdrIdGroup, SWT.NONE | SWT.READ_ONLY);
			if (_currentReference.getRecord().getRevisions().lastElement().getAuthority() != null)
			{
				_revisorName.setText(_currentReference.getRecord().getRevisions().lastElement().getAuthority()
						.toString()); //$NON-NLS-1$
			}
			_revisorName.setLayoutData(new GridData());

			Label revisionTime = new Label(pdrIdGroup, SWT.NONE);
			revisionTime.setText(NLMessages.getString("Editor_date")); //$NON-NLS-1$
			revisionTime.setLayoutData(new GridData());
			_revisionTimeText = new Text(pdrIdGroup, SWT.NONE | SWT.READ_ONLY);
			if (_currentReference.getRecord().getRevisions().lastElement().getTimeStamp() != null)
			{
				_revisionTimeText.setText(_adminDateFormat.format(_currentReference.getRecord().getRevisions()
						.lastElement().getTimeStamp())); //$NON-NLS-1$
			}
			_revisionTimeText.setLayoutData(new GridData());

			final RevisionHistoryToolTip historyToolTip = new RevisionHistoryToolTip(historyLabel,
					_currentReference.getRecord());
			historyToolTip.setShift(new Point(-25, -25));
			historyToolTip.setPopupDelay(0);
			historyToolTip.setHideOnMouseDown(true);
			historyToolTip.activate();
			historyLabel.addMouseListener(new MouseListener()
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
		}

		pdrIdGroup.pack();
		pdrIdGroup.layout();
		// pdrIdGroup
		_compositeAdminData.pack();
		_compositeAdminData.layout();

	}

	/**
	 * Load identifiers.
	 * @param contentComp the content comp
	 */
	private void loadIdentifiers(final Composite contentComp)
	{
		Composite identifierComp = new Composite(contentComp, SWT.NONE);
		identifierComp.setLayoutData(new GridData());
		((GridData) identifierComp.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) identifierComp.getLayoutData()).grabExcessHorizontalSpace = true;
		identifierComp.setLayout(new GridLayout());
		((GridLayout) identifierComp.getLayout()).numColumns = 6;
		((GridLayout) identifierComp.getLayout()).makeColumnsEqualWidth = false;

		for (int i = 0; i < _currentReference.getIdentifiersMods().size(); i++)
		{
			final IdentifierMods id = _currentReference.getIdentifiersMods().get(i);

			Label label20 = new Label(identifierComp, SWT.NONE);
			label20.setText(NLMessages.getString("Editor_identifier")); //$NON-NLS-1$

			Label label21 = new Label(identifierComp, SWT.NONE);
			label21.setText(NLMessages.getString("Editor_type")); //$NON-NLS-1$
			final Combo identTypeCombo = new Combo(identifierComp, SWT.READ_ONLY);
			identTypeCombo.setEnabled(_mayWrite);
			identTypeCombo.setBackground(WHITE_COLOR);
			identTypeCombo.setData("id", i); //$NON-NLS-1$
			identTypeCombo.setLayoutData(new GridData());
			identTypeCombo.setItems(AEConstants.REF_IDENTIFIER_TYPE);
			identTypeCombo.addSelectionListener(new SelectionAdapter()
			{
				@Override
				public void widgetSelected(final SelectionEvent se)
				{
					id.setType(identTypeCombo.getItem(identTypeCombo.getSelectionIndex()));
				}
			});

			if (id.getType() != null)
			{
				identTypeCombo.select(identTypeCombo.indexOf(id.getType()));
			}
			else
			{
				identTypeCombo.select(identTypeCombo.indexOf("Signatur")); //$NON-NLS-1$
			}

			Label label22 = new Label(identifierComp, SWT.NONE);
			label22.setText(NLMessages.getString("Editor_id")); //$NON-NLS-1$

			final Text ident = new Text(identifierComp, SWT.BORDER);
			ident.setEditable(_mayWrite);
			ident.setBackground(WHITE_COLOR);
			ident.setData("id", i); //$NON-NLS-1$
			ident.setLayoutData(_gridData);
			if (id.getIdentifier() != null)
			{
				ident.setText(id.getIdentifier().trim());
			}
			else
			{
				ident.setText(""); //$NON-NLS-1$
			}
			ident.addFocusListener(new FocusListener()
			{
				@Override
				public void focusGained(final FocusEvent e)
				{
					String[] vals = new String[]
					{"test", "test2"}; //$NON-NLS-1$ //$NON-NLS-2$
					try
					{
						vals = _mainSearcher.getFacets("reference", "identifier", null, null, null); //$NON-NLS-1$ //$NON-NLS-2$
					}
					catch (Exception e1)
					{

						e1.printStackTrace();
					}
					new AutoCompleteField(ident, new TextContentAdapter(), vals);
				}

				@Override
				public void focusLost(final FocusEvent e)
				{
					_currentReference.getIdentifiersMods().get((Integer) ident.getData("id")) //$NON-NLS-1$
							.setIdentifier(ident.getText());
				}
			});

			final Button deleteIds = new Button(identifierComp, SWT.PUSH);
			deleteIds.setText("-"); //$NON-NLS-1$
			deleteIds.setToolTipText(NLMessages.getString("Editor_remove_field"));
			deleteIds.setEnabled(_mayWrite);
			deleteIds.setLayoutData(_gridData);
			deleteIds.setData("id", i); //$NON-NLS-1$
			deleteIds.addSelectionListener(new SelectionAdapter()
			{
				@Override
				public void widgetSelected(final SelectionEvent event)
				{
					int index = (Integer) deleteIds.getData("id");
					if (_currentReference.getIdentifiersMods().size() > index)
					{
						_currentReference.getIdentifiersMods().removeElementAt(index); //$NON-NLS-1$
						loadValues(_currentReference);
					}

				}
			});
			deleteIds.setLayoutData(new GridData());

			// if (i == _currentReference.getIdentifiersMods().size() -1)
			// {
			// final Button addIdentifier = new Button(identifierComp,
			// SWT.PUSH);
			//		    	addIdentifier.setText("+"); //$NON-NLS-1$
			// addIdentifier.setToolTipText(NLMessages.getString("Editor_add_identifier"));
			// addIdentifier.setEnabled(_mayWrite);
			// addIdentifier.setLayoutData(_gridData);
			// addIdentifier.addSelectionListener(new SelectionAdapter()
			// {
			// public void widgetSelected(final SelectionEvent event)
			// {
			// IdentifierMods i = new IdentifierMods();
			//						i.setIdentifier(""); //$NON-NLS-1$
			//						i.setType(""); //$NON-NLS-1$
			// _currentReference.getIdentifiersMods().add(i);
			// loadValues(_currentReference);
			//
			//
			// } });
			// addIdentifier.setLayoutData(new GridData());
			// }
			// else{
			// new Label(identifierComp, SWT.NONE);
			// }
		}
	}

	/**
	 * Load location.
	 * @param contentComp the content comp
	 */
	private void loadLocation(final Composite contentComp)
	{
		Composite locationComp = new Composite(contentComp, SWT.NONE);
		locationComp.setLayoutData(new GridData());
		((GridData) locationComp.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) locationComp.getLayoutData()).grabExcessHorizontalSpace = true;
		locationComp.setLayout(new GridLayout());
		((GridLayout) locationComp.getLayout()).numColumns = 3;
		((GridLayout) locationComp.getLayout()).makeColumnsEqualWidth = false;

		LocationMods l = _currentReference.getLocation();

		if (l.getUrl() != null)
		{
			Label label25 = new Label(locationComp, SWT.NONE);
			label25.setText(NLMessages.getString("Editor_url")); //$NON-NLS-1$
			final Text url = new Text(locationComp, SWT.BORDER);
			url.setEditable(_mayWrite);
			url.setBackground(WHITE_COLOR);
			url.setLayoutData(_gridData);
			url.setText(l.getUrl().trim());
			url.addFocusListener(new FocusListener()
			{
				@Override
				public void focusGained(final FocusEvent e)
				{
					String[] vals = new String[]
					{"test", "test2"}; //$NON-NLS-1$ //$NON-NLS-2$
					try
					{
						vals = _mainSearcher.getFacets("reference", "url", null, null, null); //$NON-NLS-1$ //$NON-NLS-2$
					}
					catch (Exception e1)
					{

						e1.printStackTrace();
					}
					new AutoCompleteField(url, new TextContentAdapter(), vals);
				}

				@Override
				public void focusLost(final FocusEvent e)
				{
					_currentReference.getLocation().setUrl(url.getText());
				}
			});

			// final Button addDate = new Button(locationComp, SWT.PUSH);
			//			addDate.setText("+"); //$NON-NLS-1$
			// addDate.setToolTipText(NLMessages.getString("Editor_add_extra_location"));
			// addDate.setLayoutData(_gridData);
			// addDate.setEnabled(l.getPhysicalLocation() == null && _mayWrite);
			// addDate.addSelectionListener(new SelectionAdapter()
			// {
			// public void widgetSelected(final SelectionEvent event)
			// {
			//					_currentReference.getLocation().setPhysicalLocation(" "); //$NON-NLS-1$
			// loadValues(_currentReference);
			// } });
			// addDate.setLayoutData(new GridData());

			final Button delDate = new Button(locationComp, SWT.PUSH);
			delDate.setText("-"); //$NON-NLS-1$
			delDate.setToolTipText(NLMessages.getString("Editor_remove_field"));
			delDate.setEnabled(_mayWrite);
			delDate.setLayoutData(_gridData);
			delDate.addSelectionListener(new SelectionAdapter()
			{
				@Override
				public void widgetSelected(final SelectionEvent event)
				{
					_currentReference.getLocation().setUrl(null);
					loadValues(_currentReference);

				}
			});
			delDate.setLayoutData(new GridData());
		}
		if (l.getPhysicalLocation() != null)
		{
			Label label26 = new Label(locationComp, SWT.NONE);
			label26.setText(NLMessages.getString("Editor_physicalLocation")); //$NON-NLS-1$
			final Text physL = new Text(locationComp, SWT.BORDER);
			physL.setEditable(_mayWrite);
			physL.setBackground(WHITE_COLOR);
			physL.setLayoutData(_gridData);
			physL.setText(l.getPhysicalLocation().trim());
			physL.addFocusListener(new FocusListener()
			{
				@Override
				public void focusGained(final FocusEvent e)
				{
					String[] vals = new String[]
					{"test", "test2"}; //$NON-NLS-1$ //$NON-NLS-2$
					try
					{
						vals = _mainSearcher.getFacets("reference", "physicalLocation", null, null, null); //$NON-NLS-1$ //$NON-NLS-2$
					}
					catch (Exception e1)
					{

						e1.printStackTrace();
					}
					new AutoCompleteField(physL, new TextContentAdapter(), vals);
				}

				@Override
				public void focusLost(final FocusEvent e)
				{
					_currentReference.getLocation().setPhysicalLocation(physL.getText());
				}
			});

			// final Button addDate = new Button(locationComp, SWT.PUSH);
			//			addDate.setText("+"); //$NON-NLS-1$
			// addDate.setToolTipText(NLMessages.getString("Editor_add_shelf"));
			// addDate.setLayoutData(_gridData);
			// addDate.setEnabled(l.getShelfLocator() == null && _mayWrite);
			//
			// addDate.addSelectionListener(new SelectionAdapter()
			// {
			// public void widgetSelected(final SelectionEvent event)
			// {
			//					_currentReference.getLocation().setShelfLocator(" "); //$NON-NLS-1$
			// loadValues(_currentReference);
			// } });
			// addDate.setLayoutData(new GridData());

			final Button delDate = new Button(locationComp, SWT.PUSH);
			delDate.setText("-"); //$NON-NLS-1$
			delDate.setToolTipText(NLMessages.getString("Editor_remove_field"));
			delDate.setEnabled(_mayWrite);
			delDate.setLayoutData(_gridData);
			delDate.addSelectionListener(new SelectionAdapter()
			{
				@Override
				public void widgetSelected(final SelectionEvent event)
				{
					_currentReference.getLocation().setPhysicalLocation(null);
					loadValues(_currentReference);

				}
			});
			delDate.setLayoutData(new GridData());
		}
		if (l.getShelfLocator() != null)
		{
			Label label27 = new Label(locationComp, SWT.NONE);
			label27.setText(NLMessages.getString("Editor_shelfLocator")); //$NON-NLS-1$
			final Text shelfL = new Text(locationComp, SWT.BORDER);
			shelfL.setEditable(_mayWrite);
			shelfL.setBackground(WHITE_COLOR);
			shelfL.setLayoutData(_gridData);
			shelfL.setText(l.getShelfLocator().trim());
			shelfL.addFocusListener(new FocusListener()
			{
				@Override
				public void focusGained(final FocusEvent e)
				{
					String[] vals = new String[]
					{"test", "test2"}; //$NON-NLS-1$ //$NON-NLS-2$
					try
					{
						vals = _mainSearcher.getFacets("reference", "shelfLocator", null, null, null); //$NON-NLS-1$ //$NON-NLS-2$
					}
					catch (Exception e1)
					{

						e1.printStackTrace();
					}
					new AutoCompleteField(shelfL, new TextContentAdapter(), vals);
				}

				@Override
				public void focusLost(final FocusEvent e)
				{
					_currentReference.getLocation().setShelfLocator(shelfL.getText());
				}
			});

			// final Button addDate = new Button(locationComp, SWT.PUSH);
			//			addDate.setText("+"); //$NON-NLS-1$
			// addDate.setToolTipText(NLMessages.getString("Editor_add_url"));
			// addDate.setLayoutData(_gridData);
			// addDate.setEnabled(l.getUrl() == null && _mayWrite);
			// addDate.addSelectionListener(new SelectionAdapter()
			// {
			// public void widgetSelected(final SelectionEvent event)
			// {
			//					_currentReference.getLocation().setUrl(" "); //$NON-NLS-1$
			// loadValues(_currentReference);
			// } });
			// addDate.setLayoutData(new GridData());

			final Button delDate = new Button(locationComp, SWT.PUSH);
			delDate.setText("-"); //$NON-NLS-1$
			delDate.setToolTipText(NLMessages.getString("Editor_remove_field"));
			delDate.setEnabled(_mayWrite);
			delDate.addSelectionListener(new SelectionAdapter()
			{
				@Override
				public void widgetSelected(final SelectionEvent event)
				{
					_currentReference.getLocation().setShelfLocator(null);
					loadValues(_currentReference);

				}
			});
			delDate.setLayoutData(new GridData());
		}

	}

	/**
	 * Load names.
	 * @param contentComp the content comp
	 */
	private void loadNames(final Composite contentComp)
	{
		Composite namesComp = new Composite(contentComp, SWT.NONE);
		namesComp.setLayoutData(new GridData());
		((GridData) namesComp.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) namesComp.getLayoutData()).grabExcessHorizontalSpace = true;
		namesComp.setLayout(new GridLayout());
		((GridLayout) namesComp.getLayout()).numColumns = 5;
		((GridLayout) namesComp.getLayout()).makeColumnsEqualWidth = false;
		((GridLayout) namesComp.getLayout()).marginHeight = 0;
		((GridLayout) namesComp.getLayout()).marginWidth = 0;
		for (int i = 0; i < _currentReference.getNameMods().size(); i++)
		{
			final NameMods n = _currentReference.getNameMods().get(i);

			Label label5 = new Label(namesComp, SWT.NONE);
			label5.setText(NLMessages.getString("Editor_name") + NLMessages.getString("Editor_reference_role")); //$NON-NLS-1$

			final Combo roleC = new Combo(namesComp, SWT.READ_ONLY);
			roleC.setData("name", i); //$NON-NLS-1$
			roleC.setEnabled(_mayWrite);
			roleC.setBackground(WHITE_COLOR);
			ComboViewer comboViewer = new ComboViewer(roleC);
			comboViewer.setContentProvider(ArrayContentProvider.getInstance());
			comboViewer.setLabelProvider(new LabelProvider()
			{

				@Override
				public String getText(final Object element)
				{
					String str = (String) element;
					return NLMessages.getString("Editor_role_" + str); //$NON-NLS-1$
				}

			});

			comboViewer.setInput(AEConstants.REF_ROLETERM_CODE);
			comboViewer.addSelectionChangedListener(new ISelectionChangedListener()
			{

				@Override
				public void selectionChanged(final SelectionChangedEvent event)
				{
					ISelection selection = event.getSelection();
					Object obj = ((IStructuredSelection) selection).getFirstElement();
					String s = (String) obj;
					_currentReference.getNameMods().get((Integer) roleC.getData("name")) //$NON-NLS-1$
							.getRoleMods().setRoleTerm(s);
				}

			});

			if (n.getRoleMods() != null && n.getRoleMods().getRoleTerm() != null)
			{
				StructuredSelection selection = new StructuredSelection(n.getRoleMods().getRoleTerm());
				comboViewer.setSelection(selection);
			}
			else
			{
				StructuredSelection selection = new StructuredSelection("aut"); //$NON-NLS-1$
				comboViewer.setSelection(selection);
				n.getRoleMods().setRoleTerm("aut"); //$NON-NLS-1$

			}
			Composite namepartsComp = new Composite(namesComp, SWT.NONE);
			namepartsComp.setLayoutData(new GridData());
			((GridData) namepartsComp.getLayoutData()).horizontalAlignment = SWT.FILL;
			((GridData) namepartsComp.getLayoutData()).grabExcessHorizontalSpace = true;
			namepartsComp.setLayout(new GridLayout());
			((GridLayout) namepartsComp.getLayout()).numColumns = n.getNameParts().size() * 2;
			((GridLayout) namepartsComp.getLayout()).makeColumnsEqualWidth = false;
			((GridLayout) namepartsComp.getLayout()).marginHeight = 0;

			int num = n.getNameParts().size();
			for (int j = 0; j < num; j++)
			{
				final NamePart namePart = n.getNameParts().get(j);

				if (namePart.getType() != null)
				{
					Label label5d = new Label(namepartsComp, SWT.NONE);
					label5d.setText(NLMessages.getString("Editor_name_" + namePart.getType()) + ":"); //$NON-NLS-1$
					label5d.pack();
				}
				final Text name = new Text(namepartsComp, SWT.BORDER);
				name.setData("name", i); //$NON-NLS-1$
				name.setData("nPart", j); //$NON-NLS-1$
				name.setEditable(_mayWrite);
				name.setBackground(WHITE_COLOR);
				final ControlDecoration decoValName = new ControlDecoration(name, SWT.LEFT | SWT.TOP);

				name.setLayoutData(new GridData());
				((GridData) name.getLayoutData()).grabExcessHorizontalSpace = true;
				((GridData) name.getLayoutData()).horizontalAlignment = SWT.FILL;

				name.addFocusListener(new FocusListener()
				{
					@Override
					public void focusGained(final FocusEvent e)
					{
						String[] vals = new String[]
						{"test", "test2"}; //$NON-NLS-1$ //$NON-NLS-2$
						try
						{
							vals = _mainSearcher.getFacets("reference", "namePart", namePart.getType(), null, null); //$NON-NLS-1$ //$NON-NLS-2$
						}
						catch (Exception e1)
						{

							e1.printStackTrace();
						}
						new AutoCompleteField(name, new TextContentAdapter(), vals);
						validate();

					}

					@Override
					public void focusLost(final FocusEvent e)
					{
						namePart.setNamePart(name.getText());
						validate();
					}
				});
				name.addKeyListener(new KeyListener()
				{

					@Override
					public void keyPressed(final KeyEvent e)
					{
						// TODO Auto-generated method stub

					}

					@Override
					public void keyReleased(final KeyEvent e)
					{
						namePart.setNamePart(name.getText());
						validate();

					}
				});
				if (namePart.getNamePart() != null)
				{
					name.setText(namePart.getNamePart().trim());
				}
				if (_currentReference.getTitleInfo() != null && _currentReference.getTitleInfo().isValid()
						&& _currentReference.getNameMods() != null && !_currentReference.getNameMods().isEmpty())
				{

					decoValName.setImage(null);
				}
				else
				{
					decoValName.setImage(FieldDecorationRegistry.getDefault()
							.getFieldDecoration(FieldDecorationRegistry.DEC_REQUIRED).getImage());
				}
				// max 3 nameparts
				if (j == 2)
				{
					break;
				}
			}
			namepartsComp.layout();

			final Button addExtra = new Button(namesComp, SWT.PUSH);
			addExtra.setText("<+>"); //$NON-NLS-1$
			addExtra.setToolTipText(NLMessages.getString("Editor_add_extra_person"));
			addExtra.setLayoutData(_gridData);
			addExtra.setData("name", i); //$NON-NLS-1$
			addExtra.setEnabled((n.getAffiliation() == null && n.getDescription() == null) && _mayWrite);
			addExtra.addSelectionListener(new SelectionAdapter()
			{
				@Override
				public void widgetSelected(final SelectionEvent event)
				{
					_currentReference.getNameMods().get((Integer) addExtra.getData("name")).setType(" "); //$NON-NLS-1$ //$NON-NLS-2$
					_currentReference.getNameMods().get((Integer) addExtra.getData("name")).setAffiliation(" "); //$NON-NLS-1$ //$NON-NLS-2$
					_currentReference.getNameMods().get((Integer) addExtra.getData("name")).setDescription(" "); //$NON-NLS-1$ //$NON-NLS-2$

					loadValues(_currentReference);

				}
			});
			addExtra.setLayoutData(new GridData());

			final Button deleteName = new Button(namesComp, SWT.PUSH);
			deleteName.setText("-"); //$NON-NLS-1$
			deleteName.setToolTipText(NLMessages.getString("Editor_remove_field"));
			// deleteName.setEnabled(_mayWrite && _currentReference.isValid());
			deleteName.setLayoutData(_gridData);
			deleteName.setData("name", i); //$NON-NLS-1$
			deleteName.addSelectionListener(new SelectionAdapter()
			{
				@Override
				public void widgetSelected(final SelectionEvent event)
				{

					_currentReference.getNameMods().removeElementAt((Integer) deleteName.getData("name")); //$NON-NLS-1$
					loadValues(_currentReference);

				}
			});
			if ((_currentReference.getTitleInfo() != null && _currentReference.getTitleInfo().getTitle() != null)
					|| (_currentReference.getNameMods() != null && _currentReference.getNameMods().size() > 1))
			{
				deleteName.setEnabled(_mayWrite);
			}
			else
			{
				deleteName.setEnabled(false);
			}
			deleteName.setLayoutData(new GridData());

			// if (i == _currentReference.getNameMods().size() -1)
			// {
			// final Button addName = new Button(namesComp, SWT.PUSH);
			//				addName.setText("+"); //$NON-NLS-1$
			// addName.setToolTipText(NLMessages.getString("Editor_add_name"));
			// addName.setEnabled(_mayWrite);
			// addName.setLayoutData(_gridData);
			// addName.addSelectionListener(new SelectionAdapter()
			// {
			// public void widgetSelected(final SelectionEvent event)
			// {
			// _currentReference.getNameMods().add(new NameMods(2));
			// loadValues(_currentReference);
			//
			//
			// } });
			// addName.setLayoutData(new GridData());
			// }
			// else{
			// new Label(namesComp, SWT.NONE);
			// }

			if (n.getType() != null && n.getAffiliation() != null && n.getDescription() != null)
			{
				Composite namesComp2 = new Composite(namesComp, SWT.NONE);
				namesComp2.setLayoutData(new GridData());
				((GridData) namesComp2.getLayoutData()).horizontalAlignment = SWT.FILL;
				((GridData) namesComp2.getLayoutData()).grabExcessHorizontalSpace = true;
				((GridData) namesComp2.getLayoutData()).horizontalSpan = 9;

				namesComp2.setLayout(new GridLayout());
				((GridLayout) namesComp2.getLayout()).numColumns = 9;
				((GridLayout) namesComp2.getLayout()).makeColumnsEqualWidth = false;
				int span = 0;
				if (n.getType() != null)
				{
					Label label6 = new Label(namesComp2, SWT.NONE);
					label6.setText(NLMessages.getString("Editor_type")); //$NON-NLS-1$
					final Combo typeCombo = new Combo(namesComp2, SWT.READ_ONLY);
					typeCombo.setData("name", i); //$NON-NLS-1$
					typeCombo.setEnabled(_mayWrite);
					typeCombo.setBackground(WHITE_COLOR);
					typeCombo.setText(n.getType().trim());
					typeCombo.setLayoutData(new GridData());
					// ((GridData)
					// typeCombo.getLayoutData()).horizontalAlignment =
					// SWT.FILL;
					// ((GridData)
					// typeCombo.getLayoutData()).grabExcessHorizontalSpace =
					// true;
					((GridData) typeCombo.getLayoutData()).horizontalSpan = 1;
					ComboViewer typeComboViewer = new ComboViewer(typeCombo);
					typeComboViewer.setContentProvider(ArrayContentProvider.getInstance());
					typeComboViewer.setLabelProvider(new LabelProvider()
					{

						@Override
						public String getText(final Object element)
						{
							String str = (String) element;
							return NLMessages.getString("Editor_name_type_" + str); //$NON-NLS-1$
						}

					});

					typeComboViewer.setInput(AEConstants.REF_NAME_TYPE);
					typeComboViewer.addSelectionChangedListener(new ISelectionChangedListener()
					{

						@Override
						public void selectionChanged(final SelectionChangedEvent event)
						{
							ISelection selection = event.getSelection();
							Object obj = ((IStructuredSelection) selection).getFirstElement();
							String s = (String) obj;
							_currentReference.getNameMods().get((Integer) typeCombo.getData("name")) //$NON-NLS-1$
									.setType(s);
						}

					});
					if (n.getType().trim().length() > 0)
					{
						StructuredSelection selection = new StructuredSelection(n.getType());
						typeComboViewer.setSelection(selection);
					}
					else
					{
						StructuredSelection selection = new StructuredSelection("personal"); //$NON-NLS-1$
						typeComboViewer.setSelection(selection);
						_currentReference.getNameMods().get((Integer) typeCombo.getData("name")) //$NON-NLS-1$
								.setType("personal"); //$NON-NLS-1$
					}
					span = 2;
				}
				if (n.getAffiliation() != null)
				{
					Label label7 = new Label(namesComp2, SWT.NONE);
					label7.setText(NLMessages.getString("Editor_affiliation")); //$NON-NLS-1$
					final Text aff = new Text(namesComp2, SWT.BORDER);
					aff.setData("name", i); //$NON-NLS-1$
					aff.setEditable(_mayWrite);
					aff.setBackground(WHITE_COLOR);
					aff.setText(n.getAffiliation().trim());
					aff.setLayoutData(new GridData());
					((GridData) aff.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) aff.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) aff.getLayoutData()).horizontalSpan = 2;

					aff.addFocusListener(new FocusListener()
					{
						@Override
						public void focusGained(final FocusEvent e)
						{
							String[] vals = new String[]
							{"test2"}; //$NON-NLS-2$
							try
							{
								vals = _mainSearcher.getFacets("reference", "affiliation", null, null, null); //$NON-NLS-1$ //$NON-NLS-2$
							}
							catch (Exception e1)
							{

								e1.printStackTrace();
							}
							new AutoCompleteField(aff, new TextContentAdapter(), vals);
						}

						@Override
						public void focusLost(final FocusEvent e)
						{
							_currentReference.getNameMods().get((Integer) aff.getData("name")) //$NON-NLS-1$
									.setAffiliation(aff.getText());
						}
					});
					span = span + 3;

				}
				if (n.getDescription() != null)
				{
					Label label8 = new Label(namesComp2, SWT.NONE);
					label8.setText(NLMessages.getString("Editor_description")); //$NON-NLS-1$
					final Text desc = new Text(namesComp2, SWT.BORDER);
					desc.setData("name", i); //$NON-NLS-1$
					desc.setEditable(_mayWrite);
					desc.setBackground(WHITE_COLOR);
					desc.setText(n.getDescription().trim());
					desc.setLayoutData(new GridData());
					((GridData) desc.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) desc.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) desc.getLayoutData()).horizontalSpan = 2;
					desc.addFocusListener(new FocusListener()
					{
						@Override
						public void focusGained(final FocusEvent e)
						{
							String[] vals = new String[]
							{"test", "test2"}; //$NON-NLS-1$ //$NON-NLS-2$
							try
							{
								vals = _mainSearcher.getFacets("reference", "description", null, null, null); //$NON-NLS-1$ //$NON-NLS-2$
							}
							catch (Exception e1)
							{

								e1.printStackTrace();
							}
							new AutoCompleteField(desc, new TextContentAdapter(), vals);
						}

						@Override
						public void focusLost(final FocusEvent e)
						{
							_currentReference.getNameMods().get((Integer) desc.getData("name")) //$NON-NLS-1$
									.setDescription(desc.getText());
						}
					});
					span = span + 3;

				}
				if (span % 8 != 0)
				{
					Label bl = new Label(namesComp2, SWT.NONE);
					bl.setLayoutData(new GridData());
					((GridData) bl.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) bl.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) bl.getLayoutData()).horizontalSpan = 8 - span;
				}
				final Button delExtra = new Button(namesComp2, SWT.PUSH);
				delExtra.setText("-"); //$NON-NLS-1$
				delExtra.setToolTipText(NLMessages.getString("Editor_remove_name_extra"));
				delExtra.setEnabled(_mayWrite);
				delExtra.setLayoutData(_gridData);
				delExtra.setData("name", i); //$NON-NLS-1$

				delExtra.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent event)
					{
						_currentReference.getNameMods().get((Integer) addExtra.getData("name")).setType(null); //$NON-NLS-1$ //$NON-NLS-2$
						_currentReference.getNameMods().get((Integer) addExtra.getData("name")).setAffiliation(null); //$NON-NLS-1$ //$NON-NLS-2$
						_currentReference.getNameMods().get((Integer) addExtra.getData("name")).setDescription(null); //$NON-NLS-1$ //$NON-NLS-2$

						loadValues(_currentReference);

					}
				});
				delExtra.setLayoutData(new GridData());
			}

		}

	}

	/**
	 * Load note.
	 * @param contentComp the content comp
	 */
	private void loadNote(final Composite contentComp)
	{
		Composite noteComp = new Composite(contentComp, SWT.NONE);
		noteComp.setLayoutData(new GridData());
		((GridData) noteComp.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) noteComp.getLayoutData()).grabExcessHorizontalSpace = true;
		noteComp.setLayout(new GridLayout());
		((GridLayout) noteComp.getLayout()).numColumns = 3;
		((GridLayout) noteComp.getLayout()).makeColumnsEqualWidth = false;

		if (_currentReference.getNote().getNote() != null)
		{
			Label label17 = new Label(noteComp, SWT.None);
			label17.setText(NLMessages.getString("Editor_note")); //$NON-NLS-1$
			final Text note = new Text(noteComp, SWT.BORDER);
			note.setEditable(_mayWrite);
			note.setBackground(WHITE_COLOR);
			note.setLayoutData(_gridData);
			note.setText(_currentReference.getNote().getNote().trim());
			note.addFocusListener(new FocusListener()
			{
				@Override
				public void focusGained(final FocusEvent e)
				{
					String[] vals = new String[]
					{"test", "test2"}; //$NON-NLS-1$ //$NON-NLS-2$
					try
					{
						vals = _mainSearcher.getFacets("reference", "note", null, null, null); //$NON-NLS-1$ //$NON-NLS-2$
					}
					catch (Exception e1)
					{

						e1.printStackTrace();
					}
					new AutoCompleteField(note, new TextContentAdapter(), vals);
				}

				@Override
				public void focusLost(final FocusEvent e)
				{
					_currentReference.getNote().setNote(note.getText());
				}
			});
			final Button delNote = new Button(noteComp, SWT.PUSH);
			delNote.setText("-"); //$NON-NLS-1$
			delNote.setToolTipText(NLMessages.getString("Editor_remove_field"));
			delNote.setEnabled(_mayWrite);
			delNote.setLayoutData(_gridData);
			delNote.addSelectionListener(new SelectionAdapter()
			{
				@Override
				public void widgetSelected(final SelectionEvent event)
				{
					_currentReference.setNote(null); //$NON-NLS-1$
					loadValues(_currentReference);

				}
			});
			delNote.setLayoutData(new GridData());

		}
		if (_currentReference.getNote().getType() != null)
		{
			Label label18 = new Label(noteComp, SWT.None);
			label18.setText(NLMessages.getString("Editor_noteType")); //$NON-NLS-1$
			final Text nType = new Text(noteComp, SWT.BORDER);
			nType.setEditable(_mayWrite);
			nType.setBackground(WHITE_COLOR);
			nType.setLayoutData(_gridData);
			nType.setText(_currentReference.getNote().getType().trim());
			nType.addFocusListener(new FocusListener()
			{
				@Override
				public void focusGained(final FocusEvent e)
				{
					String[] vals = new String[]
					{"test", "test2"}; //$NON-NLS-1$ //$NON-NLS-2$
					try
					{
						vals = _mainSearcher.getFacets("reference", "note", "type", null, null); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					}
					catch (Exception e1)
					{

						e1.printStackTrace();
					}
					new AutoCompleteField(nType, new TextContentAdapter(), vals);
				}

				@Override
				public void focusLost(final FocusEvent e)
				{
					_currentReference.getNote().setType(nType.getText());
				}
			});

		}

	}

	/**
	 * Load origin info.
	 * @param contentComp the content comp
	 */
	private void loadOriginInfo(final Composite contentComp)
	{
		Composite originComp = new Composite(contentComp, SWT.NONE);
		originComp.setLayoutData(new GridData());
		((GridData) originComp.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) originComp.getLayoutData()).grabExcessHorizontalSpace = true;
		originComp.setLayout(new GridLayout());
		((GridLayout) originComp.getLayout()).numColumns = 9;
		((GridLayout) originComp.getLayout()).makeColumnsEqualWidth = false;

		OriginInfo oi = _currentReference.getOriginInfo();
		if (oi.getPlaceTerm() != null)
		{
			Label label13 = new Label(originComp, SWT.NONE);
			label13.setText(NLMessages.getString("Editor_place")); //$NON-NLS-1$
			final Text place = new Text(originComp, SWT.BORDER);
			place.setEditable(_mayWrite);
			place.setBackground(WHITE_COLOR);
			place.setLayoutData(new GridData());
			((GridData) place.getLayoutData()).horizontalAlignment = GridData.FILL;
			((GridData) place.getLayoutData()).grabExcessHorizontalSpace = true;
			((GridData) place.getLayoutData()).horizontalSpan = 7;
			place.setText(oi.getPlaceTerm().trim());
			place.addFocusListener(new FocusListener()
			{
				@Override
				public void focusGained(final FocusEvent e)
				{
					String[] vals = new String[]
					{"test", "test2"}; //$NON-NLS-1$ //$NON-NLS-2$
					try
					{
						vals = _mainSearcher.getFacets("reference", "placeTerm", "text", null, null); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					}
					catch (Exception e1)
					{

						e1.printStackTrace();
					}
					new AutoCompleteField(place, new TextContentAdapter(), vals);
				}

				@Override
				public void focusLost(final FocusEvent e)
				{
					_currentReference.getOriginInfo().setPlaceTerm(place.getText());
				}
			});

			final Button delDate = new Button(originComp, SWT.PUSH);
			delDate.setText("-"); //$NON-NLS-1$
			delDate.setToolTipText(NLMessages.getString("Editor_remove_field"));
			delDate.setEnabled(_mayWrite);
			delDate.setLayoutData(_gridData);
			delDate.addSelectionListener(new SelectionAdapter()
			{
				@Override
				public void widgetSelected(final SelectionEvent event)
				{
					_currentReference.getOriginInfo().setPlaceTerm(null);
					loadValues(_currentReference);

				}
			});
			delDate.setLayoutData(new GridData());
		}
		if (oi.getPublisher() != null)
		{
			Label label12 = new Label(originComp, SWT.NONE);
			label12.setText(NLMessages.getString("Editor_publisher")); //$NON-NLS-1$
			final Text pub = new Text(originComp, SWT.BORDER);
			pub.setEditable(_mayWrite);
			pub.setBackground(WHITE_COLOR);
			pub.setLayoutData(new GridData());
			((GridData) pub.getLayoutData()).horizontalAlignment = GridData.FILL;
			((GridData) pub.getLayoutData()).grabExcessHorizontalSpace = true;
			((GridData) pub.getLayoutData()).horizontalSpan = 7;

			pub.setText(oi.getPublisher().trim());
			pub.addFocusListener(new FocusListener()
			{
				@Override
				public void focusGained(final FocusEvent e)
				{
					String[] vals = new String[]
					{"test", "test2"}; //$NON-NLS-1$ //$NON-NLS-2$
					try
					{
						vals = _mainSearcher.getFacets("reference", "publisher", null, null, null); //$NON-NLS-1$ //$NON-NLS-2$
					}
					catch (Exception e1)
					{

						e1.printStackTrace();
					}
					new AutoCompleteField(pub, new TextContentAdapter(), vals);
				}

				@Override
				public void focusLost(final FocusEvent e)
				{
					_currentReference.getOriginInfo().setPublisher(pub.getText());
				}
			});

			final Button delDate = new Button(originComp, SWT.PUSH);
			delDate.setText("-"); //$NON-NLS-1$
			delDate.setToolTipText(NLMessages.getString("Editor_remove_field"));
			delDate.setEnabled(_mayWrite);
			delDate.setLayoutData(_gridData);
			delDate.addSelectionListener(new SelectionAdapter()
			{
				@Override
				public void widgetSelected(final SelectionEvent event)
				{
					_currentReference.getOriginInfo().setPublisher(null);
					loadValues(_currentReference);

				}
			});
			delDate.setLayoutData(new GridData());
		}

		if (oi.getDateCreated() != null)
		{
			Label label9 = new Label(originComp, SWT.NONE);
			label9.setText(NLMessages.getString("Editor_dateCreated")); //$NON-NLS-1$

			{
				Label labelDay = new Label(originComp, SWT.NONE);
				labelDay.setText(NLMessages.getString("Editor_day"));

				final Combo comboTimeDay = new Combo(originComp, SWT.READ_ONLY);
				comboTimeDay.setEnabled(_mayWrite);
				comboTimeDay.setBackground(WHITE_COLOR);
				comboTimeDay.setLayoutData(new GridData());
				((GridData) comboTimeDay.getLayoutData()).horizontalAlignment = GridData.FILL;
				((GridData) comboTimeDay.getLayoutData()).grabExcessHorizontalSpace = true;
				comboTimeDay.setItems(AEConstants.DAYS);
				comboTimeDay.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent se)
					{
						_currentReference.getOriginInfo().getDateCreated().setDay(comboTimeDay.getSelectionIndex());
					}
				});
				Label labelMonth = new Label(originComp, SWT.NONE);
				labelMonth.setText(NLMessages.getString("Editor_month"));

				final Combo comboTimeMonth = new Combo(originComp, SWT.READ_ONLY);
				comboTimeMonth.setEnabled(_mayWrite);
				comboTimeMonth.setBackground(WHITE_COLOR);
				comboTimeMonth.setLayoutData(new GridData());
				((GridData) comboTimeMonth.getLayoutData()).horizontalAlignment = GridData.FILL;
				((GridData) comboTimeMonth.getLayoutData()).grabExcessHorizontalSpace = true;
				comboTimeMonth.setItems(AEConstants.MONTHS);
				comboTimeMonth.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent se)
					{
						_currentReference.getOriginInfo().getDateCreated().setMonth(comboTimeMonth.getSelectionIndex());
					}
				});
				Label labelYear = new Label(originComp, SWT.NONE);
				labelYear.setText(NLMessages.getString("Editor_year"));

				final YearSpinner spinnerTimeYear = new YearSpinner(originComp, SWT.BORDER);
				spinnerTimeYear.setEnabled(_mayWrite);
				spinnerTimeYear.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent se)
					{
						_currentReference.getOriginInfo().getDateCreated().setYear(spinnerTimeYear.getSelection());
					}
				});
				if (oi.getDateCreated().getYear() > 0)
				{
					comboTimeDay.select(oi.getDateCreated().getDay());
					comboTimeMonth.select(oi.getDateCreated().getMonth());
					spinnerTimeYear.setSelection(oi.getDateCreated().getYear());
				}
				else
				{
					spinnerTimeYear.setSelection(0);
				}
			}

			final Button delDate = new Button(originComp, SWT.PUSH);
			delDate.setText("-"); //$NON-NLS-1$
			delDate.setToolTipText(NLMessages.getString("Editor_remove_date"));
			delDate.setEnabled(_mayWrite);
			delDate.setLayoutData(_gridData);
			delDate.addSelectionListener(new SelectionAdapter()
			{
				@Override
				public void widgetSelected(final SelectionEvent event)
				{
					_currentReference.getOriginInfo().setDateCreated(null);
					loadValues(_currentReference);

				}
			});
			delDate.setLayoutData(new GridData());

		}
		if (oi.getDateCreatedTimespan() != null)
		{
			Composite timespanComp = new Composite(contentComp, SWT.NONE);
			timespanComp.setLayoutData(new GridData());
			((GridData) timespanComp.getLayoutData()).horizontalAlignment = SWT.FILL;
			((GridData) timespanComp.getLayoutData()).grabExcessHorizontalSpace = true;
			((GridData) timespanComp.getLayoutData()).horizontalSpan = 9;

			timespanComp.setLayout(new GridLayout());
			((GridLayout) timespanComp.getLayout()).numColumns = 13;
			((GridLayout) timespanComp.getLayout()).makeColumnsEqualWidth = false;
			Label label9 = new Label(timespanComp, SWT.NONE);
			label9.setText(NLMessages.getString("Editor_dateCreated") + " " + NLMessages.getString("Editor_from")); //$NON-NLS-1$

			{

				Label labelMonth = new Label(timespanComp, SWT.NONE);
				labelMonth.setText(NLMessages.getString("Editor_month"));

				final Combo comboTimeMonth = new Combo(timespanComp, SWT.READ_ONLY);
				comboTimeMonth.setEnabled(_mayWrite);
				comboTimeMonth.setBackground(WHITE_COLOR);
				comboTimeMonth.setLayoutData(new GridData());
				((GridData) comboTimeMonth.getLayoutData()).horizontalAlignment = GridData.FILL;
				((GridData) comboTimeMonth.getLayoutData()).grabExcessHorizontalSpace = true;
				comboTimeMonth.setItems(AEConstants.MONTHS);
				comboTimeMonth.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent se)
					{
						_currentReference.getOriginInfo().getDateCreatedTimespan().getDateFrom()
								.setMonth(comboTimeMonth.getSelectionIndex());
					}
				});
				Label labelYear = new Label(timespanComp, SWT.NONE);
				labelYear.setText(NLMessages.getString("Editor_year"));

				final YearSpinner spinnerTimeYear = new YearSpinner(timespanComp, SWT.BORDER);
				spinnerTimeYear.setEnabled(_mayWrite);
				spinnerTimeYear.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent se)
					{
						_currentReference.getOriginInfo().getDateCreatedTimespan().getDateFrom()
								.setYear(spinnerTimeYear.getSelection());
					}
				});
				if (oi.getDateCreatedTimespan().getDateFrom() != null
						&& oi.getDateCreatedTimespan().getDateFrom().getYear() > 0)
				{
					comboTimeMonth.select(oi.getDateCreatedTimespan().getDateFrom().getMonth());
					spinnerTimeYear.setSelection(oi.getDateCreatedTimespan().getDateFrom().getYear());
				}
				else
				{
					spinnerTimeYear.setSelection(0);
				}
			}
			{
				Label labelTo = new Label(timespanComp, SWT.NONE);
				labelTo.setText(NLMessages.getString("Editor_to"));

				Label labelMonth = new Label(timespanComp, SWT.NONE);
				labelMonth.setText(NLMessages.getString("Editor_month"));

				final Combo comboTimeMonth = new Combo(timespanComp, SWT.READ_ONLY);
				comboTimeMonth.setEnabled(_mayWrite);
				comboTimeMonth.setBackground(WHITE_COLOR);
				comboTimeMonth.setLayoutData(new GridData());
				((GridData) comboTimeMonth.getLayoutData()).horizontalAlignment = GridData.FILL;
				((GridData) comboTimeMonth.getLayoutData()).grabExcessHorizontalSpace = true;
				comboTimeMonth.setItems(AEConstants.MONTHS);
				comboTimeMonth.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent se)
					{
						_currentReference.getOriginInfo().getDateCreatedTimespan().getDateTo()
								.setMonth(comboTimeMonth.getSelectionIndex());
					}
				});
				Label labelYear = new Label(timespanComp, SWT.NONE);
				labelYear.setText(NLMessages.getString("Editor_year"));

				final YearSpinner spinnerTimeYear = new YearSpinner(timespanComp, SWT.BORDER);
				spinnerTimeYear.setEnabled(_mayWrite);
				spinnerTimeYear.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent se)
					{
						_currentReference.getOriginInfo().getDateCreatedTimespan().getDateTo()
								.setYear(spinnerTimeYear.getSelection());
					}
				});
				if (oi.getDateCreatedTimespan().getDateTo() != null
						&& oi.getDateCreatedTimespan().getDateTo().getYear() > 0)
				{
					comboTimeMonth.select(oi.getDateCreatedTimespan().getDateTo().getMonth());
					spinnerTimeYear.setSelection(oi.getDateCreatedTimespan().getDateTo().getYear());
				}
				else
				{
					spinnerTimeYear.setSelection(0);
				}
			}

			final Button delDate = new Button(timespanComp, SWT.PUSH);
			delDate.setText("-"); //$NON-NLS-1$
			delDate.setToolTipText(NLMessages.getString("Editor_remove_date"));
			delDate.setEnabled(_mayWrite);
			delDate.setLayoutData(_gridData);
			delDate.addSelectionListener(new SelectionAdapter()
			{
				@Override
				public void widgetSelected(final SelectionEvent event)
				{
					_currentReference.getOriginInfo().setDateCreatedTimespan(null);
					loadValues(_currentReference);

				}
			});
			delDate.setLayoutData(new GridData());

		}
		if (oi.getDateIssued() != null)
		{
			Label label9 = new Label(originComp, SWT.NONE);
			label9.setText(NLMessages.getString("Editor_dateIssued"));

			{
				Label labelDay = new Label(originComp, SWT.NONE);
				labelDay.setText(NLMessages.getString("Editor_day"));

				final Combo comboTimeDay = new Combo(originComp, SWT.READ_ONLY);
				comboTimeDay.setEnabled(_mayWrite);
				comboTimeDay.setBackground(WHITE_COLOR);
				comboTimeDay.setLayoutData(new GridData());
				((GridData) comboTimeDay.getLayoutData()).horizontalAlignment = GridData.FILL;
				((GridData) comboTimeDay.getLayoutData()).grabExcessHorizontalSpace = true;
				comboTimeDay.setItems(AEConstants.DAYS);
				comboTimeDay.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent se)
					{
						_currentReference.getOriginInfo().getDateIssued().setDay(comboTimeDay.getSelectionIndex());
					}
				});
				Label labelMonth = new Label(originComp, SWT.NONE);
				labelMonth.setText(NLMessages.getString("Editor_month"));

				final Combo comboTimeMonth = new Combo(originComp, SWT.READ_ONLY);
				comboTimeMonth.setEnabled(_mayWrite);
				comboTimeMonth.setBackground(WHITE_COLOR);
				comboTimeMonth.setLayoutData(new GridData());
				((GridData) comboTimeMonth.getLayoutData()).horizontalAlignment = GridData.FILL;
				((GridData) comboTimeMonth.getLayoutData()).grabExcessHorizontalSpace = true;
				comboTimeMonth.setItems(AEConstants.MONTHS);
				comboTimeMonth.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent se)
					{
						_currentReference.getOriginInfo().getDateIssued().setMonth(comboTimeMonth.getSelectionIndex());
					}
				});
				Label labelYear = new Label(originComp, SWT.NONE);
				labelYear.setText(NLMessages.getString("Editor_year"));

				final YearSpinner spinnerTimeYear = new YearSpinner(originComp, SWT.BORDER);
				spinnerTimeYear.setEnabled(_mayWrite);

				spinnerTimeYear.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent se)
					{
						_currentReference.getOriginInfo().getDateIssued().setYear(spinnerTimeYear.getSelection());
					}
				});
				if (oi.getDateIssued().getYear() > 0)
				{
					comboTimeDay.select(oi.getDateIssued().getDay());
					comboTimeMonth.select(oi.getDateIssued().getMonth());
					spinnerTimeYear.setSelection(oi.getDateIssued().getYear());
				}
				else
				{
					spinnerTimeYear.setSelection(0);
				}
			}
			// final Button addDate = new Button(originComp, SWT.PUSH);
			//				addDate.setText("+"); //$NON-NLS-1$
			// addDate.setToolTipText(NLMessages.getString("Editor_date_capture"));
			// addDate.setLayoutData(_gridData);
			// addDate.setEnabled(oi.getDateCaptured() == null && _mayWrite);
			//
			// addDate.addSelectionListener(new SelectionAdapter()
			// {
			// public void widgetSelected(final SelectionEvent event)
			// {
			// _currentReference.getOriginInfo().setDateCaptured(new PdrDate(0,
			// 0, 0));
			// loadValues(_currentReference);
			// } });
			// addDate.setLayoutData(new GridData());

			final Button delDate = new Button(originComp, SWT.PUSH);
			delDate.setText("-"); //$NON-NLS-1$
			delDate.setToolTipText(NLMessages.getString("Editor_remove_date"));
			delDate.setEnabled(_mayWrite);
			delDate.setLayoutData(_gridData);
			delDate.addSelectionListener(new SelectionAdapter()
			{
				@Override
				public void widgetSelected(final SelectionEvent event)
				{
					_currentReference.getOriginInfo().setDateIssued(null);
					loadValues(_currentReference);

				}
			});
			delDate.setLayoutData(new GridData());

		}
		if (oi.getDateIssuedTimespan() != null)
		{
			Composite timespanComp = new Composite(contentComp, SWT.NONE);
			timespanComp.setLayoutData(new GridData());
			((GridData) timespanComp.getLayoutData()).horizontalAlignment = SWT.FILL;
			((GridData) timespanComp.getLayoutData()).grabExcessHorizontalSpace = true;
			((GridData) timespanComp.getLayoutData()).horizontalSpan = 9;

			timespanComp.setLayout(new GridLayout());
			((GridLayout) timespanComp.getLayout()).numColumns = 13;
			((GridLayout) timespanComp.getLayout()).makeColumnsEqualWidth = false;
			Label label9 = new Label(timespanComp, SWT.NONE);
			label9.setText(NLMessages.getString("Editor_dateIssued") + " " + NLMessages.getString("Editor_from")); //$NON-NLS-1$

			{

				Label labelMonth = new Label(timespanComp, SWT.NONE);
				labelMonth.setText(NLMessages.getString("Editor_month"));

				final Combo comboTimeMonth = new Combo(timespanComp, SWT.READ_ONLY);
				comboTimeMonth.setEnabled(_mayWrite);
				comboTimeMonth.setBackground(WHITE_COLOR);
				comboTimeMonth.setLayoutData(new GridData());
				((GridData) comboTimeMonth.getLayoutData()).horizontalAlignment = GridData.FILL;
				((GridData) comboTimeMonth.getLayoutData()).grabExcessHorizontalSpace = true;
				comboTimeMonth.setItems(AEConstants.MONTHS);
				comboTimeMonth.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent se)
					{
						_currentReference.getOriginInfo().getDateIssuedTimespan().getDateFrom()
								.setMonth(comboTimeMonth.getSelectionIndex());
					}
				});
				Label labelYear = new Label(timespanComp, SWT.NONE);
				labelYear.setText(NLMessages.getString("Editor_year"));

				final YearSpinner spinnerTimeYear = new YearSpinner(timespanComp, SWT.BORDER);
				spinnerTimeYear.setEnabled(_mayWrite);
				spinnerTimeYear.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent se)
					{
						_currentReference.getOriginInfo().getDateIssuedTimespan().getDateFrom()
								.setYear(spinnerTimeYear.getSelection());
					}
				});
				if (oi.getDateIssuedTimespan().getDateFrom() != null
						&& oi.getDateIssuedTimespan().getDateFrom().getYear() > 0)
				{
					comboTimeMonth.select(oi.getDateIssuedTimespan().getDateFrom().getMonth());
					spinnerTimeYear.setSelection(oi.getDateIssuedTimespan().getDateFrom().getYear());
				}
				else
				{
					spinnerTimeYear.setSelection(0);
				}
			}
			{
				Label labelTo = new Label(timespanComp, SWT.NONE);
				labelTo.setText(NLMessages.getString("Editor_to"));

				Label labelMonth = new Label(timespanComp, SWT.NONE);
				labelMonth.setText(NLMessages.getString("Editor_month"));

				final Combo comboTimeMonth = new Combo(timespanComp, SWT.READ_ONLY);
				comboTimeMonth.setEnabled(_mayWrite);
				comboTimeMonth.setBackground(WHITE_COLOR);
				comboTimeMonth.setLayoutData(new GridData());
				((GridData) comboTimeMonth.getLayoutData()).horizontalAlignment = GridData.FILL;
				((GridData) comboTimeMonth.getLayoutData()).grabExcessHorizontalSpace = true;
				comboTimeMonth.setItems(AEConstants.MONTHS);
				comboTimeMonth.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent se)
					{
						_currentReference.getOriginInfo().getDateIssuedTimespan().getDateTo()
								.setMonth(comboTimeMonth.getSelectionIndex());
					}
				});
				Label labelYear = new Label(timespanComp, SWT.NONE);
				labelYear.setText(NLMessages.getString("Editor_year"));

				final YearSpinner spinnerTimeYear = new YearSpinner(timespanComp, SWT.BORDER);
				spinnerTimeYear.setEnabled(_mayWrite);
				spinnerTimeYear.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent se)
					{
						_currentReference.getOriginInfo().getDateIssuedTimespan().getDateTo()
								.setYear(spinnerTimeYear.getSelection());
					}
				});
				if (oi.getDateIssuedTimespan().getDateTo() != null
						&& oi.getDateIssuedTimespan().getDateTo().getYear() > 0)
				{
					comboTimeMonth.select(oi.getDateIssuedTimespan().getDateTo().getMonth());
					spinnerTimeYear.setSelection(oi.getDateIssuedTimespan().getDateTo().getYear());
				}
				else
				{
					spinnerTimeYear.setSelection(0);
				}
			}

			final Button delDate = new Button(timespanComp, SWT.PUSH);
			delDate.setText("-"); //$NON-NLS-1$
			delDate.setToolTipText(NLMessages.getString("Editor_remove_date"));
			delDate.setEnabled(_mayWrite);
			delDate.setLayoutData(_gridData);
			delDate.addSelectionListener(new SelectionAdapter()
			{
				@Override
				public void widgetSelected(final SelectionEvent event)
				{
					_currentReference.getOriginInfo().setDateIssuedTimespan(null);
					loadValues(_currentReference);

				}
			});
			delDate.setLayoutData(new GridData());
		}
		if (oi.getDateCaptured() != null)
		{
			Label label10 = new Label(originComp, SWT.NONE);
			label10.setText(NLMessages.getString("Editor_dateCaptured")); //$NON-NLS-1$
			{
				Label labelDay = new Label(originComp, SWT.NONE);
				labelDay.setText(NLMessages.getString("Editor_day"));
				final Combo comboTimeDayCap = new Combo(originComp, SWT.READ_ONLY);
				comboTimeDayCap.setEnabled(_mayWrite);
				comboTimeDayCap.setBackground(WHITE_COLOR);
				comboTimeDayCap.setLayoutData(new GridData());
				((GridData) comboTimeDayCap.getLayoutData()).horizontalAlignment = GridData.FILL;
				((GridData) comboTimeDayCap.getLayoutData()).grabExcessHorizontalSpace = true;
				comboTimeDayCap.setItems(AEConstants.DAYS);
				comboTimeDayCap.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent se)
					{
						_currentReference.getOriginInfo().getDateCaptured().setDay(comboTimeDayCap.getSelectionIndex());
					}
				});
				Label labelMonth = new Label(originComp, SWT.NONE);
				labelMonth.setText(NLMessages.getString("Editor_month"));
				final Combo comboTimeMonthCap = new Combo(originComp, SWT.READ_ONLY);
				comboTimeMonthCap.setEnabled(_mayWrite);
				comboTimeMonthCap.setBackground(WHITE_COLOR);
				comboTimeMonthCap.setLayoutData(new GridData());
				((GridData) comboTimeMonthCap.getLayoutData()).horizontalAlignment = GridData.FILL;
				((GridData) comboTimeMonthCap.getLayoutData()).grabExcessHorizontalSpace = true;
				comboTimeMonthCap.setItems(AEConstants.MONTHS);
				comboTimeMonthCap.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent se)
					{
						_currentReference.getOriginInfo().getDateCaptured()
								.setMonth(comboTimeMonthCap.getSelectionIndex());
					}
				});
				Label labelYear = new Label(originComp, SWT.NONE);
				labelYear.setText(NLMessages.getString("Editor_year"));
				final YearSpinner spinnerTimeYearCap = new YearSpinner(originComp, SWT.BORDER);
				spinnerTimeYearCap.setEnabled(_mayWrite);

				spinnerTimeYearCap.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent se)
					{
						_currentReference.getOriginInfo().getDateCaptured().setYear(spinnerTimeYearCap.getSelection());
					}
				});
				if (oi.getDateCaptured().getYear() > 0)
				{
					comboTimeDayCap.select(oi.getDateCaptured().getDay());
					comboTimeMonthCap.select(oi.getDateCaptured().getMonth());
					spinnerTimeYearCap.setSelection(oi.getDateCaptured().getYear());
				}
				else
				{
					spinnerTimeYearCap.setSelection(0);
				}
			}
			// final Button addDate = new Button(originComp, SWT.PUSH);
			//				addDate.setText("+"); //$NON-NLS-1$
			// addDate.setToolTipText(NLMessages.getString("Editor_add_date_copyright"));
			// addDate.setLayoutData(_gridData);
			// addDate.setEnabled(oi.getCopyrightDate() == null && _mayWrite);
			// addDate.addSelectionListener(new SelectionAdapter()
			// {
			// public void widgetSelected(final SelectionEvent event)
			// {
			// _currentReference.getOriginInfo().setCopyrightDate(new PdrDate(0,
			// 0, 0));
			// loadValues(_currentReference);
			//
			// } });
			// addDate.setLayoutData(new GridData());

			final Button delDate = new Button(originComp, SWT.PUSH);
			delDate.setText("-"); //$NON-NLS-1$
			delDate.setToolTipText(NLMessages.getString("Editor_remove_date"));
			delDate.setEnabled(_mayWrite);
			delDate.setLayoutData(_gridData);
			delDate.addSelectionListener(new SelectionAdapter()
			{
				@Override
				public void widgetSelected(final SelectionEvent event)
				{
					_currentReference.getOriginInfo().setDateCaptured(null);
					loadValues(_currentReference);

				}
			});
			delDate.setLayoutData(new GridData());

		}
		if (oi.getDateCapturedTimespan() != null)
		{
			Composite timespanComp = new Composite(contentComp, SWT.NONE);
			timespanComp.setLayoutData(new GridData());
			((GridData) timespanComp.getLayoutData()).horizontalAlignment = SWT.FILL;
			((GridData) timespanComp.getLayoutData()).grabExcessHorizontalSpace = true;
			((GridData) timespanComp.getLayoutData()).horizontalSpan = 9;

			timespanComp.setLayout(new GridLayout());
			((GridLayout) timespanComp.getLayout()).numColumns = 13;
			((GridLayout) timespanComp.getLayout()).makeColumnsEqualWidth = false;
			Label label9 = new Label(timespanComp, SWT.NONE);
			label9.setText(NLMessages.getString("Editor_dateCaptured") + " " + NLMessages.getString("Editor_from")); //$NON-NLS-1$

			{

				Label labelMonth = new Label(timespanComp, SWT.NONE);
				labelMonth.setText(NLMessages.getString("Editor_month"));

				final Combo comboTimeMonth = new Combo(timespanComp, SWT.READ_ONLY);
				comboTimeMonth.setEnabled(_mayWrite);
				comboTimeMonth.setBackground(WHITE_COLOR);
				comboTimeMonth.setLayoutData(new GridData());
				((GridData) comboTimeMonth.getLayoutData()).horizontalAlignment = GridData.FILL;
				((GridData) comboTimeMonth.getLayoutData()).grabExcessHorizontalSpace = true;
				comboTimeMonth.setItems(AEConstants.MONTHS);
				comboTimeMonth.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent se)
					{
						_currentReference.getOriginInfo().getDateCapturedTimespan().getDateFrom()
								.setMonth(comboTimeMonth.getSelectionIndex());
					}
				});
				Label labelYear = new Label(timespanComp, SWT.NONE);
				labelYear.setText(NLMessages.getString("Editor_year"));

				final YearSpinner spinnerTimeYear = new YearSpinner(timespanComp, SWT.BORDER);
				spinnerTimeYear.setEnabled(_mayWrite);
				spinnerTimeYear.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent se)
					{
						_currentReference.getOriginInfo().getDateCapturedTimespan().getDateFrom()
								.setYear(spinnerTimeYear.getSelection());
					}
				});
				if (oi.getDateCapturedTimespan().getDateFrom() != null
						&& oi.getDateCapturedTimespan().getDateFrom().getYear() > 0)
				{
					comboTimeMonth.select(oi.getDateCapturedTimespan().getDateFrom().getMonth());
					spinnerTimeYear.setSelection(oi.getDateCapturedTimespan().getDateFrom().getYear());
				}
				else
				{
					spinnerTimeYear.setSelection(0);
				}
			}
			{
				Label labelTo = new Label(timespanComp, SWT.NONE);
				labelTo.setText(NLMessages.getString("Editor_to"));

				Label labelMonth = new Label(timespanComp, SWT.NONE);
				labelMonth.setText(NLMessages.getString("Editor_month"));

				final Combo comboTimeMonth = new Combo(timespanComp, SWT.READ_ONLY);
				comboTimeMonth.setEnabled(_mayWrite);
				comboTimeMonth.setBackground(WHITE_COLOR);
				comboTimeMonth.setLayoutData(new GridData());
				((GridData) comboTimeMonth.getLayoutData()).horizontalAlignment = GridData.FILL;
				((GridData) comboTimeMonth.getLayoutData()).grabExcessHorizontalSpace = true;
				comboTimeMonth.setItems(AEConstants.MONTHS);
				comboTimeMonth.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent se)
					{
						_currentReference.getOriginInfo().getDateCapturedTimespan().getDateTo()
								.setMonth(comboTimeMonth.getSelectionIndex());
					}
				});
				Label labelYear = new Label(timespanComp, SWT.NONE);
				labelYear.setText(NLMessages.getString("Editor_year"));

				final YearSpinner spinnerTimeYear = new YearSpinner(timespanComp, SWT.BORDER);
				spinnerTimeYear.setEnabled(_mayWrite);
				spinnerTimeYear.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent se)
					{
						_currentReference.getOriginInfo().getDateCapturedTimespan().getDateTo()
								.setYear(spinnerTimeYear.getSelection());
					}
				});
				if (oi.getDateCapturedTimespan().getDateTo() != null
						&& oi.getDateCapturedTimespan().getDateTo().getYear() > 0)
				{
					comboTimeMonth.select(oi.getDateCapturedTimespan().getDateTo().getMonth());
					spinnerTimeYear.setSelection(oi.getDateCapturedTimespan().getDateTo().getYear());
				}
				else
				{
					spinnerTimeYear.setSelection(0);
				}
			}

			final Button delDate = new Button(timespanComp, SWT.PUSH);
			delDate.setText("-"); //$NON-NLS-1$
			delDate.setToolTipText(NLMessages.getString("Editor_remove_date"));
			delDate.setEnabled(_mayWrite);
			delDate.setLayoutData(_gridData);
			delDate.addSelectionListener(new SelectionAdapter()
			{
				@Override
				public void widgetSelected(final SelectionEvent event)
				{
					_currentReference.getOriginInfo().setDateCapturedTimespan(null);
					loadValues(_currentReference);

				}
			});
			delDate.setLayoutData(new GridData());
		}
		if (oi.getCopyrightDate() != null)
		{
			Label label11 = new Label(originComp, SWT.NONE);
			label11.setText(NLMessages.getString("Editor_copyrightDate")); //$NON-NLS-1$
			{
				Label labelDay = new Label(originComp, SWT.NONE);
				labelDay.setText(NLMessages.getString("Editor_day"));
				final Combo comboTimeDayCop = new Combo(originComp, SWT.READ_ONLY);
				comboTimeDayCop.setEnabled(_mayWrite);
				comboTimeDayCop.setBackground(WHITE_COLOR);
				comboTimeDayCop.setLayoutData(new GridData());
				((GridData) comboTimeDayCop.getLayoutData()).horizontalAlignment = GridData.FILL;
				((GridData) comboTimeDayCop.getLayoutData()).grabExcessHorizontalSpace = true;
				comboTimeDayCop.setItems(AEConstants.DAYS);
				comboTimeDayCop.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent se)
					{
						_currentReference.getOriginInfo().getCopyrightDate()
								.setDay(comboTimeDayCop.getSelectionIndex());
					}
				});
				Label labelMonth = new Label(originComp, SWT.NONE);
				labelMonth.setText(NLMessages.getString("Editor_month"));
				final Combo comboTimeMonthCop = new Combo(originComp, SWT.READ_ONLY);
				comboTimeMonthCop.setEnabled(_mayWrite);
				comboTimeMonthCop.setBackground(WHITE_COLOR);
				comboTimeMonthCop.setLayoutData(new GridData());
				((GridData) comboTimeMonthCop.getLayoutData()).horizontalAlignment = GridData.FILL;
				((GridData) comboTimeMonthCop.getLayoutData()).grabExcessHorizontalSpace = true;
				comboTimeMonthCop.setItems(AEConstants.MONTHS);
				comboTimeMonthCop.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent se)
					{
						_currentReference.getOriginInfo().getCopyrightDate()
								.setMonth(comboTimeMonthCop.getSelectionIndex());
					}
				});
				Label labelYear = new Label(originComp, SWT.NONE);
				labelYear.setText(NLMessages.getString("Editor_year"));
				final YearSpinner spinnerTimeYearCop = new YearSpinner(originComp, SWT.BORDER);
				spinnerTimeYearCop.setEnabled(_mayWrite);

				spinnerTimeYearCop.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent se)
					{
						_currentReference.getOriginInfo().getCopyrightDate().setYear(spinnerTimeYearCop.getSelection());
					}
				});
				if (oi.getCopyrightDate().getYear() > 0)
				{
					comboTimeDayCop.select(oi.getCopyrightDate().getDay());
					comboTimeMonthCop.select(oi.getCopyrightDate().getMonth());
					spinnerTimeYearCop.setSelection(oi.getCopyrightDate().getYear());
				}
				else
				{
					spinnerTimeYearCop.setSelection(0);
				}
			}
			// final Button addDate = new Button(originComp, SWT.PUSH);
			//				addDate.setText("+"); //$NON-NLS-1$
			// addDate.setToolTipText(NLMessages.getString("Editor_add_date_creation"));
			// addDate.setLayoutData(_gridData);
			// addDate.setEnabled(oi.getDateCreated() == null && _mayWrite);
			//
			// addDate.addSelectionListener(new SelectionAdapter()
			// {
			// public void widgetSelected(final SelectionEvent event)
			// {
			// _currentReference.getOriginInfo().setDateCreated(new PdrDate(0,
			// 0, 0));
			// loadValues(_currentReference);
			// } });
			// addDate.setLayoutData(new GridData());

			final Button delDate = new Button(originComp, SWT.PUSH);
			delDate.setText("-"); //$NON-NLS-1$
			delDate.setToolTipText(NLMessages.getString("Editor_remove_date"));
			delDate.setEnabled(_mayWrite);
			delDate.setLayoutData(_gridData);
			delDate.addSelectionListener(new SelectionAdapter()
			{
				@Override
				public void widgetSelected(final SelectionEvent event)
				{
					_currentReference.getOriginInfo().setCopyrightDate(null);
					loadValues(_currentReference);
				}
			});
			delDate.setLayoutData(new GridData());

		}
		if (oi.getCopyrightDateTimespan() != null)
		{
			Composite timespanComp = new Composite(contentComp, SWT.NONE);
			timespanComp.setLayoutData(new GridData());
			((GridData) timespanComp.getLayoutData()).horizontalAlignment = SWT.FILL;
			((GridData) timespanComp.getLayoutData()).grabExcessHorizontalSpace = true;
			((GridData) timespanComp.getLayoutData()).horizontalSpan = 9;

			timespanComp.setLayout(new GridLayout());
			((GridLayout) timespanComp.getLayout()).numColumns = 13;
			((GridLayout) timespanComp.getLayout()).makeColumnsEqualWidth = false;
			Label label9 = new Label(timespanComp, SWT.NONE);
			label9.setText(NLMessages.getString("Editor_copyrightDate") + " " + NLMessages.getString("Editor_from")); //$NON-NLS-1$

			{

				Label labelMonth = new Label(timespanComp, SWT.NONE);
				labelMonth.setText(NLMessages.getString("Editor_month"));

				final Combo comboTimeMonth = new Combo(timespanComp, SWT.READ_ONLY);
				comboTimeMonth.setEnabled(_mayWrite);
				comboTimeMonth.setBackground(WHITE_COLOR);
				comboTimeMonth.setLayoutData(new GridData());
				((GridData) comboTimeMonth.getLayoutData()).horizontalAlignment = GridData.FILL;
				((GridData) comboTimeMonth.getLayoutData()).grabExcessHorizontalSpace = true;
				comboTimeMonth.setItems(AEConstants.MONTHS);
				comboTimeMonth.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent se)
					{
						_currentReference.getOriginInfo().getCopyrightDateTimespan().getDateFrom()
								.setMonth(comboTimeMonth.getSelectionIndex());
					}
				});
				Label labelYear = new Label(timespanComp, SWT.NONE);
				labelYear.setText(NLMessages.getString("Editor_year"));

				final YearSpinner spinnerTimeYear = new YearSpinner(timespanComp, SWT.BORDER);
				spinnerTimeYear.setEnabled(_mayWrite);
				spinnerTimeYear.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent se)
					{
						_currentReference.getOriginInfo().getCopyrightDateTimespan().getDateFrom()
								.setYear(spinnerTimeYear.getSelection());
					}
				});
				if (oi.getCopyrightDateTimespan().getDateFrom() != null
						&& oi.getCopyrightDateTimespan().getDateFrom().getYear() > 0)
				{
					comboTimeMonth.select(oi.getCopyrightDateTimespan().getDateFrom().getMonth());
					spinnerTimeYear.setSelection(oi.getCopyrightDateTimespan().getDateFrom().getYear());
				}
				else
				{
					spinnerTimeYear.setSelection(0);
				}
			}
			{
				Label labelTo = new Label(timespanComp, SWT.NONE);
				labelTo.setText(NLMessages.getString("Editor_to"));

				Label labelMonth = new Label(timespanComp, SWT.NONE);
				labelMonth.setText(NLMessages.getString("Editor_month"));

				final Combo comboTimeMonth = new Combo(timespanComp, SWT.READ_ONLY);
				comboTimeMonth.setEnabled(_mayWrite);
				comboTimeMonth.setBackground(WHITE_COLOR);
				comboTimeMonth.setLayoutData(new GridData());
				((GridData) comboTimeMonth.getLayoutData()).horizontalAlignment = GridData.FILL;
				((GridData) comboTimeMonth.getLayoutData()).grabExcessHorizontalSpace = true;
				comboTimeMonth.setItems(AEConstants.MONTHS);
				comboTimeMonth.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent se)
					{
						_currentReference.getOriginInfo().getCopyrightDateTimespan().getDateTo()
								.setMonth(comboTimeMonth.getSelectionIndex());
					}
				});
				Label labelYear = new Label(timespanComp, SWT.NONE);
				labelYear.setText(NLMessages.getString("Editor_year"));

				final YearSpinner spinnerTimeYear = new YearSpinner(timespanComp, SWT.BORDER);
				spinnerTimeYear.setEnabled(_mayWrite);
				spinnerTimeYear.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent se)
					{
						_currentReference.getOriginInfo().getCopyrightDateTimespan().getDateTo()
								.setYear(spinnerTimeYear.getSelection());
					}
				});
				if (oi.getCopyrightDateTimespan().getDateTo() != null
						&& oi.getCopyrightDateTimespan().getDateTo().getYear() > 0)
				{
					comboTimeMonth.select(oi.getCopyrightDateTimespan().getDateTo().getMonth());
					spinnerTimeYear.setSelection(oi.getCopyrightDateTimespan().getDateTo().getYear());
				}
				else
				{
					spinnerTimeYear.setSelection(0);
				}
			}

			final Button delDate = new Button(timespanComp, SWT.PUSH);
			delDate.setText("-"); //$NON-NLS-1$
			delDate.setToolTipText(NLMessages.getString("Editor_remove_date"));
			delDate.setEnabled(_mayWrite);
			delDate.setLayoutData(_gridData);
			delDate.addSelectionListener(new SelectionAdapter()
			{
				@Override
				public void widgetSelected(final SelectionEvent event)
				{
					_currentReference.getOriginInfo().setCopyrightDateTimespan(null);
					loadValues(_currentReference);

				}
			});
			delDate.setLayoutData(new GridData());
		}

		if (oi.getEdition() != null)
		{
			Label label15 = new Label(originComp, SWT.NONE);
			label15.setText(NLMessages.getString("Editor_edition")); //$NON-NLS-1$
			final Text edition = new Text(originComp, SWT.BORDER);
			edition.setEditable(_mayWrite);
			edition.setBackground(WHITE_COLOR);
			edition.setLayoutData(new GridData());
			((GridData) edition.getLayoutData()).horizontalAlignment = GridData.FILL;
			((GridData) edition.getLayoutData()).grabExcessHorizontalSpace = true;
			((GridData) edition.getLayoutData()).horizontalSpan = 7;
			edition.setText(oi.getEdition().trim());
			edition.addFocusListener(new FocusListener()
			{
				@Override
				public void focusGained(final FocusEvent e)
				{
					String[] vals = new String[]
					{"test", "test2"}; //$NON-NLS-1$ //$NON-NLS-2$
					try
					{
						vals = _mainSearcher.getFacets("reference", "edition", null, null, null); //$NON-NLS-1$ //$NON-NLS-2$
					}
					catch (Exception e1)
					{
						e1.printStackTrace();
					}
					new AutoCompleteField(edition, new TextContentAdapter(), vals);
				}

				@Override
				public void focusLost(final FocusEvent e)
				{
					_currentReference.getOriginInfo().setEdition(edition.getText());
				}
			});

			final Button delDate = new Button(originComp, SWT.PUSH);
			delDate.setText("-"); //$NON-NLS-1$
			delDate.setToolTipText(NLMessages.getString("Editor_remove_field"));
			delDate.setEnabled(_mayWrite);
			delDate.setLayoutData(_gridData);
			delDate.addSelectionListener(new SelectionAdapter()
			{
				@Override
				public void widgetSelected(final SelectionEvent event)
				{
					_currentReference.getOriginInfo().setEdition(null);
					loadValues(_currentReference);

				}
			});
			delDate.setLayoutData(new GridData());
		}

	}

	/**
	 * Load reference.
	 */
	private void loadReference()
	{
		// if (_facade.getCurrentReference() != null)
		// {
		// _currentReference = (ReferenceMods)
		// _facade.getCurrentReference().clone();
		_buttonNew.setEnabled(false);
		Revision revision = new Revision();
		revision.setRevisor(new String(_facade.getCurrentUser().getDisplayName()));
		revision.setTimeStamp(_facade.getCurrentDate());
		revision.setAuthority(_facade.getCurrentUser().getPdrId().clone());
		if (_currentReference.isNew())
		{
			revision.setRef(0);
			Record record = new Record();
			record.getRevisions().add(revision);
			_currentReference.setRecord(record);
		}

		_mayWrite = new UserRichtsChecker().mayWrite(_currentReference);
		if (!_mayWrite)
		{
			setMessage(NLMessages.getString("Editor_message_no_writing"));
		}
		loadValues(_currentReference);
		_sourceGenreCombo.setEnabled(_mayWrite);
		// }
	}

	/**
	 * Load ref format.
	 * @param reference the reference
	 */
	private void loadRefFormat(final ReferenceMods reference)
	{
		_currentReference = reference.clone();
		_currentReference.setPdrId(_newReferenceId);
		_currentReference.setNew(true);
		Revision revision = new Revision();
		revision.setRevisor(new String(_facade.getCurrentUser().getDisplayName()));
		revision.setTimeStamp(_facade.getCurrentDate());
		revision.setAuthority(_facade.getCurrentUser().getPdrId().clone());
		if (_currentReference.isNew())
		{
			revision.setRef(0);
			Record record = new Record();
			record.getRevisions().add(revision);
			_currentReference.setRecord(record);
		}

		if (_currentReference.getGenre() == null)
		{
			_currentReference.setGenre(new Genre());
		}
		_currentReference.getGenre().setAuthority("PDR");
		IStructuredSelection selection = (IStructuredSelection) _sourceGenreComboViewer.getSelection();
		Object obj = selection.getFirstElement();
		ReferenceModsTemplate template = (ReferenceModsTemplate) obj;
		_currentReference.getGenre().setGenre(template.getValue());
		_buttonNew.setEnabled(false);
		_mayWrite = true;
		_savebutton.setEnabled(_mayWrite);

		if (_superReference != null)
		{
			RelatedItem relItem = new RelatedItem();
			relItem.setType("host"); //$NON-NLS-1$
			relItem.setId(_superReference.toString());
			if (_currentReference.getRelatedItems() == null)
			{
				_currentReference.setRelatedItems(new Vector<RelatedItem>(1));
			}
			_currentReference.getRelatedItems().add(relItem); //$NON-NLS-1$
		}
		loadValues(_currentReference);

	}

	/**
	 * Load related items.
	 * @param contentComp the content comp
	 */
	private void loadRelatedItems(final Composite contentComp)
	{
		Composite relatedItemComp = new Composite(contentComp, SWT.NONE);
		relatedItemComp.setLayoutData(new GridData());
		((GridData) relatedItemComp.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) relatedItemComp.getLayoutData()).grabExcessHorizontalSpace = true;
		relatedItemComp.setLayout(new GridLayout());
		((GridLayout) relatedItemComp.getLayout()).numColumns = 10;
		((GridLayout) relatedItemComp.getLayout()).makeColumnsEqualWidth = false;

		for (int i = 0; i < _currentReference.getRelatedItems().size(); i++)
		{
			final RelatedItem relItem = _currentReference.getRelatedItems().get(i);

			Label label20 = new Label(relatedItemComp, SWT.NONE);
			label20.setText(NLMessages.getString("Editor_part_in"));

			final Text sourceText = new Text(relatedItemComp, SWT.BORDER);
			sourceText.setEditable(_mayWrite);
			sourceText.setBackground(WHITE_COLOR);
			sourceText.setLayoutData(new GridData());
			((GridData) sourceText.getLayoutData()).horizontalAlignment = SWT.FILL;
			((GridData) sourceText.getLayoutData()).grabExcessHorizontalSpace = true;
			((GridData) sourceText.getLayoutData()).horizontalIndent = 8;
			((GridData) sourceText.getLayoutData()).horizontalSpan = 6;

			ControlDecoration decoValIdInfo = new ControlDecoration(sourceText, SWT.LEFT | SWT.BOTTOM);
			decoValIdInfo.setDescriptionText(NLMessages.getString("Editor_proposal_keybinding"));
			decoValIdInfo.setImage(FieldDecorationRegistry.getDefault()
					.getFieldDecoration(FieldDecorationRegistry.DEC_INFORMATION).getImage());
			decoValIdInfo.setShowOnlyOnFocus(false);
			final ControlDecoration decoValId = new ControlDecoration(sourceText, SWT.LEFT | SWT.TOP);
			decoValId.setShowOnlyOnFocus(false);
			// TODO auskommentiert da in den SelectObjectDialog erst einer
			// Filter eingebaut werden muss,
			// der Quellen ausblende, die in zweiter Ebene gehosted sind.
			// Button relateRefButton = new Button(relatedItemComp, SWT.PUSH);
			//	   		relateRefButton.setText(NLMessages.getString("Editor_select_dots")); //$NON-NLS-1$
			// relateRefButton.setImage(_imageReg.get(IconsInternal.SEARCH));
			// relateRefButton.setEnabled(_mayWrite);
			// relateRefButton.setLayoutData(new GridData());
			//	   		relateRefButton.setToolTipText(""); //$NON-NLS-1$
			//
			// relateRefButton.pack();

			if (relItem.getId() != null) //$NON-NLS-1$
			{
				PdrObject o = _facade.getPdrObject(new PdrId(relItem.getId()));
				if (o != null)
				{
					sourceText.setText(o.getDisplayNameWithID());
				}
				else
				{
					sourceText.setText(relItem.getId());
				}
			}
			else
			{
				sourceText.setText(""); //$NON-NLS-1$
			}

			sourceText.addFocusListener(new FocusListener()
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
						ContentProposalAdapter adapter = new ContentProposalAdapter(sourceText,
								new TextContentAdapter(), new FacetContentProposalProvider(_facade
										.getAllReferenceFacetsRelItemFiltered(), _currentReference.getPdrId()),
								keyStroke, autoActivationCharacters);
						adapter.setLabelProvider(new AutoCompleteNameLabelProvider());
						adapter.addContentProposalListener(new IContentProposalListener()
						{

							@Override
							public void proposalAccepted(final IContentProposal proposal)
							{
								sourceText.setText(proposal.getContent());
								if (((Facet) proposal).getKey() != null)
								{
									relItem.setId(((Facet) proposal).getKey());
									PdrObject o = _facade.getPdrObject(new PdrId(((Facet) proposal).getKey()));
									if (o != null)
									{
										decoValId.setImage(null);
										sourceText.setText(o.getDisplayNameWithID());
										// relItem.setId((sourceText.getText()));
									}
									else
									{
										relItem.setId(null);
										decoValId.setImage(FieldDecorationRegistry.getDefault()
												.getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).getImage());
										decoValId.setDescriptionText(NLMessages
												.getString("Editor_missing_object_no_relation"));
									}
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
					if (relItem.getId() != null && _facade.getReference(new PdrId(relItem.getId())) != null)
					{
						decoValId.setDescriptionText("");
						decoValId.setImage(null);
					}
					// else
					// {
					// relItem.setId(null);
					// decoValId.setImage(FieldDecorationRegistry.getDefault().getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).getImage());
					// decoValId.setDescriptionText("Missing PDR-Object. You cannot relate to a missing Object.");
					// }
					validate();
				}
			});
			sourceText.addKeyListener(new KeyListener()
			{
				@Override
				public void keyPressed(final KeyEvent e)
				{
				}

				@Override
				public void keyReleased(final KeyEvent e)
				{
					if (sourceText.getText().length() == 23)
					{
						PdrObject o = _facade.getPdrObject(new PdrId(sourceText.getText()));
						if (o != null)
						{
							decoValId.setImage(null);
							relItem.setId(o.getPdrId().toString());
							sourceText.setText(o.getDisplayNameWithID());
						}
						else
						{
							relItem.setId(null);
						}

					}
					else if (sourceText.getText().length() == 0)
					{
						relItem.setId(null);
					}
					if (sourceText.getText().length() != 0 && relItem.getId() == null)
					{
						decoValId.setImage(FieldDecorationRegistry.getDefault()
								.getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).getImage());
						decoValId.setDescriptionText(NLMessages.getString("Editor_missing_object_no_relation"));
					}
				}
			});
			Button relateRefButton = new Button(relatedItemComp, SWT.PUSH);
			relateRefButton.setImage(_imageReg.get(IconsInternal.SEARCH));
			relateRefButton.setEnabled(_mayWrite);
			relateRefButton.setLayoutData(new GridData());
			relateRefButton.setToolTipText(NLMessages
					.getString("Editor_linkWithSource")); //$NON-NLS-1$
			relateRefButton.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent event) {

					IWorkbench workbench = PlatformUI.getWorkbench();
					Display display = workbench.getDisplay();
					Shell shell = new Shell(display);
					SelectObjectDialog dialog = new SelectObjectDialog(shell, 2);
					dialog.open();
					if (_facade.getRequestedId() != null) {
						relItem.setId(_facade.getRequestedId().toString());
						if (relItem.getId().trim().length() > 0) {
							PdrObject o = _facade.getReference(new PdrId(
									relItem.getId()));
							if (o != null) {
								sourceText.setText(o.getDisplayNameWithID());
							} else
								sourceText.setText(relItem.getId());
						}
					} else {
						sourceText.setText(""); //$NON-NLS-1$
					}
				}
			});

			Button newRefButton = new Button(relatedItemComp, SWT.PUSH);
			newRefButton.setText(NLMessages.getString("Editor_create_new_ref"));
			newRefButton.setToolTipText(NLMessages.getString("Editor_create_new_ref_tip"));
			newRefButton.setImage(_imageReg.get(IconsInternal.REFERENCE_NEW));
			newRefButton.setEnabled(_mayWrite);
			newRefButton.setLayoutData(new GridData());
			newRefButton.addSelectionListener(new SelectionAdapter()
			{
				@Override
				public void widgetSelected(final SelectionEvent ev)
				{

					Event event = new Event();
					event.data = _currentReference.getPdrId();
					IHandlerService handlerService = (IHandlerService) PlatformUI.getWorkbench().getService(
							IHandlerService.class);
					try
					{
						handlerService.executeCommand("org.bbaw.pdr.ae.view.main.commands.NewReference", event); //$NON-NLS-1$
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
					if (_facade.getCurrentReference() != null)
					{
						sourceText.setText(_facade.getCurrentReference().getDisplayNameWithID());
						relItem.setId(_facade.getCurrentReference().getPdrId().toString());
					}

				}
			});
			newRefButton.pack();

			final Button deleteRels = new Button(relatedItemComp, SWT.PUSH);
			deleteRels.setText("-"); //$NON-NLS-1$
			deleteRels.setToolTipText(NLMessages.getString("Editor_remove_field"));
			deleteRels.setEnabled(_mayWrite);
			deleteRels.setLayoutData(_gridData);
			deleteRels.setData("id", i); //$NON-NLS-1$
			deleteRels.addSelectionListener(new SelectionAdapter()
			{
				@Override
				public void widgetSelected(final SelectionEvent event)
				{
					_currentReference.getRelatedItems().removeElementAt((Integer) deleteRels.getData("id")); //$NON-NLS-1$
					loadValues(_currentReference);

				}
			});
			deleteRels.setLayoutData(new GridData());

			boolean relItemExtra = false;
			if (relItem.getPart() != null)
			{
				Composite relatedExtraComp = new Composite(relatedItemComp, SWT.NONE);
				relatedExtraComp.setLayoutData(new GridData());
				((GridData) relatedExtraComp.getLayoutData()).horizontalAlignment = SWT.FILL;
				((GridData) relatedExtraComp.getLayoutData()).grabExcessHorizontalSpace = true;
				((GridData) relatedExtraComp.getLayoutData()).horizontalSpan = 10;

				relatedExtraComp.setLayout(new GridLayout());
				((GridLayout) relatedExtraComp.getLayout()).numColumns = 10;
				((GridLayout) relatedExtraComp.getLayout()).makeColumnsEqualWidth = false;
				((GridLayout) relatedExtraComp.getLayout()).marginWidth = 0;
				((GridLayout) relatedExtraComp.getLayout()).marginHeight = 0;

				final DetailMods detailVolume = relItem.getPart().getDetailVolume();
				if (detailVolume != null && detailVolume.getNumber() != null)
				{
					relItemExtra = true;
					Label label30 = new Label(relatedExtraComp, SWT.RIGHT);
					label30.setText(NLMessages.getString("Editor_volume_number"));
					label30.setLayoutData(new GridData());
					final Text volumeNumber = new Text(relatedExtraComp, SWT.BORDER);
					volumeNumber.setEditable(_mayWrite);
					volumeNumber.setBackground(WHITE_COLOR);
					volumeNumber.setLayoutData(_gridData);
					volumeNumber.addFocusListener(new FocusListener()
					{
						@Override
						public void focusGained(final FocusEvent e)
						{

						}

						@Override
						public void focusLost(final FocusEvent e)
						{
							detailVolume.setNumber(volumeNumber.getText());
						}
					});
					volumeNumber.setText(detailVolume.getNumber());
				}
				final DetailMods detailIssue = relItem.getPart().getDetailIssue();
				if (detailIssue != null && detailIssue.getNumber() != null)
				{
					relItemExtra = true;
					Label label31 = new Label(relatedExtraComp, SWT.RIGHT);
					label31.setText(NLMessages.getString("Editor_issue_number"));
					label31.setLayoutData(new GridData());
					final Text issueNumber = new Text(relatedExtraComp, SWT.BORDER);
					issueNumber.setEditable(_mayWrite);
					issueNumber.setBackground(WHITE_COLOR);
					issueNumber.setLayoutData(_gridData);
					issueNumber.addFocusListener(new FocusListener()
					{
						@Override
						public void focusGained(final FocusEvent e)
						{

						}

						@Override
						public void focusLost(final FocusEvent e)
						{
							detailIssue.setNumber(issueNumber.getText());
						}
					});
					issueNumber.setText(detailIssue.getNumber());
				}
				final ExtendMods extendPages = relItem.getPart().getExtendPages();
				if (extendPages != null)
				{
					relItemExtra = true;
					Label label32 = new Label(relatedExtraComp, SWT.RIGHT);
					label32.setText(NLMessages.getString("Editor_page"));
					label32.setLayoutData(_gridData);

					Label label33 = new Label(relatedExtraComp, SWT.RIGHT);
					label33.setText(NLMessages.getString("Editor_page_start"));
					label33.setLayoutData(_gridData);

					final Text pagesStart = new Text(relatedExtraComp, SWT.BORDER);
					pagesStart.setEditable(_mayWrite);
					pagesStart.setBackground(WHITE_COLOR);
					pagesStart.setLayoutData(_gridData);
					pagesStart.addFocusListener(new FocusListener()
					{
						@Override
						public void focusGained(final FocusEvent e)
						{

						}

						@Override
						public void focusLost(final FocusEvent e)
						{
							extendPages.setStart(pagesStart.getText());
						}
					});
					if (extendPages.getStart() != null)
					{
						pagesStart.setText(extendPages.getStart());
					}

					Label label34 = new Label(relatedExtraComp, SWT.RIGHT);
					label34.setText(NLMessages.getString("Editor_page_end"));
					label34.setLayoutData(_gridData);

					final Text pagesEnd = new Text(relatedExtraComp, SWT.BORDER);
					pagesEnd.setEditable(_mayWrite);
					pagesEnd.setBackground(WHITE_COLOR);
					pagesEnd.setLayoutData(_gridData);
					pagesEnd.addFocusListener(new FocusListener()
					{
						@Override
						public void focusGained(final FocusEvent e)
						{

						}

						@Override
						public void focusLost(final FocusEvent e)
						{
							extendPages.setEnd(pagesEnd.getText());
						}
					});
					if (extendPages.getEnd() != null)
					{
						pagesEnd.setText(extendPages.getEnd());
					}

				}
				if (relItemExtra)
				{
					final Button delDate = new Button(relatedExtraComp, SWT.PUSH);
					delDate.setText("-"); //$NON-NLS-1$
					delDate.setToolTipText(NLMessages.getString("Editor_remove_field"));
					delDate.setEnabled(_mayWrite);
					delDate.addSelectionListener(new SelectionAdapter()
					{
						@Override
						public void widgetSelected(final SelectionEvent event)
						{
							relItem.getPart().getDetails().removeAllElements();
							relItem.getPart().getExtendsMods().removeAllElements();
							loadValues(_currentReference);
						}
					});
					delDate.setLayoutData(new GridData());
				}
				if (relItem.getPart().getDates() != null && !relItem.getPart().getDates().isEmpty()
						&& relItem.getPart().getDates().firstElement() != null)
				{
					Label label9 = new Label(relatedExtraComp, SWT.FILL);
					label9.setText(NLMessages.getString("Editor_add_date_part"));
					Label labelDay = new Label(relatedExtraComp, SWT.RIGHT);
					labelDay.setText(NLMessages.getString("Editor_day"));
					labelDay.setLayoutData(_gridDataRight);

					final Combo comboTimeDay = new Combo(relatedExtraComp, SWT.READ_ONLY);
					comboTimeDay.setEnabled(_mayWrite);
					comboTimeDay.setBackground(WHITE_COLOR);
					comboTimeDay.setLayoutData(new GridData());
					((GridData) comboTimeDay.getLayoutData()).horizontalAlignment = GridData.FILL;
					((GridData) comboTimeDay.getLayoutData()).grabExcessHorizontalSpace = true;
					comboTimeDay.setItems(AEConstants.DAYS);
					comboTimeDay.addSelectionListener(new SelectionAdapter()
					{
						@Override
						public void widgetSelected(final SelectionEvent se)
						{
							relItem.getPart().getDates().firstElement().setDay(comboTimeDay.getSelectionIndex());
						}
					});
					Label labelMonth = new Label(relatedExtraComp, SWT.RIGHT);
					labelMonth.setText(NLMessages.getString("Editor_month"));
					labelMonth.setLayoutData(_gridData);
					final Combo comboTimeMonth = new Combo(relatedExtraComp, SWT.READ_ONLY);
					comboTimeMonth.setEnabled(_mayWrite);
					comboTimeMonth.setBackground(WHITE_COLOR);
					comboTimeMonth.setLayoutData(new GridData());
					((GridData) comboTimeMonth.getLayoutData()).horizontalAlignment = GridData.FILL;
					((GridData) comboTimeMonth.getLayoutData()).grabExcessHorizontalSpace = true;
					comboTimeMonth.setItems(AEConstants.MONTHS);
					comboTimeMonth.addSelectionListener(new SelectionAdapter()
					{
						@Override
						public void widgetSelected(final SelectionEvent se)
						{
							relItem.getPart().getDates().firstElement().setMonth(comboTimeMonth.getSelectionIndex());
						}
					});
					Label labelYear = new Label(relatedExtraComp, SWT.RIGHT);
					labelYear.setText(NLMessages.getString("Editor_year"));
					labelYear.setLayoutData(_gridDataRight);
					final YearSpinner spinnerTimeYear = new YearSpinner(relatedExtraComp, SWT.BORDER);
					spinnerTimeYear.setEnabled(_mayWrite);
					spinnerTimeYear.addSelectionListener(new SelectionAdapter()
					{
						@Override
						public void widgetSelected(final SelectionEvent se)
						{
							relItem.getPart().getDates().firstElement().setYear(spinnerTimeYear.getSelection());
						}
					});

					comboTimeDay.select(relItem.getPart().getDates().firstElement().getDay());
					comboTimeMonth.select(relItem.getPart().getDates().firstElement().getMonth());
					spinnerTimeYear.setSelection(relItem.getPart().getDates().firstElement().getYear());

					final Button delDate = new Button(relatedExtraComp, SWT.PUSH);
					delDate.setText("-"); //$NON-NLS-1$
					delDate.setToolTipText(NLMessages.getString("Editor_remove_field"));
					delDate.setEnabled(_mayWrite);
					delDate.addSelectionListener(new SelectionAdapter()
					{
						@Override
						public void widgetSelected(final SelectionEvent event)
						{
							relItem.getPart().setDates(null);
							loadValues(_currentReference);
						}
					});
					delDate.setLayoutData(new GridData());
				}
			}
		}
	}

	/**
	 * Load series title info.
	 * @param contentComp the content comp
	 */
	private void loadSeriesTitleInfo(final Composite contentComp)
	{
		Composite titleComp = new Composite(contentComp, SWT.NONE);
		titleComp.setLayoutData(new GridData());
		((GridData) titleComp.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) titleComp.getLayoutData()).grabExcessHorizontalSpace = true;
		titleComp.setLayout(new GridLayout());
		((GridLayout) titleComp.getLayout()).numColumns = 3;
		((GridLayout) titleComp.getLayout()).makeColumnsEqualWidth = false;

		if (_currentReference.getSeriesTitleInfo().getTitle() != null)
		{
			Label label1 = new Label(titleComp, SWT.NONE);
			label1.setText(NLMessages.getString("Editor_add_series") + " " + NLMessages.getString("Editor_title")); //$NON-NLS-1$
			final Text stitel = new Text(titleComp, SWT.BORDER);
			stitel.setEditable(_mayWrite);
			stitel.setBackground(WHITE_COLOR);
			stitel.setLayoutData(_gridData);

			stitel.setText(_currentReference.getSeriesTitleInfo().getTitle().trim());

			//
			// {
			//			_titel.setText(NLMessages.getString("Editor_enterTitle")); //$NON-NLS-1$
			// _decoValTi.setImage(FieldDecorationRegistry.getDefault().getFieldDecoration(FieldDecorationRegistry.DEC_REQUIRED).getImage());
			// }
			stitel.addFocusListener(new FocusListener()
			{
				// FIXME Autocomplete
				@Override
				public void focusGained(final FocusEvent e)
				{
					String[] vals = new String[]
					{"test", "test2"}; //$NON-NLS-1$ //$NON-NLS-2$
					try
					{
						vals = _mainSearcher.getFacets("reference", "relatedItem", "title", null, null); //$NON-NLS-1$ //$NON-NLS-2$
					}
					catch (Exception e1)
					{

						e1.printStackTrace();
					}
					new AutoCompleteField(stitel, new TextContentAdapter(), vals);

				}

				@Override
				public void focusLost(final FocusEvent e)
				{
					_currentReference.getSeriesTitleInfo().setTitle(stitel.getText());
				}
			});

			Button delSTitle = new Button(titleComp, SWT.PUSH);
			delSTitle.setText("-"); //$NON-NLS-1$
			delSTitle.setToolTipText(NLMessages.getString("Editor_remove_series_title"));
			delSTitle.setEnabled(_mayWrite);
			delSTitle.setLayoutData(_gridData);
			delSTitle.addSelectionListener(new SelectionAdapter()
			{
				@Override
				public void widgetSelected(final SelectionEvent event)
				{
					_currentReference.setSeriesTitleInfo(null); //$NON-NLS-1$
					loadValues(_currentReference);

				}
			});
			delSTitle.setLayoutData(new GridData());
		}
		if (_currentReference.getSeriesTitleInfo().getSubTitle() != null)

		{
			Composite titleComp2 = new Composite(contentComp, SWT.NONE);

			titleComp2.setLayoutData(new GridData());
			((GridData) titleComp2.getLayoutData()).horizontalAlignment = SWT.FILL;
			((GridData) titleComp2.getLayoutData()).grabExcessHorizontalSpace = true;
			((GridData) titleComp2.getLayoutData()).horizontalSpan = 3;

			titleComp2.setLayout(new GridLayout());
			((GridLayout) titleComp2.getLayout()).numColumns = 4;
			((GridLayout) titleComp2.getLayout()).makeColumnsEqualWidth = false;

			if (_currentReference.getSeriesTitleInfo().getSubTitle() != null)
			{
				Label label2 = new Label(titleComp2, SWT.NONE);
				label2.setText(NLMessages.getString("Editor_add_series") + " "
						+ NLMessages.getString("Editor_subtitle"));
				final Text subSTitel = new Text(titleComp2, SWT.BORDER);
				subSTitel.setEditable(_mayWrite);
				subSTitel.setBackground(WHITE_COLOR);
				subSTitel.setLayoutData(_gridData);
				subSTitel.setText(_currentReference.getSeriesTitleInfo().getSubTitle().trim());
				subSTitel.addFocusListener(new FocusListener()
				{
					// FIXME
					@Override
					public void focusGained(final FocusEvent e)
					{
						String[] vals = new String[]
						{"test", "test2"}; //$NON-NLS-1$ //$NON-NLS-2$

						try
						{
							vals = _mainSearcher.getFacets("reference", "relatedItem", "subTitle", null, null); //$NON-NLS-1$ //$NON-NLS-2$
						}
						catch (Exception e1)
						{

							e1.printStackTrace();
						}
						new AutoCompleteField(subSTitel, new TextContentAdapter(), vals);
					}

					@Override
					public void focusLost(final FocusEvent e)
					{
						_currentReference.getSeriesTitleInfo().setSubTitle(subSTitel.getText());
					}
				});

			}

			final Button delExtra = new Button(titleComp2, SWT.PUSH);
			delExtra.setText("-"); //$NON-NLS-1$
			delExtra.setToolTipText(NLMessages.getString("Editor_remove_subtitle"));
			delExtra.setEnabled(_mayWrite);
			delExtra.setLayoutData(_gridData);
			delExtra.addSelectionListener(new SelectionAdapter()
			{
				@Override
				public void widgetSelected(final SelectionEvent event)
				{
					_currentReference.getSeriesTitleInfo().setSubTitle(null); //$NON-NLS-1$
					loadValues(_currentReference);
				}
			});
			delExtra.setLayoutData(new GridData());
		}
		if (_currentReference.getSeriesTitleInfo().getPartName() != null
				&& _currentReference.getSeriesTitleInfo().getPartNumber() != null)

		{
			Composite titleComp2 = new Composite(contentComp, SWT.NONE);

			titleComp2.setLayoutData(new GridData());
			((GridData) titleComp2.getLayoutData()).horizontalAlignment = SWT.FILL;
			((GridData) titleComp2.getLayoutData()).grabExcessHorizontalSpace = true;
			((GridData) titleComp2.getLayoutData()).horizontalSpan = 3;

			titleComp2.setLayout(new GridLayout());
			((GridLayout) titleComp2.getLayout()).numColumns = 5;
			((GridLayout) titleComp2.getLayout()).makeColumnsEqualWidth = false;

			if (_currentReference.getSeriesTitleInfo().getPartName() != null)
			{
				Label label2 = new Label(titleComp2, SWT.NONE);
				label2.setText(NLMessages.getString("Editor_add_series") + " " + NLMessages.getString("Editor_partName")); //$NON-NLS-1$
				final Text partName = new Text(titleComp2, SWT.BORDER);
				partName.setEditable(_mayWrite);
				partName.setBackground(WHITE_COLOR);
				partName.setLayoutData(_gridData);
				partName.setText(_currentReference.getSeriesTitleInfo().getPartName().trim());
				partName.addFocusListener(new FocusListener()
				{
					@Override
					public void focusGained(final FocusEvent e)
					{
						String[] vals = new String[]
						{"test", "test2"}; //$NON-NLS-1$ //$NON-NLS-2$

						try
						{
							vals = _mainSearcher.getFacets("reference", "relatedItem", "partName", null, null); //$NON-NLS-1$ //$NON-NLS-2$
						}
						catch (Exception e1)
						{

							e1.printStackTrace();
						}
						new AutoCompleteField(partName, new TextContentAdapter(), vals);
					}

					@Override
					public void focusLost(final FocusEvent e)
					{
						_currentReference.getSeriesTitleInfo().setPartName(partName.getText());
					}
				});

			}
			if (_currentReference.getSeriesTitleInfo().getPartNumber() != null)
			{
				Label label3 = new Label(titleComp2, SWT.NONE);
				label3.setText(NLMessages.getString("Editor_add_series") + " " + NLMessages.getString("Editor_partNumber")); //$NON-NLS-1$
				final Text partNum = new Text(titleComp2, SWT.BORDER);
				partNum.setEditable(_mayWrite);
				partNum.setBackground(WHITE_COLOR);
				partNum.setLayoutData(_gridData);
				partNum.setText(_currentReference.getSeriesTitleInfo().getPartNumber().trim());
				partNum.addFocusListener(new FocusListener()
				{
					@Override
					public void focusGained(final FocusEvent e)
					{
						String[] vals = new String[]
						{"test", "test2"}; //$NON-NLS-1$ //$NON-NLS-2$

						try
						{
							vals = _mainSearcher.getFacets("reference", "relatedItem", "partNumber", null, null); //$NON-NLS-1$ //$NON-NLS-2$
						}
						catch (Exception e1)
						{

							e1.printStackTrace();
						}
						new AutoCompleteField(partNum, new TextContentAdapter(), vals);
					}

					@Override
					public void focusLost(final FocusEvent e)
					{
						_currentReference.getSeriesTitleInfo().setPartNumber(partNum.getText());
					}
				});

			}
			final Button delExtra = new Button(titleComp2, SWT.PUSH);
			delExtra.setText("-"); //$NON-NLS-1$
			delExtra.setToolTipText(NLMessages.getString("Editor_remove_partName"));
			delExtra.setEnabled(_mayWrite);
			delExtra.setLayoutData(_gridData);
			delExtra.addSelectionListener(new SelectionAdapter()
			{
				@Override
				public void widgetSelected(final SelectionEvent event)
				{
					_currentReference.getSeriesTitleInfo().setPartName(null); //$NON-NLS-1$
					_currentReference.getSeriesTitleInfo().setPartNumber(null); //$NON-NLS-1$
					loadValues(_currentReference);

				}
			});
			delExtra.setLayoutData(new GridData());
		}
	}

	/**
	 * Load title info.
	 * @param contentComp the content comp
	 */
	private void loadTitleInfo(final Composite contentComp)
	{
		Composite titleComp = new Composite(contentComp, SWT.NONE);
		titleComp.setLayoutData(new GridData());
		((GridData) titleComp.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) titleComp.getLayoutData()).grabExcessHorizontalSpace = true;
		titleComp.setLayout(new GridLayout());
		((GridLayout) titleComp.getLayout()).numColumns = 3;
		((GridLayout) titleComp.getLayout()).makeColumnsEqualWidth = false;

		if (_currentReference.getTitleInfo().getTitle() != null)
		{
			Label label1 = new Label(titleComp, SWT.NONE);
			label1.setText(NLMessages.getString("Editor_title")); //$NON-NLS-1$
			_titel = new Text(titleComp, SWT.BORDER);
			_titel.setEditable(_mayWrite);
			_titel.setBackground(WHITE_COLOR);
			_decoValTi = new ControlDecoration(_titel, SWT.LEFT | SWT.TOP);
			_titel.setLayoutData(_gridData);

			_titel.setText(_currentReference.getTitleInfo().getTitle().trim());
			_decoValTi.setImage(null);

			_titel.addFocusListener(new FocusListener()
			{
				@Override
				public void focusGained(final FocusEvent e)
				{
					String[] vals = new String[]
					{"test", "test2"}; //$NON-NLS-1$ //$NON-NLS-2$
					try
					{
						vals = _mainSearcher.getFacets("reference", "title", null, null, null); //$NON-NLS-1$ //$NON-NLS-2$
					}
					catch (Exception e1)
					{

						e1.printStackTrace();
					}
					new AutoCompleteField(_titel, new TextContentAdapter(), vals);

				}

				@Override
				public void focusLost(final FocusEvent e)
				{
					if (!_asGenreEditor)
					{
						_currentReference.getTitleInfo().setTitle(_titel.getText().trim());
						if (_currentReference.getTitleInfo().getTitle().trim().length() > 0)
						{
							_decoValTi.setImage(null);
						}
						else
						{
							_decoValTi.setImage(FieldDecorationRegistry.getDefault()
									.getFieldDecoration(FieldDecorationRegistry.DEC_REQUIRED).getImage());
						}
						validate();
					}
					else
					{
						_currentReference.getTitleInfo().setTitle(_titel.getText());
					}
				}
			});
			_titel.addKeyListener(new KeyListener()
			{

				@Override
				public void keyPressed(final KeyEvent arg0)
				{
					// TODO Auto-generated method stub

				}

				@Override
				public void keyReleased(final KeyEvent e)
				{
					if (!_asGenreEditor)
					{
						_currentReference.getTitleInfo().setTitle(_titel.getText().trim());
						if (_currentReference.getTitleInfo().getTitle().trim().length() > 0)
						{
							_decoValTi.setImage(null);
						}
						else
						{
							_decoValTi.setImage(FieldDecorationRegistry.getDefault()
									.getFieldDecoration(FieldDecorationRegistry.DEC_REQUIRED).getImage());
						}
						validate();
					}
					else
					{
						_currentReference.getTitleInfo().setTitle(_titel.getText());
					}
				}
			});

			_delTitle = new Button(titleComp, SWT.PUSH);
			_delTitle.setText("-"); //$NON-NLS-1$
			_delTitle.setToolTipText(NLMessages.getString("Editor_remove_title"));
			_delTitle.setEnabled(_mayWrite && _currentReference.isValid());
			_delTitle.setLayoutData(_gridData);
			_delTitle.addSelectionListener(new SelectionAdapter()
			{
				@Override
				public void widgetSelected(final SelectionEvent event)
				{
					_currentReference.setTitleInfo(null); //$NON-NLS-1$
					loadValues(_currentReference);

				}
			});
			_delTitle.setLayoutData(new GridData());
		}
		if (_currentReference.getTitleInfo().getSubTitle() != null)

		{
			Composite titleComp2 = new Composite(contentComp, SWT.NONE);

			titleComp2.setLayoutData(new GridData());
			((GridData) titleComp2.getLayoutData()).horizontalAlignment = SWT.FILL;
			((GridData) titleComp2.getLayoutData()).grabExcessHorizontalSpace = true;
			((GridData) titleComp2.getLayoutData()).horizontalSpan = 3;

			titleComp2.setLayout(new GridLayout());
			((GridLayout) titleComp2.getLayout()).numColumns = 4;
			((GridLayout) titleComp2.getLayout()).makeColumnsEqualWidth = false;

			if (_currentReference.getTitleInfo().getSubTitle() != null)
			{
				Label label2 = new Label(titleComp2, SWT.NONE);
				label2.setText(NLMessages.getString("Editor_subtitle"));
				_subTitel = new Text(titleComp2, SWT.BORDER);
				_subTitel.setEditable(_mayWrite);
				_subTitel.setBackground(WHITE_COLOR);
				_subTitel.setLayoutData(_gridData);
				_subTitel.setText(_currentReference.getTitleInfo().getSubTitle().trim());
				_subTitel.addFocusListener(new FocusListener()
				{
					@Override
					public void focusGained(final FocusEvent e)
					{
						String[] vals = new String[]
						{"test", "test2"}; //$NON-NLS-1$ //$NON-NLS-2$

						try
						{
							vals = _mainSearcher.getFacets("reference", "subTitle", null, null, null); //$NON-NLS-1$ //$NON-NLS-2$
						}
						catch (Exception e1)
						{

							e1.printStackTrace();
						}
						new AutoCompleteField(_subTitel, new TextContentAdapter(), vals);
					}

					@Override
					public void focusLost(final FocusEvent e)
					{
						_currentReference.getTitleInfo().setSubTitle(_subTitel.getText());
					}
				});

			}

			final Button delExtra = new Button(titleComp2, SWT.PUSH);
			delExtra.setText("-"); //$NON-NLS-1$
			delExtra.setToolTipText(NLMessages.getString("Editor_remove_subtitle"));
			delExtra.setEnabled(_mayWrite);
			delExtra.setLayoutData(_gridData);
			delExtra.addSelectionListener(new SelectionAdapter()
			{
				@Override
				public void widgetSelected(final SelectionEvent event)
				{
					_currentReference.getTitleInfo().setSubTitle(null); //$NON-NLS-1$
					loadValues(_currentReference);
				}
			});
			delExtra.setLayoutData(new GridData());
		}
		if (_currentReference.getTitleInfo().getPartName() != null
				&& _currentReference.getTitleInfo().getPartNumber() != null)

		{
			Composite titleComp2 = new Composite(contentComp, SWT.NONE);

			titleComp2.setLayoutData(new GridData());
			((GridData) titleComp2.getLayoutData()).horizontalAlignment = SWT.FILL;
			((GridData) titleComp2.getLayoutData()).grabExcessHorizontalSpace = true;
			((GridData) titleComp2.getLayoutData()).horizontalSpan = 3;

			titleComp2.setLayout(new GridLayout());
			((GridLayout) titleComp2.getLayout()).numColumns = 5;
			((GridLayout) titleComp2.getLayout()).makeColumnsEqualWidth = false;

			if (_currentReference.getTitleInfo().getPartName() != null)
			{
				Label label2 = new Label(titleComp2, SWT.NONE);
				label2.setText(NLMessages.getString("Editor_partName")); //$NON-NLS-1$
				final Text partName = new Text(titleComp2, SWT.BORDER);
				partName.setEditable(_mayWrite);
				partName.setBackground(WHITE_COLOR);
				partName.setLayoutData(_gridData);
				partName.setText(_currentReference.getTitleInfo().getPartName().trim());
				partName.addFocusListener(new FocusListener()
				{
					@Override
					public void focusGained(final FocusEvent e)
					{
						String[] vals = new String[]
						{"test", "test2"}; //$NON-NLS-1$ //$NON-NLS-2$

						try
						{
							vals = _mainSearcher.getFacets("reference", "partName", null, null, null); //$NON-NLS-1$ //$NON-NLS-2$
						}
						catch (Exception e1)
						{

							e1.printStackTrace();
						}
						new AutoCompleteField(partName, new TextContentAdapter(), vals);
					}

					@Override
					public void focusLost(final FocusEvent e)
					{
						_currentReference.getTitleInfo().setPartName(partName.getText());
					}
				});

			}
			if (_currentReference.getTitleInfo().getPartNumber() != null)
			{
				Label label3 = new Label(titleComp2, SWT.NONE);
				label3.setText(NLMessages.getString("Editor_partNumber")); //$NON-NLS-1$
				final Text partNum = new Text(titleComp2, SWT.BORDER);
				partNum.setEditable(_mayWrite);
				partNum.setBackground(WHITE_COLOR);
				partNum.setLayoutData(_gridData);
				partNum.setText(_currentReference.getTitleInfo().getPartNumber().trim());
				partNum.addFocusListener(new FocusListener()
				{
					@Override
					public void focusGained(final FocusEvent e)
					{
						String[] vals = new String[]
						{"test", "test2"}; //$NON-NLS-1$ //$NON-NLS-2$

						try
						{
							vals = _mainSearcher.getFacets("reference", "partNumber", null, null, null); //$NON-NLS-1$ //$NON-NLS-2$
						}
						catch (Exception e1)
						{

							e1.printStackTrace();
						}
						new AutoCompleteField(partNum, new TextContentAdapter(), vals);
					}

					@Override
					public void focusLost(final FocusEvent e)
					{
						_currentReference.getTitleInfo().setPartNumber(partNum.getText());
					}
				});

			}
			final Button delExtra = new Button(titleComp2, SWT.PUSH);
			delExtra.setText("-"); //$NON-NLS-1$
			delExtra.setToolTipText(NLMessages.getString("Editor_remove_partName"));
			delExtra.setEnabled(_mayWrite);
			delExtra.setLayoutData(_gridData);
			delExtra.addSelectionListener(new SelectionAdapter()
			{
				@Override
				public void widgetSelected(final SelectionEvent event)
				{
					_currentReference.getTitleInfo().setPartName(null); //$NON-NLS-1$
					_currentReference.getTitleInfo().setPartNumber(null); //$NON-NLS-1$
					loadValues(_currentReference);

				}
			});
			delExtra.setLayoutData(new GridData());
		}
	}

	/**
	 * Load values.
	 * @param currentRef the current ref
	 */
	public final void loadValues(final ReferenceMods currentRef)
	{
		_currentReference = currentRef;
		if (_compositeRef == null)
		{
			_compositeRef = new Composite(_compositeSourcePanel, SWT.NONE);
		}
		_compositeRef.setLayoutData(new GridData());
		((GridData) _compositeRef.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _compositeRef.getLayoutData()).grabExcessHorizontalSpace = true;
		_compositeRef.setLayout(new GridLayout());
		((GridLayout) _compositeRef.getLayout()).numColumns = 1;
		((GridLayout) _compositeRef.getLayout()).makeColumnsEqualWidth = false;
		((GridLayout) _compositeRef.getLayout()).marginHeight = 0;

		_sourceStackLayout.topControl = _compositeRef;

		_currentReference = currentRef;
		if (!_asGenreEditor && _scrolledCompositeMain == null)
		{
			loadAdminData();
		}
		else
		{
			_mayWrite = true;
		}
		if (_mayWrite && _scrolledCompositeMain == null)
		{
			createAddButtons(_compositeRef);
		}
		else if (_mayWrite)
		{
			_addToolTip.setReference(_currentReference);
		}

		if (_scrolledCompositeMain != null)
		{
			_scrolledCompositeMain.dispose();
			_scrolledCompositeMain = null;
		}
		_scrolledCompositeMain = new ScrolledComposite(_compositeRef, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		_scrolledCompositeMain.setExpandHorizontal(true);
		_scrolledCompositeMain.setExpandVertical(true);
		_scrolledCompositeMain.setMinHeight(1);
		_scrolledCompositeMain.setMinWidth(1);

		_scrolledCompositeMain.setLayout(new GridLayout());
		_scrolledCompositeMain.setLayoutData(new GridData());
		((GridData) _scrolledCompositeMain.getLayoutData()).heightHint = 385;
		((GridData) _scrolledCompositeMain.getLayoutData()).widthHint = 600;

		((GridData) _scrolledCompositeMain.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _scrolledCompositeMain.getLayoutData()).grabExcessHorizontalSpace = true;
		_scrolledCompositeMain.pack();

		Composite contentComp = new Composite(_scrolledCompositeMain, SWT.NONE);
		contentComp.setLayout(new GridLayout());
		_scrolledCompositeMain.setContent(contentComp);
		// if (_mayWrite) loadAddButtons(contentComp);

		if (_currentReference.getNameMods() != null && _currentReference.getNameMods().size() > 0)
		{
			loadNames(contentComp);
		}
		if (_currentReference.getTitleInfo() != null)
		{
			loadTitleInfo(contentComp);
		}
		if (_currentReference.getSeriesTitleInfo() != null)
		{
			loadSeriesTitleInfo(contentComp);
		}

		if (!_asGenreEditor && _currentReference.getGenre() != null && _currentReference.getGenre().getGenre() != null)
		{
			@SuppressWarnings("unchecked")
			ReferenceModsTemplate temp = ((HashMap<String, ReferenceModsTemplate>) _sourceGenreComboViewer.getInput())
					.get(_currentReference.getGenre().getGenre());
			if (temp != null && temp.getLabel() == null)
			{
				if (temp.getValue() != null)
				{
					temp.setLabel(temp.getValue());
				}
			}
			if (temp == null || temp.getLabel() == null)
			{
				temp = new ReferenceModsTemplate();
				temp.setLabel(_currentReference.getGenre().getGenre());
				temp.setValue(_currentReference.getGenre().getGenre());
				_sourceGenreComboViewer.add(temp);

			}
			StructuredSelection selection = new StructuredSelection(temp);
			_sourceGenreComboViewer.setSelection(selection);
		}
		if (_currentReference.getOriginInfo() != null)
		{
			loadOriginInfo(contentComp);
		}
		if (_currentReference.getNote() != null)
		{
			loadNote(contentComp);
		}
		if (_currentReference.getIdentifiersMods() != null)
		{
			loadIdentifiers(contentComp);
		}
		if (_currentReference.getLocation() != null)
		{
			loadLocation(contentComp);
		}
		if (_currentReference.getAccessCondition() != null)
		{
			loadAccessCondition(contentComp);
		}
		if (_currentReference.getRelatedItems() != null
		// && _currentReference.getRelatedItems().size() > 0
		// && _currentReference.getRelatedItems().firstElement() != null
		)
		{
			loadRelatedItems(contentComp);
		}
		validate();
		_scrolledCompositeMain.setContent(contentComp);
		Point point = contentComp.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
		Point mp = _mainComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
		if (point.x > mp.x - 40)
		{
			point.x = mp.x - 40;
		}
		_scrolledCompositeMain.setMinSize(point);
		_scrolledCompositeMain.layout();
		_compositeRef.redraw();
		_compositeRef.layout();
		_compositeRef.pack();
		_compositeSourcePanel.layout();
		_compositeSourcePanel.pack();
		_mainComposite.layout();
		_mainComposite.pack();

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
			isModifiedOrNew = _idService.isModifiedOrNewObject(_currentReference.getPdrId());
		}
		catch (Exception e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (!_currentReference.isNew() && !isModifiedOrNew)
		{
			revision.setRef(_currentReference.getRecord().getRevisions().size());
			_currentReference.getRecord().getRevisions().add(revision);
			_currentReference.setDirty(true);
		}
		else
		{
			_currentReference.getRecord().getRevisions().lastElement().setTimeStamp(_facade.getCurrentDate());
			_currentReference.setDirty(true);
		}
		// hier wird injestet
		try
		{
			_facade.saveReference(_currentReference);
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public final void update(final Observable o, final Object arg)
	{
		loadValues(_currentReference);

	}

	/**
	 * Validate.
	 */
	public final void validate()
	{
		boolean valid = (_currentReference != null && _currentReference.isValid());
		if (_currentReference.getNameMods() != null && !_currentReference.getNameMods().isEmpty())
		{
			if (_delTitle != null && !_delTitle.isDisposed())
			{
				_delTitle.setEnabled(_mayWrite);
			}
			if (_decoValTi != null && _decoValTi.getControl() != null && !_decoValTi.getControl().isDisposed())
			{
				_decoValTi.setImage(null);
			}
		}
		else
		{
			if (_delTitle != null && !_delTitle.isDisposed())
			{
				_delTitle.setEnabled(false);
			}
			if (_decoValTi != null && _decoValTi.getControl() != null && !_decoValTi.getControl().isDisposed())
			{
				_decoValTi.setImage(FieldDecorationRegistry.getDefault()
						.getFieldDecoration(FieldDecorationRegistry.DEC_REQUIRED).getImage());
			}
		}

		if (_savebutton != null && !_savebutton.isDisposed())
		{
			_savebutton.setEnabled(valid && _mayWrite);
		}
	}

}
