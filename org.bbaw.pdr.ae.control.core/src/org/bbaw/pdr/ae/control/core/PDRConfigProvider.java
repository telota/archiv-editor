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
package org.bbaw.pdr.ae.control.core;

import java.util.HashMap;
import java.util.Vector;

import org.bbaw.pdr.ae.common.AEConstants;
import org.bbaw.pdr.ae.common.CommonActivator;
import org.bbaw.pdr.ae.config.model.ConfigData;
import org.bbaw.pdr.ae.config.model.ConfigItem;
import org.bbaw.pdr.ae.config.model.DatatypeDesc;
import org.bbaw.pdr.ae.control.facade.Facade;
import org.eclipse.core.runtime.Platform;

/**
 * class provides classification and relation configuration.
 * @author Christoph Plutte
 */
public class PDRConfigProvider
{
	private static final int LINE_LENGTH = 60;

	private PDRConfigProvider()
	{
	};
	/** __facade singleton instance. */
	private static Facade _facade = Facade.getInstanz();
	/** semantic provider. */
	private static String _semanticProvider = Platform
			.getPreferencesService()
			.getString(CommonActivator.PLUGIN_ID,
					"PRIMARY_SEMANTIC_PROVIDER", AEConstants.CLASSIFICATION_AUTHORITY, null).toUpperCase(); //$NON-NLS-1$
	/** markup provider. */
	private static String _markupProvider = Platform.getPreferencesService()
			.getString(CommonActivator.PLUGIN_ID, "PRIMARY_TAGGING_PROVIDER", AEConstants.TAGGING_LIST_PROVIDER, null)
			.toUpperCase();
	/** relation classification provider. */
	private static String _relationProvider = Platform
			.getPreferencesService()
			.getString(CommonActivator.PLUGIN_ID, "PRIMARY_RELATION_PROVIDER",
					AEConstants.RELATION_CLASSIFICATION_PROVIDER, null).toUpperCase();

	/**
	 * returns the label of a markup value from aspect object according to the
	 * classification configuration.
	 * @param element the markup element.
	 * @param type the type attribute of the markup - maybe null.
	 * @param subtype the subtype attribute of the markup - maybe null.
	 * @param role the role attribute of the markup - maybe null.
	 * @return label of last parameter which is not null. returns the label of
	 *         the markup element or attribut. - if label not found in
	 *         classification configuration, it returns the given value of the
	 *         last parameter which is not null.
	 */
	public static final String getLabelOfMarkup(String element, final String type, final String subtype,
			final String role)
	{
		if (element != null && !element.startsWith("aodl:"))
		{
			element = "aodl:" + element; //$NON-NLS-1$ //$NON-NLS-2$
		}
		String label = null;
		Vector<String> providers = new Vector<String>();
		for (String s : _facade.getConfigs().keySet())
		{
			if (!s.equals(_markupProvider))
			{
				providers.add(s);
			}
		}
		HashMap<String, ConfigData> configs = new HashMap<String, ConfigData>();
		if (element != null && type == null)
		{

			if (_facade.getConfigs().containsKey(_markupProvider)
					&& _facade.getConfigs().get(_markupProvider).getChildren() != null
					&& _facade.getConfigs().get(_markupProvider).getChildren().containsKey(element))
			{
				configs.putAll(_facade.getConfigs().get(_markupProvider).getChildren());
			}
			if (configs.get(element) != null)
			{
				label = configs.get(element).getLabel();
			}
			else
			{
				label = element;
			}
		}
		else if (element != null && type != null && subtype == null)
		{
			// System.out.println("get label for type " + type);
			for (String p : providers)
			{
				if (_facade.getConfigs().get(p).getChildren().containsKey(element))
				{
					configs.putAll(_facade.getConfigs().get(p).getChildren().get(element).getChildren());
				}
			}
			// System.out.println("markupprovider " + provider);

			if (_facade.getConfigs().containsKey(_markupProvider)
					&& _facade.getConfigs().get(_markupProvider).getChildren() != null
					&& _facade.getConfigs().get(_markupProvider).getChildren().containsKey(element))
			{
				configs.putAll(_facade.getConfigs().get(_markupProvider).getChildren().get(element).getChildren());
			}
			// System.out.println("config size2 " + configs.size());

			if (configs.get(type) != null)
			{
				label = configs.get(type).getLabel();
			}
			else
			{
				label = type;
				// System.out.println("get label for label " + label);
			}

		}
		else if (element != null && type != null && subtype != null && role == null)
		{
			for (String provider : providers)
			{
				if (_facade.getConfigs().get(provider).getChildren().containsKey(element)
						&& _facade.getConfigs().get(provider).getChildren().get(element).getChildren() != null
						&& _facade.getConfigs().get(provider).getChildren().get(element).getChildren()
								.containsKey(type))
				{
					configs.putAll(_facade.getConfigs().get(provider).getChildren().get(element).getChildren()
							.get(type).getChildren());
				}
			}
			if (_facade.getConfigs().containsKey(_markupProvider)
					&& _facade.getConfigs().get(_markupProvider).getChildren() != null
					&& _facade.getConfigs().get(_markupProvider).getChildren().containsKey(element)
					&& _facade.getConfigs().get(_markupProvider).getChildren().get(element).getChildren() != null
					&& _facade.getConfigs().get(_markupProvider).getChildren().get(element).getChildren()
							.containsKey(type))
			{
				configs.putAll(_facade.getConfigs().get(_markupProvider).getChildren().get(element).getChildren()
						.get(type).getChildren());
			}
			if (configs.get(subtype) != null)
			{
				label = configs.get(subtype).getLabel();
			}
			else
			{
				label = subtype;
			}
		}
		else if (element != null && type != null && subtype != null && role != null)
		{
			for (String provider : providers)
			{
				if (_facade.getConfigs().get(provider).getChildren().containsKey(element)
						&& _facade.getConfigs().get(provider).getChildren().get(element).getChildren() != null
						&& _facade.getConfigs().get(provider).getChildren().get(element).getChildren()
								.containsKey(type)
						&& _facade.getConfigs().get(provider).getChildren().get(element).getChildren().get(type)
								.getChildren() != null
						&& _facade.getConfigs().get(provider).getChildren().get(element).getChildren().get(type)
								.getChildren().containsKey(subtype))
				{
					configs.putAll(_facade.getConfigs().get(provider).getChildren().get(element).getChildren()
							.get(type).getChildren().get(subtype).getChildren());
				}
			}
			if (_facade.getConfigs().containsKey(_markupProvider)
					&& _facade.getConfigs().get(_markupProvider).getChildren() != null
					&& _facade.getConfigs().get(_markupProvider).getChildren().containsKey(element)
					&& _facade.getConfigs().get(_markupProvider).getChildren().get(element).getChildren() != null
					&& _facade.getConfigs().get(_markupProvider).getChildren().get(element).getChildren()
							.containsKey(type)
					&& _facade.getConfigs().get(_markupProvider).getChildren().get(element).getChildren().get(type)
							.getChildren() != null
					&& _facade.getConfigs().get(_markupProvider).getChildren().get(element).getChildren().get(type)
							.getChildren().containsKey(subtype))
			{
				configs.putAll(_facade.getConfigs().get(_markupProvider).getChildren().get(element).getChildren()
						.get(type).getChildren().get(subtype).getChildren());
			}

			if (configs.get(role) != null)
			{
				label = configs.get(role).getLabel();
			}
			else
			{
				label = role;
			}
		}
		return label;
	}

	/**
	 * returns the label of a relation value from aspect object according to the
	 * classification configuration.
	 * @param provider the provider from the relation element of the
	 *            relationStm.
	 * @param context the context from the relation element of the relationStm.
	 * @param classR the class from the relation element of the relationStm.
	 * @param value the text content from the relation element of the
	 *            relationStm.
	 * @return label of last parameter which is not null. returns the label of
	 *         the relation context, class or value. - if label not found in
	 *         classification configuration, it returns the given value of the
	 *         last parameter which is not null.
	 */
	public static final String getLabelOfRelation(String provider, final String context, final String classR,
			final String value)
	{
		String label = null;
		if (provider == null)
		{
			provider = _relationProvider;
		}
		Vector<String> providers = new Vector<String>();
		for (String s : _facade.getConfigs().keySet())
		{
			if (!s.equals(provider))
			{
				providers.add(s);
			}
		}
		HashMap<String, ConfigData> configs = new HashMap<String, ConfigData>();
		String element = "aodl:relation";
		if (context != null && classR == null)
		{
			// System.out.println("get label for type " + type);
			for (String p : providers)
			{
				if (_facade.getConfigs().get(p).getChildren().containsKey(element))
				{
					configs.putAll(_facade.getConfigs().get(p).getChildren().get(element).getChildren());
				}
			}
			// System.out.println("markupprovider " + provider);

			if (_facade.getConfigs().containsKey(provider) && _facade.getConfigs().get(provider).getChildren() != null
					&& _facade.getConfigs().get(provider).getChildren().containsKey(element))
			{
				configs.putAll(_facade.getConfigs().get(provider).getChildren().get(element).getChildren());
			}
			// System.out.println("config size2 " + configs.size());

			if (configs.get(context) != null)
			{
				label = configs.get(context).getLabel();
			}
			else
			{
				label = context;
				// System.out.println("get label for label " + label);
			}

		}
		else if (context != null && classR != null && value == null)
		{
			for (String p : providers)
			{
				if (_facade.getConfigs().get(p).getChildren().containsKey(element)
						&& _facade.getConfigs().get(p).getChildren().get(element).getChildren() != null
						&& _facade.getConfigs().get(p).getChildren().get(element).getChildren().containsKey(context))
				{
					configs.putAll(_facade.getConfigs().get(p).getChildren().get(element).getChildren().get(context)
							.getChildren());
				}
			}
			if (_facade.getConfigs().containsKey(provider) && _facade.getConfigs().get(provider).getChildren() != null
					&& _facade.getConfigs().get(provider).getChildren().containsKey(element)
					&& _facade.getConfigs().get(provider).getChildren().get(element).getChildren() != null
					&& _facade.getConfigs().get(provider).getChildren().get(element).getChildren().containsKey(context))
			{
				configs.putAll(_facade.getConfigs().get(provider).getChildren().get(element).getChildren().get(context)
						.getChildren());
			}
			if (configs.get(classR) != null)
			{
				label = configs.get(classR).getLabel();
			}
			else
			{
				label = classR;
			}
		}
		else if (context != null && classR != null && value != null)
		{
			for (String p : providers)
			{
				if (_facade.getConfigs().get(p).getChildren().containsKey(element)
						&& _facade.getConfigs().get(p).getChildren().get(element).getChildren() != null
						&& _facade.getConfigs().get(p).getChildren().get(element).getChildren().containsKey(context)
						&& _facade.getConfigs().get(p).getChildren().get(element).getChildren().get(context)
								.getChildren() != null
						&& _facade.getConfigs().get(p).getChildren().get(element).getChildren().get(context)
								.getChildren().containsKey(classR))
				{
					configs.putAll(_facade.getConfigs().get(p).getChildren().get(element).getChildren().get(context)
							.getChildren().get(classR).getChildren());
				}
			}
			if (_facade.getConfigs().containsKey(provider)
					&& _facade.getConfigs().get(provider).getChildren() != null
					&& _facade.getConfigs().get(provider).getChildren().containsKey(element)
					&& _facade.getConfigs().get(provider).getChildren().get(element).getChildren() != null
					&& _facade.getConfigs().get(provider).getChildren().get(element).getChildren().containsKey(context)
					&& _facade.getConfigs().get(provider).getChildren().get(element).getChildren().get(context)
							.getChildren() != null
					&& _facade.getConfigs().get(provider).getChildren().get(element).getChildren().get(context)
							.getChildren().containsKey(classR))
			{
				configs.putAll(_facade.getConfigs().get(provider).getChildren().get(element).getChildren().get(context)
						.getChildren().get(classR).getChildren());
			}

			if (configs.get(value) != null)
			{
				label = configs.get(value).getLabel();
			}
			else
			{
				label = value;
			}
		}
		return label;
	}

	/**
	 * returns the label of a semantic value from aspect object according to the
	 * classification configuration.
	 * @param prov the provider from the semanticStm of the aspect.
	 * @param semantic the semantic Label from semanticStm of the aspect.
	 * @return the label that is the displayname of this semantic value
	 *         (semantic Label from semanticStm).
	 */
	public static final String getSemanticLabel(String prov, final String semantic)
	{
		if (prov == null)
		{
			prov = _semanticProvider;
		}
		if (_facade.getConfigs().get(prov) != null
				&& _facade.getConfigs().get(prov).getChildren() != null
				&& _facade.getConfigs().get(prov).getChildren().get("aodl:semanticStm") != null
				&& _facade.getConfigs().get(prov).getChildren().get("aodl:semanticStm").getChildren() != null
				&& _facade.getConfigs().get(prov).getChildren().get("aodl:semanticStm").getChildren()
						.containsKey(semantic))
		{

			return _facade.getConfigs().get(prov).getChildren().get("aodl:semanticStm").getChildren().get(semantic)
					.getLabel();
		}

		return semantic;
	}

	/**
	 * Read docu.
	 * @param provider the provider
	 * @param configType the config type
	 * @param pos the pos
	 * @param element the element
	 * @param type the type
	 * @param subtype the subtype
	 * @param role the role
	 * @return the string
	 */
	public static final String readDocu(final String provider, final String configType, final String pos,
			final String element,
			final String type, final String subtype, final String role)
	{
		String docu = "";
		String lang = AEConstants.getCurrentLocale().getLanguage();
		// System.out.println("provider " + provider);
		//
		// System.out.println("configType " + configType);
		// System.out.println("pos " + pos);
		//
		// System.out.println("element " + element);
		//
		// System.out.println("type " + type);
		//
		// System.out.println("subtype " + subtype);
		//
		// System.out.println("lang " + lang);
		DatatypeDesc dd = _facade.getConfigs().get(provider);

		if (dd != null)
		{
			if (configType.equals("semanticStm"))
			{
				if (dd.getChildren().get("aodl:semanticStm").getChildren() != null
						&& dd.getChildren().get("aodl:semanticStm").getChildren().get(pos) != null
						&& dd.getChildren().get("aodl:semanticStm").getChildren().get(pos).getDocumentation() != null)
				{
					docu = dd.getChildren().get("aodl:semanticStm").getChildren().get(pos).getDocumentation()
.get(lang);
				}
				// System.out.println("tooltip: " + docu);
				return wrappLines(docu);
			}
			else if (configType.equals("relation"))
			{
				if (pos.equals("context"))
				{
					if (dd.getChildren().get("aodl:relation").getChildren() != null
							&& dd.getChildren().get("aodl:relation").getChildren().get(element) != null
							&& dd.getChildren().get("aodl:relation").getChildren().get(element).getDocumentation() != null)
					{
						docu = dd.getChildren().get("aodl:relation").getChildren().get(element).getDocumentation()
								.get(lang);
					}
					return wrappLines(docu);
				}
				else if (pos.equals("class"))
				{
					if (dd.getChildren().get("aodl:relation").getChildren() != null
							&& dd.getChildren().get("aodl:relation").getChildren().get(element) != null
							&& dd.getChildren().get("aodl:relation").getChildren().get(element).getChildren()
									.containsKey(type)
							&& dd.getChildren().get("aodl:relation").getChildren().get(element).getChildren().get(type)
									.getDocumentation() != null)
					{
						docu = dd.getChildren().get("aodl:relation").getChildren().get(element).getChildren().get(type)
								.getDocumentation().get(lang);
					}
					return wrappLines(docu);
				}
				else if (pos.equals("value"))
				{
					if (dd.getChildren().get("aodl:relation").getChildren() != null
							&& dd.getChildren().get("aodl:relation").getChildren().get(element) != null
							&& dd.getChildren().get("aodl:relation").getChildren().get(element).getChildren()
									.containsKey(type)
							&& dd.getChildren().get("aodl:relation").getChildren().get(element).getChildren()
.get(type)
									.getChildren().containsKey(subtype)
							&& dd.getChildren().get("aodl:relation").getChildren().get(element).getChildren().get(type)
									.getChildren().get(subtype).getDocumentation() != null)
					{
						docu = dd.getChildren().get("aodl:relation").getChildren().get(element).getChildren().get(type)
								.getChildren().get(subtype).getDocumentation().get(lang);
					}
					return wrappLines(docu);
				}
			}
			if (pos.equals("element"))
			{
				if (dd.getChildren() != null && dd.getChildren().get("aodl:" + element) != null
						&& dd.getChildren().get("aodl:" + element).getDocumentation() != null)
				{
					docu = dd.getChildren().get("aodl:" + element).getDocumentation().get(lang);
				}

			}
			else if (pos.equals("type"))
			{
				if (dd.getChildren() != null && dd.getChildren().get("aodl:" + element) != null
						&& dd.getChildren().get("aodl:" + element).getChildren().get(type) != null)
				{
					docu = dd.getChildren().get("aodl:" + element).getChildren().get(type).getDocumentation()
.get(lang);
				}
				else
				{
					docu = null;
				}

			}
			else if (pos.equals("subtype"))
			{
				if (dd.getChildren().get("aodl:" + element).getChildren().get(type) != null
						&& dd.getChildren().get("aodl:" + element).getChildren().get(type).getChildren().get(subtype) != null)
				{
					docu = dd.getChildren().get("aodl:" + element).getChildren().get(type).getChildren().get(subtype)
							.getDocumentation().get(lang);
				}
				else
				{
					docu = null;
				}

			}
			else if (pos.equals("role"))
			{
				if (dd.getChildren().get("aodl:" + element).getChildren().get(type) != null
						&& dd.getChildren().get("aodl:" + element).getChildren().get(type).getChildren().get(subtype) != null
						&& dd.getChildren().get("aodl:" + element).getChildren().get(type).getChildren().get(subtype)
								.getChildren().get(role) != null)
				{
					docu = dd.getChildren().get("aodl:" + element).getChildren().get(type).getChildren().get(subtype)
							.getChildren().get(role).getDocumentation().get(lang);
				}

			}
			else if (pos.equals("context"))
			{
				if (dd.getChildren().get("aodl:relation").getChildren().get(element) != null)
				{
					docu = dd.getChildren().get("aodl:relation").getChildren().get(element).getDocumentation()
							.get(lang);
				}

			}
			else if (pos.equals("class"))
			{
				if (dd.getChildren().get("aodl:relation").getChildren().get(element).getChildren().get(type) != null)
				{
					docu = dd.getChildren().get("aodl:relation").getChildren().get(element).getChildren().get(type)
							.getDocumentation().get(lang);
				}

			}
			else if (pos.equals("_TEXTNODE"))
			{
				if (dd.getChildren().get("aodl:relation").getChildren().get(element).getChildren().get(type)
						.getChildren().get(subtype) != null)
				{
					docu = dd.getChildren().get("aodl:relation").getChildren().get(element).getChildren().get(type)
							.getChildren().get(subtype).getDocumentation().get(lang);

				}

			}

		}
		else
		{
			docu = null;
		}
		return wrappLines(docu);
	}

	private static String wrappLines(String string) {
		if (string != null && string.length() > LINE_LENGTH)
		{
			StringBuilder sb = new StringBuilder(string);

			int i = 0;
			while (i + LINE_LENGTH < sb.length() && (i = sb.lastIndexOf(" ", i + LINE_LENGTH)) != -1) {
			    sb.replace(i, i + 1, "\n");
			}
			return sb.toString();
		}
		return string;
	}

	/**
	 * Read configs.
	 * @param provider the provider
	 * @param configType the config type
	 * @param pos the pos
	 * @param element the element
	 * @param type the type
	 * @param subtype the subtype
	 * @return the string[]
	 */
	public static final String[] readConfigs(final String provider, final String configType, final String pos,
			final String element, final String type, final String subtype)
	{
		String[] values = null;
		ConfigItem[] items = null;

		// System.out.println("_markupProvider " + _markupProvider);
		//
		// System.out.println("configType " + configType);
		// System.out.println("pos " + pos);
		//
		// System.out.println("element " + element);
		//
		// System.out.println("type " + type);
		//
		// System.out.println("subtype " + subtype);

		DatatypeDesc dd = _facade.getConfigs().get(provider);

		if (configType.equals("_semanticProvider"))
		{
			int i = 0;
			values = new String[_facade.getConfigs().size()];
			for (String s : _facade.getConfigs().keySet())
			{
				values[i] = s;
				i++;
			}
			return values;
		}
		else if (configType.equals("relationProvider"))
		{
			int i = 0;
			values = new String[_facade.getConfigs().size()];
			for (String s : _facade.getConfigs().keySet())
			{
				values[i] = s;
				i++;
			}
			return values;
		}
		else if (configType.equals("semanticStm"))
		{
			int i = 0;
			// values = new
			// String[_facade.getConfigs().get(provider).getChildren().get("aodl:semantic").getChildren().size()];
			items = new ConfigItem[_facade.getConfigs().get(provider).getChildren().get("aodl:semanticStm")
					.getChildren().size()];
			for (String s : _facade.getConfigs().get(provider).getChildren().get("aodl:semanticStm").getChildren()
					.keySet())
			{
				items[i] = (ConfigItem) _facade.getConfigs().get(provider).getChildren().get("aodl:semanticStm")
						.getChildren().get(s);
				i++;
			}
		}
		else if (pos.equals("type"))
		{
			int i = 0;
			items = new ConfigItem[dd.getChildren().get("aodl:" + element).getChildren().size()];
			for (String s : dd.getChildren().get("aodl:" + element).getChildren().keySet())
			{
				items[i] = (ConfigItem) dd.getChildren().get("aodl:" + element).getChildren().get(s);
				i++;
			}
		}
		else if (pos.equals("subtype"))
		{
			int i = 0;
			items = new ConfigItem[dd.getChildren().get("aodl:" + element).getChildren().get(type).getChildren().size()];
			for (String s : dd.getChildren().get("aodl:" + element).getChildren().get(type).getChildren().keySet())
			{
				items[i] = (ConfigItem) dd.getChildren().get(element).getChildren().get(type).getChildren().get(s);
				i++;
			}

		}
		else if (pos.equals("role"))
		{
			int i = 0;

			items = new ConfigItem[dd.getChildren().get("aodl:" + element).getChildren().get(type).getChildren()
					.get(subtype).getChildren().size()];
			for (String s : dd.getChildren().get("aodl:" + element).getChildren().get(type).getChildren().get(subtype)
					.getChildren().keySet())
			{
				items[i] = (ConfigItem) dd.getChildren().get("aodl:" + element).getChildren().get(type).getChildren()
						.get(subtype).getChildren().get(s);
				i++;
			}
		}
		else if (pos.equals("context"))
		{
			int i = 0;
			items = new ConfigItem[dd.getChildren().get("aodl:relation").getChildren().size()];
			for (String s : dd.getChildren().get("aodl:relation").getChildren().keySet())
			{
				items[i] = (ConfigItem) dd.getChildren().get("aodl:relation").getChildren().get(s);
				i++;
			}

		}
		else if (pos.equals("class"))
		{
			int i = 0;
			items = new ConfigItem[dd.getChildren().get("aodl:relation").getChildren().get(element).getChildren()
					.size()];
			for (String s : dd.getChildren().get("aodl:relation").getChildren().get(element).getChildren().keySet())
			{
				items[i] = (ConfigItem) dd.getChildren().get("aodl:relation").getChildren().get(element).getChildren()
						.get(s);
				i++;
			}
		}
		else if (pos.equals("_TEXTNODE"))
		{
			int i = 0;
			items = new ConfigItem[dd.getChildren().get("aodl:relation").getChildren().get(element).getChildren()
					.get(type).getChildren().size()];
			for (String s : dd.getChildren().get("aodl:relation").getChildren().get(element).getChildren().get(type)
					.getChildren().keySet())
			{
				items[i] = (ConfigItem) dd.getChildren().get("aodl:relation").getChildren().get(element).getChildren()
						.get(type).getChildren().get(s);
				i++;
			}
		}

		else
		{
			values = new String[1];
			values[0] = "error";
		}

		if (Platform.getPreferencesService().getBoolean(CommonActivator.PLUGIN_ID, "ASPECT_VIEW_MARKUPSORT_BYPRIORITY",
				AEConstants.ASPECT_VIEW_MARKUPSORT_BYPRIORITY, null))
		{
			java.util.Arrays.sort(items);
			values = new String[items.length];
			for (int j = 0; j < items.length; j++)
			{
				values[j] = items[j].getValue();
				// System.out.println("neu sortierte liste " + values[j]);
			}
			return values;
		}
		else
		{
			values = new String[items.length];
			for (int j = 0; j < items.length; j++)
			{
				values[j] = items[j].getValue();
			}
			java.util.Arrays.sort(values);
			return values;
		}
	}
}
