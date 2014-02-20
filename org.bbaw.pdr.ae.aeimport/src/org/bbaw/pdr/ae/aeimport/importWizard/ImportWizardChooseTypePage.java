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
package org.bbaw.pdr.ae.aeimport.importWizard;

import org.bbaw.pdr.ae.common.NLMessages;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

/** page to choose the import type.
 * @author Christoph Plutte
 *
 */
public class ImportWizardChooseTypePage extends WizardPage
{
     /** import type.*/
    private Combo _importType;
     /** instance of import wizard..*/
    private ImportWizard _importWizard;


     /** constructor.
     * @param pageName page name.
     */
    protected ImportWizardChooseTypePage(final String pageName)
     {
              super(pageName);
              setTitle(NLMessages.getString("ImportWizard_choose_type_title"));
              setDescription(NLMessages.getString("ImportWizard_choose_type_message"));
     }
     /** create control.
     * @param parent parent composite.
     * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
     */
    public final void createControl(final Composite parent)
     {
    	 _importWizard = (ImportWizard) getWizard();

              Composite composite = new Composite(parent, SWT.NONE);
              GridLayout layout = new GridLayout();
              layout.numColumns = 2;
              composite.setLayout(layout);
              setControl(composite);
              new Label(composite, SWT.NONE).setText(NLMessages.getString("ImportWizard_import_type"));
              _importType = new Combo(composite, SWT.READ_ONLY);
              _importType.setItems(new String[] {NLMessages.getString("ImportWizard_markup_configuration"),
            		  NLMessages.getString("ImportWizard_reference_template")});
              _importType.addSelectionListener(new SelectionAdapter()
      			{
      			public void widgetSelected(final SelectionEvent event)
      			{
      				_importWizard.setImportTypeInt(_importType.getSelectionIndex());
      			}
      			}); //SelectionListener
     }
}

