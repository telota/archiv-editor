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

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;

/**
 * Writes a document to an output stream, using a stylesheet to provide 
 * formatting hints.
 *
 * <ul>
 * <li>Children of an element are indented by a configurable amount.</li>
 * <li>Text is wrapped to fit within a configurable width.<li>
 * </ul>
 *
 * <p>Documents are currently saved UTF-8 encoding, with no encoding
 * specified in the XML declaration.</p>
 */
public class DocumentWriter {

    private IWhitespacePolicy whitespacePolicy;
    private String indent;
    private int wrapColumn;

    /**
     * Class constructor.
     */
    public DocumentWriter() {
	this.indent = "  ";
	this.wrapColumn = 72;
    }

    /**
     * Escapes special XML characters. Changes '<', '>', and '&' to
     * '&lt;', '&gt;' and '&amp;', respectively.
     *
     * @param s the string to be escaped.
     * @return the escaped string
     */
    public static String escape(String s) {
	StringBuffer sb = new StringBuffer(s.length());
	
	for (int i = 0; i < s.length(); i++) {
	    char c = s.charAt(i);
	    if (c == '<') {
		sb.append("&lt;");
	    } else if (c == '>') {
		sb.append("&gt;");
	    } else if (c == '&') {
		sb.append("&amp;");
        } else if (c == '"') {
            sb.append("&quot;");
        } else if (c == '\'') {
            sb.append("&apos;");
	    } else {
		sb.append(c);
	    }
	}
	return sb.toString();
    }

    /**
     * Returns the indent string. By default this is two spaces.
     */
    public String getIndent() {
	return this.indent;
    }

    /**
     * Returns the whitespace policy used by this writer.
     */
    public IWhitespacePolicy getWhitespacePolicy() {
        return whitespacePolicy;
    }
    
    /**
     * Returns the column at which text should be wrapped. By default this
     * is 72.
     */
    public int getWrapColumn() {
	return this.wrapColumn;
    }

    /**
     * Sets the value of the indent string.
     *
     * @param indent new value for the indent string.
     */
    public void setIndent(String indent) {
	this.indent = indent;
    }

    /**
     * Sets the whitespace policy for this writer. The whitespace policy tells
     * the writer which elements are block-formatted and which are pre-formatted.
     * 
     * @param whitespacePolicy The whitespacePolicy to set.
     */
    public void setWhitespacePolicy(IWhitespacePolicy whitespacePolicy) {
        this.whitespacePolicy = whitespacePolicy;
    }

    /**
     * Sets the value of the wrap column.
     *
     * @param wrapColumn new value for the wrap column.
     */
    public void setWrapColumn(int wrapColumn) {
	this.wrapColumn = wrapColumn;
    }

    public void write(Document doc, OutputStream os)
	throws IOException {

	OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
	PrintWriter pw = new PrintWriter(osw);
	pw.println("<?xml version='1.0'?>");
	if (doc.getSystemID() != null) {
	    StringBuffer sb = new StringBuffer();
            sb.append("<!DOCTYPE ");
            sb.append(doc.getRootElement().getName());
            if (doc.getPublicID() != null) {
                sb.append(" PUBLIC");
                sb.append(" \"");
                sb.append(doc.getPublicID());
                sb.append("\"");
            } else {
                sb.append(" SYSTEM");
            } 
            sb.append(" \"");
            sb.append(doc.getSystemID());
            sb.append("\">");
            pw.println(sb.toString());
	}
	this.writeNode(doc.getRootElement(), pw, "");
	pw.flush();
    }


    //====================================================== PRIVATE


    private void writeNode(Node node, 
			   PrintWriter pw,
			   String indent) {

	if (node instanceof Text) {
	    TextWrapper wrapper = new TextWrapper();
	    wrapper.add(escape(node.getText()));

	    String[] lines = wrapper.wrap(this.wrapColumn - indent.length());

	    for (int i = 0; i < lines.length; i++) {
		pw.print(indent);
		pw.println(lines[i]);
	    }

	} else {

	    Element element = (Element) node;

	    if (this.whitespacePolicy != null && this.whitespacePolicy.isPre(element)) {
		pw.print(indent);
		writeNodeNoWrap(node, pw);
		pw.println();
		return;
	    }

	    boolean hasBlockChild = false;
	    Element[] children = element.getChildElements();
	    for (int i = 0; i < children.length; i++) {
                if (this.whitespacePolicy != null && this.whitespacePolicy.isBlock(children[i])) {
		    hasBlockChild = true;
		    break;
		}
	    }

	    if (hasBlockChild) {
		pw.print(indent);
		pw.print("<");
		pw.print(element.getName());

		TextWrapper wrapper = new TextWrapper();
		wrapper.addNoSplit(this.getAttributeString(element));
		int outdent = indent.length() + 1 + element.getName().length();
		String[] lines = wrapper.wrap(this.wrapColumn - outdent);
		char[] bigIndent = new char[outdent];
		Arrays.fill(bigIndent, ' ');
		for (int i = 0; i < lines.length; i++) {
		    if (i > 0) {
			pw.print(bigIndent);
		    }
		    pw.print(lines[i]);
		    if (i < lines.length - 1) {
			pw.println();
		    }
		}
		pw.println(">");

		String childIndent = indent + this.indent;
		Node[] content = element.getChildNodes();
		for (int i = 0; i < content.length; i++) {
		    this.writeNode(content[i], pw, childIndent);
		}
		pw.print(indent);
		pw.print("</");
		pw.print(element.getName());
		pw.println(">");
	    } else {
		TextWrapper wrapper = new TextWrapper();
		this.addNode(element, wrapper);
		String[] lines = wrapper.wrap(this.wrapColumn-indent.length());
		for (int i = 0; i < lines.length; i++) {
		    pw.print(indent);
		    pw.println(lines[i]);
		}
	    }
		
	}
    }
   
    private void writeNodeNoWrap(Node node, PrintWriter pw) {

	if (node instanceof Text) {
	    pw.print(escape(node.getText()));
	} else {

	    Element element = (Element) node;

	    pw.print("<");
	    pw.print(element.getName());
	    pw.print(this.getAttributeString(element));
	    pw.print(">");

	    Node[] content = element.getChildNodes();
	    for (int i = 0; i < content.length; i++) {
		this.writeNodeNoWrap(content[i], pw);
	    }

	    pw.print("</");
	    pw.print(element.getName());
	    pw.print(">");
	}
    }
   

    private String attrToString(String name, String value) {
	StringBuffer sb = new StringBuffer();
	sb.append(" ");
	sb.append(name);
	sb.append("=\"");
	sb.append(escape(value));
	sb.append("\"");
	return sb.toString();
    }

    private void addNode(Node node, TextWrapper wrapper) {
	if (node instanceof Text) {
	    wrapper.add(escape(node.getText()));
	} else {
	    Element element = (Element)node;
	    Node[] content = element.getChildNodes();
	    String[] attrs = element.getAttributeNames();
	    Arrays.sort(attrs);

	    if (attrs.length == 0) {
		if (content.length == 0) {
		    wrapper.add("<" + element.getName() + " />");
		} else {
		    wrapper.add("<" + element.getName() + ">");
		}
	    } else {
	        Validator validator = element.getDocument().getValidator();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < attrs.length; i++) {
		    sb.setLength(0);
		    if (i == 0) {
			sb.append("<" + element.getName());
		    }
		    if (!attrHasDefaultValue(validator, element, attrs[i])) {
		        sb.append(attrToString(attrs[i], element.getAttribute(attrs[i])));
		    }
		    if (i == attrs.length - 1) {
			if (content.length == 0) {
			    sb.append("/>");
			} else {
			    sb.append(">");
			}
		    }
		    wrapper.addNoSplit(sb.toString());
		}
	    }

	    for (int i = 0; i < content.length; i++) {
		addNode(content[i], wrapper);
	    }

	    if (content.length > 0) {
		wrapper.add("</" + element.getName() + ">");
	    }
	}
    }

    private String getAttributeString(Element element) {
        
        Validator validator = element.getDocument().getValidator();
        
	String[] attrs = element.getAttributeNames();
	Arrays.sort(attrs);
	StringBuffer sb = new StringBuffer();
	for (int i = 0; i < attrs.length; i++) {
	    if (attrHasDefaultValue(validator, element, attrs[i])) {
	        continue;
	    }
	    sb.append(" ");
	    sb.append(attrs[i]);
	    sb.append("=\"");
	    sb.append(escape(element.getAttribute(attrs[i])));
	    sb.append("\"");
	}
	return sb.toString();
    }

    private static boolean attrHasDefaultValue(Validator validator, Element element, String attribute) {
        if (validator != null) {
            AttributeDefinition ad = validator.getAttributeDefinition(element.getName(), attribute);
            if (ad != null) {
                String value = element.getAttribute(attribute);
                String defaultValue = ad.getDefaultValue();
                return value != null && value.equals(defaultValue);
            }
        }
        return false;
    }
}

