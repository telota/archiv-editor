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
 * The Class TimeStm.
 * @author Christoph Plutte
 */
public class TimeStm implements Cloneable
{

	/** The type. */
	private String _type;

	/** The times. */
	private Vector<Time> _times;

	/**
	 * Instantiates a new time stm.
	 */
	public TimeStm()
	{
		this._type = "undefined";
	}

	/**
	 * @return cloned time statement
	 * @see java.lang.Object#clone()
	 */
	@Override
	public final TimeStm clone()
	{
		try
		{
			TimeStm clone = (TimeStm) super.clone();
			if (this._type != null)
			{
				clone._type = new String(this._type);
			}
			if (this._times != null)
			{
				clone._times = new Vector<Time>(this._times.size());
				for (int i = 0; i < this._times.size(); i++)
				{
					clone._times.add(this._times.get(i).clone());
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
	 * @param tStm the t stm
	 * @return true, if successful
	 */
	public final boolean equals(final TimeStm tStm)
	{
		if (this.getTimes() != null && tStm.getTimes() != null)
		{
			if (!(this.getTimes().size() == tStm.getTimes().size()))
			{
				return false;
			}
			for (int i = 0; i < this.getTimes().size(); i++)
			{
				if (!this.getTimes().get(i).equals(tStm.getTimes().get(i)))
				{
					return false;
				}
			}
		}
		else if ((this.getTimes() == null && tStm.getTimes() != null)
				|| (this.getTimes() != null && tStm.getTimes() == null))
		{
			return false;
		}

		if (this._type != null && tStm._type != null)
		{
			if (!this._type.equals(tStm._type))
			{
				return false;
			}
		}
		else if ((this._type == null && tStm._type != null) || (this._type != null && tStm._type == null))
		{
			return false;
		}
		return true;

	}

	/**
	 * Gets the times.
	 * @return the times
	 */
	public final Vector<Time> getTimes()
	{
		if (_times == null)
		{
			_times = new Vector<Time>(1);
		}
		return _times;
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
		if (_times != null)
		{
			for (Time t : _times)
			{
				if (!t.isValid())
				{
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Sets the times.
	 * @param times the new times
	 */
	public final void setTimes(final Vector<Time> times)
	{
		this._times = times;
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
