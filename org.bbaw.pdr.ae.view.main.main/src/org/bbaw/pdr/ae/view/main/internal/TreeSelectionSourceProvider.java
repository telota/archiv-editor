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
package org.bbaw.pdr.ae.view.main.internal;

import java.util.HashMap;
import java.util.Map;

import org.bbaw.pdr.ae.common.AEPluginIDs;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.AbstractSourceProvider;
import org.eclipse.ui.ISources;

/**
 * The Class TreeSelectionSourceProvider.
 * @author Christoph Plutte
 */
public class TreeSelectionSourceProvider extends AbstractSourceProvider
{

	/** The tree_selection. */
	private String _treeSelection;

	/** The tree_type. */
	private String _treeType;

	/** The tree_viewer. */
	private Object _treeViewer;

	/**
	 * @see org.eclipse.ui.ISourceProvider#dispose()
	 */
	@Override
	public void dispose()
	{
	}

	@SuppressWarnings(
	{"unchecked", "rawtypes"})
	@Override
	public final Map getCurrentState()
	{
		// System.out.println(_treeType);
		Map currentState = new HashMap(3);
		currentState.put(AEPluginIDs.SOURCE_PARAMETER_TREE, _treeSelection);
		currentState.put(AEPluginIDs.SOURCE_PARAMETER_TREE_TYPE, _treeType);
		currentState.put(AEPluginIDs.SOURCE_PARAMETER_TREE_VIEWER, _treeViewer);

		return currentState;
	}

	@Override
	public final String[] getProvidedSourceNames()
	{
		return new String[]
		{AEPluginIDs.SOURCE_PARAMETER_TREE, AEPluginIDs.SOURCE_PARAMETER_TREE_TYPE,
				AEPluginIDs.SOURCE_PARAMETER_TREE_VIEWER};
	}

	/**
	 * Sets the tree selection.
	 * @param treeSelection the new tree selection
	 */
	public final void setTreeSelection(final String treeSelection)
	{
		if (this._treeSelection != null && this._treeSelection.equals(treeSelection))
		{
			return;
		}
		this._treeSelection = treeSelection;
		fireSourceChanged(ISources.WORKBENCH, AEPluginIDs.SOURCE_PARAMETER_TREE, _treeSelection);
	}

	/**
	 * Sets the tree type.
	 * @param treeType the new tree type
	 */
	public final void setTreeType(final String treeType)
	{
		if (this._treeType != null && this._treeType.equals(treeType))
		{
			return;
		}
		this._treeType = treeType;
		fireSourceChanged(ISources.WORKBENCH, AEPluginIDs.SOURCE_PARAMETER_TREE_TYPE, _treeType);
	}

	/**
	 * Sets the tree viewer.
	 * @param treeViewer the new tree viewer
	 */
	public final void setTreeViewer(final TreeViewer treeViewer)
	{
		if (this._treeViewer != null && this._treeViewer.equals(treeViewer))
		{
			return;
		}
		this._treeViewer = treeViewer;
		fireSourceChanged(ISources.WORKBENCH, AEPluginIDs.SOURCE_PARAMETER_TREE_VIEWER, _treeViewer);
	}
}
