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
package org.bbaw.pdr.ae.control.datahandling.solr.internal;

import java.net.URL;
import java.util.HashMap;
import java.util.Vector;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.bbaw.pdr.ae.common.AEConstants;
import org.bbaw.pdr.ae.common.CommonActivator;
import org.bbaw.pdr.ae.control.interfaces.AMainSearcher;
import org.bbaw.pdr.ae.control.saxHandler.PersonSaxHandler;
import org.bbaw.pdr.ae.metamodel.PdrId;
import org.bbaw.pdr.ae.model.Aspect;
import org.bbaw.pdr.ae.model.PdrObject;
import org.bbaw.pdr.ae.model.Person;
import org.bbaw.pdr.ae.model.ReferenceMods;
import org.bbaw.pdr.ae.model.ReferenceModsTemplate;
import org.bbaw.pdr.ae.model.search.PdrQuery;
import org.bbaw.pdr.ae.model.view.Facet;
import org.bbaw.pdr.allies.client.Repository;
import org.bbaw.pdr.allies.client.Utilities;
import org.bbaw.pdr.utils.IDRange;
import org.bbaw.pdr.utils.PDRType;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.xml.sax.InputSource;

public class MainSearcherSolr extends AMainSearcher {

	private int repositoryId = Platform.getPreferencesService().getInt(CommonActivator.PLUGIN_ID, "REPOSITORY_ID", AEConstants.REPOSITORY_ID, null);
	private int projectId = Platform.getPreferencesService().getInt(CommonActivator.PLUGIN_ID, "PROJECT_ID", AEConstants.PROJECT_ID, null);
	private final int MAX_OBJECT_NUMBER = 99999999;
	/** stores temporary resulting Persons after search. */
    private HashMap<PdrId, Person> tempPersons;
    
    /** stores temporary resulting Persons after search. */
    private HashMap<PdrId, Person> allPersons;
	@Override
	public Facet[] getComplexFacets(String type, String criteria1,
			String criteria2, String criteria3, String criteria4)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getFacets(String type, String criteria1, String tListName,
			String sListName, String rListName) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vector<String> getNewAspects() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vector<String> getNewPersons() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vector<String> getNewReferences() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getNumberOfAllPersons() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getObjectXML(String idString, String col) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Person getPersonById(PdrId id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ReferenceMods getReferenceFormate(String genre) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getValues(String type, String criteria1, String criteria2,
			String criteria3, String criteria4) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HashMap<PdrId, Person> searchAllPersons() throws Exception {
		URL url = new URL(Platform.getPreferencesService().getString(CommonActivator.PLUGIN_ID, "REPOSITORY_URL", AEConstants.REPOSITORY_URL, null));
		Utilities.setAxis2Base(url);
		Vector<IDRange> personRanges;
		int totalWork = 0;
		int totalPersons = 0;
		StringBuilder sb = new StringBuilder();
		personRanges = Utilities.getOccupiedObjectIDRanges(PDRType.PERSON, repositoryId, projectId, 1, MAX_OBJECT_NUMBER);
		
		if (personRanges != null && !personRanges.isEmpty())
		{
			for (IDRange range : personRanges)
			{
				totalPersons = totalPersons + range.getUpperBound() - range.getLowerBound();
			}
		}
		int lowerBound = 1;
		int upperBound = 1;
		sb.append("<result>");
		for (IDRange range : personRanges)
		{
//			System.out.println("range " + range.getLowerBound() + " upper b " + range.getUpperBound());
			lowerBound = range.getLowerBound();
			
			while(upperBound < range.getUpperBound())
			{
				if (range.getUpperBound() - lowerBound <= 249 ) upperBound = range.getUpperBound();
				else upperBound = lowerBound + 249;
//				monitor.subTask("Updating " + totalPersons + " Persons from Repository " + upperBound);
				Vector<String> objs = Utilities.getObjects(PDRType.PERSON, repositoryId, projectId, lowerBound, upperBound);
				for (String s : objs)
				{
					sb.append("<p>" + s + "</p>");
//					System.out.println(s);
//					name = extractPdrId(s) + ".xml";
//					dbCon.storeQuick2DB(s, col, name);
//					s = null;
//					monitor.worked(1);
				}
				lowerBound = Math.min(lowerBound + 250, range.getUpperBound());
			}
		}
		sb.append("</result>");
		SAXParserFactory factory = SAXParserFactory.newInstance();
		PersonSaxHandler saxHandler = new PersonSaxHandler();

		 try {
		     InputSource is = new InputSource(sb.toString());
		     SAXParser      saxParser = factory.newSAXParser();
		    saxParser.parse(is, saxHandler);

		 } catch (Throwable err) {
		     err.printStackTrace ();
		 }
    	allPersons = (HashMap<PdrId, Person>) saxHandler.getResultObject();
    	
    	String query = "(id:pdrAo." + repositoryId + "." + projectId + ".*)AND(semanticstm:NormName_* OR semanticstm:Name)";
//		"fl=id%20notification%20relation.object%20revision.authority",
//		"0", "999999";
    	String solrResult = Repository.solrSelect(query);
    	System.out.println(solrResult);
		return null;
	}

	@Override
	public HashMap<PdrId, ReferenceMods> searchAllReferences() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HashMap<String, ReferenceModsTemplate> searchAllReferenceTemplates()
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Aspect searchAspect(PdrId id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Aspect searchAspect(String id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HashMap<PdrId, Aspect> searchAspects(PdrQuery q) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Object searchFacetAspects(PdrQuery pdrQuery, IProgressMonitor monitor)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String searchObjectString(String col, String name) throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vector<Person> searchPersons(PdrQuery q) throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vector<ReferenceMods> searchReferences(PdrQuery q) throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PdrObject searchAspectsByReference(PdrObject pdrObject, IProgressMonitor monitor) throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PdrObject searchAspectsByRelatedObject(PdrObject o, IProgressMonitor monitor) throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}

	

}
