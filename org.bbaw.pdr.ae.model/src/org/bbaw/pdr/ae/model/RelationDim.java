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

import org.bbaw.pdr.ae.metamodel.PdrId;

/**
 * The Class RelationDim.
 * @author Christoph Plutte
 */
public class RelationDim implements Cloneable
{

	/** The relation stms. */
	private Vector<RelationStm> _relationStms;

	/**
	 * Instantiates a new relation dim.
	 */
	public RelationDim()
	{

	}

	/**
	 * @return cloned relation dimension
	 * @see java.lang.Object#clone()
	 */
	@Override
	public final RelationDim clone()
	{
		try
		{
			RelationDim clone = (RelationDim) super.clone();
			if (this._relationStms != null)
			{
				clone._relationStms = new Vector<RelationStm>(this._relationStms.size());
				for (int i = 0; i < this._relationStms.size(); i++)
				{
					clone._relationStms.add(this._relationStms.get(i).clone());
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
	 * Gets the relation stms.
	 * @return the relation stms
	 */
	public final Vector<RelationStm> getRelationStms()
	{
		if (_relationStms == null)
		{
			_relationStms = new Vector<RelationStm>(1);
		}
		return _relationStms;
	}

	/**
	 * Checks if is valid.
	 * @return true, if is valid
	 */
	public final boolean isValid()
	{
		if (_relationStms != null)
		{
			for (RelationStm s : _relationStms)
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
	public final boolean remove(final Integer index)
	{
		_relationStms.removeElementAt(index);
		return true;

	}

	/**
	 * Sets the relation stms.
	 * @param relationStms the new relation stms
	 */
	public final void setRelationStms(final Vector<RelationStm> relationStms)
	{
		this._relationStms = relationStms;
	}

	/**
	 * Similar relations.
	 * @param relationDim the relation dim
	 * @param a1 the a1
	 * @param a2 the a2
	 * @param p1 the p1
	 * @param p2 the p2
	 * @return true, if successful
	 */
	public final boolean similarRelations(final RelationDim relationDim, final PdrId a1, final PdrId a2,
			final PdrId p1, final PdrId p2)
	{
		if (this.getRelationStms() != null && relationDim.getRelationStms() != null)
		{
			if (this.getRelationStms().size() != relationDim.getRelationStms().size())
			{
				return false;
			}
			for (int i = 0; i < this.getRelationStms().size(); i++)
			{
				if (this.getRelationStms().get(i).equals(relationDim.getRelationStms().get(i)))
				{

				}
				else if (!this.getRelationStms().get(i)
						.similarRelations(relationDim.getRelationStms().get(i), a1, a2, p1, p2))
				{
					return false;
				}
			}
		}
		else if ((this.getRelationStms() != null && relationDim.getRelationStms() == null)
				|| (this.getRelationStms() == null && relationDim.getRelationStms() != null))
		{
			return false;
		}
		return true;
	}
	/**
	 * Equals.
	 * @param sDim the s dim
	 * @return true, if successful
	 */
	public final boolean equals(final RelationDim rDim)
	{
		if (this.getRelationStms()!= null && rDim.getRelationStms() != null)
		{
			if (!(this.getRelationStms().size() == rDim.getRelationStms().size()))
			{
				return false;
			}
			for (int i = 0; i < this.getRelationStms().size(); i++)
			{
				if (!this.getRelationStms().get(i).equals(rDim.getRelationStms().get(i)))
				{
					return false;
				}
			}
		}
		else if ((this.getRelationStms() == null && rDim.getRelationStms() != null)
				|| (this.getRelationStms() != null && rDim.getRelationStms() == null))
		{
			return false;
		}
		return true;

	}

}
