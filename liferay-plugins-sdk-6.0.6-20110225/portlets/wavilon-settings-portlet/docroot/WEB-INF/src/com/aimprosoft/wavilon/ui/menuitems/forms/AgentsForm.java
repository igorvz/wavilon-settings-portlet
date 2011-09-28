package com.aimprosoft.wavilon.ui.menuitems.forms;

import com.aimprosoft.wavilon.application.GenericPortletApplication;
import com.aimprosoft.wavilon.model.Agent;
import com.aimprosoft.wavilon.model.Extension;
import com.aimprosoft.wavilon.service.AgentDatabaseService;
import com.aimprosoft.wavilon.service.ExtensionDatabaseService;
import com.aimprosoft.wavilon.spring.ObjectFactory;
import com.liferay.portal.util.PortalUtil;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.*;

import javax.portlet.PortletRequest;
import java.util.*;

public class AgentsForm extends Window {
    private AgentDatabaseService service = ObjectFactory.getBean(AgentDatabaseService.class);
    private ExtensionDatabaseService extensionService = ObjectFactory.getBean(ExtensionDatabaseService.class);
    private ResourceBundle bundle;
    private PortletRequest request;
    private Table table;
    private Agent agent;


    public AgentsForm(ResourceBundle bundle, Table table) {
        this.bundle = bundle;
        this.table = table;
        setCaption("Edit Agent");
    }

    public void init(String id) {
        request = ((GenericPortletApplication) getApplication()).getPortletRequest();
        agent = createAgent(id);

        VerticalLayout content = new VerticalLayout();
        content.addStyleName("formRegion");

        addComponent(content);

        Label headerForm = createHeader(id, agent);
        content.addComponent(headerForm);

        final Form form = createForm();
        content.addComponent(form);


        HorizontalLayout buttons = createButtons(content);


        Button cancel = new Button("Cancel", new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                close();
            }
        });
        buttons.addComponent(cancel);

        Button save = new Button(bundle.getString("wavilon.settings.validation.form.button.save"), new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                try {
                    form.commit();

                    String name = (String) form.getField("firstName").getValue();
                    String currentExtension = (String) form.getField("extensions").getValue();
                    agent.setName(name);
                    agent.setCurrentExtension(currentExtension);

                    service.addAgent(agent);

                    if (null != agent.getRevision()) {
                        table.removeItem(table.getValue());
                        table.select(null);
                    }

                    Object object = table.addItem();
                    table.getContainerProperty(object, "NAME").setValue(agent.getName());
                    table.getContainerProperty(object, "CURRENT EXTENSION").setValue(agent.getCurrentExtension());
                    table.getContainerProperty(object, "id").setValue(agent.getId());

                    getWindow().showNotification("Well done");
                    close();
                } catch (Exception ignored) {
                }
            }
        });
        buttons.addComponent(save);
    }

    private Agent createAgent(String id) {
        if ("-1".equals(id)) {
            return newAgent();
        }
        try {
            return service.getAgent(id);
        } catch (Exception e) {
            return newAgent();
        }

    }

    private Agent newAgent() {
        Agent newAgent = new Agent();

        try {
            newAgent.setId(UUID.randomUUID().toString());
            newAgent.setLiferayUserId(PortalUtil.getUserId(request));
            newAgent.setLiferayOrganizationId(PortalUtil.getScopeGroupId(request));
            newAgent.setLiferayPortalId(PortalUtil.getCompany(request).getWebId());
        } catch (Exception ignored) {
        }

        newAgent.setName("");
        return newAgent;
    }

    private Form createForm() {
        Form form = new Form();
        form.addStyleName("labelField");

        TextField firstName = new TextField("First Name");
        firstName.setRequired(true);
        firstName.setRequiredError("Empty field First Name");

        List<String> extensionList = createExtensions();
        ComboBox extensions = new ComboBox("Current extension", extensionList);
        extensions.setImmediate(true);
        extensions.setNullSelectionAllowed(false);
        extensions.setNullSelectionItemId("Select . . .");
        extensions.setRequired(true);
        extensions.setRequiredError("Empty field \"Current extension\"");


        if (null != agent.getRevision() && !"".equals(agent.getRevision())) {
            firstName.setValue(agent.getName());
            extensions.setValue(agent.getCurrentExtension());
        }
        form.addField("firstName", firstName);
        form.addField("extensions", extensions);

        return form;
    }

    private List<String> createExtensions() {
        List<String> extensions = new LinkedList<String>();

        List<Extension> extensionList = getExtensions();
        for (Extension extension : extensionList) {
            extensions.add(extension.getName());
        }
        Collections.sort(extensions);
        extensions.add(0,"Select . . .");
        return extensions;
    }

    private List<Extension> getExtensions() {
        try {
            return extensionService.getAllExtensionByUserId(PortalUtil.getUserId(request), PortalUtil.getScopeGroupId(request));
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    private Label createHeader(String id, Agent agent) {
        Label headerForm = new Label("-1".equals(id) ? "New Agent" : agent.getName());

        headerForm.setHeight(27, Sizeable.UNITS_PIXELS);
        headerForm.setWidth("100%");
        headerForm.addStyleName("headerForm");

        return headerForm;
    }

    private HorizontalLayout createButtons(VerticalLayout content) {
        HorizontalLayout buttons = new HorizontalLayout();
        content.addComponent(buttons);
        content.setComponentAlignment(buttons, Alignment.MIDDLE_RIGHT);
        buttons.addStyleName("buttonsPanel");
        return buttons;
    }

}
