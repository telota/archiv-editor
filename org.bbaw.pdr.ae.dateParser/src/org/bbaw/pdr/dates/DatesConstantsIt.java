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
 * Die Klasse enthält die statischen Objekte zur Kodierung eines italienischen Originaltextes.
 * @author Martin Fechner
 */
class DatesConstantsIt {
	// alle allgemein möglichen Elemente (G)
	/** Ein beliebiges Wort. */
	static DatesConstant word = new DatesConstant(
			"sGwo", "(\\p{L}+)");
	
	/** Eine beliebiges Dezimalzahl. */
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
	static DatesConstant Other = new DatesConstant(
			"sGot", "(.*[^\\d\\p{L}\\s\\p{P}].*)");

	// verschiedenstellige Dezimalzahlen (D)
	/** Eine einstellige Dezimalzahl. */
	static DatesConstant d1 = new DatesConstant(
			"sD1_", "(\\d)");
	
	/** Eine zweistellige Dezimalzahl. */
	static DatesConstant d2 = new DatesConstant(
			"sD2_", "(\\d\\d)");
	
	/** Eine dreitstellige Dezimalzahl. */
	static DatesConstant d3 = new DatesConstant(
			"sD3_", "(\\d\\d\\d)");
	
	/** Eine vierstellige Dezimalzahl. */
	static DatesConstant d4 = new DatesConstant(
			"sD4_", "(\\d\\d\\d\\d)");
	
	// augeschriebene Ordinalzahlen (O)
	/** Der oder das Erste. S. auch 'notAfterFirst'. */
	static DatesConstant ordinal01 = new DatesConstant(
			"sO01", "(Primo|primo|Primi|primi)");
	
	/** Der oder das Zweite. */
	static DatesConstant ordinal02 = new DatesConstant(
			"sO02", "(Seconda|seconda|2a)");
	
	/** Der oder das Sechste. */
	static DatesConstant ordinal06 = new DatesConstant(
			"sO06", "(Sesto|sesto)");
	
	/** Der oder das Siebte. */
	static DatesConstant ordinal07 = new DatesConstant(
			"sO07", "(Settimo|settimo)");
	
	/** Der oder das Achte. */
	static DatesConstant ordinal08 = new DatesConstant(
			"sO08", "(Ottavo|ottavo)");
	
	/** Der oder das Neunte. */
	static DatesConstant ordinal09 = new DatesConstant(
			"sO09", "(Nono|nono)");
	
	/** Der oder das Zehnte. */
	static DatesConstant ordinal10 = new DatesConstant(
			"sO10", "(Decimo|decimo)");
	
	/** Der oder das Sechzehnte. */
	static DatesConstant ordinal16 = new DatesConstant(
			"sO16", "(Decimosesto|decimosesto|Sedicesimo|sedicesimo|XVI)");
	
	/** Der oder das Siebzehnte. */
	static DatesConstant ordinal17 = new DatesConstant(
			"sO17", "(Decimosettimo|decimosettimo|Diciasettesimo|diciasettesimo|XVII)");
	
	/** Der oder das Achtzehnte. */
	static DatesConstant ordinal18 = new DatesConstant(
			"sO18", "(Decimottavo|decimottavo|Diciottesimo|diciottesimo|XVIII)");
	
	/** Der oder das Neunzehnte. */
	static DatesConstant ordinal19 = new DatesConstant(
			"sO19", "(Decimonono|decimonono|Dicianovesimo|dicianovesimo|XIX)");
	
	/** Der oder das Zwanzigste. */
	static DatesConstant ordinal20 = new DatesConstant(
			"sO20", "(Ventesimo|ventesimo|XX)");
	
	/** Der oder das Einundzwanzigste. */
	static DatesConstant ordinal21 = new DatesConstant(
			"sO21", "(Ventesimoprimo|ventesimoprimo|Ventunesimo|ventunesimo|XXI)");
		
	// augeschriebene Kardinalzahlen (K)
	/** Der Zweite des Monats. */
	static DatesConstant cardinal02 = new DatesConstant(
			"sK02", "(Due|due)");
	
	/** Der Dritte des Monats. */
	static DatesConstant cardinal03 = new DatesConstant(
			"sK03", "(Tre|tre)");
	
	/** Der Vierte des Monats. */
	static DatesConstant cardinal04 = new DatesConstant(
			"sK04", "(Quattro|quattro)");
	
	/** Der Fünfte des Monats. */
	static DatesConstant cardinal05 = new DatesConstant(
			"sK05","(Cinque|cinque)");
	
	/** Der Sechste des Monats. */
	static DatesConstant cardinal06 = new DatesConstant(
			"sK06", "(Sei|sei)");
	
	/** Der Siebte des Monats. */
	static DatesConstant cardinal07 = new DatesConstant(
			"sK07", "(Sette|sette)");
	
	/** Der Achte des Monats. */
	static DatesConstant cardinal08 = new DatesConstant(
			"sK08", "(Otto|otto)");
	
	/** Der Neunte des Monats. */
	static DatesConstant cardinal09 = new DatesConstant(
			"sK09", "(Nove|nove)");
	
	/** Der Zehnte des Monats. */
	static DatesConstant cardinal10 = new DatesConstant(
			"sK10", "(Dieci|dieci)");
	
	/** Der Elfte des Monats. */
	static DatesConstant cardinal11 = new DatesConstant(
			"sK11", "(Undici|undici)");
	
	/** Der Zwölfte des Monats. */
	static DatesConstant cardinal12 = new DatesConstant(
			"sK12", "(Dodici|dodici)");
	
	/** Der Dreizehnte des Monats. */
	static DatesConstant cardinal13 = new DatesConstant(
			"sK13", "(Tredici|tredici)");
	
	/** Der Vierzehnte des Monats. */
	static DatesConstant cardinal14 = new DatesConstant(
			"sK14", "(Quattordici|quattordici)");
	
	/** Der Fünfzehnte des Monats. */
	static DatesConstant cardinal15 = new DatesConstant(
			"sK15", "(Quindici|quindici)");
	
	/** Der Sechzehnte des Monats. */
	static DatesConstant cardinal16 = new DatesConstant(
			"sK16", "(Sedici|sedici)");
	
	/** Der Siebzehnte des Monats. */
	static DatesConstant cardinal17 = new DatesConstant(
			"sK17", "(Diciasette|diciasette)");
	
	/** Der Achtzehnte des Monats. */
	static DatesConstant cardinal18 = new DatesConstant(
			"sK18", "(Diciotto|diciotto)");
	
	/** Der Neunzehnte des Monats. */
	static DatesConstant cardinal19 = new DatesConstant(
			"sK19", "(Diciannove|diciannove)");
	
	/** Der Zwanzigste des Monats. */
	static DatesConstant cardinal20 = new DatesConstant(
			"sK20", "(Venti|venti)");
	
	/** Der Einundzwanzigste des Monats. */
	static DatesConstant cardinal21 = new DatesConstant(
			"sK21", "(Ventuno|ventuno)");
	
	/** Der Zweiundzwanzigste des Monats. */
	static DatesConstant cardinal22 = new DatesConstant(
			"sK22", "(Ventidue|ventidue)");
	
	/** Der Dreiundzwanzigste des Monats. */
	static DatesConstant cardinal23 = new DatesConstant(
			"sK23", "(Ventitre|ventitre)");
	
	/** Der Vierundzwanzigste des Monats. */
	static DatesConstant cardinal24 = new DatesConstant(
			"sK24", "(Ventiquattro|ventiquattro)");
	
	/** Der Fünfundzwanzigste des Monats. */
	static DatesConstant cardinal25 = new DatesConstant(
			"sK25", "(Venticinque|venticinque)");
	
	/** Der Sechsundzwanzigste des Monats. */
	static DatesConstant cardinal26 = new DatesConstant(
			"sK26", "(Ventisei|ventisei)");
	
	/** Der Siebenundzwanzigste des Monats. */
	static DatesConstant cardinal27 = new DatesConstant(
			"sK27", "(Ventisette|ventisette)");
	
	/** Der Achtundzwanzigste des Monats. */
	static DatesConstant cardinal28 = new DatesConstant(
			"sK28", "(Ventotto|ventotto)");
	
	/** Der Neunundzwanzigste des Monats. */
	static DatesConstant cardinal29 = new DatesConstant(
			"sK29", "(Ventinove|ventinove)");
	
	/** Der Dreißigste des Monats. */
	static DatesConstant cardinal30 = new DatesConstant(
			"sK30", "(Trenta|trenta)");
	
	/** Der Einunddreißigste des Monats. */
	static DatesConstant cardinal31 = new DatesConstant(
			"sK31", "(Trentuno|trentuno)");

	// feste Feiertage (H)
	/** Neujahr : 01.01. */
	static DatesConstant newYear = new DatesConstant(
			"sHny", "(Capodanno)");
	
	/** Heilige Drei Könige: 06.01. */
	static DatesConstant epiphany = new DatesConstant(
			"sHep", "Epifania|Befana");
	
	/** Mariä Himmelfahrt: 15.08. */
	static DatesConstant ferragosto = new DatesConstant(
			"sHfe", "(Ferragosto)");
	
	/** Mariä Himmelfahrt: 15.08. */
	static DatesConstant assumption = new DatesConstant(
			"sHas", "(Assunzione)");
	
	/** Allerheiligen: 01.11. */
	static DatesConstant allHallows = new DatesConstant(
			"sHah", "(Ognissanti)");
	
	/** Allerseelen: 02.11. */
	static DatesConstant allSouls = new DatesConstant(
			"sHas", "(Morti)");
	
	/** Mariä Empfängnis: 08.12. */
	static DatesConstant immaculateConception = new DatesConstant(
			"sHic", "(Immacolata|Concezione)");
	
	/** Weihnachten: 25.12. */
	static DatesConstant christmas = new DatesConstant(
			"sHxm", "(Natale)");
	
	// bewegliche Feiertage um Ostern (E)
	/** Karnevalstage: 52-46 Tage vor Ostern */
	static DatesConstant carnivalWeek = new DatesConstant(
			"sEcw", "(Grasso|grasso)");
	
	/** Karnevalssonntag: 49 Tage vor Ostern */
	static DatesConstant carnival = new DatesConstant(
			"sEca", "(Carnevale)");
	
	/** Palmsonntag: 7 Tage vor Ostern */
	static DatesConstant palmSunday = new DatesConstant(
			"sEps", "(Palme)");
	
	/** Ostern */
	static DatesConstant easter = new DatesConstant(
			"sE00", "(Pasqua)");
	
	/** Ostermontag: 1 Tag nach Ostern */
	static DatesConstant easterMonday = new DatesConstant(
			"sE01", "(Pasquetta)");
	
	/** Christi Himmelfahrt: 39 Tage nach Ostern */
	static DatesConstant ascension = new DatesConstant(
			"sE39", "(Ascensione)");
	
	/** Pfingsten: 49 Tage nach Ostern */
	static DatesConstant pentecost = new DatesConstant(
			"sE49", "(Pentecoste)");
	
	/** Fronleichnam: 60 Tage nach Ostern */
	static DatesConstant corpusChristi = new DatesConstant(
			"sE60", "(Corpus|Domini)");

	// die Wochentage (W)
	/** Montag */
	static DatesConstant monday = new DatesConstant(
			"sWmo", "(Lunedì|lunedì)");
	
	/** Dienstag */
	static DatesConstant tuesday = new DatesConstant(
			"sWtu", "(Martedì|martedì)");
	
	/** Dienstag */
	static DatesConstant wednesday = new DatesConstant(
			"sWwe", "(Mercoledì|mercoledì)");
	
	/** Donnerstag */
	static DatesConstant thursday = new DatesConstant(
			"sWth", "(Giovedì|giovedì)");
	
	/** Dienstag */
	static DatesConstant friday = new DatesConstant(
			"sWfr", "(Venerdì|venerdì)");
	
	/** Dienstag */
	static DatesConstant saturday = new DatesConstant(
			"sWsa", "(Sabato|sabato)");
	
	/** Dienstag */
	static DatesConstant sunday = new DatesConstant(
			"sWsu", "(Domenica|domenica)");
	
	// die verschiedenen Monate (M)
	/** Eine Monatsangabe für Januar. */
	static DatesConstant month01 = new DatesConstant(
			"sM01", "(Gennaio|gennaio|Gen|gen|I)");
	
	/** Eine Monatsangabe für Februar. */
	static DatesConstant month02 = new DatesConstant(
			"sM02", "(Febbraio|febbraio|Feb|feb|II)");
	
	/** Eine Monatsangabe für März. */
	static DatesConstant month03 = new DatesConstant(
			"sM03", "(Marzo|marzo|Mar|mar|III)");
	
	/** Eine Monatsangabe für April. */
	static DatesConstant month04 = new DatesConstant(
			"sM04", "(Aprile|aprile|Apr|apr|IV)");
	
	/** Eine Monatsangabe für Mai. */
	static DatesConstant month05 = new DatesConstant(
			"sM05", "(Maggio|maggio|Mag|mag|V)");
	
	/** Eine Monatsangabe für Juni. */
	static DatesConstant month06 = new DatesConstant(
			"sM06", "(Giugno|giugno|Giu|giu|VI)");
	
	/** Eine Monatsangabe für Juli. */
	static DatesConstant month07 = new DatesConstant(
			"sM07", "(Luglio|luglio|Lug|lug|VII)");
	
	/** Eine Monatsangabe für August. */
	static DatesConstant month08 = new DatesConstant(
			"sM08", "(Agosto|agosto|Ago|ago|VIII)");
	
	/** Eine Monatsangabe für September. */
	static DatesConstant month09 = new DatesConstant(
			"sM09", "(Settembre|settembre|Set|set|IX)");
	
	/** Eine Monatsangabe für Oktober. */
	static DatesConstant month10 = new DatesConstant(
			"sM10", "(Ottobre|ottobre|Ott|ott|X)");
	
	/** Eine Monatsangabe für November. */
	static DatesConstant month11 = new DatesConstant(
			"sM11", "(Novembre|novembre|Nov|nov|XI)");
	
	/** Eine Monatsangabe für Dezember. */
	static DatesConstant month12 = new DatesConstant(
			"sM12", "(Dicembre|dicembre|Dic|dic|XII)");
	
	// Jahreszeiten (S)
	/** Eine Angabe für Frühling. */
	static DatesConstant spring = new DatesConstant(
			"sSsp", "(Primavera|primavera)");
	
	/** Eine Angabe für den Sommer. */
	static DatesConstant summer = new DatesConstant(
			"sSsu", "(Estate|estate)");
	
	/** Eine Angabe für den Herbst. */
	static DatesConstant autumn = new DatesConstant(
			"sSau", "(Autunno|autunno)");
	
	/** Eine Angabe für den Winter. */
	static DatesConstant winter = new DatesConstant(
			"sSwi", "(Inverno|inverno)");
	
	// Jahrhundert (C)
	/** Eine Jahrhundertangabe. */
	static DatesConstant century = new DatesConstant(
			"sCce", "(Secolo|secolo|s)");
	
	/** Eine Jahrhundertangabe. */
	static DatesConstant century16th = new DatesConstant(
			"sC16", "(Cinquecento)");
	
	/** Eine Jahrhundertangabe. */
	static DatesConstant century17th = new DatesConstant(
			"sC17", "(Seicento)");
	
	/** Eine Jahrhundertangabe. */
	static DatesConstant century18th = new DatesConstant(
			"sC18", "(Settecento)");
	
	/** Eine Jahrhundertangabe. */
	static DatesConstant century19th = new DatesConstant(
			"sC19", "(Ottocento)");
	
	/** Eine Jahrhundertangabe. */
	static DatesConstant century20th = new DatesConstant(
			"sC20", "(Novecento)");

	// mögliche Angaben zu Zeitpunkten und Zeiträumen (A)
	/** Ein Zeitpunkt. */
	static DatesConstant at = new DatesConstant(
			"sAat", "(In|in|Nel|nel|Nell|nell|Nella|nella|Negli|negli)");
	
	/** Ein Zeitpunkt, sowie als Verbindung zwischen zwei Angaben. */
	static DatesConstant atNext = new DatesConstant(
			"sAan", "(A|a|Al|al|All|all|Alla|alla)");
	
	/** Ein ungefährer Zeitpunkt. */
	static DatesConstant around = new DatesConstant(
			"sAar", "(Circa|circa|Ca|ca)");
	
	/** Eine Näherungsangabe eines Zeitpunkts. */
	static DatesConstant approximation = new DatesConstant(
			"sAap", "(Inizio|inizio|Fine|fine)");

	/** Eine Näherungsangabe eines Zeitpunkts. */
	static DatesConstant half = new DatesConstant(
			"sAha", "(metà)");

	/** Die vordere Grenze eines Zeitraums. */
	static DatesConstant from = new DatesConstant(
			"sAfr", "(Da|da|Dal|dal|Dall|dall|Dalla|dalla)");
	
	/** Die vordere Grenze eines Zeitraums. */
	static DatesConstant notBefore = new DatesConstant(
			"sAnb", "(Dopo|dopo)");
	
	/** Die hintere Grenze eines Zeitraums. */
	static DatesConstant to = new DatesConstant(
			"sAto", "(Fino|fino)");
	
	/** Die hintere Grenze eines Zeitraums und das Erste. */
	static DatesConstant notAfterFirst = new DatesConstant(
			"sAna", "(Prima|prima)");
	
	/** Der Hinweis auf einen Zeitraum. */
	static DatesConstant Between = new DatesConstant(
			"sAbe", "(Tra|tra)");

	// zur Verbindung zweier Datumselemente (L)
	/** Eine Verbindungselement. */
	static DatesConstant link = new DatesConstant(
			"sLli", "(-|/|-|-|–)");
	
	/** Eine Verbindungselement. */
	static DatesConstant and = new DatesConstant(
			"sLan", "(e|ed)");

	// besondere Namen (N)
	/** Jesus */
	static DatesConstant Jesus = new DatesConstant(
			"sNJe", "(Gesù)");
	
	/** Maria. */
	static DatesConstant maria = new DatesConstant(
			"sNma", "(Maria)");
	
	/** St. Silvester: 31.12. */
	static DatesConstant silvester = new DatesConstant(
			"sNsi", "(Silvestro)");
	
	/** St. Stephan: 26.12. */
	static DatesConstant stephan = new DatesConstant(
			"sNst", "(Stefano)");
	
	/** St. Valentin: 14.02. */
	static DatesConstant valetine = new DatesConstant(
			"sNva", "(Valentino)");
	
	// andere spezielle Worte (V)
	/** Ein Artikel, der im Zusammenhang mit Zeiträumen benutzt wird. */
	static DatesConstant article = new DatesConstant(
			"sVar",	"(Il|il|L|l|I|i|La|la|Le|le)");
	
	/** Ein Artikel, der im Zusammenhang mit Zeiträumen benutzt wird. */
	static DatesConstant of = new DatesConstant(
			"sVof", "(Di|di|Del|del|Dell|dell|D|d|Dei|dei|Della|della|Delle|delle)");

	/** Der Heilige. */
	static DatesConstant saint = new DatesConstant(
			"sVsa", "(San|Santo|santo|Santi)");
	
	/** Alle */
	static DatesConstant all = new DatesConstant(
			"sVal", "(Tutti|tutti)");
	
	/** Ostermontag: 1 Tag nach Ostern */
	static DatesConstant angel = new DatesConstant(
			"sVan", "(Angelo)");
	
	/** Bezeichnung eines Jahres. */
	static DatesConstant year = new DatesConstant(
			"sVye", "(Anno|anno|Anni|anni)");
	
	/** Bezeichnung eines Tages. */
	static DatesConstant day = new DatesConstant(
			"sVda", "(Giorno|giorno)");
	
	// spezielle Zeichen (P)
	/** Das Apostroph. */
	static DatesConstant apostrophe = new DatesConstant(
			"sPap", "('|’)");
	
	/** Ein Ordinalzeichen. */
	static DatesConstant ordinalIndicator = new DatesConstant(
			"sPoi", "(º|°)");
	
}

