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
package org.bbaw.pdr.ae.view.control.filters;

import org.bbaw.pdr.ae.common.interfaces.AEFilter;
import org.bbaw.pdr.ae.model.Aspect;
import org.bbaw.pdr.ae.model.Time;
import org.bbaw.pdr.ae.model.TimeStm;

/**
 * The Class AspectYearFilter.
 * @author Christoph Plutte
 */
public class AspectYearFilter implements AEFilter
{

	/** The aspect min year. */
	private int _aspectMinYear;

	/** The aspect max year. */
	private int _aspectMaxYear;

	/**
	 * Instantiates a new aspect year filter.
	 * @param aspectMinYear the aspect min year
	 * @param aspectMaxYear the aspect max year
	 */
	public AspectYearFilter(final int aspectMinYear, final int aspectMaxYear)
	{
		this._aspectMaxYear = aspectMaxYear;
		this._aspectMinYear = aspectMinYear;
	}

	/**
	 * Gets the aspect max year.
	 * @return the aspect max year
	 */
	public final int getAspectMaxYear()
	{
		return _aspectMaxYear;
	}

	/**
	 * Gets the aspect min year.
	 * @return the aspect min year
	 */
	public final int getAspectMinYear()
	{
		return _aspectMinYear;
	}

	@Override
	public final boolean select(final Object element)
	{
		boolean select = true;
		if (element instanceof Aspect)
		{
			Aspect a = (Aspect) element;
			if (a.getTimeDim() != null && a.getTimeDim().getTimeStms() != null)
			{
				for (TimeStm tStm : a.getTimeDim().getTimeStms())
				{
					if (tStm.getTimes() != null)
					{
						for (Time time : tStm.getTimes())
						{
							if (time.getTimeStamp() != null)
							{
								if (time.getTimeStamp().getYear() < _aspectMaxYear
										|| time.getTimeStamp().getYear() > _aspectMaxYear)
								{
									select = false;
								}
								else
								{
									return true;
								}
							}
						}
					}
				}
			}
		}
		return select;
	}

	/**
	 * Sets the aspect max year.
	 * @param aspectMaxYear the new aspect max year
	 */
	public final void setAspectMaxYear(final int aspectMaxYear)
	{
		this._aspectMaxYear = aspectMaxYear;
	}

	/**
	 * Sets the aspect min year.
	 * @param aspectMinYear the new aspect min year
	 */
	public final void setAspectMinYear(final int aspectMinYear)
	{
		this._aspectMaxYear = aspectMinYear;
	}

}
