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
package org.bbaw.pdr.ae.view.markup.stext;

import java.util.ArrayList;

import org.bbaw.pdr.ae.common.NLMessages;
import org.bbaw.pdr.ae.control.facade.Facade;
import org.bbaw.pdr.ae.model.ReferenceMods;
import org.bbaw.pdr.ae.view.control.interfaces.IReferencePresentation;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.PaintObjectEvent;
import org.eclipse.swt.custom.PaintObjectListener;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GlyphMetrics;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.IHandlerService;

/**
 * The Class ReferencePresentationSText.
 * @author Christoph Plutte
 */
public class ReferencePresentationSText implements IReferencePresentation
{

	/** The _reference. */
	private ReferenceMods _reference;

	/** The _text composite. */
	private Composite _textComposite;

	/** The _selected. */
	private boolean _selected;

	/** The _stext. */
	private StyledText _stext;

	/** The _facade. */
	private Facade _facade = Facade.getInstanz();

	/** The references selection adapter. */
	private SelectionAdapter _referencesSelectionAdapter;

	/** Resource Manager for colors and fonts. */
	private static LocalResourceManager resources = new LocalResourceManager(JFaceResources.getResources());

	/** The COLO r_ white. */
	private static final Color COLOR_WHITE = resources.createColor(new RGB(255, 255, 255));

	/** The COLO r_ selected. */
	private static final Color COLOR_SELECTED = resources.createColor(new RGB(255, 255, 200));

	/** The MARGIN. */
	private static final int MARGIN = 0;

	/** The _selection listener. */
	private ArrayList<Listener> _selectionListener = new ArrayList<Listener>();


	/** The _double click listener. */
	private ArrayList<Listener> _doubleClickListener = new ArrayList<Listener>();

	/**
	 * Adds the control.
	 * @param control the control
	 * @param offset the offset
	 * @param stext the stext
	 */
	private void addControl(final Control control, final int offset, final StyledText stext)
	{
		StyleRange style = new StyleRange();
		style.start = offset;
		style.length = 1;
		control.pack();
		Rectangle rect = control.getBounds();
		int ascent = 2 * rect.height / 3;
		int descent = rect.height - ascent;
		style.metrics = new GlyphMetrics(ascent + MARGIN, descent + MARGIN, rect.width + 2 * MARGIN);
		stext.setStyleRange(style);
	}

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
		GridData gd = new GridData();
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalAlignment = GridData.FILL;
		gd.widthHint = 200;
		// System.out.println( event + category + position);
		createAdditionalReferencesSelectionAdapter();
		final ArrayList<Control> controls = new ArrayList<Control>();
		_stext = new StyledText(_textComposite, SWT.WRAP | SWT.NO_BACKGROUND | SWT.NO_FOCUS | SWT.CURSOR_ARROW
				| SWT.BORDER);

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
				se.data = ReferencePresentationSText.this;
				for (Listener s : _doubleClickListener)
				{
					s.handleEvent(se);
				}
			}

			@Override
			public void mouseDown(final MouseEvent e)
			{
				Event se = new Event();
				se.data = ReferencePresentationSText.this;
				for (Listener s : _selectionListener)
				{
					s.handleEvent(se);
				}

			}

		});

		int lastOffset = _stext.getText().length();
		_stext.append("\n"); //$NON-NLS-1$
		_stext.append(_reference.getDisplayNameLong());
		_stext.append("\n" + "\uFFFC"); //$NON-NLS-1$ //$NON-NLS-2$
		Link rLink = new Link(_stext, SWT.PUSH);
		rLink.addSelectionListener(_referencesSelectionAdapter);
		rLink.setData(_reference.getPdrId().toString());
		rLink.setBackground(COLOR_WHITE);
		rLink.setText("<a href=\"native\">" + _reference.getPdrId().toString() + "</a>"); //$NON-NLS-1$ //$NON-NLS-2$
		controls.add(rLink);
		final int[] linkOffsets = new int[controls.size()];
		for (int i = 0; i < controls.size(); i++)
		{
			int offset = _stext.getText().indexOf("\uFFFC", lastOffset); //$NON-NLS-1$
			linkOffsets[i] = offset;
			addControl(controls.get(i), linkOffsets[i], _stext);
			lastOffset = offset + 1;
		}
		if (_reference.getRecord() != null && _reference.getRecord().getRevisions() != null
				&& _reference.getRecord().getRevisions().firstElement() != null
				&& _reference.getRecord().getRevisions().firstElement().getAuthority() != null)
		{
			_stext.append(NLMessages.getString("View_lb_user"));
			_stext.append(_facade.getObjectDisplayName(_reference.getRecord().getRevisions().firstElement()
					.getAuthority()));
		}
		// use a verify listener to keep the offsets up to date
		_stext.addVerifyListener(new VerifyListener()
		{
			@Override
			public void verifyText(final VerifyEvent e)
			{
				int start = e.start;
				int replaceCharCount = e.end - e.start;
				int newCharCount = e.text.length();
				for (int i = 0; i < linkOffsets.length; i++)
				{
					int offset = linkOffsets[i];
					if (start <= offset && offset < start + replaceCharCount)
					{
						// this widget is being deleted from the text
						if (controls.get(i) != null && !controls.get(i).isDisposed())
						{
							controls.get(i).dispose();
							controls.remove(i);
						}
						offset = -1;
					}
					if (offset != -1 && offset >= start)
					{
						offset += newCharCount - replaceCharCount;
					}
					linkOffsets[i] = offset;
				}
			}
		});

		// reposition widgets on paint event
		_stext.addPaintObjectListener(new PaintObjectListener()
		{
			@Override
			public void paintObject(final PaintObjectEvent event)
			{
				StyleRange style = event.style;
				int start = style.start;
				for (int i = 0; i < linkOffsets.length; i++)
				{
					int offset = linkOffsets[i];
					if (start == offset)
					{
						Point pt = controls.get(i).getSize();
						int x = event.x + 3;
						int y = event.y + event.ascent - 2 * pt.y / 3;
						controls.get(i).setLocation(x, y);
						break;
					}
				}
			}
		});
		_stext.append("\n"); //$NON-NLS-1$
		_stext.setLayoutData(gd);

		_stext.layout();

	}

	@Override
	public void setComposite(final Composite textComposite)
	{
		this._textComposite = textComposite;
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
			}
			else
			{
				_stext.setBackground(COLOR_WHITE);
			}
		}

	}

}
