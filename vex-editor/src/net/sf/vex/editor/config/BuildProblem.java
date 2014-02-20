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
package net.sf.vex.editor.config;

/**
 * Represents a problem found when parsing a configuration resource.
 */
public class BuildProblem {

    public static final int SEVERITY_ERROR = 1;
    public static final int SEVERITY_WARNING = 2;

    /**
     * Class constructor.
     */
    public BuildProblem() {
        
    }

    /**
     * Class constructor.
     * @param severity Severity of the problem: SEVERITY_WARNING or SEVERITY_ERROR
     * @param resourcePath Path of the resource being built, relative to its
     * plugin or project.
     * @param message Description of the problem.
     * @param lineNumber Line number on which the problem occurred, or -1 if unknown.
     */
    public BuildProblem(int severity, String resourcePath, String message, int lineNumber) {
        this.severity = severity;
        this.resourcePath = resourcePath;
        this.message = message;
        this.lineNumber = lineNumber;
    }
    
    /**
     * Returns the line number on which the error occurred, or -1 if no
     * line number can be identified.
     */
    public int getLineNumber() {
        return lineNumber;
    }
    
    /**
     * Returns the message describing the problem.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Returns the path of the resource relative to the project root.
     */
    public String getResourcePath() {
        return resourcePath;
    }

    /**
     * Returns the severity of the problem, either SEVERITY_ERROR or 
     * SEVERITY_WARNING.
     */
    public int getSeverity() {
        return severity;
    }
    
    /**
     * Sets the line number of the problem.
     * @param lineNumber Line number on which the problem occurred.
     */
    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }
    
    /**
     * Sets the message describing the problem.
     * @param message Message describing the problem.
     */
    public void setMessage(String message) {
        this.message = message;
    }
    
    /**
     * Sets the path of the resource that had the problem.
     * @param resourcePath Path of the resource, relative to the project root.
     */
    public void setResourcePath(String resourcePath) {
        this.resourcePath = resourcePath;
    }
    
    /**
     * Sets the severity of the problem.
     * @param severity Severity of the problem. Should be one of
     * SEVERITY_ERROR or SEVERITY_WARNING.
     */
    public void setSeverity(int severity) {
        this.severity = severity;
    }
    //==================================================== PRIVATE
    
    private String resourcePath;
    private int severity;
    private String message;
    private int lineNumber;
}
