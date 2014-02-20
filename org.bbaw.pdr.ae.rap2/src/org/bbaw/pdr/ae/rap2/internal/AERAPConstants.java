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
package org.bbaw.pdr.ae.rap2.internal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.bbaw.pdr.ae.rap2.Activator;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;

public final class AERAPConstants {
	
	/** Logger. */
	public static final ILog ILOGGER = Activator.getDefault().getLog();
	/** repository id. */
	public static final int REPOSITORY_ID;
	/** project id. */
	public static final int PROJECT_ID;
	/** repository url. */
	public static final String REPOSITORY_URL;
	/** file separator. */
	public static final String FS = System.getProperty("file.separator");
	/** rap home. */
	public static final String AE_RAP_HOME;
	/** Path to config file. */
	public static final String PROPERTIES_FILENAME;
	/** Properties. */
	public static final Properties PROPERTIES;
	
	public static final String[] PROJECT_IDS;
	
	public static final String[] USER_IDS_BLACK_LIST;
	
	public static final String[] USER_IDS_WHITE_LIST;

	/* Initialisierung */
	static
	{
		
		IPath actLoc = Activator.getDefault().getStateLocation();
		IPath p = Platform.getLocation();
		IStatus ae = new Status(IStatus.INFO, Activator.PLUGIN_ID, "actLoc.toString(): " + actLoc.toString());
		ILOGGER.log(ae);
		ae = new Status(IStatus.INFO, Activator.PLUGIN_ID, "p.toString(): " + p.toString());
		ILOGGER.log(ae);
		
		// develop in eclipse
		 String pdrHome = System.getenv("AE_RAP_HOME");
		 if (pdrHome != null)
		 {
			 AE_RAP_HOME = pdrHome;
		 }
		 else
		{

			// rap
			 AE_RAP_HOME = actLoc.removeLastSegments(8).toOSString();


		}
		 ae = new Status(IStatus.INFO, Activator.PLUGIN_ID, "reduced string: " + AE_RAP_HOME);
			ILOGGER.log(ae);
		 /** Properties laden. */
			PROPERTIES = new Properties();
		 PROPERTIES_FILENAME = AE_RAP_HOME  + FS + "RAPConfig" + FS + "config.properties";
		 File file = new File(PROPERTIES_FILENAME);
			// try {
			// if (_i) _l.info("Lade: " + file.toURI().toString());
			try
			{
				PROPERTIES.load(new FileInputStream(file));
			}
			catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		PROJECT_IDS = PROPERTIES.getProperty("PROJECT_IDS").split("\\|");
		REPOSITORY_ID = Integer.parseInt(PROPERTIES.getProperty("REPOSITORY_ID"));
		PROJECT_ID = Integer.parseInt(PROPERTIES.getProperty("PROJECT_ID"));
		REPOSITORY_URL = PROPERTIES.getProperty("REPOSITORY_URL");
		if (PROPERTIES.getProperty("USER_IDS_BLACK_LIST") != null)
		{
			USER_IDS_BLACK_LIST = PROPERTIES.getProperty("USER_IDS_BLACK_LIST").split("\\|");
		}
		else
		{
			USER_IDS_BLACK_LIST = null;
		}
		if (PROPERTIES.getProperty("USER_IDS_WHITE_LIST") != null)
		{
			USER_IDS_WHITE_LIST = PROPERTIES.getProperty("USER_IDS_WHITE_LIST").split("\\|");
		}
		else
		{
			USER_IDS_WHITE_LIST = null;
		}

	}
}
