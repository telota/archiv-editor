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

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.bbaw.pdr.ae.common.AEConstants;
import org.bbaw.pdr.ae.common.AEPluginIDs;
import org.bbaw.pdr.ae.common.AEVIEWConstants;
import org.bbaw.pdr.ae.common.CommonActivator;
import org.bbaw.pdr.ae.common.NLMessages;
import org.bbaw.pdr.ae.common.icons.IconsInternal;
import org.bbaw.pdr.ae.config.model.ConfigData;
import org.bbaw.pdr.ae.config.model.ConfigItem;
import org.bbaw.pdr.ae.config.model.DataType;
import org.bbaw.pdr.ae.control.core.PDRConfigProvider;
import org.bbaw.pdr.ae.control.core.UserRichtsChecker;
import org.bbaw.pdr.ae.control.facade.Facade;
import org.bbaw.pdr.ae.control.interfaces.AMainSearcher;
import org.bbaw.pdr.ae.control.interfaces.IPdrIdService;
import org.bbaw.pdr.ae.metamodel.IAEPresentable;
import org.bbaw.pdr.ae.metamodel.PdrDate;
import org.bbaw.pdr.ae.metamodel.PdrId;
import org.bbaw.pdr.ae.metamodel.Record;
import org.bbaw.pdr.ae.metamodel.Revision;
import org.bbaw.pdr.ae.model.Aspect;
import org.bbaw.pdr.ae.model.PdrObject;
import org.bbaw.pdr.ae.model.Person;
import org.bbaw.pdr.ae.model.Place;
import org.bbaw.pdr.ae.model.Reference;
import org.bbaw.pdr.ae.model.ReferenceMods;
import org.bbaw.pdr.ae.model.Relation;
import org.bbaw.pdr.ae.model.RelationDim;
import org.bbaw.pdr.ae.model.RelationStm;
import org.bbaw.pdr.ae.model.SemanticDim;
import org.bbaw.pdr.ae.model.SemanticStm;
import org.bbaw.pdr.ae.model.SpatialDim;
import org.bbaw.pdr.ae.model.SpatialStm;
import org.bbaw.pdr.ae.model.TaggingRange;
import org.bbaw.pdr.ae.model.Time;
import org.bbaw.pdr.ae.model.TimeDim;
import org.bbaw.pdr.ae.model.TimeStm;
import org.bbaw.pdr.ae.model.User;
import org.bbaw.pdr.ae.model.Validation;
import org.bbaw.pdr.ae.model.ValidationStm;
import org.bbaw.pdr.ae.model.view.Facet;
import org.bbaw.pdr.ae.model.view.UndoInformation;
import org.bbaw.pdr.ae.view.control.ControlExtensions;
import org.bbaw.pdr.ae.view.control.ViewHelper;
import org.bbaw.pdr.ae.view.control.customSWTWidges.YearSpinner;
import org.bbaw.pdr.ae.view.control.dialogs.CharMapDialog;
import org.bbaw.pdr.ae.view.control.dialogs.SelectObjectDialog;
import org.bbaw.pdr.ae.view.control.interfaces.IDateParser;
import org.bbaw.pdr.ae.view.control.interfaces.IMarkupEditor;
import org.bbaw.pdr.ae.view.control.provider.AutoCompleteNameLabelProvider;
import org.bbaw.pdr.ae.view.control.provider.FacetContentProposalProvider;
import org.bbaw.pdr.ae.view.control.provider.MarkupContentProvider;
import org.bbaw.pdr.ae.view.control.provider.MarkupLabelProvider;
import org.bbaw.pdr.ae.view.control.provider.MarkupListContentProposalProvider;
import org.bbaw.pdr.ae.view.control.provider.MarkupListLabelProvider;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IParameter;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.Parameterization;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
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
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
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
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.IHandlerService;
import org.xml.sax.SAXException;

/**
 * @author cplutte class creates the dialog of the editor for Aspects.
 */
public class AspectEditorDialog extends TitleAreaDialog implements ISelectionProvider
{
	/** ID of editor. */
	public static final String ID = "org.bbaw.pdr.ae.view.main.editors.AspectEditorTab"; //$NON-NLS-1$
	/** local copy(clone) of current Aspect. */
	private Aspect _currentAspect;

	/** date format of administrative dates in PDR. */
	private SimpleDateFormat _adminDateFormat = AEConstants.ADMINDATE_FORMAT;

	/** singleton instance of _facade. */
	private Facade _facade = Facade.getInstanz();

	/** instance of pdrIdService. */
	private IPdrIdService _idService = _facade.getIdService();
	/** MainSearcher. */
	private AMainSearcher _mainSearcher = _facade.getMainSearcher();
	/** Instance of shared image registry. */
	private ImageRegistry _imageReg = CommonActivator.getDefault().getImageRegistry();
	// /** Resource Manager for colors and fonts. */
	// private LocalResourceManager _resources = new
	// LocalResourceManager(JFaceResources.getResources());

	/** The WHIT e_ color. */
	private static final Color WHITE_COLOR = Display.getDefault().getSystemColor(SWT.COLOR_WHITE);
	/** cache for tagged dates that have been added since last save. */
	private CopyOnWriteArrayList<TaggingRange> _taggedDateCache = new CopyOnWriteArrayList<TaggingRange>();

	/** cache for tagged place3s that have been added since last save. */
	private CopyOnWriteArrayList<TaggingRange> _taggedPlaceCache = new CopyOnWriteArrayList<TaggingRange>();
	
	private TaggingRange[] _selectedTaggingRanges = null;

	/** The prefered year. */
	private int _preferedYear;

	/** The _customize favorite markup. */
	private Action _undoAction, _redoAction, _customizeFavoriteMarkup, _createPersonFromString;

	/** main Tabfolder. */
	private CTabFolder _mainTabFolder;

	/** Tabitems of mainTabFolder. */
	private CTabItem _frontTabItem;

	/** The dimension tab item. */
	private CTabItem _dimensionTabItem;

	/** The relation tab item. */
	private CTabItem _relationTabItem;

	/** The tagging tab item. */
	private CTabItem _taggingTabItem;

	/** The source tab item. */
	private CTabItem _sourceTabItem;

	/** The rights tab item. */
	private CTabItem _rightsTabItem;

	/** The admin tab item. */
	private CTabItem _adminTabItem;

	/** The grid layout. */
	private GridLayout _gridLayout;

	/** The grid data. */
	private GridData _gridData;

	/** The grid layout2. */
	private GridLayout _gridLayout2;

	/** Label of which the tooltip shows the revision history. */
	private Label _historyLabel;

	/** combos for tagging. */
	private Combo _comboTaggingElement;

	/** The combo tagging type. */
	private Combo _comboTaggingType;

	/** The combo tagging subtype. */
	private Combo _comboTaggingSubtype;

	/** The combo tagging role. */
	private Combo _comboTaggingRole;

	/** The text tagging key. */
	private Text _textTaggingKey;

	/** The quick select text. */
	private Text _quickSelectText;
	// private Combo comboTaggingContent;
	/** The combo t date day. */
	private Combo _comboTDateDay;

	/** The combo t date month. */
	private Combo _comboTDateMonth;

	/** The combo t date range from day. */
	private Combo _comboTDateRangeFromDay;

	/** The combo t date range from month. */
	private Combo _comboTDateRangeFromMonth;

	/** The combo t date range to day. */
	private Combo _comboTDateRangeToDay;

	/** The combo t date range to month. */
	private Combo _comboTDateRangeToMonth;

	/** The combo t date point ot. */
	private Combo _comboTDatePointOT;

	/** The combo t date before. */
	private Combo _comboTDateBefore;

	/** The combo t date after. */
	private Combo _comboTDateAfter;

	/** The combo tagging element viewer. */
	private ComboViewer _comboTaggingElementViewer;

	/** The combo tagging type viewer. */
	private ComboViewer _comboTaggingTypeViewer;

	/** The combo tagging subtype viewer. */
	private ComboViewer _comboTaggingSubtypeViewer;

	/** The combo tagging role viewer. */
	private ComboViewer _comboTaggingRoleViewer;

	/** The combo viewer t date point ot. */
	private ComboViewer _comboViewerTDatePointOT;

	/** The combo viewer t date before. */
	private ComboViewer _comboViewerTDateBefore;

	/** The combo viewer t date after. */
	private ComboViewer _comboViewerTDateAfter;

	/** The spinner t date year. */
	private YearSpinner _spinnerTDateYear;

	/** The spinner t date range from year. */
	private YearSpinner _spinnerTDateRangeFromYear;

	/** The spinner t date range to year. */
	private YearSpinner _spinnerTDateRangeToYear;

	/** Labels for each Tagging combo that contain tooltip. */
	private ControlDecoration _elementDeco;

	/** The type deco. */
	private ControlDecoration _typeDeco;

	/** The subtype deco. */
	private ControlDecoration _subtypeDeco;

	/** The role deco. */
	private ControlDecoration _roleDeco;

	/** The content text. */
	private Text _contentText;

	/** The revision time text. */
	private Text _revisionTimeText;

	/** The revisor name. */
	private Text _revisorName;

	/** The creation time text. */
	private Text _creationTimeText;

	/** The creator name text. */
	private Text _creatorNameText;

	/** The pdr id. */
	private Text _pdrID;

	/** The text tagging ana. */
	private Text _textTaggingAna;

	/** The tagging group. */
	private Group _taggingGroup;

	/** The egg. */
	private String _egg;

	private String _selectedText;

	private Person _currentPerson;

	/** The e. */
	private boolean _e = false;

	/** The composite tagging buttons. */
	private Composite _compositeTaggingButtons;

	/** The composite tagging date. */
	private Composite _compositeTaggingDate;

	/** The composite t date range. */
	private Composite _compositeTDateRange;

	/** The composite t date. */
	private Composite _compositeTDate;

	/** The tagging1 coposite. */
	private Composite _tagging1Coposite;

	/** The composite tagging panel. */
	private Composite _compositeTaggingPanel;

	/** The composite tagging place. */
	private Composite _compositeTaggingPlace;

	/** The composite empty. */
	private Composite _compositeEmpty;

	/** The front composite. */
	private Composite _frontComposite;

	/** The dimension composite. */
	private Composite _dimensionComposite;

	/** The relation composite. */
	private Composite _relationComposite;

	/** The classification composite. */
	private Composite _classificationComposite;

	/** The source composite. */
	private Composite _sourceComposite;

	/** The rights composite. */
	private Composite _rightsComposite;

	/** The admin composite. */
	private Composite _adminComposite;

	/** boolean if pointOfTime. */
	private boolean _pointOfTime = false;
	/** boolean if rangeOfTime. */
	private boolean _rangeOfTime = false;

	/** scrollable composite for classification. */
	private ScrolledComposite _scrollCompClass;
	/** scroll composite for relation dimension. */
	private ScrolledComposite _scrollCompRel;
	/** scroll composite for validation . */
	private ScrolledComposite _scrollCompVal;
	/** scroll composite for place dimension . */
	private ScrolledComposite _scrollCompTimePlace;

	/** groups for each relationStatement. */
	private Group _relGroup;
	/** group for timeStms. */
	private Group _timeGroup;
	/** group for spatialStm. */
	private Group _placeGroup;

	/** The markup provider. */
	private String _markupProvider = Platform
			.getPreferencesService()
			.getString(CommonActivator.PLUGIN_ID, "PRIMARY_TAGGING_PROVIDER", AEConstants.TAGGING_LIST_PROVIDER, null).toUpperCase(); //$NON-NLS-1$;

	/** The relation provider. */
	private String _relationProvider;

	/** The semantic provider. */
	private String _semanticProvider;

	/** The content proposal. */
	private String _contentProposal;

	/** composite for rights TabItem. */
	private Composite _rightsTableComposite;

	/** stacklayout for extra tagging settings such as date, place. */
	private StackLayout _taggingStackLayout;

	/** The button tagging delete. */
	private Button _buttonTaggingDelete;

	/** The button tagging set. */
	private Button _buttonTaggingSet;

	/** The button tagging insert set. */
	private Button _buttonTaggingInsertSet;

	/** The button t date. */
	private Button _buttonTDate;

	/** The button t date range. */
	private Button _buttonTDateRange;

	/** The save button. */
	private Button _saveButton;

	/** The add relations button. */
	private Button _addRelationsButton;

	/** The add belongs to button. */
	private Button _addBelongsToButton;

	/** The add further classifier. */
	private Button _addFurtherClassifier;

	/** The add time stm button. */
	private Button _addTimeStmButton;

	/** The add spatial stm button. */
	private Button _addSpatialStmButton;

	/** The add references button. */
	private Button _addReferencesButton;

	/** The find ana. */
	private Button _findAna;

	/** The find key. */
	private Button _findKey;

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

	/** Strings for tagging list selection via TaggingListProcessor. */
	/** name of selected list. */
	private String _rListName = ""; //$NON-NLS-1$

	/** The e list name. */
	private String _eListName = ""; //$NON-NLS-1$

	/** The t list name. */
	private String _tListName = ""; //$NON-NLS-1$

	/** The s list name. */
	private String _sListName = ""; //$NON-NLS-1$

	/** The message. */
	private String _message;

	/** The front viewed. */
	private boolean _frontViewed;

	/** The source viewed. */
	private boolean _sourceViewed;

	/** The rel viewed. */
	private boolean _relViewed;

	/** The time place viewed. */
	private boolean _timePlaceViewed;

	/** The clas view. */
	private boolean _clasView;

	/** The source view. */
	private boolean _sourceView;

	/** The rel view. */
	private boolean _relView;

	/** The may write. */
	private boolean _mayWrite;

	/** The time place view. */
	private boolean _timePlaceView;

	/** The whens. */
	private final String[] _whens = new String[]
	{"when", "notBefore", "notAfter"}; //$NON-NLS-1$

	/** The befores. */
	private final String[] _befores = new String[]
	{"from", "notBefore"}; //$NON-NLS-1$

	/** The afters. */
	private final String[] _afters = new String[]
	{"to", "notAfter"}; //$NON-NLS-1$

	/** The PRESELECTE d_ year. */
	private static final int PRESELECTED_YEAR = Platform.getPreferencesService().getInt(CommonActivator.PLUGIN_ID,
			"ASPECT_PRESELECTED_DATE_YEAR", AEConstants.ASPECT_PRESELECTED_DATE_YEAR, null);



	/** The _markup editor. */
	private IMarkupEditor _markupEditor;

	/** The _date parser. */
	private IDateParser _dateParser = new ControlExtensions().getDateParser();
	
	/* Undo/Redo */
	/** The UND o_ stacksize. */
	private static final int UNDO_STACKSIZE = 50;
	/** The _stack undo. */
	private Stack<UndoInformation> _stackUndo;

	/** The _stack redo. */
	private Stack<UndoInformation> _stackRedo;
	/** The _protect redo stack. */
	private boolean _protectRedoStack;
	private Button _buttonTaggingInsertMarkup;
	private Action _insertSpecialCharAction;
	private Button _symbolButton;
	private Composite _editorComposite;

	/**
	 * Instantiates a new aspect editor dialog.
	 * @param parentShell the parent shell
	 * @param currentAspect the _current aspect
	 */
	public AspectEditorDialog(final Shell parentShell, final Aspect currentAspect)
	{
		super(parentShell);
		this._currentAspect = currentAspect;
		this._currentPerson = _facade.getCurrentPerson();
		//setTitle(NLMessages.getString("AspectEditorDialog_titelAspectEditor")); //$NON-NLS-1$
	}

	/**
	 * Instantiates a new aspect editor dialog.
	 * @param parentShell the parent shell
	 * @param currentPerson current person
	 * @param currentAspect the _current aspect
	 * @param message the message
	 */
	public AspectEditorDialog(final Shell parentShell, Person currentPerson, final Aspect currentAspect,
			final String message)
	{
		super(parentShell);
		this._currentAspect = currentAspect;
		if (currentPerson != null)
		{
			this._currentPerson = currentPerson;
		}
		else
		{
			this._currentPerson = _facade.getCurrentPerson();
		}
		this._message = message;
		
		// _facade.addObserver(this);

	}
	
	@Override
	public void create() {
		super.create();
		setTitle(NLMessages.getString("AspectEditorDialog_titelAspectEditor")); //$NON-NLS-1$
	};

	@Override
	public void addSelectionChangedListener(ISelectionChangedListener listener)
	{
		// TODO Auto-generated method stub

	}

	/**
	 * Adjust date.
	 * @param c1 the c1
	 * @param c2 the c2
	 * @param s the s
	 * @param date the date
	 */
	private void adjustDate(final Combo c1, final Combo c2, final YearSpinner s, final PdrDate date)
	{
		c1.select(date.getDay());
		c2.select(date.getMonth());
		s.setSelection(date.getYear());
	}

	// /**
	// * chooses color for background color of tagged element depending on
	// tagging
	// * type.
	// * @param name name of tagging element according to which color is chosen.
	// * @return RGB for background color of tagged element
	// */
	// private Color chooseColor(final String name)
	// {
	//		if (name.equals("persName")) //$NON-NLS-1$
	// {
	// return _colorPers;
	// }
	//		else if (name.equals("orgName")) //$NON-NLS-1$
	// {
	// return _colorOrg;
	// }
	//		else if (name.equals("placeName")) //$NON-NLS-1$
	// {
	// return _colorPlace;
	// }
	//		else if (name.equals("name")) //$NON-NLS-1$
	// {
	// return _colorName;
	// }
	// else
	// {
	// return _colorDate;
	// }
	//
	// }



	/**
	 * Creates the action from config data.
	 * @param menuManager the menu manager
	 * @param cd the cd
	 */
	private void createActionFromConfigData(final IMenuManager menuManager, final ConfigData cd)
	{
		boolean ignore = false;
		if (cd instanceof ConfigItem)
		{
			ignore = ((ConfigItem) cd).isIgnore();
		}

		if (!ignore && cd instanceof ConfigItem)
		{
			Action markup = new Action(cd.getLabel())
			{
				@Override
				public void run()
				{
					setCombosByQuickSelect(cd);
					setMarkup();
				}
			};
			menuManager.add(markup);
			markup.setImageDescriptor(_imageReg.getDescriptor(IconsInternal.MARKUP));
		}
		if (!ignore && cd.getChildren() != null)
		{
			IMenuManager mm2 = new MenuManager(cd.getLabel());
			menuManager.add(mm2);
			List<String> keys = new ArrayList<String>(cd.getChildren().keySet());
			Collections.sort(keys);
			for (String key : keys)
			{
				final ConfigData cd2 = cd.getChildren().get(key);
				createActionFromConfigData(mm2, cd2);
			}

		}
	}

	/**
	 * Creates the actions.
	 */
	private void createActions()
	{
		_undoAction = new Action(NLMessages.getString("Editor_action_undo"))
		{
			@Override
			public void run()
			{
				/* Undo -> Redo */
				if (_stackRedo.size() == UNDO_STACKSIZE)
				{
					_stackRedo.removeElementAt(0);
				}
				if (!_stackUndo.isEmpty())
				{
					_stackRedo.push(_stackUndo.pop());

					UndoInformation currentUndoInformation;

					if (!_stackUndo.isEmpty())
					{
						currentUndoInformation = _stackUndo.pop();

						/* Redo-Stack vorm Loeschen bewahren */
						_protectRedoStack = true;

						/* Aenderung ausfuehren */
						if (currentUndoInformation.isModifiedText())
						{
							_currentAspect.setNotification(currentUndoInformation.getReplacedText());
							_currentAspect.setRangeList(currentUndoInformation.getReplacedRanges());
							_markupEditor.refresh();

						}
						else
						{
							// System.out.println("Ungueltige Undo-Information: "
							// + currentUndoInformation.toString());
						}
						/*
						 * Bei der naechsten Aenderung, die auf den Undo-Stack
						 * kommt, kann der Redo-Stack wieder geloescht werden
						 */
						_protectRedoStack = false;
					}
				}
				
			}
		};
		_undoAction.setEnabled(false);
		_undoAction.setImageDescriptor(_imageReg.getDescriptor(IconsInternal.UNDO));
		_redoAction = new Action(NLMessages.getString("Editor_action_redo"))
		{
			@Override
			public void run()
			{
				UndoInformation currentUndoInformation;
				if (!_stackRedo.isEmpty())
				{
					currentUndoInformation = _stackRedo.pop();

					/* Redo-Stack vorm Loeschen bewahren */
					_protectRedoStack = true;

					/* Aenderung ausfuehren */
					if (currentUndoInformation.isModifiedText())
					{
						_currentAspect.setNotification(currentUndoInformation.getReplacedText());
						_currentAspect.setRangeList(currentUndoInformation.getReplacedRanges());
						_markupEditor.refresh();
					}
					else
					{
						// System.out.println("Ungueltige Undo-Information: " +
						// currentUndoInformation.toString());
					}
					/*
					 * Bei der naechsten Aenderung, die auf den Undo-Stack
					 * kommt, kann der Redo-Stack wieder geloescht werden
					 */
					_protectRedoStack = false;
				}

			}
		};
		_redoAction.setEnabled(false);
		_redoAction.setImageDescriptor(_imageReg.getDescriptor(IconsInternal.REDO));

		_customizeFavoriteMarkup = new Action(NLMessages.getString("View_customize_favorite_markup"))
		{
			@Override
			public void run()
			{

				ArrayList<Parameterization> parameters = new ArrayList<Parameterization>();
				IParameter iparam = null;

				// get the command from plugin.xml
				IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
				ICommandService cmdService = (ICommandService) window.getService(ICommandService.class);
				Command cmd = cmdService.getCommand("org.eclipse.ui.window.preferences");

				// get the parameter
				try
				{
					iparam = cmd.getParameter("preferencePageId");
				}
				catch (NotDefinedException e1)
				{
					e1.printStackTrace();
				}
				Parameterization params = new Parameterization(iparam,
						"org.bbaw.pdr.ae.view.main.preferences.FavoriteMarkupPage");
				parameters.add(params);

				// build the parameterized command
				ParameterizedCommand pc = new ParameterizedCommand(cmd,
						parameters.toArray(new Parameterization[parameters.size()]));

				// execute the command
				try
				{
					IHandlerService handlerService = (IHandlerService) window.getService(IHandlerService.class);
					handlerService.executeCommand(pc, null);
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

				_customizeFavoriteMarkup.setChecked(false);

			}
		};
		_customizeFavoriteMarkup.setChecked(false);
		_customizeFavoriteMarkup.setImageDescriptor(_imageReg.getDescriptor(IconsInternal.PREFERENCES));

		_createPersonFromString = new Action(NLMessages.getString("Editor_create_person_fromString"))
		{
			@Override
			public void run()
			{
				ArrayList<Parameterization> parameters = new ArrayList<Parameterization>();
				IParameter iparam = null;
				IParameter iparam2 = null;
				// get the command from plugin.xml
				IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
				ICommandService cmdService = (ICommandService) window.getService(ICommandService.class);
				Command cmd = cmdService.getCommand("org.bbaw.pdr.ae.view.main.commands.CreatePersonFromNameString");

				// get the parameter
				try
				{
					iparam = cmd.getParameter("org.bbaw.pdr.ae.view.main.param.personNameString");
				}
				catch (NotDefinedException e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Parameterization params = new Parameterization(iparam, _selectedText);
				parameters.add(params);

				try
				{
					iparam2 = cmd.getParameter("org.bbaw.pdr.ae.view.main.param.originalAspectID");
				}
				catch (NotDefinedException e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				params = new Parameterization(iparam2, _currentAspect.getPdrId().toString());
				parameters.add(params);
				// build the parameterized command
				ParameterizedCommand pc = new ParameterizedCommand(cmd,
						parameters.toArray(new Parameterization[parameters.size()]));

				// execute the command
				try
				{
					IHandlerService handlerService = (IHandlerService) window.getService(IHandlerService.class);
					handlerService.executeCommand(pc, null);
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
		};
		_createPersonFromString.setChecked(false);
		_createPersonFromString.setImageDescriptor(_imageReg.getDescriptor(IconsInternal.PERSON_QUICK));

		_insertSpecialCharAction = new Action("Insert special Char")
		{
			@Override
			public void run()
			{
				CharMapDialog dialog = new CharMapDialog(new Shell());
				int c = dialog.open();
				if (c != Window.CANCEL)
				{
					_markupEditor.insert(new String(new char[]
					{(char) c}));

				}
			}
		};
		_insertSpecialCharAction.setImageDescriptor(_imageReg.getDescriptor(IconsInternal.EDIT_SYMBOL));
	}

	/**
	 * Creates the admin tab item.
	 * @param mainTabFolder the main tab folder
	 */
	private void createAdminTabItem(final CTabFolder mainTabFolder)
	{
		_adminTabItem = new CTabItem(mainTabFolder, SWT.NONE);
		_adminTabItem.setText(NLMessages.getString("Editor_admin_data")); //$NON-NLS-1$
		_adminComposite = new Composite(mainTabFolder, SWT.NONE);
		_adminComposite.setLayout(new GridLayout());
		_adminTabItem.setControl(_adminComposite);
		Group pdrIdGroup = new Group(_adminComposite, SWT.SHADOW_IN);
		pdrIdGroup.setText(NLMessages.getString("Editor_administrativData")); //$NON-NLS-1$

		pdrIdGroup.setLayoutData(new GridData());
		((GridData) pdrIdGroup.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) pdrIdGroup.getLayoutData()).minimumHeight = 50;
		((GridData) pdrIdGroup.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) pdrIdGroup.getLayoutData()).minimumWidth = 500;

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
//		_pdrID.pack();

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
		creationTime.setText(NLMessages.getString("Editor_date"));
		creationTime.setLayoutData(new GridData());
		_creationTimeText = new Text(pdrIdGroup, SWT.NONE | SWT.READ_ONLY);
		_creationTimeText.setText("                                            "); //$NON-NLS-1$
		_creationTimeText.setLayoutData(new GridData());
		((GridData) _creationTimeText.getLayoutData()).horizontalSpan = 3;

		Label revisorLabel = new Label(pdrIdGroup, SWT.NONE);
		revisorLabel.setText(NLMessages.getString("Editor_lastChanged"));
		revisorLabel.setLayoutData(new GridData());
		((GridData) revisorLabel.getLayoutData()).horizontalSpan = 3;

		_revisorName = new Text(pdrIdGroup, SWT.NONE | SWT.READ_ONLY);
		_revisorName.setLayoutData(new GridData());
		((GridData) _revisorName.getLayoutData()).horizontalSpan = 3;
		_revisorName.setText("                                            "); //$NON-NLS-1$

		Label revisionTime = new Label(pdrIdGroup, SWT.NONE);
		revisionTime.setText(NLMessages.getString("Editor_date"));
		revisionTime.setLayoutData(new GridData());
		_revisionTimeText = new Text(pdrIdGroup, SWT.NONE | SWT.READ_ONLY);
		_revisionTimeText.setLayoutData(new GridData());
		((GridData) _revisionTimeText.getLayoutData()).horizontalSpan = 3;
		_revisionTimeText.setText("                                            "); //$NON-NLS-1$

		// pdrIdGroup.pack();
		// pdrIdGroup
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
				if (_currentAspect.isNew())
				{
					try
					{
						_idService.resetIdUnused(_currentAspect.getPdrId());
					}
					catch (Exception e1)
					{
						e1.printStackTrace();
					}
				}
				// _facade.setCurrentAspect(_facade.getLastAspects().get(_facade.getLastAspects().size()
				// -2 ));
				close();
			}
		});
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
		if (AEVIEWConstants.IS_SMALL_MONITOR_DIMENSION)
		{
			Point point = super.getShell().computeSize(850, 550, true);
			super.getShell().setSize(850, point.y);
			parent.setSize(800, 450);
		}
		else
		{
			// TODO fix layout problem: MarkupEditor grab horizontal space and widens tabfolder
			Point point = super.getShell().computeSize(870, 650, true);
			super.getShell().setSize(870, point.y);
			parent.setSize(860, 550);
		}
	}

	/**
	 * @param mainTabFolder creates TabItem "Klassifikation" in AspectEditor.
	 */
	@SuppressWarnings("unused")
	private void createClassificationTabItem(final CTabFolder mainTabFolder)
	{
		_taggingTabItem = new CTabItem(mainTabFolder, SWT.NONE);
		_taggingTabItem.setText(NLMessages.getString("Editor_classification")); //$NON-NLS-1$
		// taggingTabItem.setImage(_imageReg.get(IconsInternal.
		// classifications));
		// tagggingTabItemDeco = new ControlDecoration((Control)
		// taggingTabItem.getControl(), SWT.LEFT | SWT.BOTTOM);

		_classificationComposite = new Composite(mainTabFolder, SWT.NONE);
		_classificationComposite.setLayout(new GridLayout());
		((GridLayout) _classificationComposite.getLayout()).marginHeight = 0;
		((GridLayout) _classificationComposite.getLayout()).marginWidth = 0;

		_classificationComposite.setLayoutData(new GridData());
		((GridData) _classificationComposite.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _classificationComposite.getLayoutData()).verticalAlignment = SWT.FILL;
		((GridData) _classificationComposite.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) _classificationComposite.getLayoutData()).grabExcessVerticalSpace = true;
		_taggingTabItem.setControl(_classificationComposite);

		_addFurtherClassifier = new Button(_classificationComposite, SWT.PUSH);
		_addFurtherClassifier.setText(NLMessages.getString("Editor_addSemStm"));
		_addFurtherClassifier.setImage(_imageReg.get(IconsInternal.CLASSIFICATION_ADD));
		_addFurtherClassifier.setLayoutData(new GridData());
		((GridData) _addFurtherClassifier.getLayoutData()).horizontalAlignment = SWT.LEFT;
		_addFurtherClassifier.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent event)
			{
				loadClassification(1, null);
				validate();
			}
		});
//		_addFurtherClassifier.pack();
		// classificationGroup.pack();
		// classificationComposite.pack();
		_classificationComposite.layout();
	}

	
	@Override
	protected final Control createDialogArea(final Composite parent)
	{

		if (!_facade.getConfigs().containsKey(_markupProvider))
		{
			_markupProvider = "PDR";
		}
		_gridLayout = new GridLayout();
		_gridLayout.numColumns = 1;
		Composite comp = parent;
		ScrolledComposite baseScrolledComp = null;
		if (AEVIEWConstants.IS_SMALL_MONITOR_DIMENSION)
		{
			parent.setLayout(new GridLayout(1, false));
			((GridLayout)parent.getLayout()).marginHeight = 0;
			((GridLayout)parent.getLayout()).marginWidth = 0;
			baseScrolledComp = new ScrolledComposite(parent, SWT.NONE | SWT.V_SCROLL);
			baseScrolledComp.setExpandHorizontal(true);
			baseScrolledComp.setExpandVertical(true);
			baseScrolledComp.setMinHeight(400);
			baseScrolledComp.setMinWidth(750);

			baseScrolledComp.setLayoutData(new GridData());
			((GridData) baseScrolledComp.getLayoutData()).heightHint = 400;
			((GridData) baseScrolledComp.getLayoutData()).widthHint = 780;
			((GridData) baseScrolledComp.getLayoutData()).horizontalSpan = 1;

			((GridData) baseScrolledComp.getLayoutData()).horizontalAlignment = SWT.FILL;
			((GridData) baseScrolledComp.getLayoutData()).grabExcessHorizontalSpace = true;
			baseScrolledComp.layout();

			comp = new Composite(baseScrolledComp, SWT.NONE);
			comp.setLayout(new GridLayout());
			((GridLayout)comp.getLayout()).marginHeight = 0;
			((GridLayout)comp.getLayout()).marginWidth = 0;
			comp.setLayoutData(new GridData());
			((GridData) comp.getLayoutData()).horizontalSpan = 1;
			((GridData) comp.getLayoutData()).horizontalAlignment = SWT.FILL;
			((GridData) comp.getLayoutData()).grabExcessHorizontalSpace = true;
			((GridData) comp.getLayoutData()).verticalAlignment = SWT.FILL;
			((GridData) comp.getLayoutData()).grabExcessVerticalSpace = true;
			baseScrolledComp.setContent(comp);
		}
		_gridData = new GridData();
		_gridData.verticalAlignment = GridData.FILL;
		_gridData.horizontalAlignment = GridData.FILL;
		_gridData.horizontalSpan = 1;
		_gridData.grabExcessHorizontalSpace = true;
		_gridData.grabExcessVerticalSpace = true;

		_mainTabFolder = new CTabFolder(comp, SWT.NONE);
		_mainTabFolder.setLayoutData(new GridData());
		((GridData) _mainTabFolder.getLayoutData()).verticalAlignment = GridData.FILL;
		((GridData) _mainTabFolder.getLayoutData()).horizontalSpan = 4;
		((GridData) _mainTabFolder.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) _mainTabFolder.getLayoutData()).grabExcessVerticalSpace = true;
		((GridData) _mainTabFolder.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _mainTabFolder.getLayoutData()).minimumHeight = 430;
		((GridData) _mainTabFolder.getLayoutData()).heightHint = 550;

		((GridData) _mainTabFolder.getLayoutData()).minimumWidth = 770;

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
				int index = _mainTabFolder.getSelectionIndex();
				// System.out.println("tabfolder index " + index);

				_sourceViewed = _sourceView;
				_relViewed = _relView;
				_timePlaceViewed = _timePlaceView;
				switch (index)
				{
					case 0:
						_clasView = true;
						break;
					case 1:
						_sourceView = true;
						break;
					case 2:
						if (!_relView)
						{
							loadRelationDim(0, null, null);
						}
						_relView = true;
						break;
					case 3:
						if (!_timePlaceView)
						{
							loadTimeSpatialDim(0, null, null, null, null);
						}
						_timePlaceView = true;
						break;
					default:
						break;
				}
				_frontViewed = (_clasView || _sourceView || _relView || _timePlaceView);
				if (_currentAspect != null)
				{
					validate();
				}

			}
		});

		_gridLayout2 = new GridLayout();
		_gridLayout2.numColumns = 4;
		_gridLayout2.makeColumnsEqualWidth = true;

		_mainTabFolder.setLayout(_gridLayout);

		createFrontTabItem(_mainTabFolder);
		// createClassificationTabItem(mainTabFolder);
		createSourceTabItem(_mainTabFolder);
		createRelationTabItem(_mainTabFolder);
		createDimensionTabItem(_mainTabFolder);
		createAdminTabItem(_mainTabFolder);

		if (Platform.getPreferencesService().getBoolean(CommonActivator.PLUGIN_ID, "RIGHTS_GENERAL",
				AEConstants.RIGHTS_GENERAL, null))
		{
			createRightsTabItem(_mainTabFolder);
		}
		_mainTabFolder.setSelection(0);
		
		try
		{
			loadValues();
		}
		catch (SAXException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		catch (IOException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		catch (ParserConfigurationException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		validate();

		_markupEditor.setSelected(true);
		
		if (AEVIEWConstants.IS_SMALL_MONITOR_DIMENSION)
		{
			comp.layout();
	
			baseScrolledComp.setContent(comp);
			Point point = comp.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
			Point mp = _mainTabFolder.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
			if (point.x > mp.x - 20)
			{
				point.x = mp.x - 20;
			}
			baseScrolledComp.setMinSize(point);
			baseScrolledComp.layout();
		}
		return parent;
	}

	/**
	 * @param mainTabFolder creates TabItem "Dimensionen" in AspectEditor
	 */
	private void createDimensionTabItem(final CTabFolder mainTabFolder)
	{
		_dimensionTabItem = new CTabItem(mainTabFolder, SWT.NONE);
		_dimensionTabItem.setText(NLMessages.getString("Editor_timePlace")); //$NON-NLS-1$
		_dimensionComposite = new Composite(mainTabFolder, SWT.NONE);
		_dimensionComposite.setLayout(new GridLayout());
		_dimensionComposite.setLayoutData(new GridData());
		_dimensionTabItem.setControl(_dimensionComposite);
		((GridLayout) _dimensionComposite.getLayout()).numColumns = 2;

		_addTimeStmButton = new Button(_dimensionComposite, SWT.PUSH);
		_addTimeStmButton.setText(NLMessages.getString("Editor_addTimeDim"));
		_addTimeStmButton.setToolTipText(NLMessages.getString("Editor_add_timeDim_tip"));
		_addTimeStmButton.setImage(_imageReg.get(IconsInternal.TIME_ADD));
		_addTimeStmButton.setLayoutData(new GridData());
		((GridData) _addTimeStmButton.getLayoutData()).verticalAlignment = SWT.BEGINNING;
		((GridData) _addTimeStmButton.getLayoutData()).horizontalAlignment = SWT.LEFT;
		_addTimeStmButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent event)
			{
				loadTimeSpatialDim(1, null, null, null, null);
				validate();
			}
		});
//		_addTimeStmButton.pack();

		_addSpatialStmButton = new Button(_dimensionComposite, SWT.PUSH);
		_addSpatialStmButton.setText(NLMessages.getString("Editor_addSpatialDim"));
		_addSpatialStmButton.setToolTipText(NLMessages.getString("Editor_add_spatialDim_tip"));
		_addSpatialStmButton.setImage(_imageReg.get(IconsInternal.PLACE_ADD));
		_addSpatialStmButton.setLayoutData(new GridData());
		((GridData) _addSpatialStmButton.getLayoutData()).verticalAlignment = SWT.BEGINNING;
		((GridData) _addSpatialStmButton.getLayoutData()).horizontalAlignment = SWT.LEFT;
		_addSpatialStmButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent event)
			{

				loadTimeSpatialDim(5, null, null, null, null);
				validate();
			}
		});
//		_addSpatialStmButton.pack();

	}

	/**
	 * @param mainTabFolder creates TabItem "Allgemein" in AspectEditor
	 */
	private void createFrontTabItem(final CTabFolder mainTabFolder)
	{
		/* Undo/Redo */
		_stackUndo = new Stack<UndoInformation>();
		_stackUndo.ensureCapacity(UNDO_STACKSIZE);
		_stackRedo = new Stack<UndoInformation>();
		_stackRedo.ensureCapacity(UNDO_STACKSIZE);
		_protectRedoStack = false;
		
		_stackUndo.removeAllElements();
		_markupEditor = ControlExtensions.createMarkupEditor();
		_frontTabItem = new CTabItem(mainTabFolder, SWT.NONE);
		_frontTabItem.setText(NLMessages.getString("Editor_general")); //$NON-NLS-1$
		_frontComposite = new Composite(mainTabFolder, SWT.NONE);
		_frontComposite.setLayout(new GridLayout());
		// ((GridLayout) frontComposite.getLayout()).numColumns = 1;
		// // ((GridLayout) frontComposite.getLayout()).makeColumnsEqualWidth =
		// true;

		_frontComposite.setLayoutData(new GridData());
		((GridData) _frontComposite.getLayoutData()).verticalAlignment = SWT.FILL;
		((GridData) _frontComposite.getLayoutData()).grabExcessVerticalSpace = true;

		_frontTabItem.setControl(_frontComposite);

		Group classifierGroup = new Group(_frontComposite, SWT.NONE);
		classifierGroup.setLayout(new GridLayout());
		classifierGroup.setLayout(new GridLayout());
		((GridLayout) classifierGroup.getLayout()).marginHeight = 0;
		((GridLayout) classifierGroup.getLayout()).marginWidth = 0;
		classifierGroup.setLayoutData(new GridData());
		((GridData) classifierGroup.getLayoutData()).horizontalAlignment = SWT.FILL;
		// ((GridData)
		// classificationComposite.getLayoutData()).verticalAlignment =
		// SWT.FILL;
		((GridData) classifierGroup.getLayoutData()).grabExcessHorizontalSpace = true;
		classifierGroup.setText(NLMessages.getString("Editor_classification"));

		_classificationComposite = new Composite(classifierGroup, SWT.NONE);
		_classificationComposite.setLayout(new GridLayout());
		((GridLayout) _classificationComposite.getLayout()).marginHeight = 0;
		((GridLayout) _classificationComposite.getLayout()).marginWidth = 0;
		_classificationComposite.setLayoutData(new GridData());
		((GridData) _classificationComposite.getLayoutData()).horizontalAlignment = SWT.FILL;
		// ((GridData)
		// classificationComposite.getLayoutData()).verticalAlignment =
		// SWT.FILL;
		((GridData) _classificationComposite.getLayoutData()).grabExcessHorizontalSpace = true;
		// ((GridData)
		// classificationComposite.getLayoutData()).grabExcessVerticalSpace =
		// true;
		// taggingTabItem.setControl(classificationComposite);

		// classificationGroup.pack();
		// classificationComposite.pack();
		_classificationComposite.layout();

		// Tagging
		_taggingGroup = new Group(_frontComposite, SWT.NONE);
		_taggingGroup.setLayout(new GridLayout());
		_taggingGroup.setLayoutData(new GridData());
		((GridLayout) _taggingGroup.getLayout()).numColumns = 1;
		((GridLayout) _taggingGroup.getLayout()).marginHeight = 0;
		((GridLayout) _taggingGroup.getLayout()).marginWidth = 0;
		((GridData) _taggingGroup.getLayoutData()).verticalAlignment = SWT.BEGINNING;
		((GridData) _taggingGroup.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) _taggingGroup.getLayoutData()).horizontalAlignment = SWT.FILL;
		_taggingGroup.setText(NLMessages.getString("Editor_tagging")); //$NON-NLS-1$

		_tagging1Coposite = new Composite(_taggingGroup, SWT.NONE);
		_tagging1Coposite.setLayout(new GridLayout());
		_tagging1Coposite.setLayoutData(new GridData());
		((GridLayout) _tagging1Coposite.getLayout()).numColumns = 8;
		((GridLayout) _tagging1Coposite.getLayout()).makeColumnsEqualWidth = true;
		// ((GridData) tagging1Coposite.getLayoutData()).verticalAlignment =
		// SWT.FILL;
		((GridData) _tagging1Coposite.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) _tagging1Coposite.getLayoutData()).horizontalAlignment = SWT.FILL;

		Label taggingElement = new Label(_tagging1Coposite, SWT.RIGHT);
		taggingElement.setText(NLMessages.getString("Editor_markup_element") + "*");
		taggingElement.setLayoutData(new GridData());

		_comboTaggingElement = new Combo(_tagging1Coposite, SWT.DROP_DOWN | SWT.READ_ONLY | SWT.RIGHT);
		_comboTaggingElement.setBackground(WHITE_COLOR);
		_comboTaggingElement.setLayoutData(new GridData());
		((GridData) _comboTaggingElement.getLayoutData()).horizontalSpan = 3;
		((GridData) _comboTaggingElement.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _comboTaggingElement.getLayoutData()).grabExcessHorizontalSpace = true;

		_comboTaggingElementViewer = new ComboViewer(_comboTaggingElement);
		_comboTaggingElementViewer.setContentProvider(new MarkupContentProvider());
		_comboTaggingElementViewer.setLabelProvider(new MarkupLabelProvider());
		if (_facade.getConfigs().containsKey(_markupProvider))
		{
			_comboTaggingElementViewer.setInput(_facade.getConfigs().get(_markupProvider).getChildren());
		}
		ConfigData cd = (ConfigData) _comboTaggingElementViewer.getElementAt(0);
		if (cd != null)
		{
			if (cd.getValue().startsWith("aodl:"))
			{
				_eListName = cd.getValue().substring(5);
			}
			else
			{
				_eListName = cd.getValue();
			}
		}
		_comboTaggingElementViewer.setSelection(new StructuredSelection(_comboTaggingElementViewer.getElementAt(0)));
		_elementDeco = new ControlDecoration(_comboTaggingElement, SWT.RIGHT | SWT.TOP);
		_elementDeco.setDescriptionText(PDRConfigProvider.readDocu(_markupProvider, "markup", "element", _eListName,
				null, null, null));
		if (_elementDeco.getDescriptionText() != null)
		{
			_elementDeco.setImage(FieldDecorationRegistry.getDefault()
					.getFieldDecoration(FieldDecorationRegistry.DEC_INFORMATION).getImage());
		}
		else
		{
			_elementDeco.setImage(null);
		}
		_comboTaggingElementViewer.addSelectionChangedListener(new ISelectionChangedListener()
		{

			@Override
			public void selectionChanged(final SelectionChangedEvent event)
			{
				ISelection iSelection = event.getSelection();
				Object obj = ((IStructuredSelection) iSelection).getFirstElement();
				ConfigData cd = (ConfigData) obj;
				if (cd.getValue().startsWith("aodl:"))
				{
					_eListName = cd.getValue().substring(5);
				}
				else
				{
					_eListName = cd.getValue();
				}
				_comboTaggingType.removeAll();
				_tListName = ""; //$NON-NLS-1$
				_comboTaggingSubtype.removeAll();
				_comboTaggingSubtype.setEnabled(false);
				_sListName = ""; //$NON-NLS-1$
				_comboTaggingRole.removeAll();
				_comboTaggingRole.setEnabled(false);
				_rListName = ""; //$NON-NLS-1$
				_textTaggingKey.setText(""); //$NON-NLS-1$
				_textTaggingKey.setEnabled(false);
				_contentText.setText("");
				_buttonTaggingSet.setEnabled(false);
				_buttonTaggingInsertSet.setEnabled(false);
				_buttonTaggingInsertMarkup.setEnabled(false);
				if (!_eListName.equals("date")) //$NON-NLS-1$
				{
					// delete date selection after every change in tagging
					// element.
					_comboTDateDay.select(0);
					_comboTDateMonth.select(0);
					_spinnerTDateYear.setSelection(PRESELECTED_YEAR);
					_comboTDateRangeFromDay.select(0);
					_comboTDateRangeFromMonth.select(0);
					_spinnerTDateRangeFromYear.setSelection(PRESELECTED_YEAR);
					_comboTDateRangeToDay.select(0);
					_comboTDateRangeToMonth.select(0);
					_spinnerTDateRangeToYear.setSelection(PRESELECTED_YEAR);
					_taggingStackLayout.topControl = _compositeEmpty;
					_compositeTaggingPanel.layout();
					_pointOfTime = false;
					_rangeOfTime = false;
				}

				// TODO erweitern für placeName Keyfindung

				_elementDeco.setDescriptionText(PDRConfigProvider.readDocu(_markupProvider, "markup", "element",
						_eListName, null, null,
						null));
				if (_elementDeco.getDescriptionText() != null && _elementDeco.getDescriptionText().trim().length() > 0)
				{
					_elementDeco.setImage(FieldDecorationRegistry.getDefault()
							.getFieldDecoration(FieldDecorationRegistry.DEC_INFORMATION).getImage());
				}
				else
				{
					_elementDeco.setImage(null);
				}
				_typeDeco.setImage(null);
				_typeDeco.setDescriptionText(null);
				_subtypeDeco.setImage(null);
				_subtypeDeco.setDescriptionText(null);
				_roleDeco.setImage(null);
				_roleDeco.setDescriptionText(null);
				if (_facade.getConfigs().containsKey(_markupProvider))
				{
				_comboTaggingTypeViewer.setInput(_facade.getConfigs().get(_markupProvider).getChildren()
						.get("aodl:" + _eListName).getChildren());
				}
				// comboTaggingType.setItems(readConfigs(_markupProvider,
				// "markup", "type", eListName, null, null));
			}

		});

		// Label elementBlanc = new Label(tagging1Coposite, SWT.None);
		// elementBlanc.setText("");
		// elementBlanc.setLayoutData(new GridData());
		// ((GridData) elementBlanc.getLayoutData()).horizontalSpan = 1;

		// comboTaggingElement

		Label taggingType = new Label(_tagging1Coposite, SWT.RIGHT);
		taggingType.setText(NLMessages.getString("Editor_type") + "*");
		taggingType.setLayoutData(new GridData());
		((GridData) taggingType.getLayoutData()).horizontalIndent = 6;
		((GridData) taggingType.getLayoutData()).horizontalAlignment = SWT.RIGHT;

		_comboTaggingType = new Combo(_tagging1Coposite, SWT.DROP_DOWN | SWT.READ_ONLY);
		_comboTaggingType.setBackground(WHITE_COLOR);
		_comboTaggingType.setLayoutData(new GridData());
		((GridData) _comboTaggingType.getLayoutData()).horizontalAlignment = GridData.FILL;
		((GridData) _comboTaggingType.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) _comboTaggingType.getLayoutData()).horizontalSpan = 3;
		_comboTaggingTypeViewer = new ComboViewer(_comboTaggingType);
		_comboTaggingTypeViewer.setContentProvider(new MarkupContentProvider());
		_comboTaggingTypeViewer.setLabelProvider(new MarkupLabelProvider());
		_comboTaggingType.setEnabled(_mayWrite);
		// comboTaggingType.setItems(readConfigs(_markupProvider, "markup",
		// "type", eListName, null, null));
		_typeDeco = new ControlDecoration(_comboTaggingType, SWT.RIGHT | SWT.TOP);
		_comboTaggingTypeViewer.setInput(_facade.getConfigs().get(_markupProvider).getChildren()
				.get("aodl:" + _eListName).getChildren());

		_comboTaggingTypeViewer.addSelectionChangedListener(new ISelectionChangedListener()
		{

			@Override
			public void selectionChanged(final SelectionChangedEvent event)
			{
				ISelection iSelection = event.getSelection();
				Object obj = ((IStructuredSelection) iSelection).getFirstElement();
				ConfigData cd = (ConfigData) obj;
				if (cd != null)
				{
					_tListName = cd.getValue();
				}
				_comboTaggingSubtype.removeAll();
				_sListName = ""; //$NON-NLS-1$
				_comboTaggingRole.removeAll();
				_comboTaggingRole.setEnabled(false);
				_rListName = ""; //$NON-NLS-1$
				_textTaggingKey.setText(""); //$NON-NLS-1$
				_textTaggingKey.setEnabled(_mayWrite && _selectedTaggingRanges == null);
				_contentText.setText("");
				_buttonTaggingSet.setEnabled(_mayWrite && _selectedTaggingRanges == null);
				_buttonTaggingInsertSet.setEnabled(_mayWrite && _selectedTaggingRanges == null);
				_buttonTaggingInsertMarkup.setEnabled(_mayWrite && _selectedTaggingRanges == null);
				if (_eListName.equals("date")) //$NON-NLS-1$
				{
					_taggingStackLayout.topControl = _compositeTaggingDate;
					// compositeTaggingDate.setVisible(true);
				}
				else if (_eListName.equals("placeName")) //$NON-NLS-1$
				{
					_taggingStackLayout.topControl = _compositeTaggingPlace;
				}
				else
				{
					_taggingStackLayout.topControl = _compositeEmpty;
				}
				_compositeTaggingPanel.layout();
				_comboTaggingSubtype.setEnabled(_mayWrite && _selectedTaggingRanges == null);

				// if (!eListName.equals("date"))
				// {
				// ConfigData input =
				// _facade.getConfigs().get(_markupProvider).getChildren().get("aodl:"
				// + eListName).getChildren().get(tListName);
				// if (input != null)
				// comboTaggingSubtypeViewer.setInput(input.getChildren());
				// }
				ConfigData input = null;
				if (_facade.getConfigs().containsKey(_markupProvider))
				{
					input = _facade.getConfigs().get(_markupProvider).getChildren().get("aodl:" + _eListName)
						.getChildren().get(_tListName);
				}
				if (input != null)
				{
					_comboTaggingSubtypeViewer.setInput(input.getChildren());

				}
				_typeDeco.setDescriptionText(PDRConfigProvider.readDocu(_markupProvider, "markup", "type", _eListName,
						_tListName, null,
						null));
				if (_typeDeco.getDescriptionText() != null && _typeDeco.getDescriptionText().trim().length() > 0)
				{
					_typeDeco.setImage(FieldDecorationRegistry.getDefault()
							.getFieldDecoration(FieldDecorationRegistry.DEC_INFORMATION).getImage());
				}
				else
				{
					_typeDeco.setImage(null);
				}
				_subtypeDeco.setImage(null);
				_subtypeDeco.setDescriptionText(null);
				_roleDeco.setImage(null);
				_roleDeco.setDescriptionText(null);
			}
		});

		// Label blancType = new Label(tagging1Coposite, SWT.NONE);
		// blancType.setText("");
		// blancType.setLayoutData(new GridData());

		Label taggingSubtype = new Label(_tagging1Coposite, SWT.RIGHT | SWT.READ_ONLY);
		taggingSubtype.setText(NLMessages.getString("Editor_subtype"));
		taggingSubtype.setLayoutData(new GridData());

		_comboTaggingSubtype = new Combo(_tagging1Coposite, SWT.READ_ONLY);
		_comboTaggingSubtype.setBackground(WHITE_COLOR);
		_comboTaggingSubtype.setLayoutData(new GridData());
		((GridData) _comboTaggingSubtype.getLayoutData()).horizontalAlignment = GridData.FILL;
		((GridData) _comboTaggingSubtype.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) _comboTaggingSubtype.getLayoutData()).horizontalSpan = 3;
		_comboTaggingSubtype.setEnabled(false);
		_comboTaggingSubtypeViewer = new ComboViewer(_comboTaggingSubtype);
		_comboTaggingSubtypeViewer.setContentProvider(new MarkupContentProvider());
		_comboTaggingSubtypeViewer.setLabelProvider(new MarkupLabelProvider());
		_subtypeDeco = new ControlDecoration(_comboTaggingSubtype, SWT.RIGHT | SWT.TOP);

		_comboTaggingSubtypeViewer.addSelectionChangedListener(new ISelectionChangedListener()
		{

			@Override
			public void selectionChanged(final SelectionChangedEvent event)
			{
				ISelection iSelection = event.getSelection();
				Object obj = ((IStructuredSelection) iSelection).getFirstElement();
				ConfigData cd = (ConfigData) obj;
				if (cd != null)
				{
					String selection = cd.getValue();
					_comboTaggingRole.removeAll();
					_comboTaggingRole.setEnabled(_mayWrite && _selectedTaggingRanges == null);
					_rListName = ""; //$NON-NLS-1$
					_sListName = selection;
					_textTaggingKey.setText(""); //$NON-NLS-1$
					_textTaggingKey.setEnabled(_mayWrite && _selectedTaggingRanges == null);
					_comboTaggingSubtype.layout();
					ConfigData input = _facade.getConfigs().get(_markupProvider);
					if (input != null)
					{
						input = input.getChildren().get("aodl:" + _eListName);
					}
					if (input != null)
					{
						input = input.getChildren().get(_tListName);
					}
					if (input != null)
					{
						input = input.getChildren().get(_sListName);
					}
					if (input != null)
					{
						_comboTaggingRoleViewer.setInput(input.getChildren());

					}
					_subtypeDeco.setDescriptionText(PDRConfigProvider.readDocu(_markupProvider, "markup", "subtype",
							_eListName,
							_tListName, _sListName, null));
					if (_subtypeDeco.getDescriptionText() != null
							&& _subtypeDeco.getDescriptionText().trim().length() > 0)
					{
						_subtypeDeco.setImage(FieldDecorationRegistry.getDefault()
								.getFieldDecoration(FieldDecorationRegistry.DEC_INFORMATION).getImage());
					}
					else
					{
						_subtypeDeco.setImage(null);
					}
					_roleDeco.setImage(null);
					_roleDeco.setDescriptionText(null);
					_contentText.setText("");
				}
			}

		});
		// Label blancSubtype = new Label(tagging1Coposite, SWT.NONE);
		// blancSubtype.setText("");
		// blancSubtype.setLayoutData(new GridData());
		// end subtype

		Label taggingRole = new Label(_tagging1Coposite, SWT.RIGHT);
		taggingRole.setText(NLMessages.getString("Editor_role"));
		taggingRole.setLayoutData(new GridData());
		taggingRole.setLayoutData(new GridData());
		((GridData) taggingRole.getLayoutData()).horizontalIndent = 6;
		((GridData) taggingRole.getLayoutData()).horizontalAlignment = SWT.RIGHT;

		_comboTaggingRole = new Combo(_tagging1Coposite, SWT.DROP_DOWN | SWT.READ_ONLY);
		_comboTaggingRole.setBackground(WHITE_COLOR);
		_comboTaggingRole.setLayoutData(new GridData());
		((GridData) _comboTaggingRole.getLayoutData()).horizontalAlignment = GridData.FILL;
		((GridData) _comboTaggingRole.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) _comboTaggingRole.getLayoutData()).horizontalSpan = 3;
		_comboTaggingRoleViewer = new ComboViewer(_comboTaggingRole);
		_comboTaggingRoleViewer.setContentProvider(new MarkupContentProvider());
		_comboTaggingRoleViewer.setLabelProvider(new MarkupLabelProvider());
		_comboTaggingRole.setEnabled(false);
		_roleDeco = new ControlDecoration(_comboTaggingRole, SWT.RIGHT | SWT.TOP);

		_comboTaggingRoleViewer.addSelectionChangedListener(new ISelectionChangedListener()
		{

			@Override
			public void selectionChanged(final SelectionChangedEvent event)
			{
				ISelection iSelection = event.getSelection();
				Object obj = ((IStructuredSelection) iSelection).getFirstElement();
				ConfigData cd = (ConfigData) obj;
				if (cd != null)
				{
					_rListName = cd.getValue();
					_contentText.setText("");
					_roleDeco.setDescriptionText(PDRConfigProvider.readDocu(_markupProvider, "markup", "role",
							_eListName, _tListName,
							_sListName, _rListName));
					if (_roleDeco.getDescriptionText() != null && _roleDeco.getDescriptionText().trim().length() > 0)
					{
						_roleDeco.setImage(FieldDecorationRegistry.getDefault()
								.getFieldDecoration(FieldDecorationRegistry.DEC_INFORMATION).getImage());
					}
					else
					{
						_roleDeco.setImage(null);
					}
				}
				else
				{
					_roleDeco.setImage(null);
				}

			}
		});
		//
		// Label blancRole = new Label(tagging1Coposite, SWT.None);
		// blancRole.setText("");
		// blancRole.setLayoutData(new GridData());
		// end role

		Label taggingAna = new Label(_tagging1Coposite, SWT.NONE);
		taggingAna.setText(NLMessages.getString("Editor_markup_ana"));
		taggingAna.setLayoutData(new GridData());

		_textTaggingAna = new Text(_tagging1Coposite, SWT.BORDER);
		_textTaggingAna.setBackground(WHITE_COLOR);
		_textTaggingAna.setLayoutData(new GridData());
		((GridData) _textTaggingAna.getLayoutData()).horizontalAlignment = GridData.FILL;
		((GridData) _textTaggingAna.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) _textTaggingAna.getLayoutData()).horizontalSpan = 6;
		ControlDecoration decoAna = new ControlDecoration(_textTaggingAna, SWT.LEFT);
		decoAna.setDescriptionText(NLMessages.getString("Editor_proposal_cntl_aspects_persons_last"));
		decoAna.setImage(FieldDecorationRegistry.getDefault()
				.getFieldDecoration(FieldDecorationRegistry.DEC_INFORMATION).getImage());
		decoAna.setShowOnlyOnFocus(false);
		_textTaggingAna.addFocusListener(new FocusListener()
		{
			@Override
			public void focusGained(final FocusEvent e)
			{
				char[] autoActivationCharacters = new char[]
				{'.', '#'};
				KeyStroke keyStrokeP;
				KeyStroke keyStrokeA;
				KeyStroke keyStrokeL;

				try
				{
					keyStrokeA = KeyStroke.getInstance("Ctrl+e");
					keyStrokeP = KeyStroke.getInstance("Ctrl+p");
					keyStrokeL = KeyStroke.getInstance("Ctrl+l");

					ContentProposalAdapter adapter = new ContentProposalAdapter(_textTaggingAna,
							new TextContentAdapter(), new FacetContentProposalProvider(_facade.getAllPersonsFacets()),
							keyStrokeP, autoActivationCharacters);
					adapter.setLabelProvider(new AutoCompleteNameLabelProvider());
					// System.out.println("innerhalb des try");
					adapter.addContentProposalListener(new IContentProposalListener()
					{
						@Override
						public void proposalAccepted(final IContentProposal proposal)
						{
							_textTaggingAna.setText(proposal.getContent());
							if (((Facet) proposal).getKey() != null)
							{
								_textTaggingAna.setData("id", ((Facet) proposal).getKey());
							}
						}
					});

					ContentProposalAdapter adapter2 = new ContentProposalAdapter(_textTaggingAna,
							new TextContentAdapter(),
							new FacetContentProposalProvider(_facade.getLoadedAspectsFacets()), keyStrokeA,
							autoActivationCharacters);
					adapter2.setLabelProvider(new AutoCompleteNameLabelProvider());
					// System.out.println("innerhalb des try");
					adapter2.addContentProposalListener(new IContentProposalListener()
					{
						@Override
						public void proposalAccepted(final IContentProposal proposal)
						{
							_textTaggingAna.setText(proposal.getContent());
							if (((Facet) proposal).getKey() != null)
							{
								_textTaggingAna.setData("id", ((Facet) proposal).getKey());
							}
						}
					});
					if (_facade.getLastObjectsFacets() != null)
					{

						ContentProposalAdapter adapter3 = new ContentProposalAdapter(_textTaggingAna,
								new TextContentAdapter(), new FacetContentProposalProvider(_facade
										.getLastObjectsFacets()), keyStrokeL, autoActivationCharacters);
						adapter3.setLabelProvider(new AutoCompleteNameLabelProvider());
						// System.out.println("innerhalb des try");
						adapter3.addContentProposalListener(new IContentProposalListener()
						{
							@Override
							public void proposalAccepted(final IContentProposal proposal)
							{
								_textTaggingAna.setText(proposal.getContent());
								if (((Facet) proposal).getKey() != null)
								{
									_textTaggingAna.setData("id", ((Facet) proposal).getKey());
								}
							}
						});
					}
				}
				catch (org.eclipse.jface.bindings.keys.ParseException e1)
				{

					e1.printStackTrace();
				}

			}

			@Override
			public void focusLost(final FocusEvent e)
			{
				if (_textTaggingAna.getText().trim().length() == 0)
				{
					_textTaggingAna.setData("id", "");
				}
				else
				{
					_facade.addIDStringToLastObjects((String) _textTaggingAna.getData("id"));
				}
			}
		});

		_findAna = new Button(_tagging1Coposite, SWT.PUSH);
//		_findAna.setText(NLMessages.getString("Editor_select_dots"));
		_findAna.setToolTipText(NLMessages.getString("Editor_open_selObjDialog_ana_tip"));
		_findAna.setImage(_imageReg.get(IconsInternal.SEARCH));
		// findAna.setLayoutData(new GridData());
		// ((GridData) findAna.getLayoutData()).horizontalSpan = 1;
		// ((GridData) findAna.getLayoutData()).horizontalAlignment =
		// SWT.RIGHT;
		_findAna.addSelectionListener(new SelectionAdapter()
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
					PdrObject o = _facade.getPdrObject(new PdrId(_facade.getRequestedId().toString()));
					_textTaggingAna.setData("id", o.getPdrId().toString());
					_textTaggingAna.setText(o.getDisplayNameWithID());
				}
			}
		});

		// end Ana

		Label taggingKey = new Label(_tagging1Coposite, SWT.NONE);
		taggingKey.setText(NLMessages.getString("Editor_key"));
		taggingKey.setLayoutData(new GridData());

		_textTaggingKey = new Text(_tagging1Coposite, SWT.BORDER);
		_textTaggingKey.setBackground(WHITE_COLOR);
		_textTaggingKey.setLayoutData(new GridData());
		((GridData) _textTaggingKey.getLayoutData()).horizontalAlignment = GridData.FILL;
		((GridData) _textTaggingKey.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) _textTaggingKey.getLayoutData()).horizontalSpan = 6;
		ControlDecoration decoKey = new ControlDecoration(_textTaggingKey, SWT.LEFT);
		decoKey.setDescriptionText(NLMessages.getString("Editor_proposal_cntl_all_ref"));
		decoKey.setImage(FieldDecorationRegistry.getDefault()
				.getFieldDecoration(FieldDecorationRegistry.DEC_INFORMATION).getImage());
		decoKey.setShowOnlyOnFocus(false);
		_textTaggingKey.addFocusListener(new FocusListener()
		{
			@Override
			public void focusGained(final FocusEvent e)
			{
				char[] autoActivationCharacters = new char[]
				{'.', '#'};
				KeyStroke keyStroke;

				try
				{
					keyStroke = KeyStroke.getInstance("Ctrl+Space");

					ContentProposalAdapter adapter = new ContentProposalAdapter(_textTaggingKey,
							new TextContentAdapter(),
							new FacetContentProposalProvider(_facade.getAllReferenceFacets()), keyStroke,
							autoActivationCharacters);
					adapter.setLabelProvider(new AutoCompleteNameLabelProvider());
					adapter.addContentProposalListener(new IContentProposalListener()
					{
						@Override
						public void proposalAccepted(final IContentProposal proposal)
						{
							_textTaggingKey.setText(proposal.getContent());
							if (((Facet) proposal).getKey() != null)
							{
								_textTaggingKey.setData("id", ((Facet) proposal).getKey());
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
				if (_textTaggingKey.getText().trim().length() == 0)
				{
					_textTaggingKey.setData("id", "");
				}
				else
				{
					_textTaggingKey.setData("id", _textTaggingKey.getText().trim());
				}
			}
		});

		// Label bk = new Label(tagging1Coposite, SWT.NONE);
		// bk.setText("");
		// bk.setLayoutData(new GridData());

		_findKey = new Button(_tagging1Coposite, SWT.PUSH);
//		_findKey.setText(NLMessages.getString("Editor_select_dots"));
		_findKey.setToolTipText(NLMessages.getString("Editor_open_selObjDialog_key_tip"));
		_findKey.setImage(_imageReg.get(IconsInternal.SEARCH));
		_findKey.setLayoutData(new GridData());
		((GridData) _findKey.getLayoutData()).horizontalSpan = 1;
		_findKey.addSelectionListener(new SelectionAdapter()
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
					PdrObject o = _facade.getPdrObject(_facade.getRequestedId());
					_textTaggingKey.setText(o.getDisplayNameWithID());
					_textTaggingKey.setData("id", o.getPdrId().toString());
				}

			}
		});

		// end key
		Label taggingContent = new Label(_tagging1Coposite, SWT.None);
		taggingContent.setText(NLMessages.getString("Editor_content"));

		_contentText = new Text(_tagging1Coposite, SWT.BORDER);
		_contentText.setBackground(WHITE_COLOR);
		_contentText.setLayoutData(new GridData());
		((GridData) _contentText.getLayoutData()).horizontalAlignment = GridData.FILL;
		((GridData) _contentText.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) _contentText.getLayoutData()).horizontalSpan = 6;
		// new AutoCompleteField(contentText, new TextContentAdapter(), new
		// String[]{"test"});
		// createDeco(contentText,
		// "Use CNTL + SPACE to see possible values");
		ControlDecoration deco = new ControlDecoration(_contentText, SWT.LEFT);
		deco.setDescriptionText(NLMessages.getString("Editor_proposal_cntl_values"));
		deco.setImage(FieldDecorationRegistry.getDefault().getFieldDecoration(FieldDecorationRegistry.DEC_INFORMATION)
				.getImage());
		deco.setShowOnlyOnFocus(false);

		_contentText.addFocusListener(new FocusListener()
		{
			@Override
			public void focusGained(final FocusEvent e)
			{
				if (_eListName != null && _eListName.trim().length() > 0)
				{
					char[] autoActivationCharacters = new char[]
					{'.', '#'};
					KeyStroke keyStroke;

					Facet[] vals = null;
					try
					{
						vals = _mainSearcher.getComplexFacets("tagging", _eListName, _tListName, _sListName, //$NON-NLS-1$
								_rListName);
					}
					catch (Exception e1)
					{
						e1.printStackTrace();
					}
					try
					{
						keyStroke = KeyStroke.getInstance("Ctrl+Space");
						ContentProposalAdapter adapter = new ContentProposalAdapter(_contentText,
								new TextContentAdapter(), new FacetContentProposalProvider(vals), keyStroke,
								autoActivationCharacters);
						adapter.setLabelProvider(new AutoCompleteNameLabelProvider());
						adapter.addContentProposalListener(new IContentProposalListener()
						{

							@Override
							public void proposalAccepted(final IContentProposal proposal)
							{
								_contentProposal = proposal.getContent();
								_contentText.setText(_contentProposal);
								if (((Facet) proposal).getKey() != null)
								{
									PdrObject o = _facade.getPdrObject(new PdrId(((Facet) proposal).getKey()));
									if (o != null)
									{
										_textTaggingKey.setText(o.getDisplayNameWithID());
										_textTaggingKey.setData("id", o.getPdrId().toString());
									}
								}
							}

						});
					}
					catch (org.eclipse.jface.bindings.keys.ParseException e1)
					{

						e1.printStackTrace();
					}

					// adapter.setLabelProvider(new
					// AutoCompleteNameLabelProvider());

					// new AutoCompleteField(contentText, new
					// TextContentAdapter(), vals);
				}
			}

			@Override
			public void focusLost(final FocusEvent e)
			{
				_contentProposal = _contentText.getText();
			}
		});
		Label bk2 = new Label(_tagging1Coposite, SWT.NONE);
		bk2.setText("");
		bk2.setLayoutData(new GridData());
		// end content
		//

		// tagging1Coposite
		_compositeTaggingPanel = new Composite(_taggingGroup, SWT.NONE);

		_taggingStackLayout = new StackLayout();
		_compositeTaggingPanel.setLayout(_taggingStackLayout);

		// empty composite for stack
		_compositeEmpty = new Composite(_compositeTaggingPanel, SWT.NONE);

		_taggingStackLayout.topControl = _compositeEmpty;

		// Composite for tagging date
		_compositeTaggingDate = new Composite(_compositeTaggingPanel, SWT.NONE);
		_compositeTaggingDate.setLayout(new GridLayout());
		_compositeTaggingDate.setLayoutData(new GridData());
		((GridData) _compositeTaggingDate.getLayoutData()).horizontalAlignment = GridData.BEGINNING;
		// ((GridData) compositeTaggingDate.getLayoutData()).horizontalSpan
		// = 1;
		((GridLayout) _compositeTaggingDate.getLayout()).numColumns = 2;
		((GridLayout) _compositeTaggingDate.getLayout()).makeColumnsEqualWidth = false;
		// compositeTaggingDate.setVisible(false);

		_buttonTDate = new Button(_compositeTaggingDate, SWT.RADIO | SWT.LEFT);
		_buttonTDate.setText(NLMessages.getString("Editor_pointOfTime")); //$NON-NLS-1$
		_buttonTDate.setSelection(true);

		// buttonTDate

		_compositeTDate = new Composite(_compositeTaggingDate, SWT.NONE);
		_compositeTDate.setLayout(new GridLayout());
		_compositeTDate.setLayoutData(new GridData());
		((GridData) _compositeTDate.getLayoutData()).horizontalAlignment = GridData.BEGINNING;
		((GridData) _compositeTDate.getLayoutData()).horizontalSpan = 1;
		((GridLayout) _compositeTDate.getLayout()).numColumns = 5;
		((GridLayout) _compositeTDate.getLayout()).makeColumnsEqualWidth = true;

		/* Emulation einer RadioButtonGroup */
		FocusListener tagFocusListener = new FocusAdapter()
		{
			@Override
			public void focusGained(final FocusEvent e)
			{
				_pointOfTime = true;
				_rangeOfTime = false;
				_buttonTDate.setSelection(true);
				_buttonTDateRange.setSelection(false);
			}
		};

		_comboTDatePointOT = new Combo(_compositeTDate, SWT.READ_ONLY);
		_comboTDatePointOT.setBackground(WHITE_COLOR);
		_comboTDatePointOT.setLayoutData(new GridData());
		((GridData) _comboTDatePointOT.getLayoutData()).horizontalAlignment = GridData.FILL;
		((GridData) _comboTDatePointOT.getLayoutData()).grabExcessHorizontalSpace = true;

		_comboViewerTDatePointOT = new ComboViewer(_comboTDatePointOT);
		_comboViewerTDatePointOT.setContentProvider(ArrayContentProvider.getInstance());
		_comboViewerTDatePointOT.setLabelProvider(new LabelProvider()
		{

			@Override
			public String getText(final Object element)
			{
				String str = (String) element;
				return NLMessages.getString("Editor_time_" + str);
			}

		});

		_comboViewerTDatePointOT.setInput(_whens);
		_comboTDatePointOT.select(0);
		_comboTDatePointOT.addFocusListener(tagFocusListener);

		_comboTDateDay = new Combo(_compositeTDate, SWT.READ_ONLY);
		_comboTDateDay.setBackground(WHITE_COLOR);
		_comboTDateDay.setLayoutData(new GridData());
		((GridData) _comboTDateDay.getLayoutData()).horizontalAlignment = GridData.FILL;
		((GridData) _comboTDateDay.getLayoutData()).grabExcessHorizontalSpace = true;
		_comboTDateDay.setItems(AEConstants.DAYS);
		_comboTDateDay.select(0);
		_comboTDateDay.addFocusListener(tagFocusListener);
		// comboTDateDay

		_comboTDateMonth = new Combo(_compositeTDate, SWT.READ_ONLY);
		_comboTDateMonth.setBackground(WHITE_COLOR);
		_comboTDateMonth.setLayoutData(new GridData());
		((GridData) _comboTDateMonth.getLayoutData()).horizontalAlignment = GridData.FILL;
		((GridData) _comboTDateMonth.getLayoutData()).grabExcessHorizontalSpace = true;
		_comboTDateMonth.setItems(AEConstants.MONTHS);
		_comboTDateMonth.addFocusListener(tagFocusListener);
		// comboDateMonth

		_spinnerTDateYear = new YearSpinner(_compositeTDate, SWT.NONE);
		_spinnerTDateYear.setSelection(_preferedYear);
		// spinnerTDateYear.pack();
		_spinnerTDateYear.addFocusListener(tagFocusListener);
		// spinner1

		// compositeTDate

		_buttonTDateRange = new Button(_compositeTaggingDate, SWT.RADIO | SWT.LEFT);
		_buttonTDateRange.setText(NLMessages.getString("Editor_periodOfTime")); //$NON-NLS-1$
		// buttonTDateRange

		_compositeTDateRange = new Composite(_compositeTaggingDate, SWT.NONE);
		_compositeTDateRange.setLayout(new GridLayout());
		_compositeTDateRange.setLayoutData(new GridData());
		((GridLayout) _compositeTDateRange.getLayout()).numColumns = 11;

		FocusListener tagFocusListener2 = new FocusAdapter()
		{

			@Override
			public void focusGained(final FocusEvent e)
			{
				_pointOfTime = false;
				_rangeOfTime = true;
				_buttonTDate.setSelection(false);
				_buttonTDateRange.setSelection(true);
			}
		};

		_comboTDateBefore = new Combo(_compositeTDateRange, SWT.READ_ONLY);
		_comboTDateBefore.setBackground(WHITE_COLOR);
		_comboTDateBefore.setLayoutData(new GridData());
		((GridData) _comboTDateBefore.getLayoutData()).horizontalAlignment = GridData.FILL;
		((GridData) _comboTDateBefore.getLayoutData()).grabExcessHorizontalSpace = true;
		_comboViewerTDateBefore = new ComboViewer(_comboTDateBefore);
		_comboViewerTDateBefore.setContentProvider(ArrayContentProvider.getInstance());
		_comboViewerTDateBefore.setLabelProvider(new LabelProvider()
		{

			@Override
			public String getText(final Object element)
			{
				String str = (String) element;
				return NLMessages.getString("Editor_time_" + str);
			}

		});

		_comboViewerTDateBefore.setInput(_befores);
		_comboTDateBefore.select(0);
		_comboTDateBefore.addFocusListener(tagFocusListener2);

		_comboTDateRangeFromDay = new Combo(_compositeTDateRange, SWT.READ_ONLY);
		_comboTDateRangeFromDay.setBackground(WHITE_COLOR);
		_comboTDateRangeFromDay.setItems(AEConstants.DAYS);
		_comboTDateRangeFromDay.addFocusListener(tagFocusListener2);
		// comboTDateRangeFromDay

		_comboTDateRangeFromMonth = new Combo(_compositeTDateRange, SWT.READ_ONLY);
		_comboTDateRangeFromMonth.setBackground(WHITE_COLOR);
		_comboTDateRangeFromMonth.setItems(AEConstants.MONTHS);
		_comboTDateRangeFromMonth.addFocusListener(tagFocusListener2);
		// comboTDateRangeFromMonth

		_spinnerTDateRangeFromYear = new YearSpinner(_compositeTDateRange, SWT.NONE);
		_spinnerTDateRangeFromYear.setSelection(_preferedYear);
		// spinnerTDateRangeFromYear.pack();
		_spinnerTDateRangeFromYear.addFocusListener(tagFocusListener2);
		// spinnerTDateRangeFromYear

		Label labelTDateRangeTo = new Label(_compositeTDateRange, SWT.NONE);
		// labelTDateRangeTo.addFocusListener(tag2FocusListener);
		labelTDateRangeTo.setText(NLMessages.getString("Editor_to2")); //$NON-NLS-1$
		// labelTDateRangeTo

		_comboTDateAfter = new Combo(_compositeTDateRange, SWT.READ_ONLY);
		_comboTDateAfter.setBackground(WHITE_COLOR);
		_comboTDateAfter.setLayoutData(new GridData());
		((GridData) _comboTDateAfter.getLayoutData()).horizontalAlignment = GridData.FILL;
		((GridData) _comboTDateAfter.getLayoutData()).grabExcessHorizontalSpace = true;
		_comboViewerTDateAfter = new ComboViewer(_comboTDateAfter);
		_comboViewerTDateAfter.setContentProvider(ArrayContentProvider.getInstance());
		_comboViewerTDateAfter.setLabelProvider(new LabelProvider()
		{

			@Override
			public String getText(final Object element)
			{
				String str = (String) element;
				return NLMessages.getString("Editor_time_" + str);
			}

		});

		_comboViewerTDateAfter.setInput(_afters);
		_comboTDateAfter.select(0);
		_comboTDateAfter.addFocusListener(tagFocusListener2);

		_comboTDateRangeToDay = new Combo(_compositeTDateRange, SWT.READ_ONLY);
		_comboTDateRangeToDay.setBackground(WHITE_COLOR);
		_comboTDateRangeToDay.setItems(AEConstants.DAYS);
		 _comboTDateRangeToDay.addFocusListener(tagFocusListener2);
		// _comboTDateRangeToDay

		_comboTDateRangeToMonth = new Combo(_compositeTDateRange, SWT.READ_ONLY);
		_comboTDateRangeToMonth.setBackground(WHITE_COLOR);
		_comboTDateRangeToMonth.setItems(AEConstants.MONTHS);
		 _comboTDateRangeToMonth.addFocusListener(tagFocusListener2);
		// _comboTDateRangeToMonth

		_spinnerTDateRangeToYear = new YearSpinner(_compositeTDateRange, SWT.NONE);
		_spinnerTDateRangeToYear.setSelection(_preferedYear);
		// _spinnerTDateRangeToYear.pack();
		_spinnerTDateRangeToYear.addFocusListener(tagFocusListener2);
		// _spinnerTDateRangeToYear

		// compositeTDateRange
		// compositeTaggingDate

		// Composite for tagging and selecting the place keys
		_compositeTaggingPlace = new Composite(_compositeTaggingPanel, SWT.NONE);
		_compositeTaggingPlace.setLayout(new GridLayout());
		_compositeTaggingPlace.setLayoutData(new GridData());
		((GridData) _compositeTaggingPlace.getLayoutData()).horizontalAlignment = GridData.BEGINNING;
		// ((GridData)
		// compositeTaggingPlace.getLayoutData()).horizontalSpan = 1;
		((GridLayout) _compositeTaggingPlace.getLayout()).numColumns = 2;
		((GridLayout) _compositeTaggingPlace.getLayout()).makeColumnsEqualWidth = false;
		_compositeTaggingPlace.setLayout(new RowLayout());
		Label testLabel = new Label(_compositeTaggingPlace, SWT.NONE);
		testLabel.setText(""); //$NON-NLS-1$
//		testLabel.pack();
		// compositeTaggingPlace

		// Composite Tagging Buttons
		_compositeTaggingButtons = new Composite(_taggingGroup, SWT.NONE);
		_compositeTaggingButtons.setLayout(new GridLayout());
		((GridLayout) _compositeTaggingButtons.getLayout()).numColumns = 7;
		_compositeTaggingButtons.setLayoutData(new GridData());
		((GridData) _compositeTaggingButtons.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _compositeTaggingButtons.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) _compositeTaggingButtons.getLayoutData()).horizontalSpan = 2;

		_buttonTaggingDelete = new Button(_compositeTaggingButtons, SWT.PUSH);
		_buttonTaggingDelete.setLayoutData(new GridData());
		_buttonTaggingDelete.setText(NLMessages.getString("Editor_delete")); //$NON-NLS-1$
		_buttonTaggingDelete.setToolTipText(NLMessages.getString("Editor_remove_markup_tip"));
		_buttonTaggingDelete.setImage(_imageReg.get(IconsInternal.MARKUP_REMOVE));
		_buttonTaggingDelete.setEnabled(false);
		_buttonTaggingDelete.addSelectionListener(new SelectionAdapter()
		{

			@Override
			public void widgetSelected(final SelectionEvent e)
			{

				if (_selectedTaggingRanges != null)
				{
					for (TaggingRange tr : _selectedTaggingRanges)
					{
						// remove deleted tagging ranges from cache.
						if (tr.getName() != null
								&& tr.getName().equals("placeName")) //$NON-NLS-1$
						{
							for (TaggingRange trPlace : _taggedPlaceCache)
							{
								if (tr.getTextValue() != null
										&& tr.getTextValue().equals(trPlace.getTextValue()))
								{
									_taggedPlaceCache.remove(trPlace);
								}
							}
						}
						if (tr.getName() != null && tr.getName().equals("date")) //$NON-NLS-1$
						{
							for (TaggingRange trDate : _taggedDateCache)
							{
								if (tr.getWhen() != null
										&& tr.getWhen().equals(trDate.getWhen()))
								{
									_taggedDateCache.remove(trDate);
								}
								else if (tr.getFrom() != null
										&& tr.getFrom().equals(trDate.getFrom()))
								{
									_taggedDateCache.remove(trDate);
								}
								else if (tr.getTo() != null
										&& tr.getTo().equals(trDate.getTo()))
								{
									_taggedDateCache.remove(trDate);
								}
								else if (tr.getNotBefore() != null
										&& tr.getNotBefore().equals(trDate.getNotBefore()))
								{
									_taggedDateCache.remove(trDate);
								}
								else if (tr.getNotAfter() != null
 && tr.getNotAfter().equals(trDate.getNotAfter()))
								{
									_taggedDateCache.remove(trDate);
								}
							}
						}
					}
					if (_stackUndo.size() == UNDO_STACKSIZE)
					{
						_stackUndo.removeElementAt(0);
					}
					_markupEditor.saveChanges();
					_stackUndo.push(new UndoInformation(_currentAspect.getNotification(), _currentAspect.getRangeList()));

					if (!_protectRedoStack)
					{
						_stackRedo.clear();
					}
					if (_undoAction != null)
					{
						_undoAction.setEnabled(_stackUndo.size() > 0);
					}
					if (_redoAction != null)
					{
						_redoAction.setEnabled(_stackRedo.size() > 0);
					}

					if (_selectedTaggingRanges != null)
					{
						for (TaggingRange tr : _selectedTaggingRanges)
						{
							_currentAspect.getRangeList().remove(tr);
						}
					}
					_markupEditor.deleteMarkup(_selectedTaggingRanges);

					activateMarkupButtonsAndCombos(false);

				}


			}
		}); // end of buttonTaggingDelete
		// buttonTaggingSet

		_buttonTaggingSet = new Button(_compositeTaggingButtons, SWT.PUSH);
		_buttonTaggingSet.setLayoutData(new GridData());
		_buttonTaggingSet.setText(NLMessages.getString("Editor_set")); //$NON-NLS-1$
		_buttonTaggingSet.setToolTipText(NLMessages.getString("Editor_set_markup_tip"));
		_buttonTaggingSet.setImage(_imageReg.get(IconsInternal.MARKUP_ADD));
		_buttonTaggingSet.setEnabled(false);
		_buttonTaggingSet.addSelectionListener(new SelectionAdapter()
		{

			@Override
			public void widgetSelected(final SelectionEvent e)
			{
				
				setMarkup();
				activateMarkupButtonsAndCombos(true);

			}

		});
		// buttonTaggingSet

		// buttonTaggingInsertSet
		_buttonTaggingInsertSet = new Button(_compositeTaggingButtons, SWT.PUSH);
		_buttonTaggingInsertSet.setLayoutData(new GridData());
		_buttonTaggingInsertSet.setText(NLMessages.getString("Editor_insertAndSet")); //$NON-NLS-1$
		_buttonTaggingInsertSet.setToolTipText(NLMessages.getString("Editor_insert_markup_tip"));
		_buttonTaggingInsertSet.setImage(_imageReg.get(IconsInternal.MARKUP_NEW));
		_buttonTaggingInsertSet.setEnabled(false);

		_buttonTaggingInsertSet.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent e)
			{
				if (_stackUndo.size() == UNDO_STACKSIZE) 
				{
					_stackUndo.removeElementAt(0);
				}
				_markupEditor.saveChanges();
				_stackUndo.push(new UndoInformation(_currentAspect.getNotification(), _currentAspect.getRangeList()));
				
				if (!_protectRedoStack)
				{
					_stackRedo.clear();
				}
				if (_undoAction != null)
				{
					_undoAction.setEnabled(_stackUndo.size() > 0);
				}
				if (_redoAction != null)
				{
					_redoAction.setEnabled(_stackRedo.size() > 0);
				}
				
				if (_currentAspect.getRangeList() == null)
				{
					_currentAspect.setRangeList(new LinkedList<TaggingRange>());
				}
				String name = _eListName;
				String type = _tListName;
				String subtype = _sListName;
				String role = _rListName;
				String ana = (String) _textTaggingAna.getData("id");
				String key = (String) _textTaggingKey.getData("id");
				String content = _contentText.getText();
				TaggingRange tr = new TaggingRange(name, type, subtype, role, ana, key);
				tr.setTextValue(content);

				if (ana != null)
				{
					processRelation(ana);
				}

				if (type != null && type.trim().length() > 0 && content.trim().length() > 0)
				{
					if (_pointOfTime)
					{
						PdrDate when = new PdrDate(_spinnerTDateYear.getSelection(), _comboTDateMonth
								.getSelectionIndex(), _comboTDateDay.getSelectionIndex());
						ISelection sel = _comboViewerTDatePointOT.getSelection();
						Object obj = ((IStructuredSelection) sel).getFirstElement();
						String tDatePointOfTime = (String) obj;

						if (tDatePointOfTime.equals("when")) //$NON-NLS-1$
						{
							tr.setWhen(when);
						}
						else if (tDatePointOfTime.equals("notBefore")) //$NON-NLS-1$
						{
							tr.setNotBefore(when);
						}
						else
						{
							tr.setNotAfter(when);
						}
						insertTimePlace();
					}
					else if (_rangeOfTime)
					{
						PdrDate from = new PdrDate(_spinnerTDateRangeFromYear.getSelection(), _comboTDateRangeFromMonth
								.getSelectionIndex(), _comboTDateRangeFromDay.getSelectionIndex());
						PdrDate to = new PdrDate(_spinnerTDateRangeToYear.getSelection(), _comboTDateRangeToMonth
								.getSelectionIndex(), _comboTDateRangeToDay.getSelectionIndex());
						ISelection sel = _comboViewerTDateBefore.getSelection();
						Object obj = ((IStructuredSelection) sel).getFirstElement();
						String tDatePointBefore = (String) obj;
						sel = _comboViewerTDateAfter.getSelection();
						obj = ((IStructuredSelection) sel).getFirstElement();
						String tDatePointAfter = (String) obj;

						if (tDatePointBefore.equals("from")) //$NON-NLS-1$
						{
							tr.setFrom(from);
						}
						else
						{
							tr.setNotBefore(from);
						}

						if (tDatePointAfter.equals("to")) //$NON-NLS-1$
						{
							tr.setTo(to);
						}
						else
						{
							tr.setNotAfter(to);
						}
						insertTimePlace();
					}
					tr.setTextValue(content);
					_markupEditor.insertContentSetMarkup(tr);

					_currentAspect.getRangeList().add(tr);
					if (_rangeOfTime || _pointOfTime)
					{
						_taggedDateCache.add(tr.clone());
					}
					if (name.equals("placeName")) //$NON-NLS-1$
					{
						_taggedPlaceCache.add(tr.clone());
					}

					Collections.sort(_currentAspect.getRangeList());
				}
				activateMarkupButtonsAndCombos(true);
			}

		});
		// buttonTaggingInsertSet
		
		// buttonTaggingInsertSet
				_buttonTaggingInsertMarkup = new Button(_compositeTaggingButtons, SWT.PUSH);
				_buttonTaggingInsertMarkup.setLayoutData(new GridData());
		_buttonTaggingInsertMarkup.setText(NLMessages.getString("Editor_insertSet_Markup")); //$NON-NLS-1$
		_buttonTaggingInsertMarkup.setToolTipText(NLMessages.getString("Editor_insertSet_Markup_tooltip"));
				_buttonTaggingInsertMarkup.setImage(_imageReg.get(IconsInternal.MARKUP_LIGHTNING));
				_buttonTaggingInsertMarkup.setEnabled(false);

				_buttonTaggingInsertMarkup.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent e)
					{
						if (_stackUndo.size() == UNDO_STACKSIZE) 
						{
							_stackUndo.removeElementAt(0);
						}
						_markupEditor.saveChanges();
						_stackUndo.push(new UndoInformation(_currentAspect.getNotification(), _currentAspect.getRangeList()));
						
						if (!_protectRedoStack)
						{
							_stackRedo.clear();
						}
						if (_undoAction != null)
						{
							_undoAction.setEnabled(_stackUndo.size() > 0);
						}
						if (_redoAction != null)
						{
							_redoAction.setEnabled(_stackRedo.size() > 0);
						}
						
						if (_currentAspect.getRangeList() == null)
						{
							_currentAspect.setRangeList(new LinkedList<TaggingRange>());
						}
						String name = _eListName;
						String type = _tListName;
						String subtype = _sListName;
						String role = _rListName;
						String ana = (String) _textTaggingAna.getData("id");
						String key = (String) _textTaggingKey.getData("id");
						String content = null;
				if (role != null && role.trim().length() > 0)
						{
							ISelection iSelection = _comboTaggingRoleViewer.getSelection();
							Object obj = ((IStructuredSelection) iSelection).getFirstElement();
							ConfigData cd = (ConfigData) obj;
							if (cd != null)
							{
								content = cd.getLabel();
							}
						}
				else if (subtype != null && subtype.trim().length() > 0)
						{
							ISelection iSelection = _comboTaggingSubtypeViewer.getSelection();
							Object obj = ((IStructuredSelection) iSelection).getFirstElement();
							ConfigData cd = (ConfigData) obj;
							if (cd != null)
							{
								content = cd.getLabel();
							}
						}
				else if (type != null && type.trim().length() > 0)
						{
							ISelection iSelection = _comboTaggingTypeViewer.getSelection();
							Object obj = ((IStructuredSelection) iSelection).getFirstElement();
							ConfigData cd = (ConfigData) obj;
							if (cd != null)
							{
								content = cd.getLabel();
							}
						}
						TaggingRange tr = new TaggingRange(name, type, subtype, role, ana, key);

						if (ana != null)
						{
							processRelation(ana);
						}

						if (type != null && type.trim().length() > 0)
						{
							if (_pointOfTime)
							{
								PdrDate when = new PdrDate(_spinnerTDateYear.getSelection(), _comboTDateMonth
										.getSelectionIndex(), _comboTDateDay.getSelectionIndex());
								ISelection sel = _comboViewerTDatePointOT.getSelection();
								Object obj = ((IStructuredSelection) sel).getFirstElement();
								String tDatePointOfTime = (String) obj;

								if (tDatePointOfTime.equals("when")) //$NON-NLS-1$
								{
									tr.setWhen(when);
								}
								else if (tDatePointOfTime.equals("notBefore")) //$NON-NLS-1$
								{
									tr.setNotBefore(when);
								}
								else
								{
									tr.setNotAfter(when);
								}
								content = when.toString();
								insertTimePlace();
							}
							else if (_rangeOfTime)
							{
								PdrDate from = new PdrDate(_spinnerTDateRangeFromYear.getSelection(), _comboTDateRangeFromMonth
										.getSelectionIndex(), _comboTDateRangeFromDay.getSelectionIndex());
								PdrDate to = new PdrDate(_spinnerTDateRangeToYear.getSelection(), _comboTDateRangeToMonth
										.getSelectionIndex(), _comboTDateRangeToDay.getSelectionIndex());
								ISelection sel = _comboViewerTDateBefore.getSelection();
								Object obj = ((IStructuredSelection) sel).getFirstElement();
								String tDatePointBefore = (String) obj;
								sel = _comboViewerTDateAfter.getSelection();
								obj = ((IStructuredSelection) sel).getFirstElement();
								String tDatePointAfter = (String) obj;

								if (tDatePointBefore.equals("from")) //$NON-NLS-1$
								{
									tr.setFrom(from);
								}
								else
								{
									tr.setNotBefore(from);
								}

								if (tDatePointAfter.equals("to")) //$NON-NLS-1$
								{
									tr.setTo(to);
								}
								else
								{
									tr.setNotAfter(to);
								}
								content = from.toString() + " - " + to.toString();
								insertTimePlace();
							}
							if (content == null)
							{
								content = "?";
							}
							tr.setTextValue(content);
							_markupEditor.insertContentSetMarkup(tr);

							_currentAspect.getRangeList().add(tr);
							if (_rangeOfTime || _pointOfTime)
							{
								_taggedDateCache.add(tr.clone());
							}
							if (name.equals("placeName")) //$NON-NLS-1$
							{
								_taggedPlaceCache.add(tr.clone());
							}

							Collections.sort(_currentAspect.getRangeList());
						}
						activateMarkupButtonsAndCombos(true);
					}

				});
				// buttonTaggingInsertSet
				
		Label quickSelectLabel = new Label(_compositeTaggingButtons, SWT.NONE);
		quickSelectLabel.setText(NLMessages.getString("Editor_quick_select"));
		quickSelectLabel.setLayoutData(new GridData());
		quickSelectLabel.setLayoutData(new GridData());
		((GridData) quickSelectLabel.getLayoutData()).horizontalIndent = 10;
		((GridData) quickSelectLabel.getLayoutData()).horizontalAlignment = SWT.RIGHT;

		_quickSelectText = new Text(_compositeTaggingButtons, SWT.BORDER);
		_quickSelectText.setBackground(WHITE_COLOR);
		_quickSelectText.setLayoutData(new GridData());
		((GridData) _quickSelectText.getLayoutData()).horizontalAlignment = GridData.FILL;
		((GridData) _quickSelectText.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) _quickSelectText.getLayoutData()).horizontalSpan = 2;
		// ControlDecoration decoKey = new
		// ControlDecoration(textTaggingKey, SWT.LEFT);
		// decoKey.setDescriptionText("Use CNTL + Space to see all References.");
		// decoKey.setImage(FieldDecorationRegistry.getDefault().getFieldDecoration(FieldDecorationRegistry.DEC_INFORMATION).getImage());
		// decoKey.setShowOnlyOnFocus(false);
		// quickSelectText.addFocusListener(new FocusListener(){
		// public void focusGained(FocusEvent e)
		// {
		ContentProposalAdapter adapter = new ContentProposalAdapter(_quickSelectText, new TextContentAdapter(),
				new MarkupListContentProposalProvider(_facade.getConfigs().get(_markupProvider), false), null, null);
		adapter.setLabelProvider(new MarkupListLabelProvider());
		adapter.addContentProposalListener(new IContentProposalListener()
		{
			@Override
			public void proposalAccepted(final IContentProposal proposal)
			{
				_quickSelectText.setText(proposal.getContent());
				ConfigData cd = (ConfigData) proposal;
				setCombosByQuickSelect(cd);
				if (_selectedText != null && _selectedText.trim().length() > 0)
				{
					_buttonTaggingSet.setFocus();
				}
				else
				{
					_buttonTaggingInsertMarkup.setFocus();
				}
			}
		});

		_editorComposite = new Composite(_frontComposite, SWT.None);
		_editorComposite.setLayout(new GridLayout(2, false));
		((GridLayout) _editorComposite.getLayout()).marginHeight = 0;
		((GridLayout) _editorComposite.getLayout()).marginWidth = 0;

		_editorComposite.setLayoutData(new GridData());
		((GridData) _editorComposite.getLayoutData()).verticalAlignment = SWT.FILL;
		((GridData) _editorComposite.getLayoutData()).grabExcessVerticalSpace = true;
		((GridData) _editorComposite.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _editorComposite.getLayoutData()).grabExcessHorizontalSpace = true;
		Group notiGroup = new Group(_editorComposite, SWT.SHADOW_IN);
		notiGroup.setText(NLMessages.getString("Editor_notificationOfAspect")); //$NON-NLS-1$
		notiGroup.setLayoutData(new GridData());
		((GridData) notiGroup.getLayoutData()).horizontalAlignment = SWT.FILL;
		// ((GridData) notiGroup.getLayoutData()).minimumHeight = 120 ;
		((GridData) notiGroup.getLayoutData()).grabExcessVerticalSpace = true;
		((GridData) notiGroup.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) notiGroup.getLayoutData()).verticalAlignment = SWT.FILL;
		notiGroup.setLayout(new GridLayout());

		_markupEditor.setComposite(notiGroup);
		_markupEditor.setTitle(NLMessages.getString("Editor_notificationOfAspect"));

		_markupEditor.createEditor();
		_symbolButton = new Button(_editorComposite, SWT.PUSH);
		_symbolButton.setImage(_imageReg.get(IconsInternal.EDIT_SYMBOL));
		_symbolButton.setLayoutData(new GridData());
		((GridData) _symbolButton.getLayoutData()).verticalAlignment = SWT.TOP;
		_symbolButton.addSelectionListener(new SelectionListener()
		{

			@Override
			public void widgetSelected(SelectionEvent e)
			{
				_insertSpecialCharAction.run();

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e)
			{
				// TODO Auto-generated method stub

			}
		});
		_editorComposite.layout();
		_markupEditor.addKeyListener(new Listener()
		{

			@Override
			public void handleEvent(final Event event)
			{
			}

		});
		_markupEditor.addFocusListener(new Listener()
		{

			@Override
			public void handleEvent(final Event event)
			{
				// _selectedTaggingRange = _markupEditor.getSelectedMarkup();
				// activateMarkupButtonsAndCombos(_selectedTaggingRange !=
				// null);


			}

		});
		
		_markupEditor.addExtendedModifyListener(new Listener()
		{

			@Override
			public void handleEvent(final Event event)
			{
				if (_stackUndo.size() == UNDO_STACKSIZE) 
				{
					_stackUndo.removeElementAt(0);
				}
				_markupEditor.saveChanges();
				_stackUndo.push(new UndoInformation(_currentAspect.getNotification(), _currentAspect.getRangeList()));
				
				if (!_protectRedoStack)
				{
					_stackRedo.clear();
				}
				if (_undoAction != null)
				{
					_undoAction.setEnabled(_stackUndo.size() > 0);
				}
				if (_redoAction != null)
				{
					_redoAction.setEnabled(_stackRedo.size() > 0);
				}
				validate();
			}
		});


		_markupEditor.addMarkupSelectionListener(new Listener()
		{

			@Override
			public void handleEvent(final Event e)
			{
				TaggingRange[] tr = null;
				if (e.type != 32)
				{
					tr = (TaggingRange[]) e.data;
					if (tr == null || tr.length == 0 || tr[0] == null || _selectedTaggingRanges == null
							|| _selectedTaggingRanges.length == 0 || !tr[0].equals(_selectedTaggingRanges[0])
							|| tr.length != _selectedTaggingRanges.length)
					{
						_selectedTaggingRanges = tr;
						activateMarkupButtonsAndCombos(_selectedTaggingRanges != null);



						if (_selectedTaggingRanges != null && _selectedTaggingRanges.length > 0
								&& _selectedTaggingRanges[0] != null)
				{
							TaggingRange firstRange = _selectedTaggingRanges[0];
							ViewHelper.setComboViewerByString(_comboTaggingElementViewer,
									"aodl:" + firstRange.getName(), true);
					//
					// comboTaggingSubtype.removeAll();
					// comboTaggingRole.removeAll();
					_textTaggingAna.setData("id", "");
					_textTaggingAna.setText("");
					_textTaggingKey.setText(""); //$NON-NLS-1$
					_textTaggingKey.setData("id", "");
					_quickSelectText.setText("");

							_elementDeco.setDescriptionText(PDRConfigProvider.readDocu(_markupProvider, "markup",
									"element",
									firstRange.getName(), null,
							null, null));
					if (_elementDeco.getDescriptionText() != null)
					{
						_elementDeco.setImage(FieldDecorationRegistry.getDefault()
								.getFieldDecoration(FieldDecorationRegistry.DEC_INFORMATION).getImage());
					}
					else
					{
						_elementDeco.setImage(null);
					}
					_typeDeco.setImage(null);
					_typeDeco.setDescriptionText(null);
					_subtypeDeco.setImage(null);
					_subtypeDeco.setDescriptionText(null);
					_roleDeco.setImage(null);
					_roleDeco.setDescriptionText(null);
					ConfigData cd = null;
					if (_facade.getConfigs().containsKey(_markupProvider))
					{
							cd = _facade.getConfigs().get(_markupProvider).getChildren()
										.get("aodl:" + firstRange.getName());
					}
					if (cd != null)
					{
						_comboTaggingTypeViewer.setInput(cd.getChildren());
					}

							if (firstRange.getType() != null)
					{
								ViewHelper.setComboViewerByString(_comboTaggingTypeViewer, firstRange.getType(), true);
								_typeDeco.setDescriptionText(PDRConfigProvider.readDocu(_markupProvider, "markup",
										"type",
										firstRange.getName(), firstRange.getType(),
										null, null));
						if (_typeDeco.getDescriptionText() != null)
						{
							_typeDeco.setImage(FieldDecorationRegistry.getDefault()
									.getFieldDecoration(FieldDecorationRegistry.DEC_INFORMATION).getImage());
						}
						else
						{
							_typeDeco.setImage(null);
						}
					}
					
					// TODO Date einpassen!!!!!!!!!!!!!!!!!!!!!
					// load tagged date
							if (firstRange.getName().equals("date")) //$NON-NLS-1$
					{
						_taggingStackLayout.topControl = _compositeTaggingDate;
						_compositeTaggingDate.setVisible(true);
						_compositeTaggingPanel.layout();

								PdrDate dateFrom = firstRange.getFrom();
								PdrDate dateTo = firstRange.getTo();
								PdrDate dateWhen = firstRange.getWhen();
								PdrDate dateNotBefore = firstRange.getNotBefore();
								PdrDate dateNotAfter = firstRange.getNotAfter();
						if ((dateFrom != null || dateNotBefore != null) && (dateTo != null || dateNotAfter != null))
						{
							_comboTDateDay.select(0);
							_comboTDateMonth.select(0);
							_spinnerTDateYear.setSelection(_preferedYear);
							// timespan
							_buttonTDateRange.setSelection(true);
							_buttonTDate.setSelection(false);
							if (dateFrom != null)
							{

								adjustDate(_comboTDateRangeFromDay, _comboTDateRangeFromMonth,
										_spinnerTDateRangeFromYear, dateFrom);
								StructuredSelection selection = new StructuredSelection("from");
								_comboViewerTDateBefore.setSelection(selection);
							}
							else
							{
								adjustDate(_comboTDateRangeFromDay, _comboTDateRangeFromMonth,
										_spinnerTDateRangeFromYear, dateNotBefore);
								StructuredSelection selection = new StructuredSelection("notBefore");
								_comboViewerTDateBefore.setSelection(selection);
							}
							if (dateTo != null)
							{
								adjustDate(_comboTDateRangeToDay, _comboTDateRangeToMonth, _spinnerTDateRangeToYear,
										dateTo);
								StructuredSelection selection = new StructuredSelection("to");
								_comboViewerTDateAfter.setSelection(selection);
							}
							else
							{
								adjustDate(_comboTDateRangeToDay, _comboTDateRangeToMonth, _spinnerTDateRangeToYear,
										dateNotAfter);
								StructuredSelection selection = new StructuredSelection("notAfter");
								_comboViewerTDateAfter.setSelection(selection);
							}
						}
						else if (dateFrom != null && (dateTo == null || dateNotAfter == null))
						{
							_comboTDateRangeFromDay.select(0);
							_comboTDateRangeFromMonth.select(0);
							_spinnerTDateRangeFromYear.setSelection(_preferedYear);

							_comboTDateRangeToDay.select(0);
							_comboTDateRangeToMonth.select(0);
							_spinnerTDateRangeToYear.setSelection(_preferedYear);

							_buttonTDate.setSelection(true);
							_buttonTDateRange.setSelection(false);
							adjustDate(_comboTDateDay, _comboTDateMonth, _spinnerTDateYear, dateFrom);
							StructuredSelection selection = new StructuredSelection("from");
							_comboViewerTDatePointOT.setSelection(selection);
						}
						else if (dateWhen != null)
						{
							_comboTDateRangeFromDay.select(0);
							_comboTDateRangeFromMonth.select(0);
							_spinnerTDateRangeFromYear.setSelection(_preferedYear);

							_comboTDateRangeToDay.select(0);
							_comboTDateRangeToMonth.select(0);
							_spinnerTDateRangeToYear.setSelection(_preferedYear);

							_buttonTDate.setSelection(true);
							_buttonTDateRange.setSelection(false);
							adjustDate(_comboTDateDay, _comboTDateMonth, _spinnerTDateYear, dateWhen);
							StructuredSelection selection = new StructuredSelection("when");
							_comboViewerTDatePointOT.setSelection(selection);
						}
						else if (dateNotBefore != null)
						{
							_comboTDateRangeFromDay.select(0);
							_comboTDateRangeFromMonth.select(0);
							_spinnerTDateRangeFromYear.setSelection(_preferedYear);

							_comboTDateRangeToDay.select(0);
							_comboTDateRangeToMonth.select(0);
							_spinnerTDateRangeToYear.setSelection(_preferedYear);

							_buttonTDate.setSelection(true);
							_buttonTDateRange.setSelection(false);
							adjustDate(_comboTDateDay, _comboTDateMonth, _spinnerTDateYear, dateNotBefore);
							StructuredSelection selection = new StructuredSelection("notBefore");
							_comboViewerTDatePointOT.setSelection(selection);
						}
						else if (dateNotAfter != null)
						{
							_comboTDateRangeFromDay.select(0);
							_comboTDateRangeFromMonth.select(0);
							_spinnerTDateRangeFromYear.setSelection(_preferedYear);

							_comboTDateRangeToDay.select(0);
							_comboTDateRangeToMonth.select(0);
							_spinnerTDateRangeToYear.setSelection(_preferedYear);

							_buttonTDate.setSelection(true);
							_buttonTDateRange.setSelection(false);
							adjustDate(_comboTDateDay, _comboTDateMonth, _spinnerTDateYear, dateNotAfter);
							StructuredSelection selection = new StructuredSelection("notAfter");
							_comboViewerTDatePointOT.setSelection(selection);
						}


					}
							else if (firstRange.getName().equals("placeName")) //$NON-NLS-1$
					{
						_taggingStackLayout.topControl = _compositeTaggingPlace;
						_compositeTaggingPlace.setVisible(true);
						_compositeTaggingPanel.layout();

					}
					else
					{
						_compositeTaggingDate.setVisible(false);
						_compositeTaggingPlace.setVisible(false);

					}
					// if no date than load subtype and key
					if (firstRange.getType() != null && firstRange.getSubtype() != null
									&& firstRange.getSubtype().trim().length() > 0)
					{
						cd = _facade.getConfigs().get(_markupProvider).getChildren()
										.get("aodl:" + firstRange.getName());
						if (cd != null && cd.getChildren() != null
										&& cd.getChildren().get(firstRange.getType()) != null)
						{
								_comboTaggingSubtypeViewer.setInput(cd.getChildren()
											.get(firstRange.getType())
											.getChildren());
								ViewHelper.setComboViewerByString(_comboTaggingSubtypeViewer, firstRange.getSubtype(), true);

								_subtypeDeco.setDescriptionText(PDRConfigProvider.readDocu(_markupProvider, "markup",
										"subtype", firstRange.getName(), firstRange.getType(), firstRange.getSubtype(),
										null));
								if (_subtypeDeco.getDescriptionText() != null)
								{
									_subtypeDeco.setImage(FieldDecorationRegistry.getDefault()
											.getFieldDecoration(FieldDecorationRegistry.DEC_INFORMATION).getImage());
								}
								else
								{
									_subtypeDeco.setImage(null);
								}
										if (firstRange.getRole() != null && firstRange.getRole().trim().length() > 0
												&& cd.getChildren().get(firstRange.getType())
												.getChildren().get(firstRange.getSubtype()) != null)
								{

									_comboTaggingRoleViewer.setInput(cd.getChildren().get(firstRange.getType())
											.getChildren().get(firstRange.getSubtype()).getChildren());
											ViewHelper.setComboViewerByString(_comboTaggingRoleViewer, firstRange.getRole(), true);
											_roleDeco.setDescriptionText(PDRConfigProvider.readDocu(_markupProvider, "markup",
													"role",
													firstRange.getName(), firstRange.getType(),
													firstRange.getSubtype(), firstRange.getRole()));
									if (_roleDeco.getDescriptionText() != null)
									{
										_roleDeco.setImage(FieldDecorationRegistry.getDefault()
												.getFieldDecoration(FieldDecorationRegistry.DEC_INFORMATION).getImage());
									}
									else
									{
										_roleDeco.setImage(null);
									}
						}

						

						}
						else
						{
							_comboTaggingRoleViewer.setInput(null);

						}

					}
					else
					{
						_comboTaggingSubtypeViewer.setInput(null);
						_comboTaggingRoleViewer.setInput(null);
					}
							if (firstRange.getAna() != null && firstRange.getAna().trim().length() > 0)
					{
								_textTaggingAna.setData("id", firstRange.getAna());
								PdrObject o = _facade.getPdrObject(new PdrId(firstRange.getAna()));
						if (o != null)
						{
							_textTaggingAna.setText(o.getDisplayNameWithID());
									_textTaggingAna.setData("id", firstRange.getAna());
						}

					}
					else
					{
						_textTaggingAna.setText("");
						_textTaggingAna.setData("id", "");
					}

							if (firstRange.getKey() != null && firstRange.getKey().trim().length() > 0)
					{
								_textTaggingKey.setData("id", firstRange.getKey());
								PdrObject o = _facade.getPdrObject(new PdrId(firstRange.getKey()));
						if (o != null)
						{
							_textTaggingKey.setText(o.getDisplayNameWithID());
									_textTaggingKey.setData("id", firstRange.getKey());
						}
						else
						{
							_textTaggingKey.setText(firstRange.getKey());
						}

					}
					else
					{
						_textTaggingKey.setText("");
						_textTaggingKey.setData("id", "");
					}

							if (firstRange != null && firstRange.getTextValue() != null)
					{
								_contentText.setText(firstRange.getTextValue());
					}

				}
				
				}
			}

			}
		});
		_markupEditor.addTextSelectionListener(new Listener()
		{
			@Override
			public void handleEvent(final Event event)
			{
				_selectedText = (String) event.data;
				Vector<String> dates = null;
				if (_dateParser != null && _selectedText != null && _selectedText.trim().length() > 0)
				{
					dates = _dateParser.getParsedDates(_selectedText);
				}
				activateMarkupButtonsAndCombos(_markupEditor.getSelectedMarkups() != null);

				if (dates != null && dates.size() == 1)
				{
					String dateString = dates.firstElement();
					if (dateString.split("\"").length == 2)
					{
						_pointOfTime = true;
						_rangeOfTime = false;
						_buttonTDate.setSelection(true);
						_buttonTDateRange.setSelection(false);

						PdrDate d = new PdrDate(dateString.split("\"")[1]);
						// System.out.println("date d " + d.getDay());
						// System.out.println("date m " + d.getMonth());
						// System.out.println("date y " + d.getYear());
						//
						_comboTDateDay.select(d.getDay());
						_comboTDateMonth.select(d.getMonth());
						_spinnerTDateYear.setSelection(d.getYear());
						StructuredSelection selection = new StructuredSelection(dateString.split("=")[0]);
						_comboViewerTDatePointOT.setSelection(selection);
						// System.out.println("date set");
						IStructuredSelection sel = (IStructuredSelection) _comboTaggingElementViewer.getSelection();
						ConfigData cd = (ConfigData) sel.getFirstElement();
						if (!cd.getValue().equals("aodl:date"))
						{
							ViewHelper.setComboViewerByString(_comboTaggingElementViewer, "aodl:date", true);
							ViewHelper.setComboViewerByString(_comboTaggingTypeViewer, "event", true);
						}
						_taggingStackLayout.topControl = _compositeTaggingDate;
					}
					else if (dateString.split("\"").length > 3)
					{
						_pointOfTime = false;
						_rangeOfTime = true;
						_buttonTDate.setSelection(false);
						_buttonTDateRange.setSelection(true);

						PdrDate d = new PdrDate(dateString.split("\"")[1]);
						_comboTDateRangeFromDay.select(d.getDay());
						_comboTDateRangeFromMonth.select(d.getMonth());
						_spinnerTDateRangeFromYear.setSelection(d.getYear());
						StructuredSelection selection = new StructuredSelection(dateString.split("=")[0]);
						_comboViewerTDateBefore.setSelection(selection);

						d = new PdrDate(dateString.split("\"")[3]);
						// System.out.println("date d " + d.getDay());
						// System.out.println("date m " + d.getMonth());
						// System.out.println("date y " + d.getYear());
						_comboTDateRangeToDay.select(d.getDay());
						_comboTDateRangeToMonth.select(d.getMonth());
						_spinnerTDateRangeToYear.setSelection(d.getYear());
						selection = new StructuredSelection(dateString.split("\"")[2].split("=")[0].trim());
						_comboViewerTDateAfter.setSelection(selection);
						IStructuredSelection sel = (IStructuredSelection) _comboTaggingElementViewer.getSelection();
						ConfigData cd = (ConfigData) sel.getFirstElement();
						if (!cd.getValue().equals("aodl:date"))
						{
							ViewHelper.setComboViewerByString(_comboTaggingElementViewer, "aodl:date", true);
							ViewHelper.setComboViewerByString(_comboTaggingTypeViewer, "timespan", true);
						}
						_taggingStackLayout.topControl = _compositeTaggingDate;
					}
				}

			}
		});

		createActions();
		createMenus(_markupEditor.getControl());

		// styledTextAspect
		// groupEvent

		// notiGroup.pack();
		// notiGroup.addFocusListener(new FocusAdapter() {
		// public void focusLost(FocusEvent e) {
		// _currentAspect.setRangeList(rangeList);
		// }
		// });
		// frontComposite.pack();
	}
	private void activateMarkupButtonsAndCombos(boolean activeTR)
	{
		_buttonTaggingDelete.setEnabled(_mayWrite && activeTR);
		_buttonTaggingInsertSet.setEnabled(_mayWrite && !activeTR);
		_buttonTaggingSet.setEnabled(_mayWrite && !activeTR);
		_buttonTaggingInsertMarkup.setEnabled(_mayWrite && !activeTR);
		_comboTaggingElement.setEnabled(_mayWrite && !activeTR);
		_comboTaggingType.setEnabled(_mayWrite && !activeTR);

		_comboTaggingSubtype.setEnabled(_mayWrite && !activeTR);
		_comboTaggingRole.setEnabled(_mayWrite && !activeTR);

		if (_comboTaggingSubtypeViewer.getInput() != null && _comboTaggingRoleViewer.getInput() == null)
		{
			Object obj = ((IStructuredSelection) _comboTaggingSubtypeViewer.getSelection()).getFirstElement();
			ConfigData cd = (ConfigData) obj;
			if (cd != null)
			{
				String selection = cd.getValue();
				_rListName = ""; //$NON-NLS-1$
				_sListName = selection;
				ConfigData input = _facade.getConfigs().get(_markupProvider);
				if (input != null)
				{
					input = input.getChildren().get("aodl:" + _eListName);
				}
				if (input != null)
				{
					input = input.getChildren().get(_tListName);
				}
				if (input != null)
				{
					input = input.getChildren().get(_sListName);
				}
				if (input != null)
				{
					_comboTaggingRoleViewer.setInput(input.getChildren());

				}
			}
		}
		else if (_comboTaggingTypeViewer.getInput() != null && _comboTaggingSubtypeViewer.getInput() == null)
		{
			Object obj = ((IStructuredSelection) _comboTaggingTypeViewer.getSelection()).getFirstElement();
			ConfigData cd = (ConfigData) obj;
			if (cd != null)
			{
				String selection = cd.getValue();
				_sListName = ""; //$NON-NLS-1$
				_tListName = selection;
				ConfigData input = _facade.getConfigs().get(_markupProvider);
				if (input != null)
				{
					input = input.getChildren().get("aodl:" + _eListName);
				}
				if (input != null)
				{
					input = input.getChildren().get(_tListName);
				}

				if (input != null)
				{
					_comboTaggingSubtypeViewer.setInput(input.getChildren());
				}
			}
		}

		_textTaggingAna.setEnabled(_mayWrite && !activeTR);
		_textTaggingKey.setEnabled(_mayWrite && !activeTR);
		_contentText.setEnabled(_mayWrite && !activeTR);

		_comboTDateRangeFromDay.setEnabled(_mayWrite && !activeTR);
		_comboTDateRangeFromMonth.setEnabled(_mayWrite && !activeTR);
		_spinnerTDateRangeFromYear.setEnabled(_mayWrite && !activeTR);
		
		_comboTDateRangeToDay.setEnabled(_mayWrite && !activeTR);
		_comboTDateRangeToMonth.setEnabled(_mayWrite && !activeTR);
		_spinnerTDateRangeToYear.setEnabled(_mayWrite && !activeTR);
		
		_comboTDateDay.setEnabled(_mayWrite && !activeTR);
		_comboTDateMonth.setEnabled(_mayWrite && !activeTR);
		_spinnerTDateYear.setEnabled(_mayWrite && !activeTR);
		
		_comboTDatePointOT.setEnabled(_mayWrite && !activeTR);
		_comboTDateBefore.setEnabled(_mayWrite && !activeTR);
		_comboTDateAfter.setEnabled(_mayWrite && !activeTR);
		_buttonTDate.setEnabled(_mayWrite && !activeTR);
		_buttonTDateRange.setEnabled(_mayWrite && !activeTR);
		_markupEditor.setFocus();
	}
	/**
	 * Creates the menus.
	 * @param control the control
	 */
	private final void createMenus(final Control control)
	{
		MenuManager menuMgr = new MenuManager();
		Menu contextMenu = menuMgr.createContextMenu(control);
		menuMgr.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener()
		{
			@Override
			public void menuAboutToShow(final IMenuManager mgr)
			{
				fillMenu(mgr);
			}
		});
		Menu menu = menuMgr.createContextMenu(control);
		fillMenu(menuMgr);
		control.setMenu(contextMenu);
		control.setMenu(menu);
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActivePart().getSite()
				.registerContextMenu(AEPluginIDs.MENU_URI_ASPECT_EDITOR, menuMgr, AspectEditorDialog.this);
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
		// if (defaultButton) {
		// Shell shell = parent.getShell();
		// if (shell != null) {
		// shell.setDefaultButton(_saveButton);
		// }
		// }
		_saveButton.setEnabled(_currentAspect.isValid() && _mayWrite);

		setButtonLayoutData(_saveButton);
		return _saveButton;
	}

	/**
	 * @param mainTabFolder creates TabItem "Verknuepfungen" in AspectEditor
	 */
	private void createRelationTabItem(final CTabFolder mainTabFolder)
	{
		_relationTabItem = new CTabItem(mainTabFolder, SWT.NONE);
		_relationTabItem.setText(NLMessages.getString("Editor_relations")); //$NON-NLS-1$
		_relationComposite = new Composite(mainTabFolder, SWT.NONE);
		_relationComposite.setLayout(new GridLayout());
		_relationComposite.setLayoutData(new GridData());
		((GridData) _relationComposite.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _relationComposite.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridLayout) _relationComposite.getLayout()).numColumns = 2;

		_relationTabItem.setControl(_relationComposite);

		_addRelationsButton = new Button(_relationComposite, SWT.PUSH);
		_addRelationsButton.setText(NLMessages.getString("Editor_morgeLinks")); //$NON-NLS-1$
		_addRelationsButton.setImage(_imageReg.get(IconsInternal.RELATION_ADD));
		_addRelationsButton.setLayoutData(new GridData());
		((GridData) _addRelationsButton.getLayoutData()).verticalAlignment = SWT.BEGINNING;
		((GridData) _addRelationsButton.getLayoutData()).horizontalAlignment = SWT.LEFT;
		_addRelationsButton.setToolTipText(NLMessages.getString("Editor_addLinks")); //$NON-NLS-1$
		_addRelationsButton.setToolTipText(NLMessages.getString("Editor_add_relation_tooltip"));
		_addRelationsButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent event)
			{
				loadRelationDim(1, null, null);
				validate();
			}
		});
//		_addRelationsButton.pack();
		_egg = "Congratulations! You have found the Easter-Egg!"; //$NON-NLS-1$

		_addBelongsToButton = new Button(_relationComposite, SWT.PUSH);
		_addBelongsToButton.setText(NLMessages.getString("Editor_addBelongsToStm"));
		_addBelongsToButton.setToolTipText(NLMessages.getString("Editor_add_aspectOfStm_tip"));
		_addBelongsToButton.setImage(_imageReg.get(IconsInternal.BELONGING_ADD));
		_addBelongsToButton.setLayoutData(new GridData());
		((GridData) _addBelongsToButton.getLayoutData()).verticalAlignment = SWT.BEGINNING;
		((GridData) _addBelongsToButton.getLayoutData()).horizontalAlignment = SWT.LEFT;
		_addBelongsToButton.setToolTipText(NLMessages.getString("Editor_addLinks")); //$NON-NLS-1$
		_addBelongsToButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent event)
			{
				loadRelationDim(5, null, null);
				validate();
			}
		});
//		_addBelongsToButton.layout();

		_relationComposite.layout();
	} // relationComposite

	/**
	 * if general rights is activated tabitem for rights management of aspect is
	 * created.
	 * @param mainTabFolder main tabFolder
	 */
	final void createRightsTabItem(final CTabFolder mainTabFolder)
	{
		_rightsTabItem = new CTabItem(mainTabFolder, SWT.NONE);
		_rightsTabItem.setText(NLMessages.getString("Editor_rights")); //$NON-NLS-1$
		_rightsComposite = new Composite(mainTabFolder, SWT.NONE);
		_rightsComposite.setLayout(new GridLayout());
		_rightsComposite.setLayoutData(new GridData());
		_rightsTabItem.setControl(_rightsComposite);

		Group rightsGroup = new Group(_rightsComposite, SWT.SHADOW_IN);
		rightsGroup.setLayout(new GridLayout());
		rightsGroup.setLayoutData(new GridData());
		((GridLayout) rightsGroup.getLayout()).numColumns = 3;
		((GridLayout) rightsGroup.getLayout()).makeColumnsEqualWidth = false;
		((GridData) rightsGroup.getLayoutData()).horizontalAlignment = GridData.FILL;
		((GridData) rightsGroup.getLayoutData()).verticalAlignment = SWT.FILL;
		((GridData) rightsGroup.getLayoutData()).minimumHeight = 60;

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
		userRightsTitel.setText(NLMessages.getString("Editor_managementOfRights")); //$NON-NLS-1$
		userRightsTitel.setLayoutData(new GridData());
		((GridData) userRightsTitel.getLayoutData()).horizontalSpan = 3;

		Label userRLabel = new Label(_rightsTableComposite, SWT.NONE);
		userRLabel.setText(NLMessages.getString("Editor_user")); //$NON-NLS-1$
		userRLabel.setLayoutData(new GridData());

		Label userReadLabel = new Label(_rightsTableComposite, SWT.NONE);
		userReadLabel.setText(NLMessages.getString("Editor_readingRights")); //$NON-NLS-1$
		userReadLabel.setLayoutData(new GridData());

		Label userWriteLabel = new Label(_rightsTableComposite, SWT.NONE);
		userWriteLabel.setText(NLMessages.getString("Editor_writingRights")); //$NON-NLS-1$
		userWriteLabel.setLayoutData(new GridData());

		Label userOwnerLabel = new Label(_rightsTableComposite, SWT.NONE);
		userOwnerLabel.setText(NLMessages.getString("Editor_owner")); //$NON-NLS-1$
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
		userWGroupLabel.setText(NLMessages.getString("Editor_workgroup")); //$NON-NLS-1$
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
		userPGroupLabel.setText(NLMessages.getString("Editor_projectgroup")); //$NON-NLS-1$
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
		userAllLabel.setText(NLMessages.getString("Editor_all")); //$NON-NLS-1$
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

		_rightsTableComposite.layout();
	}

	/**
	 * @param mainTabFolder creates TabItem "source"
	 */
	private void createSourceTabItem(final CTabFolder mainTabFolder)
	{

		_sourceTabItem = new CTabItem(mainTabFolder, SWT.NONE);
		_sourceTabItem.setText(NLMessages.getString("Editor_sources")); //$NON-NLS-1$
		_sourceComposite = new Composite(mainTabFolder, SWT.NONE);
		_sourceComposite.setLayout(new GridLayout());
		_sourceTabItem.setControl(_sourceComposite);

		_addReferencesButton = new Button(_sourceComposite, SWT.PUSH);
		_addReferencesButton.setText(NLMessages.getString("Editor_moreSources")); //$NON-NLS-1$
		_addReferencesButton.setToolTipText(NLMessages.getString("Editor_add_valStm_tip"));
		_addReferencesButton.setImage(_imageReg.get(IconsInternal.REFERENCE_ADD));
		_addReferencesButton.setLayoutData(new GridData());
		((GridData) _addReferencesButton.getLayoutData()).verticalAlignment = SWT.LEFT;

		_addReferencesButton.setToolTipText(NLMessages.getString("Editor_addSources")); //$NON-NLS-1$
		_addReferencesButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent event)
			{
				loadValidation(1, null);
				validate();
			}
		});
//		_addReferencesButton.pack();
		_sourceComposite.layout();

	}

	/**
	 * Fill menu.
	 * @param rootMenuManager the root menu manager
	 */
	protected final void fillMenu(final IMenuManager rootMenuManager)
	{

		if (Platform.getPreferencesService().getBoolean(CommonActivator.PLUGIN_ID, "FAVORITE_MARKUP_ALL", false, null))
		{
			final HashMap<String, ConfigData> configs = _facade.getConfigs().get(_markupProvider).getChildren();
			for (String key : configs.keySet())
			{
				ConfigData cd = configs.get(key);
				if (!(cd.getValue().contains("relation") || cd.getValue().contains("semantic")))
				{
					createActionFromConfigData(rootMenuManager, cd);
				}
			}
		}
		else if (_facade.getFavoriteMarkups() != null && !_facade.getFavoriteMarkups().isEmpty())
		{
			if (_facade.getFavoriteMarkups().size() < 20)
			{
				List<String> keys = new ArrayList<String>(_facade.getFavoriteMarkups().keySet());
				Collections.sort(keys);
				for (String key : keys)
				{
					final ConfigData cd = _facade.getFavoriteMarkups().get(key);
					Action markup = new Action(cd.getLabel())
					{
						@Override
						public void run()
						{

							setCombosByQuickSelect(cd);
							setMarkup();
						}
					};
					rootMenuManager.add(markup);
					markup.setImageDescriptor(_imageReg.getDescriptor(IconsInternal.MARKUP));
				}
			}
			else
			{
				IMenuManager subtype = new MenuManager(NLMessages.getString("Editor_submenu_subtype"));
				IMenuManager role = new MenuManager(NLMessages.getString("Editor_submenu_role"));
				List<String> keys = new ArrayList<String>(_facade.getFavoriteMarkups().keySet());
				Collections.sort(keys);
				for (String key : keys)
				{
					final ConfigData cd = _facade.getFavoriteMarkups().get(key);
					Action markup = new Action(cd.getLabel())
					{
						@Override
						public void run()
						{
							setCombosByQuickSelect(cd);
							setMarkup();

						}
					};
					if (cd.getPos().equals("type"))
					{
						rootMenuManager.add(markup);
					}
					else if (cd.getPos().equals("subtype"))
					{
						subtype.add(markup);
					}
					else
					{
						role.add(markup);
					}
				}
				rootMenuManager.add(new Separator());
				if (!subtype.isEmpty())
				{
					rootMenuManager.add(subtype);
				}
				if (!role.isEmpty())
				{
					rootMenuManager.add(role);
				}

			}
		}
		rootMenuManager.add(new Separator());
		rootMenuManager.add(_customizeFavoriteMarkup);
		if (_selectedText != null && _selectedText.trim().length() > 0)
		{
			rootMenuManager.add(_createPersonFromString);
		}
		rootMenuManager.add(new Separator());
		rootMenuManager.add(_undoAction);
		rootMenuManager.add(_redoAction);
		rootMenuManager.add(_insertSpecialCharAction);
	}

	@Override
	public ISelection getSelection()
	{
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * <h4>Insert time place.</h4>
	 * <p>Appends spatial and temporal information of recently created markups as
	 * {@link TimeStm} or {@link SpatialStm} to the currently edited {@link Aspect}.</p>
	 * @see TaggingRange#getWhen()
	 */
	private void insertTimePlace()
	{
		if (!_taggedDateCache.isEmpty())
		{
			//			System.out.println("datecache not empty"); //$NON-NLS-1$
			if (_currentAspect.getTimeDim() == null)
			{
				_currentAspect.setTimeDim(new TimeDim());
			}
			else
			{
				if (_currentAspect.getTimeDim().getTimeStms() != null
						&& _currentAspect.getTimeDim().getTimeStms().isEmpty()
						&& _currentAspect.getTimeDim().getTimeStms().firstElement().getType().equals("undefined"))
				{
					_currentAspect.getTimeDim().getTimeStms().remove(0);
				}
			}
			for (int i = 0; i < _taggedDateCache.size(); i++)
			{
				TimeStm ts = new TimeStm();
				ts.setType("defined"); //$NON-NLS-1$
				ts.setTimes(new Vector<Time>(2));
				if (_taggedDateCache.get(i).getWhen() != null)
				{
					Time t = new Time();
					t.setType("when"); //$NON-NLS-1$
					t.setTimeStamp(_taggedDateCache.get(i).getWhen());
					ts.getTimes().add(t);
				}
				if (_taggedDateCache.get(i).getFrom() != null)
				{
					Time t = new Time();
					t.setType("from"); //$NON-NLS-1$
					t.setTimeStamp(_taggedDateCache.get(i).getFrom());
					ts.getTimes().add(t);
				}
				if (_taggedDateCache.get(i).getTo() != null)
				{
					Time t = new Time();
					t.setType("to"); //$NON-NLS-1$
					t.setTimeStamp(_taggedDateCache.get(i).getTo());
					ts.getTimes().add(t);
				}
				if (_taggedDateCache.get(i).getNotBefore() != null)
				{
					Time t = new Time();
					t.setType("notBefore"); //$NON-NLS-1$
					t.setTimeStamp(_taggedDateCache.get(i).getNotBefore());
					ts.getTimes().add(t);
				}
				if (_taggedDateCache.get(i).getNotAfter() != null)
				{
					Time t = new Time();
					t.setType("notAfter"); //$NON-NLS-1$
					t.setTimeStamp(_taggedDateCache.get(i).getNotAfter());
					ts.getTimes().add(t);
				}
				_currentAspect.getTimeDim().getTimeStms().add(ts);

			}
			_taggedDateCache.clear();
		}

		if (!_taggedPlaceCache.isEmpty())
		{
			if (_currentAspect.getSpatialDim() == null)
			{
				_currentAspect.setSpatialDim(new SpatialDim());
			}

			if (_currentAspect.getSpatialDim().getSpatialStms() != null
					&& _currentAspect.getSpatialDim().getSpatialStms().firstElement() != null
					&& _currentAspect.getSpatialDim().getSpatialStms().firstElement().getType().equals("undefined"))
			{
				_currentAspect.getSpatialDim().getSpatialStms().remove(0);
			}

			for (int i = 0; i < _taggedPlaceCache.size(); i++)
			{
				SpatialStm spS = new SpatialStm();
				spS.setType("defined"); //$NON-NLS-1$
				spS.setPlaces(new Vector<Place>(1));
				Place p = new Place();
				if (_taggedPlaceCache.get(i).getTextValue() != null)
				{
					p.setPlaceName(_taggedPlaceCache.get(i).getTextValue());
				}
				if (_taggedPlaceCache.get(i).getKey() != null)
				{
					p.setKey(_taggedPlaceCache.get(i).getKey());
				}
				if (_taggedPlaceCache.get(i).getType() != null)
				{
					p.setType(_taggedPlaceCache.get(i).getType());
				}
				if (_taggedPlaceCache.get(i).getSubtype() != null)
				{
					p.setSubtype(_taggedPlaceCache.get(i).getSubtype());
				}
				spS.getPlaces().add(p);

				_currentAspect.getSpatialDim().getSpatialStms().add(spS);

			}
			_taggedPlaceCache.clear();
		}
		loadTimeSpatialDim(0, null, null, null, null);
	}

	@Override
	protected final boolean isResizable()
	{
		return true;
	}

	// TODO validierung einrichten
	/**
	 * Checks if is valid input.
	 * @return true, if is valid input
	 */
	private boolean isValidInput()
	{
		// Validator v = new Validator();
		// int error = v.isValid(_currentAspect);
		//		System.out.println("Error: " + error); //$NON-NLS-1$
		// if(error == 1000)
		// {
		// return true;
		// }
		// else if (error == 1200 || error == 1210)
		// {
		// setMessage(NLMessages.getString("Editor_error1200"),
		// IMessageProvider.ERROR);
		// return false;
		// }
		// else if (error == 1300 || error == 1310 || error == 1320)
		// {
		// setMessage(NLMessages.getString("Editor_error1300"),
		// IMessageProvider.ERROR);
		// return false;
		// }
		// else
		// {
		// setMessage(NLMessages.getString("Editor_error1400"),
		// IMessageProvider.ERROR);
		// return false;
		// }
		return true;

	}

	/**
	 * Load classification.
	 * @param type the type
	 * @param cla the cla
	 */
	private void loadClassification(final int type, final Integer cla)
	{
		if (_scrollCompClass != null)
		{
			_scrollCompClass.dispose();
		}
		_scrollCompClass = new ScrolledComposite(_classificationComposite, SWT.V_SCROLL);
		_scrollCompClass.setExpandHorizontal(true);
		_scrollCompClass.setExpandVertical(true);
		_scrollCompClass.setMinHeight(1);
		_scrollCompClass.setMinWidth(1);

		_scrollCompClass.setLayout(new GridLayout());
		_scrollCompClass.setLayoutData(new GridData());
		((GridData) _scrollCompClass.getLayoutData()).heightHint = 100;
		((GridData) _scrollCompClass.getLayoutData()).widthHint = 700;
		((GridData) _scrollCompClass.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _scrollCompClass.getLayoutData()).grabExcessHorizontalSpace = true;

		Composite contentCompClass = new Composite(_scrollCompClass, SWT.NONE);
		contentCompClass.setLayout(new GridLayout());
		((GridLayout) contentCompClass.getLayout()).numColumns = 4;
		((GridLayout) contentCompClass.getLayout()).makeColumnsEqualWidth = false;
		((GridLayout) contentCompClass.getLayout()).marginHeight = 0;
		((GridLayout) contentCompClass.getLayout()).marginWidth = 0;
		_scrollCompClass.setContent(contentCompClass);

		switch (type)
		{
			case 0:
				break; // nix, normales laden
			case 1: // neue SemanticStm einfügen
				if (_currentAspect.getSemanticDim() == null)
				{
					_currentAspect.setSemanticDim(new SemanticDim());
					_currentAspect.getSemanticDim().setSemanticStms(new Vector<SemanticStm>());
					_currentAspect.getSemanticDim().getSemanticStms().add(new SemanticStm());
				}
				else
				{
					_currentAspect.getSemanticDim().getSemanticStms().add(new SemanticStm());

				}
				_currentAspect
						.getSemanticDim()
						.getSemanticStms()
						.lastElement()
						.setProvider(
								Platform.getPreferencesService()
										.getString(CommonActivator.PLUGIN_ID, "PRIMARY_SEMANTIC_PROVIDER",
												AEConstants.CLASSIFICATION_AUTHORITY, null).toUpperCase()); //$NON-NLS-1$
				break;
			case 2: // SemanticStm löschen
				_currentAspect.getSemanticDim().remove(cla);
				break;
			default:
				break;
		}
		// contentCompClass = (Composite)scrollCompClass.getContent();
		// Control[] children = contentCompClass.getChildren();
		// for (Control c : children)
		// {
		// c.dispose();
		// }

		if (_currentAspect.getSemanticDim() != null)
		{
			for (int i = 0; i < _currentAspect.getSemanticDim().getSemanticStms().size(); i++)
			{
				final SemanticStm semanticStm = _currentAspect.getSemanticDim().getSemanticStms().get(i);

				if (_facade.isPersonNameTag(semanticStm.getLabel()))
				{
					_addBelongsToButton.setEnabled(false);
					_addRelationsButton.setEnabled(false);
					while (_currentAspect.getRelationDim().getRelationStms().size() > 1)
					{
						_currentAspect.getRelationDim().getRelationStms()
								.remove(_currentAspect.getRelationDim().getRelationStms().lastElement());
					}
				}
				else
				{
					setMessage("");
					_addBelongsToButton.setEnabled(_mayWrite);
					_addRelationsButton.setEnabled(_mayWrite);
				}

				final Combo claProviderCombo = new Combo(contentCompClass, SWT.DROP_DOWN | SWT.READ_ONLY);
				claProviderCombo.setItems(PDRConfigProvider.readConfigs(_semanticProvider, "_semanticProvider", null,
						null,
						null, null));
				claProviderCombo.setEnabled(_mayWrite);
				claProviderCombo.setLayoutData(new GridData());
				((GridData) claProviderCombo.getLayoutData()).minimumWidth = 100;
				// ((GridData)
				// claProviderCombo.getLayoutData()).horizontalAlignment =
				// SWT.LEFT;
				// claProviderCombo.pack();
				claProviderCombo.setBackground(WHITE_COLOR);

				final Combo classifierCombo = new Combo(contentCompClass, SWT.DROP_DOWN | SWT.READ_ONLY);
				classifierCombo.setEnabled(_mayWrite);
				classifierCombo.setBackground(WHITE_COLOR);
				final ComboViewer comboSemanticViewer = new ComboViewer(classifierCombo);
				comboSemanticViewer.setContentProvider(new MarkupContentProvider());
				comboSemanticViewer.setLabelProvider(new MarkupLabelProvider());

				claProviderCombo.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent se)
					{
						semanticStm.setProvider(claProviderCombo.getItem(claProviderCombo.getSelectionIndex()));
						classifierCombo.removeAll();

						if (semanticStm.getProvider() != null && semanticStm.getProvider().trim().length() > 0)
						{
							if (_facade.getConfigs().get(semanticStm.getProvider().toUpperCase()) != null
									&& _facade.getConfigs().get(semanticStm.getProvider().toUpperCase()).getChildren()
											.get("aodl:semanticStm").getChildren() != null
									&& !_facade.getConfigs().get(semanticStm.getProvider().toUpperCase()).getChildren()
											.get("aodl:semanticStm").getChildren().isEmpty())
							{
								comboSemanticViewer.setInput(_facade.getConfigs().get(semanticStm.getProvider())
										.getChildren().get("aodl:semanticStm").getChildren());

							}
						}
					}
				});

				// }

				if (semanticStm.getProvider() != null && semanticStm.getProvider().trim().length() > 0)
				{
					ViewHelper.setComboByString(claProviderCombo, semanticStm.getProvider().toUpperCase());
					if (_facade.getConfigs().get(semanticStm.getProvider().toUpperCase()) != null
							&& _facade.getConfigs().get(semanticStm.getProvider().toUpperCase()).getChildren()
									.get("aodl:semanticStm").getChildren() != null
							&& !_facade.getConfigs().get(semanticStm.getProvider().toUpperCase()).getChildren()
									.get("aodl:semanticStm").getChildren().isEmpty())
					{
						comboSemanticViewer.setInput(_facade.getConfigs().get(semanticStm.getProvider().toUpperCase())
								.getChildren().get("aodl:semanticStm").getChildren());
					}

				}
				classifierCombo.setLayoutData(new GridData());
				((GridData) classifierCombo.getLayoutData()).grabExcessHorizontalSpace = true;
				((GridData) classifierCombo.getLayoutData()).horizontalAlignment = SWT.FILL;
				((GridData) classifierCombo.getLayoutData()).horizontalIndent = 6;
				final ControlDecoration decoClassC = new ControlDecoration(classifierCombo, SWT.LEFT | SWT.TOP);
				final ControlDecoration decoClassInfo = new ControlDecoration(classifierCombo, SWT.RIGHT | SWT.TOP);

				comboSemanticViewer.addSelectionChangedListener(new ISelectionChangedListener()
				{
					@Override
					public void selectionChanged(final SelectionChangedEvent event)
					{
						ISelection iSelection = event.getSelection();
						Object obj = ((IStructuredSelection) iSelection).getFirstElement();
						IAEPresentable cp = (IAEPresentable) obj;
						if (cp != null)
						{
							semanticStm.setLabel(cp.getValue());
							decoClassInfo.setDescriptionText(PDRConfigProvider.readDocu(semanticStm.getProvider(),
									"semanticStm",
									semanticStm.getLabel(), null, null, null, null));
							if (decoClassInfo.getDescriptionText() != null)
							{
								decoClassInfo.setImage(FieldDecorationRegistry.getDefault()
										.getFieldDecoration(FieldDecorationRegistry.DEC_INFORMATION).getImage());
							}
							else
							{
								decoClassInfo.setImage(null);
							}
							decoClassC.setImage(null);
							if (_facade.isPersonNameTag(semanticStm.getLabel()))
							{
								_addBelongsToButton.setEnabled(false);
								_addRelationsButton.setEnabled(false);
								setMessage(NLMessages.getString("Editor_semantic_norm_name_only_one_relation"));
								while (_currentAspect.getRelationDim().getRelationStms().size() > 1)
								{
									_currentAspect.getRelationDim().getRelationStms()
											.remove(_currentAspect.getRelationDim().getRelationStms().lastElement());
								}
								loadRelationDim(0, 0, 0);
							}
							else
							{
								setMessage("");
								_addBelongsToButton.setEnabled(true);
								_addRelationsButton.setEnabled(true);
							}
							validate();
						}

					}

				});

				if (semanticStm.getLabel() != null)
				{
					ViewHelper.setComboViewerByString(comboSemanticViewer, semanticStm.getLabel(), true);
					decoClassInfo.setDescriptionText(PDRConfigProvider.readDocu(
							semanticStm.getProvider()
							.toUpperCase(), "semanticStm",
							semanticStm.getLabel(), null, null, null, null));
					if (decoClassInfo.getDescriptionText() != null)
					{
						decoClassInfo.setImage(FieldDecorationRegistry.getDefault()
								.getFieldDecoration(FieldDecorationRegistry.DEC_INFORMATION).getImage());
					}
					else
					{
						decoClassInfo.setImage(null);
					}
					// @SuppressWarnings("unchecked")
					// HashMap<String, ConfigData> inputs = (HashMap<String,
					// ConfigData>) comboSemanticViewer.getInput();
					// if (inputs.containsKey(semanticStm.getLabel()))
					// {
					// // System.out.println("contains key s " + s);
					// for (String key : inputs.keySet())
					// {
					// if (key.equals(semanticStm.getLabel()))
					// {
					// ConfigData cd = inputs.get(key);
					//
					// // StructuredSelection sel = new
					// // StructuredSelection(cd);
					// // comboSemanticViewer.setSelection(sel, true);
					// comboSemanticViewer.reveal(cd);
					// break;
					// }
					// }
					// }
				}
				else
				{
					decoClassC.setImage(FieldDecorationRegistry.getDefault()
							.getFieldDecoration(FieldDecorationRegistry.DEC_REQUIRED).getImage());
				}

				// classifierCombo.pack();

				final Button delSemanticStm = new Button(contentCompClass, SWT.PUSH);
				delSemanticStm.setToolTipText(NLMessages.getString("Editor_remove_semStm_tip"));
				delSemanticStm.setImage(_imageReg.get(IconsInternal.CLASSIFICATION_REMOVE));
				delSemanticStm.setLayoutData(new GridData());
				((GridData) delSemanticStm.getLayoutData()).horizontalAlignment = SWT.RIGHT;
				((GridData) delSemanticStm.getLayoutData()).horizontalIndent = 6;
				delSemanticStm.setData("cla", i); //$NON-NLS-1$
				delSemanticStm.setEnabled(_currentAspect.getSemanticDim().getSemanticStms().size() > 1 && _mayWrite);
				delSemanticStm.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent event)
					{
						loadClassification(2, (Integer) delSemanticStm.getData("cla")); //$NON-NLS-1$
						validate();

					}
				});
//				delSemanticStm.pack();
				if (i == 0)
				{
					Button addFurtherClassifier = new Button(contentCompClass, SWT.PUSH);
					addFurtherClassifier.setToolTipText(NLMessages.getString("Editor_add_semStm_tip"));
					addFurtherClassifier.setImage(_imageReg.get(IconsInternal.CLASSIFICATION_ADD));
					addFurtherClassifier.setEnabled(_mayWrite);
					addFurtherClassifier.setLayoutData(new GridData());
					((GridData) addFurtherClassifier.getLayoutData()).horizontalAlignment = SWT.LEFT;
					addFurtherClassifier.addSelectionListener(new SelectionAdapter()
					{
						@Override
						public void widgetSelected(final SelectionEvent event)
						{
							loadClassification(1, null);
							validate();
						}
					});
//					addFurtherClassifier.pack();
				}
				else
				{
					Label blanc = new Label(contentCompClass, SWT.NONE);
					blanc.setText("");
				}
				// ccCompo.redraw();
				// ccCompo.layout();
				// ccCompo.pack();
			}
		}
		// classificationGroup.redraw();
		// classificationGroup.layout();
		// classificationGroup.pack();
		contentCompClass.layout();
		contentCompClass.layout();
		_scrollCompClass.setContent(contentCompClass);
		Point point = contentCompClass.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
		// System.out.println("contentComp Class height " + point.y);
		Point mp = _mainTabFolder.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
		if (point.x > mp.x - 20)
		{
			point.x = mp.x - 20;
		}
		_scrollCompClass.setMinSize(point);
		_scrollCompClass.layout();

		_classificationComposite.redraw();
		_classificationComposite.layout();
//		_classificationComposite.pack();
//		_mainTabFolder.redraw();
		_mainTabFolder.layout();
//		_mainTabFolder.pack();

	}

	/**
	 * Load relation dim.
	 * @param type the type
	 * @param relStm the rel stm
	 * @param rel the rel
	 */
	private void loadRelationDim(final int type, final Integer relStm, final Integer rel)
	{
		if (_scrollCompRel != null)
		{
			_scrollCompRel.dispose();
		}
		if (_relGroup != null)
		{
			_relGroup.dispose();
		}
		_scrollCompRel = new ScrolledComposite(_relationComposite, SWT.BORDER | SWT.V_SCROLL);
		_scrollCompRel.setExpandHorizontal(true);
		_scrollCompRel.setExpandVertical(true);
		_scrollCompRel.setMinHeight(1);
		_scrollCompRel.setMinWidth(1);
		if (_currentAspect.getPdrId().getId() % 2 == 0)
		{
			_e = true;
		}

		_scrollCompRel.setLayout(new GridLayout());
		_scrollCompRel.setLayoutData(new GridData());
		((GridData) _scrollCompRel.getLayoutData()).heightHint = 490;
		((GridData) _scrollCompRel.getLayoutData()).widthHint = 700;
		((GridData) _scrollCompRel.getLayoutData()).horizontalSpan = 2;

		((GridData) _scrollCompRel.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _scrollCompRel.getLayoutData()).grabExcessHorizontalSpace = true;
		_scrollCompRel.layout();

		Composite contentCompRel = new Composite(_scrollCompRel, SWT.NONE);
		contentCompRel.setLayout(new GridLayout());
		_scrollCompRel.setContent(contentCompRel);

		switch (type)
		{
			case 0:
				break; // nix, normales laden
			case 1: // neues relationStm einfügen
				if (_currentAspect.getRelationDim() == null)
				{
					_currentAspect.setRelationDim(new RelationDim());
					_currentAspect.getRelationDim().setRelationStms(new Vector<RelationStm>());
					_currentAspect.getRelationDim().getRelationStms().add(new RelationStm());

				}
				else
				{
					_currentAspect.getRelationDim().getRelationStms().add(new RelationStm());
				}
				break;
			case 2: // relationStm löschen
				_currentAspect.getRelationDim().remove(relStm);
				break;
			case 3: // neue relation einfügen
				_currentAspect.getRelationDim().getRelationStms().get(relStm).getRelations().add(new Relation());
				break;
			case 4: // relation löschen
				_currentAspect.getRelationDim().getRelationStms().get(relStm).getRelations().removeElementAt(rel);
				break;
			case 5: // belongsto statement einfügen
				if (_currentAspect.getRelationDim() == null)
				{
					_currentAspect.setRelationDim(new RelationDim());
					_currentAspect.getRelationDim().setRelationStms(new Vector<RelationStm>());

				}

				RelationStm rs = new RelationStm();
				rs.setSubject(_currentAspect.getPdrId());
				Relation r = new Relation();
				r.setRelation("aspect_of"); //$NON-NLS-1$
				r.setProvider("PDR"); //$NON-NLS-1$
				rs.setRelations(new Vector<Relation>(1));
				rs.getRelations().add(r);

				_currentAspect.getRelationDim().getRelationStms().add(rs);

				break;
			default:
				break;
		}
		for (int i = 0; i < _currentAspect.getRelationDim().getRelationStms().size(); i++)
		{
			int l = i + 1;

			final RelationStm relationStm = _currentAspect.getRelationDim().getRelationStms().get(i);

			_relGroup = new Group(contentCompRel, SWT.SHADOW_IN);
			_relGroup.setData("relStm", i); //$NON-NLS-1$
			_relGroup.setLayout(new GridLayout());
			((GridLayout) _relGroup.getLayout()).numColumns = 8;
			((GridLayout) _relGroup.getLayout()).makeColumnsEqualWidth = false;

			_relGroup.setLayoutData(new GridData());
			((GridData) _relGroup.getLayoutData()).horizontalAlignment = SWT.FILL;
			((GridData) _relGroup.getLayoutData()).grabExcessHorizontalSpace = true;
			_relGroup.setText(NLMessages.getString("Editor_relStm") + l);

			// check whether relationStm contains only a belongs-to statement.
			if (relationStm.getSubject() != null && relationStm.getSubject().equals(_currentAspect.getPdrId()))
			{
				final Relation relBel = relationStm.getRelations().firstElement();
				Label belongsto = new Label(_relGroup, SWT.NONE);
				belongsto.setText(NLMessages.getString("Editor_aspect_of"));
				belongsto.setLayoutData(new GridData());

				final Text belongstoObject = new Text(_relGroup, SWT.BORDER);
				belongstoObject.setData("relStm", i); //$NON-NLS-1$
				belongstoObject.setEditable(_mayWrite);
				belongstoObject.setBackground(WHITE_COLOR);
				belongstoObject.setLayoutData(new GridData());
				((GridData) belongstoObject.getLayoutData()).horizontalSpan = 3;
				((GridData) belongstoObject.getLayoutData()).horizontalAlignment = SWT.FILL;
				((GridData) belongstoObject.getLayoutData()).grabExcessHorizontalSpace = true;
				final ControlDecoration decoRelBelongsTo = new ControlDecoration(belongstoObject, SWT.LEFT | SWT.TOP);

				if (relBel.getObject() != null)
				{
					PdrObject o = _facade.getPdrObject(relBel.getObject());
					decoRelBelongsTo.setImage(null);
					if (o != null)
					{
						belongstoObject.setText(o.getDisplayNameWithID());
					}
					else
					{
						belongstoObject.setText(relBel.getObject().toString());
						decoRelBelongsTo.setImage(FieldDecorationRegistry.getDefault()
								.getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).getImage());
						decoRelBelongsTo.setDescriptionText(NLMessages.getString("Editor_missing_object_no_relation"));
					}
				}
				else
				{
					belongstoObject.setText(""); //$NON-NLS-1$
					decoRelBelongsTo.setImage(FieldDecorationRegistry.getDefault()
							.getFieldDecoration(FieldDecorationRegistry.DEC_REQUIRED).getImage());

				}
				ControlDecoration decoBelObj = new ControlDecoration(belongstoObject, SWT.LEFT | SWT.BOTTOM);
				decoBelObj.setDescriptionText(NLMessages.getString("Editor_relation_proposal_deco"));
				decoBelObj.setImage(FieldDecorationRegistry.getDefault()
						.getFieldDecoration(FieldDecorationRegistry.DEC_INFORMATION).getImage());
				decoBelObj.setShowOnlyOnFocus(false);
				belongstoObject.addFocusListener(new FocusListener()
				{
					@Override
					public void focusGained(final FocusEvent e)
					{
						char[] autoActivationCharacters = new char[]
						{'.', '#'};
						KeyStroke keyStrokeP;
						KeyStroke keyStrokeA;
						KeyStroke keyStrokeL;

						try
						{
							keyStrokeA = KeyStroke.getInstance("Ctrl+e");
							keyStrokeP = KeyStroke.getInstance("Ctrl+p");
							keyStrokeL = KeyStroke.getInstance("Ctrl+l");

							ContentProposalAdapter adapter = new ContentProposalAdapter(belongstoObject,
									new TextContentAdapter(), new FacetContentProposalProvider(_facade
											.getAllPersonsFacets()), keyStrokeP, autoActivationCharacters);
							adapter.setLabelProvider(new AutoCompleteNameLabelProvider());
							adapter.addContentProposalListener(new IContentProposalListener()
							{
								@Override
								public void proposalAccepted(final IContentProposal proposal)
								{
									belongstoObject.setText(proposal.getContent());
									if (((Facet) proposal).getKey() != null)
									{
										relBel.setObject(new PdrId(((Facet) proposal).getKey()));
										decoRelBelongsTo.setImage(null);
										validate();
									}
								}
							});

							if (_facade.getLoadedAspectsFacets() != null)
							{
								ContentProposalAdapter adapter2 = new ContentProposalAdapter(belongstoObject,
										new TextContentAdapter(), new FacetContentProposalProvider(_facade
												.getLoadedAspectsFacets()), keyStrokeA, autoActivationCharacters);
								adapter2.setLabelProvider(new AutoCompleteNameLabelProvider());
								adapter2.addContentProposalListener(new IContentProposalListener()
								{
									@Override
									public void proposalAccepted(final IContentProposal proposal)
									{
										belongstoObject.setText(proposal.getContent());
										if (((Facet) proposal).getKey() != null)
										{
											relBel.setObject(new PdrId(((Facet) proposal).getKey()));
											decoRelBelongsTo.setImage(null);
											validate();
										}
									}
								});
							}
							if (_facade.getLastObjectsFacets() != null)
							{
								ContentProposalAdapter adapter3 = new ContentProposalAdapter(belongstoObject,
										new TextContentAdapter(), new FacetContentProposalProvider(_facade
												.getLastObjectsFacets()), keyStrokeL, autoActivationCharacters);
								adapter3.setLabelProvider(new AutoCompleteNameLabelProvider());
								adapter3.addContentProposalListener(new IContentProposalListener()
								{
									@Override
									public void proposalAccepted(final IContentProposal proposal)
									{
										belongstoObject.setText(proposal.getContent());
										if (((Facet) proposal).getKey() != null)
										{
											relBel.setObject(new PdrId(((Facet) proposal).getKey()));
											decoRelBelongsTo.setImage(null);
											validate();
										}
									}
								});
							}
						}
						catch (org.eclipse.jface.bindings.keys.ParseException e1)
						{

							e1.printStackTrace();
						}

					}

					@Override
					public void focusLost(final FocusEvent e)
					{

						if (relBel.getObject() != null)
						{
							if (relBel.getObject().getType().equals("pdrAo")
									|| _facade.getPdrObject(relBel.getObject()) != null)
							{
								decoRelBelongsTo.setDescriptionText("");
								decoRelBelongsTo.setImage(null);
							}

						}
						else
						{
							relBel.setObject(null);
							decoRelBelongsTo.setImage(FieldDecorationRegistry.getDefault()
									.getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).getImage());
							decoRelBelongsTo.setDescriptionText(NLMessages
									.getString("Editor_missing_object_no_relation"));
						}
						validate();

					}
				});

				belongstoObject.addKeyListener(new KeyListener()
				{

					@Override
					public void keyPressed(final KeyEvent e)
					{
					}

					@Override
					public void keyReleased(final KeyEvent e)
					{
						if (belongstoObject.getText().length() == 23)
						{
							PdrObject o = _facade.getPdrObject(new PdrId(belongstoObject.getText()));
							if (o != null)
							{
								decoRelBelongsTo.setImage(null);
								relBel.setObject(new PdrId(belongstoObject.getText()));
								belongstoObject.setText(o.getDisplayNameWithID());
							}
						}
						else if (belongstoObject.getText().trim().length() == 0)
						{
							relBel.setObject(null);
						}

						validate();
					}
				});

				Button relateCPButton = new Button(_relGroup, SWT.PUSH);
				relateCPButton.setText(NLMessages.getString("Editor_currentPerson"));
				relateCPButton.setToolTipText(NLMessages.getString("Editor_relate_cp_aspect_of"));
				relateCPButton.setImage(_imageReg.get(IconsInternal.PERSON));
				relateCPButton.setLayoutData(new GridData());
				((GridData) relateCPButton.getLayoutData()).horizontalAlignment = SWT.RIGHT;
				((GridData) relateCPButton.getLayoutData()).grabExcessHorizontalSpace = false;
				relateCPButton.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent event)
					{
						relBel.setObject(_currentPerson.getPdrId());
						PdrObject o = _currentPerson;
						if (o != null)
						{
							belongstoObject.setText(o.getDisplayNameWithID());
						}
						else
						{
							belongstoObject.setText(relBel.getObject().toString());
						}
						if (relBel.isValid())
						{
							decoRelBelongsTo.setImage(null);
						}
						else
						{
							decoRelBelongsTo.setImage(FieldDecorationRegistry.getDefault()
									.getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).getImage());
						}
						validate();
					}
				});
				if (_currentPerson != null)
				{
					relateCPButton.setEnabled(_mayWrite);
				}
				else
				{
					relateCPButton.setEnabled(false);

				}

				Button belongstoObjectButton = new Button(_relGroup, SWT.PUSH);
				belongstoObjectButton.setText(NLMessages.getString("Editor_select_dots"));
				belongstoObjectButton.setToolTipText(NLMessages.getString("Editor_open_selObjDialog_aspectOf_tip"));
				belongstoObjectButton.setImage(_imageReg.get(IconsInternal.SEARCH));
				belongstoObjectButton.setEnabled(_mayWrite);
				belongstoObjectButton.setLayoutData(new GridData());
				((GridData) belongstoObjectButton.getLayoutData()).horizontalAlignment = SWT.RIGHT;
				((GridData) belongstoObjectButton.getLayoutData()).grabExcessHorizontalSpace = false;
				belongstoObjectButton.setToolTipText(NLMessages.getString("Editor_linkPersonOrAspectToolTip")); //$NON-NLS-1$
				belongstoObjectButton.addSelectionListener(new SelectionAdapter()
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
							relBel.setObject(_facade.getRequestedId());

							PdrObject o = _facade.getPdrObject(relBel.getObject());
							if (o != null)
							{
								belongstoObject.setText(o.getDisplayNameWithID());
							}
							else
							{
								belongstoObject.setText(relBel.getObject().toString());
							}
							if (relBel.isValid())
							{
								decoRelBelongsTo.setImage(null);
							}
						}
						else
						{
							if (!relBel.isValid())
							{
								decoRelBelongsTo.setImage(FieldDecorationRegistry.getDefault()
									.getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).getImage());
							}
						}
						validate();
					}
				});
//				belongstoObjectButton.pack();

				final Button delRelationStm = new Button(_relGroup, SWT.PUSH);
				delRelationStm.setImage(_imageReg.get(IconsInternal.BELONGING_REMOVE));
				delRelationStm.setData("relStm", i); //$NON-NLS-1$
				delRelationStm.setEnabled(_currentAspect.getRelationDim().getRelationStms().size() > 1 && _mayWrite);

				if (_e && (Integer) delRelationStm.getData("relStm") == 3) //$NON-NLS-1$
				{
					delRelationStm.setToolTipText(_egg);

				}
				else
				{
					delRelationStm.setToolTipText("Delete"); //$NON-NLS-1$
				}
				delRelationStm.setLayoutData(new GridData());
				delRelationStm.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent event)
					{
						loadRelationDim(2, (Integer) delRelationStm.getData("relStm"), null); //$NON-NLS-1$
						validate();

					}
				});
				delRelationStm.setLayoutData(new GridData());

			}
			else
			{

				Label relationSubjectLabel = new Label(_relGroup, SWT.NONE);
				relationSubjectLabel.setText(NLMessages.getString("Editor_subject"));
				relationSubjectLabel.setLayoutData(new GridData());

				final Text relationSubject = new Text(_relGroup, SWT.BORDER);
				relationSubject.setEditable(_mayWrite);
				relationSubject.setBackground(WHITE_COLOR);
				relationSubject.setLayoutData(new GridData());
				((GridData) relationSubject.getLayoutData()).horizontalSpan = 5;
				((GridData) relationSubject.getLayoutData()).horizontalAlignment = SWT.FILL;
				((GridData) relationSubject.getLayoutData()).grabExcessHorizontalSpace = true;
				final ControlDecoration decoRelSubject = new ControlDecoration(relationSubject, SWT.LEFT | SWT.TOP);

				if (relationStm.getSubject() != null) //$NON-NLS-1$
				{
					PdrObject o = _facade.getPdrObject(relationStm.getSubject());
					decoRelSubject.setImage(null);
					if (o != null)
					{
						relationSubject.setText(o.getDisplayNameWithID());
					}
					else
					{
						relationSubject.setText(relationStm.getSubject().toString());
						decoRelSubject.setImage(FieldDecorationRegistry.getDefault()
								.getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).getImage());
						decoRelSubject.setDescriptionText(NLMessages.getString("Editor_missing_object_no_relation"));
					}
				}
				else
				{
					relationSubject.setText(""); //$NON-NLS-1$
					decoRelSubject.setImage(FieldDecorationRegistry.getDefault()
							.getFieldDecoration(FieldDecorationRegistry.DEC_REQUIRED).getImage());

				}
				ControlDecoration decorelationSubject = new ControlDecoration(relationSubject, SWT.LEFT | SWT.BOTTOM);
				decorelationSubject.setDescriptionText(NLMessages.getString("Editor_relation_proposal_deco"));
				decorelationSubject.setImage(FieldDecorationRegistry.getDefault()
						.getFieldDecoration(FieldDecorationRegistry.DEC_INFORMATION).getImage());
				decorelationSubject.setShowOnlyOnFocus(false);
				relationSubject.addFocusListener(new FocusListener()
				{
					@Override
					public void focusGained(final FocusEvent e)
					{
						char[] autoActivationCharacters = new char[]
						{'.', '#'};
						KeyStroke keyStrokeP;
						KeyStroke keyStrokeA;
						KeyStroke keyStrokeL;

						try
						{
							keyStrokeA = KeyStroke.getInstance("Ctrl+e");
							keyStrokeP = KeyStroke.getInstance("Ctrl+p");
							keyStrokeL = KeyStroke.getInstance("Ctrl+l");

							ContentProposalAdapter adapter = new ContentProposalAdapter(relationSubject,
									new TextContentAdapter(), new FacetContentProposalProvider(_facade
											.getAllPersonsFacets()), keyStrokeP, autoActivationCharacters);
							adapter.setLabelProvider(new AutoCompleteNameLabelProvider());
							adapter.addContentProposalListener(new IContentProposalListener()
							{
								@Override
								public void proposalAccepted(final IContentProposal proposal)
								{
									relationSubject.setText(proposal.getContent());
									if (((Facet) proposal).getKey() != null)
									{
										relationStm.setSubject(new PdrId(((Facet) proposal).getKey()));
										decoRelSubject.setImage(null);
										validate();
									}
								}
							});

							ContentProposalAdapter adapter2 = new ContentProposalAdapter(relationSubject,
									new TextContentAdapter(), new FacetContentProposalProvider(_facade
											.getLoadedAspectsFacets()), keyStrokeA, autoActivationCharacters);
							adapter2.setLabelProvider(new AutoCompleteNameLabelProvider());
							adapter2.addContentProposalListener(new IContentProposalListener()
							{
								@Override
								public void proposalAccepted(final IContentProposal proposal)
								{
									relationSubject.setText(proposal.getContent());
									if (((Facet) proposal).getKey() != null)
									{
										relationStm.setSubject(new PdrId(((Facet) proposal).getKey()));
										decoRelSubject.setImage(null);
										validate();
									}
								}
							});
							if (_facade.getLastObjectsFacets() != null)
							{

								ContentProposalAdapter adapter3 = new ContentProposalAdapter(relationSubject,
										new TextContentAdapter(), new FacetContentProposalProvider(_facade
												.getLastObjectsFacets()), keyStrokeL, autoActivationCharacters);
								adapter3.setLabelProvider(new AutoCompleteNameLabelProvider());
								adapter3.addContentProposalListener(new IContentProposalListener()
								{
									@Override
									public void proposalAccepted(final IContentProposal proposal)
									{
										relationSubject.setText(proposal.getContent());
										if (((Facet) proposal).getKey() != null)
										{
											relationStm.setSubject(new PdrId(((Facet) proposal).getKey()));
											decoRelSubject.setImage(null);
											validate();
										}
									}
								});
							}
						}
						catch (org.eclipse.jface.bindings.keys.ParseException e1)
						{

							e1.printStackTrace();
						}

					}

					@Override
					public void focusLost(final FocusEvent e)
					{
						if (relationStm.getSubject() == null)
						{
							if (relationSubject.getText() != null
									&& _facade.getPdrObject(new PdrId(relationSubject.getText())) != null)
							{
								relationStm.setSubject(new PdrId(relationSubject.getText()));
								decoRelSubject.setDescriptionText("");
								decoRelSubject.setImage(null);
							}
							else
							{
								relationStm.setSubject(null);
								decoRelSubject.setImage(FieldDecorationRegistry.getDefault()
										.getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).getImage());
								decoRelSubject.setDescriptionText(NLMessages
										.getString("Editor_missing_object_no_relation"));
							}
						}
						else if (relationSubject.getText().trim().length() == 0)
						{
							relationStm.setSubject(null);
							decoRelSubject.setImage(FieldDecorationRegistry.getDefault()
									.getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).getImage());
						}
						validate();
					}
				});

				relationSubject.addKeyListener(new KeyListener()
				{

					@Override
					public void keyPressed(final KeyEvent e)
					{
					}

					@Override
					public void keyReleased(final KeyEvent e)
					{
						if (relationSubject.getText().length() == 23)
						{
							PdrObject o = _facade.getPdrObject(relationStm.getSubject());
							if (o != null)
							{
								decoRelSubject.setImage(null);
								relationStm.setSubject(new PdrId(relationSubject.getText()));
								relationSubject.setText(o.getDisplayNameWithID());
							}
						}
						else if (relationSubject.getText().trim().length() == 0)
						{
							relationStm.setSubject(null);
							decoRelSubject.setImage(FieldDecorationRegistry.getDefault()
									.getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).getImage());
						}
						// if (relationSubject.getText().length() == 23)
						// {
						// relationStm.setSubject(new
						// PdrId(relationSubject.getText()));
						// if (relationStm.getSubject().isValid())
						// {
						// decoRelSubject.setImage(null);
						// PdrObject o =
						// _facade.getReference(relationStm.getSubject());
						// if (o != null)
						// relationSubject.setText(o.getDisplayNameWithID());
						// else
						// relationSubject.setText(relationStm.getSubject().toString());
						// }
						// else
						// {
						// decoRelSubject.setImage(FieldDecorationRegistry
						// .getDefault().getFieldDecoration(FieldDecorationRegistry.DEC_REQUIRED).getImage());
						// }
						// }
						// else if (relationSubject.getText().length() == 0)
						// {
						// relationStm.setSubject(null);
						// decoRelSubject.setImage(FieldDecorationRegistry
						// .getDefault().getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).getImage());
						// }

						validate();
					}
				});

				Button relateCPButton = new Button(_relGroup, SWT.PUSH);
				relateCPButton.setText(NLMessages.getString("Editor_currentPerson"));
				relateCPButton.setToolTipText(NLMessages.getString("Editor_relate_cp_subject_tip"));
				relateCPButton.setImage(_imageReg.get(IconsInternal.PERSON));
				relateCPButton.setEnabled(_mayWrite);
				relateCPButton.setLayoutData(new GridData());
				((GridData) relateCPButton.getLayoutData()).horizontalAlignment = SWT.RIGHT;
				((GridData) relateCPButton.getLayoutData()).grabExcessHorizontalSpace = false;
				relateCPButton.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent event)
					{
						relationSubject.setText(_currentPerson.getPdrId().toString());
						relationStm.setSubject(_currentPerson.getPdrId());

						PdrObject o = _currentPerson;
						if (o != null)
						{
							relationSubject.setText(o.getDisplayNameWithID());
						}
						else
						{
							relationSubject.setText(relationStm.getSubject().toString());
						}
						if (relationStm.getSubject().isValid())
						{
							decoRelSubject.setImage(null);
						}
						else
						{
							decoRelSubject.setImage(FieldDecorationRegistry.getDefault()
									.getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).getImage());
						}
						validate();
					}
				});
				if (_currentPerson != null)
				{
					relateCPButton.setEnabled(_mayWrite);
				}
				else
				{
					relateCPButton.setEnabled(false);

				}

				Button relateSubjectButton = new Button(_relGroup, SWT.PUSH);
				relateSubjectButton.setText(NLMessages.getString("Editor_select_dots"));
				relateSubjectButton.setToolTipText(NLMessages.getString("Editor_open_selObjDialog_subject_tip"));
				relateSubjectButton.setImage(_imageReg.get(IconsInternal.SEARCH));
				relateSubjectButton.setEnabled(_mayWrite);
				relateSubjectButton.setLayoutData(new GridData());
				// ((GridData)
				// relateSubjectButton.getLayoutData()).horizontalAlignment =
				// SWT.RIGHT;
				((GridData) relateSubjectButton.getLayoutData()).grabExcessHorizontalSpace = false;
				relateSubjectButton.setToolTipText(NLMessages.getString("Editor_linkPersonOrAspectToolTip")); //$NON-NLS-1$

				relateSubjectButton.addSelectionListener(new SelectionAdapter()
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
							relationStm.setSubject(_facade.getRequestedId());
							PdrObject o = _facade.getPdrObject(relationStm.getSubject());
							if (o != null)
							{
								relationSubject.setText(o.getDisplayNameWithID());
							}
							else
							{
								relationSubject.setText(_facade.getRequestedId().toString());
							}
							if (relationStm.getSubject().isValid())
							{
								decoRelSubject.setImage(null);
							}
						}
						else
						{
							if (!relationStm.getSubject().isValid())
							{
								decoRelSubject.setImage(FieldDecorationRegistry.getDefault()
									.getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).getImage());
							}
						}
						validate();

					}
				});
//				relateSubjectButton.pack();

				final Button delRelationStm = new Button(_relGroup, SWT.PUSH);
				delRelationStm.setText(NLMessages.getString("Editor_delete"));
				delRelationStm.setToolTipText(NLMessages.getString("Editor_remove_relStm_tip"));
				delRelationStm.setImage(_imageReg.get(IconsInternal.RELATION_REMOVE));
				delRelationStm.setEnabled(_currentAspect.getRelationDim().getRelationStms().size() > 1 && _mayWrite);

				delRelationStm.setLayoutData(new GridData());
				delRelationStm.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent event)
					{
						loadRelationDim(2, (Integer) _relGroup.getData("relStm"), null); //$NON-NLS-1$
						validate();

					}
				});
				delRelationStm.setLayoutData(new GridData());

				final Button addRelation = new Button(_relGroup, SWT.PUSH);
				addRelation.setText(NLMessages.getString("Editor_addRelation"));
				addRelation.setToolTipText(NLMessages.getString("Editor_add_relObj_tip"));
				addRelation.setImage(_imageReg.get(IconsInternal.RELATION_ADD));
				addRelation.setEnabled(_mayWrite);
				addRelation.setLayoutData(new GridData());
				addRelation.setData("relStm", i); //$NON-NLS-1$
				addRelation.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent event)
					{
						loadRelationDim(3, (Integer) addRelation.getData("relStm"), null); //$NON-NLS-1$

						validate();

					}
				});
				addRelation.setLayoutData(new GridData());

				if (relationStm.getRelations() != null) //$NON-NLS-1$
				{
					for (int j = 0; j < relationStm.getRelations().size(); j++) //$NON-NLS-1$
					{
						final Composite relComposite = new Composite(_relGroup, SWT.NONE);
						relComposite.setLayoutData(new GridData());
						((GridData) relComposite.getLayoutData()).horizontalAlignment = SWT.FILL;
						((GridData) relComposite.getLayoutData()).grabExcessHorizontalSpace = true;

						((GridData) relComposite.getLayoutData()).horizontalSpan = 8;
						relComposite.setLayout(new GridLayout());
						((GridLayout) relComposite.getLayout()).numColumns = 6;
						((GridLayout) relComposite.getLayout()).makeColumnsEqualWidth = true;

						relComposite.setData("relStm", i); //$NON-NLS-1$
						relComposite.setData("rel", j); //$NON-NLS-1$

						final Relation relation = relationStm.getRelations().get(j); //$NON-NLS-1$

						final Combo relationClassCombo;
						final Combo relationProviderCombo;
						final Combo relationContextCombo;

						Label relationObjectLabel = new Label(relComposite, SWT.NONE);
						relationObjectLabel.setText(NLMessages.getString("Editor_object"));
						relationObjectLabel.setLayoutData(new GridData());

						final Text relationObject = new Text(relComposite, SWT.BORDER);
						relationObject.setEditable(_mayWrite);
						relationObject.setBackground(WHITE_COLOR);
						relationObject.setLayoutData(new GridData());
						((GridData) relationObject.getLayoutData()).horizontalSpan = 3;
						((GridData) relationObject.getLayoutData()).horizontalAlignment = SWT.FILL;
						((GridData) relationObject.getLayoutData()).grabExcessHorizontalSpace = true;
						final ControlDecoration decoRelObj = new ControlDecoration(relationObject, SWT.LEFT | SWT.TOP);

						if (relation.getObject() != null)
						{
							PdrObject o = _facade.getPdrObject(relation.getObject());
							decoRelObj.setImage(null);
							if (o != null)
							{
								relationObject.setText(o.getDisplayNameWithID());
							}
							else
							{
								relationObject.setText(relation.getObject().toString());
								decoRelObj.setImage(FieldDecorationRegistry.getDefault()
										.getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).getImage());
								decoRelObj
										.setDescriptionText(NLMessages.getString("Editor_missing_object_no_relation"));
							}
						}
						else
						{
							relationObject.setText(""); //$NON-NLS-1$
							decoRelObj.setImage(FieldDecorationRegistry.getDefault()
									.getFieldDecoration(FieldDecorationRegistry.DEC_REQUIRED).getImage());

						}
						ControlDecoration decorelationObj = new ControlDecoration(relationObject, SWT.LEFT | SWT.BOTTOM);
						decorelationObj.setDescriptionText(NLMessages.getString("Editor_relation_proposal_deco"));
						decorelationObj.setImage(FieldDecorationRegistry.getDefault()
								.getFieldDecoration(FieldDecorationRegistry.DEC_INFORMATION).getImage());
						decorelationObj.setShowOnlyOnFocus(false);
						relationObject.addFocusListener(new FocusListener()
						{
							@Override
							public void focusGained(final FocusEvent e)
							{
								char[] autoActivationCharacters = new char[]
								{'.', '#'};
								KeyStroke keyStrokeP;
								KeyStroke keyStrokeA;
								KeyStroke keyStrokeL;
								try
								{
									keyStrokeA = KeyStroke.getInstance("Ctrl+e");
									keyStrokeP = KeyStroke.getInstance("Ctrl+p");
									keyStrokeL = KeyStroke.getInstance("Ctrl+l");

									ContentProposalAdapter adapter = new ContentProposalAdapter(relationObject,
											new TextContentAdapter(), new FacetContentProposalProvider(_facade
													.getAllPersonsFacets()), keyStrokeP, autoActivationCharacters);
									adapter.setLabelProvider(new AutoCompleteNameLabelProvider());
									adapter.addContentProposalListener(new IContentProposalListener()
									{
										@Override
										public void proposalAccepted(final IContentProposal proposal)
										{
											relationObject.setText(proposal.getContent());
											if (((Facet) proposal).getKey() != null)
											{
												relation.setObject(new PdrId(((Facet) proposal).getKey()));
												decoRelObj.setImage(null);
												validate();
											}
										}
									});

									ContentProposalAdapter adapter2 = new ContentProposalAdapter(relationObject,
											new TextContentAdapter(), new FacetContentProposalProvider(_facade
													.getLoadedAspectsFacets()), keyStrokeA, autoActivationCharacters);
									adapter2.setLabelProvider(new AutoCompleteNameLabelProvider());
									adapter2.addContentProposalListener(new IContentProposalListener()
									{
										@Override
										public void proposalAccepted(final IContentProposal proposal)
										{
											relationObject.setText(proposal.getContent());
											if (((Facet) proposal).getKey() != null)
											{
												relation.setObject(new PdrId(((Facet) proposal).getKey()));
												decoRelObj.setImage(null);
												validate();
											}
										}
									});
									if (_facade.getLastObjectsFacets() != null)
									{

										ContentProposalAdapter adapter3 = new ContentProposalAdapter(relationObject,
												new TextContentAdapter(), new FacetContentProposalProvider(_facade
														.getLastObjectsFacets()), keyStrokeL, autoActivationCharacters);
										adapter3.setLabelProvider(new AutoCompleteNameLabelProvider());
										adapter3.addContentProposalListener(new IContentProposalListener()
										{
											@Override
											public void proposalAccepted(final IContentProposal proposal)
											{
												relationObject.setText(proposal.getContent());
												if (((Facet) proposal).getKey() != null)
												{
													relation.setObject(new PdrId(((Facet) proposal).getKey()));
													decoRelObj.setImage(null);
													validate();
												}
											}
										});
									}
								}
								catch (org.eclipse.jface.bindings.keys.ParseException e1)
								{

									e1.printStackTrace();
								}

							}

							@Override
							public void focusLost(final FocusEvent e)
							{
								if (relation.getObject() == null)
								{
									if (relationObject.getText() != null
											&& _facade.getPdrObject(new PdrId(relationObject.getText())) != null)
									{
										relation.setObject(new PdrId(relationObject.getText()));
										decoRelObj.setDescriptionText("");
										decoRelObj.setImage(null);
									}
									else
									{
										relation.setObject(null);
										decoRelObj.setImage(FieldDecorationRegistry.getDefault()
												.getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).getImage());
										decoRelObj.setDescriptionText(NLMessages
												.getString("Editor_missing_object_no_relation"));
									}
								}
								else if (relationObject.getText().trim().length() == 0)
								{
									relation.setObject(null);
									decoRelObj.setImage(FieldDecorationRegistry.getDefault()
											.getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).getImage());
								}
								validate();

							}
						});

						relationObject.addKeyListener(new KeyListener()
						{

							@Override
							public void keyPressed(final KeyEvent e)
							{
							}

							@Override
							public void keyReleased(final KeyEvent e)
							{
								if (relationObject.getText().length() == 23)
								{
									PdrObject o = _facade.getPdrObject(new PdrId(relationObject.getText()));
									if (o != null)
									{
										decoRelObj.setImage(null);
										relation.setObject(new PdrId(relationObject.getText()));
										relationObject.setText(o.getDisplayNameWithID());
									}
								}
								else if (relationObject.getText().trim().length() == 0)
								{
									relation.setObject(null);
									decoRelObj.setImage(FieldDecorationRegistry.getDefault()
											.getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).getImage());
								}

								validate();
							}
						});

						Button relateCPObjButton = new Button(relComposite, SWT.PUSH);
						relateCPObjButton.setText(NLMessages.getString("Editor_currentPerson"));
						relateCPObjButton.setToolTipText(NLMessages.getString("Editor_relate_cp_object_tip"));
						relateCPObjButton.setImage(_imageReg.get(IconsInternal.PERSON));
						relateCPObjButton.setEnabled(_mayWrite);
						relateCPObjButton.setLayoutData(new GridData());
						relateCPObjButton.addSelectionListener(new SelectionAdapter()
						{
							@Override
							public void widgetSelected(final SelectionEvent event)
							{
								relationObject.setText(_currentPerson.getPdrId().toString());
								relation.setObject(_currentPerson.getPdrId());

								PdrObject o = _currentPerson;
								if (o != null)
								{
									relationObject.setText(o.getDisplayNameWithID());
								}
								else
								{
									relationObject.setText(relation.getObject().toString());
								}
								if (relation.isValid())
								{
									decoRelObj.setImage(null);
								}
								else
								{
									decoRelObj.setImage(FieldDecorationRegistry.getDefault()
											.getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).getImage());
									relationObject.setText("");
								}
								validate();
							}
						});
						if (_currentPerson != null)
						{
							relateCPObjButton.setEnabled(_mayWrite);
						}
						else
						{
							relateCPObjButton.setEnabled(false);

						}

						Button relateObjectButton = new Button(relComposite, SWT.PUSH);
						relateObjectButton.setText(NLMessages.getString("Editor_select_dots"));
						relateObjectButton.setToolTipText(NLMessages.getString("Editor_open_selObjDialog_object_tip"));
						relateObjectButton.setImage(_imageReg.get(IconsInternal.SEARCH));
						relateObjectButton.setEnabled(_mayWrite);
						relateObjectButton.setLayoutData(new GridData());
						relateObjectButton.setToolTipText(NLMessages.getString("Editor_linkPersonOrAspectToolTip")); //$NON-NLS-1$
						relateObjectButton.addSelectionListener(new SelectionAdapter()
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
									relation.setObject(_facade.getRequestedId());
									PdrObject o = _facade.getPdrObject(relation.getObject());
									if (o != null)
									{
										relationObject.setText(o.getDisplayNameWithID());
									}
									else
									{
										relationObject.setText(relation.getObject().toString());
									}
									if (relation.isValid())
									{
										decoRelObj.setImage(null);
									}
								}
								else
								{
									if (!relation.isValid())
									{
										decoRelObj.setImage(FieldDecorationRegistry.getDefault()
											.getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).getImage());
										relationObject.setText("");
									}
								}
								validate();

							}
						});
//						relateObjectButton.pack();

						Label relationClassificationLabel = new Label(relComposite, SWT.NONE);
						relationClassificationLabel.setText(NLMessages.getString("Editor_provider"));
						relationClassificationLabel.setLayoutData(new GridData());
						((GridData) relationClassificationLabel.getLayoutData()).horizontalSpan = 1;

						relationProviderCombo = new Combo(relComposite, SWT.READ_ONLY);
						relationProviderCombo.setEnabled(_mayWrite);
						relationProviderCombo.setBackground(WHITE_COLOR);
						relationProviderCombo.setLayoutData(new GridData());
						((GridData) relationProviderCombo.getLayoutData()).horizontalAlignment = GridData.FILL;
						((GridData) relationProviderCombo.getLayoutData()).horizontalSpan = 1;
						((GridData) relationProviderCombo.getLayoutData()).grabExcessHorizontalSpace = true;
						relationProviderCombo.setItems(PDRConfigProvider.readConfigs(_relationProvider,
								"relationProvider",
								null, null,
								null, null));
						if (relation.getProvider() != null)
						{
							ViewHelper.setComboByString(relationProviderCombo, relation.getProvider());
						}
						else
						{
							relationProviderCombo.select(relationProviderCombo.indexOf(_relationProvider));
							relation.setProvider(_relationProvider);
						}

						Label relationContextLabel = new Label(relComposite, SWT.NONE);
						relationContextLabel.setText(NLMessages.getString("Editor_context"));
						relationContextLabel.setLayoutData(new GridData());
						((GridData) relationContextLabel.getLayoutData()).horizontalSpan = 1;

						relationContextCombo = new Combo(relComposite, SWT.READ_ONLY);
						relationContextCombo.setEnabled(_mayWrite);
						relationContextCombo.setBackground(WHITE_COLOR);
						relationContextCombo.setLayoutData(new GridData());
						((GridData) relationContextCombo.getLayoutData()).horizontalSpan = 3;
						((GridData) relationContextCombo.getLayoutData()).horizontalAlignment = SWT.FILL;
						((GridData) relationContextCombo.getLayoutData()).grabExcessHorizontalSpace = true;
						final ComboViewer relationContextComboViewer = new ComboViewer(relationContextCombo);
						relationContextComboViewer.setContentProvider(new MarkupContentProvider());
						relationContextComboViewer.setLabelProvider(new MarkupLabelProvider());
						if (relation.getProvider() != null && _facade.getConfigs().get(relation.getProvider()) != null)
						{
							relationContextComboViewer.setInput(_facade.getConfigs().get(relation.getProvider())
								.getChildren().get("aodl:relation").getChildren());
						}

						final ControlDecoration relContextDeco = new ControlDecoration(relationContextCombo, SWT.RIGHT
								| SWT.TOP);

						Label relationTypeLabel = new Label(relComposite, SWT.NONE);
						relationTypeLabel.setText(NLMessages.getString("Editor_class"));
						relationTypeLabel.setLayoutData(new GridData());
						((GridData) relationTypeLabel.getLayoutData()).horizontalSpan = 1;

						relationClassCombo = new Combo(relComposite, SWT.READ_ONLY);
						relationClassCombo.setEnabled(_mayWrite);
						relationClassCombo.setBackground(WHITE_COLOR);
						relationClassCombo.setLayoutData(new GridData());
						((GridData) relationClassCombo.getLayoutData()).horizontalSpan = 3;
						((GridData) relationClassCombo.getLayoutData()).horizontalAlignment = SWT.FILL;
						((GridData) relationClassCombo.getLayoutData()).grabExcessHorizontalSpace = true;
						final ComboViewer relationClassComboViewer = new ComboViewer(relationClassCombo);
						relationClassComboViewer.setContentProvider(new MarkupContentProvider());
						relationClassComboViewer.setLabelProvider(new MarkupLabelProvider());
						final ControlDecoration relClassDeco = new ControlDecoration(relationClassCombo, SWT.RIGHT
								| SWT.TOP);

						Label b = new Label(relComposite, SWT.NONE);
						b.setText("");
						b.setLayoutData(new GridData());
						((GridData) b.getLayoutData()).horizontalSpan = 2;

						Label relCitationLabel = new Label(relComposite, SWT.NONE);
						relCitationLabel.setText(NLMessages.getString("Config_value"));
						relCitationLabel.setLayoutData(new GridData());

						final Combo relValue = new Combo(relComposite, SWT.BORDER | SWT.READ_ONLY);
						relValue.setEnabled(_mayWrite);
						relValue.setBackground(WHITE_COLOR);
						relValue.setLayoutData(new GridData());
						((GridData) relValue.getLayoutData()).horizontalAlignment = GridData.FILL;
						((GridData) relValue.getLayoutData()).grabExcessHorizontalSpace = true;
						((GridData) relValue.getLayoutData()).horizontalSpan = 4;
						final ComboViewer relValueComboViewer = new ComboViewer(relValue);
						relValueComboViewer.setContentProvider(new MarkupContentProvider());
						relValueComboViewer.setLabelProvider(new MarkupLabelProvider());
						final ControlDecoration relValueDeco = new ControlDecoration(relValue, SWT.RIGHT | SWT.TOP);


						relationProviderCombo.addSelectionListener(new SelectionAdapter()
						{
							@Override
							public void widgetSelected(final SelectionEvent se)
							{
								relationClassCombo.removeAll();
								relationContextCombo.removeAll();
								relValue.removeAll();

								relation.setProvider(relationProviderCombo.getItem(relationProviderCombo
										.getSelectionIndex()));
								relationContextComboViewer.setInput(_facade.getConfigs().get(relation.getProvider())
										.getChildren().get("aodl:relation").getChildren());
							}
						});
//						relationProviderCombo.pack();

//						relValue.pack();

						// control for relationClassCombo
						// ArrayList<String> list = cListPro
						//						.getList("types", "type", "relation"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						// for (int n = 0; n < list.size(); n++)
						// {
						// relationClassCombo.add(list.get(n));
						// }
						if (relation.getContext() != null)
						{
							ViewHelper.setComboViewerByString(relationContextComboViewer, relation.getContext(), true);
						}

						relationContextComboViewer.addSelectionChangedListener(new ISelectionChangedListener()
						{
							@Override
							public void selectionChanged(final SelectionChangedEvent event)
							{
								ISelection iSelection = event.getSelection();
								Object obj = ((IStructuredSelection) iSelection).getFirstElement();
								ConfigData cd = (ConfigData) obj;
								relation.setContext(cd.getValue());
								relationClassCombo.removeAll();
								relValue.removeAll();
								relContextDeco.setDescriptionText(PDRConfigProvider.readDocu(relation.getProvider(),
										"relation",
										"context", relation.getContext(), null, null, null));
								if (relContextDeco.getDescriptionText() != null)
								{
									relContextDeco.setImage(FieldDecorationRegistry.getDefault()
											.getFieldDecoration(FieldDecorationRegistry.DEC_INFORMATION).getImage());
								}
								else
								{
									relContextDeco.setImage(null);
								}
								relationClassComboViewer.setInput(_facade.getConfigs().get(relation.getProvider())
										.getChildren().get("aodl:relation").getChildren().get(relation.getContext())
										.getChildren());
							}

						});

//						relationContextCombo.pack();

						// control for relationContextCombo
						if (relation.getRClass() != null)
						{
							if (relation.getContext() != null
									&& _facade.getConfigs().get(relation.getProvider()) != null
									&& _facade.getConfigs().get(relation.getProvider()).getChildren()
											.get("aodl:relation") != null
									&& _facade.getConfigs().get(relation.getProvider()).getChildren()
											.get("aodl:relation").getChildren().get(relation.getContext()) != null)
							{
								relationClassComboViewer.setInput(_facade.getConfigs().get(relation.getProvider())
										.getChildren().get("aodl:relation").getChildren().get(relation.getContext())
										.getChildren());
							}
							ViewHelper.setComboViewerByString(relationClassComboViewer, relation.getRClass(), true);
						}
						else
						{
							relationClassComboViewer.setInput(null);
							relationClassComboViewer.refresh();
						}
						relationClassComboViewer.addSelectionChangedListener(new ISelectionChangedListener()
						{
							@Override
							public void selectionChanged(final SelectionChangedEvent event)
							{
								ISelection iSelection = event.getSelection();
								Object obj = ((IStructuredSelection) iSelection).getFirstElement();
								ConfigData cd = (ConfigData) obj;
								relation.setRClass(cd.getValue());
								relValueComboViewer.setInput(_facade.getConfigs().get(relation.getProvider())
										.getChildren().get("aodl:relation").getChildren().get(relation.getContext())
										.getChildren().get(relation.getRClass()).getChildren());
								relClassDeco.setDescriptionText(PDRConfigProvider.readDocu(relation.getProvider(),
										"relation", "class",
										relation.getContext(), relation.getRClass(), null, null));
								if (relClassDeco.getDescriptionText() != null)
								{
									relClassDeco.setImage(FieldDecorationRegistry.getDefault()
											.getFieldDecoration(FieldDecorationRegistry.DEC_INFORMATION).getImage());
								}
								else
								{
									relClassDeco.setImage(null);
								}
							}
						});
//						relationClassCombo.pack();
						if (relation.getRelation() != null)
						{
							if (relation.getRClass() != null
									&& _facade.getConfigs().get(relation.getProvider()) != null
									&& _facade.getConfigs().get(relation.getProvider()).getChildren()
											.get("aodl:relation") != null
									&& _facade.getConfigs().get(relation.getProvider()).getChildren()
											.get("aodl:relation").getChildren().get(relation.getContext()) != null
									&& _facade.getConfigs().get(relation.getProvider()).getChildren()
											.get("aodl:relation").getChildren().get(relation.getContext())
											.getChildren().get(relation.getRClass()) != null)
							{
								relValueComboViewer.setInput(_facade.getConfigs().get(relation.getProvider())
										.getChildren().get("aodl:relation").getChildren().get(relation.getContext())
										.getChildren().get(relation.getRClass()).getChildren());
							}
							ViewHelper.setComboViewerByString(relValueComboViewer, relation.getRelation(), true);
						}
						else
						{
							relValueComboViewer.setInput(null);
							relValueComboViewer.refresh();
						}
						relValueComboViewer.addSelectionChangedListener(new ISelectionChangedListener()
						{
							@Override
							public void selectionChanged(final SelectionChangedEvent event)
							{
								ISelection iSelection = event.getSelection();
								Object obj = ((IStructuredSelection) iSelection).getFirstElement();
								ConfigData cd = (ConfigData) obj;
								relation.setRelation(cd.getValue());
								relValueDeco.setDescriptionText(PDRConfigProvider.readDocu(relation.getProvider(),
										"relation", "value",
										relation.getContext(), relation.getRClass(), relation.getRelation(), null));
								if (relValueDeco.getDescriptionText() != null)
								{
									relValueDeco.setImage(FieldDecorationRegistry.getDefault()
											.getFieldDecoration(FieldDecorationRegistry.DEC_INFORMATION).getImage());
								}
								else
								{
									relValueDeco.setImage(null);
								}

							}
						});

						final Button delRelation = new Button(relComposite, SWT.PUSH);
						delRelation.setText(NLMessages.getString("Editor_deleteRel"));
						delRelation.setToolTipText(NLMessages.getString("Editor_remove_relation_tip"));
						delRelation.setImage(_imageReg.get(IconsInternal.RELATION_REMOVE));
						delRelation.setEnabled(_mayWrite);
						delRelation.setLayoutData(_gridData);
						((GridData) delRelation.getLayoutData()).horizontalIndent = 8;
						delRelation.addSelectionListener(new SelectionAdapter()
						{
							@Override
							public void widgetSelected(final SelectionEvent event)
							{
								loadRelationDim(4, (Integer) relComposite.getData("relStm"), //$NON-NLS-1$
										((Integer) relComposite.getData("rel"))); //$NON-NLS-1$
								validate();
							}
						});
						delRelation.setLayoutData(new GridData());

						relComposite.layout();
					}
				}
			}
			_relGroup.layout();

		}
		contentCompRel.layout();

		_scrollCompRel.setContent(contentCompRel);
		Point point = contentCompRel.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
		Point mp = _mainTabFolder.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
		if (point.x > mp.x - 20)
		{
			point.x = mp.x - 20;
		}
		_scrollCompRel.setMinSize(point);
		_scrollCompRel.layout();
		_relationComposite.redraw();
		_relationComposite.layout();
//		_relationComposite.pack();
//		_mainTabFolder.redraw();
		_mainTabFolder.layout();
//		_mainTabFolder.pack();
	}

	/**
	 * <h4>Load time spatial dim.</h4>
	 * <p>
	 * generates GUI elements for representation and modification of
	 * {@link TimeDim} and {@link SpatialDim} information of the current {@link Aspect}.
	 * </p>
	 * @param type the type
	 * @param timeStm the time stm
	 * @param time the time
	 * @param spatialStm the spatial stm
	 * @param place the place
	 */
	private void loadTimeSpatialDim(Integer type, final Integer timeStm, final Integer time, final Integer spatialStm,
			final Integer place)
	{
		System.out.println("LOAD loadTimeSpatialDim");
		if (_scrollCompTimePlace != null)
		{
			_scrollCompTimePlace.dispose();
		}
		if (_timeGroup != null)
		{
			_timeGroup.dispose();
		}
		if (_placeGroup != null)
		{
			_placeGroup.dispose();
		}
		_scrollCompTimePlace = new ScrolledComposite(_dimensionComposite, SWT.BORDER | SWT.V_SCROLL);
		_scrollCompTimePlace.setExpandHorizontal(true);
		_scrollCompTimePlace.setExpandVertical(true);
		_scrollCompTimePlace.setMinHeight(1);
		_scrollCompTimePlace.setMinWidth(1);

		_scrollCompTimePlace.setLayout(new GridLayout());
		_scrollCompTimePlace.setLayoutData(new GridData());
		((GridData) _scrollCompTimePlace.getLayoutData()).heightHint = 490;
		((GridData) _scrollCompTimePlace.getLayoutData()).widthHint = 700;
		((GridData) _scrollCompTimePlace.getLayoutData()).horizontalSpan = 2;

		((GridData) _scrollCompTimePlace.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _scrollCompTimePlace.getLayoutData()).grabExcessHorizontalSpace = true;
		_scrollCompTimePlace.layout();

		Composite contentCompTimePlace = new Composite(_scrollCompTimePlace, SWT.NONE);
		contentCompTimePlace.setLayout(new GridLayout());
		_scrollCompTimePlace.setContent(contentCompTimePlace);

		if (_currentAspect.getTimeDim() == null)
		{
			_currentAspect.setTimeDim(new TimeDim());
			_currentAspect.getTimeDim().setTimeStms(new Vector<TimeStm>());
		}
		if (_currentAspect.getSpatialDim() == null)
		{
			_currentAspect.setSpatialDim(new SpatialDim());
			_currentAspect.getSpatialDim().setSpatialStms(new Vector<SpatialStm>());
		}
		if (_currentAspect.getTimeDim().getTimeStms().size() == 0
				&& _currentAspect.getSpatialDim().getSpatialStms().size() == 0)
		{
			type = 15;
		}
		else if (_currentAspect.getTimeDim().getTimeStms().size() == 0)
		{
			type = 1;
		}
		else if (_currentAspect.getSpatialDim().getSpatialStms().size() == 0)
		{
			type = 5;
		}

		switch (type)
		{
			case 0:
				break; // nix, normales laden
			case 1: // neues timeStm einfügen

				_currentAspect.getTimeDim().getTimeStms().add(new TimeStm());

				break;
			case 2: // timeStm löschen
				_currentAspect.getTimeDim().remove(timeStm);
				break;
			case 3: // neue time einfügen
				_currentAspect.getTimeDim().getTimeStms().get(timeStm).getTimes().add(new Time());
				_currentAspect.getTimeDim().getTimeStms().get(timeStm).getTimes().lastElement()
						.setTimeStamp(new PdrDate(PRESELECTED_YEAR, 0, 0));

				break;
			case 4: // time löschen
				_currentAspect.getTimeDim().getTimeStms().get(timeStm).getTimes().removeElementAt(time);
				break;

			case 5: // neues spatialStm einfügen

				_currentAspect.getSpatialDim().getSpatialStms().add(new SpatialStm());

				break;
			case 6: // spatialStm löschen
				_currentAspect.getSpatialDim().remove(spatialStm);
				break;
			case 7: // neuer place einfügen
				_currentAspect.getSpatialDim().getSpatialStms().get(spatialStm).getPlaces().add(new Place());
				break;
			case 8: // place löschen
				_currentAspect.getSpatialDim().getSpatialStms().get(spatialStm).getPlaces().removeElementAt(place);
				break;
			case 15: // neues spatialStm und timeStm einfügen
				_currentAspect.getTimeDim().getTimeStms().add(new TimeStm());
				_currentAspect.getSpatialDim().getSpatialStms().add(new SpatialStm());
				break;
			default:
				break;
		}

		if (_currentAspect.getTimeDim() != null && _currentAspect.getTimeDim().getTimeStms() != null)
		{
			for (int i = 0; i < _currentAspect.getTimeDim().getTimeStms().size(); i++)
			{
				final TimeStm tStm = _currentAspect.getTimeDim().getTimeStms().get(i);
				_timeGroup = new Group(contentCompTimePlace, SWT.SHADOW_IN);
				_timeGroup.setData("timeStm", i); //$NON-NLS-1$
				_timeGroup.setLayout(new GridLayout());
				((GridLayout) _timeGroup.getLayout()).numColumns = 5;
				_timeGroup.setLayoutData(new GridData());
				((GridLayout) _timeGroup.getLayout()).makeColumnsEqualWidth = false;
				((GridData) _timeGroup.getLayoutData()).horizontalAlignment = GridData.FILL;
				((GridData) _timeGroup.getLayoutData()).grabExcessHorizontalSpace = true;
				((GridData) _timeGroup.getLayoutData()).minimumHeight = 20;

				int m = 1 + i;
				_timeGroup.setText(NLMessages.getString("Editor_timeStm") + m);

				Label spatialStmTypeLabel = new Label(_timeGroup, SWT.NONE);
				spatialStmTypeLabel.setText(NLMessages.getString("Editor_space_type"));
				spatialStmTypeLabel.setLayoutData(new GridData());

				final Combo timeStmCombo = new Combo(_timeGroup, SWT.DROP_DOWN | SWT.READ_ONLY);
				timeStmCombo.setEnabled(_mayWrite);
				timeStmCombo.setBackground(WHITE_COLOR);
				timeStmCombo.setLayoutData(new GridData());
				((GridData) timeStmCombo.getLayoutData()).horizontalAlignment = GridData.FILL;
				((GridData) timeStmCombo.getLayoutData()).grabExcessHorizontalSpace = true;
				((GridData) timeStmCombo.getLayoutData()).horizontalSpan = 2;
				ComboViewer timeStmComboViewer = new ComboViewer(timeStmCombo);
				timeStmComboViewer.setContentProvider(ArrayContentProvider.getInstance());
				timeStmComboViewer.setLabelProvider(new LabelProvider()
				{

					@Override
					public String getText(final Object element)
					{
						String str = (String) element;
						return NLMessages.getString("Editor_timetypes_" + str);
					}

				});

				timeStmComboViewer.setInput(AEConstants.TIMEDIMTYPES);
				timeStmComboViewer.addSelectionChangedListener(new ISelectionChangedListener()
				{

					@Override
					public void selectionChanged(final SelectionChangedEvent event)
					{
						ISelection selection = event.getSelection();
						Object obj = ((IStructuredSelection) selection).getFirstElement();
						String s = (String) obj;
						tStm.setType(s);
					}

				});
				if (tStm.getType() != null)
				{
					StructuredSelection selection = new StructuredSelection(tStm.getType());
					timeStmComboViewer.setSelection(selection);
				}
				else
				{
					timeStmCombo.select(0);
					ISelection selection = timeStmComboViewer.getSelection();
					Object obj = ((IStructuredSelection) selection).getFirstElement();
					String s = (String) obj;
					tStm.setType(s);
				}

				final Button delTimeStm = new Button(_timeGroup, SWT.PUSH);
				delTimeStm.setText(NLMessages.getString("Editor_delete"));
				delTimeStm.setToolTipText(NLMessages.getString("Editor_remove_timeStm_tip"));
				delTimeStm.setImage(_imageReg.get(IconsInternal.TIME_REMOVE));
				delTimeStm.setLayoutData(_gridData);
				delTimeStm.setData("timeStm", i); //$NON-NLS-1$
				delTimeStm.setEnabled(_currentAspect.getTimeDim().getTimeStms().size() > 1 && _mayWrite);
				delTimeStm.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent event)
					{
						//					System.out.println("del timeStm " + (Integer) delTimeStm.getData("timeStm")); //$NON-NLS-1$ //$NON-NLS-2$
						loadTimeSpatialDim(2, (Integer) delTimeStm.getData("timeStm"), null, null, null); //$NON-NLS-1$
						validate();

					}
				});
				delTimeStm.setLayoutData(new GridData());

				final Button addTime = new Button(_timeGroup, SWT.PUSH);
				addTime.setText(NLMessages.getString("Editor_addTime"));
				addTime.setToolTipText(NLMessages.getString("Editor_add_time_tip"));
				addTime.setImage(_imageReg.get(IconsInternal.ADD));
				addTime.setEnabled(_mayWrite);
				addTime.setLayoutData(_gridData);
				addTime.setData("timeStm", i); //$NON-NLS-1$
				addTime.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent event)
					{
						//					System.out.println("add time " + (Integer) addTime.getData("timeStm")); //$NON-NLS-1$ //$NON-NLS-2$
						loadTimeSpatialDim(3, (Integer) addTime.getData("timeStm"), null, null, null); //$NON-NLS-1$
						validate();

					}
				});
				addTime.setLayoutData(new GridData());

				if (tStm.getTimes() != null)
				{

					for (int j = 0; j < tStm.getTimes().size(); j++)
					{
						final Time tTime = tStm.getTimes().get(j);
						final Composite compositeTime = new Composite(_timeGroup, SWT.NONE);
						compositeTime.setData("timeStm", i); //$NON-NLS-1$
						compositeTime.setData("time", j); //$NON-NLS-1$
						compositeTime.setLayout(new GridLayout());
						compositeTime.setLayoutData(new GridData());
						((GridLayout) compositeTime.getLayout()).numColumns = 8;
						((GridLayout) compositeTime.getLayout()).makeColumnsEqualWidth = false;
						((GridData) compositeTime.getLayoutData()).horizontalSpan = 5;
						((GridData) compositeTime.getLayoutData()).horizontalAlignment = SWT.FILL;
						((GridData) compositeTime.getLayoutData()).grabExcessHorizontalSpace = true;

						FocusListener focusListener = new FocusAdapter()
						{
						};
						int l = j + 1;
						Label timeLabel = new Label(compositeTime, SWT.NONE);
						timeLabel.setText(NLMessages.getString("Editor_time_space") + l);
						timeLabel.setLayoutData(new GridData());

						final Combo timeTypeCombo = new Combo(compositeTime, SWT.DROP_DOWN | SWT.READ_ONLY);
						timeTypeCombo.setBackground(WHITE_COLOR);
						timeTypeCombo.setEnabled(_mayWrite);
						timeTypeCombo.setLayoutData(new GridData());
						((GridData) timeTypeCombo.getLayoutData()).horizontalSpan = 1;
						((GridData) timeTypeCombo.getLayoutData()).horizontalAlignment = SWT.FILL;
						((GridData) timeTypeCombo.getLayoutData()).grabExcessHorizontalSpace = true;
						ComboViewer timeTypeComboViewer = new ComboViewer(timeTypeCombo);
						timeTypeComboViewer.setContentProvider(ArrayContentProvider.getInstance());
						timeTypeComboViewer.setLabelProvider(new LabelProvider()
						{

							@Override
							public String getText(final Object element)
							{
								String str = (String) element;
								if (NLMessages.getString("Editor_time_" + str) != null)
								{
									return NLMessages.getString("Editor_time_" + str);
								}
								return str;
							}

						});

						timeTypeComboViewer.setInput(AEConstants.TIME_TYPES);
						timeTypeComboViewer.addSelectionChangedListener(new ISelectionChangedListener()
						{

							@Override
							public void selectionChanged(final SelectionChangedEvent event)
							{
								ISelection selection = event.getSelection();
								Object obj = ((IStructuredSelection) selection).getFirstElement();
								String s = (String) obj;
								tTime.setType(s);
							}

						});
						if (tTime.getType() != null)
						{
							StructuredSelection selection = new StructuredSelection(tTime.getType());
							timeTypeComboViewer.setSelection(selection);
						}
						else
						{
							timeTypeCombo.select(0);
							ISelection selection = timeTypeComboViewer.getSelection();
							Object obj = ((IStructuredSelection) selection).getFirstElement();
							String s = (String) obj;
							tTime.setType(s);
						}

						Combo comboTimeDay = new Combo(compositeTime, SWT.READ_ONLY);
						comboTimeDay.setEnabled(_mayWrite);
						comboTimeDay.setBackground(WHITE_COLOR);
						comboTimeDay.setLayoutData(new GridData());
						((GridData) comboTimeDay.getLayoutData()).horizontalAlignment = GridData.FILL;
						((GridData) comboTimeDay.getLayoutData()).grabExcessHorizontalSpace = true;
						comboTimeDay.setItems(AEConstants.DAYS);
						comboTimeDay.addFocusListener(focusListener);

						if (tTime.getTimeStamp() != null)
						{
							comboTimeDay.select(tTime.getTimeStamp().getDay());
						}
						comboTimeDay.addSelectionListener(new SelectionAdapter()
						{
							@Override
							public void widgetSelected(final SelectionEvent se)
							{
								tTime.getTimeStamp().setDay(timeTypeCombo.getSelectionIndex());
							}
						});

						final Combo comboTimeMonth = new Combo(compositeTime, SWT.READ_ONLY);
						comboTimeMonth.setEnabled(_mayWrite);
						comboTimeMonth.setBackground(WHITE_COLOR);
						comboTimeMonth.setLayoutData(new GridData());
						((GridData) comboTimeMonth.getLayoutData()).horizontalAlignment = GridData.FILL;
						((GridData) comboTimeMonth.getLayoutData()).grabExcessHorizontalSpace = true;
						comboTimeMonth.setItems(AEConstants.MONTHS);
						comboTimeMonth.addFocusListener(focusListener);

						if (tTime.getTimeStamp() != null)
						{
							comboTimeMonth.select(tTime.getTimeStamp().getMonth());
						}
						comboTimeMonth.addSelectionListener(new SelectionAdapter()
						{
							@Override
							public void widgetSelected(final SelectionEvent se)
							{
								tTime.getTimeStamp().setMonth(comboTimeMonth.getSelectionIndex());
							}
						});

						final YearSpinner spinnerTimeYear = new YearSpinner(compositeTime, SWT.BORDER);
						spinnerTimeYear.setEnabled(_mayWrite);
						final ControlDecoration decoTime = new ControlDecoration(spinnerTimeYear, SWT.LEFT | SWT.TOP);
						if (tTime.getTimeStamp() != null)
						{
							spinnerTimeYear.setSelection(tTime.getTimeStamp().getYear());
						}
						else
						{
							decoTime.setImage(FieldDecorationRegistry.getDefault()
									.getFieldDecoration(FieldDecorationRegistry.DEC_REQUIRED).getImage());
						}
						spinnerTimeYear.pack();
						spinnerTimeYear.addFocusListener(focusListener);
						spinnerTimeYear.addSelectionListener(new SelectionAdapter()
						{
							@Override
							public void widgetSelected(final SelectionEvent se)
							{
								tTime.getTimeStamp().setYear(spinnerTimeYear.getSelection());
								if (tTime.isValid())
								{
									decoTime.setImage(null);
								}
								else
								{
									decoTime.setImage(FieldDecorationRegistry.getDefault()
											.getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).getImage());
								}
								validate();
							}
						});
						Label timeAccuracy = new Label(compositeTime, SWT.NONE);
						timeAccuracy.setText(NLMessages.getString("Editor_quality2"));
						timeAccuracy.setLayoutData(new GridData());

						final Combo timeAccuracyCombo = new Combo(compositeTime, SWT.DROP_DOWN | SWT.READ_ONLY);
						timeAccuracyCombo.setEnabled(_mayWrite);
						timeAccuracyCombo.setBackground(WHITE_COLOR);
						timeAccuracyCombo.setLayoutData(new GridData());
						((GridData) timeAccuracyCombo.getLayoutData()).horizontalSpan = 2;
						((GridData) timeAccuracyCombo.getLayoutData()).horizontalSpan = 1;
						((GridData) timeAccuracyCombo.getLayoutData()).horizontalAlignment = SWT.FILL;
						((GridData) timeAccuracyCombo.getLayoutData()).grabExcessHorizontalSpace = true;
						ComboViewer timeAccuracyComboViewer = new ComboViewer(timeAccuracyCombo);
						timeAccuracyComboViewer.setContentProvider(ArrayContentProvider.getInstance());
						timeAccuracyComboViewer.setLabelProvider(new LabelProvider()
						{

							@Override
							public String getText(final Object element)
							{
								String str = (String) element;
								return NLMessages.getString("Editor_accuracy_" + str);
							}

						});

						timeAccuracyComboViewer.setInput(AEConstants.TIME_ACCURACY);
						timeAccuracyComboViewer.addSelectionChangedListener(new ISelectionChangedListener()
						{

							@Override
							public void selectionChanged(final SelectionChangedEvent event)
							{
								ISelection selection = event.getSelection();
								Object obj = ((IStructuredSelection) selection).getFirstElement();
								String s = (String) obj;
								tTime.setAccuracy(s);
							}

						});
						if (tTime.getAccuracy() != null) //$NON-NLS-1$ //$NON-NLS-2$
						{
							StructuredSelection selection = new StructuredSelection(tTime.getAccuracy());
							timeAccuracyComboViewer.setSelection(selection);
						}
						else
						{
							timeAccuracyCombo.select(0);
							ISelection selection = timeAccuracyComboViewer.getSelection();
							Object obj = ((IStructuredSelection) selection).getFirstElement();
							String s = (String) obj;
							tTime.setAccuracy(s);
						}

						final Button delTime = new Button(compositeTime, SWT.PUSH);
						delTime.setText(NLMessages.getString("Editor_deleteTime"));
						delTime.setToolTipText(NLMessages.getString("Editor_remove_time_tip"));
						delTime.setImage(_imageReg.get(IconsInternal.REMOVE));
						delTime.setEnabled(_mayWrite);
						// delTime.setImage(Activator.getDefault().getImageDescriptor("remove).createImage());
						delTime.setLayoutData(_gridData);
						delTime.addSelectionListener(new SelectionAdapter()
						{
							@Override
							public void widgetSelected(final SelectionEvent event)
							{
								//							System.out.println("del time " + (Integer) compositeTime //$NON-NLS-1$
								// .getData("timeStm")
								//	+ " " + (Integer) compositeTime.getData("time")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
								loadTimeSpatialDim(4, (Integer) compositeTime.getData("timeStm"), //$NON-NLS-1$
										(Integer) compositeTime.getData("time"), null, null); //$NON-NLS-1$
								validate();
							}
						});
						delTime.setLayoutData(new GridData());
						delTime.pack();
						compositeTime.layout();
					} // compositeTime
				}
				// timeGroup
			}
		}

		if (_currentAspect.getSpatialDim() != null && _currentAspect.getSpatialDim().getSpatialStms() != null)
		{
			for (int i = 0; i < _currentAspect.getSpatialDim().getSpatialStms().size(); i++)
			{
				final SpatialStm sStm = _currentAspect.getSpatialDim().getSpatialStms().get(i);
				int l = i + 1;
				_placeGroup = new Group(contentCompTimePlace, SWT.SHADOW_IN);
				_placeGroup.setData("spatialStm", i); //$NON-NLS-1$
				_placeGroup.setLayout(new GridLayout());
				((GridLayout) _placeGroup.getLayout()).numColumns = 5;
				_placeGroup.setText(NLMessages.getString("Editor_spatialStm") + l);
				_placeGroup.setLayoutData(new GridData());
				((GridData) _placeGroup.getLayoutData()).horizontalAlignment = GridData.FILL;
				((GridData) _placeGroup.getLayoutData()).minimumHeight = 60;
				((GridData) _placeGroup.getLayoutData()).grabExcessHorizontalSpace = true;

				Label spatialStmTypeLabel = new Label(_placeGroup, SWT.NONE);
				spatialStmTypeLabel.setText(NLMessages.getString("Editor_stm_space") + l
						+ NLMessages.getString("Editor_space_type"));
				spatialStmTypeLabel.setLayoutData(new GridData());

				final Combo spatialTypCombo = new Combo(_placeGroup, SWT.DROP_DOWN | SWT.READ_ONLY);
				spatialTypCombo.setEnabled(_mayWrite);
				spatialTypCombo.setBackground(WHITE_COLOR);
				spatialTypCombo.setLayoutData(new GridData());
				((GridData) spatialTypCombo.getLayoutData()).horizontalAlignment = GridData.FILL;
				((GridData) spatialTypCombo.getLayoutData()).horizontalSpan = 2;
				((GridData) spatialTypCombo.getLayoutData()).grabExcessHorizontalSpace = true;
				ComboViewer spatialTypComboViewer = new ComboViewer(spatialTypCombo);
				spatialTypComboViewer.setContentProvider(ArrayContentProvider.getInstance());
				spatialTypComboViewer.setLabelProvider(new LabelProvider()
				{

					@Override
					public String getText(final Object element)
					{
						String str = (String) element;
						return NLMessages.getString("Editor_spatialdim_types_" + str);
					}

				});

				spatialTypComboViewer.setInput(AEConstants.SPATIALDIMTYPES);
				spatialTypComboViewer.addSelectionChangedListener(new ISelectionChangedListener()
				{

					@Override
					public void selectionChanged(final SelectionChangedEvent event)
					{
						ISelection selection = event.getSelection();
						Object obj = ((IStructuredSelection) selection).getFirstElement();
						String s = (String) obj;
						sStm.setType(s);
					}

				});
				if (sStm.getType() != null) //$NON-NLS-1$
				{
					StructuredSelection selection = new StructuredSelection(sStm.getType());
					spatialTypComboViewer.setSelection(selection);
				}
				else
				{
					spatialTypCombo.select(0);
					ISelection selection = spatialTypComboViewer.getSelection();
					Object obj = ((IStructuredSelection) selection).getFirstElement();
					String s = (String) obj;
					sStm.setType(s);
				}

				final Button delSpatialStm = new Button(_placeGroup, SWT.PUSH);
				delSpatialStm.setText(NLMessages.getString("Editor_delete"));
				delSpatialStm.setToolTipText(NLMessages.getString("Editor_remove_spatialStm_tip"));
				delSpatialStm.setImage(_imageReg.get(IconsInternal.PLACE_REMOVE));
				delSpatialStm.setEnabled(_currentAspect.getSpatialDim().getSpatialStms().size() > 1 && _mayWrite);

				delSpatialStm.setLayoutData(_gridData);
				delSpatialStm.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent event)
					{
						//						System.out.println("del timeStm " + (Integer) placeGroup //$NON-NLS-1$
						//								.getData("spatialStm")); //$NON-NLS-1$
						loadTimeSpatialDim(6, null, null, ((Integer) _placeGroup.getData("spatialStm")), null); //$NON-NLS-1$

					}
				});
				delSpatialStm.setLayoutData(new GridData());

				final Button addPlace = new Button(_placeGroup, SWT.PUSH);
				addPlace.setText(NLMessages.getString("Editor_addPlace"));
				addPlace.setToolTipText(NLMessages.getString("Editor_add_place_tip"));
				addPlace.setImage(_imageReg.get(IconsInternal.ADD));
				addPlace.setEnabled(_mayWrite);
				addPlace.setLayoutData(_gridData);
				addPlace.setData("spatialStm", i); //$NON-NLS-1$
				addPlace.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent event)
					{
						//						System.out.println("add place " + (Integer) placeGroup //$NON-NLS-1$
						//								.getData("spatialStm")); //$NON-NLS-1$
						loadTimeSpatialDim(7, null, null, ((Integer) addPlace.getData("spatialStm")), null); //$NON-NLS-1$

						validate();

					}
				});
				addPlace.setLayoutData(new GridData());

				if (sStm.getPlaces() != null)
				{
					for (int j = 0; j < sStm.getPlaces().size(); j++)
					{
						int m = j + 1;

						final Place p = sStm.getPlaces().get(j);
						final Composite compositePlace = new Composite(_placeGroup, SWT.NONE);
						compositePlace.setData("spatialStm", i); //$NON-NLS-1$
						compositePlace.setData("place", j); //$NON-NLS-1$
						compositePlace.setLayout(new GridLayout());
						compositePlace.setLayoutData(new GridData());
						((GridLayout) compositePlace.getLayout()).numColumns = 6;
						((GridLayout) compositePlace.getLayout()).makeColumnsEqualWidth = true;
						((GridData) compositePlace.getLayoutData()).horizontalAlignment = GridData.FILL;
						((GridData) compositePlace.getLayoutData()).horizontalSpan = 5;

						@SuppressWarnings("unused")
						FocusListener focusListener = new FocusAdapter()
						{

						};

						Label spacialLabel = new Label(compositePlace, SWT.NONE);
						spacialLabel.setText(NLMessages.getString("Editor_palce_space") + m
								+ NLMessages.getString("Editor_space_name"));
						spacialLabel.setLayoutData(new GridData());

						final Text placeText = new Text(compositePlace, SWT.BORDER);
						placeText.setEditable(_mayWrite);
						placeText.setBackground(WHITE_COLOR);
						placeText.setLayoutData(new GridData());
						((GridData) placeText.getLayoutData()).horizontalAlignment = GridData.FILL;
						((GridData) placeText.getLayoutData()).grabExcessHorizontalSpace = true;
						((GridData) placeText.getLayoutData()).horizontalSpan = 5;
						final ControlDecoration decoPlace = new ControlDecoration(placeText, SWT.LEFT | SWT.TOP);

						if (p.getPlaceName() != null)
						{
							placeText.setText(p.getPlaceName()); //$NON-NLS-1$

						}
						else
						{
							placeText.setText(""); //$NON-NLS-1$
							decoPlace.setImage(FieldDecorationRegistry.getDefault()
									.getFieldDecoration(FieldDecorationRegistry.DEC_REQUIRED).getImage());
						}
						placeText.addFocusListener(new FocusListener()
						{
							@Override
							public void focusLost(final FocusEvent e)
							{
								p.setPlaceName(placeText.getText()); //$NON-NLS-1$
								if (p.isValid())
								{
									decoPlace.setImage(null);
								}
								else
								{
									decoPlace.setImage(FieldDecorationRegistry.getDefault()
											.getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).getImage());
								}
								validate();

							}

							@Override
							public void focusGained(FocusEvent e) {
								String[] vals = new String[]
								{"NO PLACE FOUNG"};
								try
								{
									vals = _mainSearcher.getFacets("tagging", "placeName", p.getType(), p.getSubtype(), null);
								}
								catch (Exception e1)
								{

									e1.printStackTrace();
								}
								new AutoCompleteField(placeText, new TextContentAdapter(), vals);
							}
						});
						placeText.addKeyListener(new KeyListener()
						{
							@Override
							public void keyPressed(final KeyEvent e)
							{
							}

							@Override
							public void keyReleased(final KeyEvent e)
							{
								p.setPlaceName(placeText.getText()); //$NON-NLS-1$
								if (p.isValid())
								{
									decoPlace.setImage(null);
								}
								else
								{
									decoPlace.setImage(FieldDecorationRegistry.getDefault()
											.getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).getImage());
								}
								validate();
							}
						});
						

						Label placeTypeL = new Label(compositePlace, SWT.NONE);
						placeTypeL.setText(NLMessages.getString("Editor_type"));
						placeTypeL.setLayoutData(new GridData());

						final Combo placeTypeCombo = new Combo(compositePlace, SWT.DROP_DOWN | SWT.READ_ONLY);
						placeTypeCombo.setEnabled(_mayWrite);
						placeTypeCombo.setBackground(WHITE_COLOR);
						placeTypeCombo.setLayoutData(new GridData());
						((GridData) placeTypeCombo.getLayoutData()).horizontalAlignment = GridData.FILL;
						((GridData) placeTypeCombo.getLayoutData()).grabExcessHorizontalSpace = true;
						((GridData) placeTypeCombo.getLayoutData()).horizontalSpan = 2;

						ComboViewer placeTypeComboViewer = new ComboViewer(placeTypeCombo);
						placeTypeComboViewer.setContentProvider(new MarkupContentProvider());
						placeTypeComboViewer.setLabelProvider(new MarkupLabelProvider());
						if (_facade.getConfigs().containsKey(_markupProvider))
						{
							placeTypeComboViewer.setInput(_facade.getConfigs().get(_markupProvider).getChildren()
									.get("aodl:placeName").getChildren());
						}
						// placeTypeCombo.setItems(readConfigs(_markupProvider,
						// "markup", "type", "placeName", null, null));
						//
						// for (String sdt : AEConstants.PLACESCALE)
						// {
						// placeTypeCombo.add(sdt);
						// }
						if (p.getType() != null)
						{
							ViewHelper.setComboViewerByString(placeTypeComboViewer, p.getType(), true);

						}
						else
						{
							ConfigData cd = (ConfigData) placeTypeComboViewer.getElementAt(0);
							p.setType(cd.getValue());
						}

						Label placeSubtypeL = new Label(compositePlace, SWT.NONE);
						placeSubtypeL.setText(NLMessages.getString("Editor_subtype"));
						placeSubtypeL.setLayoutData(new GridData());

						final Combo placeSubtypeCombo = new Combo(compositePlace, SWT.DROP_DOWN | SWT.READ_ONLY);
						placeSubtypeCombo.setEnabled(_mayWrite);
						placeSubtypeCombo.setBackground(WHITE_COLOR);
						placeSubtypeCombo.setLayoutData(new GridData());
						((GridData) placeSubtypeCombo.getLayoutData()).horizontalAlignment = GridData.FILL;
						((GridData) placeSubtypeCombo.getLayoutData()).grabExcessHorizontalSpace = true;
						((GridData) placeSubtypeCombo.getLayoutData()).horizontalSpan = 2;
						final ComboViewer placeSubtypeComboViewer = new ComboViewer(placeSubtypeCombo);
						placeSubtypeComboViewer.setContentProvider(new MarkupContentProvider());
						placeSubtypeComboViewer.setLabelProvider(new MarkupLabelProvider());

						placeTypeComboViewer.addSelectionChangedListener(new ISelectionChangedListener()
						{
							@Override
							public void selectionChanged(final SelectionChangedEvent event)
							{
								ISelection iSelection = event.getSelection();
								Object obj = ((IStructuredSelection) iSelection).getFirstElement();
								ConfigData cd = (ConfigData) obj;
								p.setType(cd.getValue());
								placeSubtypeComboViewer.setInput(_facade.getConfigs().get(_markupProvider)
										.getChildren().get("aodl:placeName").getChildren().get(p.getType())
										.getChildren());
							}
						});

						// for (String sdt : AEConstants.PLACESCALE)
						// {
						// placeSubtypeCombo.add(sdt);
						// }
						if (p.getSubtype() != null)
						{
							ViewHelper.setComboViewerByString(placeSubtypeComboViewer, p.getSubtype(), true);
						}
						placeSubtypeComboViewer.addSelectionChangedListener(new ISelectionChangedListener()
						{
							@Override
							public void selectionChanged(final SelectionChangedEvent event)
							{
								ISelection iSelection = event.getSelection();
								Object obj = ((IStructuredSelection) iSelection).getFirstElement();
								ConfigData cd = (ConfigData) obj;
								p.setSubtype(cd.getValue());
							}
						});

						Label placeKeyL = new Label(compositePlace, SWT.NONE);
						placeKeyL.setText(NLMessages.getString("Editor_key"));
						placeKeyL.setLayoutData(new GridData());

						final Text placeKeyText = new Text(compositePlace, SWT.BORDER);
						placeKeyText.setEditable(_mayWrite);
						placeKeyText.setBackground(WHITE_COLOR);
						placeKeyText.setLayoutData(new GridData());
						((GridData) placeKeyText.getLayoutData()).horizontalAlignment = SWT.FILL;
						((GridData) placeKeyText.getLayoutData()).grabExcessHorizontalSpace = true;
						((GridData) placeKeyText.getLayoutData()).horizontalSpan = 3;
						if (p.getKey() != null)
						{
							placeKeyText.setText(p.getKey()); //$NON-NLS-1$
						}
						else
						{
							placeKeyText.setText(""); //$NON-NLS-1$
						}
						placeKeyText.addFocusListener(new FocusAdapter()
						{
							@Override
							public void focusLost(final FocusEvent e)
							{
								p.setKey(placeKeyText.getText());
							}
						});
						final Button setPlaceKey = new Button(compositePlace, SWT.PUSH);
						setPlaceKey.setText(NLMessages.getString("Editor_setKey"));
						setPlaceKey.setLayoutData(_gridData);
						setPlaceKey.setEnabled(false);
						setPlaceKey.addSelectionListener(new SelectionAdapter()
						{
							@Override
							public void widgetSelected(final SelectionEvent event)
							{
							}
						});
						setPlaceKey.setLayoutData(new GridData());
						setPlaceKey.pack();

						final Button delPlace = new Button(compositePlace, SWT.PUSH);
						delPlace.setText(NLMessages.getString("Editor_deletePlace"));
						delPlace.setToolTipText(NLMessages.getString("Editor_remove_place_tip"));
						delPlace.setImage(_imageReg.get(IconsInternal.REMOVE));
						delPlace.setEnabled(_mayWrite);
						delPlace.setLayoutData(_gridData);
						delPlace.addSelectionListener(new SelectionAdapter()
						{
							@Override
							public void widgetSelected(final SelectionEvent event)
							{
								//								System.out.println("del place " + (Integer) compositePlace //$NON-NLS-1$
								//.getData("spatialStm") + " " + (Integer) compositePlace.getData("place")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
								loadTimeSpatialDim(8, null, null, (Integer) compositePlace.getData("spatialStm"), //$NON-NLS-1$
										(Integer) compositePlace.getData("place")); //$NON-NLS-1$
								validate();
							}
						});
						delPlace.setLayoutData(new GridData());
						delPlace.pack();

						compositePlace.layout(); // compositePlace
					}
				}
				_timeGroup.layout();

				_placeGroup.layout(); // placeGroup
			}
		}
		contentCompTimePlace.layout();

		_scrollCompTimePlace.setContent(contentCompTimePlace);
		Point point = contentCompTimePlace.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
		Point mp = _mainTabFolder.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
		if (point.x > mp.x - 20)
		{
			point.x = mp.x - 20;
		}
		_scrollCompTimePlace.setMinSize(point);
		_scrollCompTimePlace.layout();
		_dimensionComposite.redraw();
		_dimensionComposite.layout();
//		_dimensionComposite.pack();
//		_mainTabFolder.redraw();
		_mainTabFolder.layout();
//		_mainTabFolder.pack();
	}

	/**
	 * Load validation.
	 * @param type the type
	 * @param src the src
	 */
	private void loadValidation(final int type, final Integer src)
	{
		if (_scrollCompVal != null)
		{
			_scrollCompVal.dispose();
		}
		_scrollCompVal = new ScrolledComposite(_sourceComposite, SWT.V_SCROLL);
		_scrollCompVal.setExpandHorizontal(true);
		_scrollCompVal.setExpandVertical(true);
		_scrollCompVal.setMinSize(SWT.DEFAULT, SWT.DEFAULT);

		_scrollCompVal.setLayout(new GridLayout());
		_scrollCompVal.setLayoutData(new GridData());
		((GridData) _scrollCompVal.getLayoutData()).heightHint = 490;
		((GridData) _scrollCompVal.getLayoutData()).widthHint = 700;

		((GridData) _scrollCompVal.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _scrollCompVal.getLayoutData()).grabExcessHorizontalSpace = true;
		_scrollCompVal.setMinHeight(1);
		_scrollCompVal.setMinWidth(1);
		_scrollCompVal.layout();

		Composite contentCompVal = new Composite(_scrollCompVal, SWT.NONE);
		contentCompVal.setLayout(new GridLayout());
		_scrollCompVal.setContent(contentCompVal);

		switch (type)
		{
			case 0:
				break; // nix, normales laden
			case 1: // neue reference einfügen
				if (_currentAspect.getValidation() == null)
				{
					_currentAspect.setValidation(new Validation());
					_currentAspect.getValidation().setValidationStms(new Vector<ValidationStm>());
					_currentAspect.getValidation().getValidationStms().add(new ValidationStm());
					_currentAspect.getValidation().getValidationStms().lastElement().setReference(new Reference());
				}
				else
				{
					_currentAspect.getValidation().getValidationStms().add(new ValidationStm());
					_currentAspect.getValidation().getValidationStms().lastElement().setReference(new Reference());

				}
				break;
			case 2: // reference löschen
				_currentAspect.getValidation().remove(src);
				break;
			default:
				break;

		}
		if (_currentAspect.getValidation() != null)
		{
			for (int i = 0; i < _currentAspect.getValidation().getValidationStms().size(); i++)
			{
				final ValidationStm vs = _currentAspect.getValidation().getValidationStms().get(i);

				Group valGroup = new Group(contentCompVal, SWT.SHADOW_IN);
				valGroup.setLayout(new GridLayout());
				((GridLayout) valGroup.getLayout()).numColumns = 1;
				((GridLayout) valGroup.getLayout()).makeColumnsEqualWidth = false;

				valGroup.setLayoutData(new GridData());
				((GridData) valGroup.getLayoutData()).horizontalAlignment = SWT.FILL;
				((GridData) valGroup.getLayoutData()).grabExcessHorizontalSpace = true;
				int l = i + 1;
				valGroup.setText(NLMessages.getString("Editor_reference") + l);

				final Composite sourceComposite = new Composite(valGroup, SWT.NONE);
				sourceComposite.setData("src", i); //$NON-NLS-1$
				sourceComposite.setLayout(new GridLayout());
				((GridLayout) sourceComposite.getLayout()).numColumns = 7;
				((GridLayout) sourceComposite.getLayout()).makeColumnsEqualWidth = false;
				sourceComposite.setLayoutData(new GridData());
				((GridData) sourceComposite.getLayoutData()).horizontalAlignment = SWT.FILL;
				((GridData) sourceComposite.getLayoutData()).grabExcessHorizontalSpace = true;

				Label sourceLabel2 = new Label(sourceComposite, SWT.NONE);
				sourceLabel2.setText(NLMessages.getString("Editor_reference"));
				sourceLabel2.setLayoutData(new GridData());

				final Text sourceText = new Text(sourceComposite, SWT.BORDER);
				sourceText.setEditable(_mayWrite);
				sourceText.setBackground(WHITE_COLOR);
				sourceText.setLayoutData(new GridData());
				((GridData) sourceText.getLayoutData()).horizontalAlignment = SWT.FILL;
				((GridData) sourceText.getLayoutData()).grabExcessHorizontalSpace = true;
				((GridData) sourceText.getLayoutData()).horizontalSpan = 3;
				final ControlDecoration decoValId = new ControlDecoration(sourceText, SWT.LEFT | SWT.TOP);
				ControlDecoration decoValIdInfo = new ControlDecoration(sourceText, SWT.LEFT | SWT.BOTTOM);
				decoValIdInfo.setDescriptionText(NLMessages.getString("Editor_proposal_cntl_all_ref"));
				decoValIdInfo.setImage(FieldDecorationRegistry.getDefault()
						.getFieldDecoration(FieldDecorationRegistry.DEC_INFORMATION).getImage());
				decoValIdInfo.setShowOnlyOnFocus(false);

				Button relateRefButton = new Button(sourceComposite, SWT.PUSH);
				relateRefButton.setText(NLMessages.getString("Editor_select_dots"));
				relateRefButton.setImage(_imageReg.get(IconsInternal.SEARCH));
				relateRefButton.setEnabled(_mayWrite);
				relateRefButton.setLayoutData(new GridData());
				relateRefButton.setToolTipText(NLMessages.getString("Editor_linkWithSource")); //$NON-NLS-1$

//				relateRefButton.pack();

				Button newRefButton = new Button(sourceComposite, SWT.PUSH);
				newRefButton.setText(NLMessages.getString("Editor_create_new_ref"));
				newRefButton.setToolTipText(NLMessages.getString("Editor_create_new_ref_tip"));
				newRefButton.setImage(_imageReg.get(IconsInternal.REFERENCE_NEW));
				newRefButton.setEnabled(_mayWrite);
				newRefButton.setLayoutData(new GridData());

//				newRefButton.pack();

				Button editRefButton = new Button(sourceComposite, SWT.PUSH);
				editRefButton.setImage(_imageReg.get(IconsInternal.REFERENCE_EDIT));
				editRefButton.setEnabled(_mayWrite);
				editRefButton.setLayoutData(new GridData());
				editRefButton.setToolTipText(NLMessages.getString("Editor_edit_reference_tooltip")); //$NON-NLS-1$

//				editRefButton.pack();

				if (vs.getReference().getSourceId() != null) //$NON-NLS-1$
				{
					decoValId.setImage(null);
					PdrObject o = _facade.getReference(vs.getReference().getSourceId());
					if (o != null)
					{
						sourceText.setText(o.getDisplayNameWithID());
					}
					else
					{
						sourceText.setText(vs.getReference().getSourceId().toString());
						decoValId.setImage(FieldDecorationRegistry.getDefault()
								.getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).getImage());
						decoValId.setDescriptionText(NLMessages.getString("Editor_missing_object_no_relation"));
					}
				}
				else
				{
					sourceText.setText("");
					decoValId.setImage(FieldDecorationRegistry.getDefault()
							.getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).getImage());
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
							keyStroke = KeyStroke.getInstance("Ctrl+Space");
							ContentProposalAdapter adapter = new ContentProposalAdapter(sourceText,
									new TextContentAdapter(), new FacetContentProposalProvider(_facade
											.getAllReferenceFacets()), keyStroke, autoActivationCharacters);
							adapter.setLabelProvider(new AutoCompleteNameLabelProvider());
							adapter.addContentProposalListener(new IContentProposalListener()
							{

								@Override
								public void proposalAccepted(final IContentProposal proposal)
								{
									sourceText.setText(proposal.getContent());
									if (((Facet) proposal).getKey() != null)
									{
										vs.getReference().setSourceId(new PdrId(((Facet) proposal).getKey()));
										decoValId.setImage(null);
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
						if (vs.getReference().getSourceId() != null
								&& _facade.getReference(vs.getReference().getSourceId()) != null)
						{
							decoValId.setDescriptionText("");
							decoValId.setImage(null);
						}
						else
						{
							vs.getReference().setSourceId(null);
							decoValId.setImage(FieldDecorationRegistry.getDefault()
									.getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).getImage());
							decoValId.setDescriptionText(NLMessages.getString("Editor_missing_object_no_relation"));
						}
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
							PdrObject o = _facade.getReference(new PdrId(sourceText.getText()));
							if (o != null)
							{
								decoValId.setImage(null);
								vs.getReference().setSourceId(new PdrId(sourceText.getText()));
								sourceText.setText(o.getDisplayNameWithID());
							}
						}
						else if (sourceText.getText().trim().length() == 0)
						{
							vs.getReference().setSourceId(null);
						}

						validate();
					}
				});

				relateRefButton.addSelectionListener(new SelectionAdapter()
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
							vs.getReference().setSourceId(_facade.getRequestedId());
							if (vs.getReference().isValidId())
							{
								decoValId.setImage(null);
								PdrObject o = _facade.getReference(vs.getReference().getSourceId());
								if (o != null)
								{
									sourceText.setText(o.getDisplayNameWithID()); //$NON-NLS-1$
								}
							}
						}
						else
						{
							if (!vs.getReference().isValidId())
							{
								decoValId.setImage(FieldDecorationRegistry.getDefault()
									.getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).getImage());
								sourceText.setText("");
							}
						}
						validate();
					}
				});
				newRefButton.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent event)
					{

						IHandlerService handlerService = (IHandlerService) PlatformUI.getWorkbench().getService(
								IHandlerService.class);
						try
						{
							handlerService.executeCommand("org.bbaw.pdr.ae.view.main.commands.NewReference", null); //$NON-NLS-1$
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
							vs.getReference().setSourceId(_facade.getCurrentReference().getPdrId());
							sourceText.setText(_facade.getReference(vs.getReference().getSourceId())
									.getDisplayNameWithID()); //$NON-NLS-1$
						}
						if (vs.getReference().isValidId())
						{
							decoValId.setImage(null);
						}
						else
						{
							decoValId.setImage(FieldDecorationRegistry.getDefault()
									.getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).getImage());
						}
						validate();

					}
				});

				editRefButton.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent event)
					{
						if (vs.getReference() != null && vs.getReference().getSourceId() != null)
						{
							ReferenceMods ref = _facade.getReference(vs.getReference().getSourceId());
							if (ref != null)
							{
								_facade.setCurrentReference(ref);

								IHandlerService handlerService = (IHandlerService) PlatformUI.getWorkbench()
										.getService(IHandlerService.class);
								try
								{
									handlerService.executeCommand(
											"org.bbaw.pdr.ae.view.main.commands.OpenSourceEditorDialog", null); //$NON-NLS-1$
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
						}

					}
				});

				Label intenalLabel = new Label(sourceComposite, SWT.NONE);
				intenalLabel.setText(NLMessages.getString("Editor_internal"));
				intenalLabel.setLayoutData(new GridData());

				final Text internalText = new Text(sourceComposite, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
				internalText.setEditable(_mayWrite);
				internalText.setBackground(WHITE_COLOR);
				internalText.setLayoutData(new GridData());
				((GridData) internalText.getLayoutData()).horizontalAlignment = SWT.FILL;
				((GridData) internalText.getLayoutData()).grabExcessHorizontalSpace = true;
				((GridData) internalText.getLayoutData()).horizontalSpan = 6;

				if (vs.getReference().getInternal() != null) //$NON-NLS-1$
				{
					internalText.setText(vs.getReference().getInternal());
				}
				internalText.addFocusListener(new FocusListener()
				{
					@Override
					public void focusLost(final FocusEvent e)
					{
						vs.getReference().setInternal(internalText.getText());
					}

					@Override
					public void focusGained(FocusEvent event) {
						String[] vals = new String[]
								{"test", "test2"};
								try
								{
									vals = _mainSearcher.getFacets("validation", "internal", null, null, //$NON-NLS-1$
											null);
								}
								catch (Exception e1)
								{

									e1.printStackTrace();
								}
								new AutoCompleteField(internalText, new TextContentAdapter(), vals);

						
					}

				});

				

				// Label blancL = new Label(sourceComposite, SWT.None);
				//		         blancL.setText(""); //$NON-NLS-1$
				// blancL.setLayoutData(new GridData());

				Label refQualLabel = new Label(sourceComposite, SWT.NONE);
				refQualLabel.setText(NLMessages.getString("Editor_quality")); //$NON-NLS-1$
				refQualLabel.setLayoutData(new GridData());
				final Button[] radios = new Button[AEConstants.REFRENCEQUALITIES.length];
				final ControlDecoration decoValQual = new ControlDecoration(refQualLabel, SWT.LEFT | SWT.TOP);
				SelectionListener refListener = new SelectionAdapter()
				{
					@Override
					public void widgetDefaultSelected(final SelectionEvent e)
					{
					}

					@Override
					public void widgetSelected(final SelectionEvent e)
					{
						final String qual = (String) ((Button) e.getSource()).getData("text");
						vs.getReference().setQuality(qual); //$NON-NLS-1$
						// System.out.println("quality set to " + qual);
						if (vs.getReference().isValidQuality())
						{
							decoValQual.setImage(null);
						}
						else
						{
							decoValQual.setImage(FieldDecorationRegistry.getDefault()
									.getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).getImage());
						}
						validate();
					}
				};
				for (int j = 0; j < AEConstants.REFRENCEQUALITIES.length; j++)
				{
					radios[j] = new Button(sourceComposite, SWT.RADIO);
					radios[j].setText(NLMessages.getString("Editor_" + AEConstants.REFRENCEQUALITIES[j]));
					radios[j].setData("text", AEConstants.REFRENCEQUALITIES[j]);

					radios[j].addSelectionListener(refListener);
					radios[j].setEnabled(_mayWrite);
					radios[j].setLayoutData(new GridData());
				}
				if (vs.getReference().getQuality() != null)
				{
					ViewHelper.setRadioByString(radios, vs.getReference().getQuality());
				}
				else
				{
					decoValId.setImage(FieldDecorationRegistry.getDefault()
							.getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).getImage());
				}

				

				Label refauthority = new Label(sourceComposite, SWT.NONE);
				if (vs.getAuthority() != null)
				{
					User u = null;
					try
					{
						u = _facade.getUserManager().getUserById(vs.getAuthority().toString());
					}
					catch (Exception e1)
					{
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					if (u != null)
					{
						refauthority.setText("User: " + u.getDisplayName());
					}
					else
					{
						refauthority.setText("User: " + vs.getAuthority().toString());
					}
				}
				else
				{
					refauthority.setText("User: " + _facade.getCurrentUser().getPdrId().toString());
					vs.setAuthority(_facade.getCurrentUser().getPdrId());
				}
				refauthority.setLayoutData(new GridData());
				((GridData) refauthority.getLayoutData()).horizontalSpan = 3;

				Label refCitLabel = new Label(sourceComposite, SWT.NONE);
				refCitLabel.setText(NLMessages.getString("Editor_interpretation"));
				refCitLabel.setLayoutData(new GridData());
				((GridData) refCitLabel.getLayoutData()).horizontalSpan = 3;
				
				final Text refCitation = new Text(sourceComposite, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
				refCitation.setEditable(_mayWrite);
				refCitation.setBackground(WHITE_COLOR);
				refCitation.setLayoutData(new GridData());
				((GridData) refCitation.getLayoutData()).horizontalAlignment = SWT.FILL;
				((GridData) refCitation.getLayoutData()).grabExcessHorizontalSpace = true;
				((GridData) refCitation.getLayoutData()).heightHint = 36;
				((GridData) refCitation.getLayoutData()).horizontalSpan = 6;

				if (vs.getInterpretation() != null)
				{
					refCitation.setText(vs.getInterpretation());
				}

				refCitation.addFocusListener(new FocusAdapter()
				{
					@Override
					public void focusLost(final FocusEvent e)
					{
						vs.setInterpretation(refCitation.getText());
					}
				});

				final Button delReference = new Button(sourceComposite, SWT.PUSH);
				delReference.setText(NLMessages.getString("Editor_delete"));
				delReference.setToolTipText(NLMessages.getString("Editor_remove_valStm_tip"));
				delReference.setImage(_imageReg.get(IconsInternal.REFERENCE_REMOVE));
				delReference.setEnabled(_mayWrite);
				delReference.setEnabled(_currentAspect.getValidation().getValidationStms().size() > 1);

				delReference.setLayoutData(_gridData);
				delReference.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent event)
					{
						loadValidation(2, ((Integer) sourceComposite.getData("src"))); //$NON-NLS-1$
						validate();

					}
				});
				delReference.setLayoutData(new GridData());
//				delReference.pack();
//				sourceComposite.pack();
//				valGroup.pack();

			}

		}
		contentCompVal.layout();

		_scrollCompVal.setContent(contentCompVal);
		Point point = contentCompVal.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
		Point mp = _mainTabFolder.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
		if (point.x > mp.x - 20)
		{
			point.x = mp.x - 20;
		}
		_scrollCompVal.setMinSize(point);
		_scrollCompVal.layout();
		_sourceComposite.redraw();
		_sourceComposite.layout();
//		_sourceComposite.pack();
//		_mainTabFolder.redraw();
		_mainTabFolder.layout();
//		_mainTabFolder.pack();
	}

	/**
	 * laods values of current aspect throws exception because it commands the
	 * the XMLProcessor to parse and load the xml-text of aspect. TODO add
	 * missing fields to be loaded TODO alter XMLProcessor so that exceptions
	 * are not thrown in dialog class.
	 * @throws SAXException sax exception
	 * @throws IOException inout exception
	 * @throws ParserConfigurationException sax parser exception
	 */
	private void loadValues() throws SAXException, IOException, ParserConfigurationException
	{
		Revision revision = new Revision();
		revision.setRevisor(new String(_facade.getCurrentUser().getDisplayName()));
		revision.setTimeStamp(_facade.getCurrentDate());
		revision.setAuthority(_facade.getCurrentUser().getPdrId().clone());
		if (_currentAspect.isNew())
		{
			revision.setRef(0);
			Record record = new Record();
			record.getRevisions().add(revision);
			_currentAspect.setRecord(record);
		}

		_mayWrite = new UserRichtsChecker().mayWrite(_currentAspect);
		if (!_mayWrite)
		{
			setMessage("You may open this aspect but not modify it.");
		}
		// addFurtherClassifier.setEnabled(_mayWrite);
		_addBelongsToButton.setEnabled(_mayWrite);
		_addRelationsButton.setEnabled(_mayWrite);
		_addReferencesButton.setEnabled(_mayWrite);
		_addSpatialStmButton.setEnabled(_mayWrite);
		_addTimeStmButton.setEnabled(_mayWrite);
		_buttonTaggingSet.setEnabled(_mayWrite);
		_buttonTaggingInsertSet.setEnabled(_mayWrite);
		_buttonTaggingInsertMarkup.setEnabled(_mayWrite);

		_comboTaggingElement.setEnabled(_mayWrite);
		_comboTaggingType.setEnabled(_mayWrite);
		_comboTaggingSubtype.setEnabled(_mayWrite);
		_comboTaggingRole.setEnabled(_mayWrite);

		// textTaggingAna.setEditable(_mayWrite);
		_findAna.setEnabled(_mayWrite);
		_textTaggingKey.setEditable(_mayWrite);
		_findKey.setEnabled(_mayWrite);
		// contentText.setEditable(_mayWrite);

		_buttonTDate.setEnabled(_mayWrite);
		_buttonTDateRange.setEnabled(_mayWrite);
		_comboTDateAfter.setEnabled(_mayWrite);
		_comboTDateBefore.setEnabled(_mayWrite);
		_comboTDateDay.setEnabled(_mayWrite);
		_comboTDateMonth.setEnabled(_mayWrite);
		_comboTDatePointOT.setEnabled(_mayWrite);
		_comboTDateRangeFromDay.setEnabled(_mayWrite);
		_comboTDateRangeFromMonth.setEnabled(_mayWrite);
		_comboTDateRangeToDay.setEnabled(_mayWrite);
		_comboTDateRangeToMonth.setEnabled(_mayWrite);
		_spinnerTDateRangeFromYear.setEnabled(_mayWrite);
		_spinnerTDateRangeToYear.setEnabled(_mayWrite);
		_spinnerTDateYear.setEnabled(_mayWrite);

		_markupProvider = Platform
				.getPreferencesService()
				.getString(CommonActivator.PLUGIN_ID,
						"PRIMARY_TAGGING_PROVIDER", AEConstants.TAGGING_LIST_PROVIDER, null).toUpperCase(); //$NON-NLS-1$
		_relationProvider = Platform
				.getPreferencesService()
				.getString(CommonActivator.PLUGIN_ID,
						"PRIMARY_RELATION_PROVIDER", AEConstants.RELATION_CLASSIFICATION_PROVIDER, null).toUpperCase(); //$NON-NLS-1$
		_semanticProvider = Platform
				.getPreferencesService()
				.getString(CommonActivator.PLUGIN_ID,
						"PRIMARY_SEMANTIC_PROVIDER", AEConstants.CLASSIFICATION_AUTHORITY, null).toUpperCase(); //$NON-NLS-1$
		String standard = "PDR";
		if (!_facade.getConfigs().containsKey(standard))
		{
			for (String s : _facade.getConfigs().keySet())
			{
				standard = s;
				break;
			}
		}
		if (!_facade.getConfigs().containsKey(_markupProvider))
		{
			_markupProvider = standard;
		}
		
		if (!_facade.getConfigs().containsKey(_relationProvider))
		{
			_relationProvider = standard;
		}
		
		if (!_facade.getConfigs().containsKey(_semanticProvider))
		{
			_semanticProvider = standard;
		}
		_preferedYear = PRESELECTED_YEAR;
		_clasView = false;
		_sourceView = false;
		_relView = false;
		_timePlaceView = false;
		_frontViewed = false;
		_sourceViewed = false;
		_relViewed = false;
		_timePlaceViewed = false;

		AEConstants.getCurrentLocale().getLanguage();
		if (_currentAspect != null)
		{
			// Front
			_pdrID.setText(_currentAspect.getPdrId().toString());

			if ((_currentAspect.getRecord() != null) && !_currentAspect.getRecord().getRevisions().isEmpty())
			{
				if (_currentAspect.getRecord().getRevisions().get(0).getRevisor() != null)
				{
					_creatorNameText.setText(_currentAspect.getRecord().getRevisions().get(0).getRevisor());
				}
				else
				{
					_creatorNameText.setText(_facade.getObjectDisplayName(_currentAspect.getRecord().getRevisions()
							.get(0).getAuthority()));
				}
				_creationTimeText.setText(_adminDateFormat.format(_currentAspect.getRecord().getRevisions().get(0)
						.getTimeStamp()));
				// creatorNameText.pack();
				// creationTimeText.pack();

				if (_currentAspect.getRecord().getRevisions().size() > 1)
				{
					if (_currentAspect.getRecord().getRevisions().lastElement().getAuthority() != null)
					{
						_revisorName.setText(_facade.getObjectDisplayName(_currentAspect.getRecord().getRevisions()
								.lastElement().getAuthority()));
					}
					else
					{
						_revisorName.setText("Revisor not found");
					}
					_revisionTimeText.setText(_adminDateFormat.format(_currentAspect.getRecord().getRevisions()
							.lastElement().getTimeStamp()));
				}
			}
			else
			{
				_creatorNameText.setText(_facade.getCurrentUser().getDisplayName());
				_creationTimeText.setText(_adminDateFormat.format(_facade.getCurrentDate()));
				_currentAspect.setNew(true);
			}
			if (_currentAspect.isNew())
			{
				setMessage(_message, IMessageProvider.INFORMATION); //$NON-NLS-1$
			}

			_markupEditor.setAspect(_currentAspect);

			_markupEditor.setEditable(_mayWrite);

			if (_currentAspect.getRelationDim() != null)
			{
				loadRelationDim(0, null, null);
				if (_currentAspect.getRelationDim().getRelationStms().firstElement().getSubject()
						.equals(_currentAspect.getPdrId()))
				{
					PdrId id = _currentAspect.getRelationDim().getRelationStms().firstElement().getRelations()
							.firstElement().getObject();
					if (id != null)
					{
						_textTaggingAna.setData("id", id.toString());
						PdrObject o = _facade.getPdrObject(id);
						if (o != null)
						{
							_textTaggingAna.setText(o.getDisplayNameWithID());
						}
					}
				}
				else
				{

					PdrId id = _currentAspect.getRelationDim().getRelationStms().firstElement().getSubject();
					if (id != null)
					{
						_textTaggingAna.setData("id", id.toString());
						PdrObject obj = _facade.getPdrObject(id);
						if (obj != null)
						{
							_textTaggingAna.setText(obj.getDisplayNameWithID());
						}
					}
				}
			}
			else
			{
				loadRelationDim(5, null, null);
			}
			// Sources
			if (_currentAspect.getValidation() != null)
			{
				loadValidation(0, null);
			}
			else
			{
				loadValidation(1, null);
			}

			// TODO dynamisieren für mehrere Labels!!!!!!!!!!!
			// Classification
			if (_currentAspect.getSemanticDim() != null)
			{
				loadClassification(0, null);
			}
			else
			{
				loadClassification(1, null);
			}



		}

//		((Composite)_markupEditor.getControl()).pack();
//
//		((Composite)_markupEditor.getControl()).layout();
//		_editorComposite.pack();
//		_editorComposite.layout();
		System.out.println("end of load values - layout maintabfolder");
//		_mainTabFolder.pack();
//		_mainTabFolder.layout();
//		_mainTabFolder.update();
		_frontComposite.layout();
	}

	/**
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	@Override
	protected final void okPressed()
	{
		saveInput();
		super.okPressed();
	}

	/**
	 * Process relation.
	 * @param id the id
	 */
	private void processRelation(final String id)
	{
		if (id != null && id.startsWith("pdr") && id.length() == 23)
		{
			boolean contains = false;
			if (_currentAspect.getRelationDim() == null)
			{
				_currentAspect.setRelationDim(new RelationDim());
			}

			for (RelationStm rst : _currentAspect.getRelationDim().getRelationStms())
			{
				if (rst.getRelations() != null && !rst.getRelations().isEmpty()
						&& rst.getRelations().firstElement() != null
						&& rst.getRelations().firstElement().getObject() != null
						&& rst.getRelations().firstElement().getObject().toString().equals(id))
				{
					contains = true;
				}
			}
			if (!contains)
			{
				RelationStm rs = new RelationStm();
				rs.setSubject(_currentAspect.getPdrId());
				Relation r = new Relation();
				r.setRelation("aspect_of"); //$NON-NLS-1$
				r.setProvider("PDR"); //$NON-NLS-1$
				r.setObject(new PdrId(id));
				rs.setRelations(new Vector<Relation>(1));
				rs.getRelations().add(r);
				_currentAspect.getRelationDim().getRelationStms().add(rs);
			}
		}
	}

	@Override
	public void removeSelectionChangedListener(ISelectionChangedListener listener)
	{
		// TODO Auto-generated method stub

	}

	// TODO Speicherfunktion einrichten.
	/**
	 * Save input.
	 */
	private void saveInput()
	{
		if (_currentPerson != null && _currentPerson.isNew())
		{
			// hier wird injestet
			try
			{
				_facade.savePerson(_currentPerson);
			}
			catch (Exception e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		Revision revision = new Revision();
		revision.setRevisor(new String(_facade.getCurrentUser().getDisplayName()));
		revision.setTimeStamp(_facade.getCurrentDate());
		revision.setAuthority(_facade.getCurrentUser().getPdrId().clone());
		boolean isModifiedOrNew = false;
		try
		{
			isModifiedOrNew = _idService.isModifiedOrNewObject(_currentAspect.getPdrId());
		}
		catch (Exception e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (!_currentAspect.isNew() && !isModifiedOrNew)
		{
			revision.setRef(_currentAspect.getRecord().getRevisions().size());
			_currentAspect.getRecord().getRevisions().add(revision);
			_currentAspect.setDirty(true);
		}
		else
		{
			_currentAspect.getRecord().getRevisions().lastElement().setTimeStamp(_facade.getCurrentDate());
			_currentAspect.setDirty(true);
		}

		if (_currentAspect.getTimeDim() == null)
		{
			_currentAspect.setTimeDim(new TimeDim());
			_currentAspect.getTimeDim().setTimeStms(new Vector<TimeStm>());

		}
		if (_currentAspect.getTimeDim().getTimeStms().size() == 0)
		{
			TimeStm st = new TimeStm();
			st.setType("undefined"); //$NON-NLS-1$
			_currentAspect.getTimeDim().getTimeStms().add(st);
		}
		if (_currentAspect.getSpatialDim() == null)
		{
			_currentAspect.setSpatialDim(new SpatialDim());
			_currentAspect.getSpatialDim().setSpatialStms(new Vector<SpatialStm>());

		}
		if (_currentAspect.getSpatialDim().getSpatialStms().size() == 0)
		{
			SpatialStm spS = new SpatialStm();
			spS.setType("undefined"); //$NON-NLS-1$
			_currentAspect.getSpatialDim().getSpatialStms().add(spS);
		}
		_markupEditor.saveChanges();
		try
		{
			_facade.saveAspect(_currentAspect);
		}
		catch (Exception e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// hier wird die zuvor geclonte person für die person mit der gleichen
		// id im allperson
		// vector eingetauscht und außerdem als currentPerson gesetzt.

	}



	/**
	 * Sets the combos by quick select.
	 * @param cd the new combos by quick select
	 */
	private void setCombosByQuickSelect(final ConfigData cd)
	{
		if (cd instanceof DataType)
		{
			DataType dt = (DataType) cd;
			_comboTaggingElementViewer.setSelection(new StructuredSelection(dt));
		}
		else if (cd instanceof ConfigItem)
		{
			ConfigItem ci = (ConfigItem) cd;
			if (ci.getPos().equals("type"))
			{
				DataType dt = (DataType) ci.getParent();
				_comboTaggingElementViewer.setSelection(new StructuredSelection(dt));
				_comboTaggingTypeViewer.setSelection(new StructuredSelection(ci));
			}
			else if (ci.getPos().equals("subtype"))
			{
				ConfigItem parent = (ConfigItem) ci.getParent();
				DataType dt = (DataType) parent.getParent();
				_comboTaggingElementViewer.setSelection(new StructuredSelection(dt));
				_comboTaggingTypeViewer.setSelection(new StructuredSelection(parent));
				_comboTaggingSubtypeViewer.setSelection(new StructuredSelection(ci));

			}
			else if (ci.getPos().equals("role"))
			{
				ConfigItem parent = (ConfigItem) ci.getParent();
				ConfigItem grandParent = (ConfigItem) parent.getParent();
				DataType dt = (DataType) grandParent.getParent();
				_comboTaggingElementViewer.setSelection(new StructuredSelection(dt));
				_comboTaggingTypeViewer.setSelection(new StructuredSelection(grandParent));
				_comboTaggingSubtypeViewer.setSelection(new StructuredSelection(parent));
				_comboTaggingRoleViewer.setSelection(new StructuredSelection(ci));
			}
		}
	}

	// /**
	// * Sets the combo viewer by string.
	// * @param cv the cv
	// * @param s the s
	// */
	// private void setComboViewerByString(ComboViewer cv, String s)
	// {
	// boolean notincl = false;
	// if (cv.getInput() == null || !(cv.getInput() instanceof HashMap<?, ?>))
	// {
	// cv.setInput(new HashMap<String, ConfigData>());
	// }
	// if (cv.getInput() instanceof HashMap<?, ?>)
	// {
	// @SuppressWarnings("unchecked")
	// HashMap<String, ConfigData> inputs = (HashMap<String, ConfigData>)
	// cv.getInput();
	// if (!inputs.containsKey(s))
	// {
	// notincl = true;
	// ConfigItem ci = new ConfigItem();
	// ci.setValue(s);
	// ci.setLabel(s);
	// inputs.put(s, ci);
	// cv.setInput(inputs);
	// cv.refresh();
	// }
	// if (inputs.containsKey(s))
	// {
	// // System.out.println("contains key s " + s);
	// for (String key : inputs.keySet())
	// {
	// if (key.equals(s))
	// {
	// ConfigData cd = inputs.get(key);
	// if (cd instanceof ConfigItem && ((ConfigItem) cd).isIgnore())
	// {
	// ((ConfigItem) cd).setReadAlthoughIgnored(true);
	// cv.setInput(inputs);
	// }
	// StructuredSelection sel = new StructuredSelection(cd);
	// cv.setSelection(sel, true);
	// break;
	// }
	// }
	// }
	// if (notincl)
	// {
	// cv.getCombo().select(0);
	// }
	// }
	//
	// }



	// //////////////////////// Observer ///////////////////////////////////

	/**
	 * Sets the markup.
	 */
	protected final void setMarkup()
	{
		if (_stackUndo.size() == UNDO_STACKSIZE) 
		{
			_stackUndo.removeElementAt(0);
		}
		_markupEditor.saveChanges();
		_stackUndo.push(new UndoInformation(_currentAspect.getNotification(), _currentAspect.getRangeList()));
		
		if (!_protectRedoStack)
		{
			_stackRedo.clear();
		}
		if (_undoAction != null)
		{
			_undoAction.setEnabled(_stackUndo.size() > 0);
		}
		if (_redoAction != null)
		{
			_redoAction.setEnabled(_stackRedo.size() > 0);
		}
		
		String name = _eListName;
		String type = _tListName;
		String subtype = _sListName;
		String role = _rListName;
		String ana = (String) _textTaggingAna.getData("id");
		String key = (String) _textTaggingKey.getData("id");
		PdrDate when;
		PdrDate from;
		PdrDate to;

		if (ana != null)
		{
			processRelation(ana);
		}

		// FIXME testselection!!!???
		if (name.length() > 0 && type.length() > 0)
		{
			TaggingRange tr = new TaggingRange(name, type, subtype, role, ana, key);
			if (_pointOfTime)
			{
				when = new PdrDate(_spinnerTDateYear.getSelection(), _comboTDateMonth.getSelectionIndex(),
						_comboTDateDay.getSelectionIndex());
				ISelection sel = _comboViewerTDatePointOT.getSelection();
				Object obj = ((IStructuredSelection) sel).getFirstElement();
				String tDatePointOfTime = (String) obj;

				if (tDatePointOfTime.equals("when")) //$NON-NLS-1$
				{
					tr.setWhen(when);
				}
				else if (tDatePointOfTime.equals("notBefore")) //$NON-NLS-1$
				{
					tr.setNotBefore(when);
				}
				else
				{
					tr.setNotAfter(when);
				}
			}
			else if (_rangeOfTime)
			{
				from = new PdrDate(_spinnerTDateRangeFromYear.getSelection(),
						_comboTDateRangeFromMonth.getSelectionIndex(), _comboTDateRangeFromDay.getSelectionIndex());
				to = new PdrDate(_spinnerTDateRangeToYear.getSelection(), _comboTDateRangeToMonth.getSelectionIndex(),
						_comboTDateRangeToDay.getSelectionIndex());
				ISelection sel = _comboViewerTDateBefore.getSelection();
				Object obj = ((IStructuredSelection) sel).getFirstElement();
				String tDatePointBefore = (String) obj;
				sel = _comboViewerTDateAfter.getSelection();
				obj = ((IStructuredSelection) sel).getFirstElement();
				String tDatePointAfter = (String) obj;
				if (tDatePointBefore.equals("from")) //$NON-NLS-1$
				{
					tr.setFrom(from);
				}
				else
				{
					tr.setNotBefore(from);
				}

				if (tDatePointAfter.equals("to")) //$NON-NLS-1$
				{
					tr.setTo(to);
				}
				else
				{
					tr.setNotAfter(to);
				}

			}
			tr.setTextValue(_markupEditor.getSelectionText());
			_currentAspect.getRangeList().add(tr);
			if (_rangeOfTime || _pointOfTime)
			{
				_taggedDateCache.add(tr.clone());
			}
			if (name.equals("placeName")) //$NON-NLS-1$
			{
				_taggedPlaceCache.add(tr.clone());
			}
			insertTimePlace();

			Collections.sort(_currentAspect.getRangeList());
			_markupEditor.setMarkup(tr);


		}
	}

	@Override
	public void setSelection(ISelection selection)
	{
		// TODO Auto-generated method stub

	}


	/**
	 * Validate.
	 */
	private void validate()
	{
		boolean valid = true;
		if (_markupEditor.isValid() && (_currentAspect.isValidNotification() || _markupEditor.isValid())
				&& _currentAspect.getSemanticDim() != null && _currentAspect.getSemanticDim().isValid())
		{
			_frontTabItem.setImage(_imageReg.get(IconsInternal.MARKUP));
		}
		else
		{
			if (_frontViewed)
			{
				_frontTabItem.setImage(_imageReg.get(IconsInternal.MARKUP_ERROR));
			}
			else
			{
				_frontTabItem.setImage(_imageReg.get(IconsInternal.MARKUP_QUESTION));
			}
			valid = false;
		}

		if (_currentAspect.getValidation() != null && _currentAspect.getValidation().isValid())
		{
			_sourceTabItem.setImage(_imageReg.get(IconsInternal.REFERENCE));
		}
		else
		{
			if (_sourceViewed)
			{
				_sourceTabItem.setImage(_imageReg.get(IconsInternal.REFERENCE_ERROR));
			}
			else
			{
				_sourceTabItem.setImage(_imageReg.get(IconsInternal.REFERENCE_QUESTION));
			}
			valid = false;
		}
		if (_currentAspect.getRelationDim() != null && _currentAspect.getRelationDim().isValid())
		{
			_relationTabItem.setImage(_imageReg.get(IconsInternal.RELATIONSTATEMENTS));
		}
		else
		{
			if (_relViewed)
			{
				_relationTabItem.setImage(_imageReg.get(IconsInternal.RELATIONSTATEMENTS_ERROR));
			}
			else
			{
				_relationTabItem.setImage(_imageReg.get(IconsInternal.RELATIONSTATEMENTS_QUESTION));
			}
			valid = false;
		}
		if (_currentAspect.getTimeDim() != null && _currentAspect.getTimeDim().isValid()
				&& _currentAspect.getSpatialDim() != null && _currentAspect.getSpatialDim().isValid())
		{
			_dimensionTabItem.setImage(_imageReg.get(IconsInternal.TIMEANDPLACE));
		}
		else
		{
			if (_timePlaceViewed)
			{
				_dimensionTabItem.setImage(_imageReg.get(IconsInternal.TIMEANDPLACE_ERROR));
			}
			else
			{
				_dimensionTabItem.setImage(_imageReg.get(IconsInternal.TIMEANDPLACE_QUESTION));
			}
			valid = false;
		}
		if (_saveButton != null)
		{
			_saveButton.setEnabled(valid && _mayWrite);
		}

	}

}
