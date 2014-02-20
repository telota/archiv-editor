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
package org.bbaw.pdr.ae.export.swt;

import java.util.HashMap;
import java.util.Vector;

import org.bbaw.pdr.ae.common.CommonActivator;
import org.bbaw.pdr.ae.common.NLMessages;
import org.bbaw.pdr.ae.common.icons.IconsInternal;
import org.bbaw.pdr.ae.common.interfaces.AEFilter;
import org.bbaw.pdr.ae.export.pluggable.AeExportCoreProvider;
import org.bbaw.pdr.ae.export.swt.preview.PdrSelectionFilterPreview;
import org.bbaw.pdr.ae.model.PdrObject;
import org.bbaw.pdr.ae.model.view.OrderingHead;
import org.bbaw.pdr.ae.view.control.PDRObjectsProvider;
import org.bbaw.pdr.ae.view.control.PDROrdererFactory;
import org.bbaw.pdr.ae.view.control.filters.AspectExcludeObjectRelationsFilter;
import org.bbaw.pdr.ae.view.control.filters.AspectSearchTextFilter;
import org.bbaw.pdr.ae.view.control.filters.OnlyAspectDivergentMarkup;
import org.bbaw.pdr.ae.view.control.filters.OnlyAspectsAboutReferenceFilter;
import org.bbaw.pdr.ae.view.control.filters.OnlyAspectsBasedOnReferenceFilter;
import org.bbaw.pdr.ae.view.control.filters.OnlyNewPDRObjectsFilter;
import org.bbaw.pdr.ae.view.control.filters.OnlyUpdatedPDRObjectsFilter;
import org.bbaw.pdr.ae.view.main.dialogs.FilterSelectionDialog;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

public class PdrObjectsPreview extends Composite implements IPdrWidgetStructure  {
	
	private final static String SETTINGS_SECTION = "pdrObjectsPreview";
	private final static String SETTINGS_ORDERER = "aspectOrdererId";
	
	private PdrSelectionFilterPreview preview;
	private WizardPage wizardPage;
	
	private int currentOrdererSelection;
	private Text searchQueryText;
	private PDRObjectsProvider _pdrObjectsProvider = AeExportCoreProvider.getInstance().getPdrObjectsProvider();
	
	private String pluginId;
	
	private static HashMap<String, String> filterActionsImgIds = new HashMap<String, String>();
	static {
		filterActionsImgIds.put("View_action_only_newAspects",IconsInternal.DECORATION_NEW);
		filterActionsImgIds.put("View_action_only_updated_aspects",IconsInternal.DECORATION_UPDATED);
		filterActionsImgIds.put("View_action_exclude_other_relations",IconsInternal.RELATION);
		filterActionsImgIds.put("View_action_only_divergent_markup",IconsInternal.MARKUP_QUESTION);
		filterActionsImgIds.put("View_action_only_based_on_reference",IconsInternal.REFERENCE);
		filterActionsImgIds.put("View_action_only_mentioning_reference",IconsInternal.REFERENCE_ASPECTS);
		filterActionsImgIds.put("View_action_reference_filter",IconsInternal.REFERENCES);
		filterActionsImgIds.put("View_action_person_filter",IconsInternal.PERSONS);
		filterActionsImgIds.put("View_action_semantic_filter",IconsInternal.CLASSIFICATIONS);
		filterActionsImgIds.put("View_action_time_filter",IconsInternal.TIME);
		filterActionsImgIds.put("View_action_user_filter",IconsInternal.USER);
		};
	private static HashMap<String, AEFilter> filterActionFilters = new HashMap<String, AEFilter>();
	static {
		filterActionFilters.put("View_action_only_newAspects",new OnlyNewPDRObjectsFilter());
		filterActionFilters.put("View_action_only_updated_aspects",new OnlyUpdatedPDRObjectsFilter());
		filterActionFilters.put("View_action_exclude_other_relations",new AspectExcludeObjectRelationsFilter(null));
		filterActionFilters.put("View_action_only_divergent_markup",new OnlyAspectDivergentMarkup());
		filterActionFilters.put("View_action_only_based_on_reference",new OnlyAspectsBasedOnReferenceFilter(null));
		filterActionFilters.put("View_action_only_mentioning_reference",new OnlyAspectsAboutReferenceFilter(null));		
	};
	private static HashMap<String, String> filterActionDialogIds = new HashMap<String, String>();
	static {
		filterActionDialogIds.put("View_action_reference_filter","reference");
		filterActionDialogIds.put("View_action_person_filter","person");
		filterActionDialogIds.put("View_action_semantic_filter","semantic");
		filterActionDialogIds.put("View_action_time_filter","year");
		filterActionDialogIds.put("View_action_user_filter","user");
		};

	
	
	PdrObjectsPreview(Composite parent, int style) {
		super(parent, style);
	}
	
	public PdrObjectsPreview(String pluginId, WizardPage page, Composite parent) {
		super(parent, SWT.BORDER);
		this.wizardPage=page;
		//setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
		this.pluginId = pluginId;
		
		currentOrdererSelection = -1;

		// restore latest ordering mode
		IDialogSettings settings = AeExportCoreProvider.getInstance().getSettings(pluginId);
		if (settings.getSection(SETTINGS_SECTION) != null) {
			String ordererId = settings.getSection(SETTINGS_SECTION).get(SETTINGS_ORDERER);
			if (ordererId != null) {
				int i = 0;
				while (currentOrdererSelection < 0 && i < PDROrdererFactory.ORDERER_IDs.length) {
					if (PDROrdererFactory.ORDERER_IDs[i].equals(ordererId))
						currentOrdererSelection = i;
					i++;
				}
			}
		}
		if (currentOrdererSelection<0)
			currentOrdererSelection=1; // semantic
			
		
		//container = new Composite(parent, SWT.BORDER);
		// set top level layout for this widget and make it fit its parent composite
		int columns = 7;
		setLayout(new GridLayout(columns, true));
		setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 
				((GridLayout)parent.getLayout()).numColumns, 1));
		
		Composite previewContainer = new Composite(this, SWT.BORDER);
		GridData grid = new GridData(SWT.FILL, SWT.FILL, true, true, columns, 1);
		previewContainer.setLayoutData(grid);
		
		previewContainer.setLayout(new GridLayout(1,false));
		preview = new PdrSelectionFilterPreview(wizardPage, previewContainer); // FIXME: hier nicht neue erzeugen, 
		// sondern eine zentral vorhandene verwenden!
		
		// build controls composite for preview regrouping
		Composite ctlGrouping = new Composite(this, SWT.BORDER);
		grid = new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1);
		ctlGrouping.setLayoutData(grid);
		ctlGrouping.setLayout(new GridLayout(3,false));
		
		Label groupingLbl = new Label(ctlGrouping, SWT.NONE);
		groupingLbl.setText(NLMessages.getString("View_group_by"));
		groupingLbl.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		
		final Combo groupingCombo = new Combo(ctlGrouping, SWT.NONE | SWT.READ_ONLY);
		groupingCombo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
		groupingCombo.setItems(new String[]
			{NLMessages.getString("View_group_all"), NLMessages.getString("View_group_semantic"),
					NLMessages.getString("View_group_year"), NLMessages.getString("View_group_place"),
					NLMessages.getString("View_group_reference"), NLMessages.getString("View_group_relation"),
					NLMessages.getString("View_group_markup"), NLMessages.getString("View_group_user")});
		// classification combo listener
		groupingCombo.addSelectionListener(new SelectionAdapter(){
			private int selection = -1;
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				int item = groupingCombo.getSelectionIndex();
				if (item != selection) {
					if (item>0) {
						AeExportCoreProvider.getInstance().getPdrObjectsProvider().setOrderer(
								PDROrdererFactory.createAspectOrderer(PDROrdererFactory.ORDERER_IDs[item]));
						System.out.println(" Orderer id: "+PDROrdererFactory.ORDERER_IDs[item]);
					} else
						AeExportCoreProvider.getInstance().getPdrObjectsProvider().setOrderer(null);
					currentOrdererSelection=item;
					selection = currentOrdererSelection;
					preview.updateColumnLabel(NLMessages.getString("View_group_by")+": "+groupingCombo.getText());
					preview.update();
				}
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				widgetSelected(arg0); //TODO: sinnvoll?
			}
		});
		groupingCombo.select(currentOrdererSelection); // loest kein event aus
		System.out.println("  PDR Objects Preview Orderer: "+currentOrdererSelection+"; "+
				PDROrdererFactory.ORDERER_IDs[currentOrdererSelection]);
		AeExportCoreProvider.getInstance().getPdrObjectsProvider().setOrderer(
				PDROrdererFactory.createAspectOrderer(PDROrdererFactory.ORDERER_IDs[currentOrdererSelection]));
		preview.updateColumnLabel(NLMessages.getString("View_group_by")+": "+groupingCombo.getText());
		preview.update(); // geht vermutlich auch schöner. FIXME: wirft unten im objectsprovider auch nen
		// nullpointer!
		
		ImageRegistry imgReg = CommonActivator.getDefault().getImageRegistry();
		// build controls composite for preview filtering
		Composite ctlFilters = new Composite(this, SWT.BORDER);
		ctlFilters.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 5, 1));
		ctlFilters.setLayout(new GridLayout(9, false));
		
		Label filterLabel = new Label(ctlFilters, SWT.NONE);
		filterLabel.setImage(imgReg.get(IconsInternal.FILTER));
		filterLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		
		// FIXME: bei window resize wird die toolbar mit den filtern total klein und 
		// die combo für gruppenauswahl total groß
		ToolBar filterBar = new ToolBar(ctlFilters, SWT.NONE);
		filterBar.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 3, 1));
		filterBar.setLayout(new GridLayout(filterActionsImgIds.size(), false));
		ToolBarManager filterBarMng = new ToolBarManager(filterBar);

		for (final String actionId : filterActionsImgIds.keySet()) {
			String imgId = filterActionsImgIds.get(actionId);
			//System.out.println(actionId+" "+imgId);
			Action action = filterActionFilters.containsKey(actionId) ? 
				new Action(NLMessages.getString(actionId)){
					AEFilter filter = filterActionFilters.get(actionId);
					@Override
					public void run() {
						if (isChecked()){
							_pdrObjectsProvider.addFilter(filter);
						} else
							_pdrObjectsProvider.removeFilter(filter);
						preview.update();
					};
				} 
			:
				new Action(NLMessages.getString(actionId)){
					String type = filterActionDialogIds.get(actionId);
					boolean isActive() {
						return type.equals("person") ? _pdrObjectsProvider.hasPersonFilter()
								: (type.equals("reference") ? _pdrObjectsProvider.hasReferenceFilter()
								: (type.equals("semantic") ? _pdrObjectsProvider.hasSemanticFilter()
								: (type.equals("user") ? _pdrObjectsProvider.hasUserFilter()
								: _pdrObjectsProvider.hasYearFilter())));
					}
					@Override
					public void run() {
						FilterSelectionDialog dialog = createDialog(type);
						if (!isActive()) {
							if (dialog.open() == 0)
								preview.update();
						} else {
							AEFilter af = _pdrObjectsProvider.getFilter(type);
							_pdrObjectsProvider.removeFilter(af);
						}
						boolean state = isActive();
						setChecked(state);
					}
				};
			action.setEnabled(true);
			//TODO: objectsprovider is being saved isn't it?
			action.setChecked(false);
			action.setImageDescriptor(imgReg.getDescriptor(imgId));
			filterBarMng.add(action);
		}
		filterBarMng.update(true);
		
		Label searchQueryLabel = new Label(ctlFilters, SWT.NONE);
		searchQueryLabel.setText(NLMessages.getString("View_search_notification"));
		searchQueryLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		searchQueryText = new Text(ctlFilters, SWT.SINGLE | SWT.BORDER);
		searchQueryText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		
		Button searchQueryButton = new Button(ctlFilters, SWT.PUSH);
		searchQueryButton.setToolTipText(NLMessages.getString("View_search_notification"));
		searchQueryButton.setImage(imgReg.get(IconsInternal.SEARCH));
		searchQueryButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		
		searchQueryButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				String search = searchQueryText.getText();
				AspectSearchTextFilter filter = _pdrObjectsProvider.getSearchFilter();
				if (search.trim().length() > 0)	{
					if (filter != null)	{
						filter.setSearchText(search);
					} else	{
						filter = new AspectSearchTextFilter(search);
						_pdrObjectsProvider.addFilter(filter);
						preview.update();
					}
				}
				else {
					if (filter != null) {
						_pdrObjectsProvider.removeFilter(filter);
						preview.update();
					}
				}			
			}});
		
	}
	

	/**
	 * Returns true if the underlying {@link PdrSelectionFilterPreview} of this
	 * widget contains a valid selection of {@link PdrObject}s ready for export.
	 */
	public boolean isValid() {
		return preview.isValid();
	}

	/**
	 * Returns the status message which is currently set by the 
	 * underlying {@link PdrSelectionFilterPreview}.
	 */
	public String getMessage() {
		return preview.getMessage();
	}
	
	/** 
	 * Returns all {@link PdrObject}s that are currently selected
	 * in this preview.
	 * @return
	 */
	public PdrObject[] getSelectedObjects() {
		return preview.getSelectedObjects();
	}


	/**
	 * From the current preview tree forest, extract those trees that contain nodes
	 * marked for export.
	 * @return
	 */
	public Vector<OrderingHead> getSelectionHeads() {
		//TODO doc
		return AeExportCoreProvider.getInstance().getPdrObjectsTree().getSelectionHeads();
	}
	
	//TODO doc
	private FilterSelectionDialog createDialog(String type) {
		IWorkbench workbench = PlatformUI.getWorkbench();
		Display display = workbench.getDisplay();
		Shell shell = new Shell(display);
		FilterSelectionDialog dialog = new FilterSelectionDialog(shell, _pdrObjectsProvider, type);
		return dialog;
	}
	
	@Override
	public void performFinish() {
		IDialogSettings settings = AeExportCoreProvider.getInstance().
				getSettingsSection(pluginId, SETTINGS_SECTION);
		settings.put(SETTINGS_ORDERER, PDROrdererFactory.ORDERER_IDs[currentOrdererSelection]);
	}
}
