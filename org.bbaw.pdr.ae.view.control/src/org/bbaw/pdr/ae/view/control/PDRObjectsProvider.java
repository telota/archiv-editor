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
package org.bbaw.pdr.ae.view.control;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Vector;

import org.bbaw.pdr.ae.common.NLMessages;
import org.bbaw.pdr.ae.common.icons.IconsInternal;
import org.bbaw.pdr.ae.common.interfaces.AEFilter;
import org.bbaw.pdr.ae.control.comparator.AspectsByCronComparator;
import org.bbaw.pdr.ae.control.comparator.ReferenceByAuthorTitleComparator;
import org.bbaw.pdr.ae.control.facade.Facade;
import org.bbaw.pdr.ae.control.interfaces.AMainSearcher;
import org.bbaw.pdr.ae.metamodel.PdrId;
import org.bbaw.pdr.ae.model.Aspect;
import org.bbaw.pdr.ae.model.Concurrence;
import org.bbaw.pdr.ae.model.PdrObject;
import org.bbaw.pdr.ae.model.Person;
import org.bbaw.pdr.ae.model.ReferenceMods;
import org.bbaw.pdr.ae.model.Relation;
import org.bbaw.pdr.ae.model.RelationStm;
import org.bbaw.pdr.ae.model.Time;
import org.bbaw.pdr.ae.model.TimeStm;
import org.bbaw.pdr.ae.model.ValidationStm;
import org.bbaw.pdr.ae.model.view.OrderingHead;
import org.bbaw.pdr.ae.view.control.filters.AspectPersonFilter;
import org.bbaw.pdr.ae.view.control.filters.AspectReferenceFilter;
import org.bbaw.pdr.ae.view.control.filters.AspectSearchTextFilter;
import org.bbaw.pdr.ae.view.control.filters.AspectSemanticFilter;
import org.bbaw.pdr.ae.view.control.filters.AspectYearFilter;
import org.bbaw.pdr.ae.view.control.filters.PdrObjectUserFilter;
import org.bbaw.pdr.ae.view.control.orderer.AspectsBySemanticOrderer;
import org.bbaw.pdr.ae.view.control.orderer.ReferencesOrderer;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Shell;

/**
 * The Class PDRObjectsProvider.
 * @author Christoph Plutte
 */
public class PDRObjectsProvider
{
	// private final int GROUP_SIZE_MAX = 12;
	// private final int ASPECT_SIZE_DEVISOR = 10;
	/** The _facade. */
	private Facade _facade = Facade.getInstanz();

	/** The _main searcher. */
	private AMainSearcher _mainSearcher = _facade.getMainSearcher();

	/** The input. */
	private Object _input;

	/** The orderer. */
	private PDRObjectsOrderer _orderer;

	/** The ref orderer. */
	private ReferencesOrderer _refOrderer;

	/** The comparator. */
	private Comparator<Aspect> _comparator;

	/** The ref comparator. */
	private Comparator<ReferenceMods> _refComparator;

	/** The filters. */
	private List<AEFilter> _filters;

	/** The aspects. */
	private Vector<Aspect> _aspects;

	/** The filtered aspects. */
	private Vector<Aspect> filteredAspects;

	/** The ordered aspects. */
	private Vector<OrderingHead> _orderedAspects;
	
	/** The ordered aspects. */
	private Vector<OrderingHead> _orderedPersonalHeads;
	
	// private Vector<OrderingHead> reducedOrderedAspects;
	/** The aspects references. */
	private Vector<String> _aspectsReferences;

	/** The aspects related objects. */
	private Vector<String> _aspectsRelatedObjects;

	/** The aspects semantics. */
	private Vector<String> _aspectsSemantics;

	/** The aspects users. */
	private Vector<String> _aspectsUsers;

	/** The aspect min year. */
	private int _aspectMinYear;

	/** The aspect max year. */
	private int _aspectMaxYear;

	/** The ordered by semantic. */
	private boolean _orderedBySemantic;

	/** The shown by person. */
	private boolean _shownByPerson;

	/** The references. */
	private Vector<ReferenceMods> _references;

	/** The person references. */
	private Vector<ReferenceMods> _personReferences;

	/** The hosted references. */
	private Vector<ReferenceMods> _hostedReferences;

	/** The reference ids. */
	private Vector<String> _referenceIds;

	/** The filtered references. */
	private Vector<ReferenceMods> _filteredReferences;

	/** The ordered references. */
	private Vector<OrderingHead> _orderedReferences;

	/** The ref filters. */
	private List<AEFilter> _refFilters;

	/** The references users. */
	private Vector<String> _referencesUsers;

	private boolean _lazySorting = false;

	/**
	 * Adds the filter.
	 * @param filter the filter
	 */
	public final void addFilter(final AEFilter filter)
	{
		if (_filters == null)
		{
			_filters = new ArrayList<AEFilter>();
		}
		_filters.add(filter);
		_orderedAspects = null;
		_orderedReferences = null;
	}

	/**
	 * Adds the ref filter.
	 * @param refFilter the ref filter
	 */
	public final void addRefFilter(final AEFilter refFilter)
	{
		if (_refFilters == null)
		{
			_refFilters = new ArrayList<AEFilter>();
		}
		_refFilters.add(refFilter);
		_orderedReferences = null;
	}

	/**
	 * Builds the ordered references on aspects.
	 * @param orderedAspects2 the ordered aspects2
	 * @return the vector
	 */
	private Vector<OrderingHead> buildOrderedReferencesOnAspects(final Vector<OrderingHead> orderedAspects2)
	{
		for (OrderingHead oh : orderedAspects2)
		{
			oh.setReferences(loadReferences(oh.getAspects(), null));
		}
		return orderedAspects2;
	}

	/**
	 * Filter aspects.
	 * @param monitor
	 * @return the vector
	 */
	private Vector<Aspect> filterAspects(IProgressMonitor monitor)
	{
		if (_filters != null)
		{
			Vector<Aspect> filtered = new Vector<Aspect>(_aspects.size());
			for (int i = 0; i < _aspects.size(); i++)
			{
				boolean add = true;
				for (int j = 0; j < _filters.size(); j++)
				{
					add = _filters.get(j).select(_aspects.get(i));
					if (!add)
					{
						break;
					}
				}
				if (add)
				{
					filtered.add(_aspects.get(i));
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
			return filtered;
		}
		return _aspects;
	}

	/**
	 * Filter references.
	 * @param filteredAspects2 the filtered aspects2
	 * @param monitor
	 * @return the vector
	 */
	private Vector<ReferenceMods> filterReferences(final Vector<Aspect> filteredAspects2, IProgressMonitor monitor)
	{
		if (_filters != null)
		{
			if (filteredAspects2.size() < _aspects.size())
			{
				Vector<ReferenceMods> refOfFilteredAspects = new Vector<ReferenceMods>(_references.size());
				Vector<String> refOfFilteredAspectsId = new Vector<String>(_referenceIds.size());
				for (Aspect a : filteredAspects2)
				{
					if (a.getValidation() != null)
					{
						for (ValidationStm vs : a.getValidation().getValidationStms())
						{
							if (vs.getReference() != null && vs.getReference().getSourceId() != null)
							{
								String id = vs.getReference().getSourceId().toString();
								if (!refOfFilteredAspects.contains(id))
								{
									if (_facade.getReference(new PdrId(id)) != null)
									{
										refOfFilteredAspects.add(_facade.getReference(vs.getReference().getSourceId()));
										refOfFilteredAspectsId.add(vs.getReference().getSourceId().toString());
									}
								}
							}
						}
					}
				}
				if (_refFilters != null)
				{
					Vector<ReferenceMods> filtered = new Vector<ReferenceMods>(_references.size());
					for (int i = 0; i < refOfFilteredAspects.size(); i++)
					{
						boolean add = true;
						for (int j = 0; j < _refFilters.size(); j++)
						{
							add = _refFilters.get(j).select(refOfFilteredAspects.get(i));
							if (!add)
							{
								break;
							}
						}
						if (add)
						{
							filtered.add(refOfFilteredAspects.get(i));
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
					return filtered;
				}
				else
				{
					return refOfFilteredAspects;
				}
			}
			else if (_refFilters != null)
			{
				Vector<ReferenceMods> filtered = new Vector<ReferenceMods>(_references.size());
				for (int i = 0; i < _references.size(); i++)
				{
					boolean add = true;
					for (int j = 0; j < _refFilters.size(); j++)
					{
						add = _refFilters.get(j).select(_references.get(i));
						if (!add)
						{
							break;
						}
					}
					if (add)
					{
						filtered.add(_references.get(i));
					}
				}
				return filtered;
			}

		}
		return _references;
	}

	/**
	 * Gets the arranged aspects.
	 * @return the arranged aspects
	 */
	public final Vector<OrderingHead> getArrangedAspects()
	{
		final ProgressMonitorDialog dialog = new ProgressMonitorDialog(new Shell());
		dialog.setCancelable(true);

		try
		{
			dialog.run(true, true, new IRunnableWithProgress()
			{

				@Override
				public void run(final IProgressMonitor monitor)
				{

					if (_aspects == null)
					{
						loadAspects(monitor);
					}
					monitor.beginTask("Filter Aspects. Number of Aspects: " + _aspects.size(), _aspects.size());
					filteredAspects = filterAspects(monitor);
					if (_orderedAspects == null)
					{
						monitor.beginTask("Group Aspects. Number of Aspects: " + filteredAspects.size(),
								filteredAspects.size());
						if (_orderer != null)
						{
							_orderedAspects = _orderer.orderedObjects(filteredAspects, monitor);
						}
						else
						{
							_orderedAspects = new Vector<OrderingHead>(1);
							OrderingHead oh = new OrderingHead();
							oh.setLabel(NLMessages.getString("View_group_all"));
							oh.setValue("ALL");
							oh.setImageString(IconsInternal.ASPECTS);
							oh.setAspects(filteredAspects);
							_orderedAspects.add(oh);
						}
					}
					monitor.beginTask("Sort Aspects. Number of Aspect Groups: " + _orderedAspects.size(),
							_orderedAspects.size());
					if (_lazySorting && !_orderedAspects.isEmpty())
					{
						if (_comparator != null)
						{
							Collections.sort(_orderedAspects.firstElement().getAspects(), _comparator);
						}
						else
						{
							Collections
									.sort(_orderedAspects.firstElement().getAspects(), new AspectsByCronComparator());

						}
						_orderedAspects.firstElement().setSorted(true);
					}
					else
					{
						if (_comparator != null)
						{
							for (OrderingHead oh : _orderedAspects)
							{
								Collections.sort(oh.getAspects(), _comparator);
								oh.setSorted(true);
								monitor.worked(1);
							}
						}
						else
						{
							AspectsByCronComparator cronComp = new AspectsByCronComparator();
							for (OrderingHead oh : _orderedAspects)
							{
								Collections.sort(oh.getAspects(), cronComp);
								oh.setSorted(true);
								monitor.worked(1);
							}
						}
					}
				}
			});
		}
		catch (InvocationTargetException e)
		{
			e.printStackTrace();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}

		return _orderedAspects;
	}


	/**
	 * Computes a list of {@link OrderingHead}s that are representing a {@link PdrObject},
	 * (most likely a {@link Person}), all children of those ordering heads are related
	 * to as aspect_of, respectively. In other words, create and return an additional
	 * layer of classification built on top of the classification tree this {@link PDRObjectsProvider}
	 * provides. 
	 * @return additional layer of classification s.t. all aspects are grouped by their aspect_of counterparts
	 * @see #getArrangedAspects()
	 */
	public Vector<OrderingHead> getArrangedAspectsByObjects() {
		// generate new person -> group -> aspects structure only if cache is empty!
		if (_orderedPersonalHeads == null) {
			// TODO: generalize for all kinds of PdrObjects, not just Person
			HashMap<Person, OrderingHead> people = new HashMap<Person, OrderingHead>();
			Vector<OrderingHead> groups = this.getArrangedAspects();
			Facade facade = Facade.getInstanz();
			// output for debugging
			/*for (OrderingHead group : groups) {
				System.out.println(group.getLabel());
				for (Aspect a : group.getAspects())
					System.out.println(" "+a.getDisplayName());
			}*/
				
			// FIXME: groups == null if AspectsByReferenceOrderer is active!
			for (OrderingHead group : groups)
				for (Aspect aspect : group.getAspects()) {
					PdrObject pobj = facade.getPdrObject(aspect.getOwningObjectId());
					// derive additional layer of classification, assigning each aspect's
					// class to the person the aspect belongs to
					// TODO: generally, an aspect_of relation does not require a Person, but can 
					// link to any PdrObject instance. 
					// FIXME: so update this accordingly!
					if (pobj instanceof Person) {
						Person p = (Person)pobj;
						// if owning person/object is not represented in a class yet, create one
						if (!people.containsKey(p)) {
							OrderingHead personalClass = new OrderingHead(p.getPdrId().toString());
							personalClass.setLabel(p.getDisplayName());
							personalClass.setImageString(IconsInternal.PERSON);
							people.put(p, personalClass);
						}
						// retrieve class representing the person this aspect belongs to
						OrderingHead byPerson = people.get(p);
						// add aspect to retrieved class, maintaining original classification structure 
						if (!byPerson.contains(group)) {
							// create a copy of this aspect's class
							OrderingHead head = group.copy();
							// remove child elements of copy and insert current aspect
							head.clear();
							head.addAspect(aspect);
							// attach head to representation of owning person
							byPerson.addSubCategory(head);
						} else {
							OrderingHead subcat = byPerson.findSubCategory(group); 
							if (subcat != null) 
								subcat.addAspect(aspect);
							else {
								//byPerson.addAspect(aspect);
								System.out.println("WARNING: Head containing "+byPerson.getLabel()+
										" does not have any sub-categories!!!");
							}
						}
					}
				}
			// return newly created layer of ordering heads
			// FIXME: currently active comparator must be applied to at least every 
			// aspect collection (owned by ordering heads), if not to ordering head
			// children themselves (which would be still to be implemented, since comparators
			// implement Comparator<Aspect> specifically..)
			_orderedPersonalHeads = new Vector<OrderingHead>(people.values());
		}
	
		// regardless of whether new structure has been created right now:
		// apply comparator -> sortiere aspekte in den untergruppen
		if (_comparator != null)
			System.out.println("Sort Aspects using: "+_comparator.getClass().getCanonicalName());
			// TODO: dafür gibt es die hauseigene sort(oh) funktion!!!
			for (OrderingHead byPerson : _orderedPersonalHeads)
				for (OrderingHead cat : byPerson.getSubCategories()) {
					Collections.sort(cat.getAspects(), _comparator);
					cat.setSorted(true); //TODO: why do we even do this?
				}
		return _orderedPersonalHeads;
	}
	
	
	/**
	 * Gets the arranged references.
	 * @return the arranged references
	 */
	public final Vector<OrderingHead> getArrangedReferences()
	{
		final ProgressMonitorDialog dialog = new ProgressMonitorDialog(new Shell());
		dialog.setCancelable(true);

		try
		{
			dialog.run(true, true, new IRunnableWithProgress()
			{

				@Override
				public void run(final IProgressMonitor monitor)
				{

					if (_aspects == null)
					{
						loadAspects(monitor);
					}
					if (_references == null)
					{
						loadReferences(monitor);
						if (_input instanceof Person)
						{
							loadPersonReferences(monitor);
						}
						if (_input instanceof ReferenceMods)
						{
							loadHostedReferences(monitor);
							if (!_references.contains(_input))
							{
								_references.add((ReferenceMods) _input);
							}
						}
					}
					// System.out.println("number of references " +
					// references.size());
					if (_aspects != null)
					{
					monitor.beginTask("Filter Aspects and References. Number of Aspects: " + _aspects.size()
							+ " Number of Aspects: " + _references.size(), _aspects.size() + _references.size());

					// System.out.println("number of filteredReferences " +
					// filteredReferences.size());
					}
					filteredAspects = filterAspects(monitor);
					_filteredReferences = filterReferences(filteredAspects, monitor);
					if (_orderedReferences == null)
					{
						if (_orderer != null)
						{
							_orderedAspects = _orderer.orderedObjects(filteredAspects, monitor);
							_orderedReferences = buildOrderedReferencesOnAspects(_orderedAspects);
							if (_personReferences != null && _personReferences.size() > 0)
							{
								monitor.beginTask("Group Aspects. Number of Aspects: " + filteredAspects.size(),
										filteredAspects.size());
								OrderingHead oh = new OrderingHead();
								if (_input instanceof Person)
								{
									oh.setLabel(((Person) _input).getDisplayName());
								}
								else
								{
									oh.setLabel("????");
								}
								oh.setValue("Person");
								oh.setImageString(IconsInternal.PERSON);
								oh.setReferences(_personReferences);
								_orderedReferences.insertElementAt(oh, 0);
							}
							if (_hostedReferences != null && _hostedReferences.size() > 0)
							{
								OrderingHead oh = new OrderingHead();
								oh.setLabel("contains");
								oh.setValue("contains");
								oh.setImageString(IconsInternal.REFERENCE);
								oh.setReferences(_hostedReferences);
								_orderedReferences.add(oh);
							}
						}
					}
					if (_orderedReferences == null)
					{
						monitor.beginTask("Group References. Number of References: " + _filteredReferences.size(),
								_filteredReferences.size());
						if (_refOrderer != null)
						{
							_orderedReferences = _refOrderer.orderedReferences(_filteredReferences, monitor);
						}
						else
						{
							_orderedReferences = new Vector<OrderingHead>(1);
							OrderingHead oh = new OrderingHead();
							oh.setLabel(NLMessages.getString("View_group_all"));
							oh.setValue("ALL");
							oh.setImageString(IconsInternal.REFERENCES);
							oh.setReferences(_filteredReferences);
							_orderedReferences.add(oh);

							if (_hostedReferences != null && _hostedReferences.size() > 0)
							{
								oh = new OrderingHead();
								oh.setLabel("contains");
								oh.setValue("contains");
								oh.setImageString(IconsInternal.REFERENCE);
								oh.setReferences(_hostedReferences);
								_orderedReferences.add(oh);
							}
						}
					}
					monitor.beginTask("Sort References. Number of References: " + _filteredReferences.size(),
							_filteredReferences.size());
					if (_lazySorting)
					{
						if (_refComparator != null)
						{
							Collections.sort(_orderedReferences.firstElement().getReferences(), _refComparator);
						}
						else
						{
							Collections.sort(_orderedReferences.firstElement().getReferences(),
									new ReferenceByAuthorTitleComparator());
						}
						_orderedReferences.firstElement().setSorted(true);
					}
					else
					{
						if (_refComparator != null)
						{
							for (OrderingHead oh : _orderedReferences)
							{
								Collections.sort(oh.getReferences(), _refComparator);
								oh.setSorted(true);

							}
						}
						else
						{
							ReferenceByAuthorTitleComparator cronComp = new ReferenceByAuthorTitleComparator();
							for (OrderingHead oh : _orderedReferences)
							{
								Collections.sort(oh.getReferences(), cronComp);
								oh.setSorted(true);
							}
						}
					}

				}
			});
		}
		catch (InvocationTargetException e)
		{
			e.printStackTrace();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}


		return _orderedReferences;
	}

	
	
	/**
	 * Gets the aspect max year.
	 * @return the aspect max year
	 */
	public final int getAspectMaxYear()
	{
		return _aspectMaxYear;
	}

	/**
	 * Gets the aspect min year.
	 * @return the aspect min year
	 */
	public final int getAspectMinYear()
	{
		return _aspectMinYear;
	}

	/**
	 * Gets the aspects.
	 * @return the aspects
	 */
	public final Vector<Aspect> getAspects()
	{
		if (_aspects == null)
		{

			loadAspects(null);
		}
		return _aspects;
	}

	/**
	 * Gets the aspects references.
	 * @return the aspects references
	 */
	public final Vector<String> getAspectsReferences()
	{
		return _aspectsReferences;
	}

	/**
	 * Gets the aspects related objects.
	 * @return the aspects related objects
	 */
	public final Vector<String> getAspectsRelatedObjects()
	{
		return _aspectsRelatedObjects;
	}

	/**
	 * Gets the aspects semantics.
	 * @return the aspects semantics
	 */
	public final Vector<String> getAspectsSemantics()
	{
		return _aspectsSemantics;
	}

	/**
	 * Gets the aspects users.
	 * @return the aspects users
	 */
	public final Vector<String> getAspectsUsers()
	{
		return _aspectsUsers;
	}

	/**
	 * Gets the comparator.
	 * @return the comparator
	 */
	public final Comparator<Aspect> getComparator()
	{
		return _comparator;
	}

	/**
	 * Gets the filters.
	 * @return the filters
	 */
	public final List<AEFilter> getFilters()
	{
		return _filters;
	}
	
	/**
	 * For a given string identifier, return an instance of the corresponding
	 * {@link AEFilter} implementation, provided such a filter is currently
	 * active in this {@link PDRObjectsProvider}. Accepted identifiers are
	 * <i>person, reference, semantic, year</i> and <i>user</i>.
	 * @return
	 * @param type string identifier
	 */
	public AEFilter getFilter(String type) {
		if (!new Vector<String>(Arrays.asList("person", "reference", "semantic", "year", "user")).contains(type))
			return null;
		//TODO: make class member? 
		HashMap<String, Class<? extends AEFilter>> types = new HashMap<String, Class<? extends AEFilter>>();
		types.put("person", AspectPersonFilter.class);
		types.put("reference", AspectReferenceFilter.class);
		types.put("semantic", AspectSemanticFilter.class);
		types.put("year", AspectYearFilter.class);
		types.put("user", PdrObjectUserFilter.class);
		if (_filters != null)
			for (AEFilter f : _filters)
				if (f.getClass().equals(types.get(type)))
					return f;
		return null;
	}

	/**
	 * Gets the input.
	 * @return the input
	 */
	public final Object getInput()
	{
		return _input;
	}

	/**
	 * Gets the number of aspects.
	 * @return the number of aspects
	 */
	public final int getNumberOfAspects()
	{
		if (_aspects == null)
		{
			loadAspects(null);
		}
		if (_filters != null && !_filters.isEmpty())
		{
			if (filteredAspects == null)
			{
				filteredAspects = filterAspects(null);
			}
			return filteredAspects.size();
		}
		return _aspects.size();
	}

	/**
	 * Gets the number of aspects.
	 * @return the number of aspects
	 */
	public final int getNumberOfReferences()
	{
		if (_aspects == null)
		{
			loadAspects(null);
		}
		if (_references == null)
		{
			loadReferences(null);
		}
		if (_filters != null && !_filters.isEmpty())
		{
			if (filteredAspects == null)
			{
				filteredAspects = filterAspects(null);
			}
		}
		if (_refFilters != null && !_refFilters.isEmpty())
		{
			if (_filteredReferences == null)
			{
				_filteredReferences = filterReferences(filteredAspects, null);
			}
			return _filteredReferences.size();
		}
		return _references.size();
	}

	/**
	 * Gets the orderer.
	 * @return the orderer
	 */
	public final PDRObjectsOrderer getOrderer()
	{
		return _orderer;
	}

	/**
	 * Gets the ref comparator.
	 * @return the ref comparator
	 */
	public final Comparator<ReferenceMods> getRefComparator()
	{
		return _refComparator;
	}

	/**
	 * Gets the references.
	 * @return the references
	 */
	public final Vector<ReferenceMods> getReferences()
	{
		final ProgressMonitorDialog dialog = new ProgressMonitorDialog(new Shell());
		dialog.setCancelable(true);

		try
		{
			dialog.run(true, true, new IRunnableWithProgress()
			{

				@Override
				public void run(final IProgressMonitor monitor)
				{
					if (_aspects == null)
					{
						loadAspects(monitor);
					}
					if (_references == null)
					{
						loadReferences(monitor);
						if (_input instanceof Person)
						{
							loadPersonReferences(monitor);
						}
						if (_input instanceof ReferenceMods)
						{
							loadHostedReferences(monitor);
							if (!_references.contains(_input))
							{
								_references.add((ReferenceMods) _input);
							}
						}
					}
				}
			});
		}
		catch (InvocationTargetException e)
		{
			e.printStackTrace();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		return _references;
	}

	/**
	 * Gets the references users.
	 * @return the references users
	 */
	public final Vector<String> getReferencesUsers()
	{
		return _referencesUsers;
	}

	/**
	 * Gets the ref filters.
	 * @return the ref filters
	 */
	public final List<AEFilter> getRefFilters()
	{
		return _refFilters;
	}

	/**
	 * Gets the ref orderer.
	 * @return the ref orderer
	 */
	public final ReferencesOrderer getRefOrderer()
	{
		return _refOrderer;
	}

	/**
	 * Gets the search filter.
	 * @return the search filter
	 */
	public final AspectSearchTextFilter getSearchFilter()
	{
		if (_filters != null)
		{
			for (AEFilter f : _filters)
			{
				if (f instanceof AspectSearchTextFilter)
				{
					return (AspectSearchTextFilter) f;
				}
			}
		}
		return null;
	}

	/**
	 * Checks for person filter.
	 * @return true, if successful
	 */
	public final boolean hasPersonFilter()
	{
		if (_filters != null)
		{
			for (AEFilter f : _filters)
			{
				if (f instanceof AspectPersonFilter)
				{
					return true;
				}
			}
			return false;
		}
		else
		{
			return false;
		}
	}

	/**
	 * Checks for reference filter.
	 * @return true, if successful
	 */
	public final boolean hasReferenceFilter()
	{
		if (_filters != null)
		{
			for (AEFilter f : _filters)
			{
				if (f instanceof AspectReferenceFilter)
				{
					return true;
				}
			}
			return false;
		}
		else
		{
			return false;
		}
	}

	/**
	 * Checks for semantic filter.
	 * @return true, if successful
	 */
	public final boolean hasSemanticFilter()
	{
		if (_filters != null)
		{
			for (AEFilter f : _filters)
			{
				if (f instanceof AspectSemanticFilter)
				{
					return true;
				}
			}
			return false;
		}
		else
		{
			return false;
		}
	}

	/**
	 * Checks for user filter.
	 * @return true, if successful
	 */
	public final boolean hasUserFilter()
	{
		if (_filters != null && _refFilters == null)
		{
			for (AEFilter f : _filters)
			{
				if (f instanceof PdrObjectUserFilter)
				{
					return true;
				}
			}
			return false;
		}
		else if (_refFilters != null)
		{
			for (AEFilter f : _refFilters)
			{
				if (f instanceof PdrObjectUserFilter)
				{
					return true;
				}
			}
			return false;
		}
		else
		{
			return false;
		}
	}

	/**
	 * Checks for year filter.
	 * @return true, if successful
	 */
	public final boolean hasYearFilter()
	{
		if (_filters != null)
		{
			for (AEFilter f : _filters)
			{
				if (f instanceof AspectYearFilter)
				{
					return true;
				}
			}
			return false;
		}
		else
		{
			return false;
		}
	}

	/**
	 * Checks if is ordered by semantic.
	 * @return true, if is ordered by semantic
	 */
	public final boolean isOrderedBySemantic()
	{
		return _orderedBySemantic;
	}

	/**
	 * Checks if is shown by person.
	 * @return true, if is shown by person
	 */
	public final boolean isShownByPerson()
	{
		return _shownByPerson;
	}

	/**
	 * Updates this {@link PDRObjectsProvider}'s internal collection of {@link Aspect}s, by
	 * calling {@link #loadAspectsByObjects(PdrObject[], IProgressMonitor)} for every 
	 * {@link PdrObject} that has been passed with the latest call of {@link #setInput(Object)}.
	 * In order to retrieve those {@link Aspect}s that fit this requirement, search methods
	 * of the internal {@link AMainSearcher} instance are called.
	 * @param monitor {@link IProgressMonitor} to indicate progress made during retrieval
	 * @see AMainSearcher
	 */
	private void loadAspects(IProgressMonitor monitor)
	{
		_aspectsReferences = new Vector<String>();
		_aspectsUsers = new Vector<String>();
		_aspectsSemantics = new Vector<String>();
		_aspectsRelatedObjects = new Vector<String>();
		_aspectMinYear = 2100;
		_aspectMaxYear = 1000;

		if (_input != null && _input instanceof PdrObject[])
		{
			final PdrObject[] pdrObjs = (PdrObject[]) _input;

			_aspects = new Vector<Aspect>();

			loadAspectsByObjects(pdrObjs, monitor);
			_shownByPerson = pdrObjs instanceof Person[];
			if (monitor != null)
			{
				monitor.worked(1);
				if (monitor.isCanceled())
				{
					return;
				}
			}
			if (pdrObjs.length > 1)
			{
				_shownByPerson = false;
			}
		}
		else if (_input != null && _input instanceof PdrObject)
		{
			PdrObject pdrObj = (PdrObject) _input;
			_aspects = new Vector<Aspect>();

			loadAspectsByObjects(new PdrObject[]
			{pdrObj}, monitor);


			_shownByPerson = (pdrObj instanceof Person);
		}
	}

	/**
	 * Load aspects by object.
	 * @param o the o
	 * @param monitor
	 */
	private void loadAspectsByObjects(final PdrObject[] objects, IProgressMonitor monitor)
	{
		ArrayList<PdrObject> l = new ArrayList<PdrObject>(objects.length);
		for (PdrObject o : objects)
		{
			if (!o.isAspectsLoaded())
			{
				l.add(o);
			}
		}
		final PdrObject[] unloadedObjs = (PdrObject[]) l.toArray(new PdrObject[l.size()]);
		if (monitor != null && unloadedObjs != null)
		{
			monitor.beginTask("Loading Aspects of selected Objects. Number of Objects: " + unloadedObjs.length,
					unloadedObjs.length);

		}
		if (objects.length > 0 && objects[0] instanceof ReferenceMods)
		{

			try
			{
				_mainSearcher.searchAspectsByReferences(unloadedObjs, monitor);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			try
			{
				_mainSearcher.searchAspectsByRelatedObjects(unloadedObjs, monitor);
			}
			catch (ParseException e)
			{
				e.printStackTrace();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		if (monitor != null && objects != null)
		{
			monitor.beginTask("Processing Aspects", objects.length);
		}
		HashSet<PdrId> aspectIDs = new HashSet<PdrId>();
		for (PdrObject o : objects)
		{
			// if selected objects are Aspects, add these seleceted aspects to the loaded aspects.
			if (o instanceof Aspect)
			{
				if (!aspectIDs.contains(o.getPdrId()))
				{
					Aspect a = (Aspect) o;
					_aspects.add(a);
					aspectIDs.add(a.getPdrId());
					processAspectReferences(a);
					processAspectSemantics(a);
					processAspectUser(a);
					processAspectRelatedObjects(a);
					processAspectTimeDim(a);
				}
			}
			for (PdrId id : o.getAspectIds())
			{
				Aspect a = _facade.getAspect(id);

				if (a != null)
				{
					if (!aspectIDs.contains(a.getPdrId()))
					{
						_aspects.add(a);
						aspectIDs.add(a.getPdrId());
						processAspectReferences(a);
						processAspectSemantics(a);
						processAspectUser(a);
						processAspectRelatedObjects(a);
						processAspectTimeDim(a);
					}

					
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
			
			if (monitor != null)
			{
				monitor.worked(1);
				if (monitor.isCanceled())
				{
					break;
				}
			}
		}
		
		aspectIDs = null;
		if (_aspectMinYear == 2100)
		{
			_aspectMinYear = 1000;
		}
		if (_aspectMaxYear == 1000)
		{
			_aspectMaxYear = 2100;
		}

	}

	/**
	 * Load hosted references.
	 * @param monitor
	 */
	private void loadHostedReferences(IProgressMonitor monitor)
	{
		_hostedReferences = new Vector<ReferenceMods>();
		ReferenceMods host = (ReferenceMods) _input;
		if (host.getHostedReferences() != null)
		{
			for (String id : host.getHostedReferences())
			{
				ReferenceMods hosted = _facade.getReference(new PdrId(id));
				if (hosted != null && !_hostedReferences.contains(hosted))
				{
					_hostedReferences.add(hosted);
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
		}

	}

	/**
	 * Load person references.
	 * @param monitor
	 */
	private void loadPersonReferences(IProgressMonitor monitor)
	{
		_personReferences = new Vector<ReferenceMods>(4);
		Vector<String> refIds = new Vector<String>(4);
		if (_input instanceof Person)
		{
			Person p = (Person) _input;
			ReferenceMods reference;
			if (p.getConcurrences() != null && !p.getConcurrences().getConcurrences().isEmpty())
			{
				for (Concurrence c : p.getConcurrences().getConcurrences())
				{
					if (c.getReferences() != null && !c.getReferences().isEmpty())
					{
						for (ValidationStm cRef : c.getReferences())
						{
							if (cRef.getReference() != null && cRef.getReference().getSourceId() != null)
							{
								String id = cRef.getReference().getSourceId().toString();
								if (!refIds.contains(id))
								{
									if (_facade.getReference(new PdrId(id)) != null)
									{
										reference = _facade.getReference(cRef.getReference().getSourceId());
										_personReferences.add(reference);
										refIds.add(id);
										if (!_references.contains(reference))
										{
											_references.add(reference);
										}
									}
								}
							}
						}

					}
				}
				if (monitor != null)
				{
					monitor.worked(1);
					if (monitor.isCanceled())
					{
						return;
					}
				}
			}

		}
	}

	/**
	 * Load references.
	 * @param monitor
	 */
	private void loadReferences(IProgressMonitor monitor)
	{
		if (_aspects == null)
		{
			loadAspects(monitor);
		}
		_references = new Vector<ReferenceMods>();
		_referenceIds = new Vector<String>();
		_referencesUsers = new Vector<String>();
		ReferenceMods reference;
		String id = null;
		if (_aspects != null)
		{
			for (Aspect a : _aspects)
			{
				if (a.getValidation() != null)
				{
					for (ValidationStm vs : a.getValidation().getValidationStms())
					{
						if (vs.getReference() != null && vs.getReference().getSourceId() != null)
						{
							id = vs.getReference().getSourceId().toString();
							if (!_referenceIds.contains(id))
							{
								if (_facade.getReference(new PdrId(id)) != null)
								{
									reference = _facade.getReference(vs.getReference().getSourceId());
									_references.add(reference);
									_referenceIds.add(vs.getReference().getSourceId().toString());
									processReferencesUsers(reference);
								}
							}
						}
					}
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
		}
	}

	/**
	 * Load references.
	 * @param aspects2 the aspects2
	 * @return the vector
	 */
	private Vector<ReferenceMods> loadReferences(final Vector<Aspect> aspects2, IProgressMonitor monitor)
	{
		Vector<ReferenceMods> refs = new Vector<ReferenceMods>(_references.size());
		Vector<String> refIds = new Vector<String>(_referenceIds.size());
		for (Aspect a : aspects2)
		{
			if (a.getValidation() != null)
			{
				for (ValidationStm vs : a.getValidation().getValidationStms())
				{
					if (vs.getReference() != null && vs.getReference().getSourceId() != null)
					{
						String id = vs.getReference().getSourceId().toString();
						if (!refIds.contains(id))
						{
							if (_facade.getReference(new PdrId(id)) != null)
							{
								refs.add(_facade.getReference(vs.getReference().getSourceId()));
								refIds.add(vs.getReference().getSourceId().toString());
							}
						}
					}
				}
			}
		}
		return refs;
	}

	/**
	 * Process aspect references.
	 * @param a the a
	 */
	private void processAspectReferences(final Aspect a)
	{
		String reference = null;
		ValidationStm valStm;
		if (a.getValidation() != null && a.getValidation().getValidationStms() != null
				&& !a.getValidation().getValidationStms().isEmpty())
		{
			for (int j = 0; j < a.getValidation().getValidationStms().size(); j++)
			{
				valStm = a.getValidation().getValidationStms().get(j);
				if (valStm.getReference() != null && valStm.getReference().getSourceId() != null)
				{
					reference = valStm.getReference().getSourceId().toString();
				}
				else
				{
					reference = "undefined";
				}
				if (!_aspectsReferences.contains(reference))
				{
					_aspectsReferences.add(reference);
				}
			}
		}
	}

	/**
	 * Process aspect related objects.
	 * @param a the a
	 */
	private void processAspectRelatedObjects(final Aspect a)
	{
		RelationStm relationStm;
		if (a.getRelationDim() != null && !a.getRelationDim().getRelationStms().isEmpty())
		{
			for (int j = 0; j < a.getRelationDim().getRelationStms().size(); j++)
			{
				relationStm = a.getRelationDim().getRelationStms().get(j);
				if (relationStm.getSubject() != null && !relationStm.getSubject().equals(a.getPdrId()))
				{
					if (!_aspectsRelatedObjects.contains(relationStm.getSubject().toString()))
					{
						_aspectsRelatedObjects.add(relationStm.getSubject().toString());
					}
				}
				if (relationStm.getRelations() != null)
				{
					for (Relation rel : relationStm.getRelations())
					{
						if (rel.getObject() != null)
						{
							if (!_aspectsRelatedObjects.contains(rel.getObject().toString()))
							{
								_aspectsRelatedObjects.add(rel.getObject().toString());
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Process aspect semantics.
	 * @param a the a
	 */
	private void processAspectSemantics(final Aspect a)
	{
		String semantic;

		for (int j = 0; j < a.getSemanticDim().getSemanticStms().size(); j++)
		{
			semantic = a.getSemanticDim().getSemanticStms().get(j).getLabel();
			if (!_aspectsSemantics.contains(semantic))
			{
				_aspectsSemantics.add(semantic);
			}
		}
	}

	/**
	 * Process aspect time dim.
	 * @param a the a
	 */
	private void processAspectTimeDim(final Aspect a)
	{
		TimeStm timeStm;
		if (a.getTimeDim() != null && !a.getTimeDim().getTimeStms().isEmpty())
		{
			for (int j = 0; j < a.getTimeDim().getTimeStms().size(); j++)
			{
				timeStm = a.getTimeDim().getTimeStms().get(j);
				if (timeStm.getTimes() != null && !timeStm.getTimes().isEmpty())
				{
					for (Time t : timeStm.getTimes())
					{
						if (t.getTimeStamp() != null)
						{
							if (t.getTimeStamp().getYear() > _aspectMaxYear)
							{
								_aspectMaxYear = t.getTimeStamp().getYear();
							}
							if (t.getTimeStamp().getYear() < _aspectMinYear)
							{
								_aspectMinYear = t.getTimeStamp().getYear();
							}
						}
					}
				}
			}
		}

	}

	/**
	 * Process aspect user.
	 * @param a the a
	 */
	private void processAspectUser(final Aspect a)
	{

		String userId = a.getRecord().getRevisions().firstElement().getAuthority().toString();

		if (!_aspectsUsers.contains(userId))
		{
			_aspectsUsers.add(userId);
		}
	}

	/**
	 * Process references users.
	 * @param reference the reference
	 */
	private void processReferencesUsers(final ReferenceMods reference)
	{
		if (reference.getRecord() != null && reference.getRecord().getRevisions() != null
				&& !reference.getRecord().getRevisions().isEmpty()
				&& reference.getRecord().getRevisions().firstElement().getAuthority() != null)
		{
			String userId = reference.getRecord().getRevisions().firstElement().getAuthority().toString();
			if (!_referencesUsers.contains(userId))
			{
				// System.out.println("process ref users id " + userId);
				_referencesUsers.add(userId);
			}
		}
	}

	// private OrderingHead mergeOrderingHeads(OrderingHead targetHead,
	// OrderingHead objectHead)
	// {
	//
	// String label = targetHead.getLabel();
	// String label2 = objectHead.getLabel();
	// // System.out.println("merge targetlabel " + label);
	// // System.out.println("merge objectLabel " + label2);
	//
	// if (orderer instanceof AspectByYearOrderer)
	// {
	// label = label.substring(0, Math.min(4, label.length()));
	// label2 = label2.substring(0, Math.min(4, label.length()));
	// }
	// else
	// {
	// if (label.length() > 10)
	// {
	// label = label.substring(0, 7) + "...";
	// }
	// if (label2.length() > 10)
	// {
	// label2 = label2.substring(0, 7) + "...";
	// }
	// }
	// label += "-";
	//
	// label += label2;
	// targetHead.setLabel(label);
	// targetHead.setValue(targetHead.getValue() + " " + objectHead.getValue());
	// targetHead.getAspects().addAll(objectHead.getAspects());
	// return targetHead;
	// }

	/**
	 * Removes the all filters.
	 */
	public final void removeAllFilters()
	{
		_filters = null;
		_orderedAspects = null;
		_orderedPersonalHeads = null;
		_orderedReferences = null;
	}

	/**
	 * Removes the all ref filters.
	 */
	public final void removeAllRefFilters()
	{
		_refFilters = null;
		_orderedAspects = null;
		_orderedPersonalHeads = null;
		_orderedReferences = null;
	}

	/**
	 * Removes the filter.
	 * @param filter the filter
	 */
	public final void removeFilter(final AEFilter filter)
	{
		if (_filters != null)
		{
			_filters.remove(filter);
		}
		_orderedAspects = null;
		_orderedPersonalHeads = null;
		_orderedReferences = null;
	}

	/**
	 * Removes the ref filter.
	 * @param refFilter the ref filter
	 */
	public final void removeRefFilter(final AEFilter refFilter)
	{
		if (_refFilters != null)
		{
			_refFilters.remove(refFilter);
		}
		_orderedReferences = null;
	}

	/**
	 * Sets the comparator.
	 * @param comparator the new comparator
	 */
	public final void setComparator(final Comparator<Aspect> comparator)
	{
		this._comparator = comparator;
	}

	/**
	 * Sets the filters.
	 * @param filters the new filters
	 */
	public final void setFilters(final List<AEFilter> filters)
	{
		this._filters = filters;
		_orderedAspects = null;
		_orderedPersonalHeads = null;
		_orderedReferences = null;
	}

	/**
	 * Sets the input. Depending on what is intended to be done with that, the
	 * argument can be an instance of {@link PdrObject}[], a {@link Person},
	 * or {@link ReferenceMods}.
	 * @param input the new input
	 */
	public final void setInput(final Object input)
	{
		this._input = input;
		_aspects = null;
		_orderedAspects = null;
		_orderedPersonalHeads = null;
		_references = null;
		_personReferences = null;
		_hostedReferences = null;
		_orderedReferences = null;
		if (input instanceof Person)
		{
			_shownByPerson = true;
		}
		else
		{
			_shownByPerson = false;
		}
	}

	/**
	 * Sets the orderer. Resetting the Orderer will set orderedAspects and
	 * orderedReferences to null. 
	 * @param orderer the new orderer
	 */
	public final void setOrderer(final PDRObjectsOrderer orderer)
	{
		// System.out.println("3 orderer " + orderer);
		
		this._orderer = orderer;
		_orderedAspects = null; 
		_orderedPersonalHeads = null;
		_orderedReferences = null;
		if (orderer instanceof AspectsBySemanticOrderer)
		{
			_orderedBySemantic = true;
		}
		else
		{
			_orderedBySemantic = false;
		}
	}

	/**
	 * Sets the ref comparator.
	 * @param refComparator the new ref comparator
	 */
	public final void setRefComparator(final Comparator<ReferenceMods> refComparator)
	{
		this._refComparator = refComparator;
	}

	/**
	 * Sets the ref filters.
	 * @param refFilters the new ref filters
	 */
	public final void setRefFilters(final List<AEFilter> refFilters)
	{
		this._refFilters = refFilters;
		_orderedReferences = null;
	}

	/**
	 * Sets the ref orderer.
	 * @param refOrderer the new ref orderer
	 */
	public final void setRefOrderer(final ReferencesOrderer refOrderer)
	{
		this._refOrderer = refOrderer;
	}

	public void setLazySorting(boolean lazySorting)
	{
		this._lazySorting = lazySorting;
	}

	/**
	 * Sort aspects and references within given {@link OrderingHead} by sorting criteria previously set
	 * by {@link #setComparator(Comparator)}, without accessing the db. 
	 * Default comparators are {@link AspectsByCronComparator} and {@link ReferenceByAuthorTitleComparator},
	 * respectively. 
	 * @param orderingHead
	 */
	public void sort(OrderingHead orderingHead)
	{
		if (orderingHead != null && !orderingHead.isSorted())
		{
			System.out.println("Sort " + orderingHead.getLabel());
			if (orderingHead.getAspects() != null && !orderingHead.getAspects().isEmpty())
			{
				if (_comparator != null)
				{
					Collections.sort(orderingHead.getAspects(), _comparator);
				}
				else
				{
					Collections.sort(orderingHead.getAspects(), new AspectsByCronComparator());

				}
				orderingHead.setSorted(true);
			}
			else if (orderingHead.getReferences() != null && !orderingHead.getReferences().isEmpty())
			{
				if (_refComparator != null)
				{
					Collections.sort(orderingHead.getReferences(), _refComparator);
				}
				else
				{
					Collections.sort(orderingHead.getReferences(), new ReferenceByAuthorTitleComparator());
				}
				orderingHead.setSorted(true);
			}

		}

	}

}
