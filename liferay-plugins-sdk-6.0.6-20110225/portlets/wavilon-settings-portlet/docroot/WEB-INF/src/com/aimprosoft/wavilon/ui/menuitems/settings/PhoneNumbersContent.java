package com.aimprosoft.wavilon.ui.menuitems.settings;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

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
        initAddressList();

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

    private void fillForm() {

    }


    private List<String> initAddressList() {
        phoneNumbers.setContainerDataSource(tableData);
        phoneNumbers.setVisibleColumns(tableFields.toArray());
        phoneNumbers.setSelectable(true);
        phoneNumbers.setImmediate(true);

        phoneNumbers.addListener(new Property.ValueChangeListener() {
            public void valueChange(ValueChangeEvent event) {
                Object id = phoneNumbers.getValue();
                entityEditor.setItemDataSource(id == null ? null : phoneNumbers.getItem(id));

                //add form
                right.removeAllComponents();
                right.addComponent(entityEditor);

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

        numbers.add("+380504565458");
        numbers.add("+380996512354");
        numbers.add("+380661155114");
        numbers.add("+380975465455");
        numbers.add("+380505445556");

        return numbers;
    }


}
