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

import org.bbaw.pdr.ae.config.model.ConfigData;
import org.bbaw.pdr.ae.config.model.ConfigItem;
import org.bbaw.pdr.ae.config.model.DataType;
import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.jface.fieldassist.IContentProposalProvider;

/**
 * The Class MarkupListContentProposalProvider.
 * @author Christoph Plutte
 */
public class MarkupListContentProposalProvider implements IContentProposalProvider
{

	/** The config datas. */
	private ArrayList<ConfigData> _configDatas = new ArrayList<ConfigData>();

	/** The input. */
	private ConfigData _input;
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

	private boolean _showBaseElements;

	/**
	 * Instantiates a new markup list content proposal provider.
	 * @param input the input
	 * @param showBaseElements
	 */
	public MarkupListContentProposalProvider(final ConfigData input, boolean showBaseElements)
	{
		super();
		this._input = input;
		this._showBaseElements = showBaseElements;

	}

	/**
	 * Append children.
	 * @param inputElement the input element
	 */
	private void appendChildren(final ConfigData inputElement)
	{
		if (inputElement instanceof ConfigData)
		{
			ConfigData cd = inputElement;
			if (cd.getChildren() != null)
			{
				for (String k : cd.getChildren().keySet())
				{
					ConfigData child = cd.getChildren().get(k);
					if (child instanceof DataType
							&& !(child.getValue().equals("aodl:relation") || child.getValue()
									.equals("aodl:semanticStm")))
					{
						// System.out.println("child appended " +
						// child.getLabel());
						// _configDatas.add(child);
						appendChildren(child);
					}
					else if (child instanceof ConfigItem)
					{
						ConfigItem ci = (ConfigItem) child;
						if (!ci.isIgnore())
						{
							_configDatas.add(ci);
							// System.out.println("child appended " +
							// ci.getLabel());

							appendChildren(ci);
						}
					}

				}
			}
		}

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

		if (_contentProposals == null)
		{
			appendChildren(_input);
			_contentProposals = _configDatas.toArray(new ConfigData[_configDatas.size()]);
		}
		if (_filterProposals)
		{
			ArrayList list = new ArrayList();
			for (int i = 0; i < _contentProposals.length; i++)
			{

				// Filter: liefert Einträge zurück, die mit dem eingegebenen
				// String beginnen.
				// if (facets[i].getContent().length() >= contents.length()
				// && facets[i].getContent().substring(0, contents.length())
				// .equalsIgnoreCase(contents)) {
				// // list.add(new ContentProposal(proposals[i], labels[i]));
				// list.add(facets[i]);

				if (_contentProposals[i].getContent().length() >= contents.length()
						&& _contentProposals[i].getContent().toLowerCase().contains(contents.toLowerCase()))
				{
					// list.add(new ContentProposal(proposals[i], labels[i]));
					list.add(_contentProposals[i]);

				}
			}
			System.out.println("number of proposals " + list.size());
			return (IContentProposal[]) list.toArray(new IContentProposal[list.size()]);
		}
		// if (_contentProposals == null)
		// {
		// _contentProposals = new IContentProposal[_contentProposals.length];
		// for (int i = 0; i < _contentProposals.length; i++)
		// {
		// // contentProposals[i] = new ContentProposal(proposals[i],
		// // labels[i], null);
		// _contentProposals[i] = _contentProposals[i];
		//
		// }
		// }
		return _contentProposals;
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

	/**
	 * Set the Strings to be used as content proposals.
	 * @param input the ConfigData to be used as proposals.
	 */

	public final void setInput(final ConfigData input)
	{
		this._input = input;
		_contentProposals = null;

	}
}
