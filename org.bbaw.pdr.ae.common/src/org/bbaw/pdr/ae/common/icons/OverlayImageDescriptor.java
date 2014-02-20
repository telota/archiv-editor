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
/**
 * <copyright> Copyright (c) 2008-2009 Jonas Helming, Maximilian Koegel. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this
 * distribution, and is available at http://www.eclipse.org/legal/epl-v10.html </copyright>
 */

package org.bbaw.pdr.ae.common.icons;

import org.eclipse.jface.resource.CompositeImageDescriptor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;

/**
 * Allows one image descriptor to be overlayed on another image descriptor to
 * generate a new image. Commonly used to decorate an image with a second image
 * decoration.
 * @author Shterev
 */
public class OverlayImageDescriptor extends CompositeImageDescriptor
{
	/** display the overlay image in the upper left corner. */
	public static final int UPPER_LEFT = 0;
	/** display the overlay image in the upper right corner. */
	public static final int UPPER_RIGHT = 1;

	/** display the overlay image in the lower right corner. */
	public static final int LOWER_RIGHT = 2;

	/** display the overlay image in the lower left corner. */
	public static final int LOWER_LEFT = 3;

	/** default image width. */
	private static final int DEFAULT_IMAGE_WIDTH = 16;

	/** default image height. */
	private static final int DEFAULT_IMAGE_HEIGHT = 16;

	/** base image. */
	private Image _srcImage;

	/** overlay image. */
	private ImageDescriptor _overlayDesc;

	/** the position of the overlay image. */
	private int _overlayPos = LOWER_RIGHT;

	/** offset. */
	private int _offset = 3;

	/**
	 * OverlayImageDescriptor constructor.
	 * @param srcImage the base image
	 * @param overlayDesc the overlay image
	 * @param overlayPos the overlay position
	 */
	public OverlayImageDescriptor(final Image srcImage, final ImageDescriptor overlayDesc, final int overlayPos)
	{
		assert null != srcImage;
		assert null != overlayDesc;
		this._srcImage = srcImage;
		this._overlayDesc = overlayDesc;
	}

	/**
	 * Draws the given source image data into this composite image at the given
	 * position.
	 * @param width the width of the image.
	 * @param height the height of the image.
	 * @see org.eclipse.jface.resource.CompositeImageDescriptor#drawCompositeImage(int,
	 *      int)
	 */
	@Override
	protected final void drawCompositeImage(final int width, final int height)
	{
		// draw the base image
		ImageData backgroundData = _srcImage.getImageData();
		if (backgroundData != null)
		{
			drawImage(backgroundData, 0, 0);
		}

		// draw the overlay image
		ImageData overlayData = _overlayDesc.getImageData();
		if (overlayData != null)
		{
			Point pos = null;
			switch (_overlayPos)
			{
				case UPPER_LEFT:
					pos = new Point(-overlayData.width / 2, -overlayData.height / 2);
					break;
				case UPPER_RIGHT:
					pos = new Point(backgroundData.width - overlayData.width / 2, 0);
					break;
				case LOWER_RIGHT:
					pos = new Point(backgroundData.width - overlayData.width / 2, backgroundData.height
							- overlayData.height / 2);
					break;
				// default = LOWER_LEFT
				default:
					pos = new Point(0, backgroundData.height - overlayData.height / 2);
					break;
			}
			drawImage(overlayData, pos.x - _offset, pos.y - _offset);

		}
	}

	/**
	 * Retrieve the size of this composite image.
	 * @return the x and y size of the image expressed as a point object
	 * @see org.eclipse.jface.resource.CompositeImageDescriptor#getSize()
	 */
	@Override
	protected final Point getSize()
	{
		return new Point(DEFAULT_IMAGE_WIDTH, DEFAULT_IMAGE_HEIGHT);
	}
}
