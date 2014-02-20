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
package org.bbaw.pdr.ae.model.search;

import org.bbaw.pdr.ae.metamodel.PdrDate;
import org.bbaw.pdr.ae.metamodel.PdrId;

/**
 * The Class Criteria.
 * @author Christoph Plutte
 */
public class Criteria implements Comparable<Criteria>, Cloneable
{

	/** 0 is aspect and tagging, 1 is related reference, 2 is relationStm. */
	private String _type;

	/** The operator. */
	private String _operator;

	/** The crit0. */
	private String _crit0; // semantic;

	/** The crit1. */
	private String _crit1; // taggingName;

	/** The crit2. */
	private String _crit2; // taggingType;

	/** The crit3. */
	private String _crit3; // taggingSubtype;

	/** The crit4. */
	private String _crit4; // taggingRole;

	/** The crit5. */
	private String _crit5; // taggingKey;

	/** The search text. */
	private String _searchText;

	/** The related id. */
	private PdrId _relatedId;

	/** The crit6. */
	private String _crit6; // internal;

	/** The relation class. */
	private String _relationClass;

	/** The relation context. */
	private String _relationContext;

	/** The date type. */
	private String _dateType;

	/** The date from. */
	private PdrDate _dateFrom;

	/** The date to. */
	private PdrDate _dateTo;

	/** The is fuzzy. */
	private boolean _isFuzzy;

	/** The include concurrences. */
	private boolean _includeConcurrences;

	/** The is empty. */
	private boolean _isEmpty;

	@Override
	public final Criteria clone()
	{
		try
		{
			Criteria clone = (Criteria) super.clone();
			if (this._crit0 != null)
			{
				clone._crit0 = new String(this._crit0);
			}
			if (this._crit1 != null)
			{
				clone._crit1 = new String(this._crit1);
			}
			if (this._crit2 != null)
			{
				clone._crit2 = new String(this._crit2);
			}
			if (this._crit3 != null)
			{
				clone._crit3 = new String(this._crit3);
			}
			if (this._crit4 != null)
			{
				clone._crit4 = new String(this._crit4);
			}
			if (this._crit5 != null)
			{
				clone._crit5 = new String(this._crit5);
			}
			if (this._crit6 != null)
			{
				clone._crit6 = new String(this._crit6);
			}
			if (this._dateType != null)
			{
				clone._dateType = new String(this._dateType);
			}
			if (this._operator != null)
			{
				clone._operator = new String(this._operator);
			}
			if (this._relationClass != null)
			{
				clone._relationClass = new String(this._relationClass);
			}
			if (this._relationContext != null)
			{
				clone._relationContext = new String(this._relationContext);
			}
			if (this._searchText != null)
			{
				clone._searchText = new String(this._searchText);
			}
			if (this._type != null)
			{
				clone._type = new String(this._type);
			}

			if (this._dateFrom != null)
			{
				clone._dateFrom = this._dateFrom.clone();
			}
			if (this._dateTo != null)
			{
				clone._dateTo = this._dateTo.clone();
			}

			if (this._relatedId != null)
			{
				clone._relatedId = this._relatedId.clone();
			}

			clone._includeConcurrences = this._includeConcurrences;
			clone._isEmpty = this._isEmpty;
			clone._isFuzzy = this._isFuzzy;

			return clone;
		}
		catch (CloneNotSupportedException e)
		{
			throw new InternalError();
		}
	}

	@Override
	public final int compareTo(final Criteria c)
	{
		if (this.getType().equals("tagging"))
		{
			if ((c.getType().equals("tagging")))
			{
				return 0;
			}
			else
			{
				return -1;
			}
		}
		else if (this.getType().equals("relation"))
		{
			if ((c.getType().equals("tagging")))
			{
				return 1;
			}
			else if ((c.getType().equals("relation")))
			{
				return 0;
			}
			else
			{
				return -1;
			}
		}
		else if (this.getType().equals("reference"))
		{
			if ((c.getType().equals("tagging")))
			{
				return 1;
			}
			if ((c.getType().equals("relation")))
			{
				return 1;
			}
			else if ((c.getType().equals("reference")))
			{
				return 0;
			}
			else
			{
				return -1;
			}
		}
		return 0;
	}

	/**
	 * @return the crit0
	 */
	public final String getCrit0()
	{
		return _crit0;
	}

	/**
	 * @return the crit1
	 */
	public final String getCrit1()
	{
		return _crit1;
	}

	/**
	 * @return the crit2
	 */
	public final String getCrit2()
	{
		return _crit2;
	}

	/**
	 * @return the crit3
	 */
	public final String getCrit3()
	{
		return _crit3;
	}

	/**
	 * @return the crit4
	 */
	public final String getCrit4()
	{
		return _crit4;
	}

	/**
	 * @return the crit5
	 */
	public final String getCrit5()
	{
		return _crit5;
	}

	/**
	 * @return the crit6
	 */
	public final String getCrit6()
	{
		return _crit6;
	}

	/**
	 * Gets the date from.
	 * @return the date from
	 */
	public final PdrDate getDateFrom()
	{
		return _dateFrom;
	}

	/**
	 * Gets the date to.
	 * @return the date to
	 */
	public final PdrDate getDateTo()
	{
		return _dateTo;
	}

	/**
	 * Gets the date type.
	 * @return the date type
	 */
	public final String getDateType()
	{
		return _dateType;
	}

	/**
	 * Gets the operator.
	 * @return the operator
	 */
	public final String getOperator()
	{
		return _operator;
	}

	/**
	 * Gets the related id.
	 * @return the related id
	 */
	public final PdrId getRelatedId()
	{
		return _relatedId;
	}

	/**
	 * Gets the relation class.
	 * @return the relation class
	 */
	public final String getRelationClass()
	{
		return _relationClass;
	}

	/**
	 * Gets the relation context.
	 * @return the relation context
	 */
	public final String getRelationContext()
	{
		return _relationContext;
	}

	/**
	 * Gets the search text.
	 * @return the search text
	 */
	public final String getSearchText()
	{
		return _searchText;
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
	 * Checks if is empty.
	 * @return true, if is empty
	 */
	public final boolean isEmpty()
	{
		return _isEmpty;
	}

	/**
	 * Checks if is fuzzy.
	 * @return true, if is fuzzy
	 */
	public final boolean isFuzzy()
	{
		return _isFuzzy;
	}

	/**
	 * Checks if is include concurrences.
	 * @return true, if is include concurrences
	 */
	public final boolean isIncludeConcurrences()
	{
		return _includeConcurrences;
	}

	/**
	 * @param crit0 the crit0 to set
	 */
	public final void setCrit0(final String crit0)
	{
		this._crit0 = crit0;
	}

	/**
	 * @param crit1 the crit1 to set
	 */
	public final void setCrit1(final String crit1)
	{
		this._crit1 = crit1;
	}

	/**
	 * @param crit2 the crit2 to set
	 */
	public final void setCrit2(final String crit2)
	{
		this._crit2 = crit2;
	}

	/**
	 * @param crit3 the crit3 to set
	 */
	public final void setCrit3(final String crit3)
	{
		this._crit3 = crit3;
	}

	/**
	 * @param crit4 the crit4 to set
	 */
	public final void setCrit4(final String crit4)
	{
		this._crit4 = crit4;
	}

	/**
	 * @param crit5 the crit5 to set
	 */
	public final void setCrit5(final String crit5)
	{
		this._crit5 = crit5;
	}

	/**
	 * @param crit6 the crit6 to set
	 */
	public final void setCrit6(final String crit6)
	{
		this._crit6 = crit6;
	}

	/**
	 * Sets the date from.
	 * @param dateFrom the new date from
	 */
	public final void setDateFrom(final PdrDate dateFrom)
	{
		this._dateFrom = dateFrom;
	}

	/**
	 * Sets the date to.
	 * @param dateTo the new date to
	 */
	public final void setDateTo(final PdrDate dateTo)
	{
		this._dateTo = dateTo;
	}

	/**
	 * Sets the date type.
	 * @param dateType the new date type
	 */
	public final void setDateType(final String dateType)
	{
		this._dateType = dateType;
	}

	/**
	 * Sets the empty.
	 * @param isEmpty the new empty
	 */
	public final void setEmpty(final boolean isEmpty)
	{
		this._isEmpty = isEmpty;
	}

	/**
	 * Sets the fuzzy.
	 * @param isFuzzy the new fuzzy
	 */
	public final void setFuzzy(final boolean isFuzzy)
	{
		this._isFuzzy = isFuzzy;
	}

	/**
	 * Sets the include concurrences.
	 * @param includeConcurrences the new include concurrences
	 */
	public final void setIncludeConcurrences(final boolean includeConcurrences)
	{
		this._includeConcurrences = includeConcurrences;
	}

	/**
	 * Sets the operator.
	 * @param operator the new operator
	 */
	public final void setOperator(final String operator)
	{
		this._operator = operator;
	}

	/**
	 * Sets the related id.
	 * @param relatedId the new related id
	 */
	public final void setRelatedId(final PdrId relatedId)
	{
		this._relatedId = relatedId;
	}

	/**
	 * Sets the relation class.
	 * @param relationClass the new relation class
	 */
	public final void setRelationClass(final String relationClass)
	{
		this._relationClass = relationClass;
	}

	/**
	 * Sets the relation context.
	 * @param relationContext the new relation context
	 */
	public final void setRelationContext(final String relationContext)
	{
		this._relationContext = relationContext;
	}

	/**
	 * Sets the search text.
	 * @param searchText the new search text
	 */
	public final void setSearchText(final String searchText)
	{
		this._searchText = searchText;
	}

	/**
	 * Sets the type.
	 * @param type the new type
	 */
	public final void setType(final String type)
	{
		this._type = type;
	}
}
