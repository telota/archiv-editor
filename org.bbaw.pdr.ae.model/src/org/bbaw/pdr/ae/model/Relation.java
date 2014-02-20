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

import org.bbaw.pdr.ae.metamodel.PdrId;

/**
 * The Class Relation.
 * @author Christoph Plutte
 */
public class Relation implements Cloneable
{

	/** The object. */
	private PdrId _object;

	/** The r class. */
	private String _rClass;

	/** The context. */
	private String _context;

	/** The provider. */
	private String _provider;

	/** The relation. */
	private String _relation;

	/**
	 * @return cloned relation
	 * @see java.lang.Object#clone()
	 */
	@Override
	public final Relation clone()
	{
		try
		{
			Relation clone = (Relation) super.clone();
			if (this._object != null)
			{
				clone._object = this._object.clone();
			}
			if (this._context != null)
			{
				clone._context = new String(this._context);
			}
			if (this._provider != null)
			{
				clone._provider = new String(this._provider);
			}
			if (this._rClass != null)
			{
				clone._rClass = new String(this._rClass);
			}
			if (this._relation != null)
			{
				clone._relation = new String(this._relation);
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
	 * @param r the r
	 * @return true, if successful
	 */
	public final boolean equals(final Relation r)
	{
		if (this._object != null && r._object != null)
		{
			if (!this._object.equals(r._object))
			{
				return false;
			}
		}
		else if ((this._object == null && r._object != null) || (this._object != null && r._object == null))
		{
			return false;
		}

		if (!equalsClassification(r))
		{
			return false;
		}
		return true;

	}

	/**
	 * Equals classification.
	 * @param r the r
	 * @return true, if successful
	 */
	private boolean equalsClassification(final Relation r)
	{
		if (this._rClass != null && r._rClass != null)
		{
			if (!this._rClass.equals(r._rClass))
			{
				return false;
			}
		}
		else if ((this._rClass == null && r._rClass != null) || (this._rClass != null && r._rClass == null))
		{
			return false;
		}
		if (this._context != null && r._context != null)
		{
			if (!this._context.equals(r._context))
			{
				return false;
			}
		}
		else if ((this._context == null && r._context != null) || (this._context != null && r._context == null))
		{
			return false;
		}
		if (this._provider != null && r._provider != null)
		{
			if (!this._provider.equals(r._provider))
			{
				return false;
			}
		}
		else if ((this._provider == null && r._provider != null) || (this._provider != null && r._provider == null))
		{
			return false;
		}
		if (this._relation != null && r._relation != null)
		{
			if (!this._relation.equals(r._relation))
			{
				return false;
			}
		}
		else if ((this._relation == null && r._relation != null) || (this._relation != null && r._relation == null))
		{
			return false;
		}
		return true;
	}

	/**
	 * Gets the context.
	 * @return the context
	 */
	public final String getContext()
	{
		return _context;
	}

	/**
	 * Gets the object.
	 * @return the object
	 */
	public final PdrId getObject()
	{
		return _object;
	}

	/**
	 * Gets the provider.
	 * @return the provider
	 */
	public final String getProvider()
	{
		return _provider;
	}

	/**
	 * Gets the r class.
	 * @return the r class
	 */
	public final String getRClass()
	{
		return _rClass;
	}

	/**
	 * Gets the relation.
	 * @return the relation
	 */
	public final String getRelation()
	{
		return _relation;
	}

	/**
	 * Checks if is valid.
	 * @return true, if is valid
	 */
	public final boolean isValid()
	{
		if (_object != null && _object.isValid())
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * Sets the context.
	 * @param context the new context
	 */
	public final void setContext(final String context)
	{
		this._context = context;
	}

	/**
	 * Sets the object.
	 * @param object the new object
	 */
	public final void setObject(final PdrId object)
	{
		this._object = object;
	}

	/**
	 * Sets the provider.
	 * @param provider the new provider
	 */
	public final void setProvider(final String provider)
	{
		this._provider = provider;
	}

	/**
	 * Sets the r class.
	 * @param rClass the new r class
	 */
	public final void setRClass(final String rClass)
	{
		this._rClass = rClass;
	}

	/**
	 * Sets the relation.
	 * @param relation the new relation
	 */
	public final void setRelation(final String relation)
	{
		this._relation = relation;
	}

	/**
	 * Similar relations.
	 * @param r the r
	 * @param a1 the a1
	 * @param a2 the a2
	 * @return true, if successful
	 */
	public final boolean similarRelations(final Relation r, final PdrId a1, final PdrId a2)
	{
		if (!equalsClassification(r))
		{
			return false;
		}
		if (this._object != null && r._object != null)
		{
			if (!(this._object.equals(a1) && r._object.equals(a2)))
			{
				return false;
			}
		}
		else if ((this._object == null && r._object != null) || (this._object != null && r._object == null))
		{
			return false;
		}
		return true;
	}
}
