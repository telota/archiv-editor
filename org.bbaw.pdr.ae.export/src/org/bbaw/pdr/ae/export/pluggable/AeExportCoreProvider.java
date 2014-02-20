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
package org.bbaw.pdr.ae.export.pluggable;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Vector;

import org.bbaw.pdr.ae.common.AEConstants;
import org.bbaw.pdr.ae.common.AEPluginIDs;
import org.bbaw.pdr.ae.common.CommonActivator;
import org.bbaw.pdr.ae.config.model.ConfigData;
import org.bbaw.pdr.ae.control.comparator.AspectsByCronComparator;
import org.bbaw.pdr.ae.control.facade.Facade;
import org.bbaw.pdr.ae.export.internal.Activator;
import org.bbaw.pdr.ae.export.internal.DialogSettingsRegistry;
import org.bbaw.pdr.ae.export.internal.ExportCommandWrapper;
import org.bbaw.pdr.ae.export.logic.PdrObjectsPreviewStructure;
import org.bbaw.pdr.ae.export.swt.IPdrWidgetStructure;
import org.bbaw.pdr.ae.export.swt.preview.PdrSelectionFilterPreview;
import org.bbaw.pdr.ae.model.PdrObject;
import org.bbaw.pdr.ae.model.Person;
import org.bbaw.pdr.ae.model.view.TreeNode;
import org.bbaw.pdr.ae.view.control.PDRObjectsProvider;
import org.bbaw.pdr.ae.view.control.PDROrdererFactory;
import org.bbaw.pdr.ae.view.control.orderer.AspectsBySemanticOrderer;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IExportWizard;
import org.eclipse.ui.PlatformUI;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

//TODO: doc
/**
 * <p>A Singleton class that aims to having covered as many generic export-related 
 * features as possible, so as to spare contributing plugins the trouble of implementing
 * functionality of general interest themselves.</p>
 * <p>Services made available include:
 * <ul><li>Provisioning of the <code>PDR</code> model classes and providers</li>
 * <li>SWT-based representations of data model stuff for GUI integration</li>
 * <li>Sharing resources of general interest, like menu icons and other 
 * I/O stuff</li>
 * <li>Making sure the two main components of each plugin contributing to the
 * export extension-point, implementations of <code>IExportWizard</code> and 
 * <code>IAeUtilityProvider</code> respectively, know each other and have mutual
 * access on their currently relevant instantiations</li></ul>
 * @author jhoeper
 * @see AeExportUtilities
 */
public class AeExportCoreProvider {
	
	public static final String pluginId = 
			FrameworkUtil.getBundle(AeExportCoreProvider.class).getLocation();
	private static AeExportCoreProvider instance;
	
	// right now, these constants are used by label providers, who have faster access the way it is
	public static final Image ICON_PDR_PERSON = loadImage("person.png");
	public static final Image ICON_PDR_ASPECT = loadImage("aspect.png");
	/*public static final Image ICON_PDR_ASPECT_NORMNAME = loadImage("classification_name_norm.png");
	public static final Image ICON_PDR_REFERENCE = loadImage("reference.png");*/
	
	/*public static final Image ICON_CLASSIFICATION = loadImage("classification.png");
	public static final Image ICON_TIME = loadImage("time.png");
	public static final Image ICON_REFERENCES = loadImage("references.png");
	public static final Image ICON_RELATION = loadImage("relation.png");
	public static final Image ICON_USER = loadImage("user.png");
	public static final Image ICON_PLACE = loadImage("place.png");
	public static final Image ICON_MARKUP = loadImage("markup.png");*/
	
	// TODO: should be moved to methods. references can change on runtime
	public static final String PRIMARY_SEMANTIC_PROVIDER = Platform
			.getPreferencesService()
			.getString(CommonActivator.PLUGIN_ID, "PRIMARY_SEMANTIC_PROVIDER",
					AEConstants.CLASSIFICATION_AUTHORITY, null).toUpperCase();
	public static final String PRIMARY_TAGGING_PROVIDER = Platform
			.getPreferencesService()
			.getString(CommonActivator.PLUGIN_ID, "PRIMARY_TAGGING_PROVIDER",
					AEConstants.CLASSIFICATION_AUTHORITY, null).toUpperCase();
	
	// maps wizard class names to their export utility providers
	private HashMap<String, AeExportUtilities> utilityProviders;
	// keeps a list of registered widgets for every pluginId
	protected HashMap<String, Vector<IPdrWidgetStructure>> pluginWidgetRegistry;
	
	public static final String SETTINGS_SAVE_DIR = AEConstants.AE_HOME;
	public static final String SETTINGS_SAVE_FN = "export";

	public static final String DEF_DIR = "directory";
	public static final String DEF_FILE = "filename";
	public static final String DEF_HIST= "recent";
	public static final String DEF_TIT = "windowtitle";
	public static final String DEF_SET = "default";
	public static final String DEF_DEF_FN = "default";

	public static final String EXT_OUT="output";
	public static final String EXT_IN="input";
	
	private DialogSettingsRegistry dialogSettingsRegistry;
	
	// Connectors to core interfaces for PDR content retrieval 
	private PDRObjectsProvider pdrObjectsProvider;
	private PDROrdererFactory pdrOrdererFactory;
	private PdrObjectsPreviewStructure pdrObjectsTree;
	
	private static Facade _facade = Facade.getInstanz();;

	
	
	/**
	 * Sets up core functionality for export plugins. Export plugins for the very
	 * export modes themselves are registered in here and get storage space for
	 * plugin-specific settings.
	 */
	private AeExportCoreProvider() {
		System.out.println(" PDR EXPORT core initializing");
		this.utilityProviders = new HashMap<String, AeExportUtilities>();
		System.out.println("AE Export Core pluginId: "+pluginId);
		dialogSettingsRegistry = new DialogSettingsRegistry();
		pluginWidgetRegistry = new HashMap<String, Vector<IPdrWidgetStructure>>(); //TODO
		// TODO: das ding jetzt zu instalziieren, wenn der drops schon gelutscht ist,
		// TODO bringt uns eigentlich auch nicht weiter. wir müssen eigentlich die komplette
		// TODO belegschaft des personenbaums schon haben, wenn darin einzelne personen
		// fuer den export ausgewählt werden. wenn der personenbaum fertig ist,
		// müßten wir eigentlich da hin und uns dessen objectsprovider abholen.
		// da wrappen wir dann schön die objectspreviewstructure drumrum, damit
		// wir zumindest für die dauer des exports keinen objectsprovider mehr brauchen.
		// wir holen uns dann alle objekte aus der previewstruktur, was deutlich schneller
		// gehen sollte als jedesmal die datenbank anzurufen wenn die export-vorschau
		// sortiert wird oder so.
		
		// TODO: genau überlegen, wann wir uns welchen objectsprovider kopieren können udn
		// was zu dem zeitpunkt da drin sein muß. und ob die previewstruktur es packt, ein 
		// komplettes pdr aufzunehmen.
		pdrObjectsProvider=new PDRObjectsProvider();
		//Collection<Person> people = _facade.getAllPersons().values(); // könnte klappen..?
		//pdrObjectsProvider.setInput(people.toArray(new Person[people.size()]));
		pdrObjectsProvider.setInput(_facade.getCurrentTreeObjects());
		pdrObjectsProvider.setOrderer(PDROrdererFactory.createAspectOrderer(PDROrdererFactory.ORDERER_IDs[1]));
		System.out.println(" PDR EXPORT core set up");
	}
	
	/**
	 * Returns the only existent instance of this class. Singleton style.
	 * @return Not just any, but the one and only <code>AeExportCoreProvider
	 * </code> instance in the world!
	 */
	public static AeExportCoreProvider getInstance() {
		if (instance != null)
			return instance;
		instance = new AeExportCoreProvider();
		return instance;
	}
	
	/** 
	 * Set up central instance.
	 */
	public static void init() {
		getInstance();
	}

	/**
	 * <p>This method is intended to be called by this plugin's {@link Activator}
	 * (or {@link ExportCommandWrapper})
	 * exclusively in order to keep track of the relationship between the 
	 * {@link IExportWizard} wizard 
	 * and the {@link AeExportUtilities} implementation dedicated
	 * to provide for it with essential functionalities required by the underlying 
	 * export mechanism.</p> 
	 * @param wizard
	 * @param provider
	 */
	public void registerWizardProvider(IExportWizard wizard, AeExportUtilities provider) {
		String classname = wizard.getClass().getCanonicalName();
		provider.setWizard(wizard);
		registerWizardProvider(classname, provider);
//		System.out.println("registering utilities for wizard class: "+classname);
//		System.out.println(": "+provider.getClass().getCanonicalName());
	}
	
	/**
	 * Registers an {@link AeExportUtilities} instance (the executable item of any
	 * export plugin) as being responsible for any {@link IExportWizard} whose
	 * class name is the one passed here.
	 * @param classname
	 * @param provider
	 */
	public void registerWizardProvider(String classname, AeExportUtilities provider) {
		this.utilityProviders.put(classname, provider);
		System.out.println(classname + " references " + provider.getClass().getCanonicalName());
	}

	// TODO: what to do in the unlikely case of multiple export extension
	// all naming the same export wizard extension????? 
	/**
	 * <p>Looks up the <code>IAeExportUtilities</code> corresponding to the named
	 * <code>IExportWizard</code> as defined in the extension contributing
	 * to the extension point <code>org.bbaw.pdr.ae.export</code>.</p>
	 * <p>Each contribution to <code>org.bbaw.pdr.ae.export</code> must name
	 * the <code>id</code> of an <code>org.eclipse.ui.exportWizard</code> 
	 * extension. Thus, the class which implements <code>IExportWizard</code>
	 * for that extension can conversely access the utility provider of the
	 * extension which declared itself responsible.</p>
	 * <p>Retrieving the utility provider from inside an class implementing
	 * <code>IExportWizard</code> should be as easy as 
	 * <code>AeExportProvider.getInstance().getUtilityProvider(<i>this</i>)
	 * </code></p>
	 * <p>Unless already done, this method informs the respective utility 
	 * provider of the call, in order to have it knowing the very instance of 
	 * <code>IExportWizard</code> which seems to represent the complementing
	 * export wizard right now.</p>
	 *   
	 * @param wizard The <code>IWizard</code> instance for which the 
	 * responsible utility provider shall be requested
	 * @return an instance of the implementation of <code>IAeExportUtilities</code>
	 * that has been declared as responsible for this wizard extension by an
	 * extension for <code>org.bbaw.pdr.ae.export</code>
	 * 
	 * @see AeExportUtilities
	 */
	public AeExportUtilities getWizardProvider(IWizard wizard) {
		String classname = wizard.getClass().getCanonicalName();
//		System.out.println("return utility provider for wizard class "+classname);
		AeExportUtilities provider = utilityProviders.get(classname);
		if (provider != null) {
			System.out.println(provider.getClass().getCanonicalName());
		}
		return provider;
	}
	
/*	*//**
	 * <p>Creates an <code>XMLContainer</code> class containing the DOM
	 * representation of XML dumps for the named list of 
	 * <code>PdrObject</code>s. </p>
	 * 
	 * @param objects an array of PDR Objects
	 * @return <code>XMLContainer</code> containing the XML representation
	 * of the named PDR objects
	 * @see PdrObject
	 * @see XMLContainer
	 *//*
	public XMLContainer getXML(PdrObject[] objects) {
		return new XMLContainer(objects);
	}*/
	
	/**
	 * Returns the {@link PDRObjectsProvider} object all Export Plugins
	 * are supposed to work with.
	 * @return central {@link PDRObjectsProvider} instance
	 */
	public PDRObjectsProvider getPdrObjectsProvider() {
		if (pdrObjectsProvider == null) {
			pdrObjectsProvider = new PDRObjectsProvider();
			// TODO: wir wollen einen provider, der alle bekannten objekte enthält und auf dessen grundlage
			// dann ein light-provider aus der previewfilterselectionstructure zusammengeschustert werden kann.
			// d.h. wir erben wahrscheinlich vom objectsprovider und simulieren den mit der structure,
			// so daß wir den echten nur noch alle jubeljahre mal aktualisieren müssen. das ist sonst echt
			// yu langsam alles
			pdrObjectsProvider.setInput(_facade.getCurrentTreeObjects());
			pdrObjectsProvider.setOrderer(PDROrdererFactory.createAspectOrderer(PDROrdererFactory.ORDERER_IDs[1]));
			pdrObjectsProvider.setComparator(new AspectsByCronComparator(true));
			pdrObjectsProvider.setLazySorting(true);
			//System.out.println("Created PDRObjectsProvider");
		}
		return pdrObjectsProvider;
	}
	
	/**
	 * Returns central {@link PDROrdererFactory} instance used for sorting
	 * Aspects according to filtering criteria in SWT Preview Widgets like
	 * {@link PdrSelectionFilterPreview}.
	 * @return {@link PDROrdererFactory}
	 */
	public PDROrdererFactory getPdrOrdererFactory() {
		if (pdrOrdererFactory == null)
			pdrOrdererFactory = new PDROrdererFactory();
		return pdrOrdererFactory;
	}
	
	/**
	 * Returns an Array of <code>PdrObject</code> elements representing the
	 * current selection in the editor
	 * @return
	 * @see PdrObject
	 */
	public PdrObject[] getPdrObjects() {
		// TODO: there should be a way to export a batch of pdr person objects
		// TODO regardless of export file format. Maybe the preview GUI composite
		// TODO could provide this choice and inform the core provider in case the
		// TODO user chose to export multiple files at once. getPdrObjects could
		// TODO accordingly craft a collection of PdrObject[] sequences, and
		// TODO the respective plugin's AeExportUtilities subclass would be 
		//TODO required to implement a method export(Vector PdrObject[]) that
		//TODO would do the work. An output file selection GUI composite would
		//TODO in that case be used to specify a file name template
		Vector<PdrObject> pdrObjects = new Vector<PdrObject>();;
		
		try {
			//System.out.println("try to get Tree View selection from active");
			//System.out.println("workbench window");
			IStructuredSelection selection = 
					(IStructuredSelection) PlatformUI.getWorkbench().
					getActiveWorkbenchWindow().getSelectionService().
					getSelection(AEPluginIDs.VIEW_TREEVIEW); // where does this come from?
			//System.out.println("Number of elements selected in Tree View: "+selection.size());
			for (Object selectedObj : selection.toArray())
				if (selectedObj instanceof PdrObject) {
					pdrObjects.add((PdrObject) selectedObj);
					//System.out.println("Selected element:"+pdrObjects.lastElement().getDisplayNameWithID());
				} 	// org.bbaw.pdr.ae.model.view.TreeNode
				else if (selectedObj instanceof TreeNode);
					//System.out.println("TREE NODE");
				
		} catch (Exception e) {
			//System.out.println("ERROR! could not retrieve selection in Tree View:");
			e.printStackTrace();
		}
		
		if (pdrObjects.isEmpty()) {
			//System.out.println("try to get tree objects from facade");
			// genau wie in AspectsView 
			pdrObjects = new Vector<PdrObject>(Arrays.asList(Facade.getInstanz().getCurrentTreeObjects()));
			//TODO: handle attempt to export without any objects selected
			//System.out.println("number of objects: "+pdrObjects.size());
		}
		return pdrObjects.toArray(new PdrObject[pdrObjects.size()]);
	}
	
	/**
	 * Creates a new {@link PdrObjectsPreviewStructure} instance containing a forest
	 * representation of whatever {@link PdrObject}s are retrieved from the {@link Facade}
	 * instance. 
	 * In order to do this, the 
	 * {@link PDRObjectsProvider} known by the central export plugin  
	 * is supplied with the current selection of PdrObjects returned 
	 * by {@link Facade#getCurrentTreeObjects()}.
	 * A private reference to the instance created during this call is held by this class
	 * and can be retrieved via {@link #getPdrObjectsTree()}.
	 * @return newly created {@link PdrObjectsPreviewStructure} instance
	 */
	public PdrObjectsPreviewStructure createPdrObjectsTree() {
		pdrObjectsProvider.setInput(_facade.getCurrentTreeObjects()); 
		pdrObjectsProvider.setOrderer(new AspectsBySemanticOrderer()); // FIXME: ganz schön unflexibel
		pdrObjectsTree = new PdrObjectsPreviewStructure();
		return pdrObjectsTree;
	}
	
	/**
	 * Returns the {@link PdrObjectsPreviewStructure} reference owned by this class. If so such
	 * instance has been created so far, {@link #createPdrObjectsTree()} will be called.  
	 * @return central {@link PdrObjectsPreviewStructure} instance.
	 */
	public PdrObjectsPreviewStructure getPdrObjectsTree() {
		if (pdrObjectsTree != null)
			return pdrObjectsTree;
		return createPdrObjectsTree();
	}
	
	
	/**
	 * TODO: probably unnecessary
	 * Loads icon image files from the <code>icons</code> resource directory
	 * within this plugin's file structure
	 * @param filename name of the requested image file, relative to directory 
	 * <code>icons</code>
	 * @return <code>Image</code> object containing the icon
	 */
	private static Image loadImage(String filename) {
		Bundle bundle = FrameworkUtil.getBundle(AeExportCoreProvider.class);
		ImageDescriptor descriptor = 
				ImageDescriptor.createFromURL(FileLocator.find(bundle, 
						new Path("icons"+AEConstants.FS+filename), null));
		return descriptor.createImage();
	}
	
	
	/**
	 * Returns an {@link IDialogSettings} section to allow in which various dialog
	 * settings can be stored.
	 * @param pluginId <blockquote><code>String pluginId = FrameworkUtil.getBundle(getClass())
	 * .getSymbolicName();</code></blockquote> 
	 * @return the dialog settings section identified by the given pluginId 
	 */
	public IDialogSettings getSettings(String pluginId) {
		// ../runtime-org.bbaw.pdr.ae.standalone.product2/.metadata/.plugins/org.eclipse.ui.workbench/dialog_settings.xml
		IDialogSettings section = dialogSettingsRegistry.getSection(pluginId);
		return section;
	}
	
	/**
	 * Returns a subsection of the specified plugin's {@link IDialogSettings} section
	 * in the application's dialog settings. If no section is found for the key <code>section</code>,
	 * a new one is created.
	 * @param pluginId
	 * @param section
	 * @return
	 * @see #getSettings(String)
	 */
	public IDialogSettings getSettingsSection(String pluginId, String section) {
		// ../runtime-org.bbaw.pdr.ae.standalone.product2/.metadata/.plugins/org.eclipse.ui.workbench/dialog_settings.xml
		return dialogSettingsRegistry.getSubSection(pluginId, section);
	}
	
	/**
	 * creates and returns a new LinkedHashMap<String, Double> object from the settings
	 * section <code>section</code> in the specified <code>pluginSettings</code>
	 * branch
	 * @param pluginId
	 * @param history
	 * @return Map
	 * @see AeExportCoreProvider#dialogSettings(String)
	 */
	public LinkedHashMap<String, Long> getFileHistory(String pluginId, String history) {
		return dialogSettingsRegistry.getHistory(pluginId, history);
	}
	/**
	 * Returns the default recent-files history for the calling plugin. A 
	 * {@link Vector} representation of this file history is being returned
	 * by the method {@link #getHistoryAsArray(String)}. 
	 * @param pluginId symbolic name of the calling plugin
	 * @return LinkedHashMap<String, Double>
	 */
	public LinkedHashMap<String, Long> getFileHistory(String pluginId) {
		return getFileHistory(pluginId, DEF_HIST);
	}
	/**
	 * adds a filename to a plugin's recent-files history.
	 * @param pluginId
	 * @param history
	 * @param filename
	 */
	public void addToHistory(String pluginId, String history, String filename) {
		dialogSettingsRegistry.addToHistory(pluginId, history, filename);
	}
	/**
	 * Saves a file name and a current time stamp in the default recent-files history
	 * of specified plugin.
	 * @param pluginId
	 * @param filename
	 */
	public void addToHistory(String pluginId, String filename) {
		addToHistory(pluginId, DEF_HIST, filename);
	}
	
	/**
	 * gets filenames stored in this history as an ordered array
	 * @param pluginId
	 * @return
	 */
	public String[] getHistoryAsArray(String pluginId) {
		return getHistoryAsArray(pluginId, DEF_HIST);
	}
	//TODO doku
	public String[] getHistoryAsArray(String pluginId, String history) {
		LinkedHashMap<String, Long> recent = getFileHistory(pluginId, history);
		return recent.keySet().toArray(new String[recent.size()]);
	}
	
	/**
	 * Dump all settings to the file specified by the String  
	 * <code>[{@link #DIR_SETTINGS_SAVE}][{@link AEConstants#FS}]exports.conf</code>
	 */
	public void saveSettings() {
		String filename = SETTINGS_SAVE_DIR+AEConstants.FS+"exports.conf";
		try {
			dialogSettingsRegistry.saveToFile(filename);
		} catch (IOException e) {
			System.out.println("Could not save to "+filename);
			e.printStackTrace();
		}
	}
	
	/**
	 * loads global export settings from file. Will most likely run best
	 * on files that have been created calling {@link #saveSettings()}.
	 * Location at which file is expected assembles like
	 * <code>[{@link #DIR_SETTINGS_SAVE}][{@link AEConstants#FS}]exports.conf</code>
	 */
	public void loadSettings() {
		String filename = SETTINGS_SAVE_DIR+AEConstants.FS+"exports.conf";
		try {
			dialogSettingsRegistry.loadFromFile(filename);
		} catch (IOException e) {
			System.out.println("Could not load settings file  "+SETTINGS_SAVE_DIR+
					AEConstants.FS+"exports.conf");
			e.printStackTrace();
		}		
	}

	/**
	 * Saves a given array of supported filetype extensions to the {@link 
	 * IDialogSettings} section identified by the given <code>pluginId</code>. 
	 * @param pluginId identifier of the plugin for which the supported 
	 * filetypes are to be saved
	 * @param filetypes A {@link HashMap} which assigns a collection of filetype
	 * extensions like <code>{".xml", ".tex", ".pdf", ...}</code> to a 
	 * set identifier
	 * @param direction IO function of the set. Either {@link #EXT_IN} or 
	 * {@link #EXT_OUT}  
	 */
	public void setPluginFiletypes(String pluginId, 
					HashMap<String, Vector<String>> filetypes) {
		if (filetypes.size()>0) {
			IDialogSettings fileset = null;
			//System.out.println("Setting filetypes for: "+pluginId);
			//System.out.println(" IO: "+direction);
			for (Entry<String, Vector<String>> filetypeSet : filetypes.entrySet()) {
				//System.out.println("  Set Name: "+filetypeSet.getKey());
				fileset = dialogSettingsRegistry.getSubSection(pluginId, filetypeSet.getKey());
				// checks for a default file name at the first element
				Vector<String> ext = filetypeSet.getValue();
				if (!ext.firstElement().startsWith("*")) {
					System.out.println(" Store default file name to dialog settings "+pluginId);
					fileset.put(DEF_DEF_FN, ext.remove(0));
					System.out.println("  "+fileset.get(DEF_DEF_FN));
				}
				// save extensions as an array 
				fileset.put("filetypes", 
						ext.toArray(new String[ext.size()]));
			}
		}
	}
	
	/**
	 * Returns a list of supported filetype extensions for the given plugin id.
	 * @param pluginId plugin identifier
	 * @param ioDirection determines the function of the requested set of 
	 * filetype extensions. Has to be either {@link #EXT_IN} or {@link #EXT_OUT}
	 * @param set identifier of the filetype extension list subset. If the
	 * plugin does not define any set ID in its export contribution, the set
	 * is stored under the key "default"
	 * @return <code>String[]</code> array containing supported 
	 * filetype extensions like <code>{".xml", ".tex", ".pdf", ...}</code>
	 */
	public String[] getPluginFiletypes(String pluginId, String ioDirection, 
			String set) {
		// FIXME: add descriptive names to filetype extensions
		return dialogSettingsRegistry.getSubSection(pluginId, set).
				getArray("filetypes");
	}
	/**
	 * Returns the default list of supported filetype extensions for the given 
	 * plugin id.
	 * @param pluginId plugin identifier
	 * @param ioDirection determines the function of the requested set of 
	 * filetype extensions. Has to be either {@link #EXT_IN} or {@link #EXT_OUT}
	 * @return <code>String[]</code> array containing supported 
	 * filetype extensions like <code>{".xml", ".tex", ".pdf", ...}</code>
	 */	
	public String[] getPluginFiletypes(String pluginId, String ioDirection) {
		return getPluginFiletypes(pluginId, ioDirection);
	}
	
	/**
	 * Returns the list of {@link IPdrWidgetStructure} objects registered for a
	 * given plugin.
	 * @param pluginId
	 * @return Vector<IPdrWidgetStructure>
	 */
	public Vector<IPdrWidgetStructure> getWidgetRegistry(String pluginId) {
		if (!pluginWidgetRegistry.containsKey(pluginId))
			pluginWidgetRegistry.put(pluginId, new Vector<IPdrWidgetStructure>());
		System.out.println(pluginId+" registered widgets: "+pluginWidgetRegistry.get(pluginId).size());
		for (IPdrWidgetStructure w : pluginWidgetRegistry.get(pluginId))
			System.out.println(w.getClass().getCanonicalName()+" - "+
						w.getMessage());
		return pluginWidgetRegistry.get(pluginId);
	}
	
	/**
	 * Makes one string containing the tokens in a given list, separated by a given delimiter.
	 * @param array
	 * @param delimiter
	 */
	public static String join(List<String> array, String delimiter) {
		// TODO: find a place where to put utils like this
		Iterator<String> iter = array.iterator();
		String res = iter.next();
		while (iter.hasNext())
			res += delimiter+iter.next();		
		return res;
	}
	
	/**
	 * Return the label the specified label provider returns
	 * for the last element of a given list of keys. 
	 * @param keys
	 * @param provider label provider id, like {@link #PRIMARY_SEMANTIC_PROVIDER}
	 * @return
	 */
	public static String getAnnotationLabel(List<String> keys, String provider) {
		String label = null;
/*		if (keys.size()>0) {
			if (!keys.get(0).startsWith("aodl:"))
				keys.add(0, "aodl:" + keys.remove(0));
		} else
			return label;*/ 
		if (_facade.getConfigs().containsKey(provider)) {
			// retrieve top level of configs known for our provider
			HashMap<String, ConfigData> confs = _facade.getConfigs().get(provider).
					getChildren();
			// walk down configuration path
			if (confs != null)
				for (String key : keys) 
					if (confs.containsKey(key)) {
						ConfigData c = confs.get(key);
						label = c.getLabel();
						confs = c.getChildren();
					} // key adjustment for markup elements 
					else if (key.equals(keys.get(0)) && confs.containsKey("aodl:"+key)) {
						ConfigData c = confs.get("aodl:"+key);
						label = c.getLabel();
						confs = c.getChildren();
					} else
						return keys.get(keys.size()-1);
			}
		return label;
	}
	
	@Override
	protected void finalize() throws Throwable {
		dialogSettingsRegistry.save();
		super.finalize();
	}
}