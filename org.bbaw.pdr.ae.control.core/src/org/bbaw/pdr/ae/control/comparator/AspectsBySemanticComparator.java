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

import org.bbaw.pdr.ae.common.AEConstants;
import org.bbaw.pdr.ae.common.CommonActivator;
import org.bbaw.pdr.ae.model.Aspect;
import org.eclipse.core.runtime.Platform;

/**
 * comparator for aspects by semantic classification.
 * @author Christoph Plutte
 */
public class AspectsBySemanticComparator implements Comparator<Aspect>
{
	/** provider of primary semantic classification. */
	private String _provider;
	/** ascending flag. */
	private boolean _ascending;

	/**
	 * constructor.
	 */
	public AspectsBySemanticComparator()
	{
		_provider = Platform
				.getPreferencesService()
				.getString(CommonActivator.PLUGIN_ID, "PRIMARY_SEMANTIC_PROVIDER",
						AEConstants.CLASSIFICATION_AUTHORITY, null).toUpperCase();
	}

	/**
	 * constructor with provider.
	 * @param provider semantic provider.
	 */
	public AspectsBySemanticComparator(final String provider)
	{
		this._provider = provider;
	}

	/**
	 * constructor with provider and ascending flag.
	 * @param provider semantic provider
	 * @param ascending ascending flag
	 */
	public AspectsBySemanticComparator(final String provider, final boolean ascending)
	{
		this._ascending = ascending;
		this._provider = provider;
	}

	@Override
	public final int compare(final Aspect a1, final Aspect a2)
	{
		int diff = 0;
		String semantic1 = null;
		String semantic2 = null;
		if (_provider != null && !_provider.equals("ALL"))
		{
			if (a1 != null && a1.getSemanticDim() != null && a1.getSemanticDim().getSemanticStms() != null)
			{
				for (int j = 0; j < a1.getSemanticDim().getSemanticStms().size(); j++)
				{
					if (a1.getSemanticDim().getSemanticStms().get(j).getProvider().equalsIgnoreCase(_provider))
					{
						semantic1 = a1.getSemanticDim().getSemanticStms().get(j).getLabel();
						break;
					}
				}
			}
			if (a2 != null && a2.getSemanticDim() != null && a2.getSemanticDim().getSemanticStms() != null)
			{
				for (int j = 0; j < a2.getSemanticDim().getSemanticStms().size(); j++)
				{
					if (a2.getSemanticDim().getSemanticStms().get(j).getProvider().equalsIgnoreCase(_provider))
					{
						semantic2 = a2.getSemanticDim().getSemanticStms().get(j).getLabel();
						break;
					}
				}
			}
		}
		else
		{
			if (a1 != null && a1.getSemanticDim() != null && a1.getSemanticDim().getSemanticStms() != null)
			{
				semantic1 = a1.getSemanticDim().getSemanticStms().firstElement().getLabel();
			}
			if (a2 != null && a2.getSemanticDim() != null && a2.getSemanticDim().getSemanticStms() != null)
			{
				semantic2 = a2.getSemanticDim().getSemanticStms().firstElement().getLabel();

			}
		}
		if (semantic1 != null && semantic2 != null)
		{
			diff = semantic1.compareTo(semantic2);
		}
		else if (semantic1 != null)
		{
			diff = -1;
		}
		else if (semantic2 != null)
		{
			diff = 1;
		}
		else
		{
			diff = 0;
		}
		if (_ascending)
		{
			return -diff;
		}
		else
		{
			return diff;
		}
	}

}
