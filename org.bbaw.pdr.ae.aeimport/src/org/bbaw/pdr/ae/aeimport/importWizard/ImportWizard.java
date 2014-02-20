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
/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.bbaw.pdr.ae.aeimport.importWizard;

import javax.xml.stream.XMLStreamException;

import org.bbaw.pdr.ae.common.CommonActivator;
import org.bbaw.pdr.ae.config.core.IConfigManager;
import org.bbaw.pdr.ae.config.model.DatatypeDesc;
import org.bbaw.pdr.ae.control.facade.Facade;
import org.bbaw.pdr.ae.control.interfaces.IDBManager;
import org.bbaw.pdr.ae.model.ReferenceModsTemplate;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.jface.wizard.IWizardContainer;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.IHandlerService;

/** main class of import wizard.
 * @author Christoph Plutte
 *
 */
public class ImportWizard extends Wizard
{
	/** choose type page.*/
	private ImportWizardChooseTypePage _chooseTypePage;
	/** file page.*/
	private ImportWizardFilePage _filePage;
	/** name conflict page.*/
	private ImportWizardProviderConflictPage _conflictPage;
	/** singleton instance of facade.*/
	private Facade _facade = Facade.getInstanz();
	/** import datatypedesc.*/
	private DatatypeDesc _importDatatypeDes;
	/** import ref template.*/
	private ReferenceModsTemplate _importRefTemplate;
	/** import type.*/
	private int _importTypeInt;
	/** is overriding existing name.*/
	private boolean _isOverride;


	/**
	 * add pages to wizard.
	 * @see org.eclipse.jface.wizard.Wizard#addPages()
	 */
	public final void addPages()
	{
    	 _chooseTypePage = new ImportWizardChooseTypePage("Personal Information Page");
              addPage(_chooseTypePage);
              _filePage = new ImportWizardFilePage("File Selection");
              addPage(_filePage);
 			 _conflictPage = new ImportWizardProviderConflictPage("Provider Name Conflict");
			 addPage(_conflictPage);

     }
     /** performFinish.
     * @return boolean finished ok
     * @see org.eclipse.jface.wizard.Wizard#performFinish()
     */
    public final boolean performFinish()
     {
    	 if (_importTypeInt == 0)
    	 {
    		 IConfigManager configManager = _facade.getConfigManager();
    		 try
    		 {
				configManager.saveConfig(_importDatatypeDes);
    		 }
    		 catch (XMLStreamException e)
    		 {
				return false;
			}
    		 catch (Exception e)
    		 {
				e.printStackTrace();
			}
			if (_filePage.isConfigAsDefault())
	    	{
				CommonActivator.getDefault().getPreferenceStore().setValue("PRIMARY_SEMANTIC_PROVIDER", //$NON-NLS-1$
						_importDatatypeDes.getProvider()); //$NON-NLS-1$
				CommonActivator.getDefault().getPreferenceStore().setValue("PRIMARY_TAGGING_PROVIDER", //$NON-NLS-1$
						_importDatatypeDes.getProvider()); //$NON-NLS-1$
				CommonActivator.getDefault().getPreferenceStore().setValue("PRIMARY_RELATION_PROVIDER", //$NON-NLS-1$
						_importDatatypeDes.getProvider());
	    	}
			IHandlerService handlerService = (IHandlerService) PlatformUI.getWorkbench().getService(
					IHandlerService.class);
			try
			{
				handlerService.executeCommand("org.bbaw.pdr.ae.base.commands.RefreshFromDB", null); //$NON-NLS-1$
			}
			catch (ExecutionException e)
			{
				e.printStackTrace();
			}
			catch (NotDefinedException e)
			{
				e.printStackTrace();
			}
			catch (NotEnabledException e)
			{
				e.printStackTrace();
			}
			catch (NotHandledException e)
			{
				e.printStackTrace();
			}
    		return true;
    	 }
    	 if (_importTypeInt == 1)
    	 {
    		 _facade.getReferenceModsTemplates().put(_importRefTemplate.getValue(), _importRefTemplate);
    		 IDBManager dbm = _facade.getDBManager();
    		 try
    		 {
				dbm.saveReferenceTemplateToDB(_facade.getReferenceModsTemplates());
				IHandlerService handlerService = (IHandlerService) PlatformUI.getWorkbench().getService(
						IHandlerService.class);
				try
				{
					handlerService.executeCommand("org.bbaw.pdr.ae.base.commands.RefreshFromDB", null); //$NON-NLS-1$
				}
				catch (ExecutionException e)
				{
					e.printStackTrace();
				}
				catch (NotDefinedException e)
				{
					e.printStackTrace();
				}
				catch (NotEnabledException e)
				{
					e.printStackTrace();
				}
				catch (NotHandledException e)
				{
					e.printStackTrace();
				}
				return true;
			}
    		 catch (Exception e)
    		 {
				e.printStackTrace();
				return false;
			}
    	 }
         return false;
     }
     @Override
	public final boolean canFinish()
     {
    	 if (_importTypeInt == 0)
    	 {
	    	 if (_importDatatypeDes != null && (!_facade.getConfigs().containsKey(_importDatatypeDes.getProvider().toUpperCase())
					 || isOverride()))
			 {
				 return true;
			 }
    	 }
    	 if (_importTypeInt == 1)
    	 {
	    	 if (_importRefTemplate != null && (!_facade.getReferenceModsTemplates().containsKey(_importRefTemplate.getValue())
					 || isOverride()))
			 {
				 return true;
			 }
    	 }
    	return false;
    }
    @Override
	public final IWizardPage getNextPage(final IWizardPage page)
    {
    	if (page instanceof ImportWizardChooseTypePage)
    	{
    		_filePage.loadValues();
    	}
    	if (page instanceof ImportWizardFilePage)
    	{
    	}
    	if (page instanceof ImportWizardProviderConflictPage)
    	{
    	}
    	return super.getNextPage(page);
    }
	/** set import type.
	 * @param importTypeInt import type as int.
	 */
	public final void setImportTypeInt(final int importTypeInt)
	{
		this._importTypeInt = importTypeInt;
	}
	/** get import type.
	 * @return import type as int.
	 */
	public final int getImportTypeInt()
	{
		return _importTypeInt;
	}

    /** get imported data type desc.
     * @return imported data type desc.
     */
    public final DatatypeDesc getImportDatatypeDes()
    {
		return _importDatatypeDes;
	}
	/** set imported data type desc.
	 * @param importDatatypeDes imported datatypedesc.
	 */
	public final void setImportDatatypeDes(final DatatypeDesc importDatatypeDes)
	{
		this._importDatatypeDes = importDatatypeDes;
	}
	/** get imported reference template.
	 * @return imported ref template.
	 */
	public final ReferenceModsTemplate getImportRefTemplate()
	{
		return _importRefTemplate;
	}
	/** set import reference template.
	 * @param importRefTemplate imported ref template.
	 */
	public final void setImportRefTemplate(final ReferenceModsTemplate importRefTemplate)
	{
		this._importRefTemplate = importRefTemplate;
	}
	/** set is overriding existing name.
	 * @param isOverride is overriding existing classification provider name.
	 */
	public final void setOverride(final boolean isOverride)
	{
		this._isOverride = isOverride;
	}
	/** is overriding.
	 * @return isoverride overriding existing name.
	 */
	public final boolean isOverride()
	{
		return _isOverride;
	}
@Override
public final IWizardContainer getContainer()
{
	return super.getContainer();
}
}

