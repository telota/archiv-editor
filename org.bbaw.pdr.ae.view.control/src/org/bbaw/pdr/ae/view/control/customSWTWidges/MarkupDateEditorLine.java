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

import org.bbaw.pdr.ae.common.AEConstants;
import org.bbaw.pdr.ae.common.AEVIEWConstants;
import org.bbaw.pdr.ae.common.CommonActivator;
import org.bbaw.pdr.ae.common.NLMessages;
import org.bbaw.pdr.ae.config.model.AspectConfigTemplate;
import org.bbaw.pdr.ae.metamodel.PdrDate;
import org.bbaw.pdr.ae.view.control.interfaces.IAEBasicEditor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.w3c.dom.Element;

public class MarkupDateEditorLine extends Composite implements IAEBasicEditor
{

	private PdrDate _date1;

	private PdrDate _date2;

	private String _date1Type;

	private String _date2Type;

	private Composite _composite;

	/** The afters. */
	private final String[] _afters = new String[]
	{"", "to", "notAfter"}; //$NON-NLS-1$



	private boolean _pointOfTime;

	private boolean _isDirty = false;

	private boolean _isValid = true;

	private IAEBasicEditor _parentEditor;
	/** The _selection listener. */
	private ArrayList<SelectionListener> _selectionListeners = new ArrayList<SelectionListener>();

	private AspectConfigTemplate _markupTemplate;

	private Element _inputElement;

	private PDRDateEditor _date1Editor;

	private PDRDateEditor _date2Editor;

	private Combo _date1TypeCombo;

	private Combo _date2TypeCombo;

	private ComboViewer _date1TypeComboViewer;

	private ComboViewer _date2TypeComboViewer;

	private boolean _loading;

	protected boolean _isSelected;

	private boolean _editable = true;

	/** The PRESELECTE d_ year. */
	private static final int PRESELECTED_YEAR = Platform.getPreferencesService().getInt(CommonActivator.PLUGIN_ID,
			"ASPECT_PRESELECTED_DATE_YEAR", AEConstants.ASPECT_PRESELECTED_DATE_YEAR, null);

	public MarkupDateEditorLine(IAEBasicEditor parentEditor, AspectConfigTemplate markupTemplate, Composite parent,
			int style)
	{
		super(parent, style);
		super.setLayout(new GridLayout(1, false));
		((GridLayout) super.getLayout()).marginHeight = 0;
		((GridLayout) super.getLayout()).verticalSpacing = 0;
		((GridLayout) super.getLayout()).marginWidth = 0;

		_parentEditor = parentEditor;

		_markupTemplate = markupTemplate;
		createEditor();


		_composite.pack();
		_composite.layout();
		// this.setSize(SWT.DEFAULT, 18);
		// this.pack();
		this.layout();
	}

	public final void addSelectionListener(final SelectionListener listener)
	{
		if (listener != null)
		{
			_selectionListeners.add(listener);
		}

	}

	private void createEditor()
	{
		this.setLayout(new GridLayout(1, false));
		((GridLayout) this.getLayout()).marginHeight = 0;
		((GridLayout) this.getLayout()).verticalSpacing = 0;

		FocusListener focusListener = new FocusListener()
		{

			@Override
			public void focusGained(final FocusEvent e)
			{
				if (!MarkupDateEditorLine.this._isSelected)
				{
					Event ee = new Event();
					ee.widget = MarkupDateEditorLine.this;
					SelectionEvent se = new SelectionEvent(ee);
					for (SelectionListener s : _selectionListeners)
					{
						s.widgetSelected(se);
					}
				}

			}

			@Override
			public void focusLost(final FocusEvent e)
			{
				contentChanged();
			}
		};

		_composite = new Composite(this, SWT.NONE);
		_composite.setLayoutData(new GridData());
		((GridData) _composite.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _composite.getLayoutData()).grabExcessHorizontalSpace = true;
		_composite.setLayout(new GridLayout(5, false));
		((GridLayout) _composite.getLayout()).marginHeight = 0;
		((GridLayout) _composite.getLayout()).verticalSpacing = 0;

		
		_date1TypeCombo = new Combo(_composite, SWT.DROP_DOWN | SWT.READ_ONLY);
		_date1TypeCombo.setLayoutData(new GridData());
		((GridData) _date1TypeCombo.getLayoutData()).horizontalSpan = 1;
		((GridData) _date1TypeCombo.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _date1TypeCombo.getLayoutData()).grabExcessHorizontalSpace = true;
		_date1TypeComboViewer = new ComboViewer(_date1TypeCombo);
		_date1TypeComboViewer.setContentProvider(ArrayContentProvider.getInstance());
		_date1TypeComboViewer.setLabelProvider(new LabelProvider()
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

		_date1TypeComboViewer.setInput(AEConstants.TIME_TYPES);
		if (_markupTemplate != null && _markupTemplate.getDate1() != null
				&& _markupTemplate.getDate1().trim().length() > 0)
		{
			StructuredSelection selection = new StructuredSelection(_markupTemplate.getDate1());
			_date1TypeComboViewer.setSelection(selection);
		}
		else
		{
			_date1TypeCombo.select(0);
			ISelection selection = _date1TypeComboViewer.getSelection();
			Object obj = ((IStructuredSelection) selection).getFirstElement();
			String s = (String) obj;
			_date1Type = s;
		}
		_date1TypeComboViewer.addSelectionChangedListener(new ISelectionChangedListener()
		{

			@Override
			public void selectionChanged(final SelectionChangedEvent event)
			{
				ISelection selection = event.getSelection();
				Object obj = ((IStructuredSelection) selection).getFirstElement();
				String s = (String) obj;
				if (_date1Type != null && !_date1Type.equals(s))
				{
					_inputElement.removeAttribute(_date1Type);
					_inputElement.setAttribute(s, _date1.toString());
				}
				_date1Type = s;
				
				if (_date1Type.equals("from") || _date1Type.equals("notBefore"))
				{
					_pointOfTime = false;
				}
				else
				{
					_pointOfTime = true;
				}
				if (_date2TypeCombo != null)
				{
					_date2TypeCombo.setEnabled(!_pointOfTime);
				}
				contentChanged();
			}

		});
		_date1TypeCombo.addFocusListener(focusListener);
		
		
		

		_date1Editor = new PDRDateEditor(MarkupDateEditorLine.this, null, _composite, SWT.NONE);
		_date1Editor.setLayoutData(new GridData());
		_date1Editor.addCustomFocusListener(focusListener);
		_date1Editor.setSelected(false, isValid());

		_date2TypeCombo = new Combo(_composite, SWT.DROP_DOWN | SWT.READ_ONLY);
		_date2TypeCombo.setLayoutData(new GridData());
		((GridData) _date2TypeCombo.getLayoutData()).horizontalSpan = 1;
		((GridData) _date2TypeCombo.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _date2TypeCombo.getLayoutData()).grabExcessHorizontalSpace = true;
		_date2TypeComboViewer = new ComboViewer(_date2TypeCombo);
		_date2TypeComboViewer.setContentProvider(ArrayContentProvider.getInstance());
		_date2TypeComboViewer.setLabelProvider(new LabelProvider()
		{

			@Override
			public String getText(final Object element)
			{
				String str = (String) element;
				if (str != null && str.length() > 0 && NLMessages.getString("Editor_time_" + str) != null)
				{
					return NLMessages.getString("Editor_time_" + str);
				}
				return str;
			}

		});

		_date2TypeComboViewer.setInput(_afters);
		if (_markupTemplate != null && _markupTemplate.getDate2() != null
				&& _markupTemplate.getDate2().trim().length() > 0)
		{
			StructuredSelection selection = new StructuredSelection(_markupTemplate.getDate2());
			_date2TypeComboViewer.setSelection(selection);
		}
		else
		{
			_date2TypeCombo.select(0);
			ISelection selection = _date2TypeComboViewer.getSelection();
			Object obj = ((IStructuredSelection) selection).getFirstElement();
			String s = (String) obj;
			_date2Type = s;
		}
		_date2TypeComboViewer.addSelectionChangedListener(new ISelectionChangedListener()
		{

			@Override
			public void selectionChanged(final SelectionChangedEvent event)
			{
				ISelection selection = event.getSelection();
				Object obj = ((IStructuredSelection) selection).getFirstElement();
				String s = (String) obj;
				if (_inputElement != null && _date2Type != null && !_date2Type.equals(s))
				{
					_inputElement.removeAttribute(_date2Type);
					if (_date2 != null && s.trim().length() > 0)
					{
						_inputElement.setAttribute(s, _date2.toString());
					}
				}
				setPointOfTime(s.trim().length() == 0);
				_date2Type = s;
				contentChanged();
			}

		});
		
		
		
		_date2TypeCombo.addFocusListener(focusListener);

		
		_date2Editor = new PDRDateEditor(MarkupDateEditorLine.this, null, _composite, SWT.NONE);
		_date2Editor.setLayoutData(new GridData());

		_date2Editor.addCustomFocusListener(focusListener);
		_date2Editor.setSelected(false, isValid());
	}


	@Override
	public boolean isDirty()
	{
		return _isDirty;
	}

	@Override
	public boolean isValid()
	{
		return _isValid;
	}



	

	@Override
	public void setBackground(Color color)
	{
		super.setBackground(color);
		_composite.setBackground(color);
		// _date1TypeCombo.setBackground(color);
		// if (_date2TypeCombo != null)
		// {
		// _date2TypeCombo.setBackground(color);
		// }

	}

	@Override
	public void setDirty(boolean isDirty)
	{
		this._isDirty = isDirty;
		if (!_loading && _parentEditor != null && _isDirty)
		{
			_parentEditor.setDirty(true);
		}

	}

	@Override
	public void setForeground(Color color)
	{
		super.setForeground(color);
		_date1TypeCombo.setForeground(color);
		_date2TypeCombo.setForeground(color);

	}

	@Override
	public void setLayoutData(Object layoutData)
	{
		super.setLayoutData(layoutData);
		// if (_composite != null) {
		// _composite.setLayoutData(layoutData);
		// }

	}

	private void setPointOfTime(boolean pointOfTime)
	{
		if (pointOfTime)
		{
			if (_inputElement != null)
			{
				_inputElement.removeAttribute(_date2Type);
			}
			_date2Type = "";
			

		}
		else if (_date2Editor != null && (_date2Editor.getDate() == null || _date2Editor.getDate().getYear() == 0))
		{
			_date2Editor.setInput(new PdrDate(PRESELECTED_YEAR, 0, 0));
		}

		if (_date2Editor != null)
		{
			_date2Editor.setSelected(!pointOfTime, isValid());
		}
		
	}

	@Override
	public void setSelected(boolean selected, boolean contextIsValid)
	{
		this._isSelected = selected;
		if (_date1Editor != null)
		{
			_date1Editor.setSelected(selected, contextIsValid);
		}
		if (_date2Editor != null)
		{
			_date2Editor.setSelected(selected && !_pointOfTime && _date2Type.trim().length() > 0, contextIsValid);
		}

		if (selected && contextIsValid)
		{
			setBackground(AEVIEWConstants.VIEW_BACKGROUND_SELECTED_COLOR);
			setForeground(AEVIEWConstants.VIEW_FOREGROUND_SELECTED_COLOR);
		}
		else if (contextIsValid)
		{
			setBackground(AEVIEWConstants.VIEW_BACKGROUND_DESELECTED_COLOR);
			setForeground(AEVIEWConstants.VIEW_FOREGROUND_DESELECTED_COLOR);
		}
		else if (selected)
		{
			setBackground(AEVIEWConstants.VIEW_BACKGROUND_INVALID_COLOR);
			setForeground(AEVIEWConstants.VIEW_FOREGROUND_SELECTED_COLOR);
		}
		else
		{
			setForeground(AEVIEWConstants.VIEW_FOREGROUND_DESELECTED_COLOR);
			setBackground(AEVIEWConstants.VIEW_BACKGROUND_INVALID_COLOR);
		}
		
	}
	private void contentChanged()
	{
		setDirty(true);
		saveInput();
		validateInternal();
		validate();
	}

	private void validateInternal() {
		setValid((_date1Editor == null || _date1Editor.isValid()) && (_date2Editor == null || _date2Editor.isValid()));
		
	}

	private void setValid(boolean isValid) {
		this._isValid = isValid;
		
	}

	public void setInput(Object input)
	{
		if (input instanceof Element)
		{
			this._inputElement = (Element) input;
			_loading = true;
			loadInput();
			_loading = false;
		}
	}

	private void loadInput()
	{
		// System.out.println("MarkupDateEditor loadInput");
		String d1 = null;
		if (_markupTemplate != null && _markupTemplate.getDate1() != null
				&& _markupTemplate.getDate1().trim().length() > 0)
		{
			d1 = _inputElement.getAttribute(_markupTemplate.getDate1());
			_date1Type = _markupTemplate.getDate1();
		}
		if (d1 != null)
		{
			_date1 = new PdrDate(d1);

		}
		if (_date1.isValid())
		{
			_date1Editor.setInput(_date1);
		}
		else 
		{
			if (_inputElement.hasAttribute("when"))
			{
				_date1Type = "when";
				d1 = _inputElement.getAttribute("when");
			}
			else if (_inputElement.hasAttribute("from"))
				
			{
				_date1Type = "from";
				d1 = _inputElement.getAttribute("from");
			}
			else if (_inputElement.hasAttribute("notBefore"))
			{
				_date1Type = "notBefore";
				d1 = _inputElement.getAttribute("notBefore");
			}
			else if (_inputElement.hasAttribute("to"))
			{
				_date1Type = "to";
				d1 = _inputElement.getAttribute("to");
			}
			else if (_inputElement.hasAttribute("notAfter"))
			{
				_date1Type = "notAfter";
				d1 = _inputElement.getAttribute("notAfter");
			}
			if (d1 != null)
			{
				_date1 = new PdrDate(d1);
				if (_date1.isValid())
				{
					_date1Editor.setInput(_date1);
				}

			}
		}

		
		String d2 = null;
		if (_markupTemplate != null && _markupTemplate.getDate2() != null
				&& _markupTemplate.getDate2().trim().length() > 0)
		{
			d2 = _inputElement.getAttribute(_markupTemplate.getDate2());
			_date2Type = _markupTemplate.getDate2();
		}
		if (d2 != null && d2.trim().length() > 0 && !_date2Type.equals(_date1Type))
		{
			_date2 = new PdrDate(d2);
			_date2Editor.setInput(_date2);
			_pointOfTime = false;
		}
		else
		{
			if (_inputElement.hasAttribute("to"))
			{
				_date2Type = "to";
				d2 = _inputElement.getAttribute("to");
			}
			else if (_inputElement.hasAttribute("notAfter"))
			{
				_date2Type = "notAfter";
				d2 = _inputElement.getAttribute("notAfter");
			}
			if (d2 != null && !_date2Type.equals(_date1Type))
			{
				_date2 = new PdrDate(d2);
				_date2Editor.setInput(_date2);
				_pointOfTime = false;

			}
			else
			{
				_pointOfTime = true;
			}
		}
		_date1TypeComboViewer.setSelection(new StructuredSelection(_date1Type));
		_date2TypeComboViewer.setSelection(new StructuredSelection(_date2Type));
		setPointOfTime(_date2Type.trim().length() == 0 && _pointOfTime);

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
		// System.out.println("MarkupDateEditorLine saveInput");
		if (_inputElement != null)
		{
			_date1 = _date1Editor.getDate();
			_date2 = _date2Editor.getDate();
			if (_markupTemplate != null && _markupTemplate.getDate1() != null)
			{
				if (_date1 != null && _date1.isValid())
				{
					_inputElement.setAttribute(_date1Type, _date1.toString());
				}
			}
	
			if (_markupTemplate != null && _date2Type.trim().length() > 0)
			{
				if (_date2 != null && _date2.isValid())
				{
					_inputElement.setAttribute(_date2Type, _date2.toString());
				}
			}
			if (_date1 != null && _date1.isValid() && _date2 != null && _date2.isValid()
					&& _date2Type.trim().length() > 0)
			{
				if ((_inputElement.getTextContent() == null || _inputElement.getTextContent().trim().length() == 0)
						|| _date1Editor.isDirty() || _date2Editor.isDirty())
				{
					_inputElement.setTextContent(_date1.toString() + " - " + _date2.toString());
				}
			}
			else if (_date1 != null && _date1.isValid())
			{
				if ((_inputElement.getTextContent() == null || _inputElement.getTextContent().trim().length() == 0)
						|| _date1Editor.isDirty())
				{
					_inputElement.setTextContent(_date1.toString());
				}
			}
			else if (_date2 != null && _date2.isValid() && _date2Type.trim().length() > 0)
			{
				if ((_inputElement.getTextContent() == null || _inputElement.getTextContent().trim().length() == 0)
						|| _date2Editor.isDirty())
				{
					_inputElement.setTextContent(_date2.toString());
				}
			}
			setDirty(true);
			if (isValid() && !_loading)
			{
				_parentEditor.saveInput();
			}
		}

	}

	@Override
	public void setEditable(boolean editable) {
		this._editable  = editable;
		_date1TypeCombo.setEnabled(_isSelected && _editable);
		_date2TypeCombo.setEnabled(_isSelected && _editable); 
		if (_date1Editor != null)
		{
			_date1Editor.setEditable(_editable);
		}
		

		if (_date2Editor != null)
		{
			_date2Editor.setEditable(_editable && !_pointOfTime && _date2Type.trim().length() > 0);
		}
	}
}
