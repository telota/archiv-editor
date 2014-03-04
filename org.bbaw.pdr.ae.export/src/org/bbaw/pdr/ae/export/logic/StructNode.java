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
import java.util.HashSet;
import java.util.Vector;

import org.bbaw.pdr.ae.export.swt.preview.PdrSelectionFilterPreview;
import org.bbaw.pdr.ae.model.Aspect;
import org.bbaw.pdr.ae.model.Person;
import org.bbaw.pdr.ae.model.Reference;
import org.bbaw.pdr.ae.model.ReferenceMods;
import org.bbaw.pdr.ae.model.ValidationStm;
import org.bbaw.pdr.ae.model.view.OrderingHead;

public class StructNode implements Comparable<StructNode> {
	private String type;
	private String label;
	private Object content;
	private Vector<StructNode> children;
	private StructNode parent;
	private boolean selected;
	private boolean expanded;
	private PdrObjectsPreviewStructure struct;
	private StructNode root;
	/**
	 * <strike>This constructor calls itself recursively for every object
	 * related to the object passed as content in a way that the
	 * tree structure maintained by {@link PdrObjectsPreviewStructure}
	 * represents as a child node.</strike>
	 * <p>Instantiates a new StructNode as a child of the passed one and
	 * labels it according to the type of the object to represent.</p>
	 * <p>Does <i>not</i> add the new instance to the children of the given
	 * parent node.</p>
	 * @param parent parent {@link StructNode}. This is what the new 
	 * instance will know as its parent and what will determine the root
	 * node it memorizes. Can be null in case a root is to be created. 
	 * @param content The content the new instance will contain. Determines
	 * the values of its {@link #getType()} and {@link #getLabel()} fields.
	 * @param structure the {@link PdrObjectsPreviewStructure} the instance 
	 * will understand itself being a part of. Unless no parent it given,
	 * this can be null. 
	 */
	public StructNode(StructNode parent, Object content, PdrObjectsPreviewStructure structure){
		// TODO: best practice:
		//http://www.eclipsezone.com/eclipse/forums/t53983.html
		this.parent = parent;
		if (this.parent != null) {
			this.root = this.parent.root;
			this.struct = this.parent.struct;
		} else {
			this.root = this;
			this.struct = structure;
		}
		this.children = new Vector<StructNode>();
		this.label="";
		this.type="";
		this.selected=true;
		this.expanded=false;
		if (content instanceof OrderingHead) {
			OrderingHead group = ((OrderingHead)content);
			label = group.getLabel();
			this.content = group;
			type = getStructure().getClassifier() // hopefully won't happen if no struct has been set yet
					+"."+group.getValue().replace(' ', '_').toLowerCase();
			//setChildren(group.getAspects().toArray());
		}  else if (content instanceof Person) {
			Person p = ((Person)content);
			label = p.getDisplayName();
			this.content = p;
			type = "pdr.person";
			//Vector<PdrId> aspectIds = new Vector<PdrId>(p.getAspectIds());
			//Vector<Aspect> aspects = new Vector<Aspect>();
			//for (PdrId pid : aspectIds)
				//aspects.add(Facade.getInstanz().getAspect(pid));
			//setChildren(aspects.toArray());
		} else if (content instanceof Aspect) {
			Aspect a = ((Aspect)content);
			label = a.getDisplayName();
			this.content = a;
			type = "pdr.aspect";
			if (a.getSemanticDim().getSemanticLabelByProvider("PDR").contains("NormName_DE"))
				type+=".normname";
			//setChildren(a.getValidation().getValidationStms().toArray());
		} else if (content instanceof ValidationStm) {
			ValidationStm validation = ((ValidationStm)content);
			Reference reference = validation.getReference();
			//TODO: WTF?
			label = "";
			this.content = reference;
			type = "pdr.reference";
		} else if (content instanceof ReferenceMods) {
			ReferenceMods mods = (ReferenceMods)content;
			this.content = mods;
			label = mods.getDisplayName();
			type = "pdr.reference.mods."+mods.getGenre().getGenre();
		}
/*		System.out.print(" created node "+label+" of type "+type);
		if (parent != null) {
			System.out.println(" with parent "+parent.label);
		} else System.out.println();*/
	}
	
	/**
	 * Creates a new root node in the given {@link PdrObjectsPreviewStructure}. 
	 * Calls {@link #StructNode(StructNode, Object, PdrObjectsPreviewStructure)}
	 * with null as a parent node.
	 * @param struct
	 * @param content
	 */
	public StructNode(Object content, PdrObjectsPreviewStructure struct) {
		this(null, content, struct);
	}
	
	/**
	 * Instantiates new nodes for every object in the given array
	 * and links the results as children of this node.
	 * @see #addChild(Object)
	 */
	public void addChildren(Object[] objects){
		if (objects.length < 1)
			return;
		for (Object obj : objects) 
				addChild(obj);
	}
	/**
	 * Tells whether this node has any children.
	 * @return
	 */
	public boolean hasChildren() {
		return (this.children.size()>0);
	}
	/**
	 * Creates a new node containing the given object and links 
	 * it as a child of this one.
	 * @param obj
	 * @return
	 */
	public StructNode addChild(Object obj){
		StructNode child = getStructure().createNode(this, obj);
		this.children.add(child);
		return child;
	}
	
	/**
	 * Determines if given node is a sibling of this one, i.e.
	 * if both are either orphans or children of node with equivalent content.
	 * @param node candidate node to test
	 * @return true if both parents represent the same object
	 */
	public boolean isSiblingOf(StructNode node) {
		if (this.parent == null) 
			return (node.parent == null);
		else if (node.parent != null) {
			if (this.parent.content instanceof OrderingHead)
				if (node.parent.content instanceof OrderingHead)
					return this.parent.type.equals(node.parent.type);
			return this.parent.content.equals(node.parent.content);
		}
		return false;
	}
	
	/**
	 * Changes selection state of this node. When becoming selected,
	 * ancestors get selected as well, when de-selected, descendants
	 * are updated consistently.
	 * <p>Returns a list of those {@link StructNode}s whose selection
	 * flag is being changed during this operation, i.e. relatives of
	 * this node.</p>
	 * @param state
	 */
	public HashSet<StructNode> setSelected(boolean state) {
		HashSet<StructNode> res = this.selectDescendants(state);
		if (state) {
			// walk up to root and select all nodes on the way
			StructNode anc = this.parent;
			while (anc != null && anc.selected != true) {
				anc.selected = true;
				res.add(anc);
				anc = anc.parent;
			}
		}
		return res;
	}
	
	/**
	 * Updates selection status of this node, maintains selection restrictions
	 * along ancestors and descendants and returns a list of all nodes that get their
	 * selection flag changed during this.
	 * @param state
	 * @return
	 */
	private HashSet<StructNode> selectDescendants(boolean state) {
		//if (state != selected) {
		// affected nodes
		HashSet<StructNode> aff = new HashSet<StructNode>();
		
		if (state != selected) {
			// if selection state changed, register this node as being affected
			selected = state;
			aff.add(this);
			// update child nodes accordingly
			for (StructNode chld : this.children)
				aff.addAll(chld.setSelected(state));
			// consistency maintenance / invariant 
			// node is reference node, parent is aspect node
			if (state == false)
				if (this.content instanceof ReferenceMods && parent.content instanceof Aspect) {
					if (parent.selected)
						aff.add(this.parent);
					parent.selected = false;
				}
		}
		
		return aff;
	}

	/**
	 * Tells if this node is flagged as selected.
	 * @return
	 */
	public boolean isSelected() {
		boolean res = selected;
		// TODO: mögliches feature: angezeigter checkstate ist nicht der tatsächliche, so
		//TODO daß bei deselektieren und dann wieder selektieren eines knotens die vorherige
		//TODO markierungssituation wieder hergestellt würde. problem: laufzeit; oder speicher
		/*StructNode anc = this.parent;
		while (anc != null) {
			res &= anc.selected;
			anc = anc.parent;
		}*/
		return res;
	}
	/**
	 * Updates this node's expansion flag. 
	 * Depending on if it is set to true or false,
	 * either all ancestors or all descendants are updated respectively 
	 * to maintain 
	 * consistency. This attribute may primarily be of concern when this node's tree
	 * is to be rendered and stuff.
	 * <p>Note that 'expanded' means that this node exposes its children,
	 * not only that it is shown itself.</p>
	 * @param state does node show its children or not?
	 * @see PdrSelectionFilterPreview
	 */
	public void setExpanded(boolean state) {
		if (this.expanded != state) {
			if (state) {
				if (this.parent != null)
					this.parent.setExpanded(true);
			} else
				if (this.children.size()>0) {
					for (StructNode chld : this.children)
						chld.setExpanded(false);
				}
			this.expanded = state;
		}
	}
	/**
	 * Returns the current expansion state of this node.
	 * @return true if this node exposes its children, false
	 * if it is collapsed.
	 * @see PdrSelectionFilterPreview
	 */
	public boolean isExpanded() {
		return expanded;
	}
	
	/**
	 * Returns a {@link Vector} of either all selected descendants of this node
	 * that do not have any selected descendants themselves, or only this node
	 * in case it is selected unlike any of its descendants. If it is not 
	 * selected itself, its descendants are ignored entirely 
	 * (since {@link #setSelected(boolean)} attempts to prevent orphan selections).
	 * @return 
	 */
	protected Vector<StructNode> getSelectedDescendants() {
		Vector<StructNode> sel = new Vector<StructNode>();
		if (this.selected) {
			if (this.children.size()>0)
				for (StructNode chld : this.children)
					sel.addAll(chld.getSelectedDescendants());
			if (sel.size()<1)
				sel.add(this);
		}
		return sel;
	}
	/**
	 * Returns a list of all nodes of this subtree that are flagged
	 * as expanded and don't have any expanded children. Apart from that, 
	 * behaves exactly like 
	 * {@link #getSelectedDescendants()}.
	 * @return all expanded nodes of the subtree rooted at this node
	 * which do not know expanded descendants themselves.
	 */
	protected Vector<StructNode> getExpandedDescendants() {
		Vector<StructNode> exp = new Vector<StructNode>();
		if (this.expanded) {
			if (this.children.size()>0)
				for (StructNode chld : this.children)
					exp.addAll(chld.getExpandedDescendants());
			if (exp.size()<1) 
				exp.add(this);
		}
		return exp;
	}
	
	/**
	 * Looks up which category/type the tree containing this node is of.
	 * To do so, this method recursively calls itself on the parent node
	 * until a root is arrived at.
	 * @return String identifier denoting what kind of group this node is part of 
	 */
	public String getRootCategory() {
		if (this.parent != null) {
			if (this.type.startsWith("grouped."))
				if (this.parent.type.startsWith("grouped."))
					return this.parent.getRootCategory()+
						this.type.substring(this.type.lastIndexOf("."));
				else
					return this.parent.getRootCategory()+"."+
							this.type;
			return this.parent.getRootCategory();
		}
		return this.type;
	}
	@Override
	public int compareTo(StructNode o) {
		return this.label.compareTo(o.label);
	}

	public StructNode getParent() {
		return this.parent;
	}
	
	public StructNode getRoot() {
		return this.root;
	}
	/**
	 * Find the {@link PdrObjectsPreviewStructure} this node belongs to by looking up the
	 * corresponding private field at the root node.
	 * @return {@link #getRoot()}.struct
	 */
	public PdrObjectsPreviewStructure getStructure() {
		return getRoot().struct;
	}
	
	public Vector<StructNode> getChildren() {
		return this.children;
	}
	
	public Object getContent() {
		return this.content;
	}
	
	public String getLabel() {
		return this.label;
	}
	
	public void setLabel(String label) {
		this.label = label;
	}
	
	public String getType() {
		return this.type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	/**
	 * <p>Creates a deep copy of this {@link StructNode} attached to the given {@link PdrObjectsPreviewStructure}.
	 * <p>If this is a root node, this causes the modification of the copy's private <i>struct</i>
	 * and <i>root</i> fields as well as recursive copying of all descendants.</p>
	 * 
	 * <p>If it's not, its ascendants will also be copied all the way up to the root, but not the siblings 
	 * of the originally targeted node. If any ascendants appear to be already represented in the target
	 * structure, the copy will be attached to those. 
	 * Calls {@link #interstructAnchor(PdrObjectsPreviewStructure)}.</p>
	 * 
	 * <p>All newly created instances are automatically registered at the target structure, but
	 * none of them will be listed as root nodes at target structure.</p> TODO: why not? won"t hurt.
	 * 
	 * @param structure a {@link PdrObjectsPreviewStructure} of which the node's copy is to be a root of.
	 * @return deep copy of this node, with all its parameters being the same as the original's, 
	 * except for its {@link #getStructure()} and {@link #getRoot()} values (and inferred information). 
	 * Returns <code>null</code> if this node is not a root.
	 */
	public StructNode copyIntoStructure(PdrObjectsPreviewStructure structure) {
		// if not a root, don't even try
		StructNode pn = (this.parent != null) ? interstructAnchor(structure) : null;
		/*// potentially available parent node to attach copy to
		StructNode pn = null;
		// if this is not a root node
		if (this.parent!=null) {
			Vector<StructNode> desc = new Vector<StructNode>();
			desc.add(this.parent);
			// walk up and copy
			while (desc.firstElement() != null) {
				Vector<StructNode> r = structure.getNodesFor(desc.firstElement().content); 
				if (!r.isEmpty()) {
					pn = r.firstElement();
					break;
				}
				desc.add(0, desc.firstElement().parent);
			}
			
			for (StructNode d : desc) {
				
			}
		}*/
		StructNode clone = structure.createNode(pn, this.content);
		// recursively copy child nodes
		for (StructNode child : this.children)
			clone.children.add(child.copyIntoStructure(structure));
		clone.expanded=this.expanded;
		clone.label=this.label;
		//clone.parent = pn;
		//clone.root=this;
		clone.selected=this.selected;
		clone.type=this.type;
		return clone;
	}
	/**
	 * Transfer a path of nested nodes from one structure to another
	 * @param structure
	 * @return
	 */
	private StructNode interstructAnchor(PdrObjectsPreviewStructure structure) {
		Vector<StructNode> to_copy = new Vector<StructNode>();
		StructNode anchor = null;
		StructNode pn = this.parent;
		// unless there is an anchor on the way, walk up to the root and copy whole path
		while (pn != null) {
			Vector<StructNode> pcandidates = structure.getNodesFor(pn.content);
			// if still no anchor in sight, keep walking
			if (pcandidates.isEmpty()) {
				to_copy.add(0, pn);
				pn = pn.parent;
			} else // if potential anchors are availabnle, attach to first one coming up
				anchor = pn;
		}
		// transfer path of unattached ancestors to target structure
		// list of nodes to transfer begins with the one on highest level
		pn = anchor;
		for (StructNode node : to_copy) {
			StructNode copy = structure.createNode(pn, node.content);
			pn = copy;
		}
		return pn;
	}
	
	/**
	 * 
	 * @param comp
	 */
	public void sort(NodeComparator ncomp) {
		Collections.sort(this.children, ncomp);
	}
}
