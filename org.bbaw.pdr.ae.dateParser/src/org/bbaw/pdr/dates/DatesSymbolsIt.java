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
 * Die Klasse enthält die Konstanten für die italienische
 * Datumserkennung im Kode.
 * Sie enthält die statischen Muster der Kodesymbole zum
 * Erkennen der Datumsangaben im Kode, und die jeweiligen
 * Bezeichnungen für die spätere Formatierung.
 * @author Martin Fechner
 */
class DatesSymbolsIt {

	/*
	 * Es folgen die lokalen Konstanten für die Bezeichner
	 */
	static private final DatesSymbol.Label EMPTY = DatesSymbol.Label.EMPTY;
	static private final DatesSymbol.Label YEAR1 = DatesSymbol.Label.YEAR1;
	static private final DatesSymbol.Label MONTH1 = DatesSymbol.Label.MONTH1;
	static private final DatesSymbol.Label DAY1 = DatesSymbol.Label.DAY1;
	static private final DatesSymbol.Label APPROXIMATION1 = DatesSymbol.Label.APPROXIMATION1;
	static private final DatesSymbol.Label LIMIT1 = DatesSymbol.Label.LIMIT1;
	static private final DatesSymbol.Label CONNECTION = DatesSymbol.Label.CONNECTION;
	static private final DatesSymbol.Label YEAR2 = DatesSymbol.Label.YEAR2;
	/*
	 * Die lokalen Konstanten für die Muster
	 */
	/** 
	 * Lokale Konstante für eine Näherungsangabe vor einem Datum.
	 * Bsp.: "inizio" oder "metà"
	 */
	static private String approximation =
		"(" + DatesConstantsIt.approximation.s() + "|" + DatesConstantsIt.half.s() + ")";

	/** Lokale Konstante für einen Artikel. Bsp.: "al" **/
	static private String article =
		"(" + DatesConstantsIt.article.s() + "|" + DatesConstantsIt.of.s() + ")";

	/**
	 * Lokale Konstante fasst die Jahrhundert 1500 bis 1900 zusammen.
	 * Bsp.: "Cinquecento" oder "Novecento"
	 */
	static private String century16_20 =
		"(" + DatesConstantsIt.century16th.s() + "|" + DatesConstantsIt.century17th.s()
		+ "|" + DatesConstantsIt.century18th.s() + "|" + DatesConstantsIt.century19th.s()
		+ "|" + DatesConstantsIt.century20th.s() + ")";

	/** Lokale Konstante für einen Tag. Bsp.: "2" oder "02" */
	static private String day = 
		"(" + DatesConstantsIt.d1.s() + "|" + DatesConstantsIt.d2.s() + ")";

	/** 
	 * Lokale Konstante fasst die ausgeschriebenen Tagebezeichnungen zusammen.
	 * Bsp.: "primo" oder "ventitre"
	 */
	static private String dayname =
		"(" + DatesConstantsIt.ordinal01.s() + "|" + DatesConstantsIt.cardinal02.s()
		+ "|" + DatesConstantsIt.cardinal03.s() + "|" + DatesConstantsIt.cardinal04.s()
		+ "|" + DatesConstantsIt.cardinal05.s() + "|" + DatesConstantsIt.cardinal06.s()
		+ "|" + DatesConstantsIt.cardinal07.s() + "|" + DatesConstantsIt.cardinal08.s()
		+ "|" + DatesConstantsIt.cardinal09.s() + "|" + DatesConstantsIt.cardinal10.s()
		+ "|" + DatesConstantsIt.cardinal11.s() + "|" + DatesConstantsIt.cardinal12.s()
		+ "|" + DatesConstantsIt.cardinal13.s() + "|" + DatesConstantsIt.cardinal14.s()
		+ "|" + DatesConstantsIt.cardinal15.s() + "|" + DatesConstantsIt.cardinal16.s()
		+ "|" + DatesConstantsIt.cardinal17.s() + "|" + DatesConstantsIt.cardinal18.s()
		+ "|" + DatesConstantsIt.cardinal19.s() + "|" + DatesConstantsIt.cardinal20.s()
		+ "|" + DatesConstantsIt.cardinal21.s() + "|" + DatesConstantsIt.cardinal22.s()
		+ "|" + DatesConstantsIt.cardinal23.s() + "|" + DatesConstantsIt.cardinal24.s()
		+ "|" + DatesConstantsIt.cardinal25.s() + "|" + DatesConstantsIt.cardinal26.s()
		+ "|" + DatesConstantsIt.cardinal27.s() + "|" + DatesConstantsIt.cardinal28.s()
		+ "|" + DatesConstantsIt.cardinal29.s() + "|" + DatesConstantsIt.cardinal30.s()
		+ "|" + DatesConstantsIt.cardinal31.s() + ")";
	
	/**
	 * Lokale Konstante fasst die beweglichen und festen Feiertage zusammen.
	 * Bsp.: "Ostern" oder "Weihnachten"
	 */
	static private String holyday =
		"(" + DatesConstantsIt.newYear.s() + "|" +  DatesConstantsIt.epiphany.s()
		+ "|" +	DatesConstantsIt.ferragosto.s() + "|" + DatesConstantsIt.allHallows.s()
		+ "|" + DatesConstantsIt.christmas.s() + "|" + DatesConstantsIt.carnival.s()
		+ "|" + DatesConstantsIt.easter.s() + "|" + DatesConstantsIt.easterMonday.s()
		+ "|" + DatesConstantsIt.pentecost.s() + "|" + DatesConstantsIt.ascension.s() + ")";

	/** 
	 * Lokale Konstante für einen Zeitpunkt oder die Grenze eines Zeitraums.
	 * Bsp.: "da" oder "in"
	 */
	static private String limit =
		"(" + DatesConstantsIt.from.s() + "|" + DatesConstantsIt.to.s()
		+ "|" + DatesConstantsIt.notBefore.s() + "|" + DatesConstantsIt.notAfterFirst.s()
		+ "|" + DatesConstantsIt.at.s() + "|" + DatesConstantsIt.around.s() + ")";

	/** Lokale Konstante für einen Monat. Bsp.: "6" oder "06" */
	static private String month = 
		"(" + DatesConstantsIt.d1.s() + "|" + DatesConstantsIt.d2.s() + ")";

	/** 
	 * Lokale Konstante fasst die ausgeschriebenen Monatsnamen zusammen.
	 * Bsp.: "gennaio" oder "giugno" oder "giu" 
	 */
	static private String monthname = 
		"(" + DatesConstantsIt.month01.s() + "|" + DatesConstantsIt.month02.s()
		+ "|" + DatesConstantsIt.month03.s() + "|" + DatesConstantsIt.month04.s()
		+ "|" + DatesConstantsIt.month05.s() + "|" + DatesConstantsIt.month06.s()
		+ "|" + DatesConstantsIt.month07.s() + "|" + DatesConstantsIt.month08.s()
		+ "|" + DatesConstantsIt.month09.s() + "|" + DatesConstantsIt.month10.s()
		+ "|" + DatesConstantsIt.month11.s() + "|" + DatesConstantsIt.month12.s() + ")";
	
	/** 
	 * Lokale Konstante für das Erste oder Zweite.
	 * Bsp.: "primo" oder "prima"
	 */
	static private String ordinal01_02 =
		"(" + DatesConstantsIt.notAfterFirst.s() + "|" + DatesConstantsIt.ordinal01.s()
		+ "|" + DatesConstantsIt.ordinal02.s() + ")";

	/**
	 * Lokale Konstante fasst die Ordinalzahlen 6.-9. zusammen.
	 * Bsp.: "nono" oder "sesto"
	 */
	static private String ordinal06_09 =
		"(" + DatesConstantsIt.ordinal06.s() + "|" + DatesConstantsIt.ordinal07.s()
		+ "|" + DatesConstantsIt.ordinal08.s() + "|" + DatesConstantsIt.ordinal09.s() + ")";

	/**
	 * Lokale Konstante fasst die Ordinalzahlen 16.-22. zusammen.
	 * Bsp.: "decimonono" oder "sedicesimo"
	 */
	static private String ordinal16_22 =
		"(" + DatesConstantsIt.ordinal16.s() + "|" + DatesConstantsIt.ordinal17.s()
		+ "|" + DatesConstantsIt.ordinal18.s() + "|" + DatesConstantsIt.ordinal19.s()
		+ "|" + DatesConstantsIt.ordinal20.s() + "|" + DatesConstantsIt.ordinal21.s() + ")";

	/**Lokale Konstante für ein Interpunktionszeichen. Bsp.: "." */
	static private String punct =
		"(" + DatesConstantsIt.punctuation.s() + "|" + DatesConstantsIt.apostrophe.s()+ ")";
	
	/**
	 * Lokale Konstante fasst die Namen der Heiligen zusammen.
	 * Bsp.: "Stefano" oder "Valentino"
	 */
	static private String saint =
		"(" + DatesConstantsIt.stephan.s() + "|" + DatesConstantsIt.silvester.s()
		+ "|" + DatesConstantsIt.valetine.s() + ")";
	
	/** 
	 * Lokale Konstante fasst die Jahreszeiten zusammen.
	 * Bsp.: "primavera" oder "inverno"
	 */
	static private String season =
		"(" + DatesConstantsIt.spring.s() + "|" + DatesConstantsIt.summer.s()
		+ "|" + DatesConstantsIt.autumn.s() + "|" + DatesConstantsIt.winter.s() + ")";
	
	/**Lokale Konstante für ein Leerzeichen. Bsp.: " " */
	static private String space =
		"(" + DatesConstantsIt.space.s() + ")";

	/**
	 * Lokale Konstante fasst die Wochentage zusammen.
	 * Bsp.: "lunedì" oder "sabato"
	 */
	static private String weekday =
		"(" + DatesConstantsIt.monday.s() + "|" + DatesConstantsIt.tuesday.s()
		+ "|" + DatesConstantsIt.wednesday.s() + "|" + DatesConstantsIt.thursday.s()
		+ "|" + DatesConstantsIt.friday.s() + "|" + DatesConstantsIt.saturday.s()
		+ "|" + DatesConstantsIt.sunday.s() + ")";
	
	/** Lokale Konstante für eine vierstellige Jahresangabe. Bsp.: "1946" */
	static private String year = 
		"(" + DatesConstantsIt.d4.s() + ")";

	/*
	 * Es folgen die verschiedenen Datumsmuster.
	 */
	// VOLLSTAENDIGE DATUMSANGABEN
	/*
	 * Einfache Jahresangaben
	 */
	/** Einfache Jahresangabe. Bsp.: "anno 1946" */
	static DatesSymbol year_yyyy = new DatesSymbol(
			new DatesSymbol.Label[]{EMPTY, EMPTY, YEAR1},
			new String[]{DatesConstantsIt.year.s(), space, year});

	/** Einfache Jahresangabe. Bsp.: "1946" */
	static DatesSymbol yyyy = new DatesSymbol(
			new DatesSymbol.Label[]{YEAR1},
			new String[]{year});
	/*
	 * Einfache Jahrhundertangaben
	 */
	/** Einfache Angabe eines Jahrhunderts. Bsp.: "'900" */
	static DatesSymbol aposyyy = new DatesSymbol(
			new DatesSymbol.Label[]{APPROXIMATION1, APPROXIMATION1},
			new String[]{DatesConstantsIt.apostrophe.s(), DatesConstantsIt.d3.s()});

	/** Einfache Angabe eines Jahrhunderts. Bsp.: "secolo decimonono" */
	static DatesSymbol century_ord = new DatesSymbol(
			new DatesSymbol.Label[]{APPROXIMATION1, EMPTY, APPROXIMATION1},
			new String[]{DatesConstantsIt.century.s(), space, ordinal16_22});
	
	/** Einfache Angabe eines Jahrhunderts. Bsp.: "secolo decimo nono" */
	static DatesSymbol century_ord_ord = new DatesSymbol(
			new DatesSymbol.Label[]{APPROXIMATION1, EMPTY, APPROXIMATION1,
					EMPTY, APPROXIMATION1},
			new String[]{DatesConstantsIt.century.s(), space,
					DatesConstantsIt.ordinal10.s(), space, ordinal06_09});

	/** Einfache Angabe eines Jahrhunderts. Bsp.: "secolo XX°" */
	static DatesSymbol century_ordo = new DatesSymbol(
			new DatesSymbol.Label[]{APPROXIMATION1, EMPTY, APPROXIMATION1,
					APPROXIMATION1},
			new String[]{DatesConstantsIt.century.s(), space, ordinal16_22,
					DatesConstantsIt.ordinalIndicator.s()});
	
	/** Einfache Angabe eines Jahrhunderts. Bsp.: "s. XX" */
	static DatesSymbol centuryp_ord = new DatesSymbol(
			new DatesSymbol.Label[]{APPROXIMATION1, APPROXIMATION1, EMPTY,
					APPROXIMATION1},
			new String[]{DatesConstantsIt.century.s(), punct, space,
					ordinal16_22});

	/** Einfache Angabe eines Jahrhunderts. Bsp.: "Novecento" */
	static DatesSymbol ncentury = new DatesSymbol(
			new DatesSymbol.Label[]{APPROXIMATION1},
			new String[]{century16_20});

	/** Einfache Angabe eines Jahrhunderts. Bsp.: "20º secolo" */
	static DatesSymbol no_century = new DatesSymbol(
			new DatesSymbol.Label[]{APPROXIMATION1, APPROXIMATION1, EMPTY,
					APPROXIMATION1},
			new String[]{DatesConstantsIt.d2.s(), DatesConstantsIt.ordinalIndicator.s(),
					space, DatesConstantsIt.century.s()});

	/** Einfache Angabe eines Jahrhunderts. Bsp.: "diciannovesimo secolo" */
	static DatesSymbol ord_century = new DatesSymbol(
			new DatesSymbol.Label[]{APPROXIMATION1, EMPTY, APPROXIMATION1},
			new String[]{ordinal16_22, space, DatesConstantsIt.century.s()});

	/** Einfache Angabe eines Jahrhunderts. Bsp.: "XXº secolo" */
	static DatesSymbol ordo_century = new DatesSymbol(
			new DatesSymbol.Label[]{APPROXIMATION1, APPROXIMATION1, EMPTY,
					APPROXIMATION1},
			new String[]{ordinal16_22, DatesConstantsIt.ordinalIndicator.s(),
					space, DatesConstantsIt.century.s()});
	/*
	 * Datumsangaben mit Jahr und Monat
	 */
	/** Monats- und Jahresangabe. Bsp.: "giugno del 1946" */
	static DatesSymbol month_art_yyyy = new DatesSymbol(
			new DatesSymbol.Label[]{MONTH1, EMPTY, EMPTY, EMPTY, YEAR1},
			new String[]{monthname, space, DatesConstantsIt.of.s(), space,
					year});
		
	/** Monats- und Jahresangabe. Bsp.: "giugno dell'anno 1946" */
	static DatesSymbol month_artpyear_yyyy = new DatesSymbol(
			new DatesSymbol.Label[]{MONTH1, EMPTY, EMPTY, EMPTY, EMPTY,
					EMPTY, YEAR1},
			new String[]{monthname, space, DatesConstantsIt.of.s(), punct,
					DatesConstantsIt.year.s(), space,	year});
		
	/** Monats- und Jahresangabe. Bsp.: "giugno 1946" */
	static DatesSymbol month_yyyy = new DatesSymbol(
			new DatesSymbol.Label[]{MONTH1, EMPTY, YEAR1},
			new String[]{monthname, space, year});

	/** Monats- und Jahresangabe. Bsp.: "giu. 1946" */
	static DatesSymbol monthp_yyyy = new DatesSymbol(
			new DatesSymbol.Label[]{MONTH1, MONTH1, EMPTY, YEAR1},
			new String[]{monthname, punct, space, year});
	/*
	 * Datumsmuster mit Jahr, Monat und Tag
	 */
	/** 
	 * Vollständiges Datum mit Tag, Monat und Jahr.
	 * Bsp.: "due di giugno del 1946"
	 */
	static DatesSymbol day_art_month_art_yyyy = new DatesSymbol(
			new DatesSymbol.Label[]{DAY1, EMPTY, EMPTY, EMPTY, MONTH1, EMPTY,
					EMPTY, EMPTY, YEAR1},
			new String[]{dayname, space, DatesConstantsIt.of.s(), space,
					monthname, space, DatesConstantsIt.of.s(), space, year});

	/** Vollständiges Datum mit Tag, Monat und Jahr. Bsp.: "due di giugno 1946" */
	static DatesSymbol day_art_month_yyyy = new DatesSymbol(
			new DatesSymbol.Label[]{DAY1, EMPTY, EMPTY, EMPTY, MONTH1, EMPTY, YEAR1},
			new String[]{dayname, space, DatesConstantsIt.of.s(), space, monthname,
					space, year});
	
	/** Vollständiges Datum mit Tag, Monat und Jahr. Bsp.: "due di giu. 1946" */
	static DatesSymbol day_art_monthp_yyyy = new DatesSymbol(
			new DatesSymbol.Label[]{DAY1, EMPTY, EMPTY, EMPTY, MONTH1, MONTH1,
					EMPTY, YEAR1},
			new String[]{dayname, space, DatesConstantsIt.of.s(), space,
					monthname, punct, space, year});

	/** Vollständiges Datum mit Tag, Monat und Jahr. Bsp.: "due giugno del 1946" */
	static DatesSymbol day_month_art_yyyy = new DatesSymbol(
			new DatesSymbol.Label[]{DAY1, EMPTY, MONTH1, EMPTY, EMPTY, EMPTY, YEAR1},
			new String[]{ dayname, space, monthname, space, DatesConstantsIt.of.s(),
					space, year});
	
	/** Vollständiges Datum mit Tag, Monat und Jahr. Bsp.: "due giugno 1946" */
	static DatesSymbol day_month_yyyy = new DatesSymbol(
			new DatesSymbol.Label[]{DAY1, EMPTY, MONTH1, EMPTY, YEAR1},
			new String[]{dayname, space, monthname, space, year});
	
	/** Vollständiges Datum mit Tag, Monat und Jahr. Bsp.: "due giu. 1946" */
	static DatesSymbol day_monthp_yyyy = new DatesSymbol(
			new DatesSymbol.Label[]{DAY1, EMPTY, MONTH1, MONTH1, EMPTY, YEAR1},
			new String[]{dayname, space, monthname, punct, space, year});

	/** Vollständiges Datum mit Tag, Monat und Jahr. Bsp.: "2 di giugno del 1946" */
	static DatesSymbol dd_art_month_art_yyyy = new DatesSymbol(
			new DatesSymbol.Label[]{DAY1, EMPTY, EMPTY, EMPTY, MONTH1, EMPTY,
					EMPTY, EMPTY, YEAR1},
			new String[]{day, space, DatesConstantsIt.of.s(), space, monthname,
					space, DatesConstantsIt.of.s(), space, year});
	
	/** Vollständiges Datum mit Tag, Monat und Jahr. Bsp.: "2 di giugno 1946" */
	static DatesSymbol dd_art_month_yyyy = new DatesSymbol(
			new DatesSymbol.Label[]{DAY1, EMPTY, EMPTY, EMPTY, MONTH1, EMPTY,
					YEAR1},
			new String[]{day, space, DatesConstantsIt.of.s(), space, monthname,
					space, year});

	/** Vollständiges Datum mit Tag, Monat und Jahr. Bsp.: "2 di giu. 1946" */
	static DatesSymbol dd_art_monthp_yyyy = new DatesSymbol(
			new DatesSymbol.Label[]{DAY1, EMPTY, EMPTY, EMPTY, MONTH1, MONTH1,
					EMPTY, YEAR1},
			new String[]{day, space, DatesConstantsIt.of.s(), space,
					monthname, punct, space, year});

	/** Vollständiges Datum mit Tag, Monat und Jahr. Bsp.: "2 giugno del 1946" */
	static DatesSymbol dd_month_art_yyyy = new DatesSymbol(
			new DatesSymbol.Label[]{DAY1, EMPTY, MONTH1, EMPTY, EMPTY, EMPTY, YEAR1},
			new String[]{day, space, monthname, space, DatesConstantsIt.of.s(),
					space, year});
	
	/** Vollständiges Datum mit Tag, Monat und Jahr. Bsp.: "2 giugno 1946" */
	static DatesSymbol dd_month_yyyy = new DatesSymbol(
			new DatesSymbol.Label[]{DAY1, EMPTY, MONTH1, EMPTY, YEAR1},
			new String[]{day, space, monthname, space, year});

	/** Vollständiges Datum mit Tag, Monat und Jahr. Bsp.: "2 giu. 1946" */
	static DatesSymbol dd_monthp_yyyy = new DatesSymbol(
			new DatesSymbol.Label[]{DAY1, EMPTY, MONTH1, MONTH1, EMPTY, YEAR1},
			new String[]{day, space, monthname, punct, space, year});

	/** Vollständiges Datum mit Tag, Monat und Jahr. Bsp.: "1° giugno del 1946" */
	static DatesSymbol ddp_month_art_yyyy = new DatesSymbol(
			new DatesSymbol.Label[]{DAY1, DAY1, EMPTY, MONTH1, EMPTY, EMPTY,
					EMPTY, YEAR1},
			new String[]{day, DatesConstantsIt.ordinalIndicator.s(), space,
					monthname, space, DatesConstantsIt.of.s(), space, year});
	
	/** Vollständiges Datum mit Tag, Monat und Jahr. Bsp.: "1° giugno 1946" */
	static DatesSymbol ddp_month_yyyy = new DatesSymbol(
			new DatesSymbol.Label[]{DAY1, DAY1, EMPTY, MONTH1, EMPTY, YEAR1},
			new String[]{day, DatesConstantsIt.ordinalIndicator.s(), space,
					monthname, space, year});
	
	/** Vollständiges Datum mit Tag, Monat und Jahr. Bsp.: "1° giu. 1946" */
	static DatesSymbol ddp_monthp_yyyy = new DatesSymbol(
			new DatesSymbol.Label[]{DAY1, DAY1, EMPTY, MONTH1, MONTH1, EMPTY,
					YEAR1},
			new String[]{day, DatesConstantsIt.ordinalIndicator.s(), space,
					monthname, punct, space, year});

	/** Vollständiges Datum mit Tag, Monat und Jahr. Bsp.: "2.6.1946" */
	static DatesSymbol ddpmmpyyyy = new DatesSymbol(
			new DatesSymbol.Label[]{DAY1, DAY1, MONTH1, MONTH1, YEAR1},
			new String[]{day, punct, month, punct, year});
	
	/** Vollständiges Datum mit Jahr, Tag und Monat. Bsp.: "2.VI.1946" */
	static DatesSymbol ddpmonthpyyyy = new DatesSymbol(
			new DatesSymbol.Label[]{DAY1, DAY1, MONTH1, MONTH1, YEAR1},
			new String[]{day, punct, monthname, punct, year});
	
	/** Vollständiges Datum mit Tag, Monat und Jahr. Bsp.: "02-06-1946" */
	static DatesSymbol ddummuyyyy = new DatesSymbol(
			new DatesSymbol.Label[]{DAY1, EMPTY, MONTH1, EMPTY, YEAR1},
			new String[]{day, DatesConstantsIt.link.s(), month,
					DatesConstantsIt.link.s(), year});

	/** Vollständiges Datum mit Jahr, Tag und Monat. Bsp.: "1946, 2 giu." */
	static DatesSymbol yyyyp_dd_monthp = new DatesSymbol(
			new DatesSymbol.Label[]{YEAR1, YEAR1, EMPTY, DAY1, EMPTY, MONTH1,
					MONTH1},
			new String[]{year, punct, space, day, space, monthname, punct});
	
	/** Eine vollständige Datumsangabe im ISO-Format. Bsp.: "1946-06-02" */
	static DatesSymbol yyyyummudd = new DatesSymbol(
			new DatesSymbol.Label[]{YEAR1, EMPTY, MONTH1, EMPTY, DAY1},
			new String[]{year, DatesConstantsIt.link.s(), DatesConstantsIt.d2.s(),
					DatesConstantsIt.link.s(), DatesConstantsIt.d2.s()});
	/*
	 * Datumsangabe mit Jahreszeiträumen
	 */
	/** Jahreszeitenangabe und Jahresangabe. Bsp.: "estate del 1946" */
	static DatesSymbol season_art_yyyy = new DatesSymbol(
			new DatesSymbol.Label[]{APPROXIMATION1, EMPTY, EMPTY, EMPTY, YEAR1},
			new String[]{season, space, DatesConstantsIt.of.s(), space, year});

	/** Jahreszeitenangabe und Jahresangabe. Bsp.: "estate 1946" */
	static DatesSymbol season_yyyy = new DatesSymbol(
			new DatesSymbol.Label[]{APPROXIMATION1, EMPTY, YEAR1},
			new String[]{season, space, year});
	/*
	 * Bestimmte Feiertage
	 */
	/** Allerheiligen mit Jahresangabe. Bsp.: "Tutti i Santi 1946" */
	static DatesSymbol all_art_hallows_yyyy = new DatesSymbol(
			new DatesSymbol.Label[]{APPROXIMATION1, EMPTY, EMPTY, EMPTY,
					APPROXIMATION1, EMPTY, YEAR1},
			new String[]{DatesConstantsIt.all.s(), space, DatesConstantsIt.article.s(),
					space, DatesConstantsIt.saint.s(), space, year});

	/** Allerseelen mit Jahresangabe. Bsp.: "Giorno dei Morti 1946" */
	static DatesSymbol all_art_souls_yyyy = new DatesSymbol(
			new DatesSymbol.Label[]{APPROXIMATION1, EMPTY, EMPTY, EMPTY,
					APPROXIMATION1, EMPTY, YEAR1},
			new String[]{DatesConstantsIt.day.s(), space, DatesConstantsIt.of.s(),
					space, DatesConstantsIt.allSouls.s(), space, year});
	
	/** Palmsonntag mit Jahresangabe. Bsp.: "Le Palme 1946" */
	static DatesSymbol art_palmsunday_yyyy = new DatesSymbol(
			new DatesSymbol.Label[]{EMPTY, EMPTY, APPROXIMATION1, EMPTY, YEAR1},
			new String[]{DatesConstantsIt.article.s(), space,
					DatesConstantsIt.palmSunday.s(), space, year});
	
	/** Himmelfahrt mit Jahresangabe. Bsp.: "Ascensione di Gesù 1946" */
	static DatesSymbol ascension_art_jesus_yyyy = new DatesSymbol(
			new DatesSymbol.Label[]{APPROXIMATION1, EMPTY, EMPTY, EMPTY,
					APPROXIMATION1, EMPTY, YEAR1},
			new String[]{DatesConstantsIt.ascension.s(), space, DatesConstantsIt.of.s(),
					space, DatesConstantsIt.Jesus.s(), space, year});
	
	/** Mariä Himmelfahrt mit Jahresangabe. Bsp.: "Assuzione di Maria 1946" */
	static DatesSymbol assumption_art_maria_yyyy = new DatesSymbol(
			new DatesSymbol.Label[]{APPROXIMATION1, EMPTY, EMPTY, EMPTY,
					APPROXIMATION1, EMPTY, YEAR1},
			new String[]{DatesConstantsIt.assumption.s(), space, DatesConstantsIt.of.s(),
					space, DatesConstantsIt.maria.s(), space, year});
	
	/** Fronleichnam mit Jahresangabe. Bsp.: "Corpus Domini 1946" */
	static DatesSymbol corpus_christi_yyyy = new DatesSymbol(
			new DatesSymbol.Label[]{APPROXIMATION1, EMPTY, APPROXIMATION1,
					EMPTY, YEAR1},
			new String[]{DatesConstantsIt.corpusChristi.s(), space,
					DatesConstantsIt.corpusChristi.s(), space, year});
	
	/** Neujahr mit Jahresangabe. Bsp.: "Primo dell'Anno 1946" */
	static DatesSymbol day_artpyear_yyyy = new DatesSymbol(
			new DatesSymbol.Label[]{APPROXIMATION1, EMPTY, EMPTY, EMPTY,
					APPROXIMATION1, EMPTY, YEAR1},
			new String[]{DatesConstantsIt.ordinal01.s(), space,
					DatesConstantsIt.of.s(), punct, DatesConstantsIt.year.s(),
					space, year});

	/** Feiertagsangabe mit Jahresangabe. Bsp.: "Ostern 1946" */
	static DatesSymbol holyday_yyyy = new DatesSymbol(
			new DatesSymbol.Label[]{APPROXIMATION1, EMPTY, YEAR1},
			new String[]{holyday, space, year});
	
	/** Mariä Empfängnis mit Jahresangabe. Bsp.: "Immacolata Concenzione 1946" */
	static DatesSymbol immaculate_conception_yyyy = new DatesSymbol(
			new DatesSymbol.Label[]{APPROXIMATION1, EMPTY, APPROXIMATION1,
					EMPTY, YEAR1},
			new String[]{DatesConstantsIt.immaculateConception.s(), space,
					DatesConstantsIt.immaculateConception.s(), space, year});
	
	/** Gedenktage der Heiligen. Bsp.: "Santo Stefano 1946" */
	static DatesSymbol st_name_yyyy = new DatesSymbol(
			new DatesSymbol.Label[]{APPROXIMATION1, EMPTY, APPROXIMATION1,
					EMPTY, YEAR1},
			new String[]{DatesConstantsIt.saint.s(), space, saint, space, year});

	/** Palmsonntag mit Jahresangabe. Bsp.: "Domenica delle Palme 1946" */
	static DatesSymbol weekday_art_palmsunday_yyyy = new DatesSymbol(
			new DatesSymbol.Label[]{APPROXIMATION1, EMPTY, EMPTY, EMPTY,
					APPROXIMATION1, EMPTY, YEAR1},
			new String[]{weekday, space, DatesConstantsIt.of.s(), space,
					DatesConstantsIt.palmSunday.s(), space, year});

	/** Pfingstmontag mit Jahresangabe. Bsp.: "Lunedì  di Pentecoste 1946" */
	static DatesSymbol weekday_art_pentecost_yyyy = new DatesSymbol(
			new DatesSymbol.Label[]{APPROXIMATION1, EMPTY, EMPTY, EMPTY,
					APPROXIMATION1, EMPTY, YEAR1},
			new String[]{weekday, space, DatesConstantsIt.of.s(), space,
					DatesConstantsIt.pentecost.s(), space, year});
	
	/** Ostermontag mit Jahresangabe. Bsp.: "Lunedì dell'Angelo 1946" */
	static DatesSymbol weekday_artpeaster_yyyy = new DatesSymbol(
			new DatesSymbol.Label[]{APPROXIMATION1, EMPTY, EMPTY, EMPTY,
					APPROXIMATION1, EMPTY, YEAR1},
			new String[]{weekday, space, DatesConstantsIt.of.s(), punct,
					DatesConstantsIt.angel.s(), space, year});

	/** Karnevalstage mit Jahresangabe. Bsp.: "Martedì grasso 1946" */
	static DatesSymbol weekday_carnival_yyyy = new DatesSymbol(
			new DatesSymbol.Label[]{APPROXIMATION1, EMPTY, APPROXIMATION1,
					EMPTY, YEAR1},
			new String[]{weekday, space, DatesConstantsIt.carnivalWeek.s(),
					space, year});
	
	/** Tage vor Ostern. Bsp.: "Venerdì Santo 1946" */
	static DatesSymbol weekday_st_yyyy = new DatesSymbol(
			new DatesSymbol.Label[]{APPROXIMATION1, EMPTY, APPROXIMATION1,
					EMPTY, YEAR1},
			new String[]{weekday, space, DatesConstantsIt.saint.s(), space,
					year});
	// VORSAETZE VOR DATUMSANGABEN
	/*
	 * Artikel
	 */
	/** Artikel vor einem Datum. Bsp.: "L" oder "l" */
	static DatesSymbol art = new DatesSymbol(
			new DatesSymbol.Label[]{EMPTY},
			new String[]{article});

	/** Artikel vor einem Datum. Bsp.: "Il " oder "il " */
	static DatesSymbol art_ = new DatesSymbol(
			new DatesSymbol.Label[]{EMPTY, EMPTY},
			new String[]{article, space});

	/** Artikel vor einem Datum. Bsp.: "L'" oder "l'" */
	static DatesSymbol artp = new DatesSymbol(
			new DatesSymbol.Label[]{EMPTY, EMPTY},
			new String[]{article, punct});
	
	/** Artikel vor einem Datum. Bsp.: "All" */
	static DatesSymbol at = new DatesSymbol(
			new DatesSymbol.Label[]{EMPTY},
			new String[]{DatesConstantsIt.atNext.s()});
	
	/** Artikel vor einem Datum. Bsp.: "Al" */
	static DatesSymbol at_ = new DatesSymbol(
			new DatesSymbol.Label[]{EMPTY, EMPTY},
			new String[]{DatesConstantsIt.atNext.s(), space});
	
	/** Artikel vor einem Datum. Bsp.: "All'" */
	static DatesSymbol atp = new DatesSymbol(
			new DatesSymbol.Label[]{EMPTY, EMPTY},
			new String[]{DatesConstantsIt.atNext.s(), punct});
	/*
	 * Näherungsangaben
	 */
	/** Näherungsangabe vor einem Datum. Bsp.: "inizio " oder "fine " */
	static DatesSymbol approx_ = new DatesSymbol(
			new DatesSymbol.Label[]{APPROXIMATION1, EMPTY},
			new String[]{approximation, space});

	/** Näherungsangabe vor einem Datum. Bsp.: "inizio dell" oder "fine dell" */
	static DatesSymbol approx_art = new DatesSymbol(
			new DatesSymbol.Label[]{APPROXIMATION1, EMPTY, EMPTY},
			new String[]{approximation, space, article});

	/** Näherungsangabe vor einem Datum. Bsp.: "inizio del " oder "fine del " */
	static DatesSymbol approx_art_ = new DatesSymbol(
			new DatesSymbol.Label[]{APPROXIMATION1, EMPTY, EMPTY, EMPTY},
			new String[]{approximation, space, article, space});

	/** Näherungsangabe vor einem Datum. Bsp.: "inizio dell'" oder "fine dell'" */
	static DatesSymbol approx_artp = new DatesSymbol(
			new DatesSymbol.Label[]{APPROXIMATION1, EMPTY, EMPTY, EMPTY},
			new String[]{approximation, space, article, punct});

	/** Näherungsangabe vor einem Datum. Bsp.: "prima metà " */
	static DatesSymbol ord_half_ = new DatesSymbol(
			new DatesSymbol.Label[]{APPROXIMATION1, EMPTY, APPROXIMATION1, EMPTY},
			new String[]{ordinal01_02, space, DatesConstantsIt.half.s(), space});

	/** Näherungsangabe vor einem Datum. Bsp.: "prima metà dell" */
	static DatesSymbol ord_half_art = new DatesSymbol(
			new DatesSymbol.Label[]{APPROXIMATION1, EMPTY, APPROXIMATION1,
					EMPTY, EMPTY},
			new String[]{ordinal01_02, space, DatesConstantsIt.half.s(),
					space, DatesConstantsIt.of.s()});

	/** Näherungsangabe vor einem Datum. Bsp.: "prima metà del " */
	static DatesSymbol ord_half_art_ = new DatesSymbol(
			new DatesSymbol.Label[]{APPROXIMATION1, EMPTY, APPROXIMATION1, EMPTY,
					EMPTY, EMPTY},
			new String[]{ordinal01_02, space, DatesConstantsIt.half.s(), space,
					DatesConstantsIt.of.s(), space});

	/** Näherungsangabe vor einem Datum. Bsp.: "prima metà dell'" */
	static DatesSymbol ord_half_artp = new DatesSymbol(
			new DatesSymbol.Label[]{APPROXIMATION1, EMPTY, APPROXIMATION1, EMPTY,
					EMPTY, EMPTY},
			new String[]{ordinal01_02, space, DatesConstantsIt.half.s(), space,
					DatesConstantsIt.of.s(), punct});
	/*
	 * Angaben zu Zeitpunkten oder Zeiträumen
	 */
	/**
	 * Angabe eines Zeitraums vor mehreren Datumsangaben.
	 * Bsp.: "tra "
	 */
	static DatesSymbol between_ = new DatesSymbol(
			new DatesSymbol.Label[]{LIMIT1, EMPTY},
			new String[]{DatesConstantsIt.Between.s(), space});
	
	/**
	 * Angabe von Zeitpunkt oder Grenze eines Zeitraums vor einem Datum.
	 * Bsp.: "nell" oder "dall"
	 */
	static DatesSymbol lim = new DatesSymbol(
			new DatesSymbol.Label[]{LIMIT1},
			new String[]{limit});
	
	/** 
	 * Angabe von Zeitpunkt oder Grenze eines Zeitraums vor einem Datum.
	 * Bsp.: "nel " oder "dal "
	 */
	static DatesSymbol limit_ = new DatesSymbol(
			new DatesSymbol.Label[]{LIMIT1, EMPTY},
			new String[]{limit, space});
	
	/**
	 * Angabe von Zeitpunkt oder Grenze eines Zeitraums vor einem Datum.
	 * Bsp.: "fino al"
	 */
	static DatesSymbol limit_at = new DatesSymbol(
			new DatesSymbol.Label[]{LIMIT1, EMPTY, EMPTY},
			new String[]{limit, space, DatesConstantsIt.atNext.s()});
	
	/**
	 * Angabe von Zeitpunkt oder Grenze eines Zeitraums vor einem Datum.
	 * Bsp.: "fino al "
	 */
	static DatesSymbol limit_at_ = new DatesSymbol(
			new DatesSymbol.Label[]{LIMIT1, EMPTY, EMPTY, EMPTY},
			new String[]{limit, space, DatesConstantsIt.atNext.s(), space});

	/**
	 * Angabe von Zeitpunkt oder Grenze eines Zeitraums vor einem Datum.
	 * Bsp.: "fino all'"
	 */
	static DatesSymbol limit_atp = new DatesSymbol(
			new DatesSymbol.Label[]{LIMIT1, EMPTY, EMPTY, EMPTY},
			new String[]{limit, space, DatesConstantsIt.atNext.s(), punct});

	/**
	 * Angabe von Zeitpunkt oder Grenze eines Zeitraums vor einem Datum.
	 * Bsp.: "prima dell"
	 */
	static DatesSymbol limit_art = new DatesSymbol(
			new DatesSymbol.Label[]{LIMIT1, EMPTY, EMPTY},
			new String[]{limit, space, article});
	
	/**
	 * Angabe von Zeitpunkt oder Grenze eines Zeitraums vor einem Datum.
	 * Bsp.: "prima di "
	 */
	static DatesSymbol limit_art_ = new DatesSymbol(
			new DatesSymbol.Label[]{LIMIT1, EMPTY, EMPTY, EMPTY},
			new String[]{limit, space, article, space});

	/**
	 * Angabe von Zeitpunkt oder Grenze eines Zeitraums vor einem Datum.
	 * Bsp.: "prima dell'"
	 */
	static DatesSymbol limit_artp = new DatesSymbol(
			new DatesSymbol.Label[]{LIMIT1, EMPTY, EMPTY, EMPTY},
			new String[]{limit, space, article, punct});

	/**
	 * Angabe von Zeitpunkt oder Grenze eines Zeitraums vor einem Datum.
	 * Bsp.: "nell'" oder "dall'
	 */
	static DatesSymbol limitp = new DatesSymbol(
			new DatesSymbol.Label[]{LIMIT1, EMPTY},
			new String[]{limit, punct});

	/** 
	 * Angabe von Zeitpunkt oder Grenze eines Zeitraums vor einem Datum. 
	 * Bsp.: "ca. " 
	 */
	static DatesSymbol limitp_ = new DatesSymbol(
			new DatesSymbol.Label[]{LIMIT1, LIMIT1, EMPTY},
			new String[]{limit, punct, space});

	// VERBINDUNG ZWEIER DATUMSANGABEN

	/** Verbindungselement zwischen zwei Datumsangaben. Bsp.: " e " */
	static DatesSymbol and_ =  new DatesSymbol(
			new DatesSymbol.Label[]{EMPTY, CONNECTION, EMPTY},
			new String[]{space, DatesConstantsIt.and.s(), space});

	/** Verbindungselement zwischen zwei Datumsangaben. Bsp.: "-" */
	static DatesSymbol connect =  new DatesSymbol(
			new DatesSymbol.Label[]{CONNECTION},
			new String[]{DatesConstantsIt.link.s()});

	/** Verbindungselement zwischen zwei Datumsangaben. Bsp.: " - " */
	static DatesSymbol connect_ =  new DatesSymbol(
			new DatesSymbol.Label[]{EMPTY, CONNECTION, EMPTY},
			new String[]{space, DatesConstantsIt.link.s(), space});
	
	/** Verbindungselement zwischen zwei Datumsangaben mit 'a'. Bsp.: " all" */
	static DatesSymbol connect_to = new DatesSymbol(
			new DatesSymbol.Label[]{EMPTY, CONNECTION},
			new String[]{space, DatesConstantsIt.atNext.s()});
	/** Verbindungselement zwischen zwei Datumsangaben mit 'a'. Bsp.: " a " */
	static DatesSymbol connect_to_ = new DatesSymbol(
			new DatesSymbol.Label[]{EMPTY, CONNECTION, EMPTY},
			new String[]{space, DatesConstantsIt.atNext.s(), space});
	
	/** Verbindungselement zwischen zwei Datumsangaben mit 'a'. Bsp.: " all'" */
	static DatesSymbol connect_top = new DatesSymbol(
			new DatesSymbol.Label[]{EMPTY, CONNECTION, EMPTY},
			new String[]{space, DatesConstantsIt.atNext.s(), punct});

	// EINE FOLGENDE UNVOLLSTÄNDIGE DATUMSANGABE 

	/** Eine zweite einstellige Jahresangabe mit Verbindungselement nach einem Datum. Bsp.: "-6" */
	static DatesSymbol connect_y = new DatesSymbol(
			new DatesSymbol.Label[]{CONNECTION, YEAR2},
			new String[]{DatesConstantsIt.link.s(), DatesConstantsIt.d1.s()
	});
	/** Eine zweite zweistellige Jahresangabe mit Verbindungselement nach einem Datum. Bsp.: "-46" */
	static DatesSymbol connect_yy = new DatesSymbol(
			new DatesSymbol.Label[]{CONNECTION, YEAR2},
			new String[]{DatesConstantsIt.link.s(), DatesConstantsIt.d2.s()});

}

