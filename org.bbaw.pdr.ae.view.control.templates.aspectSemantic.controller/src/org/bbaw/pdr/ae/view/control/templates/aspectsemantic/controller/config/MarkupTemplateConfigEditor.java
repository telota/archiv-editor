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
package org.bbaw.pdr.ae.view.control.templates.aspectsemantic.controller.config;

import java.util.HashMap;
import java.util.Map.Entry;

import org.bbaw.pdr.ae.common.AEConstants;
import org.bbaw.pdr.ae.common.NLMessages;
import org.bbaw.pdr.ae.config.core.ConfigFactory;
import org.bbaw.pdr.ae.config.core.IAEMarkupTemplateConfigEditor;
import org.bbaw.pdr.ae.config.core.IConfigFacade;
import org.bbaw.pdr.ae.config.core.IConfigRightsChecker;
import org.bbaw.pdr.ae.config.model.AspectConfigTemplate;
import org.bbaw.pdr.ae.config.model.ComplexSemanticTemplate;
import org.bbaw.pdr.ae.config.model.ConfigData;
import org.bbaw.pdr.ae.config.model.ConfigItem;
import org.bbaw.pdr.ae.config.model.ConfigTreeNode;
import org.bbaw.pdr.ae.config.model.SemanticTemplate;
import org.bbaw.pdr.ae.control.core.PDRConfigProvider;
import org.bbaw.pdr.ae.control.facade.Facade;
import org.bbaw.pdr.ae.metamodel.IAEPresentable;
import org.bbaw.pdr.ae.view.control.ViewHelper;
import org.bbaw.pdr.ae.view.control.customSWTWidges.AEAspectWidgetCustomizable;
import org.bbaw.pdr.ae.view.control.provider.MarkupContentProvider;
import org.bbaw.pdr.ae.view.control.provider.MarkupLabelProvider;
import org.bbaw.pdr.ae.view.control.templates.aspectsemantic.controller.internal.AspectTemplatePreviewDialog;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

public class MarkupTemplateConfigEditor implements IAEMarkupTemplateConfigEditor
{


	private Text _labelText;
	/** singleton instance of facade. */
	private IConfigFacade _configFacade = ConfigFactory.getConfigFacade();
	/** user rights checker. */
	private IConfigRightsChecker _userRichtsChecker = _configFacade.getConfigRichtsChecker();
	private Text _name;
	private Combo _widgetTypeCombo;

	private Button _addMarkupTemplate;
	private String[] _widgetTypes = new String[]
	{"TEXT", "COMBO", "DATE", "DATE_COMBO", "RELATION", "RELATION_DEFINED", "TIME_STM", "REFERENCE"};
	private Spinner _baseSpinner;
	private Text _addElement;
	private TreeViewer _treeViewer;

	/** available languages. */
	private String[] _langs =
	{"de", "en", "it", "fr"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	private String _systemLang = AEConstants.getCurrentLocale().getLanguage();;
	private String _lang;
	private Facade _facade = Facade.getInstanz();
	private String _provider;

	/** The WHIT e_ color. */
	private static final Color WHITE_COLOR = Display.getDefault().getSystemColor(SWT.COLOR_WHITE);
	private Composite _innerComp;
	private boolean _loading;
	private Button _deleteFromList;
	private AEAspectWidgetCustomizable _prevWidget;
	@Override
	public int open()
	{
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public void setMarkupConfigTemplate(AspectConfigTemplate aspectConfigTemplate)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public Composite loadEditor(TreeViewer treeViewer, Composite composite, final ConfigData configData, String provider)
	{
		_treeViewer = treeViewer;
		_provider = provider.toUpperCase();
		for (Control c : composite.getChildren())
		{
			c.dispose();
		}
		if (configData.getValue().equals("aspectTemplates"))
		{
			return createAspectTemplateEditor(composite, configData);
		}
		else if (configData.getValue().equals("complexTemplates"))
		{
			return createComplexTemplateEditor(composite, configData);
		}
		else if (configData.getValue().equals("semanticTemplates"))
		{
			return createSemanticTemplatesEditor(composite, configData);
		}
		else if (configData instanceof SemanticTemplate)
		{
			return createEditor(composite, (SemanticTemplate) configData);
		}
		else if (configData instanceof AspectConfigTemplate)
		{
			return createEditor(composite, (AspectConfigTemplate) configData);
		}
		else if (configData instanceof ComplexSemanticTemplate)
		{
			return createComplexSemanticTemplateEditor(composite, (ComplexSemanticTemplate) configData);
		}
		else
		{
			return composite;
		}

	}

	private Composite createSemanticTemplatesEditor(Composite composite, final ConfigData configData)
	{
		composite.setLayoutData(new GridData());
		((GridData) composite.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) composite.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) composite.getLayoutData()).grabExcessVerticalSpace = true;
		((GridData) composite.getLayoutData()).verticalAlignment = SWT.FILL;
		composite.setLayout(new GridLayout());
		((GridLayout) composite.getLayout()).numColumns = 4;
		((GridLayout) composite.getLayout()).makeColumnsEqualWidth = false;

		Label title = new Label(composite, SWT.NONE);
		title.setText(NLMessages.getString("Editor_Semantic_Templates"));
		title.setLayoutData(new GridData());
		((GridData) title.getLayoutData()).horizontalSpan = 4;
		((GridData) title.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) title.getLayoutData()).horizontalAlignment = SWT.FILL;

		Label ignoreLabel = new Label(composite, SWT.NONE);
		ignoreLabel.setText(NLMessages.getString("Config_ignore"));
		ignoreLabel.setLayoutData(new GridData());
		((GridData) ignoreLabel.getLayoutData()).horizontalSpan = 2;
		((GridData) ignoreLabel.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) ignoreLabel.getLayoutData()).horizontalAlignment = SWT.FILL;
		ignoreLabel.pack();

		final Button ignoreButton = new Button(composite, SWT.CHECK);
		ignoreButton.setLayoutData(new GridData());
		ignoreButton.setEnabled(_userRichtsChecker.maySetConfigIgnored());
		((GridData) ignoreButton.getLayoutData()).horizontalSpan = 2;
		ignoreButton.setSelection(configData.isIgnore());

		ignoreButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent se)
			{
				configData.setIgnore(ignoreButton.getSelection());
				Object[] objects = _treeViewer.getExpandedElements();
				Object input = _treeViewer.getInput();
				_treeViewer.setInput(input);
				for (Object o : objects)
				{
					_treeViewer.setExpandedState(o, true);
				}

			}
		}); // SelectionListener

		composite.layout();
		return composite;
	}

	private Composite createAspectTemplateEditor(Composite composite, final ConfigData configData)
	{
		composite.setLayoutData(new GridData());
		((GridData) composite.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) composite.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) composite.getLayoutData()).grabExcessVerticalSpace = true;
		((GridData) composite.getLayoutData()).verticalAlignment = SWT.FILL;
		composite.setLayout(new GridLayout());
		((GridLayout) composite.getLayout()).numColumns = 4;
		((GridLayout) composite.getLayout()).makeColumnsEqualWidth = false;

		Label title = new Label(composite, SWT.NONE);
		title.setText(NLMessages.getString("Editor_aspectTemplateEditor"));
		title.setLayoutData(new GridData());
		((GridData) title.getLayoutData()).horizontalSpan = 4;
		((GridData) title.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) title.getLayoutData()).horizontalAlignment = SWT.FILL;

		Button activateComplexTemplate = new Button(composite, SWT.PUSH | SWT.END);
		activateComplexTemplate.setEnabled(_userRichtsChecker.mayEditConfig()
				&& !configData.getChildren().containsKey("complexTemplates"));
		activateComplexTemplate.setText("Activate Complex Template");
		activateComplexTemplate.setLayoutData(new GridData());
		((GridData) activateComplexTemplate.getLayoutData()).verticalAlignment = SWT.FILL;
		activateComplexTemplate.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent e)
			{
				ConfigData complexTemplates = new ConfigData();
				complexTemplates.setValue("complexTemplates");
				complexTemplates.setLabel(NLMessages.getString("Editor_Complex_Templates"));
				configData.getChildren().put("complexTemplates", complexTemplates);
				complexTemplates.setPriority(configData.getChildren().size());
				Object[] objects = _treeViewer.getExpandedElements();
				Object input = _treeViewer.getInput();
				_treeViewer.setInput(input);
				for (Object o : objects)
				{
					_treeViewer.setExpandedState(o, true);
				}
			}
		});

		composite.layout();
		return composite;
	}

	private Composite createComplexTemplateEditor(Composite composite, final ConfigData configData)
	{
		composite.setLayoutData(new GridData());
		((GridData) composite.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) composite.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) composite.getLayoutData()).grabExcessVerticalSpace = true;
		((GridData) composite.getLayoutData()).verticalAlignment = SWT.FILL;
		composite.setLayout(new GridLayout());
		((GridLayout) composite.getLayout()).numColumns = 4;
		((GridLayout) composite.getLayout()).makeColumnsEqualWidth = false;

		Label title = new Label(composite, SWT.NONE);
		title.setText(NLMessages.getString("Editor_Complex_Templates"));
		title.setLayoutData(new GridData());
		((GridData) title.getLayoutData()).horizontalSpan = 4;
		((GridData) title.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) title.getLayoutData()).horizontalAlignment = SWT.FILL;


		Label ignoreLabel = new Label(composite, SWT.NONE);
		ignoreLabel.setText(NLMessages.getString("Config_ignore"));
		ignoreLabel.setLayoutData(new GridData());
		((GridData) ignoreLabel.getLayoutData()).horizontalSpan = 2;
		((GridData) ignoreLabel.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) ignoreLabel.getLayoutData()).horizontalAlignment = SWT.FILL;
		ignoreLabel.pack();

		final Button ignoreButton = new Button(composite, SWT.CHECK);
		ignoreButton.setLayoutData(new GridData());
		ignoreButton.setEnabled(_userRichtsChecker.maySetConfigIgnored());
		((GridData) ignoreButton.getLayoutData()).horizontalSpan = 2;
		ignoreButton.setSelection(configData.isIgnore());

		ignoreButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent se)
			{
				configData.setIgnore(ignoreButton.getSelection());
				Object[] objects = _treeViewer.getExpandedElements();
				Object input = _treeViewer.getInput();
				_treeViewer.setInput(input);
				for (Object o : objects)
				{
					_treeViewer.setExpandedState(o, true);
				}

			}
		}); // SelectionListener

		Label l = new Label(composite, SWT.NONE);
		l.setText(NLMessages.getString("Editor_add_complexTempalte"));
		l.setLayoutData(new GridData());

		_addElement = new Text(composite, SWT.BORDER);
		_addElement.setEditable(_userRichtsChecker.mayEditConfig());

		_addElement.setLayoutData(new GridData());
		((GridData) _addElement.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) _addElement.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _addElement.getLayoutData()).horizontalSpan = 2;
		_addElement.addKeyListener(new KeyListener()
		{

			@Override
			public void keyPressed(final KeyEvent e)
			{
				_addElement.setFocus();

				if (e.keyCode == SWT.CR && _addElement.getText().trim().length() > 0)
				{
					String value = _addElement.getText().trim();
					ComplexSemanticTemplate complexTemplate = new ComplexSemanticTemplate(value);
					complexTemplate.setLabel(value);
					complexTemplate.setParent(configData);
					configData.getChildren().put(value, complexTemplate);
					complexTemplate.setPriority(configData.getChildren().size());
					Object[] objects = _treeViewer.getExpandedElements();
					Object input = _treeViewer.getInput();

					IStructuredSelection selection = (IStructuredSelection) _treeViewer.getSelection();
					Object obj = selection.getFirstElement();
					_treeViewer.setInput(input);
					for (Object o : objects)
					{
						_treeViewer.setExpandedState(o, true);
					}
					_treeViewer.setExpandedState(obj, true);
				}
			}

			@Override
			public void keyReleased(final KeyEvent e)
			{
				// TODO Auto-generated method stub

			}
		});

		final Button addComplexTemplate = new Button(composite, SWT.PUSH | SWT.END);
		addComplexTemplate.setEnabled(_userRichtsChecker.mayEditConfig());
		addComplexTemplate.setText(NLMessages.getString("Editor_Complex_Templates"));
		addComplexTemplate.setLayoutData(new GridData());
		((GridData) addComplexTemplate.getLayoutData()).verticalAlignment = SWT.FILL;
		addComplexTemplate.setFocus();

		_addElement.addModifyListener(new ModifyListener()
		{

			@Override
			public void modifyText(ModifyEvent e)
			{
				String name = _addElement.getText().trim();
				if (name.trim().length() > 0)
				{
					if (configData.getChildren() != null && configData.getChildren().containsKey(name))
					{
						addComplexTemplate.setEnabled(false);
						// setMessage("Warning. A Markup Element with the same name exists already!");
					}
					else
					{
						addComplexTemplate.setEnabled(_userRichtsChecker.mayEditConfig());
					}
				}
				else
				{
					addComplexTemplate.setEnabled(false);
				}

			}
		});

		addComplexTemplate.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent e)
			{
				String value = _addElement.getText().trim();
				ComplexSemanticTemplate complexTemplate = new ComplexSemanticTemplate(value);
				complexTemplate.setLabel(value);
				complexTemplate.setParent(configData);
				configData.getChildren().put(value, complexTemplate);
				complexTemplate.setPriority(configData.getChildren().size());
				Object[] objects = _treeViewer.getExpandedElements();
				Object input = _treeViewer.getInput();
				IStructuredSelection selection = (IStructuredSelection) _treeViewer.getSelection();
				Object obj = selection.getFirstElement();
				_treeViewer.setInput(input);
				for (Object o : objects)
				{
					_treeViewer.setExpandedState(o, true);
				}
				_treeViewer.setExpandedState(obj, true);
			}
		});

		composite.layout();
		return composite;
	}

	private Composite createComplexSemanticTemplateEditor(Composite composite,
			final ComplexSemanticTemplate complexTemplate)
	{
		composite.setLayoutData(new GridData());
		((GridData) composite.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) composite.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) composite.getLayoutData()).grabExcessVerticalSpace = true;
		((GridData) composite.getLayoutData()).verticalAlignment = SWT.FILL;
		composite.setLayout(new GridLayout());
		((GridLayout) composite.getLayout()).numColumns = 4;
		((GridLayout) composite.getLayout()).makeColumnsEqualWidth = false;
		
		Label title = new Label(composite, SWT.NONE);
		title.setText(NLMessages.getString("Config_Complex_Semantic_Template_Editor"));
		title.setLayoutData(new GridData());
		((GridData) title.getLayoutData()).horizontalSpan = 4;
		((GridData) title.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) title.getLayoutData()).horizontalAlignment = SWT.FILL;

		Label label = new Label(composite, SWT.NONE);
		label.setText(NLMessages.getString("Config_label"));
		label.setLayoutData(new GridData());
		((GridData) label.getLayoutData()).horizontalSpan = 1;
		((GridData) label.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) label.getLayoutData()).horizontalAlignment = SWT.FILL;

		_labelText = new Text(composite, SWT.BORDER);
		_labelText.setEditable(_userRichtsChecker.mayEditConfig());
		_labelText.setLayoutData(new GridData());
		((GridData) _labelText.getLayoutData()).horizontalSpan = 3;
		((GridData) _labelText.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) _labelText.getLayoutData()).horizontalAlignment = SWT.FILL;
		if (complexTemplate.getLabel() != null)
		{
			_labelText.setText(complexTemplate.getLabel());
		}
		else
		{
			_labelText.setText(complexTemplate.getValue());
		}
		_labelText.addFocusListener(new FocusAdapter()
		{
			@Override
			public void focusLost(final FocusEvent e)
			{
				complexTemplate.setLabel(_labelText.getText().trim());
				// if (c instanceof ConfigItem)
				// {
				// ((ConfigItem) c).setLabel(labelText.getText().trim());
				// }
			}
		});
		_labelText.addModifyListener(new ModifyListener() {
					
					@Override
					public void modifyText(ModifyEvent e) {
						IStructuredSelection selection = (IStructuredSelection) _treeViewer.getSelection();
						Object obj = selection.getFirstElement();
						ConfigTreeNode tn = (ConfigTreeNode) obj;
						complexTemplate.setLabel(_labelText.getText().trim());
						tn.setLabel(complexTemplate.getLabel());
						_treeViewer.update(tn, null);
						
					}
				});

		Label ignoreLabel = new Label(composite, SWT.NONE);
		ignoreLabel.setText(NLMessages.getString("Config_ignore"));
		ignoreLabel.setLayoutData(new GridData());
		((GridData) ignoreLabel.getLayoutData()).horizontalSpan = 2;
		((GridData) ignoreLabel.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) ignoreLabel.getLayoutData()).horizontalAlignment = SWT.FILL;
		ignoreLabel.pack();

		final Button ignoreButton = new Button(composite, SWT.CHECK);
		ignoreButton.setLayoutData(new GridData());
		ignoreButton.setEnabled(_userRichtsChecker.maySetConfigIgnored());
		((GridData) ignoreButton.getLayoutData()).horizontalSpan = 2;
		ignoreButton.setSelection(complexTemplate.isIgnore());

		ignoreButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent se)
			{
				complexTemplate.setIgnore(ignoreButton.getSelection());
				Object[] objects = _treeViewer.getExpandedElements();
				Object input = _treeViewer.getInput();
				_treeViewer.setInput(input);
				for (Object o : objects)
				{
					_treeViewer.setExpandedState(o, true);
				}

			}
		}); // SelectionListener
		Label base = new Label(composite, SWT.NONE);
		base.setText(NLMessages.getString("Config_priority"));
		base.setLayoutData(new GridData());
		((GridData) base.getLayoutData()).horizontalSpan = 2;
		((GridData) base.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) base.getLayoutData()).horizontalAlignment = SWT.FILL;
		base.pack();

		_baseSpinner = new Spinner(composite, SWT.NONE);
		_baseSpinner.setLayoutData(new GridData());
		_baseSpinner.setEnabled(_userRichtsChecker.mayEditConfig());
		((GridData) _baseSpinner.getLayoutData()).horizontalSpan = 2;
		((GridData) _baseSpinner.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) _baseSpinner.getLayoutData()).horizontalAlignment = SWT.FILL;

		_baseSpinner.setMinimum(0);
		_baseSpinner.setMaximum(999);

		_baseSpinner.setSelection(complexTemplate.getPriority());
		_baseSpinner.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent se)
			{

				complexTemplate.setPriority(_baseSpinner.getSelection());

			}
		}); // SelectionListener
		Label l = new Label(composite, SWT.NONE);
		l.setText(NLMessages.getString("Config_new_Semantic_Template"));
		l.setLayoutData(new GridData());

		Combo semanticTemplatCombo = new Combo(composite, SWT.READ_ONLY);
		semanticTemplatCombo.setEnabled(_userRichtsChecker.mayEditConfig());
		semanticTemplatCombo.setLayoutData(new GridData());
		((GridData) semanticTemplatCombo.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) semanticTemplatCombo.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) semanticTemplatCombo.getLayoutData()).horizontalSpan = 2;

		final ComboViewer semanticTemplatComboViewer = new ComboViewer(semanticTemplatCombo);
		semanticTemplatComboViewer.setContentProvider(new MarkupContentProvider());
		semanticTemplatComboViewer.setLabelProvider(new MarkupLabelProvider());

		semanticTemplatComboViewer.setInput(_facade.getConfigs().get(_provider).getChildren().get("aodl:semanticStm")
				.getChildren());
		semanticTemplatComboViewer.addSelectionChangedListener(new ISelectionChangedListener()
		{
			@Override
			public void selectionChanged(final SelectionChangedEvent event)
			{
				ISelection iSelection = event.getSelection();
				Object obj = ((IStructuredSelection) iSelection).getFirstElement();
				IAEPresentable cp = (IAEPresentable) obj;
				if (cp != null)
				{
					String name = cp.getValue().trim();
					if (name.length() > 0)
					{
						if (complexTemplate.getChildren() != null && complexTemplate.getChildren().containsKey(name))
						{
							_addMarkupTemplate.setEnabled(false);
							// setMessage("Warning. A Markup Element with the same name exists already!");
						}
						else
						{
							_addMarkupTemplate.setEnabled(_userRichtsChecker.mayEditConfig());
						}
					}
					else
					{
						_addMarkupTemplate.setEnabled(false);
					}
				}

			}

		});


		_addMarkupTemplate = new Button(composite, SWT.PUSH | SWT.END);
		_addMarkupTemplate.setEnabled(false);
		_addMarkupTemplate.setText(NLMessages.getString("Config_add"));
		_addMarkupTemplate.setLayoutData(new GridData());
		((GridData) _addMarkupTemplate.getLayoutData()).verticalAlignment = SWT.FILL;
		_addMarkupTemplate.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent e)
			{
				ISelection iSelection = semanticTemplatComboViewer.getSelection();
				Object obj = ((IStructuredSelection) iSelection).getFirstElement();
				IAEPresentable cp = (IAEPresentable) obj;
				if (cp != null)
				{
					String value = cp.getValue().trim();

					SemanticTemplate semanticTemplate = new SemanticTemplate(value);
					semanticTemplate.setLabel(cp.getLabel());
					semanticTemplate.setParent(complexTemplate);

					String val = NLMessages.getString("Dialog_reference");
					AspectConfigTemplate configTemplate = new AspectConfigTemplate(val);
					configTemplate.setParent(semanticTemplate);
					configTemplate.setPriority(25);
					configTemplate.setHorizontalSpan(4);
					configTemplate.setRequired(true);
					configTemplate.setWidgetType(7);
					semanticTemplate.getChildren().put(val, configTemplate);

					complexTemplate.getChildren().put(value, semanticTemplate);
					semanticTemplate.setPriority(complexTemplate.getChildren().size());
					Object[] objects = _treeViewer.getExpandedElements();
					Object input = _treeViewer.getInput();

					ISelection iSel = _treeViewer.getSelection();
					Object selObj = ((IStructuredSelection) iSel).getFirstElement();
					_treeViewer.setInput(input);
					for (Object o : objects)
					{
						_treeViewer.setExpandedState(o, true);
					}
					_treeViewer.setExpandedState(selObj, true);
				}
			}
		});
		Label langLabel = new Label(composite, SWT.NONE);
		langLabel.setText(NLMessages.getString("Config_language"));
		langLabel.setLayoutData(new GridData());
		((GridData) langLabel.getLayoutData()).horizontalSpan = 1;

		final Combo langCombo = new Combo(composite, SWT.NONE | SWT.READ_ONLY);
		langCombo.setLayoutData(new GridData());
		((GridData) langCombo.getLayoutData()).horizontalSpan = 1;
		((GridData) langCombo.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) langCombo.getLayoutData()).horizontalAlignment = SWT.FILL;
		langCombo.setItems(_langs);

		Label docu = new Label(composite, SWT.NONE);
		docu.setText(NLMessages.getString("Config_documentation"));
		docu.setLayoutData(new GridData());
		((GridData) docu.getLayoutData()).horizontalSpan = 2;

		final Text docuText = new Text(composite, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
		docuText.setLayoutData(new GridData());
		docuText.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		((GridData) docuText.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) docuText.getLayoutData()).verticalAlignment = SWT.FILL;
		((GridData) docuText.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) docuText.getLayoutData()).grabExcessVerticalSpace = true;

		if (complexTemplate.getDocumentation() != null
				&& complexTemplate.getDocumentation().containsKey(_systemLang))
		{
			_lang = _systemLang;
			langCombo.select(langCombo.indexOf((_systemLang)));
			docuText.setText(complexTemplate.getDocumentation().get((_systemLang)));

		}
		else if (complexTemplate.getDocumentation() != null
				&& complexTemplate.getDocumentation().get("de") != null) //$NON-NLS-1$
		{
			_lang = "de"; //$NON-NLS-1$
			langCombo.select(langCombo.indexOf((_lang)));
			docuText.setText(complexTemplate.getDocumentation().get((_lang)));

		}
		else if (complexTemplate.getDocumentation() != null
				&& complexTemplate.getDocumentation().get("en") != null) //$NON-NLS-1$
		{
			_lang = "en"; //$NON-NLS-1$

			langCombo.select(langCombo.indexOf((_lang)));
			docuText.setText(complexTemplate.getDocumentation().get((_lang)));

		}
		else if (complexTemplate.getDocumentation() != null
				&& complexTemplate.getDocumentation().get("it") != null) //$NON-NLS-1$
		{
			_lang = "it"; //$NON-NLS-1$

			langCombo.select(langCombo.indexOf((_lang)));
			docuText.setText(complexTemplate.getDocumentation().get((_lang)));

		}
		else if (complexTemplate.getDocumentation() != null
				&& complexTemplate.getDocumentation().get("fr") != null) //$NON-NLS-1$
		{
			_lang = "fr"; //$NON-NLS-1$

			langCombo.select(langCombo.indexOf((_lang)));
			docuText.setText(complexTemplate.getDocumentation().get((_lang)));

		}
		else
		{
			langCombo.select(langCombo.indexOf((_systemLang)));
			_lang = _systemLang;
		}

		langCombo.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent se)
			{

				_lang = langCombo.getItem(langCombo.getSelectionIndex());
				if (complexTemplate.getDocumentation() != null
						&& complexTemplate.getDocumentation().containsKey(_lang))
				{
					docuText.setText(complexTemplate.getDocumentation().get((_lang)));
				}
				else
				{
					docuText.setText(""); //$NON-NLS-1$
				}

			}
		}); // SelectionListener
		if (complexTemplate.getDocumentation() != null
				&& complexTemplate.getDocumentation().containsKey("de")) //$NON-NLS-1$
		{
			docuText.setText(complexTemplate.getDocumentation().get(("de"))); //$NON-NLS-1$
		}
		else
		{
			docuText.setText(""); //$NON-NLS-1$
		}
		docuText.addFocusListener(new FocusAdapter()
		{
			@Override
			public void focusLost(final FocusEvent e)
			{
				complexTemplate.getDocumentation().put(_lang, docuText.getText());
			}
		});
		((GridData) docuText.getLayoutData()).horizontalSpan = 4;
		((GridData) docuText.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) docuText.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) docuText.getLayoutData()).minimumHeight = 60;

		final Button previewButton = new Button(composite, SWT.PUSH);
		previewButton.setText(NLMessages.getString("Config_Preview"));
		previewButton.setLayoutData(new GridData());
		((GridData) previewButton.getLayoutData()).horizontalSpan = 2;
		((GridData) previewButton.getLayoutData()).verticalAlignment = SWT.FILL;

		previewButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent se)
			{
				IWorkbench workbench = PlatformUI.getWorkbench();
				Display display = workbench.getDisplay();
				Shell shell = new Shell(display);
				AspectTemplatePreviewDialog dialog = new AspectTemplatePreviewDialog(shell, complexTemplate);
				dialog.open();

			}
		}); // SelectionListener

		_deleteFromList = new Button(composite, SWT.PUSH | SWT.END);
		_deleteFromList.setText(NLMessages.getString("Config_delete_entry"));
		_deleteFromList.setToolTipText(NLMessages.getString("Config_delete_entry_tooltip"));
		_deleteFromList.setLayoutData(new GridData());
		// deleteFromList.setEnabled(false);

		if (_userRichtsChecker.mayEditConfig())
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
				delete(complexTemplate);
			}

		});
		composite.layout();
		return composite;
	}

	private Composite createEditor(Composite composite, final AspectConfigTemplate aspectConfigTemplate)
	{
		composite.setLayoutData(new GridData());
		((GridData) composite.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) composite.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) composite.getLayoutData()).grabExcessVerticalSpace = true;
		((GridData) composite.getLayoutData()).verticalAlignment = SWT.FILL;
		composite.setLayout(new GridLayout());
		((GridLayout) composite.getLayout()).numColumns = 4;
		((GridLayout) composite.getLayout()).makeColumnsEqualWidth = false;

		Label label = new Label(composite, SWT.NONE);
		label.setText(NLMessages.getString("Config_label"));
		label.setLayoutData(new GridData());
		((GridData) label.getLayoutData()).horizontalSpan = 1;
		((GridData) label.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) label.getLayoutData()).horizontalAlignment = SWT.FILL;

		_labelText = new Text(composite, SWT.BORDER);
		_labelText.setEditable(_userRichtsChecker.mayEditConfig());
		_labelText.setLayoutData(new GridData());
		((GridData) _labelText.getLayoutData()).horizontalSpan = 1;
		((GridData) _labelText.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) _labelText.getLayoutData()).horizontalAlignment = SWT.FILL;
		if (aspectConfigTemplate.getLabel() != null)
		{
			_labelText.setText(aspectConfigTemplate.getLabel());
		}
		else
		{
			_labelText.setText(aspectConfigTemplate.getValue());
		}
		_labelText.addFocusListener(new FocusAdapter()
		{
			@Override
			public void focusLost(final FocusEvent e)
			{
				aspectConfigTemplate.setLabel(_labelText.getText().trim());
				// if (c instanceof ConfigItem)
				// {
				// ((ConfigItem) c).setLabel(labelText.getText().trim());
				// }
			}
		});

		_labelText.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				IStructuredSelection selection = (IStructuredSelection) _treeViewer.getSelection();
				Object obj = selection.getFirstElement();
				ConfigTreeNode tn = (ConfigTreeNode) obj;
				aspectConfigTemplate.setLabel(_labelText.getText().trim());
				tn.setLabel(aspectConfigTemplate.getLabel());
				_treeViewer.update(tn, null);
				
			}
		});

		Label base = new Label(composite, SWT.NONE);
		base.setText(NLMessages.getString("Config_priority"));
		base.setLayoutData(new GridData());
		((GridData) base.getLayoutData()).horizontalSpan = 1;
		((GridData) base.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) base.getLayoutData()).horizontalAlignment = SWT.FILL;
		base.pack();

		_baseSpinner = new Spinner(composite, SWT.NONE);
		_baseSpinner.setLayoutData(new GridData());
		_baseSpinner.setEnabled(_userRichtsChecker.mayEditConfig());
		((GridData) _baseSpinner.getLayoutData()).horizontalSpan = 1;
		((GridData) _baseSpinner.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) _baseSpinner.getLayoutData()).horizontalAlignment = SWT.FILL;

		_baseSpinner.setMinimum(0);
		_baseSpinner.setMaximum(999);

		_baseSpinner.setSelection(aspectConfigTemplate.getPriority());
		_baseSpinner.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent se)
			{

				aspectConfigTemplate.setPriority(_baseSpinner.getSelection());
				

			}
		}); // SelectionListener

		Label pos = new Label(composite, SWT.NONE);
		pos.setText(NLMessages.getString("Config_widget_type"));
		pos.setLayoutData(new GridData());
		((GridData) pos.getLayoutData()).horizontalSpan = 1;
		((GridData) pos.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) pos.getLayoutData()).horizontalAlignment = SWT.FILL;
		pos.pack();

		_widgetTypeCombo = new Combo(composite, SWT.NONE | SWT.READ_ONLY);
		_widgetTypeCombo.setLayoutData(new GridData());
		((GridData) _widgetTypeCombo.getLayoutData()).horizontalSpan = 1;
		((GridData) _widgetTypeCombo.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) _widgetTypeCombo.getLayoutData()).horizontalAlignment = SWT.FILL;
		_widgetTypeCombo.setItems(_widgetTypes);
		_widgetTypeCombo.select(aspectConfigTemplate.getWidgetType());
		_widgetTypeCombo.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent se)
			{
				if (aspectConfigTemplate.getWidgetType() != _widgetTypeCombo.getSelectionIndex())
				{
					aspectConfigTemplate.setWidgetType(_widgetTypeCombo.getSelectionIndex());
					loadIndividualSettings(aspectConfigTemplate, _innerComp);
				}
			}
		}); // SelectionListener
		Label ignoreLabel = new Label(composite, SWT.NONE);
		ignoreLabel.setText(NLMessages.getString("Config_ignore"));
		ignoreLabel.setLayoutData(new GridData());
		((GridData) ignoreLabel.getLayoutData()).horizontalSpan = 1;
		((GridData) ignoreLabel.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) ignoreLabel.getLayoutData()).horizontalAlignment = SWT.FILL;
		ignoreLabel.pack();

		final Button ignoreButton = new Button(composite, SWT.CHECK);
		ignoreButton.setLayoutData(new GridData());
		((GridData) ignoreButton.getLayoutData()).horizontalSpan = 1;
		ignoreButton.setSelection(aspectConfigTemplate.isIgnore());

		ignoreButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent se)
			{
				aspectConfigTemplate.setIgnore(ignoreButton.getSelection());
				Object[] objects = _treeViewer.getExpandedElements();
				Object input = _treeViewer.getInput();
				_treeViewer.setInput(input);
				for (Object o : objects)
				{
					_treeViewer.setExpandedState(o, true);
				}

			}
		}); // SelectionListener

		Label requiredLabel = new Label(composite, SWT.NONE);
		requiredLabel.setText(NLMessages.getString("Config_Required"));
		requiredLabel.setLayoutData(new GridData());
		((GridData) requiredLabel.getLayoutData()).horizontalSpan = 1;
		((GridData) requiredLabel.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) requiredLabel.getLayoutData()).horizontalAlignment = SWT.FILL;
		requiredLabel.pack();

		final Button requiredButton = new Button(composite, SWT.CHECK);
		requiredButton.setLayoutData(new GridData());
		((GridData) requiredButton.getLayoutData()).horizontalSpan = 1;
		requiredButton.setSelection(aspectConfigTemplate.isRequired());

		requiredButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent se)
			{
				aspectConfigTemplate.setRequired(requiredButton.getSelection());
			}
		}); // SelectionListene

		Label allowMultipleLabel = new Label(composite, SWT.NONE);
		allowMultipleLabel.setText(NLMessages.getString("Config_allowMultiple"));
		allowMultipleLabel.setLayoutData(new GridData());
		((GridData) allowMultipleLabel.getLayoutData()).horizontalSpan = 1;
		((GridData) allowMultipleLabel.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) allowMultipleLabel.getLayoutData()).horizontalAlignment = SWT.FILL;
		allowMultipleLabel.pack();

		final Button allowMultipleButton = new Button(composite, SWT.CHECK);
		allowMultipleButton.setLayoutData(new GridData());
		((GridData) allowMultipleButton.getLayoutData()).horizontalSpan = 1;
		allowMultipleButton.setSelection(aspectConfigTemplate.isAllowMultiple());

		allowMultipleButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent se)
			{
				aspectConfigTemplate.setAllowMultiple(allowMultipleButton.getSelection());
			}
		}); // SelectionListene

		_innerComp = new Composite(composite, SWT.NONE);
		_innerComp.setLayoutData(new GridData());
		((GridData) _innerComp.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) _innerComp.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _innerComp.getLayoutData()).grabExcessVerticalSpace = true;
		((GridData) _innerComp.getLayoutData()).verticalAlignment = SWT.FILL;
		((GridData) _innerComp.getLayoutData()).horizontalSpan = 4;

		_innerComp.setLayout(new GridLayout());
		((GridLayout) _innerComp.getLayout()).numColumns = 4;
		((GridLayout) _innerComp.getLayout()).marginHeight = 0;
		((GridLayout) _innerComp.getLayout()).marginWidth = 0;
		((GridLayout) composite.getLayout()).makeColumnsEqualWidth = false;

		loadIndividualSettings(aspectConfigTemplate, _innerComp);
		

		

		// layout



		Label langLabel = new Label(composite, SWT.NONE);
		langLabel.setText(NLMessages.getString("Config_language"));
		langLabel.setLayoutData(new GridData());
		((GridData) langLabel.getLayoutData()).horizontalSpan = 1;

		final Combo langCombo = new Combo(composite, SWT.NONE | SWT.READ_ONLY);
		langCombo.setLayoutData(new GridData());
		((GridData) langCombo.getLayoutData()).horizontalSpan = 1;
		((GridData) langCombo.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) langCombo.getLayoutData()).horizontalAlignment = SWT.FILL;
		langCombo.setItems(_langs);

		Label docu = new Label(composite, SWT.NONE);
		docu.setText(NLMessages.getString("Config_documentation"));
		docu.setLayoutData(new GridData());
		((GridData) docu.getLayoutData()).horizontalSpan = 2;

		final Text docuText = new Text(composite, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
		docuText.setLayoutData(new GridData());
		docuText.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		((GridData) docuText.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) docuText.getLayoutData()).verticalAlignment = SWT.FILL;
		((GridData) docuText.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) docuText.getLayoutData()).grabExcessVerticalSpace = true;

		if (aspectConfigTemplate.getDocumentation() != null
				&& aspectConfigTemplate.getDocumentation().containsKey(_systemLang))
		{
			_lang = _systemLang;
			langCombo.select(langCombo.indexOf((_systemLang)));
			docuText.setText(aspectConfigTemplate.getDocumentation().get((_systemLang)));

		}
		else if (aspectConfigTemplate.getDocumentation() != null
				&& aspectConfigTemplate.getDocumentation().get("de") != null) //$NON-NLS-1$
		{
			_lang = "de"; //$NON-NLS-1$
			langCombo.select(langCombo.indexOf((_lang)));
			docuText.setText(aspectConfigTemplate.getDocumentation().get((_lang)));

		}
		else if (aspectConfigTemplate.getDocumentation() != null
				&& aspectConfigTemplate.getDocumentation().get("en") != null) //$NON-NLS-1$
		{
			_lang = "en"; //$NON-NLS-1$

			langCombo.select(langCombo.indexOf((_lang)));
			docuText.setText(aspectConfigTemplate.getDocumentation().get((_lang)));

		}
		else if (aspectConfigTemplate.getDocumentation() != null
				&& aspectConfigTemplate.getDocumentation().get("it") != null) //$NON-NLS-1$
		{
			_lang = "it"; //$NON-NLS-1$

			langCombo.select(langCombo.indexOf((_lang)));
			docuText.setText(aspectConfigTemplate.getDocumentation().get((_lang)));

		}
		else if (aspectConfigTemplate.getDocumentation() != null
				&& aspectConfigTemplate.getDocumentation().get("fr") != null) //$NON-NLS-1$
		{
			_lang = "fr"; //$NON-NLS-1$

			langCombo.select(langCombo.indexOf((_lang)));
			docuText.setText(aspectConfigTemplate.getDocumentation().get((_lang)));

		}
		else
		{
			langCombo.select(langCombo.indexOf((_systemLang)));
			_lang = _systemLang;
		}

		langCombo.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent se)
			{

				_lang = langCombo.getItem(langCombo.getSelectionIndex());
				if (aspectConfigTemplate.getDocumentation() != null
						&& aspectConfigTemplate.getDocumentation().containsKey(_lang))
				{
					docuText.setText(aspectConfigTemplate.getDocumentation().get((_lang)));
				}
				else
				{
					docuText.setText(""); //$NON-NLS-1$
				}

			}
		}); // SelectionListener

		((GridData) docuText.getLayoutData()).horizontalSpan = 4;
		((GridData) docuText.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) docuText.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) docuText.getLayoutData()).minimumHeight = 60;

		//

		if (aspectConfigTemplate.getDocumentation() != null
				&& aspectConfigTemplate.getDocumentation().containsKey("de")) //$NON-NLS-1$
		{
			docuText.setText(aspectConfigTemplate.getDocumentation().get(("de"))); //$NON-NLS-1$
		}
		else
		{
			docuText.setText(""); //$NON-NLS-1$
		}
		docuText.addFocusListener(new FocusAdapter()
		{
			@Override
			public void focusLost(final FocusEvent e)
			{
				// System.out.println(" ################# lang " + _lang +
				// " docu " + docuText.getText());
				aspectConfigTemplate.getDocumentation().put(_lang, docuText.getText());
			}
		});

		_deleteFromList = new Button(composite, SWT.PUSH | SWT.END);
		_deleteFromList.setText(NLMessages.getString("Config_delete_entry"));
		_deleteFromList.setToolTipText(NLMessages.getString("Config_delete_entry_tooltip"));
		_deleteFromList.setLayoutData(new GridData());
		// deleteFromList.setEnabled(false);

		if (_userRichtsChecker.mayEditConfig())
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
				delete(aspectConfigTemplate);

			}

		});
		composite.layout();
		return composite;
	}

	private void loadIndividualSettings(final AspectConfigTemplate aspectConfigTemplate, Composite composite)
	{
		_prevWidget = null;
		for (Control c : composite.getChildren())
		{
			c.dispose();
		}
		boolean pathB = false;
		boolean combo = false;
		boolean date = false;
		boolean levelSpanB = false;
		boolean relation = false;
		boolean preSuffix = false;
		boolean horizontalSpan = false;
		
		switch (aspectConfigTemplate.getWidgetType())
		{
			case 0: //text
				pathB = true;
				combo = false;
				date = false;
				levelSpanB = false;
				relation = false;
				preSuffix = true;
				horizontalSpan = true;
				break;
			case 1: //combo
				pathB = false;
				combo = true;
				date = false;
				levelSpanB = true;
				relation = false;
				preSuffix = true;
				horizontalSpan = true;
				break;
			case 2: //date
				pathB = true;
				combo = false;
				date = true;
				levelSpanB = false;
				relation = false;
				preSuffix = true;
				horizontalSpan = false;
				break;
			case 3: // date_combo
				pathB = false;
				combo = true;
				date = true;
				levelSpanB = true;
				relation = false;
				preSuffix = true;
				horizontalSpan = false;
				break;
			case 4: // relation
				pathB = false;
				combo = false;
				date = false;
				levelSpanB = false;
				relation = false;
				preSuffix = false;
				horizontalSpan = false;
				break;
			case 5: // relation_defined
				pathB = false;
				combo = false;
				date = false;
				levelSpanB = false;
				relation = true;
				preSuffix = false;
				horizontalSpan = true;
				break;
			case 6: // timeStm
				pathB = false;
				combo = false;
				date = true;
				levelSpanB = false;
				relation = false;
				preSuffix = false;
				horizontalSpan = false;
				break;
			case 7: // reference
				pathB = false;
				combo = false;
				date = false;
				levelSpanB = false;
				relation = false;
				preSuffix = false;
				horizontalSpan = false;
				break;
			default:
				break;
		}
		final boolean pathCombo = combo;
		
		// path
			SelectionListener aspectFacetListener = new SelectionAdapter()
			{
				@Override
				public void widgetDefaultSelected(final SelectionEvent e)
				{
				}
	
				@Override
				public void widgetSelected(final SelectionEvent e)
				{
					final String type = (String) ((Button) e.getSource()).getData();
					aspectConfigTemplate.setPos(type);
					//                 System.out.println("aspectFacetQuery key set to " + type); //$NON-NLS-1$
					if (type.equals("type")) //$NON-NLS-1$
					{
					aspectConfigTemplate.setType(null);
					}
					else if (type.equals("subtype")) //$NON-NLS-1$
					{
					aspectConfigTemplate.setSubtype(null);
					aspectConfigTemplate.setRole(null);
					}
					else if (type.equals("role")) //$NON-NLS-1$
					{
					aspectConfigTemplate.setRole(null);

					}
	
				}
			};
		if (pathB || pathCombo)
		{
		Label path = new Label(composite, SWT.RIGHT);
			path.setText(NLMessages.getString("Config_Path"));
		path.setLayoutData(new GridData());
		((GridData) path.getLayoutData()).horizontalSpan = 4;

			Label taggingElement = new Label(composite, SWT.NONE);
		taggingElement.setText(NLMessages.getString("Editor_markup_element") + "*");
		taggingElement.setLayoutData(new GridData());

			Combo comboTaggingElement = new Combo(composite, SWT.READ_ONLY | SWT.RIGHT);
		comboTaggingElement.setBackground(WHITE_COLOR);
		comboTaggingElement.setLayoutData(new GridData());
		((GridData) comboTaggingElement.getLayoutData()).horizontalSpan = 3;
		((GridData) comboTaggingElement.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) comboTaggingElement.getLayoutData()).grabExcessHorizontalSpace = true;

		ComboViewer comboTaggingElementViewer = new ComboViewer(comboTaggingElement);
		comboTaggingElementViewer.setContentProvider(new MarkupContentProvider());
		comboTaggingElementViewer.setLabelProvider(new MarkupLabelProvider());
		if (_facade.getConfigs().containsKey(_provider))
		{
			// TODO comboTaggingElementViewer
//			comboTaggingElementViewer.setInput(
//							_facade.getConfigs().get(_provider).getChildren());
			comboTaggingElementViewer.setInput(getMarkupsOnLevel(aspectConfigTemplate, 0));
		}


			
		final ControlDecoration elementDeco = new ControlDecoration(comboTaggingElement, SWT.RIGHT | SWT.TOP);
			elementDeco.setDescriptionText(PDRConfigProvider.readDocu(_provider, "markup", "element",
				aspectConfigTemplate.getElement(), null, null, null));
		if (elementDeco.getDescriptionText() != null)
		{
			elementDeco.setImage(FieldDecorationRegistry.getDefault()
					.getFieldDecoration(FieldDecorationRegistry.DEC_INFORMATION).getImage());
		}
		else
		{
			elementDeco.setImage(null);
		}

			Label taggingType = new Label(composite, SWT.NONE);
		taggingType.setText(NLMessages.getString("Editor_type") + "*");
		taggingType.setLayoutData(new GridData());

			final Combo comboTaggingType = new Combo(composite, SWT.READ_ONLY | SWT.RIGHT);
		comboTaggingType.setBackground(WHITE_COLOR);
		comboTaggingType.setLayoutData(new GridData());
		((GridData) comboTaggingType.getLayoutData()).horizontalAlignment = GridData.FILL;
		((GridData) comboTaggingType.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) comboTaggingType.getLayoutData()).horizontalSpan = 3;

		final ComboViewer comboTaggingTypeViewer = new ComboViewer(comboTaggingType);
		comboTaggingTypeViewer.setContentProvider(new MarkupContentProvider());
		comboTaggingTypeViewer.setLabelProvider(new MarkupLabelProvider());
		// comboTaggingType.setItems(readConfigs(_markupProvider, "markup",
		// "type", eListName, null, null));
		final ControlDecoration typeDeco = new ControlDecoration(comboTaggingType, SWT.RIGHT | SWT.TOP);
		
			if (aspectConfigTemplate.getElement() != null)
			{
				if (aspectConfigTemplate.getElement().startsWith("aodl:"))
			{
					aspectConfigTemplate.setElement(aspectConfigTemplate.getElement().substring(5));
				}
				if (_facade.getConfigs().get(_provider).getChildren().containsKey(aspectConfigTemplate.getElement()))
			{
					if (pathCombo)
				{
//						comboTaggingTypeViewer.setInput(
//										
//										_facade.getConfigs().get(_provider).getChildren()
//											.get("aodl:" + aspectConfigTemplate.getElement()).getChildren());
						// TODO comboTaggingTypeViewer
						comboTaggingTypeViewer.setInput(getMarkupsOnLevel(aspectConfigTemplate, 1));
						ConfigItem ci = new ConfigItem();
						ci.setLabel("COMBO Selection");
						ci.setValue("COMBO Selection");
						ViewHelper.comboViewerInsertElement(comboTaggingTypeViewer, ci, 0);
				}
					else
				{
//						comboTaggingTypeViewer.setInput(_facade.getConfigs().get(_provider).getChildren()
//								.get("aodl:" + aspectConfigTemplate.getElement()).getChildren());
						// TODO: comboTaggingTypeViewer
						comboTaggingTypeViewer.setInput(getMarkupsOnLevel(aspectConfigTemplate, 1));
				}
			}
			}

			Label taggingSubtype = new Label(composite, SWT.NONE);
		taggingSubtype.setText(NLMessages.getString("Editor_subtype"));
		taggingSubtype.setLayoutData(new GridData());

			final Combo comboTaggingSubtype = new Combo(composite, SWT.READ_ONLY | SWT.RIGHT);
		comboTaggingSubtype.setBackground(WHITE_COLOR);
		comboTaggingSubtype.setLayoutData(new GridData());
		((GridData) comboTaggingSubtype.getLayoutData()).horizontalAlignment = GridData.FILL;
		((GridData) comboTaggingSubtype.getLayoutData()).grabExcessHorizontalSpace = true;
		comboTaggingSubtype.setEnabled(false);
		final ComboViewer comboTaggingSubtypeViewer = new ComboViewer(comboTaggingSubtype);
		comboTaggingSubtypeViewer.setContentProvider(new MarkupContentProvider());
		comboTaggingSubtypeViewer.setLabelProvider(new MarkupLabelProvider());
		final ControlDecoration subtypeDeco = new ControlDecoration(comboTaggingSubtype, SWT.RIGHT | SWT.TOP);

		((GridData) comboTaggingSubtype.getLayoutData()).horizontalSpan = 3;

		Label taggingRole = new Label(composite, SWT.NONE);
		taggingRole.setText(NLMessages.getString("Editor_role"));
		taggingRole.setLayoutData(new GridData());


			final Combo comboTaggingRole = new Combo(composite, SWT.READ_ONLY | SWT.RIGHT);
		comboTaggingRole.setBackground(WHITE_COLOR);
		comboTaggingRole.setLayoutData(new GridData());
		((GridData) comboTaggingRole.getLayoutData()).horizontalAlignment = GridData.FILL;
		((GridData) comboTaggingRole.getLayoutData()).grabExcessHorizontalSpace = true;
		final ComboViewer comboTaggingRoleViewer = new ComboViewer(comboTaggingRole);
		comboTaggingRoleViewer.setContentProvider(new MarkupContentProvider());
		comboTaggingRoleViewer.setLabelProvider(new MarkupLabelProvider());
		comboTaggingRole.setEnabled(false);
		final ControlDecoration roleDeco = new ControlDecoration(comboTaggingRole, SWT.RIGHT | SWT.TOP);

		((GridData) comboTaggingRole.getLayoutData()).horizontalSpan = 3;

			_loading = true;

		comboTaggingElementViewer.addSelectionChangedListener(new ISelectionChangedListener()
		{

			@Override
			public void selectionChanged(final SelectionChangedEvent event)
			{
				ISelection iSelection = event.getSelection();
				Object obj = ((IStructuredSelection) iSelection).getFirstElement();
				ConfigData cd = (ConfigData) obj;
				if (cd.getValue().startsWith("aodl:"))
				{
					aspectConfigTemplate.setElement(cd.getValue().substring(5));
				}
				else
				{
					aspectConfigTemplate.setElement(cd.getValue());
				}
				comboTaggingSubtype.removeAll();
				comboTaggingSubtype.setEnabled(false);
				if (!_loading)
				{
						comboTaggingType.removeAll();
					aspectConfigTemplate.setType(""); //$NON-NLS-1$
					aspectConfigTemplate.setSubtype(""); //$NON-NLS-1$
					aspectConfigTemplate.setRole(""); //$NON-NLS-1$
				}
				comboTaggingRole.removeAll();
				comboTaggingRole.setEnabled(false);



					elementDeco.setDescriptionText(PDRConfigProvider.readDocu(_provider, "markup", "element",
						aspectConfigTemplate.getElement(), null, null, null));
				if (elementDeco.getDescriptionText() != null && elementDeco.getDescriptionText().trim().length() > 0)
				{
					elementDeco.setImage(FieldDecorationRegistry.getDefault()
							.getFieldDecoration(FieldDecorationRegistry.DEC_INFORMATION).getImage());
				}
				else
				{
					elementDeco.setImage(null);
				}
				typeDeco.setImage(null);
				typeDeco.setDescriptionText(null);
				subtypeDeco.setImage(null);
				subtypeDeco.setDescriptionText(null);
				roleDeco.setImage(null);
				roleDeco.setDescriptionText(null);
				if (_facade.getConfigs().containsKey(_provider))
				{
//					HashMap<String,ConfigData> children = 
//						_facade.getConfigs().get(_provider).getChildren()
//								.get("aodl:" + aspectConfigTemplate.getElement()).getChildren();
							
					// TODO: comboTaggingTypeViewer
					comboTaggingTypeViewer.setInput(getMarkupsOnLevel(aspectConfigTemplate, 1));
						if (pathCombo)
						{
							ConfigItem ci = new ConfigItem();
							ci.setLabel("COMBO Selection");
							ci.setValue("COMBO Selection");
							ViewHelper.comboViewerInsertElement(comboTaggingTypeViewer, ci, 0);
							if (!_loading)
							{
								comboTaggingTypeViewer.setSelection(new StructuredSelection(ci));
								aspectConfigTemplate.setPos("type");
							}
						}
						else if (!_loading)
						{
							comboTaggingTypeViewer.setSelection(new StructuredSelection(comboTaggingTypeViewer
									.getElementAt(0)));
						}
				}
				comboTaggingTypeViewer.refresh();
				// comboTaggingType.setItems(readConfigs(_markupProvider,
				// "markup", "type", eListName, null, null));
			}

		});


		comboTaggingTypeViewer.addSelectionChangedListener(new ISelectionChangedListener()
		{

			@Override
			public void selectionChanged(final SelectionChangedEvent event)
			{
				ISelection iSelection = event.getSelection();
				Object obj = ((IStructuredSelection) iSelection).getFirstElement();
				ConfigData cd = (ConfigData) obj;
				comboTaggingSubtype.removeAll();
				comboTaggingRole.removeAll();
				comboTaggingRole.setEnabled(false);
				if (!_loading)
				{
					aspectConfigTemplate.setSubtype(""); //$NON-NLS-1$
					aspectConfigTemplate.setRole(""); //$NON-NLS-1$
				}
				if (cd != null)
				{
					if (cd.getValue().equals("COMBO Selection"))
					{
						comboTaggingRole.setEnabled(false);
						comboTaggingSubtype.setEnabled(false);
						aspectConfigTemplate.setType(null);
						aspectConfigTemplate.setSubtype(null);
						aspectConfigTemplate.setRole(null);
						aspectConfigTemplate.setPos("type");
					}
					else
					{
						aspectConfigTemplate.setType(cd.getValue());
//						ConfigData input = null;
//						if (_facade.getConfigs().containsKey(_provider))
//						{
//							input = _facade.getConfigs().get(_provider).getChildren()
//									.get("aodl:" + aspectConfigTemplate.getElement()).getChildren()
//									.get(aspectConfigTemplate.getType());
//						}
//						if (input != null)
						if (ifMarkupsOnLevel(aspectConfigTemplate, 1))
						{
							//HashMap<String, ConfigData> children = input.getChildren();
							// TODO comboTaggingSubtypeViewer
							HashMap<String, ConfigData> children = getMarkupsOnLevel(aspectConfigTemplate, 2);
							comboTaggingSubtypeViewer.setInput(children);

								if (pathCombo && !_loading)
								{
									ConfigItem ci = new ConfigItem();
									ci.setLabel("COMBO Selection");
									ci.setValue("COMBO Selection");
									ViewHelper.comboViewerInsertElement(comboTaggingSubtypeViewer, ci, 0);
									if (!_loading)
									{
									comboTaggingSubtypeViewer.setSelection(new StructuredSelection(ci));
									aspectConfigTemplate.setPos("subtype");
									}
								}
							comboTaggingSubtype.setEnabled(true);
						}
							typeDeco.setDescriptionText(PDRConfigProvider.readDocu(_provider, "markup", "type",
								aspectConfigTemplate.getElement(), aspectConfigTemplate.getType(), null, null));
						if (typeDeco.getDescriptionText() != null && typeDeco.getDescriptionText().trim().length() > 0)
						{
							typeDeco.setImage(FieldDecorationRegistry.getDefault()
									.getFieldDecoration(FieldDecorationRegistry.DEC_INFORMATION).getImage());
						}
						else
						{
							typeDeco.setImage(null);
						}
					}
					

				}
				
				
				subtypeDeco.setImage(null);
				subtypeDeco.setDescriptionText(null);
				roleDeco.setImage(null);
				roleDeco.setDescriptionText(null);
				processMarkupComboSettings(aspectConfigTemplate);
					updatePreviewWidget(aspectConfigTemplate);
				}


		});

		// Label blancType = new Label(tagging1Coposite, SWT.NONE);
		// blancType.setText("");
		// blancType.setLayoutData(new GridData());

		comboTaggingSubtypeViewer.addSelectionChangedListener(new ISelectionChangedListener()
		{

			@Override
			public void selectionChanged(final SelectionChangedEvent event)
			{
				ISelection iSelection = event.getSelection();
				Object obj = ((IStructuredSelection) iSelection).getFirstElement();
				ConfigData cd = (ConfigData) obj;
				if (cd != null)
				{
					aspectConfigTemplate.setSubtype(cd.getValue());
					comboTaggingRole.removeAll();
					if (!_loading)
					{
						aspectConfigTemplate.setRole(""); //$NON-NLS-1$
					}
					comboTaggingSubtype.layout();
						if (cd.getValue().equals("COMBO Selection") && !_loading)
					{
						comboTaggingRole.setEnabled(false);
						aspectConfigTemplate.setSubtype(null);
						aspectConfigTemplate.setRole(null);
						aspectConfigTemplate.setPos("subtype");
					}
					else
					{
//						ConfigData input = _facade.getConfigs().get(_provider);
//						if (input != null)
//						{
//							input = input.getChildren().get("aodl:" + aspectConfigTemplate.getElement());
//						}
//						if (input != null)
//						{
//							input = input.getChildren().get(aspectConfigTemplate.getType());
//						}
//						if (input != null)
//						{
//							input = input.getChildren().get(aspectConfigTemplate.getSubtype());
//						}
//						if (input != null)
						if (ifMarkupsOnLevel(aspectConfigTemplate, 2))
						{
							// TODO comboTaggingRoleViewer
							HashMap<String, ConfigData> children = getMarkupsOnLevel(aspectConfigTemplate, 3);
							comboTaggingRoleViewer.setInput(children);
								if (pathCombo)
								{
									ConfigItem ci = new ConfigItem();
									ci.setLabel("COMBO Selection");
									ci.setValue("COMBO Selection");
									ViewHelper.comboViewerInsertElement(comboTaggingRoleViewer, ci, 0);
									comboTaggingRoleViewer.setSelection(new StructuredSelection(ci));
									aspectConfigTemplate.setPos("role");
								}
							comboTaggingRole.setEnabled(true);
	
						}
							subtypeDeco.setDescriptionText(PDRConfigProvider.readDocu(_provider, "markup", "subtype",
								aspectConfigTemplate.getElement(), aspectConfigTemplate.getType(),
								aspectConfigTemplate.getSubtype(), null));
						if (subtypeDeco.getDescriptionText() != null
								&& subtypeDeco.getDescriptionText().trim().length() > 0)
						{
							subtypeDeco.setImage(FieldDecorationRegistry.getDefault()
									.getFieldDecoration(FieldDecorationRegistry.DEC_INFORMATION).getImage());
						}
						else
						{
							subtypeDeco.setImage(null);
						}
					}
					roleDeco.setImage(null);
					roleDeco.setDescriptionText(null);
						processMarkupComboSettings(aspectConfigTemplate);
						updatePreviewWidget(aspectConfigTemplate);

				}
			}

		});
		// Label blancSubtype = new Label(tagging1Coposite, SWT.NONE);
		// blancSubtype.setText("");
		// blancSubtype.setLayoutData(new GridData());
		// end subtype

		comboTaggingRoleViewer.addSelectionChangedListener(new ISelectionChangedListener()
		{

			@Override
			public void selectionChanged(final SelectionChangedEvent event)
			{
				ISelection iSelection = event.getSelection();
				Object obj = ((IStructuredSelection) iSelection).getFirstElement();
				ConfigData cd = (ConfigData) obj;
				if (cd != null)
				{
						if (cd.getValue().equals("COMBO Selection"))
					{
							aspectConfigTemplate.setRole(null);
							aspectConfigTemplate.setPos("role");
					}
					else
					{
							aspectConfigTemplate.setRole(cd.getValue());
							roleDeco.setDescriptionText(PDRConfigProvider.readDocu(_provider, "markup", "role",
									aspectConfigTemplate.getElement(), aspectConfigTemplate.getType(),
									aspectConfigTemplate.getSubtype(), aspectConfigTemplate.getRole()));
							if (roleDeco.getDescriptionText() != null
									&& roleDeco.getDescriptionText().trim().length() > 0)
							{
								roleDeco.setImage(FieldDecorationRegistry.getDefault()
										.getFieldDecoration(FieldDecorationRegistry.DEC_INFORMATION).getImage());
							}
							else
							{
								roleDeco.setImage(null);
							}
					}
						processMarkupComboSettings(aspectConfigTemplate);
						updatePreviewWidget(aspectConfigTemplate);

				}
				else
				{
					roleDeco.setImage(null);
				}

			}
		});

		// set combos
			if (date && comboTaggingElementViewer != null)
			{
				aspectConfigTemplate.setElement("date");
				if (_facade.getConfigs().get(_provider) != null
						&& _facade.getConfigs().get(_provider).getChildren().containsKey("aodl:date"))
			{
					comboTaggingElementViewer.setSelection(new StructuredSelection(_facade.getConfigs().get(_provider)
							.getChildren().get("aodl:date")));
					comboTaggingElement.setEnabled(false);
					comboTaggingType.setEnabled(true);
					comboTaggingTypeViewer.refresh();
			}
			}
		if (aspectConfigTemplate.getElement() != null)
		{
			if (!aspectConfigTemplate.getElement().startsWith("aodl:"))
			{
					setComboViewerByString(comboTaggingElementViewer, "aodl:" + aspectConfigTemplate.getElement());
			}
			else
			{
					setComboViewerByString(comboTaggingElementViewer, aspectConfigTemplate.getElement());
			}

				if (aspectConfigTemplate.getType() != null)
				{
//					if (_facade.getConfigs().get(_provider) != null
//							&& _facade.getConfigs().get(_provider).getChildren()
//									.get("aodl:" + aspectConfigTemplate.getElement()) != null)
				if (ifMarkupsOnLevel(aspectConfigTemplate, 0)) {
//						comboTaggingTypeViewer.setInput(_facade.getConfigs().get(_provider).getChildren()
//								.get("aodl:" + aspectConfigTemplate.getElement()).getChildren());
					//TODO comboTaggingTypeViewer
						comboTaggingTypeViewer.setInput(getMarkupsOnLevel(aspectConfigTemplate, 1));

						if (pathCombo && aspectConfigTemplate.getPos() != null
								&& aspectConfigTemplate.getPos().equals("type"))
						{
							ConfigItem ci = new ConfigItem();
							ci.setLabel("COMBO Selection");
							ci.setValue("COMBO Selection");
							ViewHelper.comboViewerInsertElement(comboTaggingTypeViewer, ci, 0);
							setComboViewerByString(comboTaggingTypeViewer, "COMBO Selection");
						}
						else
						{
							setComboViewerByString(comboTaggingTypeViewer, aspectConfigTemplate.getType());
						}
//						if (aspectConfigTemplate.getType() != null
//								&& _facade.getConfigs().get(_provider).getChildren()
//										.get("aodl:" + aspectConfigTemplate.getElement()).getChildren()
//										.get(aspectConfigTemplate.getType()) != null)
						if (ifMarkupsOnLevel(aspectConfigTemplate, 1))

						{
//							comboTaggingSubtypeViewer.setInput(_facade.getConfigs().get(_provider).getChildren()
//									.get("aodl:" + aspectConfigTemplate.getElement()).getChildren()
//									.get(aspectConfigTemplate.getType()).getChildren());
							// TODO: comboTaggingSubtypeViewer
							comboTaggingSubtypeViewer.setInput(getMarkupsOnLevel(aspectConfigTemplate, 2));
							if (pathCombo && aspectConfigTemplate.getPos() != null
									&& aspectConfigTemplate.getPos().equals("subtype"))
							{
								ConfigData ci = new ConfigData();
								ci.setLabel("COMBO Selection");
								ci.setValue("COMBO Selection");
								ViewHelper.comboViewerInsertElement(comboTaggingSubtypeViewer, ci, 0);
								setComboViewerByString(comboTaggingSubtypeViewer, "COMBO Selection");
							}
							else
							{
								setComboViewerByString(comboTaggingSubtypeViewer, aspectConfigTemplate.getSubtype());

							}

//							if (aspectConfigTemplate.getSubtype() != null
//									&& _facade.getConfigs().get(_provider).getChildren()
//									.get("aodl:" + aspectConfigTemplate.getElement()).getChildren()
//										.get(aspectConfigTemplate.getType()).getChildren()
//											.get(aspectConfigTemplate.getSubtype()) != null)
							if (ifMarkupsOnLevel(aspectConfigTemplate, 2))
							{
								// TODO comboTaggingRoleViewer
								comboTaggingRoleViewer.setInput(getMarkupsOnLevel(aspectConfigTemplate, 3));

								if (pathCombo && aspectConfigTemplate.getPos() != null
										&& aspectConfigTemplate.getPos().equals("role"))
								{
									ConfigItem ci = new ConfigItem();
									ci.setLabel("COMBO Selection");
									ci.setValue("COMBO Selection");
									ViewHelper.comboViewerInsertElement(comboTaggingRoleViewer, ci, 0);
									setComboViewerByString(comboTaggingRoleViewer, "COMBO Selection");
								}
								else
								{
									setComboViewerByString(comboTaggingRoleViewer, aspectConfigTemplate.getRole());

								}
							}
						}
					}


				}
				_loading = false;
		}
		else
		{
				comboTaggingElementViewer.setSelection(new StructuredSelection(_facade.getConfigs().get(_provider)
						.getChildren().get("aodl:persName")));
				comboTaggingElement.setEnabled(true);
				if (_facade.getConfigs().containsKey(_provider))
			{
					comboTaggingTypeViewer.setInput(_facade.getConfigs().get(_provider).getChildren()
							.get("aodl:persName").getChildren());
			}
				comboTaggingTypeViewer.refresh();
			}
		}
		if (levelSpanB)
		{
			// levelSpan
			Label levelSpan = new Label(composite, SWT.NONE);
			levelSpan.setText(NLMessages.getString("Config_level_span"));
			levelSpan.setLayoutData(new GridData());
			((GridData) levelSpan.getLayoutData()).horizontalSpan = 1;
			((GridData) levelSpan.getLayoutData()).grabExcessHorizontalSpace = true;
			((GridData) levelSpan.getLayoutData()).horizontalAlignment = SWT.FILL;
			levelSpan.pack();

			final Spinner levelSpanSpinner = new Spinner(composite, SWT.NONE | SWT.READ_ONLY);
			levelSpanSpinner.setLayoutData(new GridData());
			((GridData) levelSpanSpinner.getLayoutData()).horizontalSpan = 1;
			((GridData) levelSpanSpinner.getLayoutData()).grabExcessHorizontalSpace = true;
			((GridData) levelSpanSpinner.getLayoutData()).horizontalAlignment = SWT.FILL;
			levelSpanSpinner.setSelection(aspectConfigTemplate.getLevelSpan());

			levelSpanSpinner.setMaximum(3);
			levelSpanSpinner.addSelectionListener(new SelectionAdapter()
			{
				@Override
				public void widgetSelected(final SelectionEvent se)
				{
					aspectConfigTemplate.setLevelSpan(levelSpanSpinner.getSelection());
					updatePreviewWidget(aspectConfigTemplate);

				}
			}); // SelectionListener
		}
		if (combo)
		{
			Label levelSpan = new Label(composite, SWT.NONE);
			levelSpan.setText(NLMessages.getString("Config_Preview"));
			levelSpan.setLayoutData(new GridData());
			((GridData) levelSpan.getLayoutData()).horizontalSpan = 1;
			((GridData) levelSpan.getLayoutData()).grabExcessHorizontalSpace = true;
			((GridData) levelSpan.getLayoutData()).horizontalAlignment = SWT.FILL;
			levelSpan.pack();

			Composite c = new Composite(composite, SWT.BORDER);
			c.setLayoutData(new GridData());
			c.setLayout(new GridLayout(1, false));
			((GridLayout) c.getLayout()).marginHeight = 0;
			((GridLayout) c.getLayout()).verticalSpacing = 0;
			((GridLayout) c.getLayout()).marginWidth = 0;
			((GridData) c.getLayoutData()).heightHint = 20;
			((GridData) c.getLayoutData()).horizontalSpan = 1;
			((GridData) c.getLayoutData()).grabExcessHorizontalSpace = true;
			((GridData) c.getLayoutData()).horizontalAlignment = SWT.FILL;
			_prevWidget = new AEAspectWidgetCustomizable(c, aspectConfigTemplate, SWT.NONE);
			updatePreviewWidget(aspectConfigTemplate);
			c.layout();
			c.pack();
		}
		


		if (date)
		{
			Label date1 = new Label(composite, SWT.RIGHT);
			date1.setText(NLMessages.getString("Editor_add_dates") + "1");
			date1.setLayoutData(new GridData());

			final Combo date1Combo = new Combo(composite, SWT.DROP_DOWN | SWT.READ_ONLY);
			date1Combo.setBackground(WHITE_COLOR);
			date1Combo.setLayoutData(new GridData());
			((GridData) date1Combo.getLayoutData()).horizontalSpan = 1;
			((GridData) date1Combo.getLayoutData()).horizontalAlignment = SWT.FILL;
			((GridData) date1Combo.getLayoutData()).grabExcessHorizontalSpace = true;
			ComboViewer date1ComboViewer = new ComboViewer(date1Combo);
			date1ComboViewer.setContentProvider(ArrayContentProvider.getInstance());
			date1ComboViewer.setLabelProvider(new LabelProvider()
			{

				@Override
				public String getText(final Object element)
				{
					String str = (String) element;
					if (NLMessages.getString("Editor_time_" + str) != null)
					{
						return NLMessages.getString("Editor_time_" + str);
					}
					return str;
				}

			});

			date1ComboViewer.setInput(AEConstants.TIME_TYPES);
			date1ComboViewer.addSelectionChangedListener(new ISelectionChangedListener()
			{

				@Override
				public void selectionChanged(final SelectionChangedEvent event)
				{
					ISelection selection = event.getSelection();
					Object obj = ((IStructuredSelection) selection).getFirstElement();
					String s = (String) obj;
					aspectConfigTemplate.setDate1(s);
				}

			});
			if (aspectConfigTemplate.getDate1() != null)
			{
				StructuredSelection selection = new StructuredSelection(aspectConfigTemplate.getDate1());
				date1ComboViewer.setSelection(selection);
			}
			else
			{
				date1Combo.select(0);
				ISelection selection = date1ComboViewer.getSelection();
				Object obj = ((IStructuredSelection) selection).getFirstElement();
				String s = (String) obj;
				aspectConfigTemplate.setDate1(s);
			}

			Label date2 = new Label(composite, SWT.RIGHT);
			date2.setText(NLMessages.getString("Editor_add_dates") + "2");
			date2.setLayoutData(new GridData());

			final Combo date2Combo = new Combo(composite, SWT.DROP_DOWN | SWT.READ_ONLY);
			date2Combo.setBackground(WHITE_COLOR);
			date2Combo.setLayoutData(new GridData());
			((GridData) date2Combo.getLayoutData()).horizontalSpan = 1;
			((GridData) date2Combo.getLayoutData()).horizontalAlignment = SWT.FILL;
			((GridData) date2Combo.getLayoutData()).grabExcessHorizontalSpace = true;
			ComboViewer date2ComboViewer = new ComboViewer(date2Combo);
			date2ComboViewer.setContentProvider(ArrayContentProvider.getInstance());
			date2ComboViewer.setLabelProvider(new LabelProvider()
			{

				@Override
				public String getText(final Object element)
				{
					String str = (String) element;
					if (str.trim().length() > 0)
					{
						String label = NLMessages.getString("Editor_time_" + str);
						if (label != null && !label.startsWith("!"))
						{
							return label;
						}
					}
					return str;
				}

			});

			String[] _afters = new String[]
			{"", "to", "notAfter"}; //$NON-NLS-1$
			date2ComboViewer.setInput(_afters);
			date2ComboViewer.addSelectionChangedListener(new ISelectionChangedListener()
			{

				@Override
				public void selectionChanged(final SelectionChangedEvent event)
				{
					ISelection selection = event.getSelection();
					Object obj = ((IStructuredSelection) selection).getFirstElement();
					String s = (String) obj;
					aspectConfigTemplate.setDate2(s);
				}

			});
			if (aspectConfigTemplate.getDate2() != null)
			{
				StructuredSelection selection = new StructuredSelection(aspectConfigTemplate.getDate2());
				date2ComboViewer.setSelection(selection);
			}
			else
			{
				date2Combo.select(0);
				ISelection selection = date2ComboViewer.getSelection();
				Object obj = ((IStructuredSelection) selection).getFirstElement();
				String s = (String) obj;
				aspectConfigTemplate.setDate2(s);
			}
		}

		if (relation)
		{
			Label relationContextLabel = new Label(composite, SWT.NONE);
			relationContextLabel.setText(NLMessages.getString("Editor_context"));
			relationContextLabel.setLayoutData(new GridData());
			((GridData) relationContextLabel.getLayoutData()).horizontalSpan = 1;

			Combo relationContextCombo = new Combo(composite, SWT.READ_ONLY);
			relationContextCombo.setBackground(WHITE_COLOR);
			relationContextCombo.setLayoutData(new GridData());
			((GridData) relationContextCombo.getLayoutData()).horizontalSpan = 3;
			((GridData) relationContextCombo.getLayoutData()).horizontalAlignment = SWT.FILL;
			((GridData) relationContextCombo.getLayoutData()).grabExcessHorizontalSpace = true;
			final ComboViewer relationContextComboViewer = new ComboViewer(relationContextCombo);
			relationContextComboViewer.setContentProvider(new MarkupContentProvider());
			relationContextComboViewer.setLabelProvider(new MarkupLabelProvider());
			
			final ControlDecoration relContextDeco = new ControlDecoration(relationContextCombo, SWT.RIGHT | SWT.TOP);

			Label relationTypeLabel = new Label(composite, SWT.NONE);
			relationTypeLabel.setText(NLMessages.getString("Editor_class"));
			relationTypeLabel.setLayoutData(new GridData());
			((GridData) relationTypeLabel.getLayoutData()).horizontalSpan = 1;

			final Combo relationClassCombo = new Combo(composite, SWT.READ_ONLY);
			relationClassCombo.setBackground(WHITE_COLOR);
			relationClassCombo.setLayoutData(new GridData());
			((GridData) relationClassCombo.getLayoutData()).horizontalSpan = 3;
			((GridData) relationClassCombo.getLayoutData()).horizontalAlignment = SWT.FILL;
			((GridData) relationClassCombo.getLayoutData()).grabExcessHorizontalSpace = true;
			final ComboViewer relationClassComboViewer = new ComboViewer(relationClassCombo);
			relationClassComboViewer.setContentProvider(new MarkupContentProvider());
			relationClassComboViewer.setLabelProvider(new MarkupLabelProvider());
			final ControlDecoration relClassDeco = new ControlDecoration(relationClassCombo, SWT.RIGHT | SWT.TOP);

			Label relCitationLabel = new Label(composite, SWT.NONE);
			relCitationLabel.setText(NLMessages.getString("Config_value"));
			relCitationLabel.setLayoutData(new GridData());

			final Combo relValueCombo = new Combo(composite, SWT.BORDER | SWT.READ_ONLY);
			relValueCombo.setBackground(WHITE_COLOR);
			relValueCombo.setLayoutData(new GridData());
			((GridData) relValueCombo.getLayoutData()).horizontalAlignment = SWT.FILL;
			((GridData) relValueCombo.getLayoutData()).grabExcessHorizontalSpace = true;
			((GridData) relValueCombo.getLayoutData()).horizontalSpan = 3;
			final ComboViewer relValueComboViewer = new ComboViewer(relValueCombo);
			relValueComboViewer.setContentProvider(new MarkupContentProvider());
			relValueComboViewer.setLabelProvider(new MarkupLabelProvider());
			final ControlDecoration relValueDeco = new ControlDecoration(relValueCombo, SWT.RIGHT | SWT.TOP);

			if (_facade.getConfigs().get(_provider) != null)
			{
				relationContextComboViewer.setInput(_facade.getConfigs().get(_provider).getChildren()
						.get("aodl:relation").getChildren());
			}

			// control for relationClassCombo
			// ArrayList<String> list = cListPro
			//						.getList("types", "type", "relation"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			// for (int n = 0; n < list.size(); n++)
			// {
			// relationClassCombo.add(list.get(n));
			// }
			if (aspectConfigTemplate.getElement() != null)
			{
				setComboViewerByString(relationContextComboViewer, aspectConfigTemplate.getElement());
			}

			relationContextComboViewer.addSelectionChangedListener(new ISelectionChangedListener()
			{
				@Override
				public void selectionChanged(final SelectionChangedEvent event)
				{
					ISelection iSelection = event.getSelection();
					Object obj = ((IStructuredSelection) iSelection).getFirstElement();
					ConfigData cd = (ConfigData) obj;
					aspectConfigTemplate.setElement(cd.getValue());
					relationClassCombo.removeAll();
					relValueCombo.removeAll();
					relContextDeco.setDescriptionText(PDRConfigProvider.readDocu(_provider, "relation", "context",
							aspectConfigTemplate.getElement(), null, null, null));
					if (relContextDeco.getDescriptionText() != null)
					{
						relContextDeco.setImage(FieldDecorationRegistry.getDefault()
								.getFieldDecoration(FieldDecorationRegistry.DEC_INFORMATION).getImage());
					}
					else
					{
						relContextDeco.setImage(null);
					}
					relationClassComboViewer.setInput(_facade.getConfigs().get(_provider).getChildren()
							.get("aodl:relation").getChildren().get(aspectConfigTemplate.getElement()).getChildren());
				}

			});

			relationContextCombo.layout();

			// control for relationContextCombo
			if (aspectConfigTemplate.getType() != null)
			{
				if (aspectConfigTemplate.getElement() != null
						&& _facade.getConfigs().get(_provider) != null
						&& _facade.getConfigs().get(_provider).getChildren().get("aodl:relation") != null
						&& _facade.getConfigs().get(_provider).getChildren().get("aodl:relation").getChildren()
								.get(aspectConfigTemplate.getElement()) != null)
				{
					relationClassComboViewer.setInput(_facade.getConfigs().get(_provider).getChildren()
							.get("aodl:relation").getChildren().get(aspectConfigTemplate.getElement()).getChildren());
				}
				setComboViewerByString(relationClassComboViewer, aspectConfigTemplate.getType());
			}
			else
			{
				relationClassComboViewer.setInput(null);
				relationClassComboViewer.refresh();
			}
			relationClassComboViewer.addSelectionChangedListener(new ISelectionChangedListener()
			{
				@Override
				public void selectionChanged(final SelectionChangedEvent event)
				{
					ISelection iSelection = event.getSelection();
					Object obj = ((IStructuredSelection) iSelection).getFirstElement();
					ConfigData cd = (ConfigData) obj;
					aspectConfigTemplate.setType(cd.getValue());
					relValueComboViewer.setInput(_facade.getConfigs().get(_provider).getChildren().get("aodl:relation")
							.getChildren().get(aspectConfigTemplate.getElement()).getChildren()
							.get(aspectConfigTemplate.getType()).getChildren());
					relValueComboViewer.refresh();
					relClassDeco.setDescriptionText(PDRConfigProvider.readDocu(_provider, "relation", "class",
							aspectConfigTemplate.getElement(), aspectConfigTemplate.getType(), null, null));
					if (relClassDeco.getDescriptionText() != null)
					{
						relClassDeco.setImage(FieldDecorationRegistry.getDefault()
								.getFieldDecoration(FieldDecorationRegistry.DEC_INFORMATION).getImage());
					}
					else
					{
						relClassDeco.setImage(null);
					}
				}
			});

			relationClassCombo.layout();
			if (aspectConfigTemplate.getSubtype() != null)
			{
				if (aspectConfigTemplate.getType() != null
						&& _facade.getConfigs().get(_provider) != null
						&& _facade.getConfigs().get(_provider).getChildren().get("aodl:relation") != null
						&& _facade.getConfigs().get(_provider).getChildren().get("aodl:relation").getChildren()
								.get(aspectConfigTemplate.getElement()) != null
						&& _facade.getConfigs().get(_provider).getChildren().get("aodl:relation").getChildren()
								.get(aspectConfigTemplate.getElement()).getChildren()
								.get(aspectConfigTemplate.getType()) != null)
				{
					relValueComboViewer.setInput(_facade.getConfigs().get(_provider).getChildren().get("aodl:relation")
							.getChildren().get(aspectConfigTemplate.getElement()).getChildren()
							.get(aspectConfigTemplate.getType()).getChildren());
				}
				setComboViewerByString(relValueComboViewer, aspectConfigTemplate.getSubtype());
			}
			else
			{
				relValueComboViewer.setInput(null);
				relValueComboViewer.refresh();
			}
			relValueComboViewer.addSelectionChangedListener(new ISelectionChangedListener()
			{
				@Override
				public void selectionChanged(final SelectionChangedEvent event)
				{
					ISelection iSelection = event.getSelection();
					Object obj = ((IStructuredSelection) iSelection).getFirstElement();
					ConfigData cd = (ConfigData) obj;
					aspectConfigTemplate.setSubtype(cd.getValue());
					relValueDeco.setDescriptionText(PDRConfigProvider.readDocu(_provider, "relation", "value",
							aspectConfigTemplate.getElement(), aspectConfigTemplate.getType(),
							aspectConfigTemplate.getSubtype(), null));
					if (relValueDeco.getDescriptionText() != null)
					{
						relValueDeco.setImage(FieldDecorationRegistry.getDefault()
								.getFieldDecoration(FieldDecorationRegistry.DEC_INFORMATION).getImage());
					}
					else
					{
						relValueDeco.setImage(null);
					}
				}
			});

		}
		if (preSuffix)
		{
			Label prefix = new Label(composite, SWT.NONE);
			prefix.setText(NLMessages.getString("Config_Prefix"));
			prefix.setLayoutData(new GridData());

			final Text prefixText = new Text(composite, SWT.BORDER);
			prefixText.setLayoutData(new GridData());
			((GridData) prefixText.getLayoutData()).horizontalSpan = 1;
			((GridData) prefixText.getLayoutData()).grabExcessHorizontalSpace = true;
			((GridData) prefixText.getLayoutData()).horizontalAlignment = SWT.FILL;
			if (aspectConfigTemplate.getPrefix() != null)
			{
				prefixText.setText(aspectConfigTemplate.getPrefix());
			}
			else
			{
				prefixText.setText("");
			}
			prefixText.addFocusListener(new FocusAdapter()
			{
				@Override
				public void focusLost(final FocusEvent e)
				{
					aspectConfigTemplate.setPrefix(prefixText.getText().trim());
					// if (c instanceof ConfigItem)
					// {
					// ((ConfigItem) c).setLabel(labelText.getText().trim());
					// }
				}
			});

			Label suffix = new Label(composite, SWT.NONE);
			suffix.setText(NLMessages.getString("Config_suffix"));
			suffix.setLayoutData(new GridData());

			final Text suffixText = new Text(composite, SWT.BORDER);
			suffixText.setLayoutData(new GridData());
			((GridData) suffixText.getLayoutData()).horizontalSpan = 1;
			((GridData) suffixText.getLayoutData()).grabExcessHorizontalSpace = true;
			((GridData) suffixText.getLayoutData()).horizontalAlignment = SWT.FILL;
			if (aspectConfigTemplate.getSuffix() != null)
			{
				suffixText.setText(aspectConfigTemplate.getSuffix());
			}
			else
			{
				suffixText.setText("");
			}
			suffixText.addFocusListener(new FocusAdapter()
			{
				@Override
				public void focusLost(final FocusEvent e)
				{
					aspectConfigTemplate.setSuffix(suffixText.getText().trim());
					// if (c instanceof ConfigItem)
					// {
					// ((ConfigItem) c).setLabel(labelText.getText().trim());
					// }
				}
			});
		}

		// levelSpan
		if (horizontalSpan)
		{
			Label horizontalSpanL = new Label(composite, SWT.NONE);
			horizontalSpanL.setText(NLMessages.getString("Config_horizontal_span"));
			horizontalSpanL.setLayoutData(new GridData());
			((GridData) horizontalSpanL.getLayoutData()).horizontalSpan = 1;
			((GridData) horizontalSpanL.getLayoutData()).grabExcessHorizontalSpace = true;
			((GridData) horizontalSpanL.getLayoutData()).horizontalAlignment = SWT.FILL;
			horizontalSpanL.pack();

			final Combo horizontalSpanCombo = new Combo(composite, SWT.NONE | SWT.READ_ONLY);
			horizontalSpanCombo.setLayoutData(new GridData());
			((GridData) horizontalSpanCombo.getLayoutData()).horizontalSpan = 1;
			((GridData) horizontalSpanCombo.getLayoutData()).grabExcessHorizontalSpace = true;
			((GridData) horizontalSpanCombo.getLayoutData()).horizontalAlignment = SWT.FILL;

			if (relation)
			{
				horizontalSpanCombo.setItems(new String[]
				{"50%", "75%", "100%"});
			}
			else
			{
				horizontalSpanCombo.setItems(new String[]
				{"25%", "50%", "75%", "100%"});
			}
			horizontalSpanCombo.select(aspectConfigTemplate.getHorizontalSpan() - 1);

			horizontalSpanCombo.addSelectionListener(new SelectionAdapter()
			{
				@Override
				public void widgetSelected(final SelectionEvent se)
				{
					String selection = horizontalSpanCombo.getItem(horizontalSpanCombo.getSelectionIndex());
					if (selection.startsWith("25"))
					{
						aspectConfigTemplate.setHorizontalSpan(1);
					}
					else if (selection.startsWith("50"))
					{
						aspectConfigTemplate.setHorizontalSpan(2);
					}
					else if (selection.startsWith("75"))
					{
						aspectConfigTemplate.setHorizontalSpan(3);
					}
					else if (selection.startsWith("100"))
					{
						aspectConfigTemplate.setHorizontalSpan(4);
					}
				}
			}); // SelectionListener
		}
		else
		{
			aspectConfigTemplate.setHorizontalSpan(4);
		}
		composite.layout();
	}
	
	
	/**
	 * parses a hashmap which maps ConfigData values on String keys, like the result of ConfigData.getChildren(),
	 * to get rid of all entries that don't have children on their own. 
	 * this is necessary for the correct initialization of combo widget configurations to avoid ending up with
	 * a combo widget that does not allow to chose on its corresponding markup level 
	 * @param chld
	 * @return all entries from chld that have children on the next markup level
	 */
	private HashMap<String, ConfigData> removeLeafes(HashMap<String, ConfigData> chld) {
		
		HashMap<String, ConfigData> result = new HashMap<String, ConfigData>();
		
		for (Entry<String, ConfigData> e : chld.entrySet())
			if (e.getValue().getChildren().size()>0) {
				// System.out.println(e.getKey()+": check! "+e.getValue().getChildren().size());
				result.put(e.getKey(), e.getValue());
			}
		// else System.out.println(e.getKey()+": NOPE");
		
		return result;
	}
	
	private boolean ifMarkupsOnLevel(AspectConfigTemplate template, int level) {
		
			
		if (_facade.getConfigs().get(_provider) != null) {
			HashMap<String, ConfigData> configs = _facade.getConfigs().get(_provider).getChildren();
			if ( configs.get("aodl:" + template.getElement()) != null)
					if (level < 1) return true;
					else {
						configs = configs.get("aodl:" + template.getElement()).getChildren();
						if (configs.get(template.getType()) != null)
							if (level < 2) return true;
							else {
								configs = configs.get(template.getType()).getChildren();
								if (configs.get(template.getSubtype()) != null)
									if (level < 3) return true;
									else {
										configs = configs.get(template.getSubtype()).getChildren();
										if (configs.get(template.getRole()) != null)
											return true;
									}
							}
					}
		}
		return false;
			
	}
	
	/**
	 * traverses down a current markup configuration path to the specified <code>level</code> and returns the markup objects
	 * on that level, if possible. the path is determined by the selections for the several markup levels in
	 * a <code>template</code> configuration panel. dependent on the the template's currently selected widget type, inappropriate
	 * markup configuration objects may be filtered out. this is necessary if the widget relies on being configured 
	 * in order to offer a set of markup configuration child nodes via a combo box, like <code>COMBO</code> and 
	 * <code>DATE_COMBO</code> do. 
	 * @param template the aspect template configuration to retrieve the markup hierarchy from
	 * @param level integer value within 0 and 3, corresponding to the markup level hierarchy: element, type, subtype, role
	 * @return hashmap <code>HashMap&lt;String, ConfigData&gt;, mapping configurations on key strings
	 * 
	 * @see ConfigData
	 */
	private HashMap<String, ConfigData> getMarkupsOnLevel(AspectConfigTemplate template, int level) {
		// if (_facade.getConfigs().get(_provider) == null)
//			System.out.println("bekomme keinen provider geladen");
		
		// root level: element
		HashMap<String, ConfigData> configs = _facade.getConfigs().get(_provider).getChildren();
		
		if (level > 0) if (configs != null)  {
			// level of first attribute: type, child nodes of element configurations
			configs = configs.get("aodl:" + template.getElement()).getChildren();
			
			if (level > 1) if (configs != null)  {
				// level of second attribute: subtype
				configs = configs.get(template.getType()).getChildren();
				HashMap<String, ConfigData> response = new HashMap<String, ConfigData>(configs.size() + 1);
				response.putAll(configs);
				response.put("", new ConfigItem("", ""));
				configs = response;
				if (level > 2) if (configs != null)  {
					// level of third and last attribute: role
					configs = configs.get(template.getSubtype()).getChildren();
					response = new HashMap<String, ConfigData>(configs.size() + 1);
					response.putAll(configs);
					response.put("", new ConfigItem("", ""));
					configs = response;
				} else return null;
			} else return null;
		} else return null;
		
		 
		
		// if currently used widget type is neither combo nor date_combo, just return map of config data
		// collected on corresponding level. 
		// if widgets combo or date_combo are used, we have to make sure that we filter out configurations
		// (markup hierarchy nodes) which are stored as leafes, which renders them useless as an option of choice
		// within these widgets, which expect chosen markup configuration nodes to provide child nodes to be 
		// offered to select by this very combo widgets
		if (template.getWidgetType() == AEAspectWidgetCustomizable.TYPE_COMBO)
			return removeLeafes(configs);
		if (template.getWidgetType() == AEAspectWidgetCustomizable.TYPE_DATE_COMBO)
			return removeLeafes(configs);
		// widget type is none of the special combo ones
		return configs;	
	}
	

	private void updatePreviewWidget(AspectConfigTemplate template)
	{
		if (_prevWidget != null)
		{
			if (template.getWidgetType() == AEAspectWidgetCustomizable.TYPE_DATE_COMBO)
			{
				AspectConfigTemplate clone = template.clone();
				clone.setWidgetType(AEAspectWidgetCustomizable.TYPE_COMBO);
				_prevWidget.setConfigTemplate(clone);
			}
			else
			{
				_prevWidget.setConfigTemplate(template);

			}
		}

	}

	private Composite createEditor(Composite composite, final SemanticTemplate semanticTemplate)
	{
		composite.setLayoutData(new GridData());
		((GridData) composite.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) composite.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) composite.getLayoutData()).grabExcessVerticalSpace = true;
		((GridData) composite.getLayoutData()).verticalAlignment = SWT.FILL;
		composite.setLayout(new GridLayout());
		((GridLayout) composite.getLayout()).numColumns = 4;
		((GridLayout) composite.getLayout()).makeColumnsEqualWidth = false;

		Label label = new Label(composite, SWT.NONE);
		label.setText(NLMessages.getString("Config_label"));
		label.setLayoutData(new GridData());
		((GridData) label.getLayoutData()).horizontalSpan = 1;
		((GridData) label.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) label.getLayoutData()).horizontalAlignment = SWT.FILL;

		_labelText = new Text(composite, SWT.BORDER);
		_labelText.setEditable(_userRichtsChecker.mayEditConfig());
		_labelText.setLayoutData(new GridData());
		((GridData) _labelText.getLayoutData()).horizontalSpan = 3;
		((GridData) _labelText.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) _labelText.getLayoutData()).horizontalAlignment = SWT.FILL;
		if (semanticTemplate.getLabel() != null)
		{
			_labelText.setText(semanticTemplate.getLabel());
		}
		else
		{
			_labelText.setText(semanticTemplate.getValue());
		}
		_labelText.addFocusListener(new FocusAdapter()
		{
			@Override
			public void focusLost(final FocusEvent e)
			{
				semanticTemplate.setLabel(_labelText.getText().trim());
				// if (c instanceof ConfigItem)
				// {
				// ((ConfigItem) c).setLabel(labelText.getText().trim());
				// }
			}
		});
_labelText.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				IStructuredSelection selection = (IStructuredSelection) _treeViewer.getSelection();
				Object obj = selection.getFirstElement();
				ConfigTreeNode tn = (ConfigTreeNode) obj;
				semanticTemplate.setLabel(_labelText.getText().trim());
				tn.setLabel(semanticTemplate.getLabel());
				_treeViewer.update(tn, null);
				
			}
		});

		Label rightTitel = new Label(composite, SWT.NONE);
		rightTitel.setText(NLMessages.getString("Config_value"));
		rightTitel.setLayoutData(new GridData());
		((GridData) rightTitel.getLayoutData()).horizontalSpan = 1;
		((GridData) rightTitel.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) rightTitel.getLayoutData()).horizontalAlignment = SWT.FILL;

		_name = new Text(composite, SWT.BORDER | SWT.READ_ONLY);
		_name.setEditable(false);

		_name.setLayoutData(new GridData());
		((GridData) _name.getLayoutData()).horizontalSpan = 3;
		((GridData) _name.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) _name.getLayoutData()).horizontalAlignment = SWT.FILL;
		if (semanticTemplate.getValue() != null)
		{
			_name.setText(semanticTemplate.getValue());

		}
		_name.addFocusListener(new FocusAdapter()
		{
			@Override
			public void focusLost(final FocusEvent e)
			{
				semanticTemplate.setValue(_name.getText().trim());

			}
		});

		

		Label ignoreLabel = new Label(composite, SWT.NONE);
		ignoreLabel.setText(NLMessages.getString("Config_ignore"));
		ignoreLabel.setLayoutData(new GridData());
		((GridData) ignoreLabel.getLayoutData()).horizontalSpan = 1;
		((GridData) ignoreLabel.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) ignoreLabel.getLayoutData()).horizontalAlignment = SWT.FILL;
		ignoreLabel.pack();

		final Button ignoreButton = new Button(composite, SWT.CHECK);
		ignoreButton.setLayoutData(new GridData());
		ignoreButton.setEnabled(_userRichtsChecker.maySetConfigIgnored());
		((GridData) ignoreButton.getLayoutData()).horizontalSpan = 1;
		ignoreButton.setSelection(semanticTemplate.isIgnore());

		ignoreButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent se)
			{
				semanticTemplate.setIgnore(ignoreButton.getSelection());
				Object[] objects = _treeViewer.getExpandedElements();
				Object input = _treeViewer.getInput();
				_treeViewer.setInput(input);
				for (Object o : objects)
				{
					_treeViewer.setExpandedState(o, true);
				}

			}
		}); // SelectionListener
		if (semanticTemplate.getParent() != null && semanticTemplate.getParent() instanceof ComplexSemanticTemplate)
		{
			Label allowMultipleLabel = new Label(composite, SWT.NONE);
			allowMultipleLabel.setText(NLMessages.getString("Config_allowMultiple"));
			allowMultipleLabel.setLayoutData(new GridData());
			((GridData) allowMultipleLabel.getLayoutData()).horizontalSpan = 1;
			((GridData) allowMultipleLabel.getLayoutData()).grabExcessHorizontalSpace = true;
			((GridData) allowMultipleLabel.getLayoutData()).horizontalAlignment = SWT.FILL;
			allowMultipleLabel.pack();
	
			final Button allowMultipleButton = new Button(composite, SWT.CHECK);
			allowMultipleButton.setLayoutData(new GridData());
			((GridData) allowMultipleButton.getLayoutData()).horizontalSpan = 1;
			allowMultipleButton.setSelection(semanticTemplate.isAllowMultiple());
	
			allowMultipleButton.addSelectionListener(new SelectionAdapter()
			{
				@Override
				public void widgetSelected(final SelectionEvent se)
				{
					semanticTemplate.setAllowMultiple(allowMultipleButton.getSelection());
				}
			}); // SelectionListene

			Label base = new Label(composite, SWT.NONE);
			base.setText(NLMessages.getString("Config_priority"));
			base.setLayoutData(new GridData());
			((GridData) base.getLayoutData()).horizontalSpan = 2;
			((GridData) base.getLayoutData()).grabExcessHorizontalSpace = true;
			((GridData) base.getLayoutData()).horizontalAlignment = SWT.FILL;
			base.pack();

			_baseSpinner = new Spinner(composite, SWT.NONE);
			_baseSpinner.setLayoutData(new GridData());
			_baseSpinner.setEnabled(_userRichtsChecker.mayEditConfig());
			((GridData) _baseSpinner.getLayoutData()).horizontalSpan = 2;
			((GridData) _baseSpinner.getLayoutData()).grabExcessHorizontalSpace = true;
			((GridData) _baseSpinner.getLayoutData()).horizontalAlignment = SWT.FILL;

			_baseSpinner.setMinimum(0);
			_baseSpinner.setMaximum(999);

			_baseSpinner.setSelection(semanticTemplate.getPriority());
			_baseSpinner.addSelectionListener(new SelectionAdapter()
			{
				@Override
				public void widgetSelected(final SelectionEvent se)
				{

					semanticTemplate.setPriority(_baseSpinner.getSelection());

				}
			}); // SelectionListener
		}
		else
		{
			((GridData) ignoreLabel.getLayoutData()).horizontalSpan = 2;
			((GridData) ignoreButton.getLayoutData()).horizontalSpan = 2;

		}
		

		Label l = new Label(composite, SWT.NONE);
		l.setText(NLMessages.getString("Editor_add_field"));
		l.setLayoutData(new GridData());

		_addElement = new Text(composite, SWT.BORDER);
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
					if (semanticTemplate.getChildren() != null && semanticTemplate.getChildren().containsKey(name))
					{
						_addMarkupTemplate.setEnabled(false);
						// setMessage("Warning. A Markup Element with the same name exists already!");
					}
					else
					{
						_addMarkupTemplate.setEnabled(_userRichtsChecker.mayEditConfig());
					}
				}
				else
				{
					_addMarkupTemplate.setEnabled(false);
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
					String value = _addElement.getText().trim();
					AspectConfigTemplate configTemplate = new AspectConfigTemplate(value);
					configTemplate.setParent(semanticTemplate);
					semanticTemplate.getChildren().put(value, configTemplate);
					configTemplate.setPriority(semanticTemplate.getChildren().size());
					configTemplate.setHorizontalSpan(1);
					Object[] objects = _treeViewer.getExpandedElements();
					Object input = _treeViewer.getInput();
					ISelection iSelection = _treeViewer.getSelection();
					Object obj = ((IStructuredSelection) iSelection).getFirstElement();
					_treeViewer.setInput(input);
					for (Object o : objects)
					{
						_treeViewer.setExpandedState(o, true);
					}
					_treeViewer.setExpandedState(obj, true);
					_addElement.setText("");
				}
			}

			@Override
			public void keyReleased(final KeyEvent e)
			{
				// TODO Auto-generated method stub

			}
		});
		_addMarkupTemplate = new Button(composite, SWT.PUSH | SWT.END);
		_addMarkupTemplate.setText(NLMessages.getString("Config_add"));
		_addMarkupTemplate.setLayoutData(new GridData());
		((GridData) _addMarkupTemplate.getLayoutData()).verticalAlignment = SWT.FILL;
		_addMarkupTemplate.setEnabled(false);
		_addMarkupTemplate.setFocus();

		_addMarkupTemplate.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent e)
			{
				String value = _addElement.getText().trim();
				AspectConfigTemplate configTemplate = new AspectConfigTemplate(value);
				configTemplate.setParent(semanticTemplate);
				semanticTemplate.getChildren().put(value, configTemplate);
				configTemplate.setPriority(semanticTemplate.getChildren().size());
				configTemplate.setHorizontalSpan(1);
				Object[] objects = _treeViewer.getExpandedElements();
				Object input = _treeViewer.getInput();
				ISelection iSelection = _treeViewer.getSelection();
				Object obj = ((IStructuredSelection) iSelection).getFirstElement();
				_treeViewer.setInput(input);
				for (Object o : objects)
				{
					_treeViewer.setExpandedState(o, true);
				}
				_treeViewer.setExpandedState(obj, true);
				_addElement.setText("");
			}
		});
		
		Label langLabel = new Label(composite, SWT.NONE);
		langLabel.setText(NLMessages.getString("Config_language"));
		langLabel.setLayoutData(new GridData());
		((GridData) langLabel.getLayoutData()).horizontalSpan = 1;

		final Combo langCombo = new Combo(composite, SWT.NONE | SWT.READ_ONLY);
		langCombo.setLayoutData(new GridData());
		((GridData) langCombo.getLayoutData()).horizontalSpan = 1;
		((GridData) langCombo.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) langCombo.getLayoutData()).horizontalAlignment = SWT.FILL;
		langCombo.setItems(_langs);

		Label docu = new Label(composite, SWT.NONE);
		docu.setText(NLMessages.getString("Config_documentation"));
		docu.setLayoutData(new GridData());
		((GridData) docu.getLayoutData()).horizontalSpan = 2;

		final Text docuText = new Text(composite, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
		docuText.setLayoutData(new GridData());
		docuText.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		((GridData) docuText.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) docuText.getLayoutData()).verticalAlignment = SWT.FILL;
		((GridData) docuText.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) docuText.getLayoutData()).grabExcessVerticalSpace = true;

		if (semanticTemplate.getDocumentation() != null
				&& semanticTemplate.getDocumentation().containsKey(_systemLang))
		{
			_lang = _systemLang;
			langCombo.select(langCombo.indexOf((_systemLang)));
			docuText.setText(semanticTemplate.getDocumentation().get((_systemLang)));

		}
		else if (semanticTemplate.getDocumentation() != null
				&& semanticTemplate.getDocumentation().get("de") != null) //$NON-NLS-1$
		{
			_lang = "de"; //$NON-NLS-1$
			langCombo.select(langCombo.indexOf((_lang)));
			docuText.setText(semanticTemplate.getDocumentation().get((_lang)));

		}
		else if (semanticTemplate.getDocumentation() != null
				&& semanticTemplate.getDocumentation().get("en") != null) //$NON-NLS-1$
		{
			_lang = "en"; //$NON-NLS-1$

			langCombo.select(langCombo.indexOf((_lang)));
			docuText.setText(semanticTemplate.getDocumentation().get((_lang)));

		}
		else if (semanticTemplate.getDocumentation() != null
				&& semanticTemplate.getDocumentation().get("it") != null) //$NON-NLS-1$
		{
			_lang = "it"; //$NON-NLS-1$

			langCombo.select(langCombo.indexOf((_lang)));
			docuText.setText(semanticTemplate.getDocumentation().get((_lang)));

		}
		else if (semanticTemplate.getDocumentation() != null
				&& semanticTemplate.getDocumentation().get("fr") != null) //$NON-NLS-1$
		{
			_lang = "fr"; //$NON-NLS-1$

			langCombo.select(langCombo.indexOf((_lang)));
			docuText.setText(semanticTemplate.getDocumentation().get((_lang)));

		}
		else
		{
			langCombo.select(langCombo.indexOf((_systemLang)));
			_lang = _systemLang;
		}

		langCombo.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent se)
			{

				_lang = langCombo.getItem(langCombo.getSelectionIndex());
				if (semanticTemplate.getDocumentation() != null
						&& semanticTemplate.getDocumentation().containsKey(_lang))
				{
					docuText.setText(semanticTemplate.getDocumentation().get((_lang)));
				}
				else
				{
					docuText.setText(""); //$NON-NLS-1$
				}

			}
		}); // SelectionListener

		((GridData) docuText.getLayoutData()).horizontalSpan = 4;
		((GridData) docuText.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) docuText.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) docuText.getLayoutData()).minimumHeight = 60;

		//

		if (semanticTemplate.getDocumentation() != null
				&& semanticTemplate.getDocumentation().containsKey("de")) //$NON-NLS-1$
		{
			docuText.setText(semanticTemplate.getDocumentation().get(("de"))); //$NON-NLS-1$
		}
		else
		{
			docuText.setText(""); //$NON-NLS-1$
		}
		docuText.addFocusListener(new FocusAdapter()
		{
			@Override
			public void focusLost(final FocusEvent e)
			{
				// System.out.println(" ################# lang " + _lang +
				// " docu " + docuText.getText());
				semanticTemplate.getDocumentation().put(_lang, docuText.getText());
			}
		});

		final Button previewButton = new Button(composite, SWT.PUSH);
		previewButton.setText(NLMessages.getString("Config_Preview"));
		previewButton.setLayoutData(new GridData());
		((GridData) previewButton.getLayoutData()).horizontalSpan = 2;
		((GridData) previewButton.getLayoutData()).verticalAlignment = SWT.FILL;

		previewButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent se)
			{
				IWorkbench workbench = PlatformUI.getWorkbench();
				Display display = workbench.getDisplay();
				Shell shell = new Shell(display);
				AspectTemplatePreviewDialog dialog = new AspectTemplatePreviewDialog(shell, semanticTemplate);
				dialog.open();

			}
		}); // SelectionListener

		if (semanticTemplate.getParent() != null && semanticTemplate.getParent() instanceof ComplexSemanticTemplate)
		{
			_deleteFromList = new Button(composite, SWT.PUSH | SWT.END);
			_deleteFromList.setText(NLMessages.getString("Config_delete_entry"));
			_deleteFromList.setToolTipText(NLMessages.getString("Config_delete_entry_tooltip"));
			_deleteFromList.setLayoutData(new GridData());
			// deleteFromList.setEnabled(false);

			if (_userRichtsChecker.mayEditConfig())
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
					delete(semanticTemplate);

				}


			});
		}

		composite.layout();
		return composite;
	}

	private void delete(ConfigData configdata)
	{
		IStructuredSelection selection = (IStructuredSelection) _treeViewer.getSelection();
		Object obj = selection.getFirstElement();
		ConfigTreeNode parent = ((ConfigTreeNode) obj).getParent();
		configdata.remove();
		_treeViewer.setSelection(new StructuredSelection(parent));
		Object[] objects = _treeViewer.getExpandedElements();
		Object input = _treeViewer.getInput();
		_treeViewer.setInput(input);
		for (Object o : objects)
		{
			_treeViewer.setExpandedState(o, true);
		}

	}
	@Override
	public boolean isEditableObject(ConfigData configData)
	{
		if (configData.getValue().equals("aspectTemplates"))
		{
			return true;
		}
		else if (configData.getValue().equals("complexTemplates"))
		{
			return true;
		}
		else if (configData.getValue().equals("semanticTemplates"))
		{
			return true;
		}
		else if (configData instanceof SemanticTemplate)
		{
			return true;
		}
		else if (configData instanceof AspectConfigTemplate)
		{
			return true;
		}
		else if (configData instanceof ComplexSemanticTemplate)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * Sets the combo viewer by string.
	 * @param cv the cv
	 * @param s the s
	 */
	private void setComboViewerByString(ComboViewer cv, String s)
	{
		boolean notincl = false;
		boolean add = false;
		if (cv.getInput() == null || !(cv.getInput() instanceof HashMap<?, ?>))
		{
			cv.setInput(new HashMap<String, ConfigData>());
		}
		if (cv.getInput() instanceof HashMap<?, ?>)
		{
			@SuppressWarnings("unchecked")
			HashMap<String, ConfigData> inputs = (HashMap<String, ConfigData>) cv.getInput();
			if (add && !inputs.containsKey(s))
			{
				notincl = true;
				ConfigItem ci = new ConfigItem();
				ci.setValue(s);
				ci.setLabel(s);
				inputs.put(s, ci);
				cv.setInput(inputs);
				cv.refresh();
			}
			if (inputs.containsKey(s))
			{
				// System.out.println("contains key s " + s);
				for (String key : inputs.keySet())
				{
					if (key.equals(s))
					{
						ConfigData cd = inputs.get(key);
						if (cd instanceof ConfigItem && ((ConfigItem) cd).isIgnore())
						{
							((ConfigItem) cd).setReadAlthoughIgnored(true);
							cv.setInput(inputs);
						}
						StructuredSelection sel = new StructuredSelection(cd);
						cv.setSelection(sel, true);
						break;
					}
				}
			}
			if (notincl)
			{
				cv.getCombo().select(0);
			}
		}

	}

	private void processMarkupComboSettings(AspectConfigTemplate aspectConfigTemplate)
	{
		if (aspectConfigTemplate.getPos() != null
				&& (aspectConfigTemplate.getWidgetType() == 1 || aspectConfigTemplate.getWidgetType() == 3))
		{
			final String type = aspectConfigTemplate.getPos();

			//                 System.out.println("aspectFacetQuery key set to " + type); //$NON-NLS-1$
			if (type.equals("type")) //$NON-NLS-1$
			{
				aspectConfigTemplate.setType(null);
				aspectConfigTemplate.setSubtype(null);
				aspectConfigTemplate.setRole(null);
			}
			else if (type.equals("subtype")) //$NON-NLS-1$
			{
				aspectConfigTemplate.setSubtype(null);
				aspectConfigTemplate.setRole(null);
			}
			else if (type.equals("role")) //$NON-NLS-1$
			{
				aspectConfigTemplate.setRole(null);
			}

		}

	}
}
