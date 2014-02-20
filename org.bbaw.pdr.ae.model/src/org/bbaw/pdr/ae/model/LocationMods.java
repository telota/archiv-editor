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
package org.bbaw.pdr.ae.model;

/**
 * The Class LocationMods.
 * @author Christoph Plutte
 */
public class LocationMods implements Cloneable
{

	/** The url. */
	private String _url;

	/** The physical location. */
	private String _physicalLocation;

	/** The shelf locator. */
	private String _shelfLocator;

	/**
	 * @return cloned loacationMods
	 * @see java.lang.Object#clone()
	 */
	@Override
	public final LocationMods clone()
	{
		try
		{
			LocationMods clone = (LocationMods) super.clone();
			if (this._physicalLocation != null)
			{
				clone._physicalLocation = new String(this._physicalLocation);
			}
			if (this._shelfLocator != null)
			{
				clone._shelfLocator = new String(this._shelfLocator);
			}
			if (this._url != null)
			{
				clone._url = new String(this._url);
			}
			return clone;
		}
		catch (CloneNotSupportedException e)
		{
			throw new InternalError();
		}
	}

	/**
	 * Gets the physical location.
	 * @return the physical location
	 */
	public final String getPhysicalLocation()
	{
		return _physicalLocation;
	}

	/**
	 * @return the shelfLocator.
	 */
	public final String getShelfLocator()
	{
		return _shelfLocator;
	}

	/**
	 * Gets the url.
	 * @return the url
	 */
	public final String getUrl()
	{
		return _url;
	}

	/**
	 * Sets the physical location.
	 * @param physicalLocation the new physical location
	 */
	public final void setPhysicalLocation(final String physicalLocation)
	{
		this._physicalLocation = physicalLocation;
	}

	/**
	 * @param shelfLocator the shelfLocator to set.
	 */
	public final void setShelfLocator(final String shelfLocator)
	{
		this._shelfLocator = shelfLocator;
	}

	/**
	 * Sets the url.
	 * @param url the new url
	 */
	public final void setUrl(final String url)
	{
		this._url = url;
	}
}
