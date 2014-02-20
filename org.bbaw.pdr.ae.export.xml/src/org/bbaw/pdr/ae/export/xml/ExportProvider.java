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
package org.bbaw.pdr.ae.export.xml;

import org.bbaw.pdr.ae.export.pluggable.AeExportUtilities;
import org.bbaw.pdr.ae.export.xml.utils.XMLContainer;
import org.bbaw.pdr.ae.model.PdrObject;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.ui.IExportWizard;
import org.osgi.framework.FrameworkUtil;

// TODO: vielleicht IExecutableExtension implementieren lassen, weil das einen
// Aufruf einer initialisierungsmethode nach sich ziehen wuerde, der das 
// verbundene IConfigurationElement uebergibt 
public class ExportProvider extends AeExportUtilities {

	IExportWizard wizard;
	IDialogSettings dialogSettings;
	String pluginId;
	
	public ExportProvider() {
		System.out.println("creating utility provider for XML export");
		setPluginId();
	}
	
	@Override
	public String pluginId() {
		return pluginId;
	}

	@Override
	public void setWizard(IExportWizard wizard) {
		System.out.println(" telling utility provider about wizard: "+wizard.getClass().getCanonicalName());
		super.setWizard(wizard);
		this.wizard = wizard;
	}
	
	@Override
	public IExportWizard getWizard() {
		return wizard;
	}

	public XMLContainer createXML(PdrObject[] objects) {
		return new XMLContainer(objects);
	}



	@Override
	public void setPluginId() {
		pluginId = FrameworkUtil.getBundle(getClass()).getSymbolicName();
	}



}
