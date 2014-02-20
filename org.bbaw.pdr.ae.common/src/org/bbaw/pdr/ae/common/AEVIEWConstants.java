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
package org.bbaw.pdr.ae.common;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

/**
 * Zentrale Ablage fuer Konstanten des Archiv-Editors.
 * @author Christoph Plutte
 */

public final class AEVIEWConstants
{
	/** Resource Manager for colors and fonts. */
	private static final LocalResourceManager _resources = new LocalResourceManager(JFaceResources.getResources());
	// GUI Colors
	public static final Color VIEW_BACKGROUND_SELECTED_COLOR = _resources.createColor(new RGB(255, 255, 204));
	public static final Color VIEW_BACKGROUND_DESELECTED_COLOR = _resources.createColor(new RGB(255, 255, 255));
	public static final Color VIEW_BACKGROUND_INVALID_COLOR = _resources.createColor(new RGB(255, 153, 151));

	public static final Color VIEW_FOREGROUND_SELECTED_COLOR = _resources.createColor(new RGB(0, 0, 0));
	public static final Color VIEW_FOREGROUND_DESELECTED_COLOR = _resources.createColor(new RGB(128, 128, 128));
	public static final Color VIEW_TEXT_SELECTED_COLOR = _resources.createColor(new RGB(0, 0, 0));
	public static final Color VIEW_TEXT_DESELECTED_COLOR = _resources.createColor(new RGB(163, 163, 163));

	/* Initialisierung */
	static
	{
	}

}
