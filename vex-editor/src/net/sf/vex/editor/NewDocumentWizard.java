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
//package net.sf.vex.editor;
//
//import java.io.ByteArrayInputStream;
//import java.io.ByteArrayOutputStream;
//import java.text.MessageFormat;
//
//import net.sf.vex.dom.Document;
//import net.sf.vex.dom.DocumentWriter;
//import net.sf.vex.dom.RootElement;
//import net.sf.vex.editor.config.Style;
//import net.sf.vex.widget.CssWhitespacePolicy;
//
//import org.eclipse.core.resources.IFile;
//import org.eclipse.core.runtime.IStatus;
//import org.eclipse.jface.dialogs.MessageDialog;
//import org.eclipse.jface.viewers.IStructuredSelection;
//import org.eclipse.ui.IEditorDescriptor;
//import org.eclipse.ui.IEditorRegistry;
//import org.eclipse.ui.IFileEditorMapping;
//import org.eclipse.ui.IWorkbench;
//import org.eclipse.ui.IWorkbenchPage;
//import org.eclipse.ui.IWorkbenchWindow;
//import org.eclipse.ui.PlatformUI;
//import org.eclipse.ui.ide.IDE;
//import org.eclipse.ui.internal.registry.EditorDescriptor;
//import org.eclipse.ui.internal.registry.EditorRegistry;
//import org.eclipse.ui.internal.registry.FileEditorMapping;
//import org.eclipse.ui.wizards.newresource.BasicNewResourceWizard;
//
///**
// * Wizard for creating a new Vex document.
// */
//public class NewDocumentWizard extends BasicNewResourceWizard {
//
//    public void addPages() {
//        this.typePage = new DocumentTypeSelectionPage();
//        this.filePage = new DocumentFileCreationPage("filePage", this.getSelection()); //$NON-NLS-1$
//        addPage(typePage);
//        addPage(filePage);
//    }
//
//    public void init(
//        IWorkbench workbench,
//        IStructuredSelection currentSelection) {
//
//        super.init(workbench, currentSelection);
//        this.setWindowTitle(Messages.getString("NewDocumentWizard.title")); //$NON-NLS-1$
//    }
//
//    public boolean performFinish() {
//        try {
//            RootElement root = new RootElement(this.typePage.getRootElementName());
//            Document doc = new Document(root);
//            doc.setPublicID(this.typePage.getDocumentType().getPublicId());
//            doc.setSystemID(this.typePage.getDocumentType().getSystemId());
//            
//            
//            Style style = VexEditor.findStyleForDoctype(doc.getPublicID());
//            if (style == null) {
//                MessageDialog.openError(this.getShell(), Messages.getString("NewDocumentWizard.noStyles.title"), Messages.getString("NewDocumentWizard.noStyles.message")); //$NON-NLS-1$ //$NON-NLS-2$
//                return false;
//                // TODO: don't allow selection of types with no stylesheets
//            }
//            
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            DocumentWriter writer = new DocumentWriter();
//            writer.setWhitespacePolicy(new CssWhitespacePolicy(style.getStyleSheet()));
//            writer.write(doc, baos);
//            baos.close();
//            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
//            
//            filePage.setInitialContents(bais);
//            IFile file = filePage.createNewFile();
//            IDE.setDefaultEditor(file, VexEditor.ID);
//            this.selectAndReveal(file);
//
//            registerEditorForFilename("*." + file.getFileExtension(), VexEditor.ID); //$NON-NLS-1$
//            
//            // Open editor on new file.
//            IWorkbenchWindow dw = getWorkbench().getActiveWorkbenchWindow();
//            if (dw != null) {
//                IWorkbenchPage page = dw.getActivePage();
//                if (page != null) {
//                    IDE.openEditor(page, file, true);
//                }
//            }
//
//            this.typePage.saveSettings();
//
//            return true;
//            
//        } catch (Exception ex) {
//            String message = MessageFormat.format(
//                    Messages.getString("NewDocumentWizard.errorLoading.message"),
//                    new Object[] { filePage.getFileName(), ex.getMessage() });
//            VexPlugin.getInstance().log(IStatus.ERROR, message, ex);
//            MessageDialog.openError(this.getShell(), Messages.getString("NewDocumentWizard.errorLoading.title"), "Unable to create " + filePage.getFileName()); //$NON-NLS-1$ //$NON-NLS-2$
//            return false;
//        }
//    }
//    
//    //=========================================================== PRIVATE
//    
//    private DocumentTypeSelectionPage typePage;
//    private DocumentFileCreationPage filePage;
//    
//    
//    /**
//     * Register an editor to use for files with the given filename.
//     * 
//     * NOTE: this method uses internal, undocumented Eclipse functionality.
//     * It may therefore break in a future version of Eclipse.
//     * 
//     * @param fileName Filename to be registered. Use the form "*.ext" to register
//     * all files with a given extension.
//     * @param editorId ID of the editor to use for the given filename.
//     */
//    private static void registerEditorForFilename(String fileName, String editorId) {
//        
//        EditorDescriptor ed = getEditorDescriptor(editorId);
//        if (ed == null) {
//            return;
//        }
//        
//        IEditorRegistry reg = PlatformUI.getWorkbench().getEditorRegistry();
//        EditorRegistry ereg = (EditorRegistry) reg;
//        FileEditorMapping[] mappings = (FileEditorMapping[]) ereg.getFileEditorMappings();
//        FileEditorMapping mapping = null;
//        for (int i = 0; i < mappings.length; i++) {
//            IFileEditorMapping fem = mappings[i];
//            if (fem.getLabel().equals(fileName)) {
//                mapping = (FileEditorMapping) fem;
//                break;
//            }
//        }
//        
//        if (mapping != null) {
//            // found mapping for fileName
//            // make sure it includes our editor
//            IEditorDescriptor[] editors = mapping.getEditors();
//            for (int i = 0; i < editors.length; i++) {
//                if (editors[i].getId().equals(editorId)) {
//                    // already mapped
//                    return;
//                }
//            }
//            
//            // editor not in the list, so add it
//            mapping.addEditor(ed);
//            ereg.setFileEditorMappings(mappings);
//            ereg.saveAssociations();
//
//        } else {
//            // no mapping found for the filename
//            // let's add one
//            String name = null;
//            String ext = null;
//            int iDot = fileName.lastIndexOf('.');
//            if (iDot == -1) {
//                name = fileName;
//            } else {
//                name = fileName.substring(0, iDot);
//                ext = fileName.substring(iDot + 1);
//            }
//            
//            mapping = new FileEditorMapping(name, ext);
//            FileEditorMapping[] newMappings = new FileEditorMapping[mappings.length + 1];
//            mapping.addEditor(ed);
//
//            System.arraycopy(mappings, 0, newMappings, 0, mappings.length);
//            newMappings[mappings.length] = mapping;
//            ereg.setFileEditorMappings(newMappings);
//            ereg.saveAssociations();
//        }
//        
//    }
//    
//    /**
//     * Return the IEditorDescriptor for the given editor ID.
//     */
//    private static EditorDescriptor getEditorDescriptor(String editorId) {
//		EditorRegistry reg = (EditorRegistry) PlatformUI.getWorkbench().getEditorRegistry();
//		IEditorDescriptor[] editors = reg.getSortedEditorsFromPlugins();
//		for (int i = 0; i < editors.length; i++) {
//		    if (editors[i].getId().equals(editorId)) {
//		        return (EditorDescriptor) editors[i];
//		    }
//		}
//		return null;
//    }
// }
