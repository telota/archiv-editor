/*
 * @author: Christoph Plutte
 */
package org.bbaw.pdr.ae.db.basex711;

import java.io.OutputStream;

import javax.xml.xquery.XQConnection;
import javax.xml.xquery.XQDataSource;
import javax.xml.xquery.XQException;

import org.basex.core.BaseXException;
import org.basex.core.Context;
import org.basex.core.cmd.Add;
import org.basex.core.cmd.Close;
import org.basex.core.cmd.CreateDB;
import org.basex.core.cmd.Delete;
import org.basex.core.cmd.Export;
import org.basex.core.cmd.InfoDB;
import org.basex.core.cmd.Open;
import org.basex.core.cmd.Optimize;
import org.basex.core.cmd.Set;

/**
 * The Class DBConnector.
 * @author Christoph Plutte
 */
public final class DBConnector
{
	private boolean aspectOptimizationRequired;

	private boolean personOptimizationRequired;

	private boolean referenceOptimizationRequired;

	/** The sigleton instance. */
	private static volatile DBConnector _sigletonInstance;

	/** The CONTEXT. */
	private static Context CONTEXT = new Context();

	/** The Constant OUT. */
	static final OutputStream OUT = System.out;


	/** Database driver. */
	private static final String DRIVER = "org.basex.api.xqj.BXQDataSource";
	/**
	 * Closes the specified connection.
	 * @param xqc connection to be closed
	 * @throws XQException connection exception
	 */
	static void close(final XQConnection xqc) throws XQException
	{
		xqc.close();
	}

	/**
	 * Creates the empty.
	 * @param col the col
	 * @throws BaseXException the base x exception
	 */
	public static void createEmpty(final String col) throws BaseXException
	{
		boolean chop = !((col.equals("refTemplate")) || (col.equals("aspect")));

		new Set("intparse", chop).execute(CONTEXT);
		new Set("dtd", true).execute(CONTEXT);
		// new Set("entity", true).execute(CONTEXT);

		new Set("chop", chop).execute(CONTEXT);
		new Set("TEXTINDEX", true).execute(CONTEXT);
		new Set("ATTRINDEX", true).execute(CONTEXT);
		new Set("PATHINDEX", true).execute(CONTEXT);
		new Set("FTINDEX", true).execute(CONTEXT);
		new Set("WILDCARDS", true).execute(CONTEXT);
		new Set("STEMMING", true).execute(CONTEXT);
		new Set("CASESENS", false).execute(CONTEXT);
		new Set("DIACRITICS", true).execute(CONTEXT);
		new CreateDB(col).execute(CONTEXT);
		new Close().execute(CONTEXT);

	}

	/**
	 * Db exists.
	 * @param col the col
	 * @return true, if successful
	 */
	public static boolean dbExists(final String col)
	{
		try
		{
			new Open(col).execute(CONTEXT);
			new Close().execute(CONTEXT);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Gets the single instance of DBConnector.
	 * @return single instance of DBConnector
	 */
	public static DBConnector getInstance()
	{
		if (_sigletonInstance == null)
		{
			synchronized (DBConnector.class)
			{
				if (_sigletonInstance == null)
				{
					_sigletonInstance = new DBConnector();
				}
			}
			verifyOrCreateDBS();
		}
		return _sigletonInstance;
	}



	/**
	 * Verify or create dbs.
	 */
	private static void verifyOrCreateDBS()
	{
		if (!dbExists("person"))
		{
			try
			{
				createEmpty("person");
			}
			catch (BaseXException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (!dbExists("aspect"))
		{
			try
			{
				createEmpty("aspect");
			}
			catch (BaseXException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (!dbExists("reference"))
		{
			try
			{
				createEmpty("reference");
			}
			catch (BaseXException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (!dbExists("users"))
		{
			try
			{
				createEmpty("users");
			}
			catch (BaseXException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (!dbExists("management"))
		{
			try
			{
				createEmpty("management");
			}
			catch (BaseXException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (!dbExists("refTemplate"))
		{
			try
			{
				createEmpty("refTemplate");
			}
			catch (BaseXException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (!dbExists("config"))
		{
			try
			{
				createEmpty("config");
			}
			catch (BaseXException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	/**
	 * Instantiates a new dB connector.
	 */
	private DBConnector()
	{
	}

	/**
	 * Close db.
	 * @param col the col
	 */
	public void closeDB(final String col)
	{
		try
		{
			new Open(col).execute(CONTEXT);
			new Close().execute(CONTEXT);
		}
		catch (BaseXException e2)
		{
			e2.printStackTrace();
		}
	}

	/**
	 * Creates the db from dir.
	 * @param col the col
	 * @param dir the dir
	 */
	public void createDBFromDir(final String col, final String dir)
	{
		boolean chop = !((col.equals("refTemplate")) || (col.equals("aspect")));
		try
		{
			new Set("intparse", chop).execute(CONTEXT);
			new Set("dtd", true).execute(CONTEXT);
			// new Set("entity", true).execute(CONTEXT);

			new Set("chop", chop).execute(CONTEXT);
			new Set("TEXTINDEX", true).execute(CONTEXT);
			new Set("ATTRINDEX", true).execute(CONTEXT);
			new Set("PATHINDEX", true).execute(CONTEXT);
			new Set("FTINDEX", true).execute(CONTEXT);
			new Set("WILDCARDS", true).execute(CONTEXT);
			new Set("STEMMING", true).execute(CONTEXT);
			new Set("CASESENS", false).execute(CONTEXT);
			new Set("DIACRITICS", true).execute(CONTEXT);

		}
		catch (BaseXException e2)
		{
			e2.printStackTrace();
		}

		try
		{
			new CreateDB(col, dir).execute(CONTEXT);
		}
		catch (BaseXException e2)
		{
			e2.printStackTrace();
		}
		try
		{
			new Close().execute(CONTEXT);
		}
		catch (BaseXException e2)
		{
			e2.printStackTrace();
		}
	}

	/**
	 * Db is empty.
	 * @param col the col
	 * @return true, if successful
	 */
	public boolean dbIsEmpty(final String col)
	{
		// XXX plutte muss angepasst werden
		try
		{
			new Open(col).execute(CONTEXT);
			InfoDB info = new InfoDB();
			info.execute(CONTEXT);
			System.out.println(info);
			// System.out.println("number of nodes " + info.getNumberOfNodes());
			// if (info.getNumberOfDocs() <= 2 && info.getNumberOfNodes() <= 2)
			// {
			// // System.out.println("db empty " + col + " true");
			// return true;
			// }
			// else
			// {
			// return false;
			// }
		}
		catch (BaseXException e2)
		{

			e2.printStackTrace();
			return false;
		}
		return false;

	}

	/**
	 * Delete.
	 * @param name the name
	 * @param col the col
	 */
	public void delete(String name, final String col)
	{
		try
		{
			new Open(col).execute(CONTEXT);
		}
		catch (BaseXException e2)
		{
			e2.printStackTrace();
		}
		if (!name.endsWith("xml"))
		{
			name += ".xml";
		}
		try
		{
			new Delete(name).execute(CONTEXT);
		}
		catch (BaseXException e1)
		{
			e1.printStackTrace();
		}
		try
		{
			new Close().execute(CONTEXT);
		}
		catch (BaseXException e2)
		{
			e2.printStackTrace();
		}
	}

	/**
	 * Gets the connection.
	 * @return the connection
	 * @throws XQException the xQ exception
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public XQConnection getConnection() throws Exception
	{

		// Create a connection
		XQConnection con = ((XQDataSource) Class.forName(DRIVER).newInstance()).getConnection();
		return con;
	}

	/**
	 * Gets the dB info.
	 * @param col the col
	 */
	public void getDBInfo(final String col)
	{
		// try
		// {
//			new Open(col).execute(CONTEXT);
//			String info = new InfoDB().execute(CONTEXT);
		// // System.out.println(info);
		// }
		// catch (BaseXException e2)
		// {
		// e2.printStackTrace();
		// }
	}

	/**
	 * Gets the dB number of docs.
	 * @param col the col
	 * @return the dB number of docs
	 */
	public int getDBNumberOfDocs(final String col)
	{
		// XXX plutte muss angepasst werden
		try
		{
			new Open(col).execute(CONTEXT);
			InfoDB info = new InfoDB();
			info.execute(CONTEXT);
			// return info.getNumberOfNodes();
		}
		catch (BaseXException e2)
		{
			e2.printStackTrace();
		}
		return 0;
	}

	/**
	 * Open collection.
	 * @param col the col
	 */
	public void openCollection(final String col)
	{
		try
		{
			new Open(col).execute(CONTEXT);
		}
		catch (BaseXException e2)
		{
			e2.printStackTrace();
		}
	}

	/**
	 * Optimize.
	 * @param col the col
	 */
	public void optimize(final String col)
	{
		try
		{
			new Open(col).execute(CONTEXT);
			new Optimize().execute(CONTEXT);
			new Close().execute(CONTEXT);
		}
		catch (BaseXException e2)
		{
			e2.printStackTrace();
		}

	}

	/**
	 * Store2 db.
	 * @param xml the xml
	 * @param col the col
	 * @param name the name
	 * @param optimize the optimize
	 */
	public void store2DB(final String xml, final String col, String name, final boolean optimize)
	{
		if (!name.endsWith(".xml"))
		{
			name += ".xml";
		}
		try
		{
			new Open(col).execute(CONTEXT);
		}
		catch (BaseXException e2)
		{
			e2.printStackTrace();
		}

		try
		{
			new Delete(name).execute(CONTEXT);
		}
		catch (BaseXException e1)
		{
			e1.printStackTrace();
		}

		if (col.equals("aspect") && !aspectOptimizationRequired)
		{
			aspectOptimizationRequired = true;
		}
		else if (col.equals("person") && !personOptimizationRequired)
		{
			personOptimizationRequired = true;
		}
		else if (col.equals("reference") && !referenceOptimizationRequired)
		{
			referenceOptimizationRequired = true;
		}
		try
		{
			boolean chop = !((col.equals("refTemplate")) || (col.equals("aspect")));
			new Set("intparse", chop).execute(CONTEXT);
			new Set("dtd", true).execute(CONTEXT);
			// new Set("entity", true).execute(CONTEXT);

			new Set("chop", chop).execute(CONTEXT);
			new Set("TEXTINDEX", true).execute(CONTEXT);
			new Set("ATTRINDEX", true).execute(CONTEXT);
			new Set("PATHINDEX", true).execute(CONTEXT);
			new Set("FTINDEX", true).execute(CONTEXT);
			new Set("WILDCARDS", true).execute(CONTEXT);
			new Set("STEMMING", true).execute(CONTEXT);
			new Set("CASESENS", false).execute(CONTEXT);
			new Set("DIACRITICS", true).execute(CONTEXT);


			new Add(name, xml).execute(CONTEXT);
			if (optimize)
			{
				new Optimize().execute(CONTEXT);
			}
			new Close().execute(CONTEXT);
		}
		catch (BaseXException e)
		{
			e.printStackTrace();
		}

	}

	public boolean isAspectOptimizationRequired()
	{
		return aspectOptimizationRequired;
	}

	public boolean isPersonOptimizationRequired()
	{
		return personOptimizationRequired;
	}

	public boolean isReferenceOptimizationRequired()
	{
		return referenceOptimizationRequired;
	}

	/**
	 * Store quick2 db.
	 * @param xml the xml
	 * @param col the col
	 * @param name the name
	 */
	public void storeQuick2DB(final String xml, final String col, String name)
	{
		if (!name.endsWith(".xml"))
		{
			name += ".xml";
		}
		if (col.equals("aspect") && !aspectOptimizationRequired)
		{
			aspectOptimizationRequired = true;
		}
		else if (col.equals("person") && !personOptimizationRequired)
		{
			personOptimizationRequired = true;
		}
		else if (col.equals("reference") && !referenceOptimizationRequired)
		{
			referenceOptimizationRequired = true;
		}
		// try
		// {
		// new Delete(name).execute(CONTEXT);
		// }
		// catch (BaseXException e1)
		// {
		// e1.printStackTrace();
		// }
		try
		{
			new Add(name, xml).execute(CONTEXT);
		}
		catch (BaseXException e2)
		{
			e2.printStackTrace();
		}
	}

	/**
	 * Write col to xml.
	 * @param col the col
	 * @param pDir the dir
	 * @throws BaseXException the base x exception
	 */
	public void writeColToXML(final String col, final String pDir) throws BaseXException
	{
		// System.out.println("write local backup col " + col);
//		new Set("indent", false).execute(CONTEXT);
		new Open(col).execute(CONTEXT);
		new Export(pDir).execute(CONTEXT);
		new Close().execute(CONTEXT);

	}

}
