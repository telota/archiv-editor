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

import java.util.ArrayList;
import java.util.Vector;

/**
 * Kapselt die Ergebnisse des Versuchs innerhalb eines uebergebenen Strings Datumsinformationen auszuzeichnen.
 * Enthält weitere Variablen, die zur Auszeichnung notwendig sind, sowie Methoden zum Lesen und Schreiben
 * der Variablen.
 * @author Martin Fechner
 */

public class DatesResult{

	//  VARIABLEN

	/** Enthält den Originaltext. Bsp.: "am 3.10.1990" */
	private String[] originalText;
	/** Enthält den kodierten Text. Bsp.: "sAt_sSPAsD1_sPUNsD2_sPUNsD4_" */
	private String[] codedText;
	/** Enthält die Arraylänge einer erkannten Datumsangabe. Bsp.: 7 */
	private int[]	 identifiedLength;
	/** 
	 * Enthält die Bezeichnungen der Elemente einer erkannten Datumsangabe.
	 * Bsp.: GRENZE1, LEER, TAG1, TAG1, MONAT1, MONAT1, JAHR1 
	 */
	private DatesSymbol.Label[] identifiedText;
	/**
	 * Enthält die gefundenen Datumsangaben mit Detailangaben.
	 * Bsp.: start=0 length=12 isotext="when='1990-10-03'" originaltext="am 3.10.1990" 
	 */
	private ArrayList<DateOccurrence> identifiedDates = new ArrayList<DateOccurrence>();

	//  CONSTRUCTOR

	/**
	 * Konstruktor zerlegt den InputText in ein Array und initialisiert die Variablen.
	 * @param input Übergebener nach Datumsangaben zu durchsuchender String.
	 */
	public DatesResult(String input){
		String[] txt = input.split("(?=\\p{P}|\\s|\\b)");
		originalText = new String[txt.length-1];
		codedText = new String[txt.length-1];
		identifiedText = new DatesSymbol.Label[txt.length-1];
		identifiedLength = new int[txt.length-1];

		for (int i=1; i<txt.length; i++){
			originalText[i-1] = txt[i];
			codedText[i-1] = "s___";
			identifiedText[i-1] = DatesSymbol.Label.EMPTY;
			identifiedLength[i-1] = 0;
		}
	}
	
	//  AUSGABE/LESEN
	
	/**
	 *  Liefert die Länge des Arrays.
	 *  @return Länge des Arrays. 
	 */
	public int length(){
		return originalText.length;
	}
	/**
	 *  Hilfsfunktion zum Ausgeben von Teilen der Textarrays.
	 *  @param from Position des Startelements.
	 *  @param length Arraylänge des angefragten Strings.
	 *  @param txt Übergebenes Textarray.
	 *  @return Der angeforderte Teiltext.
	 */
	private String output(int from, int length, String[] txt){
		String result = "";
		for (int i=from; i<from+length && i<txt.length && i>=0; i++)
			result = result+txt[i];
		return result;
	}	
	/**
	 *  Gibt den vollständigen Originaltext zurück.
	 *  @return Der vollständige Originaltext.
	 */
	public String original(){
		return output(0, length(), originalText);
	}
	/**
	 *  Gibt nur Element i des Originaltextes zurück.
	 *  @param i Position des angeforderten Arrayelements.
	 *  @return Arrayelement i des Originaltextes.
	 */
	public String original(int i){
		return output(i, 1, originalText);
	}
	/**
	 *  Gibt den Originaltext von i bis i+j zurück.
	 *  @param i Startposition des angeforderten Teilarrays.
	 *  @param j Länge des angeforderten Teilarrays.
	 *  @return Teilarray des Originaltextes.
	 */
	String original(int i, int j){
		return output(i, j, originalText);
	}
	/**
	 *  Gibt den vollständigen kodierten Text zurück.
	 *  @return Der vollständige kodierte Text.
	 */
	String coded(){
		return output(0, length(), codedText);
	}
	/**
	 *  Gibt Element i des kodierten Textes zurück.
	 *  @param i Position des angefoderten Arrayelements.
	 *  @return Element i des kodierten Textes.
	 */
	public String coded(int i){
		return output(i, 1, codedText);
	}
	/**
	 *  Gibt Element i des erkannten Textes als EBez zurück.
	 *  @param i Position des angeforderten Arrayelements.
	 *  @return Element i des erkannten Textes als EBez.
	 */
	public DatesSymbol.Label identified(int i){
		DatesSymbol.Label result = DatesSymbol.Label.EMPTY;
		if (i>=0 && i<length())
			result = identifiedText[i];
		return result;
	}
	/**
	 *  Gibt die Arraylänge der erkannten Datumsangabe bei i zurück.
	 *  @param i Position des Arrayelements.
	 *  @return Die Arraylänge der erkannten Datumsangabe bei i.
	 */
	public int identifiedLength(int i){
		int result = 0;
		if (i>=0 && i<length())
			result = identifiedLength[i];
		return result;
	}
	/**
	 *  Gibt die erkannten Ergebnisse in der Form 
	 *  "offset= .. length= .. iso= .. found= .." zurück.
	 *  @return Eine Liste der erkannten Ergebnisse.
	 */
	String print(){
		String result = "";
		result += "pdrws-results\n";
		result += "pdrws-service=dates\n"; 
		result += String.format("pdrws-text=%1$s", original());
		DateOccurrence[] ea = new DateOccurrence[identifiedDates.size()];
//		result += String.format("results: %1$2s\n", ea.length);
		ea = identifiedDates.toArray(ea);
		for (int i=0; i<ea.length; i++){
			result += "\n";
	    	result += String.format(
	    			"pdrws-result={offset=%1$s; length=%2$s; iso={%3$s}; found=%4$s}",
	    			ea[i].start, ea[i].length, ea[i].isoText, ea[i].originalText);
	    }
		return result;
	}
	/**
	 *  Gibt einen Vektor auf die Ergebnisse zurück.
	 *  @return Ein Vektor auf die Ergebnisse.
	 */
	public Vector<DateOccurrence> getOccurrences(){
		Vector<DateOccurrence> v = new Vector<DateOccurrence>();
		DateOccurrence[] ea = new DateOccurrence[identifiedDates.size()];
		ea = identifiedDates.toArray(ea);
		for (int i=0; i<ea.length; i++){
			v.add(ea[i]);
		}
		return v;
	}

	/**
	 *  Gibt die Anzahl der erkannten Datumsangaben zurück
	 *  @return Die Anzahl der erkannten Datumsangaben.
	 */
	int numberOfResults(){
		return identifiedDates.size();
	}

	//  SCHREIBEN

	/**
	 *  Überschreibt das Element i des kodierten Textes.
	 *  @param i Position des zu überschreibenden Elements.
	 *  @param newCode Der neue Kode.
	 */
	void writeCodedText(int i, String newCode){
		if (i>=0 && i<length())
			codedText[i] = newCode;
	}
	/**
	 *  Überschreibt das Element i des erkannten Textes mit einem
	 *  Element vom Typ EBez.
	 *  @param i Position des zu überschreibenden Elements.
	 *  @param newLabel Der neue Bezeichner.
	 */
	void writeIdentifiedText(int i, DatesSymbol.Label newLabel){
		if (i>=0 && i<length())
			identifiedText[i] = newLabel;
	}
	/**
	 *  Hängt eine erkannte Datumsangabe an die Liste der Ergebnisse an.
	 *  @param newDate Das anzuhängende Ergebnis.
	 */
	void addIdentifiedDate(DateOccurrence newDate){
		identifiedDates.add(newDate);
	}
	/**
	 *  Schreibt die Arraylänge der gefundenen Datumsangabe bei i in
	 *  erkanntelaenge.
	 *  @param i Die Position des zu überschreibenden Elements.
	 *  @param newLength Die neue Länge.
	 */
	void writeIdentifiedLength(int i, int newLength){
		identifiedLength[i] = newLength;
	}
	
}