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
package org.bbaw.pdr.ae.view.main.dialogs;

import java.util.HashMap;
import java.util.Vector;

import org.bbaw.pdr.ae.common.AEConstants;
import org.bbaw.pdr.ae.common.CommonActivator;
import org.bbaw.pdr.ae.common.NLMessages;
import org.bbaw.pdr.ae.common.icons.IconsInternal;
import org.bbaw.pdr.ae.config.core.ConfigDataComparator;
import org.bbaw.pdr.ae.config.model.ConfigData;
import org.bbaw.pdr.ae.config.model.ConfigItem;
import org.bbaw.pdr.ae.config.model.DataType;
import org.bbaw.pdr.ae.control.facade.Facade;
import org.bbaw.pdr.ae.control.interfaces.AMainSearcher;
import org.bbaw.pdr.ae.metamodel.IAEPresentable;
import org.bbaw.pdr.ae.metamodel.PdrDate;
import org.bbaw.pdr.ae.model.ReferenceModsTemplate;
import org.bbaw.pdr.ae.model.search.Criteria;
import org.bbaw.pdr.ae.model.search.Operator;
import org.bbaw.pdr.ae.model.search.PdrQuery;
import org.bbaw.pdr.ae.view.control.customSWTWidges.YearSpinner;
import org.bbaw.pdr.ae.view.control.provider.AEConfigPresentableContentProvider;
import org.bbaw.pdr.ae.view.control.provider.AEConfigPresentableLabelProvider;
import org.bbaw.pdr.ae.view.control.provider.MarkupContentProvider;
import org.bbaw.pdr.ae.view.control.provider.MarkupLabelProvider;
import org.bbaw.pdr.ae.view.control.provider.RefTemplateContentProvider;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.fieldassist.AutoCompleteField;
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
import org.eclipse.swt.events.FocusAdapter;
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
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

/**
 * The Class AdvancedSearchDialog.
 * @author Christoph Plutte
 */
public class AdvancedSearchDialog extends TitleAreaDialog
{

	/** Instance of shared image registry. */
	private ImageRegistry _imageReg = CommonActivator.getDefault().getImageRegistry();
	/** singleton facade instance. */
	private Facade _facade = Facade.getInstanz();
	/* UserConfigLoader. */
	/** MainSearcher. */
	private AMainSearcher _mainSearcher = _facade.getMainSearcher();

	/** The preselection. */
	private int _preselection = Platform.getPreferencesService().getInt(CommonActivator.PLUGIN_ID,
			"ASPECT_PRESELECTED_DATE_YEAR", AEConstants.ASPECT_PRESELECTED_DATE_YEAR, null); //$NON-NLS-1$

	/** The loaded query. */
	private PdrQuery _loadedQuery;

	/** The person query. */
	private PdrQuery _personQuery;

	/** The facet query. */
	private PdrQuery _facetQuery;

	/** The facet strings. */
	private String[] _facetStrings;

	/** The aspect facet query. */
	private PdrQuery _aspectFacetQuery;

	/** The reference query. */
	private PdrQuery _referenceQuery;

	/** The aspect facet strings. */
	private String[] _aspectFacetStrings;

	/** The reference facets. */
	private String[] _referenceFacets;

	/** layout elements. */
	private TabFolder _mainTabFolder;

	/** person tab item. */
	private TabItem _personTabItem;
	/** The person tab item. */
	private TabItem _facetPersonTabItem;

	/** The facet aspect tab item. */
	private TabItem _facetAspectTabItem;

	/** The reference tab item. */
	private TabItem _referenceTabItem;

	/** The grid layout. */
	private GridLayout _gridLayout;

	/** The grid layout2. */
	private GridLayout _gridLayout2;

	/** The grid data2. */
	private GridData _gridData2;

	/** The facet element. */
	private String _facetElement;

	/** The facet type. */
	private String _facetType;

	/** The facet subtype. */
	private String _facetSubtype;

	/** The facet role. */
	private String _facetRole;

	/** The aspect facet element. */
	private String _aspectFacetElement;

	/** The aspect facet type. */
	private String _aspectFacetType;

	/** The aspect facet subtype. */
	private String _aspectFacetSubtype;

	/** The aspect facet role. */
	private String _aspectFacetRole;

	/**
	 * Composite des TabItems personTabItem.
	 */
	private Composite _personComposite;

	/** The search p tag comp. */
	private Composite _searchPTagComp;

	/** The search p rel comp. */
	private Composite _searchPRelComp;

	/** The search p date comp. */
	private Composite _searchPDateComp;

	/** The search p ref comp. */
	private Composite _searchPRefComp;

	/** The search a tag comp. */
	private Composite _searchATagComp;

	/** The search a date comp. */
	private Composite _searchADateComp;

	/** The search a ref comp. */
	private Composite _searchARefComp;

	/** The person search group. */
	private Group _personSearchGroup;

	/** The facet person search group. */
	private Group _facetPersonSearchGroup;

	/** The facet aspect search group. */
	private Group _facetAspectSearchGroup;

	/** The reference search group. */
	private Group _referenceSearchGroup;

	/** The markup provider. */
	private String _markupProvider = Platform
			.getPreferencesService()
			.getString(CommonActivator.PLUGIN_ID,
					"PRIMARY_TAGGING_PROVIDER", AEConstants.CLASSIFICATION_AUTHORITY, null).toUpperCase(); //$NON-NLS-1$

	/** The relation provider. */
	private String _relationProvider = Platform
			.getPreferencesService()
			.getString(CommonActivator.PLUGIN_ID, "PRIMARY_RELATION_PROVIDER",
					AEConstants.RELATION_CLASSIFICATION_PROVIDER, null).toUpperCase();

	/** The facet combo. */
	private Combo _facetCombo;

	/** The facet type combo viewer. */
	private ComboViewer _facetTypeComboViewer;

	/** The facet subtype combo viewer. */
	private ComboViewer _facetSubtypeComboViewer;

	/** The facet role combo viewer. */
	private ComboViewer _facetRoleComboViewer;

	/** The aspect facet combo. */
	private Combo _aspectFacetCombo;

	/** The aspect facet type combo viewer. */
	private ComboViewer _aspectFacetTypeComboViewer;

	/** The aspect facet subtype combo viewer. */
	private ComboViewer _aspectFacetSubtypeComboViewer;

	/** The aspect facet role combo viewer. */
	private ComboViewer _aspectFacetRoleComboViewer;

	/**
	 * Instantiates a new advanced search dialog.
	 * @param parentShell the parent shell
	 * @param loadedQuery the loaded query
	 */
	public AdvancedSearchDialog(final Shell parentShell, final PdrQuery loadedQuery)
	{
		super(parentShell);
		this._loadedQuery = loadedQuery;
		if (_markupProvider == null)
		{
			_markupProvider = (String) _facade.getConfigs().keySet().toArray()[0];
		}
		if (_relationProvider == null)
		{
			_relationProvider = (String) _facade.getConfigs().keySet().toArray()[0];
		}
	}

	/**
	 * Builds the facet aspect search.
	 * @param type the type
	 * @param crit the crit
	 */
	private void buildFacetAspectSearch(final int type, final Integer crit)
	{

		DataType dtAll = new DataType();
		dtAll.setValue("ALL"); //$NON-NLS-1$
		dtAll.setLabel("ALL"); //$NON-NLS-1$

		boolean tag1 = true;
		boolean date1 = true;

		Composite searchPFacetComp = new Composite(_facetAspectSearchGroup, SWT.NONE);
		searchPFacetComp.setLayout(new GridLayout());
		((GridLayout) searchPFacetComp.getLayout()).makeColumnsEqualWidth = true;
		((GridLayout) searchPFacetComp.getLayout()).numColumns = 12;
		searchPFacetComp.setLayoutData(new GridData());
		((GridData) searchPFacetComp.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) searchPFacetComp.getLayoutData()).grabExcessHorizontalSpace = true;
		// ((GridData) searchPTagComp.getLayoutData()).heightHint = 200;
		((GridData) searchPFacetComp.getLayoutData()).grabExcessVerticalSpace = false;
		((GridData) searchPFacetComp.getLayoutData()).horizontalSpan = 1;

		Label aspectFacet = new Label(searchPFacetComp, SWT.NONE);
		aspectFacet.setText("Choose aspectFacets"); //$NON-NLS-1$
		aspectFacet.setLayoutData(new GridData());
		((GridData) aspectFacet.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) aspectFacet.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) aspectFacet.getLayoutData()).horizontalSpan = 4;

		SelectionListener aspectFacetListener = new SelectionAdapter()
		{
			@Override
			public void widgetDefaultSelected(final SelectionEvent e)
			{
			}

			@SuppressWarnings("unchecked")
			@Override
			public void widgetSelected(final SelectionEvent e)
			{
				final String type = (String) ((Button) e.getSource()).getData();
				_aspectFacetQuery.setKey(type);
				//                 System.out.println("aspectFacetQuery key set to " + type); //$NON-NLS-1$
				if (type.equals("content")) //$NON-NLS-1$
				{
					try
					{
						_aspectFacetStrings = _mainSearcher.getFacets(
								"tagging", _aspectFacetElement, _aspectFacetType, _aspectFacetSubtype, //$NON-NLS-1$
								_aspectFacetRole);
						setQueryFacets(_aspectFacetQuery, _aspectFacetStrings);
						_aspectFacetCombo.setItems(_aspectFacetStrings);
					}
					catch (Exception e1)
					{
						e1.printStackTrace();
					}
				}
				else if (type.equals("type")) //$NON-NLS-1$
				{
					try
					{
						_aspectFacetStrings = (_mainSearcher.getFacets(
								"tagging_values", _aspectFacetElement, null, null, null)); //$NON-NLS-1$
					}
					catch (Exception e1)
					{
						e1.printStackTrace();
					}
					_aspectFacetQuery.setFacets((HashMap<String, IAEPresentable>) _aspectFacetTypeComboViewer
							.getInput());
				}
				else if (type.equals("subtype")) //$NON-NLS-1$
				{
					try
					{
						_aspectFacetStrings = (_mainSearcher.getFacets(
								"tagging_values", _aspectFacetElement, _aspectFacetType, null, null)); //$NON-NLS-1$
					}
					catch (Exception e1)
					{
						e1.printStackTrace();
					}
					_aspectFacetQuery.setFacets((HashMap<String, IAEPresentable>) _aspectFacetSubtypeComboViewer
							.getInput());
				}
				else if (type.equals("role")) //$NON-NLS-1$
				{
					try
					{
						_aspectFacetStrings = (_mainSearcher.getFacets(
								"tagging_values", _aspectFacetElement, _aspectFacetType, _aspectFacetSubtype, null)); //$NON-NLS-1$
					}
					catch (Exception e1)
					{
						e1.printStackTrace();
					}
					_aspectFacetQuery.setFacets((HashMap<String, IAEPresentable>) _aspectFacetRoleComboViewer
							.getInput());

				}

			}
		};
		Button facetTypeButton = new Button(searchPFacetComp, SWT.RADIO);
		facetTypeButton.setText(NLMessages.getString("Dialog_type"));
		facetTypeButton.setData("type"); //$NON-NLS-1$
		facetTypeButton.addSelectionListener(aspectFacetListener);
		facetTypeButton.setLayoutData(new GridData());
		((GridData) facetTypeButton.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) facetTypeButton.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) facetTypeButton.getLayoutData()).horizontalSpan = 2;

		Button facetSubtypeButton = new Button(searchPFacetComp, SWT.RADIO);
		facetSubtypeButton.setText(NLMessages.getString("Dialog_subtype"));
		facetSubtypeButton.setData("subtype"); //$NON-NLS-1$
		facetSubtypeButton.addSelectionListener(aspectFacetListener);
		facetSubtypeButton.setLayoutData(new GridData());
		((GridData) facetSubtypeButton.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) facetSubtypeButton.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) facetSubtypeButton.getLayoutData()).horizontalSpan = 2;

		Button facetRoleButton = new Button(searchPFacetComp, SWT.RADIO);
		facetRoleButton.setText(NLMessages.getString("Dialog_role"));
		facetRoleButton.setData("role"); //$NON-NLS-1$
		facetRoleButton.addSelectionListener(aspectFacetListener);
		facetRoleButton.setLayoutData(new GridData());
		((GridData) facetRoleButton.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) facetRoleButton.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) facetRoleButton.getLayoutData()).horizontalSpan = 2;

		Button facetContentButton = new Button(searchPFacetComp, SWT.RADIO);
		facetContentButton.setText(NLMessages.getString("Dialog_markup_content"));
		facetContentButton.setData("content"); //$NON-NLS-1$
		facetContentButton.addSelectionListener(aspectFacetListener);
		facetContentButton.setLayoutData(new GridData());
		((GridData) facetContentButton.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) facetContentButton.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) facetContentButton.getLayoutData()).horizontalSpan = 2;
		facetContentButton.setSelection(true);

		Button chooseFacetsButton = new Button(searchPFacetComp, SWT.CHECK);
		chooseFacetsButton.setText(NLMessages.getString("Dialog_faceted_search"));
		chooseFacetsButton.setLayoutData(new GridData());
		((GridData) chooseFacetsButton.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) chooseFacetsButton.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) chooseFacetsButton.getLayoutData()).horizontalSpan = 2;
		chooseFacetsButton.setSelection(_aspectFacetQuery.getType() == 4);
		chooseFacetsButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent event)
			{
				if (_aspectFacetQuery.getType() == 4)
				{
					_aspectFacetQuery.setType(0);
				}
				else
				{
					_aspectFacetQuery.setType(4);
				}
			}
		});

		final Combo facetElementCombo = new Combo(searchPFacetComp, SWT.READ_ONLY);
		final ComboViewer facetElementComboViewer = new ComboViewer(facetElementCombo);
		final Combo facetTypeCombo = new Combo(searchPFacetComp, SWT.READ_ONLY);
		_aspectFacetTypeComboViewer = new ComboViewer(facetTypeCombo);
		final Combo facetSubtypeCombo = new Combo(searchPFacetComp, SWT.READ_ONLY);
		_aspectFacetSubtypeComboViewer = new ComboViewer(facetSubtypeCombo);
		final Combo facetRoleCombo = new Combo(searchPFacetComp, SWT.READ_ONLY);
		_aspectFacetRoleComboViewer = new ComboViewer(facetRoleCombo);

		_aspectFacetCombo = new Combo(searchPFacetComp, SWT.READ_ONLY);
		facetElementCombo.setLayoutData(new GridData());

		facetElementCombo.setLayoutData(new GridData());
		((GridData) facetElementCombo.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) facetElementCombo.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) facetElementCombo.getLayoutData()).horizontalSpan = 2;

		facetElementComboViewer.setContentProvider(new MarkupContentProvider(true));
		facetElementComboViewer.setLabelProvider(new MarkupLabelProvider());
		facetElementComboViewer.setComparator(new ConfigDataComparator());
		if (_facade.getConfigs().containsKey(_markupProvider))
		{
			HashMap<String, ConfigData> input = _facade.getConfigs().get(_markupProvider).getChildren();
			facetElementComboViewer.setInput(input);
		}
		Object obj = facetElementComboViewer.getElementAt(0);
		if (obj != null)
		{
			facetElementComboViewer.setSelection(new StructuredSelection(obj));
			ConfigData cd = (ConfigData) facetElementComboViewer.getElementAt(0);
			if (cd.getValue().startsWith("aodl:"))
			{
				_aspectFacetElement = cd.getValue().substring(5); //$NON-NLS-1$
			}
			else
			{
				_aspectFacetElement = cd.getValue();
			}

		}
		facetElementComboViewer.addSelectionChangedListener(new ISelectionChangedListener()
		{
			@Override
			@SuppressWarnings("unchecked")
			public void selectionChanged(final SelectionChangedEvent event)
			{
				ISelection iSelection = event.getSelection();
				Object obj = ((IStructuredSelection) iSelection).getFirstElement();
				ConfigData cd = (ConfigData) obj;
				if (cd.getValue().startsWith("aodl:"))
				{
					_aspectFacetElement = cd.getValue().substring(5); //$NON-NLS-1$
				}
				else
				{
					_aspectFacetElement = cd.getValue();
				}
				_aspectFacetQuery.getCriterias().get(0).setCrit1(_aspectFacetElement);
				facetTypeCombo.removeAll();
				_aspectFacetQuery.getCriterias().get(0).setCrit2(null);
				facetSubtypeCombo.removeAll();
				_aspectFacetQuery.getCriterias().get(0).setCrit3(null);
				facetRoleCombo.removeAll();
				_aspectFacetQuery.getCriterias().get(0).setCrit4(null);
				_aspectFacetCombo.removeAll();
				setComboViewerInput(_aspectFacetTypeComboViewer, "tagging_values", _aspectFacetElement, null, null); //$NON-NLS-1$

				if (_aspectFacetQuery.getKey().equals("content")) //$NON-NLS-1$
				{
					try
					{
						_aspectFacetStrings = _mainSearcher.getFacets(
								"tagging", _aspectFacetElement, _aspectFacetType, _aspectFacetSubtype, //$NON-NLS-1$
								_aspectFacetRole);
						_aspectFacetCombo.setItems(_aspectFacetStrings);
						setQueryFacets(_aspectFacetQuery, _aspectFacetStrings);
					}
					catch (Exception e1)
					{
						e1.printStackTrace();
					}
				}
				else if (_aspectFacetQuery.getKey().equals("type")) //$NON-NLS-1$
				{
					try
					{
						_aspectFacetStrings = (_mainSearcher.getFacets(
								"tagging_values", _aspectFacetElement, null, null, null)); //$NON-NLS-1$
					}
					catch (Exception e1)
					{
						e1.printStackTrace();
					}
					_aspectFacetQuery.setFacets((HashMap<String, IAEPresentable>) _aspectFacetTypeComboViewer
							.getInput());
				}

			}
		});

		facetTypeCombo.setLayoutData(new GridData());
		facetTypeCombo.setLayoutData(new GridData());
		((GridData) facetTypeCombo.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) facetTypeCombo.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) facetTypeCombo.getLayoutData()).horizontalSpan = 2;
		_aspectFacetTypeComboViewer.setContentProvider(new MarkupContentProvider());
		_aspectFacetTypeComboViewer.setLabelProvider(new MarkupLabelProvider());
		_aspectFacetTypeComboViewer.setComparator(new ConfigDataComparator());

		_aspectFacetTypeComboViewer.addSelectionChangedListener(new ISelectionChangedListener()
		{
			@Override
			@SuppressWarnings("unchecked")
			public void selectionChanged(final SelectionChangedEvent event)
			{
				ISelection iSelection = event.getSelection();
				Object obj = ((IStructuredSelection) iSelection).getFirstElement();
				ConfigData cd = (ConfigData) obj;
				facetSubtypeCombo.removeAll();
				_aspectFacetQuery.getCriterias().get(0).setCrit3(null);
				facetRoleCombo.removeAll();
				_aspectFacetQuery.getCriterias().get(0).setCrit4(null);
				_aspectFacetCombo.removeAll();
				_aspectFacetType = cd.getValue();
				_aspectFacetQuery.getCriterias().get(0).setCrit2(_aspectFacetType);
				setComboViewerInput(_aspectFacetSubtypeComboViewer,
						"tagging_values", _aspectFacetElement, _aspectFacetType, null); //$NON-NLS-1$

				if (_aspectFacetQuery.getKey().equals("content")) //$NON-NLS-1$
				{
					try
					{
						_aspectFacetStrings = (_mainSearcher.getFacets(
								"tagging", _aspectFacetElement, _aspectFacetType, _aspectFacetSubtype, //$NON-NLS-1$
								_aspectFacetRole));
						setQueryFacets(_aspectFacetQuery, _aspectFacetStrings);
						_aspectFacetCombo.setItems(_aspectFacetStrings);
					}
					catch (Exception e1)
					{
						e1.printStackTrace();
					}
				}
				else if (_aspectFacetQuery.getKey().equals("subtype")) //$NON-NLS-1$
				{
					try
					{
						_aspectFacetStrings = (_mainSearcher.getFacets(
								"tagging_values", _aspectFacetElement, _aspectFacetType, null, null)); //$NON-NLS-1$
					}
					catch (Exception e1)
					{
						e1.printStackTrace();
					}
					_aspectFacetQuery.setFacets((HashMap<String, IAEPresentable>) _aspectFacetSubtypeComboViewer
							.getInput());

				}
			}
		});

		facetSubtypeCombo.setLayoutData(new GridData());
		facetSubtypeCombo.setLayoutData(new GridData());
		((GridData) facetSubtypeCombo.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) facetSubtypeCombo.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) facetSubtypeCombo.getLayoutData()).horizontalSpan = 2;
		_aspectFacetSubtypeComboViewer.setContentProvider(new MarkupContentProvider());
		_aspectFacetSubtypeComboViewer.setLabelProvider(new MarkupLabelProvider());
		_aspectFacetSubtypeComboViewer.setComparator(new ConfigDataComparator());

		_aspectFacetSubtypeComboViewer.addSelectionChangedListener(new ISelectionChangedListener()
		{
			@Override
			@SuppressWarnings("unchecked")
			public void selectionChanged(final SelectionChangedEvent event)
			{
				ISelection iSelection = event.getSelection();
				Object obj = ((IStructuredSelection) iSelection).getFirstElement();
				ConfigData cd = (ConfigData) obj;
				_aspectFacetSubtype = cd.getValue();
				facetRoleCombo.removeAll();
				_aspectFacetQuery.getCriterias().get(0).setCrit4(null);
				_aspectFacetCombo.removeAll();
				_aspectFacetQuery.getCriterias().get(0).setCrit3(_aspectFacetSubtype);
				setComboViewerInput(_aspectFacetRoleComboViewer, "tagging_values", _aspectFacetElement,
						_aspectFacetType, _aspectFacetSubtype);

				if (_aspectFacetQuery.getKey().equals("content")) //$NON-NLS-1$
				{
					try
					{
						_aspectFacetStrings = (_mainSearcher.getFacets(
								"tagging", _aspectFacetElement, _aspectFacetType, _aspectFacetSubtype, //$NON-NLS-1$
								_aspectFacetRole));
						setQueryFacets(_aspectFacetQuery, _aspectFacetStrings);
						_aspectFacetCombo.setItems(_aspectFacetStrings);
					}
					catch (Exception e1)
					{
						e1.printStackTrace();
					}
				}
				else if (_aspectFacetQuery.getKey().equals("role")) //$NON-NLS-1$
				{
					try
					{
						_aspectFacetStrings = (_mainSearcher.getFacets(
								"tagging_values", _aspectFacetElement, _aspectFacetType, _aspectFacetSubtype, null)); //$NON-NLS-1$
					}
					catch (Exception e1)
					{
						e1.printStackTrace();
					}
					_aspectFacetQuery.setFacets((HashMap<String, IAEPresentable>) _aspectFacetRoleComboViewer
							.getInput());

				}

			}
		});

		facetRoleCombo.setLayoutData(new GridData());
		facetRoleCombo.setLayoutData(new GridData());
		((GridData) facetRoleCombo.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) facetRoleCombo.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) facetRoleCombo.getLayoutData()).horizontalSpan = 2;
		_aspectFacetRoleComboViewer.setContentProvider(new MarkupContentProvider());
		_aspectFacetRoleComboViewer.setLabelProvider(new MarkupLabelProvider());
		_aspectFacetRoleComboViewer.setComparator(new ConfigDataComparator());

		_aspectFacetRoleComboViewer.addSelectionChangedListener(new ISelectionChangedListener()
		{
			@Override
			public void selectionChanged(final SelectionChangedEvent event)
			{
				ISelection iSelection = event.getSelection();
				Object obj = ((IStructuredSelection) iSelection).getFirstElement();
				ConfigData cd = (ConfigData) obj;
				_aspectFacetRole = cd.getValue();
				_aspectFacetCombo.removeAll();
				_aspectFacetQuery.getCriterias().get(0).setCrit4(_aspectFacetRole);
				if (_aspectFacetQuery.getKey().equals("content")) //$NON-NLS-1$
				{
					try
					{
						_aspectFacetStrings = (_mainSearcher.getFacets(
								"tagging", _aspectFacetElement, _aspectFacetType, _aspectFacetSubtype, //$NON-NLS-1$
								_aspectFacetRole));
						setQueryFacets(_aspectFacetQuery, _aspectFacetStrings);
						_aspectFacetCombo.setItems(_aspectFacetStrings);
					}
					catch (Exception e1)
					{
						e1.printStackTrace();
					}
				}

			}
		});

		_aspectFacetCombo.setLayoutData(new GridData());
		_aspectFacetCombo.setLayoutData(new GridData());
		((GridData) _aspectFacetCombo.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _aspectFacetCombo.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) _aspectFacetCombo.getLayoutData()).horizontalSpan = 2;

		// facetCombo.addFocusListener(new FocusAdapter(){
		// public void focusGained(FocusEvent e)
		// {
		// try {
		//					facetQuery.setFacets(mainSearcher.getFacets( "tagging", facetElement, facetType, facetSubtype, //$NON-NLS-1$
		// facetRole));
		// facetCombo.setItems(facetQuery.getFacets());
		//
		// } catch (Exception e1) {
		// e1.printStackTrace();
		// }
		// }
		// }
		// );

		for (int i = 1; i < _aspectFacetQuery.getCriterias().size(); i++)
		{
			//			System.out.println("for i = " + i); //$NON-NLS-1$
			final Criteria c = _aspectFacetQuery.getCriterias().get(i);

			if (c.getType().equals("tagging")) //$NON-NLS-1$
			{
				if (tag1)
				{
					tag1 = false;
					_searchATagComp = new Composite(_facetAspectSearchGroup, SWT.NONE);
					_searchATagComp.setLayout(new GridLayout());
					((GridLayout) _searchATagComp.getLayout()).makeColumnsEqualWidth = true;
					((GridLayout) _searchATagComp.getLayout()).numColumns = 14;
					_searchATagComp.setLayoutData(new GridData());
					((GridData) _searchATagComp.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) _searchATagComp.getLayoutData()).grabExcessHorizontalSpace = true;
					// ((GridData) searchATagComp.getLayoutData()).heightHint =
					// 200;
					((GridData) _searchATagComp.getLayoutData()).grabExcessVerticalSpace = false;
					((GridData) _searchATagComp.getLayoutData()).horizontalSpan = 1;

					Label op = new Label(_searchATagComp, SWT.NONE);
					op.setText(NLMessages.getString("Dialog_operand")); //$NON-NLS-1$
					op.setLayoutData(new GridData());
					((GridData) op.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) op.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) op.getLayoutData()).horizontalSpan = 1;

					Label sem = new Label(_searchATagComp, SWT.NONE);
					sem.setText(NLMessages.getString("Dialog_semantic")); //$NON-NLS-1$
					sem.setLayoutData(new GridData());
					((GridData) sem.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) sem.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) sem.getLayoutData()).horizontalSpan = 2;

					Label tagName = new Label(_searchATagComp, SWT.NONE);
					tagName.setText(NLMessages.getString("Dialog_markup")); //$NON-NLS-1$
					tagName.setLayoutData(new GridData());
					((GridData) tagName.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) tagName.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) tagName.getLayoutData()).horizontalSpan = 2;

					Label tagType = new Label(_searchATagComp, SWT.NONE);
					tagType.setText(NLMessages.getString("Dialog_type")); //$NON-NLS-1$
					tagType.setLayoutData(new GridData());
					((GridData) tagType.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) tagType.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) tagType.getLayoutData()).horizontalSpan = 2;

					Label tagSubtype = new Label(_searchATagComp, SWT.NONE);
					tagSubtype.setText(NLMessages.getString("Dialog_subtype")); //$NON-NLS-1$
					tagSubtype.setLayoutData(new GridData());
					((GridData) tagSubtype.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) tagSubtype.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) tagSubtype.getLayoutData()).horizontalSpan = 2;

					Label searchTextLabel = new Label(_searchATagComp, SWT.NONE);
					searchTextLabel.setText(NLMessages.getString("Dialog_searchText")); //$NON-NLS-1$
					searchTextLabel.setLayoutData(new GridData());
					((GridData) searchTextLabel.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) searchTextLabel.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) searchTextLabel.getLayoutData()).horizontalSpan = 3;

					Label fuzzy = new Label(_searchATagComp, SWT.NONE);
					fuzzy.setText(NLMessages.getString("Dialog_fuzzy")); //$NON-NLS-1$
					fuzzy.setLayoutData(new GridData());
					((GridData) fuzzy.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) fuzzy.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) fuzzy.getLayoutData()).horizontalSpan = 1;

					Label include = new Label(_searchATagComp, SWT.NONE);
					include.setText(NLMessages.getString("Dialog_include")); //$NON-NLS-1$
					include.setToolTipText(NLMessages.getString("Dialog_includeConcurrences")); //$NON-NLS-1$
					include.setLayoutData(new GridData());
					((GridData) include.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) include.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) include.getLayoutData()).horizontalSpan = 1;

				}

				if (i == 0)
				{
					Label l = new Label(_searchATagComp, SWT.NONE);
					l.setText(NLMessages.getString("Dialog_markup")); //$NON-NLS-1$
					l.setLayoutData(new GridData());
					((GridData) l.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) l.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) l.getLayoutData()).horizontalSpan = 1;
				}
				else
				{
					Label l = new Label(_searchATagComp, SWT.NONE);
					l.setText("AND"); //$NON-NLS-1$
					l.setLayoutData(new GridData());
					((GridData) l.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) l.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) l.getLayoutData()).horizontalSpan = 1;

					// final Combo opCombo = new Combo(searchATagComp,
					// SWT.READ_ONLY);
					// opCombo.setLayoutData(new GridData());
					// opCombo.add(Operator.AND.toString());
					// opCombo.add(Operator.OR.toString());
					// opCombo.add(Operator.NOT.toString());
					// opCombo.setLayoutData(new GridData());
					// ((GridData) opCombo.getLayoutData()).horizontalAlignment
					// = SWT.FILL;
					// ((GridData)
					// opCombo.getLayoutData()).grabExcessHorizontalSpace = true
					// ;
					// ((GridData) opCombo.getLayoutData()).horizontalSpan = 1;
					// if (c.getOperator() != null)
					// {
					// opCombo.setText(c.getOperator());
					// }
					// else
					// {
					// opCombo.select(0);
					// c.setOperator(opCombo.getItem(0));
					//
					// }
					// opCombo.addSelectionListener(new SelectionAdapter(){
					// public void widgetSelected(SelectionEvent se){
					// c.setOperator(opCombo.getItem(opCombo.getSelectionIndex()));
					// }
					// });
				}

				final Combo semCombo = new Combo(_searchATagComp, SWT.READ_ONLY);
				semCombo.setLayoutData(new GridData());
				ComboViewer comboSemanticViewer = new ComboViewer(semCombo);
				comboSemanticViewer.setContentProvider(new AEConfigPresentableContentProvider());
				comboSemanticViewer.setLabelProvider(new AEConfigPresentableLabelProvider());
				((AEConfigPresentableContentProvider) comboSemanticViewer.getContentProvider()).setAddALL(true);

				if (_facade.getAllSemantics() != null && !_facade.getAllSemantics().isEmpty())
				{
					comboSemanticViewer.setInput(_facade.getAllSemantics());
					if (c.getCrit0() != null)
					{
						setComboViewerByString(comboSemanticViewer, c.getCrit0());
					}
					else
					{
						semCombo.select(0);
						c.setCrit0(semCombo.getItem(0));
					}
				}

				semCombo.setLayoutData(new GridData());
				((GridData) semCombo.getLayoutData()).horizontalAlignment = SWT.FILL;
				((GridData) semCombo.getLayoutData()).grabExcessHorizontalSpace = true;
				((GridData) semCombo.getLayoutData()).horizontalSpan = 2;

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
							c.setCrit0(cp.getValue());
						}
					}
				});

				final Combo tagCombo = new Combo(_searchATagComp, SWT.READ_ONLY);
				tagCombo.setLayoutData(new GridData());
				tagCombo.setLayoutData(new GridData());
				((GridData) tagCombo.getLayoutData()).horizontalAlignment = SWT.FILL;
				((GridData) tagCombo.getLayoutData()).grabExcessHorizontalSpace = true;
				((GridData) tagCombo.getLayoutData()).horizontalSpan = 2;
				final ComboViewer tagComboViewer = new ComboViewer(tagCombo);
				tagComboViewer.setContentProvider(new MarkupContentProvider(true));
				tagComboViewer.setLabelProvider(new MarkupLabelProvider());
				tagComboViewer.setComparator(new ConfigDataComparator());

				if (_facade.getConfigs().containsKey(_markupProvider))
				{
					HashMap<String, ConfigData> input = _facade.getConfigs().get(_markupProvider).getChildren();
				tagComboViewer.setInput(input);
				}

				if (c.getCrit1() != null)
				{
					setComboViewerByString(tagComboViewer, c.getCrit1());
				}
				else
				{
					obj = tagComboViewer.getElementAt(0);
					if (obj != null)
					{
						tagComboViewer.setSelection(new StructuredSelection(obj));
						ConfigData cd = (ConfigData) tagComboViewer.getElementAt(0);
						if (cd.getValue().startsWith("aodl:"))
						{
							c.setCrit1(cd.getValue().substring(5)); //$NON-NLS-1$
						}
						else
						{
							c.setCrit1(cd.getValue());
						}

					}
				}
				final Combo typeCombo = new Combo(_searchATagComp, SWT.READ_ONLY);
				typeCombo.setLayoutData(new GridData());
				typeCombo.setLayoutData(new GridData());
				((GridData) typeCombo.getLayoutData()).horizontalAlignment = SWT.FILL;
				((GridData) typeCombo.getLayoutData()).grabExcessHorizontalSpace = true;
				((GridData) typeCombo.getLayoutData()).horizontalSpan = 2;
				final ComboViewer typeComboViewer = new ComboViewer(typeCombo);
				typeComboViewer.setContentProvider(new MarkupContentProvider());
				typeComboViewer.setLabelProvider(new MarkupLabelProvider());
				typeComboViewer.setComparator(new ConfigDataComparator());

				tagComboViewer.addSelectionChangedListener(new ISelectionChangedListener()
				{
					@Override
					public void selectionChanged(final SelectionChangedEvent event)
					{
						ISelection iSelection = event.getSelection();
						Object obj = ((IStructuredSelection) iSelection).getFirstElement();
						ConfigData cd = (ConfigData) obj;
						String selection;
						if (cd.getValue().startsWith("aodl:"))
						{
							selection = cd.getValue().substring(5); //$NON-NLS-1$
						}
						else
						{
							selection = cd.getValue();
						}
						c.setCrit1(selection);
						setComboViewerInput(typeComboViewer, "tagging_values", c.getCrit1(), null, null); //$NON-NLS-1$
					}
				});

				if (c.getCrit2() != null)
				{
					setComboViewerByString(typeComboViewer, c.getCrit2());
				}

				final Combo subtypeCombo = new Combo(_searchATagComp, SWT.READ_ONLY);
				final ComboViewer subtypeComboViewer = new ComboViewer(subtypeCombo);
				subtypeComboViewer.setContentProvider(new MarkupContentProvider());
				subtypeComboViewer.setLabelProvider(new MarkupLabelProvider());
				subtypeComboViewer.setComparator(new ConfigDataComparator());

				// typeCombo.addFocusListener(new FocusAdapter(){
				// public void focusGained(FocusEvent e)
				// {
				// }
				// }
				// );
				typeComboViewer.addSelectionChangedListener(new ISelectionChangedListener()
				{
					@Override
					public void selectionChanged(final SelectionChangedEvent event)
					{
						ISelection iSelection = event.getSelection();
						Object obj = ((IStructuredSelection) iSelection).getFirstElement();
						ConfigData cd = (ConfigData) obj;
						subtypeComboViewer.setInput(null);
						if (cd != null)
						{
							c.setCrit2(cd.getValue());
						}
						setComboViewerInput(subtypeComboViewer, "tagging_values", c.getCrit1(), c.getCrit2(), null); //$NON-NLS-1$
					}
				});

				subtypeCombo.setLayoutData(new GridData());
				subtypeCombo.setLayoutData(new GridData());
				((GridData) subtypeCombo.getLayoutData()).horizontalAlignment = SWT.FILL;
				((GridData) subtypeCombo.getLayoutData()).grabExcessHorizontalSpace = true;
				((GridData) subtypeCombo.getLayoutData()).horizontalSpan = 2;
				if (c.getCrit3() != null)
				{
					setComboViewerByString(subtypeComboViewer, c.getCrit3());
				}
				// subtypeCombo.addFocusListener(new FocusAdapter(){
				// public void focusGained(FocusEvent e)
				// {
				//
				// }
				// }
				// );
				subtypeComboViewer.addSelectionChangedListener(new ISelectionChangedListener()
				{
					@Override
					public void selectionChanged(final SelectionChangedEvent event)
					{
						ISelection iSelection = event.getSelection();
						Object obj = ((IStructuredSelection) iSelection).getFirstElement();
						ConfigData cd = (ConfigData) obj;
						if (cd != null)
						{
							c.setCrit3(cd.getValue());
						}
					}
				});

				final Text searchText = new Text(_searchATagComp, SWT.BORDER);
				searchText.setLayoutData(new GridData());
				searchText.setLayoutData(new GridData());
				((GridData) searchText.getLayoutData()).horizontalAlignment = SWT.FILL;
				((GridData) searchText.getLayoutData()).grabExcessHorizontalSpace = true;
				((GridData) searchText.getLayoutData()).horizontalSpan = 3;
				if (c.getSearchText() != null)
				{
					searchText.setText(c.getSearchText());
				}
				searchText.addFocusListener(new FocusAdapter()
				{
					@Override
					public void focusLost(final FocusEvent e)
					{
						c.setSearchText(searchText.getText());
					}
				});
				searchText.addKeyListener(new KeyListener()
				{
					@Override
					public void keyPressed(final KeyEvent e)
					{
						if (e.keyCode == SWT.CR)
						{
							c.setSearchText(searchText.getText());
							okPressed();
						}
					}

					@Override
					public void keyReleased(final KeyEvent e)
					{
					}
				});
				final Button fuzzyB = new Button(_searchATagComp, SWT.CHECK);
				fuzzyB.setLayoutData(new GridData());
				fuzzyB.setSelection(c.isFuzzy());
				fuzzyB.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent event)
					{
						c.setFuzzy(!c.isFuzzy());
					}
				});

				final Button includeB = new Button(_searchATagComp, SWT.CHECK);
				includeB.setLayoutData(new GridData());
				includeB.setSelection(c.isIncludeConcurrences());
				includeB.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent event)
					{
						c.setIncludeConcurrences(!c.isIncludeConcurrences());
					}
				});

			} // if tagging

			if (c.getType().equals("date")) //$NON-NLS-1$
			{
				if (date1)
				{
					date1 = false;
					_searchADateComp = new Composite(_facetAspectSearchGroup, SWT.NONE);
					_searchADateComp.setLayout(new GridLayout());
					((GridLayout) _searchADateComp.getLayout()).makeColumnsEqualWidth = true;
					((GridLayout) _searchADateComp.getLayout()).numColumns = 14;
					_searchADateComp.setLayoutData(new GridData());
					((GridData) _searchADateComp.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) _searchADateComp.getLayoutData()).grabExcessHorizontalSpace = true;
					// ((GridData) searchADateComp.getLayoutData()).heightHint =
					// 200;
					((GridData) _searchADateComp.getLayoutData()).grabExcessVerticalSpace = false;
					((GridData) _searchADateComp.getLayoutData()).horizontalSpan = 1;

					Label l2 = new Label(_searchADateComp, SWT.NONE);
					l2.setText(NLMessages.getString("Dialog_date")); //$NON-NLS-1$
					l2.setLayoutData(new GridData());
					((GridData) l2.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) l2.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) l2.getLayoutData()).horizontalSpan = 1;

					Label typeDate = new Label(_searchADateComp, SWT.NONE);
					typeDate.setText(NLMessages.getString("Dialog_type")); //$NON-NLS-1$
					typeDate.setLayoutData(new GridData());
					((GridData) typeDate.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) typeDate.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) typeDate.getLayoutData()).horizontalSpan = 2;

					Label bl = new Label(_searchADateComp, SWT.NONE);
					bl.setText(""); //$NON-NLS-1$
					Label notBefore = new Label(_searchADateComp, SWT.NONE);
					notBefore.setText(NLMessages.getString("Dialog_day")); //$NON-NLS-1$
					notBefore.setLayoutData(new GridData());
					((GridData) notBefore.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) notBefore.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) notBefore.getLayoutData()).horizontalSpan = 1;

					Label month = new Label(_searchADateComp, SWT.NONE);
					month.setText(NLMessages.getString("Dialog_month")); //$NON-NLS-1$
					month.setLayoutData(new GridData());
					((GridData) month.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) month.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) month.getLayoutData()).horizontalSpan = 1;

					Label year = new Label(_searchADateComp, SWT.NONE);
					year.setText(NLMessages.getString("Dialog_year")); //$NON-NLS-1$
					year.setLayoutData(new GridData());
					((GridData) year.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) year.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) year.getLayoutData()).horizontalSpan = 2;

					Label bl2 = new Label(_searchADateComp, SWT.NONE);
					bl2.setText(""); //$NON-NLS-1$

					Label notAfter = new Label(_searchADateComp, SWT.NONE);
					notAfter.setText(NLMessages.getString("Dialog_day")); //$NON-NLS-1$
					notAfter.setLayoutData(new GridData());
					((GridData) notAfter.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) notAfter.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) notAfter.getLayoutData()).horizontalSpan = 1;

					Label month2 = new Label(_searchADateComp, SWT.NONE);
					month2.setText(NLMessages.getString("Dialog_month")); //$NON-NLS-1$
					month2.setLayoutData(new GridData());
					((GridData) month2.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) month2.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) month2.getLayoutData()).horizontalSpan = 1;

					Label year2 = new Label(_searchADateComp, SWT.NONE);
					year2.setText(NLMessages.getString("Dialog_year")); //$NON-NLS-1$
					year2.setLayoutData(new GridData());
					((GridData) year2.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) year2.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) year2.getLayoutData()).horizontalSpan = 2;

					Label include = new Label(_searchADateComp, SWT.NONE);
					include.setText(""); //$NON-NLS-1$
					include.setToolTipText(NLMessages.getString("Dialog_includeConcurrences")); //$NON-NLS-1$
					include.setLayoutData(new GridData());
					((GridData) include.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) include.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) include.getLayoutData()).horizontalSpan = 1;

				}

				if (i == 0)
				{
					Label l = new Label(_searchADateComp, SWT.NONE);
					l.setLayoutData(new GridData());
					((GridData) l.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) l.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) l.getLayoutData()).horizontalSpan = 1;
				}
				else
				{
					Label l = new Label(_searchADateComp, SWT.NONE);
					l.setText("AND"); //$NON-NLS-1$
					l.setLayoutData(new GridData());
					((GridData) l.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) l.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) l.getLayoutData()).horizontalSpan = 1;

					// final Combo opCombo = new Combo(searchADateComp,
					// SWT.READ_ONLY);
					// opCombo.setLayoutData(new GridData());
					// opCombo.add(Operator.AND.toString());
					// opCombo.add(Operator.OR.toString());
					// opCombo.add(Operator.NOT.toString());
					// opCombo.setLayoutData(new GridData());
					// ((GridData) opCombo.getLayoutData()).horizontalAlignment
					// = SWT.FILL;
					// ((GridData)
					// opCombo.getLayoutData()).grabExcessHorizontalSpace = true
					// ;
					// ((GridData) opCombo.getLayoutData()).horizontalSpan = 1;
					// if (c.getOperator() != null)
					// {
					// opCombo.setText(c.getOperator());
					// }
					// else
					// {
					// opCombo.select(0);
					// c.setOperator(opCombo.getItem(0));
					//
					// }
					// opCombo.addSelectionListener(new SelectionAdapter(){
					// public void widgetSelected(SelectionEvent se){
					// c.setOperator(opCombo.getItem(opCombo.getSelectionIndex()));
					// }
					// });
				}

				final Combo typeDCombo = new Combo(_searchADateComp, SWT.READ_ONLY);
				typeDCombo.setLayoutData(new GridData());
				ComboViewer timeTypeComboViewer = new ComboViewer(typeDCombo);
				timeTypeComboViewer.setContentProvider(ArrayContentProvider.getInstance());
				timeTypeComboViewer.setLabelProvider(new LabelProvider()
				{

					@Override
					public String getText(final Object element)
					{
						String str = (String) element;
						if (NLMessages.getString("Editor_time_" + str) != null) //$NON-NLS-1$
						{
							return NLMessages.getString("Editor_time_" + str); //$NON-NLS-1$
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
						c.setDateType(s);
					}

				});
				if (c.getDateType() != null)
				{
					StructuredSelection selection = new StructuredSelection(c.getDateType());
					timeTypeComboViewer.setSelection(selection);
				}
				else
				{
					StructuredSelection selection = new StructuredSelection(AEConstants.TIME_TYPES[0]);
					timeTypeComboViewer.setSelection(selection);
					c.setDateType(AEConstants.TIME_TYPES[0]);
				}
				typeDCombo.pack();
				typeDCombo.setLayoutData(new GridData());
				((GridData) typeDCombo.getLayoutData()).horizontalAlignment = SWT.FILL;
				((GridData) typeDCombo.getLayoutData()).grabExcessHorizontalSpace = true;
				((GridData) typeDCombo.getLayoutData()).horizontalSpan = 2;

				Label from = new Label(_searchADateComp, SWT.NONE);
				from.setText(NLMessages.getString("Dialog_from")); //$NON-NLS-1$
				from.setLayoutData(new GridData());
				((GridData) from.getLayoutData()).horizontalAlignment = SWT.FILL;
				((GridData) from.getLayoutData()).grabExcessHorizontalSpace = true;
				((GridData) from.getLayoutData()).horizontalSpan = 1;

				final Combo day1Combo = new Combo(_searchADateComp, SWT.READ_ONLY);
				day1Combo.setLayoutData(new GridData());
				day1Combo.setItems(AEConstants.DAYS);
				day1Combo.setLayoutData(new GridData());
				((GridData) day1Combo.getLayoutData()).horizontalAlignment = SWT.FILL;
				((GridData) day1Combo.getLayoutData()).grabExcessHorizontalSpace = true;
				((GridData) day1Combo.getLayoutData()).horizontalSpan = 1;

				if (c.getDateFrom() == null)
				{
					PdrDate dateFrom = new PdrDate("0000-00-00"); //$NON-NLS-1$
					// dateFrom.setDay(0);
					// dateFrom.setMonth(0);
					// dateFrom.setYear(0);
					c.setDateFrom(dateFrom);
					day1Combo.select(c.getDateFrom().getDay());
				}
				//				System.out.println("test: dateFrom " + c.getDateFrom().toString()); //$NON-NLS-1$
				// else
				// {
				//
				// day1Combo.select(0);
				// c.getDateFrom().setDay(0);
				//
				// }

				day1Combo.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent se)
					{
						c.getDateFrom().setDay(day1Combo.getSelectionIndex());
					}
				});

				final Combo month1Combo = new Combo(_searchADateComp, SWT.READ_ONLY);
				month1Combo.setLayoutData(new GridData());
				month1Combo.setItems(AEConstants.MONTHS);
				month1Combo.setLayoutData(new GridData());
				((GridData) month1Combo.getLayoutData()).horizontalAlignment = SWT.FILL;
				((GridData) month1Combo.getLayoutData()).grabExcessHorizontalSpace = true;
				((GridData) month1Combo.getLayoutData()).horizontalSpan = 1;
				if (c.getDateFrom() != null)
				{
					month1Combo.select(c.getDateFrom().getMonth());
				}
				else
				{

					month1Combo.select(0);
					c.getDateFrom().setMonth(0);

				}

				month1Combo.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent se)
					{
						c.getDateFrom().setMonth(month1Combo.getSelectionIndex());
					}
				});

				final YearSpinner year1Spinner = new YearSpinner(_searchADateComp, SWT.NULL);
				// year1Spinner.setLayoutData(new GridData());
				// year1Spinner.setLayoutData(new GridData());
				// ((GridData) year1Spinner.getLayoutData()).horizontalAlignment
				// = SWT.FILL;
				// ((GridData)
				// year1Spinner.getLayoutData()).grabExcessHorizontalSpace =
				// true;
				// ((GridData) year1Spinner.getLayoutData()).horizontalSpan = 2;
				// year1Spinner.setMinimum(0);
				// year1Spinner.setMaximum(9999);
				if (c.getDateFrom() != null)
				{
					year1Spinner.setSelection(c.getDateFrom().getYear());
				}
				else
				{

					year1Spinner.setSelection(_preselection);
					c.getDateFrom().setYear(_preselection);

				}

				year1Spinner.addSelectionListener(new SelectionListener()
				{

					@Override
					public void widgetDefaultSelected(final SelectionEvent e)
					{
						c.getDateFrom().setYear(year1Spinner.getSelection());
						// System.out.println("year1Spinner.getSelection() " +
						// year1Spinner.getSelection());

					}

					@Override
					public void widgetSelected(final SelectionEvent e)
					{
						c.getDateFrom().setYear(year1Spinner.getSelection());
						// System.out.println("year1Spinner.getSelection() " +
						// year1Spinner.getSelection());
					}

				});

				Label to = new Label(_searchADateComp, SWT.NONE);
				to.setText(NLMessages.getString("Dialog_to")); //$NON-NLS-1$
				to.setLayoutData(new GridData());
				((GridData) to.getLayoutData()).horizontalAlignment = SWT.FILL;
				((GridData) to.getLayoutData()).grabExcessHorizontalSpace = true;
				((GridData) to.getLayoutData()).horizontalSpan = 1;

				final Combo day2Combo = new Combo(_searchADateComp, SWT.READ_ONLY);
				day2Combo.setLayoutData(new GridData());
				day2Combo.setItems(AEConstants.DAYS);
				day2Combo.setLayoutData(new GridData());
				((GridData) day2Combo.getLayoutData()).horizontalAlignment = SWT.FILL;
				((GridData) day2Combo.getLayoutData()).grabExcessHorizontalSpace = true;
				((GridData) day2Combo.getLayoutData()).horizontalSpan = 1;
				if (c.getDateTo() == null)
				{
					PdrDate dateTo = new PdrDate("0000-00-00"); //$NON-NLS-1$
					c.setDateTo(dateTo);
					day2Combo.select(c.getDateTo().getDay());
				}

				day2Combo.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent se)
					{
						c.getDateTo().setDay(day2Combo.getSelectionIndex());
					}
				});

				final Combo month2Combo = new Combo(_searchADateComp, SWT.READ_ONLY);
				month2Combo.setLayoutData(new GridData());
				month2Combo.setItems(AEConstants.MONTHS);
				month2Combo.setLayoutData(new GridData());
				((GridData) month2Combo.getLayoutData()).horizontalAlignment = SWT.FILL;
				((GridData) month2Combo.getLayoutData()).grabExcessHorizontalSpace = true;
				((GridData) month2Combo.getLayoutData()).horizontalSpan = 1;
				if (c.getDateTo() != null)
				{
					month2Combo.select(c.getDateTo().getMonth());
				}
				else
				{

					month2Combo.select(0);
					c.getDateTo().setMonth(0);

				}

				month2Combo.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent se)
					{
						c.getDateTo().setMonth(month2Combo.getSelectionIndex());
					}
				});

				final YearSpinner year2Spinner = new YearSpinner(_searchADateComp, SWT.NULL);
				// year2Spinner.setLayoutData(new GridData());
				// year2Spinner.setLayoutData(new GridData());
				// ((GridData) year2Spinner.getLayoutData()).horizontalAlignment
				// = SWT.FILL;
				// ((GridData)
				// year2Spinner.getLayoutData()).grabExcessHorizontalSpace =
				// true;
				// ((GridData) year2Spinner.getLayoutData()).horizontalSpan = 2;
				// year2Spinner.setMinimum(0);
				// year2Spinner.setMaximum(9999);
				if (c.getDateTo() != null)
				{
					year2Spinner.setSelection(c.getDateTo().getYear());
				}
				else
				{

					year2Spinner.setSelection(_preselection);
					c.getDateTo().setYear(_preselection);

				}

				year2Spinner.addSelectionListener(new SelectionListener()
				{

					@Override
					public void widgetDefaultSelected(final SelectionEvent e)
					{
						c.getDateTo().setYear(year2Spinner.getSelection());

					}

					@Override
					public void widgetSelected(final SelectionEvent e)
					{
						c.getDateTo().setYear(year2Spinner.getSelection());
					}

				});

				final Button includeB = new Button(_searchADateComp, SWT.CHECK);
				includeB.setLayoutData(new GridData());
				includeB.setSelection(c.isIncludeConcurrences());
				includeB.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent event)
					{
						c.setIncludeConcurrences(!c.isIncludeConcurrences());
					}
				});

			} // if date

			if (c.getType().equals("reference")) //$NON-NLS-1$
			{

				_searchARefComp = new Composite(_facetAspectSearchGroup, SWT.NONE);
				_searchARefComp.setLayout(new GridLayout());
				((GridLayout) _searchARefComp.getLayout()).makeColumnsEqualWidth = true;
				((GridLayout) _searchARefComp.getLayout()).numColumns = 14;
				_searchARefComp.setLayoutData(new GridData());
				((GridData) _searchARefComp.getLayoutData()).horizontalAlignment = SWT.FILL;
				((GridData) _searchARefComp.getLayoutData()).grabExcessHorizontalSpace = true;
				// ((GridData) searchPTagComp.getLayoutData()).heightHint = 200;
				((GridData) _searchARefComp.getLayoutData()).grabExcessVerticalSpace = false;
				((GridData) _searchARefComp.getLayoutData()).horizontalSpan = 1;

				Label l = new Label(_searchARefComp, SWT.NONE);
				l.setText("AND"); //$NON-NLS-1$
				l.setLayoutData(new GridData());
				((GridData) l.getLayoutData()).horizontalAlignment = SWT.FILL;
				((GridData) l.getLayoutData()).grabExcessHorizontalSpace = true;
				((GridData) l.getLayoutData()).horizontalSpan = 1;

				// final Combo opCombo = new Combo(searchARefComp,
				// SWT.READ_ONLY);
				// opCombo.setLayoutData(new GridData());
				// opCombo.add(Operator.AND.toString());
				// opCombo.add(Operator.OR.toString());
				// opCombo.add(Operator.NOT.toString());
				// opCombo.setLayoutData(new GridData());
				// ((GridData) opCombo.getLayoutData()).horizontalAlignment =
				// SWT.FILL;
				// ((GridData)
				// opCombo.getLayoutData()).grabExcessHorizontalSpace = true ;
				// ((GridData) opCombo.getLayoutData()).horizontalSpan = 1;
				// if (c.getOperator() != null)
				// {
				// opCombo.setText(c.getOperator());
				// }
				// else
				// {
				// opCombo.select(0);
				// c.setOperator(opCombo.getItem(0));
				//
				// }
				// opCombo.addSelectionListener(new SelectionAdapter(){
				// public void widgetSelected(SelectionEvent se){
				// c.setOperator(opCombo.getItem(opCombo.getSelectionIndex()));
				// }
				// });

				Label sem = new Label(_searchARefComp, SWT.NONE);
				sem.setText(NLMessages.getString("Dialog_reference")); //$NON-NLS-1$
				sem.setLayoutData(new GridData());
				((GridData) sem.getLayoutData()).horizontalAlignment = SWT.FILL;
				((GridData) sem.getLayoutData()).grabExcessHorizontalSpace = true;
				((GridData) sem.getLayoutData()).horizontalSpan = 2;

				Label tagName = new Label(_searchARefComp, SWT.NONE);
				tagName.setText(NLMessages.getString("Dialog_genre")); //$NON-NLS-1$
				tagName.setLayoutData(new GridData());
				((GridData) tagName.getLayoutData()).horizontalAlignment = SWT.FILL;
				((GridData) tagName.getLayoutData()).grabExcessHorizontalSpace = true;
				((GridData) tagName.getLayoutData()).horizontalSpan = 2;

				final Combo genreCombo = new Combo(_searchARefComp, SWT.READ_ONLY);
				genreCombo.setLayoutData(new GridData());
				ComboViewer genreComboViewer = new ComboViewer(genreCombo);
				genreComboViewer.setContentProvider(new RefTemplateContentProvider(false));
				genreComboViewer.setLabelProvider(new LabelProvider()
				{

					@Override
					public String getText(final Object element)
					{
						ReferenceModsTemplate template = (ReferenceModsTemplate) element;
						return template.getLabel();
					}

				});

				genreComboViewer.setInput(_facade.getAllGenres());
				genreComboViewer.addSelectionChangedListener(new ISelectionChangedListener()
				{

					@Override
					public void selectionChanged(final SelectionChangedEvent event)
					{
						ISelection selection = event.getSelection();
						Object obj = ((IStructuredSelection) selection).getFirstElement();
						ReferenceModsTemplate template = (ReferenceModsTemplate) obj;
						if (template != null)
						{
							c.setCrit0(template.getValue());
						}
					}

				});
				genreCombo.add("ALL", 0); //$NON-NLS-1$

				genreCombo.setLayoutData(new GridData());
				((GridData) genreCombo.getLayoutData()).horizontalAlignment = SWT.FILL;
				((GridData) genreCombo.getLayoutData()).grabExcessHorizontalSpace = true;
				((GridData) genreCombo.getLayoutData()).horizontalSpan = 2;
				if (c.getCrit0() != null)
				{
					StructuredSelection selection = new StructuredSelection(c.getCrit0());
					genreComboViewer.setSelection(selection);
				}
				else
				{
					genreCombo.select(0);
					c.setCrit0(genreCombo.getItem(0));

				}

				Label tagType = new Label(_searchARefComp, SWT.NONE);
				tagType.setText(NLMessages.getString("Dialog_searchText")); //$NON-NLS-1$
				tagType.setLayoutData(new GridData());
				((GridData) tagType.getLayoutData()).horizontalAlignment = SWT.FILL;
				((GridData) tagType.getLayoutData()).grabExcessHorizontalSpace = true;
				((GridData) tagType.getLayoutData()).horizontalSpan = 2;

				final Text searchText = new Text(_searchARefComp, SWT.BORDER);
				searchText.setLayoutData(new GridData());
				searchText.setLayoutData(new GridData());
				((GridData) searchText.getLayoutData()).horizontalAlignment = SWT.FILL;
				((GridData) searchText.getLayoutData()).grabExcessHorizontalSpace = true;
				((GridData) searchText.getLayoutData()).horizontalSpan = 3;
				if (c.getSearchText() != null)
				{
					searchText.setText(c.getSearchText());
				}
				searchText.addFocusListener(new FocusAdapter()
				{
					@Override
					public void focusLost(final FocusEvent e)
					{
						c.setSearchText(searchText.getText());
					}
				});
				searchText.addKeyListener(new KeyListener()
				{
					@Override
					public void keyPressed(final KeyEvent e)
					{
						if (e.keyCode == SWT.CR)
						{
							c.setSearchText(searchText.getText());
							okPressed();
						}
					}

					@Override
					public void keyReleased(final KeyEvent e)
					{
					}
				});

				final Button fuzzyB = new Button(_searchARefComp, SWT.CHECK);
				fuzzyB.setLayoutData(new GridData());
				fuzzyB.setSelection(c.isFuzzy());
				fuzzyB.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent event)
					{
						c.setFuzzy(!c.isFuzzy());
					}
				});

				final Button includeB = new Button(_searchARefComp, SWT.CHECK);
				includeB.setLayoutData(new GridData());
				includeB.setSelection(c.isIncludeConcurrences());
				includeB.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent event)
					{
						c.setIncludeConcurrences(!c.isIncludeConcurrences());
					}
				});

			} // if reference
		}

		// contentCompSearch.layout();
		// scrollCompSearch.setContent(contentCompSearch);
		// scrollCompSearch.setMinSize(contentCompSearch.computeSize(SWT.DEFAULT,
		// SWT.DEFAULT, true));
		// scrollCompSearch.layout();
		_facetAspectSearchGroup.redraw();
		_facetAspectSearchGroup.layout();
		_facetAspectSearchGroup.pack();
		_facetAspectSearchGroup.layout();
		// facetPersonSearchGroup.pack();

	}

	/**
	 * Builds the facet person search.
	 * @param type the type
	 * @param crit the crit
	 */
	private void buildFacetPersonSearch(final int type, final Integer crit)
	{
		//

		DataType dtAll = new DataType();
		dtAll.setValue("ALL"); //$NON-NLS-1$
		dtAll.setLabel("ALL"); //$NON-NLS-1$

		boolean tag1 = true;
		boolean date1 = true;

		Composite searchPFacetComp = new Composite(_facetPersonSearchGroup, SWT.NONE);
		searchPFacetComp.setLayout(new GridLayout());
		((GridLayout) searchPFacetComp.getLayout()).makeColumnsEqualWidth = true;
		((GridLayout) searchPFacetComp.getLayout()).numColumns = 12;
		searchPFacetComp.setLayoutData(new GridData());
		((GridData) searchPFacetComp.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) searchPFacetComp.getLayoutData()).grabExcessHorizontalSpace = true;
		// ((GridData) searchPTagComp.getLayoutData()).heightHint = 200;
		((GridData) searchPFacetComp.getLayoutData()).grabExcessVerticalSpace = false;
		((GridData) searchPFacetComp.getLayoutData()).horizontalSpan = 1;

		Label facet = new Label(searchPFacetComp, SWT.NONE);
		facet.setText(NLMessages.getString("Dialog_choose_facets"));
		facet.setLayoutData(new GridData());
		((GridData) facet.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) facet.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) facet.getLayoutData()).horizontalSpan = 4;

		SelectionListener facetListener = new SelectionAdapter()
		{
			@Override
			public void widgetDefaultSelected(final SelectionEvent e)
			{
			}

			@SuppressWarnings("unchecked")
			@Override
			public void widgetSelected(final SelectionEvent e)
			{
				final String type = (String) ((Button) e.getSource()).getData();
				_facetQuery.setKey(type);
				//                 System.out.println("facetQuery key set to " + type); //$NON-NLS-1$
				if (type.equals("content")) //$NON-NLS-1$
				{
					try
					{
						_facetStrings = _mainSearcher.getFacets("tagging", _facetElement, _facetType, _facetSubtype, //$NON-NLS-1$
								_facetRole);
						setQueryFacets(_facetQuery, _facetStrings);
						_facetCombo.setItems(_facetStrings);
					}
					catch (Exception e1)
					{
						e1.printStackTrace();
					}
				}
				else if (type.equals("type")) //$NON-NLS-1$
				{
					try
					{
						_facetStrings = (_mainSearcher.getFacets("tagging_values", _facetElement, null, null, null)); //$NON-NLS-1$
					}
					catch (Exception e1)
					{
						e1.printStackTrace();
					}
					_facetQuery.setFacets((HashMap<String, IAEPresentable>) _facetTypeComboViewer.getInput());
				}
				else if (type.equals("subtype")) //$NON-NLS-1$
				{
					try
					{
						_facetStrings = (_mainSearcher.getFacets(
								"tagging_values", _facetElement, _facetType, null, null)); //$NON-NLS-1$
					}
					catch (Exception e1)
					{
						e1.printStackTrace();
					}
					_facetQuery.setFacets((HashMap<String, IAEPresentable>) _facetSubtypeComboViewer.getInput());
				}
				else if (type.equals("role")) //$NON-NLS-1$
				{
					try
					{
						_facetStrings = (_mainSearcher.getFacets(
								"tagging_values", _facetElement, _facetType, _facetSubtype, null)); //$NON-NLS-1$
					}
					catch (Exception e1)
					{
						e1.printStackTrace();
					}
					_facetQuery.setFacets((HashMap<String, IAEPresentable>) _facetRoleComboViewer.getInput());

				}

			}
		};
		Button facetTypeButton = new Button(searchPFacetComp, SWT.RADIO);
		facetTypeButton.setText(NLMessages.getString("Dialog_type"));
		facetTypeButton.setData("type"); //$NON-NLS-1$
		facetTypeButton.addSelectionListener(facetListener);
		facetTypeButton.setLayoutData(new GridData());
		((GridData) facetTypeButton.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) facetTypeButton.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) facetTypeButton.getLayoutData()).horizontalSpan = 2;

		Button facetSubtypeButton = new Button(searchPFacetComp, SWT.RADIO);
		facetSubtypeButton.setText(NLMessages.getString("Dialog_subtype"));
		facetSubtypeButton.setData("subtype"); //$NON-NLS-1$
		facetSubtypeButton.addSelectionListener(facetListener);
		facetSubtypeButton.setLayoutData(new GridData());
		((GridData) facetSubtypeButton.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) facetSubtypeButton.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) facetSubtypeButton.getLayoutData()).horizontalSpan = 2;

		Button facetRoleButton = new Button(searchPFacetComp, SWT.RADIO);
		facetRoleButton.setText(NLMessages.getString("Dialog_role"));
		facetRoleButton.setData("role"); //$NON-NLS-1$
		facetRoleButton.addSelectionListener(facetListener);
		facetRoleButton.setLayoutData(new GridData());
		((GridData) facetRoleButton.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) facetRoleButton.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) facetRoleButton.getLayoutData()).horizontalSpan = 2;

		Button facetContentButton = new Button(searchPFacetComp, SWT.RADIO);
		facetContentButton.setText(NLMessages.getString("Dialog_markup_content"));
		facetContentButton.setData("content"); //$NON-NLS-1$
		facetContentButton.addSelectionListener(facetListener);
		facetContentButton.setLayoutData(new GridData());
		((GridData) facetContentButton.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) facetContentButton.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) facetContentButton.getLayoutData()).horizontalSpan = 2;
		facetContentButton.setSelection(true);

		Label blanc = new Label(searchPFacetComp, SWT.NONE);
		blanc.setText(""); //$NON-NLS-1$
		blanc.setLayoutData(new GridData());
		((GridData) blanc.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) blanc.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) blanc.getLayoutData()).horizontalSpan = 2;
		final Combo facetElementCombo = new Combo(searchPFacetComp, SWT.READ_ONLY);
		final ComboViewer facetElementComboViewer = new ComboViewer(facetElementCombo);
		final Combo facetTypeCombo = new Combo(searchPFacetComp, SWT.READ_ONLY);
		_facetTypeComboViewer = new ComboViewer(facetTypeCombo);
		final Combo facetSubtypeCombo = new Combo(searchPFacetComp, SWT.READ_ONLY);
		_facetSubtypeComboViewer = new ComboViewer(facetSubtypeCombo);
		final Combo facetRoleCombo = new Combo(searchPFacetComp, SWT.READ_ONLY);
		_facetRoleComboViewer = new ComboViewer(facetRoleCombo);

		_facetCombo = new Combo(searchPFacetComp, SWT.READ_ONLY);
		facetElementCombo.setLayoutData(new GridData());

		facetElementCombo.setLayoutData(new GridData());
		((GridData) facetElementCombo.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) facetElementCombo.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) facetElementCombo.getLayoutData()).horizontalSpan = 2;

		facetElementComboViewer.setContentProvider(new MarkupContentProvider(true));
		facetElementComboViewer.setLabelProvider(new MarkupLabelProvider());
		facetElementComboViewer.setComparator(new ConfigDataComparator());
		if (_facade.getConfigs().containsKey(_markupProvider))
		{
		HashMap<String, ConfigData> input = _facade.getConfigs().get(_markupProvider).getChildren();
		facetElementComboViewer.setInput(input);
		}
		Object obj = facetElementComboViewer.getElementAt(0);
		if (obj != null)
		{
			facetElementComboViewer.setSelection(new StructuredSelection(obj));
			ConfigData cd = (ConfigData) facetElementComboViewer.getElementAt(0);
			if (cd.getValue().startsWith("aodl:"))
			{
				_facetElement = cd.getValue().substring(5); //$NON-NLS-1$
			}
			else
			{
				_facetElement = cd.getValue();
			}

		}

		facetElementComboViewer.addSelectionChangedListener(new ISelectionChangedListener()
		{
			@Override
			@SuppressWarnings("unchecked")
			public void selectionChanged(final SelectionChangedEvent event)
			{
				ISelection iSelection = event.getSelection();
				Object obj = ((IStructuredSelection) iSelection).getFirstElement();
				ConfigData cd = (ConfigData) obj;
				if (cd.getValue().startsWith("aodl:"))
				{
					_facetElement = cd.getValue().substring(5); //$NON-NLS-1$
				}
				else
				{
					_facetElement = cd.getValue();
				}
				_facetQuery.getCriterias().get(0).setCrit1(_facetElement);
				facetTypeCombo.removeAll();
				_facetQuery.getCriterias().get(0).setCrit2(null);
				facetSubtypeCombo.removeAll();
				_facetQuery.getCriterias().get(0).setCrit3(null);
				facetRoleCombo.removeAll();
				_facetQuery.getCriterias().get(0).setCrit4(null);
				_facetCombo.removeAll();
				setComboViewerInput(_facetTypeComboViewer, "tagging_values", _facetElement, null, null); //$NON-NLS-1$

				if (_facetQuery.getKey().equals("content")) //$NON-NLS-1$
				{
					try
					{
						_facetStrings = _mainSearcher.getFacets("tagging", _facetElement, _facetType, _facetSubtype, //$NON-NLS-1$
								_facetRole);
						_facetCombo.setItems(_facetStrings);
						setQueryFacets(_facetQuery, _facetStrings);
					}
					catch (Exception e1)
					{
						e1.printStackTrace();
					}
				}
				else if (_facetQuery.getKey().equals("type")) //$NON-NLS-1$
				{
					try
					{
						_facetStrings = (_mainSearcher.getFacets("tagging_values", _facetElement, null, null, null)); //$NON-NLS-1$
					}
					catch (Exception e1)
					{
						e1.printStackTrace();
					}
					_facetQuery.setFacets((HashMap<String, IAEPresentable>) _facetTypeComboViewer.getInput());
				}

			}
		});

		facetTypeCombo.setLayoutData(new GridData());
		facetTypeCombo.setLayoutData(new GridData());
		((GridData) facetTypeCombo.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) facetTypeCombo.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) facetTypeCombo.getLayoutData()).horizontalSpan = 2;
		_facetTypeComboViewer.setContentProvider(new MarkupContentProvider());
		_facetTypeComboViewer.setLabelProvider(new MarkupLabelProvider());
		_facetTypeComboViewer.setComparator(new ConfigDataComparator());

		_facetTypeComboViewer.addSelectionChangedListener(new ISelectionChangedListener()
		{
			@Override
			@SuppressWarnings("unchecked")
			public void selectionChanged(final SelectionChangedEvent event)
			{
				ISelection iSelection = event.getSelection();
				Object obj = ((IStructuredSelection) iSelection).getFirstElement();
				ConfigData cd = (ConfigData) obj;
				facetSubtypeCombo.removeAll();
				_facetQuery.getCriterias().get(0).setCrit3(null);
				facetRoleCombo.removeAll();
				_facetQuery.getCriterias().get(0).setCrit4(null);
				_facetCombo.removeAll();
				_facetType = cd.getValue();
				_facetQuery.getCriterias().get(0).setCrit2(_facetType);
				setComboViewerInput(_facetSubtypeComboViewer, "tagging_values", _facetElement, _facetType, null); //$NON-NLS-1$

				if (_facetQuery.getKey().equals("content")) //$NON-NLS-1$
				{
					try
					{
						_facetStrings = (_mainSearcher.getFacets("tagging", _facetElement, _facetType, _facetSubtype, //$NON-NLS-1$
								_facetRole));
						setQueryFacets(_facetQuery, _facetStrings);
						_facetCombo.setItems(_facetStrings);
					}
					catch (Exception e1)
					{
						e1.printStackTrace();
					}
				}
				else if (_facetQuery.getKey().equals("subtype")) //$NON-NLS-1$
				{
					try
					{
						_facetStrings = (_mainSearcher.getFacets(
								"tagging_values", _facetElement, _facetType, null, null)); //$NON-NLS-1$
					}
					catch (Exception e1)
					{
						e1.printStackTrace();
					}
					_facetQuery.setFacets((HashMap<String, IAEPresentable>) _facetSubtypeComboViewer.getInput());

				}
			}
		});

		facetSubtypeCombo.setLayoutData(new GridData());
		facetSubtypeCombo.setLayoutData(new GridData());
		((GridData) facetSubtypeCombo.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) facetSubtypeCombo.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) facetSubtypeCombo.getLayoutData()).horizontalSpan = 2;
		_facetSubtypeComboViewer.setContentProvider(new MarkupContentProvider());
		_facetSubtypeComboViewer.setLabelProvider(new MarkupLabelProvider());
		_facetSubtypeComboViewer.setComparator(new ConfigDataComparator());

		_facetSubtypeComboViewer.addSelectionChangedListener(new ISelectionChangedListener()
		{
			@Override
			@SuppressWarnings("unchecked")
			public void selectionChanged(final SelectionChangedEvent event)
			{
				ISelection iSelection = event.getSelection();
				Object obj = ((IStructuredSelection) iSelection).getFirstElement();
				ConfigData cd = (ConfigData) obj;
				_aspectFacetSubtype = cd.getValue();
				facetRoleCombo.removeAll();
				_facetQuery.getCriterias().get(0).setCrit4(null);
				_facetCombo.removeAll();
				_facetQuery.getCriterias().get(0).setCrit3(_facetSubtype);
				setComboViewerInput(_facetRoleComboViewer, "tagging_values", _facetElement, _facetType, _facetSubtype); //$NON-NLS-1$

				if (_facetQuery.getKey().equals("content")) //$NON-NLS-1$
				{
					try
					{
						_facetStrings = (_mainSearcher.getFacets("tagging", _facetElement, _facetType, _facetSubtype, //$NON-NLS-1$
								_facetRole));
						setQueryFacets(_facetQuery, _facetStrings);
						_facetCombo.setItems(_facetStrings);
					}
					catch (Exception e1)
					{
						e1.printStackTrace();
					}
				}
				else if (_facetQuery.getKey().equals("role")) //$NON-NLS-1$
				{
					try
					{
						_facetStrings = (_mainSearcher.getFacets(
								"tagging_values", _facetElement, _facetType, _facetSubtype, null)); //$NON-NLS-1$
					}
					catch (Exception e1)
					{
						e1.printStackTrace();
					}
					_facetQuery.setFacets((HashMap<String, IAEPresentable>) _facetRoleComboViewer.getInput());

				}

			}
		});

		facetRoleCombo.setLayoutData(new GridData());
		facetRoleCombo.setLayoutData(new GridData());
		((GridData) facetRoleCombo.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) facetRoleCombo.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) facetRoleCombo.getLayoutData()).horizontalSpan = 2;
		_facetRoleComboViewer.setContentProvider(new MarkupContentProvider());
		_facetRoleComboViewer.setLabelProvider(new MarkupLabelProvider());
		_facetRoleComboViewer.setComparator(new ConfigDataComparator());

		_facetRoleComboViewer.addSelectionChangedListener(new ISelectionChangedListener()
		{
			@Override
			public void selectionChanged(final SelectionChangedEvent event)
			{
				ISelection iSelection = event.getSelection();
				Object obj = ((IStructuredSelection) iSelection).getFirstElement();
				ConfigData cd = (ConfigData) obj;
				_facetRole = cd.getValue();
				_facetCombo.removeAll();
				_facetQuery.getCriterias().get(0).setCrit4(_facetRole);
				if (_facetQuery.getKey().equals("content")) //$NON-NLS-1$
				{
					try
					{
						_facetStrings = (_mainSearcher.getFacets("tagging", _facetElement, _facetType, _facetSubtype, //$NON-NLS-1$
								_facetRole));
						setQueryFacets(_facetQuery, _facetStrings);
						_facetCombo.setItems(_facetStrings);
					}
					catch (Exception e1)
					{
						e1.printStackTrace();
					}
				}

			}
		});

		_facetCombo.setLayoutData(new GridData());
		_facetCombo.setLayoutData(new GridData());
		((GridData) _facetCombo.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _facetCombo.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) _facetCombo.getLayoutData()).horizontalSpan = 2;

		// facetCombo.addFocusListener(new FocusAdapter(){
		// public void focusGained(FocusEvent e)
		// {
		// try {
		//					facetQuery.setFacets(mainSearcher.getFacets( "tagging", facetElement, facetType, facetSubtype, //$NON-NLS-1$
		// facetRole));
		// facetCombo.setItems(facetQuery.getFacets());
		//
		// } catch (Exception e1) {
		// e1.printStackTrace();
		// }
		// }
		// }
		// );

		for (int i = 1; i < _facetQuery.getCriterias().size(); i++)
		{
			//			System.out.println("for i = " + i); //$NON-NLS-1$
			final Criteria c = _facetQuery.getCriterias().get(i);

			if (c.getType().equals("tagging")) //$NON-NLS-1$
			{
				if (tag1)
				{
					tag1 = false;
					_searchPTagComp = new Composite(_facetPersonSearchGroup, SWT.NONE);
					_searchPTagComp.setLayout(new GridLayout());
					((GridLayout) _searchPTagComp.getLayout()).makeColumnsEqualWidth = true;
					((GridLayout) _searchPTagComp.getLayout()).numColumns = 14;
					_searchPTagComp.setLayoutData(new GridData());
					((GridData) _searchPTagComp.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) _searchPTagComp.getLayoutData()).grabExcessHorizontalSpace = true;
					// ((GridData) searchPTagComp.getLayoutData()).heightHint =
					// 200;
					((GridData) _searchPTagComp.getLayoutData()).grabExcessVerticalSpace = false;
					((GridData) _searchPTagComp.getLayoutData()).horizontalSpan = 1;

					Label op = new Label(_searchPTagComp, SWT.NONE);
					op.setText(NLMessages.getString("Dialog_operand")); //$NON-NLS-1$
					op.setLayoutData(new GridData());
					((GridData) op.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) op.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) op.getLayoutData()).horizontalSpan = 1;

					Label sem = new Label(_searchPTagComp, SWT.NONE);
					sem.setText(NLMessages.getString("Dialog_semantic")); //$NON-NLS-1$
					sem.setLayoutData(new GridData());
					((GridData) sem.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) sem.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) sem.getLayoutData()).horizontalSpan = 2;

					Label tagName = new Label(_searchPTagComp, SWT.NONE);
					tagName.setText(NLMessages.getString("Dialog_markup")); //$NON-NLS-1$
					tagName.setLayoutData(new GridData());
					((GridData) tagName.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) tagName.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) tagName.getLayoutData()).horizontalSpan = 2;

					Label tagType = new Label(_searchPTagComp, SWT.NONE);
					tagType.setText(NLMessages.getString("Dialog_type")); //$NON-NLS-1$
					tagType.setLayoutData(new GridData());
					((GridData) tagType.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) tagType.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) tagType.getLayoutData()).horizontalSpan = 2;

					Label tagSubtype = new Label(_searchPTagComp, SWT.NONE);
					tagSubtype.setText(NLMessages.getString("Dialog_subtype")); //$NON-NLS-1$
					tagSubtype.setLayoutData(new GridData());
					((GridData) tagSubtype.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) tagSubtype.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) tagSubtype.getLayoutData()).horizontalSpan = 2;

					Label searchTextLabel = new Label(_searchPTagComp, SWT.NONE);
					searchTextLabel.setText(NLMessages.getString("Dialog_searchText")); //$NON-NLS-1$
					searchTextLabel.setLayoutData(new GridData());
					((GridData) searchTextLabel.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) searchTextLabel.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) searchTextLabel.getLayoutData()).horizontalSpan = 3;

					Label fuzzy = new Label(_searchPTagComp, SWT.NONE);
					fuzzy.setText(NLMessages.getString("Dialog_fuzzy")); //$NON-NLS-1$
					fuzzy.setLayoutData(new GridData());
					((GridData) fuzzy.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) fuzzy.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) fuzzy.getLayoutData()).horizontalSpan = 1;

					Label include = new Label(_searchPTagComp, SWT.NONE);
					include.setText(NLMessages.getString("Dialog_include")); //$NON-NLS-1$
					include.setToolTipText(NLMessages.getString("Dialog_includeConcurrences")); //$NON-NLS-1$
					include.setLayoutData(new GridData());
					((GridData) include.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) include.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) include.getLayoutData()).horizontalSpan = 1;

				}

				if (i == 0)
				{
					Label l = new Label(_searchPTagComp, SWT.NONE);
					l.setText(NLMessages.getString("Dialog_markup")); //$NON-NLS-1$
					l.setLayoutData(new GridData());
					((GridData) l.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) l.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) l.getLayoutData()).horizontalSpan = 1;
				}
				else
				{
					final Combo opCombo = new Combo(_searchPTagComp, SWT.READ_ONLY);
					opCombo.setLayoutData(new GridData());
					opCombo.add(Operator.AND.toString());
					opCombo.add(Operator.OR.toString());
					opCombo.add(Operator.NOT.toString());
					opCombo.setLayoutData(new GridData());
					((GridData) opCombo.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) opCombo.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) opCombo.getLayoutData()).horizontalSpan = 1;
					if (c.getOperator() != null)
					{
						opCombo.setText(c.getOperator());
					}
					else
					{
						opCombo.select(0);
						c.setOperator(opCombo.getItem(0));

					}
					opCombo.addSelectionListener(new SelectionAdapter()
					{
						@Override
						public void widgetSelected(final SelectionEvent se)
						{
							c.setOperator(opCombo.getItem(opCombo.getSelectionIndex()));
						}
					});
				}

				final Combo semCombo = new Combo(_searchPTagComp, SWT.READ_ONLY);
				semCombo.setLayoutData(new GridData());
				ComboViewer comboSemanticViewer = new ComboViewer(semCombo);
				comboSemanticViewer.setContentProvider(new AEConfigPresentableContentProvider());
				comboSemanticViewer.setLabelProvider(new AEConfigPresentableLabelProvider());
				((AEConfigPresentableContentProvider) comboSemanticViewer.getContentProvider()).setAddALL(true);

				if (_facade.getAllSemantics() != null && !_facade.getAllSemantics().isEmpty())
				{
					comboSemanticViewer.setInput(_facade.getAllSemantics());
					if (c.getCrit0() != null)
					{
						setComboViewerByString(comboSemanticViewer, c.getCrit0());
					}
					else
					{
						semCombo.select(0);
						c.setCrit0(semCombo.getItem(0));
					}
				}
				semCombo.setLayoutData(new GridData());
				((GridData) semCombo.getLayoutData()).horizontalAlignment = SWT.FILL;
				((GridData) semCombo.getLayoutData()).grabExcessHorizontalSpace = true;
				((GridData) semCombo.getLayoutData()).horizontalSpan = 2;

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
							c.setCrit0(cp.getValue());
						}
					}
				});

				final Combo tagCombo = new Combo(_searchPTagComp, SWT.READ_ONLY);
				tagCombo.setLayoutData(new GridData());
				tagCombo.setLayoutData(new GridData());
				((GridData) tagCombo.getLayoutData()).horizontalAlignment = SWT.FILL;
				((GridData) tagCombo.getLayoutData()).grabExcessHorizontalSpace = true;
				((GridData) tagCombo.getLayoutData()).horizontalSpan = 2;
				final ComboViewer tagComboViewer = new ComboViewer(tagCombo);
				tagComboViewer.setContentProvider(new MarkupContentProvider(true));
				tagComboViewer.setLabelProvider(new MarkupLabelProvider());
				tagComboViewer.setComparator(new ConfigDataComparator());
				if (_facade.getConfigs().containsKey(_markupProvider))
				{
					HashMap<String, ConfigData> input = _facade.getConfigs().get(_markupProvider).getChildren();
					facetElementComboViewer.setInput(input);
				}
				if (c.getCrit1() != null)
				{
					setComboViewerByString(tagComboViewer, c.getCrit1());
				}
				else
				{
					obj = facetElementComboViewer.getElementAt(0);
					if (obj != null)
					{
						facetElementComboViewer.setSelection(new StructuredSelection(obj));
						ConfigData cd = (ConfigData) facetElementComboViewer.getElementAt(0);
						if (cd != null && cd.getValue().startsWith("aodl:"))
						{
							c.setCrit1(cd.getValue().substring(5)); //$NON-NLS-1$
						}
						else if (cd != null)
						{
							c.setCrit1(cd.getValue());
						}

					}

				}
				final Combo typeCombo = new Combo(_searchPTagComp, SWT.READ_ONLY);
				typeCombo.setLayoutData(new GridData());
				typeCombo.setLayoutData(new GridData());
				((GridData) typeCombo.getLayoutData()).horizontalAlignment = SWT.FILL;
				((GridData) typeCombo.getLayoutData()).grabExcessHorizontalSpace = true;
				((GridData) typeCombo.getLayoutData()).horizontalSpan = 2;
				final ComboViewer typeComboViewer = new ComboViewer(typeCombo);
				typeComboViewer.setContentProvider(new MarkupContentProvider());
				typeComboViewer.setLabelProvider(new MarkupLabelProvider());
				typeComboViewer.setComparator(new ConfigDataComparator());

				tagComboViewer.addSelectionChangedListener(new ISelectionChangedListener()
				{
					@Override
					public void selectionChanged(final SelectionChangedEvent event)
					{
						ISelection iSelection = event.getSelection();
						Object obj = ((IStructuredSelection) iSelection).getFirstElement();
						ConfigData cd = (ConfigData) obj;
						String selection;
						if (cd.getValue().startsWith("aodl:"))
						{
							selection = cd.getValue().substring(5); //$NON-NLS-1$
						}
						else
						{
							selection = cd.getValue();
						}
						c.setCrit1(selection);
						setComboViewerInput(typeComboViewer, "tagging_values", c.getCrit1(), null, null); //$NON-NLS-1$
					}
				});

				if (c.getCrit2() != null)
				{
					setComboViewerByString(typeComboViewer, c.getCrit2());
				}

				final Combo subtypeCombo = new Combo(_searchPTagComp, SWT.READ_ONLY);
				final ComboViewer subtypeComboViewer = new ComboViewer(subtypeCombo);
				subtypeComboViewer.setContentProvider(new MarkupContentProvider());
				subtypeComboViewer.setLabelProvider(new MarkupLabelProvider());
				subtypeComboViewer.setComparator(new ConfigDataComparator());

				// typeCombo.addFocusListener(new FocusAdapter(){
				// public void focusGained(FocusEvent e)
				// {
				// }
				// }
				// );
				typeComboViewer.addSelectionChangedListener(new ISelectionChangedListener()
				{
					@Override
					public void selectionChanged(final SelectionChangedEvent event)
					{
						ISelection iSelection = event.getSelection();
						Object obj = ((IStructuredSelection) iSelection).getFirstElement();
						ConfigData cd = (ConfigData) obj;
						subtypeComboViewer.setInput(null);
						if (cd != null)
						{
							c.setCrit2(cd.getValue());
						}
						setComboViewerInput(subtypeComboViewer, "tagging_values", c.getCrit1(), c.getCrit2(), null); //$NON-NLS-1$
					}
				});

				subtypeCombo.setLayoutData(new GridData());
				subtypeCombo.setLayoutData(new GridData());
				((GridData) subtypeCombo.getLayoutData()).horizontalAlignment = SWT.FILL;
				((GridData) subtypeCombo.getLayoutData()).grabExcessHorizontalSpace = true;
				((GridData) subtypeCombo.getLayoutData()).horizontalSpan = 2;
				if (c.getCrit3() != null)
				{
					setComboViewerByString(subtypeComboViewer, c.getCrit3());
				}
				// subtypeCombo.addFocusListener(new FocusAdapter(){
				// public void focusGained(FocusEvent e)
				// {
				//
				// }
				// }
				// );
				subtypeComboViewer.addSelectionChangedListener(new ISelectionChangedListener()
				{
					@Override
					public void selectionChanged(final SelectionChangedEvent event)
					{
						ISelection iSelection = event.getSelection();
						Object obj = ((IStructuredSelection) iSelection).getFirstElement();
						ConfigData cd = (ConfigData) obj;
						if (cd != null)
						{
							c.setCrit3(cd.getValue());
						}
					}
				});

				final Text searchText = new Text(_searchPTagComp, SWT.BORDER);
				searchText.setLayoutData(new GridData());
				searchText.setLayoutData(new GridData());
				((GridData) searchText.getLayoutData()).horizontalAlignment = SWT.FILL;
				((GridData) searchText.getLayoutData()).grabExcessHorizontalSpace = true;
				((GridData) searchText.getLayoutData()).horizontalSpan = 3;
				if (c.getSearchText() != null)
				{
					searchText.setText(c.getSearchText());
				}
				searchText.addFocusListener(new FocusAdapter()
				{
					@Override
					public void focusLost(final FocusEvent e)
					{
						c.setSearchText(searchText.getText());
					}
				});
				searchText.addKeyListener(new KeyListener()
				{
					@Override
					public void keyPressed(final KeyEvent e)
					{
						if (e.keyCode == SWT.CR)
						{
							c.setSearchText(searchText.getText());
							okPressed();
						}
					}

					@Override
					public void keyReleased(final KeyEvent e)
					{
					}
				});
				final Button fuzzyB = new Button(_searchPTagComp, SWT.CHECK);
				fuzzyB.setLayoutData(new GridData());
				fuzzyB.setSelection(c.isFuzzy());
				fuzzyB.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent event)
					{
						c.setFuzzy(!c.isFuzzy());
					}
				});

				final Button includeB = new Button(_searchPTagComp, SWT.CHECK);
				includeB.setLayoutData(new GridData());
				includeB.setSelection(c.isIncludeConcurrences());
				includeB.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent event)
					{
						c.setIncludeConcurrences(!c.isIncludeConcurrences());
					}
				});

			} // if tagging

			if (c.getType().equals("date")) //$NON-NLS-1$
			{
				if (date1)
				{
					date1 = false;
					_searchPDateComp = new Composite(_facetPersonSearchGroup, SWT.NONE);
					_searchPDateComp.setLayout(new GridLayout());
					((GridLayout) _searchPDateComp.getLayout()).makeColumnsEqualWidth = true;
					((GridLayout) _searchPDateComp.getLayout()).numColumns = 14;
					_searchPDateComp.setLayoutData(new GridData());
					((GridData) _searchPDateComp.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) _searchPDateComp.getLayoutData()).grabExcessHorizontalSpace = true;
					// ((GridData) searchPDateComp.getLayoutData()).heightHint =
					// 200;
					((GridData) _searchPDateComp.getLayoutData()).grabExcessVerticalSpace = false;
					((GridData) _searchPDateComp.getLayoutData()).horizontalSpan = 1;

					Label l2 = new Label(_searchPDateComp, SWT.NONE);
					l2.setText(NLMessages.getString("Dialog_date")); //$NON-NLS-1$
					l2.setLayoutData(new GridData());
					((GridData) l2.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) l2.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) l2.getLayoutData()).horizontalSpan = 1;

					Label typeDate = new Label(_searchPDateComp, SWT.NONE);
					typeDate.setText(NLMessages.getString("Dialog_type")); //$NON-NLS-1$
					typeDate.setLayoutData(new GridData());
					((GridData) typeDate.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) typeDate.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) typeDate.getLayoutData()).horizontalSpan = 2;

					Label bl = new Label(_searchPDateComp, SWT.NONE);
					bl.setText(""); //$NON-NLS-1$
					Label notBefore = new Label(_searchPDateComp, SWT.NONE);
					notBefore.setText(NLMessages.getString("Dialog_day")); //$NON-NLS-1$
					notBefore.setLayoutData(new GridData());
					((GridData) notBefore.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) notBefore.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) notBefore.getLayoutData()).horizontalSpan = 1;

					Label month = new Label(_searchPDateComp, SWT.NONE);
					month.setText(NLMessages.getString("Dialog_month")); //$NON-NLS-1$
					month.setLayoutData(new GridData());
					((GridData) month.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) month.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) month.getLayoutData()).horizontalSpan = 1;

					Label year = new Label(_searchPDateComp, SWT.NONE);
					year.setText(NLMessages.getString("Dialog_year")); //$NON-NLS-1$
					year.setLayoutData(new GridData());
					((GridData) year.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) year.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) year.getLayoutData()).horizontalSpan = 2;

					Label bl2 = new Label(_searchPDateComp, SWT.NONE);
					bl2.setText(""); //$NON-NLS-1$

					Label notAfter = new Label(_searchPDateComp, SWT.NONE);
					notAfter.setText(NLMessages.getString("Dialog_day")); //$NON-NLS-1$
					notAfter.setLayoutData(new GridData());
					((GridData) notAfter.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) notAfter.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) notAfter.getLayoutData()).horizontalSpan = 1;

					Label month2 = new Label(_searchPDateComp, SWT.NONE);
					month2.setText(NLMessages.getString("Dialog_month")); //$NON-NLS-1$
					month2.setLayoutData(new GridData());
					((GridData) month2.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) month2.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) month2.getLayoutData()).horizontalSpan = 1;

					Label year2 = new Label(_searchPDateComp, SWT.NONE);
					year2.setText(NLMessages.getString("Dialog_year")); //$NON-NLS-1$
					year2.setLayoutData(new GridData());
					((GridData) year2.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) year2.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) year2.getLayoutData()).horizontalSpan = 2;

					Label include = new Label(_searchPDateComp, SWT.NONE);
					include.setText(""); //$NON-NLS-1$
					include.setToolTipText(NLMessages.getString("Dialog_includeConcurrences")); //$NON-NLS-1$
					include.setLayoutData(new GridData());
					((GridData) include.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) include.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) include.getLayoutData()).horizontalSpan = 1;

				}

				if (i == 0)
				{
					Label l = new Label(_searchPDateComp, SWT.NONE);
					l.setLayoutData(new GridData());
					((GridData) l.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) l.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) l.getLayoutData()).horizontalSpan = 1;
				}
				else
				{
					final Combo opCombo = new Combo(_searchPDateComp, SWT.READ_ONLY);
					opCombo.setLayoutData(new GridData());
					opCombo.add(Operator.AND.toString());
					opCombo.add(Operator.OR.toString());
					opCombo.add(Operator.NOT.toString());
					opCombo.setLayoutData(new GridData());
					((GridData) opCombo.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) opCombo.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) opCombo.getLayoutData()).horizontalSpan = 1;
					if (c.getOperator() != null)
					{
						opCombo.setText(c.getOperator());
					}
					else
					{
						opCombo.select(0);
						c.setOperator(opCombo.getItem(0));

					}
					opCombo.addSelectionListener(new SelectionAdapter()
					{
						@Override
						public void widgetSelected(final SelectionEvent se)
						{
							c.setOperator(opCombo.getItem(opCombo.getSelectionIndex()));
						}
					});
				}

				final Combo typeDCombo = new Combo(_searchPDateComp, SWT.READ_ONLY);
				typeDCombo.setLayoutData(new GridData());
				ComboViewer timeTypeComboViewer = new ComboViewer(typeDCombo);
				timeTypeComboViewer.setContentProvider(ArrayContentProvider.getInstance());
				timeTypeComboViewer.setLabelProvider(new LabelProvider()
				{

					@Override
					public String getText(final Object element)
					{
						String str = (String) element;
						if (NLMessages.getString("Editor_time_" + str) != null) //$NON-NLS-1$
						{
							return NLMessages.getString("Editor_time_" + str); //$NON-NLS-1$
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
						c.setDateType(s);
					}

				});
				if (c.getDateType() != null)
				{
					StructuredSelection selection = new StructuredSelection(c.getDateType());
					timeTypeComboViewer.setSelection(selection);
				}
				else
				{
					StructuredSelection selection = new StructuredSelection(AEConstants.TIME_TYPES[0]);
					timeTypeComboViewer.setSelection(selection);
					c.setDateType(AEConstants.TIME_TYPES[0]);
				}
				typeDCombo.pack();
				typeDCombo.setLayoutData(new GridData());
				((GridData) typeDCombo.getLayoutData()).horizontalAlignment = SWT.FILL;
				((GridData) typeDCombo.getLayoutData()).grabExcessHorizontalSpace = true;
				((GridData) typeDCombo.getLayoutData()).horizontalSpan = 2;

				Label from = new Label(_searchPDateComp, SWT.NONE);
				from.setText(NLMessages.getString("Dialog_from")); //$NON-NLS-1$
				from.setLayoutData(new GridData());
				((GridData) from.getLayoutData()).horizontalAlignment = SWT.FILL;
				((GridData) from.getLayoutData()).grabExcessHorizontalSpace = true;
				((GridData) from.getLayoutData()).horizontalSpan = 1;

				final Combo day1Combo = new Combo(_searchPDateComp, SWT.READ_ONLY);
				day1Combo.setLayoutData(new GridData());
				day1Combo.setItems(AEConstants.DAYS);
				day1Combo.setLayoutData(new GridData());
				((GridData) day1Combo.getLayoutData()).horizontalAlignment = SWT.FILL;
				((GridData) day1Combo.getLayoutData()).grabExcessHorizontalSpace = true;
				((GridData) day1Combo.getLayoutData()).horizontalSpan = 1;

				if (c.getDateFrom() == null)
				{
					PdrDate dateFrom = new PdrDate("0000-00-00"); //$NON-NLS-1$
					// dateFrom.setDay(0);
					// dateFrom.setMonth(0);
					// dateFrom.setYear(0);
					c.setDateFrom(dateFrom);
					day1Combo.select(c.getDateFrom().getDay());
				}
				//				System.out.println("test: dateFrom " + c.getDateFrom().toString()); //$NON-NLS-1$
				// else
				// {
				//
				// day1Combo.select(0);
				// c.getDateFrom().setDay(0);
				//
				// }

				day1Combo.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent se)
					{
						c.getDateFrom().setDay(day1Combo.getSelectionIndex());
					}
				});

				final Combo month1Combo = new Combo(_searchPDateComp, SWT.READ_ONLY);
				month1Combo.setLayoutData(new GridData());
				month1Combo.setItems(AEConstants.MONTHS);
				month1Combo.setLayoutData(new GridData());
				((GridData) month1Combo.getLayoutData()).horizontalAlignment = SWT.FILL;
				((GridData) month1Combo.getLayoutData()).grabExcessHorizontalSpace = true;
				((GridData) month1Combo.getLayoutData()).horizontalSpan = 1;
				if (c.getDateFrom() != null)
				{
					month1Combo.select(c.getDateFrom().getMonth());
				}
				else
				{

					month1Combo.select(0);
					c.getDateFrom().setMonth(0);

				}

				month1Combo.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent se)
					{
						c.getDateFrom().setMonth(month1Combo.getSelectionIndex());
					}
				});

				final YearSpinner year1Spinner = new YearSpinner(_searchPDateComp, SWT.NULL);
				// year1Spinner.setLayoutData(new GridData());
				// year1Spinner.setLayoutData(new GridData());
				// ((GridData) year1Spinner.getLayoutData()).horizontalAlignment
				// = SWT.FILL;
				// ((GridData)
				// year1Spinner.getLayoutData()).grabExcessHorizontalSpace =
				// true;
				// ((GridData) year1Spinner.getLayoutData()).horizontalSpan = 2;
				// year1Spinner.setMinimum(0);
				// year1Spinner.setMaximum(9999);
				if (c.getDateFrom() != null)
				{
					year1Spinner.setSelection(c.getDateFrom().getYear());
				}
				else
				{
					year1Spinner.setSelection(_preselection);
					c.getDateFrom().setYear(_preselection);

				}

				year1Spinner.addSelectionListener(new SelectionListener()
				{

					@Override
					public void widgetDefaultSelected(final SelectionEvent e)
					{
						c.getDateFrom().setYear(year1Spinner.getSelection());
						// System.out.println("year1Spinner.getSelection() " +
						// year1Spinner.getSelection());

					}

					@Override
					public void widgetSelected(final SelectionEvent e)
					{
						c.getDateFrom().setYear(year1Spinner.getSelection());
						// System.out.println("year1Spinner.getSelection() " +
						// year1Spinner.getSelection());
					}

				});

				Label to = new Label(_searchPDateComp, SWT.NONE);
				to.setText(NLMessages.getString("Dialog_to")); //$NON-NLS-1$
				to.setLayoutData(new GridData());
				((GridData) to.getLayoutData()).horizontalAlignment = SWT.FILL;
				((GridData) to.getLayoutData()).grabExcessHorizontalSpace = true;
				((GridData) to.getLayoutData()).horizontalSpan = 1;

				final Combo day2Combo = new Combo(_searchPDateComp, SWT.READ_ONLY);
				day2Combo.setLayoutData(new GridData());
				day2Combo.setItems(AEConstants.DAYS);
				day2Combo.setLayoutData(new GridData());
				((GridData) day2Combo.getLayoutData()).horizontalAlignment = SWT.FILL;
				((GridData) day2Combo.getLayoutData()).grabExcessHorizontalSpace = true;
				((GridData) day2Combo.getLayoutData()).horizontalSpan = 1;
				if (c.getDateTo() == null)
				{
					PdrDate dateTo = new PdrDate("0000-00-00"); //$NON-NLS-1$
					c.setDateTo(dateTo);
					day2Combo.select(c.getDateTo().getDay());
				}

				day2Combo.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent se)
					{
						c.getDateTo().setDay(day2Combo.getSelectionIndex());
					}
				});

				final Combo month2Combo = new Combo(_searchPDateComp, SWT.READ_ONLY);
				month2Combo.setLayoutData(new GridData());
				month2Combo.setItems(AEConstants.MONTHS);
				month2Combo.setLayoutData(new GridData());
				((GridData) month2Combo.getLayoutData()).horizontalAlignment = SWT.FILL;
				((GridData) month2Combo.getLayoutData()).grabExcessHorizontalSpace = true;
				((GridData) month2Combo.getLayoutData()).horizontalSpan = 1;
				if (c.getDateTo() != null)
				{
					month2Combo.select(c.getDateTo().getMonth());
				}
				else
				{

					month2Combo.select(0);
					c.getDateTo().setMonth(0);

				}

				month2Combo.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent se)
					{
						c.getDateTo().setMonth(month2Combo.getSelectionIndex());
					}
				});

				final YearSpinner year2Spinner = new YearSpinner(_searchPDateComp, SWT.NULL);
				// year2Spinner.setLayoutData(new GridData());
				// year2Spinner.setLayoutData(new GridData());
				// ((GridData) year2Spinner.getLayoutData()).horizontalAlignment
				// = SWT.FILL;
				// ((GridData)
				// year2Spinner.getLayoutData()).grabExcessHorizontalSpace =
				// true;
				// ((GridData) year2Spinner.getLayoutData()).horizontalSpan = 2;
				// year2Spinner.setMinimum(0);
				// year2Spinner.setMaximum(9999);
				if (c.getDateTo() != null)
				{
					year2Spinner.setSelection(c.getDateTo().getYear());
				}
				else
				{

					year2Spinner.setSelection(_preselection);
					c.getDateTo().setYear(_preselection);

				}

				year2Spinner.addSelectionListener(new SelectionListener()
				{

					@Override
					public void widgetDefaultSelected(final SelectionEvent e)
					{
						c.getDateTo().setYear(year2Spinner.getSelection());

					}

					@Override
					public void widgetSelected(final SelectionEvent e)
					{
						c.getDateTo().setYear(year2Spinner.getSelection());
					}

				});

				final Button includeB = new Button(_searchPDateComp, SWT.CHECK);
				includeB.setLayoutData(new GridData());
				includeB.setSelection(c.isIncludeConcurrences());
				includeB.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent event)
					{
						c.setIncludeConcurrences(!c.isIncludeConcurrences());
					}
				});

			} // if date

			if (c.getType().equals("reference")) //$NON-NLS-1$
			{

				_searchPRefComp = new Composite(_facetPersonSearchGroup, SWT.NONE);
				_searchPRefComp.setLayout(new GridLayout());
				((GridLayout) _searchPRefComp.getLayout()).makeColumnsEqualWidth = true;
				((GridLayout) _searchPRefComp.getLayout()).numColumns = 14;
				_searchPRefComp.setLayoutData(new GridData());
				((GridData) _searchPRefComp.getLayoutData()).horizontalAlignment = SWT.FILL;
				((GridData) _searchPRefComp.getLayoutData()).grabExcessHorizontalSpace = true;
				// ((GridData) searchPTagComp.getLayoutData()).heightHint = 200;
				((GridData) _searchPRefComp.getLayoutData()).grabExcessVerticalSpace = false;
				((GridData) _searchPRefComp.getLayoutData()).horizontalSpan = 1;

				final Combo opCombo = new Combo(_searchPRefComp, SWT.READ_ONLY);
				opCombo.setLayoutData(new GridData());
				opCombo.add(Operator.AND.toString());
				opCombo.add(Operator.OR.toString());
				opCombo.add(Operator.NOT.toString());
				opCombo.setLayoutData(new GridData());
				((GridData) opCombo.getLayoutData()).horizontalAlignment = SWT.FILL;
				((GridData) opCombo.getLayoutData()).grabExcessHorizontalSpace = true;
				((GridData) opCombo.getLayoutData()).horizontalSpan = 1;
				if (c.getOperator() != null)
				{
					opCombo.setText(c.getOperator());
				}
				else
				{
					opCombo.select(0);
					c.setOperator(opCombo.getItem(0));

				}
				opCombo.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent se)
					{
						c.setOperator(opCombo.getItem(opCombo.getSelectionIndex()));
					}
				});

				Label sem = new Label(_searchPRefComp, SWT.NONE);
				sem.setText(NLMessages.getString("Dialog_reference")); //$NON-NLS-1$
				sem.setLayoutData(new GridData());
				((GridData) sem.getLayoutData()).horizontalAlignment = SWT.FILL;
				((GridData) sem.getLayoutData()).grabExcessHorizontalSpace = true;
				((GridData) sem.getLayoutData()).horizontalSpan = 2;

				Label tagName = new Label(_searchPRefComp, SWT.NONE);
				tagName.setText(NLMessages.getString("Dialog_genre")); //$NON-NLS-1$
				tagName.setLayoutData(new GridData());
				((GridData) tagName.getLayoutData()).horizontalAlignment = SWT.FILL;
				((GridData) tagName.getLayoutData()).grabExcessHorizontalSpace = true;
				((GridData) tagName.getLayoutData()).horizontalSpan = 2;

				final Combo genreCombo = new Combo(_searchPRefComp, SWT.READ_ONLY);
				genreCombo.setLayoutData(new GridData());
				ComboViewer genreComboViewer = new ComboViewer(genreCombo);
				genreComboViewer.setContentProvider(new RefTemplateContentProvider(false));
				genreComboViewer.setLabelProvider(new LabelProvider()
				{

					@Override
					public String getText(final Object element)
					{
						ReferenceModsTemplate template = (ReferenceModsTemplate) element;
						return template.getLabel();
					}

				});

				genreComboViewer.setInput(_facade.getAllGenres());
				genreComboViewer.addSelectionChangedListener(new ISelectionChangedListener()
				{

					@Override
					public void selectionChanged(final SelectionChangedEvent event)
					{
						ISelection selection = event.getSelection();
						Object obj = ((IStructuredSelection) selection).getFirstElement();
						ReferenceModsTemplate template = (ReferenceModsTemplate) obj;
						if (template != null)
						{
							c.setCrit0(template.getValue());
						}
					}

				});
				genreCombo.add("ALL", 0); //$NON-NLS-1$

				genreCombo.setLayoutData(new GridData());
				((GridData) genreCombo.getLayoutData()).horizontalAlignment = SWT.FILL;
				((GridData) genreCombo.getLayoutData()).grabExcessHorizontalSpace = true;
				((GridData) genreCombo.getLayoutData()).horizontalSpan = 2;
				if (c.getCrit0() != null)
				{
					StructuredSelection selection = new StructuredSelection(c.getCrit0());
					genreComboViewer.setSelection(selection);
				}
				else
				{
					genreCombo.select(0);
					c.setCrit0(genreCombo.getItem(0));

				}

				Label tagType = new Label(_searchPRefComp, SWT.NONE);
				tagType.setText(NLMessages.getString("Dialog_searchText")); //$NON-NLS-1$
				tagType.setLayoutData(new GridData());
				((GridData) tagType.getLayoutData()).horizontalAlignment = SWT.FILL;
				((GridData) tagType.getLayoutData()).grabExcessHorizontalSpace = true;
				((GridData) tagType.getLayoutData()).horizontalSpan = 2;

				final Text searchText = new Text(_searchPRefComp, SWT.BORDER);
				searchText.setLayoutData(new GridData());
				searchText.setLayoutData(new GridData());
				((GridData) searchText.getLayoutData()).horizontalAlignment = SWT.FILL;
				((GridData) searchText.getLayoutData()).grabExcessHorizontalSpace = true;
				((GridData) searchText.getLayoutData()).horizontalSpan = 3;
				if (c.getSearchText() != null)
				{
					searchText.setText(c.getSearchText());
				}
				searchText.addFocusListener(new FocusAdapter()
				{
					@Override
					public void focusLost(final FocusEvent e)
					{
						c.setSearchText(searchText.getText());
					}
				});

				searchText.addKeyListener(new KeyListener()
				{
					@Override
					public void keyPressed(final KeyEvent e)
					{
						if (e.keyCode == SWT.CR)
						{
							c.setSearchText(searchText.getText());
							okPressed();
						}
					}

					@Override
					public void keyReleased(final KeyEvent e)
					{
					}
				});

				final Button fuzzyB = new Button(_searchPRefComp, SWT.CHECK);
				fuzzyB.setLayoutData(new GridData());
				fuzzyB.setSelection(c.isFuzzy());
				fuzzyB.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent event)
					{
						c.setFuzzy(!c.isFuzzy());
					}
				});

				final Button includeB = new Button(_searchPRefComp, SWT.CHECK);
				includeB.setLayoutData(new GridData());
				includeB.setSelection(c.isIncludeConcurrences());
				includeB.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent event)
					{
						c.setIncludeConcurrences(!c.isIncludeConcurrences());
					}
				});

			} // if reference
		}

		// contentCompSearch.layout();
		// scrollCompSearch.setContent(contentCompSearch);
		// scrollCompSearch.setMinSize(contentCompSearch.computeSize(SWT.DEFAULT,
		// SWT.DEFAULT, true));
		// scrollCompSearch.layout();
		_facetPersonSearchGroup.redraw();
		_facetPersonSearchGroup.layout();
		_facetPersonSearchGroup.pack();
		_facetPersonSearchGroup.layout();
		// facetPersonSearchGroup.pack();

	}

	/**
	 * Builds the person search.
	 * @param type the type
	 * @param crit the crit
	 */
	private void buildPersonSearch(final int type, final Integer crit)
	{

		_markupProvider = Platform
				.getPreferencesService()
				.getString(CommonActivator.PLUGIN_ID,
						"PRIMARY_TAGGING_PROVIDER", AEConstants.CLASSIFICATION_AUTHORITY, null).toUpperCase(); //$NON-NLS-1$
		_relationProvider = Platform
				.getPreferencesService()
				.getString(CommonActivator.PLUGIN_ID, "PRIMARY_RELATION_PROVIDER",
						AEConstants.RELATION_CLASSIFICATION_PROVIDER, null).toUpperCase();
		DataType dtAll = new DataType();
		dtAll.setValue("ALL"); //$NON-NLS-1$
		dtAll.setLabel("ALL"); //$NON-NLS-1$

		boolean tag1 = true;
		boolean rel1 = true;
		boolean date1 = true;
		for (int i = 0; i < _personQuery.getCriterias().size(); i++)
		{
			//			System.out.println("for i = " + i); //$NON-NLS-1$
			final Criteria c = _personQuery.getCriterias().get(i);

			if (c.getType().equals("tagging")) //$NON-NLS-1$
			{
				if (tag1)
				{
					tag1 = false;
					_searchPTagComp = new Composite(_personSearchGroup, SWT.NONE);
					_searchPTagComp.setLayout(new GridLayout());
					((GridLayout) _searchPTagComp.getLayout()).makeColumnsEqualWidth = true;
					((GridLayout) _searchPTagComp.getLayout()).numColumns = 14;
					_searchPTagComp.setLayoutData(new GridData());
					((GridData) _searchPTagComp.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) _searchPTagComp.getLayoutData()).grabExcessHorizontalSpace = true;
					// ((GridData) searchPTagComp.getLayoutData()).heightHint =
					// 200;
					((GridData) _searchPTagComp.getLayoutData()).grabExcessVerticalSpace = false;
					((GridData) _searchPTagComp.getLayoutData()).horizontalSpan = 1;

					Label op = new Label(_searchPTagComp, SWT.NONE);
					op.setText(NLMessages.getString("Dialog_operand")); //$NON-NLS-1$
					op.setLayoutData(new GridData());
					((GridData) op.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) op.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) op.getLayoutData()).horizontalSpan = 1;

					Label sem = new Label(_searchPTagComp, SWT.NONE);
					sem.setText(NLMessages.getString("Dialog_semantic")); //$NON-NLS-1$
					sem.setLayoutData(new GridData());
					((GridData) sem.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) sem.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) sem.getLayoutData()).horizontalSpan = 2;

					Label tagName = new Label(_searchPTagComp, SWT.NONE);
					tagName.setText(NLMessages.getString("Dialog_markup")); //$NON-NLS-1$
					tagName.setLayoutData(new GridData());
					((GridData) tagName.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) tagName.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) tagName.getLayoutData()).horizontalSpan = 2;

					Label tagType = new Label(_searchPTagComp, SWT.NONE);
					tagType.setText(NLMessages.getString("Dialog_type")); //$NON-NLS-1$
					tagType.setLayoutData(new GridData());
					((GridData) tagType.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) tagType.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) tagType.getLayoutData()).horizontalSpan = 2;

					Label tagSubtype = new Label(_searchPTagComp, SWT.NONE);
					tagSubtype.setText(NLMessages.getString("Dialog_subtype")); //$NON-NLS-1$
					tagSubtype.setLayoutData(new GridData());
					((GridData) tagSubtype.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) tagSubtype.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) tagSubtype.getLayoutData()).horizontalSpan = 2;

					Label searchTextLabel = new Label(_searchPTagComp, SWT.NONE);
					searchTextLabel.setText(NLMessages.getString("Dialog_searchText")); //$NON-NLS-1$
					searchTextLabel.setLayoutData(new GridData());
					((GridData) searchTextLabel.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) searchTextLabel.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) searchTextLabel.getLayoutData()).horizontalSpan = 3;

					Label fuzzy = new Label(_searchPTagComp, SWT.NONE);
					fuzzy.setText(NLMessages.getString("Dialog_fuzzy")); //$NON-NLS-1$
					fuzzy.setLayoutData(new GridData());
					((GridData) fuzzy.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) fuzzy.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) fuzzy.getLayoutData()).horizontalSpan = 1;

					Label include = new Label(_searchPTagComp, SWT.NONE);
					include.setText(NLMessages.getString("Dialog_include")); //$NON-NLS-1$
					include.setToolTipText(NLMessages.getString("Dialog_includeConcurrences")); //$NON-NLS-1$
					include.setLayoutData(new GridData());
					((GridData) include.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) include.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) include.getLayoutData()).horizontalSpan = 1;

				}

				if (i == 0)
				{
					Label l = new Label(_searchPTagComp, SWT.NONE);
					l.setText(NLMessages.getString("Dialog_markup")); //$NON-NLS-1$
					l.setLayoutData(new GridData());
					((GridData) l.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) l.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) l.getLayoutData()).horizontalSpan = 1;
				}
				else
				{
					final Combo opCombo = new Combo(_searchPTagComp, SWT.READ_ONLY);
					opCombo.setLayoutData(new GridData());
					opCombo.add(Operator.AND.toString());
					opCombo.add(Operator.OR.toString());
					opCombo.add(Operator.NOT.toString());
					opCombo.setLayoutData(new GridData());
					((GridData) opCombo.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) opCombo.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) opCombo.getLayoutData()).horizontalSpan = 1;
					if (c.getOperator() != null)
					{
						opCombo.select(opCombo.indexOf(c.getOperator()));
					}
					else
					{
						opCombo.select(0);
						c.setOperator(opCombo.getItem(0));

					}
					opCombo.addSelectionListener(new SelectionAdapter()
					{
						@Override
						public void widgetSelected(final SelectionEvent se)
						{
							c.setOperator(opCombo.getItem(opCombo.getSelectionIndex()));
						}
					});
					opCombo.pack();
				}

				final Combo semCombo = new Combo(_searchPTagComp, SWT.READ_ONLY);
				semCombo.setLayoutData(new GridData());
				ComboViewer comboSemanticViewer = new ComboViewer(semCombo);
				comboSemanticViewer.setContentProvider(new AEConfigPresentableContentProvider());
				comboSemanticViewer.setLabelProvider(new AEConfigPresentableLabelProvider());
				((AEConfigPresentableContentProvider) comboSemanticViewer.getContentProvider()).setAddALL(true);

				if (_facade.getAllSemantics() != null && !_facade.getAllSemantics().isEmpty())
				{
					comboSemanticViewer.setInput(_facade.getAllSemantics());
					if (c.getCrit0() != null)
					{
						setComboViewerByString(comboSemanticViewer, c.getCrit0());
					}
					else
					{
						semCombo.select(0);
						c.setCrit0(semCombo.getItem(0));
					}
				}
				semCombo.setLayoutData(new GridData());
				((GridData) semCombo.getLayoutData()).horizontalAlignment = SWT.FILL;
				((GridData) semCombo.getLayoutData()).grabExcessHorizontalSpace = true;
				((GridData) semCombo.getLayoutData()).horizontalSpan = 2;

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
							c.setCrit0(cp.getValue());
						}
					}
				});
				semCombo.pack();

				final Combo tagCombo = new Combo(_searchPTagComp, SWT.READ_ONLY);
				tagCombo.setLayoutData(new GridData());
				tagCombo.setLayoutData(new GridData());
				((GridData) tagCombo.getLayoutData()).horizontalAlignment = SWT.FILL;
				((GridData) tagCombo.getLayoutData()).grabExcessHorizontalSpace = true;
				((GridData) tagCombo.getLayoutData()).horizontalSpan = 2;
				final ComboViewer tagComboViewer = new ComboViewer(tagCombo);
				tagComboViewer.setContentProvider(new MarkupContentProvider(true));
				tagComboViewer.setLabelProvider(new MarkupLabelProvider());
				tagComboViewer.setComparator(new ConfigDataComparator());
				if (_facade.getConfigs().containsKey(_markupProvider))
				{
					HashMap<String, ConfigData> input = _facade.getConfigs().get(_markupProvider).getChildren();
					tagComboViewer.setInput(input);
				}
				if (c.getCrit1() != null)
				{
					setComboViewerByString(tagComboViewer, c.getCrit1());
				}
				else
				{
					Object obj = tagComboViewer.getElementAt(0);
					if (obj != null)
					{
						tagComboViewer.setSelection(new StructuredSelection(obj));
						ConfigData cd = (ConfigData) tagComboViewer.getElementAt(0);
						if (cd.getValue().startsWith("aodl:")) //$NON-NLS-1$
						{
							c.setCrit1(cd.getValue().substring(5));
						}
						else
						{
							c.setCrit1(cd.getValue());
						}
					}
				}
				final Combo typeCombo = new Combo(_searchPTagComp, SWT.READ_ONLY);
				typeCombo.setLayoutData(new GridData());
				typeCombo.setLayoutData(new GridData());
				((GridData) typeCombo.getLayoutData()).horizontalAlignment = SWT.FILL;
				((GridData) typeCombo.getLayoutData()).grabExcessHorizontalSpace = true;
				((GridData) typeCombo.getLayoutData()).horizontalSpan = 2;
				final ComboViewer typeComboViewer = new ComboViewer(typeCombo);
				typeComboViewer.setContentProvider(new MarkupContentProvider(false));
				typeComboViewer.setLabelProvider(new MarkupLabelProvider());
				typeComboViewer.setComparator(new ConfigDataComparator());

				tagComboViewer.addSelectionChangedListener(new ISelectionChangedListener()
				{
					@Override
					public void selectionChanged(final SelectionChangedEvent event)
					{
						ISelection iSelection = event.getSelection();
						Object obj = ((IStructuredSelection) iSelection).getFirstElement();
						ConfigData cd = (ConfigData) obj;
						String selection;
						if (cd.getValue().startsWith("aodl:")) //$NON-NLS-1$
						{
							selection = cd.getValue().substring(5);
						}
						else
						{
							selection = cd.getValue();
						}
						c.setCrit1(selection);
						setComboViewerInput(typeComboViewer, "tagging_values", c.getCrit1(), null, null); //$NON-NLS-1$
					}
				});

				if (c.getCrit2() != null)
				{
					setComboViewerByString(typeComboViewer, c.getCrit2());
				}

				final Combo subtypeCombo = new Combo(_searchPTagComp, SWT.READ_ONLY);
				final ComboViewer subtypeComboViewer = new ComboViewer(subtypeCombo);
				subtypeComboViewer.setContentProvider(new MarkupContentProvider());
				subtypeComboViewer.setLabelProvider(new MarkupLabelProvider());
				subtypeComboViewer.setComparator(new ConfigDataComparator());

				typeComboViewer.addSelectionChangedListener(new ISelectionChangedListener()
				{
					@Override
					public void selectionChanged(final SelectionChangedEvent event)
					{
						ISelection iSelection = event.getSelection();
						Object obj = ((IStructuredSelection) iSelection).getFirstElement();
						ConfigData cd = (ConfigData) obj;
						subtypeComboViewer.setInput(null);
						if (cd != null)
						{
							c.setCrit2(cd.getValue());
						}
						setComboViewerInput(subtypeComboViewer, "tagging_values", c.getCrit1(), c.getCrit2(), null); //$NON-NLS-1$
					}
				});

				subtypeCombo.setLayoutData(new GridData());
				subtypeCombo.setLayoutData(new GridData());
				((GridData) subtypeCombo.getLayoutData()).horizontalAlignment = SWT.FILL;
				((GridData) subtypeCombo.getLayoutData()).grabExcessHorizontalSpace = true;
				((GridData) subtypeCombo.getLayoutData()).horizontalSpan = 2;
				if (c.getCrit3() != null)
				{
					setComboViewerByString(subtypeComboViewer, c.getCrit3());
				}

				subtypeComboViewer.addSelectionChangedListener(new ISelectionChangedListener()
				{
					@Override
					public void selectionChanged(final SelectionChangedEvent event)
					{
						ISelection iSelection = event.getSelection();
						Object obj = ((IStructuredSelection) iSelection).getFirstElement();
						ConfigData cd = (ConfigData) obj;
						if (cd != null)
						{
							c.setCrit3(cd.getValue());
						}
					}
				});

				final Text searchText = new Text(_searchPTagComp, SWT.BORDER);
				searchText.setLayoutData(new GridData());
				searchText.setLayoutData(new GridData());
				((GridData) searchText.getLayoutData()).horizontalAlignment = SWT.FILL;
				((GridData) searchText.getLayoutData()).grabExcessHorizontalSpace = true;
				((GridData) searchText.getLayoutData()).horizontalSpan = 3;
				if (c.getSearchText() != null)
				{
					searchText.setText(c.getSearchText());
				}
				searchText.addFocusListener(new FocusAdapter()
				{
					@Override
					public void focusLost(final FocusEvent e)
					{
						c.setSearchText(searchText.getText());
					}
				});
				searchText.addKeyListener(new KeyListener()
				{
					@Override
					public void keyPressed(final KeyEvent e)
					{
						if (e.keyCode == SWT.CR)
						{
							c.setSearchText(searchText.getText());
							okPressed();
						}
					}

					@Override
					public void keyReleased(final KeyEvent e)
					{
					}
				});

				final Button fuzzyB = new Button(_searchPTagComp, SWT.CHECK);
				fuzzyB.setLayoutData(new GridData());
				fuzzyB.setSelection(c.isFuzzy());
				fuzzyB.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent event)
					{
						c.setFuzzy(!c.isFuzzy());
					}
				});

				final Button includeB = new Button(_searchPTagComp, SWT.CHECK);
				includeB.setLayoutData(new GridData());
				includeB.setSelection(c.isIncludeConcurrences());
				includeB.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent event)
					{
						c.setIncludeConcurrences(!c.isIncludeConcurrences());
					}
				});

			} // if tagging

			if (c.getType().equals("relation")) //$NON-NLS-1$
			{
				if (rel1)
				{
					rel1 = false;
					_searchPRelComp = new Composite(_personSearchGroup, SWT.NONE);
					_searchPRelComp.setLayout(new GridLayout());
					((GridLayout) _searchPRelComp.getLayout()).makeColumnsEqualWidth = true;
					((GridLayout) _searchPRelComp.getLayout()).numColumns = 14;
					_searchPRelComp.setLayoutData(new GridData());
					((GridData) _searchPRelComp.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) _searchPRelComp.getLayoutData()).grabExcessHorizontalSpace = true;
					// ((GridData) searchPRelComp.getLayoutData()).heightHint =
					// 200;
					((GridData) _searchPRelComp.getLayoutData()).grabExcessVerticalSpace = false;
					((GridData) _searchPRelComp.getLayoutData()).horizontalSpan = 1;

					Label l = new Label(_searchPRelComp, SWT.NONE);
					l.setText(NLMessages.getString("Dialog_relation")); //$NON-NLS-1$
					l.setLayoutData(new GridData());
					((GridData) l.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) l.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) l.getLayoutData()).horizontalSpan = 1;

					Label context = new Label(_searchPRelComp, SWT.NONE);
					context.setText(NLMessages.getString("Dialog_context")); //$NON-NLS-1$
					context.setLayoutData(new GridData());
					((GridData) context.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) context.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) context.getLayoutData()).horizontalSpan = 2;

					Label classL = new Label(_searchPRelComp, SWT.NONE);
					classL.setText(NLMessages.getString("Dialog_class")); //$NON-NLS-1$
					classL.setLayoutData(new GridData());
					((GridData) classL.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) classL.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) classL.getLayoutData()).horizontalSpan = 2;

					Label relObj = new Label(_searchPRelComp, SWT.NONE);
					relObj.setText(NLMessages.getString("Dialog_relObject")); //$NON-NLS-1$
					relObj.setLayoutData(new GridData());
					((GridData) relObj.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) relObj.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) relObj.getLayoutData()).horizontalSpan = 4;

					Label searchTextLabel = new Label(_searchPRelComp, SWT.NONE);
					searchTextLabel.setText(NLMessages.getString("Dialog_searchText")); //$NON-NLS-1$
					searchTextLabel.setLayoutData(new GridData());
					((GridData) searchTextLabel.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) searchTextLabel.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) searchTextLabel.getLayoutData()).horizontalSpan = 3;

					Label fuzzy = new Label(_searchPRelComp, SWT.NONE);
					fuzzy.setText(""); //$NON-NLS-1$
					fuzzy.setLayoutData(new GridData());
					((GridData) fuzzy.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) fuzzy.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) fuzzy.getLayoutData()).horizontalSpan = 1;

					Label include = new Label(_searchPRelComp, SWT.NONE);
					include.setText(""); //$NON-NLS-1$
					include.setToolTipText(NLMessages.getString("Dialog_includeConcurrences")); //$NON-NLS-1$
					include.setLayoutData(new GridData());
					((GridData) include.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) include.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) include.getLayoutData()).horizontalSpan = 1;

				}

				if (i == 0)
				{
					Label l = new Label(_searchPRelComp, SWT.NONE);
					l.setLayoutData(new GridData());
					l.setLayoutData(new GridData());
					((GridData) l.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) l.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) l.getLayoutData()).horizontalSpan = 1;
				}
				else
				{
					final Combo opCombo = new Combo(_searchPRelComp, SWT.READ_ONLY);
					opCombo.setLayoutData(new GridData());
					opCombo.add(Operator.AND.toString());
					opCombo.add(Operator.OR.toString());
					opCombo.add(Operator.NOT.toString());
					opCombo.setLayoutData(new GridData());
					((GridData) opCombo.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) opCombo.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) opCombo.getLayoutData()).horizontalSpan = 1;
					if (c.getOperator() != null)
					{
						opCombo.select(opCombo.indexOf(c.getOperator()));
					}
					else
					{
						opCombo.select(0);
						c.setOperator(opCombo.getItem(0));

					}
					opCombo.addSelectionListener(new SelectionAdapter()
					{
						@Override
						public void widgetSelected(final SelectionEvent se)
						{
							c.setOperator(opCombo.getItem(opCombo.getSelectionIndex()));
						}
					});
				}

				final Combo contextCombo = new Combo(_searchPRelComp, SWT.READ_ONLY);
				contextCombo.setLayoutData(new GridData());
				contextCombo.setLayoutData(new GridData());
				((GridData) contextCombo.getLayoutData()).horizontalAlignment = SWT.FILL;
				((GridData) contextCombo.getLayoutData()).grabExcessHorizontalSpace = true;
				((GridData) contextCombo.getLayoutData()).horizontalSpan = 2;
				final ComboViewer contextComboViewer = new ComboViewer(contextCombo);
				contextComboViewer.setContentProvider(new MarkupContentProvider());
				contextComboViewer.setLabelProvider(new MarkupLabelProvider());
				contextComboViewer.setComparator(new ConfigDataComparator());

				final Combo classCombo = new Combo(_searchPRelComp, SWT.READ_ONLY);
				classCombo.setLayoutData(new GridData());
				classCombo.setLayoutData(new GridData());
				((GridData) classCombo.getLayoutData()).horizontalAlignment = SWT.FILL;
				((GridData) classCombo.getLayoutData()).grabExcessHorizontalSpace = true;
				((GridData) classCombo.getLayoutData()).horizontalSpan = 2;
				final ComboViewer classComboViewer = new ComboViewer(classCombo);
				classComboViewer.setContentProvider(new MarkupContentProvider());
				classComboViewer.setLabelProvider(new MarkupLabelProvider());
				classComboViewer.setComparator(new ConfigDataComparator());
				setComboViewerInput(contextComboViewer, "relation", null, null, null); //$NON-NLS-1$
				if (c.getRelationContext() != null)
				{
					setComboViewerByString(contextComboViewer, c.getRelationContext());
					setComboViewerInput(classComboViewer, "relation", c.getRelationContext(), null, null); //$NON-NLS-1$

				}
				else
				{
					contextComboViewer.setSelection(new StructuredSelection(contextComboViewer.getElementAt(0)));
					ConfigData cd = (ConfigData) contextComboViewer.getElementAt(0);
					c.setRelationContext(cd.getValue());

				}
				contextComboViewer.addSelectionChangedListener(new ISelectionChangedListener()
				{
					@Override
					public void selectionChanged(final SelectionChangedEvent event)
					{
						ISelection iSelection = event.getSelection();
						Object obj = ((IStructuredSelection) iSelection).getFirstElement();
						ConfigData cd = (ConfigData) obj;
						c.setRelationContext(cd.getValue());
						setComboViewerInput(classComboViewer, "relation", c.getRelationContext(), null, null); //$NON-NLS-1$
					}
				});

				if (c.getRelationClass() != null)
				{
					setComboViewerByString(classComboViewer, c.getRelationClass());
				}

				classComboViewer.addSelectionChangedListener(new ISelectionChangedListener()
				{
					@Override
					public void selectionChanged(final SelectionChangedEvent event)
					{
						ISelection iSelection = event.getSelection();
						Object obj = ((IStructuredSelection) iSelection).getFirstElement();
						ConfigData cd = (ConfigData) obj;
						c.setRelationClass(cd.getValue());
					}
				});

				final Text relObjText = new Text(_searchPRelComp, SWT.BORDER);
				relObjText.setLayoutData(new GridData());
				relObjText.setLayoutData(new GridData());
				((GridData) relObjText.getLayoutData()).horizontalAlignment = SWT.FILL;
				((GridData) relObjText.getLayoutData()).grabExcessHorizontalSpace = true;
				((GridData) relObjText.getLayoutData()).horizontalSpan = 3;
				if (c.getRelatedId() != null)
				{
					relObjText.setText(c.getRelatedId().toString());
				}
				relObjText.setEnabled(false);

				final Button setObj = new Button(_searchPRelComp, SWT.PUSH);
				setObj.setEnabled(false);
				setObj.setText(NLMessages.getString("Dialog_set_key")); //$NON-NLS-1$
				setObj.setFont(JFaceResources.getDialogFont());
				setObj.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent event)
					{
					}
				});

				final Text searchText = new Text(_searchPRelComp, SWT.BORDER);
				searchText.setLayoutData(new GridData());
				searchText.setLayoutData(new GridData());
				((GridData) searchText.getLayoutData()).horizontalAlignment = SWT.FILL;
				((GridData) searchText.getLayoutData()).grabExcessHorizontalSpace = true;
				((GridData) searchText.getLayoutData()).horizontalSpan = 3;
				if (c.getSearchText() != null)
				{
					searchText.setText(c.getSearchText());
				}
				searchText.addFocusListener(new FocusAdapter()
				{
					@Override
					public void focusLost(final FocusEvent e)
					{
						c.setSearchText(searchText.getText());
					}
				});
				searchText.addKeyListener(new KeyListener()
				{
					@Override
					public void keyPressed(final KeyEvent e)
					{
						if (e.keyCode == SWT.CR)
						{
							c.setSearchText(searchText.getText());
							okPressed();
						}
					}

					@Override
					public void keyReleased(final KeyEvent e)
					{
					}
				});
				final Button fuzzyB = new Button(_searchPRelComp, SWT.CHECK);
				fuzzyB.setLayoutData(new GridData());
				fuzzyB.setSelection(c.isFuzzy());
				fuzzyB.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent event)
					{
						c.setFuzzy(!c.isFuzzy());
					}
				});

				final Button includeB = new Button(_searchPRelComp, SWT.CHECK);
				includeB.setLayoutData(new GridData());
				includeB.setSelection(c.isIncludeConcurrences());
				includeB.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent event)
					{
						c.setIncludeConcurrences(!c.isIncludeConcurrences());
					}
				});

			} // if relation
			if (c.getType().equals("date")) //$NON-NLS-1$
			{
				if (date1)
				{
					date1 = false;
					_searchPDateComp = new Composite(_personSearchGroup, SWT.NONE);
					_searchPDateComp.setLayout(new GridLayout());
					((GridLayout) _searchPDateComp.getLayout()).makeColumnsEqualWidth = true;
					((GridLayout) _searchPDateComp.getLayout()).numColumns = 14;
					_searchPDateComp.setLayoutData(new GridData());
					((GridData) _searchPDateComp.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) _searchPDateComp.getLayoutData()).grabExcessHorizontalSpace = true;
					// ((GridData) searchPDateComp.getLayoutData()).heightHint =
					// 200;
					((GridData) _searchPDateComp.getLayoutData()).grabExcessVerticalSpace = false;
					((GridData) _searchPDateComp.getLayoutData()).horizontalSpan = 1;

					Label l2 = new Label(_searchPDateComp, SWT.NONE);
					l2.setText(NLMessages.getString("Dialog_date")); //$NON-NLS-1$
					l2.setLayoutData(new GridData());
					((GridData) l2.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) l2.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) l2.getLayoutData()).horizontalSpan = 1;

					Label typeDate = new Label(_searchPDateComp, SWT.NONE);
					typeDate.setText(NLMessages.getString("Dialog_type")); //$NON-NLS-1$
					typeDate.setLayoutData(new GridData());
					((GridData) typeDate.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) typeDate.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) typeDate.getLayoutData()).horizontalSpan = 2;

					//
					Label notBefore = new Label(_searchPDateComp, SWT.NONE);
					notBefore.setText(NLMessages.getString("Dialog_day")); //$NON-NLS-1$
					notBefore.setLayoutData(new GridData());
					((GridData) notBefore.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) notBefore.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) notBefore.getLayoutData()).horizontalSpan = 1;

					Label month = new Label(_searchPDateComp, SWT.NONE);
					month.setText(NLMessages.getString("Dialog_month")); //$NON-NLS-1$
					month.setLayoutData(new GridData());
					((GridData) month.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) month.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) month.getLayoutData()).horizontalSpan = 1;
					Label bl = new Label(_searchPDateComp, SWT.NONE);
					bl.setText(""); //$NON-NLS-1$
					Label year = new Label(_searchPDateComp, SWT.NONE);
					year.setText(NLMessages.getString("Dialog_year")); //$NON-NLS-1$
					year.setLayoutData(new GridData());
					((GridData) year.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) year.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) year.getLayoutData()).horizontalSpan = 2;

					// Label bl2 = new Label (searchPDateComp, SWT.NONE);
					//					bl2.setText(""); //$NON-NLS-1$

					Label notAfter = new Label(_searchPDateComp, SWT.NONE);
					notAfter.setText(NLMessages.getString("Dialog_day")); //$NON-NLS-1$
					notAfter.setLayoutData(new GridData());
					((GridData) notAfter.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) notAfter.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) notAfter.getLayoutData()).horizontalSpan = 1;

					Label month2 = new Label(_searchPDateComp, SWT.NONE);
					month2.setText(NLMessages.getString("Dialog_month")); //$NON-NLS-1$
					month2.setLayoutData(new GridData());
					((GridData) month2.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) month2.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) month2.getLayoutData()).horizontalSpan = 1;
					Label bl2 = new Label(_searchPDateComp, SWT.NONE);
					bl2.setText(""); //$NON-NLS-1$
					Label year2 = new Label(_searchPDateComp, SWT.NONE);
					year2.setText(NLMessages.getString("Dialog_year")); //$NON-NLS-1$
					year2.setLayoutData(new GridData());
					((GridData) year2.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) year2.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) year2.getLayoutData()).horizontalSpan = 2;

					Label include = new Label(_searchPDateComp, SWT.NONE);
					include.setText(""); //$NON-NLS-1$
					include.setToolTipText(NLMessages.getString("Dialog_includeConcurrences")); //$NON-NLS-1$
					include.setLayoutData(new GridData());
					((GridData) include.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) include.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) include.getLayoutData()).horizontalSpan = 1;

				}

				if (i == 0)
				{
					Label l = new Label(_searchPDateComp, SWT.NONE);
					l.setLayoutData(new GridData());
					((GridData) l.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) l.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) l.getLayoutData()).horizontalSpan = 1;
				}
				else
				{
					final Combo opCombo = new Combo(_searchPDateComp, SWT.READ_ONLY);
					opCombo.setLayoutData(new GridData());
					opCombo.add(Operator.AND.toString());
					opCombo.add(Operator.OR.toString());
					opCombo.add(Operator.NOT.toString());
					opCombo.setLayoutData(new GridData());
					((GridData) opCombo.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) opCombo.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) opCombo.getLayoutData()).horizontalSpan = 1;
					if (c.getOperator() != null)
					{
						opCombo.setText(c.getOperator());
					}
					else
					{
						opCombo.select(0);
						c.setOperator(opCombo.getItem(0));

					}
					opCombo.addSelectionListener(new SelectionAdapter()
					{
						@Override
						public void widgetSelected(final SelectionEvent se)
						{
							c.setOperator(opCombo.getItem(opCombo.getSelectionIndex()));
						}
					});
				}

				final Combo typeDCombo = new Combo(_searchPDateComp, SWT.READ_ONLY);
				typeDCombo.setLayoutData(new GridData());
				ComboViewer timeTypeComboViewer = new ComboViewer(typeDCombo);
				timeTypeComboViewer.setContentProvider(ArrayContentProvider.getInstance());
				timeTypeComboViewer.setLabelProvider(new LabelProvider()
				{

					@Override
					public String getText(final Object element)
					{
						String str = (String) element;
						if (NLMessages.getString("Editor_time_" + str) != null) //$NON-NLS-1$
						{
							return NLMessages.getString("Editor_time_" + str); //$NON-NLS-1$
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
						//				        System.out.println("datetype selected " + s); //$NON-NLS-1$
						c.setDateType(s);
					}

				});
				if (c.getDateType() != null)
				{
					StructuredSelection selection = new StructuredSelection(c.getDateType());
					timeTypeComboViewer.setSelection(selection);
				}
				else
				{
					StructuredSelection selection = new StructuredSelection(AEConstants.TIME_TYPES[0]);
					timeTypeComboViewer.setSelection(selection);
					c.setDateType(AEConstants.TIME_TYPES[0]);
				}

				Label from = new Label(_searchPDateComp, SWT.NONE);
				from.setText(NLMessages.getString("Dialog_from")); //$NON-NLS-1$
				from.setLayoutData(new GridData());
				((GridData) from.getLayoutData()).horizontalAlignment = SWT.FILL;
				((GridData) from.getLayoutData()).grabExcessHorizontalSpace = true;
				((GridData) from.getLayoutData()).horizontalSpan = 1;

				final Combo day1Combo = new Combo(_searchPDateComp, SWT.READ_ONLY);
				day1Combo.setLayoutData(new GridData());
				day1Combo.setItems(AEConstants.DAYS);
				day1Combo.setLayoutData(new GridData());
				((GridData) day1Combo.getLayoutData()).horizontalAlignment = SWT.FILL;
				((GridData) day1Combo.getLayoutData()).grabExcessHorizontalSpace = true;
				((GridData) day1Combo.getLayoutData()).horizontalSpan = 1;

				if (c.getDateFrom() == null)
				{
					PdrDate dateFrom = new PdrDate("0000-00-00"); //$NON-NLS-1$
					// dateFrom.setDay(0);
					// dateFrom.setMonth(0);
					// dateFrom.setYear(0);
					c.setDateFrom(dateFrom);
					day1Combo.select(c.getDateFrom().getDay());
				}
				//				System.out.println("test: dateFrom " + c.getDateFrom().toString()); //$NON-NLS-1$
				// else
				// {
				//
				// day1Combo.select(0);
				// c.getDateFrom().setDay(0);
				//
				// }

				day1Combo.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent se)
					{
						c.getDateFrom().setDay(day1Combo.getSelectionIndex());
					}
				});

				final Combo month1Combo = new Combo(_searchPDateComp, SWT.READ_ONLY);
				month1Combo.setLayoutData(new GridData());
				month1Combo.setItems(AEConstants.MONTHS);
				month1Combo.setLayoutData(new GridData());
				((GridData) month1Combo.getLayoutData()).horizontalAlignment = SWT.FILL;
				((GridData) month1Combo.getLayoutData()).grabExcessHorizontalSpace = true;
				((GridData) month1Combo.getLayoutData()).horizontalSpan = 1;
				if (c.getDateFrom() != null)
				{
					month1Combo.select(c.getDateFrom().getMonth());
				}
				else
				{

					month1Combo.select(0);
					c.getDateFrom().setMonth(0);

				}

				month1Combo.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent se)
					{
						c.getDateFrom().setMonth(month1Combo.getSelectionIndex());
					}
				});

				final YearSpinner year1Spinner = new YearSpinner(_searchPDateComp, SWT.NULL);
				if (c.getDateFrom() != null)
				{
					year1Spinner.setSelection(c.getDateFrom().getYear());
				}
				else
				{

					year1Spinner.setSelection(_preselection);
					c.getDateFrom().setYear(_preselection);

				}

				year1Spinner.addSelectionListener(new SelectionListener()
				{

					@Override
					public void widgetDefaultSelected(final SelectionEvent e)
					{
						c.getDateFrom().setYear(year1Spinner.getSelection());

					}

					@Override
					public void widgetSelected(final SelectionEvent e)
					{
						c.getDateFrom().setYear(year1Spinner.getSelection());
					}

				});

				Label to = new Label(_searchPDateComp, SWT.NONE);
				to.setText(NLMessages.getString("Dialog_to")); //$NON-NLS-1$
				to.setLayoutData(new GridData());
				((GridData) to.getLayoutData()).horizontalAlignment = SWT.FILL;
				((GridData) to.getLayoutData()).grabExcessHorizontalSpace = true;
				((GridData) to.getLayoutData()).horizontalSpan = 1;

				final Combo day2Combo = new Combo(_searchPDateComp, SWT.READ_ONLY);
				day2Combo.setLayoutData(new GridData());
				day2Combo.setItems(AEConstants.DAYS);
				day2Combo.setLayoutData(new GridData());
				((GridData) day2Combo.getLayoutData()).horizontalAlignment = SWT.FILL;
				((GridData) day2Combo.getLayoutData()).grabExcessHorizontalSpace = true;
				((GridData) day2Combo.getLayoutData()).horizontalSpan = 1;
				if (c.getDateTo() == null)
				{
					PdrDate dateTo = new PdrDate("0000-00-00"); //$NON-NLS-1$
					c.setDateTo(dateTo);
					day2Combo.select(c.getDateTo().getDay());
				}

				day2Combo.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent se)
					{
						c.getDateTo().setDay(day2Combo.getSelectionIndex());
					}
				});

				final Combo month2Combo = new Combo(_searchPDateComp, SWT.READ_ONLY);
				month2Combo.setLayoutData(new GridData());
				month2Combo.setItems(AEConstants.MONTHS);
				month2Combo.setLayoutData(new GridData());
				((GridData) month2Combo.getLayoutData()).horizontalAlignment = SWT.FILL;
				((GridData) month2Combo.getLayoutData()).grabExcessHorizontalSpace = true;
				((GridData) month2Combo.getLayoutData()).horizontalSpan = 1;
				if (c.getDateTo() != null)
				{
					month2Combo.select(c.getDateTo().getMonth());
				}
				else
				{

					month2Combo.select(0);
					c.getDateTo().setMonth(0);

				}

				month2Combo.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent se)
					{
						c.getDateTo().setMonth(month2Combo.getSelectionIndex());
					}
				});

				final YearSpinner year2Spinner = new YearSpinner(_searchPDateComp, SWT.NULL);
				if (c.getDateTo() != null)
				{
					year2Spinner.setSelection(c.getDateTo().getYear());
				}
				else
				{
					year2Spinner.setSelection(_preselection);
					c.getDateTo().setYear(_preselection);
				}

				year2Spinner.addSelectionListener(new SelectionListener()
				{

					@Override
					public void widgetDefaultSelected(final SelectionEvent e)
					{
						c.getDateTo().setYear(year2Spinner.getSelection());

					}

					@Override
					public void widgetSelected(final SelectionEvent e)
					{
						c.getDateTo().setYear(year2Spinner.getSelection());
					}

				});

				final Button includeB = new Button(_searchPDateComp, SWT.CHECK);
				includeB.setLayoutData(new GridData());
				includeB.setSelection(c.isIncludeConcurrences());
				includeB.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent event)
					{
						c.setIncludeConcurrences(!c.isIncludeConcurrences());
					}
				});

			} // if date

			if (c.getType().equals("reference")) //$NON-NLS-1$
			{
				_searchPRefComp = new Composite(_personSearchGroup, SWT.NONE);
				_searchPRefComp.setLayout(new GridLayout());
				((GridLayout) _searchPRefComp.getLayout()).makeColumnsEqualWidth = true;
				((GridLayout) _searchPRefComp.getLayout()).numColumns = 14;
				_searchPRefComp.setLayoutData(new GridData());
				((GridData) _searchPRefComp.getLayoutData()).horizontalAlignment = SWT.FILL;
				((GridData) _searchPRefComp.getLayoutData()).grabExcessHorizontalSpace = true;
				// ((GridData) searchPTagComp.getLayoutData()).heightHint = 200;
				((GridData) _searchPRefComp.getLayoutData()).grabExcessVerticalSpace = false;
				((GridData) _searchPRefComp.getLayoutData()).horizontalSpan = 1;

				final Combo opCombo = new Combo(_searchPRefComp, SWT.READ_ONLY);
				opCombo.setLayoutData(new GridData());
				opCombo.add(Operator.AND.toString());
				opCombo.add(Operator.OR.toString());
				opCombo.add(Operator.NOT.toString());
				opCombo.setLayoutData(new GridData());
				((GridData) opCombo.getLayoutData()).horizontalAlignment = SWT.FILL;
				((GridData) opCombo.getLayoutData()).grabExcessHorizontalSpace = true;
				((GridData) opCombo.getLayoutData()).horizontalSpan = 1;
				if (c.getOperator() != null)
				{
					opCombo.setText(c.getOperator());
				}
				else
				{
					opCombo.select(0);
					c.setOperator(opCombo.getItem(0));

				}
				opCombo.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent se)
					{
						c.setOperator(opCombo.getItem(opCombo.getSelectionIndex()));
					}
				});

				Label sem = new Label(_searchPRefComp, SWT.NONE);
				sem.setText(NLMessages.getString("Dialog_reference")); //$NON-NLS-1$
				sem.setLayoutData(new GridData());
				((GridData) sem.getLayoutData()).horizontalAlignment = SWT.FILL;
				((GridData) sem.getLayoutData()).grabExcessHorizontalSpace = true;
				((GridData) sem.getLayoutData()).horizontalSpan = 2;

				Label tagName = new Label(_searchPRefComp, SWT.NONE);
				tagName.setText(NLMessages.getString("Dialog_genre")); //$NON-NLS-1$
				tagName.setLayoutData(new GridData());
				((GridData) tagName.getLayoutData()).horizontalAlignment = SWT.FILL;
				((GridData) tagName.getLayoutData()).grabExcessHorizontalSpace = true;
				((GridData) tagName.getLayoutData()).horizontalSpan = 2;

				final Combo genreCombo = new Combo(_searchPRefComp, SWT.READ_ONLY);
				genreCombo.setLayoutData(new GridData());
				ComboViewer genreComboViewer = new ComboViewer(genreCombo);
				genreComboViewer.setContentProvider(new RefTemplateContentProvider(false));
				genreComboViewer.setLabelProvider(new LabelProvider()
				{

					@Override
					public String getText(final Object element)
					{
						ReferenceModsTemplate template = (ReferenceModsTemplate) element;
						return template.getLabel();
					}

				});

				genreComboViewer.setInput(_facade.getAllGenres());
				genreComboViewer.addSelectionChangedListener(new ISelectionChangedListener()
				{

					@Override
					public void selectionChanged(final SelectionChangedEvent event)
					{
						ISelection selection = event.getSelection();
						Object obj = ((IStructuredSelection) selection).getFirstElement();
						ReferenceModsTemplate template = (ReferenceModsTemplate) obj;
						if (template != null)
						{
							c.setCrit0(template.getValue());
						}
					}

				});
				genreCombo.add("ALL", 0); //$NON-NLS-1$

				genreCombo.setLayoutData(new GridData());
				((GridData) genreCombo.getLayoutData()).horizontalAlignment = SWT.FILL;
				((GridData) genreCombo.getLayoutData()).grabExcessHorizontalSpace = true;
				((GridData) genreCombo.getLayoutData()).horizontalSpan = 2;
				if (c.getCrit0() != null)
				{
					StructuredSelection selection = new StructuredSelection(c.getCrit0());
					genreComboViewer.setSelection(selection);
				}
				else
				{
					genreCombo.select(0);
					c.setCrit0(genreCombo.getItem(0));

				}

				Label tagType = new Label(_searchPRefComp, SWT.NONE);
				tagType.setText(NLMessages.getString("Dialog_searchText")); //$NON-NLS-1$
				tagType.setLayoutData(new GridData());
				((GridData) tagType.getLayoutData()).horizontalAlignment = SWT.FILL;
				((GridData) tagType.getLayoutData()).grabExcessHorizontalSpace = true;
				((GridData) tagType.getLayoutData()).horizontalSpan = 2;

				final Text searchText = new Text(_searchPRefComp, SWT.BORDER);
				searchText.setLayoutData(new GridData());
				searchText.setLayoutData(new GridData());
				((GridData) searchText.getLayoutData()).horizontalAlignment = SWT.FILL;
				((GridData) searchText.getLayoutData()).grabExcessHorizontalSpace = true;
				((GridData) searchText.getLayoutData()).horizontalSpan = 3;
				if (c.getSearchText() != null)
				{
					searchText.setText(c.getSearchText());
				}
				searchText.addFocusListener(new FocusAdapter()
				{
					@Override
					public void focusLost(final FocusEvent e)
					{
						c.setSearchText(searchText.getText());
					}
				});

				searchText.addKeyListener(new KeyListener()
				{
					@Override
					public void keyPressed(final KeyEvent e)
					{
						if (e.keyCode == SWT.CR)
						{
							c.setSearchText(searchText.getText());
							okPressed();
						}
					}

					@Override
					public void keyReleased(final KeyEvent e)
					{
					}
				});

				final Button fuzzyB = new Button(_searchPRefComp, SWT.CHECK);
				fuzzyB.setLayoutData(new GridData());
				fuzzyB.setSelection(c.isFuzzy());
				fuzzyB.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent event)
					{
						c.setFuzzy(!c.isFuzzy());
					}
				});

				final Button includeB = new Button(_searchPRefComp, SWT.CHECK);
				includeB.setLayoutData(new GridData());
				includeB.setSelection(c.isIncludeConcurrences());
				includeB.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent event)
					{
						c.setIncludeConcurrences(!c.isIncludeConcurrences());
					}
				});

			} // if reference
		}

		// contentCompSearch.layout();
		// scrollCompSearch.setContent(contentCompSearch);
		// scrollCompSearch.setMinSize(contentCompSearch.computeSize(SWT.DEFAULT,
		// SWT.DEFAULT, true));
		// scrollCompSearch.layout();
		_personSearchGroup.redraw();
		_personSearchGroup.layout();
		_personSearchGroup.pack();
		_personSearchGroup.layout();
		// personSearchGroup.pack();

	}

	/**
	 * Builds the reference search.
	 * @param i the i
	 * @param object the object
	 */
	private void buildReferenceSearch(final int i, final Object object)
	{

		final Criteria c = _referenceQuery.getCriterias().firstElement();
		Composite searchRefComp = new Composite(_referenceSearchGroup, SWT.NONE);
		searchRefComp.setLayout(new GridLayout());
		((GridLayout) searchRefComp.getLayout()).makeColumnsEqualWidth = true;
		((GridLayout) searchRefComp.getLayout()).numColumns = 10;
		searchRefComp.setLayoutData(new GridData());
		((GridData) searchRefComp.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) searchRefComp.getLayoutData()).grabExcessHorizontalSpace = true;
		// ((GridData) searchRefComp.getLayoutData()).heightHint = 200;
		((GridData) searchRefComp.getLayoutData()).grabExcessVerticalSpace = false;
		((GridData) searchRefComp.getLayoutData()).horizontalSpan = 1;

		Button chooseFacetsButton = new Button(searchRefComp, SWT.CHECK);
		chooseFacetsButton.setText(NLMessages.getString("Dialog_faceted_search"));
		chooseFacetsButton.setLayoutData(new GridData());
		((GridData) chooseFacetsButton.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) chooseFacetsButton.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) chooseFacetsButton.getLayoutData()).horizontalSpan = 2;
		chooseFacetsButton.setSelection(_referenceQuery.getType() == 5);
		chooseFacetsButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent event)
			{
				if (_referenceQuery.getType() == 5)
				{
					_referenceQuery.setType(2);
				}
				else
				{
					_referenceQuery.setType(5);
				}
			}
		});

		Label refFacet = new Label(searchRefComp, SWT.NONE);
		refFacet.setText(NLMessages.getString("Dialog_faceted_by"));
		refFacet.setLayoutData(new GridData());
		((GridData) refFacet.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) refFacet.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) refFacet.getLayoutData()).horizontalSpan = 2;

		final Combo facetTypeCombo = new Combo(searchRefComp, SWT.READ_ONLY);
		facetTypeCombo.setLayoutData(new GridData());
		facetTypeCombo.setLayoutData(new GridData());
		((GridData) facetTypeCombo.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) facetTypeCombo.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) facetTypeCombo.getLayoutData()).horizontalSpan = 2;
		ComboViewer facetTypeComboViewer = new ComboViewer(facetTypeCombo);
		facetTypeComboViewer.setContentProvider(ArrayContentProvider.getInstance());
		facetTypeComboViewer.setLabelProvider(new LabelProvider()
		{
			@Override
			public String getText(final Object element)
			{
				String str = (String) element;
				return NLMessages.getString("Editor_" + str); //$NON-NLS-1$
			}
		});

		String[] refFacets = new String[]
		{
				"title", "subtitle", "partName", "name", "genre", "dateCreated", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
				"dateIssued", "dateCaptured", "copyrightDate", "publisher", "place", "edition", "physicalLocation", "shelfLocator"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$

		facetTypeComboViewer.setInput(refFacets);

		final Combo refFacetsCombo = new Combo(searchRefComp, SWT.READ_ONLY);
		refFacetsCombo.setLayoutData(new GridData());
		final ComboViewer refFacetsComboViewer = new ComboViewer(refFacetsCombo);
		refFacetsComboViewer.setContentProvider(ArrayContentProvider.getInstance());
		refFacetsComboViewer.setLabelProvider(new LabelProvider()
		{

			@Override
			public String getText(final Object element)
			{
				String str = (String) element;
				return str;
			}

		});
		facetTypeComboViewer.addSelectionChangedListener(new ISelectionChangedListener()
		{

			@Override
			public void selectionChanged(final SelectionChangedEvent event)
			{
				ISelection selection = event.getSelection();
				Object obj = ((IStructuredSelection) selection).getFirstElement();
				String s = (String) obj;
				String type = null;
				if (s.equals("name")) //$NON-NLS-1$
				{
					s = "namePart"; //$NON-NLS-1$
					type = "family"; //$NON-NLS-1$
				}
				try
				{
					_referenceFacets = _mainSearcher.getFacets("reference", s, type, null, null); //$NON-NLS-1$
				}
				catch (Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (s.equals("genre")) //$NON-NLS-1$
				{
					_referenceQuery.setKey("genre"); //$NON-NLS-1$
					String[] genreLabel = new String[_referenceFacets.length];
					for (int i = 0; i < _referenceFacets.length; i++)
					{
						ReferenceModsTemplate temp = _facade.getReferenceModsTemplates().get(_referenceFacets[i]);
						if (temp != null)
						{
							genreLabel[i] = temp.getLabel();
						}
						else
						{
							genreLabel[i] = _referenceFacets[i];
						}
					}
					refFacetsComboViewer.setInput(genreLabel);
				}
				else
				{
					_referenceQuery.setKey(null);
					refFacetsComboViewer.setInput(_referenceFacets);
				}
				setQueryFacets(_referenceQuery, _referenceFacets);
			}

		});

		// refFacetsComboViewer.setInput(facade.getReferenceModsTemplates());
		// refFacetsComboViewer.addSelectionChangedListener(new
		// ISelectionChangedListener() {
		//
		// public void selectionChanged(SelectionChangedEvent event) {
		// ISelection selection = event.getSelection();
		// Object obj = ((IStructuredSelection) selection).getFirstElement();
		// ReferenceModsTemplate template = (ReferenceModsTemplate) obj;
		//
		// }
		//
		// });

		refFacetsCombo.setLayoutData(new GridData());
		((GridData) refFacetsCombo.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) refFacetsCombo.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) refFacetsCombo.getLayoutData()).horizontalSpan = 4;

		Label op = new Label(searchRefComp, SWT.NONE);
		op.setText(NLMessages.getString("Dialog_genre")); //$NON-NLS-1$
		op.setLayoutData(new GridData());
		((GridData) op.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) op.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) op.getLayoutData()).horizontalSpan = 2;

		Label tagName = new Label(searchRefComp, SWT.NONE);
		tagName.setText(NLMessages.getString("Dialog_role")); //$NON-NLS-1$
		tagName.setLayoutData(new GridData());
		((GridData) tagName.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) tagName.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) tagName.getLayoutData()).horizontalSpan = 2;

		Label tagType = new Label(searchRefComp, SWT.NONE);
		tagType.setText(NLMessages.getString("Dialog_name")); //$NON-NLS-1$
		tagType.setLayoutData(new GridData());
		((GridData) tagType.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) tagType.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) tagType.getLayoutData()).horizontalSpan = 5;

		Label fuzzy = new Label(searchRefComp, SWT.NONE);
		fuzzy.setText(NLMessages.getString("Dialog_fuzzy")); //$NON-NLS-1$
		fuzzy.setLayoutData(new GridData());
		((GridData) fuzzy.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) fuzzy.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) fuzzy.getLayoutData()).horizontalSpan = 1;

		final Combo genreCombo = new Combo(searchRefComp, SWT.READ_ONLY);
		genreCombo.setLayoutData(new GridData());
		ComboViewer genreComboViewer = new ComboViewer(genreCombo);
		genreComboViewer.setContentProvider(new RefTemplateContentProvider(false));
		genreComboViewer.setLabelProvider(new LabelProvider()
		{

			@Override
			public String getText(final Object element)
			{
				ReferenceModsTemplate template = (ReferenceModsTemplate) element;
				return template.getLabel();
			}

		});

		genreComboViewer.setInput(_facade.getAllGenres());
		genreComboViewer.addSelectionChangedListener(new ISelectionChangedListener()
		{

			@Override
			public void selectionChanged(final SelectionChangedEvent event)
			{
				ISelection selection = event.getSelection();
				Object obj = ((IStructuredSelection) selection).getFirstElement();
				ReferenceModsTemplate template = (ReferenceModsTemplate) obj;
				if (template != null)
				{
					c.setCrit0(template.getValue());
				}
			}

		});
		genreCombo.add("ALL", 0); //$NON-NLS-1$

		genreCombo.setLayoutData(new GridData());
		((GridData) genreCombo.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) genreCombo.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) genreCombo.getLayoutData()).horizontalSpan = 2;
		if (c.getCrit0() != null)
		{
			StructuredSelection selection = new StructuredSelection(c.getCrit0());
			genreComboViewer.setSelection(selection);
		}
		else
		{
			genreCombo.select(0);
			c.setCrit0(genreCombo.getItem(0));

		}

		final Combo roleCombo = new Combo(searchRefComp, SWT.READ_ONLY);
		roleCombo.setLayoutData(new GridData());
		roleCombo.setLayoutData(new GridData());
		((GridData) roleCombo.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) roleCombo.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) roleCombo.getLayoutData()).horizontalSpan = 2;
		ComboViewer comboViewer = new ComboViewer(roleCombo);
		comboViewer.setContentProvider(ArrayContentProvider.getInstance());
		comboViewer.setLabelProvider(new LabelProvider()
		{
			@Override
			public String getText(final Object element)
			{
				String str = (String) element;
				if (str.equals("ALL"))
				{
					return str; //$NON-NLS-1$
				}
				return NLMessages.getString("Editor_role_" + str); //$NON-NLS-1$
			}
		});

		String[] input = new String[AEConstants.REF_ROLETERM_CODE.length + 1];
		System.arraycopy(new String[]
		{"ALL"}, 0, input, 0, 1); //$NON-NLS-1$
		System.arraycopy(AEConstants.REF_ROLETERM_CODE, 0, input, 1, AEConstants.REF_ROLETERM_CODE.length);
		comboViewer.setInput(input);
		comboViewer.addSelectionChangedListener(new ISelectionChangedListener()
		{

			@Override
			public void selectionChanged(final SelectionChangedEvent event)
			{
				ISelection selection = event.getSelection();
				Object obj = ((IStructuredSelection) selection).getFirstElement();
				String s = (String) obj;
				c.setCrit1(s);
			}

		});

		//		roleCombo.add("ALL", 0); //$NON-NLS-1$

		// if (c.getCrit1() != null)
		// {
		// roleCombo.setText(c.getCrit1());
		// }
		// else
		// {
		// roleCombo.select(0);
		// c.setCrit1(roleCombo.getItem(0));
		//
		//
		// }
		// roleCombo.addSelectionListener(new SelectionAdapter(){
		// public void widgetSelected(SelectionEvent se)
		// {
		// // if (!(semCombo.getSelectionIndex() > semCombo.getItems().length))
		// // {
		// c.setCrit1(roleCombo.getItem(roleCombo.getSelectionIndex()));
		// // }
		// }
		// });

		final Text nameText = new Text(searchRefComp, SWT.BORDER);
		nameText.setLayoutData(new GridData());
		nameText.setLayoutData(new GridData());
		((GridData) nameText.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) nameText.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) nameText.getLayoutData()).horizontalSpan = 5;
		if (c.getCrit3() != null)
		{
			nameText.setText(c.getCrit3());
		}
		nameText.addFocusListener(new FocusListener()
		{
			@Override
			public void focusGained(final FocusEvent e)
			{
				String[] vals = new String[]
				{};
				try
				{
					vals = _mainSearcher.getFacets("reference", "namePart", null, null, null); //$NON-NLS-1$ //$NON-NLS-2$
				}
				catch (Exception e1)
				{
					e1.printStackTrace();
				}
				new AutoCompleteField(nameText, new TextContentAdapter(), vals);

			}

			@Override
			public void focusLost(final FocusEvent e)
			{
				c.setCrit3(nameText.getText());
			}
		});
		nameText.addKeyListener(new KeyListener()
		{
			@Override
			public void keyPressed(final KeyEvent e)
			{
				if (e.keyCode == SWT.CR)
				{
					c.setCrit3(nameText.getText());
					okPressed();
				}
			}

			@Override
			public void keyReleased(final KeyEvent e)
			{
			}
		});
		final Button fuzzyB = new Button(searchRefComp, SWT.CHECK);
		fuzzyB.setLayoutData(new GridData());
		fuzzyB.setSelection(c.isFuzzy());
		fuzzyB.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent event)
			{
				c.setFuzzy(!c.isFuzzy());
			}
		});

		Label title = new Label(searchRefComp, SWT.NONE);
		title.setText(NLMessages.getString("Dialog_refTitle")); //$NON-NLS-1$
		title.setLayoutData(new GridData());
		((GridData) title.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) title.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) title.getLayoutData()).horizontalSpan = 2;

		final Text searchText2 = new Text(searchRefComp, SWT.BORDER);
		searchText2.setLayoutData(new GridData());
		searchText2.setLayoutData(new GridData());
		((GridData) searchText2.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) searchText2.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) searchText2.getLayoutData()).horizontalSpan = 8;
		if (c.getCrit4() != null)
		{
			searchText2.setText(c.getCrit4());
		}
		searchText2.addFocusListener(new FocusListener()
		{
			@Override
			public void focusGained(final FocusEvent e)
			{
				String[] vals = new String[]
				{};
				try
				{
					vals = _mainSearcher.getFacets("reference", "title", null, null, null); //$NON-NLS-1$ //$NON-NLS-2$
				}
				catch (Exception e1)
				{
					e1.printStackTrace();
				}
				new AutoCompleteField(searchText2, new TextContentAdapter(), vals);

			}

			@Override
			public void focusLost(final FocusEvent e)
			{
				c.setCrit4(searchText2.getText());
			}
		});
		searchText2.addKeyListener(new KeyListener()
		{
			@Override
			public void keyPressed(final KeyEvent e)
			{
				if (e.keyCode == SWT.CR)
				{
					c.setCrit4(searchText2.getText());
					okPressed();
				}
			}

			@Override
			public void keyReleased(final KeyEvent e)
			{
			}
		});
		Label searchText = new Label(searchRefComp, SWT.NONE);
		searchText.setText(NLMessages.getString("Dialog_freeSearch")); //$NON-NLS-1$
		searchText.setLayoutData(new GridData());
		((GridData) searchText.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) searchText.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) searchText.getLayoutData()).horizontalSpan = 2;

		final Text searchText3 = new Text(searchRefComp, SWT.BORDER);
		searchText3.setLayoutData(new GridData());
		searchText3.setLayoutData(new GridData());
		((GridData) searchText3.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) searchText3.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) searchText3.getLayoutData()).horizontalSpan = 8;
		if (c.getSearchText() != null)
		{
			searchText3.setText(c.getSearchText());
		}
		searchText3.addFocusListener(new FocusAdapter()
		{
			@Override
			public void focusLost(final FocusEvent e)
			{
				c.setSearchText(searchText3.getText());
			}
		});
		searchText3.addKeyListener(new KeyListener()
		{
			@Override
			public void keyPressed(final KeyEvent e)
			{
				if (e.keyCode == SWT.CR)
				{
					c.setSearchText(searchText3.getText());
					okPressed();
				}
			}

			@Override
			public void keyReleased(final KeyEvent e)
			{
			}
		});
	}

	/**
	 * Cerate aspect query.
	 */
	private void cerateAspectQuery()
	{
		_aspectFacetQuery = new PdrQuery();
		_aspectFacetQuery.setType(0);
		_aspectFacetQuery.setSearchLevel(1);
		_aspectFacetQuery.setKey("content"); //$NON-NLS-1$
		Criteria criteria = new Criteria();
		criteria.setType("tagging"); //$NON-NLS-1$
		_aspectFacetQuery.getCriterias().add(criteria);
		criteria = new Criteria();
		criteria.setType("tagging"); //$NON-NLS-1$
		_aspectFacetQuery.getCriterias().add(criteria);
		criteria = new Criteria();
		criteria.setType("tagging"); //$NON-NLS-1$
		_aspectFacetQuery.getCriterias().add(criteria);

		criteria = new Criteria();
		_aspectFacetQuery.getCriterias().add(criteria);
		criteria.setType("date"); //$NON-NLS-1$

		criteria = new Criteria();
		_aspectFacetQuery.getCriterias().add(criteria);
		criteria.setType("reference"); //$NON-NLS-1$

	}

	/**
	 * @see org.eclipse.jface.dialogs.Dialog#create()
	 */
	@Override
	public final void create()
	{
		super.create();
		// Set the title
		setTitle(NLMessages.getString("AdvancedDialog_title")); //$NON-NLS-1$
		// Set the message
		setMessage(NLMessages.getString("Dialog_message"), IMessageProvider.INFORMATION); //$NON-NLS-1$

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
		createOkButton(parent, OK, NLMessages.getString("Dialog_search"), true); //$NON-NLS-1$
		// Add a SelectionListener

		// Create Cancel button
		Button cancelButton = createButton(parent, CANCEL, NLMessages.getString("Dialog_cancel"), false); //$NON-NLS-1$
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

		parent.setSize(300, 200);

		_gridLayout = new GridLayout();
		_gridLayout.numColumns = 3;
		_gridLayout.makeColumnsEqualWidth = false;

		new GridData();

		_gridData2 = new GridData();
		_gridData2.verticalAlignment = GridData.FILL;
		_gridData2.horizontalSpan = 1;
		_gridData2.grabExcessHorizontalSpace = true;
		_gridData2.grabExcessVerticalSpace = true;
		_gridData2.horizontalAlignment = SWT.FILL;

		_mainTabFolder = new TabFolder(parent, SWT.TOP | SWT.FILL);
		_mainTabFolder.setLayoutData(_gridData2);

		_gridLayout2 = new GridLayout();
		_gridLayout2.numColumns = 1;
		_gridLayout2.makeColumnsEqualWidth = true;

		createQueries();

		createPersonTabItem(_mainTabFolder);
		createFacetPersonTabItem(_mainTabFolder);
		createFacetAspectTabItem(_mainTabFolder);
		createReferenceTabItem(_mainTabFolder);
		// parent.setLayout(layout);

		if (_loadedQuery != null)
		{
			switch (_loadedQuery.getType())
			{
				case 0:
				{
					_mainTabFolder.setSelection(2);
					break;
				}
				case 1:
				{
					_mainTabFolder.setSelection(0);
					break;
				}

				case 2:
				{
					_mainTabFolder.setSelection(3);
					break;
				}
				case 3:
				{
					_mainTabFolder.setSelection(1);
					break;
				}
				case 4:
				{
					_mainTabFolder.setSelection(2);
					break;
				}
				case 5:
				{
					_mainTabFolder.setSelection(3);
					break;
				}

			}
		}
		parent.pack();
		return parent;
	}

	/**
	 * Creates the facet aspect tab item.
	 * @param mainTabFolder the main tab folder
	 */
	private void createFacetAspectTabItem(final TabFolder mainTabFolder)
	{
		_facetAspectTabItem = new TabItem(mainTabFolder, SWT.NONE);
		_facetAspectTabItem.setText(NLMessages.getString("Dialog_aspectSearch"));
		_facetAspectTabItem.setImage(_imageReg.get(IconsInternal.ASPECTS));
		Composite facetPersonComposite = new Composite(mainTabFolder, SWT.NONE);
		facetPersonComposite.setLayout(new GridLayout());
		facetPersonComposite.setLayoutData(new GridData());
		((GridData) facetPersonComposite.getLayoutData()).verticalAlignment = SWT.FILL;

		_facetAspectTabItem.setControl(facetPersonComposite);

		_facetAspectSearchGroup = new Group(facetPersonComposite, SWT.SHADOW_IN);
		_facetAspectSearchGroup.setText("Aspect Search"); //$NON-NLS-1$

		_facetAspectSearchGroup.setLayoutData(new GridData());
		((GridData) _facetAspectSearchGroup.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _facetAspectSearchGroup.getLayoutData()).verticalAlignment = SWT.FILL;
		((GridData) _facetAspectSearchGroup.getLayoutData()).minimumHeight = 90;
		((GridData) _facetAspectSearchGroup.getLayoutData()).grabExcessHorizontalSpace = true;
		_facetAspectSearchGroup.setLayout(new GridLayout());
		((GridLayout) _facetAspectSearchGroup.getLayout()).makeColumnsEqualWidth = true;
		((GridLayout) _facetAspectSearchGroup.getLayout()).numColumns = 1;

		buildFacetAspectSearch(0, null);

		// _searchPTagComp
		// personSearchGroup
		_facetAspectSearchGroup.layout();

	}

	/**
	 * Creates the faceted person query.
	 */
	private void createFacetedPersonQuery()
	{
		_facetQuery = new PdrQuery();
		_facetQuery.setType(3);
		_facetQuery.setSearchLevel(1);
		_facetQuery.setKey("content"); //$NON-NLS-1$
		Criteria criteria = new Criteria();
		criteria.setType("tagging"); //$NON-NLS-1$
		_facetQuery.getCriterias().add(criteria);
		criteria = new Criteria();
		criteria.setType("tagging"); //$NON-NLS-1$
		_facetQuery.getCriterias().add(criteria);
		criteria = new Criteria();
		criteria.setType("tagging"); //$NON-NLS-1$
		_facetQuery.getCriterias().add(criteria);

		criteria = new Criteria();
		_facetQuery.getCriterias().add(criteria);
		criteria.setType("date"); //$NON-NLS-1$

		criteria = new Criteria();
		_facetQuery.getCriterias().add(criteria);
		criteria.setType("reference"); //$NON-NLS-1$

	}

	/**
	 * Creates the facet person tab item.
	 * @param mainTabFolder the main tab folder
	 */
	private void createFacetPersonTabItem(final TabFolder mainTabFolder)
	{
		_facetPersonTabItem = new TabItem(mainTabFolder, SWT.NONE);
		_facetPersonTabItem.setText(NLMessages.getString("Dialog_faceted_person")); //$NON-NLS-1$
		_facetPersonTabItem.setImage(_imageReg.get(IconsInternal.MARKUP));
		Composite facetPersonComposite = new Composite(mainTabFolder, SWT.NONE);
		facetPersonComposite.setLayout(new GridLayout());
		facetPersonComposite.setLayoutData(new GridData());
		((GridData) facetPersonComposite.getLayoutData()).verticalAlignment = SWT.FILL;

		_facetPersonTabItem.setControl(facetPersonComposite);

		_facetPersonSearchGroup = new Group(facetPersonComposite, SWT.SHADOW_IN);
		_facetPersonSearchGroup.setText(""); //$NON-NLS-1$

		_facetPersonSearchGroup.setLayoutData(new GridData());
		((GridData) _facetPersonSearchGroup.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _facetPersonSearchGroup.getLayoutData()).verticalAlignment = SWT.FILL;
		((GridData) _facetPersonSearchGroup.getLayoutData()).minimumHeight = 90;
		((GridData) _facetPersonSearchGroup.getLayoutData()).grabExcessHorizontalSpace = true;
		_facetPersonSearchGroup.setLayout(new GridLayout());
		((GridLayout) _facetPersonSearchGroup.getLayout()).makeColumnsEqualWidth = true;
		((GridLayout) _facetPersonSearchGroup.getLayout()).numColumns = 1;

		buildFacetPersonSearch(0, null);

		// _searchPTagComp
		// personSearchGroup
		_facetPersonSearchGroup.layout();

	}

	/**
	 * meth creates the OK button.
	 * @param parent parent composite
	 * @param id id
	 * @param label label of button
	 * @param defaultButton is default
	 * @return button.
	 */
	protected final Button createOkButton(final Composite parent, final int id, final String label,
			final boolean defaultButton)
	{
		// increment the number of columns in the button bar
		((GridLayout) parent.getLayout()).numColumns++;
		Button button = new Button(parent, SWT.PUSH);
		button.setText(label);
		button.setImage(_imageReg.get(IconsInternal.SEARCH));
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
		if (defaultButton)
		{
			Shell shell = parent.getShell();
			if (shell != null)
			{
				shell.setDefaultButton(button);
			}
		}
		setButtonLayoutData(button);
		return button;
	}

	/**
	 * Creates the person query.
	 */
	private void createPersonQuery()
	{
		_personQuery = new PdrQuery();
		_personQuery.setType(1);
		_personQuery.setSearchLevel(1);
		Criteria criteria = new Criteria();
		criteria.setType("tagging"); //$NON-NLS-1$
		_personQuery.getCriterias().add(criteria);
		criteria = new Criteria();
		criteria.setType("tagging"); //$NON-NLS-1$
		_personQuery.getCriterias().add(criteria);

		criteria = new Criteria();
		_personQuery.getCriterias().add(criteria);
		criteria.setType("relation"); //$NON-NLS-1$

		criteria = new Criteria();
		_personQuery.getCriterias().add(criteria);
		criteria.setType("date"); //$NON-NLS-1$

		criteria = new Criteria();
		_personQuery.getCriterias().add(criteria);
		criteria.setType("reference"); //$NON-NLS-1$

	}

	/**
	 * meth creates the TabItem for selecting a person.
	 * @param mainTabFolder main tabfolder
	 */
	private void createPersonTabItem(final TabFolder mainTabFolder)
	{
		_personTabItem = new TabItem(mainTabFolder, SWT.NONE);
		_personTabItem.setText(NLMessages.getString("Dialog_persons_search")); //$NON-NLS-1$
		_personTabItem.setImage(_imageReg.get(IconsInternal.PERSONS));
		_personComposite = new Composite(mainTabFolder, SWT.NONE);
		_personComposite.setLayout(new GridLayout());
		_personComposite.setLayoutData(new GridData());
		((GridData) _personComposite.getLayoutData()).verticalAlignment = SWT.FILL;

		_personTabItem.setControl(_personComposite);

		_personSearchGroup = new Group(_personComposite, SWT.SHADOW_IN);
		_personSearchGroup.setText(""); //$NON-NLS-1$

		_personSearchGroup.setLayoutData(new GridData());
		((GridData) _personSearchGroup.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _personSearchGroup.getLayoutData()).verticalAlignment = SWT.FILL;
		((GridData) _personSearchGroup.getLayoutData()).minimumHeight = 90;
		((GridData) _personSearchGroup.getLayoutData()).grabExcessHorizontalSpace = true;
		_personSearchGroup.setLayout(new GridLayout());
		((GridLayout) _personSearchGroup.getLayout()).makeColumnsEqualWidth = true;
		((GridLayout) _personSearchGroup.getLayout()).numColumns = 1;

		buildPersonSearch(0, null);

		// _searchPTagComp
		// personSearchGroup
		_personSearchGroup.layout();

	}

	/**
	 * Creates the queries.
	 */
	private void createQueries()
	{
		if (_loadedQuery != null)
		{
			switch (_loadedQuery.getType())
			{
				case 0:
					createPersonQuery();
					createFacetedPersonQuery();
					_aspectFacetQuery = _loadedQuery;
					createReferenceQuery();
					break;
				case 1:
					_personQuery = _loadedQuery;
					createFacetedPersonQuery();
					cerateAspectQuery();
					createReferenceQuery();
					break;
				case 2:
					createPersonQuery();
					createFacetedPersonQuery();
					cerateAspectQuery();
					_referenceQuery = _loadedQuery;
					break;
				case 3:
					createPersonQuery();
					_facetQuery = _loadedQuery;
					cerateAspectQuery();
					createReferenceQuery();
					break;
				case 4:
					createPersonQuery();
					createFacetedPersonQuery();
					_aspectFacetQuery = _loadedQuery;
					createReferenceQuery();
					break;
				case 5:
					createPersonQuery();
					createFacetedPersonQuery();
					cerateAspectQuery();
					_referenceQuery = _loadedQuery;
					break;
				default:
					break;

			}
		}
		else
		{
			createPersonQuery();
			createFacetedPersonQuery();
			cerateAspectQuery();
			createReferenceQuery();
		}

	}

	/**
	 * Creates the reference query.
	 */
	private void createReferenceQuery()
	{
		_referenceQuery = new PdrQuery();
		_referenceQuery.setType(2);
		_referenceQuery.setSearchLevel(1);
		Criteria criteria = new Criteria();
		criteria.setType("reference"); //$NON-NLS-1$
		_referenceQuery.getCriterias().add(criteria);

	}

	/**
	 * Creates the reference tab item.
	 * @param mainTabFolder the main tab folder
	 */
	private void createReferenceTabItem(final TabFolder mainTabFolder)
	{
		_referenceTabItem = new TabItem(mainTabFolder, SWT.NONE);
		_referenceTabItem.setText(NLMessages.getString("Dialog_aspect_search"));
		_referenceTabItem.setImage(_imageReg.get(IconsInternal.REFERENCES));
		Composite referenceComposite = new Composite(mainTabFolder, SWT.NONE);
		referenceComposite.setLayout(new GridLayout());
		referenceComposite.setLayoutData(new GridData());
		((GridData) referenceComposite.getLayoutData()).verticalAlignment = SWT.FILL;

		_referenceTabItem.setControl(referenceComposite);

		_referenceSearchGroup = new Group(referenceComposite, SWT.SHADOW_IN);
		_referenceSearchGroup.setText("Reference Search"); //$NON-NLS-1$

		_referenceSearchGroup.setLayoutData(new GridData());
		((GridData) _referenceSearchGroup.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _referenceSearchGroup.getLayoutData()).verticalAlignment = SWT.FILL;
		((GridData) _referenceSearchGroup.getLayoutData()).minimumHeight = 90;
		((GridData) _referenceSearchGroup.getLayoutData()).grabExcessHorizontalSpace = true;
		_referenceSearchGroup.setLayout(new GridLayout());
		((GridLayout) _referenceSearchGroup.getLayout()).makeColumnsEqualWidth = true;
		((GridLayout) _referenceSearchGroup.getLayout()).numColumns = 1;

		buildReferenceSearch(0, null);
		_referenceSearchGroup.layout();

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
		if (_mainTabFolder.getSelectionIndex() == 0)
		{
			_facade.setAdvancedQuery(_personQuery);
		}
		else if (_mainTabFolder.getSelectionIndex() == 1)
		{
			_facade.setAdvancedQuery(_facetQuery);
		}
		else if (_mainTabFolder.getSelectionIndex() == 2)
		{
			_facade.setAdvancedQuery(_aspectFacetQuery);
		}
		else if (_mainTabFolder.getSelectionIndex() == 3)
		{
			_facade.setAdvancedQuery(_referenceQuery);
		}
	}

	/**
	 * Sets the combo viewer by string.
	 * @param cv the cv
	 * @param s the s
	 */
	private void setComboViewerByString(final ComboViewer cv, final String s)
	{
		if (cv.getInput() instanceof HashMap<?, ?>)
		{
			//			System.out.println("has input and is hashmap"); //$NON-NLS-1$
			@SuppressWarnings("unchecked")
			HashMap<String, ConfigData> inputs = (HashMap<String, ConfigData>) cv.getInput();
			if (inputs.containsKey(s))
			{
				//				System.out.println("contains key s " + s); //$NON-NLS-1$
				for (String key : inputs.keySet())
				{
					if (key.equals(s))
					{
						ConfigData cd = inputs.get(key);
						if (cd instanceof ConfigItem && ((ConfigItem) cd).isIgnore())
						{
							((ConfigItem) cd).setReadAlthoughIgnored(true);
							cv.setInput(inputs);
						}
						cv.setSelection(new StructuredSelection(cd));
						return;
					}
				}
			}
		}
		ConfigItem ci = new ConfigItem();
		ci.setValue(s);
		ci.setLabel(s);
		cv.add(ci);
		StructuredSelection selection = new StructuredSelection(ci);
		cv.setSelection(selection);

	}

	/**
	 * Sets the combo viewer input.
	 * @param comboViewer the combo viewer
	 * @param facetType the facet type
	 * @param crit1 the crit1
	 * @param crit2 the crit2
	 * @param crit3 the crit3
	 */
	protected final void setComboViewerInput(final ComboViewer comboViewer, final String facetType, final String crit1,
			final String crit2, final String crit3)
	{
		// if (!crit1.startsWith("aodl:")) crit1 = "aodl:" + crit1;
		Vector<String> providers = new Vector<String>();
		for (String s : _facade.getConfigs().keySet())
		{
			if (!s.equals(_markupProvider))
			{
				providers.add(s);
			}
		}
		HashMap<String, ConfigData> inputs = new HashMap<String, ConfigData>();
		HashMap<String, ConfigData> configs = new HashMap<String, ConfigData>();
		ConfigItem ciAll = new ConfigItem();
		ciAll.setValue("ALL"); //$NON-NLS-1$
		ciAll.setLabel("ALL"); //$NON-NLS-1$
		ciAll.setIgnore(false);
		inputs.put("ALL", ciAll); //$NON-NLS-1$
		String[] values = null;
		if (facetType.equals("relation")) //$NON-NLS-1$
		{
			if (crit1 == null)
			{
				try
				{
					values = _mainSearcher.getFacets(facetType, null, null, null, //$NON-NLS-1$
							null);
				}
				catch (Exception e1)
				{
					e1.printStackTrace();
				}
				for (String provider : providers)
				{
					if (_facade.getConfigs().get(provider).getChildren().containsKey("aodl:relation")) //$NON-NLS-1$
					{
						configs.putAll(_facade.getConfigs().get(provider).getChildren()
								.get("aodl:relation").getChildren()); //$NON-NLS-1$
					}
				}
				//				System.out.println("markupprovider " + relationProvider); //$NON-NLS-1$
				if (_facade.getConfigs().containsKey(_relationProvider)
						&& _facade.getConfigs().get(_relationProvider).getChildren() != null
						&& _facade.getConfigs().get(_relationProvider).getChildren().containsKey("aodl:relation")) //$NON-NLS-1$
				{
					configs.putAll(_facade.getConfigs().get(_relationProvider).getChildren()
							.get("aodl:relation").getChildren()); //$NON-NLS-1$
				}
			}
			else if (crit1 != null)
			{
				try
				{
					values = _mainSearcher.getFacets(facetType, null, crit1, null, //$NON-NLS-1$
							null);
				}
				catch (Exception e1)
				{
					e1.printStackTrace();
				}
				for (String provider : providers)
				{
					if (_facade.getConfigs().get(provider).getChildren().containsKey("aodl:relation") //$NON-NLS-1$
							&& _facade.getConfigs().get(provider).getChildren().get("aodl:relation").getChildren() != null //$NON-NLS-1$
							&& _facade.getConfigs().get(provider).getChildren()
									.get("aodl:relation").getChildren().containsKey(crit1)) //$NON-NLS-1$
					{
						configs.putAll(_facade.getConfigs().get(provider).getChildren()
								.get("aodl:relation").getChildren().get(crit1).getChildren()); //$NON-NLS-1$
					}
				}
				//				System.out.println("relationProvider " + relationProvider); //$NON-NLS-1$
				if (_facade.getConfigs().containsKey(_relationProvider)
						&& _facade.getConfigs().get(_relationProvider).getChildren() != null
						&& _facade.getConfigs().get(_relationProvider).getChildren().containsKey("aodl:relation") //$NON-NLS-1$
						&& _facade.getConfigs().get(_relationProvider).getChildren().get("aodl:relation").getChildren() != null //$NON-NLS-1$
						&& _facade.getConfigs().get(_relationProvider).getChildren()
								.get("aodl:relation").getChildren().containsKey(crit1)) //$NON-NLS-1$
				{
					configs.putAll(_facade.getConfigs().get(_relationProvider).getChildren()
							.get("aodl:relation").getChildren().get(crit1).getChildren()); //$NON-NLS-1$
				}
			}

		}
		else if (crit1 != null && crit2 == null)
		{
			try
			{
				values = _mainSearcher.getFacets(facetType, crit1, null, null, //$NON-NLS-1$
						null);
			}
			catch (Exception e1)
			{
				e1.printStackTrace();
			}
			//			System.out.println("before put into configs crit1" + crit1); //$NON-NLS-1$

			for (String provider : providers)
			{
				if (_facade.getConfigs().get(provider).getChildren().containsKey("aodl:" + crit1)) //$NON-NLS-1$
				{
					//					System.out.println("put into configs crit1" + crit1); //$NON-NLS-1$

					configs.putAll(_facade.getConfigs().get(provider).getChildren().get("aodl:" + crit1).getChildren()); //$NON-NLS-1$
				}
			}
			//			System.out.println("markupprovider " + markupProvider); //$NON-NLS-1$
			if (_facade.getConfigs().containsKey(_markupProvider)
					&& _facade.getConfigs().get(_markupProvider).getChildren() != null
					&& _facade.getConfigs().get(_markupProvider).getChildren().containsKey("aodl:" + crit1)) //$NON-NLS-1$
			{
				//				System.out.println("put into configs crit1" + crit1); //$NON-NLS-1$
				configs.putAll(_facade.getConfigs().get(_markupProvider).getChildren()
						.get("aodl:" + crit1).getChildren()); //$NON-NLS-1$
			}

		}
		else if (crit1 != null && crit2 != null)
		{
			try
			{
				values = _mainSearcher.getFacets(facetType, crit1, crit2, null, //$NON-NLS-1$
						null);
			}
			catch (Exception e1)
			{
				e1.printStackTrace();
			}
			for (String provider : providers)
			{
				if (_facade.getConfigs().get(provider).getChildren().containsKey("aodl:" + crit1) //$NON-NLS-1$
						&& _facade.getConfigs().get(provider).getChildren().get("aodl:" + crit1).getChildren() != null //$NON-NLS-1$
						&& _facade.getConfigs().get(provider).getChildren()
								.get("aodl:" + crit1).getChildren().containsKey(crit2)) //$NON-NLS-1$
				{
					configs.putAll(_facade.getConfigs().get(provider).getChildren()
							.get("aodl:" + crit1).getChildren().get(crit2).getChildren()); //$NON-NLS-1$
				}
			}
			//			System.out.println("markupprovider " + markupProvider); //$NON-NLS-1$
			if (_facade.getConfigs().containsKey(_markupProvider)
					&& _facade.getConfigs().get(_markupProvider).getChildren() != null
					&& _facade.getConfigs().get(_markupProvider).getChildren().containsKey("aodl:" + crit1) //$NON-NLS-1$
					&& _facade.getConfigs().get(_markupProvider).getChildren().get("aodl:" + crit1).getChildren() != null //$NON-NLS-1$
					&& _facade.getConfigs().get(_markupProvider).getChildren()
							.get("aodl:" + crit1).getChildren().containsKey(crit2)) //$NON-NLS-1$
			{
				configs.putAll(_facade.getConfigs().get(_markupProvider).getChildren()
						.get("aodl:" + crit1).getChildren().get(crit2).getChildren()); //$NON-NLS-1$
			}

		}
		for (String value : values)
		{
			//			System.out.println("value " + value); //$NON-NLS-1$
			if (configs.containsKey(value))
			{
				inputs.put(value, configs.get(value));
			}
			else
			{
				ConfigItem ci = new ConfigItem();
				ci.setLabel(value);
				ci.setValue(value);
				inputs.put(value, ci);
			}
		}
		comboViewer.setInput(inputs);
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
		}
		facetQuery.setFacets(facets);
	}

}
