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
 * The Class TitleInfo.
 * @author Christoph Plutte
 */
public class TitleInfo implements Cloneable
{

	/** The title. */
	private String _title;

	/** The sub title. */
	private String _subTitle;

	/** The part name. */
	private String _partName;

	/** The part number. */
	private String _partNumber;

	/**
	 * @return clone title info
	 * @see java.lang.Object#clone()
	 */
	@Override
	public final TitleInfo clone()
	{
		try
		{
			TitleInfo clone = (TitleInfo) super.clone();
			if (this._partName != null)
			{
				clone._partName = new String(this._partName);
			}
			if (this._partNumber != null)
			{
				clone._partNumber = new String(this._partNumber);
			}
			if (this._subTitle != null)
			{
				clone._subTitle = new String(this._subTitle);
			}
			if (this._title != null)
			{
				clone._title = new String(this._title);
			}
			return clone;
		}
		catch (CloneNotSupportedException e)
		{
			throw new InternalError();
		}
	}

	/**
	 * Gets the part name.
	 * @return the part name
	 */
	public final String getPartName()
	{
		return _partName;
	}

	/**
	 * Gets the part number.
	 * @return the part number
	 */
	public final String getPartNumber()
	{
		return _partNumber;
	}

	/**
	 * Gets the sub title.
	 * @return the sub title
	 */
	public final String getSubTitle()
	{
		return _subTitle;
	}

	/**
	 * Gets the title.
	 * @return the title
	 */
	public final String getTitle()
	{
		return _title;
	}

	/**
	 * Checks if is valid.
	 * @return true, if is valid
	 */
	public final boolean isValid()
	{
		if (getTitle() != null && getTitle().trim().length() > 0)
		{
			return true;
		}
		return false;
	}

	/**
	 * Sets the part name.
	 * @param partName the new part name
	 */
	public final void setPartName(final String partName)
	{
		this._partName = partName;
	}

	/**
	 * Sets the part number.
	 * @param partNumber the new part number
	 */
	public final void setPartNumber(final String partNumber)
	{
		this._partNumber = partNumber;
	}

	/**
	 * Sets the sub title.
	 * @param subTitle the new sub title
	 */
	public final void setSubTitle(final String subTitle)
	{
		this._subTitle = subTitle;
	}

	/**
	 * Sets the title.
	 * @param title the new title
	 */
	public final void setTitle(final String title)
	{
		this._title = title;
	}
}
