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
package org.bbaw.pdr.ae.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;
import java.util.regex.Pattern;

import org.bbaw.pdr.ae.common.utils.CopyDirectory;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.RGB;
import org.osgi.framework.Bundle;

/**
 * Zentrale Ablage fuer Konstanten des Archiv-Editors.
 * @author Christoph Plutte
 */

public final class AEConstants
{
	/** Logger. */
	public static final ILog ILOGGER = CommonActivator.getDefault().getLog();
	/** Path to config file. */
	public static final String PROPERTIES_FILENAME;
	/* Konstanten */
	/** Properties. */
	public static final Properties PROPERTIES;
	/** tagging array. */
	public static final String[] TAGGING;
	/** time dimensions types. */
	public static final String[] TIMEDIMTYPES;
	/** time types. */
	public static final String[] TIME_TYPES;
	/** spatial types. */
	public static final String[] SPATIALDIMTYPES;
	/** qualities of references. */
	public static final String[] REFRENCEQUALITIES;
	/** scale of places. */
	public static final String[] PLACESCALE;
	/** file separator. */
	public static final String FS = System.getProperty("file.separator");
	/** alphabet. */
	public static final String[] ALPHABET;
	/** alphabet extended. */
	public static final String[] ALPHABET_EXTENDED;
	/** days. */
	public static final String[] DAYS;
	/** months. */
	public static final String[] MONTHS;
	/** date fuzzy. */
	public static final String[] DATE_FUZZY = new String[]
	{"am", "vor", "nach", "um"};
	/** languages of application. */
	public static final String[] LANGUAGES_AE;
	/** ae home directory. */
	public static final String AE_HOME;
	
	public static boolean SAVE_DB_IN_INSTALLATION_DIR;

	public static boolean SHOW_INSTALLATION_DIALOG;

	// public static final String BASEX_HOME;
	// public static final String BASEX_WORK;
	/** baseX home sup path. */
	public static final String BASEX_SUB = "baseXHOME" + System.getProperty("file.separator");
	/** text style normal. */
	public static final int TEXTSTYLE_NORMAL = SWT.NORMAL;
	/** text style bold. */
	public static final int TEXTSTYLE_BOLD = SWT.BOLD;
	/** text style italic. */
	public static final int TEXTSTYLE_ITALIC = SWT.ITALIC;
	/** text style underline. */
	public static final int TEXTSTYLE_UNDERLINE = 111;
	/** Path to config file. */
	public static final int MAX_NUMBER_CATEGORIES = 40;
	/** external person identifiers provider. */
	// public static final String[] EXTERNAL_IDENTIFIER_PROVIDER;
	/** time accuracy types. */
	public static final String[] TIME_ACCURACY;
	/** faceted person search default proposals. */
	public static final String[] FACET_PERSON_SEARCH_PROPOSALS;
	/** reference name types. */
	public static final String[] REF_NAME_TYPE;
	/** reference name part types. */
	public static final String[] REF_NAMEPART_TYPE;
	/** reference role term codes. */
	public static final String[] REF_ROLETERM_CODE;
	/** reference role term text. */
	public static final String[] REF_ROLETERM_TEXT;
	/** reference date encoding types. */
	public static final String[] REF_DATE_ENCODING;
	/** reference identifier types. */
	public static final String[] REF_IDENTIFIER_TYPE;
	/** path to data directory. */
	public static final String DATA_DIR;
	/** default management directory. */
	public static final String DEFAULT_MANAGEMENT_DIR;
	/** default persname color. */
	public static final String ASPECT_COLOR_PERSNAME;
	/** default orgname color. */
	public static final String ASPECT_COLOR_ORGNAME;
	/** default placename color. */
	public static final String ASPECT_COLOR_PLACENAME;
	/** default date color. */
	public static final String ASPECT_COLOR_DATE;
	/** default name color. */
	public static final String ASPECT_COLOR_NAME;
	// GUI Colors
	public static final RGB VIEW_BACKGROUND_SELECTED_RGB = new RGB(255, 255, 204);
	public static final RGB VIEW_BACKGROUND_DESELECTED_RGB = new RGB(255, 255, 255);;
	public static final RGB VIEW_FOREGROUND_SELECTED_RGB = new RGB(0, 0, 153);
	public static final RGB VIEW_FOREGROUND_DESELECTED_RGB = new RGB(128, 128, 128);
	public static final RGB VIEW_TEXT_SELECTED_RGB = new RGB(0, 0, 0);
	public static final RGB VIEW_TEXT_DESELECTED_RGB = new RGB(163, 163, 163);

	/** default markup sort by priority. */
	public static final boolean ASPECT_VIEW_MARKUPSORT_BYPRIORITY;
	/** pdr id pattern. */
	public static final Pattern PDR_ID_PATTERN = Pattern.compile("pdr[APRU]o\\.\\d{3}\\.\\d{3}\\.\\d{9}");
	/** date format of administrative dates in PDR. */
	public static final SimpleDateFormat ADMINDATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.GERMAN);
	/** default values for preferences. */
	public static final String DEFAULT_LANGUAGE;
	/** default user language selected. */
	public static final boolean USER_LANGUAGE_OK;
	/**
	 * string for configuring aspect editor and primary classification authority
	 * for category view and select object dialog.
	 */
	public static final String CLASSIFICATION_AUTHORITY;
	/** general rights configuration. */
	public static final boolean RIGHTS_GENERAL;
	/**
	 * boolean for rights configuration. WG - Workgroup PG - Projectgroup A -
	 * All R - Read W - Write
	 */
	public static final boolean RIGHTS_WGR;
	/** workgroup write. */
	public static final boolean RIGHTS_WGW;
	/** project group read. */
	public static final boolean RIGHTS_PGR;
	/** project group write. */
	public static final boolean RIGHTS_PGW;
	/** all read. */
	public static final boolean RIGHTS_AR;
	/** primary tagging list provider. */
	public static final String TAGGING_LIST_PROVIDER;
	/** primary relation classification provider. */
	public static final String RELATION_CLASSIFICATION_PROVIDER;
	/** default view settings. */
	public static final boolean ASPECT_VIEW_ID;
	/** default view aspect relations. */
	public static final boolean ASPECT_VIEW_RELATIONS;
	/** default view aspect references. */
	public static final boolean ASPECT_VIEW_REFERENCE;
	/** default preselected year. */
	public static final Integer ASPECT_PRESELECTED_DATE_YEAR;
	/** repository name. */
	public static final String REPOSITORY_NAME;

	/** default values of reference display name. */
	public static final boolean REFERENCE_VIEW_AUTHOR_SURNAME;
	/** author forename. */
	public static final boolean REFERENCE_VIEW_AUTHOR_FORENAME;
	/** author surname. */
	public static final boolean REFERENCE_VIEW_OTHER_SURNAME;
	/** title. */
	public static final boolean REFERENCE_VIEW_TITLE;
	/** title partname. */
	public static final boolean REFERENCE_VIEW_TITLE_PARTNAME;
	/** partnumber. */
	public static final boolean REFERENCE_VIEW_TITLE_PARTNUMBER;
	/** signatur. */
	public static final boolean REFERENCE_VIEW_SIGNATUR;
	/** year. */
	public static final boolean REFERENCE_VIEW_YEAR;
	/** location. */
	public static final boolean REFERENCE_VIEW_LOCATION;
	/** default automated update. */
	public static final boolean AUTOMATED_UPDATE;

	/** first update. */
	public static Date FIRST_EVER_UPDATE_TIMESTAMP;
	/** repository id. */
	public static final int REPOSITORY_ID;
	/** project id. */
	public static final int PROJECT_ID;
	/** repository url. */
	public static final String REPOSITORY_URL;
	/** temp download directory. */
	public static final String TEMP_DOWNLOAD_DIR;

	public static final boolean AE_ADVANCED_VERSION;

	public static final boolean ASPECT_VIEW_NOTIFICATION_TEMPLATE;
	public static final String MARKUP_EDITOR;
	public static final String MARKUP_PRESENTATION;
	public static final String REFERENCE_PRESENTATION;
	
	public static final boolean ASPECT_LITE_EDIT_ANA_KEY;
	
	/* Initialisierung */
	static
	{
		try
		{
			FIRST_EVER_UPDATE_TIMESTAMP = ADMINDATE_FORMAT.parse("2011-01-01T12:00:00");
		}
		catch (ParseException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		IPath actLoc = CommonActivator.getDefault().getStateLocation();
		IPath p = Platform.getLocation();
		IStatus ae = new Status(IStatus.INFO, CommonActivator.PLUGIN_ID, "actLoc.toString(): " + actLoc.toString());
		ILOGGER.log(ae);
		ae = new Status(IStatus.INFO, CommonActivator.PLUGIN_ID, "p.toString(): " + p.toString());
		ILOGGER.log(ae);
		ae = new Status(IStatus.INFO, CommonActivator.PLUGIN_ID, "reduced string: " + actLoc.removeLastSegments(4));
		ILOGGER.log(ae);
		
		String home = CommonActivator.getAEHome();
		if (home != null)
		{

			AE_HOME = home;


		}
		else
		{
//			 develop in eclipse
			// String pdrHome = System.getenv("PDR_HOME");
			// if (pdrHome != null)
			// {
			// AE_HOME = pdrHome;
			// }
			// else
			{
				 if (System.getProperty("os.name").toLowerCase().contains("mac"))
				 {
					 AE_HOME = actLoc.removeLastSegments(7).toOSString();// + FS +
					 // "workspace_ae8"+
					 // FS +
					 // "ArchivEditor";
				 }
				 else
				 {
					 AE_HOME = actLoc.removeLastSegments(4).toOSString();// + FS +
					 // "workspace_ae_ng";
				 }
				// rap
//				AE_HOME = actLoc.removeLastSegments(8).toOSString();
	
				// win pc export
				// AE_HOME = actLoc.removeLastSegments(4).toOSString();// + FS +
				// "workspace_ae6"+ FS + "ArchivEditor";
				// mac export
				// AE_HOME = actLoc.removeLastSegments(7).toOSString();// + FS +
				// "workspace_ae6"+ FS + "ArchivEditor";
			}
		}
		TEMP_DOWNLOAD_DIR = AE_HOME + FS  + "temp";
		File tf = new File(TEMP_DOWNLOAD_DIR);
		if (!tf.exists())
		{
			tf.mkdir();
		}
		ae = new Status(IStatus.INFO, CommonActivator.PLUGIN_ID, "AE_HOME: " + AE_HOME);
		ILOGGER.log(ae);
		// BASEX_HOME = AE_HOME + FS + BASEX_SUB;
		// ae = new Status(IStatus.INFO,Activator.PLUGIN_ID, "BASEX_HOME: " +
		// BASEX_HOME);
		// iLogger.log(ae);
		// BASEX_WORK = BASEX_HOME + "WORK";
		// ae = new Status(IStatus.INFO,Activator.PLUGIN_ID, "BASEX_WORK: " +
		// BASEX_WORK);
		// iLogger.log(ae);
		PROPERTIES_FILENAME = AE_HOME  + FS + "AEConfig" + FS + "config.properties";

		/* Alphabet */
		ALPHABET = new String[26];
		for (int i = 0; i <= 25; i++)
		{
			ALPHABET[i] = Character.toString((char) (i + 97));
		}
		char[] alphabet = "abcdefghijklmnopqrstuvwxyzäöüß#".toCharArray();
		ALPHABET_EXTENDED = new String[alphabet.length];
		for (int i = 0; i < alphabet.length; i++)
		{
			ALPHABET_EXTENDED[i] = Character.toString((alphabet[i]));
		}
		/* Tage und Monate */
		DAYS = new String[32];
		for (int i = 0; i <= 31; i++)
		{
			DAYS[i] = (i == 0) ? "" : Integer.toString(i);
		}
		MONTHS = new String[13];
		for (int i = 0; i < 13; i++)
		{
			switch (i)
			{
				case 0:
					MONTHS[i] = "";
					break;
				default:
					MONTHS[i] = Integer.toString(i);
			}
		}

		/** Properties laden. */
		PROPERTIES = new Properties();
		File file = new File(PROPERTIES_FILENAME);
		if (!file.exists())
		{
			ResourceLocator locator = new ResourceLocator();
			InputStream stream = locator.getClass().getClassLoader().getResourceAsStream("/AEConfig.zip");
			CopyDirectory.unZipIt(stream, AE_HOME);
			file = new File(PROPERTIES_FILENAME);
		}
		File dbDir = new File(AE_HOME  + FS + "baseXHOME");
		if (!dbDir.exists())
		{
			ResourceLocator locator = new ResourceLocator();
			InputStream stream = locator.getClass().getClassLoader().getResourceAsStream("/baseXHOME.zip");
			CopyDirectory.unZipIt(stream, AE_HOME);
		}
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
		// } catch (FileNotFoundException e) {
		// _l.fatal("Ausnahme beim Laden von: " + file.toURI().toString(), e);
		// throw new RuntimeException(e);
		// } catch (IOException e) {
		// _l.fatal("Ausnahme beim Laden von: " + file.toURI().toString(), e);
		// throw new RuntimeException(e);
		
		SAVE_DB_IN_INSTALLATION_DIR = (PROPERTIES.getProperty("SAVE_DB_IN_INSTALLATION_DIR").equals("true"));
		
		
		// } /* timeDimTypes */
		TIMEDIMTYPES = PROPERTIES.getProperty("timeDimTypes").split("\\|");
		/* spatialDimTypes */
		SPATIALDIMTYPES = PROPERTIES.getProperty("spatialDimTypes").split("\\|");
		/* referenceQualities */
		REFRENCEQUALITIES = PROPERTIES.getProperty("referenceQualities").split("\\|");

		/* placeScale */
		PLACESCALE = PROPERTIES.getProperty("placeScale").split("\\|");

		/* Tagging */
		TAGGING = PROPERTIES.getProperty("tagging").split("\\|");
		/* external identifier provider such as pnd, lccn */
		// EXTERNAL_IDENTIFIER_PROVIDER =
		// PROPERTIES.getProperty("external_identifier_provider").split("\\|");

		/* Languages */
		LANGUAGES_AE = PROPERTIES.getProperty("languages").split("\\|");
		/* time accuracy */
		TIME_ACCURACY = PROPERTIES.getProperty("time_accuracy").split("\\|");
		/* time type */
		TIME_TYPES = PROPERTIES.getProperty("time_type").split("\\|");
		/* facet person search proposals. */
		FACET_PERSON_SEARCH_PROPOSALS = PROPERTIES.getProperty("facet_person_search_proposals").split("\\|");
		/* time type */
		REF_NAME_TYPE = PROPERTIES.getProperty("ref_name_type").split("\\|");
		REF_NAMEPART_TYPE = PROPERTIES.getProperty("ref_namePart_type").split("\\|");
		REF_ROLETERM_CODE = PROPERTIES.getProperty("ref_roleTerm_code").split("\\|");
		REF_ROLETERM_TEXT = PROPERTIES.getProperty("ref_roleTerm_text").split("\\|");
		REF_DATE_ENCODING = PROPERTIES.getProperty("ref_date_encoding").split("\\|");
		REF_IDENTIFIER_TYPE = PROPERTIES.getProperty("ref_identifier_type").split("\\|");
		/* Categories */
		DEFAULT_LANGUAGE = PROPERTIES.getProperty("language");
		IStatus sLang = new Status(IStatus.INFO, CommonActivator.PLUGIN_ID, "Language set to: " + DEFAULT_LANGUAGE);
		ILOGGER.log(sLang);
		USER_LANGUAGE_OK = PROPERTIES.getProperty("userDefinedLanguageOk").equals("true");
		IStatus sUserLang = new Status(IStatus.INFO, CommonActivator.PLUGIN_ID, "user defined language ok: "
				+ USER_LANGUAGE_OK);
		ILOGGER.log(sUserLang);
		/* selected external person identifiers */
		// PND = (PROPERTIES.getProperty("pnd").equals("true"));
		// LCCN = (PROPERTIES.getProperty("lccn").equals("true"));
		// ICCU = (PROPERTIES.getProperty("iccu").equals("true"));
		// VIAF = (PROPERTIES.getProperty("viaf").equals("true"));

		/* urls of external person identifier */
		// PND_URL = PROPERTIES.getProperty("pnd_url");
		// LCCN_URL = PROPERTIES.getProperty("lccn_url");
		// ICCU_URL = PROPERTIES.getProperty("iccu_url");
		// VIAF_URL = PROPERTIES.getProperty("viaf_url");
		CLASSIFICATION_AUTHORITY = PROPERTIES.getProperty("classification_authority");
		/* set primary tagging list provider */
		TAGGING_LIST_PROVIDER = PROPERTIES.getProperty("tagging_list_provider");
		/* set primary relation classification provider */
		RELATION_CLASSIFICATION_PROVIDER = PROPERTIES.getProperty("relation_classification_provider");
		// System.out.println("relation class prov : " +
		// RELATION_CLASSIFICATION_PROVIDER);
		/* load rights configuration */
		RIGHTS_GENERAL = (PROPERTIES.getProperty("general_rights").equals("true"));
		RIGHTS_WGR = (PROPERTIES.getProperty("workgroup_read").equals("true"));
		RIGHTS_WGW = (PROPERTIES.getProperty("workgroup_write").equals("true"));
		RIGHTS_PGR = (PROPERTIES.getProperty("projectgroup_read").equals("true"));
		RIGHTS_PGW = (PROPERTIES.getProperty("projectgroup_write").equals("true"));
		RIGHTS_AR = (PROPERTIES.getProperty("all_read").equals("true"));
		ASPECT_VIEW_ID = (PROPERTIES.getProperty("ASPECT_VIEW_ID").equals("true"));
		ASPECT_VIEW_RELATIONS = (PROPERTIES.getProperty("ASPECT_VIEW_RELATIONS").equals("true"));
		ASPECT_VIEW_REFERENCE = (PROPERTIES.getProperty("ASPECT_VIEW_REFERENCE").equals("true"));
		ASPECT_VIEW_MARKUPSORT_BYPRIORITY = (PROPERTIES.getProperty("ASPECT_VIEW_MARKUPSORT_BYPRIORITY").equals("true"));
		ASPECT_VIEW_NOTIFICATION_TEMPLATE = (PROPERTIES.getProperty("ASPECT_VIEW_NOTIFICATION_TEMPLATE").equals("true"));

		DATA_DIR = PROPERTIES.getProperty("DATA_DIR");
		DEFAULT_MANAGEMENT_DIR = PROPERTIES.getProperty("DEFAULT_MANAGEMENT_DIR");

		ASPECT_COLOR_PERSNAME = PROPERTIES.getProperty("ASPECT_COLOR_PERSNAME");
		ASPECT_COLOR_ORGNAME = PROPERTIES.getProperty("ASPECT_COLOR_ORGNAME");
		ASPECT_COLOR_PLACENAME = PROPERTIES.getProperty("ASPECT_COLOR_PLACENAME");
		ASPECT_COLOR_DATE = PROPERTIES.getProperty("ASPECT_COLOR_DATE");
		ASPECT_COLOR_NAME = PROPERTIES.getProperty("ASPECT_COLOR_NAME");

		ASPECT_PRESELECTED_DATE_YEAR = Integer.valueOf(PROPERTIES.getProperty("ASPECT_PRESELECTED_DATE_YEAR"));
		REPOSITORY_NAME = PROPERTIES.getProperty("REPOSITORY_NAME");

		REFERENCE_VIEW_AUTHOR_SURNAME = (PROPERTIES.getProperty("REFERENCE_VIEW_AUTHOR_SURNAME").equals("true"));
		REFERENCE_VIEW_AUTHOR_FORENAME = (PROPERTIES.getProperty("REFERENCE_VIEW_AUTHOR_FORENAME").equals("true"));
		REFERENCE_VIEW_OTHER_SURNAME = (PROPERTIES.getProperty("REFERENCE_VIEW_OTHER_SURNAME").equals("true"));
		REFERENCE_VIEW_TITLE = (PROPERTIES.getProperty("REFERENCE_VIEW_TITLE").equals("true"));
		REFERENCE_VIEW_TITLE_PARTNAME = (PROPERTIES.getProperty("REFERENCE_VIEW_TITLE_PARTNAME").equals("true"));

		REFERENCE_VIEW_TITLE_PARTNUMBER = (PROPERTIES.getProperty("REFERENCE_VIEW_TITLE_PARTNUMBER").equals("true"));
		REFERENCE_VIEW_SIGNATUR = (PROPERTIES.getProperty("REFERENCE_VIEW_SIGNATUR").equals("true"));
		REFERENCE_VIEW_YEAR = (PROPERTIES.getProperty("REFERENCE_VIEW_YEAR").equals("true"));
		REFERENCE_VIEW_LOCATION = (PROPERTIES.getProperty("REFERENCE_VIEW_YEAR").equals("true"));

		AUTOMATED_UPDATE = (PROPERTIES.getProperty("AUTOMATED_UPDATE").equals("true"));

		REPOSITORY_ID = Integer.parseInt(PROPERTIES.getProperty("REPOSITORY_ID"));
		if (CommonActivator.getProjectID() >= 0)
		{
			PROJECT_ID = CommonActivator.getProjectID();
		}
		else
		{
			PROJECT_ID = Integer.parseInt(PROPERTIES.getProperty("PROJECT_ID"));
		}
		REPOSITORY_URL = PROPERTIES.getProperty("REPOSITORY_URL");

		AE_ADVANCED_VERSION = (PROPERTIES.getProperty("AE_ADVANCED_VERSION").equals("true"));

		SHOW_INSTALLATION_DIALOG = (PROPERTIES.getProperty("SHOW_INSTALLATION_DIALOG").equals("true"));

		MARKUP_EDITOR = PROPERTIES.getProperty("MARKUP_EDITOR");
		MARKUP_PRESENTATION = PROPERTIES.getProperty("MARKUP_PRESENTATION");
		REFERENCE_PRESENTATION = PROPERTIES.getProperty("REFERENCE_PRESENTATION");

		ASPECT_LITE_EDIT_ANA_KEY = (PROPERTIES.getProperty("ASPECT_LITE_EDIT_ANA_KEY") != null)
				&& (PROPERTIES.getProperty("SHOW_INSTALLATION_DIALOG").equals("true"));
		CommonActivator.getDefault().getPreferenceStore().setValue("AE_HOME", AE_HOME);

	}

	/**
	 * get current locale.
	 * @return Locale current locale.
	 */
	public static Locale getCurrentLocale()
	{
		// if (userConfigLoader.USER_LANGUAGE_OK)
		// {
		// if (userConfigLoader.LANGUAGE.equals("it"))
		// {
		// currentLocale = Locale.ITALIAN;
		// }
		// else if (userConfigLoader.LANGUAGE.equals("de"))
		// {
		// currentLocale = Locale.GERMAN;
		// }
		// else if (userConfigLoader.LANGUAGE.equals("en"))
		// {
		// currentLocale = Locale.ENGLISH;
		// }
		// else if (userConfigLoader.LANGUAGE.equals("fr"))
		// {
		// currentLocale = Locale.FRENCH;
		// }
		// else
		// {
		// currentLocale = Locale.GERMAN;
		// }
		// }
		// else
		// {
		Locale currentLocale = Locale.getDefault();
		// }
		return currentLocale;
	}

	/**
	 * constructor.
	 */
	private AEConstants()
	{
		CommonActivator.getDefault().getPreferenceStore().setValue("AE_HOME", AE_HOME);
	}

}
