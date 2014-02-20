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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sf.vex.core.IntRange;
import net.sf.vex.css.CSS;
import net.sf.vex.css.StyleSheet;
import net.sf.vex.css.Styles;
import net.sf.vex.dom.Element;
import net.sf.vex.dom.Node;

/**
 * Tools for layout and rendering of CSS-styled boxes
 */
public class LayoutUtils {

    /**
     * Create a List of generated inline boxes for the given pseudo-element.
     * @param context LayoutContext in use
     * @param pseudoElement Element representing the generated content.
     */
    public static List createGeneratedInlines(LayoutContext context, Element pseudoElement) {
        String text = getGeneratedContent(context, pseudoElement);
        List list = new ArrayList();
        if (text.length() > 0) {
            list.add(new StaticTextBox(context, pseudoElement, text));
        }
        return list;
    }

    /**
     * Returns true if the given offset falls within the given element or range.
     * 
     * @param elementOrRange Element or IntRange object representing a range 
     * of offsets.
     * @param offset Offset to test.
     */
    public static boolean elementOrRangeContains(Object elementOrRange, int offset) {
        if (elementOrRange instanceof Element) {
            Element element = (Element) elementOrRange;
            return offset > element.getStartOffset() && offset <= element.getEndOffset();
        } else {
            IntRange range = (IntRange) elementOrRange;
            return offset >= range.getStart() && offset <= range.getEnd();
        }
    }
    
    /**
     * Creates a string representing the generated content for the given 
     * pseudo-element.
     * @param context LayoutContext in use
     * @param pseudoElement PseudoElement for which the generated content
     * is to be returned.
     */
    private static String getGeneratedContent(LayoutContext context, Element pseudoElement) {
        Styles styles = context.getStyleSheet().getStyles(pseudoElement);
        List content = styles.getContent();
        StringBuffer sb = new StringBuffer();
        for (Iterator it = content.iterator(); it.hasNext(); ) {
            sb.append((String) it.next()); // TODO: change to ContentPart
        }
        return sb.toString();
    }

    /**
     * Call the given callback for each child matching one of the given
     * display styles. Any nodes that do not match one of the given display types
     * cause the onRange callback to be called, with a range covering all such
     * contiguous nodes.
     * 
     * @param context LayoutContext to use.
     * @param displayStyles Display types to be explicitly recognized.
     * @param element Element containing the children over which to iterate.
     * @param startOffset Starting offset of the range containing nodes in which we're interested.
     * @param endOffset Ending offset of the range containing nodes in which we're interested.
     * @param callback DisplayStyleCallback through which the caller is notified
     * of matching elements and non-matching ranges.
     */
    public static void iterateChildrenByDisplayStyle(StyleSheet styleSheet, Set displayStyles, Element element, int startOffset, int endOffset, ElementOrRangeCallback callback) {
        
        List nonMatching = new ArrayList();
        
        Node[] nodes = element.getChildNodes();
        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i].getEndOffset() <= startOffset) {
                continue;
            } else if (nodes[i].getStartOffset() >= endOffset) {
                break;
            } else {
                Node node = nodes[i];
    
                if (node instanceof Element) {
                    Element childElement = (Element) node;
                    String display = styleSheet.getStyles(childElement).getDisplay();
                    if (displayStyles.contains(display)) {
                        if (nonMatching.size() > 0) {
                            Node firstNode = (Node) nonMatching.get(0);
                            Node lastNode = (Node) nonMatching.get(nonMatching.size() - 1);
                            if (lastNode instanceof Element) {
                                callback.onRange(element, firstNode.getStartOffset(), lastNode.getEndOffset() + 1);
                            } else {
                                callback.onRange(element, firstNode.getStartOffset(), lastNode.getEndOffset());
                            }
                            nonMatching.clear();
                        }
                        callback.onElement(childElement, display);
                    } else {
                        nonMatching.add(node);
                    }
                } else {
                    nonMatching.add(node);
                }
            }
        }
        
        if (nonMatching.size() > 0) {
            Node firstNode = (Node) nonMatching.get(0);
            Node lastNode = (Node) nonMatching.get(nonMatching.size() - 1);
            if (lastNode instanceof Element) {
                callback.onRange(element, firstNode.getStartOffset(), lastNode.getEndOffset() + 1);
            } else {
                callback.onRange(element, firstNode.getStartOffset(), lastNode.getEndOffset());
            }
        }
    }

    /**
     * Call the given callback for each child matching one of the given
     * display styles. Any nodes that do not match one of the given display types
     * cause the onRange callback to be called, with a range covering all such
     * contiguous nodes.
     * 
     * @param context LayoutContext to use.
     * @param displayStyles Display types to be explicitly recognized.
     * @param element Element containing the children over which to iterate.
     * @param callback DisplayStyleCallback through which the caller is notified
     * of matching elements and non-matching ranges.
     */
    public static void iterateChildrenByDisplayStyle(StyleSheet styleSheet, Set displayStyles, Element element, ElementOrRangeCallback callback) {
        iterateChildrenByDisplayStyle(styleSheet, displayStyles, element, element.getStartOffset() + 1, element.getEndOffset(), callback);
    }

    /**
     * Returns true if the given styles represent an element that can be 
     * the child of a table element.
     * 
     * @param styleSheet StyleSheet to use.
     * @param element Element to test.
     */
    public static boolean isTableChild(StyleSheet styleSheet, Element element) {
        String display = styleSheet.getStyles(element).getDisplay();
        return TABLE_CHILD_STYLES.contains(display);
    }

    public static void iterateTableRows(final StyleSheet styleSheet, final Element element, int startOffset, int endOffset, final ElementOrRangeCallback callback) {
        
        iterateChildrenByDisplayStyle(styleSheet, nonRowStyles, element, startOffset, endOffset, new ElementOrRangeCallback() {
            public void onElement(Element child, String displayStyle) {
                if (displayStyle.equals(CSS.TABLE_ROW_GROUP)
                        || displayStyle.equals(CSS.TABLE_HEADER_GROUP)
                        || displayStyle.equals(CSS.TABLE_FOOTER_GROUP)) {
        
                    // iterate rows in group
                    iterateChildrenByDisplayStyle(styleSheet, rowStyles, child, child.getStartOffset() + 1, child.getEndOffset(), callback);
                } else {
                    // other element's can't contain rows
                }
            }
            public void onRange(Element parent, int startOffset, int endOffset) {
                // iterate over rows in range
                iterateChildrenByDisplayStyle(styleSheet, rowStyles, element, startOffset, endOffset, callback);
            }
        });
    
    }

    public static void iterateTableCells(StyleSheet styleSheet, Element element, int startOffset, int endOffset, final ElementOrRangeCallback callback) {
        iterateChildrenByDisplayStyle(styleSheet, cellStyles, element, startOffset, endOffset, callback);
    }

    public static void iterateTableCells(StyleSheet styleSheet, Element element, final ElementOrRangeCallback callback) {
        iterateChildrenByDisplayStyle(styleSheet, cellStyles, element, element.getStartOffset(), element.getEndOffset(), callback);
    }
    
    /**
     * Set of CSS display values that represent elements that can be children 
     * of table elements.
     */
    public static Set TABLE_CHILD_STYLES = new HashSet();
    
    private static Set nonRowStyles = new HashSet();
    private static Set rowStyles = new HashSet();
    private static Set cellStyles = new HashSet();

    
    static {
        nonRowStyles.add(CSS.TABLE_CAPTION);
        nonRowStyles.add(CSS.TABLE_COLUMN);
        nonRowStyles.add(CSS.TABLE_COLUMN_GROUP);
        nonRowStyles.add(CSS.TABLE_ROW_GROUP);
        nonRowStyles.add(CSS.TABLE_HEADER_GROUP);
        nonRowStyles.add(CSS.TABLE_FOOTER_GROUP);
        
        rowStyles.add(CSS.TABLE_ROW);
        
        cellStyles.add(CSS.TABLE_CELL);
        
        TABLE_CHILD_STYLES.addAll(nonRowStyles);
        TABLE_CHILD_STYLES.addAll(rowStyles);
        TABLE_CHILD_STYLES.addAll(cellStyles);
    }



}
