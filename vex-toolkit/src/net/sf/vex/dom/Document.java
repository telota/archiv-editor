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
package net.sf.vex.dom;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.vex.core.ListenerList;
import net.sf.vex.undo.CannotRedoException;
import net.sf.vex.undo.CannotUndoException;
import net.sf.vex.undo.IUndoableEdit;

/**
 * Represents an XML document.
 */
public class Document {

    private Content content;
    private RootElement rootElement;
    private ListenerList listeners = new ListenerList(DocumentListener.class, DocumentEvent.class);
    private boolean undoEnabled = true;
    
    private String publicID;
    private String systemID;
    private String encoding;
    private Validator validator;

    /**
     * Class constructor.
     * @param rootElement root element of the document. The
     * document property of this RootElement is set by this
     * constructor.
     */
    public Document(RootElement rootElement) {
	this.content = new GapContent(100);
	this.rootElement = rootElement;
        rootElement.setDocument(this);
	this.content.insertString(0, "\0\0");
	rootElement.setContent(this.content, 0, 1);
    }

    /**
     * Class constructor. This constructor is used by the document builder
     * and assumes that the content and root element have bee properly set up.
     * 
     * @param content Content object used to store the document's content.
     * @param rootElement RootElement of the document.
     */
    public Document(Content content, RootElement rootElement) {
        this.content = content;
        this.rootElement = rootElement;
    }
    
    /**
     * Adds a document listener to the list of listeners to be notified of
     * document changes.
     *
     * @param listener <code>DocumentListener</code> to add.
     */
    public void addDocumentListener(DocumentListener listener) {
        this.listeners.add(listener);
    }

    /**
     * Returns true if the given document fragment can be inserted at the
     * given offset. 
     * 
     * @param offset offset where the insertion is to occur
     * @param fragment fragment to be inserted
     */
    public boolean canInsertFragment(int offset, DocumentFragment fragment) {
        
        if (this.validator == null) {
            return true;
        }
        
        Element element = this.getElementAt(offset);
        String[] seq1 = this.getNodeNames(element.getStartOffset() + 1, offset);
        String[] seq2 = fragment.getNodeNames();
        String[] seq3 = this.getNodeNames(offset, element.getEndOffset());
        return this.validator.isValidSequence(
            element.getName(),
            seq1, seq2, seq3, true);
    }
    
    /**
     * Returns true if text can be inserted at the
     * given offset. 
     * 
     * @param offset offset where the insertion is to occur
     */
    public boolean canInsertText(int offset) {
        
        if (this.validator == null) {
            return true;
        }
        
        Element element = this.getElementAt(offset);
        String[] seq1 = this.getNodeNames(element.getStartOffset() + 1, offset);
        String[] seq2 = new String[] { Validator.PCDATA };
        String[] seq3 = this.getNodeNames(offset, element.getEndOffset());
        
        return this.validator.isValidSequence(
            element.getName(), seq1, seq2, seq3, true);        
    }
    
    /**
     * Creates a <code>Position</code> object at the given character offset.
     *
     * @param offset initial character offset of the position
     */
    public Position createPosition(int offset) {
	return this.content.createPosition(offset);
    }

    /**
     * Deletes a portion of the document. No element may straddle the
     * deletion span.
     *
     * @param startOffset start of the range to delete
     * @param endOffset end of the range to delete
     * @throws DocumentValidationException if the change would result
     * in an invalid document.
     */
    public void delete(int startOffset, int endOffset)
	throws DocumentValidationException {

        Element e1 = this.getElementAt(startOffset);
        Element e2 = this.getElementAt(endOffset);
        if (e1 != e2) {
            throw new IllegalArgumentException("Deletion from " + startOffset + " to " + endOffset + " is unbalanced");
        }

	Validator validator = this.getValidator();
	if (validator != null) {
            String[] seq1 = this.getNodeNames(e1.getStartOffset() + 1, startOffset);
            String[] seq2 = this.getNodeNames(endOffset, e1.getEndOffset());
	    if (!validator.isValidSequence(
                    e1.getName(), seq1, seq2, null, true)) {
		throw new DocumentValidationException("Unable to delete from " + startOffset + " to " + endOffset);
	    }
	}
    
        // Grab the fragment for the undoable edit while it's still here
        DocumentFragment frag = getFragment(startOffset, endOffset);

        this.fireBeforeContentDeleted( 
            new DocumentEvent(this, e1, startOffset, endOffset - startOffset, null));

	Iterator iter = e1.getChildIterator();
	while (iter.hasNext()) {
	    Element child = (Element) iter.next();
	    if (startOffset <= child.getStartOffset() &&
		child.getEndOffset() < endOffset) {
		iter.remove();
	    }
	}

	this.content.remove(startOffset, endOffset - startOffset);

        IUndoableEdit edit = this.undoEnabled ? 
                new DeleteEdit(startOffset, endOffset, frag) : null;
    
        this.fireContentDeleted( 
            new DocumentEvent(this, e1, startOffset, endOffset - startOffset, edit));
    }

    /**
     * Finds the lowest element that contains both of the given offsets.
     *
     * @param offset1 the first offset
     * @param offset2 the second offset
     */
    public Element findCommonElement(int offset1, int offset2) {
	Element element = this.rootElement;
	for (;;) {
	    boolean tryAgain = false;
	    Element[] children = element.getChildElements();
	    for (int i = 0; i < children.length; i++) {
		if (offset1 > children[i].getStartOffset() &&
		    offset2 > children[i].getStartOffset() &&
		    offset1 <= children[i].getEndOffset() &&
		    offset2 <= children[i].getEndOffset()) {
		    
		    element = children[i];
		    tryAgain = true;
		    break;
		}
	    }
	    if (!tryAgain) {
		break;
	    }
	}
	return element;
    }

    /**
     * Returns the character at the given offset.
     */
    public char getCharacterAt(int offset) {
	return this.content.getString(offset, 1).charAt(0);
    }

    /**
     * Returns the element at the given offset. The given offset must be
     * greater or equal to 1 and less than the current document length.
     */
    public Element getElementAt(int offset) {
	if (offset < 1 || offset >= this.getLength()) {
	    throw new IllegalArgumentException("Illegal offset: " + offset + ". Must be between 1 and n-1");
	}
	Element element = this.rootElement;
	for (;;) {
	    boolean tryAgain = false;
	    Element[] children = element.getChildElements();
	    for (int i = 0; i < children.length; i++) {
		Element child = children[i];
		if (offset <= child.getStartOffset()) {
		    return element;
		} else if (offset <= child.getEndOffset()) {
		    element = child;
		    tryAgain = true;
		    break;
		}
	    }
	    if (!tryAgain) {
		break;
	    }
	}
	return element;
    }

    /**
     * Returns the encoding used for this document, or null if no
     * encoding has been declared.
     */
    public String getEncoding() {
	return this.encoding;
    }

    /**
     * Create a <code>DocumentFragment</code> representing the given
     * range of offsets.
     * 
     * @return
     */
    public DocumentFragment getFragment(int startOffset, int endOffset) {
    
        assertOffset(startOffset, 0, this.content.getLength());
        assertOffset(endOffset, 0, this.content.getLength());
            
        if (endOffset <= startOffset) {
            throw new IllegalArgumentException(
                "Invalid range (" + startOffset + ", " + endOffset + ")");
        }
        
        Element e1 = this.getElementAt(startOffset);
        Element e2 = this.getElementAt(endOffset);
        if (e1 != e2) {
            throw new IllegalArgumentException(
                "Fragment from "
                    + startOffset
                    + " to "
                    + endOffset
                    + " is unbalanced");
        }

        Element[] children = e1.getChildElements();
        
        Content newContent = new GapContent(endOffset - startOffset);
        String s = this.content.getString(startOffset, endOffset - startOffset);
        newContent.insertString(0, s);
        List newChildren = new ArrayList();
        for (int i = 0; i < children.length; i++) {
            Element child = children[i];
            if (child.getEndOffset() <= startOffset) {
                continue;
            } else if (child.getStartOffset() >= endOffset) {
                break;
            } else {
                newChildren.add(
                    this.cloneElement(child, newContent, -startOffset, null));
            }
        }
        
        Element[] elementArray =
            (Element[]) newChildren.toArray(new Element[newChildren.size()]);
        return new DocumentFragment(newContent, elementArray);
    }
    
    /**
     * Returns the length of the document in characters, including the null
     * characters that delimit each element.
     */
    public int getLength() {
	return this.content.getLength();
    }

    /**
     * Returns an array of element names and Validator.PCDATA representing
     * the content between the given offsets. The given offsets must both
     * be directly in the same element.
     * 
     * @param startOffset the offset at which the sequence begins
     * @param endOffset the offset at which the sequence ends
     */
    public String[] getNodeNames(int startOffset, int endOffset) {

        Node[] nodes = this.getNodes(startOffset, endOffset);
        String[] names = new String[nodes.length];
        
        for (int i = 0; i < nodes.length; i++) {
            Node node = nodes[i];
            if (node instanceof Element) {
                names[i] = ((Element)node).getName();
            } else {
                names[i] = Validator.PCDATA;
            }
        }

        return names;
    }
    
    /**
     * Returns an array of Nodes representing the selected range. The given offsets must both
     * be directly in the same element.
     * 
     * @param startOffset the offset at which the sequence begins
     * @param endOffset the offset at which the sequence ends
     */
    public Node[] getNodes(int startOffset, int endOffset) {

        Element element = this.getElementAt(startOffset);
        if (element != this.getElementAt(endOffset)) {
            throw new IllegalArgumentException(
                "Offsets are unbalanced: " + 
                startOffset + " is in " + element.getName() + ", " + 
                endOffset + " is in " + this.getElementAt(endOffset).getName());
        }

        List list = new ArrayList();
        Node[] nodes = element.getChildNodes();
        for (int i = 0; i < nodes.length; i++) {
            Node node = nodes[i];
            if (node.getEndOffset() <= startOffset) {
                continue;
            } else if (node.getStartOffset() >= endOffset) {
                break;
            } else {
                if (node instanceof Element) {
                    list.add(node);
                } else {
                    Text text = (Text) node;
                    if (text.getStartOffset() < startOffset) {
                        text.setContent(text.getContent(), startOffset, text.getEndOffset());
                    } else if (text.getEndOffset() > endOffset) {
                        text.setContent(text.getContent(), text.getStartOffset(), endOffset);
                    }
                    list.add(text);
                }
            }
        }

        return (Node[]) list.toArray(new Node[list.size()]);
    }
    
    /**
     * Creates an array of nodes for a given run of content. The returned array includes the given child
     * elements and <code>Text</code> objects where text appears between elements.
     * 
     * @param content Content object containing the content
     * @param startOffset start offset of the run
     * @param endOffset end offset of the run
     * @param elements child elements that are within the run
     */
    static Node[] createNodeArray(Content content, int startOffset, int endOffset, Element[] elements) {

        List nodes = new ArrayList();
        int offset = startOffset;
        for (int i = 0; i < elements.length; i++) {
            int start = elements[i].getStartOffset();
            if (offset < start) {
                nodes.add(new Text(content, offset, start));
            }
            nodes.add(elements[i]);
            offset = elements[i].getEndOffset() + 1;
        }

        if (offset < endOffset) {
            nodes.add(new Text(content, offset, endOffset));
        }

        return (Node[]) nodes.toArray(new Node[nodes.size()]);
    }

    /**
     * Returns the public ID of the document type.
     */
    public String getPublicID() {
	return this.publicID;
    }

    /**
     * Returns the text between the two given offsets. Unlike getText,
     * sentinel characters are not removed.
     *
     * @param startOffset character offset of the start of the text
     * @param endOffset character offset of the end of the text
     */
    public String getRawText(int startOffset, int endOffset) {
	return this.content.getString(startOffset, 
				      endOffset - startOffset);
    }

    /**
     * Returns the root element of this document.
     */
    public Element getRootElement() {
        return this.rootElement;
    }

    /**
     * Returns the system ID of the document type.
     */
    public String getSystemID() {
	return this.systemID;
    }

    /**
     * Returns the text between the two given offsets. Sentinal characters
     * are removed.
     *
     * @param startOffset character offset of the start of the text
     * @param endOffset character offset of the end of the text
     */
    public String getText(int startOffset, int endOffset) {
	String raw = this.content.getString(startOffset, 
					    endOffset - startOffset);
	StringBuffer sb = new StringBuffer(raw.length());
	for (int i = 0; i < raw.length(); i++) {
	    char c = raw.charAt(i);
	    if (c != '\0') {
		sb.append(c);
	    }
	}
	return sb.toString();
    }

    /**
     * Returns the validator used to validate the document, or null if
     * a validator has not been set. Note that the DocumentFactory
     * does not automatically create a validator.
     */
    public Validator getValidator() {
        return this.validator;
    }

    /**
     * Inserts an element at the given position.
     *
     * @param offset character offset at which the element is to be inserted.
     * Must be greater or equal to 1 and less than the current length of the
     * document, i.e. it must be within the range of the root element.
     * @param element element to insert
     * @throws DocumentValidationException if the change would result
     * in an invalid document.
     */
    public void insertElement(int offset, Element element) 
	throws DocumentValidationException {

	if (offset < 1 || offset >= this.getLength()) {
	    throw new IllegalArgumentException("Error inserting element <" + element.getName() + ">: offset is " + offset + ", but it must be between 1 and " + (this.getLength() - 1));
	}

	Validator validator = this.getValidator();
	if (validator != null) {
            Element parent = this.getElementAt(offset);
            String[] seq1 = this.getNodeNames(parent.getStartOffset() + 1, offset);
            String[] seq2 = new String[] { element.getName() };
            String[] seq3 = this.getNodeNames(offset, parent.getEndOffset());
            if (!validator.isValidSequence(
                    parent.getName(), seq1, seq2, seq3, true)) {
		throw new DocumentValidationException("Cannot insert element " + element.getName() + " at offset " + offset);
	    }		
	}

	// find the parent, and the index into its children at which
	// this element should be inserted
	Element parent = this.rootElement;
	int childIndex = -1;
	while (childIndex == -1) {
	    boolean tryAgain = false;
	    Element[] children = parent.getChildElements();
	    for (int i = 0; i < children.length; i++) {
		Element child = children[i];
		if (offset <= child.getStartOffset()) {
		    childIndex = i;
		    break;
		} else if (offset <= child.getEndOffset()) {
		    parent = child;
		    tryAgain = true;
		    break;
		}
	    }
	    if (!tryAgain && childIndex == -1) {
		childIndex = children.length;
		break;
	    }
	}

        this.fireBeforeContentInserted(new DocumentEvent(this, parent, offset, 2, null));
        
	this.content.insertString(offset, "\0\0");

	element.setContent(this.content, offset, offset + 1);
	element.setParent(parent);
	parent.insertChild(childIndex, element);

        IUndoableEdit edit = this.undoEnabled ? 
                new InsertElementEdit(offset, element) : null;
    
	this.fireContentInserted(new DocumentEvent(this, parent, offset, 2, edit));
    }


    /**
     * Inserts a document fragment at the given position.
     *
     * @param offset character offset at which the element is to be inserted.
     * Must be greater or equal to 1 and less than the current length of the
     * document, i.e. it must be within the range of the root element.
     * @param fragment fragment to insert
     * @throws DocumentValidationException if the change would result
     * in an invalid document.
     */
    public void insertFragment(int offset, DocumentFragment fragment)
        throws DocumentValidationException {
         
        if (offset < 1 || offset >= this.getLength()) {
            throw new IllegalArgumentException("Error inserting document fragment");
        }

        Element parent = this.getElementAt(offset);

        if (this.validator != null) {
            String[] seq1 = this.getNodeNames(parent.getStartOffset() + 1, offset);
            String[] seq2 = fragment.getNodeNames();
            String[] seq3 = this.getNodeNames(offset, parent.getEndOffset());
            if (!validator.isValidSequence(
                    parent.getName(), seq1, seq2, seq3, true)) {
             
                throw new DocumentValidationException("Cannot insert document fragment");
            }
        }

        this.fireBeforeContentInserted(new DocumentEvent(this, parent, offset, 2, null));
        
        Content c = fragment.getContent();
        String s = c.getString(0, c.getLength());
        this.content.insertString(offset, s);
        
        Element[] children = parent.getChildElements();
        int index = 0;
        while (index < children.length 
            && children[index].getEndOffset() < offset) {
            index++;
        }
        
        Element[] elements = fragment.getElements();
        for (int i = 0; i < elements.length; i++) {
            Element newElement = this.cloneElement(elements[i], this.content, offset, parent);
            parent.insertChild(index, newElement);
            index++;
        }
        
        IUndoableEdit edit = this.undoEnabled ?
                new InsertFragmentEdit(offset, fragment) : null;
                
        this.fireContentInserted(
            new DocumentEvent(this, parent, offset, fragment.getContent().getLength(), edit));
    }
    
    /**
     * Inserts text at the given position.
     *
     * @param offset character offset at which the text is to be inserted.
     * Must be greater or equal to 1 and less than the current length of the
     * document, i.e. it must be within the range of the root element.
     * @param text text to insert
     * @return UndoableEdit that can be used to undo the deletion
     * @throws DocumentValidationException if the change would result
     * in an invalid document.
     */
    public void insertText(int offset, String text)
	throws DocumentValidationException {

	if (offset < 1 || offset >= this.getLength()) {
	    throw new IllegalArgumentException("Offset must be between 1 and n-1");
	}

        Element parent = this.getElementAt(offset);

	boolean isValid = false;
	if (this.getCharacterAt(offset-1) != '\0') {
	    isValid = true;
	} else if (this.getCharacterAt(offset) != '\0') {
	    isValid = true;
	} else {
	    Validator validator = this.getValidator();
	    if (validator != null) {
                String[] seq1 = this.getNodeNames(parent.getStartOffset() + 1, offset);
                String[] seq2 = new String[] { Validator.PCDATA };
                String[] seq3 = this.getNodeNames(offset, parent.getEndOffset());
                isValid = validator.isValidSequence(
                    parent.getName(), seq1, seq2, seq3, true);
	    } else {
		isValid = true;
	    }
	}

	if (!isValid) {
	    throw new DocumentValidationException("Cannot insert text '" + text + "' at offset " + offset);
	}
    
	// Convert control chars to spaces
	StringBuffer sb = new StringBuffer(text);
	for (int i = 0; i < sb.length(); i++) {
	    if (Character.isISOControl(sb.charAt(i)) && sb.charAt(i) != '\n') {
	        sb.setCharAt(i, ' ');
	    }
	}
    
	String s = sb.toString();

        this.fireBeforeContentInserted(new DocumentEvent(this, parent, offset, 2, null));
        
	this.content.insertString(offset, s);

	IUndoableEdit edit = this.undoEnabled ?
            new InsertTextEdit(offset, s) : null;
            
	this.fireContentInserted(
            new DocumentEvent(this, parent, offset, s.length(), edit));
    }

    /**
     * Returns true if undo is enabled, that is, undoable edit events are fired
     * to registered listeners.
     */
    public boolean isUndoEnabled() {
        return this.undoEnabled;
    }
    
    /**
     * Removes a document listener from the list of listeners so that
     * it is no longer notified of document changes.
     *
     * @param listener <code>DocumentListener</code> to remove.
     */
    public void removeDocumentListener(DocumentListener listener) {
        this.listeners.remove(listener);
    }

    /**
     * Sets the public ID for the document's document type.
     *
     * @param publicID New value for the public ID.
     */
    public void setPublicID(String publicID) {
	this.publicID = publicID;
    }

    /**
     * Sets the system ID for the document's document type.
     *
     * @param systemID New value for the system ID.
     */
    public void setSystemID(String systemID) {
	this.systemID = systemID;
    }

    /**
     * Sets whether undo events are enabled. Typically, undo events are
     * disabled while an edit is being undone or redone.
     * 
     * @param undoEnabled If true, undoable edit events are fired to 
     * registered listeners.
     */
    public void setUndoEnabled(boolean undoEnabled) {
        this.undoEnabled = undoEnabled;
    }
    
    /**
     * Sets the validator to use for this document.
     *
     * @param validator Validator to use for this document.
     */
    public void setValidator(Validator validator) {
	this.validator = validator;
    }

    //==================================================== PRIVATE
    
    /**
     * Represents a deletion from a document that can be undone
     * and redone.
     */
    private class DeleteEdit implements IUndoableEdit {

        private int startOffset;
        private int endOffset;
        private DocumentFragment frag;
    
        public DeleteEdit(int startOffset, int endOffset, DocumentFragment frag) {
            this.startOffset = startOffset;
            this.endOffset = endOffset;
            this.frag = frag; 
        }
        
        public boolean combine(IUndoableEdit edit) {
            return false;
        }

        public void undo() throws CannotUndoException {
            try {
                setUndoEnabled(false);
                insertFragment(this.startOffset, this.frag);
            } catch (DocumentValidationException ex) {
                throw new CannotUndoException();
            } finally {
                setUndoEnabled(true);
            }
        }

        public void redo() throws CannotRedoException {
            try {
                setUndoEnabled(false);
                delete(this.startOffset, this.endOffset);
            } catch (DocumentValidationException ex) {
                throw new CannotUndoException();
            } finally {
                setUndoEnabled(true);
            }
        }

    }


    /**
     * Represents an insertion of an element into the document.
     */
    private class InsertElementEdit implements IUndoableEdit {

        private int offset;
        private Element element;
    
        public InsertElementEdit(int offset, Element element) {
            this.offset = offset;
            this.element = element;
        }

        public boolean combine(IUndoableEdit edit) {
            return false;
        }

        public void undo() throws CannotUndoException {
            try {
                setUndoEnabled(false);
                delete(this.offset, this.offset + 2);
            } catch (DocumentValidationException ex) {
                throw new CannotUndoException();
            } finally {
                setUndoEnabled(true);
            }
        }

        public void redo() throws CannotRedoException {
            try {
                setUndoEnabled(false);
                insertElement(this.offset, this.element);
            } catch (DocumentValidationException ex) {
                throw new CannotUndoException();
            } finally {
                setUndoEnabled(true);
            }
        }

    }
    
     
    /**
     * Represents an insertion of a fragment into the document.
     */
    private class InsertFragmentEdit implements IUndoableEdit {

        private int offset;
        private DocumentFragment frag;
    
        public InsertFragmentEdit(int offset, DocumentFragment frag) {
            this.offset = offset;
            this.frag = frag;
        }

        public boolean combine(IUndoableEdit edit) {
            return false;
        }

        public void undo() throws CannotUndoException {
            try {
                setUndoEnabled(false);
                int length = this.frag.getContent().getLength();
                delete(this.offset, this.offset + length);
            } catch (DocumentValidationException ex) {
                throw new CannotUndoException();
            } finally {
                setUndoEnabled(true);
            }
        }

        public void redo() throws CannotRedoException {
            try {
                setUndoEnabled(false);
                insertFragment(this.offset, this.frag);
            } catch (DocumentValidationException ex) {
                throw new CannotUndoException();
            } finally {
                setUndoEnabled(true);
            }
        }

    }
    
   
    /**
     * Represents an insertion of text into the document.
     */
    private class InsertTextEdit implements IUndoableEdit {

        private int offset;
        private String text;
    
        public InsertTextEdit(int offset, String text) {
            this.offset = offset;
            this.text = text;
        }

        public boolean combine(IUndoableEdit edit) {
            if (edit instanceof InsertTextEdit) {
                InsertTextEdit ite = (InsertTextEdit) edit;
                if (ite.offset == this.offset + this.text.length()) {
                    this.text = this.text + ite.text;
                    return true;
                }
            }
            return false;
        }

        public void undo() throws CannotUndoException {
            try {
                setUndoEnabled(false);
                delete(this.offset, this.offset + this.text.length());
            } catch (DocumentValidationException ex) {
                throw new CannotUndoException();
            } finally {
                setUndoEnabled(true);
            }
        }

        public void redo() throws CannotRedoException {
            try {
                setUndoEnabled(false);
                insertText(this.offset, this.text);
            } catch (DocumentValidationException ex) {
                throw new CannotUndoException();
            } finally {
                setUndoEnabled(true);
            }
        }

    }
    
    /**
     * Assert that the given offset is within the given range,
     * throwing IllegalArgumentException if not.
     */
    private static void assertOffset(int offset, int min, int max) {
        if (offset < min || offset > max) {
            throw new IllegalArgumentException("Bad offset " + offset +
                               "must be between " + min +
                               " and " + max);
        }   
    }



    /**
     * Clone an element tree, pointing to a new Content object.
     * 
     * @param original Element to be cloned
     * @param content new Content object to which the clone will point
     * @param shift amount to shift offsets to be valid in the new Content.
     * @param parent parent for the cloned Element
     */
    private Element cloneElement(
        Element original,
        Content content,
        int shift,
        Element parent) {
            
        Element clone = new Element(original.getName());
        clone.setContent(
            content,
            original.getStartOffset() + shift,
            original.getEndOffset() + shift);
        String[] attrNames = original.getAttributeNames();
        for (int i = 0; i < attrNames.length; i++) {
            try {
                clone.setAttribute(attrNames[i], original.getAttribute(attrNames[i]));
            } catch (DocumentValidationException ex) {
                throw new RuntimeException("Unexpected exception: " + ex);
            }
        }
        clone.setParent(parent);

        Element[] children = original.getChildElements();
        for (int i = 0; i < children.length; i++) {
            Element cloneChild =
                this.cloneElement(children[i], content, shift, clone);
            clone.insertChild(i, cloneChild);
        }

        return clone;
    }
    
    void fireAttributeChanged(DocumentEvent e) {
        this.listeners.fireEvent("attributeChanged", e);
    }
    
    private void fireBeforeContentDeleted(DocumentEvent e) {
        this.listeners.fireEvent("beforeContentDeleted", e);
    }

    private void fireBeforeContentInserted(DocumentEvent e) {
        this.listeners.fireEvent("beforeContentInserted", e);
    }

    private void fireContentDeleted(DocumentEvent e) {
        this.listeners.fireEvent("contentDeleted", e);
    }

    private void fireContentInserted(DocumentEvent e) {
        this.listeners.fireEvent("contentInserted", e);
    }


}
