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

import java.util.regex.Pattern;

/**
 * Die Objekte von DSymb sind die Konstanten für die Datumserkennung im Kode.
 * Sie enthalten einen String aus Kodesymbolen,
 * das zugehörige Pattern und eine Liste der entsprechenden Bezeichner
 * für die Formatierung des Datums.
 * @author Martin Fechner
 */
class DatesSymbol{

	/**
	 * Definition der Bezeichnungen einzelner Datumsbestandteile.
	 */
	public enum Label {EMPTY, YEAR1, YEAR2, MONTH1, MONTH2, DAY1, DAY2,
		APPROXIMATION1, APPROXIMATION2, LIMIT1, LIMIT2, CONNECTION}

	/** Die Liste der Bezeichner für die spätere Formatierung. */
	private Label[] label;
	/** Das Pattern für die Datumserkennung im Kodetext. */
	private Pattern p;

	/** Der Konstruktor initialisiert die Lister der Bezeichner und das Pattern.
	 * @param lab Die Liste der Bezeichner.
	 * @param symb Der reguläre Ausdruck zur Erkennung des Datums im Kode. 
	 * @throws FalseDatesSymbolDefinitionException 
	 */
	DatesSymbol(Label[] lab, String[] symb){
		label = lab;
		String symbol;
		if(lab.length != symb.length){
			throw new FalseDatesSymbolDefinitionException("DatesSymbol isn't defined well.");
		}
		if((symb == null)||(symb.length == 0)){
			symbol = "";
		} else {
			StringBuffer buffer = new StringBuffer(symb[0]);
			for (int i=1; i<symb.length; i++){
				buffer.append(symb[i]);
			}
			symbol = buffer.toString();
		}
		p = Pattern.compile(symbol);
	}
	
	class FalseDatesSymbolDefinitionException extends RuntimeException{
		/**
		 * 
		 */
		private static final long serialVersionUID = -6507435953981861963L;

		public FalseDatesSymbolDefinitionException(String s)
		  {
		    super(s);	
		  }
	}
	
	/** Gibt die Liste der Bezeichnunger zurück.
	 * @return Die Liste der Bezeichner.
	 */
	Label[] l(){
		return label;
	}

	/** Gibt den regulären Ausdruck als Pattern zurück.
	 * @return Das angeforderte Pattern.
	 */
	Pattern p(){
		return p;
	}
	
}


