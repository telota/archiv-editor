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
package org.bbaw.pdr.ae.metamodel;

import java.util.Date;

/**
 * The Class Revision.
 * @author Christoph Plutte
 */
public class Revision implements Cloneable
{

	/** The revision number. */
	private int _ref;

	/** The time stamp. */
	private Date _timeStamp;

	/** The authority. */
	private PdrId _authority;

	/** The revisor. */
	private String _revisor;

	/**
	 * Instantiates a new revision.
	 */
	public Revision()
	{

	}

	/**
	 * @return
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Revision clone()
	{
		try
		{
			Revision clone = (Revision) super.clone();
			if (this._authority != null)
			{
				clone._authority = new PdrId(this.getAuthority().clone());
			}
			clone._ref = this.getRef();
			if (this._revisor != null)
			{
				clone._revisor = new String(this.getRevisor());
			}
			if (this._timeStamp != null)
			{
				clone._timeStamp = (Date) this._timeStamp.clone();
			}
			return clone;
		}
		catch (CloneNotSupportedException e)
		{
			throw new InternalError();
		}
	}

	/**
	 * Gets the authority.
	 * @return the authority
	 */
	public final PdrId getAuthority()
	{
		return _authority;
	}

	/**
	 * Gets the ref.
	 * @return the ref
	 */
	public final int getRef()
	{
		return _ref;
	}

	/**
	 * Gets the revisor.
	 * @return the revisor
	 */
	public final String getRevisor()
	{
		return _revisor;
	}

	/**
	 * Gets the time stamp.
	 * @return the time stamp
	 */
	public final Date getTimeStamp()
	{
		return _timeStamp;
	}

	/**
	 * Sets the authority.
	 * @param authority the new authority
	 */
	public final void setAuthority(final PdrId authority)
	{
		this._authority = authority;
	}

	/**
	 * Sets the ref.
	 * @param ref the new ref
	 */
	public final void setRef(final int ref)
	{
		this._ref = ref;
	}

	/**
	 * Sets the revisor.
	 * @param revisor the new revisor
	 */
	public final void setRevisor(final String revisor)
	{
		this._revisor = revisor;
	}

	/**
	 * Sets the time stamp.
	 * @param timeStamp the new time stamp
	 */
	public final void setTimeStamp(final Date timeStamp)
	{
		this._timeStamp = timeStamp;
	}
}
