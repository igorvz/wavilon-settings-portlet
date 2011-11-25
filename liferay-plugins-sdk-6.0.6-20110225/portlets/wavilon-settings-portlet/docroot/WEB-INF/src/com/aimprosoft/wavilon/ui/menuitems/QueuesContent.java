package com.aimprosoft.wavilon.ui.menuitems;

import com.aimprosoft.wavilon.application.GenericPortletApplication;
import com.aimprosoft.wavilon.couch.CouchModel;
import com.aimprosoft.wavilon.couch.CouchModelLite;
import com.aimprosoft.wavilon.couch.CouchTypes;
import com.aimprosoft.wavilon.model.Queue;
import com.aimprosoft.wavilon.service.QueueDatabaseService;
import com.aimprosoft.wavilon.spring.ObjectFactory;
import com.aimprosoft.wavilon.ui.menuitems.forms.QueuesDragAndDropAgents;
import com.aimprosoft.wavilon.ui.menuitems.forms.QueuesForm;
import com.aimprosoft.wavilon.util.CouchModelUtil;
import com.aimprosoft.wavilon.util.LayoutUtil;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

import javax.portlet.PortletRequest;
import java.io.IOException;
import java.util.*;

public class QueuesContent extends VerticalLayout {
    private ResourceBundle bundle;
    private PortletRequest request;
    private IndexedContainer tableData;
    private List<String> tableFields;
    private List<String> hiddenFields;
    private Table queuesTable = new Table();
    private QueueDatabaseService service = ObjectFactory.getBean(QueueDatabaseService.class);
    private VerticalLayout bottom;

    public QueuesContent(ResourceBundle bundle) {
        this.bundle = bundle;
    }

    public void init() {
        request = ((GenericPortletApplication) getApplication()).getPortletRequest();
        this.hiddenFields = fillHiddenFields();
        this.tableFields = fillFields();
        this.tableData = createTableData();

        setSizeFull();
        initLayout();
        initQueuesTable();
    }

    private void initLayout() {
        VerticalLayout top = new VerticalLayout();
        addComponent(top);

        bottom = new VerticalLayout();
        addComponent(bottom);

        VerticalLayout head = LayoutUtil.createHead(bundle, queuesTable, CouchTypes.queue, getWindow());
        setWidth(100, Sizeable.UNITS_PERCENTAGE);
        top.addComponent(head);

        this.queuesTable.setContainerDataSource(this.tableData);
        this.queuesTable.setHeight(207, Sizeable.UNITS_PIXELS);
        this.queuesTable.addStyleName("queuesCustom");

        top.addComponent(this.queuesTable);
    }

    private void initQueuesTable() {
        this.queuesTable.setVisibleColumns(this.tableFields.toArray());
        this.queuesTable.setSelectable(true);
        this.queuesTable.setImmediate(true);
        LayoutUtil.setTableWidth(queuesTable, CouchTypes.queue);
        this.queuesTable.setColumnExpandRatio(bundle.getString("wavilon.table.queues.column.name"), 2);
        this.queuesTable.setColumnExpandRatio(bundle.getString("wavilon.table.queues.column.forward.to.on.max.time"), 3);
        this.queuesTable.setColumnExpandRatio(bundle.getString("wavilon.table.queues.column.forward.to.on.max.length"), 3);

        this.queuesTable.addListener(new Property.ValueChangeListener() {
            public void valueChange(Property.ValueChangeEvent event) {
                Object id = queuesTable.getValue();
                if (null != id) {
                    String queueId = (String) queuesTable.getItem(id).getItemProperty("id").getValue();
                    getAgentsTwinColumns(queueId);
                } else {
                    bottom.removeAllComponents();
                }
            }
        });

        this.queuesTable.addListener(new ItemClickEvent.ItemClickListener() {
            public void itemClick(ItemClickEvent event) {
                if (event.isDoubleClick()) {
                    Item item = event.getItem();
                    if (null != item) {
                        String queueId = (String) item.getItemProperty("id").getValue();
                        getAgentsTwinColumns(queueId);
                        LayoutUtil.getForm(queueId, event.getItemId(), getWindow(), new QueuesForm(bundle, queuesTable));
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
                Queue queue = getQueue(couchModel);
                CouchModelLite forwardToOnMaxLength = CouchModelUtil.getCouchModelLite(queue.getForwardToOnMaxLength(), bundle);
                CouchModelLite forwardToOnMaxTime = CouchModelUtil.getCouchModelLite(queue.getForwardToOnMaxTime(), bundle);
                final Object object = ic.addItem();
                ic.getContainerProperty(object, bundle.getString("wavilon.table.queues.column.name")).setValue(queue.getName());
                ic.getContainerProperty(object, bundle.getString("wavilon.table.queues.column.forward.to.on.max.time")).setValue(forwardToOnMaxTime);
                ic.getContainerProperty(object, bundle.getString("wavilon.table.queues.column.forward.to.on.max.length")).setValue(forwardToOnMaxLength);
                ic.getContainerProperty(object, "id").setValue(couchModel.getId());

                HorizontalLayout buttons = LayoutUtil.createTablesEditRemoveButtons(queuesTable, object, couchModel, bundle, null, getWindow());
                ic.getContainerProperty(object, "").setValue(buttons);

            }
        }
        return ic;
    }

    private Queue getQueue(CouchModel couchModel) {
        try {
            return service.getQueue(couchModel);
        } catch (IOException e) {
            return new Queue();
        }
    }

    private List<CouchModel> getCouchModels() {
        try {
            return service.getAllUsersCouchModelQueue(CouchModelUtil.getOrganizationId(request));
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    private void getAgentsTwinColumns(String id) {
        QueuesDragAndDropAgents agentsLayout = new QueuesDragAndDropAgents(bundle);

        this.bottom.removeAllComponents();
        this.bottom.addComponent(agentsLayout);
        agentsLayout.init(id);
    }

    private List<String> fillHiddenFields() {
        List<String> hiddenFields = new LinkedList<String>();

        hiddenFields.add(bundle.getString("wavilon.table.queues.column.name"));
        hiddenFields.add(bundle.getString("wavilon.table.queues.column.forward.to.on.max.time"));
        hiddenFields.add(bundle.getString("wavilon.table.queues.column.forward.to.on.max.length"));
        hiddenFields.add("id");
        hiddenFields.add("");

        return hiddenFields;
    }

    private List<String> fillFields() {
        List<String> tableFields = new LinkedList<String>();

        tableFields.add(bundle.getString("wavilon.table.queues.column.name"));
        tableFields.add(bundle.getString("wavilon.table.queues.column.forward.to.on.max.time"));
        tableFields.add(bundle.getString("wavilon.table.queues.column.forward.to.on.max.length"));
        tableFields.add("");

        return tableFields;
    }


}
