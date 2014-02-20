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
package org.bbaw.pdr.ae.export.internal;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import org.bbaw.pdr.ae.common.AEConstants;
import org.bbaw.pdr.ae.export.pluggable.AeExportCoreProvider;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.ui.internal.WorkbenchPlugin;

//TODO: doc
/**
 * 
 * @author jhoeper
 *
 */
public class DialogSettingsRegistry {
	// Settings landen in runtime-org.bbaw.pdr.ae.standalone.product2/.metadata/.plugins/org.eclipse.ui.workbench/dialog_settings.xml

	private static final String aeExportSettingsSection = 
								"AeExportPluginsDialogs";
	
	protected static final String HISTORY_FILES_ARRAY_ID = "files";
	
	IDialogSettings rootSettings;
	
	public DialogSettingsRegistry(){
		IDialogSettings workbenchSettings = WorkbenchPlugin.getDefault()
				.getDialogSettings();		
		rootSettings = workbenchSettings.getSection(aeExportSettingsSection);
		if (rootSettings == null)
			rootSettings = workbenchSettings.addNewSection(aeExportSettingsSection);
	}
	
	/**
	 * Returns a {@link IDialogSettings} representation of the subsection that
	 * the calling plugin requests. If denoted subsection cannot be found, it
	 * is created before being returned.
	 * @param pluginId the id of the caling plugin
	 * @param subsection a subsection may be specified, for instance one allocated
	 * by a recent-files history, usually identified by 
	 * {@link AeExportCoreProvider#DEF_HIST}
	 * @return {@link IDialogSettings}
	 */
	public IDialogSettings getSubSection(String pluginId, String subsection) {
		IDialogSettings section = getSection(pluginId);
		if (subsection == null)
			subsection = AeExportCoreProvider.DEF_SET;
		IDialogSettings sub = section.getSection(subsection);
		if (sub == null)
			sub = section.addNewSection(subsection);
		return sub;
	}
	
	/**
	 * Returns whether an {@link IDialogSettings} subsection is present at the
	 * settings section of a specified plugin.
	 * @param pluginId
	 * @param subsection
	 * @return true if subsection exists, false otherwise
	 */
	public boolean hasSubSection(String pluginId, String subsection) {
		IDialogSettings pluginSettings = rootSettings.getSection(pluginId);
		if (pluginSettings != null)
			return (pluginSettings.getSection(subsection) != null);
		return false;
	}
	
	/**
	 * Returns the {@link IDialogSettings} section allocated for the calling
	 * plugin. If plugins didn't have its own section so far, it will be created
	 * and returned. 
	 * @param pluginId the calling plugin is expected to pass its id
	 * @return {@link IDialogSettings}
	 */
	public IDialogSettings getSection(String pluginId) {
		IDialogSettings section = rootSettings.getSection(pluginId);
		if (section == null) {
			section = rootSettings.addNewSection(pluginId);
			// set up some default values, history etc
			section.put(AeExportCoreProvider.DEF_DIR, AeExportCoreProvider.SETTINGS_SAVE_DIR);
			String def_filename = AeExportCoreProvider.SETTINGS_SAVE_FN;
			if (hasSubSection(pluginId, AeExportCoreProvider.EXT_OUT)) {
				IDialogSettings filetypes = getSubSection(pluginId, AeExportCoreProvider.EXT_OUT);
				if (filetypes.getArray(AeExportCoreProvider.DEF_SET) != null) {
					def_filename+=".";
					def_filename+=filetypes.getArray(AeExportCoreProvider.DEF_SET)[0].split(".")[1];
				}
			} 
			section.put(AeExportCoreProvider.DEF_FILE, def_filename);
			// section.addNewSection(AeExportCoreProvider.DEF_HIST);
		}		
		return section;
	}
	
	
	/**
	 * Returns a {@link LinkedHashMap} containing recently used filenames and their timestamps
	 * under the custom identifier <code>name</code>, sorted by time in descending order.
	 * 
	 * @param pluginId
	 * @param name
	 * @return
	 */
	public LinkedHashMap<String, Long> getHistory(String pluginId, String name) {
		// use linked structures to preserve element order
		LinkedHashMap<String, Long> history = new LinkedHashMap<String, Long>();
		// get subsection of settings section for requesting plugin
		IDialogSettings hist = getSubSection(pluginId, name);
		// for specified history (name), try to read list of prviously used filenames
		String[] files = hist.getArray(DialogSettingsRegistry.HISTORY_FILES_ARRAY_ID);
		if (files == null || files.length<1) {
			hist.put(HISTORY_FILES_ARRAY_ID, new String[0]);
			return history;
		}
		// if recent files could be loaded, try to load their respective timestamps
		
		for (String filename : files) {
			
			Long timestamp;
			try {
				timestamp = hist.getLong(filename);
			} catch (NumberFormatException e) {
				timestamp = new Long(0);
			}
			history.put(filename, (timestamp!=null)?timestamp:0);
		}
		// sort linked map by timestamps		
		List<Map.Entry<String, Long>> entries = 
				new LinkedList<Map.Entry<String, Long>>(history.entrySet());
		// comparator sorts in descending order
		Collections.sort(entries, new Comparator<Map.Entry<String, Long>>(){
				@Override
				public int compare(Entry<String, Long> o1,
						Entry<String, Long> o2) {
					return o2.getValue().compareTo(o1.getValue());
				}
			});
		history = new LinkedHashMap<String, Long>();
		if (entries.size()>0)
			for (Map.Entry<String, Long> entry : entries)
				history.put(entry.getKey(), entry.getValue());
		/*else
			// if no files are known to have been used recently, look for a default file locator
			if (hist.get(AeExportCoreProvider.DEF_DEF_FN) != null) {
				history.put(hist.get(AeExportCoreProvider.DEF_DEF_FN), (long)0);
				System.out.println(" Placing default filename into recent files: "+hist.get(AeExportCoreProvider.DEF_DEF_FN));
			}*/
			
		
		System.out.println(getClass().getCanonicalName()+
				" returning file history "+name+": "+entries.get(0).getKey()+
				"("+entries.get(0).getValue()+")");
		return history;
	}
	
	/**
	 * Returns a {@link LinkedHashMap} containing the default recent-files-list
	 * for the specified plugin.
	 * @param pluginId
	 * @return FileHistory
	 */
	public LinkedHashMap<String, Long> getHistory(String pluginId) {
		return getHistory(pluginId, AeExportCoreProvider.DEF_HIST);
	}
	
	/**
	 * Save given key and a fresh timestamp in the recent-files history
	 * specified by the identifier history in the settings section of specified plugin.
	 * @param pluginId
	 * @param history
	 * @param key
	 */
	public void addToHistory(String pluginId, String history, String key) {
		// get subsection of settings section for requesting plugin
		IDialogSettings hist = getSubSection(pluginId, history);
		String[] filenames = hist.getArray(HISTORY_FILES_ARRAY_ID);
		Vector<String> files = (filenames != null) ? new Vector<String>(Arrays.asList(filenames)) 
				: new Vector<String>();
		if (!files.contains(key)) {
			files.add(key);
			hist.put(HISTORY_FILES_ARRAY_ID, files.toArray(new String[files.size()]));
		}
		long timestamp = new Date().getTime();
		hist.put(key, timestamp);
	}
	
	
	/**
	 * saves all settings to a specified file
	 * @param filename
	 * @throws IOException
	 */
	public void saveToFile(String filename) throws IOException {
		rootSettings.save(filename);
	}
	
	/**
	 * loads settings stored in specified file into internal root settings 
	 * section
	 * @param filename
	 * @throws IOException
	 */
	public void loadFromFile(String filename) throws IOException {
		rootSettings.load(filename);
	}
	
	
	public void save() {
		WorkbenchPlugin.getDefault().getDialogSettings().addSection(rootSettings);
	}
	
	/**
	 * save settings for this pluginId under the specified directory
	 * @param pluginId
	 * @param directory
	 * @throws IOException
	 */
	public void savePluginSettings(String pluginId, String directory) throws IOException {
		IDialogSettings settings = getSection(pluginId);
		settings.save(directory+AEConstants.FS+pluginId+".conf");
	}
	
}
