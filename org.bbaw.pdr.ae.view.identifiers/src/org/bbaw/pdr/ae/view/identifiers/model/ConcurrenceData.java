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
package org.bbaw.pdr.ae.view.identifiers.model;

import java.util.Vector;

public class ConcurrenceData implements Comparable<ConcurrenceData>
{
	private String normName;

	private Vector<String> otherNames;

	private String dateOfBirth;

	private String placeOfBirth;

	private String dateOfDeath;

	private String placeOfDeath;

	private String description;

	private String gender;

	private String referenceURL;

	private Vector<ConcurrenceIdentifier> identifiers;

	private int score;

	private int maxQueryScore;

	private String service;

	public String getNormName()
	{
		return normName;
	}

	public void setNormName(String normName)
	{
		this.normName = normName;
	}

	public Vector<String> getOtherNames()
	{
		return otherNames;
	}

	public void setOtherNames(Vector<String> otherNames)
	{
		this.otherNames = otherNames;
	}

	public void addOtherName(String otherName)
	{
		if (this.otherNames == null)
		{
			this.otherNames = new Vector<String>();
		}
		this.otherNames.add(otherName);
	}

	public String getDateOfBirth()
	{
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth)
	{
		this.dateOfBirth = dateOfBirth;
	}

	public String getPlaceOfBirth()
	{
		return placeOfBirth;
	}

	public void setPlaceOfBirth(String placeOfBirth)
	{
		this.placeOfBirth = placeOfBirth;
	}

	public String getDateOfDeath()
	{
		return dateOfDeath;
	}

	public void setDateOfDeath(String dateOfDeath)
	{
		this.dateOfDeath = dateOfDeath;
	}

	public String getPlaceOfDeath()
	{
		return placeOfDeath;
	}

	public void setPlaceOfDeath(String placeOfDeath)
	{
		this.placeOfDeath = placeOfDeath;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getGender()
	{
		return gender;
	}

	public void setGender(String gender)
	{
		this.gender = gender;
	}

	public Vector<ConcurrenceIdentifier> getIdentifiers()
	{
		return identifiers;
	}

	public void setIdentifiers(Vector<ConcurrenceIdentifier> identifiers)
	{
		this.identifiers = identifiers;
	}

	public void addIdentifier(ConcurrenceIdentifier identifier)
	{
		if (this.identifiers == null)
		{
			this.identifiers = new Vector<ConcurrenceIdentifier>();
		}
		if (identifier != null)
		{
			this.identifiers.add(identifier);
		}
	}

	public int getScore()
	{
		return score;
	}

	public void setScore(int score)
	{
		this.score = score;
	}

	public int getMaxQueryScore()
	{
		return maxQueryScore;
	}

	public void setMaxQueryScore(int maxQueryScore)
	{
		this.maxQueryScore = maxQueryScore;
	}

	public String getReferenceURL()
	{
		return referenceURL;
	}

	public void setReferenceURL(String referenceURL)
	{
		this.referenceURL = referenceURL;
	}

	@Override
	public int compareTo(ConcurrenceData o)
	{
		return o.score - this.score;
	}

	public String getService()
	{
		return service;
	}

	public void setService(String service)
	{
		this.service = service;
	}

}
