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
package org.bbaw.pdr.ae.config.editor.internal;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.bbaw.pdr.ae.common.AEConstants;
import org.eclipse.osgi.util.NLS;

/** NL Class for config editor.
 * @author Christoph Plutte
 *
 */
public final class ConfigMessages extends NLS
{
	/** name of bundle for messages of package editors in puglin org.bbaw.pdr.ae.nl.*/
	private static final String BUNDLE_NAME = "org.bbaw.pdr.ae.nl.config.view.configMessages"; //$NON-NLS-1$

	/** resource bundle.*/
	private static ResourceBundle resourceBundle;

	/**
	 * private constructor.
	 */
	private ConfigMessages()
	{
	}
	/** get Resource Bundle.
	 * @return resource bundle
	 */
	private static ResourceBundle getResourceBundle()
	{
		 	if (resourceBundle == null)
		 	{
				try
				{
					resourceBundle = ResourceBundle.getBundle(BUNDLE_NAME, AEConstants.getCurrentLocale());
				}
				catch (MissingResourceException e)
				{
					e.printStackTrace();
					resourceBundle = ResourceBundle.getBundle(BUNDLE_NAME,
							Locale.GERMAN);
				}
			}
			return resourceBundle;
		}

	  /** get the language string of the given key.
	 * @param key key for translated name.
	 * @return the name according to nl settings.
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

//	public static String Config_add;
//
//	public static String Config_add_tooltip;
//
//	public static String Config_as_display_name;
//
//	public static String Config_as_display_name_tooltip;
//
//	public static String Config_as_person_name;
//
//	public static String Config_as_person_name_tooltip;
//
//	public static String Config_based_on;
//
//	public static String Config_delete;
//
//	public static String Config_delete_configuration;
//
//	public static String Config_delete_entry;
//
//	public static String Config_delete_entry_tooltip;
//
//	public static String Config_delete_tooltip;
//
//	public static String Config_delete_warning1;
//
//	public static String Config_delete_warning2;
//
//	public static String Config_displayname_of_person;
//
//	public static String Config_documentation;
//
//	public static String Config_editor_title;
//
//	public static String Config_ignore;
//
//	public static String Config_label;
//
//	public static String Config_language;
//
//	public static String Config_mandatory;
//
//	public static String Config_message_same_provider_name;
//
//	public static String Config_name_of_person;
//
//	public static String Config_new;
//
//	public static String Config_new_delete;
//
//	public static String Config_new_provider;
//
//	public static String Config_open;
//
//	public static String Config_open_tooltip;
//
//	public static String Config_position;
//
//	public static String Config_priority;
//
//	public static String Config_provider;
//
//	public static String Config_remove_person_name_settting;
//
//	public static String Config_remove_person_name_settting_tooltip;
//
//	public static String Config_value;

}
