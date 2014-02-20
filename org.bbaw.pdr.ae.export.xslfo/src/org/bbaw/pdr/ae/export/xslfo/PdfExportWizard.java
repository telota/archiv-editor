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

import org.bbaw.pdr.ae.common.NLMessages;
import org.bbaw.pdr.ae.export.pluggable.AeExportCoreProvider;
import org.bbaw.pdr.ae.export.pluggable.AeExportUtilities;
import org.bbaw.pdr.ae.export.swt.FileSelectionGroup;
import org.bbaw.pdr.ae.export.swt.PdrObjectsPreview;
import org.bbaw.pdr.ae.export.xslfo.pages.IntroPage;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.ui.IExportWizard;
import org.eclipse.ui.IWorkbench;

public class PdfExportWizard extends Wizard implements IExportWizard {

	WizardPage introPage;
	
	//public AeExportUtilities provider;
	
	
	public PdfExportWizard() {
		super();
		setWindowTitle(NLMessages.getString("Export_Start"));
		//provider = AeExportCoreProvider.getInstance().getWizardProvider(this);
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		//AeExportCoreProvider.getInstance().getWizardProvider(this).setWizard(this);
		this.createPageControls(getShell());
	}
	
	@Override
	public void addPages() {
		introPage = new IntroPage("PDF Export - Output"); 
		addPage(introPage);
	}

	@Override
	public boolean canFinish() {
		return introPage.isPageComplete();
	}
	
	@Override
	public boolean performFinish() {
		//TODO find a proper stage in life cycle to save GUI component contents
		//to plugin dialog settings section (filename history!)
		AeExportUtilities provider = AeExportCoreProvider.getInstance().getWizardProvider(this);
		boolean returnState = provider.export();
		provider.unregisterWidgets();
		return returnState;
	}

}
