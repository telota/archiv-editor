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
 * Die Objekte der Klasse DConst enthalten die Muster zur Kodierung
 * des Textes und die entsprechenden Kodesymbole.
 * @author Martin Fechner
 */
class DatesConstant{

	// VARIABLEN

	/** Der reguläre Ausdruck, nach dem gesucht wird. */
	private String regularExpression; 
	/** Das vier Zeichen lange Kodesymbol. */
	private String symbol;
	/** Der reguläre Ausdruck als Pattern. */
	private Pattern pattern;

	// CONSTRUCTOR

	/** 
	 * Der Konstruktor erhält das Kodesymbol und den regulären Ausdruck.
	 * @param symb Das Kodesymbol bestehend aus vier Zeichen.
	 * @param regExpr Der reguläre Ausdruck.
	 * @throws FalseDatesConstantDefinitionException
	 */
	DatesConstant(String symb, String regExpr) {
		if(symb.length()!=4){
			throw new FalseDatesConstantDefinitionException("DatesConstant isn't defined well.");
		}
		regularExpression = regExpr;
		symbol = symb;
		pattern = Pattern.compile(regExpr);
	}

	class FalseDatesConstantDefinitionException extends RuntimeException{
		/**
		 * 
		 */
		private static final long serialVersionUID = -7353145492082011363L;

		public FalseDatesConstantDefinitionException(String s)
		  {
		    super(s);	
		  }
	}

	// AUSGABE

	/** 
	 * Gibt den regulären Ausdruck als String zurück.
	 * @return Der reguläre Ausdruck.
	 */
	String r() {
		return regularExpression;
	}
	/**
	 * Gibt das Kodesymbol zurück.
	 * @return Das Kodesymbol.
	 */
	String s() {
		return symbol;
	}
	/**
	 * Gibt den regulären Ausdruck als Pattern zurück.
	 * @return Das Pattern.
	 */
	Pattern p() {
		return pattern;
	}
}

