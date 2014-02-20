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

import java.util.HashMap;
import java.util.Map;

import org.bbaw.pdr.ae.common.AEConstants;
import org.bbaw.pdr.ae.common.AEPluginIDs;
import org.bbaw.pdr.ae.common.CommonActivator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.AbstractSourceProvider;
import org.eclipse.ui.ISources;

public class AEVersionProvider extends AbstractSourceProvider
{

	public final static String ADVANCED = "ADVANCED";
	public final static String LITE = "LITE";
	private boolean _advanced = true;
	private boolean canSynchronize = true;

	@Override
	public void dispose()
	{
	}

	// You cannot return NULL
	@SuppressWarnings(
	{"rawtypes", "unchecked"})
	@Override
	public Map getCurrentState() {
		Map map = new HashMap(1);
		String value = _advanced ? ADVANCED : LITE;
		String url = Platform.getPreferencesService().getString(CommonActivator.PLUGIN_ID, "REPOSITORY_URL",
				AEConstants.REPOSITORY_URL, null);
		int rep = Platform.getPreferencesService().getInt(CommonActivator.PLUGIN_ID, "REPOSITORY_ID",
				AEConstants.REPOSITORY_ID, null);
		int pro = Platform.getPreferencesService().getInt(CommonActivator.PLUGIN_ID, "PROJECT_ID",
				AEConstants.PROJECT_ID, null);
		if (url == null || "".equals(url.trim()) || "xxx".equals(url) || rep == 0 || pro == 0)
		{
			canSynchronize = false;
		}
		String var2 = canSynchronize ? "TRUE" : "FALSE";
		map.put(AEPluginIDs.SOURCE_PARAMETER_AE_ADVANCED_VERSION, value);
		map.put(AEPluginIDs.SOURCE_PARAMETER_CAN_SYNCHRONIZE, var2);
		return map;
	}

	// We could return several values but for this example one value is sufficient
	@Override
	public String[] getProvidedSourceNames() {
		return new String[]
		{AEPluginIDs.SOURCE_PARAMETER_AE_ADVANCED_VERSION,
				AEPluginIDs.SOURCE_PARAMETER_CAN_SYNCHRONIZE};
	}

	// This method can be used from other commands to change the state
	// Most likely you would use a setter to define directly the state and not
	// use this toogle method
	// But hey, this works well for my example
	public void setAEAdvancedVersion(boolean advanced)
	{
		if (!this._advanced == advanced)
		{
			_advanced = advanced;
			String value = advanced ? ADVANCED : LITE;
			fireSourceChanged(ISources.WORKBENCH, AEPluginIDs.SOURCE_PARAMETER_AE_ADVANCED_VERSION, value);
		}
	}
	
	public void setCanSynchronize(boolean canSynchronize)
	{
		if (!this.canSynchronize == canSynchronize)
		{
			this.canSynchronize = canSynchronize;
			String value = canSynchronize ? "TRUE" : "FALSE";
			fireSourceChanged(ISources.WORKBENCH, AEPluginIDs.SOURCE_PARAMETER_CAN_SYNCHRONIZE, value);
		}
	}

}