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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

import org.bbaw.pdr.ae.common.AEConstants;
import org.bbaw.pdr.ae.common.CommonActivator;
import org.bbaw.pdr.ae.export.swt.FileSelectionGroup;
import org.bbaw.pdr.ae.export.swt.IPdrWidgetStructure;
import org.bbaw.pdr.ae.export.swt.preview.PdrSelectionFilterPreview;
import org.bbaw.pdr.ae.model.PdrObject;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IExportWizard;
import org.eclipse.ui.IWorkbenchWizard;

//FIXME: change from interface to abstract class 
//TODO: doc
/**
 *<p>Plugins contributing to the extension point <code>org.bbaw.pdr.ae.export.core</code>
 * are required to denote a subclass that implements the methods of this abstract
 * class. That subclass is meant to act as a local delegate that provides and
 * customizes the API made available by the globally qualified
 * {@link AeExportCoreProvider} instance.</p>
* <p>Any implementing subclasses should initialize it's respective plugin identifier
* in their constructors, for instance by calling the accordingly implemented 
* method {@link #setPluginId()}. The plugin identifier is used by the core
* plugin as a key for plugin-specific information, for instance to return
* saved settings to {@link #getSettings()}</p>
* <h1>Corresponding {@link IExportWizard} implementation</h1>
* <p>The extension point <code>org.bbaw.pdr.ae.export.core</code> requires
* contributing plugins to refer to a class designated by a 
* <code>org.eclipse.ui.exportWizards</code> extension. That implies that every
* plugin intended to contribute to the AE export extension point should
* also contribute to <code>org.eclipse.ui.exportWizards</code>.</p>
* <p>The wizard that has to be present in a 
* <code>org.eclipse.ui.exportWizards</code> implementation has to both extend 
* the abstract class {@link Wizard} and implement {@link IExportWizard}, so 
* technically, it has only to implement 
* {@link IWorkbenchWizard#init(org.eclipse.ui.IWorkbench, 
* org.eclipse.jface.viewers.IStructuredSelection)} and {@link 
* IWizard#performFinish()}, but in order to have convenient access to the
* central AE functionality, a wizard should be equipped with some additional
* information:</p>
* <p>First, any wizard that is used in the way illustrated above can be given a
* reference to the <code>AeExportUtilities</code> subclass designated 
* as its complement by the
* plugin's AE export contribution by calling
* <blockquote><code>AeExportCoreProvider.getInstance().getUtilityProvider(this);
* </code></blockquote>
* Methods that could come in handy include {@link #getSettings()} 
* and {@link #getLatestFiles(String)}.</p>
* <p>For putting together the wizard's GUI with little effort, there are
* ready-to-use widget structures exported by the export core plugin. These 
* components are implementations of {@link IPdrWidgetStructure}. For instance:
* <ul><li>{@link PdrSelectionFilterPreview}: A tree view that autonomously obtains
* the AE's current selection of PDR objects and renders it as a interactive
* preview that allows for further filtering of export content</li>
* <li>{@link FileSelectionGroup}: A simple interface for the selection of
* resources in the local file system. Features file history, filetype extension
* filtering, file selection dialog handling and input validation.</li></ul>
* 
* <h2>Addressing wizard</h2>
* <p>When an export plugin is detected, the denoted wizard class is linked
* to the corresponding export utility provider object by the core plugin. 
* When that wizard is 
* instantiated, it can hence let the core plugin provider look up the 
* provider dedicated to it. It then notifies its provider by calling its
* {@link #setWizard(IExportWizard)} method in oder to claim that its up and
* running. This way, the utility provider has a chance to obtain a reference
* to the very wizard object it is expected to work with.</p> 
*/
public abstract class AeExportUtilities {

	/** Logger. */
	private static ILog logger = AEConstants.ILOGGER; 
	/**
	 * <p>By calling this method, the subclass can easily
	 * obtain the <code>provider</code> singleton reference 
	 * and store it in a private field.</p>   
	 * @return provider singleton instance of {@link AeExportCoreProvider}
	 */
	public AeExportCoreProvider getCoreProvider() {
		return AeExportCoreProvider.getInstance();
	}

	/**
	* <p>When an implementation of an <code> {@link IExportWizard} </code> that is 
	* being named by a plugin contributing to the extension point 
	* <code>org.bbaw.ae.export.core</code> is instantiated by eclipse to open
	* an export wizard dialog, that very wizard instantly calls this method
	* to report its availability.</p>
	* <p>This method is meant to be called only once, namely automatically during 
	* the wizard's instantiation. <strike>The default implementation resets the 
	* {@link AeExportCoreProvider#getWidgetRegistry(String)}, just in case
	* there are still widgets registered from previous activation of said wizard.
	* Hence, when overwriting this method, calling the superclasse's implementation
	* might be worth considering:
	* <code>super.setWizard(wizard);</code></strike></p>  
	*   
	* <p>In order to conversely being able to call the active wizard itself,
	* any class implementing this method should keep the reference, so as to
	* be on the safe side</p>
	*
	* @param wizard {@link IExportWizard} implementation calling to announce
	* itself as being ready to serve
	*/
	public void setWizard(IExportWizard wizard) {
		unregisterWidgets(); // FIXME wird leider nie aufgerufen
	}
	
	/**
	 * <p>Designed to return the {@link IExportWizard} instance that has been reported being
	 * responsible for the currently active export dialog via {@link 
	 * #setWizard(IExportWizard)}.</p> <p>However, the {@link AeExportUtilities} subclass
	 * that is intended to serve that wizard is free to do nothing in its
	 * implementation of {@link #setWizard(IExportWizard)}, since said wizard
	 * object will always know its {@link AeExportUtilities} counterpart. </p>
	 * @return latest {@link IExportWizard} instance that has been passed
	 * with a {@link #setWizard(IExportWizard)} call. 
	 */
	public abstract IExportWizard getWizard();
	
	/**
	 * To be implemented by the subclass in order to return the implementing
	 * plugin's symbolic name assigned to some subclass member in 
	 * {@link #setPluginId()}.
	 * @return 
	 */
	public abstract String pluginId();

	/**
	 * Has to be overwritten by subclass in order to initialize the subclass's
	 * <code>pluginId</code> field. This method is by default called during the first call
	 * of {@link #pluginId()}.
	 * <p>The subclass doesn't have much more to do than call the following line
	 * in its implementation:
	 * <blockquote><code>	
	 * pluginId = FrameworkUtil.getBundle(getClass()).getSymbolicName();
	 * </code></blockquote>
	 * That's it.</p>
	 */
	public abstract void setPluginId();

	
	//TODO doc
	public PdrObject[] getPdrObjects() {
		// TODO: do we even need this?
		return getCoreProvider().getPdrObjects();
	}
	
	
	/**
	* <p>The <code>IExportWizard</code> implementation might be interested in
	* having an own section of {@link IDialogSettings} at its disposal, 
	* most likely because of the desire to set up GUI components using content
	* from a previous session.</p>
	* <p>To avoid confusion, the implementation of this method should call 
	* {@link AeExportCoreProvider#getSettings(String)} passing a unique 
	* identifier. On behalf of comprehensibility, the plugin name might be
	* a reasonable choice:</p>
	* <blockquote><code>String pluginId = FrameworkUtil.getBundle(getClass())
	* .getSymbolicName();</code></blockquote>
	* @return <code>IDialogSettings</code> a dialog settings section to save
	* configuration data to
	*/
	public IDialogSettings getSettings() {
		return getCoreProvider().getSettings(pluginId());
	}
	


	//TODO doc
	public String[] getFiletypes(int ioFunc, String setName) {
		return getCoreProvider().getPluginFiletypes(pluginId(),
				(ioFunc == SWT.SAVE) ? AeExportCoreProvider.EXT_OUT :
										AeExportCoreProvider.EXT_IN,
				setName);
	}
	
	/**
	 * Returns the location of an export plugin's resource specified by
	 * its relative path in the bundled plugin.
	 * @param path relative path of desired resource within plugin file structure.
	 * @return absolute path to file
	 * @throws IOException
	 */
	public String staticResource(String path) throws IOException {
		// http://www.eclipse.org/forums/index.php/mv/msg/45047/146049/#msg_146049
		//TODO: test
		String extendedPath = AEConstants.AE_HOME + AEConstants.FS + "export-stylesheets" + AEConstants.FS +path;
		File file = new File(extendedPath);
		if (!file.exists())
		{
			InputStream stream = this.getClass().getClassLoader().getResourceAsStream(path);
			File dir = new File(AEConstants.AE_HOME + AEConstants.FS + "export-stylesheets"+ AEConstants.FS + "resources");
			if (!dir.exists()) dir.mkdirs();
			OutputStream out = new FileOutputStream(file);

			// Transfer bytes from in to out
			byte[] buf = new byte[1024];
			int len;
			while ((len = stream.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			stream.close();
			out.close();
		}
		
//		Bundle bundle = Platform.getBundle(pluginId());
//		iLogger.log(new Status(IStatus.INFO, CommonActivator.PLUGIN_ID,
//				"Retrieving locator for resource '"+path+"' of plugin "+pluginId()));
//		iLogger.log(new Status(IStatus.INFO, CommonActivator.PLUGIN_ID,
//				"Plugin location: "+bundle.getLocation()));
//		// url of resource shipped with bundle
//		URL url = bundle.getEntry(path); // wg moegl. probleme mit getResource
//		if (url != null) {
//			iLogger.log(new Status(IStatus.INFO, CommonActivator.PLUGIN_ID,
//					"Resource URL: "+url));
//			try {
//				// URI representation of resource URL (due to possible problems using URL alone.
//				// this here is how its done in AEConstants, RepositoryUpdateManager etc.)
//				URI uri = FileLocator.resolve(url).toURI();
//				iLogger.log(new Status(IStatus.INFO, CommonActivator.PLUGIN_ID,
//						"Resolved locator: "+uri));
//				// file object linking said URI
//				File file = new File(uri);
//				iLogger.log(new Status(IStatus.INFO, CommonActivator.PLUGIN_ID,
//						"Resolved file name: "+file.getAbsolutePath()));
//				iLogger.log(new Status(IStatus.INFO, CommonActivator.PLUGIN_ID,
//						"Does file exist? - "+file.exists()));
//				if (file.exists()) return file.getAbsolutePath();
//			} catch (URISyntaxException e) {
//				iLogger.log(new Status(IStatus.WARNING, CommonActivator.PLUGIN_ID,
//						"Failed to transform file URL "+url+" to URI."));
//				e.printStackTrace();
//				// file object created using URL instead of URI
//				File file = new File(FileLocator.resolve(url).toString());
//				iLogger.log(new Status(IStatus.INFO, CommonActivator.PLUGIN_ID,
//						"File name resolved from URL: "+file.getAbsolutePath()));
//				iLogger.log(new Status(IStatus.INFO, CommonActivator.PLUGIN_ID,
//						"Does file exist? - "+file.exists()));
//				if (file.exists()) return file.getAbsolutePath();
//			}
//		}
		/*
		// load via classloader
		ResourceLocator locator = new ResourceLocator();
		InputStream in = locator.getClass().getClassLoader().getResourceAsStream(path);*/ 
		logger.log(new Status(IStatus.WARNING, CommonActivator.PLUGIN_ID,
				"Failed to resolve resource path. Returning "+extendedPath));
		return extendedPath;
	}
	
	/**
	 * Retrieve the default file location for a given set name.
	 * TODO: pluginId kein guter identifier. 
	 * @param set
	 * @return
	 */
	public String getDefaultFilename(String set) {
		String filename = getCoreProvider().getSettingsSection(pluginId(), set).get(
				AeExportCoreProvider.DEF_DEF_FN);
		logger.log(new Status(IStatus.INFO, CommonActivator.PLUGIN_ID,
				"Retrieve default file location for plugin "+pluginId()+", configuration '"+set+"'."));
		if (filename != null) {
			String absolute;
			try {
				absolute = staticResource(filename);
				if (absolute != null) 
					return absolute;			
			} catch (IOException e) {
				log(IStatus.WARNING,
						"Could not resolve resource path "+filename+" for plugin "+pluginId());
			}
			return filename;
		} 
		return null;
	}
	
	
	/**
	 * Logs a message.
	 * @param level level of message, e.g. {@link IStatus.INFO}
	 * @param msg
	 */
	public void log(int level, String msg) {
		if (pluginId() != null) {
			logger.log(new Status(level, pluginId(), msg));
		} else {
			throw new NullPointerException("pluginId has not been set!");
		}
	}

	//TODO define abstract method export() or process() that as an argument
	//TODO takes some collection of PdrObject[] arrays and returns true on
	//TODO successful export
	//TODO this would allow for choosing to export multiple files for multiple
	//TODO pdr persons at once. The active wizard would call the AeExportUtilities
	//TODO export method in performFinish() and let it do all of the processing.
	//TODO The core plugin would have been informed about the choice to export
	//TODO a batch instead of a single file (e.g. by the preview GUI composite)
	//TODO and return a collection of PdrObject[]
	//TODO sequences. 
	/**
	 * Dummy method which does nothing but returning true. Meant to be replaced
	 * by an abstract declaration if it seems to be of a better design to have
	 * the actual export/save code in the utility provider class of each export 
	 * plugin.
	 * @return
	 */
	public boolean export() {
		// TODO Auto-generated method stub: make abstract if this design
		// turns out to be viable
		return true;
	}
	
	/**
	 * Registers an {@link IPdrWidgetStructure} object for this plugin.
	 * @param widget
	 */
	public void registerWidget(IPdrWidgetStructure widget) { 
		Vector<IPdrWidgetStructure> widgetRegistry = getCoreProvider().getWidgetRegistry(pluginId());
		if (!widgetRegistry.contains(widget)) {
			widgetRegistry.add(widget);
			log(IStatus.INFO,
					"Plugin "+pluginId()+": register "+widget.getClass().getCanonicalName());
		}
	}
	
	/**
	 * Unregisters {@link IPdrWidgetStructure} object from plugin's registry.
	 * @param widget
	 */
	public void unregisterWidget(IPdrWidgetStructure widget) {
		// TODO: think about how to unregister widgets the most conveniently
		Vector<IPdrWidgetStructure> widgetRegistry = getCoreProvider().getWidgetRegistry(pluginId());
		if (widgetRegistry.contains(widget))
			widgetRegistry.remove(widget);
	}
	
	/**
	 * Calls {@link IPdrWidgetStructure#performFinish()} for all registered widgets.
	 * Unregisters all widgets from registry afterwards.
	 * @see #registerWidget(IPdrWidgetStructure)
	 */
	public void terminateWidgets() {
		for (IPdrWidgetStructure widget : getCoreProvider().getWidgetRegistry(pluginId())) {
			log(IStatus.INFO,
					"Perform termination of widget "+widget.getClass().getCanonicalName());
			widget.performFinish();
		}
		unregisterWidgets();
	}
	
	/**
	 * Remove all {@link IPdrWidgetStructure}s from registry
	 * @see #unregisterWidget(IPdrWidgetStructure)
	 */
	public void unregisterWidgets() {
		getCoreProvider().getWidgetRegistry(pluginId()).removeAllElements();
	}
	
}
