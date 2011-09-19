package com.aimprosoft.wavilon.ui.menuitems.forms;


import com.aimprosoft.wavilon.model.Agent;
import com.aimprosoft.wavilon.service.AgentDatabaseService;
import com.aimprosoft.wavilon.spring.ObjectFactory;
import com.vaadin.data.Item;
import com.vaadin.ui.*;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class AgentsForm extends VerticalLayout {
    private ResourceBundle bundle;
    private Item item;
    private static AgentDatabaseService service = ObjectFactory.getBean(AgentDatabaseService.class);
    private List<String> extensions = new LinkedList<String>();
    Agent agent = null;
    public AgentsForm(final ResourceBundle bundle, Item item) {
        this.bundle = bundle;
        this.item = item;

        String id = (String) item.getItemProperty("id").getValue();



        try {
            agent = service.getAgent(id);
        } catch (IOException ignored) {
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
            if (extension.equals("Ojgice 101")) {
                contentExtension.setEnabled(true);
            }
        }
        form.addField("contentExtension", contentExtension);

        Button apply = new Button(bundle.getString("wavilon.settings.validation.form.button.save"), new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                try {
                    form.commit();

                    String firstName= (String) form.getField("firstName").getValue();
                    agent.setFirstName(firstName);
                    service.addAgent(agent);
                     getWindow().showNotification("Well done");
                } catch (Exception ignored) {}
            }
        });
        addComponent(form);
        addComponent(apply);

    }


}
