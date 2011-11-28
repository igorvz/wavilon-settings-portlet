package com.aimprosoft.wavilon.ui.menuitems.forms;

import com.aimprosoft.wavilon.couch.CouchModel;
import com.aimprosoft.wavilon.couch.CouchModelLite;
import com.aimprosoft.wavilon.couch.CouchTypes;
import com.aimprosoft.wavilon.model.Queue;
import com.aimprosoft.wavilon.service.QueueDatabaseService;
import com.aimprosoft.wavilon.spring.ObjectFactory;
import com.aimprosoft.wavilon.util.CouchModelUtil;
import com.aimprosoft.wavilon.util.LayoutUtil;
import com.vaadin.data.validator.IntegerValidator;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.*;
import org.apache.commons.lang.math.NumberUtils;

import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

public class QueuesForm extends GeneralForm {
    private QueueDatabaseService service = ObjectFactory.getBean(QueueDatabaseService.class);
    private Queue queue;
    private Boolean oldMusicOnHold = false;

    public QueuesForm(final ResourceBundle bundle, Table table) {
        super(bundle, table);
    }

    @Override
    public void init(String id, final Object itemId) {
        super.init(id, itemId);
        model = createCoucModel(id, service, CouchTypes.queue);
        queue = createQueue(model);

        if ("-1".equals(id)) {
            setCaption(bundle.getString("wavilon.form.queues.new.queue"));
        } else {
            setCaption(bundle.getString("wavilon.form.queues.edit.queue"));
        }
        final Form form = createForm();

        initForm(form, new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                try {
                    form.commit();

                    String name = (String) form.getField("name").getValue();
                    String maxTimeInput = form.getField("maxTimeInput").getValue().toString();
                    String maxLengthInput = form.getField("maxLengthInput").getValue().toString();
                    String forwardToOnMaxTimeInput = null;
                    List<String> queuesAgents = Collections.emptyList();

                    if (null != form.getField("forwardToOnMaxTimeInput").getValue()) {
                        forwardToOnMaxTimeInput = ((CouchModelLite) form.getField("forwardToOnMaxTimeInput").getValue()).getId();
                    }
                    String forwardToOnMaxLengthInput = null;

                    if (null != form.getField("forwardToOnMaxLengthInput").getValue()) {
                        forwardToOnMaxLengthInput = ((CouchModelLite) form.getField("forwardToOnMaxLengthInput").getValue()).getId();
                    }

                    Boolean musicOnHold = (Boolean) form.getField("musicOnHold").getValue();

                    if (null != model.getRevision()) {
                        table.removeItem(itemId);
                        table.select(null);
                        queuesAgents = (List<String>) model.getOutputs().get("agents");
                    }

                    if (!musicOnHold) {
                        queue.setMusicOnHold(null);
                    } else if (!oldMusicOnHold) {
                        queue.setMusicOnHold(musicOnHold);
                    }


                    queue.setName(name);

                    if (!"".equals(maxTimeInput)) {
                        queue.setMaxTime(NumberUtils.toInt(maxTimeInput));
                    } else {
                        queue.setMaxTime(null);
                    }

                    if (!"".equals(maxLengthInput)) {
                        queue.setMaxLength(NumberUtils.toInt(maxLengthInput));
                    } else {
                        queue.setMaxLength(null);
                    }

                    queue.setForwardToOnMaxTime(forwardToOnMaxTimeInput);
                    queue.setForwardToOnMaxLength(forwardToOnMaxLengthInput);


                    service.addQueue(queue, model, queuesAgents);

                    final Object object = table.addItem();

                    table.getContainerProperty(object, bundle.getString("wavilon.table.queues.column.name")).setValue(queue.getName());
                    table.getContainerProperty(object, bundle.getString("wavilon.table.queues.column.forward.to.on.max.time")).setValue(CouchModelUtil.getCouchModelLite(queue.getForwardToOnMaxTime(), bundle));
                    table.getContainerProperty(object, bundle.getString("wavilon.table.queues.column.forward.to.on.max.length")).setValue(CouchModelUtil.getCouchModelLite(queue.getForwardToOnMaxLength(), bundle));
                    table.getContainerProperty(object, "id").setValue(model.getId());
                    HorizontalLayout buttons = LayoutUtil.createTablesEditRemoveButtons(table, object, model, bundle, null, application.getMainWindow(), new QueuesForm(bundle, table));
                    table.getContainerProperty(object, "").setValue(buttons);

                    LayoutUtil.setTableBackground(table, CouchTypes.queue);

                    getParent().getWindow().showNotification(bundle.getString("wavilon.well.done"));
                    close();
                } catch (Exception ignored) {
                }
            }
        });
    }

    private Queue createQueue(CouchModel model) {
        if (null == model.getRevision()) {
            return newQueue();
        }
        try {
            Queue existingQueue = getModel(model, service, Queue.class);
            if (null != existingQueue.getMusicOnHold()) {
                oldMusicOnHold = true;
            }
            return existingQueue;
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
        maxTimeInput.addValidator(new IntegerValidator(bundle.getString("wavilon.error.massage.queues.max.time.integer")));

        //third row
        TextField maxLengthInput = new TextField();
        maxLengthInput.setWidth(150, Sizeable.UNITS_PIXELS);

        List<CouchModelLite> forwards = getForwards();

        //fourth
        ComboBox forwardToOnMaxTimeInput = new ComboBox();
        fillForward(forwards, forwardToOnMaxTimeInput);

        //fifth
        ComboBox forwardToOnMaxLengthInput = new ComboBox();
        fillForward(forwards, forwardToOnMaxLengthInput);

        //sixth

        CheckBox musicOnHold = new CheckBox();

        if (null != model.getRevision()) {
            name.setValue(queue.getName());

            if (null != queue.getMaxTime()) {
                maxTimeInput.setValue(queue.getMaxTime());
            }
            if (null != queue.getMaxLength()) {
                maxLengthInput.setValue(queue.getMaxLength());
            }

            if (null != queue.getMusicOnHold()) {
                musicOnHold.setValue(true);
            }

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
        forwardTo.setNullSelectionItemId(bundle.getString("wavilon.form.select"));
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

}
