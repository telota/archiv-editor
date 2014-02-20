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

import java.util.Vector;

/**
 * The Class Validation.
 * @author Christoph Plutte
 */
public class Validation implements Cloneable
{

	/** The validation stms. */
	private Vector<ValidationStm> _validationStms;

	/**
	 * Instantiates a new validation.
	 */
	public Validation()
	{

	}

	/**
	 * @return cloned validation
	 * @see java.lang.Object#clone()
	 */
	@Override
	public final Validation clone()
	{
		try
		{
			Validation clone = (Validation) super.clone();
			if (this._validationStms != null)
			{
				clone._validationStms = new Vector<ValidationStm>(this._validationStms.size());
				for (int i = 0; i < this._validationStms.size(); i++)
				{
					clone._validationStms.add(this._validationStms.get(i).clone());
				}
			}
			return clone;
		}
		catch (CloneNotSupportedException e)
		{
			throw new InternalError();
		}
	}

	/**
	 * Equals.
	 * @param validation the validation
	 * @return true, if successful
	 */
	public final boolean equals(final Validation validation)
	{
		if (validation.getValidationStms() != null)
		{
			if (!(this.getValidationStms().size() == validation.getValidationStms().size()))
			{
				return false;
			}
			for (int i = 0; i < this._validationStms.size(); i++)
			{
				if (!this._validationStms.get(i).equals(validation.getValidationStms().get(i)))
				{
					return false;
				}
			}
		}
		else if ((this.getValidationStms() == null && validation.getValidationStms() != null)
				|| (this.getValidationStms() != null && validation.getValidationStms() == null))
		{
			return false;
		}
		return true;

	}

	/**
	 * @return the validationStms
	 */
	public final Vector<ValidationStm> getValidationStms()
	{
		if (_validationStms == null)
		{
			_validationStms = new Vector<ValidationStm>(1);
		}
		return _validationStms;
	}

	/**
	 * Checks if is valid.
	 * @return true, if is valid
	 */
	public final boolean isValid()
	{
		if (_validationStms != null)
		{
			for (ValidationStm v : _validationStms)
			{
				if (v.getReference() == null || !v.getReference().isValid())
				{
					return false;
				}
			}
		}
		return (_validationStms != null && _validationStms.size() > 0);
	}

	/**
	 * Removes the.
	 * @param index the index
	 * @return true, if successful
	 */
	public final boolean remove(final Integer index)
	{
		if (_validationStms != null)
		{
			_validationStms.removeElementAt(index);
		}
		return true;
	}

	/**
	 * @param validationStms the validationStms to set
	 */
	public final void setValidationStms(final Vector<ValidationStm> validationStms)
	{
		this._validationStms = validationStms;
	}
}
