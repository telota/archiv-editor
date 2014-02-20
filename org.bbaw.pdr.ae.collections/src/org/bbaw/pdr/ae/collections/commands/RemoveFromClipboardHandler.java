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
package org.bbaw.pdr.ae.collections.commands;

import java.util.ArrayList;
import java.util.Map;

import org.bbaw.pdr.ae.collections.control.CollectionsFacade;
import org.bbaw.pdr.ae.collections.model.PDRCollection;
import org.bbaw.pdr.ae.common.AEPluginIDs;
import org.bbaw.pdr.ae.model.PdrObject;
import org.bbaw.pdr.ae.model.view.TreeNode;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.services.ISourceProviderService;

/** handler for command to remove selected object from clipboard.
 * @author Christoph Plutte
 *
 */
public class RemoveFromClipboardHandler implements IHandler
{

	@Override
	public void addHandlerListener(final IHandlerListener handlerListener)
	{
	}

	@Override
	public void dispose()
	{

	}

	@Override
	public final Object execute(final ExecutionEvent event) throws ExecutionException
	{
		IStructuredSelection sel;
		sel = (IStructuredSelection) PlatformUI.getWorkbench()
		.getActiveWorkbenchWindow().getSelectionService().getSelection(AEPluginIDs.VIEW_TREEVIEW);

		if (sel != null)
		{
			Object[] objs =  ((IStructuredSelection) sel).toArray();
			ArrayList<PdrObject> selection = new ArrayList<PdrObject>();
			for (Object o : objs)
			{
				if (o instanceof PdrObject)
				{
					selection.add((PdrObject)o);
				}
			}
	           PDRCollection coll;
	           ISourceProviderService service = (ISourceProviderService) PlatformUI
	        		   .getWorkbench().getActiveWorkbenchWindow().getService(ISourceProviderService.class);
	           @SuppressWarnings("rawtypes")
			Map state = service.getSourceProvider(AEPluginIDs.SOURCE_PARAMETER_TREE).getCurrentState();
	           String currentTree = (String) state.get(AEPluginIDs.SOURCE_PARAMETER_TREE);
	           if (currentTree == null || currentTree.equals("clipboard"))
		       {
	        	   coll = CollectionsFacade.getInstance().getClipboard();
		       }
	           else
	           {
	        	   coll = CollectionsFacade.getInstance().getLoadedCollections().get(currentTree);
	           }

	           if (coll != null)
	           {
		           for (PdrObject o : selection)
		           {
		        	   if (o != null)
					{
						coll.remove(o);
					}
		           }

			   		TreeNode root = new TreeNode(coll.getName(), "collection");
			   		if (coll != null && coll.getObjects() != null)
			   		{
			   			for (TreeNode item : coll.getObjects())
			   			{
			   				root.addChild(item);
			   			}
			   			Event ev = new Event();
			   			ev.data = root;
			   			IHandlerService handlerService = (IHandlerService) PlatformUI.getWorkbench()
			   		    .getService(IHandlerService.class);
			   			try
			   			{
			   				handlerService.executeCommand(
			   						"org.bbaw.pdr.ae.view.main.commands.ShowObjectsInTreeView", ev); //$NON-NLS-1$
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
	           }
        }
		return null;
	}

	@Override
	public final boolean isEnabled()
	{
		return true;
	}

	@Override
	public final boolean isHandled()
	{
		return true;
	}

	@Override
	public void removeHandlerListener(final IHandlerListener handlerListener)
	{
	}

}
