package com.aimprosoft.wavilon.ui.menuitems;

import com.aimprosoft.wavilon.model.Agent;
import com.aimprosoft.wavilon.service.AgentDatabaseService;
import com.aimprosoft.wavilon.spring.ObjectFactory;
import com.aimprosoft.wavilon.ui.menuitems.forms.AgentsForm;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.*;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class AgentsContent extends VerticalLayout {
    private ResourceBundle bundle;
    private static AgentDatabaseService agentsService = ObjectFactory.getBean(AgentDatabaseService.class);
    private List<String> hiddenFields;
    private HorizontalLayout main = new HorizontalLayout();
    private VerticalLayout left = new VerticalLayout();
    private VerticalLayout right = new VerticalLayout();

    private Table table = new Table();
    private List<String> tableFields;
    private IndexedContainer tableData;

    private HorizontalLayout bottomLeftCorner = new HorizontalLayout();
    private Button contactRemovalButton;

    public AgentsContent(ResourceBundle bundle) {
        this.bundle = bundle;
        tableFields = fillFields();
        hiddenFields = fillHiddenFields();
        tableData = createTableData();

        initLayout();
        initTableAddRemoveButtons();
        initAddressList();
    }

    private void initLayout() {
        main.setHeight(400, Sizeable.UNITS_PIXELS);
        main.setSizeUndefined();
        addComponent(main);
        main.addComponent(left);
        main.addComponent(right);

        table.setContainerDataSource(tableData);
        table.setHeight(330, Sizeable.UNITS_PIXELS);
        table.setWidth(200, Sizeable.UNITS_PIXELS);
        left.addComponent(table);

        bottomLeftCorner.setWidth(100, Sizeable.UNITS_PIXELS);
        left.addComponent(bottomLeftCorner);
        left.setComponentAlignment(bottomLeftCorner, Alignment.BOTTOM_LEFT);
    }

    private List<String> initAddressList() {
        table.setContainerDataSource(tableData);
        table.setVisibleColumns(tableFields.toArray());
        table.setSelectable(true);
        table.setImmediate(true);

        table.addListener(new Property.ValueChangeListener() {
            public void valueChange(Property.ValueChangeEvent event) {
                Object id = table.getValue();

                contactRemovalButton.setVisible(id != null);

                if (id != null) {
                    viewRightColumnContent(table.getItem(id));
                }

            }
        });

        return tableFields;
    }

    private void initTableAddRemoveButtons() {
        // New item button
        bottomLeftCorner.addComponent(new Button("+",
                new Button.ClickListener() {
                    public void buttonClick(Button.ClickEvent event) {
                        viewRightColumnContent(null);
                    }
                }));

        // Remove item button
        contactRemovalButton = new Button("-", new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                Object id = table.getValue();

                String agentID = (String) table.getItem(id).getItemProperty("id").getValue();


                try {
                    agentsService.removeAgent(agentID);
                } catch (IOException ignored) {
                }

                table.removeItem(table.getValue());
                table.select(null);
                right.removeAllComponents();

            }
        });
        contactRemovalButton.setVisible(false);
        bottomLeftCorner.addComponent(contactRemovalButton);
    }

    private LinkedList<String> fillFields() {
        LinkedList<String> tableFields = new LinkedList<String>();

        tableFields.add(bundle.getString("wavilon.agent.name"));

        return tableFields;
    }

    private IndexedContainer createTableData() {
        IndexedContainer ic = new IndexedContainer();

        List<Agent> agents = getAgents();

        for (String field : hiddenFields) {
            ic.addContainerProperty(field, String.class, "");
        }

        if (!agents.isEmpty()) {
            for (Agent agent : agents) {
                Object object = ic.addItem();
                ic.getContainerProperty(object, bundle.getString("wavilon.agent.name")).setValue(agent.getFirstName());
                ic.getContainerProperty(object, "id").setValue(agent.getId());
            }
        } else {
            Object object = ic.addItem();
            ic.getContainerProperty(object, bundle.getString("wavilon.agent.name")).setValue("Empty data");
        }
        return ic;
    }

    private static List<Agent> getAgents() {
        try {
            return agentsService.getAllAgents();
        } catch (IOException e) {
            return null;
        }
    }

    private void viewRightColumnContent(Object id) {
        Item item = id == null ? null : (Item) id;

        right.removeAllComponents();
        right.addComponent(new AgentsForm(bundle, item, right, table));
    }

    private List<String> fillHiddenFields() {
        LinkedList<String> tableFields = new LinkedList<String>();

        tableFields.add(bundle.getString("wavilon.agent.name"));
        tableFields.add("id");

        return tableFields;
    }
}
