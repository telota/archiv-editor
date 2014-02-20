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

import org.bbaw.pdr.ae.common.AEConstants;
import org.bbaw.pdr.ae.common.CommonActivator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.TypedListener;

/**
 * The Class YearSpinner.
 * @author Christoph Plutte
 */
public class YearSpinner extends Composite
{

	/** The year spinner. */
	private Spinner _yearSpinner;

	/** The preselection. */
	private int _preselection = Platform.getPreferencesService().getInt(CommonActivator.PLUGIN_ID,
			"ASPECT_PRESELECTED_DATE_YEAR", AEConstants.ASPECT_PRESELECTED_DATE_YEAR, null); //$NON-NLS-1$

	/**
	 * Instantiates a new year spinner.
	 * @param parent the parent
	 * @param style the style
	 */
	public YearSpinner(final Composite parent, final int style)
	{
		super(parent, SWT.None);
		_yearSpinner = new Spinner(YearSpinner.this, SWT.BORDER);
		GridLayout gl = new GridLayout(1, false);
		this.setLayout(gl);
		((GridLayout) this.getLayout()).marginHeight = 0;
		((GridLayout) this.getLayout()).marginWidth = 0;

		_yearSpinner.setMinimum(1000);
		_yearSpinner.setMaximum(2100);
		_yearSpinner.setSelection(_preselection);

		final TimeSliderToolTip timeSlider = new TimeSliderToolTip(_yearSpinner, _yearSpinner.getSelection())
		{

			@Override
			protected Composite createContentArea(final Composite parent)
			{
				Composite comp = super.createContentArea(parent);
				comp.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_WHITE));
				return comp;
			}

		};
		_yearSpinner.addSelectionListener(new SelectionListener()
		{

			@Override
			public void widgetDefaultSelected(final SelectionEvent e)
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void widgetSelected(final SelectionEvent e)
			{
				timeSlider.setTime(_yearSpinner.getSelection());
				YearSpinner.this.notifyListeners(SWT.Selection, new Event());
			}

		});
		timeSlider.setShift(new Point(0, 5));
		timeSlider.setPopupDelay(0);
		timeSlider.createToolTipContentArea(null, _yearSpinner);
		timeSlider.setShift(new Point(-5, -5));
		timeSlider.setHideOnMouseDown(false);
		timeSlider.activate();
		timeSlider.setTime(_yearSpinner.getSelection());
		_yearSpinner.setSelection(_preselection);

		this.pack();

	}

	/**
	 * Adds the selection listener.
	 * @param listener the listener
	 */
	public final void addSelectionListener(final SelectionListener listener)
	{

		if (listener != null)
		{
			addListener(SWT.Selection, new TypedListener(listener));
		}



	}

	/**
	 * Gets the selection.
	 * @return the selection
	 */
	public final int getSelection()
	{
		return _yearSpinner.getSelection();
	}

	@Override
	public void setEnabled(boolean enabled)
	{
		super.setEnabled(enabled);
		_yearSpinner.setEnabled(enabled);
	}

	/**
	 * Sets the maximum.
	 * @param maximum the new maximum
	 */
	public final void setMaximum(final int maximum)
	{
		_yearSpinner.setMaximum(maximum);
	}

	/**
	 * Sets the minimum.
	 * @param minimum the new minimum
	 */
	public final void setMinimum(final int minimum)
	{
		_yearSpinner.setMinimum(minimum);
	}

	/**
	 * Sets the selection.
	 * @param selection the new selection
	 */
	public final void setSelection(final int selection)
	{
		_yearSpinner.setSelection(selection);
	}
}
