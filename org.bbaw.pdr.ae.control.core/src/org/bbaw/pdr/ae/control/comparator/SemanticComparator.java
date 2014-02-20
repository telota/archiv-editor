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
package org.bbaw.pdr.ae.control.comparator;

import java.util.Comparator;
import java.util.HashMap;

import org.bbaw.pdr.ae.config.model.ConfigData;
import org.bbaw.pdr.ae.config.model.ConfigItem;
import org.bbaw.pdr.ae.control.facade.Facade;

/**
 * semantic comparator to compare semantic classifications according to
 * priority.
 * @author Christoph Plutte
 */
public class SemanticComparator implements Comparator<String>
{

	/** singleton facade. */
	private Facade _facade = Facade.getInstanz();
	/** semantics as hashmap of configData. */
	private HashMap<String, ConfigData> _semantics = new HashMap<String, ConfigData>();

	/**
	 * constructor with semantic provider.
	 * @param provider semantic classification provider
	 */
	public SemanticComparator(final String provider)
	{
		if (provider != null && !provider.equals("ALL"))
		{
			_semantics = _facade.getConfigs().get(provider.toUpperCase()).getChildren().get("aodl:semanticStm")
					.getChildren();
		}
		else
		{
			for (String s : _facade.getConfigs().keySet())
			{
				_semantics.putAll(_facade.getConfigs().get(s.toUpperCase()).getChildren().get("aodl:semanticStm")
						.getChildren());
			}
		}
	}

	@Override
	public final int compare(final String sem1, final String sem2)
	{
		ConfigItem ci1 = (ConfigItem) _semantics.get(sem1);
		ConfigItem ci2 = (ConfigItem) _semantics.get(sem2);
		if (ci1 != null && ci2 != null)
		{
			return ci1.getPriority() - ci2.getPriority();
		}
		else if (ci1 != null)
		{
			return -1;
		}
		else
		{
			return 1;
		}

	}

}
