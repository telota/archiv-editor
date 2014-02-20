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

import org.bbaw.pdr.ae.metamodel.PdrDate;

/**
 * The Class OriginInfo.
 * @author Christoph Plutte
 */
public class OriginInfo implements Cloneable
{

	/** The date created. */
	private PdrDate _dateCreated;

	/** The date created timespan. */
	private TimeSpan _dateCreatedTimespan;

	/** The date issued. */
	private PdrDate _dateIssued;

	/** The date issued timespan. */
	private TimeSpan _dateIssuedTimespan;

	/** The date captured. */
	private PdrDate _dateCaptured;

	/** The date captured timespan. */
	private TimeSpan _dateCapturedTimespan;

	/** The copyright date. */
	private PdrDate _copyrightDate;

	/** The copyright date timespan. */
	private TimeSpan _copyrightDateTimespan;

	/** The publisher. */
	private String _publisher;

	/** The place term. */
	private String _placeTerm;

	/** The place type. */
	private String _placeType;

	/** The edition. */
	private String _edition;

	/**
	 * @return cloned origin info
	 * @see java.lang.Object#clone()
	 */
	@Override
	public final OriginInfo clone()
	{
		try
		{
			OriginInfo clone = (OriginInfo) super.clone();
			if (this._copyrightDate != null)
			{
				clone._copyrightDate = this._copyrightDate.clone();
			}
			if (this._dateCaptured != null)
			{
				clone._dateCaptured = this._dateCaptured.clone();
			}
			if (this._dateCreated != null)
			{
				clone._dateCreated = this._dateCreated.clone();
			}
			if (this._dateIssued != null)
			{
				clone._dateIssued = this._dateIssued.clone();
			}
			if (this._copyrightDateTimespan != null)
			{
				clone._copyrightDateTimespan = this._copyrightDateTimespan.clone();
			}
			if (this._dateCapturedTimespan != null)
			{
				clone._dateCapturedTimespan = this._dateCapturedTimespan.clone();
			}
			if (this._dateCreatedTimespan != null)
			{
				clone._dateCreatedTimespan = this._dateCreatedTimespan.clone();
			}
			if (this._dateIssuedTimespan != null)
			{
				clone._dateIssuedTimespan = this._dateIssuedTimespan.clone();
			}

			if (this._edition != null)
			{
				clone._edition = new String(this._edition);
			}
			if (this._placeTerm != null)
			{
				clone._placeTerm = new String(this._placeTerm);
			}
			if (this._placeType != null)
			{
				clone._placeType = new String(this._placeType);
			}
			if (this._publisher != null)
			{
				clone._publisher = new String(this._publisher);
			}

			return clone;
		}
		catch (CloneNotSupportedException e)
		{
			throw new InternalError();
		}
	}

	/**
	 * Gets the copyright date.
	 * @return the copyright date
	 */
	public final PdrDate getCopyrightDate()
	{
		return _copyrightDate;
	}

	/**
	 * Gets the copyright date timespan.
	 * @return the copyright date timespan
	 */
	public final TimeSpan getCopyrightDateTimespan()
	{
		return _copyrightDateTimespan;
	}

	/**
	 * Gets the date captured.
	 * @return the date captured
	 */
	public final PdrDate getDateCaptured()
	{
		return _dateCaptured;
	}

	/**
	 * Gets the date captured timespan.
	 * @return the date captured timespan
	 */
	public final TimeSpan getDateCapturedTimespan()
	{
		return _dateCapturedTimespan;
	}

	/**
	 * Gets the date created.
	 * @return the date created
	 */
	public final PdrDate getDateCreated()
	{
		return _dateCreated;
	}

	/**
	 * Gets the date created timespan.
	 * @return the date created timespan
	 */
	public final TimeSpan getDateCreatedTimespan()
	{
		return _dateCreatedTimespan;
	}

	/**
	 * Gets the date issued.
	 * @return the date issued
	 */
	public final PdrDate getDateIssued()
	{
		return _dateIssued;
	}

	/**
	 * Gets the date issued timespan.
	 * @return the date issued timespan
	 */
	public final TimeSpan getDateIssuedTimespan()
	{
		return _dateIssuedTimespan;
	}

	/**
	 * Gets the edition.
	 * @return the edition
	 */
	public final String getEdition()
	{
		return _edition;
	}

	/**
	 * Gets the place term.
	 * @return the place term
	 */
	public final String getPlaceTerm()
	{
		return _placeTerm;
	}

	/**
	 * Gets the place type.
	 * @return the place type
	 */
	public final String getPlaceType()
	{
		return _placeType;
	}

	/**
	 * Gets the publisher.
	 * @return the publisher
	 */
	public final String getPublisher()
	{
		return _publisher;
	}

	/**
	 * Sets the copyright date.
	 * @param copyrightDate the new copyright date
	 */
	public final void setCopyrightDate(final PdrDate copyrightDate)
	{
		this._copyrightDate = copyrightDate;
	}

	/**
	 * Sets the copyright date timespan.
	 * @param copyrightDateTimespan the new copyright date timespan
	 */
	public final void setCopyrightDateTimespan(final TimeSpan copyrightDateTimespan)
	{
		this._copyrightDateTimespan = copyrightDateTimespan;
	}

	/**
	 * Sets the date captured.
	 * @param dateCaptured the new date captured
	 */
	public final void setDateCaptured(final PdrDate dateCaptured)
	{
		this._dateCaptured = dateCaptured;
	}

	/**
	 * Sets the date captured timespan.
	 * @param dateCapturedTimespan the new date captured timespan
	 */
	public final void setDateCapturedTimespan(final TimeSpan dateCapturedTimespan)
	{
		this._dateCapturedTimespan = dateCapturedTimespan;
	}

	/**
	 * Sets the date created.
	 * @param dateCreated the new date created
	 */
	public final void setDateCreated(final PdrDate dateCreated)
	{
		this._dateCreated = dateCreated;
	}

	/**
	 * Sets the date created timespan.
	 * @param dateCreatedTimespan the new date created timespan
	 */
	public final void setDateCreatedTimespan(final TimeSpan dateCreatedTimespan)
	{
		this._dateCreatedTimespan = dateCreatedTimespan;
	}

	/**
	 * Sets the date issued.
	 * @param dateIssued the new date issued
	 */
	public final void setDateIssued(final PdrDate dateIssued)
	{
		this._dateIssued = dateIssued;
	}

	/**
	 * Sets the date issued timespan.
	 * @param dateIssuedTimespan the new date issued timespan
	 */
	public final void setDateIssuedTimespan(final TimeSpan dateIssuedTimespan)
	{
		this._dateIssuedTimespan = dateIssuedTimespan;
	}

	/**
	 * Sets the edition.
	 * @param edition the new edition
	 */
	public final void setEdition(final String edition)
	{
		this._edition = edition;
	}

	/**
	 * Sets the place term.
	 * @param placeTerm the new place term
	 */
	public final void setPlaceTerm(final String placeTerm)
	{
		this._placeTerm = placeTerm;
	}

	/**
	 * Sets the place type.
	 * @param placeType the new place type
	 */
	public final void setPlaceType(final String placeType)
	{
		this._placeType = placeType;
	}

	/**
	 * Sets the publisher.
	 * @param publisher the new publisher
	 */
	public final void setPublisher(final String publisher)
	{
		this._publisher = publisher;
	}
}
