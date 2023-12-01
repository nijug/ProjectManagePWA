package com.example.projectmanagepwa.view;

import com.vaadin.flow.spring.annotation.UIScope;
import org.ProjectService.Project;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;


@Route
@UIScope
public class ProjectView extends VerticalLayout {


    private final CardGrid cardGrid;


    public ProjectView() {

        cardGrid = new CardGrid();
        Button addButton = new Button("Add");
        addButton.addClickListener(event -> onAdd());

        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        Button previousButton = new Button("Previous");
        previousButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        Button nextButton = new Button("Next");
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

        cardGrid.updateGrid();
    }

    private void onAdd() {
        Project newProject = new Project();
        ProjectForm addForm = new ProjectForm();
        addForm.setProject(newProject);
        Dialog addDialog = new Dialog(addForm);
        addDialog.addDialogCloseActionListener(event -> cardGrid.updateGrid());
        addDialog.open();
    }
}
