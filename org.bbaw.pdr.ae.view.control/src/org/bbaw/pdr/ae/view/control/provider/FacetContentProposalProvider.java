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
package org.bbaw.pdr.ae.view.control.provider;

import java.util.ArrayList;

import org.bbaw.pdr.ae.metamodel.PdrId;
import org.bbaw.pdr.ae.model.view.Facet;
import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.jface.fieldassist.IContentProposalProvider;

/**
 * SimpleContentProposalProvider is a class designed to map a static list of
 * Strings to content proposals.
 * @see IContentProposalProvider
 * @since 3.2
 */
public class FacetContentProposalProvider implements IContentProposalProvider
{

	/** The facets. */
	private Facet[] _facets;
	/*
	 * The proposals provided.
	 */

	/**
	 * current reference which must be filtered in as well as other references
	 * that have two levesl of reference relations.
	 */
	private PdrId _currentReferenceId;
	/*
	 * The proposals mapped to IContentProposal. Cached for speed in the case
	 * where filtering is not used.
	 */
	/** The content proposals. */
	private IContentProposal[] _contentProposals;

	/*
	 * Boolean that tracks whether filtering is used.
	 */
	/** The filter proposals. */
	private boolean _filterProposals = true;

	/**
	 * Construct a SimpleContentProposalProvider whose content proposals are
	 * always the specified array of Objects.
	 * @param facets the array of Strings to be returned whenever proposals are
	 *            requested.
	 */
	// public FacetContentProposalProvider(String[] proposals) {
	// super();
	// this.proposals = proposals;
	// }

	public FacetContentProposalProvider(final Facet[] facets)
	{
		super();
		this._facets = facets;
		// proposals = new String[facets.size()];
		// labels = new String[facets.size()];
		//
		// for (int i = 0; i < facets.size(); i++)
		// {
		// System.out.println("facets " + facets.get(i).getValue() + " " +
		// facets.get(i).getType());
		// proposals[i] = facets.get(i).getValue();
		// labels[i] = facets.get(i).getValue() + " " + facets.get(i).getType();
		// }
	}

	/**
	 * Instantiates a new facet content proposal provider.
	 * @param facets the facets
	 * @param pdrId the pdr id
	 */
	public FacetContentProposalProvider(final Facet[] facets, final PdrId pdrId)
	{
		super();
		this._facets = facets;
		this._currentReferenceId = pdrId;

	}

	/**
	 * Return an array of Objects representing the valid content proposals for a
	 * field.
	 * @param contents the current contents of the field (only consulted if
	 *            filtering is set to <code>true</code>)
	 * @param position the current cursor position within the field (ignored)
	 * @return the array of Objects that represent valid proposals for the field
	 *         given its current content.
	 */
	@Override
	@SuppressWarnings(
	{"unchecked", "rawtypes"})
	public final IContentProposal[] getProposals(final String contents, final int position)
	{
		if (_filterProposals)
		{
			ArrayList list = new ArrayList();
			for (int i = 0; i < _facets.length; i++)
			{

				// Filter: liefert Einträge zurück, die mit dem eingegebenen
				// String beginnen.
				// if (facets[i].getContent().length() >= contents.length()
				// && facets[i].getContent().substring(0, contents.length())
				// .equalsIgnoreCase(contents)) {
				// // list.add(new ContentProposal(proposals[i], labels[i]));
				// list.add(facets[i]);

				if (_facets[i] != null && _facets[i].getContent() != null
						&& _facets[i].getContent().length() >= contents.length()
						&& _facets[i].getContent().toLowerCase().contains(contents.toLowerCase()))
				{
					// list.add(new ContentProposal(proposals[i], labels[i]));
					if (_currentReferenceId == null || !_facets[i].getKey().equals(_currentReferenceId.toString()))
					{
						list.add(_facets[i]);
					}

				}
			}
			return (IContentProposal[]) list.toArray(new IContentProposal[list.size()]);
		}
		if (_contentProposals == null)
		{
			_contentProposals = new IContentProposal[_facets.length];
			for (int i = 0; i < _facets.length; i++)
			{
				// contentProposals[i] = new ContentProposal(proposals[i],
				// labels[i], null);
				_contentProposals[i] = _facets[i];

			}
		}
		return _contentProposals;
	}

	/**
	 * Set the Strings to be used as content proposals.
	 * @param facets the array of Strings to be used as proposals.
	 */

	public final void setFacets(final Facet[] facets)
	{
		this._facets = facets;
		_contentProposals = null;

	}

	/**
	 * Set the boolean that controls whether proposals are filtered according to
	 * the current field content.
	 * @param filterProposals <code>true</code> if the proposals should be
	 *            filtered to show only those that match the current contents of
	 *            the field, and <code>false</code> if the proposals should
	 *            remain the same, ignoring the field content.
	 * @since 3.3
	 */
	public final void setFiltering(final boolean filterProposals)
	{
		this._filterProposals = filterProposals;
		// Clear any cached proposals.
		_contentProposals = null;
	}
}
