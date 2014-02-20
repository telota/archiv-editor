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
import org.bbaw.pdr.ae.control.internal.Activator;
import org.bbaw.pdr.ae.metamodel.Revision;
import org.bbaw.pdr.ae.model.User;
import org.bbaw.pdr.ae.model.UserContact;
import org.bbaw.pdr.ae.model.UserInformation;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

/**
 * The Class UserXMLProcessor.
 * @author Christoph Plutte
 */
public class UserXMLProcessor
{

	/** The _admin date format. */
	private SimpleDateFormat _adminDateFormat = AEConstants.ADMINDATE_FORMAT;

	// private final String _encoding = "iso-8601";

	/** The _end. */
	private XMLEvent _end;

	/** The _tab. */
	private XMLEvent _tab;

	/** Logger. */
	private static ILog iLogger = AEConstants.ILOGGER;
	/** status. */
	private IStatus _log;

	/**
	 * Creates the node.
	 * @param eventWriter the event writer
	 * @param name the name of node
	 * @param r the revision
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
		eventWriter.add(eventFactory.createAttribute("authority", r.getAuthority().toString()));
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
	 * Write to xml.
	 * @param user the user
	 * @return the string
	 * @throws XMLStreamException the xML stream exception
	 */
	public final String writeToXML(final User user) throws XMLStreamException
	{
		// Create a XMLOutputFactory
		XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
		// Create XMLEventWriter
		ByteArrayOutputStream bout = new ByteArrayOutputStream();

		XMLEventWriter eventWriter = outputFactory.createXMLEventWriter(bout);

		// Create a EventFactory
		XMLEventFactory eventFactory = XMLEventFactory.newInstance();
		_end = eventFactory.createDTD("\n");
		_tab = eventFactory.createDTD("\t");

		// Create and write Start Tag
		StartDocument startDocument = eventFactory.createStartDocument();
		eventWriter.add(startDocument);

		// Create config open tag
		StartElement startElement = eventFactory.createStartElement("uodl", "http://pdr.bbaw.de/namespaces/uodl/",
				"user");
		eventWriter.add(startElement);
		eventWriter.add(eventFactory.createNamespace("uodl", "http://pdr.bbaw.de/namespaces/uodl/"));
		eventWriter.add(eventFactory.createAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance"));
		eventWriter.add(eventFactory.createAttribute("xsi:schemaLocation",
				"http://pdr.bbaw.de/namespaces/uodl/ http://pdr.bbaw.de/schema/uodl.xsd"));
		eventWriter.add(eventFactory.createAttribute("id", user.getPdrId().toString()));

		eventWriter.add(_end);

		// Write the different nodes
		if (user.getRecord() != null && user.getRecord().getRevisions() != null)
		{
			startElement = eventFactory.createStartElement("uodl", "http://pdr.bbaw.de/namespaces/uodl/", "record");
			eventWriter.add(startElement);
			eventWriter.add(_end);

			for (int i = 0; i < user.getRecord().getRevisions().size(); i++)
			{

				createNode(eventWriter, "revision", user.getRecord().getRevisions().get(i), "uodl",
						"http://pdr.bbaw.de/namespaces/uodl/");
			}
			eventWriter.add(eventFactory.createEndElement("uodl", "http://pdr.bbaw.de/namespaces/uodl/", "record"));
			eventWriter.add(_end);
		}
		if (user.getAuthentication() != null)
		{
			startElement = eventFactory.createStartElement("uodl", "http://pdr.bbaw.de/namespaces/uodl/",
					"authentication");
			eventWriter.add(startElement);
			if (user.getAuthentication().getUserName() != null)
			{
				eventWriter.add(eventFactory.createAttribute("username", user.getAuthentication().getUserName()));
			}
			if (user.getAuthentication().getPassword() != null)
			{
				eventWriter.add(eventFactory.createAttribute("password", user.getAuthentication().getPassword()));
			}
			eventWriter.add(_end);

			if (user.getAuthentication().getRoles() != null)
			{
				eventWriter.add(_tab);
				startElement = eventFactory.createStartElement("uodl", "http://pdr.bbaw.de/namespaces/uodl/", "roles");
				eventWriter.add(startElement);
				eventWriter.add(_end);

				for (String role : user.getAuthentication().getRoles())
				{
					eventWriter.add(_tab);
					eventWriter.add(_tab);
					startElement = eventFactory.createStartElement("uodl", "http://pdr.bbaw.de/namespaces/uodl/",
							"role");
					eventWriter.add(startElement);
					if (role != null)
					{
						Characters characters = eventFactory.createCharacters(role);
						eventWriter.add(characters);
					}
					eventWriter.add(eventFactory
							.createEndElement("uodl", "http://pdr.bbaw.de/namespaces/uodl/", "role"));
					eventWriter.add(_end);
				}

				eventWriter.add(_tab);
				eventWriter.add(eventFactory.createEndElement("uodl", "http://pdr.bbaw.de/namespaces/uodl/", "roles"));
				eventWriter.add(_end);
			}

			eventWriter.add(eventFactory.createEndElement("uodl", "http://pdr.bbaw.de/namespaces/uodl/",
					"authentication"));
			eventWriter.add(_end);
		}

		if (user.getUserInformation() != null)
		{
			UserInformation ui = user.getUserInformation();
			startElement = eventFactory
					.createStartElement("uodl", "http://pdr.bbaw.de/namespaces/uodl/", "information");
			eventWriter.add(startElement);
			eventWriter.add(_end);
			eventWriter.add(_tab);
			startElement = eventFactory.createStartElement("uodl", "http://pdr.bbaw.de/namespaces/uodl/", "fullname");
			eventWriter.add(startElement);
			if (ui.getForename() != null)
			{
				eventWriter.add(eventFactory.createAttribute("forename", ui.getForename()));
			}
			if (ui.getSurname() != null)
			{
				eventWriter.add(eventFactory.createAttribute("surname", ui.getSurname()));
			}
			eventWriter.add(eventFactory.createEndElement("uodl", "http://pdr.bbaw.de/namespaces/uodl/", "fullname"));
			eventWriter.add(_end);
			eventWriter.add(_tab);
			startElement = eventFactory.createStartElement("uodl", "http://pdr.bbaw.de/namespaces/uodl/", "project");
			eventWriter.add(startElement);
			if (ui.getUserProjectPosition() != null)
			{
				eventWriter.add(eventFactory.createAttribute("position", ui.getUserProjectPosition()));
			}
			if (ui.getProjectName() != null)
			{
				Characters characters = eventFactory.createCharacters(ui.getProjectName());
				eventWriter.add(characters);
			}
			eventWriter.add(eventFactory.createEndElement("uodl", "http://pdr.bbaw.de/namespaces/uodl/", "project"));
			eventWriter.add(_end);

			if (ui.getUserContacts() != null)
			{
				for (UserContact uc : ui.getUserContacts())
				{
					eventWriter.add(_tab);
					startElement = eventFactory.createStartElement("uodl", "http://pdr.bbaw.de/namespaces/uodl/",
							"contact");
					eventWriter.add(startElement);
					if (uc.getType() != null)
					{
						eventWriter.add(eventFactory.createAttribute("type", uc.getType()));
					}
					if (uc.getContact() != null)
					{
						Characters characters = eventFactory.createCharacters(uc.getContact());
						eventWriter.add(characters);
					}
					eventWriter.add(eventFactory.createEndElement("uodl", "http://pdr.bbaw.de/namespaces/uodl/",
							"contact"));
					eventWriter.add(_end);
				}
			}
			eventWriter
					.add(eventFactory.createEndElement("uodl", "http://pdr.bbaw.de/namespaces/uodl/", "information"));
			eventWriter.add(_end);
		}

		eventWriter.add(eventFactory.createEndElement("uodl", "http://pdr.bbaw.de/namespaces/uodl/", "user"));
		eventWriter.add(_end);
		eventWriter.add(eventFactory.createEndDocument());
		eventWriter.close();

		_log = new Status(IStatus.WARNING, Activator.PLUGIN_ID, "XMLProcessor output: " + bout.toString());
		iLogger.log(_log);
		return bout.toString();
	}

}
