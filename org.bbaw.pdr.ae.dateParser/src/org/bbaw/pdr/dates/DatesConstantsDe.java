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
 * Die Klasse enthält die statischen Objekte zur Kodierung des Originaltextes.
 * @author Martin Fechner
 */
class DatesConstantsDe {
	// alle allgemein möglichen Elemente (G)
	/** Ein beliebiges Wort. */
	static DatesConstant word = new DatesConstant(
			"sGwo", "(\\p{L}+)");
	
	/** Eine beliebige Dezimalzahl. */
	static DatesConstant d = new DatesConstant(
			"sGd_", "(\\d+)");
	
	/** Ein gemischter Ausdruck mit Buchstaben und Zahlen. */
	static DatesConstant miscellaneous = new DatesConstant(
			"sGmi", "(.*(\\d\\p{L}|\\p{L}\\d).*)");
	
	/** Ein Leerzeichen. */
	static DatesConstant space = new DatesConstant(
			"sGsp", "(\\s)");
	
	/** Ein Interpunktionszeichen. */
	static DatesConstant punctuation = new DatesConstant(
			"sGpu", "(\\p{P})");
	
	/** Eine beliebige Zeichenkombination, die keine Zahl oder Wort ist. */
	static DatesConstant other = new DatesConstant(
			"sGot", "(.*[^\\d\\p{L}\\s\\p{P}].*)");
	
	// verschiedenstellige Dezimalzahlen (D)
	/** Eine einstellige Dezimalzahl. */
	static DatesConstant d1 = new DatesConstant(
			"sD1_", "(\\d)");
	
	/** Eine zweistellige Dezimalzahl. */
	static DatesConstant d2 = new DatesConstant(
			"sD2_", "(\\d\\d)");
	
	/** Eine vierstellige Dezimalzahl. */
	static DatesConstant d4 = new DatesConstant(
			"sD4_", "(\\d\\d\\d\\d)");

	// augeschriebene Kardinalzahlen (C)
	/** Die Zahl eins. */
	static DatesConstant cardinal01 = new DatesConstant(
			"sC01", "(Eins|eins)");
	
	/** Die Zahl zwei. */
	static DatesConstant cardinal02 = new DatesConstant(
			"sC02", "(Zwei|zwei)");
	
	/** Die Zahl drei. */
	static DatesConstant cardinal03 = new DatesConstant(
			"sC03", "(Drei|drei)");
	
	/** Die Zahl vier. */
	static DatesConstant cardinal04 = new DatesConstant(
			"sC04", "(Vier|vier)");
	
	/** Die Zahl fünf. */
	static DatesConstant cardinal05 = new DatesConstant(
			"sC05", "(Fünf|fünf)");
	
	/** Die Zahl sechs. */
	static DatesConstant cardinal06 = new DatesConstant(
			"sC03", "(Sechs|sechs)");
	
	/** Die Zahl sieben. */
	static DatesConstant cardinal07 = new DatesConstant(
			"sC07", "(Sieben|sieben)");
	
	// feste Feiertage (H)
	/** Neujahr : 01.01. */
	static DatesConstant newYear = new DatesConstant(
			"sHny", "(Neujahr)");
	
	/** Heilige Drei Könige: 06.01. */
	static DatesConstant epiphany = new DatesConstant(
			"sHep", "(Könige)");
	
	/** St. Valentin: 14.02. */
	static DatesConstant stValetinesDay = new DatesConstant(
			"sHvd", "(Valentinstag)");
	
	/** Allerheiligen: 01.11. */
	static DatesConstant allHallows = new DatesConstant(
			"sHah", "(Allerheiligen)");
	
	/** Allerseelen: 02.11. */
	static DatesConstant allSouls = new DatesConstant(
			"sHas", "(Allerseelen)");
	
	/** Mariä Empfängnis: 08.12. */
	static DatesConstant immaculateConception = new DatesConstant(
			"sHic", "(Unbefleckte|Empfängnis)");
	
	/** Heiligabend: 24.12. */
	static DatesConstant christmasEve = new DatesConstant(
			"sHxe", "(Heiligabend)");
	
	/** Weihnachten: 25.12. */
	static DatesConstant christmas = new DatesConstant(
			"sHxm", "(Weihnachten)");
	
	/** 1. oder 2. Weihnachtstag: 25. bzw. 26.12. */
	static DatesConstant christmasDay = new DatesConstant(
			"sHxd", "(Weihnachtstag|Weihnachtsfeiertag)");
	
	/** Stefanstag: 26.12. */
	static DatesConstant stStephansDay = new DatesConstant(
			"sHsd", "(Stefanstag)");
	
	// bewegliche Feiertage um Ostern (E)
	/** Weiberfastnacht: 52 Tage vor Ostern */
	static DatesConstant carnivalThursday = new DatesConstant(
			"sEc4", "(Weiberfastnacht)");
	
	/** Rosenmontag: 48 Tage vor Ostern */
	static DatesConstant carnivalMonday = new DatesConstant(
			"sEc1", "(Rosenmontag)");
	
	/** Fastnachtsdienstag: 47 Tage vor Ostern */
	static DatesConstant carnivalTuesday = new DatesConstant(
			"sEc2", "(Fastnachtsdienstag|Faschingsdienstag)");
	
	/** Aschermittwoch: 46 Tage vor Ostern */
	static DatesConstant carnivalWednesday = new DatesConstant(
			"sEc3", "(Aschermittwoch)");
	
	/** Palmsonntag: 7 Tage vor Ostern */
	static DatesConstant palmSunday = new DatesConstant(
			"sEps", "(Palmsonntag)");
	
	/** Gründonnerstag: 3 Tage vor Ostern */
	static DatesConstant holyThursday = new DatesConstant(
			"sEh4", "(Gründonnerstag)");
	
	/** Karfreitag: 2 Tage vor Ostern */
	static DatesConstant holyFriday = new DatesConstant(
			"sEh5", "(Karfreitag)");
	
	/** Karsamstag: 1 Tag vor Ostern */
	static DatesConstant holySaturday = new DatesConstant(
			"sEh6", "(Karsamstag|Karsonnabend)");
	
	/** Ostern */
	static DatesConstant Easter = new DatesConstant(
			"sE00", "(Ostern|Ostersonntag)");
	
	/** Ostermontag: 1 Tag nach Ostern */
	static DatesConstant easterMonday = new DatesConstant(
			"sE01", "(Ostermontag)");
	
	/** Christi Himmelfahrt: 39 Tage nach Ostern */
	static DatesConstant ascension = new DatesConstant(
			"sE39", "(Himmelfahrt)");
	
	/** Pfingsten: 49 Tage nach Ostern */
	static DatesConstant pentecost = new DatesConstant(
			"sE49", "(Pfingsten|Pfingstsonntag)");
	
	/** Pfingstmontag: 50 Tage nach Ostern */
	static DatesConstant pentecostMonday = new DatesConstant(
			"sE50", "(Pfingstmontag)");
	
	/** Fronleichnam: 60 Tage nach Ostern */
	static DatesConstant corpusChristi = new DatesConstant(
			"sE60", "(Fronleichnam)");
	
	// die verschiedenen Monate (M)
	/** Eine Monatsangabe für Januar. */
	static DatesConstant month01 = new DatesConstant(
			"sM01", "(Januar|Jänner|Jan)");
	
	/** Eine Monatsangabe für Februar. */
	static DatesConstant month02 = new DatesConstant(
			"sM02", "(Februar|Feb)");
	
	/** Eine Monatsangabe für März. */
	static DatesConstant month03 = new DatesConstant(
			"sM03", "(März|Maerz)");
	
	/** Eine Monatsangabe für April. */
	static DatesConstant month04 = new DatesConstant(
			"sM04", "(April)");
	
	/** Eine Monatsangabe für Mai. */
	static DatesConstant month05 = new DatesConstant(
			"sM05", "(Mai)");
	
	/** Eine Monatsangabe für Juni. */
	static DatesConstant month06 = new DatesConstant(
			"sM06", "(Juni)");
	
	/** Eine Monatsangabe für Juli. */
	static DatesConstant month07 = new DatesConstant(
			"sM07", "(Juli)");
	
	/** Eine Monatsangabe für August. */
	static DatesConstant month08 = new DatesConstant(
			"sM08", "(August|Aug)");
	
	/** Eine Monatsangabe für September. */
	static DatesConstant month09 = new DatesConstant(
			"sM09", "(September|Sept|Sep)");
	
	/** Eine Monatsangabe für Oktober. */
	static DatesConstant month10 = new DatesConstant(
			"sM10", "(Oktober|Okt)");
	
	/** Eine Monatsangabe für November. */
	static DatesConstant month11 = new DatesConstant(
			"sM11", "(November|Nov)");
	
	/** Eine Monatsangabe für Dezember. */
	static DatesConstant month12 = new DatesConstant(
			"sM12", "(Dezember|December|Dez)");

	// Jahreszeiten
	/** Eine Angabe für Frühling */
	static DatesConstant spring = new DatesConstant(
			"sSsp", "(Frühling)");
	
	/** Eine Angabe für den Sommer */
	static DatesConstant summer = new DatesConstant(
			"sSsu", "(Sommer)");
	
	/** Eine Angabe für den Herbst */
	static DatesConstant autumn = new DatesConstant(
			"sSau", "(Herbst)");
	
	/** Eine Angabe für den Winter */
	static DatesConstant winter = new DatesConstant(
			"sSwi", "(Winter)");
	
	// Jahrhundert (C)
	/** Eine Jahrhundertangabe */
	static DatesConstant century = new DatesConstant(
			"sCce", "(Jahrhundert|Jh|Jahrh|Jahrhunderts)");

	// mögliche Angaben zu Zeitpunkten und Zeiträumen (A)
	/** Ein Zeitpunkt. */
	static DatesConstant at = new DatesConstant(
			"sAat", "(Am|am|Zum|zum)");
	
	/** Ein ungefährer Zeitpunkt. */
	static DatesConstant around = new DatesConstant(
			"sAar", "(Ca|ca|Etwa|etwa|Um|um)");
	
	/** Eine Näherungsangabe eines Zeitpunkts. */
	static DatesConstant approximation = new DatesConstant(
			"sAap", "(Anfang|Anf|Ende|Mitte)");
	
	/** Eine Näherungsangabe eines Zeitpunkts. */
	static DatesConstant half = new DatesConstant(
			"sAha", "(Hälfte)");

	/** Die vordere Grenze eines Zeitraums. */
	static DatesConstant from = new DatesConstant(
			"sAfr", "(Ab|ab|Seit|seit)");
	
	/** Die vordere Grenze eines Zeitraums. */
	static DatesConstant notBefore = new DatesConstant(
			"sAnb", "(Nach|nach)");
	
	/** Die hintere Grenze eines Zeitraums, sowie als Verbindung zwischen zwei Angaben. */
	static DatesConstant to = new DatesConstant(
			"sAto", "(Bis|bis)");
	
	/** Die hintere Grenze eines Zeitraums. */
	static DatesConstant notAfter = new DatesConstant(
			"sAna", "(Vor|vor)");
	
	/** Der Hinweis auf einen Zeitraum. */
	static DatesConstant between = new DatesConstant(
			"sAbe", "(Zwischen|zwischen)");
	
	// zur Verbindung zweier Datumsangaben (L)
	/** Eine Verbindungselement. */
	static DatesConstant link = new DatesConstant(
			"sLli", "(-|/|-|-|–)");
		
	/** Eine Verbindungselement. */
	static DatesConstant and = new DatesConstant(
			"sLan", "(und)");
	
	// besondere Namen (N)
	/** Jesus */
	static DatesConstant Jesus = new DatesConstant(
			"sNJe", "(Christi)");
	
	/** Maria. */
	static DatesConstant maria = new DatesConstant(
			"sNma", "(Maria|Mariä)");
	
	/** St. Silvester: 31.12. */
	static DatesConstant silvester = new DatesConstant(
			"sNsi", "(Silvester)");

	// andere spezielle Worte (V)
	/** Ein Artikel, der im Zusammenhang mit Zeitangaben benutzt wird. */
	static DatesConstant article = new DatesConstant(
			"sVar", "(dem|den|des)");
			
	/** Der Heilige. */
	static DatesConstant saint = new DatesConstant(
			"sVsa", "(Heilige|Heiliger)");
	
	// spezielle Zeichen (P)
	
}
