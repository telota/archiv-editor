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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.TransformerException;

import org.bbaw.pdr.ae.common.AEConstants;
import org.bbaw.pdr.ae.common.AEVIEWConstants;
import org.bbaw.pdr.ae.common.CommonActivator;
import org.bbaw.pdr.ae.model.Aspect;
import org.bbaw.pdr.ae.model.PdrObject;
import org.bbaw.pdr.ae.model.Reference;
import org.bbaw.pdr.ae.model.Relation;
import org.bbaw.pdr.ae.model.RelationDim;
import org.bbaw.pdr.ae.model.RelationStm;
import org.bbaw.pdr.ae.model.TimeDim;
import org.bbaw.pdr.ae.model.TimeStm;
import org.bbaw.pdr.ae.model.Validation;
import org.bbaw.pdr.ae.model.ValidationStm;
import org.bbaw.pdr.ae.view.control.interfaces.IAEBasicEditor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class AspectMarkupTemplate extends Composite implements IAEBasicEditor
{

	private Aspect _aspect;
	private PdrObject _owningObject;
	private Vector<AEAspectWidgetCustomizable> _widgets = new Vector<AEAspectWidgetCustomizable>();
	private Document _notificationDOM;
	/** The _selection listener. */
	private ArrayList<SelectionListener> _selectionListeners = new ArrayList<SelectionListener>();
	private SelectionListener _selectionListener;
	private boolean _editable;
	private boolean _isValid;
	public AspectMarkupTemplate(IAEBasicEditor parentEditor, PdrObject owningObject, Composite parent, int style)
	{
		super(parent, style);
		_owningObject = owningObject;
		if (_selectionListener == null)
		{
			createSelectionListener();
		}

	}

	
	public AspectMarkupTemplate(IAEBasicEditor parentEditor, Aspect aspect, Composite parent, int style)
	{
		super(parent, style);
		if (_selectionListener == null)
		{
			createSelectionListener();
		}
		setAspect(aspect);

	}

	private void createSelectionListener() {
		_selectionListener = new SelectionListener()
		{

			@Override
			public void widgetSelected(SelectionEvent e)
			{
				Event ee = new Event();
				// ee.data = EasyAspectEditor.this;
				ee.widget = AspectMarkupTemplate.this;
				SelectionEvent se = new SelectionEvent(ee);
				se.data = AspectMarkupTemplate.this;
				for (SelectionListener s : _selectionListeners)
				{
					s.widgetSelected(se);
				}

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e)
			{
				// TODO Auto-generated method stub

			}
		};
		
	}

	private void setAspect(Aspect aspect)
	{
		_aspect = aspect;

		if (_aspect != null)
		{
			try
			{
				_notificationDOM = _aspect.getNotificationAsDOM();
//				System.out.println("_notificationDOM");
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

			NodeList nodes = _notificationDOM.getElementsByTagName("*");
			List<Node> unprocessedItems = new ArrayList<Node>(nodes.getLength());
			HashMap<Node, AEAspectWidgetCustomizable> matchingTable = new HashMap<Node, AEAspectWidgetCustomizable>(
					nodes.getLength());
			HashMap<AEAspectWidgetCustomizable, Integer> multiInputTable = new HashMap<AEAspectWidgetCustomizable, Integer>(
					_widgets.size());
			Collections.sort(_widgets);
			for (int i = 0; i < nodes.getLength(); i++)
			{
				if (!nodes.item(i).getNodeName().equals("notification"))
				{
					unprocessedItems.add(nodes.item(i));
				}

			}
			for (Node n : unprocessedItems)
			{
				for (AEAspectWidgetCustomizable w : _widgets)
				{
					if (w.getWidgetType() <= 4 && w.matchesInput(n) > 0)
					{
						if (matchingTable.containsKey(n))
						{
							AEAspectWidgetCustomizable oldW = matchingTable.get(n);
							int oldMatch = oldW.matchesInput(n);
							int oldMuliIndex = 0;
							if (multiInputTable.containsKey(oldW))
							{
								Integer index = multiInputTable.get(oldW);
								oldMuliIndex = index;
							}
							int currentMatch = w.matchesInput(n);
							int currentMultiIndex = 0;
							if (multiInputTable.containsKey(w))
							{
								Integer index = multiInputTable.get(w);
								currentMultiIndex = index;
							}
							if ((oldMatch < currentMatch)
									|| ((w.getConfigTemplate().isAllowMultiple() || currentMultiIndex == 0)
											&& oldMatch - oldMuliIndex < currentMatch - currentMultiIndex))
							{
								matchingTable.remove(n);
								matchingTable.put(n, w);
								if (multiInputTable.containsKey(oldW))
								{
									Integer index = multiInputTable.get(oldW);
									index = index - 1;
									multiInputTable.remove(oldW);
									multiInputTable.put(oldW, index);
								}
								else
								{
									multiInputTable.put(w, 0);
								}
								if (multiInputTable.containsKey(w))
								{
									Integer index = multiInputTable.get(w);
									index = index + 1;
									multiInputTable.remove(w);
									multiInputTable.put(w, index);
								}
								else
								{
									multiInputTable.put(w, 0);
								}
							}
						}
						else
						{
							matchingTable.put(n, w);
							if (multiInputTable.containsKey(w))
							{
								Integer index = multiInputTable.get(w);
								index = index + 1;
								multiInputTable.remove(w);
								multiInputTable.put(w, index);
							}
							else
							{
								multiInputTable.put(w, 0);
							}
						}
					}
				}
			}
			for (Node n : unprocessedItems)
			{
				if (matchingTable.containsKey(n))
				{
					AEAspectWidgetCustomizable w = matchingTable.get(n);
					w.setInput(n);
				}
			}
			// relations
			if (_aspect.getRelationDim() != null)
			{
				for (RelationStm rStm : _aspect.getRelationDim().getRelationStms())
				{
					if (rStm.getSubject() != null && _owningObject != null
							&& rStm.getSubject().equals(_owningObject.getPdrId()))
					{
						for (Relation relation : rStm.getRelations())
						{
							boolean found = false;
							AEAspectWidgetCustomizable lastWidget = null;
							for (AEAspectWidgetCustomizable w : _widgets)
							{
								if (!found && w.getWidgetType() == AEAspectWidgetCustomizable.RELATION_DEFINED && w.matchesInput(relation) > 0)
								{
									w.setInput(relation);
									found = true;
								}
							}
							if (!found)
							{
								for (AEAspectWidgetCustomizable w : _widgets)
								{
									if (!found && w.getWidgetType() == AEAspectWidgetCustomizable.RELATION && w.matchesInput(relation) > 0)
									{
										w.setInput(relation);
										found = true;
										lastWidget = w;

									}
								}
								if (!found && lastWidget != null)
								{
								lastWidget.setInput(relation);
								}
							}
						}
					}
				}
				
				
			}
			else
			{
				_aspect.setRelationDim(new RelationDim());
			}
			
			
			//timeDim
			if (_aspect.getTimeDim() != null)
			{
				for (TimeStm tStm : _aspect.getTimeDim().getTimeStms())
				{
					boolean found = false;
					AEAspectWidgetCustomizable lastWidget = null;
					for (AEAspectWidgetCustomizable w : _widgets)
					{
						if (!found && w.getWidgetType() == AEAspectWidgetCustomizable.TIME_STM && (w.getInput() == null
								|| !(w.getInput() instanceof Vector<?>)
								|| ((Vector<?>) w.getInput()).isEmpty()))
						{
							w.setInput(tStm);
							found = true;
						}
						lastWidget = w;
					}
					if (!found && lastWidget != null)
					{
						lastWidget.setInput(tStm);
					}
				}
			}
			else
			{
				_aspect.setTimeDim(new TimeDim());
			}

			//references
			if (_aspect.getValidation() != null)
			{
				AEAspectWidgetCustomizable lastWidget = null;
				for (ValidationStm vStm : _aspect.getValidation().getValidationStms())
				{
					boolean found = false;
					
					for (AEAspectWidgetCustomizable w : _widgets)
					{
						if (!found && w.getWidgetType() == AEAspectWidgetCustomizable.REFERENCE && (w.getInput() == null
								|| !(w.getInput() instanceof Vector<?>)
								|| ((Vector<?>) w.getInput()).isEmpty()))
						{
							w.setInput(vStm);
							lastWidget = w;
							found = true;
						}
					}
					if (!found && lastWidget != null)
					{
						lastWidget.setInput(vStm);
					}
				}
			}
			else
			{
				_aspect.setValidation(new Validation());
			}
			// create default input
			for (AEAspectWidgetCustomizable w : _widgets)
			{
				if (w.getInput() == null
						|| !(w.getInput() instanceof Vector<?>)
						|| ((Vector<?>) w.getInput()).isEmpty())
				{
					createDefaultInput(w, _notificationDOM);
				}
			}
		}
		else
		{
			for (AEAspectWidgetCustomizable w : _widgets)
			{
				w.clearInput();
			}

		}

	}

	public void createDefaultInput(AEAspectWidgetCustomizable w, Document notificationDOM2)
	{
		
		if (w.getConfigTemplate() != null && w.getWidgetType() < 4 && w.getConfigTemplate().getElement() != null)
		{
			try
			{
				Element e = notificationDOM2.createElement(w.getConfigTemplate().getElement());
				if (w.getConfigTemplate().getType() != null)
				{
					e.setAttribute("type", w.getConfigTemplate().getType());
				}
				if (w.getConfigTemplate().getSubtype() != null)
				{
					e.setAttribute("subtype", w.getConfigTemplate().getSubtype());
				}
				if (w.getConfigTemplate().getRole() != null)
				{
					e.setAttribute("role", w.getConfigTemplate().getRole());
				}
				if (_owningObject != null)
				{
					e.setAttribute("ana", _owningObject.getPdrId().toString());
				}
				w.setInput(e);
			}
			catch (Exception ee)
			{
//				System.out.println("widget element " + w.getConfigTemplate().getElement());
				ee.printStackTrace();
			}

		}
		else if (w.getWidgetType() == AEAspectWidgetCustomizable.RELATION_DEFINED)
		{
			Relation relation = new Relation();
			relation.setProvider(Platform
					.getPreferencesService()
					.getString(CommonActivator.PLUGIN_ID, "PRIMARY_RELATION_PROVIDER",
							AEConstants.RELATION_CLASSIFICATION_PROVIDER, null).toUpperCase());
			if (w.getConfigTemplate().getElement() != null)
			{
				relation.setContext(w.getConfigTemplate().getElement());
			}
			if (w.getConfigTemplate().getType() != null)
			{
				relation.setRClass(w.getConfigTemplate().getType());
			}
			if (w.getConfigTemplate().getSubtype() != null)
			{
				relation.setRelation(w.getConfigTemplate().getSubtype());
			}
			
			w.setInput(relation);
		}
		else if (w.getWidgetType() == AEAspectWidgetCustomizable.RELATION)
		{
			Relation relation = new Relation();
			relation.setProvider(Platform
					.getPreferencesService()
					.getString(CommonActivator.PLUGIN_ID, "PRIMARY_RELATION_PROVIDER",
							AEConstants.RELATION_CLASSIFICATION_PROVIDER, null).toUpperCase());
			
			w.setInput(relation);
		}
		else if (w.getWidgetType() == AEAspectWidgetCustomizable.TIME_STM)
		{
			TimeStm tStm = new TimeStm();
			tStm.setType("defined");
			w.setInput(tStm);
		}
		else if (w.getWidgetType() == AEAspectWidgetCustomizable.REFERENCE)
		{
			ValidationStm validationStm = new ValidationStm();
			validationStm.setReference(new Reference());
			validationStm.getReference().setQuality("certain");
			_aspect.getValidation().getValidationStms().add(validationStm);
			w.setInput(validationStm);
		}
		
	}

	private String parseEscapedChars(String suffix)
	{
		String response = suffix.replace("\\n", "\n");
		return response;
	}

	public void setOwningObject(PdrObject owningObject)
	{
		_owningObject = owningObject;

	}

	public void addWidget(AEAspectWidgetCustomizable widget)
	{
		_widgets.add(widget);

		Collections.sort(_widgets);
		widget.addSelectionListener(_selectionListener);

	}

	public final void addSelectionListener(final SelectionListener selectionListener)
	{
		if (selectionListener != null)
		{
			_selectionListeners.add(selectionListener);
		}
		

	}
	@Override
	public boolean isDirty()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isValid()
	{
		validateInternal();
		return _isValid;
	}

	@Override
	public void setDirty(boolean isDirty)
	{

		if (_aspect != null && isDirty)
		{

			_aspect.setDirty(true);
		}

	}

	@Override
	public void setSelected(boolean isSelected, boolean contextIsValid)
	{
		if (_widgets != null)
		{
			for (AEAspectWidgetCustomizable w : _widgets)
			{
				if (!w.isDisposed())
				{
					w.setSelected(isSelected, contextIsValid);
				}
			}
		}
		if (isSelected && contextIsValid)
		{
			setBackground(AEVIEWConstants.VIEW_BACKGROUND_SELECTED_COLOR);
			setForeground(AEVIEWConstants.VIEW_FOREGROUND_SELECTED_COLOR);
		}
		else if (contextIsValid)
		{
			setBackground(AEVIEWConstants.VIEW_BACKGROUND_DESELECTED_COLOR);
			setForeground(AEVIEWConstants.VIEW_FOREGROUND_DESELECTED_COLOR);
		}
		else if (isSelected)
		{
			setBackground(AEVIEWConstants.VIEW_BACKGROUND_INVALID_COLOR);
			setForeground(AEVIEWConstants.VIEW_FOREGROUND_SELECTED_COLOR);
		}
		else
		{
			setForeground(AEVIEWConstants.VIEW_FOREGROUND_DESELECTED_COLOR);
			setBackground(AEVIEWConstants.VIEW_BACKGROUND_INVALID_COLOR);
		}

	}
	private void validateInternal()
	{
		boolean valid = true;
		for (AEAspectWidgetCustomizable widget : _widgets)
		{
			if (widget.getConfigTemplate() != null && widget.getConfigTemplate().isRequired())
			{
				if (!widget.isValid())
				{
					valid = false;
					break;
				}
			}
		}
		setValid(valid);
	}

	
	private void setValid(boolean isValid)
	{

		this._isValid = isValid;

	}

	@Override
	public void validate()
	{
		validateInternal();

	}

	@Override
	public void saveInput()
	{
		// System.out.println("saveInput _notificationDOM");

		if (_aspect != null)
		{
		setDirty(true);
		try
		{
			if (_notificationDOM != null)
			{
				Collections.sort(_widgets);
				Node lastElement = null;
				Element notificationElement = (Element) _notificationDOM.getElementsByTagName("notification").item(0);
				for (AEAspectWidgetCustomizable w : _widgets)
				{
						if (w.getWidgetType() < 4 && w.getInput() instanceof Vector<?> && !w.getInput().isEmpty()
							&& w.getInput().firstElement() instanceof Element)
					{
						for (Object o : w.getInput())
						{
							Element e = (Element) o;
							String prefix = null;
							String suffix = null;
							if (e.getTextContent() != null && e.getTextContent().trim().length() > 0
									|| e.getOwnerDocument().equals(_notificationDOM.getOwnerDocument()))
							{
								if (w.getConfigTemplate() != null && w.getConfigTemplate().getPrefix() != null
										&& w.getConfigTemplate().getPrefix().length() > 0)
								{
									prefix = w.getConfigTemplate().getPrefix();
									prefix = parseEscapedChars(prefix);
								}
								if (w.getConfigTemplate() != null && w.getConfigTemplate().getSuffix() != null
										&& w.getConfigTemplate().getSuffix().length() > 0)
								{
									suffix = w.getConfigTemplate().getSuffix();
									suffix = parseEscapedChars(suffix);
								}
								if (e.getParentNode() == null || !e.getParentNode().equals(notificationElement))
								{
									insertAfterElement(e, lastElement, notificationElement, prefix, suffix);
								}
								else
								{
										// nachträglich prefix einbauen
									// String prefix = null;
									// if (w.getConfigTemplate() != null &&
									// w.getConfigTemplate().getPrefix() != null
									// &&
									// w.getConfigTemplate().getPrefix().length()
									// > 0)
									// {
									// prefix =
									// w.getConfigTemplate().getPrefix();
									//
									// }
									// if (prefix != null)
									// {
									// if (notificationElement.getLastChild() !=
									// null
									// &&
									// notificationElement.getLastChild().getTextContent()
									// != null
									// &&
									// !notificationElement.getLastChild().getTextContent().equals(prefix))
									// {
									// notificationElement.getLastChild().setTextContent(
									// notificationElement.getLastChild().getTextContent()
									// + prefix);
									// }
									// }
								}
							}
							if (suffix != null && e.getNextSibling() != null
									&& e.getNextSibling().getNodeType() == Document.TEXT_NODE
									&& e.getNextSibling().getTextContent() != null
									&& e.getNextSibling().getTextContent().contains(suffix))
							{
								lastElement = e.getNextSibling();
							}
							else
							{
								lastElement = e;
							}
						}

					}
					else if (w.getWidgetType() == AEAspectWidgetCustomizable.RELATION_DEFINED || w.getWidgetType() == AEAspectWidgetCustomizable.RELATION)
					{
						for (Object o : w.getInput())
						{
							boolean found = false;
							if (o instanceof Relation)
							{
								Relation rel = (Relation) o;
								if (_aspect.getRelationDim() != null)
								{
									for (RelationStm rtm : _aspect.getRelationDim().getRelationStms())
									{
										if (rtm.getRelations().contains(rel))
										{
											found = true;
										}
									}
								}
								if (!found && rel.isValid())
								{
									RelationStm relStm = new RelationStm();
									if (_owningObject != null)
									{
										relStm.setSubject(_owningObject.getPdrId());
									}
									relStm.getRelations().add(rel);
									_aspect.getRelationDim().getRelationStms().add(relStm);
								}
							}
						}
						
					}
					else if (w.getWidgetType() == AEAspectWidgetCustomizable.TIME_STM)
					{
						for (Object o : w.getInput())
						{
							boolean found = false;
							if (o instanceof TimeStm)
							{
								TimeStm tStm = (TimeStm) o;
								if (_aspect.getTimeDim() != null || _aspect.getTimeDim().getTimeStms().contains(tStm))
								{
									found = true;
									
								}
								if (!found && tStm.isValid())
								{
									
									_aspect.getTimeDim().getTimeStms().add(tStm);
								}
							}
						}
						
					}
				}

			}
			_aspect.setNotificationAsDOM(_notificationDOM);
		}
		catch (TransformerException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		// _parentEditor.saveInput();
	}

	private void insertAfterElement(Element e, Node lastElement, Element notificationElement, String prefix, String suffix)
	{

		if (lastElement != null && lastElement.getNextSibling() != null
				&& !lastElement.getNextSibling().equals(notificationElement))
		{
			if (prefix != null)
			{
				if (lastElement.getNodeType() == Document.TEXT_NODE
						&& (lastElement.getTextContent() == null || !lastElement.getTextContent().endsWith(
								prefix.trim())))
				{
					lastElement.setTextContent(removeDoubleWhiteSpaces(lastElement.getTextContent() + prefix));
				}
				else if (lastElement.getNodeType() != Document.TEXT_NODE)
				{
					lastElement = insertTextNodeAfter(lastElement, prefix, notificationElement);
				}
			}
			else if (lastElement.getNodeType() != Document.TEXT_NODE)
			{
				lastElement = insertTextNodeAfter(lastElement, " ", notificationElement);
			}
			try
			{
				notificationElement.insertBefore(e, lastElement.getNextSibling());
			}
			catch (DOMException ex)
			{
				ex.printStackTrace();
				notificationElement.appendChild(e);
				if (suffix != null)
				{
					insertTextNodeAfter(e, suffix, notificationElement);
					suffix = null;
				}
			}
			if (suffix != null)
			{
				insertTextNodeAfter(e, suffix, notificationElement);
			}

		}
		else
		{

			if (prefix != null)
			{
				if (notificationElement.getLastChild() == null
						|| notificationElement.getLastChild().getTextContent() == null
						|| !notificationElement.getLastChild().getTextContent().equals(prefix))
				{
					insertTextNodeAfter(null, prefix, notificationElement);

				}
			}
			else if (notificationElement.getFirstChild() != null)
			// if node to insert is not the very first content of notification
			{
				insertTextNodeAfter(null, " ", notificationElement);
			}
			notificationElement.appendChild(e);
			if (suffix != null)
			{
				insertTextNodeAfter(e, suffix, notificationElement);
			}
		}

	}

	private Node insertTextNodeAfter(Node lastElement, String prefix, Element notificationElement)
	{
		Node textNode;
		String insertString = null;
		if (!prefix.equals(" "))
		{
			insertString = " " + prefix.trim() + " ";
		}
		else
		{
			insertString = " ";
		}
		if (lastElement != null && lastElement.getNextSibling() != null)
		{
			if (lastElement.getNextSibling().getNodeType() == Document.TEXT_NODE)
			{
				textNode = notificationElement.getOwnerDocument().createTextNode(insertString);
				notificationElement.insertBefore(textNode, lastElement.getNextSibling());
			}
			else
			{
				textNode = notificationElement.getOwnerDocument().createTextNode(insertString);
				notificationElement.appendChild(textNode);
			}
		}
		else
		{
			textNode = notificationElement.getOwnerDocument().createTextNode(insertString);
			notificationElement.appendChild(textNode);
		}
		return textNode;
	}

	public void removeMarkupNode(AEAspectWidgetCustomizable w, Element el)
	{
		// checking for preffixes
		Node previousSib = el.getPreviousSibling();
		if (previousSib != null)
		{
			// checking for suffixes
			if (w.getConfigTemplate() != null && w.getConfigTemplate().getPrefix() != null
					&& w.getConfigTemplate().getPrefix().length() > 0)
			{
				String prefix = w.getConfigTemplate().getPrefix();
				prefix = parseEscapedChars(prefix);
				if (previousSib.getNodeType() == Node.TEXT_NODE && previousSib.getTextContent().contains(prefix))
				{
					previousSib.setTextContent(previousSib.getTextContent().replace(prefix, ""));
					previousSib.setTextContent(removeDoubleWhiteSpaces(previousSib.getTextContent()));
				}
			}
		}

		Node followerEl = el.getNextSibling();
		if (followerEl != null)
		{
			// checking for suffixes
			if (w.getConfigTemplate() != null && w.getConfigTemplate().getSuffix() != null
					&& w.getConfigTemplate().getSuffix().length() > 0)
			{
				String suffix = w.getConfigTemplate().getSuffix();
				if (suffix.equals("\\n") && followerEl.getTextContent() != null
						&& followerEl.getTextContent().equals(" "))
				{

					if (followerEl.getNextSibling() != null && followerEl.getNextSibling().getNodeName().equals("lb"))
					{
						followerEl.getParentNode().removeChild(followerEl.getNextSibling());
					}
					if (followerEl.getParentNode() != null)
					{
						followerEl.getParentNode().removeChild(followerEl);
					}
				}
				else if (followerEl.getNodeType() == Node.TEXT_NODE && followerEl.getTextContent().contains(suffix))
				{
					followerEl.setTextContent(followerEl.getTextContent().replace(suffix, ""));
					followerEl.setTextContent(removeDoubleWhiteSpaces(followerEl.getTextContent()));

				}
			}
		}
		while (followerEl != null && followerEl.getNodeType() == Node.TEXT_NODE)
		{
			followerEl.setTextContent(removeDoubleWhiteSpaces(followerEl.getTextContent()));
			followerEl = followerEl.getNextSibling();
		}
		if (el.getParentNode() != null)
		{
			el.getParentNode().removeChild(el);
		}

	}

	private String removeDoubleWhiteSpaces(String string)
	{
		Pattern ws = Pattern.compile("[ \\t\\x0b\\r\\f]{2,}");
		Matcher m = ws.matcher(string);
		string = m.replaceAll(" ");
		return string;
	}


	public void removeValidationStm(
			AEAspectWidgetCustomizable aeAspectWidgetCustomizable,
			ValidationStm vStm)
	{
		_aspect.getValidation().getValidationStms().remove(vStm);
		
	}


	@Override
	public void setInput(Object input) {
		if (input instanceof Aspect)
		{
			setAspect((Aspect)input);
			validateInternal();
		}
		else
		{
			setAspect(null);
		}
		
	}


	public void removeRelation(
			AEAspectWidgetCustomizable aeAspectWidgetCustomizable, Relation relation) {
		for (RelationStm rtm : _aspect.getRelationDim().getRelationStms())
		{
			if (rtm.getRelations().contains(relation))
			{
				rtm.getRelations().remove(relation);
			}
		}
		
	}

	@Override
	public void dispose()
	{
		super.dispose();
		_selectionListeners.clear();
	}


	@Override
	public void setEditable(boolean editable) {
		this._editable = editable;
		if (_widgets != null)
		{
			for (AEAspectWidgetCustomizable w : _widgets)
			{
				if (!w.isDisposed())
				{
					w.setEditable(_editable);
				}
			}
		}
	}

}
