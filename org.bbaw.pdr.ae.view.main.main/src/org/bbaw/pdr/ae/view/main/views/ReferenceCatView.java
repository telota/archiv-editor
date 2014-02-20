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

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import org.bbaw.pdr.ae.common.AEConstants;
import org.bbaw.pdr.ae.common.CommonActivator;
import org.bbaw.pdr.ae.common.NLMessages;
import org.bbaw.pdr.ae.common.icons.IconsInternal;
import org.bbaw.pdr.ae.common.interfaces.AEFilter;
import org.bbaw.pdr.ae.control.comparator.ReferenceByAuthorTitleComparator;
import org.bbaw.pdr.ae.control.comparator.ReferenceByCreatorComparator;
import org.bbaw.pdr.ae.control.comparator.ReferenceByRecentChangesComparator;
import org.bbaw.pdr.ae.control.comparator.ReferenceByTitleComparator;
import org.bbaw.pdr.ae.control.comparator.ReferenceCronComparator;
import org.bbaw.pdr.ae.control.facade.Facade;
import org.bbaw.pdr.ae.control.facade.RightsChecker;
import org.bbaw.pdr.ae.metamodel.PdrId;
import org.bbaw.pdr.ae.model.Aspect;
import org.bbaw.pdr.ae.model.PdrObject;
import org.bbaw.pdr.ae.model.Person;
import org.bbaw.pdr.ae.model.ReferenceMods;
import org.bbaw.pdr.ae.model.view.OrderingHead;
import org.bbaw.pdr.ae.view.control.ControlExtensions;
import org.bbaw.pdr.ae.view.control.PDRObjectsProvider;
import org.bbaw.pdr.ae.view.control.PDROrdererFactory;
import org.bbaw.pdr.ae.view.control.ViewHelper;
import org.bbaw.pdr.ae.view.control.filters.OnlyNewPDRObjectsFilter;
import org.bbaw.pdr.ae.view.control.filters.OnlyUpdatedPDRObjectsFilter;
import org.bbaw.pdr.ae.view.control.interfaces.IReferencePresentation;
import org.bbaw.pdr.ae.view.main.dialogs.FilterSelectionDialog;
import org.bbaw.pdr.ae.view.main.internal.Activator;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
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
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.part.ViewPart;

/**
 * This ViewClass creates the ReferenceCatView of the Aspects of the current
 * Person. it extends AbstractCatView. The List of Categories is still static
 * and based upon the List given in the properties file. ReferenceCatView .
 * @author Christoph Plutte
 */
public class ReferenceCatView extends ViewPart implements ISelectionListener, Observer, ISelectionProvider
{

	/** The concurring. */
	private boolean _concurring = false;

	/** The additional. */
	private boolean _additional = false;

	/** The all references. */
	private Text _allReferences;

	private ReferenceMods _selectedReference;

	/** The order combo. */
	private Combo _orderCombo;

	/** The order button. */
	private Button _orderButton;

	/** The sorted combo. */
	private Combo _sortedCombo;

	/** The sort button desc. */
	private Button _sortButtonDesc;

	/** The sort button asc. */
	private Button _sortButtonAsc;

	/** The current ref category id. */
	private int _currentRefCategoryId;

	/** The semantic filter action. */
	private Action _openAspectsInNewTap, _openReferencesInNewTap, _onlyNewReferencesAction,
			_onlyUpdatedReferencesAction, _userFilterAction, _semanticFilterAction;

	/** The only updated references filter. */
	private AEFilter _onlyNewReferencesFilter, _onlyUpdatedReferencesFilter;



	/** The root menu manager. */
	private IMenuManager _rootMenuManager;


	/** The references. */
	private Vector<ReferenceMods> _references;

	/** The _main composite. */
	private Composite _mainComposite;

	/** The _tab folder ref. */
	private CTabFolder _tabFolderRef;

	/** The parent shell. */
	private Shell _parentShell;

	// private CTabItem selectetTi;

	/** hashmap for tabitems and semantic categories in category view. */
	private HashMap<Integer, CTabItem> _tabMap = new HashMap<Integer, CTabItem>(AEConstants.MAX_NUMBER_CATEGORIES);

	/**
	 * id of category view.
	 */
	public static final String ID = "org.bbaw.pdr.ae.view.main.views.ReferenceCatView"; //$NON-NLS-1$

	/** The _current objects. */
	private PdrObject[] _currentObjects;
	/** __facade singleton instance. */
	private Facade _facade = Facade.getInstanz();

	/** Instance of shared image registry. */
	private ImageRegistry _imageReg = CommonActivator.getDefault().getImageRegistry();


	/** Logger. */
	private static ILog iLogger = AEConstants.ILOGGER;

	/** The pdr objects provider. */
	private PDRObjectsProvider _pdrObjectsProvider = new PDRObjectsProvider();

	/** The _orderer factory. */
	private PDROrdererFactory _ordererFactory = new PDROrdererFactory();

	private boolean _advanced = Platform.getPreferencesService().getBoolean(CommonActivator.PLUGIN_ID,
			"AE_ADVANCED_VERSION", AEConstants.AE_ADVANCED_VERSION, null);

	/**
	 * constructor.
	 */
	public ReferenceCatView()
	{

	}

	@Override
	public void addSelectionChangedListener(ISelectionChangedListener listener)
	{
		// TODO Auto-generated method stub

	}

	/**
	 * method to build Categories.
	 * @param pdrObjects the currently selected PDRObjects
	 * @param savedReference the just saved reference or null
	 */
	public final void buildCategories(final PdrObject[] pdrObjects, final ReferenceMods savedReference)
	{
		_currentObjects = pdrObjects;

		if (_tabMap != null)
		{
			_tabMap.clear();
			_tabMap = null;
		}

		_tabFolderRef = new CTabFolder(_mainComposite, SWT.NONE);
		_tabFolderRef.setLayoutData(new GridData());
		((GridData) _tabFolderRef.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _tabFolderRef.getLayoutData()).verticalAlignment = SWT.FILL;
		((GridData) _tabFolderRef.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) _tabFolderRef.getLayoutData()).grabExcessVerticalSpace = true;
		_tabMap = new HashMap<Integer, CTabItem>(AEConstants.MAX_NUMBER_CATEGORIES);
		ScrolledComposite scrollComp;
		Composite contentComp;
		GridLayout layout;
		CTabItem tabItem;
		final Vector<OrderingHead> groupedReferences = _pdrObjectsProvider.getArrangedReferences();

		if (_advanced)
		{
			if (_currentObjects != null)
			{
				_allReferences.setText("" + _pdrObjectsProvider.getNumberOfReferences()); //$NON-NLS-1$
				_sortedCombo.setEnabled(true);
				_orderButton.setEnabled(true);
				_orderCombo.setEnabled(true);
			}
		}
		int start = 0;
		if (groupedReferences != null && !groupedReferences.isEmpty())
		{
			for (int i = 0; i < groupedReferences.size(); i++)
			{
				if (groupedReferences.get(i).getReferences() != null
						&& !groupedReferences.get(i).getReferences().isEmpty())
				{
					tabItem = new CTabItem(_tabFolderRef, SWT.NONE);
					tabItem.setText(groupedReferences.get(i).getLabel());

					// System.out.println(tabItem);

					tabItem.setData("id", groupedReferences.get(i).getValue()); //$NON-NLS-1$
					tabItem.setData("nr", i); //$NON-NLS-1$
					tabItem.setData("loaded", "false"); //$NON-NLS-1$ //$NON-NLS-2$

					tabItem.setImage(_imageReg.get(groupedReferences.get(i).getImageString()));

					_tabMap.put(i, tabItem);

					// System.out.println(tabMap.get(i));
					// System.out.println("tabmap hast mapping für "
					//					+ i + " ??: " + tabMap.containsKey(i)); //$NON-NLS-1$ //$NON-NLS-2$

					scrollComp = new ScrolledComposite(_tabFolderRef, SWT.V_SCROLL | SWT.H_SCROLL);
					scrollComp.setExpandHorizontal(true);
					scrollComp.setExpandVertical(true);
					scrollComp.setMinSize(SWT.DEFAULT, SWT.DEFAULT);
					ViewHelper.accelerateScrollbar(scrollComp, 20);
					layout = new GridLayout();
					layout.numColumns = 1;
					layout.verticalSpacing = 2;
					scrollComp.setLayout(layout);
					contentComp = new Composite(scrollComp, SWT.NONE);
					contentComp.setLayout(layout);
					scrollComp.setContent(contentComp);
					// contentComp.setLayoutData(new
					// GridData(GridData.FILL_BOTH));
					tabItem.setControl(scrollComp);

				} // for-loop
			}
			if (savedReference != null)
			{
				_currentRefCategoryId = 0;
				_tabFolderRef.setSelection(0);

				Vector<Aspect> as = groupedReferences.get(0).getAspects();
				for (int k = 0; k < as.size(); k++)
				{
					if (as.get(k).getPdrId().equals(savedReference.getPdrId()))
					{
						start = k;
						break;
					}
				}
			}
			_tabFolderRef.setSelection(0);
			_currentRefCategoryId = 0;

			_tabFolderRef.addSelectionListener(new SelectionAdapter()
			{
				@Override
				public void widgetSelected(final SelectionEvent se)
				{
					IStatus stabfolder = new Status(IStatus.INFO, Activator.PLUGIN_ID,
							"ReferenceCatView tab item selected: " //$NON-NLS-1$
									+ _tabFolderRef.getSelection().getText());
					iLogger.log(stabfolder);

					_currentRefCategoryId = _tabFolderRef.getSelectionIndex();
					IStatus sCat = new Status(IStatus.INFO, Activator.PLUGIN_ID, "ReferenceCatView current Category: " //$NON-NLS-1$
							+ _currentRefCategoryId);
					iLogger.log(sCat);

					// selectetTi =
					// _tabFolderRef.getItem(_tabFolderRef.getSelectionIndex());

					if (_tabFolderRef.getSelectionIndex() != 0
							&& !_tabMap.get(_currentRefCategoryId).getData("loaded").equals("true")) //$NON-NLS-1$ //$NON-NLS-2$
					{
						loadReferences(groupedReferences, _currentRefCategoryId, 0, 10, null);
					}
				}
			}); // SelectionListener
			_tabFolderRef.setSelection(0);

			if (_tabMap.containsKey(_currentRefCategoryId))
			{
				loadReferences(groupedReferences, _currentRefCategoryId, start, 10, savedReference);
			}
			else if (_tabMap.containsKey(0))
			{
				loadReferences(groupedReferences, 0, start, 10, savedReference);
			}

			// loadReferences(0, 0, 10);
		}
		else
		{
			//    		System.out.println("no categories"); //$NON-NLS-1$
			Label noCatLabel = new Label(_tabFolderRef, SWT.None);
			noCatLabel.setText(NLMessages.getString("View_message_no_references_found"));
			noCatLabel.pack();
		}
		_tabFolderRef.layout();
		_tabFolderRef.pack();
		_mainComposite.layout();
	} // buildCategories

	/**
	 * Creates the actions.
	 */
	private void createActions()
	{
		_onlyNewReferencesAction = new Action(NLMessages.getString("View_action_only_new_references"))
		{
			@Override
			public void run()
			{
				updateFilter(_onlyNewReferencesAction);
			}
		};
		_onlyNewReferencesAction.setChecked(false);
		_onlyNewReferencesAction.setEnabled(false);
		_onlyNewReferencesAction.setImageDescriptor(_imageReg.getDescriptor(IconsInternal.DECORATION_NEW));

		_onlyUpdatedReferencesAction = new Action(NLMessages.getString("View_action_only_updated_references"))
		{
			@Override
			public void run()
			{
				updateFilter(_onlyUpdatedReferencesAction);
			}
		};
		_onlyUpdatedReferencesAction.setChecked(false);
		_onlyUpdatedReferencesAction.setEnabled(false);
		_onlyUpdatedReferencesAction.setImageDescriptor(_imageReg.getDescriptor(IconsInternal.DECORATION_UPDATED));

		_semanticFilterAction = new Action(NLMessages.getString("View_action_semantic_filter"))
		{
			@Override
			public void run()
			{
				IWorkbench workbench = PlatformUI.getWorkbench();
				Display display = workbench.getDisplay();
				Shell shell = new Shell(display);
				FilterSelectionDialog dialog = new FilterSelectionDialog(shell, _pdrObjectsProvider, "semantic"); //$NON-NLS-1$
				dialog.open();
				if (dialog.getReturnCode() == 0)
				{
					disposeTabItems();
					buildCategories(_currentObjects, null);
				}
				_semanticFilterAction.setChecked(_pdrObjectsProvider.hasSemanticFilter());
			}
		};
		_semanticFilterAction.setChecked(false);
		_semanticFilterAction.setEnabled(false);
		_semanticFilterAction.setImageDescriptor(_imageReg.getDescriptor(IconsInternal.CLASSIFICATIONS));

		_userFilterAction = new Action(NLMessages.getString("View_action_user_filter"))
		{
			@Override
			public void run()
			{
				//				System.out.println("open user filter"); //$NON-NLS-1$
				IWorkbench workbench = PlatformUI.getWorkbench();
				Display display = workbench.getDisplay();
				Shell shell = new Shell(display);
				FilterSelectionDialog dialog = new FilterSelectionDialog(shell, _pdrObjectsProvider, "userRef"); //$NON-NLS-1$
				dialog.open();
				if (dialog.getReturnCode() == 0)
				{
					disposeTabItems();
					buildCategories(_currentObjects, null);
				}
				_userFilterAction.setChecked(_pdrObjectsProvider.hasUserFilter());
			}
		};
		_userFilterAction.setChecked(false);
		_userFilterAction.setEnabled(false);
		_userFilterAction.setImageDescriptor(_imageReg.getDescriptor(IconsInternal.USER));

	}

	/**
	 * Creates the context menu.
	 * @param control the control
	 */
	protected final void createContextMenu(final Control control)
	{
		/* Context Menu */
		// Menu contextMenu = new Menu(stext);
		MenuManager menuMgr = new MenuManager();
		Menu contextMenu = menuMgr.createContextMenu(control);
		SelectionAdapter contextMenuSelectionListener = new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent event)
			{
				MenuItem source = (MenuItem) event.getSource();
				if ("".equals(source.getText())) //$NON-NLS-1$
				{
					_facade.setCurrentCopiedAspect();
				}


			}
		};
		menuMgr.add(_openAspectsInNewTap);
		menuMgr.add(_openReferencesInNewTap);
		menuMgr.add(new Separator());
		MenuItem menuOpenAspects = new MenuItem(contextMenu, SWT.PUSH);
		menuOpenAspects.setText(""); //$NON-NLS-1$
		menuOpenAspects.addSelectionListener(contextMenuSelectionListener);

		MenuItem menuItemCopyAspect = new MenuItem(contextMenu, SWT.PUSH);
		menuItemCopyAspect.setText(""); //$NON-NLS-1$
		menuItemCopyAspect.addSelectionListener(contextMenuSelectionListener);
		MenuItem menuItemCopyText = new MenuItem(contextMenu, SWT.PUSH);
		menuItemCopyText.setText(NLMessages.getString("View_copyText")); //$NON-NLS-1$
		menuItemCopyText.addSelectionListener(contextMenuSelectionListener);
		control.setMenu(contextMenu);

		getSite().registerContextMenu(menuMgr, ReferenceCatView.this);
	}

	/**
	 * Creates the context menu action.
	 */
	private void createContextMenuAction()
	{
		_openAspectsInNewTap = new Action(NLMessages.getString("View_action_open_aspects_new_tab"))
		{
			@Override
			public void run()
			{
				ReferenceMods r = _facade.getCurrentReference();
				if (r != null && r.getPdrId() != null)
				{
					Event event = new Event();
					event.data = r.getPdrId().toString();
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

				ReferenceMods r = _facade.getCurrentReference();
				if (r != null && r.getPdrId() != null)
				{
					Event event = new Event();
					event.data = r.getPdrId().toString();
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

				_openReferencesInNewTap.setChecked(false);

			}
		};
		_openReferencesInNewTap.setChecked(false);
		_openReferencesInNewTap.setImageDescriptor(_imageReg.getDescriptor(IconsInternal.REFERENCES));
	}

	/**
	 * Creates the filters.
	 */
	private void createFilters()
	{
		// COMEBACK createFiltersAndSorters
		_onlyNewReferencesFilter = new OnlyNewPDRObjectsFilter();
		_onlyUpdatedReferencesFilter = new OnlyUpdatedPDRObjectsFilter();
	}

	/**
	 * Creates the menus.
	 */
	private void createMenus()
	{
		_rootMenuManager = getViewSite().getActionBars().getMenuManager();
		_rootMenuManager.setRemoveAllWhenShown(true);
		_rootMenuManager.addMenuListener(new IMenuListener()
		{
			@Override
			public void menuAboutToShow(final IMenuManager mgr)
			{
				fillMenu(mgr);
			}
		});
		fillMenu(_rootMenuManager);

		getViewSite().getActionBars().getToolBarManager().add(_onlyNewReferencesAction);
		getViewSite().getActionBars().getToolBarManager().add(_userFilterAction);
		getViewSite().getActionBars().getToolBarManager().add(_semanticFilterAction);
		getViewSite().getActionBars().updateActionBars();
	}

	@Override
	public final void createPartControl(final Composite parent)
	{
		String secId = this.getViewSite().getSecondaryId();
		if (secId != null)
		{
			_concurring = (secId.equals("b")); //$NON-NLS-1$
		}
		if (_mainComposite == null)
		{
			_mainComposite = new Composite(parent, SWT.NULL);
			_mainComposite.setLayoutData(new GridData());
			((GridData) _mainComposite.getLayoutData()).horizontalAlignment = SWT.FILL;
			((GridData) _mainComposite.getLayoutData()).verticalAlignment = SWT.FILL;
			((GridData) _mainComposite.getLayoutData()).grabExcessHorizontalSpace = true;
			((GridData) _mainComposite.getLayoutData()).grabExcessVerticalSpace = true;
			_mainComposite.setLayout(new GridLayout());
			_mainComposite.layout();
			_mainComposite.pack();
		}
		if (_advanced)
		{
			Composite topComposite = new Composite(_mainComposite, SWT.NONE);
			topComposite.setLayoutData(new GridData());
			((GridData) topComposite.getLayoutData()).horizontalAlignment = SWT.FILL;
			((GridData) topComposite.getLayoutData()).grabExcessHorizontalSpace = true;
			((GridData) topComposite.getLayoutData()).widthHint = 20;
			topComposite.setLayout(new RowLayout());

			Composite alltitleComp = new Composite(topComposite, SWT.NONE);
			alltitleComp.setLayout(new GridLayout());
			((GridLayout) alltitleComp.getLayout()).numColumns = 2;
			((GridLayout) alltitleComp.getLayout()).marginHeight = 6;
			((GridLayout) alltitleComp.getLayout()).marginWidth = 0;

			Label all = new Label(alltitleComp, SWT.NONE);
			all.setText(NLMessages.getString("View_number_of_references"));
			_allReferences = new Text(alltitleComp, SWT.NONE | SWT.READ_ONLY);
			_allReferences.setLayoutData(new GridData());
			((GridData) _allReferences.getLayoutData()).widthHint = 40;
			Composite ordertitleComp = new Composite(topComposite, SWT.NONE);
			ordertitleComp.setLayout(new GridLayout());
			((GridLayout) ordertitleComp.getLayout()).numColumns = 3;
			((GridLayout) ordertitleComp.getLayout()).marginHeight = 0;
			((GridLayout) ordertitleComp.getLayout()).marginWidth = 0;

			Label order = new Label(ordertitleComp, SWT.NONE);
			order.setText(NLMessages.getString("View_group_by"));
			_orderCombo = new Combo(ordertitleComp, SWT.NONE | SWT.READ_ONLY);
			String[] oderers = new String[]
			{NLMessages.getString("View_group_all"), NLMessages.getString("View_group_aspect_semantic"),
					NLMessages.getString("View_group_aspect_year"), NLMessages.getString("View_group_aspect_place"),
					NLMessages.getString("View_group_aspect_reference"),
					NLMessages.getString("View_group_aspect_relation"),
					NLMessages.getString("View_group_aspect_markup"), NLMessages.getString("View_group_aspect_person"),
					NLMessages.getString("View_group_user"), NLMessages.getString("View_group_genre"),
					NLMessages.getString("View_group_author"), NLMessages.getString("View_group_title"),
					NLMessages.getString("View_group_publisher"), NLMessages.getString("View_group_place"),
					NLMessages.getString("View_group_location"), NLMessages.getString("View_group_date_copyright"),
					NLMessages.getString("View_grou_date_captured"), NLMessages.getString("View_group_date_creation")};
			_orderCombo.setItems(oderers);
			_orderCombo.select(0);
			_orderCombo.addSelectionListener(new SelectionAdapter()
			{
				@Override
				public void widgetSelected(final SelectionEvent event)
				{
				}
			}); // SelectionListener

			_orderButton = new Button(ordertitleComp, SWT.NONE | SWT.READ_ONLY);
			_orderButton.setText(NLMessages.getString("View_group"));
			_orderButton.setToolTipText(NLMessages.getString("View_group_references_tooltip"));
			_orderButton.setImage(_imageReg.get(IconsInternal.GROUP));
			_orderButton.addSelectionListener(new SelectionAdapter()
			{
				@Override
				public void widgetSelected(final SelectionEvent event)
				{
					int selection = _orderCombo.getSelectionIndex();
					switch (selection)
					{
						case 0:
							_pdrObjectsProvider.setOrderer(null);
							_pdrObjectsProvider.setRefOrderer(null);
							break;
						case 1:
							_pdrObjectsProvider.setOrderer(_ordererFactory.createAspectOrderer("aspect.semantic"));
							_pdrObjectsProvider.setRefOrderer(null);
							break;
						case 2:
							_pdrObjectsProvider.setOrderer(_ordererFactory.createAspectOrderer("aspect.year"));
							_pdrObjectsProvider.setRefOrderer(null);
							break;
						case 3:
							_pdrObjectsProvider.setOrderer(_ordererFactory.createAspectOrderer("aspect.place"));
							_pdrObjectsProvider.setRefOrderer(null);
							break;
						case 4:
							_pdrObjectsProvider.setOrderer(_ordererFactory.createAspectOrderer("aspect.reference"));
							_pdrObjectsProvider.setRefOrderer(null);
							break;
						case 5:
							_pdrObjectsProvider.setOrderer(_ordererFactory.createAspectOrderer("aspect.relation"));
							_pdrObjectsProvider.setRefOrderer(null);
							break;
						case 6:
							_pdrObjectsProvider.setOrderer(_ordererFactory.createAspectOrderer("aspect.markup"));
							_pdrObjectsProvider.setRefOrderer(null);
							break;
						case 7:
							_pdrObjectsProvider.setOrderer(_ordererFactory.createAspectOrderer("aspect.person"));
							_pdrObjectsProvider.setRefOrderer(null);
							break;
						case 8:
							_pdrObjectsProvider.setOrderer(null);
							_pdrObjectsProvider.setRefOrderer(_ordererFactory.createReferenceOrderer("reference.user"));
							break;
						case 9:
							_pdrObjectsProvider.setOrderer(null);
							_pdrObjectsProvider.setRefOrderer(_ordererFactory.createReferenceOrderer("reference.genre"));
							break;
						case 10:
							_pdrObjectsProvider.setOrderer(null);
							_pdrObjectsProvider.setRefOrderer(_ordererFactory
									.createReferenceOrderer("reference.author"));
							break;
						case 11:
							_pdrObjectsProvider.setOrderer(null);
							_pdrObjectsProvider.setRefOrderer(_ordererFactory.createReferenceOrderer("reference.title"));
							break;
						case 12:
							_pdrObjectsProvider.setOrderer(null);
							_pdrObjectsProvider.setRefOrderer(_ordererFactory
									.createReferenceOrderer("reference.origin.publisher"));
							break;
						case 13:
							_pdrObjectsProvider.setOrderer(null);
							_pdrObjectsProvider.setRefOrderer(_ordererFactory
									.createReferenceOrderer("reference.origin.place"));
							break;
						case 14:
							_pdrObjectsProvider.setOrderer(null);
							_pdrObjectsProvider.setRefOrderer(_ordererFactory
									.createReferenceOrderer("reference.location"));
							break;
						case 15:
							_pdrObjectsProvider.setOrderer(null);
							_pdrObjectsProvider.setRefOrderer(_ordererFactory
									.createReferenceOrderer("reference.date.copyright"));
							break;
						case 16:
							_pdrObjectsProvider.setOrderer(null);
							_pdrObjectsProvider.setRefOrderer(_ordererFactory
									.createReferenceOrderer("reference.date.capture"));
							break;
						case 17:
							_pdrObjectsProvider.setOrderer(null);
							_pdrObjectsProvider.setRefOrderer(_ordererFactory
									.createReferenceOrderer("reference.date.creation"));
							break;
						default:
							break;
					}
					disposeTabItems();
					_currentRefCategoryId = 0;
					if (_concurring)
					{
						buildCategories(new Person[]
						{_facade.getConcurringPerson()}, _facade.getCurrentReference());
					}
					else
					{
						buildCategories(_facade.getCurrentTreeObjects(), _facade.getCurrentReference());
					}
				}
			}); // SelectionListener
			_orderButton.setEnabled(false);

			Composite sorttitleComp = new Composite(topComposite, SWT.NONE);
			sorttitleComp.setLayout(new GridLayout());
			((GridLayout) sorttitleComp.getLayout()).numColumns = 4;
			((GridLayout) sorttitleComp.getLayout()).marginHeight = 0;
			((GridLayout) sorttitleComp.getLayout()).marginWidth = 0;

			Label sort = new Label(sorttitleComp, SWT.NONE);
			sort.setText(NLMessages.getString("View_sort_by"));
			sort.setLayoutData(new GridData());
			((GridData) sort.getLayoutData()).horizontalIndent = 8;

			_sortedCombo = new Combo(sorttitleComp, SWT.NONE | SWT.READ_ONLY);
			String[] sorters = new String[]
			{NLMessages.getString("View_sort_cronologically"), NLMessages.getString("View_sort_title"),
					NLMessages.getString("View_sort_revision"), NLMessages.getString("View_sort_author"),
					NLMessages.getString("View_sort_creator")};
			_sortedCombo.setItems(sorters);
			_sortedCombo.select(0);
			_sortedCombo.addSelectionListener(new SelectionAdapter()
			{
				@Override
				public void widgetSelected(final SelectionEvent event)
				{
					_sortButtonDesc.setEnabled(true);
					_sortButtonAsc.setEnabled(true);
				}
			}); // SelectionListener
			_sortButtonDesc = new Button(sorttitleComp, SWT.NONE | SWT.READ_ONLY);
			_sortButtonDesc.setToolTipText(NLMessages.getString("View_sort_references_desc_desc"));
			_sortButtonDesc.setImage(_imageReg.get(IconsInternal.SORT_ALPHABETIC_ASC));
			_sortButtonDesc.setEnabled(false);
			_sortButtonDesc.addSelectionListener(new SelectionAdapter()
			{
				@Override
				public void widgetSelected(final SelectionEvent event)
				{
					_sortButtonDesc.setEnabled(false);
					_sortButtonAsc.setEnabled(true);
					int selection = _sortedCombo.getSelectionIndex();
					switch (selection)
					{
						case 0:
							_pdrObjectsProvider.setRefComparator(new ReferenceCronComparator(false));
							break;
						case 1:
							_pdrObjectsProvider.setRefComparator(new ReferenceByTitleComparator(false));
							break;
						case 2:
							_pdrObjectsProvider.setRefComparator(new ReferenceByRecentChangesComparator(false));
							break;
						case 3:
							_pdrObjectsProvider.setRefComparator(new ReferenceByAuthorTitleComparator(false));
							break;
						case 4:
							_pdrObjectsProvider.setRefComparator(new ReferenceByCreatorComparator(false));
							break;
						default:
							break;
					}
					disposeTabItems();
					if (_concurring)
					{
						buildCategories(new Person[]
						{_facade.getConcurringPerson()}, _facade.getCurrentReference());
					}
					else
					{
						buildCategories(_facade.getCurrentTreeObjects(), _facade.getCurrentReference());
					}
					_tabFolderRef.setSelection(_currentRefCategoryId);
				}
			}); // SelectionListener
			_sortButtonDesc.setEnabled(false);
			_sortButtonAsc = new Button(sorttitleComp, SWT.NONE | SWT.READ_ONLY);
			_sortButtonAsc.setToolTipText(NLMessages.getString("View_sort_references_asc_tooltip"));
			_sortButtonAsc.setImage(_imageReg.get(IconsInternal.SORT_ALPHABETIC_DESC));
			_sortButtonAsc.setEnabled(true);
			_sortButtonAsc.addSelectionListener(new SelectionAdapter()
			{
				@Override
				public void widgetSelected(final SelectionEvent event)
				{
					_sortButtonDesc.setEnabled(true);
					_sortButtonAsc.setEnabled(false);
					int selection = _sortedCombo.getSelectionIndex();
					//				System.out.println("ordering selection " + selection); //$NON-NLS-1$
					switch (selection)
					{
						case 0:
							_pdrObjectsProvider.setRefComparator(new ReferenceCronComparator(true));
							break;
						case 1:
							_pdrObjectsProvider.setRefComparator(new ReferenceByTitleComparator(true));
							break;
						case 2:
							_pdrObjectsProvider.setRefComparator(new ReferenceByRecentChangesComparator(true));
							break;
						case 3:
							_pdrObjectsProvider.setRefComparator(new ReferenceByAuthorTitleComparator(true));
							break;
						case 4:
							_pdrObjectsProvider.setRefComparator(new ReferenceByCreatorComparator(true));
							break;
						default:
							break;
					}
					disposeTabItems();
					if (_concurring)
					{
						// loadPdrObject(_facade.getConcurringPerson());
						buildCategories(new Person[]
						{_facade.getConcurringPerson()}, _facade.getCurrentReference());
					}
					else
					{
						buildCategories(_facade.getCurrentTreeObjects(), _facade.getCurrentReference());
					}
					_tabFolderRef.setSelection(_currentRefCategoryId);
				}
			}); // SelectionListener

			topComposite.layout();
			topComposite.pack();
		}
		_mainComposite.layout();
		_mainComposite.pack();
		createFilters();
		createContextMenuAction();
		createActions();
		createMenus();

		if (secId != null)
		{
			//			System.out.println("secId " + secId); //$NON-NLS-1$
			if (secId.startsWith("pdrPo")) //$NON-NLS-1$
			{
				_additional = true;
				Person p = _facade.getPerson(new PdrId(secId));
				if (p != null)
				{
					loadPdrObject(new Person[]
					{p});
					setPartName(p.getDisplayName(27));
					setTitleToolTip(p.getDisplayName());
				}
			}
			else if (secId.startsWith("pdrRo")) //$NON-NLS-1$
			{
				_additional = true;
				ReferenceMods r = _facade.getReference(new PdrId(secId));
				if (r != null)
				{
					loadPdrObject(new ReferenceMods[]
					{r});
					setPartName(r.getDisplayName(27));
					setTitleToolTip(r.getDisplayName());
				}
			}
			else if (secId.startsWith("pdrAo")) //$NON-NLS-1$
			{
				_additional = true;
				Aspect a = _facade.getLoadedAspects().get(secId);
				if (a != null)
				{
					loadPdrObject(new Aspect[]
					{a});
					setPartName(a.getDisplayName(27));
					setTitleToolTip(a.getDisplayName());
				}
			}
			else if (secId.startsWith("a")) //$NON-NLS-1$
			{
				loadPdrObject(_facade.getCurrentTreeObjects());
			}
			else if (secId.startsWith("b")) //$NON-NLS-1$
			{
				loadPdrObject(new Person[]
				{_facade.getConcurringPerson()});
			}
		}
		else
		{
			loadPdrObject(_facade.getCurrentTreeObjects());
		}
		_facade.addObserver(this);
	}

	@Override
	public final void dispose()
	{
		_facade.deleteObserver(ReferenceCatView.this);

	}

	/**
	 * Dispose tab items.
	 */
	private void disposeTabItems()
	{
		if (_tabMap != null)
		{
			_tabMap.clear();
			_tabMap = null;

		}

		if (_tabFolderRef != null)
		{
			_tabFolderRef.dispose();
		}

	}

	/**
	 * Fill menu.
	 * @param rootMenuManager the root menu manager
	 */
	private void fillMenu(final IMenuManager rootMenuManager)
	{

		rootMenuManager.add(_onlyNewReferencesAction);
		rootMenuManager.add(_onlyUpdatedReferencesAction);
		rootMenuManager.add(_semanticFilterAction);
		rootMenuManager.add(_userFilterAction);

	}

	@Override
	public ISelection getSelection()
	{
		StructuredSelection selection;
		if (_selectedReference != null)
		{
			selection = new StructuredSelection(new PdrObject[]
			{_selectedReference});
		}
		else
		{
			selection = new StructuredSelection(new PdrObject[]
			{});
		}
		return selection;

	}

	/**
	 * Load pdr object.
	 * @param pdrObjects the pdr objects
	 */
	private void loadPdrObject(final PdrObject[] pdrObjects)
	{
		_currentObjects = pdrObjects;
		if (_tabFolderRef != null)
		{
			_tabFolderRef.dispose();
		}

		_pdrObjectsProvider.setInput(pdrObjects);
		_pdrObjectsProvider.setLazySorting(true);
		_pdrObjectsProvider.setOrderer(null);
		_pdrObjectsProvider.removeAllFilters();
		if (_advanced)
		{
			_orderCombo.select(0);
		}
		_currentRefCategoryId = 0;
		_onlyNewReferencesAction.setChecked(false);
		_onlyUpdatedReferencesAction.setChecked(false);
		_userFilterAction.setChecked(false);
		_semanticFilterAction.setChecked(false);

		_onlyNewReferencesAction.setEnabled(true);
		_onlyUpdatedReferencesAction.setEnabled(true);
		_userFilterAction.setEnabled(true);
		_semanticFilterAction.setEnabled(true);

		if (_currentObjects != null)
		{
			if (_facade.getCurrentReference() != null
					&& _currentObjects.length > 0
					&& _currentObjects[_currentObjects.length - 1] != null
					&& _currentObjects[_currentObjects.length - 1].getAspectIds().contains(
							_facade.getCurrentReference().getPdrId().toString()))
			{
				buildCategories(_currentObjects, _facade.getCurrentReference());
			}
			else
			{
				buildCategories(_currentObjects, null);
			}
		}

	}

	/**
	 * meth load one reference, sets layout of text, sets stext and adds
	 * mouselistener for message box and context menu.
	 * @param cr current reference
	 * @param cCat current category id
	 * @param refPresentation reference presentation
	 * @param position position
	 */
	private void loadReference(final ReferenceMods cr, final int cCat, final IReferencePresentation refPresentation,
			final int position)
	{

		refPresentation.setReference(cr);
		refPresentation.createPresentation();
		refPresentation.addSelectionListener(new Listener()
		{

			@Override
			public void handleEvent(final Event event)
			{
				IReferencePresentation current = (IReferencePresentation) event.data;
				_selectedReference = current.getReference();
				current.setSelected(true);
				_facade.setCurrentReference(_selectedReference); //$NON-NLS-1$
				IReferencePresentation last = (IReferencePresentation) _tabFolderRef.getData("lastSelected"); //$NON-NLS-1$

				IStatus sca = new Status(IStatus.INFO, Activator.PLUGIN_ID,
						"ReferenceCatView current reference: " + _facade.getCurrentReference().getPdrId().toString()); //$NON-NLS-1$
				iLogger.log(sca);
				if (last != null && !last.equals(current))
				{
					last.setSelected(false);
				}
				_tabFolderRef.setData("lastSelected", current); //$NON-NLS-1$
			}
		});

		refPresentation.addDoubleClickListener(new Listener()
		{
			@Override
			public void handleEvent(final Event event)
			{
				IHandlerService handlerService = (IHandlerService) PlatformUI.getWorkbench().getService(
						IHandlerService.class);
				try
				{
					handlerService.executeCommand("org.bbaw.pdr.ae.view.main.commands.OpenSourceEditorDialog", null); //$NON-NLS-1$
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

	/**
	 * meth. loads all aspects that belong to the same currently selected
	 * category.
	 * @param groupedReferences vector of oderingHeads containing the grouped
	 *            references
	 * @param currentCategoryID current category ID
	 * @param startIndex start index
	 * @param number number references to be shown on one page
	 * @param savedReference just saved reference or null
	 */
	private void loadReferences(final Vector<OrderingHead> groupedReferences, final int currentCategoryID,
			final int startIndex, final int number, final ReferenceMods savedReference)
	{
		final int start;



		// System.out.println("tabmap hast mapping für " + currentCategoryID
		//		+ " ??: " + tabMap.containsKey(currentCategoryID)); //$NON-NLS-1$ //$NON-NLS-2$
		// System.out.println(tabMap.get(currentCategoryID));
		CTabItem ti = _tabMap.get(currentCategoryID);
		ti.setData("loaded", "true"); //$NON-NLS-1$ //$NON-NLS-2$

		ScrolledComposite sc = (ScrolledComposite) ti.getControl();
		Composite comp = (Composite) sc.getContent();
		Control[] children = comp.getChildren();

		for (Control c : children)
		{
			c.dispose();
		}
		if (!groupedReferences.get(currentCategoryID).isSorted())
		{
			_pdrObjectsProvider.sort(groupedReferences.get(currentCategoryID));
		}
		_references = groupedReferences.get(currentCategoryID).getReferences();
		IStatus sla = new Status(IStatus.INFO, Activator.PLUGIN_ID,
				"RefCatView load references - number of references: " + _references.size()); //$NON-NLS-1$
		iLogger.log(sla);

		if (_references != null)
		{
			final int size = _references.size();
			//	        System.out.println("anzahl der ref " + size); //$NON-NLS-1$
			Label referenceNumber = new Label(comp, SWT.NONE);
			int endIndex = startIndex + 10;
			if (endIndex > size)
			{
				endIndex = size;
			}
			referenceNumber.setText(NLMessages.getString("View_total_number") + " " + size + " "
					+ NLMessages.getString("View_references") + (startIndex + 1) + " - " + endIndex); //$NON-NLS-1$
			if (size > 10)
			{

				start = startIndex;

				Group eventNavBar = new Group(comp, SWT.NONE);
				eventNavBar.setText(NLMessages.getString("View_scroll"));
				eventNavBar.setLayout(new RowLayout());

				Button toStart = new Button(eventNavBar, SWT.PUSH);
				toStart.setText(" |< "); //$NON-NLS-1$
				toStart.addSelectionListener(new SelectionAdapter()
				{

					@Override
					public void widgetSelected(final SelectionEvent arg0)
					{
						loadReferences(groupedReferences, currentCategoryID, 0, number, null);
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

						loadReferences(groupedReferences, currentCategoryID, nextStart, number, null);
					}

				});
				final Button minusOne = new Button(eventNavBar, SWT.PUSH);
				minusOne.setText(" -1 "); //$NON-NLS-1$
				if (start - 1 < 0)
				{
					minusOne.setEnabled(false);
				}
				else
				{
					minusOne.setEnabled(true);
				}
				minusOne.addSelectionListener(new SelectionAdapter()
				{

					@Override
					public void widgetSelected(final SelectionEvent arg0)
					{
						int nextStart = start - 1;
						loadReferences(groupedReferences, currentCategoryID, nextStart, number, null);
					}

				});
				final Text jumpTo = new Text(eventNavBar, SWT.BORDER);
				jumpTo.setSize(15, 20);

				Button okButton = new Button(eventNavBar, SWT.PUSH);
				okButton.setText(NLMessages.getString("View_ok"));
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
									loadReferences(groupedReferences, currentCategoryID, --n, number, null);
								}
							}
						}
						catch (NumberFormatException ex)
						{
							String message = NLMessages.getString("View_pleaseEnterNumber"); //$NON-NLS-1$
							MessageDialog.openInformation(_parentShell, NLMessages.getString("View_error"), message); //$NON-NLS-1$
						}
					}
				});

				final Button plusOne = new Button(eventNavBar, SWT.PUSH);
				plusOne.setText(" +1 "); //$NON-NLS-1$
				if (start + 1 >= size)
				{
					plusOne.setEnabled(false);
				}
				else
				{
					plusOne.setEnabled(true);
				}
				plusOne.addSelectionListener(new SelectionAdapter()
				{

					@Override
					public void widgetSelected(final SelectionEvent arg0)
					{
						int nextStart = start + 1;

						loadReferences(groupedReferences, currentCategoryID, nextStart, number, null);
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

						loadReferences(groupedReferences, currentCategoryID, nextStart, number, null);
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
					//	        		System.out.println("i " + i); //$NON-NLS-1$
					//	        		if (references == null) System.out.println("refs null"); //$NON-NLS-1$
					ReferenceMods cr = _references.get(i);
					@SuppressWarnings("unused")
					RightsChecker rc = new RightsChecker();
					if (true)
					{
						IReferencePresentation referencePresentation = ControlExtensions.createReferencePresentation();
						referencePresentation.setComposite(comp);

						// StyledText stext = new StyledText(comp, SWT.WRAP |
						// SWT.NO_BACKGROUND | SWT.NO_FOCUS | SWT.CURSOR_ARROW |
						// SWT.BORDER);



						//						if (cr == null) System.out.println("cr null"); //$NON-NLS-1$
						//						if (cr.getPdrId() == null) System.out.println("id null"); //$NON-NLS-1$
						//			        	System.out.println("hier bricht er ab " + cr.getPdrId().toString()); //$NON-NLS-1$
						loadReference(cr, currentCategoryID, referencePresentation, i);
						createContextMenu(referencePresentation.getControl());

						i++;
					}

				}
				else
				{
					break;
				}

			}
		}
		setStatusLine(NLMessages.getString("View_statusLine_ready"));
		sc.setContent(comp);
		sc.setMinSize(comp.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		comp.layout();

	}

	// /////////////////////Update - Observer ///////////////////////////////

	@Override
	public void removeSelectionChangedListener(ISelectionChangedListener listener)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void selectionChanged(final IWorkbenchPart part, final ISelection selection)
	{
		// TODO Auto-generated method stub

	}

	/**
	 * Sets Focus in view.
	 */
	@Override
	public void setFocus()
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void setSelection(ISelection selection)
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
		IActionBars bars = getViewSite().getActionBars();
		bars.getStatusLineManager().setMessage(message);
	}

	@Override
	public final void update(final Observable o, final Object arg)
	{
		IStatus supdate = new Status(IStatus.INFO, Activator.PLUGIN_ID, "ReferenceCatView update: " + arg); //$NON-NLS-1$
		iLogger.log(supdate);

		if (!_concurring && !_additional)
		{
			if (arg.equals("newTreeObjects")) //$NON-NLS-1$
			{
				// PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().activate(this);
				loadPdrObject(_facade.getCurrentTreeObjects());
			}
			else if (arg.equals("newReference") && _tabFolderRef != null) //$NON-NLS-1$
			{
				if (_tabFolderRef != null)
				{
					IReferencePresentation last = (IReferencePresentation) _tabFolderRef.getData("lastSelected"); //$NON-NLS-1$
					if (last != null && !_facade.getCurrentReference().equals(last.getReference()))
					{
						last.setSelected(false);
					}
				}
			}
			else if (arg.equals("newNewReference")) //$NON-NLS-1$
			{
				// PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().activate(this);
				loadPdrObject(_facade.getCurrentTreeObjects());
			}
			else if (arg.equals("refreshAll")) //$NON-NLS-1$
			{
				// PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().activate(this);
				disposeTabItems();
			}
		}
		else if (!_additional)
		{
			if (arg.equals("newConcurringPerson")) //$NON-NLS-1$ //$NON-NLS-2$
			{
				// PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().activate(this);
				_currentRefCategoryId = 0;
				loadPdrObject(new Person[]
				{_facade.getConcurringPerson()});

			}
			else if (arg.equals("newNewAspect")) //$NON-NLS-1$ //$NON-NLS-2$
			{
				buildCategories(new Person[]
				{_facade.getConcurringPerson()}, null);
			}
			else if (arg.equals("noSelectedConcurringPerson")) //$NON-NLS-1$
			{
				disposeTabItems();
			}
			else if (arg.equals("newReference") && _tabFolderRef != null) //$NON-NLS-1$
			{
				if (_tabFolderRef != null)
				{
					IReferencePresentation last = (IReferencePresentation) _tabFolderRef.getData("lastSelected"); //$NON-NLS-1$
					if (last != null && !_facade.getCurrentReference().equals(last.getReference()))
					{
						last.setSelected(false);
					}
				}
			}
		}
		else if (_additional)
		{
			if (arg.equals("newReference") && _tabFolderRef != null) //$NON-NLS-1$
			{
				if (_tabFolderRef != null)
				{
					IReferencePresentation last = (IReferencePresentation) _tabFolderRef.getData("lastSelected"); //$NON-NLS-1$
					if (last != null && !_facade.getCurrentReference().equals(last.getReference()))
					{
						last.setSelected(false);
					}
				}
			}
		}
	}

	/**
	 * Update filter.
	 * @param action the action
	 */
	private void updateFilter(final Action action)
	{
		if (action == _onlyNewReferencesAction)
		{
			if (action.isChecked())
			{
				_pdrObjectsProvider.addFilter(_onlyNewReferencesFilter);
			}
			else
			{
				_pdrObjectsProvider.removeFilter(_onlyNewReferencesFilter);
			}
		}
		else if (action == _onlyUpdatedReferencesAction)
		{
			if (action.isChecked())
			{
				_pdrObjectsProvider.addFilter(_onlyUpdatedReferencesFilter);
			}
			else
			{
				_pdrObjectsProvider.removeFilter(_onlyUpdatedReferencesFilter);
			}
		}
		disposeTabItems();
		buildCategories(_currentObjects, null);
	}
}
