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

import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import org.eclipse.jface.dialogs.IDialogSettings;

/**
 * <p>A simple implementation of a history of recently accessed filenames.</p>
 * <p>The constructor {@link #FileHistory(IDialogSettings, String)} expects
 * an <code>IDialogSettings</code> section containing plugin-specific settings
 * as well as a <code>String</code> parameter identifying the subsection where
 * the contents of this history are supposed to having been saved.</p> 
 * @author jhoeper
 *
 */
@Deprecated
public class FileHistory {
	
	static final String SEC_HIST="filenames";
	static final String SEC_TIME="timestamps";

	private IDialogSettings section;
	
	private Vector<String> history;
	private HashMap<String, Long> timestamps;
	
	public FileHistory(IDialogSettings section) {
		this.section = section;
		this.history = rebuildHistory();
		this.timestamps = reassignTimes();
	}
	
	/**
	 * create the history vector from the the representation in the 
	 * <code>IDialogSettings</code> format
	 * @return file history as <code>Vector&lt;String&gt;</code>
	 */
	public Vector<String> rebuildHistory() {
		Vector<String> results = new Vector<String>();
		String[] entries = this.section.getArray(SEC_HIST);
		if (entries == null) 
			entries = new String[]{};
		for (int i=0; i<entries.length; i++) 
			results.add(entries[i]);
		return results;
	}
	/**
	 * try to complete the internal history representation by looking up the 
	 * corresponding timestamp values in the <code>IDialogSettings</code> for 
	 * all entries in the filename history. This means that 
	 * {@link #rebuildHistory()} always has to be called before 
	 * {@link #reassignTimes()}!
	 * @return <code>HashMap&lt;String, Long&gt;</code> assigning filename to
	 * timestamp value
	 */
	public HashMap<String, Long> reassignTimes() {
		HashMap<String, Long> results = new HashMap<String, Long>();
		IDialogSettings times = this.section.getSection(SEC_TIME);
		if (times == null)
			times = this.section.addNewSection(SEC_TIME);
		for (String entry : this.history)
			try {
				results.put(entry, times.getLong(entry));
			} catch (Exception e) {}
		return results;
	}
	
	/**
	 * saves this history to the <code>IDialogSettings</code> section known from
	 * the constructor call.
	 */
	public void saveToSettings() {
		// save filenames as an array to the history section
		String[] histArray = this.history.toArray(new String[]{});
		this.section.put(SEC_HIST, histArray);
		// get the time section for timestamp storage
		IDialogSettings times = this.section.getSection(SEC_TIME);
		if (times == null)
			times = this.section.addNewSection(SEC_TIME);
		
		for (String name : this.history) {
			System.out.println("timestamp for filename "+name);
			long timestamp = this.timestamps.containsKey(name) ? this.timestamps.get(name) : 0;
			System.out.println(": "+timestamp);
			times.put(name, timestamp);
		}
	}
	
	/**
	 * <p>Let the given <code>filename</code> be the first element in the history
	 * and get a contemporary <code>timestamp</code>. In case the filename had
	 * already been in the history, it will be removed at the former position.
	 * </p>
	 * <p>Writes changes in dedicated setting section by calling {@link #saveToSettings()}.</p>   
	 * @param filename <code>String</code> pointing to a file's location
	 */
	public void add(String filename) {
		if (filename == null) {
			System.out.println("filename null???");
			return;
		}
		if (this.history.contains(filename))
			this.history.remove(filename);
		this.history.add(0, filename);
		long timestamp = new Date().getTime();
		this.timestamps.put(filename, timestamp);
		this.saveToSettings();
	}
	
	/**
	 * returns the most recently added filename 
	 * @return <code>String</code>
	 */
	public String mostRecent() {
		return (this.history != null && this.history.size()>0) ? 
				this.history.firstElement() : null;
	}
	
	/**
	 * returns a {@link Vector}<code>&lt;String&gt;</code> array containing all 
	 * known filenames sorted according to how recent they have been accessed  
	 * @return {@link Vector}<code>&lt;String&gt;</code>
	 */
	public Vector<String> getRecentFilenames() {
		return this.history;
	}
}
