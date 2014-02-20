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
package org.bbaw.pdr.ae.export.swt.preview;

import java.util.Collections;
import java.util.HashSet;
import java.util.Vector;

import org.bbaw.pdr.ae.export.logic.PdrObjectsPreviewStructure;
import org.bbaw.pdr.ae.export.logic.StructNode;
import org.bbaw.pdr.ae.export.pluggable.AeExportCoreProvider;
import org.bbaw.pdr.ae.export.swt.IPdrWidgetStructure;
import org.bbaw.pdr.ae.model.Aspect;
import org.bbaw.pdr.ae.model.PdrObject;
import org.bbaw.pdr.ae.model.Person;
import org.bbaw.pdr.ae.model.ReferenceMods;
import org.bbaw.pdr.ae.view.control.PDRObjectsProvider;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ITreeViewerListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeExpansionEvent;
import org.eclipse.jface.wizard.IWizardContainer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Tree;
import org.osgi.framework.FrameworkUtil;

//TODO: doc
/**
 * Important: Must be the only child of parent composite! (due to including
 * TreeColumnLayout)
 * <p><b>Also important:</b> CheckboxTreeViewer is actually not supposed to be
 * subclassed. <i>"It is designed to be instantiated with a pre-existing SWT tree control".</i>
 * Are we gonna get away with this...?</p>
 * @author jhoeper
 * @see {@link CheckboxTreeViewer}, {@link IPdrWidgetStructure}
 *
 */
public class PdrSelectionFilterPreview extends CheckboxTreeViewer 
								implements IPdrWidgetStructure {
	
	/**
	 * This Listener takes care of which tree items are being
	 * selected and in what way a new selection is different from the one
	 * before. 
	 * @author jhoeper
	 * @see PdrPreviewCheckStateListener
	 */
	private class PdrPreviewSelectionListener implements 
				ISelectionChangedListener {
		private StructuredSelection previous, current;
		public PdrPreviewSelectionListener() {
			addSelectionChangedListener(this);
			previous = null;
			current = new StructuredSelection();
		}
		@Override
		public void selectionChanged(SelectionChangedEvent event) {
			StructuredSelection arg = (StructuredSelection)event.getSelection();
			previous = current;
			current = arg; 
		}
		/**
		 * Returns a presumption on whether it seems like a multi-item selection
		 * put together recently has collapsed even more recently due to a user
		 * interaction that probably was performed with the intention of keeping
		 * said selection.
		 * <p>The most likely use case to require this is that a user has 
		 * selected multiple tree items and wants to change the <code>check 
		 * state</code> value of every selected item simultaneously. Any action
		 * leading to an updated item check state will also result in a new
		 * single item selection, so {@link PdrPreviewCheckStateListener}
		 * probably will {@link #rewind()} the selection history in order to
		 * operate on every of the intentionally selected items.</p>
		 * @return <code>true</code> if the current selection seems to have
		 * replaced a larger one
		 */
		public boolean rewindable() {
			return 	(current.size() == 1) &&
					(previous.size() > 1) && 
					(previous.toList().contains(current.getFirstElement()));
		}
		/**
		 * Backs up one step in selection history by restoring the very 
		 * selection that has been replaced the most recently.  
		 */
		public void rewind() {
			setSelection(previous);
		}
	}
	/**
	 * <p><i>Note:
	 * Problems might arise from the current implementation, as it always calls
	 * {@link PdrSelectionFilterPreview#setChecked(Object, boolean)}, which
	 * itself delegates node-wise consistency maintenance to 
	 * {@link PdrSelectionFilterPreview#setChecked(Object, boolean, boolean)},  
	 * from which recursive method {@link PdrSelectionFilterPreview#expandAndSelectDescendants(StructNode, boolean)}
	 * is called to traverse the affected subtree down to its leaves. This actually should
	 * not be done recursively. TODO: Is it possible to rather expand the concerned 
	 * subtree entirely and iterate over the exposed items to archieve consistency?
	 * </i></p>
	 * 
	 * This listener keeps the hierarchic checking logic of the preview tree
	 * up to date. Whenever the check state of an item changes, the check states
	 * of related items are updated according to the following rules:
	 * <ul><li>If the item whose check state was changed is expandable, assign
	 * its check state to all its direct children</li>
	 * <li>If the item is representing an {@link Aspect}, and its check state 
	 * has been set to <code>true</code>, make sure the parent gets checked
	 * as well in order to prevent <code>Aspect</code> items from ending up
	 * being selected for export with no {@link Person} alongside itself</li>
	 * <li>If multiple items are selected at once, have them all being of the
	 * check state that was changed</li></ul>
	 * <p>The last rule is enforced by asking {@link 
	 * PdrPreviewSelectionListener} if the user's interaction that resulted in 
	 * an updated check state might have destroyed a recently set up multi-item 
	 * selection. If so, we assume that the user did actually intend to apply
	 * the outcome of their interaction to each item formerly assembled by the
	 * multi-item selection. To make this implicitly expected result come true,
	 * we {@link PdrPreviewSelectionListener#rewind()} the most recent selection
	 * history and proceed as specified above.</p>
	 * <p>Both <code>PdrPreviewCheckStateListener</code> and {@link 
	 * PdrPreviewSelectionListener} auto-activate themselves as soon as they
	 * are being instantiated by {@link PdrSelectionFilterPreview}</p> 
	 *    
	 * @author jhoeper
	 * @see PdrPreviewSelectionListener
	 */
	private class PdrPreviewCheckStateListener implements ICheckStateListener {
		public PdrPreviewCheckStateListener() {
			addCheckStateListener(this);
		}
	    public void checkStateChanged(CheckStateChangedEvent event) {
	    	
	    	HashSet<StructNode> affects = new HashSet<StructNode>();
	    	
			// if there is more than one item selected, assign the same
			// checked status to all of them
	    	// TODO: more than one?
			if (selectionListener.rewindable()) {
				selectionListener.rewind();
				StructuredSelection selectedItems = 
						(StructuredSelection)getSelection();
				for (Object obj : selectedItems.toArray()) 
					//setChecked(obj, event.getChecked());
					affects.addAll(previewStructure.setSelected(obj, event.getChecked()));
			} else {
				Object obj = event.getElement();
				affects.addAll(previewStructure.setSelected(obj, event.getChecked()));
			}
			
			/*if (affects.size()>0) {
				System.out.println("  new check state: "+event.getChecked());
				System.out.println("  affected relatives: ");
				for (StructNode a : affects)
					System.out.println("   "+a.getLabel()+" -> "+a.isSelected());
			}*/
			update(affects.toArray(new Object[affects.size()]), null);
			// setChecked(obj, event.getChecked());
			
			// TODO: how expensive is this operation and how can it be improved?
			validateSelection();

		}
	    
	}
	/**
	 * Handles changes in expansion state
	 * @author jhoeper
	 *
	 */
	private class PdrPreviewExpansionListener implements ITreeViewerListener {
		public PdrPreviewExpansionListener() {
			addTreeListener(this);
		}
		@Override
		public void treeCollapsed(TreeExpansionEvent event) {
			StructNode node = (StructNode)event.getElement();
			node.setExpanded(false);
		}
		@Override
		public void treeExpanded(TreeExpansionEvent event) {
			StructNode node = (StructNode)event.getElement();
			node.setExpanded(true);
		}
	}
	

	//TODO Maybe extend widget in a way that export to multiple files at once is possible
	/* In order to allow for choosing to export multiple pdr persons in multiple files
	 * at once, this class would then have to inform the core export plugin, which crafts
	 * the PdrObject selection representation used by extending plugins, about
	 * that choice, possibly via the local export provider.  
	 */
	
	// PdrSelectionPreview private fields
	private Tree tree;
	//ITreeContentProvider contentProvider;
	IBaseLabelProvider labelProvider;
	
	PdrPreviewSelectionListener selectionListener;
	PdrPreviewCheckStateListener checkStateListener;
	PdrPreviewExpansionListener expansionListener;
	
	private boolean isvalid;
	private String message;
	
	private WizardPage wizardPage;
	
	private PdrObjectsPreviewStructure previewStructure;

	/**
	 * Creates a PDR Selection Preview on a newly-created tree under the given
	 * parent.
	 * <p>Explicitly intended to be used in a {@link GridLayout} environment.
	 * That means that the <code>parent</code> composite must provide such a 
	 * layout.</p> 
	 * <p>Standard layout data is applied using {@link SWT#FILL} and 
	 * grabbing excess space flags both horizontally and vertically. <br/>
	 * Layout data may however be customized by using {@link #setLayoutData(int, 
	 * int, boolean, boolean, int, int)} if specification row or column span 
	 * is desired.</p> 
	 * @param parent the parent Control with {@link GridLayout}
	 */
	public PdrSelectionFilterPreview(WizardPage page, Composite parent) {
		super(parent, SWT.BORDER | SWT.CHECK | SWT.MULTI | SWT.V_SCROLL);
		this.wizardPage = page;
		tree = getTree();

		PdrSelectionPreviewColumns.createColumns(this, parent);
				
		// workaround to prevent eclipse window builder from crashing
		if (FrameworkUtil.getBundle(getClass()) != null) {
			// TODO: this resets the contents of our central pdr objetcs provider
			// during the pdr objects preview struct constructor. 
			// we might however want to instantiate multiple previews
			// for the same structure instance/pdr objects provider
			// FIXME: den jedesmal zu erzeugen ist ja wohl auch quatsch. dabei wird jedesmal auch
			// der objectsprovider zurückgesetyt und bekommt neue eingaben aus der fassade. man muß sich auch mal entscheiden!
			previewStructure = AeExportCoreProvider.getInstance().createPdrObjectsTree();
			setContentProvider(previewStructure);
			setCheckStateProvider(previewStructure);
		}

		setLayoutData(SWT.FILL, SWT.FILL, true, true);
		isvalid = true;
		setInput(previewStructure.getElements(null));
		// select all
		for (StructNode root : previewStructure.getElements()) {
			//setChecked(root, true, false);
			/*for (StructNode chld : root.getChildren()) {
				collapseToLevel(chld, ALL_LEVELS);
				chld.setExpanded(false);
			}*/
				
		}
		
	}

	/**
	 * Sets a custom layout data for this tree viewer. Calling this method 
	 * does the same as calling {@link Control#setLayoutData(Object)}.
	 * <p>In addition, minimum width and height are set as 150 and 100 pixels
	 * respectively, {@link Tree#setLinesVisible(boolean)} is set <code>true
	 * </code> and an {@link ICheckStateListener} is initialized for triggering
	 * shallow recursive checking of subtrees whenever a expandable node is
	 * checked and checking of the nodes of which at least one child node
	 * has been checked.</p>
	 * <p>Span numbers are optional. {@link #setLayoutData(int, int, boolean, 
	 * boolean)} may be used as well.</p> 
	 * @param horizontalAlignment {@link SWT} style for horizontal alignment 
	 * @param verticalAlignment {@link SWT} style for vertical alignment
	 * @param grabExcessHorizontalSpace boolean space grabbing flag horizontal
	 * @param grabExcessVerticalSpace space grabbing flag vertical
	 * @param horizontalSpan number of columns to be used in gridlayout
	 * @param verticalSpan number of rows to be used in gridlayout
	 */
	public void setLayoutData(int horizontalAlignment, int verticalAlignment, 
			boolean grabExcessHorizontalSpace, boolean grabExcessVerticalSpace, 
			int horizontalSpan, int verticalSpan) {
		GridData gd = new GridData(horizontalAlignment, verticalAlignment, 
				grabExcessHorizontalSpace, grabExcessVerticalSpace, 
				horizontalSpan, verticalSpan);
		gd.minimumHeight = 100;
		gd.minimumWidth = 150;
		tree.setLayoutData(gd);
		tree.setLinesVisible(true);
		tree.setHeaderVisible(true);
		// http://www.vogella.com/articles/EclipseJFaceTable/article.html
		// treecolumnlayout: http://workorhobby.blogspot.de/2012/02/eclipse-jface-virtual-treeviewer.html
		this.tree.pack();
		checkStateListener = new PdrPreviewCheckStateListener();
		selectionListener = new PdrPreviewSelectionListener();
		expansionListener = new PdrPreviewExpansionListener();
	}
	/**
	 * The same thing as {@link #setLayoutData(int, int, boolean, boolean, 
	 * int, int)} except for that no column and row spans are used.
	 * @param horizontalAlignment {@link SWT} style for horizontal alignment 
	 * @param verticalAlignment {@link SWT} style for vertical alignment
	 * @param grabExcessHorizontalSpace boolean space grabbing flag horizontal
	 * @param grabExcessVerticalSpace space grabbing flag vertical
	 * @see #setLayoutData(int, int, boolean, boolean, int, int)
	 */
	public void setLayoutData(int horizontalAlignment, int verticalAlignment, 
			boolean grabExcessHorizontalSpace, boolean grabExcessVerticalSpace) {
		setLayoutData(horizontalAlignment, verticalAlignment, grabExcessHorizontalSpace,
				grabExcessVerticalSpace, 1, 1);
	}
	
	/**
	 * Sets the input data for this tree viewer as an array of {@link StructNode}
	 * elements. Each and every object is being 'checked' by default.
	 * @param input An array of {@link StructNode}s
	 */
	public void setInput(Object[] input) {
		super.setInput(input);
		/*for (Object obj : input) 
			if (obj instanceof StructNode)
				setChecked(obj, ((StructNode)obj).isSelected());*/
	}
	
	/**
	 * Validates current selection.
	 */
	public void validateSelection() {
		// find out if resulting selection is ready to be exported
		// TODO: find more sophisticated requirements for valid selection
		// check if at least one Pdr Person is checked in preview
		// otherwise, preview contents are marked invalid
		PdrObject[] newSelection = getSelectedObjects();
		if (newSelection.length > 0) {
			for (PdrObject pobj : newSelection) 
				if (pobj instanceof Person) {
					setValid();
					return;
				}
			setInvalid("Please select at least one Person.");
		} else
			setInvalid("Please select some content to export.");	
	}
	/**
	 * returns an array of all objects represented by tree items whose check
	 * state is true, no matter if they are persons or aspects
	 * @return
	 */
	public PdrObject[] getSelectedObjects() {
		StructNode[] selection = previewStructure.getSelected();
		Vector<PdrObject> objects = new Vector<PdrObject>();
		// for all selection leaf nodes, walk up towards its root and 
		// collect every PdrObject on the way. 
		for (StructNode leaf : selection) {
			Vector<StructNode> path = new Vector<StructNode>();
			path.add(leaf);
			// collect ancestors
			while (path.lastElement().getParent() != null)
				path.add(path.lastElement().getParent());
			Collections.reverse(path);
			// filter out objects that are not PdrObject instances
			for (StructNode node : path)
				if (node.getContent() instanceof PdrObject)
					objects.add((PdrObject) node.getContent());
		}
		return objects.toArray(new PdrObject[objects.size()]);
	}
	
	/**
	 * Returns wheather the widget's contents are valid in a way that for 
	 * instance export wizards can continue given its state.
	 * @return
	 */
	@Override
	public boolean isValid() {
		System.out.println("Pdr objects previewer is "+(isvalid ? "valid." : "not valid!"));
		return isvalid;
	}

	/**
	 * Mark this widget invalid.
	 * @param message Complementary error message.
	 */
	private void setInvalid(String message) {
		if (isvalid) if (message != null && !message.equals(this.message)){
			System.out.println("Mark previewer as invalid");
			isvalid = false;
			this.message = message;
			IWizardContainer wizardContainer = this.wizardPage.getWizard().getContainer(); 
			wizardContainer.updateButtons();
			wizardContainer.updateMessage();
		}
	}
	
	/**
	 * Mark this widget valid
	 */
	private void setValid() {
		System.out.println("Preview selection is valid.");
		if (!isvalid) {
			isvalid = true;
			this.message = null;
			IWizardContainer wizardContainer = this.wizardPage.getWizard().getContainer(); 
			wizardContainer.updateButtons();
			wizardContainer.updateMessage();
		}
	}
	
	@Override
	public String getMessage() {
		return message;
	}
	
	
	public void updateColumnLabel(String label) {
		PdrSelectionPreviewColumns.setColumnLabel(label);
	}
	
	/**
	 * Jeder Aufruf setzt den komplette Baum zurück. Der muß jedesmal neu gebaut werden. 
	 * Dafür wird jedesmal auch der zentrale {@link PDRObjectsProvider} angerufen, der gar nicht
	 * weiß wie ihm geschieht, weil er ständig von Hinz und Kunz neu instanziiert oder mit neuen
	 * Inputs malträtiert wird. Ob der dann bei so ner Aktion wie hier überhaupt in nem
	 * startklaren Zustand ist, weiß kein Mensch. (Vor allem nervt PdrObjectsPreview rum. Wenigetsnd
	 * bei jedem angelegten Wizard mit ner Preview wird der provider komplett eingestampft)
	 */
	@Deprecated
	public void update(){
		previewStructure.buildTree();
		this.refresh();	
		/*for (StructNode node : previewStructure.getElements())
			setChecked(node, true);*/
	}
	
	/**
	 * Causes the internal representations of those {@link PdrObject}s that
	 * have been retrieved from the database the most recently to update their
	 * sorting criteria and to repopulate. Attempts to backup and reconstruct
	 * the selections made in the GUI. 
	 * @param compId index number of the attribute by which the contents are
	 * desired to be ordered.
	 * @return index number of the applied sorting method plus a flag indicating 
	 * if the contents have been put in ascending or descending order at bit number 3.
	 */
	public int sortBy(int compId){
		// backup current selected nodes, nodes flagged as expanded
		StructNode[] selBck = previewStructure.getSelected();
		StructNode[] expBck = previewStructure.getExpandedNodes();
		// re-sort, re-populate, overwrite structures
		// first, disconnect check state listener
		this.removeCheckStateListener(checkStateListener);
		// then give tree entire rebirth
		int res = previewStructure.sortBy(compId);
		// then update tree viewer input FIXME: what for???
		//setInput(previewStructure.getElements(null));
		
		// ############ RESTORE SELECTION STATE FROM BACKUP ##############
		// from the backed up nodes, filter only those that
		//// represent Aspects, as the entire selection can be
		//// inferred on this basis
		// unfold entire tree
		for (StructNode root : previewStructure.getElements(null))
			expandToLevel(root, ALL_LEVELS);
		// for each of the backed up aspects, retrieve from the
		// re-populated tree structure those nodes that represent
		// the same aspect
		// and try to find out which of them supposedly is to be
		// considered the backup node's alter ego:
		// + if parent node of both the backup node and the candidate
		// represent the same object
		// + if category of root node (group) remains
		System.out.println("Backup selection state to restore:");
		System.out.println("====================================================");
		System.out.println(" "+selBck.length+" nodes");
		/*
		for (StructNode exp : selBck) 
			System.out.println(exp.getLabel() + " ("+exp.getRootCategory()+")");
		System.out.println("----------------------------------------------------");*/
		
		previewStructure.deselectAll();
		
		// FIXME: the objects provider has been modified and now caches its known
		// FIXME: person/orderinghead top layer, which means that even on re-sort and
		// FIXME: thus invoked tree rebuild, the provided person, oh, and aspect objects
		// FIXME: remain the same!
		// FIXME: hence, it should be way easier to find those objects that were 
		// FIXME: the selected and expanded ones in the replaced tree!
		
		// FIXME: right now, sorting does not work, expansion backup is ignored.
		
		Vector<StructNode> aliases = new Vector<StructNode>();
		for (StructNode sel : selBck) {
			// find possible incarnations of vanished tree selection nodes
			Vector<StructNode> candidates = previewStructure.getNodesFor(sel.getContent());
			String cat = sel.getRootCategory();
			// test candidates
			for (StructNode cnd : candidates)  
				// same parent? // same group?
				// then select matching reborn candidate
				if (cnd.isSiblingOf(sel) &&
					(cat == null || cat.equals(cnd.getRootCategory())))  
						//aliases.add(cnd);
						cnd.setSelected(true);
		}
		// select every of the identified Aliases
		// FIXME: this also checks all child references by default!
		// TODO: or does it?
		//for (StructNode als : aliases)
			//setCheckStates(als, true);
			//als.setSelected(true);
		
		// ######## RESTORE EXPANSION STATE FROM BACKUP ###############
		//since the entire tree is still expanded, we simply process every
		// maximum-depth nodes flagged as expanded in the backup structure
		// and try to collapse what can be considered their children in
		// the re-built tree
		System.out.println("Backup expansion state to restore:");
		System.out.println("====================================================");
		System.out.println(" "+expBck.length+" nodes");
		/*for (StructNode exp : expBck) 
			System.out.println(exp.getLabel() + " ("+exp.getRootCategory()+")");
		System.out.println("----------------------------------------------------");*/
		aliases = new Vector<StructNode>();
		for (StructNode exp : expBck) {
			Vector<StructNode> candidates = previewStructure.getNodesFor(exp.getContent());
			String cat = exp.getRootCategory();
			// requirements for a node holding the same content as the query node
			// to be considered identical:
			// + same parent (can be null)
			// + same tree category
			for (StructNode cand : candidates) 
				if (cand.isSiblingOf(exp))
					if (cat == null || cand.getRootCategory().equals(cat))
						aliases.add(cand);
		}
		// restore pre-sorting expansion state by expanding nodes assumingly 
		// identical to backup
		// (inversely, since right now, the entire tree is expanded)
		// restore expansion situation
		for (StructNode als : aliases) {
			System.out.println("Expand node: "+als.getLabel()+" ("+als.getRootCategory()+")");
			als.setExpanded(true);
			collapseToLevel(als, ALL_LEVELS);
			expandToLevel(als, 1);
		}
		// collapse to valid state
		// FIXME: traverse through all nodes, not only roots!
		for (StructNode rtn : previewStructure.getElements(null))
			if (!rtn.isExpanded())
				collapseToLevel(rtn, ALL_LEVELS);
		this.refresh();
		// re-connect check state listener when tree viewer is completely repopulated
		this.addCheckStateListener(checkStateListener);
		return res;
	}
	
/*	@Override
	public boolean setChecked(Object element, boolean state) {
		return setChecked(element, state, true);
		// FIXME: what about superclass?
	}*/
	
	/**
	 * Set the the checkbox state of a given element. Also
	 * checks <strike>parent elements up to root level and</strike> child elements
	 * if necessary. {@link ReferenceMods} elements checkbox states
	 * depend on the aspect elements they belong to. 
	 * @param element
	 * @param state
	 * @return
	 */
	private boolean setChecked(Object element, boolean state, boolean expand) {
		//TODO: this is actually really bad. We handle tree logic in the 
		//representation layer. If anybody finds out, we are totally embarassed.
		//This checkbox state handling/node expanding stuff should be moved
		//to StructNode or at least PreviewStructure entirely.
		//TODO on the other hand, the expanded flag is clearly representation.
		//also, it might be good to keep viewer checkbox states and tree node 
		//selection flags seperated in order to maintain tree consistency even
		//if viewer is inconsistency for some reason.
		//TODO decision
		StructNode node = (StructNode)element;
		StructNode parent = node.getParent();
		//System.out.println("Check node "+ node.label+" > "+ state);
		
		// if reference selection state changes within aspect node, update aspect 
		// selection state, s.t. either every reference within aspect node is 
		// deselected or every reference is selected, equivalent to aspect selection
		// state
		if (node.getContent() instanceof ReferenceMods) 
			if (parent != null && parent.getContent() instanceof Aspect) { 
				setChecked(parent, state, true);
				//System.out.println(" -> Check parent aspect node"+" > "+state);
			}
		boolean ret;
		// if expand flag is passed, update selection flags recursively
		if (expand) 
			ret = expandAndSelectDescendants(node, state);
		else // if not, just this node
			ret = setCheckStates(element, state);
		return ret;
	}
	/**
	 * Calls superclass method {@link CheckboxTreeViewer#setChecked(Object, boolean)},
	 * updates check flag of internal representation, and checks all parent nodes.
	 * @param element
	 * @param state
	 * @return
	 */
	private boolean setCheckStates(Object element, boolean state) {
		if (element instanceof StructNode) {
			StructNode node = ((StructNode)element);
			node.setSelected(state);
			if (state) {
				boolean ok = true;
				// FIXME: references are currently not loaded into the model
				// whenever a reference gets selected, find it in 
				// reference roots and select it there as well (references need to
				// be exported as objects just like persons)
				// Wow, this is really bad practice!!! // FIXME: but currently inactive
				if (node.getContent() instanceof ReferenceMods) 
					// FIXME: doesn't find any matching nodes. 
					for (StructNode ref : previewStructure.getNodesFor(node.getContent())) 
						if (ref.getRootCategory().equals("grouped.reference")) 
							ok &= setCheckStates(ref, state);
				// select all ancestors of this node as well in CheckboxTreeViewer terms
				while (node != null) {
					ok &= super.setChecked(node, state);
					node = node.getParent();
				}
				return ok;
			}
		}
		return super.setChecked(element, state);
	}
	/**
	 * Expands an object in the tree (unless it is an Aspect) and updates its checkbox state
	 * according to the given parameter.<br/>
	 * This is not like an alias for {@link #expandToLevel(Object, int)}, but more like
	 * recursively applying a selection state to nodes in a subtree, and expand them
	 * under certain conditions.
	 * @param node
	 * @param check
	 */
	boolean expandAndSelectDescendants(StructNode node, boolean check){
		// tracking if any changes are made check-wise
		boolean change = setCheckStates(node, check);
		if (isExpandable(node)) {
			// save current expansion state
			boolean expanded = getExpandedState(node);
			// unfold this node -> uncover direct child nodes
			if (!expanded) {
				expandToLevel(node, 1);
				node.setExpanded(true);
			}
			// if desired, mark all children as selected
			// in any case, walk down links to expandable children
			// and continue there
			for (StructNode chld : node.getChildren()) 
				change |= expandAndSelectDescendants(chld, check);
			// if during this call a node got expanded that hadn't been
			// until now, re-collapse this node only at one of these conditions:
			// * node holds Aspect (don't show references)
			// * node wasn't to be selected (don't show boring unselected ancestors)
			// * selection state of subtree didn't change (nothing happened at all)
			if (!expanded && (node.getContent() instanceof Aspect || !check || !change)) {
				node.setExpanded(false);
				collapseToLevel(node, CheckboxTreeViewer.ALL_LEVELS);
			}
		}
		return change;
	}
	
	@Override
	public boolean getExpandedState(Object elementOrTreePath) {
		if (elementOrTreePath instanceof StructNode)
			return ((StructNode)elementOrTreePath).isExpanded();
		return super.getExpandedState(elementOrTreePath);
	}
	
	@Override
	public void performFinish() {
	}
	
}
