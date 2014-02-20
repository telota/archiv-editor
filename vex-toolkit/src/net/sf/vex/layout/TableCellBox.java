/*******************************************************************************
 * Copyright (c) 2004, 2008 John Krasnay and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     John Krasnay - initial API and implementation
 *******************************************************************************/
package net.sf.vex.layout;

import java.util.List;

import net.sf.vex.css.Styles;
import net.sf.vex.dom.Element;

/**
 * Represents an element with display:table-cell, or a generated, anonymous
 * table cell.
 */
public class TableCellBox extends AbstractBlockBox {

    /**
     * Class constructor for non-anonymous table cells.
     * 
     * @param context LayoutContext to use.
     * @param parent Parent box.
     * @param element Element with which this box is associated.
     */
    public TableCellBox(LayoutContext context, BlockBox parent, Element element, int width) {
        super(context, parent, element);
        Styles styles = context.getStyleSheet().getStyles(element);
        this.setWidth(width 
                - styles.getBorderLeftWidth() 
                - styles.getPaddingLeft().get(parent.getWidth())
                - styles.getPaddingRight().get(parent.getWidth())
                - styles.getBorderRightWidth());
    }

    public TableCellBox(LayoutContext context, BlockBox parent, int startOffset, int endOffset, int width) {
        super(context, parent, startOffset, endOffset);
        this.setWidth(width);
    }

    protected List createChildren(LayoutContext context) {
        return this.createBlockBoxes(context, this.getStartOffset(), this.getEndOffset(), this.getWidth(), null, null);
    }

    public void setInitialSize(LayoutContext context) {
        // we've already set width in the ctor
        // override to avoid setting width again
        this.setHeight(this.getEstimatedHeight(context));
    }

    

    //======================================================= PRIVATE
    
}
