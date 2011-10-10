package com.aimprosoft.wavilon.ui.menuitems.forms;

import com.aimprosoft.wavilon.application.GenericPortletApplication;
import com.aimprosoft.wavilon.couch.CouchModel;
import com.aimprosoft.wavilon.couch.CouchModelLite;
import com.aimprosoft.wavilon.couch.CouchTypes;
import com.aimprosoft.wavilon.model.Agent;
import com.aimprosoft.wavilon.model.Queue;
import com.aimprosoft.wavilon.service.AgentDatabaseService;
import com.aimprosoft.wavilon.service.CouchModelLiteDatabaseService;
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
        setHeight(250, Sizeable.UNITS_PIXELS);

        List<CouchModel> availableAgentList = Collections.emptyList();
        try {
            availableAgentList = agentService.getAllUsersCouchModelAgent(PortalUtil.getUserId(request), PortalUtil.getScopeGroupId(request));
        } catch (Exception ignored) {
        }

        final List<CouchModel> agentsInQueueList = getQueuesAgents();
        availableAgentList.removeAll(agentsInQueueList);

        final List<String> tableVisibleFields = createVisible();
        List<String> tableHiddenFields = createHidden();

        VerticalLayout left = new VerticalLayout();
        addComponent(left);
        left.addComponent(new Label("Agents In Queue"));

        VerticalLayout middle = new VerticalLayout();
        middle.setWidth(50, Sizeable.UNITS_PIXELS);
        addComponent(middle);

        VerticalLayout right = new VerticalLayout();
        addComponent(right);
        right.addComponent(new Label("Available Agents"));


        final Table agentsInQueue = fillTable(agentsInQueueList, tableVisibleFields, tableHiddenFields);
        final Table availableAgents = fillTable(availableAgentList, tableVisibleFields, tableHiddenFields);

        dragAndDropInit(agentsInQueue, availableAgents);

        left.addComponent(agentsInQueue);
        right.addComponent(availableAgents);


        setExpandRatio(left, 3);
        setExpandRatio(middle, 1);
        setExpandRatio(right, 3);
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


                agentsInQueue.getContainerProperty(object, "NAME").setValue(item.getItemProperty("NAME").getValue().toString());
                agentsInQueue.getContainerProperty(object, "CURRENT EXTENSION").setValue(item.getItemProperty("CURRENT EXTENSION").getValue().toString());
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


                availableAgents.getContainerProperty(object, "NAME").setValue(item.getItemProperty("NAME").getValue().toString());
                availableAgents.getContainerProperty(object, "CURRENT EXTENSION").setValue(item.getItemProperty("CURRENT EXTENSION").getValue().toString());
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
            List<String> agents = (List) model.getOutputs().get("agents");
            for (String agentId : agents) {
                try {
                    CouchModel agent = agentService.getModel(agentId);
                    agentsInQueueList.add(agent);
                } catch (Exception ignored) {
                    Agent agent = new Agent();
                    agent.setName("This has been removed!");
                }
            }
        }
        return agentsInQueueList;
    }

    private List<String> createHidden() {
        List<String> tableHiddenFields = new LinkedList<String>();
        tableHiddenFields.add("NAME");
        tableHiddenFields.add("CURRENT EXTENSION");
        tableHiddenFields.add("id");
        return tableHiddenFields;
    }

    private List<String> createVisible() {
        List<String> tableVisibleFields = new LinkedList<String>();
        tableVisibleFields.add("NAME");
        tableVisibleFields.add("CURRENT EXTENSION");
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
        table.setHeight(100, Sizeable.UNITS_PERCENTAGE);
        table.setVisibleColumns(tableVisibleFields.toArray());
        table.setImmediate(true);

        return table;
    }

    private IndexedContainer createTableData(List<CouchModel> agentList, List<String> tableHiddenFields) {
        IndexedContainer ic = new IndexedContainer();

        for (String field : tableHiddenFields) {
            ic.addContainerProperty(field, String.class, "");
        }

        for (CouchModel agent : agentList) {
            Object object = ic.addItem();
            ic.getContainerProperty(object, "NAME").setValue(agent.getProperties().get("name"));
            CouchModelLite extension = CouchModelUtil.getCouchModelLite((String) agent.getOutputs().get("extension"));

            ic.getContainerProperty(object, "CURRENT EXTENSION").setValue(extension);
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
