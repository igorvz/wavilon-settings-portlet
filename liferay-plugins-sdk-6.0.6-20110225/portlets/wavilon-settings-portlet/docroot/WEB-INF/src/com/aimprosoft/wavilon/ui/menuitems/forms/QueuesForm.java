package com.aimprosoft.wavilon.ui.menuitems.forms;

import com.aimprosoft.wavilon.application.GenericPortletApplication;
import com.aimprosoft.wavilon.model.Queue;
import com.aimprosoft.wavilon.service.QueueDatabaseService;
import com.aimprosoft.wavilon.spring.ObjectFactory;
import com.liferay.portal.util.PortalUtil;
import com.vaadin.data.validator.IntegerValidator;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.*;
import org.apache.commons.lang.math.NumberUtils;

import javax.portlet.PortletRequest;
import java.util.*;

public class QueuesForm extends Window {

    private QueueDatabaseService service = ObjectFactory.getBean(QueueDatabaseService.class);
    private ResourceBundle bundle;
    private PortletRequest request;
    private Table table;
    private Queue queue;


    public QueuesForm(ResourceBundle bundle, Table table) {
        this.bundle = bundle;
        this.table = table;
    }

    public void init(String id) {
        request = ((GenericPortletApplication) getApplication()).getPortletRequest();
        queue = createQueue(id);

        if ("-1".equals(id)) {
            setCaption("New Queue");
        } else {
            setCaption("Edit Queue");
        }

        VerticalLayout content = new VerticalLayout();
        content.addStyleName("formRegion");
        content.setSizeFull();
        addComponent(content);

        Label headerForm = createHeader(id, queue);
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

                    String name = (String) form.getField("name").getValue();
                    String maxTimeInput = form.getField("maxTimeInput").getValue().toString();
                    String maxLengthInput = form.getField("maxLengthInput").getValue().toString();
                    String extensionOnMaxTimeInput = (String) form.getField("extensionOnMaxTimeInput").getValue();
                    String extensionOnMaxLengthInput = (String) form.getField("extensionOnMaxLengthInput").getValue();
                    String musicOnHold = (String) form.getField("musicOnHold").getValue();


                    if (null != queue.getRevision()) {
                        table.removeItem(table.getValue());
                        table.select(null);
                    }

                    queue.setName(name);
                    queue.setMaxTime(NumberUtils.toInt(maxTimeInput));
                    queue.setMaxLength(NumberUtils.toInt(maxLengthInput));
                    queue.setExtensionOnMaxTime(extensionOnMaxTimeInput);
                    queue.setExtensionOnMaxLength(extensionOnMaxLengthInput);
                    queue.setMusicOnHold(musicOnHold);

                    service.addQueue(queue);
                    Object object = table.addItem();
                    table.getContainerProperty(object, "NAME").setValue(queue.getName());
                    table.getContainerProperty(object, "EXTENSION ON MAX. TIME").setValue(queue.getExtensionOnMaxTime());
                    table.getContainerProperty(object, "EXTENSION ON MAX. LENGTH").setValue(queue.getExtensionOnMaxLength());
                    table.getContainerProperty(object, "id").setValue(queue.getId());

                    getWindow().showNotification("Well done");
                    close();
                } catch (Exception ignored) {
                }
            }
        });
        buttons.addComponent(save);
    }

    private Queue createQueue(String id) {
        if ("-1".equals(id)) {
            return newQueue();
        }
        try {
            return service.getQueue(id);
        } catch (Exception e) {
            return newQueue();
        }

    }

    private Queue newQueue() {
        Queue newQueue = new Queue();

        try {
            newQueue.setId(UUID.randomUUID().toString());
            newQueue.setLiferayUserId(PortalUtil.getUserId(request));
            newQueue.setLiferayOrganizationId(PortalUtil.getScopeGroupId(request));
            newQueue.setLiferayPortalId(PortalUtil.getCompany(request).getWebId());
            newQueue.setAgents(Collections.<String>emptyList());
        } catch (Exception ignored) {
        }

        newQueue.setName("");
        return newQueue;
    }

    private Form createForm() {
        Form queuesForm = new QueuesFormLayout();

        //first row
        TextField name = new TextField();
        name.setWidth(230, Sizeable.UNITS_PIXELS);
        name.setRequired(true);
        name.setRequiredError(bundle.getString("wavilon.settings.validation.form.error.empty.queues.title"));

        //second row
        TextField maxTimeInput = new TextField();
        maxTimeInput.setWidth(150, Sizeable.UNITS_PIXELS);
        maxTimeInput.setRequired(true);
        maxTimeInput.setRequiredError(bundle.getString("wavilon.settings.validation.form.error.empty.queues.max.time.input"));
        maxTimeInput.addValidator(new IntegerValidator(bundle.getString("wavilon.settings.validation.form.error.queues.integer.max.time.input")));

        //third row
        TextField maxLengthInput = new TextField();
        maxLengthInput.setWidth(150, Sizeable.UNITS_PIXELS);
        maxLengthInput.setRequired(true);
        maxLengthInput.setRequiredError(bundle.getString("wavilon.settings.validation.form.error.empty.queues.max.length.input"));
        maxLengthInput.addValidator(new IntegerValidator(bundle.getString("wavilon.settings.validation.form.error.queues.integer.max.length.input")));

        //fourth
        ComboBox extensionOnMaxTimeInput = new ComboBox();
        extensionOnMaxTimeInput.setWidth(230, Sizeable.UNITS_PIXELS);
        extensionOnMaxTimeInput.setRequired(true);
        extensionOnMaxTimeInput.setRequiredError("Empty field \"Extension on Max. time\"");

        //fifth
        ComboBox extensionOnMaxLengthInput = new ComboBox();
        extensionOnMaxLengthInput.setWidth(230, Sizeable.UNITS_PIXELS);
        extensionOnMaxLengthInput.setRequired(true);
        extensionOnMaxLengthInput.setRequiredError("Empty field \"Extension on Max. length\"");
        //sixth
        ComboBox musicOnHold = new ComboBox();
        musicOnHold.setWidth(230, Sizeable.UNITS_PIXELS);
        musicOnHold.setRequired(true);
        musicOnHold.setRequiredError("Empty field \"Music on hold\"");


        List<String> extensionList = new LinkedList<String>();
        extensionList.add("Extension 1");
        extensionList.add("Extension 2");
        extensionList.add("Extension 3");
        extensionList.add(0, "Select . . .");

        List<String> musicOnHoldList = new LinkedList<String>();
        musicOnHoldList.add("Music 1");
        musicOnHoldList.add("Music 2");
        musicOnHoldList.add("Music 3");
        musicOnHoldList.add(0, "Select . . .");


        if (null != queue.getRevision()) {
            name.setValue(queue.getName());
            maxTimeInput.setValue(queue.getMaxTime());
            maxLengthInput.setValue(queue.getMaxLength());
        }
        for (String e : extensionList) {
            extensionOnMaxTimeInput.addItem(e);
            extensionOnMaxLengthInput.addItem(e);

            if (null != queue.getRevision()) {
                if (null != queue.getExtensionOnMaxTime() && queue.getExtensionOnMaxTime().equals(e)) {
                    extensionOnMaxTimeInput.setValue(e);
                }
                if (null != queue.getExtensionOnMaxLength() && queue.getExtensionOnMaxLength().equals(e)) {
                    extensionOnMaxLengthInput.setValue(e);
                }
            }

        }

        for (String s : musicOnHoldList) {
            musicOnHold.addItem(s);

            if (null != queue.getRevision()) {
                if (null != queue.getMusicOnHold() && queue.getMusicOnHold().equals(s)) {
                    musicOnHold.setValue(s);
                }
            }
        }


        queuesForm.addField("name", name);
        queuesForm.addField("maxTimeInput", maxTimeInput);
        queuesForm.addField("maxLengthInput", maxLengthInput);

        queuesForm.addField("extensionOnMaxTimeInput", extensionOnMaxTimeInput);
        extensionOnMaxTimeInput.setNullSelectionAllowed(false);
        extensionOnMaxTimeInput.setNullSelectionItemId("Select . . .");

        queuesForm.addField("extensionOnMaxLengthInput", extensionOnMaxLengthInput);
        extensionOnMaxLengthInput.setNullSelectionAllowed(false);
        extensionOnMaxLengthInput.setNullSelectionItemId("Select . . .");

        queuesForm.addField("musicOnHold", musicOnHold);
        musicOnHold.setNullSelectionAllowed(false);
        musicOnHold.setNullSelectionItemId("Select . . .");

        return queuesForm;
    }

    private Label createHeader(String id, Queue queue) {
        Label headerForm = new Label("-1".equals(id) ? "New Queue" : queue.getName());

        headerForm.setHeight(27, Sizeable.UNITS_PIXELS);
        headerForm.setWidth("100%");
        headerForm.addStyleName("headerForm");

        return headerForm;
    }

    private HorizontalLayout createButtons(VerticalLayout content) {
        HorizontalLayout buttons = new HorizontalLayout();
        content.addComponent(buttons);
        buttons.addStyleName("buttonsPanel");
        content.setComponentAlignment(buttons, Alignment.BOTTOM_RIGHT);

        return buttons;
    }

    private static class QueuesFormLayout extends Form {
        private GridLayout layout;

        QueuesFormLayout() {
            layout = new GridLayout(3, 6);
            layout.setMargin(true, false, false, true);
            layout.setSpacing(true);

            setLayout(layout);

            layout.addComponent(new Label("Name"), 0, 0);
            layout.addComponent(new Label("Max. time"), 0, 1);

            Label sec = new Label("seconds");
            sec.setWidth(60, Sizeable.UNITS_PIXELS);
            layout.addComponent(sec, 2, 1);
            layout.setComponentAlignment(sec, Alignment.MIDDLE_RIGHT);
            layout.addComponent(new Label("Max. length"), 0, 2);

            Label calls = new Label("calls");
            calls.setWidth(40, Sizeable.UNITS_PIXELS);
            layout.addComponent(calls, 2, 2);
            layout.setComponentAlignment(calls, Alignment.MIDDLE_RIGHT);

            layout.addComponent(new Label("Extension on Max. time"), 0, 3);
            layout.addComponent(new Label("Extension on Max. length"), 0, 4);
            layout.addComponent(new Label("Music on hold"), 0, 5);
        }


        @Override
        protected void attachField(Object propertyId, Field field) {
            if (propertyId.equals("name")) {
                layout.addComponent(field, 1, 0, 2, 0);
            } else if (propertyId.equals("maxTimeInput")) {
                layout.addComponent(field, 1, 1);
            } else if (propertyId.equals("maxLengthInput")) {
                layout.addComponent(field, 1, 2);
            } else if (propertyId.equals("extensionOnMaxTimeInput")) {
                layout.addComponent(field, 1, 3, 2, 3);
            } else if (propertyId.equals("extensionOnMaxLengthInput")) {
                layout.addComponent(field, 1, 4, 2, 4);
            } else if (propertyId.equals("musicOnHold")) {
                layout.addComponent(field, 1, 5, 2, 5);
            }
        }


    }
}
