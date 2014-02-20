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
 * The Class SpatialDim.
 * @author Christoph Plutte
 */
public class SpatialDim implements Cloneable
{

	/** The spatial stms. */
	private Vector<SpatialStm> _spatialStms;

	/**
	 * Instantiates a new spatial dim.
	 */
	public SpatialDim()
	{

	}

	/**
	 * @return cloned spatial dimension
	 * @see java.lang.Object#clone()
	 */
	@Override
	public final SpatialDim clone()
	{
		try
		{
			SpatialDim clone = (SpatialDim) super.clone();
			if (this._spatialStms != null)
			{
				clone._spatialStms = new Vector<SpatialStm>(this._spatialStms.size());
				for (int i = 0; i < this._spatialStms.size(); i++)
				{
					clone._spatialStms.add(this._spatialStms.get(i).clone());
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
	 * @param sDim the s dim
	 * @return true, if successful
	 */
	public final boolean equals(final SpatialDim sDim)
	{
		if (this.getSpatialStms() != null && sDim.getSpatialStms() != null)
		{
			if (!(this.getSpatialStms().size() == sDim.getSpatialStms().size()))
			{
				return false;
			}
			for (int i = 0; i < this.getSpatialStms().size(); i++)
			{
				if (!this.getSpatialStms().get(i).equals(sDim.getSpatialStms().get(i)))
				{
					return false;
				}
			}
		}
		else if ((this.getSpatialStms() == null && sDim.getSpatialStms() != null)
				|| (this.getSpatialStms() != null && sDim.getSpatialStms() == null))
		{
			return false;
		}
		return true;

	}

	/**
	 * Gets the spatial stms.
	 * @return the spatial stms
	 */
	public final Vector<SpatialStm> getSpatialStms()
	{
		if (_spatialStms == null)
		{
			_spatialStms = new Vector<SpatialStm>(1);
		}
		return _spatialStms;
	}

	/**
	 * Checks if is valid.
	 * @return true, if is valid
	 */
	public final boolean isValid()
	{
		if (_spatialStms != null)
		{
			for (SpatialStm s : _spatialStms)
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
		if (_spatialStms != null)
		{
			_spatialStms.removeElementAt(index);
		}
		return true;

	}

	/**
	 * Sets the spatial stms.
	 * @param spatialStms the new spatial stms
	 */
	public final void setSpatialStms(final Vector<SpatialStm> spatialStms)
	{
		this._spatialStms = spatialStms;
	}
}
