package com.example.projectmanagepwa.view;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;


@Route
@CssImport("./styles/styles.css")
@UIScope

public class MainView extends VerticalLayout {

    public MainView() {
        ProjectView projectView = new ProjectView();
        setId("main-layout");

        H1 title = new H1("Project Management");
        title.addClassName("title");

        add(title, projectView);
    }
}
