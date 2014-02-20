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
package org.bbaw.pdr.ae.view.identifiers.view;

import org.bbaw.pdr.ae.common.AEPluginIDs;
import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

/**
 * perspective for adding external person identifiers.
 * @author cplutte
 */
public class ExternalIdentiferPerspective implements IPerspectiveFactory
{

	@Override
	public final void createInitialLayout(final IPageLayout layout)
	{
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(false);
		layout.setFixed(true);

		layout.addStandaloneView(AEPluginIDs.VIEW_TREEVIEW, false, IPageLayout.LEFT, 0.30f, editorArea);

		layout.addStandaloneView(AEPluginIDs.VIEW_ASPECTS, true, IPageLayout.LEFT, 0.5f, editorArea);

		IFolderLayout topLeft = layout.createFolder("topRight", IPageLayout.RIGHT, 0.33f, "folder.topRight");
		// topLeft.addPlaceholder("org.bbaw.pdr.ae.view.browser.BrowserViewPart");
		topLeft.addView(AEPluginIDs.VIEW_IDENTIFIERS_SEARCH);
		// layout.addStandaloneView("org.bbaw.pdr.ae.view.browser.BrowserViewPart",
		// true, IPageLayout.LEFT, 0.5f, editorArea);

	}

}
