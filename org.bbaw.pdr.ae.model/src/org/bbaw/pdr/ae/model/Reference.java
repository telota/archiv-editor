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
 * The Class Reference.
 * @author Christoph Plutte
 */
public class Reference implements Cloneable
{

	/** The source id. */
	private PdrId _sourceId;

	/** The internal. */
	private String _internal;

	/** The quality. */
	private String _quality;

	/** The authority. */
	private PdrId _authority;

	/**
	 * Instantiates a new reference.
	 * @param string
	 */
	public Reference(String quality)
	{
		this._quality = quality;

	}

	/**
	 * Instantiates a new reference.
	 */
	public Reference()
	{

	}

	/**
	 * @return cloned reference
	 * @see java.lang.Object#clone()
	 */
	@Override
	public final Reference clone()
	{
		try
		{
			Reference clone = (Reference) super.clone();
			if (this._authority != null)
			{
				clone._authority = this._authority.clone();
			}
			if (this._internal != null)
			{
				clone._internal = new String(this._internal);
			}
			if (this._quality != null)
			{
				clone._quality = new String(this._quality);
			}
			if (this._sourceId != null)
			{
				clone._sourceId = this._sourceId.clone();
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
	public final boolean equals(final Reference r)
	{
		if (this._sourceId != null && r._sourceId != null)
		{
			if (!this._sourceId.equals(r._sourceId))
			{
				return false;
			}
		}
		else if ((this._sourceId != null && r._sourceId == null) || (this._sourceId == null && r._sourceId != null))
		{
			return false;
		}

		if (this._internal != null && r._internal != null)
		{
			if (!this._internal.equals(r._internal))
			{
				return false;
			}
		}
		else if ((this._internal != null && r._internal == null) || (this._internal == null && r._internal != null))
		{
			return false;
		}
		if (this._quality != null && r._quality != null)
		{
			if (!this._quality.equals(r._quality))
			{
				return false;
			}
		}
		else if ((this._quality != null && r._quality == null) || (this._quality == null && r._quality != null))
		{
			return false;
		}
		if (this._authority != null && r._authority != null)
		{
			if (!this._authority.equals(r._authority))
			{
				return false;
			}
		}
		else if ((this._authority != null && r._authority == null) || (this._authority == null && r._authority != null))
		{
			return false;
		}

		return true;

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
	 * Gets the internal.
	 * @return the internal
	 */
	public final String getInternal()
	{
		return _internal;
	}

	/**
	 * Gets the quality.
	 * @return the quality
	 */
	public final String getQuality()
	{
		return _quality;
	}

	/**
	 * Gets the source id.
	 * @return the source id
	 */
	public final PdrId getSourceId()
	{
		return _sourceId;
	}

	/**
	 * Checks if is valid.
	 * @return true, if is valid
	 */
	public final boolean isValid()
	{
		if (isValidQuality() && isValidId())
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * Checks if is valid id.
	 * @return true, if is valid id
	 */
	public final boolean isValidId()
	{
		if (_sourceId != null && _sourceId.isValid() && _sourceId.getType().equals("pdrRo"))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	// equals - meth. zum Vergleich zweier References

	/**
	 * Checks if is valid quality.
	 * @return true, if is valid quality
	 */
	public final boolean isValidQuality()
	{
		return (_quality != null);
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
	 * Sets the internal.
	 * @param internal the new internal
	 */
	public final void setInternal(final String internal)
	{
		this._internal = internal;
	}

	/**
	 * Sets the quality.
	 * @param quality the new quality
	 */
	public final void setQuality(final String quality)
	{
		this._quality = quality;
	}

	/**
	 * Sets the source id.
	 * @param sourceId the new source id
	 */
	public final void setSourceId(final PdrId sourceId)
	{
		this._sourceId = sourceId;

		// this.sourceId = sourceId;
	}
}
