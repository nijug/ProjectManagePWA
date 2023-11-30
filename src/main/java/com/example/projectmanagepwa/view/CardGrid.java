package com.example.projectmanagepwa.view;

import com.example.projectmanagepwa.events.ProjectUpdated;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.spring.annotation.UIScope;
import jakarta.annotation.PostConstruct;
import org.ProjectService.Project;
import org.ProjectService.ProjectService;

import java.util.Arrays;
import java.util.List;
@Route
@UIScope
public class CardGrid extends VerticalLayout {

    private final ProjectService projectService;
    private Registration registration;

    private int currentPage = 0;

    public CardGrid(ProjectService projectService) {
        this.projectService = projectService;
        setAlignItems(Alignment.CENTER);
        setMargin(true);
    }

    @PostConstruct
    public void init() {
        updateGrid();
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        registration = ComponentUtil.addListener(
                attachEvent.getUI(),
                ProjectUpdated.class,
                event -> updateGrid()
        );
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        super.onDetach(detachEvent);
        registration.remove();
    }

    public void nextPage() {
        if (hasNextPage()) {
            currentPage++;
            updateGrid();
        }
    }

    public void previousPage() {
        if (hasPreviousPage()) {
            currentPage--;
            updateGrid();
        }
    }

    private boolean hasNextPage() {
        int startIndex = (currentPage + 1) * 4;
        return startIndex < projectService.readProjects().length;
    }

    private boolean hasPreviousPage() {
        return currentPage > 0;
    }

    void updateGrid() {
        removeAll();
        List<Project> projects = Arrays.stream(projectService.readProjects())
                .skip(currentPage * 4L)
                .limit(4)
                .toList();

        for (int i = 0; i < 2; i++) {
            HorizontalLayout rowLayout = new HorizontalLayout();
            rowLayout.setWidthFull();
            rowLayout.setAlignItems(Alignment.CENTER);
            rowLayout.setJustifyContentMode(JustifyContentMode.CENTER);

            for (int j = 0; j < 2; j++) {
                int index = i * 2 + j;
                if (index < projects.size()) {
                    Component card = createProjectCard(projects.get(index));
                    rowLayout.add(card);
                } else {
                    rowLayout.add(new Div());
                }
            }

            add(rowLayout);
        }
    }

    private Component createProjectCard(Project project) {
        VerticalLayout cardLayout = new VerticalLayout();
        cardLayout.addClassName("project-card");
        H3 projectName = new H3(project.getName());
        projectName.addClassName("project-name");
        Paragraph projectDescription = new Paragraph("Description: " + project.getDescription());
        projectDescription.addClassName("project-description");
        Paragraph dateStarted = new Paragraph("Start Date: " + project.getDateStarted().toString());
        dateStarted.addClassName("project-date");
        Paragraph dateEnded = new Paragraph("End Date: " + project.getDateEnded().toString());
        dateEnded.addClassName("project-date");
        Paragraph priority = new Paragraph("Priority: " + project.getPriority());
        priority.addClassName("project-priority");

        cardLayout.add(projectName, projectDescription, dateStarted, dateEnded, priority);
        cardLayout.addClickListener(event -> openFormForUpdate(project));

        return cardLayout;
    }

    private void openFormForUpdate(Project project) {
        ProjectForm updateForm = new ProjectForm(projectService);
        updateForm.setProject(project);

        Dialog dialog = new Dialog(updateForm);
        dialog.open();
    }

}
