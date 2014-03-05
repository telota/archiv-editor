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
package org.bbaw.pdr.ae.export.xml.pages;

import org.bbaw.pdr.ae.common.AEConstants;
import org.bbaw.pdr.ae.common.CommonActivator;
import org.bbaw.pdr.ae.common.NLMessages;
import org.bbaw.pdr.ae.export.pluggable.AeExportCoreProvider;
import org.bbaw.pdr.ae.export.pluggable.AeExportUtilities;
import org.bbaw.pdr.ae.export.swt.FileSelectionGroup;
import org.bbaw.pdr.ae.export.swt.PdrObjectsPreview;
import org.bbaw.pdr.ae.export.xml.XmlExportWizard;
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
import org.osgi.framework.FrameworkUtil;

//TODO: evtl eigene abstrakte klasse schreiben, die schonmal ispagecomplete und
//TODO getpreviouspage oder so implementiert?
//TODO und z.b. widgettemplate registry
public class IntroPage extends WizardPage {

	private Composite container;
	private XmlExportWizard wizard;
	//TODO; probably just debugging stuff, right?
	private static int count = 0;
	//logger
	private ILog log = AEConstants.ILOGGER;
	
	public IntroPage(String pageName) {
		super(pageName);
	    setTitle(NLMessages.getString("export.xml.title"));
	    setDescription(NLMessages.getString("export.xml.desc"));
	    count++;
	}
	
	@Override
	public void createControl(Composite parent) {
		log.log(new Status(IStatus.INFO, CommonActivator.PLUGIN_ID,
				"Create controls in XML export wizard page instance no. "+count));
		setControl(parent); // wichtig!
		// retrieve owning wizard..
		wizard = (XmlExportWizard) this.getWizard();
		// get id of owning plugin..
		String pluginId = FrameworkUtil.getBundle(getClass()).getSymbolicName();
		// get util provider for XML plugin based on active wizard
		AeExportUtilities provider = AeExportCoreProvider.getInstance().getWizardProvider(wizard);
		
		// assemble GUI
		container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(3, false));
		// label preview tree, row 1
		Label outputLabel = new Label(container, SWT.NONE); 
		outputLabel.setText(NLMessages.getString("export.select.data.lbl"));
		outputLabel.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 
				3, 1));
		// dummy label for layouting
		new Label(container, SWT.NONE);
		// preview treeviewer, spans 3 clolumns, row 2
		log.log(new Status(IStatus.INFO, CommonActivator.PLUGIN_ID,
				"XML wizard page: add objects preview.."));
		// pdr objects selection preview as a checkbox tree viewer
		wizard.preview = new PdrObjectsPreview(pluginId, this, container);
		log.log(new Status(IStatus.INFO, CommonActivator.PLUGIN_ID,
				"XML wizard page: register objects preview at util provider."));
		provider.registerWidget(wizard.preview);
		// dummy label
		new Label(container, SWT.NONE);
		log.log(new Status(IStatus.INFO, CommonActivator.PLUGIN_ID,
				"XML wizard page: add file selector"));
		// file selection control widgets group template for export destination
		wizard.outputFileSelect = new FileSelectionGroup(pluginId, this, container, SWT.SAVE);
		wizard.outputFileSelect.init(NLMessages.getString("export.fileselector.save.caption"),
				NLMessages.getString("export.fileselector.save.desc"), 3);
		provider.registerWidget(wizard.outputFileSelect);
		// arrange all of this
		container.pack();
		container.layout(true, true);
		log.log(new Status(IStatus.INFO, CommonActivator.PLUGIN_ID,
				"XML wizard page set up."));
	}
	
	
	@Override
	public boolean isPageComplete() {
		// FIXME: das geht so nicht. aus irgendwelchen  ecken (org.eclipse.jface plugin)
		// FIXME: rufen hier irgendwelche superklassen an und wollen buttons
		// FIXME fertigmachen. wir sind aber noch überhaupt nicht so weit, daß
		// FIXME die export-widgets überhaupt ansprechbar wären. es fliegen hier jedenfalls
		// FIXME nullpointer ohne ende. der ganze kram mit widget muß irgendwie
		// FIXME in eine eigene komponente, die alles was mit den widgets zu tun hat
		// selbstständig macht. wie man sich bei der seine GUI-konfigurationen bestellt
		// muß dann über irgendein schema geklärt werden, vielleicht sogar extension point
		// (extension point export gui, kann 1 preview und ne handvoll file selector enthalten)
		// FIXME daß nicht klar ist wem die teile gehören und wer die initialisieren soll und so
		// kann jedenfalls nicht angehen
		boolean complete = wizard.outputFileSelect.isValid() &&
				wizard.preview.isValid();
		if (!complete) {
			String message = wizard.outputFileSelect.getMessage();
			if (message == null)
				message = wizard.preview.getMessage();
			this.setErrorMessage(message);
		} else
			this.setErrorMessage(null);
		//FIXME no message shown
		//this.getContainer().updateMessage();
		//this.getContainer().updateTitleBar();
		log.log(new Status(IStatus.INFO, CommonActivator.PLUGIN_ID,
				"completeness state of XML export wizard page 1: "+complete));
		return complete;
	}
	
	@Override
	public IWizardPage getPreviousPage() {
		return null;
	}
	
	@Override
	public IWizardPage getNextPage() {
		return isPageComplete() ? null : null;
	}
	

}
