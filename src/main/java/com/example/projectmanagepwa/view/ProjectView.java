package com.example.projectmanagepwa.view;

import com.example.projectmanagepwa.model.Project;
import com.example.projectmanagepwa.service.ProjectService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.Route;

import java.util.Arrays;

@Route
public class ProjectView extends VerticalLayout {

    private ProjectService projectService;
    private CardGrid cardGrid;
    private Button addButton = new Button("Add");
    private Button previousButton = new Button("Previous");
    private Button nextButton = new Button("Next");

    public ProjectView(ProjectService projectService) {
        this.projectService = projectService;
        cardGrid = new CardGrid(projectService);
        addButton.addClickListener(event -> onAdd());

        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        previousButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        nextButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        addButton.addClassName("button-primary");
        previousButton.addClassName("button-primary");
        nextButton.addClassName("button-primary");

        previousButton.addClickListener(event -> cardGrid.previousPage());
        nextButton.addClickListener(event -> cardGrid.nextPage());

        HorizontalLayout cardLayout = new HorizontalLayout(cardGrid);
        cardLayout.setSizeFull();
        cardLayout.setAlignItems(Alignment.CENTER);
        cardLayout.setJustifyContentMode(JustifyContentMode.CENTER);


        HorizontalLayout buttonLayout = new HorizontalLayout(previousButton, addButton, nextButton);
        buttonLayout.setSizeFull();
        buttonLayout.setAlignItems(Alignment.CENTER);
        buttonLayout.setJustifyContentMode(JustifyContentMode.CENTER);


        VerticalLayout mainLayout = new VerticalLayout(cardLayout, buttonLayout);
        mainLayout.setSizeFull();
        mainLayout.setAlignItems(Alignment.CENTER);
        mainLayout.setJustifyContentMode(JustifyContentMode.CENTER);

        add(mainLayout);

        loadProjects();
    }

    private void loadProjects() {
        System.out.println("loadProjects");
        cardGrid.updateGrid();
    }

    private void onAdd() {
        Project newProject = new Project();
        ProjectForm addForm = new ProjectForm(projectService);
        addForm.setProject(newProject);
        Dialog addDialog = new Dialog(addForm);
        addDialog.addDialogCloseActionListener(event -> loadProjects());
        addDialog.open();
    }
}
