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
 * Die Klasse enthält die Konstanten für die Datumserkennung im Kode.
 * Sie enthält die statischen Muster der Kodesymbole zum
 * Erkennen der Datumsangaben im Kode, und die jeweiligen
 * Bezeichnungen für die spätere Formatierung.
 * @author Martin Fechner
 */
class DatesSymbolsDe {
	/*
	 * Es folgen die lokalen Konstanten für die Bezeichner
	 */
	static private final DatesSymbol.Label EMPTY = DatesSymbol.Label.EMPTY;
	static private final DatesSymbol.Label YEAR1 = DatesSymbol.Label.YEAR1;
	static private final DatesSymbol.Label YEAR2 = DatesSymbol.Label.YEAR2;
	static private final DatesSymbol.Label MONTH1 = DatesSymbol.Label.MONTH1;
	static private final DatesSymbol.Label DAY1 = DatesSymbol.Label.DAY1;
	static private final DatesSymbol.Label APPROXIMATION1 = DatesSymbol.Label.APPROXIMATION1;
	static private final DatesSymbol.Label LIMIT1 = DatesSymbol.Label.LIMIT1;
	static private final DatesSymbol.Label CONNECTION = DatesSymbol.Label.CONNECTION;
	/*
	 * Die lokalen Konstanten für die Muster
	 */
	/**
	 * Lokale Konstante fasst die Kardinalzahlen 1-7 zusammen.
	 * Bsp.: "eins" oder "Drei"
	 */
	static private String cardinal1_7 =
		"(" + DatesConstantsDe.cardinal01.s() + "|" + DatesConstantsDe.cardinal02.s()
		+ "|" + DatesConstantsDe.cardinal03.s() + "|" + DatesConstantsDe.cardinal04.s()
		+ "|" + DatesConstantsDe.cardinal05.s() + "|" + DatesConstantsDe.cardinal06.s()
		+ "|" + DatesConstantsDe.cardinal07.s() + ")";

	/** Lokale Konstante für einen Tag. Bsp.: "3" oder "03" */
	static private String day = 
		"(" + DatesConstantsDe.d1.s() + "|" + DatesConstantsDe.d2.s() + ")";

	/**
	 * Lokale Konstante fasst die beweglichen und festen Feiertage zusammen.
	 * Bsp.: "Ostern" oder "Weihnachten"
	 */
	static private String holyday =
		"(" + DatesConstantsDe.newYear.s() + "|" +  DatesConstantsDe.stValetinesDay.s()
		+ "|" + DatesConstantsDe.allHallows.s() + "|" + DatesConstantsDe.allSouls.s()
		+ "|" + DatesConstantsDe.christmasEve.s() + "|" + DatesConstantsDe.christmas.s()
		+ "|" + DatesConstantsDe.stStephansDay.s() + "|" + DatesConstantsDe.silvester.s()
		+ "|" + DatesConstantsDe.carnivalThursday.s() + "|" + DatesConstantsDe.carnivalMonday.s()
		+ "|" + DatesConstantsDe.carnivalTuesday.s() + "|" + DatesConstantsDe.carnivalWednesday.s()
		+ "|" + DatesConstantsDe.palmSunday.s() + "|" + DatesConstantsDe.holySaturday.s()
		+ "|" + DatesConstantsDe.holyThursday.s() + "|" + DatesConstantsDe.holyFriday.s()
		+ "|" + DatesConstantsDe.Easter.s() + "|" + DatesConstantsDe.easterMonday.s()
		+ "|" + DatesConstantsDe.ascension.s() + "|" + DatesConstantsDe.pentecost.s()
		+ "|" + DatesConstantsDe.pentecostMonday.s() + "|" + DatesConstantsDe.corpusChristi.s() + ")";

	/** Lokale Konstante für einen Zeitpunkt oder die Grenze eines Zeitraums. Bsp.: "von" oder "am" */
	static private String limit =
		"(" + DatesConstantsDe.from.s() + "|" + DatesConstantsDe.at.s()
		+ "|" + DatesConstantsDe.notBefore.s() + "|" + DatesConstantsDe.notAfter.s()
		+ "|" + DatesConstantsDe.around.s() + ")";

	/** Lokale Konstante für einen Monat. Bsp.: "1" oder "01" */
	static private String month = 
		"(" + DatesConstantsDe.d1.s() + "|" + DatesConstantsDe.d2.s() + ")";

	/** 
	 * Lokale Konstante fasst die ausgeschriebenen Monatsnamen zusammen.
	 * Bsp.: "Januar" oder "Oktober" oder "Okt" 
	 */
	static private String monthname = 
		"(" + DatesConstantsDe.month01.s() + "|" + DatesConstantsDe.month02.s()
		+ "|" + DatesConstantsDe.month03.s() + "|" + DatesConstantsDe.month04.s()
		+ "|" + DatesConstantsDe.month05.s() + "|" + DatesConstantsDe.month06.s()
		+ "|" + DatesConstantsDe.month07.s() + "|" + DatesConstantsDe.month08.s()
		+ "|" + DatesConstantsDe.month09.s() + "|" + DatesConstantsDe.month10.s()
		+ "|" + DatesConstantsDe.month11.s() + "|" + DatesConstantsDe.month12.s() + ")";

	/** Lokale Konstante für eine Interpunktionszeichen. Bsp.: "." */
	static private String punct =
		"(" + DatesConstantsDe.punctuation.s() + ")";

	/** 
	 * Lokale Konstante fasst die Jahreszeiten zusammen.
	 * Bsp.: "Frühling" oder "Winter"
	 */
	static private String season =
		"(" + DatesConstantsDe.spring.s() + "|" + DatesConstantsDe.summer.s()
		+ "|" + DatesConstantsDe.autumn.s() + "|" + DatesConstantsDe.winter.s() + ")";

	/** Lokale Konstante für ein Leerzeichen. Bsp.: " " */
	static private String space =
		"(" + DatesConstantsDe.space.s() + ")";

	/** Lokale Konstante für eine vierstellige Jahresangabe. Bsp.: "1990" */
	static private String year = 
		"(" + DatesConstantsDe.d4.s() + ")";

	/*
	 * Es folgen die verschiedenen Datumsmuster.
	 */
	// VOLLSTAENDIGE DATUMSANGABEN
	/*
	 * Einfache Jahresangaben
	 */
	/** Einfache Jahresangabe. Bsp.: "1990" */
	static DatesSymbol yyyy = new DatesSymbol(
			new DatesSymbol.Label[]{YEAR1},
			new String[]{year});
	/*
	 * Einfache Jahrhundertangaben
	 */
	/** Einfache Angabe eines Jahrhunderts. Bsp.: "20. Jahrhundert" */
	static DatesSymbol yyp_century = new DatesSymbol(
			new DatesSymbol.Label[]{APPROXIMATION1, APPROXIMATION1, EMPTY,
					APPROXIMATION1},	
			new String[]{DatesConstantsDe.d2.s(), punct, space,
					DatesConstantsDe.century.s()});

	/** Einfache Angabe eines Jahrhunderts. Bsp.: "20. Jh." */
	static DatesSymbol yyp_centuryp = new DatesSymbol(
			new DatesSymbol.Label[]{APPROXIMATION1, APPROXIMATION1, EMPTY,
					APPROXIMATION1, APPROXIMATION1},
			new String[]{DatesConstantsDe.d2.s(), punct, space,
					DatesConstantsDe.century.s(), punct});
	/*
	 * Datumsangaben mit Jahr und Monat
	 */
	/** Monats- und Jahresangabe. Bsp.: "10.1990" */
	static DatesSymbol mmpyyyy = new DatesSymbol(
			new DatesSymbol.Label[]{MONTH1, MONTH1, YEAR1},
			new String[]{month, punct, year});
	
	/** Monats- und Jahresangabe. Bsp.: "Oktober 1990" */
	static DatesSymbol month_yyyy = new DatesSymbol(
			new DatesSymbol.Label[]{MONTH1, EMPTY, YEAR1},
			new String[]{monthname, space, year});

	/** Monats- und Jahresangabe. Bsp.: "Okt. 1990" */
	static DatesSymbol monthp_yyyy = new DatesSymbol(
			new DatesSymbol.Label[]{MONTH1, MONTH1, EMPTY, YEAR1},
			new String[]{monthname, punct, space, year});
	/*
	 * Datumsmuster mit Jahr, Monat und Tag
	 */
	/** Vollständiges Datum mit Tag, Monat und Jahr. Bsp.: "3.10.1990" */
	static DatesSymbol dd_mm_yyyy = new DatesSymbol(
			new DatesSymbol.Label[]{DAY1, DAY1, MONTH1, MONTH1, YEAR1},
			new String[]{day, punct, month, punct, year});

	/** Vollständiges Datum mit Tag, Monat und Jahr. Bsp.: "3. Oktober 1990" */
	static DatesSymbol dd_month_yyyy = new DatesSymbol(
			new DatesSymbol.Label[]{DAY1, DAY1, EMPTY, MONTH1, EMPTY, YEAR1},
			new String[]{day, punct, space, monthname, space, year});

	/** Vollständiges Datum mit Tag, Monat und Jahr. Bsp.: "3. Okt. 1990" */
	static DatesSymbol dd_monthp_yyyy = new DatesSymbol(
			new DatesSymbol.Label[]{DAY1, DAY1, EMPTY, MONTH1, MONTH1,
					EMPTY, YEAR1},
			new String[]{day, punct, space, monthname, punct, space, year});
	
	/** Eine vollständige Datumsangabe im ISO-Format. Bsp.: "1990-10-03" */
	static DatesSymbol yyyy_mm_dd = new DatesSymbol(
			new DatesSymbol.Label[]{YEAR1, EMPTY, MONTH1, EMPTY, DAY1},
			new String[]{year, DatesConstantsDe.link.s(),
					DatesConstantsDe.d2.s(), DatesConstantsDe.link.s(),
					DatesConstantsDe.d2.s()});
	/*
	 * Datumsangabe mit Jahreszeiträumen
	 */
	/** Jahreszeitenangabe und Jahresangabe. Bsp.: "Herbst 1990" */
	static DatesSymbol season_yyyy = new DatesSymbol(
			new DatesSymbol.Label[]{APPROXIMATION1, EMPTY, YEAR1},
			new String[]{season, space, year});
	/*
	 * Bestimmte Feiertage
	 */
	/** Feiertagsangabe mit Jahresangabe. Bsp.: "Ostern 1990" */
	static DatesSymbol holyday_yyyy = new DatesSymbol(
			new DatesSymbol.Label[]{APPROXIMATION1, EMPTY, YEAR1},
			new String[]{holyday, space, year});

	/** Mariä Empfängnis mit Jahresangabe. Bsp.: "Unbefleckte Empfängnis 1990" */
	static DatesSymbol immacolate_conception_yyyy = new DatesSymbol(
			new DatesSymbol.Label[]{APPROXIMATION1, EMPTY, APPROXIMATION1,
					EMPTY, YEAR1},
			new String[]{DatesConstantsIt.immaculateConception.s(), space,
					DatesConstantsIt.immaculateConception.s(), space, year});
	
	/** Christi Himmelfahrt mit Jahresangabe. Bsp.: "Christi Himmelfahrt 1990" */
	static DatesSymbol jesus_ascension_yyyy = new DatesSymbol(
			new DatesSymbol.Label[]{APPROXIMATION1, EMPTY, APPROXIMATION1,
					EMPTY, YEAR1},
			new String[]{DatesConstantsDe.Jesus.s(), space,
					DatesConstantsDe.ascension.s(), space, year});
	
	/** Mariä Himmelfahrt mit Jahresangabe. Bsp.: "Mariä Himmelfahrt 1990" */
	static DatesSymbol maria_assumption_yyyy = new DatesSymbol(
			new DatesSymbol.Label[]{APPROXIMATION1, EMPTY, APPROXIMATION1,
					EMPTY, YEAR1},
			new String[]{DatesConstantsDe.maria.s(), space,
					DatesConstantsDe.ascension.s(), space, year});

	/** Mariä Empfängnis mit Jahresangabe. Bsp.: "Mariä Empfängnis 1990" */
	static DatesSymbol maria_conception_yyyy = new DatesSymbol(
			new DatesSymbol.Label[]{APPROXIMATION1, EMPTY, APPROXIMATION1,
					EMPTY, YEAR1},
			new String[]{DatesConstantsIt.maria.s(), space,
					DatesConstantsIt.immaculateConception.s(), space, year});
	
	/** Weihnachtsfeiertage mit Jahresangabe. Bsp.: "1. Weihnachstfeiertag 1990" */
	static DatesSymbol np_christmasday_yyyy = new DatesSymbol(
			new DatesSymbol.Label[]{APPROXIMATION1, APPROXIMATION1, EMPTY,
					APPROXIMATION1, EMPTY, YEAR1},
			new String[]{DatesConstantsDe.d1.s(), punct, space,
					DatesConstantsDe.christmasDay.s(), space, year});

	/** Heilige Drei Könige mit Jahresangabe. Bsp.: "Heilige Drei Könige 1990" */
	static DatesSymbol st_cardinal_epiphany_yyyy = new DatesSymbol(
			new DatesSymbol.Label[]{APPROXIMATION1, EMPTY, APPROXIMATION1,
					EMPTY, APPROXIMATION1, EMPTY, YEAR1},
			new String[]{DatesConstantsDe.saint.s(), space, cardinal1_7,
					space, DatesConstantsDe.epiphany.s(), space, year});
	// VORSAETZE VOR DATUMSANGABEN
	/*
	 * Näherungsangaben
	 */
	/** Näherungsangabe vor einem Datum. Bsp.: "Anfang " oder "Ende " */
	static DatesSymbol approx_ = new DatesSymbol(
			new DatesSymbol.Label[]{APPROXIMATION1, EMPTY},
			new String[]{DatesConstantsDe.approximation.s(), space});

	/** Näherungsangabe vor einem Datum. Bsp.: "Anfang des " oder "Ende des " */
	static DatesSymbol approx_art_ = new DatesSymbol(
			new DatesSymbol.Label[]{APPROXIMATION1, EMPTY, APPROXIMATION1,
					EMPTY},
			new String[]{DatesConstantsDe.approximation.s(), space,
					DatesConstantsDe.article.s(), space});

	/** Näherungsangabe vor einem Datum. Bsp.: "Anf. " */
	static DatesSymbol approxp_ = new DatesSymbol(
			new DatesSymbol.Label[]{APPROXIMATION1, APPROXIMATION1, EMPTY},
			new String[]{DatesConstantsDe.approximation.s(), punct, space});
	
	/** Näherungsangabe vor einem Datum. Bsp.: "Anf. des " */
	static DatesSymbol approxp_art_ = new DatesSymbol(
			new DatesSymbol.Label[]{APPROXIMATION1, APPROXIMATION1, EMPTY,
					APPROXIMATION1, EMPTY},
			new String[]{DatesConstantsDe.approximation.s(), punct, space,
					DatesConstantsDe.article.s(), space});

	/** Näherungsangabe vor einem Datum. Bsp.: "1. Hälfte " */
	static DatesSymbol np_half_ = new DatesSymbol(
			new DatesSymbol.Label[]{APPROXIMATION1, APPROXIMATION1, EMPTY,
					APPROXIMATION1, EMPTY},
			new String[]{DatesConstantsDe.d1.s(), punct, space,
					DatesConstantsDe.half.s(), space});

	/** Näherungsangabe vor einem Datum. Bsp.: "1. Hälfte des" */
	static DatesSymbol np_half_art_ = new DatesSymbol(
			new DatesSymbol.Label[]{APPROXIMATION1, APPROXIMATION1, EMPTY,
					APPROXIMATION1, EMPTY, APPROXIMATION1, EMPTY},
			new String[]{DatesConstantsDe.d1.s(), punct, space,
					DatesConstantsDe.half.s(), space,
					DatesConstantsDe.article.s(), space});
	/*
	 * Angaben zu Zeitpunkten oder Zeiträumen
	 */
	/**
	 * Angabe eines Zeitraums vor mehreren Datumsangaben.
	 * Bsp.: "zwischen "
	 */
	static DatesSymbol between_ = new DatesSymbol(
			new DatesSymbol.Label[]{LIMIT1, EMPTY},
			new String[]{DatesConstantsDe.between.s(), space});

	/** 
	 * Angabe von Zeitpunkt oder Grenze eines Zeitraums vor einem Datum.
	 * Bsp.: "von " oder "vor " 
	 */
	static DatesSymbol limit_ = new DatesSymbol(
			new DatesSymbol.Label[]{LIMIT1, EMPTY},
			new String[]{limit, space});

	/** 
	 * Angabe von Zeitpunkt oder Grenze eines Zeitraums vor einem Datum. 
	 * Bsp.: "nach dem" 
	 */
	static DatesSymbol limit_art_ = new DatesSymbol(
			new DatesSymbol.Label[]{LIMIT1, LIMIT1, LIMIT1, EMPTY},
			new String[]{limit, space, DatesConstantsDe.article.s(), space});

	/** 
	 * Angabe von Zeitpunkt oder Grenze eines Zeitraums vor einem Datum. 
	 * Bsp.: "ca. " 
	 */
	static DatesSymbol limitp_ = new DatesSymbol(
			new DatesSymbol.Label[]{LIMIT1, LIMIT1, EMPTY},
			new String[]{limit, punct, space});

	/** Angabe einer Grenze vor einem Zeitraum mit 'bis'. Bsp.: "bis " */
	static DatesSymbol to_ = new DatesSymbol(
			new DatesSymbol.Label[]{LIMIT1, EMPTY},
			new String[]{DatesConstantsDe.to.s(), space});

	// VERBINDUNG ZWEIER DATUMSANGABEN

	/** Verbindungselement zwischen zwei Datumsangaben. Bsp.: " und " */
	static DatesSymbol and_ =  new DatesSymbol(
			new DatesSymbol.Label[]{EMPTY, CONNECTION, EMPTY},
			new String[]{space, DatesConstantsDe.and.s(), space});

	/** Verbindungselement zwischen zwei Datumsangaben. Bsp.: "-" */
	static DatesSymbol connect =  new DatesSymbol(
			new DatesSymbol.Label[]{CONNECTION},
			new String[]{DatesConstantsDe.link.s()});

	/** Verbindungselement zwischen zwei Datumsangaben. Bsp.: " - " */
	static DatesSymbol connect_ =  new DatesSymbol(
			new DatesSymbol.Label[]{EMPTY, CONNECTION, EMPTY},
			new String[]{space, DatesConstantsDe.link.s(), space});
	
	/** 
	 * Verbindungselement zwischen zwei Datumsangaben mit 'bis'. 
	 * Bsp.: " bis " 
	 */
	static DatesSymbol connect_to_ = new DatesSymbol(
			new DatesSymbol.Label[]{EMPTY, CONNECTION, EMPTY},
			new String[]{space, DatesConstantsDe.to.s(), space});
	
	// VORANGEHENDE UNVOLLSTAENDIGE DATUMSANGABE

	/** 
	 * Erste Tagesanabe mit Verbindungselement vor einer zweiten Datumsangabe. 
	 * Bsp.: "3.-" 
	 * Kann auch vor einer zweiten Jahrhundertangabe stehen.
	 */
	static DatesSymbol dd_connect = new DatesSymbol(
			new DatesSymbol.Label[]{DAY1, DAY1, CONNECTION},
			new String[]{day, punct, DatesConstantsDe.link.s()});

	/** 
	 * Erste Tagesangabe mit Verbindungselement vor einer zweiten Datumsangabe mit 'bis'. 
	 * Bsp.: "3. bis "
	 * Kann auch vor einer zweiten Jahrhundertangabe stehen. 
	 */
	static DatesSymbol dd_connect_ = new DatesSymbol(
			new DatesSymbol.Label[]{DAY1, DAY1, EMPTY, CONNECTION, EMPTY},
			new String[]{day, punct, space, DatesConstantsDe.to.s(), space});

	/** 
	 * Erste Tages- und Monatsangabe mit Verbindungselement vor einer zweiten Datumsangabe.
	 * Bsp.: "3.10.-"
	 */
	static DatesSymbol dd_mm_connect = new DatesSymbol(
			new DatesSymbol.Label[]{DAY1, DAY1, MONTH1, MONTH1, CONNECTION},
			new String[]{day, punct, month, punct, DatesConstantsDe.link.s()});

	/** 
	 * Erste Tages- und Monatsangabe mit Verbindungselement vor einer zweiten Datumsangabe mit 'bis'.
	 * Bsp.: "3.10. bis " 
	 */
	static DatesSymbol dd_mm_connect_= new DatesSymbol(
			new DatesSymbol.Label[]{DAY1, DAY1, MONTH1, MONTH1, EMPTY,
					CONNECTION, EMPTY},
			new String[]{day, punct, month, punct, space,
					DatesConstantsDe.to.s(), space});
	
	/** 
	 * Erste Tages- und Monatsangabe mit Verbindungselement vor einer zweiten Datumsangabe mit 'bis'.
	 * Bsp.: "3. Oktober bis " 
	 */
	static DatesSymbol dd_month_connect_ = new DatesSymbol(
			new DatesSymbol.Label[]{DAY1, DAY1, EMPTY, MONTH1, EMPTY,
					CONNECTION, EMPTY},
			new String[]{day, punct, space, monthname, space,
					DatesConstantsDe.to.s(), space});
	
	/** 
	 * Erste Tages- und Monatsangabe mit Verbindungselement vor einer zweiten Datumsangabe mit 'bis'.
	 * Bsp.: "3. Okt. bis " 
	 */
	static DatesSymbol dd_monthp_connect_ = new DatesSymbol(
			new DatesSymbol.Label[]{DAY1, DAY1, EMPTY, MONTH1, MONTH1, EMPTY,
					CONNECTION, EMPTY},
			new String[]{day, punct, space, monthname, punct, space,
					DatesConstantsDe.to.s(), space});

	/** 
	 * Erste Monatsangabe mit Verbindungselement vor einer zweiten Datumsangabe mit 'bis'.
	 * Bsp.: "Oktober bis " 
	 */
	static DatesSymbol month_connect_ = new DatesSymbol(
			new DatesSymbol.Label[]{MONTH1, EMPTY, CONNECTION, EMPTY},
			new String[]{monthname, space, DatesConstantsDe.to.s(), space});

	/** 
	 * Erste Monatsangabe mit Verbindungselement vor einer zweiten Datumsangabe mit 'bis'.
	 * Bsp.: "Okt. bis " 
	 */
	static DatesSymbol monthp_connect_= new DatesSymbol(
			new DatesSymbol.Label[]{MONTH1, MONTH1, EMPTY, CONNECTION, EMPTY},
			new String[]{monthname, punct, space, DatesConstantsDe.to.s(),
					space});

	// EINE FOLGENDE UNVOLLSTÄNDIGE DATUMSANGABE 

	/** Eine zweite zweistellige Jahresangabe mit Verbindungselement nach einem Datum. Bsp.: "-45" */
	static DatesSymbol connect_yy = new DatesSymbol(
			new DatesSymbol.Label[]{CONNECTION, YEAR2},
			new String[]{DatesConstantsDe.link.s(), DatesConstantsDe.d2.s()});

}

