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
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.bbaw.pdr.ae.common.AEConstants;
import org.bbaw.pdr.ae.common.AEVIEWConstants;
import org.bbaw.pdr.ae.common.CommonActivator;
import org.bbaw.pdr.ae.config.model.ConfigItem;
import org.bbaw.pdr.ae.control.core.PDRConfigProvider;
import org.bbaw.pdr.ae.control.facade.Facade;
import org.bbaw.pdr.ae.model.Aspect;
import org.bbaw.pdr.ae.model.TaggingRange;
import org.bbaw.pdr.ae.view.control.interfaces.IMarkupEditor;
import org.bbaw.pdr.ae.view.control.provider.MarkupListContentProposalProvider;
import org.bbaw.pdr.ae.view.control.provider.MarkupListLabelProvider;
import org.bbaw.pdr.ae.view.control.swtnotrwthelper.StyledTextContentAdapter;
import org.bbaw.pdr.ae.view.control.swtnotrwthelper.TaggingRangeTransformer;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.bindings.keys.KeyStroke;
import org.eclipse.jface.bindings.keys.ParseException;
import org.eclipse.jface.fieldassist.ContentProposalAdapter;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.jface.fieldassist.IContentProposalListener;
import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ExtendedModifyEvent;
import org.eclipse.swt.custom.ExtendedModifyListener;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

/**
 * The Class MarkupEditorStext.
 * @author Christoph Plutte
 */
public class MarkupEditorStext implements IMarkupEditor
{

	/** The _aspect. */
	private Aspect _aspect;

	/** The _composite. */
	private Composite _composite;

	/** The _title. */
	private String _title;

	/** The _stext. */
	private StyledText _stext;

	/** The _selected tr. */
	private TaggingRange[] _selectedTRs;

	/** The _tr transformer. */
	private TaggingRangeTransformer _trTransformer = new TaggingRangeTransformer();

	/** The _font descriptor. */
	private FontDescriptor _fontDescriptor = FontDescriptor.createFrom("Tahoma", 10, SWT.ROMAN); //$NON-NLS-1$

	/** Resource Manager for colors and fonts. */
	private LocalResourceManager _resources = new LocalResourceManager(JFaceResources.getResources());

	/** The _markup listeners. */
	private ArrayList<Listener> _markupListeners = new ArrayList<Listener>();

	/** The _modify listeners. */
	private ArrayList<Listener> _modifyListeners = new ArrayList<Listener>();

	/** The _focus listeners. */
	private ArrayList<Listener> _focusListeners = new ArrayList<Listener>();

	/** The _key listeners. */
	private ArrayList<Listener> _keyListeners = new ArrayList<Listener>();

	/** The _text selection listeners. */
	private ArrayList<Listener> _textSelectionListeners = new ArrayList<Listener>();

	/** The markup provider. */
	private String _markupProvider = Platform
			.getPreferencesService()
			.getString(CommonActivator.PLUGIN_ID, "PRIMARY_TAGGING_PROVIDER", AEConstants.TAGGING_LIST_PROVIDER, null).toUpperCase(); //$NON-NLS-1$;
	

	private Facade _facade = Facade.getInstanz();

	private boolean _loaded;

	@Override
	public final void addExtendedModifyListener(final Listener extendedModifyListener)
	{
		if (extendedModifyListener != null)
		{
			_modifyListeners.add(extendedModifyListener);
		}

	}

	@Override
	public final void addFocusListener(final Listener focusListener)
	{
		if (focusListener != null)
		{
			_focusListeners.add(focusListener);
		}

	}

	@Override
	public final void addKeyListener(final Listener keyListener)
	{
		if (keyListener != null)
		{
			_keyListeners.add(keyListener);
		}

	}

	@Override
	public final void addMarkupSelectionListener(final Listener listener)
	{
		if (listener != null)
		{
			_markupListeners.add(listener);
		}

	}

	@Override
	public final void addTextSelectionListener(final Listener listener)
	{
		if (listener != null)
		{
			_textSelectionListeners.add(listener);
		}

	}

	@Override
	public final void createEditor()
	{
		_stext = new StyledText(_composite, SWT.MULTI | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		GridData gd = new GridData();
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalAlignment = SWT.FILL;
		gd.grabExcessVerticalSpace = true;
		gd.verticalAlignment = SWT.FILL;
		// gd.widthHint = 280;
		gd.heightHint = 50;

		_stext.setLayoutData(gd);
		_stext.setFont(_resources.createFont(_fontDescriptor)); //$NON-NLS-1$
		//         PlatformUI.getWorkbench().getHelpSystem().setHelp(_stext, "org.bbaw.pdr.ae.help.notificationText"); //$NON-NLS-1$
		final ControlDecoration decoNoti = new ControlDecoration(_stext, SWT.LEFT | SWT.TOP);

		_stext.addExtendedModifyListener(new ExtendedModifyListener() {
			public void modifyText(ExtendedModifyEvent event)
			{
				String modi = event.replacedText;
				Event se = new Event();
				se.data = modi;
				for (Listener s : _modifyListeners)
				{
					s.handleEvent(se);
				}
			}
        });

		_stext.addMouseMoveListener(new MouseMoveListener()
		{

			@Override
			public void mouseMove(final MouseEvent e)
			{

				Point p = new Point(e.x, e.y);
				boolean hit = false;
				if (p != null)
				{
					try
					{
						int offset = _stext.getOffsetAtLocation(p);
						if (offset >= 1 && offset <= _stext.getText().length())
						{
							if (_stext.getStyleRangeAtOffset(offset) != null
									|| _stext.getStyleRangeAtOffset(offset - 1) != null)
							{

								StyleRange ss = _stext.getStyleRangeAtOffset(offset);
								if (ss != null)
								{
									TaggingRange tr = (TaggingRange) ss.data;
									_selectedTRs = new TaggingRange[]
									{tr};
									hit = true;
								}
								else
								{
									_selectedTRs = null;
								}
								Event se = new Event();
								se.data = _selectedTRs;
								se.x = e.x;
								se.y = e.y;
								se.type = SWT.MouseHover;
								for (Listener s : _markupListeners)
								{
									s.handleEvent(se);
								}

							}
						}

					}

					catch (IllegalArgumentException ex)
					{

					}

				}
				if (!hit)
				{
					Event se = new Event();
					se.data = null;
					se.x = e.x;
					se.y = e.y;
					se.type = SWT.MouseHover;
					for (Listener s : _markupListeners)
					{
						s.handleEvent(se);
					}
				}
			}
		});

		_stext.addMouseListener(new MouseAdapter()
		{
			public void mouseDown(MouseEvent e)
			{
				// System.out.println("mousListner offsett: " + offset);
				handleSelection();

			}
		});
		_stext.addFocusListener(new FocusListener()
		{

			@Override
			public void focusLost(FocusEvent e)
			{
				saveChanges();
				Event se = new Event();
				se.doit = false;
				for (Listener l : _focusListeners)
				{
					l.handleEvent(se);
				}

			}
			
			@Override
			public void focusGained(FocusEvent e)
			{
				Event se = new Event();
				se.doit = true;
				for (Listener l : _focusListeners)
				{
					l.handleEvent(se);
				}

			}
		});

		_stext.addKeyListener(new KeyListener()
		{
			@Override
			public void keyPressed(final KeyEvent e)
			{
			}

			@Override
			public void keyReleased(final KeyEvent e)
			{
				if (_stext.getText().trim().length() > 0)
				{
					decoNoti.setImage(null);
				}
				else
				{
					decoNoti.setImage(FieldDecorationRegistry.getDefault()
							.getFieldDecoration(FieldDecorationRegistry.DEC_REQUIRED).getImage());
				}
				handleSelection();




			}
		});

		_stext.addSelectionListener(new SelectionListener()
		{

			@Override
			public void widgetDefaultSelected(final SelectionEvent e)
			{

			}

			@Override
			public void widgetSelected(final SelectionEvent e)
			{

				handleSelection();
				// }
			}

		});

		KeyStroke keyStroke;
		try
		{
			keyStroke = KeyStroke.getInstance("Ctrl+Space");
			System.out.println("contentProposal");

			ContentProposalAdapter adapter = new ContentProposalAdapter(_stext, new StyledTextContentAdapter(),
					new MarkupListContentProposalProvider(_facade.getConfigs().get(_markupProvider), false), keyStroke,
					null);
			adapter.setLabelProvider(new MarkupListLabelProvider());
			adapter.addContentProposalListener(new IContentProposalListener()
			{
				@Override
				public void proposalAccepted(final IContentProposal proposal)
				{
					if (proposal instanceof ConfigItem)
					{
						ConfigItem ci = (ConfigItem) proposal;
						ConfigItem ciParent;
						ConfigItem ciGrandParent;
						ConfigItem ciGreatGrandParent;
						String name;
						String type = null;
						String subtype = null;
						String role = null;
						// String ana = _currentPerson.getPdrId().toString();
						if (ci.getParent() != null)
						{
							if (ci.getParent() instanceof ConfigItem)
							{
								ciParent = (ConfigItem) ci.getParent();
								if (ciParent.getParent() != null)
								{
									if (ciParent.getParent() instanceof ConfigItem)
									{
										ciGrandParent = (ConfigItem) ciParent.getParent();
										if (ciGrandParent.getParent() != null)
										{
											if (ciGrandParent.getParent() instanceof ConfigItem)
											{
												ciGreatGrandParent = (ConfigItem) ciGrandParent.getParent();
												name = ciGreatGrandParent.getValue();
												type = ciGrandParent.getValue();
												subtype = ciParent.getValue();
												role = ci.getValue();
											}
											else
											{
												name = ciGrandParent.getParent().getValue();
												type = ciGrandParent.getValue();
												subtype = ciParent.getValue();
												role = ci.getValue();
											}

										}
										else
										{
											name = ciGrandParent.getValue();
											type = ciParent.getValue();
											subtype = ci.getValue();
										}
									}
									else
									{
										name = ciParent.getParent().getValue();
										type = ciParent.getValue();
										subtype = ci.getValue();
									}
								}
								else
								{
									name = ciParent.getValue();
									type = ci.getValue();
								}
							}
							else
							{
								name = ci.getParent().getValue();
								type = ci.getValue();
							}
						}
						else
						{
							name = ci.getValue();
						}

						if (name.startsWith("aodl:"))
						{
							name = name.substring(5);
						}
						if (name.length() > 0 && type.length() > 0)
						{
							TaggingRange tr = new TaggingRange(name, type, subtype, role, null, null);

							String content = PDRConfigProvider.getLabelOfMarkup(name, type, subtype, role);
							int start = 0;

							start = _stext.getCaretOffset();

							// System.out.println(_stext.getSelectionText());
							tr.setStart(start);
							tr.setLength(content.length());
							tr.setTextValue(content);
							insertContentSetMarkup(tr);

							for (TaggingRange t : _aspect.getRangeList())
							{
								if (t.getStart() > start)
								{
									t.setStart(t.getStart() + content.length());
								}
							}
							_aspect.getRangeList().add(tr);

							Collections.sort(_aspect.getRangeList());

							_stext.setSelection(start + content.length());

							// System.out.println(proposal.getContent());
						}
					}
				}
			});
		}
		catch (ParseException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	protected void handleSelection()
	{
		int start = _stext.getSelection().x;
		int end = _stext.getSelection().y;
		_selectedTRs = null;
		if (start <= end && start >= 0 && end <= _stext.getCharCount())
		{
			StyleRange[] ss;
			if (start == end && start < _stext.getCharCount() && _stext.getStyleRangeAtOffset(start) != null)
			{
				ss = new StyleRange[]
				{_stext.getStyleRangeAtOffset(start)};
			}
			else
			{
				ss = _stext.getStyleRanges(start, end - start);
			}
			if (ss != null && ss.length > 0)
			{
				ArrayList<TaggingRange> trs = new ArrayList<TaggingRange>(ss.length);
				for (StyleRange s : ss)
				{
					if (s != null)
					{
						TaggingRange tr = (TaggingRange) s.data;
						trs.add(tr);
					}

				}
				_selectedTRs = (TaggingRange[]) trs.toArray(new TaggingRange[trs.size()]);
			}
			Event se = new Event();
			se.data = _selectedTRs;
			se.type = SWT.KeyUp;
			for (Listener s : _markupListeners)
			{
				s.handleEvent(se);
			}
			if (_selectedTRs == null)
			{

				String text = _stext.getText().substring(start, end);
				if (text == null || text.trim().length() > 0)
				{
					se.data = text;
				}
				else
				{
					se.data = null;
				}
				for (Listener s : _textSelectionListeners)
				{
					s.handleEvent(se);
				}
			}
		}

	}

	@Override
	public void saveChanges()
	{
		if (_aspect != null && _loaded)
		{
			if (_aspect.getRangeList() != null)
			{
				processStyleRanges();
			}
			_aspect.setNotification(_stext.getText());
		}
		
	}
	private void processStyleRanges()
	{
		TaggingRange tr;
		List<TaggingRange> processedRanges = new ArrayList<TaggingRange>();
		for (StyleRange sr : _stext.getStyleRanges())
		{
			if (sr.data != null && sr.data instanceof TaggingRange && sr.length > 0)
			{
				tr = (TaggingRange) sr.data;
				tr.setStart(sr.start);
				tr.setLength(sr.length);
				tr.setTextValue(_stext.getText(sr.start, sr.start + sr.length - 1));
				processedRanges.add(tr);
			}
		}
		_aspect.getRangeList().clear();
		_aspect.getRangeList().addAll(processedRanges);
		Collections.sort(_aspect.getRangeList());

	}
	@Override
	public final void deleteMarkup(final TaggingRange[] taggingRanges)
	{
		if (taggingRanges != null)
		{
			for (TaggingRange tr : taggingRanges)
			{
				if (_stext.getStyleRangeAtOffset(tr.getStart()) != null)
				{
					_stext.setStyleRange(new StyleRange(tr.getStart(), tr.getLength(), null, null));
				}
			}
		}


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

	@Override
	public final TaggingRange[] getSelectedMarkups()
	{
		return _selectedTRs;
	}

	@Override
	public final String getTitle()
	{
		return _title;
	}

	@Override
	public final void insertContentSetMarkup(final TaggingRange tr)
	{
		if (tr.getType() != null && tr.getType().trim().length() > 0 && tr.getTextValue().trim().length() > 0)
		{
			StyleRange sr = new StyleRange();
			sr.data = tr;
			sr.start = _stext.getSelection().x;
			tr.setStart(sr.start);
			sr.length = tr.getTextValue().trim().length();
			tr.setLength(sr.length);
			sr.background = _trTransformer.chooseColor(tr.getName());
			_stext.insert(tr.getTextValue());
			_stext.setStyleRange(sr);
			_stext.setSelection(sr.start + sr.length, sr.start + sr.length);
			_aspect.getRangeList().add(tr);

			Collections.sort(_aspect.getRangeList());

		}

	}

	@Override
	public final boolean isValid()
	{
		return _stext.getText().trim().length() > 0;
	}

	/**
	 * Load values.
	 */
	private void loadValues()
	{
		_loaded = false;
		if (_aspect.getNotification() != null)
		{
			_stext.setText(_aspect.getNotification());
		}
		// Tagging
		LinkedList<TaggingRange> rangeList;
		StyleRange sr;
		if (_aspect.getRangeList() != null)
		{
			rangeList = (LinkedList<TaggingRange>) _aspect.getRangeList();
			for (TaggingRange tr : rangeList)
			{
				// System.out.println(tr.getStyleRange().start);
				// System.out.println(tr.getStart());
				// System.out.println(tr.getLength());
				// System.out.println(styledTextAspect.getText().length());
				if (tr != null)
				{
					if (tr.getStart() < _aspect.getNotification().length()
						&& tr.getStart() + tr.getLength() < _aspect.getNotification().length())
					{
						sr = _trTransformer.transformStyleRange(tr);
						sr.data = tr;
						_stext.setStyleRange(sr);
					}
					else if (tr.getStart() < _aspect.getNotification().length())
					{
						sr = _trTransformer.transformStyleRange(tr);
						sr.length = _aspect.getNotification().length() - tr.getStart();
						sr.data = tr;
						_stext.setStyleRange(sr);
					}
				}
			}
		}
		else
		{
			rangeList = new LinkedList<TaggingRange>();
		}
		_loaded = true;
	}

	@Override
	public final void setAspect(final Aspect aspect)
	{
		this._aspect = aspect;
		if (_aspect != null)
		{
			loadValues();
		}

	}

	@Override
	public final void setComposite(final Composite composite)
	{
		this._composite = composite;
	}

	@Override
	public final void setEditable(final boolean editable)
	{
		_stext.setEditable(editable);

	}

	@Override
	public final void setFont(final FontDescriptor fontDescriptor)
	{
		this._fontDescriptor = fontDescriptor;

	}

	@Override
	public final void setMarkup(final TaggingRange taggingRange)
	{
		
		int start = _stext.getSelection().x;
		int end = _stext.getSelectionCount() - 1 + start;
		if (end < start)
		{
			end = start;
		}

		if (_stext.getSelectionRange().y > 0)
		{
			int selectionStart = _stext.getSelectionRange().x;
			int selectionEnd = selectionStart + _stext.getSelectionRange().y;
			if (_aspect.getRangeList() != null && _aspect.getRangeList().size() > 0)
			{
				for (TaggingRange currentTaggingRange : _aspect.getRangeList())
				{
					int styleRangeStart = currentTaggingRange.getStart();
					int styleRangeEnd = styleRangeStart + currentTaggingRange.getLength();
					if (selectionStart < styleRangeStart && selectionEnd > styleRangeStart)
					{
						/*
						 * Selection ueberlappt Anfang der StyleRange (oder auch
						 * Anfang und Ende)
						 */
						start = selectionStart;
						end = styleRangeStart;
						break;
					}
					else if (selectionStart < styleRangeEnd && selectionEnd > styleRangeEnd)
					{
						/* Selection ueberlappt Ende der StyleRange */
						start = styleRangeEnd;
						end = selectionEnd;
						break;
					}
				}
			}
		}
		String selectedText = _stext.getText(start, end);
		while (selectedText.startsWith(" "))
		{
			selectedText = selectedText.substring(1);
			start++;
		}
		while (selectedText.endsWith(" "))
		{
			selectedText = selectedText.substring(0, selectedText.length() - 1);
			end--;
		}
		taggingRange.setStart(start);
		taggingRange.setLength(end - start + 1);
		if (_stext.getText().length() >= end)
		{
			taggingRange.setTextValue(_stext.getText(start, end));
		}
		else
		{
			taggingRange.setTextValue(_stext.getText(start, _stext.getText().length()));
		}

		StyleRange sr = _trTransformer.transformStyleRange(taggingRange);
		sr.data = taggingRange;
		if (sr != null)
		{
			if (sr.start >= 0 && sr.length + sr.start <= _stext.getText().length())
			{
				_stext.setStyleRange(sr);
			}
			else if (sr.length + sr.start <= _stext.getText().length())
			{
				taggingRange.setLength(_stext.getText().length() - taggingRange.getStart());
				sr.length = _stext.getText().length() - sr.start;
				try
				{
					_stext.setStyleRange(sr);
				}
				finally
				{

					// nothing
				}
			}
		}
		_stext.setSelection(sr.start + sr.length, sr.start + sr.length);

	}

	@Override
	public final void setTitle(final String title)
	{
		this._title = title;

	}

	@Override
	public void refresh()
	{
		int caret = _stext.getCaretOffset();
		loadValues();
		_stext.setCaretOffset(Math.min(caret, _stext.getTextLimit()));
	}

	@Override
	public void insert(String string)
	{
		_stext.insert(string);
		_stext.setSelection(_stext.getSelection().x + string.length());

	}

	@Override
	public void setSelected(boolean selected)
	{
		if (selected)
		{
			_stext.setForeground(AEVIEWConstants.VIEW_FOREGROUND_SELECTED_COLOR);
		}
		else
		{
			_stext.setForeground(AEVIEWConstants.VIEW_FOREGROUND_DESELECTED_COLOR);
		}

	}

	@Override
	public void setFocus()
	{
		_stext.setFocus();

	}

	@Override
	public String getSelectionText()
	{
		return _stext.getSelectionText();
	}
	
}
