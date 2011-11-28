package com.aimprosoft.wavilon.ui.menuitems;

import com.aimprosoft.wavilon.application.GenericPortletApplication;
import com.aimprosoft.wavilon.couch.CouchModel;
import com.aimprosoft.wavilon.couch.CouchModelLite;
import com.aimprosoft.wavilon.couch.CouchTypes;
import com.aimprosoft.wavilon.model.Agent;
import com.aimprosoft.wavilon.service.AgentDatabaseService;
import com.aimprosoft.wavilon.spring.ObjectFactory;
import com.aimprosoft.wavilon.ui.menuitems.forms.AgentsForm;
import com.aimprosoft.wavilon.ui.menuitems.forms.ConfirmingRemove;
import com.aimprosoft.wavilon.ui.menuitems.forms.GeneralForm;
import com.aimprosoft.wavilon.util.CouchModelUtil;
import com.aimprosoft.wavilon.util.LayoutUtil;
import com.liferay.portal.util.PortalUtil;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

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
        removeAllComponents();
        request = ((GenericPortletApplication) getApplication()).getPortletRequest();
        tableFields = fillFields();
        hiddenFields = fillHiddenFields();
        tableData = createTableData();

        this.setSizeFull();
        initLayout();
        initAgents();
    }

    private void initLayout() {
        VerticalLayout head = LayoutUtil.createHead(bundle, table, CouchTypes.agent, getWindow());
        setWidth(100, Sizeable.UNITS_PERCENTAGE);
        addComponent(head);

        table.setContainerDataSource(this.tableData);
        table.setHeight("555px");
        table.setWidth(100, Sizeable.UNITS_PERCENTAGE);
        table.addStyleName("tableCustom");
        addComponent(table);
    }

    private void initAgents() {
        table.setVisibleColumns(this.tableFields.toArray());
        table.setSelectable(true);
        table.setImmediate(true);
        LayoutUtil.setTableWidth(table, CouchTypes.agent);


        table.addListener(new ItemClickEvent.ItemClickListener() {
            public void itemClick(ItemClickEvent event) {
                if (event.isDoubleClick()) {
                    Item item = event.getItem();
                    if (null != item) {
                        LayoutUtil.getForm((String) event.getItem().getItemProperty("id").getValue(), event.getItemId(), getWindow(), new AgentsForm(bundle, table));
                    }
                }
            }
        });
    }

    private IndexedContainer createTableData() {
        IndexedContainer ic = new IndexedContainer();

        List<CouchModel> couchModels = getCouchModels();

        LayoutUtil.addContainerProperties(hiddenFields, ic);

        if (!couchModels.isEmpty()) {

            for (final CouchModel couchModel : couchModels) {
                Agent agent = getAgent(couchModel);
                CouchModelLite extension = CouchModelUtil.getCouchModelLite((String) couchModel.getOutputs().get("extension"), bundle);
                final Object object = ic.addItem();
                ic.getContainerProperty(object, bundle.getString("wavilon.table.agents.column.name")).setValue(agent.getName());
                ic.getContainerProperty(object, bundle.getString("wavilon.table.agents.column.current.extension")).setValue(extension);
                ic.getContainerProperty(object, "id").setValue(couchModel.getId());

                HorizontalLayout buttons = LayoutUtil.createTablesEditRemoveButtons(table, object, couchModel, bundle, null, getWindow(), new AgentsForm(bundle, table));
                ic.getContainerProperty(object, "").setValue(buttons);
            }
        }
        return ic;
    }

    private List<CouchModel> getCouchModels() {
        try {
            return service.getAllUsersCouchModelAgent(CouchModelUtil.getOrganizationId(request));
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
