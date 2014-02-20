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
package org.bbaw.pdr.ae.config.editor.internal;

import org.bbaw.pdr.ae.config.model.ConfigTreeNode;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;

/** drag listener for drag-n-drop support in config editor tree.
 * @author Christoph Plutte
 *
 */
public class ConfigDragListener implements DragSourceListener
{

	/** treeviewer.*/
	private final TreeViewer _viewer;

	/** constructor with treeviewer.
	 * @param viewer viewer
	 */
	public ConfigDragListener(final TreeViewer viewer)
	{
		this._viewer = viewer;
	}

	@Override
	public void dragFinished(final DragSourceEvent event)
	{
//		System.out.println("Finshed Drag");
	}

	@Override
	public final void dragSetData(final DragSourceEvent event)
	{
		// Here you do the convertion to the type which is expected.
//		IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
//		TreeNode firstElement = (TreeNode) selection.getFirstElement();
		IStructuredSelection selection = (IStructuredSelection) _viewer.getSelection();
        Object obj =  selection.getFirstElement();
        ConfigTreeNode tn = (ConfigTreeNode) obj;

		if (ConfigTransfer.getInstance().isSupportedType(event.dataType))
	{
			event.data = tn.getConfigData();
		}
//		if (event != null) System.out.println("im drag listener event dataType " + event.dataType);
//		if (selection != null)   System.out.println("im drag listener selection " + selection.toString());
//		if (obj != null)    System.out.println("im drag listener event obj " + obj.toString());
//		if (tn != null)    System.out.println("im drag listener tn " + tn.getValue());
		event.data = tn.getConfigData();

//		if (event != null && event.data != null)    System.out.println("im drag listener event data " + ((ConfigItem) event.data).getValue());


	}

	@Override
	public void dragStart(final DragSourceEvent event)
	{
//		System.out.println("Start Drag");
	}

}

