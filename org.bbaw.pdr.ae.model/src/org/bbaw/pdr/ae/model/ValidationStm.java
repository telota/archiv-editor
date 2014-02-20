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

import org.bbaw.pdr.ae.metamodel.PdrId;

/**
 * The Class ValidationStm.
 * @author Christoph Plutte
 */
public class ValidationStm implements Cloneable
{

	/** The reference. */
	private Reference _reference;

	/** The interpretation. */
	private String _interpretation;

	/** The authority. */
	private PdrId _authority;

	/**
	 * Instantiates a new validation stm.
	 */
	public ValidationStm()
	{

	}

	/**
	 * @return cloned validation statement
	 * @see java.lang.Object#clone()
	 */
	@Override
	public final ValidationStm clone()
	{
		try
		{
			ValidationStm clone = (ValidationStm) super.clone();
			if (this._authority != null)
			{
				clone._authority = this._authority.clone();
			}
			if (this._interpretation != null)
			{
				clone._interpretation = new String(this._interpretation);
			}
			if (this._reference != null)
			{
				clone._reference = this._reference.clone();
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
	 * @param vStm the v stm
	 * @return true, if successful
	 */
	public final boolean equals(final ValidationStm vStm)
	{
		if (this._reference != null && vStm._reference != null)
		{
			if (!this._reference.equals(vStm._reference))
			{
				return false;
			}
		}
		else if ((this._reference != null && vStm._reference == null)
				|| (this._reference == null && vStm._reference != null))
		{
			return false;
		}

		if (this._interpretation != null && vStm._interpretation != null)
		{
			if (!this._interpretation.equals(vStm._interpretation))
			{
				return false;
			}
		}
		else if ((this._interpretation != null && vStm._interpretation == null)
				|| (this._interpretation == null && vStm._interpretation != null))
		{
			return false;
		}
		if (this._authority != null && vStm._authority != null)
		{
			if (!this._authority.equals(vStm._authority))
			{
				return false;
			}
		}
		else if ((this._authority != null && vStm._authority == null)
				|| (this._authority == null && vStm._authority != null))
		{
			return false;
		}

		return true;
	}

	/**
	 * @return the authority
	 */
	public final PdrId getAuthority()
	{
		return _authority;
	}

	/**
	 * @return the interpretation
	 */
	public final String getInterpretation()
	{
		return _interpretation;
	}

	/**
	 * @return the reference
	 */
	public final Reference getReference()
	{
		return _reference;
	}

	/**
	 * Checks if is valid.
	 * @return true, if is valid
	 */
	public final boolean isValid()
	{
		if (_reference != null && _reference.isValid())
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * @param authority the authority to set
	 */
	public final void setAuthority(final PdrId authority)
	{
		this._authority = authority;
	}

	/**
	 * @param interpretation the interpretation to set
	 */
	public final void setInterpretation(final String interpretation)
	{
		this._interpretation = interpretation;
	}

	/**
	 * @param reference the reference to set
	 */
	public final void setReference(final Reference reference)
	{
		this._reference = reference;
	}
}
