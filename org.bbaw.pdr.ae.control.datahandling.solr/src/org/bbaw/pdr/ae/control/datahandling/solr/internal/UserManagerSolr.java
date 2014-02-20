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
package org.bbaw.pdr.ae.control.datahandling.solr.internal;

import java.util.HashMap;
import java.util.Vector;

import javax.xml.stream.XMLStreamException;

import org.bbaw.pdr.ae.control.interfaces.IUserManager;
import org.bbaw.pdr.ae.model.User;

public class UserManagerSolr implements IUserManager {

	@Override
	public User getUsersByUserName(String userName) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User getUserById(String id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setUsers(Vector<User> users) {
		// TODO Auto-generated method stub

	}

	@Override
	public void saveUser(User user) throws XMLStreamException, Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean verifyOrCreateUsers() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public HashMap<String, User> getMapOfSavedUsers() {
		// TODO Auto-generated method stub
		return null;
	}

}
