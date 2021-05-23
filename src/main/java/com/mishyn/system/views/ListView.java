package com.mishyn.system.views;

import com.mishyn.system.entity.CommentEntity;
import com.mishyn.system.entity.TicketEntity;
import com.mishyn.system.entity.UserEntity;
import com.mishyn.system.entity.WorkSpaceEntity;
import com.mishyn.system.service.CommentService;
import com.mishyn.system.service.TicketService;
import com.mishyn.system.service.UserService;
import com.mishyn.system.service.WorkspaceService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Route(value="workspace", layout = MainLayout.class)
@CssImport(value = "./styles/shared-styles.css")
@CssImport(value = "./styles/drawer-styles.css", themeFor = "vaadin-app-layout")
@Slf4j
public class ListView extends VerticalLayout implements HasDynamicTitle, HasUrlParameter<Long> {

    private final WorkspaceService workspaceService;
    private final TicketService ticketService;
    private final UserService userService;
    private final CommentService commentService;

    private String title = "Workspace";
    private WorkSpaceEntity workSpaceEntity;
    private H2 header = new H2("Workspace");
    private Grid<TicketEntity> grid = new Grid<>(TicketEntity.class);

    private TicketEntity currentTicket;

    private Dialog ticketDialog = new Dialog();
    private Button closeButton = new Button(new Icon(VaadinIcon.CLOSE));
    private H3 workspaceHeader = new H3();
    private TextField ticketTitle = new TextField("Title");
    private TextArea ticketBody = new TextArea("Body");
    private DateTimePicker dueDate = new DateTimePicker("Due date");
    private ComboBox<TicketEntity.TicketStatus> statusSelect = new ComboBox<>();
    private ComboBox<UserEntity> assigneeSelect = new ComboBox<>();
    private Button saveButton = new Button("Update", event -> updateTicket(currentTicket));
    private TextArea commentBody = new TextArea("Comment");
    private Button addComment = new Button("Add Comment");
    private VerticalLayout verticalLayout = new VerticalLayout(commentBody, addComment);

    private Dialog createTicketDialog = new Dialog();
    private Button createCloseButton = new Button(new Icon(VaadinIcon.CLOSE));
    private H3 createWorkspaceHeader = new H3();
    private TextField createTicketTitle = new TextField("Title");
    private TextArea createTicketBody = new TextArea("Body");
    private DateTimePicker createDueDate = new DateTimePicker("Due date");
    private ComboBox<TicketEntity.TicketStatus> createStatusSelect = new ComboBox<>();
    private ComboBox<UserEntity> createAssigneeSelect = new ComboBox<>();
    private Button createSaveButton = new Button("Save", e -> {
        createTicket();
        createTicketDialog.close();
        UI.getCurrent().getPage().reload();
    });
    private Button createTicket = new Button("Create Ticket", clickEvent -> {
        openCreateDialog(new TicketEntity());
        createTicketDialog.open();
    });
    private HorizontalLayout listHead = new HorizontalLayout(header, createTicket);
    private Span commentsHeader = new Span("\nComments:");
    private VerticalLayout comments = new VerticalLayout();

    public ListView(WorkspaceService workspaceService,
                    TicketService ticketService,
                    UserService userService,
                    CommentService commentService) {
        this.workspaceService = workspaceService;
        this.ticketService = ticketService;
        this.userService = userService;
        this.commentService = commentService;

        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        workspaceHeader.getElement().getStyle().set("margin-top", "0");
        createWorkspaceHeader.getElement().getStyle().set("margin-top", "0");
        createTicket.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        createTicket.getElement().getStyle().set("margin-left", "auto");

        listHead.setAlignItems(Alignment.BASELINE);
        listHead.setWidthFull();
        add(listHead);
        add(grid);
        grid.getDataProvider().refreshAll();
        setUpDialog();
        setUpCreateDialog();
    }

    private void setUpDialog() {
        commentsHeader.getStyle().set("font-size", "1.4em");
        commentsHeader.getStyle().set("font-weight", "bold");
        closeButton.addClickListener(event -> ticketDialog.close());
        closeButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        closeButton.getStyle().set("margin-left", "auto");

        HorizontalLayout close = new HorizontalLayout(closeButton);
        close.getStyle().set("margin-right", "auto");
        ticketDialog.add(close);
        ticketDialog.add(new HorizontalLayout(workspaceHeader));
        ticketDialog.add(new HorizontalLayout(ticketTitle));

        statusSelect.setLabel("Status");
        statusSelect.setItems(DataProvider.fromStream(
                Arrays.stream(TicketEntity.TicketStatus.values())
        ));
        statusSelect.setItemLabelGenerator(ticketStatus -> ticketStatus.toString().toLowerCase());
        ticketDialog.add(new HorizontalLayout(statusSelect));

        assigneeSelect.setLabel("Assignee");
        assigneeSelect.setItems(DataProvider.fromStream(
                userService.findAll().stream()
        ));
        assigneeSelect.setItemLabelGenerator(UserEntity::getUsername);
        ticketDialog.add(new HorizontalLayout(assigneeSelect));

        ticketDialog.add(new HorizontalLayout(dueDate));
        ticketBody.setWidth("30em");
        ticketBody.getStyle().set("height", "20em");

        ticketDialog.add(new HorizontalLayout(ticketBody));

        ticketDialog.add(saveButton);
        commentBody.setWidth("30em");
        commentBody.setPlaceholder("Add your comment...");
        addComment.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        verticalLayout.getStyle().set("padding", "inherit");
        addComment.addClickListener(e -> {
            addComment();
            commentBody.clear();
            comments.removeAll();
            comments.add(commentService.findByTicketId(currentTicket.getId()).stream().map(this::createCommentRepresentation).toArray(VerticalLayout[]::new));
        });
        ticketDialog.add(verticalLayout);

        saveButton.getStyle().set("margin-top", "1em");
        saveButton.getStyle().set("margin-bottom", "1em");
        ticketDialog.add(commentsHeader);
        ticketDialog.add(comments);
    }

    private void setUpCreateDialog() {
        createCloseButton.addClickListener(event -> createTicketDialog.close());
        createCloseButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        createCloseButton.getElement().getStyle().set("margin-left", "auto");
        createCloseButton.getElement().getStyle().set("margin-top", "auto");

        createTicketDialog.add(new HorizontalLayout(createCloseButton));
        createTicketDialog.add(new HorizontalLayout(createWorkspaceHeader));
        createTicketDialog.add(new HorizontalLayout(createTicketTitle));

        createStatusSelect.setLabel("Status");
        createStatusSelect.setItems(DataProvider.fromStream(
                Arrays.stream(TicketEntity.TicketStatus.values())
        ));
        createStatusSelect.setItemLabelGenerator(ticketStatus -> ticketStatus.toString().toLowerCase());
        createTicketDialog.add(new HorizontalLayout(createStatusSelect));

        createAssigneeSelect.setLabel("Assignee");
        createAssigneeSelect.setItems(DataProvider.fromStream(
                userService.findAll().stream()
        ));
        createAssigneeSelect.setItemLabelGenerator(UserEntity::getUsername);
        createTicketDialog.add(new HorizontalLayout(createAssigneeSelect));

        createTicketDialog.add(new HorizontalLayout(createDueDate));
        createTicketBody.setWidth("30em");
        createTicketBody.getStyle().set("height", "20em");

        createTicketDialog.add(new HorizontalLayout(createTicketBody));
        createTicketDialog.add(new HorizontalLayout(createSaveButton));
    }

    @Override
    public String getPageTitle() {
        return title;
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter Long parameter) {
        this.workSpaceEntity = workspaceService.getById(parameter);
        if (parameter == null || this.workSpaceEntity == null) {
            UI.getCurrent().getPage().setLocation("/");
            return;
        }
        setUpGrid();
        this.title = workSpaceEntity.getName();
        this.grid.setItems(workSpaceEntity.getTicketEntities());

        header.setText(title);
    }

    private void setUpGrid() {
        grid.removeAllColumns();

        grid.addColumns("id", "title");

        grid.addColumn(ticketEntity -> {
            UserEntity assignee = ticketEntity.getAssignee();
            return assignee == null ?
                    "" :
                    assignee.getUsername() == null ? "" : assignee.getUsername();
        }).setHeader("Assignee").setSortable(true);

        grid.addColumn(ticketEntity -> {
            UserEntity createdBy = ticketEntity.getCreatedBy();
            return createdBy == null ?
                    "" :
                    createdBy.getUsername() == null ? "" : createdBy.getUsername();
        }).setHeader("Created By").setSortable(true);

        grid.addColumns( "status");

        grid.addColumn(
                ticketEntity -> ticketEntity.getDueDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
        ).setHeader("Due Date").setSortable(true).setComparator(Comparator.comparing(TicketEntity::getModifiedAt));
        grid.addColumn(
                ticketEntity -> ticketEntity.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
        ).setHeader("Created At").setSortable(true).setComparator(Comparator.comparing(TicketEntity::getModifiedAt));
        grid.addColumn(
                ticketEntity -> ticketEntity.getModifiedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
        ).setHeader("Modified At").setSortable(true).setComparator(Comparator.comparing(TicketEntity::getModifiedAt));

        grid.addComponentColumn(item -> createActions(grid, item)).setHeader("");

        grid.getColumns().forEach(col -> {
            col.setAutoWidth(true);
            col.setTextAlign(ColumnTextAlign.CENTER);
        });
    }

    private HorizontalLayout createActions(Grid<TicketEntity> grid, TicketEntity ticketEntity) {
        HorizontalLayout horizontalLayout = new HorizontalLayout(
                createEditButton(grid, ticketEntity),
                createDeleteButton(grid, ticketEntity)
        );
        horizontalLayout.setJustifyContentMode(JustifyContentMode.AROUND);
        return horizontalLayout;
    }

    private Button createEditButton(Grid<TicketEntity> grid, TicketEntity ticketEntity) {
        return new Button("Edit", clickEvent -> {
            openDialog(ticketEntity);
            currentTicket = ticketEntity;
            ticketDialog.open();
        });
    }

    private Button createDeleteButton(Grid<TicketEntity> grid, TicketEntity ticketEntity) {
        Button button = new Button("Delete", clickEvent -> {
            ticketService.deleteTicketById(ticketEntity.getId());
            grid.setItems(ticketService.findTicketsEntitiesByWorkspaceId(workSpaceEntity.getId()));
        });
        button.addThemeVariants(ButtonVariant.LUMO_ERROR);
        return button;
    }

    private void openDialog(TicketEntity ticketEntity) {
        workspaceHeader.setText("Workspace - " + ticketEntity.getWorkspace().getName());
        ticketTitle.setValue(ticketEntity.getTitle());
        ticketBody.setValue(ticketEntity.getBody());
        assigneeSelect.setValue(ticketEntity.getAssignee());
        dueDate.setValue(ticketEntity.getDueDate() == null ? LocalDateTime.now() : ticketEntity.getDueDate());
        statusSelect.setValue(ticketEntity.getStatus() == null ? TicketEntity.TicketStatus.NEW : ticketEntity.getStatus());

        commentBody.setValue("");
        comments.removeAll();
        comments.add(commentService.findByTicketId(ticketEntity.getId()).stream().map(this::createCommentRepresentation).toArray(VerticalLayout[]::new));
    }

    private VerticalLayout createCommentRepresentation(CommentEntity commentEntity) {
        Span body = new Span();
        body.setText(commentEntity.getBody());
        body.getStyle().set("padding", "0");
        body.getStyle().set("background-color", "var(--lumo-contrast-10pct)");
        body.getStyle().set("border-radius", "var(--lumo-border-radius)");
        body.getStyle().set("padding", "0 calc(0.375em + var(--lumo-border-radius) / 4 - 1px)");
        body.getStyle().set("font-weight", "500");
        body.getStyle().set("margin-top", "0");
        body.setSizeFull();

        Span username = new Span(commentEntity.getCreatedBy().getUsername());
        username.getStyle().set("padding", "0");
        username.getStyle().set("margin-top", "1em");
        username.getStyle().set("color", "blue");

        VerticalLayout verticalLayout = new VerticalLayout(username, body);
        verticalLayout.getStyle().set("margin", "0");
        verticalLayout.getStyle().set("padding", "0");

        return verticalLayout;
    }

    private void openCreateDialog(TicketEntity ticketEntity) {
        createSaveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        createWorkspaceHeader.setText("Workspace - " + workSpaceEntity.getName());
        createDueDate.setValue(ticketEntity.getDueDate() == null ? LocalDateTime.now() : ticketEntity.getDueDate());
        createStatusSelect.setValue(ticketEntity.getStatus() == null ? TicketEntity.TicketStatus.NEW : ticketEntity.getStatus());
    }

    private void addComment() {
        CommentEntity commentEntity = CommentEntity.builder()
                .body(commentBody.getValue())
                .createdBy(getCurrentUser())
                .ticket(currentTicket)
                .build();
        this.commentService.saveCommentEntity(commentEntity);
    }

    private void createTicket() {
        TicketEntity ticketEntity = TicketEntity.builder()
                .workspace(workspaceService.getById(workSpaceEntity.getId()))
                .title(createTicketTitle.getValue())
                .assignee(createAssigneeSelect.getValue())
                .createdBy(getCurrentUser())
                .body(createTicketBody.getValue())
                .status(createStatusSelect.getValue())
                .dueDate(createDueDate.getValue())
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build();
        this.ticketService.saveTicketEntity(ticketEntity);
    }

    private void updateTicket(TicketEntity ticketEntity) {
        ticketEntity.setTitle(ticketTitle.getValue());
        ticketEntity.setStatus(statusSelect.getValue());
        ticketEntity.setAssignee(assigneeSelect.getValue());
        ticketEntity.setDueDate(dueDate.getValue());
        ticketEntity.setBody(ticketBody.getValue());
        ticketEntity.setModifiedAt(LocalDateTime.now());
        this.ticketService.saveTicketEntity(ticketEntity);
        grid.setItems(ticketService.findTicketsEntitiesByWorkspaceId(workSpaceEntity.getId()));
        grid.getDataProvider().refreshAll();
    }

    private UserEntity getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        return userService.findByUsername(username);
    }
}