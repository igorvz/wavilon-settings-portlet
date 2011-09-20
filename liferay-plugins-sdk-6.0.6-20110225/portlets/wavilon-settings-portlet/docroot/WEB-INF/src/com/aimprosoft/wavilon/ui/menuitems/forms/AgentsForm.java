package com.aimprosoft.wavilon.ui.menuitems.forms;


import com.aimprosoft.wavilon.application.GenericPortletApplication;
import com.aimprosoft.wavilon.model.Agent;
import com.aimprosoft.wavilon.service.AgentDatabaseService;
import com.aimprosoft.wavilon.spring.ObjectFactory;
import com.liferay.portal.util.PortalUtil;
import com.vaadin.data.Item;
import com.vaadin.ui.*;
import org.apache.commons.collections.PredicateUtils;

import javax.portlet.PortletRequest;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;

public class AgentsForm extends VerticalLayout {
    private ResourceBundle bundle;
    private Item item;
    private static AgentDatabaseService service = ObjectFactory.getBean(AgentDatabaseService.class);
    private List<String> extensions = new LinkedList<String>();
    Agent agent = null;

    public AgentsForm(final ResourceBundle bundle, Item item, final VerticalLayout right, Table table) {
        this.bundle = bundle;
        this.item = item;


        if (item != null) {
            String id = (String) item.getItemProperty("id").getValue();
            try {
                agent = service.getAgent(id);
            } catch (IOException ignored) {
            }
        } else {

            agent = new Agent();
            agent.setFirstName("");
        }

        final Form form = new Form();

        TextField firstName = new TextField("First name");
        firstName.setValue(agent.getFirstName());
        firstName.setRequired(true);
        firstName.setRequiredError(bundle.getString("wavilon.settings.validation.form.error.firstName"));
        form.addField("firstName", firstName);

        extensions.add("Ojgice 101");
        extensions.add("Nane 3527");
        ComboBox contentExtension = new ComboBox();
        for (String extension : extensions) {
            contentExtension.addItem(extension);
        }
        form.addField("contentExtension", contentExtension);

        Button apply = new Button(bundle.getString("wavilon.settings.validation.form.button.save"), new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                try {
                    form.commit();

                    if (agent.getId() == null){
                        PortletRequest request = ((GenericPortletApplication)getApplication()).getPortletRequest();
                        agent.setLiferayUserId(PortalUtil.getUserId(request));
                        agent.setLiferayOrganizationId(PortalUtil.getScopeGroupId(request));
                        agent.setLiferayPortalId(PortalUtil.getCompany(request).getWebId());
                    }

                    String firstName = (String) form.getField("firstName").getValue();
                    agent.setId(UUID.randomUUID().toString());
                    agent.setFirstName(firstName);
                    service.addAgent(agent);
                    getWindow().showNotification("Well done");
                    right.removeAllComponents();

                } catch (Exception ignored) {
                }
            }
        });
        addComponent(form);
        addComponent(apply);

    }


}
