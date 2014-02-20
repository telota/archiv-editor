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
 * The Class Identifier.
 * @author Christoph Plutte
 */
public class Identifier implements Cloneable
{

	/**
	 * enhält den identifier z.B. PND oder LCCN Nummer requiered
	 */
	private String _identifier;

	/**
	 * provider des identifiers z.b. PND, LCCN requiered
	 */
	private String _provider;

	/**
	 * Authoritaet bzw. user, der diesen identifier verknuepft hat. requiered
	 */
	private PdrId _authority;

	/**
	 * zuverlässigkeit der Identität requiered.
	 */
	private String _quality;

	/**
	 * Instantiates a new identifier.
	 */
	public Identifier()
	{

	}

	/**
	 * @return cloned identifier
	 * @see java.lang.Object#clone()
	 */
	@Override
	public final Identifier clone()
	{
		try
		{
			Identifier clone = (Identifier) super.clone();
			if (this._authority != null)
			{
				clone._authority = this._authority.clone();
			}
			if (this._identifier != null)
			{
				clone._identifier = new String(this._identifier);
			}
			if (this._provider != null)
			{
				clone._provider = new String(this._provider);
			}
			if (this._quality != null)
			{
				clone._quality = new String(this._quality);
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
	 * Gets the identifier.
	 * @return the identifier
	 */
	public final String getIdentifier()
	{
		return _identifier;
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
	 * Gets the quality.
	 * @return the quality
	 */
	public final String getQuality()
	{
		return _quality;
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
		if (_identifier != null && _identifier.trim().length() > 0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * Checks if is valid quality.
	 * @return true, if is valid quality
	 */
	public final boolean isValidQuality()
	{
		if (_quality != null && _quality.trim().length() > 0)
		{
			return true;
		}
		else
		{
			return false;
		}
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
	 * Sets the identifier.
	 * @param identifier the new identifier
	 */
	public final void setIdentifier(final String identifier)
	{
		this._identifier = identifier;
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
	 * Sets the quality.
	 * @param quality the new quality
	 */
	public final void setQuality(final String quality)
	{
		this._quality = quality;
	}
}
