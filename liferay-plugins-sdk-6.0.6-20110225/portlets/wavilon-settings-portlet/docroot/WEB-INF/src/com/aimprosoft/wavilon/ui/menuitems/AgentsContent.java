package com.aimprosoft.wavilon.ui.menuitems;

import com.aimprosoft.wavilon.application.GenericPortletApplication;
import com.aimprosoft.wavilon.model.Agent;
import com.aimprosoft.wavilon.service.AgentDatabaseService;
import com.aimprosoft.wavilon.spring.ObjectFactory;
import com.aimprosoft.wavilon.ui.menuitems.forms.AgentsForm;
import com.aimprosoft.wavilon.ui.menuitems.forms.ConfirmingRemove;
import com.liferay.portal.util.PortalUtil;
import com.vaadin.data.Property;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.*;

import javax.portlet.PortletRequest;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class AgentsContent extends VerticalLayout {
    private ResourceBundle bundle;
    private AgentDatabaseService service = ObjectFactory.getBean(AgentDatabaseService.class);
    private List<String> hiddenFields;
    private VerticalLayout right = new VerticalLayout();
    private PortletRequest request;
    private Table table = new Table();
    private List<String> tableFields;
    private IndexedContainer tableData;
    private AgentsForm agentsForm;

    public AgentsContent(ResourceBundle bundle) {
        this.bundle = bundle;
    }

    public void init() {
        request = ((GenericPortletApplication) getApplication()).getPortletRequest();
        tableFields = fillFields();
        hiddenFields = fillHiddenFields();
        tableData = createTableData();

        this.setWidth(100, Sizeable.UNITS_PERCENTAGE);
        this.setSizeFull();
        initLayout();
        initAgents();
    }

    private void initLayout() {
        HorizontalLayout head = createHead();
        setWidth(100, Sizeable.UNITS_PERCENTAGE);
        addComponent(head);

        table.setContainerDataSource(this.tableData);
        table.setWidth(100, Sizeable.UNITS_PERCENTAGE);
        table.setStyleName("tableCustom");
        addComponent(table);
    }

    private List<String> initAgents() {
        table.setContainerDataSource(this.tableData);
        table.setVisibleColumns(this.tableFields.toArray());
        table.setSelectable(true);
        table.setImmediate(true);

        table.addListener(new Property.ValueChangeListener() {
            public void valueChange(Property.ValueChangeEvent event) {

                Object id = table.getValue();
                if (null != id) {
                    getForm((String) table.getItem(id).getItemProperty("id").getValue());
                }
            }
        });
        return this.tableFields;
    }

    private HorizontalLayout createButtons() {
        HorizontalLayout addRemoveButtons = new HorizontalLayout();
        addRemoveButtons.addComponent(new Button("+", new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                getForm("-1");
            }
        }));
        addRemoveButtons.addComponent(new Button("-", new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                Object id = table.getValue();
                if (null != id) {
                    String phoneNumbersID = (String) table.getItem(id).getItemProperty("id").getValue();

                    ConfirmingRemove confirmingRemove = new ConfirmingRemove(bundle);
                    getWindow().addWindow(confirmingRemove);
                    confirmingRemove.init(phoneNumbersID, table);
                    confirmingRemove.center();
                    confirmingRemove.setWidth("300px");
                    confirmingRemove.setHeight("180px");

                } else {
                    getWindow().showNotification("Select Agent");
                }
            }
        }));
        return addRemoveButtons;
    }

    private LinkedList<String> fillFields() {
        LinkedList<String> tableFields = new LinkedList<String>();

        tableFields.add("NAME");
        tableFields.add("CURRENT EXTENSION");

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
                ic.getContainerProperty(object, "NAME").setValue(agent.getName());
                ic.getContainerProperty(object, "CURRENT EXTENSION").setValue(agent.getCurrentExtension());
                ic.getContainerProperty(object, "id").setValue(agent.getId());
            }
        }
        return ic;
    }

    private List<Agent> getAgents() {
        try {
            return service.getAllAgentsByUser(PortalUtil.getUserId(request), PortalUtil.getScopeGroupId(request));
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    private void getForm(String id) {
        agentsForm = new AgentsForm(bundle, table);
        agentsForm.setWidth("400px");
        agentsForm.setHeight("300px");
        agentsForm.center();
        agentsForm.setModal(true);

        getWindow().addWindow(agentsForm);
        agentsForm.init(id);
    }

    private List<String> fillHiddenFields() {
        LinkedList<String> tableFields = new LinkedList<String>();

        tableFields.add("NAME");
        tableFields.add("CURRENT EXTENSION");
        tableFields.add("id");

        return tableFields;
    }

    public HorizontalLayout createHead() {
        HorizontalLayout head = new HorizontalLayout();
        head.setWidth(100, Sizeable.UNITS_PERCENTAGE);
        Label headLabel = new Label("Agents");
        head.addComponent(headLabel);
        head.setMargin(false);
        head.addStyleName("headLine");
        headLabel.addStyleName("agentHeader");
        headLabel.addStyleName("tableHeader");

        HorizontalLayout addRemoveButtons = createButtons();
        head.addComponent(addRemoveButtons);

        head.setComponentAlignment(headLabel, Alignment.TOP_LEFT);
        head.setComponentAlignment(addRemoveButtons, Alignment.MIDDLE_RIGHT);

        return head;
    }

}
