///*******************************************************************************
// * Copyright (c) 2004, 2008 John Krasnay and others.
// * All rights reserved. This program and the accompanying materials
// * are made available under the terms of the Eclipse Public License v1.0
// * which accompanies this distribution, and is available at
// * http://www.eclipse.org/legal/epl-v10.html
// * 
// * Contributors:
// *     John Krasnay - initial API and implementation
// *******************************************************************************/
//package net.sf.vex.editor.config;
//
//import java.io.ByteArrayInputStream;
//import java.io.ByteArrayOutputStream;
//import java.io.PrintStream;
//
//import net.sf.vex.editor.VexPlugin;
//
//import org.eclipse.core.resources.IFile;
//import org.eclipse.core.resources.IProject;
//import org.eclipse.core.resources.IProjectDescription;
//import org.eclipse.core.runtime.CoreException;
//import org.eclipse.core.runtime.IStatus;
//import org.eclipse.jface.viewers.IStructuredSelection;
//import org.eclipse.ui.IWorkbench;
//import org.eclipse.ui.ide.IDE;
//import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;
//
///**
// * Wizard for creating a new Vex Plugin Project.
// */
//public class NewPluginProjectWizard extends BasicNewProjectResourceWizard {
//
//    public void init(IWorkbench workbench, IStructuredSelection currentSelection) {
//        super.init(workbench, currentSelection);
//
//        this.setWindowTitle(Messages.getString("NewPluginProjectWizard.title")); //$NON-NLS-1$
//    }
//    
//    public boolean performFinish() {
//        
//        boolean success = super.performFinish();
//        if (success) {
//            try {
//                this.createVexPluginXml();
//                this.registerVexPluginNature();
//                PluginProject.load(this.getNewProject());
//            } catch (CoreException e) {
//                VexPlugin.getInstance().log(IStatus.ERROR, Messages.getString("NewPluginProjectWizard.createError"), e); //$NON-NLS-1$
//                success = false;
//            }
//            
//            
//        }
//        
//        return success;
//    }
//    
//    //====================================================== PRIVATE
//    
//    private void createVexPluginXml() throws CoreException {
//
//        IProject project = this.getNewProject();
//        
//        ByteArrayOutputStream baos;
//        PrintStream out;
//
//        baos = new ByteArrayOutputStream();
//        out = new PrintStream(baos);
//
//        out.println("<?xml version='1.0'?>"); //$NON-NLS-1$
//        out.println("<plugin>"); //$NON-NLS-1$
//        out.println("</plugin>"); //$NON-NLS-1$
//        out.close();
//        
//        IFile pluginXml = project.getFile(PluginProject.PLUGIN_XML);
//        pluginXml.create(new ByteArrayInputStream(baos.toByteArray()), true, null);
//        
//        // By default open the Default Text Editor for vex-plugin.xml.
//        // This isn't perfect, because the Vex icon is still shown, but
//        // it'll do until we create a custom editor.
//        IDE.setDefaultEditor(pluginXml, "org.eclipse.ui.DefaultTextEditor"); //$NON-NLS-1$
//    }
//    
//    private void registerVexPluginNature() throws CoreException {
//        IProject project = this.getNewProject();
//        IProjectDescription description = project.getDescription();
//        String[] natures = description.getNatureIds();
//        String[] newNatures = new String[natures.length + 1];
//        System.arraycopy(natures, 0, newNatures, 0, natures.length);
//        newNatures[natures.length] = PluginProjectNature.ID;
//        description.setNatureIds(newNatures);
//        project.setDescription(description, null);
//    }
// }
