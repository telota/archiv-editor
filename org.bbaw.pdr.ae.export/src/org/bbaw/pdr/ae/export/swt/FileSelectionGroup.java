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
package org.bbaw.pdr.ae.export.swt;

import java.io.File;

import org.bbaw.pdr.ae.common.AEConstants;
import org.bbaw.pdr.ae.common.CommonActivator;
import org.bbaw.pdr.ae.common.NLMessages;
import org.bbaw.pdr.ae.export.pluggable.AeExportCoreProvider;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ContentViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.IWizardContainer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;

//TODO: doku
/**
 * {@link AeExportCoreProvider#getFileHistory(String)}
 * {@link SWT#SAVE}
 * {@link #init(String, int)}
 * @author jhoeper
 *
 */
public class FileSelectionGroup extends ContentViewer 
								implements IPdrWidgetStructure {
	/**
	 * Opens a {@link FileDialog} for selection of a file from disk,
	 * passes resulting file name to the corresponding {@link ComboViewer} and
	 * asks {@link AeExportCoreProvider} to save it to the responsible file name history.  
	 * @author jhoeper
	 *
	 */
	private class SelectButtonListener extends SelectionAdapter {
		@Override
		public void widgetSelected(SelectionEvent event) {
			String[] segments = getFileLocation();
			// create file browser dialog
			FileDialog dialog = new FileDialog(container.getShell(),
									selectMode);
			dialog.setFilterPath(segments[0]);
			dialog.setFileName(segments[1]);
			dialog.setText(NLMessages.getString("export.fileselector.browse.caption"));
			dialog.setFilterExtensions(
					provider.getPluginFiletypes(pluginId,
									(selectMode == SWT.SAVE) ? 
											AeExportCoreProvider.EXT_OUT :
											AeExportCoreProvider.EXT_IN, 
									configuration));
			// open dialog
			String dest = dialog.open();
			// process resulting file name
			if (dest != null) {
				// update filename combo
				filenameCombo.insert(dest, 0);
				filenameCombo.getCombo().select(0);
				// save file name to history of recent files
				provider.addToHistory(pluginId, configuration, dest);
			}
		}
	}
	
	private class FilenameChangedListener implements ISelectionChangedListener,
														ModifyListener {
		public FilenameChangedListener() {
			filenameCombo.addSelectionChangedListener(this);
			filenameCombo.getCombo().addModifyListener(this);
		}
		@Override
		public void selectionChanged(SelectionChangedEvent event) {
			//System.out.println("combo selection changed");
			//FIXME: viel zu früh! damit wird automatisch der wizard angesprochen, der dann rückmeldung
			// von allen seinen GUI-komponenten will, die aber noch gar nicht alle fertig sind!
			validate();
		}
		@Override
		public void modifyText(ModifyEvent event) {
			//System.out.println("output file string changed");
			validate();
		}
	}
	
	// logger
	ILog log = AEConstants.ILOGGER;
	
	private Group container;
	private Label dsclabel;
	private Button checkEnable;
	private ComboViewer filenameCombo;
	private Button browseButton;
	//TODO: 'overwrite existent files' check button?
	
	private String pluginId;
	// corresponds to input/output sets in extension point
	private String configuration;
	// whatever the plugin specifies as default file for a configuration
	private String default_filename;
	
	private AeExportCoreProvider provider;
	
	private WizardPage wizardPage;
	
	//	open/save to file
	private int selectMode;
	private boolean isvalid;
	
	private String message;

	//TODO doc wizard
	/**
	 * <p>Creates a new group of widgets for file selection functionality.</p>
	 * <p>The framing container will be a simple {@link Group} with a border
	 * as defined by the SWT style {@link SWT#SHADOW_ETCHED_IN}. In order to
	 * have all involved widgets set up, {@link #init(String, int, int)} has to
	 * be called before use. If a custom SWT style is desired to have for
	 * the widget group, the constructor {@link #FileSelectionGroup(Composite, 
	 * int, String)} can be used alternatively. That one also allows for 
	 * specifying the mode of file access this template is desired to provide
	 * (<code>SWT.SAVE/SWT.OPEN</code>). The default is <code>SWT.OPEN</code>.
	 * </p>
	 * <p>Two arguments are required: the {@link Composite} in which the newly
	 * created GUI elements are to be embedded, and the symbolic name of the
	 * plugin contributing the export wizard which this file selection group
	 * is for. The plugin name is neccessary for identifying the internal
	 * file history, which unless further specification is the very one that is
	 * made available by the core export plugin's {@link AeExportCoreProvider}
	 * for every registered export plugin.</p>
	 * <p>Export plugins that wish to use this GUI template can retrieve their
	 * own plugin id by calling
	 * <blockquote><code>FrameworkUtil.getBundle(getClass()).getSymbolicName();
	 * </code></blockquote>
	 * The plugin's standard file history can be and is in fact by this 
	 * construtor obtained via {@link 
	 * AeExportCoreProvider#getFileHistory(String)}.  
	 * </p>
	 * @param parent Composite into which the newly created group will be 
	 * embedded
	 * @param plugin The symbolic name of the plugin contributing the dialog 
	 * that wishes to use this template 
	 */
	public FileSelectionGroup(String plugin, WizardPage page) {
		this(plugin, page, SWT.BORDER | SWT.SHADOW_ETCHED_IN | SWT.OPEN);
	}
	
	//TODO doc wizard
	/**
	 * <p>Creates a new group of widgets for file selection functionality.</p>
	 * <p>Customizable version of {@link #FileSelectionGroup(Composite, String)}.
	 * Both constructors do exactly the same. The main reason for this one to
	 * exist is the extra information that the <code>style</code> argument can
	 * deliver. If in the binary representation of <code>style</code>, the bit
	 * for the style flag {@link SWT#CHECK} is set, the template will be 
	 * equipped with an extra check box button for enabling/disabling
	 * the entire ensemble. With the flag {@link SWT#SAVE} set, the template
	 * will act as a selection tool for output files. Without, its default
	 * behaviour as a widget ensemble for opening existent files is kept.</p>
	 * <p>On further requirements, please read the base constructor {@link 
	 * #FileSelectionGroup(Composite, String)}'s documentation.</p>
	 * @param parent Composite into which the newly created group will be 
	 * embedded
	 * @param style Combination of desired SWT style flags by bitwise OR. If 
	 * <code>style</code> contains the bit for {@link SWT#CHECK}, the template
	 * will be equipped with an extra check box button for enabling/disabling
	 * the entire ensemble.
	 * @param plugin The symbolic name of the plugin contributing the dialog 
	 * that wishes to use this template 
	 */
	public FileSelectionGroup(String plugin, WizardPage page, Composite parent, int style) {
		container = new Group(parent, style | SWT.SHADOW_ETCHED_IN);
		container.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		container.setLayout(new GridLayout(3, false));
		
		this.wizardPage=page;
		pluginId = plugin;
		selectMode = (style & SWT.SAVE) == SWT.SAVE ? SWT.SAVE : SWT.OPEN;
		configuration = AeExportCoreProvider.DEF_SET;
	}
	
	//TODO doc
	/**
	 * Creates an instance for the given plugin and wizard page and applies 
	 * style to it
	 * @param plugin
	 * @param page
	 * @param style
	 */
	public FileSelectionGroup(String plugin, WizardPage page, int style) {
		this(plugin, page, (Composite)page.getControl(), style);
	}


	/**
	 * Initializes the embedded widgets serving as the components of this
	 * file selector template.
	 * <p>The arrangement of the template components will be more or less like 
	 * this:
	 * <blockquote>
	 * <table style="border:1px solid;"><tr>
	 * <td style="border:1px solid;">label</td>
	 * <td style="border:1px solid;">combo viewer</td>
	 * <td style="border:1px solid;">button</td></tr></table>
	 * </blockquote>
	 * </p>
	 * @param label Descriptive text label
	 * @param colspan Number of columns the template is desired to cover within
	 * its parent's layout
	 * @see #init(String, String, int)
	 */
	public void init(String label, int colspan) {
		// initialize connection to central export functionalities
		provider = AeExportCoreProvider.getInstance();
		
		((GridData)container.getLayoutData()).horizontalSpan=colspan;
		
		if ((container.getStyle() & SWT.CHECK) == SWT.CHECK) {
			checkEnable = new Button(container, SWT.CHECK);
			checkEnable.setText(label);
			//TODO: enabler listener
		} else {
			dsclabel = new Label(container, SWT.LEFT);
			dsclabel.setText(label);
		}
		
		filenameCombo = new ComboViewer(container, SWT.ALL);
		Combo combo = filenameCombo.getCombo();
		combo.setLayoutData(
				new GridData(SWT.FILL, SWT.CENTER, true, false));
		combo.setEnabled(true);
		filenameCombo.setLabelProvider(new LabelProvider());
		filenameCombo.setContentProvider(ArrayContentProvider.getInstance());

		default_filename = provider.getWizardProvider(
				wizardPage.getWizard()).getDefaultFilename(configuration);
		log.log(new Status(IStatus.INFO, CommonActivator.PLUGIN_ID,
				" Default file name for configuration '"+configuration+"': "+default_filename));
		
		String[] recent = provider.getHistoryAsArray(pluginId, configuration);
		log.log(new Status(IStatus.INFO, CommonActivator.PLUGIN_ID,
				"  Recent file list for configuration '"+configuration+"': "+recent.length));
		// if history could be restored
		if (recent.length>0)
			filenameCombo.setInput(recent);
		else if (default_filename != null)
			filenameCombo.setInput(new String[]{default_filename}); 
		if (combo.getItemCount() > 0)
			combo.setText(combo.getItem(0));
		//validate();
		new FilenameChangedListener();
		
		
		//TODO: listener on losing focus!!!
		
		browseButton = new Button(container, SWT.PUSH);
		browseButton.setLayoutData(
				new GridData(SWT.FILL, SWT.CENTER, false, false));
		browseButton.setText(NLMessages.getString("export.fileselector.browse.button"));
		browseButton.addSelectionListener( new SelectButtonListener() );
		// check if everything is correct.
		validate();
	}
	
	//TODO: doc
	public void init(String label, int colspan, String filetypeSetName) {
		this.configuration = filetypeSetName;
		init(label, colspan);
	}
	
	/**
	 * Does exactly the same as {@link #init(String, int)}, but also sets
	 * a text label for the widget group itself, meaning that there will be
	 * an additional description displayed, most likely, but dependent on the
	 * running platform's look and feel, being placed on the surrounding border. 
	 * @param boxlabel Top-level description label for this widget group
	 * @param label Descriptive label for the functionality this template 
	 * provides
	 * @param colspan Number of columns this template should cover in its 
	 * parent's layout
	 */
	public void init(String boxlabel, String label, int colspan) {
		container.setText(boxlabel);
		init(label, colspan);
	}
	
	//TODO:doc
	public void init(String boxlabel, String label, int colspan, 
						String filetypeSetName) {
		this.configuration = filetypeSetName;
		
		init(boxlabel, label, colspan);
	}
	
	
	/**
	 * Returns a <code>String[]</code> array of length 2, the elements of
	 * which represent a directory path and a base filename.
	 * <p>If the {@link ComboViewer} widget of this {@link FileSelectionGroup} 
	 * currently contains a valid file system path, the text content of the
	 * combo is segmented into an absolute directory and a relative file name.
	 * TODO
	 * If no directory segment can be derived from the combo's textual content,
	 * but a valid file name is available, then the latter will be understood
	 * relatively to the directory specified by {@link AEConstants#AE_HOME}</p> 
	 * @return <code>String[]{directory, filename}</code>
	 */
	public String[] getFileLocation() {
		
		File file = resolvPath();
		
		IPath path = new Path(file.getAbsolutePath());
		
		String filename = path.lastSegment();
		String dir = path.removeLastSegments(1).toOSString();
		
/*		System.out.println("File location:");
		System.out.println(dir);
		System.out.println(filename);*/
	
		return new String[]{dir, filename};
	}
	
	/**
	 * Return a resolved {@link File} representation of the widget's current 
	 * content. If this {@link FileSelectionGroup} is configured as an output
	 * file selector and points to an existing file, a dialog is raised
	 * to let the user decide whether to overwrite the file. 
	 * @return null if the file exists and the user doesn't agree in 
	 * overwriting it, {@link File} otherwise
	 */
	public File getFile() {
		
		File file = resolvPath();
		// raise dialog in case file is to be overwritten
		if (selectMode == SWT.SAVE)
			if (file.exists()) 
				return MessageDialog.openConfirm(container.getShell(), 
						NLMessages.getString("export.fileselector.file_exists.caption"),
						file.getAbsolutePath()+NLMessages.getString("Export_Dialog_FileExistsQuestion")) 
						?
						file : null;
		return file;
	}
	
	/**
	 * determines whether the current widget content represents a file usable
	 * as a input resource.
	 */
	private void validateIn(File file) {
		if (file.isDirectory())
			setInvalid(NLMessages.getString("export.fileselector.no_filename")); // Export_Filename_ErrorNoName
		else if (!file.exists()) 
			setInvalid(NLMessages.getString("export.fileselector.no_file"));
		else
			if (!file.canRead())
				setInvalid(NLMessages.getString("export.fileselector.not_permitted"));
			else 
				setValid();				
	}
	
	/**
	 * determines whether the current widget content can be used as an output
	 * file locator
	 */
	private void validateOut(File file) {
		//System.out.println("Validate output file location "+file.getPath());
		if (file.isDirectory()) {
			setInvalid(NLMessages.getString("export.fileselector.no_filename"));
		} else if (file.exists()) {
			if (!file.canWrite())
				setInvalid(NLMessages.getString("export.fileselector.not_permitted"));
			else
				setValid();
		} else 
			if (!file.getParentFile().canWrite()) 
				setInvalid(NLMessages.getString("export.fileselector.not_permitted"));
			else 
				setValid();		
	}
	
	/**
	 * Evaluates the current contents of the involved widgets and decides on
	 * overall validity.
	 */
	private void validate() {
		//System.out.println("validating input");
		
			File file = resolvPath();
			if (file == null) {
				setInvalid(NLMessages.getString("export.fileselector.invalid_filename"));
				return;
			}
			
			if (selectMode == SWT.OPEN)
				validateIn(file);
			else if (selectMode == SWT.SAVE)
				validateOut(file);
			
	}
	
	//TODO
	/**
	 * Tries to resolve the user input to a valid, absolute file location
	 * identifier which then is returned as a {@link File} object.
	 * <p>This is really only about having a clean file identifier. There is no 
	 * validation whatsoever involved in terms of read/write permissions, 
	 * file/directory resource or filetype extension.</p>   
	 * @return {@link File} object representing the resolved user input, 
	 * {@link AEConstants#AE_HOME} if said input cannot be resolved
	 */
	public File resolvPath() {
		IPath path = Path.EMPTY;
		String input = filenameCombo.getCombo().getText();
		//TODO: return directory? is that smart? given that this will be
		//forwarded to method that rely on getting a proper file?
		/*if (!path.isValidPath(input)) 
			return new File(AEConstants.AE_HOME + AEConstants.FS);*/
		path = new Path(input);
		if (!path.isAbsolute()) {
			path = new Path(AEConstants.AE_HOME).append(path);
			path.makeAbsolute();
		}		
		return new File(path.toOSString());
	}
	
	/**
	 * Indicates whether or not this widget ensemble is in an overall state
	 * that allows for i.e. involved wizard dialogs to continue. 
	 * @return <code>true</code> if the widget group's contents are legal
	 */
	@Override
	public boolean isValid() {
		//System.out.println(this.getClass().getCanonicalName()+(isvalid ? " is valid." : " is not valid"));
		return isvalid;
	}
	
	/**
	 * Mark this widget valid. Also make wizardcontainer update its controls, which
	 * means that the responsible wizard is made to reevaluate its 
	 * {@link IWizard#canFinish()}, which can be overwritten in order to consider
	 * the {@link IPdrWidgetStructure#isValid()} status of further widgets involved. 
	 */
	private void setValid() {
		isvalid = true;
		this.message = null;
		IWizardContainer wizardContainer = 
				this.wizardPage.getWizard().getContainer();
		try {
			wizardContainer.updateButtons();
			wizardContainer.updateMessage();
		} catch (Exception e) {
			log.log(new Status(IStatus.WARNING, CommonActivator.PLUGIN_ID,
					" Wizard noch nicht bereit? "+wizardPage.getClass().getCanonicalName()));
		}
	}
	
	/**
	 * Mark this widget invalid.
	 * @param message Complementary error message.
	 */
	private void setInvalid(String message) {
		//System.out.println("Mark file selector as invalid");
		isvalid = false;
		this.message = message;
		IWizardContainer wizardContainer = this.wizardPage.getWizard().getContainer(); 
		wizardContainer.updateButtons();
		wizardContainer.updateMessage();
	}
	
	@Override
	public String getMessage() {
		return message;
	}

	@Override
	public Control getControl() {
		if (filenameCombo != null)
			return filenameCombo.getControl();
		else 
			return null;
	}

	@Override
	public ISelection getSelection() {
		if (filenameCombo != null)
			return new StructuredSelection(filenameCombo.getCombo().getText());
		return null;
	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setSelection(ISelection selection, boolean reveal) {
		// TODO Auto-generated method stub
		
	}

	
	@Override
	public void performFinish() {
		if (filenameCombo != null) { 
			provider.addToHistory(pluginId, configuration, resolvPath().getAbsolutePath());
			String[] locators = getFileLocation();
			provider.getSettings(pluginId).put(AeExportCoreProvider.DEF_DIR, 
					locators[0]);
			provider.getSettings(pluginId).put(AeExportCoreProvider.DEF_FILE, 
					locators[1]);
		}
	}
}
