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
package net.sf.vex.widget;

import java.io.IOException;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;

import net.sf.vex.css.StyleSheet;
import net.sf.vex.dom.Document;
import net.sf.vex.dom.DocumentFragment;
import net.sf.vex.dom.DocumentValidationException;
import net.sf.vex.dom.Element;
import net.sf.vex.layout.Box;
import net.sf.vex.layout.BoxFactory;
import net.sf.vex.undo.CannotRedoException;
import net.sf.vex.undo.CannotUndoException;

import org.xml.sax.SAXException;

/**
 * Methods implemented by implementations of the Vex widget on all platforms.
 * This interface is more important as a place to gather common Javadoc than
 * as a way to enforce a contract. 
 */
public interface IVexWidget {
    /**
     * Signals the start of a set of operations that should be considered
     * a single unit for undo/redo purposes.
     * 
     * <p><b>It is <i>strongly</i> recommended to use the {@link #doWork(IRunnable)} method 
     * instead of manually implementing beginWork/endWork.</b></p>
     * 
     * <p>Each call to beginWork should
     * be matched with a call to {@link #endWork(boolean)}. The following pattern can be used
     * to enforce this rules even in the face of exceptions.</p>
     * 
     * <pre>
     * VexComponent c = ...;
     * boolean success = false;
     * try {
     *     c.beginWork();
     *     // do multiple inserts/deletes
     *     success = true;
     * } finally {
     *     c.endWork(success);
     * }
     * </pre>
     * 
     * <p>In the case of nested beginWork/endWork calls, only the outermost
     * results in an undoable event.</p>
     * 
     * @see endWork(boolean)
     */
    public void beginWork();

    /**
     * Returns true if the clipboard has content that can be
     * pasted. Used to enable/disable the paste action of a containing
     * application.
     */
    public boolean canPaste();
    
    /**
     * Returns true if the clipboard has plain text content that can be
     * pasted. Used to enable/disable the "paste text" action of a containing
     * application.
     */
    public boolean canPasteText();
    
    /**
     * Returns true if a redo can be performed.
     */
    public boolean canRedo();

    /**
     * Returns true if an undo can be performed.
     */
    public boolean canUndo();

    /**
     * Returns true if the current element can be unwrapped, i.e. replaced with
     * its content.
     */
    public boolean canUnwrap();
    
    /**
     * Copy the current selection to the clipboard.
     */
    public void copySelection();

    /**
     * Cuts the current selection to the clipboard.
     */
    public void cutSelection();

    /**
     * Deletes the character to the right of the caret.
     */
    public void deleteNextChar() throws DocumentValidationException;

    /**
     * Deletes the character to the left of the caret.
     */
    public void deletePreviousChar() throws DocumentValidationException;

    /**
     * Delete the current selection. Does nothing if there is no
     * current selection.
     */
    public void deleteSelection();

    /**
     * Perform the runnable's run method within a beginWork/endWork pair.
     * All operations in the runnable are treated as a single unit of 
     * work, and can be undone in one operation by the user. Also, if
     * a later operation fails, all earlier operations are also undone.
     *  
     * @param runnable Runnable implementing the work to be done.
     */
    public void doWork(Runnable runnable);
    
    /**
     * Perform the runnable's run method within a beginWork/endWork pair.
     * All operations in the runnable are treated as a single unit of 
     * work, and can be undone in one operation by the user. Also, if
     * a later operation fails, all earlier operations are also undone.
     *  
     * @param savePosition If true, the current caret position is saved
     * and restored once the operation is complete.
     * @param runnable Runnable implementing the work to be done.
     */
    public void doWork(boolean savePosition, Runnable runnable);
    
    /**
     * Signals the end of a set of operations that should be treated as
     * a single unit for undo/redo purposes.
     * 
     * @param success If true, an edit is added to the undo stack.
     * If false, all the changes since the matching beginWork call
     * are undone.
     * 
     * @see #beginWork()
     */
    public void endWork(boolean success);

    /**
     * Returns the innermost box containing the current caret offset
     * that matches the given filter. 
     *
     * @param filter IBoxFilter that determines which box to return
     */
    public Box findInnermostBox(IBoxFilter filter);
    
    /**
     * Returns the <code>BoxFactory</code> used for generating boxes in the
     * layout.
     */
    public BoxFactory getBoxFactory();

    /**
     * Return the offset into the document represented by the caret.
     */
    public int getCaretOffset();

    /**
     * Returns the element at the current caret offset.
     */
    public Element getCurrentElement();

    /**
     * Returns the document associated with this component.
     */
    public Document getDocument();

    /**
     * Returns the width to which the document was layed out.
     */
    public int getLayoutWidth();
    
    /**
     * Returns the offset at which the selection ends.
     */
    public int getSelectionEnd();

    /**
     * Returns the offset at which the selection starts.
     */
    public int getSelectionStart();

    /**
     * Returns the currently selected document fragment, or null if
     * there is no current selection.
     */
    public DocumentFragment getSelectedFragment();

    /**
     * Returns the currently selected string, or an empty string if
     * there is no current selection.
     */
    public String getSelectedText();


    /**
     * Returns the style sheet used to format the document while editing.
     */
    public StyleSheet getStyleSheet();
    
    /**
     * Returns the number of undoable edits that have occurred on this document
     * since editing has started, not including limitations due to maximum
     * undo depth.
     */
    public int getUndoDepth();

    /**
     * Returns an array of names of elements that are valid to insert at the
     * given caret offset and selection
     */
    public String[] getValidInsertElements();

    /**
     * Returns an array of names of elements to which the element at the 
     * current caret location can be morphed.
     */
    public String[] getValidMorphElements();

    /**
     * Returns true if the user currently has some text selected.
     */
    public boolean hasSelection();

    /**
     * Inserts the given character at the current caret position. Any selected
     * content is deleted. The main difference between this method and
     * insertText is that this method does not use beginWork/endWork, so
     * consecutive calls to insertChar are collapsed into a single
     * IUndoableEdit. This method should normally only be called in response
     * to a user typing a key.
     *  
     * @param c Character to insert.
     */
    public void insertChar(char c) throws DocumentValidationException;
    
    /**
     * Inserts the given document fragment at the current caret position. Any
     * selected content is deleted.
     *
     * @param frag DocumentFragment to insert.
     */
    public void insertFragment(DocumentFragment frag)
        throws DocumentValidationException;

    /**
     * Inserts the given element at the current caret position. Any
     * selected content becomes the new contents of the element.
     *
     * @param element Element to insert.
     */
    public void insertElement(Element element)
        throws DocumentValidationException;
        
    /**
     * Inserts the given text at the current caret position. Any
     * selected content is first deleted.
     *
     * @param text String to insert.
     */
    public void insertText(String text) throws DocumentValidationException;

    /**
     * Returns the value of the debugging flag.
     */
    public boolean isDebugging();
    
    /**
     * Sets the value of the debugging flag. When debugging, copious information
     * is dumped to stdout.
     * @param debugging true if debugging is to be enabled.
     */
    public void setDebugging(boolean debugging);

    /**
     * Replaces the current element with the given element. 
     * The content of the element is preserved.
     * @param element Element to replace the current element with.
     * @throws DocumentValidationException if the given element is
     * not valid at this place in the document, or if the current
     * element's content is not compatible with the given element.
     */
    public void morph(Element element) throws DocumentValidationException;
    
    /**
     * Moves the caret a given distance relative to the current caret
     * offset.
     * @param distance Amount by which to alter the caret offset. 
     * Positive values increase the caret offset.
     */
    public void moveBy(int distance);

    /**
     * Moves the caret a given distance relative to the current caret
     * offset.
     * @param distance Amount by which to alter the caret offset. 
     * Positive values increase the caret offset.
     * @param select if true, the current selection is extended to
     * match the new caret offset
     */
    public void moveBy(int distance, boolean select);

    /**
     * Moves the caret to a new offset. The selection is not extended.
     * This is equivalent to <code>moveTo(offset, false)</code>. 
     * @param int new offset for the caret. The offset must be >= 1 and less
     * than the document size; if not, it is silently ignored.
     */
    public void moveTo(int offset);
    
    /**
     * Moves the caret to the new offset, possibly changing the selection.
     *
     * @param int new offset for the caret. The offset must be >= 1 and less
     * than the document size; if not, it is silently ignored.
     * @param select if true, the current selection is extended to
     * match the new caret offset.
     */
    public void moveTo(int offset, boolean select);

    /**
     * Move the caret to the end of the current line.
     * @param select If true, the selection is extended.
     */
    public void moveToLineEnd(boolean select);

    /**
     * Move the caret to the start of the current line.
     * @param select If true, the selection is extended.
     */
    public void moveToLineStart(boolean select);

    /**
     * Move the caret down to the next line. Attempts to preserve the
     * same distance from the left edge of the control.
     * @param select If true, the selection is extended.
     */
    public void moveToNextLine(boolean select);

    /**
     * Move the caret down to the next page. Attempts to preserve the same
     * distance from the left edge of the control.
     * @param select If true, the selection is extended.
     */
    public void moveToNextPage(boolean select);
    
    /**
     * Moves the caret to the end of the current or next word.
     * @param select If true, the selection is extended.
     */
    public void moveToNextWord(boolean select);
    
    /**
     * Moves the caret up to the previous line. 
     * @param select If true, the selection is extended
     */
    public void moveToPreviousLine(boolean select);

    /**
     * Moves the caret up to the previous page. 
     * @param select If true, the selection is extended
     */
    public void moveToPreviousPage(boolean select);

    /**
     * Moves the caret to the start of the current or previous word.
     * @param select If true, the selection is extended.
     */
    public void moveToPreviousWord(boolean select);

    /**
     * Paste the current clipboard contents into the document at the
     * current caret position.
     */
    public void paste() throws DocumentValidationException;

    /**
     * Paste the current clipboard contents as plain text into the document at the
     * current caret position.
     */
    public void pasteText() throws DocumentValidationException;
    /**
     * Redoes the last action on the redo stack.
     * @throws CannotRedoException if the last action cannot be re-done, or if
     * there is nothing to redo.
     */
    public void redo();

    /**
     * Removes an attribute from the current element. Attributes removed in this manner
     * (as opposed to calling Element.setAttribute directly) will be subject to
     * undo/redo.
     * @param attributeName Name of the attribute to remove.
     */
    public void removeAttribute(String attributeName);

    /**
     * Execute a Runnable, restoring the caret position to its original position
     * afterward.
     * 
     * @param runnable Runnable to be invoked.
     */
    public void savePosition(Runnable runnable);
    
    /**
     * Selects all content in the document.
     */
    public void selectAll();
    
    /**
     * Selects the word at the current caret offset.
     */
    public void selectWord();

    /**
     * Sets the value of an attribute in the current element. 
     * Attributes set in this manner
     * (as opposed to calling Element.setAttribute directly) will be subject to
     * undo/redo.
     * @param attributeName Name of the attribute being changed.
     * @param value New value for the attribute. If null, the attribute is 
     * removed from the element.
     */
    public void setAttribute(String attributeName, String value);

    /**
     * Sets the box factory to be applied to the current document during editing.
     * 
     * @param boxFactory the new BoxFactory to use
     */
    public void setBoxFactory(BoxFactory boxFactory);

    /**
     * Sets a new document for this control.
     * 
     * @param document new Document to display
     * @param styleSheet StyleSheet to use for formatting
     */    
    public void setDocument(Document document, StyleSheet styleSheet);
        
    
    /**
     * Sets a new document for this control.
     * 
     * @param docURL URL of the document to display.
     * @param ssURL URL of the stylesheet to use for formatting.
     */    
    public void setDocument(URL docURL, URL ssURL) 
		throws IOException, ParserConfigurationException, SAXException;
    
    /**
     * Sets the width to which the document should be layed out. The actual
     * resulting width may be different due to overflowing boxes. 
     */    
    public void setLayoutWidth(int width);
    
    /**
     * Sets the style sheet to be applied to the current document during editing.
     * If no resolver has been set, the style sheet will also be used for any
     * subsequently loaded documents. If a resolver has been set, the style sheet
     * returned by the resolver will be used for subsequently loaded documents.
     * 
     * @param styleSheet the new StyleSheet to use
     */
    public void setStyleSheet(StyleSheet styleSheet);
    
    /**
     * Sets the stylesheet to be used for this widget.
     *
     * @param ssUrl URL of the CSS style sheet to use.
     */
    public void setStyleSheet(URL ssUrl) throws IOException;
        
    /**
     * Split the element at the current caret offset. This is the normal behaviour
     * when the user presses Enter.
     */
    public void split() throws DocumentValidationException;

    /**
     * Undoes the last action on the undo stack.
     * @throws CannotUndoException if the last action cannot be undone, or
     * if there's nothing left to undo.
     */
    public void undo();

    /**
     * Return the offset into the document for the given coordinates.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     */
    public int viewToModel(int x, int y);


}
