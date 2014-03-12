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
package org.bbaw.pdr.ae.view.control.customSWTWidges;

import java.util.ArrayList;
import java.util.HashMap;

import org.bbaw.pdr.ae.common.AEConstants;
import org.bbaw.pdr.ae.common.AEVIEWConstants;
import org.bbaw.pdr.ae.common.CommonActivator;
import org.bbaw.pdr.ae.common.NLMessages;
import org.bbaw.pdr.ae.common.icons.IconsInternal;
import org.bbaw.pdr.ae.config.model.AspectConfigTemplate;
import org.bbaw.pdr.ae.config.model.ConfigData;
import org.bbaw.pdr.ae.config.model.ConfigItem;
import org.bbaw.pdr.ae.control.facade.Facade;
import org.bbaw.pdr.ae.model.TaggingRange;
import org.bbaw.pdr.ae.view.control.ViewHelper;
import org.bbaw.pdr.ae.view.control.dialogs.SelectOwnerAndKeyDialog;
import org.bbaw.pdr.ae.view.control.interfaces.IAEBasicEditor;
import org.bbaw.pdr.ae.view.control.provider.MarkupContentProvider;
import org.bbaw.pdr.ae.view.control.provider.MarkupLabelProvider;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
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
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Text;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class AEMarkupWidget extends Composite implements IAEBasicEditor
{


	private Element _inputElement;

	private boolean _requiered;

	private int _widgetType;

	private Composite _parentComposite;

	private Text _markupText;

	private IAEBasicEditor _parentEditor;

	private AspectConfigTemplate _markupTemplate;

	/** The markup provider. */
	private String _markupProvider = Platform
			.getPreferencesService()
			.getString(CommonActivator.PLUGIN_ID, "PRIMARY_TAGGING_PROVIDER", AEConstants.TAGGING_LIST_PROVIDER, null).toUpperCase(); //$NON-NLS-1$;

	private Facade _facade = Facade.getInstanz();

	private boolean _mayWrite;

	private ComboViewer _comboTaggingViewer;

	private boolean _isValid;

	private MarkupDateEditorLine _dateEditor;

	private Combo _comboTagging;

	/** The _selection listener. */
	private ArrayList<SelectionListener> _selectionListeners = new ArrayList<SelectionListener>();

	private boolean _loading;

	private MarkupDateComboEditorLine _dateComboEditor;

	private boolean _selected;

	private boolean _editable = true;

	/** Instance of shared image registry. */
	private ImageRegistry _imageReg = CommonActivator.getDefault().getImageRegistry();


	public AEMarkupWidget(IAEBasicEditor editor, Composite parent, AspectConfigTemplate template, boolean maywrite,
			int style)
	{
		super(parent, SWT.NONE);
		this.setLayout(new GridLayout(1, false));
		((GridLayout) this.getLayout()).marginHeight = 0;
		((GridLayout) this.getLayout()).verticalSpacing = 0;
		((GridLayout) this.getLayout()).marginWidth = 0;
		this.setLayoutData(new GridData());
		((GridData) this.getLayoutData()).heightHint = 20;

		_parentComposite = parent;
		_markupTemplate = template;
		_widgetType = template.getWidgetType();
		_parentEditor = editor;
		_mayWrite = maywrite;
		_requiered = template.isRequired();
		template.getLabel();
		createMarkupWidget();
	}

	public final void addSelectionListener(final SelectionListener selectionListener)
	{
		if (selectionListener != null)
		{
			_selectionListeners.add(selectionListener);
		}

	}
	private void createMarkupWidget()
	{
		this.setLayout(new GridLayout(1, false));
		((GridLayout) this.getLayout()).marginHeight = 0;
		((GridLayout) this.getLayout()).verticalSpacing = 0;
		((GridLayout) this.getLayout()).marginWidth = 0;

		_parentComposite = new Composite(this, SWT.NONE);
		_parentComposite.setLayoutData(new GridData());
		((GridData) _parentComposite.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _parentComposite.getLayoutData()).grabExcessHorizontalSpace = true;
		if (Platform.getPreferencesService().getBoolean(CommonActivator.PLUGIN_ID, "ASPECT_LITE_EDIT_ANA_KEY",
				AEConstants.ASPECT_LITE_EDIT_ANA_KEY, null))
		{
			_parentComposite.setLayout(new GridLayout(3, false));
		}
		else
		{
			_parentComposite.setLayout(new GridLayout(2, false));
		}
		((GridLayout) _parentComposite.getLayout()).marginHeight = 0;
		((GridLayout) _parentComposite.getLayout()).verticalSpacing = 0;
		((GridLayout) _parentComposite.getLayout()).marginWidth = 0;

		
		if (_widgetType == AEAspectWidgetCustomizable.TYPE_TEXT)
		{
			createMarkupTextWidget();
		}
		else if (_widgetType == AEAspectWidgetCustomizable.TYPE_COMBO)
		{
			createMarkupComboWidget();
		}
		else if (_widgetType == AEAspectWidgetCustomizable.TYPE_DATE)
		{
			createMarkupDateWidget();
		}
		else if (_widgetType == AEAspectWidgetCustomizable.TYPE_DATE_COMBO)
		{
			createMarkupDateComboWidget();
		}
		if (_markupTemplate.getWidgetType() < 4)
		{
			if (Platform.getPreferencesService().getBoolean(CommonActivator.PLUGIN_ID, "ASPECT_LITE_EDIT_ANA_KEY",
					AEConstants.ASPECT_LITE_EDIT_ANA_KEY, null))
			{
				Button anaKeyButton = new Button(_parentComposite, SWT.PUSH);
				anaKeyButton.setImage(_imageReg.get(IconsInternal.KEY));
				anaKeyButton.setToolTipText(NLMessages.getString("Editor_edit_ana_key"));
				anaKeyButton.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(SelectionEvent e)
					{
						String ana = _inputElement.getAttribute("ana");
						String key = _inputElement.getAttribute("key");
						TaggingRange tr = new TaggingRange();
						tr.setAna(ana);
						tr.setKey(key);
						Dialog dialog = new SelectOwnerAndKeyDialog(_parentComposite.getShell(), tr);
						int returnCode = dialog.open();
						if (returnCode == 0)
						{
							_inputElement.setAttribute("ana", tr.getAna());
							_inputElement.setAttribute("key", tr.getKey());
							contentChanged();
						}

					}
				});

			}
		}
	}

	private void createMarkupDateComboWidget()
	{
		_dateComboEditor = new MarkupDateComboEditorLine(AEMarkupWidget.this, _markupTemplate, _parentComposite, SWT.None);
		_dateComboEditor.setLayoutData(new GridData());
		((GridData) _dateComboEditor.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _dateComboEditor.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) _dateComboEditor.getLayoutData()).horizontalSpan = 2;
		_dateComboEditor.setSelected(false, true);
		_dateComboEditor.addSelectionListener(new SelectionAdapter()
		{

			@Override
			public void widgetSelected(SelectionEvent e)
			{
				Event ee = new Event();
				// ee.data = EasyAspectEditor.this;
				ee.widget = AEMarkupWidget.this;
				SelectionEvent se = new SelectionEvent(ee);
				se.data = AEMarkupWidget.this;
				setDirty(true);
				for (SelectionListener s : _selectionListeners)
				{
					s.widgetSelected(se);
				}

			}

		});

		_parentComposite.redraw();
		_parentComposite.layout();
		_parentComposite.update();

	}

	public int getWidgetType()
	{
		return _widgetType;
	}

	public void setWidgetType(int widgetType)
	{
		this._widgetType = widgetType;
	}

	private void createMarkupTextWidget()
	{

		_markupText = new Text(_parentComposite, SWT.BORDER);
		_markupText.setBackground(AEVIEWConstants.VIEW_BACKGROUND_DESELECTED_COLOR);
		_markupText.setLayoutData(new GridData());
		((GridData) _markupText.getLayoutData()).horizontalSpan = 2;
		((GridData) _markupText.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _markupText.getLayoutData()).grabExcessHorizontalSpace = true;
		_markupText.setEnabled(_mayWrite);

		_markupText.addKeyListener(new KeyListener()
		{

			@Override
			public void keyReleased(KeyEvent e)
			{
				contentChanged();

			}

			@Override
			public void keyPressed(KeyEvent e)
			{

			}

		});

		_markupText.addFocusListener(new FocusListener()
		{

			@Override
			public void focusGained(final FocusEvent e)
			{
				Event ee = new Event();
				// ee.data = EasyAspectEditor.this;
				ee.widget = AEMarkupWidget.this;
				SelectionEvent se = new SelectionEvent(ee);
				se.data = AEMarkupWidget.this;
				for (SelectionListener s : _selectionListeners)
				{
					s.widgetSelected(se);
				}
//				validate();

			}

			@Override
			public void focusLost(FocusEvent e)
			{
//				saveInput();
//				validate();

			}

		});
		_markupText.addModifyListener(new ModifyListener()
		{

			@Override
			public void modifyText(ModifyEvent e)
			{

				contentChanged();

			}
		});
	}
	private void contentChanged()
	{
		setDirty(true);
		saveInput();
		validateInternal();
		validate();
	}
	
	private void validateInternal()
	{
		if (_requiered)
		{
			if (_widgetType == AEAspectWidgetCustomizable.TYPE_TEXT)
			{
				setValid(_markupText.getText().trim().length() > 0);
			}
			else if (_widgetType == AEAspectWidgetCustomizable.TYPE_COMBO)
			{
				setValid(true);
			}
			else if (_widgetType == AEAspectWidgetCustomizable.TYPE_DATE)
			{
				setValid(_dateEditor.isValid());
			}
			else if (_widgetType == AEAspectWidgetCustomizable.TYPE_DATE_COMBO)
			{
				setValid(_dateComboEditor.isValid());
			}
		}
		else
		{
			setValid(true);
		}
	}

	private void setValid(boolean valid) {
		this._isValid = valid;
		
	}

	public void validate()
	{
		if (_parentEditor != null)
		{
			_parentEditor.validate();
		}
	}

	private void createMarkupComboWidget()
	{
		_comboTagging = new Combo(_parentComposite, SWT.READ_ONLY);
		_comboTagging.setLayoutData(new GridData());
		((GridData) _comboTagging.getLayoutData()).horizontalAlignment = GridData.FILL;
		((GridData) _comboTagging.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) _comboTagging.getLayoutData()).verticalAlignment = GridData.BEGINNING;
		((GridData) _comboTagging.getLayoutData()).horizontalSpan = 2;

		_comboTaggingViewer = new ComboViewer(_comboTagging);
		_comboTaggingViewer.setContentProvider(new MarkupContentProvider());
		_comboTaggingViewer.setLabelProvider(new MarkupLabelProvider());
		if (_markupTemplate != null && _markupTemplate.getPos() != null)
		{
			ConfigData input = null;
			if (_markupTemplate.getPos().equals("type") && _facade.getConfigs().containsKey(_markupProvider))
			{
				HashMap<String, ConfigData> inputs = _facade.getConfigs().get(_markupProvider).getChildren()
						.get("aodl:" + _markupTemplate.getElement()).getChildren();
				HashMap<String, ConfigItem> clones = new HashMap<String, ConfigItem>(inputs.size());
				for (String key : inputs.keySet())
				{
					ConfigItem clone = (ConfigItem) inputs.get(key).clone();
					clones.put(new String(key), clone);
				}

				if (_markupTemplate.getLevelSpan() == 2)
				{
					for (String k : inputs.keySet())
					{
						ConfigData cd = inputs.get(k);
						if (cd != null)
						{
							for (String kk : cd.getChildren().keySet())
							{
								ConfigItem clone = (ConfigItem) cd.getChildren().get(kk).clone();
								clones.put(new String(kk), clone);
							}
						}
					}
				}
				if (_markupTemplate.getLevelSpan() == 3)
				{
					for (String k : inputs.keySet())
					{
						ConfigData cd = inputs.get(k);
						if (cd != null)
						{
							for (String kk : cd.getChildren().keySet())
							{
								ConfigData cd2 = cd.getChildren().get(kk);
								if (cd2 != null)
								{
									clones.put(new String(kk), ((ConfigItem) cd2).clone());
									for (String kkk : cd2.getChildren().keySet())
									{
										clones.put(new String(kkk), (ConfigItem) cd2.getChildren().get(kkk).clone());
									}
								}
							}
						}
					}
				}
				_comboTaggingViewer.setInput(clones);
				_comboTagging.setEnabled(this.isEnabled());

			}
			else if (_markupTemplate.getPos().equals("subtype") && _facade.getConfigs().containsKey(_markupProvider))
			{
				if (_facade.getConfigs().get(_markupProvider).getChildren().get("aodl:" + _markupTemplate.getElement()) != null)
				{
					input = _facade.getConfigs().get(_markupProvider).getChildren()
							.get("aodl:" + _markupTemplate.getElement()).getChildren().get(_markupTemplate.getType());
				}
				if (input != null)
				{
					HashMap<String, ConfigData> inputs = input.getChildren();
					HashMap<String, ConfigItem> clones = new HashMap<String, ConfigItem>(inputs.size());
					for (String key : inputs.keySet())
					{
						ConfigItem clone = (ConfigItem) inputs.get(key).clone();
						clones.put(new String(key), clone);
					}
					if (_markupTemplate.getLevelSpan() == 2)
					{
						for (String k : inputs.keySet())
						{
							ConfigData cd = inputs.get(k);
							if (cd != null)
							{
								for (String kk : cd.getChildren().keySet())
								{
									ConfigItem clone = (ConfigItem) cd.getChildren().get(kk).clone();
									clones.put(new String(kk), clone);
								}
							}
						}
					}
					_comboTaggingViewer.setInput(clones);
					_comboTagging.setEnabled(this.isEnabled());

				}

			}
			else if (_markupTemplate.getPos().equals("role") && _facade.getConfigs().containsKey(_markupProvider))
			{
				if (_facade.getConfigs().get(_markupProvider).getChildren().get("aodl:" + _markupTemplate.getElement()) != null
						&& _facade.getConfigs().get(_markupProvider).getChildren()
								.get("aodl:" + _markupTemplate.getElement()).getChildren()
								.get(_markupTemplate.getType()) != null)
				{
					input = _facade.getConfigs().get(_markupProvider).getChildren()
							.get("aodl:" + _markupTemplate.getElement()).getChildren().get(_markupTemplate.getType())
							.getChildren().get(_markupTemplate.getSubtype());
				}
				if (input != null)
				{
					HashMap<String, ConfigData> inputs = input.getChildren();
					HashMap<String, ConfigItem> clones = new HashMap<String, ConfigItem>(inputs.size());
					for (String key : inputs.keySet())
					{
						ConfigItem clone = (ConfigItem) inputs.get(key).clone();
						clones.put(new String(key), clone);
					}
					_comboTaggingViewer.setInput(clones);
					_comboTagging.setEnabled(this.isEnabled());

				}
			}
		}
		_comboTagging.addFocusListener(new FocusListener()
		{

			@Override
			public void focusGained(final FocusEvent e)
			{
				Event ee = new Event();
				// ee.data = EasyAspectEditor.this;
				ee.widget = AEMarkupWidget.this;
				SelectionEvent se = new SelectionEvent(ee);
				se.data = AEMarkupWidget.this;
				for (SelectionListener s : _selectionListeners)
				{
					s.widgetSelected(se);
				}
			}

			@Override
			public void focusLost(FocusEvent e)
			{
//				saveInput();

			}

		});

		final ControlDecoration typeDeco = new ControlDecoration(_comboTagging, SWT.RIGHT | SWT.TOP);
		_comboTaggingViewer.addSelectionChangedListener(new ISelectionChangedListener()
		{

			@Override
			public void selectionChanged(final SelectionChangedEvent event)
			{
				ISelection iSelection = event.getSelection();
				Object obj = ((IStructuredSelection) iSelection).getFirstElement();
				ConfigData cd = (ConfigData) obj;
				if (cd != null)
				{
					if (_inputElement != null)
					{
						String deco = cd.getDescription();
						if (deco != null && deco.trim().length() > 0)
						{
							typeDeco.setDescriptionText(deco);
							typeDeco.setImage(FieldDecorationRegistry.getDefault()
									.getFieldDecoration(FieldDecorationRegistry.DEC_INFORMATION).getImage());
						}
						else
						{
							typeDeco.setImage(null);
						}
						_inputElement.setTextContent(cd.getContent());
						setValid(true);
						if (cd.getPos() != null)
						{
							if (cd.getPos().equals("type"))
							{
								setAttributeNodeValue("type", cd.getValue());
								setAttributeNodeValue("subtype", null);
								setAttributeNodeValue("role", null);
							}
							else if (cd.getPos().equals("subtype"))
							{
								setAttributeNodeValue("subtype", cd.getValue());
								ConfigItem ci = (ConfigItem) cd;
								setAttributeNodeValue("type", ci.getParent().getValue());
								setAttributeNodeValue("role", null);
							}
							else if (cd.getPos().equals("role"))
							{
								setAttributeNodeValue("role", cd.getValue());
								ConfigItem ci = (ConfigItem) cd;
								setAttributeNodeValue("subtype", ci.getParent().getValue());
								ci = (ConfigItem) ci.getParent();
								setAttributeNodeValue("type", ci.getParent().getValue());
							}
						}

					}
				}
				if (!_loading)
				{
					contentChanged();
				}
			}
		});

		_parentComposite.redraw();
		_parentComposite.layout();
		_parentComposite.update();

	}
	private void setAttributeNodeValue(String attributeName, String value) {
		if (attributeName != null)
		{
			Node node = _inputElement.getAttributeNode(attributeName);
			if (node != null)
			{
				node.setNodeValue(value);
			}
			else
			{
				_inputElement.setAttribute(attributeName, value);
			}
		}
		
		
	}

	private void createMarkupDateWidget()
	{
		_dateEditor = new MarkupDateEditorLine(AEMarkupWidget.this, _markupTemplate, _parentComposite, SWT.None);
		_dateEditor.setLayoutData(new GridData());
		((GridData) _dateEditor.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _dateEditor.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) _dateEditor.getLayoutData()).horizontalSpan = 2;
		_dateEditor.setSelected(false, true);
		_dateEditor.addSelectionListener(new SelectionAdapter()
		{

			@Override
			public void widgetSelected(SelectionEvent e)
			{
				Event ee = new Event();
				// ee.data = EasyAspectEditor.this;
				ee.widget = AEMarkupWidget.this;
				SelectionEvent se = new SelectionEvent(ee);
				se.data = AEMarkupWidget.this;
				setDirty(true);
				for (SelectionListener s : _selectionListeners)
				{
					s.widgetSelected(se);
				}

			}

		});
		_parentComposite.redraw();
		_parentComposite.layout();
		_parentComposite.update();

	}

	public Node getInputNode()
	{
		return _inputElement;
	}

	@Override
	public void setInput(Object o)
	{
		if (o instanceof Element)
		{
			this._inputElement = (Element) o;
			_loading = true;
			loadInput();
			_loading = false;
		}

	}

	private void loadInput()
	{
		if (_widgetType == AEAspectWidgetCustomizable.TYPE_TEXT)
		{
			loadMarkupText();
		}
		else if (_widgetType == AEAspectWidgetCustomizable.TYPE_COMBO)
		{
			loadMarkupCombo();
		}
		else if (_widgetType == AEAspectWidgetCustomizable.TYPE_DATE)
		{
			loadMarkupDate();
		}
		else if (_widgetType == AEAspectWidgetCustomizable.TYPE_DATE_COMBO)
		{
			loadMarkupDateCombo();
		}
		this.redraw();
		this.layout();
		this.update();
	}

	private void loadMarkupText()
	{
		_markupText.setText(_inputElement.getTextContent());
	}

	private void loadMarkupCombo()
	{

			if (_inputElement.getAttribute("type") != null
					&& (_inputElement.getAttribute("subtype") == null || _inputElement.getAttribute("subtype").trim()
						.length() == 0))
				{
//				System.out.println("set combo by type");
			if (_inputElement.getTextContent().trim().length() > 0)
			{
				ViewHelper.setComboViewerByString(_comboTaggingViewer, _inputElement.getAttribute("type"), !_loading);
			}
				}
			else if (_inputElement.getAttribute("type") != null
					&& (_inputElement.getAttribute("subtype") != null || _inputElement.getAttribute("subtype").trim()
							.length() > 0)
					&& (_inputElement.getAttribute("role") == null || _inputElement.getAttribute("role").trim()
							.length() == 0))
				{
//				System.out.println("set combo by subtype");

			if (_inputElement.getTextContent().trim().length() > 0)
			{
				ViewHelper.setComboViewerByString(_comboTaggingViewer, _inputElement.getAttribute("subtype"), !_loading);

			}
				}
			else if (_inputElement.getAttribute("type") != null
					&& (_inputElement.getAttribute("subtype") != null || _inputElement.getAttribute("subtype").trim()
						.length() > 0)
					&& (_inputElement.getAttribute("role") != null || _inputElement.getAttribute("role").trim()
							.length() > 0))
				{
//				System.out.println("set combo by role");
			if (_inputElement.getTextContent().trim().length() > 0)
			{
				ViewHelper.setComboViewerByString(_comboTaggingViewer, _inputElement.getAttribute("role"), !_loading);

			}
				}
			// }

		// }
			_comboTagging.redraw();
			_comboTagging.layout();
			_comboTagging.update();
			_comboTagging.pack();

	}

	private void loadMarkupDate()
	{
		_dateEditor.setInput(_inputElement);

	}

	private void loadMarkupDateCombo()
	{
		_dateComboEditor.setInput(_inputElement);

	}

	public void setDirty(boolean b)
	{
		if (!_loading && b && _parentEditor != null)
		{
			_parentEditor.setDirty(true);
		}
	}

	

	@Override
	public boolean isDirty()
	{
		return false;
	}

	@Override
	public boolean isValid()
	{
//		if (_requiered)
//		{
//			if (_widgetType == AEAspectWidgetCustomizable.TYPE_TEXT)
//			{
//				setValid(_markupText.getText().trim().length() > 0);
//			}
//			else if (_widgetType == AEAspectWidgetCustomizable.TYPE_COMBO)
//			{
////				loadMarkupCombo();
//			}
//			else if (_widgetType == AEAspectWidgetCustomizable.TYPE_DATE)
//			{
////				loadMarkupDate();
//				setValid(_dateEditor.isValid());
//			}
//			else if (_widgetType == AEAspectWidgetCustomizable.TYPE_DATE_COMBO)
//			{
////				loadMarkupDateCombo();
//				setValid(_dateComboEditor.isValid());
//			}
//		}
//		else
//		{
//			setValid(true);
//		}
		return _isValid;
	}

	@Override
	public void setSelected(boolean isSelected, boolean contextIsValid)
	{
		this._selected = isSelected;
		if (_dateEditor != null)
		{
			_dateEditor.setSelected(_selected, contextIsValid);
		}
		if (_dateComboEditor != null)
		{
			_dateComboEditor.setSelected(_selected, contextIsValid);
		}
		if (isSelected && contextIsValid)
		{
			setBackground(AEVIEWConstants.VIEW_BACKGROUND_SELECTED_COLOR);
			setForeground(AEVIEWConstants.VIEW_FOREGROUND_SELECTED_COLOR);
		}
		else if (contextIsValid)
		{
			setForeground(AEVIEWConstants.VIEW_FOREGROUND_DESELECTED_COLOR);
			setBackground(AEVIEWConstants.VIEW_BACKGROUND_INVALID_COLOR);
		}
		else if (isSelected)
		{
			setBackground(AEVIEWConstants.VIEW_BACKGROUND_INVALID_COLOR);
			setForeground(AEVIEWConstants.VIEW_FOREGROUND_SELECTED_COLOR);
		}
		else
		{
			setBackground(AEVIEWConstants.VIEW_BACKGROUND_DESELECTED_COLOR);
			setForeground(AEVIEWConstants.VIEW_FOREGROUND_DESELECTED_COLOR);
		}
		_parentComposite.redraw();
		_parentComposite.layout();
		_parentComposite.update();
	}

	@Override
	public void setBackground(Color color)
	{
		super.setBackground(color);
		_parentComposite.setBackground(color);
	}

	@Override
	public void setForeground(Color color)
	{
		super.setForeground(color);
		if (_markupText != null)
		{
			_markupText.setForeground(color);
		}
		if (_comboTagging != null)
		{
			_comboTagging.setForeground(color);
		}
	}

//	@Override
//	public void setValid(boolean isValid)
//	{
//		if (isValid)
//		{
//			setBackground(AEVIEWConstants.VIEW_BACKGROUND_SELECTED_COLOR);	
//		}
//		else
//		{
//			setBackground(AEVIEWConstants.VIEW_BACKGROUND_INVALID_COLOR);
//		}
//		if (_dateEditor != null)
//		{
//			_dateEditor.setValid(isValid);
//		}
//		if (_dateComboEditor != null)
//		{
//			_dateComboEditor.setValid(isValid);
//		}
//		this._isValid = isValid;
//
//	}

	@Override
	public void saveInput()
	{
		if (!_loading)
		{
			switch (_widgetType)
			{
				case AEAspectWidgetCustomizable.TYPE_TEXT:
					_inputElement.setTextContent(_markupText.getText().trim());
					break;
				case AEAspectWidgetCustomizable.TYPE_COMBO:
//					loadMarkupCombo();
					break;
				case AEAspectWidgetCustomizable.TYPE_DATE:
//					loadMarkupDate();
					break;
				case AEAspectWidgetCustomizable.TYPE_DATE_COMBO:
//					loadMarkupDateCombo();
					break;

			}
			// setDirty(true);
//			validate();
//			
			_parentEditor.saveInput();

		}

	}

	@Override
	public void setEditable(boolean editable) {
		this._editable  = editable;
		if (_comboTagging != null)
		{
			_comboTagging.setEnabled(_editable);
		}
		if (_markupText != null)
		{
			_markupText.setEditable(editable);
		}
		if (_dateEditor != null)
		{
			_dateEditor.setEditable(_editable);
		}
		if (_dateComboEditor != null)
		{
			_dateComboEditor.setEditable(_editable);
		}
		
	}
}
