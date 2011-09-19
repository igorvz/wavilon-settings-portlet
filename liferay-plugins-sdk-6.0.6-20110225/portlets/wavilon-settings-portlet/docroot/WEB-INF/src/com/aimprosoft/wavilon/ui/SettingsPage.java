package com.aimprosoft.wavilon.ui;

import com.aimprosoft.wavilon.ui.menuitems.AgentsContent;
import com.aimprosoft.wavilon.ui.menuitems.settings.ExtensionsForm;
import com.aimprosoft.wavilon.ui.menuitems.settings.PhoneNumbersContent;
import com.aimprosoft.wavilon.ui.menuitems.settings.RecordingsForm;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.*;

import java.util.Iterator;
import java.util.ResourceBundle;

public class SettingsPage extends VerticalLayout {
    private ResourceBundle bundle;
    private VerticalLayout leftColumn;
    private VerticalLayout rightColumn;
    private VerticalLayout detailsContent;

    public SettingsPage(final ResourceBundle bundle) {
        this.bundle = bundle;

        addStyleName("settings");
        setSides(this);

        HorizontalSplitPanel panel = new HorizontalSplitPanel();
//        setSides(panel);
        panel.setSplitPosition(150, Sizeable.UNITS_PIXELS);
        panel.setLocked(false);
        addComponent(panel);

        leftColumn = new VerticalLayout();
        leftColumn.setStyleName("leftcolumn");
//        setSides(leftColumn);
        panel.addComponent(leftColumn);

        rightColumn = new VerticalLayout();
        rightColumn.setStyleName("rightcolumn");
        rightColumn.setHeight("100%");
        panel.addComponent(rightColumn);

        VerticalLayout detailsBox = new VerticalLayout();
        setSides(detailsBox);

        detailsContent = new VerticalLayout();
//        setSides(detailsContent);
        detailsBox.addComponent(detailsContent);

        rightColumn.addComponent(detailsBox);

        addButtons();

        setExpandRatio(panel, 0);
    }

    private void addButtons() {
        Label services = new Label(bundle.getString("wavilon.settings.services"));
        Button phoneNumbersServices = new NativeButton(bundle.getString("wavilon.settings.services.phoneNumbers"));
        phoneNumbersServices.addListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
               Button button = event.getButton();

                assignActiveButton(button);
                detailsContent.removeAllComponents();
                detailsContent.addComponent(new PhoneNumbersContent(bundle));
            }
        });

        Button virtualNumbersServices = new NativeButton(bundle.getString("wavilon.settings.services.virtualNumbers"));
        virtualNumbersServices.addListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                Button button = event.getButton();

                assignActiveButton(button);

            }
        });

        Button gtalkServices = new NativeButton(bundle.getString("wavilon.settings.services.gtalk"));
        gtalkServices.addListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                Button button = event.getButton();

                assignActiveButton(button);

            }
        });

        Button queues = new NativeButton(bundle.getString("wavilon.settings.queues"));
        queues.addListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                Button button = event.getButton();

                assignActiveButton(button);

            }
        });

        Button agents = new NativeButton(bundle.getString("wavilon.settings.agents"));
        agents.addListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
               Button button = event.getButton();

                assignActiveButton(button);

                detailsContent.removeAllComponents();
                detailsContent.addComponent(new AgentsContent(bundle));
            }
        });

        Label extension = new Label(bundle.getString("wavilon.settings.extensions"));
        Button sipExtension = new NativeButton(bundle.getString("wavilon.settings.extensions.sip"));
        sipExtension.addListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
               Button button = event.getButton();

                assignActiveButton(button);

                detailsContent.removeAllComponents();
                detailsContent.addComponent(new ExtensionsForm(bundle));
            }
        });

        Button gtalkExtension = new NativeButton(bundle.getString("wavilon.settings.extensions.gtalk"));
        gtalkExtension.addListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
               Button button = event.getButton();

                assignActiveButton(button);

                detailsContent.removeAllComponents();
                detailsContent.addComponent(new ExtensionsForm(bundle));
            }
        });

        Button phoneNumbersExtension = new NativeButton(bundle.getString("wavilon.settings.extensions.phoneNumbers"));
        phoneNumbersExtension.addListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
               Button button = event.getButton();

                assignActiveButton(button);

                detailsContent.removeAllComponents();
                detailsContent.addComponent(new ExtensionsForm(bundle));
            }
        });

        Button recordings = new NativeButton(bundle.getString("wavilon.settings.recordings"));
        recordings.addListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                Button button = event.getButton();

                assignActiveButton(button);

                detailsContent.removeAllComponents();
                detailsContent.addComponent(new RecordingsForm(bundle));
            }
        });

        leftColumn.addComponent(services);
        leftColumn.addComponent(phoneNumbersServices);
        leftColumn.addComponent(virtualNumbersServices);
        leftColumn.addComponent(gtalkServices);
        leftColumn.addComponent(queues);
        leftColumn.addComponent(agents);
        leftColumn.addComponent(extension);
        leftColumn.addComponent(sipExtension);
        leftColumn.addComponent(gtalkExtension);
        leftColumn.addComponent(phoneNumbersExtension);
        leftColumn.addComponent(recordings);
    }

    private void assignActiveButton(Button button) {
        removeButtonSelection();
        button.addStyleName("active");
    }

    //remove selection for all buttons
    private void removeButtonSelection() {
        Iterator<Component> componentIterator = leftColumn.getComponentIterator();
        while (componentIterator.hasNext()){
            Component component = componentIterator.next();
            //make sure component is button
            if (component instanceof Button){
                component.removeStyleName("active");
            }
        }
    }

    private void setSides(Component component) {
        component.setWidth(100, Sizeable.UNITS_PERCENTAGE);
        component.setHeight(100, Sizeable.UNITS_PERCENTAGE);
    }
}
