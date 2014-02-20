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
package org.bbaw.pdr.ae.model;

import java.util.Collections;
import java.util.Vector;

import org.bbaw.pdr.ae.metamodel.PdrId;

/**
 * The Class ReferenceMods.
 * @author Christoph Plutte
 */
public class ReferenceMods extends PdrObject implements Cloneable
{

	/** The title info. */
	private TitleInfo _titleInfo;

	/** The series title info. */
	private TitleInfo _seriesTitleInfo;

	/** The name mods. */
	private Vector<NameMods> _nameMods;

	/** The genre. */
	private Genre _genre;

	/** The origin info. */
	private OriginInfo _originInfo;

	/** The note. */
	private Note _note;

	/** The identifiers mods. */
	private Vector<IdentifierMods> _identifiersMods;

	/** The location. */
	private LocationMods _location;

	/** The access condition. */
	private AccessCondition _accessCondition;

	/** The related items. */
	private Vector<RelatedItem> _relatedItems;

	/** The display name long. */
	private String _displayNameLong;

	/** The hosted references. */
	private Vector<String> _hostedReferences = new Vector<String>(4);

	/**
	 * Constructor for creating new reference templates.
	 * @param genre Genre of reference template.
	 */
	public ReferenceMods(final Genre genre)
	{
		super("genre.000000.000000000");
		this._genre = genre;
	}

	/**
	 * Instantiates a new reference mods.
	 * @param pdrId the pdr id
	 */
	public ReferenceMods(final PdrId pdrId)
	{
		super(pdrId);
	}

	/**
	 * Instantiates a new reference mods.
	 * @param idString the id string
	 */
	public ReferenceMods(final String idString)
	{
		super(idString);
	}

	/**
	 * @return cloned referenceMods
	 * @see org.bbaw.pdr.ae.model.PdrObject#clone()
	 */
	@Override
	public final ReferenceMods clone()
	{
		ReferenceMods clone = (ReferenceMods) super.clone();
		if (this._accessCondition != null)
		{
			clone._accessCondition = this._accessCondition.clone();
		}
		if (this._genre != null)
		{
			clone._genre = this._genre.clone();
		}
		if (this._identifiersMods != null)
		{
			clone._identifiersMods = new Vector<IdentifierMods>(this._identifiersMods.size());
			for (int i = 0; i < this._identifiersMods.size(); i++)
			{
				clone._identifiersMods.add(this._identifiersMods.get(i).clone());
			}
		}
		if (this._location != null)
		{
			clone._location = this._location.clone();
		}
		if (this._nameMods != null)
		{
			clone._nameMods = new Vector<NameMods>(this._nameMods.size());
			for (int i = 0; i < this._nameMods.size(); i++)
			{
				clone._nameMods.add(this._nameMods.get(i).clone());
			}
		}
		if (this._note != null)
		{
			clone._note = this._note.clone();
		}
		if (this._originInfo != null)
		{
			clone._originInfo = this._originInfo.clone();
		}
		if (this._seriesTitleInfo != null)
		{
			clone._seriesTitleInfo = this._seriesTitleInfo.clone();
		}
		if (this._titleInfo != null)
		{
			clone._titleInfo = this._titleInfo.clone();
		}
		if (this._displayNameLong != null)
		{
			clone._displayNameLong = new String(this._displayNameLong);
		}
		if (this._hostedReferences != null)
		{
			clone._hostedReferences = new Vector<String>(this._hostedReferences.size());
			for (int i = 0; i < this._hostedReferences.size(); i++)
			{
				clone._hostedReferences.add(new String(this._hostedReferences.get(i)));
			}
		}
		if (this._relatedItems != null)
		{
			clone._relatedItems = new Vector<RelatedItem>(this._relatedItems.size());
			for (int i = 0; i < this._relatedItems.size(); i++)
			{
				clone._relatedItems.add(this._relatedItems.get(i).clone());
			}
		}
		return clone;
	}

	/**
	 * @return the accessCondition
	 */
	public final AccessCondition getAccessCondition()
	{
		return _accessCondition;
	}

	/**
	 * Gets the display name long.
	 * @return the display name long
	 */
	public final String getDisplayNameLong()
	{
		return _displayNameLong;
	}

	/**
	 * @return the genre
	 */
	public final Genre getGenre()
	{
		return _genre;
	}

	/**
	 * Gets the hosted references.
	 * @return the hosted references
	 */
	public final Vector<String> getHostedReferences()
	{
		return _hostedReferences;
	}

	/**
	 * @return the identifiersMods
	 */
	public final Vector<IdentifierMods> getIdentifiersMods()
	{
		return _identifiersMods;
	}

	/**
	 * @return the location
	 */
	public final LocationMods getLocation()
	{
		return _location;
	}

	/**
	 * @return the nameMods
	 */
	public final Vector<NameMods> getNameMods()
	{
		return _nameMods;
	}

	/**
	 * @return the note
	 */
	public final Note getNote()
	{
		return _note;
	}

	/**
	 * @return the originInfo
	 */
	public final OriginInfo getOriginInfo()
	{
		return _originInfo;
	}

	/**
	 * Gets the related items.
	 * @return the related items
	 */
	public final Vector<RelatedItem> getRelatedItems()
	{
		return _relatedItems;
	}

	/**
	 * Gets the series title info.
	 * @return the series title info
	 */
	public final TitleInfo getSeriesTitleInfo()
	{
		return _seriesTitleInfo;
	}

	/**
	 * @return the titleInfo
	 */
	public final TitleInfo getTitleInfo()
	{
		return _titleInfo;
	}

	/**
	 * Checks if is hosted.
	 * @return true, if is hosted
	 */
	public final boolean isHosted()
	{
		if (this._relatedItems == null || this._relatedItems.isEmpty())
		{
			return false;
		}
		else if (this._relatedItems.firstElement() != null
				&& this._relatedItems.firstElement().getType().equals("host")
				&& this._relatedItems.firstElement().getId() != null
				&& this._relatedItems.firstElement().getId().length() == 23)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * Checks if is valid.
	 * @return true, if is valid
	 */
	public final boolean isValid()
	{
		if (_titleInfo != null && _titleInfo.getTitle() != null && _titleInfo.getTitle().trim().length() > 0)
		{
			return true;
		}
		if (_nameMods != null && !_nameMods.isEmpty() && _nameMods.firstElement() != null
				&& _nameMods.firstElement().getNameParts() != null)
		{
			for (NamePart np : _nameMods.firstElement().getNameParts())
			{
				if (np.getNamePart() != null && np.getNamePart().trim().length() > 0)
				{
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * @param accessCondition the accessCondition to set
	 */
	public final void setAccessCondition(final AccessCondition accessCondition)
	{
		this._accessCondition = accessCondition;
	}

	/**
	 * Sets the display name long.
	 * @param displayNameLong the new display name long
	 */
	public final void setDisplayNameLong(final String displayNameLong)
	{
		this._displayNameLong = displayNameLong;
	}

	/**
	 * @param genre the genre to set
	 */
	public final void setGenre(final Genre genre)
	{
		this._genre = genre;
	}

	/**
	 * Sets the hosted references.
	 * @param hostedReferences the new hosted references
	 */
	public final void setHostedReferences(final Vector<String> hostedReferences)
	{
		this._hostedReferences = hostedReferences;
	}

	/**
	 * @param identifiersMods the identifiersMods to set
	 */
	public final void setIdentifiersMods(final Vector<IdentifierMods> identifiersMods)
	{
		this._identifiersMods = identifiersMods;
	}

	/**
	 * @param location the location to set
	 */
	public final void setLocation(final LocationMods location)
	{
		this._location = location;
	}

	/**
	 * @param nameMods the nameMods to set
	 */
	public final void setNameMods(final Vector<NameMods> nameMods)
	{
		if (nameMods != null)
		{
			Collections.sort(nameMods);
		}
		this._nameMods = nameMods;
	}

	/**
	 * @param note the note to set
	 */
	public final void setNote(final Note note)
	{
		this._note = note;
	}

	/**
	 * @param originInfo the originInfo to set
	 */
	public final void setOriginInfo(final OriginInfo originInfo)
	{
		this._originInfo = originInfo;
	}

	@Override
	public final void setPdrId(final PdrId id)
	{
		super.setPdrId(id);

	}

	/**
	 * Sets the related items.
	 * @param relatedItems the new related items
	 */
	public final void setRelatedItems(final Vector<RelatedItem> relatedItems)
	{
		this._relatedItems = relatedItems;
	}

	/**
	 * Sets the series title info.
	 * @param seriesTitleInfo the new series title info
	 */
	public final void setSeriesTitleInfo(final TitleInfo seriesTitleInfo)
	{
		this._seriesTitleInfo = seriesTitleInfo;
	}

	/**
	 * @param titleInfo the titleInfo to set
	 */
	public final void setTitleInfo(final TitleInfo titleInfo)
	{
		this._titleInfo = titleInfo;
	}
}
