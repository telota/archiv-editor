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
package org.bbaw.pdr.ae.collections.control;

import java.text.ParseException;
import java.util.Stack;

import org.bbaw.pdr.ae.collections.model.PDRCollection;
import org.bbaw.pdr.ae.common.AEConstants;
import org.bbaw.pdr.ae.control.facade.Facade;
import org.bbaw.pdr.ae.metamodel.PdrId;
import org.bbaw.pdr.ae.metamodel.Record;
import org.bbaw.pdr.ae.metamodel.Revision;
import org.bbaw.pdr.ae.model.PdrObject;
import org.bbaw.pdr.ae.model.view.TreeNode;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/** SaxHandler for parsing collections from file or db.
 * @author Christoph Plutte
 *
 */
public class CollectionSaxHandler extends DefaultHandler implements ContentHandler
{
	/** collection.*/
	private PDRCollection _coll = null;
	/** record.*/
	private Record _record = null;
	/** revision.*/
	private Revision _revision = null;
	/** treenode for objects in collection.*/
	private TreeNode _treeNode = null;
	/** pdrobject.*/
	private PdrObject _pdrObject = null;
	/** singleton facade.*/
	private Facade _facade = Facade.getInstanz();
	/** private stack.*/
	private Stack<TreeNode> _stack = new Stack<TreeNode>();


	@Override
	public void characters(final char[] arg0, final int arg1, final int arg2) throws SAXException
	{
	}

	@Override
	public void endDocument() throws SAXException
	{
	}

	@Override
	public final void endElement(final String u, final String name, final String qn)
    {
		if (name.equals("item") || qn.equals("item")
				|| name.equals("codl:item") || qn.equals("codl:item"))
  	    {
  	    	if (!_stack.isEmpty())
  	    	{
  	    		_stack.peek().addChild(_treeNode);
  	    		_treeNode = _stack.pop();
  	    	}
  	    	else
  	    	{
  	    		_coll.addItem(_treeNode);
  	    		_treeNode = null;
  	    	}
   	    }
		else if (name.equals("record") || name.equals("codl:record")
	    		|| qn.equals("record") || qn.equals("codl:record"))
	    {
	    	_coll.setRecord(_record);
	    }
	    else if (name.equals("revision") || name.equals("codl:revision")
	    		|| qn.equals("revision") || qn.equals("codl:revision"))
	    {
	    	_record.getRevisions().add(_revision);
	    }
	}

	@Override
	public void endPrefixMapping(final String arg0) throws SAXException
	{
	}

	@Override
	public void ignorableWhitespace(final char[] arg0, final int arg1, final int arg2)
			throws SAXException
			{
	}

	@Override
	public void processingInstruction(final String arg0, final String arg1)
			throws SAXException
	{
	}

	@Override
	public void setDocumentLocator(final Locator arg0)
	{
	}

	@Override
	public void skippedEntity(final String arg0) throws SAXException
	{

	}

	@Override
	public void startDocument() throws SAXException
	{
	}

	@Override
	public final void startElement(final String uri, final String localName, final String qName,
			final Attributes atts) throws SAXException
	{
		if (localName.equals("pdrCollection") || localName.equals("pdrCollection")
				|| localName.equals("codl:pdrCollection") || qName.equals("codl:pdrCollection"))
        {
        	_coll = new PDRCollection();
        	for (int i = 0; i < atts.getLength(); i++)
           	 {
                    if (atts.getQName(i).equals("name"))
                    {
                    	_coll.setName(atts.getValue(i));
                    }
           	 }
        }
		else if (localName.equals("record") || qName.equals("codl:record")
				|| localName.equals("codl:record") || qName.equals("record"))
	    {
	    	_record = new Record();
	    }
	    else if (localName.equals("revision")  || localName.equals("codl:revision")
	    		|| qName.equals("revision") || qName.equals("codl:revision"))
	    {
	   	 _revision = new Revision();
	   	 for (int i = 0; i < atts.getLength(); i++)
	   	 {
//	            System.out.println("Attribut: " + a.getQName(i)
//	                             + " Wert: " + a.getValue(i));
	            if (atts.getQName(i).equals("ref"))
	            {
	           	 _revision.setRef(Integer.valueOf(atts.getValue(i)).intValue());
	            }
	            else if (atts.getQName(i).equals("timestamp"))
	            {
	           	 try
	           	 {
						_revision.setTimeStamp(AEConstants.ADMINDATE_FORMAT.parse(atts.getValue(i)));
					}
	           	 catch (ParseException e)
	           	 {
						e.printStackTrace();
					}
	            }
	            else if (atts.getQName(i).equals("authority"))
	            {
	           	 _revision.setAuthority(new PdrId(atts.getValue(i)));
	            }
	        }
	    }
	    else if (localName.equals("item") || qName.equals("item")
	    		|| localName.equals("codl:item") || qName.equals("codl:item"))
	    {
	    	for (int i = 0; i < atts.getLength(); i++)
          	 {
                   if (atts.getQName(i).equals("id"))
                   {
	                   	String id = atts.getValue(i);
	                   	_pdrObject = _facade.getPdrObject(new PdrId(id));
	                   	if (id != null)
	                   	{
	                   		if (_treeNode != null)
	        	        	{
	        	        		_stack.push(_treeNode);
	        	        	}
	                   		if (_pdrObject != null)
	                   		{
	                   			_treeNode = new TreeNode(_pdrObject.getDisplayName(), _pdrObject);
	                   		}
	                   		else
	                   		{
	                   			_treeNode = new TreeNode(id, id.substring(0, 6));
	                   		}

	                   		break;
	                   	}
                   }
          	 }
	    }
	}

	@Override
	public void startPrefixMapping(final String arg0, final String arg1)
			throws SAXException
	{
	}

	/** get result object after parsing.
	 * @return returns result collection.
	 */
	public final PDRCollection getResultObject()
	{
		return _coll;
	}

}
