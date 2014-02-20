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

import org.bbaw.pdr.ae.control.facade.Facade;
import org.bbaw.pdr.ae.metamodel.Revision;
import org.bbaw.pdr.ae.model.PdrObject;
import org.bbaw.pdr.ae.model.User;

/**
 * The Class UserRichtsChecker.
 * @author Christoph Plutte
 */
public class UserRichtsChecker
{
	// pseudo policy
	/** The ADMI n_ ma y_ creat e_ ne w_ config. */
	private static final boolean ADMIN_MAY_CREATE_NEW_CONFIG = true;

	/** The USE r_ ma y_ creat e_ ne w_ config. */
	private static final boolean USER_MAY_CREATE_NEW_CONFIG = false;

	/** The USE r_ ma y_ modif y_ mandator y_ config. */
	private static final boolean USER_MAY_MODIFY_MANDATORY_CONFIG = false;

	/** The ADMI n_ ma y_ modif y_ mandator y_ config. */
	private static final boolean ADMIN_MAY_MODIFY_MANDATORY_CONFIG = false;

	/** The PDRADMI n_ ma y_ modif y_ mandator y_ config. */
	private static final boolean PDRADMIN_MAY_MODIFY_MANDATORY_CONFIG = true;

	// private boolean ADMIN_MAY_MERGE_OWN_OBJECTS = true;
	/** The USE r_ ma y_ merg e_ ow n_ objects. */
	private static final boolean USER_MAY_MERGE_OWN_OBJECTS = true;

	/** The ADMI n_ ma y_ merg e_ other s_ objects. */
	private static final boolean ADMIN_MAY_MERGE_OTHERS_OBJECTS = true;
	// private boolean USER_MAY_MERGE_OTHERS_OBJECTS = false;

	/** The ADMI n_ ma y_ edi t_ config. */
	private static final boolean ADMIN_MAY_EDIT_CONFIG = true;

	/** The USE r_ ma y_ edi t_ config. */
	private static final boolean USER_MAY_EDIT_CONFIG = false;

	/** The ADMI n_ ma y_ se t_ ignored. */
	private static final boolean ADMIN_MAY_SET_IGNORED = true;

	/** The USE r_ ma y_ se t_ ignored. */
	private static final boolean USER_MAY_SET_IGNORED = true;

	/** The user is admin. */
	private boolean _userIsAdmin;

	/** The user is pdr admin. */
	private boolean _userIsPDRAdmin;

	/** The user. */
	private User _user;

	/** The _facade. */
	private Facade _facade = Facade.getInstanz();

	/**
	 * Instantiates a new user richts checker.
	 */
	public UserRichtsChecker()
	{
	}

	/**
	 * Checks if is user admin.
	 * @return true, if is user admin
	 */
	public final boolean isUserAdmin()
	{
		_user = _facade.getCurrentUser();
		if (_user != null && _user.getAuthentication() != null && _user.getAuthentication().getRoles() != null
				&& _user.getAuthentication().getRoles().contains("admin"))
		{
			_userIsAdmin = true;
			return _userIsAdmin;
		}
		_userIsAdmin = false;
		return _userIsAdmin;

	}

	/**
	 * Checks if is user guest.
	 * @return true, if is user guest
	 */
	public final boolean isUserGuest()
	{
		_user = _facade.getCurrentUser();
		if (_user != null && _user.getAuthentication() != null && _user.getAuthentication().getRoles() != null
				&& (_user.getAuthentication().getRoles().contains("admin")
				|| _user.getAuthentication().getRoles().contains("user")
				|| _user.getAuthentication().getRoles().contains("pdrAdmin")))
		{
			return false;
		}
		return true;

	}

	/**
	 * Checks if is user pdr admin.
	 * @return true, if is user pdr admin
	 */
	public final boolean isUserPDRAdmin()
	{
		_user = _facade.getCurrentUser();
		if (_user != null && _user.getAuthentication() != null && _user.getAuthentication().getRoles() != null
				&& _user.getAuthentication().getRoles().contains("pdrAdmin"))
		{
			_userIsPDRAdmin = true;
			_userIsAdmin = true;
			return _userIsPDRAdmin;
		}
		_userIsPDRAdmin = false;
		return _userIsPDRAdmin;

	}

	/**
	 * Checks if is user pdr admin.
	 * @param u the u
	 * @return true, if is user pdr admin
	 */
	public final boolean isUserPDRAdmin(final User u)
	{
		if (u != null && u.getAuthentication() != null && u.getAuthentication().getRoles() != null
				&& u.getAuthentication().getRoles().contains("pdrAdmin"))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * May create new config.
	 * @return true, if successful
	 */
	public final boolean mayCreateNewConfig()
	{
		_user = _facade.getCurrentUser();
		if (_user.getAuthentication().getRoles().contains("admin"))
		{
			return ADMIN_MAY_CREATE_NEW_CONFIG;
		}
		else if (_user.getAuthentication().getRoles().contains("user"))
		{
			return USER_MAY_CREATE_NEW_CONFIG;
		}
		return false;
	}

	/**
	 * May delete.
	 * @param object the object
	 * @return true, if successful
	 */
	public final boolean mayDelete(final PdrObject object)
	{
		_user = _facade.getCurrentUser();
		if (object != null && !isUserGuest())
		{
			if (object.getPdrId().getId() < 100000000)
			{
				return false;
			}
			else
			{
				if (_user.getAuthentication().getRoles().contains("pdrAdmin"))
				{
					return true;
				}
				else if (object.getPdrId().getInstance() == _user.getPdrId().getInstance())
				{
					if (_user.getAuthentication().getRoles().contains("admin"))
					{
						return true;
					}
					else if (object.getRecord() != null && object.getRecord().getRevisions() != null)

					{
						for (Revision r : object.getRecord().getRevisions())
						{
							if (r.getAuthority() != null && r.getAuthority().equals(_user.getPdrId()))
							{
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}

	/**
	 * May edit config.
	 * @return true, if successful
	 */
	public final boolean mayEditConfig()
	{
		_user = _facade.getCurrentUser();
		if (_user.getAuthentication().getRoles().contains("admin"))
		{
			return ADMIN_MAY_EDIT_CONFIG;
		}
		else if (_user.getAuthentication().getRoles().contains("user"))
		{
			return USER_MAY_EDIT_CONFIG;
		}
		return false;
	}

	/**
	 * May merge.
	 * @param object the object
	 * @return true, if successful
	 */
	public final boolean mayMerge(final PdrObject object)
	{
		_user = _facade.getCurrentUser();
		if (object != null && !isUserGuest())
		{
			if (_user.getAuthentication().getRoles().contains("pdrAdmin"))
			{
				return true;
			}
			else if (object.getPdrId().getInstance() == _user.getPdrId().getInstance())
			{
				if (_user.getAuthentication().getRoles().contains("admin"))
				{
					return ADMIN_MAY_MERGE_OTHERS_OBJECTS;
				}
				else if (object.getRecord() != null && object.getRecord().getRevisions() != null)

				{
					for (Revision r : object.getRecord().getRevisions())
					{
						if (r.getAuthority() != null && r.getAuthority().equals(_user.getPdrId()))
						{
							return USER_MAY_MERGE_OWN_OBJECTS;
						}
					}
				}
			}

		}
		return false;
	}

	/**
	 * May modify mandatory config.
	 * @return true, if successful
	 */
	public final boolean mayModifyMandatoryConfig()
	{
		_user = _facade.getCurrentUser();
		if (_user.getAuthentication().getRoles().contains("pdrAdmin"))
		{
			return PDRADMIN_MAY_MODIFY_MANDATORY_CONFIG;
		}
		else if (_user.getAuthentication().getRoles().contains("admin"))
		{
			return ADMIN_MAY_MODIFY_MANDATORY_CONFIG;
		}
		else if (_user.getAuthentication().getRoles().contains("user"))
		{
			return USER_MAY_MODIFY_MANDATORY_CONFIG;
		}
		return false;
	}

	/**
	 * May read.
	 * @param object the object
	 * @return true, if successful
	 */
	public final boolean mayRead(final PdrObject object)
	{
		return true;
	}

	/**
	 * May set config ignored.
	 * @return true, if successful
	 */
	public final boolean maySetConfigIgnored()
	{
		_user = _facade.getCurrentUser();
		if (_user.getAuthentication().getRoles().contains("admin"))
		{
			return ADMIN_MAY_SET_IGNORED;
		}
		else if (_user.getAuthentication().getRoles().contains("user"))
		{
			return USER_MAY_SET_IGNORED;
		}
		return false;
	}

	// public boolean test(Object o, String s, Object[] os, Object o2) {
	// System.out.println("test property - return " + userIsAdmin);
	// return userIsAdmin;
	// }
	/**
	 * May write.
	 * @param object the object
	 * @return true, if successful
	 */
	public final boolean mayWrite(final PdrObject object)
	{
		_user = _facade.getCurrentUser();
		if (object != null && !isUserGuest())
		{
			if (_user.getAuthentication().getRoles().contains("pdrAdmin"))
			{
				return true;
			}
			else if (object.getPdrId().getInstance() == _user.getPdrId().getInstance())
			{
				if (_user.getAuthentication().getRoles().contains("admin"))
				{
					return true;
				}
				else if (object.getRecord() != null && object.getRecord().getRevisions() != null)

				{
					for (Revision r : object.getRecord().getRevisions())
					{
						if (r.getAuthority() != null && r.getAuthority().equals(_user.getPdrId()))
						{
							return true;
						}
					}
				}
			}

		}
		return false;
	}
}
