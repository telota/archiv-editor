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
package org.bbaw.pdr.ae.rap2;

import java.util.Observable;
import java.util.Observer;

import org.bbaw.pdr.ae.common.NLMessages;
import org.bbaw.pdr.ae.control.facade.Facade;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.menus.WorkbenchWindowControlContribution;

public class WorkbenchWindowControlContributionUser extends WorkbenchWindowControlContribution implements Observer
{
	private Label _userName;
	private Facade _facade = Facade.getInstanz();

	public WorkbenchWindowControlContributionUser()
	{
	}

	public WorkbenchWindowControlContributionUser(String id)
	{
		super(id);
	}

	@Override
	protected Control createControl(Composite parent)
	{
		_facade.addObserver(this);
		Composite c = new Composite(parent, SWT.NONE);
		c.setLayout(new GridLayout(1, false));
		Label l = new Label(c, SWT.None);
		l.setText(NLMessages.getString("Commandsr_current_user"));
		l.pack();

		_userName = new Label(c, SWT.None);
		if (_facade.getCurrentUser() != null)
		{
			_userName.setText(_facade.getCurrentUser().getDisplayName());
		}
		_userName.pack();

		return c;
	}

	@Override
	public void update(Observable o, Object arg)
	{
		if (arg.equals("newUser")) //$NON-NLS-1$ //$NON-NLS-2$
		{
			if (_facade.getCurrentUser() != null)
			{
				_userName.setText(_facade.getCurrentUser().getDisplayName());
				_userName.redraw();
				_userName.update();
			}
		}

	}

}
