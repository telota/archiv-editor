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
package org.bbaw.pdr.ae.view.control.dialogs;

import java.lang.reflect.InvocationTargetException;
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
import org.bbaw.pdr.ae.model.Aspect;
import org.bbaw.pdr.ae.model.Person;
import org.bbaw.pdr.ae.model.ReferenceMods;
import org.bbaw.pdr.ae.model.ReferenceModsTemplate;
import org.bbaw.pdr.ae.model.search.Criteria;
import org.bbaw.pdr.ae.model.search.Operator;
import org.bbaw.pdr.ae.model.search.PdrQuery;
import org.bbaw.pdr.ae.view.control.comparator.PdrObjectViewComparator;
import org.bbaw.pdr.ae.view.control.customSWTWidges.YearSpinner;
import org.bbaw.pdr.ae.view.control.provider.AEConfigPresentableContentProvider;
import org.bbaw.pdr.ae.view.control.provider.AEConfigPresentableLabelProvider;
import org.bbaw.pdr.ae.view.control.provider.AspectTableContentProvider;
import org.bbaw.pdr.ae.view.control.provider.ListLabelProvider;
import org.bbaw.pdr.ae.view.control.provider.MarkupContentProvider;
import org.bbaw.pdr.ae.view.control.provider.MarkupLabelProvider;
import org.bbaw.pdr.ae.view.control.provider.PersonVectorContentProvider;
import org.bbaw.pdr.ae.view.control.provider.RefTemplateContentProvider;
import org.bbaw.pdr.ae.view.control.provider.ReferenceTableContentProvider;
import org.bbaw.pdr.ae.view.control.provider.TableLabelProvider;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.fieldassist.AutoCompleteField;
import org.eclipse.jface.fieldassist.TextContentAdapter;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.progress.UIJob;

/**
 * @author cplutte Klasse erzeugt den Dialog zum Auswählen von Personen,
 *         Aspekten und Quellen zur Erzeugung von Verknüpfungen zwischen
 *         Aspekten und Personen etc.
 */
public class SelectObjectDialog extends TitleAreaDialog
{
	/** list for last persons, aspects, sources. */
	private List _lastAspectList;

	/** The last person list. */
	private List _lastPersonList;

	/** The _last reference list. */
	private List _lastReferenceList;
	/** singleton facade instance. */
	private Facade _facade = Facade.getInstanz();
	/** MainSearcher. */
	private AMainSearcher _mainSearcher = _facade.getMainSearcher();
	/** Instance of shared image registry. */
	private ImageRegistry _imageReg = CommonActivator.getDefault().getImageRegistry();

	/** The _person table viewer. */
	private TableViewer _personTableViewer;

	/** The _aspect table viewer. */
	private TableViewer _aspectTableViewer;

	/** The _reference table viewer. */
	private TableViewer _referenceTableViewer;

	/** The preselection. */
	private int _preselection = Platform.getPreferencesService().getInt(CommonActivator.PLUGIN_ID,
			"ASPECT_PRESELECTED_DATE_YEAR", AEConstants.ASPECT_PRESELECTED_DATE_YEAR, null); //$NON-NLS-1$

	/** The _person list viewer. */
	private ListViewer _personListViewer;

	/** The _selected aspect text. */
	private Text _selectedAspectText;

	/** The _selected person text. */
	private Text _selectedPersonText;

	/** The _selected ref text. */
	private Text _selectedRefText;
	/** selected person, aspect, source. */
	private Person _selectedP = null;

	/** The selected a. */
	private Aspect _selectedA = null;

	/** The selected r. */
	private ReferenceMods _selectedR = null;

	/** type of requested object 0 = aspect, 1 = person 2 = reference. */
	private int _type;

	/** The selection type. */
	private int _selectionType;

	/** The _person search group. */
	private Group _personSearchGroup;

	/** The _person result group. */
	private Group _personResultGroup;

	/** The _aspect search group. */
	private Group _aspectSearchGroup;

	/** The _ref search group. */
	private Group _refSearchGroup;

	/** layout elements. */
	private TabFolder _mainTabFolder;

	/** The aspect tab item. */
	private TabItem _aspectTabItem;

	/** The person tab item. */
	private TabItem _personTabItem;

	/** The source tab item. */
	private TabItem _sourceTabItem;

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

	/** The aspect composite. */
	private Composite _aspectComposite;

	/** The source composite. */
	private Composite _sourceComposite;

	/** The person main s form. */
	private SashForm _personMainSForm;

	/** The person search s form. */
	private SashForm _personSearchSForm;

	/** The aspect main s form. */
	private SashForm _aspectMainSForm;

	/** The aspect search s form. */
	private SashForm _aspectSearchSForm;

	/** The source main s form. */
	private SashForm _sourceMainSForm;

	/** The source search s form. */
	private SashForm _sourceSearchSForm;

	/** The person query. */
	private PdrQuery _personQuery;

	/** The aspect query. */
	private PdrQuery _aspectQuery;

	/** The reference query. */
	private PdrQuery _referenceQuery;

	/** The criteria. */
	private Criteria _criteria;

	/** The search p tag comp. */
	private Composite _searchPTagComp;

	/** The search p rel comp. */
	private Composite _searchPRelComp;

	/** The search p date comp. */
	private Composite _searchPDateComp;

	/** The search a tag comp. */
	private Composite _searchATagComp;

	/** The search a date comp. */
	private Composite _searchADateComp;

	/** The search p. */
	private Button _searchP;

	/** The search a. */
	private Button _searchA;

	/** The search r. */
	private Button _searchR;

	/** The _ok button. */
	private Button _okButton;

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
	 * Instantiates a new select object dialog.
	 * @param parentShell the parent shell
	 * @param type the type
	 */
	public SelectObjectDialog(final Shell parentShell, final int type)
	{
		super(parentShell);
		this._type = type;
		this._selectionType = type;
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
	 * Builds the aspect search.
	 */
	private void buildAspectSearch()
	{
		DataType dtAll = new DataType();
		dtAll.setValue("ALL");
		dtAll.setLabel("ALL");
		_aspectQuery = new PdrQuery();
		_aspectQuery.setType(0);
		_criteria = new Criteria();
		_criteria.setType("tagging"); //$NON-NLS-1$
		_aspectQuery.getCriterias().add(_criteria);
		final Criteria c = _aspectQuery.getCriterias().firstElement();

		_searchATagComp = new Composite(_aspectSearchGroup, SWT.NONE);
		_searchATagComp.setLayout(new GridLayout());
		((GridLayout) _searchATagComp.getLayout()).makeColumnsEqualWidth = true;
		((GridLayout) _searchATagComp.getLayout()).numColumns = 15;
		_searchATagComp.setLayoutData(new GridData());
		((GridData) _searchATagComp.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _searchATagComp.getLayoutData()).grabExcessHorizontalSpace = true;
		// ((GridData) searchPTagComp.getLayoutData()).heightHint = 200;
		((GridData) _searchATagComp.getLayoutData()).grabExcessVerticalSpace = false;
		((GridData) _searchATagComp.getLayoutData()).horizontalSpan = 1;

		Label sem = new Label(_searchATagComp, SWT.NONE);
		sem.setText(NLMessages.getString("Dialog_semantic"));
		sem.setLayoutData(new GridData());
		((GridData) sem.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) sem.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) sem.getLayoutData()).horizontalSpan = 2;

		Label tagName = new Label(_searchATagComp, SWT.NONE);
		tagName.setText(NLMessages.getString("Dialog_Markup"));
		tagName.setLayoutData(new GridData());
		((GridData) tagName.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) tagName.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) tagName.getLayoutData()).horizontalSpan = 2;

		Label tagType = new Label(_searchATagComp, SWT.NONE);
		tagType.setText(NLMessages.getString("Dialog_type"));
		tagType.setLayoutData(new GridData());
		((GridData) tagType.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) tagType.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) tagType.getLayoutData()).horizontalSpan = 2;

		Label tagSubtype = new Label(_searchATagComp, SWT.NONE);
		tagSubtype.setText(NLMessages.getString("Dialog_subtype"));
		tagSubtype.setLayoutData(new GridData());
		((GridData) tagSubtype.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) tagSubtype.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) tagSubtype.getLayoutData()).horizontalSpan = 2;

		Label searchTextLabel = new Label(_searchATagComp, SWT.NONE);
		searchTextLabel.setText(NLMessages.getString("Dialog_searchText"));
		searchTextLabel.setLayoutData(new GridData());
		((GridData) searchTextLabel.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) searchTextLabel.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) searchTextLabel.getLayoutData()).horizontalSpan = 5;

		Label fuzzy = new Label(_searchATagComp, SWT.NONE);
		fuzzy.setText(NLMessages.getString("Dialog_fuzzy"));
		fuzzy.setLayoutData(new GridData());
		((GridData) fuzzy.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) fuzzy.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) fuzzy.getLayoutData()).horizontalSpan = 1;

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
		semCombo.pack();
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
		final Combo typeCombo = new Combo(_searchATagComp, SWT.READ_ONLY);
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
				if (cd.getValue().startsWith("aodl:"))
				{
					selection = cd.getValue().substring(5);
				}
				else
				{
					selection = cd.getValue();
				}
				c.setCrit1(selection);
				setComboViewerInput(typeComboViewer, "tagging_values", c.getCrit1(), null, null);
			}
		});

		if (c.getCrit2() != null)
		{
			setComboViewerByString(typeComboViewer, c.getCrit2());
		}

		final Combo subtypeCombo = new Combo(_searchATagComp, SWT.READ_ONLY);
		final ComboViewer subtypeComboViewer = new ComboViewer(subtypeCombo);
		subtypeComboViewer.setContentProvider(new MarkupContentProvider(false));
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
				setComboViewerInput(subtypeComboViewer, "tagging_values", c.getCrit1(), c.getCrit2(), null);
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

		final Text searchText = new Text(_searchATagComp, SWT.BORDER);
		searchText.setLayoutData(new GridData());
		searchText.setLayoutData(new GridData());
		((GridData) searchText.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) searchText.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) searchText.getLayoutData()).horizontalSpan = 5;
		if (c.getSearchText() != null)
		{
			searchText.setText(c.getSearchText());
		}
		searchText.addFocusListener(new FocusListener()
		{
			@Override
			public void focusGained(final FocusEvent e)
			{
				String[] vals = new String[]
				{};
				try
				{
					vals = _mainSearcher.getFacets("tagging", c.getCrit1(), c.getCrit2(), null, null);
				}
				catch (Exception e1)
				{

					e1.printStackTrace();
				}
				new AutoCompleteField(searchText, new TextContentAdapter(), vals);

			}

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
					searchAspects();
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

		_searchADateComp = new Composite(_aspectSearchGroup, SWT.NONE);
		_searchADateComp.setLayout(new GridLayout());
		((GridLayout) _searchADateComp.getLayout()).makeColumnsEqualWidth = true;
		((GridLayout) _searchADateComp.getLayout()).numColumns = 13;
		_searchADateComp.setLayoutData(new GridData());
		((GridData) _searchADateComp.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _searchADateComp.getLayoutData()).grabExcessHorizontalSpace = true;
		// ((GridData) searchADateComp.getLayoutData()).heightHint = 200;
		((GridData) _searchADateComp.getLayoutData()).grabExcessVerticalSpace = false;
		((GridData) _searchADateComp.getLayoutData()).horizontalSpan = 1;

		Label typeDate = new Label(_searchADateComp, SWT.NONE);
		typeDate.setText(NLMessages.getString("Dialog_type"));
		typeDate.setLayoutData(new GridData());
		((GridData) typeDate.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) typeDate.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) typeDate.getLayoutData()).horizontalSpan = 2;

		Label bl = new Label(_searchADateComp, SWT.NONE);
		bl.setText(""); //$NON-NLS-1$
		Label notBefore = new Label(_searchADateComp, SWT.NONE);
		notBefore.setText(NLMessages.getString("Dialog_day"));
		notBefore.setLayoutData(new GridData());
		((GridData) notBefore.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) notBefore.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) notBefore.getLayoutData()).horizontalSpan = 1;

		Label month = new Label(_searchADateComp, SWT.NONE);
		month.setText(NLMessages.getString("Dialog_month"));
		month.setLayoutData(new GridData());
		((GridData) month.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) month.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) month.getLayoutData()).horizontalSpan = 1;

		Label year = new Label(_searchADateComp, SWT.NONE);
		year.setText(NLMessages.getString("Dialog_year"));
		year.setLayoutData(new GridData());
		((GridData) year.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) year.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) year.getLayoutData()).horizontalSpan = 2;

		Label bl2 = new Label(_searchADateComp, SWT.NONE);
		bl2.setText(""); //$NON-NLS-1$

		Label notAfter = new Label(_searchADateComp, SWT.NONE);
		notAfter.setText(NLMessages.getString("Dialog_day"));
		notAfter.setLayoutData(new GridData());
		((GridData) notAfter.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) notAfter.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) notAfter.getLayoutData()).horizontalSpan = 1;

		Label month2 = new Label(_searchADateComp, SWT.NONE);
		month2.setText(NLMessages.getString("Dialog_month"));
		month2.setLayoutData(new GridData());
		((GridData) month2.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) month2.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) month2.getLayoutData()).horizontalSpan = 1;

		Label year2 = new Label(_searchADateComp, SWT.NONE);
		year2.setText(NLMessages.getString("Dialog_year"));
		year2.setLayoutData(new GridData());
		((GridData) year2.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) year2.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) year2.getLayoutData()).horizontalSpan = 2;

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
		from.setText(NLMessages.getString("Dialog_from"));
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
		//			System.out.println("test: dateFrom " + c.getDateFrom().toString()); //$NON-NLS-1$
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
		// ((GridData) year1Spinner.getLayoutData()).horizontalAlignment =
		// SWT.FILL;
		// ((GridData) year1Spinner.getLayoutData()).grabExcessHorizontalSpace =
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

			}

			@Override
			public void widgetSelected(final SelectionEvent e)
			{
				c.getDateFrom().setYear(year1Spinner.getSelection());
			}

		});

		Label to = new Label(_searchADateComp, SWT.NONE);
		to.setText(NLMessages.getString("Dialog_to"));
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
		// ((GridData) year2Spinner.getLayoutData()).horizontalAlignment =
		// SWT.FILL;
		// ((GridData) year2Spinner.getLayoutData()).grabExcessHorizontalSpace =
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

	}

	/**
	 * Builds the person search.
	 */
	private void buildPersonSearch()
	{
		DataType dtAll = new DataType();
		dtAll.setValue("ALL");
		dtAll.setLabel("ALL");
		_personQuery = new PdrQuery();
		_personQuery.setType(1);
		_criteria = new Criteria();
		_criteria.setType("tagging"); //$NON-NLS-1$
		_personQuery.getCriterias().add(_criteria);
		_criteria = new Criteria();
		_criteria.setType("tagging"); //$NON-NLS-1$
		_personQuery.getCriterias().add(_criteria);

		_criteria = new Criteria();
		_personQuery.getCriterias().add(_criteria);
		_criteria.setType("relation"); //$NON-NLS-1$

		_criteria = new Criteria();
		_personQuery.getCriterias().add(_criteria);
		_criteria.setType("date"); //$NON-NLS-1$

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
					((GridLayout) _searchPTagComp.getLayout()).numColumns = 15;
					_searchPTagComp.setLayoutData(new GridData());
					((GridData) _searchPTagComp.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) _searchPTagComp.getLayoutData()).grabExcessHorizontalSpace = true;
					// ((GridData) searchPTagComp.getLayoutData()).heightHint =
					// 200;
					((GridData) _searchPTagComp.getLayoutData()).grabExcessVerticalSpace = false;
					((GridData) _searchPTagComp.getLayoutData()).horizontalSpan = 1;

					Label op = new Label(_searchPTagComp, SWT.NONE);
					op.setText(NLMessages.getString("Dialog_operand"));
					op.setLayoutData(new GridData());
					((GridData) op.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) op.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) op.getLayoutData()).horizontalSpan = 2;

					Label sem = new Label(_searchPTagComp, SWT.NONE);
					sem.setText(NLMessages.getString("Dialog_semantic"));
					sem.setLayoutData(new GridData());
					((GridData) sem.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) sem.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) sem.getLayoutData()).horizontalSpan = 2;

					Label tagName = new Label(_searchPTagComp, SWT.NONE);
					tagName.setText(NLMessages.getString("Dialog_markup"));
					tagName.setLayoutData(new GridData());
					((GridData) tagName.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) tagName.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) tagName.getLayoutData()).horizontalSpan = 2;

					Label tagType = new Label(_searchPTagComp, SWT.NONE);
					tagType.setText(NLMessages.getString("Dialog_type"));
					tagType.setLayoutData(new GridData());
					((GridData) tagType.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) tagType.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) tagType.getLayoutData()).horizontalSpan = 2;

					Label tagSubtype = new Label(_searchPTagComp, SWT.NONE);
					tagSubtype.setText(NLMessages.getString("Dialog_subtype"));
					tagSubtype.setLayoutData(new GridData());
					((GridData) tagSubtype.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) tagSubtype.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) tagSubtype.getLayoutData()).horizontalSpan = 2;

					Label searchTextLabel = new Label(_searchPTagComp, SWT.NONE);
					searchTextLabel.setText(NLMessages.getString("Dialog_searchText"));
					searchTextLabel.setLayoutData(new GridData());
					((GridData) searchTextLabel.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) searchTextLabel.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) searchTextLabel.getLayoutData()).horizontalSpan = 3;

					Label fuzzy = new Label(_searchPTagComp, SWT.NONE);
					fuzzy.setText(NLMessages.getString("Dialog_fuzzy"));
					fuzzy.setLayoutData(new GridData());
					((GridData) fuzzy.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) fuzzy.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) fuzzy.getLayoutData()).horizontalSpan = 1;

					Label include = new Label(_searchPTagComp, SWT.NONE);
					include.setText(NLMessages.getString("Dialog_include"));
					include.setToolTipText(NLMessages.getString("Dialog_includeConcurrences"));
					include.setLayoutData(new GridData());
					((GridData) include.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) include.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) include.getLayoutData()).horizontalSpan = 1;

				}

				if (i == 0)
				{
					Label l = new Label(_searchPTagComp, SWT.NONE);
					l.setText(NLMessages.getString("Dialog_Markup"));
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
						if (cd.getValue().startsWith("aodl:"))
						{
							selection = cd.getValue().substring(5);
						}
						else
						{
							selection = cd.getValue();
						}
						c.setCrit1(selection);
						setComboViewerInput(typeComboViewer, "tagging_values", c.getCrit1(), null, null);
					}
				});

				if (c.getCrit2() != null)
				{
					setComboViewerByString(typeComboViewer, c.getCrit2());
				}

				final Combo subtypeCombo = new Combo(_searchPTagComp, SWT.READ_ONLY);
				final ComboViewer subtypeComboViewer = new ComboViewer(subtypeCombo);
				subtypeComboViewer.setContentProvider(new MarkupContentProvider(false));
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
						setComboViewerInput(subtypeComboViewer, "tagging_values", c.getCrit1(), c.getCrit2(), null);
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
				searchText.addFocusListener(new FocusListener()
				{
					@Override
					public void focusGained(final FocusEvent e)
					{
						String[] vals = new String[]
						{};
						try
						{
							vals = _mainSearcher.getFacets("tagging", c.getCrit1(), c.getCrit2(), null, null);
						}
						catch (Exception e1)
						{

							e1.printStackTrace();
						}
						new AutoCompleteField(searchText, new TextContentAdapter(), vals);

					}

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
							searchPersons();
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
					((GridLayout) _searchPRelComp.getLayout()).numColumns = 15;
					_searchPRelComp.setLayoutData(new GridData());
					((GridData) _searchPRelComp.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) _searchPRelComp.getLayoutData()).grabExcessHorizontalSpace = true;
					// ((GridData) searchPRelComp.getLayoutData()).heightHint =
					// 200;
					((GridData) _searchPRelComp.getLayoutData()).grabExcessVerticalSpace = false;
					((GridData) _searchPRelComp.getLayoutData()).horizontalSpan = 1;

					Label l = new Label(_searchPRelComp, SWT.NONE);
					l.setText(NLMessages.getString("Dialog_relation"));
					l.setLayoutData(new GridData());
					((GridData) l.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) l.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) l.getLayoutData()).horizontalSpan = 2;

					Label context = new Label(_searchPRelComp, SWT.NONE);
					context.setText(NLMessages.getString("Dialog_context"));
					context.setLayoutData(new GridData());
					((GridData) context.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) context.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) context.getLayoutData()).horizontalSpan = 2;

					Label classL = new Label(_searchPRelComp, SWT.NONE);
					classL.setText(NLMessages.getString("Dialog_class"));
					classL.setLayoutData(new GridData());
					((GridData) classL.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) classL.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) classL.getLayoutData()).horizontalSpan = 2;

					Label relObj = new Label(_searchPRelComp, SWT.NONE);
					relObj.setText(NLMessages.getString("Dialog_relObject"));
					relObj.setLayoutData(new GridData());
					((GridData) relObj.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) relObj.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) relObj.getLayoutData()).horizontalSpan = 4;

					Label searchTextLabel = new Label(_searchPRelComp, SWT.NONE);
					searchTextLabel.setText(NLMessages.getString("Dialog_searchText"));
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
					include.setToolTipText(NLMessages.getString("Dialog_includeConcurrences"));
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
				// contextCombo.setItems(_mainSearcher.getFacets
				//				("relation", null, null, null, null)); //$NON-NLS-1$ //$NON-NLS-2$
				// } catch (Exception e1) {
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
				// classCombo.setItems(_mainSearcher
				//				.getFacets("relation", null, selection, null, null)); //$NON-NLS-1$ //$NON-NLS-2$
				// } catch (Exception e1) {
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
				final Combo contextCombo = new Combo(_searchPRelComp, SWT.READ_ONLY);
				contextCombo.setLayoutData(new GridData());
				contextCombo.setLayoutData(new GridData());
				((GridData) contextCombo.getLayoutData()).horizontalAlignment = SWT.FILL;
				((GridData) contextCombo.getLayoutData()).grabExcessHorizontalSpace = true;
				((GridData) contextCombo.getLayoutData()).horizontalSpan = 2;
				final ComboViewer contextComboViewer = new ComboViewer(contextCombo);
				contextComboViewer.setContentProvider(new MarkupContentProvider(false));
				contextComboViewer.setLabelProvider(new MarkupLabelProvider());
				contextComboViewer.setComparator(new ConfigDataComparator());

				final Combo classCombo = new Combo(_searchPRelComp, SWT.READ_ONLY);
				classCombo.setLayoutData(new GridData());
				classCombo.setLayoutData(new GridData());
				((GridData) classCombo.getLayoutData()).horizontalAlignment = SWT.FILL;
				((GridData) classCombo.getLayoutData()).grabExcessHorizontalSpace = true;
				((GridData) classCombo.getLayoutData()).horizontalSpan = 2;
				final ComboViewer classComboViewer = new ComboViewer(classCombo);
				classComboViewer.setContentProvider(new MarkupContentProvider(false));
				classComboViewer.setLabelProvider(new MarkupLabelProvider());
				classComboViewer.setComparator(new ConfigDataComparator());
				setComboViewerInput(contextComboViewer, "relation", null, null, null);
				if (c.getRelationContext() != null)
				{
					setComboViewerByString(contextComboViewer, c.getRelationContext());
					setComboViewerInput(classComboViewer, "relation", c.getRelationContext(), null, null);

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
						setComboViewerInput(classComboViewer, "relation", c.getRelationContext(), null, null);
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
				relObjText.addFocusListener(new FocusListener()
				{

					@Override
					public void focusGained(final FocusEvent e)
					{
					}

					@Override
					public void focusLost(final FocusEvent e)
					{

					}

				});

				final Button setObj = new Button(_searchPRelComp, SWT.PUSH);
				setObj.setText(NLMessages.getString("Dialog_set_key"));
				setObj.setEnabled(false);
				setObj.setFont(JFaceResources.getDialogFont());
				setObj.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent event)
					{
						//						System.out.println("setObj"); //$NON-NLS-1$

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
							searchPersons();
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
					((GridLayout) _searchPDateComp.getLayout()).numColumns = 15;
					_searchPDateComp.setLayoutData(new GridData());
					((GridData) _searchPDateComp.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) _searchPDateComp.getLayoutData()).grabExcessHorizontalSpace = true;
					// ((GridData) searchPDateComp.getLayoutData()).heightHint =
					// 200;
					((GridData) _searchPDateComp.getLayoutData()).grabExcessVerticalSpace = false;
					((GridData) _searchPDateComp.getLayoutData()).horizontalSpan = 1;

					Label l2 = new Label(_searchPDateComp, SWT.NONE);
					l2.setText(NLMessages.getString("Dialog_date"));
					l2.setLayoutData(new GridData());
					((GridData) l2.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) l2.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) l2.getLayoutData()).horizontalSpan = 2;

					Label typeDate = new Label(_searchPDateComp, SWT.NONE);
					typeDate.setText(NLMessages.getString("Dialog_type"));
					typeDate.setLayoutData(new GridData());
					((GridData) typeDate.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) typeDate.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) typeDate.getLayoutData()).horizontalSpan = 2;

					Label bl = new Label(_searchPDateComp, SWT.NONE);
					bl.setText(""); //$NON-NLS-1$
					Label notBefore = new Label(_searchPDateComp, SWT.NONE);
					notBefore.setText(NLMessages.getString("Dialog_day"));
					notBefore.setLayoutData(new GridData());
					((GridData) notBefore.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) notBefore.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) notBefore.getLayoutData()).horizontalSpan = 1;

					Label month = new Label(_searchPDateComp, SWT.NONE);
					month.setText(NLMessages.getString("Dialog_month"));
					month.setLayoutData(new GridData());
					((GridData) month.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) month.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) month.getLayoutData()).horizontalSpan = 1;

					Label year = new Label(_searchPDateComp, SWT.NONE);
					year.setText(NLMessages.getString("Dialog_year"));
					year.setLayoutData(new GridData());
					((GridData) year.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) year.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) year.getLayoutData()).horizontalSpan = 2;

					Label bl2 = new Label(_searchPDateComp, SWT.NONE);
					bl2.setText(""); //$NON-NLS-1$

					Label notAfter = new Label(_searchPDateComp, SWT.NONE);
					notAfter.setText(NLMessages.getString("Dialog_day"));
					notAfter.setLayoutData(new GridData());
					((GridData) notAfter.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) notAfter.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) notAfter.getLayoutData()).horizontalSpan = 1;

					Label month2 = new Label(_searchPDateComp, SWT.NONE);
					month2.setText(NLMessages.getString("Dialog_month"));
					month2.setLayoutData(new GridData());
					((GridData) month2.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) month2.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) month2.getLayoutData()).horizontalSpan = 1;

					Label year2 = new Label(_searchPDateComp, SWT.NONE);
					year2.setText(NLMessages.getString("Dialog_year"));
					year2.setLayoutData(new GridData());
					((GridData) year2.getLayoutData()).horizontalAlignment = SWT.FILL;
					((GridData) year2.getLayoutData()).grabExcessHorizontalSpace = true;
					((GridData) year2.getLayoutData()).horizontalSpan = 2;

					Label include = new Label(_searchPDateComp, SWT.NONE);
					include.setText(""); //$NON-NLS-1$
					include.setToolTipText(NLMessages.getString("Dialog_includeConcurrences"));
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
				from.setText(NLMessages.getString("Dialog_from"));
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

					}

					@Override
					public void widgetSelected(final SelectionEvent e)
					{
						c.getDateFrom().setYear(year1Spinner.getSelection());
					}

				});

				Label to = new Label(_searchPDateComp, SWT.NONE);
				to.setText(NLMessages.getString("Dialog_to"));
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

				final YearSpinner year2Spinner = new YearSpinner(_searchPDateComp, SWT.NONE);
				year2Spinner.setLayoutData(new GridData());
				year2Spinner.setLayoutData(new GridData());
				((GridData) year2Spinner.getLayoutData()).horizontalAlignment = SWT.FILL;
				((GridData) year2Spinner.getLayoutData()).grabExcessHorizontalSpace = true;
				((GridData) year2Spinner.getLayoutData()).horizontalSpan = 1;
				year2Spinner.setMinimum(0);
				year2Spinner.setMaximum(9999);
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
		}
	}

	/**
	 * Builds the reference search.
	 */
	private void buildReferenceSearch()
	{
		_referenceQuery = new PdrQuery();
		_referenceQuery.setType(2);
		_criteria = new Criteria();
		_criteria.setType("reference"); //$NON-NLS-1$
		_referenceQuery.getCriterias().add(_criteria);

		final Criteria c = _referenceQuery.getCriterias().firstElement();
		Composite searchRefComp = new Composite(_refSearchGroup, SWT.NONE);
		searchRefComp.setLayout(new GridLayout());
		((GridLayout) searchRefComp.getLayout()).makeColumnsEqualWidth = true;
		((GridLayout) searchRefComp.getLayout()).numColumns = 10;
		searchRefComp.setLayoutData(new GridData());
		((GridData) searchRefComp.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) searchRefComp.getLayoutData()).grabExcessHorizontalSpace = true;
		// ((GridData) searchRefComp.getLayoutData()).heightHint = 200;
		((GridData) searchRefComp.getLayoutData()).grabExcessVerticalSpace = false;
		((GridData) searchRefComp.getLayoutData()).horizontalSpan = 1;

		Label op = new Label(searchRefComp, SWT.NONE);
		op.setText(NLMessages.getString("Dialog_genre"));
		op.setLayoutData(new GridData());
		((GridData) op.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) op.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) op.getLayoutData()).horizontalSpan = 2;

		Label tagName = new Label(searchRefComp, SWT.NONE);
		tagName.setText(NLMessages.getString("Dialog_role"));
		tagName.setLayoutData(new GridData());
		((GridData) tagName.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) tagName.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) tagName.getLayoutData()).horizontalSpan = 2;

		Label tagType = new Label(searchRefComp, SWT.NONE);
		tagType.setText(NLMessages.getString("Dialog_name"));
		tagType.setLayoutData(new GridData());
		((GridData) tagType.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) tagType.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) tagType.getLayoutData()).horizontalSpan = 5;

		Label fuzzy = new Label(searchRefComp, SWT.NONE);
		fuzzy.setText(NLMessages.getString("Dialog_fuzzy"));
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
					return str;
				}
				return NLMessages.getString("Editor_role_" + str);
			}
		});

		String[] input = new String[AEConstants.REF_ROLETERM_CODE.length + 1];
		System.arraycopy(new String[]
		{"ALL"}, 0, input, 0, 1);
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
					vals = _mainSearcher.getFacets("reference", "namePart", null, null, null);
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
					searchReferences();
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
		title.setText(NLMessages.getString("Dialog_refTitle"));
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
					vals = _mainSearcher.getFacets("reference", "title", null, null, null);
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
					searchReferences();
				}
			}

			@Override
			public void keyReleased(final KeyEvent e)
			{
			}
		});
		Label searchText = new Label(searchRefComp, SWT.NONE);
		searchText.setText(NLMessages.getString("Dialog_freeSearch"));
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
					searchReferences();
				}
			}

			@Override
			public void keyReleased(final KeyEvent e)
			{
			}
		});

	}

	@Override
	public final void create()
	{
		super.create();
		// Set the title
		setTitle(NLMessages.getString("ObjectDialog_title")); //$NON-NLS-1$
		// Set the message
		setMessage(NLMessages.getString("Dialog_firstMessage"), IMessageProvider.INFORMATION); //$NON-NLS-1$

	}

	/**
	 * meth creates the TabItem for selecting an aspect.
	 * @param mainTabFolder main tabFolder
	 */
	private void createAspectTabItem(final TabFolder mainTabFolder)
	{
		_aspectTabItem = new TabItem(mainTabFolder, SWT.NONE);
		_aspectTabItem.setText(NLMessages.getString("Dialog_aspects")); //$NON-NLS-1$
		_aspectTabItem.setImage(_imageReg.get(IconsInternal.ASPECTS));
		_aspectComposite = new Composite(mainTabFolder, SWT.NONE);
		_aspectComposite.setLayout(new GridLayout());
		_aspectComposite.setLayoutData(new GridData());
		((GridData) _aspectComposite.getLayoutData()).verticalAlignment = SWT.FILL;

		_aspectTabItem.setControl(_aspectComposite);

		_aspectMainSForm = new SashForm(_aspectComposite, SWT.HORIZONTAL);
		_aspectMainSForm.setLayoutData(new GridData(GridData.FILL_BOTH));
		((GridData) _aspectMainSForm.getLayoutData()).minimumHeight = 500;
		((GridData) _aspectMainSForm.getLayoutData()).minimumWidth = 700;

		_aspectSearchSForm = new SashForm(_aspectMainSForm, SWT.VERTICAL);
		_aspectSearchSForm.setLayoutData(new GridData(GridData.FILL_BOTH));
		_aspectSearchGroup = new Group(_aspectSearchSForm, SWT.SHADOW_IN);
		_aspectSearchGroup.setText(NLMessages.getString("Dialog_aspectSearch")); //$NON-NLS-1$
		_aspectSearchGroup.setLayoutData(new GridData());
		((GridData) _aspectSearchGroup.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _aspectSearchGroup.getLayoutData()).verticalAlignment = SWT.FILL;
		((GridData) _aspectSearchGroup.getLayoutData()).minimumHeight = 60;
		((GridData) _aspectSearchGroup.getLayoutData()).grabExcessHorizontalSpace = true;
		_aspectSearchGroup.setLayout(_gridLayout2);
		buildAspectSearch();
		_searchA = new Button(_aspectSearchGroup, SWT.PUSH);
		_searchA.setText(NLMessages.getString("Dialog_search")); //$NON-NLS-1$
		_searchA.setImage(_imageReg.get(IconsInternal.SEARCH));
		_searchA.setFont(JFaceResources.getDialogFont());
		_searchA.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent event)
			{
				searchAspects();

			}

		});
		// aspectSearchGroup
		_aspectSearchGroup.pack();
		Group aspectResultGroup = new Group(_aspectSearchSForm, SWT.SHADOW_IN);
		aspectResultGroup.setText(NLMessages.getString("Dialog_result")); //$NON-NLS-1$
		aspectResultGroup.setLayoutData(new GridData());
		((GridData) aspectResultGroup.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) aspectResultGroup.getLayoutData()).verticalAlignment = SWT.FILL;
		((GridData) aspectResultGroup.getLayoutData()).minimumHeight = 100;
		((GridData) aspectResultGroup.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) aspectResultGroup.getLayoutData()).grabExcessVerticalSpace = true;
		aspectResultGroup.setLayout(_gridLayout2);
		_aspectTableViewer = new TableViewer(aspectResultGroup, SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
		String[] titles =
		{NLMessages.getString("Dialog_displayName"), NLMessages.getString("Dialog_semantic"),
				NLMessages.getString("Dialog_id")};
		int[] bounds =
		{300, 180, 180};
		for (int i = 0; i < titles.length; i++)
		{
			TableViewerColumn column = new TableViewerColumn(_aspectTableViewer, SWT.NONE);
			column.getColumn().setText(titles[i]);
			column.getColumn().setWidth(bounds[i]);
			column.getColumn().setResizable(true);
			column.getColumn().setMoveable(true);
			column.getColumn().addSelectionListener(getSelectionAdapter(_aspectTableViewer, column.getColumn(), i));

		}
		Table table = _aspectTableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setLayoutData(new GridData());
		((GridData) table.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) table.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) table.getLayoutData()).verticalAlignment = SWT.FILL;
		((GridData) table.getLayoutData()).grabExcessVerticalSpace = true;
		_aspectTableViewer.addDoubleClickListener(new IDoubleClickListener()
		{
			@Override
			public void doubleClick(final DoubleClickEvent event)
			{
				ISelection selection = _aspectTableViewer.getSelection();
				Object obj = ((IStructuredSelection) selection).getFirstElement();
				_selectedA = (Aspect) obj;
				_selectedAspectText.setText(_selectedA.getNotification());
			}
		});
		Label selectLabel = new Label(aspectResultGroup, SWT.None);
		selectLabel.setText(NLMessages.getString("Dialog_selection"));
		_selectedAspectText = new Text(aspectResultGroup, SWT.SHADOW_IN | SWT.WRAP | SWT.READ_ONLY);
		_selectedAspectText.setLayoutData(new GridData());
		((GridData) _selectedAspectText.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _selectedAspectText.getLayoutData()).verticalAlignment = SWT.FILL;
		((GridData) _selectedAspectText.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) _selectedAspectText.getLayoutData()).minimumHeight = 30;
		_selectedAspectText.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		_aspectSearchSForm.setWeights(new int[]
		{2, 2});
		Group lastAspectsGroup = new Group(_aspectMainSForm, SWT.SHADOW_IN);
		lastAspectsGroup.setText(NLMessages.getString("Dialog_lastAspects")); //$NON-NLS-1$
		lastAspectsGroup.setLayoutData(new GridData());
		((GridData) lastAspectsGroup.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) lastAspectsGroup.getLayoutData()).verticalAlignment = SWT.FILL;
		((GridData) lastAspectsGroup.getLayoutData()).minimumHeight = 120;
		((GridData) lastAspectsGroup.getLayoutData()).grabExcessHorizontalSpace = true;
		lastAspectsGroup.setLayout(_gridLayout2);
		_lastAspectList = new List(lastAspectsGroup, SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		_lastAspectList.setLayoutData(new GridData());
		((GridData) _lastAspectList.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _lastAspectList.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) _lastAspectList.getLayoutData()).verticalAlignment = SWT.FILL;
		((GridData) _lastAspectList.getLayoutData()).grabExcessVerticalSpace = true;
		final Vector<Aspect> lastAspects = _facade.getLastAspects();
		if (_facade.getLastAspects() != null && !_facade.getLastAspects().isEmpty())
		{
			for (int i = lastAspects.size() - 1; i > -1; i--)
			{
				_lastAspectList.add(lastAspects.get(i).getDisplayNameWithID());
			}
		}
		_lastAspectList.addSelectionListener(new SelectionListener()
		{
			@Override
			public void widgetDefaultSelected(final SelectionEvent event)
			{
				// int[] selectedItems =
				// lastAspectList.getSelectionIndices();
				//				        String outString = ""; //$NON-NLS-1$
				// for (int loopIndex = 0; loopIndex <
				// selectedItems.length; loopIndex++)
				// {
				//							outString += selectedItems[loopIndex] + " "; //$NON-NLS-1$
				// }
				//				        System.out.println("Selected Items: " + outString); //$NON-NLS-1$
			}

			@Override
			public void widgetSelected(final SelectionEvent event)
			{
				int si = lastAspects.size() - _lastAspectList.getSelectionIndex() - 1;
				String selected = NLMessages.getString("Dialog_aspect") //$NON-NLS-1$
						+ lastAspects.get(si).getSemanticDim().getSemanticStms().get(0).getLabel()
						+ NLMessages.getString("Dialog_ID") //$NON-NLS-1$
						+ lastAspects.get(si).getPdrId().toString();
				// System.out.println(selected);
				//						System.out.println("index " + si); //$NON-NLS-1$
				setMessage(selected);
				_selectedA = lastAspects.get(si);
				_selectedAspectText.setText(_selectedA.getNotification());

				// delete possible person selection
				_lastPersonList.deselectAll();
				_lastReferenceList.deselectAll();

			}
		});
		_aspectMainSForm.setWeights(new int[]
		{3, 1});
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
		createOkButton(parent, OK, NLMessages.getString("Dialog_save"), true); //$NON-NLS-1$
		// Add a SelectionListener

		// Create Cancel button
		Button cancelButton = createButton(parent, CANCEL, NLMessages.getString("Dialog_cancel"), false); //$NON-NLS-1$
		// Add a SelectionListener
		cancelButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent e)
			{
				_facade.setRequestedId(null);
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

		createAspectTabItem(_mainTabFolder);
		createPersonTabItem(_mainTabFolder);
		createSourceTabItem(_mainTabFolder);
		_mainTabFolder.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent e)
			{
				_selectionType = _mainTabFolder.getSelectionIndex();
				//		    	  System.out.println("maintabfolder selection " + mainTabFolder.getSelectionIndex()); //$NON-NLS-1$

				switch (_selectionType)
				{
					case 0:
						_searchA.setFocus();
						break;
					case 1:
						_searchP.setFocus();
						break;
					case 2:
						_searchR.setFocus();
						break;
					default:
						break;
				}
			}
		});
		// parent.setLayout(layout);

		_mainTabFolder.setSelection(_type);
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
		_okButton = new Button(parent, SWT.PUSH);
		_okButton.setText(label);
		_okButton.setFont(JFaceResources.getDialogFont());
		_okButton.setData(new Integer(id));
		_okButton.addSelectionListener(new SelectionAdapter()
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

		setButtonLayoutData(_okButton);
		return _okButton;
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
		_personTabItem.setText(NLMessages.getString("Dialog_persons")); //$NON-NLS-1$
		_personTabItem.setImage(_imageReg.get(IconsInternal.PERSONS));
		_personComposite = new Composite(mainTabFolder, SWT.NONE);
		_personComposite.setLayout(new GridLayout());
		_personComposite.setLayoutData(new GridData());
		((GridData) _personComposite.getLayoutData()).verticalAlignment = SWT.FILL;

		_personTabItem.setControl(_personComposite);

		_personMainSForm = new SashForm(_personComposite, SWT.HORIZONTAL);
		_personMainSForm.setLayoutData(new GridData(GridData.FILL_BOTH));
		((GridData) _personMainSForm.getLayoutData()).minimumHeight = 500;
		((GridData) _personMainSForm.getLayoutData()).minimumWidth = 700;

		_personSearchSForm = new SashForm(_personMainSForm, SWT.VERTICAL);
		_personSearchSForm.setLayoutData(new GridData(GridData.FILL_BOTH));
		_personSearchGroup = new Group(_personSearchSForm, SWT.SHADOW_IN);
		_personSearchGroup.setText(NLMessages.getString("Dialog_personSearch")); //$NON-NLS-1$
		_personSearchGroup.setLayoutData(new GridData());
		((GridData) _personSearchGroup.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _personSearchGroup.getLayoutData()).verticalAlignment = SWT.FILL;
		((GridData) _personSearchGroup.getLayoutData()).minimumHeight = 90;
		((GridData) _personSearchGroup.getLayoutData()).grabExcessHorizontalSpace = true;
		_personSearchGroup.setLayout(new GridLayout());
		((GridLayout) _personSearchGroup.getLayout()).makeColumnsEqualWidth = false;
		((GridLayout) _personSearchGroup.getLayout()).numColumns = 1;
		buildPersonSearch();
		_searchP = new Button(_personSearchGroup, SWT.PUSH);
		_searchP.setText(NLMessages.getString("Dialog_search")); //$NON-NLS-1$
		_searchP.setImage(_imageReg.get(IconsInternal.SEARCH));
		_searchP.setFont(JFaceResources.getDialogFont());
		_searchP.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent event)
			{
				//					System.out.println("suchen"); //$NON-NLS-1$
				searchPersons();

			}

		});
		// personSearchGroup
		_personSearchGroup.pack();
		_personResultGroup = new Group(_personSearchSForm, SWT.SHADOW_IN);
		_personResultGroup.setText(NLMessages.getString("Dialog_result")); //$NON-NLS-1$
		_personResultGroup.setLayoutData(new GridData());
		((GridData) _personResultGroup.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _personResultGroup.getLayoutData()).verticalAlignment = SWT.FILL;
		((GridData) _personResultGroup.getLayoutData()).minimumHeight = 90;
		((GridData) _personResultGroup.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) _personResultGroup.getLayoutData()).grabExcessVerticalSpace = true;
		_personResultGroup.setLayout(_gridLayout2);
		_personTableViewer = new TableViewer(_personResultGroup, SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
		String[] titles =
		{NLMessages.getString("Dialog_displayName"), NLMessages.getString("Dialog_id")};
		int[] bounds =
		{300, 180};
		for (int i = 0; i < titles.length; i++)
		{
			TableViewerColumn column = new TableViewerColumn(_personTableViewer, SWT.NONE);
			column.getColumn().setText(titles[i]);
			column.getColumn().setWidth(bounds[i]);
			column.getColumn().setResizable(true);
			column.getColumn().setMoveable(true);
			column.getColumn().addSelectionListener(getSelectionAdapter(_personTableViewer, column.getColumn(), i));

		}
		_personTableViewer.setComparator(new PdrObjectViewComparator());
		Table table = _personTableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setLayoutData(new GridData());
		((GridData) table.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) table.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) table.getLayoutData()).verticalAlignment = SWT.FILL;
		((GridData) table.getLayoutData()).grabExcessVerticalSpace = true;
		_personTableViewer.addDoubleClickListener(new IDoubleClickListener()
		{
			@Override
			public void doubleClick(final DoubleClickEvent event)
			{
				ISelection selection = _personTableViewer.getSelection();
				Object obj = ((IStructuredSelection) selection).getFirstElement();
				_selectedP = (Person) obj;
				_selectedPersonText.setText(_selectedP.getDisplayName());
			}
		});
		Label selectLabel = new Label(_personResultGroup, SWT.None);
		selectLabel.setText(NLMessages.getString("Dialog_selection"));
		_selectedPersonText = new Text(_personResultGroup, SWT.SHADOW_IN | SWT.WRAP | SWT.READ_ONLY);
		_selectedPersonText.setLayoutData(new GridData());
		((GridData) _selectedPersonText.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _selectedPersonText.getLayoutData()).verticalAlignment = SWT.FILL;
		((GridData) _selectedPersonText.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) _selectedPersonText.getLayoutData()).minimumHeight = 30;
		_selectedPersonText.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		// }// personResultGroup
		_personSearchSForm.setWeights(new int[]
		{4, 2});
		Group lastPersonsGroup = new Group(_personMainSForm, SWT.SHADOW_IN);
		lastPersonsGroup.setText(NLMessages.getString("Dialog_lastPersons")); //$NON-NLS-1$
		lastPersonsGroup.setLayoutData(new GridData());
		((GridData) lastPersonsGroup.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) lastPersonsGroup.getLayoutData()).verticalAlignment = SWT.FILL;
		((GridData) lastPersonsGroup.getLayoutData()).minimumHeight = 120;
		((GridData) lastPersonsGroup.getLayoutData()).grabExcessHorizontalSpace = true;
		lastPersonsGroup.setLayout(_gridLayout2);
		_lastPersonList = new List(lastPersonsGroup, SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		_lastPersonList.setLayoutData(new GridData());
		((GridData) _lastPersonList.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _lastPersonList.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) _lastPersonList.getLayoutData()).verticalAlignment = SWT.FILL;
		((GridData) _lastPersonList.getLayoutData()).grabExcessVerticalSpace = true;
		_personListViewer = new ListViewer(_lastPersonList);
		_personListViewer.setContentProvider(new PersonVectorContentProvider());
		_personListViewer.setLabelProvider(new ListLabelProvider());
		try
		{
			if (_facade.getLastPersons() != null && !_facade.getLastPersons().isEmpty()
					&& _facade.getLastPersons().size() > 2)
			{

				_personListViewer.setInput(_facade.getLastPersons());
				_personListViewer.addSelectionChangedListener(new ISelectionChangedListener()
				{

					@Override
					public void selectionChanged(final SelectionChangedEvent event)
					{
						ISelection selection = _personListViewer.getSelection();
						Object obj = ((IStructuredSelection) selection).getFirstElement();
						_selectedP = (Person) obj;
						_selectedPersonText.setText(_selectedP.getDisplayNameWithID());
						_lastAspectList.deselectAll();
						_lastReferenceList.deselectAll();

					}
				});
			}

		}
		finally
		{
		}
		// _personListViewer.addSelectionListener(new
		// SelectionListener() {
		// public void widgetSelected(final SelectionEvent event) {
		// int si = lastPersons.size() -
		// lastPersonList.getSelectionIndex() - 1;
		//				    	System.out.println("lastpersonlist selection " + lastPersonList.getSelectionIndex()); //$NON-NLS-1$
		// // Person p =
		// lastPersonList.getItem(lastPersonList.getSelectionIndex());
		//				        String selected = NLMessages.getString("Dialog_person"); //$NON-NLS-1$
		// Person p = lastPersons.get(si);
		// //
		// selected = p.getDisplayName();
		//				        selected = selected + "" //$NON-NLS-1$
		// + p.getPdrId().toString();
		//
		// setMessage(selected);
		// selectedP = lastPersons.get(si);
		// _selectedPersonText.setText(selectedP.getDisplayName());
		//
		// // deleted possible aspect selection
		//
		//
		//
		// }
		_personMainSForm.setWeights(new int[]
		{3, 1});
	}

	/**
	 * meth creates the TabItem for selecting a source.
	 * @param mainTabFolder main tab folder.
	 */
	private void createSourceTabItem(final TabFolder mainTabFolder)
	{
		_sourceTabItem = new TabItem(mainTabFolder, SWT.NONE);
		_sourceTabItem.setText(NLMessages.getString("Dialog_Sources")); //$NON-NLS-1$
		_sourceTabItem.setImage(_imageReg.get(IconsInternal.REFERENCES));
		_sourceComposite = new Composite(mainTabFolder, SWT.NONE);
		_sourceComposite.setLayout(new GridLayout());
		_sourceComposite.setLayoutData(new GridData());
		((GridData) _sourceComposite.getLayoutData()).verticalAlignment = SWT.FILL;

		_sourceTabItem.setControl(_sourceComposite);

		_sourceMainSForm = new SashForm(_sourceComposite, SWT.HORIZONTAL);
		_sourceMainSForm.setLayoutData(new GridData(GridData.FILL_BOTH));
		((GridData) _sourceMainSForm.getLayoutData()).minimumHeight = 500;
		((GridData) _sourceMainSForm.getLayoutData()).minimumWidth = 700;

		_sourceSearchSForm = new SashForm(_sourceMainSForm, SWT.VERTICAL);
		_sourceSearchSForm.setLayoutData(new GridData(GridData.FILL_BOTH));
		_refSearchGroup = new Group(_sourceSearchSForm, SWT.SHADOW_IN);
		_refSearchGroup.setText(NLMessages.getString("Dialog_sourceSearch")); //$NON-NLS-1$
		_refSearchGroup.setLayoutData(new GridData());
		((GridData) _refSearchGroup.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _refSearchGroup.getLayoutData()).verticalAlignment = SWT.FILL;
		((GridData) _refSearchGroup.getLayoutData()).minimumHeight = 90;
		((GridData) _refSearchGroup.getLayoutData()).grabExcessHorizontalSpace = true;
		_refSearchGroup.setLayout(_gridLayout2);
		buildReferenceSearch();
		_searchR = new Button(_refSearchGroup, SWT.PUSH);
		_searchR.setText(NLMessages.getString("Dialog_search")); //$NON-NLS-1$
		_searchR.setImage(_imageReg.get(IconsInternal.SEARCH));
		_searchR.setFont(JFaceResources.getDialogFont());
		_searchR.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent event)
			{
				searchReferences();
			}

		});
		// aspectSearchGroup
		_refSearchGroup.pack();
		Group sourceResultGroup = new Group(_sourceSearchSForm, SWT.SHADOW_IN);
		sourceResultGroup.setText(NLMessages.getString("Dialog_result")); //$NON-NLS-1$
		sourceResultGroup.setLayoutData(new GridData());
		((GridData) sourceResultGroup.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) sourceResultGroup.getLayoutData()).verticalAlignment = SWT.FILL;
		((GridData) sourceResultGroup.getLayoutData()).minimumHeight = 90;
		((GridData) sourceResultGroup.getLayoutData()).grabExcessHorizontalSpace = true;
		sourceResultGroup.setLayout(_gridLayout2);
		{
			_referenceTableViewer = new TableViewer(sourceResultGroup, SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);

			String[] titles =
			{NLMessages.getString("Dialog_displayName"), NLMessages.getString("Dialog_title"),
					NLMessages.getString("Dialog_id")}; //$NON-NLS-1$ //$NON-NLS-2$
			int[] bounds =
			{300, 180, 180};

			for (int i = 0; i < titles.length; i++)
			{
				TableViewerColumn column = new TableViewerColumn(_referenceTableViewer, SWT.NONE);
				column.getColumn().setText(titles[i]);
				column.getColumn().setWidth(bounds[i]);
				column.getColumn().setResizable(true);
				column.getColumn().setMoveable(true);
				column.getColumn().addSelectionListener(
						getSelectionAdapter(_referenceTableViewer, column.getColumn(), i));
			}
			Table table = _referenceTableViewer.getTable();
			table.setHeaderVisible(true);
			table.setLinesVisible(true);
			table.setLayoutData(new GridData());
			((GridData) table.getLayoutData()).horizontalAlignment = SWT.FILL;
			((GridData) table.getLayoutData()).grabExcessHorizontalSpace = true;
			((GridData) table.getLayoutData()).verticalAlignment = SWT.FILL;
			((GridData) table.getLayoutData()).grabExcessVerticalSpace = true;

			_referenceTableViewer.addDoubleClickListener(new IDoubleClickListener()
			{
				@Override
				public void doubleClick(final DoubleClickEvent event)
				{
					ISelection selection = _referenceTableViewer.getSelection();
					Object obj = ((IStructuredSelection) selection).getFirstElement();
					_selectedR = (ReferenceMods) obj;
					_selectedRefText.setText(_selectedR.getDisplayNameLong());
				}
			});

			// aspectResultGroup
			Label selectLabel = new Label(sourceResultGroup, SWT.None);
			selectLabel.setText(NLMessages.getString("Dialog_selection"));

			_selectedRefText = new Text(sourceResultGroup, SWT.SHADOW_IN | SWT.WRAP | SWT.READ_ONLY);
			_selectedRefText.setLayoutData(new GridData());
			((GridData) _selectedRefText.getLayoutData()).horizontalAlignment = SWT.FILL;
			((GridData) _selectedRefText.getLayoutData()).verticalAlignment = SWT.FILL;
			((GridData) _selectedRefText.getLayoutData()).grabExcessHorizontalSpace = true;
			((GridData) _selectedRefText.getLayoutData()).minimumHeight = 30;

			_selectedRefText.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

		} // sourceResultGroup
		_sourceSearchSForm.setWeights(new int[]
		{2, 2});
		Group lastSourcesGroup = new Group(_sourceMainSForm, SWT.SHADOW_IN);
		lastSourcesGroup.setText(NLMessages.getString("Dialog_lastSources")); //$NON-NLS-1$
		lastSourcesGroup.setLayoutData(new GridData());
		((GridData) lastSourcesGroup.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) lastSourcesGroup.getLayoutData()).verticalAlignment = SWT.FILL;
		((GridData) lastSourcesGroup.getLayoutData()).minimumHeight = 120;
		((GridData) lastSourcesGroup.getLayoutData()).grabExcessHorizontalSpace = true;
		lastSourcesGroup.setLayout(_gridLayout2);
		_lastReferenceList = new List(lastSourcesGroup, SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		_lastReferenceList.setLayoutData(new GridData());
		((GridData) _lastReferenceList.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _lastReferenceList.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) _lastReferenceList.getLayoutData()).verticalAlignment = SWT.FILL;
		((GridData) _lastReferenceList.getLayoutData()).grabExcessVerticalSpace = true;
		final Vector<ReferenceMods> lastRefs = _facade.getLastReferences();
		if (_facade.getLastReferences() != null && !_facade.getLastReferences().isEmpty())
		{
			for (int i = lastRefs.size() - 1; i > -1; i--)
			{
				_lastReferenceList.add(lastRefs.get(i).getDisplayNameWithID());
			}
		}
		_lastReferenceList.addSelectionListener(new SelectionListener()
		{
			@Override
			public void widgetDefaultSelected(final SelectionEvent e)
			{

			}

			@Override
			public void widgetSelected(final SelectionEvent event)
			{
				int si = lastRefs.size() - _lastReferenceList.getSelectionIndex() - 1;
				String selected = NLMessages.getString("Dialog_selectedRef");
				ReferenceMods r = lastRefs.get(si);
				//
				selected = r.getDisplayName();
				selected = selected + " ID: " //$NON-NLS-1$
						+ r.getPdrId().toString();

				setMessage(selected);
				_selectedR = lastRefs.get(si);
				_selectedRefText.setText(_selectedR.getDisplayNameLong());

				// deleted possible aspect selection
				_lastAspectList.deselectAll();
				_lastPersonList.deselectAll();

			}

		});
		_sourceMainSForm.setWeights(new int[]
		{3, 1});
	}

	/**
	 * Gets the selection adapter.
	 * @param tableViewer the table viewer
	 * @param column the column
	 * @param index the index
	 * @return the selection adapter
	 */
	final SelectionAdapter getSelectionAdapter(final TableViewer tableViewer, final TableColumn column, final int index)
	{
		SelectionAdapter selectionAdapter = new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent e)
			{
				((PdrObjectViewComparator) tableViewer.getComparator()).setColumn(index);
				int dir = tableViewer.getTable().getSortDirection();
				if (tableViewer.getTable().getSortColumn() == column)
				{
					dir = dir == SWT.UP ? SWT.DOWN : SWT.UP;
				}
				else
				{

					dir = SWT.DOWN;
				}
				tableViewer.getTable().setSortDirection(dir);
				tableViewer.getTable().setSortColumn(column);
				tableViewer.refresh();
			}
		};
		return selectionAdapter;
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

	/**
	 * Save input.
	 */
	private void saveInput()
	{
		//		System.out.println("maintabfolder selection " + mainTabFolder.getSelectionIndex()); //$NON-NLS-1$
		switch (_selectionType)
		{
			case 0:
				if (_selectedA != null)
				{
					_facade.setRequestedId(_selectedA.getPdrId());
					_facade.addToLastAspects(_selectedA);
				}
				break;
			case 1:
				if (_selectedP != null)
				{
					_facade.setRequestedId(_selectedP.getPdrId());
					_facade.addToLastPersons(_selectedP);
				}
				break;
			case 2:
				if (_selectedR != null)
				{
					_facade.setRequestedId(_selectedR.getPdrId());
					_facade.addToLastReferences(_selectedR);
				}
				//		System.out.println("set requestetd id to " + _facade.getRequestedId()); //$NON-NLS-1$
				break;
			default:
				break;
		}

	}

	/**
	 * Search aspects.
	 */
	private void searchAspects()
	{
		_aspectTableViewer.setContentProvider(new AspectTableContentProvider());
		_aspectTableViewer.setLabelProvider(new TableLabelProvider());
		_aspectTableViewer.setComparator(new PdrObjectViewComparator());

		ProgressMonitorDialog dialog = new ProgressMonitorDialog(super.getShell());
		dialog.setCancelable(true);

		try
			{
			dialog.run(true, true, new IRunnableWithProgress()
				{
				@Override
				public void run(final IProgressMonitor monitor)
					{

					try
					{
						final Object result = _mainSearcher.searchAspects(_aspectQuery, monitor);
						UIJob job = new UIJob(_personTableViewer.getTable().getDisplay(), "Load Result")
						{
							@Override
							public IStatus runInUIThread(final IProgressMonitor monitor)
							{
								_aspectTableViewer.setInput(result);
								return Status.OK_STATUS;
							}
						};
						job.setUser(true);
						job.schedule();
					}
					catch (Exception e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					}

				}
);
		}
		catch (InvocationTargetException e)
		{
			e.printStackTrace();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
			}

		_aspectSearchGroup.layout();

	}

	/**
	 * Search persons.
	 */
	private void searchPersons()
	{
		_personTableViewer.setContentProvider(new PersonVectorContentProvider());
		_personTableViewer.setLabelProvider(new TableLabelProvider());
		_personTableViewer.setComparator(new PdrObjectViewComparator());
		ProgressMonitorDialog dialog = new ProgressMonitorDialog(super.getShell());
		dialog.setCancelable(true);

		try
			{
			dialog.run(true, true, new IRunnableWithProgress()
				{
				@Override
				public void run(final IProgressMonitor monitor)
				{

					try
					{

						final Object result = _mainSearcher.searchPersons(_personQuery, monitor);
						UIJob job = new UIJob(_personTableViewer.getTable().getDisplay(), "Load Result")
						{
							@Override
							public IStatus runInUIThread(final IProgressMonitor monitor)
							{
								_personTableViewer.setInput(result);
								return Status.OK_STATUS;
							}
						};
						job.setUser(true);
						job.schedule();

					}
					catch (Exception e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				}
);
		}
		catch (InvocationTargetException e)
		{
			e.printStackTrace();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}

		_personResultGroup.layout();

	}

	/**
	 * Search references.
	 */
	private void searchReferences()
	{
		_referenceTableViewer.setContentProvider(new ReferenceTableContentProvider());
		_referenceTableViewer.setLabelProvider(new TableLabelProvider());
		_referenceTableViewer.setComparator(new PdrObjectViewComparator());

		ProgressMonitorDialog dialog = new ProgressMonitorDialog(super.getShell());
		dialog.setCancelable(true);

		try
			{
			dialog.run(true, true, new IRunnableWithProgress()
				{
				@Override
				public void run(final IProgressMonitor monitor)
				{

					try
					{

						final Object result = _mainSearcher.searchReferences(_referenceQuery, monitor);
						UIJob job = new UIJob(_personTableViewer.getTable().getDisplay(), "Load Result")
						{
							@Override
							public IStatus runInUIThread(final IProgressMonitor monitor)
							{
								_referenceTableViewer.setInput(result);
								return Status.OK_STATUS;
							}
						};
						job.setUser(true);
						job.schedule();
					}
					catch (Exception e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				}
);
		}
		catch (InvocationTargetException e)
		{
			e.printStackTrace();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
			}

		_refSearchGroup.layout();

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
			// System.out.println("has input and is hashmap");
			@SuppressWarnings("unchecked")
			HashMap<String, ConfigData> inputs = (HashMap<String, ConfigData>) cv.getInput();
			if (inputs.containsKey(s))
			{
				// System.out.println("contains key s " + s);
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
		ciAll.setValue("ALL");
		ciAll.setLabel("ALL");
		ciAll.setIgnore(false);
		inputs.put("ALL", ciAll);
		String[] values = null;
		if (facetType.equals("relation"))
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
					if (_facade.getConfigs().get(provider).getChildren().containsKey("aodl:relation"))
					{
						configs.putAll(_facade.getConfigs().get(provider).getChildren().get("aodl:relation")
								.getChildren());
					}
				}
				// System.out.println("markupprovider " + relationProvider);
				if (_facade.getConfigs().containsKey(_relationProvider)
						&& _facade.getConfigs().get(_relationProvider).getChildren() != null
						&& _facade.getConfigs().get(_relationProvider).getChildren().containsKey("aodl:relation"))
				{
					configs.putAll(_facade.getConfigs().get(_relationProvider).getChildren().get("aodl:relation")
							.getChildren());
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
					if (_facade.getConfigs().get(provider).getChildren().containsKey("aodl:relation")
							&& _facade.getConfigs().get(provider).getChildren().get("aodl:relation").getChildren() != null
							&& _facade.getConfigs().get(provider).getChildren().get("aodl:relation").getChildren()
									.containsKey(crit1))
					{
						configs.putAll(_facade.getConfigs().get(provider).getChildren().get("aodl:relation")
								.getChildren().get(crit1).getChildren());
					}
				}
				// System.out.println("relationProvider " + relationProvider);
				if (_facade.getConfigs().containsKey(_relationProvider)
						&& _facade.getConfigs().get(_relationProvider).getChildren() != null
						&& _facade.getConfigs().get(_relationProvider).getChildren().containsKey("aodl:relation")
						&& _facade.getConfigs().get(_relationProvider).getChildren().get("aodl:relation").getChildren() != null
						&& _facade.getConfigs().get(_relationProvider).getChildren().get("aodl:relation").getChildren()
								.containsKey(crit1))
				{
					configs.putAll(_facade.getConfigs().get(_relationProvider).getChildren().get("aodl:relation")
							.getChildren().get(crit1).getChildren());
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
				if (_facade.getConfigs().get(provider).getChildren().containsKey("aodl:" + crit1))
				{
					configs.putAll(_facade.getConfigs().get(provider).getChildren().get("aodl:" + crit1).getChildren());
				}
			}
			// System.out.println("markupprovider " + markupProvider);
			if (_facade.getConfigs().containsKey(_markupProvider)
					&& _facade.getConfigs().get(_markupProvider).getChildren() != null
					&& _facade.getConfigs().get(_markupProvider).getChildren().containsKey("aodl:" + crit1))
			{
				configs.putAll(_facade.getConfigs().get(_markupProvider).getChildren().get("aodl:" + crit1)
						.getChildren());
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
				if (_facade.getConfigs().get(provider).getChildren().containsKey(crit1)
						&& _facade.getConfigs().get(provider).getChildren().get("aodl:" + crit1).getChildren() != null
						&& _facade.getConfigs().get(provider).getChildren().get("aodl:" + crit1).getChildren()
								.containsKey(crit2))
				{
					configs.putAll(_facade.getConfigs().get(provider).getChildren().get("aodl:" + crit1).getChildren()
							.get(crit2).getChildren());
				}
			}
			// System.out.println("markupprovider " + markupProvider);
			if (_facade.getConfigs().containsKey(_markupProvider)
					&& _facade.getConfigs().get(_markupProvider).getChildren() != null
					&& _facade.getConfigs().get(_markupProvider).getChildren().containsKey("aodl:" + crit1)
					&& _facade.getConfigs().get(_markupProvider).getChildren().get("aodl:" + crit1).getChildren() != null
					&& _facade.getConfigs().get(_markupProvider).getChildren().get("aodl:" + crit1).getChildren()
							.containsKey(crit2))
			{
				configs.putAll(_facade.getConfigs().get(_markupProvider).getChildren().get("aodl:" + crit1)
						.getChildren().get(crit2).getChildren());
			}

		}
		for (String value : values)
		{
			// System.out.println("value " + value);
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
	 * Sets Focus in view.
	 */
	public void setFocus()
	{
	}

}
