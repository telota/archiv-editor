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
package org.bbaw.pdr.ae.model.search;

import java.util.HashMap;
import java.util.Vector;

import org.bbaw.pdr.ae.metamodel.IAEPresentable;

/**
 * The Class PdrQuery.
 * @author Christoph Plutte
 */
public class PdrQuery implements Cloneable
{
	/** 0 for aspect, 1 for person, 2 for reference. */
	private int _type;

	/** The search level. */
	private int _searchLevel;

	/** The key. */
	private String _key;

	/** The criterias. */
	private Vector<Criteria> _criterias = new Vector<Criteria>(5);

	/** The facets. */
	private HashMap<String, IAEPresentable> _facets;

	@Override
	public final PdrQuery clone()
	{
		PdrQuery clone = null;
		try
		{
			clone = (PdrQuery) super.clone();

			if (this._key != null)
			{
				clone._key = new String(this._key);
			}
			clone._searchLevel = this._searchLevel;
			clone._type = this._type;
			if (this._criterias != null)
			{
				clone._criterias = new Vector<Criteria>(this._criterias.size());
				for (int i = 0; i < this._criterias.size(); i++)
				{
					clone._criterias.add(this._criterias.get(i).clone());
				}
			}
			if (this._facets != null)
			{
				clone._facets = null;
			}
		}
		catch (CloneNotSupportedException e)
		{
			e.printStackTrace();
		}

		return clone;
	}

	/**
	 * Gets the criterias.
	 * @return the criterias
	 */
	public final Vector<Criteria> getCriterias()
	{
		return _criterias;
	}

	/**
	 * Gets the facets.
	 * @return the facets
	 */
	public final HashMap<String, IAEPresentable> getFacets()
	{
		return _facets;
	}

	/**
	 * Gets the key.
	 * @return the key
	 */
	public final String getKey()
	{
		return _key;
	}

	/**
	 * Gets the search level.
	 * @return the search level
	 */
	public final int getSearchLevel()
	{
		return _searchLevel;
	}

	/**
	 * Gets the type.
	 * @return the type
	 */
	public final int getType()
	{
		return _type;
	}

	/**
	 * Sets the criterias.
	 * @param criterias the new criterias
	 */
	public final void setCriterias(final Vector<Criteria> criterias)
	{
		this._criterias = criterias;
	}

	/**
	 * Sets the facets.
	 * @param facets the facets
	 */
	public final void setFacets(final HashMap<String, IAEPresentable> facets)
	{
		this._facets = facets;
	}

	/**
	 * Sets the key.
	 * @param key the new key
	 */
	public final void setKey(final String key)
	{
		this._key = key;
	}

	/**
	 * Sets the search level.
	 * @param searchLevel the new search level
	 */
	public final void setSearchLevel(final int searchLevel)
	{
		this._searchLevel = searchLevel;
	}

	/**
	 * Sets the type.
	 * @param type the new type
	 */
	public final void setType(final int type)
	{
		this._type = type;
	}
}
