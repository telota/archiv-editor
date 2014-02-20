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
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * The Class MarkupTooltip.
 * @author Christoph Plutte
 */
public class MarkupTooltip extends CustomTooltip
{

	/** The parent shell. */
	private Shell _parentShell;

	/** The tool tip text. */
	private String _toolTipText;

	/** The link. */
	private Text _text;

	/** The image reg. */
	private ImageRegistry _imageReg = CommonActivator.getDefault().getImageRegistry();

	/** The header text. */
	private String _headerText = "Markup Information";

	private Composite _parentComposite;

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

	/**
	 * Instantiates a new markup tooltip.
	 * @param control the control
	 */
	public MarkupTooltip(final Control control)
	{
		super(control);
		this._parentShell = control.getShell();
	}

	public MarkupTooltip(final Control control, String headerText)
	{
		super(control);
		this._parentShell = control.getShell();
		this._headerText = headerText;
	}
	/**
	 * Creates the content area.
	 * @param parent the parent
	 * @return the composite
	 */
	protected final Composite createContentArea(final Composite parent)
	{
		this._parentComposite = parent;
		Composite comp = new Composite(parent, SWT.NONE);
		comp.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_INFO_BACKGROUND));

		GridLayout layout = new GridLayout();
		layout.marginWidth = 5;
		comp.setLayout(layout);
		_text = new Text(comp, SWT.NONE | SWT.WRAP | SWT.MULTI);

		if (_toolTipText != null)
		{
			_text.setText(_toolTipText);
		}
		_text.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_INFO_BACKGROUND));
		_text.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent e)
			{
				openURL();
			}
		});
		Point p = _text.computeSize(250, SWT.DEFAULT);
		_text.setSize(p);
		_text.setLayoutData(new GridData(p.x, p.y));
		// _text.redraw();
		// _text.update();
		_text.pack(true);

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
		data.widthHint = 200;
		topArea.setLayoutData(data);
		topArea.setBackground(JFaceResources.getColorRegistry().get(HEADER_BG_COLOR));

		gl = new GridLayout(2, false);
		gl.marginBottom = 2;
		gl.marginTop = 2;
		gl.marginHeight = 0;
		gl.marginWidth = 0;
		gl.marginLeft = 5;
		gl.marginRight = 2;

		topArea.setLayout(gl);

		Label l = new Label(topArea, SWT.NONE);
		l.setText(_headerText);
		l.setBackground(JFaceResources.getColorRegistry().get(HEADER_BG_COLOR));
		l.setFont(JFaceResources.getFontRegistry().get(HEADER_FONT));
		l.setForeground(JFaceResources.getColorRegistry().get(HEADER_FG_COLOR));
		l.setLayoutData(new GridData(GridData.FILL_BOTH));

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

		Label helpIcon = new Label(iconComp, SWT.NONE);
		helpIcon.setBackground(JFaceResources.getColorRegistry().get(HEADER_BG_COLOR));
		helpIcon.setImage(_imageReg.get(IconsInternal.MARKUP));
		helpIcon.addMouseListener(new MouseAdapter()
		{

			@Override
			public void mouseDown(final MouseEvent e)
			{
				hide();
				// MarkupTooltip.this.dispose();
				openHelp();
			}
		});

		Label closeIcon = new Label(iconComp, SWT.NONE);
		closeIcon.setBackground(JFaceResources.getColorRegistry().get(HEADER_BG_COLOR));
		closeIcon.setImage(JFaceResources.getImage(HEADER_CLOSE_ICON));
		closeIcon.addMouseListener(new MouseAdapter()
		{

			@Override
			public void mouseDown(final MouseEvent e)
			{
				_parentShell.setFocus();
				hide();
				// MarkupTooltip.this.dispose();
			}
		});

		createContentArea(comp).setLayoutData(new GridData(GridData.FILL_BOTH));

		return comp;
	}

	/**
	 * Gets the tool tip text.
	 * @return the tool tip text
	 */
	public final String getToolTipText()
	{
		return _toolTipText;
	}

	/**
	 * Open help.
	 */
	protected void openHelp()
	{
		// parentShell.setFocus();
		//
		// MessageBox box = new MessageBox(parentShell,SWT.ICON_INFORMATION);
		// box.setText("Info");
		// box.setMessage("Here is where we'd show some information.");
		// box.open();
	}

	/**
	 * Open url.
	 */
	protected void openURL()
	{
		// MessageBox box = new MessageBox(parentShell,SWT.ICON_INFORMATION);
		// box.setText("Eclipse.org");
		// box.setMessage("Here is where we'd open the URL.");
		// box.open();
	}

	/**
	 * Sets the tool tip text.
	 * @param toolTipText the new tool tip text
	 */
	public final void setToolTipText(final String toolTipText)
	{
		this._toolTipText = toolTipText;
		if (_text != null && !_text.isDisposed())
		{
			_text.setText(toolTipText);
			_text.pack(true);
		}
	}
}
