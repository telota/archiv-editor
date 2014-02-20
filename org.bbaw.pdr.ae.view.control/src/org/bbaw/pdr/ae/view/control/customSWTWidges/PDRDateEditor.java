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
import org.bbaw.pdr.ae.metamodel.PdrDate;
import org.bbaw.pdr.ae.view.control.interfaces.IAEBasicEditor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;

public class PDRDateEditor extends Composite implements IAEBasicEditor
{

	private PdrDate _date;

	private Composite _composite;

	private Combo _comboDayFrom;
	private Combo _comboMonthFrom;

	private YearSpinner _spinnerYearFrom;

	private ControlDecoration _decoTimeFrom;

	private boolean _isDirty = false;

	private boolean _isValid = true;

	private IAEBasicEditor _parentEditor;
	/** The _selection listener. */
	private ArrayList<SelectionListener> _selectionListeners = new ArrayList<SelectionListener>();

	private ArrayList<FocusListener> _focusListeners = new ArrayList<FocusListener>();

	private boolean _editable = true;

	private boolean _selected;

	private boolean _loading;

	/** The PRESELECTE d_ year. */
	private static final int PRESELECTED_YEAR = Platform.getPreferencesService().getInt(CommonActivator.PLUGIN_ID,
			"ASPECT_PRESELECTED_DATE_YEAR", AEConstants.ASPECT_PRESELECTED_DATE_YEAR, null);

	public PDRDateEditor(IAEBasicEditor parentEditor, PdrDate date, Composite parent, int style)
	{
		super(parent, style);
		_parentEditor = parentEditor;
		this._date = date;

		createEditor();

		_loading = true;
		loadDate();
		validate();
		_loading = false;
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
	public final void addCustomFocusListener(final FocusListener listener)
	{
		if (listener != null)
		{
			_focusListeners .add(listener);
		}

	}

	private void createEditor()
	{
		this.setLayout(new GridLayout(1, false));
		((GridLayout) this.getLayout()).marginHeight = 0;
		((GridLayout) this.getLayout()).marginWidth = 0;
		((GridLayout) this.getLayout()).verticalSpacing = 0;
		FocusListener focusListener = new FocusListener()
		{

			@Override
			public void focusGained(final FocusEvent e)
			{
				Event ee = new Event();
				ee.widget = PDRDateEditor.this;
				FocusEvent fe = new FocusEvent(ee);
				for (FocusListener l : _focusListeners)
				{
					l.focusGained(fe);
				}

			}

			@Override
			public void focusLost(final FocusEvent e)
			{
				Event ee = new Event();
				ee.widget = PDRDateEditor.this;
				FocusEvent fe = new FocusEvent(ee);
				for (FocusListener l : _focusListeners)
				{
					l.focusLost(fe);
				}

			}
		};

		_composite = new Composite(this, SWT.NONE);
		_composite.setLayoutData(new GridData());
		((GridData) _composite.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _composite.getLayoutData()).grabExcessHorizontalSpace = true;
		_composite.setLayout(new GridLayout(4, false));
		((GridLayout) _composite.getLayout()).marginHeight = 0;
		((GridLayout) _composite.getLayout()).marginWidth = 0;
		((GridLayout) _composite.getLayout()).verticalSpacing = 0;

		_comboDayFrom = new Combo(_composite, SWT.READ_ONLY);
		_comboDayFrom.setLayoutData(new GridData());
		_comboDayFrom.setItems(AEConstants.DAYS);
		_comboDayFrom.addFocusListener(focusListener);

		_comboDayFrom.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent se)
			{
				_date.setDay(_comboDayFrom.getSelectionIndex());
				contentChanged();
			}
		});

		_comboMonthFrom = new Combo(_composite, SWT.READ_ONLY);
		_comboMonthFrom.setLayoutData(new GridData());
		((GridData) _comboMonthFrom.getLayoutData()).horizontalAlignment = GridData.FILL;
		((GridData) _comboMonthFrom.getLayoutData()).grabExcessHorizontalSpace = true;
		_comboMonthFrom.setItems(AEConstants.MONTHS);
		_comboMonthFrom.addFocusListener(focusListener);

		_comboMonthFrom.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent se)
			{
				_date.setMonth(_comboMonthFrom.getSelectionIndex());
				contentChanged();
			}
		});

		_spinnerYearFrom = new YearSpinner(_composite, SWT.BORDER);
		_spinnerYearFrom.addFocusListener(focusListener);

		_decoTimeFrom = new ControlDecoration(_spinnerYearFrom, SWT.LEFT | SWT.TOP);

		_spinnerYearFrom.pack();
		_spinnerYearFrom.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent se)
			{
				_date.setYear(_spinnerYearFrom.getSelection());
				
				contentChanged();
			}
		});

	}

	public PdrDate getDate()
	{
		return _date;
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

	private void loadDate()
	{
		if (_date != null)
		{
			_comboDayFrom.select(_date.getDay());
			_comboMonthFrom.select(_date.getMonth());
			_spinnerYearFrom.setSelection(_date.getYear());
		}
		else
		{
			_date = new PdrDate(PRESELECTED_YEAR, 0, 0);
		}
	}

	@Override
	public void setBackground(Color color)
	{
		super.setBackground(color);
		_composite.setBackground(color);

	}

	public void setInput(Object input)
	{

		if (input instanceof PdrDate)
		{
		this._date = (PdrDate) input;
			_loading = true;
		loadDate();
			_loading = false;
		}
	}

	@Override
	public void setDirty(boolean isDirty)
	{
		this._isDirty = isDirty;
		if (_parentEditor != null && _isDirty && !_loading)
		{
			_parentEditor.setDirty(true);
		}

	}

	@Override
	public void setForeground(Color color)
	{
		super.setForeground(color);
	}

	@Override
	public void setLayoutData(Object layoutData)
	{
		super.setLayoutData(layoutData);
		// if (_composite != null) {
		// _composite.setLayoutData(layoutData);
		// }

	}

	@Override
	public void setSelected(boolean isSelected, boolean contextIsValid)
	{
		this._selected = isSelected;
		_comboDayFrom.setEnabled(_selected && _editable);
		_comboMonthFrom.setEnabled(_selected && _editable);
		_spinnerYearFrom.setEnabled(_selected && _editable);

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
		if (_date.isValid())
		{
			_decoTimeFrom.setImage(null);
		}
		else
		{
			_decoTimeFrom.setImage(FieldDecorationRegistry.getDefault()
					.getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).getImage());
		}
		setValid(_date.isValid());
		
	}

	@Override
	public void validate()
	{
		if (_parentEditor != null && !_loading)
		{
			_parentEditor.validate();
		}

	}

	private void setValid(boolean valid) {
		this._isValid = valid;
		
	}

	@Override
	public void saveInput()
	{
		if (_parentEditor != null && !_loading)
		{
			_parentEditor.saveInput();
		}

	}

	@Override
	public void setEditable(boolean editable) {
		this._editable  = editable;
		_comboDayFrom.setEnabled(_selected && _editable);
		_comboMonthFrom.setEnabled(_selected && _editable);
		_spinnerYearFrom.setEnabled(_selected && _editable);
	}
}
