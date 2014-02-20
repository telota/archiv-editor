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

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import javax.xml.stream.XMLStreamException;
import javax.xml.xquery.XQConnection;
import javax.xml.xquery.XQException;
import javax.xml.xquery.XQPreparedExpression;
import javax.xml.xquery.XQResultSequence;

import org.basex.core.BaseXException;
import org.basex.core.Context;
import org.bbaw.pdr.ae.common.AEConstants;
import org.bbaw.pdr.ae.common.CommonActivator;
import org.bbaw.pdr.ae.control.core.XMLProcessor;
import org.bbaw.pdr.ae.control.facade.Facade;
import org.bbaw.pdr.ae.control.interfaces.IDBManager;
import org.bbaw.pdr.ae.db.basex711.DBConnector;
import org.bbaw.pdr.ae.metamodel.PdrId;
import org.bbaw.pdr.ae.model.Aspect;
import org.bbaw.pdr.ae.model.PdrObject;
import org.bbaw.pdr.ae.model.Person;
import org.bbaw.pdr.ae.model.ReferenceMods;
import org.bbaw.pdr.ae.model.ReferenceModsTemplate;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

/**
 * The Class DBManager.
 * @author Christoph Plutte
 */
public class DBManager implements IDBManager
{

	/** The Constant CONTEXT. */
	static final Context CONTEXT = new Context();

	/** The db connector. */
	private DBConnector _dbConnector = DBConnector.getInstance();
	/** Logger. */
	private static ILog iLogger = AEConstants.ILOGGER;
	/** status. */
	private IStatus _log;

	@Override
	public final void createDBFromDir(final String col, final String subDir)
	{
		synchronized (_dbConnector)
		{
			_dbConnector.createDBFromDir(col, subDir);
		}
	}

	@Override
	public final void createEmptyDB(final String col) throws BaseXException
	{
		synchronized (_dbConnector)
		{
			DBConnector.createEmpty(col);
		}

	}

	@Override
	public final boolean dbIsEmpty()
	{
		if (!testCollectionIsEmpty("aspect"))
		{
			return false;
		}
		if (!testCollectionIsEmpty("person"))
		{
			return false;
		}
		if (!testCollectionIsEmpty("reference"))
		{
			return false;
		}
		return true;

	}

	private boolean testCollectionIsEmpty(String col)
	{
		String query = "";
		if (col.equals("person"))
		{
			query = "declare namespace podl=\"http://pdr.bbaw.de/namespaces/podl/\";\n"
					+ "for $x in collection(\"person\")//podl:person\n" + "return string($x/@id)";
		}
		else if (col.equals("aspect"))
		{
			query = "declare namespace aodl=\"http://pdr.bbaw.de/namespaces/aodl/\";\n"
					+ "for $x in collection(\"aspect\")//aodl:aspect\n" + "return string($x/@id)";
		}
		else if (col.equals("reference"))
		{
			query = "declare namespace mods=\"http://www.loc.gov/mods/v3\";\n"
					+ "for $x in collection(\"reference\")//mods:mods\n" + "return string($x/@ID)";
		}
		_log = new Status(IStatus.INFO, CommonActivator.PLUGIN_ID, "testCollectionEmpty: " + query);
		iLogger.log(_log);
		XQResultSequence xqs;
		XQPreparedExpression xqp;
		XQConnection con;
		try
		{
			con = _dbConnector.getConnection();
			synchronized (_dbConnector)
			{
				xqp = con.prepareExpression(query);

				xqs = xqp.executeQuery();
				while (xqs.next())
				{
					return false;
				}
				return true;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}

	}

	@Override
	public final void delete(final PdrId pdrId, final String col) throws Exception
	{
		synchronized (_dbConnector)
		{
			_dbConnector.delete(pdrId.toString() + ".xml", col);
		}

		PdrIdService idService = new PdrIdService();
		idService.insertIdDeletedObject(pdrId);
	}

	@Override
	public final void delete(final String fileName, final String col) throws XQException
	{
		synchronized (_dbConnector)
		{
			_dbConnector.delete(fileName, col);
		}

	}

	@Override
	public final void loadLocalBackup(String dir, IProgressMonitor monitor)
	{
		dir = dir + AEConstants.FS;
		// person
		String col = "person";
		String subDir = dir + AEConstants.FS + col;
		monitor.subTask("Loading " + col);
		monitor.worked(25);

		subDir = subDir + AEConstants.FS;
		synchronized (_dbConnector)
		{
			_dbConnector.createDBFromDir(col, subDir);
		}

		_log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "DBManager load local backup col: " + col);
		iLogger.log(_log);
		// //aspects
		col = "aspect";
		subDir = dir + col;
		monitor.subTask("Loading " + col);
		monitor.worked(25);
		subDir = subDir + AEConstants.FS;
		synchronized (_dbConnector)
		{
			_dbConnector.createDBFromDir(col, subDir);
		}
		_log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "DBManager load local backup col: " + col);
		iLogger.log(_log);
		//
		// //reference
		col = "reference";
		subDir = dir + col;
		monitor.subTask("Loading " + col);
		monitor.worked(25);
		subDir = subDir + AEConstants.FS;
		synchronized (_dbConnector)
		{
			_dbConnector.createDBFromDir(col, subDir);
		}
		_log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "DBManager load local backup col: " + col);
		iLogger.log(_log);
		//
		// //reference
		col = "refTemplate";
		subDir = dir + col;
		monitor.subTask("Loading " + col);
		monitor.worked(25);
		subDir = subDir + AEConstants.FS;
		synchronized (_dbConnector)
		{
			_dbConnector.createDBFromDir(col, subDir);
		}
		_log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "DBManager load local backup col: " + col);
		iLogger.log(_log);
		
		checkAndUpgradeLineBreaksInAspectCollection();
	}

	private void checkAndUpgradeLineBreaksInAspectCollection() {
		if(isLineBreaksInAspectCollection())
		{
			removeLineBreaksInAspectCollection();
		}
		
	}

	private void removeLineBreaksInAspectCollection() {
		synchronized (_dbConnector)
		{
			XQConnection con;
			
			try {
				con = _dbConnector.getConnection();
			
				XQPreparedExpression xqp;
	
				String query = "declare namespace aodl=\"http://pdr.bbaw.de/namespaces/aodl/\";\n" +
				"for $x in collection(\"aspect\")//aodl:aspect/aodl:notification/text()\n"+
						"return replace value of node $x with fn:replace($x, '\\n\\s*', ' ')";
				
				xqp = con.prepareExpression(query);
				XQResultSequence xqs = xqp.executeQuery();
				xqs.close();
				con.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	private boolean isLineBreaksInAspectCollection() {
		boolean isLB = true;
		synchronized (_dbConnector)
		{
			XQConnection con;
			
			try {
				con = _dbConnector.getConnection();
			
				XQPreparedExpression xqp;
	
				String query = "declare namespace aodl=\"http://pdr.bbaw.de/namespaces/aodl/\";\n" +
				"let $r := fn:exists(collection(\"aspect\")//aodl:aspect/aodl:notification/text()[(matches(., '\\n\\s*'))])\n"+
						"return $r";
				
				xqp = con.prepareExpression(query);
				XQResultSequence xqs = xqp.executeQuery();
				 String r = xqs.getSequenceAsString(null);
				 isLB = r.equals("true");
				xqs.close();
				con.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return isLB;
	}

	@Override
	public final void loadLocalConfigBackup(String dir)
	{
		dir = dir + AEConstants.FS;
		// person
		String col = "config";
		String subDir = dir + col;

		subDir = subDir + AEConstants.FS;
		synchronized (_dbConnector)
		{
			_dbConnector.createDBFromDir(col, subDir);
		}

		_log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "DBManager load local config backup col: " + col);
		iLogger.log(_log);
	}

	@Override
	public final void saveReferenceTemplateToDB(final HashMap<String, ReferenceModsTemplate> templates)
			throws XMLStreamException
	{
		for (ReferenceModsTemplate template : templates.values())
		{
			String xml = new XMLProcessor().writeToXML(template);
			synchronized (_dbConnector)
			{
				_dbConnector.store2DB(xml, "refTemplate", template.getValue() + ".xml", true);
			}

		}

	}

	private final void saveToDB(final Aspect a, boolean noteChange) throws Exception
	{
		String xml = new XMLProcessor().writeToXML(a);
		// System.out.println(xml);
		synchronized (_dbConnector)
		{
			_dbConnector.store2DB(xml, "aspect", a.getPdrId().toString() + ".xml", false);
		}

		PdrIdService idService = new PdrIdService();
		if (a.getPdrId().getId() >= 100000000)
		{
			idService.insertIdNewObject(a.getPdrId());
		}
		else if (noteChange)
		{
			idService.insertIdModifiedObject(a.getPdrId());
		}
	}

	@Override
	public final void saveToDB(final PdrObject object) throws Exception
	{
		if (object instanceof Person)
		{
			saveToDB((Person) object, true);
		}
		if (object instanceof Aspect)
		{
			saveToDB((Aspect) object, true);
		}
		if (object instanceof ReferenceMods)
		{
			saveToDB((ReferenceMods) object, true);
		}
	}

	private final void saveToDB(final Person p, boolean noteChange) throws Exception
	{
		String xml = new XMLProcessor().writeToXML(p);
		synchronized (_dbConnector)
		{
			_dbConnector.store2DB(xml, "person", p.getPdrId().toString() + ".xml", false);
		}
		PdrIdService idService = new PdrIdService();
		if (p.getPdrId().getId() >= 100000000)
		{
			idService.insertIdNewObject(p.getPdrId());
		}
		else if (noteChange)
		{
			idService.insertIdModifiedObject(p.getPdrId());
		}
	}

	private final void saveToDB(final ReferenceMods r, boolean noteChange) throws Exception
	{
		String xml = new XMLProcessor().writeToXML(r);
		synchronized (_dbConnector)
		{
			_dbConnector.store2DB(xml, "reference", r.getPdrId().toString() + ".xml", false);
		}

		PdrIdService idService = new PdrIdService();
		if (r.getPdrId().getId() >= 100000000)
		{
			idService.insertIdNewObject(r.getPdrId());
		}
		else if (noteChange)
		{
			idService.insertIdModifiedObject(r.getPdrId());
		}
	}

	@Override
	public final void writeToLocalBackup(String dir) throws Exception
	{
		SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
		dir = dir + AEConstants.FS + "AEBackup-" + dateFormate.format(Facade.getInstanz().getCurrentDate());

		File f;
		f = new File(dir);
		f.mkdir();
		dir += AEConstants.FS;
		// person
		String subDir = dir + "person";
		String col = "person";
		// if (!new File(subDir).exists())
		// {
		// f = new File(subDir);
		// f.mkdir();
		// }
		f = new File(subDir);
		f.mkdir();
		subDir = subDir + AEConstants.FS;
		synchronized (_dbConnector)
		{
			_dbConnector.writeColToXML(col, subDir);
		}
		_log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "DBManager write local backup col: " + col);
		iLogger.log(_log);

		// reference
		col = "reference";
		subDir = dir + col;
		if (!new File(subDir).exists())
		{
			f = new File(subDir);
			f.mkdir();
		}
		subDir = subDir + AEConstants.FS;
		synchronized (_dbConnector)
		{
			_dbConnector.writeColToXML(col, subDir);
		}
		_log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "DBManager write local backup col: " + col);
		iLogger.log(_log);
		// reference
		col = "refTemplate";
		subDir = dir + col;
		if (!new File(subDir).exists())
		{
			f = new File(subDir);
			f.mkdir();
		}
		subDir = subDir + AEConstants.FS;
		synchronized (_dbConnector)
		{
			_dbConnector.writeColToXML(col, subDir);
		}
		_log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "DBManager write local backup col: " + col);
		iLogger.log(_log);

		// aspects
		subDir = dir + "aspect";
		col = "aspect";
		if (!new File(subDir).exists())
		{
			f = new File(subDir);
			f.mkdir();
		}
		subDir = subDir + AEConstants.FS;
		synchronized (_dbConnector)
		{
			_dbConnector.writeColToXML(col, subDir);
		}
		_log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "DBManager write local backup col: " + col);
		iLogger.log(_log);

	}

	@Override
	public final void writeToLocalConfigBackup(String dir) throws BaseXException
	{
		// config
		SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
		dir = dir + AEConstants.FS + "AEConfig-" + dateFormate.format(Facade.getInstanz().getCurrentDate());
		File f;
		f = new File(dir);
		f.mkdir();
		dir += AEConstants.FS;

		String subDir = dir + AEConstants.FS + "config";
		String col = "config";
		if (!new File(subDir).exists())
		{
			f = new File(subDir);
			f.mkdir();
		}
		subDir = subDir + AEConstants.FS;
		synchronized (_dbConnector)
		{
			_dbConnector.writeColToXML(col, subDir);
		}

		_log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "DBManager write local config backup col: " + col);
		iLogger.log(_log);

	}

	@Override
	public final void writeToLocalReferenceTemplateBackup(String dir) throws BaseXException
	{
		SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
		dir = dir + AEConstants.FS + "AEReferenceTemplate-" + dateFormate.format(Facade.getInstanz().getCurrentDate());
		File f;
		f = new File(dir);
		f.mkdir();

		String col = "refTemplate";

		dir = dir + AEConstants.FS;
		synchronized (_dbConnector)
		{
			_dbConnector.writeColToXML(col, dir);
		}

		_log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "DBManager write local refTemplate backup col: " + col);
		iLogger.log(_log);

	}

	@Override
	public boolean isOptimizationRequired()
	{
		return _dbConnector.isAspectOptimizationRequired() || _dbConnector.isPersonOptimizationRequired()
				|| _dbConnector.isReferenceOptimizationRequired();
	}

	@Override
	public void optimizeAll(IProgressMonitor monitor)
	{
		if (monitor != null)
		{
			monitor.beginTask("Optimize Database Indexes", 6);
		}
		if (_dbConnector.isAspectOptimizationRequired())
		{
			_dbConnector.optimize("aspect");
		}
		if (monitor != null)
		{
			monitor.worked(1);
		}
		if (_dbConnector.isPersonOptimizationRequired())
		{
			_dbConnector.optimize("person");
		}
		if (monitor != null)
		{
			monitor.worked(1);
		}
		if (_dbConnector.isReferenceOptimizationRequired())
		{
			_dbConnector.optimize("reference");
		}
		if (monitor != null)
		{
			monitor.worked(1);
		}
		_dbConnector.optimize("users");
		if (monitor != null)
		{
			monitor.worked(1);
		}
		_dbConnector.optimize("config");
		if (monitor != null)
		{
			monitor.worked(1);
		}
		_dbConnector.optimize("management");
		if (monitor != null)
		{
			monitor.worked(1);
		}

	}

	@Override
	public void saveToDB(PdrObject object, boolean noteChange) throws Exception {
		if (object instanceof Person)
		{
			saveToDB((Person) object, noteChange);
		}
		if (object instanceof Aspect)
		{
			saveToDB((Aspect) object, noteChange);
		}
		if (object instanceof ReferenceMods)
		{
			saveToDB((ReferenceMods) object, noteChange);
		}
		
	}

}
