/*******************************************************************************
 * Copyright (c) 2004, 2008 John Krasnay and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     John Krasnay - initial API and implementation
 *     Dave Holroyd - Implement text decoration
 *******************************************************************************/
package net.sf.vex.css;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.vex.core.Color;
import net.sf.vex.core.FontSpec;


/**
 * Represents the computed style properties for a particular element.
 */
public class Styles {

    /** Maps property name (String) => value (Object) */
    private Map values = new HashMap();
    
    private List content;
    private FontSpec font;

    /**
     * Returns the value of the given property, or null if the property
     * does not have a value.
     * @param propertyName
     * @return
     */
    public Object get(String propertyName) {
        return this.values.get(propertyName);
    }
    
    /**
     * Returns the value of the <code>backgroundColor</code> property.
     */
    public Color getBackgroundColor() {
        return (Color) this.values.get(CSS.BACKGROUND_COLOR);
    }

    /**
     * Returns the value of the <code>borderBottomColor</code> property.
     */
    public Color getBorderBottomColor() {
        return (Color) this.values.get(CSS.BORDER_BOTTOM_COLOR);
    }

    /**
     * Returns the value of the <code>borderBottomStyle</code> property.
     */
    public String getBorderBottomStyle() {
        return (String) this.values.get(CSS.BORDER_BOTTOM_STYLE);
    }

    /**
     * Returns the value of the <code>borderLeftColor</code> property.
     */
    public Color getBorderLeftColor() {
        return (Color) this.values.get(CSS.BORDER_LEFT_COLOR);
    }

    /**
     * Returns the value of the <code>borderLeftStyle</code> property.
     */
    public String getBorderLeftStyle() {
        return (String) this.values.get(CSS.BORDER_LEFT_STYLE);
    }

    /**
     * Returns the value of the <code>borderRightColor</code> property.
     */
    public Color getBorderRightColor() {
        return (Color) this.values.get(CSS.BORDER_RIGHT_COLOR);
    }

    /**
     * Returns the value of the <code>borderRightStyle</code> property.
     */
    public String getBorderRightStyle() {
        return (String) this.values.get(CSS.BORDER_RIGHT_STYLE);
    }

    /**
     * Returns the value of the <code>borderSpacing</code> property.
     */
    public BorderSpacingProperty.Value getBorderSpacing() {
        return (BorderSpacingProperty.Value) this.values.get(CSS.BORDER_SPACING);
    }

    /**
     * Returns the value of the <code>borderTopColor</code> property.
     */
    public Color getBorderTopColor() {
        return (Color) this.values.get(CSS.BORDER_TOP_COLOR);
    }

    /**
     * Returns the value of the <code>borderTopStyle</code> property.
     */
    public String getBorderTopStyle() {
        return (String) this.values.get(CSS.BORDER_TOP_STYLE);
    }

    /**
     * Returns the value of the <code>color</code> property.
     */
    public Color getColor() {
        return (Color) this.values.get(CSS.COLOR);
    }

    /**
     * Returns a <code>List</code> of <code>ContentPart</code> objects
     * representing the <code>content</code> property.
     */
    public List getContent() {
        return content;
    }
    
    /**
     * Returns the value of the <code>display</code> property.
     */
    public String getDisplay() {
        return (String) this.values.get(CSS.DISPLAY);
    }

    /**
     * Returns the value of the <code>font</code> property.
     */
    public FontSpec getFont() {
        return font;
    }

    /**
     * Returns the value of the <code>fontFamily</code> property.
     */
    public String[] getFontFamilies() {
        return (String[]) this.values.get(CSS.FONT_FAMILY);
    }

    /**
     * Returns the value of the <code>fontSize</code> property.
     */
    public float getFontSize() {
        return ((Float) this.values.get(CSS.FONT_SIZE)).floatValue();
    }

    /**
     * Returns the value of the <code>fontStyle</code> property.
     */
    public String getFontStyle() {
        return (String) this.values.get(CSS.FONT_STYLE);
    }

    /**
     * Returns the value of the <code>fontWeight</code> property.
     */
    public int getFontWeight() {
        return ((Integer) this.values.get(CSS.FONT_WEIGHT)).intValue();
    }

    /**
     * Returns the value of the <code>lineHeight</code> property.
     */
    public int getLineHeight() {
        return ((RelativeLength) this.values.get(CSS.LINE_HEIGHT)).get(Math.round(this.getFontSize()));
    }

    /**
     * Returns the value of the <code>listStyleType</code> property.
     */
    public String getListStyleType() {
        return (String) this.values.get(CSS.LIST_STYLE_TYPE);
    }

    /**
     * Returns the value of the <code>textAlign</code> property.
     */
    public String getTextAlign() {
        return (String) this.values.get(CSS.TEXT_ALIGN);
    }

    /**
     * Returns the value of the <code>textDecoration</code> property.
     */
    public String getTextDecoration() {
        return (String) this.values.get(CSS.TEXT_DECORATION);
    }

    /**
     * Returns the value of the <code>whiteSpace</code> property.
     */
    public String getWhiteSpace() {
        return (String) this.values.get(CSS.WHITE_SPACE);
    }
    
    /**
     * Returns true if this element is block-formatted, or false if it
     * is inline-formatted.
     */
    public boolean isBlock() {
        return this.getDisplay().equals(CSS.BLOCK)
            || this.getDisplay().equals(CSS.LIST_ITEM)
            || this.getDisplay().equals(CSS.TABLE)
            || this.getDisplay().equals(CSS.TABLE_CAPTION)
            || this.getDisplay().equals(CSS.TABLE_CELL)
            || this.getDisplay().equals(CSS.TABLE_COLUMN)
            || this.getDisplay().equals(CSS.TABLE_COLUMN_GROUP)
            || this.getDisplay().equals(CSS.TABLE_FOOTER_GROUP)
            || this.getDisplay().equals(CSS.TABLE_HEADER_GROUP)
            || this.getDisplay().equals(CSS.TABLE_ROW)
            || this.getDisplay().equals(CSS.TABLE_ROW_GROUP);
    }

    /**
     * Sets the value of a property in this stylesheet.
     * @param propertyName Name of the property being set.
     * @param value Value of the property.
     */
    public void put(String propertyName, Object value) {
        this.values.put(propertyName, value);
    }
    
    /**
     * Sets the vale of the <code>content</code> property.
     * @param content <code>List</code> of <code>ContentPart</code> objects
     * representing the content.
     */
    public void setContent(List content) {
        this.content = content;
    }
    
    /**
     * Sets the value of the <code>font</code> property.
     * @param font new value for the <code>font</code> property.
     */
    public void setFont(FontSpec font) {
        this.font = font;
    }

    /**
     * @return the value of border-bottom-width
     */
    public int getBorderBottomWidth() {
        return ((Integer) this.values.get(CSS.BORDER_BOTTOM_WIDTH)).intValue();
    }

    /**
     * @return the value of border-left-width
     */
    public int getBorderLeftWidth() {
        return ((Integer) this.values.get(CSS.BORDER_LEFT_WIDTH)).intValue();
    }

    /**
     * @return the value of border-right-width
     */
    public int getBorderRightWidth() {
        return ((Integer) this.values.get(CSS.BORDER_RIGHT_WIDTH)).intValue();
    }

    /**
     * @return the value of border-top-width
     */
    public int getBorderTopWidth() {
        return ((Integer) this.values.get(CSS.BORDER_TOP_WIDTH)).intValue();
    }

    /**
     * @return the value of margin-bottom
     */
    public RelativeLength getMarginBottom() {
        return (RelativeLength) this.values.get(CSS.MARGIN_BOTTOM);
        //return marginBottom;
    }

    /**
     * @return the value of margin-left
     */
    public RelativeLength getMarginLeft() {
        return (RelativeLength) this.values.get(CSS.MARGIN_LEFT);
    }

    /**
     * @return the value of margin-right
     */
    public RelativeLength getMarginRight() {
        return (RelativeLength) this.values.get(CSS.MARGIN_RIGHT);
    }

    /**
     * @return the value of margin-top
     */
    public RelativeLength getMarginTop() {
        return (RelativeLength) this.values.get(CSS.MARGIN_TOP);
    }

    /**
     * @return the value of padding-bottom
     */
    public RelativeLength getPaddingBottom() {
        return (RelativeLength) this.values.get(CSS.PADDING_BOTTOM);
    }

    /**
     * @return the value of padding-left
     */
    public RelativeLength getPaddingLeft() {
        return (RelativeLength) this.values.get(CSS.PADDING_LEFT);
    }

    /**
     * @return the value of padding-right
     */
    public RelativeLength getPaddingRight() {
        return (RelativeLength) this.values.get(CSS.PADDING_RIGHT);
    }

    /**
     * @return the value of padding-top
     */
    public RelativeLength getPaddingTop() {
        return (RelativeLength) this.values.get(CSS.PADDING_TOP);
    }

}
