package com.aimprosoft.wavilon.ui.menuitems.forms;

import com.aimprosoft.wavilon.application.GenericPortletApplication;
import com.aimprosoft.wavilon.model.Agent;
import com.aimprosoft.wavilon.model.Queue;
import com.aimprosoft.wavilon.service.AgentDatabaseService;
import com.aimprosoft.wavilon.service.QueueDatabaseService;
import com.aimprosoft.wavilon.spring.ObjectFactory;
import com.liferay.portal.util.PortalUtil;
import com.vaadin.data.validator.IntegerValidator;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.*;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import javax.portlet.PortletRequest;
import java.io.IOException;
import java.util.*;

public class QueuesForm extends VerticalLayout {
    private ResourceBundle bundle;
    private QueueDatabaseService service = ObjectFactory.getBean(QueueDatabaseService.class);
    private AgentDatabaseService agentService = ObjectFactory.getBean(AgentDatabaseService.class);

    private Queue queue;

    public QueuesForm(final ResourceBundle bundle, final String queueId, final VerticalLayout queuesFormLayout) {
        this.bundle = bundle;
        queue = getQueue(queueId);


        final Form queueForm = new QueuesFormLayout();

        //first row
        TextField maxTimeInput = new TextField();
        maxTimeInput.setWidth(40, Sizeable.UNITS_PIXELS);
        maxTimeInput.setRequired(true);
        maxTimeInput.setRequiredError(bundle.getString("wavilon.settings.validation.form.error.empty.max.time.input"));
        maxTimeInput.addValidator(new IntegerValidator(bundle.getString("wavilon.settings.validation.form.error.integer.max.time.input")));

        List<String> typeList = new LinkedList<String>();
        typeList.add("Type 1");
        typeList.add("Type 2");
        typeList.add("Type 3");

        List<String> nodeList = new LinkedList<String>();
        nodeList.add("Node 1");
        nodeList.add("Node 2");
        nodeList.add("Node 3");

        List<String> musicOnHold = new LinkedList<String>();
        musicOnHold.add("Music 1");
        musicOnHold.add("Music 2");
        musicOnHold.add("Music 3");

        List<String> availableAgents = null;
        List<Agent> agentList = null;
        try {
            agentList = agentService.getAllAgents();
        } catch (IOException ignored) {
            agentList = Collections.emptyList();
        }


        ComboBox firstRowType = new ComboBox();
        firstRowType.setWidth(100, Sizeable.UNITS_PIXELS);
        ComboBox firstRowSelectNode = new ComboBox();
        firstRowSelectNode.setWidth(100, Sizeable.UNITS_PIXELS);

        //second row
        TextField maxLengthInput = new TextField();
        maxLengthInput.setWidth(40, Sizeable.UNITS_PIXELS);
        maxLengthInput.setRequired(true);
        maxLengthInput.setRequiredError(bundle.getString("wavilon.settings.validation.form.error.empty.max.length.input"));
        maxLengthInput.addValidator(new IntegerValidator(bundle.getString("wavilon.settings.validation.form.error.integer.max.length.input")));
        ComboBox secondRowType = new ComboBox();
        secondRowType.setWidth(100, Sizeable.UNITS_PIXELS);
        ComboBox secondRowSelectNode = new ComboBox();
        secondRowSelectNode.setWidth(100, Sizeable.UNITS_PIXELS);

        //third row
        ComboBox hold = new ComboBox();
        hold.setWidth(100, Sizeable.UNITS_PIXELS);

        TextField title = new TextField();
        title.setWidth(120, Sizeable.UNITS_PIXELS);
        title.setRequired(true);
        title.setRequiredError(bundle.getString("wavilon.settings.validation.form.error.empty.title"));


        //twin col select
        TwinColSelect agents = new TwinColSelect();
        agents.setHeight(120, Sizeable.UNITS_PIXELS);
        agents.setNullSelectionAllowed(true);
        agents.setMultiSelect(true);
        agents.setImmediate(true);
        agents.setLeftColumnCaption("Available agents");
        agents.setRightColumnCaption("Agents In Queue");

        if (null != queue.getRevision()) {
            title.setValue(queue.getTitle());
            maxTimeInput.setValue(queue.getMaxTime());
            maxLengthInput.setValue(queue.getMaxLength());

            if(null != queue.getAgents()){
            availableAgents = queue.getAgents();
            }else {
                availableAgents= Collections.emptyList();
            }
        }
        for (String s : typeList) {
            secondRowType.addItem(s);
            firstRowType.addItem(s);

            if (null != queue.getRevision()) {
                if (queue.getDistinctionMaxTimeType().equals(s)) {
                    firstRowType.setValue(s);
                }
                if (queue.getDistinctionFullType().equals(s)) {
                    secondRowType.setValue(s);
                }
            }

        }
        for (String s : nodeList) {
            firstRowSelectNode.addItem(s);
            secondRowSelectNode.addItem(s);

            if (null != queue.getRevision()) {
                if (queue.getDistinctionMaxTimeNode().equals(s)) {
                    firstRowSelectNode.setValue(s);
                }
                if (queue.getDistinctionFullNode().equals(s)) {
                    secondRowSelectNode.setValue(s);
                }
            }
        }
        for (String s : musicOnHold) {
            hold.addItem(s);

            if (null != queue.getRevision()) {
                if (queue.getMusicOnHold().equals(s)) {
                    hold.setValue(s);
                }
            }
        }

        List<Agent> selectedAgents = new ArrayList<Agent>();
        for (Agent agent : agentList) {
            agents.addItem(agent);
            if (null != queue.getRevision() && queue.getAgents()!= null) {
                for (String agentId: queue.getAgents()){
                    if (agent.getId().equals(agentId)){
                        selectedAgents.add(agent);
                    }
                }
            }
        }

        //set selected agent
        agents.setValue(Collections.unmodifiableCollection(selectedAgents));

        queueForm.addField("maxTimeInput", maxTimeInput);
        queueForm.addField("firstRowType", firstRowType);
        queueForm.addField("firstRowSelectNode", firstRowSelectNode);

        queueForm.addField("maxLengthInput", maxLengthInput);
        queueForm.addField("secondRowType", secondRowType);
        queueForm.addField("secondRowSelectNode", secondRowSelectNode);

        queueForm.addField("hold", hold);
        queueForm.addField("title", title);

        queueForm.addField("agents", agents);

        Button apply = new Button(bundle.getString("wavilon.settings.validation.form.button.save"), new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                try {
                    queueForm.commit();


                    if (null == queue.getRevision()) {
                        queue.setId(UUID.randomUUID().toString());
                        PortletRequest request = ((GenericPortletApplication) getApplication()).getPortletRequest();
                        queue.setLiferayUserId(PortalUtil.getUserId(request));
                        queue.setLiferayOrganizationId(PortalUtil.getScopeGroupId(request));
                        queue.setLiferayPortalId(PortalUtil.getCompany(request).getWebId());
                    }

                    queue.setMaxTime(NumberUtils.toInt((String) queueForm.getField("maxTimeInput").getValue().toString()));
                    queue.setMaxLength(NumberUtils.toInt((String) queueForm.getField("maxLengthInput").getValue().toString()));
                    queue.setDistinctionMaxTimeType((String) queueForm.getField("firstRowType").getValue());
                    queue.setDistinctionMaxTimeNode((String) queueForm.getField("firstRowSelectNode").getValue());
                    queue.setDistinctionFullType((String) queueForm.getField("secondRowType").getValue());
                    queue.setDistinctionFullNode((String) queueForm.getField("secondRowSelectNode").getValue());
                    queue.setMusicOnHold((String) queueForm.getField("hold").getValue());
                    queue.setTitle((String) queueForm.getField("title").getValue());


                    Set set = (Set) queueForm.getField("agents").getValue();
                    List<String> agentIDS = new LinkedList<String>();
                    for (Object o : set) {
                        Agent agent = (Agent) o;
                        agentIDS.add(agent.getId());
                    }
                    queue.setAgents(agentIDS);

                    service.addQueue(queue);
                    queuesFormLayout.removeAllComponents();
                    getWindow().showNotification("Well done");
                } catch (Exception e) {
                    e.getMessage();
                }
            }
        });
        addComponent(queueForm);
        addComponent(apply);
        setComponentAlignment(apply, Alignment.BOTTOM_RIGHT);

        if (null != queue.getRevision()) {
            Button remove = new Button("Remove", new Button.ClickListener() {
                public void buttonClick(Button.ClickEvent event) {
                    try {
                        service.removeQueue(queueId);
                    } catch (IOException ignored) {
                    }
                    queuesFormLayout.removeAllComponents();
                }
            });
            addComponent(remove);
            setComponentAlignment(remove, Alignment.BOTTOM_RIGHT);
        }

    }

    private Queue getQueue(String queueId) {
        if (StringUtils.isEmpty(queueId)) {
            return new Queue();
        }
        try {
            return service.getQueue(queueId);
        } catch (IOException e) {
            return new Queue();
        }
    }

    private static class QueuesFormLayout extends Form {
        private GridLayout layout;

        QueuesFormLayout() {
            layout = new GridLayout(41, 4);
            layout.setMargin(true, false, false, true);
            layout.setSpacing(true);


            setLayout(layout);

            Label maxTime = new Label("Queue Max Time");
            layout.addComponent(maxTime, 0, 0, 9, 0);

            Label seconds = new Label("seconds");
            layout.addComponent(seconds, 13, 0, 17, 0);

            Label firstRowSeparator = new Label("");
            firstRowSeparator.setWidth(20, Sizeable.UNITS_PIXELS);
            layout.addComponent(firstRowSeparator, 18, 0);

            Label firstRowDistinction = new Label("Distinction on Max Time");
            layout.addComponent(firstRowDistinction, 19, 0, 32, 0);


            Label maxLength = new Label("Queue Max Length");
            layout.addComponent(maxLength, 0, 1, 9, 1);

            Label calls = new Label("calls");
            layout.addComponent(calls, 13, 1, 17, 1);

            Label secondRowSeparator = new Label("");
            secondRowSeparator.setWidth(20, Sizeable.UNITS_PIXELS);
            layout.addComponent(secondRowSeparator, 18, 1);

            Label secondRowDistinction = new Label("Distinction on queue full");
            layout.addComponent(secondRowDistinction, 19, 1, 32, 1);

            Label musicAnHold = new Label("Music an Hold");
            layout.addComponent(musicAnHold, 0, 2, 7, 2);

            Label title = new Label("Queues Title");
            layout.addComponent(title, 22, 2, 29, 2);
        }


        @Override
        protected void attachField(Object propertyId, Field field) {
            if (propertyId.equals("maxTimeInput")) {
                layout.addComponent(field, 10, 0, 12, 0);
            } else if (propertyId.equals("firstRowType")) {
                layout.addComponent(field, 33, 0, 36, 0);
            } else if (propertyId.equals("firstRowSelectNode")) {
                layout.addComponent(field, 37, 0, 40, 0);
            } else if (propertyId.equals("maxLengthInput")) {
                layout.addComponent(field, 10, 1, 12, 1);
            } else if (propertyId.equals("secondRowType")) {
                layout.addComponent(field, 33, 1, 36, 1);
            } else if (propertyId.equals("secondRowSelectNode")) {
                layout.addComponent(field, 37, 1, 40, 1);
            } else if (propertyId.equals("hold")) {
                layout.addComponent(field, 10, 2, 20, 2);
            } else if (propertyId.equals("agents")) {
                layout.addComponent(field, 10, 3, 30, 3);
            } else if (propertyId.equals("title")) {
                layout.addComponent(field, 30, 2, 40, 2);
            }
        }


    }
}
