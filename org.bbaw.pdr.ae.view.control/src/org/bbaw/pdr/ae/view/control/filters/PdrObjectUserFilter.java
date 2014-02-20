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
package org.bbaw.pdr.ae.view.control.filters;

import java.util.Vector;

import org.bbaw.pdr.ae.common.interfaces.AEFilter;
import org.bbaw.pdr.ae.metamodel.Revision;
import org.bbaw.pdr.ae.model.PdrObject;

/**
 * The Class PdrObjectUserFilter.
 * @author Christoph Plutte
 */
public class PdrObjectUserFilter implements AEFilter
{

	/** The users. */
	private Vector<String> _users;

	/**
	 * Instantiates a new pdr object user filter.
	 * @param users the users
	 */
	public PdrObjectUserFilter(final Vector<String> users)
	{
		this._users = users;
	}

	/**
	 * Adds the user id to filter.
	 * @param user the user
	 */
	public final void addUserIdToFilter(final String user)
	{
		if (_users == null)
		{
			_users = new Vector<String>();
		}
		if (!_users.contains(user))
		{
			_users.add(user);
		}
	}

	/**
	 * Gets the user ids.
	 * @return the user ids
	 */
	public final Vector<String> getUserIds()
	{
		return _users;
	}

	/**
	 * Removes the user id to filter.
	 * @param user the user
	 */
	public final void removeUserIdToFilter(final String user)
	{
		if (_users != null)
		{
			_users.removeElement(user);
		}
	}

	@Override
	public final boolean select(final Object element)
	{
		if (_users != null)
		{
			if (element instanceof PdrObject)
			{
				PdrObject o = (PdrObject) element;
				if (o.getRecord() != null && o.getRecord().getRevisions() != null
						&& !o.getRecord().getRevisions().isEmpty())
				{
					for (Revision revision : o.getRecord().getRevisions())
					{
						if (revision.getAuthority() != null && _users.contains(revision.getAuthority().toString()))
						{
							return true;
						}
					}
					return false;
				}
			}
			return false;
		}
		else
		{
			return true;
		}
	}

	/**
	 * Sets the user ids.
	 * @param users the new user ids
	 */
	public final void setUserIds(final Vector<String> users)
	{
		this._users = users;
	}
}
