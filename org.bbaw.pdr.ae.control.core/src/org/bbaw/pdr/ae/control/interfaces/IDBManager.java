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

import java.util.HashMap;

import org.bbaw.pdr.ae.metamodel.PdrId;
import org.bbaw.pdr.ae.model.Aspect;
import org.bbaw.pdr.ae.model.PdrObject;
import org.bbaw.pdr.ae.model.Person;
import org.bbaw.pdr.ae.model.ReferenceMods;
import org.bbaw.pdr.ae.model.ReferenceModsTemplate;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * The Interface IDBManager which handles save, load, delete operations over the
 * data storage. it also imports, exports backups, checks whether data store is
 * empty.
 * @author Christoph Plutte
 */
public interface IDBManager
{

	/**
	 * Creates the db from dir.
	 * @param col the col
	 * @param subDir the sub dir
	 */
	void createDBFromDir(String col, String subDir);

	/**
	 * Creates the empty db.
	 * @param col the col
	 * @throws Exception the exception
	 */
	void createEmptyDB(String col) throws Exception;

	/**
	 * checks whether the database is empty.
	 * @return true if db is empty.
	 */
	boolean dbIsEmpty();

	/**
	 * Delete.
	 * @param pdrId the pdr id
	 * @param col the col
	 * @throws Exception the exception
	 */
	void delete(PdrId pdrId, String col) throws Exception;

	/**
	 * Delete.
	 * @param fileName the file name
	 * @param col the col
	 * @throws Exception the exception
	 */
	void delete(String fileName, String col) throws Exception;

	/**
	 * Load local backup.
	 * @param dir the dir
	 * @param monitor progress monitor for showing progress if available.
	 */
	void loadLocalBackup(String dir, IProgressMonitor monitor);

	/**
	 * Load local config backup.
	 * @param dir the dir
	 */
	void loadLocalConfigBackup(String dir);

	/**
	 * Save reference template to db.
	 * @param templates the templates
	 * @throws Exception the exception
	 */
	void saveReferenceTemplateToDB(HashMap<String, ReferenceModsTemplate> templates) throws Exception;

	

	/**
	 * Save to db.
	 * @param object the object
	 * @throws Exception the exception
	 */
	void saveToDB(PdrObject object) throws Exception;

	
	/**
	 * Save to db.
	 * @param object the object
	 * @param noteChange notes the change in this object if param true.
	 * @throws Exception the exception
	 */
	void saveToDB(PdrObject object, boolean noteChange) throws Exception;
	

	

	/**
	 * Write to local backup.
	 * @param dir the dir
	 * @throws Exception the exception
	 */
	void writeToLocalBackup(String dir) throws Exception;

	/**
	 * Write to local config backup.
	 * @param dir the dir
	 * @throws Exception the exception
	 */
	void writeToLocalConfigBackup(String dir) throws Exception;

	/**
	 * Write to local reference template backup.
	 * @param dir the dir
	 * @throws Exception the exception
	 */
	void writeToLocalReferenceTemplateBackup(String dir) throws Exception;

	boolean isOptimizationRequired();

	void optimizeAll(IProgressMonitor monitor);

}
