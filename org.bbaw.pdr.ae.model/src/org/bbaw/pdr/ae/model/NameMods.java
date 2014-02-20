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

import java.util.Collections;
import java.util.Vector;

/**
 * The Class NameMods.
 * @author Christoph Plutte
 */
public class NameMods implements Comparable<NameMods>, Cloneable
{

	/** The type. */
	private String _type;

	/** The name parts. */
	private Vector<NamePart> _nameParts;

	/** The role mods. */
	private RoleMods _roleMods = new RoleMods();

	/** The affiliation. */
	private String _affiliation;

	/** The description. */
	private String _description;

	/**
	 * Instantiates a new name mods.
	 */
	public NameMods()
	{
	}

	/**
	 * Instantiates a new name mods.
	 * @param numNames the num names
	 */
	public NameMods(final int numNames)
	{
		_nameParts = new Vector<NamePart>(numNames);
		for (int i = 0; i < numNames / 2; i++)
		{
			_nameParts.add(new NamePart("family"));
			_nameParts.add(new NamePart("given"));
		}
	}

	/**
	 * @return cloned nameMods
	 * @see java.lang.Object#clone()
	 */
	@Override
	public final NameMods clone()
	{
		try
		{
			NameMods clone = (NameMods) super.clone();
			if (this._affiliation != null)
			{
				clone._affiliation = new String(this._affiliation);
			}
			if (this._description != null)
			{
				clone._description = new String(this._description);
			}
			if (this._nameParts != null)
			{
				clone._nameParts = new Vector<NamePart>(this._nameParts.size());
				for (int i = 0; i < this._nameParts.size(); i++)
				{
					clone._nameParts.add(this._nameParts.get(i).clone());
				}
			}
			if (this._roleMods != null)
			{
				clone._roleMods = this._roleMods.clone();
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
	 * @param r name mods
	 * @return <0 if this nameMods is in alphabetical order before the second
	 *         one >0 if otherwise.
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public final int compareTo(final NameMods r)
	{
		if (this.getRoleMods() != null && this.getRoleMods().getType() != null && r.getRoleMods() != null
				&& r.getRoleMods().getType() != null)
		{
			if (this.getRoleMods().getType().equals("aut"))
			{
				if (r.getRoleMods().getType().equals("aut"))
				{
					return 0;
				}
				else
				{
					return -1;
				}
			}
			else if (this.getRoleMods().getType().equals("cre"))
			{
				if (r.getRoleMods().getType().equals("aut"))
				{
					return 1;
				}
				if (r.getRoleMods().getType().equals("cre"))
				{
					return 0;
				}
				else
				{
					return -1;
				}
			}
			else if (this.getRoleMods().getType().equals("edt"))
			{
				if (r.getRoleMods().getType().equals("aut") || r.getRoleMods().getType().equals("cre"))
				{
					return 1;
				}
				if (r.getRoleMods().getType().equals("edt"))
				{
					return 0;
				}
				else
				{
					return -1;
				}
			}
			else
			{
				return -1;
			}
		}
		else
		{
			return 0;
		}
	}

	/**
	 * Gets the affiliation.
	 * @return the affiliation
	 */
	public final String getAffiliation()
	{
		return _affiliation;
	}

	/**
	 * Gets the description.
	 * @return the description
	 */
	public final String getDescription()
	{
		return _description;
	}

	/**
	 * Gets the forename.
	 * @return the forename
	 */
	public final String getForename()
	{
		for (NamePart n : _nameParts)
		{
			if (n.getType() != null)
			{
				if (n.getType().equals("given"))
				{
					if (n.getNamePart() != null && n.getNamePart().trim().length() > 0)
					{
						return n.getNamePart().trim();
					}
				}
			}
		}
		return null;
	}

	/**
	 * Gets the full name.
	 * @return the full name
	 */
	public final String getFullName()
	{
		String name = "";
		String forName = "";
		for (NamePart n : _nameParts)
		{
			if (n.getType() != null)
			{
				if (n.getType().equals("family"))
				{
					if (n.getNamePart() != null && n.getNamePart().trim().length() > 0)
					{
						name = n.getNamePart().trim();
					}
				}
				else if (n.getType().equals("given"))
				{
					if (n.getNamePart() != null && n.getNamePart().trim().length() > 0)
					{
						forName += n.getNamePart().trim() + " ";
					}
				}
			}
			else
			{
				if (n.getNamePart() != null && n.getNamePart().trim().length() > 0)
				{
					name += n.getNamePart();
				}
			}

		}
		if (forName.trim().length() > 0)
		{
			name = name + ", " + forName.trim();
		}
		return name.trim();
	}

	/**
	 * @return the nameParts
	 */
	public final Vector<NamePart> getNameParts()
	{
		Collections.sort(_nameParts);
		return _nameParts;
	}

	/**
	 * Gets the role mods.
	 * @return the role mods
	 */
	public final RoleMods getRoleMods()
	{
		return _roleMods;
	}

	/**
	 * Gets the surname.
	 * @return the surname
	 */
	public final String getSurname()
	{
		for (NamePart n : _nameParts)
		{
			if (n.getType() != null)
			{
				if (n.getType().equals("family"))
				{
					if (n.getNamePart() != null && n.getNamePart().trim().length() > 0)
					{
						return n.getNamePart().trim();
					}
				}
			}
		}
		return null;
	}

	/**
	 * Gets the type.
	 * @return the type
	 */
	public final String getType()
	{
		return _type;
	}

	/**
	 * Sets the affiliation.
	 * @param affiliation the new affiliation
	 */
	public final void setAffiliation(final String affiliation)
	{
		this._affiliation = affiliation;
	}

	/**
	 * Sets the description.
	 * @param description the new description
	 */
	public final void setDescription(final String description)
	{
		this._description = description;
	}

	/**
	 * @param nameParts the nameParts to set
	 */
	public final void setNameParts(final Vector<NamePart> nameParts)
	{
		this._nameParts = nameParts;
	}

	/**
	 * Sets the role mods.
	 * @param roleMods the new role mods
	 */
	public final void setRoleMods(final RoleMods roleMods)
	{
		this._roleMods = roleMods;
	}

	/**
	 * Sets the type.
	 * @param type the new type
	 */
	public final void setType(final String type)
	{
		this._type = type;
	}
}
