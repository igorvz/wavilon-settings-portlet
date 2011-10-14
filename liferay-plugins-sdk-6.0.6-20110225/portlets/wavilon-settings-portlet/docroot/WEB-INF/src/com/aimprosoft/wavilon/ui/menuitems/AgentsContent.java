package com.aimprosoft.wavilon.ui.menuitems;

import com.aimprosoft.wavilon.application.GenericPortletApplication;
import com.aimprosoft.wavilon.couch.CouchModel;
import com.aimprosoft.wavilon.couch.CouchModelLite;
import com.aimprosoft.wavilon.model.Agent;
import com.aimprosoft.wavilon.service.AgentDatabaseService;
import com.aimprosoft.wavilon.spring.ObjectFactory;
import com.aimprosoft.wavilon.ui.menuitems.forms.AgentsForm;
import com.aimprosoft.wavilon.ui.menuitems.forms.ConfirmingRemove;
import com.aimprosoft.wavilon.util.CouchModelUtil;
import com.liferay.portal.util.PortalUtil;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.*;

import javax.portlet.PortletRequest;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class AgentsContent extends VerticalLayout {
    private ResourceBundle bundle;
    private AgentDatabaseService service = ObjectFactory.getBean(AgentDatabaseService.class);
    private List<String> hiddenFields;
    private PortletRequest request;
    private Table table = new Table();
    private List<String> tableFields;
    private IndexedContainer tableData;

    public AgentsContent(ResourceBundle bundle) {
        this.bundle = bundle;
    }

    public void init() {
        request = ((GenericPortletApplication) getApplication()).getPortletRequest();
        tableFields = fillFields();
        hiddenFields = fillHiddenFields();
        tableData = createTableData();

        this.setSizeFull();
        initLayout();
        initAgents();
    }

    private void initLayout() {
        HorizontalLayout head = createHead();
        setWidth(100, Sizeable.UNITS_PERCENTAGE);
        addComponent(head);

        table.setColumnWidth("", 60);
        table.setContainerDataSource(this.tableData);
        table.setWidth(100, Sizeable.UNITS_PERCENTAGE);
        table.setHeight("555px");
        table.addStyleName("tableCustom");
        addComponent(table);
    }

    private void initAgents() {
//        table.setContainerDataSource(this.tableData);
        table.setVisibleColumns(this.tableFields.toArray());
        table.setSelectable(true);
        table.setImmediate(true);

        table.addListener(new ItemClickEvent.ItemClickListener() {
            public void itemClick(ItemClickEvent event) {
                if (event.isDoubleClick()) {
                    Item item = event.getItem();
                    if (null != item) {
                        getForm((String) event.getItem().getItemProperty("id").getValue(), event.getItemId());
                    }
                }
            }
        });
    }

    private HorizontalLayout createButton() {
        HorizontalLayout addButton = new HorizontalLayout();
        addButton.addComponent(new Button(bundle.getString("wavilon.button.add"), new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                getForm("-1", "-1");
            }
        }));
        return addButton;
    }

    private IndexedContainer createTableData() {
        IndexedContainer ic = new IndexedContainer();

        List<CouchModel> couchModels = getCouchModels();

        for (String field : hiddenFields) {
            if ("".equals(field)) {
                ic.addContainerProperty(field, Button.class, "");
            } else {
                ic.addContainerProperty(field, String.class, "");
            }
        }

        if (!couchModels.isEmpty()) {

            for (final CouchModel couchModel : couchModels) {
                Agent agent = getAgent(couchModel);
                CouchModelLite extension = CouchModelUtil.getCouchModelLite((String) couchModel.getOutputs().get("extension"));
                final Object object = ic.addItem();
                ic.getContainerProperty(object, bundle.getString("wavilon.table.agents.column.name")).setValue(agent.getName());
                ic.getContainerProperty(object, bundle.getString("wavilon.table.agents.column.current.extension")).setValue(extension);
                ic.getContainerProperty(object, "id").setValue(couchModel.getId());
                ic.getContainerProperty(object, "").setValue(new Button("", new Button.ClickListener() {
                    public void buttonClick(Button.ClickEvent event) {
                        table.select(object);
                        ConfirmingRemove confirmingRemove = new ConfirmingRemove(bundle);
                        getWindow().addWindow(confirmingRemove);
                        confirmingRemove.init(couchModel.getId(), table);
                    }
                }));
            }

        }
        return ic;
    }

    private List<CouchModel> getCouchModels() {
        try {
            return service.getAllUsersCouchModelAgent(PortalUtil.getUserId(request), PortalUtil.getScopeGroupId(request));
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    private Agent getAgent(CouchModel couchModel) {
        try {
            return service.getAgent(couchModel);
        } catch (Exception e) {
            return new Agent();
        }
    }

    private void getForm(String id, Object itemId) {
        AgentsForm agentsForm = new AgentsForm(bundle, table);
        agentsForm.setWidth("450px");
        agentsForm.setHeight("320px");
        agentsForm.center();
        agentsForm.setModal(true);

        getWindow().addWindow(agentsForm);
        agentsForm.init(id, itemId);
    }

    public HorizontalLayout createHead() {
        HorizontalLayout head = new HorizontalLayout();
        head.setWidth(100, Sizeable.UNITS_PERCENTAGE);
        Label headLabel = new Label(bundle.getString("wavilon.menuitem.agents"));
        head.addComponent(headLabel);
        head.setMargin(false);
        head.addStyleName("head");
        headLabel.addStyleName("label");

        HorizontalLayout addButton = createButton();
        head.addComponent(addButton);

        head.setComponentAlignment(headLabel, Alignment.TOP_LEFT);
        head.setComponentAlignment(addButton, Alignment.MIDDLE_RIGHT);

        return head;
    }

    private List<String> fillHiddenFields() {
        LinkedList<String> tableFields = new LinkedList<String>();

        tableFields.add(bundle.getString("wavilon.table.agents.column.name"));
        tableFields.add(bundle.getString("wavilon.table.agents.column.current.extension"));
        tableFields.add("id");
        tableFields.add("");

        return tableFields;
    }

    private LinkedList<String> fillFields() {
        LinkedList<String> tableFields = new LinkedList<String>();

        tableFields.add(bundle.getString("wavilon.table.agents.column.name"));
        tableFields.add(bundle.getString("wavilon.table.agents.column.current.extension"));
        tableFields.add("");

        return tableFields;
    }

}
