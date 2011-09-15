package com.aimprosoft.wavilon.ui;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.*;

import java.util.*;

public class SettingsPage extends VerticalLayout {
    ResourceBundle bundle;

    public SettingsPage(ResourceBundle bundle) {
        this.bundle = bundle;
        addStyleName("settings");
        setSides(this);


        HorizontalSplitPanel panel = new HorizontalSplitPanel();
        setSides(panel);
        panel.setSplitPosition(150, Sizeable.UNITS_PIXELS);
        panel.setLocked(false);
        addComponent(panel);


        VerticalLayout leftColumn = new VerticalLayout();
        leftColumn.setStyleName("leftcolumn");
        setSides(leftColumn);
        panel.addComponent(leftColumn);

        Tree menu = new Tree();
        menu.setWidth(100, Sizeable.UNITS_PERCENTAGE);
        menu.setHeight(400, Sizeable.UNITS_PIXELS);
        leftColumn.addComponent(menu);

        VerticalLayout rightColumn = new VerticalLayout();
        rightColumn.setStyleName("rightcolumn");
        setSides(rightColumn);
        rightColumn.setSizeUndefined();
        panel.addComponent(rightColumn);

        //items box
        VerticalLayout detailsBox = new VerticalLayout();
        setSides(detailsBox);

        //display item title
        final Label item_title = new Label("item title");
        item_title.setSizeUndefined();
        detailsBox.addComponent(item_title);

        //items content
        VerticalLayout detailsContent = new VerticalLayout();
        setSides(detailsContent);
        detailsBox.addComponent(detailsContent);


        rightColumn.addComponent(detailsBox);
        rightColumn.setComponentAlignment(detailsBox, Alignment.MIDDLE_CENTER);

        //fill tree menu
        fillTreeMenu(menu, item_title, detailsContent);
        menu.setImmediate(true);

        setExpandRatio(panel, 0);
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

            if (children.isEmpty()) {
                menu.setChildrenAllowed(s, false);
            } else {
                for (String child : children) {
                    Label label = new Label(child);
                    menu.addItem(label);
                    menu.setParent(label, s);
                    menu.setChildrenAllowed(label, false);
                    menu.expandItemsRecursively(s);
                }
            }
        }

        menu.addListener(new Property.ValueChangeListener() {
            public void valueChange(ValueChangeEvent event) {
                if (event.getProperty() != null && event.getProperty().getValue() != null) {
                    item_title.setValue(event.getProperty());
                    detailsContent.removeAllComponents();
                    detailsContent.addComponent(new TreeContentSwitch(event.getProperty().toString(), bundle));
                }
            }
        });
    }

    private void setSides(Component component) {
        component.setWidth(100, Sizeable.UNITS_PERCENTAGE);
        component.setHeight(100, Sizeable.UNITS_PERCENTAGE);
    }

}
