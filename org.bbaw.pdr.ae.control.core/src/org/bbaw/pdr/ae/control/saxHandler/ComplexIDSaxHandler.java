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
package org.bbaw.pdr.ae.control.saxHandler;

import java.util.Collections;
import java.util.Vector;

import org.bbaw.pdr.ae.common.AEConstants;
import org.bbaw.pdr.ae.control.internal.Activator;
import org.bbaw.pdr.ae.metamodel.PdrId;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/**
 * The Class ComplexIDSaxHandler.
 * @author Christoph Plutte
 */
public class ComplexIDSaxHandler implements ContentHandler
{

	/** The ids. */
	private Vector<PdrId> _ids = new Vector<PdrId>();

	/** The id. */
	private PdrId _id = null;

	/** The aspect id. */
	private PdrId _aspectId = null;

	/** The _result object. */
	private Object _resultObject;

	/** The boolean for the text content of the relation object. */
	private boolean _object = false;

	private IProgressMonitor _monitor;

	public ComplexIDSaxHandler(IProgressMonitor monitor)
	{
		this._monitor = monitor;
	}

	@Override
	public final void characters(final char[] ch, final int start, final int length) throws SAXException
	{
		if (_object)
		{
			_id = new PdrId(new String(ch));
			if (!_id.equals(_aspectId))
			{
				if (!_ids.contains(_id))
				{
					_ids.add(_id);
				}
			}
			_object = false;
		}
	}

	@Override
	public void endDocument() throws SAXException
	{

	}

	@Override
	public final void endElement(final String uri, final String localName, final String qName) throws SAXException
	{
		if (localName.equals("asp"))
		{
			_ids.add(_aspectId);
		}

		else if (localName.equals("object"))
		{
			_object = false;

		}
		else if (localName.equals("result"))
		{
			if (_ids.isEmpty())
			{
				// ids.add("NO_RESULT");
			}
			Collections.sort(_ids);
			setResultObject(_ids);
		}

	}

	@Override
	public void endPrefixMapping(final String prefix) throws SAXException
	{

	}

	/**
	 * Gets the result object.
	 * @return the result object
	 */
	public final Object getResultObject()
	{
		return _resultObject;
	}

	@Override
	public void ignorableWhitespace(final char[] ch, final int start, final int length) throws SAXException
	{

	}

	@Override
	public void processingInstruction(final String target, final String data) throws SAXException
	{

	}

	@Override
	public void setDocumentLocator(final Locator locator)
	{

	}

	/**
	 * Sets the result object.
	 * @param resultObject the new result object
	 */
	private void setResultObject(final Object resultObject)
	{
		this._resultObject = resultObject;
	}

	@Override
	public void skippedEntity(final String name) throws SAXException
	{

	}

	@Override
	public void startDocument() throws SAXException
	{

	}

	@Override
	public final void startElement(final String uri, final String localName, final String qName, final Attributes atts)
			throws SAXException
	{
		if (_monitor != null && _monitor.isCanceled())
		{

			AEConstants.ILOGGER.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, "Parsing cancelled by user."));
			throw new SAXException("Parsing cancelled by user.");
		}
		if (localName.equals("a"))
		{
			for (int i = 0; i < atts.getLength(); i++)
			{
				if (atts.getQName(i).equals("id"))
				{
					_aspectId = new PdrId(new String(atts.getValue(i)));
				}
			}
		}
		else if (localName.equals("p"))
		{
			for (int i = 0; i < atts.getLength(); i++)
			{
				if (atts.getQName(i).equals("ana"))
				{
					_id = new PdrId(new String(atts.getValue(i)));
					if (!_ids.contains(_id))
					{
						_ids.add(_id);
					}
					break;

				}
				else if (atts.getQName(i).equals("subject"))
				{
					if (!atts.getValue(i).equals(_aspectId))
					{
						_id = new PdrId(new String(atts.getValue(i)));
						// TODO algorithmus beschleunigung.
						if (!_ids.contains(_id))
						{
							_ids.add(_id);
						}
					}
				}
				// else if (atts.getQName(i).equals("object"))
				// {
				// if (!atts.getValue(i).equals(_aspectId))
				// {
				// _id = new PdrId(new String(atts.getValue(i)));
				// if (!_ids.contains(_id))
				// {
				// _ids.add(_id);
				// }
				// }
				// }
			}
		}
		if (localName.equals("r"))
		{
			for (int i = 0; i < atts.getLength(); i++)
			{
				if (atts.getQName(i).equals("id"))
				{
					_ids.add(new PdrId(new String(atts.getValue(i))));
				}
			}
		}
		else if (localName.equals("object"))
		{
			_object = true;

		}

	}

	@Override
	public void startPrefixMapping(final String prefix, final String uri) throws SAXException
	{

	}
}
