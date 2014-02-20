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
package org.bbaw.pdr.ae.model.view;

import java.util.Vector;

import org.bbaw.pdr.ae.metamodel.IAEPresentable;
import org.bbaw.pdr.ae.model.Aspect;
import org.bbaw.pdr.ae.model.PdrObject;
import org.bbaw.pdr.ae.model.ReferenceMods;

/**
 * The Class OrderingHead.
 * @author Christoph Plutte
 */
public class OrderingHead implements IAEPresentable
{

	/** The _label. */
	private String _label;

	/** The _value. */
	private String _value;

	/** The _priority. */
	private int _priority;

	/** The _image string. */
	private String _imageString;

	/** The _aspects. */
	private Vector<Aspect> _aspects = new Vector<Aspect>();

	/** The _references. */
	private Vector<ReferenceMods> _references = new Vector<ReferenceMods>();
	
	// TODO: make orderinghead either contain aspects or categories?
	// TODO: if sub-categories are known, make getAspects return aspects of those instead of direct ones?
	/** List of sub categories this odering head might have its comprised aspects assigned to **/
	private Vector<OrderingHead> _subOrderings;

	private boolean _sorted;

	/**
	 * Instantiates a new head.
	 */
	public OrderingHead() {
	}
	
	/**
	 * Instantiates a new head, ready to comprise all aspects considered
	 * part of the classifier denoted by value.<br/>
	 * To append an {@link Aspect} to this head, it is passed as the 
	 * parameter of {@link #addAspect(Aspect)}.
	 * @param value
	 */
	public OrderingHead(String value) {
		this._value = value;
		this._label = value.length() > 17 ? value.substring(0, 15)+"..." : value;
	}
	
	/**
	 * Adds an {@link Aspect} to this head. If the aspect is already 
	 * assigned to this head, the method will do nothing. 
	 * @param aspect {@link Aspect} to be assigned to this head
	 */
	public final void addAspect(final Aspect aspect)
	{
		if (!_aspects.contains(aspect))
			_aspects.add(aspect);
	}
	
	public final void addReference(final ReferenceMods reference) {
		if (!_references.contains(reference))
			_references.add(reference);
	}
	
	public final void addSubCategory(final OrderingHead cat) {
		if (_subOrderings == null)
			_subOrderings = new Vector<OrderingHead>();
		if (!_subOrderings.contains(cat))
			_subOrderings.add(cat);
	}

	/**
	 * Attaches all aspects in the given {@link Vector} to this head.
	 * @param aspects
	 */
	public final void addAll(final Vector<Aspect> aspects)
	{
		for (Aspect a : aspects)
			this.addAspect(a);
	}	
	
	@Override
	public final int compareTo(final IAEPresentable o)
	{
		return this.getLabel().compareToIgnoreCase(o.getLabel());
	}

	/**
	 * Returns whether a given object is amongst this {@link OrderingHead}'s children, no
	 * matter if its an {@link Aspect} or another OrderingHead instance. If it is neither,
	 * false is returned. To determine the parenthood towards an OrderingHead argument, 
	 * its {@link #getValue()} field is compared to the ones of this head's sub categories. 
	 * @param obj
	 * @return
	 */
	public boolean contains(Object obj) {
		if (obj instanceof Aspect)
			return this._aspects.contains(obj);
		else
			if (this._subOrderings != null)
				if (this._subOrderings.contains(obj))
					return true;
				else
					for (OrderingHead subCat : this._subOrderings)
						if (subCat._value.equals(this._value))
							return true;
		return false;
	}
	
	/**
	 * Returns a new OrderingHead instance with the exact same contents of this one.
	 * @return
	 */
	public OrderingHead copy() {
		OrderingHead clone = new OrderingHead();
		clone._label = this._label;
		clone._value = this._value;
		clone._priority = this._priority;
		clone._imageString = this._imageString;
		clone._aspects = (this._aspects != null) ? new Vector<Aspect>(this._aspects) : null;
		clone._references = (this._references != null) ? new Vector<ReferenceMods>(this._references) : null;
		clone._subOrderings = (this._subOrderings != null) ? new Vector<OrderingHead>(this._subOrderings) : null;
		clone._sorted = this._sorted;
		return clone;
	}
	
	/**
	 * Remove all child elements this OrderingHead is currently linked to 
	 * ({@link Aspect}s, {@link ReferenceMods}, {@link OrderingHead} sub-categories),
	 * i.e. reset contained objects.
	 */
	public void clear() {
		this._aspects.removeAllElements();
		this._references.removeAllElements();
		this._subOrderings = null;
		this._sorted = true;
	}
	
	/**
	 * Gets the aspects.
	 * @return the aspects
	 */
	public final Vector<Aspect> getAspects()
	{
		if (_aspects == null)
		{
			_aspects = new Vector<Aspect>();
		}
		return _aspects;
	}
	
	/**
	 * Returns a list of sub-categories that have been appended using 
	 * {@link #addSubCategory(OrderingHead)}, or null, if none are known.
	 * @return
	 */
	public final Vector<OrderingHead> getSubCategories() {
		return _subOrderings;
	}
	
	/**
	 * Returns true if this {@link OrderingHead} is referencing any OrderingHeads as its children. 
	 * @return
	 */
	public boolean hasSubCategories() {
		return (_subOrderings != null && _subOrderings.size()>0);
	}
	
	/**
	 * If one or more of this head's sub categories appear to be copies of the
	 * argument (indicated by equivalent value strings), return the first match. 
	 * If no alias can be found, return null.
	 * @param node
	 * @return
	 */
	public OrderingHead findSubCategory(OrderingHead node) {
		if (this._subOrderings != null)
			for (OrderingHead cat : this._subOrderings)
				if (cat._value.equals(node._value))
					return cat;
		return null;
	}

	@Override
	public final String getContent()
	{
		return null;
	}

	@Override
	public final int getCursorPosition()
	{
		return 0;
	}

	@Override
	public final String getDescription()
	{
		return null;
	}

	@Override
	public final String getImageString()
	{
		return _imageString;
	}

	@Override
	public final String getLabel()
	{
		return _label;
	}

	@Override
	public final int getPriority()
	{
		return _priority;
	}

	/**
	 * Gets the references.
	 * @return the references
	 */
	public final Vector<ReferenceMods> getReferences()
	{
		return _references;
	}

	@Override
	public final String getValue()
	{
		return _value;
	}

	/**
	 * Sets the aspects.
	 * @param aspects the new aspects
	 */
	public final void setAspects(final Vector<Aspect> aspects)
	{
		this._aspects.removeAllElements();
		this.addAll(aspects);
	}

	/**
	 * Sets the image string.
	 * @param imageString the new image string
	 */
	public final void setImageString(final String imageString)
	{
		this._imageString = imageString;
	}

	/**
	 * Sets the label.
	 * @param label the new label
	 */
	public final void setLabel(final String label)
	{
		this._label = label;
	}

	/**
	 * Sets the priority.
	 * @param priority the new priority
	 */
	public final void setPriority(final int priority)
	{
		this._priority = priority;
	}

	/**
	 * Sets the references.
	 * @param references the new references
	 */
	public final void setReferences(final Vector<ReferenceMods> references)
	{
		this._references = references;
	}

	@Override
	public final void setValue(final String value)
	{
		this._value = value;
	}

	public void setSorted(boolean sorted)
	{
		this._sorted = sorted;

	}

	public boolean isSorted()
	{
		return this._sorted;
	}

}
