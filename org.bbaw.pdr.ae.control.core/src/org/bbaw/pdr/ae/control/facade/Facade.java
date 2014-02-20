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
package org.bbaw.pdr.ae.control.facade;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Observable;
import java.util.Vector;

import javax.xml.stream.XMLStreamException;

import org.bbaw.pdr.ae.common.AEConstants;
import org.bbaw.pdr.ae.common.AEPluginIDs;
import org.bbaw.pdr.ae.common.CommonActivator;
import org.bbaw.pdr.ae.config.core.ConfigFactory;
import org.bbaw.pdr.ae.config.core.IConfigFacade;
import org.bbaw.pdr.ae.config.core.IConfigManager;
import org.bbaw.pdr.ae.config.core.IConfigRightsChecker;
import org.bbaw.pdr.ae.config.model.ConfigData;
import org.bbaw.pdr.ae.config.model.ConfigItem;
import org.bbaw.pdr.ae.config.model.DatatypeDesc;
import org.bbaw.pdr.ae.control.core.FacetProposalManager;
import org.bbaw.pdr.ae.control.core.FavoriteMarkupManager;
import org.bbaw.pdr.ae.control.core.PDRObjectDisplayNameProcessor;
import org.bbaw.pdr.ae.control.core.UserRoleSourceProvider;
import org.bbaw.pdr.ae.control.factories.IDataHandlingFactory;
import org.bbaw.pdr.ae.control.interfaces.AMainSearcher;
import org.bbaw.pdr.ae.control.interfaces.IDBManager;
import org.bbaw.pdr.ae.control.interfaces.IPdrIdService;
import org.bbaw.pdr.ae.control.interfaces.IUpdateManager;
import org.bbaw.pdr.ae.control.interfaces.IUserManager;
import org.bbaw.pdr.ae.metamodel.PdrId;
import org.bbaw.pdr.ae.model.Aspect;
import org.bbaw.pdr.ae.model.BasicPersonData;
import org.bbaw.pdr.ae.model.ComplexName;
import org.bbaw.pdr.ae.model.PdrObject;
import org.bbaw.pdr.ae.model.Person;
import org.bbaw.pdr.ae.model.ReferenceMods;
import org.bbaw.pdr.ae.model.ReferenceModsTemplate;
import org.bbaw.pdr.ae.model.RelatedItem;
import org.bbaw.pdr.ae.model.SemanticStm;
import org.bbaw.pdr.ae.model.TaggingRange;
import org.bbaw.pdr.ae.model.User;
import org.bbaw.pdr.ae.model.search.PdrQuery;
import org.bbaw.pdr.ae.model.view.Facet;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.UIJob;
import org.eclipse.ui.services.ISourceProviderService;
import org.omg.CORBA.FREE_MEM;

/**
 * Die Klasse Facade übernimmt die Funktion eines Containers und stellt
 * Schnittstellen zw. View und Controll/Modell zur Verfügung. Facade ist ein
 * Singleton und wird Multithreadsicher von der Klasse Activator.java erzeugt
 * und initialisert.
 * @author cplutte
 */

public final class Facade extends Observable implements IConfigFacade
{

	/** Singleton for standalone RCP. */
	private static volatile Facade singletonFacadeInstance;

	/** id of update manager extension. */
	private static final String UPDATEMANAGER_ID = "org.bbaw.pdr.ae.control.updateManager";

	/**
	 * Gets the instanz.
	 * @return the instanz
	 */
	public static Facade getInstanz()
	{
		if (singletonFacadeInstance == null)
		{
			synchronized (Facade.class)
			{
				if (singletonFacadeInstance == null)
				{
					singletonFacadeInstance = new Facade();
				}
			}
		}
		return singletonFacadeInstance;
	}

//	 Singleton for RAP
	// public static Facade getInstanz()
	// {
	//
	// return (Facade) SessionSingletonBase.getInstance(Facade.class);
	// }

	/**
	 * Sets the current locale.
	 */
	public static void setCurrentLocale()
	{
		Locale.getDefault();
	}

	/** The _main searcher. */
	private AMainSearcher _mainSearcher;

	/** The _id service. */
	private IPdrIdService _idService;

	/** The _db manager. */
	private IDBManager _dbManager;

	/** The _user manager. */
	private IUserManager _userManager;

	/** The _update managers. */
	private IUpdateManager[] _updateManagers;

	/** The _data handling factory. */
	private IDataHandlingFactory _dataHandlingFactory = null;
	// This is the ID from your extension point
	/** The Constant DATAHANDLING_ID. */
	private static final String DATAHANDLING_ID = "org.bbaw.pdr.ae.control.datahandlingFactory";

	/** currently selected Object - either person oder reference. */
	private PdrObject _currentPdrObject;
	/** currently selected person. */
	private Person _currentPerson;
	/** current tree object. */
	// private PdrObject currentTreeObject;
	/** currently concurring person - concurrencePerspective. */
	private Person _concurringPerson;
	/** currently selected aspect. */
	private Aspect _currentAspect;
	// /** currently selected category. */
	// private String currentCategory;
	// private Integer currentCategoryID;
	/** currently ready for copy aspect. */
	private Aspect _currentCopiedAspect;
	/** stores last Persons for select object dialog and history. */
	private Vector<Person> _lastPersons = new Vector<Person>();
	/** boolean indicates if working data has been loaded. */
	private boolean _workingDataLoaded;
	/** stores resulting Persons after search. */
	private Vector<Person> _resultingPersons = new Vector<Person>();

	/**
	 * stores all persons after first persons search. the application works on
	 * this vector of persons, further search results will be added to this
	 * vector.
	 */
	private HashMap<PdrId, Person> _allPersons = new HashMap<PdrId, Person>();

	/** The loaded aspects. */
	private HashMap<PdrId, Aspect> _loadedAspects = new HashMap<PdrId, Aspect>();
	/** stores all references. */

	private HashMap<PdrId, ReferenceMods> _allReferences;

	/** stores last Aspects for select object dialog and history. */
	private Vector<Aspect> _lastAspects = new Vector<Aspect>();

	/** selected Aspect for relation. */
	private Aspect _selectedAspect;

	/** selected Person for relation. */
	private Person _selectedPerson;

	/** stores last references. */
	private Vector<ReferenceMods> _lastReferences = new Vector<ReferenceMods>();

	/** current reference. */
	private ReferenceMods _currentReference;

	/** The requested id. */
	private PdrId _requestedId;

	/**
	 * int defines typ to object which shall be related 0 = person 1 = aspect 2
	 * = source.
	 */
	private int _relObjTyp;

	/** save last expert search. */
	private PdrQuery _lastExpertSearch;

	/** The number of all persons. */
	private int _numberOfAllPersons;
	/** saves a hashmap of all semantics present in the data. */
	private HashMap<String, ConfigData> _allSemantics;
	/** saves a hashmap of all genres present in the data. */
	private HashMap<String, ReferenceModsTemplate> _allGenres;

	/** The advanced query. */
	private PdrQuery _advancedQuery;

	/** param for identifier browser. */
	private String _requestedIdentifier;
	/** param for identifier browser. type of identifier pnd, lccn */
	private String _requestedIdentifierType;

	// private String[] _referenceGenres;

	/** The reference mods templates. */
	private HashMap<String, ReferenceModsTemplate> _referenceModsTemplates; // =
																			// new
																			// HashMap<String,
																			// ReferenceModsTemplate>();
	/** current user name. */
	private User _currentUser;

	/**
	 * hashmaps of update states of objects. 0 = normal, 1 = updated, 2 = new
	 * and not yet syncronized with repository.
	 */
	private HashMap<String, Integer> _personsUpdateState;

	/** The aspects update state. */
	private HashMap<String, Integer> _aspectsUpdateState;

	/** The references update state. */
	private HashMap<String, Integer> _referencesUpdateState;

	// /** The configs. */
	// private HashMap<String, DatatypeDesc> configsHelp;

	/** The favorite markups. */
	private HashMap<String, ConfigData> _favoriteMarkups;

	/** The facet proposals. */
	private HashMap<String, ConfigData> _facetProposals;

	/** The aspect facet proposals. */
	private HashMap<String, ConfigData> _aspectFacetProposals;
	/** Logger. */
	private static ILog iLogger = AEConstants.ILOGGER;
	/** status. */
	private IStatus _log;
	/**
	 * @return Returns the currentPerson.
	 */


	/** The _config manager. */
	private IConfigManager _configManager;

	/** The _rights checker. */
	private RightsChecker _rightsChecker = new RightsChecker();

	/** The _configs. */
	private HashMap<String, DatatypeDesc> _configs;

	/** The current tree objects. */
	private PdrObject[] _currentTreeObjects;

	private UserRoleSourceProvider _userRoleSourceProvider;

	/**
	 * Instantiates a new facade.
	 */
	private Facade()
	{
	}

	public void addIDStringToLastObjects(String objectId)
	{
		PdrId id = new PdrId(objectId);
		if (id != null)
		{
			PdrObject o = getPdrObject(id);
			if (o != null)
			{
				if (o instanceof Aspect)
				{
					addToLastAspects((Aspect)o);
				}
				else if (o instanceof Person)
				{
					addToLastPersons((Person)o);
				}
				if (o instanceof ReferenceMods)
				{
					addToLastReferences((ReferenceMods)o);
				}
			}
		}

	}

	/**
	 * Adds the to last aspects.
	 * @param ca the ca
	 */
	public void addToLastAspects(final Aspect ca)
	{
		if (!_lastAspects.contains(ca))
		{
			_lastAspects.add(ca);
		}
		else
		{
			_lastAspects.remove(ca);
			_lastAspects.add(ca);

		}
	}

	/**
	 * Adds the to last persons.
	 * @param cp the cp
	 */
	public void addToLastPersons(final Person cp)
	{
		if (!_lastPersons.contains(cp))
		{
			_lastPersons.add(cp);
		}
		else
		{
			_lastPersons.remove(cp);
			_lastPersons.add(cp);

		}

	}

	/**
	 * Adds the to last references.
	 * @param cr the cr
	 */
	public void addToLastReferences(final ReferenceMods cr)
	{
		if (!_lastReferences.contains(cr))
		{
			_lastReferences.add(cr);
		}
		else
		{
			_lastReferences.remove(cr);
			_lastReferences.add(cr);

		}
	}

	/**
	 * Delelte ref template.
	 * @param genre the genre
	 */
	public void delelteRefTemplate(final String genre)
	{
		try
		{
			_dbManager.delete(genre + ".xml", "refTemplate"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		catch (Exception e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	/**
	 * Delete aspect.
	 * @param a the a
	 */
	public void deleteAspect(final Aspect a)
	{
		PdrId pId;
		if (a.getRelationDim().getRelationStms() != null)
		{
			for (int i = 0; i < a.getRelationDim().getRelationStms().size(); i++)
			{
				if (a.getRelationDim().getRelationStms().get(i).getRelations() != null)
				{
					for (int j = 0; j < a.getRelationDim().getRelationStms().get(i).getRelations().size(); j++)
					{
						if (a.getRelationDim().getRelationStms().get(i).getRelations().get(j) != null
								&& a.getRelationDim().getRelationStms().get(i).getRelations().get(j).getObject() != null
								&& a.getRelationDim().getRelationStms().get(i).getRelations().get(j).getObject()
										.getType().equals("pdrPo"))
						{
							pId = a.getRelationDim().getRelationStms().get(i).getRelations().get(j).getObject();
							removeAspectOfPerson(_allPersons.get(pId), a);

						}

					}
				}
				else if (a.getRelationDim().getRelationStms().get(i).getSubject() != null
						&& a.getRelationDim().getRelationStms().get(i).getSubject().getType().equals("pdrPo"))
				{
				}
				pId = a.getRelationDim().getRelationStms().get(i).getSubject();
				Person p = _allPersons.get(pId);
				if (p != null)
				{
					removeAspectOfPerson(_allPersons.get(pId), a);
				}

			}
		}

		if (a.getValidation() != null && a.getValidation().getValidationStms() != null)
		{
			for (int i = 0; i < a.getValidation().getValidationStms().size(); i++)
			{
				if (a.getValidation().getValidationStms().get(i).getReference() != null)
				{
					pId = a.getValidation().getValidationStms().get(i).getReference().getSourceId();
					ReferenceMods r = _allReferences.get(pId);
					if (r != null)
					{
						r.removeAspect(a.getPdrId());
					}
				}

			}
		}
		deleteAspectFromLoadedAspects(a);
		_currentAspect = null;
		setChanged();
		notifyObservers("newNewAspect");

	}

	/**
	 * Delete aspect from loaded aspects.
	 * @param a the a
	 */
	public void deleteAspectFromLoadedAspects(final Aspect a)
	{
		if (_loadedAspects.containsKey(a.getPdrId()))
		{
			_loadedAspects.remove(a.getPdrId());
		}
	}

	/**
	 * Delete person from all persons.
	 * @param p the p
	 */
	public void deletePersonFromAllPersons(final Person p)
	{
		// allPersons.removeElement(p);
		_allPersons.remove(p.getPdrId());

		_currentPerson = null;
		setChanged();
		notifyObservers("newNewPerson");
	}

	/**
	 * Delete reference.
	 * @param r the r
	 */
	public void deleteReference(final ReferenceMods r)
	{
		_allReferences.remove(r.getPdrId());
		setChanged();
		_currentReference = null;
		notifyObservers("newNewReference");

	}

	public void fireUpdateEvent(String event)
	{
		if(event != null)
		{
			setChanged();
			notifyObservers(event);
			// if (event.equals("newPersonTreeRequiered"))
			// {
			//
			//
			// }
		}
	}

	/**
	 * Gets the advanced query.
	 * @return the advanced query
	 */
	public PdrQuery getAdvancedQuery()
	{
		return _advancedQuery;
	}

	/**
	 * Gets the all genres.
	 * @return the all genres
	 */
	public HashMap<String, ReferenceModsTemplate> getAllGenres()
	{
		if (_allGenres == null)
		{
			String[] values = null;
			try
			{
				values = getMainSearcher().getFacets("reference", "genre", null, null, null);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				values = new String[]
				{"Error"};
			}
			_allGenres = new HashMap<String, ReferenceModsTemplate>();
			for (String value : values)
			{
				// System.out.println("value " + value);
				if (this.getReferenceModsTemplates().containsKey(value))
				{
					_allGenres.put(value, this.getReferenceModsTemplates().get(value));
					// System.out.println("contains key " + value);

				}
				else
				{
					ReferenceModsTemplate ci = new ReferenceModsTemplate();
					ci.setLabel(value);
					ci.setValue(value);
					_allGenres.put(value, ci);
				}
			}
		}
		return _allGenres;
	}

	/**
	 * Gets the all persons.
	 * @return the all persons
	 */
	public HashMap<PdrId, Person> getAllPersons()
	{
		return _allPersons;
	}

	/**
	 * Gets the all persons facets.
	 * @return the all persons facets
	 */
	public Facet[] getAllPersonsFacets()
	{
		Facet[] facets = new Facet[_allPersons.size()];
		int i = 0;
		for (PdrId k : _allPersons.keySet())
		{
			facets[i] = new Facet();
			facets[i].setKey(k.toString());
			facets[i].setValue(_allPersons.get(k).getDisplayNameWithID());
			facets[i].setType(4);
			i++;
		}
		java.util.Arrays.sort(facets);
		return facets;
	}

	/**
	 * Gets the all reference facets.
	 * @return the all reference facets
	 */
	public Facet[] getAllReferenceFacets()
	{
		Facet[] facets = new Facet[_allReferences.size()];
		int i = 0;
		for (PdrId k : _allReferences.keySet())
		{
			ReferenceMods ref = _allReferences.get(k);
			facets[i] = new Facet();
			facets[i].setKey(k.toString());
			facets[i].setValue(ref.getDisplayNameWithID());
			if (ref.getGenre() != null && ref.getGenre().getGenre() != null)
			{
				facets[i].setValue2(ref.getGenre().getGenre());
			}
			facets[i].setType(5);
			i++;
		}
		java.util.Arrays.sort(facets);
		return facets;

	}

	/**
	 * Gets the all reference facets rel item filtered.
	 * @return the all reference facets rel item filtered
	 */
	public Facet[] getAllReferenceFacetsRelItemFiltered()
	{
		ArrayList<Facet> fs = new ArrayList<Facet>(_allReferences.size());
		Facet f;
		boolean hosted2 = false;
		for (PdrId k : _allReferences.keySet())
		{
			ReferenceMods ref = _allReferences.get(k);
			if (ref.getRelatedItems() != null && !ref.getRelatedItems().isEmpty())
			{
				for (RelatedItem item : ref.getRelatedItems())
				{
					if (item.getType() != null && item.getType().equals("host") && item.getId() != null)
					{
						ReferenceMods ref2 = _allReferences.get(item.getId());
						if (ref2 != null && ref2.getRelatedItems() != null && !ref2.getRelatedItems().isEmpty())
						{
							for (RelatedItem item2 : ref2.getRelatedItems())
							{
								if (item2.getType() != null && item2.getType().equals("host") && item2.getId() != null)
								{
									hosted2 = true;
									break;
								}
							}
						}
					}
					if (hosted2)
					{
						break;
					}
				}
			}
			if (!hosted2)
			{
				f = new Facet();
				f.setKey(k.toString());
				f.setValue(ref.getDisplayNameWithID());
				if (ref.getGenre() != null && ref.getGenre().getGenre() != null)
				{
					f.setValue2(ref.getGenre().getGenre());
				}
				f.setType(5);
				fs.add(f);
			}
		}
		Facet[] facets = fs.toArray(new Facet[fs.size()]);
		java.util.Arrays.sort(facets);
		return facets;
	}

	/**
	 * @return the allReferences
	 */
	public HashMap<PdrId, ReferenceMods> getAllReferences()
	{
		return _allReferences;
	}

	/**
	 * Gets the all semantics.
	 * @return the all semantics
	 */
	public HashMap<String, ConfigData> getAllSemantics()
	{
		if (_allSemantics == null)
		{
			String semanticProvider = Platform.getPreferencesService().getString(CommonActivator.PLUGIN_ID,
					"PRIMARY_SEMANTIC_PROVIDER", "PDR", null);
			String[] values = null;
			try
			{
				values = getMainSearcher().getFacets("semantic", null, null, null, null);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				
			}
			if (values == null)
			{
				values = new String[]
						{"Error"};
			}
			Vector<String> providers = new Vector<String>();
			for (String s : this.getConfigs().keySet())
			{
				if (!s.equals(semanticProvider))
				{
					providers.add(s);
				}
			}
			_allSemantics = new HashMap<String, ConfigData>();
			HashMap<String, ConfigData> configsHelp = new HashMap<String, ConfigData>();
			// ConfigItem ciAll = new ConfigItem();
			// ciAll.setValue("ALL");
			// ciAll.setLabel("ALL");
			// ciAll.setIgnore(false);
			// allSemantics.put("ALL", ciAll);

			for (String provider : providers)
			{
				if (this.getConfigs().get(provider).getChildren().containsKey("aodl:semanticStm"))
				{
					configsHelp.putAll(this.getConfigs().get(provider).getChildren().get("aodl:semanticStm")
							.getChildren());
				}
			}
			if (this.getConfigs().containsKey(semanticProvider)
					&& this.getConfigs().get(semanticProvider).getChildren() != null
					&& this.getConfigs().get(semanticProvider).getChildren().containsKey("aodl:semanticStm"))
			{
				configsHelp.putAll(this.getConfigs().get(semanticProvider).getChildren().get("aodl:semanticStm")
						.getChildren());
			}
			for (String value : values)
			{
				// System.out.println("value " + value);
				if (configsHelp.containsKey(value))
				{
					_allSemantics.put(value, configsHelp.get(value));
				}
				else
				{
					ConfigItem ci = new ConfigItem();
					ci.setLabel(value);
					ci.setValue(value);
					_allSemantics.put(value, ci);
				}
			}
		}
		return _allSemantics;
	}

	/**
	 * Gets the aspect.
	 * @param id the id
	 * @return the aspect
	 */
	public Aspect getAspect(final PdrId id)
	{
		Aspect a = _loadedAspects.get(id);
		if (a != null)
		{
			return a;
		}
		else
		{
			try
			{
				a = getMainSearcher().searchAspect(id);
				_loadedAspects.put(id, a);
				return a;

			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

		}

		return null;
	}

	/**
	 * Gets the aspect facet proposals.
	 * @return the aspect facet proposals
	 */
	public HashMap<String, ConfigData> getAspectFacetProposals()
	{
		if (_aspectFacetProposals == null || _aspectFacetProposals.isEmpty())
		{
			FacetProposalManager fmm = new FacetProposalManager();
			_aspectFacetProposals = fmm.loadAspectFacetProposals();
		}
		return _aspectFacetProposals;
	}

	/**
	 * Gets the aspects update state.
	 * @return the aspects update state
	 * @throws Exception the exception
	 */
	public HashMap<String, Integer> getAspectsUpdateState() throws Exception
	{
		if (_aspectsUpdateState == null)
		{
			_aspectsUpdateState = new HashMap<String, Integer>();
			_aspectsUpdateState.putAll(getIdService().loadObjectsUpdateState("pdrAo"));
		}
		return _aspectsUpdateState;
	}

	/**
	 * Gets the concurring person.
	 * @return the concurring person
	 */
	public Person getConcurringPerson()
	{
		return _concurringPerson;
	}

	/**
	 * @return configManager
	 * @see org.bbaw.pdr.ae.config.core.IConfigFacade#getConfigManager()
	 */
	@Override
	public IConfigManager getConfigManager()
	{
		if (_configManager == null)
		{
			_configManager = ConfigFactory.createConfigManager();
		}
		return _configManager;
	}

	/**
	 * @return rights checker
	 * @see org.bbaw.pdr.ae.config.core.IConfigFacade#getConfigRichtsChecker()
	 */
	@Override
	public IConfigRightsChecker getConfigRichtsChecker()
	{
		return _rightsChecker;
	}

	/**
	 * @return configs
	 * @see org.bbaw.pdr.ae.config.core.IConfigFacade#getConfigs()
	 */
	@Override
	public HashMap<String, DatatypeDesc> getConfigs()
	{
		if (_configs == null)
		{
			try
			{
				setConfigs(getConfigManager().getConfigs());
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return _configs;
	}

	/**
	 * get curretn aspect.
	 * @return currentAspect
	 */
	public Aspect getCurrentAspect()
	{
		return _currentAspect;
	}

	/**
	 * returns currently ready for copy aspect.
	 * @return currentCopiedAspect.
	 */
	public Aspect getCurrentCopiedAspect()
	{
		return _currentCopiedAspect;
	}

	/**
	 * Gets the current date.
	 * @return the current date
	 */
	public Date getCurrentDate()
	{
		return new java.util.Date();
	}

	/**
	 * returns current date in iso format used by pdr.
	 * @return current date
	 */
	public String getCurrentDateAsString()
	{
		java.util.Date date = new java.util.Date();
		String currentDate = AEConstants.ADMINDATE_FORMAT.format(date);
		return currentDate;
	}

	/**
	 * @return the currentPdrObject
	 */
	public PdrObject getCurrentPdrObject()
	{
		return _currentPdrObject;
	}

	/**
	 * Gets the current person.
	 * @return the current person
	 */
	public Person getCurrentPerson()
	{
		return _currentPerson;
	}

	/**
	 * Gets the current reference.
	 * @return the current reference
	 */
	public ReferenceMods getCurrentReference()
	{
		return _currentReference;
	}

	/**
	 * Gets the current tree objects.
	 * @return the current tree objects
	 */
	public PdrObject[] getCurrentTreeObjects()
	{
		return _currentTreeObjects;
	}

	/**
	 * Gets the current user.
	 * @return the current user
	 */
	public User getCurrentUser()
	{
		return _currentUser;
	}

	/**
	 * Gets the data handling factory.
	 * @return the data handling factory
	 */
	private IDataHandlingFactory getDataHandlingFactory()
	{
		// System.out.println("getDataHandlingFactory " + _dataHandlingFactory);

		if (_dataHandlingFactory == null)
		{
			IConfigurationElement[] factory = Platform.getExtensionRegistry().getConfigurationElementsFor(
					DATAHANDLING_ID);
			try
			{
				for (IConfigurationElement e : factory)
				{
					final Object o = e.createExecutableExtension("class");
					if (o instanceof IDataHandlingFactory)
					{
						_dataHandlingFactory = (IDataHandlingFactory) o;
						return _dataHandlingFactory;
					}
				}
			}
			catch (CoreException ex)
			{
				 System.out.println("CoreException");
				 System.out.println(ex.getMessage());
			}

		}
		else
		{
			return _dataHandlingFactory;
		}
		return _dataHandlingFactory;
	}

	/**
	 * Gets the dB manager.
	 * @return the dB manager
	 */
	public IDBManager getDBManager()
	{
		if (_dbManager == null)
		{
			_dbManager = getDataHandlingFactory().createDBManager();
		}
		return _dbManager;
	}

	/**
	 * Gets the facet proposals.
	 * @return the facet proposals
	 */
	public HashMap<String, ConfigData> getFacetProposals()
	{
		if (_facetProposals == null)
		{
			FacetProposalManager fmm = new FacetProposalManager();
			_facetProposals = fmm.loadFacetProposals();
		}
		return _facetProposals;
	}

	/**
	 * Gets the favorite markups.
	 * @return the favorite markups
	 */
	public HashMap<String, ConfigData> getFavoriteMarkups()
	{
		if (_favoriteMarkups == null)
		{
			FavoriteMarkupManager fmm = new FavoriteMarkupManager();
			_favoriteMarkups = fmm.loadFavoriteMarkups();
		}
		return _favoriteMarkups;
	}

	/**
	 * Gets the id service.
	 * @return the id service
	 */
	public IPdrIdService getIdService()
	{
		if (_idService == null)
		{
			_idService = getDataHandlingFactory().createIdService();
		}
		return _idService;
	}

	/**
	 * Gets the key of person norm name tag.
	 * @param tag the tag
	 * @return the key of person norm name tag
	 */
	public String getKeyOfPersonNormNameTag(final String tag)
	{
		if (_configs == null)
		{
			try
			{
				_configs = getConfigs();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		for (String s : _configs.keySet())
		{

			DatatypeDesc dtd = _configs.get(s);

			if (dtd.getUsage() != null && dtd.getUsage().getUsageDisplay() != null
					&& dtd.getUsage().getUsageDisplay().getPersonNameTag() != null
					&& dtd.getUsage().getUsageDisplay().getPersonNormNameTag().contains(tag))
			{
				// baue den key für die sprachspezifischen displayname tags
				if (tag.contains("_"))
				{
					s += "_" + (tag.substring(tag.length() - 2, tag.length()).toLowerCase());
				}
				return s;
			}
		}
		return null;
	}

	/**
	 * Gets the last aspects.
	 * @return the last aspects
	 */
	public Vector<Aspect> getLastAspects()
	{
		if (!_workingDataLoaded)
		{
			loadWorkingData();
		}
		return _lastAspects;
	}

	/**
	 * Gets the last expert search.
	 * @return the last expert search
	 */
	public PdrQuery getLastExpertSearch()
	{
		return _lastExpertSearch;
	}

	/**
	 * Gets the last objects facets.
	 * @return the last objects facets
	 */
	public Facet[] getLastObjectsFacets()
	{
		int size = _lastAspects.size() + _lastPersons.size();
		ArrayList<Facet> facets = new ArrayList<Facet>(size);
		Facet f;
		if (_lastAspects.size() > 0)
		{
			for (Aspect a : _lastAspects)
			{
				if (a != null)
				{
					f = new Facet();
					f.setValue(a.getDisplayNameWithID());
					f.setKey(a.getPdrId().toString());
					f.setType(3);
					facets.add(f);

				}
			}
		}
		if (_lastPersons.size() > 0)
		{
			for (Person p : _lastPersons)
			{
				if (p != null)
				{
					f = new Facet();
					f.setValue(p.getDisplayNameWithID());
					f.setKey(p.getPdrId().toString());
					f.setType(4);
					facets.add(f);
				}
			}
		}
		// for (ReferenceMods r : lastReferences)
		// {
		// facets[i] = new Facet();
		// facets[i].setValue(r.getDisplayNameWithID());
		// facets[i].setKey(r.getPdrId().toString());
		// facets[i].setType(5);
		// i++;
		// }
		if (facets != null && facets.size() > 0)
		{
			Collections.sort(facets);
			Facet[] fs = facets.toArray(new Facet[facets.size()]);
			return fs;
		}
		else
		{
			return null;
		}

	}

	/**
	 * Gets the last persons.
	 * @return the last persons
	 */
	public Vector<Person> getLastPersons()
	{
		if (!_workingDataLoaded)
		{
			loadWorkingData();
		}
		return _lastPersons;
	}

	/**
	 * @return the lastReferences
	 */
	public Vector<ReferenceMods> getLastReferences()
	{
		if (!_workingDataLoaded)
		{
			loadWorkingData();
		}
		return _lastReferences;
	}

	/**
	 * Gets the lazy loading.
	 * @return the lazy loading
	 */
	public Boolean getLazyLoading()
	{
		// if (_lazyLoading == null)
		// {
		// try
		// {
		// if (_mainSearcher.getNumberOfAllPersons() > 10000)
		// {
		// _lazyLoading = true;
		// }
		// else
		// {
		// _lazyLoading = false;
		// }
		// }
		// catch (Exception e)
		// {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }
		return false;
	}

	/**
	 * Gets the loaded aspects.
	 * @return the loaded aspects
	 */
	public HashMap<PdrId, Aspect> getLoadedAspects()
	{
		return _loadedAspects;
	}

	/**
	 * Gets the loaded aspects facets.
	 * @return the loaded aspects facets
	 */
	public Facet[] getLoadedAspectsFacets()
	{
		Facet[] facets = new Facet[_loadedAspects.size()];
		int i = 0;
		for (PdrId k : _loadedAspects.keySet())
		{
			facets[i] = new Facet();
			facets[i].setKey(k.toString());
			PdrObject o = _loadedAspects.get(k);
			if (o != null)
			{
				facets[i].setValue(o.getDisplayNameWithID());
			}
			else
			{
				o = getPdrObject(k);
				if (o != null)
				{
					facets[i].setValue(o.getDisplayNameWithID());
				}
			}
			facets[i].setType(3);
			i++;
		}
		java.util.Arrays.sort(facets);
		return facets;
	}

	/**
	 * Gets the main searcher.
	 * @return the main searcher
	 */
	public AMainSearcher getMainSearcher()
	{
		if (_mainSearcher == null)
		{
			_mainSearcher = getDataHandlingFactory().createMainSearcher();
		}
		return _mainSearcher;
	}

	/**
	 * Gets the number of all persons.
	 * @return the number of all persons
	 */
	public int getNumberOfAllPersons()
	{
		return _numberOfAllPersons;
	}

	/**
	 * Gets the object display name.
	 * @param id the id
	 * @return the object display name
	 */
	public String getObjectDisplayName(final PdrId id)
	{
		PdrObject o = getPdrObject(id);
		if (o != null)
		{
			return o.getDisplayName();
		}
		else if (id != null)
		{
			User u = null;
			try
			{
				u = getUserManager().getUserById(id.toString());
			}
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (u != null)
			{
				return u.getDisplayName();
			}
			return id.toString();
		}
		return "Error - ID undefined: " + id;
		
	}

	/**
	 * Gets the pdr object.
	 * @param id the id
	 * @return the pdr object
	 */
	public PdrObject getPdrObject(final PdrId id)
	{
		if (id != null && id.getType().equals("pdrPo"))
		{
			return getPerson(id);
		}
		else if (id != null && id.getType().equals("pdrAo"))
		{
			return getAspect(id);
		}
		else if (id != null && id.getType().equals("pdrRo"))
		{
			return getReference(id);
		}
		return null;
	}

	/**
	 * Gets the person.
	 * @param id the id
	 * @return the person
	 */
	public Person getPerson(final PdrId id)
	{
		// FIXME
		return _allPersons.get(id);
		// if (allPersons.containsKey(id))
		// {
		// return allPersons.get(id);
		// }
		// else if (id.getType().equals("pdrPo"))
		// {
		// Person p;
		// try {
		// p = _mainSearcher.getPersonById(id);
		// allPersons.put(p.getPdrId(), p);
		// return p;
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }
		// return null;

	}

	/**
	 * @param provider semantic provider
	 * @return vector of person display name tags
	 * @see org.bbaw.pdr.ae.config.core.IConfigFacade#getPersonDisplayNameTags(java.lang.String)
	 */
	@Override
	public Vector<String> getPersonDisplayNameTags(final String provider)
	{
		if (_configs == null)
		{
			try
			{
				_configs = getConfigManager().getConfigs();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		Vector<String> tags = new Vector<String>(5);
		if (provider != null && _configs.containsKey(provider))
		{
			DatatypeDesc dtd = _configs.get(provider);
			if (dtd != null && dtd.getUsage() != null && dtd.getUsage().getUsageDisplay() != null
					&& dtd.getUsage().getUsageDisplay().getPersonNormNameTag() != null)
			{
				for (String tag : dtd.getUsage().getUsageDisplay().getPersonNormNameTag())
				{
					if (!tags.contains(tag))
					{
						tags.add(tag);
					}

				}
			}
		}
		else
		{
			for (String s : _configs.keySet())
			{
				DatatypeDesc dtd = _configs.get(s);
				if (dtd.getUsage() != null && dtd.getUsage().getUsageDisplay() != null
						&& dtd.getUsage().getUsageDisplay().getPersonNormNameTag() != null)
				{
					for (String tag : dtd.getUsage().getUsageDisplay().getPersonNormNameTag())
					{
						if (!tags.contains(tag))
						{
							tags.add(tag);
						}

					}
				}
			}
		}
		if (tags.isEmpty())
		{
			tags.add("NormName_DE");
			tags.add("NormName_EN");
			tags.add("NormName_FR");
			tags.add("NormName_IT");

		}
		return tags;
	}

	/**
	 * @param provider semantic provider
	 * @return vector of person name tags
	 * @see org.bbaw.pdr.ae.config.core.IConfigFacade#getPersonNameTags(java.lang.String)
	 */
	@Override
	public Vector<String> getPersonNameTags(final String provider)
	{
		if (_configs == null)
		{
			try
			{
				_configs = getConfigManager().getConfigs();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		Vector<String> tags = new Vector<String>(5);
		if (provider != null && _configs.containsKey(provider))
		{
			DatatypeDesc dtd = _configs.get(provider);
			if (dtd != null && dtd.getUsage() != null && dtd.getUsage().getUsageDisplay() != null
					&& dtd.getUsage().getUsageDisplay().getPersonNameTag() != null)
			{
				for (String tag : dtd.getUsage().getUsageDisplay().getPersonNameTag())
				{
					if (!tags.contains(tag))
					{
						tags.add(tag);
					}

				}
			}
		}
		else
		{
			for (String s : _configs.keySet())
			{
				DatatypeDesc dtd = _configs.get(s);
				if (dtd.getUsage() != null && dtd.getUsage().getUsageDisplay() != null
						&& dtd.getUsage().getUsageDisplay().getPersonNameTag() != null)
				{
					for (String tag : dtd.getUsage().getUsageDisplay().getPersonNameTag())
					{
						if (!tags.contains(tag))
						{
							tags.add(tag);
						}

					}
				}
			}
		}
		if (tags.isEmpty())
		{
			tags.add("Name");
		}
		return tags;
	}

	/**
	 * Gets the persons update state.
	 * @return the persons update state
	 * @throws Exception the exception
	 */
	public HashMap<String, Integer> getPersonsUpdateState() throws Exception
	{
		if (_personsUpdateState == null)
		{
			_personsUpdateState = new HashMap<String, Integer>();
			_personsUpdateState.putAll(getIdService().loadObjectsUpdateState("pdrPo"));
		}
		return _personsUpdateState;
	}

	/**
	 * Gets the reference.
	 * @param id the id
	 * @return the reference
	 */
	public ReferenceMods getReference(final PdrId id)
	{
		if (_allReferences != null)
		{
			return _allReferences.get(id);
		}
		else
		{
			return null;
		}

	}

	/**
	 * Gets the reference mods templates.
	 * @return the reference mods templates
	 */
	public HashMap<String, ReferenceModsTemplate> getReferenceModsTemplates()
	{
		if (_referenceModsTemplates == null)
		{
			try
			{
				_referenceModsTemplates = getMainSearcher().searchAllReferenceTemplates();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return _referenceModsTemplates;
	}

	/**
	 * Gets the references update state.
	 * @return the references update state
	 * @throws Exception the exception
	 */
	public HashMap<String, Integer> getReferencesUpdateState() throws Exception
	{
		if (_referencesUpdateState == null)
		{
			_referencesUpdateState = new HashMap<String, Integer>();
			_referencesUpdateState.putAll(getIdService().loadObjectsUpdateState("pdrRo"));
		}
		return _referencesUpdateState;
	}

	/**
	 * Gets the rel obj typ.
	 * @return the rel obj typ
	 */
	public int getRelObjTyp()
	{
		return _relObjTyp;
	}

	/**
	 * @return the requestedId
	 */
	public PdrId getRequestedId()
	{
		return _requestedId;
	}

	/**
	 * Gets the requested identifier.
	 * @return the requested identifier
	 */
	public String getRequestedIdentifier()
	{
		return _requestedIdentifier;
	}

	/**
	 * Gets the requested identifier type.
	 * @return the requested identifier type
	 */
	public String getRequestedIdentifierType()
	{
		return _requestedIdentifierType;
	}

	/**
	 * Gets the resulting persons.
	 * @return the resulting persons
	 */
	public Vector<Person> getResultingPersons()
	{
		return _resultingPersons;
	}

	/**
	 * Gets the selected aspect.
	 * @return the selected aspect
	 */
	public Aspect getSelectedAspect()
	{
		return _selectedAspect;
	}

	/**
	 * Gets the selected person.
	 * @return the selected person
	 */
	public Person getSelectedPerson()
	{
		return _selectedPerson;
	}

	/**
	 * Gets the update managers.
	 * @return the update managers
	 */
	public IUpdateManager[] getUpdateManagers()
	{
		if (_updateManagers == null)
		{
			IConfigurationElement[] managers = Platform.getExtensionRegistry().getConfigurationElementsFor(
					UPDATEMANAGER_ID);
			Vector<IUpdateManager> helps = new Vector<IUpdateManager>(managers.length);
			try
			{
				for (IConfigurationElement e : managers)
				{
					final Object o = e.createExecutableExtension("class");
					if (o instanceof IUpdateManager)
					{
						helps.add((IUpdateManager) o);
					}

				}
				_updateManagers = helps.toArray(new IUpdateManager[helps.size()]);
			}
			catch (CoreException ex)
			{
				// System.out.println("CoreException");
				System.out.println(ex.getMessage());
			}
		}
		else
		{
			return _updateManagers;
		}
		return _updateManagers;
	}

	/**
	 * Gets the user manager.
	 * @return the user manager
	 */
	public IUserManager getUserManager()
	{
		if (_userManager == null)
		{
			IDataHandlingFactory factory = getDataHandlingFactory();
			if (factory != null) {
				_userManager = factory.createUserManager();
			} else {
				System.out.println("Facade Error: No Datahandling Factory found");
			}
		}
		return _userManager;
	}

	private UserRoleSourceProvider getUserRoleSourceProvider()
	{
		if (_userRoleSourceProvider == null)
		{
			ISourceProviderService service = (ISourceProviderService) PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getService(ISourceProviderService.class);
			_userRoleSourceProvider = (UserRoleSourceProvider) service
					.getSourceProvider(AEPluginIDs.SOURCE_PARAMETER_USER_MAY_DELETE);
		}
		return _userRoleSourceProvider;
	}

	/**
	 * Insert aspect at person.
	 * @param ca the ca
	 * @param pId the id
	 * @param newAspect the new aspect
	 */
	private boolean insertAspectAtPerson(final Aspect ca, final PdrId pId, final boolean newAspect)
	{
		boolean newBPD = false;
		Person p = _allPersons.get(pId);
		if (p != null)
		{
			if (!p.getAspectIds().contains(ca.getPdrId()))
			{
				p.getAspectIds().add(ca.getPdrId());
			}
			if (ca.getSemanticDim() != null && ca.getSemanticDim().getSemanticStms() != null)
			{
				for (SemanticStm sStm : ca.getSemanticDim().getSemanticStms())
				{
					if (sStm != null)
					{
						if (getPersonDisplayNameTags(null).contains(sStm.getLabel())
								|| sStm.getLabel().startsWith("NormName"))
						{
							_log = new Status(IStatus.INFO, CommonActivator.PLUGIN_ID,
									"Facade insert aspect with displayname at person set to: " + p.getPdrId().toString());
							iLogger.log(_log);
							if (setBasicPersonData(p, ca, newAspect, true, sStm.getLabel()))
							{
								newBPD = true;
							}
	
						}
						else if (getPersonNameTags(null).contains(sStm.getLabel()))
						{
							_log = new Status(IStatus.INFO, CommonActivator.PLUGIN_ID,
									"Facade insert aspect with name at person set to: " + p.getPdrId().toString());
							iLogger.log(_log);
							if (setBasicPersonData(p, ca, newAspect, false, sStm.getLabel()))
							{
								newBPD = true;
							}

						}
						else if (sStm.getLabel().equals("biographicalData"))
						{
							_log = new Status(IStatus.INFO, CommonActivator.PLUGIN_ID,
									"Facade insert aspect with biographical data at person set to: "
											+ p.getPdrId().toString());
							iLogger.log(_log);
							if (setBasicPersonData(p, ca, newAspect, false, sStm.getLabel()))
							{
								newBPD = true;
							}

						}
						else if (sStm.getLabel().equals("principalDescription"))
						{
							_log = new Status(IStatus.INFO, CommonActivator.PLUGIN_ID,
									"Facade insert aspect with principal description at person set to: "
											+ p.getPdrId().toString());
							iLogger.log(_log);
							if (setBasicPersonData(p, ca, newAspect, false, sStm.getLabel()))
							{
								newBPD = true;
							}

						}
					}
				}
			}
			// System.out.println("aspect to all persons added");
		}
		return newBPD;
	}

	// /**
	// * @param _referenceGenres the _referenceGenres to set
	// */
	// public void setReferenceGenres(String[] _referenceGenres) {
	// this._referenceGenres = _referenceGenres;
	// }
	//
	// /**
	// * @return the _referenceGenres
	// */
	// public String[] getReferenceGenres()
	// {
	// return allSemantics;
	// if (_referenceGenres == null)
	// {
	// try {
	// _referenceGenres = new MainSearcher().getFacets("refTemplate", null,
	// null, null, null);
	// } catch (XQException e) {
	// e.printStackTrace();
	// }
	// }
	// return _referenceGenres;
	// }

	/**
	 * Insert aspect at reference.
	 * @param ca the ca
	 * @param pId the id
	 * @param newAspect the new aspect
	 */
	private void insertAspectAtReference(final Aspect ca, final PdrId pId, final boolean newAspect)
	{
		ReferenceMods r = _allReferences.get(pId);
		if (r != null && !r.getAspectIds().contains(ca.getPdrId()))
		{
			r.getAspectIds().add(ca.getPdrId());
		}
	}

	/**
	 * Insert aspect by related person.
	 * @param ca the ca
	 * @param newAspect the new aspect
	 */
	private boolean insertAspectByRelatedPerson(final Aspect ca, final boolean newAspect)
	{
		PdrId pId = null;
		boolean newBPD = false;
		if (ca.getRelationDim().getRelationStms() != null)
		{
			for (int i = 0; i < ca.getRelationDim().getRelationStms().size(); i++)
			{
				if (ca.getRelationDim().getRelationStms().get(i).getRelations() != null)
				{
					for (int j = 0; j < ca.getRelationDim().getRelationStms().get(i).getRelations().size(); j++)
					{
						if (ca.getRelationDim().getRelationStms().get(i).getRelations().get(j) != null
								&& ca.getRelationDim().getRelationStms().get(i).getRelations().get(j).getObject() != null
								&& ca.getRelationDim().getRelationStms().get(i).getRelations().get(j).getObject()
										.getType().equals("pdrPo"))
						{
							pId = ca.getRelationDim().getRelationStms().get(i).getRelations().get(j).getObject();
							if (insertAspectAtPerson(ca, pId, newAspect))
							{
								newBPD = true;
							}

						}

					}
				}
				else if (ca.getRelationDim().getRelationStms().get(i).getSubject() != null
						&& ca.getRelationDim().getRelationStms().get(i).getSubject().getType().equals("pdrPo"))
				{
				}
				pId = ca.getRelationDim().getRelationStms().get(i).getSubject();
				if (insertAspectAtPerson(ca, pId, newAspect))
				{
					newBPD = true;
				}
			}
		}
		return newBPD;

	}

	/**
	 * Insert aspect by related reference.
	 * @param ca the ca
	 * @param newAspect the new aspect
	 */
	private void insertAspectByRelatedReference(final Aspect ca, final boolean newAspect)
	{
		PdrId pId = null;
		if (ca.getValidation().getValidationStms() != null)
		{
			for (int i = 0; i < ca.getValidation().getValidationStms().size(); i++)
			{
				if (ca.getValidation().getValidationStms().get(i).getReference() != null)
				{
					pId = ca.getValidation().getValidationStms().get(i).getReference().getSourceId();
					if (pId != null)
					{
						insertAspectAtReference(ca, pId, newAspect);
					}
				}

			}
		}

	}

	/**
	 * Insert aspect into cache.
	 * @param ca the ca
	 * @param newAspect the new aspect
	 */
	private boolean insertAspectIntoCache(final Aspect ca, final boolean newAspect)
	{
		boolean newA = newAspect;
		boolean newBPD = false;
		_loadedAspects.put(ca.getPdrId(), ca);
		newBPD = insertAspectByRelatedPerson(ca, newA);
		if (ca.getValidation() != null && ca.getValidation().getValidationStms().size() > 0)
		{
			insertAspectByRelatedReference(ca, newA);
		}
		notifyObservers("newAspect");
		notifyObservers("newAspects");
		return newBPD;

	}

	/**
	 * Insert person into all persons.
	 * @param cp the cp
	 * @param newPerson the new person
	 */
	private void insertPersonIntoAllPersons(final Person cp, final boolean newPerson)
	{
		if (_allPersons == null)
		{
			_allPersons = new HashMap<PdrId, Person>();
		}
		_allPersons.put(cp.getPdrId(), cp);

	}

	/**
	 * Insert reference into all referencess.
	 * @param cr the cr
	 * @param newReference the new reference
	 */
	private void insertReferenceIntoAllReferencess(final ReferenceMods cr, final boolean newReference)
	{
		if (_allReferences == null)
		{
			_allReferences = new HashMap<PdrId, ReferenceMods>();
		}

		_allReferences.put(cr.getPdrId(), cr);

	}

	/**
	 * Checks if is person name tag.
	 * @param tag the tag
	 * @return true, if is person name tag
	 */
	public boolean isPersonNameTag(final String tag)
	{
		if (_configs == null)
		{
			try
			{
				_configs = getConfigs();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		for (String s : _configs.keySet())
		{
			DatatypeDesc dtd = _configs.get(s);
			if (dtd.getUsage() != null && dtd.getUsage().getUsageDisplay() != null
					&& dtd.getUsage().getUsageDisplay().getPersonNameTag() != null
					&& dtd.getUsage().getUsageDisplay().getPersonNameTag().contains(tag))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * @param dir
	 * @see org.bbaw.pdr.ae.config.core.IConfigFacade#loadLocalConfigBackup(java.lang.String)
	 */
	@Override
	public void loadLocalConfigBackup(final String dir)
	{
		// person
		String col = "config";
		String subDir = dir + AEConstants.FS + col;

		subDir = subDir + AEConstants.FS;

		getDBManager().createDBFromDir(col, subDir);

		refreshAllData();
	}

	/**
	 * Load working data.
	 */
	private void loadWorkingData()
	{

		Person p = null;
		ReferenceMods r = null;
		if (!_workingDataLoaded)
		{
			for (int i = 0; i < 12; i++)
			{
				p = getPerson(new PdrId(Platform.getPreferencesService().getString("org.bbaw.pdr.ae.common",
						"lastPerson" + i, "", null)));
				if (p != null)
				{
					if (!_lastPersons.contains(p))
					{
						_lastPersons.add(p);
					}
				}
				else
				{
					break;
				}
			}
			// TODO das Laden alter Aspekte versucht ständige Fehler und eine
			// Schleife.
			// for (int i = 0; i < 12; i++)
			// {
			// a = getAspect(new
			// PdrId(Activator.getDefault().getPreferenceStore().getString("lastAspect"
			// + i)));
			// if (a != null)
			// {
			// if (!lastAspects.contains(a)) lastAspects.add(a);
			// }
			// else
			// {
			// break;
			// }
			// }
			for (int i = 0; i < 12; i++)
			{
				r = getReference(new PdrId(Platform.getPreferencesService().getString(CommonActivator.PLUGIN_ID,
						"lastReference" + i, "", null)));
				if (r != null)
				{
					if (!_lastReferences.contains(r))
					{
						_lastReferences.add(r);
					}
				}
				else
				{
					break;
				}
			}
		}
		_workingDataLoaded = true;
		_log = new Status(IStatus.INFO, CommonActivator.PLUGIN_ID, "Facade - working data loaded: "
				+ _workingDataLoaded);
		iLogger.log(_log);
	}

	/**
	 * Process user role.
	 */
	public void processUserRole()
	{
		if (_currentUser != null && _currentUser.getAuthentication() != null
				&& _currentUser.getAuthentication().getRoles() != null)
		{
			if (_currentUser.getAuthentication().getRoles().contains("pdrAdmin"))
			{
			}
			else if (_currentUser.getAuthentication().getRoles().contains("admin"))
			{
			}
			else if (_currentUser.getAuthentication().getRoles().contains("user"))
			{
			}
			else
			{
			}

			// FIXME in rcp einkommentieren, in rap raus?
			// if (PlatformUI.isWorkbenchRunning())
			// {
			// IWorkbench w = PlatformUI.getWorkbench();
			// // get the window (which is a IServiceLocator)
			// if (w != null)
			// {
			// IWorkbenchWindow window = w.getActiveWorkbenchWindow();
			// // get the service
			// ISourceProviderService service = (ISourceProviderService)
			// window.getService(ISourceProviderService.class);
			// // get our source provider by querying by the variable name
			// UserRoleSourceProvider userRoleSourceProvider =
			// (UserRoleSourceProvider) service
			// .getSourceProvider(AEPluginIDs.SOURCE_PARAMETER_USER_ROLE);
			// // set the value
			// userRoleSourceProvider.setUserRole(userRole);
			// }
			//
			// }
		}
	}

	/**
	 * Refresh all data.
	 */
	public void refreshAllData()
	{
		_workingDataLoaded = false;
		_allSemantics = null;
		_currentAspect = null;
		setCurrentPerson(null);
		setCurrentReferenceToNull();
		setConcurringPerson(null);
		setCurrentTreeObjects(null);
		setReferenceModsTemplates(null);

		_lastPersons.clear();
		_lastAspects.clear();
		_lastReferences.clear();

		_configs = null;
		_personsUpdateState = null;
		_aspectsUpdateState = null;
		_referencesUpdateState = null;
		_aspectFacetProposals = null;
		_facetProposals = null;

		_loadedAspects.clear();
		_allPersons = null;
		UIJob job = new UIJob("allPersons")
		{

			@Override
			public IStatus runInUIThread(final IProgressMonitor monitor)
			{
				try
				{
					setAllPersons(_mainSearcher.searchAllPersons());
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				if (monitor.isCanceled())
				{
					return Status.CANCEL_STATUS;
				}

				return Status.OK_STATUS;
			}
		};
		job.setUser(true);
		job.schedule();
		_allReferences = null;
		UIJob job2 = new UIJob("allReferences")
		{

			@Override
			public IStatus runInUIThread(final IProgressMonitor monitor)
			{
				try
				{
					setAllReferences(_mainSearcher.searchAllReferences());
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				if (monitor.isCanceled())
				{
					return Status.CANCEL_STATUS;
				}

				return Status.OK_STATUS;
			}
		};
		job2.setUser(true);
		job2.schedule();

		if (_allPersons == null)
		{
			_allPersons = new HashMap<PdrId, Person>();
		}
		if (_allReferences == null)
		{
			_allReferences = new HashMap<PdrId, ReferenceMods>();
		}
		setChanged();
		notifyObservers("refreshAll");
		_log = new Status(IStatus.INFO, CommonActivator.PLUGIN_ID, "Facade - all data refreshed.");
		iLogger.log(_log);

	}

	/**
	 * Removes the aspect of person.
	 * @param p the person
	 * @param ca the current aspect
	 */
	private void removeAspectOfPerson(final Person p, final Aspect ca)
	{
		boolean newBPD = false;
		if (p != null)
		{
			if (!p.getAspectIds().contains(ca.getPdrId()))
			{
				p.removeAspect(ca.getPdrId());
			}
			if (ca.getSemanticDim() != null && ca.getSemanticDim().getSemanticStms() != null)
			{
				for (SemanticStm sStm : ca.getSemanticDim().getSemanticStms())
				{
					if (getPersonDisplayNameTags(null).contains(sStm.getLabel()))
					{
						_log = new Status(IStatus.INFO, CommonActivator.PLUGIN_ID,
								"Facade remove aspect with displayname at person set to: " + p.getPdrId().toString());
						iLogger.log(_log);
						if (removeFromBasicPersonData(p, ca, true, sStm.getLabel()))
						{
							newBPD = true;
						}

					}
					else if (getPersonNameTags(null).contains(sStm.getLabel()))
					{
						_log = new Status(IStatus.INFO, CommonActivator.PLUGIN_ID,
								"Facade remove aspect with name at person set to: " + p.getPdrId().toString());
						iLogger.log(_log);
						if (removeFromBasicPersonData(p, ca, false, sStm.getLabel()))
						{
							newBPD = true;
						}
					}
					else if (sStm.getLabel().equals("biographicalData"))
					{
						_log = new Status(IStatus.INFO, CommonActivator.PLUGIN_ID,
								"Facade remove aspect with biographical data at person set to: "
										+ p.getPdrId().toString());
						iLogger.log(_log);
						if (removeFromBasicPersonData(p, ca, false, sStm.getLabel()))
						{
							newBPD = true;
						}
					}
					else if (sStm.getLabel().equals("principalDescription"))
					{
						_log = new Status(IStatus.INFO, CommonActivator.PLUGIN_ID,
								"Facade remove aspect with principal description at person set to: "
										+ p.getPdrId().toString());
						iLogger.log(_log);
						if (removeFromBasicPersonData(p, ca, false, sStm.getLabel()))
						{
							newBPD = true;
						}
					}
				}
			}
			// System.out.println("aspect to all persons added");
		}
		if (newBPD)
		{
			setChanged();
			notifyObservers("newPersonTreeRequiered");
		}

	}

	/**
	 * Removes the from basic person data.
	 * @param p the person
	 * @param ca the current aspect
	 * @param isNorm the is semantic classification of aspect norm name
	 * @param label the label
	 * @return
	 */
	private boolean removeFromBasicPersonData(final Person p, final Aspect ca, final boolean isNorm, final String label)
	{
		if (p.getBasicPersonData() != null)
		{
			if (label.equals("biographicalData"))
			{
				if (ca.getRangeList() != null && ca.getNotification() != null)
				{
					for (TaggingRange tr : ca.getRangeList())
					{
						if (tr.getName().equals("date"))
						{
							if (tr.getType().equals("lifespan"))
							{
								p.getBasicPersonData().setBeginningOfLife(null);
								p.getBasicPersonData().setEndOfLife(null);
							}
							else if (tr.getType().equals("beginningOfLife"))
							{
								if ("birth".equals(tr.getSubtype()))
								{
									if (tr.getWhen() != null)
									{
										p.getBasicPersonData().setBeginningOfLife(null);
									}
									else if (tr.getNotBefore() != null)
									{
										p.getBasicPersonData().setBeginningOfLife(null);
									}
									else if (tr.getNotAfter() != null)
									{
										p.getBasicPersonData().setBeginningOfLife(null);
									}
								}
							}
							else if (tr.getType().equals("endOfLife"))
							{
								if ("death".equals(tr.getSubtype()))
								{
									if (tr.getWhen() != null)
									{
										p.getBasicPersonData().setEndOfLife(null);
									}
									else if (tr.getNotBefore() != null)
									{
										p.getBasicPersonData().setEndOfLife(null);
									}
									else if (tr.getNotAfter() != null)
									{
										p.getBasicPersonData().setEndOfLife(null);
									}
								}
							}
						}
					}
				}
			}
			else if (label.equals("principalDescription"))
			{
				if (ca.getRangeList() != null && ca.getNotification() != null)
				{
					for (TaggingRange tr : ca.getRangeList())
					{
						if (tr.getName().equals("name"))
						{
							p.getBasicPersonData()
									.getDescriptions()
									.remove(ca.getNotification().substring(tr.getStart(),
											tr.getStart() + tr.getLength()));
						}
					}
				}
			}
			else if (p.getBasicPersonData().getComplexNames() != null)
			{
				ComplexName cn = new ComplexName();
				cn.setForeName("");
				cn.setSurName("");
				cn.setNameLink("");
				cn.setGenName("");
				cn.setOrgName("");
				// System.out.println("setBasicPersonData");
				if (ca.getRangeList() != null && ca.getNotification() != null)
				{
					for (TaggingRange tr : ca.getRangeList())
					{
						if (tr.getName().equals("persName"))
						{
							if (tr.getType().equals("surname"))
							{
								cn.setSurName(ca.getNotification().substring(tr.getStart(),
										tr.getStart() + tr.getLength()));
							}
							else if (tr.getType().equals("forename"))
							{
								cn.setForeName(ca.getNotification().substring(tr.getStart(),
										tr.getStart() + tr.getLength()));
							}
							else if (tr.getType().equals("nameLink"))
							{
								cn.setNameLink(ca.getNotification().substring(tr.getStart(),
										tr.getStart() + tr.getLength()));
							}
							else if (tr.getType().equals("genName"))
							{
								cn.setGenName(ca.getNotification().substring(tr.getStart(),
										tr.getStart() + tr.getLength()));
							}
						}
						if (tr.getName().equals("orgName"))
						{
							cn.setOrgName(ca.getNotification().substring(tr.getStart(), tr.getStart() + tr.getLength()));
						}
					}
				}
				if (cn.getSurName().trim().length() == 0 && cn.getForeName().trim().length() == 0
						&& ca.getNotification() != null)
				{
					cn.setSurName(ca.getNotification().trim());
					// System.out.println("cn surname: " +
					// ca.getNotification().trim());
				}
				p.getBasicPersonData().getComplexNames().remove(cn);

				if (isNorm)
				{
					p.getBasicPersonData().getDisplayNames().remove(getKeyOfPersonNormNameTag(label));
					// System.out.println("put key: " +
					// getKeyOfPersonNormNameTag(label) + " " +
					// cn.getSurName());

				}
			}
			String oldName = new String(p.getDisplayName());
			PDRObjectDisplayNameProcessor pdrDisplayNameProc = new PDRObjectDisplayNameProcessor();
			pdrDisplayNameProc.processDisplayName(p);
			return !oldName.equals(p.getDisplayName());
		}
		return false;
	}

	/**
	 * Save aspect.
	 * @param ca the current aspect
	 * @throws Exception the exception
	 */
	public void saveAspect(final Aspect ca) throws Exception
	{
		boolean newAspect = false;
		if (ca != null)
		{
			if (ca.isNew())
			{
				newAspect = true;
				ca.setNew(false);
			}
			if (newAspect || ca.isDirty())
			{
				ca.setDirty(false);
				if (saveAspectInternal(ca)) // if BasicPersonData changed,
											// update tree
				{
					setChanged();
					notifyObservers("newPersonTreeRequiered");
				}
				setChanged();
				notifyObservers("newNewAspect");
			}
			setCurrentAspect(ca);
		}
	}

	private boolean saveAspectInternal(Aspect ca) throws Exception
	{
		PDRObjectDisplayNameProcessor pdrDisplayNameProc = new PDRObjectDisplayNameProcessor();
		pdrDisplayNameProc.processDisplayName(ca);
		boolean newAspect = false;
		boolean newBPD = false;
		if (ca != null)
		{
			newBPD = insertAspectIntoCache(ca, newAspect);
			getAspectsUpdateState().put(ca.getPdrId().toString(), 2);
			getDBManager().saveToDB(ca);
			ca.setDirty(false);
			ca.setNew(false);
		}
		return newBPD;

	}

	public void savePdrObjects(Vector<Aspect> dirtyAspects) throws Exception
	{
		boolean newBPD = false;
		for (Aspect a : dirtyAspects)
		{
			if (a.isDirty() || a.isNew())
			{
				if (saveAspectInternal(a))
				{
					newBPD = true;
				}
			}
		}
		if (newBPD) // if BasicPersonData changed,
		// update tree
		{
			setChanged();
			notifyObservers("newPersonTreeRequiered");
		}
		setChanged();
		notifyObservers("newNewAspect");
	}

	/**
	 * Save person.
	 * @param cp the cp
	 */
	public void savePerson(final Person cp) throws Exception
	{
		PDRObjectDisplayNameProcessor pdrDisplayNameProc = new PDRObjectDisplayNameProcessor();
		pdrDisplayNameProc.processDisplayName(cp);
		setCurrentPerson(cp);

		boolean newPerson = false;
		if (cp != null)
		{
			if (cp.isNew())
			{
				newPerson = true;
				cp.setNew(false);
			}
			if (newPerson || cp.isDirty())
			{
				setChanged();
				cp.setDirty(false);
				insertPersonIntoAllPersons(cp, newPerson);
				if (_personsUpdateState != null)
				{
					_personsUpdateState.put(cp.getPdrId().toString(), 2);
				}
				notifyObservers("newNewPerson");
				getDBManager().saveToDB(cp);
			}

		}
	}

	/**
	 * Save reference.
	 * @param cr the cr
	 * @throws Exception
	 */
	public void saveReference(final ReferenceMods cr) throws Exception
	{
		PDRObjectDisplayNameProcessor pdrDisplayNameProc = new PDRObjectDisplayNameProcessor();
		pdrDisplayNameProc.processDisplayName(cr);
		pdrDisplayNameProc.processDisplayNameLong(cr);

		setCurrentReference(cr);
		boolean newReference = false;
		if (cr != null)
		{
			if (cr.isNew())
			{
				newReference = true;
				cr.setNew(false);
			}
			if (newReference || cr.isDirty())
			{
				cr.setDirty(false);
				insertReferenceIntoAllReferencess(cr, newReference);
				if (cr.getRelatedItems() != null && cr.getRelatedItems().size() > 0)
				{
					String id = cr.getRelatedItems().firstElement().getId();
					ReferenceMods host = _allReferences.get(id);
					if (host != null && host.getHostedReferences() != null
							&& !host.getHostedReferences().contains(cr.getPdrId().toString()))
					{
						host.getHostedReferences().add(cr.getPdrId().toString());
					}
				}
				if (_referencesUpdateState != null)
				{
					_referencesUpdateState.put(cr.getPdrId().toString(), 2);
				}
				getDBManager().saveToDB(cr);
				setChanged();
				notifyObservers("newNewReference");
			}
		}
	}

	/**
	 * Save reference templates.
	 */
	public void saveReferenceTemplates()
	{
		try
		{
			getDBManager().saveReferenceTemplateToDB(getReferenceModsTemplates());
		}
		catch (XMLStreamException e)
		{
			e.printStackTrace();

		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Sets the advanced query.
	 * @param advancedQuery the new advanced query
	 */
	public void setAdvancedQuery(final PdrQuery advancedQuery)
	{
		this._advancedQuery = advancedQuery;
		setChanged();

		notifyObservers("newAdvancedQuery");
	}

	/**
	 * Sets the all persons.
	 * @param allPersons the all persons
	 */
	public void setAllPersons(final HashMap<PdrId, Person> allPersons)
	{
		this._allPersons = allPersons;
		setChanged();
		notifyObservers("allPersons");
	}

	/**
	 * @param allReferences the allReferences to set
	 */
	public void setAllReferences(final HashMap<PdrId, ReferenceMods> allReferences)
	{
		this._allReferences = allReferences;
		if (allReferences == null)
		{
			_allReferences = new HashMap<PdrId, ReferenceMods>();
		}
		PDRObjectDisplayNameProcessor nameProc = new PDRObjectDisplayNameProcessor();
		for (ReferenceMods r : allReferences.values())
		{
			nameProc.processDisplayName(r);
			nameProc.processDisplayNameLong(r);
		}
		setChanged();
		notifyObservers("allReferences");
		// System.out.println("refs size " + allReferences.size());
	}

	/**
	 * Sets the all semantics.
	 * @param allSemantics the all semantics
	 */
	public void setAllSemantics(final HashMap<String, ConfigData> allSemantics)
	{
		this._allSemantics = allSemantics;
	}

	/**
	 * Sets the aspect facet proposals.
	 * @param aspectFacetProposals the aspect facet proposals
	 */
	public void setAspectFacetProposals(final HashMap<String, ConfigData> aspectFacetProposals)
	{
		this._aspectFacetProposals = aspectFacetProposals;
		setChanged();
		notifyObservers("newAspectFacetProposals");
	}

	/**
	 * Sets the basic person data.
	 * @param p the person
	 * @param ca the current aspect
	 * @param newAspect the new aspect
	 * @param isNorm the is norm
	 * @param label the label
	 */
	private boolean setBasicPersonData(final Person p, final Aspect ca, final boolean newAspect, final boolean isNorm,
			final String label)
	{
		boolean changed = false;
		if (p.getBasicPersonData() == null)
		{
			BasicPersonData bpd = new BasicPersonData();
			Vector<ComplexName> cns = new Vector<ComplexName>(3);
			bpd.setComplexNames(cns);
			p.setBasicPersonData(bpd);
		}
		if (label.equals("biographicalData"))
		{
			if (ca.getRangeList() != null && ca.getNotification() != null)
			{
				for (TaggingRange tr : ca.getRangeList())
				{
					if (tr.getName().equals("date"))
					{
						if (tr.getType().equals("lifespan")
								&& !p.getBasicPersonData().getBeginningOfLife().equals(tr.getFrom())
								&& !p.getBasicPersonData().getEndOfLife().equals(tr.getTo()))
						{
							p.getBasicPersonData().setBeginningOfLife(tr.getFrom());
							p.getBasicPersonData().setEndOfLife(tr.getTo());
							changed = true;
						}
						else if (tr.getType().equals("beginningOfLife"))
						{
							if (tr.getSubtype() != null && tr.getSubtype().equals("birth"))
							{
								if (tr.getWhen() != null
										&& !p.getBasicPersonData().getBeginningOfLife().equals(tr.getWhen()))
								{
									p.getBasicPersonData().setBeginningOfLife(tr.getWhen());
									changed = true;
								}
								else if (tr.getNotBefore() != null
										&& !p.getBasicPersonData().getBeginningOfLife().equals(tr.getNotBefore()))
								{
									p.getBasicPersonData().setBeginningOfLife(tr.getNotBefore());
									changed = true;
								}
								else if (tr.getNotAfter() != null
										&& !p.getBasicPersonData().getBeginningOfLife().equals(tr.getNotAfter()))
								{
									p.getBasicPersonData().setBeginningOfLife(tr.getNotAfter());
									changed = true;
								}
							}
						}
						else if (tr.getType().equals("endOfLife"))
						{
							if (tr.getSubtype() != null && tr.getSubtype().equals("death"))
							{
								if (tr.getWhen() != null && !p.getBasicPersonData().getEndOfLife().equals(tr.getWhen()))
								{
									p.getBasicPersonData().setEndOfLife(tr.getWhen());
									changed = true;
								}
								else if (tr.getNotBefore() != null
										&& !p.getBasicPersonData().getEndOfLife().equals(tr.getNotBefore()))
								{
									p.getBasicPersonData().setEndOfLife(tr.getNotBefore());
									changed = true;
								}
								else if (tr.getNotAfter() != null
										&& !p.getBasicPersonData().getEndOfLife().equals(tr.getNotAfter()))
								{
									p.getBasicPersonData().setEndOfLife(tr.getNotAfter());
									changed = true;
								}
							}
						}
					}
				}
			}
		}
		else if (label.equals("principalDescription"))
		{
			if (ca.getRangeList() != null && ca.getNotification() != null)
			{
				for (TaggingRange tr : ca.getRangeList())
				{
					if (tr.getName().equals("name")
							&& !p.getBasicPersonData()
									.getDescriptions()
									.contains(
											ca.getNotification().substring(tr.getStart(),
													tr.getStart() + tr.getLength())))
					{
						p.getBasicPersonData().getDescriptions()
								.add(ca.getNotification().substring(tr.getStart(), tr.getStart() + tr.getLength()));
						changed = true;
					}
				}
			}
		}
		else
		{
			ComplexName cn = new ComplexName();
			cn.setForeName("");
			cn.setSurName("");
			cn.setNameLink("");
			cn.setGenName("");
			cn.setOrgName("");
			// System.out.println("setBasicPersonData");
			if (ca.getRangeList() != null && ca.getNotification() != null)
			{
				for (TaggingRange tr : ca.getRangeList())
				{
					if (ca.getNotification().length() >= tr.getStart()
							&& ca.getNotification().length() >= (tr.getStart() + tr.getLength()))
						if (tr.getName().equals("persName") && tr.getType() != null)
						{
							if (tr.getType().equals("surname"))
							{
									cn.setSurName(ca.getNotification().substring(tr.getStart(),
											Math.min(ca.getNotification().length(), tr.getStart() + tr.getLength())));
							}
							else if (tr.getType().equals("forename")
									&& (tr.getSubtype() == null || tr.getSubtype().trim().length() == 0 || tr.getSubtype().equals("first")))
							{
								if (cn.getForeName() != null)
								{
									cn.setForeName(cn.getForeName() + " " + ca.getNotification().substring(tr.getStart(),
											Math.min(ca.getNotification().length(), tr.getStart() + tr.getLength())));
								}
								else
								{
									cn.setForeName(ca.getNotification().substring(tr.getStart(),
											Math.min(ca.getNotification().length(), tr.getStart() + tr.getLength())));
								}
								
							}
							else if (tr.getType().equals("nameLink"))
							{
								cn.setNameLink(ca.getNotification().substring(tr.getStart(), Math.min(
														ca.getNotification().length(),
														Math.min(ca.getNotification().length(),
																tr.getStart() + tr.getLength()))));
							}
							else if (tr.getType().equals("genName"))
							{
									cn.setGenName(ca.getNotification().substring(tr.getStart(),
											Math.min(ca.getNotification().length(), tr.getStart() + tr.getLength())));
							}
						}
						if (tr.getName().equals("orgName"))
						{
							cn.setOrgName(ca.getNotification().substring(tr.getStart(),
									Math.min(ca.getNotification().length(), tr.getStart() + tr.getLength())));
						}
				}
			}
			if (cn.getSurName().trim().length() == 0 && cn.getForeName().trim().length() == 0
					&& ca.getNotification() != null)
			{
				cn.setSurName(ca.getNotification().trim());
			}

			if (p.getBasicPersonData() == null)
			{
				BasicPersonData bpd = new BasicPersonData();
				Vector<ComplexName> cns = new Vector<ComplexName>(3);
				bpd.setComplexNames(cns);
				p.setBasicPersonData(bpd);
			}
			else if (p.getBasicPersonData().getComplexNames() == null)
			{
				Vector<ComplexName> cns = new Vector<ComplexName>(3);
				p.getBasicPersonData().setComplexNames(cns);
			}

			if (isNorm)
			{
				p.getBasicPersonData().getDisplayNames().put(getKeyOfPersonNormNameTag(label), cn);

			}
			p.getBasicPersonData().getComplexNames().add(cn);
		}
		String oldName = new String(p.getDisplayName());
		PDRObjectDisplayNameProcessor pdrDisplayNameProc = new PDRObjectDisplayNameProcessor();
		pdrDisplayNameProc.processDisplayName(p);
		System.out.println("oldname " + oldName + " new: " + p.getDisplayName());
		if (!oldName.equals(p.getDisplayName()))
		{
			changed = true;
		}
		return changed;
	}

	/**
	 * Sets the concurring person.
	 * @param concurringPerson the new concurring person
	 */
	public void setConcurringPerson(final Person concurringPerson)
	{
		this._concurringPerson = concurringPerson;
		setChanged();
		if (concurringPerson != null)
		{
			_log = new Status(IStatus.INFO, CommonActivator.PLUGIN_ID, "Facade current concurring person set to: "
					+ concurringPerson.getPdrId().toString());
			iLogger.log(_log);
			notifyObservers("newConcurringPerson");
		}
		else
		{
			notifyObservers("noSelectedConcurringPerson");
		}
	}

	/**
	 * @param configs classification configuration to set
	 * @see org.bbaw.pdr.ae.config.core.IConfigFacade#setConfigs(java.util.HashMap)
	 */
	@Override
	public void setConfigs(HashMap<String, DatatypeDesc> configs)
	{
		this._configs = configs;
		
		if (_configs != null)
		{
			String markupProvider = Platform
					.getPreferencesService()
					.getString(CommonActivator.PLUGIN_ID, "PRIMARY_TAGGING_PROVIDER", AEConstants.TAGGING_LIST_PROVIDER, null).toUpperCase(); //$NON-NLS-1$;
			String standard = "PDR";
			if (!_configs.containsKey(standard))
			{
				for (String s : _configs.keySet())
				{
					standard = s;
					break;
				}
			}
			if (!_configs.containsKey(markupProvider))
			{
				CommonActivator.getDefault().getPreferenceStore()
				.setValue("PRIMARY_TAGGING_PROVIDER", standard); //$NON-NLS-1$
			}
			String relationProvider = Platform
					.getPreferencesService()
					.getString(CommonActivator.PLUGIN_ID,
							"PRIMARY_RELATION_PROVIDER", AEConstants.RELATION_CLASSIFICATION_PROVIDER, null).toUpperCase(); //$NON-NLS-1$
			if (!_configs.containsKey(relationProvider))
			{
				CommonActivator.getDefault().getPreferenceStore()
				.setValue("PRIMARY_RELATION_PROVIDER", standard); //$NON-NLS-1$
			}
			String semanticProvider = Platform
					.getPreferencesService()
					.getString(CommonActivator.PLUGIN_ID,
							"PRIMARY_SEMANTIC_PROVIDER", AEConstants.CLASSIFICATION_AUTHORITY, null).toUpperCase(); //$NON-NLS-1$
			if (!_configs.containsKey(semanticProvider))
			{
				CommonActivator.getDefault().getPreferenceStore()
				.setValue("PRIMARY_SEMANTIC_PROVIDER", standard); //$NON-NLS-1$
			}
		}
	}

	/**
	 * Sets the current aspect.
	 * @param ca the new current aspect
	 */
	public void setCurrentAspect(final Aspect ca)
	{
		this._currentAspect = ca;
		setChanged();
		if (ca != null)
		{
			IStatus sAspect = new Status(IStatus.INFO, CommonActivator.PLUGIN_ID, "Facade current aspect set to: "
					+ _currentAspect.getPdrId().toString());
			iLogger.log(sAspect);
			addToLastAspects(ca);
			notifyObservers("newAspect");
			setCurrentPdrObject(ca);
		}
		else
		{
			notifyObservers("noSelectedAspect");
		}
	}

	/**
	 * Sets the current aspect.
	 * @param id of the new current aspect
	 */
	public void setCurrentAspect(final String id)
	{
		setCurrentAspect(_loadedAspects.get(id));

	}

	/**
	 * sets currently copied aspect to current aspect.
	 */
	public void setCurrentCopiedAspect()
	{
		_currentCopiedAspect = _currentAspect;
	}

	/**
	 * @param currentPdrObject the currentPdrObject to set
	 */
	public void setCurrentPdrObject(final PdrObject currentPdrObject)
	{
		this._currentPdrObject = currentPdrObject;
		boolean currentMayDelete = _rightsChecker.mayDelete(currentPdrObject);
		getUserRoleSourceProvider().setUserMayDelete(currentMayDelete);

		// if (!userMayDelete.equals(((Boolean) currentMayDelete).toString()))
		// {
		// // get our source provider by querying by the variable name
		//
		// // set the value
		// _userRoleSourceProvider.setUserMayDelete(currentMayDelete);
		// // ICommandService cService = (ICommandService)
		// PlatformUI.getWorkbench().getService(ICommandService.class);
		// //
		// cService.refreshElements("org.bbaw.pdr.ae.view.main.commands.DeletePerson",
		// null);
		// }
	}

	/**
	 * sets current person.
	 * @param cp currentPerson Person currently selected person.
	 */
	public void setCurrentPerson(final Person cp)
	{
		this._currentPerson = cp;
		addToLastPersons(cp);

		setChanged();
		if (cp != null)
		{
			IStatus sPerson = new Status(IStatus.INFO, CommonActivator.PLUGIN_ID, "Facade current person set to: "
					+ cp.getPdrId().toString());
			iLogger.log(sPerson);
			notifyObservers("newPerson");
			setCurrentPdrObject(cp);
		}
		else
		{
			notifyObservers("noSelectedPerson");
		}
	}

	/**
	 * Sets the current reference.
	 * @param cr the new current reference
	 */
	public void setCurrentReference(final ReferenceMods cr)
	{
		this._currentReference = cr;
		setChanged();
		if (cr != null)
		{
			IStatus sRef = new Status(IStatus.INFO, CommonActivator.PLUGIN_ID, "Facade current reference set to: "
					+ cr.getPdrId().toString());
			iLogger.log(sRef);
			addToLastReferences(cr);
			notifyObservers("newReference");
			setCurrentPdrObject(cr);
		}
	}

	/**
	 * Sets the current reference.
	 * @param id the new current reference
	 */
	public void setCurrentReference(final String id)
	{
		setCurrentReference(getReference(new PdrId(id)));

	}

	/**
	 * Sets the current reference to null.
	 */
	public void setCurrentReferenceToNull()
	{
		_currentReference = null;

	}

	/**
	 * Sets the current tree objects.
	 * @param pdrObjs the new current tree objects
	 */
	public void setCurrentTreeObjects(final PdrObject[] pdrObjs)
	{

		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getPartService().getActivePartReference()
				.getId();

		this._currentTreeObjects = pdrObjs;
		if (_currentTreeObjects != null && _currentTreeObjects.length > 0
				&& _currentTreeObjects[_currentTreeObjects.length - 1] != null)
		{
			for (PdrObject o : _currentTreeObjects)
			{
				if (!_rightsChecker.mayDelete(o))
				{
					break;
				}
			}
			_currentPdrObject = _currentTreeObjects[_currentTreeObjects.length - 1];
			if (_currentPdrObject instanceof Person)
			{
				setCurrentPerson((Person) _currentPdrObject);
			}
			else if (_currentPdrObject instanceof Aspect)
			{
				setCurrentAspect((Aspect) _currentPdrObject);
			}
			else if (_currentPdrObject instanceof ReferenceMods)
			{
				setCurrentReference((ReferenceMods) _currentPdrObject);
			}
		}
		// getUserRoleSourceProvider().setUserMayDelete(currentMayDelete);
		// if (!userMayDelete.equals(((Boolean) currentMayDelete).toString()))
		// {
		// // get our source provider by querying by the variable name
		// UserRoleSourceProvider userRoleSourceProvider =
		// (UserRoleSourceProvider) service
		// .getSourceProvider(AEPluginIDs.SOURCE_PARAMETER_USER_MAY_DELETE);
		// // set the value
		// userRoleSourceProvider.setUserMayDelete(currentMayDelete);
		// ICommandService cService = (ICommandService)
		// PlatformUI.getWorkbench().getService(ICommandService.class);
		// cService.refreshElements("org.bbaw.pdr.ae.view.main.commands.DeletePerson",
		// null);
		// }

		// FIXMEE Delete after testing
		// state =
		// service.getSourceProvider(AEPluginIDs.SOURCE_PARAMETER_USER_MAY_DELETE).getCurrentState();
		// String currentState = (String)
		// state.get(AEPluginIDs.SOURCE_PARAMETER_USER_MAY_DELETE);
		// System.out.println("currentState " + currentState);
		//
		// partid =
		// PlatformUI.getWorkbench().getActiveWorkbenchWindow().getPartService().getActivePartReference().getId();
		// System.out.println(partid);
		//
		setChanged();
		if (_currentTreeObjects != null)
		{
			notifyObservers("newTreeObjects");
		}
		//
		// partid =
		// PlatformUI.getWorkbench().getActiveWorkbenchWindow().getPartService().getActivePartReference().getId();
		// System.out.println(partid);
	}

	/**
	 * Sets the current user.
	 * @param currentUser the new current user
	 */
	public void setCurrentUser(final User currentUser)
	{
		this._currentUser = currentUser;
		if (PlatformUI.isWorkbenchRunning())
		{
			if (currentUser != null)
			{
				processUserRole();
			}
		}
		setChanged();
		notifyObservers("newUser");
	}

	/**
	 * Sets the facet proposals.
	 * @param facetProposals the facet proposals
	 */
	public void setFacetProposals(final HashMap<String, ConfigData> facetProposals)
	{
		this._facetProposals = facetProposals;
		setChanged();
		notifyObservers("newPersonFacetProposals");
	}

	/**
	 * Sets the favorite markups to null.
	 */
	public void setFavoriteMarkupsToNull()
	{
		_favoriteMarkups = null;
	}

	/**
	 * Sets the last aspects.
	 * @param lastAspects the new last aspects
	 */
	public void setLastAspects(final Vector<Aspect> lastAspects)
	{
		this._lastAspects = lastAspects;

	}

	/**
	 * Sets the last expert search.
	 * @param lastExpertSearch the new last expert search
	 */
	public void setLastExpertSearch(final PdrQuery lastExpertSearch)
	{
		this._lastExpertSearch = lastExpertSearch;
	}

	/**
	 * Sets the last persons.
	 * @param lastPersons the new last persons
	 */
	public void setLastPersons(final Vector<Person> lastPersons)
	{
		/** add currentPerson to lastPersons */
		// lastPersons.add(currentPerson);
		this._lastPersons = lastPersons;
	}

	/**
	 * @param lastReferences the lastReferences to set
	 */
	public void setLastReferences(final Vector<ReferenceMods> lastReferences)
	{
		this._lastReferences = lastReferences;
	}

	/**
	 * Sets the lazy loading.
	 * @param lazyLoading the new lazy loading
	 */
	public void setLazyLoading(final Boolean lazyLoading)
	{
	}

	/**
	 * Sets the loaded aspects.
	 * @param loadedAspects the loaded aspects
	 */
	public void setLoadedAspects(final HashMap<PdrId, Aspect> loadedAspects)
	{
		this._loadedAspects = loadedAspects;
	}

	/**
	 * Sets the number of all persons.
	 * @param numberOfAllPersons the new number of all persons
	 */
	public void setNumberOfAllPersons(final int numberOfAllPersons)
	{
		this._numberOfAllPersons = numberOfAllPersons;
	}

	/**
	 * Sets the reference mods templates.
	 * @param referenceModsTemplates the reference mods templates
	 */
	public void setReferenceModsTemplates(final HashMap<String, ReferenceModsTemplate> referenceModsTemplates)
	{
		this._referenceModsTemplates = referenceModsTemplates;
	}

	/**
	 * Sets the rel obj typ.
	 * @param relObjTyp the new rel obj typ
	 */
	public void setRelObjTyp(final int relObjTyp)
	{
		this._relObjTyp = relObjTyp;
	}

	/**
	 * @param requestedId the requestedId to set
	 */
	public void setRequestedId(final PdrId requestedId)
	{
		this._requestedId = requestedId;
	}

	/**
	 * Sets the requested identifier.
	 * @param requestedIdentifier the new requested identifier
	 */
	public void setRequestedIdentifier(final String requestedIdentifier)
	{
		this._requestedIdentifier = requestedIdentifier;
	}

	/**
	 * Sets the requested identifier type.
	 * @param requestedIdentifierType the new requested identifier type
	 */
	public void setRequestedIdentifierType(final String requestedIdentifierType)
	{
		this._requestedIdentifierType = requestedIdentifierType;
	}

	/**
	 * Sets the resulting persons.
	 * @param resultingPersons the new resulting persons
	 */
	public void setResultingPersons(final Vector<Person> resultingPersons)
	{
		if (resultingPersons != null)
		{
			_log = new Status(IStatus.INFO, CommonActivator.PLUGIN_ID,
					"Facade has received new resulting persons. size: " + resultingPersons.size());
			iLogger.log(_log);
			notifyObservers("newResultingPersons");
		}
		this._resultingPersons = resultingPersons;
	}

	/**
	 * Sets the selected aspect.
	 * @param selectedAspect the new selected aspect
	 */
	public void setSelectedAspect(final Aspect selectedAspect)
	{
		this._selectedAspect = selectedAspect;
		setChanged();
		if (_lastAspects != null)
		{
			notifyObservers("newSelectedAspect");
		}
	}

	/**
	 * Sets the selected person.
	 * @param selectedPerson the new selected person
	 */
	public void setSelectedPerson(final Person selectedPerson)
	{
		this._selectedPerson = selectedPerson;
		IStatus sPerson = new Status(IStatus.INFO, CommonActivator.PLUGIN_ID, "selected person. " + selectedPerson);
		iLogger.log(sPerson);
		setChanged();
		if (selectedPerson != null)
		{
			notifyObservers("newSelectedPerson");
		}
	}

	/**
	 * @param selectedDirectory the selected directory for writing config backup
	 * @throws Exception exc.
	 * @see org.bbaw.pdr.ae.config.core.IConfigFacade#writeToLocalConfigBackup(java.lang.String)
	 */
	@Override
	public void writeToLocalConfigBackup(final String selectedDirectory) throws Exception
	{
		getDBManager().writeToLocalConfigBackup(selectedDirectory);

	}
}
