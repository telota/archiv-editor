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
class DatesToolsIt extends DatesTools
{
		
		//  METHODEN

		/**
		 *  Kodiert einen italienischen Originaltext durch die aufgeführten Kodes
		 *  und schreibt den Kode in den kodierten Text.
		 *  @param result Das zu bearbeitende DatesResult-Objekt.
		 */
		private static void code(DatesResult result){
			for (int i=0; i<result.length(); i++){
				code(result, i, DatesConstantsIt.word); // muss vorne stehen,
						// da bestimmte worte nochmal neu kodiert werden.
				code(result, i, DatesConstantsIt.space);
				code(result, i, DatesConstantsIt.miscellaneous);
				code(result, i, DatesConstantsIt.Other);
				code(result, i, DatesConstantsIt.punctuation);
				code(result, i, DatesConstantsIt.d); // muss vor den anderen Zahlen
						// stehen, da die nochmal neu kodiert werden.
				code(result, i, DatesConstantsIt.ordinalIndicator);
				code(result, i, DatesConstantsIt.apostrophe);
				// Zahlen
				code(result, i, DatesConstantsIt.d1);
				code(result, i, DatesConstantsIt.d2);
				code(result, i, DatesConstantsIt.d3);
				code(result, i, DatesConstantsIt.d4);
				// spezielle Worte
				code(result, i, DatesConstantsIt.link);
				code(result, i, DatesConstantsIt.article);
				code(result, i, DatesConstantsIt.of);
				code(result, i, DatesConstantsIt.from);
				code(result, i, DatesConstantsIt.to);
				code(result, i, DatesConstantsIt.notBefore);
				code(result, i, DatesConstantsIt.notAfterFirst);
				code(result, i, DatesConstantsIt.around);
				code(result, i, DatesConstantsIt.at);
				code(result, i, DatesConstantsIt.atNext);
				code(result, i, DatesConstantsIt.year);
				code(result, i, DatesConstantsIt.day);
				code(result, i, DatesConstantsIt.and);
				code(result, i, DatesConstantsIt.Between);
				code(result, i, DatesConstantsIt.approximation);
				code(result, i, DatesConstantsIt.half);
				// Zahlworte
				code(result, i, DatesConstantsIt.ordinal01);
				code(result, i, DatesConstantsIt.ordinal02);
				// Namen
				code(result, i, DatesConstantsIt.saint);
				code(result, i, DatesConstantsIt.maria);
				code(result, i, DatesConstantsIt.Jesus);
				code(result, i, DatesConstantsIt.stephan);
				code(result, i, DatesConstantsIt.silvester);
				code(result, i, DatesConstantsIt.valetine);
				code(result, i, DatesConstantsIt.all);
				// Wochentage
				code(result, i, DatesConstantsIt.monday);
				code(result, i, DatesConstantsIt.tuesday);
				code(result, i, DatesConstantsIt.wednesday);
				code(result, i, DatesConstantsIt.thursday);
				code(result, i, DatesConstantsIt.friday);
				code(result, i, DatesConstantsIt.saturday);
				code(result, i, DatesConstantsIt.sunday);
				// Tage
				code(result, i, DatesConstantsIt.cardinal02);
				code(result, i, DatesConstantsIt.cardinal03);
				code(result, i, DatesConstantsIt.cardinal04);
				code(result, i, DatesConstantsIt.cardinal05);
				code(result, i, DatesConstantsIt.cardinal06);
				code(result, i, DatesConstantsIt.cardinal07);
				code(result, i, DatesConstantsIt.cardinal08);
				code(result, i, DatesConstantsIt.cardinal09);
				code(result, i, DatesConstantsIt.cardinal10);
				code(result, i, DatesConstantsIt.cardinal11);
				code(result, i, DatesConstantsIt.cardinal12);
				code(result, i, DatesConstantsIt.cardinal13);
				code(result, i, DatesConstantsIt.cardinal14);
				code(result, i, DatesConstantsIt.cardinal15);
				code(result, i, DatesConstantsIt.cardinal16);
				code(result, i, DatesConstantsIt.cardinal17);
				code(result, i, DatesConstantsIt.cardinal18);
				code(result, i, DatesConstantsIt.cardinal19);
				code(result, i, DatesConstantsIt.cardinal20);
				code(result, i, DatesConstantsIt.cardinal21);
				code(result, i, DatesConstantsIt.cardinal22);
				code(result, i, DatesConstantsIt.cardinal23);
				code(result, i, DatesConstantsIt.cardinal24);
				code(result, i, DatesConstantsIt.cardinal25);
				code(result, i, DatesConstantsIt.cardinal26);
				code(result, i, DatesConstantsIt.cardinal27);
				code(result, i, DatesConstantsIt.cardinal28);
				code(result, i, DatesConstantsIt.cardinal29);
				code(result, i, DatesConstantsIt.cardinal30);
				code(result, i, DatesConstantsIt.cardinal31);
				// Monate
				code(result, i, DatesConstantsIt.month01);
				code(result, i, DatesConstantsIt.month02);
				code(result, i, DatesConstantsIt.month03);
				code(result, i, DatesConstantsIt.month04);
				code(result, i, DatesConstantsIt.month05);
				code(result, i, DatesConstantsIt.month06);
				code(result, i, DatesConstantsIt.month07);
				code(result, i, DatesConstantsIt.month08);
				code(result, i, DatesConstantsIt.month09);
				code(result, i, DatesConstantsIt.month10);
				code(result, i, DatesConstantsIt.month11);
				code(result, i, DatesConstantsIt.month12);
				// Jahreszeiten
				code(result, i, DatesConstantsIt.spring);
				code(result, i, DatesConstantsIt.summer);
				code(result, i, DatesConstantsIt.autumn);
				code(result, i, DatesConstantsIt.winter);
				// Feste
				code(result, i, DatesConstantsIt.newYear);
				code(result, i, DatesConstantsIt.epiphany);
				code(result, i, DatesConstantsIt.valetine);
				code(result, i, DatesConstantsIt.ferragosto);
				code(result, i, DatesConstantsIt.allHallows);
				code(result, i, DatesConstantsIt.allSouls);
				code(result, i, DatesConstantsIt.immaculateConception);
				code(result, i, DatesConstantsIt.christmas);
				code(result, i, DatesConstantsIt.assumption);
				// Feiertage
				code(result, i, DatesConstantsIt.carnivalWeek);
				code(result, i, DatesConstantsIt.carnival);
				code(result, i, DatesConstantsIt.palmSunday);
				code(result, i, DatesConstantsIt.easter);
				code(result, i, DatesConstantsIt.easterMonday);
				code(result, i, DatesConstantsIt.angel);
				code(result, i, DatesConstantsIt.ascension);
				code(result, i, DatesConstantsIt.pentecost);
				code(result, i, DatesConstantsIt.corpusChristi);
				// Jahrhundert
				code(result, i, DatesConstantsIt.century);
				code(result, i, DatesConstantsIt.century16th);
				code(result, i, DatesConstantsIt.century17th);
				code(result, i, DatesConstantsIt.century18th);
				code(result, i, DatesConstantsIt.century19th);
				code(result, i, DatesConstantsIt.century20th);
				code(result, i, DatesConstantsIt.ordinal06);
				code(result, i, DatesConstantsIt.ordinal07);
				code(result, i, DatesConstantsIt.ordinal08);
				code(result, i, DatesConstantsIt.ordinal09);
				code(result, i, DatesConstantsIt.ordinal10);
				code(result, i, DatesConstantsIt.ordinal16);
				code(result, i, DatesConstantsIt.ordinal17);
				code(result, i, DatesConstantsIt.ordinal18);
				code(result, i, DatesConstantsIt.ordinal19);
				code(result, i, DatesConstantsIt.ordinal20);
				code(result, i, DatesConstantsIt.ordinal21);
			}
		}

		/**
		 *  Erkennt die aufgeführten Muster für italienische Datumsangaben 
		 *  im Kode und schreibt die passenden Bezeichner in erkannterText.
		 *  @param result Das zu bearbeitende DatesResult-Objekt.
		 */
		private static void identify(DatesResult result){
			// zuerst einfache Datumsangaben
			identifyDate(result, DatesSymbolsIt.day_art_month_art_yyyy);
			identifyDate(result, DatesSymbolsIt.dd_art_month_art_yyyy);
			identifyDate(result, DatesSymbolsIt.day_month_art_yyyy);
			identifyDate(result, DatesSymbolsIt.ddp_month_art_yyyy);
			identifyDate(result, DatesSymbolsIt.dd_month_art_yyyy);
			identifyDate(result, DatesSymbolsIt.day_art_monthp_yyyy);
			identifyDate(result, DatesSymbolsIt.dd_art_monthp_yyyy);
			identifyDate(result, DatesSymbolsIt.day_monthp_yyyy);
			identifyDate(result, DatesSymbolsIt.ddp_monthp_yyyy);
			identifyDate(result, DatesSymbolsIt.dd_monthp_yyyy);
			identifyDate(result, DatesSymbolsIt.day_art_month_yyyy);
			identifyDate(result, DatesSymbolsIt.dd_art_month_yyyy);
			identifyDate(result, DatesSymbolsIt.day_month_yyyy);
			identifyDate(result, DatesSymbolsIt.ddp_month_yyyy);
			identifyDate(result, DatesSymbolsIt.dd_month_yyyy);
			identifyDate(result, DatesSymbolsIt.ddpmonthpyyyy);
			identifyDate(result, DatesSymbolsIt.ddpmmpyyyy);
			identifyDate(result, DatesSymbolsIt.ddummuyyyy);
			identifyDate(result, DatesSymbolsIt.month_artpyear_yyyy);
			identifyDate(result, DatesSymbolsIt.month_art_yyyy);
			identifyDate(result, DatesSymbolsIt.monthp_yyyy);
			identifyDate(result, DatesSymbolsIt.month_yyyy);
			identifyDate(result, DatesSymbolsIt.year_yyyy);
			identifyDate(result, DatesSymbolsIt.yyyy);
			// Jahreszeiten
			identifyDate(result, DatesSymbolsIt.season_art_yyyy);
			identifyDate(result, DatesSymbolsIt.season_yyyy);
			// Feiertage und Feste
			identifyDate(result, DatesSymbolsIt.day_artpyear_yyyy);
			identifyDate(result, DatesSymbolsIt.assumption_art_maria_yyyy);
			identifyDate(result, DatesSymbolsIt.all_art_hallows_yyyy);
			identifyDate(result, DatesSymbolsIt.all_art_souls_yyyy);
			identifyDate(result, DatesSymbolsIt.immaculate_conception_yyyy);
			identifyDate(result, DatesSymbolsIt.weekday_carnival_yyyy);
			identifyDate(result, DatesSymbolsIt.art_palmsunday_yyyy);
			identifyDate(result, DatesSymbolsIt.weekday_art_palmsunday_yyyy);
			identifyDate(result, DatesSymbolsIt.weekday_st_yyyy);
			identifyDate(result, DatesSymbolsIt.weekday_artpeaster_yyyy);
			identifyDate(result, DatesSymbolsIt.weekday_art_pentecost_yyyy);
			identifyDate(result, DatesSymbolsIt.ascension_art_jesus_yyyy);
			identifyDate(result, DatesSymbolsIt.corpus_christi_yyyy);
			identifyDate(result, DatesSymbolsIt.st_name_yyyy);
			identifyDate(result, DatesSymbolsIt.holyday_yyyy);
			// weiteres
			identifyDate(result, DatesSymbolsIt.yyyyp_dd_monthp);
			identifyDate(result, DatesSymbolsIt.ord_century);
			identifyDate(result, DatesSymbolsIt.no_century);
			identifyDate(result, DatesSymbolsIt.ordo_century);
			identifyDate(result, DatesSymbolsIt.century_ord);
			identifyDate(result, DatesSymbolsIt.century_ordo);
			identifyDate(result, DatesSymbolsIt.centuryp_ord);
			identifyDate(result, DatesSymbolsIt.century_ord_ord);
			identifyDate(result, DatesSymbolsIt.ncentury);
			if (identifyDate(result, DatesSymbolsIt.aposyyy))
				{
				identifyPrePhrase(result, DatesSymbolsIt.ord_half_art);
				identifyPrePhrase(result, DatesSymbolsIt.approx_art);
				identifyPrePhrase(result, DatesSymbolsIt.limit_art);
				identifyPrePhrase(result, DatesSymbolsIt.limit_at);
				identifyPrePhrase(result, DatesSymbolsIt.lim);
				identifyPrePhrase(result, DatesSymbolsIt.art);
				identifyConnection(result, DatesSymbolsIt.connect);
				identifyPrePhrase(result, DatesSymbolsIt.at);
				}
			// mögliche Angaben vor einfachen Datumsangaben
			identifyPrePhrase(result, DatesSymbolsIt.ord_half_art_);
			identifyPrePhrase(result, DatesSymbolsIt.ord_half_artp);
			identifyPrePhrase(result, DatesSymbolsIt.ord_half_);
			identifyPrePhrase(result, DatesSymbolsIt.approx_art_);
			identifyPrePhrase(result, DatesSymbolsIt.approx_artp);
			identifyPrePhrase(result, DatesSymbolsIt.approx_);
			identifyPrePhrase(result, DatesSymbolsIt.limit_art_);
			identifyPrePhrase(result, DatesSymbolsIt.limit_artp);
			identifyPrePhrase(result, DatesSymbolsIt.limit_at_);
			identifyPrePhrase(result, DatesSymbolsIt.limit_atp);
			identifyPrePhrase(result, DatesSymbolsIt.limit_);
			identifyPrePhrase(result, DatesSymbolsIt.limitp);
			identifyPrePhrase(result, DatesSymbolsIt.limitp_);
			identifyPrePhrase(result, DatesSymbolsIt.art_);
			identifyPrePhrase(result, DatesSymbolsIt.artp);
			// zwei zusammengehörige Datumsangaben
			if(findPattern(result, DatesSymbolsIt.between_) &&
					findPattern(result, DatesSymbolsIt.and_)){
				identifyPrePhrase(result, DatesSymbolsIt.between_);
				identifyConnection(result, DatesSymbolsIt.and_);
			}
			identifyConnection(result, DatesSymbolsIt.connect);
			identifyConnection(result, DatesSymbolsIt.connect_);
			identifyConnection(result, DatesSymbolsIt.connect_to_);
			identifyConnection(result, DatesSymbolsIt.connect_top);
			identifyAfterDate(result, DatesSymbolsIt.connect_y);
			identifyAfterDate(result, DatesSymbolsIt.connect_yy);
			// mögliche Angaben vor den kombinierten Datumsangaben
			identifyPrePhrase(result, DatesSymbolsIt.at_);
			identifyPrePhrase(result, DatesSymbolsIt.atp);
			// Angaben im ISO-Format
			identifyDate(result, DatesSymbolsIt.yyyyummudd);
		}

		/**
		 *  Fügt das erkannte italienische Datum, das bei 'i' startet,
		 *  im richtigen Format der Ergebnisliste "erkannteDaten" hinzu.
		 *  @param result Das zu bearbeitende DatesResult-Objekt.
		 *  @param i Startelement des erkannten Datums.
		 */
		private static void formatDate_it(DatesResult result, int i){
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
			String weekday1 = "";
			String weekday2 = "";
			String holyday1 = "";
			String holyday2 = "";
			String easter1 = "";
			String easter2 = "";
			String century1 = "";
			String century2 = "";
			String approximation1 = "";
			String approximation2 = "";
			boolean isCentury1 = false;
			boolean isCentury2 = false;
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
					if (result.coded(i+j).equals(DatesConstantsIt.from.s()))
						limit1 = "from";
					if (result.coded(i+j).equals(DatesConstantsIt.at.s()))
						limit1 = "when";
					if (result.coded(i+j).equals(DatesConstantsIt.to.s()))
						limit1 = "to";
					if (result.coded(i+j).equals(DatesConstantsIt.notBefore.s()))
						limit1 = "notBefore";
					if (result.coded(i+j).equals(DatesConstantsIt.notAfterFirst.s()))
						limit1 = "notAfter";
					if (result.coded(i+j).equals(DatesConstantsDe.around.s()))
						circa1 = true;
					break;
				case YEAR1:
					if (result.coded(i+j).equals(DatesConstantsIt.d4.s()))
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
					if (result.coded(i+j).equals(DatesConstantsIt.d1.s()))
						month1 = "0" + result.original(i+j);
					if (result.coded(i+j).equals(DatesConstantsIt.d2.s()))
					{
						month1 = result.original(i+j);
						if (Integer.parseInt(month1)>12)
							isDate = false;
					}
					if (result.coded(i+j).equals(DatesConstantsIt.month01.s()))
						month1 = "01";
					if (result.coded(i+j).equals(DatesConstantsIt.month02.s()))
						month1 = "02";
					if (result.coded(i+j).equals(DatesConstantsIt.month03.s()))
						month1 = "03";
					if (result.coded(i+j).equals(DatesConstantsIt.month04.s()))
						month1 = "04";
					if (result.coded(i+j).equals(DatesConstantsIt.month05.s()))
						month1 = "05";
					if (result.coded(i+j).equals(DatesConstantsIt.month06.s()))
						month1 = "06";
					if (result.coded(i+j).equals(DatesConstantsIt.month07.s()))
						month1 = "07";
					if (result.coded(i+j).equals(DatesConstantsIt.month08.s()))
						month1 = "08";
					if (result.coded(i+j).equals(DatesConstantsIt.month09.s()))
						month1 = "09";
					if (result.coded(i+j).equals(DatesConstantsIt.month10.s()))
						month1 = "10";
					if (result.coded(i+j).equals(DatesConstantsIt.month11.s()))
						month1 = "11";
					if (result.coded(i+j).equals(DatesConstantsIt.month12.s()))
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
					if (result.coded(i+j).equals(DatesConstantsIt.d1.s()))
						day1 = "0" + result.original(i+j);
					if (result.coded(i+j).equals(DatesConstantsIt.d2.s()))
					{
						day1 = result.original(i+j);
						if (Integer.parseInt(day1)>31)
							isDate = false;
					}	
					if (result.coded(i+j).equals(DatesConstantsIt.ordinal01.s()))
						day1 = "01";
					if (result.coded(i+j).equals(DatesConstantsIt.cardinal02.s()))
						day1 = "02";
					if (result.coded(i+j).equals(DatesConstantsIt.cardinal03.s()))
						day1 = "03";
					if (result.coded(i+j).equals(DatesConstantsIt.cardinal04.s()))
						day1 = "04";
					if (result.coded(i+j).equals(DatesConstantsIt.cardinal05.s()))
						day1 = "05";
					if (result.coded(i+j).equals(DatesConstantsIt.cardinal06.s()))
						day1 = "06";
					if (result.coded(i+j).equals(DatesConstantsIt.cardinal07.s()))
						day1 = "07";
					if (result.coded(i+j).equals(DatesConstantsIt.cardinal08.s()))
						day1 = "08";
					if (result.coded(i+j).equals(DatesConstantsIt.cardinal09.s()))
						day1 = "09";
					if (result.coded(i+j).equals(DatesConstantsIt.cardinal10.s()))
						day1 = "10";
					if (result.coded(i+j).equals(DatesConstantsIt.cardinal11.s()))
						day1 = "11";
					if (result.coded(i+j).equals(DatesConstantsIt.cardinal12.s()))
						day1 = "12";
					if (result.coded(i+j).equals(DatesConstantsIt.cardinal13.s()))
						day1 = "13";
					if (result.coded(i+j).equals(DatesConstantsIt.cardinal14.s()))
						day1 = "14";
					if (result.coded(i+j).equals(DatesConstantsIt.cardinal15.s()))
						day1 = "15";
					if (result.coded(i+j).equals(DatesConstantsIt.cardinal16.s()))
						day1 = "16";
					if (result.coded(i+j).equals(DatesConstantsIt.cardinal17.s()))
						day1 = "17";
					if (result.coded(i+j).equals(DatesConstantsIt.cardinal18.s()))
						day1 = "18";
					if (result.coded(i+j).equals(DatesConstantsIt.cardinal19.s()))
						day1 = "19";
					if (result.coded(i+j).equals(DatesConstantsIt.cardinal20.s()))
						day1 = "20";
					if (result.coded(i+j).equals(DatesConstantsIt.cardinal21.s()))
						day1 = "21";
					if (result.coded(i+j).equals(DatesConstantsIt.cardinal22.s()))
						day1 = "22";
					if (result.coded(i+j).equals(DatesConstantsIt.cardinal23.s()))
						day1 = "23";
					if (result.coded(i+j).equals(DatesConstantsIt.cardinal24.s()))
						day1 = "24";
					if (result.coded(i+j).equals(DatesConstantsIt.cardinal25.s()))
						day1 = "25";
					if (result.coded(i+j).equals(DatesConstantsIt.cardinal26.s()))
						day1 = "26";
					if (result.coded(i+j).equals(DatesConstantsIt.cardinal27.s()))
						day1 = "27";
					if (result.coded(i+j).equals(DatesConstantsIt.cardinal28.s()))
						day1 = "28";
					if (result.coded(i+j).equals(DatesConstantsIt.cardinal29.s()))
						day1 = "29";
					if (result.coded(i+j).equals(DatesConstantsIt.cardinal30.s()))
						day1 = "30";
					if (result.coded(i+j).equals(DatesConstantsIt.cardinal31.s()))
						day1 = "31";
					if (!approximation1.isEmpty())
						approximation1 = "";
					break;
				case APPROXIMATION1:
					// Jahreszeiten
					if (result.coded(i+j).equals(DatesConstantsIt.spring.s())
							|| result.coded(i+j).equals(DatesConstantsIt.summer.s())
							|| result.coded(i+j).equals(DatesConstantsIt.autumn.s())
							|| result.coded(i+j).equals(DatesConstantsIt.winter.s()))
					{
						if (!limit1.isEmpty())
							limit1_old = limit1;
						if (result.coded(i+j).equals(DatesConstantsIt.spring.s()))
						{
							limit1 = "notBefore";
							month1 = "03";
							limit2 = "notAfter";
							month2 = "06";
						}
						if (result.coded(i+j).equals(DatesConstantsIt.summer.s()))
						{
							limit1 = "notBefore";
							month1 = "06";
							limit2 = "notAfter";
							month2 = "09";
						}
						if (result.coded(i+j).equals(DatesConstantsIt.autumn.s()))
						{
							limit1 = "notBefore";
							month1 = "09";
							limit2 = "notAfter";
							month2 = "12";
						}
						if (result.coded(i+j).equals(DatesConstantsIt.winter.s()))
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
					// Wochentage
					if (result.coded(i+j).equals(DatesConstantsIt.monday.s()))
						weekday1 = "1";
					if (result.coded(i+j).equals(DatesConstantsIt.tuesday.s()))
						weekday1 = "2";
					if (result.coded(i+j).equals(DatesConstantsIt.wednesday.s()))
						weekday1 = "3";
					if (result.coded(i+j).equals(DatesConstantsIt.thursday.s()))
						weekday1 = "4";
					if (result.coded(i+j).equals(DatesConstantsIt.friday.s()))
						weekday1 = "5";
					if (result.coded(i+j).equals(DatesConstantsIt.saturday.s()))
						weekday1 = "6";
					if (result.coded(i+j).equals(DatesConstantsIt.sunday.s()))
						weekday1 = "7";
					// Feste
					if (result.coded(i+j).equals(DatesConstantsIt.newYear.s()))
					{
						day1 = "01";
						month1 = "01";
						approximation1 = "";							
					}
					if (result.coded(i+j).equals(DatesConstantsIt.ordinal01.s()))
						holyday1 = "01";
					if (result.coded(i+j).equals(DatesConstantsIt.year.s()))
					{
						if (holyday1.equals("01"))
						{
							day1 = "01";
							month1 = "01";
							approximation1 = "";
						}
					}
					if (result.coded(i+j).equals(DatesConstantsIt.epiphany.s()))
					{
						day1 = "06";
						month1 = "01";
						approximation1 = "";							
					}
					if (result.coded(i+j).equals(DatesConstantsIt.valetine.s()))
					{
						day1 = "14";
						month1 = "02";
						approximation1 = "";							
					}
					if (result.coded(i+j).equals(DatesConstantsIt.ferragosto.s()))
					{
						day1 = "15";
						month1 = "08";
						approximation1 = "";							
					}
					if (result.coded(i+j).equals(DatesConstantsIt.assumption.s()))
						holyday1 = "MariäHimmelfahrt";
					if (result.coded(i+j).equals(DatesConstantsIt.maria.s()))
					{
						if (holyday1.equals("MariäHimmelfahrt"))
						{
							day1 = "15";
							month1 = "08";
							approximation1 = "";
						}
					}
					if (result.coded(i+j).equals(DatesConstantsIt.allHallows.s()))
					{
						day1 = "01";
						month1 = "11";
						approximation1 = "";							
					}
					if (result.coded(i+j).equals(DatesConstantsIt.all.s()))
						holyday1 = "Allerheiligen";
					if (result.coded(i+j).equals(DatesConstantsIt.saint.s()))
					{
						if (holyday1.equals("Allerheiligen"))
						{
							day1 = "01";
							month1 = "11";
							approximation1 = "";
						}
					}
					if (result.coded(i+j).equals(DatesConstantsIt.allSouls.s()))
					{
						day1 = "02";
						month1 = "11";
						approximation1 = "";							
					}
					if (result.coded(i+j).equals(DatesConstantsIt.immaculateConception.s()))
					{
						if (holyday1.equals("MariäEmpfängnis"))
						{
							day1 = "08";
							month1 = "12";
						}
						else
							holyday1 = "MariäEmpfängnis";
					}
					if (result.coded(i+j).equals(DatesConstantsIt.christmas.s()))
					{
						day1 = "25";
						month1 = "12";
						approximation1 = "";							
					}
					if (result.coded(i+j).equals(DatesConstantsIt.stephan.s()))
					{
						day1 = "26";
						month1 = "12";
						approximation1 = "";							
					}
					if (result.coded(i+j).equals(DatesConstantsIt.silvester.s()))
					{
						day1 = "31";
						month1 = "12";
						approximation1 = "";							
					}
					
					// bewegliche Feiertage
					if (result.coded(i+j).equals(DatesConstantsIt.carnivalWeek.s()))
					{
						if (weekday1.equals("4")) 
							easter1 = "-52";
						if (weekday1.equals("5")) 
							easter1 = "-51";
						if (weekday1.equals("6")) 
							easter1 = "-50";
						if (weekday1.equals("7")) 
							easter1 = "-49";
						if (weekday1.equals("1")) 
							easter1 = "-48";
						if (weekday1.equals("2"))
							easter1 = "-47";
						if (weekday1.equals("3")) 
							easter1 = "-46";
					}
					if (result.coded(i+j).equals(DatesConstantsIt.carnival.s()))
						easter1 = "-49";
					if (result.coded(i+j).equals(DatesConstantsIt.palmSunday.s()))
						easter1 = "-7";
					if (result.coded(i+j).equals(DatesConstantsIt.saint.s()))
					{
						if (weekday1.equals("1")) 
							easter1 = "-6";
						if (weekday1.equals("2")) 
							easter1 = "-5";
						if (weekday1.equals("3")) 
							easter1 = "-4";
						if (weekday1.equals("4")) 
							easter1 = "-3";
						if (weekday1.equals("5"))
							easter1 = "-2";
						if (weekday1.equals("6")) 
							easter1 = "-1";
						if (weekday1.equals("7")) 
							easter1 = "0";
					}
					if (result.coded(i+j).equals(DatesConstantsIt.easter.s()))
						easter1 = "0";
					if (result.coded(i+j).equals(DatesConstantsIt.easterMonday.s()))
						easter1 = "1";
					if (result.coded(i+j).equals(DatesConstantsIt.angel.s()))
						easter1 = "1";
					if (result.coded(i+j).equals(DatesConstantsIt.ascension.s()))
						easter1 = "39";
					if (result.coded(i+j).equals(DatesConstantsIt.pentecost.s()))
					{
						if (weekday1.equals("1"))
							easter1 = "50";
						else
							easter1 = "49";
					}
					if (result.coded(i+j).equals(DatesConstantsIt.corpusChristi.s()))
						easter1 = "60";
					
					
					// für Jahrhundertangaben
					if (result.coded(i+j).equals(DatesConstantsIt.century.s()))
					{
						isCentury1 = true;
					}
					if (result.coded(i+j).equals(DatesConstantsIt.d2.s()))
						century1 = result.original(i+j);
					if (result.coded(i+j).equals(DatesConstantsIt.ordinal16.s()))
						century1 = "16";
					if (result.coded(i+j).equals(DatesConstantsIt.ordinal17.s()))
						century1 = "17";
					if (result.coded(i+j).equals(DatesConstantsIt.ordinal18.s()))
						century1 = "18";
					if (result.coded(i+j).equals(DatesConstantsIt.ordinal19.s()))
						century1 = "19";
					if (result.coded(i+j).equals(DatesConstantsIt.ordinal20.s()))
						century1 = "20";
					if (result.coded(i+j).equals(DatesConstantsIt.ordinal21.s()))
						century1 = "21";
					if (result.coded(i+j).equals(DatesConstantsIt.century16th.s()))
					{
						isCentury1 = true;
						century1 = "16";
					}
					if (result.coded(i+j).equals(DatesConstantsIt.century17th.s()))
					{
						isCentury1 = true;
						century1 = "17";
					}
					if (result.coded(i+j).equals(DatesConstantsIt.century18th.s()))
					{
						isCentury1 = true;
						century1 = "18";
					}
					if (result.coded(i+j).equals(DatesConstantsIt.century19th.s()))
					{
						isCentury1 = true;
						century1 = "19";
					}
					if (result.coded(i+j).equals(DatesConstantsIt.century20th.s()))
					{
						isCentury1 = true;
						century1 = "20";
					}
					if (result.coded(i+j).equals(DatesConstantsIt.ordinal10.s()))
					{
						if (century1.equals("6") || century1.equals("7") ||
								century1.equals("8") || century1.equals("9"))
							century1 = "1" + century1;
						else century1 = "10";
					}
					if (result.coded(i+j).equals(DatesConstantsIt.ordinal06.s()))
					{
						if (century1.equals("10"))
							century1 = "16";
						else century1 = "6";
					}
					if (result.coded(i+j).equals(DatesConstantsIt.ordinal07.s()))
					{
						if (century1.equals("10"))
							century1 = "17";
						else century1 = "7";
					}
					if (result.coded(i+j).equals(DatesConstantsIt.ordinal08.s()))
					{
						if (century1.equals("10"))
							century1 = "18";
						else century1 = "8";
					}
					if (result.coded(i+j).equals(DatesConstantsIt.ordinal09.s()))
					{
						if (century1.equals("10"))
							century1 = "19";
						else century1 = "9";
					}
					if (result.coded(i+j).equals(DatesConstantsIt.apostrophe.s()))
						isCentury1 = true;
					if (result.coded(i+j).equals(DatesConstantsIt.d3.s()))
					{
						if (result.original(i+j).substring(1).equals("00"))
							century1 = (Integer.parseInt(result.original(i+j).substring(0, 1))+ 11) + "";
					}
					if (isCentury1 && !century1.isEmpty())
					{
						if (Integer.parseInt(century1)<=15)
							isDate = false;
						if (Integer.parseInt(century1)>21)
							isDate = false;
						if (Integer.parseInt(century1)>15 &&
								Integer.parseInt(century1)<=21)
						{
							isDate = true;
							isCentury1 = false;
							limit1_old = limit1;
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
					// 1. Hälfte, Anfang, Mitte, etc.
					if (result.coded(i+j).equals(DatesConstantsIt.half.s()))
					{
						if (result.original(i+j).equals("metà"))
							approximation1 = "2/3";
						if (result.coded(i+j-2).equals(DatesConstantsIt.notAfterFirst.s()))
							approximation1 = "1/2";
						if (result.coded(i+j-2).equals(DatesConstantsIt.ordinal01.s()))
							approximation1 = "1/2";
						if (result.coded(i+j-2).equals(DatesConstantsIt.ordinal02.s()))
							approximation1 = "2/2";
					}
					if (result.coded(i+j).equals(DatesConstantsIt.approximation.s()))
					{
						if (result.original(i+j).equals("inizio"))
							approximation1 = "1/3";
						if (result.original(i+j).equals("Inizio"))
							approximation1 = "1/3";
						if (result.original(i+j).equals("fine"))
							approximation1 = "3/3";
						if (result.original(i+j).equals("Fine"))
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
					if (result.coded(i+j).equals(DatesConstantsIt.notAfterFirst.s()))
						limit2 = "notAfter";
					break;
				case YEAR2:
					if (result.coded(i+j).equals(DatesConstantsIt.d4.s()))
					{
						year2 = result.original(i+j);
						if (Integer.parseInt(year2)>2100)
							isDate = false;
						if (Integer.parseInt(year2)<1582)
							isDate = false;
					}
					if (result.coded(i+j).equals(DatesConstantsIt.d2.s()))
					{
						year2 = year1.substring(0, 2) + result.original(i+j);
						if (Integer.parseInt(year2)<Integer.parseInt(year1) && month1.isEmpty())
						{
							month1 = result.original(i+j);
							year2 = "";
						}
					}
					if (result.coded(i+j).equals(DatesConstantsIt.d1.s()))
					{
						year2 = year1.substring(0, 3) + result.original(i+j);
					}
					// für bewegliche Feiertage mit Bezug zu Ostern
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
				        	month2 = "04";
				        	hDay = holydayDate-31;
				        }
				        else if (holydayDate <= 31+30+31)
				        {
				        	month2 = "05";
				        	hDay = holydayDate-31-30;
				        }
				        else if (holydayDate <= 31+30+31+30)
				        {
				        	month2 = "06";
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
					if (result.coded(i+j).equals(DatesConstantsIt.d1.s()))
						month2 = "0" + result.original(i+j);
					if (result.coded(i+j).equals(DatesConstantsIt.d2.s()))
					{
						month2 = result.original(i+j);
						if (Integer.parseInt(month2)>12)
							isDate = false;
					}
					if (result.coded(i+j).equals(DatesConstantsIt.month01.s()))
						month2 = "01";
					if (result.coded(i+j).equals(DatesConstantsIt.month02.s()))
						month2 = "02";
					if (result.coded(i+j).equals(DatesConstantsIt.month03.s()))
						month2 = "03";
					if (result.coded(i+j).equals(DatesConstantsIt.month04.s()))
						month2 = "04";
					if (result.coded(i+j).equals(DatesConstantsIt.month05.s()))
						month2 = "05";
					if (result.coded(i+j).equals(DatesConstantsIt.month06.s()))
						month2 = "06";
					if (result.coded(i+j).equals(DatesConstantsIt.month07.s()))
						month2 = "07";
					if (result.coded(i+j).equals(DatesConstantsIt.month08.s()))
						month2 = "08";
					if (result.coded(i+j).equals(DatesConstantsIt.month09.s()))
						month2 = "09";
					if (result.coded(i+j).equals(DatesConstantsIt.month10.s()))
						month2 = "10";
					if (result.coded(i+j).equals(DatesConstantsIt.month11.s()))
						month2 = "11";
					if (result.coded(i+j).equals(DatesConstantsIt.month12.s()))
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
					if (result.coded(i+j).equals(DatesConstantsIt.d1.s()))
						day2 = "0" + result.original(i+j);
					if (result.coded(i+j).equals(DatesConstantsIt.d2.s()))
					{
						day2 = result.original(i+j);
						if (Integer.parseInt(day2)>31)
							isDate = false;
					}	
					if (result.coded(i+j).equals(DatesConstantsIt.ordinal01.s()))
						day2 = "01";
					if (result.coded(i+j).equals(DatesConstantsIt.cardinal02.s()))
						day2 = "02";
					if (result.coded(i+j).equals(DatesConstantsIt.cardinal03.s()))
						day2 = "03";
					if (result.coded(i+j).equals(DatesConstantsIt.cardinal04.s()))
						day2 = "04";
					if (result.coded(i+j).equals(DatesConstantsIt.cardinal05.s()))
						day2 = "05";
					if (result.coded(i+j).equals(DatesConstantsIt.cardinal06.s()))
						day2 = "06";
					if (result.coded(i+j).equals(DatesConstantsIt.cardinal07.s()))
						day2 = "07";
					if (result.coded(i+j).equals(DatesConstantsIt.cardinal08.s()))
						day2 = "08";
					if (result.coded(i+j).equals(DatesConstantsIt.cardinal09.s()))
						day2 = "09";
					if (result.coded(i+j).equals(DatesConstantsIt.cardinal10.s()))
						day2 = "10";
					if (result.coded(i+j).equals(DatesConstantsIt.cardinal11.s()))
						day2 = "11";
					if (result.coded(i+j).equals(DatesConstantsIt.cardinal12.s()))
						day2 = "12";
					if (result.coded(i+j).equals(DatesConstantsIt.cardinal13.s()))
						day2 = "13";
					if (result.coded(i+j).equals(DatesConstantsIt.cardinal14.s()))
						day2 = "14";
					if (result.coded(i+j).equals(DatesConstantsIt.cardinal15.s()))
						day2 = "15";
					if (result.coded(i+j).equals(DatesConstantsIt.cardinal16.s()))
						day2 = "16";
					if (result.coded(i+j).equals(DatesConstantsIt.cardinal17.s()))
						day2 = "17";
					if (result.coded(i+j).equals(DatesConstantsIt.cardinal18.s()))
						day2 = "18";
					if (result.coded(i+j).equals(DatesConstantsIt.cardinal19.s()))
						day2 = "19";
					if (result.coded(i+j).equals(DatesConstantsIt.cardinal20.s()))
						day2 = "20";
					if (result.coded(i+j).equals(DatesConstantsIt.cardinal21.s()))
						day2 = "21";
					if (result.coded(i+j).equals(DatesConstantsIt.cardinal22.s()))
						day2 = "22";
					if (result.coded(i+j).equals(DatesConstantsIt.cardinal23.s()))
						day2 = "23";
					if (result.coded(i+j).equals(DatesConstantsIt.cardinal24.s()))
						day2 = "24";
					if (result.coded(i+j).equals(DatesConstantsIt.cardinal25.s()))
						day2 = "25";
					if (result.coded(i+j).equals(DatesConstantsIt.cardinal26.s()))
						day2 = "26";
					if (result.coded(i+j).equals(DatesConstantsIt.cardinal27.s()))
						day2 = "27";
					if (result.coded(i+j).equals(DatesConstantsIt.cardinal28.s()))
						day2 = "28";
					if (result.coded(i+j).equals(DatesConstantsIt.cardinal29.s()))
						day2 = "29";
					if (result.coded(i+j).equals(DatesConstantsIt.cardinal30.s()))
						day2 = "30";
					if (result.coded(i+j).equals(DatesConstantsIt.cardinal31.s()))
						day2 = "31";
					if (!approximation2.isEmpty())
					{
						approximation2 = "";							
					}
					break;
				case APPROXIMATION2:
					// Jahreszeiten
					if (result.coded(i+j).equals(DatesConstantsIt.spring.s()))
					{
						limit2 = "notAfter";
						month2 = "06";
					}
					if (result.coded(i+j).equals(DatesConstantsIt.summer.s()))
					{
						limit2 = "notAfter";
						month2 = "09";
					}
					if (result.coded(i+j).equals(DatesConstantsIt.autumn.s()))
					{
						limit2 = "notAfter";
						month2 = "12";
					}
					if (result.coded(i+j).equals(DatesConstantsIt.winter.s()))
					{
						limit2 = "notAfter";
						month2 = "12";
					}
					// Wochentage
					if (result.coded(i+j).equals(DatesConstantsIt.monday.s()))
						weekday2 = "1";
					if (result.coded(i+j).equals(DatesConstantsIt.tuesday.s()))
						weekday2 = "2";
					if (result.coded(i+j).equals(DatesConstantsIt.wednesday.s()))
						weekday2 = "3";
					if (result.coded(i+j).equals(DatesConstantsIt.thursday.s()))
						weekday2 = "4";
					if (result.coded(i+j).equals(DatesConstantsIt.friday.s()))
						weekday2 = "5";
					if (result.coded(i+j).equals(DatesConstantsIt.saturday.s()))
						weekday2 = "6";
					if (result.coded(i+j).equals(DatesConstantsIt.sunday.s()))
						weekday2 = "7";
					// Feste
					if (result.coded(i+j).equals(DatesConstantsIt.newYear.s()))
					{
						day2 = "01";
						month2 = "01";
						approximation2 = "";							
					}
					if (result.coded(i+j).equals(DatesConstantsIt.ordinal01.s()))
						holyday2 = "01";
					if (result.coded(i+j).equals(DatesConstantsIt.year.s()))
					{
						if (holyday2.equals("01"))
						{
							day2 = "01";
							month2 = "01";
							approximation2 = "";
						}
					}
					if (result.coded(i+j).equals(DatesConstantsIt.epiphany.s()))
					{
						day2 = "06";
						month2 = "01";
						approximation2 = "";							
					}
					if (result.coded(i+j).equals(DatesConstantsIt.valetine.s()))
					{
						day2 = "14";
						month2 = "02";
						approximation2 = "";							
					}
					if (result.coded(i+j).equals(DatesConstantsIt.ferragosto.s()))
					{
						day2 = "15";
						month2 = "08";
						approximation2 = "";							
					}
					if (result.coded(i+j).equals(DatesConstantsIt.assumption.s()))
						holyday2 = "MariäHimmelfahrt";
					if (result.coded(i+j).equals(DatesConstantsIt.maria.s()))
					{
						if (holyday2.equals("MariäHimmelfahrt"))
						{
							day2 = "15";
							month2 = "08";
							approximation2 = "";
						}
					}
					if (result.coded(i+j).equals(DatesConstantsIt.allHallows.s()))
					{
						day2 = "01";
						month2 = "11";
						approximation2 = "";							
					}
					if (result.coded(i+j).equals(DatesConstantsIt.all.s()))
						holyday2 = "Allerheiligen";
					if (result.coded(i+j).equals(DatesConstantsIt.saint.s()))
					{
						if (holyday2.equals("Allerheiligen"))
						{
							day2 = "01";
							month2 = "11";
							approximation2 = "";
						}
					}
					if (result.coded(i+j).equals(DatesConstantsIt.allSouls.s()))
					{
						day2 = "02";
						month2 = "11";
						approximation2 = "";							
					}
					if (result.coded(i+j).equals(DatesConstantsIt.immaculateConception.s()))
					{
						if (holyday2.equals("MariäEmpfängnis"))
						{
							day2 = "08";
							month2 = "12";
							approximation2 = "";
						}
						else
							holyday2 = "MariäEmpfängnis";
					}
					if (result.coded(i+j).equals(DatesConstantsIt.christmas.s()))
					{
						day2 = "25";
						month2 = "12";
						approximation2 = "";							
					}
					if (result.coded(i+j).equals(DatesConstantsIt.stephan.s()))
					{
						day2 = "26";
						month2 = "12";
						approximation2 = "";							
					}
					if (result.coded(i+j).equals(DatesConstantsIt.silvester.s()))
					{
						day2 = "31";
						month2 = "12";
						approximation2 = "";							
					}
					
					// bewegliche Feiertage
					if (result.coded(i+j).equals(DatesConstantsIt.carnivalWeek.s()))
					{
						if (weekday2.equals("4")) 
							easter2 = "-52";
						if (weekday2.equals("5")) 
							easter2 = "-51";
						if (weekday2.equals("6")) 
							easter2 = "-50";
						if (weekday2.equals("7")) 
							easter2 = "-49";
						if (weekday2.equals("1")) 
							easter2 = "-48";
						if (weekday2.equals("2"))
							easter2 = "-47";
						if (weekday2.equals("3")) 
							easter2 = "-46";
					}
					if (result.coded(i+j).equals(DatesConstantsIt.carnival.s()))
						easter2 = "-49";
					if (result.coded(i+j).equals(DatesConstantsIt.palmSunday.s()))
						easter2 = "-7";
					if (result.coded(i+j).equals(DatesConstantsIt.saint.s()))
					{
						if (weekday2.equals("1")) 
							easter2 = "-6";
						if (weekday2.equals("2")) 
							easter2 = "-5";
						if (weekday2.equals("3")) 
							easter2 = "-4";
						if (weekday2.equals("4")) 
							easter2 = "-3";
						if (weekday2.equals("5"))
							easter2 = "-2";
						if (weekday2.equals("6")) 
							easter2 = "-1";
						if (weekday2.equals("7")) 
							easter2 = "0";
					}
					if (result.coded(i+j).equals(DatesConstantsIt.easter.s()))
						easter2 = "0";
					if (result.coded(i+j).equals(DatesConstantsIt.easterMonday.s()))
						easter2 = "1";
					if (result.coded(i+j).equals(DatesConstantsIt.angel.s()))
						easter2 = "1";
					if (result.coded(i+j).equals(DatesConstantsIt.ascension.s()))
						easter2 = "39";
					if (result.coded(i+j).equals(DatesConstantsIt.pentecost.s()))
					{
						if (weekday2.equals("1"))
							easter2 = "50";
						else
							easter2 = "49";
					}
					if (result.coded(i+j).equals(DatesConstantsIt.corpusChristi.s()))
						easter2 = "60";
					
					// für Jahrhundertangaben
					if (result.coded(i+j).equals(DatesConstantsIt.century.s()))
					{
						isCentury2 = true;
					}
					if (result.coded(i+j).equals(DatesConstantsIt.d2.s()))
						century2 = result.original(i+j);
					if (result.coded(i+j).equals(DatesConstantsIt.ordinal16.s()))
						century2 = "16";
					if (result.coded(i+j).equals(DatesConstantsIt.ordinal17.s()))
						century2 = "17";
					if (result.coded(i+j).equals(DatesConstantsIt.ordinal18.s()))
						century2 = "18";
					if (result.coded(i+j).equals(DatesConstantsIt.ordinal19.s()))
						century2 = "19";
					if (result.coded(i+j).equals(DatesConstantsIt.ordinal20.s()))
						century2 = "20";
					if (result.coded(i+j).equals(DatesConstantsIt.ordinal21.s()))
						century2 = "21";
					if (result.coded(i+j).equals(DatesConstantsIt.century16th.s()))
					{
						isCentury2 = true;
						century2 = "16";
					}
					if (result.coded(i+j).equals(DatesConstantsIt.century17th.s()))
					{
						isCentury2 = true;
						century2 = "17";
					}
					if (result.coded(i+j).equals(DatesConstantsIt.century18th.s()))
					{
						isCentury2 = true;
						century2 = "18";
					}
					if (result.coded(i+j).equals(DatesConstantsIt.century19th.s()))
					{
						isCentury2 = true;
						century2 = "19";
					}
					if (result.coded(i+j).equals(DatesConstantsIt.century20th.s()))
					{
						isCentury2 = true;
						century2 = "20";
					}
					if (result.coded(i+j).equals(DatesConstantsIt.ordinal10.s()))
					{
						if (century2.equals("6") || century2.equals("7") ||
								century2.equals("8") || century2.equals("9"))
							century2 = "1" + century2;
						else century2 = "10";
					}
					if (result.coded(i+j).equals(DatesConstantsIt.ordinal06.s()))
					{
						if (century2.equals("10"))
							century2 = "16";
						else century2 = "6";
					}
					if (result.coded(i+j).equals(DatesConstantsIt.ordinal07.s()))
					{
						if (century2.equals("10"))
							century2 = "17";
						else century2 = "7";
					}
					if (result.coded(i+j).equals(DatesConstantsIt.ordinal08.s()))
					{
						if (century2.equals("10"))
							century2 = "18";
						else century2 = "8";
					}
					if (result.coded(i+j).equals(DatesConstantsIt.ordinal09.s()))
					{
						if (century2.equals("10"))
							century2 = "19";
						else century2 = "9";
					}
					if (result.coded(i+j).equals(DatesConstantsIt.apostrophe.s()))
						isCentury2 = true;
					if (result.coded(i+j).equals(DatesConstantsIt.d3.s()))
					{
						if (result.original(i+j).substring(1).equals("00"))
							century2 = (Integer.parseInt(result.original(i+j).substring(0, 1))+ 11) + "";
					}
					if (isCentury2 && !century2.isEmpty())
					{
						if (Integer.parseInt(century2)<=15)
							isDate = false;
						if (Integer.parseInt(century2)>21)
							isDate = false;
						if (Integer.parseInt(century2)>15 &&
								Integer.parseInt(century2)<=21)
						{
							isDate = true;
							isCentury2 = false;
							year2 = (Integer.parseInt(century2)) + "00";
							limit2 = "notAfter";
							if (!approximation2.isEmpty())
							{
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
					// 1. Hälfte, Anfang, Mitte, etc.
					if (result.coded(i+j).equals(DatesConstantsIt.half.s()))
					{
						if (result.original(i+j).equals("metà"))
							approximation2 = "2/3";
						if (result.coded(i+j-2).equals(DatesConstantsIt.notAfterFirst.s()))
							approximation2 = "1/2";
						if (result.coded(i+j-2).equals(DatesConstantsIt.ordinal01.s()))
							approximation2 = "1/2";
						if (result.coded(i+j-2).equals(DatesConstantsIt.ordinal02.s()))
							approximation2 = "2/2";
					}
					if (result.coded(i+j).equals(DatesConstantsIt.approximation.s()))
					{
						if (result.original(i+j).equals("inizio"))
							approximation2 = "1/3";
						if (result.original(i+j).equals("Inizio"))
							approximation2 = "1/3";
						if (result.original(i+j).equals("fine"))
							approximation2 = "3/3";
						if (result.original(i+j).equals("Fine"))
							approximation2 = "3/3";
					}
					break;
				default:
					break;
				}
			}
			{	
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
		 *  Sucht den italienischen Text nach den Startelemente
		 *  von erkannten Daten durch und formatiert das jeweilige Datum.
		 *  @param result Das zu bearbeitende DatesResult-Objekt.
		 */
		private static void format(DatesResult result){
			boolean isDate = false;
			for (int i=0; i<result.length(); i++){
				if (result.identifiedLength(i)==0)
					isDate = false;
				else{
					if (!isDate){
						formatDate_it(result, i);
					}
					isDate = true;
				}
			}
		}
		
		/**
		 * Findet italienische Datumsangaben im Eingabetext des übergebenen
		 * Ergebnis-Objects.
		 */
		static void findOccurrences(DatesResult result){
			code(result);
			identify(result);
			format(result);
		}

		
	}
	

