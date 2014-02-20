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

import java.util.Vector;

import org.bbaw.pdr.ae.metamodel.PdrDate;

/**
 * The Class PartMods.
 * @author Christoph Plutte
 */
public class PartMods implements Cloneable
{

	/** The details. */
	private Vector<DetailMods> _details;;

	/** The extends mods. */
	private Vector<ExtendMods> _extendsMods;;

	/** The dates. */
	private Vector<PdrDate> _dates;

	/**
	 * @return cloned partmods
	 * @see java.lang.Object#clone()
	 */
	@Override
	public final PartMods clone()
	{
		PartMods clone;
		try
		{
			clone = (PartMods) super.clone();
			if (this._dates != null)
			{
				clone._dates = new Vector<PdrDate>(this._dates.size());
				for (int i = 0; i < this._dates.size(); i++)
				{
					clone._dates.add(this._dates.get(i).clone());
				}
			}
			if (this._details != null)
			{
				clone._details = new Vector<DetailMods>(this._details.size());
				for (int i = 0; i < this._details.size(); i++)
				{
					clone._details.add(this._details.get(i).clone());
				}
			}
			if (this._extendsMods != null)
			{
				clone._extendsMods = new Vector<ExtendMods>(this._extendsMods.size());
				for (int i = 0; i < this._extendsMods.size(); i++)
				{
					clone._extendsMods.add(this._extendsMods.get(i).clone());
				}
			}
			return clone;
		}
		catch (CloneNotSupportedException e)
		{
			throw new InternalError();
		}

	}

	/**
	 * Gets the dates.
	 * @return the dates
	 */
	public final Vector<PdrDate> getDates()
	{
		return _dates;
	}

	/**
	 * Gets the detail issue.
	 * @return the detail issue
	 */
	public final DetailMods getDetailIssue()
	{
		if (_details != null)
		{
			for (DetailMods detail : _details)
			{
				if (detail.getType() != null && detail.getType().equals("issue"))
				{
					return detail;
				}
			}
		}
		return null;
	}

	/**
	 * Gets the details.
	 * @return the details
	 */
	public final Vector<DetailMods> getDetails()
	{
		return _details;
	}

	/**
	 * Gets the detail volume.
	 * @return the detail volume
	 */
	public final DetailMods getDetailVolume()
	{
		if (_details != null)
		{
			for (DetailMods detail : _details)
			{
				if (detail.getType() != null && detail.getType().equals("volume"))
				{
					return detail;
				}
			}
		}
		return null;
	}

	/**
	 * Gets the extend pages.
	 * @return the extend pages
	 */
	public final ExtendMods getExtendPages()
	{
		if (_extendsMods != null)
		{
			for (ExtendMods extend : _extendsMods)
			{
				if (extend.getUnit() != null && extend.getUnit().equals("pages"))
				{
					return extend;
				}
			}
		}
		return null;
	}

	/**
	 * Gets the extends mods.
	 * @return the extends mods
	 */
	public final Vector<ExtendMods> getExtendsMods()
	{
		return _extendsMods;
	}

	/**
	 * Sets the dates.
	 * @param dates the new dates
	 */
	public final void setDates(final Vector<PdrDate> dates)
	{
		this._dates = dates;
	}

	/**
	 * Sets the details.
	 * @param details the new details
	 */
	public final void setDetails(final Vector<DetailMods> details)
	{
		this._details = details;
	}

	/**
	 * Sets the extends mods.
	 * @param extendsMods the new extends mods
	 */
	public final void setExtendsMods(final Vector<ExtendMods> extendsMods)
	{
		this._extendsMods = extendsMods;
	}
}
