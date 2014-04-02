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

//import java.awt.Desktop;
import java.io.File;
//import java.io.IOException;

import org.bbaw.pdr.ae.common.AEConstants;
import org.bbaw.pdr.ae.common.CommonActivator;
import org.bbaw.pdr.ae.common.NLMessages;
import org.bbaw.pdr.ae.export.pluggable.AeExportCoreProvider;
import org.bbaw.pdr.ae.export.pluggable.AeExportUtilities;
import org.bbaw.pdr.ae.export.swt.FileSelectionGroup;
import org.bbaw.pdr.ae.export.swt.PdrObjectsPreview;
import org.bbaw.pdr.ae.export.xml.pages.IntroPage;
import org.bbaw.pdr.ae.export.xml.utils.XMLContainer;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.ui.IExportWizard;
import org.eclipse.ui.IWorkbench;

//TODO: doc
// FIXME: write superclass to imply how to set up (get provider etc.)
/**
 * 
 * @author jhoeper
 *
 */
public class XmlExportWizard extends Wizard implements IExportWizard{

	protected WizardPage introPage;
	protected WizardPage optionsPage;
	
	public PdrObjectsPreview preview;
	public FileSelectionGroup outputFileSelect;
	
	private ILog log = AEConstants.ILOGGER; 
	
	public XmlExportWizard() {
		super();
		setWindowTitle(NLMessages.getString("Export_Start"));
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		AeExportCoreProvider.getInstance().getWizardProvider(this).setWizard(this);
		this.createPageControls(getShell());
	}
	
	@Override
	public void addPages() {
		introPage = new IntroPage(""); 
		addPage(introPage);
	}
	
	@Override
	public boolean canFinish() {
		return introPage.isPageComplete();
	}

	@Override
	public boolean performFinish() {
		
		//TODO:	getContainer().run(true, true, new IRunnableWithProgress() 
		
		AeExportUtilities provider = AeExportCoreProvider.getInstance().getWizardProvider(this);
		File file = outputFileSelect.getFile();
		if (file == null) return false;
		String filename = file.getAbsolutePath();
		//TODO: test!
		//XMLContainer xml = new XMLContainer(preview.getSelectedObjects());
		
		XMLContainer xml = new XMLContainer(preview.getSelectionHeads());
		try	{
			xml.saveToFile(filename);		
			provider.terminateWidgets();
			log.log(new Status(IStatus.INFO, CommonActivator.PLUGIN_ID,
				"Succesfully saved XML to file " + filename));			
		} catch (Exception e) {
			log.log(new Status(IStatus.ERROR, CommonActivator.PLUGIN_ID,
					"Saving file failed!\n"+filename));
			e.printStackTrace();
			provider.unregisterWidgets();
			return false;
		}
		//AeExportCoreProvider.getInstance().saveSettings();
		// open result in external application
		/*
		try {
			Desktop.getDesktop().open(file);
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		return true;
	}
	
	@Override
	public boolean performCancel() {
		//AeExportCoreProvider.getInstance().getWizardProvider(this).unregisterWidgets();
		return super.performCancel();
	}
}
