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
package org.bbaw.pdr.ae.view.main.internal;

import java.util.Vector;

import org.bbaw.pdr.ae.common.AEConstants;
import org.bbaw.pdr.ae.control.facade.Facade;
import org.bbaw.pdr.ae.control.interfaces.AMainSearcher;
import org.bbaw.pdr.ae.model.PdrObject;
import org.bbaw.pdr.ae.model.Person;
import org.bbaw.pdr.ae.model.search.Criteria;
import org.bbaw.pdr.ae.model.search.PdrQuery;
import org.bbaw.pdr.ae.model.view.TreeNode;
import org.bbaw.pdr.ae.view.control.provider.TreeContentProvider;

/**
 * The Class TreeSearchHelper.
 * @author Christoph Plutte
 */
public class TreeSearchHelper
{

	/** The _main searcher. */
	private AMainSearcher _mainSearcher = Facade.getInstanz().getMainSearcher();

	/** The _tc provider. */
	private TreeContentProvider _tcProvider = new TreeContentProvider();

	/**
	 * Builds the query.
	 * @param type the type
	 * @param newBegin the new begin
	 * @return the pdr query
	 */
	private PdrQuery buildQuery(final String type, final String newBegin)
	{
		PdrQuery q = new PdrQuery();
		q.setType(0);
		Criteria c = new Criteria();
		c.setType("tagging");
		c.setCrit0("Name");
		c.setSearchText(newBegin + "*");
		q.getCriterias().add(c);
		return q;
	}

	/**
	 * Gets the tree node alphabet.
	 * @param type the type
	 * @param depth the depth
	 * @return the tree node alphabet
	 */
	public final TreeNode getTreeNodeAlphabet(final String type, final int depth)
	{
		TreeNode root = new TreeNode("root", type);
		root = processNodes(root, "", type, depth, 0);
		return root;

	}

	/**
	 * Process nodes.
	 * @param tn the tn
	 * @param begin the begin
	 * @param type the type
	 * @param depth the depth
	 * @param position the position
	 * @return the tree node
	 */
	private TreeNode processNodes(final TreeNode tn, final String begin, final String type, final int depth,
			final int position)
	{
		TreeNode child;
		String newBegin = begin;
		String chr = null;
		for (int i = 0; i < AEConstants.ALPHABET_EXTENDED.length; i++)
		{
			chr = AEConstants.ALPHABET_EXTENDED[i];
			newBegin = begin + chr;
			child = new TreeNode(newBegin, type);
			if (position < depth)
			{
				child = processNodes(child, newBegin, type, depth, position + 1);
			}
			else
			{
				child.setPdrQuery(buildQuery(type, newBegin));
			}
			tn.addChild(child);
		}
		return tn;
	}

	/**
	 * Process tree node.
	 * @param tn the tn
	 */
	public final void processTreeNode(final TreeNode tn)
	{
		if (tn.getPdrQuery() != null)
		{
			try
			{
				Vector<Person> pers = _mainSearcher.searchPersons(tn.getPdrQuery(), null);
				tn.setPdrQuery(null);

				if (pers != null && !pers.isEmpty())
				{

					Vector<PdrObject> objs = new Vector<PdrObject>(pers.size());
					for (Person p : pers)
					{
						objs.add(p);
					}
					if (tn.getType().equals("facet"))
					{
						_tcProvider.transformToNode(tn, objs, "pdrPo", 0);
					}
					else
					{

						_tcProvider.processLeaves(tn, tn.getId(), 2, objs, tn.getType());
					}
					if (tn.getNumberOfLeaves() <= 0)
					{
						tn.getParent().remove(tn);
					}
				}
				else
				{
					tn.getParent().remove(tn);
				}
			}
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}
