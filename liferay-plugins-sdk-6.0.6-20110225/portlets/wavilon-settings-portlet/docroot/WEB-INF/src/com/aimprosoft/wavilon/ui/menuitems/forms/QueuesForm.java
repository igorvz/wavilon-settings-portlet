package com.aimprosoft.wavilon.ui.menuitems.forms;

import com.aimprosoft.wavilon.application.GenericPortletApplication;
import com.aimprosoft.wavilon.couch.CouchModel;
import com.aimprosoft.wavilon.couch.CouchModelLite;
import com.aimprosoft.wavilon.couch.CouchTypes;
import com.aimprosoft.wavilon.model.Queue;
import com.aimprosoft.wavilon.service.QueueDatabaseService;
import com.aimprosoft.wavilon.spring.ObjectFactory;
import com.aimprosoft.wavilon.util.CouchModelUtil;
import com.liferay.portal.util.PortalUtil;
import com.vaadin.Application;
import com.vaadin.data.validator.IntegerValidator;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.*;
import org.apache.commons.lang.math.NumberUtils;

import javax.portlet.PortletRequest;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class QueuesForm extends Window {

    private QueueDatabaseService service = ObjectFactory.getBean(QueueDatabaseService.class);
    private ResourceBundle bundle;
    private PortletRequest request;
    private Table table;
    private CouchModel model;
    private Queue queue;


    public QueuesForm(ResourceBundle bundle, Table table) {
        this.bundle = bundle;
        this.table = table;
    }

    public void init(String id, final Object itemId) {
        final Application application = getApplication();
        request = ((GenericPortletApplication) application).getPortletRequest();
        model = createModel(id);
        queue = createQueue(model);

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
                    String forwardToOnMaxTimeInput = ((CouchModelLite) form.getField("forwardToOnMaxTimeInput").getValue()).getId();
                    String forwardToOnMaxLengthInput = ((CouchModelLite) form.getField("forwardToOnMaxLengthInput").getValue()).getId();
                    String musicOnHold = (String) form.getField("musicOnHold").getValue();


                    if (null != model.getRevision()) {
                        table.removeItem(itemId);
                        table.select(null);
                    }

                    queue.setName(name);
                    queue.setMaxTime(NumberUtils.toInt(maxTimeInput));
                    queue.setMaxLength(NumberUtils.toInt(maxLengthInput));
                    queue.setForwardToOnMaxTime(forwardToOnMaxTimeInput);
                    queue.setForwardToOnMaxLength(forwardToOnMaxLengthInput);
                    queue.setMusicOnHold(musicOnHold);

                    service.addQueue(queue, model, Collections.<String>emptyList());

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


                    table.getContainerProperty(object, "NAME").setValue(queue.getName());
                    table.getContainerProperty(object, "FORWARD TO ON MAX. TIME").setValue(CouchModelUtil.getCouchModelLite(queue.getForwardToOnMaxTime()));
                    table.getContainerProperty(object, "FORWARD TO ON MAX. LENGTH").setValue(CouchModelUtil.getCouchModelLite(queue.getForwardToOnMaxLength()));
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

    private Queue createQueue(CouchModel model) {
        if (null == model.getRevision()) {
            return newQueue();
        }
        try {
            return service.getQueue(model);
        } catch (Exception e) {
            return newQueue();
        }

    }

    private Queue newQueue() {
        Queue newQueue = new Queue();

        newQueue.setName("");
        newQueue.setForwardToOnMaxLength("");
        newQueue.setForwardToOnMaxTime("");

        return newQueue;
    }

    private Form createForm() {
        Form form = new QueuesFormLayout();
        form.addStyleName("labelField");

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


        List<CouchModelLite> forwards = getForwards();


        //fourth
        ComboBox forwardToOnMaxTimeInput = new ComboBox("Forward To On Max Time");
        fillForward(forwards, forwardToOnMaxTimeInput);
        forwardToOnMaxTimeInput.setRequiredError("Empty field \"Extension on Max. time\"");

        //fifth
        ComboBox forwardToOnMaxLengthInput = new ComboBox("Forward To On Max Length");
        fillForward(forwards, forwardToOnMaxLengthInput);
        forwardToOnMaxLengthInput.setRequiredError("Empty field \"Extension on Max. length\"");

        //sixth
        ComboBox musicOnHold = new ComboBox();
        musicOnHold.setWidth(230, Sizeable.UNITS_PIXELS);
        musicOnHold.setRequired(true);
        musicOnHold.setRequiredError("Empty field \"Music on hold\"");
        musicOnHold.setNullSelectionItemId("Select . . .");


        List<String> musicOnHoldList = new LinkedList<String>();
        musicOnHoldList.add("Music 1");
        musicOnHoldList.add("Music 2");
        musicOnHoldList.add("Music 3");
        musicOnHoldList.add(0, "Select . . .");

        for (String s : musicOnHoldList) {
            musicOnHold.addItem(s);

            if (null != model.getRevision()) {
                if (null != queue.getMusicOnHold() && queue.getMusicOnHold().equals(s)) {
                    musicOnHold.setValue(s);
                }
            }
        }


        if (null != model.getRevision()) {
            name.setValue(queue.getName());
            maxTimeInput.setValue(queue.getMaxTime());
            maxLengthInput.setValue(queue.getMaxLength());
            musicOnHold.setValue(queue.getMusicOnHold());
        }

        form.addField("name", name);
        form.addField("maxTimeInput", maxTimeInput);
        form.addField("maxLengthInput", maxLengthInput);
        form.addField("forwardToOnMaxTimeInput", forwardToOnMaxTimeInput);
        form.addField("forwardToOnMaxLengthInput", forwardToOnMaxLengthInput);
        form.addField("musicOnHold", musicOnHold);

        return form;
    }

    private void fillForward(List<CouchModelLite> forwards, ComboBox forwardTo) {
        forwardTo.addItem("Select . . .");
        for (CouchModelLite forward : forwards) {
            forwardTo.addItem(forward);
        }
        forwardTo.setWidth(230, Sizeable.UNITS_PIXELS);
        forwardTo.setRequired(true);
        forwardTo.setNullSelectionItemId("Select . . .");
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

    private List<CouchModelLite> getForwards() {
        try {
            return CouchModelUtil.getForwards(PortalUtil.getUserId(request), PortalUtil.getScopeGroupId(request));
        } catch (Exception ignored) {
            return Collections.emptyList();
        }
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
            } else if (propertyId.equals("forwardToOnMaxTimeInput")) {
                layout.addComponent(field, 1, 3, 2, 3);
            } else if (propertyId.equals("forwardToOnMaxLengthInput")) {
                layout.addComponent(field, 1, 4, 2, 4);
            } else if (propertyId.equals("musicOnHold")) {
                layout.addComponent(field, 1, 5, 2, 5);
            }
        }


    }

    private CouchModel createModel(String id) {
        try {
            return service.getModel(id);
        } catch (Exception e) {
            return CouchModelUtil.newCouchModel(request, CouchTypes.queue);
        }
    }

}
