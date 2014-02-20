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
 * _id object of all pdr _ids. contains _type - either pdrAo for aspect pdrPo -
 * person. pdrRo - for reference objects. pdrUo - for user objects.
 * @author cplutte.
 */
public class PdrId implements Comparable<PdrId>, Cloneable
{

	/**
	 * _type of pdr _id.
	 */
	private String _type;
	/** _instance of pdr repository. */
	private int _instance;
	/** _project id. */
	private int _projectID;
	/** _id of object within given repository _instance. */
	private int _id;

	/** standard constructor. */
	public PdrId()
	{
	}

	/**
	 * Instantiates a new pdr id.
	 * @param id the id
	 */
	public PdrId(final PdrId id)
	{
		this._type = id.getType();
		this._instance = id.getInstance();
		this._projectID = id.getProjectID();
		this._id = id.getId();
	}

	/**
	 * contructor by _id string.
	 * @param id id as string.
	 */
	public PdrId(final String id)
	{
		if (id != null)
		{
			if (id.length() == 23)
			{
				this._type = id.substring(0, 5);
				this._instance = Integer.valueOf(id.substring(6, 9)).intValue();
				this._projectID = Integer.valueOf(id.substring(10, 13)).intValue();
				this._id = Integer.valueOf(id.substring(14, 23)).intValue();
			}
			else if (id.length() == 22)
			{
				this._type = id.substring(0, 5);
				this._instance = 2; // Integer.valueOf(_id.substring(7,
									// 9)).intValue();
				this._projectID = 1;
				this._id = Integer.valueOf(id.substring(14, 22)).intValue();
			}
			else if (id.length() == 24)
			{
				this._type = id.substring(0, 5);
				this._instance = 2; // Integer.valueOf(_id.substring(7,
									// 9)).intValue();
				this._projectID = 1;
				this._id = Integer.valueOf(id.substring(14, 24)).intValue();
			}
			else
			{
				this._type = "error";
				this._instance = 0;
				this._projectID = 0;
				this._id = 0;
			}
		}
		else
		{
			this._type = "error";
			this._instance = 0;
			this._projectID = 0;
			this._id = 0;
		}
	}

	/**
	 * Instantiates a new pdr id.
	 * @param type the type
	 * @param instance the instance
	 * @param projectId the project id
	 * @param id the id
	 */
	public PdrId(final String type, final int instance, final int projectId, final int id)
	{
		this._type = type;
		this._instance = instance;
		this._projectID = projectId;
		this._id = id;
	}

	@Override
	public final PdrId clone()
	{
		try
		{
			PdrId clone = (PdrId) super.clone();
			clone._id = this._id;
			clone._projectID = this._projectID;
			clone._instance = this._instance;
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

	@Override
	public final int compareTo(final PdrId id)
	{
		if (this._instance == id._instance)
		{
			if (this._projectID == id._projectID)
			{
				return this._id - id._id;
			}
			else
			{
				return this._projectID - id._projectID;
			}
		}
		else
		{
			return this._instance - id._instance;
		}
	}

	@Override
	public final boolean equals(final Object o)
	{
		if (o instanceof PdrId)
		{
			PdrId secId = (PdrId) o;
			if (this._type.equals(secId._type) && this._instance == secId._instance
					&& this._projectID == secId._projectID && this._id == secId._id)
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Equals or smaler than.
	 * @param secId the sec id
	 * @return true, if successful
	 */
	public final boolean equalsOrSmalerThan(final PdrId secId)
	{
		if (this._type.equals(secId._type) && this._instance == secId._instance && this._projectID == secId._projectID
				&& this._id <= secId._id)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * getter for _id.
	 * @return _id.
	 */
	public final int getId()
	{
		return _id;
	}

	/**
	 * getter for _instance.
	 * @return _instance.
	 */
	public final int getInstance()
	{
		return _instance;
	}

	/**
	 * Gets the project id.
	 * @return the project id
	 */
	public final int getProjectID()
	{
		return _projectID;
	}

	/**
	 * getter for _type.
	 * @return _type.
	 */
	public final String getType()
	{
		return _type;
	}

	@Override
	public final int hashCode()
	{
		return _id;
	}

	/**
	 * Checks if is valid.
	 * @return true, if is valid
	 */
	public final boolean isValid()
	{
		if (_type.startsWith("pdr") && _instance >= 0 && _projectID >= 0 && _id >= 0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * setter of _id.
	 * @param id _id of ob
	 */
	public final void setId(final int id)
	{
		this._id = id;
	}

	/**
	 * setter for _instance.
	 * @param instance _instance of _id that is repository.
	 */
	public final void setInstance(final int instance)
	{
		this._instance = instance;
	}

	/**
	 * Sets the project id.
	 * @param projectID the new project id
	 */
	public final void setProjectID(final int projectID)
	{
		this._projectID = projectID;
	}

	/**
	 * setter for _type.
	 * @param type _type of _id.
	 */
	public final void setType(final String type)
	{
		this._type = type;
	}

	/**
	 * returns pdrId-Object as string.
	 * @return _id as String.
	 */
	@Override
	public final String toString()
	{
		String pdrId = _type + "." + String.format("%03d", _instance) + "." + String.format("%03d", _projectID) + "."
				+ String.format("%09d", _id);
		return pdrId;
	}

	/**
	 * returns given _id as string.
	 * @param type _type of _id.
	 * @param instance _instance of _id that is repository.
	 * @param projectID project ID
	 * @param id _id of object.
	 * @return id as string.
	 */
	public final String toString(final String type, final int instance, final int projectID, final int id)
	{

		String pdrId = type + ":" + String.format("%03d", instance) + "." + String.format("%03d", projectID) + "."
				+ String.format("%09d", id);
		return pdrId;
	}
}
