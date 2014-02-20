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
package org.bbaw.pdr.ae.view.control.swtnotrwthelper;

import org.bbaw.pdr.ae.common.AEConstants;
import org.bbaw.pdr.ae.common.CommonActivator;
import org.bbaw.pdr.ae.model.TaggingRange;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

/**
 * The Class TaggingRangeTransformer.
 * @author Christoph Plutte
 */
public class TaggingRangeTransformer
{

	/** The Constant ASPECT_COLOR_PERSNAME. */
	private static final String ASPECT_COLOR_PERSNAME = Platform.getPreferencesService().getString(
			CommonActivator.PLUGIN_ID, "ASPECT_COLOR_PERSNAME", AEConstants.ASPECT_COLOR_PERSNAME, null);

	/** The Constant ASPECT_COLOR_ORGNAME. */
	private static final String ASPECT_COLOR_ORGNAME = Platform.getPreferencesService().getString(
			CommonActivator.PLUGIN_ID, "ASPECT_COLOR_ORGNAME", AEConstants.ASPECT_COLOR_ORGNAME, null);

	/** The Constant ASPECT_COLOR_PLACENAME. */
	private static final String ASPECT_COLOR_PLACENAME = Platform.getPreferencesService().getString(
			CommonActivator.PLUGIN_ID, "ASPECT_COLOR_PLACENAME", AEConstants.ASPECT_COLOR_PLACENAME, null);

	/** The Constant ASPECT_COLOR_DATE. */
	private static final String ASPECT_COLOR_DATE = Platform.getPreferencesService().getString(
			CommonActivator.PLUGIN_ID, "ASPECT_COLOR_DATE", AEConstants.ASPECT_COLOR_DATE, null);

	/** The Constant ASPECT_COLOR_NAME. */
	private static final String ASPECT_COLOR_NAME = Platform.getPreferencesService().getString(
			CommonActivator.PLUGIN_ID, "ASPECT_COLOR_NAME", AEConstants.ASPECT_COLOR_NAME, null);
	/** Resource Manager for colors and fonts. */
	private static LocalResourceManager resources = new LocalResourceManager(JFaceResources.getResources());

	/** The color pers. */
	private static Color colorPers = resources.createColor(new RGB(new Integer(ASPECT_COLOR_PERSNAME.split(",")[0]),
			new Integer(ASPECT_COLOR_PERSNAME.split(",")[1]), new Integer(ASPECT_COLOR_PERSNAME.split(",")[2])));

	/** The color org. */
	private static Color colorOrg = resources.createColor(new RGB(new Integer(ASPECT_COLOR_ORGNAME.split(",")[0]),
			new Integer(ASPECT_COLOR_ORGNAME.split(",")[1]), new Integer(ASPECT_COLOR_ORGNAME.split(",")[2])));

	/** The color place. */
	private static Color colorPlace = resources.createColor(new RGB(new Integer(ASPECT_COLOR_PLACENAME.split(",")[0]),
			new Integer(ASPECT_COLOR_PLACENAME.split(",")[1]), new Integer(ASPECT_COLOR_PLACENAME.split(",")[2])));

	/** The color date. */
	private static Color colorDate = resources.createColor(new RGB(new Integer(ASPECT_COLOR_DATE.split(",")[0]),
			new Integer(ASPECT_COLOR_DATE.split(",")[1]), new Integer(ASPECT_COLOR_DATE.split(",")[2])));

	/** The color name. */
	private static Color colorName = resources.createColor(new RGB(new Integer(ASPECT_COLOR_NAME.split(",")[0]),
			new Integer(ASPECT_COLOR_NAME.split(",")[1]), new Integer(ASPECT_COLOR_NAME.split(",")[2])));

	/**
	 * Choose color.
	 * @param name the name
	 * @return the color
	 */
	public final Color chooseColor(final String name)
	{
		if (name.equals("persName")) //$NON-NLS-1$
		{
			return colorPers;
		}
		else if (name.equals("orgName")) //$NON-NLS-1$
		{
			return colorOrg;
		}
		else if (name.equals("placeName")) //$NON-NLS-1$
		{
			return colorPlace;
		}
		else if (name.equals("name")) //$NON-NLS-1$
		{
			return colorName;
		}
		else
		{
			return colorDate;
		}

	}

	/**
	 * Transform style range.
	 * @param tr the tr
	 * @return the style range
	 */
	public final StyleRange transformStyleRange(final TaggingRange tr)
	{
		StyleRange sr = new StyleRange(tr.getStart(), tr.getLength(), null, chooseColor(tr.getName()));
		return sr;
	}
}
