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
/*
 * @author: Christoph Plutte
 */
package org.bbaw.pdr.ae.dateparser;

import java.util.Locale;
import java.util.Vector;

import org.bbaw.pdr.ae.view.control.interfaces.IDateParser;
import org.bbaw.pdr.dates.DatesResult;
import org.bbaw.pdr.dates.DatesUtils;

/**
 * The Class DateParser.
 * @author Christoph Plutte
 */
public class DateParser implements IDateParser
{
	@Override
	public final Vector<String> getParsedDates(final String text)
	{
		if (text == null)
		{
			return null;
		}
		String lang = Activator.getDefault().getPreferenceStore().getString("DATE_PARSING_LANG");
		if (lang == null || !lang.equals("de") || !lang.equals("it"))
		{
			if (Locale.getDefault().getLanguage().equals("it"))
			{
				lang = "it";
			}
			else
			{
				lang = "de";
			}
			Activator.getDefault().getPreferenceStore().putValue("DATE_PARSING_LANG", lang);
		}
		Vector<String> dates = null;
		try
		{
			DatesResult result = new DatesResult(text);
			DatesUtils.findOccurrences(result, lang);

			dates = new Vector<String>(result.getOccurrences().size());
			for (int i = 0; i < result.getOccurrences().size(); i++)
			{
				dates.add(result.getOccurrences().get(i).getISO());
			}
		}
		catch (Exception e)
		{
			return null;
		}
		if (dates.size() > 0)
		{
			return dates;
		}
		else
		{
			return null;
		}

	}

}
