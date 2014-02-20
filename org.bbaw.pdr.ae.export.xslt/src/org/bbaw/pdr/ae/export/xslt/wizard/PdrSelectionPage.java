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

import org.bbaw.pdr.ae.common.NLMessages;
import org.bbaw.pdr.ae.export.swt.FileSelectionGroup;
import org.bbaw.pdr.ae.export.swt.PdrObjectsPreview;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public class PdrSelectionPage extends WizardPage {

	HtmlExportWizard wizard;
	
	protected PdrSelectionPage(String pageName) {
		super(pageName);
		setTitle(NLMessages.getString("export.html.title"));
		setDescription(NLMessages.getString("export.html.desc"));
	}

	@Override
	public void createControl(Composite parent) {
		setControl(parent);
		
		wizard = (HtmlExportWizard)getWizard();
		
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(3, false));
		
		String pluginId = wizard.provider.pluginId();
		//PdrObject[] pdrObjects = wizard.provider.getPdrObjects();
		
		wizard.preview = new PdrObjectsPreview(pluginId, this,  container);
		//wizard.preview.setInput(pdrObjects);
		
		wizard.outputSelector = new FileSelectionGroup(pluginId, this, 
				container, SWT.SAVE);
		wizard.outputSelector.init(NLMessages.getString("export.fileselector.save.caption"), 3);
		
		wizard.stylesheetSelector = new FileSelectionGroup(pluginId, this,
				container, SWT.OPEN);
		wizard.stylesheetSelector.init(NLMessages.getString("export.fileselector.stylesheet.caption"), 3, 
				"stylesheets");
	}
	
	@Override
	public IWizardPage getPreviousPage() {
		return null;
	}
	
	@Override
	public boolean isPageComplete() {
		// TODO generalize
		boolean complete =
				(wizard != null && wizard.preview.isValid() 
					&& wizard.outputSelector != null && wizard.outputSelector.isValid()
					&& wizard.stylesheetSelector != null && wizard.stylesheetSelector.isValid());
		if (!complete) {
			String message = wizard.preview.getMessage();
			if (message == null && wizard.stylesheetSelector != null)
				message = wizard.stylesheetSelector.getMessage();
			if (message == null && wizard.outputSelector != null)
				message = wizard.outputSelector.getMessage();
			this.setErrorMessage(message);
		} else
			this.setErrorMessage(null);
		
		return complete;
	}

}
