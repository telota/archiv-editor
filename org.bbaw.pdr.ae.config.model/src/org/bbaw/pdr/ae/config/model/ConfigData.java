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

import java.util.HashMap;

import org.bbaw.pdr.ae.metamodel.IAEPresentable;

/**
 * The Class ConfigData.
 * @author Christoph Plutte
 */
public class ConfigData implements IAEPresentable, Cloneable
{

	/** The documentation. */
	private HashMap<String, String> _documentation = new HashMap<String, String>(4);

	/** The children. */
	private HashMap<String, ConfigData> _children = new HashMap<String, ConfigData>(4);

	/** The value. */
	private String _value;

	/** The label. */
	private String _label;

	/** The my have children. */
	private boolean _myHaveChildren;

	/** The _ignore. */
	private boolean _ignore;

	/** The pos. */
	private String _pos;

	/** The priority. */
	private int _priority;

	/** The image string. */
	private String _imageString;

	/** The _parent. */
	private ConfigData _parent;
	
	/** The _mandatory. */
	private boolean _mandatory;
	/**
	 * compares this priority to the priority of the given AEPresentable object.
	 * @param arg0 given AEPresentable
	 * @return this._priority - arg0.getPriority();
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public final int compareTo(final IAEPresentable arg0)
	{
		return this._priority - arg0.getPriority();
	}

	/**
	 * Equals. checks whether this configData and another are equal.
	 * @param obj object.
	 * @return true, if equal
	 */
	@Override
	public boolean equals(final Object obj)
	{
		if (obj instanceof ConfigData)
		{
			ConfigData cd = (ConfigData) obj;
			if (this.getPos() != null && cd.getPos() != null && this.getValue() != null && cd.getValue() != null)
			{
				if (this.getChildren() != null && cd.getChildren() != null)
				{
					if (this.getChildren().size() == cd.getChildren().size())
					{
						if (this.getPos() == cd.getPos() && this.getValue().equals(cd.getValue()))
						{
							return true;
						}

					}
					else if (this.getPos() == cd.getPos() && this.getValue().equals(cd.getValue()))
					{
						return true;
					}
				}
				else if (this.getPos() == cd.getPos())
				{
					return (this.getValue().equals(cd.getValue()));
				}
				return false;
			}
			else if (this.getValue() != null && cd.getValue() != null)
			{
				return (this.getValue().equals(cd.getValue()));
			}
			else
			{
				return false;
			}
		}
		return false;
	}

	/**
	 * Gets the children.
	 * @return the children
	 */
	public final HashMap<String, ConfigData> getChildren()
	{
		return _children;
	}

	/**
	 * @return label
	 * @see org.eclipse.jface.fieldassist.IContentProposal#getContent()
	 */
	@Override
	public final String getContent()
	{
		return _label;
	}

	/**
	 * @return 0
	 * @see org.eclipse.jface.fieldassist.IContentProposal#getCursorPosition()
	 */
	@Override
	public final int getCursorPosition()
	{
		return 0;
	}

	/**
	 * @return the path of this configData
	 * @see org.eclipse.jface.fieldassist.IContentProposal#getDescription()
	 */
	@Override
	public final String getDescription()
	{
		if (this instanceof ConfigItem)
		{
			String path = "";
			ConfigItem ci = (ConfigItem) this;
			String sep = " >> ";
			if (ci.getPos() != null)
			{
				if (ci.getPos().equals("_TEXTNODE")) //$NON-NLS-1$
				{
					path = ci.getLabel(); //$NON-NLS-1$
				}
				else if (ci.getPos().equals("type")) //$NON-NLS-1$
				{
					DataType dt = (DataType) ci.getParent();
					if (dt.getDatatypeDesc() != null)
					{
						path = dt.getDatatypeDesc().getProvider();
					}
					path += sep + dt.getLabel(); //$NON-NLS-1$
					path += sep + ci.getLabel(); //$NON-NLS-1$
				}
				else if (ci.getPos().equals("subtype")) //$NON-NLS-1$
				{
					ConfigItem parent = (ConfigItem) ci.getParent();
					DataType dt = (DataType) parent.getParent();
					if (dt.getDatatypeDesc() != null)
					{
						path = dt.getDatatypeDesc().getProvider();
					}
					path += sep + dt.getLabel(); //$NON-NLS-1$
					path += sep + parent.getLabel(); //$NON-NLS-1$
					path += sep + ci.getLabel(); //$NON-NLS-1$
	
				}
				else if (ci.getPos().equals("role")) //$NON-NLS-1$
				{
					ConfigItem parent = (ConfigItem) ci.getParent();
					ConfigItem grandParent = (ConfigItem) parent.getParent();
					DataType dt = (DataType) grandParent.getParent();
					if (dt.getDatatypeDesc() != null)
					{
						path = dt.getDatatypeDesc().getProvider();
					}
					path += sep + dt.getLabel(); //$NON-NLS-1$
					path += sep + grandParent.getLabel(); //$NON-NLS-1$
					path += sep + parent.getLabel(); //$NON-NLS-1$
					path += sep + ci.getLabel(); //$NON-NLS-1$
				}
				else if (!ci.getLabel().equals(ci.getValue()))
				{
					path = ci.getLabel() + " = " + ci.getValue(); //$NON-NLS-1$
				}
			}
			else if (!ci.getLabel().equals(ci.getValue()))
			{
				path = ci.getLabel() + " = " + ci.getValue(); //$NON-NLS-1$
			}
			if (_documentation != null && !_documentation.isEmpty())
			{
				path += "\n\n";

				for (String key : _documentation.keySet())
				{
					path += _documentation.get(key) + "\n\n";
				}
			}

			return path;
		}
		else
		{
			String desc = "";
			if (!this.getLabel().equals(this.getValue()))
			{
				desc = this.getLabel() + " = " + this.getValue(); //$NON-NLS-1$
			}
			if (_documentation != null && !_documentation.isEmpty())
			{
				desc += "\n\n";

				for (String key : _documentation.keySet())
				{
					desc += _documentation.get(key) + "\n\n";
				}
			}
			return desc;

		}
	}

	/**
	 * Gets the documentation.
	 * @return the documentation
	 */
	public final HashMap<String, String> getDocumentation()
	{
		return _documentation;
	}

	/**
	 * @return image String
	 * @see org.bbaw.pdr.ae.metamodel.IAEPresentable#getImageString()
	 */
	@Override
	public final String getImageString()
	{
		return _imageString;
	}

	/**
	 * @return label
	 * @see org.bbaw.pdr.ae.metamodel.IAEPresentable#getLabel()
	 */
	@Override
	public final String getLabel()
	{
		return _label;
	}

	/**
	 * Gets the pos.
	 * @return the pos
	 */
	public final String getPos()
	{
		return _pos;
	}

	/**
	 * @return priority
	 * @see org.bbaw.pdr.ae.metamodel.IAEPresentable#getPriority()
	 */
	@Override
	public final int getPriority()
	{
		return _priority;
	}

	/**
	 * @return value
	 * @see org.bbaw.pdr.ae.metamodel.IAEPresentable#getValue()
	 */
	@Override
	public String getValue()
	{
		if (_value != null)
		{
			return _value;
		}
		else
		{
			return "error";
		}
	}

	@Override
	public final int hashCode()
	{
		return _value.hashCode();
	}

//	/**
//	 * Checks if is locked.
//	 * @return true, if is locked
//	 */
//	public final boolean isLocked()
//	{
//		return _locked;
//	}

	/**
	 * Checks if is my have children.
	 * @return true, if is my have children
	 */
	public final boolean isMyHaveChildren()
	{
		return _myHaveChildren;
	}

	/**
	 * Process position settings.
	 */
	public final void processPositionSettings()
	{
		if (!(this instanceof DatatypeDesc))
		{
			for (String s : this.getChildren().keySet())
			{
				if (this.getChildren().get(s) instanceof ConfigItem)
				{
					ConfigItem ci = (ConfigItem) this.getChildren().get(s);

					for (String s2 : this.getChildren().keySet())
					{
						ConfigItem c2 = (ConfigItem) this.getChildren().get(s2);
						if (!ci.equals(c2) && ci.compareTo(c2) == 0)
						{
							c2.setPriority(c2.getPriority() + 1);
						}
					}
					if (ci.getChildren().size() > 0)
					{
						ci.processPositionSettings();
					}
				}
			}
		}
		else
		{
			for (String key : this.getChildren().keySet())
			{
				this.getChildren().get(key).processPositionSettings();
			}
		}
	}

	/**
	 * Sets the children.
	 * @param children the children
	 */
	public final void setChildren(final HashMap<String, ConfigData> children)
	{
		this._children = children;
	}

	/**
	 * Sets the documentation.
	 * @param documentation the documentation
	 */
	public final void setDocumentation(final HashMap<String, String> documentation)
	{
		this._documentation = documentation;
	}

	/**
	 * Sets the image string.
	 * @param imageString the new image string
	 */
	public final void setImageString(final String imageString)
	{
		this._imageString = imageString;
	}

	/**
	 * Sets the label.
	 * @param label the new label
	 */
	public final void setLabel(final String label)
	{
		this._label = label;
	}

//	/**
//	 * Sets the locked.
//	 * @param locked the new locked
//	 */
//	public final void setLocked(final boolean locked)
//	{
//		this._locked = locked;
//	}

	/**
	 * Sets the my have children.
	 * @param myHaveChildren the new my have children
	 */
	public final void setMyHaveChildren(final boolean myHaveChildren)
	{
		this._myHaveChildren = myHaveChildren;
	}

	/**
	 * Sets the pos.
	 * @param pos the new pos
	 */
	public final void setPos(final String pos)
	{
		this._pos = pos;
	}

	/**
	 * Sets the priority.
	 * @param priority the new priority
	 */
	public final void setPriority(final int priority)
	{
		this._priority = priority;
	}

	/**
	 * @param value value to be set
	 * @see org.bbaw.pdr.ae.metamodel.IAEPresentable#setValue(java.lang.String)
	 */
	@Override
	public final void setValue(final String value)
	{
		this._value = value;
	}

	/**
	 * Checks if is ignore.
	 * @return true, if is ignore
	 */
	public final boolean isIgnore()
	{
		return _ignore;
	}

	/**
	 * Sets the ignore.
	 * @param ignore the new ignore
	 */
	public final void setIgnore(final boolean ignore)
	{
		this._ignore = ignore;
	}

	public ConfigData clone()
	{
		ConfigData clone = null;
		try
		{
			clone = (ConfigData) super.clone();
			if (this._documentation != null && !this._documentation.isEmpty())
			{
				clone.setDocumentation(new HashMap<String, String>(this._documentation.size()));
				for (String key : this._documentation.keySet())
				{
					clone.getDocumentation().put(new String(key), new String(this._documentation.get(key)));
				}

			}
			if (this._imageString != null)
			{
				clone.setImageString(new String(this._imageString));
			}
			if (this._label != null)
			{
				clone.setLabel(new String(this._label));
			}
			if (this._pos != null)
			{
				clone.setPos(new String(this._pos));
			}
			if (this._value != null)
			{
				clone.setValue(new String(this._value));
			}

			clone.setIgnore(this._ignore);
			clone.setMandatory(this.isMandatory());
			clone.setParent(this._parent);
			clone.setPriority(this._priority);
			return clone;
		}
		catch (CloneNotSupportedException e)
		{
			e.printStackTrace();
		}
		return clone;
	}
	/**
	 * Sets the parent.
	 * @param parent the new parent
	 */
	public final void setParent(final ConfigData parent)
	{
		this._parent = parent;
	}
	/**
	 * Gets the parent.
	 * @return the parent
	 */
	public final ConfigData getParent()
	{
		return _parent;
	}
	/**
	 * Removes the.
	 */
	public final void remove()
	{
		if (_parent.getChildren().remove(this.getValue()) == null)
		{
			String oldKey = "";
			for (String key : _parent.getChildren().keySet())
			{
				if (_parent.getChildren().get(key).equals(this))
				{
					oldKey = key;
					break;
				}
			}
			_parent.getChildren().remove(oldKey);
		}
	}
	/**
	 * Checks if is mandatory.
	 * @return true, if is mandatory
	 */
	public final boolean isMandatory()
	{
		return _mandatory;
	}

	/**
	 * Sets the mandatory.
	 * @param mandatory the new mandatory
	 */
	public final void setMandatory(final boolean mandatory)
	{
		this._mandatory = mandatory;
	}
}
