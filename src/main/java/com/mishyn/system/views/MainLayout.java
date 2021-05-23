package com.mishyn.system.views;

import com.mishyn.system.entity.WorkSpaceEntity;
import com.mishyn.system.service.WorkspaceService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;
import org.apache.commons.lang3.StringUtils;

@CssImport(value = "./styles/shared-styles.css")
@CssImport(value = "./styles/drawer-styles.css", themeFor = "vaadin-app-layout")
public class MainLayout extends AppLayout {

    private final WorkspaceService workspaceService;
    private final Button add = new Button(new Icon(VaadinIcon.CHECK));
    private final Button cancel = new Button(new Icon(VaadinIcon.CLOSE));
    private final Button addWorkspaceButton = new Button(new Icon(VaadinIcon.PLUS));
    private final VerticalLayout workspaces = new VerticalLayout();

    public MainLayout(WorkspaceService workspaceService) {
        this.workspaceService = workspaceService;
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        H1 logo = new H1("Ticketing system");
        logo.addClassName("logo");
        logo.getElement().getStyle().set("cursor", "pointer");
        logo.addClickListener(e -> UI.getCurrent().getPage().setLocation("/"));
        Button logout = new Button("Log out", event1 -> UI.getCurrent().getPage().setLocation("/logout"));
        logout.addClassName("logout");
        logout.getElement().getStyle().set("margin-left", "auto");

        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), logo, logout);
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.setWidth("100%");
        header.addClassName("header");

        addToNavbar(header);
    }

    private void createDrawer() {
        workspaces.setSpacing(false);
        workspaces.setMargin(false);

        HorizontalLayout addNewWorkspace = new HorizontalLayout();
        TextField newWorkspaceName = new TextField();
        newWorkspaceName.setPlaceholder("New workspace name");

        VerticalLayout workspaceLinks = new VerticalLayout();
        workspaceLinks.add(
                workspaceService.findAll().stream()
                        .map(workSpaceEntity -> createWorkspace(workSpaceEntity.getName(), workSpaceEntity.getId()))
                        .toArray(RouterLink[]::new)
        );
        workspaceLinks.setAlignItems(FlexComponent.Alignment.CENTER);

        add.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        add.addClickListener(event -> {
            final String workspaceName = newWorkspaceName.getValue();
            if (!StringUtils.isBlank(workspaceName)) {
                WorkSpaceEntity workSpaceEntity = workspaceService.saveNewWorkspace(workspaceName);
                workspaceLinks.add(createWorkspace(workSpaceEntity.getName(), workSpaceEntity.getId()));
                addWorkspaceButton.setVisible(true);
                addNewWorkspace.setVisible(false);
                UI.getCurrent().getPage().reload();
            }
        });

        cancel.addThemeVariants(ButtonVariant.LUMO_ERROR);
        cancel.addClickListener(event -> {
            newWorkspaceName.clear();
            addWorkspaceButton.setVisible(true);
            addNewWorkspace.setVisible(false);
        });

        addNewWorkspace.add(newWorkspaceName, add, cancel);
        addNewWorkspace.setVisible(false);


        addWorkspaceButton.addClickListener(e ->{
            addWorkspaceButton.setVisible(false);
            addNewWorkspace.setVisible(true);
        });
        addWorkspaceButton.addThemeVariants(ButtonVariant.LUMO_ICON);
        HorizontalLayout workspaceHeader = new HorizontalLayout(
                new H3("Workspaces"),
                addWorkspaceButton
        );
        workspaceHeader.setAlignItems(FlexComponent.Alignment.BASELINE);
        workspaceHeader.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        workspaceHeader.setHeight("auto");
        workspaceHeader.setWidthFull();



        workspaces.add(workspaceHeader);
        workspaces.add(addNewWorkspace);
        workspaces.add(workspaceLinks);

        addToDrawer(workspaces);
        setDrawerOpened(false);
    }

    private RouterLink createWorkspace(String name, Long id) {
        RouterLink listLink = new RouterLink(name, ListView.class);
        listLink.getElement().setAttribute("href", "workspace/" + id);
        listLink.setHighlightCondition(HighlightConditions.sameLocation());
        return listLink;
    }
}