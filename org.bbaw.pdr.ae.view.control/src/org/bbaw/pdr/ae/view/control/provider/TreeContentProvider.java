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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.bbaw.pdr.ae.common.AEConstants;
import org.bbaw.pdr.ae.common.CommonActivator;
import org.bbaw.pdr.ae.common.NLMessages;
import org.bbaw.pdr.ae.control.facade.Facade;
import org.bbaw.pdr.ae.metamodel.PdrId;
import org.bbaw.pdr.ae.model.Aspect;
import org.bbaw.pdr.ae.model.Concurrence;
import org.bbaw.pdr.ae.model.PdrObject;
import org.bbaw.pdr.ae.model.Person;
import org.bbaw.pdr.ae.model.ReferenceMods;
import org.bbaw.pdr.ae.model.view.FacetResultNode;
import org.bbaw.pdr.ae.model.view.TreeNode;
import org.bbaw.pdr.ae.view.control.comparator.PdrObjectsByDisplayNameComparator;
import org.bbaw.pdr.ae.view.control.internal.Activator;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * @author cplutte Class provides the Content for the TreeViewer. TODO has to be
 *         adapted to dynamic Data.
 */
public class TreeContentProvider implements ITreeContentProvider
{

	/** Logger. */
	private static ILog iLogger = AEConstants.ILOGGER;
	/** status. */
	private IStatus _log;
	/** _facade singleton instance. */
	private Facade _facade = Facade.getInstanz();

	/** The _type. */
	private String _type;

	/** The _index. */
	private int _index;

	/** The _objects. */
	private Vector<PdrObject> _objects;

	/** The _ref by host only. */
	private boolean _refByHostOnly = false;

	/** The _is reference. */
	private boolean _isReference = false;

	/** The _max_depth. */
	private int _maxdepth = 3;

	/** The _ ma x_ leaves. */
	private static final int MAXLEAVES = 30;

	/** The _ fir s_ character. */
	private static final String FIRSCHARACTER = AEConstants.ALPHABET_EXTENDED[0];

	/** The _ las t_ character. */
	private static final String LAST_CHARACTER = AEConstants.ALPHABET_EXTENDED[AEConstants.ALPHABET_EXTENDED.length - 1];

	/**
	 * Adds the hosted references.
	 * @param host the host
	 * @param hostTN the host tn
	 * @param recursionLevel the recursion level
	 */
	private void addHostedReferences(final ReferenceMods host, final TreeNode hostTN, final int recursionLevel)
	{
		if (host.getHostedReferences() != null)
		{
			for (String id : host.getHostedReferences())
			{
				ReferenceMods hosted = _facade.getReference(new PdrId(id));
				TreeNode hostedTN = null;
				if (hosted != null)
				{
					hostedTN = new TreeNode(hosted.getDisplayName(), hosted);
					hostTN.addChild(hostedTN);
					if (recursionLevel <= 2 && hosted.getHostedReferences() != null
							&& hosted.getHostedReferences().size() > 0)
					{
						addHostedReferences(hosted, hostedTN, recursionLevel + 1);
					}
				}
				try
				{
					checkUpdateState(hosted, hostedTN);
					processUpdateState(hostTN, hostedTN);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * Check update state.
	 * @param o the o
	 * @param objTn the obj tn
	 * @throws Exception the exception
	 */
	private void checkUpdateState(final PdrObject o, final TreeNode objTn) throws Exception
	{
		if (o instanceof Person)
		{
			if (_facade.getPersonsUpdateState().containsKey(o.getPdrId().toString()))
			{
				if (_facade.getPersonsUpdateState().get(o.getPdrId().toString()) == 1)
				{
					objTn.setUpdated(true);
				}
				else if (o.getPdrId().getId() > 99999999)
				{
					objTn.setNew(true);
				}
			}
		}
		if (o instanceof Aspect)
		{
			if (_facade.getAspectsUpdateState().containsKey(o.getPdrId().toString()))
			{
				if (_facade.getAspectsUpdateState().get(o.getPdrId().toString()) == 1)
				{
					objTn.setUpdated(true);
				}
				else if (o.getPdrId().getId() > 99999999)
				{
					objTn.setNew(true);
				}
			}
		}
		if (o instanceof ReferenceMods)
		{
			if (_facade.getReferencesUpdateState().containsKey(o.getPdrId().toString()))
			{
				if (_facade.getReferencesUpdateState().get(o.getPdrId().toString()) == 1)
				{
					objTn.setUpdated(true);
				}
				else if (o.getPdrId().getId() > 99999999)
				{
					objTn.setNew(true);
				}
			}
		}
	}

	/**
	 * Creates the object node.
	 * @param o the o
	 * @param tn the tn
	 * @param sameNameTN the same name tn
	 * @return the tree node
	 */
	private TreeNode createObjectNode(final PdrObject o, final TreeNode tn, final boolean sameNameTN)
	{

		// create new treenode and add object.
		TreeNode objTn;
		objTn = new TreeNode(o.getDisplayName(), o);
		if (_isReference && o instanceof ReferenceMods)
		{

			ReferenceMods r = (ReferenceMods) o;
			if (!_refByHostOnly || !r.isHosted())
			{
				tn.addChild(objTn);
			}
			if (r.getHostedReferences() != null && !r.getHostedReferences().isEmpty())
			{
				addHostedReferences(r, objTn, 0);
			}
		}
		else
		{
			tn.addChild(objTn);
		}
		try
		{
			checkUpdateState(o, objTn);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		processUpdateState(objTn, tn);

		// if treeNode is node which has an object of the same name.
		if (sameNameTN)
		{
			if (tn.getPdrObject() != null)
			{
				TreeNode tn2 = new TreeNode(tn.getPdrObject().getDisplayName(), tn.getPdrObject());
				processUpdateState(tn, tn2);
				tn.addChild(tn2);
				tn.setPdrObject(null);
			}

			tn.setNumberOfLeaves(tn.getChildren().length);
			return tn;
		}
		return objTn;
	}

	/**
	 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
	 */
	@Override
	public void dispose()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public final Object[] getChildren(final Object parentElement)
	{
		return ((TreeNode) parentElement).getChildren();
	}

	@SuppressWarnings("unchecked")
	@Override
	public final Object[] getElements(final Object inputElement)
	{
		// System.out.println("getElements " + _index);
		_refByHostOnly = Platform.getPreferencesService().getBoolean(CommonActivator.PLUGIN_ID,
				"REFERENCE_TREE_ONLY_BYHOST", false, null); // Activator.getDefault().getPreferenceStore().getBoolean("REFERENCE_TREE_ONLY_BYHOST");
		TreeNode root = null;
		if (inputElement instanceof Vector<?>)
		{
			if (((Vector<?>) inputElement) != null && !((Vector<?>) inputElement).isEmpty()
					&& ((Vector<?>) inputElement).firstElement() != null
					&& ((Vector<?>) inputElement).firstElement() instanceof PdrObject)
			{
				_objects = (Vector<PdrObject>) inputElement;
				Collections.sort(_objects, new PdrObjectsByDisplayNameComparator());
				_isReference = false;
				if (((Vector<?>) inputElement).firstElement() instanceof Aspect)
				{
					_type = "pdrAo";
					System.out.println("TreeContentProvider type: pdrAo");

				}
				else if (((Vector<?>) inputElement).firstElement() instanceof Person)
				{
					_type = "pdrPo";
				}
				else if (((Vector<?>) inputElement).firstElement() instanceof ReferenceMods)
				{
					_type = "pdrRo";
					_isReference = true;
				}
				else
				{
					_type = "error";
				}

				root = new TreeNode("root", _type); //$NON-NLS-1$ //$NON-NLS-2$
				root = transformToNode(root, _objects, _type, 1);

			}
			else if (((Vector<?>) inputElement) != null && !((Vector<?>) inputElement).isEmpty()
					&& ((Vector<?>) inputElement).firstElement() instanceof FacetResultNode)
			{
				Vector<FacetResultNode> facetNodes = (Vector<FacetResultNode>) inputElement;

				root = new TreeNode("root", "facet"); //$NON-NLS-1$ //$NON-NLS-2$
				for (FacetResultNode facetNode : facetNodes)
				{
					// _log = new Status(IStatus.INFO, Activator.PLUGIN_ID,
					//							"TreeContentProvider facet " + facetNode.getId()); //$NON-NLS-1$
					// iLogger.log(_log);
					if (facetNode.getObjects() != null && !facetNode.getObjects().isEmpty())
					{
						_objects = facetNode.getObjects();
						Collections.sort(_objects, new PdrObjectsByDisplayNameComparator());
						_isReference = false;

						if (((Vector<?>) _objects).firstElement() instanceof Aspect)
						{
							_type = "pdrAo";
							System.out.println("TreeContentProvider type: pdrAo");

						}
						else if (((Vector<?>) _objects).firstElement() instanceof Person)
						{
							_type = "pdrPo";
						}
						else if (((Vector<?>) _objects).firstElement() instanceof ReferenceMods)
						{
							_type = "pdrRo";
							_isReference = true;
						}
						else
						{
							_type = "error";
						}
						facetNode = (FacetResultNode) transformToNode(facetNode, _objects, _type, 1);
						if (facetNode.getNumberOfLeaves() > 0)
						{
							root.addChild(facetNode);
						}
					}
					else
					{
						root.addChild(facetNode);
					}
				}

			}

			else
			{
				root = new TreeNode("root", "noResult"); //$NON-NLS-1$ //$NON-NLS-2$
				TreeNode tnId = new TreeNode(NLMessages.getString("ViewProvider_no_result"), "noResult"); //$NON-NLS-2$
				root.addChild(tnId);
				_log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "TreeContentProvider no result for tree"); //$NON-NLS-1$
				iLogger.log(_log);
			}
			_log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "TreeContentProvider transformed to nodes"); //$NON-NLS-1$
			iLogger.log(_log);

		}
		else if (inputElement instanceof HashMap<?, ?>)
		{
			if (((HashMap<?, ?>) inputElement) != null && !((HashMap<?, ?>) inputElement).isEmpty())
			{
				_log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "TreeContentProvider input hashmap"); //$NON-NLS-1$
				iLogger.log(_log);
				HashMap<PdrId, PdrObject> map = (HashMap<PdrId, PdrObject>) inputElement;
				for (PdrId key : map.keySet())
				{
					if (map.get(key) instanceof PdrObject)
					{
						if (map.get(key) instanceof Aspect)
						{
							_type = "pdrAo";
							System.out.println("TreeContentProvider type: pdrAo");
						}
						else if (map.get(key) instanceof Person)
						{
							_type = "pdrPo";
						}
						else if (map.get(key) instanceof ReferenceMods)
						{
							_type = "pdrRo";
							_isReference = true;

						}
						else
						{
							_type = "error";
						}

						_objects = new Vector<PdrObject>(map.size());

						for (PdrId k : map.keySet())
						{
							_objects.add(map.get(k));
						}
						Collections.sort(_objects, new PdrObjectsByDisplayNameComparator());

						root = new TreeNode("root", _type); //$NON-NLS-1$ //$NON-NLS-2$

						root = transformToNode(root, _objects, _type, 1);
						break;
					}

				}

			}
		}
		else if (inputElement instanceof TreeNode)
		{
			return ((TreeNode) inputElement).getChildren();
		}
		else
		{
			root = new TreeNode("root", "noResult"); //$NON-NLS-1$ //$NON-NLS-2$
			TreeNode tnId = new TreeNode(NLMessages.getString("ViewProvider_no_result"), "noResult"); //$NON-NLS-2$
			root.addChild(tnId);
			_log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "TreeContentProvider No Result"); //$NON-NLS-1$
			iLogger.log(_log);
		}
		if (root == null || root.getChildren() == null)
		{
			root = new TreeNode("root", "noResult"); //$NON-NLS-1$ //$NON-NLS-2$
			TreeNode tnId = new TreeNode(NLMessages.getString("ViewProvider_not_result"), "noResult"); //$NON-NLS-2$
			root.addChild(tnId);
			_log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "TreeContentProvider No Result"); //$NON-NLS-1$
			iLogger.log(_log);
		}

		return root.getChildren();

	}

	@Override
	public final Object getParent(final Object element)
	{
		return ((TreeNode) element).getParent();
	}

	@Override
	public final boolean hasChildren(final Object element)
	{
		TreeNode tn = (TreeNode) element;
		if (tn.hasChildren())
		{
			return true;
		}
		else if (tn.getPdrQuery() != null)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	@Override
	public void inputChanged(final Viewer arg0, final Object arg1, final Object arg2)
	{
		// TODO Auto-generated method stub

	}

	/**
	 * Optimize.
	 * @param tn the tn
	 */
	private void optimize(final TreeNode tn)
	{
		if (tn.getNumberOfLeaves() == 0)
		{
			tn.setChildren(null);
		}
		else if (tn.getNumberOfLeaves() < MAXLEAVES)
		{
			if (tn.hasChildren() && tn.getChildren() != null)
			{
				List<TreeNode> leeves = new ArrayList<TreeNode>(tn.getNumberOfLeaves());
				for (TreeNode child : tn.getChildren())
				{
					if (child.getChildren() != null)
					{
						for (TreeNode leef : child.getChildren())
						{
							leeves.add(leef);
						}
					}

				}
				tn.setChildren(leeves);
			}
		}

	}

	/**
	 * Process end.
	 * @param str the str
	 * @return the string
	 */
	private String processEnd(String str)
	{
		String c;
		if (str.length() < _maxdepth - 1)
		{
			while (str.length() <= _maxdepth)
			{
				str = str + FIRSCHARACTER;
			}
			return str;
		}
		else if (str.endsWith(LAST_CHARACTER))
		{
			while (str.length() > 0 && str.endsWith(LAST_CHARACTER))
			{
				str = str.substring(0, str.length() - 1);
			}
			if (str.length() > 0)
			{
				c = str.substring(str.length() - 1, str.length());
				for (int i = 0; i < AEConstants.ALPHABET_EXTENDED.length - 1; i++)
				{
					if (AEConstants.ALPHABET_EXTENDED[i].equalsIgnoreCase(c))
					{
						c = AEConstants.ALPHABET_EXTENDED[i + 1];
						break;
					}
				}
				str = str.substring(0, str.length() - 1) + c;
			}
			else
			{
				return null;
			}
		}
		else
		{
			if (str.length() > 0)
			{
				c = str.substring(str.length() - 1, str.length());
				for (int i = 0; i < AEConstants.ALPHABET_EXTENDED.length - 1; i++)
				{
					if (AEConstants.ALPHABET_EXTENDED[i].equalsIgnoreCase(c))
					{
						c = AEConstants.ALPHABET_EXTENDED[i + 1];
						break;
					}
				}
				str = str + c;
			}
		}
		return str;
	}

	/**
	 * Process leaves.
	 * @param tn the tn
	 * @param begin the begin
	 * @param position the position
	 * @param objects the objects
	 * @param type the type
	 * @return the tree node
	 */
	public final TreeNode processLeaves(final TreeNode tn, final String begin, final int position,
			final Vector<PdrObject> objects, final String type)
	{
		int count = 0;
		PdrObject o;
		TreeNode lastNode = null;
		String lastName = null;
		if (begin.equals("?"))
		{
			while (_index < objects.size() && objects.get(_index).getDisplayName().compareToIgnoreCase("a") < 0) //$NON-NLS-1$
			{
				o = objects.get(_index);
				if (o.getDisplayName().equals(lastName))
				{
					lastNode = createObjectNode(o, lastNode, true);
				}
				else
				{
					lastNode = createObjectNode(o, tn, false);
				}
				if (type.equals("pdrPo") && o instanceof Person)
				{
					processConcurrences(o, lastNode);
				}
				lastName = o.getDisplayName();

				count++;
				_index++;
			}
		}
		else if (begin.endsWith(LAST_CHARACTER))
		{
			String end = processEnd(begin);
			if (end != null)
			{
				while (_index < objects.size() && objects.get(_index).getDisplayName().compareToIgnoreCase(end) < 0) //$NON-NLS-1$
				{
					o = objects.get(_index);
					if (o.getDisplayName().equals(lastName))
					{
						lastNode = createObjectNode(o, lastNode, true);
					}
					else
					{
						lastNode = createObjectNode(o, tn, false);
					}
					if (type.equals("pdrPo") && o instanceof Person)
					{
						processConcurrences(o, lastNode);
					}
					lastName = o.getDisplayName();
					count++;
					_index++;
				}
			}
			else
			{
				while (_index < objects.size()
						&& objects.get(_index).getDisplayName().compareToIgnoreCase(LAST_CHARACTER) > 0) //$NON-NLS-1$
				{
					o = objects.get(_index);
					if (o.getDisplayName().equals(lastName))
					{
						lastNode = createObjectNode(o, lastNode, true);
					}
					else
					{
						lastNode = createObjectNode(o, tn, false);
					}
					if (type.equals("pdrPo") && o instanceof Person)
					{
						processConcurrences(o, lastNode);
					}
					lastName = o.getDisplayName();
					count++;
					_index++;
				}
			}
		}
		else
		{
			while (_index < objects.size() && objects.get(_index).getDisplayName().toLowerCase().startsWith(begin)) //$NON-NLS-1$
			{
				o = objects.get(_index);
				if (o.getDisplayName().equals(lastName))
				{
					lastNode = createObjectNode(o, lastNode, true);
				}
				else
				{
					lastNode = createObjectNode(o, tn, false);
				}
				if (type.equals("pdrPo") && o instanceof Person)
				{
					processConcurrences(o, lastNode);
				}
				lastName = o.getDisplayName();
				count++;
				_index++;
			}
		}
		tn.setNumberOfLeaves(count);
		return tn;
	}

	private void processConcurrences(PdrObject o, TreeNode lastNode)
	{
		Person mainPerson = (Person) o;
		if (mainPerson.getConcurrences() != null && mainPerson.getConcurrences().getConcurrences() != null)
		{
			for (Concurrence c : mainPerson.getConcurrences().getConcurrences())
			{
				if (c.getPersonId() != null)
				{
					Person concurringPerson = _facade.getPerson(c.getPersonId());
					if (concurringPerson != null)
					{
						createObjectNode(concurringPerson, lastNode, false);

					}
				}
			}
			lastNode.setNumberOfLeaves(lastNode.getChildren().length);

		}

	}

	/**
	 * Process nodes.
	 * @param tn the tn
	 * @param objects the objects
	 * @param begin the begin
	 * @param position the position
	 * @param type the type
	 * @return the tree node
	 */
	private TreeNode processNodes(final TreeNode tn, final Vector<PdrObject> objects, final String begin,
			final int position, final String type)
	{
		TreeNode child;
		String newBegin = begin;
		int count = 0;
		String chr = null;

		for (int i = 0; i < AEConstants.ALPHABET_EXTENDED.length; i++)
		{
			chr = AEConstants.ALPHABET_EXTENDED[i];
			newBegin = begin + chr;
			child = new TreeNode(newBegin, type);
			if (position < _maxdepth)
			{
				child = processNodes(child, objects, newBegin, position + 1, type);
			}
			else
			{
				child = processLeaves(child, newBegin, position, objects, type);
			}
			if (child.getNumberOfLeaves() > 0)
			{
				tn.addChild(child);
				processUpdateState(child, tn);
			}
			count = count + child.getNumberOfLeaves();
		}

		tn.setNumberOfLeaves(count);
		optimize(tn);
		return tn;

	}

	/**
	 * Process update state.
	 * @param objTn the obj tn
	 * @param tn the tn
	 */
	private void processUpdateState(final TreeNode objTn, final TreeNode tn)
	{
		if (objTn.isUpdated())
		{
			tn.setUpdated(true);
		}
		if (objTn.isNew())
		{
			tn.setNew(true);
		}

	}

	/**
	 * Transform to node.
	 * @param root the root
	 * @param objects the objects
	 * @param type the type
	 * @param position the position
	 * @return the tree node
	 */
	public TreeNode transformToNode(TreeNode root, final Vector<PdrObject> objects, final String type,
			final int position)
	{
		_index = 0;

		if (objects.size() < 30)
		{
			_maxdepth = 1;
		}
		else if (objects.size() < 500)
		{
			_maxdepth = 2;
		}
		else if (objects.size() < 10000)
		{
			_maxdepth = 3;
		}
		else
		{
			_maxdepth = 4;
		}

		String begin;

		TreeNode child;
		for (int j = 0; j < 10; j++)
		{
			begin = new Integer(j).toString();
			child = new TreeNode(begin, type);
			child = processLeaves(child, begin, position, objects, type);
			if (child.getNumberOfLeaves() > 0)
			{
				root.addChild(child);
			}
		}
		begin = "?";
		child = new TreeNode(begin, type);
		child = processLeaves(child, begin, position, objects, type);
		if (child.getNumberOfLeaves() > 0)
		{
			root.addChild(child);
		}

		begin = "";

		root = processNodes(root, objects, begin, position, type);

		return root;
	}
}
