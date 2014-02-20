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
package org.bbaw.pdr.ae.config.model;

import java.util.HashMap;

/**
 * The Class Usage.
 * @author Christoph Plutte
 */
public class Usage
{

	/** The _documentation. */
	private HashMap<String, String> _documentation = new HashMap<String, String>();

	/** The _usage display. */
	private UsageDisplay _usageDisplay;

	private ConfigData _templates;

	private ConfigData _personIdentifiers;

	/**
	 * Gets the documentation.
	 * @return the documentation
	 */
	public final HashMap<String, String> getDocumentation()
	{
		return _documentation;
	}

	/**
	 * Gets the usage display.
	 * @return the usage display
	 */
	public final UsageDisplay getUsageDisplay()
	{
		return _usageDisplay;
	}

	/**
	 * Sets the documentation.
	 * @param documentation the documentation
	 */
	public final void setDocumentation(final HashMap<String, String> documentation)
	{
		this._documentation = documentation;
	}

	/**
	 * Sets the usage display.
	 * @param usageDisplay the new usage display
	 */
	public final void setUsageDisplay(final UsageDisplay usageDisplay)
	{
		this._usageDisplay = usageDisplay;
	}

	public ConfigData getTemplates()
	{
		if (_templates == null)
		{
			_templates = new ConfigData();
		}
		return _templates;
	}

	public void setTemplates(ConfigData templates)
	{
		this._templates = templates;
	}

	public void setIdentifiers(ConfigData personIdentifiers)
	{
		this._personIdentifiers = personIdentifiers;

	}

	public ConfigData getIdentifiers()
	{
		if (_personIdentifiers == null)
		{
			_personIdentifiers = new ConfigData();
			_personIdentifiers.setValue("personIdentifiers");
			_personIdentifiers.setLabel("Person Identifiers");
			_personIdentifiers.setMyHaveChildren(true);
			_personIdentifiers.setPos("personIdentifiers");
		}
		return _personIdentifiers;
	}
}
