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
 * The Class RelationStm.
 * @author Christoph Plutte
 */
public class RelationStm implements Cloneable
{

	/** The subject. */
	private PdrId _subject;

	/** The relations. */
	private Vector<Relation> _relations;

	/**
	 * Instantiates a new relation stm.
	 */
	public RelationStm()
	{

	}

	/**
	 * @return cloned relation statement
	 * @see java.lang.Object#clone()
	 */
	@Override
	public final RelationStm clone()
	{
		try
		{
			RelationStm clone = (RelationStm) super.clone();
			if (this._subject != null)
			{
				clone._subject = this._subject.clone();
			}
			if (this._relations != null)
			{
				clone._relations = new Vector<Relation>(this._relations.size());
				for (int i = 0; i < this._relations.size(); i++)
				{
					clone._relations.add(this._relations.get(i).clone());
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
	 * @param rStm the r stm
	 * @return true, if successful
	 */
	public final boolean equals(final RelationStm rStm)
	{
		if (this.getRelations() != null && rStm.getRelations() != null)
		{
			if (!(this.getRelations().size() == rStm.getRelations().size()))
			{
				return false;
			}
			for (int i = 0; i < this.getRelations().size(); i++)
			{
				if (!this.getRelations().get(i).equals(rStm.getRelations().get(i)))
				{
					return false;
				}
			}
		}
		else if ((this.getRelations() == null && rStm.getRelations() != null)
				|| (this.getRelations() != null && rStm.getRelations() == null))
		{
			return false;
		}

		if (this._subject != null && rStm._subject != null)
		{
			if (!this._subject.equals(rStm._subject))
			{
				return false;
			}
		}
		else if ((this._subject == null && rStm._subject != null) || (this._subject != null && rStm._subject == null))
		{
			return false;
		}
		return true;

	}

	/**
	 * Gets the relations.
	 * @return the relations
	 */
	public final Vector<Relation> getRelations()
	{
		if (_relations == null)
		{
			_relations = new Vector<Relation>(1);
		}
		return _relations;
	}

	/**
	 * Gets the subject.
	 * @return the subject
	 */
	public final PdrId getSubject()
	{
		return _subject;
	}

	/**
	 * Checks if is valid.
	 * @return true, if is valid
	 */
	public final boolean isValid()
	{
		if (_subject == null || !_subject.isValid())
		{
			return false;
		}
		if (_relations != null)
		{
			for (Relation r : _relations)
			{
				if (!r.isValid())
				{
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Sets the relations.
	 * @param relations the new relations
	 */
	public final void setRelations(final Vector<Relation> relations)
	{
		this._relations = relations;
	}

	/**
	 * Sets the subject.
	 * @param subject the new subject
	 */
	public final void setSubject(final PdrId subject)
	{
		this._subject = subject;
	}

	/**
	 * Similar relations.
	 * @param rStm the r stm
	 * @param a1 the a1
	 * @param a2 the a2
	 * @param p1 the p1
	 * @param p2 the p2
	 * @return true, if successful
	 */
	public final boolean similarRelations(final RelationStm rStm, final PdrId a1, final PdrId a2, final PdrId p1,
			final PdrId p2)
	{
		boolean similar = false;
		if (this._subject != null && rStm._subject != null)
		{
			if (this._subject.equals(rStm._subject))
			{
				if (this.getRelations() != null && rStm.getRelations() != null)
				{
					if (this.getRelations().size() != rStm.getRelations().size())
					{
						return false;
					}
					for (int i = 0; i < this.getRelations().size(); i++)
					{
						if (this.getRelations().get(i).similarRelations(rStm.getRelations().get(i), a1, a2))
						{
							similar = true;
						}
						if (this.getRelations().get(i).similarRelations(rStm.getRelations().get(i), p1, p2))
						{
							similar = true;
						}
					}
				}
				else if ((this.getRelations() != null && rStm.getRelations() == null)
						|| (this.getRelations() == null && rStm.getRelations() != null))
				{
					return false;
				}
			}
			else if (this._subject.equals(a1) && rStm._subject.equals(a2))
			{
				if (this.getRelations() != null && rStm.getRelations() != null)
				{
					if (this.getRelations().size() != rStm.getRelations().size())
					{
						return false;
					}
					for (int i = 0; i < this.getRelations().size(); i++)
					{
						if (this.getRelations().get(i).similarRelations(rStm.getRelations().get(i), p1, p2))
						{
							similar = true;
						}
					}
				}
				else if ((this.getRelations() != null && rStm.getRelations() == null)
						|| (this.getRelations() == null && rStm.getRelations() != null))
				{
					return false;
				}
			}
			else if (this._subject.equals(p1) && rStm._subject.equals(p2))
			{
				if (this.getRelations() != null && rStm.getRelations() != null)
				{
					if (this.getRelations().size() != rStm.getRelations().size())
					{
						return false;
					}
					for (int i = 0; i < this.getRelations().size(); i++)
					{
						if (this.getRelations().get(i).similarRelations(rStm.getRelations().get(i), a1, a2))
						{
							similar = true;
						}
					}
				}
				else if ((this.getRelations() != null && rStm.getRelations() == null)
						|| (this.getRelations() == null && rStm.getRelations() != null))
				{
					return false;
				}
			}

		}

		return similar;
	}
}
