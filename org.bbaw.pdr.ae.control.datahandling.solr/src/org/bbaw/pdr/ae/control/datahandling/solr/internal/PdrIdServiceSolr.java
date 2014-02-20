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

import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import org.bbaw.pdr.ae.control.interfaces.IPdrIdService;
import org.bbaw.pdr.ae.metamodel.PdrId;

public class PdrIdServiceSolr implements IPdrIdService {

	@Override
	public void insertIdUpdatedObjects(Vector<String> pdrIds, String type)
			throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public HashMap<String, Integer> loadObjectsUpdateState(String type)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void clearAllUpdateStates() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public Date getUpdateTimeStamp() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setUpdateTimeStamp(Date date) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public Vector<String> getModifiedAspectIds() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vector<String> getModifiedPersonIds() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vector<String> getModifiedReferenceIds() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vector<String> getModifiedUserIds() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void insertIdModifiedObject(Vector<String> modifiedIds, String type)
			throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void insertModifiedConfig(String provider) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void insertNewConfig(String provider) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public Vector<String> getModifiedConfigs() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vector<String> getNewConfigs() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vector<String> getModifiedObjects() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vector<String> getNewUserIds() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void clearUserUpdateStates() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public PdrId getNewId(String type) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void resetIdUnused(PdrId pdrId) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void insertIdNewObject(PdrId pdrId) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void insertIdModifiedObject(PdrId pdrId) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void insertIdUpdatedObject(PdrId pdrId) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void insertIdDeletedObject(PdrId pdrId) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isModifiedOrNewObject(PdrId pdrId) {
		// TODO Auto-generated method stub
		return false;
	}

}
