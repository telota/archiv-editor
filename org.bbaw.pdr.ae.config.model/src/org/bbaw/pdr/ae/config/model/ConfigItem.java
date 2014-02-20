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
package org.bbaw.pdr.ae.config.model;


/**
 * ConfigItem for classification configuration. equals <item> in dtdl-XML.
 * @author Christoph Plutte
 */
public class ConfigItem extends ConfigData implements Cloneable
{



	

	/** The _read although ignored. */
	private boolean _readAlthoughIgnored;

	

	/**
	 * Instantiates a new config item.
	 */
	public ConfigItem()
	{
	}

	/**
	 * Instantiates a new config item.
	 * @param label the label
	 * @param value the value
	 */
	public ConfigItem(final String label, final String value)
	{
		setLabel(label);
		setValue(value);
	}

	/**
	 * Compare to.
	 * @param o the o
	 * @return the int
	 */
	public final int compareTo(final ConfigItem o)
	{
		return this.getPriority() - o.getPriority();
	}

	@Override
	public boolean equals(Object object)
	{
		if (object instanceof ConfigItem)
		{
			if (!super.equals(object))
			{
				return false;
			}
			else
			{
				ConfigItem ci = (ConfigItem) object;
				if (this.isIgnore() == ci.isIgnore())
				{
					return true;
				}
			}
		}
		return false;
	}

	

	

	/**
	 * Checks if is read although ignored.
	 * @return true, if is read although ignored
	 */
	public final boolean isReadAlthoughIgnored()
	{
		return _readAlthoughIgnored;
	}

	




	// public int compareTo(AEConfigPresentable o) {
	// if (o != null) return this.getPriority() - o.getPriority();
	// else return -1;
	// }

	

	/**
	 * Sets the read although ignored.
	 * @param readAlthoughIgnored the new read although ignored
	 */
	public final void setReadAlthoughIgnored(final boolean readAlthoughIgnored)
	{
		this._readAlthoughIgnored = readAlthoughIgnored;
	}

	@Override
	public ConfigItem clone()
	{
		ConfigItem clone = (ConfigItem) super.clone();
		clone.setReadAlthoughIgnored(this._readAlthoughIgnored);
		return clone;
	}
}
