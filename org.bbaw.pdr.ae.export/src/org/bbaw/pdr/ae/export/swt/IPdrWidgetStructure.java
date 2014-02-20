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
package org.bbaw.pdr.ae.export.swt;

import org.bbaw.pdr.ae.export.pluggable.AeExportCoreProvider;
import org.bbaw.pdr.ae.export.pluggable.AeExportUtilities;
import org.eclipse.jface.dialogs.IDialogSettings;

/**
 * 
 * @author Jakob Hoeper
 *
 */
public interface IPdrWidgetStructure {
	
	public boolean isValid();
	
	public String getMessage();
	
	/**
	 * <p>When this {@link IPdrWidgetStructure} is involved in a successful exporting
	 * operation, it might be nice to reward it by calling this method, so it can
	 * get things done before moving on. For instance, a class implementing this method
	 * might want to save some of its members to the {@link IDialogSettings} section
	 * of its plugin, e.g. by making use of {@link AeExportCoreProvider#getSettings(String)}.</p>
	 * <p>Note that {@link IPdrWidgetStructure} objects may get registered at 
	 * {@link AeExportCoreProvider} without noticing it. Therefore, every {@link IPdrWidgetStructure}
	 * instance should unregister itself either during this method, or in
	 * its {@link Object#finalize()} implementation.
	 * @see AeExportCoreProvider#addToHistory(String, String)
	 * @see AeExportUtilities#unRegisterWidget(IPdrWidgetStructure)
	 */
	public void performFinish();

}
