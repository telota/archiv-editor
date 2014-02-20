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

/**
 * The Class Genre.
 * @author Christoph Plutte
 */
public class Genre implements Cloneable
{

	/** The genre. */
	private String _genre;

	/** The authority. */
	private String _authority;

	/**
	 * @return cloned genre
	 * @see java.lang.Object#clone()
	 */
	@Override
	public final Genre clone()
	{
		try
		{
			Genre clone = (Genre) super.clone();
			if (this._authority != null)
			{
				clone._authority = new String(this._authority);
			}
			if (this._genre != null)
			{
				clone._genre = new String(this._genre);
			}
			return clone;
		}
		catch (CloneNotSupportedException e)
		{
			throw new InternalError();
		}
	}

	/**
	 * @return the authority
	 */
	public final String getAuthority()
	{
		return _authority;
	}

	/**
	 * @return the genre
	 */
	public final String getGenre()
	{
		return _genre;
	}

	/**
	 * @param authority the authority to set
	 */
	public final void setAuthority(final String authority)
	{
		this._authority = authority;
	}

	/**
	 * @param genre the genre to set
	 */
	public final void setGenre(final String genre)
	{
		this._genre = genre;
	}

}
