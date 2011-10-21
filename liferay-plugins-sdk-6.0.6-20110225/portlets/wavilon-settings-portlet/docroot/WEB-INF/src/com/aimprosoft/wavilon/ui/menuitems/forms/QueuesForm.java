package com.aimprosoft.wavilon.ui.menuitems.forms;

import com.aimprosoft.wavilon.application.GenericPortletApplication;
import com.aimprosoft.wavilon.couch.CouchModel;
import com.aimprosoft.wavilon.couch.CouchModelLite;
import com.aimprosoft.wavilon.couch.CouchTypes;
import com.aimprosoft.wavilon.model.Queue;
import com.aimprosoft.wavilon.service.QueueDatabaseService;
import com.aimprosoft.wavilon.spring.ObjectFactory;
import com.aimprosoft.wavilon.util.CouchModelUtil;
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

public class QueuesForm extends AbstractForm {

    private QueueDatabaseService service = ObjectFactory.getBean(QueueDatabaseService.class);
    private ResourceBundle bundle;
    private PortletRequest request;
    private Table table;
    private CouchModel model;
    private Queue queue;

    public QueuesForm(final ResourceBundle bundle, Table table) {
        this.bundle = bundle;
        this.table = table;
    }

    public void init(String id, final Object itemId) {
        removeAllComponents();
        final Application application = getApplication();
        request = ((GenericPortletApplication) application).getPortletRequest();
        model = createModel(id);
        queue = createQueue(model);

        if ("-1".equals(id)) {
            setCaption(bundle.getString("wavilon.form.queues.new.queue"));
        } else {
            setCaption(bundle.getString("wavilon.form.queues.edit.queue"));
        }

        VerticalLayout content = new VerticalLayout();
        content.addStyleName("formRegion");
        content.setSizeFull();

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
                        }
                    };

                    table.getContainerProperty(object, bundle.getString("wavilon.table.queues.column.name")).setValue(queue.getName());
                    table.getContainerProperty(object, bundle.getString("wavilon.table.queues.column.forward.to.on.max.time")).setValue(CouchModelUtil.getCouchModelLite(queue.getForwardToOnMaxTime(), bundle));
                    table.getContainerProperty(object, bundle.getString("wavilon.table.queues.column.forward.to.on.max.length")).setValue(CouchModelUtil.getCouchModelLite(queue.getForwardToOnMaxLength(), bundle));
                    table.getContainerProperty(object, "id").setValue(model.getId());
                    table.getContainerProperty(object, "").setValue(new Button("", listener));

                    getParent().getWindow().showNotification(bundle.getString("wavilon.well.done"));
                    close();
                } catch (Exception ignored) {
                }
            }
        });
        save.addStyleName("saveButton");
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
        Form form = new QueuesFormLayout(bundle);
        form.addStyleName("labelField");

        //first row
        TextField name = new TextField();
        name.setWidth(230, Sizeable.UNITS_PIXELS);
        name.setRequired(true);
        name.setRequiredError(bundle.getString("wavilon.error.massage.queues.name.empty"));

        //second row
        TextField maxTimeInput = new TextField();
        maxTimeInput.setWidth(150, Sizeable.UNITS_PIXELS);
        maxTimeInput.setRequired(true);
        maxTimeInput.setRequiredError(bundle.getString("wavilon.error.massage.queues.max.time.empty"));
        maxTimeInput.addValidator(new IntegerValidator(bundle.getString("wavilon.error.massage.queues.max.time.integer")));

        //third row
        TextField maxLengthInput = new TextField();
        maxLengthInput.setWidth(150, Sizeable.UNITS_PIXELS);
        maxLengthInput.setRequired(true);
        maxLengthInput.setRequiredError(bundle.getString("wavilon.error.massage.queues.extension.max.length.empty"));
        maxLengthInput.addValidator(new IntegerValidator(bundle.getString("wavilon.error.massage.queues.max.length.integer")));

        List<CouchModelLite> forwards = getForwards();

        //fourth
        ComboBox forwardToOnMaxTimeInput = new ComboBox();
        fillForward(forwards, forwardToOnMaxTimeInput);
        forwardToOnMaxTimeInput.setRequiredError(bundle.getString("wavilon.error.massage.queues.max.time.empty"));

        //fifth
        ComboBox forwardToOnMaxLengthInput = new ComboBox();
        fillForward(forwards, forwardToOnMaxLengthInput);
        forwardToOnMaxLengthInput.setRequiredError(bundle.getString("wavilon.error.massage.queues.max.length.empty"));

        //sixth
        ComboBox musicOnHold = new ComboBox();
        musicOnHold.setWidth(230, Sizeable.UNITS_PIXELS);
        musicOnHold.setRequired(true);
        musicOnHold.setRequiredError(bundle.getString("wavilon.error.massage.queues.music.empty"));
        musicOnHold.setNullSelectionItemId(bundle.getString("wavilon.form.select"));


        List<String> musicOnHoldList = new LinkedList<String>();
        musicOnHoldList.add("Music 1");
        musicOnHoldList.add("Music 2");
        musicOnHoldList.add("Music 3");
        musicOnHoldList.add(0, bundle.getString("wavilon.form.select"));

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
        forwardTo.addItem(bundle.getString("wavilon.form.select"));
        for (CouchModelLite forward : forwards) {
            forwardTo.addItem(forward);
        }
        forwardTo.setWidth(230, Sizeable.UNITS_PIXELS);
        forwardTo.setRequired(true);
        forwardTo.setNullSelectionItemId(bundle.getString("wavilon.form.select"));
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
            return CouchModelUtil.getForwards(CouchModelUtil.getOrganizationId(request));
        } catch (Exception ignored) {
            return Collections.emptyList();
        }
    }

    private static class QueuesFormLayout extends Form {
        private GridLayout layout;
        QueuesFormLayout(final ResourceBundle bundle) {
            layout = new GridLayout(3, 6);
            layout.setMargin(true, false, false, true);
            layout.setSpacing(true);

            setLayout(layout);

            layout.addComponent(new Label(bundle.getString("wavilon.form.name")), 0, 0);
            layout.addComponent(new Label(bundle.getString("wavilon.form.queues.max.time")), 0, 1);

            Label sec = new Label(bundle.getString("wavilon.form.queues.seconds"));
            sec.setWidth(60, Sizeable.UNITS_PIXELS);
            layout.addComponent(sec, 2, 1);
            layout.setComponentAlignment(sec, Alignment.MIDDLE_RIGHT);
            layout.addComponent(new Label(bundle.getString("wavilon.form.queues.max.length")), 0, 2);

            Label calls = new Label(bundle.getString("wavilon.form.queues.calls"));
            calls.setWidth(40, Sizeable.UNITS_PIXELS);
            layout.addComponent(calls, 2, 2);
            layout.setComponentAlignment(calls, Alignment.MIDDLE_RIGHT);

            layout.addComponent(new Label(bundle.getString("wavilon.form.queues.forward.to.on.max.time.label")), 0, 3);
            layout.addComponent(new Label(bundle.getString("wavilon.form.queues.forward.to.on.max.length.label")), 0, 4);
            layout.addComponent(new Label(bundle.getString("wavilon.form.queues.music.on.hold")), 0, 5);
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
