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
package org.bbaw.pdr.ae.export.xslt.wizard;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.bbaw.pdr.ae.common.AEConstants;
import org.bbaw.pdr.ae.common.CommonActivator;
import org.bbaw.pdr.ae.common.NLMessages;
import org.bbaw.pdr.ae.export.pluggable.AeExportCoreProvider;
import org.bbaw.pdr.ae.export.pluggable.AeExportUtilities;
import org.bbaw.pdr.ae.export.swt.FileSelectionGroup;
import org.bbaw.pdr.ae.export.swt.PdrObjectsPreview;
import org.bbaw.pdr.ae.export.xml.utils.XMLContainer;
import org.bbaw.pdr.ae.export.xslt.util.XSLTProcessor;
//import org.eclipse.core.internal.content.ContentType;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Button;
import org.eclipse.ui.IExportWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

public class HtmlExportWizard extends Wizard implements IExportWizard {

	AeExportUtilities provider;
	
	PdrObjectsPreview preview;
	FileSelectionGroup outputSelector;
	FileSelectionGroup stylesheetSelector;
	Button opnExtBtn;
	
	private ILog log = AEConstants.ILOGGER;
	
	public HtmlExportWizard() {
		super();
		setWindowTitle(NLMessages.getString("Export_Start"));
		provider = AeExportCoreProvider.getInstance().getWizardProvider(this);
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		createPageControls(getShell());
	}

	@Override
	public void addPages() {
		addPage(new PdrSelectionPage("Export HTML - Select content"));
	}
	
	@Override
	public boolean canFinish() {
		boolean result=true;
		for (IWizardPage page : this.getPages())
			result &= page.isPageComplete();
		return result;
	}
	
	@Override
	public boolean performFinish() {
		//TODO: test!
		File file = outputSelector.getFile();
		if (file == null)
			return false;
		
		String filename = file.getAbsolutePath();
		File styleFile = stylesheetSelector.getFile();
		
		XMLContainer xml = new XMLContainer(preview.getSelectionHeads());
		
		Source src;
		try {
			src = new StreamSource(new ByteArrayInputStream(
					xml.toString().getBytes("UTF-8")));
		} catch (UnsupportedEncodingException e) {
			log.log(new Status(IStatus.ERROR, CommonActivator.PLUGIN_ID,
					"Could not process XML"));
			return false;
		}
		
		Source stylesheet = new StreamSource(styleFile);
		
		XSLTProcessor xslt = new XSLTProcessor(src, stylesheet);
		if (xslt.process()) {
			try	{
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
						new FileOutputStream(filename), "UTF-8"));
				writer.write(xslt.result().toString());
				writer.close();
				log.log(new Status(IStatus.INFO, CommonActivator.PLUGIN_ID,
						"Succesfully saved HTML to file " + filename));
			} catch (Exception e) {
				log.log(new Status(IStatus.ERROR, CommonActivator.PLUGIN_ID,
					"Saving file failed!\n"+filename));
				e.printStackTrace();
				return false;
			}
			// Open in external Browser
			if (this.opnExtBtn.getSelection())
				try {
					PlatformUI.getWorkbench().getBrowserSupport().getExternalBrowser().openURL(
							file.toURI().toURL());
					//
					PlatformUI.getWorkbench().getEditorRegistry().getDefaultEditor(filename, null);
					provider.log(IStatus.INFO, "Opening Result in Browser instance: "+
							PlatformUI.getWorkbench().getBrowserSupport().getExternalBrowser().getId());
				} catch (Exception e) {
					provider.log(IStatus.ERROR, "Could not open export result in system default browser somehow");
				}
		}
		provider.getSettings().put("open_ext_browser", this.opnExtBtn.getSelection());
		provider.terminateWidgets();
		return true;
	}

}
