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

import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

/** custom decorator.
 * @author Christoph Plutte
 *
 */
public class CustomDecorator implements ILabelDecorator
{
//	/** Instance of shared image registry.*/
//	private ImageRegistry _imageReg = CommonActivator.getDefault().getImageRegistry();
//	/** imagedescriptor.*/
//	private ImageDescriptor _imageDescriptor;
//	/** icon.*/
//	private ImageDescriptor _icon;
    /** empty method.
     * @param image image to decorate
     * @param element object of this image.
     * @return decorated image
     * @see org.eclipse.jface.viewers.ILabelDecorator#decorateImage(org.eclipse.swt.graphics.Image, java.lang.Object)
     */
    public final Image decorateImage(final Image image, final Object element)
    {
    	Image resultImage = null;
//    	if(element instanceof TreeNode)
//    	{
//    		TreeNode tn = (TreeNode) element;
////   		 System.out.println("decorating image1");
//
//        if(tn.getPdrObject() != null){
//            PdrObject o = tn.getPdrObject();
//
//            if (o instanceof Person)
//            {
////       		 System.out.println("decorating image2");
//
//            	Person p = (Person) o;
//            	resultImage = imageReg.get(IconsInternal.DECORATION_IDENTIFIER);
//            	 if(p.getIdentifiers() != null && p.getIdentifiers().getIdentifiers().size() > 0)
////                	 if(true)
//
//            	 {
//                	Point point = new Point (16, 16);
////            		 System.out.println("decorating image3");
//            		 icon = icon.createFromImage(imageReg.get(IconsInternal.DECORATION_IDENTIFIER));
//            		 OverlayIcon oi = new OverlayIcon(imageDescriptor.createFromImage(image), icon, point);
//                     /*
//                      * Here use the org.eclipse.ui.internal.OverlayIcon class to
//                      * put the lock icon on the bottom right on the original image
//                      *
//                      */
//            		 resultImage = oi.createImage();
//                 }
//            	 if(p.getConcurrences() != null && p.getConcurrences().getConcurrences().size() > 0)
////                	 if(true)
//
//            	 {
//                	Point point = new Point (16, 16);
////            		 System.out.println("decorating image3");
//            		 icon = icon.createFromImage(imageReg.get(IconsInternal.DECORATION_CONCURRENCE));
//            		 OverlayIcon oi = new OverlayIcon(imageDescriptor.createFromImage(image), icon, point);
//                     /*
//                      * Here use the org.eclipse.ui.internal.OverlayIcon class to
//                      * put the lock icon on the bottom right on the original image
//                      *
//                      */
//            		 resultImage = oi.createImage();
//                 }
//            }
//        }
//        }
        return resultImage;
    }

	/** add listener.
	 * @param listener given listener to add
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#addListener(org.eclipse.jface.viewers.ILabelProviderListener)
	 */
	public void addListener(final ILabelProviderListener listener)
	{
	}

	/** dispose decorator.
	 *
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#dispose()
	 */
	public void dispose()
	{
	}

	/** is label property. see super class.
	 * @param element element.
	 * @param property property.
	 * @return boolean is labelproperty.
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#isLabelProperty(java.lang.Object, java.lang.String)
	 */
	public final boolean isLabelProperty(final Object element, final String property)
	{
		return false;
	}

	/** remove listener.
	 * @param listener to be removed.
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#removeListener(org.eclipse.jface.viewers.ILabelProviderListener)
	 */
	public void removeListener(final ILabelProviderListener listener)
	{
	}

	/** decorate text. not implemented.
	 * @param text text to decorate
	 * @param element element of text.
	 * @return decorated text.
	 * @see org.eclipse.jface.viewers.ILabelDecorator#decorateText(java.lang.String, java.lang.Object)
	 */
	public final String decorateText(final String text, final Object element)
	{
		return text;
	}

}
