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
import java.util.List;

import org.bbaw.pdr.ae.common.AEConstants;
import org.bbaw.pdr.ae.common.CommonActivator;
import org.bbaw.pdr.ae.common.NLMessages;
import org.bbaw.pdr.ae.control.core.PDRConfigProvider;
import org.bbaw.pdr.ae.control.facade.Facade;
import org.bbaw.pdr.ae.model.Aspect;
import org.bbaw.pdr.ae.model.Relation;
import org.bbaw.pdr.ae.model.RelationStm;
import org.bbaw.pdr.ae.model.TaggingRange;
import org.bbaw.pdr.ae.model.ValidationStm;
import org.bbaw.pdr.ae.view.control.customSWTWidges.MarkupTooltip;
import org.bbaw.pdr.ae.view.control.interfaces.IMarkupPresentation;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.IHandlerService;

/**
 * The Class MarkupPresentationWeb.
 * @author Christoph Plutte
 */
public class MarkupPresentationWeb implements IMarkupPresentation
{

	/** The _aspect. */
	private Aspect _aspect;

	/** The _text composite. */
	private Composite _textComposite;

	/** The _selected. */
	private boolean _selected;

	/** The _text canvas. */
	private Composite _textCanvas;

	/** The _facade. */
	private Facade _facade = Facade.getInstanz();

	/** Resource Manager for colors and fonts. */
	private static LocalResourceManager resources = new LocalResourceManager(JFaceResources.getResources());

	/** The COLO r_ white. */
	private static final Color COLOR_WHITE = resources.createColor(new RGB(255, 255, 255));

	/** The COLO r_ selected. */
	private static final Color COLOR_SELECTED = resources.createColor(new RGB(255, 255, 200));

	/** The aspects selection adapter. */
	private SelectionAdapter _aspectsSelectionAdapter;

	/** The _selection listener. */
	private ArrayList<Listener> _selectionListener = new ArrayList<Listener>();

	/** The _markup selection listener. */
	private ArrayList<Listener> _markupSelectionListener = new ArrayList<Listener>();

	/** The _double click listener. */
	private ArrayList<Listener> _doubleClickListener = new ArrayList<Listener>();

	/** The ASPEC t_ vie w_ id. */
	private boolean _aspectViewID = Platform.getPreferencesService().getBoolean(CommonActivator.PLUGIN_ID,
			"ASPECT_VIEW_ID", AEConstants.ASPECT_VIEW_ID, null);

	/** The ASPEC t_ vie w_ userid. */
	private boolean _aspectViewUserID = Platform.getPreferencesService().getBoolean(CommonActivator.PLUGIN_ID,
			"ASPECT_VIEW_USERID", false, null);

	/** The ASPEC t_ vie w_ relations. */
	private boolean _aspectViewRelations = Platform.getPreferencesService().getBoolean(CommonActivator.PLUGIN_ID,
			"ASPECT_VIEW_RELATIONS", AEConstants.ASPECT_VIEW_RELATIONS, null);

	/** The ASPEC t_ vie w_ reference. */
	private boolean _aspectViewReference = Platform.getPreferencesService().getBoolean(CommonActivator.PLUGIN_ID,
			"ASPECT_VIEW_REFERENCE", AEConstants.ASPECT_VIEW_REFERENCE, null);

	private Composite _mainComposite;

	private ArrayList<Composite> _lineComps;
	/** The Constant ASPECT_COLOR_PERSNAME. */
	private static final String ASPECT_COLOR_PERSNAME = Platform.getPreferencesService().getString(
			CommonActivator.PLUGIN_ID, "ASPECT_COLOR_PERSNAME", AEConstants.ASPECT_COLOR_PERSNAME, null);

	/** The Constant ASPECT_COLOR_ORGNAME. */
	private static final String ASPECT_COLOR_ORGNAME = Platform.getPreferencesService().getString(
			CommonActivator.PLUGIN_ID, "ASPECT_COLOR_ORGNAME", AEConstants.ASPECT_COLOR_ORGNAME, null);

	/** The Constant ASPECT_COLOR_PLACENAME. */
	private static final String ASPECT_COLOR_PLACENAME = Platform.getPreferencesService().getString(
			CommonActivator.PLUGIN_ID, "ASPECT_COLOR_PLACENAME", AEConstants.ASPECT_COLOR_PLACENAME, null);

	/** The Constant ASPECT_COLOR_DATE. */
	private static final String ASPECT_COLOR_DATE = Platform.getPreferencesService().getString(
			CommonActivator.PLUGIN_ID, "ASPECT_COLOR_DATE", AEConstants.ASPECT_COLOR_DATE, null);

	/** The Constant ASPECT_COLOR_NAME. */
	private static final String ASPECT_COLOR_NAME = Platform.getPreferencesService().getString(
			CommonActivator.PLUGIN_ID, "ASPECT_COLOR_NAME", AEConstants.ASPECT_COLOR_NAME, null);
	/** Resource Manager for colors and fonts. */

	private static Color colorPers = resources.createColor(new RGB(new Integer(ASPECT_COLOR_PERSNAME.split(",")[0]),
			new Integer(ASPECT_COLOR_PERSNAME.split(",")[1]), new Integer(ASPECT_COLOR_PERSNAME.split(",")[2])));

	/** The color org. */
	private static Color colorOrg = resources.createColor(new RGB(new Integer(ASPECT_COLOR_ORGNAME.split(",")[0]),
			new Integer(ASPECT_COLOR_ORGNAME.split(",")[1]), new Integer(ASPECT_COLOR_ORGNAME.split(",")[2])));

	/** The color place. */
	private static Color colorPlace = resources.createColor(new RGB(new Integer(ASPECT_COLOR_PLACENAME.split(",")[0]),
			new Integer(ASPECT_COLOR_PLACENAME.split(",")[1]), new Integer(ASPECT_COLOR_PLACENAME.split(",")[2])));

	/** The color date. */
	private static Color colorDate = resources.createColor(new RGB(new Integer(ASPECT_COLOR_DATE.split(",")[0]),
			new Integer(ASPECT_COLOR_DATE.split(",")[1]), new Integer(ASPECT_COLOR_DATE.split(",")[2])));

	/** The color name. */
	private static Color colorName = resources.createColor(new RGB(new Integer(ASPECT_COLOR_NAME.split(",")[0]),
			new Integer(ASPECT_COLOR_NAME.split(",")[1]), new Integer(ASPECT_COLOR_NAME.split(",")[2])));

	@Override
	public final void addDoubleClickListener(final Listener listener)
	{
		if (listener != null)
		{
			_doubleClickListener.add(listener);
		}

	}

	@Override
	public final void addMarkupSelectionListener(final Listener listener)
	{
		if (listener != null)
		{
			_markupSelectionListener.add(listener);
		}

	}

	@Override
	public final void addSelectionListener(final Listener listener)
	{
		if (listener != null)
		{
			_selectionListener.add(listener);
		}

	}

	/**
	 * Choose color.
	 * @param name the name
	 * @return the color
	 */
	private Color chooseColor(final String name)
	{
		if (name.equals("persName")) //$NON-NLS-1$
		{
			return colorPers;
		}
		else if (name.equals("orgName")) //$NON-NLS-1$
		{
			return colorOrg;
		}
		else if (name.equals("placeName")) //$NON-NLS-1$
		{
			return colorPlace;
		}
		else if (name.equals("name")) //$NON-NLS-1$
		{
			return colorName;
		}
		else
		{
			return colorDate;
		}

	}

	/**
	 * Creates the additional aspects selection adapter.
	 */
	private void createAdditionalAspectsSelectionAdapter()
	{
		_aspectsSelectionAdapter = new SelectionAdapter()
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
					handlerService
							.executeCommand("org.bbaw.pdr.ae.view.main.commands.OpenAdditionalAspectsView", event); //$NON-NLS-1$
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

		loadAspect();
	}

	@Override
	public final Aspect getAspect()
	{
		return _aspect;
	}

	// void addControl(Control control, int offset, StyledText stext) {
	// StyleRange style = new StyleRange();
	// style.start = offset;
	// style.length = 2;
	// control.pack();
	// Rectangle rect = control.getBounds();
	// int ascent = 2 * rect.height / 3;
	// int descent = rect.height - ascent;
	// style.metrics = new GlyphMetrics(ascent + MARGIN, descent + MARGIN,
	// rect.width + 2 * MARGIN);
	// stext.setStyleRange(style);
	// }

	@Override
	public final Control getControl()
	{
		return _textCanvas;
	}

	/**
	 * Load aspect.
	 */
	private void loadAspect()
	{
		GridData gd = new GridData();
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalAlignment = GridData.FILL;
		gd.widthHint = 200;
		_mainComposite = new Composite(_textComposite, SWT.CURSOR_ARROW);
		_mainComposite.setSize(_textComposite.getSize());
		ControlDecoration stextDeco = new ControlDecoration(_textComposite, SWT.LEFT | SWT.TOP);

		_mainComposite.setEnabled(true);
		//    	stext.setData("category", categorieID); //$NON-NLS-1$
		_mainComposite.setData("key", _aspect.getPdrId().toString()); //$NON-NLS-1$
		_mainComposite.setData(NLMessages.getString("View_37"), _aspect.getPdrId().toString()); //$NON-NLS-1$
		_mainComposite.setData("textOffset", 0); //$NON-NLS-1$
		_mainComposite.setBackground(COLOR_WHITE);
		_mainComposite.setLayoutData(gd);
		_mainComposite.setLayout(new GridLayout(1, false));

		_textCanvas = new Composite(_mainComposite, SWT.CURSOR_ARROW);
		_textCanvas.setEnabled(true);
		//    	stext.setData("category", categorieID); //$NON-NLS-1$
		_textCanvas.setData("key", _aspect.getPdrId().toString()); //$NON-NLS-1$
		_textCanvas.setData(NLMessages.getString("View_37"), _aspect.getPdrId().toString()); //$NON-NLS-1$
		_textCanvas.setData("textOffset", 0); //$NON-NLS-1$
		_textCanvas.setBackground(COLOR_WHITE);
		_textCanvas.setLayoutData(gd);
		// IStatus slaCaret = new Status(IStatus.INFO,Activator.PLUGIN_ID,
		//		"CategoryView load aspect - stext caret offset: " + _text.getCaretOffset()); //$NON-NLS-1$
		// _iLogger.log(slaCaret);

		final MarkupTooltip markupTooltipLabel = new MarkupTooltip(_textComposite);
		markupTooltipLabel.setShift(new Point(0, 10));
		markupTooltipLabel.setPopupDelay(0);
		markupTooltipLabel.setHideOnMouseDown(false);
		markupTooltipLabel.deactivate();

		createAdditionalAspectsSelectionAdapter();

		MouseListener mouseListener = new MouseListener()
		{

			@Override
			public void mouseDoubleClick(final MouseEvent se)
			{
				System.out.println("mouseDoubleClick");

				IHandlerService handlerService = (IHandlerService) PlatformUI.getWorkbench().getService(
						IHandlerService.class);
				try
				{
					handlerService.executeCommand("org.bbaw.pdr.ae.view.main.commands.CallAspectEditor", null); //$NON-NLS-1$
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
				// if(!_textCanvas.isDisposed()) _textCanvas.setSelection(0, 0);

			}

			@Override
			public void mouseDown(final MouseEvent e)
			{
				System.out.println("mouse down");

				Event se = new Event();
				se.data = MarkupPresentationWeb.this;
				for (Listener s : _selectionListener)
				{
					s.handleEvent(se);
				}



			}

			@Override
			public void mouseUp(final MouseEvent e)
			{

			}
		};
		_textComposite.addMouseListener(mouseListener);
		_mainComposite.addMouseListener(mouseListener);
		_textCanvas.addMouseListener(mouseListener);

		writeAspect2Text(_aspect, _textCanvas, stextDeco);
		_textCanvas.setLayoutData(gd);

		// stext.setBounds(0,0,100,100);
		// _text.layout();
	}

	@Override
	public final void setAspect(final Aspect aspect)
	{
		this._aspect = aspect;

	}

	/**
	 * Sets the color.
	 * @param charText the char text
	 * @param tr the tr
	 */
	private void setColor(final Text charText, final TaggingRange tr)
	{
		charText.setBackground(chooseColor(tr.getName()));

	}

	@Override
	public final void setComposite(final Composite textComposite)
	{
		this._textComposite = textComposite;
	}

	@Override
	public final void setGrayedOut(final boolean grayedout)
	{

	}

	@Override
	public final void setSelected(final boolean seleted)
	{
		this._selected = seleted;
		if (_textComposite != null && _textCanvas != null)
		{
			if (_selected)
			{
				_textComposite.setBackground(COLOR_SELECTED);
				_mainComposite.setBackground(COLOR_SELECTED);
				_textCanvas.setBackground(COLOR_SELECTED);
				for (Composite c :_lineComps)
				{
					c.setBackground(COLOR_SELECTED);
				}
			}
			else
			{
				_textComposite.setBackground(COLOR_WHITE);
				_mainComposite.setBackground(COLOR_WHITE);
				_textCanvas.setBackground(COLOR_WHITE);
				for (Composite c :_lineComps)
				{
					c.setBackground(COLOR_WHITE);
				}
			}
		}
	}

	/**
	 * meth. writes styledText to text with styleRange, colored background and
	 * info for information message.
	 * @param aspect aspect to be processed
	 * @param textCanvas composite to present aspect
	 * @param stextDeco composite decoration
	 */
	private void writeAspect2Text(final Aspect aspect, final Composite textCanvas, final ControlDecoration stextDeco)
	{
		System.out.println("write aspect2text");
		
		textCanvas.setLayout(new GridLayout(1, true));
		((GridLayout) textCanvas.getLayout()).verticalSpacing = 0;
		((GridLayout) textCanvas.getLayout()).marginHeight = 0;

		Composite lineComposite = new Composite(textCanvas, SWT.NONE);
		RowLayout rLayout = new RowLayout(SWT.HORIZONTAL);
		rLayout.marginLeft = 0;
		rLayout.marginRight = 0;
		rLayout.spacing = 0;
		rLayout.wrap = false;
		lineComposite.setLayout(rLayout);
		lineComposite.setBackground(COLOR_WHITE);
		_lineComps = new ArrayList<Composite>();
		_lineComps.add(lineComposite);
		String tempText = aspect.getNotification();
		List<TaggingRange> rangeList = aspect.getRangeList();
		Text charText;
		final ArrayList<Control> controls = new ArrayList<Control>();
		int start = 0;
		if (rangeList != null && rangeList.size() > 0)
		{
			for (final TaggingRange tr : rangeList)
			{
				if (tr.getStart() > start)
				{
					if (tempText.substring(start, tr.getStart()).contains("\n"))
					{
						String s = tempText.substring(start, tr.getStart());
						String[] strs = s.split("\n");
						for (int i = 0; i < strs.length; i++)
						{
							String ss = strs[i];
							charText = new Text(lineComposite, SWT.WRAP);
							charText.setText(ss);
							charText.setEditable(false);
							charText.setBackground(COLOR_WHITE);

							if (i < strs.length - 1)
							{
								lineComposite = new Composite(textCanvas, SWT.NONE);
								lineComposite.setLayout(rLayout);
								lineComposite.setBackground(COLOR_WHITE);
								_lineComps.add(lineComposite);
							}
						}

					}
					else
					{
						charText = new Text(lineComposite, SWT.WRAP);
						String s = tempText.substring(start, tr.getStart());
						charText.setText(s);
						charText.setEditable(false);
						charText.setBackground(COLOR_WHITE);
						charText.addMouseListener(new MouseListener()
	
						{
	
							@Override
							public void mouseDoubleClick(final MouseEvent e)
							{
	
							}
	
							@Override
							public void mouseDown(final MouseEvent e)
							{
								Event se = new Event();
								se.data = null;
								se.x = e.x;
								se.y = e.y;
								for (Listener s : _markupSelectionListener)
								{
									s.handleEvent(se);
								}
	
							}
	
							@Override
							public void mouseUp(final MouseEvent e)
							{
	
							}
						});
					}
				}
				charText = new Text(lineComposite, SWT.WRAP);
				if (tempText.length() < tr.getStart() + tr.getLength())
				{
					charText.setText(tempText.substring(tr.getStart(), tempText.length()));
				}
				else
				{
					charText.setText(tempText.substring(tr.getStart(), tr.getStart() + tr.getLength()));
				}
				charText.setEditable(false);
				setColor(charText, tr);
				charText.addMouseListener(new MouseListener()
				{

					@Override
					public void mouseDoubleClick(final MouseEvent e)
					{
						System.out.println("mouseDoubleClick tr");

					}

					@Override
					public void mouseDown(final MouseEvent e)
					{
						System.out.println("mouseDown tr");
						Event se = new Event();
						se.data = tr;
						se.x = e.x;
						se.y = e.y;
						for (Listener s : _markupSelectionListener)
						{
							s.handleEvent(se);
						}

					}

					@Override
					public void mouseUp(final MouseEvent e)
					{

					}
				});
				start = tr.getStart() + tr.getLength();
			}
			if (start < tempText.length())
			{
				String s = tempText.substring(start, tempText.length());

				if (s.contains("\n"))
				{
					String[] strs = s.split("\n");
					for (String ss : strs)
					{
						charText = new Text(lineComposite, SWT.WRAP);
						charText.setText(ss);
						charText.setEditable(false);
						charText.setBackground(COLOR_WHITE);

						lineComposite = new Composite(textCanvas, SWT.NONE);
						lineComposite.setLayout(rLayout);
						
						lineComposite.setBackground(COLOR_WHITE);
						_lineComps.add(lineComposite);
					}

				}
				else
				{
					charText = new Text(lineComposite, SWT.WRAP);
					charText.setText(s);
					charText.setEditable(false);
					charText.setBackground(COLOR_WHITE);

				}
			}
		}
		else
		{
			charText = new Text(lineComposite, SWT.WRAP);
			charText.setBackground(COLOR_WHITE);

			charText.setText(tempText);
			charText.setEditable(false);
		}

		if (_aspectViewID)
		{
			lineComposite = new Composite(textCanvas, SWT.NONE);
			lineComposite.setLayout(rLayout);
			
			lineComposite.setBackground(COLOR_WHITE);
			_lineComps.add(lineComposite);
			charText = new Text(lineComposite, SWT.WRAP);
			charText.setText(NLMessages.getString("CategoryView_id") + aspect.getPdrId().toString());
			charText.setBackground(COLOR_WHITE);

			charText.setEditable(false);
		}
		if (_aspectViewUserID)
		{
			lineComposite = new Composite(textCanvas, SWT.NONE);
			lineComposite.setLayout(rLayout);
			lineComposite.setBackground(COLOR_WHITE);
			charText = new Text(lineComposite, SWT.WRAP);
			charText.setText(NLMessages.getString("View_User")
					+ _facade.getObjectDisplayName(aspect.getRecord().getRevisions().firstElement().getAuthority()));
			charText.setBackground(COLOR_WHITE);

			charText.setEditable(false);

		}
		if (_aspectViewRelations)
		{
			if (aspect.getRelationDim().getRelationStms().size() > 1)
			{
				lineComposite = new Composite(textCanvas, SWT.NONE);
				lineComposite.setLayout(rLayout);
				lineComposite.setBackground(COLOR_WHITE);
				charText = new Text(lineComposite, SWT.WRAP);
				charText.setText(NLMessages.getString("View_other_relations_dot"));
				charText.setBackground(COLOR_WHITE);
				charText.setEditable(false);
			}
			for (RelationStm rStm : aspect.getRelationDim().getRelationStms())
			{
				if (rStm.getSubject().equals(aspect.getPdrId()))
				{
					if (_facade.getCurrentPerson() != null
							&& rStm.getRelations() != null
							&& rStm.getRelations().firstElement() != null
							&& !rStm.getRelations().firstElement().getObject()
									.equals(_facade.getCurrentPerson().getPdrId()))
					{
						lineComposite = new Composite(textCanvas, SWT.NONE);
						lineComposite.setLayout(rLayout);
						lineComposite.setBackground(COLOR_WHITE);
						charText = new Text(lineComposite, SWT.WRAP);
						charText.setText(NLMessages.getString("View_aspect_of") + " ");
						charText.setBackground(COLOR_WHITE);
						charText.setEditable(false);
						String name = _facade.getObjectDisplayName(rStm.getRelations().firstElement().getObject());

						if (_facade.getPdrObject(rStm.getRelations().firstElement().getObject()) != null)
						{
							Link rLink = new Link(lineComposite, SWT.PUSH);
							rLink.addSelectionListener(_aspectsSelectionAdapter);
							rLink.setData(rStm.getRelations().firstElement().getObject().toString());
							rLink.setBackground(COLOR_WHITE);
							rLink.setText("<a href=\"native\">" + name + "</a>"); //$NON-NLS-1$ //$NON-NLS-2$
							controls.add(rLink);
						}
						else
						{
							charText = new Text(lineComposite, SWT.WRAP);
							charText.setText(NLMessages.getString("View_object_dot_delete") + " " + name + " "
									+ NLMessages.getString("View_missing_object"));
							charText.setBackground(COLOR_WHITE);
							charText.setEditable(false);
						}
					}

				}
				else
				{
					lineComposite = new Composite(textCanvas, SWT.NONE);
					lineComposite.setLayout(rLayout);
					lineComposite.setBackground(COLOR_WHITE);
					charText = new Text(lineComposite, SWT.WRAP);
					charText.setText(NLMessages.getString("View_relation_subject"));
					charText.setBackground(COLOR_WHITE);
					charText.setEditable(false);
					String name = _facade.getObjectDisplayName(rStm.getSubject());

					if (_facade.getPdrObject(rStm.getSubject()) != null)
					{

						Link rLink = new Link(lineComposite, SWT.PUSH);
						// pButton.setImage(imageReg.get(IconsInternal.REFERENCE));
						rLink.addSelectionListener(_aspectsSelectionAdapter);
						rLink.setData(rStm.getSubject().toString());
						rLink.setBackground(COLOR_WHITE);
						rLink.setText("<a href=\"native\">" + name + "</a>"); //$NON-NLS-1$ //$NON-NLS-2$
						controls.add(rLink);

					}
					else
					{
						charText = new Text(lineComposite, SWT.WRAP);
						charText.setText(name + NLMessages.getString("View_missing_object"));
						charText.setBackground(COLOR_WHITE);
						charText.setEditable(false);
					}

					for (Relation r : rStm.getRelations())
					{
						lineComposite = new Composite(textCanvas, SWT.NONE);
						lineComposite.setLayout(rLayout);
						lineComposite.setBackground(COLOR_WHITE);
						if (r.getContext() != null)
						{
							charText = new Text(lineComposite, SWT.WRAP);
							charText.setText(NLMessages.getString("View_relation_context_dot") + " "
									+ PDRConfigProvider.getLabelOfRelation(r.getProvider(), r.getContext(), null, null)
									+ "   ");
							charText.setBackground(COLOR_WHITE);
							charText.setEditable(false);

						}
						if (r.getRClass() != null)
						{
							charText = new Text(lineComposite, SWT.WRAP);
							charText.setText(NLMessages.getString("View_relation_class_dot")
									+ PDRConfigProvider.getLabelOfRelation(r.getProvider(), r.getContext(),
											r.getRClass(), null) + "   ");
							charText.setBackground(COLOR_WHITE);
							charText.setEditable(false);

						}
						if (r.getRelation() != null)
						{
							charText = new Text(lineComposite, SWT.WRAP);
							charText.setText(NLMessages.getString("View_relation_value_dot")
									+ PDRConfigProvider.getLabelOfRelation(r.getProvider(), r.getContext(),
											r.getRClass(), r.getRelation()));
							charText.setBackground(COLOR_WHITE);
							charText.setEditable(false);

						}
						lineComposite = new Composite(textCanvas, SWT.NONE);
						lineComposite.setLayout(rLayout);
						lineComposite.setBackground(COLOR_WHITE);
						charText = new Text(lineComposite, SWT.WRAP);
						charText.setText(NLMessages.getString("View_object_dot_delete"));
						charText.setBackground(COLOR_WHITE);
						charText.setEditable(false);
						String objectName = _facade.getObjectDisplayName(r.getObject());
						if (r.getObject() != null)
						{
							Link rLink = new Link(lineComposite, SWT.PUSH);
							// pButton.setImage(imageReg.get(IconsInternal.REFERENCE));
							rLink.addSelectionListener(_aspectsSelectionAdapter);
							rLink.setData(r.getObject().toString());
							rLink.setBackground(COLOR_WHITE);
							rLink.setText("<a href=\"native\">" + objectName + "</a>"); //$NON-NLS-1$ //$NON-NLS-2$
							controls.add(rLink);
						}
						else
						{
							charText = new Text(lineComposite, SWT.WRAP);
							charText.setText(" " + objectName + NLMessages.getString("View_missing_object"));
							charText.setBackground(COLOR_WHITE);
							charText.setEditable(false);
						}

					}
				}

			}
		}
		if (_aspectViewReference)
		{
			if (aspect.getValidation() != null && aspect.getValidation().getValidationStms() != null)
			{
				for (ValidationStm vs : aspect.getValidation().getValidationStms())
				{
					if (vs.getReference() != null && vs.getReference().getSourceId() != null)
					{
						lineComposite = new Composite(textCanvas, SWT.NONE);
						lineComposite.setLayout(rLayout);
						lineComposite.setBackground(COLOR_WHITE);
						charText = new Text(lineComposite, SWT.WRAP);
						charText.setText(NLMessages.getString("View_lbReference"));
						charText.setBackground(COLOR_WHITE);
						charText.setEditable(false);
						if (_facade.getReference(vs.getReference().getSourceId()) != null)
						{
							String name = _facade.getReference(vs.getReference().getSourceId()).getDisplayNameLong(); //$NON-NLS-1$ //$NON-NLS-2$
							Link rLink = new Link(lineComposite, SWT.PUSH);
							rLink.addSelectionListener(_aspectsSelectionAdapter);
							rLink.setData(vs.getReference().getSourceId().toString());
							rLink.setBackground(COLOR_WHITE);
							rLink.setText("<a href=\"native\">" + name + "</a>"); //$NON-NLS-1$ //$NON-NLS-2$
							controls.add(rLink);
						}
						else
						{
							charText = new Text(lineComposite, SWT.WRAP);
							charText.setText(" " + NLMessages.getString("View_missing_reference") + " ("
									+ vs.getReference().getSourceId().toString() + ")");
							charText.setBackground(COLOR_WHITE);
							charText.setEditable(false);
						}
						if (vs.getReference().getInternal() != null)
						{
							lineComposite = new Composite(textCanvas, SWT.NONE);
							lineComposite.setLayout(rLayout);
							lineComposite.setBackground(COLOR_WHITE);
							charText = new Text(lineComposite, SWT.WRAP);
							charText.setText(" " + NLMessages.getString("Editor_internal") + ": "
									+ vs.getReference().getInternal());
							charText.setBackground(COLOR_WHITE);
							charText.setEditable(false);
						}
					}

				}
			}
		}
		textCanvas.layout();
		_mainComposite.layout();

	}

	@Override
	public void setBackground(Color greenColor) {
		if (_textComposite != null && _textCanvas != null)
		{
			_textComposite.setBackground(greenColor);
			_mainComposite.setBackground(greenColor);
			_textCanvas.setBackground(greenColor);
			for (Composite c :_lineComps)
			{
				c.setBackground(greenColor);
			}
		}
		
	}
}
