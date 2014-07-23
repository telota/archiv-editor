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
package org.bbaw.pdr.ae.control.core;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.bbaw.pdr.ae.common.AEConstants;
import org.bbaw.pdr.ae.control.facade.Facade;
import org.bbaw.pdr.ae.control.internal.Activator;
import org.bbaw.pdr.ae.metamodel.PdrId;
import org.bbaw.pdr.ae.metamodel.PdrMetaObject;
import org.bbaw.pdr.ae.metamodel.Record;
import org.bbaw.pdr.ae.metamodel.Revision;
import org.bbaw.pdr.ae.model.Aspect;
import org.bbaw.pdr.ae.model.Concurrence;
import org.bbaw.pdr.ae.model.Identifier;
import org.bbaw.pdr.ae.model.NameMods;
import org.bbaw.pdr.ae.model.OriginInfo;
import org.bbaw.pdr.ae.model.PartMods;
import org.bbaw.pdr.ae.model.PdrObject;
import org.bbaw.pdr.ae.model.Person;
import org.bbaw.pdr.ae.model.Place;
import org.bbaw.pdr.ae.model.Reference;
import org.bbaw.pdr.ae.model.ReferenceMods;
import org.bbaw.pdr.ae.model.RelatedItem;
import org.bbaw.pdr.ae.model.Relation;
import org.bbaw.pdr.ae.model.RelationStm;
import org.bbaw.pdr.ae.model.SemanticStm;
import org.bbaw.pdr.ae.model.SpatialStm;
import org.bbaw.pdr.ae.model.TaggingRange;
import org.bbaw.pdr.ae.model.TimeStm;
import org.bbaw.pdr.ae.model.ValidationStm;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

/**
 * The Class ExportXMLProcessor write PDRObjects to xml like the XMLProcessor
 * but writes out full names that means that the xml is not valid according to
 * PDR standards but human readably.
 * @author Christoph Plutte
 */
public class ExportXMLProcessor implements XMLProcessorInterface
{

	/** administrative date format. */
	private SimpleDateFormat _adminDateFormat = org.bbaw.pdr.ae.common.AEConstants.ADMINDATE_FORMAT;

	/** date encoding type. */
	private final String _encoding = "iso8601";

	/** end event. */
	private XMLEvent _end;
	/** Logger. */
	private static ILog iLogger = AEConstants.ILOGGER;
	/** status. */
	private IStatus _log;


	/** singleton instance of Facade. */
	private Facade _facade = Facade.getInstanz();

	/**
	 * create Node.
	 * @param eventWriter writer
	 * @param name name of node
	 * @param c concurrence
	 * @param person 
	 * @throws XMLStreamException exc.
	 */
	private void createNode(final XMLEventWriter eventWriter, final String name, final Concurrence c, PdrMetaObject person)
			throws XMLStreamException
	{
		XMLEventFactory eventFactory = XMLEventFactory.newInstance();
		XMLEvent end = eventFactory.createCharacters("\n");
		XMLEvent tab = eventFactory.createCharacters("\t");
		// Create Start node
		StartElement sElement = eventFactory.createStartElement("podl", "http://pdr.bbaw.de/namespaces/podl/", name);
		eventWriter.add(tab);
		eventWriter.add(sElement);
		// FIXME nullpointer abfangen!!1
		PdrObject o = _facade.getPdrObject(c.getPersonId());
		if (o != null)
		{
			eventWriter.add(eventFactory.createAttribute("person", o.getDisplayName()));
		}
		else
		{
			eventWriter.add(eventFactory.createAttribute("person", c.getPersonId().toString()));
		}
		eventWriter.add(end);

		if (c.getReferences() != null)
		{
			for (int j = 0; j < c.getReferences().size(); j++)
			{
				createNode(eventWriter, "validationStm", c.getReferences().get(j), "podl",
						"http://pdr.bbaw.de/namespaces/podl/", person);
			}
		}
		// Create End node
		EndElement eElement = eventFactory.createEndElement("podl", "http://pdr.bbaw.de/namespaces/podl/", name);
		eventWriter.add(eElement);
		eventWriter.add(end);

	}

	// /**
	// * Creates the node.
	// * @param eventWriter the event writer
	// * @param name the name
	// * @param d the d
	// * @throws XMLStreamException the xML stream exception
	// */
	// private void createNode(XMLEventWriter eventWriter, String name,
	// HashMap<String, String> d)
	// throws XMLStreamException
	// {
	//
	// XMLEventFactory eventFactory = XMLEventFactory.newInstance();
	// XMLEvent end = eventFactory.createCharacters("\n");
	// XMLEvent tab = eventFactory.createCharacters("\t");
	// // Create Start node
	// StartElement sElement = eventFactory.createStartElement("", "", name);
	// eventWriter.add(tab);
	// eventWriter.add(sElement);
	// // FIXME nullpointer abfangen!!1
	// eventWriter.add(end);
	//
	// for (String str : d.keySet())
	// {
	//
	// createNode(eventWriter, "docPart", d.get(str), str);
	// }
	// if (d.isEmpty())
	// {
	// createNode(eventWriter, "docPart", "", "de");
	//
	// }
	// eventWriter.add(eventFactory.createEndElement("", "", name));
	// eventWriter.add(end);
	//
	// }

	/**
	 * Creates the node.
	 * @param eventWriter the event writer
	 * @param name the name
	 * @param i the i
	 * @throws XMLStreamException the xML stream exception
	 */
	private void createNode(final XMLEventWriter eventWriter, final String name, final Identifier i)
			throws XMLStreamException
	{
		XMLEventFactory eventFactory = XMLEventFactory.newInstance();
		XMLEvent end = eventFactory.createCharacters("\n");
		XMLEvent tab = eventFactory.createCharacters("\t");
		// Create Start node
		StartElement sElement = eventFactory.createStartElement("podl", "http://pdr.bbaw.de/namespaces/podl/", name);
		eventWriter.add(tab);
		eventWriter.add(sElement);
		if (i.getProvider() != null)
		{
			eventWriter.add(eventFactory.createAttribute("provider", i.getProvider()));
		}
		if (i.getQuality() != null)
		{
			eventWriter.add(eventFactory.createAttribute("quality", i.getQuality()));
		}
		if (i.getAuthority() != null)
		{
			eventWriter.add(eventFactory.createAttribute("authority", _facade.getObjectDisplayName(i.getAuthority())));
		}

		// Create Content
		Characters characters = eventFactory.createCharacters(i.getIdentifier());
		eventWriter.add(characters);
		// Create End node
		EndElement eElement = eventFactory.createEndElement("podl", "http://pdr.bbaw.de/namespaces/podl/", name);
		eventWriter.add(eElement);
		eventWriter.add(end);

	}

	/**
	 * Creates the node.
	 * @param eventWriter the event writer
	 * @param name the name
	 * @param nameMods the name mods
	 * @throws XMLStreamException the xML stream exception
	 */
	private void createNode(final XMLEventWriter eventWriter, final String name, final NameMods nameMods)
			throws XMLStreamException
	{
		XMLEventFactory eventFactory = XMLEventFactory.newInstance();
		XMLEvent end = eventFactory.createCharacters("\n");
		XMLEvent tab = eventFactory.createCharacters("\t");
		// Create Start node
		StartElement sElement = eventFactory.createStartElement("", "", "name");
		eventWriter.add(sElement);
		if (nameMods.getType() != null)
		{
			eventWriter.add(eventFactory.createAttribute("type", nameMods.getType()));
		}
		eventWriter.add(end);
		for (int i = 0; i < nameMods.getNameParts().size(); i++)
		{
			StartElement sE = eventFactory.createStartElement("", "", "namePart");
			eventWriter.add(tab);
			eventWriter.add(sE);
			if (nameMods.getNameParts().get(i).getType() != null)
			{
				eventWriter.add(eventFactory.createAttribute("type", nameMods.getNameParts().get(i).getType()));
			}
			Characters characters = eventFactory.createCharacters(nameMods.getNameParts().get(i).getNamePart());
			eventWriter.add(characters);

			eventWriter.add(eventFactory.createEndElement("", "", "namePart"));
			eventWriter.add(end);
		}
		if (nameMods.getAffiliation() != null)
		{
			StartElement sE = eventFactory.createStartElement("", "", "affiliation");
			eventWriter.add(tab);
			eventWriter.add(sE);

			Characters characters = eventFactory.createCharacters(nameMods.getAffiliation());
			eventWriter.add(characters);

			eventWriter.add(eventFactory.createEndElement("", "", "affiliation"));
			eventWriter.add(end);
		}
		if (nameMods.getRoleMods() != null)
		{
			StartElement sE = eventFactory.createStartElement("", "", "role");
			eventWriter.add(tab);
			eventWriter.add(sE);
			eventWriter.add(end);
			if (nameMods.getRoleMods().getRoleTerm() != null)
			{
				StartElement sE2 = eventFactory.createStartElement("", "", "roleTerm");
				eventWriter.add(tab);
				eventWriter.add(tab);

				eventWriter.add(sE2);

				if (nameMods.getRoleMods().getAuthority() != null)
				{
					eventWriter.add(eventFactory.createAttribute("authority", nameMods.getRoleMods().getAuthority()));
				}
				if (nameMods.getRoleMods().getType() != null)
				{
					eventWriter.add(eventFactory.createAttribute("type", nameMods.getRoleMods().getType()));
				}

				Characters characters = eventFactory.createCharacters(nameMods.getRoleMods().getRoleTerm());
				eventWriter.add(characters);

				eventWriter.add(eventFactory.createEndElement("", "", "roleTerm"));
				eventWriter.add(end);
			}

			eventWriter.add(tab);
			eventWriter.add(eventFactory.createEndElement("", "", "role"));
			eventWriter.add(end);
		}



		if (nameMods.getDescription() != null)
		{
			StartElement sE = eventFactory.createStartElement("", "", "description");
			eventWriter.add(tab);
			eventWriter.add(sE);

			Characters characters = eventFactory.createCharacters(nameMods.getDescription());
			eventWriter.add(characters);

			eventWriter.add(eventFactory.createEndElement("", "", "description"));
			eventWriter.add(end);
		}
		eventWriter.add(eventFactory.createEndElement("", "", "name"));
		eventWriter.add(end);

	}

	/**
	 * Creates the node.
	 * @param eventWriter the event writer
	 * @param name the name
	 * @param originInfo the origin info
	 * @throws XMLStreamException the xML stream exception
	 */
	private void createNode(final XMLEventWriter eventWriter, final String name, final OriginInfo originInfo)
			throws XMLStreamException
	{
		XMLEventFactory eventFactory = XMLEventFactory.newInstance();
		XMLEvent end = eventFactory.createCharacters("\n");
		XMLEvent tab = eventFactory.createCharacters("\t");
		// Create Start node
		StartElement sElement = eventFactory.createStartElement("", "", name);
		eventWriter.add(tab);
		eventWriter.add(sElement);
		eventWriter.add(end);

		if (originInfo.getDateCreated() != null)
		{
			StartElement sE = eventFactory.createStartElement("", "", "dateCreated");
			eventWriter.add(sE);
			eventWriter.add(eventFactory.createAttribute("encoding", _encoding));

			Characters characters = eventFactory.createCharacters(originInfo.getDateCreated().toString());
			eventWriter.add(characters);

			eventWriter.add(eventFactory.createEndElement("", "", "dateCreated"));
			eventWriter.add(end);
		}
		if (originInfo.getDateCreatedTimespan() != null)
		{
			if (originInfo.getDateCreatedTimespan().getDateFrom() != null)
			{
				StartElement sE = eventFactory.createStartElement("", "", "dateCreated");
				eventWriter.add(sE);
				eventWriter.add(eventFactory.createAttribute("encoding", _encoding));
				eventWriter.add(eventFactory.createAttribute("point", "start"));

				Characters characters = eventFactory.createCharacters(originInfo.getDateCreatedTimespan().getDateFrom()
						.toString());
				eventWriter.add(characters);

				eventWriter.add(eventFactory.createEndElement("", "", "dateCreated"));
				eventWriter.add(end);
			}
			if (originInfo.getDateCreatedTimespan().getDateTo() != null)
			{
				StartElement sE = eventFactory.createStartElement("", "", "dateCreated");
				eventWriter.add(sE);
				eventWriter.add(eventFactory.createAttribute("encoding", _encoding));
				eventWriter.add(eventFactory.createAttribute("point", "end"));

				Characters characters = eventFactory.createCharacters(originInfo.getDateCreatedTimespan().getDateTo()
						.toString());
				eventWriter.add(characters);

				eventWriter.add(eventFactory.createEndElement("", "", "dateCreated"));
				eventWriter.add(end);
			}
		}
		if (originInfo.getDateIssued() != null)
		{
			StartElement sE = eventFactory.createStartElement("", "", "getDateIssued");
			eventWriter.add(sE);
			eventWriter.add(eventFactory.createAttribute("encoding", _encoding));

			Characters characters = eventFactory.createCharacters(originInfo.getDateIssued().toString());
			eventWriter.add(characters);

			eventWriter.add(eventFactory.createEndElement("", "", "getDateIssued"));
			eventWriter.add(end);
		}
		if (originInfo.getDateIssuedTimespan() != null)
		{
			if (originInfo.getDateIssuedTimespan().getDateFrom() != null)
			{
				StartElement sE = eventFactory.createStartElement("", "", "getDateIssued");
				eventWriter.add(sE);
				eventWriter.add(eventFactory.createAttribute("encoding", _encoding));
				eventWriter.add(eventFactory.createAttribute("point", "start"));

				Characters characters = eventFactory.createCharacters(originInfo.getDateIssuedTimespan().getDateFrom()
						.toString());
				eventWriter.add(characters);

				eventWriter.add(eventFactory.createEndElement("", "", "getDateIssued"));
				eventWriter.add(end);
			}
			if (originInfo.getDateIssuedTimespan().getDateTo() != null)
			{
				StartElement sE = eventFactory.createStartElement("", "", "getDateIssued");
				eventWriter.add(sE);
				eventWriter.add(eventFactory.createAttribute("encoding", _encoding));
				eventWriter.add(eventFactory.createAttribute("point", "end"));

				Characters characters = eventFactory.createCharacters(originInfo.getDateIssuedTimespan().getDateTo()
						.toString());
				eventWriter.add(characters);

				eventWriter.add(eventFactory.createEndElement("", "", "getDateIssued"));
				eventWriter.add(end);
			}
		}
		if (originInfo.getDateCaptured() != null)
		{
			StartElement sE = eventFactory.createStartElement("", "", "dateCaptured");
			eventWriter.add(tab);
			eventWriter.add(sE);
			eventWriter.add(eventFactory.createAttribute("encoding", _encoding));

			Characters characters = eventFactory.createCharacters(originInfo.getDateCaptured().toString());
			eventWriter.add(characters);

			eventWriter.add(eventFactory.createEndElement("", "", "dateCaptured"));
			eventWriter.add(end);
		}
		if (originInfo.getDateCapturedTimespan() != null)
		{
			if (originInfo.getDateCapturedTimespan().getDateFrom() != null)
			{
				StartElement sE = eventFactory.createStartElement("", "", "dateCaptured");
				eventWriter.add(tab);
				eventWriter.add(sE);
				eventWriter.add(eventFactory.createAttribute("encoding", _encoding));
				eventWriter.add(eventFactory.createAttribute("point", "start"));

				Characters characters = eventFactory.createCharacters(originInfo.getDateCapturedTimespan()
						.getDateFrom().toString());
				eventWriter.add(characters);

				eventWriter.add(eventFactory.createEndElement("", "", "dateCaptured"));
				eventWriter.add(end);
			}
			if (originInfo.getDateCapturedTimespan().getDateTo() != null)
			{
				StartElement sE = eventFactory.createStartElement("", "", "dateCaptured");
				eventWriter.add(tab);
				eventWriter.add(sE);
				eventWriter.add(eventFactory.createAttribute("encoding", _encoding));
				eventWriter.add(eventFactory.createAttribute("point", "end"));

				Characters characters = eventFactory.createCharacters(originInfo.getDateCapturedTimespan().getDateTo()
						.toString());
				eventWriter.add(characters);

				eventWriter.add(eventFactory.createEndElement("", "", "dateCaptured"));
				eventWriter.add(end);
			}
		}
		if (originInfo.getCopyrightDate() != null)
		{
			StartElement sE = eventFactory.createStartElement("", "", "copyrightDate");
			eventWriter.add(tab);
			eventWriter.add(sE);
			eventWriter.add(eventFactory.createAttribute("encoding", _encoding));

			Characters characters = eventFactory.createCharacters(originInfo.getCopyrightDate().toString());
			eventWriter.add(characters);

			eventWriter.add(eventFactory.createEndElement("", "", "copyrightDate"));
			eventWriter.add(end);
		}
		if (originInfo.getCopyrightDateTimespan() != null)
		{
			if (originInfo.getCopyrightDateTimespan().getDateFrom() != null)
			{
				StartElement sE = eventFactory.createStartElement("", "", "copyrightDate");
				eventWriter.add(tab);
				eventWriter.add(sE);
				eventWriter.add(eventFactory.createAttribute("encoding", _encoding));
				eventWriter.add(eventFactory.createAttribute("point", "start"));

				Characters characters = eventFactory.createCharacters(originInfo.getCopyrightDateTimespan()
						.getDateFrom().toString());
				eventWriter.add(characters);

				eventWriter.add(eventFactory.createEndElement("", "", "copyrightDate"));
				eventWriter.add(end);
			}
			if (originInfo.getCopyrightDateTimespan().getDateTo() != null)
			{
				StartElement sE = eventFactory.createStartElement("", "", "copyrightDate");
				eventWriter.add(tab);
				eventWriter.add(sE);
				eventWriter.add(eventFactory.createAttribute("encoding", _encoding));
				eventWriter.add(eventFactory.createAttribute("point", "end"));

				Characters characters = eventFactory.createCharacters(originInfo.getCopyrightDateTimespan().getDateTo()
						.toString());
				eventWriter.add(characters);

				eventWriter.add(eventFactory.createEndElement("", "", "copyrightDate"));
				eventWriter.add(end);
			}
		}

		if (originInfo.getPublisher() != null)
		{
			StartElement sE = eventFactory.createStartElement("", "", "publisher");
			eventWriter.add(tab);
			eventWriter.add(sE);

			Characters characters = eventFactory.createCharacters(originInfo.getPublisher());
			eventWriter.add(characters);

			eventWriter.add(eventFactory.createEndElement("", "", "publisher"));
			eventWriter.add(end);
		}

		if (originInfo.getPlaceTerm() != null)
		{
			StartElement sE = eventFactory.createStartElement("", "", "place");
			eventWriter.add(tab);
			eventWriter.add(sE);
			eventWriter.add(end);

			StartElement sE2 = eventFactory.createStartElement("", "", "placeTerm");
			eventWriter.add(tab);
			eventWriter.add(tab);

			eventWriter.add(sE2);
			eventWriter.add(eventFactory.createAttribute("type", originInfo.getPlaceType()));

			Characters characters = eventFactory.createCharacters(originInfo.getPlaceTerm());
			eventWriter.add(characters);
			eventWriter.add(eventFactory.createEndElement("", "", "placeTerm"));
			eventWriter.add(end);
			eventWriter.add(tab);

			eventWriter.add(eventFactory.createEndElement("", "", "place"));
			eventWriter.add(end);
		}
		if (originInfo.getEdition() != null)
		{
			StartElement sE = eventFactory.createStartElement("", "", "edition");
			eventWriter.add(tab);
			eventWriter.add(sE);

			Characters characters = eventFactory.createCharacters(originInfo.getEdition());
			eventWriter.add(characters);

			eventWriter.add(eventFactory.createEndElement("", "", "edition"));
			eventWriter.add(end);
		}

		eventWriter.add(eventFactory.createEndElement("", "", name));
		eventWriter.add(end);
	}

	/**
	 * Creates the node.
	 * @param eventWriter the event writer
	 * @param name the name
	 * @param part the part
	 * @throws XMLStreamException the xML stream exception
	 */
	private void createNode(final XMLEventWriter eventWriter, final String name, final PartMods part)
			throws XMLStreamException
	{
		XMLEventFactory eventFactory = XMLEventFactory.newInstance();
		XMLEvent end = eventFactory.createCharacters("\n");
		XMLEvent tab = eventFactory.createCharacters("\t");
		// Create Start node
		StartElement sElement = eventFactory.createStartElement("", "", "part");
		eventWriter.add(sElement);

		eventWriter.add(end);
		if (part.getDetails() != null)
		{
			for (int i = 0; i < part.getDetails().size(); i++)
			{
				StartElement sE = eventFactory.createStartElement("", "", "detail");
				eventWriter.add(tab);
				eventWriter.add(sE);
				if (part.getDetails().get(i).getType() != null)
				{
					eventWriter.add(eventFactory.createAttribute("type", part.getDetails().get(i).getType()));
				}
				if (part.getDetails().get(i).getNumber() != null)
				{
					sElement = eventFactory.createStartElement("", "", "number");
					eventWriter.add(sElement);
					eventWriter.add(end);
					Characters characters = eventFactory.createCharacters(part.getDetails().get(i).getNumber());
					eventWriter.add(characters);

					eventWriter.add(eventFactory.createEndElement("", "", "number"));
					eventWriter.add(end);

				}
				if (part.getDetails().get(i).getCaption() != null)
				{
					sElement = eventFactory.createStartElement("", "", "caption");
					eventWriter.add(sElement);
					eventWriter.add(end);
					Characters characters = eventFactory.createCharacters(part.getDetails().get(i).getCaption());
					eventWriter.add(characters);

					eventWriter.add(eventFactory.createEndElement("", "", "caption"));
					eventWriter.add(end);

				}
				eventWriter.add(eventFactory.createEndElement("", "", "detail"));
				eventWriter.add(end);
			}
		}

		if (part.getExtendsMods() != null)
		{
			for (int i = 0; i < part.getExtendsMods().size(); i++)
			{
				StartElement sE = eventFactory.createStartElement("", "", "extent");
				eventWriter.add(tab);
				eventWriter.add(sE);
				if (part.getDetails().get(i).getType() != null)
				{
					eventWriter.add(eventFactory.createAttribute("unit", part.getExtendsMods().get(i).getUnit()));
				}
				if (part.getExtendsMods().get(i).getStart() != null)
				{
					sElement = eventFactory.createStartElement("", "", "start");
					eventWriter.add(sElement);
					eventWriter.add(end);
					Characters characters = eventFactory.createCharacters(part.getExtendsMods().get(i).getStart());
					eventWriter.add(characters);

					eventWriter.add(eventFactory.createEndElement("", "", "start"));
					eventWriter.add(end);

				}
				if (part.getExtendsMods().get(i).getEnd() != null)
				{
					sElement = eventFactory.createStartElement("", "", "end");
					eventWriter.add(sElement);
					eventWriter.add(end);
					Characters characters = eventFactory.createCharacters(part.getExtendsMods().get(i).getEnd());
					eventWriter.add(characters);

					eventWriter.add(eventFactory.createEndElement("", "", "end"));
					eventWriter.add(end);

				}
				eventWriter.add(eventFactory.createEndElement("", "", "extent"));
				eventWriter.add(end);
			}
		}

		if (part.getDates() != null && !part.getDates().isEmpty())
		{
			for (int i = 0; i < part.getDates().size(); i++)
			{
				StartElement sE = eventFactory.createStartElement("", "", "date");
				eventWriter.add(tab);
				eventWriter.add(sE);

				Characters characters = eventFactory.createCharacters(part.getDates().get(i).toString());
				eventWriter.add(characters);

				eventWriter.add(eventFactory.createEndElement("", "", "date"));
				eventWriter.add(end);
			}
		}

		eventWriter.add(eventFactory.createEndElement("", "", "part"));
		eventWriter.add(end);

	}

	/**
	 * Creates the node.
	 * @param eventWriter the event writer
	 * @param name the name
	 * @param place the place
	 * @throws XMLStreamException the xML stream exception
	 */
	private void createNode(final XMLEventWriter eventWriter, final String name, final Place place)
			throws XMLStreamException
	{
		XMLEventFactory eventFactory = XMLEventFactory.newInstance();
		XMLEvent end = eventFactory.createCharacters("\n");
		XMLEvent tab = eventFactory.createCharacters("\t");
		// Create Start node
		StartElement sElement = eventFactory.createStartElement("aodl", "http://pdr.bbaw.de/namespaces/aodl/", name);
		eventWriter.add(tab);
		eventWriter.add(sElement);

		if (place.getType() != null)
		{
			eventWriter.add(eventFactory.createAttribute("type",
					PDRConfigProvider.getLabelOfMarkup("placeName", place.getType(), null, null)));

		}
		if (place.getSubtype() != null)
		{
			eventWriter.add(eventFactory.createAttribute("subtype",
					PDRConfigProvider.getLabelOfMarkup("placeName", place.getType(), place.getSubtype(), null)));

		}
		if (place.getKey() != null)
		{
			eventWriter.add(eventFactory.createAttribute("key", place.getKey()));

		}
		// Create Content
		if (place.getPlaceName() != null)
		{
			Characters characters = eventFactory.createCharacters(place.getPlaceName());
			eventWriter.add(characters);

		}
		eventWriter.add(eventFactory.createEndElement("aodl", "http://pdr.bbaw.de/namespaces/aodl/", "place"));
		eventWriter.add(end);

	}

	// /**
	// * Creates the node.
	// * @param eventWriter the event writer
	// * @param name the name
	// * @param ref the ref
	// * @param prefix the prefix
	// * @param uri the uri
	// * @throws XMLStreamException the xML stream exception
	// */
	// private void createNode(XMLEventWriter eventWriter, String name,
	// Reference ref, String prefix, String uri)
	// throws XMLStreamException
	// {
	// XMLEventFactory eventFactory = XMLEventFactory.newInstance();
	// XMLEvent end = eventFactory.createCharacters("\n");
	// XMLEvent tab = eventFactory.createCharacters("\t");
	// // Create Start node
	// StartElement sElement = eventFactory.createStartElement(prefix, uri,
	// name);
	// eventWriter.add(tab);
	// eventWriter.add(sElement);
	// if (ref.getInternal() != null)
	// {
	// eventWriter.add(eventFactory.createAttribute("internal",
	// ref.getInternal()));
	// }
	// if (ref.getQuality() != null)
	// {
	// eventWriter.add(eventFactory.createAttribute("quality",
	// ref.getQuality()));
	// }
	// if (ref.getAuthority() != null)
	// {
	// eventWriter.add(eventFactory.createAttribute("authority",
	// ref.getAuthority().toString()));
	// }
	//
	// // Create Content
	// if (ref.getSourceId() != null)
	// {
	// Characters characters =
	// eventFactory.createCharacters(ref.getSourceId().toString());
	// eventWriter.add(characters);
	// }
	// // Create End node
	// EndElement eElement = eventFactory.createEndElement(prefix, uri, name);
	// eventWriter.add(eElement);
	// eventWriter.add(end);
	// }

	/**
	 * Creates the node.
	 * @param eventWriter the event writer
	 * @param name the name
	 * @param r the r
	 * @param prefix the prefix
	 * @param uri the uri
	 * @throws XMLStreamException the xML stream exception
	 */
	private void createNode(final XMLEventWriter eventWriter, final String name, final ReferenceMods r,
			final String prefix, final String uri) throws XMLStreamException
	{
		XMLEventFactory eventFactory = XMLEventFactory.newInstance();
		_end = eventFactory.createDTD("\n");
		// Create config open tag
		StartElement startElement = eventFactory.createStartElement("", "", "mods");
		eventWriter.add(startElement);

		// TODO
		eventWriter.add(eventFactory.createAttribute("displayName", r.getDisplayName()));

		// eventWriter.add(eventFactory.createAttribute("xmlns:xsi",
		// "http://www.w3.org/2001/XMLSchema-instance"));
		// eventWriter.add(eventFactory.createAttribute("xsi:schemaLocation",
		// "http://pdr.bbaw.de/namespaces/rodl/ http://pdr.bbaw.de/schema/rodl_mods.xsd"));
		if (!r.getPdrId().getType().equals("genre"))
		{
			eventWriter.add(eventFactory.createAttribute("ID", r.getPdrId().toString()));
		}

		eventWriter.add(_end);

		// Write the different nodes

		createReferenceChildren(eventWriter, r);

		if (r.getRecord() != null)
		{
			createNodeNoNamespace(eventWriter, "recordInfo", r.getRecord());
		}
		eventWriter.add(eventFactory.createEndElement("", "", "mods"));
		eventWriter.add(_end);

	}

	/**
	 * Creates the node.
	 * @param eventWriter the event writer
	 * @param name the name
	 * @param relation the relation
	 * @param relationStm 
	 * @throws XMLStreamException the xML stream exception
	 */
	private void createNode(final XMLEventWriter eventWriter, final String name, final Relation relation, RelationStm relationStm)
			throws XMLStreamException
	{
		XMLEventFactory eventFactory = XMLEventFactory.newInstance();
		XMLEvent end = eventFactory.createCharacters("\n");
		XMLEvent tab = eventFactory.createCharacters("\t");
		// Create Start node
		StartElement sElement = eventFactory.createStartElement("aodl", "http://pdr.bbaw.de/namespaces/aodl/", name);
		eventWriter.add(tab);
		eventWriter.add(sElement);

		if (relation.getObject() != null)
		{
			PdrObject o = _facade.getPdrObject(relation.getObject());
			if (o != null)
			{
				eventWriter.add(eventFactory.createAttribute("object", o.getDisplayName()));
			}
			else
			{
				eventWriter.add(eventFactory.createAttribute("object", relation.getObject().toString()));
			}

		}
		else
		{
			eventWriter.add(eventFactory.createAttribute("object", relationStm.getSubject().toString()));

		}
		if (relation.getProvider() != null)
		{
			eventWriter.add(eventFactory.createAttribute("provider", relation.getProvider()));

		}

		if (relation.getContext() != null)
		{
			eventWriter.add(eventFactory.createAttribute("context",
					PDRConfigProvider.getLabelOfRelation(relation.getProvider(), relation.getContext(), null, null)));

		}
		if (relation.getRClass() != null)
		{
			eventWriter.add(eventFactory.createAttribute(
					"class",
					PDRConfigProvider.getLabelOfRelation(relation.getProvider(), relation.getContext(),
							relation.getRClass(), null)));

		}
		// Create Content
		if (relation.getRelation() != null)
		{
			Characters characters = eventFactory.createCharacters(PDRConfigProvider.getLabelOfRelation(
					relation.getProvider(), relation.getContext(), relation.getRClass(), relation.getRelation()));
			eventWriter.add(characters);

		}
		eventWriter.add(eventFactory.createEndElement("aodl", "http://pdr.bbaw.de/namespaces/aodl/", "relation"));
		eventWriter.add(end);

	}

	/**
	 * Creates the node.
	 * @param eventWriter the event writer
	 * @param name the name
	 * @param relStm the rel stm
	 * @throws XMLStreamException the xML stream exception
	 */
	private void createNode(final XMLEventWriter eventWriter, final String name, final RelationStm relStm)
			throws XMLStreamException
	{
		XMLEventFactory eventFactory = XMLEventFactory.newInstance();
		XMLEvent end = eventFactory.createCharacters("\n");
		XMLEvent tab = eventFactory.createCharacters("\t");
		// Create Start node
		StartElement sElement = eventFactory.createStartElement("aodl", "http://pdr.bbaw.de/namespaces/aodl/", name);
		eventWriter.add(tab);
		eventWriter.add(sElement);
		if (relStm.getSubject() != null)
		{
			PdrObject o = _facade.getPdrObject(relStm.getSubject());
			if (o != null)
			{
				eventWriter.add(eventFactory.createAttribute("subject", o.getDisplayName()));
			}
			else
			{
				eventWriter.add(eventFactory.createAttribute("subject", relStm.getSubject().toString()));
			}
		}

		// Create Content
		if (relStm.getRelations() != null)
		{
			for (int j = 0; j < relStm.getRelations().size(); j++)
			{
				createNode(eventWriter, "relation", relStm.getRelations().get(j), relStm);

			}

		}
		// Create End node
		EndElement eElement = eventFactory.createEndElement("aodl", "http://pdr.bbaw.de/namespaces/aodl/", name);
		eventWriter.add(eElement);
		eventWriter.add(end);

	}

	/**
	 * Creates the node.
	 * @param eventWriter the event writer
	 * @param name the name
	 * @param r the r
	 * @param prefix the prefix
	 * @param uri the uri
	 * @throws XMLStreamException the xML stream exception
	 */
	private void createNode(final XMLEventWriter eventWriter, final String name, final Revision r, final String prefix,
			final String uri) throws XMLStreamException
	{
		XMLEventFactory eventFactory = XMLEventFactory.newInstance();
		XMLEvent end = eventFactory.createCharacters("\n");
		XMLEvent tab = eventFactory.createCharacters("\t");
		// Create Start node
		StartElement sElement = eventFactory.createStartElement(prefix, uri, name);
		eventWriter.add(tab);
		eventWriter.add(sElement);
		eventWriter.add(eventFactory.createAttribute("ref", new Integer(r.getRef()).toString()));
		eventWriter
				.add(eventFactory.createAttribute("timestamp", _adminDateFormat.format(r.getTimeStamp()).toString()));

		eventWriter.add(eventFactory.createAttribute("authority", _facade.getObjectDisplayName(r.getAuthority())));

		// TODO das Schema sieht keinen authority namen mehr vor, änderung im
		// model umsetzen.
		// // Create Content
		// Characters characters =
		// eventFactory.createCharacters(r.getRevisor());
		// eventWriter.add(characters);
		// Create End node
		EndElement eElement = eventFactory.createEndElement("aodl", "http://pdr.bbaw.de/namespaces/aodl/", name);
		eventWriter.add(eElement);
		eventWriter.add(end);
	}

	/**
	 * Creates the node.
	 * @param eventWriter the event writer
	 * @param name the name
	 * @param semStm the sem stm
	 * @throws XMLStreamException the xML stream exception
	 */
	private void createNode(final XMLEventWriter eventWriter, final String name, final SemanticStm semStm)
			throws XMLStreamException
	{
		XMLEventFactory eventFactory = XMLEventFactory.newInstance();
		XMLEvent end = eventFactory.createCharacters("\n");
		XMLEvent tab = eventFactory.createCharacters("\t");
		// Create Start node
		StartElement sElement = eventFactory.createStartElement("aodl", "http://pdr.bbaw.de/namespaces/aodl/", name);
		eventWriter.add(tab);
		eventWriter.add(sElement);
		if (semStm.getProvider() != null)
		{
			eventWriter.add(eventFactory.createAttribute("provider", semStm.getProvider()));
		}

		// Create Content
		if (semStm.getLabel() != null)
		{
			Characters characters = eventFactory.createCharacters(PDRConfigProvider.getSemanticLabel(
					semStm.getProvider(), semStm.getLabel()));
			eventWriter.add(characters);

		}
		// Create End node
		EndElement eElement = eventFactory.createEndElement("aodl", "http://pdr.bbaw.de/namespaces/aodl/", name);
		eventWriter.add(eElement);
		eventWriter.add(end);

	}

	/**
	 * Creates the node.
	 * @param eventWriter the event writer
	 * @param name the name
	 * @param spaStm the spa stm
	 * @throws XMLStreamException the xML stream exception
	 */
	private void createNode(final XMLEventWriter eventWriter, final String name, final SpatialStm spaStm)
			throws XMLStreamException
	{
		XMLEventFactory eventFactory = XMLEventFactory.newInstance();
		XMLEvent end = eventFactory.createCharacters("\n");
		XMLEvent tab = eventFactory.createCharacters("\t");
		// Create Start node
		StartElement sElement = eventFactory.createStartElement("aodl", "http://pdr.bbaw.de/namespaces/aodl/", name);
		eventWriter.add(tab);
		eventWriter.add(sElement);
		if (spaStm.getType() != null)
		{
			eventWriter.add(eventFactory.createAttribute("type", spaStm.getType()));
		}

		// Create Content
		if (spaStm.getPlaces() != null)
		{
			for (int j = 0; j < spaStm.getPlaces().size(); j++)
			{
				createNode(eventWriter, "place", spaStm.getPlaces().get(j));

			}

		}
		// Create End node
		EndElement eElement = eventFactory.createEndElement("aodl", "http://pdr.bbaw.de/namespaces/aodl/", name);
		eventWriter.add(eElement);
		eventWriter.add(end);

	}

	/**
	 * Creates the node.
	 * @param eventWriter the event writer
	 * @param name the name
	 * @param text the text
	 * @throws XMLStreamException the xML stream exception
	 */
	private void createNode(final XMLEventWriter eventWriter, final String name, final String text)
			throws XMLStreamException
	{
		XMLEventFactory eventFactory = XMLEventFactory.newInstance();
		XMLEvent end = eventFactory.createCharacters("\n");
		XMLEvent tab = eventFactory.createCharacters("\t");
		// Create Start node
		StartElement sElement = eventFactory.createStartElement("", "", name);
		eventWriter.add(tab);
		eventWriter.add(sElement);
		Characters characters = eventFactory.createCharacters(text);
		eventWriter.add(characters);
		eventWriter.add(eventFactory.createEndElement("", "", name));
		eventWriter.add(end);

	}

	// /**
	// * Creates the node.
	// * @param eventWriter the event writer
	// * @param name the name
	// * @param value the value
	// * @param lang the lang
	// * @throws XMLStreamException the xML stream exception
	// */
	// private void createNode(final XMLEventWriter eventWriter, final String
	// name, final String value, final String lang)
	// throws XMLStreamException
	// {
	// XMLEventFactory eventFactory = XMLEventFactory.newInstance();
	// XMLEvent end = eventFactory.createCharacters("\n");
	// XMLEvent tab = eventFactory.createCharacters("\t");
	// // Create Start node
	// StartElement sElement = eventFactory.createStartElement("", "", name);
	// eventWriter.add(tab);
	// eventWriter.add(sElement);
	// eventWriter.add(eventFactory.createAttribute("xml:lang", lang));
	// eventWriter.add(end);
	//
	// if (value.trim().length() > 0)
	// {
	// Characters characters = eventFactory.createCharacters(value.trim());
	// eventWriter.add(characters);
	// }
	// eventWriter.add(eventFactory.createEndElement("", "", name));
	// eventWriter.add(end);
	//
	// }

	/**
	 * Creates the node.
	 * @param eventWriter the event writer
	 * @param name the name
	 * @param tStm the t stm
	 * @throws XMLStreamException the xML stream exception
	 */
	private void createNode(final XMLEventWriter eventWriter, final String name, final TimeStm tStm)
			throws XMLStreamException
	{
		XMLEventFactory eventFactory = XMLEventFactory.newInstance();
		XMLEvent end = eventFactory.createCharacters("\n");
		XMLEvent tab = eventFactory.createCharacters("\t");
		// Create Start node
		StartElement sElement = eventFactory.createStartElement("aodl", "http://pdr.bbaw.de/namespaces/aodl/", name);
		eventWriter.add(tab);
		eventWriter.add(sElement);
		if (tStm.getType() != null)
		{
			eventWriter.add(eventFactory.createAttribute("type", tStm.getType()));
		}

		// Create Content
		if (tStm.getTimes() != null)
		{
			for (int j = 0; j < tStm.getTimes().size(); j++)
			{
				StartElement startElement = eventFactory.createStartElement("aodl",
						"http://pdr.bbaw.de/namespaces/aodl/", "time");
				eventWriter.add(startElement);

				if (tStm.getTimes().get(j).getAccuracy() != null)
				{
					eventWriter.add(eventFactory.createAttribute("accuracy", tStm.getTimes().get(j).getAccuracy()));

				}
				if (tStm.getTimes().get(j).getType() != null)
				{
					eventWriter.add(eventFactory.createAttribute("type", tStm.getTimes().get(j).getType()));

				}
				// Create Content
				if (tStm.getTimes().get(j).getTimeStamp() != null)
				{
					Characters characters = eventFactory.createCharacters(tStm.getTimes().get(j).getTimeStamp()
							.toString());
					eventWriter.add(characters);

				}
				eventWriter.add(eventFactory.createEndElement("aodl", "http://pdr.bbaw.de/namespaces/aodl/", "time"));
				eventWriter.add(end);
			}

		}
		// Create End node
		EndElement eElement = eventFactory.createEndElement("aodl", "http://pdr.bbaw.de/namespaces/aodl/", name);
		eventWriter.add(eElement);
		eventWriter.add(end);

	}

	/**
	 * Creates the node.
	 * @param eventWriter the event writer
	 * @param name the name
	 * @param validationStm the validation stm
	 * @param prefix the prefix
	 * @param uri the uri
	 * @param object 
	 * @throws XMLStreamException the xML stream exception
	 */
	private void createNode(final XMLEventWriter eventWriter, final String name, final ValidationStm validationStm,
			final String prefix, final String uri, PdrMetaObject object) throws XMLStreamException
	{
		XMLEventFactory eventFactory = XMLEventFactory.newInstance();
		XMLEvent end = eventFactory.createCharacters("\n");
		XMLEvent tab = eventFactory.createCharacters("\t");
		// Create Start node
		StartElement sElement = eventFactory.createStartElement(prefix, uri, name);

		eventWriter.add(tab);
		eventWriter.add(sElement);
		if (validationStm.getAuthority() != null)
		{
			eventWriter.add(eventFactory.createAttribute("authority",
					_facade.getObjectDisplayName(validationStm.getAuthority())));
		}
		else
		{
			eventWriter.add(eventFactory.createAttribute("authority", object.getRecord().getRevisions().get(0).getAuthority().toString()));
		}
		eventWriter.add(end);

		if (validationStm.getReference() != null)
		{
			Reference ref = validationStm.getReference();
			sElement = eventFactory.createStartElement(prefix, uri, "reference");
			eventWriter.add(tab);
			eventWriter.add(sElement);
			if (ref.getInternal() != null)
			{
				eventWriter.add(eventFactory.createAttribute("internal", ref.getInternal()));
			}
			if (ref.getQuality() != null)
			{
				eventWriter.add(eventFactory.createAttribute("quality", ref.getQuality()));
			}
			if (ref.getAuthority() != null)
			{
				eventWriter.add(eventFactory.createAttribute("authority",
						_facade.getObjectDisplayName(ref.getAuthority())));
			}

			// Create Content
			if (ref.getSourceId() != null)
			{
				Characters characters;
				PdrObject o = _facade.getPdrObject(ref.getSourceId());
				if (o != null)
				{
					characters = eventFactory.createCharacters(o.getDisplayName());
				}
				else
				{
					characters = eventFactory.createCharacters(ref.getSourceId().toString());
				}
				eventWriter.add(characters);
			}
			EndElement eElement = eventFactory.createEndElement(prefix, uri, "reference");
			eventWriter.add(eElement);
			eventWriter.add(end);
		}

		sElement = eventFactory.createStartElement(prefix, uri, "interpretation");
		eventWriter.add(tab);
		eventWriter.add(sElement);

		if (validationStm.getInterpretation() != null)
		{
			Characters characters = eventFactory.createCharacters(validationStm.getInterpretation());
			eventWriter.add(characters);
		}
		EndElement eElement = eventFactory.createEndElement(prefix, uri, "interpretation");
		eventWriter.add(eElement);
		eventWriter.add(end);

		// Create End node
		eElement = eventFactory.createEndElement(prefix, uri, name);
		eventWriter.add(eElement);
		eventWriter.add(end);

	}

	/**
	 * Creates the node.
	 * @param eventWriter the event writer
	 * @param tr the tr
	 * @param text the text
	 * @throws XMLStreamException the xML stream exception
	 */
	private void createNode(final XMLEventWriter eventWriter, final TaggingRange tr, String text)
			throws XMLStreamException
	{
		XMLEventFactory eventFactory = XMLEventFactory.newInstance();
		XMLEvent end = eventFactory.createCharacters("\n");
		XMLEvent tab = eventFactory.createCharacters("\t");

		// Create Start node
		StartElement sElement = eventFactory.createStartElement("aodl", "http://pdr.bbaw.de/namespaces/aodl/",
				tr.getName());
		eventWriter.add(tab);
		eventWriter.add(sElement);
		eventWriter.add(eventFactory.createAttribute("element",
				PDRConfigProvider.getLabelOfMarkup(tr.getName(), null, null, null)));
		if (tr.getType() != null && tr.getType().length() > 0)
		{
			eventWriter.add(eventFactory.createAttribute("type",
					PDRConfigProvider.getLabelOfMarkup(tr.getName(), tr.getType(), null, null)));
		}
		else
		{
			eventWriter.add(eventFactory.createAttribute("type",
					PDRConfigProvider.getLabelOfMarkup(tr.getName(), "undefined", null, null)));
		}
		if (tr.getSubtype() != null && tr.getSubtype().length() > 0)
		{
			eventWriter.add(eventFactory.createAttribute("subtype",
					PDRConfigProvider.getLabelOfMarkup(tr.getName(), tr.getType(), tr.getSubtype(), null)));
		}
		if (tr.getRole() != null && tr.getRole().length() > 0)
		{
			eventWriter.add(eventFactory.createAttribute("role",
					PDRConfigProvider.getLabelOfMarkup(tr.getName(), tr.getType(), tr.getSubtype(), tr.getRole())));
		}
		if (tr.getAna() != null && tr.getAna().length() > 0)
		{
			PdrObject o = _facade.getPdrObject(new PdrId(tr.getAna()));
			if (o != null)
			{
				eventWriter.add(eventFactory.createAttribute("ana", o.getDisplayName()));
			}
			else
			{
				eventWriter.add(eventFactory.createAttribute("ana", tr.getAna()));
			}
		}

		if (tr.getKey() != null && tr.getKey().length() > 0)
		{
			PdrObject o = _facade.getPdrObject(new PdrId(tr.getKey()));
			if (o != null)
			{
				eventWriter.add(eventFactory.createAttribute("key", o.getDisplayName()));
			}
			else
			{
				eventWriter.add(eventFactory.createAttribute("key", tr.getKey()));
			}
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

		EndElement eElement = eventFactory
				.createEndElement("aodl", "http://pdr.bbaw.de/namespaces/aodl/", tr.getName());
		eventWriter.add(eElement);
		eventWriter.add(end);
	}

	/**
	 * Creates the node no namespace.
	 * @param eventWriter the event writer
	 * @param name the name
	 * @param record the record
	 * @throws XMLStreamException the xML stream exception
	 */
	private void createNodeNoNamespace(final XMLEventWriter eventWriter, final String name, final Record record)
			throws XMLStreamException
	{
		XMLEventFactory eventFactory = XMLEventFactory.newInstance();
		XMLEvent end = eventFactory.createCharacters("\n");
		XMLEvent tab = eventFactory.createCharacters("\t");
		// Create Start node

		if (!record.getRevisions().isEmpty())
		{
			if (record.getRevisions().firstElement() != null
					&& record.getRevisions().firstElement().getTimeStamp() != null
					&& record.getRevisions().firstElement().getAuthority() != null)
			{
				StartElement sE = eventFactory.createStartElement("", "", name);
				eventWriter.add(sE);
				eventWriter.add(end);

				StartElement sE2 = eventFactory.createStartElement("", "", "recordCreationDate");
				eventWriter.add(tab);
				eventWriter.add(sE2);
				Characters characters;
				eventWriter.add(eventFactory.createAttribute("encoding", _encoding));

				characters = eventFactory.createCharacters(_adminDateFormat.format(record.getRevisions().firstElement()
						.getTimeStamp()));
				eventWriter.add(characters);

				eventWriter.add(eventFactory.createEndElement("", "", "recordCreationDate"));
				eventWriter.add(end);

				sE2 = eventFactory.createStartElement("", "", "recordContentSource");
				eventWriter.add(tab);
				eventWriter.add(sE2);
				eventWriter.add(eventFactory.createAttribute("authority", "PDR"));
				characters = eventFactory.createCharacters(_facade.getObjectDisplayName(record.getRevisions()
						.firstElement().getAuthority()));
				eventWriter.add(characters);

				eventWriter.add(eventFactory.createEndElement("", "", "recordContentSource"));
				eventWriter.add(end);

				eventWriter.add(eventFactory.createEndElement("", "", name));
				eventWriter.add(end);
			}

			if (record.getRevisions().size() > 1)
			{
				for (int j = 1; j < record.getRevisions().size(); j++)
				{
					if (record.getRevisions().get(j).getTimeStamp() != null
							&& record.getRevisions().get(j).getAuthority() != null)
					{
						StartElement sE = eventFactory.createStartElement("", "", name);
						eventWriter.add(sE);
						eventWriter.add(end);

						Characters characters;
						StartElement sE2 = eventFactory.createStartElement("", "", "recordContentSource");
						eventWriter.add(tab);
						eventWriter.add(sE2);
						eventWriter.add(eventFactory.createAttribute("authority", "PDR"));
						characters = eventFactory.createCharacters(_facade.getObjectDisplayName(record.getRevisions()
								.get(j).getAuthority()));
						eventWriter.add(characters);

						eventWriter.add(eventFactory.createEndElement("", "", "recordContentSource"));
						eventWriter.add(end);
						sE2 = eventFactory.createStartElement("", "", "recordChangeDate");
						eventWriter.add(tab);
						eventWriter.add(sE2);
						eventWriter.add(eventFactory.createAttribute("encoding", _encoding));

						characters = eventFactory.createCharacters(_adminDateFormat.format(record.getRevisions().get(j)
								.getTimeStamp()));
						eventWriter.add(characters);

						eventWriter.add(eventFactory.createEndElement("", "", "recordChangeDate"));
						eventWriter.add(end);


						eventWriter.add(eventFactory.createEndElement("", "", name));
						eventWriter.add(end);
					}
				}
			}
		}

	}

	/**
	 * Creates the reference children.
	 * @param eventWriter the event writer
	 * @param r the r
	 * @throws XMLStreamException the xML stream exception
	 */
	private void createReferenceChildren(final XMLEventWriter eventWriter, final ReferenceMods r)
			throws XMLStreamException
	{
		XMLEventFactory eventFactory = XMLEventFactory.newInstance();
		_end = eventFactory.createDTD("\n");
		XMLEvent tab = eventFactory.createCharacters("\t");
		StartElement startElement;
		if (r.getTitleInfo() != null)
		{
			startElement = eventFactory.createStartElement("", "", "titleInfo");
			eventWriter.add(startElement);
			eventWriter.add(_end);
			if (r.getTitleInfo() != null && r.getTitleInfo().getTitle() != null)
			{
				createNode(eventWriter, "title", r.getTitleInfo().getTitle());
			}
			if (r.getTitleInfo() != null && r.getTitleInfo().getSubTitle() != null)
			{
				createNode(eventWriter, "subTitle", r.getTitleInfo().getSubTitle());
			}
			if (r.getTitleInfo() != null && r.getTitleInfo().getPartNumber() != null)
			{
				createNode(eventWriter, "partNumber", r.getTitleInfo().getPartNumber());
			}
			if (r.getTitleInfo() != null && r.getTitleInfo().getPartName() != null)
			{
				createNode(eventWriter, "partName", r.getTitleInfo().getPartName());
			}
			eventWriter.add(eventFactory.createEndElement("", "", "titleInfo"));
			eventWriter.add(_end);
		}
		if (r.getNameMods() != null)
		{
			for (int i = 0; i < r.getNameMods().size(); i++)
			{
				createNode(eventWriter, "name", r.getNameMods().get(i));
			}
		}
		if (r.getGenre() != null)
		{
			StartElement sE = eventFactory.createStartElement("", "", "genre");
			eventWriter.add(sE);
			if (r.getGenre().getAuthority() != null)
			{
				eventWriter.add(eventFactory.createAttribute("authority", r.getGenre().getAuthority()));
			}
			Characters characters = eventFactory.createCharacters(r.getGenre().getGenre());
			eventWriter.add(characters);

			eventWriter.add(eventFactory.createEndElement("", "", "genre"));
			eventWriter.add(_end);
		}

		if (r.getOriginInfo() != null)
		{
			createNode(eventWriter, "originInfo", r.getOriginInfo());
		}

		if (r.getNote() != null)
		{
			StartElement sE = eventFactory.createStartElement("", "", "note");
			eventWriter.add(sE);
			if (r.getNote().getType() != null)
			{
				eventWriter.add(eventFactory.createAttribute("type", r.getNote().getType()));
			}
			Characters characters = eventFactory.createCharacters(r.getNote().getNote());
			eventWriter.add(characters);

			eventWriter.add(eventFactory.createEndElement("", "", "note"));
			eventWriter.add(_end);
		}

		if (r.getIdentifiersMods() != null)
		{
			for (int i = 0; i < r.getIdentifiersMods().size(); i++)
			{
				StartElement sE = eventFactory.createStartElement("", "", "identifier");
				eventWriter.add(sE);
				if (r.getIdentifiersMods().get(i).getType() != null)
				{
					eventWriter.add(eventFactory.createAttribute("type", r.getIdentifiersMods().get(i).getType()));
				}
				Characters characters = eventFactory.createCharacters(r.getIdentifiersMods().get(i).getIdentifier());
				eventWriter.add(characters);

				eventWriter.add(eventFactory.createEndElement("", "", "identifier"));
				eventWriter.add(_end);
			}
		}

		if (r.getLocation() != null)
		{
			StartElement sE = eventFactory.createStartElement("", "", "location");
			eventWriter.add(sE);
			eventWriter.add(_end);


			if (r.getLocation().getPhysicalLocation() != null)
			{
				StartElement sE2 = eventFactory.createStartElement("", "", "physicalLocation");
				eventWriter.add(tab);
				eventWriter.add(sE2);
				Characters characters = eventFactory.createCharacters(r.getLocation().getPhysicalLocation());
				eventWriter.add(characters);
				eventWriter.add(eventFactory.createEndElement("", "", "physicalLocation"));
				eventWriter.add(_end);
			}
			if (r.getLocation().getShelfLocator() != null)
			{
				StartElement sE2 = eventFactory.createStartElement("", "", "shelfLocator");
				eventWriter.add(tab);
				eventWriter.add(sE2);
				Characters characters = eventFactory.createCharacters(r.getLocation().getShelfLocator());
				eventWriter.add(characters);
				eventWriter.add(eventFactory.createEndElement("", "", "shelfLocator"));
				eventWriter.add(_end);
			}
			if (r.getLocation().getUrl() != null)
			{
				StartElement sE2 = eventFactory.createStartElement("", "", "url");
				eventWriter.add(tab);
				eventWriter.add(sE2);
				Characters characters = eventFactory.createCharacters(r.getLocation().getUrl());
				eventWriter.add(characters);
				eventWriter.add(eventFactory.createEndElement("", "", "url"));
				eventWriter.add(_end);
			}
			eventWriter.add(eventFactory.createEndElement("", "", "location"));
			eventWriter.add(_end);
		}

		if (r.getAccessCondition() != null)
		{
			StartElement sE = eventFactory.createStartElement("", "", "accessCondition");
			eventWriter.add(sE);
			if (r.getAccessCondition().getAccessCondition() != null)
			{
				eventWriter.add(eventFactory.createAttribute("type", r.getAccessCondition().getType()));
			}
			Characters characters = eventFactory.createCharacters(r.getAccessCondition().getAccessCondition());
			eventWriter.add(characters);

			eventWriter.add(eventFactory.createEndElement("", "", "accessCondition"));
			eventWriter.add(_end);
		}
		if (r.getSeriesTitleInfo() != null)
		{
			StartElement sE = eventFactory.createStartElement("", "", "relatedItem");
			eventWriter.add(sE);
			eventWriter.add(eventFactory.createAttribute("type", "series"));

			if (r.getSeriesTitleInfo() != null)
			{
				startElement = eventFactory.createStartElement("", "", "titleInfo");
				eventWriter.add(startElement);
				eventWriter.add(_end);
				if (r.getSeriesTitleInfo() != null && r.getSeriesTitleInfo().getTitle() != null)
				{

					createNode(eventWriter, "title", r.getSeriesTitleInfo().getTitle());

				}
				if (r.getSeriesTitleInfo() != null && r.getSeriesTitleInfo().getSubTitle() != null)
				{

					createNode(eventWriter, "subTitle", r.getSeriesTitleInfo().getSubTitle());

				}
				if (r.getSeriesTitleInfo() != null && r.getSeriesTitleInfo().getPartName() != null)
				{
					createNode(eventWriter, "partName", r.getSeriesTitleInfo().getPartName());

				}
				if (r.getSeriesTitleInfo() != null && r.getSeriesTitleInfo().getPartNumber() != null)
				{
					createNode(eventWriter, "partNumber", r.getSeriesTitleInfo().getPartNumber());

				}
				eventWriter.add(eventFactory.createEndElement("", "", "titleInfo"));
				eventWriter.add(_end);

			}
			eventWriter.add(eventFactory.createEndElement("", "", "relatedItem"));
			eventWriter.add(_end);
		}
		if (r.getRelatedItems() != null && r.getRelatedItems().size() > 0)
		{
			for (RelatedItem relItem : r.getRelatedItems())
			{
				StartElement sE = eventFactory.createStartElement("", "", "relatedItem");
				eventWriter.add(sE);
				if (relItem.getType() != null)
				{
					eventWriter.add(eventFactory.createAttribute("type", relItem.getType()));
				}
				if (relItem.getId() != null)
				{
					eventWriter.add(eventFactory.createAttribute("ID", relItem.getId()));
				}
				if (relItem.getPart() != null)
				{
					createNode(eventWriter, "part", relItem.getPart());
				}
				if (relItem.getId() != null)
				{
					ReferenceMods host = _facade.getReference(new PdrId(relItem.getId()));
					if (host != null)
					{
						createReferenceChildren(eventWriter, host);
					}
				}
				eventWriter.add(eventFactory.createEndElement("", "", "relatedItem"));
				eventWriter.add(_end);
			}

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
					StartElement startElement = eventFactory.createStartElement("aodl",
							"http://pdr.bbaw.de/namespaces/aodl/", "lb");
					eventWriter.add(startElement);

					eventWriter.add(eventFactory.createEndElement("aodl", "http://pdr.bbaw.de/namespaces/aodl/", "lb"));
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
	 * @param a aspect.
	 * @return xml string
	 * @throws XMLStreamException exc.
	 * @see org.bbaw.pdr.ae.control.core.XMLProcessorInterface#writeToXML(org.bbaw.pdr.ae.model.Aspect)
	 */
	@Override
	public final String writeToXML(final Aspect a) throws XMLStreamException
	{

		// Create a XMLOutputFactory
		XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
		// Create XMLEventWriter

		ByteArrayOutputStream bout = new ByteArrayOutputStream();

		XMLEventWriter eventWriter = outputFactory.createXMLEventWriter(bout);

		// Create a EventFactory
		XMLEventFactory eventFactory = XMLEventFactory.newInstance();
		XMLEvent end = eventFactory.createCharacters("\n");
		// Create and write Start Tag
		StartDocument startDocument = eventFactory.createStartDocument();
		eventWriter.add(startDocument);

		// Create config open tag
		StartElement startElement = eventFactory.createStartElement("aodl", "http://pdr.bbaw.de/namespaces/aodl/",
				"aspect");
		eventWriter.add(startElement);
		eventWriter.add(eventFactory.createNamespace("aodl", "http://pdr.bbaw.de/namespaces/aodl/"));

		// eventWriter.add(eventFactory.createAttribute("xmlns:xsi",
		// "http://www.w3.org/2001/XMLSchema-instance"));
		// eventWriter.add(eventFactory.createAttribute("xsi:schemaLocation",
		// "http://pdr.bbaw.de/namespaces/aodl/ http://pdr.bbaw.de/schema/aodl.xsd"));
		eventWriter.add(eventFactory.createAttribute("id", a.getPdrId().toString()));
		// add display name
		eventWriter.add(eventFactory.createAttribute("displayName", a.getDisplayName()));

		// eventWriter.add(end);

		// Write the different nodes

		startElement = eventFactory.createStartElement("aodl", "http://pdr.bbaw.de/namespaces/aodl/", "record");
		eventWriter.add(startElement);
		eventWriter.add(end);
		for (int i = 0; i < a.getRecord().getRevisions().size(); i++)
		{

			createNode(eventWriter, "revision", a.getRecord().getRevisions().get(i), "aodl",
					"http://pdr.bbaw.de/namespaces/aodl/");
		}
		eventWriter.add(eventFactory.createEndElement("aodl", "http://pdr.bbaw.de/namespaces/aodl/", "record"));
		eventWriter.add(end);

		startElement = eventFactory.createStartElement("aodl", "http://pdr.bbaw.de/namespaces/aodl/", "timeDim");
		eventWriter.add(startElement);
		eventWriter.add(end);
		if (a.getTimeDim() != null && a.getTimeDim().getTimeStms() != null)
		{
			for (int i = 0; i < a.getTimeDim().getTimeStms().size(); i++)
			{

				createNode(eventWriter, "timeStm", a.getTimeDim().getTimeStms().get(i));
			}
		}
		eventWriter.add(eventFactory.createEndElement("aodl", "http://pdr.bbaw.de/namespaces/aodl/", "timeDim"));
		eventWriter.add(end);

		startElement = eventFactory.createStartElement("aodl", "http://pdr.bbaw.de/namespaces/aodl/", "spatialDim");
		eventWriter.add(startElement);
		eventWriter.add(end);

		if (a.getSpatialDim() != null && a.getSpatialDim().getSpatialStms() != null)
		{
			for (int i = 0; i < a.getSpatialDim().getSpatialStms().size(); i++)
			{
				createNode(eventWriter, "spatialStm", a.getSpatialDim().getSpatialStms().get(i));
			}
		}
		eventWriter.add(eventFactory.createEndElement("aodl", "http://pdr.bbaw.de/namespaces/aodl/", "spatialDim"));
		eventWriter.add(end);

		startElement = eventFactory.createStartElement("aodl", "http://pdr.bbaw.de/namespaces/aodl/", "relationDim");
		eventWriter.add(startElement);
		eventWriter.add(end);

		if (a.getRelationDim() != null && a.getRelationDim().getRelationStms() != null)
		{
			for (int i = 0; i < a.getRelationDim().getRelationStms().size(); i++)
			{
				createNode(eventWriter, "relationStm", a.getRelationDim().getRelationStms().get(i));
			}
		}
		eventWriter.add(eventFactory.createEndElement("aodl", "http://pdr.bbaw.de/namespaces/aodl/", "relationDim"));
		eventWriter.add(end);

		startElement = eventFactory.createStartElement("aodl", "http://pdr.bbaw.de/namespaces/aodl/", "semanticDim");
		eventWriter.add(startElement);
		eventWriter.add(end);

		if (a.getSemanticDim() != null && a.getSemanticDim().getSemanticStms() != null)
		{
			for (int i = 0; i < a.getSemanticDim().getSemanticStms().size(); i++)
			{
				createNode(eventWriter, "semanticStm", a.getSemanticDim().getSemanticStms().get(i));
			}
		}
		eventWriter.add(eventFactory.createEndElement("aodl", "http://pdr.bbaw.de/namespaces/aodl/", "semanticDim"));
		eventWriter.add(end);

		startElement = eventFactory.createStartElement("aodl", "http://pdr.bbaw.de/namespaces/aodl/", "notification");
		eventWriter.add(startElement);
		eventWriter.add(end);

		int start = 0;
		// System.out.println("notifi " + a.getNotification());
		// FIXME!!!!!!!!!!!!!!!!
		if (a.getNotification() != null && a.getRangeList() != null)
		{
			// System.out.println("RangeListe hat TaggingListen, Zahl: " +
			// a.getRangeList().size());
			for (int i = 0; i < a.getRangeList().size(); i++)
			{
				if (a.getNotification().length() > start
						&& a.getNotification().length() >= a.getRangeList().get(i).getStart()
						&& a.getRangeList().get(i).getStart() >= 0)
				{
					String subText = a.getNotification().substring(start, a.getRangeList().get(i).getStart());

					processAppendTextWithLineBreaks(eventWriter, subText);

					createNode(eventWriter, a.getRangeList().get(i), a.getNotification());
					start = a.getRangeList().get(i).getStart() + a.getRangeList().get(i).getLength();
				}
			}
			if (a.getNotification().length() > start)
			{
				String subText = a.getNotification().substring(start);

				processAppendTextWithLineBreaks(eventWriter, subText);

			}
		}
		else if (a.getNotification() != null)
		{
			processAppendTextWithLineBreaks(eventWriter, a.getNotification());

		}
		eventWriter.add(eventFactory.createEndElement("aodl", "http://pdr.bbaw.de/namespaces/aodl/", "notification"));
		eventWriter.add(end);

		startElement = eventFactory.createStartElement("aodl", "http://pdr.bbaw.de/namespaces/aodl/", "validation");
		eventWriter.add(startElement);
		eventWriter.add(end);

		if (a.getValidation() != null && a.getValidation().getValidationStms() != null)
		{
			for (int i = 0; i < a.getValidation().getValidationStms().size(); i++)
			{
				createNode(eventWriter, "validationStm", a.getValidation().getValidationStms().get(i), "aodl",
						"http://pdr.bbaw.de/namespaces/aodl/", a);
			}
		}
		eventWriter.add(eventFactory.createEndElement("aodl", "http://pdr.bbaw.de/namespaces/aodl/", "validation"));
		eventWriter.add(end);

		eventWriter.add(eventFactory.createEndElement("aodl", "http://pdr.bbaw.de/namespaces/aodl/", "aspect"));
		eventWriter.add(end);
		eventWriter.add(eventFactory.createEndDocument());
		eventWriter.close();

		_log = new Status(IStatus.WARNING, Activator.PLUGIN_ID, "XMLProcessor output: " + bout.toString());
		iLogger.log(_log);

		return bout.toString();

	}

	/**
	 * @param p person
	 * @return xml string
	 * @throws XMLStreamException exc.
	 * @see org.bbaw.pdr.ae.control.core.XMLProcessorInterface#writeToXML(org.bbaw.pdr.ae.model.Person)
	 */
	@Override
	public final String writeToXML(final Person p) throws XMLStreamException
	{
		// Create a XMLOutputFactory
		XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
		// Create XMLEventWriter
		ByteArrayOutputStream bout = new ByteArrayOutputStream();

		XMLEventWriter eventWriter = outputFactory.createXMLEventWriter(bout);

		// Create a EventFactory
		XMLEventFactory eventFactory = XMLEventFactory.newInstance();
		_end = eventFactory.createDTD("\n");
		// Create and write Start Tag
		StartDocument startDocument = eventFactory.createStartDocument();
		eventWriter.add(startDocument);

		// Create config open tag
		StartElement startElement = eventFactory.createStartElement("podl", "http://pdr.bbaw.de/namespaces/podl/",
				"person");
		eventWriter.add(startElement);
		eventWriter.add(eventFactory.createNamespace("podl", "http://pdr.bbaw.de/namespaces/podl/"));
		// eventWriter.add(eventFactory.createAttribute("xmlns:xsi",
		// "http://www.w3.org/2001/XMLSchema-instance"));
		// eventWriter.add(eventFactory.createAttribute("xsi:schemaLocation",
		// "http://pdr.bbaw.de/namespaces/podl/ http://pdr.bbaw.de/schema/podl.xsd"));
		eventWriter.add(eventFactory.createAttribute("id", p.getPdrId().toString()));
		// add display name
		eventWriter.add(eventFactory.createAttribute("displayName", p.getDisplayName()));

		eventWriter.add(_end);

		// Write the different nodes
		if (p.getRecord() != null && p.getRecord().getRevisions() != null)
		{
			startElement = eventFactory.createStartElement("podl", "http://pdr.bbaw.de/namespaces/podl/", "record");
			eventWriter.add(startElement);
			eventWriter.add(_end);

			for (int i = 0; i < p.getRecord().getRevisions().size(); i++)
			{

				createNode(eventWriter, "revision", p.getRecord().getRevisions().get(i), "podl",
						"http://pdr.bbaw.de/namespaces/podl/");
			}
			eventWriter.add(eventFactory.createEndElement("podl", "http://pdr.bbaw.de/namespaces/podl/", "record"));
			eventWriter.add(_end);
		}
		if (p.getIdentifiers() != null)
		{
			startElement = eventFactory
					.createStartElement("podl", "http://pdr.bbaw.de/namespaces/podl/", "identifiers");
			eventWriter.add(startElement);
			eventWriter.add(_end);
			if (p.getIdentifiers() != null && p.getIdentifiers().getIdentifiers() != null)
			{
				for (int i = 0; i < p.getIdentifiers().getIdentifiers().size(); i++)
				{

					createNode(eventWriter, "identifier", p.getIdentifiers().getIdentifiers().get(i));
				}
			}
			eventWriter
					.add(eventFactory.createEndElement("podl", "http://pdr.bbaw.de/namespaces/podl/", "identifiers"));
			eventWriter.add(_end);
		}

		if (p.getConcurrences() != null)
		{
			startElement = eventFactory.createStartElement("podl", "http://pdr.bbaw.de/namespaces/podl/",
					"concurrences");
			eventWriter.add(startElement);
			eventWriter.add(_end);

			if (p.getConcurrences() != null && p.getConcurrences().getConcurrences() != null)
			{
				for (int i = 0; i < p.getConcurrences().getConcurrences().size(); i++)

				{

					createNode(eventWriter, "concurrence", p.getConcurrences().getConcurrences().get(i), p);
				}
			}
			eventWriter.add(eventFactory
					.createEndElement("podl", "http://pdr.bbaw.de/namespaces/podl/", "concurrences"));
			eventWriter.add(_end);
		}

		eventWriter.add(eventFactory.createEndElement("podl", "http://pdr.bbaw.de/namespaces/podl/", "person"));
		eventWriter.add(_end);
		eventWriter.add(eventFactory.createEndDocument());
		eventWriter.close();

		_log = new Status(IStatus.WARNING, Activator.PLUGIN_ID, "XMLProcessor output: " + bout.toString());
		iLogger.log(_log);
		return bout.toString();

	}

	/**
	 * @param r reference
	 * @return xml string
	 * @throws XMLStreamException exc.
	 * @see org.bbaw.pdr.ae.control.core.XMLProcessorInterface#writeToXML(org.bbaw.pdr.ae.model.ReferenceMods)
	 */
	@Override
	public final String writeToXML(final ReferenceMods r) throws XMLStreamException
	{
		// Create a XMLOutputFactory
		XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
		// Create XMLEventWriter

		ByteArrayOutputStream bout = new ByteArrayOutputStream();

		XMLEventWriter eventWriter = outputFactory.createXMLEventWriter(bout);

		// Create a EventFactory
		XMLEventFactory eventFactory = XMLEventFactory.newInstance();
		_end = eventFactory.createDTD("\n");
		// Create and write Start Tag
		StartDocument startDocument = eventFactory.createStartDocument();
		eventWriter.add(startDocument);
		eventWriter.add(_end);

		createNode(eventWriter, "mods", r, "", "");

		eventWriter.add(eventFactory.createEndDocument());
		eventWriter.close();

		_log = new Status(IStatus.WARNING, Activator.PLUGIN_ID, "XMLProcessor output: " + bout.toString());
		iLogger.log(_log);

		return bout.toString();

	}
}
