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
package org.bbaw.pdr.ae.control.datahandling.xqj.config;

import java.util.HashMap;
import java.util.Vector;

import javax.xml.xquery.XQConnection;
import javax.xml.xquery.XQPreparedExpression;
import javax.xml.xquery.XQResultSequence;

import org.bbaw.pdr.ae.common.AEConstants;
import org.bbaw.pdr.ae.config.core.ConfigXMLProcessor;
import org.bbaw.pdr.ae.config.core.DataDescSaxHandler;
import org.bbaw.pdr.ae.config.core.IConfigManager;
import org.bbaw.pdr.ae.config.model.DatatypeDesc;
import org.bbaw.pdr.ae.control.datahandling.xqj.internal.Activator;
import org.bbaw.pdr.ae.control.datahandling.xqj.internal.PdrIdService;
import org.bbaw.pdr.ae.db.basex711.DBConnector;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

/**
 * The Class ConfigManager.
 * @author Christoph Plutte
 */
public class ConfigManager implements IConfigManager
{

	/** Logger. */
	private static ILog iLogger = AEConstants.ILOGGER;
	/** status. */
	private IStatus _log;

	/** The datatype desc. */
	private DatatypeDesc _datatypeDesc;

	/** The db con. */
	private DBConnector _dbCon = DBConnector.getInstance();

	/** The _xml proc. */
	private ConfigXMLProcessor _xmlProc = new ConfigXMLProcessor();

	/** The _id service. */
	private PdrIdService _idService = new PdrIdService();

	/** The configs. */
	private HashMap<String, DatatypeDesc> _configs;

	/**
	 * Instantiates a new config manager.
	 */
	public ConfigManager()
	{

	}

	@Override
	public final void deleteConfig(final String provider)
	{
		synchronized (_dbCon)
		{
			_dbCon.delete(provider + "Config.xml", "config");
		}
	}

	@Override
	public final HashMap<String, DatatypeDesc> getConfigs() throws Exception
	{

		// get Connection
		synchronized (_dbCon)
		{
			XQConnection con = _dbCon.getConnection();
			String query = "declare namespace dtdl=\"http://pdr.bbaw.de/namespaces/dtdl/\";\n" + "<result>{\n"
					+ "for $x in collection(\"config\")//dtdl:datatypeDesc\n" + "return <p>{$x}</p>" + "}</result>";
			XQPreparedExpression xqp;

			_log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "ConfigManager query: " + query);
			iLogger.log(_log);

			xqp = con.prepareExpression(query);

			XQResultSequence xqs = xqp.executeQuery();
			// execute the XQuery Expression

			// TODO Feldgr��e �bergeben.

			try
			{
				xqs.writeSequenceToSAX(new DataDescSaxHandler(this));
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		// while (datatypeDesc == null)
		// {
		// System.out.print(".");
		// }

		HashMap<String, DatatypeDesc> tmp = _configs;
		_configs = null;
		return tmp;
	}

	@Override
	public final Vector<String> getConfigs(final Vector<String> providers) throws Exception
	{
		DatatypeDesc dtd = null;
		String dtdString = null;
		Vector<String> newConfigs = new Vector<String>(providers.size());
		for (String s : providers)
		{
			dtd = getDatatypeDesc(s);
			if (dtd != null)
			{
				dtdString = _xmlProc.writeToXML(dtd);
			}
			if (dtdString != null && dtdString.trim().length() > 0)
			{
				newConfigs.add(dtdString);
			}
		}
		return newConfigs;
	}

	@Override
	public final DatatypeDesc getDatatypeDesc()
	{
		return _datatypeDesc;
	}

	@Override
	public final DatatypeDesc getDatatypeDesc(final String provider) throws Exception
	{

		// get Connection

		String query = "declare namespace dtdl=\"http://pdr.bbaw.de/namespaces/dtdl/\";\n" + "<resultProvider>{\n"
				+ "for $x in collection(\"config\")//dtdl:datatypeDesc[.//@provider='" + provider + "']\n"
				+ "return <p>{$x}</p>" + "}</resultProvider>";
		XQPreparedExpression xqp;

		_log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "ConfigManager query: " + query);
		iLogger.log(_log);
		synchronized (_dbCon)
		{
			XQConnection con = _dbCon.getConnection();
			xqp = con.prepareExpression(query);

			XQResultSequence xqs = xqp.executeQuery();
			// execute the XQuery Expression

			// TODO Feldgr��e �bergeben.

			try
			{
				xqs.writeSequenceToSAX(new DataDescSaxHandler(this));
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			con.close();
		}

		// while (datatypeDesc == null)
		// {
		// System.out.print(".");
		// }
		DatatypeDesc tmp = _datatypeDesc;
		_datatypeDesc = null;
		return tmp;
	}

	@Override
	public final void saveConfig(final DatatypeDesc d) throws Exception
	{
		_log = new Status(IStatus.INFO, Activator.PLUGIN_ID, "ConfigManager provider " + d.getProvider());
		iLogger.log(_log);
		// XXX anpassen
		// facade.getConfigs().put(d.getProvider(), d);
		if (getDatatypeDesc(d.getProvider()) != null)
		{
			_idService.insertModifiedConfig(d.getProvider());
		}
		else
		{
			_idService.insertNewConfig(d.getProvider());
		}
		String xml = new ConfigXMLProcessor().writeToXML(d);
		synchronized (_dbCon)
		{
			_dbCon.store2DB(xml, "config", d.getProvider() + "Config.xml", true);
		}
		// log = new Status(IStatus.WARNING, Activator.PLUGIN_ID,
		// "ConfigManager saveConfig xml " + xml);
		// iLogger.log(log);
	}

	@Override
	public final void setConfigs(final HashMap<String, DatatypeDesc> configs)
	{
		this._configs = configs;

	}

	@Override
	public final void setDatatypeDesc(final DatatypeDesc datatypeDesc)
	{
		this._datatypeDesc = datatypeDesc;
	}

}
