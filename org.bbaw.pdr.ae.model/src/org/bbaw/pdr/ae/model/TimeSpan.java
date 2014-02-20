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

import org.bbaw.pdr.ae.metamodel.PdrDate;

/**
 * The Class TimeSpan.
 * @author Christoph Plutte
 */
public class TimeSpan implements Cloneable
{

	/** The date from. */
	private PdrDate _dateFrom;

	/** The date to. */
	private PdrDate _dateTo;

	/**
	 * @return cloned time span
	 * @see java.lang.Object#clone()
	 */
	@Override
	public final TimeSpan clone()
	{
		try
		{
			TimeSpan clone = (TimeSpan) super.clone();
			if (this._dateFrom != null)
			{
				clone._dateFrom = this._dateFrom.clone();
			}
			if (this._dateTo != null)
			{
				clone._dateTo = this._dateTo.clone();
			}

			return clone;
		}
		catch (CloneNotSupportedException e)
		{
			throw new InternalError();
		}
	}

	/**
	 * Gets the date from.
	 * @return the date from
	 */
	public final PdrDate getDateFrom()
	{
		if (_dateFrom == null)
		{
			_dateFrom = new PdrDate(0, 0, 0);
		}
		return _dateFrom;
	}

	/**
	 * Gets the date to.
	 * @return the date to
	 */
	public final PdrDate getDateTo()
	{
		if (_dateTo == null)
		{
			_dateTo = new PdrDate(0, 0, 0);
		}
		return _dateTo;
	}

	/**
	 * Sets the date from.
	 * @param dateFrom the new date from
	 */
	public final void setDateFrom(final PdrDate dateFrom)
	{
		this._dateFrom = dateFrom;
	}

	/**
	 * Sets the date to.
	 * @param dateTo the new date to
	 */
	public final void setDateTo(final PdrDate dateTo)
	{
		this._dateTo = dateTo;
	}
}
