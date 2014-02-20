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
package org.bbaw.pdr.ae.collections.control;

import java.io.ByteArrayOutputStream;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.bbaw.pdr.ae.collections.model.PDRCollection;
import org.bbaw.pdr.ae.common.AEConstants;
import org.bbaw.pdr.ae.metamodel.Revision;
import org.bbaw.pdr.ae.model.view.TreeNode;

/** collection xml processor to write a given collection to xml using
 * collection namespace cudl.
 * @author Christoph Plutte
 *
 */
public class CollectionXMLProcessor
{

	/** collections namespace prefix.*/
	private static final String PREFIX = "codl";
	/** collections namespace uri.*/
	private static final String URI = "http://pdr.bbaw.de/namespaces/codl/";
	/** write collection to xml.
	 * @param coll collection to write to xml
	 * @return xml string of collection
	 * @throws XMLStreamException xml exception.
	 */
	public final String writeToXML(final PDRCollection coll) throws XMLStreamException
	{
		// Create a XMLOutputFactory
		XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
		// Create XMLEventWriter
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		XMLEventWriter eventWriter = outputFactory
				.createXMLEventWriter(bout);
		// Create a EventFactory
		XMLEventFactory eventFactory = XMLEventFactory.newInstance();
//		end = eventFactory.createDTD("\n");
		XMLEvent end = eventFactory.createCharacters("\n");
//		XMLEvent tab = eventFactory.createCharacters("\t");
		// Create and write Start Tag
		StartDocument startDocument = eventFactory.createStartDocument();
		eventWriter.add(startDocument);

		// Create config open tag
		StartElement startElement = eventFactory.createStartElement(PREFIX, URI, "pdrCollection");
		eventWriter.add(startElement);
		eventWriter.add(eventFactory.createNamespace(PREFIX, URI));

//		eventWriter.add(eventFactory.createAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance"));
//		eventWriter.add(eventFactory.createAttribute("xsi:schemaLocation",
//				"http://pdr.bbaw.de/namespaces/dtdl/ http://telotadev.bbaw.de/telotasvn/pdr/Programme/schema/dtdl.xsd"));
		eventWriter.add(eventFactory.createAttribute("name", coll.getName()));

		eventWriter.add(end);

		// Write the different nodes
		if (coll.getRecord() != null && coll.getRecord().getRevisions() != null)
		{
			startElement = eventFactory.createStartElement(PREFIX, URI, "record");
			eventWriter.add(startElement);
			eventWriter.add(end);
			for (int i = 0; i < coll.getRecord().getRevisions().size(); i++)
			{
				createNode(eventWriter, "revision", coll.getRecord().getRevisions().get(i));
			}
			eventWriter.add(eventFactory.createEndElement(PREFIX, URI, "record"));
			eventWriter.add(end);
		}
		if (coll.getObjects() != null && !coll.getObjects().isEmpty())
		{
			startElement = eventFactory.createStartElement(PREFIX, URI, "collection");
			eventWriter.add(startElement);
			eventWriter.add(end);
			for (TreeNode tn : coll.getObjects())
			{
				createNode(eventWriter, "item", tn);
			}
			eventWriter.add(eventFactory.createEndElement(PREFIX, URI, "collection"));
			eventWriter.add(end);
		}


		eventWriter.add(eventFactory.createEndElement(PREFIX, URI, "pdrCollection"));
		eventWriter.add(end);
		eventWriter.add(eventFactory.createEndDocument());
		eventWriter.close();

//		System.out.println(bout.toString());
		return bout.toString();

	}
	/** create node of given treenode.
	 * @param eventWriter writer.
	 * @param name node name.
	 * @param tn treenode.
	 * @throws XMLStreamException exc.
	 */
	private void createNode(final XMLEventWriter eventWriter, final String name,
			final TreeNode tn) throws XMLStreamException
			{
		XMLEventFactory eventFactory = XMLEventFactory.newInstance();
		XMLEvent end = eventFactory.createCharacters("\n");
		XMLEvent tab = eventFactory.createCharacters("\t");
		// Create Start node
		StartElement sElement = eventFactory.createStartElement(PREFIX, URI, name);
		eventWriter.add(tab);
		eventWriter.add(sElement);
		if (tn.getPdrObject() != null)
		{
			eventWriter.add(eventFactory.createAttribute("id", tn.getPdrObject().getPdrId().toString()));
		}
		else
		{
			eventWriter.add(eventFactory.createAttribute("id", tn.getId()));
		}

		if (tn.hasChildren() && tn.getChildren() != null)
		{
			for (TreeNode t : tn.getChildren())
			{
				createNode(eventWriter, "item", t);
			}
		}
		// Create End node
		EndElement eElement = eventFactory.createEndElement(PREFIX,
				URI, name);
		eventWriter.add(eElement);
		eventWriter.add(end);
	}
	/** create node of given revision.
	 * @param eventWriter writer.
	 * @param name node name
	 * @param r revision.
	 * @throws XMLStreamException exc.
	 */
	private void createNode(final XMLEventWriter eventWriter, final String name,
			final Revision r) throws XMLStreamException
	{
		XMLEventFactory eventFactory = XMLEventFactory.newInstance();
		XMLEvent end = eventFactory.createCharacters("\n");
		XMLEvent tab = eventFactory.createCharacters("\t");
		// Create Start node
		StartElement sElement = eventFactory.createStartElement(PREFIX,
				URI, name);
		eventWriter.add(tab);
		eventWriter.add(sElement);
		eventWriter.add(eventFactory.createAttribute("ref", new Integer(r.getRef()).toString()));
		eventWriter.add(eventFactory.createAttribute("timestamp", AEConstants.ADMINDATE_FORMAT.format(r.getTimeStamp()).toString()));
		eventWriter.add(eventFactory.createAttribute("authority", r.getAuthority().toString()));
// TODO das Schema sieht keinen authority namen mehr vor, �nderung im model umsetzen.
//		// Create Content
//		Characters characters = eventFactory.createCharacters(r.getRevisor());
//		eventWriter.add(characters);
		// Create End node
		EndElement eElement = eventFactory.createEndElement(PREFIX,
				URI, name);
		eventWriter.add(eElement);
		eventWriter.add(end);
	}
}
