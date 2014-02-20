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
package org.bbaw.pdr.ae.control.interfaces;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Vector;

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
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * Class serves as major interface between view and control and thus model. It
 * handels all search request, splits them up into queries executed
 * simultaneously, resolves the intersection, gets data of all objects that meet
 * the criteria and transforms xml to pdrObjects.
 * @author cplutte
 */
public abstract class AMainSearcher
{

	/** stores temporary resulting Persons after search. */
	private HashMap<PdrId, Person> _allPersons;

	/** stores ids of complex searches. */
	private Vector<PdrId> _searchIds;

	/**
	 * Gets the all persons.
	 * @return the all persons
	 */
	public HashMap<PdrId, Person> getAllPersons()
	{
		return _allPersons;
	}

	/**
	 * Gets the complex facets.
	 * @param type the type
	 * @param criteria1 the criteria1
	 * @param criteria2 the criteria2
	 * @param criteria3 the criteria3
	 * @param criteria4 the criteria4
	 * @return the complex facets
	 * @throws Exception the exception
	 */
	public abstract Facet[] getComplexFacets(String type, String criteria1, String criteria2, String criteria3,
			String criteria4) throws Exception;

	/**
	 * Gets the difference.
	 * @param resultIds the result ids
	 * @param searchIds2 the search ids2
	 * @return the difference
	 */
	protected Vector<PdrId> getDifference(Vector<PdrId> resultIds, Vector<PdrId> searchIds2)
	{
		Vector<PdrId> temp = new Vector<PdrId>(resultIds.size());
		int count = 0;
		int j;
		for (int i = 0; i < searchIds2.size(); i++)
		{

			// System.out.println("searchIds2 " + searchIds2.get(i));
			for (j = count; j < resultIds.size(); j++)
			{
				// System.out.println("getDifference: resultIds(" +j+") " +
				// resultIds.get(j));

				if (resultIds.get(j).compareTo(searchIds2.get(i)) < 0)
				{
					temp.add(resultIds.get(j));
					// System.out.println("getDifference: person added " +
					// resultIds.get(j));
				}
				else if (resultIds.get(j).compareTo(searchIds2.get(i)) >= 0)
				{
					j++;
					break;
				}

			}
			count = j;
		}

		return temp;
	}

	/**
	 * Gets the facets.
	 * @param type the type
	 * @param criteria1 the criteria1
	 * @param tListName the t list name
	 * @param sListName the s list name
	 * @param rListName the r list name
	 * @return the facets
	 * @throws Exception the exception
	 */
	public abstract String[] getFacets(String type, String criteria1, String tListName, String sListName,
			String rListName) throws Exception;

	/**
	 * Gets the insection.
	 * @param resultIds the result ids
	 * @param searchIds2 the search ids2
	 * @return the insection
	 */
	protected Vector<PdrId> getInsection(Vector<PdrId> resultIds, Vector<PdrId> searchIds2)
	{

		Vector<PdrId> temp = new Vector<PdrId>(resultIds.size());
		for (int i = 0; i < resultIds.size(); i++)
		{
			if (searchIds2.contains(resultIds.get(i)))
			{
				temp.add(resultIds.get(i));
			}
		}
		return temp;
	}

	/**
	 * Gets the new aspects.
	 * @return the new aspects
	 * @throws Exception the exception
	 */
	public abstract Vector<String> getNewAspects() throws Exception;

	/**
	 * Gets the new persons.
	 * @return the new persons
	 * @throws Exception the exception
	 */
	public abstract Vector<String> getNewPersons() throws Exception;

	/**
	 * Gets the new references.
	 * @return the new references
	 * @throws Exception the exception
	 */
	public abstract Vector<String> getNewReferences() throws Exception;

	/**
	 * Get Number of all persons in DB.
	 * @return number of persons. If exception occurs, it returns -1.
	 * @throws Exception DB-Connection exception.
	 */
	public abstract int getNumberOfAllPersons() throws Exception;

	/**
	 * Gets the object xml.
	 * @param idString the id string
	 * @param col the col
	 * @return the object xml
	 * @throws Exception the exception
	 */
	public abstract String getObjectXML(String idString, String col) throws Exception;

	/**
	 * Gets the person by id.
	 * @param id the id
	 * @return the person by id
	 * @throws Exception the exception
	 */
	public abstract Person getPersonById(PdrId id) throws Exception;

	/**
	 * Gets the reference formate.
	 * @param genre the genre
	 * @return the reference formate
	 * @throws Exception the exception
	 */
	public abstract ReferenceMods getReferenceFormate(String genre) throws Exception;

	//

	/**
	 * Gets the search ids.
	 * @return the search ids
	 */
	public Vector<PdrId> getSearchIds()
	{
		return _searchIds;
	}

	/**
	 * Gets the union.
	 * @param resultIds the result ids
	 * @param searchIds2 the search ids2
	 * @return the union
	 */
	protected Vector<PdrId> getUnion(Vector<PdrId> resultIds, Vector<PdrId> searchIds2)
	{
		Vector<PdrId> temp = new Vector<PdrId>(resultIds.size());
		for (PdrId id : searchIds2)
		{
			if (!resultIds.contains(id))
			{
				resultIds.add(id);
			}
		}

		temp.addAll(resultIds);
		Collections.sort(temp);

		return temp;
	}

	/**
	 * Gets the values.
	 * @param type the type
	 * @param criteria1 the criteria1
	 * @param criteria2 the criteria2
	 * @param criteria3 the criteria3
	 * @param criteria4 the criteria4
	 * @return the values
	 * @throws Exception the exception
	 */
	public abstract String[] getValues(String type, String criteria1, String criteria2, String criteria3,
			String criteria4) throws Exception;


	/**
	 * Parses the facet.
	 * @param facet the facet
	 * @return the string
	 */
	protected final String parseFacet(final String facet)
	{
		char[] result = facet.toCharArray();
		String fixedString = null;
		for (int i = 0; i < result.length; ++i)
		{
			char c = result[i];
			if (c == 0x27)
			{
				result[i] = '"';

			}
		}
		fixedString = new String(result);
		return fixedString;
	}

	/**
	 * Parses the wild cards string.
	 * @param string the string
	 * @return the string
	 */
	protected final String parseWildCardsString(final String string)
	{
		String sText = string;
		if (string.contains("?"))
		{
			if (!string.startsWith("?"))
			{
				sText = string.split("\\?")[0];
				sText = sText + ".?";
			}
			else if (string.length() > 1)
			{
				sText = string.split("\\?")[1];
				sText = "?." + sText;
			}
			else
			{
				sText = string;
			}

		}
		if (string.contains("*"))
		{
			if (!string.startsWith("*"))
			{
				sText = sText.split("\\*")[0];
				sText = sText + ".*";
			}
			else if (string.length() > 1)
			{
				sText = sText.split("\\*")[1];
				sText = "*." + sText;
			}
			else
			{
				sText = string;
			}
		}
		else
		{
			return string;
		}
		return sText;
	}

	/**
	 * Search all persons.
	 * @return the hash map
	 * @throws Exception the exception
	 */
	public abstract HashMap<PdrId, Person> searchAllPersons() throws Exception;

	/**
	 * Search all references.
	 * @return the hash map
	 * @throws Exception the exception
	 */
	public abstract HashMap<PdrId, ReferenceMods> searchAllReferences() throws Exception;

	/**
	 * Search all reference templates.
	 * @return the hash map
	 * @throws Exception the exception
	 */
	public abstract HashMap<String, ReferenceModsTemplate> searchAllReferenceTemplates() throws Exception;

	/**
	 * Search aspect.
	 * @param id the id
	 * @return the aspect
	 * @throws Exception the exception
	 */
	public abstract Aspect searchAspect(PdrId id) throws Exception;

	/**
	 * Search aspect.
	 * @param id the id
	 * @return the aspect
	 * @throws Exception the exception
	 */
	public abstract Aspect searchAspect(String id) throws Exception;

	/**
	 * Search aspects.
	 * @param q the q
	 * @param monitor
	 * @return the hash map
	 * @throws Exception the exception
	 */
	public abstract HashMap<PdrId, Aspect> searchAspects(PdrQuery q, IProgressMonitor monitor) throws Exception;

	/**
	 * Search aspects by reference.
	 * @param pdrObject the pdr object
	 * @param monitor progress monitor if available to report progress.
	 * @return the pdr object
	 * @throws Exception the exception
	 */
	public abstract PdrObject searchAspectsByReference(PdrObject pdrObject, IProgressMonitor monitor) throws Exception;

	/**
	 * Search aspects by related object.
	 * @param o the o
	 * @param monitor progress monitor if available to report progress.
	 * @return the pdr object
	 * @throws Exception the exception
	 */
	public abstract PdrObject searchAspectsByRelatedObject(final PdrObject o, IProgressMonitor monitor)
			throws Exception;

	/**
	 * Search facet aspects.
	 * @param pdrQuery the pdr query
	 * @param monitor progress monitor if available to report progress.
	 * @return the object
	 * @throws Exception
	 */
	public abstract Object searchFacetAspects(PdrQuery pdrQuery, IProgressMonitor monitor) throws Exception;

	/**
	 * Search facet persons.
	 * @param pdrQuery the pdr query
	 * @param monitor progress monitor to report progress if available
	 * @return the vector
	 * @throws Exception
	 */
	public Vector<FacetResultNode> searchFacetPersons(final PdrQuery pdrQuery, IProgressMonitor monitor)
			throws Exception
	{
		if (pdrQuery.getFacets() != null)
		{
			Vector<FacetResultNode> facetNodes = new Vector<FacetResultNode>();
			FacetResultNode facetNode;
			Criteria criteria = new Criteria();
			criteria.setType("tagging");
			criteria.setOperator("AND");
			pdrQuery.getCriterias().add(criteria);
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
				try
				{
					facetNode.setObjects(searchPersons(pdrQuery, monitor));
					if (facetNode.getObjects() != null)
					{
					}

				}
				catch (Exception e)
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

	/**
	 * Search facet references.
	 * @param pdrQuery the pdr query
	 * @return the object
	 * @throws Exception
	 */
	public Object searchFacetReferences(final PdrQuery pdrQuery, final IProgressMonitor monitor) throws Exception
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
					Vector<ReferenceMods> results = searchReferences(pdrQuery, null);
					if (results != null && !results.isEmpty())
					{
						for (ReferenceMods r : results)
						{
							refs.add(r);
						}
						facetNode.setObjects(refs);
						if (facetNode.getObjects() != null)
						{

						}
					}

				}
				catch (Exception e)
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

	/**
	 * Search object string.
	 * @param col the col
	 * @param name the name
	 * @return the string
	 * @throws Exception the exception
	 */
	public abstract String searchObjectString(String col, String name) throws Exception;

	/**
	 * Search persons.
	 * @param q the q
	 * @param monitor
	 * @return the vector
	 * @throws Exception the exception
	 */
	public abstract Vector<Person> searchPersons(PdrQuery q, IProgressMonitor monitor) throws Exception;

	/**
	 * Search references.
	 * @param q the q
	 * @param monitor
	 * @return the vector
	 * @throws Exception the exception
	 */
	public abstract Vector<ReferenceMods> searchReferences(PdrQuery q, IProgressMonitor monitor) throws Exception;

	/**
	 * Sets the all persons.
	 * @param allPersons the all persons
	 */
	public void setAllPersons(final HashMap<PdrId, Person> allPersons)
	{
		this._allPersons = allPersons;
	}

	/**
	 * Sets the complex facets.
	 * @param complexFacets the new complex facets
	 */
	public void setComplexFacets(final Vector<Facet> complexFacets)
	{
	}

	/**
	 * Sets the facets.
	 * @param facets the new facets
	 */
	public void setFacets(final ArrayList<String> facets)
	{
	}

	/**
	 * @param refTemplate the refTemplate to set
	 */
	public void setRefTemplate(final ReferenceMods refTemplate)
	{
	}

	/**
	 * @param aspects the searchAspect to set
	 */
	public void setSearchAspects(final HashMap<PdrId, Aspect> aspects)
	{
	}

	/**
	 * Sets the search ids.
	 * @param searchIds the new search ids
	 */
	public void setSearchIds(final Vector<PdrId> searchIds)
	{
		this._searchIds = searchIds;
	}

	public abstract PdrObject[] searchAspectsByRelatedObjects(PdrObject[] objects, IProgressMonitor monitor)
			throws Exception;

	public abstract PdrObject[] searchAspectsByReferences(PdrObject[] objects, IProgressMonitor monitor)
			throws Exception;

}
