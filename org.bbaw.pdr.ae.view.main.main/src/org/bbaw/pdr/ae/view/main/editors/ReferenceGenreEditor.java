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

import java.util.HashMap;
import java.util.Vector;

import org.bbaw.pdr.ae.common.AEVIEWConstants;
import org.bbaw.pdr.ae.common.CommonActivator;
import org.bbaw.pdr.ae.common.NLMessages;
import org.bbaw.pdr.ae.common.icons.IconsInternal;
import org.bbaw.pdr.ae.control.facade.Facade;
import org.bbaw.pdr.ae.model.Genre;
import org.bbaw.pdr.ae.model.NameMods;
import org.bbaw.pdr.ae.model.ReferenceMods;
import org.bbaw.pdr.ae.model.ReferenceModsTemplate;
import org.bbaw.pdr.ae.model.TitleInfo;
import org.bbaw.pdr.ae.view.control.provider.RefTemplateContentProvider;
import org.bbaw.pdr.ae.view.control.provider.RefTemplateLabelProvider;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.IHandlerService;

/**
 * The Class ReferenceGenreEditor.
 * @author Christoph Plutte
 */
public class ReferenceGenreEditor extends TitleAreaDialog
{

	/** The parent shell. */
	private Shell _parentShell;

	/** The _facade. */
	private Facade _facade = Facade.getInstanz();

	/** The image reg. */
	private ImageRegistry _imageReg = CommonActivator.getDefault().getImageRegistry();

	/** The genre table viewer. */
	private TableViewer _genreTableViewer;

	/** The current template. */
	private ReferenceModsTemplate _currentTemplate;

	/** The last template. */
	private ReferenceModsTemplate _lastTemplate;

	/** The main composite. */
	private Composite _mainComposite;

	/** The editor composite. */
	private Composite _editorComposite;

	/** The deleted templates. */
	private Vector<String> _deletedTemplates = new Vector<String>(2);

	/** The new button. */
	private Button _newButton;

	/** The delete button. */
	private Button _deleteButton;

	/** The ignore button. */
	private Button _ignoreButton;

	/** The genre label text. */
	private Text _genreLabelText;

	/** The genre value text. */
	private Text _genreValueText;

	/** The docu text en. */
	private Text _docuTextEN;

	/** The docu text de. */
	private Text _docuTextDE;

	/** The docu text it. */
	private Text _docuTextIT;

	/** The docu text fr. */
	private Text _docuTextFR;

	/** The icon buttons. */
	private Button[] _iconButtons;

	/** The source editor. */
	private SourceEditorDialog _sourceEditor;

	/** The genre text. */
	private Text _genreText;

	/** The _icons. */
	private String[] _icons = new String[]
	{IconsInternal.REFERENCE, IconsInternal.REFERENCE_ARTICLE, IconsInternal.REFERENCE_AUTOGRAPH,
			IconsInternal.REFERENCE_BOOK, IconsInternal.REFERENCE_CASSETTE, IconsInternal.REFERENCE_CD,
			IconsInternal.REFERENCE_COLLECTION, IconsInternal.REFERENCE_COMPOSITION, IconsInternal.REFERENCE_DATABASE,
			IconsInternal.REFERENCE_ENCYCLOPEDIA, IconsInternal.REFERENCE_IMAGE, IconsInternal.REFERENCE_JOKE,
			IconsInternal.REFERENCE_LETTER, IconsInternal.REFERENCE_MAP, IconsInternal.REFERENCE_MICROFILM,
			IconsInternal.REFERENCE_MOVIE, IconsInternal.REFERENCE_PERFORMANCE, IconsInternal.REFERENCE_PERIODICAL,
			IconsInternal.REFERENCE_THEATRE, IconsInternal.REFERENCE_BLUEFOLDER_MUCIC,
			IconsInternal.REFERENCE_BLUEFOLDERS, IconsInternal.REFERENCE_BLUEFOLDER,
			IconsInternal.REFERENCE_BLUEDOCUMENT_MUSIC, IconsInternal.REFERENCE_BLUEFOLDER_FILM,
			IconsInternal.REFERENCE_CLIPBOARD, IconsInternal.REFERENCE_DRAWER, IconsInternal.REFERENCE_FOLDER,
			IconsInternal.REFERENCE_FOLDERS, IconsInternal.REFERENCE_FOLDER_MUSIC, IconsInternal.REFERENCE_MUSIC_NOTE,
			IconsInternal.REFERENCE_PAPER_BAG, IconsInternal.REFERENCE_PIANO, IconsInternal.REFERENCE_BUILDING,
			IconsInternal.REFERENCE_CHAPTER, IconsInternal.REFERENCE_GAME, IconsInternal.REFERENCE_GROUP,
			IconsInternal.REFERENCE_NEWSPAPER, IconsInternal.REFERENCE_PERSON_FEMALE,
			IconsInternal.REFERENCE_PERSON_MALE};

	/**
	 * Instantiates a new reference genre editor.
	 * @param parentShell the parent shell
	 */
	public ReferenceGenreEditor(final Shell parentShell)
	{
		super(parentShell);
		this._parentShell = parentShell;
	}

	@Override
	public final void create()
	{
		super.create();
		// Set the title
		setTitle(NLMessages.getString("Editor_refGenreEditor_title"));
		// Set the message
		setMessage(NLMessages.getString("Editor_refGenreEditor_title_message"), IMessageProvider.INFORMATION);
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
		gridData.verticalAlignment = SWT.FILL;
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
				_facade.setReferenceModsTemplates(null);
				setReturnCode(CANCEL);
				close();
			}
		});
	}

	@Override
	protected final Control createDialogArea(final Composite parent)
	{

		parent.setSize(300, 200);
		_deletedTemplates.clear();
		SashForm mainSForm = new SashForm(parent, SWT.HORIZONTAL);
		mainSForm.setLayoutData(new GridData(GridData.FILL_BOTH));
		if (AEVIEWConstants.IS_SMALL_MONITOR_DIMENSION)
		{
			((GridData) mainSForm.getLayoutData()).minimumHeight = 350;
		}else
		{
			((GridData) mainSForm.getLayoutData()).minimumHeight = 600;
		}
		((GridData) mainSForm.getLayoutData()).minimumWidth = 800;

		_mainComposite = new Composite(mainSForm, SWT.NONE);
		_mainComposite.setLayoutData(new GridData());
		((GridData) _mainComposite.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) _mainComposite.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _mainComposite.getLayoutData()).grabExcessVerticalSpace = true;
		((GridData) _mainComposite.getLayoutData()).verticalAlignment = SWT.FILL;
		_mainComposite.setLayout(new GridLayout());
		((GridLayout) _mainComposite.getLayout()).numColumns = 1;
		((GridLayout) _mainComposite.getLayout()).makeColumnsEqualWidth = false;

		Composite upperComposite = new Composite(_mainComposite, SWT.NONE);
		upperComposite.setLayoutData(new GridData());
		((GridData) upperComposite.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) upperComposite.getLayoutData()).horizontalAlignment = SWT.FILL;
		upperComposite.setLayout(new GridLayout());
		((GridLayout) upperComposite.getLayout()).numColumns = 3;
		((GridLayout) upperComposite.getLayout()).makeColumnsEqualWidth = false;

		Label newLabel = new Label(upperComposite, SWT.NONE);
		newLabel.setLayoutData(new GridData());
		newLabel.setText(NLMessages.getString("Editor_new_genre"));

		_genreText = new Text(upperComposite, SWT.BORDER);
		_genreText.setLayoutData(new GridData());
		((GridData) _genreText.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) _genreText.getLayoutData()).horizontalAlignment = SWT.FILL;
		// @SuppressWarnings({ "unchecked", "rawtypes" })
		// final Vector<String> genres = new
		// Vector(Arrays.asList(_facade.getReferenceGenres()));
		_genreText.addKeyListener(new KeyListener()
		{

			@Override
			public void keyPressed(final KeyEvent e)
			{
			}

			@Override
			public void keyReleased(final KeyEvent e)
			{

				//				System.out.println("genre text " + _genreText.getText()); //$NON-NLS-1$
				if (_facade.getReferenceModsTemplates().containsKey(_genreText.getText()))
				{
					//					System.out.println("checking zwei"); //$NON-NLS-1$
					setMessage(NLMessages.getString("Editor_message_genre_exists"), SWT.ERROR);
					_newButton.setEnabled(false);
				}
				else
				{
					setMessage(""); //$NON-NLS-1$
					_newButton.setEnabled(true);
				}

			}

		});

		_newButton = new Button(upperComposite, SWT.PUSH);
		_newButton.setText(NLMessages.getString("Editor_new"));
		_newButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent e)
			{

				createNewGenre(_genreText.getText().trim());
			}
		});
		_newButton.pack();
		upperComposite.layout();
		upperComposite.pack();

		_editorComposite = new Composite(_mainComposite, SWT.NONE);
		_editorComposite.setLayoutData(new GridData());
		((GridData) _editorComposite.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) _editorComposite.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _editorComposite.getLayoutData()).grabExcessVerticalSpace = true;
		((GridData) _editorComposite.getLayoutData()).verticalAlignment = SWT.FILL;
		_editorComposite.setLayout(new GridLayout());
		((GridLayout) _editorComposite.getLayout()).numColumns = 1;
		((GridLayout) _editorComposite.getLayout()).makeColumnsEqualWidth = false;

		_genreTableViewer = new TableViewer(_editorComposite, SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);

		String[] titles =
		{"Displayname"}; //$NON-NLS-1$ //$NON-NLS-2$
		int[] bounds =
		{180};

		for (int i = 0; i < titles.length; i++)
		{
			TableViewerColumn column = new TableViewerColumn(_genreTableViewer, SWT.NONE);
			column.getColumn().setText(titles[i]);
			column.getColumn().setWidth(bounds[i]);
			column.getColumn().setResizable(false);
			column.getColumn().setMoveable(true);
			// column.getColumn().addSelectionListener(getSelectionAdapter(_genreTableViewer,
			// column.getColumn(), i));

		}
		// _genreTableViewer.setComparator(new PdrObjectViewComparator());

		Table table = _genreTableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(false);
		table.setLayoutData(new GridData());
		((GridData) table.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) table.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) table.getLayoutData()).verticalAlignment = SWT.FILL;
		((GridData) table.getLayoutData()).grabExcessVerticalSpace = true;

		_genreTableViewer.addSelectionChangedListener(new ISelectionChangedListener()
		{
			@Override
			public void selectionChanged(final SelectionChangedEvent event)
			{
				ISelection selection = _genreTableViewer.getSelection();
				Object obj = ((IStructuredSelection) selection).getFirstElement();
				if (_currentTemplate != null)
				{
					_lastTemplate = _currentTemplate;
					if (_sourceEditor.getCurrentReference() != null)
					{
						_lastTemplate.setRefTemplate(_sourceEditor.getCurrentReference());
					}
				}
				_currentTemplate = (ReferenceModsTemplate) obj;
				if (_currentTemplate != null)
				{
					loadValues();
				}

			}
		});
		_genreTableViewer.setContentProvider(new RefTemplateContentProvider(true));
		_genreTableViewer.setLabelProvider(new RefTemplateLabelProvider());
		_genreTableViewer.setComparator(null);
		_genreTableViewer.setInput(_facade.getReferenceModsTemplates());

		_genreTableViewer.refresh();
		_editorComposite.layout();

		Composite lowerLeftComposite = new Composite(_mainComposite, SWT.NONE);
		lowerLeftComposite.setLayoutData(new GridData());
		((GridData) lowerLeftComposite.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) lowerLeftComposite.getLayoutData()).horizontalAlignment = SWT.FILL;
		lowerLeftComposite.setLayout(new GridLayout());
		((GridLayout) lowerLeftComposite.getLayout()).numColumns = 2;
		((GridLayout) lowerLeftComposite.getLayout()).makeColumnsEqualWidth = false;

		_deleteButton = new Button(lowerLeftComposite, SWT.PUSH);
		_deleteButton.setText(NLMessages.getString("Editor_delete"));
		_deleteButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent e)
			{
				String message = NLMessages.getString("Editor_delete_refTemplate");
				message += "\n";
				message += NLMessages.getString("Editor_delte_refTemplate_warning") + "\n"
						+ NLMessages.getString("Editor_delte_refTemplate_warning2") + "\n\n"
						+ NLMessages.getString("Editor_delte_refTemplate_warning3");
				MessageDialog messageDialog = new MessageDialog(_parentShell, NLMessages
						.getString("Editor_delete_refTemplate"), null, message, MessageDialog.WARNING, new String[]
				{NLMessages.getString("Editor_delete"), NLMessages.getString("Editor_cancel")}, 1);
				if (messageDialog.open() == 0)
				{
					_deletedTemplates.add(_currentTemplate.getValue());
					_facade.getReferenceModsTemplates().remove(_currentTemplate.getValue());

					_currentTemplate = _lastTemplate;
					_genreTableViewer.setInput(_facade.getReferenceModsTemplates());
					_genreTableViewer.refresh();
					if (_currentTemplate != null)
					{
						StructuredSelection sel = new StructuredSelection(_currentTemplate);
						_genreTableViewer.setSelection(sel);
						loadValues();
					}
				}
			}
		});
		_deleteButton.pack();

		Button exportButton = new Button(lowerLeftComposite, SWT.PUSH);
		exportButton.setText("Export");
		exportButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent ee)
			{
				IHandlerService handlerService = (IHandlerService) PlatformUI.getWorkbench().getService(
						IHandlerService.class);
				try
				{
					handlerService.executeCommand(
							"org.bbaw.pdr.ae.view.main.commands.ExportReferenceModsTemplates", null); //$NON-NLS-1$
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
		exportButton.pack();
		upperComposite.layout();
		upperComposite.pack();

		TabFolder mainTabFolder = new TabFolder(mainSForm, SWT.TOP | SWT.FILL);
		GridData gridData2 = new GridData();
		gridData2.verticalAlignment = GridData.FILL;
		gridData2.horizontalSpan = 1;
		gridData2.grabExcessHorizontalSpace = true;
		gridData2.grabExcessVerticalSpace = true;
		gridData2.horizontalAlignment = SWT.FILL;
		mainTabFolder.setLayoutData(gridData2);

		TabItem genreFormTabItem = new TabItem(mainTabFolder, SWT.NONE);
		genreFormTabItem.setText(NLMessages.getString("Editor_genre_formular")); //$NON-NLS-1$

		Composite rightComposite = new Composite(mainTabFolder, SWT.NONE);
		rightComposite.setLayoutData(new GridData());
		((GridData) rightComposite.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) rightComposite.getLayoutData()).horizontalAlignment = SWT.FILL;
		rightComposite.setLayout(new GridLayout());
		((GridLayout) rightComposite.getLayout()).numColumns = 1;
		((GridLayout) rightComposite.getLayout()).makeColumnsEqualWidth = false;

		genreFormTabItem.setControl(rightComposite);

		Composite topRightComposite = new Composite(rightComposite, SWT.NONE);
		topRightComposite.setLayoutData(new GridData());
		((GridData) topRightComposite.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) topRightComposite.getLayoutData()).horizontalAlignment = SWT.FILL;
		topRightComposite.setLayout(new GridLayout());
		((GridLayout) topRightComposite.getLayout()).numColumns = 6;
		((GridLayout) topRightComposite.getLayout()).makeColumnsEqualWidth = false;

		Label genreLabelL = new Label(topRightComposite, SWT.NONE);
		genreLabelL.setLayoutData(new GridData());
		genreLabelL.setText(NLMessages.getString("Editor_genre_label"));

		_genreLabelText = new Text(topRightComposite, SWT.BORDER);
		_genreLabelText.setLayoutData(new GridData());
		((GridData) _genreLabelText.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) _genreLabelText.getLayoutData()).horizontalAlignment = SWT.FILL;

		_genreLabelText.addKeyListener(new KeyListener()
		{

			@Override
			public void keyPressed(final KeyEvent e)
			{
			}

			@Override
			public void keyReleased(final KeyEvent e)
			{

				//				System.out.println("genre text " + _genreText.getText()); //$NON-NLS-1$
				if (_facade.getReferenceModsTemplates().containsKey(_genreLabelText.getText()))
				{
					setMessage(NLMessages.getString("Editor_message_genre_exists"), SWT.ERROR);
				}
				else
				{
					setMessage(""); //$NON-NLS-1$
					_currentTemplate.setLabel(_genreLabelText.getText());
					_genreTableViewer.refresh();

				}

			}

		});
		Label genreValueL = new Label(topRightComposite, SWT.NONE);
		genreValueL.setLayoutData(new GridData());
		genreValueL.setText(NLMessages.getString("Editor_genre_value"));

		_genreValueText = new Text(topRightComposite, SWT.BORDER);
		_genreValueText.setLayoutData(new GridData());
		((GridData) _genreValueText.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) _genreValueText.getLayoutData()).horizontalAlignment = SWT.FILL;
		_genreValueText.setEditable(false);
		_genreValueText.addKeyListener(new KeyListener()
		{

			@Override
			public void keyPressed(final KeyEvent e)
			{
			}

			@Override
			public void keyReleased(final KeyEvent e)
			{

				//				System.out.println("genre text " + _genreText.getText()); //$NON-NLS-1$
				if (_facade.getReferenceModsTemplates().containsKey(_genreValueText.getText()))
				{
					setMessage(NLMessages.getString("Editor_message_genre_exists"), SWT.ERROR);
				}
				else
				{
					setMessage(""); //$NON-NLS-1$
					_currentTemplate.setLabel(_genreValueText.getText());
					_genreTableViewer.refresh();

				}

			}

		});
		Label ignoreLabel = new Label(topRightComposite, SWT.NONE);
		ignoreLabel.setText(NLMessages.getString("Editor_ignore"));
		ignoreLabel.setLayoutData(new GridData());

		_ignoreButton = new Button(topRightComposite, SWT.CHECK);
		_ignoreButton.setLayoutData(new GridData());
		// _ignoreButton.setEnabled(_userRichtsChecker.maySetConfigIgnored());
		((GridData) _ignoreButton.getLayoutData()).horizontalSpan = 1;
		_ignoreButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent se)
			{
				_currentTemplate.setIgnore(_ignoreButton.getSelection());
				_genreTableViewer.refresh();

			}
		}); // SelectionListener

		Composite lowerRightComposite = new Composite(rightComposite, SWT.NONE);
		lowerRightComposite.setLayoutData(new GridData());
		((GridData) lowerRightComposite.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) lowerRightComposite.getLayoutData()).horizontalAlignment = SWT.FILL;
		lowerRightComposite.setLayout(new GridLayout());
		((GridLayout) lowerRightComposite.getLayout()).numColumns = 1;
		((GridLayout) lowerRightComposite.getLayout()).makeColumnsEqualWidth = false;

		_sourceEditor = new SourceEditorDialog(_parentShell, null, true);
		_sourceEditor.create();
		_sourceEditor.createDialogArea(lowerRightComposite);

		TabItem docuTabItem = new TabItem(mainTabFolder, SWT.NONE);
		docuTabItem.setText(NLMessages.getString("Editor_genre_documentation")); //$NON-NLS-1$

		Composite docuComposite = new Composite(mainTabFolder, SWT.NONE);
		docuComposite.setLayoutData(new GridData());
		((GridData) docuComposite.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) docuComposite.getLayoutData()).horizontalAlignment = SWT.FILL;
		docuComposite.setLayout(new GridLayout());
		((GridLayout) docuComposite.getLayout()).numColumns = 1;
		((GridLayout) docuComposite.getLayout()).makeColumnsEqualWidth = false;

		docuTabItem.setControl(docuComposite);

		Label docuLabelDE = new Label(docuComposite, SWT.NONE);
		docuLabelDE.setLayoutData(new GridData());
		((GridData) docuLabelDE.getLayoutData()).horizontalAlignment = SWT.LEFT;
		docuLabelDE.setText("Deutsch"); //$NON-NLS-1$

		_docuTextDE = new Text(docuComposite, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		_docuTextDE.setLayoutData(new GridData());
		((GridData) _docuTextDE.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) _docuTextDE.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _docuTextDE.getLayoutData()).heightHint = 50;

		_docuTextDE.addFocusListener(new FocusListener()
		{
			@Override
			public void focusGained(final FocusEvent e)
			{

			}

			@Override
			public void focusLost(final FocusEvent e)
			{
				if (_currentTemplate.getDocumentation() == null)
				{
					_currentTemplate.setDocumentation(new HashMap<String, String>());
				}
				_currentTemplate.getDocumentation().put("de", _docuTextDE.getText()); //$NON-NLS-1$
			}
		});

		Label docuLabelEN = new Label(docuComposite, SWT.NONE);
		docuLabelEN.setLayoutData(new GridData());
		docuLabelEN.setText("English"); //$NON-NLS-1$

		_docuTextEN = new Text(docuComposite, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		_docuTextEN.setLayoutData(new GridData());
		((GridData) _docuTextEN.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) _docuTextEN.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _docuTextEN.getLayoutData()).heightHint = 50;

		_docuTextEN.addFocusListener(new FocusListener()
		{
			@Override
			public void focusGained(final FocusEvent e)
			{

			}

			@Override
			public void focusLost(final FocusEvent e)
			{
				if (_currentTemplate.getDocumentation() == null)
				{
					_currentTemplate.setDocumentation(new HashMap<String, String>());
				}
				_currentTemplate.getDocumentation().put("en", _docuTextEN.getText()); //$NON-NLS-1$
			}
		});

		Label docuLabelIT = new Label(docuComposite, SWT.NONE);
		docuLabelIT.setLayoutData(new GridData());
		((GridData) docuLabelIT.getLayoutData()).horizontalAlignment = SWT.LEFT;
		docuLabelIT.setText("Italiano"); //$NON-NLS-1$

		_docuTextIT = new Text(docuComposite, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		_docuTextIT.setLayoutData(new GridData());
		((GridData) _docuTextIT.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) _docuTextIT.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _docuTextIT.getLayoutData()).heightHint = 50;

		_docuTextIT.addFocusListener(new FocusListener()
		{
			@Override
			public void focusGained(final FocusEvent e)
			{

			}

			@Override
			public void focusLost(final FocusEvent e)
			{
				if (_currentTemplate.getDocumentation() == null)
				{
					_currentTemplate.setDocumentation(new HashMap<String, String>());
				}
				_currentTemplate.getDocumentation().put("de", _docuTextIT.getText()); //$NON-NLS-1$
			}
		});
		Label docuLabelFR = new Label(docuComposite, SWT.NONE);
		docuLabelFR.setLayoutData(new GridData());
		((GridData) docuLabelFR.getLayoutData()).horizontalAlignment = SWT.LEFT;
		docuLabelFR.setText("Francais"); //$NON-NLS-1$

		_docuTextFR = new Text(docuComposite, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		_docuTextFR.setLayoutData(new GridData());
		((GridData) _docuTextFR.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) _docuTextFR.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _docuTextFR.getLayoutData()).heightHint = 50;

		_docuTextFR.addFocusListener(new FocusListener()
		{
			@Override
			public void focusGained(final FocusEvent e)
			{

			}

			@Override
			public void focusLost(final FocusEvent e)
			{
				if (_currentTemplate.getDocumentation() == null)
				{
					_currentTemplate.setDocumentation(new HashMap<String, String>());
				}
				_currentTemplate.getDocumentation().put("de", _docuTextFR.getText()); //$NON-NLS-1$
			}
		});
		_docuTextDE.setEnabled(false);
		_docuTextEN.setEnabled(false);
		_docuTextIT.setEnabled(false);
		_docuTextFR.setEnabled(false);

		TabItem iconTabItem = new TabItem(mainTabFolder, SWT.NONE);
		iconTabItem.setText(NLMessages.getString("Editor_genre_icon")); //$NON-NLS-1$

		Composite iconComposite = new Composite(mainTabFolder, SWT.NONE);
		iconComposite.setLayoutData(new GridData());
		((GridData) iconComposite.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) iconComposite.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) iconComposite.getLayoutData()).grabExcessVerticalSpace = true;
		((GridData) iconComposite.getLayoutData()).verticalAlignment = SWT.FILL;
		iconComposite.setLayout(new GridLayout());
		((GridLayout) iconComposite.getLayout()).numColumns = 8;
		((GridLayout) iconComposite.getLayout()).makeColumnsEqualWidth = true;
		((GridLayout) iconComposite.getLayout()).marginHeight = 12;
		((GridLayout) iconComposite.getLayout()).marginWidth = 12;
		((GridLayout) iconComposite.getLayout()).horizontalSpacing = 35;

		((GridLayout) iconComposite.getLayout()).verticalSpacing = 35;

		iconTabItem.setControl(iconComposite);

		Label iconLabel = new Label(iconComposite, SWT.NONE);
		iconLabel.setLayoutData(new GridData());
		((GridData) iconLabel.getLayoutData()).horizontalAlignment = SWT.LEFT;
		((GridData) iconLabel.getLayoutData()).horizontalSpan = 8;

		iconLabel.setText(NLMessages.getString("Editor_genre_icon_info"));
		SelectionListener iconListener = new SelectionAdapter()
		{
			@Override
			public void widgetDefaultSelected(final SelectionEvent e)
			{
			}

			@Override
			public void widgetSelected(final SelectionEvent e)
			{
				final String ico = (String) ((Button) e.getSource()).getData("icon"); //$NON-NLS-1$
				if (_currentTemplate != null)
				{
					_currentTemplate.setImageString(ico);
					_genreTableViewer.refresh();
				}

			}
		};
		_iconButtons = new Button[_icons.length];
		for (int i = 0; i < _icons.length; i++)
		{
			String icon = _icons[i];

			_iconButtons[i] = new Button(iconComposite, SWT.RADIO | SWT.CENTER);
			_iconButtons[i].setData("icon", icon); //$NON-NLS-1$
			_iconButtons[i].setImage(_imageReg.get(icon));
			_iconButtons[i].addSelectionListener(iconListener);
			_iconButtons[i].setLayoutData(new GridData());
			_iconButtons[i].pack();
		}
		iconComposite.layout();
		mainSForm.setWeights(new int[]
		{1, 3});

		_editorComposite.layout();
		parent.pack();
		return parent;
	}

	/**
	 * Creates the new genre.
	 * @param genre the genre
	 */
	protected final void createNewGenre(final String genre)
	{
		ReferenceModsTemplate template = new ReferenceModsTemplate();
		Genre g = new Genre();
		g.setAuthority("PDR"); //$NON-NLS-1$
		g.setGenre(genre);
		template.setValue(genre);
		template.setLabel(genre);
		ReferenceMods ref = new ReferenceMods(g);
		ref.setTitleInfo(new TitleInfo());
		ref.getTitleInfo().setTitle(" "); //$NON-NLS-1$
		NameMods name = new NameMods(2);
		ref.setNameMods(new Vector<NameMods>(1));
		ref.getNameMods().add(name);
		template.setRefTemplate(ref);

		if (_currentTemplate != null)
		{
			_lastTemplate = _currentTemplate;
			if (_sourceEditor.getCurrentReference() != null)
			{
				_lastTemplate.setRefTemplate(_sourceEditor.getCurrentReference());
			}
		}
		_currentTemplate = template;
		_facade.getReferenceModsTemplates().put(genre, template);
		_genreTableViewer.setInput(_facade.getReferenceModsTemplates());
		_genreTableViewer.refresh();
		loadValues();
	}

	/**
	 * meth creates the OK button.
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
				//				System.out.println("OK pressed"); //$NON-NLS-1$
				if (isValidInput())
				{
					okPressed();
				}
			}
		});

		setButtonLayoutData(button);
		return button;
	}

	@Override
	protected final boolean isResizable()
	{
		return true;
	}

	/**
	 * meth. checks whether selection is valid. true if facade.getRelObjTyp 0 or
	 * 1 and one aspect or one person is selected. if facade.getRelObjTyp is 2,
	 * returns true only if a source is selected.
	 * @return boolean valid.
	 */
	private boolean isValidInput()
	{
		boolean valid = true;

		return valid;
	}

	/**
	 * Load values.
	 */
	protected final void loadValues()
	{
		_ignoreButton.setSelection(_currentTemplate.isIgnore());
		_genreLabelText.setText(_currentTemplate.getLabel());
		_genreValueText.setText(_currentTemplate.getValue());
		_sourceEditor.loadValues(_currentTemplate.getRefTemplate());

		if (_currentTemplate.getDocumentation() != null && _currentTemplate.getDocumentation().containsKey("de")) //$NON-NLS-1$
		{
			_docuTextDE.setText(_currentTemplate.getDocumentation().get("de").trim()); //$NON-NLS-1$
		}
		else
		{
			_docuTextDE.setText(""); //$NON-NLS-1$
		}

		if (_currentTemplate.getDocumentation() != null && _currentTemplate.getDocumentation().containsKey("en")) //$NON-NLS-1$
		{
			_docuTextEN.setText(_currentTemplate.getDocumentation().get("en").trim()); //$NON-NLS-1$
		}
		else
		{
			_docuTextEN.setText(""); //$NON-NLS-1$
		}
		if (_currentTemplate.getDocumentation() != null && _currentTemplate.getDocumentation().containsKey("it")) //$NON-NLS-1$
		{
			_docuTextIT.setText(_currentTemplate.getDocumentation().get("it").trim()); //$NON-NLS-1$
		}
		else
		{
			_docuTextIT.setText(""); //$NON-NLS-1$
		}
		if (_currentTemplate.getDocumentation() != null && _currentTemplate.getDocumentation().containsKey("fr")) //$NON-NLS-1$
		{
			_docuTextFR.setText(_currentTemplate.getDocumentation().get("fr").trim()); //$NON-NLS-1$
		}
		else
		{
			_docuTextFR.setText(""); //$NON-NLS-1$
		}
		if (_currentTemplate.getImageString() != null)
		{
			setRadioByString(_iconButtons, _currentTemplate.getImageString());
		}
		_docuTextDE.setEnabled(true);
		_docuTextEN.setEnabled(true);
		_docuTextIT.setEnabled(true);
		_docuTextFR.setEnabled(true);
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
		if (!_deletedTemplates.isEmpty())
		{
			for (String s : _deletedTemplates)
			{
				_facade.delelteRefTemplate(s);
			}

		}

		if (_currentTemplate != null)
		{
			_currentTemplate.setRefTemplate(_sourceEditor.getCurrentReference());
		}
		_facade.saveReferenceTemplates();

		_facade.setReferenceModsTemplates(null);
	}

	/**
	 * Sets the radio by string.
	 * @param radios the radios
	 * @param imageString the image string
	 */
	private void setRadioByString(final Button[] radios, final String imageString)
	{
		for (int i = 0; i < _icons.length; i++)
		{
			if (_icons[i].equals(imageString))
			{
				radios[i].setSelection(true);
			}
			else
			{
				radios[i].setSelection(false);
			}

		}

	}

}
