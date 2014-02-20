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
package org.bbaw.pdr.ae.view.control.provider;

import org.bbaw.pdr.ae.common.CommonActivator;
import org.bbaw.pdr.ae.common.NLMessages;
import org.bbaw.pdr.ae.common.icons.IconsInternal;
import org.bbaw.pdr.ae.common.icons.PdrObjectDecorator;
import org.bbaw.pdr.ae.control.facade.Facade;
import org.bbaw.pdr.ae.model.Aspect;
import org.bbaw.pdr.ae.model.Concurrence;
import org.bbaw.pdr.ae.model.PdrObject;
import org.bbaw.pdr.ae.model.Person;
import org.bbaw.pdr.ae.model.ReferenceMods;
import org.bbaw.pdr.ae.model.ReferenceModsTemplate;
import org.bbaw.pdr.ae.model.view.TreeNode;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

/**
 * @author Christoph Plutte. Class provides the Labels for the TreeViewer. TODO
 *         has to be dynamized and adapted to dynamic data.
 */
public class TreeLabelProvider implements ILabelProvider
{
	/** Instance of shared image registry. */
	private static ImageRegistry imageReg = CommonActivator.getDefault().getImageRegistry();
	// private CustomDecorator imageDecorator = new CustomDecorator();
	/** The _facade. */
	private Facade _facade = Facade.getInstanz();

	/** The decorator. */
	private static PdrObjectDecorator decorator = new PdrObjectDecorator();

	/** The Constant PERSONS_NEW. */
	private static final Image PERSONS_NEW = decorator.decorateImageKeys(imageReg.get(IconsInternal.PERSONS),
			new String[]
			{IconsInternal.DECORATION_NEW});

	/** The Constant PERSONS_UPDATED. */
	private static final Image PERSONS_UPDATED = decorator.decorateImageKeys(imageReg.get(IconsInternal.PERSONS),
			new String[]
			{IconsInternal.DECORATION_UPDATED});

	/** The Constant PERSON_NEW. */
	private static final Image PERSON_NEW = decorator.decorateImageKeys(imageReg.get(IconsInternal.PERSON),
			new String[]
			{IconsInternal.DECORATION_NEW});

	/** The Constant PERSON_UPDATED. */
	private static final Image PERSON_UPDATED = decorator.decorateImageKeys(imageReg.get(IconsInternal.PERSON),
			new String[]
			{IconsInternal.DECORATION_UPDATED});

	/** The Constant PERSONS_UNDEFINED_NEW. */
	private static final Image PERSONS_UNDEFINED_NEW = decorator.decorateImageKeys(
			imageReg.get(IconsInternal.PERSONS_UNDEFINED), new String[]
			{IconsInternal.DECORATION_NEW});

	/** The Constant PERSONS_UNDEFINED_UPDATED. */
	private static final Image PERSONS_UNDEFINED_UPDATED = decorator.decorateImageKeys(
			imageReg.get(IconsInternal.PERSONS_UNDEFINED), new String[]
			{IconsInternal.DECORATION_UPDATED});

	/** The Constant PERSON_UNDEFINED_NEW. */
	private static final Image PERSON_UNDEFINED_NEW = decorator.decorateImageKeys(
			imageReg.get(IconsInternal.PERSON_UNDEFINED), new String[]
			{IconsInternal.DECORATION_NEW});

	/** The Constant PERSON_UNDEFINED_UPDATED. */
	private static final Image PERSON_UNDEFINED_UPDATED = decorator.decorateImageKeys(
			imageReg.get(IconsInternal.PERSON_UNDEFINED), new String[]
			{IconsInternal.DECORATION_UPDATED});

	/** The Constant ASPECTS_NEW. */
	private static final Image ASPECTS_NEW = decorator.decorateImageKeys(imageReg.get(IconsInternal.ASPECTS),
			new String[]
			{IconsInternal.DECORATION_NEW});

	/** The Constant ASPECTS_UPDATED. */
	private static final Image ASPECTS_UPDATED = decorator.decorateImageKeys(imageReg.get(IconsInternal.ASPECTS),
			new String[]
			{IconsInternal.DECORATION_UPDATED});

	/** The Constant ASPECT_NEW. */
	private static final Image ASPECT_NEW = decorator.decorateImageKeys(imageReg.get(IconsInternal.ASPECT),
			new String[]
			{IconsInternal.DECORATION_NEW});

	/** The Constant ASPECT_UPDATED. */
	private static final Image ASPECT_UPDATED = decorator.decorateImageKeys(imageReg.get(IconsInternal.ASPECT),
			new String[]
			{IconsInternal.DECORATION_UPDATED});

	/** The Constant REFERENCES_NEW. */
	private static final Image REFERENCES_NEW = decorator.decorateImage(imageReg.get(IconsInternal.REFERENCES),
			new String[]
			{IconsInternal.DECORATION_NEW});

	/** The Constant REFERENCES_UPDATED. */
	private static final Image REFERENCES_UPDATED = decorator.decorateImage(imageReg.get(IconsInternal.REFERENCES),
			new String[]
			{IconsInternal.DECORATION_UPDATED});

	@Override
	public void addListener(final ILabelProviderListener listener)
	{

	}

	/**
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#dispose()
	 */
	@Override
	public void dispose()
	{

	}

	// private static final Image REFERENCE_NEW =
	// decorator.decorateImage(imageReg.get(IconsInternal.REFERENCE), new
	// String[]{IconsInternal.DECORATION_NEW});
	// private static final Image REFERENCE_UPDATED =
	// decorator.decorateImage(imageReg.get(IconsInternal.REFERENCE), new
	// String[]{IconsInternal.DECORATION_UPDATED});

	@Override
	public final Image getImage(final Object element)
	{
		TreeNode tn = (TreeNode) element;
		if (tn.hasChildren())
		{
			if (tn.getType().equals("pdrPo"))
			{
				if (tn.getChildren()[0].getPdrObject() != null)
				{
					if (tn.getChildren()[0].getPdrObject().getDisplayName().startsWith("pdrPo")
							|| tn.getChildren()[0].getPdrObject().getDisplayName().compareToIgnoreCase("a") < 0)
					{
						if (tn.isNew())
						{
							return PERSONS_UNDEFINED_NEW;
						}
						if (tn.isUpdated())
						{
							return PERSONS_UNDEFINED_UPDATED;
						}
						else
						{
							return imageReg.get(IconsInternal.PERSONS_UNDEFINED);
						}
					}
					else
					{
						if (tn.isNew())
						{
							return PERSONS_NEW;
						}
						if (tn.isUpdated())
						{
							return PERSONS_UPDATED;
						}
						else
						{
							return imageReg.get(IconsInternal.PERSONS);
						}
					}
				}
				else
				{
					if (tn.isNew())
					{
						return PERSONS_NEW;
					}
					if (tn.isUpdated())
					{
						return PERSONS_UPDATED;
					}
					else
					{
						return imageReg.get(IconsInternal.PERSONS);
					}
				}

			}
			else if (tn.getType().equals("pdrRo"))
			{
				ReferenceMods ref = (ReferenceMods) tn.getPdrObject();
				if (ref != null && ref.getGenre() != null && ref.getGenre().getGenre() != null)
				{
					ReferenceModsTemplate template = _facade.getReferenceModsTemplates().get(ref.getGenre().getGenre());
					if (template != null && template.getImageString() != null)
					{
						return decorator.decorateImage(imageReg.get(template.getImageString()), tn);
					}
					else
					{
						return decorator.decorateImage(imageReg.get(IconsInternal.REFERENCES), tn);
					}
				}
				else if (ref != null && ref.getRelatedItems() != null && ref.getRelatedItems().size() > 0)
				{
					return decorator.decorateImage(imageReg.get(IconsInternal.REFERENCES), tn);
				}
				else if (tn.isNew())
				{
					return REFERENCES_NEW;
				}
				else if (tn.isUpdated())
				{
					return REFERENCES_UPDATED;
				}
				else
				{
					return imageReg.get(IconsInternal.REFERENCES);
					// return
					// decorator.decorateImage(imageReg.get(IconsInternal.REFERENCES),
					// tn);
				}
			}
			else if (tn.getType().equals("pdrAo"))
			{
				if (tn.isNew())
				{
					return ASPECTS_NEW;
				}
				if (tn.isUpdated())
				{
					return ASPECTS_UPDATED;
				}
				else
				{
					return imageReg.get(IconsInternal.ASPECTS);
				}
			}
			else if (tn.getType().equals("facet"))
			{
				return imageReg.get(IconsInternal.MARKUP);
			}
		}
		else if (tn.getPdrObject() != null)
		{
			if (tn.getType().equals("pdrPo"))

			{
				if (tn.getPdrObject().getDisplayName().startsWith("pdrPo")
						|| tn.getPdrObject().getDisplayName().compareToIgnoreCase("a") < 0)
				{
					if (tn.isNew())
					{
						return PERSON_UNDEFINED_NEW;
					}
					if (tn.isUpdated())
					{
						return PERSON_UNDEFINED_UPDATED;
					}
					else
					{
						return imageReg.get(IconsInternal.PERSON_UNDEFINED);
					}

				}
				else
				{
					if (tn.getPdrObject() != null && tn.getParent() != null && tn.getParent().getPdrObject() != null)
					{
						Person parent = (Person) tn.getParent().getPdrObject();
						if (parent.getConcurrences() != null && parent.getConcurrences().getConcurrences() != null)
						{
							for (Concurrence c : parent.getConcurrences().getConcurrences())
							{
								if (c.getPersonId() != null && c.getPersonId().equals(tn.getPdrObject().getPdrId()))
								{
									return imageReg.get(IconsInternal.CONCURRENCE);
								}
							}
						}
					}
					if (tn.isNew())
					{
						return PERSON_NEW;
					}
					if (tn.isUpdated())
					{
						return PERSON_UPDATED;
					}
					else
					{
						return imageReg.get(IconsInternal.PERSON);
					}

				}
			}
			else if (tn.getType().equals("pdrRo"))
			{
				if (tn.getPdrObject() instanceof ReferenceMods)
				{
					ReferenceMods ref = (ReferenceMods) tn.getPdrObject();
					if (ref.getGenre() != null && ref.getGenre().getGenre() != null)
					{
						ReferenceModsTemplate template = _facade.getReferenceModsTemplates().get(
								ref.getGenre().getGenre());
						if (template != null && template.getImageString() != null)
						{
							return decorator.decorateImage(imageReg.get(template.getImageString()), tn);
						}
						else
						{
							return decorator.decorateImage(imageReg.get(IconsInternal.REFERENCE), tn);
						}
					}
					else if (ref.getRelatedItems() != null && ref.getRelatedItems().size() > 0)
					{
						return decorator.decorateImage(imageReg.get(IconsInternal.REFERENCE), tn);
					}
				}
				return decorator.decorateImage(imageReg.get(IconsInternal.REFERENCE), tn);
			}
			else if (tn.getType().equals("pdrAo"))
			{
				if (tn.isNew())
				{
					return ASPECT_NEW;
				}
				if (tn.isUpdated())
				{
					return ASPECT_UPDATED;
				}
				else
				{
					return imageReg.get(IconsInternal.ASPECT);
				}
			}
			else if (tn.getType().equals("noResult"))
			{
				return null;
			}
		}
		else if (tn.getType().equals("facet"))
		{
			return imageReg.get(IconsInternal.MARKUP);
		}
		return null;
	}

	/**
	 * This method returns the actual LabelText put together from different
	 * items.
	 * @param element - element of content array.
	 * @return label - label text of element.
	 */

	@Override
	public final String getText(final Object element)
	{
		TreeNode tn = (TreeNode) element;
		String label = "No Result";
		if (tn.hasChildren())
		{
			return tn.getId() + " (" + tn.getNumberOfLeaves() + " " + typeName(tn.getType()) + ")";
		}
		else if (tn.getType().equals("pdrPo"))
		{
			label = "No Person found";
			// if (p != null)
			// {
			// label = p.getDisplayName();
			// }
			label = tn.getId();
		}
		else if (tn.getType().equals("pdrRo"))
		{
			PdrObject r = tn.getPdrObject();
			label = "No Reference found";
			if (r != null)
			{
				label = r.getDisplayName();
			}
		}
		else if (tn.getType().equals("pdrAo"))
		{
			PdrObject a = tn.getPdrObject();
			label = "No Aspect found";
			if (a != null)
			{
				label = a.getDisplayName();
			}
		}
		else if (tn.getType().equals("facet"))
		{
			return tn.getId();
		}
		return label;

	}

	@Override
	public final boolean isLabelProperty(final Object element, final String property)
	{
		return false;
	}

	@Override
	public final void removeListener(final ILabelProviderListener listener)
	{

	}

	/**
	 * Type name.
	 * @param type the type
	 * @return the string
	 */
	private String typeName(final String type)
	{
		if (type.equals("pdrPo"))
		{
			return NLMessages.getString("ViewProvider_persons");
		}
		if (type.equals("pdrRo"))
		{
			return NLMessages.getString("ViewProvider_references");
		}
		if (type.equals("pdrAo"))
		{
			return NLMessages.getString("ViewProvider_aspects");
		}
		return "";
	}

}
