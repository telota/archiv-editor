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
import org.bbaw.pdr.ae.common.CommonActivator;
import org.bbaw.pdr.ae.common.NLMessages;
import org.bbaw.pdr.ae.common.icons.IconsInternal;
import org.bbaw.pdr.ae.common.interfaces.AEFilter;
import org.bbaw.pdr.ae.control.comparator.AspectsByCreatorComparator;
import org.bbaw.pdr.ae.control.comparator.AspectsByCronComparator;
import org.bbaw.pdr.ae.control.comparator.AspectsByRecentChangesComparator;
import org.bbaw.pdr.ae.control.comparator.AspectsByReferenceComparator;
import org.bbaw.pdr.ae.control.comparator.AspectsBySemanticComparator;
import org.bbaw.pdr.ae.control.core.PDRConfigProvider;
import org.bbaw.pdr.ae.control.facade.Facade;
import org.bbaw.pdr.ae.control.facade.RightsChecker;
import org.bbaw.pdr.ae.metamodel.PdrId;
import org.bbaw.pdr.ae.model.Aspect;
import org.bbaw.pdr.ae.model.PdrObject;
import org.bbaw.pdr.ae.model.Person;
import org.bbaw.pdr.ae.model.ReferenceMods;
import org.bbaw.pdr.ae.model.RelationStm;
import org.bbaw.pdr.ae.model.SemanticStm;
import org.bbaw.pdr.ae.model.TaggingRange;
import org.bbaw.pdr.ae.model.view.OrderingHead;
import org.bbaw.pdr.ae.view.control.ControlExtensions;
import org.bbaw.pdr.ae.view.control.PDRObjectsProvider;
import org.bbaw.pdr.ae.view.control.PDROrdererFactory;
import org.bbaw.pdr.ae.view.control.ViewHelper;
import org.bbaw.pdr.ae.view.control.customSWTWidges.MarkupTooltip;
import org.bbaw.pdr.ae.view.control.filters.AspectExcludeObjectRelationsFilter;
import org.bbaw.pdr.ae.view.control.filters.AspectSearchTextFilter;
import org.bbaw.pdr.ae.view.control.filters.OnlyAspectDivergentMarkup;
import org.bbaw.pdr.ae.view.control.filters.OnlyAspectsAboutReferenceFilter;
import org.bbaw.pdr.ae.view.control.filters.OnlyAspectsBasedOnReferenceFilter;
import org.bbaw.pdr.ae.view.control.filters.OnlyNewPDRObjectsFilter;
import org.bbaw.pdr.ae.view.control.filters.OnlyUpdatedPDRObjectsFilter;
import org.bbaw.pdr.ae.view.control.interfaces.IMarkupPresentation;
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
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
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
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.part.ViewPart;

/**
 * This ViewClass creates the CategoryView of the Aspects of the current Person.
 * it extends AbstractCatView. The List of Categories is still static and based
 * upon the List given in the properties file. CategoryView .
 * @author Christoph Plutte
 */
public class AspectsView extends ViewPart implements Observer, ISelectionProvider
{

	/** The concurring. */
	private boolean _concurring = false;

	/** The additional. */
	private boolean _additional = false;

	/** The _provider. */
	private String _provider;

	/** The current category id. */
	private int _currentCategoryID;

	/** The all aspects. */
	private Text _allAspects;

	/** The search text. */
	private Text _searchText;

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

	/** The search button. */
	private Button _searchButton;

	/** The year filter action. */
	private Action _onlyAspectsWithDivergentMarkup, _openAspectsInNewTap, _openReferencesInNewTap,
			_onlyNewAspectsAction, _onlyUpdatedAspectsAction, _excludeObjectRelationsAction, _referenceFilterAction,
			_userFilterAction, _semanticFilterAction, _personFilterAction, _yearFilterAction;

	/** The aspect exlude object relations filter. */
	private AEFilter _onlyAspectsDivergentMarkupFilter, _onlyNewAspectsFilter, _onlyUpdatedAspectsFilter,
			_aspectExludeObjectRelationsFilter;

	/** The root menu manager. */
	private IMenuManager _rootMenuManager;


	/** The aspects selection adapter. */
	private SelectionAdapter _aspectsSelectionAdapter;

	/** The aspects. */
	private Vector<Aspect> _aspects;

	/** The parent shell. */
	private Shell _parentShell;

	/** The _main composite. */
	private Composite _mainComposite;

	/** The tab folder right. */
	private CTabFolder _tabFolderRight;

	/** hashmap for tabitems and semantic categories in category view. */
	private HashMap<Integer, CTabItem> _tabMap = new HashMap<Integer, CTabItem>(AEConstants.MAX_NUMBER_CATEGORIES);

	/**
	 * id of category view.
	 */
	public static final String ID = "org.bbaw.pdr.ae.view.main.views.AspectsView"; //$NON-NLS-1$

	/** __facade singleton instance. */
	private Facade _facade = Facade.getInstanz();

	/** The _current objects. */
	private PdrObject[] _currentObjects;

	/** The WHIT e_ color. */
	private static final Color WHITE_COLOR = Display.getDefault().getSystemColor(SWT.COLOR_WHITE);


	/** Logger. */
	private static ILog iLogger = AEConstants.ILOGGER;
	/** Instance of shared image registry. */
	private ImageRegistry _imageReg = CommonActivator.getDefault().getImageRegistry();

	/** The pdr objects _provider. */
	private PDRObjectsProvider _pdrObjectsProvider = new PDRObjectsProvider();

	/** The _orderer factory. */
	private PDROrdererFactory _ordererFactory = new PDROrdererFactory();



	/** The selection changed listeners. */
	private ArrayList<ISelectionChangedListener> _selectionChangedListeners = new ArrayList<ISelectionChangedListener>();

	/** The _current aspect. */
	private Aspect _currentAspect;

	private boolean _advanced = Platform.getPreferencesService().getBoolean(CommonActivator.PLUGIN_ID,
			"AE_ADVANCED_VERSION", AEConstants.AE_ADVANCED_VERSION, null);

	private Action _onlyAspectsBasedOnReference;

	private Action _onlyAspectsAboutReference;

	private OnlyAspectsBasedOnReferenceFilter _onlyAspectsBasedOnReferenceFilter;

	private OnlyAspectsAboutReferenceFilter _onlyAspectsAboutReferenceFilter;;
	/**
	 * constructor.
	 */
	
	public static AspectsView instance;
	
	public AspectsView()
	{
		instance=this;
	}

	@Override
	public final void addSelectionChangedListener(final ISelectionChangedListener listener)
	{
		_selectionChangedListeners.add(listener);

	}

	/**
	 * method to build Categories.
	 * @param currentObjects the currently selected objects of whom the related
	 *            aspects are to be shown
	 * @param savedAspect the aspect just saved or null
	 */
	public final void buildCategories(final PdrObject[] currentObjects, final Aspect savedAspect)
	{
		_currentObjects = currentObjects;
		if (_tabMap != null)
		{
			_tabMap.clear();
			_tabMap = null;
		}

		_tabFolderRight = new CTabFolder(_mainComposite, SWT.NONE);
		_tabFolderRight.setLayoutData(new GridData());
		((GridData) _tabFolderRight.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _tabFolderRight.getLayoutData()).verticalAlignment = SWT.FILL;
		((GridData) _tabFolderRight.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) _tabFolderRight.getLayoutData()).grabExcessVerticalSpace = true;
		_tabMap = new HashMap<Integer, CTabItem>(AEConstants.MAX_NUMBER_CATEGORIES);

		ScrolledComposite scrollComp;
		Composite contentComp;
		GridLayout layout;
		CTabItem tabItem;
		final Vector<OrderingHead> orderedAspects = _pdrObjectsProvider.getArrangedAspects();
		// System.out.println("orderer " + pdrObjectsProvider.getOrderer());
		if (_currentObjects != null && _advanced)
		{
			_allAspects.setText("" + _pdrObjectsProvider.getNumberOfAspects()); //$NON-NLS-1$
			_sortedCombo.setEnabled(true);
			_orderButton.setEnabled(true);
			_orderCombo.setEnabled(true);
		}
		int start = 0;
		if (orderedAspects != null && !orderedAspects.isEmpty())
		{
			for (int i = 0; i < orderedAspects.size(); i++)
			{
				tabItem = new CTabItem(_tabFolderRight, SWT.NONE);
				tabItem.setText(orderedAspects.get(i).getLabel());
				tabItem.setData("id", orderedAspects.get(i).getValue()); //$NON-NLS-1$
				tabItem.setData("nr", i); //$NON-NLS-1$
				tabItem.setData("loaded", "false"); //$NON-NLS-1$ //$NON-NLS-2$
				tabItem.setImage(_imageReg.get(orderedAspects.get(i).getImageString()));
				_tabMap.put(i, tabItem);

				scrollComp = new ScrolledComposite(_tabFolderRight, SWT.V_SCROLL | SWT.H_SCROLL);
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
				tabItem.setControl(scrollComp);

			} // for-loop
			if (savedAspect != null)
			{
				String cat = null;
				if (savedAspect.getSemanticDim().getSemanticLabelByProvider(_provider) != null
						&& !savedAspect.getSemanticDim().getSemanticLabelByProvider(_provider).isEmpty())
				{
					cat = savedAspect.getSemanticDim().getSemanticLabelByProvider(_provider).firstElement();
				}
				else if (!savedAspect.getSemanticDim().getSemanticLabelByProvider(null).isEmpty())
				{
					cat = savedAspect.getSemanticDim().getSemanticLabelByProvider(null).firstElement();
				}
				if (cat != null)
				{
					Integer catID = 0;
					for (int j = 0; j < orderedAspects.size(); j++)
					{
						OrderingHead oh = orderedAspects.get(j);
						if (cat.equals(oh.getValue()))
						{
							catID = j;
							break;
						}
					}
					_currentCategoryID = catID;
					_tabFolderRight.setSelection(catID);

					Vector<Aspect> as = orderedAspects.get(catID).getAspects();
					for (int k = 0; k < as.size(); k++)
					{
						if (as.get(k).getPdrId().equals(savedAspect.getPdrId()))
						{
							start = k;
							break;
						}
					}

				}
				else
				{
					_currentCategoryID = 0;
				}
			}
			_tabFolderRight.addSelectionListener(new SelectionAdapter()
			{
				@Override
				public void widgetSelected(final SelectionEvent se)
				{
					int selected = _tabFolderRight.getSelectionIndex();

					IStatus stabfolder = new Status(IStatus.INFO, Activator.PLUGIN_ID,
							"AspectsView tab item selected: " //$NON-NLS-1$
									+ _tabFolderRight.getSelection().getText());
					iLogger.log(stabfolder);

					_currentCategoryID = selected;
					IStatus sCat = new Status(IStatus.INFO, Activator.PLUGIN_ID, "AspectsView current CategoryID: " //$NON-NLS-1$
							+ _currentCategoryID);
					iLogger.log(sCat);

					if (!_tabMap.get(_currentCategoryID).getData("loaded").equals("true")) //$NON-NLS-1$ //$NON-NLS-2$
					{
						loadAspects(orderedAspects, _currentCategoryID, 0, 10, null);
					}
				}
			}); // SelectionListener

			if (_tabMap.containsKey(_currentCategoryID))
			{
				loadAspects(orderedAspects, _currentCategoryID, start, 10, savedAspect);
				_tabFolderRight.setSelection(_currentCategoryID);
			}
			else
			{
				loadAspects(orderedAspects, 0, start, 10, savedAspect);
				_tabFolderRight.setSelection(0);
			}

		}
		else
		{
			//    		System.out.println("no categories"); //$NON-NLS-1$
			Label noCatLabel = new Label(_tabFolderRight, SWT.None);
			noCatLabel.setText(NLMessages.getString("View_message_no_aspects_found"));
			noCatLabel.pack();
		}
		// _tabFolderRight.layout();
		_tabFolderRight.pack();
		_mainComposite.layout();
		// _mainComposite.pack();

	} // buildCategories

	/**
	 * Creates the actions.
	 */
	private void createActions()
	{
		_onlyNewAspectsAction = new Action(NLMessages.getString("View_action_only_newAspects"))
		{
			@Override
			public void run()
			{
				updateFilter(_onlyNewAspectsAction);
			}
		};
		_onlyNewAspectsAction.setChecked(false);
		_onlyNewAspectsAction.setEnabled(false);
		_onlyNewAspectsAction.setImageDescriptor(_imageReg.getDescriptor(IconsInternal.DECORATION_NEW));

		_onlyUpdatedAspectsAction = new Action(NLMessages.getString("View_action_only_updated_aspects"))
		{
			@Override
			public void run()
			{
				updateFilter(_onlyUpdatedAspectsAction);
			}
		};
		_onlyUpdatedAspectsAction.setChecked(false);
		_onlyUpdatedAspectsAction.setEnabled(false);
		_onlyUpdatedAspectsAction.setImageDescriptor(_imageReg.getDescriptor(IconsInternal.DECORATION_UPDATED));

		_excludeObjectRelationsAction = new Action(NLMessages.getString("View_action_exclude_other_relations"))
		{
			@Override
			public void run()
			{
				updateFilter(_excludeObjectRelationsAction);
			}
		};
		_excludeObjectRelationsAction.setChecked(false);
		_excludeObjectRelationsAction.setEnabled(false);
		_excludeObjectRelationsAction.setImageDescriptor(_imageReg.getDescriptor(IconsInternal.RELATION));

		_onlyAspectsWithDivergentMarkup = new Action(NLMessages.getString("View_action_only_divergent_markup"))
		{
			@Override
			public void run()
			{
				updateFilter(_onlyAspectsWithDivergentMarkup);
			}
		};
		_onlyAspectsWithDivergentMarkup.setChecked(false);
		_onlyAspectsWithDivergentMarkup.setEnabled(false);
		_onlyAspectsWithDivergentMarkup.setImageDescriptor(_imageReg.getDescriptor(IconsInternal.MARKUP_QUESTION));


		_onlyAspectsBasedOnReference = new Action(NLMessages.getString("View_action_only_based_on_reference"))
		{
			@Override
			public void run()
			{
				updateFilter(_onlyAspectsBasedOnReference);
			}
		};
		_onlyAspectsBasedOnReference.setChecked(false);
		_onlyAspectsBasedOnReference.setEnabled(false);
		_onlyAspectsBasedOnReference.setImageDescriptor(_imageReg.getDescriptor(IconsInternal.REFERENCE));

		_onlyAspectsAboutReference = new Action(NLMessages.getString("View_action_only_mentioning_reference"))
		{
			@Override
			public void run()
			{
				updateFilter(_onlyAspectsAboutReference);
			}
		};
		_onlyAspectsAboutReference.setChecked(false);
		_onlyAspectsAboutReference.setEnabled(false);
		_onlyAspectsAboutReference.setImageDescriptor(_imageReg.getDescriptor(IconsInternal.REFERENCE_ASPECTS));


		_referenceFilterAction = new Action(NLMessages.getString("View_action_reference_filter"))
		{
			@Override
			public void run()
			{
				IWorkbench workbench = PlatformUI.getWorkbench();
				Display display = workbench.getDisplay();
				Shell shell = new Shell(display);
				FilterSelectionDialog dialog = new FilterSelectionDialog(shell, _pdrObjectsProvider, "reference"); //$NON-NLS-1$
				dialog.open();
				if (dialog.getReturnCode() == 0)
				{
					disposeTabItems();
					buildCategories(_currentObjects, null);
				}
				_referenceFilterAction.setChecked(_pdrObjectsProvider.hasReferenceFilter());
			}
		};
		_referenceFilterAction.setChecked(false);
		_referenceFilterAction.setEnabled(false);
		_referenceFilterAction.setImageDescriptor(_imageReg.getDescriptor(IconsInternal.REFERENCES));

		_personFilterAction = new Action(NLMessages.getString("View_action_person_filter"))
		{
			@Override
			public void run()
			{
				IWorkbench workbench = PlatformUI.getWorkbench();
				Display display = workbench.getDisplay();
				Shell shell = new Shell(display);
				FilterSelectionDialog dialog = new FilterSelectionDialog(shell, _pdrObjectsProvider, "person"); //$NON-NLS-1$
				dialog.open();
				if (dialog.getReturnCode() == 0)
				{
					disposeTabItems();
					buildCategories(_currentObjects, null);
				}
				_personFilterAction.setChecked(_pdrObjectsProvider.hasPersonFilter());
			}
		};
		_personFilterAction.setChecked(false);
		_personFilterAction.setEnabled(false);
		_personFilterAction.setImageDescriptor(_imageReg.getDescriptor(IconsInternal.PERSONS));

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

		_yearFilterAction = new Action(NLMessages.getString("View_action_time_filter"))
		{
			@Override
			public void run()
			{
				IWorkbench workbench = PlatformUI.getWorkbench();
				Display display = workbench.getDisplay();
				Shell shell = new Shell(display);
				FilterSelectionDialog dialog = new FilterSelectionDialog(shell, _pdrObjectsProvider, "year"); //$NON-NLS-1$
				dialog.open();
				if (dialog.getReturnCode() == 0)
				{
					disposeTabItems();
					buildCategories(_currentObjects, null);
				}
				_yearFilterAction.setChecked(_pdrObjectsProvider.hasYearFilter());
			}
		};
		_yearFilterAction.setChecked(false);
		_yearFilterAction.setEnabled(false);
		_yearFilterAction.setImageDescriptor(_imageReg.getDescriptor(IconsInternal.TIME));

		_userFilterAction = new Action(NLMessages.getString("View_action_user_filter"))
		{
			@Override
			public void run()
			{
				IWorkbench workbench = PlatformUI.getWorkbench();
				Display display = workbench.getDisplay();
				Shell shell = new Shell(display);
				FilterSelectionDialog dialog = new FilterSelectionDialog(shell, _pdrObjectsProvider, "user"); //$NON-NLS-1$
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
	 * Creates the additional aspects selection adapter.
	 */
	private void createAdditionalAspectsSelectionAdapter()
	{
		_aspectsSelectionAdapter = new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent ev)
			{
				//			 System.out.println("Selection: " + ev.text); //$NON-NLS-1$
				Link button = (Link) ev.getSource();
				Event event = new Event();
				event.data = button.getData();
				IHandlerService handlerService = (IHandlerService) PlatformUI.getWorkbench().getService(
						IHandlerService.class);
				try
				{
					handlerService
							.executeCommand("org.bbaw.pdr.ae.view.main.commands.OpenAdditionalAspectsView", event); //$NON-NLS-1$
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

		getSite().registerContextMenu(menuMgr, AspectsView.this);
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
				Aspect a = _facade.getCurrentAspect();
				if (a != null && a.getPdrId() != null)
				{
					Event event = new Event();
					event.data = a.getPdrId().toString();
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

				Aspect a = _facade.getCurrentAspect();
				if (a != null && a.getPdrId() != null)
				{
					Event event = new Event();
					event.data = a.getPdrId().toString();
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
		_onlyNewAspectsFilter = new OnlyNewPDRObjectsFilter();
		_onlyUpdatedAspectsFilter = new OnlyUpdatedPDRObjectsFilter();
		_onlyAspectsDivergentMarkupFilter = new OnlyAspectDivergentMarkup();
		_aspectExludeObjectRelationsFilter = new AspectExcludeObjectRelationsFilter(null);

		_onlyAspectsBasedOnReferenceFilter = new OnlyAspectsBasedOnReferenceFilter(null);
		_onlyAspectsAboutReferenceFilter = new OnlyAspectsAboutReferenceFilter(null);
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
		getViewSite().getActionBars().getToolBarManager().removeAll();
		getViewSite().getActionBars().getToolBarManager().add(_onlyNewAspectsAction);
		getViewSite().getActionBars().getToolBarManager().add(_personFilterAction);
		getViewSite().getActionBars().getToolBarManager().add(_referenceFilterAction);
		getViewSite().getActionBars().getToolBarManager().add(_semanticFilterAction);
		getViewSite().getActionBars().getToolBarManager().add(_yearFilterAction);

		if (_currentObjects != null && _currentObjects.length > 0 && _currentObjects[0] instanceof ReferenceMods)
		{
			getViewSite().getActionBars().getToolBarManager().add(_onlyAspectsBasedOnReference);
			getViewSite().getActionBars().getToolBarManager().add(_onlyAspectsAboutReference);
		}

		getViewSite().getActionBars().updateActionBars();
	}

	@Override
	public final void createPartControl(final Composite parent)
	{
		parent.setLayout(new GridLayout());
		String secId = this.getViewSite().getSecondaryId();
		if (secId != null)
		{
			_concurring = (secId.equals("b")); //$NON-NLS-1$
		}
		_provider = Platform
				.getPreferencesService()
				.getString(CommonActivator.PLUGIN_ID, "PRIMARY_SEMANTIC_PROVIDER",
						AEConstants.CLASSIFICATION_AUTHORITY, null).toUpperCase(); //$NON-NLS-1$
		_mainComposite = new Composite(parent, SWT.NULL);
		_mainComposite.setLayoutData(new GridData());
		((GridData) _mainComposite.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _mainComposite.getLayoutData()).verticalAlignment = SWT.FILL;
		((GridData) _mainComposite.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) _mainComposite.getLayoutData()).grabExcessVerticalSpace = true;
		_mainComposite.setLayout(new GridLayout());
		_pdrObjectsProvider.setOrderer(_ordererFactory.createAspectOrderer("aspect.semantic"));

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
			all.setText(NLMessages.getString("View_number_of_allAspects"));
			_allAspects = new Text(alltitleComp, SWT.NONE | SWT.READ_ONLY);
			_allAspects.setLayoutData(new GridData());
			((GridData) _allAspects.getLayoutData()).widthHint = 40;
			Composite ordertitleComp = new Composite(topComposite, SWT.NONE);
			ordertitleComp.setLayout(new GridLayout());
			((GridLayout) ordertitleComp.getLayout()).numColumns = 3;
			((GridLayout) ordertitleComp.getLayout()).marginHeight = 0;
			((GridLayout) ordertitleComp.getLayout()).marginWidth = 0;

			Label order = new Label(ordertitleComp, SWT.NONE);
			order.setText(NLMessages.getString("View_group_by"));
			_orderCombo = new Combo(ordertitleComp, SWT.NONE | SWT.READ_ONLY);
			String[] oderers = new String[]
			{NLMessages.getString("View_group_all"), NLMessages.getString("View_group_semantic"),
					NLMessages.getString("View_group_year"), NLMessages.getString("View_group_place"),
					NLMessages.getString("View_group_reference"), NLMessages.getString("View_group_relation"),
					NLMessages.getString("View_group_markup"), NLMessages.getString("View_group_person"),
					NLMessages.getString("View_group_user")};
			_orderCombo.setItems(oderers);
			_orderCombo.select(1);

			_orderCombo.addSelectionListener(new SelectionAdapter()
			{
				@Override
				public void widgetSelected(final SelectionEvent event)
				{
				}
			}); // SelectionListener

			_orderButton = new Button(ordertitleComp, SWT.NONE | SWT.READ_ONLY);
			_orderButton.setText(NLMessages.getString("View_group"));
			_orderButton.setToolTipText(NLMessages.getString("View_group_tooltip"));
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
							break;
						case 1:
							_pdrObjectsProvider.setOrderer(_ordererFactory.createAspectOrderer("aspect.semantic"));
							break;
						case 2:
							_pdrObjectsProvider.setOrderer(_ordererFactory.createAspectOrderer("aspect.year"));
							break;
						case 3:
							_pdrObjectsProvider.setOrderer(_ordererFactory.createAspectOrderer("aspect.place"));
							break;
						case 4:
							_pdrObjectsProvider.setOrderer(_ordererFactory.createAspectOrderer("aspect.reference"));
							break;
						case 5:
							_pdrObjectsProvider.setOrderer(_ordererFactory.createAspectOrderer("aspect.relation"));
							break;
						case 6:
							_pdrObjectsProvider.setOrderer(_ordererFactory.createAspectOrderer("aspect.markup"));
							break;
						case 7:
							_pdrObjectsProvider.setOrderer(_ordererFactory.createAspectOrderer("aspect.person"));
							break;
						case 8:
							_pdrObjectsProvider.setOrderer(_ordererFactory.createAspectOrderer("aspect.user"));
							break;
						default:
							break;
					}
					disposeTabItems();
					_currentCategoryID = 0;
					if (_concurring)
					{
						buildCategories(new Person[]
						{_facade.getConcurringPerson()}, _facade.getCurrentAspect());
					}
					else
					{
						buildCategories(_facade.getCurrentTreeObjects(), _facade.getCurrentAspect());
					}
					_tabFolderRight.setSelection(0);
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
			{NLMessages.getString("View_sort_cronologically"), NLMessages.getString("View_sort_semantic"),
					NLMessages.getString("View_sort_revision"), NLMessages.getString("View_sort_reference"),
					NLMessages.getString("View_sort_creator")};
			_sortedCombo.setItems(sorters);
			_sortedCombo.select(0);
			_sortedCombo.addSelectionListener(new SelectionAdapter()
			{
				private int current_selection = _sortedCombo.getSelectionIndex();
				@Override
				public void widgetSelected(final SelectionEvent event)
				{
					// activate sorting order buttons in case sorting criterion has been changed.
					if (_sortedCombo.getSelectionIndex() != current_selection) {
						_sortButtonDesc.setEnabled(true);
						_sortButtonAsc.setEnabled(true);
						current_selection = _sortedCombo.getSelectionIndex(); 
					}
				}
			}); // SelectionListener

			
			// Button for sorting in ascending order
			_sortButtonAsc = new Button(sorttitleComp, SWT.NONE | SWT.READ_ONLY);
			_sortButtonAsc.setToolTipText(NLMessages.getString("View_sort_asc_tooltip"));
			_sortButtonAsc.setImage(_imageReg.get(IconsInternal.SORT_ALPHABETIC_ASC));
			_sortButtonAsc.setEnabled(false);
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
							_pdrObjectsProvider.setComparator(new AspectsByCronComparator(true));
							break;
						case 1:
							_pdrObjectsProvider.setComparator(new AspectsBySemanticComparator(_provider, true));
							break;
						case 2:
							_pdrObjectsProvider.setComparator(new AspectsByRecentChangesComparator(true));
							break;
						case 3:
							_pdrObjectsProvider.setComparator(new AspectsByReferenceComparator(true));
							break;
						case 4:
							_pdrObjectsProvider.setComparator(new AspectsByCreatorComparator(true));
							break;
						default:
							break;
					}
					disposeTabItems();
					if (_concurring)
					{
						// loadPdrObject(_facade.getConcurringPerson());
						buildCategories(new Person[]
						{_facade.getConcurringPerson()}, _facade.getCurrentAspect());
					}
					else
					{
						buildCategories(_facade.getCurrentTreeObjects(), _facade.getCurrentAspect());
					}
					_tabFolderRight.setSelection(_currentCategoryID);
				}
			}); // SelectionListener			
			
			
			// button for sorting in descending order
			_sortButtonDesc = new Button(sorttitleComp, SWT.NONE | SWT.READ_ONLY);
			_sortButtonDesc.setToolTipText(NLMessages.getString("View_sort_tooltip"));
			_sortButtonDesc.setImage(_imageReg.get(IconsInternal.SORT_ALPHABETIC_DESC));
			_sortButtonDesc.setEnabled(true);
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
							_pdrObjectsProvider.setComparator(new AspectsByCronComparator(false));
							break;
						case 1:
							_pdrObjectsProvider.setComparator(new AspectsBySemanticComparator(_provider, false));
							break;
						case 2:
							_pdrObjectsProvider.setComparator(new AspectsByRecentChangesComparator(false));
							break;
						case 3:
							_pdrObjectsProvider.setComparator(new AspectsByReferenceComparator(false));
							break;
						case 4:
							_pdrObjectsProvider.setComparator(new AspectsByCreatorComparator(false));
							break;
						default:
							break;
					}
					disposeTabItems();
					if (_concurring)
					{
						buildCategories(new Person[]
						{_facade.getConcurringPerson()}, _facade.getCurrentAspect());
					}
					else
					{
						buildCategories(_facade.getCurrentTreeObjects(), _facade.getCurrentAspect());
					}
					_tabFolderRight.setSelection(_currentCategoryID);

				}
			}); // SelectionListener

			
			Composite searchComp = new Composite(topComposite, SWT.NONE);
			searchComp.setLayout(new GridLayout());
			((GridLayout) searchComp.getLayout()).numColumns = 3;
			((GridLayout) searchComp.getLayout()).marginHeight = 0;
			((GridLayout) searchComp.getLayout()).marginWidth = 0;

			Label iconLable = new Label(searchComp, SWT.NONE);
			iconLable.setImage(_imageReg.get(IconsInternal.FILTER));
			iconLable.setLayoutData(new GridData());
			((GridData) iconLable.getLayoutData()).horizontalIndent = 8;

			_searchText = new Text(searchComp, SWT.BORDER);
			_searchText.setLayoutData(new GridData());
			((GridData) _searchText.getLayoutData()).horizontalAlignment = SWT.FILL;
			((GridData) _searchText.getLayoutData()).grabExcessHorizontalSpace = true;
			((GridData) _searchText.getLayoutData()).minimumWidth = 80;

			_searchButton = new Button(searchComp, SWT.PUSH);
			_searchText.addKeyListener(new KeyListener()
			{
				@Override
				public void keyPressed(final KeyEvent e)
				{
					if (e.keyCode == SWT.CR)
					{
						String search = _searchText.getText();
						AspectSearchTextFilter filter = _pdrObjectsProvider.getSearchFilter();
						if (search.trim().length() > 0)
						{
							if (filter != null)
							{
								filter.setSearchText(search);
							}
							else
							{
								filter = new AspectSearchTextFilter(search);
								_pdrObjectsProvider.addFilter(filter);
							}
						}
						else
						{
							if (filter != null)
							{
								_pdrObjectsProvider.removeFilter(filter);
							}
						}
						disposeTabItems();
						_currentCategoryID = 0;
						if (_concurring)
						{
							buildCategories(new Person[]
							{_facade.getConcurringPerson()}, _facade.getCurrentAspect());
						}
						else
						{
							buildCategories(_facade.getCurrentTreeObjects(), _facade.getCurrentAspect());
						}
					}
				}

				@Override
				public void keyReleased(final KeyEvent e)
				{
					if (_searchText.getText().trim().length() == 0)
					{
						AspectSearchTextFilter filter = _pdrObjectsProvider.getSearchFilter();
						if (filter != null)
						{
							_pdrObjectsProvider.removeFilter(filter);
							disposeTabItems();
							if (_concurring)
							{
								buildCategories(new Person[]
								{_facade.getConcurringPerson()}, _facade.getCurrentAspect());
							}
							else
							{
								buildCategories(_facade.getCurrentTreeObjects(), _facade.getCurrentAspect());
							}
						}
					}
				}
			});
			_searchButton.setToolTipText(NLMessages.getString("View_search_notification"));
			_searchButton.setImage(_imageReg.get(IconsInternal.SEARCH));
			_searchButton.setEnabled(true);
			_searchButton.addSelectionListener(new SelectionAdapter()
			{
				@Override
				public void widgetSelected(final SelectionEvent event)
				{
					String search = _searchText.getText();
					AspectSearchTextFilter filter = _pdrObjectsProvider.getSearchFilter();
					if (search.trim().length() > 0)
					{
						if (filter != null)
						{
							filter.setSearchText(search);
						}
						else
						{
							filter = new AspectSearchTextFilter(search);
							_pdrObjectsProvider.addFilter(filter);
						}
					}
					else
					{
						if (filter != null)
						{
							_pdrObjectsProvider.removeFilter(filter);
						}
					}
					disposeTabItems();
					_currentCategoryID = 0;
					if (_concurring)
					{
						buildCategories(new Person[]
						{_facade.getConcurringPerson()}, _facade.getCurrentAspect());
					}
					else
					{
						buildCategories(_facade.getCurrentTreeObjects(), _facade.getCurrentAspect());
					}
				}
			});
			topComposite.layout();
			topComposite.pack();
			_mainComposite.layout();
			_mainComposite.pack();
		}
		createFilters();
		createActions();
		createContextMenuAction();
		createAdditionalAspectsSelectionAdapter();
		createMenus();
		if (secId != null)
		{
			if (secId.startsWith("pdrPo")) //$NON-NLS-1$
			{
				Person p = _facade.getPerson(new PdrId(secId));
				if (p != null)
				{
					loadPdrObject(new Person[]
					{p});
					setPartName(p.getDisplayName(27));
					setTitleToolTip(p.getDisplayName());
				}
				_additional = true;
			}
			else if (secId.startsWith("pdrRo")) //$NON-NLS-1$
			{
				ReferenceMods r = _facade.getReference(new PdrId(secId));
				_additional = true;
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
				Aspect a = _facade.getLoadedAspects().get(secId);
				_additional = true;
				if (a != null)
				{
					loadPdrObject(new Aspect[]
					{a});
					setPartName(a.getDisplayName(27));
					setTitleToolTip(a.getDisplayName());
				}
			}
			else if (secId.startsWith("a") || secId.startsWith("b")) //$NON-NLS-1$ //$NON-NLS-2$
			{
				if (_advanced)
				{
					_orderCombo.setEnabled(false);
					_orderButton.setEnabled(false);
					_sortedCombo.setEnabled(false);
					_sortButtonAsc.setEnabled(false);
					_sortButtonDesc.setEnabled(false);
					_searchText.setEnabled(false);
					_searchButton.setEnabled(false);
				}
			}
		}
		else
		{
			if (_advanced)
			{
				_orderCombo.setEnabled(false);
				_orderButton.setEnabled(false);
				_sortedCombo.setEnabled(false);
				_sortButtonAsc.setEnabled(false);
				_sortButtonDesc.setEnabled(false);
				_searchText.setEnabled(false);
				_searchButton.setEnabled(false);
			}
		}
		getSite().setSelectionProvider(AspectsView.this);
		_facade.addObserver(this);
	}

	@Override
	public final void dispose()
	{
		_facade.deleteObserver(AspectsView.this);

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

		if (_tabFolderRight != null)
		{
			_tabFolderRight.dispose();
		}

	}

	/**
	 * Fill menu.
	 * @param rootMenuManager the root menu manager
	 */
	private void fillMenu(final IMenuManager rootMenuManager)
	{

		rootMenuManager.removeAll();
		rootMenuManager.add(_onlyNewAspectsAction);
		rootMenuManager.add(_onlyUpdatedAspectsAction);
		rootMenuManager.add(_onlyAspectsWithDivergentMarkup);
		rootMenuManager.add(_excludeObjectRelationsAction);
		rootMenuManager.add(_referenceFilterAction);
		rootMenuManager.add(_personFilterAction);
		rootMenuManager.add(_semanticFilterAction);
		rootMenuManager.add(_yearFilterAction);
		rootMenuManager.add(_userFilterAction);
		if (_currentObjects != null && _currentObjects.length > 0 && _currentObjects[0] instanceof ReferenceMods)
		{
			rootMenuManager.add(_onlyAspectsBasedOnReference);
			rootMenuManager.add(_onlyAspectsAboutReference);
		}

	}

	// /**
	// * Gets the label of config data.
	// * @param element the element
	// * @param type the type
	// * @param subtype the subtype
	// * @param role the role
	// * @return the label of config data
	// */
	// private String getLabelOfConfigData(String element, final String type,
	// final String subtype, final String role)
	// {
	// if (!element.startsWith("aodl:"))
	// {
	//			element = "aodl:" + element; //$NON-NLS-1$ //$NON-NLS-2$
	// }
	// String label = null;
	// Vector<String> providers = new Vector<String>();
	// for (String s : _facade.getConfigs().keySet())
	// {
	// if (!s.equals(_provider))
	// {
	// providers.add(s);
	// }
	// }
	// HashMap<String, ConfigData> configs = new HashMap<String, ConfigData>();
	// if (element != null && type == null)
	// {
	//
	// if (_facade.getConfigs().containsKey(_provider)
	// && _facade.getConfigs().get(_provider).getChildren() != null
	// &&
	// _facade.getConfigs().get(_provider).getChildren().containsKey(element))
	// {
	// configs.putAll(_facade.getConfigs().get(_provider).getChildren());
	// }
	// if (configs.get(element) != null)
	// {
	// label = configs.get(element).getLabel();
	// }
	// else
	// {
	// label = element;
	// }
	// }
	// else if (element != null && type != null && subtype == null)
	// {
	//			//			System.out.println("get label for type " + type); //$NON-NLS-1$
	// for (String p : providers)
	// {
	// if (_facade.getConfigs().get(p).getChildren().containsKey(element))
	// {
	// configs.putAll(_facade.getConfigs().get(p).getChildren().get(element).getChildren());
	// }
	// }
	//			//			System.out.println("markupprovider " + _provider); //$NON-NLS-1$
	//			//			System.out.println("config size1 " + configs.size()); //$NON-NLS-1$
	//
	// if (_facade.getConfigs().containsKey(_provider)
	// && _facade.getConfigs().get(_provider).getChildren() != null
	// &&
	// _facade.getConfigs().get(_provider).getChildren().containsKey(element))
	// {
	// configs.putAll(_facade.getConfigs().get(_provider).getChildren().get(element).getChildren());
	// }
	//			//			System.out.println("config size2 " + configs.size()); //$NON-NLS-1$
	//
	// if (configs.get(type) != null)
	// {
	// label = configs.get(type).getLabel();
	// }
	// else
	// {
	// label = type;
	//				//			System.out.println("get label for label " + label); //$NON-NLS-1$
	// }
	//
	// }
	// else if (element != null && type != null && subtype != null && role ==
	// null)
	// {
	// for (String provider : providers)
	// {
	// if (_facade.getConfigs().get(provider).getChildren().containsKey(element)
	// &&
	// _facade.getConfigs().get(provider).getChildren().get(element).getChildren()
	// != null
	// &&
	// _facade.getConfigs().get(provider).getChildren().get(element).getChildren()
	// .containsKey(type))
	// {
	// configs.putAll(_facade.getConfigs().get(provider).getChildren().get(element).getChildren()
	// .get(type).getChildren());
	// }
	// }
	// if (_facade.getConfigs().containsKey(_provider)
	// && _facade.getConfigs().get(_provider).getChildren() != null
	// && _facade.getConfigs().get(_provider).getChildren().containsKey(element)
	// &&
	// _facade.getConfigs().get(_provider).getChildren().get(element).getChildren()
	// != null
	// &&
	// _facade.getConfigs().get(_provider).getChildren().get(element).getChildren().containsKey(type))
	// {
	// configs.putAll(_facade.getConfigs().get(_provider).getChildren().get(element).getChildren().get(type)
	// .getChildren());
	// }
	// if (configs.get(subtype) != null)
	// {
	// label = configs.get(subtype).getLabel();
	// }
	// else
	// {
	// label = subtype;
	// }
	// }
	// else if (element != null && type != null && subtype != null && role !=
	// null)
	// {
	// for (String provider : providers)
	// {
	// if (_facade.getConfigs().get(provider).getChildren().containsKey(element)
	// &&
	// _facade.getConfigs().get(provider).getChildren().get(element).getChildren()
	// != null
	// &&
	// _facade.getConfigs().get(provider).getChildren().get(element).getChildren()
	// .containsKey(type)
	// &&
	// _facade.getConfigs().get(provider).getChildren().get(element).getChildren().get(type)
	// .getChildren() != null
	// &&
	// _facade.getConfigs().get(provider).getChildren().get(element).getChildren().get(type)
	// .getChildren().containsKey(subtype))
	// {
	// configs.putAll(_facade.getConfigs().get(provider).getChildren().get(element).getChildren()
	// .get(type).getChildren().get(subtype).getChildren());
	// }
	// }
	// if (_facade.getConfigs().containsKey(_provider)
	// && _facade.getConfigs().get(_provider).getChildren() != null
	// && _facade.getConfigs().get(_provider).getChildren().containsKey(element)
	// &&
	// _facade.getConfigs().get(_provider).getChildren().get(element).getChildren()
	// != null
	// &&
	// _facade.getConfigs().get(_provider).getChildren().get(element).getChildren().containsKey(type)
	// &&
	// _facade.getConfigs().get(_provider).getChildren().get(element).getChildren().get(type)
	// .getChildren() != null
	// &&
	// _facade.getConfigs().get(_provider).getChildren().get(element).getChildren().get(type)
	// .getChildren().containsKey(subtype))
	// {
	// configs.putAll(_facade.getConfigs().get(_provider).getChildren().get(element).getChildren().get(type)
	// .getChildren().get(subtype).getChildren());
	// }
	//
	// if (configs.get(role) != null)
	// {
	// label = configs.get(role).getLabel();
	// }
	// else
	// {
	// label = role;
	// }
	// }
	// return label;
	// }
	//
	@Override
	public final ISelection getSelection()
	{
		StructuredSelection selection;
		if (_currentAspect != null)
		{
			selection = new StructuredSelection(new PdrObject[]
			{_currentAspect});
		}
		else
		{
			selection = new StructuredSelection(new PdrObject[]
			{});
		}
		return selection;
	}

	/**
	 * Gets the semantic label.
	 * @param prov the prov
	 * @param semantic the semantic
	 * @return the semantic label
	 */

	/**
	 * meth load one aspect, sets layout of text, sets stext and adds
	 * mouselistener for message box and context menu.
	 * @param ca current aspect
	 * @param categorieID category id
	 * @param textComposite textcomposite
	 * @param ti tabitem
	 * @param isSavedAspect aspect is saved
	 * @param markupPresentation markup presentation
	 */
	private void loadAspect(final Aspect ca, final Integer categorieID, final Composite textComposite,
			final CTabItem ti, final boolean isSavedAspect, final IMarkupPresentation markupPresentation)
	{
		markupPresentation.setAspect(ca);
		markupPresentation.setComposite(textComposite);
		markupPresentation.setSelected(isSavedAspect);
		if (isSavedAspect)
		{
			_tabFolderRight.setData("lastSelected", markupPresentation); //$NON-NLS-1$
		}
		markupPresentation.createPresentation();
		createContextMenu(markupPresentation.getControl());

		final MarkupTooltip markupTooltipLabel = new MarkupTooltip(textComposite);
		markupTooltipLabel.setPopupDelay(0);
		markupTooltipLabel.setHideOnMouseDown(true);
		markupTooltipLabel.deactivate();

		markupPresentation.addSelectionListener(new Listener()
		{

			@Override
			public void handleEvent(final Event arg0)
			{
				IMarkupPresentation current = (IMarkupPresentation) arg0.data;
				_currentAspect = current.getAspect();
				_facade.setCurrentAspect(_currentAspect); //$NON-NLS-1$
				IMarkupPresentation last = (IMarkupPresentation) _tabFolderRight.getData("lastSelected"); //$NON-NLS-1$
				(current).setSelected(true);
				IStatus sca = new Status(IStatus.INFO, Activator.PLUGIN_ID,
						"AspectsView current aspect: " + _facade.getCurrentAspect().getPdrId().toString()); //$NON-NLS-1$
				iLogger.log(sca);
				if (last != null && !last.equals(current))
				{
					last.setSelected(false);
				}
				_tabFolderRight.setData("lastSelected", current); //$NON-NLS-1$

			}
		});
		markupPresentation.addMarkupSelectionListener(new Listener()
		{
			@Override
			public void handleEvent(final Event event)
			{
				TaggingRange tr = (TaggingRange) event.data;
				if (tr != null)
				{
					String message;
					if (!tr.getName().equals("date")) //$NON-NLS-1$
					{
						message = NLMessages.getString("View_markupName")
								+ PDRConfigProvider.getLabelOfMarkup(tr.getName(), null, null, null) + "\n"; //$NON-NLS-1$//$NON-NLS-2$
						if (tr.getType() != null)
						{
							message = message + NLMessages.getString("View_type")
									+ PDRConfigProvider.getLabelOfMarkup(tr.getName(), tr.getType(), null, null) + "\n"; //$NON-NLS-1$//$NON-NLS-2$
						}
						if (tr.getSubtype() != null && tr.getSubtype().trim().length() > 0)
						{
							message = message + NLMessages.getString("View_subtype")
									+ PDRConfigProvider.getLabelOfMarkup(tr.getName(), tr.getType(), tr.getSubtype(),
											null) + "\n"; //$NON-NLS-1$//$NON-NLS-2$
						}
						if (tr.getRole() != null && tr.getRole().trim().length() > 0)
						{
							message = message + NLMessages.getString("View_role")
									+ PDRConfigProvider.getLabelOfMarkup(tr.getName(), tr.getType(), tr.getSubtype(),
											tr.getRole())
									+ "\n"; //$NON-NLS-1$//$NON-NLS-2$
						}
						if (tr.getKey() != null && tr.getKey().trim().length() > 0)
						{
							message = message + NLMessages.getString("View_key") + tr.getKey(); //$NON-NLS-1$
							PdrObject o = _facade.getPdrObject(new PdrId(tr.getKey()));
							if (o != null)
							{
								message = message + " " + o.getDisplayName(); //$NON-NLS-1$
							}
							else
							{
								message = message + NLMessages.getString("View_message_missing_dataObject");
							}
						}
						if (tr.getAna() != null && tr.getAna().trim().length() > 0)
						{
							message = message + "\n" + NLMessages.getString("View_lb_lb_owner");
							PdrObject o = _facade.getPdrObject(new PdrId(tr.getAna()));
							if (o != null)
							{
								message = message + " " + o.getDisplayNameWithID(); //$NON-NLS-1$
							}
							else
							{
								message = message + NLMessages.getString("View_message_missing_dataObject");
							}
						}
						message = message + "\n" + NLMessages.getString("View_lb_content") + tr.getTextValue();
					}
					else if (tr.getName().equals("date")) //$NON-NLS-1$
					{
						message = NLMessages.getString("View_MarkupDate")
								+ PDRConfigProvider.getLabelOfMarkup(tr.getName(), null, null, null) + "\n"; //$NON-NLS-1$//$NON-NLS-2$
						if (tr.getType() != null)
						{
							message = message + NLMessages.getString("View_type")
									+ PDRConfigProvider.getLabelOfMarkup(tr.getName(), tr.getType(), null, null) + "\n"; //$NON-NLS-1$//$NON-NLS-2$
						}
						if (tr.getSubtype() != null && tr.getSubtype().trim().length() > 0)
						{
							message = message + NLMessages.getString("View_subtype")
									+ PDRConfigProvider.getLabelOfMarkup(tr.getName(), tr.getType(), tr.getSubtype(),
											null) + "\n"; //$NON-NLS-1$//$NON-NLS-2$
						}
						if (tr.getWhen() != null)
						{
							message = message + NLMessages.getString("View_when") + tr.getWhen().toString(); //$NON-NLS-1$

						}
						if (tr.getFrom() != null)
						{
							message = message + NLMessages.getString("View_from") + tr.getFrom().toString(); //$NON-NLS-1$

						}
						if (tr.getTo() != null)
						{
							message = message + "\n" + NLMessages.getString("View_to") + tr.getTo().toString(); //$NON-NLS-1$

						}
						if (tr.getNotBefore() != null)
						{
							message = message + NLMessages.getString("View_notBefore") + tr.getNotBefore().toString(); //$NON-NLS-1$

						}
						if (tr.getNotAfter() != null)
						{
							message = message
									+ "\n" + NLMessages.getString("View_NotAfter") + tr.getNotAfter().toString(); //$NON-NLS-1$

						}
						if (tr.getAna() != null && tr.getAna().trim().length() > 0)
						{
							message = message + "\n" + NLMessages.getString("View_lb_lb_owner");
							PdrObject o = _facade.getPdrObject(new PdrId(tr.getAna()));
							if (o != null)
							{
								message = message + " " + o.getDisplayNameWithID(); //$NON-NLS-1$
							}
							else
							{
								message = message + NLMessages.getString("View_message_missing_dataObject");
							}
						}
						message = message + "\n" + NLMessages.getString("View_lb_content") + tr.getTextValue();
					}
					else
					{
						message = NLMessages.getString("View_errorMarkupInfo"); //$NON-NLS-1$
					}
					//	    							MessageDialog.openInformation(parentShell, "", message); //$NON-NLS-1$
					// break;
					// System.out.println("open message " + message);
					markupTooltipLabel.setToolTipText(message);
					// markupTooltipLabel.activate();
					markupTooltipLabel.show(new Point(event.x + 5, event.y + 10));
				}
				else
				{
					markupTooltipLabel.hide();
				}

			}
		});

		markupPresentation.addDoubleClickListener(new Listener()
		{

			@Override
			public void handleEvent(final Event event)
			{
				IHandlerService handlerService = (IHandlerService) PlatformUI.getWorkbench().getService(
						IHandlerService.class);
				try
				{
					handlerService.executeCommand("org.bbaw.pdr.ae.view.main.commands.CallAspectEditor", null); //$NON-NLS-1$
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
		ViewHelper.equipWithMouseExitListener(markupPresentation.getControl(), markupTooltipLabel);

		// XXX RAP auskommentieren
		// markupPresentation.getControl().addListener(SWT.MouseExit, new
		// Listener()
		// {
		// public void handleEvent(Event event)
		// {
		// switch (event.type)
		// {
		// case SWT.MouseEnter:
		// case SWT.MouseMove:
		// markupTooltipLabel.hide();
		// case SWT.MouseExit:
		// markupTooltipLabel.hide();
		// break;
		// }
		// }
		// });
	}

	/**
	 * meth. loads all aspects that belong to the same currently selected
	 * category.
	 * @param orderedAspects ordered aspects
	 * @param categorieID category id
	 * @param startIndex current start index
	 * @param number number of aspects to show on one page
	 * @param savedAspect saved aspect
	 */
	private void loadAspects(final Vector<OrderingHead> orderedAspects, final int categorieID, final int startIndex,
			final int number, final Aspect savedAspect)
	{
		final int start;
		//		System.out.println("tabmap hast mapping für " + categorieID + "? : " + tabMap.containsKey(categorieID)); //$NON-NLS-1$ //$NON-NLS-2$
		// System.out.println(tabMap.get(categorieID));
		CTabItem ti = _tabMap.get(categorieID);
		ti.setData("loaded", "true"); //$NON-NLS-1$ //$NON-NLS-2$

		ScrolledComposite sc = (ScrolledComposite) ti.getControl();
		Composite comp = (Composite) sc.getContent();
		Control[] children = comp.getChildren();
		for (Control c : children)
		{
			c.dispose();
		}
		if (!orderedAspects.get(categorieID).isSorted())
		{
			_pdrObjectsProvider.sort(orderedAspects.get(categorieID));
		}
		_aspects = orderedAspects.get(categorieID).getAspects();
		// Collections.sort(aspects, new AspectsByCronComparator());
		IStatus sla = new Status(IStatus.INFO, Activator.PLUGIN_ID,
				"CategoryView load aspects - number of aspects: " + _aspects.size()); //$NON-NLS-1$ //$NON-NLS-2$
		iLogger.log(sla);

		if (_aspects != null)
		{
			final int size = _aspects.size(); //$NON-NLS-1$
			Label eventNumber = new Label(comp, SWT.NONE);
			int endIndex = startIndex + 10;
			if (endIndex > size)
			{
				endIndex = size;
			}
			eventNumber
					.setText(NLMessages.getString("View_allTogether") + " " + size + " " + NLMessages.getString("View_aspects") + //$NON-NLS-1$ //$NON-NLS-2$
					" " + (startIndex + 1) + " - " + endIndex); //$NON-NLS-1$
			if (size > 10)
			{

				start = startIndex;

				Group eventNavBar = new Group(comp, SWT.NONE);
				eventNavBar.setText(NLMessages.getString("View_scroll")); //$NON-NLS-1$
				eventNavBar.setLayout(new RowLayout());

				Button toStart = new Button(eventNavBar, SWT.PUSH);
				toStart.setText(" |< "); //$NON-NLS-1$
				toStart.addSelectionListener(new SelectionAdapter()
				{

					@Override
					public void widgetSelected(final SelectionEvent arg0)
					{
						loadAspects(orderedAspects, categorieID, 0, number, null);
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

						loadAspects(orderedAspects, categorieID, nextStart, number, null);
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

						loadAspects(orderedAspects, categorieID, nextStart, number, null);
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

						loadAspects(orderedAspects, categorieID, nextStart, number, null);
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
						loadAspects(orderedAspects, categorieID, nextStart, number, null);
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
									loadAspects(orderedAspects, categorieID, --n, number, null);
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

						loadAspects(orderedAspects, categorieID, nextStart, number, null);
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

						loadAspects(orderedAspects, categorieID, nextStart, number, null);
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

						loadAspects(orderedAspects, categorieID, nextStart, number, null);
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

						loadAspects(orderedAspects, categorieID, nextStart, number, null);
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
					final Aspect ca = _aspects.get(i); //$NON-NLS-1$
					RightsChecker rc = new RightsChecker();
					final IMarkupPresentation markupPresentation = ControlExtensions.createMarkupPresentation();

					if (rc.mayRead(ca) && markupPresentation != null)
					{
						Composite textComp = new Composite(comp, SWT.LEFT | SWT.BORDER);
						textComp.setBackground(WHITE_COLOR);
						textComp.setLayoutData(new GridData());
						((GridData) textComp.getLayoutData()).grabExcessHorizontalSpace = true;
						((GridData) textComp.getLayoutData()).horizontalAlignment = SWT.FILL;

						textComp.setLayout(new GridLayout());
						((GridLayout) textComp.getLayout()).numColumns = 2;
						((GridLayout) textComp.getLayout()).marginHeight = 0;
						((GridLayout) textComp.getLayout()).marginWidth = 0;

						// StyledText stext = new StyledText(textComp, SWT.WRAP
						// | SWT.NO_BACKGROUND | SWT.NO_FOCUS | SWT.CURSOR_ARROW
						// );
						loadAspect(ca, categorieID, textComp, ti, ca.equals(savedAspect), markupPresentation);

						Composite rightcomp = new Composite(textComp, SWT.RIGHT | SWT.TOP);
						rightcomp.setBackground(WHITE_COLOR);
						rightcomp.setLayout(new GridLayout());
						((GridLayout) rightcomp.getLayout()).numColumns = 2;
						rightcomp.setLayoutData(new GridData());
						((GridData) rightcomp.getLayoutData()).verticalAlignment = SWT.TOP;
						((GridData) rightcomp.getLayoutData()).horizontalAlignment = SWT.RIGHT;

						Label blancLabel = new Label(rightcomp, SWT.NONE);
						blancLabel.setText("");
						blancLabel.setBackground(WHITE_COLOR);
						blancLabel.setLayoutData(new GridData());
						blancLabel.pack();

						final Button editButton = new Button(rightcomp, SWT.PUSH);
						editButton.setImage(_imageReg.get(IconsInternal.EDIT));
						editButton.setToolTipText("Aspekt in Editor bearbeiten");
						editButton.setData(markupPresentation);
						editButton.setEnabled((rc.mayWrite(ca)));
						editButton.setLayoutData(new GridData());
						((GridData) editButton.getLayoutData()).verticalAlignment = SWT.TOP;
						((GridData) editButton.getLayoutData()).horizontalAlignment = SWT.RIGHT;
						editButton.addSelectionListener(new SelectionAdapter()
						{

							@Override
							public void widgetSelected(SelectionEvent se)
							{
								IMarkupPresentation current = (IMarkupPresentation) editButton.getData(); //$NON-NLS-1$
								_facade.setCurrentAspect(current.getAspect());
								markupPresentation.setSelected(true);
								IMarkupPresentation last = (IMarkupPresentation) _tabFolderRight
										.getData("lastSelected"); //$NON-NLS-1$
								IStatus sca = new Status(
										IStatus.INFO,
										Activator.PLUGIN_ID,
										"AspectsView current aspect: " + _facade.getCurrentAspect().getPdrId().toString()); //$NON-NLS-1$
								iLogger.log(sca);
								if (last != null && !last.equals(markupPresentation))
								{
									last.setSelected(false);
								}
								_tabFolderRight.setData("lastSelected", markupPresentation); //$NON-NLS-1$

								IHandlerService handlerService = (IHandlerService) PlatformUI.getWorkbench()
										.getService(IHandlerService.class);
								try
								{
									handlerService.executeCommand(
											"org.bbaw.pdr.ae.view.main.commands.CallAspectEditor", null); //$NON-NLS-1$
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
						editButton.pack();

						if (!_pdrObjectsProvider.isOrderedBySemantic() || !_pdrObjectsProvider.isShownByPerson())
						{


							if (!_pdrObjectsProvider.isOrderedBySemantic())
							{
								for (SemanticStm sStm : ca.getSemanticDim().getSemanticStms())
								{
									Label imLabel = new Label(rightcomp, SWT.NONE);
									if (sStm.getLabel().startsWith("NormName")
											|| _facade.getPersonDisplayNameTags(_provider).contains(sStm.getLabel()))
									{
										imLabel.setImage(_imageReg.get(IconsInternal.CLASSIFICATION_NAME_NORM));
									}
									else if (sStm.getLabel().equals("Name")
											|| _facade.getPersonNameTags(_provider).contains(sStm.getLabel()))
									{
										imLabel.setImage(_imageReg.get(IconsInternal.CLASSIFICATION_NAME));
									}
									else
									{
										imLabel.setImage(_imageReg.get(IconsInternal.CLASSIFICATION));
									}
									imLabel.setLayoutData(new GridData());
									imLabel.pack();
									Label semantic = new Label(rightcomp, SWT.NONE);
									semantic.setText(PDRConfigProvider.getSemanticLabel(sStm.getProvider(),
											sStm.getLabel()));
									semantic.setBackground(WHITE_COLOR);
									semantic.setLayoutData(new GridData());
									semantic.pack();
								}
							}
							if (!_pdrObjectsProvider.isShownByPerson())
							{
								for (RelationStm rStm : ca.getRelationDim().getRelationStms())
								{
									if (rStm.getSubject().equals(ca.getPdrId()) && rStm.getRelations() != null
											&& rStm.getRelations().firstElement() != null
											&& rStm.getRelations().firstElement().getObject() != null)
									{
										Label imLabel = new Label(rightcomp, SWT.NONE);
										imLabel.setToolTipText(NLMessages.getString("View_message_aspect_belongsto"));
										final String id = rStm.getRelations().firstElement().getObject().toString();
										String name = id;
										PdrObject obj = _facade.getPdrObject(new PdrId(id));
										if (obj != null)
										{
											name = obj.getDisplayName(27);
											if (id.startsWith("pdrPo")) //$NON-NLS-1$
											{
												imLabel.setImage(_imageReg.get(IconsInternal.PERSON));
											}
											if (id.startsWith("pdrAo")) //$NON-NLS-1$
											{
												imLabel.setImage(_imageReg.get(IconsInternal.ASPECT));
											}
											if (id.startsWith("pdrRo")) //$NON-NLS-1$
											{
												imLabel.setImage(_imageReg.get(IconsInternal.REFERENCE));
											}
										}
										imLabel.setLayoutData(new GridData());
										imLabel.pack();
										Link person = new Link(rightcomp, SWT.NONE);
										person.addSelectionListener(_aspectsSelectionAdapter);
										person.setText("<a href=\"native\">" + name + "</a>");
										person.setToolTipText(obj.getDisplayName());
										person.setData(id);
										person.setBackground(WHITE_COLOR);
										person.setLayoutData(new GridData());
										person.pack();

									}

								}
							}

							// rightcomp.layout();
							rightcomp.pack();
						}
						// textComp.layout();
						textComp.pack();
						i++;
					}

				}
				else
				{
					break;
				}

			}
		}

		sc.setContent(comp);
		sc.setMinSize(comp.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		comp.layout();

	}

	/**
	 * Load pdr object.
	 * @param pdrObjects the pdr objects
	 */
	private void loadPdrObject(final PdrObject[] pdrObjects)
	{
		if (pdrObjects != null)
		{
			_currentObjects = pdrObjects;
			_provider = Platform
					.getPreferencesService()
					.getString(CommonActivator.PLUGIN_ID, "PRIMARY_SEMANTIC_PROVIDER",
							AEConstants.CLASSIFICATION_AUTHORITY, null).toUpperCase(); //$NON-NLS-1$
			//			System.out.println("cat view load pdr object _provider " + _provider); //$NON-NLS-1$
			if (_tabFolderRight != null)
			{
				_tabFolderRight.dispose();

			}

			_pdrObjectsProvider.setInput(_currentObjects);
			_pdrObjectsProvider.setLazySorting(false);
			_pdrObjectsProvider.removeAllFilters();

			_onlyNewAspectsAction.setChecked(false);
			_onlyUpdatedAspectsAction.setChecked(false);
			_excludeObjectRelationsAction.setChecked(false);
			_referenceFilterAction.setChecked(false);
			_userFilterAction.setChecked(false);
			_semanticFilterAction.setChecked(false);
			_personFilterAction.setChecked(false);
			_yearFilterAction.setChecked(false);
			_onlyAspectsAboutReference.setEnabled(false);
			_onlyAspectsBasedOnReference.setEnabled(false);

			_onlyNewAspectsAction.setEnabled(true);
			_onlyUpdatedAspectsAction.setEnabled(true);
			_onlyAspectsWithDivergentMarkup.setEnabled(true);
			_excludeObjectRelationsAction.setEnabled(true);
			_referenceFilterAction.setEnabled(true);
			_userFilterAction.setEnabled(true);
			_semanticFilterAction.setEnabled(true);
			_personFilterAction.setEnabled(true);
			_yearFilterAction.setEnabled(true);

			_onlyAspectsAboutReference.setEnabled(true);
			_onlyAspectsBasedOnReference.setEnabled(true);

			createMenus();

			if (_currentObjects != null)
			{
				if (_facade.getCurrentAspect() != null
						&& _facade.getCurrentAspect().getPdrId() != null
						&& _currentObjects != null
						&& _currentObjects.length > 0
						&& _currentObjects[_currentObjects.length - 1] != null
						&& _currentObjects[_currentObjects.length - 1].getAspectIds().contains(
								_facade.getCurrentAspect().getPdrId().toString()))
				{
					buildCategories(_currentObjects, _facade.getCurrentAspect());
				}
				else
				{
					buildCategories(_currentObjects, null);
				}
			}

			_tabFolderRight.setSelection(_currentCategoryID);
		}

	}

	@Override
	public final void removeSelectionChangedListener(final ISelectionChangedListener listener)
	{
		_selectionChangedListeners.remove(listener);

	}

	/**
	 * registers changed selection.
	 * @param part workbench part
	 * @param selection new selection
	 */

	public void selectionChanged(final IWorkbenchPart part, final ISelection selection)
	{

	}

	@Override
	public final void setFocus()
	{
		if (_tabFolderRight != null && !_tabFolderRight.isDisposed())
		{
			_tabFolderRight.setFocus();
		}
	}

	@Override
	public final void setSelection(final ISelection arg0)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public final void update(final Observable o, final Object arg)
	{
		IStatus supdate = new Status(IStatus.INFO, Activator.PLUGIN_ID, "AspectsView update: " + arg); //$NON-NLS-1$
		iLogger.log(supdate);

		if (!_concurring && !_additional)
		{
			if (_facade.getCurrentTreeObjects() != null)
			{
				if (_advanced)
				{
				_orderCombo.setEnabled(true);
				_orderButton.setEnabled(true);
				_sortedCombo.setEnabled(true);
				_sortButtonAsc.setEnabled(true);
				_sortButtonDesc.setEnabled(true);
				_searchText.setEnabled(true);
				_searchButton.setEnabled(true);
				}
			}
			if (arg.equals("newTreeObjects")) //$NON-NLS-1$ //$NON-NLS-2$
			{
				_currentCategoryID = 0;
				loadPdrObject(_facade.getCurrentTreeObjects());

			}
			else if (arg.equals("newNewAspect")) //$NON-NLS-1$ //$NON-NLS-2$
			{
				loadPdrObject(_facade.getCurrentTreeObjects());
			}
			else if (arg.equals("refreshAll")) //$NON-NLS-1$
			{
				disposeTabItems();
			}
			else if (arg.equals("newAspect") && _tabFolderRight != null && !_tabFolderRight.isDisposed()) //$NON-NLS-1$
			{
				if (_tabFolderRight != null)
				{
					IMarkupPresentation last = (IMarkupPresentation) _tabFolderRight.getData("lastSelected"); //$NON-NLS-1$
					if (last != null && !_facade.getCurrentAspect().equals(last.getAspect())) //$NON-NLS-1$
					{
						last.setSelected(false);

					}
				}
			}
		}
		else if (!_additional)
		{
			if (_facade.getConcurringPerson() != null)
			{
				if (_advanced)
				{
				_orderCombo.setEnabled(true);
				_orderButton.setEnabled(true);
				_sortedCombo.setEnabled(true);
				_sortButtonAsc.setEnabled(true);
				_sortButtonDesc.setEnabled(true);
				_searchText.setEnabled(true);
				_searchButton.setEnabled(true);
				}
			}
			if (arg.equals("newConcurringPerson")) //$NON-NLS-1$ //$NON-NLS-2$
			{
				_currentCategoryID = 0;
				loadPdrObject(new Person[]
				{_facade.getConcurringPerson()});

			}
			else if (arg.equals("newNewAspect")) //$NON-NLS-1$ //$NON-NLS-2$
			{
				loadPdrObject(new Person[]
				{_facade.getConcurringPerson()});
			}
			else if (arg.equals("noSelectedConcurringPerson")) //$NON-NLS-1$
			{
				disposeTabItems();
			}
			else if (arg.equals("newAspect") && _tabFolderRight != null) //$NON-NLS-1$
			{
				if (_tabFolderRight != null && !_tabFolderRight.isDisposed())
				{
					IMarkupPresentation last = (IMarkupPresentation) _tabFolderRight.getData("lastSelected"); //$NON-NLS-1$
					if (last != null && !_facade.getCurrentAspect().equals(last.getAspect())) //$NON-NLS-1$
					{
						last.setSelected(false);

					}
				}
			}
		}
		else if (_additional)
		{
			if (arg.equals("newAspect") && _tabFolderRight != null) //$NON-NLS-1$
			{
				if (_tabFolderRight != null && !_tabFolderRight.isDisposed())
				{
					IMarkupPresentation last = (IMarkupPresentation) _tabFolderRight.getData("lastSelected"); //$NON-NLS-1$
					if (last != null && !_facade.getCurrentAspect().equals(last.getAspect())) //$NON-NLS-1$
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
		if (action == _onlyNewAspectsAction)
		{
			if (action.isChecked())
			{
				_pdrObjectsProvider.addFilter(_onlyNewAspectsFilter);
			}
			else
			{
				_pdrObjectsProvider.removeFilter(_onlyNewAspectsFilter);
			}
		}
		else if (action == _onlyUpdatedAspectsAction)
		{
			if (action.isChecked())
			{
				_pdrObjectsProvider.addFilter(_onlyUpdatedAspectsFilter);
			}
			else
			{
				_pdrObjectsProvider.removeFilter(_onlyUpdatedAspectsFilter);
			}
		}
		else if (action == _onlyAspectsWithDivergentMarkup)
		{
			if (action.isChecked())
			{
				_pdrObjectsProvider.addFilter(_onlyAspectsDivergentMarkupFilter);
			}
			else
			{
				_pdrObjectsProvider.removeFilter(_onlyAspectsDivergentMarkupFilter);
			}
		}
		else if (action == _excludeObjectRelationsAction)
		{
			if (action.isChecked())
			{
				Vector<String> ids = new Vector<String>(_facade.getCurrentTreeObjects().length);
				for (int i = 0; i < _facade.getCurrentTreeObjects().length; i++)
				{
					ids.add(_facade.getCurrentTreeObjects()[i].getPdrId().toString());
				}
				((AspectExcludeObjectRelationsFilter) _aspectExludeObjectRelationsFilter).setPersonIds(ids);
				_pdrObjectsProvider.addFilter(_aspectExludeObjectRelationsFilter);
			}
			else
			{
				_pdrObjectsProvider.removeFilter(_aspectExludeObjectRelationsFilter);
			}
		}
		else if (action == _onlyAspectsAboutReference)
		{
			if (action.isChecked())
			{
				Vector<String> ids = new Vector<String>(_facade.getCurrentTreeObjects().length);
				for (int i = 0; i < _facade.getCurrentTreeObjects().length; i++)
				{
					ids.add(_facade.getCurrentTreeObjects()[i].getPdrId().toString());
				}
				((OnlyAspectsAboutReferenceFilter) _onlyAspectsAboutReferenceFilter).setObjectIds(ids);
				_pdrObjectsProvider.addFilter(_onlyAspectsAboutReferenceFilter);
			}
			else
			{
				_pdrObjectsProvider.removeFilter(_onlyAspectsAboutReferenceFilter);
			}
		}
		else if (action == _onlyAspectsBasedOnReference)
		{
			if (action.isChecked())
			{
				Vector<String> ids = new Vector<String>(_facade.getCurrentTreeObjects().length);
				for (int i = 0; i < _facade.getCurrentTreeObjects().length; i++)
				{
					ids.add(_facade.getCurrentTreeObjects()[i].getPdrId().toString());
				}
				((OnlyAspectsBasedOnReferenceFilter) _onlyAspectsBasedOnReferenceFilter).setObjectIds(ids);
				_pdrObjectsProvider.addFilter(_onlyAspectsBasedOnReferenceFilter);
			}
			else
			{
				_pdrObjectsProvider.removeFilter(_onlyAspectsBasedOnReferenceFilter);
			}
		}

		disposeTabItems();
		buildCategories(_currentObjects, _facade.getCurrentAspect());
	}
	
	public PDRObjectsProvider getPdrObjectsProvider() {
		return _pdrObjectsProvider;
	}

}
