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
 * The Class AccessCondition.
 * @author Christoph Plutte
 */
public class AccessCondition implements Cloneable
{

	/** The access condition. */
	private String _accessCondition;

	/** The type. */
	private String _type;

	/**
	 * @return cloned accesscondition.
	 * @see java.lang.Object#clone()
	 */
	@Override
	public final AccessCondition clone()
	{
		try
		{
			AccessCondition clone = (AccessCondition) super.clone();
			if (this._accessCondition != null)
			{
				clone._accessCondition = new String(this._accessCondition);
			}
			if (this._type != null)
			{
				clone._type = new String(this._type);
			}
			return clone;
		}
		catch (CloneNotSupportedException e)
		{
			throw new InternalError();
		}
	}

	/**
	 * @return the accessCondition
	 */
	public final String getAccessCondition()
	{
		return _accessCondition;
	}

	/**
	 * @return the type
	 */
	public final String getType()
	{
		return _type;
	}

	/**
	 * @param accessCondition the accessCondition to set
	 */
	public final void setAccessCondition(final String accessCondition)
	{
		this._accessCondition = accessCondition;
	}

	/**
	 * @param type the type to set
	 */
	public final void setType(final String type)
	{
		this._type = type;
	}
}
