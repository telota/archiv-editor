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

import java.util.HashMap;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bbaw.pdr.ae.control.facade.Facade;
import org.bbaw.pdr.ae.metamodel.PdrDate;
import org.bbaw.pdr.ae.metamodel.PdrId;
import org.bbaw.pdr.ae.model.BasicPersonData;
import org.bbaw.pdr.ae.model.ComplexName;
import org.bbaw.pdr.ae.model.Person;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/**
 * The Class NamePersIDSaxHandler.
 * @author Christoph Plutte
 */
public class NamePersIDSaxHandler implements ContentHandler
{

	/** The _facade. */
	private Facade _facade = Facade.getInstanz();

	/** The persons. */
	private HashMap<PdrId, Person> _persons;

	/** The person. */
	private Person _person = null;

	/** The bpd. */
	private BasicPersonData _bpd = null;

	/** The complex names. */
	private Vector<ComplexName> _complexNames = null;

	/** The display names. */
	private HashMap<String, ComplexName> _displayNames = null;

	/** The complex name. */
	private ComplexName _complexName = null;

	/** The descriptions. */
	private Vector<String> _descriptions = null;

	/** The id. */
	private PdrId _id;

	// /** The first. */
	// private boolean _first = true;

	/** The temp name. */
	private String _tempName = "";

	/** The sur name b. */
	private boolean _surNameB = false;

	/** The fore name b. */
	private boolean _foreNameB = false;

	/** The name link b. */
	private boolean _nameLinkB = false;

	/** The gen n. */
	private boolean _genN = false;

	/** The notif. */
	private boolean _notif = false;

	/** The semantic b. */
	private boolean _semanticB = false;

	/** The org name b. */
	private boolean _orgNameB = false;

	/** The name b. */
	private boolean _nameB = false;

	/** The _result object. */
	private Object _resultObject;

	/**
	 * Instantiates a new name pers id sax handler.
	 * @param persons the persons
	 */
	public NamePersIDSaxHandler(final HashMap<PdrId, Person> persons)
	{
		this._persons = persons;
	}

	@Override
	public final void characters(final char[] ch, final int start, final int len)
	{

		// notification surName
		if (_surNameB)
		{
			_complexName.setSurName(_complexName.getSurName() + " " + new String(ch, start, len).trim());
			_surNameB = false;
		}
		// notification foreName
		if (_foreNameB)
		{
			_complexName.setForeName(_complexName.getForeName() + " " + new String(ch, start, len).trim());
			_foreNameB = false;
		}
		// notification nameLink
		if (_nameLinkB)
		{
			_complexName.setNameLink(_complexName.getNameLink() + " " + new String(ch, start, len).trim());
			_nameLinkB = false;
		}
		if (_genN)
		{
			_complexName.setGenName(_complexName.getGenName() + " " + new String(ch, start, len).trim());
			_genN = false;
		}
		else if (_orgNameB)
		{
			_complexName.setOrgName(_complexName.getOrgName() + " " + new String(ch, start, len).trim());
			_orgNameB = false;
		}
		else if (_nameB)
		{
			String desc = new String(ch, start, len).trim();

			if (!_descriptions.contains(desc))
			{
				_descriptions.add(desc);
			}
			_nameB = false;
		}
		// notification no tagging
		else if (_notif)
		{
			_tempName = new String(ch, start, len).trim();
			Pattern p = Pattern.compile("\\n");
			Matcher m = p.matcher(_tempName);
			if (m.find() && _tempName.split("\\n").length > 0)
			{
				_tempName = _tempName.split("\\n")[1];
			}
			_notif = false;
		}

		else if (_semanticB)
		{
			String tag = new String(ch, start, len).trim();
			if (tag.equals("biographicalData"))
			{
			}
			else if (tag.equals("principalDescription"))
			{
				if (_bpd.getDescriptions().isEmpty())
				{
					_bpd.setDescriptions(_descriptions);
				}
				else
				{
					for (String s : _descriptions)
					{
						if (!_bpd.getDescriptions().contains(s))
						{
							_bpd.getDescriptions().add(s);
						}
					}
				}
			}
			else
			{
				if (_facade.isPersonNameTag(tag) && !_complexNames.contains(_complexName))
				{
					_complexNames.add(_complexName);
				}
				if (_facade.getKeyOfPersonNormNameTag(tag) != null)
				{
					// System.out.println("tag " + tag);
					// System.out.println("put with key " +
					// _facade.getKeyOfPersonNormNameTag(tag));
					// System.out.println("name der person  " +
					// complexName.toString());
					_displayNames.put(_facade.getKeyOfPersonNormNameTag(tag), _complexName);
				}

			}
			_semanticB = false;
		}

		// notification no tagging

	}

	@Override
	public void endDocument() throws SAXException
	{

	}

	@Override
	public final void endElement(final String u, final String name, final String qn)
	{
		if (name.equals("person") || name.equals("aodl:person"))
		{
			// if(!idB)
			// {
			// bpd.setComplexNames(complexNames);
			// bpd.setDisplayNames(displayNames);
			// person.setBasicPersonData(bpd);
			// }
			// idB = false;

		}

		else if (name.equals("notification") || name.equals("aodl:notification"))
		{
			if (_complexName.getSurName().length() == 0 && _complexName.getForeName().length() == 0)
			{
				_complexName.setSurName(_tempName);
			}
			_notif = false;
		}
		// orgName
		else if (name.equals("orgName") || name.equals("aodl:orgName"))
		{

			_orgNameB = false;

		}
		// name
		else if (name.equals("name") || name.equals("aodl:name"))
		{

			_nameB = false;

		}
		// date
		else if (name.equals("date") || name.equals("aodl:date"))
		{
		}
		// else if (name.equals("person") || name.equals("aodl:person"))
		// {
		// // if (b12)
		// // {
		// // if (new Integer(id.substring(18)) < 20)
		// System.out.println(" as first");
		// // complexNames.insertElementAt(complexName, 0);
		// // b12 = false;
		// // }
		// // else
		// // {
		// // complexNames.add(complexName);
		// //
		// // }
		// if (!b13)
		// {
		// bpd.setComplexNames(complexNames);
		// person.setBasicPersonData(bpd);
		//
		// }

		// }
		else if (name.equals("result"))
		{
			// for (int h = 0; h<persons.size(); h++)
			// {
			// System.out.println("name pers id sax handler personen id " +
			// persons.get(h).getPdrId().toString());
			//
			// }
			// Collections.sort(persons, new PersonByNameComparator());
			setResultObject(_persons);
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
	public final void startElement(final String u, final String name, final String qn, final Attributes a)
	{
		// id and new person
		if (name.equals("person") || name.equals("aodl:person"))
		{
			for (int i = 0; i < a.getLength(); i++)
			{
				if (a.getQName(i).equals("id"))
				{
					_id = new PdrId(new String(a.getValue(i)));

					if (!_persons.containsKey(_id))
					{
						_person = new Person(_id);
						_persons.put(_person.getPdrId(), _person);
						_person.setDirty(false);
						_person.setNew(false);
						_bpd = new BasicPersonData();
						_complexNames = new Vector<ComplexName>(3);
						_displayNames = new HashMap<String, ComplexName>();
						_descriptions = new Vector<String>(2);
						_bpd.setComplexNames(_complexNames);
						_bpd.setDisplayNames(_displayNames);
						_bpd.setDescriptions(_descriptions);
						_person.setBasicPersonData(_bpd);
					}
					else
					{
						_person = _persons.get(_id);
						if (_person.getBasicPersonData() == null)
						{
							_bpd = new BasicPersonData();
							_complexNames = new Vector<ComplexName>(3);
							_displayNames = new HashMap<String, ComplexName>();
							_descriptions = new Vector<String>(2);
							_bpd.setComplexNames(_complexNames);
							_bpd.setDisplayNames(_displayNames);
							_bpd.setDescriptions(_descriptions);
							_person.setBasicPersonData(_bpd);
						}
						_bpd = _person.getBasicPersonData();
						_complexNames = _bpd.getComplexNames();
						_displayNames = _bpd.getDisplayNames();
						_descriptions = _bpd.getDescriptions();
					}
				}
			}

		}
		// semanticStm
		if (name.equals("semanticStm") || name.equals("aodl:semanticStm"))
		{
			_semanticB = true;

		}

		// notification
		if (name.equals("notification") || name.equals("aodl:notification"))
		{
			_complexName = new ComplexName();
			_complexName.setForeName("");
			_complexName.setSurName("");
			_complexName.setNameLink("");
			_notif = true;
		}

		// persName
		if (name.equals("persName") || name.equals("aodl:persName"))
		{

			for (int i = 0; i < a.getLength(); i++)
			{
				if (a.getQName(i).equals("type"))
				{
					if (a.getValue(i).equalsIgnoreCase("surname"))
					{
						_surNameB = true;
					}
					else if (a.getValue(i).equalsIgnoreCase("forename"))
					{
						_foreNameB = true;
					}
					else if (a.getValue(i).equalsIgnoreCase("nameLink"))
					{
						_nameLinkB = true;
					}
					else if (a.getValue(i).equalsIgnoreCase("genName"))
					{
						_genN = true;
					}
				}
			}
		}
		// orgName
		else if (name.equals("orgName") || name.equals("aodl:orgName"))
		{

			_orgNameB = true;

		}
		// name
		else if (name.equals("name") || name.equals("aodl:name"))
		{
			_nameB = true;

		}
		// date
		else if (name.equals("date") || name.equals("aodl:date"))
		{
			boolean lifespan = false;
			boolean birth = false;
			boolean death = false;
			for (int i = 0; i < a.getLength(); i++)
			{
				if (a.getQName(i).equals("type"))
				{
					if (a.getValue(i).equalsIgnoreCase("lifespan"))
					{
						lifespan = true;
					}
					else if (a.getValue(i).equals("endOfLife"))
					{
						death = true;
					}
					else if (a.getValue(i).equals("beginningOfLife"))
					{
						birth = true;
					}
				}
				if (a.getQName(i).equals("subtype"))
				{
					if (a.getValue(i).equalsIgnoreCase("birth"))
					{
						birth = true;
					}
					else if (a.getValue(i).equalsIgnoreCase("baptism"))
					{
						birth = true;
					}
					else if (a.getValue(i).equalsIgnoreCase("death"))
					{
						death = true;
					}
				}
				else if (a.getQName(i).equals("when"))
				{

					if (birth)
					{
						birth = false;
						_bpd.setBeginningOfLife(new PdrDate(a.getValue(i)));
					}
					else if (death)
					{
						death = true;
						_bpd.setEndOfLife(new PdrDate(a.getValue(i)));
					}
				}
				else if (a.getQName(i).equals("from"))
				{

					if (birth || lifespan)
					{
						birth = false;
						_bpd.setBeginningOfLife(new PdrDate(a.getValue(i)));
					}
				}
				else if (a.getQName(i).equals("to"))
				{

					if (death || lifespan)
					{
						death = false;
						_bpd.setEndOfLife(new PdrDate(a.getValue(i)));
					}
				}
				else if (a.getQName(i).equals("notBefore"))
				{

					if (birth)
					{
						birth = false;
						_bpd.setBeginningOfLife(new PdrDate(a.getValue(i)));
					}
					else if (death)
					{
						death = false;
						_bpd.setEndOfLife(new PdrDate(a.getValue(i)));
					}
				}
				else if (a.getQName(i).equals("notAfter"))
				{
					if (birth)
					{
						birth = false;
						_bpd.setBeginningOfLife(new PdrDate(a.getValue(i)));
					}
					else if (death)
					{
						death = false;
						_bpd.setEndOfLife(new PdrDate(a.getValue(i)));
					}
				}

			}
		}
	}

	@Override
	public void startPrefixMapping(final String prefix, final String uri) throws SAXException
	{

	}
}
