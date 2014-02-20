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
 * The Class Time.
 * @author Christoph Plutte
 */
public class Time implements Cloneable
{

	/** The type. */
	private String _type;

	/** The accuracy. */
	private String _accuracy;

	/** The time stamp. */
	private PdrDate _timeStamp;

	public Time()
	{
	}

	public Time(String type, String accuracy, PdrDate timeStamp)
	{
		this._type = type;
		this._accuracy = accuracy;
		this._timeStamp = timeStamp;
	}

	/**
	 * @return cloned time
	 * @see java.lang.Object#clone()
	 */
	@Override
	public final Time clone()
	{
		try
		{
			Time clone = (Time) super.clone();
			if (this._accuracy != null)
			{
				clone._accuracy = new String(this._accuracy);
			}
			if (this._type != null)
			{
				clone._type = new String(this._type);
			}
			if (this._timeStamp != null)
			{
				clone._timeStamp = this._timeStamp.clone();
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
	 * @param time the time
	 * @return true, if successful
	 */
	public final boolean equals(final Time time)
	{
		if (this._type != null && time._type != null)
		{
			if (!this._type.equals(time._type))
			{
				return false;
			}
		}
		else if ((this._type == null && time._type != null) || (this._type != null && time._type == null))
		{
			return false;
		}

		if (this._accuracy != null && time._accuracy != null)
		{
			if (!this._accuracy.equals(time._accuracy))
			{
				return false;
			}
		}
		else if ((this._accuracy == null && time._accuracy != null)
				|| (this._accuracy != null && time._accuracy == null))
		{
			return false;
		}
		if (this._timeStamp != null && time._timeStamp != null)
		{
			if (!this._timeStamp.equals(time._timeStamp))
			{
				return false;
			}
		}
		else if ((this._timeStamp == null && time._timeStamp != null)
				|| (this._timeStamp != null && time._timeStamp == null))
		{
			return false;
		}
		return true;

	}

	/**
	 * Gets the accuracy.
	 * @return the accuracy
	 */
	public final String getAccuracy()
	{
		return _accuracy;
	}

	/**
	 * Gets the time stamp.
	 * @return the time stamp
	 */
	public final PdrDate getTimeStamp()
	{
		return _timeStamp;
	}

	/**
	 * Gets the type.
	 * @return the type
	 */
	public final String getType()
	{
		return _type;
	}

	/**
	 * Checks if is valid.
	 * @return true, if is valid
	 */
	public final boolean isValid()
	{
		if (_timeStamp != null && _timeStamp.isValid())
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * Sets the accuracy.
	 * @param accuracy the new accuracy
	 */
	public final void setAccuracy(final String accuracy)
	{
		this._accuracy = accuracy;
	}

	/**
	 * Sets the time stamp.
	 * @param timeStamp the new time stamp
	 */
	public final void setTimeStamp(final PdrDate timeStamp)
	{
		this._timeStamp = timeStamp;
	}

	/**
	 * Sets the type.
	 * @param type the new type
	 */
	public final void setType(final String type)
	{
		this._type = type;
	}
}
