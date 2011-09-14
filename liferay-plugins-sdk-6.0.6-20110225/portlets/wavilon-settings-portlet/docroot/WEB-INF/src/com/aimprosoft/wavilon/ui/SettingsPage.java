package com.aimprosoft.wavilon.ui;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.*;

import java.util.*;

public class SettingsPage extends VerticalLayout {
    ResourceBundle bundle;

    public SettingsPage(ResourceBundle bundle) {
        this.bundle = bundle;
        addStyleName("settings");
        setSizeFull();

        HorizontalLayout mainContent = createMainContent();
        addComponent(mainContent);
        setExpandRatio(mainContent, 1);

        VerticalLayout leftColumn =  new VerticalLayout();
        leftColumn.setStyleName("leftcolumn");
        leftColumn.setHeight("100%");
        leftColumn.setSizeUndefined();
        mainContent.addComponent(leftColumn);

        Tree menu = new Tree();
        menu.setSizeUndefined();
        leftColumn.addComponent(menu);

        VerticalLayout rightColumn = new VerticalLayout();
        rightColumn.setStyleName("rightcolumn");
        rightColumn.setHeight("100%");
        rightColumn.setSizeFull();
        mainContent.addComponent(rightColumn);

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


        rightColumn.addComponent(detailsBox);
        rightColumn.setComponentAlignment(detailsBox, Alignment.MIDDLE_CENTER);

//
        mainContent.setExpandRatio(rightColumn, 1);
        mainContent.setExpandRatio(leftColumn, 0);

        //fill tree menu
        fillTreeMenu(menu, item_title, detailsContent);
        menu.setImmediate(true);

    }

    //main working part (main content)
    private HorizontalLayout createMainContent() {
        HorizontalLayout mainContent = new HorizontalLayout();
        mainContent.setSizeFull();
        mainContent.setSpacing(true);
        return mainContent;
    }

    private void fillTreeBar(Map<String, List<String>> treeBar) {

        List<String> service = new LinkedList<String>();
        service.add(bundle.getString("wavilon.settings.services.phoneNumbers"));
        service.add(bundle.getString("wavilon.settings.services.virtualNumbers"));
        service.add(bundle.getString("wavilon.settings.services.gtalk"));

        treeBar.put(bundle.getString("wavilon.settings.services"), service);

        List<String> queues = new LinkedList<String>();

        queues.add(bundle.getString("wavilon.settings.queues.subMenu"));
        queues.add(bundle.getString("wavilon.services.queues.anotherSubMenu"));

        treeBar.put(bundle.getString("wavilon.settings.queues"), queues);

        List<String> agents = new LinkedList<String>();

        agents.add(bundle.getString("wavilon.settings.agents.subMenu"));
        agents.add(bundle.getString("wavilon.settings.agents.anotherSubMenu"));

        treeBar.put(bundle.getString("wavilon.settings.agents"), agents);

        List<String> extensions = new LinkedList<String>();

        extensions.add(bundle.getString("wavilon.settings.extensions.sip"));
        extensions.add(bundle.getString("wavilon.settings.extensions.gtalk"));
        extensions.add(bundle.getString("wavilon.settings.extensions.phoneNumbers"));

        treeBar.put(bundle.getString("wavilon.settings.extensions"), extensions);

        List<String> recordings = new LinkedList<String>();

        recordings.add(bundle.getString("wavilon.settings.recordings.subMenu"));
        recordings.add(bundle.getString("wavilon.settings.recordings.anotherSubMenu"));

        treeBar.put(bundle.getString("wavilon.settings.recordings"), recordings);

        //todo probably remove this, test user validation form
        treeBar.put(bundle.getString("wavilon.settings.validation.form"), Collections.<String>emptyList());
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
                    detailsContent.addComponent(new TreeContent(event.getProperty().toString(), bundle));
                }
            }
        });
    }

}
