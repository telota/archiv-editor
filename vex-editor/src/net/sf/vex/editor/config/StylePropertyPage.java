/*******************************************************************************
 * Copyright (c) 2004, 2008 John Krasnay and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     John Krasnay - initial API and implementation
 *******************************************************************************/
package net.sf.vex.editor.config;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.sf.vex.editor.VexPlugin;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.PropertyPage;

/**
 * Property page for .css files.
 */
public class StylePropertyPage extends PropertyPage {

    protected Control createContents(Composite parent) {

        pane = new Composite(parent, SWT.NONE);

        createPropertySheet();
        
        configListener = new IConfigListener() {

            public void configChanged( final ConfigEvent e) {
                populateDoctypes();
            }

            public void configLoaded(final ConfigEvent e) {
                setMessage( getTitle() );
                populateStyle();
                setValid(true);
                
                try { // force an incremental build 
                    getPluginProject().writeConfigXml();
                } catch (Exception ex) {
                    String message = MessageFormat.format(
                            Messages.getString("StylePropertyPage.writeError"), //$NON-NLS-1$
                            new Object[] { PluginProject.PLUGIN_XML });
                    VexPlugin.getInstance().log(IStatus.ERROR, message, ex);
                }       

            }
        };

        ConfigRegistry.getInstance().addConfigListener( configListener );


        if( ConfigRegistry.getInstance().isConfigLoaded() ){
        
            populateStyle();  
            populateDoctypes();

        } else {

            setValid(false);
                      
            setMessage(Messages.getString("StylePropertyPage.loading")); //$NON-NLS-1$
            
        }

        return pane;
       }
       
    private void createPropertySheet() {
        
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        pane.setLayout(layout);
        GridData gd;
        
        Label label;
        
        label = new Label(pane, SWT.NONE);
        label.setText(Messages.getString("StylePropertyPage.name")); //$NON-NLS-1$
        this.nameText = new Text(pane, SWT.BORDER);
        gd = new GridData();
        gd.widthHint = NAME_WIDTH;
        this.nameText.setLayoutData(gd);
        
        final String resourcePath = ((IFile) this.getElement()).getProjectRelativePath().toString();

        final ConfigSource config = this.getPluginProject();
        
        this.style = (Style) config.getItemForResource(resourcePath);
        if (this.style == null) {
            this.style = new Style(config);
            this.style.setResourcePath(resourcePath);
            config.addItem(this.style);
        }
        
        // Generate a simple ID for this one if necessary
        if (this.style.getSimpleId() == null || this.style.getSimpleId().length() == 0) {
            this.style.setSimpleId(this.style.generateSimpleId());
        }
        
        label = new Label(pane, SWT.NONE);
        label.setText(Messages.getString("StylePropertyPage.doctypes")); //$NON-NLS-1$
        gd = new GridData();
        gd.horizontalSpan = 2;
        label.setLayoutData(gd);

        final Composite tablePane = new Composite(pane, SWT.BORDER );
        
        gd = new GridData(GridData.FILL_BOTH);
        gd.heightHint = 200;
        gd.horizontalSpan = 2;
        tablePane.setLayoutData(gd);
        
        final FillLayout fillLayout = new FillLayout();
        tablePane.setLayout(fillLayout);
        
        this.doctypesTable = new Table( tablePane, SWT.CHECK );
       
    }

    /**
     * Returns the PluginProject associated with this resource.
     * @return
     */
    public PluginProject getPluginProject() {
        IFile file = (IFile) this.getElement();
        return PluginProject.get(file.getProject());
    }

    public boolean performOk() {
    
        performApply();
        
        return super.performOk();
    }

    public void performApply() {

        this.style.setName(this.nameText.getText());
        
        List doctypeList = ConfigRegistry.getInstance().getAllConfigItems( DocumentType.EXTENSION_POINT);
        Collections.sort( doctypeList );

        final ArrayList selectedDoctypes = new ArrayList();
        final TableItem[] tia = this.doctypesTable.getItems();
        
        for (int i = 0; i < tia.length; i++) {
            if( tia[i].getChecked() ) {
                selectedDoctypes.add( tia[i].getText());
            }
        }

        this.style.removeAllDocumentTypes();

        for (int i = 0; i < doctypeList.size(); i++) {
            if( selectedDoctypes.contains( ((DocumentType)doctypeList.get(i)).getName() ) ) {
                this.style.addDocumentType( ((DocumentType)doctypeList.get(i)).getPublicId() );
            }
        }
  
        try {
            this.getPluginProject().writeConfigXml();
        } catch (Exception e) {
            String message = MessageFormat.format(
                    Messages.getString("StylePropertyPage.writeError"), //$NON-NLS-1$
                    new Object[] { PluginProject.PLUGIN_XML });
            VexPlugin.getInstance().log(IStatus.ERROR, message, e);
        }
        
        ConfigRegistry.getInstance().fireConfigChanged(new ConfigEvent(this));
    }
    
    protected void performDefaults() {

        super.performDefaults();

        populateStyle();

        populateDoctypes();
        
    }

    public void dispose() {
        super.dispose();
        
        if (this.configListener != null) {
            ConfigRegistry.getInstance().removeConfigListener(this.configListener);
        }
    }
    
    //======================================================= PRIVATE

    private Style style;
    private static final int NAME_WIDTH = 150;
    
    private Composite pane;  
    private Text nameText;
    private Table doctypesTable;

    private IConfigListener configListener;
     
    private void populateStyle() {
        this.setText(this.nameText, this.style.getName());
        
    }
    
    private void populateDoctypes() {

        final Set selectedDoctypes = new TreeSet( this.style.getDocumentTypes() );
        doctypesTable.removeAll();
        
        List doctypeList = ConfigRegistry.getInstance().getAllConfigItems( DocumentType.EXTENSION_POINT);
        Collections.sort( doctypeList );
        for (int i = 0; i < doctypeList.size(); i++) {
           
            TableItem item1 = new TableItem( doctypesTable, SWT.NONE );
            item1.setText( ((DocumentType) doctypeList.get(i)).getName() );
            if( selectedDoctypes.contains( ((DocumentType)doctypeList.get(i)).getPublicId() ) ) {
              
                item1.setChecked(true);
            }
        }
    }
    
    private void setText(Text textBox, String s) {
        textBox.setText(s == null ? "" : s); //$NON-NLS-1$
    }

}
