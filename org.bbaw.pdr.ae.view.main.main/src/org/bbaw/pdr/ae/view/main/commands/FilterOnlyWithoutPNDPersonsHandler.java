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
package org.bbaw.pdr.ae.view.main.commands;

import java.util.Map;

import org.bbaw.pdr.ae.common.AEPluginIDs;
import org.bbaw.pdr.ae.view.control.filters.PersonWithoutPNDFilter;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.core.commands.State;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.commands.IElementUpdater;
import org.eclipse.ui.menus.UIElement;
import org.eclipse.ui.services.ISourceProviderService;

/**
 * The Class FilterOnlyWithoutPNDPersonsHandler.
 * @author Christoph Plutte
 */
public class FilterOnlyWithoutPNDPersonsHandler implements IHandler, IElementUpdater
{

	/** The is checked. */
	private boolean _isChecked;

	@Override
	public final void addHandlerListener(final IHandlerListener handlerListener)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public final void dispose()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public final Object execute(final ExecutionEvent event) throws ExecutionException
	{
		ISourceProviderService service = (ISourceProviderService) PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getService(ISourceProviderService.class);
		@SuppressWarnings("rawtypes")
		Map state = service.getSourceProvider(AEPluginIDs.SOURCE_PARAMETER_TREE_VIEWER).getCurrentState();
		TreeViewer currentTree = (TreeViewer) state.get(AEPluginIDs.SOURCE_PARAMETER_TREE_VIEWER);
		ICommandService cService = (ICommandService) PlatformUI.getWorkbench().getService(ICommandService.class);
		Command command = cService.getCommand("org.bbaw.pdr.ae.view.main.commands.FilterOnlyWithoutPNDPersons");
		State tState = command.getState(AEPluginIDs.TOGGLE_STATE_FILTERONLYWITHOUTPNDPERSONS);
		_isChecked = (Boolean) tState.getValue();
		if (!_isChecked)
		{
			currentTree.addFilter(new PersonWithoutPNDFilter());
		}
		else
		{
			ViewerFilter[] filters = currentTree.getFilters();
			if (filters.length <= 1)
			{
				currentTree.resetFilters();
			}
			else
			{
				for (ViewerFilter f : filters)
				{
					if (f instanceof PersonWithoutPNDFilter)
					{
						currentTree.removeFilter(f);
						break;
					}
				}
			}
		}
		_isChecked = !_isChecked;
		tState.setValue(_isChecked);
		cService.refreshElements(command.getId(), null);
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
	public final void removeHandlerListener(final IHandlerListener handlerListener)
	{
		// TODO Auto-generated method stub

	}

	@SuppressWarnings("rawtypes")
	@Override
	public final void updateElement(final UIElement element, final Map arg1)
	{
		element.setChecked(_isChecked);

	}

}
