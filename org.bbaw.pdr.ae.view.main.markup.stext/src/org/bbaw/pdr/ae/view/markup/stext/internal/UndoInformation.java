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
package org.bbaw.pdr.ae.view.markup.stext.internal;

import java.util.LinkedList;

import org.bbaw.pdr.ae.model.TaggingRange;

/**
 * Informationen fuer das Rueckgaengigmachen und Wiederherstellen
 * @author Christoph Plutte
 */
public class UndoInformation
{
	/** Text-Aenderung */
	public static final int TYPE_TEXT = 2354;
	/** Format-Aenderung */
	public static final int TYPE_FORMAT = 2355;

	/** Art der Aenderung */
	private int _type;
	/** Ggf. ersetzter Text */
	private String _replacedText;
	
	private LinkedList<TaggingRange> _replacedRanges;
	
	/**
	 * Konstruktor fuer Aenderungen am Text.
	 * @param replacedText Ersetzter Text
	 * @param startNewText Index des neuen Textes
	 * @param lengthNewText Laenge des neuen Textes
	 */
	public UndoInformation(final String replacedText, final LinkedList<TaggingRange> linkedList)
	{
		_type = TYPE_TEXT;
		_replacedText = new String(replacedText);
		_replacedRanges = new LinkedList<TaggingRange>();
		if (linkedList != null)
		{
			_replacedRanges = new LinkedList<TaggingRange>();
			for (int i = 0; i < linkedList.size(); i++)
			{
				_replacedRanges.add(linkedList.get(i).clone());
			}
		}
	}
	
	

	/**
	 * Liefert den durch die Aenderung ersetzten Text.
	 * @return Text
	 */
	public String getReplacedText()
	{
		return _replacedText;
	}

	
	
	

	/**
	 * Die durchgefuehrte Aenderung betraf der Text.
	 */ 
	public boolean isModifiedText()
	{
		return _type == TYPE_TEXT;
	}
	
	/**
	 * Die durchgefuehrte Aenderung betraf eine Formatierung.
	 */ 
	public boolean isModifiedFormat()
	{
		return _type == TYPE_FORMAT;
	}

	/**
	 * Liefert einen String mit allen enthaltenen Informatioen.
	 */
	public String toString()
	{
		return _replacedText;
	}

	public LinkedList<TaggingRange> getReplacedRanges()
	{
		return _replacedRanges;
	}

	public void setReplacedRanges(LinkedList<TaggingRange> _replacedRanges)
	{
		this._replacedRanges = _replacedRanges;
	}
	
}
