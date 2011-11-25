package com.aimprosoft.wavilon.ui;

import com.aimprosoft.wavilon.ui.menuitems.*;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.Reindeer;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class SettingsPage extends VerticalLayout {
    private ResourceBundle bundle;
    private VerticalLayout leftColumn;
    private VerticalLayout detailsContent;
    private List<String> styles = new LinkedList<String>();
    private Button phoneNumbers;

    {
        styles.add("phoneNumbers");
        styles.add("virtualNumbers");
        styles.add("queues");
        styles.add("agents");
        styles.add("extensions");
        styles.add("recordings");
        styles.add("contacts");
    }

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
        detailsBox.setSizeFull();
        setSides(detailsBox);

        detailsContent = new VerticalLayout();
        detailsContent.setSizeFull();
        detailsBox.addComponent(detailsContent);

        rightColumn.addComponent(detailsBox);

        addButtons();

        detailsContent.removeAllComponents();
        PhoneNumbersContent phoneNumbersContent = new PhoneNumbersContent(bundle);
        detailsContent.addComponent(phoneNumbersContent);
        phoneNumbersContent.init();
        assignActiveButton(phoneNumbers);

    }

    private void addButtons() {

        phoneNumbers = new NativeButton(bundle.getString("wavilon.menuitem.phonenumbers"));
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

        Button virtualNumbers = new NativeButton(bundle.getString("wavilon.menuitem.virtualnumbers"));
        virtualNumbers.addStyleName("virtualNumbersButton");
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

        Button queues = new NativeButton(bundle.getString("wavilon.menuitem.queues"));
        queues.addStyleName("queuesButton");
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

        Button agents = new NativeButton(bundle.getString("wavilon.menuitem.agents"));
        agents.addStyleName("agentsButton");
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

        Button extensions = new NativeButton(bundle.getString("wavilon.menuitem.extensions"));
        extensions.addStyleName("extensionsButton");
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

        Button recordings = new NativeButton(bundle.getString("wavilon.menuitem.recordings"));
        recordings.addStyleName("recordingsButton");
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

        Button contacts = new NativeButton(bundle.getString("wavilon.menuitem.contacts"));
        contacts.addStyleName("contactsButton");
        contacts.addListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                Button button = event.getButton();
                ContactsContent contactsContent = new ContactsContent(bundle);

                assignActiveButton(button);

                detailsContent.removeAllComponents();
                detailsContent.addComponent(contactsContent);
                contactsContent.init();
            }
        });

        leftColumn.addComponent(phoneNumbers);
        leftColumn.addComponent(virtualNumbers);
        leftColumn.addComponent(queues);
        leftColumn.addComponent(agents);
        leftColumn.addComponent(extensions);
        leftColumn.addComponent(recordings);
        leftColumn.addComponent(contacts);
    }

    private void assignActiveButton(Button button) {
        removeButtonSelection();
        if ((bundle.getString("wavilon.menuitem.phonenumbers")).equals(button.getCaption())) {
            button.addStyleName("phoneNumbersButtonSelect");
        } else if ((bundle.getString("wavilon.menuitem.virtualnumbers")).equals(button.getCaption())) {
            button.addStyleName("virtualNumbersButtonSelect");
        } else if ((bundle.getString("wavilon.menuitem.queues")).equals(button.getCaption())) {
            button.addStyleName("queuesButtonSelect");
        } else if ((bundle.getString("wavilon.menuitem.agents")).equals(button.getCaption())) {
            button.addStyleName("agentsButtonSelect");
        } else if ((bundle.getString("wavilon.menuitem.extensions")).equals(button.getCaption())) {
            button.addStyleName("extensionsButtonSelect");
        } else if ((bundle.getString("wavilon.menuitem.recordings")).equals(button.getCaption())){
            button.addStyleName("recordingsButtonSelect");
        }else {
            button.addStyleName("contactsButtonSelect");
        }
    }

    //remove selection for all buttons
    private void removeButtonSelection() {
        Iterator<Component> componentIterator = leftColumn.getComponentIterator();
        while (componentIterator.hasNext()) {
            Component component = componentIterator.next();
            //make sure component is button
            if (component instanceof Button) {
                for (String style : styles) {
                    component.removeStyleName(style + "ButtonSelect");
                }
            }
        }
    }

    private void setSides(Component component) {
        component.setWidth(100, Sizeable.UNITS_PERCENTAGE);
        component.setHeight(100, Sizeable.UNITS_PERCENTAGE);
    }
}
