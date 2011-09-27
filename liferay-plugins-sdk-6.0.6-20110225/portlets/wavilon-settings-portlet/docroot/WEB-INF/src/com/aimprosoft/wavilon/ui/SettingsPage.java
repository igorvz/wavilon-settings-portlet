package com.aimprosoft.wavilon.ui;

import com.aimprosoft.wavilon.ui.menuitems.*;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.*;

import java.util.Iterator;
import java.util.ResourceBundle;

public class SettingsPage extends VerticalLayout {
    private ResourceBundle bundle;
    private VerticalLayout leftColumn;
    private VerticalLayout rightColumn;
    private VerticalLayout detailsContent;
    private Long userId;

    public SettingsPage(final ResourceBundle bundle, Long userId) {
        this.bundle = bundle;
        this.userId = userId;

        addStyleName("settingsPanel");

        GridLayout panel = new GridLayout(2, 1);
//        HorizontalSplitPanel panel = new HorizontalSplitPanel();
//        panel.setSplitPosition(150, Sizeable.UNITS_PIXELS);
//        panel.setLocked(false);
        panel.setSizeFull();
        panel.setWidth("100%");
        addComponent(panel);

        leftColumn = new VerticalLayout();
        leftColumn.setStyleName("leftcolumn");
        leftColumn.setWidth(150, Sizeable.UNITS_PIXELS);
        panel.addComponent(leftColumn);

        rightColumn = new VerticalLayout();
        rightColumn.setStyleName("rightcolumn");
        panel.addComponent(rightColumn);

        VerticalLayout detailsBox = new VerticalLayout();
        detailsBox.setStyleName("detailsBox");
        setSides(detailsBox);

        detailsContent = new VerticalLayout();
        detailsContent.setStyleName("detailsContent");
        detailsBox.addComponent(detailsContent);

        rightColumn.addComponent(detailsBox);

        panel.addStyleName("gridLayout");
        panel.setColumnExpandRatio(0, 0);
        panel.setColumnExpandRatio(1, 1.0f);
        addButtons();

        setExpandRatio(panel, 0);
    }

    private void addButtons() {

        Button phoneNumbers = new NativeButton(bundle.getString("wavilon.settings.services.phoneNumbers"));
        phoneNumbers.addStyleName("label");
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

        Button virtualNumbers= new NativeButton(bundle.getString("wavilon.settings.services.virtualNumbers"));
        virtualNumbers.addStyleName("label");
        virtualNumbers.addListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                Button button = event.getButton();

                assignActiveButton(button);
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
