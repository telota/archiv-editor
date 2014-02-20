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
package org.bbaw.pdr.ae.common.icons;

import java.util.Vector;

import org.bbaw.pdr.ae.common.CommonActivator;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;


/** pdrObjects decorator.
 * decorates pdr objects for tree view.
 * @author Christoph Plutte
 *
 */
public class PdrObjectDecorator implements ILabelDecorator
{
	 /** display the overlay image in the upper left corner. */
    public static final int UPPER_LEFT = 0;

    /** display the overlay image in the upper right corner. */
    public static final int UPPER_RIGHT = 1;

    /** display the overlay image in the lower right corner. */
    public static final int LOWER_RIGHT = 2;

    /** display the overlay image in the lower left corner. */
    public static final int LOWER_LEFT = 3;
	/** Instance of shared image registry.*/
	private ImageRegistry _imageReg = CommonActivator.getDefault().getImageRegistry();


	/** add listener.
	 * @param listener listener to be added.
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#addListener(org.eclipse.jface.viewers.ILabelProviderListener)
	 */
	@Override
	public void addListener(final ILabelProviderListener listener)
	{

	}



	/** decorate image.
	 * @param image image to be decorated.
	 * @param element object of image.
	 * @return decorated image.
	 * @see org.eclipse.jface.viewers.ILabelDecorator#decorateImage(org.eclipse.swt.graphics.Image, java.lang.Object)
	 */
	@Override
	public final Image decorateImage(final Image image, final Object element)
	{
		Vector<String> decorationKeys = new Vector<String>(1);
//	    if (element instanceof TreeNode)
//	    {
//	    	TreeNode tn = (TreeNode) element;
//	    	if (tn.isNew())
//	    	{
//	    		decorationKeys.add(IconsInternal.DECORATION_NEW);
//	    	}
//	    	if (tn.isUpdated()) decorationKeys.add(IconsInternal.DECORATION_UPDATED);
//		    if (tn.getPdrObject() instanceof Person)
//		    {
//		    	Person p = (Person) tn.getPdrObject();
//		    	if (p.getConcurrences() != null && !p.getConcurrences().getConcurrences().isEmpty())
//		    	{
//		    		decorationKeys.add(IconsInternal.DECORATION_CONCURRENCE);
//		    	}
//		    }
//	    }
//	    else if (element instanceof ConfigData)
//	    {
////	    	System.out.println("decorate image " +IconsInternal.DECORATION_LOCK);
//	    	ConfigData cd = (ConfigData) element;
//	    	if (cd instanceof DataType)
//	    	{
//	    		decorationKeys.add(IconsInternal.DECORATION_LOCK);
//	    	}
//	    	if (cd instanceof ConfigItem)
//	    	{
//	    		if (((ConfigItem) cd).isMandatory())
//	    		{
//	    			decorationKeys.add(IconsInternal.DECORATION_LOCK);
//	    		}
//	    	}
//	    }
	    return decorateImageKeys(image, decorationKeys.toArray(new String[decorationKeys.size()]));
	}



	/** decorate image by given keys.
	 * @param image image to be decorated.
	 * @param decorationKeys array of keys.
	 * @return decorated image.
	 */
	public final Image decorateImageKeys(final Image image, final String[] decorationKeys)
	{
//		System.out.println("decorate image size " + decorationKeys.length);

	    Image decoratedImage;
		if (decorationKeys.length != 0)
	    {
	    	decoratedImage = drawIconImage(image, decorationKeys);
	    	return decoratedImage;
	    }
	    else
	    {
	        return image;
	    }
	}

	@Override
	public final String decorateText(final String text, final Object element)
	{
		return null;
	}

	/**
	 *  dispose.
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#dispose()
	 */
	@Override
	public void dispose()
	{

	}

	/**
	   * Function to draw icon image.
	   * @param baseImage base image of the object resource
	   * @param decoratorImageKeys vector of image keys
	   * @return icon image with which the resource is to be decorated
	   */
	  private Image drawIconImage(final Image baseImage, final String[] decoratorImageKeys)
	  {
		Image resultImage = baseImage;
	    for (String key : decoratorImageKeys)
	    {
	    	OverlayImageDescriptor oid = new OverlayImageDescriptor(baseImage, _imageReg.getDescriptor(key), organizeImage(key));
	    	resultImage = oid.createImage();

	    }
	    return resultImage;
	  }
	 /** is label property.
	 * @param element element.
	 * @param property property.
	 * @return is label property.
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#isLabelProperty(java.lang.Object, java.lang.String)
	 */
	@Override
	public final boolean isLabelProperty(final Object element, final String property)
	{
		return false;
	}
	  /**
	   * Organize the images. This function scans through the image key and.
	   * finds out the location of the images
	   * @param key key.
	   * @return position of decoration according to key.
	   */
	  private int organizeImage(final String key)
	  {
		  if (key.equals(IconsInternal.DECORATION_LOCK))
    	  {
			  return LOWER_RIGHT;
    	  }
	      if (key.equals(IconsInternal.DECORATION_NEW))
	    	  {
	    	  return LOWER_RIGHT;
	    	  }
	      if (key.equals(IconsInternal.DECORATION_UPDATED))
	      {
	        // Draw he lock icon in top left corner.
	       return LOWER_RIGHT;
	      }
	      if (key.equals(IconsInternal.DECORATION_CONCURRENCE))
	      {
	        // Draw dirty flag indicator in the top right corner
	        return UPPER_LEFT;
	      }
		return 0;

	  }
	/** remove listener.
	 * @param listener to be removed.
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#removeListener(org.eclipse.jface.viewers.ILabelProviderListener)
	 */
	@Override
	public void removeListener(final ILabelProviderListener listener)
	{

	}


}
