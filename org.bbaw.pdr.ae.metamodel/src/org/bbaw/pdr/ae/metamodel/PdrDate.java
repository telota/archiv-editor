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
package org.bbaw.pdr.ae.metamodel;

/**
 * <h1>The Class PdrDate.</h1>
 * <p><b>Note: this class has a natural ordering that is inconsistent with equals.</b></p>
 * @author Christoph Plutte
 */
public class PdrDate implements Cloneable, Comparable<PdrDate>
{

	/** The year. */
	private int _year;

	/** The month. */
	private int _month;

	/** The day. */
	private int _day;
	
	/** days since 0000-00-00 */
	private int _value;
	
	/** number of days per month */
	private static final int[] daysAsOfMonth = {0,31,59,90,120,151,181,212,243,273,304,334};

	
	/**
	 * Computes a linear value for a given year, month, and day.
	 * @param year 1871
	 * @param month 10
	 * @param day 18
	 * @return linear value of the date represented by parameters. In days.  
	 */
	public static int calcDayId(final int year, final int month, final int day) {
		int res = year * 365 + (year/4); // schaltjahre!
		if (month>0)
			res += daysAsOfMonth[month-1];
		res += day;
		return res;
	}
	
	/**
	 * Instantiates a new pdr date.
	 * @param year the year
	 * @param month the month
	 * @param day the day
	 */
	public PdrDate(final int year, final int month, final int day)
	{
		this._year = year;
		this._month = month;
		this._day = day;
		this._value = calcDayId(year, month, day);
	}

	/**
	 * constructor for PdrDate.
	 * @param date date as String.
	 */
	public PdrDate(final String date)
	{
		if (date == null)
		{
			this._year = 0;
			this._month = 0;
			this._day = 0;
		}
		else
		{
			if (date.length() == 10)
			{
				try{
				this.setYear(Integer.parseInt(date.substring(0, 4)));
				}
				catch (NumberFormatException e)
				{
					this.setYear(0);
				}
				try{
				this.setMonth(Integer.parseInt(date.substring(5, 7)));
				}
				catch (NumberFormatException e)
				{
					this.setMonth(0);
				}
				try{
				this.setDay(Integer.parseInt(date.substring(8, 10)));
				}
				catch (NumberFormatException e)
				{
					this.setDay(0);
				}
			}
			else if (date.length() == 7)
			{
				try{
				this.setYear(Integer.parseInt(date.substring(0, 4)));
				}
				catch (NumberFormatException e)
				{
					this.setYear(0);
				}
				try{
				this.setMonth(Integer.parseInt(date.substring(5, 7)));
				}
				catch (NumberFormatException e)
				{
					this.setMonth(0);
				}
				this._day = 0;
			}
			else if (date.length() == 4)
			{
				try{
				this.setYear(Integer.parseInt(date.substring(0, 4)));
				}
				catch (NumberFormatException e)
				{
					this.setYear(0);
				}
				this._month = 0;
				this._day = 0;
	
			}
			else if (date.length() > 1)
			{
				if (date.matches("[-+]?\\d+(\\.\\d+)?"))
				{
					this._year = Integer.parseInt(date);
				}
				else
				{
					this._year = 0;
				}
				this._month = 0;
				this._day = 0;
			}
			else
			{
				this._year = 0;
				this._month = 0;
				this._day = 0;
			}
		}
		this._value = calcDayId(_year, _month, _day);
	}

	@Override
	public final PdrDate clone()
	{
		try
		{
			PdrDate clone = (PdrDate) super.clone();
			clone._year = this._year;
			clone._month = this._month;
			clone._day = this._day;
			clone._value = this._value;
			return clone;
		}
		catch (CloneNotSupportedException e)
		{
			throw new InternalError();
		}
	}

	/**
	 * Compare this {@link PdrDate} instance to the one passed as d2. 
	 * The returning integer value approximates how many days passed between the two.
	 * A negative return value indicates that the calling instance represents a
	 * date earlier than the parameter's. 
	 * @param d2 {@link PdrDate} instance to compare to
	 * @return approximate number of days between those two dates. Negative result
	 * when calling instance was earliest.  
	 */
	public final int compare(final PdrDate d2)
	{
		/*if (this._year - d2._year != 0)
		{
			return (this._year - d2._year);
		}
		else if (this._month - d2._month != 0)
		{
			return (this._month - d2._month);
		}
		else if (this._day - d2._day != 0)
		{
			return (this._day - d2._day);
		}
		else
		{
			return 0;
		}*/
		/*return (this._year - d2._year)*365+
				(this._month - d2._month)*30+
				(this._day - d2._day);*/
		return this._value - d2._value;
	}

	@Override
	public final boolean equals(final Object obj)
	{
		if (obj instanceof PdrDate)
		{
			PdrDate date = (PdrDate) obj;
			if (this._year == date._year && this._month == date._month && this._day == date._day)
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Gets the day.
	 * @return the day
	 */
	public final int getDay()
	{
		return _day;
	}

	/**
	 * Gets the month.
	 * @return the month
	 */
	public final int getMonth()
	{
		return _month;
	}

	/**
	 * Gets the year.
	 * @return the year
	 */
	public final int getYear()
	{
		return _year;
	}
	
	/**
	 * Returns a linear value of this date in days.
	 */
	public int getValue() {
		return _value;
	}
	

	@Override
	public final int hashCode()
	{
		return super.hashCode();
	}

	/**
	 * Checks if is valid. Validity is constituted by a date between 999-00-00 and 9999-12-31.
	 * @return true, if date is valid
	 */
	public final boolean isValid()
	{
		if (_year > 999 && _year < 10000)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * Sets the day.
	 * @param day the new day
	 */
	public final void setDay(final int day)
	{
		this._day = day;
		this._value = calcDayId(_year, _month, _day);
	}

	/**
	 * Sets the month.
	 * @param month the new month
	 */
	public final void setMonth(final int month)
	{
		this._month = month;
		this._value = calcDayId(_year, _month, _day);
	}

	/**
	 * Sets the year.
	 * @param year the new year
	 */
	public final void setYear(final int year)
	{
		this._year = year;
		this._value = calcDayId(_year, _month, _day);
	}

	/**
	 * returns the date given by params as String.
	 * @return date as yyyy-mm-dd if mm not 0 and dd not 0, else yyyy-mm or yyy.
	 */
	@Override
	public final String toString()
	{
		String date = String.format("%04d", _year);
		if (_month > 0)
		{
			date += "-" + String.format("%02d", _month);
		}
		if (_day > 0)
		{
			date += "-" + String.format("%02d", _day);
		}
		return date;
	}

	/**
	 * returns the date given by params as String.
	 * @param year year.
	 * @param month month.
	 * @param day day.
	 * @return date String as yyyy-mm-dd.
	 */
	public final String toString(final int year, final int month, final int day)
	{

		String date = String.format("%04d", year) + "-" + String.format("%02d", month) + "-"
				+ String.format("%02d", day);
		return date;
	}

	@Override
	public int compareTo(PdrDate arg0) {
		/*if (arg0._value == this._value) return 0;
		return arg0._value > this._value ? 1 : -1;*/
		return this.compare(arg0);
	}

	public String toString(String separator) {

		String date = String.format("%04d", _year);
		if (_month > 0)
		{
			date += separator + String.format("%02d", _month);
		}
		if (_day > 0)
		{
			date += separator + String.format("%02d", _day);
		}
		return date;
	}
}
