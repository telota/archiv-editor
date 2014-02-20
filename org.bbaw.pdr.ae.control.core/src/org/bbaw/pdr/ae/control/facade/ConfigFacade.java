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
package org.bbaw.pdr.ae.control.facade;

import java.util.HashMap;
import java.util.Vector;

import org.bbaw.pdr.ae.config.core.IConfigFacade;
import org.bbaw.pdr.ae.config.core.IConfigManager;
import org.bbaw.pdr.ae.config.core.IConfigRightsChecker;
import org.bbaw.pdr.ae.config.model.DatatypeDesc;

/**
 * Interface of ConfigFacade, main methods to hold and give access to
 * classification configuration. Must be implemented as singleton.
 * @author Christoph Plutte
 */
public class ConfigFacade implements IConfigFacade
{

	/** Facade . */
	private Facade _facade = Facade.getInstanz();

	@Override
	public final IConfigManager getConfigManager()
	{
		return _facade.getConfigManager();
	}

	@Override
	public final IConfigRightsChecker getConfigRichtsChecker()
	{
		return _facade.getConfigRichtsChecker();
	}

	@Override
	public final HashMap<String, DatatypeDesc> getConfigs()
	{
		return _facade.getConfigs();
	}

	@Override
	public final Vector<String> getPersonDisplayNameTags(final String provider)
	{
		return _facade.getPersonDisplayNameTags(provider);
	}

	@Override
	public final Vector<String> getPersonNameTags(final String provider)
	{
		return _facade.getPersonNameTags(provider);
	}

	@Override
	public final void loadLocalConfigBackup(final String selectedDirectory)
	{
		_facade.loadLocalConfigBackup(selectedDirectory);

	}

	@Override
	public final void setConfigs(final HashMap<String, DatatypeDesc> configs)
	{
		_facade.setConfigs(configs);

	}

	@Override
	public final void writeToLocalConfigBackup(final String selectedDirectory) throws Exception
	{
		_facade.writeToLocalConfigBackup(selectedDirectory);

	}

}
