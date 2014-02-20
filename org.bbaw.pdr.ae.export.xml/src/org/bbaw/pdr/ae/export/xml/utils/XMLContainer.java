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
package org.bbaw.pdr.ae.export.xml.utils;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.bbaw.pdr.ae.common.AEConstants;
import org.bbaw.pdr.ae.common.CommonActivator;
import org.bbaw.pdr.ae.control.comparator.ReferenceByAuthorTitleComparator;
import org.bbaw.pdr.ae.control.core.XMLProcessor;
import org.bbaw.pdr.ae.control.core.XMLProcessorInterface;
import org.bbaw.pdr.ae.control.facade.Facade;
import org.bbaw.pdr.ae.export.pluggable.AeExportCoreProvider;
import org.bbaw.pdr.ae.metamodel.PdrId;
import org.bbaw.pdr.ae.model.Aspect;
import org.bbaw.pdr.ae.model.PdrObject;
import org.bbaw.pdr.ae.model.Person;
import org.bbaw.pdr.ae.model.ReferenceMods;
import org.bbaw.pdr.ae.model.view.OrderingHead;
import org.bbaw.pdr.ae.view.control.PDRObjectsProvider;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

/**
 * Holds given XML Data as a DOM tree and provides merging XML and transforming
 * XML via XSLT and XSL-FO.
 * <p>
 * The DOM tree can be created out of an XMl-formatted String.
 * </p>
 * @author jhoeper
 */
public class XMLContainer
{
	
	private ILog log = AEConstants.ILOGGER;

	static String ELEMENT_PDR_OBJECT = "pdrEntity"; 
	static String ELEMENT_PDR_GROUPED = "pdrAspectsGroup";
	static String ATTRIBUTE_LABEL = "label";
	static String ATTRIBUTE_ID = "id";
	static String ATTRIBUTE_GROUP_CONDITION = "classification";
	/**
	 * DOM document containing the XML data.
	 */
	private Document _document = null;
	/**
	 * Builder used to parse XML Data given as Strings into DOM documents.
	 */
	private DocumentBuilder _builder = null;

	/**
	 * Sets up Container with an empty DOM document.
	 */
	public XMLContainer()
	{
		setupBuilder();
		_document = _builder.newDocument();
	}

	/**
	 * Creates a new Container containing given DOM Document.
	 * @param doc DOM
	 */
	public XMLContainer(final Document doc)
	{
		setupBuilder();
		_document = doc;
	}

	/**
	 * Sets up the Container and creates DOM tree out of XML formatted String.
	 * @param xml XML String
	 */
	public XMLContainer(final String xml)
	{
		setupBuilder();
		try
		{
			_document = _builder.parse(
					new ByteArrayInputStream(xml.getBytes("UTF-8")));
		}
		catch (Exception e)
		{
			log.log(new Status(IStatus.ERROR, CommonActivator.PLUGIN_ID, 
					"Couldn't build DOM tree for XML Data"));
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Creates an instance of XMLContainer containing an XML representation of the
	 * given PdrObjects.
	 * <p>The array where the PdrObjects come in may either be of the common type,
	 * meaning that it holds only objects of the type {@link Person} or it may as
	 * well be of mixed types, most likely {@link Person}s and {@link Aspect}s
	 * that don't need to be in any particular order.</p>
	 * <p>In the first case, the constructor doesn't mind collecting the 
	 * corresponding <code>Aspect</code>s itself, with the support of the
	 * {@link PDRObjectsProvider} class. In the latter case, the XML being put
	 * together along with the container creation will actually only contain 
	 * aspects provided by the given array explicitly. By doing so, it is even
	 * possible to create an XML representation merely containing {@link Person}
	 * elements.</p>
	 * <p>What about references??</p>
	 * @param exportObjects
	 */
	public XMLContainer(PdrObject[] exportObjects) {
		this();
		XMLProcessorInterface xmlproc = new XMLProcessor();
		
/*		setRootNode("export", new String[]
		{"date"}, new String[]
		{new Date().toString()});*/
		setRootNode("export", new String[]{"xmlns:xsi"},
				new String[]{"http://www.w3.org/2001/XMLSchema-instance"});
		appendText("\n\n");		
		
		Vector<Person> persons = new Vector<Person>();
		Vector<Aspect> aspects = new Vector<Aspect>();
		
		// FIXME: nicht auf Person beschränken, OrderingHeads zulassen?
		for (PdrObject obj : exportObjects)	{
			if (obj instanceof Person) {
				Person person = (Person) obj;
				// try to export xml data of the Person
				try	{
					importXML(xmlproc.writeToXML(person));
					appendText("\n\n");
					persons.add(person);
				} catch (Exception e) {
					appendComment(" ERROR EXPORTING PERSON DATA ");
					e.printStackTrace();
				}
			} else if (obj instanceof Aspect) {
				Aspect aspect = (Aspect) obj;
				// stack aspect object for later use
				aspects.add(aspect);
			}
		}
		
		log.log(new Status(IStatus.INFO, CommonActivator.PLUGIN_ID, 
				"number of persons to export: "+persons.size()));
		log.log(new Status(IStatus.INFO, CommonActivator.PLUGIN_ID, 
				"number of aspects to export: "+aspects.size()));
		
		
		// set up PDR object provider
		// FIXME: PdrObjectsProvider nicht anfassen! Ausgewählte PdrObjects/OrderingHeads(?) kommen
		// vom jeweiligen Export-Plugin und werden brav abgearbeitet.
		PDRObjectsProvider provider = AeExportCoreProvider.getInstance().getPdrObjectsProvider();
		provider.setInput(persons);

		// provider.setOrderer(new AspectsBySemanticOrderer());

		// prepare processing of aspects
		Vector<OrderingHead> aspectGroups;
		if (aspects.size() < 1) {
			// if no aspects in method argument, get them via provider
			aspectGroups = provider.getArrangedAspects();
		} else {
			// if aspects have been passed within argument, use those
			aspectGroups = new Vector<OrderingHead>();
			OrderingHead aspectGroup = new OrderingHead(); 
			for (Aspect a : aspects)
				aspectGroup.addAspect(a);
			aspectGroups.add(aspectGroup);
		}
		

		appendText("\n\n");
		appendComment("  BEGIN ASPECT LIST  ");

		// try to export xml data of the person's aspects

		for (OrderingHead aspectGroup : aspectGroups)
		{
			aspects = aspectGroup.getAspects();
			for (Aspect a : aspects)
			{
				try
				{
					importXML(xmlproc.writeToXML(a));
					appendText("\n\n");
				}
				catch (Exception e)
				{
					appendComment(" ERROR EXPORTING ASPECTS ");
					e.printStackTrace();
				}
			}
		}

		// retrieve references
		provider.setOrderer(null);
		provider.setRefOrderer(null);
		// catch nullpointer exception 
		// at org.bbaw.pdr.ae.view.control.PDRObjectsProvider.
		// getArrangedReferences(PDRObjectsProvider.java:421)
		try {
			provider.setRefComparator(new ReferenceByAuthorTitleComparator(true));
		} catch (Exception e) {	}
		// FIXME nullpointer
		// at org.bbaw.pdr.ae.view.control.PDRObjectsProvider$2.run(PDRObjectsProvider.java:450)
		// bzw at org.bbaw.pdr.ae.view.control.PDRObjectsProvider.getArrangedReferences(PDRObjectsProvider.java:421)
		Vector<OrderingHead> referenceGroups = null;
		try {
			referenceGroups = provider.getArrangedReferences();
		} catch (Exception e) { };

		appendText("\n\n");
		appendComment("  BEGIN REFERENCE LIST  ");
		appendText("\n");

		// try to export xml data of the references concerning the person
		if (referenceGroups != null)
			try	{
				//FIXME nullpointer
				// at org.bbaw.pdr.ae.view.control.PDRObjectsProvider$2.run(PDRObjectsProvider.java:450)
				for (OrderingHead referenceGroup : referenceGroups)	{
					Vector<ReferenceMods> references = referenceGroup.getReferences();
					for (ReferenceMods r : references)	{
						importXML(xmlproc.writeToXML(r));
						appendText("\n\n");
					}
				}
			}
			catch (Exception e)
			{
				appendComment(" ERROR EXPORTING REFERENCES ");
				e.printStackTrace();
			}	
		//TODO: effect?
		this._document.normalize();
	}
	
	/**
	 * Instantiates a new {@link XMLContainer} containing the XML representation
	 * of the provided list of {@link OrderingHead}.
	 * @param heads
	 */
	public XMLContainer(Collection<OrderingHead> heads) {
		this();
		XMLProcessorInterface xmlproc = new XMLProcessor();
		
		setRootNode("export", new String[]{"xmlns:xsi"},
				new String[]{"http://www.w3.org/2001/XMLSchema-instance"});
		Vector<ReferenceMods> references = new Vector<ReferenceMods>();

		// encode root level of passed structure, assuming to find Person objects
		for (OrderingHead head : heads) {
			PdrId id = new PdrId(head.getValue());
			if (id.isValid()) {
				Person p = Facade.getInstanz().getPerson(id);
				//System.out.println(p.getDisplayNameWithID());
				HashMap<String, String> attributes = new HashMap<String, String>();
				attributes.put(ATTRIBUTE_ID, head.getValue());
				attributes.put(ATTRIBUTE_LABEL, p.getDisplayName());
				//
				appendComment("PdrObject: "+p.getPdrId().toString());
				Element entityNode = appendNode(ELEMENT_PDR_OBJECT, attributes, null);
				// person/pdr object xml
				try {
					importXML(xmlproc.writeToXML(p), entityNode);
				} catch (Exception e) {
					log.log(new Status(IStatus.ERROR, CommonActivator.PLUGIN_ID, 
						"Failed to write xml representation for Person "+p.getDisplayName()));
					e.printStackTrace();
				}
				// orderinghead/groups xml 
				appendComment("Start aspect group list", entityNode);
				for (OrderingHead cat : head.getSubCategories()) {
					// collect references
					for (ReferenceMods ref : cat.getReferences())
						if (!references.contains(ref))
							references.add(ref);
					// extract attributes
					String classification = cat.getValue().substring(cat.getValue().indexOf("grouped"));
					attributes = new HashMap<String, String>();
					attributes.put(ATTRIBUTE_GROUP_CONDITION, classification);
					attributes.put(ATTRIBUTE_ID, cat.getValue());
					attributes.put(ATTRIBUTE_LABEL, cat.getLabel());
					appendComment(cat.getLabel()+"; "+cat.getAspects().size()+" aspects", entityNode);
					Element catNode = appendNode(entityNode, ELEMENT_PDR_GROUPED, attributes, null);
					// aspects xml
					for (Aspect aspect : cat.getAspects())
						try {
							appendComment(aspect.getDisplayNameWithID(), catNode);
							importXML(xmlproc.writeToXML(aspect), catNode);
						} catch (Exception e) {
							log.log(new Status(IStatus.ERROR, CommonActivator.PLUGIN_ID, 
								"Failed to write xml representation for Aspect "+aspect.getDisplayName()));
							e.printStackTrace();
						}
					if (cat.getAspects().size()>0)
						appendComment("end of aspects list", catNode);
				}
				entityNode.normalize();
			}	
		}
		appendComment("Bibliography - "+references.size()+" sources");
		this._document.getDocumentElement().normalize();
		for (ReferenceMods ref : references) {
			/*appendComment(ref.getNameMods().firstElement().getFullName() + 
					(ref.getNameMods().size()>1 ? " et al." : ""));
			appendComment(ref.getDisplayName());*/
			try {
				importXML(xmlproc.writeToXML(ref));
			} catch (Exception e) {
				log.log(new Status(IStatus.ERROR, CommonActivator.PLUGIN_ID, 
					"Could not import XML for reference "+ref.getDisplayName()));
				e.printStackTrace();
			}
		}
		this._document.getDocumentElement().normalize();
		this._document.normalize();
		this._document.normalizeDocument();
	}
	

	/**
	 * appends comment of given content to the document's root node.
	 * @param text text
	 */
	public final void appendComment(final String text) {
		appendComment(text, _document.getDocumentElement());
	}
	
	/**
	 * Appends a comment to a given node.
	 * @param text
	 * @param node
	 */
	public final void appendComment(final String text, Element node)
	{
		Comment comment = _document.createComment(text);
		try {
			node.appendChild(comment);
		} catch (DOMException e) {
			e.printStackTrace();
			log.log(new Status(IStatus.ERROR, CommonActivator.PLUGIN_ID, 
					"Couldn't append Node. Is node set?"));
		}
	}

	/**
	 * appends a new node of the name <code>name</code> and which the given
	 * attributes to the document's root node.<br/>
	 * appends a text node of the given text
	 * @param name name
	 * @param attributes attributes
	 * @param text text
	 */
	public final Element appendNode(Element parent, final String name, final HashMap<String, String> attributes, final String text)
	{
		Element element = _document.createElement(name);

		if (attributes != null)
		{
			for (String k : attributes.keySet())
			{
				element.setAttribute(k, attributes.get(k));
			}
		}

		if (text != null)
			element.appendChild(_document.createTextNode(text));

		appendText("\n");
		try
		{
			parent.appendChild(element);
		}
		catch (DOMException e)
		{
			e.printStackTrace();
			log.log(new Status(IStatus.ERROR, CommonActivator.PLUGIN_ID, 
					"Couldn't append Node. Does parent element exist?"));
		}
		appendText("\n");
		return element;
	}
	
	public Element appendNode(final String name, final HashMap<String, String> attributes, final String text) {
		return appendNode(_document.getDocumentElement(), name, attributes, text);
	}

	/**
	 * appends a {@link Text} node to this document's root node.
	 * @param text Text to be written to XML
	 */
	public final void appendText(final String text) {
		appendText(text, _document.getDocumentElement());
	}
	
	/**
	 * Attaches a new {@link Text} node to the given XML {@link Element}. 
	 * @param text
	 * @param node {@link Element} to which text is to be attached. If null,
	 * current {@link Document}'s root is used.
	 */
	public final void appendText(final String text, Element node)
	{
		Text textNode = _document.createTextNode(text);
		if (node == null)
			node = _document.getDocumentElement();
		try
		{
			node.appendChild(textNode);
		}
		catch (DOMException e)
		{
			e.printStackTrace();
			log.log(new Status(IStatus.ERROR, CommonActivator.PLUGIN_ID, 
					"Couldn't append Text Node. Does parent node exist?"));
		}
	}

	/**
	 * Imports an XML-formatted String and merges the included XML structure
	 * with the one already loaded.
	 * @param xml XML-formatted String
	 * @throws SAXException exc
	 * @throws IOException exc
	 * @return the created {@link Node}
	 */
	public final Node importXML(final String xml, Element node) throws SAXException, 
																IOException	{
		if (xml == null) {
			log.log(new Status(IStatus.ERROR, CommonActivator.PLUGIN_ID, 
					"String is NULL"));
			return null;
		}
		if (_builder == null) {
			log.log(new Status(IStatus.ERROR, CommonActivator.PLUGIN_ID, 
					"No builder set up"));
			return null;
		}
		if (_document == null) {
			log.log(new Status(IStatus.ERROR, CommonActivator.PLUGIN_ID, 
					"No document set up"));
			return null;
		}
		
		// Create DOM of the XML String to attach
		Document doc = _builder.parse(new ByteArrayInputStream(
						xml.getBytes("UTF-8")));

		//TODO any effect?
		doc.normalize();
		// Extracts all direct child nodes of the DOM to import
		NodeList nodes = doc.getChildNodes();

		String nodeName = null;
		Node impNode = null;
		
		node.appendChild(_document.createTextNode("\n"));
				
		// append all child nodes of the DOM to import to the root node of the
		// currently contained DOM
		for (int i = 0; i < nodes.getLength(); i++) 	{
			try	{
				impNode = _document.importNode(nodes.item(i), true);
				String idTag = null;

				// Try to get an aspect or reference ID
				nodeName = impNode.getNodeName();

				/*System.out.println(impNode.getBaseURI());
				System.out.println(impNode.getNamespaceURI());
				System.out.println();*/
				
				if (impNode.hasAttributes()) {
					NamedNodeMap attr = impNode.getAttributes();
					idTag = nodeName.equals("aodl:aspect") ? "id" : "ID";
					Node id = attr.getNamedItem(idTag);
					// insert aspect ID as a comment into XML dump
					if (id != null) 	{
						node.appendChild(_document.createComment(
								(nodeName.equals("aodl:aspect") ? " ASPECT "
								: " REFERENCE ") + id + " "));
						//node.appendChild(_document.createTextNode("\n"));
					}
					
					for (int a=0; a<attr.getLength(); a++) {
						Node att = attr.item(a);
						String ns = att.getPrefix();
/*						System.out.println(ns);
						System.out.println(att.getLocalName());
						System.out.println(att.getNamespaceURI());
						System.out.println();*/
						if (ns != null) 
							if (ns.matches("xmlns")) {
								node.setAttribute(att.getNodeName(), 
													att.getNodeValue());
								/*System.out.println(att.getNodeName()+"="+
													att.getNodeValue());*/
								attr.removeNamedItem(att.getNodeName());
							}
					}
				}
				
				node.appendChild(impNode);
			}
			catch (DOMException e)
			{
				log.log(new Status(IStatus.ERROR, CommonActivator.PLUGIN_ID, 
						"Error at Node No " + i));
				e.printStackTrace();
				return null;
			}
		}
		
		node.normalize();

		// System.out.println("imported "+nodes.getLength()+" Nodes");
		return impNode;
	}
	
	/**
	 * Inserts elements encoded by the XML structure in a given string as children
	 * of this document's root element. 
	 * @param xml
	 * @return
	 * @throws SAXException
	 * @throws IOException
	 */
	public Node importXML(String xml) throws SAXException, IOException {
		return importXML(xml, _document.getDocumentElement());
	}



	/**
	 * Creates a new root node and appends all contained nodes to it.
	 * @param tagname tag name of the root node to create
	 * @return true on success, false otherwise
	 */
	public final boolean setRootNode(final String tagname)
	{
		return setRootNode(tagname, null, null);
	}

	/**
	 * Encapsules the complete DOM tree under a new root node.
	 * <p>
	 * A node name is required and optional attributes can be set.
	 * </p>
	 * @param name the name of the node
	 * @param attributes optional attributes as String[]
	 * @param values values of the attributes as String[]
	 * @return true usually
	 */
	public final boolean setRootNode(final String name, final String[] attributes, final String[] values)
	{
		if (_builder == null) {
			log.log(new Status(IStatus.ERROR, CommonActivator.PLUGIN_ID, 
					"No builder set up"));
			return false;
		}

		// create empty document
		Document doc = _builder.newDocument();
		//doc.getDomConfig().setParameter("canonical-form", true);

		// get root Nodes of current document
		NodeList nodes = _document.getChildNodes();

		// create new root element in empty document
		Element root = doc.createElement(name);

		// apply attributes
		if (attributes != null && values != null)
		{
			for (int i = 0; i < Math.min(attributes.length, values.length); i++)
			{
				root.setAttribute(attributes[i], values[i]);
			}
		}

		// import nodes from document and append them to new root node
		Node impNode = null;
		for (int i = 0; i < nodes.getLength(); i++)
		{
			impNode = doc.importNode(nodes.item(i), true);
			root.appendChild(impNode);
		}
		
		doc.appendChild(root);

		_document = doc;

		log.log(new Status(IStatus.INFO, CommonActivator.PLUGIN_ID, 
				"Set up new root node <" + _document.getDocumentElement().getNodeName() + ">"));

		return true;

	}

	/**
	 * setup builder.
	 */
	private void setupBuilder()
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		try
		{
			_builder = factory.newDocumentBuilder();
		}
		catch (ParserConfigurationException e)
		{
			e.printStackTrace();
			log.log(new Status(IStatus.ERROR, CommonActivator.PLUGIN_ID, 
					"failed on instantiating Document builder"));
		}
	}

	@Override
	public final String toString()
	{
		StringWriter out = new StringWriter();
		Result result = new StreamResult(out);

		Source src = new DOMSource(_document);

		try
		{
			TransformerFactory transFactory = TransformerFactory.newInstance();
			transFactory.setAttribute("indent-number", 8);
			Transformer trans = transFactory.newTransformer();
			trans = SAXTransformerFactory.newInstance().newTransformer();
			
			trans.setOutputProperty(OutputKeys.INDENT, "yes");
			trans.setOutputProperty(
					"{http://xml.apache.org/xslt}indent-amount", "2");
			
			trans.transform(src, result);
		}
		catch (Exception e)
		{
			log.log(new Status(IStatus.ERROR, CommonActivator.PLUGIN_ID, 
					"Could not convert DOM to String"));
			e.printStackTrace();
			return "";
		}

		return out.toString();
	}
	
	/**
	 * Returns a {@link Source} object for the XML which this container is
	 * currently holding.
	 * @return {@link StreamSource} object ready to be processed by a 
	 * {@link Transformer}, or <code>null</code> if something goes wrong
	 */
	public Source getStream() {
		StreamSource source = null;
		try {
			source = new StreamSource(
					new ByteArrayInputStream(this.toString().getBytes("UTF-8")));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			log.log(new Status(IStatus.ERROR, CommonActivator.PLUGIN_ID, 
					"Could not retrieve Stream Source for XML."));
		}
		return source;
	}
	
	/**
	 * Saves XML contents to file. 
	 * @param filename
	 */
	public void saveToFile(String filename) throws Exception {
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(filename), "UTF-8"));
		writer.write(this.toString());
		writer.close();
	}

}
