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

import java.util.Collections;
import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.part.ViewPart;

/**
 * View showing all configuration items defined in Vex.
 */
public class ConfigurationView extends ViewPart {

    public void createPartControl(Composite parent) {
    
        this.parentControl = parent;
        
        ConfigRegistry registry = ConfigRegistry.getInstance();
        
        registry.addConfigListener(this.configListener);
        
        if (registry.isConfigLoaded()) {
            this.createTreeViewer();
        } else {
            this.loadingLabel = new Label(parent, SWT.NONE);
            this.loadingLabel.setText(Messages.getString("ConfigurationView.loading")); //$NON-NLS-1$
        }

    }

    public void dispose() {
        super.dispose();
        ConfigRegistry.getInstance().removeConfigListener(this.configListener);
    }

    public void setFocus() {
        if (this.treeViewer != null) {
            this.treeViewer.getTree().setFocus();
        }
    }

    //===================================================== PRIVATE
    
    private Composite parentControl;
    
    private Label loadingLabel;
    
    private TreeViewer treeViewer;
    
    private void createTreeViewer() {
        this.treeViewer = new TreeViewer(this.parentControl, SWT.SINGLE);
        this.treeViewer.setContentProvider(new ContentProvider());
        this.treeViewer.setLabelProvider(new MyLabelProvider());
        this.treeViewer.setAutoExpandLevel(2);
        this.treeViewer.setInput(ConfigRegistry.getInstance());
    }
    
    private static class ContentProvider implements ITreeContentProvider {

        public Object[] getChildren(Object parentElement) {
            if (parentElement instanceof IConfigItemFactory) {
                IConfigItemFactory factory = (IConfigItemFactory) parentElement;
                List items = ConfigRegistry.getInstance().getAllConfigItems(factory.getExtensionPointId());
                Collections.sort(items);
                return items.toArray();
            } else {
                return null;
            }
        }

        public Object getParent(Object element) {
            if (element instanceof ConfigItem) {
                ConfigItem item = (ConfigItem) element;
                return ConfigRegistry.getInstance().getConfigItemFactory(item.getExtensionPointId());
            } else {
                return ConfigRegistry.getInstance();
            }
        }

        public boolean hasChildren(Object element) {
            return element instanceof IConfigItemFactory;
        }

        public Object[] getElements(Object inputElement) {
            return ConfigRegistry.getInstance().getAllConfigItemFactories();
        }

        public void dispose() {
        }

        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        }
        
    }
    
    private static class MyLabelProvider extends LabelProvider {
        
        public String getText(Object element) {
            if (element instanceof IConfigItemFactory) {
                return ((IConfigItemFactory) element).getPluralName();
            } else {
                return ((ConfigItem) element).getName();
            }
        }
    }

    private IConfigListener configListener = new IConfigListener() {
        public void configChanged(ConfigEvent e) {
            treeViewer.refresh();
        }
        
        public void configLoaded(ConfigEvent e) {
            loadingLabel.dispose();
            createTreeViewer();
            parentControl.layout();
        }
    };
}
