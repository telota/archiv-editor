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
package org.bbaw.pdr.ae.view.markup.web;

import java.util.ArrayList;

import org.bbaw.pdr.ae.common.NLMessages;
import org.bbaw.pdr.ae.model.ReferenceMods;
import org.bbaw.pdr.ae.view.control.interfaces.IReferencePresentation;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.IHandlerService;

/**
 * The Class ReferencePresentationWeb.
 * @author Christoph Plutte
 */
public class ReferencePresentationWeb implements IReferencePresentation
{

	/** The _reference. */
	private ReferenceMods _reference;

	private Composite _parentComposite;
	/** The _text composite. */
	private Composite _textComposite;

	/** The _selected. */
	private boolean _selected;

	/** The _stext. */
	private Text _stext;


	/** The references selection adapter. */
	private SelectionAdapter _referencesSelectionAdapter;

	/** Resource Manager for colors and fonts. */
	private static LocalResourceManager resources = new LocalResourceManager(JFaceResources.getResources());

	/** The COLO r_ white. */
	private static final Color COLOR_WHITE = resources.createColor(new RGB(255, 255, 255));

	/** The COLO r_ selected. */
	private static final Color COLOR_SELECTED = resources.createColor(new RGB(255, 255, 200));

	/** The _selection listener. */
	private ArrayList<Listener> _selectionListener = new ArrayList<Listener>();


	/** The _double click listener. */
	private ArrayList<Listener> _doubleClickListener = new ArrayList<Listener>();

	@Override
	public void addDoubleClickListener(final Listener listener)
	{
		if (listener != null)
		{
			_doubleClickListener.add(listener);
		}

	}

	@Override
	public void addSelectionListener(final Listener listener)
	{
		if (listener != null)
		{
			_selectionListener.add(listener);
		}

	}

	/**
	 * Creates the additional references selection adapter.
	 */
	private void createAdditionalReferencesSelectionAdapter()
	{
		_referencesSelectionAdapter = new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent ev)
			{
				//			 System.out.println("Selection: " + ev.text); //$NON-NLS-1$
				Event se = new Event();
				se.data = ReferencePresentationWeb.this;
				for (Listener s : _selectionListener)
				{
					s.handleEvent(se);
				}
				Link button = (Link) ev.getSource();
				Event event = new Event();
				event.data = button.getData();
				IHandlerService handlerService = (IHandlerService) PlatformUI.getWorkbench().getService(
						IHandlerService.class);
				try
				{
					handlerService.executeCommand(
							"org.bbaw.pdr.ae.view.main.commands.OpenAdditionalReferencesView", event); //$NON-NLS-1$
				}
				catch (ExecutionException e)
				{
					e.printStackTrace();
				}
				catch (NotDefinedException e)
				{
					e.printStackTrace();
				}
				catch (NotEnabledException e)
				{
					e.printStackTrace();
				}
				catch (NotHandledException e)
				{
					e.printStackTrace();
				}
			}
		};
	}

	@Override
	public final void createPresentation()
	{
		loadReference();
		setSelected(false);
	}

	@Override
	public Control getControl()
	{
		return _stext;
	}

	@Override
	public final ReferenceMods getReference()
	{
		return _reference;
	}

	/**
	 * Load reference.
	 */
	private void loadReference()
	{
		_textComposite = new Composite(_parentComposite, SWT.BORDER);
		_textComposite.setLayout(new GridLayout(1, true));
		((GridLayout) _textComposite.getLayout()).verticalSpacing = 0;
		((GridLayout) _textComposite.getLayout()).marginHeight = 0;
		GridData gd = new GridData();
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalAlignment = GridData.FILL;
		gd.widthHint = 200;
		_textComposite.setLayoutData(gd);
		// System.out.println( event + category + position);
		createAdditionalReferencesSelectionAdapter();
		final ArrayList<Control> controls = new ArrayList<Control>();
		_stext = new Text(_textComposite, SWT.WRAP | SWT.NO_BACKGROUND | SWT.NO_FOCUS | SWT.CURSOR_ARROW );

		_stext.setEditable(false);
		_stext.setData("key", _reference.getPdrId().toString()); //$NON-NLS-1$
		_stext.setData("id", _reference.getPdrId().toString()); //$NON-NLS-1$
		_stext.setData("textOffset", 0); //$NON-NLS-1$
		// System.out.println("stext data cat: " + stext.getData("category")
		//		+ " position " + stext.getData("position")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		// IStatus slaCaret = new Status(IStatus.INFO,Activator.PLUGIN_ID,
		//		"ReferenceCatView load aspect - stext caret offset: " + _stext.getCaretOffset()); //$NON-NLS-1$
		// iLogger.log(slaCaret);
		_stext.addMouseListener(new MouseAdapter()
		{

			@Override
			public void mouseDoubleClick(final MouseEvent me)
			{
				Event se = new Event();
				se.data = ReferencePresentationWeb.this;
				for (Listener s : _doubleClickListener)
				{
					s.handleEvent(se);
				}
			}

			@Override
			public void mouseDown(final MouseEvent e)
			{
				Event se = new Event();
				se.data = ReferencePresentationWeb.this;
				for (Listener s : _selectionListener)
				{
					s.handleEvent(se);
				}

			}

		});
		_textComposite.addMouseListener(new MouseAdapter()
		{

			@Override
			public void mouseDoubleClick(final MouseEvent me)
			{
				Event se = new Event();
				se.data = ReferencePresentationWeb.this;
				for (Listener s : _doubleClickListener)
				{
					s.handleEvent(se);
				}
			}

			@Override
			public void mouseDown(final MouseEvent e)
			{
				Event se = new Event();
				se.data = ReferencePresentationWeb.this;
				for (Listener s : _selectionListener)
				{
					s.handleEvent(se);
				}

			}

		});

		_stext.append("\n"); //$NON-NLS-1$
		_stext.append(_reference.getDisplayNameLong());
		Link rLink = new Link(_textComposite, SWT.PUSH);
		rLink.addSelectionListener(_referencesSelectionAdapter);
		rLink.setData(_reference.getPdrId().toString());
		rLink.setBackground(COLOR_WHITE);
		rLink.setText("<a href=\"native\">" + _reference.getPdrId().toString() + "</a>"); //$NON-NLS-1$ //$NON-NLS-2$
		controls.add(rLink);
		
		if (_reference.getRecord() != null && _reference.getRecord().getRevisions() != null
				&& _reference.getRecord().getRevisions().firstElement() != null
				&& _reference.getRecord().getRevisions().firstElement().getAuthority() != null)
		{
			_stext.append(NLMessages.getString("View_lb_user")
					+ _reference.getRecord().getRevisions().firstElement().getAuthority().toString());
		}
		
		_stext.append("\n"); //$NON-NLS-1$
		_stext.setLayoutData(gd);
		_textComposite.layout();

	}

	@Override
	public final void setComposite(final Composite parentComposite)
	{
		this._parentComposite = parentComposite;
	}

	@Override
	public final void setGrayedOut(final boolean grayedout)
	{

	}

	@Override
	public final void setReference(final ReferenceMods reference)
	{
		this._reference = reference;


	}

	@Override
	public final void setSelected(final boolean seleted)
	{
		this._selected = seleted;
		if (_stext != null)
		{
			if (_selected)
			{
				_stext.setBackground(COLOR_SELECTED);
				_textComposite.setBackground(COLOR_SELECTED);
			}
			else
			{
				_stext.setBackground(COLOR_WHITE);
				_textComposite.setBackground(COLOR_WHITE);
			}
		}

	}

}
