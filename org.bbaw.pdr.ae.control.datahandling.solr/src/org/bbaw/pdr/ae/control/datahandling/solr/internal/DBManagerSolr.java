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

import org.bbaw.pdr.ae.control.interfaces.IDBManager;
import org.bbaw.pdr.ae.metamodel.PdrId;
import org.bbaw.pdr.ae.model.Aspect;
import org.bbaw.pdr.ae.model.PdrObject;
import org.bbaw.pdr.ae.model.Person;
import org.bbaw.pdr.ae.model.ReferenceMods;
import org.bbaw.pdr.ae.model.ReferenceModsTemplate;
import org.eclipse.core.runtime.IProgressMonitor;

public class DBManagerSolr implements IDBManager {

	@Override
	public void saveToDB(Person p) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void saveToDB(Aspect a) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void saveToDB(ReferenceMods r) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void saveReferenceTemplateToDB(
			HashMap<String, ReferenceModsTemplate> templates) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void writeToLocalBackup(String dir) throws Exception {
		// TODO Auto-generated method stub

	}


	@Override
	public void delete(PdrId pdrId, String col) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(String fileName, String col) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void createEmptyDB(String col) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void loadLocalConfigBackup(String dir) {
		// TODO Auto-generated method stub

	}

	@Override
	public void writeToLocalConfigBackup(String dir) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void saveToDB(PdrObject object) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void writeToLocalReferenceTemplateBackup(String dir)
			throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void createDBFromDir(String col, String subDir) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean dbIsEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void loadLocalBackup(String dir, IProgressMonitor monitor)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isOptimizationRequired()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void optimizeAll(IProgressMonitor monitor)
	{
		// TODO Auto-generated method stub

	}

}
