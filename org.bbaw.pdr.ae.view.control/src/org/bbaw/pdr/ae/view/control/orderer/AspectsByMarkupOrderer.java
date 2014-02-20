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

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import org.bbaw.pdr.ae.common.AEConstants;
import org.bbaw.pdr.ae.common.CommonActivator;
import org.bbaw.pdr.ae.common.NLMessages;
import org.bbaw.pdr.ae.common.icons.IconsInternal;
import org.bbaw.pdr.ae.config.model.ConfigData;
import org.bbaw.pdr.ae.control.comparator.CronOrderComparator;
import org.bbaw.pdr.ae.control.facade.Facade;
import org.bbaw.pdr.ae.model.Aspect;
import org.bbaw.pdr.ae.model.TaggingRange;
import org.bbaw.pdr.ae.model.view.OrderingHead;
import org.bbaw.pdr.ae.view.control.PDRObjectsOrderer;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;

/**
 * The Class AspectsByMarkupOrderer.
 * @author Christoph Plutte, Jakob Hoeper
 */
public class AspectsByMarkupOrderer implements PDRObjectsOrderer
{

	/** The _facade. */
	private Facade _facade = Facade.getInstanz();

	/** The provider. */
	private String _provider;

	/** The ordered objects. */
	private HashMap<String, org.bbaw.pdr.ae.model.view.OrderingHead> _orderedObjects;

	/**
	 * Instantiates a new aspects by markup orderer.
	 */
	public AspectsByMarkupOrderer()
	{
		_provider = Platform
				.getPreferencesService()
				.getString(CommonActivator.PLUGIN_ID,
						"PRIMARY_TAGGING_PROVIDER", AEConstants.CLASSIFICATION_AUTHORITY, null).toUpperCase(); //$NON-NLS-1$
	}

	/**
	 * Instantiates a new aspects by markup orderer.
	 * @param provider the provider
	 */
	public AspectsByMarkupOrderer(final String provider)
	{
		this._provider = provider;
	}


	
	/**
	 * Gets the label of config data.
	 * @param element the element
	 * @param type the type
	 * @param subtype the subtype
	 * @param role the role
	 * @return the label of config data
	 */
	/*private String getLabelOfConfigData(String element, final String type, final String subtype, final String role)
	{
		if (!element.startsWith("aodl:"))
		{
			element = "aodl:" + element; //$NON-NLS-1$ //$NON-NLS-2$
		}
		String label = null;
		Vector<String> providers = new Vector<String>();
		for (String s : _facade.getConfigs().keySet())
		{
			if (!s.equals(_provider))
			{
				providers.add(s);
			}
		}
		HashMap<String, ConfigData> configs = new HashMap<String, ConfigData>();
		if (element != null && type == null)
		{

			if (_facade.getConfigs().containsKey(_provider)
					&& _facade.getConfigs().get(_provider).getChildren() != null
					&& _facade.getConfigs().get(_provider).getChildren().containsKey(element))
			{
				configs.putAll(_facade.getConfigs().get(_provider).getChildren());
			}
			if (configs.get(element) != null)
			{
				label = configs.get(element).getLabel();
			}
			else
			{
				label = element;
			}
		}
		else if (element != null && type != null && subtype == null)
		{
			// System.out.println("get label for type " + type);
			for (String p : providers)
			{
				if (_facade.getConfigs().get(p).getChildren().containsKey(element))
				{
					configs.putAll(_facade.getConfigs().get(p).getChildren().get(element).getChildren());
				}
			}
			// System.out.println("markupprovider " + provider);

			if (_facade.getConfigs().containsKey(_provider)
					&& _facade.getConfigs().get(_provider).getChildren() != null
					&& _facade.getConfigs().get(_provider).getChildren().containsKey(element))
			{
				configs.putAll(_facade.getConfigs().get(_provider).getChildren().get(element).getChildren());
			}
			// System.out.println("config size2 " + configs.size());

			if (configs.get(type) != null)
			{
				label = configs.get(type).getLabel();
			}
			else
			{
				label = type;
				// System.out.println("get label for label " + label);
			}

		}
		else if (element != null && type != null && subtype != null && role == null)
		{
			for (String provider : providers)
			{
				if (_facade.getConfigs().get(provider).getChildren().containsKey(element)
						&& _facade.getConfigs().get(provider).getChildren().get(element).getChildren() != null
						&& _facade.getConfigs().get(provider).getChildren().get(element).getChildren()
								.containsKey(type))
				{
					configs.putAll(_facade.getConfigs().get(provider).getChildren().get(element).getChildren()
							.get(type).getChildren());
				}
			}
			if (_facade.getConfigs().containsKey(_provider)
					&& _facade.getConfigs().get(_provider).getChildren() != null
					&& _facade.getConfigs().get(_provider).getChildren().containsKey(element)
					&& _facade.getConfigs().get(_provider).getChildren().get(element).getChildren() != null
					&& _facade.getConfigs().get(_provider).getChildren().get(element).getChildren().containsKey(type))
			{
				configs.putAll(_facade.getConfigs().get(_provider).getChildren().get(element).getChildren().get(type)
						.getChildren());
			}
			if (configs.get(subtype) != null)
			{
				label = configs.get(subtype).getLabel();
			}
			else
			{
				label = subtype;
			}
		}
		else if (element != null && type != null && subtype != null && role != null)
		{
			for (String provider : providers)
			{
				if (_facade.getConfigs().get(provider).getChildren().containsKey(element)
						&& _facade.getConfigs().get(provider).getChildren().get(element).getChildren() != null
						&& _facade.getConfigs().get(provider).getChildren().get(element).getChildren()
								.containsKey(type)
						&& _facade.getConfigs().get(provider).getChildren().get(element).getChildren().get(type)
								.getChildren() != null
						&& _facade.getConfigs().get(provider).getChildren().get(element).getChildren().get(type)
								.getChildren().containsKey(subtype))
				{
					configs.putAll(_facade.getConfigs().get(provider).getChildren().get(element).getChildren()
							.get(type).getChildren().get(subtype).getChildren());
				}
			}
			if (_facade.getConfigs().containsKey(_provider)
					&& _facade.getConfigs().get(_provider).getChildren() != null
					&& _facade.getConfigs().get(_provider).getChildren().containsKey(element)
					&& _facade.getConfigs().get(_provider).getChildren().get(element).getChildren() != null
					&& _facade.getConfigs().get(_provider).getChildren().get(element).getChildren().containsKey(type)
					&& _facade.getConfigs().get(_provider).getChildren().get(element).getChildren().get(type)
							.getChildren() != null
					&& _facade.getConfigs().get(_provider).getChildren().get(element).getChildren().get(type)
							.getChildren().containsKey(subtype))
			{
				configs.putAll(_facade.getConfigs().get(_provider).getChildren().get(element).getChildren().get(type)
						.getChildren().get(subtype).getChildren());
			}

			if (configs.get(role) != null)
			{
				label = configs.get(role).getLabel();
			}
			else
			{
				label = role;
			}
		}
		return label;
	}*/

	
	@Override
	public Vector<OrderingHead> orderedObjects(Vector<Aspect> aspects, IProgressMonitor monitor) {
		_orderedObjects = new HashMap<String, OrderingHead>();
		// process all aspects
		for (Aspect a : aspects) { 
			Vector<String> classes = this.retrieveClasses(a);
			if (classes.size()>0) {
				// apply all its classes to this aspect
				for (String clss : classes) {
					OrderingHead group;
					// find head node for classification
					if (!_orderedObjects.containsKey(clss)) {
						group = new OrderingHead(clss);
						_orderedObjects.put(clss, group);
						Vector<String> labels = this.retrieveLabels(clss);
						group.setLabel(labels.lastElement()); //FIXME: we have to keep all labels somehow
						group.setImageString(IconsInternal.MARKUP);
					} else 
						group = _orderedObjects.get(clss);
					// assign aspect to its label
					group.addAspect(a);
				}
			}
		}
		Vector<OrderingHead> result = new Vector<OrderingHead>(_orderedObjects.values());
		Collections.sort(result, new CronOrderComparator());
		return result;
	}

	/**
	 * Retrieve a list of path expressions identifying markup classes. Those
	 * identifiers look like 'element::type::subtype::role'
	 * @param a {@link Aspect} whose annotated content is to be identified
	 * @return
	 */
	private Vector<String> retrieveClasses(Aspect a) {
		Vector<String> classes = new Vector<String>();
		// if we have markup information
		if (a.getRangeList() != null) {
			// work through every markup in aspect notification
			for (TaggingRange tag : a.getRangeList()) {
				// assemble markup element configuration path from element's attributes
				Vector<String> keys = new Vector<String>(); 
				for (String key : new String[]{
						tag.getName(), tag.getType(),
						tag.getSubtype(), tag.getRole()}) 
					if (key != null)
						keys.add(key);
				Iterator<String> iter = keys.iterator();
				String id = iter.next();
				while (iter.hasNext())
					id += "::"+iter.next();
				if (!classes.contains(id))
					classes.add(id);
			}
		} else // no markup in notification
			classes.add(NLMessages.getString("Orderer_uncategorized"));
		return classes;
	}
	
	/**
	 * Returns a list of labels for the markup classes identified in a given list.
	 * @param markupClass identifier for a certain markup class, looking like 'element::type::...'
	 * @return a {@link Vector} containing annotation identifiers of the form "element::type::subtype::role",
	 * or of whatever detail an annotation is.
	 * @see #labelElementConfigs(Vector)
	 */
	private Vector<String> retrieveLabels(String markupClass){
		Vector<String> keys = new Vector<String>(Arrays.asList(markupClass.split("::")));
		
		// retrieve labels for markup element configuration
		Vector<String> labeled = this.labelElementConfigs(keys);
		// did we found any labels for the config?
		if (labeled.size()<1)
			labeled.add(NLMessages.getString("Orderer_uncategorized")); // maybe sth. different
		return labeled; 
	}
	

		
	/**
	 * For a markup element's configuration, specified by a path of refining attributes 
	 * (and starting with the markup element's name), retrieve the correct labels from the
	 * primary(?) markup provider. If no labels are found for a certain markup configuration
	 * token, its identifier is used instead.
	 * @return list labels for identifiers on the given path through this markup element configuration.
	 */
	private Vector<String> labelElementConfigs(Vector<String> keys){
		Vector<String> labels = new Vector<String>();
		// can we even use input?
		if (keys.size()>0) {
			if (!keys.firstElement().startsWith("aodl:"))
				keys.add(0, "aodl:" + keys.remove(0)) ;
		} else
			return labels; 
		// check if there is configuration data available for our markup provider 
		if (_facade.getConfigs().containsKey(_provider) && 
				_facade.getConfigs().get(_provider).getChildren() != null) {
			// retrieve top level of configs known for our provider
			HashMap<String, ConfigData> confs = _facade.getConfigs().get(_provider).getChildren();
			// walk down configuration path
			for (String key : keys) 
				if (confs != null && confs.containsKey(key)) {
					ConfigData c = confs.get(key);
					labels.add(c.getLabel());
					confs = c.getChildren();
				} else
					labels.add(key);
		}
		return labels;
	}
	
	//@Override
/*	public final Vector<OrderingHead> _orderedObjects(final Vector<Aspect> aspects, IProgressMonitor monitor)
	{
		_markups = new HashSet<String>();
		_orderedObjects = new HashMap<String, OrderingHead>();
		HashSet<PdrId> chosenAspects = new HashSet<PdrId>();
		HashSet<Aspect> rejectedAspets = new HashSet<Aspect>();
		Aspect a;
		OrderingHead oh = null;
		String element = null;
		String type = null;
		TaggingRange taggingRange;
		for (int i = 0; i < aspects.size(); i++)
		{
			a = aspects.get(i);
			if (a.getRangeList() != null && !a.getRangeList().isEmpty())
			{
				for (int j = 0; j < a.getRangeList().size(); j++)
				{
					taggingRange = a.getRangeList().get(j);
					if (taggingRange.getName() != null && taggingRange.getType() != null)
					{
						element = taggingRange.getName();
						type = taggingRange.getType();
						if (type != null)
						{
							if (!_markups.contains(element + type))
							{
								_markups.add(element + type);
								oh = new OrderingHead();
								String label = getLabelOfConfigData(element, type, null, null);
								if (label != null)
								{
									oh.setLabel(label);
								}
								else
								{
									oh.setLabel(type);
								}
								oh.setValue(element + type);
								oh.setImageString(IconsInternal.MARKUP);
								_orderedObjects.put(element + type, oh);
							}
							_orderedObjects.get(element + type).addAspect(a); //TODO
							chosenAspects.add(a.getPdrId());
							rejectedAspets.remove(a);
						}
						else if (!chosenAspects.contains(a))
						{
							rejectedAspets.add(a);
						}
					}
				}
			}
			else
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
			oh.setLabel(NLMessages.getString("Orderer_uncategorized"));
			oh.setValue("uncategorized"); //$NON-NLS-1$
			oh.getAspects().clear();
			oh.getAspects().addAll(rejectedAspets);
			_orderedObjects.put(oh.getValue(), oh);
		}
		Vector<OrderingHead> result = new Vector<OrderingHead>(_orderedObjects.values());
		Collections.sort(result, new CronOrderComparator());
		return result;
	}*/
}
