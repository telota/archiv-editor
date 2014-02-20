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
package org.bbaw.pdr.ae.control.saxHandler;

import java.util.Vector;

import org.bbaw.pdr.ae.control.facade.Facade;
import org.bbaw.pdr.ae.model.view.Facet;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/**
 * The Class ComplexFacetSaxHandler.
 * @author Christoph Plutte
 */
public class ComplexFacetSaxHandler implements ContentHandler
{
	/** _facade singleton instance. */
	private Facade _facade = Facade.getInstanz();

	// /** The provider. */
	// private String _provider =
	// Platform.getPreferencesService().getString(CommonActivator.PLUGIN_ID,
	// "PRIMARY_SEMANTIC_PROVIDER", AEConstants.CLASSIFICATION_AUTHORITY, null);

	/** The facets. */
	private Vector<Facet> _facets = new Vector<Facet>();

	/** The facet. */
	private Facet _facet = new Facet();

	/** The b1. */
	private boolean _b1 = false;

	/** The b2. */
	private boolean _b2 = false;

	/** The b3. */
	private boolean _b3 = false;

	/** The _result object. */
	private Object _resultObject;

	@Override
	public final void characters(final char[] ch, final int start, final int length) throws SAXException
	{
		if (_b1)
		{
			// System.out.println("key tag " + new String(ch, start, length));

			// System.out.println("key check " +
			// _facade.getKeyOfPersonNormNameTag(new String(ch, start,
			// length)));
			if (_facade.getKeyOfPersonNormNameTag(new String(ch, start, length)) != null)
			{
				_facet.setType(1);
			}
			_b1 = false;
		}
		else if (_b2)
		{
			_facet.setValue(new String(ch, start, length));
			_b2 = false;
		}
		else if (_b3)
		{
			_facet.setKey(new String(ch, start, length));
			_b3 = false;
		}

	}

	@Override
	public void endDocument() throws SAXException
	{

	}

	@Override
	public final void endElement(final String uri, final String localName, final String qName) throws SAXException
	{
		if (localName.equals("result"))
		{
			setResultObject(_facets);
		}
		else if (localName.equals("complexFacet"))
		{
			boolean con = false;
			for (Facet f : _facets)
			{
				if (f.equals(_facet))
				{
					con = true;
				}
			}
			if (!con)
			{
				_facets.add(_facet);
			}
		}

	}

	@Override
	public void endPrefixMapping(final String prefix) throws SAXException
	{

	}

	/**
	 * Gets the result object.
	 * @return the result object
	 */
	public final Object getResultObject()
	{
		return _resultObject;
	}

	@Override
	public void ignorableWhitespace(final char[] ch, final int start, final int length) throws SAXException
	{
	}

	@Override
	public void processingInstruction(final String target, final String data) throws SAXException
	{
	}

	@Override
	public void setDocumentLocator(final Locator locator)
	{

	}

	/**
	 * Sets the result object.
	 * @param resultObject the new result object
	 */
	private void setResultObject(final Object resultObject)
	{
		this._resultObject = resultObject;
	}

	@Override
	public void skippedEntity(final String name) throws SAXException
	{

	}

	@Override
	public void startDocument() throws SAXException
	{

	}

	@Override
	public final void startElement(final String uri, final String localName, final String qName, final Attributes atts)
			throws SAXException
	{
		if (localName.equals("complexFacet"))
		{
			_facet = new Facet();
			_facet.setType(0);
		}
		else if (localName.equals("semanticStm"))
		{
			_b1 = true;
		}
		else if (localName.equals("facet"))
		{
			_b2 = true;

		}

		else if (localName.equals("key"))
		{
			_b3 = true;
		}
	}

	@Override
	public void startPrefixMapping(final String prefix, final String uri) throws SAXException
	{

	}
}
