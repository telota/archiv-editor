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

/**
 * The Class PdrMetaObject.
 * @author Christoph Plutte
 */
public class PdrMetaObject implements Cloneable
{

	/** The _display name. */
	protected String _displayName;

	/** The _pdr id. */
	private PdrId _pdrId;

	/** The _record. */
	private Record _record;

	// /** Logger. */
	// private static ILog iLogger =
	// org.bbaw.pdr.ae.view.Activator.getILogger();
	// /** status. */
	// private IStatus log;

	/**
	 * Instantiates a new pdr meta object.
	 * @param pdrId the pdr id
	 */
	public PdrMetaObject(final PdrId pdrId)
	{
		this._pdrId = pdrId;
	}

	/**
	 * Instantiates a new pdr meta object.
	 * @param idString the id string
	 */
	public PdrMetaObject(final String idString)
	{
		_pdrId = new PdrId(idString);
	}

	@Override
	public PdrMetaObject clone()
	{
		try
		{
			PdrMetaObject clone = (PdrMetaObject) super.clone();
			if (this._pdrId != null)
			{
				clone._pdrId = new PdrId(this._pdrId.clone());
			}
			if (this._record != null)
			{
				clone._record = new Record(this.getRecord().clone());
			}
			if (this._displayName != null)
			{
				clone._displayName = null;
			}
			return clone;
		}
		catch (CloneNotSupportedException e)
		{
			throw new InternalError();
		}
	}

	/**
	 * @return the displayName
	 */
	public String getDisplayName()
	{

		if (_displayName == null)
		{
			return _pdrId.toString();
		}
		return _displayName;
	}

	/**
	 * returns the displayname - if the name is longer than the given length it
	 * is cut and "..." are added.
	 * @param length length of string
	 * @return displayname shortened to given length
	 */
	public String getDisplayName(int length)
	{
		String name = _displayName;
		if (name == null || name.trim().length() == 0)
		{
			name = _pdrId.toString();
		}
		if (name.length() > length)
		{
			name = name.substring(0, length - 3) + "...";
		}
		return name;
	}

	/**
	 * @return the displayName
	 */
	public String getDisplayNameWithID()
	{

		if (_displayName == null)
		{
			return _pdrId.toString();
		}
		return _displayName + " (" + _pdrId.toString() + ")";
	}

	/**
	 * Gets the pdr id.
	 * @return the pdr id
	 */
	public final PdrId getPdrId()
	{
		return _pdrId;
	}

	/**
	 * Gets the record.
	 * @return the record
	 */
	public final Record getRecord()
	{
		return _record;
	}

	/**
	 * Sets the display name.
	 * @param displayName the new display name
	 */
	public final void setDisplayName(final String displayName)
	{
		this._displayName = displayName;
	}

	/**
	 * Sets the pdr id.
	 * @param id the new pdr id
	 */
	protected void setPdrId(final PdrId id)
	{
		this._pdrId = id;

	}

	/**
	 * Sets the record.
	 * @param record the new record
	 */
	public final void setRecord(final Record record)
	{
		this._record = record;
	}

}
