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
package org.bbaw.pdr.ae.view.main.views;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import org.bbaw.pdr.ae.common.AEConstants;
import org.bbaw.pdr.ae.common.AEPluginIDs;
import org.bbaw.pdr.ae.common.CommonActivator;
import org.bbaw.pdr.ae.common.NLMessages;
import org.bbaw.pdr.ae.common.icons.IconsInternal;
import org.bbaw.pdr.ae.config.model.ConfigItem;
import org.bbaw.pdr.ae.control.core.PDRConfigProvider;
import org.bbaw.pdr.ae.control.facade.Facade;
import org.bbaw.pdr.ae.control.interfaces.AMainSearcher;
import org.bbaw.pdr.ae.metamodel.IAEPresentable;
import org.bbaw.pdr.ae.metamodel.PdrId;
import org.bbaw.pdr.ae.model.Aspect;
import org.bbaw.pdr.ae.model.BasicPersonData;
import org.bbaw.pdr.ae.model.PdrObject;
import org.bbaw.pdr.ae.model.Person;
import org.bbaw.pdr.ae.model.ReferenceMods;
import org.bbaw.pdr.ae.model.ReferenceModsTemplate;
import org.bbaw.pdr.ae.model.search.Criteria;
import org.bbaw.pdr.ae.model.search.PdrQuery;
import org.bbaw.pdr.ae.model.view.TreeNode;
import org.bbaw.pdr.ae.view.control.ViewHelper;
import org.bbaw.pdr.ae.view.control.filters.OnlyAspectDivergentMarkup;
import org.bbaw.pdr.ae.view.control.filters.OnlyIncorrectPDRObjectsFilter;
import org.bbaw.pdr.ae.view.control.filters.PersonConcurrenceFilter;
import org.bbaw.pdr.ae.view.control.filters.PersonIdentifierFilter;
import org.bbaw.pdr.ae.view.control.filters.PersonWithoutICCUFilter;
import org.bbaw.pdr.ae.view.control.filters.PersonWithoutLCCNFilter;
import org.bbaw.pdr.ae.view.control.filters.PersonWithoutPNDFilter;
import org.bbaw.pdr.ae.view.control.filters.PersonWithoutVIAFFilter;
import org.bbaw.pdr.ae.view.control.filters.TreeNodeNewFilter;
import org.bbaw.pdr.ae.view.control.filters.TreeNodeUpdatedFilter;
import org.bbaw.pdr.ae.view.control.provider.AEConfigPresentableContentProvider;
import org.bbaw.pdr.ae.view.control.provider.AEConfigPresentableLabelProvider;
import org.bbaw.pdr.ae.view.control.provider.TreeContentProvider;
import org.bbaw.pdr.ae.view.control.provider.TreeLabelProvider;
import org.bbaw.pdr.ae.view.main.internal.Activator;
import org.bbaw.pdr.ae.view.main.internal.TreeSearchHelper;
import org.bbaw.pdr.ae.view.main.internal.TreeSelectionSourceProvider;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IParameter;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.Parameterization;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.core.commands.State;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabFolder2Adapter;
import org.eclipse.swt.custom.CTabFolderEvent;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISizeProvider;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.progress.UIJob;
import org.eclipse.ui.services.ISourceProviderService;

/**
 * Class creates tree view on left side of GUI view.
 * @author cplutte
 */
public class Treeview extends ViewPart implements ISelectionListener, ISelectionProvider, Observer, ISizeProvider
{

	/**
	 * Id des Treeviews.
	 */
	public static final String ID = "org.bbaw.pdr.ae.view.main.views.Treeview"; //$NON-NLS-1$

	/** The sash form main. */
	private SashForm _sashFormMain;

	/** Treeviewer. */
	private TreeViewer _treeViewer;
	/** tree viewer of all persons. */
	private TreeViewer _allPersonsTreeViewer;
	/** tree viewer of all references. */
	private TreeViewer _allReferencesTreeViewer;

	/** The _tree search helper. */
	private TreeSearchHelper _treeSearchHelper = new TreeSearchHelper();

	/** The _combo tree viewer. */
	private ComboViewer _comboTreeViewer;

	/** The _combo semantic viewer. */
	private ComboViewer _comboSemanticViewer;

	/** The treenode updated filter. */
	private ViewerFilter _treenodeUpdatedFilter = new TreeNodeUpdatedFilter();

	/** The treenode new filter. */
	private ViewerFilter _treenodeNewFilter = new TreeNodeNewFilter();

	/** The only incorrect pdr objects. */
	private ViewerFilter _onlyIncorrectPDRObjects = new OnlyIncorrectPDRObjectsFilter();

	/** The only i aspects divergent markup. */
	private ViewerFilter _onlyIAspectsDivergentMarkup = new OnlyAspectDivergentMarkup();

	/** The person identifier filter. */
	private ViewerFilter _personIdentifierFilter = new PersonIdentifierFilter();

	/** The person concurrence filter. */
	private ViewerFilter _personConcurrenceFilter = new PersonConcurrenceFilter();

	/** The person without pnd filter. */
	private ViewerFilter _personWithoutPNDFilter = new PersonWithoutPNDFilter();

	/** The person without lccn filter. */
	private ViewerFilter _personWithoutLCCNFilter = new PersonWithoutLCCNFilter();

	/** The person without viaf filter. */
	private ViewerFilter _personWithoutVIAFFilter = new PersonWithoutVIAFFilter();

	/** The person without iccu filter. */
	private ViewerFilter _personWithoutICCUFilter = new PersonWithoutICCUFilter();

	/** The only person concurrences. */
	private Action _onlyAspectsWithDivergentMarkup, _openAspectsInNewTap, _openReferencesInNewTap, _onlyUpdatedObjects,
			_onlyNewObjects, _onlyIncorrectObjects, _onlyPersonIdentifiers, _onlyPersonConcurrences,
			_selectAllChildNods;

	/** The only without iccu persons. */
	private Action _onlyWithoutPNDPersons, _onlyWithoutLCCNPersons, _onlyWithoutVIAFPersons, _onlyWithoutICCUPersons;

	/** The load query. */
	private Action _loadQuery;

	/** The tree viewer map. */
	private HashMap<String, TreeViewer> _treeViewerMap = new HashMap<String, TreeViewer>();
	/**
	 * Tabfolder containing Trees.
	 */
	private CTabFolder _tabFolderLeft;
	/**
	 * contains the currently selected Object.
	 */
	private Group _currentObjectGroup;
	/**
	 * group contains Combos, Text, Button for TreeBuilder.
	 */
	private Group _groupTree;
	/**
	 * contains the currently selected Object.
	 */
	private Text _currentObjectText;
	/** search text. */
	private Text _searchText;
	/**
	 * part of Searchcombos for tree builder.
	 */
	private Combo _comboTree;
	/**
	 * Combo for Selection of Criteria for TreeBuilder.
	 */
	private Combo _comboTag;

	/** The _search result. */
	private Object _searchResult;

	/** The _selection. */
	private PdrObject[] _selection;

	/** The selection changed listeners. */
	private ArrayList<ISelectionChangedListener> _selectionChangedListeners = new ArrayList<ISelectionChangedListener>();

	/** Button to create tree. */
	private Button _treeButton;

	/** The _add aspect button. */
	private Button _addAspectButton;

	/** The _add similar aspect button. */
	private Button _addSimilarAspectButton;

	/** pdrquery. */
	private PdrQuery _pdrQuery;

	/** criteria for query. */
	private Criteria _criteria;

	/** tree counter. */
	private int _keyCounter = 0;

	/** The pressed. */
	@SuppressWarnings("unused")
	private boolean _pressed = false;
	/** _facade singleton instance. */
	private Facade _facade = Facade.getInstanz();

	/** Logger. */
	private ILog _iLogger = AEConstants.ILOGGER;

	/** _MainSearcher als Singleton. */
	private AMainSearcher _mainSearcher = _facade.getMainSearcher();

	/** Instance of shared image registry. */
	private ImageRegistry _imageReg = CommonActivator.getDefault().getImageRegistry();

	/**
	 * Hauptmethode der ViewKlasse.
	 * @param parent.
	 */
	private Shell _parentShell;

	/** The ref facets. */
	private String[] _refFacets = new String[]
	{"title", "subtitle", "partName", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			"name", "genre", "dateCreated", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			"dateIssued", "dateCaptured", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			"copyrightDate", "publisher", "place", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			"edition", "physicalLocation", "shelfLocator"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

	private boolean _advanced = Platform.getPreferencesService().getBoolean(CommonActivator.PLUGIN_ID,
			"AE_ADVANCED_VERSION", AEConstants.AE_ADVANCED_VERSION, null);

	private Button _facetPreferenceButton;
	/**
	 * public Constructor.
	 */
	public Treeview()
	{
	}

	@Override
	public final void addSelectionChangedListener(final ISelectionChangedListener listener)
	{
		_selectionChangedListeners.add(listener);

	}

	/**
	 * Builds the tab folder left.
	 */
	private void buildTabFolderLeft()
	{
		_tabFolderLeft = new CTabFolder(_groupTree, SWT.TOP);
		_tabFolderLeft.setLayout(new GridLayout());

		_tabFolderLeft.setLayoutData(new GridData(GridData.FILL_BOTH));
		// ((GridData) _tabFolderLeft.getLayoutData()).horizontalAlignment =
		// SWT.FILL;
		// ((GridData) _tabFolderLeft.getLayoutData()).grabExcessHorizontalSpace
		// = true;
		// ((GridData) _tabFolderLeft.getLayoutData()).verticalAlignment =
		// SWT.FILL;
		// ((GridData) _tabFolderLeft.getLayoutData()).grabExcessVerticalSpace =
		// true;
		((GridData) _tabFolderLeft.getLayoutData()).horizontalSpan = 4;
		_tabFolderLeft.setUnselectedImageVisible(false);
		_tabFolderLeft.setUnselectedCloseVisible(false);
		_tabFolderLeft.setMRUVisible(true);
		ViewHelper.setTabfolderSimple(_tabFolderLeft, false);
		_tabFolderLeft.addCTabFolder2Listener(new CTabFolder2Adapter()
		{
			@Override
			public void close(final CTabFolderEvent event)
			{
				_treeViewerMap.remove(_tabFolderLeft.getSelection().getData("key")); //$NON-NLS-1$
				uncheckToggleCommands();
			}
		});
		PlatformUI.getWorkbench().getHelpSystem().setHelp(_tabFolderLeft, "org.bbaw.pdr.ae.help._tabFolderLeft"); //$NON-NLS-1$

		_tabFolderLeft.setSelection(0);
		_tabFolderLeft.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent ev)
			{
				String key = (String) _tabFolderLeft.getSelection().getData("key");
				String type = (String) _tabFolderLeft.getSelection().getData("type");

				TreeViewer tv = _treeViewerMap.get(key);
				if (tv != null)
				{
					tv.resetFilters(); //$NON-NLS-1$
				}
				uncheckToggleCommands();

				// get the window (which is a IServiceLocator)
				IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
				// get the service
				ISourceProviderService service = (ISourceProviderService) window
						.getService(ISourceProviderService.class);
				// get our source provider by querying by the variable name
				TreeSelectionSourceProvider treeSelectionSourceProvider = (TreeSelectionSourceProvider) service
						.getSourceProvider(AEPluginIDs.SOURCE_PARAMETER_TREE);
				// set the value
				treeSelectionSourceProvider.setTreeSelection(key);
				treeSelectionSourceProvider.setTreeType(type);
				// System.out.println("treeSelectionSourceProvider.setTreeType(type) "
				// + type);
				// System.out.println("treeSelectionSourceProvider.setTreeType(key) "
				// + key);

				treeSelectionSourceProvider.setTreeViewer(tv);
			}

		}); // SelectionListener

		_sashFormMain.redraw();
		createTreeViewer(_tabFolderLeft, "allPersons"); //$NON-NLS-1$
		_keyCounter++;
		createTreeViewer(_tabFolderLeft, "allReferences"); //$NON-NLS-1$
		_tabFolderLeft.setSelection(0);

		// initialize sources parameters:
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		// get the service
		ISourceProviderService service = (ISourceProviderService) window.getService(ISourceProviderService.class);
		// get our source provider by querying by the variable name
		TreeSelectionSourceProvider treeSelectionSourceProvider = (TreeSelectionSourceProvider) service
				.getSourceProvider(AEPluginIDs.SOURCE_PARAMETER_TREE);
		// set the value
		treeSelectionSourceProvider.setTreeSelection("allPersons");
		treeSelectionSourceProvider.setTreeType("pdrPo");
		TreeViewer tv = _treeViewerMap.get("allPersons");
		if (tv != null)
		{
			treeSelectionSourceProvider.setTreeViewer(tv);
		}

		_keyCounter++;
		_groupTree.layout();
		_groupTree.pack();
		_sashFormMain.layout();
	}

	/**
	 * @param treeViewer This method creates the Tree.
	 */
	private void buildTree(final TreeViewer treeViewer)
	{
		createActions();

		createMenus(treeViewer.getControl());
		treeViewer.addSelectionChangedListener(new ISelectionChangedListener()
		{
			@Override
			public void selectionChanged(final SelectionChangedEvent event)
			{
				if (event.getSelection() instanceof IStructuredSelection)
				{
					IStructuredSelection selection = (IStructuredSelection) event.getSelection();
					Object[] objs = selection.toArray();
					TreeNode[] tns = new TreeNode[objs.length];
					TreeNode tn;
					for (int i = 0; i < objs.length; i++)
					{
						tns[i] = (TreeNode) objs[i];
					}

					System.out.println("Selection size " + selection.size());
					boolean loaded = true;
					if (tns != null && tns.length > 0)
					{
						tn = tns[tns.length - 1];
						if (tn.getPdrQuery() != null)
						{
							loaded = false;
							_treeSearchHelper.processTreeNode(tn);
							treeViewer.refresh();
						}

						if (tn != null && tn.hasChildren())
						{
							if (!treeViewer.getExpandedState(tn))
							{
								loaded = tn.getPdrObject() != null;
								// treeViewer.setExpandedState(tn,
								// !treeViewer.getExpandedState(tn));
							}
						}
						if (loaded) // objects are only selected if they are
									// already loaded, otherwise it might take
									// too long
						{
							// FIXME Selection steuerung
							ArrayList<PdrObject> helpObjs = new ArrayList<PdrObject>();
							for (int i = 0; i < tns.length; i++)
							{
								if (tns[i].getPdrObject() != null)
								{
									helpObjs.add(tns[i].getPdrObject());
								}
							}
							setStatusLine(helpObjs.size() + " " + NLMessages.getString("View_objects_selected"));
							_selection = helpObjs.toArray(new PdrObject[helpObjs.size()]);
							_facade.setCurrentTreeObjects(_selection);
							showCurrentPdrObject();
						}

					}

					IStatus sAspect = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Tree item selected: " + selection); //$NON-NLS-1$
					_iLogger.log(sAspect);
				}
			}
		});
		treeViewer.addDoubleClickListener(new IDoubleClickListener()
		{
			@Override
			public void doubleClick(final DoubleClickEvent event)
			{
				IStructuredSelection selection = (IStructuredSelection) event.getSelection();
				Object obj = selection.getFirstElement();
				TreeNode tn = (TreeNode) obj;
				if (tn != null && tn.hasChildren())
				{
					treeViewer.setExpandedState(tn, !treeViewer.getExpandedState(tn));
				}

			}
		});

	}

	@Override
	public final int computePreferredSize(final boolean width, final int availableParallel,
			final int availablePerpendicular, final int preferredResult)
	{
		return 350;
	}

	/**
	 * Creates the actions.
	 */
	@SuppressWarnings("unchecked")
	protected final void createActions()
	{
		_openAspectsInNewTap = new Action(NLMessages.getString("View_action_open_aspects_new_tab"))
		{
			@Override
			public void run()
			{
				TreeViewer viewer = (TreeViewer) _tabFolderLeft.getSelection().getData("viewer"); //$NON-NLS-1$
				//				System.out.println("TreeViewer " + viewer); //$NON-NLS-1$
				//				System.out.println("Selection " + viewer.getSelection()); //$NON-NLS-1$
				if (viewer != null)
				{
					IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
					Object obj = selection.getFirstElement();
					TreeNode tn = (TreeNode) obj;
					if (tn.getPdrObject() != null)
					{
						PdrObject pdrO = tn.getPdrObject();
						if (pdrO != null && pdrO.getPdrId() != null)
						{
							Event event = new Event();
							event.data = pdrO.getPdrId().toString();
							IHandlerService handlerService = (IHandlerService) PlatformUI.getWorkbench().getService(
									IHandlerService.class);
							try
							{
								handlerService.executeCommand(
										"org.bbaw.pdr.ae.view.main.commands.OpenAdditionalAspectsView", event); //$NON-NLS-1$
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
				_openAspectsInNewTap.setChecked(false);

			}
		};
		_openAspectsInNewTap.setChecked(false);
		_openAspectsInNewTap.setImageDescriptor(_imageReg.getDescriptor(IconsInternal.ASPECTS));

		_openReferencesInNewTap = new Action(NLMessages.getString("View_action_open_references_new_tab"))
		{
			@Override
			public void run()
			{
				TreeViewer viewer = (TreeViewer) _tabFolderLeft.getSelection().getData("viewer"); //$NON-NLS-1$
				//				System.out.println("TreeViewer " + viewer); //$NON-NLS-1$
				//				System.out.println("Selection " + viewer.getSelection()); //$NON-NLS-1$
				if (viewer != null)
				{
					IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
					Object obj = selection.getFirstElement();
					TreeNode tn = (TreeNode) obj;
					if (tn.getPdrObject() != null)
					{
						PdrObject pdrO = tn.getPdrObject();
						if (pdrO != null && pdrO.getPdrId() != null)
						{
							Event event = new Event();
							event.data = pdrO.getPdrId().toString();
							IHandlerService handlerService = (IHandlerService) PlatformUI.getWorkbench().getService(
									IHandlerService.class);
							try
							{
								handlerService.executeCommand(
										"org.bbaw.pdr.ae.view.main.commands.OpenAdditionalReferencesView", event); //$NON-NLS-1$
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

				_openReferencesInNewTap.setChecked(false);

			}
		};
		_openReferencesInNewTap.setChecked(false);
		_openReferencesInNewTap.setImageDescriptor(_imageReg.getDescriptor(IconsInternal.REFERENCES));

		_loadQuery = new Action(NLMessages.getString("View_action_load_query"))
		{
			@Override
			public void run()
			{
				_loadQuery.setChecked(false);
				PdrQuery query = (PdrQuery) _tabFolderLeft.getItem(_tabFolderLeft.getSelectionIndex()).getData("query"); //$NON-NLS-1$
				if (query != null)
				{
					Event event = new Event();
					event.data = query.clone();
					IHandlerService handlerService = (IHandlerService) PlatformUI.getWorkbench().getService(
							IHandlerService.class);
					if (query.getSearchLevel() == 0)
					{
						//						System.out.println("load query in simple search " + query.getType()); //$NON-NLS-1$
						_comboTree.select(query.getType());
						if (query.getCriterias() != null && query.getCriterias().firstElement() != null)
						{
							String crit0 = query.getCriterias().firstElement().getCrit0();
							//							System.out.println("crit0 " + crit0); //$NON-NLS-1$
							if (crit0 == null)
							{
								crit0 = "ALL"; //$NON-NLS-1$
							}
							IAEPresentable ci = ((HashMap<String, IAEPresentable>) _comboSemanticViewer.getInput())
									.get(crit0);
							if (ci == null)
							{
								ci = new ConfigItem(crit0, crit0);
								_comboSemanticViewer.add(ci);
							}
							IStructuredSelection selection = new StructuredSelection(ci);
							_comboSemanticViewer.setSelection(selection);
							if (query.getCriterias().firstElement().getSearchText() != null)
							{
								_searchText.setText(query.getCriterias().firstElement().getSearchText());
							}
							else
							{
								_searchText.setText(""); //$NON-NLS-1$
							}
						}
						else
						{
							_searchText.setText(""); //$NON-NLS-1$
							IAEPresentable ci = ((HashMap<String, IAEPresentable>) _comboSemanticViewer.getInput())
									.get("ALL"); //$NON-NLS-1$
							if (ci == null)
							{
								ci = new ConfigItem("ALL", "ALL"); //$NON-NLS-1$ //$NON-NLS-2$
								_comboSemanticViewer.add(ci);
							}
							IStructuredSelection selection = new StructuredSelection(ci);
							_comboSemanticViewer.setSelection(selection);
						}
					}
					else if (query.getSearchLevel() == 1)
					{
						try
						{
							handlerService.executeCommand(
									"org.bbaw.pdr.ae.view.main.commands.OpenAdvancedSearchDialog", event); //$NON-NLS-1$
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
					else if (query.getSearchLevel() == 2)
					{
						try
						{
							handlerService.executeCommand(
									"org.bbaw.pdr.ae.view.main.commands.OpenExpertSearchDialog", event); //$NON-NLS-1$
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
		};
		_loadQuery.setChecked(false);
		_loadQuery.setImageDescriptor(_imageReg.getDescriptor(IconsInternal.SEARCH));

		_selectAllChildNods = new Action(NLMessages.getString("Views_treeview_select_all_children"))
		{
			@Override
			public void run()
			{
				TreeViewer viewer = (TreeViewer) _tabFolderLeft.getSelection().getData("viewer"); //$NON-NLS-1$
				//				System.out.println("TreeViewer " + viewer); //$NON-NLS-1$
				//				System.out.println("Selection " + viewer.getSelection()); //$NON-NLS-1$
				if (viewer != null)
				{
					ArrayList<PdrObject> helpObjs = new ArrayList<PdrObject>();
					IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
					Object[] objs = selection.toArray();
					TreeNode[] tns = new TreeNode[objs.length];
					for (int i = 0; i < objs.length; i++)
					{
						tns[i] = (TreeNode) objs[i];
					}
					for (int i = 0; i < tns.length; i++)
					{
						if (tns[i].getPdrObject() != null)
						{
							helpObjs.add(tns[i].getPdrObject());
						}
						if (tns[i].hasChildren())
						{
							for (TreeNode c : tns[i].getChildren())
							{
								if (c.getPdrObject() != null)
								{
									helpObjs.add(c.getPdrObject());
								}
								if (c.hasChildren())
								{
									for (TreeNode cc : c.getChildren())
									{
										if (cc.getPdrObject() != null)
										{
											helpObjs.add(cc.getPdrObject());
										}
										if (cc.hasChildren())
										{
											for (TreeNode ccc : cc.getChildren())
											{
												if (ccc.getPdrObject() != null)
												{
													helpObjs.add(ccc.getPdrObject());
												}
												if (ccc.hasChildren())
												{
													for (TreeNode cccc : ccc.getChildren())
													{
														if (cccc.getPdrObject() != null)
														{
															helpObjs.add(cccc.getPdrObject());
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
					System.out.println("tree all children size: " + helpObjs.size());
					_selection = helpObjs.toArray(new PdrObject[helpObjs.size()]);
					StructuredSelection sel = null;
					if (_selection != null)
					{
						sel = new StructuredSelection(_selection);
					}
					selectionChanged(Treeview.this, sel);
					_facade.setCurrentTreeObjects(_selection);
					setStatusLine(helpObjs.size() + " " + NLMessages.getString("View_objects_selected"));

				}

				_selectAllChildNods.setChecked(false);

			}
		};
		_selectAllChildNods.setChecked(false);
		_selectAllChildNods.setImageDescriptor(_imageReg.getDescriptor(IconsInternal.TREE));

	}

	/**
	 * Creates the menus.
	 * @param control the control
	 */
	protected final void createMenus(final Control control)
	{
		MenuManager menuMgr = new MenuManager();
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

		control.setMenu(menu);
		getSite().registerContextMenu(menuMgr, Treeview.this);
	}

	@Override
	public final void createPartControl(final Composite parent)
	{
		parent.setLayout(new GridLayout());
		_pdrQuery = new PdrQuery();
		_criteria = new Criteria();
		getSite().getPage().addSelectionListener(this);
		getSite().setSelectionProvider(this);
		_facade.addObserver(this);

		_sashFormMain = new SashForm(parent, SWT.VERTICAL);
		_sashFormMain.setLayout(new GridLayout());
		_sashFormMain.setLayoutData(new GridData(GridData.FILL_BOTH));
		{
			_currentObjectGroup = new Group(_sashFormMain, SWT.SHADOW_IN);
			_currentObjectGroup.setText(""); //$NON-NLS-1$
			_currentObjectGroup.setLayout(new GridLayout());
			((GridLayout) _currentObjectGroup.getLayout()).numColumns = 2;
			_currentObjectGroup.setLayoutData(new GridData());
			_currentObjectText = new Text(_currentObjectGroup, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL | SWT.READ_ONLY);
			_currentObjectText.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
			_currentObjectText.setLayoutData(new GridLayout());
			_currentObjectText.setLayoutData(new GridData());
			((GridData) _currentObjectText.getLayoutData()).horizontalAlignment = SWT.FILL;
			((GridData) _currentObjectText.getLayoutData()).verticalAlignment = SWT.FILL;
			((GridData) _currentObjectText.getLayoutData()).horizontalSpan = 2;
			((GridData) _currentObjectText.getLayoutData()).grabExcessHorizontalSpace = true;
			((GridData) _currentObjectText.getLayoutData()).grabExcessVerticalSpace = true;

			if (_advanced)
			{
				_addAspectButton = new Button(_currentObjectGroup, SWT.PUSH);
				_addAspectButton.setText(NLMessages.getString("View_add_new_aspect")); //$NON-NLS-1$
				_addAspectButton.setToolTipText(NLMessages.getString("View_add_new_aspect_tooltip")); //$NON-NLS-1$
				_addAspectButton.setLayoutData(new GridData());
				((GridData) _addAspectButton.getLayoutData()).horizontalAlignment = SWT.LEFT;
				((GridData) _addAspectButton.getLayoutData()).horizontalSpan = 2;

				_addAspectButton.setEnabled(false);
				_addAspectButton.setImage(_imageReg.get(IconsInternal.ASPECT_ADD_SAME_PERSON));
				_addAspectButton.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent event)
					{
						IHandlerService handlerService = (IHandlerService) PlatformUI.getWorkbench().getService(
								IHandlerService.class);
						try
						{
							handlerService.executeCommand("org.bbaw.pdr.ae.view.main.commands.AddNewAspect", null); //$NON-NLS-1$
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
				}); // SelectionListener

				_addSimilarAspectButton = new Button(_currentObjectGroup, SWT.PUSH);
				_addSimilarAspectButton.setText(NLMessages.getString("View_add_similar_aspect"));
				_addSimilarAspectButton.setToolTipText(NLMessages.getString("View_add_similar_aspect_tooltip")); //$NON-NLS-1$
				_addSimilarAspectButton.setLayoutData(new GridData());
				((GridData) _addSimilarAspectButton.getLayoutData()).horizontalSpan = 2;

				((GridData) _addSimilarAspectButton.getLayoutData()).horizontalAlignment = SWT.LEFT;
				_addSimilarAspectButton.setEnabled(false);
				_addSimilarAspectButton.setImage(_imageReg.get(IconsInternal.ASPECT_ADD_SAME_SOURCE));
				_addSimilarAspectButton.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent event)
					{
						IHandlerService handlerService = (IHandlerService) PlatformUI.getWorkbench().getService(
								IHandlerService.class);
						try
						{
							handlerService.executeCommand("org.bbaw.pdr.ae.view.main.commands.AddSimilarAspect", null); //$NON-NLS-1$
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
			}
			{

				{
					_groupTree = new Group(_sashFormMain, SWT.SHADOW_IN);
					_groupTree.setText(NLMessages.getString("Treeview_criteriaForTree")); //$NON-NLS-1$
					_groupTree.setLayout(new GridLayout());
					((GridLayout) _groupTree.getLayout()).numColumns = 4;
					((GridLayout) _groupTree.getLayout()).makeColumnsEqualWidth = false;
					_groupTree.setLayoutData(new GridData());

					{
						Label treeSearchLabel = new Label(_groupTree, SWT.NONE);
						treeSearchLabel.setText(NLMessages.getString("Treeview_searchFor")); //$NON-NLS-1$
						treeSearchLabel.setLayoutData(new GridData());
						((GridData) treeSearchLabel.getLayoutData()).horizontalSpan =2;

						_comboTree = new Combo(_groupTree, SWT.READ_ONLY);
						_comboTree.setLayoutData(new GridData());
						((GridData) _comboTree.getLayoutData()).horizontalAlignment = SWT.FILL;
						((GridData) _comboTree.getLayoutData()).grabExcessHorizontalSpace = true;
						((GridData) _comboTree.getLayoutData()).horizontalSpan = 2;
						_comboTreeViewer = new ComboViewer(_comboTree);
						_comboTreeViewer.setContentProvider(new ArrayContentProvider());
						_comboTreeViewer.setLabelProvider(new LabelProvider()
						{

							@Override
							public String getText(final Object element)
							{
								String str = (String) element;
								return NLMessages.getString("Treeview_searchTrees_" + str); //$NON-NLS-1$
							}

						});
						String[] trees = new String[]
						{"aspects", //$NON-NLS-1$
								"persons", //$NON-NLS-1$
								"references", //$NON-NLS-1$
								"faceted-persons", //$NON-NLS-1$
								"faceted-aspects", //$NON-NLS-1$
								"faceted-references"}; //$NON-NLS-1$
						_comboTreeViewer.setInput(trees);
						_comboTree.select(1);
						final Label treeSemanticLabel = new Label(_groupTree, SWT.NONE);
						_comboTreeViewer.addSelectionChangedListener(new ISelectionChangedListener()
						{

							@Override
							public void selectionChanged(final SelectionChangedEvent event)
							{
								ISelection iSelection = event.getSelection();
								Object obj = ((IStructuredSelection) iSelection).getFirstElement();
								String str = (String) obj;
								if (str != null)
								{
									_pdrQuery.setType(_comboTree.getSelectionIndex());
									_pdrQuery.setCriterias(null);

									_criteria.setFuzzy(true);

									_criteria.setType("tagging"); //$NON-NLS-1$

									//    		    	    		System.out.println(_comboTree.getSelectionIndex() + " selected: "  //$NON-NLS-1$
									//    		    	    			+ str); //$NON-NLS-1$
									if (_comboTree.getSelectionIndex() == 2)
									{
										_facetPreferenceButton.setEnabled(false);
										((AEConfigPresentableContentProvider) _comboSemanticViewer.getContentProvider())
												.setAddALL(true);
										treeSemanticLabel.setText(NLMessages.getString("View_genre"));
										if (_facade.getAllGenres() != null && !_facade.getAllGenres().isEmpty())
										{
											_comboSemanticViewer.setInput(_facade.getAllGenres());
										}
									}
									else if (_comboTree.getSelectionIndex() == 3)
									{
										_facetPreferenceButton.setEnabled(true);
										((AEConfigPresentableContentProvider) _comboSemanticViewer.getContentProvider())
												.setAddALL(false);
										_criteria.setCrit0(null);
										_pdrQuery.setKey("content"); //$NON-NLS-1$
										treeSemanticLabel.setText(NLMessages.getString("View_facets"));
										if (_facade.getFacetProposals() != null
												&& !_facade.getFacetProposals().isEmpty())
										{
											_comboSemanticViewer.setInput(_facade.getFacetProposals());
										}
										else
										{
											_comboSemanticViewer.setInput(null);
											MessageDialog messageDialog = new MessageDialog(
													_parentShell,
													NLMessages.getString("View_message_person_facet_empty"),
													null,
													NLMessages.getString("View_message_person_facet_customize"),
													MessageDialog.INFORMATION,
													new String[]
													{
															NLMessages
																	.getString("View_message_customize_person_facet_proposals"),
															NLMessages.getString("Handler_cancel")}, 0);
											int returnCode = messageDialog.open();
											if (returnCode == 0)
											{
												openPreferencePage("org.bbaw.pdr.ae.view.main.preferences.FacetedSearch");
												if (_facade.getFacetProposals() != null
														&& !_facade.getFacetProposals().isEmpty())
												{
													_comboSemanticViewer.setInput(_facade.getFacetProposals());
												}
											}
										}
									}
									else if (_comboTree.getSelectionIndex() == 4)
									{
										_facetPreferenceButton.setEnabled(true);
										((AEConfigPresentableContentProvider) _comboSemanticViewer.getContentProvider())
												.setAddALL(false);
										_criteria.setCrit0(null);
										_pdrQuery.setKey("content"); //$NON-NLS-1$
										treeSemanticLabel.setText(NLMessages.getString("View_facets"));
										if (_facade.getAspectFacetProposals() != null
												&& !_facade.getAspectFacetProposals().isEmpty())
										{
											_comboSemanticViewer.setInput(_facade.getAspectFacetProposals());
										}
										else
										{
											_comboSemanticViewer.setInput(null);
											MessageDialog messageDialog = new MessageDialog(
													_parentShell,
													NLMessages.getString("View_message_aspect_facet_empty"),
													null,
													NLMessages.getString("View_message_aspect_facet_customize"),
													MessageDialog.INFORMATION,
													new String[]
													{
															NLMessages
																	.getString("View_message_customize_aspect_facet_proposals"),
															NLMessages.getString("Handler_cancel")}, 0);
											int returnCode = messageDialog.open();
											if (returnCode == 0)
											{
												openPreferencePage("org.bbaw.pdr.ae.view.main.preferences.FacetedAspectSearch");
												if (_facade.getAspectFacetProposals() != null
														&& !_facade.getAspectFacetProposals().isEmpty())
												{
													_comboSemanticViewer.setInput(_facade.getAspectFacetProposals());
												}
											}

										}
									}
									else if (_comboTree.getSelectionIndex() == 5)
									{
										_facetPreferenceButton.setEnabled(false);
										((AEConfigPresentableContentProvider) _comboSemanticViewer.getContentProvider())
												.setAddALL(false);
										_criteria.setCrit0(null);
										treeSemanticLabel.setText(NLMessages.getString("View_facets"));
										HashMap<String, ConfigItem> refFacetsConfigItems = new HashMap<String, ConfigItem>(
												_refFacets.length);
										for (int i = 0; i < _refFacets.length; i++)
										{
											ConfigItem ci = new ConfigItem();
											ci.setValue(_refFacets[i]);
											ci.setLabel(NLMessages.getString("Editor_" + ci.getValue())); //$NON-NLS-1$
											refFacetsConfigItems.put(ci.getValue(), ci);
										}
										_comboSemanticViewer.setInput(refFacetsConfigItems);
									}
									else
									{
										((AEConfigPresentableContentProvider) _comboSemanticViewer.getContentProvider())
												.setAddALL(true);
										treeSemanticLabel.setText(NLMessages.getString("View_semantic"));
										if (_facade.getAllSemantics() != null && !_facade.getAllSemantics().isEmpty())
										{
											_comboSemanticViewer.setInput(_facade.getAllSemantics());
										}
										_facetPreferenceButton.setEnabled(false);

									}
									_comboTag.select(0);
								}
							}

						});

						_pdrQuery.setType(_comboTree.getSelectionIndex());
						_pdrQuery.setCriterias(null);
						_criteria.setType("tagging"); //$NON-NLS-1$
						_criteria.setFuzzy(true);

						treeSemanticLabel.setText(NLMessages.getString("Treeview_semantic_genre")); //$NON-NLS-1$
						treeSemanticLabel.setLayoutData(new GridData());
						
						_facetPreferenceButton = new Button(_groupTree, SWT.READ_ONLY);
						_facetPreferenceButton.setLayoutData(new GridData());
						((GridData) _facetPreferenceButton.getLayoutData()).horizontalAlignment = SWT.RIGHT;
						_facetPreferenceButton.setImage(_imageReg.get(IconsInternal.PREFERENCES));
						_facetPreferenceButton.setEnabled(false);
						_facetPreferenceButton.addSelectionListener(new SelectionAdapter()
						{
							@Override
							public void widgetSelected(final SelectionEvent event)
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
								String pageID = null;
								switch (_pdrQuery.getType())
								{
									case 3:
										pageID = "org.bbaw.pdr.ae.view.main.preferences.FacetedSearch";
										break;
									case 4:
										pageID = "org.bbaw.pdr.ae.view.main.preferences.FacetedAspectSearch";
										break;

									default:
										break;
								}
								if (pageID != null)
								{
									Parameterization params = new Parameterization(iparam, pageID);
									parameters.add(params);

									// build the parameterized command
									ParameterizedCommand pc = new ParameterizedCommand(cmd, parameters
											.toArray(new Parameterization[parameters.size()]));

									// execute the command
									try
									{
										IHandlerService handlerService = (IHandlerService) window
												.getService(IHandlerService.class);
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

							}
						}); // SelectionListener

						_comboTag = new Combo(_groupTree, SWT.READ_ONLY);
						_comboTag.setLayoutData(new GridData());
						((GridData) _comboTag.getLayoutData()).horizontalAlignment = SWT.FILL;
						((GridData) _comboTag.getLayoutData()).grabExcessHorizontalSpace = true;
						((GridData) _comboTag.getLayoutData()).horizontalSpan = 2;
						_comboSemanticViewer = new ComboViewer(_comboTag);
						_comboSemanticViewer.setContentProvider(new AEConfigPresentableContentProvider());
						_comboSemanticViewer.setLabelProvider(new AEConfigPresentableLabelProvider());
						((AEConfigPresentableContentProvider) _comboSemanticViewer.getContentProvider())
								.setAddALL(true);
						if (_facade.getAllSemantics() != null && !_facade.getAllSemantics().isEmpty())
						{
							_comboSemanticViewer.setInput(_facade.getAllSemantics());
							_comboTag.select(0);
							_criteria.setCrit0(_comboTag.getItem(0));
						}

						_comboSemanticViewer.addSelectionChangedListener(new ISelectionChangedListener()
						{

							@Override
							public void selectionChanged(final SelectionChangedEvent event)
							{
								ISelection iSelection = event.getSelection();
								Object obj = ((IStructuredSelection) iSelection).getFirstElement();
								IAEPresentable cp = (IAEPresentable) obj;
								if (cp != null)
								{
									_criteria.setType("tagging"); //$NON-NLS-1$
									_criteria.setFuzzy(true);
									if (_comboTree.getSelectionIndex() == 3 || _comboTree.getSelectionIndex() == 4)
									{
										_criteria.setCrit0(null);
										ConfigItem ci = (ConfigItem) cp;
										if (ci.getPos() != null && ci.getPos().equals("type")) //$NON-NLS-1$
										{
											try
											{
												String[] facetStrings = _mainSearcher
														.getFacets(
																"tagging", ci.getParent().getValue().substring(5), ci.getValue(), null, //$NON-NLS-1$
																null);
												setQueryFacets(_pdrQuery, facetStrings);
											}
											catch (Exception e1)
											{
												e1.printStackTrace();
											}
										}
										else if (ci.getPos() != null && ci.getPos().equals("subtype")) //$NON-NLS-1$
										{
											try
											{
												String[] facetStrings = _mainSearcher.getFacets(
														"tagging",
														((ConfigItem) ci.getParent()).getParent().getValue()
																.substring(5), ci.getParent().getValue(),
														ci.getValue(), //$NON-NLS-1$
														null);
												setQueryFacets(_pdrQuery, facetStrings);
											}
											catch (Exception e1)
											{
												e1.printStackTrace();
											}
										}
										else if (ci.getPos() != null && ci.getPos().equals("role")) //$NON-NLS-1$
										{
											try
											{
												String[] facetStrings = _mainSearcher.getFacets("tagging",
														((ConfigItem) ((ConfigItem) ci.getParent()).getParent())
																.getParent().getValue().substring(5), ((ConfigItem) ci
																.getParent()).getParent().getValue(), ci.getParent()
																.getValue(), ci.getValue()); //$NON-NLS-1$
												setQueryFacets(_pdrQuery, facetStrings);
											}
											catch (Exception e1)
											{
												e1.printStackTrace();
											}
										}

										// try {
										// _pdrQuery.setFacets(_mainSearcher.getFacets("tagging",
										// "name", cp.getValue(), null, null));
										// } catch (XQException e) {
										// e.printStackTrace();
										// }
										_criteria.setCrit0("ALL"); //$NON-NLS-1$

									}
									else if (_comboTree.getSelectionIndex() == 5)
									{
										_criteria.setCrit0(null);
										ConfigItem ci = (ConfigItem) cp;
										String type = null;
										String[] referenceFacets = null;
										if (ci.getValue().equals("name")) //$NON-NLS-1$
										{
											ci.setValue("namePart"); //$NON-NLS-1$
											type = "family"; //$NON-NLS-1$
										}
										try
										{
											referenceFacets = _mainSearcher.getFacets(
													"reference", ci.getValue(), type, null, null); //$NON-NLS-1$
										}
										catch (Exception e)
										{
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										if (ci.getValue().equals("genre")) //$NON-NLS-1$
										{
											_pdrQuery.setKey("genre"); //$NON-NLS-1$

										}
										else
										{
											_pdrQuery.setKey(null);
										}
										setQueryFacets(_pdrQuery, referenceFacets);
									}
									else if (cp.getValue() == null || cp.getValue().equals("ALL")
											|| cp.getLabel() == null || cp.getLabel().equals("ALL")) //$NON-NLS-1$ //$NON-NLS-2$
									{
										_criteria.setCrit0(null);

									}
									else
									{
										_criteria.setCrit0(cp.getValue());
									}

								}
							}
						});
						// _comboTag.addSelectionListener(new SelectionAdapter()
						// {
						// public void widgetSelected(SelectionEvent se)
						// {
						//		    	    		_criteria.setType("tagging"); //$NON-NLS-1$
						// _criteria.setFuzzy(true);
						//
						// // if facet person search set facets.
						// if (_pdrQuery.getType() == 3)
						// {
						// try {
						// _pdrQuery.setFacets(_mainSearcher.getFacets("tagging",
						// "name",
						// _comboTag.getItem(_comboTag.getSelectionIndex()),
						// null, null));
						// } catch (XQException e) {
						// e.printStackTrace();
						// }
						// _criteria.setCrit0("ALL");
						//
						// }
						// else
						// {
						// _criteria.setCrit0(_comboTag.getItem(_comboTag.getSelectionIndex()));
						// }
						// // System.out.println(_comboTag.getSelectionIndex()
						////		    	    			+ " selected: " //$NON-NLS-1$
						// // +
						// _comboTag.getItem(_comboTag.getSelectionIndex()));
						// }
						// }); //SelectionListener

						_searchText = new Text(_groupTree, SWT.BORDER);
						_searchText.setLayoutData(new GridData());
						_searchText.setLayoutData(new GridData());
						((GridData) _searchText.getLayoutData()).horizontalAlignment = SWT.FILL;
						((GridData) _searchText.getLayoutData()).grabExcessHorizontalSpace = true;
						((GridData) _searchText.getLayoutData()).horizontalSpan = 4;

						ControlDecoration decoValIdInfo = new ControlDecoration(_searchText, SWT.LEFT | SWT.BOTTOM);
						decoValIdInfo
								.setDescriptionText("Use ? as wildcard for a single character.\nUse * for any number of characters.");
						decoValIdInfo.setImage(FieldDecorationRegistry.getDefault()
								.getFieldDecoration(FieldDecorationRegistry.DEC_INFORMATION).getImage());
						decoValIdInfo.setShowOnlyOnFocus(false);

						_searchText.addKeyListener(new KeyListener()
						{

							@Override
							public void keyPressed(final KeyEvent e)
							{
								if (e.keyCode == SWT.CR)
								{
									search();
								}
							}

							@Override
							public void keyReleased(final KeyEvent e)
							{
								// TODO Auto-generated method stub

							}
						});

						if (_advanced)
						{
							Button advancedTreeButton = new Button(_groupTree, SWT.PUSH);
							advancedTreeButton.setText(NLMessages.getString("Treeview_advanced")); //$NON-NLS-1$
							advancedTreeButton
									.setToolTipText(NLMessages.getString("View_open_advanced_search_tooltip"));
							advancedTreeButton.setImage(_imageReg.get(IconsInternal.SEARCH_ADVANCED));
							advancedTreeButton.setLayoutData(new GridData());
							((GridData) advancedTreeButton.getLayoutData()).horizontalAlignment = SWT.LEFT;

							advancedTreeButton.addSelectionListener(new SelectionAdapter()
							{
								@Override
								public void widgetSelected(final SelectionEvent event)
								{

									IHandlerService handlerService = (IHandlerService) PlatformUI.getWorkbench()
											.getService(IHandlerService.class);
									try
									{
										handlerService.executeCommand(
												"org.bbaw.pdr.ae.view.main.commands.OpenAdvancedSearchDialog", null); //$NON-NLS-1$
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

							Button expertTreeButton = new Button(_groupTree, SWT.PUSH);
							expertTreeButton.setText(NLMessages.getString("Treeview_expert")); //$NON-NLS-1$
							expertTreeButton.setToolTipText(NLMessages.getString("View_open_expert_search_tooltip"));
							expertTreeButton.setImage(_imageReg.get(IconsInternal.SEARCH_EXPERT));
							expertTreeButton.setLayoutData(new GridData());
							((GridData) expertTreeButton.getLayoutData()).horizontalAlignment = SWT.LEFT;
							((GridData) expertTreeButton.getLayoutData()).horizontalSpan = 2;

							expertTreeButton.addSelectionListener(new SelectionAdapter()
							{
								@Override
								public void widgetSelected(final SelectionEvent event)
								{

									IHandlerService handlerService = (IHandlerService) PlatformUI.getWorkbench()
											.getService(IHandlerService.class);
									try
									{
										handlerService.executeCommand(
												"org.bbaw.pdr.ae.view.main.commands.OpenExpertSearchDialog", null); //$NON-NLS-1$
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
						}

						_treeButton = new Button(_groupTree, SWT.PUSH);
						_treeButton.setText(NLMessages.getString("View_search")); //$NON-NLS-1$
						_treeButton.setToolTipText(NLMessages.getString("View_create_tree_tooltip"));
						_treeButton.setLayoutData(new GridData());
						((GridData) _treeButton.getLayoutData()).horizontalAlignment = SWT.RIGHT;
						((GridData) _treeButton.getLayoutData()).horizontalSpan = 1;

						_treeButton.setImage(_imageReg.get(IconsInternal.SEARCH));

						_treeButton.addSelectionListener(new SelectionAdapter()
						{
							@Override
							public void widgetSelected(final SelectionEvent event)
							{
								search();
							}
						}); // SelectionListener
					} // _groupTree
				}

			}
			buildTabFolderLeft();

			_sashFormMain.setWeights(new int[]
			{1, 4});

		}
	}

	/**
	 * creates new TabItem for TreeBuilder in which the newly generated Tree is
	 * beeing shown. This method is separated from the TreeBuilder itself in
	 * order to pass the Treeviewermethod later to a separate thread.
	 * @param tabFolderLeft tabFolder
	 * @param name name
	 */
	@SuppressWarnings("unused")
	private void createTabItem(final TabFolder tabFolderLeft, final String name)
	{

		TabItem tabItemLowerLeftPersonen = new TabItem(tabFolderLeft, SWT.NONE);
		tabItemLowerLeftPersonen.setText(name);
		tabFolderLeft.setSelection(tabItemLowerLeftPersonen);
		// setBulb();
		Tree tree = new Tree(_tabFolderLeft, SWT.BORDER);
		tabItemLowerLeftPersonen.setControl(tree);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(tree, "org.bbaw.pdr.ae.help.objectsTree"); //$NON-NLS-1$
		// buildTree(tree);

	}

	/**
	 * Creates the tree viewer.
	 * @param tabFolderLeft the _tab folder left
	 * @param key the key
	 */
	private void createTreeViewer(final CTabFolder tabFolderLeft, final String key)
	{

		CTabItem tabItemLowerLeftPersonen;
		Tree tree;
		String type = "";
		if (key.equals("allPersons")) //$NON-NLS-1$
		{
			tabItemLowerLeftPersonen = new CTabItem(tabFolderLeft, SWT.NONE);
			tabItemLowerLeftPersonen.setText(NLMessages.getString("Treeview_all_persons"));
			tabItemLowerLeftPersonen.setData("key", key); //$NON-NLS-1$
			tabItemLowerLeftPersonen.setData("type", "pdrPo"); //$NON-NLS-1$

			tabFolderLeft.setSelection(tabItemLowerLeftPersonen);
			// setBulb();
			tabItemLowerLeftPersonen.setImage(_imageReg.get(IconsInternal.PERSONS));
			tree = new Tree(tabFolderLeft, SWT.BORDER | SWT.MULTI);
			tabItemLowerLeftPersonen.setControl(tree);
			PlatformUI.getWorkbench().getHelpSystem().setHelp(tree, "org.bbaw.pdr.ae.help.objectsTree"); //$NON-NLS-1$
			_allPersonsTreeViewer = new TreeViewer(tree);
			_allPersonsTreeViewer.setData("key", 1); //$NON-NLS-1$
			_allPersonsTreeViewer.setData("tab", tabItemLowerLeftPersonen); //$NON-NLS-1$

			tabItemLowerLeftPersonen.setData("viewer", _allPersonsTreeViewer); //$NON-NLS-1$
			_allPersonsTreeViewer.setContentProvider(new TreeContentProvider());
			_allPersonsTreeViewer.setLabelProvider(new TreeLabelProvider());
			_allPersonsTreeViewer.setUseHashlookup(true);
			_treeViewerMap.put(key, _allPersonsTreeViewer);
			if (!_facade.getLazyLoading())
			{
				if (_facade.getAllPersons() == null || _facade.getAllPersons().isEmpty())
				{
					UIJob job = new UIJob(key)
					{

						@Override
						public IStatus runInUIThread(final IProgressMonitor monitor)
						{
							try
							{
								_facade.setAllPersons(_mainSearcher.searchAllPersons());
							}
							catch (Exception e)
							{
								e.printStackTrace();
							}
							if (monitor.isCanceled())
							{
								return Status.CANCEL_STATUS;
							}

							return Status.OK_STATUS;
						}
					};
					job.setUser(true);
					job.schedule();

				}
			}
			else
			{
				TreeNode root = new TreeSearchHelper().getTreeNodeAlphabet("pdrPo", 1);
				_allPersonsTreeViewer.setInput(root);
			}

			// Create menu, toolbars, filters, sorters.
			// createActions();
			// createMenus(_allPersonsTreeViewer);
			// _allPersonsTreeViewer.setInput(_facade.getAllPersons());
			buildTree(_allPersonsTreeViewer);
			setStatusLine("Ready");

		}
		else if (key.equals("allReferences")) //$NON-NLS-1$
		{
			tabItemLowerLeftPersonen = new CTabItem(_tabFolderLeft, SWT.NONE);
			tabItemLowerLeftPersonen.setText(NLMessages.getString("Treeview_all_references"));
			tabItemLowerLeftPersonen.setData("key", key); //$NON-NLS-1$
			tabItemLowerLeftPersonen.setData("type", "pdrRo"); //$NON-NLS-1$

			_tabFolderLeft.setSelection(tabItemLowerLeftPersonen);
			tabItemLowerLeftPersonen.setImage(_imageReg.get(IconsInternal.REFERENCES));
			tree = new Tree(_tabFolderLeft, SWT.BORDER | SWT.MULTI);
			tabItemLowerLeftPersonen.setControl(tree);

			PlatformUI.getWorkbench().getHelpSystem().setHelp(tree, "org.bbaw.pdr.ae.help.objectsTree"); //$NON-NLS-1$
			_allReferencesTreeViewer = new TreeViewer(tree);
			_allReferencesTreeViewer.setData("key", 2); //$NON-NLS-1$
			_allReferencesTreeViewer.setData("tab", tabItemLowerLeftPersonen); //$NON-NLS-1$

			tabItemLowerLeftPersonen.setData("viewer", _allReferencesTreeViewer); //$NON-NLS-1$

			if (_facade.getAllReferences() == null || _facade.getAllReferences().isEmpty())
			{
				setStatusLine("Performing Search " + key);
				UIJob job = new UIJob(key)
				{

					@Override
					public IStatus runInUIThread(final IProgressMonitor monitor)
					{
						try
						{
							_facade.setAllReferences(_mainSearcher.searchAllReferences());
						}
						catch (Exception e)
						{
							e.printStackTrace();
						}
						if (monitor.isCanceled())
						{
							return Status.CANCEL_STATUS;
						}

						return Status.OK_STATUS;
					}
				};
				job.setUser(true);
				job.schedule();

			}

			_allReferencesTreeViewer.setContentProvider(new TreeContentProvider());
			_allReferencesTreeViewer.setLabelProvider(new TreeLabelProvider());
			_treeViewerMap.put(key, _allReferencesTreeViewer);
			// Create menu, toolbars, filters, sorters.
			// createActions();
			// createMenus(_allReferencesTreeViewer);

			_allReferencesTreeViewer.setInput(_facade.getAllReferences());
			buildTree(_allReferencesTreeViewer);
			setStatusLine("Ready");
		}

		else
		{
			tabItemLowerLeftPersonen = new CTabItem(_tabFolderLeft, SWT.CLOSE);
			tabItemLowerLeftPersonen.setText(key);
			tabItemLowerLeftPersonen.setData("key", key); //$NON-NLS-1$
			_tabFolderLeft.setSelection(tabItemLowerLeftPersonen);

			tree = new Tree(_tabFolderLeft, SWT.BORDER | SWT.MULTI);
			tabItemLowerLeftPersonen.setControl(tree);

			PlatformUI.getWorkbench().getHelpSystem().setHelp(tree, "org.bbaw.pdr.ae.help.objectsTree"); //$NON-NLS-1$
			//            System.out.println("pdrQeuery type: " +_pdrQuery.getType()); //$NON-NLS-1$
			if (_pdrQuery.getType() == 0)

			{
				tabItemLowerLeftPersonen.setImage(_imageReg.get(IconsInternal.ASPECTS));
				tabItemLowerLeftPersonen.setData("type", "pdrAo"); //$NON-NLS-1$

				_treeViewer = new TreeViewer(tree);
				_treeViewer.setData("key", 0); //$NON-NLS-1$
				_treeViewer.setData("tab", tabItemLowerLeftPersonen); //$NON-NLS-1$

				tabItemLowerLeftPersonen.setData("viewer", _treeViewer); //$NON-NLS-1$
				tabItemLowerLeftPersonen.setData("query", _pdrQuery.clone()); //$NON-NLS-1$
				_treeViewer.setContentProvider(new TreeContentProvider());
				_treeViewer.setLabelProvider(new TreeLabelProvider());
				_treeViewerMap.put(key, _treeViewer);
				type = "pdrAo";
				setStatusLine("Performing Search " + key);
				UIJob job = new UIJob(_treeViewer.getTree().getDisplay(), key)
				{

					@Override
					public IStatus runInUIThread(final IProgressMonitor monitor)
					{
						_searchResult = null;
						try
						{
							_searchResult = _mainSearcher.searchAspects(_pdrQuery, monitor);
							_treeViewer.setInput(_searchResult);
							setStatusLine("Ready");
						}
						catch (Exception e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if (monitor.isCanceled())
						{
							return Status.CANCEL_STATUS;
						}
						return Status.OK_STATUS;
					}
				};
				job.setUser(true);
				job.schedule();
			}
			else if (_pdrQuery.getType() == 1)

			{
				tabItemLowerLeftPersonen.setImage(_imageReg.get(IconsInternal.PERSONS));
				tabItemLowerLeftPersonen.setData("key", key); //$NON-NLS-1$
				tabItemLowerLeftPersonen.setData("type", "pdrPo"); //$NON-NLS-1$

				_treeViewer = new TreeViewer(tree);
				_treeViewer.setData("key", 1); //$NON-NLS-1$
				_treeViewer.setData("tab", tabItemLowerLeftPersonen); //$NON-NLS-1$

				tabItemLowerLeftPersonen.setData("viewer", _treeViewer); //$NON-NLS-1$
				tabItemLowerLeftPersonen.setData("query", _pdrQuery.clone()); //$NON-NLS-1$
				_treeViewer.setContentProvider(new TreeContentProvider());
				_treeViewer.setLabelProvider(new TreeLabelProvider());
				_treeViewerMap.put(key, _treeViewer);
				type = "pdrPo";
				setStatusLine("Performing Search " + key);
				Job job = new Job(key)
				{
					@Override
					protected IStatus run(final IProgressMonitor monitor)
					{
						_searchResult = null;
						try
						{
							_searchResult = _mainSearcher.searchPersons(_pdrQuery, monitor);
						}
						catch (Exception e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if (monitor.isCanceled())
						{
							return Status.CANCEL_STATUS;
						}
						UIJob job = new UIJob(_treeViewer.getTree().getDisplay(), "Load Result")
						{
							@Override
							public IStatus runInUIThread(final IProgressMonitor monitor)
							{
								_treeViewer.setInput(_searchResult);
								setStatusLine("Ready");
								return Status.OK_STATUS;
							}
						};
						job.setUser(true);
						job.schedule();
						return Status.OK_STATUS;
					}
				};
				job.setUser(true);
				job.schedule();
			}
			else if (_pdrQuery.getType() == 2)
			{
				tabItemLowerLeftPersonen.setImage(_imageReg.get(IconsInternal.REFERENCES));
				tabItemLowerLeftPersonen.setData("key", key); //$NON-NLS-1$
				tabItemLowerLeftPersonen.setData("type", "pdrRo"); //$NON-NLS-1$

				_treeViewer = new TreeViewer(tree);
				_treeViewer.setData("key", 2); //$NON-NLS-1$
				_treeViewer.setData("tab", tabItemLowerLeftPersonen); //$NON-NLS-1$

				tabItemLowerLeftPersonen.setData("viewer", _treeViewer); //$NON-NLS-1$
				tabItemLowerLeftPersonen.setData("query", _pdrQuery.clone()); //$NON-NLS-1$
				_treeViewer.setContentProvider(new TreeContentProvider());
				_treeViewer.setLabelProvider(new TreeLabelProvider());
				_treeViewerMap.put(key, _treeViewer);
				type = "pdrRo";
				setStatusLine("Performing Search " + key);
				Job job = new Job(key)
				{
					@Override
					protected IStatus run(final IProgressMonitor monitor)
					{
						_searchResult = null;
						try
						{
							_searchResult = _mainSearcher.searchReferences(_pdrQuery, monitor);
						}
						catch (Exception e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if (monitor.isCanceled())
						{
							return Status.CANCEL_STATUS;
						}
						UIJob job = new UIJob(_treeViewer.getTree().getDisplay(), "Load Result")
						{
							@Override
							public IStatus runInUIThread(final IProgressMonitor monitor)
							{
								_treeViewer.setInput(_searchResult);
								setStatusLine("Ready");
								return Status.OK_STATUS;
							}
						};
						job.setUser(true);
						job.schedule();
						return Status.OK_STATUS;
					}
				};
				job.setUser(true);
				job.schedule();
			}
			else if (_pdrQuery.getType() == 3)
			{
				tabItemLowerLeftPersonen.setImage(_imageReg.get(IconsInternal.PERSONS));
				tabItemLowerLeftPersonen.setData("key", key); //$NON-NLS-1$
				tabItemLowerLeftPersonen.setData("type", "facet"); //$NON-NLS-1$

				_treeViewer = new TreeViewer(tree);
				_treeViewer.setData("key", 3); //$NON-NLS-1$
				_treeViewer.setData("tab", tabItemLowerLeftPersonen); //$NON-NLS-1$

				tabItemLowerLeftPersonen.setData("viewer", _treeViewer); //$NON-NLS-1$
				tabItemLowerLeftPersonen.setData("query", _pdrQuery.clone()); //$NON-NLS-1$
				_treeViewer.setContentProvider(new TreeContentProvider());
				_treeViewer.setLabelProvider(new TreeLabelProvider());
				_treeViewerMap.put(key, _treeViewer);
				type = "pdrPo";
				setStatusLine("Performing Search " + key);

				//	        	System.out.println("im else teil pers, nun er soll nach facet-person suchen"); //$NON-NLS-1$
				Job job = new Job(key)
				{
					@Override
					protected IStatus run(final IProgressMonitor monitor)
					{
						int work = 10;
						if (_pdrQuery.getFacets() != null)
						{
							work = _pdrQuery.getFacets().size();
						}
						monitor.beginTask("Searching... Number of Facets: " + work, work);
						try
						{
							_searchResult = _mainSearcher.searchFacetPersons(_pdrQuery, monitor);
						}
						catch (Exception e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						
						UIJob job = new UIJob(_treeViewer.getTree().getDisplay(), "Load Result")
						{
							@Override
							public IStatus runInUIThread(final IProgressMonitor monitor)
							{
								_treeViewer.setInput(_searchResult);
								setStatusLine("Ready");
								return Status.OK_STATUS;
							}
						};
						
						job.setUser(true);
						job.schedule();
						return Status.OK_STATUS;
					}
				};
				job.setUser(true);
				job.schedule();

			}
			else if (_pdrQuery.getType() == 4)
			{
				tabItemLowerLeftPersonen.setImage(_imageReg.get(IconsInternal.ASPECTS));
				tabItemLowerLeftPersonen.setData("key", key); //$NON-NLS-1$
				tabItemLowerLeftPersonen.setData("type", "facet"); //$NON-NLS-1$

				_treeViewer = new TreeViewer(tree);
				_treeViewer.setData("key", 4); //$NON-NLS-1$
				_treeViewer.setData("tab", tabItemLowerLeftPersonen); //$NON-NLS-1$

				tabItemLowerLeftPersonen.setData("viewer", _treeViewer); //$NON-NLS-1$
				tabItemLowerLeftPersonen.setData("query", _pdrQuery.clone()); //$NON-NLS-1$
				_treeViewer.setContentProvider(new TreeContentProvider());
				_treeViewer.setLabelProvider(new TreeLabelProvider());
				_treeViewerMap.put(key, _treeViewer);
				type = "pdrAo";
				setStatusLine("Performing Search " + key);

				Job job = new Job(key)
				{
					@Override
					protected IStatus run(final IProgressMonitor monitor)
					{
						int work = 10;
						if (_pdrQuery.getFacets() != null)
						{
							work = _pdrQuery.getFacets().size();
						}
						monitor.beginTask("Searching... Number of Facets: " + work, work);
						try
						{
							_searchResult = _mainSearcher.searchFacetAspects(_pdrQuery, monitor);
						}
						catch (Exception e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						UIJob job = new UIJob(_treeViewer.getTree().getDisplay(), "Load Result")
						{
							@Override
							public IStatus runInUIThread(final IProgressMonitor monitor)
							{
								_treeViewer.setInput(_searchResult);
								setStatusLine("Ready");
								if (monitor.isCanceled())
								{
									return Status.CANCEL_STATUS;
								}
								return Status.OK_STATUS;
							}
						};
						job.setUser(true);
						job.schedule();
						return Status.OK_STATUS;
					}
				};
				job.setUser(true);
				job.schedule();
			}
			else if (_pdrQuery.getType() == 5)
			{
				tabItemLowerLeftPersonen.setImage(_imageReg.get(IconsInternal.REFERENCES));
				tabItemLowerLeftPersonen.setData("key", key); //$NON-NLS-1$
				tabItemLowerLeftPersonen.setData("type", "facet"); //$NON-NLS-1$

				_treeViewer = new TreeViewer(tree);
				_treeViewer.setData("key", 3); //$NON-NLS-1$
				_treeViewer.setData("tab", tabItemLowerLeftPersonen); //$NON-NLS-1$

				tabItemLowerLeftPersonen.setData("viewer", _treeViewer); //$NON-NLS-1$
				tabItemLowerLeftPersonen.setData("query", _pdrQuery.clone()); //$NON-NLS-1$
				_treeViewer.setContentProvider(new TreeContentProvider());
				_treeViewer.setLabelProvider(new TreeLabelProvider());
				_treeViewerMap.put(key, _treeViewer);
				type = "pdrRo";
				setStatusLine("Performing Search " + key);
				Job job = new Job(key)
				{
					@Override
					protected IStatus run(final IProgressMonitor monitor)
					{
						try
						{
							_searchResult = _mainSearcher.searchFacetReferences(_pdrQuery, monitor);
						}
						catch (Exception e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if (monitor.isCanceled())
						{
							return Status.CANCEL_STATUS;
						}
						UIJob job = new UIJob(_treeViewer.getTree().getDisplay(), "Load Result")
						{
							@Override
							public IStatus runInUIThread(final IProgressMonitor monitor)
							{
								_treeViewer.setInput(_searchResult);
								setStatusLine("Ready");
								return Status.OK_STATUS;
							}
						};
						job.setUser(true);
						job.schedule();
						return Status.OK_STATUS;
					}
				};
				job.setUser(true);
				job.schedule();
			}

			if (_treeViewer.getInput() == null)
			{
				_treeViewer.setContentProvider(new TreeContentProvider());
				_treeViewer.setLabelProvider(new TreeLabelProvider());

				TreeNode tn = new TreeNode("Error", "Error"); //$NON-NLS-1$ //$NON-NLS-2$
				tn.addChild(new TreeNode("Error", "Error")); //$NON-NLS-1$ //$NON-NLS-2$
				_treeViewer.setInput(tn);
			}
			// get the window (which is a IServiceLocator)
			IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
			// get the service
			ISourceProviderService service = (ISourceProviderService) window.getService(ISourceProviderService.class);
			// get our source provider by querying by the variable name
			TreeSelectionSourceProvider treeSelectionSourceProvider = (TreeSelectionSourceProvider) service
					.getSourceProvider(AEPluginIDs.SOURCE_PARAMETER_TREE);
			// set the value
			treeSelectionSourceProvider.setTreeSelection(key);
			treeSelectionSourceProvider.setTreeType(type);
			treeSelectionSourceProvider.setTreeViewer(_treeViewer);
			setStatusLine("Ready"); //$NON-NLS-1$
			buildTree(_treeViewer);

		}
		// XXX anpassen an all person.

	}

	@Override
	public final void dispose()
	{
		// ISelectionService selectionService =
		// (ISelectionService)getSite().getService(ISelectionService.class);
		// selectionService.removeSelectionListener(this);
		super.dispose();
	}

	/**
	 * Dispose tab items.
	 */
	private void disposeTabItems()
	{

		if (_tabFolderLeft != null)
		{
			_tabFolderLeft.dispose();
		}

	}

	/**
	 * Fill menu.
	 * @param rootMenuManager the root menu manager
	 */
	protected final void fillMenu(final IMenuManager rootMenuManager)
	{
		rootMenuManager.add(_openAspectsInNewTap);
		rootMenuManager.add(_openReferencesInNewTap);
		rootMenuManager.add(new Separator());
		rootMenuManager.add(_loadQuery);
		rootMenuManager.add(_selectAllChildNods);
		rootMenuManager.add(new Separator());
		// IMenuManager filterSubmenu = new
		// MenuManager(NLMessages.getString("View_submenu_filters"));
		// rootMenuManager.add(filterSubmenu);
		// filterSubmenu.add(onlyUpdatedObjects);
		// filterSubmenu.add(onlyNewObjects);
		// filterSubmenu.add(onlyIncorrectObjects);
		// switch (key)
		// {
		// case 0:
		// {
		// filterSubmenu.add(onlyAspectsWithDivergentMarkup);
		// break;
		// }
		// case 1:
		// {
		// filterSubmenu.add(onlyPersonConcurrences);
		// filterSubmenu.add(onlyPersonIdentifiers);
		// filterSubmenu.add(onlyWithoutPNDPersons);
		// filterSubmenu.add(onlyWithoutLCCNPersons);
		// filterSubmenu.add(onlyWithoutVIAFPersons);
		// filterSubmenu.add(onlyWithoutICCUPersons);
		// }
		//
		// }
	}

	@Override
	public final ISelection getSelection()
	{
		if (_tabFolderLeft != null)
		{
			CTabItem tab = _tabFolderLeft.getItem(_tabFolderLeft.getSelectionIndex());
			if (tab != null)
			{
				TreeViewer tv = (TreeViewer) tab.getData("viewer");
				Object[] objs = ((IStructuredSelection) tv.getSelection()).toArray();
				TreeNode[] tns = new TreeNode[objs.length];
				for (int i = 0; i < objs.length; i++)
				{
					tns[i] = (TreeNode) objs[i];
				}

				if (tns != null && tns.length > 0)
				{
					ArrayList<PdrObject> helpObjs = new ArrayList<PdrObject>();
					for (int i = 0; i < tns.length; i++)
					{
						if (tns[i].getPdrObject() != null)
						{
							helpObjs.add(tns[i].getPdrObject());
						}
					}
//						else if (tns[i].hasChildren())
//						{
//							for (TreeNode c : tns[i].getChildren())
//							{
//								if (c.getPdrObject() != null)
//								{
//									helpObjs.add(c.getPdrObject());
//								}
//								else if (c.hasChildren())
//								{
//									for (TreeNode cc : c.getChildren())
//									{
//										if (cc.getPdrObject() != null)
//										{
//											helpObjs.add(cc.getPdrObject());
//										}
//									}
//								}
//							}
//						}
					// }
					setStatusLine(helpObjs.size() + " " + NLMessages.getString("View_objects_selected"));
					_selection = helpObjs.toArray(new PdrObject[helpObjs.size()]);
				}
			}
			if (_selection != null)
			{

				StructuredSelection selection = new StructuredSelection(_selection);
				return selection;
			}
			else
			{
				return null;
			}

		}
		else
		{
			return null;
		}
	}

	@Override
	public final int getSizeFlags(final boolean width)
	{
		return SWT.MIN;
	}

	/**
	 * Open in tree viewer.
	 * @param tn the tn
	 */
	public final void openInTreeViewer(final TreeNode tn)
	{
		if (tn != null)
		{
			TreeViewer treeViewer;
			CTabItem tabItem;
			String key;
			if (tn.getId() != null)
			{
				key = tn.getId();
			}
			else
			{
				key = "clipboard";
			}
			if (_treeViewerMap.containsKey(key))
			{
				treeViewer = _treeViewerMap.get(key);
				tabItem = (CTabItem) treeViewer.getData("tab");
			}
			else
			{
				tabItem = new CTabItem(_tabFolderLeft, SWT.CLOSE);
				Tree tree = new Tree(_tabFolderLeft, SWT.BORDER | SWT.MULTI);
				tabItem.setControl(tree);
				tabItem.setData("type", "clipboard"); //$NON-NLS-1$

				PlatformUI.getWorkbench().getHelpSystem().setHelp(tree, "org.bbaw.pdr.ae.help.objectsTree"); //$NON-NLS-1$
				treeViewer = new TreeViewer(tree);
				treeViewer.setUseHashlookup(true);

			}
			tabItem.setText(key);
			tabItem.setData("key", key); //$NON-NLS-1$
			// setBulb();
			tabItem.setImage(_imageReg.get(IconsInternal.PERSONS));

			treeViewer.setData("key", 1); //$NON-NLS-1$
			treeViewer.setData("tab", tabItem); //$NON-NLS-1$
			tabItem.setData("viewer", treeViewer); //$NON-NLS-1$

			treeViewer.setContentProvider(new TreeContentProvider());
			treeViewer.setLabelProvider(new TreeLabelProvider());
			_treeViewerMap.put(key, treeViewer);
			// Create menu, toolbars, filters, sorters.
			createActions();
			buildTree(treeViewer);
			setStatusLine("Ready");
			treeViewer.setInput(tn);
		}

	}

	/**
	 * Open preference page.
	 * @param pageId the page id
	 */
	protected final void openPreferencePage(final String pageId)
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
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Parameterization params = new Parameterization(iparam, pageId);
		parameters.add(params);

		// build the parameterized command
		ParameterizedCommand pc = new ParameterizedCommand(cmd, parameters.toArray(new Parameterization[parameters
				.size()]));

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

	@Override
	public final void removeSelectionChangedListener(final ISelectionChangedListener listener)
	{
		_selectionChangedListeners.remove(listener);

	}

	/**
	 * Search.
	 */
	protected final void search()
	{
		_pdrQuery.setSearchLevel(0);
		_pdrQuery.setType(_comboTree.getSelectionIndex());
		ISelection iSelection = _comboSemanticViewer.getSelection();
		Object obj = ((IStructuredSelection) iSelection).getFirstElement();
		IAEPresentable cp = (IAEPresentable) obj;
		if (_pdrQuery.getType() == 3)
		{
			_pdrQuery.setCriterias(new Vector<Criteria>(1));
			_pdrQuery.getCriterias().add(_criteria);
		}
		else if (_pdrQuery.getType() < 3)
		{

			if (cp != null)
			{
				if (cp.getValue() == null
						|| cp.getValue().equals("ALL") || cp.getLabel() == null || cp.getLabel().equals("ALL")) //$NON-NLS-1$ //$NON-NLS-2$
				{
					_criteria.setCrit0(null);

				}
				else
				{
					_criteria.setCrit0(cp.getValue());
				}
			}
			_criteria.setSearchText(_searchText.getText());
			_pdrQuery.setCriterias(new Vector<Criteria>(1));
			_pdrQuery.getCriterias().add(_criteria);
		}
		if (_comboTree.getSelectionIndex() == 3 || _comboTree.getSelectionIndex() == 4)
		{
			_criteria.setCrit0(null);
			ConfigItem ci = (ConfigItem) cp;
			String[] facetStrings = null;
			if (ci.getPos().equals("type")) //$NON-NLS-1$
			{
				try
				{
					facetStrings = _mainSearcher.getFacets(
							"tagging", ci.getParent().getValue().substring(5), ci.getValue(), null, //$NON-NLS-1$
							null);
					setQueryFacets(_pdrQuery, facetStrings);
				}
				catch (Exception e1)
				{
					e1.printStackTrace();
				}
			}
			else if (ci.getPos().equals("subtype")) //$NON-NLS-1$
			{
				try
				{
					facetStrings = _mainSearcher.getFacets("tagging", ((ConfigItem) ci.getParent()).getParent()
							.getValue().substring(5), ci.getParent().getValue(), ci.getValue(), //$NON-NLS-1$
							null);
					setQueryFacets(_pdrQuery, facetStrings);
				}
				catch (Exception e1)
				{
					e1.printStackTrace();
				}
			}
			else if (ci.getPos().equals("role")) //$NON-NLS-1$
			{
				try
				{
					facetStrings = _mainSearcher.getFacets("tagging", ((ConfigItem) ((ConfigItem) ci.getParent())
							.getParent()).getParent().getValue().substring(5), ((ConfigItem) ci.getParent())
							.getParent().getValue(), ci.getParent().getValue(), ci.getValue()); //$NON-NLS-1$
					setQueryFacets(_pdrQuery, facetStrings);
				}
				catch (Exception e1)
				{
					e1.printStackTrace();
				}
			}

			// try {
			// _pdrQuery.setFacets(_mainSearcher.getFacets("tagging", "name",
			// cp.getValue(), null, null));
			// } catch (XQException e) {
			// e.printStackTrace();
			// }
			_criteria.setCrit0("ALL"); //$NON-NLS-1$
			_criteria.setSearchText(_searchText.getText());
			_pdrQuery.setCriterias(new Vector<Criteria>(1));
			_pdrQuery.getCriterias().add(_criteria);
			// setQueryFacets(_pdrQuery, facetStrings);

		}
		else if (_pdrQuery.getType() == 5)
		{
			_criteria.setCrit0(null);
			ConfigItem ci = (ConfigItem) cp;
			String type = null;
			String[] referenceFacets = null;
			if (ci.getValue().equals("name") //$NON-NLS-1$
					|| ci.getValue().equals("namePart")) //$NON-NLS-1$
			{
				ci.setValue("namePart"); //$NON-NLS-1$
				type = "family"; //$NON-NLS-1$
			}
			try
			{
				referenceFacets = _mainSearcher.getFacets("reference", ci.getValue(), type, null, null); //$NON-NLS-1$
			}
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (ci.getValue().equals("genre")) //$NON-NLS-1$
			{
				_pdrQuery.setKey("genre"); //$NON-NLS-1$

			}
			else
			{
				_pdrQuery.setKey(null);
			}
			_pdrQuery.setCriterias(new Vector<Criteria>(1));
			_pdrQuery.getCriterias().add(_criteria);
			setQueryFacets(_pdrQuery, referenceFacets);
		}

		_keyCounter++;
		String key = _comboTree.getItem(_comboTree.getSelectionIndex()) + " " + _keyCounter;

		createTreeViewer(_tabFolderLeft, key);
		String message = "Performing Search..."; //$NON-NLS-1$

		setStatusLine(message);

	}

	/**
	 * Search and build tree.
	 * @param pdrQuery the pdr query
	 */
	public final void searchAndBuildTree(final PdrQuery pdrQuery)
	{
		this._pdrQuery = pdrQuery;
		_keyCounter++;
		String key = _comboTree.getItem(_pdrQuery.getType()) + " " + _keyCounter; //$NON-NLS-1$
		// String key = new String(new Integer(_pdrQuery.getType()) + " " +
		// keyCounter);

		// _pdrQuery.setKey(key);

		createTreeViewer(_tabFolderLeft, key);
	}

	// //////////////////////// Observer //////////////////////////////////////

	@Override
	public void selectionChanged(final IWorkbenchPart part, final ISelection selection)
	{
	}

	/**
	 * Select tree.
	 * @param treeName the tree name
	 */
	public final void selectTree(final String treeName)
	{
		if (_treeViewerMap.containsKey(treeName))
		{
			TreeViewer treeViewer;
			CTabItem tabItem;
			treeViewer = _treeViewerMap.get(treeName);
			tabItem = (CTabItem) treeViewer.getData("tab");
			_tabFolderLeft.setSelection(tabItem);
		}

	}

	@Override
	public final void setFocus()
	{
		_treeButton.setFocus();

	}

	/**
	 * Sets the pdr query.
	 * @param pdrQuery the new pdr query
	 */
	public final void setPdrQuery(final PdrQuery pdrQuery)
	{
		this._pdrQuery = pdrQuery;
	}

	/**
	 * Sets the query facets.
	 * @param facetQuery the facet query
	 * @param facetStrings the facet strings
	 */
	protected final void setQueryFacets(final PdrQuery facetQuery, final String[] facetStrings)
	{
		HashMap<String, IAEPresentable> facets = new HashMap<String, IAEPresentable>(facetStrings.length);
		for (String str : facetStrings)
		{
			ConfigItem ci = new ConfigItem();
			if (facetQuery.getKey() != null && facetQuery.getKey().equals("genre")) //$NON-NLS-1$
			{
				ReferenceModsTemplate temp = _facade.getReferenceModsTemplates().get(str);
				if (temp != null)
				{
					ci.setLabel(temp.getLabel());
				}
				else
				{
					ci.setLabel(str);
				}
			}
			else
			{
				ci.setLabel(str);
			}
			ci.setValue(str);
			facets.put(str, ci);
			//			System.out.println("facet " + str); //$NON-NLS-1$
		}
		facetQuery.setFacets(facets);
	}

	@Override
	public void setSelection(final ISelection selection)
	{
		// TODO Auto-generated method stub

	}

	/**
	 * Sets the status line.
	 * @param message the new status line
	 */
	private void setStatusLine(final String message)
	{
		// Get the status line and set the text
		// if (message.trim().length() == 0)
		// {
		// Integer i = null;
		// System.out.println(i.doubleValue());
		// }
		// System.out.println("setStatusLine" + message);
		IActionBars bars = getViewSite().getActionBars();
		bars.getStatusLineManager().setMessage(message);
	}

	/**
	 * method shows currently selected person in upper left group.
	 */
	private void showCurrentPdrObject()

	{
		// delete old content
		_currentObjectText.setText(""); //$NON-NLS-1$
		String label = ""; //$NON-NLS-1$
		if (_facade.getCurrentTreeObjects() != null && _facade.getCurrentTreeObjects().length > 0)
		{
			IStatus slp = new Status(IStatus.INFO, Activator.PLUGIN_ID, "TreeView current object: " //$NON-NLS-1$
					+ _facade.getCurrentTreeObjects()[_facade.getCurrentTreeObjects().length - 1].getPdrId().toString());
			_iLogger.log(slp);

			if (_advanced)
			{
				_addAspectButton.setEnabled(true);
				if (!_facade.getLastAspects().isEmpty())
				{
					_addSimilarAspectButton.setEnabled(true);
				}
			}
		}
		else
		{
			if (_advanced)
			{
				_addAspectButton.setEnabled(false);
				_addSimilarAspectButton.setEnabled(false);
			}
		}

		if (_facade.getCurrentTreeObjects() != null && _facade.getCurrentTreeObjects().length > 0
				&& _facade.getCurrentTreeObjects()[_facade.getCurrentTreeObjects().length - 1] != null)
		{
			if (_facade.getCurrentTreeObjects()[_facade.getCurrentTreeObjects().length - 1] instanceof Person)
			{
				Person cp = (Person) _facade.getCurrentTreeObjects()[_facade.getCurrentTreeObjects().length - 1];
				// System.out.println("currentPerson: " +
				// Facade.getInstanz().getCurrentPerson());
				/* set Text to newly selected person */
				label = ""; //$NON-NLS-1$
				if (_advanced)
				{
					_addAspectButton.setEnabled(true);
					if (_facade.getCurrentAspect() != null)
					{
						_addSimilarAspectButton.setEnabled(true);
					}
				}
				if (cp.getBasicPersonData() != null)
				{
					BasicPersonData basicPersonData = cp.getBasicPersonData();
					label = label + cp.getDisplayName(); //$NON-NLS-1$
					if (!Platform.getPreferencesService().getBoolean(CommonActivator.PLUGIN_ID,
							"PERSON_DISPLAYNAME_LIFESPAN", false, null))
					{
						if (basicPersonData.getBeginningOfLife() != null && 
								basicPersonData.getBeginningOfLife().getYear() != 0 
								&& basicPersonData.getEndOfLife() != null
								&& basicPersonData.getEndOfLife().getYear() != 0 )
						{
							label += " (" + basicPersonData.getBeginningOfLife().toString(".") + " - "
									+ basicPersonData.getEndOfLife().toString(".") + ")";
						}
						else if (basicPersonData.getBeginningOfLife() != null && 
								basicPersonData.getBeginningOfLife().getYear() != 0 )
						{
							label += " (" + basicPersonData.getBeginningOfLife().toString(".") + "-)";
						}
						else if (basicPersonData.getEndOfLife() != null && basicPersonData.getEndOfLife().getYear() != 0 )
						{
							label += " (-" + basicPersonData.getEndOfLife().toString(".") + ")";
						}
					}
					if (cp.getPdrId() != null)
					{
						label = label + "\n\nID: " + cp.getPdrId().toString(); //$NON-NLS-1$
					}
					if (basicPersonData.getComplexNames().size() > 1)
					{
						label = label + "\n\n" + NLMessages.getString("Treeview_namevariants"); //$NON-NLS-1$ //$NON-NLS-2$
						Vector<String> helpNameVariants = new Vector<String>(5);

						for (int i = 1; i < basicPersonData.getComplexNames().size(); i++)
						{
							String name = basicPersonData.getComplexNames().get(i).toString();
							if (!helpNameVariants.contains(name))
							{
								helpNameVariants.add(name);
							}
						}
						for (String n : helpNameVariants)
						{
							label += n + "\n";
						}
					}
					if (!Platform.getPreferencesService().getBoolean(CommonActivator.PLUGIN_ID,
							"PERSON_DISPLAYNAME_DESCRIPTION", false, null))
					{
						if (basicPersonData.getDescriptions() != null && !basicPersonData.getDescriptions().isEmpty())
						{
							label = label + "\n\n"
									+ PDRConfigProvider.getSemanticLabel(null, "principalDescription") + ":"; //$NON-NLS-1$ //$NON-NLS-3$
							boolean first = true;
							for (String s : basicPersonData.getDescriptions())
							{
								if (first)
								{
									label += " ";
								}
								else
								{
									label += ", ";
								}
								label += s; //$NON-NLS-1$
							}
						}
					}
				}
				else
				{
					label = label + "\n\nID: " + cp.getPdrId().toString(); //$NON-NLS-1$
				}
			}
			else if (_facade.getCurrentTreeObjects()[_facade.getCurrentTreeObjects().length - 1] instanceof ReferenceMods)
			{
				ReferenceMods cr = (ReferenceMods) _facade.getCurrentTreeObjects()[_facade.getCurrentTreeObjects().length - 1];
				if (_advanced)
				{
					_addAspectButton.setEnabled(true);
					_addSimilarAspectButton.setEnabled(true);
				}
				label = NLMessages.getString("Treeview_currentlySelectedRef"); //$NON-NLS-1$
				label += cr.getDisplayNameLong();
			}
			else if (_facade.getCurrentTreeObjects()[_facade.getCurrentTreeObjects().length - 1] instanceof Aspect)
			{
				Aspect ca = (Aspect) _facade.getCurrentTreeObjects()[_facade.getCurrentTreeObjects().length - 1];
				if (_advanced)
				{
					_addAspectButton.setEnabled(false);
					_addSimilarAspectButton.setEnabled(false);
				}
				label = NLMessages.getString("Treeview_currentlySelectedA"); //$NON-NLS-1$
				label += "ID: " + _facade.getCurrentAspect().getPdrId().toString() + "\n\n"; //$NON-NLS-1$ //$NON-NLS-2$
				label += ca.getNotification();
			}
		}
		_currentObjectText.setText(label);


	}

	/**
	 * Uncheck toggle commands.
	 */
	private void uncheckToggleCommands()
	{
		ICommandService cService = (ICommandService) PlatformUI.getWorkbench().getService(ICommandService.class);
		Command command;
		State tState;

		command = cService.getCommand("org.bbaw.pdr.ae.view.main.commands.FilterOnlyAspectsDivergentMarkup");
		tState = command.getState(AEPluginIDs.TOGGLE_STATE_FILTERONLYASPECTSDIVERGENTMARKUP);
		if ((Boolean) tState.getValue())
		{
			IHandlerService handlerService = (IHandlerService) PlatformUI.getWorkbench().getService(
					IHandlerService.class);
			try
			{
				handlerService.executeCommand(
						"org.bbaw.pdr.ae.view.main.commands.FilterOnlyAspectsDivergentMarkup", null); //$NON-NLS-1$
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
		command = cService.getCommand("org.bbaw.pdr.ae.view.main.commands.FilterOnlyIncorrectObjects");
		tState = command.getState(AEPluginIDs.TOGGLE_STATE_FILTERONLYINCORRECTOBJECTS);
		if ((Boolean) tState.getValue())
		{
			IHandlerService handlerService = (IHandlerService) PlatformUI.getWorkbench().getService(
					IHandlerService.class);
			try
			{
				handlerService.executeCommand("org.bbaw.pdr.ae.view.main.commands.FilterOnlyIncorrectObjects", null); //$NON-NLS-1$
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
		command = cService.getCommand("org.bbaw.pdr.ae.view.main.commands.FilterOnlyNewObjects");
		tState = command.getState(AEPluginIDs.TOGGLE_STATE_FILTERONLYNEWOBJECTS);
		if ((Boolean) tState.getValue())
		{
			IHandlerService handlerService = (IHandlerService) PlatformUI.getWorkbench().getService(
					IHandlerService.class);
			try
			{
				handlerService.executeCommand("org.bbaw.pdr.ae.view.main.commands.FilterOnlyNewObjects", null); //$NON-NLS-1$
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
		command = cService.getCommand("org.bbaw.pdr.ae.view.main.commands.FilterOnlyPersonConcurrences");
		tState = command.getState(AEPluginIDs.TOGGLE_STATE_FILTERONLYPERSONCONCURRENCES);
		if ((Boolean) tState.getValue())
		{
			IHandlerService handlerService = (IHandlerService) PlatformUI.getWorkbench().getService(
					IHandlerService.class);
			try
			{
				handlerService.executeCommand("org.bbaw.pdr.ae.view.main.commands.FilterOnlyPersonConcurrences", null); //$NON-NLS-1$
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
		command = cService.getCommand("org.bbaw.pdr.ae.view.main.commands.FilterOnlyPersonIdentifiers");
		tState = command.getState(AEPluginIDs.TOGGLE_STATE_FILTERONLYPERSONIDENTIFIERS);
		if ((Boolean) tState.getValue())
		{
			IHandlerService handlerService = (IHandlerService) PlatformUI.getWorkbench().getService(
					IHandlerService.class);
			try
			{
				handlerService.executeCommand("org.bbaw.pdr.ae.view.main.commands.FilterOnlyPersonIdentifiers", null); //$NON-NLS-1$
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
		command = cService.getCommand("org.bbaw.pdr.ae.view.main.commands.FilterOnlyUpdatedObjects");
		tState = command.getState(AEPluginIDs.TOGGLE_STATE_FILTERONLYUPDATEDOBJECTS);
		if ((Boolean) tState.getValue())
		{
			IHandlerService handlerService = (IHandlerService) PlatformUI.getWorkbench().getService(
					IHandlerService.class);
			try
			{
				handlerService.executeCommand("org.bbaw.pdr.ae.view.main.commands.FilterOnlyUpdatedObjects", null); //$NON-NLS-1$
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
		command = cService.getCommand("org.bbaw.pdr.ae.view.main.commands.FilterOnlyWithoutICCUPersons");
		tState = command.getState(AEPluginIDs.TOGGLE_STATE_FILTERONLYWITHOUTICCUPERSONS);
		if ((Boolean) tState.getValue())
		{
			IHandlerService handlerService = (IHandlerService) PlatformUI.getWorkbench().getService(
					IHandlerService.class);
			try
			{
				handlerService.executeCommand("org.bbaw.pdr.ae.view.main.commands.FilterOnlyWithoutICCUPersons", null); //$NON-NLS-1$
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
		command = cService.getCommand("org.bbaw.pdr.ae.view.main.commands.FilterOnlyWithoutLCCNPersons");
		tState = command.getState(AEPluginIDs.TOGGLE_STATE_FILTERONLYWITHOUTLCCNPERSONS);
		if ((Boolean) tState.getValue())
		{
			IHandlerService handlerService = (IHandlerService) PlatformUI.getWorkbench().getService(
					IHandlerService.class);
			try
			{
				handlerService.executeCommand("org.bbaw.pdr.ae.view.main.commands.FilterOnlyWithoutLCCNPersons", null); //$NON-NLS-1$
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
		command = cService.getCommand("org.bbaw.pdr.ae.view.main.commands.FilterOnlyWithoutPNDPersons");
		tState = command.getState(AEPluginIDs.TOGGLE_STATE_FILTERONLYWITHOUTPNDPERSONS);
		if ((Boolean) tState.getValue())
		{
			IHandlerService handlerService = (IHandlerService) PlatformUI.getWorkbench().getService(
					IHandlerService.class);
			try
			{
				handlerService.executeCommand("org.bbaw.pdr.ae.view.main.commands.FilterOnlyWithoutPNDPersons", null); //$NON-NLS-1$
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
		command = cService.getCommand("org.bbaw.pdr.ae.view.main.commands.FilterOnlyWithoutVIAFPersons");
		tState = command.getState(AEPluginIDs.TOGGLE_STATE_FILTERONLYWITHOUTVIAFPERSONS);
		if ((Boolean) tState.getValue())
		{
			IHandlerService handlerService = (IHandlerService) PlatformUI.getWorkbench().getService(
					IHandlerService.class);
			try
			{
				handlerService.executeCommand("org.bbaw.pdr.ae.view.main.commands.FilterOnlyWithoutVIAFPersons", null); //$NON-NLS-1$
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

	@Override
	public final void update(final Observable o, final Object arg)
	{
		IStatus sAspect = new Status(IStatus.INFO, Activator.PLUGIN_ID, "TreeView update: " + arg); //$NON-NLS-1$
		_iLogger.log(sAspect);

		//        System.out.println("treeviewer notified"); //$NON-NLS-1$

		if (arg.equals("newAspect")) //$NON-NLS-1$ //$NON-NLS-2$
		{
			if (_facade.getCurrentAspect() != null)
			{
				if (_facade.getCurrentPerson() != null)
				{
					if (_advanced)
					{
						_addSimilarAspectButton.setEnabled(true);
					}
				}
				// _treeViewer.refresh();
			}
		}
		else if (arg.equals("newTreeObjects")) //$NON-NLS-1$
		{
			showCurrentPdrObject();
		}
		else if (arg.equals("noSelectedPerson")) //$NON-NLS-1$
		{

			_currentObjectText.setText(""); //$NON-NLS-1$

		}
		else if (arg.equals("newResultingPersons")) //$NON-NLS-1$
		{
			createTreeViewer(_tabFolderLeft, "Personen"); //$NON-NLS-1$

		}
		else if (arg.equals("newPersonTreeRequiered")) //$NON-NLS-1$
		{
			//    		createTreeViewer(_tabFolderLeft, "allPersons"); //$NON-NLS-1$
			Object[] elements = _allPersonsTreeViewer.getExpandedElements();
			_allPersonsTreeViewer.setInput(_facade.getAllPersons());
			for (Object obj : elements)
			{
				_allPersonsTreeViewer.setExpandedState(obj, true);
			}
			showCurrentPdrObject();

		}
		else if (arg.equals("newAdvancedQuery")) //$NON-NLS-1$
		{
			searchAndBuildTree(_facade.getAdvancedQuery());

		}
		else if (arg.equals("newNewPerson")) //$NON-NLS-1$
		{
			Object[] elements = _allPersonsTreeViewer.getExpandedElements();
			_allPersonsTreeViewer.refresh();
			for (Object obj : elements)
			{
				_allPersonsTreeViewer.setExpandedState(obj, true);
			}
			showCurrentPdrObject();

		}
		else if (arg.equals("newNewReference")) //$NON-NLS-1$
		{
			Object[] elements = _allReferencesTreeViewer.getExpandedElements();
			_allReferencesTreeViewer.setInput(_facade.getAllReferences());
			for (Object obj : elements)
			{
				_allReferencesTreeViewer.setExpandedState(obj, true);
			}
			showCurrentPdrObject();

		}
		else if (arg.equals("refreshAll")) //$NON-NLS-1$
		{
			//
			_allReferencesTreeViewer.refresh();
			_allPersonsTreeViewer.refresh();
			disposeTabItems();
			buildTabFolderLeft();
			if (_advanced)
			{
				_addSimilarAspectButton.setEnabled(false);
			}

			showCurrentPdrObject();

		}
		else if (arg.equals("allPersons")) //$NON-NLS-1$
		{
			_allPersonsTreeViewer.setInput(_facade.getAllPersons());
			showCurrentPdrObject();
		}
		else if (arg.equals("allReferences")) //$NON-NLS-1$
		{
			_allReferencesTreeViewer.setInput(_facade.getAllReferences());
			showCurrentPdrObject();
		}
		else if (arg.equals("newPersonFacetProposals")) //$NON-NLS-1$
		{
			if (_facade.getFacetProposals() != null && !_facade.getFacetProposals().isEmpty())
			{
				_comboSemanticViewer.setInput(_facade.getFacetProposals());
			}
			else
			{
				_comboSemanticViewer.setInput(null);
				MessageDialog messageDialog = new MessageDialog(_parentShell,
						NLMessages.getString("View_message_person_facet_empty"), null,
						NLMessages.getString("View_message_person_facet_customize"), MessageDialog.INFORMATION,
						new String[]
						{NLMessages.getString("View_message_customize_person_facet_proposals"),
								NLMessages.getString("Handler_cancel")}, 0);
				int returnCode = messageDialog.open();
				if (returnCode == 0)
				{
					openPreferencePage("org.bbaw.pdr.ae.view.main.preferences.pages.FacetedSearch");
					if (_facade.getFacetProposals() != null && !_facade.getFacetProposals().isEmpty())
					{
						_comboSemanticViewer.setInput(_facade.getFacetProposals());
					}
				}
			}
			_comboSemanticViewer.refresh();
		}
		else if (arg.equals("newAspectFacetProposals")) //$NON-NLS-1$
		{
			if (_facade.getAspectFacetProposals() != null && !_facade.getAspectFacetProposals().isEmpty())
			{
				_comboSemanticViewer.setInput(_facade.getAspectFacetProposals());
			}
			else
			{
				_comboSemanticViewer.setInput(null);
				MessageDialog messageDialog = new MessageDialog(_parentShell,
						NLMessages.getString("View_message_aspect_facet_empty"), null,
						NLMessages.getString("View_message_aspect_facet_customize"), MessageDialog.INFORMATION,
						new String[]
						{NLMessages.getString("View_message_customize_aspect_facet_proposals"),
								NLMessages.getString("Handler_cancel")}, 0);
				int returnCode = messageDialog.open();
				if (returnCode == 0)
				{
					openPreferencePage("org.bbaw.pdr.ae.view.main.preferences.pages.FacetedAspectSearch");
					if (_facade.getAspectFacetProposals() != null && !_facade.getAspectFacetProposals().isEmpty())
					{
						_comboSemanticViewer.setInput(_facade.getAspectFacetProposals());
					}
				}

			}
			_comboSemanticViewer.refresh();
		}
	}

	/* Multiple filters can be enabled at a time. */
	/**
	 * Update filter.
	 * @param action the action
	 */
	protected final void updateFilter(final Action action)
	{
		TreeViewer treeViewer = _treeViewerMap.get(_tabFolderLeft.getSelection().getData("key")); //$NON-NLS-1$
		if (action == _onlyUpdatedObjects)
		{
			if (action.isChecked())
			{
				treeViewer.addFilter(_treenodeUpdatedFilter);
			}
			else
			{
				treeViewer.removeFilter(_treenodeUpdatedFilter);
			}
		}
		else if (action == _onlyNewObjects)
		{
			if (action.isChecked())
			{
				treeViewer.addFilter(_treenodeNewFilter);
			}
			else
			{
				treeViewer.removeFilter(_treenodeNewFilter);
			}
		}
		else if (action == _onlyIncorrectObjects)
		{
			if (action.isChecked())
			{
				treeViewer.addFilter(_onlyIncorrectPDRObjects);
			}
			else
			{
				treeViewer.removeFilter(_onlyIncorrectPDRObjects);
			}
		}
		else if (action == _onlyAspectsWithDivergentMarkup)
		{
			if (action.isChecked())
			{
				treeViewer.addFilter(_onlyIAspectsDivergentMarkup);
			}
			else
			{
				treeViewer.removeFilter(_onlyIAspectsDivergentMarkup);
			}
		}

		else if (action == _onlyPersonConcurrences)
		{
			if (action.isChecked())
			{
				treeViewer.addFilter(_personConcurrenceFilter);
			}
			else
			{
				treeViewer.removeFilter(_personConcurrenceFilter);
			}
		}
		else if (action == _onlyPersonIdentifiers)
		{
			if (action.isChecked())
			{
				treeViewer.addFilter(_personIdentifierFilter);
			}
			else
			{
				treeViewer.removeFilter(_personIdentifierFilter);
			}
		}
		else if (action == _onlyWithoutPNDPersons)
		{
			if (action.isChecked())
			{
				treeViewer.addFilter(_personWithoutPNDFilter);
			}
			else
			{
				treeViewer.removeFilter(_personWithoutPNDFilter);
			}
		}
		else if (action == _onlyWithoutLCCNPersons)
		{
			if (action.isChecked())
			{
				treeViewer.addFilter(_personWithoutLCCNFilter);
			}
			else
			{
				treeViewer.removeFilter(_personWithoutLCCNFilter);
			}
		}
		else if (action == _onlyWithoutVIAFPersons)
		{
			if (action.isChecked())
			{
				treeViewer.addFilter(_personWithoutVIAFFilter);
			}
			else
			{
				treeViewer.removeFilter(_personWithoutVIAFFilter);
			}
		}
		else if (action == _onlyWithoutICCUPersons)
		{
			if (action.isChecked())
			{
				treeViewer.addFilter(_personWithoutICCUFilter);
			}
			else
			{
				treeViewer.removeFilter(_personWithoutICCUFilter);
			}
		}
	}

	public PdrObject[] getInputOfTree(String treeName, boolean filtered)
	{
		if (_treeViewerMap.containsKey(treeName))
		{
			TreeViewer treeViewer;
			treeViewer = _treeViewerMap.get(treeName);
			Object obj = treeViewer.getInput();
			ArrayList<PdrObject> inputObjects = new ArrayList<PdrObject>();
			if (obj instanceof HashMap<?, ?>)
			{
				@SuppressWarnings("unchecked")
				HashMap<PdrId, PdrObject> inputs = (HashMap<PdrId, PdrObject>) obj;
				ViewerFilter[] filters = treeViewer.getFilters();
				for (PdrId id : inputs.keySet())
				{
					PdrObject po = inputs.get(id);
					if (filtered && filters != null && filters.length > 0)
					{
						for (ViewerFilter filter : filters)
						{
							if (filter.select(treeViewer, null, po))
							{
								inputObjects.add(po);
							}
						}
					}
					else
					{
						inputObjects.add(po);
					}
				}
				return inputObjects.toArray(new PdrObject[inputObjects.size()]);
			}
			else if (obj instanceof Vector<?>)
			{
				@SuppressWarnings("unchecked")
				Vector<PdrObject> inputs = (Vector<PdrObject>) obj;
				ViewerFilter[] filters = treeViewer.getFilters();
				for (PdrObject po : inputs)
				{
					if (filtered && filters != null && filters.length > 0)
					{
						for (ViewerFilter filter : filters)
						{
							if (filter.select(treeViewer, null, po))
							{
								inputObjects.add(po);
							}
						}
					}
					else
					{
						inputObjects.add(po);
					}
				}
				return inputObjects.toArray(new PdrObject[inputObjects.size()]);
			 }
		}
		return null;
	}
}
