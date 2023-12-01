package com.example.projectmanagepwa.view;

import com.example.projectmanagepwa.events.ProjectUpdated;
import com.example.projectmanagepwa.model.SqlDateToLocalDateConverter;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexLayout;

import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import org.ProjectService.Project;
import org.ProjectService.ProjectService;
import com.vaadin.flow.data.validator.StringLengthValidator;



import java.util.Optional;
@Route
@UIScope
public class ProjectForm extends FlexLayout {

    private final ProjectService projectService;
    private final Button deleteButton = new Button("Delete");

    private final Binder<Project> binder = new Binder<>(Project.class);

    public ProjectForm() {
        projectService = ProjectService.getInstance();
        TextField name = new TextField("Name");
        binder.forField(name)
                .asRequired("Name cannot be empty")
                .withValidator(new StringLengthValidator(
                        "Name must be between 1 and 255 characters",
                        1, 255))
                .bind(Project::getName, Project::setName);

        TextField description = new TextField("Description");
        binder.forField(description)
                .asRequired("Description cannot be empty")
                .withValidator(new StringLengthValidator(
                        "Description must be between 1 and 1000 characters",
                        1, 1000))
                .bind(Project::getDescription, Project::setDescription);


        DatePicker dateStarted = new DatePicker("Date Started");
        binder.forField(dateStarted)
                .withConverter(new SqlDateToLocalDateConverter())
                .asRequired("Date Started cannot be empty")
                .bind(Project::getDateStarted, Project::setDateStarted);

        DatePicker dateEnded = new DatePicker("Date Ended");
        binder.forField(dateEnded)
                .withConverter(new SqlDateToLocalDateConverter())
                .asRequired("Date Ended cannot be empty")
                .bind(Project::getDateEnded, Project::setDateEnded);

        ComboBox<String> priority = new ComboBox<>("Priority");
        binder.forField(priority)
                .asRequired("Priority cannot be empty")
                .bind(Project::getPriority, Project::setPriority);

        binder.bindInstanceFields(this);
        priority.setItems("High", "Medium", "Low");

        Button saveButton = new Button("Save");
        saveButton.addClickListener(event -> onSave());
        deleteButton.addClickListener(event -> onDelete());
        Button closeButton = new Button(new Icon(VaadinIcon.CLOSE));
        closeButton.addClickListener(event -> onClose());

        saveButton.addClassName("button-primary");
        deleteButton.addClassName("button-error");
        closeButton.addClassName("button-close");

        setFlexDirection(FlexDirection.COLUMN);
        setAlignItems(Alignment.CENTER);

        add(closeButton, name, description, dateStarted, dateEnded, priority, saveButton, deleteButton);
    }

    private void onDelete() {
        Project project = binder.getBean();
        projectService.deleteProjects(project.getId());
        Notification.show("Project deleted");
        binder.setBean(null);

        this.fireRefreshEvent();

        closeForm();
    }

    private void fireRefreshEvent() {
        ComponentUtil.fireEvent(UI.getCurrent(), new ProjectUpdated(this, true));
    }


    private void onSave() {
        BinderValidationStatus<Project> validationResult = binder.validate();
        if (validationResult.isOk()) {
            Project project = binder.getBean();
            if (project != null) {
                if (project.getId() != null) {
                    projectService.updateProjects(project);
                    Notification.show("Project updated");
                } else {
                    projectService.addProjects(project);
                    Notification.show("Project added");
                }
                binder.setBean(new Project());
                this.fireRefreshEvent();
            }
            closeForm();
        } else {
            validationResult.getValidationErrors().forEach(error ->
                    Notification.show(error.getErrorMessage()));
        }
    }

    private void onClose() {
        closeForm();
    }

    public void setProject(Project project) {
        binder.setBean(project);

        boolean isEditing = project.getId() != null;
        deleteButton.setEnabled(isEditing);
        deleteButton.setVisible(isEditing);

    }

    private void closeForm() {
        this.setVisible(false);
        Optional<Component> parentOptional = getParent();
        if (parentOptional.isPresent() && parentOptional.get() instanceof Dialog dialog) {
            dialog.close();
        }
    }
}

