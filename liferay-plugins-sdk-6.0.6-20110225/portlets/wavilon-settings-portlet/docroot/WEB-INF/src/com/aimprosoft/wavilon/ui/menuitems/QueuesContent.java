package com.aimprosoft.wavilon.ui.menuitems;

import com.aimprosoft.wavilon.couch.CouchModel;
import com.aimprosoft.wavilon.couch.CouchModelLite;
import com.aimprosoft.wavilon.couch.CouchTypes;
import com.aimprosoft.wavilon.model.Queue;
import com.aimprosoft.wavilon.service.QueueDatabaseService;
import com.aimprosoft.wavilon.spring.ObjectFactory;
import com.aimprosoft.wavilon.ui.menuitems.forms.QueuesDragAndDropAgents;
import com.aimprosoft.wavilon.util.CouchModelUtil;
import com.vaadin.data.Property;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

import java.util.ResourceBundle;

public class QueuesContent extends VerticalLayout {
    private Content content;
    private VerticalLayout bottom = new VerticalLayout();

    public QueuesContent(ResourceBundle bundle) {
        content = new Content(bundle);
    }

    public void init(final ResourceBundle b) {
        addComponent(content);
        content.init();
        addComponent(bottom);

        content.getTable().addStyleName("queuesCustom");
        content.getTable().addListener(new Property.ValueChangeListener() {
            public void valueChange(Property.ValueChangeEvent event) {
                Object id = content.getTable().getValue();
                if (null != id) {
                    String queueId = (String) content.getTable().getItem(id).getItemProperty("id").getValue();
                    getAgentsTwinColumns(queueId, b);
                } else {
                    bottom.removeAllComponents();
                }
            }
        });

    }

    private void getAgentsTwinColumns(String id, ResourceBundle b) {
        QueuesDragAndDropAgents agentsLayout = new QueuesDragAndDropAgents(b);

        this.bottom.removeAllComponents();
        this.bottom.addComponent(agentsLayout);
        agentsLayout.init(id);
    }

    private class Content extends GenerelContent {
        private QueueDatabaseService service = ObjectFactory.getBean(QueueDatabaseService.class);

        public Content(ResourceBundle bundle) {
            super(bundle);
        }

        public void init() {
            super.init();
            fillVisibleFields();
            fillHiddenFields();
            createTableData(service, CouchTypes.queue);
            fillTable();
            initLayout(CouchTypes.queue, ratioMap(1, 2, 2));
        }

        private void fillVisibleFields() {
            visibleFields.add(bundle.getString("wavilon.table.queues.column.name"));
            visibleFields.add(bundle.getString("wavilon.table.queues.column.forward.to.on.max.time"));
            visibleFields.add(bundle.getString("wavilon.table.queues.column.forward.to.on.max.length"));
        }

        private void fillTable() {
            if (!couchModels.isEmpty()) {

                for (final CouchModel couchModel : couchModels) {
                    Queue queue = getModel(couchModel, service, Queue.class);
                    CouchModelLite forwardToOnMaxLength = CouchModelUtil.getCouchModelLite(queue.getForwardToOnMaxLength(), bundle);
                    CouchModelLite forwardToOnMaxTime = CouchModelUtil.getCouchModelLite(queue.getForwardToOnMaxTime(), bundle);
                    final Object object = tableData.addItem();

                    tableData.getContainerProperty(object, "id").setValue(couchModel.getId());
                    tableData.getContainerProperty(object, bundle.getString("wavilon.table.queues.column.name")).setValue(queue.getName());
                    tableData.getContainerProperty(object, bundle.getString("wavilon.table.queues.column.forward.to.on.max.time")).setValue(forwardToOnMaxTime);
                    tableData.getContainerProperty(object, bundle.getString("wavilon.table.queues.column.forward.to.on.max.length")).setValue(forwardToOnMaxLength);
                    tableData.getContainerProperty(object, "").setValue(createTablesEditRemoveButtons(object, couchModel, null));

                }
            }
        }


        public Table getTable() {
            return table;
        }
    }
}
