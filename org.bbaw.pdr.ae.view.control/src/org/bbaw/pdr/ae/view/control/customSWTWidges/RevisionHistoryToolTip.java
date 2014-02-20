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

import java.text.SimpleDateFormat;

import org.bbaw.pdr.ae.common.AEConstants;
import org.bbaw.pdr.ae.common.CommonActivator;
import org.bbaw.pdr.ae.common.NLMessages;
import org.bbaw.pdr.ae.common.icons.IconsInternal;
import org.bbaw.pdr.ae.control.facade.Facade;
import org.bbaw.pdr.ae.metamodel.Record;
import org.bbaw.pdr.ae.metamodel.Revision;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.util.Policy;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 * The Class RevisionHistoryToolTip.
 * @author Christoph Plutte
 */
public class RevisionHistoryToolTip extends CustomTooltip
{

	/** The parent shell. */
	private Shell _parentShell;

	/** The _record. */
	private Record _record;

	/** The image reg. */
	private ImageRegistry _imageReg = CommonActivator.getDefault().getImageRegistry();

	/** The _facade. */
	private Facade _facade = Facade.getInstanz();
	/** date format of administrative dates in PDR. */
	private SimpleDateFormat _adminDateFormat = AEConstants.ADMINDATE_FORMAT;

	/** The header text. */
	private String _headerText = NLMessages.getString("Editor_revision_history");

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
	 * Instantiates a new revision history tool tip.
	 * @param control the control
	 * @param record the record
	 */
	public RevisionHistoryToolTip(final Control control, final Record record)
	{
		super(control);
		this._parentShell = control.getShell();
		this._record = record;
	}

	/**
	 * Creates the content area.
	 * @param parent the parent
	 * @return the composite
	 */
	protected final Composite createContentArea(final Composite parent)
	{
		Composite comp = new Composite(parent, SWT.NONE);
		comp.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_INFO_BACKGROUND));
		comp.setLayout(new GridLayout());
		comp.setLayoutData(new GridData());
		((GridData) comp.getLayoutData()).horizontalAlignment = SWT.FILL;
		((GridData) comp.getLayoutData()).grabExcessHorizontalSpace = true;
		((GridLayout) comp.getLayout()).makeColumnsEqualWidth = true;
		((GridLayout) comp.getLayout()).numColumns = 2;

		Label revisorL = new Label(comp, SWT.NONE);
		revisorL.setText(NLMessages.getString("Editor_createdBy"));
		revisorL.setLayoutData(new GridData());
		revisorL.setBackground(revisorL.getParent().getBackground());

		Label dateL = new Label(comp, SWT.NONE);
		dateL.setText(NLMessages.getString("Editor_date"));
		dateL.setLayoutData(new GridData());
		dateL.setBackground(dateL.getParent().getBackground());

		Label rev;
		Label date;
		if (_record != null && _record.getRevisions() != null && !_record.getRevisions().isEmpty())
		{
			for (Revision r : _record.getRevisions())
			{
				if (r != null && r.getAuthority() != null && r.getTimeStamp() != null)
				{
					rev = new Label(comp, SWT.NONE);
					rev.setLayoutData(new GridData());
					rev.setBackground(rev.getParent().getBackground());

					date = new Label(comp, SWT.NONE);
					date.setLayoutData(new GridData());
					date.setBackground(date.getParent().getBackground());

					rev.setText(_facade.getObjectDisplayName(r.getAuthority()));
					date.setText(_adminDateFormat.format(r.getTimeStamp()));
				}
			}
		}

		comp.pack();

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
		helpIcon.setImage(_imageReg.get(IconsInternal.USER));
		helpIcon.addMouseListener(new MouseAdapter()
		{

			@Override
			public void mouseDown(final MouseEvent e)
			{
				hide();
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
			}
		});

		createContentArea(comp).setLayoutData(new GridData(GridData.FILL_BOTH));

		return comp;
	}

}
