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
package org.bbaw.pdr.ae.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.StartElement;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.bbaw.pdr.ae.metamodel.PdrDate;
import org.bbaw.pdr.ae.metamodel.PdrId;
import org.w3c.dom.Document;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * The Class Aspect.
 * @author Christoph Plutte
 */
public class Aspect extends PdrObject implements Cloneable
{

	/** The notification. */
	private String _notification;

	/** The spatial dim. */
	private SpatialDim _spatialDim;

	/** The time dim. */
	private TimeDim _timeDim;

	/** The relation dim. */
	private RelationDim _relationDim;

	/** The semantic dim. */
	private SemanticDim _semanticDim;

	/** The validation. */
	private Validation _validation;

	/** The range list. */
	private LinkedList<TaggingRange> _rangeList;

	/**
	 * Instantiates a new aspect.
	 * @param pdrId the pdr id
	 */
	public Aspect(final PdrId pdrId)
	{
		super(pdrId);
	}

	/**
	 * Instantiates a new aspect.
	 * @param idString the id string
	 */
	public Aspect(final String idString)
	{
		super(idString);
	}

	/**
	 * @return cloned aspect.
	 * @see org.bbaw.pdr.ae.model.PdrObject#clone()
	 */
	@Override
	public final Aspect clone()
	{
		Aspect clone = (Aspect) super.clone();
		if (this._notification != null)
		{
			clone._notification = new String(this.getNotification());
		}

		if (this._rangeList != null)
		{
			clone._rangeList = new LinkedList<TaggingRange>();
			for (int i = 0; i < this._rangeList.size(); i++)
			{
				clone._rangeList.add(this._rangeList.get(i).clone());
			}
		}

		if (this._relationDim != null)
		{
			clone._relationDim = this._relationDim.clone();
		}
		if (this._semanticDim != null)
		{
			clone._semanticDim = this._semanticDim.clone();
		}
		if (this._spatialDim != null)
		{
			clone._spatialDim = this._spatialDim.clone();
		}
		if (this._timeDim != null)
		{
			clone._timeDim = this._timeDim.clone();
		}
		if (this._validation != null)
		{
			clone._validation = this._validation.clone();
		}

		clone._displayName = null;
		return clone;
	}

	/**
	 * Equals.
	 * @param o Object
	 * @return true, if successful
	 */
	@Override
	public final boolean equals(final Object o)
	{
		if (o != null && o instanceof Aspect)
		{
			Aspect a = (Aspect) o;
			if (this.getPdrId().equals(a.getPdrId()))
			{
				return true;
			}
		}
		return false;

	}

	/**
	 * Equals content.
	 * @param aspect the aspect
	 * @return true, if successful
	 */
	public final boolean equalsContent(final Aspect aspect)
	{
		if (this.getNotification() != null && aspect.getNotification() != null)
		{
			if (!this.getNotification().equals(aspect.getNotification()))
			{
				return false;
			}
		}
		else if ((this.getNotification() != null && aspect.getNotification() == null)
				|| (this.getNotification() == null && aspect.getNotification() != null))
		{
			return false;
		}

		if (this.getRangeList() != null && aspect.getRangeList() != null)
		{
			if (!(this.getRangeList().size() == aspect.getRangeList().size()))
			{
				return false;
			}
			for (int i = 0; i < this.getRangeList().size(); i++)
			{
				if (!this.getRangeList().get(i).equalsContent(aspect.getRangeList().get(i)))
				{
					return false;
				}
			}
		}
		else if ((this.getRangeList() != null && aspect.getRangeList() == null)
				|| (this.getRangeList() == null && aspect.getRangeList() != null))
		{
			return false;
		}

		if (this.getSpatialDim() != null && aspect.getSpatialDim() != null)
		{
			if (!this.getSpatialDim().equals(aspect.getSpatialDim()))
			{
				return false;
			}
		}
		else if ((this.getSpatialDim() != null && aspect.getSpatialDim() == null)
				|| (this.getSpatialDim() == null && aspect.getSpatialDim() != null))
		{
			return false;
		}

		if (this.getTimeDim() != null && aspect.getTimeDim() != null)
		{
			if (!this.getTimeDim().equals(aspect.getTimeDim()))
			{
				return false;
			}
		}
		else if ((this.getTimeDim() != null && aspect.getTimeDim() == null)
				|| (this.getTimeDim() == null && aspect.getTimeDim() != null))
		{
			return false;
		}

		if (this.getSemanticDim() != null && aspect.getSemanticDim() != null)
		{
			if (!this.getSemanticDim().equals(aspect.getSemanticDim()))
			{
				return false;
			}
		}
		else if ((this.getSemanticDim() != null && aspect.getSemanticDim() == null)
				|| (this.getSemanticDim() == null && aspect.getSemanticDim() != null))
		{
			return false;
		}

		if (this.getValidation() != null && aspect.getValidation() != null)
		{
			if (!this.getValidation().equals(aspect.getValidation()))
			{
				return false;
			}
		}
		else if ((this.getValidation() != null && aspect.getValidation() == null)
				|| (this.getValidation() == null && aspect.getValidation() != null))
		{
			return false;
		}

		return true;
	}

	/**
	 * Gets the notification.
	 * @return the notification
	 */
	public final String getNotification()
	{
		return _notification;
	}

	/**
	 * Get PdrId of owning object. That is the object to which this Aspect has a
	 * "aspect_of" relation. If this Aspect has more than one of those relations
	 * only the first owning object ID is returned. If this Aspect has no such
	 * relation the first RelationStm-Subject ID which is not equal to the Aspect
	 * ID is return. If this Aspect has no RelationStm null is returned.
	 * @return PdrId of owning object or null if none.
	 */
	public PdrId getOwningObjectId()
	{
		if (this.getRelationDim() != null && this.getRelationDim().getRelationStms() != null)
		{
			for (RelationStm relStm : this.getRelationDim().getRelationStms())
			{
				if (relStm.getSubject() != null)
				{
					if (relStm.getSubject().equals(this.getPdrId()))
					{
						if (relStm.getRelations() != null)
						{
							for (Relation r : relStm.getRelations())
							{
								if (r.getRelation() != null && r.getRelation().equals("aspect_of"))
								{
									return r.getObject();
								}
							}
						}
					}
					else
					{
						return relStm.getSubject();
					}
				}
			}
		}
		return null;
	}

	/**
	 * Gets the range list.
	 * @return the range list
	 */
	public final LinkedList<TaggingRange> getRangeList()
	{
		if (_rangeList == null)
		{
			_rangeList = new LinkedList<TaggingRange>();
		}
		return _rangeList;
	}

	/**
	 * Gets the relation dim.
	 * @return the relation dim
	 */
	public final RelationDim getRelationDim()
	{
		return _relationDim;
	}

	/**
	 * Gets the semantic dim.
	 * @return the semantic dim
	 */
	public final SemanticDim getSemanticDim()
	{
		return _semanticDim;
	}

	/**
	 * Gets the spatial dim.
	 * @return the spatial dim
	 */
	public final SpatialDim getSpatialDim()
	{
		return _spatialDim;
	}

	/**
	 * Gets the time dim.
	 * @return the time dim
	 */
	public final TimeDim getTimeDim()
	{
		return _timeDim;
	}

	/**
	 * Gets the validation.
	 * @return the validation
	 */
	public final Validation getValidation()
	{
		return _validation;
	}

	/**
	 * Checks if is valid.
	 * @return true, if is valid
	 */
	public final boolean isValid()
	{
		if (isValidNotification())
		{
			if (_semanticDim != null && _relationDim != null && _spatialDim != null && _timeDim != null
					&& _validation != null)
			{
				if (_semanticDim.isValid() && _relationDim.isValid() && _spatialDim.isValid() && _timeDim.isValid()
						&& _validation.isValid())
				{
					return true;
				}
				else
				{
					return false;
				}
			}
			else
			{
				return false;
			}
		}
		return false;
	}

	/**
	 * Checks if is valid notification.
	 * @return true, if is valid notification
	 */
	public final boolean isValidNotification()
	{
		if (_notification != null && _notification.trim().length() > 0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * Sets the notification.
	 * @param notification the new notification
	 */
	public final void setNotification(final String notification)
	{
		this._notification = notification;
	}

	/**
	 * Sets the range list.
	 * @param rangeList the new range list
	 */
	public final void setRangeList(final LinkedList<TaggingRange> rangeList)
	{
		this._rangeList = rangeList;
	}

	/**
	 * Sets the relation dim.
	 * @param relationDim the new relation dim
	 */
	public final void setRelationDim(final RelationDim relationDim)
	{
		this._relationDim = relationDim;
	}

	/**
	 * Sets the semantic dim.
	 * @param semanticDim the new semantic dim
	 */
	public final void setSemanticDim(final SemanticDim semanticDim)
	{
		this._semanticDim = semanticDim;
	}

	/**
	 * Sets the spatial dim.
	 * @param spatialDim the new spatial dim
	 */
	public final void setSpatialDim(final SpatialDim spatialDim)
	{
		this._spatialDim = spatialDim;
	}

	/**
	 * Sets the time dim.
	 * @param timeDim the new time dim
	 */
	public final void setTimeDim(final TimeDim timeDim)
	{
		this._timeDim = timeDim;
	}

	/**
	 * Sets the validation.
	 * @param validation the new validation
	 */
	public final void setValidation(final Validation validation)
	{
		this._validation = validation;

	}

	/**
	 * Similar relations.
	 * @param aspect the aspect
	 * @param id1 the id1
	 * @param id2 the id2
	 * @return true, if successful
	 */
	public final boolean similarRelations(final Aspect aspect, final PdrId id1, final PdrId id2)
	{
		if (this.getRelationDim() != null && aspect.getRelationDim() != null)
		{
			if (!this.getRelationDim().similarRelations(aspect.getRelationDim(), this.getPdrId(), aspect.getPdrId(),
					id1, id2))
			{
				return false;
			}
		}
		else if ((this.getRelationDim() != null && aspect.getRelationDim() == null)
				|| (this.getRelationDim() == null && aspect.getRelationDim() != null))
		{
			return false;
		}

		return true;
	}

	public final String getNotificationAsXML() throws XMLStreamException
	{
		XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
		// Create XMLEventWriter

		ByteArrayOutputStream bout = new ByteArrayOutputStream();

		XMLEventWriter eventWriter = outputFactory.createXMLEventWriter(bout);

		// Create a EventFactory
		XMLEventFactory eventFactory = XMLEventFactory.newInstance();
		// Create and write Start Tag
		StartDocument startDocument = eventFactory.createStartDocument("UTF-8");
		eventWriter.add(startDocument);
		StartElement startElement = eventFactory.createStartElement("", "", "notification");
		eventWriter.add(startElement);

		int start = 0;
		// System.out.println("notifi " + a.getNotification());
		// FIXME!!!!!!!!!!!!!!!!
		if (this.getNotification() != null && this.getRangeList() != null)
		{
			// System.out.println("RangeListe hat TaggingListen, Zahl: " +
			// a.getRangeList().size());
			for (int i = 0; i < this.getRangeList().size(); i++)
			{
				if (this.getNotification().length() > start
						&& this.getNotification().length() >= this.getRangeList().get(i).getStart()
						&& start <= this.getRangeList().get(i).getStart())
				{
					String subText = this.getNotification().substring(start, this.getRangeList().get(i).getStart());
					processAppendTextWithLineBreaks(eventWriter, subText);

					createNode(eventWriter, this.getRangeList().get(i), this.getNotification());
					start = this.getRangeList().get(i).getStart() + this.getRangeList().get(i).getLength();
				}
			}
			if (this.getNotification().length() > start)
			{
				String subText = this.getNotification().substring(start);
				processAppendTextWithLineBreaks(eventWriter, subText);

			}
		}
		else if (this.getNotification() != null)
		{
			processAppendTextWithLineBreaks(eventWriter, this.getNotification());

		}
		eventWriter.add(eventFactory.createEndElement("", "", "notification"));
		eventWriter.add(eventFactory.createEndDocument());
		eventWriter.close();
		String xml = bout.toString();
		Pattern p = Pattern.compile("<\\?xml version=\"1.0\"\\?>");
		Matcher m = p.matcher(xml);
		xml = m.replaceAll("<\\?xml version=\"1.0\" encoding=\"UTF-8\" \\?>");
		// System.out.println("notification als string: " + xml);
		return xml;

	}
	
	public final String getNotificationAsHTML() throws XMLStreamException
	{
		XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
		// Create XMLEventWriter

		ByteArrayOutputStream bout = new ByteArrayOutputStream();

		XMLEventWriter eventWriter = outputFactory.createXMLEventWriter(bout);

		// Create a EventFactory
		XMLEventFactory eventFactory = XMLEventFactory.newInstance();
		// Create and write Start Tag
		StartDocument startDocument = eventFactory.createStartDocument("UTF-8");
		eventWriter.add(startDocument);
		StartElement startElement = eventFactory.createStartElement("", "", "p");
		eventWriter.add(startElement);

		int start = 0;
		if (this.getNotification() != null && this.getRangeList() != null)
		{
			for (int i = 0; i < this.getRangeList().size(); i++)
			{
				if (this.getNotification().length() > start
						&& this.getNotification().length() >= this.getRangeList().get(i).getStart()
						&& start <= this.getRangeList().get(i).getStart())
				{
					String subText = this.getNotification().substring(start, this.getRangeList().get(i).getStart());
					processAppendTextWithParagraph(eventWriter, subText);

					createHTMLNode(eventWriter, this.getRangeList().get(i), this.getNotification());
					start = this.getRangeList().get(i).getStart() + this.getRangeList().get(i).getLength();
				}
			}
			if (this.getNotification().length() > start)
			{
				String subText = this.getNotification().substring(start);
				processAppendTextWithParagraph(eventWriter, subText);

			}
		}
		else if (this.getNotification() != null)
		{
			processAppendTextWithLineBreaks(eventWriter, this.getNotification());

		}
		eventWriter.add(eventFactory.createEndElement("", "", "p"));
		eventWriter.add(eventFactory.createEndDocument());
		eventWriter.close();
		String html = bout.toString();
		html = html.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "");
		 System.out.println("notification als html string: " + html);
		return html;

	}

	private void createHTMLNode(XMLEventWriter eventWriter,
			TaggingRange tr, String notification) throws XMLStreamException {
		XMLEventFactory eventFactory = XMLEventFactory.newInstance();

		// Create Start node
		StartElement sElement = eventFactory.createStartElement("", "", "span" );
		eventWriter.add(sElement);
		eventWriter.add(eventFactory.createAttribute("class", tr.getName()));
		
		String titleAttr = "";
		if (tr.getType() != null && tr.getType().length() > 0)
		{
			titleAttr = "type:" + tr.getType();
		}
		if (tr.getSubtype() != null && tr.getSubtype().length() > 0)
		{
			titleAttr +=",subtype:" + tr.getSubtype();
		}
		if (tr.getRole() != null && tr.getRole().trim().length() > 0)
		{
			titleAttr += ",role:" + tr.getRole();
		}
		if (tr.getFrom() != null)
		{
			titleAttr += ",from:" + tr.getFrom().toString();
		}
		if (tr.getTo() != null)
		{
			titleAttr += ",to:" + tr.getTo().toString();
		}
		if (tr.getWhen() != null)
		{
			titleAttr += ",when:" + tr.getWhen().toString();
		}
		if (tr.getNotBefore() != null)
		{
			titleAttr += ",notBefore:" + tr.getNotBefore().toString();
		}
		if (tr.getNotAfter() != null)
		{
			titleAttr += ",notAfter:" + tr.getNotAfter().toString();
		}
		if (tr.getKey() != null && tr.getKey().trim().length() > 0)
		{
			titleAttr += ",key:" + tr.getKey();
		}
		if (tr.getAna() != null && tr.getAna().trim().length() > 0)
		{
			titleAttr += ",ana:" + tr.getAna();
		}
		if (titleAttr.length() > 0)
		{
			eventWriter.add(eventFactory.createAttribute("title", titleAttr));
		}
		// FIXME Workaround
		notification = notification + " ";
		// System.out.println("injester, tr.start " + tr.getStart() + " ln " +
		// tr.getLength());
		String subText = notification.substring(tr.getStart(), Math.min(tr.getStart() + tr.getLength(), notification.length()));
		Characters characters = eventFactory.createCharacters(subText);
		eventWriter.add(characters);

		EndElement eElement = eventFactory.createEndElement("", "", "span");
		eventWriter.add(eElement);
		
	}

	private void processAppendTextWithParagraph(XMLEventWriter eventWriter,
			String subText) throws XMLStreamException {
		XMLEventFactory eventFactory = XMLEventFactory.newInstance();

		if (subText.contains("\n"))
		{
			String[] subs = subText.split("\\n");
			for (int i = 0; i < subs.length; i++)
			{
				Characters characters = eventFactory.createCharacters(subs[i]);
				eventWriter.add(characters);

				if (i < subs.length - 1)
				{
					
					eventWriter.add(eventFactory.createEndElement("", "", "p"));
					
					StartElement startElement = eventFactory.createStartElement("", "", "p");
					eventWriter.add(startElement);

				}

			}
		}
		else
		{
			Characters characters = eventFactory.createCharacters(subText);
			eventWriter.add(characters);
		}
		
	}

	/**
	 * Process append text with line breaks.
	 * @param eventWriter the event writer
	 * @param subText the sub text
	 * @throws XMLStreamException the xML stream exception
	 */
	private void processAppendTextWithLineBreaks(final XMLEventWriter eventWriter, final String subText)
			throws XMLStreamException
	{
		XMLEventFactory eventFactory = XMLEventFactory.newInstance();

		if (subText.contains("\n"))
		{
			String[] subs = subText.split("\\n");
			for (int i = 0; i < subs.length; i++)
			{
				Characters characters = eventFactory.createCharacters(subs[i]);
				eventWriter.add(characters);

				if (i < subs.length - 1)
				{
					StartElement startElement = eventFactory.createStartElement("", "", "lb");
					eventWriter.add(startElement);

					eventWriter.add(eventFactory.createEndElement("", "", "lb"));
				}

			}
		}
		else
		{
			Characters characters = eventFactory.createCharacters(subText);
			eventWriter.add(characters);
		}
	}

	/**
	 * Creates the node.
	 * @param eventWriter the event writer
	 * @param tr the tagging range
	 * @param text the text
	 * @throws XMLStreamException the xML stream exception
	 */
	private void createNode(final XMLEventWriter eventWriter, final TaggingRange tr, String text)
			throws XMLStreamException
	{
		XMLEventFactory eventFactory = XMLEventFactory.newInstance();

		// Create Start node
		StartElement sElement = eventFactory.createStartElement("", "", tr.getName());
		eventWriter.add(sElement);
		if (tr.getType() != null && tr.getType().length() > 0)
		{
			eventWriter.add(eventFactory.createAttribute("type", tr.getType()));
		}
		if (tr.getSubtype() != null && tr.getSubtype().length() > 0)
		{
			eventWriter.add(eventFactory.createAttribute("subtype", tr.getSubtype()));
		}
		if (tr.getRole() != null && tr.getRole().length() > 0)
		{
			eventWriter.add(eventFactory.createAttribute("role", tr.getRole()));
		}
		if (tr.getAna() != null && tr.getAna().length() > 0)
		{
			eventWriter.add(eventFactory.createAttribute("ana", tr.getAna()));
		}

		if (tr.getKey() != null && tr.getKey().length() > 0)
		{
			eventWriter.add(eventFactory.createAttribute("key", tr.getKey()));
		}
		if (tr.getWhen() != null)
		{
			eventWriter.add(eventFactory.createAttribute("when", tr.getWhen().toString()));
		}
		if (tr.getFrom() != null)
		{
			eventWriter.add(eventFactory.createAttribute("from", (tr.getFrom().toString())));
		}
		if (tr.getTo() != null)
		{
			eventWriter.add(eventFactory.createAttribute("to", tr.getTo().toString()));
		}
		if (tr.getNotBefore() != null)
		{
			eventWriter.add(eventFactory.createAttribute("notBefore", tr.getNotBefore().toString()));
		}
		if (tr.getNotAfter() != null)
		{
			eventWriter.add(eventFactory.createAttribute("notAfter", tr.getNotAfter().toString()));
		}

		// FIXME Workaround
		text = text + " ";
		// System.out.println("injester, tr.start " + tr.getStart() + " ln " +
		// tr.getLength());
		String subText = text.substring(tr.getStart(), Math.min(tr.getStart() + tr.getLength(), text.length()));
		Characters characters = eventFactory.createCharacters(subText);
		eventWriter.add(characters);

		EndElement eElement = eventFactory.createEndElement("", "", tr.getName());
		eventWriter.add(eElement);
	}

	public final Document getNotificationAsDOM() throws ParserConfigurationException, SAXException, IOException,
			XMLStreamException
	{
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();

		DocumentBuilder builder = builderFactory.newDocumentBuilder();
		InputStream is = new ByteArrayInputStream(this.getNotificationAsXML().getBytes("UTF-8"));
		Document document = builder.parse(is);
		return document;
	}

	public final void setNotificationAsXML(String notificationAsXML)
	{
		SAXParserFactory factory = SAXParserFactory.newInstance();
		try
		{
			InputStream xmlInput = new ByteArrayInputStream(notificationAsXML.getBytes("UTF-8"));
			SAXParser saxParser = factory.newSAXParser();
			NotificationSaxHandler handler = new NotificationSaxHandler();
			saxParser.parse(xmlInput, handler);

		}
		catch (Throwable err)
		{
			err.printStackTrace();
		}
	}

	public final void setNotificationAsDOM(Document notificationAsDOM) throws TransformerException, IOException
	{
		TransformerFactory factory = TransformerFactory.newInstance();
		Transformer transformer = factory.newTransformer();
		StringWriter writer = new StringWriter();
		Result result = new StreamResult(writer);
		Source source = new DOMSource(notificationAsDOM);
		transformer.transform(source, result);
		writer.close();
		String xml = writer.toString();
		setNotificationAsXML(xml);
	}

	// /////inner class
	private class NotificationSaxHandler extends DefaultHandler
	{
		public NotificationSaxHandler()
		{

		}

		/** The tr. */
		private TaggingRange _tr = null;

		/** string size. */
		private static final int STRING_SIZE = 1000;
		/** The sb. */
		private StringBuilder _sb = new StringBuilder(STRING_SIZE);

		/** The range list. */
		private LinkedList<TaggingRange> _rangeList = null;

		/** The str. */
		private String _str;

		/** The noti b. */
		private boolean _notiB = false;

		/** The noti text b. */
		private boolean _notiTextB = false;

		/** The pers name b. */
		private boolean _persNameB = false;

		/** The org name b. */
		private boolean _orgNameB = false;

		/** The place name b. */
		private boolean _placeNameB = false;

		/** The name b. */
		private boolean _nameB = false;

		/** The date b. */
		private boolean _dateB = false;

		/**
		 * @param ch chars
		 * @param start start
		 * @param len length of chars
		 * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
		 */
		@Override
		public final void characters(final char[] ch, final int start, final int len)
		{

			// notification
			if (_notiB && _notiTextB)
			{
				_str = new String(ch, start, len);
				// if (_str.startsWith(",") || _str.startsWith(".") ||
				// _str.startsWith(";") || _str.startsWith(":")
				// || _str.startsWith("-") || _str.startsWith("'") ||
				// _str.startsWith("\""))
				// {
				// _sb.append(_str);
				// }
				// else if (_sb.toString().endsWith("\n") ||
				// _sb.toString().endsWith(" ") || _str.startsWith(" ")
				// || _str.startsWith("\n"))
				// {
				// _sb.append(_str);
				// }
				// else
				// {
				// _sb.append(" ");
				// _sb.append(_str);
				// }
				_sb.append(_str);
				_notiTextB = false;
				// new String(ch, start, len).length();
				// notification = notification + "</notification>";
				// a.setNotification(notification);
				// notification = "<notification>";
				// }
			}
			// persName
			else if (_persNameB)
			{

				_tr.setStart(_tr.getStart());
				_tr.setLength(len);

				_sb.append(new String(ch, start, len));
				_tr.setTextValue(new String(ch, start, len));
				_rangeList.add(_tr);

				_persNameB = false;
			}
			// orgName
			else if (_orgNameB)
			{

				_tr.setStart(_tr.getStart());
				_tr.setLength(len);

				_sb.append(new String(ch, start, len));
				_tr.setTextValue(new String(ch, start, len));
				_rangeList.add(_tr);

				_orgNameB = false;
			}
			// placeName
			else if (_placeNameB)
			{

				_tr.setStart(_tr.getStart());
				_tr.setLength(len);

				_sb.append(new String(ch, start, len));
				_tr.setTextValue(new String(ch, start, len));
				_rangeList.add(_tr);

				_placeNameB = false;
			}
			// name
			else if (_nameB)
			{

				_tr.setStart(_tr.getStart());
				_tr.setLength(len);

				_sb.append(new String(ch, start, len));
				_tr.setTextValue(new String(ch, start, len));
				_rangeList.add(_tr);

				_nameB = false;
			}
			// date
			else if (_dateB)
			{

				_tr.setStart(_tr.getStart());
				_tr.setLength(len);

				_sb.append(new String(ch, start, len));
				_tr.setTextValue(new String(ch, start, len));
				_rangeList.add(_tr);

				_dateB = false;
			}
		}

		/**
		 * @param u uri
		 * @param name localName
		 * @param qn QName
		 * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String,
		 *      java.lang.String, java.lang.String)
		 */
		@Override
		public final void endElement(final String u, final String name, final String qn)
		{

			if (name.equals("notification") || name.equals("aodl:notification") || qn.equals("notification")
					|| qn.equals("aodl:notification"))
			{
				_notiB = false;
				Aspect.this.setRangeList(_rangeList);
				Aspect.this.setNotification(_sb.toString());
			}
			else if (name.equals("persName") || name.equals("aodl:persName") || qn.equals("persName")
					|| qn.equals("aodl:persName"))
			{
				_notiTextB = true;
				_persNameB = false;
			}
			// orgName
			else if (name.equals("orgName") || name.equals("aodl:orgName") || qn.equals("orgName")
					|| qn.equals("aodl:orgName"))
			{
				_orgNameB = false;
				_notiTextB = true;
			}
			// placeName
			else if (name.equals("placeName") || name.equals("aodl:placeName") || qn.equals("placeName")
					|| qn.equals("aodl:placeName"))
			{
				_placeNameB = false;
				_notiTextB = true;
			}
			// name
			else if (name.equals("name") || name.equals("aodl:name") || qn.equals("name") || qn.equals("aodl:name"))
			{
				_nameB = false;
				_notiTextB = true;
			}
			// date
			else if (name.equals("date") || name.equals("aodl:date") || qn.equals("date") || qn.equals("aodl:date"))
			{
				_dateB = false;
				_notiTextB = true;
			}

		}

		/**
		 * @param u uri
		 * @param name local name
		 * @param qn QName
		 * @param a attributes
		 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String,
		 *      java.lang.String, java.lang.String, org.xml.sax.Attributes)
		 */
		@Override
		public final void startElement(final String u, final String name, final String qn, final Attributes a)
				throws SAXException
		{

			if (name.equals("notification") || name.equals("aodl:notification") || qn.equals("notification")
					|| qn.equals("aodl:notification"))
			{
				_notiB = true;
				_notiTextB = true;
				_sb = new StringBuilder(STRING_SIZE);
				_rangeList = new LinkedList<TaggingRange>();
			}
			else if (name.equals("lb") || name.equals("aodl:lb") || qn.equals("lb") || qn.equals("aodl:lb"))
			{
				_sb.append("\n");
				_notiTextB = true;
			}
			else if (name.equals("persName") || name.equals("aodl:persName") || qn.equals("persName")
					|| qn.equals("aodl:persName"))
			{
				_persNameB = true;
				_notiTextB = false;
				_tr = new TaggingRange();
				_tr.setName("persName");
				_tr.setStart(_sb.length());
				for (int i = 0; i < a.getLength(); i++)
				{
					if (a.getQName(i).equals("type"))
					{
						_tr.setType(a.getValue(i));
					}
					else if (a.getQName(i).equals("subtype"))
					{
						_tr.setSubtype(a.getValue(i));
					}
					else if (a.getQName(i).equals("role"))
					{
						_tr.setRole(a.getValue(i));
					}
					else if (a.getQName(i).equals("ana"))
					{
						_tr.setAna(a.getValue(i));
					}
					else if (a.getQName(i).equals("key"))
					{
						_tr.setKey(a.getValue(i));
					}
				}
			}
			// orgName
			else if (name.equals("orgName") || name.equals("aodl:orgName") || qn.equals("orgName")
					|| qn.equals("aodl:orgName"))
			{
				_orgNameB = true;
				_notiTextB = false;
				_tr = new TaggingRange();
				_tr.setName("orgName");
				_tr.setStart(_sb.length());
				for (int i = 0; i < a.getLength(); i++)
				{
					if (a.getQName(i).equals("type"))
					{
						_tr.setType(a.getValue(i));
					}
					else if (a.getQName(i).equals("subtype"))
					{
						_tr.setSubtype(a.getValue(i));
					}
					else if (a.getQName(i).equals("role"))
					{
						_tr.setRole(a.getValue(i));
					}
					else if (a.getQName(i).equals("ana"))
					{
						_tr.setAna(a.getValue(i));
					}
					else if (a.getQName(i).equals("key"))
					{
						_tr.setKey(a.getValue(i));
					}
				}
			}
			// placeName
			else if (name.equals("placeName") || name.equals("aodl:placeName") || qn.equals("placeName")
					|| qn.equals("aodl:placeName"))
			{
				_placeNameB = true;
				_notiTextB = false;
				_tr = new TaggingRange();
				_tr.setName("placeName");
				_tr.setStart(_sb.length());
				for (int i = 0; i < a.getLength(); i++)
				{
					if (a.getQName(i).equals("type"))
					{
						_tr.setType(a.getValue(i));
					}
					else if (a.getQName(i).equals("subtype"))
					{
						_tr.setSubtype(a.getValue(i));
					}
					else if (a.getQName(i).equals("role"))
					{
						_tr.setRole(a.getValue(i));
					}
					else if (a.getQName(i).equals("ana"))
					{
						_tr.setAna(a.getValue(i));
					}
					else if (a.getQName(i).equals("key"))
					{
						_tr.setKey(a.getValue(i));
					}
				}
			}
			// name
			else if (name.equals("name") || name.equals("aodl:name") || qn.equals("name") || qn.equals("aodl:name"))
			{
				_nameB = true;
				_notiTextB = false;
				_tr = new TaggingRange();
				_tr.setName("name");
				_tr.setStart(_sb.length());
				for (int i = 0; i < a.getLength(); i++)
				{
					if (a.getQName(i).equals("type"))
					{
						_tr.setType(a.getValue(i));
					}
					else if (a.getQName(i).equals("subtype"))
					{
						_tr.setSubtype(a.getValue(i));
					}
					else if (a.getQName(i).equals("role"))
					{
						_tr.setRole(a.getValue(i));
					}
					else if (a.getQName(i).equals("ana"))
					{
						_tr.setAna(a.getValue(i));
					}
					else if (a.getQName(i).equals("key"))
					{
						_tr.setKey(a.getValue(i));
					}
				}
			}
			// date
			else if (name.equals("date") || name.equals("aodl:date") || qn.equals("date") || qn.equals("aodl:date"))
			{
				_dateB = true;
				_notiTextB = false;
				_tr = new TaggingRange();
				_tr.setName("date");
				_tr.setStart(_sb.length());
				for (int i = 0; i < a.getLength(); i++)
				{
					if (a.getQName(i).equals("type"))
					{
						_tr.setType(a.getValue(i));
					}
					else if (a.getQName(i).equals("subtype"))
					{
						_tr.setSubtype(a.getValue(i));
					}
					else if (a.getQName(i).equals("role"))
					{
						_tr.setRole(a.getValue(i));
					}
					else if (a.getQName(i).equals("ana"))
					{
						_tr.setAna(a.getValue(i));
					}
					else if (a.getQName(i).equals("when"))
					{
						_tr.setWhen(new PdrDate(a.getValue(i)));
					}
					else if (a.getQName(i).equals("from"))
					{
						_tr.setFrom(new PdrDate(a.getValue(i)));
					}
					else if (a.getQName(i).equals("to"))
					{
						_tr.setTo(new PdrDate(a.getValue(i)));
					}
					else if (a.getQName(i).equals("notBefore"))
					{
						_tr.setNotBefore(new PdrDate(a.getValue(i)));
					}
					else if (a.getQName(i).equals("notAfter"))
					{
						_tr.setNotAfter(new PdrDate(a.getValue(i)));
					}
				}
			}

		}
	}
}
