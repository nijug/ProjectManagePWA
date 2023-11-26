package com.example.projectmanagepwa.events;

import com.example.projectmanagepwa.view.ProjectForm;
import com.vaadin.flow.component.ComponentEvent;

public class ProjectUpdated extends ComponentEvent<ProjectForm> {
    /**
     * Creates a new event using the given source and indicator whether the
     * event originated from the client side or the server side.
     *
     * @param source     the source component
     * @param fromClient <code>true</code> if the event originated from the client
     *                   side, <code>false</code> otherwise
     */
    public ProjectUpdated(ProjectForm source, boolean fromClient) {
        super(source, fromClient);
    }
}
