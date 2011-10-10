package com.aimprosoft.wavilon.ui.menuitems.forms;

import com.aimprosoft.wavilon.application.GenericPortletApplication;
import com.aimprosoft.wavilon.couch.CouchModel;
import com.aimprosoft.wavilon.couch.CouchModelLite;
import com.aimprosoft.wavilon.couch.CouchTypes;
import com.aimprosoft.wavilon.model.Agent;
import com.aimprosoft.wavilon.service.AgentDatabaseService;
import com.aimprosoft.wavilon.service.CouchModelLiteDatabaseService;
import com.aimprosoft.wavilon.spring.ObjectFactory;
import com.aimprosoft.wavilon.util.CouchModelUtil;
import com.liferay.portal.util.PortalUtil;
import com.vaadin.Application;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.*;

import javax.portlet.PortletRequest;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

public class AgentsForm extends Window {
    private AgentDatabaseService service = ObjectFactory.getBean(AgentDatabaseService.class);
    private CouchModelLiteDatabaseService modelLiteService = ObjectFactory.getBean(CouchModelLiteDatabaseService.class);
    private ResourceBundle bundle;
    private PortletRequest request;
    private Table table;
    private Application application;
    private CouchModel model;
    private Agent agent;

    public AgentsForm(ResourceBundle bundle, Table table) {
        this.bundle = bundle;
        this.table = table;
    }

    public void init(String id, final Object itemId) {
        application = getApplication();
        request = ((GenericPortletApplication) application).getPortletRequest();
        model = createModel(id);
        agent = createAgent(model);

        if ("-1".equals(id)) {
            setCaption("New Agent");
        } else {
            setCaption("Edit Agent");
        }

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
                    String extension = ((CouchModelLite)form.getField("extensions").getValue()).getId();

                    agent.setName(name);

                    service.addAgent(agent, model, extension);

                    if (null != model.getRevision()) {
                        table.removeItem(itemId);
                        table.select(null);
                    }


                    final Object object = table.addItem();


                    Button.ClickListener listener = new Button.ClickListener() {
                        public void buttonClick(Button.ClickEvent event) {

                            table.select(object);
                            String phoneNumbersID = (String) table.getItem(object).getItemProperty("id").getValue();
                            ConfirmingRemove confirmingRemove = new ConfirmingRemove(bundle);
                            application.getMainWindow().addWindow(confirmingRemove);
                            confirmingRemove.init(phoneNumbersID, table);
                            confirmingRemove.center();
                            confirmingRemove.setWidth("300px");
                            confirmingRemove.setHeight("180px");
                        }
                    };

                    table.getContainerProperty(object, "NAME").setValue(agent.getName());
                    table.getContainerProperty(object, "CURRENT EXTENSION").setValue(form.getField("extensions").getValue());
                    table.getContainerProperty(object, "id").setValue(model.getId());
                    table.getContainerProperty(object, "").setValue(new Button("-", listener));

                    getWindow().showNotification("Well done");
                    close();
                } catch (Exception ignored) {
                }
            }
        });
        buttons.addComponent(save);
    }

    private Agent createAgent(CouchModel model) {
        if (null == model.getRevision()) {
            return newAgent();
        }
        try {
            return service.getAgent(model);
        } catch (Exception e) {
            return newAgent();
        }
    }

    private Agent newAgent() {
        Agent newAgent = new Agent();

        newAgent.setName("");

        return newAgent;
    }

    private Form createForm() {
        Form form = new Form();
        form.addStyleName("labelField");

        TextField firstName = new TextField("First Name");
        firstName.setRequired(true);
        firstName.setRequiredError("Empty field First Name");

        ComboBox extensions = getExtensionsComboBox();

        if (null != model.getRevision() && !"".equals(model.getRevision())) {
            firstName.setValue(agent.getName());
            extensions.setValue(getCurrentExtension());
        }

        form.addField("firstName", firstName);
        form.addField("extensions", extensions);

        return form;
    }

    private CouchModelLite getCurrentExtension() {
        try {
            return modelLiteService.getCouchLiteModel((String) model.getOutputs().get("extension"));
        } catch (IOException e) {
            return new CouchModelLite();
        }
    }

    private ComboBox getExtensionsComboBox() {
        List<CouchModelLite> extensionModelList = getExtensions();
        ComboBox extensions = new ComboBox("Current extension");
        extensions.addItem("Select . . .");

        for (CouchModelLite couchExtensionModel : extensionModelList) {
            extensions.addItem(couchExtensionModel);
        }

        extensions.setNullSelectionItemId("Select . . .");
        extensions.setRequired(true);
        extensions.setRequiredError("Empty field \"Current extension\"");


        return extensions;
    }

    private List<CouchModelLite> getExtensions() {
        try {
            return modelLiteService.getAllCouchModelsLite(PortalUtil.getUserId(request), PortalUtil.getScopeGroupId(request), CouchTypes.extension);
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

    private CouchModel createModel(String id) {
        try {
            return service.getModel(id);
        } catch (Exception e) {
            return CouchModelUtil.newCouchModel(request, CouchTypes.agent);
        }
    }

}
