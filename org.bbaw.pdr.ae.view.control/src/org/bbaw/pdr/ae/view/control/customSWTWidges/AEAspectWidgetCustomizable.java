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
import java.util.Vector;

import org.bbaw.pdr.ae.common.AEConstants;
import org.bbaw.pdr.ae.common.AEVIEWConstants;
import org.bbaw.pdr.ae.common.CommonActivator;
import org.bbaw.pdr.ae.config.model.AspectConfigTemplate;
import org.bbaw.pdr.ae.control.core.PDRConfigProvider;
import org.bbaw.pdr.ae.model.Relation;
import org.bbaw.pdr.ae.model.TimeStm;
import org.bbaw.pdr.ae.model.ValidationStm;
import org.bbaw.pdr.ae.view.control.interfaces.IAEBasicEditor;
import org.bbaw.pdr.ae.view.control.interfaces.IEasyAspectEditor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class AEAspectWidgetCustomizable extends Composite implements IAEBasicEditor,
		Comparable<AEAspectWidgetCustomizable>
{

	public static final int TYPE_TEXT = 0;

	public static final int TYPE_COMBO = 1;

	public static final int TYPE_DATE = 2;

	public static final int TYPE_DATE_COMBO = 3;

	public static final int RELATION = 4;

	public static final int RELATION_DEFINED = 5;

	public static final int TIME_STM = 6;

	public static final int REFERENCE = 7;

	/** Instance of shared image registry. */
	private ImageRegistry _imageReg = CommonActivator.getDefault().getImageRegistry();

	private AspectMarkupTemplate _aspectTemplate;

	/** The markup provider. */
	private String _markupProvider = Platform
			.getPreferencesService()
			.getString(CommonActivator.PLUGIN_ID, "PRIMARY_TAGGING_PROVIDER", AEConstants.TAGGING_LIST_PROVIDER, null).toUpperCase(); //$NON-NLS-1$;

	private AspectConfigTemplate _configTemplate;

	private IEasyAspectEditor _parentEditor;

	private Vector<IAEBasicEditor> _editors;


	private Vector<AEMarkupWidget> _widgets = new Vector<AEMarkupWidget>(2);

	private boolean _maywrite;

	private ControlDecoration _deco;

	/** The _selection listener. */
	private ArrayList<SelectionListener> _selectionListeners = new ArrayList<SelectionListener>();

	private Vector<Button> _multiButtons = new Vector<Button>();


	private Vector<Object> _inputs = new Vector<Object>(2);

	private Composite _mainComposite;


	private Vector<Label> _labels = new Vector<Label>();

	private boolean _selected;

	private boolean _editable = true;

	private boolean _loading;

	private boolean _isValid;

	public AEAspectWidgetCustomizable(Composite parent, int style)
	{
		super(parent, style);
		// TODO Auto-generated constructor stub
	}

	public AEAspectWidgetCustomizable(Composite parent, AspectConfigTemplate configTemplate, int style)
	{
		super(parent, SWT.NONE);

		super.setLayoutData(new GridData());
		((GridData) super.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) super.getLayoutData()).grabExcessHorizontalSpace = true;
		super.setLayout(new GridLayout(1, false));
		((GridLayout) super.getLayout()).marginHeight = 0;
		((GridLayout) super.getLayout()).verticalSpacing = 0;
		((GridLayout) super.getLayout()).marginWidth = 0;

		_aspectTemplate = new AspectMarkupTemplate(null, null, parent, style);
		_configTemplate = configTemplate;
		_maywrite = true;

		createMainComposite(style);

		createAspectWidget(0);
		setSelected(false, true);
	}

	public AEAspectWidgetCustomizable(IEasyAspectEditor editor, AspectMarkupTemplate aspectTemplate,
			AspectConfigTemplate configTemplate, boolean maywrite,
			int style)
	{
		super(aspectTemplate, SWT.NONE);

		super.setLayoutData(new GridData());
		((GridData) super.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) super.getLayoutData()).grabExcessHorizontalSpace = true;
		super.setLayout(new GridLayout(1, false));
		((GridLayout) super.getLayout()).marginHeight = 0;
		((GridLayout) super.getLayout()).verticalSpacing = 0;
		((GridLayout) super.getLayout()).marginWidth = 0;

		_aspectTemplate = aspectTemplate;
		_configTemplate = configTemplate;
		_parentEditor = editor;
		_maywrite = maywrite;

		createMainComposite(style);

		createAspectWidget(0);
		setSelected(false, true);
	}

	private void createMainComposite(int style)
	{
		_mainComposite = new Composite(this, style);

		_mainComposite.setLayoutData(new GridData());
		((GridData) _mainComposite.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _mainComposite.getLayoutData()).grabExcessHorizontalSpace = true;
		if (_configTemplate != null && _configTemplate.isAllowMultiple())
		{
			_mainComposite.setLayout(new GridLayout(3, false));
		}
		else
		{
			_mainComposite.setLayout(new GridLayout(2, false));
		}
		((GridLayout) _mainComposite.getLayout()).marginHeight = 0;
		((GridLayout) _mainComposite.getLayout()).verticalSpacing = 0;
		
	}

	public final void addSelectionListener(final SelectionListener selectionListener)
	{
		if (selectionListener != null)
		{
			_selectionListeners.add(selectionListener);
		}
		if (_widgets != null)
		{
			for (AEMarkupWidget widget : _widgets)
			{
				widget.addSelectionListener(selectionListener);
			}
		}
		if (_editors != null)
		{
			for (IAEBasicEditor editor : _editors)
			{
				editor.addSelectionListener(selectionListener);
			}
		}
	}

	private void createAspectWidget(final int index)
	{
//		System.out.println("createAspectWidget");

		Label label = new Label(_mainComposite, SWT.NONE);
		if (_configTemplate.isRequired())
		{
			label.setText(_configTemplate.getLabel() + "*");
		}
		else
		{
			label.setText(_configTemplate.getLabel());
		}
		_mainComposite.addMouseListener(new MouseAdapter()
		{

			@Override
			public void mouseDown(MouseEvent e)
			{
				Event ee = new Event();
				// ee.data = EasyAspectEditor.this;
				ee.widget = AEAspectWidgetCustomizable.this;
				SelectionEvent se = new SelectionEvent(ee);
				se.data = AEAspectWidgetCustomizable.this;
				for (SelectionListener s : _selectionListeners)
				{
					s.widgetSelected(se);
				}

			}
		});
		label.setLayoutData(new GridData());
		((GridData) label.getLayoutData()).horizontalSpan = 1;
		((GridData) label.getLayoutData()).horizontalAlignment = SWT.RIGHT;
		((GridData) label.getLayoutData()).minimumWidth = 40;
		_deco = new ControlDecoration(label, SWT.LEFT | SWT.BOTTOM);
		_labels.add(label);
		switch (_configTemplate.getWidgetType())
		{
			case AEAspectWidgetCustomizable.TYPE_TEXT:
				createMarkupTextWidget(index);
				break;
			case TYPE_COMBO:
				createMarkupComboWidget(index);
				break;
			case TYPE_DATE:
				createMarkupDateWidget(index);
				break;
			case TYPE_DATE_COMBO:
				createMarkupDateComboWidget(index);
				break;
			case RELATION:
				if (_editors == null)
				{
					_editors = new Vector<IAEBasicEditor>(1);
				}
				createRelationWidget();
				break;
			case RELATION_DEFINED:
				if (_editors == null)
				{
					_editors = new Vector<IAEBasicEditor>(1);
				}
				createDefinedRelationWidget();
				break;
			case TIME_STM:
				if (_editors == null)
				{
					_editors = new Vector<IAEBasicEditor>(1);
				}
				createTimeStmWidget();
				break;
			case REFERENCE:
				if (_editors == null)
				{
					_editors = new Vector<IAEBasicEditor>(1);
				}
				createReferenceWidget();
				break;
			default:
				break;
		}
		String lang = AEConstants.getCurrentLocale().getLanguage();
		String deco = null;

		String path = null;
		if (_configTemplate.getWidgetType() < 4)
		{
			path = PDRConfigProvider.getLabelOfMarkup(_configTemplate.getElement(), null, null, null);
			if (_configTemplate.getType() != null && _configTemplate.getType().trim().length() > 0)
			{
				path += ">>"
						+ PDRConfigProvider.getLabelOfMarkup(_configTemplate.getElement(), _configTemplate.getType(),
								null, null);
			}
			if (_configTemplate.getSubtype() != null && _configTemplate.getSubtype().trim().length() > 0)
			{
				path += ">>"
						+ PDRConfigProvider.getLabelOfMarkup(_configTemplate.getElement(), _configTemplate.getType(),
								_configTemplate.getSubtype(), null);
			}
			if (_configTemplate.getRole() != null && _configTemplate.getRole().trim().length() > 0)
			{
				path += ">>"
						+ PDRConfigProvider.getLabelOfMarkup(_configTemplate.getElement(), _configTemplate.getType(),
								_configTemplate.getSubtype(), _configTemplate.getRole());
			}
			if (path != null)
			{
				path = path.trim();
			}
		}
		else if (_configTemplate.getWidgetType() == 5)
		{
			path = PDRConfigProvider.getLabelOfRelation(_markupProvider, _configTemplate.getElement(), null, null);
			if (_configTemplate.getType() != null && _configTemplate.getType().trim().length() > 0)
			{
				path += ">>"
						+ PDRConfigProvider.getLabelOfRelation(_markupProvider, _configTemplate.getElement(),
								_configTemplate.getType(), null);
			}
			if (_configTemplate.getSubtype() != null && _configTemplate.getSubtype().trim().length() > 0)
			{
				path += ">>"
						+ PDRConfigProvider.getLabelOfRelation(_markupProvider, _configTemplate.getElement(),
								_configTemplate.getType(), _configTemplate.getSubtype());
			}
			if (path != null)
			{
				path = path.trim();
			}
		}
		if (path != null)
		{
			deco = path;
		}
		if (_configTemplate.getDocumentation().get(lang) != null)
		{
			deco = deco + "\n\n" + _configTemplate.getDocumentation().get(lang);
		}
		if (deco != null && deco.trim().length() > 0)
		{
			_deco.setDescriptionText(deco);
			_deco.setImage(FieldDecorationRegistry.getDefault()
					.getFieldDecoration(FieldDecorationRegistry.DEC_INFORMATION).getImage());
		}

		if (_configTemplate != null && _configTemplate.isAllowMultiple())
		{
			Button multiButton = new Button(_mainComposite, SWT.PUSH);
			if (index == 0)
			{
				multiButton.setText("+");
			}
			else
			{
				multiButton.setText("-");
			}
			multiButton.setLayoutData(new GridData());
			((GridData) multiButton.getLayoutData()).horizontalSpan = 1;
			((GridData) multiButton.getLayoutData()).horizontalAlignment = SWT.LEFT;
			_multiButtons.add(multiButton);
			multiButton.addSelectionListener(new SelectionListener()
			{

				@Override
				public void widgetSelected(SelectionEvent e)
				{
					if (_inputs != null && !_inputs.isEmpty())
					{
						if (index == 0)
						{
							Object o = _inputs.firstElement();
							if (o instanceof Element)
							{
								_aspectTemplate.createDefaultInput(AEAspectWidgetCustomizable.this,
										((Element) o).getOwnerDocument());
							}
							else
							{
								_aspectTemplate.createDefaultInput(AEAspectWidgetCustomizable.this, null);
							}
						}
						else
						{
							Object o = _inputs.get(index);

							if (o instanceof Element)
							{
								Element el = (Element) o;
								_aspectTemplate.removeMarkupNode(AEAspectWidgetCustomizable.this, el);
							}
							else if (o instanceof ValidationStm)
							{
								_aspectTemplate.removeValidationStm(AEAspectWidgetCustomizable.this, (ValidationStm) o);
							}
							else if (o instanceof Relation)
							{
								_aspectTemplate.removeRelation(AEAspectWidgetCustomizable.this, (Relation) o);
							}
							_inputs.remove(index);
							Vector<Object> help = _inputs;
							clearInput();
							for (Object oo : help)
							{
								setInput(oo);
							}
						}
						AEAspectWidgetCustomizable.this.getParent().layout();
						Point mp = AEAspectWidgetCustomizable.this.getParent().computeSize(SWT.DEFAULT, SWT.DEFAULT,
								true);
						AEAspectWidgetCustomizable.this.getParent().setSize(mp);
						_parentEditor.resize();

						
					}
					validateInternal();
					setSelected(true, _isValid);
					validate();

				}

				@Override
				public void widgetDefaultSelected(SelectionEvent e)
				{
					// TODO Auto-generated method stub

				}
			});
		}
		for (SelectionListener sl : _selectionListeners)
		{
			if (!_widgets.isEmpty() && _widgets.size() == index - 1)
			{
				_widgets.get(index).addSelectionListener(sl);
			}
			else if (_editors != null && _editors.size() < index)
			{
				
				_editors.get(index).addSelectionListener(sl);
			}
		}

		// Point mp = _mainComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT,
		// true);
		_mainComposite.redraw();
		_mainComposite.layout();
		_mainComposite.update();
		this.layout();
//		_mainComposite.setSize(_mainComposite.getParent().getSize());
		// _mainComposite.res
	}


	private void createMarkupDateComboWidget(int index)
	{
		// System.out.println("createMarkupDateComboWidget");

		AEMarkupWidget widget = new AEMarkupWidget(AEAspectWidgetCustomizable.this, _mainComposite, _configTemplate,
				_maywrite, SWT.NONE);
		widget.setLayoutData(new GridData());
		((GridData) widget.getLayoutData()).horizontalSpan = 1;
		// ((GridData) widget.getLayoutData()).horizontalAlignment = SWT.FILL;
		// ((GridData) widget.getLayoutData()).grabExcessHorizontalSpace = true;
		for (SelectionListener l : _selectionListeners)
		{
			widget.addSelectionListener(l);
		}
		_widgets.add(widget);

	}

	private void createDefinedRelationWidget()
	{
		// System.out.println("createDefinedRelationWidget");

		DefinedRelationEditorLine defRelEditor = new DefinedRelationEditorLine(AEAspectWidgetCustomizable.this, null,
				_mainComposite, false, SWT.NONE);
		defRelEditor.setLayoutData(new GridData());
		((GridData) defRelEditor.getLayoutData()).horizontalSpan = 1;
		((GridData) defRelEditor.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) defRelEditor.getLayoutData()).grabExcessHorizontalSpace = true;
		for (SelectionListener l : _selectionListeners)
		{
			defRelEditor.addSelectionListener(l);
		}
		_editors.add(defRelEditor);

	}

	private void createMarkupTextWidget(int index)
	{
		// System.out.println("createMarkupTextWidget");

		AEMarkupWidget widget = new AEMarkupWidget(AEAspectWidgetCustomizable.this, _mainComposite, _configTemplate,
				_maywrite, SWT.NONE);
		widget.setLayoutData(new GridData());
		((GridData) widget.getLayoutData()).horizontalSpan = 1;
		((GridData) widget.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) widget.getLayoutData()).grabExcessHorizontalSpace = true;

		for (SelectionListener l : _selectionListeners)
		{
			widget.addSelectionListener(l);
		}
		_widgets.add(widget);

	}

	private void createMarkupComboWidget(int index)
	{
		// System.out.println("createMarkupComboWidget");

		AEMarkupWidget widget = new AEMarkupWidget(AEAspectWidgetCustomizable.this, _mainComposite, _configTemplate,
				_maywrite, SWT.NONE);
		widget.setLayoutData(new GridData());
		((GridData) widget.getLayoutData()).horizontalSpan = 1;
		((GridData) widget.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) widget.getLayoutData()).grabExcessHorizontalSpace = true;
		for (SelectionListener l : _selectionListeners)
		{
			widget.addSelectionListener(l);
		}
		_widgets.add(widget);
	}

	private void createMarkupDateWidget(int index)
	{
		// System.out.println("createMarkupDateWidget");

		AEMarkupWidget widget = new AEMarkupWidget(AEAspectWidgetCustomizable.this, _mainComposite, _configTemplate,
				_maywrite, SWT.NONE);
		widget.setLayoutData(new GridData());
		((GridData) widget.getLayoutData()).horizontalSpan = 1;
//		((GridData) widget.getLayoutData()).horizontalAlignment = SWT.FILL;
//		((GridData) widget.getLayoutData()).grabExcessHorizontalSpace = true;
		for (SelectionListener l : _selectionListeners)
		{
			widget.addSelectionListener(l);
		}
		_widgets.add(widget);
	}

	private void createRelationWidget()
	{

		RelationEditorLine2 relEditor = new RelationEditorLine2(AEAspectWidgetCustomizable.this, null, _mainComposite,
				false, SWT.NONE);
		relEditor.setLayoutData(new GridData());
		((GridData) relEditor.getLayoutData()).horizontalSpan = 1;
		((GridData) relEditor.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) relEditor.getLayoutData()).grabExcessHorizontalSpace = true;
		for (SelectionListener l : _selectionListeners)
		{
			relEditor.addSelectionListener(l);
		}
		_editors.add(relEditor);
	}

	private void createTimeStmWidget()
	{
		// System.out.println("createTimeStmWidget");

		TimeStmEditorLine timeStmEditor = new TimeStmEditorLine(AEAspectWidgetCustomizable.this, null, _mainComposite,
				SWT.NONE);
		timeStmEditor.setLayoutData(new GridData());
		((GridData) timeStmEditor.getLayoutData()).horizontalAlignment = SWT.LEFT;
		((GridData) timeStmEditor.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridData) timeStmEditor.getLayoutData()).horizontalSpan = 1;
		for (SelectionListener l : _selectionListeners)
		{
			timeStmEditor.addSelectionListener(l);
		}
		_editors.add(timeStmEditor);
	}

	private void createReferenceWidget()
	{
		// System.out.println("createReferenceWidget");
		ValidationEditorLine validationEditor = new ValidationEditorLine(AEAspectWidgetCustomizable.this, null,
				_mainComposite, false, SWT.NONE);
		validationEditor.setLayoutData(new GridData());
		((GridData) validationEditor.getLayoutData()).horizontalSpan = 1;
		((GridData) validationEditor.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) validationEditor.getLayoutData()).grabExcessHorizontalSpace = true;
		for (SelectionListener l : _selectionListeners)
		{
			validationEditor.addSelectionListener(l);
		}
		_editors.add(validationEditor);

	}

	public int getWidgetType()
	{
		return _configTemplate.getWidgetType();
	}

	public void setInput(Object input)
	{
		if (matchesInput(input) >= 0)
		{
			_loading = true;
			this._inputs.add(input);
			if (_inputs.size() > 1)
			{
				createAspectWidget(_inputs.size() - 1);
			}


			Element element;
			switch (_configTemplate.getWidgetType())
			{
				case AEAspectWidgetCustomizable.TYPE_TEXT:
					element = (Element) input;
					_widgets.get(_inputs.size() - 1).setInput(element);
					break;
				case TYPE_COMBO:
					element = (Element) input;
//					System.out.println("setinput combo widget");

					_widgets.get(_inputs.size() - 1).setInput(element);
					break;
				case TYPE_DATE:
//					System.out.println("setinput date widget");
					element = (Element) input;
					_widgets.get(_inputs.size() - 1).setInput(element);
					break;
				case TYPE_DATE_COMBO:
//					System.out.println("setinput date combo widget");
					element = (Element) input;
					_widgets.get(_inputs.size() - 1).setInput(element);
					break;
				case RELATION:
					Relation rel = (Relation) input;
					_editors.get(_inputs.size() - 1).setInput(rel);
					break;
				case RELATION_DEFINED:
					rel = (Relation) input;
					_editors.get(_inputs.size() - 1).setInput(rel);
					break;
				case TIME_STM:
					TimeStm tStm = (TimeStm) input;
					_editors.get(_inputs.size() - 1).setInput(tStm);
					break;
				case REFERENCE:
				{
					ValidationStm vStm = (ValidationStm) input;
					_editors.get(_inputs.size() - 1).setInput(vStm);

				}
					break;

			}

		}
		_loading = false;
	}



	public Vector<Object> getInput()
	{
		return _inputs;
	}

	public int matchesInput(Object input)
	{
		switch (_configTemplate.getWidgetType())
		{
			case AEAspectWidgetCustomizable.TYPE_TEXT:
				if (input instanceof Element)
				{
					return compareNodeAndConfig((Element) input);
				}
				break;
			case TYPE_COMBO:
				if (input instanceof Element)
				{
					// if (compareNodeAndConfig((Element) input) < 0)
					// {
					// return -1;
					// }
					// if (compareComboConfigAndNode((Element) input) < 0)
					// {
					// System.out.println("matches input index -1");
					// return -1;
					// }
					int index = compareComboConfigAndNode((Element) input);
//					System.out.println("matches input index " + index);

					return index;
				}
				break;
			case TYPE_DATE:
				if (input instanceof Element)
				{
					return compareNodeAndConfig((Element) input);

				}
				break;
			case TYPE_DATE_COMBO:
				if (input instanceof Element)
				{
					return compareNodeAndConfig((Element) input);
				}
				break;
			case RELATION:
				if (input instanceof Relation)
				{
					return 1;
				}
				break;
			case RELATION_DEFINED:
				if (input instanceof Relation)
				{
					return compareRelationDefinition((Relation) input);
				}
				break;
			case TIME_STM:
				if (input instanceof TimeStm)
				{
					return 1;
				}
				break;
			case REFERENCE:
			{
				if (input instanceof ValidationStm)
				{
					return 1;
				}
			}
				break;
			default:
				return -1;
		}
		return -1;
	}

	private int compareRelationDefinition(Relation relation)
	{
		int matchIndex = -1;
		if (_configTemplate.getElement() != null && relation.getContext() != null && relation.getContext().equals(_configTemplate.getElement()))
		{
			if (((_configTemplate.getType() == null || _configTemplate.getType().trim().length() == 0) && (relation
					.getRClass() == null || relation
							.getRClass().trim().length() == 0))
					|| _configTemplate.getType().equals(relation
							.getRClass()))
			{
				if (((_configTemplate.getSubtype() == null || _configTemplate.getSubtype().trim().length() == 0) 
						&& (relation.getRelation() == null || relation.getRelation().trim().length() == 0 ))
						|| (relation.getRelation() != null && _configTemplate.getSubtype() != null && _configTemplate
								.getSubtype().equals(relation.getRelation())))
				{
					
						matchIndex = 4;
					
				}
				else
				{
					matchIndex = 3;
				}
			}
			else
			{
				matchIndex = 2;
			}
		}
		else if ((_configTemplate.getElement() == null && relation.getContext() == null) 
				|| (_configTemplate.getElement().trim().length() == 0 &&relation.getContext().trim().length() == 0))
		{
			matchIndex = 1;
		}
		else
		{
			return -1;
		}
		if (_inputs != null)
		{
			matchIndex = matchIndex + Math.max(0, 30 - _inputs.size());
		}
		else
		{
			matchIndex = matchIndex + 30;
		}
		return matchIndex;
		
	}

	private int compareComboConfigAndNode(Element element)
	{
		// System.out.println("compareComboConfigAndNode _configTemplate.getElement(): "
		// + _configTemplate.getElement());
		// System.out.println("_configTemplate.getType(): " +
		// _configTemplate.getType());
		// System.out.println("_configTemplate.getSubtype(): " +
		// _configTemplate.getSubtype());
		// System.out.println("_configTemplate.getRole(): " +
		// _configTemplate.getRole());
		// System.out.println("Element: " + element.getNodeName());
		// System.out.println("type: " + element.getAttribute("type"));
		// System.out.println("subtype: " + element.getAttribute("subtype"));
		// System.out.println("role: " + element.getAttribute("role"));
		// System.out.println("content: " + element.getTextContent());

		int matchIndex = -1;
		String labelOfMarkup = PDRConfigProvider.getLabelOfMarkup(_configTemplate.getElement(),
				element.getAttribute("type"), element.getAttribute("subtype"), element.getAttribute("role"));
		if (element.getTextContent() != null
				&& _configTemplate.getElement() != null && element.getNodeName().contains(_configTemplate.getElement()))
		{
			if (element.getAttribute("type") != null && element.getAttribute("type").trim().length() > 0)
			{
				if (element.getAttribute("subtype") != null && element.getAttribute("subtype").trim().length() > 0)
				{
					if (element.getAttribute("role") != null && element.getAttribute("role").trim().length() > 0)
					{
						if (element.getTextContent().equals(element.getAttribute("role"))
								|| element.getTextContent().equals(labelOfMarkup))
						{
							matchIndex = 5;
						}
					}
					if (element.getTextContent().equals(element.getAttribute("subtype"))
							|| element.getTextContent().equals(labelOfMarkup))
					{
						matchIndex = 4;
					}
				}
				if (element.getTextContent().equals(element.getAttribute("type"))
						|| element.getTextContent().equals(labelOfMarkup))
				{
					matchIndex = 3;
				}
			}
			matchIndex = 0;
		}
		else
		{
			return -1;
		}
		if (_inputs != null)
		{
			matchIndex = matchIndex + Math.max(0, 30 - _inputs.size());
		}
		else
		{
			matchIndex = matchIndex + 30;
		}
		// compare prefix
		if (_configTemplate.getPrefix() != null && _configTemplate.getPrefix().trim().length() > 0)
		{
			if (element.getPreviousSibling() != null
					&& element.getPreviousSibling().getNodeType() == Document.TEXT_NODE)
			{
				if (element.getPreviousSibling().getTextContent().contains(_configTemplate.getPrefix()))
				{
					matchIndex = matchIndex + 5;
				}
			}
		}
		// compare suffix
		if (_configTemplate.getSuffix() != null && _configTemplate.getSuffix().trim().length() > 0)
		{
			if (element.getNextSibling() != null && element.getNextSibling().getNodeType() == Document.TEXT_NODE)
			{
				if (element.getNextSibling().getTextContent().contains(_configTemplate.getSuffix()))
				{
					matchIndex = matchIndex + 5;
				}
			}
		}

		// System.out.println("mathIndex date " + matchIndex);
		return matchIndex;
	}

	private int compareNodeAndConfig(Element element)
	{
//		System.out.println("_configTemplate.getElement(): " + _configTemplate.getElement());
//		System.out.println("_configTemplate.getType(): " + _configTemplate.getType());
//		System.out.println("_configTemplate.getSubtype(): " + _configTemplate.getSubtype());
//		System.out.println("_configTemplate.getRole(): " + _configTemplate.getRole());
//		System.out.println("Element: " + element.getNodeName());
//		System.out.println("type: " + element.getAttribute("type"));
//		System.out.println("subtype: " + element.getAttribute("subtype"));
//		System.out.println("role: " + element.getAttribute("role"));
//		System.out.println("content: " + element.getTextContent());

		int matchIndex = -1;
		boolean empty = element.getTextContent() == null;
		if (_configTemplate.getElement() != null && element.getNodeName().contains(_configTemplate.getElement()))
		{
			if (((_configTemplate.getType() != null && _configTemplate.getType().trim().length() > 0) && (element
					.getAttribute("type") != null && element.getAttribute("type").trim().length() > 0)))
			{
				if (_configTemplate.getType().equals(element.getAttribute("type")))
				{
					if (((_configTemplate.getSubtype() != null && _configTemplate.getSubtype().trim().length() > 0) && (element
							.getAttribute("subtype") != null && element.getAttribute("subtype").trim().length() > 0)))
					{
						if (_configTemplate.getSubtype().equals(element.getAttribute("subtype")))
						{
							if ((_configTemplate.getRole() != null && _configTemplate.getRole().trim().length() > 0)
									&& (element.getAttribute("role") != null && element.getAttribute("role").trim()
											.length() > 0))

							{
								if (_configTemplate.getRole().equals(element.getAttribute("role")))
								{
									matchIndex = 5;
								}
								else
								// role definition divergent
								{
									return -1;
								}

							}
							else if (_configTemplate.getRole() != null && _configTemplate.getRole().trim().length() > 0)
							// config has role but element not
							{
								return -1;
							}
							else
							// none has role definition
							{
								matchIndex = 4;
							}
						}
						else
						// subtype definitions divergent
						{
							return -1;
						}

					}
					else if (_configTemplate.getSubtype() != null && _configTemplate.getSubtype().trim().length() > 0)
					{// config has subtype but element not
						return -1;
					}
					else
					// none has definition of subtype
					{
						matchIndex = 3;
					}
				}
				else
				// type definitions divergent
				{
					return -1;
				}

			}
			else if (empty && element.hasAttribute("type"))
			// none has definition of type
			{
				matchIndex = 2;
			}
			else
			{
				matchIndex = 0;
			}
		}
		else
		{
			return -1;
		}
		if (_inputs != null)
		{
			matchIndex = matchIndex + Math.max(0, 30 - _inputs.size());
		}
		else
		{
			matchIndex = matchIndex + 30;
		}
		// compare prefix
		if (_configTemplate.getPrefix() != null && _configTemplate.getPrefix().trim().length() >0)
		{
			if (element.getPreviousSibling() != null && element.getPreviousSibling().getNodeType() == Document.TEXT_NODE)
			{
				if (element.getPreviousSibling().getTextContent().contains(_configTemplate.getPrefix()))
				{
					matchIndex = matchIndex + 5;
				}
			}
		}
		// compare suffix
				if (_configTemplate.getSuffix() != null && _configTemplate.getSuffix().trim().length() >0)
				{
					if (element.getNextSibling() != null && element.getNextSibling().getNodeType() == Document.TEXT_NODE)
					{
						if (element.getNextSibling().getTextContent().contains(_configTemplate.getSuffix()))
						{
							matchIndex = matchIndex + 5;
						}
					}
				}

		// System.out.println("mathIndex date " + matchIndex);
		return matchIndex;
	}

	@Override
	public boolean isDirty()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isValid()
	{
		if (_configTemplate.isRequired())
		{
		switch (_configTemplate.getWidgetType())
		{
			case AEAspectWidgetCustomizable.TYPE_TEXT:
				return _widgets.get(0).isValid();
		case TYPE_COMBO:
				return _widgets.get(0).isValid();
		case TYPE_DATE:
				return _widgets.get(0).isValid();
				case TYPE_DATE_COMBO:
					return _widgets.get(0).isValid();
		case RELATION:
			return _editors.get(0).isValid();
			case RELATION_DEFINED:
				return _editors.get(0).isValid();
			case TIME_STM:
				return _editors.get(0).isValid();
			case REFERENCE:
				return _editors.get(0).isValid();
			default:
				break;
		}
		}
		return true;
	}

	@Override
	public void setDirty(boolean isDirty)
	{
		if (isDirty && _parentEditor != null && !_loading)
		{
			_parentEditor.setDirty(true);
		}
		
	}

	@Override
	public void setSelected(boolean isSelected, boolean contextIsValid)
	{
		this._selected = isSelected;
		if (_widgets != null)
		{
			for (AEMarkupWidget w : _widgets)
			{
				if (!w.isDisposed())
				{
					w.setSelected(_selected && _editable, contextIsValid);
				}
			}
		}
		if (_editors != null)
		{
			for (IAEBasicEditor ed : _editors)
			{
				ed.setSelected(_selected && _editable, contextIsValid);
			}
		}
		if (_multiButtons != null)
		{
			for (Button b : _multiButtons)
			{
				if (!b.isDisposed())
				{
					b.setEnabled(_selected && _editable);
				}
			}
		}
		if (isSelected && contextIsValid)
		{
			setBackground(AEVIEWConstants.VIEW_BACKGROUND_SELECTED_COLOR);
			setForeground(AEVIEWConstants.VIEW_FOREGROUND_SELECTED_COLOR);
		}
		else if (contextIsValid)
		{
			setBackground(AEVIEWConstants.VIEW_BACKGROUND_DESELECTED_COLOR);
			setForeground(AEVIEWConstants.VIEW_FOREGROUND_DESELECTED_COLOR);
		}
		else if (isSelected)
		{
			setForeground(AEVIEWConstants.VIEW_FOREGROUND_SELECTED_COLOR);
			setBackground(AEVIEWConstants.VIEW_BACKGROUND_INVALID_COLOR);
		}
		else
		{
			setBackground(AEVIEWConstants.VIEW_BACKGROUND_INVALID_COLOR);			
		}
	}

	@Override
	public void setBackground(Color color)
	{
		super.setBackground(color);
		if (_labels != null)
		{
			for (Label l : _labels)
			{
				if (!l.isDisposed())
				{
					l.setBackground(color);
				}
			}
		}
		_mainComposite.setBackground(color);

	}

	@Override
	public void setForeground(Color color)
	{
		super.setForeground(color);
		if (_labels != null)
		{
			for (Label l : _labels)
			{
				if (!l.isDisposed())
				{
					l.setForeground(color);
				}
			}
		}

		_mainComposite.setForeground(color);
	}

	private void setValid(boolean isValid)
	{
		this._isValid = isValid;
	}

	private void validateInternal() {
		boolean valid = true;
		if (_widgets != null)
		{
			for (AEMarkupWidget w : _widgets)
			{
				if (!w.isDisposed() && !w.isValid())
				{
					valid = false;
					break;
				}
			}
		}
		if (_editors != null)
		{
			for (IAEBasicEditor ed : _editors)
			{
				if (!ed.isValid())
				{
					valid = false;
					break;
				}
			}
		}
		setValid(valid);
	}

	@Override
	public void validate()
	{
		if (_parentEditor != null && !_loading)
		{
			_parentEditor.validate();
		}
		
	}

	@Override
	public void saveInput()
	{
		if (_parentEditor != null && !_loading)
		{
			_parentEditor.saveInput();
		}

	}

	public AspectConfigTemplate getConfigTemplate()
	{
		return _configTemplate;
	}

	@Override
	public int compareTo(AEAspectWidgetCustomizable o)
	{
		if (this._configTemplate != null && o.getConfigTemplate() != null)
		{
			return this._configTemplate.getPriority() - o.getConfigTemplate().getPriority();
		}
		return 0;
	}

	public void clearInput()
	{
		// System.out.println("clearInput");
		_inputs = new Vector<Object>(1);
		_widgets = new Vector<AEMarkupWidget>(1);
		_editors = null;
		_mainComposite.dispose();
		_mainComposite = null;
		createMainComposite(getStyle());
		createAspectWidget(0);

	}

	@Override
	public void setEditable(boolean editable) {
		this._editable  = editable;
		if (_widgets != null)
		{
			for (AEMarkupWidget w : _widgets)
			{
				if (!w.isDisposed())
				{
					w.setEditable(_editable);
				}
			}
		}
		if (_editors != null)
		{
			for (IAEBasicEditor ed : _editors)
			{
				ed.setEditable(_editable);
			}
		}
		if (_multiButtons != null)
		{
			for (Button b : _multiButtons)
			{
				if (!b.isDisposed())
				{
					b.setEnabled(_selected && _editable);
				}
			}
		}
		
	}

	public void setConfigTemplate(AspectConfigTemplate aspectConfigTemplate)
	{
		_configTemplate = aspectConfigTemplate;
		for (Control c : this.getChildren())
		{
			c.dispose();
		}
		createMainComposite(SWT.NONE);

		createAspectWidget(0);
		setSelected(false, true);

	}

}
