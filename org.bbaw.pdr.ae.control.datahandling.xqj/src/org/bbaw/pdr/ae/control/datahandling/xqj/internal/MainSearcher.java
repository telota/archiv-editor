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
package org.bbaw.pdr.ae.control.datahandling.xqj.internal;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Vector;
import java.util.regex.Matcher;

import javax.xml.stream.XMLStreamException;
import javax.xml.xquery.XQConnection;
import javax.xml.xquery.XQException;
import javax.xml.xquery.XQPreparedExpression;
import javax.xml.xquery.XQResultSequence;

import org.bbaw.pdr.ae.common.AEConstants;
import org.bbaw.pdr.ae.common.CommonActivator;
import org.bbaw.pdr.ae.control.core.PDRObjectDisplayNameProcessor;
import org.bbaw.pdr.ae.control.facade.Facade;
import org.bbaw.pdr.ae.control.interfaces.AMainSearcher;
import org.bbaw.pdr.ae.control.saxHandler.AspectSaxHandler;
import org.bbaw.pdr.ae.control.saxHandler.ComplexFacetSaxHandler;
import org.bbaw.pdr.ae.control.saxHandler.ComplexIDSaxHandler;
import org.bbaw.pdr.ae.control.saxHandler.FacetSaxHandler;
import org.bbaw.pdr.ae.control.saxHandler.NamePersIDSaxHandler;
import org.bbaw.pdr.ae.control.saxHandler.PersonSaxHandler;
import org.bbaw.pdr.ae.control.saxHandler.ReferenceSaxHandler;
import org.bbaw.pdr.ae.db.basex711.DBConnector;
import org.bbaw.pdr.ae.metamodel.IAEPresentable;
import org.bbaw.pdr.ae.metamodel.PdrId;
import org.bbaw.pdr.ae.model.Aspect;
import org.bbaw.pdr.ae.model.PdrObject;
import org.bbaw.pdr.ae.model.Person;
import org.bbaw.pdr.ae.model.ReferenceMods;
import org.bbaw.pdr.ae.model.ReferenceModsTemplate;
import org.bbaw.pdr.ae.model.search.Criteria;
import org.bbaw.pdr.ae.model.search.PdrQuery;
import org.bbaw.pdr.ae.model.view.Facet;
import org.bbaw.pdr.ae.model.view.FacetResultNode;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;

/**
 * Class serves as major interface between view and control and thus model. It
 * handels all search request, splits them up into queries executed
 * simultaneously, resolves the intersection, gets data of all objects that meet
 * the criteria and transforms xml to pdrObjects.
 * @author cplutte
 */
public class MainSearcher extends AMainSearcher
{

	// public static MainSearcher getInstance(){
	// if (singletonMainSearcher == null) {
	// synchronized (MainSearcher.class){
	// if (singletonMainSearcher == null){
	// singletonMainSearcher = new MainSearcher();
	// }
	// }
	// }
	// return singletonMainSearcher;
	// }
	/** The db con. */
	private DBConnector _dbCon = DBConnector.getInstance();

	/** instance of PDRObjectDisplayNameProcessor. */
	private PDRObjectDisplayNameProcessor _pdrDisplayNameProc = new PDRObjectDisplayNameProcessor();

	/** stores temporary resulting Persons after search. */
	private HashMap<PdrId, Person> _allPersons;

	/** stores ids of complex searches. */
	private Vector<PdrId> _searchIds;

	/** The facets. */
	private ArrayList<String> _facets;

	/** The complex facets. */
	private Vector<Facet> _complexFacets;

	/** The ref template. */
	private ReferenceMods _refTemplate;

	/** The search aspects. */
	private HashMap<PdrId, Aspect> _searchAspects = new HashMap<PdrId, Aspect>();

	/** _facade singleton instance. */
	private Facade _facade = Facade.getInstanz();

	/** Logger. */
	private static ILog iLogger = AEConstants.ILOGGER;

	/** status. */
	private IStatus _log;

	/**
	 * constructor.
	 */
	public MainSearcher()
	{

	}

	@Override
	public final HashMap<PdrId, Person> getAllPersons()
	{
		return _allPersons;
	}

	@Override
	public final Facet[] getComplexFacets(final String type, final String criteria1, final String tListName,
			final String sListName, final String rListName) throws Exception
	{

		// Platform.getPreferencesService().getString(CommonActivator.PLUGIN_ID,
		// "PRIMARY_SEMANTIC_PROVIDER", "PDR", null);
		String searchedValue = "";
		String query = "";
		if (type.equals("tagging"))
		{
			query = "declare namespace aodl=\"http://pdr.bbaw.de/namespaces/aodl/\";\n" + "<result>{\n"
					+ "for $x in collection(\"aspect\")//aodl:aspect \n";
			if (tListName != null && tListName.trim().length() > 0)
			{
				query += "[.//aodl:" + criteria1 + "/@type='" + tListName.trim() + "'] ";
			}
			if (sListName != null && sListName.trim().length() > 0)
			{
				query += "[.//aodl:" + criteria1 + "/@subtype='" + sListName.trim() + "'] ";
			}
			if (rListName != null && rListName.trim().length() > 0)
			{
				query += "[.//aodl:" + criteria1 + "/@role='" + rListName.trim() + "'] ";
			}
			// if (criteria1.trim().equals("persName"))
			// {
			// query+= "[.//aodl:semanticStm/text()= 'NormName_IT'] ";
			// }

			query += "\n for $n in $x//aodl:" + criteria1;

			if (tListName != null && tListName.trim().length() > 0)
			{
				query += "[./@type='" + tListName.trim() + "']";
			}
			if (sListName != null && sListName.trim().length() > 0)
			{
				query += "[./@subtype='" + sListName.trim() + "'] ";
			}
			if (rListName != null && rListName.trim().length() > 0)
			{
				query += "[./@role='" + rListName.trim() + "'] ";
			}

			query += "\nreturn <complexFacet> {$x//aodl:semanticStm}\n" + "<facet>{$n/text()}</facet> \n"
					+ "<key>{string($n/@key)}</key> \n " + "</complexFacet>\n" + "}</result>";
		}
		else if (type.equals("semantic"))
		{

			query = "declare namespace aodl=\"http://pdr.bbaw.de/namespaces/aodl/\";\n" + "<result>{\n"
					+ "for $x in collection(\"aspect\")//aodl:aspect";
			if (criteria1 != null)
			{
				query += "[.//aodl:semanticStm/@provider='" + criteria1.toLowerCase() + "' or "
						+ ".//aodl:semanticStm/@provider='" + criteria1.toUpperCase() + "']";
			}
			query += "\nreturn <a>{\n" + "for $n in $x//aodl:semanticStm";
			if (criteria1 != null)
			{
				query += "[.//aodl:semanticStm/@provider='" + criteria1.toLowerCase() + "' or "
						+ ".//aodl:semanticStm/@provider='" + criteria1.toUpperCase() + "']/text()";
			}
			query += "\nreturn  <facet>{$n}</facet> \n" + "} </a>\n" + "}</result>";
		}
		else if (type.equals("tagging_values"))
		{
			query = "declare namespace aodl=\"http://pdr.bbaw.de/namespaces/aodl/\";\n" + "<result>{\n"
					+ "for $x in collection(\"aspect\")//aodl:aspect ";
			if (criteria1.trim().length() > 0)
			{
				query += "[.//aodl:" + criteria1 + "]";
				searchedValue = "type";
			}
			// if (sListName.trim().length() > 0)
			// {
			// query+= "[.//aodl:" + criteria1 + "/@subtype='" +
			// sListName.trim() + "'] ";
			// }
			// if (rListName.trim().length() > 0)
			// {
			// query+= "[.//aodl:" + criteria1 + "/@role='" + rListName.trim() +
			// "'] ";
			// }

			query += "\n return <a> {	for $v in $x//aodl:" + criteria1;

			if (tListName != null && tListName.trim().length() > 0)
			{
				query += "[./@type='" + tListName.trim() + "']";
				searchedValue = "subtype";

			}
			if (sListName != null && sListName.trim().length() > 0)
			{
				query += "[./@subtype='" + sListName.trim() + "'] ";
				searchedValue = "role";

			}
			if (rListName != null && rListName.trim().length() > 0)
			{
				query += "[./@role='" + rListName.trim() + "'] ";
				searchedValue = "key";

			}

			query += "\nreturn <facet>{string($v//@" + searchedValue + ")}</facet> } </a> \n" + "}</result>";
		}
		// else if (type.equals("semantic") && criteria1 != null)
		// {
		// query =
		// "declare namespace aodl=\"http://pdr.bbaw.de/namespaces/aodl/\";\n" +
		// "<result>{\n" +
		// "for $x in collection(\"aspect\")//aodl:aspect[.//aodl:semanticStm/@provider='"
		// + criteria1.toLowerCase() + "' or " +
		// ".//aodl:semanticStm/@provider='" + criteria1.toUpperCase() + "']\n"+
		// "for $n in $x//aodl:semanticStm[.//aodl:semanticStm/@provider='" +
		// criteria1.toLowerCase() + "' or " +
		// ".//aodl:semanticStm/@provider='" + criteria1.toUpperCase() +
		// "']/text()\n"+
		// "order by $n \n" +
		// "return  <facet>{$n}</facet> \n"+
		// "}</result>";
		// }
		else if (type.equals("relation"))
		{
			searchedValue = "class";

			query = "declare namespace aodl=\"http://pdr.bbaw.de/namespaces/aodl/\";\n" + "<result>{\n"
					+ "for $x in collection(\"aspect\")//aodl:aspect ";
			if (criteria1 != null && criteria1.trim().length() > 0)
			{
				query += "[.//aodl:semanticStm/@provider='" + criteria1.toLowerCase() + "' or "
						+ ".//aodl:semanticStm/@provider='" + criteria1.toUpperCase() + "']";
			}
			// if (sListName.trim().length() > 0)
			// {
			// query+= "[.//aodl:" + criteria1 + "/@subtype='" +
			// sListName.trim() + "'] ";
			// }
			// if (rListName.trim().length() > 0)
			// {
			// query+= "[.//aodl:" + criteria1 + "/@role='" + rListName.trim() +
			// "'] ";
			// }

			query += "\n return <a> {	for $v in $x//aodl:relation";

			if (tListName != null && tListName.trim().length() > 0 && !tListName.equalsIgnoreCase("ALL"))
			{
				query += "[./@class='" + tListName.trim() + "']";
				searchedValue = "context";

			}
			if (sListName != null && sListName.trim().length() > 0 && !sListName.equalsIgnoreCase("ALL"))
			{
				query += "[./@context='" + sListName.trim() + "'] ";

			}

			query += "\nreturn <facet>{string($v//@" + searchedValue + ")}</facet> } </a> \n" + "}</result>";
		}
		else if (type.equals("refTemplate"))
		{
			query = "<result>{\n" + "for $x in collection(\"refTemplate\")//mods/genre \n";

			query += "\nreturn <facet>{$x/text()}</facet>" + "}</result>";
		}

		else if (type.equals("validation"))
		{
			query = "declare namespace aodl=\"http://pdr.bbaw.de/namespaces/aodl/\";\n" + "<result>{\n"
					+ "for $x in collection(\"aspect\")//aodl:aspect ";

			//
			query += "\n return <a> {	for $v in $x//aodl:reference";

			query += "\nreturn <facet>{string($v//@internal)}</facet> } </a> \n" + "}</result>";
		}

		_log = new Status(IStatus.INFO, CommonActivator.PLUGIN_ID, "MainSearcher getComplexFacets query: " + query);
		iLogger.log(_log);

		synchronized (_dbCon)
		{
			// get Connection
			XQConnection con = _dbCon.getConnection();
			XQPreparedExpression xqp;
			xqp = con.prepareExpression(query);

			XQResultSequence xqs = xqp.executeQuery();
			// execute the XQuery Expression

			ComplexFacetSaxHandler saxHandler = new ComplexFacetSaxHandler();
			try
			{
				xqs.writeSequenceToSAX(saxHandler);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			_complexFacets = (Vector<Facet>) saxHandler.getResultObject();

			con.close();
			_dbCon.closeDB("aspect");
		}

		if (_complexFacets == null)
			return null;
		Collections.sort(_complexFacets);

		Facet help[] = _complexFacets.toArray(new Facet[_complexFacets.size()]);

		_complexFacets = null;
		// if (type.equals("semantic"))
		// {
		// _facade.setAllSemantics(help);
		// }
		// for (String s : help)
		// {
		// System.out.println("Was für facetten hat er denn gefunden " + s);
		// }

		return help;
	}

	@Override
	public final String[] getFacets(final String type, final String criteria1, final String tListName,
			final String sListName, final String rListName) throws Exception
	{

		Platform.getPreferencesService().getString(CommonActivator.PLUGIN_ID, "PRIMARY_SEMANTIC_PROVIDER", "PDR", null);
		String searchedValue = "";
		String query = "";
		if (type.equals("tagging"))
		{
			query = "declare namespace aodl=\"http://pdr.bbaw.de/namespaces/aodl/\";\n" + "<result>{\n"
					+ "for $x in collection(\"aspect\")//aodl:aspect \n";
			if (tListName != null && tListName.trim().length() > 0 && !tListName.equals("ALL"))
			{
				query += "[.//aodl:" + criteria1 + "/@type='" + tListName.trim() + "'] ";
			}
			if (sListName != null && sListName.trim().length() > 0 && !sListName.equals("ALL"))
			{
				query += "[.//aodl:" + criteria1 + "/@subtype='" + sListName.trim() + "'] ";
			}
			if (rListName != null && rListName.trim().length() > 0 && !rListName.equals("ALL"))
			{
				query += "[.//aodl:" + criteria1 + "/@role='" + rListName.trim() + "'] ";
			}

			query += "\n for $n in $x//aodl:" + criteria1;

			if (tListName != null && tListName.trim().length() > 0 && !tListName.equals("ALL"))
			{
				query += "[./@type='" + tListName.trim() + "']";
			}
			if (sListName != null && sListName.trim().length() > 0 && !sListName.equals("ALL"))
			{
				query += "[./@subtype='" + sListName.trim() + "'] ";
			}
			if (rListName != null && rListName.trim().length() > 0 && !rListName.equals("ALL"))
			{
				query += "[./@role='" + rListName.trim() + "'] ";
			}

			query += "/text()\nreturn <facet>{$n}</facet>" + "}</result>";
		}
		else if (type.equals("semantic"))
		{

			query = "declare namespace aodl=\"http://pdr.bbaw.de/namespaces/aodl/\";\n" + "<result>{\n"
					+ "for $x in collection(\"aspect\")//aodl:aspect/aodl:semanticDim/aodl:semanticStm";
			if (criteria1 != null)
			{
				query += "[./@provider='" + criteria1.toLowerCase() + "' or " + "./@provider='"
						+ criteria1.toUpperCase() + "']";
			}

			query += "\nreturn  <facet>{$x/text()}</facet> \n" + "}</result>";
		}
		else if (type.equals("tagging_values"))
		{
			query = "declare namespace aodl=\"http://pdr.bbaw.de/namespaces/aodl/\";\n" + "<result>{\n"
					+ "for $x in collection(\"aspect\")//aodl:aspect ";
			if (criteria1.trim().length() > 0)
			{
				query += "[.//aodl:" + criteria1 + "]";
				searchedValue = "type";
			}
			// if (sListName.trim().length() > 0)
			// {
			// query+= "[.//aodl:" + criteria1 + "/@subtype='" +
			// sListName.trim() + "'] ";
			// }
			// if (rListName.trim().length() > 0)
			// {
			// query+= "[.//aodl:" + criteria1 + "/@role='" + rListName.trim() +
			// "'] ";
			// }

			query += "\n return <a> {	for $v in $x//aodl:" + criteria1;

			if (tListName != null && tListName.trim().length() > 0 && !tListName.equals("ALL"))
			{
				query += "[./@type='" + tListName.trim() + "']";
				searchedValue = "subtype";

			}
			if (sListName != null && sListName.trim().length() > 0 && !sListName.equals("ALL"))
			{
				query += "[./@subtype='" + sListName.trim() + "'] ";
				searchedValue = "role";

			}
			if (rListName != null && rListName.trim().length() > 0 && !rListName.equals("ALL"))
			{
				query += "[./@role='" + rListName.trim() + "'] ";
				searchedValue = "key";

			}

			query += "\nreturn <facet>{string($v//@" + searchedValue + ")}</facet> } </a> \n" + "}</result>";
		}
		// else if (type.equals("semantic") && criteria1 != null)
		// {
		// query =
		// "declare namespace aodl=\"http://pdr.bbaw.de/namespaces/aodl/\";\n" +
		// "<result>{\n" +
		// "for $x in collection(\"aspect\")//aodl:aspect[.//aodl:semanticStm/@provider='"
		// + criteria1.toLowerCase() + "' or " +
		// ".//aodl:semanticStm/@provider='" + criteria1.toUpperCase() + "']\n"+
		// "for $n in $x//aodl:semanticStm[.//aodl:semanticStm/@provider='" +
		// criteria1.toLowerCase() + "' or " +
		// ".//aodl:semanticStm/@provider='" + criteria1.toUpperCase() +
		// "']/text()\n"+
		// "order by $n \n" +
		// "return  <facet>{$n}</facet> \n"+
		// "}</result>";
		// }
		else if (type.equals("relation"))
		{
			searchedValue = "context";

			query = "declare namespace aodl=\"http://pdr.bbaw.de/namespaces/aodl/\";\n" + "<result>{\n"
					+ "for $x in collection(\"aspect\")//aodl:aspect ";
			// if (criteria1 != null && criteria1.trim().length() > 0)
			// {
			// query+= "[.//aodl:semanticStm/@provider='" +
			// criteria1.toLowerCase() + "' or " +
			// ".//aodl:semanticStm/@provider='" + criteria1.toUpperCase() +
			// "']";
			// }
			// if (sListName.trim().length() > 0)
			// {
			// query+= "[.//aodl:" + criteria1 + "/@subtype='" +
			// sListName.trim() + "'] ";
			// }
			// if (rListName.trim().length() > 0)
			// {
			// query+= "[.//aodl:" + criteria1 + "/@role='" + rListName.trim() +
			// "'] ";
			// }

			query += "\n return <a> {	for $v in $x//aodl:relation";

			if (tListName != null && tListName.trim().length() > 0 && !tListName.equalsIgnoreCase("ALL"))
			{
				query += "[./@context='" + tListName.trim() + "']";
				searchedValue = "class";

			}
			if (sListName != null && sListName.trim().length() > 0 && !sListName.equalsIgnoreCase("ALL"))
			{
				query += "[./@class='" + sListName.trim() + "'] ";

			}

			query += "\nreturn <facet>{string($v//@" + searchedValue + ")}</facet> } </a> \n" + "}</result>";
		}
		else if (type.equals("refTemplate"))
		{
			query = "declare namespace mods=\"http://www.loc.gov/mods/v3\";\n" + "<result>{\n"
					+ "for $x in collection(\"refTemplate\")//mods/genre \n";

			query += "\nreturn <facet>{$x/text()}</facet>" + "}</result>";
		}

		else if (type.equals("validation"))
		{
			query = "declare namespace aodl=\"http://pdr.bbaw.de/namespaces/aodl/\";\n" + "<result>{\n"
					+ "for $x in collection(\"aspect\")//aodl:aspect ";

			//
			query += "\n return <a> {	for $v in $x//aodl:reference";

			query += "\nreturn <facet>{string($v//@internal)}</facet> } </a> \n" + "}</result>";
		}
		else if (type.equals("reference"))
		{
			query = "declare namespace mods=\"http://www.loc.gov/mods/v3\";\n" + "<result>{\n"
					+ "for $x in collection(\"reference\")//mods:mods";
			if (tListName != null && tListName.trim().length() > 0 && tListName.equals("type"))
			{
				query += "\n for $n in $x//mods:" + criteria1;
				query += "\nreturn <facet>{string($n//@type)}</facet>" + "}</result>";
			}
			else if (tListName != null && tListName.trim().length() > 0 && criteria1.equals("relatedItem"))
			{
				query += "\n for $n in $x/mods:relatedItem//" + tListName;
				query += "\nreturn <facet>{$n/text()}</facet>" + "}</result>";
			}
			else if (criteria1 != null && criteria1.trim().length() > 0 && criteria1.equals("genre"))
			{
				if (criteria1 != null && criteria1.trim().length() > 0)
				{
					query += "\n for $n in $x//mods:" + criteria1;
				}
				query += "\nreturn <facet>{$n/text()}</facet>" + "}</result>";
			}
			else
			{
				if (tListName != null && tListName.trim().length() > 0)

				{
					query += "\n for $n in $x//mods:" + criteria1 + "[./@type='" + tListName + "']";
				}
				else if (criteria1 != null && criteria1.trim().length() > 0)
				{
					query += "\n for $n in $x/*/mods:" + criteria1;
				}
				query += "\nreturn <facet>{$n/text()}</facet>" + "}</result>";
			}
		}
		_log = new Status(IStatus.INFO, CommonActivator.PLUGIN_ID, "MainSearcher getFacets query: " + query);
		iLogger.log(_log);

		synchronized (_dbCon)
		{
			// get Connection
			XQConnection con = _dbCon.getConnection();
			XQPreparedExpression xqp;
			xqp = con.prepareExpression(query);

			// System.out.println(xqp);
			XQResultSequence xqs = xqp.executeQuery();
			// execute the XQuery Expression
			FacetSaxHandler saxHanlder = new FacetSaxHandler();
			try
			{
				xqs.writeSequenceToSAX(saxHanlder);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			_facets = (ArrayList<String>) saxHanlder.getResultObject();
			con.close();
			_dbCon.closeDB("aspect");
		}

		Collections.sort(_facets);
		String[] help = _facets.toArray(new String[_facets.size()]);
		_facets = null;
		// if (type.equals("semantic"))
		// {
		// _facade.setAllSemantics(help);
		// }

		return help;
	}


	@Override
	public final Vector<String> getNewAspects() throws Exception
	{
		String query = "declare namespace aodl=\"http://pdr.bbaw.de/namespaces/aodl/\";\n"
				+ "for $x in collection(\"aspect\")//aodl:aspect\n"
				+ "where (number(substring($x/@id, 15)) >= 100000000)\n" + "order by $x/@id\n" + "\nreturn $x";

		_log = new Status(IStatus.INFO, CommonActivator.PLUGIN_ID, "MainSearcher searchAspect query: " + query);
		iLogger.log(_log);
		Vector<String> aspects = new Vector<String>();

		synchronized (_dbCon)
		{
			XQConnection con = _dbCon.getConnection();
			XQPreparedExpression xqp;
			xqp = con.prepareExpression(query);

			XQResultSequence xqs = xqp.executeQuery();
			// execute the XQuery Expression

			while (xqs.next())
			{
				aspects.add(xqs.getItemAsString(null));
			}
			con.close();
			_dbCon.closeDB("aspect");
		}

		return aspects;
	}

	@Override
	public final Vector<String> getNewPersons() throws Exception
	{
		String query = "declare namespace podl=\"http://pdr.bbaw.de/namespaces/podl/\";\n"
				+ "for $x in collection(\"person\")//podl:person\n"
				+ "where (number(substring($x/@id, 15)) >= 100000000)\n" + "order by $x/@id\n" + "\nreturn $x";

		_log = new Status(IStatus.INFO, CommonActivator.PLUGIN_ID, "MainSearcher persons query: " + query);
		iLogger.log(_log);
		Vector<String> persons = new Vector<String>();

		synchronized (_dbCon)
		{
			XQConnection con = _dbCon.getConnection();
			XQPreparedExpression xqp;
			xqp = con.prepareExpression(query);

			XQResultSequence xqs = xqp.executeQuery();
			// execute the XQuery Expression

			while (xqs.next())
			{
				persons.add(xqs.getItemAsString(null));
			}
			con.close();
			_dbCon.closeDB("person");
		}

		return persons;
	}

	// private Vector<PdrId> getDifference(Vector<PdrId> resultIds,
	// Vector<PdrId> searchIds2)
	// {
	// Vector<String> temp = new Vector<String>(resultIds.size());
	// int count = 0;
	// int j;
	// for (int i = 0; i < searchIds2.size(); i++)
	// {
	//
	// // System.out.println("searchIds2 " + searchIds2.get(i));
	// for (j = count; j < resultIds.size(); j++)
	// {
	// // System.out.println("getDifference: resultIds(" +j+") " +
	// resultIds.get(j));
	//
	// if (resultIds.get(j).compareTo(searchIds2.get(i)) < 0)
	// {
	// temp.add(resultIds.get(j));
	// // System.out.println("getDifference: person added " + resultIds.get(j));
	// }
	// else if (resultIds.get(j).compareTo(searchIds2.get(i)) >= 0)
	// {
	// j++;
	// break;
	// }
	//
	// }
	// count = j;
	// }
	//
	// return temp;
	// }

	@Override
	public final Vector<String> getNewReferences() throws Exception
	{
		String query = "declare namespace mods=\"http://www.loc.gov/mods/v3\";\n"
				+ "for $x in collection(\"reference\")//mods:mods\n"
				+ "where (number(substring($x/@ID, 15)) >= 100000000)\n" + "order by $x/@ID\n" + "\nreturn $x";

		_log = new Status(IStatus.INFO, CommonActivator.PLUGIN_ID, "MainSearcher references query: " + query);
		iLogger.log(_log);
		Vector<String> references = new Vector<String>();

		synchronized (_dbCon)
		{
			XQConnection con = _dbCon.getConnection();
			XQPreparedExpression xqp;
			xqp = con.prepareExpression(query);

			XQResultSequence xqs = xqp.executeQuery();
			// execute the XQuery Expression

			while (xqs.next())
			{
				references.add(xqs.getItemAsString(null));
			}
			// System.out.println("persons size " + references.size());
			for (String s : references)
			{
				// System.out.println("new references " + s);
			}
			con.close();
			_dbCon.closeDB("reference");
		}

		return references;
	}

	@Override
	public final int getNumberOfAllPersons() throws Exception
	{
		String query = "declare namespace podl=\"http://pdr.bbaw.de/namespaces/podl/\";\n"
				+ "let $x := collection(\"person\")//podl:person\n" + "return count($x)";

		XQConnection con = _dbCon.getConnection();
		XQPreparedExpression xqp;
		xqp = con.prepareExpression(query);

		XQResultSequence xqs = xqp.executeQuery();
		// execute the XQuery Expression
		while (xqs.next())
		{
			try
			{
				Integer size = new Integer(xqs.getItemAsString(null));
				return size;
			}
			catch (NumberFormatException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (XQException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		}

		return -1;
	}

	@Override
	public final String getObjectXML(final String idString, final String col) throws Exception
	{

		String query = null;
		String xml = null;
		if (col.equals("person"))
		{
			query = "declare namespace podl=\"http://pdr.bbaw.de/namespaces/podl/\";\n"
					+ "for $x in collection(\"person\")//podl:person[./@id='" + idString + "']\n" + "return $x";
		}
		else if (col.equals("aspect"))
		{
			query = "declare namespace aodl=\"http://pdr.bbaw.de/namespaces/aodl/\";\n"
					+ "for $x in collection(\"aspect\")//aodl:aspect[./@id='" + idString + "']\n" + "return $x";
		}
		else if (col.equals("reference"))
		{
			query = "declare namespace mods=\"http://www.loc.gov/mods/v3\";\n"
					+ "for $x in collection(\"reference\")//mods:mods[./@ID='" + idString + "']\n" + "return $x";
		}
		else if (col.equals("users"))
		{
			query = "declare namespace uodl=\"http://pdr.bbaw.de/namespaces/uodl/\";\n"
					+ "for $x in collection(\"users\")//user[./@id='" + idString + "']\n" + "return $x";
		}

		_log = new Status(IStatus.INFO, CommonActivator.PLUGIN_ID, "MainSearcher getObjectXML " + query);
		iLogger.log(_log);

		// // execute the XQuery Expression
		// OutputStream output = new OutputStream()
		// {
		// private StringBuilder string = new StringBuilder();
		//
		// public void write(int b) throws IOException {
		// this.string.append((char) b );
		// }
		//
		// public String toString(){
		// return this.string.toString();
		// }
		// };
		// Properties props = new Properties();
		// props.setProperty("method", "xml");
		// props.setProperty("indent", "yes");
		// props.setProperty("omit-xml-declaration", "yes");
		// props.setProperty("{http://saxon.sf.net/}indent-spaces", "1");
		synchronized (_dbCon)
		{
			// get Connection
			XQConnection con = _dbCon.getConnection();
			XQPreparedExpression xqp;
			xqp = con.prepareExpression(query);

			XQResultSequence xqs = xqp.executeQuery();
			xml = xqs.getSequenceAsString(null);
			_log = new Status(IStatus.WARNING, CommonActivator.PLUGIN_ID, "MainSearcher getObjectXML output " + xml);
			iLogger.log(_log);
			con.close();
			_dbCon.closeDB("aspect");
			_dbCon.closeDB("person");
			_dbCon.closeDB("reference");
		}

		return xml;
	}

	@Override
	public final Person getPersonById(final PdrId id) throws Exception
	{
		Person person = null;
		HashMap<PdrId, Person> pers;
		String query = "declare namespace podl=\"http://pdr.bbaw.de/namespaces/podl/\";\n" + "<result>{\n"
				+ "for $p in collection(\"person\")//podl:person[./@id='" + id.toString() + "']\n"
				+ "return <p>{$p}</p>\n" + "}</result>\n";

		_log = new Status(IStatus.INFO, CommonActivator.PLUGIN_ID, "MainSearcher getPersonById: " + query);
		iLogger.log(_log);
		XQConnection con;
		XQPreparedExpression xqp;
		XQResultSequence xqs;
		synchronized (_dbCon)
		{
			con = _dbCon.getConnection();
			xqp = con.prepareExpression(query);

			// execute the XQuery Expression
			xqs = xqp.executeQuery();
			PersonSaxHandler saxHandler = new PersonSaxHandler();
			try
			{
				xqs.writeSequenceToSAX(saxHandler);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

			con.close();
			_dbCon.closeDB("person");
			pers = (HashMap<PdrId, Person>) saxHandler.getAllPersons();
			for (PdrId i : pers.keySet())
			{
				person = pers.get(i);
				break;
			}
		}
		query = "declare namespace aodl=\"http://pdr.bbaw.de/namespaces/aodl/\";\n" + "<result>{\n"
				+ "for $x in collection(\"aspect\")//aodl:aspect";

		if (_facade.getPersonNameTags(null).size() > 0)
		{
			query += "[";
			boolean addOr = false;
			for (String tag : _facade.getPersonNameTags(null))
			{
				if (addOr)
				{
					query += " or ";
				}
				query += ".//aodl:semanticStm='" + tag + "'";
				addOr = true;
			}

			if (addOr)
			{
				query += " or ";
			}
			query += ".//aodl:semanticStm='biographicalData'";
			addOr = true;

			if (addOr)
			{
				query += " or ";
			}
			query += ".//aodl:semanticStm='principalDescription'";
			addOr = true;

			query += "]\n";

			query += "[.//@object=\"" + id.toString() + "\"]";
		}
		// "[.//aodl:semanticStm='Name' or .//aodl:semanticStm='Normname' or .//aodl:semanticStm='NormName_DE'"
		// +
		// "or .//aodl:semanticStm='NormName_IT' or .//aodl:semanticStm='NormName_EN' or .//aodl:semanticStm='Nomi'"
		// +
		// "or .//aodl:semanticStm='nome' or .//aodl:semanticStm='nome di norma']\n"+

		query += "return  <person id='{$x//aodl:relation/@object}'>"
				+ "{$x//aodl:notification}{$x//aodl:semanticStm}</person>\n" + "}</result>";

		_log = new Status(IStatus.INFO, CommonActivator.PLUGIN_ID,
				"MainSearcher getPersonById query for person names in aspects: " + query);
		iLogger.log(_log);

		synchronized (_dbCon)
		{
			con = _dbCon.getConnection();

			xqp = con.prepareExpression(query);

			xqs = xqp.executeQuery();

			NamePersIDSaxHandler idSaxHandler = new NamePersIDSaxHandler(pers);
			try
			{
				xqs.writeSequenceToSAX(idSaxHandler);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			_dbCon.closeDB("aspect");
			con.close();
			// tempPersons = (HashMap<PdrId, Person>)
			// idSaxHandler.getResultObject();
		}

		if (person != null && pers.containsKey(person.getPdrId()))
		{
			person.setBasicPersonData((pers.get(person.getPdrId()).getBasicPersonData()));
			_pdrDisplayNameProc.processDisplayName(person);
			return person;

		}
		return null;
	}



	@Override
	public final ReferenceMods getReferenceFormate(final String genre) throws Exception
	{

		String query = "declare namespace mods=\"http://www.loc.gov/mods/v3\";\n" + "<template>{\n"
				+ "for $x in collection(\"refTemplate\")//mods[.//genre='" + genre + "']\n" + "return <r>{$x}</r>"
				+ "}</template>";

		_log = new Status(IStatus.INFO, CommonActivator.PLUGIN_ID, "MainSearcher getReferenceFormate query: " + query);
		iLogger.log(_log);

		synchronized (_dbCon)
		{
			// if (!dbCon.dbExists("refTemplate"))
			// {
			// try {
			// dbCon.createEmpty("refTemplate");
			// } catch (BaseXException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			// }
			XQConnection con = _dbCon.getConnection();

			XQPreparedExpression xqp;
			xqp = con.prepareExpression(query);

			XQResultSequence xqs = xqp.executeQuery();
			// execute the XQuery Expression

			ReferenceSaxHandler saxHandler = new ReferenceSaxHandler();
			try
			{
				xqs.writeSequenceToSAX(saxHandler);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

			con.close();
			_dbCon.closeDB("refTemplate");
			_refTemplate = (ReferenceMods) saxHandler.getResultObject();
		}

		if (_refTemplate == null)
		{
		}
		// System.out.println("return template");
		return _refTemplate;
	}

	@Override
	public final Vector<PdrId> getSearchIds()
	{
		return _searchIds;
	}



	@Override
	public String[] getValues(String type, String criteria1, String tListName, String sListName, String rListName)
			throws Exception
	{
		String searchedValue = type;

		String query = "";
		if (type.equals("tagging_values"))
		{
			query = "declare namespace aodl=\"http://pdr.bbaw.de/namespaces/aodl/\";\n" + "<result>{\n"
					+ "for $x in collection(\"aspect\")//aodl:aspect \n";
			if (criteria1.trim().length() > 0)
			{
				query += "[.//aodl:" + criteria1 + "]";
				searchedValue = "type";
			}
			// if (sListName.trim().length() > 0)
			// {
			// query+= "[.//aodl:" + criteria1 + "/@subtype='" +
			// sListName.trim() + "'] ";
			// }
			// if (rListName.trim().length() > 0)
			// {
			// query+= "[.//aodl:" + criteria1 + "/@role='" + rListName.trim() +
			// "'] ";
			// }

			query += "\n return <a> {	for $v in $x///aodl:" + criteria1;

			if (tListName.trim().length() > 0)
			{
				query += "[./@type='" + tListName.trim() + "']";
				searchedValue = "subtype";

			}
			if (sListName.trim().length() > 0)
			{
				query += "[./@subtype='" + sListName.trim() + "'] ";
				searchedValue = "role";

			}
			if (rListName.trim().length() > 0)
			{
				query += "[./@role='" + rListName.trim() + "'] ";
				searchedValue = "key";

			}

			query += "\nreturn <v>{string($v//@" + searchedValue + ")}</v> } </a> \n" + "}</result>";
		}

		_log = new Status(IStatus.INFO, CommonActivator.PLUGIN_ID, "MainSearcher getValues query: " + query);
		iLogger.log(_log);

		synchronized (_dbCon)
		{
			// get Connection
			XQConnection con = _dbCon.getConnection();
			XQPreparedExpression xqp;
			xqp = con.prepareExpression(query);

			XQResultSequence xqs = xqp.executeQuery();
			// execute the XQuery Expression

			FacetSaxHandler saxHanlder = new FacetSaxHandler();
			try
			{
				xqs.writeSequenceToSAX(saxHanlder);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			_facets = (ArrayList<String>) saxHanlder.getResultObject();

			con.close();
			_dbCon.closeDB("aspect");
		}

		Collections.sort(_facets);
		String[] help = _facets.toArray(new String[_facets.size()]);
		_facets = null;
		// if (type.equals("semantic"))
		// {
		// _facade.setAllSemantics(help);
		// }

		return help;
	}

	@Override
	public final HashMap<PdrId, Person> searchAllPersons() throws Exception, XMLStreamException
	{
		// set mainmem to on

		// get Connection
		XQConnection con = _dbCon.getConnection();

		XQPreparedExpression xqp;

		String query = "declare namespace podl=\"http://pdr.bbaw.de/namespaces/podl/\";\n"
				+ "for $x in collection(\"person\")//podl:person\n" + "return $x";
		_log = new Status(IStatus.INFO, CommonActivator.PLUGIN_ID, "MainSearcher searchAllPersons: " + query);
		iLogger.log(_log);
		XQResultSequence xqs;
		synchronized (_dbCon)
		{
			xqp = con.prepareExpression(query);

			xqs = xqp.executeQuery();
			// execute the XQuery Expression

			PersonSaxHandler saxHandler = new PersonSaxHandler();
			try
			{
				xqs.writeSequenceToSAX(saxHandler);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			_dbCon.closeDB("person");
			_allPersons = (HashMap<PdrId, Person>) saxHandler.getAllPersons();

			IStatus nPersons = new Status(IStatus.INFO, CommonActivator.PLUGIN_ID,
					"MainSearcher Number of all persons: " + _allPersons.size());
			iLogger.log(nPersons);

			query = "declare namespace aodl=\"http://pdr.bbaw.de/namespaces/aodl/\";\n" + "<result>{\n"
					+ "for $x in collection(\"aspect\")//aodl:aspect";

			if (_facade.getPersonNameTags(null).size() > 0)
			{
				query += "[";
				boolean addOr = false;
				for (String tag : _facade.getPersonNameTags(null))
				{
					if (addOr)
					{
						query += " or ";
					}
					query += ".//aodl:semanticStm='" + tag + "'";
					addOr = true;
				}

				if (addOr)
				{
					query += " or ";
				}
				query += ".//aodl:semanticStm='biographicalData'";
				addOr = true;

				if (addOr)
				{
					query += " or ";
				}
				query += ".//aodl:semanticStm='principalDescription'";
				addOr = true;

				query += "]\n";
			}
			// "[.//aodl:semanticStm='Name' or .//aodl:semanticStm='Normname' or .//aodl:semanticStm='NormName_DE'"
			// +
			// "or .//aodl:semanticStm='NormName_IT' or .//aodl:semanticStm='NormName_EN' or .//aodl:semanticStm='Nomi'"
			// +
			// "or .//aodl:semanticStm='nome' or .//aodl:semanticStm='nome di norma']\n"+

			query += "return  <person id='{$x//aodl:relation/@object}'>"
					+ "{$x//aodl:notification}{$x//aodl:semanticStm}</person>\n" + "}</result>";

			_log = new Status(IStatus.INFO, CommonActivator.PLUGIN_ID,
					"MainSearcher searchAllPersons query for person names: " + query);
			iLogger.log(_log);

			// synchronized (dbCon)
			// {
			xqp = con.prepareExpression(query);

			xqs = xqp.executeQuery();

			NamePersIDSaxHandler idSaxHandler = new NamePersIDSaxHandler(_allPersons);
			try
			{
				xqs.writeSequenceToSAX(idSaxHandler);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			_dbCon.closeDB("aspect");
			con.close();
			// tempPersons = (HashMap<PdrId, Person>)
			// idSaxHandler.getResultObject();

		}
		// log = new Status(IStatus.INFO, CommonActivator.PLUGIN_ID,
		// "MainSearcher entered merger, num of persons: " +
		// tempPersons.size());
		// iLogger.log(log);
		_log = new Status(IStatus.INFO, CommonActivator.PLUGIN_ID, "MainSearcher entered merger, num of all persons: "
				+ _allPersons.size());
		iLogger.log(_log);

		Person person;
		for (PdrId key : _allPersons.keySet())
		{
			person = _allPersons.get(key);
			_pdrDisplayNameProc.processDisplayName(person);

			// if (tempPersons.containsKey(key))
			// {
			// person = allPersons.get(key);
			// person.setBasicPersonData(tempPersons.get(key).getBasicPersonData());
			// pdrDisplayNameProc.processDisplayName(person);
			//
			// }
		}
		// IStatus nPersons = new Status(IStatus.INFO,
		// CommonActivator.PLUGIN_ID,
		// "MainSearcher Number of persons after merge: "
		// + allPersons.size());
		// iLogger.log(nPersons);
		// tempPersons = null;
		return _allPersons;
	}

	@Override
	public final HashMap<PdrId, ReferenceMods> searchAllReferences() throws Exception, XMLStreamException
	{
		// set mainmem to on

		String query = "declare namespace mods=\"http://www.loc.gov/mods/v3\";\n" + "<result>{\n"
				+ "for $x in collection(\"reference\")//mods:mods\n" + "order by $x/@ID\n"
				+ "return <r id=\"{$x/@ID}\">{$x}</r>" + "}</result>";

		XQResultSequence xqs;
		synchronized (_dbCon)
		{
			XQConnection con = _dbCon.getConnection();

			XQPreparedExpression xqp;

			_log = new Status(IStatus.INFO, CommonActivator.PLUGIN_ID, "MainSearcher searchAllReferences: " + query);
			iLogger.log(_log);

			xqp = con.prepareExpression(query);

			xqs = xqp.executeQuery();
			// execute the XQuery Expression
			ReferenceSaxHandler saxHandler = new ReferenceSaxHandler();
			try
			{
				xqs.writeSequenceToSAX(saxHandler);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			con.close();
			_dbCon.closeDB("reference");
			HashMap<PdrId, ReferenceMods> result = (HashMap<PdrId, ReferenceMods>) saxHandler.getResultObject();
			return result;
		}
	}

	@Override
	public final HashMap<String, ReferenceModsTemplate> searchAllReferenceTemplates() throws Exception
	{

		String query = "declare namespace rodl=\"http://pdr.bbaw.de/namespaces/rodl/\";\n" + "<resultTemplates>{\n"
				+ "for $x in collection(\"refTemplate\")//refTemplate\n" + "order by $x//@genre\n"
				+ "return <template>{$x}</template>" + "}</resultTemplates>";

		_log = new Status(IStatus.INFO, CommonActivator.PLUGIN_ID, "MainSearcher searchAllReferenceTemplates: " + query);
		iLogger.log(_log);

		synchronized (_dbCon)
		{
			XQConnection con = _dbCon.getConnection();

			XQPreparedExpression xqp;
			xqp = con.prepareExpression(query);

			XQResultSequence xqs = xqp.executeQuery();
			// execute the XQuery Expression

			ReferenceSaxHandler saxHandler = new ReferenceSaxHandler();
			try
			{
				xqs.writeSequenceToSAX(saxHandler);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			con.close();
			_dbCon.closeDB("refTemplate");
			return (HashMap<String, ReferenceModsTemplate>) saxHandler.getResultObject();

		}

	}

	@Override
	public final Aspect searchAspect(final PdrId id) throws Exception
	{
		// get Connection

		if (_searchAspects != null)
		{
			_searchAspects.clear();
		}

		String query = "declare namespace aodl=\"http://pdr.bbaw.de/namespaces/aodl/\";\n" + "<qAspect>{\n"
				+ "for $x in collection(\"aspect\")//aodl:aspect[./@id='" + id.toString() + "']";

		query += "\nreturn <a>{$x}</a>\n" + "}</qAspect>";

		_log = new Status(IStatus.INFO, CommonActivator.PLUGIN_ID, "MainSearcher searchAspect query: " + query);
		iLogger.log(_log);

		synchronized (_dbCon)
		{
			XQConnection con = _dbCon.getConnection();
			XQPreparedExpression xqp;
			xqp = con.prepareExpression(query);

			XQResultSequence xqs = xqp.executeQuery();
			// execute the XQuery Expression
			AspectSaxHandler saxHandler = new AspectSaxHandler(new PdrObject[]
			{}, null);
			try
			{
				xqs.writeSequenceToSAX(saxHandler);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			con.close();
			_dbCon.closeDB("aspect");
			_searchAspects = (HashMap<PdrId, Aspect>) saxHandler.getResultObject();
		}

		IStatus nAspects = new Status(IStatus.INFO, CommonActivator.PLUGIN_ID, "Number of all aspects: "
				+ _searchAspects.size());
		iLogger.log(nAspects);
		Aspect resultAspect = null;
		for (PdrId key : _searchAspects.keySet())
		{
			resultAspect = _searchAspects.get(key);
			break;
		}
		_searchAspects.clear();

		return resultAspect;
	}

	@Override
	public final Aspect searchAspect(final String id) throws Exception
	{
		// get Connection

		_searchAspects.clear();

		String query = "declare namespace aodl=\"http://pdr.bbaw.de/namespaces/aodl/\";\n" + "<qAspect>{\n"
				+ "for $x in collection(\"aspect\")//aodl:aspect[./@id='" + id + "']";

		query += "\nreturn <a>{$x}</a>\n" + "}</qAspect>";

		_log = new Status(IStatus.INFO, CommonActivator.PLUGIN_ID, "MainSearcher searchAspect query: " + query);
		iLogger.log(_log);

		synchronized (_dbCon)
		{
			XQConnection con = _dbCon.getConnection();
			XQPreparedExpression xqp;
			xqp = con.prepareExpression(query);

			XQResultSequence xqs = xqp.executeQuery();
			// execute the XQuery Expression

			AspectSaxHandler saxHandler = new AspectSaxHandler(new PdrObject[]
			{}, null);
			try
			{
				xqs.writeSequenceToSAX(saxHandler);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			con.close();
			_dbCon.closeDB("aspect");
			_searchAspects = (HashMap<PdrId, Aspect>) saxHandler.getResultObject();
		}

		IStatus nAspects = new Status(IStatus.INFO, CommonActivator.PLUGIN_ID, "Number of all aspects: "
				+ _searchAspects.size());
		iLogger.log(nAspects);
		Aspect resultAspect = null;
		for (PdrId key : _searchAspects.keySet())
		{
			resultAspect = _searchAspects.get(key);
			break;
		}
		_searchAspects.clear();

		return resultAspect;
	}

	@Override
	public HashMap<PdrId, Aspect> searchAspects(final PdrQuery q, final IProgressMonitor monitor) throws Exception
	{

		// Vector <Aspect> resultPersons;
		boolean wildcards = false;
		// remove empty criterias from vector
		boolean notFirst = false;
		for (Criteria c : q.getCriterias())
		{
			if (c.getType().equals("tagging"))
			{
				if ((c.getSearchText() == null || c.getSearchText().trim().length() == 0)
						&& ((c.getCrit0() == null || c.getCrit1() == null) || (c.getCrit0().equals("ALL") && c
								.getCrit1().equals("ALL"))))
				{
					_log = new Status(IStatus.INFO, CommonActivator.PLUGIN_ID, "MainSearcher criteria deleted, type "
							+ c.getType());
					iLogger.log(_log);

					if (notFirst)
					{
						c.setEmpty(true);
					}
					notFirst = true;

				}
			}
			else if (c.getType().equals("relation"))
			{
				if ((c.getSearchText() == null || c.getSearchText().trim().length() < 0)
						&& (c.getRelationContext().equals("ALL"))
						&& (c.getRelationClass() == null || c.getRelationClass().equals("ALL")))
				{
					_log = new Status(IStatus.INFO, CommonActivator.PLUGIN_ID, "MainSearcher criteria deleted, type "
							+ c.getType());
					iLogger.log(_log);

					c.setEmpty(true);

				}
			}
			else if (c.getType().equals("date"))
			{
				if (c.getDateFrom().getYear() < 1000 || c.getDateTo().getYear() < 1000)
				{
					_log = new Status(IStatus.INFO, CommonActivator.PLUGIN_ID, "MainSearcher criteria deleted, type "
							+ c.getType());
					iLogger.log(_log);
					c.setEmpty(true);
				}
			}
			else if (c.getType().equals("reference"))
			{
				if ((c.getSearchText() == null || c.getSearchText().trim().length() < 0)
						&& (c.getCrit0().equals("ALL")))
				{
					_log = new Status(IStatus.INFO, CommonActivator.PLUGIN_ID, "MainSearcher criteria deleted, type "
							+ c.getType());
					iLogger.log(_log);

					c.setEmpty(true);
				}
			}

		}
		int l = 0;
		q.getCriterias().size();
		while (!q.getCriterias().isEmpty() && l < q.getCriterias().size())
		{
			if (q.getCriterias().get(l).isEmpty())
			{
				q.getCriterias().remove(l);

			}
			l++;
		}
		String query = "declare namespace aodl=\"http://pdr.bbaw.de/namespaces/aodl/\";\n";

		boolean fuzzy = false;
		boolean date = false;
		for (int i = 0; i < q.getCriterias().size(); i++)
		{
			if (q.getCriterias().get(i) != null && !q.getCriterias().get(i).isEmpty())
			{
				if (q.getCriterias().get(i).getType() != null && q.getCriterias().get(i).getType().equals("tagging"))
				{
					if (q.getCriterias().get(i).getSearchText() != null
							&& q.getCriterias().get(i).getSearchText().trim().length() > 0)
					{
						if (q.getCriterias().get(i).getSearchText().contains("?")
								|| q.getCriterias().get(i).getSearchText().contains("*"))
						{
							wildcards = true;
						}
					}
					if (q.getCriterias().get(i) != null && q.getCriterias().get(i).isFuzzy())
					{
						fuzzy = true;
					}
				}
				if (q.getCriterias().get(i).getType() != null && q.getCriterias().get(i).getType().equals("date"))
				{
					date = true;
				}
			}
		}
		if (!wildcards)
		{
			if (fuzzy)
			{
				query += "declare ft-option using fuzzy using stemming;\n";
			}
			else
			{
				query += "declare ft-option using case sensitive using stemming;\n";
			}
		}

		if (date)
		{
			query += "declare function local:yyyy-mm-dd-to-date\n"
					+ "( $dateString as xs:string? )  as xs:date? {\n"
					+

					"if (empty($dateString))\n"
					+ "then ()\n"
					+ "else if (not(matches($dateString, '^(\\d{4})-?(\\d{2})?-?(\\d{2})?$')))\n"
					+
					// "then error(xs:QName('Invalid_Date_Format '))\n"+
					"then ()\n" + "(: year :)\n" + "else if (matches($dateString, '^\\d{4}$'))\n"
					+ "then xs:date(replace($dateString, '^(\\d{4})$', '$1-01-01'))\n" + "(:year-month:)\n"
					+ "else if (matches($dateString, '^\\d{4}-\\d{2}$'))\n"
					+ "then xs:date(replace($dateString, '^(\\d{4})-(\\d{2})$', '$1-$2-01'))\n"
					+ "(:year-month-day:)\n" + "else if (matches($dateString, '^\\d{4}-\\d{2}-\\d{2}$'))\n"
					+ "then xs:date(replace($dateString,\n" + "'^(\\d{4})-(\\d{2})-(\\d{2})$', '$1-$2-$3'))\n"
					+ "else\n" + "error(xs:QName($dateString))\n" + "} ;";

		}

		query += "<qAspect>{\n";
		query += "for $x in collection(\"aspect\")//aodl:aspect";

		for (int i = 0; i < q.getCriterias().size(); i++)
		{
			if (q.getCriterias().get(i) != null && !q.getCriterias().get(i).isEmpty()
					&& q.getCriterias().get(i).getType() != null && q.getCriterias().get(i).getType().equals("tagging"))
			{
				if (q.getCriterias().get(i).getCrit0() != null)
				{
					if (q.getCriterias().get(i).getCrit0().equalsIgnoreCase("ALL"))
					{

					}
					else if (_facade.isPersonNameTag(q.getCriterias().get(i).getCrit0()))

					{
						if (_facade.getPersonNameTags(null).size() > 0)
						{
							query += "[";
							boolean addOr = false;
							for (String tag : _facade.getPersonNameTags(null))
							{
								if (addOr)
								{
									query += " or ";
								}
								query += ".//aodl:semanticStm='" + tag + "'";
								addOr = true;
							}
							query += "]\n";
						}
					}
					else
					{
						query += "[.//aodl:semanticStm='" + q.getCriterias().get(i).getCrit0() + "']";
					}
				}
				if (q.getCriterias().get(i).getCrit1() != null
						&& !q.getCriterias().get(i).getCrit1().equalsIgnoreCase("ALL"))
				{
					query += "[.//aodl:" + q.getCriterias().get(i).getCrit1();
					if (q.getCriterias().get(i).getCrit2() != null)
					{
						query += "/@type='" + q.getCriterias().get(i).getCrit2().trim() + "'] ";
						if (q.getCriterias().get(i).getCrit3() != null)
						{
							query += "[.//aodl:" + q.getCriterias().get(i).getCrit1() + "/@subtype='"
									+ q.getCriterias().get(i).getCrit3().trim() + "'] ";
						}
						if (q.getCriterias().get(i).getCrit4() != null)
						{
							query += "[.//aodl:" + q.getCriterias().get(i).getCrit1() + "/@role='"
									+ q.getCriterias().get(i).getCrit4().trim() + "'] ";
						}
					}
					else
					{
						query += "]";
					}
				}
				if (q.getCriterias().get(i).getSearchText() != null
						&& q.getCriterias().get(i).getSearchText().trim().length() > 0)
				{
					if (!wildcards)
					{
						if (q.getCriterias().get(i).getCrit1() != null
								&& !q.getCriterias().get(i).getCrit1().equalsIgnoreCase("ALL"))
						{
							query += "[.//aodl:" + q.getCriterias().get(i).getCrit1() + "/text() contains text '"
									+ q.getCriterias().get(i).getSearchText() + "']";
						}
						else
						{
							query += "[.//text() contains text '" + q.getCriterias().get(i).getSearchText() + "'"
									+ " or .//*/@* = \"" + q.getCriterias().get(i).getSearchText() + "\"]";
						}
					}

				}
			}
			else if (q.getCriterias().get(i) != null && q.getCriterias().get(i).getType() != null
					&& q.getCriterias().get(i).getType().equals("relation"))
			{
				if (q.getCriterias().get(i).getRelationContext() != null)
				{
					if (q.getCriterias().get(i).getRelationContext().equalsIgnoreCase("ALL"))
					{
					}
					else
					{
						query += "[.//aodl:relation/@context='" + q.getCriterias().get(i).getRelationContext() + "']";
					}
				}
				if (q.getCriterias().get(i).getRelationClass() != null
						&& !q.getCriterias().get(i).getRelationClass().equalsIgnoreCase("ALL"))
				{
					query += "[.//aodl:relation/@class='" + q.getCriterias().get(i).getRelationClass() + "'] ";
				}
				if (q.getCriterias().get(i).getSearchText() != null)
				{
					query += "[.//aodl:relation/text() contains text '" + q.getCriterias().get(i).getSearchText()
							+ "']";
				}
			}
			else if (date)
			{

				if (q.getCriterias().get(i).getDateType() != null)
				{
					query += "[.//aodl:time/@type='" + q.getCriterias().get(i).getDateType() + "']";

				}

			}
			else if (q.getCriterias().get(i) != null && q.getCriterias().get(i).getType() != null
					&& q.getCriterias().get(i).getType().equals("reference"))
			{
				PdrQuery qhelp = new PdrQuery();
				qhelp.setType(2);
				qhelp.setCriterias(new Vector<Criteria>());
				qhelp.getCriterias().add(q.getCriterias().get(i));

				Vector<String> refIds = new Vector<String>();
				for (ReferenceMods r : searchReferences(qhelp, monitor))
				{
					refIds.add(r.getPdrId().toString());
				}
				for (int k = 0; k < refIds.size(); k++)
				{
					query += "[.//aodl:reference='" + refIds.get(k) + "']";
				}
			}
		}

		if (wildcards)
		{
			query += "\nwhere ";
			boolean first = true;
			for (int i = 0; i < q.getCriterias().size(); i++)
			{
				if (q.getCriterias().get(i) != null && !q.getCriterias().get(i).isEmpty()
						&& q.getCriterias().get(i).getSearchText() != null
						&& q.getCriterias().get(i).getSearchText().trim().length() > 0)
				{
					// String whereString = null;
					String whereString = " $x//text() contains text ";

					if (q.getCriterias().get(i).isFuzzy() && q.getCriterias().get(i).getSearchText().contains(" "))
					{
						String[] st = q.getCriterias().get(i).getSearchText().split(" ");
						for (String s : st)
						{
							if (!first)
							{
								query += " or ";
							}
							first = false;
							query += whereString + "('" + parseWildCardsString(s) + "')";
							if (wildcards)
							{
								query += " using wildcards";
							}
						}
					}
					else
					{
						if (!first)
						{
							query += " and ";
						}
						first = false;
						query += whereString + "('" + parseWildCardsString(q.getCriterias().get(i).getSearchText())
								+ "') ";
						if (wildcards)
						{
							query += " using wildcards";
						}
					}
				}
			}
		}
		if (date)
		{
			query += "\nfor $d in $x//aodl:time";
			for (int i = 0; i < q.getCriterias().size(); i++)
			{

				if (q.getCriterias().get(i).getDateFrom() != null)
				{
					// System.out.println("dateFrom " +
					// q.getCriterias().get(i).getDateFrom().toString());
					if (q.getCriterias().get(i).getDateFrom().getMonth() == 0)
					{
						q.getCriterias().get(i).getDateFrom().setMonth(01);
					}
					if (q.getCriterias().get(i).getDateFrom().getDay() == 0)
					{
						q.getCriterias().get(i).getDateFrom().setDay(01);
					}
					query += "[local:yyyy-mm-dd-to-date(./text()) >= xs:date('"
							+ q.getCriterias().get(i).getDateFrom().toString() + "')]";
				}
				if (q.getCriterias().get(i).getDateTo() != null)
				{
					// System.out.println("dateTo " +
					// q.getCriterias().get(i).getDateTo().toString());
					if (q.getCriterias().get(i).getDateTo().getMonth() == 0)
					{
						q.getCriterias().get(i).getDateTo().setMonth(12);
					}
					if (q.getCriterias().get(i).getDateTo().getDay() == 0)
					{
						q.getCriterias().get(i).getDateTo().setDay(31);
					}
					query += "[local:yyyy-mm-dd-to-date(./text()) <= xs:date('"
							+ q.getCriterias().get(i).getDateTo().toString() + "')]";
				}
			}
			query += "\nreturn <a>{$x}</a>\n" + "}</qAspect>";
		}
		else
		{
			query += "\nreturn <a>{$x}</a>\n" + "}</qAspect>";
		}

		_log = new Status(IStatus.INFO, CommonActivator.PLUGIN_ID, "MainSearcher aspect query " + query);
		iLogger.log(_log);

		synchronized (_dbCon)
		{
			// get Connection
			XQConnection con = _dbCon.getConnection();
			XQPreparedExpression xqp;
			xqp = con.prepareExpression(query);

			XQResultSequence xqs = xqp.executeQuery();
			// execute the XQuery Expression
			AspectSaxHandler saxHandler = new AspectSaxHandler(new PdrObject[]
			{}, monitor);
			try
			{
				xqs.writeSequenceToSAX(saxHandler);

			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			con.close();
			_dbCon.closeDB("aspect");
			_searchAspects = (HashMap<PdrId, Aspect>) saxHandler.getResultObject();
			if (_searchAspects != null)
			{
				IStatus st = new Status(IStatus.INFO, CommonActivator.PLUGIN_ID, "Number of search aspects: "
						+ _searchAspects.size());
				iLogger.log(st);
			}
			else
			{
				IStatus st = new Status(IStatus.INFO, CommonActivator.PLUGIN_ID, "Search aspects null");
				iLogger.log(st);
			}

		}



		final HashMap<PdrId, Aspect> resultAspects = _searchAspects;
		_searchAspects = null;

		_facade.getLoadedAspects().putAll(resultAspects);

		return resultAspects;


	}

	@Override
	public final PdrObject searchAspectsByReference(final PdrObject pdrObject, final IProgressMonitor monitor)
			throws Exception
	{

		String query = "declare namespace aodl=\"http://pdr.bbaw.de/namespaces/aodl/\";\n" + "<result>{\n"
				+ "for $a in collection(\"aspect\")//aodl:aspect[.//aodl:reference/text()='"
				+ pdrObject.getPdrId().toString() + "']\n" + "return <a>{$a}</a>\n" + "}</result>\n";

		_log = new Status(IStatus.INFO, CommonActivator.PLUGIN_ID, "MainSearcher searchAspectsByReference query: "
				+ query);
		iLogger.log(_log);
		synchronized (_dbCon)
		{
			XQConnection con = _dbCon.getConnection();

			XQPreparedExpression xqp;
			xqp = con.prepareExpression(query);

			// execute the XQuery Expression
			XQResultSequence xqs = xqp.executeQuery();
			AspectSaxHandler saxHandler = new AspectSaxHandler(pdrObject, monitor);
			try
			{
				xqs.writeSequenceToSAX(saxHandler);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

			con.close();
			_dbCon.closeDB("aspect");
			pdrObject.setAspectsLoaded(true);

			return (PdrObject) saxHandler.getResultObject();
		}

	}

	@Override
	public final PdrObject searchAspectsByRelatedObject(final PdrObject pdrObject, final IProgressMonitor monitor)
			throws Exception
	{

		String query = "declare namespace aodl=\"http://pdr.bbaw.de/namespaces/aodl/\";\n" + "<result>{\n"
				+ "for $a in collection(\"aspect\")//aodl:aspect[.//aodl:relationStm/@subject='"
				+ pdrObject.getPdrId().toString() + "' or .//aodl:relation/@object ='"
				+ pdrObject.getPdrId().toString() + "']\n"
				+ "return <a>{$a}</a>\n" + "}</result>\n";

		_log = new Status(IStatus.INFO, CommonActivator.PLUGIN_ID, "MainSearcher searchAspectsByRelatedObject: "
				+ query);
		iLogger.log(_log);
		synchronized (_dbCon)
		{
			XQConnection con = _dbCon.getConnection();
			XQPreparedExpression xqp;
			xqp = con.prepareExpression(query);
			// execute the XQuery Expression
			XQResultSequence xqs = xqp.executeQuery();

			AspectSaxHandler saxHandler = new AspectSaxHandler(pdrObject, monitor);
			try
			{
				xqs.writeSequenceToSAX(saxHandler);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

			con.close();
			_dbCon.closeDB("aspect");
			pdrObject.setAspectsLoaded(true);

			return (PdrObject) saxHandler.getResultObject();
		}
	}

	@Override
	public final Object searchFacetAspects(final PdrQuery pdrQuery, final IProgressMonitor monitor) throws Exception
	{
		if (pdrQuery.getFacets() != null)
		{
			Calendar calendar = new GregorianCalendar();
			int startSec = calendar.get(Calendar.SECOND) + 1;
			Vector<FacetResultNode> facetNodes = new Vector<FacetResultNode>();
			FacetResultNode facetNode;
			Criteria criteria = new Criteria();
			criteria.setFuzzy(false);
			criteria.setType("tagging");
			criteria.setOperator("AND");
			pdrQuery.getCriterias().insertElementAt(criteria, 0);
			for (IAEPresentable facet : pdrQuery.getFacets().values())
			{
				facet.setValue(parseFacet(facet.getValue()));
				facetNode = new FacetResultNode(facet.getLabel(), "facet");

				// System.out.println("query key " + pdrQuery.getKey());

				if (pdrQuery.getKey() != null)
				{
					if (pdrQuery.getKey().equals("type"))
					{
						criteria.setCrit2(facet.getValue());
					}
					else if (pdrQuery.getKey().equals("subtype"))
					{
						criteria.setCrit3(facet.getValue());
					}
					else if (pdrQuery.getKey().equals("role"))
					{
						criteria.setCrit4(facet.getValue());
					}
					else if (pdrQuery.getKey().equals("content"))
					{
						criteria.setSearchText(facet.getValue());
					}
				}
				else
				{
					criteria.setSearchText(facet.getValue());
				}
				if (new GregorianCalendar().get(Calendar.SECOND) < startSec
						|| new GregorianCalendar().get(Calendar.SECOND) < 3)
				{
				try
				{
						facetNode.setObjects(searchPersons(pdrQuery, monitor));
						HashMap<PdrId, Aspect> searchAspects = searchAspects(pdrQuery, monitor);
					if (monitor != null)
					{
						monitor.worked(1);
						if (monitor.isCanceled())
						{
							Collections.sort(facetNodes);
							return facetNodes;
						}
					}
					Vector<Aspect> aspects = new Vector<Aspect>();
					for (Aspect a : searchAspects.values())
					{
						aspects.add(a);
					}
					facetNode.setObjects(aspects);
					if (facetNode.getObjects() != null)
					{
						_log = new Status(IStatus.INFO, Activator.PLUGIN_ID,
								"MainSearcher number of aspects per facet " + facetNode.getObjects().size() + " facet "
										+ facet);
						iLogger.log(_log);
					}
				}
				catch (XQException e)
				{
					e.printStackTrace();
				}
				if (facetNode.getObjects() != null && facetNode.getObjects().size() > 0)
				{
					facetNodes.add(facetNode);
				}
				}
				else
				{
					facetNode = new FacetResultNode(facet.getLabel(), "facet");
					PdrQuery pq = pdrQuery.clone();
					facetNode.setPdrQuery(pq);
					facetNodes.add(facetNode);
				}
			}
			Collections.sort(facetNodes);
			return facetNodes;
		}
		return null;
	}

	@Override
	public final Vector<FacetResultNode> searchFacetPersons(final PdrQuery pdrQuery, final IProgressMonitor monitor)
			throws Exception
	{
		if (pdrQuery.getFacets() != null)
		{
			Calendar calendar = new GregorianCalendar();
			int startSec = calendar.get(Calendar.SECOND) + 1;
			Vector<FacetResultNode> facetNodes = new Vector<FacetResultNode>();
			FacetResultNode facetNode;
			Criteria criteria = new Criteria();
			criteria.setFuzzy(false);
			criteria.setType("tagging");
			criteria.setOperator("AND");
			pdrQuery.getCriterias().insertElementAt(criteria, 0);
			for (IAEPresentable facet : pdrQuery.getFacets().values())
			{

				facet.setValue(parseFacet(facet.getValue()));
				facetNode = new FacetResultNode(facet.getLabel(), "facet");
				// System.out.println("query key " + pdrQuery.getKey());
				if (pdrQuery.getKey() != null)
				{
					if (pdrQuery.getKey().equals("type"))
					{
						criteria.setCrit2(facet.getValue());
					}
					else if (pdrQuery.getKey().equals("subtype"))
					{
						criteria.setCrit3(facet.getValue());
					}
					else if (pdrQuery.getKey().equals("role"))
					{
						criteria.setCrit4(facet.getValue());
					}
					else if (pdrQuery.getKey().equals("content"))
					{
						criteria.setSearchText(facet.getValue());
					}
				}
				else
				{
					pdrQuery.getCriterias().get(0).setSearchText(facet.getValue());
				}
				// System.out.println("start " + startSec + " current " + new
				// GregorianCalendar().get(Calendar.SECOND));
				if (new GregorianCalendar().get(Calendar.SECOND) < startSec
						|| new GregorianCalendar().get(Calendar.SECOND) < 3)
				{
					try
					{
						facetNode.setObjects(searchPersons(pdrQuery, monitor));
						if (monitor != null)
						{
							monitor.worked(1);
							if (monitor.isCanceled())
							{
								Collections.sort(facetNodes);
								return facetNodes;
							}
						}
						if (facetNode.getObjects() != null)
						{
							_log = new Status(IStatus.INFO, Activator.PLUGIN_ID,
									"MainSearcher number of persons per facet " + facetNode.getObjects().size()
											+ " facet " + facet);
							iLogger.log(_log);
						}

					}
					catch (XQException e)
					{
						e.printStackTrace();
					}
					if (facetNode.getObjects() != null && facetNode.getObjects().size() > 0)
					{
						facetNodes.add(facetNode);
					}
				}
				else
				{
					facetNode = new FacetResultNode(facet.getLabel(), "facet");
					PdrQuery pq = pdrQuery.clone();
					facetNode.setPdrQuery(pq);
					facetNodes.add(facetNode);
				}
			}
			Collections.sort(facetNodes);

			return facetNodes;
		}
		return null;
	}

	@Override
	public final Object searchFacetReferences(final PdrQuery pdrQuery, final IProgressMonitor monitor) throws Exception
	{
		if (pdrQuery.getFacets() != null)
		{
			Vector<FacetResultNode> facetNodes = new Vector<FacetResultNode>();
			FacetResultNode facetNode;
			for (IAEPresentable facet : pdrQuery.getFacets().values())
			{
				facet.setValue(parseFacet(facet.getValue()));
				facetNode = new FacetResultNode(facet.getLabel(), "facet");
				pdrQuery.getCriterias().get(0).setSearchText(facet.getValue());

				try
				{
					Vector<ReferenceMods> refs = new Vector<ReferenceMods>();
					Vector<ReferenceMods> results = searchReferences(pdrQuery, monitor);
					if (results != null && !results.isEmpty())
					{
						for (ReferenceMods r : results)
						{
							refs.add(r);
						}
						facetNode.setObjects(refs);
						if (facetNode.getObjects() != null)
						{
							_log = new Status(IStatus.INFO, Activator.PLUGIN_ID,
									"MainSearcher number of refs per facet " + facetNode.getObjects().size()
											+ " facet " + facet);
							iLogger.log(_log);
						}
					}

				}
				catch (XQException e)
				{
					e.printStackTrace();
				}
				if (facetNode.getObjects() != null && facetNode.getObjects().size() > 0)
				{
					facetNodes.add(facetNode);
				}
			}
			Collections.sort(facetNodes);
			return facetNodes;
		}
		return null;
	}

	@Override
	public final String searchObjectString(final String col, final String name) throws Exception
	{
		String query = "";
		String id = name.substring(0, 23);
		String obj = null;
		if (col.equals("person"))
		{
			query = "declare namespace podl=\"http://pdr.bbaw.de/namespaces/podl/\";\n"
					+ "for $x in collection(\"person\")//podl:person[./@id='" + id + "']\n" + "return $x";
		}
		else if (col.equals("aspect"))
		{
			query = "declare namespace aodl=\"http://pdr.bbaw.de/namespaces/aodl/\";\n"
					+ "for $x in collection(\"aspect\")//aodl:aspect[./@id='" + id + "']\n" + "return $x";
		}
		else if (col.equals("reference"))
		{
			query = "declare namespace mods=\"http://www.loc.gov/mods/v3\";\n"
					+ "for $x in collection(\"reference\")//mods:mods[./@ID='" + id + "']\n" + "return $x";
		}
		else if (col.equals("users"))
		{
			query = "declare namespace uodl=\"http://pdr.bbaw.de/namespaces/uodl/\";\n"
					+ "for $x in collection(\"user\")//uodl:user[./@id='" + id + "']\n" + "return $x";
		}
		XQConnection con = _dbCon.getConnection();
		XQPreparedExpression xqp;
		// System.out.println("searchObjectString " + query);
		xqp = con.prepareExpression(query);

		XQResultSequence xqs = xqp.executeQuery();
		// execute the XQuery Expression

		while (xqs.next())
		{
			obj = xqs.getItemAsString(null);
			Matcher m = AEConstants.PDR_ID_PATTERN.matcher(obj);
			if (m.find())
			{
				break;
			}
		}
		con.close();
		if (obj != null)
		{
			return obj;
		}
		return null;
	}

	@Override
	public Vector<Person> searchPersons(final PdrQuery q, final IProgressMonitor monitor) throws Exception
	{
		// get Connection

		Vector<PdrId> resultIds = new Vector<PdrId>();
		Vector<Person> resultPersons;
		Vector<PdrId> refResultIds;
		boolean ref = false;
		boolean wildcards = false;
		// remove empty criterias from vector
		boolean notFirst = false;
		for (Criteria c : q.getCriterias())
		{
			if (c.getType().equals("tagging"))
			{
				if ((c.getSearchText() == null || c.getSearchText().trim().length() == 0)
						&& ((c.getCrit0() == null || c.getCrit1() == null) || (c.getCrit0().equals("ALL") && c
								.getCrit1().equals("ALL"))))
				{
					if (notFirst)
					{
						_log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "MainSearcher criteria deleted, type "
								+ c.getType());
						iLogger.log(_log);
						c.setEmpty(true);
					}
				}
				notFirst = true;
			}
			else if (c.getType().equals("relation"))
			{
				if ((c.getSearchText() == null || c.getSearchText().trim().length() < 0)
						&& (c.getRelationContext().equals("ALL"))
						&& (c.getRelationClass() == null || c.getRelationClass().equals("ALL")))
				{
					_log = new Status(IStatus.INFO, CommonActivator.PLUGIN_ID, "MainSearcher criteria deleted, type "
							+ c.getType());
					iLogger.log(_log);

					c.setEmpty(true);

				}
			}
			else if (c.getType().equals("date"))
			{
				if (c.getDateFrom().getYear() < 1000 || c.getDateTo().getYear() < 1000)
				{
					_log = new Status(IStatus.INFO, CommonActivator.PLUGIN_ID, "MainSearcher criteria deleted, type "
							+ c.getType());
					iLogger.log(_log);
					c.setEmpty(true);
				}
			}
			else if (c.getType().equals("reference"))
			{
				if ((c.getSearchText() == null || c.getSearchText().trim().length() < 0)
						&& (c.getCrit0().equals("ALL")))
				{
					_log = new Status(IStatus.INFO, CommonActivator.PLUGIN_ID, "MainSearcher criteria deleted, type "
							+ c.getType());
					iLogger.log(_log);

					c.setEmpty(true);
				}
			}

		}
		int l = 0;
		q.getCriterias().size();
		while (!q.getCriterias().isEmpty() && l < q.getCriterias().size())
		{
			if (q.getCriterias().get(l).isEmpty())
			{
				q.getCriterias().remove(l);

			}
			l++;
		}

		for (int i = 0; i < q.getCriterias().size(); i++)
		{
			if (q.getCriterias().get(i).getSearchText() != null
					&& q.getCriterias().get(i).getSearchText().trim().length() > 0)
			{
				if (q.getCriterias().get(i).getSearchText().contains("?")
						|| q.getCriterias().get(i).getSearchText().contains("*"))
				{
					wildcards = true;
				}
				else
				{
					wildcards = false;
				}
			}
			else
			{
				wildcards = false;
			}

			if (q.getCriterias().get(i) != null && !q.getCriterias().get(i).isEmpty())
			{
				String query = "declare namespace aodl=\"http://pdr.bbaw.de/namespaces/aodl/\";\n";

				String subQuery = "";

				if (q.getCriterias().get(i) != null && q.getCriterias().get(i).getType() != null
						&& q.getCriterias().get(i).getType().equals("tagging"))
				{
					if (!wildcards)
					{
						if (q.getCriterias().get(i) != null && q.getCriterias().get(i).isFuzzy())
						{
							query += "declare ft-option using fuzzy using stemming;\n";
						}
						else
						{
							query += "declare ft-option using case sensitive using stemming;\n";
						}
					}
					else
					{
						query += "declare ft-option using wildcards;\n";
					}
					query += "<result>{\n";
					query += "for $x in collection(\"aspect\")//aodl:aspect"; // [.//aodl:semanticStm='Name'
																				// or
																				// .//aodl:semanticStm='Normname']\n"+

					if (q.getCriterias().get(i).getCrit0() != null)
					{
						if (q.getCriterias().get(i).getCrit0().equalsIgnoreCase("ALL"))
						{

						}
						else if (_facade.isPersonNameTag(q.getCriterias().get(i).getCrit0()))

						{
							if (_facade.getPersonNameTags(null).size() > 0)
							{
								query += "[";
								boolean addOr = false;
								for (String tag : _facade.getPersonNameTags(null))
								{
									if (addOr)
									{
										query += " or ";
									}
									query += ".//aodl:semanticStm='" + tag + "'";
									addOr = true;
								}
								query += "]\n";
							}
						}
						else
						{
							query += "[.//aodl:semanticStm='" + q.getCriterias().get(i).getCrit0() + "']";
						}
					}
					if (q.getCriterias().get(i).getCrit1() != null
							&& !q.getCriterias().get(i).getCrit1().equalsIgnoreCase("ALL"))
					{
						query += "[.//aodl:" + q.getCriterias().get(i).getCrit1();
						subQuery = "ana=\"{$x//aodl:" + q.getCriterias().get(i).getCrit1();
						if (q.getCriterias().get(i).getCrit2() != null)
						{
							query += "/@type='" + q.getCriterias().get(i).getCrit2().trim() + "'] ";
							subQuery += "[./@type='" + q.getCriterias().get(i).getCrit2().trim() + "'] ";
							if (q.getCriterias().get(i).getCrit3() != null)
							{
								query += "[.//aodl:" + q.getCriterias().get(i).getCrit1() + "/@subtype='"
										+ q.getCriterias().get(i).getCrit3().trim() + "'] ";
								subQuery += "[./@subtype='" + q.getCriterias().get(i).getCrit3().trim() + "'] ";
							}
							if (q.getCriterias().get(i).getCrit4() != null)
							{
								subQuery += "[./@role='" + q.getCriterias().get(i).getCrit4().trim() + "'] ";
								query += "[.//aodl:" + q.getCriterias().get(i).getCrit1() + "/@role='"
										+ q.getCriterias().get(i).getCrit4().trim() + "'] ";
							}
						}
						else
						{
							query += "]";
						}
						subQuery += "/@ana}\"";
						// if (q.getCriterias().get(i).getCrit5() != null)
						// {
						// query+= "[.//aodl:" +
						// q.getCriterias().get(i).getCrit1() + "/@role='" +
						// q.getCriterias().get(i).getCrit5().trim() + "'] ";
						// }

					}
					if (q.getCriterias().get(i).getSearchText() != null
							&& q.getCriterias().get(i).getSearchText().trim().length() > 0)
					{
						String whereString = null;
						String whereStringAtt = " or $x//*/@* = ";
						;

						query += "\nwhere";
						if (q.getCriterias().get(i).getCrit1() != null
								&& !q.getCriterias().get(i).getCrit1().equalsIgnoreCase("ALL"))
						{
							whereString = " $x//aodl:" + q.getCriterias().get(i).getCrit1() + "/text() contains text ";
						}
						else
						{
							whereString = " $x//text() contains text ";
						}
						if (q.getCriterias().get(i).isFuzzy() && q.getCriterias().get(i).getSearchText().contains(" "))
						{
							String[] st = q.getCriterias().get(i).getSearchText().split(" ");
							boolean first = true;
							for (String s : st)
							{
								if (!first)
								{
									query += " or ";
								}
								first = false;
								query += whereString + "('" + parseWildCardsString(s) + "')";
								if (wildcards)
								{
									query += " using wildcards";
								}
								query += whereStringAtt + "\"" + s + "\"";

							}
						}
						else
						{
							query += whereString + "('" + parseWildCardsString(q.getCriterias().get(i).getSearchText())
									+ "') ";
							if (wildcards)
							{
								query += " using wildcards";
							}
							query += whereStringAtt + "\"" + q.getCriterias().get(i).getSearchText() + "\" ";

						}
					}
					query += "\nreturn <a id=\"{$x/@id}\">{\n" + "for $r in $x//aodl:relationStm\n" + "return  <p  "
							+ subQuery + ">{$r/@subject}" + "{for $rel in $r//aodl:relation\n"
							+ "return <object>{string($rel/@object)}</object>}\n" + "</p>\n" + "}</a>\n" + "}</result>";
				}
				else if (q.getCriterias().get(i) != null && q.getCriterias().get(i).getType() != null
						&& q.getCriterias().get(i).getType().equals("relation"))
				{
					if (q.getCriterias().get(i) != null && q.getCriterias().get(i).isFuzzy())
					{
						query += "declare ft-option using fuzzy using stemming;\n";
					}
					else
					{
						query += "declare ft-option using case sensitive using stemming;\n";
					}
					query += "<result>{\n";
					query += "for $x in collection(\"aspect\")//aodl:aspect"; // [.//aodl:semanticStm='Name'
																				// or
																				// .//aodl:semanticStm='Normname']\n"+

					if (q.getCriterias().get(i).getRelationContext() != null)
					{
						if (q.getCriterias().get(i).getRelationContext().equalsIgnoreCase("ALL"))
						{

						}
						else
						{
							query += "[.//aodl:relation/@context='" + q.getCriterias().get(i).getRelationContext()
									+ "']";
						}
					}
					if (q.getCriterias().get(i).getRelationClass() != null
							&& !q.getCriterias().get(i).getRelationClass().equalsIgnoreCase("ALL"))
					{
						query += "[.//aodl:relation/@class='" + q.getCriterias().get(i).getRelationClass() + "'] ";

					}
					if (q.getCriterias().get(i).getSearchText() != null
							&& q.getCriterias().get(i).getSearchText().trim().length() > 0)
					{
						String whereString = null;
						query += "\nwhere";
						whereString = " $x//aodl:relation/text() contains text ";

						if (q.getCriterias().get(i).getSearchText().contains(" "))
						{
							String[] st = q.getCriterias().get(i).getSearchText().split(" ");
							boolean first = true;
							for (String s : st)
							{
								if (!first)
								{
									query += " and ";
								}
								first = false;
								query += whereString + "('" + parseWildCardsString(s) + "')";
								if (wildcards)
								{
									query += " using wildcards";
								}
							}
						}
						else
						{
							query += whereString + "('" + parseWildCardsString(q.getCriterias().get(i).getSearchText())
									+ "') ";
							if (wildcards)
							{
								query += " using wildcards";
							}
						}
						// query+= "[.//aodl:relation/text() contains text '" +
						// q.getCriterias().get(i).getSearchText() + "']";
					}
					query += "\nreturn <a id=\"{$x/@id}\">{\n" + "for $r in $x//aodl:relationStm\n"
							+ "return  <p>{$r/@subject}" + "{for $rel in $r//aodl:relation\n"
							+ "return <object>{string($rel/@object)}</object>}\n" + "</p>\n" + "}</a>\n" + "}</result>";
				}
				else if (q.getCriterias().get(i) != null && q.getCriterias().get(i).getType() != null
						&& q.getCriterias().get(i).getType().equals("date"))
				{
					// query+="declare namespace functx=\"http://www.functx.com/\";\n"+
					query += "declare function local:yyyy-mm-dd-to-date\n"
							+ "( $dateString as xs:string? )  as xs:date? {\n"
							+

							"if (empty($dateString))\n"
							+ "then ()\n"
							+ "else if (not(matches($dateString, '^(\\d{4})-?(\\d{2})?-?(\\d{2})?$')))\n"
							+
							// "then error(xs:QName('Invalid_Date_Format '))\n"+
							"then ()\n"

							+ "(: year :)\n" + "else if (matches($dateString, '^\\d{4}$'))\n"
							+ "then xs:date(replace($dateString, '^(\\d{4})$', '$1-01-01'))\n" + "(:year-month:)\n"
							+ "else if (matches($dateString, '^\\d{4}-\\d{2}$'))\n"
							+ "then xs:date(replace($dateString, '^(\\d{4})-(\\d{2})$', '$1-$2-01'))\n"
							+ "(:year-month-day:)\n" + "else if (matches($dateString, '^\\d{4}-\\d{2}-\\d{2}$'))\n"
							+ "then xs:date(replace($dateString,\n" + "'^(\\d{4})-(\\d{2})-(\\d{2})$', '$1-$2-$3'))\n"
							+ "else\n" + "error(xs:QName($dateString))\n" + "} ;";

					query += "\n<result>{\n" + "for $x in collection(\"aspect\")//aodl:aspect";
					if (q.getCriterias().get(i).getCrit0() != null && !q.getCriterias().get(i).getCrit0().equals("ALL"))
					{
						query += "[.//aodl:semanticStm/text()='" + q.getCriterias().get(i).getCrit0() + "']";

					}
					if (q.getCriterias().get(i).getDateType() != null)
					{
						query += "[.//aodl:time/@type='" + q.getCriterias().get(i).getDateType() + "']";

					}

					query += "\nfor $d in $x//aodl:time";

					if (q.getCriterias().get(i).getDateFrom() != null)
					{
						// System.out.println("dateFrom " +
						// q.getCriterias().get(i).getDateFrom().toString());
						if (q.getCriterias().get(i).getDateFrom().getMonth() == 0)
						{
							q.getCriterias().get(i).getDateFrom().setMonth(01);
						}
						if (q.getCriterias().get(i).getDateFrom().getDay() == 0)
						{
							q.getCriterias().get(i).getDateFrom().setDay(01);
						}
						query += "[local:yyyy-mm-dd-to-date(./text()) >= xs:date('"
								+ q.getCriterias().get(i).getDateFrom().toString() + "')]";

					}
					if (q.getCriterias().get(i).getDateTo() != null)
					{
						// System.out.println("dateTo " +
						// q.getCriterias().get(i).getDateTo().toString());
						if (q.getCriterias().get(i).getDateTo().getMonth() == 0)
						{
							q.getCriterias().get(i).getDateTo().setMonth(12);
						}
						if (q.getCriterias().get(i).getDateTo().getDay() == 0)
						{
							q.getCriterias().get(i).getDateTo().setDay(31);
						}
						query += "[local:yyyy-mm-dd-to-date(./text()) <= xs:date('"
								+ q.getCriterias().get(i).getDateTo().toString() + "')]";
					}

					query += "\nreturn <a id=\"{$x/@id}\">{\n" + "for $r in $x//aodl:relationStm\n"
							+ "return  <p>{$r/@subject}" + "{for $rel in $r//aodl:relation\n"
							+ "return <object>{string($rel/@object)}</object>}\n" + "</p>\n" + "}</a>\n" + "}</result>";
				}

				else if (q.getCriterias().get(i) != null && q.getCriterias().get(i).getType() != null
						&& q.getCriterias().get(i).getType().equals("reference"))
				{
					PdrQuery qhelp = new PdrQuery();
					qhelp.setType(2);
					qhelp.setCriterias(new Vector<Criteria>());
					qhelp.getCriterias().add(q.getCriterias().get(i));

					Vector<PdrId> refIds = new Vector<PdrId>();
					refResultIds = new Vector<PdrId>();
					ref = true;
					for (ReferenceMods r : searchReferences(qhelp, monitor))
					{
						refIds.add(r.getPdrId());
					}

					for (int k = 0; k < refIds.size(); k++)
					{
						if (monitor != null && monitor.isCanceled())
						{
							break;
						}
						query = "declare namespace aodl=\"http://pdr.bbaw.de/namespaces/aodl/\";\n";
						query += "<result>{\n";
						query += "for $x in collection(\"aspect\")//aodl:aspect[.//aodl:reference='" + refIds.get(k)
								+ "']";
						query += "\nreturn <a id=\"{$x/@id}\">{\n" + "for $r in $x//aodl:relationStm\n"
								+ "return  <p>{$r/@subject}" + "{for $rel in $r//aodl:relation\n"
								+ "return <object>{string($rel/@object)}</object>}\n" + "</p>\n" + "}</a>\n"
								+ "}</result>";

						_log = new Status(IStatus.INFO, CommonActivator.PLUGIN_ID,
								"MainSearcher searchPersons(final PdrQuery q) reference criteria: " + query);
						iLogger.log(_log);
						synchronized (_dbCon)
						{
							XQConnection con = _dbCon.getConnection();
							XQPreparedExpression xqp;
							xqp = con.prepareExpression(query);

							XQResultSequence xqs = xqp.executeQuery();
							// execute the XQuery Expression

							ComplexIDSaxHandler saxHandler = new ComplexIDSaxHandler(monitor);
							try
							{
								xqs.writeSequenceToSAX(saxHandler);
							}
							catch (Exception e)
							{
								e.printStackTrace();
							}
							_searchIds = (Vector<PdrId>) saxHandler.getResultObject();
							con.close();
							_dbCon.closeDB("reference");
						}
						refResultIds = getUnion(resultIds, _searchIds);
						_searchIds = null;
					}

					_searchIds = refResultIds;
				}

				if (!ref)
				{

					_log = new Status(IStatus.INFO, CommonActivator.PLUGIN_ID,
							"MainSearcher searchPersons(final PdrQuery q) " + query);
					iLogger.log(_log);
					synchronized (_dbCon)
					{
						XQConnection con = _dbCon.getConnection();
						XQPreparedExpression xqp;
						xqp = con.prepareExpression(query);

						XQResultSequence xqs = xqp.executeQuery();

						// execute the XQuery Expression
						ComplexIDSaxHandler saxHandler = new ComplexIDSaxHandler(monitor);

						try
						{
							xqs.writeSequenceToSAX(saxHandler);
						}
						catch (Exception e)
						{
							e.printStackTrace();
						}
						_searchIds = (Vector<PdrId>) saxHandler.getResultObject();

						con.close();
						_dbCon.closeDB("person");
						_dbCon.closeDB("aspect");
					}
					IStatus nPersons = new Status(IStatus.INFO, CommonActivator.PLUGIN_ID, "Number of all persons: "
							+ _facade.getNumberOfAllPersons());
					iLogger.log(nPersons);

					_log = new Status(IStatus.INFO, CommonActivator.PLUGIN_ID,
							"MainSearcher query for persons executed " + i + " times.");
					iLogger.log(_log);
				}
				ref = false;
				_log = new Status(IStatus.INFO, CommonActivator.PLUGIN_ID, "MainSearcher searchids size "
						+ _searchIds.size());
				iLogger.log(_log);
				Collections.sort(_searchIds);
				if (q.getCriterias().get(i).getOperator() != null && i > 0)
				{
					_log = new Status(IStatus.INFO, CommonActivator.PLUGIN_ID, "MainSearcher operant "
							+ q.getCriterias().get(i).getOperator());
					iLogger.log(_log);
					if (q.getCriterias().get(i).getOperator().equals("AND"))
					{
						resultIds = getInsection(resultIds, _searchIds);
					}
					else if (q.getCriterias().get(i).getOperator().equals("OR"))
					{
						resultIds = getUnion(resultIds, _searchIds);
					}
					else if (q.getCriterias().get(i).getOperator().equals("NOT"))
					{
						resultIds = getDifference(resultIds, _searchIds);
					}
					else
					{
						resultIds = _searchIds;
					}
				}
				else
				{
					resultIds = _searchIds;
				}
				_searchIds = null;
			}
			if (monitor != null && monitor.isCanceled())
			{
				break;
			}
		} // end of for-loop.

		resultPersons = new Vector<Person>(resultIds.size());
		_log = new Status(IStatus.INFO, CommonActivator.PLUGIN_ID, "MainSearcher resultIds size: " + resultIds.size());
		iLogger.log(_log);
		if (resultIds.isEmpty())
		{
			return null;
		}
		else
		{
			Person p;
			for (int i = 0; i < resultIds.size(); i++)
			{
				p = _facade.getPerson(resultIds.get(i));
				if (p != null)
				{
					resultPersons.add(p);
				}

			}
		}
		return resultPersons;
	}

	@Override
	public final Vector<ReferenceMods> searchReferences(final PdrQuery q, final IProgressMonitor monitor)
			throws Exception
	{
		boolean wildcards = false;

		Vector<PdrId> resultIds = new Vector<PdrId>();
		Vector<ReferenceMods> resultReferences;
		// remove empty criterias from vector
		// for (Criteria c : q.getCriterias())
		// {
		// if (c.getType().equals("tagging") || c.getType().equals("reference"))
		// {
		// if ((c.getCrit3() != null || c.getCrit3().trim().length() < 0)
		// && (c.getSearchText() == null
		// || c.getSearchText().trim().length() < 0))
		// {
		// System.out.println("criteria deleted, type " + c.getType());
		// System.out.println(". criteria deleted, text " + c.getSearchText());
		//
		// c.setEmpty(true);
		//
		// }
		// }
		// }
		for (int i = 0; i < q.getCriterias().size(); i++)
		{
			if (monitor != null && monitor.isCanceled())
			{
				break;
			}
			if (q.getCriterias().get(i) != null && !q.getCriterias().get(i).isEmpty())
			{
				String query = "declare namespace mods=\"http://www.loc.gov/mods/v3\";\n";

				if (q.getCriterias().get(i) != null
						&& q.getCriterias().get(i).getType() != null
						&& (q.getCriterias().get(i).getType().equals("tagging") || q.getCriterias().get(i).getType()
								.equals("reference")))
				{
					if ((q.getCriterias().get(i).getCrit3() != null && (q.getCriterias().get(i).getCrit3()
							.contains("*") || q.getCriterias().get(i).getCrit3().contains("?")))
							|| (q.getCriterias().get(i).getCrit4() != null && (q.getCriterias().get(i).getCrit4()
									.contains("*") || q.getCriterias().get(i).getCrit4().contains("?")))
							|| (q.getCriterias().get(i).getSearchText() != null && (q.getCriterias().get(i)
									.getSearchText().contains("*") || q.getCriterias().get(i).getSearchText()
									.contains("?"))))
					{
						wildcards = true;
					}
					else
					{
						wildcards = false;
					}
					if (!wildcards)
					{
						if (q.getCriterias().get(i) != null && q.getCriterias().get(i).isFuzzy())
						{
							query += "declare ft-option using fuzzy using stemming;\n";
						}
						else
						{
							query += "declare ft-option using stemming;\n";
						}
					}
					query += "<result>{\n";
					query += "for $x in collection(\"reference\")//mods:mods"; // [.//aodl:semanticStm='Name'
																				// or
																				// .//aodl:semanticStm='Normname']\n"+

					if (q.getCriterias().get(i).getCrit0() != null)
					{
						if (q.getCriterias().get(i).getCrit0().equalsIgnoreCase("ALL"))
						{

						}
						else
						{
							query += "[./mods:genre='" + q.getCriterias().get(i).getCrit0() + "']";
						}
					}
					if (q.getCriterias().get(i).getCrit1() != null
							&& !q.getCriterias().get(i).getCrit1().equalsIgnoreCase("ALL"))
					{
						if (!q.getCriterias().get(i).getCrit1().equalsIgnoreCase("ALL"))
						{
							query += "[.//mods:roleTerm='" + q.getCriterias().get(i).getCrit1() + "'] ";
						}
					}

					if ((q.getCriterias().get(i).getCrit3() != null && q.getCriterias().get(i).getCrit3().trim()
							.length() > 0)
							|| (q.getCriterias().get(i).getCrit4() != null && q.getCriterias().get(i).getCrit4().trim()
									.length() > 0)
							|| (q.getCriterias().get(i).getSearchText() != null && q.getCriterias().get(i)
									.getSearchText().trim().length() > 0))
					{
						query += "\nwhere";
						String whereString = null;
						if (q.getCriterias().get(i).getCrit3() != null
								&& q.getCriterias().get(i).getCrit3().trim().length() > 0)
						{
							whereString = " $x/mods:name//text() contains text ";
							if (q.getCriterias().get(i).getCrit3().contains(" "))
							{
								String[] st = q.getCriterias().get(i).getCrit3().split(" ");
								boolean first = true;
								for (String s : st)
								{
									if (!first)
									{
										query += " and ";
									}
									first = false;
									query += whereString + "('" + parseWildCardsString(s) + "')";
									if (wildcards)
									{
										query += " using wildcards";
									}
								}
							}
							else
							{
								query += whereString + "('" + parseWildCardsString(q.getCriterias().get(i).getCrit3())
										+ "') ";
								if (wildcards)
								{
									query += " using wildcards";
								}
							}
							// query+= "[./name//text() contains text '" +
							// q.getCriterias().get(i).getCrit3().trim() +
							// "'] ";
						}
						if (q.getCriterias().get(i).getCrit4() != null
								&& q.getCriterias().get(i).getCrit4().trim().length() > 0)
						{
							whereString = " $x/mods:titleInfo//text() contains text ";
							if (q.getCriterias().get(i).getCrit4().contains(" "))
							{
								String[] st = q.getCriterias().get(i).getCrit4().split(" ");
								boolean first = true;
								for (String s : st)
								{
									if (!first)
									{
										query += " and ";
									}
									first = false;
									query += whereString + "('" + parseWildCardsString(s) + "')";
									if (wildcards)
									{
										query += " using wildcards";
									}
								}
							}
							else
							{
								query += whereString + "('" + parseWildCardsString(q.getCriterias().get(i).getCrit4())
										+ "') ";
								if (wildcards)
								{
									query += " using wildcards";
								}
							}
							// query+= "[./titleInfo//text() contains text '" +
							// q.getCriterias().get(i).getCrit4().trim() +
							// "'] ";
						}
						if (q.getCriterias().get(i).getSearchText() != null
								&& q.getCriterias().get(i).getSearchText().trim().length() > 0)
						{
							whereString = " $x//text() contains text ";
							if (q.getCriterias().get(i).getSearchText().contains(" "))
							{
								String[] st = q.getCriterias().get(i).getSearchText().split(" ");
								boolean first = true;
								for (String s : st)
								{
									if (!first)
									{
										query += " and ";
									}
									first = false;
									query += whereString + "('" + parseWildCardsString(s) + "')";
									if (wildcards)
									{
										query += " using wildcards";
									}
								}
							}
							else
							{
								query += whereString + "('"
										+ parseWildCardsString(q.getCriterias().get(i).getSearchText()) + "') ";
								if (wildcards)
								{
									query += " using wildcards";
								}
							}
							// query+= "[.//text() contains text '" +
							// q.getCriterias().get(i).getSearchText() + "']";
						}
					}

					query += "\nreturn <r id=\"{$x/@ID}\"></r>\n" + "}</result>";
				}

				_log = new Status(IStatus.INFO, CommonActivator.PLUGIN_ID,
						"MainSearcher searchReferences(final PdrQuery q): " + query);
				iLogger.log(_log);
				XQResultSequence xqs;
				synchronized (_dbCon)
				{
					XQConnection con = _dbCon.getConnection();
					XQPreparedExpression xqp;
					xqp = con.prepareExpression(query);
					xqs = xqp.executeQuery();
					// execute the XQuery Expression

					ComplexIDSaxHandler saxHandler = new ComplexIDSaxHandler(monitor);
					try
					{
						xqs.writeSequenceToSAX(saxHandler);
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
					con.close();
					_dbCon.closeDB("reference");
					_searchIds = (Vector<PdrId>) saxHandler.getResultObject();
				}

				_log = new Status(IStatus.INFO, CommonActivator.PLUGIN_ID, "MainSearcher searchReference executed " + i
						+ ". times.");
				iLogger.log(_log);

				_log = new Status(IStatus.INFO, CommonActivator.PLUGIN_ID, "MainSearcher searchIds size: "
						+ _searchIds.size());
				iLogger.log(_log);
				Collections.sort(_searchIds);
				if (q.getCriterias().size() > 1 && q.getCriterias().get(i).getOperator() != null)
				{
					_log = new Status(IStatus.INFO, CommonActivator.PLUGIN_ID, "MainSearcher operant "
							+ q.getCriterias().get(i).getOperator());
					iLogger.log(_log);
					if (q.getCriterias().get(i).getOperator().equals("AND"))
					{
						resultIds = getInsection(resultIds, _searchIds);
					}
					else if (q.getCriterias().get(i).getOperator().equals("OR"))
					{
						resultIds = getUnion(resultIds, _searchIds);
					}
					else if (q.getCriterias().get(i).getOperator().equals("NOT"))
					{
						resultIds = getDifference(resultIds, _searchIds);
					}
					else
					{
						resultIds = _searchIds;
					}
				}
				else
				{
					resultIds = _searchIds;
				}
				_searchIds = null;
			}
		} // end of for-loop.

		resultReferences = new Vector<ReferenceMods>(resultIds.size());
		_log = new Status(IStatus.INFO, CommonActivator.PLUGIN_ID, "MainSearcher resultIds size: " + resultIds.size());
		iLogger.log(_log);
		for (PdrId id : resultIds)
		{
			if (_facade.getAllReferences().containsKey(id))
			{
				resultReferences.add(_facade.getReference(id));
			}
		}

		return resultReferences;
	}

	@Override
	public final void setAllPersons(final HashMap<PdrId, Person> allPersons)
	{
		this._allPersons = allPersons;
	}

	// public void setResultIds(Vector<String> ids)
	// {
	// this.resultIds = ids;
	//
	// }

	@Override
	public final void setFacets(final ArrayList<String> facets)
	{
		this._facets = facets;
	}

	/**
	 * @param refTemplate the refTemplate to set
	 */
	@Override
	public final void setRefTemplate(final ReferenceMods refTemplate)
	{
		this._refTemplate = refTemplate;
	}

	/**
	 * @param aspects the searchAspect to set
	 */
	@Override
	public final void setSearchAspects(final HashMap<PdrId, Aspect> aspects)
	{
		this._searchAspects = aspects;
	}

	@Override
	public final void setSearchIds(final Vector<PdrId> searchIds)
	{
		this._searchIds = searchIds;
	}

	public void setTempPersons(final HashMap<PdrId, Person> tempPersons)
	{
	}

	@Override
	public PdrObject[] searchAspectsByRelatedObjects(PdrObject[] objects, IProgressMonitor monitor) throws Exception
	{
		if (objects == null || objects.length == 0)
		{
			return null;
		}
		if (monitor != null)
		{
			monitor.worked(5);
		}
		// System.out.println("searchAspectsByRelatedObjects, num of obj " +
		// objects.length);
		String rel = "[";
		for (PdrObject o : objects)
		{
			rel += ".//aodl:relationStm/@subject='" + o.getPdrId().toString() + "' or .//aodl:relation/@object ='"
					+ o.getPdrId().toString() + "' or ";
		}
		rel = rel.substring(0, rel.length() - 5);
		rel += "']\n";
		String query = "declare namespace aodl=\"http://pdr.bbaw.de/namespaces/aodl/\";\n" + "<result>{\n"
				+ "for $a in collection(\"aspect\")//aodl:aspect" + rel
 + "return <a>{$a}</a>\n" + "}</result>\n";

		_log = new Status(IStatus.INFO, CommonActivator.PLUGIN_ID, "MainSearcher searchAspectsByRelatedObject: "
				+ query);
		iLogger.log(_log);
		synchronized (_dbCon)
		{
			XQConnection con = _dbCon.getConnection();
			XQPreparedExpression xqp;
			xqp = con.prepareExpression(query);
			// execute the XQuery Expression
			XQResultSequence xqs = xqp.executeQuery();

			AspectSaxHandler saxHandler = new AspectSaxHandler(objects, monitor);
			try
			{
				xqs.writeSequenceToSAX(saxHandler);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

			con.close();
			_dbCon.closeDB("aspect");
			setObjectsLoaded(objects);

			return objects;
		}

	}

	@Override
	public PdrObject[] searchAspectsByReferences(PdrObject[] objects, IProgressMonitor monitor) throws Exception
	{
		if (objects == null || objects.length == 0)
		{
			return null;
		}
		if (monitor != null)
		{
			monitor.worked(5);
		}
		String rel = "[";
		for (PdrObject o : objects)
		{
			rel += ".//aodl:reference/text()='" + o.getPdrId().toString() + "' or ";
		}
		rel = rel.substring(0, rel.length() - 5);
		rel += "']\n";
		String query = "declare namespace aodl=\"http://pdr.bbaw.de/namespaces/aodl/\";\n" + "<result>{\n"
				+ "for $a in collection(\"aspect\")//aodl:aspect" + rel + "\n" + "return <a>{$a}</a>\n"
				+ "}</result>\n";

		_log = new Status(IStatus.INFO, CommonActivator.PLUGIN_ID, "MainSearcher searchAspectsByReferences: " + query);
		iLogger.log(_log);
		synchronized (_dbCon)
		{
			XQConnection con = _dbCon.getConnection();
			XQPreparedExpression xqp;
			xqp = con.prepareExpression(query);
			// execute the XQuery Expression
			XQResultSequence xqs = xqp.executeQuery();

			AspectSaxHandler saxHandler = new AspectSaxHandler(objects, monitor);
			try
			{
				xqs.writeSequenceToSAX(saxHandler);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

			con.close();
			_dbCon.closeDB("aspect");
			setObjectsLoaded(objects);
			return objects;
		}

	}

	private void setObjectsLoaded(PdrObject[] objects)
	{
		if (objects != null)
		{
			for (PdrObject o : objects)
			{
				o.setAspectsLoaded(true);
			}
		}

	}


}
