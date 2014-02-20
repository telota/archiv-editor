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
package org.bbaw.pdr.ae.indentifiers.searchservice.concord;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Vector;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.bbaw.pdr.ae.indentifiers.searchservice.concord.internal.ConcordSaxHandler;
import org.bbaw.pdr.ae.metamodel.IAEPresentable;
import org.bbaw.pdr.ae.view.identifiers.interfaces.IConcurrenceSearchService;
import org.bbaw.pdr.ae.view.identifiers.model.ConcurrenceData;
import org.bbaw.pdr.ae.view.identifiers.model.ConcurrenceQuery;

public class ConcordSearchService implements IConcurrenceSearchService
{
	private static final String url = "http://pdrdev.bbaw.de/concord/1-4/";

	public ConcordSearchService()
	{
	}

	@Override
	public String getLabel()
	{
		return "PDR Concord Service";
	}


	@Override
	public String getImageString()
	{
		return null;
	}

	@Override
	public int getPriority()
	{
		return 0;
	}

	@Override
	public String getValue()
	{
		return "PDR Concord Service";
	}

	@Override
	public void setValue(String value)
	{

	}

	@Override
	public int compareTo(IAEPresentable arg0)
	{
		return 0;
	}

	@Override
	public String getContent()
	{
		return "PDR Concord Service";
	}

	@Override
	public int getCursorPosition()
	{
		return 0;
	}

	@Override
	public String getDescription()
	{
		return "PDR Concord Service";
	}

	@Override
	public URL buildUrl(ConcurrenceQuery query)
	{
		String link = url + "?";
		URL url;
		boolean notFirst = false;
		if (query != null)
		{
			if (query.getNormName() != null && query.getNormName().trim().length() > 0)
			{
				try {
					link += "n=" + (URLEncoder.encode(query.getNormName().trim(), "UTF-8").trim());
					notFirst = true;
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (query.getOtherNames() != null && !query.getOtherNames().isEmpty())
			{
				boolean firstOtherName = true;
				for (String name : query.getOtherNames())
				{
					if (firstOtherName)
					{
						link += "&on=";
						firstOtherName = false;
					}
					else if (notFirst)
					{
						link += "%20";
					}
					link += name.trim();
					notFirst = true;
				}

			}
			if (query.getDateOfBirth() != null)
			{
				if (notFirst)
				{
					link += "&";
				}
				link += "db=" + query.getDateOfBirth().toString();
				notFirst = true;
			}
			if (query.getPlaceOfBirth() != null)
			{
				if (notFirst)
				{
					link += "&";
				}
				link += "pb=" + query.getPlaceOfBirth().toString();
				notFirst = true;
			}
			if (query.getDateOfDeath() != null)
			{
				if (notFirst)
				{
					link += "&";
				}
				link += "dd=" + query.getDateOfDeath().toString();
				notFirst = true;
			}
			if (query.getPlaceOfDeath() != null)
			{
				if (notFirst)
				{
					link += "&";
				}
				link += "pd=" + query.getPlaceOfDeath().toString();
				notFirst = true;
			}
			if (query.getDescription() != null)
			{
				if (notFirst)
				{
					link += "&";
				}
				link += "d=" + query.getDescription().trim();
				notFirst = true;
			}
			if (query.getDescription() != null)
			{
				if (notFirst)
				{
					link += "&";
				}
				link += "d=" + query.getDescription().trim();
				notFirst = true;
			}
			// gender
			if (query.getGender() != null)
			{
				if (notFirst)
				{
					link += "&";
				}
				link += "g=" + query.getGender().trim();
				notFirst = true;
			}
			// year of activity
			// country of activity
		}
		try
		{
			link.replace("\\s", "%20");
			url = new URL(link);
			return url;
		}
		catch (MalformedURLException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Vector<ConcurrenceData> parseSearchResult(String xml)
	{
		if (xml != null)
		{
			SAXParserFactory factory = SAXParserFactory.newInstance();
			try
			{
				InputStream xmlInput = new ByteArrayInputStream(xml.getBytes("UTF-8"));
				SAXParser saxParser = factory.newSAXParser();
				ConcordSaxHandler handler = new ConcordSaxHandler();
				saxParser.parse(xmlInput, handler);
				return handler.getConcurrenceDatas();

			}
			catch (Throwable err)
			{
				err.printStackTrace();
			}
		}
		return null;
	}

}
