package com.aimprosoft.wavilon.ui;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.*;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SimplePage extends VerticalLayout {


    public SimplePage() {
        addStyleName("settings");
        setSizeFull();

        //title bar
        HorizontalLayout titleBar = createTitleBar();
        addComponent(titleBar);

        //top label [WAVILON]
        Label topLabel = createTopLabel();
        titleBar.addComponent(topLabel);

        //top label comment [Cloud Contact Center]
        Label titleComment = createTopLabelComment();
        titleBar.addComponent(titleComment);

        titleBar.setExpandRatio(topLabel, 1.0f);

        //main working part (main content)
        HorizontalLayout mainContent = createMainContent();
        addComponent(mainContent);
        setExpandRatio(mainContent, 1);


        //LEFT PART  (menu)
        Panel treeMenu = createTreeMenu();
        mainContent.addComponent(treeMenu);

        Tree menu = new Tree();
        menu.setSizeUndefined();
        treeMenu.addComponent(menu);


        //RIGHT PART  (view content)

        //display label [Details]
        Panel detailsPanel = createDetailsPanel();
        mainContent.addComponent(detailsPanel);

        VerticalLayout detailsLayout = new VerticalLayout();
        detailsLayout.setSizeFull();
        detailsPanel.setContent(detailsLayout);

        //items box
        VerticalLayout detailsBox = new VerticalLayout();
        detailsBox.setSizeUndefined();

        //display item title
        final Label item_title = new Label("item title");
        item_title.setSizeUndefined();
        detailsBox.addComponent(item_title);

        //items content
        VerticalLayout detailsContent = new VerticalLayout();
        detailsBox.setSizeFull();
        detailsBox.addComponent(detailsContent);


        detailsLayout.addComponent(detailsBox);
        detailsLayout.setComponentAlignment(detailsBox, Alignment.MIDDLE_CENTER);


        mainContent.setExpandRatio(detailsPanel, 1);
        mainContent.setExpandRatio(treeMenu, 0);

        //fill tree menu
        fillTreeMenu(menu, item_title, detailsContent);
        menu.setImmediate(true);

    }

    //title bar
    private HorizontalLayout createTitleBar() {
        HorizontalLayout titleBar = new HorizontalLayout();
        titleBar.setWidth("100%");
        return titleBar;
    }

    //top label [WAVILON]
    private Label createTopLabel() {
        Label topLabel = new Label("WAVILON");
        topLabel.addStyleName("title");
        return topLabel;
    }

    //top label comment [Cloud Contact Center]
    private Label createTopLabelComment() {
        Label titleComment = new Label("Cloud Contact Center");
        titleComment.addStyleName("titlecomment");
        titleComment.setSizeUndefined();
        return titleComment;
    }

    //main working part (main content)
    private HorizontalLayout createMainContent() {
        HorizontalLayout mainContent = new HorizontalLayout();
        mainContent.setSizeFull();
        mainContent.setSpacing(true);
        return mainContent;
    }

    //LEFT PART  (menu)
    private Panel createTreeMenu() {
        Panel menuContainer = new Panel("Settings");
        menuContainer.addStyleName("menucontainer");
        menuContainer.addStyleName("light");
        menuContainer.setWidth("-1px");
        menuContainer.setHeight("100%");
        menuContainer.getContent().setWidth("-1px");
        return menuContainer;
    }

    //display label [Details]
    private Panel createDetailsPanel() {
        Panel detailsPanel = new Panel("Details");
        detailsPanel.addStyleName("detailspanel");
        detailsPanel.addStyleName("light");
        detailsPanel.setSizeFull();
        return detailsPanel;
    }

    private void fillTreeBar(Map<String, List<String>> treeBar) {
        List<String> service = new LinkedList<String>();
        service.add("Phone Numbers");
        service.add("Virtual Numbers");
        service.add("Gtalk");

        treeBar.put("Service", service);

        List<String> queues = new LinkedList<String>();

        queues.add("Sub menu");
        queues.add("Another Sub menu");

        treeBar.put("Queues", queues);

        List<String> agents = new LinkedList<String>();

        agents.add("Sub menu");
        agents.add("Another Sub menu");

        treeBar.put("Agents", agents);

        List<String> extensions = new LinkedList<String>();

        extensions.add("SIP");
        extensions.add("Gtalk");
        extensions.add("Phone numbers");

        treeBar.put("Extensions", extensions);

        List<String> recordings = new LinkedList<String>();

        recordings.add("Sub menu");
        recordings.add("Another Sub menu");

        treeBar.put("Recordings", recordings);
    }

    //fill tree menu
    private void fillTreeMenu(Tree menu, final Label item_title, final VerticalLayout detailsContent) {
        Map<String, List<String>> treeBar = new LinkedHashMap<String, List<String>>();
        fillTreeBar(treeBar);

        for (String s : treeBar.keySet()) {
            menu.addItem(s);
            List<String> children = treeBar.get(s);
            for (String child : children) {
                Label label = new Label(child);
                menu.addItem(label);
                menu.setParent(label, s);
                menu.setChildrenAllowed(label, false);
                menu.expandItemsRecursively(s);
            }
        }

        menu.addListener(new Property.ValueChangeListener() {
            public void valueChange(ValueChangeEvent event) {
                if (event.getProperty() != null && event.getProperty().getValue() != null) {
                    item_title.setValue(event.getProperty());
                    detailsContent.removeAllComponents();
                    detailsContent.addComponent(new TreeContent(event.getProperty().toString()));
                }
            }
        });
    }

}
