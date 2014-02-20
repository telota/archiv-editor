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
import org.eclipse.jface.resource.CompositeImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;

/**
 * The Class PdrObjectOverlayImageDescriptor.
 * @author Christoph Plutte
 */
public class PdrObjectOverlayImageDescriptor extends CompositeImageDescriptor
{
	/** Instance of shared image registry.*/
	private ImageRegistry _imageReg = CommonActivator.getDefault().getImageRegistry();
	/**
	   * Base image of the object.
	   */
	  private Image _baseImage;
	  /**
	   * Size of the base image.
	   */
	  private Point _sizeOfImage;
	  /**
	   * Vector of image keys.
	   */
	  private Vector<String> _imageKeys;

	  /** The Constant TOP_LEFT. */
  	private static final int TOP_LEFT = 0;
  	/** The Constant TOP_RIGHT. */
  	private static final int TOP_RIGHT = 1;
  	/** The Constant BOTTOM_LEFT. */
  	private static final int BOTTOM_LEFT = 2;

  	/** The Constant BOTTOM_RIGHT. */
  	private static final int BOTTOM_RIGHT = 3;
	  /**
	   * Constructor for overlayImageIcon.
	 * @param baseImage base image to be decorated.
	 * @param imageKeys decoration keys.
	 */
	public PdrObjectOverlayImageDescriptor(final Image baseImage,
	                          final Vector<String> imageKeys)
	  {
	    // Base image of the object
	    _baseImage = baseImage;
	    this._imageKeys = imageKeys;
	    _sizeOfImage = new Point(baseImage.getBounds().width,
	                             baseImage.getBounds().height);
	  }

	  /**
	   * @see org.eclipse.jface.resource.CompositeImageDescriptor#drawCompositeImage(int, int)
	   * DrawCompositeImage is called to draw the composite image.
	 * @param arg0 arg0.
	 * @param arg1 arg1.
	 * @see org.eclipse.jface.resource.CompositeImageDescriptor#drawCompositeImage(int, int)
	 */
	protected final void drawCompositeImage(final int arg0, final int arg1)
	  {
	    // Draw the base image
	     drawImage(_baseImage.getImageData(), 0, 0);
	     int[] locations = organizeImages();
	     for (int i = 0; i < _imageKeys.size(); i++)
	     {
	        ImageData imageData = _imageReg.getDescriptor(_imageKeys.get(i)).getImageData();
	        switch(locations[i])
	        {
	          // Draw on the top left corner
	          case TOP_LEFT:
	            drawImage(imageData, 0, 0);
//	  	  		System.out.println("try drawCompositeImage TOP_LEFT ");

	            break;
	          // Draw on top right corner
	          case TOP_RIGHT:
	            drawImage(imageData, _sizeOfImage.x - imageData.width, 0);
//	  	  		System.out.println("try drawCompositeImage TOP_RIGHT ");

	            break;
	          // Draw on bottom left
	          case BOTTOM_LEFT:
	            drawImage(imageData, 0, _sizeOfImage.y - imageData.height);
//	  	  		System.out.println("try drawCompositeImage BOTTOM_LEFT ");

	            break;
	          // Draw on bottom right corner
	          case BOTTOM_RIGHT:
//	  	  		System.out.println("try drawCompositeImage BOTTOM_RIGHT ");

	            drawImage(imageData, _sizeOfImage.x - imageData.width,
	                      _sizeOfImage.y - imageData.height);
	            break;
				default:
					break;
	        }
	     }
	  }
	  /**
	   * Organize the images. This function scans through the image key and
	   * finds out the location of the images.
	   * @return position of decoration.
	   */
	  private int [] organizeImages()
	  {
//	  		System.out.println("try organizeImages keys size " + imageKeys.size());

	    int[] locations = new int[_imageKeys.size()];
	    String imageKeyValue;
	    for (int i = 0; i < _imageKeys.size(); i++)
	    {
	    	imageKeyValue = _imageKeys.get(i);
	      if (imageKeyValue.equals(IconsInternal.DECORATION_NEW))
		{
			locations[i] = BOTTOM_RIGHT;
		}
	      if (imageKeyValue.equals(IconsInternal.DECORATION_UPDATED))
	      {
	        // Draw he lock icon in top left corner.
	        locations[i] = BOTTOM_RIGHT;
	      }
	      if (imageKeyValue.equals(IconsInternal.DECORATION_CONCURRENCE))
	      {
	        // Draw dirty flag indicator in the top right corner
	        locations[i] = BOTTOM_LEFT;
	      }
	    }
	    return locations;
	  }
	  /**
	   * @see org.eclipse.jface.resource.CompositeImageDescriptor#getSize()
	   * get the size of the object
	   * @return size of image.
	   */
	  protected final Point getSize()
	  {
	    return _sizeOfImage;
	  }
	  /**
	   * Get the image formed by overlaying different images on the base image.
	   *
	   * @return composite image
	   */
	  public final Image getImage()
	  {
	    return createImage();
	  }
}
