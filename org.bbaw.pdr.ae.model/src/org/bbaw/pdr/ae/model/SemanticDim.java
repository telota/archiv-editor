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
 * The Class SemanticDim.
 * @author Christoph Plutte
 */
public class SemanticDim implements Cloneable
{

	/** The _semantic stms. */
	private Vector<SemanticStm> _semanticStms;

	/**
	 * Instantiates a new semantic dim.
	 */
	public SemanticDim()
	{

	}

	/**
	 * Instantiates a new semantic dim.
	 * @param semanticDim the semantic dim
	 */
	public SemanticDim(final SemanticDim semanticDim)
	{
		this._semanticStms = semanticDim.getSemanticStms();
	}

	/**
	 * @return clooned semantic dimension
	 * @see java.lang.Object#clone()
	 */
	@Override
	public final SemanticDim clone()
	{
		try
		{
			SemanticDim clone = (SemanticDim) super.clone();
			if (this._semanticStms != null)
			{
				clone._semanticStms = new Vector<SemanticStm>(this._semanticStms.size());
				for (int i = 0; i < this._semanticStms.size(); i++)
				{
					clone._semanticStms.add(this._semanticStms.get(i).clone());
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
	 * @param sDim the s dim
	 * @return true, if successful
	 */
	public final boolean equals(final SemanticDim sDim)
	{
		if (this.getSemanticStms() != null && sDim.getSemanticStms() != null)
		{
			if (!(this.getSemanticStms().size() == sDim.getSemanticStms().size()))
			{
				return false;
			}
			for (int i = 0; i < this.getSemanticStms().size(); i++)
			{
				if (!this.getSemanticStms().get(i).equals(sDim.getSemanticStms().get(i)))
				{
					return false;
				}
			}
		}
		else if ((this.getSemanticStms() == null && sDim.getSemanticStms() != null)
				|| (this.getSemanticStms() != null && sDim.getSemanticStms() == null))
		{
			return false;
		}
		return true;

	}

	/**
	 * Gets the semantic label by provider.
	 * @param provider the provider
	 * @return the semantic label by provider
	 */
	public final Vector<String> getSemanticLabelByProvider(final String provider)
	{
		Vector<String> labels = new Vector<String>();
		if (_semanticStms != null)
		{
			for (int i = 0; i < _semanticStms.size(); i++)
			{
				if (_semanticStms.get(i).getProvider() != null && _semanticStms.get(i).getProvider().equals(provider)
						&& !labels.contains(_semanticStms.get(i).getLabel()))
				{
					labels.add(_semanticStms.get(i).getLabel());
				}
			}
		}
		return labels;

	}

	/**
	 * Gets the semantic providers.
	 * @return the semantic providers
	 */
	public final Vector<String> getSemanticProviders()
	{
		Vector<String> providers = new Vector<String>(2);
		if (_semanticStms != null)
		{
			for (int i = 0; i < _semanticStms.size(); i++)
			{
				if (_semanticStms.get(i).getProvider() != null
						&& !providers.contains(_semanticStms.get(i).getProvider()))
				{
					providers.add(_semanticStms.get(i).getProvider());
				}
			}
		}
		return providers;
	}

	/**
	 * Gets the semantic stms.
	 * @return the semantic stms
	 */
	public final Vector<SemanticStm> getSemanticStms()
	{
		if (_semanticStms == null)
		{
			_semanticStms = new Vector<SemanticStm>(2);
		}
		return _semanticStms;
	}

	/**
	 * Checks if is valid.
	 * @return true, if is valid
	 */
	public final boolean isValid()
	{
		if (_semanticStms != null)
		{
			for (SemanticStm s : _semanticStms)
			{
				if (!s.isValid())
				{
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Removes the.
	 * @param index the index
	 * @return true, if successful
	 */
	public final boolean remove(final int index)
	{
		if (_semanticStms != null)
		{
			_semanticStms.removeElementAt(index);
		}
		return true;
	}

	/**
	 * Sets the semantic stms.
	 * @param semanticStms the new semantic stms
	 */
	public final void setSemanticStms(final Vector<SemanticStm> semanticStms)
	{
		this._semanticStms = semanticStms;
	}
}
