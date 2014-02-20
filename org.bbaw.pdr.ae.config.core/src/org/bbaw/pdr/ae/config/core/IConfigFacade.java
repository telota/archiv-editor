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
package org.bbaw.pdr.ae.config.core;

import java.util.HashMap;
import java.util.Vector;

import org.bbaw.pdr.ae.config.model.DatatypeDesc;

/** interface that defines the facade methods used by classification configuration plugins.
 * @author plutte
 *
 */
public interface IConfigFacade
{

	/** get-method to get map of saved classification configurations.
	 * HashMap is map of provider name as String (key) and
	 * configuration as DatatypeDesc.
	 * @return map of configurations.
	 */
	HashMap<String, DatatypeDesc> getConfigs();

	/** set-method to set configurations map.
	 * @param configs map of configurations.
	 */
	void setConfigs(HashMap<String, DatatypeDesc> configs);

	/** get semantic tags that define a person name.
	 * @param provider provider name.
	 * @return vector of semantic tags as Strings.
	 */
	Vector<String> getPersonNameTags(String provider);

	/** get semantic tags that define a person display name.
	 * @param provider provider name.
	 * @return vector of semantic tags as Strings.
	 */
	Vector<String> getPersonDisplayNameTags(String provider);

	/** returns the implementation of IConfigManager.
	 * @return implementation of IConfigManager.
	 */
	IConfigManager getConfigManager();

	/** returns the implementation of IConfigRightsChecker.
	 * @return implementation of IConfigRightsChecker.
	 */
	IConfigRightsChecker getConfigRichtsChecker();


	/** loads a local backup of classification configuration from file system.
	 * @param selectedDirectory directory of backup.
	 */
	void loadLocalConfigBackup(String selectedDirectory);

	/** write a local backup of classification configuration to file system.
	 * @param selectedDirectory path to directory where to write the backup.
	 * @throws Exception any kind of exception from data store.
	 */
	void writeToLocalConfigBackup(String selectedDirectory) throws Exception;



}
