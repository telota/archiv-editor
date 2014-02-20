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
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/** view page for solving provider name conflicts.
 * if an imported classification has the same name as one which exists before
 * then the page opens and one can override or rename the conflicting classifications.
 * @author Christoph Plutte
 *
 */
public class ImportWizardProviderConflictPage extends WizardPage
{
     /** override button.*/
    private Button _overrideButton;
     /** new provider name.*/
    private String _newName;
     /** new provider name text.*/
    private Text _newNameText;
     /** boolean override.*/
    private boolean _override = false;
     /** instance of import wizard.*/
    private ImportWizard _importWizard;


     /** constructor.
     * @param pageName page name.
     */
    protected ImportWizardProviderConflictPage(final String pageName)
     {
              super(pageName);
              setTitle(NLMessages.getString("ImportWizard_provider_conflict_title"));
              setDescription(NLMessages.getString("ImportWizard_provider_conflict_message"));
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
              new Label(composite, SWT.NONE).setText(NLMessages.getString("ImportWizard_rename"));
              _newNameText = new Text(composite, SWT.BORDER);
              _newNameText.setLayoutData(new GridData());
              ((GridData) _newNameText.getLayoutData()).horizontalAlignment = GridData.FILL;
              ((GridData) _newNameText.getLayoutData()).grabExcessHorizontalSpace = true;
              if (_importWizard.getImportTypeInt() == 0)
              {
            	  if (_importWizard != null && _importWizard.getImportDatatypeDes() != null
            		  && _importWizard.getImportDatatypeDes().getProvider() != null)
            	  {
            		  _newNameText.setText(_importWizard.getImportDatatypeDes().getProvider());
            	  }
              }
              else if (_importWizard.getImportTypeInt() == 1)
              {
            	  if (_importWizard != null && _importWizard.getImportRefTemplate() != null
            		  && _importWizard.getImportRefTemplate().getValue() != null)
            	  {
            		  _newNameText.setText(_importWizard.getImportRefTemplate().getValue());
            	  }
              }
              _newNameText.addModifyListener(new ModifyListener() {
				
				@Override
				public void modifyText(ModifyEvent e) {
					if (_importWizard.getImportTypeInt() == 0)
		              {
						_importWizard.getImportDatatypeDes().setProvider(_newNameText.getText());
			    		 _importWizard.getContainer().updateButtons();
		              }
					else if (_importWizard.getImportTypeInt() == 1)
		              {
						_importWizard.getImportRefTemplate().setValue(_newNameText.getText());
			    		_importWizard.getContainer().updateButtons();
		              }
					
				}
			});
              _newNameText.addKeyListener(new KeyListener()
              {

				@Override
				public void keyPressed(final KeyEvent e)
				{
				}

				@Override
				public void keyReleased(final KeyEvent e)
				{
					if (_importWizard.getImportTypeInt() == 0)
		              {
						_importWizard.getImportDatatypeDes().setProvider(_newNameText.getText());
			    		 _importWizard.getContainer().updateButtons();
		              }
					else if (_importWizard.getImportTypeInt() == 1)
		              {
						_importWizard.getImportRefTemplate().setValue(_newNameText.getText());
			    		_importWizard.getContainer().updateButtons();
		              }
				}
              });
              _overrideButton = new Button(composite, SWT.CHECK);
              _overrideButton.setText(NLMessages.getString("ImportWizard_override"));
              _overrideButton.addSelectionListener(new SelectionAdapter()
    			{
    			public void widgetSelected(final SelectionEvent event)
    			{
    				_override = !_override;
					_importWizard.setOverride(_override);
		    		 _importWizard.getContainer().updateButtons();


    			}
    			}); //SelectionListener
     }
	/** get new provider name.
	 * @return new provider name.
	 */
	public final String getNewName()
	{
		return _newName;
	}
	/** set new provider name.
	 * @param provider new provider name.
	 */
	public final void setNewNameText(final String provider)
	{
		_newName = provider;
	}
	/** is override existing name.
	 * @return boolean is overriding existing name.
	 */
	public final boolean isOverride()
	{
		return _override;
	}
}
