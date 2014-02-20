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
/*
 * @author: Christoph Plutte
 */
package org.bbaw.pdr.ae.control.saxHandler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import org.bbaw.pdr.ae.metamodel.PdrId;
import org.bbaw.pdr.ae.metamodel.Record;
import org.bbaw.pdr.ae.metamodel.Revision;
import org.bbaw.pdr.ae.model.Concurrence;
import org.bbaw.pdr.ae.model.Concurrences;
import org.bbaw.pdr.ae.model.Identifier;
import org.bbaw.pdr.ae.model.Identifiers;
import org.bbaw.pdr.ae.model.Person;
import org.bbaw.pdr.ae.model.Reference;
import org.bbaw.pdr.ae.model.ValidationStm;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * The Class PersonSaxHandler.
 * @author Christoph Plutte
 */
public class PersonSaxHandler extends DefaultHandler implements ContentHandler
{

	/** The admin data format. */
	private SimpleDateFormat _adminDataFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");

	/** The all persons. */
	private HashMap<PdrId, Person> _allPersons = new HashMap<PdrId, Person>();

	/** The person. */
	private Person _person = null;

	// revision
	/** The revision. */
	private Revision _revision = null;

	/** The record. */
	private Record _record = null;

	// identifiers
	/** The identifiers. */
	private Identifiers _identifiers = null;

	/** The identifier. */
	private Identifier _identifier = null;

	// concurrences
	/** The concurrences. */
	private Concurrences _concurrences = null;

	/** The concurrence. */
	private Concurrence _concurrence = null;

	/** The validation stm. */
	private ValidationStm _validationStm = null;

	/** The reference. */
	private Reference _reference = null;

	/** The revision b. */
	private boolean _revisionB = false;

	/** The identifier b. */
	private boolean _identifierB = false;

	/** The reference b. */
	private boolean _referenceB = false;

	/** The interpretation. */
	private boolean _interpretation = false;

	/** The _result object. */
	private Object _resultObject;

	@Override
	public final void characters(final char[] ch, final int start, final int len)
	{
		// System.out.println("Characters:\t\"" + new String(ch, start, len) +
		// "\"");

		// record and revision
		if (_revisionB)
		{
			_revision.setRevisor(new String(ch, start, len));
			_revisionB = false;
		}
		// identifiers
		if (_identifierB)
		{
			_identifier.setIdentifier(new String(ch, start, len));
			_identifierB = false;
		}
		if (_interpretation)
		{
			_validationStm.setInterpretation(new String(ch, start, len));
			_interpretation = false;
		}
		// concurrences
		if (_referenceB)
		{
			_reference.setSourceId(new PdrId(new String(ch, start, len)));
			_referenceB = false;
		}
		// auskommentiert, weil Klasse zz nicht benutzt wird.

		// // notification surName
		// if (b4)
		// {
		// bpd.setSurName(bpd.getSurName() + " " + new String(ch, start,
		// len).trim());
		// System.out.println("surname: " + bpd.getSurName());
		// b4 = false;
		// }
		// // notification foreName
		// if (b5)
		// {
		// bpd.setForeName(bpd.getForeName() + " " + new String(ch, start,
		// len).trim());
		// System.out.println("forename: " + bpd.getForeName());
		// b5 = false;
		// }
		// // notification nameLink
		// if (b6)
		// {
		// bpd.setNameLink(bpd.getNameLink() + " " + new String(ch, start,
		// len).trim());
		// System.out.println("namelink: " + bpd.getNameLink());
		// b6 = false;
		// }

	}

	@Override
	public final void endDocument() throws SAXException
	{
		if (!_allPersons.isEmpty())
		{
			setResultObject(_allPersons);
		}
		else
		{
			setResultObject(_person);
		}
	}

	@Override
	public final void endElement(final String u, final String name, final String qn)
	{
		if (name.equals("person") || name.equals("podl:person") || qn.equals("person") || qn.equals("podl:person"))
		{
			_allPersons.put(_person.getPdrId(), _person);
		}
		else if (name.equals("record") || name.equals("podl:record") || qn.equals("record") || qn.equals("podl:record"))
		{
			_person.setRecord(_record);
		}
		else if (name.equals("revision") || name.equals("podl:revision") || qn.equals("revision")
				|| qn.equals("podl:revision"))
		{
			_revisionB = false;
			_record.getRevisions().add(_revision);
		}
		else if (name.equals("identifiers") || name.equals("podl:identifiers") || qn.equals("identifiers")
				|| qn.equals("podl:identifiers"))
		{
			_person.setIdentifiers(_identifiers);
		}
		else if (name.equals("identifier") || name.equals("podl:identifier") || qn.equals("identifier")
				|| qn.equals("podl:identifier"))
		{
			_identifierB = false;
			_identifiers.getIdentifiers().add(_identifier);
		}
		else if (name.equals("concurrences") || name.equals("podl:concurrences") || qn.equals("concurrences")
				|| qn.equals("podl:concurrences"))
		{
			_person.setConcurrences(_concurrences);
		}
		else if (name.equals("concurrence") || name.equals("podl:concurrence") || qn.equals("concurrence")
				|| qn.equals("podl:concurrence"))
		{
			_concurrences.getConcurrences().add(_concurrence);
		}
		else if (name.equals("validationStm") || name.equals("podl:validationStm") || qn.equals("validationStm")
				|| qn.equals("podl:validationStm"))
		{
			_concurrence.getReferences().add(_validationStm);
		}
		else if (name.equals("interpretation") || name.equals("podl:interpretation") || qn.equals("interpretation")
				|| qn.equals("podl:interpretation"))
		{
			_interpretation = false;
		}
		else if (name.equals("reference") || name.equals("podl:reference") || qn.equals("reference")
				|| qn.equals("podl:reference"))
		{
			_referenceB = false;
			_validationStm.setReference(_reference);
		}

		// else if (name.equals("result"))
		// {
		// // mainSearcher.setResultingPerson(person);
		// setResultObject(_allPersons);
		// // System.out.println("allpersons set size " + allPersons.size());
		// }
	}

	@Override
	public void endPrefixMapping(final String prefix) throws SAXException
	{

	}

	/**
	 * get result object.
	 * @return result object
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
	 * set result object.
	 * @param resultObject result object
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
	public final void startElement(final String u, final String name, final String qn, final Attributes a)
	{
		// id and new person
		if (name.equals("person") || name.equals("podl:person") || qn.equals("person") || qn.equals("podl:person"))
		{
			for (int i = 0; i < a.getLength(); i++)
			{
				if (a.getQName(i).equals("id"))
				{
					_person = new Person(a.getValue(i));
				}
			}
		}

		else if (name.equals("record") || name.equals("podl:record") || qn.equals("record") || qn.equals("podl:record"))
		{
			_record = new Record();
		}
		else if (name.equals("revision") || name.equals("podl:revision") || qn.equals("revision")
				|| qn.equals("podl:revision"))
		{
			_revisionB = true;
			_revision = new Revision();
			for (int i = 0; i < a.getLength(); i++)
			{

				if (a.getQName(i).equals("ref"))
				{
					_revision.setRef(Integer.valueOf(a.getValue(i)).intValue());
				}
				else if (a.getQName(i).equals("timestamp"))
				{
					try
					{
						_revision.setTimeStamp(_adminDataFormat.parse(a.getValue(i)));
					}
					catch (ParseException e)
					{
						e.printStackTrace();
					}
				}
				else if (a.getQName(i).equals("authority"))
				{
					_revision.setAuthority(new PdrId(a.getValue(i)));
				}
			}
		}

		// identifiers
		else if (name.equals("identifiers") || name.equals("podl:identifiers") || qn.equals("identifiers")
				|| qn.equals("podl:identifiers"))
		{
			_identifiers = new Identifiers();
		}

		// identifier
		if (name.equals("identifier") || name.equals("podl:identifier") || qn.equals("identifier")
				|| qn.equals("podl:identifier"))
		{
			_identifierB = true;
			_identifier = new Identifier();
			for (int i = 0; i < a.getLength(); i++)
			{

				if (a.getQName(i).equals("provider"))
				{
					_identifier.setProvider(a.getValue(i));
				}
				else if (a.getQName(i).equals("quality"))
				{
					_identifier.setQuality(a.getValue(i));
				}
				else if (a.getQName(i).equals("authority"))
				{
					_identifier.setAuthority(new PdrId(a.getValue(i)));
				}
			}
		}

		// concurrences
		if (name.equals("concurrences") || name.equals("podl:concurrences") || qn.equals("concurrences")
				|| qn.equals("podl:concurrences"))
		{
			_concurrences = new Concurrences();
		}

		// concurrence
		if (name.equals("concurrence") || name.equals("podl:concurrence") || qn.equals("concurrence")
				|| qn.equals("podl:concurrence"))
		{
			_concurrence = new Concurrence();
			for (int i = 0; i < a.getLength(); i++)
			{
				if (a.getQName(i).equals("person"))
				{
					_concurrence.setPersonId(new PdrId(a.getValue(i)));
				}
			}
		}
		else if (name.equals("validationStm") || name.equals("podl:validationStm") || qn.equals("validationStm")
				|| qn.equals("podl:validationStm"))
		{
			_validationStm = new ValidationStm();
			for (int i = 0; i < a.getLength(); i++)
			{
				if (a.getQName(i).equals("authority"))
				{
					_validationStm.setAuthority(new PdrId(a.getValue(i)));
				}
			}
		}
		else if (name.equals("interpretation") || name.equals("podl:interpretation") || qn.equals("interpretation")
				|| qn.equals("podl:interpretation"))
		{
			_interpretation = true;
		}
		// reference
		if (name.equals("reference") || name.equals("podl:reference") || qn.equals("reference")
				|| qn.equals("podl:reference"))
		{

			_referenceB = true;
			_reference = new Reference();
			for (int i = 0; i < a.getLength(); i++)
			{

				if (a.getQName(i).equals("quality"))
				{
					_reference.setQuality(a.getValue(i));
				}
				else if (a.getQName(i).equals("authority"))
				{
					_reference.setAuthority(new PdrId(a.getValue(i)));
				}
				else if (a.getQName(i).equals("internal"))
				{
					_reference.setInternal(a.getValue(i));
				}
			}
		}

	}

	@Override
	public void startPrefixMapping(final String prefix, final String uri) throws SAXException
	{

	}

	public HashMap<PdrId, Person> getAllPersons()
	{
		return _allPersons;
	}
}
