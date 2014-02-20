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
/*
 * @author: Christoph Plutte
 */
package org.bbaw.pdr.ae.dateparser;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.osgi.util.NLS;

/**
 * The Class Messages.
 * @author Christoph Plutte
 */
public final class Messages extends NLS
{
	/**
	 * name of bundle for messages of package editors in puglin.
	 * org.bbaw.pdr.ae.nl
	 * @author Christoph Plutte
	 */
	private static final String BUNDLE_NAME = "org.bbaw.pdr.ae.dateparser.nl.messages"; //$NON-NLS-1$

	/** The resource bundle. */
	private static ResourceBundle resourceBundle;

	/**
	 * Gets the resource bundle.
	 * @return the resource bundle
	 */
	private static ResourceBundle getResourceBundle()
	{
		if (resourceBundle == null)
		{
			try
			{
				resourceBundle = ResourceBundle.getBundle(BUNDLE_NAME, Locale.getDefault());
			}
			catch (MissingResourceException e)
			{
				e.printStackTrace();
				resourceBundle = ResourceBundle.getBundle(BUNDLE_NAME, Locale.ENGLISH);
			}
		}
		return resourceBundle;
	}

	/**
	 * Gets the string.
	 * @param key the key
	 * @return the string
	 */
	public static String getString(final String key)
	{
		try
		{
			return getResourceBundle().getString(key);
		}
		catch (MissingResourceException e)
		{
			e.printStackTrace();
			return "!" + key + "!";
		}
	}

	/**
	 * Instantiates a new messages.
	 */
	private Messages()
	{
	}
}
