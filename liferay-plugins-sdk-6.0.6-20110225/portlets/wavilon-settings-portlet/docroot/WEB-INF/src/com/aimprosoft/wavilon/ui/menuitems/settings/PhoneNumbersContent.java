package com.aimprosoft.wavilon.ui.menuitems.settings;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.*;

import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class PhoneNumbersContent extends HorizontalLayout {
    private IndexedContainer tableData;
    private List<String> tableFields;

    private Table phoneNumbers = new Table();
    private Form entityEditor = new Form();

    private VerticalLayout left = new VerticalLayout();
    private VerticalLayout right = new VerticalLayout();

    public PhoneNumbersContent(ResourceBundle bundle) {
        tableFields = fillFields(bundle);

        tableData = createTableData(bundle);

        initLayout();
        initAddressList(bundle);
    }

    //fill tables fields
    private LinkedList<String> fillFields(ResourceBundle bundle) {
        LinkedList<String> tableFields = new LinkedList<String>();

        tableFields.add(bundle.getString("wavilon.settings.services.phoneNumbers.mobile"));

        return tableFields;
    }


    private void initLayout() {
        // add table
        phoneNumbers.setContainerDataSource(tableData);
        left.addComponent(phoneNumbers);

        //add lift and right parts
        addComponent(left);
        addComponent(right);
    }

    private List<String> initAddressList(final ResourceBundle bundle) {
        phoneNumbers.setContainerDataSource(tableData);
        phoneNumbers.setVisibleColumns(tableFields.toArray());
        phoneNumbers.setSelectable(true);
        phoneNumbers.setImmediate(true);

        phoneNumbers.addListener(new Property.ValueChangeListener() {
            public void valueChange(ValueChangeEvent event) {
                Object id = phoneNumbers.getValue();

                right.removeAllComponents();

                TextField field = new TextField(bundle.getString("wavilon.settings.services.phoneNumbers.mobile"));
                field.setValue(id == null ? null : phoneNumbers.getItem(id));
                field.setRequired(true);
                field.setRequiredError(bundle.getString("wavilon.settings.services.error.phoneNumbers.mobile.empty"));
                field.addValidator(new RegexpValidator("[+][0-9]{10}", bundle.getString("wavilon.settings.services.error.phoneNumbers.mobile.incorrect")));

                entityEditor.removeAllProperties();
                entityEditor.addField("mobile", field);

                Button change = new Button(bundle.getString("wavilon.settings.services.phoneNumbers.change"), entityEditor, "commit");
                change.setClickShortcut(KeyCode.ENTER);

                //add form and button
                right.addComponent(entityEditor);
                right.addComponent(change);
            }
        });

        return tableFields;
    }


    //fill table
    private IndexedContainer createTableData(ResourceBundle bundle) {
        IndexedContainer ic = new IndexedContainer();
        List<String> numbers = getNumbers();

        for (String field : tableFields) {
            ic.addContainerProperty(field, String.class, "");
        }

        for (String number : numbers) {
            Object object = ic.addItem();
            ic.getContainerProperty(object, bundle.getString("wavilon.settings.services.phoneNumbers.mobile")).setValue(number);
        }

        return ic;
    }

    //todo retrieve from DB
    private static List<String> getNumbers() {
        List<String> numbers = new LinkedList<String>();

        numbers.add("+3804565458");
        numbers.add("+3806512354");
        numbers.add("+3801155114");
        numbers.add("+3805465455");
        numbers.add("+3805445556");

        return numbers;
    }


}
