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
package org.bbaw.pdr.ae.control.core;

import java.util.HashMap;
import java.util.Map;

import org.bbaw.pdr.ae.common.AEPluginIDs;
import org.eclipse.ui.AbstractSourceProvider;
import org.eclipse.ui.ISources;

/**
 * The Class UserRoleSourceProvider.
 * @author Christoph Plutte
 */
public class UserRoleSourceProvider extends AbstractSourceProvider
{

	public final static String TRUE = "TRUE";
	public final static String FALSE = "FALSE";
	/** The user role. */
	private String _userRole;

	/** The user may delete. */
	private boolean _userMayDelete = false;


	/**
	 * @see org.eclipse.ui.ISourceProvider#dispose()
	 */
	@Override
	public void dispose()
	{
	}

	/**
	 * @return return current states of source properties.
	 * @see org.eclipse.ui.ISourceProvider#getCurrentState()
	 */
	@SuppressWarnings(
	{"unchecked", "rawtypes"})
	@Override
	public final Map getCurrentState()
	{
		Map currentState = new HashMap(2);
		currentState.put(AEPluginIDs.SOURCE_PARAMETER_USER_ROLE, _userRole);
		String value = _userMayDelete ? TRUE : FALSE;
		currentState.put(AEPluginIDs.SOURCE_PARAMETER_USER_MAY_DELETE, value);
		return currentState;
	}

	/**
	 * @return array of strings with all names of provided source properties
	 * @see org.eclipse.ui.ISourceProvider#getProvidedSourceNames()
	 */
	@Override
	public final String[] getProvidedSourceNames()
	{
		return new String[]
		{AEPluginIDs.SOURCE_PARAMETER_USER_ROLE, AEPluginIDs.SOURCE_PARAMETER_USER_MAY_DELETE};
	}



	/**
	 * Sets the user may delete.
	 * @param userMayDelete the new user may delete
	 */
	public final void setUserMayDelete(final boolean userMayDelete)
	{
		// if (this._userMayDelete == userMayDelete)
		// {
		// return;
		// }
		this._userMayDelete = userMayDelete;
		String value = userMayDelete ? TRUE : FALSE;
		fireSourceChanged(ISources.WORKBENCH, AEPluginIDs.SOURCE_PARAMETER_USER_MAY_DELETE,
 value);
	}

	/**
	 * Sets the user role.
	 * @param userRole the new user role
	 */
	public final void setUserRole(final String userRole)
	{
		if (this._userRole != null && this._userRole.equals(userRole))
		{
			return;
		}
		this._userRole = userRole;
		fireSourceChanged(ISources.WORKBENCH, AEPluginIDs.SOURCE_PARAMETER_USER_ROLE, userRole);
	}
}