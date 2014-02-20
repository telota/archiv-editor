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
package org.bbaw.pdr.ae.export.xslfo.pages;

import org.bbaw.pdr.ae.common.AEConstants;
import org.bbaw.pdr.ae.common.CommonActivator;
import org.bbaw.pdr.ae.common.NLMessages;
import org.bbaw.pdr.ae.export.pluggable.AeExportCoreProvider;
import org.bbaw.pdr.ae.export.swt.FileSelectionGroup;
import org.bbaw.pdr.ae.export.swt.PdrObjectsPreview;
import org.bbaw.pdr.ae.export.xslfo.PdfExportWizard;
import org.bbaw.pdr.ae.export.xslfo.PdfProvider;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class IntroPage extends WizardPage {

	private Composite container;
	private PdfExportWizard wizard;
	private PdfProvider provider;
	
	private ILog log = AEConstants.ILOGGER;

	public IntroPage(String pageName) {
		super(pageName);
	    setTitle(NLMessages.getString("export.pdf.title"));
	    setDescription(NLMessages.getString("export.pdf.desc"));
	}

	@Override
	public void createControl(Composite parent) {
		
		setControl(parent);
		wizard = (PdfExportWizard) this.getWizard();
		provider = (PdfProvider) AeExportCoreProvider.getInstance().getWizardProvider(wizard);
		
		String pluginId = provider.pluginId();
		
		container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(3, false));
		
		// label preview tree, row 1
		Label outputLabel = new Label(container, SWT.NONE); 
		outputLabel.setText(NLMessages.getString("export.select.data.lbl"));
		outputLabel.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 
				3, 1));
		
		new Label(container, SWT.NONE);
		
		// pdr objects selection preview as a checkbox tree viewer
		provider.preview = new PdrObjectsPreview(pluginId, this, container);
		provider.registerWidget(provider.preview);
		new Label(container, SWT.NONE);
		
		// TODO: implement switch for output type: user should be allowed
		// TODO: to choose from several options:
		// TODO: allow to choose between exporting PDF of FO
		// TODO: and allow to save FO dump to file
		// TODO: OR allow to dynamically change generated FO source before exporting?
		// file selection control widgets group template for export destination
		provider.outputSelector = 
				new FileSelectionGroup(pluginId, this, container, SWT.SAVE);
		provider.outputSelector.init(NLMessages.getString("export.fileselector.save.caption"),
				NLMessages.getString("export.fileselector.save.desc"), 3);
		provider.registerWidget(provider.outputSelector);
		// and style sheet file selection
		provider.styleSelector = 
				new FileSelectionGroup(pluginId, this, container, SWT.OPEN);
		provider.styleSelector.init(NLMessages.getString("export.fileselector.stylesheet.caption"), 
				3, "stylesheets");
		provider.registerWidget(provider.outputSelector);
		
		container.pack();
		container.layout(true, true);
	}
	
	@Override
	public IWizardPage getPreviousPage() {
		return null;
	}
	
	@Override
	public IWizardPage getNextPage() {
		return isPageComplete() ? null : null;
	}
	
	@Override
	public boolean isPageComplete() {
		// FIXME: copied as-is from xml plugin. Re-evaluate!
		// FIXME: nullpointer exception weil provider noch nicht initialisiert ist??
		if (wizard == null || provider.outputSelector == null || provider.styleSelector == null || provider.preview == null)
			return false;
		// TODO: das alles wird viel schöner und besser, wenn wir nur noch widgets in die registry
		// schmeißen und den ganzen krempel nicht mehr selber machen müssen. einmal über alle widgets
		// in der registry rübergucken, und wenn alle valid sind, kanns losgehen. bevor die nicht komplett
		// initialisiert sind, werden die auch nicht registriert.
		boolean complete = provider.outputSelector.isValid() &&
				provider.preview.isValid() &&
				provider.styleSelector.isValid();
		if (!complete) {
			String message = provider.outputSelector.getMessage();
			if (message == null)
				message = provider.preview.getMessage();
			if (message == null)
				message = provider.styleSelector.getMessage();
			this.setErrorMessage(message);
		} else
			this.setErrorMessage(null);
		//FIXME no message shown
		//this.getContainer().updateMessage();
		//this.getContainer().updateTitleBar();
		log.log(new Status(IStatus.INFO, CommonActivator.PLUGIN_ID, 
				"Wizard page completeness: "+complete));
		
		return complete;
	}
	

}
