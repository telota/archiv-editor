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
import org.bbaw.pdr.ae.metamodel.PdrDate;
import org.bbaw.pdr.ae.model.Time;
import org.bbaw.pdr.ae.model.TimeStm;
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
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;

public class TimeStmEditorLine extends Composite implements IAEBasicEditor
{

	private TimeStm _timeStm;

	private Time _timeFrom;

	private Time _timeTo;

	private Composite _composite;

	private Button _dateFromLabel;

	private Button _dateToButton;

	private Combo _comboDayFrom;
	private Combo _comboMonthFrom;

	private YearSpinner _spinnerYearFrom;

	private Combo _comboDayTo;
	private Combo _comboMonthTo;
	private YearSpinner _spinnerYearTo;

	private ControlDecoration _decoTimeFrom;
	private ControlDecoration _decoTimeTo;

	private boolean _pointOfTime;

	private boolean _customSetTime = false;

	private boolean _isDirty = false;

	private boolean _isValid = true;

	private IAEBasicEditor _parentEditor;
	/** The _selection listener. */
	private ArrayList<SelectionListener> _selectionListener = new ArrayList<SelectionListener>();

	private boolean _editable = true;

	private boolean _selected;

	/** The PRESELECTE d_ year. */
	private static final int PRESELECTED_YEAR = Platform.getPreferencesService().getInt(CommonActivator.PLUGIN_ID,
			"ASPECT_PRESELECTED_DATE_YEAR", AEConstants.ASPECT_PRESELECTED_DATE_YEAR, null);

	public TimeStmEditorLine(IAEBasicEditor parentEditor, TimeStm timeStm, Composite parent, int style)
	{
		super(parent, style);
		_parentEditor = parentEditor;
		setTimesInternal(timeStm);

		createEditor();

		loadTimeStm();

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
			_selectionListener.add(listener);
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
				Event ee = new Event();
				ee.widget = TimeStmEditorLine.this;
				SelectionEvent se = new SelectionEvent(ee);
				for (SelectionListener s : _selectionListener)
				{
					s.widgetSelected(se);
				}

			}

			@Override
			public void focusLost(final FocusEvent e)
			{

			}
		};

		_composite = new Composite(this, SWT.NONE);
		_composite.setLayoutData(new GridData());
		((GridData) _composite.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _composite.getLayoutData()).grabExcessHorizontalSpace = true;
		_composite.setLayout(new GridLayout(12, false));
		((GridLayout) _composite.getLayout()).marginHeight = 0;
		((GridLayout) _composite.getLayout()).verticalSpacing = 0;

		_dateFromLabel = new Button(_composite, SWT.CHECK);
		_dateFromLabel.setText(NLMessages.getString("Editor_timeStm_date_from"));
		_dateFromLabel.setLayoutData(new GridData());
		((GridData) _dateFromLabel.getLayoutData()).horizontalSpan = 1;
		((GridData) _dateFromLabel.getLayoutData()).horizontalAlignment = SWT.RIGHT;
		_dateFromLabel.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				_customSetTime = !_customSetTime;
				setActivateTime(_customSetTime);

				contentChanged();
			}
		});

		_comboDayFrom = new Combo(_composite, SWT.READ_ONLY);
		_comboDayFrom.setLayoutData(new GridData());
		_comboDayFrom.setItems(AEConstants.DAYS);
		_comboDayFrom.addFocusListener(focusListener);

		_comboDayFrom.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent se)
			{
				_timeFrom.getTimeStamp().setDay(_comboDayFrom.getSelectionIndex());
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
				_timeFrom.getTimeStamp().setMonth(_comboMonthFrom.getSelectionIndex());
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
				_timeFrom.getTimeStamp().setYear(_spinnerYearFrom.getSelection());
				if (_timeFrom.isValid())
				{
					_decoTimeFrom.setImage(null);
				}
				else
				{
					_decoTimeFrom.setImage(FieldDecorationRegistry.getDefault()
							.getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).getImage());
				}
				contentChanged();
			}
		});

		_dateToButton = new Button(_composite, SWT.CHECK);
		_dateToButton.setText(NLMessages.getString("Editor_timeStm_date_to"));
		_dateToButton.setLayoutData(new GridData());
		((GridData) _dateToButton.getLayoutData()).horizontalSpan = 1;
		((GridData) _dateToButton.getLayoutData()).horizontalAlignment = SWT.RIGHT;
		_dateToButton.addFocusListener(focusListener);

		_dateToButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				_pointOfTime = !_pointOfTime;
				setPointOfTiem(_pointOfTime);
				contentChanged();
			}
		});

		_comboDayTo = new Combo(_composite, SWT.READ_ONLY);
		_comboDayTo.setLayoutData(new GridData());
		((GridData) _comboDayTo.getLayoutData()).horizontalAlignment = GridData.FILL;
		((GridData) _comboDayTo.getLayoutData()).grabExcessHorizontalSpace = true;
		_comboDayTo.setItems(AEConstants.DAYS);
		_comboDayTo.addFocusListener(focusListener);


		_comboDayTo.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent se)
			{
				_timeTo.getTimeStamp().setDay(_comboDayTo.getSelectionIndex());
				contentChanged();
			}
		});

		_comboMonthTo = new Combo(_composite, SWT.READ_ONLY);
		_comboMonthTo.setLayoutData(new GridData());
		((GridData) _comboMonthTo.getLayoutData()).horizontalAlignment = GridData.FILL;
		((GridData) _comboMonthTo.getLayoutData()).grabExcessHorizontalSpace = true;
		_comboMonthTo.setItems(AEConstants.MONTHS);
		_comboMonthTo.addFocusListener(focusListener);


		_comboMonthTo.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent se)
			{
				_timeTo.getTimeStamp().setMonth(_comboMonthTo.getSelectionIndex());
				contentChanged();
			}
		});

		_spinnerYearTo = new YearSpinner(_composite, SWT.BORDER);
		_spinnerYearTo.addFocusListener(focusListener);

		_decoTimeTo = new ControlDecoration(_spinnerYearTo, SWT.LEFT | SWT.TOP);
		if (_timeTo.getTimeStamp() != null)
		{
			_spinnerYearTo.setSelection(_timeTo.getTimeStamp().getYear());
		}
		else
		{
			_decoTimeTo.setImage(FieldDecorationRegistry.getDefault()
					.getFieldDecoration(FieldDecorationRegistry.DEC_REQUIRED).getImage());
		}
		_spinnerYearTo.pack();
		_spinnerYearTo.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent se)
			{
				_timeTo.getTimeStamp().setYear(_spinnerYearTo.getSelection());
				if (_timeTo.isValid())
				{
					_decoTimeTo.setImage(null);
				}
				else
				{
					_decoTimeTo.setImage(FieldDecorationRegistry.getDefault()
							.getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).getImage());
				}
				contentChanged();
			}
		});


	}

	public TimeStm getTimeStm()
	{
		return _timeStm;
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

	private void loadTimeStm()
	{
		if (_timeFrom != null && _timeFrom.getTimeStamp() != null)
		{
			_comboDayFrom.select(_timeFrom.getTimeStamp().getDay());
			_comboMonthFrom.select(_timeFrom.getTimeStamp().getMonth());
			_spinnerYearFrom.setSelection(_timeFrom.getTimeStamp().getYear());
		}
		else
		{
			// decoTime.setImage(FieldDecorationRegistry.getDefault()
			// .getFieldDecoration(FieldDecorationRegistry.DEC_REQUIRED).getImage());
		}
		if (_timeTo != null && _timeTo.getTimeStamp() != null)
		{
			_comboDayTo.select(_timeTo.getTimeStamp().getDay());
			_comboMonthTo.select(_timeTo.getTimeStamp().getMonth());
			_spinnerYearTo.setSelection(_timeTo.getTimeStamp().getYear());
		}
		setActivateTime(_customSetTime);

	}

	private void setActivateTime(boolean customSetTime)
	{
		if (customSetTime)
		{
			if (!_timeStm.getTimes().contains(_timeFrom))
			{
				_timeStm.getTimes().add(_timeFrom);
			}
			_timeStm.getTimes().remove(_timeTo);
		}
		else
		{
			_timeStm.getTimes().remove(_timeFrom);
			_timeStm.getTimes().remove(_timeTo);
			_decoTimeFrom.setImage(null);
			_decoTimeTo.setImage(null);

		}
		_comboDayFrom.setEnabled(customSetTime && _editable);
		_comboMonthFrom.setEnabled(customSetTime && _editable);
		_spinnerYearFrom.setEnabled(customSetTime && _editable);
		_dateFromLabel.setSelection(customSetTime && _editable);
		_dateToButton.setEnabled(customSetTime && _editable);
		_dateToButton.setSelection(customSetTime && !_pointOfTime && _editable);
		_comboDayTo.setEnabled(customSetTime && !_pointOfTime && _editable);
		_comboMonthTo.setEnabled(customSetTime && !_pointOfTime && _editable);
		_spinnerYearTo.setEnabled(customSetTime && !_pointOfTime && _editable);
	}

	@Override
	public void setBackground(Color color)
	{
		super.setBackground(color);
		_composite.setBackground(color);
		_dateFromLabel.setBackground(color);
	}

	@Override
	public void setDirty(boolean isDirty)
	{
		this._isDirty = isDirty;
		if (_parentEditor != null && _isDirty)
		{
			_parentEditor.setDirty(true);
		}

	}

	@Override
	public void setForeground(Color color)
	{
		super.setForeground(color);
		_dateFromLabel.setForeground(color);

	}

	@Override
	public void setLayoutData(Object layoutData)
	{
		super.setLayoutData(layoutData);
		// if (_composite != null) {
		// _composite.setLayoutData(layoutData);
		// }

	}

	private void setPointOfTiem(boolean pointOfTime)
	{
		if (!pointOfTime)
		{
			if (!_timeStm.getTimes().contains(_timeTo))
			{
				_timeStm.getTimes().add(_timeTo);
			}
			_timeFrom.setType("from");
		}
		else
		{
			_timeStm.getTimes().remove(_timeTo);
			_timeFrom.setType("when");
			_decoTimeTo.setImage(null);
		}
		_comboDayTo.setEnabled(!pointOfTime && _editable);
		_comboMonthTo.setEnabled(!pointOfTime && _editable);
		_spinnerYearTo.setEnabled(!pointOfTime && _editable);

	}

	@Override
	public void setSelected(boolean isSelected, boolean contextIsValid)
	{
		this._selected = isSelected;
		_comboDayFrom.setEnabled(_selected && _customSetTime && _editable);
		_comboMonthFrom.setEnabled(_selected && _customSetTime && _editable);
		_spinnerYearFrom.setEnabled(_selected && _customSetTime && _editable);
		_dateToButton.setEnabled(_selected && _customSetTime && _editable);

		_comboDayTo.setEnabled(_selected && _customSetTime && !_pointOfTime && _editable);
		_comboMonthTo.setEnabled(_selected && _customSetTime && !_pointOfTime && _editable);
		_spinnerYearTo.setEnabled(_selected && _customSetTime && !_pointOfTime && _editable);
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
		setValid(_timeStm.isValid());
	}

	private void setTimesInternal(TimeStm timeStm)
	{
		this._timeStm = timeStm;
		if (_timeStm != null && _timeStm.getTimes() != null)
		{
			for (Time t : _timeStm.getTimes())
			{
				if (t.getType() != null)
				{
					if (t.getType().equals("when"))
					{
						_timeFrom = t;
						_pointOfTime = true;
						_customSetTime = true;
					}
					else if (t.getType().equals("from"))
					{
						_timeFrom = t;
						_pointOfTime = false;
						_customSetTime = true;
					}
					else if (t.getType().equals("to"))
					{
						_timeTo = t;
						_pointOfTime = false;
						_customSetTime = true;
					}
				}

			}
		}
		if (!_customSetTime)
		{
			if (_timeStm == null)
			{
				_timeStm = new TimeStm();
				_timeStm.setType("defined");
			}
			_timeFrom = new Time();
			_timeFrom.setTimeStamp(new PdrDate(PRESELECTED_YEAR, 0, 0));
			_timeFrom.setType("when");
			_timeFrom.setAccuracy("exact");
			_pointOfTime = true;
		}
		else
		{
			setDirty(true);
		}
		if (_timeTo == null)
		{
			_timeTo = new Time();
			_timeTo.setTimeStamp(new PdrDate(PRESELECTED_YEAR, 0, 0));
			_timeTo.setType("to");
			_timeTo.setAccuracy("exact");
		}


	}

	public void setInput(Object input)
	{
		if (input instanceof TimeStm)
		{
			_timeStm = (TimeStm) input;
			setTimesInternal((TimeStm)input);
			loadTimeStm();
		}
		
	}

	private void setValid(boolean isValid)
	{
		this._isValid = isValid;
	}

	@Override
	public void validate()
	{
		if (_parentEditor != null)
		{
			_parentEditor.validate();
		}

	}

	@Override
	public void saveInput()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void setEditable(boolean editable) {
		this._editable = editable;
		_comboDayFrom.setEnabled(_selected && _customSetTime && _editable);
		_comboMonthFrom.setEnabled(_selected && _customSetTime && _editable);
		_spinnerYearFrom.setEnabled(_selected && _customSetTime && _editable);
		_dateToButton.setEnabled(_selected && _customSetTime && _editable);

		_comboDayTo.setEnabled(_selected && _customSetTime && !_pointOfTime && _editable);
		_comboMonthTo.setEnabled(_selected && _customSetTime && !_pointOfTime && _editable);
		_spinnerYearTo.setEnabled(_selected && _customSetTime && !_pointOfTime && _editable);
		
	}
}
