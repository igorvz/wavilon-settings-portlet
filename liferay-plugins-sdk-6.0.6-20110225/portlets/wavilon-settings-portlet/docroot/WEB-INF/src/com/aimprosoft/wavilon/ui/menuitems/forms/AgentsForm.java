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
import com.vaadin.ui.*;

import javax.portlet.PortletRequest;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

public class AgentsForm extends AbstractForm {
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
        removeAllComponents();

        application = getApplication();
        request = ((GenericPortletApplication) application).getPortletRequest();
        model = createModel(id);
        agent = createAgent(model);

        if ("-1".equals(id)) {
            setCaption(bundle.getString("wavilon.form.agents.new.agent"));
        } else {
            setCaption(bundle.getString("wavilon.form.agents.edit.agent"));
        }

        VerticalLayout content = new VerticalLayout();
        content.addStyleName("formRegion");

        addComponent(content);

        final Form form = createForm();
        content.addComponent(form);

        HorizontalLayout buttons = createButtons(content);

        Button cancel = new Button(bundle.getString("wavilon.button.cancel"), new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                close();
            }
        });
        buttons.addComponent(cancel);

        Button save = new Button(bundle.getString("wavilon.button.save"), new Button.ClickListener() {
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
                        }
                    };

                    table.getContainerProperty(object, bundle.getString("wavilon.table.agents.column.name")).setValue(agent.getName());
                    table.getContainerProperty(object, bundle.getString("wavilon.table.agents.column.current.extension")).setValue(form.getField("extensions").getValue());
                    table.getContainerProperty(object, "id").setValue(model.getId());
                    table.getContainerProperty(object, "").setValue(new Button("", listener));

                    getWindow().showNotification(bundle.getString("wavilon.well.done"));
                    close();
                } catch (Exception ignored) {
                }
            }
        });
        save.addStyleName("saveButton");
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

        TextField firstName = new TextField(bundle.getString("wavilon.form.agents.first.name"));
        firstName.setRequired(true);
        firstName.setRequiredError(bundle.getString("wavilon.error.massage.agents.firstname.empty"));

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
        ComboBox extensions = new ComboBox(bundle.getString("wavilon.form.agents.current.extension"));
        extensions.addItem(bundle.getString("wavilon.form.select"));

        for (CouchModelLite couchExtensionModel : extensionModelList) {
            extensions.addItem(couchExtensionModel);
        }

        extensions.setNullSelectionItemId(bundle.getString("wavilon.form.select"));
        extensions.setRequired(true);
        extensions.setRequiredError(bundle.getString("wavilon.error.massage.agents.extension.empty"));

        return extensions;
    }

    private List<CouchModelLite> getExtensions() {
        try {
            return modelLiteService.getAllCouchModelsLite(PortalUtil.getScopeGroupId(request), CouchTypes.extension);
        } catch (Exception e) {
            return Collections.emptyList();
        }
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
