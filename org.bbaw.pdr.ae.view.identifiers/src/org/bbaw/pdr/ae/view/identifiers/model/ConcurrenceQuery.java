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

import org.bbaw.pdr.ae.metamodel.PdrDate;

public class ConcurrenceQuery
{
	private String normName;

	private Vector<String> otherNames;

	private PdrDate dateOfBirth;

	private String placeOfBirth;

	private PdrDate dateOfDeath;

	private String placeOfDeath;

	private String description;

	private String gender;

	private String yearOfActitiy;

	private String countryOfActitiy;

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

	public void addOtherNames(String otherName)
	{
		if (this.otherNames == null)
		{
			this.otherNames = new Vector<String>();
		}
		if (otherName != null)
		{
			this.otherNames.add(otherName);
		}
	}

	public PdrDate getDateOfBirth()
	{
		return dateOfBirth;
	}

	public void setDateOfBirth(PdrDate dateOfBirth)
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

	public PdrDate getDateOfDeath()
	{
		return dateOfDeath;
	}

	public void setDateOfDeath(PdrDate dateOfDeath)
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

	public String getYearOfActitiy()
	{
		return yearOfActitiy;
	}

	public void setYearOfActitiy(String yearOfActitiy)
	{
		this.yearOfActitiy = yearOfActitiy;
	}

	public String getCountryOfActitiy()
	{
		return countryOfActitiy;
	}

	public void setCountryOfActitiy(String countryOfActitiy)
	{
		this.countryOfActitiy = countryOfActitiy;
	}

}
