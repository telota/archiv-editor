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

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.bbaw.pdr.ae.common.NLMessages;
import org.bbaw.pdr.ae.config.core.DataDescSaxHandler;
import org.bbaw.pdr.ae.config.core.IConfigManager;
import org.bbaw.pdr.ae.config.model.DatatypeDesc;
import org.bbaw.pdr.ae.control.facade.Facade;
import org.bbaw.pdr.ae.control.saxHandler.ReferenceSaxHandler;
import org.bbaw.pdr.ae.model.ReferenceModsTemplate;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;

/** view page to select and load a file for import.
 * @author Christoph Plutte
 *
 */
public class ImportWizardFilePage extends WizardPage
{
     /** file path.*/
    private Text _filePath;
     /** provider text.*/
    private Text _providerText;
     /** provider label.*/
    private Label _providerLabel;
     /** display name.*/
    private Text _displayNameText;
     /** select file button.*/
    private Button _selectFile;
     /** load selected file button.*/
    private Button _loadFile;
     /** imported config as default button.*/
    private Button _asConfigAsDefault;
     /** file path as string.*/
    private String _filePathString;
     /** main composite.*/
    private Composite _composite;
     /** content composite.*/
    private Composite _contentComp;
     /** boolean config as default.*/
    private boolean _configAsDefault = true;
     /** instance of import wizard.*/
    private ImportWizard _importWizard;
 	/** singleton instance of facade.*/
 	private Facade _facade = Facade.getInstanz();


     /** constructor.
     * @param pageName page name.
     */
    protected ImportWizardFilePage(final String pageName)
     {
              super(pageName);
              setTitle(NLMessages.getString("ImportWizard_select_file_title"));
              setDescription(NLMessages.getString("ImportWizard_select_file_message"));
     }
     /** create control.
     * @param parent parent composite.
     * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
     */
    public final void createControl(final Composite parent)
     {
    	 _importWizard = (ImportWizard) getWizard();
         _composite = new Composite(parent, SWT.NONE | SWT.FILL);
         GridLayout layout = new GridLayout();
         layout.numColumns = 2;
         _composite.setLayout(layout);
         _composite.setLayoutData(new GridData());
         ((GridData) _composite.getLayoutData()).horizontalAlignment = SWT.FILL;
         ((GridData) _composite.getLayoutData()).grabExcessHorizontalSpace = true;
         setControl(_composite);
     }
    /**
     * load values according to selected import type.
     */
    final void loadValues()
    {
    	if (_contentComp != null)
    	{
    		_contentComp.dispose();
    		_contentComp = null;
    	}
    	_contentComp = new Composite(_composite, SWT.NONE | SWT.FILL);
         GridLayout layout = new GridLayout();
         layout.numColumns = 2;
         _contentComp.setLayout(layout);
         _contentComp.setLayoutData(new GridData());
         ((GridData) _contentComp.getLayoutData()).horizontalAlignment = SWT.FILL;
         ((GridData) _contentComp.getLayoutData()).grabExcessHorizontalSpace = true;
    	new Label(_contentComp, SWT.NONE).setText(NLMessages.getString("ImportWizard_file"));
        _filePath = new Text(_contentComp, SWT.BORDER);
        _filePath.setLayoutData(new GridData());
        ((GridData) _filePath.getLayoutData()).horizontalAlignment = SWT.FILL;
        ((GridData) _filePath.getLayoutData()).grabExcessHorizontalSpace = true;
        _selectFile = new Button(_contentComp, SWT.PUSH);
        _selectFile.setText(NLMessages.getString("ImportWizard_select"));
        _selectFile.addSelectionListener(new SelectionAdapter()
			{
			public void widgetSelected(final SelectionEvent event)
			{
				IWorkbench workbench = PlatformUI.getWorkbench();
	              Display display = workbench.getDisplay();
	              Shell shell = new Shell(display);
	              FileDialog fileDialog = new FileDialog(shell);
	              fileDialog.setFilterPath("/"); //$NON-NLS-1$
	              fileDialog.setText(NLMessages.getString("ImportWizard_file_dialog_import_title"));
	              fileDialog.setFilterExtensions(new String[] {"*.xml"}); //$NON-NLS-1$
	  			String selectedfile = fileDialog.open();
	  			if (selectedfile != null)
	  			{
	  				_filePath.setText(selectedfile);
	  				_filePathString = selectedfile;
	  				_loadFile.setEnabled(true);
	  				loadFile();
	  			}
			}
			}); //SelectionListener
//        new Label(contentComp,SWT.NONE).setText("");
        _loadFile = new Button(_contentComp, SWT.PUSH);
        _loadFile.setText(NLMessages.getString("ImportWizard_laod_file"));
        _loadFile.setEnabled(false);
        _providerLabel = new Label(_contentComp, SWT.NONE);
        if (_importWizard.getImportTypeInt() == 0)
   	 {
            _providerLabel.setText(NLMessages.getString("ImportWizard_provider"));
   	 }
		else if (_importWizard.getImportTypeInt() == 1)
    	 {
         _providerLabel.setText("Value of Genre");
    	 }
        _loadFile.addSelectionListener(new SelectionAdapter()
			{
			public void widgetSelected(final SelectionEvent event)
			{
				loadFile();
			}
			}); //SelectionListener

       _providerText = new Text(_contentComp, SWT.BORDER);
       _providerText.setLayoutData(new GridData());
        ((GridData) _providerText.getLayoutData()).horizontalAlignment = SWT.FILL;
        ((GridData) _providerText.getLayoutData()).grabExcessHorizontalSpace = true;
        if (_importWizard.getImportTypeInt() == 1)
        {
	        new Label(_contentComp, SWT.NONE).setText(NLMessages.getString("ImportWizard_display_name_genre"));
	        _displayNameText = new Text(_contentComp, SWT.BORDER);
	        _displayNameText.setLayoutData(new GridData());
	         ((GridData) _displayNameText.getLayoutData()).horizontalAlignment = SWT.FILL;
	         ((GridData) _displayNameText.getLayoutData()).grabExcessHorizontalSpace = true;
        }
         System.out.println("type " + _importWizard.getImportTypeInt());
         if (_importWizard.getImportTypeInt() == 0)
         {
      	   new Label(_contentComp, SWT.NONE).setText("");
             _asConfigAsDefault = new Button(_contentComp, SWT.CHECK);
             _asConfigAsDefault.setSelection(_configAsDefault);
             _asConfigAsDefault.setLayoutData(new GridData());
             _asConfigAsDefault.setText(NLMessages.getString("ImportWizard_classificatin_default"));
             ((GridData) _asConfigAsDefault.getLayoutData()).horizontalAlignment = SWT.FILL;
             ((GridData) _asConfigAsDefault.getLayoutData()).grabExcessHorizontalSpace = true;
             _asConfigAsDefault.addSelectionListener(new SelectionAdapter() {
          	   public void widgetSelected(final SelectionEvent event)
     			{
          		   _configAsDefault = !_configAsDefault;
     			};
			});
         }
		else
		{
			_asConfigAsDefault = null;
		}
         Point p = this.getControl().getSize();
         _contentComp.computeSize(p.x, p.y);
         _contentComp.redraw();
         _contentComp.layout();
         _composite.redraw();
         _composite.layout();
    }
	protected void loadFile() {if (_importWizard.getImportTypeInt() == 0)
	 {
        _providerLabel.setText(NLMessages.getString("ImportWizard_provider"));

		 File file = new File(_filePathString);
//		 System.out.println("file path " + filePathString); //$NON-NLS-1$
		 SAXParserFactory factory = SAXParserFactory.newInstance();
		 IConfigManager configManager = _facade.getConfigManager();
		 try
		 {

		     InputStream    xmlInput  = new FileInputStream(file);
		     SAXParser      saxParser = factory.newSAXParser();

		     DataDescSaxHandler handler   = new DataDescSaxHandler(configManager);
		     XMLReader reader = saxParser.getXMLReader();
		     try
		     {
		    	  // Turn on validation
		    	 reader.setFeature("http://xml.org/sax/features/validation", true); //$NON-NLS-1$
		    	  // Ensure namespace processing is on (the default)
		    	 reader.setFeature("http://xml.org/sax/features/namespaces", true); //$NON-NLS-1$
		    	}
		     catch (SAXNotRecognizedException e)
		     {
				//		    		    	  System.err.println("Unknown feature specified: " + e.getMessage()); //$NON-NLS-1$
		    	}
		     catch (SAXNotSupportedException e)
		     {
				//		    		    	  System.err.println("Unsupported feature specified: " + e.getMessage()); //$NON-NLS-1$
		    }

		     saxParser.parse(xmlInput, handler);

		 }
		 catch (Throwable err)
		 {
		     err.printStackTrace();
		 }
		 DatatypeDesc dtd = null;
		try
		{
			dtd = configManager.getDatatypeDesc();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		 if (dtd != null && dtd.isValid())
		 {
			 _importWizard.setImportDatatypeDes(dtd);
 		 _providerText.setText(_importWizard.getImportDatatypeDes().getProvider());
 		_providerText.setEditable(false);
 		 if (_displayNameText != null)
 		{
 			 _displayNameText.setText("");
 			_displayNameText.setEditable(false);
 		}
 		 _importWizard.getContainer().updateButtons();
		 }
		 else
		 {
			 _providerText.setText(NLMessages.getString("ImportWizard_error_message_no_valid_configuration"));
		 }

	 }
	else if (_importWizard.getImportTypeInt() == 1)
	 {
    _providerLabel.setText(NLMessages.getString("ImportWizard_code_of_genre"));

		 File file = new File(_filePathString);
//		 System.out.println("file path " + filePathString); //$NON-NLS-1$
		 SAXParserFactory factory = SAXParserFactory.newInstance();
		 try
		 {

		     InputStream    xmlInput  = new FileInputStream(file);
		     SAXParser      saxParser = factory.newSAXParser();

		     ReferenceSaxHandler handler   = new ReferenceSaxHandler();
		     XMLReader reader = saxParser.getXMLReader();
		     try
		     {
		    	  // Turn on validation
		    	 reader.setFeature("http://xml.org/sax/features/validation", true); //$NON-NLS-1$
		    	  // Ensure namespace processing is on (the default)
//		    	 reader.setFeature("http://xml.org/sax/features/namespaces", true); //$NON-NLS-1$
		    	}
		     catch (SAXNotRecognizedException e)
		     {
		    	  System.err.println("Unknown feature specified: " + e.getMessage()); //$NON-NLS-1$
		    	}
		     catch (SAXNotSupportedException e)
		     {
		    	  System.err.println("Unsupported feature specified: " + e.getMessage()); //$NON-NLS-1$
		    }

		     saxParser.parse(xmlInput, handler);
		     if (handler.getResultObject() != null
   				 && handler.getResultObject() instanceof ReferenceModsTemplate)
   		 {
		    	 ReferenceModsTemplate rmt = (ReferenceModsTemplate) handler.getResultObject();
		    	 _importWizard.setImportRefTemplate(rmt);
    		 	_providerText.setText(rmt.getValue());
	    			_providerText.setEditable(false);
	    			_displayNameText.setText(rmt.getLabel());
	    			_displayNameText.setEditable(false);
	    			_importWizard.getContainer().updateButtons();
   		 }
   		 else
   		 {
   			 _providerText.setText(NLMessages.getString("ImportWizard_error_message_no_valid_configuration"));
   		 }
		 }
		 catch (Throwable err)
		 {
		     err.printStackTrace();
		 }
	 }
		
	}
	/** set file path.
	 * @param filePathString file path.
	 */
	public final void setFilePathString(final String filePathString)
	{
		this._filePathString = filePathString;
	}
	/** get file path as string.
	 * @return file path.
	 */
	public final String getFilePathString()
	{
		return _filePathString;
	}
	/** is config set to default.
	 * @return is imported configuration classification set to default.
	 */
	public final boolean isConfigAsDefault()
	{
		return _configAsDefault;
	}
}
