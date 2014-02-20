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
package org.bbaw.pdr.ae.config.core;

import java.util.HashMap;
import java.util.Stack;
import java.util.Vector;

import org.bbaw.pdr.ae.config.model.AspectConfigTemplate;
import org.bbaw.pdr.ae.config.model.ComplexSemanticTemplate;
import org.bbaw.pdr.ae.config.model.ConfigData;
import org.bbaw.pdr.ae.config.model.ConfigItem;
import org.bbaw.pdr.ae.config.model.DataType;
import org.bbaw.pdr.ae.config.model.DatatypeDesc;
import org.bbaw.pdr.ae.config.model.IdentifierConfig;
import org.bbaw.pdr.ae.config.model.SemanticTemplate;
import org.bbaw.pdr.ae.config.model.Usage;
import org.bbaw.pdr.ae.config.model.UsageDisplay;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/** SAX Handler for parsing datadesc
 * @author Christoph Plutte
 *
 */
/**
 * @author Christoph Plutte
 */
public class DataDescSaxHandler extends DefaultHandler // implements
														// ContentHandler
{
	/** default vector size. */
	private static final int SIZE = 3;

	/** config manager. */
	private IConfigManager _cfgManger;
	/** configs hash map. */
	private HashMap<String, DatatypeDesc> _configs = new HashMap<String, DatatypeDesc>();
	/** datatypedesc. */
	private DatatypeDesc _datatypeDesc;
	/** datatype. */
	private DataType _dataType;
	/** configitem. */
	private ConfigItem _configItem;
	/** language string. */
	private String _lang;

	/** help stack. */
	private Stack<ConfigItem> _stack = new Stack<ConfigItem>();

	/** vector of name tags. */
	private Vector<String> _nameTags = new Vector<String>(SIZE);
	/** vector of display name tags. */
	private Vector<String> _displayNameTags = new Vector<String>(SIZE);
	/** usage object. */
	private Usage _usage;
	/** display object of usage. */
	private UsageDisplay _usageDisplay;

	private ConfigData _templates;

	private ConfigData _aspectTemplates;

	private SemanticTemplate _semanticTemplate;

	private IdentifierConfig _identifierConfig;

	/** parsing boolean. */
	private boolean _b1 = false;
	// /** parsing boolean.*/
	// private boolean _b2 = false;
	/** parsing boolean. */
	private boolean _b3 = false;
	/** parsing boolean. */
	private boolean _b4 = false;
	/** parsing boolean. */
	private boolean _b5 = false;
	// /** parsing boolean.*/
	// private boolean _b6 = false;
	// /** parsing boolean.*/
	// private boolean _b8 = false;
	/** parsing boolean. */
	private boolean _b9 = false;
	/** parsing boolean. */
	private boolean _b10 = false;

	/** parsing boolean. */
	private boolean _b11 = false;
	/** parsing boolean. */
	private boolean _templateText = false;

	private boolean _semanticTemplateB = false;
	// /** parsing boolean.*/
	// private boolean _b13 = false;

	private AspectConfigTemplate _markupTemplate;

	private ConfigData _semanticTemplates;

	private boolean _complexTemplatesB;

	private ConfigData _complexTemplates;

	private ComplexSemanticTemplate _complexTemplate;

	private boolean _markupTemplateB;

	private boolean _complexTemplateB;

	private boolean _identifierConfigB;

	private ConfigData _personIdentifiers;

	/**
	 * constructor with IconfigManger.
	 * @param configManager instance of configManager to set Configs
	 */
	public DataDescSaxHandler(final IConfigManager configManager)
	{
		this._cfgManger = configManager;
	}

	/**
	 * start element found.
	 * @param u uri
	 * @param name localName
	 * @param qn QName
	 * @param a attributes
	 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String,
	 *      java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	public final void startElement(final String u, final String name, final String qn, final Attributes a)
	{
		// System.out.println("start element " + name + " u " + u + " qn " +
		// qn);
		// datatypeDesc
		if (name.equals("datatypeDesc") || name.equals("dtdl:datatypeDesc") || qn.equals("dtdl:datatypeDesc"))
		{
			// System.out.println("im sax handler datatypeDesc");

			_b1 = true;
			_datatypeDesc = new DatatypeDesc();
			for (int i = 0; i < a.getLength(); i++)
			{
				if (a.getQName(i).equals("provider"))
				{
					_datatypeDesc.setProvider(a.getValue(i));
				}
			}
		}
		// documentation
		else if (name.equals("documentation") || name.equals("dtdl:documentation") || qn.equals("dtdl:documentation"))
		{
			// _b2 = true;
		}

		// docPart
		else if (name.equals("docPart") || name.equals("dtdl:docPart") || qn.equals("dtdl:docPart"))
		{
			_b3 = true;

			for (int i = 0; i < a.getLength(); i++)
			{
				if (a.getQName(i).equals("xml:lang"))
				{
					_lang = a.getValue(i);
				}
			}

		}
		// usage
		else if (name.equals("usage") || name.equals("dtdl:usage") || qn.equals("dtdl:usage"))
		{
			_usage = new Usage();
			_b1 = false;
		}
		// usage
		else if (name.equals("display") || name.equals("dtdl:display") || qn.equals("dtdl:display"))
		{
			_usageDisplay = new UsageDisplay();
			_nameTags = new Vector<String>(SIZE);
			_displayNameTags = new Vector<String>(SIZE);

		}
		// usage
		else if (name.equals("nameTag") || name.equals("dtdl:nameTag") || qn.equals("dtdl:nameTag"))
		{
			_b9 = true;

		}
		// usage
		else if (name.equals("displayNameTag") || name.equals("dtdl:displayNameTag")
				|| qn.equals("dtdl:displayNameTag"))
		{
			_b10 = true;

		}
		// templates
		else if (name.equals("templates") || name.equals("dtdl:templates") || qn.equals("dtdl:templates"))
		{
			_templates = new ConfigData();
			_templates.setValue("templates");
			_templates.setLabel("Templates");
			_usage.setTemplates(_templates);
		}
		// aspectTemplates
		else if (name.equals("aspectTemplates") || name.equals("dtdl:aspectTemplates")
				|| qn.equals("dtdl:aspectTemplates"))
		{
			_aspectTemplates = new ConfigData();
			_aspectTemplates.setValue("aspectTemplates");
			_aspectTemplates.setLabel("Aspect Templates");
			_templates.getChildren().put("aspectTemplates", _aspectTemplates);
		}
		// complexTemplates
		else if (name.equals("complexTemplates") || name.equals("dtdl:complexTemplates")
				|| qn.equals("dtdl:complexTemplates"))
		{
			_complexTemplatesB = true;
			_complexTemplates = new ConfigData();
			_complexTemplates.setValue("complexTemplates");
			_complexTemplates.setLabel("Complex Templates");
			for (int i = 0; i < a.getLength(); i++)
			{
				if (a.getQName(i).equals("ignore"))
				{
					_complexTemplates.setIgnore(a.getValue(i) != null && a.getValue(i).equals("true"));
				}
			}
			_aspectTemplates.getChildren().put("complexTemplates", _complexTemplates);
		}
		// complexTemplate
		else if (name.equals("complexTemplate") || name.equals("dtdl:complexTemplate")
				|| qn.equals("dtdl:complexTemplate"))
		{
			_complexTemplateB = true;
			_complexTemplate = new ComplexSemanticTemplate();
			for (int i = 0; i < a.getLength(); i++)
			{
				if (a.getQName(i).equals("value"))
				{
					_complexTemplate.setValue(a.getValue(i));
				}
				else if (a.getQName(i).equals("label"))
				{
					_complexTemplate.setLabel(a.getValue(i));
				}
				else if (a.getQName(i).equals("priority"))
				{
					_complexTemplate.setPriority(Integer.valueOf(a.getValue(i)));
				}
			}
			_complexTemplate.setParent(_complexTemplates);
			_complexTemplates.getChildren().put(_complexTemplate.getLabel(), _complexTemplate);
		}
		// aspectTemplates
		else if (name.equals("semanticTemplates") || name.equals("dtdl:semanticTemplates")
				|| qn.equals("dtdl:semanticTemplates"))
		{
			_semanticTemplates = new ConfigData();
			_semanticTemplates.setValue("semanticTemplates");
			_semanticTemplates.setLabel("Semantic Templates");
			for (int i = 0; i < a.getLength(); i++)
			{
				if (a.getQName(i).equals("ignore"))
				{
					_aspectTemplates.setIgnore(a.getValue(i) != null && a.getValue(i).equals("true"));
				}
			}
			_aspectTemplates.getChildren().put("semanticTemplates", _semanticTemplates);
		}
		// semanticTemplate
		else if (name.equals("semanticTemplate") || name.equals("dtdl:semanticTemplate")
				|| qn.equals("dtdl:semanticTemplate"))
		{
			_semanticTemplate = new SemanticTemplate();
			_semanticTemplateB = true;
			for (int i = 0; i < a.getLength(); i++)
			{
				if (a.getQName(i).equals("semantic"))
				{
					_semanticTemplate.setValue(a.getValue(i));
				}
				else if (a.getQName(i).equals("label"))
				{
					_semanticTemplate.setLabel(a.getValue(i));
				}
				else if (a.getQName(i).equals("priority"))
				{
					_semanticTemplate.setPriority(Integer.valueOf(a.getValue(i)));
				}
				else if (a.getQName(i).equals("allowMultiple"))
				{
					_semanticTemplate.setAllowMultiple(a.getValue(i) != null && a.getValue(i).equals("true"));

				}
			}
			if (_semanticTemplate.getLabel() == null || _semanticTemplate.getLabel().trim().length() == 0)
			{
				_semanticTemplate.setLabel(new String(_semanticTemplate.getValue()));
			}
		}
		// templateText
		else if (name.equals("templateText") || name.equals("dtdl:templateText") || qn.equals("dtdl:templateText"))
		{
			_templateText = true;
			for (int i = 0; i < a.getLength(); i++)
			{
				if (a.getQName(i).equals("ignore"))
				{
					_semanticTemplate.setIgnoreTemplateText(a.getValue(i) != null && a.getValue(i).equals("true"));
				}
			}
		}
		else if (name.equals("markupTemplates") || name.equals("dtdl:markupTemplates")
				|| qn.equals("dtdl:markupTemplates"))
		{
			for (int i = 0; i < a.getLength(); i++)
			{
				if (a.getQName(i).equals("ignore"))
				{
					_semanticTemplate.setIgnore(a.getValue(i) != null && a.getValue(i).equals("true"));

				}
			}
		}
		else if (name.equals("markupTemplate") || name.equals("dtdl:markupTemplate")
				|| qn.equals("dtdl:markupTemplate"))
		{
			_markupTemplate = new AspectConfigTemplate();
			_markupTemplateB = true;
			_markupTemplate.setParent(_semanticTemplate);
			for (int i = 0; i < a.getLength(); i++)
			{
				if (a.getQName(i).equals("widgetType"))
				{
					_markupTemplate.setWidgetType(new Integer(a.getValue(i)));

				}
				else if (a.getQName(i).equals("requiered"))
				{
					_markupTemplate.setRequired(a.getValue(i) != null && a.getValue(i).equals("true"));

				}
				if (a.getQName(i).equals("ignore"))
				{
					_markupTemplate.setIgnore(a.getValue(i) != null && a.getValue(i).equals("true"));

				}
				if (a.getQName(i).equals("priority"))
				{
					_markupTemplate.setPriority(Integer.valueOf(a.getValue(i)));
				}
				else if (a.getQName(i).equals("element"))
				{
					_markupTemplate.setElement(a.getValue(i));

				}
				else if (a.getQName(i).equals("type"))
				{
					_markupTemplate.setType(a.getValue(i));

				}
				else if (a.getQName(i).equals("subtype"))
				{
					_markupTemplate.setSubtype(a.getValue(i));

				}
				else if (a.getQName(i).equals("role"))
				{
					_markupTemplate.setRole(a.getValue(i));

				}
				else if (a.getQName(i).equals("date1"))
				{
					_markupTemplate.setDate1(a.getValue(i));

				}
				else if (a.getQName(i).equals("date2"))
				{
					_markupTemplate.setDate2(a.getValue(i));

				}
				else if (a.getQName(i).equals("pos"))
				{
					_markupTemplate.setPos(a.getValue(i));

				}
				else if (a.getQName(i).equals("levelSpan"))
				{
					_markupTemplate.setLevelSpan(new Integer(a.getValue(i)));

				}
				else if (a.getQName(i).equals("allowMultiple"))
				{
					_markupTemplate.setAllowMultiple(a.getValue(i) != null && a.getValue(i).equals("true"));

				}
				else if (a.getQName(i).equals("label"))
				{
					_markupTemplate.setLabel(a.getValue(i));
					_markupTemplate.setValue(a.getValue(i));

				}
				else if (a.getQName(i).equals("prefix"))
				{
					_markupTemplate.setPrefix(a.getValue(i));

				}
				else if (a.getQName(i).equals("suffix"))
				{
					_markupTemplate.setSuffix(a.getValue(i));

				}
				else if (a.getQName(i).equals("horizontalSpan"))
				{
					_markupTemplate.setHorizontalSpan(new Integer(a.getValue(i)));

				}
			}
			_semanticTemplate.getChildren().put(_markupTemplate.getLabel(), _markupTemplate);

		}
		// personIdentifiers
		else if (name.equals("personIdentifiers") || name.equals("dtdl:personIdentifiers")
				|| qn.equals("dtdl:personIdentifiers"))
		{
			_personIdentifiers = new ConfigData();
			_personIdentifiers.setValue("personIdentifiers");
			_personIdentifiers.setLabel("Person Identifiers");
			_personIdentifiers.setMyHaveChildren(true);
			_personIdentifiers.setPos("personIdentifiers");
			_usage.setIdentifiers(_personIdentifiers);
		}
		// identifierConfig
				else if (name.equals("identifier") || name.equals("dtdl:identifier") || qn.equals("dtdl:identifier"))
				{
					_identifierConfigB = true;
					_identifierConfig = new IdentifierConfig();
					for (int i = 0; i < a.getLength(); i++)
					{
						if (a.getQName(i).equals("priority"))
						{
							_identifierConfig.setPriority(Integer.valueOf(a.getValue(i)));
						}
						else if (a.getQName(i).equals("value"))
						{
							_identifierConfig.setValue(a.getValue(i));
						}
						else if (a.getQName(i).equals("label"))
						{
							_identifierConfig.setLabel(a.getValue(i));
						}
						else if (a.getQName(i).equals("ignore"))
						{
							_identifierConfig.setIgnore(a.getValue(i).equals("true"));
						}
						else if (a.getQName(i).equals("mandatory"))
						{
							_identifierConfig.setMandatory(a.getValue(i).equals("true"));
						}
						else if (a.getQName(i).equals("url"))
						{
							_identifierConfig.setUrl(a.getValue(i));
						}
						else if (a.getQName(i).equals("prefix"))
						{
							_identifierConfig.setPrefix(a.getValue(i));
						}
						else if (a.getQName(i).equals("suffix"))
						{
					_identifierConfig.setSuffix(a.getValue(i));
						}
						else if (a.getQName(i).equals("regex"))
						{
							_identifierConfig.setRegex(a.getValue(i));
						}
					}
			_identifierConfig.setParent(_personIdentifiers);
			_identifierConfig.setPos("personIdentifier");

			_personIdentifiers.getChildren().put(_identifierConfig.getValue(), _identifierConfig);
				}
		// datatype
		else if (name.equals("datatype") || name.equals("dtdl:datatype") || qn.equals("dtdl:datatype"))
		{
			_dataType = new DataType();
			_dataType.setMyHaveChildren(true);
			_b4 = true;
			_b1 = false;

			for (int i = 0; i < a.getLength(); i++)
			{
				if (a.getQName(i).equals("element"))
				{
					_dataType.setValue(a.getValue(i));
				}
				if (a.getQName(i).equals("label"))
				{
					_dataType.setLabel(a.getValue(i));
				}
				if (a.getQName(i).equals("type"))
				{
					_dataType.setPos(a.getValue(i));
					if (a.getValue(i).equals("tree"))
					{
						_b11 = true;
					}
					else
					{
						_b11 = false;
					}
				}
			}
		}
		// item
		else if (name.equals("item") || name.equals("dtdl:item") || qn.equals("dtdl:item"))
		{
			_b4 = false;
			_b5 = true;
			if (_configItem != null)
			{
				_stack.push(_configItem);
			}
			_configItem = new ConfigItem();
			for (int i = 0; i < a.getLength(); i++)
			{
				if (a.getQName(i).equals("priority"))
				{
					_configItem.setPriority(Integer.valueOf(a.getValue(i)));
				}
				else if (a.getQName(i).equals("value"))
				{
					_configItem.setValue(a.getValue(i));
				}
				if (a.getQName(i).equals("label"))
				{
					_configItem.setLabel(a.getValue(i));
				}
				else if (a.getQName(i).equals("pos"))
				{
					_configItem.setPos(a.getValue(i));
				}
				else if (a.getQName(i).equals("ignore"))
				{
					_configItem.setIgnore(a.getValue(i).equals("true"));
				}
				else if (a.getQName(i).equals("mandatory"))
				{
					_configItem.setMandatory(a.getValue(i).equals("true"));
				}
			}
		}
	}

	/**
	 * end element found.
	 * @param u uri
	 * @param name localName
	 * @param qn QName
	 * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String,
	 *      java.lang.String, java.lang.String)
	 */
	public final void endElement(final String u, final String name, final String qn)
	{
		if (name.equals("item") || name.equals("dtdl:item") || qn.equals("dtdl:item"))
		{
			if (_configItem.getLabel() == null || _configItem.getLabel().trim().length() == 0)
			{
				_configItem.setLabel(_configItem.getValue());
			}
			_b5 = false;
			if (_b11)
			{
				if (_configItem.getPos() != null
						&& (_configItem.getPos().equals("role") || _configItem.getPos().equals("_TEXTNODE")))
				{
					_configItem.setMyHaveChildren(false);
				}
				else
				{
					_configItem.setMyHaveChildren(true);
				}
			}
			if (!_stack.isEmpty())
			{
				_configItem.setParent(_stack.peek());
				_stack.peek().getChildren().put(_configItem.getValue(), _configItem);
				_configItem = _stack.pop();
			}
			else
			{
				_configItem.setParent(_dataType);
				_dataType.getChildren().put(_configItem.getValue(), _configItem);
				_configItem = null;
			}

		}

		else if (name.equals("datatype") || name.equals("dtdl:datatype") || qn.equals("dtdl:datatype"))
		{
			if (_dataType.getLabel() == null || _dataType.getLabel().trim().length() == 0)
			{
				_dataType.setLabel(_dataType.getValue());
			}
			_dataType.setMyHaveChildren(true);
			_dataType.setDatatypeDesc(_datatypeDesc);

			_datatypeDesc.getChildren().put(_dataType.getValue(), _dataType);
		}
		else if (name.equals("datatypeDesc") || name.equals("dtdl:datatypeDesc") || qn.equals("dtdl:datatypeDesc"))
		{
			_configs.put(_datatypeDesc.getProvider().toUpperCase(), _datatypeDesc);
			_nameTags = null;
			_displayNameTags = null;
			_usageDisplay = null;
			_usage = null;
		}
		else if (name.equals("usage") || name.equals("dtdl:usage") || qn.equals("dtdl:usage"))
		{
			_usageDisplay.setPersonNameTag(_nameTags);
			_usageDisplay.setPersonNormNameTag(_displayNameTags);
			_usage.setUsageDisplay(_usageDisplay);
			_datatypeDesc.setUsage(_usage);
		}
		else if (name.equals("templateText") || name.equals("dtdl:templateText") || qn.equals("dtdl:templateText"))
		{
			_templateText = false;
		}
		else if (name.equals("semanticTemplates") || name.equals("dtdl:semanticTemplates")
				|| qn.equals("dtdl:semanticTemplates"))
		{
			// _aspectTemplates.getChildren().put("semanticTemplates",
			// _semanticTemplates);
		}
		else if (name.equals("complexTemplates") || name.equals("dtdl:complexTemplates")
				|| qn.equals("dtdl:complexTemplates"))
		{
			_complexTemplatesB = false;
		}
		else if (name.equals("complexTemplate") || name.equals("dtdl:complexTemplate")
				|| qn.equals("dtdl:complexTemplate"))
		{
			_complexTemplateB = false;
		}
		else if (name.equals("semanticTemplate") || name.equals("dtdl:semanticTemplate")
				|| qn.equals("dtdl:semanticTemplate"))
		{
			_semanticTemplateB = false;
			if (_complexTemplatesB && _complexTemplate != null)
			{
				_semanticTemplate.setParent(_complexTemplate);

				_complexTemplate.getChildren().put(_semanticTemplate.getValue(), _semanticTemplate);
			}
			else if (_semanticTemplates != null)
			{
				_semanticTemplate.setParent(_semanticTemplates);
				_semanticTemplates.getChildren().put(_semanticTemplate.getValue(), _semanticTemplate);
			}
		}
		else if (name.equals("markupTemplate") || name.equals("dtdl:markupTemplate")
				|| qn.equals("dtdl:markupTemplate"))
		{
			_markupTemplateB = false;
		}
		// identifierConfig
		else if (name.equals("identifier") || name.equals("dtdl:identifier") || qn.equals("dtdl:identifier"))
		{
			_identifierConfigB = false;
		}
		// identifierConfig
		else if (name.equals("personIdentifiers") || name.equals("dtdl:personIdentifiers")
				|| qn.equals("dtdl:personIdentifiers"))
		{
			_identifierConfigB = false;
		}
		else if (name.equals("result"))
		{
			// System.out.println("verlasse sax handler");

			_cfgManger.setConfigs(_configs);
		}
		else if (name.equals("resultProvider"))
		{

			_cfgManger.setDatatypeDesc(_datatypeDesc);
		}
	}

	/**
	 * characters.
	 * @param ch chars
	 * @param start start
	 * @param len length of characters
	 * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
	 */
	public final void characters(final char[] ch, final int start, final int len)
	{
		// notification surName
		if (_b1 && _b3)
		{
			_datatypeDesc.getDocumentation().put(_lang, new String(ch, start, len));
			_b3 = false;
		}
		// notification foreName
		else if (_b4 && _b3)
		{
			_dataType.getDocumentation().put(_lang, new String(ch, start, len));
			_b3 = false;
		}
		// notification nameLink
		else if (_b5 && _b3)
		{
			_configItem.getDocumentation().put(_lang, new String(ch, start, len));
			_b3 = false;
		}
		else if (_markupTemplateB && _b3)
		{
			_markupTemplate.getDocumentation().put(_lang, new String(ch, start, len));
			_b3 = false;
		}
		else if (_b9)
		{
			_nameTags.add(new String(ch, start, len));
			_b9 = false;
		}
		else if (_b10)
		{
			_displayNameTags.add(new String(ch, start, len));

			_b10 = false;
		}
		if (_templateText)
		{
			_semanticTemplate.setTemplateText(new String(ch, start, len));

			_templateText = false;
		}
		else if (_semanticTemplateB && _b3)
		{
			_semanticTemplate.getDocumentation().put(_lang, new String(ch, start, len));
			_b3 = false;
		}
		else if (_complexTemplateB && _b3)
		{
			_complexTemplate.getDocumentation().put(_lang, new String(ch, start, len));
			_b3 = false;
		}
		else if (_identifierConfigB && _b3)
		{
			_identifierConfig.getDocumentation().put(_lang, new String(ch, start, len));
			_b3 = false;
		}
	}

	/**
	 * set Locator.
	 * @param locator locator.
	 * @see org.xml.sax.helpers.DefaultHandler#setDocumentLocator(org.xml.sax.Locator)
	 */
	public void setDocumentLocator(final Locator locator)
	{
	}

	/**
	 * startDocument.
	 * @throws SAXException esc.
	 * @see org.xml.sax.helpers.DefaultHandler#startDocument()
	 */
	public void startDocument() throws SAXException
	{
		// System.out.println("im sax handler startDocument");

	}

	/**
	 * endDoc.
	 * @throws SAXException exc.
	 * @see org.xml.sax.helpers.DefaultHandler#endDocument()
	 */
	public final void endDocument() throws SAXException
	{
		// System.out.println("end document");
		_cfgManger.setDatatypeDesc(_datatypeDesc);

	}

	/**
	 * start prefix mapping.
	 * @param prefix prefix
	 * @param uri uri
	 * @throws SAXException exc.
	 * @see org.xml.sax.helpers.DefaultHandler#startPrefixMapping(java.lang.String,
	 *      java.lang.String)
	 */
	public void startPrefixMapping(final String prefix, final String uri) throws SAXException
	{

	}

	/**
	 * end prefix mapping.
	 * @param prefix prefix.
	 * @throws SAXException exc
	 * @see org.xml.sax.helpers.DefaultHandler#endPrefixMapping(java.lang.String)
	 */
	public void endPrefixMapping(final String prefix) throws SAXException
	{

	}

	/**
	 * ignorableWhitespace.
	 * @param ch chars
	 * @param start start
	 * @param length length
	 * @throws SAXException exc.
	 * @see org.xml.sax.helpers.DefaultHandler#ignorableWhitespace(char[], int,
	 *      int)
	 */
	public void ignorableWhitespace(final char[] ch, final int start, final int length) throws SAXException
	{

	}

	/**
	 * processingIntruction.
	 * @param target target
	 * @param data data
	 * @throws SAXException exc.
	 * @see org.xml.sax.helpers.DefaultHandler#processingInstruction(java.lang.String,
	 *      java.lang.String)
	 */
	public void processingInstruction(final String target, final String data) throws SAXException
	{

	}

	/**
	 * skipped entity.
	 * @param name name
	 * @throws SAXException exc.
	 * @see org.xml.sax.helpers.DefaultHandler#skippedEntity(java.lang.String)
	 */
	public void skippedEntity(final String name) throws SAXException
	{

	}

}
