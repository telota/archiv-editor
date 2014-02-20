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

import java.util.HashMap;
import java.util.Vector;

import org.bbaw.pdr.ae.common.AEConstants;
import org.bbaw.pdr.ae.common.CommonActivator;
import org.bbaw.pdr.ae.export.pluggable.AeExportCoreProvider;
import org.bbaw.pdr.ae.export.pluggable.AeExportUtilities;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class Activator extends AbstractUIPlugin {
	
	private static final String extensionPoint = "org.bbaw.pdr.ae.export.core";
	// export utilities singleton
	AeExportCoreProvider coreProvider;
	private ILog log = AEConstants.ILOGGER;

	public Activator() {
		super();
		coreProvider = AeExportCoreProvider.getInstance();
		//provider.loadSettings();
		
		// retrieve extension registry in order to prepare export wizard/utility provider pairs
		IExtensionRegistry extensionRegistry = Platform.getExtensionRegistry();
		
		//FIXME: update file type extension parsing for input and output formats 
		IConfigurationElement[] extensions = extensionRegistry.getConfigurationElementsFor(extensionPoint);
		
		//System.out.println("contributions to "+extensionPoint+":  "+extensions.length);
		
		
		// instantiate export plugin util providers and register them for their
		// corresponding wizard classes
		for (IConfigurationElement cfg : extensions) {
			String providerClass = cfg.getAttribute("class");
			String wizardId = cfg.getAttribute("wizardId");
			if (providerClass != null && wizardId != null) {
				try {
					// instantiate export plugin core
					AeExportUtilities provider = (AeExportUtilities) 
						cfg.createExecutableExtension("class");
					// register file types expected by plugin
					HashMap<String, Vector<String>> filetypes = 
							getFiletypes(cfg, "input");
					coreProvider.setPluginFiletypes(provider.pluginId(), filetypes);
					filetypes = getFiletypes(cfg, "output");
					coreProvider.setPluginFiletypes(provider.pluginId(), filetypes);
					// detect wizard class name
					// register export provider for wizard name
					String wizardClass = getWizardClassname(wizardId);
					//TODO: clean up
					//IWizardDescriptor wizardDsc = wizardRegistry.findWizard(wizardId);
					//IExportWizard wizard = (IExportWizard) wizardDsc.createWizard();
					//System.out.println(wizardClass+" -- "+wizard.getClass().getCanonicalName());
					// register
					coreProvider.registerWizardProvider(wizardClass, provider);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
	
		//TODO: evaluate unused code and clean up!!! 
/*		for (IConfigurationElement cfg : extensions) {
			//System.out.println(" name: " +cfg.getName());
			//System.out.println(" namespace: "+cfg.getNamespaceIdentifier());
//			System.out.println(" attributes: ");
			AeExportUtilities extUtils=null;
			for ( String an : cfg.getAttributeNames()) {
//				System.out.print("  "+an);
//				System.out.println(" : "+cfg.getAttribute(an));
				if (an.equals("class"))
					try {
						// instantiate export plugin's provider class
						// TODO: evtl aber auch nur wenn entspr. wizard gebraucht wird?
						extUtils = (AeExportUtilities) 
								cfg.createExecutableExtension("class");
						//System.out.println("   creating executable extension "
						//					+cfg.getNamespaceIdentifier());
						// assign export core provider instance to plugin
						// extUtils.setCoreProvider(this.provider);
						// save supported filetypes in plugin's settings section
						HashMap<String, Vector<String>> filetypes = 
								getFiletypes(cfg, AeExportCoreProvider.EXT_IN);
						coreProvider.setPluginFiletypes(extUtils.pluginId(), 
										filetypes, AeExportCoreProvider.EXT_IN);
						filetypes = getFiletypes(cfg, AeExportCoreProvider.EXT_OUT);
						coreProvider.setPluginFiletypes(extUtils.pluginId(), 
										filetypes, AeExportCoreProvider.EXT_OUT);
					} catch (Exception e) {
						//System.out.println("error while creating executable " +
						//		"extension: "+cfg.getNamespaceIdentifier());
						e.printStackTrace();
					}
				else if (an.equals("wizardId")) {
					String wizardId = cfg.getAttribute("wizardId");
					//Platform.getExtensionRegistry().get
					//System.out.println("==============="+wizardId+"=============");

					//IExportWizard wizard;
					
					IWizardRegistry wizardRegistry = WorkbenchPlugin.getDefault().getExportWizardRegistry();
					
					IWizardDescriptor wizardDsc = wizardRegistry.findWizard(wizardId);
					
					if (wizardDsc != null) {
						try {
							
							//wizard = (IExportWizard) wizardDsc.createWizard();
							//String classname = wizard.getClass().getCanonicalName();
							String classname = getWizardClassname(wizardId);
							
							//System.out.println("--------------"+classname+"------------");
							if (extUtils != null && classname != null) {
								coreProvider.registerWizardProvider(classname, extUtils);
							}
							
//							Object adapter = wizardDsc.getAdapter(wizard.getClass());
//							System.out.println("is there an object acting as an adapter for this class?");
//							if (adapter != null)
//								System.out.println(" Yes: "+adapter.getClass().getCanonicalName());
//							else
//								System.out.println(" No.");
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		}*/
		AeExportCoreProvider.init();
		log.log(new Status(IStatus.OK, CommonActivator.PLUGIN_ID, 
				"Export plugin Activator set up and ready."));
	}
	
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		log.log(new Status(IStatus.INFO, CommonActivator.PLUGIN_ID, 
				"Export plugin Activator start() method done"));
	}
	
	/**
	 * Extracts all filetype declarations found within given configuration 
	 * element.
	 * @param cfg {@link IConfigurationElement} representing a contribution
	 * to the export core plugin's main {@link #extensionPoint}
	 * @return String[], i.e. {".xml", ".html", ...}
	 */
	private HashMap<String, Vector<String>> getFiletypes(
				IConfigurationElement cfg, String direction) {
		
		HashMap<String, Vector<String>> results = 
				new HashMap<String, Vector<String>>();

		if (!direction.matches("input|output"))
			return results;
		
		IConfigurationElement[] extCfgs = cfg.getChildren("filetypes");
		if (extCfgs.length < 1)
			return results;
		else 
			extCfgs = extCfgs[0].getChildren(direction);
		//System.out.println("number of filetype declarations in contribution");
		//System.out.println(cfg.getNamespaceIdentifier()+": "+extCfgs.length);
		
		for (IConfigurationElement ioCfg : extCfgs) {
			String setName = ioCfg.getAttribute("set");
			setName = (setName != null) ? setName : direction;
			Vector<String> exts = new Vector<String>();
			//System.out.println("Value: "+ext);
			// look for default file name
			try { 
				String default_filename = ioCfg.getChildren(AeExportCoreProvider.DEF_DEF_FN)[0].getAttribute("file");
				
				log.log(new Status(IStatus.INFO, CommonActivator.PLUGIN_ID, 
				" Extract default file name from plugin configs: "+
						default_filename+"; "+setName));
				exts.add(default_filename); // TODO: make absolute
			} catch (Exception e) {};			

			// load supported filetypes
			for (IConfigurationElement extCfg : ioCfg.getChildren("filetype") ) {
				String ext = extCfg.getAttribute("extension");
				//String desc = extCfg.getAttribute("format"); // TODO: are we supposed to handle format field in plugin configuration here?
				if (!ext.startsWith("*."))
					if (!ext.startsWith("."))
						ext = "*."+ext;
					else
						ext = "*"+ext;	
				exts.add(ext);
			}

			results.put(setName, exts);
		}
		return results;
	}
	
	/**
	 * Return the executable class identifier of the extension specified by the
	 * given string.
	 * @param wizardId
	 * @return class name
	 */
	private String getWizardClassname(String wizardId) {
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		for (IConfigurationElement cfg : registry.getConfigurationElementsFor("org.eclipse.ui.exportWizards")) {
			//System.out.println("Looking for wizard class in: "+cfg.getName());
			if (wizardId.equals(cfg.getAttribute("id"))) {
				String classname = cfg.getAttribute("class");
				if (classname != null)
					return classname;
			}
		}
		return null;
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		//provider.saveSettings();
		super.stop(context);
	}

}
