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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;

import net.sf.vex.css.StyleSheet;
import net.sf.vex.css.StyleSheetReader;
import net.sf.vex.dom.Document;
import net.sf.vex.dom.DocumentReader;
import net.sf.vex.dom.DocumentWriter;
import net.sf.vex.dom.Element;
import net.sf.vex.dom.IWhitespacePolicy;
import net.sf.vex.dom.RootElement;
import net.sf.vex.swt.VexWidget;

import org.bbaw.pdr.ae.common.AEConstants;
import org.bbaw.pdr.ae.model.Aspect;
import org.bbaw.pdr.ae.model.TaggingRange;
import org.bbaw.pdr.ae.view.control.interfaces.IMarkupEditor;
import org.bbaw.pdr.ae.view.markup.web.internal.Activator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.PlatformUI;
import org.xml.sax.SAXException;

/**
 * The Class MarkupEditorStext.
 * @author Christoph Plutte
 */
public class MarkupEditorVex implements IMarkupEditor
{
	private StyleSheet styleSheet;
	/** The _aspect. */
	private Aspect _aspect;

	/** The _composite. */
	private Composite _composite;

	/** The _title. */
	private String _title;

	/** The _selected tr. */
	private TaggingRange[] _selectedTRs;

	/** The _font descriptor. */
	// Rap auskommentieren SWT.ROMAN
	private FontDescriptor _fontDescriptor = FontDescriptor.createFrom("Tahoma", 10, 3); //$NON-NLS-1$

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

	private HashMap<Element, TaggingRange> _taggingRangeMap;

	private HashMap<TaggingRange, TaggingRange> _recentTaggingRangeMap;

	private VexWidget _vexWidget;

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
		_vexWidget = new VexWidget(_composite, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL | SWT.BORDER);
		GridData gd = new GridData();
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalAlignment = SWT.FILL;
		gd.grabExcessVerticalSpace = true;
		gd.verticalAlignment = SWT.FILL;
		// gd.widthHint = 280;
		gd.heightHint = 50;

		_vexWidget.setLayoutData(gd);
		_vexWidget.setFont(_resources.createFont(_fontDescriptor)); //$NON-NLS-1$
		PlatformUI.getWorkbench().getHelpSystem().setHelp(_vexWidget, "org.bbaw.pdr.ae.help.notificationText"); //$NON-NLS-1$
		_vexWidget.addSelectionChangedListener(new ISelectionChangedListener()
		{
			public void selectionChanged(SelectionChangedEvent event)
			{
				System.out.println("vexwidget selection changed");
//				Integer n = null;
//				System.out.println(n.toString());
				IStructuredSelection selection = (IStructuredSelection) event.getSelection();
				Object[] objs = selection.toArray();
				if (objs instanceof Object[])
				{

					ArrayList<TaggingRange> trs = new ArrayList<TaggingRange>(objs.length);
					boolean textSelection = _vexWidget.getCurrentElement().getName().equals("notification");

					for (int i = 0; i < objs.length; i++)
					{
						if (objs[i] instanceof Element)
						{
							Element el = (Element) objs[i];
							if (!el.getName().equals("notification"))
							{
								textSelection = false;
								trs.add(_taggingRangeMap.get(el));
							}
						}
					}
					if (textSelection)
					{
						_selectedTRs = null;
						Event se = new Event();
						se.data = _selectedTRs;
						se.type = SWT.Selection;
						for (Listener s : _markupListeners)
						{
							s.handleEvent(se);
						}
						Event see = new Event();
						see.data = _vexWidget.getSelectedText();
						for (Listener s : _textSelectionListeners)
						{
							s.handleEvent(see);
						}
					}
					else
					{
						_selectedTRs = (TaggingRange[]) trs.toArray(new TaggingRange[trs.size()]);
						Event se = new Event();
						se.data = _selectedTRs;
						se.type = SWT.Selection;
						for (Listener s : _markupListeners)
						{
							s.handleEvent(se);
						}
					}
				}
				_vexWidget.layout();
				_vexWidget.redraw();
				_vexWidget.update();
				

			}
		});
		final ControlDecoration decoNoti = new ControlDecoration(_vexWidget, SWT.LEFT | SWT.TOP);

		_vexWidget.addFocusListener(new FocusListener()
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

		_vexWidget.addKeyListener(new KeyListener()
		{

			@Override
			public void keyReleased(KeyEvent e)
			{
				Event se = new Event();
				se.data = e.character;
				for (Listener s : _modifyListeners)
				{
					s.handleEvent(se);
				}

			}

			@Override
			public void keyPressed(KeyEvent e)
			{
				// TODO Auto-generated method stub

			}
		});

	}

	private boolean compareTaggingRangeAndElement(TaggingRange tr, Element el)
	{
		if (tr.getName().equals(el.getName())
				&& (tr.getType() == null || tr.getType().trim().length() == 0 || tr.getType().equals(
						el.getAttribute("type")))
				&& (tr.getSubtype() == null || tr.getSubtype().trim().length() == 0 || tr.getSubtype().equals(
						el.getAttribute("subtype")))
				&& (tr.getRole() == null || tr.getRole().trim().length() == 0 || tr.getRole().equals(
						el.getAttribute("role")))
				&& tr.getTextValue().equals(el.getText()))
		{
			System.out.println("tr equal el");
			return true;
		}
			return false;
	}


	private static StyleSheet createStyleSheet()
	{
		final URL styleSheetUrl = MarkupEditorVex.class.getResource("comment.css");
		final StyleSheetReader reader = new StyleSheetReader();
		try
		{
			return reader.read(styleSheetUrl);
		}
		catch (IOException e)
		{
			AEConstants.ILOGGER.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, "Error trying to read Stylesheet: " + styleSheetUrl.toString()));
			throw new AssertionError("Cannot read the stylesheet: " + e.getMessage());
		}
	}
	@Override
	public void saveChanges()
	{
		if (_aspect != null && _vexWidget != null && _vexWidget.getDocument() != null)
		{
			ByteArrayOutputStream output = new ByteArrayOutputStream();

			DocumentWriter docWriter = new DocumentWriter();
			IWhitespacePolicy wsp = new IWhitespacePolicy()
			{

				@Override
				public boolean isPre(Element element)
				{
					System.out.println("isPre " + element.getName());

					if (element.getName().equals("notification"))
					{
						return true;
					}
					else
					{
						return true;
					}
				}

				@Override
				public boolean isBlock(Element element)
				{
					System.out.println("isBlock " + element.getName());
					if (element.getName().equals("notification"))
					{
						return true;
					}
					else
					{
						return true;
					}
				}
			};
			docWriter.setWhitespacePolicy(wsp);
			System.out.println("document encoding " + _vexWidget.getDocument().getEncoding());
			try
			{
				docWriter.write(_vexWidget.getDocument(), output);
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String xml = null;

			try
			{
				xml = new String(output.toByteArray(), "UTF-8");
			}
			catch (UnsupportedEncodingException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Pattern p = Pattern.compile("<\\?xml version='1.0'\\?>");
			Matcher m = p.matcher(xml);
			xml = m.replaceAll("<\\?xml version=\"1.0\" encoding=\"UTF-8\"\\?>");
			System.out.println("saveChanges in vex " + xml);

			_aspect.setNotificationAsXML(xml);
		}
	}



	@Override
	public final void deleteMarkup(final TaggingRange[] taggingRanges)
	{
		if (taggingRanges != null)
		{
			if (_recentTaggingRangeMap != null)
			{
				for (TaggingRange tr : taggingRanges)
				{
					_aspect.getRangeList().remove(_recentTaggingRangeMap.get(tr));
				}
			}
			else
			{
				for (TaggingRange tr : taggingRanges)
				{
					_aspect.getRangeList().remove(tr);
				}
			}
			int caret = _vexWidget.getCaretOffset();

			try
			{
				loadValues();
			}
			catch (ParserConfigurationException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (SAXException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (XMLStreamException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			_vexWidget.moveTo(caret);
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
		return _vexWidget;
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
	public final void insertContentSetMarkup(final TaggingRange taggingRange)
	{
		if (taggingRange.getType() != null && taggingRange.getType().trim().length() > 0
				&& taggingRange.getTextValue().trim().length() > 0)
		{

			Collections.sort(_aspect.getRangeList());
			int start = _vexWidget.getSelectionStart();

			String selectedText = taggingRange.getTextValue();

			taggingRange.setStart(start);
			taggingRange.setLength(selectedText.length());

			Element el = new Element(taggingRange.getName());
			if (taggingRange.getType() != null && taggingRange.getType().trim().length() > 0)
			{
				el.setAttribute("type", taggingRange.getType());
			}
			if (taggingRange.getSubtype() != null && taggingRange.getSubtype().trim().length() > 0)
			{
				el.setAttribute("subtype", taggingRange.getSubtype());
			}
			if (taggingRange.getRole() != null && taggingRange.getRole().trim().length() > 0)
			{
				el.setAttribute("role", taggingRange.getRole());
			}
			if (taggingRange.getFrom() != null)
			{
				el.setAttribute("from", taggingRange.getFrom().toString());
			}
			if (taggingRange.getTo() != null)
			{
				el.setAttribute("to", taggingRange.getTo().toString());
			}
			if (taggingRange.getWhen() != null)
			{
				el.setAttribute("when", taggingRange.getWhen().toString());
			}
			if (taggingRange.getNotBefore() != null)
			{
				el.setAttribute("notBefore", taggingRange.getNotBefore().toString());
			}
			if (taggingRange.getNotAfter() != null)
			{
				el.setAttribute("notAfter", taggingRange.getNotAfter().toString());
			}
			if (taggingRange.getKey() != null && taggingRange.getKey().trim().length() > 0)
			{
				el.setAttribute("key", taggingRange.getKey());
			}
			if (taggingRange.getAna() != null && taggingRange.getAna().trim().length() > 0)
			{
				el.setAttribute("ana", taggingRange.getAna());
			}
			_taggingRangeMap.put(el, taggingRange);

			_vexWidget.insertText(selectedText);
			_vexWidget.moveTo(start, false);
			_vexWidget.moveTo(start + selectedText.length(), true);
			_vexWidget.insertElement(el);
			saveChanges();
			loadTaggingMap();

		}

	}



	@Override
	public final boolean isValid()
	{
		return true;
	}

	/**
	 * Load values.
	 * @throws XMLStreamException
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 */
	private void loadValues() throws ParserConfigurationException, SAXException, IOException, XMLStreamException
	{
		System.out.println("loadValues");
		styleSheet = createStyleSheet();
		_taggingRangeMap = new HashMap<Element, TaggingRange>();
		Document document = createNotificatinDoc();
		System.out.println("load values encoding " + document.getEncoding());
//		document.setEncoding("UTF-8");
//		_vexWidget.setForeground(AEVIEWConstants.VIEW_FOREGROUND_SELECTED_COLOR);

		_vexWidget.setDocument(document, styleSheet);
		loadTaggingMap();

//		_vexWidget.setStyleSheet(styleSheet);
//		_vexWidget.redraw();
		_vexWidget.layout();
		_vexWidget.redraw();
		_vexWidget.update();
		
	
	}

	private void loadTaggingMap()
	{
		// Tagging
		LinkedList<TaggingRange> rangeList;
		ArrayList<Element> processedElements = new ArrayList<Element>();
		if (_aspect.getRangeList() != null)
		{
			rangeList = (LinkedList<TaggingRange>) _aspect.getRangeList();

			for (int i = 1; i < _vexWidget.getDocument().getLength(); i++)
			{
				Element e = _vexWidget.getDocument().getElementAt(i);
				if (e != null && !processedElements.contains(e))
				{
					processedElements.add(e);
					for (TaggingRange tr : rangeList)
					{
						if (compareTaggingRangeAndElement(tr, e))
						{
							_taggingRangeMap.put(e, tr);
						}
					}
				}
			}
		}

	}

	private Document createNotificatinDoc() throws ParserConfigurationException, SAXException, IOException,
			XMLStreamException
	{
		Document document;
		if (_aspect.getNotification() != null)
		{
			document = new DocumentReader().read(replaceLB(_aspect.getNotificationAsXML()));

			// document.setValidator(createValidator());
			return document;
			

		}
		else
		{
			document = new Document(new RootElement("notification"));
//			document.setValidator(createValidator());
		}
		return document;
	}

	private String replaceLB(String notificationAsXML)
	{
		notificationAsXML.replaceAll("<lb></lb>", "\n");
		Pattern p = Pattern.compile("<lb></lb>");
		Matcher m = p.matcher(notificationAsXML);
		String replaced = m.replaceAll("\n");
		return replaced;
	}

	@Override
	public final void setAspect(final Aspect aspect)
	{
		this._aspect = aspect;
		if (_aspect != null)
		{
			try
			{
				loadValues();
			}
			catch (ParserConfigurationException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (SAXException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (XMLStreamException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
		_vexWidget.setEnabled(editable);

	}

	@Override
	public final void setFont(final FontDescriptor fontDescriptor)
	{
		this._fontDescriptor = fontDescriptor;

	}

	@Override
	public final void setMarkup(final TaggingRange taggingRange)
	{
		int start = _vexWidget.getSelectionStart();
		int end = _vexWidget.getSelectionEnd();

		String selectedText = _vexWidget.getSelectedText();
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
		taggingRange.setTextValue(_vexWidget.getSelectedText());

		Element el = new Element(taggingRange.getName());
		if (taggingRange.getType() != null && taggingRange.getType().trim().length() > 0)
		{
			el.setAttribute("type", taggingRange.getType());
		}
		if (taggingRange.getSubtype() != null && taggingRange.getSubtype().trim().length() > 0)
		{
			el.setAttribute("subtype", taggingRange.getSubtype());
		}
		if (taggingRange.getRole() != null && taggingRange.getRole().trim().length() > 0)
		{
			el.setAttribute("role", taggingRange.getRole());
		}
		if (taggingRange.getFrom() != null)
		{
			el.setAttribute("from", taggingRange.getFrom().toString());
		}
		if (taggingRange.getTo() != null)
		{
			el.setAttribute("to", taggingRange.getTo().toString());
		}
		if (taggingRange.getWhen() != null)
		{
			el.setAttribute("when", taggingRange.getWhen().toString());
		}
		if (taggingRange.getNotBefore() != null)
		{
			el.setAttribute("notBefore", taggingRange.getNotBefore().toString());
		}
		if (taggingRange.getNotAfter() != null)
		{
			el.setAttribute("notAfter", taggingRange.getNotAfter().toString());
		}
		if (taggingRange.getKey() != null && taggingRange.getKey().trim().length() > 0)
		{
			el.setAttribute("key", taggingRange.getKey());
		}
		if (taggingRange.getAna() != null && taggingRange.getAna().trim().length() > 0)
		{
			el.setAttribute("ana", taggingRange.getAna());
		}
		_taggingRangeMap.put(el, taggingRange);
		_vexWidget.insertElement(el);
		saveChanges();
		loadTaggingMap();

	}

	@Override
	public final void setTitle(final String title)
	{
		this._title = title;

	}

	@Override
	public void refresh()
	{
		if (_aspect != null && _vexWidget != null)
		{
			int offset = _vexWidget.getCaretOffset();
			int textLength = _vexWidget.getDocument().getLength();
			int rangeNum = _vexWidget.getDocument().getRootElement().getChildNodes().length;
			try
			{
				loadValues();
			}
			catch (ParserConfigurationException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (SAXException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (XMLStreamException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			_vexWidget.moveTo(offset);

			if (textLength != _vexWidget.getDocument().getLength()
					|| rangeNum != _vexWidget.getDocument().getRootElement().getChildNodes().length)
			{
				Event se = new Event();
				for (Listener s : _modifyListeners)
				{
					s.handleEvent(se);
				}
			}
		}
	}

	@Override
	public void insert(String string)
	{
		_vexWidget.insertText(string);
	}

	@Override
	public void setSelected(boolean selected)
	{
//		if (selected)
//		{
//			_vexWidget.setForeground(AEVIEWConstants.VIEW_FOREGROUND_SELECTED_COLOR);
//		}
//		else
//		{
//			_vexWidget.setForeground(AEVIEWConstants.VIEW_FOREGROUND_DESELECTED_COLOR);
//		}
//		_vexWidget.setStyleSheet(styleSheet);
		
	}

	@Override
	public void setFocus()
	{
		_vexWidget.setFocus();

	}

	@Override
	public String getSelectionText()
	{
		return _vexWidget.getSelectedText();
	}

}
