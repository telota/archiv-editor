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
import java.util.LinkedList;

import org.bbaw.pdr.ae.common.AEConstants;
import org.bbaw.pdr.ae.common.AEVIEWConstants;
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
import org.bbaw.pdr.ae.view.control.swtnotrwthelper.TaggingRangeTransformer;
import org.bbaw.pdr.ae.view.markup.stext.internal.Activator;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.PaintObjectEvent;
import org.eclipse.swt.custom.PaintObjectListener;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
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
 * The Class MarkupPresentationStext.
 * @author Christoph Plutte
 */
public class MarkupPresentationStext implements IMarkupPresentation
{

	/** The _aspect. */
	private Aspect _aspect;

	/** The _text composite. */
	private Composite _textComposite;

	/** The _selected. */
	private boolean _selected;

	/** The _grayed out. */
	@SuppressWarnings("unused")
	private boolean _grayedOut;

	/** The _i logger. */
	private ILog _iLogger = AEConstants.ILOGGER;

	/** The _stext. */
	private StyledText _stext;

	/** The _facade. */
	private Facade _facade = Facade.getInstanz();

	/** The _tr transformer. */
	private TaggingRangeTransformer _trTransformer = new TaggingRangeTransformer();
	/** Resource Manager for colors and fonts. */
	private static LocalResourceManager resources = new LocalResourceManager(JFaceResources.getResources());

	/** The COLO r_ white. */
	private static final Color COLOR_WHITE = resources.createColor(new RGB(255, 255, 255));

	/** The COLO r_ selected. */
	private static final Color COLOR_SELECTED = resources.createColor(new RGB(255, 255, 200));

	/** The MARGIN. */
	private static final int MARGIN = 0;

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

	/**
	 * Adds the control.
	 * @param control the control
	 * @param offset the offset
	 * @param stext the stext
	 */
	final void addControl(final Control control, final int offset, final StyledText stext)
	{
		StyleRange style = new StyleRange();
		style.start = offset;
		style.length = 2;
		control.pack();
		Rectangle rect = control.getBounds();
		int ascent = 2 * rect.height / 3;
		int descent = rect.height - ascent;
		style.metrics = new GlyphMetrics(ascent + MARGIN, descent + MARGIN, rect.width + 2 * MARGIN);
		stext.setStyleRange(style);
	}

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

	@Override
	public final Control getControl()
	{
		return _stext;
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
		_stext = new StyledText(_textComposite, SWT.WRAP | SWT.NO_BACKGROUND | SWT.NO_FOCUS | SWT.CURSOR_ARROW);

		ControlDecoration stextDeco = new ControlDecoration(_stext, SWT.LEFT | SWT.TOP);

		_stext.setEditable(false);
		//    	stext.setData("category", categorieID); //$NON-NLS-1$
		_stext.setData("key", _aspect.getPdrId().toString()); //$NON-NLS-1$
		_stext.setData(NLMessages.getString("View_37"), _aspect.getPdrId().toString()); //$NON-NLS-1$
		_stext.setData("textOffset", 0); //$NON-NLS-1$

		IStatus slaCaret = new Status(IStatus.INFO, Activator.PLUGIN_ID,
				"CategoryView load aspect - stext caret offset: " + _stext.getCaretOffset()); //$NON-NLS-1$
		_iLogger.log(slaCaret);

		final MarkupTooltip markupTooltipLabel = new MarkupTooltip(_stext);
		markupTooltipLabel.setShift(new Point(0, 10));
		markupTooltipLabel.setPopupDelay(0);
		markupTooltipLabel.setHideOnMouseDown(false);
		markupTooltipLabel.deactivate();

		createAdditionalAspectsSelectionAdapter();

		_stext.addMouseMoveListener(new MouseMoveListener()
		{

			@Override
			public void mouseMove(final MouseEvent e)
			{
				// Status sca = new Status(IStatus.INFO,Activator.PLUGIN_ID,
				//				"CategoryView current aspect: " + _facade.getCurrentAspect().getPdrId().toString()); //$NON-NLS-1$
				// _iLogger.log(sca);
				// System.out.println("x " + e.x + " y " + e.y);
				Point p = new Point(e.x, e.y);
				if (p != null)
				{
					try
					{
						int offset = _stext.getOffsetAtLocation(p);
						// System.out.println("offset " + offset);
						// System.out.println(" offset " + offset + " zwischen 0
						// und
						//						der stext.länge " + stext.getText().length()); //$NON-NLS-1$ //$NON-NLS-2$
						if (offset >= 1 && offset <= _stext.getText().length())
						{
							int index = (Integer) _stext.getData("textOffset"); //$NON-NLS-1$
							// FIXME hier ist der
							if (_stext.getStyleRangeAtOffset(offset) != null
									|| _stext.getStyleRangeAtOffset(offset - 1) != null)
							{

								markupTooltipLabel.activate();

								//    					System.out.println(" es gibt eine SR bei " + modifiedOffset); //$NON-NLS-1$
								IStatus sindex = new Status(IStatus.INFO, Activator.PLUGIN_ID,
										"MarkupPresentationView index: " + index); //$NON-NLS-1$
								_iLogger.log(sindex);
								StyleRange sr;
								boolean hit = false;
								@SuppressWarnings("unchecked")
								LinkedList<TaggingRange> rangeList = (LinkedList<TaggingRange>) _stext
										.getData("rangeList"); //$NON-NLS-1$
								for (TaggingRange tr : rangeList)
								{
									sr = _trTransformer.transformStyleRange(tr);

									// System.out.println("TR "
									// + tr.getName() + " index " + index +
									// " sr.start " + sr.start + " length "
									//									+ sr.length); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
									//		    						System.out.println("offset " + offset); //$NON-NLS-1$

									IStatus ssr2 = new Status(IStatus.INFO, Activator.PLUGIN_ID,
											"MarkupPresentationView StyleRange: " + sr); //$NON-NLS-1$
									_iLogger.log(ssr2);

									int modifiedStart = sr.start + index;
									int modifiedEnd = sr.start + index + sr.length;

									IStatus sms = new Status(IStatus.INFO, Activator.PLUGIN_ID,
											"MarkupPresentationView modifiedStart " + modifiedStart //$NON-NLS-1$
													+ " length: " + sr.length + " modifiedEnd: " + modifiedEnd); //$NON-NLS-1$ //$NON-NLS-2$
									_iLogger.log(sms);

									if (modifiedStart <= offset && offset <= modifiedEnd)
									{
										hit = true;
										Event se = new Event();
										se.data = tr;
										se.x = e.x;
										se.y = e.y;
										for (Listener s : _markupSelectionListener)
										{
											s.handleEvent(se);
										}
										//
									}
								}
								if (!hit)
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

							}
						}
						else
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
					}

					catch (IllegalArgumentException ex)
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

				}

			}
		});

		_stext.addMouseListener(new MouseListener()
		{
			@Override
			public void mouseDoubleClick(final MouseEvent me)
			{
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
				if (!_stext.isDisposed())
				{
					_stext.setSelection(0, 0);
				}

			}

			@Override
			public void mouseDown(final MouseEvent e)
			{
				Event se = new Event();
				se.data = MarkupPresentationStext.this;
				for (Listener s : _selectionListener)
				{
					s.handleEvent(se);
				}

				// StyledText current = (StyledText) e.widget;
				//            	_facade.setCurrentAspect((String) current.getData("id")); //$NON-NLS-1$
				//				StyledText last = (StyledText) tabFolderRight.getData("lastSelected"); //$NON-NLS-1$
				// (current).setBackground(COLOR_SELECTED);
				// current.getParent().setBackground(COLOR_SELECTED);
				//
				// IStatus sca = new Status(IStatus.INFO,Activator.PLUGIN_ID,
				//				"CategoryView current aspect: " + _facade.getCurrentAspect().getPdrId().toString()); //$NON-NLS-1$
				// _iLogger.log(sca);
				// if (last != null && !last.equals(current) &&
				// !last.isDisposed()) {
				// last.setBackground(COLOR_WHITE);
				// last.getParent().setBackground(COLOR_WHITE);
				// }
				//            	tabFolderRight.setData("lastSelected", current); //$NON-NLS-1$
				//
				// if (e.button > 1)
				// {
				// try {
				// int offset = stext.getOffsetAtLocation(new Point(e.x, e.y));
				// // System.out.println(offset);
				// if (offset > 0 && offset < stext.getText().length()) {
				// if (stext.getStyleRangeAtOffset(offset) != null) {
				//            					int index = (Integer)stext.getData("textOffset"); //$NON-NLS-1$
				// // StyleRange sr;
				// // @SuppressWarnings("unchecked")
				// // LinkedList<TaggingRange> rangeList =
				// (LinkedList<TaggingRange>)
				//				stext.getData("rangeList"); //$NON-NLS-1$
				// // for (TaggingRange tr : rangeList) {
				// // sr = tr.getStyleRange();
				// // int modifiedStart = sr.start + index;
				// // if (modifiedStart <= offset && (modifiedStart+sr.length)
				// >=offset) {
				// //
				// //// System.out.println("Typ: " +tr.getName()
				//				+"\nWert: "+tr.getType()); //$NON-NLS-1$ //$NON-NLS-2$
				// //// mb.open();
				// // break;
				// // }
				// // }
				// }
				// }
				// } catch (IllegalArgumentException ex) {
				// // ignorieren
				// }
				// }

			}

			@Override
			public void mouseUp(final MouseEvent e)
			{
				// TODO Auto-generated method stub

			}
		});

		writeAspect2Text(_aspect, _stext, stextDeco);
		_stext.setLayoutData(gd);

		// stext.setBounds(0,0,100,100);
		// _stext.layout();
	}

	@Override
	public final void setAspect(final Aspect aspect)
	{
		this._aspect = aspect;

	}

	@Override
	public final void setComposite(final Composite textComposite)
	{
		this._textComposite = textComposite;
	}

	@Override
	public final void setGrayedOut(final boolean grayedout)
	{
		this._grayedOut = grayedout;

	}

	@Override
	public final void setSelected(final boolean seleted)
	{
		this._selected = seleted;
		if (_stext != null && !_stext.isDisposed())
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

	/**
	 * meth. writes styledText to text with styleRange, colored background and
	 * info for information message.
	 * @param aspect aspect to be processed
	 * @param stext styled text to present the aspect
	 * @param stextDeco controldecoration of styled text
	 */
	private void writeAspect2Text(final Aspect aspect, final StyledText stext, final ControlDecoration stextDeco)
	{
		String aContent = aspect.getNotification();
		String tempText = aContent;
		boolean valid = true;
		final ArrayList<Control> controls = new ArrayList<Control>();

		if (tempText != null)
		{
			stext.append("\n"); //$NON-NLS-1$
			int index = stext.getText().length();
			stext.append(tempText); //$NON-NLS-1$

			/* TaggingRanges */
			LinkedList<TaggingRange> rangeList = aspect.getRangeList();
			StyleRange sr;
			if (rangeList != null)
			{
				for (TaggingRange tr : rangeList)
				{
					sr = (StyleRange) _trTransformer.transformStyleRange(tr).clone();
					sr.start = sr.start + index;

					if (sr.start <= stext.getText().length() && stext.getText().length() >= (sr.start + sr.length))
					{
						stext.setStyleRange(sr);
					}
				}
			}
			stext.setData("rangeList", rangeList); //$NON-NLS-1$
			stext.setData("textOffset", index); //$NON-NLS-1$
			IStatus soffset = new Status(IStatus.INFO, Activator.PLUGIN_ID,
					"MarkupPresentationView offset: " + stext.getData("textOffset")); //$NON-NLS-1$ //$NON-NLS-2$
			_iLogger.log(soffset);
		}
		stext.append("\n"); //$NON-NLS-1$
		if (_aspectViewID)
		{
			int start = stext.getText().length();
			stext.append(" \n" + NLMessages.getString("CategoryView_id") + aspect.getPdrId().toString()); //$NON-NLS-1$ //$NON-NLS-2$
			stext.setStyleRange(new StyleRange(start, stext.getText().length() - start,
					AEVIEWConstants.VIEW_FOREGROUND_DESELECTED_COLOR, null));
		}
		if (_aspectViewUserID)
		{
			int start = stext.getText().length();
			stext.append("\n" + NLMessages.getString("View_User")
					+ _facade.getObjectDisplayName(aspect.getRecord().getRevisions().firstElement().getAuthority())); //$NON-NLS-1$ //$NON-NLS-2$
			stext.setStyleRange(new StyleRange(start, stext.getText().length() - start,
					AEVIEWConstants.VIEW_FOREGROUND_DESELECTED_COLOR, null));
		}
		int lastOffset = stext.getText().length();

		if (_aspectViewRelations)
		{
			if (aspect.getRelationDim().getRelationStms().size() > 1)
			{
				int start = stext.getText().length();
				stext.append("\n\n" + NLMessages.getString("View_other_relations_dot")); //$NON-NLS-1$
				stext.setStyleRange(new StyleRange(start, stext.getText().length() - start,
						AEVIEWConstants.VIEW_FOREGROUND_DESELECTED_COLOR, null));
			}
			for (RelationStm rStm : aspect.getRelationDim().getRelationStms())
			{
				if (rStm.getSubject().equals(aspect.getPdrId()))
				{
					if (_facade.getCurrentPerson() != null
							&& rStm.getRelations() != null
							&& rStm.getRelations().firstElement() != null
							&& rStm.getRelations().firstElement().getObject() != null
							&& !rStm.getRelations().firstElement().getObject()
									.equals(_facade.getCurrentPerson().getPdrId()))
					{
						int start = stext.getText().length();
						stext.append("\n" + NLMessages.getString("View_aspect_of") + " ");
						String name = _facade.getObjectDisplayName(rStm.getRelations().firstElement().getObject());
						stext.setStyleRange(new StyleRange(start, stext.getText().length() - start,
								AEVIEWConstants.VIEW_FOREGROUND_DESELECTED_COLOR, null));
						if (_facade.getPdrObject(rStm.getRelations().firstElement().getObject()) != null)
						{
							stext.append("\uFFFC"); //$NON-NLS-1$ //$NON-NLS-2$
							Link rLink = new Link(stext, SWT.PUSH);
							rLink.setLayoutData(new GridData());
							((GridData) rLink.getLayoutData()).verticalIndent = 50;
							// pButton.setImage(imageReg.get(IconsInternal.REFERENCE));
							rLink.addSelectionListener(_aspectsSelectionAdapter);
							rLink.setData(rStm.getRelations().firstElement().getObject().toString());
							rLink.setBackground(COLOR_WHITE);
							rLink.setText("<a href=\"native\">" + name + "</a>"); //$NON-NLS-1$ //$NON-NLS-2$
							controls.add(rLink);
						}
						else
						{
							start = stext.getText().length();
							stext.append(NLMessages.getString("View_object_dot_delete") + " " + name + " "
									+ NLMessages.getString("View_missing_object")); //$NON-NLS-1$
							stext.setStyleRange(new StyleRange(start, stext.getText().length() - start,
									AEVIEWConstants.VIEW_FOREGROUND_DESELECTED_COLOR, null));
							valid = false;
						}
					}

				}
				else
				{
					int start = stext.getText().length();
					stext.append("\n" + NLMessages.getString("View_relation_subject"));
					stext.setStyleRange(new StyleRange(start, stext.getText().length() - start,
							AEVIEWConstants.VIEW_FOREGROUND_DESELECTED_COLOR, null));
					String name = _facade.getObjectDisplayName(rStm.getSubject());

					if (_facade.getPdrObject(rStm.getSubject()) != null)
					{

						stext.append("  " + "\uFFFC"); //$NON-NLS-1$ //$NON-NLS-2$
						Link rLink = new Link(stext, SWT.PUSH);
						// pButton.setImage(imageReg.get(IconsInternal.REFERENCE));
						rLink.addSelectionListener(_aspectsSelectionAdapter);
						rLink.setData(rStm.getSubject().toString());
						rLink.setBackground(COLOR_WHITE);
						rLink.setText("<a href=\"native\">" + name + "</a>"); //$NON-NLS-1$ //$NON-NLS-2$
						controls.add(rLink);



					}
					else
					{
						start = stext.getText().length();
						stext.append( name + NLMessages.getString("View_missing_object")); //$NON-NLS-1$ //$NON-NLS-3$ //$NON-NLS-4$
						valid = false;
						stext.setStyleRange(new StyleRange(start, stext.getText().length() - start,
								AEVIEWConstants.VIEW_FOREGROUND_DESELECTED_COLOR, null));
					}

					for (Relation r : rStm.getRelations())
					{
						start = stext.getText().length();
						if (r.getContext() != null)
						{
							stext.append("\n" + NLMessages.getString("View_relation_context_dot") + " "
									+ PDRConfigProvider.getLabelOfRelation(r.getProvider(), r.getContext(), null, null)
									+ "   "); //$NON-NLS-1$ //$NON-NLS-3$

						}
						if (r.getRClass() != null)
						{
							stext.append(NLMessages.getString("View_relation_class_dot")
									+ PDRConfigProvider.getLabelOfRelation(r.getProvider(), r.getContext(),
											r.getRClass(), null) + "   "); //$NON-NLS-2$

						}
						if (r.getRelation() != null)
						{
							stext.append(NLMessages.getString("View_relation_value_dot")
									+ PDRConfigProvider.getLabelOfRelation(r.getProvider(), r.getContext(),
											r.getRClass(), r.getRelation()));

						}
						stext.append("\n" + NLMessages.getString("View_object_dot_delete"));
						stext.setStyleRange(new StyleRange(start, stext.getText().length() - start,
								AEVIEWConstants.VIEW_FOREGROUND_DESELECTED_COLOR, null));
						String objectName = _facade.getObjectDisplayName(r.getObject());
						if (r.getObject() != null)
						{
							stext.append("  " + "\uFFFC"); //$NON-NLS-1$ //$NON-NLS-2$
							Link rLink = new Link(stext, SWT.PUSH);
							// pButton.setImage(imageReg.get(IconsInternal.REFERENCE));
							rLink.addSelectionListener(_aspectsSelectionAdapter);
							rLink.setData(r.getObject().toString());
							rLink.setBackground(COLOR_WHITE);
							rLink.setText("<a href=\"native\">" + objectName + "</a>"); //$NON-NLS-1$ //$NON-NLS-2$
							controls.add(rLink);
						}
						else
						{
							stext.append(" " + objectName + NLMessages.getString("View_missing_object")); //$NON-NLS-1$ //$NON-NLS-3$ //$NON-NLS-4$
							valid = false;
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
						int start = stext.getText().length();
						stext.append("\n");
						stext.append("\n" + NLMessages.getString("View_lbReference"));
						stext.setStyleRange(new StyleRange(start, stext.getText().length() - start,
								AEVIEWConstants.VIEW_FOREGROUND_DESELECTED_COLOR, null));
						if (_facade.getReference(vs.getReference().getSourceId()) != null)
						{
							String name = _facade.getReference(vs.getReference().getSourceId()).getDisplayNameLong(); //$NON-NLS-1$ //$NON-NLS-2$
							stext.append("\n              " + "\uFFFC"); //$NON-NLS-1$ //$NON-NLS-2$
							Link rLink = new Link(stext, SWT.PUSH | SWT.WRAP);
							rLink.addSelectionListener(_aspectsSelectionAdapter);
							rLink.setData(vs.getReference().getSourceId().toString());
							rLink.setBackground(COLOR_WHITE);
							rLink.setText("<a href=\"native\">" + name + "</a>"); //$NON-NLS-1$ //$NON-NLS-2$
							rLink.setSize(stext.getSize());
							controls.add(rLink);
						}
						else
						{
							start = stext.getText().length();
							stext.append(" " + NLMessages.getString("View_missing_reference") + " ("
									+ vs.getReference().getSourceId().toString() + ")"); //$NON-NLS-1$ //$NON-NLS-3$ //$NON-NLS-4$
							valid = false;
							stext.setStyleRange(new StyleRange(start, stext.getText().length() - start,
									AEVIEWConstants.VIEW_FOREGROUND_DESELECTED_COLOR, null));
						}
						if (vs.getReference().getInternal() != null)
						{
							start = stext.getText().length();
							stext.append(" " + NLMessages.getString("Editor_internal") + ": "
									+ vs.getReference().getInternal());
							stext.setStyleRange(new StyleRange(start, stext.getText().length() - start,
									AEVIEWConstants.VIEW_FOREGROUND_DESELECTED_COLOR, null));
						}
					}

				}
			}
		}
		stext.append("\n"); //$NON-NLS-1$
		stext.setData("id", aspect.getPdrId().toString()); //$NON-NLS-1$
		final int[] linkOffsets = new int[controls.size()];
		for (int i = 0; i < controls.size(); i++)
		{
			int offset = stext.getText().indexOf("\uFFFC", lastOffset); //$NON-NLS-1$
			linkOffsets[i] = offset;
			addControl(controls.get(i), linkOffsets[i], stext);
			lastOffset = offset + 1;
		}

		// use a verify listener to keep the offsets up to date
		stext.addVerifyListener(new VerifyListener()
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
		stext.addPaintObjectListener(new PaintObjectListener()
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
						int y = event.y + event.ascent - 3 * pt.y / 3;
						controls.get(i).setLocation(x, y);
						break;
					}
				}
			}
		});

		if (valid)
		{
			stextDeco.setImage(null);
		}
		else
		{
			stextDeco.setImage(FieldDecorationRegistry.getDefault()
					.getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).getImage());
		}

		// stext.setWordWrap(true);
		// stext.layout();
		// stext.setSize(getParent().getSize().x, stext.getLineCount() *
		// stext.getLineHeight());
		// Composite comp = stext.getParent();
		// ((ScrolledComposite)
		// comp.getParent()).setMinSize(comp.computeSize(SWT.DEFAULT,
		// SWT.DEFAULT));
	}

	@Override
	public void setBackground(Color greenColor) {
		if (_stext != null && !_stext.isDisposed())
		{
			_stext.setBackground(greenColor);
		}
		
	}
}
