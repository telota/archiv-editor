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
package org.bbaw.pdr.ae.view.network.views;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import org.bbaw.pdr.ae.common.AEConstants;
import org.bbaw.pdr.ae.common.AEVIEWConstants;
import org.bbaw.pdr.ae.common.CommonActivator;
import org.bbaw.pdr.ae.common.NLMessages;
import org.bbaw.pdr.ae.common.icons.IconsInternal;
import org.bbaw.pdr.ae.common.interfaces.AEFilter;
import org.bbaw.pdr.ae.control.core.PDRConfigProvider;
import org.bbaw.pdr.ae.control.core.PDRObjectBuilder;
import org.bbaw.pdr.ae.control.facade.Facade;
import org.bbaw.pdr.ae.metamodel.PdrId;
import org.bbaw.pdr.ae.model.Aspect;
import org.bbaw.pdr.ae.model.PdrObject;
import org.bbaw.pdr.ae.model.Person;
import org.bbaw.pdr.ae.model.ReferenceMods;
import org.bbaw.pdr.ae.model.Relation;
import org.bbaw.pdr.ae.model.RelationStm;
import org.bbaw.pdr.ae.model.view.OrderingHead;
import org.bbaw.pdr.ae.view.control.PDRObjectsProvider;
import org.bbaw.pdr.ae.view.control.PDROrdererFactory;
import org.bbaw.pdr.ae.view.control.customSWTWidges.AspectPresentationTooltip;
import org.bbaw.pdr.ae.view.control.filters.AspectExcludeObjectRelationsFilter;
import org.bbaw.pdr.ae.view.control.filters.OnlyAspectDivergentMarkup;
import org.bbaw.pdr.ae.view.control.filters.OnlyAspectsAboutReferenceFilter;
import org.bbaw.pdr.ae.view.control.filters.OnlyAspectsBasedOnReferenceFilter;
import org.bbaw.pdr.ae.view.control.filters.OnlyNewPDRObjectsFilter;
import org.bbaw.pdr.ae.view.control.filters.OnlyUpdatedPDRObjectsFilter;
import org.bbaw.pdr.ae.view.main.dialogs.FilterSelectionDialog;
import org.bbaw.pdr.ae.view.network.internal.Activator;
import org.bbaw.pdr.ae.view.network.internal.PersonAspectRelationContentProvider;
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
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.OwnerDrawLabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
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
public class RelationTableView extends ViewPart implements Observer, ISelectionProvider
{

	/** The concurring. */
	private boolean _concurring = false;

	/** The additional. */
	private boolean _additional = false;



	/** The year filter action. */
	private Action _onlyAspectsWithDivergentMarkup, _openAspectsInNewTap, _openReferencesInNewTap,
			_onlyNewAspectsAction, _onlyUpdatedAspectsAction, _excludeObjectRelationsAction, _referenceFilterAction,
			_userFilterAction, _semanticFilterAction, _personFilterAction, _yearFilterAction;

	/** The aspect exlude object relations filter. */
	private AEFilter _onlyAspectsDivergentMarkupFilter, _onlyNewAspectsFilter, _onlyUpdatedAspectsFilter,
			_aspectExludeObjectRelationsFilter;

	/** The root menu manager. */
	private IMenuManager _rootMenuManager;


	/** The _main composite. */
	private Composite _mainComposite;

	/**
	 * id of category view.
	 */
	public static final String ID = "org.bbaw.pdr.ae.view.relationnetwork.views.RelationTableView"; //$NON-NLS-1$

	/** __facade singleton instance. */
	private Facade _facade = Facade.getInstanz();

	/** The _current objects. */
	private PdrObject[] _currentObjects;

	/** Logger. */
	private static ILog iLogger = AEConstants.ILOGGER;
	/** Instance of shared image registry. */
	private static ImageRegistry _imageReg = CommonActivator.getDefault().getImageRegistry();

	/** The pdr objects _provider. */
	private PDRObjectsProvider _pdrObjectsProvider = new PDRObjectsProvider();

	/** The _orderer factory. */
	private PDROrdererFactory _ordererFactory = new PDROrdererFactory();

	/** The selection changed listeners. */
	private ArrayList<ISelectionChangedListener> _selectionChangedListeners = new ArrayList<ISelectionChangedListener>();

	/** The _current aspect. */
	private Aspect _currentAspect;

	private Action _onlyAspectsBasedOnReference;

	private Action _onlyAspectsAboutReference;

	private OnlyAspectsBasedOnReferenceFilter _onlyAspectsBasedOnReferenceFilter;

	private OnlyAspectsAboutReferenceFilter _onlyAspectsAboutReferenceFilter;

	private TableViewer viewer;
	private PDRObjectBuilder _pdrObjectBuilder = new PDRObjectBuilder();

	private Menu headerMenu;
	private Composite tableComposite;


	private int _maxRows;

	private Composite _parentComposite;

	private Label _warningLabel;
	/**
	 * constructor.
	 */
	public RelationTableView()
	{

	}

	@Override
	public final void addSelectionChangedListener(final ISelectionChangedListener listener)
	{
		_selectionChangedListeners.add(listener);

	}


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

		_onlyAspectsWithDivergentMarkup = new Action("Only Aspects with divergent Markup")
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


		_onlyAspectsBasedOnReference = new Action("Only Aspects which are based on this Reference")
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

		_onlyAspectsAboutReference = new Action("Only Aspects which speak about this Reference")
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
					loadPdrObject(_facade.getCurrentTreeObjects());
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
					loadPdrObject(_facade.getCurrentTreeObjects());
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
					loadPdrObject(_facade.getCurrentTreeObjects());
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
					loadPdrObject(_facade.getCurrentTreeObjects());
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
					loadPdrObject(_facade.getCurrentTreeObjects());
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
		new SelectionAdapter()
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

		getSite().registerContextMenu(menuMgr, RelationTableView.this);
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
		_parentComposite = parent;
		_parentComposite.setLayout(new GridLayout());
		String secId = this.getViewSite().getSecondaryId();
		if (secId != null)
		{
			_concurring = (secId.equals("b")); //$NON-NLS-1$
		}
		Platform
				.getPreferencesService()
				.getString(CommonActivator.PLUGIN_ID, "PRIMARY_SEMANTIC_PROVIDER",
						AEConstants.CLASSIFICATION_AUTHORITY, null).toUpperCase();
		_mainComposite = new Composite(parent, SWT.BORDER);
		_mainComposite.setLayoutData(new GridData());
		((GridData) _mainComposite.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _mainComposite.getLayoutData()).verticalAlignment = SWT.FILL;
		((GridData) _mainComposite.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) _mainComposite.getLayoutData()).grabExcessVerticalSpace = true;
		_mainComposite.setLayout(new GridLayout());
		_pdrObjectsProvider.setOrderer(_ordererFactory.createAspectOrderer("aspect.semantic"));


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
					setPartName(p.getDisplayName());
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
					setPartName(r.getDisplayName());
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
					setPartName(a.getDisplayName());
				}
			}
			else if (secId.startsWith("a") || secId.startsWith("b")) //$NON-NLS-1$ //$NON-NLS-2$
			{

			}
		}
		else
		{

		}
		getSite().setSelectionProvider(RelationTableView.this);
		// parent.layout();
		// parent.pack();
		_facade.addObserver(this);
		update(null, "newTreeObjects");
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

	private void createViewer(Composite parent, Vector<OrderingHead> arrangedAspects, String[] columntitles)
	{

		// Define the TableViewer
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);

		// Create the columns
		createColumns(parent, columntitles);

		// Make lines and make header visible
		final Table table = viewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		// Set the ContentProvider
		viewer.setContentProvider(new PersonAspectRelationContentProvider());
		viewer.setLabelProvider(new OwnerDrawLabelProvider()
		{

			@Override
			protected void measure(Event event, Object element)
			{

				event.height = 26 + 18 * _maxRows;

			}

			@Override
			protected void paint(Event event, Object element)
			{
				Image img = null;
				if (event.index == 0)
				{
					img = _imageReg.get(IconsInternal.PERSON);
					Rectangle bounds = ((TableItem) event.item).getBounds(event.index);
					Rectangle imgBounds = img.getBounds();

					bounds.height /= 2;
					bounds.height -= imgBounds.height / 2;

					int y = bounds.height > 0 ? bounds.y + bounds.height : bounds.y;

					event.gc.drawImage(img, 0, y);
					event.gc.drawText(getFirstColumnText(element, event.index), imgBounds.width + 2, y, true);
				}
				else
				{

				}

			}

			public String getFirstColumnText(Object element, int columnIndex)
			{
				if (element instanceof String)
				{
					return ((String) element);
				}
				else if (element instanceof Object[])
				{
					if (columnIndex == 0)
					{
						return ((String) ((Object[]) element)[0]);
					}
				}
				return null;
			}
		});
				
		
		
		// Get the content for the Viewer,
		// setInput will call getElements in the ContentProvider
		if (arrangedAspects != null)
			viewer.setInput(arrangedAspects);

		TableItem[] items = table.getItems();
		TableItem item;
		SelectionListener selectionListener = new SelectionAdapter()
		{
			

			@Override
			public void widgetSelected(SelectionEvent e)
			{
				Control l = (Control) e.widget;
				PdrId id = (PdrId) l.getData("id");
				PdrId subj;
				PdrId obj;
				Aspect a;
				if (id != null)
				{
					a = _facade.getAspect(id);
				}
				else
				{
					subj = (PdrId) l.getData("sub");
					obj = (PdrId) l.getData("obj");
					if (_facade.getLastAspects() != null && !_facade.getLastAspects().isEmpty())
					{
						a = _pdrObjectBuilder.buildSimilarAspect(subj, obj, _facade.getLastAspects().lastElement());
					}
					else
					{
						a = _pdrObjectBuilder.buildSimilarAspect(subj, obj, null);
					}
				}
				
				_facade.setCurrentAspect(a);
				 IHandlerService handlerService = (IHandlerService)
				 PlatformUI.getWorkbench().getService(
				 IHandlerService.class);
				 try
				 {
					handlerService.executeCommand("org.bbaw.pdr.ae.view.main.commands.CallAspectEditor", new Event()); //$NON-NLS-1$
				 }
				catch (ExecutionException ex)
				 {
					ex.printStackTrace();
				 }
				catch (NotDefinedException ex)
				 {
					ex.printStackTrace();
				 }
				catch (NotEnabledException ex)
				 {
					ex.printStackTrace();
				 }
				catch (NotHandledException ex)
				 {
					ex.printStackTrace();
				 }
				 

			}
			
			
		};
		Relation relation;
		for (int i = 0; i < items.length; i++)
		{
			TableEditor editor = new TableEditor(table);
			GridLayout gl = new GridLayout(2, false);
			gl.marginHeight = 0;
			gl.marginWidth = 0;
			PdrId subject = new PdrId(arrangedAspects.get(i).getValue());
			item = items[i];
			int rows = 0;
			if (item.getData() != null)
			{
				Object[] objs = (Object[]) item.getData();
				if (objs.length > 1)
				{
					for (int j = 1; j < objs.length; j++)
					{
						Object o = objs[j];
						PdrId objectId = new PdrId(arrangedAspects.get(j - 1).getValue());
						boolean empty = true;
						editor = new TableEditor(table);
						Composite composite = new Composite(table, SWT.NONE);
						// composite.setText("Text");
						composite.setLayout(gl);
						composite.setBackground(AEVIEWConstants.VIEW_BACKGROUND_DESELECTED_COLOR);
						editor.grabHorizontal = true;
						if (o != null && o instanceof Vector<?>)
						{
							Vector<Aspect> aspects = (Vector<Aspect>) o;
							if (!aspects.isEmpty())
							{


								RelationStm relationSm;
								String relationString = null;
								for (Aspect a : aspects)
								{
									rows = 0;
									if (a.getRelationDim() != null && !a.getRelationDim().getRelationStms().isEmpty())
									{
										for (int k = 0; k < a.getRelationDim().getRelationStms().size(); k++)
										{
											relationSm = a.getRelationDim().getRelationStms().get(k);
											if (relationSm.getSubject() != null
													&& relationSm.getSubject().equals(subject)
													&& relationSm.getRelations() != null
													&& !relationSm.getRelations().isEmpty())
											{
												for (int l = 0; l < relationSm.getRelations().size(); l++)
												{
													relation = relationSm.getRelations().get(l);
													if (relation.getObject().equals(objectId)
															&& relation.getContext() != null)
													{
														empty = false;
														rows++;
														relationString = PDRConfigProvider.getLabelOfRelation(
																relation.getProvider(), relation.getContext(),
																relation.getRClass(), relation.getRelation());
														Label label = new Label(composite, SWT.NONE);
														label.setImage(_imageReg.get(IconsInternal.RELATION));
														label.setBackground(AEVIEWConstants.VIEW_BACKGROUND_DESELECTED_COLOR);

														Link link = new Link(composite, SWT.None);
														link.setText("<a>" + relationString + ": "
																+ a.getDisplayNameWithID() + "</a>");
														link.setBackground(AEVIEWConstants.VIEW_BACKGROUND_DESELECTED_COLOR);

														link.setData("id", a.getPdrId());


														final AspectPresentationTooltip aspectTooltip = new AspectPresentationTooltip(
																link, a);
														aspectTooltip.setPopupDelay(1);
														aspectTooltip.setHideDelay(0);
														aspectTooltip.setHideOnMouseDown(false);
														aspectTooltip.activate();
														link.addSelectionListener(selectionListener);
														link.pack();
													}
												}

											}
										}
									}
									if (rows > _maxRows)
									{
										_maxRows = rows;
									}
								}

							}
						}
						if (arrangedAspects.size() < 12 && empty && i != j - 1)
						{
							Button newAspect = new Button(composite, SWT.PUSH);
							newAspect.setImage(_imageReg.get(IconsInternal.ASPECT_ADD_SAME_PERSON));
							newAspect.setBackground(AEVIEWConstants.VIEW_BACKGROUND_DESELECTED_COLOR);

							newAspect.setData("sub", subject.clone());
							newAspect.setData("obj", objectId.clone());
							newAspect.addSelectionListener(selectionListener);
							newAspect.pack();
						}
						composite.pack();
						composite.layout();
						editor.setEditor(composite, items[i], j);
						editor.minimumHeight = 20;
					}
				}
			}


		}
		
		// // Make the selection available to other Views
		getSite().setSelectionProvider(viewer);

		// Layout the viewer
		GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.horizontalSpan = 2;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalAlignment = GridData.FILL;
		viewer.getControl().setLayoutData(gridData);


		// Set the sorter for the table
		// viewer.setComparator(comparator);
	}

	// Used to update the viewer from outsite
	public void refresh()
	{
		viewer.refresh();
	}

	// This will create the columns for the table
	private void createColumns(final Composite parent, String[] columntitles)
	{
		headerMenu = new Menu(parent.getShell(), SWT.POP_UP);


		// First column is for the first name
		for (int i = 0; i < columntitles.length; i++)
		{
			TableViewerColumn col = createTableViewerColumn(columntitles[i], 130, i);

		}

	}

	private void createMenuItem(Menu parent, final TableColumn column)
	{
		final MenuItem itemName = new MenuItem(parent, SWT.CHECK);
		itemName.setText(column.getText());
		itemName.setSelection(column.getResizable());
		itemName.addListener(SWT.Selection, new Listener()
		{
			public void handleEvent(Event event)
			{
				if (itemName.getSelection())
				{
					column.setWidth(150);
					column.setResizable(true);
				}
				else
				{
					column.setWidth(0);
					column.setResizable(false);
				}
			}
		});

	}

	private TableViewerColumn createTableViewerColumn(String title, int bound, final int colNumber)
	{
		final TableViewerColumn viewerColumn = new TableViewerColumn(viewer, SWT.NONE);
		final TableColumn column = viewerColumn.getColumn();
		column.setText(title);
		column.setWidth(bound);
		column.setResizable(true);
		column.setMoveable(false);
		if (colNumber > 0)
		{
			column.setImage(_imageReg.get(IconsInternal.PERSON));
		}
		// column.addSelectionListener(getSelectionAdapter(column, colNumber));
		// Create the menu item for this column
		createMenuItem(headerMenu, column);
		return viewerColumn;
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus()
	{
		if (viewer != null)
			viewer.getControl().setFocus();
	}



	private void loadPdrObject(PdrObject[] selectedTreeObjects)
	{
		// _pdrObjectsProvider.removeAllFilters();
		PdrObject[] currentTreeObjects = null;
		if (selectedTreeObjects.length > 40)
		{
			currentTreeObjects = new PdrObject[40];
			for (int i = 0; i < 40; i++)
			{
				currentTreeObjects[i] = selectedTreeObjects[i];
			}
		}
		else
		{
			currentTreeObjects = selectedTreeObjects;
		}
		if (currentTreeObjects != null)
		{
			_maxRows = 0;
			_onlyNewAspectsAction.setChecked(false);
			_onlyUpdatedAspectsAction.setChecked(false);
			_excludeObjectRelationsAction.setChecked(false);
			_referenceFilterAction.setChecked(false);
			_userFilterAction.setChecked(false);
			_semanticFilterAction.setChecked(false);
			_personFilterAction.setChecked(false);
			_yearFilterAction.setChecked(false);

			_onlyNewAspectsAction.setEnabled(true);
			_onlyUpdatedAspectsAction.setEnabled(true);
			_onlyAspectsWithDivergentMarkup.setEnabled(true);
			_excludeObjectRelationsAction.setEnabled(true);
			_referenceFilterAction.setEnabled(true);
			_userFilterAction.setEnabled(true);
			_semanticFilterAction.setEnabled(true);
			_personFilterAction.setEnabled(true);
			_yearFilterAction.setEnabled(true);
			_pdrObjectsProvider.setInput(currentTreeObjects);
			_pdrObjectsProvider.setOrderer(_ordererFactory.createAspectOrderer("aspect.person"));
			Vector<OrderingHead> arrangedAspects = _pdrObjectsProvider.getArrangedAspects();
			String[] columntitles = new String[arrangedAspects.size() + 1];
			for (int i = 0; i <= arrangedAspects.size(); i++)
			{
				if (i == 0)
				{
					columntitles[0] = NLMessages.getString("Editor_subject") + " \\ "
							+ NLMessages.getString("Editor_object");
				}
				else
				{
					OrderingHead oh = arrangedAspects.get(i - 1);
					columntitles[i] = oh.getLabel();
				}

			}
			if (tableComposite != null)
			{
				tableComposite.dispose();
				tableComposite = null;

			}
			if (selectedTreeObjects.length > 40)
			{
				if (_warningLabel == null)
				{
					_warningLabel = new Label(_mainComposite, SWT.NONE);
					_warningLabel
							.setText("More then 40 Persons selected - but this TableView can present only 40. Some Persons are not presented.");
					_warningLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
					_warningLabel.pack();
				}

			}
			else if (_warningLabel != null)
			{
				_warningLabel.dispose();
				_warningLabel = null;
			}
			tableComposite = new Composite(_mainComposite, SWT.NONE);
			tableComposite.setLayout(new GridLayout(2, false));
			tableComposite.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL, GridData.HORIZONTAL_ALIGN_FILL, true,
					true, 2, 0));
			((GridData) tableComposite.getLayoutData()).grabExcessHorizontalSpace = true;
			((GridData) tableComposite.getLayoutData()).grabExcessVerticalSpace = true;
			((GridData) tableComposite.getLayoutData()).horizontalAlignment = SWT.FILL;
			((GridData) tableComposite.getLayoutData()).verticalAlignment = SWT.FILL;

			createViewer(tableComposite, arrangedAspects, columntitles);
			viewer.getTable().setLayoutData(
					new GridData(GridData.GRAB_HORIZONTAL, GridData.HORIZONTAL_ALIGN_FILL, true, true, 2, 0));
			((GridData) viewer.getTable().getLayoutData()).grabExcessHorizontalSpace = true;
			((GridData) viewer.getTable().getLayoutData()).grabExcessVerticalSpace = true;
			((GridData) viewer.getTable().getLayoutData()).horizontalAlignment = SWT.FILL;
			((GridData) viewer.getTable().getLayoutData()).verticalAlignment = SWT.FILL;
			_mainComposite.pack();
			_parentComposite.redraw();
			_parentComposite.update();
			_parentComposite.layout();

		}

	}

	@Override
	public void dispose()
	{
		_facade.deleteObserver(RelationTableView.this);

		super.dispose();
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
	public final void setSelection(final ISelection arg0)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public final void update(final Observable o, final Object arg)
	{
		IStatus supdate = new Status(IStatus.INFO, Activator.PLUGIN_ID, "RelationTableView update: " + arg); //$NON-NLS-1$
		iLogger.log(supdate);
		_pdrObjectsProvider.removeAllFilters();
		if (!_concurring && !_additional)
		{
			if (_facade.getCurrentTreeObjects() != null)
			{

			}
			if (arg.equals("newTreeObjects")) //$NON-NLS-1$ //$NON-NLS-2$
			{
				loadPdrObject(_facade.getCurrentTreeObjects());

			}
			else if (arg.equals("newNewAspect")) //$NON-NLS-1$ //$NON-NLS-2$
			{
				loadPdrObject(_facade.getCurrentTreeObjects());
			}
			else if (arg.equals("refreshAll")) //$NON-NLS-1$
			{
			}

		}
		else if (!_additional)
		{
			if (_facade.getConcurringPerson() != null)
			{

			}
			if (arg.equals("newConcurringPerson")) //$NON-NLS-1$ //$NON-NLS-2$
			{
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

		loadPdrObject(_facade.getCurrentTreeObjects());
	}

}
