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
package org.bbaw.pdr.ae.collections.model;

import java.util.ArrayList;
import java.util.Vector;

import org.bbaw.pdr.ae.metamodel.Record;
import org.bbaw.pdr.ae.model.PdrObject;
import org.bbaw.pdr.ae.model.view.TreeNode;

/** modell object of collections.
 * @author Christoph Plutte
 *
 */
public class PDRCollection
{
	/** name of collection.*/
	private String _name;

	/** vector of treenodes that hold the objects in collection.*/
	private Vector<TreeNode> _objects;

	/** record head of collection.*/
	private Record _record;

	/** geter for name.
	 * @return name.
	 */
	public final String getName()
	{
		return _name;
	}

	/** setter for name.
	 * @param name name of collection.
	 */
	public final void setName(final String name)
	{
		this._name = name;
	}

	/** get vector of treenodes.
	 * @return vector of treenodes with objects.
	 */
	public final Vector<TreeNode> getObjects()
	{
		return _objects;
	}
	/** add item to collection.
	 * @param object treenode to be added.
	 */
	public final void addItem(final TreeNode object)
	{
		if (_objects == null)
		{
			_objects = new Vector<TreeNode>();
		}
		_objects.add(object);
	}

	/** set treenodes vector with objects.
	 * @param objects treenodes that hold the objects.
	 */
	public final void setObjects(final Vector<TreeNode> objects)
	{
		this._objects = objects;
	}
	/** add pdrobject.
	 * @param object pdr object to be added to collection.
	 */
	public final void addPDRObject(final PdrObject object)
	{
		if (object != null && object.getPdrId() != null)
		{
			if (_objects != null && !_objects.isEmpty())
			{
				for (TreeNode t : _objects)
				{
					if (containsObject(t, object))
					{
						return;
					}
				}
			}
			TreeNode tn = new TreeNode(object.getDisplayName(), object);
			addItem(tn);
		}
	}

	/** checks whether collection contains this object.
	 * @param t treenode
	 * @param object pdr object.
	 * @return true if contained else false.
	 */
	private boolean containsObject(final TreeNode t, final PdrObject object)
	{
		if (t.getPdrObject() != null && t.getPdrObject().equals(object))
		{
			return true;
		}
		else if (t.hasChildren())
		{
			for (TreeNode c : _objects)
			{
				if (containsObject(c, object))
				{
					return true;
				}
			}
		}
		return false;
	}

	/** getter for record.
	 * @return record.
	 */
	public final Record getRecord()
	{
		return _record;
	}

	/** setter for record.
	 * @param record record.
	 */
	public final void setRecord(final Record record)
	{
		this._record = record;
	}

	/** remove pdrObject from collection.
	 * @param o pdrObject to be removed.
	 */
	public final void remove(final PdrObject o)
	{
		if (_objects != null && !_objects.isEmpty())
		{
			ArrayList<TreeNode> deleted = new ArrayList<TreeNode>(_objects.size());
			for (TreeNode tn : _objects)
			{
				if (tn.getPdrObject() != null && tn.getPdrObject().equals(o))
				{
					deleted.add(tn);
				}
				else if (tn.hasChildren())
				{
					removePdrObject(tn, o);
				}
			}
			for (TreeNode t : deleted)
			{
				if (_objects.contains(t))
				{
					_objects.remove(t);
				}
			}
		}
	}

	/** private remove pdrObject.
	 * @param tn treenode
	 * @param o object
	 */
	private void removePdrObject(final TreeNode tn, final PdrObject o)
	{
		if (tn.getChildren() != null)
		{
			TreeNode c;
			ArrayList<TreeNode> deleted = new ArrayList<TreeNode>(_objects.size());

			for (int i = 0; i < tn.getChildren().length; i++)
			{
				c = tn.getChildren()[i];
				if (c.getPdrObject() != null && c.getPdrObject().equals(o))
				{
					deleted.add(tn);
				}
				else if (c.hasChildren())
				{
					removePdrObject(c, o);
				}
			}
			for (TreeNode t : deleted)
			{
				if (_objects.contains(t))
				{
					_objects.remove(t);
				}
			}
		}
	}

}
