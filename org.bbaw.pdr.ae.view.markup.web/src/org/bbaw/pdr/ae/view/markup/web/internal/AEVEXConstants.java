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
//package org.bbaw.pdr.ae.view.markup.web.internal;
//
//import java.io.IOException;
//
//import org.eclipse.vex.core.internal.css.StyleSheet;
//import org.eclipse.vex.core.internal.css.StyleSheetReader;
//import org.eclipse.vex.ui.internal.config.ConfigurationRegistry;
//import org.eclipse.vex.ui.internal.config.ConfigurationRegistryImpl;
//import org.eclipse.vex.ui.internal.config.Style;
//
//
//public class AEVEXConstants
//{
//	private AEVEXConstants()
//	{
//	}
//
//	public static final Style VEXSTYLE_ASPECT;
//
//	public static final StyleSheet VEXSTYLESHEET_ASPECT;
//
//	static
//	{
//		ConfigurationRegistry registry = new ConfigurationRegistryImpl(null);
//		Style style = registry.getStyle(Style.EXTENSION_POINT);
//
//		VEXSTYLE_ASPECT = style;
//
//		StyleSheet ss = null;
//		try
//		{
//			ss = new StyleSheetReader().read("file:C:/IT/workspace_ae-2.1/vex-xhtml/1.0/xhtml1-plain.css");
//		}
//		catch (CSSException e)
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		catch (IOException e)
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		VEXSTYLESHEET_ASPECT = ss;
//	}
//}
