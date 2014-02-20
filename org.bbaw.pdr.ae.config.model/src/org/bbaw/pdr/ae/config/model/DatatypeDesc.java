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
package org.bbaw.pdr.ae.config.model;

import org.bbaw.pdr.ae.metamodel.PdrDate;

/**
 * The Class DatatypeDesc corresponds to <datatypedesc> in dtdl XML.
 * @author Christoph Plutte
 */
public class DatatypeDesc extends ConfigData
{

	/** The _provider. */
	private String _provider;

	/** The _last update. */
	private PdrDate _lastUpdate;

	/** The _usage. */
	private Usage _usage;

	/**
	 * Gets the last update.
	 * @return the last update
	 */
	public final PdrDate getLastUpdate()
	{
		return _lastUpdate;
	}

	/**
	 * Gets the provider.
	 * @return the provider
	 */
	public final String getProvider()
	{
		return _provider;
	}

	/**
	 * Gets the usage.
	 * @return the usage
	 */
	public final Usage getUsage()
	{
		if (_usage == null)
		{
			_usage = new Usage();
		}
		return _usage;
	}

	/**
	 * Checks if is valid.
	 * @return true, if is valid
	 */
	public final boolean isValid()
	{
		String[] datatypes = new String[]
		{"aodl:persName", "aodl:orgName", "aodl:placeName", "aodl:date", "aodl:name", "aodl:semanticStm",
				"aodl:relation"};

		for (String type : datatypes)
		{
			if (!this.getChildren().containsKey(type))
			{
				return false;
			}
		}
		return true;
	}

	/**
	 * Sets the last update.
	 * @param lastUpdate the new last update
	 */
	public final void setLastUpdate(final PdrDate lastUpdate)
	{
		this._lastUpdate = lastUpdate;
	}

	/**
	 * Sets the provider.
	 * @param provider the new provider
	 */
	public final void setProvider(final String provider)
	{
		this._provider = provider;
	}

	/**
	 * Sets the usage.
	 * @param usage the new usage
	 */
	public final void setUsage(final Usage usage)
	{
		this._usage = usage;
	}

}
