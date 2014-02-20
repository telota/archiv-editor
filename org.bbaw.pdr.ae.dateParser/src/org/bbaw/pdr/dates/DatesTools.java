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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *  DatesUtils enthält die statischen Methoden zur Datumserkennung.
 *  D.h. zur Kodierung des Originaltextes,
 *  zur Erkennung von Datumsangaben aus dem kodierten Text,
 *  zur Formatierung der erkannten Angaben ins ISO-Format.
 *  @author Martin Fechner
 */
class DatesTools
{
		
		//  METHODEN

		/**
		 * Schreibt das Kodesymbol in den kodierten Text bei i, wenn das Muster
		 * und der Originaltext übereinstimmen.
		 * Bsp.: "\\d" durch "sD1_"
		 * @param result Das zu bearbeitende DatesResult-Objekt.
		 * @param i Position des Elements.
		 * @param must Das Muster als regulärer Ausdruck.
		 * @param symb Das Kodesymbol.
		 */
		static void code(DatesResult result, int i, DatesConstant dconst){
			String regex = dconst.r();
			String symb = dconst.s();
			if (result.original(i).matches(regex))
				result.writeCodedText(i, symb);
		}

		/**
		 *  Sucht das Muster im Kode.
		 *  @param result Das zu bearbeitende DatesResult-Objekt.
		 *  @param p Das im kodierten Text zu suchende Pattern.
		 *  @return Gibt an, ob das Muster gefunden wurde.
		 */
		static boolean findPattern(DatesResult result, DatesSymbol dsymb){
			boolean identified = false;
			Pattern p = dsymb.p();
			Matcher m = p.matcher(result.coded());
			while (m.find()){
				int start = m.start()/4;
				int length = (m.end()/4-m.start()/4);
				if (result.identifiedLength(start)<length){
					identified = true;
				}
			}
			return identified;
		}

		/**
		 *  Sucht das Muster im Kode, und schreibt Länge und Bezeichner
		 *  in ErkannterText.
		 *  @param result Das zu bearbeitende DatesResult-Objekt.
		 *  @param p Das im kodierten Text zu suchende Pattern.
		 *  @param bez Das Array enthält die Bezeichner.
		 *  @return Gibt an, ob das Muster gefunden und markiert wurde.
		 */
		static boolean identifyDate(DatesResult result, DatesSymbol dsymb){
			boolean identified = false;
			Pattern p = dsymb.p();
			DatesSymbol.Label[] bez = dsymb.l();
			Matcher m = p.matcher(result.coded());
			while (m.find()){
				int start = m.start()/4;
				int length = (m.end()/4-m.start()/4);
				if (result.identifiedLength(start)<length){
					identified = true;
					for (int i=start; i<start+length; i++){
						result.writeIdentifiedLength(i, length);
						result.writeIdentifiedText(i, bez[i-start]);	
					}
				}
			}
			return identified;
		}
		
		/**
		 *  Sucht das Muster vor schon erkannten Angaben und kombiniert
		 *  Muster und Angabe zu einem langen Datum.
		 *  @param result Das zu bearbeitende DatesResult-Objekt.
		 *  @param p Das im kodierten Text zu suchende Pattern.
		 *  @param bez Das Array enthält die Bezeichner.
		 *  @return Gibt an, ob das Muster gefunden und markiert wurde.
		 */
		static boolean identifyPrePhrase(DatesResult result, DatesSymbol dsymb){
			boolean identified = false;
			Pattern p = dsymb.p();
			DatesSymbol.Label[] bez = dsymb.l();
			Matcher m = p.matcher(result.coded());
			while (m.find()){
				int start = m.start()/4;
				int length = (m.end()/4-m.start()/4);
				if (result.identifiedLength(start)==0 &&
						result.identifiedLength(start+length-1)==0 &&
						result.identifiedLength(start+length)!=0){
					identified = true;
					int newlength = length + result.identifiedLength(start+length);
					for (int i=start; i<start+newlength; i++){
						result.writeIdentifiedLength(i, newlength);
						if (i< start+length)
							result.writeIdentifiedText(i, bez[i-start]);	
					}
				}						
			}
			return identified;
		}
		
		/**
		 *  Sucht das Muster zwischen zwei erkannten Angaben und kombiniert
		 *  die drei zu einer langen Angabe.
		 *  @param result Das zu bearbeitende DatesResult-Objekt.
		 *  @param p Das im kodierten Text zu suchende Pattern.
		 *  @param bez Das Array enthält die Bezeichner.
		 *  @return Gibt an, ob das Muster gefunden und markiert wurde.
		 */
		static boolean identifyConnection(DatesResult result, DatesSymbol dsymb){
			boolean identified = false;
			Pattern p = dsymb.p();
			DatesSymbol.Label[] label = dsymb.l();
			Matcher m = p.matcher(result.coded());
			while (m.find()){
				int start = m.start()/4;
				int length = (m.end()/4-m.start()/4);
				if (result.identifiedLength(start-1)!=0 && result.identifiedLength(start)==0 &&
						result.identifiedLength(start+length)!=0){
					identified = true;
					int newLength = length + result.identifiedLength(start-1) +
									result.identifiedLength(start+length);
					int newStart = start - result.identifiedLength(start-1);
					for (int i=newStart; i<newStart+newLength; i++){
						result.writeIdentifiedLength(i, newLength);
						if (i>=start && i<start+length)
							result.writeIdentifiedText(i, label[i-start]);
						if (i>=start+length){
							// Bezeichner der zweiten Datumsangabe
							// ändern: Jahr1 zu Jahr2
							DatesSymbol.Label newLabel;
							switch (result.identified(i)){
							case YEAR1:
								newLabel = DatesSymbol.Label.YEAR2;
								break;
							case MONTH1:
								newLabel = DatesSymbol.Label.MONTH2;
								break;
							case DAY1:
								newLabel = DatesSymbol.Label.DAY2;
								break;
							case APPROXIMATION1:
								newLabel = DatesSymbol.Label.APPROXIMATION2;
								break;
							case LIMIT1:
								newLabel = DatesSymbol.Label.LIMIT2;
								break;
							default:
								newLabel = DatesSymbol.Label.EMPTY;
								break;
							}
							result.writeIdentifiedText(i, newLabel);
						}
					}
				}
			}
			return identified;
		}

		/**
		 *  Sucht das Muster für eine unvollständige Datumsangabe vor 
		 *  erkannten Angaben und kombiniert beide. Bsp.: "3.-" vor "13.10.1990"
		 *  @param result Das zu bearbeitende DatesResult-Objekt.
		 *  @param p Das im kodierten Text zu suchende Pattern.
		 *  @param bez Das Array enthält die Bezeichner.
		 *  @return Gibt an, ob das Muster gefunden und markiert wurde.
		 */
		static boolean identifyPreDate(DatesResult result, DatesSymbol dsymb){
			boolean identified = false;
			Pattern p = dsymb.p();
			DatesSymbol.Label[] label = dsymb.l();
			Matcher m = p.matcher(result.coded());
			while (m.find()){
				int start = m.start()/4;
				int length = (m.end()/4-m.start()/4);
				if (result.identifiedLength(start)==0 &&
						result.identifiedLength(start+length)!=0){
					identified = true;
					int newLength = length + result.identifiedLength(start+length);
					for (int i=start; i<start+newLength; i++){
						result.writeIdentifiedLength(i, newLength);
						if (i< start + length)
							result.writeIdentifiedText(i, label[i-start]);
						if (i>= start + length){
							// Ändert die Bezeichner der zweiten Datumsangabe
							DatesSymbol.Label newLabel;
							switch (result.identified(i)){
							case YEAR1:
								newLabel = DatesSymbol.Label.YEAR2;
								break;
							case MONTH1:
								newLabel = DatesSymbol.Label.MONTH2;
								break;
							case DAY1:
								newLabel = DatesSymbol.Label.DAY2;
								break;
							case APPROXIMATION1:
								newLabel = DatesSymbol.Label.APPROXIMATION2;
								break;
							case LIMIT1:
								newLabel = DatesSymbol.Label.LIMIT2;
								break;
							default:
								newLabel = DatesSymbol.Label.EMPTY;
								break;
							}
							result.writeIdentifiedText(i, newLabel);
						}
					}
				}
			}
			return identified;
		}
		
		/**
		 *  Sucht das Muster für eine unvollständige Datumsangabe nach
		 *  erkannten Angaben und kombiniert beide. Bsp.: "1990" und "-99"
		 *  @param result Das zu bearbeitende DatesResult-Objekt.
		 *  @param p Das im kodierten Text zu suchende Pattern.
		 *  @param bez Das Array enthält die Bezeichner.
		 *  @return Gibt an, ob das Muster gefunden und markiert wurde.
		 */
		static boolean identifyAfterDate(DatesResult result, DatesSymbol dsymb){
			boolean identified = false;
			Pattern p = dsymb.p();
			DatesSymbol.Label[] label = dsymb.l();
			Matcher m = p.matcher(result.coded());
			while (m.find()){
				int start = m.start()/4;
				int length = (m.end()/4-m.start()/4);
				if (result.identifiedLength(start-1)!=0
						&& result.identified(start-1).equals(DatesSymbol.Label.YEAR1)
						&& result.identifiedLength(start)==0){
					identified = true;
					int newLength = result.identifiedLength(start-1) + length;
					int newStart = start - result.identifiedLength(start-1);
					for (int i=newStart; i<newStart+newLength; i++){
						result.writeIdentifiedLength(i, newLength);
						if (i>=start){
							result.writeIdentifiedText(i, label[i-start]);
						}
					}
				}			
			}
			return identified;
		}

		
		/** 
		 * Berechnet das Osterdatum in einem bestimmten Jahr.
		 * @param year Das Jahr, in dem Ostern berechnet werden soll.
		 * @return Der Tag, an dem Ostersonntag liegt, vom 1. März ausgerechnet.
		 */
		static int easterDate(String year)
		{
			int X = Integer.parseInt(year);
			int K,M,S,A,D,R,OG,SZ,OE,OS;
			
			 K = X/100; 
			 M = 15 + ((3*K+3)/4) - ((8*K+13)/25); 
			 S = 2 - ((3*K+3)/4); 
			 A = X%19; 
			 D = (19*A+M)%30; 
			 R = (D/29) + ((D/28) - (D/ 29)) * (A/11); 
			 OG = 21 + D - R;
			 SZ = 7 - (X+(X/4)+S)%7;
			 OE = 7 - (OG-SZ)%7;
			 OS = OG+OE; 
			return OS;
		}		
		
		/**
		 * Berechnet, ob es sich bei dem angegeben Jahr um ein Schaltjahr handelt.
		 */
		static boolean isLeapYear(String year)
		{
			boolean leapYear = false;
			int y = Integer.parseInt(year);
			if ( ( (y%4)==0 && (y%100)!=0 ) ||
				 ( (y%4)==0 && (y%400)==0 ) )
					leapYear = true;
			return leapYear;
		}
		
	}
