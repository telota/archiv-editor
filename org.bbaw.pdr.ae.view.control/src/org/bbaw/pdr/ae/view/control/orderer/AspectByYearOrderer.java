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
package org.bbaw.pdr.ae.view.control.orderer;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;

import org.bbaw.pdr.ae.common.NLMessages;
import org.bbaw.pdr.ae.common.icons.IconsInternal;
import org.bbaw.pdr.ae.control.comparator.CronOrderComparator;
import org.bbaw.pdr.ae.metamodel.PdrId;
import org.bbaw.pdr.ae.model.Aspect;
import org.bbaw.pdr.ae.model.TaggingRange;
import org.bbaw.pdr.ae.model.Time;
import org.bbaw.pdr.ae.model.TimeStm;
import org.bbaw.pdr.ae.model.view.OrderingHead;
import org.bbaw.pdr.ae.view.control.PDRObjectsOrderer;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * The Class AspectByYearOrderer.
 * @author Christoph Plutte
 */
public class AspectByYearOrderer implements PDRObjectsOrderer
{
	/** Instance of shared image registry. */
	private HashSet<String> _times;

	/** The ordered objects. */
	private HashMap<String, OrderingHead> _orderedObjects;

	/**
	 * Instantiates a new aspect by year orderer.
	 */
	public AspectByYearOrderer()
	{
	}

	@Override
	public final Vector<OrderingHead> orderedObjects(final Vector<Aspect> aspects, IProgressMonitor monitor)
	{
		_times = new HashSet<String>();
		_orderedObjects = new HashMap<String, OrderingHead>();
		HashSet<PdrId> chosenAspects = new HashSet<PdrId>();
		HashSet<Aspect> rejectedAspets = new HashSet<Aspect>();
		Aspect a;
		OrderingHead oh = null;
		String time = null;
		TimeStm timeStm;
		Vector<Time> tVec;
		boolean timeFound = false;
		boolean added = false;
		for (int i = 0; i < aspects.size(); i++)
		{
			timeFound = false;
			added = false;
			a = aspects.get(i);
			if (a.getTimeDim() != null && !a.getTimeDim().getTimeStms().isEmpty())
			{
				for (int j = 0; j < a.getTimeDim().getTimeStms().size(); j++)
				{
					timeStm = a.getTimeDim().getTimeStms().get(j);
					if (timeStm.getTimes() != null && !timeStm.getTimes().isEmpty())
					{
						tVec = timeStm.getTimes();
						for (Time t : tVec)
						{
							if (t.getTimeStamp() != null)
							{
								timeFound = true;
								added = true;
								time = "" + t.getTimeStamp().getYear();
								if (!_times.contains(time))
								{
									_times.add(time);
									oh = new OrderingHead();
									oh.setLabel(time);
									oh.setValue(time);
									oh.setImageString(IconsInternal.TIME);
									_orderedObjects.put(time, oh);
								}
								_orderedObjects.get(time).addAspect(a);
								chosenAspects.add(a.getPdrId());
							}
						}
					}
					else
					{
						added = true;
						time = timeStm.getType();
						if (time == null)
						{
							time = "uncategorized";
						}
						String label = NLMessages.getString("Editor_timetypes_" + time);
						if (label.startsWith("!Editor_timetypes_"))
						{
							label = time;
						}
						if (!_times.contains(label))
						{
							_times.add(label);
							oh = new OrderingHead();
							oh.setLabel(label);
							oh.setValue(time);
							oh.setImageString(IconsInternal.TIME);
							_orderedObjects.put(time, oh);
						}
						oh = _orderedObjects.get(time);
						if (oh == null)
						{
							oh = _orderedObjects.get(label);
						}
						if (oh != null)
						{
							oh.addAspect(a);
							chosenAspects.add(a.getPdrId());
						}
						else
						{
							rejectedAspets.add(a);
						}
					}

				}
			}
			if (!timeFound && a.getRangeList() != null && !a.getRangeList().isEmpty())
			{
				for (TaggingRange tr : a.getRangeList())
				{
					if (tr.getName().endsWith("date"))
					{
						if (tr.getWhen() != null)
						{
							time = "" + tr.getWhen().getYear();
						}
						else if (tr.getFrom() != null)
						{
							time = "" + tr.getFrom().getYear();
						}
						else if (tr.getTo() != null)
						{
							time = "" + tr.getTo().getYear();
						}
						else if (tr.getNotBefore() != null)
						{
							time = "" + tr.getNotBefore().getYear();
						}
						else if (tr.getNotAfter() != null)
						{
							time = "" + tr.getNotAfter().getYear();
						}
						else
						{
							break;
						}

						timeFound = true;
						added = true;
						if (!_times.contains(time))
						{
							_times.add(time);
							oh = new OrderingHead();
							oh.setLabel(time);
							oh.setValue(time);
							oh.setImageString(IconsInternal.TIME);
							_orderedObjects.put(time, oh);
						}
						_orderedObjects.get(time).addAspect(a);
						chosenAspects.add(a.getPdrId());
					}
				}
			}
			if (!added)
			{
				rejectedAspets.add(a);
			}
			if (monitor != null)
			{
				monitor.worked(1);
				if (monitor.isCanceled())
				{
					break;
				}
			}
		}

		if (!rejectedAspets.isEmpty())
		{
			oh = new OrderingHead();
			oh.setLabel("uncategorized");
			oh.setValue("uncategorized");
			oh.getAspects().addAll(rejectedAspets);
			_orderedObjects.put(oh.getValue(), oh);
		}
		Vector<OrderingHead> result = new Vector<OrderingHead>(_orderedObjects.values());
		Collections.sort(result, new CronOrderComparator());
		return result;
	}
}
