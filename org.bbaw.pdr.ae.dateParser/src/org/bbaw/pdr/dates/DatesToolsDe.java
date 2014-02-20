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

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 *  DatesUtils enthält die statischen Methoden zur Datumserkennung.
 *  D.h. zur Kodierung des Originaltextes,
 *  zur Erkennung von Datumsangaben aus dem kodierten Text,
 *  zur Formatierung der erkannten Angaben ins ISO-Format.
 *  @author Martin Fechner
 */
class DatesToolsDe extends DatesTools
{
		
		//  METHODEN

		/**
		 *  Kodiert den Originaltext durch die aufgeführten Kodes
		 *  und schreibt den Kode in den kodierten Text.
		 *  @param result Das zu bearbeitende DatesResult-Objekt.
		 */
		private static void code(DatesResult result){
			for (int i=0; i<result.length(); i++){
				code(result, i, DatesConstantsDe.word); // muss vorne stehen,
						// da bestimmte worte nochmal neu kodiert werden.
				code(result, i, DatesConstantsDe.space);
				code(result, i, DatesConstantsDe.miscellaneous);
				code(result, i, DatesConstantsDe.other);
				code(result, i, DatesConstantsDe.punctuation);
				code(result, i, DatesConstantsDe.d); // muss vor den anderen Zahlen
						// stehen, da die nochmal neu kodiert werden.
				// Zahlen
				code(result, i, DatesConstantsDe.d1);
				code(result, i, DatesConstantsDe.d2);
				code(result, i, DatesConstantsDe.d4);
				// Zahlworte
				code(result, i, DatesConstantsDe.cardinal01);
				code(result, i, DatesConstantsDe.cardinal02);
				code(result, i, DatesConstantsDe.cardinal03);
				code(result, i, DatesConstantsDe.cardinal04);
				code(result, i, DatesConstantsDe.cardinal05);
				code(result, i, DatesConstantsDe.cardinal06);
				code(result, i, DatesConstantsDe.cardinal07);
				// spezielle Worte
				code(result, i, DatesConstantsDe.from);
				code(result, i, DatesConstantsDe.at);
				code(result, i, DatesConstantsDe.notBefore);
				code(result, i, DatesConstantsDe.notAfter);
				code(result, i, DatesConstantsDe.around);
				code(result, i, DatesConstantsDe.article);
				code(result, i, DatesConstantsDe.link);
				code(result, i, DatesConstantsDe.to);
				code(result, i, DatesConstantsDe.approximation);
				code(result, i, DatesConstantsDe.half);
				code(result, i, DatesConstantsDe.between);
				code(result, i, DatesConstantsDe.and);
				// Namen
				code(result, i, DatesConstantsDe.saint);
				code(result, i, DatesConstantsDe.Jesus);
				code(result, i, DatesConstantsDe.maria);
				code(result, i, DatesConstantsDe.silvester);
				// Monate
				code(result, i, DatesConstantsDe.month01);
				code(result, i, DatesConstantsDe.month02);
				code(result, i, DatesConstantsDe.month03);
				code(result, i, DatesConstantsDe.month04);
				code(result, i, DatesConstantsDe.month05);
				code(result, i, DatesConstantsDe.month06);
				code(result, i, DatesConstantsDe.month07);
				code(result, i, DatesConstantsDe.month08);
				code(result, i, DatesConstantsDe.month09);
				code(result, i, DatesConstantsDe.month10);
				code(result, i, DatesConstantsDe.month11);
				code(result, i, DatesConstantsDe.month12);
				// Jahreszeiten
				code(result, i, DatesConstantsDe.spring);
				code(result, i, DatesConstantsDe.summer);
				code(result, i, DatesConstantsDe.autumn);
				code(result, i, DatesConstantsDe.winter);
				// Feste
				code(result, i, DatesConstantsDe.newYear);
				code(result, i, DatesConstantsDe.epiphany);
				code(result, i, DatesConstantsDe.stValetinesDay);
				code(result, i, DatesConstantsDe.allHallows);
				code(result, i, DatesConstantsDe.allSouls);
				code(result, i, DatesConstantsDe.immaculateConception);
				code(result, i, DatesConstantsDe.christmasEve);
				code(result, i, DatesConstantsDe.christmas);
				code(result, i, DatesConstantsDe.christmasDay);
				code(result, i, DatesConstantsDe.stStephansDay);
				// Feiertage
				code(result, i, DatesConstantsDe.carnivalThursday);
				code(result, i, DatesConstantsDe.carnivalMonday);
				code(result, i, DatesConstantsDe.carnivalTuesday);
				code(result, i, DatesConstantsDe.carnivalWednesday);
				code(result, i, DatesConstantsDe.palmSunday);
				code(result, i, DatesConstantsDe.holySaturday);
				code(result, i, DatesConstantsDe.holyThursday);
				code(result, i, DatesConstantsDe.holyFriday);
				code(result, i, DatesConstantsDe.Easter);
				code(result, i, DatesConstantsDe.easterMonday);
				code(result, i, DatesConstantsDe.ascension);
				code(result, i, DatesConstantsDe.pentecost);
				code(result, i, DatesConstantsDe.pentecostMonday);
				code(result, i, DatesConstantsDe.corpusChristi);
				// Jahrhundert
				code(result, i, DatesConstantsDe.century);
			}
		}

		/**
		 *  Erkennt die aufgeführten Muster für Datumsangaben im Kode
		 *  und schreibt die passenden Bezeichner in erkannterText.
		 *  @param result Das zu bearbeitende DatesResult-Objekt.
		 */
		private static void identify(DatesResult result){
			// zuerst einfache Datumsangaben
			identifyDate(result, DatesSymbolsDe.dd_monthp_yyyy);
			identifyDate(result, DatesSymbolsDe.dd_month_yyyy);
			identifyDate(result, DatesSymbolsDe.dd_mm_yyyy);
			identifyDate(result, DatesSymbolsDe.monthp_yyyy);
			identifyDate(result, DatesSymbolsDe.month_yyyy);
			identifyDate(result, DatesSymbolsDe.mmpyyyy);
			identifyDate(result, DatesSymbolsDe.yyyy);
			identifyDate(result, DatesSymbolsDe.season_yyyy);
			identifyDate(result, DatesSymbolsDe.holyday_yyyy);
			identifyDate(result, DatesSymbolsDe.st_cardinal_epiphany_yyyy);
			identifyDate(result, DatesSymbolsDe.maria_assumption_yyyy);
			identifyDate(result, DatesSymbolsDe.immacolate_conception_yyyy);
			identifyDate(result, DatesSymbolsDe.maria_conception_yyyy);
			identifyDate(result, DatesSymbolsDe.np_christmasday_yyyy);
			identifyDate(result, DatesSymbolsDe.jesus_ascension_yyyy);
			identifyDate(result, DatesSymbolsDe.yyp_century);
			identifyDate(result, DatesSymbolsDe.yyp_centuryp);
			// mögliche Angaben vor einfachen Datumsangaben
			identifyPrePhrase(result, DatesSymbolsDe.np_half_);
			identifyPrePhrase(result, DatesSymbolsDe.np_half_art_);
			identifyPrePhrase(result, DatesSymbolsDe.approx_);
			identifyPrePhrase(result, DatesSymbolsDe.approx_art_);
			identifyPrePhrase(result, DatesSymbolsDe.approxp_art_);
			identifyPrePhrase(result, DatesSymbolsDe.limit_);
			identifyPrePhrase(result, DatesSymbolsDe.limitp_);
			identifyPrePhrase(result, DatesSymbolsDe.limit_art_);
			// zwei zusammengehörige Datumsangaben
			if(findPattern(result, DatesSymbolsDe.between_)&&
					findPattern(result, DatesSymbolsDe.and_)){
				identifyPrePhrase(result, DatesSymbolsDe.between_);
				identifyConnection(result, DatesSymbolsDe.and_);
			}
			identifyConnection(result, DatesSymbolsDe.connect);
			identifyConnection(result, DatesSymbolsDe.connect_);
			identifyConnection(result, DatesSymbolsDe.connect_to_);
			identifyPreDate(result, DatesSymbolsDe.dd_mm_connect);
			identifyPreDate(result, DatesSymbolsDe.dd_mm_connect_);
			identifyPreDate(result, DatesSymbolsDe.dd_month_connect_);
			identifyPreDate(result, DatesSymbolsDe.dd_monthp_connect_);
			identifyPreDate(result, DatesSymbolsDe.month_connect_);
			identifyPreDate(result, DatesSymbolsDe.monthp_connect_);
			identifyPreDate(result, DatesSymbolsDe.dd_connect); // muss hinter DD_MM_CONNECT stehen
			identifyPreDate(result, DatesSymbolsDe.dd_connect_); // 
			identifyAfterDate(result, DatesSymbolsDe.connect_yy);
			// mögliche Angaben vor den kombinierten Datumsangaben
			identifyPrePhrase(result, DatesSymbolsDe.approx_);
			identifyPrePhrase(result, DatesSymbolsDe.approxp_);
			identifyPrePhrase(result, DatesSymbolsDe.limit_);
			identifyPrePhrase(result, DatesSymbolsDe.limitp_);
			identifyPrePhrase(result, DatesSymbolsDe.limit_art_);
			identifyPrePhrase(result, DatesSymbolsDe.to_);
			// Angaben im ISO-Format
			identifyDate(result, DatesSymbolsDe.yyyy_mm_dd);
		}
		
		/**
		 *  Fügt das erkannte Datum, das bei 'i' startet, im richtigen Format
		 *  der Ergebnisliste "erkannteDaten" hinzu.
		 *  @param result Das zu bearbeitende DatesResult-Objekt.
		 *  @param i Startelement des erkannten Datums.
		 */
		private static void formatDate_de(DatesResult result, int i){
			// ermittelt die Startposition der Angabe im Originalstring.
			int start = 0;
			for (int j=0; j<i; j++){
				start += result.original(j).length();
			}
			// initialisiert die notwendigen Angaben für eine neue Datumsangabe.
			int length = 0;
			boolean isDate = true;
			String originalText = "";
			String limit1 = "";
			String limit2 = "";
			String limit1_old = "";
			String year1 = "";
			String year2 = "";
			String month1 = "";
			String month2 = "";
			String day1 = "";
			String day2 = "";
			String holyday1 = "";
			String holyday2 = "";
			String easter1 = "";
			String easter2 = "";
			String century1 = "";
			String century2 = "";
			String approximation1 = "";
			String approximation2 = "";
			boolean circa1 = false;
			boolean circa2 = false;
			
			// geht die Bezeichner des erkannten Datums durch und 
			// übernimmt die Angaben im richtigen Format
			for (int j=0; j<result.identifiedLength(i); j++){
				// erhöht die Länge der Angabe
				length += result.original(i+j).length();
				// übernimmt den Originaltext
				originalText += result.original(i+j);
				// unterscheidet die Angaben durch den Bezeichner 
				DatesSymbol.Label label = result.identified(i+j);
				switch (label){
				case LIMIT1:
					if (result.coded(i+j).equals(DatesConstantsDe.from.s()))
						limit1 = "from";
					if (result.coded(i+j).equals(DatesConstantsDe.at.s()))
						limit1 = "when";
					if (result.coded(i+j).equals(DatesConstantsDe.to.s()))
						limit1 = "to";
					if (result.coded(i+j).equals(DatesConstantsDe.notBefore.s()))
						limit1 = "notBefore";
					if (result.coded(i+j).equals(DatesConstantsDe.notAfter.s()))
						limit1 = "notAfter";
					if (result.coded(i+j).equals(DatesConstantsDe.around.s()))
						circa1 = true;
					break;
				case YEAR1:
					if (result.coded(i+j).equals(DatesConstantsDe.d4.s()))
					{
						year1 = result.original(i+j);
						if (Integer.parseInt(year1)>2100)
							isDate = false;
						if (Integer.parseInt(year1)<1582)
							isDate = false;
					}
					// für bewegliche Feiertage mit Bezug zu Ostern
					if (!easter1.isEmpty())
					{
						int holydayDate = easterDate(year1) + Integer.parseInt(easter1);
						int hDay=1;
						if (holydayDate <1)
						{
							month1 = "02";
							hDay = holydayDate + 28;
							if (isLeapYear(year1))
								hDay = hDay + 1;
							if (hDay<1)
							{
								month1 = "01";
								hDay = hDay + 31;
							}
						}		
						else if (holydayDate <= 31)
				        {
				        	month1 = "03";
				        	hDay = holydayDate;
				        }
				        else if (holydayDate <= 31+30)
				        {
				        	month1= "04";
				        	hDay = holydayDate-31;
				        }
				        else if (holydayDate <= 31+30+31)
				        {
				        	month1= "05";
				        	hDay = holydayDate-31-30;
				        }
				        else if (holydayDate <= 31+30+31+30)
				        {
				        	month1= "06";
				        	hDay = holydayDate-31-30-31;
				        }
				        if (hDay<10)
				        	day1 = "0" + String.valueOf(hDay);
				        else
				        	day1 = String.valueOf(hDay);
				        approximation1 = "";
					}
					// Für Näherungsangaben
					if (!approximation1.isEmpty())
					{
						if (!limit1.isEmpty())
							limit1_old = limit1;
						if (approximation1.equals("1/2"))
						{
							limit1 = "notBefore";
							month1 = "01";
							limit2 = "notAfter";
							month2 = "06";
							year2 = year1;
						}
						if (approximation1.equals("2/2"))
						{
							limit1 = "notBefore";
							month1 = "07";
							limit2 = "notAfter";
							month2 = "12";
							year2 = year1;
						}
						if (approximation1.equals("1/3"))
						{
							limit1 = "notBefore";
							month1 = "01";
							limit2 = "notAfter";
							month2 = "04";
							year2 = year1;
						}
						if (approximation1.equals("2/3"))
						{
							limit1 = "notBefore";
							month1 = "05";
							limit2 = "notAfter";
							month2 = "08";
							year2 = year1;
						}
						if (approximation1.equals("3/3"))
						{
							limit1 = "notBefore";
							month1 = "09";
							limit2 = "notAfter";
							month2 = "12";
							year2 = year1;
						}
						// falls schon Zeitraumangaben vorhanden waren
						if (limit1_old.equals("from"))
						{
							limit2 = "";
							month2 = "";
							year2 = "";
						}
						if (limit1_old.equals("notBefore"))
						{
							limit2 = "";
							month2 = "";
							year2 = "";
						}
						if (limit1_old.equals("to"))
						{
							limit1 = limit2;
							month1 = month2;
							limit2 = "";
							month2 = "";
							year2 = "";
						}
						if (limit1_old.equals("notAfter"))
						{
							limit1 = limit2;
							month1 = month2;
							limit2 = "";
							month2 = "";
							year2 = "";
						}
						approximation1 = "";							
					}
					
					break;
				case MONTH1:
					if (result.coded(i+j).equals(DatesConstantsDe.d1.s()))
						month1 = "0" + result.original(i+j);
					if (result.coded(i+j).equals(DatesConstantsDe.d2.s()))
					{
						month1 = result.original(i+j);
						if (Integer.parseInt(month1)>12)
							isDate = false;
					}
					if (result.coded(i+j).equals(DatesConstantsDe.month01.s()))
						month1 = "01";
					if (result.coded(i+j).equals(DatesConstantsDe.month02.s()))
						month1 = "02";
					if (result.coded(i+j).equals(DatesConstantsDe.month03.s()))
						month1 = "03";
					if (result.coded(i+j).equals(DatesConstantsDe.month04.s()))
						month1 = "04";
					if (result.coded(i+j).equals(DatesConstantsDe.month05.s()))
						month1 = "05";
					if (result.coded(i+j).equals(DatesConstantsDe.month06.s()))
						month1 = "06";
					if (result.coded(i+j).equals(DatesConstantsDe.month07.s()))
						month1 = "07";
					if (result.coded(i+j).equals(DatesConstantsDe.month08.s()))
						month1 = "08";
					if (result.coded(i+j).equals(DatesConstantsDe.month09.s()))
						month1 = "09";
					if (result.coded(i+j).equals(DatesConstantsDe.month10.s()))
						month1 = "10";
					if (result.coded(i+j).equals(DatesConstantsDe.month11.s()))
						month1 = "11";
					if (result.coded(i+j).equals(DatesConstantsDe.month12.s()))
						month1 = "12";
					if (!approximation1.isEmpty())
					{
						if (!limit1.isEmpty())
							limit1_old = limit1;
						if (approximation1.equals("1/2"))
						{
							limit1 = "notBefore";
							day1 = "01";
							limit2 = "notAfter";
							day2 = "15";
							month2 = month1;
						}
						if (approximation1.equals("2/2"))
						{
							limit1 = "notBefore";
							day1 = "16";
							limit2 = "notAfter";
							if (month1.equals("01")||month1.equals("03")||month1.equals("05")||
									month1.equals("07")||month1.equals("08")||month1.equals("10")||
									month1.equals("12"))
								day2 = "31";
							if (month1.equals("02")||month1.equals("04")||month1.equals("06")||
									month1.equals("09")||month1.equals("11"))
								day2 = "30";
							month2 = month1;
						}
						if (approximation1.equals("1/3"))
						{
							limit1 = "notBefore";
							day1 = "01";
							limit2 = "notAfter";
							day2 = "10";
							month2 = month1;
						}
						if (approximation1.equals("2/3"))
						{
							limit1 = "notBefore";
							day1 = "11";
							limit2 = "notAfter";
							day2 = "20";
							month2 = month1;
						}
						if (approximation1.equals("3/3"))
						{
							limit1 = "notBefore";
							day1 = "21";
							limit2 = "notAfter";
							if (month1.equals("01")||month1.equals("03")||month1.equals("05")||
									month1.equals("07")||month1.equals("08")||month1.equals("10")||
									month1.equals("12"))
								day2 = "31";
							if (month1.equals("02")||month1.equals("04")||month1.equals("06")||
									month1.equals("09")||month1.equals("11"))
								day2 = "30";
							month2 = month1;
						}
						// falls schon Zeitraumangaben vorhanden waren
						if (limit1_old.equals("from"))
						{
							limit2 = "";
							day2 = "";
							month2 = "";
						}
						if (limit1_old.equals("notBefore"))
						{
							limit2 = "";
							day2 = "";
							month2 = "";
						}
						if (limit1_old.equals("to"))
						{
							limit1 = limit2;
							day1 = day2;
							limit2 = "";
							day2 = "";
							month2 = "";
						}
						if (limit1_old.equals("notAfter"))
						{
							limit1 = limit2;
							day1 = day2;
							limit2 = "";
							day2 = "";
							month2 = "";
						}
						approximation1 = "";							
					}
					break;
				case DAY1:
					if (result.coded(i+j).equals(DatesConstantsDe.d1.s()))
						day1 = "0" + result.original(i+j);
					if (result.coded(i+j).equals(DatesConstantsDe.d2.s()))
					{
						day1 = result.original(i+j);
						if (Integer.parseInt(day1)>31)
							isDate = false;
					}		
					if (!approximation1.isEmpty())
					{
						approximation1 = "";							
					}
					break;
				case APPROXIMATION1:
					// Jahreszeiten
					if (result.coded(i+j).equals(DatesConstantsDe.spring.s())
							|| result.coded(i+j).equals(DatesConstantsDe.summer.s())
							|| result.coded(i+j).equals(DatesConstantsDe.autumn.s())
							|| result.coded(i+j).equals(DatesConstantsDe.winter.s()))
					{
						if (!limit1.isEmpty())
							limit1_old = limit1;
						if (result.coded(i+j).equals(DatesConstantsDe.spring.s()))
						{
							limit1 = "notBefore";
							month1 = "03";
							limit2 = "notAfter";
							month2 = "06";
						}
						if (result.coded(i+j).equals(DatesConstantsDe.summer.s()))
						{
							limit1 = "notBefore";
							month1 = "06";
							limit2 = "notAfter";
							month2 = "09";
						}
						if (result.coded(i+j).equals(DatesConstantsDe.autumn.s()))
						{
							limit1 = "notBefore";
							month1 = "09";
							limit2 = "notAfter";
							month2 = "12";
						}
						if (result.coded(i+j).equals(DatesConstantsDe.winter.s()))
						{
							limit1 = "notBefore";
							month1 = "01";
							limit2 = "notAfter";
							month2 = "03";
						}
						if (limit1_old.equals("from"))
						{
							limit2 = "";
							month2 = "";
						}
						if (limit1_old.equals("notBefore"))
						{
							limit2 = "";
							month2 = "";
						}
						if (limit1_old.equals("to"))
						{
							limit1 = limit2;
							month1 = month2;
							limit2 = "";
							month2 = "";
						}
						if (limit1_old.equals("notAfter"))
						{
							limit1 = limit2;
							month1 = month2;
							limit2 = "";
							month2 = "";
						}
					}
					// Feste
					if (result.coded(i+j).equals(DatesConstantsDe.newYear.s()))
					{
						day1 = "01";
						month1 = "01";
						approximation1 = "";							
					}
					if (result.coded(i+j).equals(DatesConstantsDe.epiphany.s()))
					{
						day1 = "06";
						month1 = "01";
						approximation1 = "";
					}
					if (result.coded(i+j).equals(DatesConstantsDe.stValetinesDay.s()))
					{
						day1 = "14";
						month1 = "02";
						approximation1 = "";
					}
					if (result.coded(i+j).equals(DatesConstantsDe.maria.s()))
						holyday1 = "Maria";
					if (result.coded(i+j).equals(DatesConstantsDe.allHallows.s()))
					{
						day1 = "01";
						month1 = "11";
						approximation1 = "";							
					}
					if (result.coded(i+j).equals(DatesConstantsDe.allSouls.s()))
					{
						day1 = "02";
						month1 = "11";
						approximation1 = "";
					}
					if (result.coded(i+j).equals(DatesConstantsDe.immaculateConception.s()))
					{
						day1 = "08";
						month1 = "12";
						approximation1 = "";
					}
					if (result.coded(i+j).equals(DatesConstantsDe.christmasEve.s()))
					{
						day1 = "24";
						month1 = "12";
						approximation1 = "";							
					}
					if (result.coded(i+j).equals(DatesConstantsDe.christmas.s()))
					{
						day1 = "25";
						month1 = "12";
						approximation1 = "";							
					}
					if (result.coded(i+j).equals(DatesConstantsDe.stStephansDay.s()))
					{
						day1 = "26";
						month1 = "12";
						approximation1 = "";							
					}
					if (result.coded(i+j).equals(DatesConstantsDe.d1.s()))
						holyday1 = result.original(i+j);
					if (result.coded(i+j).equals(DatesConstantsDe.christmasDay.s()))
					{
						if (holyday1.equals("1"))
						{
							day1 = "25";
							month1 = "12";
							approximation1 = "";							
						}
						if (holyday1.equals("2"))
						{
							day1 = "26";
							month1 = "12";
							approximation1 = "";							
						}
					}
					if (result.coded(i+j).equals(DatesConstantsDe.silvester.s()))
					{
						day1 = "31";
						month1 = "12";
						approximation1 = "";							
					}

					// bewegliche Feiertage
					if (result.coded(i+j).equals(DatesConstantsDe.carnivalThursday.s()))
						easter1 = "-52";
					if (result.coded(i+j).equals(DatesConstantsDe.carnivalMonday.s()))
						easter1 = "-48";
					if (result.coded(i+j).equals(DatesConstantsDe.carnivalTuesday.s()))
						easter1 = "-47";
					if (result.coded(i+j).equals(DatesConstantsDe.carnivalWednesday.s()))
						easter1 = "-46";
					if (result.coded(i+j).equals(DatesConstantsDe.palmSunday.s()))
						easter1 = "-7";
					if (result.coded(i+j).equals(DatesConstantsDe.holyThursday.s()))
						easter1 = "-3";
					if (result.coded(i+j).equals(DatesConstantsDe.holyFriday.s()))
						easter1 = "-2";
					if (result.coded(i+j).equals(DatesConstantsDe.holySaturday.s()))
						easter1 = "-1";
					if (result.coded(i+j).equals(DatesConstantsDe.Easter.s()))
						easter1 = "0";
					if (result.coded(i+j).equals(DatesConstantsDe.easterMonday.s()))
						easter1 = "1";
					if (result.coded(i+j).equals(DatesConstantsDe.ascension.s()))
					{
						if (holyday1.equals("Maria"))
						{
							day1 = "15";
							month1 = "08";
						}
						else
							easter1 = "39";
					}
					if (result.coded(i+j).equals(DatesConstantsDe.pentecost.s()))
						easter1 = "49";
					if (result.coded(i+j).equals(DatesConstantsDe.pentecostMonday.s()))
						easter1 = "50";
					if (result.coded(i+j).equals(DatesConstantsDe.corpusChristi.s()))
						easter1 = "60";
					
					// für Jahrhundertangaben
					if (result.coded(i+j).equals(DatesConstantsDe.century.s()))
					{
						if (!result.original(i+j-3).isEmpty())
						{
							if (result.coded(i+j-3).equals(DatesConstantsDe.d2.s()))
								century1 = result.original(i+j-3);
							if (Integer.parseInt(century1)<=15)
								isDate = false;
							if (Integer.parseInt(century1)>21)
								isDate = false;
							if (Integer.parseInt(century1)>15 &&
									Integer.parseInt(century1)<=21)
							{
								year1 = (Integer.parseInt(century1)-1) + "00";
								limit1 = "notBefore";
								year2 = (Integer.parseInt(century1)) + "00";
								limit2 = "notAfter";
								if (!approximation1.isEmpty())
								{
									if (approximation1.equals("1/2"))
										year2 = (Integer.parseInt(century1)-1) + "50";
									if (approximation1.equals("2/2"))
										year1 = (Integer.parseInt(century1)-1) + "50";
									if (approximation1.equals("1/3"))
										year2 = (Integer.parseInt(century1)-1) + "33";
									if (approximation1.equals("2/3"))
									{
										year1 = (Integer.parseInt(century1)-1) + "33";
										year2 = (Integer.parseInt(century1)-1) + "66";
									}
									if (approximation1.equals("3/3"))
										year1 = (Integer.parseInt(century1)-1) + "66";
									approximation1 = "";							
								}
								if (limit1_old.equals("from"))
								{
									year2 = "";
									limit2 = "";
								}
								if (limit1_old.equals("notBefore"))
								{
									year2 = "";
									limit2 = "";
								}
								if (limit1_old.equals("to"))
								{
									year1 = year2;
									limit1 = limit2;
									year2 = "";
									limit2 = "";
								}
								if (limit1_old.equals("notAfter"))
								{
									year1 = year2;
									limit1 = limit2;
									year2 = "";
									limit2 = "";
								}
							}
						}
					}

					// 1. Hälfte, Anfang, Mitte, etc.
					if (result.coded(i+j).equals(DatesConstantsDe.half.s()))
						if (result.coded(i+j-3).equals(DatesConstantsDe.d1.s()))
						{
							if (result.original(i+j-3).equals("1"))
								approximation1 = "1/2";
							if (result.original(i+j-3).equals("2"))
								approximation1 = "2/2";
						}
					if (result.coded(i+j).equals(DatesConstantsDe.approximation.s()))
					{
						if (result.original(i+j).equals("Anfang"))
							approximation1 = "1/3";
						if (result.original(i+j).equals("Anf"))
							approximation1 = "1/3";
						if (result.original(i+j).equals("Mitte"))
							approximation1 = "2/3";
						if (result.original(i+j).equals("Ende"))
							approximation1 = "3/3";
					}
					break;
				case CONNECTION:
					limit2 = "";
					year2 = "";
					month2 = "";
					day2 = "";
					break;
				case LIMIT2:
					if (result.coded(i+j).equals(DatesConstantsDe.around.s()))
						circa2 = true;
					if (result.coded(i+j).equals(DatesConstantsDe.notAfter.s()))
						limit2 = "notAfter";
					break;
				case YEAR2:
					if (result.coded(i+j).equals(DatesConstantsDe.d4.s()))
					{
						year2 = result.original(i+j);
						if (Integer.parseInt(year2)>2100)
							isDate = false;
						if (Integer.parseInt(year2)<1582)
							isDate = false;
					}
					if (result.coded(i+j).equals(DatesConstantsDe.d2.s()))
					{
						year2 = year1.substring(0, 2) + result.original(i+j);
						if (Integer.parseInt(year2)<Integer.parseInt(year1) && month1.isEmpty())
						{
							month1 = result.original(i+j);
							year2 = "";
						}
					}
					if (!easter2.isEmpty())
					{
						int holydayDate = easterDate(year2) + Integer.parseInt(easter2);
						int hDay=1;
						if (holydayDate <=1)
						{
							month2 = "02";
							hDay = holydayDate + 28;
							if (isLeapYear(year2))
								hDay = hDay + 1;
							if (hDay<1)
							{
								month2 = "01";
								hDay = hDay + 31;
							}
						}
						else if (holydayDate <= 31)
				        {
				        	month2 = "03";
				        	hDay = holydayDate;
				        }
				        else if (holydayDate <= 31+30)
				        {
				        	month2= "04";
				        	hDay = holydayDate-31;
				        }
				        else if (holydayDate <= 31+30+31)
				        {
				        	month2= "05";
				        	hDay = holydayDate-31-30;
				        }
				        else if (holydayDate <= 31+30+31+30)
				        {
				        	month2= "06";
				        	hDay = holydayDate-31-30-31;
				        }
				        if (hDay<10)
				        	day2 = "0" + String.valueOf(hDay);
				        else
				        	day2 = String.valueOf(hDay);
				        approximation2 = "";
					}
					if (!approximation2.isEmpty())
					{
						if (approximation2.equals("1/2"))
						{
							limit2 = "notAfter";
							month2 = "06";
						}
						if (approximation2.equals("2/2"))
						{
							limit2 = "notAfter";
							month2 = "12";
						}
						if (approximation2.equals("1/3"))
						{
							limit2 = "notAfter";
							month2 = "04";
						}
						if (approximation2.equals("2/3"))
						{
							limit2 = "notAfter";
							month2 = "08";
						}
						if (approximation2.equals("3/3"))
						{
							limit2 = "notAfter";
							month2 = "12";
						}
						approximation2 = "";							
					}

					break;
				case MONTH2:
					if (result.coded(i+j).equals(DatesConstantsDe.d1.s()))
						month2 = "0" + result.original(i+j);
					if (result.coded(i+j).equals(DatesConstantsDe.d2.s()))
					{
						month2 = result.original(i+j);
						if (Integer.parseInt(month2)>12)
							isDate = false;
					}
					if (result.coded(i+j).equals(DatesConstantsDe.month01.s()))
						month2 = "01";
					if (result.coded(i+j).equals(DatesConstantsDe.month02.s()))
						month2 = "02";
					if (result.coded(i+j).equals(DatesConstantsDe.month03.s()))
						month2 = "03";
					if (result.coded(i+j).equals(DatesConstantsDe.month04.s()))
						month2 = "04";
					if (result.coded(i+j).equals(DatesConstantsDe.month05.s()))
						month2 = "05";
					if (result.coded(i+j).equals(DatesConstantsDe.month06.s()))
						month2 = "06";
					if (result.coded(i+j).equals(DatesConstantsDe.month07.s()))
						month2 = "07";
					if (result.coded(i+j).equals(DatesConstantsDe.month08.s()))
						month2 = "08";
					if (result.coded(i+j).equals(DatesConstantsDe.month09.s()))
						month2 = "09";
					if (result.coded(i+j).equals(DatesConstantsDe.month10.s()))
						month2 = "10";
					if (result.coded(i+j).equals(DatesConstantsDe.month11.s()))
						month2 = "11";
					if (result.coded(i+j).equals(DatesConstantsDe.month12.s()))
						month2 = "12";
					if (!approximation2.isEmpty())
					{
						if (approximation2.equals("1/2"))
						{
							limit2 = "notAfter";
							day2 = "15";
						}
						if (approximation2.equals("2/2"))
						{
							limit2 = "notAfter";
							if (month2.equals("01")||month2.equals("03")||month2.equals("05")||
									month2.equals("07")||month2.equals("08")||month2.equals("10")||
									month2.equals("12"))
								day2 = "31";
							if (month2.equals("02")||month2.equals("04")||month2.equals("06")||
									month2.equals("09")||month2.equals("11"))
								day2 = "30";
						}
						if (approximation2.equals("1/3"))
						{
							limit2 = "notAfter";
							day2 = "10";
						}
						if (approximation2.equals("2/3"))
						{
							limit2 = "notAfter";
							day2 = "20";
						}
						if (approximation2.equals("3/3"))
						{
							limit2 = "notAfter";
							if (month2.equals("01")||month2.equals("03")||month2.equals("05")||
									month2.equals("07")||month2.equals("08")||month2.equals("10")||
									month2.equals("12"))
								day2 = "31";
							if (month2.equals("02")||month2.equals("04")||month2.equals("06")||
									month2.equals("09")||month2.equals("11"))
								day2 = "30";
						}
						approximation2 = "";							
					}
					break;
				case DAY2:
					if (result.coded(i+j).equals(DatesConstantsDe.d1.s()))
						day2 = "0" + result.original(i+j);
					if (result.coded(i+j).equals(DatesConstantsDe.d2.s()))
					{
						day2 = result.original(i+j);
						if (Integer.parseInt(day2)>31)
							isDate = false;
					}
					if (!approximation2.isEmpty())
					{
						approximation2 = "";							
					}
					break;
				case APPROXIMATION2:
					
					// Jahreszeiten
					if (result.coded(i+j).equals(DatesConstantsDe.spring.s()))
					{
						limit2 = "notAfter";
						month2 = "06";
					}
					if (result.coded(i+j).equals(DatesConstantsDe.summer.s()))
					{
						limit2 = "notAfter";
						month2 = "09";
					}
					if (result.coded(i+j).equals(DatesConstantsDe.autumn.s()))
					{
						limit2 = "notAfter";
						month2 = "12";
					}
					if (result.coded(i+j).equals(DatesConstantsDe.winter.s()))
					{
						limit2 = "notAfter";
						month2 = "12";
					}
					
					// Feste
					if (result.coded(i+j).equals(DatesConstantsDe.newYear.s()))
					{
						day2 = "01";
						month2 = "01";
						approximation2 = "";
					}
					if (result.coded(i+j).equals(DatesConstantsDe.epiphany.s()))
					{
						day2 = "06";
						month2 = "01";
						approximation2 = "";
					}
					if (result.coded(i+j).equals(DatesConstantsDe.stValetinesDay.s()))
					{
						day2 = "14";
						month2 = "02";
						approximation2 = "";
					}	
					if (result.coded(i+j).equals(DatesConstantsDe.maria.s()))
						holyday2 = "Maria";
					if (result.coded(i+j).equals(DatesConstantsDe.allHallows.s()))
					{
						day2 = "01";
						month2 = "11";
						approximation2 = "";							
					}
					if (result.coded(i+j).equals(DatesConstantsDe.allSouls.s()))
					{
						day2 = "02";
						month2 = "11";
						approximation2 = "";
					}
					if (result.coded(i+j).equals(DatesConstantsDe.immaculateConception.s()))
					{
						day2 = "08";
						month2 = "12";
						approximation2 = "";
					}		
					if (result.coded(i+j).equals(DatesConstantsDe.christmasEve.s()))
					{
						day2 = "24";
						month2 = "12";
						approximation2 = "";							
					}
					if (result.coded(i+j).equals(DatesConstantsDe.christmas.s()))
					{
						day2 = "25";
						month2 = "12";
						approximation2 = "";							
					}
					if (result.coded(i+j).equals(DatesConstantsDe.stStephansDay.s()))
					{
						day2 = "26";
						month2 = "12";
						approximation2 = "";							
					}
					if (result.coded(i+j).equals(DatesConstantsDe.d1.s()))
						holyday2 = result.original(i+j);
					if (result.coded(i+j).equals(DatesConstantsDe.christmasDay.s()))
					{
						if (holyday2.equals("1"))
						{
							day2 = "25";
							month2 = "12";
							approximation2 = "";							
						}
						if (holyday2.equals("2"))
						{
							day2 = "26";
							month2 = "12";
							approximation2 = "";							
						}
					}
					if (result.coded(i+j).equals(DatesConstantsDe.silvester.s()))
					{
						day2 = "31";
						month2 = "12";
						approximation2 = "";							
					}
					
					// bewegliche Feiertage
					if (result.coded(i+j).equals(DatesConstantsDe.carnivalThursday.s()))
						easter2 = "-52";
					if (result.coded(i+j).equals(DatesConstantsDe.carnivalMonday.s()))
						easter2 = "-48";
					if (result.coded(i+j).equals(DatesConstantsDe.carnivalTuesday.s()))
						easter2 = "-47";
					if (result.coded(i+j).equals(DatesConstantsDe.carnivalWednesday.s()))
						easter2 = "-46";
					if (result.coded(i+j).equals(DatesConstantsDe.palmSunday.s()))
						easter2 = "-7";
					if (result.coded(i+j).equals(DatesConstantsDe.holyThursday.s()))
						easter2 = "-3";
					if (result.coded(i+j).equals(DatesConstantsDe.holyFriday.s()))
						easter2 = "-2";
					if (result.coded(i+j).equals(DatesConstantsDe.holySaturday.s()))
						easter2 = "-1";
					if (result.coded(i+j).equals(DatesConstantsDe.Easter.s()))
						easter2 = "0";
					if (result.coded(i+j).equals(DatesConstantsDe.easterMonday.s()))
						easter2 = "1";
					if (result.coded(i+j).equals(DatesConstantsDe.ascension.s()))
					{
						if (holyday2.equals("Maria"))
						{
							day2 = "15";
							month2 = "08";
							approximation2 = "";							
						}
						else
							easter2 = "39";
					}
					if (result.coded(i+j).equals(DatesConstantsDe.pentecost.s()))
						easter2 = "49";
					if (result.coded(i+j).equals(DatesConstantsDe.pentecostMonday.s()))
						easter2 = "50";
					if (result.coded(i+j).equals(DatesConstantsDe.corpusChristi.s()))
						easter2 = "60";
					
					// für Jahrhundertangaben
					if (result.coded(i+j).equals(DatesConstantsDe.century.s()))
					{
						if (!result.original(i+j-3).isEmpty())
						{
							if (result.coded(i+j-3).equals(DatesConstantsDe.d2.s()))
								century2 = result.original(i+j-3);
							if (Integer.parseInt(century2)<=15)
								isDate = false;
							if (Integer.parseInt(century2)>21)
								isDate = false;
							if (Integer.parseInt(century2)>15 &&
									Integer.parseInt(century2)<=21)
							{
								year2 = (Integer.parseInt(century2)) + "00";
								limit2 = "notAfter";
								if (!day1.isEmpty() && year1.isEmpty())
								{
									century1 = day1;
									day1="";
									if (Integer.parseInt(century1)>15 &&
											Integer.parseInt(century1)<=21)
									{
										year1 = (Integer.parseInt(century1)-1) + "00";
										limit1 = "notBefore";
									}
								}
								if (!approximation2.isEmpty())
								{
										year1 = (Integer.parseInt(century1)-1) + "50";
									if (approximation2.equals("1/2"))
										year2 = (Integer.parseInt(century2)-1) + "50";
									if (approximation2.equals("1/3"))
										year2 = (Integer.parseInt(century2)-1) + "33";
									if (approximation2.equals("2/3"))
										year2 = (Integer.parseInt(century2)-1) + "66";
									approximation2 = "";							
								}
								
									
							}
						}
					}
					// 1. Hälfte, Anfang, Mitte, etc.
					if (result.coded(i+j).equals(DatesConstantsDe.half.s()))
						if (result.coded(i+j-3).equals(DatesConstantsDe.d1.s()))
						{
							if (result.original(i+j-3).equals("1"))
								approximation2 = "1/2";
							if (result.original(i+j-3).equals("2"))
								approximation2 = "2/2";
						}
					if (result.coded(i+j).equals(DatesConstantsDe.approximation.s()))
					{
						if (result.original(i+j).equals("Anfang"))
							approximation2 = "1/3";
						if (result.original(i+j).equals("Mitte"))
							approximation2 = "2/3";
						if (result.original(i+j).equals("Ende"))
							approximation2 = "3/3";
					}
					
					break;
				default:
					break;
				}
			}
			{	
				// falls keine erste Jahresangabe vorhanden ist, wird die zweite
				// für die erste übernommen
				if (year1.isEmpty())
					year1 = year2;
				// entsprechendes für den Monat, wenn eine erste Tagangabe existiert
				if (month1.isEmpty() && !day1.isEmpty())
					month1 = month2;
				// falls eine zweite Monatsangabe vorhanden ist, wird das Jahr ergänzt
				if (!month2.isEmpty() && year2.isEmpty())
					year2 = year1;
				// falls Ende Februar gemeint ist, wird geschaut, ob es ein Schaltjahr ist
				if (!month1.isEmpty() && !day1.isEmpty())
				{
					if (month1.equals("02") && day1.equals("30"))
					{
						if (isLeapYear(year1))
							day1 = "29";
						else
							day1 = "28";
					}
				}
				if (!month2.isEmpty() && !day2.isEmpty())
				{
					if (month2.equals("02") && day2.equals("30"))
					{
						if (isLeapYear(year2))
							day2 = "29";
						else
							day2 = "28";
					}
				}
			}
			// für ungefähre Datumsangaben
			if (circa1 || circa2)
			{
				if(year2.isEmpty())
				{
					if(circa1)
					{
						if(!day1.isEmpty())
						{
							Calendar date1 = new GregorianCalendar(Integer.parseInt(year1), Integer.parseInt(month1)-1, Integer.parseInt(day1));
							Calendar date2 = new GregorianCalendar(Integer.parseInt(year1), Integer.parseInt(month1)-1, Integer.parseInt(day1));
							date1.add(Calendar.DAY_OF_MONTH, -3);
							day1 = String.format("%1$td", date1);
							month1 = String.format("%1$tm", date1);
							year1 = String.format("%1$tY", date1);
							limit1 = "notBefore";
							date2.add(Calendar.DAY_OF_MONTH, 3);
							day2 = String.format("%1$td", date2);
							month2 = String.format("%1$tm", date2);
							year2 = String.format("%1$tY", date2);
							limit2 = "notAfter";
						}
						else if(!month1.isEmpty())
						{
							Calendar date1 = new GregorianCalendar(Integer.parseInt(year1), Integer.parseInt(month1)-1, 1);
							Calendar date2 = new GregorianCalendar(Integer.parseInt(year1), Integer.parseInt(month1)-1, 1);
							date1.add(Calendar.MONTH, -2);
							month1 = String.format("%1$tm", date1);
							year1 = String.format("%1$tY", date1);
							limit1 = "notBefore";
							date2.add(Calendar.MONTH, 2);
							month2 = String.format("%1$tm", date2);
							year2 = String.format("%1$tY", date2);
							limit2 = "notAfter";
						}
						else if(Integer.parseInt(year1)<=1900)
						{
							Calendar date1 = new GregorianCalendar(Integer.parseInt(year1), 0, 1);
							Calendar date2 = new GregorianCalendar(Integer.parseInt(year1), 0, 1);
							date1.add(Calendar.YEAR, -5);
							year1 = String.format("%1$tY", date1);
							limit1 = "notBefore";
							date2.add(Calendar.YEAR, 5);
							year2 = String.format("%1$tY", date2);
							limit2 = "notAfter";
						}
						else
						{
							Calendar date1 = new GregorianCalendar(Integer.parseInt(year1), 0, 1);
							Calendar date2 = new GregorianCalendar(Integer.parseInt(year1), 0, 1);
							date1.add(Calendar.YEAR, -2);
							year1 = String.format("%1$tY", date1);
							limit1 = "notBefore";
							date2.add(Calendar.YEAR, 2);
							year2 = String.format("%1$tY", date2);
							limit2 = "notAfter";
						}
					}
				} 
				else
				{
					if(circa1)
					{
						if(!day1.isEmpty())
						{
							Calendar date1 = new GregorianCalendar(Integer.parseInt(year1), Integer.parseInt(month1)-1, Integer.parseInt(day1));
							date1.add(Calendar.DAY_OF_MONTH, -3);
							day1 = String.format("%1$td", date1);
							month1 = String.format("%1$tm", date1);
							year1 = String.format("%1$tY", date1);
							limit1 = "notBefore";
						}
						else if(!month1.isEmpty())
						{
							Calendar date1 = new GregorianCalendar(Integer.parseInt(year1), Integer.parseInt(month1)-1, 1);
							date1.add(Calendar.MONTH, -2);
							month1 = String.format("%1$tm", date1);
							year1 = String.format("%1$tY", date1);
							limit1 = "notBefore";
						}
						else if(Integer.parseInt(year1)<=1900)
						{
							Calendar date1 = new GregorianCalendar(Integer.parseInt(year1), 0, 1);
							date1.add(Calendar.YEAR, -5);
							year1 = String.format("%1$tY", date1);
							limit1 = "notBefore";
						}
						else
						{
							Calendar date1 = new GregorianCalendar(Integer.parseInt(year1), 0, 1);
							date1.add(Calendar.YEAR, -2);
							year1 = String.format("%1$tY", date1);
							limit1 = "notBefore";
						}
					}
					if(circa2)
					{
						if(!day2.isEmpty())
						{
							Calendar date2 = new GregorianCalendar(Integer.parseInt(year2), Integer.parseInt(month2)-1, Integer.parseInt(day2));
							date2.add(Calendar.DAY_OF_MONTH, 3);
							day2 = String.format("%1$td", date2);
							month2 = String.format("%1$tm", date2);
							year2 = String.format("%1$tY", date2);
							limit2 = "notAfter";
						}
						else if(!month2.isEmpty())
						{
							Calendar date2 = new GregorianCalendar(Integer.parseInt(year2), Integer.parseInt(month2)-1, 1);
							date2.add(Calendar.MONTH, 2);
							month2 = String.format("%1$tm", date2);
							year2 = String.format("%1$tY", date2);
							limit2 = "notAfter";
						}
						else if(Integer.parseInt(year2)<=1900)
						{
							Calendar date2 = new GregorianCalendar(Integer.parseInt(year2), 0, 1);
							date2.add(Calendar.YEAR, 5);
							year2 = String.format("%1$tY", date2);
							limit2 = "notAfter";
						}
						else
						{
							Calendar date2 = new GregorianCalendar(Integer.parseInt(year2), 0, 1);
							date2.add(Calendar.YEAR, 2);
							year2 = String.format("%1$tY", date2);
							limit2 = "notAfter";
						}
					}
				}
			}
			// erzeugt eine neue Datumsangabe und schreibt sie in die Ergebnisliste
			if (isDate)
			{
				DateOccurrence newDate = new DateOccurrence(start, length, limit1, year1, month1, day1,
					limit2, year2, month2, day2, originalText);
				result.addIdentifiedDate(newDate);
			}
		}
		
		/**
		 *  Sucht den Text nach den Startelemente von erkannten Daten durch
		 *  und formatiert das jeweilige Datum.
		 *  @param result Das zu bearbeitende DatesResult-Objekt.
		 */
		private static void format(DatesResult result){
			boolean isDate = false;
			for (int i=0; i<result.length(); i++){
				if (result.identifiedLength(i)==0)
					isDate = false;
				else{
					if (!isDate){
						formatDate_de(result, i);
					}
					isDate = true;
				}
			}
		}
		
		/**
		 * Findet deutsche Datumsangaben im Eingabetext des übergebenen
		 * Ergebnis-Objects.
		 */
		static void findOccurrences(DatesResult result){
			code(result);
			identify(result);
			format(result);
		}
				
	}
	

