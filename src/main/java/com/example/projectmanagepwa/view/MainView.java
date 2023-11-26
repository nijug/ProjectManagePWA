package com.example.projectmanagepwa.view;

import com.example.projectmanagepwa.service.ProjectService;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;


@Route
@CssImport("./styles/styles.css")
public class MainView extends VerticalLayout {
    private ProjectService projectService;
    public MainView(ProjectService projectService) {
        setId("main-layout");
        this.projectService = projectService;
        ProjectView projectView = new ProjectView(projectService);

        H1 title = new H1("Project Management");
        title.addClassName("title");

        add(title, projectView);


    }
}
