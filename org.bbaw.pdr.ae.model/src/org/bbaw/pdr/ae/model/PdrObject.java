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

import java.util.HashSet;

import org.bbaw.pdr.ae.metamodel.PdrId;
import org.bbaw.pdr.ae.metamodel.PdrMetaObject;

/**
 * The Class PdrObject.
 * @author Christoph Plutte
 */
public class PdrObject extends PdrMetaObject implements Cloneable
{

	/** The is new. */
	private boolean _isNew;

	/** The is dirty. */
	private boolean _isDirty;

	/** The aspect ids. */
	private HashSet<PdrId> _aspectIds;

	private boolean _aspectsLoaded = false;

	/**
	 * Instantiates a new pdr object.
	 * @param pdrId the pdr id
	 */
	public PdrObject(final PdrId pdrId)
	{
		super(pdrId);
	}

	/**
	 * Instantiates a new pdr object.
	 * @param idString the id string
	 */
	public PdrObject(final String idString)
	{
		super(idString);
	}

	/**
	 * @return cloned pdrObject
	 * @see org.bbaw.pdr.ae.metamodel.PdrMetaObject#clone()
	 */
	@Override
	public PdrObject clone()
	{
		PdrObject clone = (PdrObject) super.clone();
		if (this._aspectIds != null)
		{
			clone._aspectIds = new HashSet<PdrId>(this._aspectIds.size());
			for (PdrId id : _aspectIds)
			{
				clone._aspectIds.add(id.clone());
			}
		}

		clone._isDirty = this._isDirty;
		clone._isNew = this._isNew;
		return clone;
	}

	/**
	 * Equals.
	 * @param obj the pdr obj
	 * @return true, if successful
	 */
	@Override
	public boolean equals(final Object obj)
	{
		if (obj != null && obj instanceof PdrObject)
		{
			PdrObject pdrObj = (PdrObject) obj;
			return this.getPdrId().equals(pdrObj.getPdrId());
		}
		return false;
	}

	/**
	 * Gets the aspect ids.
	 * @return the aspect ids
	 */
	public final HashSet<PdrId> getAspectIds()
	{
		if (_aspectIds == null)
		{
			_aspectIds = new HashSet<PdrId>(3);
		}
		return _aspectIds;
	}

	/**
	 * @return
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		return this.getPdrId().hashCode();
	}

	/**
	 * Checks if is dirty.
	 * @return true, if is dirty
	 */
	public final boolean isDirty()
	{
		return _isDirty;
	}

	/**
	 * Checks if is new.
	 * @return true, if is new
	 */
	public final boolean isNew()
	{
		return _isNew;
	}

	/**
	 * Removes the aspect.
	 * @param id the id
	 */
	public final void removeAspect(final PdrId id)
	{
		if (_aspectIds != null && id != null)
		{
			_aspectIds.remove(id.toString());
		}
	}

	/**
	 * Sets the aspect ids.
	 * @param aspectIds the new aspect ids
	 */
	public final void setAspectIds(final HashSet<PdrId> aspectIds)
	{
		this._aspectIds = aspectIds;
	}

	/**
	 * Sets the dirty.
	 * @param isDirty the new dirty
	 */
	public final void setDirty(final boolean isDirty)
	{
		this._isDirty = isDirty;
	}

	/**
	 * Sets the new.
	 * @param isNew the new new
	 */
	public final void setNew(final boolean isNew)
	{
		this._isNew = isNew;
	}

	/**
	 * @param id new id
	 * @see org.bbaw.pdr.ae.metamodel.PdrMetaObject#setPdrId(org.bbaw.pdr.ae.metamodel.PdrId)
	 */
	@Override
	protected void setPdrId(final PdrId id)
	{
		super.setPdrId(id);
	}

	public boolean isAspectsLoaded()
	{
		return _aspectsLoaded;
	}

	public void setAspectsLoaded(boolean _aspectsLoaded)
	{
		this._aspectsLoaded = _aspectsLoaded;
	}

	public void addAspectId(PdrId id)
	{
		if (id != null && !getAspectIds().contains(id))
		{
			getAspectIds().add(id);
		}

	}

}
