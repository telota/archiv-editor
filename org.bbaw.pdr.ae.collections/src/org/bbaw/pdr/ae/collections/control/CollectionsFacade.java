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

import java.util.HashMap;

import org.bbaw.pdr.ae.collections.model.PDRCollection;
import org.bbaw.pdr.ae.model.PdrObject;

/** singleton facade that holds loaded collections.
 * @author Christoph Plutte
 *
 */
public final class CollectionsFacade
{

	/**
	 * constructor.
	 */
	private CollectionsFacade()
	{
	}

	// Singleton for standalone RCP
		/** static volatile collectionsFacade.*/
		private static volatile CollectionsFacade signletonCollectionFacade;
		/** singleton access to collections facade.
		 * @return singleton instance of collectionfacade.
		 */
		public static CollectionsFacade getInstance()
		{
			if (signletonCollectionFacade == null)
			{
				synchronized (CollectionsFacade.class)
				{
					if (signletonCollectionFacade == null)
					{
						signletonCollectionFacade = new CollectionsFacade();
					}
				}
			}
			return signletonCollectionFacade;
		}

		// Singleton for RAP
//		public static Facade getInstanz() {
	//
	//
//	        return (Facade) SessionSingletonBase.getInstance(Facade.class);
//	    }
		/** map of loaded collections.*/
		private HashMap<String, PDRCollection> _loadedCollections = null;
		/** clipboard.*/
		private PDRCollection _clipboard = null;

		/** get clipboard.
		 * @return clipboard.
		 */
		public PDRCollection getClipboard()
		{
			return _clipboard;
		}

		/** set clipboard.
		 * @param clipboard clipboad.
		 */
		public void setClipboard(final PDRCollection clipboard)
		{
			this._clipboard = clipboard;
		}
		/** add object to clipboard.
		 * @param object to add to clipboard.
		 */
		public void addToClipboard(final PdrObject object)
		{
			if (this._clipboard == null)
			{
				this._clipboard = new PDRCollection();
				this._clipboard.setName("clipboard");
			}
			this._clipboard.addPDRObject(object);

		}

		/** remove object from clipboard.
		 * @param o to be removed.
		 */
		public void removeFromclipboard(final PdrObject o)
		{
			if (this._clipboard != null)
			{
				this._clipboard.remove(o);
			}
		}

		/** get loaded collections.
		 * @return loaded collections.
		 */
		public HashMap<String, PDRCollection> getLoadedCollections()
		{
			if (_loadedCollections == null)
			{
				_loadedCollections = new HashMap<String, PDRCollection>();
			}
			return _loadedCollections;
		}

		/** set loaded collections.
		 * @param loadedCollections loaded collections
		 */
		public void setLoadedCollections(final HashMap<String, PDRCollection> loadedCollections)
		{
			this._loadedCollections = loadedCollections;
		}

}
