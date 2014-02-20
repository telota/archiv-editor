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


public class SemanticTemplate extends ConfigData
{


	private String _templateText;

	private boolean _ignoreTemplateText;

	private boolean _isAllowMultiple;



	public SemanticTemplate()
	{
	}

	public SemanticTemplate(String semantic)
	{
		setValue(semantic);
	}



	public String getTemplateText()
	{
		return _templateText;
	}

	public void setTemplateText(String _templateText)
	{
		this._templateText = _templateText;
	}

	public boolean isIgnoreTemplateText()
	{
		return _ignoreTemplateText;
	}

	public void setIgnoreTemplateText(boolean _ignoreTemplateText)
	{
		this._ignoreTemplateText = _ignoreTemplateText;
	}

	public boolean isAllowMultiple() {
		return this._isAllowMultiple;
	}

	public void setAllowMultiple(boolean allowMultiple) {
		this._isAllowMultiple = allowMultiple;
	}



}
