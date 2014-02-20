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
package org.bbaw.pdr.ae.config.editor.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.xml.stream.XMLStreamException;

import org.bbaw.pdr.ae.common.AEConstants;
import org.bbaw.pdr.ae.common.CommonActivator;
import org.bbaw.pdr.ae.common.NLMessages;
import org.bbaw.pdr.ae.common.icons.IconsInternal;
import org.bbaw.pdr.ae.config.core.ConfigDataComparator;
import org.bbaw.pdr.ae.config.core.ConfigFactory;
import org.bbaw.pdr.ae.config.core.IAEMarkupTemplateConfigEditor;
import org.bbaw.pdr.ae.config.core.IConfigFacade;
import org.bbaw.pdr.ae.config.core.IConfigManager;
import org.bbaw.pdr.ae.config.core.IConfigRightsChecker;
import org.bbaw.pdr.ae.config.editor.internal.ConfigDragListener;
import org.bbaw.pdr.ae.config.editor.internal.ConfigDropListner;
import org.bbaw.pdr.ae.config.editor.internal.ConfigTransfer;
import org.bbaw.pdr.ae.config.editor.internal.CreateConfigDialog;
import org.bbaw.pdr.ae.config.model.AspectConfigTemplate;
import org.bbaw.pdr.ae.config.model.ConfigData;
import org.bbaw.pdr.ae.config.model.ConfigItem;
import org.bbaw.pdr.ae.config.model.ConfigTreeNode;
import org.bbaw.pdr.ae.config.model.DataType;
import org.bbaw.pdr.ae.config.model.DatatypeDesc;
import org.bbaw.pdr.ae.config.model.IdentifierConfig;
import org.bbaw.pdr.ae.config.model.SemanticTemplate;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IParameter;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.Parameterization;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.IHandlerService;

/**
 * creates dialog for editing and creating relation classification lists.
 * @author cplutte
 */
public class ConfigEditor extends TitleAreaDialog implements Observer
{

	/** singleton instance of facade. */
	private IConfigFacade _configFacade = ConfigFactory.getConfigFacade();
	/** user rights checker. */
	private IConfigRightsChecker _userRichtsChecker = _configFacade.getConfigRichtsChecker();
	/** config manager. */
	private IConfigManager _cfgManager = _configFacade.getConfigManager();
	/** Instance of shared image registry. */
	private ImageRegistry _imageReg = CommonActivator.getDefault().getImageRegistry();
	/** processor for loading and saving tagging lists. */
	/** The markup provider. */
	private String _semanticProvider = Platform
			.getPreferencesService()
			.getString(CommonActivator.PLUGIN_ID,
					"PRIMARY_SEMANTIC_PROVIDER", AEConstants.CLASSIFICATION_AUTHORITY, null).toUpperCase(); //$NON-NLS-1$;
	/** composite left. */
	private Composite _leftComposite;
	/** composite right. */
	private Composite _rightComposite;
	/** main sashForm. */
	private SashForm _mainSashForm;
	/** combo for relation types. */
	private Spinner _baseSpinner;
	/** combo for relation subtypes. */
	private Combo _langCombo;
	/** provider combo. */
	private Combo _providerCombo;
	/** for adding entry to list. */
	private Button _addToList;
	/** deleting entry from list. */
	private Button _deleteFromList;
	/** button to create new configuration. */
	private Button _newButton;
	/** button to set semantic tag as person name tag . */
	private Button _setAsPersonName;
	/** button to set semantic tag as person display name tag. */
	private Button _setAsNormName;
	/** button to set config item as ignored. */
	private Button _ignoreButton;
	/** button to delete config. */
	private Button _deleteConfigButton;

	/** name of selected list. */
	private String _lang = ""; //$NON-NLS-1$

	/** system language. */
	private String _systemLang = ""; //$NON-NLS-1$

	/** name text. */
	private Text _name;
	/** label text. */
	private Text _labelText;
	/** position text. */
	private Text _posText;

	/** documentation text. */
	private Text _docuText;

	private Text _semanticAspectTemplateText;
	/** datatypedesc. */
	private DatatypeDesc _datatypeDesc;

	/** new additional entry. */
	private Text _addElement;

	/** tree viewer. */
	private TreeViewer _treeViewer;

	/** new configuration. */
	private DatatypeDesc _newConfiguration;

	/** available languages. */
	private String[] _langs =
	{"de", "en", "it", "fr"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	protected IAEMarkupTemplateConfigEditor _markupTemplateEditor = ConfigFactory.getMarkupTemplateConfigEditor();
	private Text currentProvText;

	/**
	 * constructor.
	 * @param parentShell parent shell
	 */
	public ConfigEditor(final Shell parentShell)
	{
		super(parentShell);
	}

	@Override
	public final void create()
	{
		super.create();
		// Set the title
		setTitle(NLMessages.getString("Config_editor_title"));
		dialogArea.addListener(SWT.Traverse, new Listener()
		{
			@Override
			public void handleEvent(final Event e)
			{
				if (e.detail == SWT.TRAVERSE_ESCAPE)
				{
					e.doit = false;
				}
			}
		});

	}

	@Override
	protected final void createButtonsForButtonBar(final Composite parent)
	{
		GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.horizontalSpan = 3;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalAlignment = SWT.CENTER;

		parent.setLayoutData(gridData);
		// Create Add button
		// Own method as we need to overview the SelectionAdapter
		createSaveButton(parent, OK, NLMessages.getString("Config_save_close"), false);
		// Add a SelectionListener

		createOkButton(parent, OK, NLMessages.getString("Config_refresh"), false);
		// Add a SelectionListener

		// Create Cancel button
		Button cancelButton = createButton(parent, CANCEL, NLMessages.getString("Config_cancel"), false);
		// Add a SelectionListener
		cancelButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent e)
			{
				setReturnCode(CANCEL);
				_configFacade.setConfigs(null);
				close();
			}
		});
	}

	

	@Override
	protected final Control createDialogArea(final Composite parent)
	{

		_mainSashForm = new SashForm(parent, SWT.HORIZONTAL | SWT.BORDER);
		_mainSashForm.setLayoutData(new GridData());
		((GridData) _mainSashForm.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) _mainSashForm.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _mainSashForm.getLayoutData()).grabExcessVerticalSpace = true;
		((GridData) _mainSashForm.getLayoutData()).verticalAlignment = SWT.FILL;
		((GridData) _mainSashForm.getLayoutData()).minimumHeight = 470;
		((GridData) _mainSashForm.getLayoutData()).minimumWidth = 800;

		_leftComposite = new Composite(_mainSashForm, SWT.NONE);
		_leftComposite.setLayoutData(new GridData());
		((GridData) _leftComposite.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) _leftComposite.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _leftComposite.getLayoutData()).grabExcessVerticalSpace = true;
		((GridData) _leftComposite.getLayoutData()).verticalAlignment = SWT.FILL;
		_leftComposite.setLayout(new GridLayout());
		((GridLayout) _leftComposite.getLayout()).numColumns = 5;
		((GridLayout) _leftComposite.getLayout()).makeColumnsEqualWidth = false;

		Label currentProv = new Label(_leftComposite, SWT.NONE);
		currentProv.setLayoutData(new GridData());
		currentProv.setText("Currently used classification provider");
		((GridData) currentProv.getLayoutData()).horizontalSpan = 2;

		currentProvText = new Text(_leftComposite, SWT.READ_ONLY);
		currentProvText.setLayoutData(new GridData());
		currentProvText.setText(_semanticProvider);
		((GridData) currentProvText.getLayoutData()).horizontalSpan = 2;

		Button providerChange = new Button(_leftComposite, SWT.PUSH);
		providerChange.setLayoutData(new GridData());
		((GridData) providerChange.getLayoutData()).horizontalAlignment = SWT.RIGHT;
		providerChange.setImage(_imageReg.get(IconsInternal.PREFERENCES));
		providerChange.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent event)
			{
				ArrayList<Parameterization> parameters = new ArrayList<Parameterization>();
				IParameter iparam = null;

				// get the command from plugin.xml
				IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
				ICommandService cmdService = (ICommandService) window.getService(ICommandService.class);
				Command cmd = cmdService.getCommand("org.eclipse.ui.window.preferences");
				// get the parameter
				try
				{
					iparam = cmd.getParameter("preferencePageId");
				}
				catch (NotDefinedException e1)
				{
					e1.printStackTrace();
				}
				Parameterization params = new Parameterization(iparam,
						"org.bbaw.pdr.ae.view.main.preferences.AspectPage");
				parameters.add(params);

				// build the parameterized command
				ParameterizedCommand pc = new ParameterizedCommand(cmd, parameters
						.toArray(new Parameterization[parameters.size()]));

				// execute the command
				try
				{
					IHandlerService handlerService = (IHandlerService) window.getService(IHandlerService.class);
					handlerService.executeCommand(pc, null);
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
				_semanticProvider = Platform
						.getPreferencesService()
						.getString(CommonActivator.PLUGIN_ID,
								"PRIMARY_SEMANTIC_PROVIDER", AEConstants.CLASSIFICATION_AUTHORITY, null).toUpperCase(); //$NON-NLS-1$;
				currentProvText.setText(_semanticProvider);

			}
		}); // SelectionListener

		Label prov = new Label(_leftComposite, SWT.NONE);
		prov.setLayoutData(new GridData());
		prov.setText(NLMessages.getString("Preference_select_primary_semantic_provider"));
		_providerCombo = new Combo(_leftComposite, SWT.BORDER | SWT.READ_ONLY);
		_providerCombo.setLayoutData(new GridData());

		if (_configFacade.getConfigs() == null)
		{
		}

		for (String s : _configFacade.getConfigs().keySet())
		{
			_providerCombo.add(s);
		}

		_providerCombo.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent e)
			{
				_datatypeDesc = _configFacade.getConfigs().get(
						_providerCombo.getItem(_providerCombo.getSelectionIndex()));
				checkAndCompleteConfig(_datatypeDesc);
				_treeViewer.setInput(_datatypeDesc);
				if (_deleteConfigButton != null)
				{
					_deleteConfigButton.setEnabled(_userRichtsChecker.mayEditConfig());
				}
				loadConfigData(null);

			}
		});

		if (_userRichtsChecker.mayEditConfig())
		{
			_deleteConfigButton = new Button(_leftComposite, SWT.PUSH);
			_deleteConfigButton.setText(NLMessages.getString("Config_delete"));
			_deleteConfigButton.setToolTipText(NLMessages.getString("Config_delete_tooltip"));
			_deleteConfigButton.addSelectionListener(new SelectionAdapter()
			{
				@Override
				public void widgetSelected(final SelectionEvent e)
				{
					IWorkbench workbench = PlatformUI.getWorkbench();
					Display display = workbench.getDisplay();
					Shell shell = new Shell(display);
					String message = NLMessages.getString("Config_delete_warning1");
					message += NLMessages.getString("Config_delete_warning2");
					MessageDialog messageDialog = new MessageDialog(shell, NLMessages
							.getString("Config_delete_configuration"), null, message, MessageDialog.WARNING,
							new String[]
							{NLMessages.getString("Config_delete"), NLMessages.getString("Config_cancel")}, 1); //$NON-NLS-1$ //$NON-NLS-2$
					if (messageDialog.open() == 0)
					{
						_configFacade.getConfigs().remove(_providerCombo.getItem(_providerCombo.getSelectionIndex()));
						_cfgManager.deleteConfig(_providerCombo.getItem(_providerCombo.getSelectionIndex())
								.toLowerCase());
						_cfgManager.deleteConfig(_providerCombo.getItem(_providerCombo.getSelectionIndex())
								.toUpperCase());

						_datatypeDesc = null;
						_treeViewer.setInput(null);
						_treeViewer.refresh();
						_providerCombo.setEnabled(true);
						_newButton.setEnabled(true);
						_providerCombo.removeAll();
						for (String s : _configFacade.getConfigs().keySet())
						{
							_providerCombo.add(s);
						}
						_providerCombo.select(0);
						_deleteConfigButton.setEnabled(false);
					}

				}
			});
			_deleteConfigButton.pack();

			_newButton = new Button(_leftComposite, SWT.PUSH);
			_newButton.setText(NLMessages.getString("Config_new"));
			_newButton.addSelectionListener(new SelectionAdapter()
			{
				@Override
				public void widgetSelected(final SelectionEvent e)
				{
					// createNewConfig(providerText.getText().trim().toUpperCase(),
					// newProviderCombo.getItem(newProviderCombo.getSelectionIndex()));
					IWorkbench workbench = PlatformUI.getWorkbench();
					Display display = workbench.getDisplay();
					Shell shell = new Shell(display);
					CreateConfigDialog dialog = new CreateConfigDialog(shell, ConfigEditor.this);
					dialog.open();
					if (_newConfiguration != null)
					{
						CommonActivator.getDefault().getPreferenceStore().setValue("PRIMARY_SEMANTIC_PROVIDER", //$NON-NLS-1$
								_newConfiguration.getProvider()); //$NON-NLS-1$
						CommonActivator.getDefault().getPreferenceStore().setValue("PRIMARY_TAGGING_PROVIDER", //$NON-NLS-1$
								_newConfiguration.getProvider()); //$NON-NLS-1$
						CommonActivator.getDefault().getPreferenceStore().setValue("PRIMARY_RELATION_PROVIDER", //$NON-NLS-1$
								_newConfiguration.getProvider());
						_providerCombo.add(_newConfiguration.getProvider(), 0);
						_providerCombo.select(0);
						_providerCombo.setEnabled(false);
						_datatypeDesc = _newConfiguration;
						checkAndCompleteConfig(_datatypeDesc);
						_treeViewer.setInput(_datatypeDesc);
						_treeViewer.refresh();
						_providerCombo.setEnabled(false);
						currentProvText.setText(_newConfiguration.getProvider());
						if (_newButton != null)
						{
							_newButton.setEnabled(false);
						}
						_newConfiguration = null;
					}
				}
			});
			_newButton.pack();
		}
		else
		{
			Label blancLabel = new Label(_leftComposite, SWT.NONE);
			blancLabel.setText(""); //$NON-NLS-1$
			blancLabel.setLayoutData(new GridData());
			((GridData) blancLabel.getLayoutData()).horizontalSpan = 2;
		}

		Tree tree = new Tree(_leftComposite, SWT.BORDER);
		tree.setLayoutData(new GridData());
		((GridData) tree.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) tree.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) tree.getLayoutData()).grabExcessVerticalSpace = true;
		((GridData) tree.getLayoutData()).verticalAlignment = SWT.FILL;
		((GridData) tree.getLayoutData()).horizontalSpan = 5;

		_treeViewer = new TreeViewer(tree);
		TreeColumn column = new TreeColumn(_treeViewer.getTree(), SWT.NONE);
		column.setWidth(450);
		column.setResizable(true);
		column.setText("Column 1"); //$NON-NLS-1$

		int operations = DND.DROP_COPY | DND.DROP_MOVE;
		Transfer[] transferTypes = new Transfer[]
		{ConfigTransfer.getInstance()};
		if (_userRichtsChecker.mayEditConfig())
		{
			_treeViewer.addDragSupport(operations, transferTypes, new ConfigDragListener(_treeViewer));
			_treeViewer.addDropSupport(operations, transferTypes, new ConfigDropListner(_treeViewer));
		}
		_treeViewer.setContentProvider(new ConfigTreeContentProvider(true, _markupTemplateEditor != null, true));
		_treeViewer.setLabelProvider(new ConfigTreeLabelProvider());

		_treeViewer.setSorter(new ConfigTreeSorter());
		_treeViewer.addDoubleClickListener(new IDoubleClickListener()
		{
			@Override
			public void doubleClick(final DoubleClickEvent event)
			{
				if (event.getSelection() instanceof IStructuredSelection)
				{
					IStructuredSelection selection = (IStructuredSelection) event.getSelection();
					Object obj = selection.getFirstElement();
					ConfigTreeNode tn = (ConfigTreeNode) obj;
					if (tn.hasChildren())
					{
						_treeViewer.setExpandedState(tn, !_treeViewer.getExpandedState(tn));
					}
				}
			}
		});
		_treeViewer.addSelectionChangedListener(new ISelectionChangedListener()
		{
			@Override
			public void selectionChanged(final SelectionChangedEvent event)
			{
				IStructuredSelection selection = (IStructuredSelection) event.getSelection();
				Object obj = selection.getFirstElement();
				ConfigTreeNode tn = (ConfigTreeNode) obj;

				loadConfigData(tn);

			}
		});

		// public void doubleClick(final DoubleClickEvent event)
		// {
		// IStructuredSelection selection = (IStructuredSelection)
		// event.getSelection();
		// Object obj = ((IStructuredSelection)
		// selection).getFirstElement();
		// TreeNode tn = (TreeNode) obj;
		// loadValues(tn.getConfigData());
		// }
		// });

		// }// leftComposite
		_leftComposite.layout();
		// leftComposite.pack();

		_rightComposite = new Composite(_mainSashForm, SWT.NONE);
		_rightComposite.setLayoutData(new GridData());
		((GridData) _rightComposite.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) _rightComposite.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _rightComposite.getLayoutData()).grabExcessVerticalSpace = true;
		((GridData) _rightComposite.getLayoutData()).verticalAlignment = SWT.FILL;
		_rightComposite.setLayout(new GridLayout());
		((GridLayout) _rightComposite.getLayout()).numColumns = 3;
		((GridLayout) _rightComposite.getLayout()).makeColumnsEqualWidth = false;
		_rightComposite.layout();
		// rightComposite.pack();

		_mainSashForm.setWeights(new int[]
		{3, 3});

		// mainSashForm.pack();
		// mainComposite
		boolean found = false;
		for (String s : _providerCombo.getItems())
		{
			if (s.equals(_semanticProvider))
			{
				_providerCombo.select(_providerCombo.indexOf(_semanticProvider));
				_datatypeDesc = _configFacade.getConfigs().get(_providerCombo.getItem(_providerCombo.getSelectionIndex()));
				found = true;
				break;
			}
		}
		if (!found)
		{
			_providerCombo.select(0);
			_semanticProvider = _providerCombo.getItem(0);
			_datatypeDesc = _configFacade.getConfigs().get(_semanticProvider);
		}
		
		checkAndCompleteConfig(_datatypeDesc);
		_treeViewer.setInput(_datatypeDesc);

		parent.pack();
		return parent;
	}

	private void checkAndCompleteConfig(DatatypeDesc _datatypeDesc)
	{
		if (_markupTemplateEditor != null)
		{
			if (_datatypeDesc.getUsage().getTemplates() == null)
			{
				ConfigData templates = new ConfigData();
				templates.setValue("templates");
				templates.setLabel("Templates");
				_datatypeDesc.getUsage().setTemplates(templates);
			}
			if (!_datatypeDesc.getUsage().getTemplates().getChildren().containsKey("aspectTemplates"))
			{
				ConfigData aspectTemplates = new ConfigData();
				aspectTemplates.setValue("aspectTemplates");
				aspectTemplates.setLabel("Aspect Templates");
				_datatypeDesc.getUsage().getTemplates().getChildren().put("aspectTemplates", aspectTemplates);
			}
			if (!_datatypeDesc.getUsage().getTemplates().getChildren().get("aspectTemplates").getChildren()
					.containsKey("complexTemplates"))
			{
				ConfigData complexTemplates = new ConfigData();
				complexTemplates.setValue("complexTemplates");
				complexTemplates.setLabel("Complex Templates");
				_datatypeDesc.getUsage().getTemplates().getChildren().get("aspectTemplates").getChildren()
						.put("complexTemplates", complexTemplates);
			}
			if (!_datatypeDesc.getUsage().getTemplates().getChildren().get("aspectTemplates").getChildren()
					.containsKey("semanticTemplates"))
			{
				ConfigData semanticTemplates = new ConfigData();
				semanticTemplates.setValue("semanticTemplates");
				semanticTemplates.setLabel("Semantic Templates");
				_datatypeDesc.getUsage().getTemplates().getChildren().get("aspectTemplates").getChildren()
						.put("semanticTemplates", semanticTemplates);
			}
			ConfigData semanticTemplates = _datatypeDesc.getUsage().getTemplates().getChildren().get("aspectTemplates")
					.getChildren().get("semanticTemplates");
			for (ConfigData semantic : _datatypeDesc.getChildren().get("aodl:semanticStm").getChildren().values())
			{
				if (!semanticTemplates.getChildren().containsKey(semantic.getValue()))
				{
					SemanticTemplate semanticTemplate = new SemanticTemplate();
					semanticTemplate.setValue(semantic.getValue());
					semanticTemplate.setLabel(semantic.getLabel());
					semanticTemplate.setParent(semanticTemplates);
					semanticTemplate.setPriority(semantic.getPriority());
					semanticTemplate.setIgnore(true);
					String value = NLMessages.getString("Dialog_reference");
					AspectConfigTemplate configTemplate = new AspectConfigTemplate(value);
					configTemplate.setParent(semanticTemplate);
					configTemplate.setPriority(25);
					configTemplate.setHorizontalSpan(4);
					configTemplate.setRequired(true);
					configTemplate.setWidgetType(7);
					semanticTemplate.getChildren().put(value, configTemplate);

					semanticTemplates.getChildren().put(semantic.getValue(), semanticTemplate);
				}
			}
			List<String> values = new ArrayList<String>(semanticTemplates.getChildren().size());
			for (String key : semanticTemplates.getChildren().keySet())
			{
				values.add(new String(key));
			}
			for (String key : values)
			{
				if (!_datatypeDesc.getChildren().get("aodl:semanticStm").getChildren().containsKey(key))
				{
					semanticTemplates.getChildren().remove(key);
				}
			}
			values = null;
		}
	}
	private void loadConfigData(ConfigTreeNode tn)
	{
		if (tn != null)
		{
			if (_markupTemplateEditor != null && _markupTemplateEditor.isEditableObject(tn.getConfigData()))
			{
				_rightComposite = _markupTemplateEditor.loadEditor(_treeViewer, _rightComposite, tn.getConfigData(),
						_datatypeDesc.getProvider());
			}
			else
			{

				loadValues(tn.getConfigData());
			}
		}
		else
		{
			// System.out.println("load null");
			_rightComposite.dispose();
			_rightComposite = new Composite(_mainSashForm, SWT.NONE);
			_rightComposite.setLayoutData(new GridData());
			((GridData) _rightComposite.getLayoutData()).grabExcessHorizontalSpace = true;
			((GridData) _rightComposite.getLayoutData()).horizontalAlignment = SWT.FILL;
			((GridData) _rightComposite.getLayoutData()).grabExcessVerticalSpace = true;
			((GridData) _rightComposite.getLayoutData()).verticalAlignment = SWT.FILL;
			_rightComposite.setLayout(new GridLayout());
			((GridLayout) _rightComposite.getLayout()).numColumns = 3;
			((GridLayout) _rightComposite.getLayout()).makeColumnsEqualWidth = false;
			_rightComposite.layout();
			_mainSashForm.layout();
		}

	}

	/**
	 * create ok button.
	 * @param parent parent composite
	 * @param id button id
	 * @param label button label
	 * @param defaultButton is default
	 * @return button
	 */
	protected final Button createOkButton(final Composite parent, final int id, final String label,
			final boolean defaultButton)
	{
		// increment the number of columns in the button bar
		((GridLayout) parent.getLayout()).numColumns++;
		Button button = new Button(parent, SWT.PUSH);
		button.setText(label);
		button.setFont(JFaceResources.getDialogFont());
		button.setData(new Integer(id));
		button.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent event)
			{
				Object[] objects = _treeViewer.getExpandedElements();
				_treeViewer.setInput(_datatypeDesc);
				for (Object o : objects)
				{
					_treeViewer.setExpandedState(o, true);
				}
			}
		});

		setButtonLayoutData(button);
		return button;
	}

	// /** meth checks whether entry contains illegal empty space.
	// *
	// * @param addEl
	// * @return returns true if no empty space
	// */
	// private boolean isValid(String addEl) {
	//			if(addEl.contains(" ")){ //$NON-NLS-1$
	// //TODO ErrorMessage
	// return false;
	// }else{
	// return true;
	// }
	// }
	// TODO buttons aufr�umen. mommentan wird direkt durch hinzuf�gen ein
	// eintrag gespeichert und in liste zur�ckgeschrieben.
	// speicher und cancel button sind also nicht korrekt.
	// ein schlie�en button reicht aus.

	/**
	 * create save button.
	 * @param parent parent composite
	 * @param id button id
	 * @param label button label
	 * @param b boolean is default button
	 * @return button
	 */
	protected final Button createSaveButton(final Composite parent, final int id, final String label, final boolean b)
	{
		((GridLayout) parent.getLayout()).numColumns++;
		Button button = new Button(parent, SWT.PUSH);
		button.setText(label);
		button.setFont(JFaceResources.getDialogFont());
		button.setData(new Integer(id));
		button.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent event)
			{
				if (isValidInput())
				{
					okPressed();
				}
			}
		});
		if (b)
		{
			Shell shell = parent.getShell();
			if (shell != null)
			{
				shell.setDefaultButton(button);
			}
		}
		setButtonLayoutData(button);
		return button;

	}

	@Override
	protected final boolean isResizable()
	{
		return true;
	}

	// TODO validierung einrichten
	/**
	 * checks if input is valid.
	 * @return is valid.
	 */
	private boolean isValidInput()
	{
		boolean valid = true;

		return valid;
	}

	/**
	 * open currently selected configdata and loads it values.
	 * @param c selected configdata to be opened.
	 */
	protected void loadValues(final ConfigData c)
	{
		_systemLang = AEConstants.getCurrentLocale().getLanguage();
		if (_rightComposite != null)
		{
			_rightComposite.dispose();
		}
		_rightComposite = new Composite(_mainSashForm, SWT.NONE);
		_rightComposite.setLayoutData(new GridData());
		((GridData) _rightComposite.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) _rightComposite.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _rightComposite.getLayoutData()).grabExcessVerticalSpace = true;
		((GridData) _rightComposite.getLayoutData()).verticalAlignment = SWT.FILL;
		_rightComposite.setLayout(new GridLayout());
		((GridLayout) _rightComposite.getLayout()).numColumns = 4;
		((GridLayout) _rightComposite.getLayout()).makeColumnsEqualWidth = false;

		Label label = new Label(_rightComposite, SWT.NONE);
		label.setText(NLMessages.getString("Config_label"));
		label.setLayoutData(new GridData());
		((GridData) label.getLayoutData()).horizontalSpan = 1;
		((GridData) label.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) label.getLayoutData()).horizontalAlignment = SWT.FILL;

		_labelText = new Text(_rightComposite, SWT.BORDER);
		_labelText.setEditable(_userRichtsChecker.mayEditConfig());
		_labelText.setLayoutData(new GridData());
		final ControlDecoration labelDeco = new ControlDecoration(_labelText, SWT.LEFT | SWT.TOP);
		((GridData) _labelText.getLayoutData()).horizontalSpan = 3;
		((GridData) _labelText.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) _labelText.getLayoutData()).horizontalAlignment = SWT.FILL;
		if (c.getLabel() != null)
		{
			_labelText.setText(c.getLabel());
		}
		else
		{
			_labelText.setText(c.getValue());
		}
		// _labelText.addFocusListener(new FocusAdapter()
		// {
		// @Override
		// public void focusLost(final FocusEvent e)
		// {
		// c.setLabel(_labelText.getText().trim());
		// // if (c instanceof ConfigItem)
		// // {
		// // ((ConfigItem) c).setLabel(labelText.getText().trim());
		// // }
		// }
		// });
		_labelText.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				String name = _labelText.getText().trim();
				if (name.trim().length() > 0)
				{
					boolean found = false;
	
					for (ConfigData cd : _configFacade.getConfigs().get(_semanticProvider).getChildren().values())
					{
						if (cd.getLabel() != null && cd.getLabel().equals(name))
						{
							found = true;
							break;
						}
					}
					if (found)
					{
						labelDeco.setImage(FieldDecorationRegistry.getDefault()
								.getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).getImage());
						setMessage("Warning. A Markup Element with the same label exists already!");
					}
					else
					{
						setMessage("");
						labelDeco.setImage(null);
						IStructuredSelection selection = (IStructuredSelection) _treeViewer.getSelection();
						Object obj = selection.getFirstElement();
						ConfigTreeNode tn = (ConfigTreeNode) obj;
						c.setLabel(name.trim());
						tn.setLabel(c.getLabel());
						_treeViewer.update(tn, null);
					}
				}
				else
				{
					setMessage("Warning. Label must consist of at least one character.");
					labelDeco.setImage(FieldDecorationRegistry.getDefault()
							.getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).getImage());
				}
				
			}
		});

		Label rightTitel = new Label(_rightComposite, SWT.NONE);
		rightTitel.setText(NLMessages.getString("Config_value"));
		rightTitel.setLayoutData(new GridData());
		((GridData) rightTitel.getLayoutData()).horizontalSpan = 1;
		((GridData) rightTitel.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) rightTitel.getLayoutData()).horizontalAlignment = SWT.FILL;

		_name = new Text(_rightComposite, SWT.BORDER);
		if (((c instanceof ConfigItem) && _userRichtsChecker.mayEditConfig())
				&& (!((ConfigItem) c).isMandatory() || _userRichtsChecker.mayModifyMandatoryConfig()))
		{
			_name.setEditable(true);
		}
		else
		{
			_name.setEditable(false);
		}

		_name.setLayoutData(new GridData());
		((GridData) _name.getLayoutData()).horizontalSpan = 3;
		((GridData) _name.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) _name.getLayoutData()).horizontalAlignment = SWT.FILL;
		if (c.getValue() != null)
		{
			_name.setText(c.getValue());

		}
		_name.addFocusListener(new FocusAdapter()
		{
			@Override
			public void focusLost(final FocusEvent e)
			{
				if (c instanceof ConfigItem)
				{
					((ConfigItem) c).setValue(_name.getText().trim());
				}
			}
		});
		_name.addModifyListener(new ModifyListener()
		{

			@Override
			public void modifyText(ModifyEvent e)
			{
				String name = _name.getText().trim();
				if (name.trim().length() > 0)
				{
					if (c.getParent() != null && c.getParent().getChildren() != null
							&& c.getParent().getChildren().containsKey(name)
							&& !c.getParent().getChildren().get(name).equals(c))
					{
						labelDeco.setImage(FieldDecorationRegistry.getDefault()
								.getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).getImage());
						setMessage("Warning. A Markup Element with the same value exists already!");
					}
					else
					{
						setMessage("");
						labelDeco.setImage(null);
						String oldValue = new String(c.getValue());
						c.setValue(name.trim());
						if (!oldValue.equals(name) && c.getParent() != null && c.getParent().getChildren() != null)
						{
							c.getParent().getChildren().put(name, c);
							c.getParent().getChildren().remove(oldValue);
						}
					}
				}
				else
				{
					setMessage("Warning. Value must consist of at least one character.");
					labelDeco.setImage(FieldDecorationRegistry.getDefault()
							.getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).getImage());
				}

			}
		});

		Label pos = new Label(_rightComposite, SWT.NONE);
		pos.setText(NLMessages.getString("Config_position"));
		pos.setLayoutData(new GridData());
		((GridData) pos.getLayoutData()).horizontalSpan = 1;
		((GridData) pos.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) pos.getLayoutData()).horizontalAlignment = SWT.FILL;
		pos.pack();

		_posText = new Text(_rightComposite, SWT.NONE | SWT.READ_ONLY);
		_posText.setLayoutData(new GridData());
		_posText.setEditable(false);
		((GridData) _posText.getLayoutData()).horizontalSpan = 3;
		((GridData) _posText.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) _posText.getLayoutData()).horizontalAlignment = SWT.FILL;

		if (c instanceof ConfigItem && c.getParent() != null && c.getParent().getValue().equals("aodl:semanticStm")) //$NON-NLS-1$
		{
			DataType dt = (DataType) (((ConfigItem) c).getParent());
			final DatatypeDesc dtd = dt.getDatatypeDesc();
			if (dtd.getUsage() != null && dtd.getUsage().getUsageDisplay() != null
					&& dtd.getUsage().getUsageDisplay().getPersonNameTag() != null
					&& dtd.getUsage().getUsageDisplay().getPersonNameTag().contains(c.getValue()))
			{
				Label name = new Label(_rightComposite, SWT.NONE);
				name.setText(NLMessages.getString("Config_name_of_person"));
				name.setLayoutData(new GridData());
				((GridData) name.getLayoutData()).horizontalSpan = 1;

				if (_datatypeDesc.getUsage().getUsageDisplay().getPersonNormNameTag() != null
						&& _datatypeDesc.getUsage().getUsageDisplay().getPersonNormNameTag().contains(c.getValue()))
				{
					Label displayName = new Label(_rightComposite, SWT.NONE);
					displayName.setText(NLMessages.getString("Config_displayname_of_person"));
					displayName.setLayoutData(new GridData());
					((GridData) displayName.getLayoutData()).horizontalSpan = 3;

				}
				else
				{
					Label blanc = new Label(_rightComposite, SWT.NONE);
					blanc.setText(""); //$NON-NLS-1$
					blanc.setLayoutData(new GridData());
					((GridData) blanc.getLayoutData()).horizontalSpan = 2;

				}
			}
		}

		Label base = new Label(_rightComposite, SWT.NONE);
		base.setText(NLMessages.getString("Config_priority"));
		base.setLayoutData(new GridData());
		((GridData) base.getLayoutData()).horizontalSpan = 1;
		((GridData) base.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) base.getLayoutData()).horizontalAlignment = SWT.FILL;
		base.pack();

		_baseSpinner = new Spinner(_rightComposite, SWT.NONE);
		_baseSpinner.setLayoutData(new GridData());
		_baseSpinner.setEnabled(_userRichtsChecker.mayEditConfig());
		((GridData) _baseSpinner.getLayoutData()).horizontalSpan = 1;
		((GridData) _baseSpinner.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) _baseSpinner.getLayoutData()).horizontalAlignment = SWT.FILL;

		_baseSpinner.setMinimum(0);
		_baseSpinner.setMaximum(999);

		if (c instanceof ConfigItem)
		{
			ConfigItem ci = (ConfigItem) c;
			if (ci.getPos() != null)
			{
				_posText.setText(ci.getPos());
			}
			_baseSpinner.setSelection(ci.getPriority());

		}
		_baseSpinner.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent se)
			{

				c.setPriority(_baseSpinner.getSelection());
				if (((ConfigItem) c).getParent().getValue().equals("aodl:semanticStm"))
				{
					if (_datatypeDesc.getUsage().getTemplates() != null
							&& _datatypeDesc.getUsage().getTemplates().getChildren().containsKey("aspectTemplates")
							&& _datatypeDesc.getUsage().getTemplates().getChildren().get("aspectTemplates")
									.getChildren().containsKey("semanticTemplates")
							&& _datatypeDesc.getUsage().getTemplates().getChildren().get("aspectTemplates")
									.getChildren().get("semanticTemplates").getChildren().containsKey(c.getValue()))
					{
						ConfigData ci = _datatypeDesc.getUsage().getTemplates().getChildren().get("aspectTemplates")
								.getChildren().get("semanticTemplates").getChildren().get(c.getValue());
						ci.setPriority(_baseSpinner.getSelection());
					}
				}
			}
		}); // SelectionListener

		Button sortButton = new Button(_rightComposite, SWT.PUSH);
		sortButton.setLayoutData(new GridData());
		sortButton.setText("Sort Children");
		sortButton.setImage(_imageReg.get(IconsInternal.SORT_ALPHABETIC_DESC));
		sortButton.setEnabled(_userRichtsChecker.mayEditConfig() && c.getChildren() != null);
		((GridData) sortButton.getLayoutData()).horizontalSpan = 2;

		sortButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent se)
			{
				sortChildren(c);
				Object[] objects = _treeViewer.getExpandedElements();
				_treeViewer.setInput(_datatypeDesc);
				for (Object o : objects)
				{
					_treeViewer.setExpandedState(o, true);
				}
			}
		}); // SelectionListener



			Label ignoreLabel = new Label(_rightComposite, SWT.NONE);
			ignoreLabel.setText(NLMessages.getString("Config_ignore"));
			ignoreLabel.setLayoutData(new GridData());
			((GridData) ignoreLabel.getLayoutData()).horizontalSpan = 1;
			((GridData) ignoreLabel.getLayoutData()).grabExcessHorizontalSpace = true;
			((GridData) ignoreLabel.getLayoutData()).horizontalAlignment = SWT.FILL;
			ignoreLabel.pack();

			_ignoreButton = new Button(_rightComposite, SWT.CHECK);
			_ignoreButton.setLayoutData(new GridData());
			_ignoreButton.setEnabled(_userRichtsChecker.maySetConfigIgnored());
			((GridData) _ignoreButton.getLayoutData()).horizontalSpan = 1;
		_ignoreButton.setSelection(c.isIgnore());

			_ignoreButton.addSelectionListener(new SelectionAdapter()
			{
				@Override
				public void widgetSelected(final SelectionEvent se)
				{
				((ConfigItem) c).setIgnore(_ignoreButton.getSelection());
				processIgnored((ConfigItem) c);
				Object[] objects = _treeViewer.getExpandedElements();
				_treeViewer.setInput(_datatypeDesc);
				for (Object o : objects)
					{
					_treeViewer.setExpandedState(o, true);
					}

				}
			}); // SelectionListener
		if (c instanceof ConfigItem)
		{
			if (_userRichtsChecker.mayModifyMandatoryConfig())
			{
				Label mandatoryLabel = new Label(_rightComposite, SWT.NONE);
				mandatoryLabel.setText(NLMessages.getString("Config_mandatory"));
				mandatoryLabel.setLayoutData(new GridData());
				((GridData) mandatoryLabel.getLayoutData()).horizontalSpan = 1;
				((GridData) mandatoryLabel.getLayoutData()).grabExcessHorizontalSpace = true;
				((GridData) mandatoryLabel.getLayoutData()).horizontalAlignment = SWT.FILL;
				mandatoryLabel.pack();

				final Button mandatoryButton = new Button(_rightComposite, SWT.CHECK);
				mandatoryButton.setLayoutData(new GridData());
				mandatoryButton.setEnabled(true);
				((GridData) mandatoryButton.getLayoutData()).horizontalSpan = 1;
				ConfigItem ci = (ConfigItem) c;
				mandatoryButton.setSelection(ci.isMandatory());

				mandatoryButton.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(final SelectionEvent se)
					{
						if (c instanceof ConfigItem)
						{
							((ConfigItem) c).setMandatory(mandatoryButton.getSelection());
							Object[] objects = _treeViewer.getExpandedElements();
							_treeViewer.setInput(_datatypeDesc);
							for (Object o : objects)
							{
								_treeViewer.setExpandedState(o, true);
							}
						}

					}
				}); // SelectionListener
			}
		}
		else
		{
			Label blancLable = new Label(_rightComposite, SWT.NONE);
			blancLable.setText(""); //$NON-NLS-1$
			blancLable.setLayoutData(new GridData());
			((GridData) blancLable.getLayoutData()).horizontalSpan = 2;
		}

		if (c instanceof IdentifierConfig)
		{
			final IdentifierConfig ci = (IdentifierConfig) c;
			Label urlL = new Label(_rightComposite, SWT.NONE);
			urlL.setText("URL");
			urlL.setLayoutData(new GridData());
			((GridData) urlL.getLayoutData()).horizontalSpan = 1;
			((GridData) urlL.getLayoutData()).grabExcessHorizontalSpace = true;
			((GridData) urlL.getLayoutData()).horizontalAlignment = SWT.FILL;

			final Text urlT = new Text(_rightComposite, SWT.BORDER);
			urlT.setEditable(_userRichtsChecker.mayEditConfig());
			urlT.setLayoutData(new GridData());
			((GridData) urlT.getLayoutData()).horizontalSpan = 3;
			((GridData) urlT.getLayoutData()).grabExcessHorizontalSpace = true;
			((GridData) urlT.getLayoutData()).horizontalAlignment = SWT.FILL;
			if (ci.getUrl() != null)
			{
				urlT.setText(ci.getUrl());
			}
			urlT.addFocusListener(new FocusAdapter()
			{
				@Override
				public void focusLost(final FocusEvent e)
				{
					ci.setUrl(urlT.getText().trim());
					// if (c instanceof ConfigItem)
					// {
					// ((ConfigItem) c).setLabel(labelText.getText().trim());
					// }
				}
			});

			Label prefixL = new Label(_rightComposite, SWT.NONE);
			prefixL.setText("Prefix");
			prefixL.setLayoutData(new GridData());
			((GridData) prefixL.getLayoutData()).horizontalSpan = 1;
			((GridData) prefixL.getLayoutData()).grabExcessHorizontalSpace = true;
			((GridData) prefixL.getLayoutData()).horizontalAlignment = SWT.FILL;

			final Text prefixT = new Text(_rightComposite, SWT.BORDER);
			prefixT.setEditable(_userRichtsChecker.mayEditConfig());
			prefixT.setLayoutData(new GridData());
			((GridData) prefixT.getLayoutData()).horizontalSpan = 3;
			((GridData) prefixT.getLayoutData()).grabExcessHorizontalSpace = true;
			((GridData) prefixT.getLayoutData()).horizontalAlignment = SWT.FILL;
			if (ci.getPrefix() != null)
			{
				prefixT.setText(ci.getPrefix());
			}
			prefixT.addFocusListener(new FocusAdapter()
			{
				@Override
				public void focusLost(final FocusEvent e)
				{
					ci.setPrefix(prefixT.getText().trim());
				}
			});

			Label suffixL = new Label(_rightComposite, SWT.NONE);
			suffixL.setText("Suffix");
			suffixL.setLayoutData(new GridData());
			((GridData) suffixL.getLayoutData()).horizontalSpan = 1;
			((GridData) suffixL.getLayoutData()).grabExcessHorizontalSpace = true;
			((GridData) suffixL.getLayoutData()).horizontalAlignment = SWT.FILL;

			final Text suffixT = new Text(_rightComposite, SWT.BORDER);
			suffixT.setEditable(_userRichtsChecker.mayEditConfig());
			suffixT.setLayoutData(new GridData());
			((GridData) suffixT.getLayoutData()).horizontalSpan = 3;
			((GridData) suffixT.getLayoutData()).grabExcessHorizontalSpace = true;
			((GridData) suffixT.getLayoutData()).horizontalAlignment = SWT.FILL;
			if (ci.getSuffix() != null)
			{
				suffixT.setText(ci.getSuffix());
			}
			suffixT.addFocusListener(new FocusAdapter()
			{
				@Override
				public void focusLost(final FocusEvent e)
				{
					ci.setSuffix(suffixT.getText().trim());
				}
			});

			Label regexL = new Label(_rightComposite, SWT.NONE);
			regexL.setText("Regex");
			regexL.setLayoutData(new GridData());
			((GridData) regexL.getLayoutData()).horizontalSpan = 1;
			((GridData) regexL.getLayoutData()).grabExcessHorizontalSpace = true;
			((GridData) regexL.getLayoutData()).horizontalAlignment = SWT.FILL;

			final Text regexT = new Text(_rightComposite, SWT.BORDER);
			regexT.setEditable(_userRichtsChecker.mayEditConfig());
			regexT.setLayoutData(new GridData());
			((GridData) regexT.getLayoutData()).horizontalSpan = 3;
			((GridData) regexT.getLayoutData()).grabExcessHorizontalSpace = true;
			((GridData) regexT.getLayoutData()).horizontalAlignment = SWT.FILL;
			if (ci.getRegex() != null)
			{
				regexT.setText(ci.getRegex());
			}
			regexT.addFocusListener(new FocusAdapter()
			{
				@Override
				public void focusLost(final FocusEvent e)
				{
					ci.setRegex(regexT.getText().trim());
				}
			});
		}

		Label langLabel = new Label(_rightComposite, SWT.NONE);
		langLabel.setText(NLMessages.getString("Config_language"));
		langLabel.setLayoutData(new GridData());
		((GridData) langLabel.getLayoutData()).horizontalSpan = 1;

		_langCombo = new Combo(_rightComposite, SWT.NONE | SWT.READ_ONLY);
		_langCombo.setLayoutData(new GridData());
		((GridData) _langCombo.getLayoutData()).horizontalSpan = 1;
		((GridData) _langCombo.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) _langCombo.getLayoutData()).horizontalAlignment = SWT.FILL;
		_langCombo.setItems(_langs);

		Label docu = new Label(_rightComposite, SWT.NONE);
		docu.setText(NLMessages.getString("Config_documentation"));
		docu.setLayoutData(new GridData());
		((GridData) docu.getLayoutData()).horizontalSpan = 2;

		_docuText = new Text(_rightComposite, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
		_docuText.setLayoutData(new GridData());
		_docuText.setEditable(_userRichtsChecker.mayEditConfig());
		_docuText.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		((GridData) _docuText.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _docuText.getLayoutData()).verticalAlignment = SWT.FILL;
		((GridData) _docuText.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) _docuText.getLayoutData()).grabExcessVerticalSpace = true;

		if (c.getDocumentation() != null && c.getDocumentation().containsKey(_systemLang))
		{
			_lang = _systemLang;
			_langCombo.select(_langCombo.indexOf((_systemLang)));
			_docuText.setText(c.getDocumentation().get((_systemLang)));

		}
		else if (c.getDocumentation() != null && c.getDocumentation().get("de") != null) //$NON-NLS-1$
		{
			_lang = "de"; //$NON-NLS-1$
			_langCombo.select(_langCombo.indexOf((_lang)));
			_docuText.setText(c.getDocumentation().get((_lang)));

		}
		else if (c.getDocumentation() != null && c.getDocumentation().get("en") != null) //$NON-NLS-1$
		{
			_lang = "en"; //$NON-NLS-1$

			_langCombo.select(_langCombo.indexOf((_lang)));
			_docuText.setText(c.getDocumentation().get((_lang)));

		}
		else if (c.getDocumentation() != null && c.getDocumentation().get("it") != null) //$NON-NLS-1$
		{
			_lang = "it"; //$NON-NLS-1$

			_langCombo.select(_langCombo.indexOf((_lang)));
			_docuText.setText(c.getDocumentation().get((_lang)));

		}
		else if (c.getDocumentation() != null && c.getDocumentation().get("fr") != null) //$NON-NLS-1$
		{
			_lang = "fr"; //$NON-NLS-1$

			_langCombo.select(_langCombo.indexOf((_lang)));
			_docuText.setText(c.getDocumentation().get((_lang)));

		}
		else
		{
			_langCombo.select(_langCombo.indexOf((_systemLang)));
		}

		_langCombo.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent se)
			{

				_lang = _langCombo.getItem(_langCombo.getSelectionIndex());
				if (c.getDocumentation() != null && c.getDocumentation().containsKey(_lang))
				{
					_docuText.setText(c.getDocumentation().get((_lang)));
				}
				else
				{
					_docuText.setText(""); //$NON-NLS-1$
				}

			}
		}); // SelectionListener

		((GridData) _docuText.getLayoutData()).horizontalSpan = 4;
		((GridData) _docuText.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) _docuText.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _docuText.getLayoutData()).minimumHeight = 60;

		//

		if (c.getDocumentation() != null && c.getDocumentation().containsKey("de")) //$NON-NLS-1$
		{
			_docuText.setText(c.getDocumentation().get(("de"))); //$NON-NLS-1$
		}
		else
		{
			_docuText.setText(""); //$NON-NLS-1$
		}
		_docuText.addFocusListener(new FocusAdapter()
		{
			@Override
			public void focusLost(final FocusEvent e)
			{
				// System.out.println(" ################# lang " + lang +
				// " docu " + docuText.getText());
				c.getDocumentation().put(_lang, _docuText.getText());
			}
		});

		if (c instanceof ConfigItem && ((ConfigItem) c).getParent().getValue().equals("aodl:semanticStm"))
		{
			SemanticTemplate semTemplateHelp = null;
			if (_datatypeDesc.getUsage().getTemplates() != null && _datatypeDesc.getUsage().getTemplates().getChildren() != null)
			{
				ConfigData cd = _datatypeDesc.getUsage().getTemplates().getChildren().get("aspectTemplates");
				if (cd == null)
				{
					cd = new ConfigData();
					_datatypeDesc.getUsage().getTemplates().getChildren().put("aspectTemplates", cd);
				}
				ConfigData cd2 = cd.getChildren().get("semanticTemplates");
				if (cd2 == null)
				{
					cd2 = new ConfigData();
					cd.getChildren().put("semanticTemplates", cd2);
				}
				semTemplateHelp = (SemanticTemplate) cd2.getChildren().get(c.getValue());
				if (semTemplateHelp == null)
				{
					semTemplateHelp = new SemanticTemplate(c.getValue());
					cd2.getChildren().put(c.getValue(), semTemplateHelp);
				}
			}


			final SemanticTemplate semTemplate = semTemplateHelp;
			// template Text
			Button templateIgnoreButton = new Button(_rightComposite, SWT.CHECK);
			templateIgnoreButton.setLayoutData(new GridData());
			templateIgnoreButton.setText(NLMessages.getString("Config_use_Template"));
			templateIgnoreButton.setEnabled(_userRichtsChecker.maySetConfigIgnored());
			templateIgnoreButton.setSelection(!semTemplate.isIgnoreTemplateText());

			((GridData) templateIgnoreButton.getLayoutData()).horizontalSpan = 2;
			templateIgnoreButton.addSelectionListener(new SelectionAdapter()
			{
				@Override
				public void widgetSelected(final SelectionEvent se)
				{
					semTemplate.setIgnoreTemplateText(!semTemplate.isIgnoreTemplateText());
					_semanticAspectTemplateText.setEnabled(!semTemplate.isIgnoreTemplateText()
							&& _userRichtsChecker.mayEditConfig());
				}
			}); // SelectionListener

			Label template = new Label(_rightComposite, SWT.NONE);
			template.setText(NLMessages.getString("Config_Notification_Template"));
			template.setLayoutData(new GridData());
			((GridData) template.getLayoutData()).horizontalSpan = 2;

			_semanticAspectTemplateText = new Text(_rightComposite, SWT.WRAP | SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
			_semanticAspectTemplateText.setLayoutData(new GridData());
			_semanticAspectTemplateText.setEnabled(!semTemplate.isIgnoreTemplateText()
					&& _userRichtsChecker.mayEditConfig());
			_semanticAspectTemplateText.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
			((GridData) _semanticAspectTemplateText.getLayoutData()).horizontalAlignment = SWT.FILL;
			((GridData) _semanticAspectTemplateText.getLayoutData()).verticalAlignment = SWT.FILL;
			((GridData) _semanticAspectTemplateText.getLayoutData()).grabExcessHorizontalSpace = true;
			((GridData) _semanticAspectTemplateText.getLayoutData()).grabExcessVerticalSpace = true;
			((GridData) _semanticAspectTemplateText.getLayoutData()).horizontalSpan = 4;

			if (semTemplate.getTemplateText() != null)
			{
				_semanticAspectTemplateText.setText(semTemplate.getTemplateText());
			}

			_semanticAspectTemplateText.addFocusListener(new FocusAdapter()
			{
				@Override
				public void focusLost(FocusEvent e)
				{
					semTemplate.setTemplateText(_semanticAspectTemplateText.getText());

				}
			});
			

		}

		if (c.isMyHaveChildren())
		{
			Label l = new Label(_rightComposite, SWT.NONE);
			l.setText(NLMessages.getString("Config_new_delete"));
			l.setLayoutData(new GridData());

			_addElement = new Text(_rightComposite, SWT.BORDER);
			_addElement.setEditable(_userRichtsChecker.mayEditConfig());

			_addElement.setLayoutData(new GridData());
			((GridData) _addElement.getLayoutData()).grabExcessHorizontalSpace = true;
			((GridData) _addElement.getLayoutData()).horizontalAlignment = SWT.FILL;
			((GridData) _addElement.getLayoutData()).horizontalSpan = 2;
			_addElement.addModifyListener(new ModifyListener()
			{

				@Override
				public void modifyText(ModifyEvent e)
				{
					String name = _addElement.getText().trim();
					if (name.length() > 0)
					{
						if (c.getChildren() != null && c.getChildren().containsKey(name))
						{
							_addToList.setEnabled(false);
							setMessage("Warning. A Markup Element with the same name exists already!");
						}
						else
						{
							setMessage("");
							_addToList.setEnabled(_userRichtsChecker.mayEditConfig());
						}
					}
					else
					{
						setMessage("");
						_addToList.setEnabled(false);
					}

				}
			});
			_addElement.addKeyListener(new KeyListener()
			{

				@Override
				public void keyPressed(final KeyEvent e)
				{
					if (e.keyCode == SWT.CR && _addElement.getText().trim().length() > 0)
					{
						addElementToListe(c);
					}

				}

				@Override
				public void keyReleased(final KeyEvent e)
				{
					// TODO Auto-generated method stub

				}
			});

			_addToList = new Button(_rightComposite, SWT.PUSH | SWT.END);
			_addToList.setEnabled(_userRichtsChecker.mayEditConfig());

			_addToList.setText(NLMessages.getString("Config_add"));
			_addToList.setToolTipText(NLMessages.getString("Config_add_tooltip"));

			_addToList.setLayoutData(new GridData());
			((GridData) _addToList.getLayoutData()).verticalAlignment = SWT.FILL;
			_addElement.setEnabled(true);
			_addToList.addSelectionListener(new SelectionAdapter()
			{
				@Override
				public void widgetSelected(final SelectionEvent e)
				{
					addElementToListe(c);

				}


			});

		}
		_deleteFromList = new Button(_rightComposite, SWT.PUSH | SWT.END);
		_deleteFromList.setText(NLMessages.getString("Config_delete_entry"));
		_deleteFromList.setToolTipText(NLMessages.getString("Config_delete_entry_tooltip"));
		_deleteFromList.setLayoutData(new GridData());
		// deleteFromList.setEnabled(false);

		
		if (((c instanceof ConfigItem ) && _userRichtsChecker.mayEditConfig())
				&& (!c.isMandatory() || _userRichtsChecker.mayModifyMandatoryConfig()))
		{
			_deleteFromList.setEnabled(true);
		} else 
 if (c instanceof IdentifierConfig && _userRichtsChecker.mayEditConfig())
		{
				_deleteFromList.setEnabled(true);
		}
		else
		{
			_deleteFromList.setEnabled(false);
		}

		// ((GridData) editListGroup.getLayoutData()).END ;
		// ((GridData) loadRelationList.getLayoutData()).verticalAlignment =
		// SWT.FILL;
		_deleteFromList.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent e)
			{

				if (c instanceof ConfigItem || c instanceof IdentifierConfig)
				{
					IStructuredSelection selection = (IStructuredSelection) _treeViewer.getSelection();
					Object obj = selection.getFirstElement();
					ConfigTreeNode parent = ((ConfigTreeNode) obj).getParent();
					((ConfigData) c).remove();
					
					
					_treeViewer.setSelection(new StructuredSelection(parent));

				} 				
				
				Object[] objects = _treeViewer.getExpandedElements();
				_treeViewer.setInput(_datatypeDesc);
				for (Object o : objects)
				{
					_treeViewer.setExpandedState(o, true);
				}
			}
		});
		if (c instanceof ConfigItem && ((ConfigItem) c).getParent().getValue().equals("aodl:semanticStm")) //$NON-NLS-1$
		{
			// name.setEditable(false);
			DataType dt = (DataType) (((ConfigItem) c).getParent());
			final DatatypeDesc dtd = dt.getDatatypeDesc();
			_setAsPersonName = new Button(_rightComposite, SWT.PUSH | SWT.END);
			_setAsPersonName.setText(NLMessages.getString("Config_as_person_name"));
			_setAsPersonName.setToolTipText(NLMessages.getString("Config_as_person_name_tooltip"));
			_setAsPersonName.setLayoutData(new GridData());
			// setAsPersonName.setEnabled(false);

			_setAsPersonName.setEnabled((c instanceof ConfigItem) && _userRichtsChecker.mayEditConfig());
			_setAsPersonName.addSelectionListener(new SelectionAdapter()
			{
				@Override
				public void widgetSelected(final SelectionEvent e)
				{
					_setAsNormName.setEnabled(_userRichtsChecker.mayEditConfig());
					dtd.getUsage().getUsageDisplay().getPersonNameTag().add(c.getValue());
					loadValues(c);

				}

			});
			_setAsNormName = new Button(_rightComposite, SWT.PUSH | SWT.END);
			_setAsNormName.setText(NLMessages.getString("Config_as_display_name"));
			_setAsNormName.setToolTipText(NLMessages.getString("Config_as_display_name_tooltip"));
			_setAsNormName.setLayoutData(new GridData());
			_setAsNormName.setEnabled(false);
			if (dtd.getUsage() != null && dtd.getUsage().getUsageDisplay() != null
					&& dtd.getUsage().getUsageDisplay().getPersonNameTag() != null
					&& dtd.getUsage().getUsageDisplay().getPersonNameTag().contains(c.getValue()))
			{
				_setAsNormName.setEnabled(_userRichtsChecker.mayEditConfig());
			}

			_setAsNormName.addSelectionListener(new SelectionAdapter()
			{
				@Override
				public void widgetSelected(final SelectionEvent e)
				{
					DataType dt = (DataType) (((ConfigItem) c).getParent());
					dt.getDatatypeDesc().getUsage().getUsageDisplay().getPersonNormNameTag().add(c.getValue());

					loadValues(c);

				}

			});
			Button removeUsageSettings = new Button(_rightComposite, SWT.PUSH | SWT.END);
			removeUsageSettings.setText(NLMessages.getString("Config_remove_person_name_settting"));
			removeUsageSettings.setToolTipText(NLMessages.getString("Config_remove_person_name_settting_tooltip"));
			removeUsageSettings.setLayoutData(new GridData());
			((GridData) removeUsageSettings.getLayoutData()).horizontalSpan = 2;

			removeUsageSettings.setEnabled(false);
			if (dtd.getUsage() != null && dtd.getUsage().getUsageDisplay() != null
					&& dtd.getUsage().getUsageDisplay().getPersonNameTag() != null
					&& dtd.getUsage().getUsageDisplay().getPersonNameTag().contains(c.getValue()))
			{
				_setAsPersonName.setEnabled(false);

				if (dtd.getUsage().getUsageDisplay().getPersonNormNameTag().contains(c.getValue()))
				{
					_setAsNormName.setEnabled(false);
				}
				if (dtd.getUsage().getUsageDisplay().getPersonNameTag().size() > 2
						&& dtd.getUsage().getUsageDisplay().getPersonNormNameTag().size() > 1)
				{
					removeUsageSettings.setEnabled(_userRichtsChecker.mayEditConfig());
				}
				else
				{
					removeUsageSettings.setEnabled(false);
					_deleteFromList.setEnabled(false);
				}
			}

			removeUsageSettings.addSelectionListener(new SelectionAdapter()
			{
				@Override
				public void widgetSelected(final SelectionEvent e)
				{
					dtd.getUsage().getUsageDisplay().getPersonNameTag().remove(c.getValue());
					dtd.getUsage().getUsageDisplay().getPersonNormNameTag().remove(c.getValue());

					loadValues(c);

				}

			});
		}
		_rightComposite.layout();
		_mainSashForm.setWeights(new int[]
		{3, 3});
		_mainSashForm.layout();
		// mainSashForm.pack();
	}

	private void addElementToListe(ConfigData c)
	{
		String addEl = _addElement.getText().trim();
		if (c.getPos() != null)
		{
			if (c.getPos().equals("personIdentifiers"))
			{
				IdentifierConfig ic = new IdentifierConfig();
				ic.setPos("personIdentifier");
				ic.setMyHaveChildren(false);
				ic.setValue(addEl);
				ic.setLabel(addEl);
				ic.setParent(c);
				ic.setPriority(c.getChildren().size() + 1);
				if (addEl.trim().length() > 0)
				{
					c.getChildren().put(addEl, ic);
					Object[] objects = _treeViewer.getExpandedElements();
					ISelection iSelection = _treeViewer.getSelection();
					Object obj = ((IStructuredSelection) iSelection).getFirstElement();
					_treeViewer.setInput(_datatypeDesc);
					for (Object o : objects)
					{
						_treeViewer.setExpandedState(o, true);
					}
					_treeViewer.setExpandedState(obj, true);
					_addElement.setText("");
				}
			}
			else
			{
				ConfigItem ci = new ConfigItem();
				ci.setValue(addEl);
				ci.setLabel(addEl);
				ci.setParent(c);
				if (c.getPos().startsWith("list")) //$NON-NLS-1$
				{
					ci.setPos(("_TEXTNODE")); //$NON-NLS-1$
					ci.setMyHaveChildren(false);
				}
				else if (c.getValue().equals("aodl:semanticStm")) //$NON-NLS-1$
				{
					ci.setPos(("_TEXTNODE")); //$NON-NLS-1$
					ci.setMyHaveChildren(false);
				}
				else if (c.getValue().equals("aodl:relation")) //$NON-NLS-1$
				{
					ci.setPos(("context")); //$NON-NLS-1$
					ci.setMyHaveChildren(true);
				}
				else if (c.getValue().startsWith("aodl")) //$NON-NLS-1$
				{
					ci.setPos(("type")); //$NON-NLS-1$
					ci.setMyHaveChildren(true);
				}
				else if (c.getPos().equals("type")) //$NON-NLS-1$
				{
					ci.setPos(("subtype")); //$NON-NLS-1$
					ci.setMyHaveChildren(true);
				}
				else if (c.getPos().equals("subtype")) //$NON-NLS-1$
				{
					ci.setPos(("role")); //$NON-NLS-1$
					ci.setMyHaveChildren(false);
				}
				else if (c.getPos().equals("context")) //$NON-NLS-1$
				{
					ci.setPos(("class")); //$NON-NLS-1$
					ci.setMyHaveChildren(true);
				}
				else if (c.getPos().equals("class")) //$NON-NLS-1$
				{
					ci.setPos(("_TEXTNODE")); //$NON-NLS-1$
					ci.setMyHaveChildren(false);
				}
				else if (c.getPos().equals("_TEXTNODE")) //$NON-NLS-1$
				{
					ci.setPos(("_TEXTNODE")); //$NON-NLS-1$
					ci.setMyHaveChildren(false);
				}
				ci.setPriority(c.getChildren().size() + 1);
				if (addEl.trim().length() > 0)
				{
					c.getChildren().put(addEl, ci);
					Object[] objects = _treeViewer.getExpandedElements();
					ISelection iSelection = _treeViewer.getSelection();
					Object obj = ((IStructuredSelection) iSelection).getFirstElement();
					_treeViewer.setInput(_datatypeDesc);
					for (Object o : objects)
					{
						_treeViewer.setExpandedState(o, true);
					}
					_treeViewer.setExpandedState(obj, true);
					_addElement.setText("");
				}
			}
		}
	}
	@Override
	protected final void okPressed()
	{
		saveInput();
		super.okPressed();
	}

	// We allow the user to resize this dialog

	/**
	 * process ignored state of config items after the have been moved.
	 * @param c config item
	 */
	protected final void processIgnored(final ConfigItem c)
	{
		if (c.getChildren() != null)
		{
			for (String key : c.getChildren().keySet())
			{
				ConfigItem ci = (ConfigItem) c.getChildren().get(key);
				ci.setIgnore(c.isIgnore());
				processIgnored(ci);
			}
		}

	}

	/**
	 * save input to data store handling.
	 */
	private void saveInput()
	{
		if (_datatypeDesc != null)
		{

			try
			{
				_cfgManager.saveConfig(_datatypeDesc);
			}
			catch (XMLStreamException e)
			{
				e.printStackTrace();
			}
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	/**
	 * set new configuration.
	 * @param newConfiguration new configuration.
	 */
	public final void setNewConfiguration(final DatatypeDesc newConfiguration)
	{
		this._newConfiguration = newConfiguration;
	}

	/**
	 * sort children of given configData alphabetically.
	 * @param c configData whoms children shall be sorted.
	 */
	protected final void sortChildren(final ConfigData c)
	{
		if (c.getChildren() != null && !c.getChildren().isEmpty())
		{
			ArrayList<ConfigData> list = new ArrayList<ConfigData>(c.getChildren().values());
			Collections.sort(list, new ConfigDataComparator(false));
			int i = 0;
			for (ConfigData cd : list)
			{
				// System.out.println("key " + cd.getLabel() + " priority " +
				// i);
				cd.setPriority(i);
				i = i + 2;
			}

		}

	}

	// //////////////////////// Observer ///////////////////////////////////

	@Override
	public void update(final Observable arg0, final Object arg1)
	{
	}

	public TreeViewer getTreeViewer()
	{
		return _treeViewer;
	}

	public void setMessagePublic(String message)
	{
		setMessage(message);
	}

}
