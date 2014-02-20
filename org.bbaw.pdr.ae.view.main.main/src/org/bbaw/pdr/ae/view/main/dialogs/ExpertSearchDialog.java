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

import java.util.Collections;
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
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
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
 * The Class ExpertSearchDialog.
 * @author Christoph Plutte
 */
public class ExpertSearchDialog extends TitleAreaDialog
{
	/** Instance of shared image registry. */
	private ImageRegistry _imageReg = CommonActivator.getDefault().getImageRegistry();
	/** singleton facade instance. */
	private Facade _facade = Facade.getInstanz();

	/** The preselection. */
	private int _preselection = Platform.getPreferencesService().getInt(CommonActivator.PLUGIN_ID,
			"ASPECT_PRESELECTED_DATE_YEAR", AEConstants.ASPECT_PRESELECTED_DATE_YEAR, null); //$NON-NLS-1$

	/** MainSearcher. */
	private AMainSearcher _mainSearcher = _facade.getMainSearcher();

	/** The pdr query. */
	private PdrQuery _pdrQuery;

	/** The criteria. */
	private Criteria _criteria;

	/** layout elements. */
	private TabFolder _mainTabFolder;

	/** The person tab item. */
	private TabItem _personTabItem;

	/** The grid layout. */
	private GridLayout _gridLayout;

	/** The grid layout2. */
	private GridLayout _gridLayout2;

	/** The grid data2. */
	private GridData _gridData2;

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

	/** The scroll comp search. */
	private ScrolledComposite _scrollCompSearch;

	/** The person search group. */
	private Group _personSearchGroup;

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

	/**
	 * Instantiates a new expert search dialog.
	 * @param parentShell the parent shell
	 */
	public ExpertSearchDialog(final Shell parentShell)
	{
		super(parentShell);
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
	 * Builds the person search.
	 * @param type the type
	 * @param crit the crit
	 */
	private void buildPersonSearch(final int type, final Integer crit)
	{
		DataType dtAll = new DataType();
		dtAll.setValue("ALL"); //$NON-NLS-1$
		dtAll.setLabel("ALL"); //$NON-NLS-1$
		if (_scrollCompSearch != null)
		{
			_scrollCompSearch.dispose();
		}
		if (_searchPTagComp != null)
		{
			_searchPTagComp.dispose();
		}
		// if (placeGroup != null) placeGroup.dispose();
		_scrollCompSearch = new ScrolledComposite(_personSearchGroup, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		_scrollCompSearch.setExpandHorizontal(true);
		_scrollCompSearch.setExpandVertical(true);
		_scrollCompSearch.setMinHeight(1);
		_scrollCompSearch.setMinWidth(1);

		_scrollCompSearch.setLayout(new GridLayout());
		_scrollCompSearch.setLayoutData(new GridData());
		((GridData) _scrollCompSearch.getLayoutData()).heightHint = 400;
		((GridData) _scrollCompSearch.getLayoutData()).horizontalSpan = 4;
		((GridData) _scrollCompSearch.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _scrollCompSearch.getLayoutData()).grabExcessHorizontalSpace = true;
		_scrollCompSearch.pack();

		Composite contentCompSearch = new Composite(_scrollCompSearch, SWT.NONE);
		contentCompSearch.setLayout(new GridLayout());

		_scrollCompSearch.setContent(contentCompSearch);

		boolean tag1 = true;
		boolean rel1 = true;
		boolean date1 = true;
		switch (type)
		{
			case 0:
				// pdrQuery = new PdrQuery();
				// criteria = new Criteria();
				// pdrQuery.getCriterias().add(criteria);

				break; // normales laden
			case 1: // neues criteria einfügen
				_criteria = new Criteria();
				_pdrQuery.getCriterias().add(_criteria);
				_criteria.setType("tagging"); //$NON-NLS-1$
				//			System.out.println("in case 1"); //$NON-NLS-1$
				//			System.out.println("anzahl crit " + pdrQuery.getCriterias().size()); //$NON-NLS-1$
				break;
			case 2: // criteria löschen
				_pdrQuery.getCriterias().removeElementAt(crit);
				break;
			case 3: // neue relation einfügen
				_criteria = new Criteria();
				_pdrQuery.getCriterias().add(_criteria);
				_criteria.setType("relation"); //$NON-NLS-1$
				break;
			case 4: // relation löschen
				_pdrQuery.getCriterias().removeElementAt(crit);
				break;
			case 5: // date criteria einfügen
				_criteria = new Criteria();
				_pdrQuery.getCriterias().add(_criteria);
				_criteria.setType("date"); //$NON-NLS-1$
				break;
			case 6:
				_pdrQuery.getCriterias().removeElementAt(crit);
				break;
			case 7: // reference criteria einfügen
				_criteria = new Criteria();
				_pdrQuery.getCriterias().add(_criteria);
				_criteria.setType("reference"); //$NON-NLS-1$
				break;
			case 8:
				_pdrQuery.getCriterias().removeElementAt(crit);
				break;
			default:
				break;
		}

		Collections.sort(_pdrQuery.getCriterias());

		// load last search.
		if (_facade.getLastExpertSearch() != null)
		{
			_pdrQuery = _facade.getLastExpertSearch();
		}

		for (int i = 0; i < _pdrQuery.getCriterias().size(); i++)
		{
			//			System.out.println("for i = " + i); //$NON-NLS-1$
			final Criteria c = _pdrQuery.getCriterias().get(i);

			if (c.getType().equals("tagging")) //$NON-NLS-1$
			{
				if (tag1)
				{
					tag1 = false;
					_searchPTagComp = new Composite(contentCompSearch, SWT.NONE);
					_searchPTagComp.setLayout(new GridLayout());
					((GridLayout) _searchPTagComp.getLayout()).makeColumnsEqualWidth = true;
					((GridLayout) _searchPTagComp.getLayout()).numColumns = 21;
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
					((GridData) op.getLayoutData()).horizontalSpan = 2;

					Label sem = new Label(_searchPTagComp, SWT.NONE);
					sem.setText(NLMessages.getString("Dialog_semantic")); //$NON-NLS-1$
					sem.setLayoutData(new GridData());
					((GridData) sem.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) sem.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) sem.getLayoutData()).horizontalSpan = 2;

					Label tagName = new Label(_searchPTagComp, SWT.NONE);
					tagName.setText(NLMessages.getString("Dialog_tagging")); //$NON-NLS-1$
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

					Label tagRole = new Label(_searchPTagComp, SWT.NONE);
					tagRole.setText(NLMessages.getString("Dialog_role")); //$NON-NLS-1$
					tagRole.setLayoutData(new GridData());
					((GridData) tagRole.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) tagRole.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) tagRole.getLayoutData()).horizontalSpan = 2;

					Label tagKey = new Label(_searchPTagComp, SWT.NONE);
					tagKey.setText(NLMessages.getString("Dialog_key")); //$NON-NLS-1$
					tagKey.setLayoutData(new GridData());
					((GridData) tagKey.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) tagKey.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) tagKey.getLayoutData()).horizontalSpan = 3;

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

					Label del = new Label(_searchPTagComp, SWT.NONE);
					del.setText(NLMessages.getString("Dialog_del")); //$NON-NLS-1$
					del.setLayoutData(new GridData());
					((GridData) del.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) del.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) del.getLayoutData()).horizontalSpan = 1;
				}

				if (i == 0)
				{
					Label l = new Label(_searchPTagComp, SWT.NONE);
					l.setLayoutData(new GridData());
					l.setLayoutData(new GridData());
					((GridData) l.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) l.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) l.getLayoutData()).horizontalSpan = 2;
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
					((GridData) opCombo.getLayoutData()).horizontalSpan = 2;
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

				final Combo roleCombo = new Combo(_searchPTagComp, SWT.READ_ONLY);
				final ComboViewer roleComboViewer = new ComboViewer(roleCombo);
				roleComboViewer.setContentProvider(new MarkupContentProvider());
				roleComboViewer.setLabelProvider(new MarkupLabelProvider());
				roleComboViewer.setComparator(new ConfigDataComparator());

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
						setComboViewerInput(roleComboViewer, "tagging_values", c.getCrit1(), c.getCrit2(), c.getCrit3()); //$NON-NLS-1$
					}
				});

				roleCombo.setLayoutData(new GridData());
				roleCombo.setLayoutData(new GridData());
				((GridData) roleCombo.getLayoutData()).horizontalAlignment = SWT.FILL;
				((GridData) roleCombo.getLayoutData()).grabExcessHorizontalSpace = true;
				((GridData) roleCombo.getLayoutData()).horizontalSpan = 2;
				if (c.getCrit4() != null)
				{
					setComboViewerByString(roleComboViewer, c.getCrit4());
				}
				roleComboViewer.addSelectionChangedListener(new ISelectionChangedListener()
				{
					@Override
					public void selectionChanged(final SelectionChangedEvent event)
					{
						ISelection iSelection = event.getSelection();
						Object obj = ((IStructuredSelection) iSelection).getFirstElement();
						ConfigData cd = (ConfigData) obj;
						if (cd != null)
						{
							c.setCrit4(cd.getValue());
						}
					}
				});

				final Text keyText = new Text(_searchPTagComp, SWT.BORDER);
				keyText.setLayoutData(new GridData());
				keyText.setLayoutData(new GridData());
				((GridData) keyText.getLayoutData()).horizontalAlignment = SWT.FILL;
				((GridData) keyText.getLayoutData()).grabExcessHorizontalSpace = true;
				((GridData) keyText.getLayoutData()).horizontalSpan = 2;
				if (c.getCrit5() != null)
				{
					keyText.setText(c.getCrit5());
				}
				keyText.setEnabled(false);

				final Button keySet = new Button(_searchPTagComp, SWT.PUSH);
				keySet.setEnabled(false);
				keySet.setText(NLMessages.getString("Dialog_set_key")); //$NON-NLS-1$
				keySet.setFont(JFaceResources.getDialogFont());
				keySet.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent event)
					{
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

				final Button delTagCriteria = new Button(_searchPTagComp, SWT.PUSH);
				delTagCriteria.setLayoutData(new GridData());
				delTagCriteria.setText("-"); //$NON-NLS-1$
				delTagCriteria.setData("tag", i); //$NON-NLS-1$

				delTagCriteria.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent event)
					{
						buildPersonSearch(2, ((Integer) delTagCriteria.getData("tag"))); //$NON-NLS-1$
					}
				});

			} // if tagging

			if (c.getType().equals("relation")) //$NON-NLS-1$
			{
				if (rel1)
				{
					rel1 = false;
					_searchPRelComp = new Composite(contentCompSearch, SWT.NONE);
					_searchPRelComp.setLayout(new GridLayout());
					((GridLayout) _searchPRelComp.getLayout()).makeColumnsEqualWidth = true;
					((GridLayout) _searchPRelComp.getLayout()).numColumns = 15;
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
					((GridData) l.getLayoutData()).horizontalSpan = 2;

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
					((GridData) l.getLayoutData()).horizontalSpan = 2;
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
					((GridData) opCombo.getLayoutData()).horizontalSpan = 2;
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
				// final Combo contextCombo = new Combo(searchPRelComp,
				// SWT.READ_ONLY);
				// contextCombo.setLayoutData(new GridData());
				// contextCombo.setLayoutData(new GridData());
				// ((GridData) contextCombo.getLayoutData()).horizontalAlignment
				// = SWT.FILL;
				// ((GridData)
				// contextCombo.getLayoutData()).grabExcessHorizontalSpace =
				// true ;
				// ((GridData) contextCombo.getLayoutData()).horizontalSpan = 2;
				//
				//
				// final Combo classCombo = new Combo(searchPRelComp,
				// SWT.READ_ONLY);
				// classCombo.setLayoutData(new GridData());
				// classCombo.setLayoutData(new GridData());
				// ((GridData) classCombo.getLayoutData()).horizontalAlignment =
				// SWT.FILL;
				// ((GridData)
				// classCombo.getLayoutData()).grabExcessHorizontalSpace = true;
				// ((GridData) classCombo.getLayoutData()).horizontalSpan = 2;
				//
				//
				// if (c.getRelationContext() != null)
				// {
				// contextCombo.select(contextCombo.indexOf(c.getRelationContext()));
				// }
				// else
				// {
				// try {
				//						contextCombo.setItems(mainSearcher.getFacets("relation", null, null, null, null)); //$NON-NLS-1$ //$NON-NLS-2$
				// } catch (XQException e1) {
				// e1.printStackTrace();
				// }
				//					contextCombo.add("ALL", 0); //$NON-NLS-1$
				// contextCombo.select(0);
				// c.setRelationContext(contextCombo.getItem(0));
				//
				// }
				// contextCombo.addSelectionListener(new SelectionAdapter(){
				// public void widgetSelected(SelectionEvent se){
				// String selection =
				// contextCombo.getItem(contextCombo.getSelectionIndex());
				// c.setRelationContext(selection);
				// try {
				// classCombo.setItems(mainSearcher
				//				.getFacets("relation", null, selection, null, null)); //$NON-NLS-1$ //$NON-NLS-2$
				// } catch (XQException e1) {
				// e1.printStackTrace();
				// }
				//						classCombo.add("ALL", 0); //$NON-NLS-1$
				// classCombo.select(0);
				// c.setRelationContext(classCombo.getItem(0));
				//
				// }
				// });
				//
				// if (c.getRelationClass() != null)
				// {
				// classCombo.setText(c.getRelationClass());
				// }
				// else
				// {
				//
				//					classCombo.add("ALL", 0); //$NON-NLS-1$
				// classCombo.select(0);
				// c.setRelationClass(classCombo.getItem(0));
				// }
				//
				//
				// classCombo.addSelectionListener(new SelectionAdapter(){
				// public void widgetSelected(SelectionEvent se){
				// String selection =
				// classCombo.getItem(classCombo.getSelectionIndex());
				// c.setRelationContext(selection);
				//
				// }
				// });
				//
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
					_searchPDateComp = new Composite(contentCompSearch, SWT.NONE);
					_searchPDateComp.setLayout(new GridLayout());
					((GridLayout) _searchPDateComp.getLayout()).makeColumnsEqualWidth = true;
					((GridLayout) _searchPDateComp.getLayout()).numColumns = 21;
					_searchPDateComp.setLayoutData(new GridData());
					((GridData) _searchPDateComp.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) _searchPDateComp.getLayoutData()).grabExcessHorizontalSpace = true;
					// ((GridData) searchPDateComp.getLayoutData()).heightHint =
					// 200;
					((GridData) _searchPDateComp.getLayoutData()).grabExcessVerticalSpace = false;
					((GridData) _searchPDateComp.getLayoutData()).horizontalSpan = 1;

					if (i != 0)
					{
						Label l2 = new Label(_searchPDateComp, SWT.NONE);
						l2.setLayoutData(new GridData());
						l2.setLayoutData(new GridData());
						((GridData) l2.getLayoutData()).horizontalAlignment = SWT.FILL;
						((GridData) l2.getLayoutData()).grabExcessHorizontalSpace = true;
						((GridData) l2.getLayoutData()).horizontalSpan = 2;
					}
					else
					{
						Label op = new Label(_searchPDateComp, SWT.NONE);
						op.setText(NLMessages.getString("Dialog_operand")); //$NON-NLS-1$
						op.setLayoutData(new GridData());
						((GridData) op.getLayoutData()).horizontalAlignment = SWT.FILL;
						((GridData) op.getLayoutData()).grabExcessHorizontalSpace = true;
						((GridData) op.getLayoutData()).horizontalSpan = 2;
					}

					Label typeDate = new Label(_searchPDateComp, SWT.NONE);
					typeDate.setText(NLMessages.getString("Dialog_type")); //$NON-NLS-1$
					typeDate.setLayoutData(new GridData());
					((GridData) typeDate.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) typeDate.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) typeDate.getLayoutData()).horizontalSpan = 3;

					Label bl = new Label(_searchPDateComp, SWT.NONE);
					bl.setText(""); //$NON-NLS-1$
					bl.setLayoutData(new GridData());
					((GridData) bl.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) bl.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) bl.getLayoutData()).horizontalSpan = 2;

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
					((GridData) year.getLayoutData()).horizontalSpan = 3;

					Label bl2 = new Label(_searchPDateComp, SWT.NONE);
					bl2.setText(""); //$NON-NLS-1$
					bl2.setLayoutData(new GridData());

					((GridData) bl2.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) bl2.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) bl2.getLayoutData()).horizontalSpan = 2;

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
					((GridData) year2.getLayoutData()).horizontalSpan = 3;

					if (i != 0)
					{
						Label l2 = new Label(_searchPDateComp, SWT.NONE);
						l2.setLayoutData(new GridData());
						l2.setLayoutData(new GridData());
						((GridData) l2.getLayoutData()).horizontalAlignment = SWT.FILL;
						((GridData) l2.getLayoutData()).grabExcessHorizontalSpace = true;
						((GridData) l2.getLayoutData()).horizontalSpan = 2;
					}
					else
					{
						Label include = new Label(_searchPDateComp, SWT.NONE);
						include.setText(NLMessages.getString("Dialog_include")); //$NON-NLS-1$
						include.setToolTipText(NLMessages.getString("Dialog_includeConcurrences")); //$NON-NLS-1$
						include.setLayoutData(new GridData());
						((GridData) include.getLayoutData()).horizontalAlignment = SWT.FILL;
						((GridData) include.getLayoutData()).grabExcessHorizontalSpace = true;
						((GridData) include.getLayoutData()).horizontalSpan = 1;

						Label del = new Label(_searchPDateComp, SWT.NONE);
						del.setText(NLMessages.getString("Dialog_del")); //$NON-NLS-1$
						del.setLayoutData(new GridData());
						((GridData) del.getLayoutData()).horizontalAlignment = SWT.FILL;
						((GridData) del.getLayoutData()).grabExcessHorizontalSpace = true;
						((GridData) del.getLayoutData()).horizontalSpan = 1;
					}

				}

				if (i == 0)
				{
					Label l = new Label(_searchPDateComp, SWT.NONE);
					l.setLayoutData(new GridData());
					((GridData) l.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) l.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) l.getLayoutData()).horizontalSpan = 2;
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
					((GridData) opCombo.getLayoutData()).horizontalSpan = 2;
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
				((GridData) from.getLayoutData()).horizontalSpan = 2;

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
				((GridData) to.getLayoutData()).horizontalSpan = 2;

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

				final Button delTagCriteria = new Button(_searchPDateComp, SWT.PUSH);
				delTagCriteria.setLayoutData(new GridData());
				delTagCriteria.setText("-"); //$NON-NLS-1$
				delTagCriteria.setData("tag", i); //$NON-NLS-1$

				delTagCriteria.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent event)
					{
						buildPersonSearch(2, ((Integer) delTagCriteria.getData("tag"))); //$NON-NLS-1$
					}
				});
			} // if date
			if (c.getType().equals("reference")) //$NON-NLS-1$
			{
				_searchPRefComp = new Composite(contentCompSearch, SWT.NONE);
				_searchPRefComp.setLayout(new GridLayout());
				((GridLayout) _searchPRefComp.getLayout()).makeColumnsEqualWidth = true;
				((GridLayout) _searchPRefComp.getLayout()).numColumns = 15;
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
				((GridData) opCombo.getLayoutData()).horizontalSpan = 2;
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
		contentCompSearch.layout();
		_scrollCompSearch.setContent(contentCompSearch);
		_scrollCompSearch.setMinSize(contentCompSearch.computeSize(SWT.DEFAULT, SWT.DEFAULT, true));
		_scrollCompSearch.layout();
		_personSearchGroup.redraw();
		_personSearchGroup.layout();
		_personSearchGroup.pack();
		_personSearchGroup.layout();
		// personSearchGroup.pack();

	}

	@Override
	public final void create()
	{
		super.create();
		// Set the title
		setTitle(NLMessages.getString("ExpertDialog_title")); //$NON-NLS-1$
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

		parent.setSize(450, 200);

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

		_pdrQuery = new PdrQuery();
		_criteria = new Criteria();
		_criteria.setType("tagging"); //$NON-NLS-1$

		_pdrQuery.setCriterias(new Vector<Criteria>());
		_pdrQuery.setType(1);

		_pdrQuery.getCriterias().add(_criteria);

		createPersonTabItem(_mainTabFolder);

		// parent.setLayout(layout);

		_mainTabFolder.setSelection(_facade.getRelObjTyp());
		parent.pack();
		return parent;
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
	 * meth creates the TabItem for selecting a person.
	 * @param mainTabFolder main tabFolder
	 */
	private void createPersonTabItem(final TabFolder mainTabFolder)
	{
		_markupProvider = Platform
				.getPreferencesService()
				.getString(CommonActivator.PLUGIN_ID,
						"PRIMARY_TAGGING_PROVIDER", AEConstants.CLASSIFICATION_AUTHORITY, null).toUpperCase(); //$NON-NLS-1$
		_relationProvider = Platform
				.getPreferencesService()
				.getString(CommonActivator.PLUGIN_ID, "PRIMARY_RELATION_PROVIDER",
						AEConstants.RELATION_CLASSIFICATION_PROVIDER, null).toUpperCase();

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
		((GridLayout) _personSearchGroup.getLayout()).numColumns = 4;

		Button addMarkupC = new Button(_personSearchGroup, SWT.PUSH);
		addMarkupC.setText(NLMessages.getString("Dialog_add_markup_crit")); //$NON-NLS-1$
		addMarkupC.setImage(_imageReg.get(IconsInternal.MARKUP));
		addMarkupC.setFont(JFaceResources.getDialogFont());
		addMarkupC.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent event)
			{
				buildPersonSearch(1, null);
			}
		});

		Button addRelC = new Button(_personSearchGroup, SWT.PUSH);
		addRelC.setText(NLMessages.getString("Dialog_add_rel_crit")); //$NON-NLS-1$
		addRelC.setImage(_imageReg.get(IconsInternal.RELATION));
		addRelC.setFont(JFaceResources.getDialogFont());
		addRelC.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent event)
			{
				buildPersonSearch(3, null);
			}
		});

		Button addDateC = new Button(_personSearchGroup, SWT.PUSH);
		addDateC.setText(NLMessages.getString("Dialog_add_date_crit")); //$NON-NLS-1$
		addDateC.setImage(_imageReg.get(IconsInternal.TIME));
		addDateC.setFont(JFaceResources.getDialogFont());
		addDateC.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent event)
			{
				buildPersonSearch(5, null);
			}
		});

		Button addRefC = new Button(_personSearchGroup, SWT.PUSH);
		addRefC.setText(NLMessages.getString("Dialog_add_ref_crit")); //$NON-NLS-1$
		addRefC.setImage(_imageReg.get(IconsInternal.REFERENCE));
		addRefC.setFont(JFaceResources.getDialogFont());
		addRefC.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent event)
			{
				buildPersonSearch(7, null);
			}
		});

		buildPersonSearch(0, null);

		// _searchPTagComp
		// personSearchGroup
		_personSearchGroup.layout();

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
		_facade.setLastExpertSearch(_pdrQuery);
		_facade.setAdvancedQuery(_pdrQuery);
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
	protected void setComboViewerInput(final ComboViewer comboViewer, final String facetType, final String crit1,
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
			for (String provider : providers)
			{
				if (_facade.getConfigs().get(provider).getChildren().containsKey(crit1))
				{
					configs.putAll(_facade.getConfigs().get(provider).getChildren().get(crit1).getChildren());
				}
			}
			//			System.out.println("markupprovider " + markupProvider); //$NON-NLS-1$
			if (_facade.getConfigs().containsKey(_markupProvider)
					&& _facade.getConfigs().get(_markupProvider).getChildren() != null
					&& _facade.getConfigs().get(_markupProvider).getChildren().containsKey("aodl:" + crit1)) //$NON-NLS-1$
			{
				configs.putAll(_facade.getConfigs().get(_markupProvider).getChildren()
						.get("aodl:" + crit1).getChildren()); //$NON-NLS-1$
			}

		}
		else if (crit1 != null && crit2 != null && crit3 == null)
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
		else if (crit1 != null && crit2 != null && crit3 != null)
		{
			try
			{
				values = _mainSearcher.getFacets(facetType, crit1, crit2, crit3, //$NON-NLS-1$
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
								.get("aodl:" + crit1).getChildren().containsKey(crit2) //$NON-NLS-1$
						&& _facade.getConfigs().get(provider).getChildren()
								.get("aodl:" + crit1).getChildren().get(crit2).getChildren() != null //$NON-NLS-1$
						&& _facade.getConfigs().get(provider).getChildren()
								.get("aodl:" + crit1).getChildren().get(crit2).getChildren().containsKey(crit3)) //$NON-NLS-1$
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
							.get("aodl:" + crit1).getChildren().containsKey(crit2) //$NON-NLS-1$
					&& _facade.getConfigs().get(_markupProvider).getChildren()
							.get("aodl:" + crit1).getChildren().get(crit2).getChildren() != null //$NON-NLS-1$
					&& _facade.getConfigs().get(_markupProvider).getChildren()
							.get("aodl:" + crit1).getChildren().get(crit2).getChildren().containsKey(crit3)) //$NON-NLS-1$
			{
				configs.putAll(_facade.getConfigs().get(_markupProvider).getChildren()
						.get("aodl:" + crit1).getChildren().get(crit2).getChildren().get(crit3).getChildren()); //$NON-NLS-1$
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

}
