package com.aimprosoft.wavilon.ui.menuitems.forms;

import com.aimprosoft.wavilon.couch.CouchModel;
import com.aimprosoft.wavilon.couch.CouchModelLite;
import com.aimprosoft.wavilon.couch.CouchTypes;
import com.aimprosoft.wavilon.model.Agent;
import com.aimprosoft.wavilon.service.AgentDatabaseService;
import com.aimprosoft.wavilon.service.CouchModelLiteDatabaseService;
import com.aimprosoft.wavilon.spring.ObjectFactory;
import com.aimprosoft.wavilon.util.CouchModelUtil;
import com.aimprosoft.wavilon.util.LayoutUtil;
import com.vaadin.ui.*;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

public class AgentsForm extends GeneralForm {
    private AgentDatabaseService service = ObjectFactory.getBean(AgentDatabaseService.class);
    private CouchModelLiteDatabaseService modelLiteService = ObjectFactory.getBean(CouchModelLiteDatabaseService.class);
    private Agent agent;

    public AgentsForm(ResourceBundle bundle, Table table) {
        super(bundle, table);
    }

    @Override
    public void init(String id, final Object itemId) {
        super.init(id, itemId);
        model = createCoucModel(id, service, CouchTypes.agent);
        agent = createAgent(model);

        if ("-1".equals(id)) {
            setCaption(bundle.getString("wavilon.form.agents.new.agent"));
        } else {
            setCaption(bundle.getString("wavilon.form.agents.edit.agent"));
        }
        final Form form = createForm();

        initForm(form, new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                try {
                    form.commit();

                    String name = (String) form.getField("firstName").getValue();
                    String extension = null;

                    if (null != form.getField("extensions").getValue()) {
                        extension = ((CouchModelLite) form.getField("extensions").getValue()).getId();
                    }

                    agent.setName(name);
                    agent.setAttachedLiferayUserId(model.getLiferayOrganizationId());

                    service.addAgent(agent, model, extension);

                    if (null != model.getRevision()) {
                        table.removeItem(itemId);
                        table.select(null);
                    }
                    final Object object = table.addItem();

                    table.getContainerProperty(object, bundle.getString("wavilon.table.agents.column.name")).setValue(agent.getName());
                    table.getContainerProperty(object, bundle.getString("wavilon.table.agents.column.current.extension")).setValue(form.getField("extensions").getValue());
                    table.getContainerProperty(object, "id").setValue(model.getId());
                    HorizontalLayout buttons = LayoutUtil.createTablesEditRemoveButtons(table, object, model, bundle, null, application.getMainWindow());
                    table.getContainerProperty(object, "").setValue(buttons);

                    LayoutUtil.setTableBackground(table, CouchTypes.agent);

                    getParent().getWindow().showNotification(bundle.getString("wavilon.well.done"));
                    close();
                } catch (Exception ignored) {
                }
            }
        });
    }

    private Agent createAgent(CouchModel model) {
        if (null == model.getRevision()) {
            return newAgent();
        }
        try {
            return getModel(model, service, Agent.class);
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
            if (null != model.getOutputs().get("extension")) {
                return modelLiteService.getCouchLiteModel((String) model.getOutputs().get("extension"));
            } else {
                return null;
            }
        } catch (IOException e) {
            return null;
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
        return extensions;
    }

    private List<CouchModelLite> getExtensions() {
        try {
            return modelLiteService.getAllCouchModelsLite(CouchModelUtil.getOrganizationId(request), CouchTypes.extension);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

}
