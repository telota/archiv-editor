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


/** Interface defines methods of IConfigManager that manages dababase access for
 * saving, loading and deleting classification configurations.
 * @author plutte
 *
 */
public interface IConfigManager
{
	/** set configuration.
	 * @param datatypeDesc configuration.
	 */
	void setDatatypeDesc(DatatypeDesc datatypeDesc);
	/** get classification configuration.
	 * @return configuration as DatatypeDesc.
	 * @throws Exception any kind of Exception that can occur during database access.
	 */
	DatatypeDesc getDatatypeDesc()throws Exception;

	/** loads classification configuration from database and returns it as DatatypeDesc.
	 * @return configuration as DatatypeDesc.
	 * @param provider provider name.
	 * @throws Exception any kind of Exception that can occur during database access.
	 */
	DatatypeDesc getDatatypeDesc(String provider)throws Exception;

	/** saves configuration to database.
	 * @param datatypeDesc configuration as DatatypeDesc.
	 * @throws Exception any kind of Exception that can occur during database access.
	 */
	void saveConfig(DatatypeDesc datatypeDesc) throws Exception;

	/** set map of configurations.
	 * @param configs map of configuration.
	 */
	void setConfigs(HashMap<String, DatatypeDesc> configs);

	/** get method, returns all configurations as map of provider name (key) and configuration DatatypeDesc (value).
	 * @return map of configurations.
	 * @throws Exception any kind of Exception that can occur during database access.
	 */
	HashMap<String, DatatypeDesc> getConfigs() throws Exception;

	/** deletes configuration of given provider name from database.
	 * @param provider name as String.
	 */
	void deleteConfig(String provider);
	/** get method, returns all configurations as xml string of provider name (key)
	 * and configuration DatatypeDesc (value).
	 * @param providers vector of provider names as Strings.
	 * @return vector of xml Strings.
	 * @throws Exception any kind of Exception that can occur during database access.
	 */
	Vector<String> getConfigs(Vector<String> providers) throws Exception;
}
