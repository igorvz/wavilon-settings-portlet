package com.aimprosoft.wavilon.ui.menuitems.forms;

import com.aimprosoft.wavilon.model.Queue;
import com.aimprosoft.wavilon.service.QueueDatabaseService;
import com.aimprosoft.wavilon.service.impl.QueueCouchDBServiceImpl;
import com.aimprosoft.wavilon.spring.ObjectFactory;
import com.vaadin.data.validator.IntegerValidator;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.*;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.util.ResourceBundle;

public class QueuesForm extends VerticalLayout {
    private ResourceBundle bundle;
    private QueueDatabaseService service = ObjectFactory.getBean(QueueDatabaseService.class);

    public QueuesForm(final ResourceBundle bundle, String queueId) {
        this.bundle = bundle;
        Queue queue = getQueue(queueId);


        final Form queueForm = new QueuesFormLayout();

        //first row
        TextField maxTimeInput = new TextField();
        maxTimeInput.setWidth(40, Sizeable.UNITS_PIXELS);
        maxTimeInput.setRequired(true);
        maxTimeInput.setRequiredError(bundle.getString("wavilon.settings.validation.form.error.empty.max.time.input"));
        maxTimeInput.addValidator(new IntegerValidator(bundle.getString("wavilon.settings.validation.form.error.integer.max.time.input")));
        ComboBox firstRowType = new ComboBox();
        firstRowType.setWidth(100, Sizeable.UNITS_PIXELS);
        ComboBox firstRowSelectNode = new ComboBox();
        firstRowSelectNode.setWidth(100, Sizeable.UNITS_PIXELS);

        //second row
        TextField maxLengthInput = new TextField();
        maxLengthInput.setWidth(40, Sizeable.UNITS_PIXELS);
        maxLengthInput.setRequired(true);
        maxLengthInput.setRequiredError(bundle.getString("wavilon.settings.validation.form.error.empty.max.length.input"));
        maxLengthInput.addValidator(new IntegerValidator(bundle.getString("wavilon.settings.validation.form.error.integer.max.length.input")));
        ComboBox secondRowType = new ComboBox();
        secondRowType.setWidth(100, Sizeable.UNITS_PIXELS);
        ComboBox secondRowSelectNode = new ComboBox();
        secondRowSelectNode.setWidth(100, Sizeable.UNITS_PIXELS);

        //third row
        ComboBox hold = new ComboBox();
        hold.setWidth(100, Sizeable.UNITS_PIXELS);

        //twin col select
        TwinColSelect agents = new TwinColSelect();
        agents.setHeight(120, Sizeable.UNITS_PIXELS);
        agents.setNullSelectionAllowed(true);
        agents.setMultiSelect(true);
        agents.setImmediate(true);
        agents.setLeftColumnCaption("Agents In Queue");
        agents.setRightColumnCaption("A___ agents");

        if (queue != null){
            //TODO fill form fields from Queue model
        }

        queueForm.addField("maxTimeInput", maxTimeInput);
        queueForm.addField("firstRowType", firstRowType);
        queueForm.addField("firstRowSelectNode", firstRowSelectNode);

        queueForm.addField("maxLengthInput", maxLengthInput);
        queueForm.addField("secondRowType", secondRowType);
        queueForm.addField("secondRowSelectNode", secondRowSelectNode);

        queueForm.addField("hold", hold);

        queueForm.addField("agents", agents);

        Button apply = new Button(bundle.getString("wavilon.settings.validation.form.button.save"), new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                try {
                    queueForm.commit();

                    getWindow().showNotification("Is processed but not saved");

                } catch (Exception ignored) {
                }
            }
        });
        addComponent(queueForm);
        addComponent(apply);

    }

    private Queue getQueue(String queueId) {
        if (StringUtils.isEmpty(queueId)){
            return null;
        }
        try {
            return service.getQueue(queueId);
        } catch (IOException e) {
            return null;
        }
    }

    private void fillAgents(TwinColSelect agents) {
        agents.setNullSelectionAllowed(true);
        agents.setMultiSelect(true);
        agents.setImmediate(true);
        agents.setLeftColumnCaption("Agents In Queue");
        agents.setRightColumnCaption("A___ agents");

    }

    private static class QueuesFormLayout extends Form {
        private GridLayout layout;

        QueuesFormLayout() {
            layout = new GridLayout(41, 4);
            layout.setMargin(true, false, false, true);
            layout.setSpacing(true);


            setLayout(layout);

            Label maxTime = new Label("Queue Max Time");
            layout.addComponent(maxTime, 0, 0, 9, 0);

            Label seconds = new Label("seconds");
            layout.addComponent(seconds, 13, 0, 17, 0);

            Label firstRowSeparator = new Label("");
            firstRowSeparator.setWidth(20, Sizeable.UNITS_PIXELS);
            layout.addComponent(firstRowSeparator, 18, 0);

            Label firstRowDistinction = new Label("Distinction on Max Time");
            layout.addComponent(firstRowDistinction, 19, 0, 32, 0);


            Label maxLength = new Label("Queue Max Length");
            layout.addComponent(maxLength, 0, 1, 9, 1);

            Label calls = new Label("calls");
            layout.addComponent(calls, 13, 1, 17, 1);

            Label secondRowSeparator = new Label("");
            secondRowSeparator.setWidth(20, Sizeable.UNITS_PIXELS);
            layout.addComponent(secondRowSeparator, 18, 1);

            Label secondRowDistinction = new Label("Distinction on Max Length");
            layout.addComponent(secondRowDistinction, 19, 1, 32, 1);

            Label musicAnHold = new Label("Music an Hold");
            layout.addComponent(musicAnHold, 0, 2, 7, 2);

        }


        @Override
        protected void attachField(Object propertyId, Field field) {
            if (propertyId.equals("maxTimeInput")) {
                layout.addComponent(field, 10, 0, 12, 0);
            } else if (propertyId.equals("firstRowType")) {
                layout.addComponent(field, 33, 0, 36, 0);
            } else if (propertyId.equals("firstRowSelectNode")) {
                layout.addComponent(field, 37, 0, 40, 0);
            } else if (propertyId.equals("maxLengthInput")) {
                layout.addComponent(field, 10, 1, 12, 1);
            } else if (propertyId.equals("secondRowType")) {
                layout.addComponent(field, 33, 1, 36, 1);
            } else if (propertyId.equals("secondRowSelectNode")) {
                layout.addComponent(field, 37, 1, 40, 1);
            } else if (propertyId.equals("hold")) {
                layout.addComponent(field, 10, 2 , 20, 2);
            } else if (propertyId.equals("agents")) {
                layout.addComponent(field, 10, 3, 30, 3);
            }
        }


    }
}
