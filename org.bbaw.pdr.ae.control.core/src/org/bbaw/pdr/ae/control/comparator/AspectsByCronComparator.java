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
package org.bbaw.pdr.ae.control.comparator;

import java.util.Comparator;
import java.util.TreeSet;

import org.bbaw.pdr.ae.metamodel.PdrDate;
import org.bbaw.pdr.ae.model.Aspect;
import org.bbaw.pdr.ae.model.TaggingRange;
import org.bbaw.pdr.ae.model.Time;
import org.bbaw.pdr.ae.model.TimeStm;

/**
 * comparator for aspects by chronological order.
 * <p>If comparator is set to ascending order, two aspects are put in order
 * by comparing their earliest temporal informations. In descending mode,
 * this comparator only cares about an aspect's latest temporal information.</p> 
 * @author Christoph Plutte, Jakob Höper
 */
public class AspectsByCronComparator implements Comparator<Aspect>
{

	/** ascending flag. */
	private boolean _ascending;

	/**
	 * constructor defaulting to ascending order.
	 */
	public AspectsByCronComparator()
	{
		this._ascending = true;
	}

	/**
	 * constructor with ascending/descending flag
	 * @param ascending If set true, comparator will sort in ascending order.
	 */
	public AspectsByCronComparator(final boolean ascending)
	{
		this._ascending = ascending;
	}

	@Override
	public final int compare(final Aspect a1, final Aspect a2)
	{
		int diff = 0;
		PdrDate d1 = null;
		PdrDate d2 = null;
		
		if (this._ascending) {
			d1 = getEarliest(a1);
			d2 = getEarliest(a2);
		} else {
			d1 = getLatest(a1);
			d2 = getLatest(a2);
		}
		
		/*if (a1.getTimeDim() != null && a1.getTimeDim().getTimeStms() != null)
		{
			for (int i = 0; i < a1.getTimeDim().getTimeStms().size(); i++)
			{
				if (a1.getTimeDim().getTimeStms().get(i).getTimes() != null)
				{
					for (int j = 0; j < a1.getTimeDim().getTimeStms().get(i).getTimes().size(); j++)
					{
						if (a1.getTimeDim().getTimeStms().get(i).getTimes().get(j) != null
								&& a1.getTimeDim().getTimeStms().get(i).getTimes().get(j).getTimeStamp() != null)
						{
							d1 = a1.getTimeDim().getTimeStms().get(i).getTimes().get(j).getTimeStamp();
							break;
						}
					}
					if (d1 != null)
					{
						break;
					}
				}
			}
			if (a2.getTimeDim() != null && a2.getTimeDim().getTimeStms() != null)
			{
				for (int i = 0; i < a2.getTimeDim().getTimeStms().size(); i++)
				{
					if (a2.getTimeDim().getTimeStms().get(i).getTimes() != null)
					{
						for (int j = 0; j < a2.getTimeDim().getTimeStms().get(i).getTimes().size(); j++)
						{
							if (a2.getTimeDim().getTimeStms().get(i).getTimes().get(j) != null
									&& a2.getTimeDim().getTimeStms().get(i).getTimes().get(j).getTimeStamp() != null)
							{
								d2 = a2.getTimeDim().getTimeStms().get(i).getTimes().get(j).getTimeStamp();
								if (d1 != null)
								{
									return d1.compare(d2) * (this._ascending ? 1 : -1);
								}
							}
						}
					}
				}
			}

		}
		// FIXME: die hinterste taggingrange überschreibt alle vorangehenden. nicht optimal.
		if (d2 == null)
		{
			if (a2.getRangeList() != null && a2.getRangeList().size() > 0)
			{
				for (TaggingRange tr : a2.getRangeList())
				{
					if (tr.getWhen() != null)
					{
						d2 = tr.getWhen();
						break;
					}
					if (tr.getNotBefore() != null)
					{
						d2 = tr.getNotBefore();
						break;
					}
					if (tr.getFrom() != null)
					{
						d2 = tr.getFrom();
						break;
					}
					if (tr.getNotAfter() != null)
					{
						d2 = tr.getNotAfter();
						break;
					}
					if (tr.getTo() != null)
					{
						d2 = tr.getTo();
						break;
					}
				}
			}
		}
		if (d1 == null)
		{
			if (a1.getRangeList() != null && a1.getRangeList().size() > 0)
			{
				for (TaggingRange tr : a1.getRangeList())
				{
					if (tr.getWhen() != null)
					{
						d1 = tr.getWhen();
						break;
					}
					if (tr.getNotBefore() != null)
					{
						d1 = tr.getNotBefore();
						break;
					}
					if (tr.getFrom() != null)
					{
						d1 = tr.getFrom();
						break;
					}
					if (tr.getNotAfter() != null)
					{
						d1 = tr.getNotAfter();
						break;
					}
					if (tr.getTo() != null)
					{
						d1 = tr.getTo();
						break;
					}
				}
			}
		}*/
		
		if (d1 != null && d2 != null) {
			diff = d1.compare(d2);
		} else if (d1 != null) {
			diff = d1.getValue(); //-1;
		} else if (d2 != null) {
			diff = -d2.getValue(); //1;
		} 
		
		return diff * (_ascending ? 1 : -1);
	}
	
	/**
	 * Returns whatever time is the earliest or latest that the given {@link Aspect} is about.
	 * If the aspect has {@link TimeStm} information, it is preferred over temporal
	 * information that might be extractable from the aspect's {@link TaggingRange}s.
	 * @param a
	 * @param earliest pass true if earliest date is desired, false if latest
	 * @return earliest or latest {@link PdrDate} mentioned, or null if no temporal information.
	 */
	private static PdrDate getEarliestOrLatest(Aspect a, boolean earliest) {
		TreeSet<PdrDate> dates = new TreeSet<PdrDate>();
		if (a.getTimeDim() != null && a.getTimeDim().getTimeStms() != null) 
			for (TimeStm stm : a.getTimeDim().getTimeStms())
				if (stm.getTimes() != null)
					for (Time t : stm.getTimes())
						if (t.getTimeStamp() != null)
							dates.add(t.getTimeStamp());
		if (dates.size() > 0) return earliest ? dates.first() : dates.last();
		// if no time statements in metadata, use markup data of notification text
		if (a.getRangeList() != null)
			for (TaggingRange tag : a.getRangeList()) {
				PdrDate date = getEarliest(tag);
				if (date != null)
					dates.add(date);
			}
		if (dates.size() > 0) return earliest ? dates.first() : dates.last();
		return null;
	}

	/**
	 * Returns whatever time is the earliest that the given {@link Aspect} is about.
	 * If the aspect has {@link TimeStm} information, it is preferred over temporal
	 * information that might be extractable from the aspect's {@link TaggingRange}s.
	 * @param a
	 * @return earliest {@link PdrDate} mentioned, or null if no temporal information.
	 */
	public static PdrDate getEarliest(Aspect a) {
		return getEarliestOrLatest(a, true);
	}

	/**
	 * Returns whatever time is the latest that the given {@link Aspect} is about.
	 * If the aspect has {@link TimeStm} information, it is preferred over temporal
	 * information that might be extractable from the aspect's {@link TaggingRange}s.
	 * @param a
	 * @return latest {@link PdrDate} mentioned, or null if no temporal information.
	 */
	public static PdrDate getLatest(Aspect a) {
		return getEarliestOrLatest(a, false);
	}
	
	/**
	 * Returns the earliest temporal meta-information encoded in a given
	 * {@link TaggingRange} as a {@link PdrDate}.
	 * @param tag {@link TaggingRange} object to look at. 
	 * @return {@link PdrDate} storing the earliest time the given tag is about, or null if no
	 * temporal information is available.
	 * @see TaggingRange
	 * @see #getLatest(TaggingRange)
	 */
	public static PdrDate getEarliest(TaggingRange tag) {
		TreeSet<PdrDate> dates = getTagDates(tag);
		for (PdrDate date : dates)
			if (date != null) return date;
		return null;
	}

	/**
	 * Returns the latest temporal meta-information encoded in a given
	 * {@link TaggingRange} as a {@link PdrDate}.
	 * @param tag {@link TaggingRange} object to look at. 
	 * @return {@link PdrDate} storing the latest time the given tag is about, or null if no
	 * temporal information is available.
	 * @see TaggingRange
	 * @see #getEarliest(TaggingRange)
	 */
	public static PdrDate getLatest(TaggingRange tag) {
		TreeSet<PdrDate> dates = getTagDates(tag);
		PdrDate date = dates.pollLast();
		while (date == null)
			date = dates.pollLast();
		return date;
	}


	
	/**
	 * Returns all {@link PdrDate}-encoded temporal information held by the
	 * given {@link TaggingRange} in chronological order.
	 * @param tag
	 * @return
	 */
	private static TreeSet<PdrDate> getTagDates(TaggingRange tag) {
		TreeSet<PdrDate> dates = new TreeSet<PdrDate>();
		if (tag.getWhen() != null) dates.add(tag.getWhen());
		if (tag.getNotAfter() != null) dates.add(tag.getNotAfter());
		if (tag.getFrom() != null) dates.add(tag.getFrom());
		if (tag.getNotAfter() != null) dates.add(tag.getNotAfter());
		if (tag.getTo() != null) dates.add(tag.getTo());
		return dates;
	}

}
