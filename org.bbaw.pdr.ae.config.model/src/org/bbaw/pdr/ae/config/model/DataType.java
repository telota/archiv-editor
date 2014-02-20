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

/**
 * The Class DataType corresponds to <datatype> in dtdl XML.
 * @author Christoph Plutte
 */
public class DataType extends ConfigData
{

	/** The _is tree. */
	private boolean _isTree;

	/** The _datatype desc. */
	private DatatypeDesc _datatypeDesc;

	/**
	 * Gets the datatype desc.
	 * @return the datatype desc
	 */
	public final DatatypeDesc getDatatypeDesc()
	{
		return _datatypeDesc;
	}

	/**
	 * Checks if is tree.
	 * @return true, if is tree
	 */
	public final boolean isTree()
	{
		return _isTree;
	}

	/**
	 * Sets the datatype desc.
	 * @param datatypeDesc the new datatype desc
	 */
	public final void setDatatypeDesc(final DatatypeDesc datatypeDesc)
	{
		this._datatypeDesc = datatypeDesc;
	}

	/**
	 * Sets the tree.
	 * @param isTree the new tree
	 */
	public final void setTree(final boolean isTree)
	{
		this._isTree = isTree;
	}

}
