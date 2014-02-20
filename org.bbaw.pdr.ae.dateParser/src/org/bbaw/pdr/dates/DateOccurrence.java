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
package org.bbaw.pdr.dates;

/**
 * Enthält eine gefundene Datumsangabe.
 * @author Martin Fechner
 */
public class DateOccurrence
{
	//  VARIABLEN

	/** Die gefundene Datumsangabe aus dem Originaltext. */
	String originalText;
	/** Die Datumsangabe im ISO-Format. */
	String isoText;
	/** Die Startposition der Datumsangabe im Originaltext, beginnend mit 0. */
	int start;
	/** Die Länge der Datumsangabe im Originaltext. */
	int length;

	//  CONSTRUCTOR

	/** Der Konstruktur erstellt eine Datumsangabe im ISO-Format.
	 * @param st Startposition der Angabe im Originalstring.
	 * @param le Länge der Angabe im Originalstring.
	 * @param l1 Die Zeitraum, -punktbestimmung der ersten Datumsangabe.
	 * @param y1 Die erste Jahresangabe als vierstellige Zahl.
	 * @param m1 Die erste Monatsangabe als zweistellige Zahl.
	 * @param d1 Die erste Tagesangabe als zweistellige Zahl.
	 * @param l2 Die Zeitraum, -punktbestimmung der zweiten Datumsangabe.
	 * @param y2 Die zweite Jahresangabe als vierstellige Zahl.
	 * @param m2 Die zweite Monatsangabe als zweistellige Zahl.
	 * @param d2 Die zweite Tagesangabe als zweistellige Zahl.
	 * @param txt Der gefundene Originalstring.
	 */
	DateOccurrence(int st, int le, String l1, String y1, String m1, String d1,
			String l2, String y2, String m2, String d2, String txt){
		originalText = txt;
		start = st;
		length = le;
		// Schreibt die ISO-Angabe.
		// bei einer Datumsangabe:
		if (y2.isEmpty()){	
			if (!l1.isEmpty())
				isoText = l1; // mögliche Attribute "when", "from", "to", "notBefore", "notAfter"
			else
				isoText = "when";
			isoText += "=\"" + y1;	// etwa "1990-10-03"
			if (!m1.isEmpty()){
				isoText += "-" + m1;
				if (!d1.isEmpty())
					isoText += "-" + d1;
			}
			isoText += "\"";
		}
		// bei zwei Datumsangaben:
		else{
			if (!l1.isEmpty() && l1.equals("notBefore"))
				isoText = "notBefore=\""; // erste Angabe "notBefore"
			else
				isoText = "from=\""; // oder "from"
			isoText += y1;
			if (!m1.isEmpty()){
				isoText += "-" + m1;
				if (!d1.isEmpty())
					isoText += "-" + d1;
			}
			isoText += "\" ";
			if (!l2.isEmpty() && l2.equals("notAfter"))
				isoText += "notAfter=\""; // zweite Angabe "notBefore"
			else
				isoText += "to=\""; // oder "from"
			isoText += y2;
			if (!m2.isEmpty()){
				isoText += "-" + m2;
				if (!d2.isEmpty())
					isoText += "-" + d2;
			}
			isoText += "\"";
		}
	}
	/**
	 * Gibt die Position der Datumsangabe im Originalstring zurück.
	 * @return Der Offset der Angabe.
	 */
	public String getOffset(){
		return Integer.toString(start);
	}
	/**
	 * Gibt die Länge der Datumsangabe im Originalstring zurück. 
	 * @return Die Länge der Angabe.
	 */
	public String getLength(){
		return Integer.toString(length);
	}
	/**
	 * Gibt die Angabe im ISO-Format zurück.
	 * @return ISO-Format der Datumsangabe.
	 */
	public String getISO(){
		return isoText;
	}
	/**
	 * Gibt die gefundene Datumsangabe aus dem Original zurück.
	 * @return Die Datumsangabe aus dem Original.
	 */
	public String getFinding(){
		return originalText;
	}

}
