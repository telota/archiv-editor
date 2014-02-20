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

public class AspectConfigTemplate extends ConfigData
{
	private int _widgetType;

	private boolean _required;

	private String _element;

	private String _type;

	private String _subtype;

	private String _role;

	private String _date1;

	private String _date2;

	private int _levelSpan;

	private boolean _allowMultiple;
	
	private String _prefix;
	
	private String _suffix;

	private int _horizontalSpan;

	public AspectConfigTemplate(String value)
	{
		super();
		setValue(value);
		setLabel(value);
	}

	public AspectConfigTemplate()
	{
		// TODO Auto-generated constructor stub
	}

	public int getWidgetType()
	{
		return _widgetType;
	}

	public void setWidgetType(int _widgetType)
	{
		this._widgetType = _widgetType;
	}

	public boolean isRequired()
	{
		return _required;
	}

	public void setRequired(boolean _required)
	{
		this._required = _required;
	}

	public String getElement()
	{
		return _element;
	}

	public void setElement(String _element)
	{
		this._element = _element;
	}

	public String getType()
	{
		return _type;
	}

	public void setType(String _type)
	{
		this._type = _type;
	}

	public String getSubtype()
	{
		return _subtype;
	}

	public void setSubtype(String _subtype)
	{
		this._subtype = _subtype;
	}

	public String getRole()
	{
		return _role;
	}

	public void setRole(String _role)
	{
		this._role = _role;
	}

	public String getDate1()
	{
		return _date1;
	}

	public void setDate1(String _date1)
	{
		this._date1 = _date1;
	}

	public String getDate2()
	{
		return _date2;
	}

	public void setDate2(String _date2)
	{
		this._date2 = _date2;
	}

	public int getLevelSpan()
	{
		return _levelSpan;
	}

	public void setLevelSpan(int _levelSpan)
	{
		this._levelSpan = _levelSpan;
	}

	public boolean isAllowMultiple()
	{
		return _allowMultiple;
	}

	public void setAllowMultiple(boolean _allowMultiple)
	{
		this._allowMultiple = _allowMultiple;
	}


	public int getHorizontalSpan()
	{
		return _horizontalSpan;
	}

	public void setHorizontalSpan(int _horizontalSpan)
	{
		this._horizontalSpan = _horizontalSpan;
	}

	public String getPrefix()
	{
		return _prefix;
	}

	public void setPrefix(String _prefix)
	{
		this._prefix = _prefix;
	}

	public String getSuffix()
	{
		return _suffix;
	}

	public void setSuffix(String _suffix)
	{
		this._suffix = _suffix;
	}

	public AspectConfigTemplate clone()
	{
		AspectConfigTemplate clone = (AspectConfigTemplate) super.clone();
		if (this._date1 != null)
		{
			clone._date1 = new String(this._date1);
		}
		if (this._date2 != null)
		{
			clone._date2 = new String(this._date2);
		}
		if (this._element != null)
		{
			clone._element = new String(this._element);
		}
		if (this._prefix != null)
		{
			clone._prefix = new String(this._prefix);
		}
		if (this._role != null)
		{
			clone._role = new String(this._role);
		}
		if (this._subtype != null)
		{
			clone._subtype = new String(this._subtype);
		}
		if (this._suffix != null)
		{
			clone._suffix = new String(this._suffix);
		}
		if (this._type != null)
		{
			clone._type = new String(this._type);
		}
		clone._allowMultiple = this._allowMultiple;
		clone._horizontalSpan = this._horizontalSpan;
		clone._levelSpan = this._levelSpan;
		clone._widgetType = this._widgetType;
		return clone;

	}

	@Override
	public String getValue() {
		return getLabel();
	}
}
