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
 * Manager to manage proposals for faceted search.
 * @author Christoph Plutte
 */
public class FacetProposalManager
{

	/** singleton facade. */
	private Facade _facade = Facade.getInstanz();

	/**
	 * get ConfigData by given path.
	 * @param inpath path to configData
	 * @return configData
	 */
	private ConfigData getMarkupByPath(final String inpath)
	{
		String path = inpath;

		if (path != null && !path.equals("EMPTY"))
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
				if (cd != null)
				{
					for (int i = 1; i < keys.length; i++)
					{
						// System.out.println("key " + keys[i]);
						if (keys[i] != null && cd.getChildren() != null)
						{
							cd = cd.getChildren().get(keys[i]);

						}
					}
				}
				// if (keys[1] != null && cd.getChildren() != null)
				// {
				// cd = cd.getChildren().get(keys[1]);
				// if (keys[2] != null && cd.getChildren() != null)
				// {
				// cd = cd.getChildren().get(keys[2]);
				// if (keys[3] != null && cd.getChildren() != null)
				// {
				// cd = cd.getChildren().get(keys[3]);
				//
				// if (keys[4] != null && cd.getChildren() != null)
				// {
				// cd = cd.getChildren().get(keys[4]);
				//
				// }
				// }
				// }
				// }
			}
			return cd;
		}
		else
		{
			return null;
		}
	}

	/**
	 * loads the proposals for faceted aspects search.
	 * @return hashmap of configData.
	 */
	public final HashMap<String, ConfigData> loadAspectFacetProposals()
	{
		HashMap<String, ConfigData> aspectFacetProposals = new HashMap<String, ConfigData>();
		boolean load = true;
		int i = 0;
		while (load)
		{
			String path = Platform.getPreferencesService().getString(CommonActivator.PLUGIN_ID, "FACET_PROPOSAL" + i,
					"", null);
			ConfigData cd = getMarkupByPath(path);
			if (cd != null)
			{
				aspectFacetProposals.put(cd.getValue(), cd);
				i++;
			}
			else
			{
				load = false;
			}
		}
		return aspectFacetProposals;
	}

	/**
	 * load proposals for faceted person search.
	 * @return hashmap of ConfigData with proposals
	 */
	public final HashMap<String, ConfigData> loadFacetProposals()
	{
		HashMap<String, ConfigData> facetProposals = new HashMap<String, ConfigData>();
		boolean load = true;
		int i = 0;
		while (load)
		{
			String path = Platform.getPreferencesService().getString(CommonActivator.PLUGIN_ID, "FACET_PROPOSAL" + i,
					"", null);
			ConfigData cd = getMarkupByPath(path);
			if (cd != null)
			{
				facetProposals.put(cd.getValue(), cd);
				i++;
			}
			else
			{
				load = false;
			}
		}
		return facetProposals;
	}
}
