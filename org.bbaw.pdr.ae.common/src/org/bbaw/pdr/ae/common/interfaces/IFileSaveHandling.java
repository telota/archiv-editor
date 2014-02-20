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
package org.bbaw.pdr.ae.common.interfaces;

import org.eclipse.swt.widgets.Composite;

/** Interface for file save handling. Must be implemented according to RCP or RAP.
 * @author Christoph Plutte
 *
 */
public interface IFileSaveHandling
{
	/** returns true if editor is running as desktop application and may export to user's local file system.
	 * @return true if local file system.
	 */
	boolean isLocalFileSystem();
	/** creates dialog for file download.
	 * @param fileName name of file.
	 * @param linkName display name of link.
	 * @param title title of dialog.
	 * @param message message of dialog.
	 */
	void createFileDownloadDialog(String fileName, String linkName, String title, String message);

	/** creates a composite allowing to choose a location where to write a file. Composite is
	 * created within the given composite. If composite is null, composite will be created in new Dialog.
	 * @param composite parent composite.
	 * @param fileName name of file.
	 * @param directory directory
	 * @param title title.
	 * @param message message.
	 * @return location.
	 */
	String createFileSaveDialog(Composite composite, String fileName, String directory, String title, String message);

	/** set file extension filter.
	 * @param extensions of accepted files.
	 */
	void setFilterExtensions(String[] extensions);

}
