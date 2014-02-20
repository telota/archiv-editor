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
package org.bbaw.pdr.ae.control.interfaces;

import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import org.bbaw.pdr.ae.metamodel.PdrId;

/**
 * The Interface IPdrIdService. Its implementation handle the administration of
 * new pdrIds and of update states and last update time stamp.
 * @author Christoph Plutte
 */
public interface IPdrIdService
{

	/**
	 * Clear all update states.
	 * @throws Exception the exception
	 */
	void clearAllUpdateStates() throws Exception;

	/**
	 * Clear user update states.
	 * @throws Exception the exception
	 */
	void clearUserUpdateStates() throws Exception;

	/**
	 * Gets the modified aspect ids.
	 * @return the modified aspect ids
	 * @throws Exception the exception
	 */
	Vector<String> getModifiedAspectIds() throws Exception;

	/**
	 * Gets the modified configs.
	 * @return the modified configs
	 * @throws Exception the exception
	 */
	Vector<String> getModifiedConfigs() throws Exception;

	/**
	 * Gets the modified objects.
	 * @return the modified objects
	 * @throws Exception the exception
	 */
	Vector<String> getModifiedObjects() throws Exception;

	/**
	 * Gets the modified person ids.
	 * @return the modified person ids
	 * @throws Exception the exception
	 */
	Vector<String> getModifiedPersonIds() throws Exception;

	/**
	 * Gets the modified reference ids.
	 * @return the modified reference ids
	 * @throws Exception the exception
	 */
	Vector<String> getModifiedReferenceIds() throws Exception;

	/**
	 * Gets the modified user ids.
	 * @return the modified user ids
	 * @throws Exception the exception
	 */
	Vector<String> getModifiedUserIds() throws Exception;

	/**
	 * Gets the new configs.
	 * @return the new configs
	 * @throws Exception the exception
	 */
	Vector<String> getNewConfigs() throws Exception;

	/**
	 * Gets the new id.
	 * @param type the type
	 * @return the new id
	 * @throws Exception the exception
	 */
	PdrId getNewId(String type) throws Exception;

	/**
	 * Gets the new user ids.
	 * @return the new user ids
	 * @throws Exception the exception
	 */
	Vector<String> getNewUserIds() throws Exception;

	/**
	 * Gets the update time stamp.
	 * @return the update time stamp
	 * @throws Exception
	 */
	Date getUpdateTimeStamp() throws Exception;

	/**
	 * Insert id deleted object.
	 * @param pdrId the pdr id
	 * @throws Exception the exception
	 */
	void insertIdDeletedObject(PdrId pdrId) throws Exception;

	/**
	 * Insert id modified object.
	 * @param pdrId the pdr id
	 * @throws Exception the exception
	 */
	void insertIdModifiedObject(PdrId pdrId) throws Exception;

	/**
	 * Insert id modified object.
	 * @param modifiedIds the modified ids
	 * @param type the type
	 * @throws Exception the exception
	 */
	void insertIdModifiedObject(Vector<String> modifiedIds, String type) throws Exception;

	/**
	 * Insert id new object.
	 * @param pdrId the pdr id
	 * @throws Exception the exception
	 */
	void insertIdNewObject(PdrId pdrId) throws Exception;

	/**
	 * Insert id updated object.
	 * @param pdrId the pdr id
	 * @throws Exception the exception
	 */
	void insertIdUpdatedObject(PdrId pdrId) throws Exception;

	/**
	 * Insert id updated objects.
	 * @param pdrIds the pdr ids
	 * @param type the type
	 * @throws Exception the exception
	 */
	void insertIdUpdatedObjects(Vector<String> pdrIds, String type) throws Exception;

	/**
	 * Insert modified config.
	 * @param provider the provider
	 * @throws Exception the exception
	 */
	void insertModifiedConfig(String provider) throws Exception;

	/**
	 * Insert new config.
	 * @param provider the provider
	 * @throws Exception the exception
	 */
	void insertNewConfig(String provider) throws Exception;

	/**
	 * Checks if is modified or new object.
	 * @param pdrId the pdr id
	 * @return true, if is modified or new object
	 * @throws Exception
	 */
	boolean isModifiedOrNewObject(PdrId pdrId) throws Exception;

	/**
	 * Load objects update state.
	 * @param type the type
	 * @return the hash map
	 * @throws Exception the exception
	 */
	HashMap<String, Integer> loadObjectsUpdateState(String type) throws Exception;

	/**
	 * Reset id unused.
	 * @param pdrId the pdr id
	 * @throws Exception the exception
	 */
	void resetIdUnused(PdrId pdrId) throws Exception;

	/**
	 * Sets the update time stamp.
	 * @param date the new update time stamp
	 * @throws Exception the exception
	 */
	void setUpdateTimeStamp(Date date) throws Exception;
}
