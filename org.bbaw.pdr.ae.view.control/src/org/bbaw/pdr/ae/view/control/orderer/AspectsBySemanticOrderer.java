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

import org.bbaw.pdr.ae.common.AEConstants;
import org.bbaw.pdr.ae.common.CommonActivator;
import org.bbaw.pdr.ae.common.NLMessages;
import org.bbaw.pdr.ae.common.icons.IconsInternal;
import org.bbaw.pdr.ae.control.comparator.SemanticOrderComparator;
import org.bbaw.pdr.ae.control.facade.Facade;
import org.bbaw.pdr.ae.metamodel.PdrId;
import org.bbaw.pdr.ae.model.Aspect;
import org.bbaw.pdr.ae.model.view.OrderingHead;
import org.bbaw.pdr.ae.view.control.PDRObjectsOrderer;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;

/**
 * The Class AspectsBySemanticOrderer.
 * @author Christoph Plutte
 */
public class AspectsBySemanticOrderer implements PDRObjectsOrderer
{

	/** The _facade. */
	private Facade _facade = Facade.getInstanz();

	/** The semantic provider. */
	private String _semanticProvider;

	/** The semantics. */
	private HashSet<String> _semantics;

	/** The ordered objects. */
	private HashMap<String, OrderingHead> _orderedObjects;

	/**
	 * Instantiates a new aspects by semantic orderer.
	 */
	public AspectsBySemanticOrderer()
	{
		_semanticProvider = Platform.getPreferencesService()
				.getString(CommonActivator.PLUGIN_ID, "PRIMARY_SEMANTIC_PROVIDER", //$NON-NLS-1$
						AEConstants.CLASSIFICATION_AUTHORITY, null).toUpperCase();
	}

	/**
	 * Instantiates a new aspects by semantic orderer.
	 * @param provider the provider
	 */
	public AspectsBySemanticOrderer(final String provider)
	{
		this._semanticProvider = provider;
	}

	/**
	 * Gets the semantic label.
	 * @param provider the provider
	 * @param semantic the semantic
	 * @return the semantic label
	 */
	private String getSemanticLabel(String provider, final String semantic)
	{
		if (provider == null)
		{
			provider = _semanticProvider;
		}
		if (semantic == null)
		{
			return "semantic_error";
		}
		else if (_facade.getConfigs().get(provider) != null
				&& _facade.getConfigs().get(provider).getChildren() != null
				&& _facade.getConfigs().get(provider).getChildren().get("aodl:semanticStm") != null
				&& _facade.getConfigs().get(provider).getChildren().get("aodl:semanticStm").getChildren() != null
				&& _facade.getConfigs().get(provider).getChildren().get("aodl:semanticStm").getChildren()
						.containsKey(semantic))
		{

			return _facade.getConfigs().get(provider).getChildren().get("aodl:semanticStm").getChildren().get(semantic)
					.getLabel();
		}

		return semantic;
	}

	@Override
	public final Vector<OrderingHead> orderedObjects(final Vector<Aspect> aspects, IProgressMonitor monitor)
	{
		_semantics = new HashSet<String>();
		_orderedObjects = new HashMap<String, OrderingHead>();
		HashSet<PdrId> chosenAspects = new HashSet<PdrId>();
		HashSet<Aspect> rejectedAspets = new HashSet<Aspect>();
		Aspect a;
		OrderingHead oh = null;
		String semantic;
		String semanticLabel;
		for (int i = 0; i < aspects.size(); i++)
		{
			a = aspects.get(i);
			if (_semanticProvider != null && !_semanticProvider.equals("ALL"))
			{
				if (a != null && a.getSemanticDim() != null && a.getSemanticDim().getSemanticStms() != null)
				{
					for (int j = 0; j < a.getSemanticDim().getSemanticStms().size(); j++)
					{
						if (a.getSemanticDim().getSemanticStms().get(j).getProvider()
								.equalsIgnoreCase(_semanticProvider))
						{
							semantic = a.getSemanticDim().getSemanticStms().get(j).getLabel();
							semanticLabel = getSemanticLabel(_semanticProvider, semantic);

							if (!_semantics.contains(semanticLabel))
							{
								_semantics.add(semanticLabel);
								oh = new OrderingHead();
								oh.setLabel(semanticLabel);
								oh.setValue(semantic);
								if (semantic != null)
								{
									if (semantic.startsWith("NormName")
											|| _facade.getPersonDisplayNameTags(_semanticProvider).contains(semantic))
									{
										oh.setImageString(IconsInternal.CLASSIFICATION_NAME_NORM);
									}
									else if (semantic.equals("Name")
											|| _facade.getPersonNameTags(_semanticProvider).contains(semantic))
									{
										oh.setImageString(IconsInternal.CLASSIFICATION_NAME);
									}
									else
									{
										oh.setImageString(IconsInternal.CLASSIFICATION);
									}
								}
								_orderedObjects.put(semanticLabel, oh);
							}
							_orderedObjects.get(semanticLabel).addAspect(a);
							chosenAspects.add(a.getPdrId());
						}
					}
				}
			}
			else
			{
				for (int j = 0; j < a.getSemanticDim().getSemanticStms().size(); j++)
				{
					semantic = a.getSemanticDim().getSemanticStms().get(j).getLabel();
					semanticLabel = getSemanticLabel(a.getSemanticDim().getSemanticStms().get(j).getProvider(),
							semantic);
					if (!_semantics.contains(semanticLabel))
					{
						_semantics.add(semanticLabel);
						oh = new OrderingHead();
						oh.setLabel(semanticLabel);
						oh.setValue(semantic);
						if (semantic.startsWith("NormName")
								|| _facade.getPersonDisplayNameTags(_semanticProvider).contains(semantic))
						{
							oh.setImageString(IconsInternal.CLASSIFICATION_NAME_NORM);
						}
						else if (semantic.equals("Name")
								|| _facade.getPersonNameTags(_semanticProvider).contains(semantic))
						{
							oh.setImageString(IconsInternal.CLASSIFICATION_NAME);
						}
						else
						{
							oh.setImageString(IconsInternal.CLASSIFICATION);
						}
						_orderedObjects.put(semanticLabel, oh);
					}
					_orderedObjects.get(semanticLabel).addAspect(a);
					chosenAspects.add(a.getPdrId());
				}
			}
			if (!chosenAspects.contains(a.getPdrId()))
			{
				for (int j = 0; j < a.getSemanticDim().getSemanticStms().size(); j++)
				{
					semantic = a.getSemanticDim().getSemanticStms().get(j).getLabel();
					semanticLabel = getSemanticLabel(a.getSemanticDim().getSemanticStms().get(j).getProvider(),
							semantic);
					semanticLabel += " (" + a.getSemanticDim().getSemanticStms().get(j).getProvider() + ")";
					if (!_semantics.contains(semanticLabel))
					{
						_semantics.add(semanticLabel);
						oh = new OrderingHead();
						oh.setLabel(semanticLabel);
						oh.setValue(semantic);
						if (semantic != null)
						{
							if (semantic.startsWith("NormName")
									|| _facade.getPersonDisplayNameTags(_semanticProvider).contains(semantic))
							{
								oh.setImageString(IconsInternal.CLASSIFICATION_NAME_NORM);
							}
							else if (semantic.equals("Name")
									|| _facade.getPersonNameTags(_semanticProvider).contains(semantic))
							{
								oh.setImageString(IconsInternal.CLASSIFICATION_NAME);
							}
						}
						else
						{
							oh.setImageString(IconsInternal.CLASSIFICATION);
						}
						_orderedObjects.put(semanticLabel, oh);
					}
					_orderedObjects.get(semanticLabel).addAspect(a);
					chosenAspects.add(a.getPdrId());
				}
				if (!chosenAspects.contains(a.getPdrId()))
				{
					rejectedAspets.add(a);
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

		Vector<OrderingHead> result = new Vector<OrderingHead>(_orderedObjects.values());
		Collections.sort(result, new SemanticOrderComparator(_semanticProvider));
		if (!rejectedAspets.isEmpty())
		{
			oh = new OrderingHead();
			oh.setLabel(NLMessages.getString("View_group_all"));
			oh.setValue("uncategorized");
			oh.getAspects().addAll(rejectedAspets);
			result.add(oh);
		}
		return result;
	}

}
