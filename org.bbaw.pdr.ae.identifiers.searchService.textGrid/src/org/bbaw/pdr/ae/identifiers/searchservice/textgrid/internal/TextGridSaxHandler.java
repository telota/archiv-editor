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
package org.bbaw.pdr.ae.identifiers.searchservice.textgrid.internal;

import java.util.Vector;

import org.bbaw.pdr.ae.view.identifiers.model.ConcurrenceData;
import org.bbaw.pdr.ae.view.identifiers.model.ConcurrenceIdentifier;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class TextGridSaxHandler extends DefaultHandler
{
	private ConcurrenceData _concurrenceData;
	private static final String serviceName = "TextGrid PND Service";
	private Vector<ConcurrenceData> _concurrenceDatas = new Vector<ConcurrenceData>();
	private boolean _nameB;
	private boolean _otherNamesB;
	
	private ConcurrenceIdentifier _identifier;

	private boolean _descB;
	public TextGridSaxHandler()
	{

	}

	/**
	 * @param ch chars
	 * @param start start
	 * @param len length of chars
	 * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
	 */
	@Override
	public final void characters(final char[] ch, final int start, final int len)
	{
		String str = new String(ch, start, len);
		if (_nameB)
		{
			_concurrenceData.setNormName(str);
			_nameB = false;
		}
		else if (_otherNamesB)
		{
			_concurrenceData.addOtherName(str);
			_otherNamesB = false;
		}
		else if (_descB)
		{
			_concurrenceData.setDescription(str);
			_descB = false;
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

		if (name.equals("preferred_name") || qn.equals("preferred_name"))
		{
			_nameB = false;
		}
		else if (name.equals("variant") || qn.equals("variant"))
		{
			_otherNamesB = false;
		}
		else if (name.equals("info") || qn.equals("info"))
		{
			_descB = false;
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
		
		if (name.equals("person") || qn.equals("person"))
		{
			_concurrenceData = new ConcurrenceData();
			_concurrenceData.setService(serviceName);
			_identifier = new ConcurrenceIdentifier();
			_identifier.setProvider("PND");
			for (int i = 0; i < a.getLength(); i++)
			{
				if (a.getQName(i).equals("id"))
				{
					_identifier.setId(a.getValue(i).substring(4));
				}

			}
			_identifier.setUrl("http://d-nb.info/gnd/" + _identifier.getId());
			_concurrenceData.addIdentifier(_identifier);
			_concurrenceDatas.add(_concurrenceData);

		}
		
		else if (name.equals("preferred_name") || qn.equals("preferred_name"))
		{
			_nameB = true;
		}
		else if (name.equals("variant") || qn.equals("variant"))
		{
			_otherNamesB = true;
		}
		else if (name.equals("info") || qn.equals("info"))
		{
			_descB = true;
		}

	}

	public Vector<ConcurrenceData> getConcurrenceDatas()
	{
		return _concurrenceDatas;
	}
}