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
package org.bbaw.pdr.ae.view.main.preferences;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.bbaw.pdr.ae.common.AEConstants;
import org.bbaw.pdr.ae.common.CommonActivator;
import org.bbaw.pdr.ae.common.NLMessages;
import org.bbaw.pdr.ae.config.editor.view.ConfigTreeContentProvider;
import org.bbaw.pdr.ae.config.editor.view.ConfigTreeLabelProvider;
import org.bbaw.pdr.ae.config.editor.view.ConfigTreeSorter;
import org.bbaw.pdr.ae.config.model.ConfigData;
import org.bbaw.pdr.ae.config.model.ConfigItem;
import org.bbaw.pdr.ae.config.model.ConfigTreeNode;
import org.bbaw.pdr.ae.config.model.DataType;
import org.bbaw.pdr.ae.config.model.DatatypeDesc;
import org.bbaw.pdr.ae.control.core.FacetProposalManager;
import org.bbaw.pdr.ae.control.facade.Facade;
import org.bbaw.pdr.ae.view.main.preferences.provider.ConfigIgnoreFilter;
import org.bbaw.pdr.ae.view.main.preferences.provider.ConfigOnlyMarkupFilter;
import org.bbaw.pdr.ae.view.main.preferences.provider.FacetedSearchFilter;
import org.bbaw.pdr.ae.view.main.preferences.provider.FavoriteMarkupComparator;
import org.bbaw.pdr.ae.view.main.preferences.provider.FavoriteMarkupContentProvider;
import org.bbaw.pdr.ae.view.main.preferences.provider.MarkupTableLabelProvider;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * The Class FacetedSearchPage.
 * @author Christoph Plutte
 */
public class FacetedSearchPage extends PreferencePage implements IWorkbenchPreferencePage
{

	/** The _facade. */
	private Facade _facade = Facade.getInstanz();

	/** The facet proposals. */
	private HashMap<String, ConfigData> _facetProposals;

	/** The main composite. */
	private Composite _mainComposite;

	/** The left composite. */
	private Composite _leftComposite;

	/** The middle composite. */
	private Composite _middleComposite;

	/** The right composite. */
	private Composite _rightComposite;

	/** The markup table viewer. */
	private TableViewer _markupTableViewer;

	/** The _tree viewer. */
	private TreeViewer _treeViewer;

	/** The tree. */
	private Tree _tree;

	/** The table. */
	private Table _table;

	/** The add to right. */
	private Button _addToRight;

	/** The remove from right. */
	private Button _removeFromRight;

	/**
	 * Instantiates a new faceted search page.
	 */
	public FacetedSearchPage()
	{
		super();

	}

	@Override
	protected final Control createContents(final Composite parent)
	{
		_mainComposite = new Composite(parent, SWT.NONE);

		_mainComposite.setLayoutData(new GridData());
		((GridData) _mainComposite.getLayoutData()).verticalAlignment = SWT.FILL;
		((GridData) _mainComposite.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _mainComposite.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) _mainComposite.getLayoutData()).minimumHeight = 500;

		_mainComposite.setLayout(new GridLayout());
		((GridLayout) _mainComposite.getLayout()).numColumns = 3;
		((GridLayout) _mainComposite.getLayout()).makeColumnsEqualWidth = false;

		Label title = new Label(_mainComposite, SWT.None);
		title.setText(NLMessages.getString("Preference_facet_person_search_proposal_description"));
		title.setLayoutData(new GridData());
		((GridData) title.getLayoutData()).horizontalSpan = 3;

		_leftComposite = new Composite(_mainComposite, SWT.NONE);
		_leftComposite.setLayoutData(new GridData());
		((GridData) _leftComposite.getLayoutData()).verticalAlignment = SWT.FILL;
		((GridData) _leftComposite.getLayoutData()).grabExcessVerticalSpace = true;
		((GridData) _leftComposite.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _leftComposite.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) _leftComposite.getLayoutData()).heightHint = 450;
		((GridData) _leftComposite.getLayoutData()).widthHint = 230;

		_leftComposite.setLayout(new GridLayout());
		((GridLayout) _leftComposite.getLayout()).numColumns = 1;
		((GridLayout) _leftComposite.getLayout()).makeColumnsEqualWidth = false;

		_tree = new Tree(_leftComposite, SWT.BORDER);
		_tree.setLayoutData(new GridData());
		((GridData) _tree.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) _tree.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _tree.getLayoutData()).grabExcessVerticalSpace = true;
		((GridData) _tree.getLayoutData()).verticalAlignment = SWT.FILL;
		((GridData) _tree.getLayoutData()).horizontalSpan = 3;

		_treeViewer = new TreeViewer(_tree);
		TreeColumn column = new TreeColumn(_treeViewer.getTree(), SWT.NONE);
		column.setWidth(200);
		column.setText("Column 1"); //$NON-NLS-1$
		_treeViewer.setContentProvider(new ConfigTreeContentProvider(false, false, false));
		_treeViewer.setLabelProvider(new ConfigTreeLabelProvider());
		_treeViewer.setSorter(new ConfigTreeSorter());
		_treeViewer.addFilter(new ConfigIgnoreFilter());
		_treeViewer.addFilter(new FacetedSearchFilter(this));
		_treeViewer.addFilter(new ConfigOnlyMarkupFilter());
		DatatypeDesc datatypeDesc = _facade.getConfigs().get(
				Platform.getPreferencesService().getString(CommonActivator.PLUGIN_ID,
						"PRIMARY_TAGGING_PROVIDER", AEConstants.TAGGING_LIST_PROVIDER, null)); //$NON-NLS-1$
		_treeViewer.setInput(datatypeDesc);
		// _treeViewer.refresh();
		_treeViewer.addSelectionChangedListener(new ISelectionChangedListener()
		{
			@Override
			public void selectionChanged(final SelectionChangedEvent event)
			{
				IStructuredSelection selection = (IStructuredSelection) event.getSelection();
				Object obj = selection.getFirstElement();
				ConfigTreeNode tn = (ConfigTreeNode) obj;
				if (tn != null && !tn.isSelected())
				{
					_addToRight.setEnabled(true);
				}
				else
				{
					_addToRight.setEnabled(false);
				}
			}
		});
		_treeViewer.addDoubleClickListener(new IDoubleClickListener()
		{
			@Override
			public void doubleClick(final DoubleClickEvent event)
			{
				if (event.getSelection() instanceof IStructuredSelection)
				{
					IStructuredSelection selection = (IStructuredSelection) event.getSelection();
					Object obj = selection.getFirstElement();
					ConfigTreeNode tn = (ConfigTreeNode) obj;
					if (tn.hasChildren())
					{
						_treeViewer.setExpandedState(tn, !_treeViewer.getExpandedState(tn));
					}
				}
			}
		});
		_middleComposite = new Composite(_mainComposite, SWT.NONE);
		_middleComposite.setLayoutData(new GridData());
		((GridData) _middleComposite.getLayoutData()).verticalAlignment = SWT.CENTER;
		((GridData) _middleComposite.getLayoutData()).grabExcessVerticalSpace = true;

		_middleComposite.setLayout(new GridLayout());
		((GridLayout) _middleComposite.getLayout()).numColumns = 1;
		((GridLayout) _middleComposite.getLayout()).makeColumnsEqualWidth = false;

		_addToRight = new Button(_middleComposite, SWT.PUSH);
		_addToRight.setText(">>>"); //$NON-NLS-1$
		_addToRight.setToolTipText(NLMessages.getString("Preference_add_proposal_tooltip"));
		_addToRight.setLayoutData(new GridData());
		((GridData) _addToRight.getLayoutData()).verticalAlignment = SWT.CENTER;
		_addToRight.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent e)
			{
				IStructuredSelection selection = (IStructuredSelection) _treeViewer.getSelection();
				Object obj = selection.getFirstElement();
				ConfigTreeNode tn = (ConfigTreeNode) obj;
				tn.setSelected(true);
				_addToRight.setEnabled(false);
				ConfigData cd = tn.getConfigData();
				if (cd instanceof ConfigItem)
				{
					_facetProposals.put(cd.getValue(), cd);
					loadValues();
					Object[] elements = _treeViewer.getExpandedElements();
					_treeViewer.refresh();
					for (Object o : elements)
					{
						_treeViewer.setExpandedState(o, true);
					}
				}
			}
		});
		_addToRight.pack();

		_removeFromRight = new Button(_middleComposite, SWT.PUSH);
		_removeFromRight.setText("<<<"); //$NON-NLS-1$
		_removeFromRight.setToolTipText(NLMessages.getString("Preference_remove_proposal_tooltip"));
		_removeFromRight.setLayoutData(new GridData());
		((GridData) _removeFromRight.getLayoutData()).verticalAlignment = SWT.CENTER;
		_removeFromRight.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent e)
			{
				IStructuredSelection selection = (IStructuredSelection) _markupTableViewer.getSelection();
				Object obj = selection.getFirstElement();
				ConfigData cd = (ConfigData) obj;
				_addToRight.setEnabled(true);
				_facetProposals.remove(cd.getValue());
				loadValues();
				Object[] elements = _treeViewer.getExpandedElements();
				_treeViewer.refresh();
				for (Object o : elements)
				{
					_treeViewer.setExpandedState(o, true);
				}
			}
		});
		_removeFromRight.pack();

		_rightComposite = new Composite(_mainComposite, SWT.NONE);
		_rightComposite.setLayoutData(new GridData());
		((GridData) _rightComposite.getLayoutData()).verticalAlignment = SWT.FILL;
		((GridData) _rightComposite.getLayoutData()).grabExcessVerticalSpace = true;
		((GridData) _rightComposite.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _rightComposite.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) _rightComposite.getLayoutData()).heightHint = 450;

		_rightComposite.setLayout(new GridLayout());
		((GridLayout) _rightComposite.getLayout()).numColumns = 1;
		((GridLayout) _rightComposite.getLayout()).makeColumnsEqualWidth = false;

		_markupTableViewer = new TableViewer(_rightComposite, SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);

		String[] titles =
		{"Name", "Position"}; //$NON-NLS-1$ //$NON-NLS-2$
		int[] bounds =
		{180, 80};

		for (int i = 0; i < titles.length; i++)
		{
			TableViewerColumn tableColumn = new TableViewerColumn(_markupTableViewer, SWT.NONE);
			tableColumn.getColumn().setText(titles[i]);
			tableColumn.getColumn().setWidth(bounds[i]);
			tableColumn.getColumn().setResizable(true);
			tableColumn.getColumn().setMoveable(true);
			tableColumn.getColumn().addSelectionListener(
					getSelectionAdapter(_markupTableViewer, tableColumn.getColumn(), i));

		}
		// markupTableViewer.setComparator(new FavoriteMarkupComparator());

		_table = _markupTableViewer.getTable();
		_table.setHeaderVisible(true);
		_table.setLinesVisible(true);
		_table.setLayoutData(new GridData());
		((GridData) _table.getLayoutData()).horizontalAlignment = SWT.FILL;
		// ((GridData) table.getLayoutData()).grabExcessHorizontalSpace = true ;
		((GridData) _table.getLayoutData()).verticalAlignment = SWT.FILL;
		((GridData) _table.getLayoutData()).grabExcessVerticalSpace = true;
		FacetProposalManager fpm = new FacetProposalManager();
		_facetProposals = fpm.loadFacetProposals();
		if (_facetProposals == null)
		{
			_facetProposals = new HashMap<String, ConfigData>();
		}
		loadValues();
		return null;
	}

	/**
	 * Gets the facet proposals.
	 * @return the facet proposals
	 */
	public final HashMap<String, ConfigData> getFacetProposals()
	{
		return _facetProposals;
	}

	/**
	 * Gets the path.
	 * @param cd the cd
	 * @return the path
	 */
	private String getPath(final ConfigData cd)
	{
		String path = Platform.getPreferencesService().getString(CommonActivator.PLUGIN_ID,
				"PRIMARY_TAGGING_PROVIDER", AEConstants.TAGGING_LIST_PROVIDER, null); //$NON-NLS-1$
		if (cd instanceof ConfigItem)
		{
			ConfigItem ci = (ConfigItem) cd;
			if (ci.getPos().equals("type")) //$NON-NLS-1$
			{
				DataType dt = (DataType) ci.getParent();
				path += "|" + dt.getValue(); //$NON-NLS-1$
				path += "|" + ci.getValue(); //$NON-NLS-1$
			}
			else if (ci.getPos().equals("subtype")) //$NON-NLS-1$
			{
				ConfigItem parent = (ConfigItem) ci.getParent();
				DataType dt = (DataType) parent.getParent();
				path += "|" + dt.getValue(); //$NON-NLS-1$
				path += "|" + parent.getValue(); //$NON-NLS-1$
				path += "|" + ci.getValue(); //$NON-NLS-1$

			}
			else if (ci.getPos().equals("role")) //$NON-NLS-1$
			{
				ConfigItem parent = (ConfigItem) ci.getParent();
				ConfigItem grandParent = (ConfigItem) parent.getParent();
				DataType dt = (DataType) grandParent.getParent();
				path += "|" + dt.getValue(); //$NON-NLS-1$
				path += "|" + grandParent.getValue(); //$NON-NLS-1$
				path += "|" + parent.getValue(); //$NON-NLS-1$
				path += "|" + ci.getValue(); //$NON-NLS-1$
			}

		}
		return path;
	}

	/**
	 * Gets the selection adapter.
	 * @param tableViewer the table viewer
	 * @param column the column
	 * @param index the index
	 * @return the selection adapter
	 */
	private SelectionAdapter getSelectionAdapter(final TableViewer tableViewer, final TableColumn column,
			final int index)
	{
		SelectionAdapter selectionAdapter = new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent e)
			{
				((FavoriteMarkupComparator) tableViewer.getComparator()).setColumn(index);
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
	public void init(final IWorkbench workbench)
	{

	}

	/**
	 * Load values.
	 */
	final void loadValues()
	{
		_markupTableViewer.setContentProvider(new FavoriteMarkupContentProvider());
		_markupTableViewer.setLabelProvider(new MarkupTableLabelProvider());
		_markupTableViewer.setComparator(new FavoriteMarkupComparator());

		_markupTableViewer.setInput(_facetProposals);
		_markupTableViewer.refresh();
	}

	@Override
	protected final void performApply()
	{
		performOk();
	}

	@Override
	public final boolean performOk()
	{
		boolean load = true;
		int i = 0;
		while (load)
		{
			String path = Platform.getPreferencesService().getString(CommonActivator.PLUGIN_ID,
					"FACET_PROPOSAL" + i, null, null); //$NON-NLS-1$
			if (path != null && path.trim().length() > 0)
			{
				CommonActivator.getDefault().getPreferenceStore().setValue("FACET_PROPOSAL" + i, "EMPTY"); //$NON-NLS-1$ //$NON-NLS-2$
				i++;
			}
			else
			{
				load = false;
			}
		}
		FavoriteMarkupComparator comparator = (FavoriteMarkupComparator) _markupTableViewer.getComparator();
		comparator.setViewer(_markupTableViewer);
		List<ConfigData> configs = new ArrayList<ConfigData>(_facetProposals.values());
		Collections.sort(configs, comparator);
		i = 0;
		// List<String> keys = new ArrayList<String>(favoriteMarkups.keySet());
		// Collections.sort(keys);
		for (ConfigData cd : configs)
		{
			String path = getPath(cd);
			if (path != null)
			{
				CommonActivator.getDefault().getPreferenceStore().setValue("FACET_PROPOSAL" + i, path); //$NON-NLS-1$
				i++;
			}
		}
		_facade.setFacetProposals(_facetProposals);
		return true;
	}
}
