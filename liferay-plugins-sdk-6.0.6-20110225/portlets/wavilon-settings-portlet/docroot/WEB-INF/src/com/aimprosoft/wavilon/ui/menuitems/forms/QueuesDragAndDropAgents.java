package com.aimprosoft.wavilon.ui.menuitems.forms;

import com.aimprosoft.wavilon.application.GenericPortletApplication;
import com.aimprosoft.wavilon.couch.CouchModel;
import com.aimprosoft.wavilon.couch.CouchModelLite;
import com.aimprosoft.wavilon.couch.CouchTypes;
import com.aimprosoft.wavilon.model.Queue;
import com.aimprosoft.wavilon.service.AgentDatabaseService;
import com.aimprosoft.wavilon.service.QueueDatabaseService;
import com.aimprosoft.wavilon.spring.ObjectFactory;
import com.aimprosoft.wavilon.util.CouchModelUtil;
import com.liferay.portal.util.PortalUtil;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.DataBoundTransferable;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.event.dd.acceptcriteria.And;
import com.vaadin.event.dd.acceptcriteria.SourceIs;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.*;

import javax.portlet.PortletRequest;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class QueuesDragAndDropAgents extends HorizontalLayout {


    private QueueDatabaseService queuesService = ObjectFactory.getBean(QueueDatabaseService.class);
    private AgentDatabaseService agentService = ObjectFactory.getBean(AgentDatabaseService.class);
    private ResourceBundle bundle;
    private PortletRequest request;
    private CouchModel model;
    private Queue queue;

    public QueuesDragAndDropAgents(ResourceBundle bundle) {
        this.bundle = bundle;
    }

    public void init(String id) {
        request = ((GenericPortletApplication) getApplication()).getPortletRequest();
        model = createModel(id);
        queue = createQueue(model);

        initLayout();
    }

    private void initLayout() {
        setWidth(100, Sizeable.UNITS_PERCENTAGE);
        setHeight(300, Sizeable.UNITS_PIXELS);

        List<CouchModel> availableAgentList = Collections.emptyList();
        try {
            availableAgentList = agentService.getAllUsersCouchModelAgent(CouchModelUtil.getOrganizationId(request));
        } catch (Exception ignored) {
        }

        final List<CouchModel> agentsInQueueList = getQueuesAgents();
        availableAgentList.removeAll(agentsInQueueList);

        final List<String> tableVisibleFields = createVisible();
        List<String> tableHiddenFields = createHidden();

        VerticalLayout left = new VerticalLayout();
        left.addStyleName("head");

        Label leftLabel = new Label(bundle.getString("wavilon.form.queues.drag.and.drop.agents.in.queue"));
        leftLabel.addStyleName("label");
        left.addComponent(leftLabel);


        VerticalLayout right = new VerticalLayout();
        right.addStyleName("head");
        Label rightLabel = new Label(bundle.getString("wavilon.form.queues.drag.and.drop.available.agents"));
        rightLabel.addStyleName("label");
        right.addComponent(rightLabel);


        final Table agentsInQueue = fillTable(agentsInQueueList, tableVisibleFields, tableHiddenFields);
        agentsInQueue.addStyleName("agentsInQueueTable");

        final Table availableAgents = fillTable(availableAgentList, tableVisibleFields, tableHiddenFields);
        availableAgents.addStyleName("availableAgentsTable");

        dragAndDropInit(agentsInQueue, availableAgents);


        VerticalLayout middle = createMiddle(agentsInQueue, availableAgents);
        middle.addStyleName("middleDragNDrop");
        left.addComponent(agentsInQueue);
        right.addComponent(availableAgents);

        addComponent(left);
        addComponent(middle);
        addComponent(right);

        setComponentAlignment(middle, Alignment.MIDDLE_CENTER);

        setExpandRatio(left, 3);
        setExpandRatio(middle, 1);
        setExpandRatio(right, 3);
    }

    private VerticalLayout createMiddle(final Table agentsInQueue, final Table availableAgents) {
        VerticalLayout middle = new VerticalLayout();

        Button toQueueButton = new Button(bundle.getString("wavilon.button.queues.drag.and.drop.to.queue"), new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {

                if (null != availableAgents.getValue()) {

                    Item item = availableAgents.getItem(availableAgents.getValue());
                    Object object = agentsInQueue.addItem();


                    List<String> queuesAgents = (List<String>) model.getOutputs().get("agents");
                    queuesAgents.add(item.getItemProperty("id").getValue().toString());
                    try {
                        queuesService.updateQueue(queue, model, queuesAgents);
                        model = queuesService.getModel(model.getId());
                    } catch (IOException ignored) {
                    }


                    agentsInQueue.getContainerProperty(object, bundle.getString("wavilon.table.agents.column.name")).setValue(item.getItemProperty(bundle.getString("wavilon.table.agents.column.name")).getValue().toString());
                    agentsInQueue.getContainerProperty(object, bundle.getString("wavilon.table.agents.column.current.extension")).setValue(item.getItemProperty(bundle.getString("wavilon.table.agents.column.current.extension")).getValue().toString());
                    agentsInQueue.getContainerProperty(object, "id").setValue(item.getItemProperty("id").getValue().toString());

                    availableAgents.removeItem(availableAgents.getValue());

                } else {
                    getWindow().showNotification(bundle.getString("wavilon.error.massage.queues.drag.and.drop.no.selectable"));
                }
            }
        });
        Button fromQueueButton = new Button(bundle.getString("wavilon.button.queues.drag.and.drop.from.queue"), new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {

                if (null != agentsInQueue.getValue()) {

                    Item item = agentsInQueue.getItem(agentsInQueue.getValue());
                    Object object = availableAgents.addItem();


                    List<String> queuesAgents = (List<String>) model.getOutputs().get("agents");
                    queuesAgents.remove(item.getItemProperty("id").getValue().toString());
                    try {
                        queuesService.updateQueue(queue, model, queuesAgents);
                        model = queuesService.getModel(model.getId());
                    } catch (IOException ignored) {
                    }


                    availableAgents.getContainerProperty(object, bundle.getString("wavilon.table.agents.column.name")).setValue(item.getItemProperty(bundle.getString("wavilon.table.agents.column.name")).getValue().toString());
                    availableAgents.getContainerProperty(object, bundle.getString("wavilon.table.agents.column.current.extension")).setValue(item.getItemProperty(bundle.getString("wavilon.table.agents.column.current.extension")).getValue().toString());
                    availableAgents.getContainerProperty(object, "id").setValue(item.getItemProperty("id").getValue().toString());

                    agentsInQueue.removeItem(agentsInQueue.getValue());
                } else {
                    getWindow().showNotification(bundle.getString("wavilon.error.massage.queues.drag.and.drop.no.selectable"));
                }

            }
        });
        Label massage = new Label(bundle.getString("wavilon.form.queues.drag.and.drop.massage"));

        middle.addComponent(toQueueButton);
        middle.addComponent(fromQueueButton);
        middle.addComponent(massage);

        middle.setComponentAlignment(toQueueButton, Alignment.MIDDLE_CENTER);
        middle.setComponentAlignment(fromQueueButton, Alignment.MIDDLE_CENTER);
        middle.setComponentAlignment(massage, Alignment.MIDDLE_CENTER);

        return middle;
    }

    private void dragAndDropInit(final Table agentsInQueue, final Table availableAgents) {
        agentsInQueue.setDragMode(Table.TableDragMode.ROW);
        agentsInQueue.setDropHandler(new DropHandler() {
            public void drop(DragAndDropEvent event) {
                DataBoundTransferable t = (DataBoundTransferable) event.getTransferable();
                Object sourceItemId = t.getItemId();
                Object object = agentsInQueue.addItem();
                Item item = t.getSourceContainer().getItem(sourceItemId);

                List<String> queuesAgents = (List<String>) model.getOutputs().get("agents");
                queuesAgents.add(item.getItemProperty("id").getValue().toString());
                try {
                    queuesService.updateQueue(queue, model, queuesAgents);
                    model = queuesService.getModel(model.getId());
                } catch (IOException ignored) {
                }


                agentsInQueue.getContainerProperty(object, bundle.getString("wavilon.table.agents.column.name")).setValue(item.getItemProperty(bundle.getString("wavilon.table.agents.column.name")).getValue().toString());
                agentsInQueue.getContainerProperty(object, bundle.getString("wavilon.table.agents.column.current.extension")).setValue(item.getItemProperty(bundle.getString("wavilon.table.agents.column.current.extension")).getValue().toString());
                agentsInQueue.getContainerProperty(object, "id").setValue(item.getItemProperty("id").getValue().toString());

                availableAgents.removeItem(sourceItemId);
            }

            public AcceptCriterion getAcceptCriterion() {
                return new And(new SourceIs(availableAgents), AbstractSelect.AcceptItem.ALL);
            }
        });

        availableAgents.setDragMode(Table.TableDragMode.ROW);
        availableAgents.setDropHandler(new DropHandler() {
            public void drop(DragAndDropEvent event) {
                DataBoundTransferable t = (DataBoundTransferable) event.getTransferable();
                Object sourceItemId = t.getItemId();
                Object object = availableAgents.addItem();
                Item item = t.getSourceContainer().getItem(sourceItemId);


                List<String> queuesAgents = (List<String>) model.getOutputs().get("agents");
                queuesAgents.remove(item.getItemProperty("id").getValue().toString());
                try {
                    queuesService.updateQueue(queue, model, queuesAgents);
                    model = queuesService.getModel(model.getId());
                } catch (IOException ignored) {
                }


                availableAgents.getContainerProperty(object, bundle.getString("wavilon.table.agents.column.name")).setValue(item.getItemProperty(bundle.getString("wavilon.table.agents.column.name")).getValue().toString());
                availableAgents.getContainerProperty(object, bundle.getString("wavilon.table.agents.column.current.extension")).setValue(item.getItemProperty(bundle.getString("wavilon.table.agents.column.current.extension")).getValue().toString());
                availableAgents.getContainerProperty(object, "id").setValue(item.getItemProperty("id").getValue().toString());

                agentsInQueue.removeItem(sourceItemId);
            }

            public AcceptCriterion getAcceptCriterion() {
                return new And(new SourceIs(agentsInQueue), AbstractSelect.AcceptItem.ALL);
            }
        });
    }

    private List<CouchModel> getQueuesAgents() {
        List<CouchModel> agentsInQueueList = new LinkedList<CouchModel>();
        if (null != model.getOutputs()) {
            List<String> agents = (List<String>) model.getOutputs().get("agents");
            List<String> modifiedAgents = new LinkedList<String>();
            for (String agentId : agents) {

                try {
                    CouchModel agent = agentService.getModel(agentId);
                    agentsInQueueList.add(agent);
                } catch (Exception ignored) {
                    modifiedAgents.add(agentId);
                }
            }
            if (!modifiedAgents.isEmpty()) {
                agents.removeAll(modifiedAgents);
                try {
                    queuesService.updateQueue(queue, model, agents);
                    model = queuesService.getModel(model.getId());
                } catch (Exception ignored) {
                }
            }

        }
        return agentsInQueueList;
    }

    private List<String> createHidden() {
        List<String> tableHiddenFields = new LinkedList<String>();
        tableHiddenFields.add(bundle.getString("wavilon.table.agents.column.name"));
        tableHiddenFields.add(bundle.getString("wavilon.table.agents.column.current.extension"));
        tableHiddenFields.add("id");
        return tableHiddenFields;
    }

    private List<String> createVisible() {
        List<String> tableVisibleFields = new LinkedList<String>();
        tableVisibleFields.add(bundle.getString("wavilon.table.agents.column.name"));
        tableVisibleFields.add(bundle.getString("wavilon.table.agents.column.current.extension"));
        return tableVisibleFields;
    }

    private Queue createQueue(CouchModel model) {
        try {
            return queuesService.getQueue(model);
        } catch (Exception e) {
            return new Queue();
        }

    }

    private Table fillTable(List<CouchModel> agentList, List<String> tableVisibleFields, List<String> tableHiddenFields) {
        Table table = new Table();
        IndexedContainer tableData = createTableData(agentList, tableHiddenFields);

        table.setContainerDataSource(tableData);
        table.setWidth(100, Sizeable.UNITS_PERCENTAGE);
        table.setHeight(257, Sizeable.UNITS_PIXELS);
        table.setVisibleColumns(tableVisibleFields.toArray());
        table.setImmediate(true);
        table.setSelectable(true);

        table.setColumnExpandRatio(bundle.getString("wavilon.table.agents.column.name"), 1);
        table.setColumnExpandRatio(bundle.getString("wavilon.table.agents.column.current.extension"), 2);

        return table;
    }

    private IndexedContainer createTableData(List<CouchModel> agentList, List<String> tableHiddenFields) {
        IndexedContainer ic = new IndexedContainer();

        for (String field : tableHiddenFields) {
            ic.addContainerProperty(field, String.class, "");
        }

        for (CouchModel agent : agentList) {
            Object object = ic.addItem();
            ic.getContainerProperty(object, bundle.getString("wavilon.table.agents.column.name")).setValue(agent.getProperties().get("name"));
            CouchModelLite extension = CouchModelUtil.getCouchModelLite((String) agent.getOutputs().get("extension"), bundle);

            ic.getContainerProperty(object, bundle.getString("wavilon.table.agents.column.current.extension")).setValue(extension);
            ic.getContainerProperty(object, "id").setValue(agent.getId());
        }

        return ic;
    }

    private CouchModel createModel(String id) {
        try {
            return queuesService.getModel(id);
        } catch (Exception e) {
            return CouchModelUtil.newCouchModel(request, CouchTypes.agent);
        }
    }

}
