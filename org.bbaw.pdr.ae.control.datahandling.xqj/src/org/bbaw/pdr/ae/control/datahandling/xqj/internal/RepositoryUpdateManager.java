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
/*
 * @author: Christoph Plutte
 */
package org.bbaw.pdr.ae.control.datahandling.xqj.internal;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.ParseException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import javax.xml.xquery.XQConnection;
import javax.xml.xquery.XQException;
import javax.xml.xquery.XQPreparedExpression;
import javax.xml.xquery.XQResultSequence;

import org.bbaw.pdr.ae.common.AEConstants;
import org.bbaw.pdr.ae.common.CommonActivator;
import org.bbaw.pdr.ae.common.ResourceLocator;
import org.bbaw.pdr.ae.common.utils.CopyDirectory;
import org.bbaw.pdr.ae.config.core.ConfigXMLProcessor;
import org.bbaw.pdr.ae.config.core.DataDescSaxHandler;
import org.bbaw.pdr.ae.config.model.DatatypeDesc;
import org.bbaw.pdr.ae.control.core.UserXMLProcessor;
import org.bbaw.pdr.ae.control.core.XMLProcessor;
import org.bbaw.pdr.ae.control.datahandling.xqj.config.ConfigManager;
import org.bbaw.pdr.ae.control.facade.Facade;
import org.bbaw.pdr.ae.control.interfaces.IUpdateManager;
import org.bbaw.pdr.ae.control.saxHandler.AspectSaxHandler;
import org.bbaw.pdr.ae.control.saxHandler.PersonSaxHandler;
import org.bbaw.pdr.ae.control.saxHandler.ReferenceSaxHandler;
import org.bbaw.pdr.ae.db.basex711.DBConnector;
import org.bbaw.pdr.ae.metamodel.PdrId;
import org.bbaw.pdr.ae.model.Aspect;
import org.bbaw.pdr.ae.model.PdrObject;
import org.bbaw.pdr.ae.model.Person;
import org.bbaw.pdr.ae.model.ReferenceMods;
import org.bbaw.pdr.ae.model.User;
import org.bbaw.pdr.ae.model.view.PDRObjectsConflict;
import org.bbaw.pdr.ae.repositoryconnection.view.UpdateConflictDialog;
import org.bbaw.pdr.allies.client.Configuration;
import org.bbaw.pdr.allies.client.IDRange;
import org.bbaw.pdr.allies.client.Identifier;
import org.bbaw.pdr.allies.client.PDRType;
import org.bbaw.pdr.allies.client.Repository;
import org.bbaw.pdr.allies.client.Utilities;
import org.bbaw.pdr.allies.client.error.InvalidIdentifierException;
import org.bbaw.pdr.allies.client.error.PDRAlliesClientException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobManager;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.UIJob;
import org.osgi.framework.Bundle;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLFilterImpl;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * The Class RepositoryUpdateManager.
 * @author Christoph Plutte
 */
public class RepositoryUpdateManager implements IUpdateManager
{

	/** The db con. */
	private DBConnector _dbCon = DBConnector.getInstance();

	/** The repository id. */
	private int _repositoryId;

	/** The project id. */
	private int _projectId;

	/** The _facade. */
	private Facade _facade = Facade.getInstanz();

	/** The main searcher. */
	private MainSearcher _mainSearcher = new MainSearcher();

	/** The _xml proc. */
	private XMLProcessor _xmlProc = new XMLProcessor();

	/** The _user manager. */
	private UserManager _userManager = new UserManager();

	/** The _db manager. */
	private DBManager _dbManager = new DBManager();

	/** The _config manager. */
	private ConfigManager _configManager = new ConfigManager();

	/** The _id service. */
	private PdrIdService _idService = new PdrIdService();

	/** The conflicting rep aspects. */
	private Vector<String> _conflictingRepAspects = null;

	/** The conflicting rep persons. */
	private Vector<String> _conflictingRepPersons = null;

	/** The conflicting rep references. */
	private Vector<String> _conflictingRepReferences = null;

	/** The parsed aspect. */
	private Aspect _parsedAspect;

	/** The parsed person. */
	private Person _parsedPerson;

	/** The parsed reference. */
	private ReferenceMods _parsedReference;

	/** The revision pattern. */
	private Pattern _revisionPattern = Pattern
			.compile("ref=\"\\d\\\" timestamp=\"\\d{4}\\-\\d{2}\\-\\d{2}T\\d{2}\\:\\d{2}\\:\\d{2}");

	/** The NEWOBJECT s_ packag e_ size. */
	private static final int NEWOBJECTS_PACKAGE_SIZE = 50;

	/** The MODIFIEDOBJECT s_ packag e_ size. */
	private static final int MODIFIEDOBJECTS_PACKAGE_SIZE = 50;

	/** package size. */
	private static final int PACKAGE_SIZE = 249;

	/** The MA x_ objec t_ number. */
	private static final int MAX_OBJECT_NUMBER = 99999999;
	/** Logger. */
	private static ILog iLogger = AEConstants.ILOGGER;
	/** status. */
	private IStatus log;

	private Validator aspectXMLValidator;

	private Validator personXMLValidator;

	private Validator userXMLValidator;

	private Validator referenceXMLValidator;
	/**
	 * checks ids.
	 * @param objects updated objects.
	 * @param id id
	 * @param idMap map of ids from repository
	 * @param begin begin index
	 * @param modifiedIds modified ids
	 * @return vector of modified ids
	 * @throws InvalidIdentifierException exc.
	 */
	private Vector<String> checkModfiedIds(final Vector<String> objects, final Identifier id,
			final Map<Identifier, Identifier> idMap, final int begin, final Vector<String> modifiedIds)
			throws InvalidIdentifierException
	{
		// System.out.println("checkModfiedIds");
		for (int i = 0; i <= begin && i < objects.size(); i++)
		{
			String s = objects.get(i);
			if (s.contains(id.toString()))
			{
				Identifier oldId = new Identifier(extractPdrId(s));
				Identifier newId = idMap.get(oldId);
				if (newId != null && modifiedIds.contains(newId.toString()))
				{
					modifiedIds.add(newId.toString());
					System.out.println("inserting modified obj oldid " + oldId.toString() + " new " + newId.toString());
				}
			}
		}
		return modifiedIds;

	}

	/**
	 * checks if updated version from repository is really younger than the
	 * local one.
	 * @param repo version from repository
	 * @param col collection
	 * @param name id.
	 * @return true if repository version not older than local one
	 * @throws Exception
	 */
	private boolean checkVersions(final String repo, final String col, final String name) throws Exception
	{
		// System.out.println("checking version repo " + repo);
		String local = null;
		try
		{
			System.out.println("checking version col " + col + " name " + name);
			local = _mainSearcher.searchObjectString(col, name);
			System.out.println("checking version local " + local);
		}
		catch (XQException e)
		{
			log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e);
			iLogger.log(log);
			e.printStackTrace();
		}
		String localLastRev = null;
		Date localLastDate = null;
		String repoLastRev = null;
		Date repoLastDate = null;
		if (local != null)
		{
			Matcher m = _revisionPattern.matcher(local);
			while (m.find())
			{
				localLastRev = m.group();
			}

			if (localLastRev != null)
			{
				localLastRev = localLastRev.split("timestamp=\"")[1];
				try
				{
					localLastDate = AEConstants.ADMINDATE_FORMAT.parse(localLastRev);
				}
				catch (ParseException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			m = _revisionPattern.matcher(repo);
			while (m.find())
			{
				repoLastRev = m.group();
			}

			if (repoLastRev != null)
			{
				repoLastRev = repoLastRev.split("timestamp=\"")[1];
				try
				{
					repoLastDate = AEConstants.ADMINDATE_FORMAT.parse(repoLastRev);
				}
				catch (ParseException e)
				{
					log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e);
					iLogger.log(log);
					e.printStackTrace();
				}
			}

			if (localLastDate != null && repoLastDate != null && repoLastDate.before(localLastDate))
			{
				return false;
			}
			else
			{
				return true;
			}
		}
		else
		{
			return true;
		}
	}

	/**
	 * extracts pdrid from object xml string.
	 * @param objectString object xml as string
	 * @return pdrid
	 */
	private String extractPdrId(final String objectString)
	{

		Matcher m = AEConstants.PDR_ID_PATTERN.matcher(objectString);
		String id = null;
		if (m.find())
		{
			id = m.group();
		}
		// objectString = objectString.split("<record")[0];
		//
		// // System.out.println("objectString " + objectString);
		// String id = objectString.split("id=\"")[1];
		// id = id.substring(0, 23);
		// System.out.println(id);
		return id;
	}

	/**
	 * Gets the modified aspects.
	 * @return the modified aspects
	 * @throws XQException the xQ exception
	 * @throws XMLStreamException the xML stream exception
	 */
	private Vector<String> getModifiedAspects() throws Exception
	{
		Vector<String> modifiedIds = _idService.getModifiedAspectIds();
		Vector<String> modifiedAspects = new Vector<String>(modifiedIds.size());
		String aspectString;

		for (String id : modifiedIds)
		{
			aspectString = _mainSearcher.searchObjectString("aspect", id);
			aspectString = removeAspectPrefixes(aspectString);
			// System.out.println("replacing aodls: " + modifiedAspects);
			String newStr = aspectString;
			try
			{
				newStr = new String(aspectString.getBytes("UTF-8"), "UTF-8");
			}
			catch (UnsupportedEncodingException e)
			{
				log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e);
				iLogger.log(log);
				e.printStackTrace();
			}
			modifiedAspects.add(newStr);
		}
		return modifiedAspects;
	}

	/**
	 * Gets the modified config.
	 * @throws Exception
	 */
	private void getModifiedConfig() throws Exception
	{
		Vector<String> providers = null;
		try {
			providers = Utilities.getCategoryProviders();
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		if (providers == null)
		{
			providers = new Vector<String>();
			for (String provider :  _facade.getConfigs().keySet())
			{
				providers.add(provider);
			}
		}
		for (String provider :  providers)
		{
			String modifiedConfig = null;
			try
			{
				modifiedConfig = Utilities.getCategories(provider);
				if (modifiedConfig != null && modifiedConfig.trim().length() > 0
						&& !modifiedConfig.contains("file not found"))
				{
					SAXParserFactory factory = SAXParserFactory.newInstance();
					ConfigManager configManager = new ConfigManager();
					try
					{

						InputStream xmlInput = new ByteArrayInputStream(modifiedConfig.getBytes("UTF-8"));
						SAXParser saxParser = factory.newSAXParser();

						DataDescSaxHandler handler = new DataDescSaxHandler(configManager);
						XMLReader reader = saxParser.getXMLReader();
						try
						{
							// Turn on validation
							reader.setFeature("http://xml.org/sax/features/validation", true); //$NON-NLS-1$
							// Ensure namespace processing is on (the default)
							reader.setFeature("http://xml.org/sax/features/namespaces", true); //$NON-NLS-1$
						}
						catch (SAXNotRecognizedException e)
						{
							log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e);
							iLogger.log(log);
						}
						catch (SAXNotSupportedException e)
						{
							log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e);
							iLogger.log(log);
						}

						saxParser.parse(xmlInput, handler);

					}
					catch (Throwable err)
					{
						err.printStackTrace();
					}
					DatatypeDesc dtd = configManager.getDatatypeDesc();
					if (dtd != null && dtd.isValid())
					{
						if (dtd.getProvider() != null
								&& dtd.getProvider().equals(
										Platform.getPreferencesService()
												.getString(CommonActivator.PLUGIN_ID, "PRIMARY_SEMANTIC_PROVIDER",
														AEConstants.CLASSIFICATION_AUTHORITY, null)))
						{

						}
						try
						{
							_configManager.saveConfig(dtd);
						}
						catch (XMLStreamException e)
						{
							log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e);
							iLogger.log(log);							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						catch (XQException e)
						{
							log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e);
							iLogger.log(log);
							e.printStackTrace();
						}
					}
				}
			}
			catch (PDRAlliesClientException e1)
			{
				log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e1);
				iLogger.log(log);
				e1.printStackTrace();
			}

		}

	}

	/**
	 * Gets the modified persons.
	 * @return the modified persons
	 * @throws XQException the xQ exception
	 * @throws XMLStreamException the xML stream exception
	 */
	private Vector<String> getModifiedPersons() throws Exception
	{
		Vector<String> modifiedIds = _idService.getModifiedPersonIds();
		Vector<String> modifiedPersons = new Vector<String>(modifiedIds.size());
		String personString;

		for (String id : modifiedIds)
		{
			personString = _mainSearcher.searchObjectString("person", id);

			personString = removePersonPrefix(personString);
			modifiedPersons.add(personString);
		}
		return modifiedPersons;
	}

	/**
	 * Gets the modified references.
	 * @return the modified references
	 * @throws XQException the xQ exception
	 * @throws XMLStreamException the xML stream exception
	 */
	private Vector<String> getModifiedReferences() throws Exception
	{
		Vector<String> modifiedIds = _idService.getModifiedReferenceIds();
		Vector<String> modifiedRefs = new Vector<String>(modifiedIds.size());
		String refString;
		for (String id : modifiedIds)
		{
			refString = _mainSearcher.searchObjectString("reference", id);
			modifiedRefs.add(refString);
		}
		return modifiedRefs;
	}

	/**
	 * Gets the modified users.
	 * @return the modified users
	 * @throws XQException the xQ exception
	 * @throws XMLStreamException the xML stream exception
	 */
	private Vector<String> getModifiedUsers() throws Exception
	{
		Vector<String> modifiedIds = _idService.getModifiedUserIds();
		Vector<String> modifiedUsers = new Vector<String>(modifiedIds.size());
		User u;
		UserXMLProcessor userXMLProc = new UserXMLProcessor();
		// Pattern openP = Pattern.compile("<podl:");
		// Pattern closeP = Pattern.compile("<\\/podl:");
		// Matcher m = openP.matcher(personString);
		// m.replaceAll("<");
		// m = closeP.matcher(personString);
		// m.replaceAll("</");
		String userString;
		for (String id : modifiedIds)
		{
			u = _userManager.getUserById(id);
			if (u != null)
			{
				userString = userXMLProc.writeToXML(u);
				userString = removeUserPrefix(userString);
				modifiedUsers.add(userString);
			}
		}
		return modifiedUsers;
	}

	/**
	 * Gets the new users.
	 * @return the new users
	 * @throws XQException the xQ exception
	 * @throws XMLStreamException the xML stream exception
	 */
	private Vector<String> getNewUsers() throws Exception
	{
		Vector<String> modifiedIds = _idService.getNewUserIds();
		Vector<String> modifiedUsers = new Vector<String>(modifiedIds.size());
		User u;
		UserXMLProcessor userXMLProc = new UserXMLProcessor();
		// Pattern openP = Pattern.compile("<podl:");
		// Pattern closeP = Pattern.compile("<\\/podl:");
		// Matcher m = openP.matcher(personString);
		// m.replaceAll("<");
		// m = closeP.matcher(personString);
		// m.replaceAll("</");
		String userString;
		for (String id : modifiedIds)
		{
			u = _userManager.getUserById(id);
			if (u != null && u.getPdrId() != null)
			{
				userString = userXMLProc.writeToXML(u);
				userString = removeUserPrefix(userString);
				modifiedUsers.add(userString);
			}

		}
		return modifiedUsers;
	}

	/**
	 * Handle objects conflicts.
	 * @param monitor the monitor
	 */
	private void handleObjectsConflicts(final IProgressMonitor monitor)
	{
		UIJob job = new UIJob("Update Conflict Handling") {
			 @Override
			 public IStatus runInUIThread(IProgressMonitor monitor) {
				 String id;
					PDRObjectsConflict oConflict;
					Vector<PDRObjectsConflict> conAspects = null;
					Vector<PDRObjectsConflict> conPersons = null;
					Vector<PDRObjectsConflict> conReferences = null;
					InputStream is;
					SAXParserFactory factory = SAXParserFactory.newInstance();
					SAXParser saxParser = null;
					try
					{
						saxParser = factory.newSAXParser();
					}
					catch (ParserConfigurationException e1)
					{
						log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e1);
						iLogger.log(log);
						e1.printStackTrace();
					}
					catch (SAXException e1)
					{
						log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e1);
						iLogger.log(log);
						e1.printStackTrace();
					}

					if (_conflictingRepAspects != null && !_conflictingRepAspects.isEmpty())
					{
						AspectSaxHandler handler = new AspectSaxHandler(new PdrObject[]
						{}, monitor);
						conAspects = new Vector<PDRObjectsConflict>(_conflictingRepAspects.size());
						for (String s : _conflictingRepAspects)
						{
							id = extractPdrId(s);
							if (saxParser != null)
							{
								try
								{
									is = new ByteArrayInputStream(s.getBytes("UTF-8"));
									saxParser.parse(is, handler);
									oConflict = new PDRObjectsConflict();
									_parsedAspect = (Aspect) handler.getResultObject();
									if (_parsedAspect != null)
									{
										oConflict.setRepositoryObject(_parsedAspect);
									}
									_parsedAspect = null;
									oConflict.setLocalObject(_facade.getAspect(new PdrId(id)));
									conAspects.add(oConflict);
								}
								catch (UnsupportedEncodingException e)
								{
									log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e);
									iLogger.log(log);
									e.printStackTrace();
								}
								catch (SAXException e)
								{
									log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e);
									iLogger.log(log);
									e.printStackTrace();
								}
								catch (IOException e)
								{
									log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e);
									iLogger.log(log);
									e.printStackTrace();
								}
							}

						}
					}
					if (_conflictingRepPersons != null && !_conflictingRepPersons.isEmpty())
					{
						conPersons = new Vector<PDRObjectsConflict>(_conflictingRepPersons.size());
						PersonSaxHandler handler = new PersonSaxHandler();
						for (String s : _conflictingRepPersons)
						{
							id = extractPdrId(s);
							if (saxParser != null)
							{
								try
								{
									is = new ByteArrayInputStream(s.getBytes("UTF-8"));
									saxParser.parse(is, handler);
									oConflict = new PDRObjectsConflict();
									_parsedPerson = (Person) handler.getResultObject();
									if (_parsedPerson != null)
									{
										oConflict.setRepositoryObject(_parsedPerson);
									}
									_parsedPerson = null;
									oConflict.setLocalObject(_facade.getPdrObject(new PdrId(id)));
									conPersons.add(oConflict);
								}
								catch (UnsupportedEncodingException e)
								{
									log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e);
									iLogger.log(log);
									e.printStackTrace();
								}
								catch (SAXException e)
								{
									log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e);
									iLogger.log(log);
									e.printStackTrace();
								}
								catch (IOException e)
								{
									log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e);
									iLogger.log(log);
									e.printStackTrace();
								}
							}

						}
					}
					if (_conflictingRepReferences != null && !_conflictingRepReferences.isEmpty())
					{
						conReferences = new Vector<PDRObjectsConflict>(_conflictingRepReferences.size());
						ReferenceSaxHandler handler = new ReferenceSaxHandler();
						for (String s : _conflictingRepReferences)
						{
							id = extractPdrId(s);
							if (saxParser != null)
							{
								try
								{
									is = new ByteArrayInputStream(s.getBytes("UTF-8"));
									saxParser.parse(is, handler);
									oConflict = new PDRObjectsConflict();
									_parsedReference = (ReferenceMods) handler.getResultObject();
									if (_parsedReference != null)
									{
										oConflict.setRepositoryObject(_parsedReference);
									}
									_parsedReference = null;
									oConflict.setLocalObject(_facade.getReference(new PdrId(id)));
									conReferences.add(oConflict);
								}
								catch (UnsupportedEncodingException e)
								{
									log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e);
									iLogger.log(log);
									e.printStackTrace();
								}
								catch (SAXException e)
								{
									log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e);
									iLogger.log(log);
									e.printStackTrace();
								}
								catch (IOException e)
								{
									log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e);
									iLogger.log(log);
									e.printStackTrace();
								}
							}

						}
					}

					 IWorkbench workbench = PlatformUI.getWorkbench();
					 Display display = workbench.getDisplay();
					 Shell shell = new Shell(display);
							UpdateConflictDialog dialog = new UpdateConflictDialog(shell, conAspects, conPersons, conReferences); //$NON-NLS-1$
					 if (dialog.open() == 0)
					 {
					 int totalWork = 0;
					 if (conAspects != null) totalWork = conAspects.size();
					 if (conPersons != null) totalWork = totalWork + conPersons.size();
					 if (conReferences != null) totalWork = totalWork +
					 conReferences.size();
					 monitor.beginTask("Resolving Update Conflicts. Number of Objects: " +
					 totalWork, totalWork);
					
					 try {
							_idService.clearAllUpdateStates();
						} catch (Exception e) {
							log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e);
							iLogger.log(log);
							e.printStackTrace();
						}
					 if (conAspects != null && !conAspects.isEmpty())
					 try {
					 insertConflictingObjects(conAspects, monitor);
					 } catch (XMLStreamException e) {
						 log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e);
							iLogger.log(log);					
						 e.printStackTrace();
					 } catch (UnsupportedEncodingException e) {
						 log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e);
							iLogger.log(log);
						 e.printStackTrace();
					 } catch (PDRAlliesClientException e) {
						 log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e);
							iLogger.log(log);
						 e.printStackTrace();
					 } catch (XQException e) {
						 log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e);
							iLogger.log(log);
					 e.printStackTrace();
					 } catch (Exception e) {

						 log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e);
							iLogger.log(log);
					}
					 if (conPersons != null && !conPersons.isEmpty())
					 try {
					 insertConflictingObjects(conPersons, monitor);
					 } catch (XMLStreamException e) {
						 log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e);
							iLogger.log(log);
					 } catch (UnsupportedEncodingException e) {
						 log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e);
							iLogger.log(log);					
					 } catch (PDRAlliesClientException e) {
						 log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e);
							iLogger.log(log);					
					 } catch (XQException e) {
						 log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e);
							iLogger.log(log);					
					 } catch (Exception e) {
						 log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e);
							iLogger.log(log);					}
					 if (conReferences != null && !conReferences.isEmpty())
					 try {
					 insertConflictingObjects(conReferences, monitor);
					 } catch (XMLStreamException e) {
						 log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e);
							iLogger.log(log);
					 } catch (UnsupportedEncodingException e) {
						 log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e);
							iLogger.log(log);					
					 } catch (PDRAlliesClientException e) {
						 log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e);
							iLogger.log(log);					
					 } catch (XQException e) {
						 log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e);
							iLogger.log(log);					
					 } catch (Exception e) {

						 log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e);
							iLogger.log(log);}
					 }
			
					 _facade.refreshAllData();

			 return Status.OK_STATUS;
			 }
			 };
			 job.setUser(true);
//			 IJobManager manager = Job.getJobManager();
//			manager.currentJob().
			 job.schedule();
			 try {
				job.join();
			} catch (InterruptedException e) {

				log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e);
				iLogger.log(log);
				}

		

	}

	/**
	 * Injest modified aspects.
	 * @param monitor the monitor
	 * @throws XQException the xQ exception
	 * @throws UnsupportedEncodingException the unsupported encoding exception
	 * @throws PDRAlliesClientException the pDR allies client exception
	 * @throws XMLStreamException the xML stream exception
	 */
	private void injestModifiedAspects(final IProgressMonitor monitor) throws Exception
	{
		synchronized (_dbCon)
		{
			_conflictingRepAspects = new Vector<String>();
			Vector<String> subConflRepAspects = new Vector<String>();
			Vector<String> aspects = getModifiedAspects();
			if (aspects.size() == 0)
			{
				return;
			}
			monitor.beginTask("Injesting Modified Aspects into Repository. Number of Objects: " + aspects.size(),
					aspects.size());

			int begin = 0;
			int end;
			if (aspects.size() > MODIFIEDOBJECTS_PACKAGE_SIZE)
			{
				end = MODIFIEDOBJECTS_PACKAGE_SIZE;
			}
			else
			{
				end = aspects.size();
			}
			Vector<String> subAspects = new Vector<String>(end);
			for (int i = begin; i < end; i++)
			{
				String xml = aspects.get(i);
				if (isValidXMLAspect(xml))
				{
					subAspects.add(xml);
				}
				else
				{
					String xml2 = makeValidXMLAspect(xml);
					if (xml2 != null)
					{
						subAspects.add(xml2);
					}
				}
			}
			while (subAspects != null && !subAspects.isEmpty())
			{
//				for (String s : subAspects)
//				{
//					// System.out.println("Aspect: " + s);
//				}

				subConflRepAspects = Repository.modifyObjects(_repositoryId, _projectId, subAspects, false);
				if (subConflRepAspects != null && !subConflRepAspects.isEmpty())
				{
					_conflictingRepAspects.addAll(subConflRepAspects);
				}
				monitor.worked(subAspects.size());
				begin = end + 1;
				if (aspects.size() > MODIFIEDOBJECTS_PACKAGE_SIZE + end)
				{
					end = end + MODIFIEDOBJECTS_PACKAGE_SIZE;
				}
				else
				{
					end = aspects.size();
				}
				subAspects.clear();

				for (int i = begin; i < end; i++)
				{
					String xml = aspects.get(i);
					if (isValidXMLAspect(xml))
					{
						subAspects.add(xml);
					}
					else
					{
						String xml2 = makeValidXMLAspect(xml);
						if (xml2 != null)
						{
							subAspects.add(xml);
						}
					}
				}

			}
		}
	}

	/**
	 * Injest modified config.
	 * @throws XQException the xQ exception
	 * @throws UnsupportedEncodingException the unsupported encoding exception
	 * @throws PDRAlliesClientException the pDR allies client exception
	 * @throws XMLStreamException the xML stream exception
	 */
	private void injestModifiedConfig() throws Exception
	{
		synchronized (_dbCon)
		{
			Vector<String> configProviders = _idService.getModifiedConfigs();
			// Vector<String> configs =
			// _configManager.getConfigs(configProviders);
			// XXX anpassen
			for (String s : configProviders)
			{
				DatatypeDesc dtd = _configManager.getDatatypeDesc(s);
				String configStr = new ConfigXMLProcessor().writeToXML(dtd);
				// System.out.println("injestModifiedConfig() " + configStr);
				Utilities.setCategories(configStr, dtd.getProvider());

			}
		}

	}

	/**
	 * Injest modified persons.
	 * @param monitor the monitor
	 * @throws UnsupportedEncodingException the unsupported encoding exception
	 * @throws PDRAlliesClientException the pDR allies client exception
	 * @throws XQException the xQ exception
	 * @throws XMLStreamException the xML stream exception
	 */
	private void injestModifiedPersons(final IProgressMonitor monitor) throws Exception
	{
		synchronized (_dbCon)
		{
			_conflictingRepPersons = new Vector<String>();
			Vector<String> subConflictingPersons = new Vector<String>();
			Vector<String> persons = getModifiedPersons();
			if (persons.size() == 0)
			{
				return;
			}
			monitor.beginTask("Injesting Modified Persons into Repository. Number of Objects: " + persons.size(),
					persons.size());

			int begin = 0;
			int end;
			if (persons.size() > NEWOBJECTS_PACKAGE_SIZE)
			{
				end = NEWOBJECTS_PACKAGE_SIZE;
			}
			else
			{
				end = persons.size();
			}
			Vector<String> subPersons = new Vector<String>(end);
			for (int i = begin; i < end; i++)
			{
				String xml = persons.get(i);
				if (isValidXMLPerson(xml))
				{
					subPersons.add(xml);
				}
				else
				{
					String xml2 = makeValidXMLPerson(xml);
					if (xml2 != null)
					{
						subPersons.add(xml2);
					}
				}
			}
			while (subPersons != null && !subPersons.isEmpty())
			{
				subConflictingPersons = Repository.modifyObjects(_repositoryId, _projectId, subPersons, false);
				if (subConflictingPersons != null && !subConflictingPersons.isEmpty())
				{
					_conflictingRepPersons.addAll(subConflictingPersons);
				}
				monitor.worked(subPersons.size());
				begin = end + 1;
				if (persons.size() > NEWOBJECTS_PACKAGE_SIZE + end)
				{
					end = end + NEWOBJECTS_PACKAGE_SIZE;
				}
				else
				{
					end = persons.size();
				}
				subPersons.clear();

				for (int i = begin; i < end; i++)
				{
					String xml = persons.get(i);
					if (isValidXMLPerson(xml))
					{
						subPersons.add(xml);
					}
					else
					{
						String xml2 = makeValidXMLPerson(xml);
						if (xml2 != null)
						{
							subPersons.add(xml2);
						}
					}
				}
			}
		}

	}

	/**
	 * Injest modified references.
	 * @param monitor the monitor
	 * @throws UnsupportedEncodingException the unsupported encoding exception
	 * @throws PDRAlliesClientException the pDR allies client exception
	 * @throws XQException the xQ exception
	 * @throws XMLStreamException the xML stream exception
	 */
	private void injestModifiedReferences(final IProgressMonitor monitor) throws Exception
	{
		synchronized (_dbCon)
		{
			_conflictingRepReferences = new Vector<String>();

			Vector<String> subConflictingRefs = new Vector<String>();
			Vector<String> references = getModifiedReferences();
			if (references.size() == 0)
			{
				return;
			}
			monitor.beginTask("Injesting Modified References into Repository. Number of Objects: " + references.size(),
					references.size());

			int begin = 0;
			int end;
			if (references.size() > NEWOBJECTS_PACKAGE_SIZE)
			{
				end = NEWOBJECTS_PACKAGE_SIZE;
			}
			else
			{
				end = references.size();
			}
			Vector<String> subReferences = new Vector<String>(end);
			String ref;
			for (int i = begin; i < end; i++)
			{
				ref = references.get(i);
				ref = removeRefPrefix(ref);
				if (isValidXMLReference(ref))
				{
					subReferences.add(ref);
				}
				else
				{
					String xml2 = makeValidXMLReference(ref);
					if (xml2 != null)
					{
						subReferences.add(xml2);
					}
				}
				
			}
			while (subReferences != null && !subReferences.isEmpty())
			{
				subConflictingRefs = Repository.modifyObjects(_repositoryId, _projectId, subReferences, false);
				if (subConflictingRefs != null && !subConflictingRefs.isEmpty())
				{
					_conflictingRepReferences.addAll(subConflictingRefs);
				}
				monitor.worked(subReferences.size());
				begin = end + 1;
				if (references.size() > NEWOBJECTS_PACKAGE_SIZE + end)
				{
					end = end + NEWOBJECTS_PACKAGE_SIZE;
				}
				else
				{
					end = references.size();
				}
				subReferences.clear();

				for (int i = begin; i < end; i++)
				{
					ref = references.get(i);
					ref = removeRefPrefix(ref);
					if (isValidXMLReference(ref))
					{
						subReferences.add(ref);
					}
					else
					{
						String xml2 = makeValidXMLReference(ref);
						if (xml2 != null)
						{
							subReferences.add(xml2);
						}
					}
				}
			}
		}

	}

	/**
	 * Injest modified users.
	 * @throws UnsupportedEncodingException the unsupported encoding exception
	 * @throws PDRAlliesClientException the pDR allies client exception
	 * @throws XQException the xQ exception
	 * @throws XMLStreamException the xML stream exception
	 */
	private final void injestModifiedUsers() throws Exception
	{
		synchronized (_dbCon)
		{
			Vector<String> users = getModifiedUsers();
			int begin = 0;
			int end;
			if (users.size() > MODIFIEDOBJECTS_PACKAGE_SIZE)
			{
				end = MODIFIEDOBJECTS_PACKAGE_SIZE;
			}
			else
			{
				end = users.size();
			}
			Vector<String> subUsers = new Vector<String>(end);
			for (int i = begin; i < end; i++)
			{
				String xml = users.get(i);
				if (isValidXMLUser(xml))
				{
					subUsers.add(xml);
				}
				else
				{
					String xml2 = makeValidXMLUser(xml);
					if (xml2 != null)
					{
						subUsers.add(xml2);
					}
				}
			}
			while (subUsers != null && !subUsers.isEmpty())
			{
				Repository.modifyObjects(_repositoryId, _projectId, subUsers, true);
				begin = end + 1;
				if (users.size() > MODIFIEDOBJECTS_PACKAGE_SIZE + end)
				{
					end = end + MODIFIEDOBJECTS_PACKAGE_SIZE;
				}
				else
				{
					end = users.size();
				}
				subUsers.clear();

				for (int i = begin; i < end; i++)
				{
					String xml = users.get(i);
					if (isValidXMLUser(xml))
					{
						subUsers.add(xml);
					}
					else
					{
						String xml2 = makeValidXMLUser(xml);
						if (xml2 != null)
						{
							subUsers.add(xml2);
						}
					}
				}
			}
		}

	}

	/**
	 * Injest new aspects.
	 * @param monitor the monitor
	 * @throws XQException the xQ exception
	 * @throws UnsupportedEncodingException the unsupported encoding exception
	 * @throws PDRAlliesClientException the pDR allies client exception
	 * @throws InvalidIdentifierException the invalid identifier exception
	 */
	private void injestNewAspects(final IProgressMonitor monitor) throws Exception
	{
		synchronized (_dbCon)
		{
			Vector<String> aspects = new Vector<String>();

			for (String s : _mainSearcher.getNewAspects())
			{
				s = removeAspectPrefixes(s);
				aspects.add(s);
			}
			if (aspects.size() == 0)
			{
				return;
			}
			monitor.beginTask("Injesting new Aspects into Repository. Number of Objects: " + aspects.size(),
					aspects.size());

			int begin = 0;
			int end;
			if (aspects.size() > NEWOBJECTS_PACKAGE_SIZE)
			{
				end = NEWOBJECTS_PACKAGE_SIZE;
			}
			else
			{
				end = aspects.size();
			}
			Vector<String> subAspects = new Vector<String>(end);
			Vector<String> modifiedAspectIds = new Vector<String>();
			for (int i = begin; i < end; i++)
			{
				String xml = aspects.get(i);
				if (isValidXMLAspect(xml))
				{
					subAspects.add(xml);
				}
				else
				{
					String xml2 = makeValidXMLAspect(xml);
					if (xml2 != null)
					{
						subAspects.add(xml);
					}
				}
			}
			while (subAspects != null && !subAspects.isEmpty())
			{
				Map<Identifier, Identifier> idMap = Repository.ingestObjects(_repositoryId, _projectId, subAspects);

				if (!idMap.isEmpty())
				{
					String newID;
					for (Identifier id : idMap.keySet())
					{
						newID = idMap.get(id).toString();
						modifiedAspectIds = checkModfiedIds(aspects, id, idMap, begin, modifiedAspectIds);
						resetObjectId(id, newID, 1);
					}
				}
				monitor.worked(subAspects.size());
				begin = end;
				if (aspects.size() > NEWOBJECTS_PACKAGE_SIZE + end)
				{
					end = end + NEWOBJECTS_PACKAGE_SIZE;
				}
				else
				{
					end = aspects.size();
				}
				subAspects.clear();

				for (int i = begin; i < end; i++)
				{
					String xml = aspects.get(i);
					if (isValidXMLAspect(xml))
					{
						subAspects.add(xml);
					}
					else
					{
						String xml2 = makeValidXMLAspect(xml);
						if (xml2 != null)
						{
							subAspects.add(xml);
						}
					}
				}
			}
			System.out.println("modifiedAspectIds size " + modifiedAspectIds.size());
			if (modifiedAspectIds != null && !modifiedAspectIds.isEmpty())
			{
				_idService.insertIdModifiedObject(modifiedAspectIds, "pdrAo");
			}
		}
	}


	private String makeValidXMLAspect(String xml) {
		// check input
		boolean isValid = isValidXMLAspect(xml);
		
		if (!isValid)
		{
			String id = extractPdrId(xml);
			Aspect a = _facade.getAspect(new PdrId(id));
			String xml2 = "";
			try {
				xml2 = _xmlProc.writeToXML(a);
			} catch (XMLStreamException e) {
				log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e);
				iLogger.log(log);
				return null;
			}
			if (isValidXMLAspect(xml2))
			{
				try {
					_facade.saveAspect(a);
					if (isValidXMLAspect(xml2))
					{
						return xml2;
					}
				} catch (Exception e) {
					log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e);
					iLogger.log(log);
				}
			}
		}
		else
		{
			return xml;
		}
		return null;
	}
	
	private boolean isValidXMLAspect(String xml) {
		Source source = new StreamSource(new StringReader(xml));
		// check input
		boolean isValid = true;
		try {
		getAspectXMLValidator().validate(source);
		} catch (Exception e) {
			log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Not valid aspect xml exempted from synchronisation " + xml);
			iLogger.log(log);
			log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e);
			iLogger.log(log);


		isValid = false;
		}
		return isValid;
	}

	private Validator getAspectXMLValidator() {
		if (aspectXMLValidator == null)
		{
			// build the schema
			SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
			InputStream stream = this.getClass().getClassLoader().getResourceAsStream("/schemas/aodl.xsd");
			Schema schema;
			Source schemaSource = new StreamSource(stream);
			try {
				schema = factory.newSchema(schemaSource);
				aspectXMLValidator = schema.newValidator();

			} catch (SAXException e) {
				log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e);iLogger.log(log);
			}
		}
		return aspectXMLValidator;
	}
	private boolean isValidXMLPerson(String xml) {
		Source source = new StreamSource(new StringReader(xml));
		// check input
		boolean isValid = true;
		try {
		getPersonXMLValidator().validate(source);
		} catch (Exception e) {
			log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Not valid person xml exempted from synchronisation " + xml);
			iLogger.log(log);
			log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e);
			iLogger.log(log);
		isValid = false;
		}

		return isValid;
	}
	private String makeValidXMLPerson(String xml) {
		// check input
		boolean isValid = isValidXMLPerson(xml);
		
		if (!isValid)
		{
			String id = extractPdrId(xml);
			Person p = _facade.getPerson(new PdrId(id));
			String xml2 = "";
			try {
				xml2 = _xmlProc.writeToXML(p);
			} catch (XMLStreamException e) {
				log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e);
				iLogger.log(log);
				return null;
			}
			if (isValidXMLPerson(xml2))
			{
				try {
					_facade.savePerson(p);
					if (isValidXMLPerson(xml2))
					{
						return xml2;
					}
				} catch (Exception e) {
					log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e);
					iLogger.log(log);
				}
			}
		}
		else
		{
			return xml;
		}
		return null;
	}
	private Validator getPersonXMLValidator() {
		if (personXMLValidator == null)
		{
			SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
			InputStream stream = this.getClass().getClassLoader().getResourceAsStream("/schemas/podl.xsd");
			Schema schema;
			Source schemaSource = new StreamSource(stream);
			try {
				schema = factory.newSchema(schemaSource);
				personXMLValidator = schema.newValidator();

			} catch (SAXException e) {

				log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e);
				iLogger.log(log);}
		}
		return personXMLValidator;
	}
	private boolean isValidXMLUser(String xml) {
		Source source = new StreamSource(new StringReader(xml));
		// check input
		boolean isValid = true;
		try {
		getUserXMLValidator().validate(source);
		} catch (Exception e) {
			log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Not valid user xml exempted from synchronisation " + xml);
			iLogger.log(log);
			log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception " + e);
			iLogger.log(log);
		isValid = false;
		}

		return isValid;
	}
	private String makeValidXMLUser(String xml) {
		// check input
		boolean isValid = isValidXMLUser(xml);
		
		if (!isValid)
		{
			String id = extractPdrId(xml);
			User u = null;
			try {
				u = _userManager.getUserById(id);
			} catch (Exception e1) {
				log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e1);
				iLogger.log(log);
				return null;
			}
			
			String xml2 = "";
			try {
				UserXMLProcessor userXMLProc = new UserXMLProcessor();
				xml2 = userXMLProc.writeToXML(u);
			} catch (XMLStreamException e) {
				log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e);
				iLogger.log(log);
				return null;
			}
			if (isValidXMLUser(xml2))
			{
				try {
					_userManager.saveUser(u);
					if (isValidXMLUser(xml2))
					{
						return xml2;
					}
				} catch (Exception e) {
					log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e);
					iLogger.log(log);
				}
			}
		}
		else
		{
			return xml;
		}
		return null;
	}
	private Validator getUserXMLValidator() {
		if (userXMLValidator == null)
		{
			SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
			InputStream stream = this.getClass().getClassLoader().getResourceAsStream("/schemas/uodl.xsd");
			Schema schema;
			Source schemaSource = new StreamSource(stream);
			try {
				schema = factory.newSchema(schemaSource);
				userXMLValidator = schema.newValidator();

			} catch (SAXException e) {
				log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e);
				iLogger.log(log);			}
		}
		return userXMLValidator;
	}
	private boolean isValidXMLReference(String xml) {
		Source source = new StreamSource(new StringReader(xml));
		// check input
		boolean isValid = true;
		try {
		getReferenceXMLValidator().validate(source);
		} catch (Exception e) {
			log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Not valid reference xml exempted from synchronisation " + xml);
			iLogger.log(log);
			log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e);
			iLogger.log(log);
		isValid = false;
		}

		return isValid;
	}
	private String makeValidXMLReference(String xml) {
		// check input
		boolean isValid = isValidXMLReference(xml);
		
		if (!isValid)
		{
			String id = extractPdrId(xml);
			ReferenceMods r = _facade.getReference(new PdrId(id));
			String xml2 = "";
			try {
				xml2 = _xmlProc.writeToXML(r);
			} catch (XMLStreamException e) {
				log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e);
				iLogger.log(log);
				return null;
			}
			if (isValidXMLReference(xml2))
			{
				try {
					_facade.saveReference(r);
					return xml2;
				} catch (Exception e) {
					log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e);
					iLogger.log(log);
				}
			}
		}
		else
		{
			return xml;
		}
		return null;
	}
	private Validator getReferenceXMLValidator() {
		if (referenceXMLValidator == null)
		{
			SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
			InputStream stream = this.getClass().getClassLoader().getResourceAsStream("/schemas/rodl_mods.xsd");
			Schema schema;
			Source schemaSource = new StreamSource(stream);
			try {
				schema = factory.newSchema(schemaSource);
				referenceXMLValidator = schema.newValidator();

			} catch (SAXException e) {
				log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e);
				iLogger.log(log);			}
		}
		return referenceXMLValidator;
	}

	/**
	 * Injest new config.
	 * @throws XQException the xQ exception
	 * @throws XMLStreamException the xML stream exception
	 * @throws UnsupportedEncodingException the unsupported encoding exception
	 * @throws PDRAlliesClientException the pDR allies client exception
	 */
	private void injestNewConfig() throws XQException, XMLStreamException, UnsupportedEncodingException,
			PDRAlliesClientException
	{
		// synchronized (dbCon)
		// {
		// Vector<String> configProviders = _idService.getNewConfigs();
		// Vector<String> configs = _configManager.getConfigs(configProviders);
		// //XXX anpassen
		// if (configs != null && !configs.isEmpty())
		// Repository.ingestObjects(repositoryId, projectId, configs);
		// }

	}

	/**
	 * Injest new persons.
	 * @param monitor the monitor
	 * @throws XQException the xQ exception
	 * @throws UnsupportedEncodingException the unsupported encoding exception
	 * @throws PDRAlliesClientException the pDR allies client exception
	 * @throws InvalidIdentifierException the invalid identifier exception
	 */
	private void injestNewPersons(final IProgressMonitor monitor) throws Exception
	{
		synchronized (_dbCon)
		{
			Vector<String> persons = new Vector<String>();
			for (String s : _mainSearcher.getNewPersons())
			{
				s = removePersonPrefix(s);
				persons.add(s);
			}
			if (persons.size() == 0)
			{
				return;
			}
			monitor.beginTask("Injesting new Persons into Repository. Number of Objects: " + persons.size(),
					persons.size());

			int begin = 0;
			int end;
			if (persons.size() > NEWOBJECTS_PACKAGE_SIZE)
			{
				end = NEWOBJECTS_PACKAGE_SIZE;
			}
			else
			{
				end = persons.size();
			}
			Vector<String> subPersons = new Vector<String>(end);
			Vector<String> modifiedPersonsIds = new Vector<String>();
			for (int i = begin; i < end; i++)
			{
				String xml = persons.get(i);
				if (isValidXMLPerson(xml))
				{
					subPersons.add(xml);
				}
				else
				{
					String xml2 = makeValidXMLPerson(xml);
					if (xml2 != null)
					{
						subPersons.add(xml2);
					}
				}
			}
			while (subPersons != null && !subPersons.isEmpty())
			{
				Map<Identifier, Identifier> idMap = Repository.ingestObjects(_repositoryId, _projectId, subPersons);

				if (!idMap.isEmpty())
				{
					// System.out.println("size of map " + idMap.size());
					// XQConnection con = _dbCon.getConnection();
					// XQPreparedExpression xqp;
					// XQResultSequence xqs = null;
					// String replace;
					String newID;
					for (Identifier id : idMap.keySet())
					{
						newID = idMap.get(id).toString();
						modifiedPersonsIds = checkModfiedIds(persons, id, idMap, begin, modifiedPersonsIds);
						resetObjectId(id, newID, 2);

						// renameObject(id, newID);
						//
						// System.out.println("map old " + id.toString() +
						// " new " + newID);
						// replace =
						// "declare namespace podl=\"http://pdr.bbaw.de/namespaces/podl/\";\n"
						// +
						// "for $x in collection(\"person\")/podl:person//@*[.='"
						// + id.toString() + "']\n"
						// + "let $new := '" + newID + "'\n" +
						// "return replace value of node $x with $new";
						// System.out.println(replace);
						// xqp = con.prepareExpression(replace);
						// xqs = xqp.executeQuery();
						//
						// // replace =
						// //
						// "declare namespace podl=\"http://pdr.bbaw.de/namespaces/podl/\";\n"
						// // +
						// //
						// "for $x in collection(\"person\")/podl:person//*[.='"
						// // + id.toString() + "']\n" +
						// // "let $new := '" + idMap.get(id).toString() + "'\n"
						// +
						// // "return replace value of node $x with $new";
						// // System.out.println(replace);
						// // xqp = con.prepareExpression(replace);
						// // xqs = xqp.executeQuery();
						//
						// replace =
						// "declare namespace aodl=\"http://pdr.bbaw.de/namespaces/aodl/\";\n"
						// +
						// "for $x in collection(\"aspect\")/aodl:aspect//@*[.='"
						// + id.toString() + "']\n"
						// + "let $new := '" + idMap.get(id).toString() + "'\n"
						// + "return replace value of node $x with $new";
						// System.out.println(replace);
						// xqp = con.prepareExpression(replace);
						// xqs = xqp.executeQuery();
						//
						// // replace =
						// //
						// "declare namespace aodl=\"http://pdr.bbaw.de/namespaces/aodl/\";\n"
						// // +
						// //
						// "for $x in collection(\"aspect\")/aodl:aspect//*[.='"
						// // + id.toString() + "']\n" +
						// // "let $new := '" + idMap.get(id).toString() + "'\n"
						// +
						// // "return replace value of node $x with $new";
						// // System.out.println(replace);
						// // xqp = con.prepareExpression(replace);
						// // xqs = xqp.executeQuery();
					}
					// xqs.close();
					// _dbCon.optimize("person");
					// _dbCon.optimize("aspect");
					//
					// con.close();
				}
				monitor.worked(subPersons.size());
				begin = end;
				if (persons.size() > NEWOBJECTS_PACKAGE_SIZE + end)
				{
					end = end + NEWOBJECTS_PACKAGE_SIZE;
				}
				else
				{
					end = persons.size();
				}
				subPersons.clear();

				for (int i = begin; i < end; i++)
				{
					String xml = persons.get(i);
					if (isValidXMLPerson(xml))
					{
						subPersons.add(xml);
					}
					else
					{
						String xml2 = makeValidXMLPerson(xml);
						if (xml2 != null)
						{
							subPersons.add(xml2);
						}
					}
				}
			}
			if (modifiedPersonsIds != null && !modifiedPersonsIds.isEmpty())
			{
				_idService.insertIdModifiedObject(modifiedPersonsIds, "pdrPo");
			}
		}
	}

	/**
	 * Injest new references.
	 * @param monitor the monitor
	 * @throws XQException the xQ exception
	 * @throws UnsupportedEncodingException the unsupported encoding exception
	 * @throws PDRAlliesClientException the pDR allies client exception
	 * @throws InvalidIdentifierException the invalid identifier exception
	 */
	private void injestNewReferences(final IProgressMonitor monitor) throws Exception
	{
		synchronized (_dbCon)
		{
			Vector<String> references = _mainSearcher.getNewReferences();
			if (references.size() == 0)
			{
				return;
			}
			monitor.beginTask("Injesting new References into Repository. Number of Objects: " + references.size(),
					references.size());

			int begin = 0;
			int end;
			if (references.size() > NEWOBJECTS_PACKAGE_SIZE)
			{
				end = NEWOBJECTS_PACKAGE_SIZE;
			}
			else
			{
				end = references.size();
			}
			Vector<String> subReferences = new Vector<String>(end);
			Vector<String> modifiedReferenceIds = new Vector<String>();
			String ref;
			for (int i = begin; i < end; i++)
			{
				ref = references.get(i);
				ref = removeRefPrefix(ref);
				if (isValidXMLReference(ref))
				{
					subReferences.add(ref);
				}
				else
				{
					String xml2 = makeValidXMLReference(ref);
					if (xml2 != null)
					{
						subReferences.add(xml2);
					}
				}
			}
			while (subReferences != null && !subReferences.isEmpty())
			{
				Map<Identifier, Identifier> idMap = Repository.ingestObjects(_repositoryId, _projectId, subReferences);
				if (!idMap.isEmpty())
				{
					// System.out.println("size of map " + idMap.size());
					// XQConnection con = _dbCon.getConnection();
					// XQPreparedExpression xqp;
					// XQResultSequence xqs = null;
					// String replace;
					String newID;
					for (Identifier id : idMap.keySet())
					{
						newID = idMap.get(id).toString();
						modifiedReferenceIds = checkModfiedIds(references, id, idMap, begin, modifiedReferenceIds);
						resetObjectId(id, newID, 3);

						// renameObject(id, newID);
						//
						// System.out.println("map old " + id.toString() +
						// " new " + newID);
						// replace =
						// "declare namespace mods=\"http://www.loc.gov/mods/v3\";\n"
						// +
						// "for $x in collection(\"reference\")/mods:mods//@*[.='"
						// + id.toString() + "']\n"
						// + "let $new := '" + newID + "'\n" +
						// "return replace value of node $x with $new";
						// System.out.println(replace);
						// xqp = con.prepareExpression(replace);
						// xqs = xqp.executeQuery();
						//
						// // replace =
						// // "for $x in collection(\"reference\")/mods//*[.='"
						// +
						// // id.toString() + "']\n" +
						// // "let $new := '" + idMap.get(id).toString() + "'\n"
						// +
						// // "return replace value of node $x with $new";
						// // System.out.println(replace);
						// // xqp = con.prepareExpression(replace);
						// // xqs = xqp.executeQuery();
						//
						// replace =
						// "declare namespace podl=\"http://pdr.bbaw.de/namespaces/podl/\";\n"
						// +
						// "for $x in collection(\"person\")/podl:person//@*[.='"
						// + id.toString() + "']\n"
						// + "let $new := '" + newID + "'\n" +
						// "return replace value of node $x with $new";
						// System.out.println(replace);
						// xqp = con.prepareExpression(replace);
						// xqs = xqp.executeQuery();
						//
						// replace =
						// "declare namespace podl=\"http://pdr.bbaw.de/namespaces/podl/\";\n"
						// +
						// "for $x in collection(\"person\")/podl:person//podl:reference[.='"
						// + id.toString()
						// + "']\n" + "let $new := '" + newID + "'\n"
						// + "return replace value of node $x with $new";
						// System.out.println(replace);
						// xqp = con.prepareExpression(replace);
						// xqs = xqp.executeQuery();
						//
						// replace =
						// "declare namespace aodl=\"http://pdr.bbaw.de/namespaces/aodl/\";\n"
						// +
						// "for $x in collection(\"aspect\")/aodl:aspect//@*[.='"
						// + id.toString() + "']\n"
						// + "let $new := '" + newID + "'\n" +
						// "return replace value of node $x with $new";
						// System.out.println(replace);
						// xqp = con.prepareExpression(replace);
						// xqs = xqp.executeQuery();
						//
						// replace =
						// "declare namespace aodl=\"http://pdr.bbaw.de/namespaces/aodl/\";\n"
						// +
						// "for $x in collection(\"aspect\")/aodl:aspect//aodl:reference[.='"
						// + id.toString()
						// + "']\n" + "let $new := '" + newID + "'\n"
						// + "return replace value of node $x with $new";
						// System.out.println(replace);
						// xqp = con.prepareExpression(replace);
						// xqs = xqp.executeQuery();
					}
					// xqs.close();
					// _dbCon.optimize("person");
					// _dbCon.optimize("aspect");
					// _dbCon.optimize("reference");
					//
					// con.close();
				}
				monitor.worked(subReferences.size());

				begin = end;
				if (references.size() > NEWOBJECTS_PACKAGE_SIZE + end)
				{
					end = end + NEWOBJECTS_PACKAGE_SIZE;
				}
				else
				{
					end = references.size();
				}
				subReferences.clear();

				for (int i = begin; i < end; i++)
				{
					ref = references.get(i);
					ref = removeRefPrefix(ref);
					if (isValidXMLReference(ref))
					{
						subReferences.add(ref);
					}
					else
					{
						String xml2 = makeValidXMLReference(ref);
						if (xml2 != null)
						{
							subReferences.add(xml2);
						}
					}
				}
			}
			if (modifiedReferenceIds != null && !modifiedReferenceIds.isEmpty())
			{
				_idService.insertIdModifiedObject(modifiedReferenceIds, "pdrRo");
			}
		}
	}

	/**
	 * Injest new users.
	 * @throws UnsupportedEncodingException the unsupported encoding exception
	 * @throws PDRAlliesClientException the pDR allies client exception
	 * @throws XQException the xQ exception
	 * @throws XMLStreamException the xML stream exception
	 * @throws InvalidIdentifierException the invalid identifier exception
	 */
	private final void injestNewUsers(String userId, String password) throws Exception
	{
		synchronized (_dbCon)
		{
			Vector<String> users = getNewUsers();
			int begin = 0;
			int end;
			if (users.size() > NEWOBJECTS_PACKAGE_SIZE)
			{
				end = NEWOBJECTS_PACKAGE_SIZE;
			}
			else
			{
				end = users.size();
			}
			Vector<String> subUsers = new Vector<String>(end);
			Vector<String> modifiedUserIds = new Vector<String>();
			Vector<String> standardUsers = new Vector<String>(9);
			for (int i = begin; i < end; i++)
			{
				if (new Integer(extractPdrId(users.get(i)).substring(14)) <= 9)
				{
					standardUsers.add(users.get(i));
				}
				else
				{
					String xml = users.get(i);
					if (isValidXMLUser(xml))
					{
						subUsers.add(xml);
					}
					else
					{
						String xml2 = makeValidXMLUser(xml);
						if (xml2 != null)
						{
							subUsers.add(xml2);
						}
					}
					
				}
			}
			while (subUsers != null && !subUsers.isEmpty())
			{
				Map<Identifier, Identifier> idMap = Repository.ingestObjects(_repositoryId, _projectId, subUsers);
				if (!idMap.isEmpty())
				{
					// System.out.println("size of map " + idMap.size());
					String newID;
					for (Identifier id : idMap.keySet())
					{
						newID = idMap.get(id).toString();
						modifiedUserIds = checkModfiedIds(users, id, idMap, begin, modifiedUserIds);
						// renameObject(id, newID);

						resetObjectId(id, newID, 4);

					}
				}

				begin = end;
				if (users.size() > NEWOBJECTS_PACKAGE_SIZE + end)
				{
					end = end + NEWOBJECTS_PACKAGE_SIZE;
				}
				else
				{
					end = users.size();
				}
				subUsers.clear();

				for (int i = begin; i < end; i++)
				{
					if (new Integer(extractPdrId(users.get(i)).substring(14)) <= 9)
					{
						standardUsers.add(users.get(i));
					}
					else
					{
						String xml = users.get(i);
						if (isValidXMLUser(xml))
						{
							subUsers.add(xml);
						}
						else
						{
							String xml2 = makeValidXMLUser(xml);
							if (xml2 != null)
							{
								subUsers.add(xml2);
							}
						}
					}
				}
			}
			if (modifiedUserIds != null && !modifiedUserIds.isEmpty())
			{
				_idService.insertIdModifiedObject(modifiedUserIds, "pdrUo");
			}
			if (!standardUsers.isEmpty())
			{
				checkAndInjestStandardUsers(standardUsers, userId, password);
			}
		}

	}

	/**
	 * Insert conflicting objects.
	 * @param conObjects the con objects
	 * @param monitor the monitor
	 * @throws XMLStreamException the xML stream exception
	 * @throws UnsupportedEncodingException the unsupported encoding exception
	 * @throws PDRAlliesClientException the pDR allies client exception
	 * @throws XQException the xQ exception
	 */
	private void insertConflictingObjects(Vector<PDRObjectsConflict> conObjects, IProgressMonitor monitor)
			throws Exception
	{
		Vector<String> keepLocalObjects = new Vector<String>();

		String object = null;
		for (PDRObjectsConflict oc : conObjects)
		{
			if (oc.isKeepLocal() && oc.getLocalObject() != null)
			{
				if (oc.getLocalObject() instanceof Aspect)
				{
					object = removeAspectPrefixes(_xmlProc.writeToXML(oc.getLocalObject()));
				}
				if (oc.getLocalObject() instanceof Person)
				{
					object = removePersonPrefix(_xmlProc.writeToXML(oc.getLocalObject()));
				}
				if (oc.getLocalObject() instanceof ReferenceMods)
				{
					object = _xmlProc.writeToXML(oc.getLocalObject());
				}

				if (object != null)
				{
					keepLocalObjects.add(object);
				}
			}
			else if (oc.isOverrideLocal())
			{
				_dbManager.saveToDB(oc.getRepositoryObject(), false);
				monitor.worked(1);
			}
			else if (oc.getLocalObject() != null) // resolve conflict later, save id to be treated as modified.
			{
				_idService.insertIdModifiedObject(oc.getLocalObject().getPdrId());
			}
		}
		if (keepLocalObjects != null && !keepLocalObjects.isEmpty())
		{
			Repository.modifyObjects(_repositoryId, _projectId, keepLocalObjects, true);
			monitor.worked(keepLocalObjects.size());
		}

	}

	/**
	 * Proccess update states.
	 * @param idRanges the id ranges
	 * @param size the size
	 * @throws Exception
	 */
	private void proccessUpdateStates(final Vector<IDRange> idRanges, final int size) throws Exception
	{
		if (!idRanges.isEmpty())
		{
			Vector<String> ids;
			if (size > 0)
			{
				ids = new Vector<String>(size);
			}
			else
			{
				ids = new Vector<String>(20);
			}
			String type = null;
			HashMap<String, Integer> updateState = null;
			PdrId id;
			if (idRanges.firstElement().getType() == PDRType.ASPECT)
			{
				type = "pdrAo";
				try
				{
					updateState = _facade.getAspectsUpdateState();
				}
				catch (Exception e)
				{
					log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e);
					iLogger.log(log);				}
			}
			else if (idRanges.firstElement().getType() == PDRType.PERSON)
			{
				type = "pdrPo";
				try
				{
					updateState = _facade.getPersonsUpdateState();
				}
				catch (Exception e)
				{
					log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e);
					iLogger.log(log);
				}

			}
			else if (idRanges.firstElement().getType() == PDRType.REFERENCE)
			{
				type = "pdrRo";
				try
				{
					updateState = _facade.getReferencesUpdateState();
				}
				catch (Exception e)
				{
					log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e);
					iLogger.log(log);				}

			}
			// System.out.println(type);
			for (IDRange range : idRanges)
			{
				for (int i = range.getLowerBound(); i <= range.getUpperBound(); i++)
				{
					id = new PdrId(type, _repositoryId, _projectId, i);
					ids.add(id.toString());
					updateState.put(id.toString(), 1);
				}
			}
			_idService.insertIdUpdatedObjects(ids, type);
		}
	}

	/**
	 * Removes the aspect prefixes.
	 * @param s the s
	 * @return the string
	 */
	private String removeAspectPrefixes(String s)
	{
		Pattern openP = Pattern.compile("<aodl:");
		Pattern closeP = Pattern.compile("<\\/aodl:");
		Pattern nameP = Pattern.compile("xmlns:aodl");
		Pattern lb = Pattern.compile("\\r\\n");
		Pattern tab = Pattern.compile(">\\s{2,}<");

		Matcher m = openP.matcher(s);
		s = m.replaceAll("<");
		m = closeP.matcher(s);
		s = m.replaceAll("</");
		m = nameP.matcher(s);
		s = m.replaceAll("xmlns");
		// m = lb.matcher(s);
		// s = m.replaceAll("");
		m = tab.matcher(s);
		s = m.replaceAll("> <");
		// System.out.println("replaced aspect " + s);
		return s;
	}

	/**
	 * Removes the person prefix.
	 * @param personString the person string
	 * @return the string
	 */
	private String removePersonPrefix(String personString)
	{
		Pattern openP = Pattern.compile("<podl:");
		Pattern closeP = Pattern.compile("<\\/podl:");
		Pattern nameP = Pattern.compile("xmlns:podl");
		Pattern lb = Pattern.compile("\\r\\n");
		Pattern tab = Pattern.compile(">\\s+?<");

		Matcher m = openP.matcher(personString);
		personString = m.replaceAll("<");
		m = closeP.matcher(personString);
		personString = m.replaceAll("</");
		m = nameP.matcher(personString);
		personString = m.replaceAll("xmlns");

		// m = lb.matcher(personString);
		// personString = m.replaceAll("");

		m = tab.matcher(personString);
		personString = m.replaceAll("><");
		// System.out.println("replaced person " + personString);

		return personString;

	}

	/**
	 * Removes the ref prefix.
	 * @param s the s
	 * @return the string
	 */
	private String removeRefPrefix(String s)
	{
		Pattern openP = Pattern.compile("<mods:");
		Pattern closeP = Pattern.compile("<\\/mods:");
		Pattern nameP = Pattern.compile("xmlns:mods");
		Pattern lb = Pattern.compile("\\r\\n");
		Pattern tab = Pattern.compile(">\\s+?<");

		Matcher m = openP.matcher(s);
		s = m.replaceAll("<");
		m = closeP.matcher(s);
		s = m.replaceAll("</");
		m = nameP.matcher(s);
		s = m.replaceAll("xmlns");
		// m = lb.matcher(s);
		// s = m.replaceAll("");
		m = tab.matcher(s);
		s = m.replaceAll("><");

		// System.out.println("replaced ref " + s);
		return s;
	}

	/**
	 * Removes the user prefix.
	 * @param s the s
	 * @return the string
	 */
	private String removeUserPrefix(String s)
	{
		Pattern openP = Pattern.compile("<uodl:");
		Pattern closeP = Pattern.compile("<\\/uodl:");
		Pattern nameP = Pattern.compile("xmlns:uodl");
		Pattern tab = Pattern.compile(">\\s+?<");
		Pattern begin = Pattern.compile("<[?].*?[?]>");

		Matcher m = openP.matcher(s);
		s = m.replaceAll("<");
		m = closeP.matcher(s);
		s = m.replaceAll("</");
		m = nameP.matcher(s);
		s = m.replaceAll("xmlns");

		// m = lb.matcher(s);
		// s = m.replaceAll("");
		m = tab.matcher(s);
		s = m.replaceAll("><");
		m = begin.matcher(s);
		s = m.replaceAll("");
		// System.out.println("replaced user " + s);
		return s;
	}
	
	/**
	 * Removes the user prefix.
	 * @param s the s
	 * @return the string
	 */
	private String addUserPrefix(String s)
	{
		XMLReader xmlReader;
		try {
			xmlReader = new XMLFilterImpl(XMLReaderFactory.createXMLReader()) {
			    String namespace = "http://pdr.bbaw.de/namespaces/uodl/";
			    String pref = "uodl:";

			    @Override
			    public void startElement(String uri, String localName, String qName, Attributes atts)
			            throws SAXException {
			        super.startElement(namespace, localName, pref + qName, atts);
			    }

			    @Override
			    public void endElement(String uri, String localName, String qName) throws SAXException {
			        super.endElement(namespace, localName, pref + qName);
			    }
			};
			TransformerFactory tf = TransformerFactory.newInstance();
	        Transformer t;
	        StringWriter sw = new StringWriter();

			try {
				t = tf.newTransformer();
				t.transform(new SAXSource(xmlReader, new InputSource(new StringReader(s))), new StreamResult(sw));
				String str = sw.toString().substring(38);
				Pattern ns = Pattern.compile("xmlns=\"http://pdr.bbaw.de/namespaces/uodl/\"");

				Matcher m = ns.matcher(str);
				str = m.replaceAll("");
				
				return str;
			} catch (TransformerConfigurationException e) {
				log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e);
				iLogger.log(log);
				return s;
			} catch (TransformerException e) {
				log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e);
				iLogger.log(log);
				return s;
			}
		} catch (SAXException e1) {

			log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e1);
			iLogger.log(log);
			}
		return s;
        
	}

	/**
	 * Rename object.
	 * @param oldId the old id
	 * @param newID the new id
	 * @throws Exception
	 */
	private void renameObject(final Identifier oldId, final String newID) throws Exception
	{
		String xml;
		String col = null;
		if (oldId.getType().equals(PDRType.ASPECT))
		{
			col = "aspect";
		}
		if (oldId.getType().equals(PDRType.PERSON))
		{
			col = "person";
		}
		if (oldId.getType().equals(PDRType.REFERENCE))
		{
			col = "reference";
		}
		if (oldId.getType().equals(PDRType.USER))
		{
			col = "users";
		}

		if (col != null)
		{
			xml = _mainSearcher.getObjectXML(oldId.toString(), col);
			// System.out.println("renameobject xml " + xml);
			if (xml != null && xml.trim().length() > 0)
			{
				System.out.println("delete " + oldId.toString());
				_dbCon.delete(oldId.toString(), col);
				System.out.println("store newid " + newID.toString());
				_dbCon.store2DB(xml, col, newID.toString() + ".xml", true);
				System.out.println("done");
			}

		}

	}

	/**
	 * Sets the parsed aspect.
	 * @param parsedAspect the new parsed aspect
	 */
	public final void setParsedAspect(final Aspect parsedAspect)
	{
		this._parsedAspect = parsedAspect;
	}

	/**
	 * Sets the parsed person.
	 * @param parsedPerson the new parsed person
	 */
	public final void setParsedPerson(final Person parsedPerson)
	{
		this._parsedPerson = parsedPerson;
	}

	/**
	 * Sets the parsed reference.
	 * @param parsedReference the new parsed reference
	 */
	public void setParsedReference(final ReferenceMods parsedReference)
	{
		this._parsedReference = parsedReference;
	}

	@Override
	public final IStatus updateAllData(final String userID, final String password, final IProgressMonitor monitor)
			throws Exception
	{
		IStatus updateStatus = Status.OK_STATUS;
		Date currentUpdate;

		
		String url = Platform.getPreferencesService().getString(CommonActivator.PLUGIN_ID, "REPOSITORY_URL",
				AEConstants.REPOSITORY_URL, null);
		_repositoryId = Platform.getPreferencesService().getInt(CommonActivator.PLUGIN_ID, "REPOSITORY_ID",
				AEConstants.REPOSITORY_ID, null);
		_projectId = Platform.getPreferencesService().getInt(CommonActivator.PLUGIN_ID, "PROJECT_ID",
				AEConstants.PROJECT_ID, null);
		Configuration.getInstance().setAxis2BaseURL(url.toString());
		Configuration.getInstance().setPDRUser(userID, password);
		log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "url " + url.toString() + " userID " + userID + " p "
				+ password);
		iLogger.log(log);
		boolean success = true;
		boolean test;
		// injest new objects
		try
		{
			injestNewUsers(userID, password);
		}
		catch (UnsupportedEncodingException e1)
		{
			log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e1);
			iLogger.log(log);
			success = false;
		}
		catch (PDRAlliesClientException e1)
		{
			updateStatus = Status.CANCEL_STATUS;
			success = false;
			log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e1);
			iLogger.log(log);		}
		catch (XQException e1)
		{
			success = false;
			log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e1);
			iLogger.log(log);		}
		catch (XMLStreamException e1)
		{
			log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e1);
			iLogger.log(log);
			success = false;
		}
		catch (InvalidIdentifierException e1)
		{
			success = false;
			log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e1);
			iLogger.log(log);
		}
		
		// new project, local user still does not yet exist in repository
		if (!success)
		{
			//use default user to injest new users.
			Configuration.getInstance().setPDRUser("pdrUo.001.002.000000001", "pdrrdp");
			success = true;
			try
			{
				injestNewUsers(userID, password);
			}
			catch (UnsupportedEncodingException e1)
			{
				log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e1);
				iLogger.log(log);
				success = false;
			}
			catch (PDRAlliesClientException e1)
			{
				updateStatus = Status.CANCEL_STATUS;
				success = false;
				log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e1);
				iLogger.log(log);
				}
			catch (XQException e1)
			{
				success = false;
				log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e1);
				iLogger.log(log);
			}
			catch (XMLStreamException e1)
			{
				success = false;
				log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e1);
				iLogger.log(log);
			}
			catch (InvalidIdentifierException e1)
			{
				success = false;
				log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e1);
				iLogger.log(log);
			}
			Configuration.getInstance().setPDRUser(userID, password);

		}
		try
		{
			injestNewReferences(monitor);
		}
		catch (UnsupportedEncodingException e1)
		{
			success = false;
			log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e1);
			iLogger.log(log);
			}
		catch (XQException e1)
		{
			success = false;
			log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e1);
			iLogger.log(log);
			}
		catch (PDRAlliesClientException e1)
		{
			updateStatus = Status.CANCEL_STATUS;
			success = false;
			log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e1);
			iLogger.log(log);
			}
		catch (InvalidIdentifierException e1)
		{
			success = false;

			log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e1);
			iLogger.log(log);}
		try
		{
			injestNewPersons(monitor);
		}
		catch (UnsupportedEncodingException e1)
		{
			success = false;

			log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e1);
			iLogger.log(log);
			}
		catch (XQException e1)
		{
			success = false;
			log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e1);
			iLogger.log(log);
			}
		catch (PDRAlliesClientException e1)
		{
			updateStatus = Status.CANCEL_STATUS;
			success = false;
			log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e1);
			iLogger.log(log);
			}
		catch (InvalidIdentifierException e1)
		{
			success = false;
			log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e1);
			iLogger.log(log);
			}
		try
		{
			injestNewAspects(monitor);
		}
		catch (UnsupportedEncodingException e1)
		{
			success = false;
			log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e1);
			iLogger.log(log);
			}
		catch (XQException e1)
		{
			success = false;
			log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e1);
			iLogger.log(log);
			}
		catch (PDRAlliesClientException e1)
		{
			updateStatus = Status.CANCEL_STATUS;
			success = false;
			log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e1);
			iLogger.log(log);
			}
		catch (InvalidIdentifierException e1)
		{
			success = false;
			log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e1);
			iLogger.log(log);
			}

		// injest modified configs
		try
		{
			injestModifiedConfig();
		}
		catch (UnsupportedEncodingException e1)
		{
			success = false;
			log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e1);
			iLogger.log(log);
			}
		catch (XQException e1)
		{
			success = false;
			log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e1);
			iLogger.log(log);
			}
		catch (PDRAlliesClientException e1)
		{
			updateStatus = Status.CANCEL_STATUS;
			success = false;
			log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e1);
			iLogger.log(log);
			}
		catch (XMLStreamException e1)
		{
			success = false;
			log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e1);
			iLogger.log(log);
			}

		// injest midified objects
		try
		{
			injestModifiedUsers();
		}
		catch (UnsupportedEncodingException e1)
		{
			success = false;
			log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e1);
			iLogger.log(log);
			}
		catch (PDRAlliesClientException e1)
		{
			updateStatus = Status.CANCEL_STATUS;
			success = false;
			log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e1);
			iLogger.log(log);
			}
		catch (XQException e1)
		{
			success = false;
			log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e1);
			iLogger.log(log);
			}
		catch (XMLStreamException e1)
		{
			success = false;
			log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e1);
			iLogger.log(log);
			}
		try
		{
			injestModifiedReferences(monitor);
		}
		catch (UnsupportedEncodingException e1)
		{
			success = false;
			log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e1);
			iLogger.log(log);
			}
		catch (PDRAlliesClientException e1)
		{
			updateStatus = Status.CANCEL_STATUS;
			success = false;
			log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e1);
			iLogger.log(log);
			}
		catch (XQException e1)
		{
			success = false;
			log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e1);
			iLogger.log(log);
			}
		catch (XMLStreamException e1)
		{
			success = false;
			log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e1);
			iLogger.log(log);
			}
		try
		{
			injestModifiedPersons(monitor);
		}
		catch (UnsupportedEncodingException e1)
		{
			success = false;
			log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e1);
			iLogger.log(log);
			}
		catch (PDRAlliesClientException e1)
		{
			updateStatus = Status.CANCEL_STATUS;
			success = false;
			log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e1);
			iLogger.log(log);
			}
		catch (XQException e1)
		{
			success = false;
			log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e1);
			iLogger.log(log);
			}
		catch (XMLStreamException e1)
		{
			success = false;
			log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e1);
			iLogger.log(log);
		}
		try
		{
			injestModifiedAspects(monitor);
		}
		catch (UnsupportedEncodingException e1)
		{
			success = false;

			log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e1);
			iLogger.log(log);
			}
		catch (XQException e1)
		{
			success = false;
			log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e1);iLogger.log(log);
			}
		catch (PDRAlliesClientException e1)
		{
			updateStatus = Status.CANCEL_STATUS;
			success = false;
			log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e1);iLogger.log(log);
		}
		catch (XMLStreamException e1)
		{
			success = false;
			log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e1);iLogger.log(log);
		}
		
		
		if ((_conflictingRepAspects != null && !_conflictingRepAspects.isEmpty())
				|| (_conflictingRepPersons != null && !_conflictingRepPersons.isEmpty())
				|| (_conflictingRepReferences != null && !_conflictingRepReferences.isEmpty()))
		{
			// System.out.println("handle conflicts asp " +
			// _conflictingRepAspects.size() + " pers "
			// + _conflictingRepPersons.size() + " refs " +
			// _conflictingRepReferences.size());

			 
			 handleObjectsConflicts(monitor);
			
			
		}
		// injest process completed. clear update states
		else if (success)
		{
			try
			{
				_idService.clearAllUpdateStates();
			}
			catch (XQException e1)
			{
				// TODO Auto-generated catch block
				log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e1);iLogger.log(log);
			}
		}

				// get all new or modified data

				// XXX neue oder modifizierte configs holen
				try
				{
					getModifiedConfig();
				}
				catch (PDRAlliesClientException e1)
				{
					updateStatus = Status.CANCEL_STATUS;
					success = false;
					log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e1);iLogger.log(log);
				}

		// Date lastUpdate = null;
		// try {
		// lastUpdate =
		// AEConstants.ADMINDATE_FORMAT.parse("2011-03-01T01:00:00");
		// } catch (ParseException e) {
		//
		// log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e);iLogger.log(log);
		// }//
		Date lastUpdate = _idService.getUpdateTimeStamp();
		log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "lastUpdate timestamp: "
				+ AEConstants.ADMINDATE_FORMAT.format(lastUpdate));
		iLogger.log(log);
		// getAll users
		try
		{
			updateUsers(userID, password, monitor);
		}
		catch (Exception e)
		{
			log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e);iLogger.log(log);
			success = false;
		}

		if (monitor.isCanceled())
		{
			success = false;
			return Status.CANCEL_STATUS;

		}
		try
		{
			currentUpdate = AEConstants.ADMINDATE_FORMAT.parse(Repository.getTime());
		}
		catch (Exception e)
		{
			currentUpdate = _facade.getCurrentDate();
		}
		log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "currentUpdate timestamp: "
				+ AEConstants.ADMINDATE_FORMAT.format(currentUpdate));
		iLogger.log(log);

		if (lastUpdate.after(AEConstants.FIRST_EVER_UPDATE_TIMESTAMP))
		{
			try
			{
				updateModifiedObjects(monitor, lastUpdate);
			}
			catch (UnsupportedEncodingException e)
			{
				success = false;
				log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e);iLogger.log(log);
			}
			catch (XQException e)
			{
				success = false;
				log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e);iLogger.log(log);
			}
			catch (PDRAlliesClientException e)
			{
				updateStatus = Status.CANCEL_STATUS;
				log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e);iLogger.log(log);
			}
		}
		else
		{
			try
			{
				updateAllOccupiedObjects(monitor);
			}
			catch (UnsupportedEncodingException e)
			{
				success = false;
				log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e);iLogger.log(log);
			}
			catch (PDRAlliesClientException e)
			{
				updateStatus = Status.CANCEL_STATUS;
				success = false;
				log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e);iLogger.log(log);
			}
		}
		if (success)
		{
			try
			{
				_idService.setUpdateTimeStamp(currentUpdate);
				log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "1new date timestamp: "
						+ AEConstants.ADMINDATE_FORMAT.format(currentUpdate));
				iLogger.log(log);
				log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "2new date timestamp: "
						+ AEConstants.ADMINDATE_FORMAT.format(_idService.getUpdateTimeStamp()));
				iLogger.log(log);
			}
			catch (XQException e)
			{
				log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "update not successful ");
				iLogger.log(log);
				log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e);iLogger.log(log);
			}
		}

		monitor.done();
		return updateStatus;

	}

	/**
	 * Update all occupied objects.
	 * @param monitor the monitor
	 * @return the i status
	 * @throws Exception
	 */
	private IStatus updateAllOccupiedObjects(final IProgressMonitor monitor) throws Exception
	{
		String col = "util";
		String name;
		int totalWork = 0;
		int totalPersons = 0;
		int totalAspects = 0;
		int totalReferences = 0;
		Vector<IDRange> personRanges;
		Vector<IDRange> aspectRanges;
		Vector<IDRange> referenceRanges;

		personRanges = Utilities.getOccupiedObjectIDRanges(PDRType.PERSON, _repositoryId, _projectId, 1,
				MAX_OBJECT_NUMBER);
		aspectRanges = Utilities.getOccupiedObjectIDRanges(PDRType.ASPECT, _repositoryId, _projectId, 1,
				MAX_OBJECT_NUMBER);
		referenceRanges = Utilities.getOccupiedObjectIDRanges(PDRType.REFERENCE, _repositoryId, _projectId, 1,
				MAX_OBJECT_NUMBER);
		// calculate total work
		if (personRanges != null && !personRanges.isEmpty())
		{
			for (IDRange range : personRanges)
			{
				totalPersons = totalPersons + range.getUpperBound() - range.getLowerBound();
			}
		}
		if (aspectRanges != null && !aspectRanges.isEmpty())
		{
			for (IDRange range : aspectRanges)
			{
				totalAspects = totalAspects + range.getUpperBound() - range.getLowerBound();
			}
		}
		if (referenceRanges != null && !referenceRanges.isEmpty())
		{
			for (IDRange range : referenceRanges)
			{
				totalReferences = totalReferences + range.getUpperBound() - range.getLowerBound();
			}
		}
		totalWork = totalPersons + totalAspects + totalReferences;
		monitor.beginTask("Updating from Repository. Number of Objects: " + totalWork, totalWork);
		if (monitor.isCanceled())
		{
			return Status.CANCEL_STATUS;
		}
		col = "person";
		int lowerBound = 1;
		int upperBound = 1;
		synchronized (_dbCon)
		{
			_dbCon.openCollection(col);
			for (IDRange range : personRanges)
			{
				log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "persons range " + range.getLowerBound()
						+ " upper b " + range.getUpperBound());
				iLogger.log(log);
				lowerBound = range.getLowerBound();

				while (upperBound < range.getUpperBound())
				{
					if (range.getUpperBound() - lowerBound <= PACKAGE_SIZE)
					{
						upperBound = range.getUpperBound();
					}
					else
					{
						upperBound = lowerBound + PACKAGE_SIZE;
					}
					monitor.subTask("Updating " + totalPersons + " Persons from Repository " + upperBound);
					Vector<String> objs = Utilities.getObjects(PDRType.PERSON, _repositoryId, _projectId, lowerBound,
							upperBound);
					for (String s : objs)
					{
						name = extractPdrId(s) + ".xml";
						_dbCon.storeQuick2DB(s, col, name);
						s = null;
						monitor.worked(1);
					}
					lowerBound = Math.min(lowerBound + 250, range.getUpperBound());
					if (monitor.isCanceled())
					{
						return Status.CANCEL_STATUS;
					}
				}
			}
			_dbCon.closeDB(col);
		}
		if (monitor.isCanceled())
		{
			monitor.subTask("Optimizing Database Index");
			col = "person";
			_dbCon.optimize(col);
			try
			{
				proccessUpdateStates(personRanges, totalPersons);
			}
			catch (XQException e)
			{

				log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e);iLogger.log(log);
			}
			monitor.done();
			return Status.CANCEL_STATUS;
		}
		// aspect
		col = "aspect";
		lowerBound = 1;
		upperBound = 1;

		for (IDRange range : aspectRanges)
		{
			log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "aspects range " + range.getLowerBound() + " upper b "
					+ range.getUpperBound());
			iLogger.log(log);
			lowerBound = range.getLowerBound();
			synchronized (_dbCon)
			{
				_dbCon.openCollection(col);
				while (upperBound < range.getUpperBound())
				{
					if (range.getUpperBound() - lowerBound <= PACKAGE_SIZE)
					{
						upperBound = range.getUpperBound();
					}
					else
					{
						upperBound = lowerBound + PACKAGE_SIZE;
					}
					monitor.subTask("Updating " + totalAspects + " Aspects from Repository " + upperBound);

					Vector<String> objs = Utilities.getObjects(PDRType.ASPECT, _repositoryId, _projectId, lowerBound,
							upperBound);
					for (String s : objs)
					{
						// System.out.println(s);
						name = extractPdrId(s) + ".xml";
						_dbCon.storeQuick2DB(s, col, name);
						monitor.worked(1);

					}
					lowerBound = Math.min(lowerBound + 250, range.getUpperBound());
					if (monitor.isCanceled())
					{
						return Status.CANCEL_STATUS;
					}
				}
				_dbCon.closeDB(col);

			}

		}
		if (monitor.isCanceled())
		{
			monitor.subTask("Optimizing Database Index");
			col = "person";
			_dbCon.optimize(col);
			col = "aspect";
			_dbCon.optimize(col);
			try
			{
				proccessUpdateStates(personRanges, totalPersons);
			}
			catch (XQException e)
			{

				log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e);iLogger.log(log);
			}
			try
			{
				proccessUpdateStates(aspectRanges, totalAspects);
			}
			catch (XQException e)
			{

				log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e);iLogger.log(log);
			}
			monitor.done();
			return Status.CANCEL_STATUS;
		}
		col = "reference";
		lowerBound = 1;
		upperBound = 1;
		synchronized (_dbCon)
		{
			_dbCon.openCollection(col);
			for (IDRange range : referenceRanges)
			{
				log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "references range " + range.getLowerBound()
						+ " upper b " + range.getUpperBound());
				iLogger.log(log);
				lowerBound = range.getLowerBound();
				monitor.subTask("Updating " + totalReferences + " References from Repository " + lowerBound);

				while (upperBound < range.getUpperBound())
				{
					if (range.getUpperBound() - lowerBound <= PACKAGE_SIZE)
					{
						upperBound = range.getUpperBound();
					}
					else
					{
						upperBound = lowerBound + PACKAGE_SIZE;
					}
					Vector<String> objs = Utilities.getObjects(PDRType.REFERENCE, _repositoryId, _projectId,
							lowerBound, upperBound);
					for (String s : objs)
					{
						System.out.println(s);
						System.out.println();
						name = extractPdrId(s) + ".xml";
						_dbCon.storeQuick2DB(s, col, name);
						monitor.worked(1);
					}
					lowerBound = Math.min(lowerBound + 250, range.getUpperBound());
					if (monitor.isCanceled())
					{
						return Status.CANCEL_STATUS;
					}
				}
			}

			_dbCon.closeDB(col);
			if (monitor.isCanceled())
			{
				monitor.subTask("Optimizing Database Index");
				col = "person";
				_dbCon.optimize(col);
				col = "aspect";
				_dbCon.optimize(col);
				col = "reference";
				_dbCon.optimize(col);
				monitor.done();
				return Status.CANCEL_STATUS;
			}
			monitor.subTask("Optimizing Database Index");
			col = "person";
			_dbCon.optimize(col);
			col = "aspect";
			_dbCon.optimize(col);
			col = "reference";
			_dbCon.optimize(col);
			monitor.done();

			monitor.subTask("Processing Update State of Objects...");

			try
			{
				proccessUpdateStates(personRanges, totalPersons);
			}
			catch (XQException e)
			{

				log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e);iLogger.log(log);
			}
			try
			{
				proccessUpdateStates(aspectRanges, totalAspects);
			}
			catch (XQException e)
			{

				log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e);iLogger.log(log);
			}
			try
			{
				proccessUpdateStates(referenceRanges, totalReferences);
			}
			catch (XQException e)
			{

				log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e);iLogger.log(log);
			}
		}
		return Status.OK_STATUS;

	}

	/**
	 * Update modified objects.
	 * @param monitor the monitor
	 * @param date the date
	 * @return the i status
	 * @throws Exception
	 */
	private IStatus updateModifiedObjects(final IProgressMonitor monitor, final Date date) throws Exception
	{
		String col = "util";
		String name;
		monitor.subTask("Connecting to Repository...");
		Vector<String> modObjs = Repository.getModifiedObjects(_repositoryId, _projectId,
				AEConstants.ADMINDATE_FORMAT.format(date));
		// calculate total work

		monitor.beginTask("Updating from Repository. Number of Objects: " + modObjs.size(), modObjs.size());
		Vector<String> pIds = new Vector<String>(modObjs.size());
		Vector<String> rIds = new Vector<String>(modObjs.size());
		Vector<String> aIds = new Vector<String>(modObjs.size());
		Vector<String> uIds = new Vector<String>(modObjs.size());

		if (modObjs.size() == 0)
		{
			monitor.subTask("Your Database has already been updated. No Update necessary");
		}
		else
		{
			monitor.subTask("Inserting Modified Objects into Local DB...");

			for (String s : modObjs)
			{
				// System.out.println(s);
				name = extractPdrId(s);
				if (name.startsWith("pdrPo"))
				{
					col = "person";
					pIds.add(name);
				}
				if (name.startsWith("pdrAo"))
				{
					col = "aspect";
					aIds.add(name);
				}
				if (name.startsWith("pdrRo"))
				{
					col = "reference";
					rIds.add(name);
				}
				if (name.startsWith("pdrUo"))
				{
					col = "users";
					uIds.add(name);
				}
				// System.out.println("modified object " + s);
				name += ".xml";

				synchronized (_dbCon)
				{
					if (!s.startsWith("no path in db registry") && checkVersions(s, col, name))
					{
						_dbCon.store2DB(s, col, name, false);
					}
				}
				s = null;
			}
			monitor.worked(1);
		}

		monitor.subTask("Optimizing Database Index...");
		col = "person";
		_dbCon.optimize(col);
		col = "aspect";
		_dbCon.optimize(col);
		col = "reference";
		_dbCon.optimize(col);
		col = "users";
		_dbCon.optimize(col);
		monitor.subTask("Processing Update State of Objects...");

		for (String id : pIds)
		{
			try
			{
				_facade.getPersonsUpdateState().put(id, 1);
			}
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e);iLogger.log(log);
			}
		}
		_idService.insertIdUpdatedObjects(pIds, "pdrPo");
		for (String id : aIds)
		{
			try
			{
				_facade.getAspectsUpdateState().put(id, 1);
			}
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e);iLogger.log(log);
			}
		}
		_idService.insertIdUpdatedObjects(aIds, "pdrAo");
		for (String id : rIds)
		{
			try
			{
				_facade.getReferencesUpdateState().put(id, 1);
			}
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e);iLogger.log(log);
			}
		}
		_idService.insertIdUpdatedObjects(rIds, "pdrRo");

		return Status.OK_STATUS;
	}

	@Override
	public final IStatus updateUsers(final String userID, final String password, final IProgressMonitor monitor)
			throws Exception
	{
		String url = Platform.getPreferencesService().getString(CommonActivator.PLUGIN_ID, "REPOSITORY_URL",
				AEConstants.REPOSITORY_URL, null);
		_repositoryId = Platform.getPreferencesService().getInt(CommonActivator.PLUGIN_ID, "REPOSITORY_ID",
				AEConstants.REPOSITORY_ID, null);
		_projectId = Platform.getPreferencesService().getInt(CommonActivator.PLUGIN_ID, "PROJECT_ID",
				AEConstants.PROJECT_ID, null);
		String name;
		Configuration.getInstance().setAxis2BaseURL(url.toString());
		//FIXME nur als default
		Configuration.getInstance().setPDRUser("pdrUo." + String.format("%03d", _repositoryId) + ".002.000000001", "pdrrdp");

		
		injestNewUsers(userID, password);
		
		Vector<IDRange> ranges = Utilities.getOccupiedObjectIDRanges(PDRType.USER, _repositoryId, _projectId, 1,
				MAX_OBJECT_NUMBER);
		String col = "users";
		int lowerBound = 1;
		int upperBound = 1;
		synchronized (_dbCon)
		{
			_dbCon.openCollection(col);
			for (IDRange range : ranges)
			{
				log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "user range " + range.getLowerBound() + " upper b "
						+ range.getUpperBound());
				iLogger.log(log);
				lowerBound = range.getLowerBound();

				while (upperBound < range.getUpperBound())
				{
					if (range.getUpperBound() - lowerBound <= PACKAGE_SIZE)
					{
						upperBound = range.getUpperBound();
					}
					else
					{
						upperBound = lowerBound + PACKAGE_SIZE;
					}

					Vector<String> objs = Utilities.getObjects(PDRType.USER, _repositoryId, _projectId, lowerBound,
							upperBound);
					for (String s : objs)
					{
						name = extractPdrId(s) + ".xml";
						if (isValidUser(s))
						{
							_dbCon.storeQuick2DB(addUserPrefix(s), col, name);
						}
						else
						{
							String us = addUserPrefix(s);
							if (isValidUser(us))
							{
								_dbCon.storeQuick2DB(us, col, name);
							}
						}
					}
					lowerBound = Math.min(lowerBound + PACKAGE_SIZE, range.getUpperBound());
				}
			}
			_dbCon.optimize(col);
			_dbCon.openCollection(col);
			_dbCon.closeDB(col);
			_idService.clearUserUpdateStates();
			Configuration.getInstance().setPDRUser(userID, password);

		}
		return null;
	}

	private boolean isValidUser(String s)
	{
		if (s.startsWith("<user xmlns=\"http://pdr.bbaw.de/namespaces/uodl/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\""))
		{
			return true;
		}
		else if (s
				.startsWith("<uodl:user xmlns:uodl=\"http://pdr.bbaw.de/namespaces/uodl/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\""))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	private void resetObjectId(Identifier id, String newID, int level) throws Exception
	{
		XQConnection con;
		con = _dbCon.getConnection();
		XQPreparedExpression xqp;
		XQResultSequence xqs = null;
		String replace;
		log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "resetObjectId old: " + id.toString() + " new: " + newID);
		iLogger.log(log);
		boolean successful = false;
		try
		{
			renameObject(id, newID);
			successful = true;
		}
		catch (XQException e2)
		{
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		if (successful)
		{
			if (level >= 4)
			{
				replace = "declare namespace uodl=\"http://pdr.bbaw.de/namespaces/uodl/\";\n"
						+ "for $x in collection(\"users\")/uodl:user//@*[.='" + id.toString() + "']\n"
						+ "let $new := '" + newID + "'\n" + "return replace value of node $x with $new";
				try
				{
					con = _dbCon.getConnection();
					xqp = con.prepareExpression(replace);
					xqs = xqp.executeQuery();
					xqs.close();
					con.close();

				}
				catch (XQException e)
				{
					// TODO Auto-generated catch block
					log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e);iLogger.log(log);
				}
				// _dbCon.optimize("users");
			}

			if (level >= 3)
			{
				replace = "declare namespace mods=\"http://www.loc.gov/mods/v3\";\n"
						+ "for $x in collection(\"reference\")/mods:mods//@*[.='" + id.toString() + "']\n"
						+ "let $new := '" + newID + "'\n" + "return replace value of node $x with $new";
				System.out.println(replace);
				try
				{
					con = _dbCon.getConnection();
					xqp = null;
					xqp = con.prepareExpression(replace);
					xqs = xqp.executeQuery();
					xqs.close();
					con.close();
				}
				catch (XQException e)
				{
					// TODO Auto-generated catch block
					log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e);iLogger.log(log);
				}

				// replace = "for $x in collection(\"reference\")/mods//*[.='" +
				// id.toString() + "']\n" +
				// "let $new := '" + idMap.get(id).toString() + "'\n" +
				// "return replace value of node $x with $new";
				// System.out.println(replace);
				// xqp = con.prepareExpression(replace);
				// xqs = xqp.executeQuery();
				// _dbCon.optimize("reference");

			}

			if (level >= 2)
			{
				replace = "declare namespace podl=\"http://pdr.bbaw.de/namespaces/podl/\";\n"
						+ "for $x in collection(\"person\")/podl:person//@*[.='" + id.toString() + "']\n"
						+ "let $new := '" + newID + "'\n" + "return replace value of node $x with $new";
				System.out.println(replace);
				try
				{
					con = _dbCon.getConnection();
					xqp = null;
					xqp = con.prepareExpression(replace);
					xqs = xqp.executeQuery();
					xqs.close();
					con.close();
				}
				catch (XQException e)
				{
					// TODO Auto-generated catch block
					log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e);iLogger.log(log);
				}

				replace = "declare namespace podl=\"http://pdr.bbaw.de/namespaces/podl/\";\n"
						+ "for $x in collection(\"person\")/podl:person//podl:reference[.='" + id.toString() + "']\n"
						+ "let $new := '" + newID + "'\n" + "return replace value of node $x with $new";
				System.out.println(replace);
				try
				{
					con = _dbCon.getConnection();
					xqp = null;
					xqp = con.prepareExpression(replace);
					xqs = xqp.executeQuery();
					xqs.close();
					con.close();
				}
				catch (XQException e1)
				{
					// TODO Auto-generated catch block
					log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e1);iLogger.log(log);
				}
				// _dbCon.optimize("person");

			}

			if (level >= 1)
			{
				replace = "declare namespace aodl=\"http://pdr.bbaw.de/namespaces/aodl/\";\n"
						+ "for $x in collection(\"aspect\")/aodl:aspect//@*[.='" + id.toString() + "']\n"
						+ "let $new := '" + newID + "'\n" + "return replace value of node $x with $new";
				System.out.println(replace);
				try
				{
					con = _dbCon.getConnection();
					xqp = null;
					xqp = con.prepareExpression(replace);
					xqs = xqp.executeQuery();
					xqs.close();
					con.close();
				}
				catch (XQException e)
				{
					// TODO Auto-generated catch block
					log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e);iLogger.log(log);
				}

				replace = "declare namespace aodl=\"http://pdr.bbaw.de/namespaces/aodl/\";\n"
						+ "for $x in collection(\"aspect\")/aodl:aspect//aodl:reference[.='" + id.toString() + "']\n"
						+ "let $new := '" + newID + "'\n" + "return replace value of node $x with $new";
				System.out.println(replace);
				try
				{
					con = _dbCon.getConnection();
					xqp = null;
					xqp = con.prepareExpression(replace);
					xqs = xqp.executeQuery();
					xqs.close();
					con.close();
				}
				catch (XQException e)
				{
					// TODO Auto-generated catch block
					log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e);iLogger.log(log);
				}
				// _dbCon.optimize("aspect");

			}

		}

	}

	private void checkAndInjestStandardUsers(Vector<String> standardUsers, String userId, String password)
	{
		Collections.sort(standardUsers);
		Vector<String> repoUsers = new Vector<String>(10);
		Configuration.getInstance().setPDRUser("pdrUo." + String.format("%03d", _repositoryId) + ".002.000000001", "pdrrdp");
		try
		{
			repoUsers = Utilities.getObjects(PDRType.USER, _repositoryId, _projectId, 1, 9);
		}
		catch (PDRAlliesClientException e2)
		{
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		Collections.sort(repoUsers);
		boolean found = false;
		for (int i = 1; i < 10; i++)
		{
			String user = standardUsers.get(i - 1);
			if (user != null && new Integer(extractPdrId(user).substring(14)) <= 9)
			{
				found = false;
				for (String u : repoUsers)
				{
					if (extractPdrId(u).equals(extractPdrId(user)))
					{
						found = true;
					}
				}
				if (!found)
				{
					Vector<String> injestUsers = new Vector<String>(1);
					User newUser = null;
					if (new Integer(extractPdrId(user).substring(14)) == 1)
					{
						newUser = _userManager.createUser(new PdrId("pdrUo", _repositoryId, _projectId, 100000001),
								"pdrAdmin", "pdrrdp", new String[]
								{"pdrAdmin", "admin", "user"}, "pdrAdmin", "pdrAdmin", "PDR-Administrator");
					}
					else if (new Integer(extractPdrId(user).substring(14)) == 2)
					{
						newUser = _userManager.createUser(new PdrId("pdrUo", _repositoryId, _projectId, 100000002),
								"admin", "admin", new String[]
								{"admin", "user"}, "admin", "admin", "Project-Administrator");
					}
					else if (new Integer(extractPdrId(user).substring(14)) == 3)
					{
						newUser = _userManager.createUser(new PdrId("pdrUo", _repositoryId, _projectId, 100000003),
								"user", "user", new String[]
								{"user"}, "user", "user", "Project-User");
					}
					else if (new Integer(extractPdrId(user).substring(14)) == 4)
					{
						newUser = _userManager.createUser(new PdrId("pdrUo", _repositoryId, _projectId, 100000004),
								"guest", "guest", new String[]
								{"guest"}, "guest", "guest", "Project-Guest");
					}
					else if (new Integer(extractPdrId(user).substring(14)) == 5)
					{
						newUser = _userManager.createUser(new PdrId("pdrUo", _repositoryId, _projectId, 100000005),
								"computer", "computer", new String[]
								{"user"}, "computer", "computer", "computer");
					}
					else if (new Integer(extractPdrId(user).substring(14)) == 6)
					{
						newUser = _userManager.createUser(new PdrId("pdrUo", _repositoryId, _projectId, 100000006),
								"dummy", "dummy", new String[]
								{"dummy"}, "dummy", "dummy", "dummy");
					}
					else if (new Integer(extractPdrId(user).substring(14)) == 7)
					{
						newUser = _userManager.createUser(new PdrId("pdrUo", _repositoryId, _projectId, 100000007),
								"dummy", "dummy", new String[]
								{"dummy"}, "dummy", "dummy", "dummy");
					}
					else if (new Integer(extractPdrId(user).substring(14)) == 8)
					{
						newUser = _userManager.createUser(new PdrId("pdrUo", _repositoryId, _projectId, 100000008),
								"dummy", "dummy", new String[]
								{"dummy"}, "dummy", "dummy", "dummy");
					}
					else if (new Integer(extractPdrId(user).substring(14)) == 9)
					{
						newUser = _userManager.createUser(new PdrId("pdrUo", _repositoryId, _projectId, 100000009),
								"dummy", "dummy", new String[]
								{"dummy"}, "dummy", "dummy", "dummy");
					}
					try
					{
						user = new UserXMLProcessor().writeToXML(newUser);
					}
					catch (XMLStreamException e1)
					{
						// TODO Auto-generated catch block
						log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e1);iLogger.log(log);
					}
					injestUsers.add(removeUserPrefix(user));
					try
					{
						Repository.ingestObjects(_repositoryId, _projectId, injestUsers);
					}
					catch (InvalidIdentifierException e)
					{
						// TODO Auto-generated catch block
						log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e);iLogger.log(log);
					}
					catch (PDRAlliesClientException e)
					{
						// TODO Auto-generated catch block
						log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exception ", e);iLogger.log(log);
					}
				}
			}
		}
		Configuration.getInstance().setPDRUser(userId, password);
	}

	@Override
	public String getUserId(String userName, int projectID) throws PDRAlliesClientException
	{
		String url = Platform.getPreferencesService().getString(CommonActivator.PLUGIN_ID, "REPOSITORY_URL",
				AEConstants.REPOSITORY_URL, null);
		Configuration.getInstance().setAxis2BaseURL(url.toString());
		return Repository.getUserID(userName, projectID);
	}

}
