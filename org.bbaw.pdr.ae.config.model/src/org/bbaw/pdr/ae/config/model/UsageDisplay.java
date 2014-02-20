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
import java.util.Vector;

/**
 * The Class UsageDisplay corresponds to <display> in <usage> in dtdl XML.
 * @author Christoph Plutte
 */
public class UsageDisplay
{

	/** The _documentation. */
	private HashMap<String, String> _documentation = new HashMap<String, String>();

	/** The _person name tag. */
	private Vector<String> _personNameTag;

	/** The _person norm name tag. */
	private Vector<String> _personNormNameTag;

	/**
	 * Gets the documentation.
	 * @return the documentation
	 */
	public final HashMap<String, String> getDocumentation()
	{
		return _documentation;
	}

	/**
	 * Gets the person name tag.
	 * @return the person name tag
	 */
	public final Vector<String> getPersonNameTag()
	{
		return _personNameTag;
	}

	/**
	 * Gets the person norm name tag.
	 * @return the person norm name tag
	 */
	public final Vector<String> getPersonNormNameTag()
	{
		return _personNormNameTag;
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
	 * Sets the person name tag.
	 * @param personNameTag the new person name tag
	 */
	public final void setPersonNameTag(final Vector<String> personNameTag)
	{
		this._personNameTag = personNameTag;
	}

	/**
	 * Sets the person norm name tag.
	 * @param personNormNameTag the new person norm name tag
	 */
	public final void setPersonNormNameTag(final Vector<String> personNormNameTag)
	{
		this._personNormNameTag = personNormNameTag;
	}

}
