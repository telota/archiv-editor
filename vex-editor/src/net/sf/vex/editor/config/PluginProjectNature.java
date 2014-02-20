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

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;

/**
 * Project nature that defines Vex Plugin projects.
 */
public class PluginProjectNature implements IProjectNature {
    
    public static final String ID = "net.sf.vex.editor.pluginNature"; //$NON-NLS-1$

    public void configure() throws CoreException {
        this.registerBuilder();
    }

    public void deconfigure() throws CoreException {
        //System.out.println("deconfiguring " + project.getName());
        project.deleteMarkers(IMarker.PROBLEM, true, 1);
    }

    public IProject getProject() {
        return this.project;
    }

    public void setProject(IProject project) {
        this.project = project;
    }

    //====================================================== PRIVATE

    private IProject project;
    
    
    private void registerBuilder() throws CoreException {
        IProjectDescription desc = project.getDescription();
        ICommand[] commands = desc.getBuildSpec();
        boolean found = false;

        for (int i = 0; i < commands.length; ++i) {
           if (commands[i].getBuilderName().equals(PluginProjectBuilder.ID)) {
              found = true;
              break;
           }
        }
        if (!found) { 
           //add builder to project
           ICommand command = desc.newCommand();
           command.setBuilderName(PluginProjectBuilder.ID);
           ICommand[] newCommands = new ICommand[commands.length + 1];

           // Add it before other builders.
           System.arraycopy(commands, 0, newCommands, 1, commands.length);
           newCommands[0] = command;
           desc.setBuildSpec(newCommands);
           project.setDescription(desc, null);
        }


    }
}
