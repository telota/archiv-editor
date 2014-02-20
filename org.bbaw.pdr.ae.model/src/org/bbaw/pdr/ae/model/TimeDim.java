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

/**
 * The Class TimeDim.
 * @author Christoph Plutte
 */
public class TimeDim implements Cloneable
{

	/** The time stms. */
	private Vector<TimeStm> _timeStms;

	/**
	 * Instantiates a new time dim.
	 */
	public TimeDim()
	{

	}

	/**
	 * @return cloned time dimension
	 * @see java.lang.Object#clone()
	 */
	@Override
	public final TimeDim clone()
	{
		try
		{
			TimeDim clone = (TimeDim) super.clone();
			if (this._timeStms != null)
			{
				clone._timeStms = new Vector<TimeStm>(this._timeStms.size());
				for (int i = 0; i < this._timeStms.size(); i++)
				{
					clone._timeStms.add(this._timeStms.get(i).clone());
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
	 * Equals.
	 * @param tDim the t dim
	 * @return true, if successful
	 */
	public final boolean equals(final TimeDim tDim)
	{
		if (this.getTimeStms() != null && tDim.getTimeStms() != null)
		{
			if (!(this.getTimeStms().size() == tDim.getTimeStms().size()))
			{
				return false;
			}
			for (int i = 0; i < this.getTimeStms().size(); i++)
			{
				if (!this.getTimeStms().get(i).equals(tDim.getTimeStms().get(i)))
				{
					return false;
				}
			}
		}
		else if ((this.getTimeStms() == null && tDim.getTimeStms() != null)
				|| (this.getTimeStms() != null && tDim.getTimeStms() == null))
		{
			return false;
		}
		return true;

	}

	/**
	 * Gets the time stms.
	 * @return the time stms
	 */
	public final Vector<TimeStm> getTimeStms()
	{
		if (_timeStms == null)
		{
			_timeStms = new Vector<TimeStm>(1);
		}
		return _timeStms;
	}

	/**
	 * Checks if is valid.
	 * @return true, if is valid
	 */
	public final boolean isValid()
	{
		if (_timeStms != null)
		{
			for (TimeStm s : _timeStms)
			{
				if (!s.isValid())
				{
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Removes the.
	 * @param index the index
	 * @return true, if successful
	 */
	public final boolean remove(final int index)
	{
		if (_timeStms != null)
		{
			_timeStms.removeElementAt(index);
		}
		return true;

	}

	/**
	 * Sets the time stms.
	 * @param timeStms the new time stms
	 */
	public final void setTimeStms(final Vector<TimeStm> timeStms)
	{
		this._timeStms = timeStms;
	}
}
