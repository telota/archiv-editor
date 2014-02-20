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
package org.bbaw.pdr.ae.standalone;

import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

/**
 * An action bar advisor is responsible for creating, adding, and disposing of
 * the actions added to a workbench window. Each window will be populated with
 * new actions.
 */
public class ApplicationActionBarAdvisor extends ActionBarAdvisor
{

	// Actions - important to allocate these only in makeActions, and then use
	// them
	// in the fill methods. This ensures that the actions aren't recreated
	// when fillActionBars is called with FILL_PROXY.
	/** The exit action. */
	private IWorkbenchAction _exitAction;

	/** The save action. */
	private IWorkbenchAction _saveAction;

	/** The save_as action. */
	private IWorkbenchAction _saveAsAction;

	/** The print action. */
	private IWorkbenchAction _printAction;

	/** The about action. */
	private IWorkbenchAction _aboutAction;

	/** The show help action. */
	private IWorkbenchAction _showHelpAction;

	/** The search help action. */
	private IWorkbenchAction _searchHelpAction;

	/** The dynamic help action. */
	private IWorkbenchAction _dynamicHelpAction;

	/** The change perspectiv action. */
	private IWorkbenchAction _changePerspectivAction;

	/**
	 * Instantiates a new application action bar advisor.
	 * @param configurer the configurer
	 */
	public ApplicationActionBarAdvisor(final IActionBarConfigurer configurer)
	{
		super(configurer);
	}

	@Override
	protected final void fillMenuBar(final IMenuManager menuBar)
	{

		// MenuManager fileMenu = new MenuManager("&File",
		// IWorkbenchActionConstants.M_FILE);
		// menuBar.add(fileMenu);
		// fileMenu.add(exitAction);
		MenuManager helpMenu = new MenuManager("&Help", IWorkbenchActionConstants.M_HELP);

		// Help
		// XXX add an additions group because this is what SDK UI expects
		helpMenu.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
		helpMenu.add(new Separator());
		helpMenu.add(_aboutAction);

	}

	@Override
	protected final void makeActions(final IWorkbenchWindow window)
	{
		// Creates the actions and registers them.
		// Registering is needed to ensure that key bindings work.
		// The corresponding commands keybindings are defined in the plugin.xml
		// file.
		// Registering also provides automatic disposal of the actions when
		// the window is closed.

		_saveAction = ActionFactory.SAVE.create(window);
		register(_saveAction);
		_saveAsAction = ActionFactory.SAVE_AS.create(window);
		register(_saveAsAction);
		_printAction = ActionFactory.PRINT.create(window);
		register(_printAction);
		_aboutAction = ActionFactory.ABOUT.create(window);
		register(_aboutAction);
		_exitAction = ActionFactory.QUIT.create(window);
		register(_exitAction);
		_showHelpAction = ActionFactory.HELP_CONTENTS.create(window);
		register(_showHelpAction);
		_searchHelpAction = ActionFactory.HELP_SEARCH.create(window);
		register(_searchHelpAction);
		_dynamicHelpAction = ActionFactory.DYNAMIC_HELP.create(window);
		register(_dynamicHelpAction);
		_changePerspectivAction = ActionFactory.OPEN_PERSPECTIVE_DIALOG.create(window);
		register(_changePerspectivAction);
	}

}
