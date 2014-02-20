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
package org.bbaw.pdr.ae.model;

import org.bbaw.pdr.ae.metamodel.PdrDate;

/**
 * The Class TaggingRange.
 * @author Christoph Plutte
 */
public class TaggingRange implements Comparable<TaggingRange>, Cloneable
{
	/** name of Tag. */
	private String _name;
	/** type of Tag. */
	private String _type;
	/** subtype of Tag. */
	private String _subtype;
	/** role of tag. */
	private String _role;
	/** key of Tag. */
	private String _key;
	/** ana of Tag. */
	private String _ana;
	/** date when. */
	private PdrDate _when;
	/** date notBefore. */
	private PdrDate _notBefore;

	/** date notAfter. */
	private PdrDate _notAfter;
	/**
     */
	private String _textValue;
	/**
     */
	private int _start;
	/**
     */
	private int _length;
	/**
     */
	/** date for tagging dates. */
	private PdrDate _from;
	/** date for tagging dates. */
	private PdrDate _to;

	/**
	 * Instantiates a new tagging range.
	 */
	public TaggingRange()
	{
	}

	/**
	 * Instantiates a new tagging range.
	 * @param name the name
	 * @param type the type
	 * @param when the when
	 * @param start the start
	 * @param lenght the lenght
	 */
	public TaggingRange(final String name, final String type, final PdrDate when, final int start, final int lenght)
	{
		this._name = name;
		this._type = type;
		this._when = when;
		this._start = start;
		this._length = lenght;
	}

	/**
	 * Instantiates a new tagging range.
	 * @param name the name
	 * @param type the type
	 * @param from the from
	 * @param to the to
	 * @param start the start
	 * @param lenght the lenght
	 */
	public TaggingRange(final String name, final String type, final PdrDate from, final PdrDate to, final int start,
			final int lenght)
	{
		this._name = name;
		this._type = type;
		this.setFrom(from);
		this.setTo(to);
		this._start = (start < 0 ? 0 : start);
		this._length = lenght;
	}

	/**
	 * constructor with als params.
	 * @param name name of tr.
	 * @param type type of tag.
	 * @param subtype subtype of tag.
	 * @param key key of tag.
	 * @param ana ana attribut
	 * @param start start
	 * @param lenght length
	 */
	public TaggingRange(final String name, final String type, final String subtype, final String ana, final String key,
			final int start, final int lenght)
	{
		this._name = name;
		this._type = type;
		this._subtype = subtype;
		this._ana = ana;
		this._key = key;

		this._start = (start < 0 ? 0 : start);
		this._length = lenght;
	}

	/**
	 * Instantiates a new tagging range.
	 * @param name the name
	 * @param type the type
	 * @param subtype the subtype
	 * @param role the role
	 * @param ana the ana
	 * @param key the key
	 */
	public TaggingRange(final String name, final String type, final String subtype, final String role,
			final String ana, final String key)
	{
		this._name = name;
		this._type = type;
		this._subtype = subtype;
		this._role = role;
		this._ana = ana;
		this._key = key;
	}

	/**
	 * Instantiates a new tagging range.
	 * @param name the name
	 * @param type the type
	 * @param subtype the subtype
	 * @param role the role
	 * @param ana the ana
	 * @param key the key
	 * @param start the start
	 * @param lenght the lenght
	 */
	public TaggingRange(final String name, final String type, final String subtype, final String role,
			final String ana, final String key, final int start, final int lenght)
	{
		this._name = name;
		this._type = type;
		this._subtype = subtype;
		this._role = role;
		this._ana = ana;
		this._key = key;
		this._start = (start < 0 ? 0 : start);
		this._length = lenght;
	}

	/**
	 * @return cloned tagging ragen
	 * @see java.lang.Object#clone()
	 */
	@Override
	public final TaggingRange clone()
	{
		try
		{
			TaggingRange clone = (TaggingRange) super.clone();
			if (this._name != null)
			{
				clone._name = new String(this._name);
			}
			if (this._type != null)
			{
				clone._type = new String(this._type);
			}
			if (this._subtype != null)
			{
				clone._subtype = new String(this._subtype);
			}
			if (this._role != null)
			{
				clone._role = new String(this._role);
			}
			if (this._key != null)
			{
				clone._key = new String(this._key);
			}
			if (this._ana != null)
			{
				clone._ana = new String(this._ana);
			}
			clone._start = this._start;
			clone._length = this._length;

			if (this._when != null)
			{
				clone._when = this._when.clone();
			}
			if (this._notBefore != null)
			{
				clone._notBefore = this._notBefore.clone();
			}
			if (this._from != null)
			{
				clone._from = this._from.clone();
			}
			if (this._notAfter != null)
			{
				clone._notAfter = this._notAfter.clone();
			}
			if (this._to != null)
			{
				clone._to = this._to.clone();
			}
			if (this._textValue != null)
			{
				clone._textValue = new String(this._textValue);
			}
			return clone;
		}
		catch (CloneNotSupportedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this;

	}

	/**
	 * @param tr tagging range
	 * @return comparisons of start int
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public final int compareTo(final TaggingRange tr)
	{
		int diff = this._start - tr._start;

		return diff;
	}

	public boolean equals(Object o)
	{
		if (o instanceof TaggingRange)
		{
			TaggingRange tr = (TaggingRange) o;
			if (this.equalsContent(tr) && ((this._ana == null && tr.getAna() == null) || this._ana.equals(tr.getAna())))
			{
				return true;
			}
		}
		return false;

	}

	/**
	 * Equals content.
	 * @param tr the tr
	 * @return true, if successful
	 */
	public final boolean equalsContent(final TaggingRange tr)
	{
		if (this._name != null && tr._name != null)
		{
			if (!this._name.equals(tr._name))
			{
				return false;
			}
		}
		else if ((this._name != null && tr._name == null) || (this._name == null && tr._name != null))
		{
			return false;
		}
		if (this._type != null && tr._type != null)
		{
			if (!this._type.equals(tr._type))
			{
				return false;
			}
		}
		else if ((this._type != null && tr._type == null) || (this._type == null && tr._type != null))
		{
			return false;
		}
		if (this._subtype != null && tr._subtype != null)
		{
			if (!this._subtype.equals(tr._subtype))
			{
				return false;
			}
		}
		else if ((this._subtype != null && tr._subtype == null) || (this._subtype == null && tr._subtype != null))
		{
			return false;
		}
		if (this._role != null && tr._role != null)
		{
			if (!this._role.equals(tr._role))
			{
				return false;
			}
		}
		else if ((this._role != null && tr._role == null) || (this._role == null && tr._role != null))
		{
			return false;
		}
		if (this._key != null && tr._key != null)
		{
			if (!this._key.equals(tr._key))
			{
				return false;
			}
		}
		else if ((this._key != null && tr._key == null) || (this._key == null && tr._key != null))
		{
			return false;
		}
		// ana is not being compared because it is not considered as content but
		// as relation.
		if (!(this._start == tr._start))
		{
			return false;
		}
		if (!(this._length == tr._length))
		{
			return false;
		}

		if (this._when != null && tr._when != null)
		{
			if (!this._when.equals(tr._when))
			{
				return false;
			}
		}
		else if ((this._when != null && tr._when == null) || (this._when == null && tr._when != null))
		{
			return false;
		}
		if (this._notBefore != null && tr._notBefore != null)
		{
			if (!this._notBefore.equals(tr._notBefore))
			{
				return false;
			}
		}
		else if ((this._notBefore != null && tr._notBefore == null)
				|| (this._notBefore == null && tr._notBefore != null))
		{
			return false;
		}
		if (this._from != null && tr._from != null)
		{
			if (!this._from.equals(tr._from))
			{
				return false;
			}
		}
		else if ((this._from != null && tr._from == null) || (this._from == null && tr._from != null))
		{
			return false;
		}
		if (this._notAfter != null && tr._notAfter != null)
		{
			if (!this._notAfter.equals(tr._notAfter))
			{
				return false;
			}
		}
		else if ((this._notAfter != null && tr._notAfter == null) || (this._notAfter == null && tr._notAfter != null))
		{
			return false;
		}
		if (this._to != null && tr._to != null)
		{
			if (!this._to.equals(tr._to))
			{
				return false;
			}
		}
		else if ((this._to != null && tr._to == null) || (this._to == null && tr._to != null))
		{
			return false;
		}

		return true;

	}

	/**
	 * @return Returns the ana.
	 */
	public final String getAna()
	{
		return _ana;
	}

	/**
	 * Gets the from.
	 * @return the from
	 */
	public final PdrDate getFrom()
	{
		return _from;
	}

	/**
	 * @return Returns the key.
	 */
	public final String getKey()
	{
		return _key;
	}

	/**
	 * @return Returns the length.
	 */
	public final int getLength()
	{
		return _length;
	}

	/**
	 * @return Returns the name.
	 */
	public final String getName()
	{
		return _name;
	}

	/**
	 * Gets the not after.
	 * @return the not after
	 */
	public final PdrDate getNotAfter()
	{
		return _notAfter;
	}

	/**
	 * Gets the not before.
	 * @return the not before
	 */
	public final PdrDate getNotBefore()
	{
		return _notBefore;
	}

	/**
	 * Gets the role.
	 * @return the role
	 */
	public final String getRole()
	{
		return _role;
	}

	/**
	 * @return Returns the start.
	 */
	public final int getStart()
	{
		return _start;
	}

	/**
	 * @return Returns the subtype.
	 */
	public final String getSubtype()
	{
		return _subtype;
	}

	/**
	 * @return Returns the textValue.
	 */
	public final String getTextValue()
	{
		return _textValue;
	}

	/**
	 * Gets the to.
	 * @return the to
	 */
	public final PdrDate getTo()
	{
		return _to;
	}

	/**
	 * @return Returns the type.
	 */
	public final String getType()
	{
		return _type;
	}

	/**
	 * Gets the when.
	 * @return the when
	 */
	public final PdrDate getWhen()
	{
		return _when;
	}

	/**
	 * @param ana The key to set.
	 */
	public final void setAna(final String ana)
	{
		this._ana = ana;
	}

	/**
	 * Sets the from.
	 * @param from the new from
	 */
	public final void setFrom(final PdrDate from)
	{
		this._from = from;
	}

	/**
	 * @param key The key to set.
	 */
	public final void setKey(final String key)
	{
		this._key = key;
	}

	/**
	 * @param length The length to set.
	 */
	public final void setLength(final int length)
	{
		this._length = length;
	}

	/**
	 * @param name The name to set.
	 */
	public final void setName(final String name)
	{
		this._name = name;
	}

	/**
	 * Sets the not after.
	 * @param notAfter the new not after
	 */
	public final void setNotAfter(final PdrDate notAfter)
	{
		this._notAfter = notAfter;
	}

	/**
	 * Sets the not before.
	 * @param notBefore the new not before
	 */
	public final void setNotBefore(final PdrDate notBefore)
	{
		this._notBefore = notBefore;
	}

	/**
	 * Sets the role.
	 * @param role the new role
	 */
	public final void setRole(final String role)
	{
		this._role = role;
	}

	/**
	 * @param start The start to set.
	 */
	public final void setStart(final int start)
	{
		this._start = (start < 0 ? 0 : start);
	}

	/**
	 * @param subtype The subtype to set.
	 */
	public final void setSubtype(final String subtype)
	{
		this._subtype = subtype;
	}

	/**
	 * @param textValue The textValue to set.
	 */
	public final void setTextValue(final String textValue)
	{
		this._textValue = textValue;
	}

	/**
	 * Sets the to.
	 * @param to the new to
	 */
	public final void setTo(final PdrDate to)
	{
		this._to = to;
	}

	/**
	 * @param type The type to set.
	 */
	public final void setType(final String type)
	{
		this._type = type;
	}

	/**
	 * Sets the when.
	 * @param when the new when
	 */
	public void setWhen(final PdrDate when)
	{
		this._when = when;
	}
}
