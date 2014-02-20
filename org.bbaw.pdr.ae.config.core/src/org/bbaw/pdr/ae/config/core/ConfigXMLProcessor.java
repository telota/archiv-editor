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
package org.bbaw.pdr.ae.config.core;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.bbaw.pdr.ae.config.model.AspectConfigTemplate;
import org.bbaw.pdr.ae.config.model.ComplexSemanticTemplate;
import org.bbaw.pdr.ae.config.model.ConfigData;
import org.bbaw.pdr.ae.config.model.ConfigItem;
import org.bbaw.pdr.ae.config.model.DataType;
import org.bbaw.pdr.ae.config.model.DatatypeDesc;
import org.bbaw.pdr.ae.config.model.IdentifierConfig;
import org.bbaw.pdr.ae.config.model.SemanticTemplate;

/**
 * The Class ConfigXMLProcessor.
 *
 * @author Christoph Plutte
 */
public class ConfigXMLProcessor
{
	/** dtdl prefix.*/
	private static final String PREFIX = "dtdl";
	/** namespace uri.*/
	private static final String URI = "http://pdr.bbaw.de/namespaces/dtdl/";
	/**
	 * Creates the node.
	 *
	 * @param eventWriter
	 *            the event writer
	 * @param name
	 *            the name
	 * @param configData
	 *            the config data
	 * @throws XMLStreamException
	 *             the xML stream exception
	 */
	private void createNode(final XMLEventWriter eventWriter, final String name,
			final ConfigData configData) throws XMLStreamException
	{
		ConfigItem i = (ConfigItem) configData;
		XMLEventFactory eventFactory = XMLEventFactory.newInstance();
		XMLEvent end = eventFactory.createCharacters("\n");
		XMLEvent tab = eventFactory.createCharacters("\t");
		// Create Start node
		StartElement sElement = eventFactory.createStartElement(PREFIX, URI, name);
		eventWriter.add(tab);
		eventWriter.add(sElement);
		if (i.getPos() != null)
		{
			eventWriter.add(eventFactory.createAttribute("pos", i.getPos()));
		}
		if (i.getValue() != null)
		{
			eventWriter.add(eventFactory.createAttribute("value", i.getValue()));
		}
		if (i.getLabel() != null)
		{
			eventWriter.add(eventFactory.createAttribute("label", i.getLabel()));
		}
		eventWriter.add(eventFactory.createAttribute("ignore", new Boolean(i.isIgnore()).toString()));
		eventWriter.add(eventFactory.createAttribute("mandatory", new Boolean(i.isMandatory()).toString()));
		eventWriter.add(eventFactory.createAttribute("priority", String.format("%03d", i.getPriority())));

		eventWriter.add(end);
		if (i.getDocumentation() != null)
		{
			createNode(eventWriter, "documentation", i.getDocumentation(), PREFIX, URI);
		}
		if (i.getChildren() != null)
		{
			for (String str2 : i.getChildren().keySet())
			{
				createNode(eventWriter, "item", i.getChildren().get(str2));
			}
		}
		eventWriter.add(eventFactory.createEndElement(PREFIX, URI, name));
		eventWriter.add(end);
	}


	/**
	 * Creates the node.
	 *
	 * @param eventWriter
	 *            the event writer
	 * @param name
	 *            the name
	 * @param d
	 *            the d
	 * @param string2
	 *            the string2
	 * @param string3
	 *            the string3
	 * @throws XMLStreamException
	 *             the xML stream exception
	 */
	private void createNode(final XMLEventWriter eventWriter, final String name,
			final HashMap<String, String> d, final String string2,
			final String string3) throws XMLStreamException
	{

		XMLEventFactory eventFactory = XMLEventFactory.newInstance();
		XMLEvent end = eventFactory.createCharacters("\n");
		XMLEvent tab = eventFactory.createCharacters("\t");
		// Create Start node
		StartElement sElement = eventFactory.createStartElement(string2, string3, name);
		eventWriter.add(tab);
		eventWriter.add(sElement);
		// FIXME nullpointer abfangen!!1
		eventWriter.add(end);

		for (String str : d.keySet())
		{
			createNode(eventWriter, "docPart", d.get(str), str, PREFIX, URI);
		}
		if (d.isEmpty())
		{
			createNode(eventWriter, "docPart", "", "de", PREFIX, URI);

		}
		eventWriter.add(eventFactory.createEndElement(string2, string3, name));
		eventWriter.add(end);
	}


	/**
	 * Creates the node.
	 *
	 * @param eventWriter
	 *            the event writer
	 * @param name
	 *            the name
	 * @param textNode
	 *            the text node
	 * @param prefix
	 *            the prefix
	 * @param namespace
	 *            the namespace
	 * @throws XMLStreamException
	 *             the xML stream exception
	 */
	private void createNode(final XMLEventWriter eventWriter, final String name, final String textNode,
			final String prefix, final String namespace) throws XMLStreamException
	{
		XMLEventFactory eventFactory = XMLEventFactory.newInstance();
		XMLEvent end = eventFactory.createCharacters("\n");
		XMLEvent tab = eventFactory.createCharacters("\t");
		// Create Start node
		StartElement sElement = eventFactory.createStartElement(PREFIX, URI, name);
		eventWriter.add(tab);
		eventWriter.add(sElement);
		eventWriter.add(end);

		if (textNode.trim().length() > 0)
		{
		Characters characters = eventFactory.createCharacters(textNode.trim());
		eventWriter.add(characters);
		}
		eventWriter.add(eventFactory.createEndElement(PREFIX, URI, name));
		eventWriter.add(end);
	}


	/**
	 * Creates the node.
	 *
	 * @param eventWriter
	 *            the event writer
	 * @param name
	 *            the name
	 * @param value
	 *            the value
	 * @param lang
	 *            the lang
	 * @param string2
	 *            the string2
	 * @param string3
	 *            the string3
	 * @throws XMLStreamException
	 *             the xML stream exception
	 */
	private void createNode(final XMLEventWriter eventWriter, final String name,
			final String value, final String lang, final String string2, final String string3) throws XMLStreamException
	{
		XMLEventFactory eventFactory = XMLEventFactory.newInstance();
		XMLEvent end = eventFactory.createCharacters("\n");
		XMLEvent tab = eventFactory.createCharacters("\t");
		// Create Start node
		StartElement sElement = eventFactory.createStartElement(string2, string3, name);
		eventWriter.add(tab);
		eventWriter.add(sElement);
		eventWriter.add(eventFactory.createAttribute("xml:lang", lang));
		eventWriter.add(end);

		if (value.trim().length() > 0)
		{
		Characters characters = eventFactory.createCharacters(value.trim());
		eventWriter.add(characters);
		}
		eventWriter.add(eventFactory.createEndElement(string2, string3, name));
		eventWriter.add(end);
	}


	/**
 * Write to xml.
 *
 * @param d
 *            the d
 * @return the string
 * @throws XMLStreamException
 *             the xML stream exception
 */
public final String writeToXML(final DatatypeDesc d) throws XMLStreamException
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
		StartElement startElement = eventFactory.createStartElement(PREFIX, URI, "datatypeDesc");
		eventWriter.add(startElement);
		eventWriter.add(eventFactory.createNamespace(PREFIX, URI));

		// TODO namespace und schema einkommentieren
		// eventWriter.add(eventFactory.createAttribute("xmlns:xsi",
		// "http://www.w3.org/2001/XMLSchema-instance"));
		// eventWriter.add(eventFactory.createAttribute("xsi:schemaLocation",
		// "http://pdr.bbaw.de/namespaces/dtdl/ http://pdr.bbaw.de/schema/dtdl.xsd"));
		eventWriter.add(eventFactory.createAttribute("provider", d.getProvider()));

		eventWriter.add(end);

		// Write the different nodes
		if (d.getDocumentation() != null)
		{
			createNode(eventWriter, "documentation", d.getDocumentation(), PREFIX, URI);
		}
		if (d.getUsage() != null)
		{
			startElement = eventFactory.createStartElement(PREFIX, URI, "usage");
			eventWriter.add(startElement);
			eventWriter.add(end);

			if (d.getUsage().getDocumentation() != null)
			{
				createNode(eventWriter, "documentation", d.getUsage().getDocumentation(), PREFIX, URI);
			}
			if (d.getUsage().getUsageDisplay() != null)
			{
				startElement = eventFactory.createStartElement(PREFIX, URI, "display");
				eventWriter.add(startElement);
				eventWriter.add(end);
				if (d.getUsage().getUsageDisplay().getDocumentation() != null)
				{
					createNode(eventWriter, "documentation",
							d.getUsage().getUsageDisplay().getDocumentation(), PREFIX, URI);
				}
				if (d.getUsage().getUsageDisplay().getPersonNameTag() != null)
				{
					for (String s : d.getUsage().getUsageDisplay().getPersonNameTag())
					{
						createNode(eventWriter, "nameTag", s, PREFIX, URI);
					}
				}
				if (d.getUsage().getUsageDisplay().getPersonNormNameTag() != null)
				{
					for (String s : d.getUsage().getUsageDisplay().getPersonNormNameTag())
					{
						createNode(eventWriter, "displayNameTag", s, PREFIX, URI);
					}
				}
				eventWriter.add(eventFactory.createEndElement(PREFIX, URI, "display"));
				eventWriter.add(end);
			}
			if (d.getUsage().getTemplates() != null)
			{
				startElement = eventFactory.createStartElement(PREFIX, URI, "templates");
				eventWriter.add(startElement);
				eventWriter.add(end);

				if (d.getUsage().getTemplates() != null && !d.getUsage().getTemplates().getChildren().isEmpty()	)
				{
					for (String key : d.getUsage().getTemplates().getChildren().keySet())
					{
						ConfigData cd = d.getUsage().getTemplates().getChildren().get(key); // aspectTemplates
						startElement = eventFactory.createStartElement(PREFIX, URI, key);
						eventWriter.add(startElement);
						eventWriter.add(end);

						if (cd.getChildren() != null)
						{
							for (String key2 : cd.getChildren().keySet())
							{

								ConfigData cd2 = cd.getChildren().get(key2);
								startElement = eventFactory.createStartElement(PREFIX, URI, key2); // semanticTemplates
																									// and
																									// complexTemplates
								eventWriter.add(startElement);
								eventWriter.add(eventFactory.createAttribute("ignore",
										new Boolean(cd2.isIgnore()).toString()));
								eventWriter.add(end);

								if (cd2.getChildren() != null)
								{

									for (String key3 : cd2.getChildren().keySet())
									{
										ConfigData cd3 = cd2.getChildren().get(
												key3);
										if (cd3 instanceof SemanticTemplate)
										{
											SemanticTemplate semanticTemplate = (SemanticTemplate) cd3;
											createNode(eventWriter, semanticTemplate);
										}
										else
										{

												if (cd3 instanceof ComplexSemanticTemplate) // complexTemplate
												{
													ComplexSemanticTemplate complexTemplate = (ComplexSemanticTemplate) cd3;
													startElement = eventFactory.createStartElement(PREFIX, URI,
															"complexTemplate"); // semanticTemplates

													eventWriter.add(startElement);
													eventWriter.add(eventFactory.createAttribute("value",
															complexTemplate.getValue()));
													eventWriter.add(eventFactory.createAttribute("label",
															complexTemplate.getLabel()));
													eventWriter.add(eventFactory.createAttribute("priority",
															new Integer(complexTemplate.getPriority()).toString()));

													eventWriter.add(end);
													if (cd3.getChildren() != null)
													{
													for (String key4 : cd3.getChildren().keySet())
														{
														ConfigData cd4 = cd3.getChildren().get(key4);
														if (cd4 instanceof SemanticTemplate)
														{
															SemanticTemplate semanticTemplate = (SemanticTemplate) cd4;
															createNode(eventWriter, semanticTemplate);
														}
														else
														{

														}

														}
													}
													if (complexTemplate.getDocumentation() != null)
													{
														createNode(eventWriter, "documentation", complexTemplate.getDocumentation(), PREFIX, URI);
													}
													eventWriter.add(eventFactory.createEndElement(PREFIX, URI,
															"complexTemplate"));
													eventWriter.add(end);
												}
												else
												{
													startElement = eventFactory.createStartElement(PREFIX, URI, key3); // semanticTemplates
													eventWriter.add(startElement);
													eventWriter.add(end);

												if (cd3.getChildren() != null)
													{
													for (String key4 : cd3.getChildren().keySet())
														{
														ConfigData cd4 = cd3.getChildren().get(key4);
														if (cd4 instanceof SemanticTemplate)
														{
															SemanticTemplate semanticTemplate = (SemanticTemplate) cd4;
															createNode(eventWriter, semanticTemplate);
														}
														else
														{

														}

														}
													}
													eventWriter.add(eventFactory.createEndElement(PREFIX, URI, key3));
													eventWriter.add(end);
												}


										}

									}
								}
								eventWriter.add(eventFactory.createEndElement(PREFIX, URI, key2));
								eventWriter.add(end);
							}
						}

						eventWriter.add(eventFactory.createEndElement(PREFIX, URI, key));
						eventWriter.add(end);
					}
				}

				eventWriter.add(eventFactory.createEndElement(PREFIX, URI, "templates"));
				eventWriter.add(end);
			}
			if (d.getUsage().getIdentifiers() != null && d.getUsage().getIdentifiers().getChildren().size() > 0)
			{
				startElement = eventFactory.createStartElement(PREFIX, URI, "personIdentifiers"); // "personIdentifiers"
				eventWriter.add(startElement);
				eventWriter.add(end);

				for (String key : d.getUsage().getIdentifiers().getChildren().keySet())
				{
					ConfigData ci = (ConfigData) d.getUsage().getIdentifiers().getChildren().get(key);

					if (ci != null && ci instanceof IdentifierConfig)
					{
						IdentifierConfig ic = (IdentifierConfig) ci;
						startElement = eventFactory.createStartElement(PREFIX, URI, "identifier"); // "identifier"
						eventWriter.add(startElement);

						if (ic.getValue() != null)
						{
							eventWriter.add(eventFactory.createAttribute("value", ic.getValue()));
						}
						if (ic.getLabel() != null)
						{
							eventWriter.add(eventFactory.createAttribute("label", ic.getLabel()));
						}

						eventWriter.add(eventFactory.createAttribute("ignore", new Boolean(ic.isIgnore()).toString()));
						eventWriter.add(eventFactory.createAttribute("mandatory",
								new Boolean(ic.isMandatory()).toString()));
						eventWriter.add(eventFactory.createAttribute("priority",
								String.format("%03d", ic.getPriority())));

						if (ic.getUrl() != null)
						{
							eventWriter.add(eventFactory.createAttribute("url", ic.getUrl()));
						}
						if (ic.getPrefix() != null)
						{
							eventWriter.add(eventFactory.createAttribute("prefix", ic.getPrefix()));
						}
						if (ic.getSuffix() != null)
						{
							eventWriter.add(eventFactory.createAttribute("suffix", ic.getSuffix()));
						}
						if (ic.getRegex() != null)
						{
							eventWriter.add(eventFactory.createAttribute("regex", ic.getRegex()));
						}
						eventWriter.add(end);
						if (ic.getDocumentation() != null)
						{
							createNode(eventWriter, "documentation", ic.getDocumentation(), PREFIX, URI);
						}
						if (ic.getChildren() != null)
						{
							for (String str2 : ic.getChildren().keySet())
							{
								createNode(eventWriter, "item", ic.getChildren().get(str2));
							}
						}

						eventWriter.add(eventFactory.createEndElement(PREFIX, URI, "identifier"));
						eventWriter.add(end);
					}
				}

				eventWriter.add(eventFactory.createEndElement(PREFIX, URI, "personIdentifiers"));
				eventWriter.add(end);
			}
			eventWriter.add(eventFactory.createEndElement(PREFIX, URI, "usage"));
			eventWriter.add(end);
		}
		if (d.getChildren() != null)
		{
			for (String str : d.getChildren().keySet())
			{
				DataType dt = (DataType) d.getChildren().get(str);
			startElement = eventFactory.createStartElement(PREFIX, URI, "datatype");
			eventWriter.add(startElement);
			eventWriter.add(eventFactory.createAttribute("element", dt.getValue()));
			if (dt.getLabel() != null)
			{
				eventWriter.add(eventFactory.createAttribute("label", dt.getLabel()));
			}

//			if (d.getChildren().get(str).isMyHaveChildren())
//			{
			if (dt.getPos() != null)
			{
				eventWriter.add(eventFactory.createAttribute("type", dt.getPos()));
			}
			else
			{
//				System.out.println("dt pos = null " + dt.getValue());
			}
//			}
//			else
//			{
//				eventWriter.add(eventFactory.createAttribute("type", "list"));
//			}
			eventWriter.add(end);
			if (dt.getDocumentation() != null)
			{
				createNode(eventWriter, "documentation", dt.getDocumentation(), PREFIX, URI);
			}
			if (dt.getChildren() != null)
			{
				for (String str2 : dt.getChildren().keySet())
				{
					createNode(eventWriter, "item", dt.getChildren().get(str2));
				}
			}
			eventWriter.add(eventFactory.createEndElement(PREFIX, URI, "datatype"));
			eventWriter.add(end);
		}


		eventWriter.add(eventFactory.createEndElement(PREFIX, URI, "datatypeDesc"));
		eventWriter.add(end);

		eventWriter.add(eventFactory.createEndDocument());
		eventWriter.close();

		}
		// System.out.println(bout.toString());
		return bout.toString();
	}

	private void createNode(XMLEventWriter eventWriter, SemanticTemplate semTemplate) throws XMLStreamException
	{
		XMLEventFactory eventFactory = XMLEventFactory.newInstance();
		XMLEvent end = eventFactory.createCharacters("\n");
		// Create Start node
		StartElement startElement = eventFactory.createStartElement(PREFIX, URI, "semanticTemplate");
		eventWriter.add(startElement);
		eventWriter.add(eventFactory.createAttribute("semantic", semTemplate.getValue()));
		if (semTemplate.getLabel() != null)
		{
			eventWriter.add(eventFactory.createAttribute("label", semTemplate.getLabel()));
		}
		eventWriter.add(eventFactory.createAttribute("priority", String.format("%03d", semTemplate.getPriority())));
		if (semTemplate.isAllowMultiple())
		{
			eventWriter.add(eventFactory.createAttribute("allowMultiple", "true"));
		}
		else
		{
			eventWriter.add(eventFactory.createAttribute("allowMultiple", "false"));
		}
		eventWriter.add(end);
		if (!semTemplate.getChildren().isEmpty())
		{

			startElement = eventFactory.createStartElement(PREFIX, URI, "markupTemplates");
			eventWriter.add(startElement);
			if (semTemplate.isIgnore())
			{
				eventWriter.add(eventFactory.createAttribute("ignore", "true"));
			}
			else
			{
				eventWriter.add(eventFactory.createAttribute("ignore", "false"));
			}
			eventWriter.add(end);

			for (String key : semTemplate.getChildren().keySet())
			{
				AspectConfigTemplate markupTemplate = (AspectConfigTemplate) semTemplate.getChildren().get(key);
				startElement = eventFactory.createStartElement(PREFIX, URI, "markupTemplate");
				eventWriter.add(startElement);
				eventWriter.add(eventFactory.createAttribute("widgetType",
						new Integer(markupTemplate.getWidgetType()).toString()));
				eventWriter.add(eventFactory.createAttribute("priority",
						new Integer(markupTemplate.getPriority()).toString()));
				
				if (markupTemplate.isRequired())
				{
					eventWriter.add(eventFactory.createAttribute("requiered", "true"));
				}
				else
				{
					eventWriter.add(eventFactory.createAttribute("requiered", "false"));
				}
				if (markupTemplate.isIgnore())
				{
					eventWriter.add(eventFactory.createAttribute("ignore", "true"));
				}
				else
				{
					eventWriter.add(eventFactory.createAttribute("ignore", "false"));
				}
				if (markupTemplate.getElement() != null)
				{
					eventWriter.add(eventFactory.createAttribute("element", markupTemplate.getElement()));
				}
				if (markupTemplate.getType() != null)
				{
					eventWriter.add(eventFactory.createAttribute("type", markupTemplate.getType()));
				}
				if (markupTemplate.getSubtype() != null)
				{
					eventWriter.add(eventFactory.createAttribute("subtype", markupTemplate.getSubtype()));
				}
				if (markupTemplate.getRole() != null)
				{
					eventWriter.add(eventFactory.createAttribute("role", markupTemplate.getRole()));
				}
				if (markupTemplate.getDate1() != null)
				{
					eventWriter.add(eventFactory.createAttribute("date1", markupTemplate.getDate1()));
				}
				if (markupTemplate.getDate2() != null)
				{
					eventWriter.add(eventFactory.createAttribute("date2", markupTemplate.getDate2()));
				}
				if (markupTemplate.getPos() != null)
				{
					eventWriter.add(eventFactory.createAttribute("pos", markupTemplate.getPos()));
				}
				
				eventWriter.add(eventFactory.createAttribute("levelSpan",
						new Integer(markupTemplate.getLevelSpan()).toString()));
				if (markupTemplate.isAllowMultiple())
				{
					eventWriter.add(eventFactory.createAttribute("allowMultiple", "true"));
				}
				else
				{
					eventWriter.add(eventFactory.createAttribute("allowMultiple", "false"));
				}
				if (markupTemplate.getLabel() != null)
				{
					eventWriter.add(eventFactory.createAttribute("label", markupTemplate.getLabel()));
				}
				if (markupTemplate.getPrefix() != null)
				{
					eventWriter.add(eventFactory.createAttribute("prefix", markupTemplate.getPrefix()));
				}
				if (markupTemplate.getSuffix() != null)
				{
					eventWriter.add(eventFactory.createAttribute("suffix", markupTemplate.getSuffix()));
				}
				eventWriter.add(eventFactory.createAttribute("horizontalSpan",
						new Integer(markupTemplate.getHorizontalSpan()).toString()));

				if (markupTemplate.getDocumentation() != null)
				{
					createNode(eventWriter, "documentation", markupTemplate.getDocumentation(), PREFIX, URI);
				}
				eventWriter.add(eventFactory.createEndElement(PREFIX, URI, "markupTemplate"));
				eventWriter.add(end);
			}

			eventWriter.add(eventFactory.createEndElement(PREFIX, URI, "markupTemplates"));
			eventWriter.add(end);

		}

		if (semTemplate.getTemplateText() != null)
		{
			startElement = eventFactory.createStartElement(PREFIX, URI, "templateText");
			eventWriter.add(startElement);
			if (semTemplate.isIgnoreTemplateText())
			{
				eventWriter.add(eventFactory.createAttribute("ignore", "true"));
			}
			else
			{
				eventWriter.add(eventFactory.createAttribute("ignore", "false"));
			}
			eventWriter.add(end);

			Characters characters = eventFactory.createCharacters(semTemplate.getTemplateText());
			eventWriter.add(characters);

			eventWriter.add(eventFactory.createEndElement(PREFIX, URI, "templateText"));
			eventWriter.add(end);
		}

		if (semTemplate.getDocumentation() != null)
		{
			createNode(eventWriter, "documentation", semTemplate.getDocumentation(), PREFIX, URI);
		}

		eventWriter.add(eventFactory.createEndElement(PREFIX, URI, "semanticTemplate"));
		eventWriter.add(end);
	}
}
