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
package org.bbaw.pdr.ae.export.xslt.util;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.bbaw.pdr.ae.export.xml.utils.XMLContainer;
import org.w3c.dom.Document;

/**
 * Allows to process given XML source with XSLT stylesheet. If no instance of an
 * implementation of {@link Result} is given, the result of the last
 * transformation is held in a {@link DOMResult} and returned as an 
 * {@link XMLContainer}.
 * <p>All constructors expect the XML input to be passed as a {@link Source} 
 * implementation. For instance, a {@link StreamSource} representation of an XML 
 * file can easily be created by calling
 * <blockquote><code>new StreamSource(filename);</code></blockquote>
 * If the XML content desired to be transformed is embedded in an 
 * {@link XMLContainer} object, a <code>Source</code> conversion can be fetched
 * at {@link XMLContainer#getStream()}.</p>
 * <p>Hence the input of a <code>XSLTProcessor</code> can be piped in from
 * the same type that its ouput can be send to: {@link XMLContainer}. That way,
 * multiple transformations can be conveniently applied in a row.</p>
 * <p>The XSL style sheet whished to use for the transformation can also be
 * identified by a file name in {@link #XSLTProcessor(Source, String)}.</p> 
 * @author jhoeper
 */
public class XSLTProcessor
{

	/** The tfactory. */
	private TransformerFactory _tfactory = null;

	/** The source. */
	private Source _source = null;

	/** The result. */
	private Result _result = null;

	/** The trans. */
	private Transformer _trans = null;

	/**
	 * Creates a new instance of XSLTProcessor by given source and stylesheet. <br/>
	 * also instantiates the Transformer that applies the XSLT style to the given
	 * XML input.
	 * @param src as {@link StreamSource}, {@link DOMSource}, {@link SAXSource}
	 * @param stylesheet as {@link StreamSource}, {@link DOMSource}, ...
	 */
	public XSLTProcessor(final Source src, final Source stylesheet)
	{
		// TODO irgendwo zentral ablegen wegen reuse
		_tfactory = TransformerFactory.newInstance();
		try
		{
			_trans = _tfactory.newTransformer(stylesheet);
			_source = src;
		}
		catch (TransformerConfigurationException e)
		{
			e.printStackTrace();
			System.out.println("Transformer could not be initiated");
		}
	}

	/**
	 * Creates a new instance of XSLT Processor by given source, stylesheet and
	 * result.
	 * @param src source
	 * @param stylesheet style sheet
	 * @param rslt as implementation of xml.transform.Result
	 */
	public XSLTProcessor(final Source src, final Source stylesheet, final Result rslt)
	{
		this(src, stylesheet);
		_result = rslt;
	}

	/**
	 * Creates a new instance of XSLTProcessor by given source and the name of a
	 * stylesheet. In detail, creates a {@link StreamSource} for path passed
	 * with the <code>stylesheetfilename</code> parameter and calls 
	 * the constructor {@link #XSLTProcessor(Source, Source)}.
	 * @param src A {@link Source} object to read XML input from
	 * @param stylesheetfilename style sheet filename
	 */
	public XSLTProcessor(final Source src, final String stylesheetfilename)
	{
		this(src, new StreamSource(stylesheetfilename));
	}

	/**
	 * Processes the XSLT transformation of the XML input on which this 
	 * {@link XSLTProcessor} was instantiated. Since no output resource is
	 * passed, the result will be stored internally as a {@link DOMResult}
	 * which can be fetched from {@link #result()}, which for its part converts 
	 * the output to an {@link XMLContainer}.
	 * @return true if everything went right
	 */
	public final boolean process()
	{
		if (_trans != null)
		{
			if (_result == null)
			{
				System.out.println("Setting up result object");
				_result = new DOMResult();
			}
			try
			{
				_trans.transform(_source, _result);
			}
			catch (TransformerException e)
			{
				System.out.println("Could not transform");
				e.printStackTrace();
				return false;
			}
			return true;
		}
		else
		{
			System.out.println("No Transformer instantiated. Was a XSL style sheet loaded?");
			return false;
		}
	}

	/**
	 * processes XML data into given result object.
	 * @param rslt result
	 * @return true if it works
	 */
	public final boolean process(final Result rslt)
	{
		_result = rslt;
		return process();
	}

	/**
	 * process XML data into the file of the given name.
	 * @param resultfilename filename of the result
	 * @return successful
	 */
	public final boolean process(final String resultfilename)
	{
		StreamResult result;
		try
		{
			result = new StreamResult(resultfilename);
		}
		catch (Exception e)
		{
			System.out.println("Could not create StreamResult");
			return false;
		}
		return process(result);
	}

	/**
	 * Returns a new XMLContainer containing the transformation result. Works
	 * <b>only if the result is held in a DOMResult instance</b>.
	 * @return XMLContainer
	 */
	public final XMLContainer result()
	{
		XMLContainer xml = null;
		if (_result != null)
		{
			// DOMResult
			try
			{
				xml = new XMLContainer((Document) ((DOMResult) _result).getNode());
			}
			catch (Exception e)
			{
				System.out
						.println("Could not instantiate XML container for transformation result (Possibly no DOM format)");
				e.printStackTrace();
			}
			return xml;
		}
		else
		{
			System.out.println("No result available. Has transformation been conducted?");
			return null;
		}

	}
}
