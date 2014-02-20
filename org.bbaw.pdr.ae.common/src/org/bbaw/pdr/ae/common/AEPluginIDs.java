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
package org.bbaw.pdr.ae.common;

/** Class holds static final strings with ids of important plugin components and parameters.
 * @author plutte
 *
 */
public final class AEPluginIDs
{
	/** id of concurrence head view.*/
	public static final String VIEW_CONCURRENCE_HEAD = "org.bbaw.pdr.ae.view.concurrences.view.ConcurrenceHeadView";
	/** id of identifier browser view.*/
	public static final String VIEW_IDENTIFIERS_BROWSER = "org.bbaw.pdr.ae.view.identifiers.view.BrowserView";
	/** id of identifiers perspective.*/
	public static final String PERSPECTIVE_IDENTIFIERS
	= "org.bbaw.pdr.ae.view.identifiers.view.ExternalIdentiferPerspective";
	/** id of concurrence perspective.*/
	public static final String PERSPECTIVE_CONCURRENCES
	= "org.bbaw.pdr.ae.view.concurrences.view.ConcurrencePerspective";
	/** id of aspects perspective.*/
	public static final String PERSPECTIVE_ASPECTS = "org.bbaw.pdr.ae.base.aspectsPerspective";
	/** id of treeview.*/
	public static final String VIEW_TREEVIEW = "org.bbaw.pdr.ae.view.main.views.Treeview";
	/** id of references view.*/
	public static final String VIEW_REFERENCES = "org.bbaw.pdr.ae.view.main.views.ReferenceCatView";
	/** id of aspects view.*/
	public static final String VIEW_ASPECTS = "org.bbaw.pdr.ae.view.main.views.AspectsView";
	/** source parameter tree - hold the key of the currently selected tree in TreeView.*/
	public static final String SOURCE_PARAMETER_TREE = "org.bbaw.pdr.ae.view.tree";

	/** source parameter tree type - hold the type of the currently selected tree in TreeView.*/
	public static final String SOURCE_PARAMETER_TREE_TYPE
	= "org.bbaw.pdr.ae.view.treeType";
	/** source parameter treeViewer - hold the treeViewer of the currently selected tree in TreeView.*/
	public static final String SOURCE_PARAMETER_TREE_VIEWER
	= "org.bbaw.pdr.ae.view.treeViewer";
	/** sourve parameter user role - holds the hightest role of the current user.*/
	public static final String SOURCE_PARAMETER_USER_ROLE
	= "org.bbaw.pdr.ae.control.userRole";
	/** source parameter user may delete - true if user may delete current object.*/
	public static final String SOURCE_PARAMETER_USER_MAY_DELETE
	= "org.bbaw.pdr.ae.control.userMayDelete";
	/** toggle state.*/
	public static final String TOGGLE_STATE_FILTERONLYUPDATEDOBJECTS
	= "org.bbaw.pdr.ae.view.main.commands.FilterOnlyUpdatedObjects.toggleState";
	/** toggle state.*/
	public static final String TOGGLE_STATE_FILTERONLYNEWOBJECTS
	= "org.bbaw.pdr.ae.view.main.commands.FilterOnlyNewObjects.toggleState";

	/** toggle state.*/
	public static final String TOGGLE_STATE_FILTERONLYINCORRECTOBJECTS
	= "org.bbaw.pdr.ae.view.main.commands.FilterOnlyIncorrectObjects.toggleState";
	/** toggle state.*/
	public static final String TOGGLE_STATE_FILTERONLYPERSONCONCURRENCES
	= "org.bbaw.pdr.ae.view.main.commands.FilterOnlyPersonConcurrences.toggleState";
	/** toggle state.*/
	public static final String TOGGLE_STATE_FILTERONLYASPECTSDIVERGENTMARKUP
	= "org.bbaw.pdr.ae.view.main.commands.FilterOnlyAspectsDivergentMarkup.toggleState";
	/** toggle state.*/
	public static final String TOGGLE_STATE_FILTERONLYPERSONIDENTIFIERS
	= "org.bbaw.pdr.ae.view.main.commands.FilterOnlyPersonIdentifiers.toggleState";
	/** toggle state.*/
	public static final String TOGGLE_STATE_FILTERONLYWITHOUTPNDPERSONS
	= "org.bbaw.pdr.ae.view.main.commands.FilterOnlyWithoutPNDPersons.toggleState";
	/** toggle state.*/
	public static final String TOGGLE_STATE_FILTERONLYWITHOUTLCCNPERSONS
	= "org.bbaw.pdr.ae.view.main.commands.FilterOnlyWithoutLCCNPersons.toggleState";
	/** toggle state.*/
	public static final String TOGGLE_STATE_FILTERONLYWITHOUTVIAFPERSONS
	= "org.bbaw.pdr.ae.view.main.commands.FilterOnlyWithoutVIAFPersons.toggleState";
	/** toggle state.*/
	public static final String TOGGLE_STATE_FILTERONLYWITHOUTICCUPERSONS
	= "org.bbaw.pdr.ae.view.main.commands.FilterOnlyWithoutICCUPersons.toggleState";
	public static final String SOURCE_PARAMETER_AE_ADVANCED_VERSION = "org.bbaw.pdr.ae.aeVersion.advanced";
	public static final String SOURCE_PARAMETER_CAN_SYNCHRONIZE = "org.bbaw.pdr.ae.repository.canSynchronize";

	public static final String MENU_URI_ASPECT_EDITOR = "org.bbaw.pdr.view.main.editor.AspectEditor";

	public static final String EXTENSION_ASPECT_SEMANTIC_TEMPLATE_CONTROLLER = "org.bbaw.pdr.view.control.templates.aspectSemantic.controller";
	public static final String EXTENSION_ASPECT_SEMANTIC_TEMPLATE_CONFIGEDITOR = "org.bbaw.pdr.view.control.templates.aspectSemantic.configEditor";
	public static final String VIEW_IDENTIFIERS_SEARCH = "org.bbaw.pdr.ae.view.identifiers.view.IdentifierSearchView";

	/**
	 * constructor.
	 */
	private AEPluginIDs()
	{

	};

}
