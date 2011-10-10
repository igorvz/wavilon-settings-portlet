package com.aimprosoft.wavilon.ui;

import com.aimprosoft.wavilon.application.GenericPortletApplication;
import com.aimprosoft.wavilon.config.ApplicationProperties;
import com.aimprosoft.wavilon.couch.CouchModel;
import com.aimprosoft.wavilon.couch.CouchTypes;
import com.aimprosoft.wavilon.model.Agent;
import com.aimprosoft.wavilon.model.Extension;
import com.aimprosoft.wavilon.model.PhoneNumber;
import com.aimprosoft.wavilon.model.Queue;
import com.aimprosoft.wavilon.service.AgentDatabaseService;
import com.aimprosoft.wavilon.service.ExtensionDatabaseService;
import com.aimprosoft.wavilon.service.PhoneNumberDatabaseService;
import com.aimprosoft.wavilon.service.QueueDatabaseService;
import com.aimprosoft.wavilon.spring.ObjectFactory;
import com.aimprosoft.wavilon.ui.menuitems.*;
import com.aimprosoft.wavilon.util.CouchModelUtil;
import com.liferay.portal.util.PortalUtil;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.Reindeer;

import javax.portlet.PortletRequest;
import java.util.Collections;
import java.util.Iterator;
import java.util.ResourceBundle;

public class SettingsPage extends VerticalLayout {
    private ResourceBundle bundle;
    private VerticalLayout leftColumn;
    private VerticalLayout detailsContent;
    private PortletRequest request;
    private ApplicationProperties properties = ObjectFactory.getBean(ApplicationProperties.class);

    public SettingsPage(final ResourceBundle bundle) {
        this.bundle = bundle;
    }

    public void init() {

        addStyleName("settingsPanel");

        HorizontalSplitPanel panel = new HorizontalSplitPanel();
        panel.setSplitPosition(250, Sizeable.UNITS_PIXELS);
        panel.addStyleName(Reindeer.SPLITPANEL_SMALL);
        panel.setLocked(false);
        panel.setHeight(550, Sizeable.UNITS_PIXELS);
        panel.setWidth("100%");
        addComponent(panel);

        leftColumn = new VerticalLayout();
        leftColumn.setStyleName("leftcolumn");
        panel.setFirstComponent(leftColumn);

        VerticalLayout rightColumn = new VerticalLayout();
        rightColumn.setStyleName("rightcolumn");
        rightColumn.setMargin(false);
        rightColumn.setSizeFull();
        panel.setSecondComponent(rightColumn);

        VerticalLayout detailsBox = new VerticalLayout();
        detailsBox.setStyleName("detailsBox");
        setSides(detailsBox);

        detailsContent = new VerticalLayout();
        detailsContent.setStyleName("detailsContent");
        detailsBox.addComponent(detailsContent);

        rightColumn.addComponent(detailsBox);

        panel.addStyleName("gridLayout");
        addButtons();

        checkEntities();

        detailsContent.removeAllComponents();
        PhoneNumbersContent phoneNumbersContent = new PhoneNumbersContent(bundle);
        detailsContent.addComponent(phoneNumbersContent);
        phoneNumbersContent.init();

        setExpandRatio(panel, 0);
    }

    private void checkEntities() {
        request = ((GenericPortletApplication) getApplication()).getPortletRequest();
        if (CouchModelUtil.getForwards(getUserId(), getOrganizationId()).isEmpty()) {
            createEmptyEntities();
        }
    }

    private long getOrganizationId() {
        try {
            return PortalUtil.getScopeGroupId(request);
        } catch (Exception ignored) {
            return 0l;
        }
    }

    private long getUserId() {
        return PortalUtil.getUserId(request);
    }

    private void createEmptyEntities() {
        ExtensionDatabaseService extensionService = ObjectFactory.getBean(ExtensionDatabaseService.class);
        AgentDatabaseService agentService = ObjectFactory.getBean(AgentDatabaseService.class);
        QueueDatabaseService queueService = ObjectFactory.getBean(QueueDatabaseService.class);
        PhoneNumberDatabaseService phoneNumberService = ObjectFactory.getBean(PhoneNumberDatabaseService.class);


        CouchModel extensionModel = CouchModelUtil.newCouchModel(request, CouchTypes.extension);
        Extension extension = new Extension();
        extension.setName(properties.getStartNodeExtensionName());
        extension.setDestination(properties.getStartNodeExtensionDestination());
        extension.setChannel(properties.getStartNodeExtensionChannel());

        CouchModel phoneNumberModel = CouchModelUtil.newCouchModel(request, CouchTypes.service);

        PhoneNumber phoneNumber = new PhoneNumber();
        phoneNumber.setName(properties.getStartNodePhoneNumberName());
        phoneNumber.setLocator(properties.getStartNodePhoneNumberLocator());


        CouchModel agentModel = CouchModelUtil.newCouchModel(request, CouchTypes.agent);

        Agent agent = new Agent();
        agent.setName(properties.getStartNodeAgentName());

        CouchModel queueModel = CouchModelUtil.newCouchModel(request, CouchTypes.queue);

        Queue queue = new Queue();
        queue.setName(properties.getStartNodeQueueName());
        queue.setMaxTime(properties.getStartNodeQueueMaxTime());
        queue.setMaxLength(properties.getStartNodeQueueMaxLength());
        queue.setForwardToOnMaxTime(extensionModel.getId());
        queue.setForwardToOnMaxLength(extensionModel.getId());
        queue.setMusicOnHold(properties.getStartNodeQueueMusicOnHold());

        try {
            phoneNumberService.addPhoneNumber(phoneNumber, phoneNumberModel, extensionModel.getId());
            extensionService.addExtension(extension, extensionModel);
            agentService.addAgent(agent, agentModel, extensionModel.getId());
            queueService.addQueue(queue, queueModel, Collections.<String>emptyList());
        } catch (Exception ignored) {
        }

    }

    private void addButtons() {

        Button phoneNumbers = new NativeButton(bundle.getString("wavilon.settings.services.phoneNumbers"));
        phoneNumbers.addStyleName("phoneNumbersButton");
        phoneNumbers.addListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                Button button = event.getButton();

                assignActiveButton(button);
                detailsContent.removeAllComponents();
                PhoneNumbersContent phoneNumbersContent = new PhoneNumbersContent(bundle);
                detailsContent.addComponent(phoneNumbersContent);
                phoneNumbersContent.init();
            }
        });

        Button virtualNumbers = new NativeButton(bundle.getString("wavilon.settings.services.virtualNumbers"));
        virtualNumbers.addStyleName("label");
        virtualNumbers.addListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                Button button = event.getButton();

                assignActiveButton(button);
                detailsContent.removeAllComponents();
                VirtualNumbersContent virtualNumbersContent = new VirtualNumbersContent(bundle);
                detailsContent.addComponent(virtualNumbersContent);
                virtualNumbersContent.init();
            }
        });

        Button queues = new NativeButton(bundle.getString("wavilon.settings.queues"));
        queues.addStyleName("label");
        queues.addListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                Button button = event.getButton();

                assignActiveButton(button);

                detailsContent.removeAllComponents();
                QueuesContent queuesContent = new QueuesContent(bundle);
                detailsContent.addComponent(queuesContent);
                queuesContent.init();
            }
        });

        Button agents = new NativeButton(bundle.getString("wavilon.settings.agents"));
        agents.addStyleName("label");
        agents.addListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                Button button = event.getButton();

                assignActiveButton(button);

                detailsContent.removeAllComponents();
                AgentsContent agentsContent = new AgentsContent(bundle);
                detailsContent.addComponent(agentsContent);
                agentsContent.init();
            }
        });

        Button extensions = new NativeButton(bundle.getString("wavilon.settings.extensions"));
        extensions.addStyleName("label");
        extensions.addListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                Button button = event.getButton();
                ExtensionContent extensionContent = new ExtensionContent(bundle);

                assignActiveButton(button);

                detailsContent.removeAllComponents();
                detailsContent.addComponent(extensionContent);
                extensionContent.init();

            }
        });

        Button recordings = new NativeButton(bundle.getString("wavilon.settings.recordings"));
        recordings.addStyleName("label");
        recordings.addListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                Button button = event.getButton();
                RecordingsContent recordingsContent = new RecordingsContent(bundle);

                assignActiveButton(button);

                detailsContent.removeAllComponents();
                detailsContent.addComponent(recordingsContent);
                recordingsContent.init();
            }
        });

        leftColumn.addComponent(phoneNumbers);
        leftColumn.addComponent(virtualNumbers);
        leftColumn.addComponent(queues);
        leftColumn.addComponent(agents);
        leftColumn.addComponent(extensions);
        leftColumn.addComponent(recordings);
    }

    private void assignActiveButton(Button button) {
        removeButtonSelection();
        button.addStyleName("active");
    }

    //remove selection for all buttons
    private void removeButtonSelection() {
        Iterator<Component> componentIterator = leftColumn.getComponentIterator();
        while (componentIterator.hasNext()) {
            Component component = componentIterator.next();
            //make sure component is button
            if (component instanceof Button) {
                component.removeStyleName("active");
            }
        }
    }

    private void setSides(Component component) {
        component.setWidth(100, Sizeable.UNITS_PERCENTAGE);
        component.setHeight(100, Sizeable.UNITS_PERCENTAGE);
    }
}
