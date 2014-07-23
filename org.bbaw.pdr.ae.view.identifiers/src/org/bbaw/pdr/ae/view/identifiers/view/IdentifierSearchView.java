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
package org.bbaw.pdr.ae.view.identifiers.view;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import org.bbaw.pdr.ae.common.AEPluginIDs;
import org.bbaw.pdr.ae.common.AEVIEWConstants;
import org.bbaw.pdr.ae.common.CommonActivator;
import org.bbaw.pdr.ae.common.icons.IconsInternal;
import org.bbaw.pdr.ae.control.facade.Facade;
import org.bbaw.pdr.ae.metamodel.PdrId;
import org.bbaw.pdr.ae.model.Identifier;
import org.bbaw.pdr.ae.model.Identifiers;
import org.bbaw.pdr.ae.model.PdrObject;
import org.bbaw.pdr.ae.model.Person;
import org.bbaw.pdr.ae.view.control.ViewHelper;
import org.bbaw.pdr.ae.view.control.provider.AEConfigPresentableContentProvider;
import org.bbaw.pdr.ae.view.control.provider.AEConfigPresentableLabelProvider;
import org.bbaw.pdr.ae.view.identifiers.interfaces.IConcurrenceSearchService;
import org.bbaw.pdr.ae.view.identifiers.internal.ConcurrenceSearchController;
import org.bbaw.pdr.ae.view.identifiers.internal.IdentifiersExtension;
import org.bbaw.pdr.ae.view.identifiers.model.ConcurrenceData;
import org.bbaw.pdr.ae.view.identifiers.model.ConcurrenceDataHead;
import org.bbaw.pdr.ae.view.identifiers.model.ConcurrenceIdentifier;
import org.bbaw.pdr.ae.view.main.views.Treeview;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IParameter;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.Parameterization;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.ISizeProvider;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.services.ISourceProviderService;

public class IdentifierSearchView extends ViewPart implements Observer, ISizeProvider
{

	private int _searchTarget = 2;
	private Person _selectedPerson;

	private IConcurrenceSearchService _concurrenceSearchService;
	private HashMap<PdrId, ConcurrenceDataHead> _resultMap;
	private Facade _facade = Facade.getInstanz();
	private Object _personsContainer;
	private ScrolledComposite _scrolledComposite;
	private Composite _contentComposite;
	private boolean _chain2Selection;

	/** Instance of shared image registry. */
	private ImageRegistry _imageReg = CommonActivator.getDefault().getImageRegistry();
	private ISelectionListener _mainSelectionListener;
	public static final String ID = "org.bbaw.pdr.ae.view.identifiers.view.IdentifierSearchView"; //$NON-NLS-1$

	public IdentifierSearchView()
	{
	}

	/**
	 * Create contents of the view part.
	 */
	@Override
	public void createPartControl(Composite parent)
	{
		createSelectionListener();
		IWorkbenchWindow ww = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		ISelectionService selService = ww.getSelectionService();
		selService.addSelectionListener(AEPluginIDs.VIEW_TREEVIEW, _mainSelectionListener);
		_facade.addObserver(this);
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(1, false));
		_resultMap = new HashMap<PdrId, ConcurrenceDataHead>();
		Composite compositeHead = new Composite(container, SWT.NONE);
		compositeHead.setLayout(new GridLayout(5, false));
		compositeHead.setLayoutData(new GridData());
		((GridData) compositeHead.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) compositeHead.getLayoutData()).grabExcessHorizontalSpace = true;
		SelectionAdapter radioSelAdapter = new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				_searchTarget = (Integer) ((Button) e.getSource()).getData();
			}
		};
		Label lblPersonsToProcess = new Label(compositeHead, SWT.NONE);
		lblPersonsToProcess.setText("Persons:");

		Button btnRadioButton = new Button(compositeHead, SWT.RADIO);
		btnRadioButton.setData(0);

		btnRadioButton.addSelectionListener(radioSelAdapter);
		btnRadioButton.setText("All Persons");

		Button btnRadioButton_1 = new Button(compositeHead, SWT.RADIO);
		btnRadioButton_1.setText("Selected Tree");
		btnRadioButton_1.setData(1);
		btnRadioButton_1.addSelectionListener(radioSelAdapter);

		Button btnRadioButton_2 = new Button(compositeHead, SWT.RADIO);
		btnRadioButton_2.setText("Selected Persons");
		btnRadioButton_2.setData(2);
		btnRadioButton_2.addSelectionListener(radioSelAdapter);
		btnRadioButton_2.setSelection(_searchTarget == 2);

		Button chaine2Selection = new Button(compositeHead, SWT.CHECK);
		chaine2Selection.setText("Chain2Selection");
		chaine2Selection.addSelectionListener(new SelectionAdapter()
		{

			@Override
			public void widgetSelected(SelectionEvent e)
			{
				_chain2Selection = !_chain2Selection;
			}

		});
		chaine2Selection.setSelection(_chain2Selection);

		Composite composite_2 = new Composite(compositeHead, SWT.NONE);
		composite_2.setLayout(new GridLayout(4, false));
		composite_2.setLayoutData(new GridData());
		((GridData) composite_2.getLayoutData()).horizontalSpan = 5;
		((GridData) composite_2.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) composite_2.getLayoutData()).grabExcessHorizontalSpace = true;

		Label lblWebservice = new Label(composite_2, SWT.NONE);
		lblWebservice.setText("Webservice");

		ComboViewer comboViewer = new ComboViewer(composite_2, SWT.READ_ONLY);
		Combo combo_1 = comboViewer.getCombo();
		combo_1.setLayoutData(new GridData());
		((GridData) combo_1.getLayoutData()).horizontalSpan = 2;
		((GridData) combo_1.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) combo_1.getLayoutData()).grabExcessHorizontalSpace = true;
		comboViewer.setContentProvider(new AEConfigPresentableContentProvider());
		comboViewer.setLabelProvider(new AEConfigPresentableLabelProvider());
		if (IdentifiersExtension.getConcurrenceSearchServices() != null)
		{
			comboViewer.setInput(IdentifiersExtension.getConcurrenceSearchServices());
		}
		comboViewer.addSelectionChangedListener(new ISelectionChangedListener()
		{

			@Override
			public void selectionChanged(final SelectionChangedEvent event)
			{
				ISelection iSelection = event.getSelection();
				Object obj = ((IStructuredSelection) iSelection).getFirstElement();
				_concurrenceSearchService = (IConcurrenceSearchService) obj;

			}
		});
		
		Object in = comboViewer.getInput();
		if (in instanceof Map<?, ?>)
		{
			Map<String, IConcurrenceSearchService> map = (Map<String, IConcurrenceSearchService>) in;
			String pdrKey = null;
			for (String s : map.keySet())
			{
				if (s.toLowerCase().contains("pdr"))
				{
					pdrKey = s;
					break;
				}
			}
			if (pdrKey != null)
			{
				IConcurrenceSearchService service = map.get(pdrKey);
				comboViewer.setSelection(new StructuredSelection(service));
			}
			else if (comboViewer.getElementAt(0) != null)
			{
				comboViewer.setSelection(new StructuredSelection(comboViewer.getElementAt(0)));
			}
		}

		Button btnGo = new Button(composite_2, SWT.NONE);
		btnGo.setText("GO");
		btnGo.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				if (_selectedPerson != null)
				{
					if (_resultMap == null)
					{
						_resultMap = new HashMap<PdrId, ConcurrenceDataHead>();
					}
					if (!_resultMap.containsKey(_selectedPerson.getPdrId()))
					{
						searchConcurringPersons(_searchTarget, _concurrenceSearchService);
					}
					else if (_resultMap.containsKey(_selectedPerson.getPdrId())
							&& !_resultMap.get(_selectedPerson.getPdrId()).getServices()
									.contains(_concurrenceSearchService.getLabel()))
					{
						searchConcurringPersons(_searchTarget, _concurrenceSearchService);
					}
					else
					{
						loadConcurrenceData(_selectedPerson, _resultMap.get(_selectedPerson.getPdrId()));
					}
				}
				else if (_searchTarget < 2)
				{
					searchConcurringPersons(_searchTarget, _concurrenceSearchService);
				}
			}

		});

		_scrolledComposite = new ScrolledComposite(container, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		_scrolledComposite.setLayoutData(new GridData());
		((GridData) _scrolledComposite.getLayoutData()).horizontalSpan = 1;
		((GridData) _scrolledComposite.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _scrolledComposite.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) _scrolledComposite.getLayoutData()).verticalAlignment = SWT.FILL;
		((GridData) _scrolledComposite.getLayoutData()).grabExcessVerticalSpace = true;
		_scrolledComposite.setExpandHorizontal(true);
		_scrolledComposite.setExpandVertical(true);
		_scrolledComposite.setBackground(AEVIEWConstants.VIEW_BACKGROUND_SELECTED_COLOR);

		_contentComposite = new Composite(_scrolledComposite, SWT.NONE);
		_contentComposite.setLayout(new GridLayout(1, false));
		_scrolledComposite.setContent(_contentComposite);
		_contentComposite.setLayoutData(new GridData());
		((GridData) _contentComposite.getLayoutData()).horizontalSpan = 1;
		((GridData) _contentComposite.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _contentComposite.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) _contentComposite.getLayoutData()).verticalAlignment = SWT.FILL;
		((GridData) _contentComposite.getLayoutData()).grabExcessVerticalSpace = true;
		_contentComposite.setBackground(AEVIEWConstants.VIEW_BACKGROUND_DESELECTED_COLOR);

	}

	private void createSelectionListener()
	{
		_mainSelectionListener = new ISelectionListener()
		{

			@Override
			public void selectionChanged(IWorkbenchPart part, ISelection selection)
			{
				// System.out.println("selection event");
				IStructuredSelection sel;
				IWorkbenchWindow ww = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
				ISelectionService selService = ww.getSelectionService();
				sel = (IStructuredSelection) selService.getSelection(AEPluginIDs.VIEW_TREEVIEW);
				Object obj = null;
				if (sel != null)
				{
					obj = ((IStructuredSelection) sel).getFirstElement();
				}
				if (obj != null && obj instanceof Person)
				{
					_selectedPerson = (Person) obj;
					if (_resultMap != null && _resultMap.containsKey(_selectedPerson.getPdrId()))
					{
						loadConcurrenceData(_selectedPerson, _resultMap.get(_selectedPerson.getPdrId()));
					}
				}

			}

		};

	}

	private void loadConcurrenceData(Person _selectedPerson, ConcurrenceDataHead conHead)
	{
		if (_contentComposite != null)
		{
			_contentComposite = null;
		}
		_contentComposite = new Composite(_scrolledComposite, SWT.NONE);
		_contentComposite.setLayout(new GridLayout(1, false));
		_scrolledComposite.setContent(_contentComposite);
		_scrolledComposite.setMinSize(_contentComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		_contentComposite.setLayoutData(new GridData());
		((GridData) _contentComposite.getLayoutData()).horizontalSpan = 1;
		((GridData) _contentComposite.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _contentComposite.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) _contentComposite.getLayoutData()).verticalAlignment = SWT.FILL;
		((GridData) _contentComposite.getLayoutData()).grabExcessVerticalSpace = true;
		_contentComposite.setBackground(AEVIEWConstants.VIEW_BACKGROUND_DESELECTED_COLOR);

		if (conHead != null)
		{
		SelectionAdapter setSelectionAdapter = new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				
				Button b = (Button) e.getSource();
				boolean set = (Boolean) b.getData("set");
				if (set)
				{
					ConcurrenceData cd = (ConcurrenceData) b.getData("cd");
						removePersonIdentifiers(cd);
					b.setImage(_imageReg.get(IconsInternal.OK));
						b.setData("set", false);
						b.setToolTipText("Add PIDs to Person");

				}
				else
				{
					ConcurrenceData cd = (ConcurrenceData) b.getData("cd");
					setPersonIdentifiers(cd);
					b.setImage(_imageReg.get(IconsInternal.DELETE));
					b.setToolTipText("Remove PIDs");
						b.setData("set", true);

				}
			}


		};
		SelectionAdapter linkSelectionAdapter = new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent ev)
			{
					// System.out.println("link pressed");
				Link button = (Link) ev.getSource();
				String urlString = (String) button.getData();
				ArrayList<Parameterization> parameters = new ArrayList<Parameterization>();
				IParameter iparam = null;

				// get the command from plugin.xml
				IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
				ICommandService cmdService = (ICommandService) window.getService(ICommandService.class);
				Command cmd = cmdService.getCommand("org.bbaw.pdr.ae.view.identifiers.commands.OpenBrowserDialog");

				// get the parameter
				try
				{
					iparam = cmd.getParameter("org.bbaw.pdr.ae.view.identifiers.url");
				}
				catch (NotDefinedException e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Parameterization params = new Parameterization(iparam, urlString);
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
		GridData gd = new GridData();
		gd.horizontalSpan = 3;
		gd.horizontalAlignment = SWT.FILL;
		gd.grabExcessHorizontalSpace = true;


		GridData gdLb = new GridData();
		gdLb.horizontalSpan = 1;
		gdLb.horizontalAlignment = SWT.FILL;
		gdLb.grabExcessHorizontalSpace = true;

			for (int i = 0; (i < conHead.getConcurrenceDatas().size() && i < 35); i++)
		{
				ConcurrenceData cd = conHead.getConcurrenceDatas().get(i);
			Composite concurrenceComp = new Composite(_contentComposite, SWT.NONE);
			concurrenceComp.setLayout(new GridLayout(2, false));
			((GridLayout) concurrenceComp.getLayout()).marginHeight = 0;
			((GridLayout) concurrenceComp.getLayout()).marginWidth = 0;

			concurrenceComp.setLayoutData(new GridData());
			((GridData) concurrenceComp.getLayoutData()).horizontalSpan = 1;
			((GridData) concurrenceComp.getLayoutData()).horizontalAlignment = SWT.FILL;
			((GridData) concurrenceComp.getLayoutData()).grabExcessHorizontalSpace = true;

			Group concurrenceGroup = new Group(_contentComposite, SWT.NONE);
			concurrenceGroup.setLayout(new GridLayout(2, false));
			concurrenceGroup.setLayoutData(new GridData());
			((GridData) concurrenceGroup.getLayoutData()).horizontalSpan = 1;
			((GridData) concurrenceGroup.getLayoutData()).horizontalAlignment = SWT.FILL;
			((GridData) concurrenceGroup.getLayoutData()).grabExcessHorizontalSpace = true;
			concurrenceGroup.setBackground(AEVIEWConstants.VIEW_BACKGROUND_SELECTED_COLOR);

			Composite infoComp = new Composite(concurrenceGroup, SWT.NONE);
				infoComp.setLayout(new GridLayout(4, true));
			infoComp.setLayoutData(new GridData());
			((GridData) infoComp.getLayoutData()).horizontalSpan = 1;
			((GridData) infoComp.getLayoutData()).horizontalAlignment = SWT.FILL;
			((GridData) infoComp.getLayoutData()).grabExcessHorizontalSpace = true;
			infoComp.setBackground(AEVIEWConstants.VIEW_BACKGROUND_SELECTED_COLOR);


			if (cd.getNormName() != null && cd.getNormName().trim().length() > 0)
			{
				Label lbName = new Label(infoComp, SWT.NONE);
				lbName.setText("Name");
				lbName.setLayoutData(gdLb);
				lbName.setBackground(AEVIEWConstants.VIEW_BACKGROUND_SELECTED_COLOR);
				Label valName = new Label(infoComp, SWT.NONE);
				valName.setText(cd.getNormName());
				valName.setLayoutData(gd);
				valName.setBackground(AEVIEWConstants.VIEW_BACKGROUND_SELECTED_COLOR);
			}
			if (cd.getOtherNames() != null && cd.getOtherNames().size() > 0)
			{
				for (String n : cd.getOtherNames())
				{
					Label lbON = new Label(infoComp, SWT.NONE);
					lbON.setText("OtherName");
					lbON.setLayoutData(gdLb);
					lbON.setBackground(AEVIEWConstants.VIEW_BACKGROUND_SELECTED_COLOR);
					Label valON = new Label(infoComp, SWT.NONE);
					valON.setText(n);
					valON.setLayoutData(gd);
					valON.setBackground(AEVIEWConstants.VIEW_BACKGROUND_SELECTED_COLOR);
				}
			}
			if (cd.getDateOfBirth() != null)
			{
				Label lbDB = new Label(infoComp, SWT.NONE);
				lbDB.setText("Birth");
				lbDB.setLayoutData(gdLb);
				lbDB.setBackground(AEVIEWConstants.VIEW_BACKGROUND_SELECTED_COLOR);
				Label valDB = new Label(infoComp, SWT.NONE);
				valDB.setText(cd.getDateOfBirth().toString());
				valDB.setLayoutData(gd);
				valDB.setBackground(AEVIEWConstants.VIEW_BACKGROUND_SELECTED_COLOR);
			}
			if (cd.getPlaceOfBirth() != null)
			{
				Label lbPB = new Label(infoComp, SWT.NONE);
					lbPB.setText("Place/Birth");
				lbPB.setLayoutData(gdLb);
				lbPB.setBackground(AEVIEWConstants.VIEW_BACKGROUND_SELECTED_COLOR);
				Label valPB = new Label(infoComp, SWT.NONE);
				valPB.setText(cd.getPlaceOfBirth());
				valPB.setLayoutData(gd);
				valPB.setBackground(AEVIEWConstants.VIEW_BACKGROUND_SELECTED_COLOR);
			}

			if (cd.getDateOfDeath() != null)
			{
				Label lbDD = new Label(infoComp, SWT.NONE);
				lbDD.setText("Death");
				lbDD.setLayoutData(gdLb);
				lbDD.setBackground(AEVIEWConstants.VIEW_BACKGROUND_SELECTED_COLOR);
				Label valDD = new Label(infoComp, SWT.NONE);
				valDD.setText(cd.getDateOfDeath().toString());
				valDD.setLayoutData(gd);
				valDD.setBackground(AEVIEWConstants.VIEW_BACKGROUND_SELECTED_COLOR);
			}
			if (cd.getPlaceOfDeath() != null)
			{
				Label lbPD = new Label(infoComp, SWT.NONE);
				lbPD.setLayoutData(gdLb);
					lbPD.setText("Place/Death");
				lbPD.setBackground(AEVIEWConstants.VIEW_BACKGROUND_SELECTED_COLOR);
				Label valPD = new Label(infoComp, SWT.NONE);
				valPD.setText(cd.getPlaceOfDeath());
				valPD.setLayoutData(gd);
				valPD.setBackground(AEVIEWConstants.VIEW_BACKGROUND_SELECTED_COLOR);
			}
			if (cd.getDescription() != null)
			{
				Label lbd = new Label(infoComp, SWT.NONE);
				lbd.setText("Description");
				lbd.setLayoutData(gdLb);
				lbd.setBackground(AEVIEWConstants.VIEW_BACKGROUND_SELECTED_COLOR);

					Label valD = new Label(infoComp, SWT.WRAP);
				valD.setText(cd.getDescription());
				valD.setLayoutData(gd);
				valD.setBackground(AEVIEWConstants.VIEW_BACKGROUND_SELECTED_COLOR);
					valD.setToolTipText(cd.getDescription());
			}
			if (cd.getIdentifiers() != null)
			{
				Label lbId = new Label(infoComp, SWT.NONE);
					lbId.setText("ID Provider:");
				lbId.setLayoutData(gdLb);
				lbId.setBackground(AEVIEWConstants.VIEW_BACKGROUND_SELECTED_COLOR);
				Label valD = new Label(infoComp, SWT.NONE);
					valD.setText("Identifier:");
				valD.setLayoutData(gd);
				valD.setBackground(AEVIEWConstants.VIEW_BACKGROUND_SELECTED_COLOR);
				for (ConcurrenceIdentifier ci : cd.getIdentifiers())
				{
					if (ci != null && ci.getId() != null)
					{
						Label lbprov = new Label(infoComp, SWT.NONE);
						lbprov.setText(ci.getProvider());
						lbprov.setLayoutData(gdLb);
						lbprov.setBackground(AEVIEWConstants.VIEW_BACKGROUND_SELECTED_COLOR);

						Link link = new Link(infoComp, SWT.PUSH);
						link.setLayoutData(gd);
						link.setBackground(AEVIEWConstants.VIEW_BACKGROUND_SELECTED_COLOR);
						link.addSelectionListener(linkSelectionAdapter);
						link.setData(ci.getUrl());
						link.setText("<a href=\"native\">" + ci.getId() + "</a>"); //$NON-NLS-1$ //$NON-NLS-2$
					}
				}
				lbId.pack();

			}
			infoComp.layout();

			Composite buttonComp = new Composite(concurrenceGroup, SWT.NONE);
			buttonComp.setLayout(new GridLayout(2, false));
			buttonComp.setBackground(AEVIEWConstants.VIEW_BACKGROUND_SELECTED_COLOR);
			buttonComp.setLayoutData(new GridData());
			((GridData) buttonComp.getLayoutData()).verticalAlignment = SWT.TOP;

			Label lbS = new Label(buttonComp, SWT.NONE);
				lbS.setText("Accuracy");
			lbS.setLayoutData(gdLb);
			lbS.setBackground(AEVIEWConstants.VIEW_BACKGROUND_SELECTED_COLOR);
			Label valS = new Label(buttonComp, SWT.NONE);
				valS.setText(new Integer(cd.getScore()).toString() + "%");
			valS.setLayoutData(gdLb);
			valS.setBackground(AEVIEWConstants.VIEW_BACKGROUND_SELECTED_COLOR);

			Label lbQS = new Label(buttonComp, SWT.NONE);
				lbQS.setText("Query-Accuracy");
			lbQS.setBackground(AEVIEWConstants.VIEW_BACKGROUND_SELECTED_COLOR);
			Label valQS = new Label(buttonComp, SWT.NONE);
				valQS.setText(new Integer(cd.getMaxQueryScore()).toString() + "%");
			valQS.setBackground(AEVIEWConstants.VIEW_BACKGROUND_SELECTED_COLOR);
			
				Label lbSS = new Label(buttonComp, SWT.NONE);
				lbSS.setText("Service:");
				lbSS.setBackground(AEVIEWConstants.VIEW_BACKGROUND_SELECTED_COLOR);
				lbSS.setLayoutData(new GridData());
				((GridData) lbSS.getLayoutData()).horizontalSpan = 2;
				Label valSS = new Label(buttonComp, SWT.NONE);
				valSS.setText(cd.getService());
				valSS.setBackground(AEVIEWConstants.VIEW_BACKGROUND_SELECTED_COLOR);
				valSS.setLayoutData(new GridData());
				((GridData) valSS.getLayoutData()).horizontalSpan = 2;

			Button btnSet = new Button(buttonComp, SWT.NONE);
			btnSet.setLayoutData(new GridData());
			((GridData) btnSet.getLayoutData()).horizontalSpan = 2;
			btnSet.setImage(_imageReg.get(IconsInternal.OK));
			btnSet.addSelectionListener(setSelectionAdapter);
			btnSet.setData("cd", cd);
			btnSet.setData("set", false);
			btnSet.setToolTipText("Add PIDs to Person");

			buttonComp.layout();
			concurrenceComp.layout();
		}
		}
		_contentComposite.layout();
		_scrolledComposite.setContent(_contentComposite);
		_scrolledComposite.layout();
		_scrolledComposite.setMinSize(_contentComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		ViewHelper.accelerateScrollbar(_scrolledComposite, 5);

		_scrolledComposite.setFocus();
	}

	private void setPersonIdentifiers(ConcurrenceData cd)
	{
		if (cd != null && cd.getIdentifiers() != null && !cd.getIdentifiers().isEmpty() && _selectedPerson != null)
		{
			boolean found = false;
			for (ConcurrenceIdentifier ci : cd.getIdentifiers())
			{
				found = false;
				if (_selectedPerson.getIdentifiers() == null)
				{
					_selectedPerson.setIdentifiers(new Identifiers());
				}
				for (Identifier id : _selectedPerson.getIdentifiers().getIdentifiers())
				{
					if (ci.getProvider() != null && id.getProvider() != null
							&& ci.getProvider().equalsIgnoreCase(id.getProvider()))
					{
						found = true;
						break;
					}

				}
				if (!found)
				{
					Identifier id = new Identifier();
					id.setProvider(ci.getProvider());
					id.setIdentifier(ci.getId());
					id.setQuality("certain");
					if (_facade.getCurrentUser() != null)
					{
						id.setAuthority(_facade.getCurrentUser().getPdrId());
					}
					_selectedPerson.getIdentifiers().getIdentifiers().add(id);
				}
			}
		}

	}

	private void removePersonIdentifiers(ConcurrenceData cd)
	{
		if (cd != null && cd.getIdentifiers() != null && !cd.getIdentifiers().isEmpty() && _selectedPerson != null
				&& _selectedPerson.getIdentifiers() != null)
		{
			for (ConcurrenceIdentifier ci : cd.getIdentifiers())
			{
				for (Identifier id : _selectedPerson.getIdentifiers().getIdentifiers())
				{
					if (ci.getProvider() != null && id.getProvider() != null
							&& ci.getProvider().equalsIgnoreCase(id.getProvider()) && ci.getId() != null
							&& id.getIdentifier() != null && ci.getId().equalsIgnoreCase(id.getIdentifier()))
					{
						_selectedPerson.getIdentifiers().getIdentifiers().remove(id);
						break;
					}

				}
			}
		}

	}
	@Override
	public void setFocus()
	{
		// set the focus
	}

	private void searchConcurringPersons(final int searchTarget,
			final IConcurrenceSearchService concurrenceSearchService)
	{
		final ConcurrenceSearchController searchController = new ConcurrenceSearchController();
		switch (searchTarget)
		{
			case 0:
				_personsContainer = _facade.getAllPersons();
				break;
			case 1:
				_personsContainer = loadTreeObjects();
				break;
			case 2:
				_personsContainer = loadSelectedPersons();
			default:
				break;
		}

		ProgressMonitorDialog dialog = new ProgressMonitorDialog(new Shell());
		dialog.setCancelable(true);
		// System.out.println("searchConcurringPersons " + searchTarget);
		try
		{
			dialog.run(true, true, new IRunnableWithProgress()
			{

				@Override
				public void run(final IProgressMonitor monitor)
				{
					searchController.search(searchTarget, _personsContainer, concurrenceSearchService, _resultMap,
							monitor);
					monitor.done();
				}
			});
		}
		catch (InvocationTargetException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		IStructuredSelection sel;
		IWorkbenchWindow ww = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		ISelectionService selService = ww.getSelectionService();
		sel = (IStructuredSelection) selService.getSelection(AEPluginIDs.VIEW_TREEVIEW);
		if (sel != null)
		{
			Object obj = ((IStructuredSelection) sel).getFirstElement();
			if (obj instanceof Person)
			{
				_selectedPerson = (Person) obj;
				if (_resultMap != null && _resultMap.containsKey(_selectedPerson.getPdrId()))
				{
					loadConcurrenceData(_selectedPerson, _resultMap.get(_selectedPerson.getPdrId()));
				}
			}
		}
	}

	private Object loadSelectedPersons()
	{
		final ArrayList<Person> selection = new ArrayList<Person>();
		IStructuredSelection sel;
		IWorkbenchWindow ww = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		ISelectionService selService = ww.getSelectionService();
		sel = (IStructuredSelection) selService.getSelection(AEPluginIDs.VIEW_TREEVIEW);
		if (sel != null)
		{
			Object[] obj = ((IStructuredSelection) sel).toArray();
			for (Object o : obj)
			{
				if (o instanceof Person)
				{
					selection.add((Person) o);
				}
			}
		}
		return selection;
	}

	private Object loadTreeObjects()
	{
		// System.out.println("loadTreeObjects");
		Treeview view = (Treeview) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.findView(AEPluginIDs.VIEW_TREEVIEW);
		ISourceProviderService service = (ISourceProviderService) PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getService(ISourceProviderService.class);
		@SuppressWarnings("rawtypes")
		Map state = service.getSourceProvider(AEPluginIDs.SOURCE_PARAMETER_TREE).getCurrentState();
		String currentTree = (String) state.get(AEPluginIDs.SOURCE_PARAMETER_TREE);
		PdrObject[] objects = view.getInputOfTree(currentTree, true);
		return objects;
	}

	@Override
	public void dispose()
	{
		IWorkbenchWindow ww = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		ISelectionService selService = ww.getSelectionService();
		selService.removeSelectionListener(AEPluginIDs.VIEW_TREEVIEW, _mainSelectionListener);
		_resultMap = null;
		_facade.deleteObserver(this);
		super.dispose();
	}


	@Override
	public void update(Observable o, Object arg)
	{
		// System.out.println("update");
		if (arg.equals("newTreeObjects")) //$NON-NLS-1$ //$NON-NLS-2$
		{
			IStructuredSelection sel;
			IWorkbenchWindow ww = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
			ISelectionService selService = ww.getSelectionService();
			sel = (IStructuredSelection) selService.getSelection(AEPluginIDs.VIEW_TREEVIEW);
			Object obj = null;
			if (sel != null)
			{
				obj = sel.getFirstElement();
			}
			if (obj != null && obj instanceof Person)
			{
				_selectedPerson = (Person) obj;
				if (_resultMap != null && _resultMap.containsKey(_selectedPerson.getPdrId()))
				{
					loadConcurrenceData(_selectedPerson, _resultMap.get(_selectedPerson.getPdrId()));
				}
				else
				{
					loadConcurrenceData(_selectedPerson, null);
					if (_chain2Selection)
					{
						searchConcurringPersons(2, _concurrenceSearchService);
					}
				}
			}
		}

	}

	@Override
	public int getSizeFlags(boolean width)
	{
		return SWT.MIN;
	}

	@Override
	public int computePreferredSize(boolean width, int availableParallel, int availablePerpendicular,
			int preferredResult)
	{
		return 440;
	}
}
