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
package org.bbaw.pdr.ae.view.identifiers.internal;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.bbaw.pdr.ae.common.AEConstants;
import org.bbaw.pdr.ae.common.CommonActivator;
import org.bbaw.pdr.ae.control.core.PDRPersonDetailProvider;
import org.bbaw.pdr.ae.metamodel.PdrId;
import org.bbaw.pdr.ae.model.ComplexName;
import org.bbaw.pdr.ae.model.PdrObject;
import org.bbaw.pdr.ae.model.Person;
import org.bbaw.pdr.ae.view.identifiers.interfaces.IConcurrenceSearchService;
import org.bbaw.pdr.ae.view.identifiers.model.ConcurrenceData;
import org.bbaw.pdr.ae.view.identifiers.model.ConcurrenceDataHead;
import org.bbaw.pdr.ae.view.identifiers.model.ConcurrenceQuery;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;

public class ConcurrenceSearchController
{

	private String _provider = Platform
			.getPreferencesService()
			.getString(CommonActivator.PLUGIN_ID,
					"PRIMARY_SEMANTIC_PROVIDER", AEConstants.CLASSIFICATION_AUTHORITY, null).toUpperCase(); //$NON-NLS-1$
	/** local language. */
	private String _lang = AEConstants.getCurrentLocale().getLanguage();
	private PDRPersonDetailProvider _personDetailProvider = new PDRPersonDetailProvider();
	/** Logger. */
	private static ILog iLogger = AEConstants.ILOGGER;
	/** status. */
	private IStatus log;
	public void search(int searchTarget,
 Object personsContainer,
			IConcurrenceSearchService concurrenceSearchService, HashMap<PdrId, ConcurrenceDataHead> conMap,
			IProgressMonitor monitor)
	{
		// System.out.println("ConcurrenceSearchController searchConcurringPersons");

		if (personsContainer != null)
		{
			searchPersons(personsContainer, concurrenceSearchService, conMap, monitor);
		}
	}

	private void searchPersons(Object personsContainer, IConcurrenceSearchService concurrenceSearchService,
			HashMap<PdrId, ConcurrenceDataHead> conMap, IProgressMonitor monitor)
	{
		if (conMap == null)
		{
			conMap = new HashMap<PdrId, ConcurrenceDataHead>();
		}
		Vector<ConcurrenceData> conDatas;
		ConcurrenceDataHead conHead;
		if (personsContainer instanceof ArrayList)
		{
			@SuppressWarnings("unchecked")
			ArrayList<Person> selection = (ArrayList<Person>) personsContainer;
			monitor.beginTask("Searching for Identifiers", selection.size());

			for (Person p : selection)
			{
				conHead = conMap.get(p.getPdrId());
				if (conHead == null)
				{
					conHead = new ConcurrenceDataHead();
					conMap.put(p.getPdrId(), conHead);
				}
				conHead.addService(concurrenceSearchService.getLabel());
				try
				{
					conDatas = searchConcurrence(p, concurrenceSearchService);
					conHead.addConcurrenceDatas(conDatas);
				}
				catch (URISyntaxException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				catch (UnsupportedEncodingException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				monitor.worked(1);
				if (monitor.isCanceled())
				{
					break;
				}
			}
		}
		if (personsContainer instanceof PdrObject[])
		{
			PdrObject[] objects = (PdrObject[]) personsContainer;
			monitor.beginTask("Searching for Identifiers", objects.length);

			for (PdrObject o : objects)
			{
				if (o instanceof Person)
				{
					Person p = (Person) o;
					conHead = conMap.get(p.getPdrId());
					if (conHead == null)
					{
						conHead = new ConcurrenceDataHead();
						conMap.put(p.getPdrId(), conHead);
					}
					conHead.addService(concurrenceSearchService.getLabel());
					try
					{
						conDatas = searchConcurrence(p, concurrenceSearchService);
						conHead.addConcurrenceDatas(conDatas);
					}
					catch (URISyntaxException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					catch (UnsupportedEncodingException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					monitor.worked(1);
					if (monitor.isCanceled())
					{
						break;
					}
				}

			}
		}
		else if (personsContainer instanceof HashMap<?, ?>)
		{
			@SuppressWarnings("unchecked")
			HashMap<PdrId, Person> personsMap = (HashMap<PdrId, Person>) personsContainer;
			{
				monitor.beginTask("Searching for Identifiers", personsMap.size());

				for (PdrId id : personsMap.keySet())
				{
					Person p = personsMap.get(id);
					conHead = conMap.get(p.getPdrId());
					if (conHead == null)
					{
						conHead = new ConcurrenceDataHead();
						conMap.put(p.getPdrId(), conHead);
					}
					conHead.addService(concurrenceSearchService.getLabel());
					try
					{
						conDatas = searchConcurrence(p, concurrenceSearchService);
						conHead.addConcurrenceDatas(conDatas);
					}
					catch (URISyntaxException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					catch (UnsupportedEncodingException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					monitor.worked(1);
					if (monitor.isCanceled())
					{
						break;
					}
				}
			}
		}
	}

	private Vector<ConcurrenceData> searchConcurrence(Person p, IConcurrenceSearchService concurrenceSearchService)
			throws URISyntaxException, UnsupportedEncodingException
	{
		if (concurrenceSearchService != null)
		{
			ConcurrenceQuery q = processQuery(p);
			URL url = concurrenceSearchService.buildUrl(q);
			log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "URL: " + url.toString());
			iLogger.log(log);
			String result = requestWebService(url);
			Vector<ConcurrenceData> resultData = concurrenceSearchService.parseSearchResult(result);
			if (resultData != null)
			{
				Collections.sort(resultData);
			}
			return resultData;
		}
		return null;
	}

	private String requestWebService(URL url) throws URISyntaxException, UnsupportedEncodingException
	{
		String result = null;
		if (url != null)
		{
			HttpClient client = new HttpClient();

			// System.out.println("url " + url.toString());
			// PostMethod method = null;
			HttpClient httpclient = null;
			httpclient = new HttpClient();

			String urlString = new String(url.toString());
			if (urlString.contains(" "))
			{
				// System.out.println("containts ws");
				urlString.replace(" ", "%20");
			}
			Pattern p = Pattern.compile("\\s");
			Matcher m = p.matcher(urlString);
			// while (m.find())
			// {
			// // System.out.println("\\s");
			// }
			urlString = m.replaceAll("%20");
			// while (m.find())
			// {
			// System.out.println("2.\\s");
			// }
			// urlString = URLEncoder.encode(urlString, "UTF-8");
			// urlString.replace("\\s+", "%20");
			GetMethod get = new GetMethod(urlString);
			HostConfiguration hf = new HostConfiguration();
			hf.setHost(urlString, url.getPort());
			httpclient.setHostConfiguration(hf);
			// get = new PostMethod(theURL);
			// LogHelper.logMessage("Before sending SMS Message: "+message);
			int respCode;
			try
			{
				respCode = httpclient.executeMethod(get);
				log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Response code: " + respCode);
				iLogger.log(log);
				// successful.

				/* send request */
				final int status = client.executeMethod(get);
				// LOG.debug("http status #execute: " +
				// Integer.toString(status));
				switch (status)
				{
					case HttpStatus.SC_NOT_IMPLEMENTED:
						get.releaseConnection();
						// throw new IOException("Solr Query #GET (" +
						// get.getURI().toString() + ") returned 501");
					default:
						result = get.getResponseBodyAsString();
						get.releaseConnection();
				}
			}
			catch (HttpException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return result;
	}



	

	

	private ConcurrenceQuery processQuery(Person p)
	{
		// System.out.println("processQuery");
		ConcurrenceQuery q = new ConcurrenceQuery();

		if (p.getBasicPersonData() != null)
		{

		}
		if (p.getBasicPersonData().getDisplayNames() != null && !p.getBasicPersonData().getDisplayNames().isEmpty())
			{
			String normName = null;
			if (p.getBasicPersonData().getDisplayNames().get(_provider + "_" + _lang) != null)
			{
				normName = p.getBasicPersonData().getDisplayNames().get(_provider + "_" + _lang).toString();
			}
			else if (p.getBasicPersonData().getDisplayNames().get(_provider) != null)
			{
				normName = p.getBasicPersonData().getDisplayNames().get(_provider).toString();
			}

			else if (p.getBasicPersonData().getDisplayNames().get("pdr_" + _lang) != null)
			{
				normName = p.getBasicPersonData().getDisplayNames().get("pdr_" + _lang).toString();
			}
			else if (p.getBasicPersonData().getDisplayNames().get("pdr") != null)
			{
				normName = p.getBasicPersonData().getDisplayNames().get("pdr").toString();
			}
			else
			{
				for (String s : p.getBasicPersonData().getDisplayNames().keySet())
				{
					normName = p.getBasicPersonData().getDisplayNames().get(s).toString();
				}
			}
			if (normName != null)
			{
				q.setNormName(normName.trim());
			}
			
			if (p.getBasicPersonData().getBeginningOfLife() != null)
			{
				q.setDateOfBirth(p.getBasicPersonData().getBeginningOfLife());
			}
			String placeOfBirth = _personDetailProvider.getMarkupedText(p.getPdrId(), "biographicalData", "placeName",
					"settlement", "beginningOfLife", null);
			if (placeOfBirth != null)
			{
				try
				{
					q.setPlaceOfBirth(URLEncoder.encode(placeOfBirth.trim(), "UTF-8"));
				}
				catch (UnsupportedEncodingException e)
				{
					e.printStackTrace();
				}
			}
			if (p.getBasicPersonData().getEndOfLife() != null)
			{
				q.setDateOfDeath(p.getBasicPersonData().getEndOfLife());
			}
			String placeOfDeath = _personDetailProvider.getMarkupedText(p.getPdrId(), "biographicalData", "placeName",
					"settlement", "endOfLife", null);
			if (placeOfDeath != null)
			{
				try
				{
					q.setPlaceOfBirth(URLEncoder.encode(placeOfDeath.trim(), "UTF-8"));
				}
				catch (UnsupportedEncodingException e)
				{
					e.printStackTrace();
				}
			}
			if (p.getBasicPersonData().getComplexNames() != null)
			{
				for (ComplexName cn : p.getBasicPersonData().getComplexNames())
				{
					try
					{
						q.addOtherNames(URLEncoder.encode(cn.getSurName().trim(), "UTF-8"));
					}
					catch (UnsupportedEncodingException e)
					{
						e.printStackTrace();
					}
					try
					{
						q.addOtherNames(URLEncoder.encode(cn.getForeName().trim(), "UTF-8"));
					}
					catch (UnsupportedEncodingException e)
					{
						e.printStackTrace();
					}
				}
			}
			if (p.getBasicPersonData().getDescriptions() != null && !p.getBasicPersonData().getDescriptions().isEmpty())
			{
				try
				{
					q.setDescription(URLEncoder
.encode(p.getBasicPersonData().getDescriptions().firstElement().trim(),
							"UTF-8"));
				}
				catch (UnsupportedEncodingException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			String gender = _personDetailProvider.getMarkupedText(p.getPdrId(), "generalAffiliation", "gender", null,
					null, null);
			if (gender != null)
			{
				try
				{
					q.setGender(URLEncoder.encode(gender.trim(), "UTF-8"));
				}
				catch (UnsupportedEncodingException e)
				{
					e.printStackTrace();
				}
			}

		}
		return q;
	}

}
