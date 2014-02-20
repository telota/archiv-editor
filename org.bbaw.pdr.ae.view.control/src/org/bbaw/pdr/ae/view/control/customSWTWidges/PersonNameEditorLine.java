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
import java.util.List;

import org.bbaw.pdr.ae.common.AEVIEWConstants;
import org.bbaw.pdr.ae.common.CommonActivator;
import org.bbaw.pdr.ae.common.NLMessages;
import org.bbaw.pdr.ae.common.icons.IconsInternal;
import org.bbaw.pdr.ae.model.Aspect;
import org.bbaw.pdr.ae.model.Person;
import org.bbaw.pdr.ae.model.TaggingRange;
import org.bbaw.pdr.ae.model.Validation;
import org.bbaw.pdr.ae.view.control.interfaces.IAEBasicEditor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

public class PersonNameEditorLine extends Composite implements IAEBasicEditor
{

	private Composite _composite;

	private Aspect _aspect;

	private Person _currentPerson;

	private Text _surNameText;

	private Text _foreNameText;

	private Label _labelSurName;

	private Label _labelForeName;

	private boolean _isValid = true;
	private boolean _isDirty = false;

	/** The WHIT e_ color. */
	private static final Color WHITE_COLOR = Display.getDefault().getSystemColor(SWT.COLOR_WHITE);

	private IAEBasicEditor _parentEditor;

	/** Instance of shared image registry. */
	private ImageRegistry _imageReg = CommonActivator.getDefault().getImageRegistry();

	/** The _selection listener. */
	private ArrayList<SelectionListener> _selectionListener = new ArrayList<SelectionListener>();

	private ValidationEditorLine _validationEditor;

	private Button _deletePersonName;

	/** The _selection listener. */
	private ArrayList<Listener> _deleteListener = new ArrayList<Listener>();

	public PersonNameEditorLine(IAEBasicEditor parentEditor, Person currentPerson, Aspect aspect, Composite parent,
			int style)
	{
		super(parent, style);
		_parentEditor = parentEditor;
		this._aspect = aspect;
		this._currentPerson = currentPerson;
		createEditor();
		loadAspect();
		_composite.pack();
		_composite.layout();
		// this.setSize(SWT.DEFAULT, 18);
		// this.pack();
		this.layout();
	}

	public void addDeleteListener(Listener listener)
	{
		if (listener != null)
		{
			_deleteListener.add(listener);
		}

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

		_composite = new Composite(this, SWT.NONE);
		_composite.setLayoutData(new GridData());
		((GridData) _composite.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _composite.getLayoutData()).grabExcessHorizontalSpace = true;
		_composite.setLayout(new GridLayout(5, false));
		((GridLayout) _composite.getLayout()).marginHeight = 0;
		((GridLayout) _composite.getLayout()).verticalSpacing = 0;

		_labelSurName = new Label(_composite, SWT.NONE);
		_labelSurName.setText(NLMessages.getString("Dialog_user_surname") + "*");
		_labelSurName.setLayoutData(new GridData());
		((GridData) _labelSurName.getLayoutData()).horizontalSpan = 1;
		((GridData) _labelSurName.getLayoutData()).horizontalAlignment = SWT.RIGHT;

		_surNameText = new Text(_composite, SWT.BORDER);
		_surNameText.setBackground(WHITE_COLOR);
		_surNameText.setLayoutData(new GridData());
		((GridData) _surNameText.getLayoutData()).horizontalSpan = 1;
		((GridData) _surNameText.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _surNameText.getLayoutData()).grabExcessHorizontalSpace = true;

		_surNameText.addKeyListener(new KeyListener()
		{

			@Override
			public void keyReleased(KeyEvent e)
			{

			}

			@Override
			public void keyPressed(KeyEvent e)
			{
				contentChanged();
			}
		});

		_surNameText.addFocusListener(new FocusListener()
		{
			@Override
			public void focusGained(final FocusEvent e)
			{
				Event ee = new Event();
				ee.widget = PersonNameEditorLine.this;
				SelectionEvent se = new SelectionEvent(ee);
				for (SelectionListener s : _selectionListener)
				{
					s.widgetSelected(se);
				}
			}

			@Override
			public void focusLost(FocusEvent e)
			{
				contentChanged();

			}
		});

		_labelForeName = new Label(_composite, SWT.NONE);
		_labelForeName.setText(NLMessages.getString("Editor_name_given"));
		_labelForeName.setLayoutData(new GridData());
		((GridData) _labelForeName.getLayoutData()).horizontalSpan = 1;
		((GridData) _labelForeName.getLayoutData()).horizontalAlignment = SWT.RIGHT;

		_foreNameText = new Text(_composite, SWT.BORDER);
		_foreNameText.setLayoutData(new GridData());
		((GridData) _foreNameText.getLayoutData()).horizontalSpan = 1;
		((GridData) _foreNameText.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _foreNameText.getLayoutData()).grabExcessHorizontalSpace = true;

		_foreNameText.addKeyListener(new KeyListener()
		{

			@Override
			public void keyReleased(KeyEvent e)
			{

			}

			@Override
			public void keyPressed(KeyEvent e)
			{
				contentChanged();
			}
		});
		_foreNameText.addFocusListener(new FocusListener()
		{
			@Override
			public void focusGained(final FocusEvent e)
			{
				Event ee = new Event();
				ee.widget = PersonNameEditorLine.this;
				SelectionEvent se = new SelectionEvent(ee);
				for (SelectionListener s : _selectionListener)
				{
					s.widgetSelected(se);
				}
			}

			@Override
			public void focusLost(FocusEvent e)
			{
				// if (isDirty())
				// {
				// saveChanges();
				// }
			}
		});
		_deletePersonName = new Button(_composite, SWT.PUSH);
		_deletePersonName.setImage(_imageReg.get(IconsInternal.REMOVE));
		_deletePersonName.setText(NLMessages.getString("EditorLite_remove_name"));
		_deletePersonName.setLayoutData(new GridData());
		((GridData) _deletePersonName.getLayoutData()).horizontalSpan = 1;
		((GridData) _deletePersonName.getLayoutData()).horizontalAlignment = SWT.RIGHT;

		_deletePersonName.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				Event ee = new Event();
				ee.widget = PersonNameEditorLine.this;
				ee.data = _aspect;
				ee.text = "del";
				for (Listener s : _deleteListener)
				{
					s.handleEvent(ee);
				}
				contentChanged();
			}

		});
		// _validationEditor = new
		// ValidationEditorLine(PersonNameEditorLine.this, null, _composite,
		// true, SWT.FILL);
		_validationEditor.setLayoutData(new GridData());
		((GridData) _validationEditor.getLayoutData()).horizontalSpan = 5;
		((GridData) _validationEditor.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _validationEditor.getLayoutData()).grabExcessHorizontalSpace = true;
		_validationEditor.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				Event ee = new Event();
				ee.widget = PersonNameEditorLine.this;
				SelectionEvent se = new SelectionEvent(ee);
				se.data = PersonNameEditorLine.this;
				for (SelectionListener s : _selectionListener)
				{
					s.widgetSelected(se);
				}
			}
		});
	}

	@Override
	public boolean isDirty()
	{
		return _isDirty;
	}


	private void loadAspect()
	{
		List<TaggingRange> rangeList = _aspect.getRangeList();
		String surName = "";
		String foreName = "";
		if (rangeList != null && !rangeList.isEmpty())
		{
			for (TaggingRange tr : rangeList)
			{
				if (tr.getName() != null && tr.getType() != null)
				{
					if (tr.getName().equals("persName") && tr.getType().equals("surname"))
					{
						if (surName.length() > 0)
						{
							surName += " ";
						}
						surName += tr.getTextValue();

					}
					if (tr.getName().equals("persName") && tr.getType().equals("forename"))
					{
						if (foreName.length() > 0)
						{
							foreName += " ";
						}
						foreName += tr.getTextValue();
					}
				}
			}
			_surNameText.setText(surName);
			_surNameText.redraw();
			_foreNameText.setText(foreName);
		}

		else if (_aspect.getNotification() != null)
		{
			_surNameText.setText(_aspect.getNotification());
		}

		if (_aspect.getValidation() != null && _aspect.getValidation().getValidationStms() != null
				&& !_aspect.getValidation().getValidationStms().isEmpty())
		{
			_validationEditor.setInput(_aspect.getValidation().getValidationStms().firstElement());
		}
		_aspect.setDirty(false);
		contentChanged();	}

	private void moveRanges(List<TaggingRange> rangeList, TaggingRange trAfter, int move)
	{
		boolean found = false;
		for (TaggingRange tr : rangeList)
		{
			if (found)
			{
				tr.setStart(tr.getStart() + move);
			}
			if (tr.equals(trAfter))
			{
				found = true;
			}

		}

	}

	public void saveAspect()
	{
		saveChanges();
	}

	private void saveChanges()
	{
		// remove old TaggingRanges
		List<TaggingRange> rangeList = _aspect.getRangeList();
		List<TaggingRange> helpList = new ArrayList<TaggingRange>(2);
		if (!_surNameText.isDisposed())
		{
			String[] surNames = _surNameText.getText().trim().split(" ");
			String[] foreNames = _foreNameText.getText().trim().split(" ");

			int sur = 0;
			int fore = 0;
			int move = 0;
			for (TaggingRange tr : rangeList)
			{
				if (tr.getName() != null && tr.getType() != null)
				{
					if (tr.getName().equals("persName") && tr.getType().equals("surname"))
					{
						if (surNames != null && surNames.length > sur)
						{
							if (tr.getTextValue().equals(surNames[sur]))
							{
								sur++;
							}
							else
							{
								_aspect.setNotification(_aspect.getNotification().replace(tr.getTextValue(),
										surNames[sur]));
								tr.setTextValue(surNames[sur]);
								move = tr.getLength() - surNames[sur].length();
								tr.setLength(surNames[sur].length());
								moveRanges(rangeList, tr, move);
								sur++;
							}
						}
						else
						{
							helpList.add(tr);
						}
					}
					if (tr.getName().equals("persName") && tr.getType().equals("forename"))
					{
						if (foreNames != null && foreNames.length > fore)
						{
							if (tr.getTextValue().equals(foreNames[fore]))
							{
								fore++;
							}
							else
							{
								_aspect.setNotification(_aspect.getNotification().replace(tr.getTextValue(),
										surNames[sur]));
								tr.setTextValue(foreNames[fore]);
								move = tr.getLength() - foreNames[fore].length();
								tr.setLength(foreNames[fore].length());
								moveRanges(rangeList, tr, move);
								fore++;
							}
						}
						else
						{
							helpList.add(tr);
						}
					}
				}
			}
			if (surNames.length > sur)
			{
				if (_aspect.getNotification() == null)
				{
					_aspect.setNotification("");
				}
				else if (_aspect.getNotification().length() > 0)
				{
					_aspect.setNotification(_aspect.getNotification() + " ");
				}
				int start = _aspect.getNotification().length();
				String name;
				while (sur < surNames.length)
				{
					name = surNames[sur];
					TaggingRange tr = new TaggingRange("persName", "surname", null, null, _currentPerson.getPdrId()
							.toString(), null, start, name.length());
					tr.setTextValue(name);
					_aspect.getRangeList().add(tr);
					_aspect.setNotification(_aspect.getNotification() + name);
					sur++;

				}
			}
			if (foreNames.length > fore)
			{
				if (_aspect.getNotification() == null)
				{
					_aspect.setNotification("");
				}
				else if (_aspect.getNotification().length() > 0)
				{
					_aspect.setNotification(_aspect.getNotification() + " ");
				}
				int start = _aspect.getNotification().length();
				String name;
				while (fore < foreNames.length)
				{
					name = foreNames[fore];
					TaggingRange tr = new TaggingRange("persName", "forename", null, null, _currentPerson.getPdrId()
							.toString(), null, start, name.length());
					tr.setTextValue(name);
					_aspect.getRangeList().add(tr);
					_aspect.setNotification(_aspect.getNotification() + name);
					fore++;

				}
			}
			if (helpList != null && !helpList.isEmpty())
			{
				_aspect.getRangeList().removeAll(helpList);
			}

			if (_aspect.getValidation() == null)
			{
				_aspect.setValidation(new Validation());
			}
			if (_aspect.getValidation().getValidationStms().contains(_validationEditor.getValidationStm()))
			{
				_aspect.getValidation().getValidationStms().remove(_validationEditor.getValidationStm());
			}
			_aspect.getValidation().getValidationStms().add(_validationEditor.getValidationStm());

			_aspect.setDirty(true);
			setDirty(false);
		}
	}

	@Override
	public void setBackground(Color color)
	{
		if (!super.isDisposed())
		{
			super.setBackground(color);
			_composite.setBackground(color);
			_labelSurName.setBackground(color);
			_labelForeName.setBackground(color);
		}

	}

	@Override
	public void setDirty(boolean isDirty)
	{
		this._isDirty = isDirty;
		if (_isDirty)
		{
			_aspect.setDirty(true);
		}
	}

	@Override
	public void setForeground(Color color)
	{
		super.setForeground(color);
		_labelSurName.setForeground(color);
		_labelForeName.setForeground(color);
		_surNameText.setForeground(color);
		_foreNameText.setForeground(color);
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
		_validationEditor.setSelected(isSelected, contextIsValid);
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
		if (!_surNameText.isDisposed())
		{
			if (_aspect.getValidation() != null)
			{
				this._isValid = (_aspect.getValidation().isValid() && (_surNameText.getText().trim().length() > 0 || _foreNameText
						.getText().trim().length() > 0));
			}
			else
			{
				this._isValid = false;
			}
		}
		else
		{
			this._isValid = true;
		}
		setValid(_isValid);
	}

	private void setValid(boolean _isValid) {
		this._isValid = _isValid;
		
	}

	@Override
	public void validate()
	{
		if (_parentEditor != null)
		{
			_parentEditor.validate();
		}
	}

	public Aspect getAspect()
	{
		return _aspect;
	}

	@Override
	public boolean isValid()
	{
		return _isValid;
	}

	@Override
	public void saveInput()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void setInput(Object input) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setEditable(boolean editable) {
		// TODO Auto-generated method stub
		
	}
}
