package com.example.projectmanagepwa.view;

import com.example.projectmanagepwa.events.ProjectUpdated;
import com.example.projectmanagepwa.model.Project;
import com.example.projectmanagepwa.model.SqlDateToLocalDateConverter;
import com.example.projectmanagepwa.service.ProjectService;
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
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.validator.StringLengthValidator;

import java.util.Optional;

public class ProjectForm extends FlexLayout {

    private final ProjectService projectService;
    private TextField name = new TextField("Name");
    private TextField description = new TextField("Description");

    private DatePicker dateStarted = new DatePicker("Date Started");

    private DatePicker dateEnded = new DatePicker("Date Ended");

    private ComboBox<String> priority = new ComboBox<>("Priority");
    private Button saveButton = new Button("Save");

    private Button deleteButton = new Button("Delete");

    private HorizontalLayout priorityLayout = new HorizontalLayout();
    private HorizontalLayout buttonLayout = new HorizontalLayout();

    private Button closeButton = new Button(new Icon(VaadinIcon.CLOSE));
    private Binder<Project> binder = new Binder<>(Project.class);

    public ProjectForm(ProjectService projectService) {
        this.projectService = projectService;
        binder.forField(name)
                .asRequired("Name cannot be empty")
                .withValidator(new StringLengthValidator(
                        "Name must be between 1 and 255 characters",
                        1, 255))
                .bind(Project::getName, Project::setName);

        binder.forField(description)
                .asRequired("Description cannot be empty")
                .withValidator(new StringLengthValidator(
                        "Description must be between 1 and 1000 characters",
                        1, 1000))
                .bind(Project::getDescription, Project::setDescription);


        binder.forField(dateStarted)
                .withConverter(new SqlDateToLocalDateConverter())
                .asRequired("Date Started cannot be empty")
                .bind(Project::getDateStarted, Project::setDateStarted);

        binder.forField(dateEnded)
                .withConverter(new SqlDateToLocalDateConverter())
                .asRequired("Date Ended cannot be empty")
                .bind(Project::getDateEnded, Project::setDateEnded);

        binder.forField(priority)
                .asRequired("Priority cannot be empty")
                .bind(Project::getPriority, Project::setPriority);

        binder.bindInstanceFields(this);
        priority.setItems("High", "Medium", "Low");

        saveButton.addClickListener(event -> onSave());
        deleteButton.addClickListener(event -> onDelete());
        closeButton.addClickListener(event -> onClose());

        saveButton.addClassName("button-primary");
        deleteButton.addClassName("button-error");
        closeButton.addClassName("button-close");

        setFlexDirection(FlexDirection.COLUMN);
        setAlignItems(Alignment.CENTER);

        add(closeButton,name, description, dateStarted, dateEnded, priority, saveButton, deleteButton);
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

