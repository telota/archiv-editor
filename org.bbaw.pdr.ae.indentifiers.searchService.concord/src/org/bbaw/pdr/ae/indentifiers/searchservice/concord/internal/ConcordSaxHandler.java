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
package org.bbaw.pdr.ae.indentifiers.searchservice.concord.internal;

import java.util.Vector;

import org.bbaw.pdr.ae.view.identifiers.model.ConcurrenceData;
import org.bbaw.pdr.ae.view.identifiers.model.ConcurrenceIdentifier;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ConcordSaxHandler extends DefaultHandler
{
	private static final String serviceName = "PDR Concord Service";
	private int requestAffidability;
	private ConcurrenceData _concurrenceData;

	private Vector<ConcurrenceData> _concurrenceDatas = new Vector<ConcurrenceData>();
	private boolean _nameB;
	private boolean _otherNamesB;
	private boolean _birthB;
	private boolean _birthPlaceB;
	private boolean _deathB;
	private boolean _deathPlaceB;
	private boolean _identifierB;
	private boolean _descB;
	private boolean _genderB;
	private boolean _countryOfActB;
	private boolean _yearOfActB;
	private ConcurrenceIdentifier _identifier;
	private boolean _resultB;

	public ConcordSaxHandler()
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
		if (_nameB && _resultB)
		{
			_concurrenceData.setNormName(str);
			_nameB = false;
		}
		else if (_otherNamesB && _resultB)
		{
			_concurrenceData.addOtherName(str);
			_otherNamesB = false;
		}
		else if (_birthB && _resultB)
		{

				_concurrenceData.setDateOfBirth(str);

			_birthB = false;
		}
		else if (_birthPlaceB && _resultB)
		{
			_concurrenceData.setPlaceOfBirth(str);
			_birthPlaceB = false;
		}
		else if (_deathB && _resultB)
		{

				_concurrenceData.setDateOfDeath(str);

			_deathB = false;
		}
		else if (_deathPlaceB && _resultB)
		{
			_concurrenceData.setPlaceOfDeath(str);
			_deathPlaceB = false;
		}
		else if (_descB && _resultB)
		{
			_concurrenceData.setDescription(str);
			_descB = false;
		}
		else if (_genderB && _resultB)
		{
			_concurrenceData.setGender(str);
			_genderB = false;
		}
		else if (_identifierB && _resultB)
		{
			_identifier.setId(str);
			_identifierB = false;
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

		if (name.equals("queries") || qn.equals("queries"))
		{

		}
		else if (name.equals("results") || qn.equals("results"))
		{
			_resultB = false;
		}
		else if (name.equals("name") || qn.equals("name"))
		{
			_nameB = false;
		}
		else if (name.equals("otherNames") || qn.equals("otherNames"))
		{
			_otherNamesB = false;
		}
		else if (name.equals("dateOfBirth") || qn.equals("dateOfBirth"))
		{
			_birthB = false;
		}
		else if (name.equals("placeOfBirth") || qn.equals("placeOfBirth"))
		{
			_birthPlaceB = false;
		}
		else if (name.equals("dateOfDeath") || qn.equals("dateOfDeath"))
		{
			_deathB = false;
		}
		else if (name.equals("placeOfDeath") || qn.equals("placeOfDeath"))
		{
			_deathPlaceB = false;
		}
		else if (name.equals("description") || qn.equals("description"))
		{
			_descB = false;
		}
		else if (name.equals("personId") || qn.equals("personId"))
		{
			_identifierB = false;
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
		if (name.equals("results") || qn.equals("results"))
		{
			_resultB = true;
		}
		else if (name.equals("queries") || qn.equals("queries"))
		{
			for (int i = 0; i < a.getLength(); i++)
			{
				if (a.getQName(i).equals("requestQuality"))
				{
					requestAffidability = new Integer(a.getValue(i).split("%")[0]);
				}

			}
		}
		else if (name.equals("match") || qn.equals("match"))
		{
			_concurrenceData = new ConcurrenceData();
			_concurrenceData.setService(serviceName);
			for (int i = 0; i < a.getLength(); i++)
			{
				if (a.getQName(i).equals("optimal"))
				{
					_concurrenceData.setScore(new Integer(a.getValue(i).split("%")[0]));
				}
			}
			_concurrenceData.setMaxQueryScore(requestAffidability);
			_concurrenceDatas.add(_concurrenceData);
		}
		else if (name.equals("name") || qn.equals("name"))
		{
			_nameB = true;
		}
		else if (name.equals("otherNames") || qn.equals("otherNames"))
		{
			_otherNamesB = true;
		}
		else if (name.equals("dateOfBirth") || qn.equals("dateOfBirth"))
		{
			_birthB = true;
		}
		else if (name.equals("placeOfBirth") || qn.equals("placeOfBirth"))
		{
			_birthPlaceB = true;
		}
		else if (name.equals("dateOfDeath") || qn.equals("dateOfDeath"))
		{
			_deathB = true;
		}
		else if (name.equals("placeOfDeath") || qn.equals("placeOfDeath"))
		{
			_deathPlaceB = true;
		}
		else if (name.equals("description") || qn.equals("description"))
		{
			_descB = true;
		}
		else if (name.equals("gender") || qn.equals("gender"))
		{
			_genderB = true;
		}
		else if (name.equals("reference") || qn.equals("reference"))
		{
			for (int i = 0; i < a.getLength(); i++)
			{
				if (a.getQName(i).equals("url"))
				{
					_concurrenceData.setReferenceURL(a.getValue(i));
				}
			}
		}
		else if (name.equals("personId") || qn.equals("personId"))
		{
			_identifierB = true;
			_identifier = new ConcurrenceIdentifier();
			for (int i = 0; i < a.getLength(); i++)
			{
				if (a.getQName(i).equals("provider"))
				{
					_identifier.setProvider(a.getValue(i));
				}
				if (a.getQName(i).equals("url"))
				{
					_identifier.setUrl(a.getValue(i));
				}
			}
			_concurrenceData.addIdentifier(_identifier);
		}

	}

	public Vector<ConcurrenceData> getConcurrenceDatas()
	{
		return _concurrenceDatas;
	}
}