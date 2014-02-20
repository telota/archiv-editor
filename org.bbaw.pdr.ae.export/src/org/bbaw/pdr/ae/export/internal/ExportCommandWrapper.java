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
package org.bbaw.pdr.ae.export.internal;

import java.util.Arrays;
import java.util.Vector;

import org.bbaw.pdr.ae.common.AEPluginIDs;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.internal.dialogs.ImportExportWizard;
import org.eclipse.ui.internal.dialogs.WorkbenchWizardElement;
import org.eclipse.ui.internal.wizards.AbstractExtensionWizardRegistry;
import org.eclipse.ui.wizards.IWizardCategory;
import org.eclipse.ui.wizards.IWizardDescriptor;
import org.eclipse.ui.wizards.IWizardRegistry;

@SuppressWarnings("restriction")
public class ExportCommandWrapper extends AbstractHandler {
	
    private static final int SIZING_WIZARD_WIDTH = 470;
    private static final int SIZING_WIZARD_HEIGHT = 550;
    public static final String sectionId = "AEExportAction";

	/*private final String excludedWizards = 
			"org\\.eclipse\\.ui\\.wizards\\.(import|export)\\.(?!Preferences).*";*/
	/*private static final String wizRegFilter = 
			"(org\\.bbaw\\.pdr\\.ae\\.export\\..*|org\\.eclipse\\.ui\\.wizards\\.export\\..*)";*/
	private static final String wizRegFilter = 
			"org\\.bbaw\\.pdr\\.ae\\.export\\..*";
    
	private IWizardRegistry _registry;
	
	/**
	 * constructor filters wizard registry
	 */
	public ExportCommandWrapper() {
		// retrieve wizard registry. wizards will be filtered for export wizards and possibly be unregistered.
    	_registry = 
    			WorkbenchPlugin.getDefault().getExportWizardRegistry();
//   	System.out.println("export wizard registry root: "+wizardRegistry.getRootCategory().getId());
//    	System.out.println("export wizard registry cat: "+wizardRegistry.getRootCategory().getCategories().length);
//    	getAllWizards(new IWizardCategory[]{wizardRegistry.getRootCategory().getCategories()});
    	
//    	IWizardCategory[] cats = wizardRegistry.getRootCategory().getCategories();
//    	traverseCategories(cats);
    	
    	// remove all wizards from registry that are not coming with an export plugin.
    	filterWizardRegistry(wizRegFilter, _registry.getRootCategory());		
	}
    
	@Override
	/**
	 * opens export wizard selection dialog
	 */
    public Object execute(ExecutionEvent event) throws ExecutionException {
    	//System.out.println();
//    	System.out.println("handle export wizard command.");
    	
    	ImportExportWizard wizard = null;
    	
    	/*String wizardId = event.getParameter(
    			IWorkbenchCommandConstants.FILE_EXPORT_PARM_WIZARDID);
    	System.out.println("wizardId: "+wizardId);*/
    	
    	IWorkbench workbench = PlatformUI.getWorkbench();
    	IWorkbenchWindow activeWindow = workbench.getActiveWorkbenchWindow();
//    	System.out.println("active window: "+activeWindow.toString());
    	
    	// fetch wizard descriptor for the wizard identified by the current event
    	// from export wizard registry and use it to create the wizard and a dialog for it
    	// TODO: this makes no sense, weil der wizard eigentlich nur der standard
    	// export-auswahl-wizard sein soll. die eigentlich wizards kommen erst irgendwie spaeter
    	/*IWizardDescriptor wizardDescriptor = _registry.findWizard(wizardId);
    	if (wizardDescriptor != null) {
    		System.out.println("descriptor: "+wizardDescriptor.getDescription());
    		try {
    			wizard = (ImportExportWizard) wizardDescriptor.createWizard();
        		// TODO: why selection?
        		IStructuredSelection selection = 
        				(IStructuredSelection) PlatformUI.getWorkbench().
        				getActiveWorkbenchWindow()    			
        				.getSelectionService().
        				getSelection(AEPluginIDs.VIEW_TREEVIEW);    			
    			wizard.init(workbench, selection);
    		} catch (Exception e) {
				System.out.println("ERROR IN CREATING WIZARD FROM DESCRIPTION");
				e.printStackTrace();
			}
    	}
    	else {*/
//    		System.out.println("NO DESCRIPTOR FOUND!");
		wizard = new ImportExportWizard(
				ImportExportWizard.EXPORT);
		// TODO: why selection?
		IStructuredSelection selection = (IStructuredSelection) PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getSelectionService().getSelection(AEPluginIDs.VIEW_TREEVIEW);
		wizard.init(workbench, selection);
    	//}
    	
    	// restore default export wizard's dialog setting from last time
		IDialogSettings workbenchSettings = WorkbenchPlugin.getDefault()
				.getDialogSettings();
		IDialogSettings wizardSettings = workbenchSettings
				.getSection(sectionId); //$NON-NLS-1$
		if (wizardSettings == null) {
			wizardSettings = workbenchSettings
					.addNewSection(sectionId); //$NON-NLS-1$
//			System.out.println("DialogSettings not found: ImportExportAction");
		} /*else {
//			System.out.println("DialogSettings found: "+wizardSettings.toString());
			IDialogSettings[] sections = wizardSettings.getSections();
			for (IDialogSettings section : sections){
				System.out.println(section.toString());
			}
		}*/
		wizard.setDialogSettings(wizardSettings);
		wizard.setForcePreviousAndNextButtons(true);
		
//		System.out.println("creating wizard dialog");
		WizardDialog dialog = new WizardDialog(activeWindow.getShell(), wizard);

		// this is the dialog from which we choose our export wizard.
		dialog.create();
		dialog.getShell().setSize(
				Math.max(SIZING_WIZARD_WIDTH, dialog.getShell()
						.getSize().x), SIZING_WIZARD_HEIGHT);
		
//		System.out.println("open wizard dialog: return code: "+dialog.getReturnCode() );
		dialog.open();
		
		return null;
    }
    
    /**
     * simply lists all wizards and subcategories for this category recursively
     * @param cats
     */
    /*private void traverseCategories(IWizardCategory[] cats){
    	for (IWizardCategory cat : cats) { 
    		System.out.println("Category: "+cat.getId());
    		IWizardCategory[] subcats = cat.getCategories(); 
			System.out.println("# Found subcategories: "+ subcats.length);
			traverseCategories(subcats);
			IWizardDescriptor[] wizards = cat.getWizards();
			System.out.println("# Found wizards: "+ wizards.length);
			for (IWizardDescriptor wd : wizards) {
				System.out.println("w: "+wd.getId());
			}
    	}    	
    }*/

    /**
     * Filters the current {@link WorkbenchPlugin#getExportWizardRegistry() } by removing all {@link IWizardDescriptor}s
     * in {@link IWizardCategory} <code>category</code> without an <code>Id</code> matching the regular expression 
     * <code>regex</code>. When the predefined
     * regex {@link #wizRegFilter} is being passed, the remaining wizard registry is limited to export wizards
     * whose identifiers conform typical AE export namespaces:
     * <blockquote>org.bbaw.pdr.ae.export.*</blockquote>
     * <p>The regex filter is applied via {@link String#matches(String)}</p>
     * @param regex <code>String</code> containing a regular expression defining allowed wizards
     * @param category {@link IWizardCategory} specifying the category for which the wizards are to be filtered. Most likely 
     * {@link IWizardRegistry#getRootCategory()}
     * @see IWizardDescriptor#getId()
     * @see AbstractExtensionWizardRegistry#removeExtension(org.eclipse.core.runtime.IExtension, Object[])
     * @see WorkbenchWizardElement
     */
    private void filterWizardRegistry(String regex, IWizardCategory category) {
    	// get global export wizard registry from rcp 
    	AbstractExtensionWizardRegistry wizardRegistry = (AbstractExtensionWizardRegistry)WorkbenchPlugin.
    			getDefault().getExportWizardRegistry();
    	// descend into sub categories of given wizard category parameter
    	IWizardCategory[] categories = category.getCategories();
    	// remove wizards defined by extensions with ids not matching the given regular expression
    	for(IWizardDescriptor wizard : getAllWizards(categories)){
    	  if(!wizard.getId().matches(regex)){
    	    WorkbenchWizardElement wizardElement = (WorkbenchWizardElement) wizard;
    	    wizardRegistry.removeExtension(
    	    		wizardElement.getConfigurationElement().getDeclaringExtension(),
    	    		new Object[]{wizardElement});
    	  }
    	}    	
    }

		

	/**
	 * Returns a structured selection based on the event to initialize the
	 * wizard with.
	 * @param event the event object containing information about the current state of the application
	 * @return the current structured selection of the application
	 */
//	protected IStructuredSelection getSelectionToUse(ExecutionEvent event) {
//		ISelection selection = HandlerUtil.getCurrentSelection(event);
//		System.out.println("selection to use: "+ selection.toString());
//		if (selection instanceof IStructuredSelection) {
//			return (IStructuredSelection) selection;
//		}
//		return StructuredSelection.EMPTY;
//	}
		
	
	
	/**
	 * Returns an array of all {@link IWizardDescriptor}s that can be found
	 * in the given {@link IWizardCategory} array.
	 * @param categories
	 * @return
	 */
	private Vector<IWizardDescriptor> getAllWizards(IWizardCategory[] categories) {
		  Vector<IWizardDescriptor> results = new Vector<IWizardDescriptor>();
		  for(IWizardCategory wizardCategory : categories){
//			  System.out.println("wiz cat: "+wizardCategory.getId()+" "+wizardCategory.getLabel());
			  results.addAll(Arrays.asList(wizardCategory.getWizards()));
			  results.addAll(getAllWizards(wizardCategory.getCategories()));
		  }
//		  System.out.println("# wizards: "+results.size());
		  return results;//.toArray(new IWizardDescriptor[results.size()]);
	}



}
