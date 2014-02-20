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
package org.bbaw.pdr.ae.export.logic;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Vector;

import org.bbaw.pdr.ae.common.AEConstants;
import org.bbaw.pdr.ae.common.CommonActivator;
import org.bbaw.pdr.ae.control.comparator.AspectsByCreatorComparator;
import org.bbaw.pdr.ae.control.comparator.AspectsByCronComparator;
import org.bbaw.pdr.ae.control.comparator.AspectsByRecentChangesComparator;
import org.bbaw.pdr.ae.control.comparator.AspectsByReferenceComparator;
import org.bbaw.pdr.ae.control.comparator.AspectsBySemanticComparator;
import org.bbaw.pdr.ae.control.facade.Facade;
import org.bbaw.pdr.ae.export.pluggable.AeExportCoreProvider;
import org.bbaw.pdr.ae.metamodel.PdrId;
import org.bbaw.pdr.ae.model.Aspect;
import org.bbaw.pdr.ae.model.PdrObject;
import org.bbaw.pdr.ae.model.Person;
import org.bbaw.pdr.ae.model.ReferenceMods;
import org.bbaw.pdr.ae.model.ValidationStm;
import org.bbaw.pdr.ae.model.view.OrderingHead;
import org.bbaw.pdr.ae.view.control.PDRObjectsOrderer;
import org.bbaw.pdr.ae.view.control.PDRObjectsProvider;
import org.bbaw.pdr.ae.view.control.orderer.AspectByYearOrderer;
import org.bbaw.pdr.ae.view.control.orderer.AspectsByMarkupOrderer;
import org.bbaw.pdr.ae.view.control.orderer.AspectsByPersonOrderer;
import org.bbaw.pdr.ae.view.control.orderer.AspectsByPlaceOrderer;
import org.bbaw.pdr.ae.view.control.orderer.AspectsByReferenceOrderer;
import org.bbaw.pdr.ae.view.control.orderer.AspectsByRelationOrderer;
import org.bbaw.pdr.ae.view.control.orderer.AspectsBySemanticOrderer;
import org.bbaw.pdr.ae.view.control.orderer.AspectsByUserOrderer;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.ICheckStateProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
/**
 * Represents the set of {@link PdrObject}s currently available at
 * the {@link PDRObjectsProvider} instance known by 
 * {@link AeExportCoreProvider} as a tree.</br>
 * As all required methods are implemented, a wrapper can easily be 
 * written to serve as an {@link ITreeContentProvider}.
 * 
 * @author Jakob Hoeper
 *
 */
public class PdrObjectsPreviewStructure implements ITreeContentProvider, ICheckStateProvider {
	
	private ILog log = AEConstants.ILOGGER;
	
	private PDRObjectsProvider provider;
	private Facade facade;
	private Vector<StructNode> roots;
	private HashMap<Object, Vector<StructNode>> nodes;
	private String groupedBy;
	private int sortedBy;
	/**
	 * <p>Set up an instance of {@link PdrObjectsPreviewStructure} fitted for a tree-ish 
	 * representation of {@link PdrObject} sets and their relations.</p>
	 * <p>In order to actually populate this structure, {@link #buildTree(boolean)} or
	 * {@link #buildTree()} have to be called. This can be done whenever a full rebuilding is
	 * desired. </p>
	 */
	public PdrObjectsPreviewStructure() {
		facade = Facade.getInstanz();
		provider = AeExportCoreProvider.getInstance().getPdrObjectsProvider();
		// TODO: save somehow what to load and how to group
		roots = new Vector<StructNode>();
		nodes = new HashMap<Object, Vector<StructNode>>();
		sortedBy = 0;
		groupedBy = null;
		//System.out.println(getClass().getSimpleName()+" set up.");
	}
	
	public PdrObjectsPreviewStructure(TreeViewer viewer) {
		this();
		viewer.setContentProvider(this);
	}
	
	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		if (oldInput == null || !oldInput.equals(newInput)) {
			//System.out.println(this.getClass().getSimpleName()+" input changed");
		}
	}
	
	/**
	 * Update entire structure to match contents of central export plugin's 
	 * {@link PDRObjectsProvider} instance.</br>
	 * (Calls {@link PDRObjectsProvider#getArrangedAspects()} or 
	 * {@link PDRObjectsProvider#getArrangedAspectsByObjects()}, depending on the top 
	 * argument being false or true).
	 * @param top if true, the {@link PdrObject}s to which the provided aspects
	 * are related as aspect_of are represented on a top-level layer. 
	 * If false, they will be represented directly above the aspects, but underneath
	 * the classification structure the provider is configured with. 
	 */
	public void buildTree(boolean top) {
		// reset class attributes
		groupedBy = groupedBy();
		roots = new Vector<StructNode>();
		nodes = new HashMap<Object, Vector<StructNode>>();
		// translate PDR Objects dependencies into more generic tree structure 
		// retrieve classified Aspects
		//System.out.println("  Retrieve aspects as arranged by PDRObjectsProvider.");
		Vector<OrderingHead> groups = top ? provider.getArrangedAspectsByObjects() 
				: provider.getArrangedAspects();
		
		if (groups != null) 
			// represent persons/objects at highest level
			// this is the current default behaviour
			if (top) {
				//System.out.println("   Initialize PDR Objects preview tree layout.");
				// leave logical layout entirely to treeify(Orderinghead) method 
				for (OrderingHead personalHead : groups) {
					//System.out.println("    Build sub tree for group "+personalHead.getLabel());
					this.roots.add(treeify(personalHead));
				}
			// previously applied layout
			} else // represent persons/objects at lowest level of class. hierarchy 
				for (OrderingHead group : groups) {
				StructNode refs = createNode(null, group);
				// estimate a parent node that will comprise all objects attached to the
				// current OrdernigHead
				StructNode head = computeClassNode(group, null);
				// contribute Aspects and Persons
				HashMap<Person, StructNode> people = new HashMap<Person, StructNode>();
				for (Aspect aspect : group.getAspects()) {
					PdrObject pobj = getPdrObject(aspect.getOwningObjectId());
					StructNode aspectNode=null;
					// insert intermediate layer of Persons
					if (pobj instanceof Person) {
						Person p = (Person)pobj;
						if (people.containsKey(p)) {
							aspectNode = people.get(p).addChild(aspect);
						} else {
							StructNode parent = head.addChild(p);
							people.put(p, parent);
							aspectNode = parent.addChild(aspect);
						}
					} else 
						aspectNode = head.addChild(aspect);
					// insert References
					for (ValidationStm validation : aspect.getValidation().getValidationStms()) {
						ReferenceMods mods = facade.getReference(validation.getReference().getSourceId());
						if (mods!=null) {
							aspectNode.addChild(mods);
							refs.addChild(mods);
						}
					}
				}
			}
		//System.out.println(this.getClass().getSimpleName()+" input changed.");
		//System.out.println(" Number of root nodes: "+roots.size());
		//System.out.println(" Number of nodes total: "+nodes.size());
	}
	
	/**
	 * Builds tree structure expressing the results of
	 * {@link PDRObjectsProvider#getArrangedAspectsByObjects()}, meaning
	 * that the classification the provider has been configured with
	 * using {@link PDRObjectsProvider#setOrderer(PDRObjectsOrderer)}
	 * will be nested within top level classes representing whatever
	 * objects (most likely {@link Person}s) own the comprised {@link Aspect}s. 
	 */
	public void buildTree() {
		this.buildTree(true);
	}
	
	
	/**
	 * Build a tree representing the given {@link OrderingHead} and its underlying aspects structure.
	 * <p>Note: nested layers of OrderingHeads are ignored. Only OrderingHeads directly beneath the
	 * given head are handled and henceforth expected to contain only aspects as children.
	 * </p>
	 * <p>This method pretty much expects {@link OrderingHead} root nodes as input, preferably those 
	 * whose {@link OrderingHead#getValue()} fields are set to a {@link Person}'s {@link PdrId}.
	 * An input like that is provided by {@link PDRObjectsProvider#getArrangedAspectsByObjects()}.
	 * </p>
	 * @param head
	 * @return
	 */
	private StructNode treeify(OrderingHead head) {
		// try to find Person object represented by top level node
		Object rootContent = head;
		PdrId id = new PdrId(head.getValue());
		if (id.isValid()) 
			rootContent = Facade.getInstanz().getPerson(id);
		if (rootContent == null)
			rootContent = head;
		// encapsulate top level node in internal tree node
		StructNode root = this.createNode(null, rootContent);
		// assume exactly one intermediate layer of ordering head nodes between
		// top level and aspects level. Nodes on this layer can be expanded.
		//System.out.println("     Traverse subcategories:");
		for (OrderingHead cat : head.getSubCategories()) {
			//System.out.println("      "+cat.getLabel());
			// place tree node container for classification ordering heads 
			// according to depth indicated by '::' delimiters in ordering head value fields
			StructNode catNode = this.computeClassNode(cat, root);
			// attach aspects to ordering head nodes, references to aspect nodes
			//System.out.println("       append aspects and references");
			for (Aspect aspect : cat.getAspects()) {
				StructNode aspectNode = catNode.addChild(aspect);
				for (ValidationStm validation : aspect.getValidation().getValidationStms()) {
					ReferenceMods mods = facade.getReference(validation.getReference().getSourceId());
					if (mods!=null)
						aspectNode.addChild(mods);
				}				
			}
		}
		// TODO: or whatever PdrObject this ordered head is representing...
		root.setType(root.getType()+"."+head.getValue().replace(' ', '_').toLowerCase());
		return root;
	}
	
	/**
	 * <p>Identifies the {@link StructNode} representing an {@link OrderingHead} within the
	 * specified subtree. A node is considered an ordering head's counterpart if
	 * {@link StructNode#getType()} of the former matches the category identified by 
	 * {@link OrderingHead#getValue()} of
	 * the latter. An ordering head value field might list multiple categories, using
	 * the delimiter '::', in which case the ordering head will be represented by
	 * multiple nodes, each one standing for a single category and being a child of the previous one.</br>
	 * If no matching node can be identified for an ordering head, it will be created at 
	 * the appropriate position.</p>
	 * <p><b>Call only for nodes representing {@link OrderingHead}s.</b></p>
	 * @param group {@link OrderingHead} object that we desire to map into this
	 * {@link PdrObjectsPreviewStructure}.
	 * @param parent Node amongst whose descendants the given ordering head is to be
	 * placed. If null, iteration starts at root level.
	 * @return newly created descendant of the given node, so that
	 * the subclass structure stored in the available {@link OrderingHead#getValue()}
	 * is properly expressed  
	 */
	private StructNode computeClassNode(OrderingHead group, StructNode parent) {
		StructNode node = parent;
		//System.out.println("______________\n"+group.getValue()+" - "+group.getLabel());
		// consider segments of ordering head value string delimited by '::' being nested aspects classes
		String[] keys = group.getValue().split("::");
		// semantic labels
		Vector<String> semPath = new Vector<String>();
		String labelProvider = null;
		// TODO: wahrscheinlich auch fuer place u.ae.
		/*if (this.getClassifier().endsWith(".semantic"))
			labelProvider = AeExportCoreProvider.PRIMARY_SEMANTIC_PROVIDER;
		else */if (this.getClassifier().endsWith(".markup"))
			labelProvider = AeExportCoreProvider.PRIMARY_TAGGING_PROVIDER;
		// determine on which level iteration starts
		Vector<StructNode> candidates = (parent != null) ? parent.getChildren() : this.roots;
		// along the path specified by the given markup configuration, stay
		// on the matching branch as long as possible before creating 
		// required sub nodes
		String type = "";
		StructNode next;
		for (String key : keys) {
			semPath.add(key);
			type += (type.length()>0 ? "." : "") + key.toLowerCase(); //TODO replace special chars
			next = null;
			// see if we can stay on branch..
			for (StructNode cand : candidates)
				if (cand.getType().contains(type))
					next = cand;
			// in case we lost branch:
			if (next == null) {
				// as in 'category', not related to feline mammals in any way..
				OrderingHead catHead = new OrderingHead(type);
				if (labelProvider != null)
					catHead.setLabel(
						AeExportCoreProvider.getAnnotationLabel(semPath, labelProvider));
				else
					catHead.setLabel(group.getLabel());
				if (node == null) {
					next = createNode(null, catHead);
					if (parent==null) this.roots.add(next);
				} else 
					next = node.addChild(catHead);
			}
			// walk down tree
			node = next;
			candidates = node.getChildren();			
		}
		//System.out.println(" Estimated category node: "+node.getLabel()+", "+node.getRootCategory());
		if (node == null)
			log.log(new Status(IStatus.WARNING, CommonActivator.PLUGIN_ID, 
			"COMPUTATION OF CLASS NODE FAILED FOR "+group.getLabel()+
					" under parent "+parent.getLabel()));
		// FIXME: make sure that when returning, the deepest nested node is the passed group instance!
		// FIXME: otherwise, making the PDRObjectsProvider keep its OH instances on comparator update
		// FIXME: is useless for us. 
		// FIXME: we need the very instance passed to here as group parameter to be able to identify known
		// FIXME: OH objects/aspects on re-sort
		return node;
	}
	
	
	/**
	 * Instantiates a new {@link StructNode} instance which
	 * references the StructNode passed as the parent parameter
	 * as its parent node and
	 * represents the passed Object.<br>
	 * @param parent
	 * @param content
	 * @return
	 */
	public StructNode createNode(StructNode parent, Object content) {
		StructNode node = new StructNode(parent, content, this);
		// register node
		// FIXME: since in the new layout, ordering heads appear multiple times
		// FIXME: at different positions, rather register them under their
		// FIXME: getRootCategory()!
		if (!nodes.containsKey(content))
			nodes.put(content, new Vector<StructNode>());
		nodes.get(content).add(node);
		return node;
	}
	
	public StructNode createRoot(Object content) {
		StructNode node = new StructNode(content, this);
		// register node
		// FIXME: since in the new layout, ordering heads appear multiple times
		// FIXME: at different positions, rather register them under their
		// FIXME: getRootCategory()!
		if (!nodes.containsKey(content))
			nodes.put(content, new Vector<StructNode>());
		nodes.get(content).add(node);
		roots.add(node);
		return node;	
	}
	
	/** 
	 * <p>Returns a string identifier for the condition at which
	 * aspects of the export plugin's current {@link PDRObjectsProvider} 
	 * are put
	 * together into groups. Calls {@link #groupedBy()} to do this.
	 * Might be out of date?</p>
	 * <p>The returning identifier corresponds to the currently active
	 * {@link PDRObjectsProvider#getOrderer()}, which might be one of
	 * {@link AspectsByMarkupOrderer}, {@link AspectsByPersonOrderer},
	 * {@link AspectsByPlaceOrderer}, {@link AspectsByReferenceOrderer},
	 * {@link AspectsByRelationOrderer}, {@link AspectsBySemanticOrderer},
	 * {@link AspectsByUserOrderer}, or any other class implementing
	 * {@link PDRObjectsOrderer}. 
	 * @return String identifier looking like <code>grouped.[condition]</code>
	 * @see {@link #groupedBy()}, {@link AeExportCoreProvider}
	 */
	public String getClassifier() {
		if (this.groupedBy == null)
			this.groupedBy = groupedBy();
		return this.groupedBy;
	}
	/**
	 * Detects criteria that causes Aspects to be grouped together
	 * in the way it can be seen in the return set of 
	 * {@link PDRObjectsProvider#getArrangedAspects()} or
	 * {@link PDRObjectsProvider#getArrangedAspectsByObjects()}. </br>
	 * In other words, name the ObjectProvider's current
	 * {@link PDRObjectsOrderer} implementation.
	 * @return String identifier for the currently active 
	 * {@link PDRObjectsOrderer} implementation in our 
	 * PDRObjectsProvider. Possible
	 * return values are:
	 * <ul><li>grouped.person</li>
	 * <li>grouped.year</li>
	 * <li>grouped.markup</li>
	 * <li>grouped.place</li>
	 * <li>grouped.reference</li>
	 * <li>grouped.relation</li>
	 * <li>grouped.semantic</li>
	 * <li>grouped.user</li></ul>
	 */
	public String groupedBy() {
		PDRObjectsOrderer agg = provider.getOrderer();
		String res = "grouped.";
		if (agg == null)
			return res+"none";
	    if (agg instanceof AspectsByPersonOrderer)
	    	return res+"person";
	    if (agg instanceof AspectByYearOrderer)
	    	return res+"year";
	    if (agg instanceof AspectsByMarkupOrderer)
	    	return res+"markup";
	    if (agg instanceof AspectsByPlaceOrderer)
	    	return res+"place";
	    if (agg instanceof AspectsByReferenceOrderer)
	    	return res+"reference";
	    if (agg instanceof AspectsByRelationOrderer)
	    	return res+"relation";
	    if (agg instanceof AspectsBySemanticOrderer)
	    	return res+"semantic";
	    if (agg instanceof AspectsByUserOrderer)
	    	return res+"user";
	    return "";
	}
	/**
	 * Return root-level nodes.
	 * @return
	 */
	@Override
	public StructNode[] getElements(Object obj) {
		return roots.toArray(new StructNode[roots.size()]);
	}
	/**
	 * Return root-level nodes.
	 * @return
	 */
	public Vector<StructNode> getElements() {
		return roots;
	}	
	/**
	 * Returns all children of the node being passed as parentElement, 
	 * assuming it is a {@link StructNode} instance. 
	 * @param parentElement a {@link StructNode} object
	 * @return Array of StructNode
	 */
	public StructNode[] getChildren(Object parentElement) {
		if (parentElement instanceof StructNode) {
			Vector<StructNode> res = ((StructNode)parentElement).getChildren();
			if (res != null) 
				return res.toArray(new StructNode[res.size()]);
			return null;
		}
		return null;
	}
	
	/**
	 * Returns the parent of a {@link StructNode}
	 * @param element StructNode
	 * @return StructNode  which references the passed entity as its child.
	 */
	public StructNode getParent(Object element) {
		if (element instanceof StructNode)
			return ((StructNode)element).getParent();
		return null;
	}
	public boolean hasChildren(Object element) {
		if (element instanceof StructNode)
			return ((StructNode)element).hasChildren();
		return false;
	}

	/**
	 * Returns a {@link Vector} with those nodes that are registered
	 * as containing the given object.
	 * @param content an Object to find nodes for, e.g. a {@link PdrObject} instance
	 * @return Vector of nodes, or empty Vector if object is unknown
	 */
	public Vector<StructNode> getNodesFor(Object obj) {
		Vector<StructNode> matches = new Vector<StructNode>();
		if (obj instanceof OrderingHead) {
			// FIXME: since in the new layout, orderingheads appear multiple times
			// FIXME: at different positions, rather register them under their
			// FIXME: getRootCategory()!			
			StructNode dummy = new StructNode(null, obj, this);
			for (Map.Entry<Object, Vector<StructNode>> chapter : nodes.entrySet())
				if (chapter.getKey() instanceof OrderingHead)
					for (StructNode node : chapter.getValue())
						if (node.getType().equals(dummy.getType()))
							matches.add(node);
		}
		if (nodes.containsKey(obj))
			matches.addAll(nodes.get(obj));
		return matches;
	}
	/**
	 * 
	 * @param id
	 * @return
	 */
	public PdrObject getPdrObject(PdrId id){
		//TODO: makes no sense. Change it into a lookup function for the pdr objects
		//stored in this structure instead.
		PdrObject obj;
		obj = Facade.getInstanz().getPdrObject(id);
		return obj;
	}
	/**
	 * returns the object stored by the given node.
	 */
	public Object getContent(Object node){
		if (node instanceof StructNode)
			return ((StructNode)node).getContent();
		return null;
	}
	/**
	 * Updates the sorting criteria of this structure, equips the responsible
	 * {@link PDRObjectsProvider} with the corresponding {@link Aspect}-based 
	 * {@link Comparator} and re-builds the internal structure tree based on
	 * the updated contents of the provider.
	 * @param compId
	 * @return
	 */
	public int sortBy(int compId) {
		//it is assumed that compId <= 5 anyway. See caller
		compId = compId & 7;
		Comparator<Aspect> comp = null;
		boolean asc;
		if ((sortedBy & 7) != compId) {
			sortedBy = compId; // overwrite sorting criterion on change
		} else // if same base comparator:
			sortedBy ^= 8; // keep sorting, but switch ascending flag
		asc = ((sortedBy >> 3) == 0); // descending order only if 8-bit is set	
		switch (sortedBy & 7) {
			case 0: Collections.sort(this.roots, asc ? null : Collections.reverseOrder()); break;
			case 1: comp = new AspectsByCronComparator(asc); break;
			case 2: comp = new AspectsBySemanticComparator(
					AeExportCoreProvider.PRIMARY_SEMANTIC_PROVIDER, asc);break;
			case 3: comp = new AspectsByRecentChangesComparator(asc);break;
			case 4: comp = new AspectsByReferenceComparator(asc);break;
			case 5: comp = new AspectsByCreatorComparator(asc);break;
		}
		log.log(new Status(IStatus.INFO, CommonActivator.PLUGIN_ID, 
				"Sorting criteria: "+sortedBy+", ascending: "+asc));
		if (comp != null) {
			provider.setComparator(comp);
			buildTree();
			log.log(new Status(IStatus.INFO, CommonActivator.PLUGIN_ID, 
					" > "+comp.getClass().getName()+"\n"+
					(asc ? "ascending" : "descending")));
		}
		return sortedBy;
	}

	
	/**
	 * If a {@link StructNode} instance is given, its selection flag will be 
	 * overwritten with the given boolean value by calling {@link StructNode#setSelected(boolean)},
	 * which will take care of selection state consistency within the affected subtree.
	 * <p>
	 * <strike>If the node's selection flag status is actually being changed by this call, the
	 * return value will be true, if not, false.</strike>
	 * </p>
	 * Returns a list of those {@link StructNode} instances whose selection flags are being
	 * changed during the procedure. 
	 * @param obj {@link StructNode} object
	 * @param state
	 * @return true if operation has any effect
	 * @see StructNode#setSelected(boolean)
	 */
	public HashSet<StructNode> setSelected(Object obj, boolean state) {
		HashSet<StructNode> res;
		if (obj instanceof StructNode) {
			StructNode node = (StructNode)obj;
			res = node.setSelected(state);
			//System.out.println(" changed check state of "+node.getLabel());
		} else
			res = new HashSet<StructNode>();
		return res;
	}
	
	/**
	 * Sets selection flags of every single node to false.
	 */
	public void deselectAll() {
		for (StructNode root : this.roots)
			root.setSelected(false);
	}
	
	/**
	 * Populates an Array of those {@link StructNode} objects that are:
	 * <ol><li>flagged as selected</li>
	 * <li>descendants of selected nodes only</li>
	 * <li>no ancestors of any selected nodes themselves</li>
	 * </ol> 
	 * If none of the root nodes are
	 * selected, the result will be an empty Array.
	 * @return {@link StructNode}[]
	 */
	public StructNode[] getSelected() {
		Vector<StructNode> selection = new Vector<StructNode>();
		for (StructNode root : roots)
			selection.addAll(root.getSelectedDescendants());
		return selection.toArray(new StructNode[selection.size()]);
	}
	
	
	/**
	 * Returns all expanded paths, represented by those nodes that are expanded
	 * themselves, but don't know any further expanded ancestors.   
	 * @return
	 */
	public StructNode[] getExpandedNodes() {
		Vector<StructNode> expansion = new Vector<StructNode>();
		for (StructNode root : roots)
			expansion.addAll(root.getExpandedDescendants());
		return expansion.toArray(new StructNode[expansion.size()]);
	}
	
	public void expand(StructNode node) {
		node.setExpanded(true);
	}
	
	public void collapse(StructNode node) {
		node.setExpanded(false);
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
	}


	/**
	 * Generates a copy of the structure's contents
	 * that will only contain those objects that are currently marked as 
	 * selected in this {@link PdrObjectsPreviewStructure}.
	 * @return List of {@link OrderingHead}s that hold the ids of {@link Person} objects
	 * and whose children, which are OrderingHead instances as well, represent groups of
	 * {@link Aspect}s and the {@link ReferenceMods} validations involved. 
	 */
	public Vector<OrderingHead> getSelectionHeads() {
		Vector<OrderingHead> res = new Vector<OrderingHead>();
		// create ordering heads for all person objects represented on root level
		log.log(new Status(IStatus.INFO, CommonActivator.PLUGIN_ID, 
				"BUILD OrderingHead CONTAINERS FOR EXPORT CONTENTS"));
		for (StructNode root : roots) {
			Person p = (Person)root.getContent();
			OrderingHead head = new OrderingHead(p.getPdrId().toString());
			// attach lower levels to new head instance
			for (StructNode cat : root.getChildren())
				for (OrderingHead node : collapseSubCategories(cat))
					head.addSubCategory(node);
			// return ordering head only if it is not empty
			if (head.hasSubCategories())
				res.add(head);
		}
		return res;
	}
	
	/**
	 * When given a {@link StructNode} whose content is an {@link OrderingHead}, return a list
	 * of OrderingHeads that represent any nested categories that might unfold under said node. 
	 */
	private Vector<OrderingHead> collapseSubCategories(StructNode catNode) {
		Vector<OrderingHead> res = new Vector<OrderingHead>();
		if (!catNode.isSelected())
			return res;
		String value = catNode.getRootCategory();
		//System.out.println(" OrderingHead contains: "+value);
		OrderingHead head = new OrderingHead(value);
		head.setLabel(catNode.getLabel());
		// test for any sub categories amongst children
		for (StructNode chld : catNode.getChildren()) 
			if (chld.isSelected())
				if (chld.getContent() instanceof OrderingHead) 
					res.addAll(collapseSubCategories(chld));
				else if (chld.getContent() instanceof Aspect) {
					head.addAspect((Aspect)chld.getContent());
					for (StructNode refNode : chld.getChildren())
						if (refNode.getContent() instanceof ReferenceMods)
							head.addReference((ReferenceMods)refNode.getContent());
			}
		// if no sub categories are expected under this node, return node itself
		if (res.size()<1)
			res.add(head);
		return res;
	}
	
	/**
	 * Returns a new {@link PdrObjectsPreviewStructure} instance, containing
	 * copies of those {@link StructNode} trees which represent or of which descendants
	 *  represent {@link PdrObject} referenced to by the given selection.
	 * @param selection an array of {@link PdrObject} instances
	 * @return
	 */
	public PdrObjectsPreviewStructure subSet(PdrObject[] selection) {
		PdrObjectsPreviewStructure subset = new PdrObjectsPreviewStructure();
		subset.groupedBy = this.groupedBy;
		subset.sortedBy = this.sortedBy;
		LinkedHashSet<StructNode> rootNodes = new LinkedHashSet<StructNode>();
		// process selection of pdr objects
		for (PdrObject pdrObj : selection) {
			Vector<StructNode> there = subset.getNodesFor(pdrObj);
			if (there.isEmpty()) {
				Vector<StructNode> here = getNodesFor(pdrObj);
				// find nodes containing specified pdr object
				for (StructNode repr : here) {
					StructNode copy = repr.copyIntoStructure(subset);
					/*if (!subset.nodes.containsKey(pdrObj))
						subset.nodes.put(pdrObj, new Vector<StructNode>());
					subset.nodes.get(pdrObj).add(copy);*/
					rootNodes.add(copy.getRoot());
				}
			}
		}
		subset.roots = new Vector<StructNode>(rootNodes);
		return subset;
	}

	@Override
	public boolean isChecked(Object element) {
		if (element instanceof StructNode) {
			StructNode node  = (StructNode)element;
			return node.isSelected();
		}
		return false;
	}

	@Override
	public boolean isGrayed(Object element) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
