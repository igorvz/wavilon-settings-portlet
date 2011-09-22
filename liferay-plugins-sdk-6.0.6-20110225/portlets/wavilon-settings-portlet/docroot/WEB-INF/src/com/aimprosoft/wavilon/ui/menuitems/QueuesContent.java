package com.aimprosoft.wavilon.ui.menuitems;

import com.aimprosoft.wavilon.model.Queue;
import com.aimprosoft.wavilon.service.QueueDatabaseService;
import com.aimprosoft.wavilon.spring.ObjectFactory;
import com.aimprosoft.wavilon.ui.menuitems.forms.QueuesForm;
import com.vaadin.data.Property;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.*;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

public class QueuesContent extends VerticalLayout {
    private ResourceBundle bundle;
    private Queue selectedQueue;
    private List<Queue> queues;
    private QueueDatabaseService service = ObjectFactory.getBean(QueueDatabaseService.class);
    private HorizontalLayout queuesPanel = new HorizontalLayout();
    private VerticalLayout queuesFormLayout = new VerticalLayout();

    public QueuesContent(ResourceBundle bundle) {
        this.bundle = bundle;
        queues = getQueues();

        initLayout();
        initQueuesPanel();

        setExpandRatio(queuesPanel, 1);
        setExpandRatio(queuesFormLayout, 40);
    }

    private void initQueuesPanel() {
        final ComboBox queuesComboBox = new ComboBox("Select queue");
        queuesComboBox.setNullSelectionAllowed(false);
        for (Queue queue : queues) {
            queuesComboBox.addItem(queue);
        }

        //todo fire value change listener
        queuesComboBox.addListener(new ComboBox.ValueChangeListener() {
            public void valueChange(Property.ValueChangeEvent event) {
                Object id = event.getProperty().getValue();

                selectedQueue = (Queue) id;

                viewQueueForm(selectedQueue.getId());

            }
        });

        queuesPanel.addComponent(queuesComboBox);

        Button addQueueButton = new NativeButton("add", new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                viewQueueForm(null);
            }
        });
        queuesPanel.addComponent(addQueueButton);
    }

    private void initLayout() {
        setHeight(400, Sizeable.UNITS_PIXELS);
        setWidth(750, Sizeable.UNITS_PIXELS);

        queuesPanel.setSizeUndefined();
        addComponent(queuesPanel);
        setComponentAlignment(queuesPanel, Alignment.TOP_CENTER);

        queuesFormLayout.setSizeUndefined();
        addComponent(queuesFormLayout);
        setComponentAlignment(queuesFormLayout, Alignment.TOP_CENTER);
    }

    private List<Queue> getQueues() {
        try {
            return service.getAllQueues();
        } catch (IOException e) {
            return Collections.emptyList();
        }
    }

    private void viewQueueForm(String queueId) {
        queuesFormLayout.removeAllComponents();
        queuesFormLayout.addComponent(new QueuesForm(bundle, queueId, queuesFormLayout));
    }
}
