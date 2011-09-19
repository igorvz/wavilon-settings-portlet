package com.aimprosoft.wavilon.ui.menuitems.settings;

import com.vaadin.data.Property;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.*;

import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class RecordingsForm extends HorizontalLayout {

    private IndexedContainer tableData;
    private List<String> tableFields;

    private Table recordingsList = new Table();

    private VerticalLayout left = new VerticalLayout();
    private VerticalLayout right = new VerticalLayout();

    public RecordingsForm(ResourceBundle bundle) {

        tableFields = fillFields(bundle);

        tableData = createTableData(bundle);

        initLayout();
        initAddressList(bundle);
    }

    private LinkedList<String> fillFields(ResourceBundle bundle) {
        LinkedList<String> tableFields = new LinkedList<String>();

        tableFields.add(bundle.getString("wavilon.settings.services.phoneNumbers.mobile"));

        return tableFields;
    }

    private void initLayout() {
        // add table
        recordingsList.setContainerDataSource(tableData);
        left.addComponent(recordingsList);

        addComponent(left);
        addComponent(right);
    }

    private List<String> initAddressList(final ResourceBundle bundle) {
        recordingsList.setContainerDataSource(tableData);
        recordingsList.setVisibleColumns(tableFields.toArray());
        recordingsList.setSelectable(true);
        recordingsList.setImmediate(true);

        recordingsList.addListener(new Property.ValueChangeListener() {
            public void valueChange(Property.ValueChangeEvent event) {
                Object id = recordingsList.getValue();

                addRightPartForm(id, right, recordingsList);
            }
        });

        HorizontalLayout buttons = new HorizontalLayout();

        Button plus = new Button("+");
        plus.addListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {

                addRightPartForm(null, right, recordingsList);
            }
        });

        Button minus = new Button("-");
        minus.addListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {

            }
        });

        buttons.addComponent(plus);
        buttons.addComponent(minus);
        left.addComponent(buttons);

        return tableFields;
    }

    private IndexedContainer createTableData(ResourceBundle bundle) {
        IndexedContainer ic = new IndexedContainer();
        List<String> numbers = getRecordings();

        for (String field : tableFields) {
            ic.addContainerProperty(field, String.class, "");
        }

        for (String number : numbers) {
            Object object = ic.addItem();
            ic.getContainerProperty(object, bundle.getString("wavilon.settings.services.phoneNumbers.mobile")).setValue(number);
        }
        return ic;
    }

    private static List<String> getRecordings() {
        List<String> numbers = new LinkedList<String>();

        numbers.add("recording 1");
        numbers.add("recording 2");
        numbers.add("recording 3");
        numbers.add("recording 4");
        numbers.add("recording 5");

        return numbers;
    }

    private static void addRightPartForm(Object id, VerticalLayout right, Table phoneNumbers) {
        right.removeAllComponents();

        Form form = new Form();
        TextField field = new TextField("Name");

        if (id != null) {
            field.setValue(id == null ? null : phoneNumbers.getItem(id));
        }

        Upload upload = new Upload("Upload file", null);
        Button select = new Button("Select", form, "commit");

        select.setClickShortcut(ShortcutAction.KeyCode.ENTER);

        form.addField("name", field);

        right.addComponent(form);
        right.addComponent(upload);
    }
}
