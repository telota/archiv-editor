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
package org.bbaw.pdr.ae.config.model;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class ConfigTreeNode is used to wrap ConfigData objects for tree viewer.
 * @author Christoph Plutte
 */
public class ConfigTreeNode
{

	/** The value. */
	private String _value;

	/** The label. */
	private String _label;

	/** The selected. */
	private boolean _selected;
	/** The config data. */
	private ConfigData _configData;

	/** The children. */
	private List<ConfigTreeNode> _children = new ArrayList<ConfigTreeNode>();

	/** The parent. */
	private ConfigTreeNode _parent;

	/**
	 * Instantiates a new config tree node.
	 * @param value the value
	 * @param label the label
	 * @param configData the config data
	 */
	public ConfigTreeNode(final String value, final String label, final ConfigData configData)
	{
		this._value = value;
		this._label = label;
		this._configData = configData;
	}

	/**
	 * Adds the child.
	 * @param node the node
	 * @return the config tree node
	 */
	public final ConfigTreeNode addChild(final ConfigTreeNode node)
	{
		this._children.add(node);
		node._parent = this;
		return node;
	}

	/**
	 * @param o treenode
	 * @return true if nodes equal one another.
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public final boolean equals(final Object o)
	{
		if (o instanceof ConfigTreeNode)
		{
			ConfigTreeNode tn = (ConfigTreeNode) o;
			if (this.getConfigData() != null && tn.getConfigData() != null)
			{
				if (this.getConfigData().equals(tn.getConfigData()))
				{
					return true;
				}
				else
				{
					return false;
				}
			}
			if (this.getValue() != null && tn.getValue() != null)
			{
				if (this.getValue().equals(tn.getValue()))
				{
					return true;
				}
				else
				{
					return false;
				}
			}
		}
		return false;
	}

	/**
	 * Gets the child.
	 * @param value the value
	 * @return the child
	 */
	public final ConfigTreeNode getChild(final String value)
	{
		for (ConfigTreeNode tn : this._children)
		{
			if (tn.getValue().equals(value))
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
	public final ConfigTreeNode[] getChildren()
	{
		return this._children.toArray(new ConfigTreeNode[_children.size()]);
	}

	/**
	 * Gets the config data.
	 * @return the config data
	 */
	public final ConfigData getConfigData()
	{
		return _configData;
	}

	/**
	 * Gets the label.
	 * @return the label
	 */
	public final String getLabel()
	{
		return _label;
	}

	/**
	 * Gets the parent.
	 * @return the parent
	 */
	public final ConfigTreeNode getParent()
	{
		return this._parent;
	}

	/**
	 * Gets the value.
	 * @return the value
	 */
	public final String getValue()
	{
		return _value;
	}

	/**
	 * Checks for children.
	 * @return true, if successful
	 */
	public final boolean hasChildren()
	{
		return !this._children.isEmpty();
	}

	@Override
	public int hashCode()
	{
		return _value.hashCode();
	}

	/**
	 * Checks if is selected.
	 * @return true, if is selected
	 */
	public final boolean isSelected()
	{
		return _selected;
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
	public final void setChildren(final List<ConfigTreeNode> children)
	{
		this._children = children;
	}

	/**
	 * Sets the config data.
	 * @param configData the new config data
	 */
	public final void setConfigData(final ConfigData configData)
	{
		this._configData = configData;
	}

	/**
	 * Sets the label.
	 * @param label the new label
	 */
	public final void setLabel(final String label)
	{
		this._label = label;
	}

	/**
	 * Sets the parent.
	 * @param parent the new parent
	 */
	public final void setParent(final ConfigTreeNode parent)
	{
		this._parent = parent;
	}

	/**
	 * Sets the selected.
	 * @param selected the new selected
	 */
	public final void setSelected(final boolean selected)
	{
		this._selected = selected;
	}

	/**
	 * Sets the value.
	 * @param value the new value
	 */
	public final void setValue(final String value)
	{
		this._value = value;
	}
}
