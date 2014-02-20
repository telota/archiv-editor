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
package org.bbaw.pdr.ae.control.core;

import java.util.HashMap;

import org.bbaw.pdr.ae.common.CommonActivator;
import org.bbaw.pdr.ae.config.model.ConfigData;
import org.bbaw.pdr.ae.control.facade.Facade;
import org.eclipse.core.runtime.Platform;

/**
 * Class manages favorite Markup settings.
 * @author Christoph Plutte
 */
public class FavoriteMarkupManager
{
	/** Facade. */
	private Facade _facade = Facade.getInstanz();

	/**
	 * get ConfigData by Path.
	 * @param inpath path to configData
	 * @return ConfigData
	 */
	private ConfigData getMarkupByPath(final String inpath)
	{
		String path = inpath;
		if (path != null && !"".equals(inpath) &&  !path.equals("EMPTY"))
		{
			// StringBuilder sb = new StringBuilder();
			// for (char c : path.toCharArray())
			// {
			// if (c)
			// }
			path = path.replaceAll("\\\\", "");
			// System.out.println("path after replace " + path);
			ConfigData cd = null;

			String[] keys = path.split("\\|");
			if (keys[0] != null)
			{
				cd = _facade.getConfigs().get(keys[0]);
				for (int i = 1; i < keys.length; i++)
				{
					// System.out.println("key " + keys[i]);
					if (keys[i] != null && cd != null && cd.getChildren() != null)
					{
						cd = cd.getChildren().get(keys[i]);

					}
				}
			}
			return cd;
		}
		else
		{
			return null;
		}
	}

	/**
	 * load favorite Markups.
	 * @return HashMap of favorite Markup.
	 */
	public final HashMap<String, ConfigData> loadFavoriteMarkups()
	{
		HashMap<String, ConfigData> favoriteMarkups = new HashMap<String, ConfigData>();
		boolean load = true;
		int i = 0;
		while (load)
		{
			String path = Platform.getPreferencesService().getString(CommonActivator.PLUGIN_ID, "FAVORITE_MARKUP" + i,
					"", null);
			ConfigData cd = getMarkupByPath(path);
			if (cd != null && cd.getValue() != null)
			{
				favoriteMarkups.put(cd.getValue(), cd);
				i++;
			}
			else
			{
				load = false;
			}
		}
		return favoriteMarkups;

	}

}
