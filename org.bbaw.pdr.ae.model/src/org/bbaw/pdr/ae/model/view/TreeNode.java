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
package org.bbaw.pdr.ae.model.view;

import java.util.ArrayList;
import java.util.List;

import org.bbaw.pdr.ae.model.PdrObject;
import org.bbaw.pdr.ae.model.search.PdrQuery;

/**
 * The Class TreeNode.
 * @author Christoph Plutte
 */
public class TreeNode
{

	/** The id. */
	private String _id;

	/** The value. */
	private int _value;

	/** The pdr object. */
	private PdrObject _pdrObject;

	/** The children. */
	private List<TreeNode> _children;

	/** The parent. */
	private TreeNode _parent;

	/** The type. */
	private String _type;

	/** The nameless. */
	private boolean _nameless;

	/** The updated. */
	private boolean _updated;

	/** The is new. */
	private boolean _isNew;

	/** The number of leaves. */
	private int _numberOfLeaves;

	/** The pdr query. */
	private PdrQuery _pdrQuery;

	/**
	 * Instantiates a new tree node.
	 * @param id the id
	 * @param pdrObject the pdr object
	 */
	public TreeNode(final String id, final PdrObject pdrObject)
	{
		this._id = id;
		this._pdrObject = pdrObject;
		this._value = id.hashCode();
		this._children = new ArrayList<TreeNode>();
		this.setNameless(false);
		this._type = pdrObject.getPdrId().getType();

	}

	/**
	 * Instantiates a new tree node.
	 * @param id the id
	 * @param type the type
	 */
	public TreeNode(final String id, final String type)
	{
		this._id = id;
		this._pdrObject = null;
		this._value = id.hashCode();
		this._children = new ArrayList<TreeNode>();
		this.setNameless(false);
		this._type = type;
	}

	/**
	 * Adds the child.
	 * @param node the node
	 * @return the tree node
	 */
	public final TreeNode addChild(final TreeNode node)
	{
		if (this._children == null)
		{
			this._children = new ArrayList<TreeNode>();
		}
		this._children.add(node);
		node._parent = this;
		return node;
	}

	@Override
	public final boolean equals(final Object obj)
	{
		if (obj != null && obj instanceof TreeNode)
		{
			TreeNode tn = (TreeNode) obj;
			if (this.getPdrObject() != null && tn.getPdrObject() != null)
			{
				if (this.getPdrObject().equals(tn.getPdrObject()))
				{
					if (this.getParent() != null && tn.getParent() != null)
					{
						if (this.getParent().equals(tn.getParent()))
						{
							return true;
						}
						else
						{
							return false;
						}
					}
					else if (this.getParent() != null)
					{
						return false;
					}
					else if (tn.getParent() != null)
					{
						return false;
					}
					else
					{
						return true;
					}
				}
				else
				{
					return false;
				}
			}
			else if (this.getId() != null && tn.getId() != null)
			{
				if (this.getId().equals(tn.getId()))
				{
					if (this.getParent() != null && tn.getParent() != null)
					{
						if (this.getParent().equals(tn.getParent()))
						{
							return true;
						}
						else
						{
							return false;
						}
					}
					else if (this.getParent() != null)
					{
						return false;
					}
					else if (tn.getParent() != null)
					{
						return false;
					}
					else
					{
						return true;
					}
				}
			}
			else if (this.equals(tn))
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		return false;
	}

	/**
	 * Gets the child.
	 * @param name the name
	 * @return the child
	 */
	public final TreeNode getChild(final String name)
	{
		for (TreeNode tn : this._children)
		{
			if (tn.getId().equals(name))
			{
				return tn;
			}
		}
		return null;
	}

	/**
	 * Gets the children.
	 * @return the children
	 */
	public final TreeNode[] getChildren()
	{
		if (_children != null)
		{
			return this._children.toArray(new TreeNode[_children.size()]);
		}
		else
		{
			return null;
		}
	}

	/**
	 * @return the id
	 */
	public final String getId()
	{
		return this._id;
	}

	/**
	 * Gets the number of leaves.
	 * @return the number of leaves
	 */
	public final int getNumberOfLeaves()
	{
		return _numberOfLeaves;
	}

	/**
	 * get parent node.
	 * @return parent node.
	 */
	public final TreeNode getParent()
	{
		return this._parent;
	}

	/**
	 * Gets the pdr object.
	 * @return the pdr object
	 */
	public final PdrObject getPdrObject()
	{
		return _pdrObject;
	}

	/**
	 * Gets the pdr query.
	 * @return the pdr query
	 */
	public PdrQuery getPdrQuery()
	{
		return _pdrQuery;
	}

	/**
	 * Gets the type.
	 * @return the type
	 */
	public final String getType()
	{
		return _type;
	}

	/**
	 * @return the value
	 */
	public final int getValue()
	{
		return this._value;
	}

	/**
	 * has Children. true if getChildren not null or if getPdrQuery not null and
	 * children have to be searched.
	 * @return boolean hasChildren.
	 */
	public final boolean hasChildren()
	{
		if (this._children != null && !this._children.isEmpty())
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * Checks if is nameless.
	 * @return true, if is nameless
	 */
	public final boolean isNameless()
	{
		return _nameless;
	}

	/**
	 * Checks if is new.
	 * @return true, if is new
	 */
	public final boolean isNew()
	{
		return _isNew;
	}

	/**
	 * Checks if is updated.
	 * @return true, if is updated
	 */
	public final boolean isUpdated()
	{
		return _updated;
	}

	/**
	 * Removes the.
	 * @param child the child
	 */
	public final void remove(final TreeNode child)
	{
		if (!this.hasChildren())
		{
			return;
		}
		else
		{
			_children.remove(child);
			return;
		}
	}

	/**
	 * Removes the node.
	 * @param index the index
	 */
	public final void removeNode(final int index)
	{
		if (!this.hasChildren())
		{
			return;
		}
		else
		{
			_children.remove(index);

			return;
		}
	}

	/**
	 * Sets the children.
	 * @param children the new children
	 */
	public final void setChildren(final List<TreeNode> children)
	{
		this._children = children;
	}

	/**
	 * @param id the id to set
	 */
	public final void setId(final String id)
	{
		this._id = id;
	}

	/**
	 * Sets the nameless.
	 * @param nameless the new nameless
	 */
	public final void setNameless(final boolean nameless)
	{
		this._nameless = nameless;
	}

	/**
	 * Sets the new.
	 * @param isNew the new new
	 */
	public final void setNew(final boolean isNew)
	{
		this._isNew = isNew;
	}

	/**
	 * Sets the number of leaves.
	 * @param numberOfLeaves the new number of leaves
	 */
	public final void setNumberOfLeaves(final int numberOfLeaves)
	{
		this._numberOfLeaves = numberOfLeaves;
	}

	/**
	 * Sets the pdr object.
	 * @param pdrObject the new pdr object
	 */
	public final void setPdrObject(final PdrObject pdrObject)
	{
		this._pdrObject = pdrObject;
	}

	/**
	 * Sets the pdr query.
	 * @param pdrQuery the new pdr query
	 */
	public void setPdrQuery(final PdrQuery pdrQuery)
	{
		this._pdrQuery = pdrQuery;
	}

	/**
	 * Sets the type.
	 * @param type the new type
	 */
	public final void setType(final String type)
	{
		this._type = type;
	}

	/**
	 * Sets the updated.
	 * @param updated the new updated
	 */
	public final void setUpdated(final boolean updated)
	{
		this._updated = updated;
	}

	/**
	 * @param value the value to set
	 */
	public final void setValue(final int value)
	{
		this._value = value;
	}

	@Override
	public final String toString()
	{
		return this._id + " (leaves " + _numberOfLeaves + " " + this._type + ")"; // this.value;
	}
}
