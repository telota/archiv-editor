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

import javax.xml.stream.XMLStreamException;

import org.bbaw.pdr.ae.model.Aspect;
import org.bbaw.pdr.ae.model.Person;
import org.bbaw.pdr.ae.model.ReferenceMods;

/**
 * This Interface defines methods for creating XML dumps of certain PDR objects
 * <p>There are two classes which implement this interface: <code>XMLProcessor</code>
 * and <code>ExportXMLProcessor</code>. Both create valid XML for Person, Aspect and
 * Reference Objects ready to be saved to disc.</p>
 * @author praktikum
 *
 */
public interface XMLProcessorInterface
{

	/**
	 * creates XML containing an <code>Aspect</code>'s content and attributes.
	 * @param a an Aspect.
	 * @return an XML formatted String.
	 * @throws XMLStreamException Exception.
	 */
	String writeToXML(Aspect a) throws XMLStreamException;

	/**
	 * creates XML containing an <code>Person</code>'s content and attributes.
	 * @param p a Person.
	 * @return an XML formatted String.
	 * @throws XMLStreamException Exception.
	 */
	String writeToXML(Person p) throws XMLStreamException;

	/**
	 * creates XML containing an <code>ReferenceMods</code>' content and attributes.
	 * @param r a Reference.
	 * @return an XML formatted String.
	 * @throws XMLStreamException Exception.
	 */
	String writeToXML(ReferenceMods r) throws XMLStreamException;
}
