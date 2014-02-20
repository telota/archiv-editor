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

/**
 * The Class SemanticStm.
 * @author Christoph Plutte
 */
public class SemanticStm implements Cloneable
{

	/** The label. */
	private String _label;

	/** The provider. */
	private String _provider;

	/**
	 * Instantiates a new semantic stm.
	 * @param value
	 */
	public SemanticStm(String label)
	{
		this._label = label;
	}

	public SemanticStm()
	{

	}

	/**
	 * Instantiates a new semantic stm.
	 * @param semanticStm the semantic stm
	 */
	public SemanticStm(final SemanticStm semanticStm)
	{
		this._label = semanticStm._label;
		this._provider = semanticStm._provider;
	}

	/**
	 * @return cloned semantic statement
	 * @see java.lang.Object#clone()
	 */
	@Override
	public final SemanticStm clone()
	{
		try
		{
			SemanticStm clone = (SemanticStm) super.clone();
			if (this._label != null)
			{
				clone._label = new String(this._label);
			}
			if (this._provider != null)
			{
				clone._provider = new String(this._provider);
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
	 * @param sStm the s stm
	 * @return true, if successful
	 */
	public final boolean equals(final SemanticStm sStm)
	{
		if (this._provider != null && sStm._provider != null)
		{
			if (!this._provider.equals(sStm._provider))
			{
				return false;
			}
		}
		else if ((this._provider == null && sStm._provider != null)
				|| (this._provider != null && sStm._provider == null))
		{
			return false;
		}

		if (this._label != null && sStm._label != null)
		{
			if (!this._label.equals(sStm._label))
			{
				return false;
			}
		}
		else if ((this._label == null && sStm._label != null) || (this._label != null && sStm._label == null))
		{
			return false;
		}

		return true;

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
	 * Gets the provider.
	 * @return the provider
	 */
	public final String getProvider()
	{
		return _provider;
	}

	/**
	 * Checks if is valid.
	 * @return true, if is valid
	 */
	public final boolean isValid()
	{
		if (_label != null && _label.trim().length() > 0)
		{
			return true;
		}
		else
		{
			return false;
		}
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
	 * Sets the provider.
	 * @param provider the new provider
	 */
	public final void setProvider(final String provider)
	{
		this._provider = provider;
	}
}
