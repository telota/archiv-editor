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

import org.bbaw.pdr.ae.common.CommonActivator;
import org.bbaw.pdr.ae.common.icons.IconsInternal;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.util.Policy;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Spinner;

/**
 * The Class TimeSliderToolTip.
 * @author Christoph Plutte
 */
public class TimeSliderToolTip extends CustomTooltip
{

	/** The time. */
	private int _time;

	/** The time scale. */
	private Scale _timeScale;

	/** The year. */
	private Label _year;

	/** The image reg. */
	private ImageRegistry _imageReg = CommonActivator.getDefault().getImageRegistry();

	/** The header text. */
	private String _headerText = "Select a Year";

	/** The Constant HEADER_BG_COLOR. */
	public static final String HEADER_BG_COLOR = Policy.JFACE + ".TOOLTIP_HEAD_BG_COLOR";

	/** The Constant HEADER_FG_COLOR. */
	public static final String HEADER_FG_COLOR = Policy.JFACE + ".TOOLTIP_HEAD_FG_COLOR";

	/** The Constant HEADER_FONT. */
	public static final String HEADER_FONT = Policy.JFACE + ".TOOLTIP_HEAD_FONT";

	/** The Constant HEADER_CLOSE_ICON. */
	public static final String HEADER_CLOSE_ICON = Policy.JFACE + ".TOOLTIP_CLOSE_ICON";

	/** The Constant HEADER_HELP_ICON. */
	public static final String HEADER_HELP_ICON = Policy.JFACE + ".TOOLTIP_HELP_ICON";

	private static final int THOUSAND = 1000;

	/** The year spinner. */
	private Spinner _yearSpinner;

	/**
	 * Instantiates a new time slider tool tip.
	 * @param control the control
	 * @param preselection the preselection
	 */
	public TimeSliderToolTip(final Control control, final int preselection)
	{
		super(control);
		this._yearSpinner = (Spinner) control;
		this._time = preselection;
	}

	/**
	 * Creates the content area.
	 * @param parent the parent
	 * @return the composite
	 */
	protected Composite createContentArea(final Composite parent)
	{
		Composite comp = new Composite(parent, SWT.NONE);
		comp.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_INFO_BACKGROUND));

		return comp;
	}

	@Override
	protected final Composite createToolTipContentArea(final Event event, final Composite parent)
	{
		Composite comp = new Composite(parent, SWT.NONE);

		GridLayout gl = new GridLayout(1, false);
		gl.marginBottom = 0;
		gl.marginTop = 0;
		gl.marginHeight = 0;
		gl.marginWidth = 0;
		gl.marginLeft = 0;
		gl.marginRight = 0;
		gl.verticalSpacing = 1;
		comp.setLayout(gl);

		Composite topArea = new Composite(comp, SWT.NONE);
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, false);
		data.widthHint = 400;
		topArea.setLayoutData(data);
		topArea.setBackground(JFaceResources.getColorRegistry().get(HEADER_BG_COLOR));

		gl = new GridLayout(3, false);
		gl.marginBottom = 2;
		gl.marginTop = 2;
		gl.marginHeight = 0;
		gl.marginWidth = 0;
		gl.marginLeft = 5;
		gl.marginRight = 2;

		topArea.setLayout(gl);

		Label l = new Label(topArea, SWT.NONE);
		l.setText(_headerText + ":");
		l.setBackground(JFaceResources.getColorRegistry().get(HEADER_BG_COLOR));
		l.setFont(JFaceResources.getFontRegistry().get(HEADER_FONT));
		l.setForeground(JFaceResources.getColorRegistry().get(HEADER_FG_COLOR));
		l.setLayoutData(new GridData(GridData.FILL_BOTH));

		_year = new Label(topArea, SWT.NONE);
		int i = _time + THOUSAND;
		_year.setText("" + i);
		_year.setBackground(JFaceResources.getColorRegistry().get(HEADER_BG_COLOR));
		_year.setFont(JFaceResources.getFontRegistry().get(HEADER_FONT));
		_year.setForeground(JFaceResources.getColorRegistry().get(HEADER_FG_COLOR));
		_year.setLayoutData(new GridData(GridData.FILL_BOTH));

		Composite iconComp = new Composite(topArea, SWT.NONE);
		iconComp.setLayoutData(new GridData());
		iconComp.setLayout(new GridLayout(2, false));
		iconComp.setBackground(JFaceResources.getColorRegistry().get(HEADER_BG_COLOR));
		gl = new GridLayout(2, false);
		gl.marginBottom = 0;
		gl.marginTop = 0;
		gl.marginHeight = 0;
		gl.marginWidth = 0;
		gl.marginLeft = 0;
		gl.marginRight = 0;
		iconComp.setLayout(gl);

		Label closeIcon = new Label(iconComp, SWT.NONE);
		closeIcon.setImage(_imageReg.get(IconsInternal.TIME));
		closeIcon.setBackground(JFaceResources.getColorRegistry().get(HEADER_BG_COLOR));
		_timeScale = new Scale(comp, SWT.HORIZONTAL);

		_timeScale.setMinimum(0);
		_timeScale.setMaximum(1100);
		_timeScale.setIncrement(1);
		_timeScale.setPageIncrement(10);
		_timeScale.setSelection(_time);
		_timeScale.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_INFO_BACKGROUND));
		_timeScale.setLayoutData(new GridData());
		((GridData) _timeScale.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) _timeScale.getLayoutData()).verticalAlignment = SWT.FILL;
		((GridData) _timeScale.getLayoutData()).grabExcessHorizontalSpace = true;
		_timeScale.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent e)
			{

				_yearSpinner.setSelection(_timeScale.getSelection() + THOUSAND);
				int i = _timeScale.getSelection() + THOUSAND;
				_year.setText("" + i);
				_yearSpinner.notifyListeners(SWT.Selection, new Event());
			}
		});

		// XXX RAP auskommentiert
		// _timeScale.addMouseWheelListener(new MouseWheelListener()
		// {
		//
		// @Override
		// public void mouseScrolled(final MouseEvent e)
		// {
		// Scale src = (Scale) e.getSource();
		// src.setSelection(src.getSelection() - e.count);
		// _yearSpinner.setSelection(_timeScale.getSelection() + 1000);
		// int i = _timeScale.getSelection() + 1000;
		// _year.setText("" + i);
		//
		// _yearSpinner.notifyListeners(SWT.Selection, new Event());
		//
		// }
		// });
		createContentArea(comp).setLayoutData(new GridData(GridData.FILL_BOTH));
		_timeScale.setFocus();
		return comp;
	}

	/**
	 * Gets the time.
	 * @return the time
	 */
	public final int getTime()
	{
		return _time;
	}

	/**
	 * Sets the time.
	 * @param time the new time
	 */
	public final void setTime(final int time)
	{
		this._time = time - THOUSAND;

		if (_timeScale != null && !_timeScale.isDisposed())
		{
			_timeScale.setSelection(time - THOUSAND);
		}
	}

}