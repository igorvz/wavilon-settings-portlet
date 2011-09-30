package com.aimprosoft.wavilon.ui.menuitems.forms;

import com.aimprosoft.wavilon.application.GenericPortletApplication;
import com.aimprosoft.wavilon.model.Agent;
import com.aimprosoft.wavilon.model.Queue;
import com.aimprosoft.wavilon.service.AgentDatabaseService;
import com.aimprosoft.wavilon.service.QueueDatabaseService;
import com.aimprosoft.wavilon.spring.ObjectFactory;
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
import java.util.*;

public class QueuesDragAndDropAgents extends HorizontalLayout {


    private QueueDatabaseService queuesService = ObjectFactory.getBean(QueueDatabaseService.class);
    private AgentDatabaseService agentsService = ObjectFactory.getBean(AgentDatabaseService.class);
    private ResourceBundle bundle;
    private PortletRequest request;
    private Queue queue;


    public QueuesDragAndDropAgents(ResourceBundle bundle) {
        this.bundle = bundle;
    }

    public void init(String id) {
        request = ((GenericPortletApplication) getApplication()).getPortletRequest();
        createQueue(id);
        initLayout();
    }

    private void initLayout() {
        setWidth(100, Sizeable.UNITS_PERCENTAGE);
        setHeight(250, Sizeable.UNITS_PIXELS);

        List<Agent> availableAgentList = Collections.emptyList();
        try {
            availableAgentList = agentsService.getAllAgentsByUser(PortalUtil.getUserId(request), PortalUtil.getScopeGroupId(request));
        } catch (Exception ignored) {
        }

        final List<Agent> agentsInQueueList = getQueuesAgents();
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

                List<String> queuesAgents = queue.getAgents();
                queuesAgents.add(item.getItemProperty("id").getValue().toString());
                queue.setAgents(queuesAgents);
                try {
                    queuesService.updateQueue(queue);
                    queue = queuesService.getQueue(queue.getId());
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


                List<String> queuesAgents = queue.getAgents();
                queuesAgents.remove(item.getItemProperty("id").getValue().toString());
                queue.setAgents(queuesAgents);
                try {
                    queuesService.updateQueue(queue);
                    queue = queuesService.getQueue(queue.getId());
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

    private List<Agent> getQueuesAgents() {
        List<Agent> agentsInQueueList = new ArrayList<Agent>();
        if (queue.getAgents() != null) {
            for (String agentId : queue.getAgents()) {
                try {
                    Agent agent = agentsService.getAgent(agentId);
                    agentsInQueueList.add(agent);
                } catch (IOException ignored) {
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

    private void createQueue(String id) {
        try {
            this.queue = queuesService.getQueue(id);
        } catch (IOException ignored) {
        }
    }

    private Table fillTable(List<Agent> agentList, List<String> tableVisibleFields, List<String> tableHiddenFields) {
        Table table = new Table();
        IndexedContainer tableData = createTableData(agentList, tableHiddenFields);

        table.setContainerDataSource(tableData);
        table.setWidth(100, Sizeable.UNITS_PERCENTAGE);
        table.setHeight(100, Sizeable.UNITS_PERCENTAGE);
        table.setVisibleColumns(tableVisibleFields.toArray());
        table.setImmediate(true);

        return table;
    }

    private IndexedContainer createTableData(List<Agent> agentList, List<String> tableHiddenFields) {
        IndexedContainer ic = new IndexedContainer();

        for (String field : tableHiddenFields) {
            ic.addContainerProperty(field, String.class, "");
        }

        for (Agent agent : agentList) {
            Object object = ic.addItem();
            ic.getContainerProperty(object, "NAME").setValue(agent.getName());
            ic.getContainerProperty(object, "CURRENT EXTENSION").setValue(agent.getCurrentExtension());
            ic.getContainerProperty(object, "id").setValue(agent.getId());
        }

        return ic;
    }


}
