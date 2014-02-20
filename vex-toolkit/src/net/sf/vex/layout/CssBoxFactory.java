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

import net.sf.vex.css.CSS;
import net.sf.vex.css.Styles;
import net.sf.vex.dom.Element;


/**
 * Implementation of the BoxFactory interface that returns boxes that
 * represent CSS semantics.
 */
public class CssBoxFactory implements BoxFactory {

    private static final long serialVersionUID = -6882526795866485074L;

    /**
     * Class constructor.
     */
    public CssBoxFactory() {
    }

    public Box createBox(LayoutContext context, Element element, BlockBox parent, int containerWidth) {
        Styles styles = context.getStyleSheet().getStyles(element);
        if (styles.getDisplay().equals(CSS.TABLE)) {
            return new TableBox(context, parent, element);
        } else if (styles.isBlock()) {
            return new BlockElementBox(context, parent, element);
        } else {
            throw new RuntimeException("Unexpected display property: " + styles.getDisplay());
        }
    }

}

