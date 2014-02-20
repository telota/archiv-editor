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
package org.bbaw.pdr.ae.control.core;

import org.bbaw.pdr.ae.common.AEConstants;
import org.bbaw.pdr.ae.common.CommonActivator;
import org.bbaw.pdr.ae.control.facade.Facade;
import org.bbaw.pdr.ae.metamodel.PdrId;
import org.bbaw.pdr.ae.model.Aspect;
import org.bbaw.pdr.ae.model.BasicPersonData;
import org.bbaw.pdr.ae.model.IdentifierMods;
import org.bbaw.pdr.ae.model.LocationMods;
import org.bbaw.pdr.ae.model.NameMods;
import org.bbaw.pdr.ae.model.Note;
import org.bbaw.pdr.ae.model.OriginInfo;
import org.bbaw.pdr.ae.model.Person;
import org.bbaw.pdr.ae.model.ReferenceMods;
import org.bbaw.pdr.ae.model.TitleInfo;
import org.eclipse.core.runtime.Platform;

/**
 * class to process the display name of pdrObjects.
 * @author Christoph Plutte
 */
public class PDRObjectDisplayNameProcessor
{
	/** facade singleton instance. */
	private Facade _facade = Facade.getInstanz();
	/** semantic provider. */
	private String _provider = Platform
			.getPreferencesService()
			.getString(CommonActivator.PLUGIN_ID,
					"PRIMARY_SEMANTIC_PROVIDER", AEConstants.CLASSIFICATION_AUTHORITY, null).toUpperCase(); //$NON-NLS-1$
	/** local language. */
	private String _lang = AEConstants.getCurrentLocale().getLanguage();

	/** min year. */
	private static final int MIN_YEAR = 1000;

	/**
	 * process display name of aspect.
	 * @param aspect aspect to be processed.
	 */
	public final void processDisplayName(final Aspect aspect)
	{
		if (aspect != null)
		{
			String name = "Error";
			if (aspect.getNotification() != null)
			{
				if (aspect.getNotification().length() > 30)
				{
					name = aspect.getNotification().substring(0, 27) + "...";
				}
				else
				{
					name = aspect.getNotification();
				}
			}
			aspect.setDisplayName(name.replace("\n", "").replace("\t", " "));
		}
	}

	/**
	 * process person display name.
	 * @param person person to be processed.
	 */
	public final void processDisplayName(final Person person)
	{
		if (person != null)
		{
			String displayName = "Error";
			BasicPersonData basicPersonData = person.getBasicPersonData();
			if (basicPersonData != null)
			{
				if (basicPersonData.getDisplayNames() != null && !basicPersonData.getDisplayNames().isEmpty())
				{

					// System.out.println("size of displaynames " +
					// basicPersonData.getDisplayNames().size());
					// for (String s :
					// basicPersonData.getDisplayNames().keySet())
					// {
					// System.out.println(" keys " + s);
					// }
					// System.out.println("id der person  " +
					// getPdrId().toString());
					if (basicPersonData.getDisplayNames().get(_provider + "_" + _lang) != null)
					{
						displayName = basicPersonData.getDisplayNames().get(_provider + "_" + _lang).toString();
						// System.out.println("norm display name " +provider +
						// "_" + lang+" "+ displayName);
					}
					else if (basicPersonData.getDisplayNames().get(_provider) != null)
					{
						displayName = basicPersonData.getDisplayNames().get(_provider).toString();
						// System.out.println("norm display name "+provider +
						// displayName);
					}

					else if (basicPersonData.getDisplayNames().get("PDR_2.0_" + _lang) != null)
					{
						displayName = basicPersonData.getDisplayNames().get("PDR_2.0_" + _lang).toString();
						// System.out.println("norm display name " +"pdr_" +
						// lang + " "+ displayName);
					}
					else if (basicPersonData.getDisplayNames().get("PDR_2.0") != null)
					{
						displayName = basicPersonData.getDisplayNames().get("PDR_2.0").toString();
						// System.out.println("norm display name " +
						// displayName);
					}
					else
					{
						for (String s : basicPersonData.getDisplayNames().keySet())
						{
							displayName = basicPersonData.getDisplayNames().get(s).toString();
							// System.out.println("norm display name aus der schleife "
							// + displayName);
						}
					}

				}
				else if (basicPersonData.getComplexNames() != null && !basicPersonData.getComplexNames().isEmpty())
				{
					displayName = basicPersonData.getComplexNames().firstElement().toString();
					// System.out.println("basic name " + displayName);
				}
				if (Platform.getPreferencesService().getBoolean(CommonActivator.PLUGIN_ID,
						"PERSON_DISPLAYNAME_LIFESPAN", false, null)
						|| displayName.trim().length() == 0)
				{
					if (basicPersonData.getBeginningOfLife() != null && basicPersonData.getEndOfLife() != null)
					{
						displayName += " (" + basicPersonData.getBeginningOfLife().toString() + " - "
								+ basicPersonData.getEndOfLife().toString() + ")";
					}
					else if (basicPersonData.getBeginningOfLife() != null)
					{
						displayName += " (" + basicPersonData.getBeginningOfLife().toString() + " - ????)";
					}
					else if (basicPersonData.getEndOfLife() != null)
					{
						displayName += " (???? - " + basicPersonData.getEndOfLife().toString() + ")";
					}
				}
				if (Platform.getPreferencesService().getBoolean(CommonActivator.PLUGIN_ID,
						"PERSON_DISPLAYNAME_DESCRIPTION", false, null)
						|| displayName.trim().length() == 0)
				{
					if (basicPersonData.getDescriptions() != null)
					{
						boolean first = true;
						for (String s : basicPersonData.getDescriptions())
						{
							if (first && displayName.trim().length() > 0)
							{
								displayName += ": ";
							}
							else if (first && displayName.trim().length() > 0)
							{
								displayName += ", ";
							}
							displayName += s;
							first = false;

						}
					}
				}
			}
			else if (basicPersonData == null && person.getPdrId() != null)
			{
				displayName = person.getPdrId().toString();
			}
			person.setDisplayName(displayName.trim());
		}
	}

	/**
	 * process reference display name.
	 * @param reference to be processed
	 */
	public final void processDisplayName(final ReferenceMods reference)
	{
		if (reference != null)
		{
			String name = "";
			if (reference.getNameMods() != null)
			{
				for (NameMods n : reference.getNameMods())
				{
					if (n.getRoleMods().getRoleTerm() != null && n.getRoleMods().getRoleTerm().equals("aut"))
					{
						if (Platform.getPreferencesService().getBoolean(CommonActivator.PLUGIN_ID,
								"REFERENCE_VIEW_AUTHOR_SURNAME", true, null)
								&& Platform.getPreferencesService().getBoolean(CommonActivator.PLUGIN_ID,
										"REFERENCE_VIEW_AUTHOR_FORENAME", true, null))
						{
							if (name.length() == 0 && n.getFullName() != null && n.getFullName().trim().length() > 0)
							{
								name = n.getFullName();
							}
						}
						else if (Platform.getPreferencesService().getBoolean(CommonActivator.PLUGIN_ID,
								"REFERENCE_VIEW_AUTHOR_SURNAME", true, null))
						{
							if (name.length() == 0 && n.getFullName() != null && n.getFullName().trim().length() > 0)
							{
								name = n.getSurname();
							}
						}
						else if (Platform.getPreferencesService().getBoolean(CommonActivator.PLUGIN_ID,
								"REFERENCE_VIEW_AUTHOR_FORENAME", true, null))
						{
							if (name.length() == 0 && n.getFullName() != null && n.getFullName().trim().length() > 0)
							{
								name = n.getForename();
							}
						}
						if (name.trim().length() > 0)
						{
							break;
						}
					}
				}
			}
			if (Platform.getPreferencesService().getBoolean(CommonActivator.PLUGIN_ID, "REFERENCE_VIEW_OTHER_SURNAME",
					true, null))
			{
				if (reference.getNameMods() != null)
				{
					for (NameMods n : reference.getNameMods())
					{
						if (n.getRoleMods().getRoleTerm() != null && !n.getRoleMods().getRoleTerm().equals("aut"))
						{
							if (n.getSurname() != null && n.getSurname().trim().length() > 0)
							{
								if (name.trim().length() > 0)
								{
									name = name + "; " + n.getSurname();
								}
								else
								{
									name = n.getSurname();
								}
							}
						}
					}
				}
			}
			if (Platform.getPreferencesService().getBoolean(CommonActivator.PLUGIN_ID, "REFERENCE_VIEW_TITLE", true,
					null))
			{
				if (reference.getTitleInfo() != null && reference.getTitleInfo().getTitle() != null
						&& reference.getTitleInfo().getTitle().trim().length() > 0)
				{
					String title = reference.getTitleInfo().getTitle().trim();
					if (name.trim().length() != 0)
					{
						name = name + ": ";
					}
					if (title.length() > 30)
					{
						name += title.substring(0, 28) + "...";
					}
					else
					{
						name += title;
					}
				}
			}
			if (Platform.getPreferencesService().getBoolean(CommonActivator.PLUGIN_ID, "REFERENCE_VIEW_TITLE_PARTNAME",
					false, null))
			{
				if (reference.getTitleInfo() != null && reference.getTitleInfo().getPartName() != null
						&& reference.getTitleInfo().getPartName().trim().length() > 0)
				{
					String title = reference.getTitleInfo().getPartName().trim();
					if (name.trim().length() != 0)
					{
						name = name + ": ";
					}
					if (title.length() > 30)
					{
						name += title.substring(0, 28) + "...";
					}
					else
					{
						name += title;
					}
				}
			}
			if (Platform.getPreferencesService().getBoolean(CommonActivator.PLUGIN_ID,
					"REFERENCE_VIEW_TITLE_PARTNUMBER", false, null))
			{
				if (reference.getTitleInfo() != null && reference.getTitleInfo().getPartNumber() != null
						&& reference.getTitleInfo().getPartNumber().trim().length() > 0)
				{
					if (name.trim().length() != 0)
					{
						name = name + ": ";
					}
					name += reference.getTitleInfo().getPartNumber().trim();
				}
			}
			if (Platform.getPreferencesService().getBoolean(CommonActivator.PLUGIN_ID, "REFERENCE_VIEW_SIGNATUR",
					false, null))
			{
				if (reference.getIdentifiersMods() != null)
				{
					for (IdentifierMods i : reference.getIdentifiersMods())
					{
						if (i.getType() != null && i.getType().equals("Signatur"))
						{
							if (i.getIdentifier() != null && i.getIdentifier().trim().length() > 0)
							{
								if (name.trim().length() > 0)
								{
									name = name + ", " + i.getIdentifier().trim();
								}
								else
								{
									name = i.getIdentifier().trim();
								}
								break;
							}
						}
					}
				}
			}
			if (Platform.getPreferencesService().getBoolean(CommonActivator.PLUGIN_ID, "REFERENCE_VIEW_YEAR", false,
					null))
			{
				if (reference.getOriginInfo() != null)
				{
					OriginInfo oi = reference.getOriginInfo();
					if (oi.getDateIssued() != null && oi.getDateIssued().getYear() > MIN_YEAR)
					{
						if (name.trim().length() > 0)
						{
							name = name + ", " + oi.getDateIssued().getYear();
						}
						else
						{
							name = "" + oi.getDateIssued().getYear();
						}
					}
					if (oi.getDateIssuedTimespan() != null && oi.getDateIssuedTimespan().getDateFrom() != null
							&& oi.getDateIssuedTimespan().getDateFrom().getYear() > MIN_YEAR
							&& oi.getDateIssuedTimespan().getDateTo() != null
							&& oi.getDateIssuedTimespan().getDateTo().getYear() > MIN_YEAR)
					{
						if (name.trim().length() > 0)
						{
							name = name + ", " + oi.getDateIssuedTimespan().getDateFrom().getYear() + "-"
									+ oi.getDateIssuedTimespan().getDateTo().getYear();
						}
						else
						{
							name = "" + oi.getDateIssuedTimespan().getDateFrom().getYear() + "-"
									+ oi.getDateIssuedTimespan().getDateTo().getYear();
						}
					}
					if (oi.getCopyrightDate() != null && oi.getCopyrightDate().getYear() > MIN_YEAR)
					{
						if (name.trim().length() > 0)
						{
							name = name + ", " + oi.getCopyrightDate().getYear();
						}
						else
						{
							name = "" + oi.getCopyrightDate().getYear();
						}
					}
					else if (oi.getCopyrightDateTimespan() != null
							&& oi.getCopyrightDateTimespan().getDateFrom() != null
							&& oi.getCopyrightDateTimespan().getDateTo() != null)
					{
						if (name.trim().length() > 0)
						{
							name = name + ", " + oi.getCopyrightDateTimespan().getDateFrom().getYear() + "-"
									+ oi.getCopyrightDateTimespan().getDateTo().getYear();
						}
						else
						{
							name = "" + oi.getCopyrightDateTimespan().getDateFrom().getYear() + "-"
									+ oi.getCopyrightDateTimespan().getDateTo().getYear();
						}
					}
					else if (oi.getDateCreated() != null && oi.getDateCreated().getYear() > MIN_YEAR)
					{
						if (name.trim().length() > 0)
						{
							name = name + ", " + oi.getDateCreated().getYear();
						}
						else
						{
							name = "" + oi.getDateCreated().getYear();
						}
					}
					else if (oi.getDateCreatedTimespan() != null && oi.getDateCreatedTimespan().getDateFrom() != null
							&& oi.getDateCreatedTimespan().getDateFrom().getYear() > MIN_YEAR
							&& oi.getDateCreatedTimespan().getDateTo() != null
							&& oi.getDateCreatedTimespan().getDateTo().getYear() > MIN_YEAR)
					{
						if (name.trim().length() > 0)
						{
							name = name + ", " + oi.getDateCreatedTimespan().getDateFrom().getYear() + "-"
									+ oi.getDateCreatedTimespan().getDateTo().getYear();
						}
						else
						{
							name = "" + oi.getDateCreatedTimespan().getDateFrom().getYear() + "-"
									+ oi.getDateCreatedTimespan().getDateTo().getYear();
						}
					}
					else if (oi.getDateCaptured() != null && oi.getDateCaptured().getYear() > MIN_YEAR)
					{
						if (name.trim().length() > 0)
						{
							name = name + ", " + oi.getDateCaptured().getYear();
						}
						else
						{
							name = "" + oi.getDateCaptured().getYear();
						}
					}
					else if (oi.getDateCapturedTimespan() != null && oi.getDateCapturedTimespan().getDateFrom() != null
							&& oi.getDateCapturedTimespan().getDateFrom().getYear() > MIN_YEAR
							&& oi.getDateCapturedTimespan().getDateTo() != null
							&& oi.getDateCapturedTimespan().getDateTo().getYear() > MIN_YEAR)
					{
						if (name.trim().length() > 0)
						{
							name = name + ", " + oi.getDateCapturedTimespan().getDateFrom().getYear() + "-"
									+ oi.getDateCapturedTimespan().getDateTo().getYear();
						}
						else
						{
							name = "" + oi.getDateCapturedTimespan().getDateFrom().getYear() + "-"
									+ oi.getDateCapturedTimespan().getDateTo().getYear();
						}
					}
				}
			}
			if (Platform.getPreferencesService().getBoolean(CommonActivator.PLUGIN_ID, "REFERENCE_VIEW_LOCATION",
					false, null))
			{
				if (reference.getLocation() != null && reference.getLocation().getPhysicalLocation() != null
						&& reference.getLocation().getPhysicalLocation().trim().length() > 0)
				{
					if (name.trim().length() != 0)
					{
						name = name + ", ";
					}
					name += reference.getLocation().getPhysicalLocation().trim();
				}
			}
			if (name.trim().length() == 0 && reference.getTitleInfo() != null
					&& reference.getTitleInfo().getTitle() != null)
			{
				name = reference.getTitleInfo().getTitle();
			}
			if (name.trim().length() == 0)
			{
				name = reference.getPdrId().toString();
			}
			reference.setDisplayName(name.trim());

			if (reference.getRelatedItems() != null && reference.getRelatedItems().size() > 0)
			{
				String id = reference.getRelatedItems().firstElement().getId();
				if (id != null)
				{
					ReferenceMods host = _facade.getReference(new PdrId(id));
					if (host != null && host.getHostedReferences() != null
							&& !host.getHostedReferences().contains(reference.getPdrId().toString()))
					{
						host.getHostedReferences().add(reference.getPdrId().toString());
					}
					processHostedRefDisplayName(host, reference, 0);
				}
			}
		}
	}

	/**
	 * process long display name of reference.
	 * @param reference to be processed
	 */
	public final void processDisplayNameLong(final ReferenceMods reference)
	{
		if (reference != null)
		{
			String name = "";
			TitleInfo titleInfo = reference.getTitleInfo();
			OriginInfo oi = reference.getOriginInfo();
			Note note = reference.getNote();
			LocationMods location = reference.getLocation();
			if (reference.getNameMods() != null)
			{
				for (NameMods n : reference.getNameMods())
				{
					if (n.getRoleMods().getRoleTerm() != null && n.getRoleMods().getRoleTerm().equals("aut"))
					{
						if (name.length() == 0 && n.getFullName() != null && n.getFullName().trim().length() > 0)
						{
							if (!name.contains(n.getFullName()))
							{
								if (name.trim().length() > 0)
								{
									name += "; ";
								}
								name += n.getFullName();
							}
						}

					}
				}
				// if (name.trim().length() > 0) name += " (" +
				// EditorsMessages.getString("Editor_role_aut") + ")";
			}
			if (reference.getNameMods() != null)
			{
				for (NameMods n : reference.getNameMods())
				{
					if (n.getRoleMods().getRoleTerm() != null && !n.getRoleMods().getRoleTerm().equals("aut"))
					{
						if (n.getFullName() != null && n.getFullName().trim().length() > 0)
						{
							String ro = n.getRoleMods().getRoleTerm();
							if (name.trim().length() > 0)
							{
								name += "; " + n.getFullName();
							}
							else
							{
								name += n.getFullName();
							}
							if (name.trim().length() > 0)
							{
								name += " (" + ro + ")";
							}
						}
					}
				}
			}
			if (titleInfo != null)
			{
				if (reference.getTitleInfo().getTitle() != null
						&& reference.getTitleInfo().getTitle().trim().length() > 0)
				{
					String title = reference.getTitleInfo().getTitle().trim();
					if (name.trim().length() != 0)
					{
						name += ": ";
					}
					name += title;

				}
				if (titleInfo.getPartName() != null && titleInfo.getPartName().trim().length() > 0)
				{
					if (name.trim().length() != 0)
					{
						name += ". ";
					}
					name += titleInfo.getPartName().trim();
				}
				if (titleInfo.getPartNumber() != null && titleInfo.getPartNumber().trim().length() > 0)
				{
					if (name.trim().length() != 0)
					{
						name += ", ";
					}
					name += titleInfo.getPartNumber().trim();
				}
			}

			if (oi != null)
			{
				if (oi.getPublisher() != null && oi.getPublisher().trim().length() > 0)
				{
					name += ", " + oi.getPublisher().trim();
				}
				if (oi.getPlaceTerm() != null && oi.getPlaceTerm().trim().length() > 0)
				{
					name += ", " + oi.getPlaceTerm().trim();
				}
				if (oi.getDateIssued() != null && oi.getDateIssued().getYear() > MIN_YEAR)
				{
					if (name.trim().length() > 0)
					{
						name = name + ", " + oi.getDateIssued().getYear();
					}
					else
					{
						name = "" + oi.getDateIssued().getYear();
					}
				}
				if (oi.getDateIssuedTimespan() != null && oi.getDateIssuedTimespan().getDateFrom() != null
						&& oi.getDateIssuedTimespan().getDateFrom().getYear() > MIN_YEAR
						&& oi.getDateIssuedTimespan().getDateTo() != null
						&& oi.getDateIssuedTimespan().getDateTo().getYear() > MIN_YEAR)
				{
					if (name.trim().length() > 0)
					{
						name = name + ", " + oi.getDateIssuedTimespan().getDateFrom().getYear() + "-"
								+ oi.getDateIssuedTimespan().getDateTo().getYear();
					}
					else
					{
						name = "" + oi.getDateIssuedTimespan().getDateFrom().getYear() + "-"
								+ oi.getDateIssuedTimespan().getDateTo().getYear();
					}
				}
				if (oi.getCopyrightDate() != null && oi.getCopyrightDate().getYear() > MIN_YEAR)
				{
					if (name.trim().length() > 0)
					{
						name = name + ", " + oi.getCopyrightDate().getYear();
					}
					else
					{
						name = "" + oi.getCopyrightDate().getYear();
					}
				}
				else if (oi.getCopyrightDateTimespan() != null && oi.getCopyrightDateTimespan().getDateFrom() != null
						&& oi.getCopyrightDateTimespan().getDateTo() != null)
				{
					if (name.trim().length() > 0)
					{
						name = name + ", " + oi.getCopyrightDateTimespan().getDateFrom().getYear() + "-"
								+ oi.getCopyrightDateTimespan().getDateTo().getYear();
					}
					else
					{
						name = "" + oi.getCopyrightDateTimespan().getDateFrom().getYear() + "-"
								+ oi.getCopyrightDateTimespan().getDateTo().getYear();
					}
				}
				else if (oi.getDateCreated() != null && oi.getDateCreated().getYear() > MIN_YEAR)
				{
					if (name.trim().length() > 0)
					{
						name = name + ", " + oi.getDateCreated().getYear();
					}
					else
					{
						name = "" + oi.getDateCreated().getYear();
					}
				}
				else if (oi.getDateCreatedTimespan() != null && oi.getDateCreatedTimespan().getDateFrom() != null
						&& oi.getDateCreatedTimespan().getDateFrom().getYear() > MIN_YEAR
						&& oi.getDateCreatedTimespan().getDateTo() != null
						&& oi.getDateCreatedTimespan().getDateTo().getYear() > MIN_YEAR)
				{
					if (name.trim().length() > 0)
					{
						name = name + ", " + oi.getDateCreatedTimespan().getDateFrom().getYear() + "-"
								+ oi.getDateCreatedTimespan().getDateTo().getYear();
					}
					else
					{
						name = "" + oi.getDateCreatedTimespan().getDateFrom().getYear() + "-"
								+ oi.getDateCreatedTimespan().getDateTo().getYear();
					}
				}
				else if (oi.getDateCaptured() != null && oi.getDateCaptured().getYear() > MIN_YEAR)
				{
					if (name.trim().length() > 0)
					{
						name = name + ", " + oi.getDateCaptured().getYear();
					}
					else
					{
						name = "" + oi.getDateCaptured().getYear();
					}
				}
				else if (oi.getDateCapturedTimespan() != null && oi.getDateCapturedTimespan().getDateFrom() != null
						&& oi.getDateCapturedTimespan().getDateFrom().getYear() > MIN_YEAR
						&& oi.getDateCapturedTimespan().getDateTo() != null
						&& oi.getDateCapturedTimespan().getDateTo().getYear() > MIN_YEAR)
				{
					if (name.trim().length() > 0)
					{
						name = name + ", " + oi.getDateCapturedTimespan().getDateFrom().getYear() + "-"
								+ oi.getDateCapturedTimespan().getDateTo().getYear();
					}
					else
					{
						name = "" + oi.getDateCapturedTimespan().getDateFrom().getYear() + "-"
								+ oi.getDateCapturedTimespan().getDateTo().getYear();
					}
				}
				// if (originInfo.getDateCreated() != null &&
				// originInfo.getDateCreated().toString().trim().length() > 0)
				// {
				// name += ", " + originInfo.getDateCreated().toString().trim();
				// }
				// if (originInfo.getCopyrightDate() != null &&
				// originInfo.getCopyrightDate().toString().trim().length() > 0)
				// {
				// name += ", " +
				// originInfo.getCopyrightDate().toString().trim();
				// }
			}
			name += "\n";
			if (note != null && note.getNote() != null && note.getNote().trim().length() > 0)
			{
				name += "\n[Note: " + note.getNote().trim() + "]";
			}
			if (location != null)
			{
				if (location.getPhysicalLocation() != null && location.getPhysicalLocation().trim().length() > 0)
				{
					name += "\nLocation: " + location.getPhysicalLocation().trim();
				}
				if (location.getShelfLocator() != null && location.getShelfLocator().trim().length() > 0)
				{
					name += "\nShelf: " + location.getShelfLocator().trim();
				}
			}
			// name += "\nID: " + reference.getPdrId().toString();
			if (reference.getRelatedItems() != null && reference.getRelatedItems().size() > 0)
			{
				String id = reference.getRelatedItems().firstElement().getId();
				if (id != null)
				{
					ReferenceMods host = _facade.getReference(new PdrId(id));
					if (host != null)
					{
						if (host.getDisplayNameLong() != null)
						{
							name += "\nin: " + host.getDisplayNameLong();
						}
						else
						{
							name += "\nin: " + host.getDisplayName();
						}
					}
				}
			}
			reference.setDisplayNameLong(name);

		}
	}

	/**
	 * process hosted name.
	 * @param host host.
	 * @param hosted hosted reference.
	 * @param recursionLevel recursion level.
	 */
	private void processHostedRefDisplayName(final ReferenceMods host, final ReferenceMods hosted,
			final int recursionLevel)
	{
		if (host != null && host.getDisplayName().startsWith("pdrRo") && recursionLevel <= 2)
		{
			processDisplayName(host);
		}
		if (host != null && host.getDisplayName() != null)
		{
			if (recursionLevel == 0 && !hosted.getDisplayName().contains("in: ")
					&& !hosted.getDisplayName().contains(host.getDisplayName()))
			{
				hosted.setDisplayName(new String(hosted.getDisplayName() + ", in: " + host.getDisplayName()));
				hosted.setDisplayNameLong(new String(hosted.getDisplayNameLong() + ", in: " + host.getDisplayName()));
			}
			else if (recursionLevel <= 2 && !hosted.getDisplayName().contains(host.getDisplayName()))
			{
				hosted.setDisplayName(new String(hosted.getDisplayName() + ", " + host.getDisplayName())); //$NON-NLS-1$
				hosted.setDisplayNameLong(new String(hosted.getDisplayNameLong() + ", " + host.getDisplayName())); //$NON-NLS-1$
			}
			// System.out.println("host " + host.getDisplayName());
			// System.out.println("hosted " + hosted.getDisplayName());
			if (recursionLevel <= 2 && host.getRelatedItems() != null && !host.getRelatedItems().isEmpty()
					&& host.getRelatedItems().firstElement().getId() != null)
			{
				ReferenceMods parentHost = _facade
						.getReference(new PdrId(host.getRelatedItems().firstElement().getId()));
				processHostedRefDisplayName(parentHost, host, recursionLevel + 1);
			}
		}
		else
		{
			processDisplayName(host);
		}
	}
}
