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
package org.bbaw.pdr.ae.base;

import org.bbaw.pdr.ae.common.AEPluginIDs;
import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
/**
 * @author cplutte
 *
 * Main perspective of rcp application.
 *
 * TODO it has to be dynamized in order to have different perspectives.
 *
 */
public class Perspective implements IPerspectiveFactory
{
	/** creates initial layout.
	 * @param layout layout.
	 */
	public final void createInitialLayout(final IPageLayout layout)
	{
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(false);
		layout.setFixed(true);
		layout.addStandaloneView(AEPluginIDs.VIEW_TREEVIEW,
				false, IPageLayout.LEFT, 0.30f, editorArea);
//		layout.addStandaloneView("org.bbaw.pdr.ae.view.main.views.AspectsView",
//				true, IPageLayout.LEFT, 0.68f, editorArea);
		IFolderLayout right = layout.createFolder("right", IPageLayout.RIGHT, 0.69f, editorArea);
//		topLeft.addPlaceholder("org.bbaw.pdr.ae.view.main.browser.BrowserViewPart");
		right.addView(AEPluginIDs.VIEW_ASPECTS +  ":a");
	}

}
