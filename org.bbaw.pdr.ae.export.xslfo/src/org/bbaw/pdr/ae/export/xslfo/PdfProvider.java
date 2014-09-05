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
package org.bbaw.pdr.ae.export.xslfo;

import java.awt.Desktop;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;

import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.bbaw.pdr.ae.export.pluggable.AeExportUtilities;
import org.bbaw.pdr.ae.export.swt.FileSelectionGroup;
import org.bbaw.pdr.ae.export.swt.PdrObjectsPreview;
import org.bbaw.pdr.ae.export.xml.utils.XMLContainer;
import org.bbaw.pdr.ae.export.xslt.util.XSLTProcessor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.swt.widgets.Button;
import org.eclipse.ui.IExportWizard;
import org.osgi.framework.FrameworkUtil;

public class PdfProvider extends AeExportUtilities {

	private String pluginId;
	private IExportWizard wizard;
	
	public FopFactory fopFactory;
	public TransformerFactory transFactory;

	public PdrObjectsPreview preview;
	public FileSelectionGroup outputSelector;
	public FileSelectionGroup styleSelector;
	public Button opnExtBtn;

	
	public PdfProvider() {
		setPluginId();
		log(IStatus.INFO, "PDF PLUGIN ALIVE. YEAH!!!");
	}

	@Override
	public void setWizard(IExportWizard wizard) {
		super.setWizard(wizard);
	}

	@Override
	public String pluginId() {
		return pluginId;
	}

	@Override
	public void setPluginId() {
		pluginId = FrameworkUtil.getBundle(getClass()).getSymbolicName();
	}
	
	@Override
	public boolean export() {
		fopFactory = FopFactory.newInstance();
		transFactory = TransformerFactory.newInstance();
				
		
		//FIXME: copied, as-is, from xml plugin. has to be adjusted for xslfo
		File file = outputSelector.getFile();
		if (file == null) return false;
		// style sheet file
		File styleFile = styleSelector.getFile();
		if (styleFile == null) return false;
		
		// TODO: retrieve registered widgets from core plugin!
		XMLContainer xml = new XMLContainer(preview.getSelectionHeads());

		// outputstream pointing to selected file
		OutputStream out;
		
		log(IStatus.INFO, "Prepare XSLT processor for style sheet "+
				styleFile.getAbsolutePath());
		// set up XSLT processor 
		XSLTProcessor xslt = 
				new XSLTProcessor(xml.getStream(), styleFile.getAbsolutePath());		
		
		//perform XSLT processing into (hopefully) FO source
		if (xslt.process()) {
			try {
				out = new BufferedOutputStream(new FileOutputStream(file));				
				// FOP processor set up for PDF on outputstream
				Fop fop = fopFactory.newFop("application/pdf", out);
				// retrieve some SAX XML parser from FOP proc
				// create holding object for transformation result targeting that SAX handler
				Result res = new SAXResult(fop.getDefaultHandler());
				// create transformer that will take outputstream and SAX container into account
				Transformer transformer = transFactory.newTransformer();
				// get resulting FO source from XSL transformation
				Source xslfo = xslt.result().getStream();
				// transform FO source into PDF, send to output stream
				transformer.transform(xslfo, res);
				// close stream
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
				log(IStatus.ERROR,"FO/PDF processing not successful");
				return false;
			}
		} else 
			return false;
		log(IStatus.OK, "PDF export to file "+file.getAbsolutePath()+"complete!");
		// open in external appl
		if (opnExtBtn.getSelection())
			try {
				Desktop.getDesktop().open( file );
			} catch (IOException e) {
				log(IStatus.ERROR, "Could not open system PDF viewer or sth.");
				log(IStatus.WARNING, e.getMessage());
				e.printStackTrace();
			}
		this.getSettings().put("open_ext_viewer", opnExtBtn.getSelection());
		super.terminateWidgets();
		return true;
	}
	
	@Override
	protected void finalize() throws Throwable {
		log(IStatus.INFO, "PDF Export Provider ready to leave.");
		super.unregisterWidgets();
		super.finalize();
	}

	@Override
	public IExportWizard getWizard() {
		return wizard;
	}



}
