package com.aimprosoft.wavilon.ui.menuitems.settings;

import com.aimprosoft.wavilon.model.ContactModel;
import com.vaadin.data.Property;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.*;

import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;


public class ExtensionsForm extends HorizontalLayout {

    private IndexedContainer tableData;
    private List<String> tableFields;

    private Table phoneNumbers = new Table();
    private Form entityEditor = new Form();

    private VerticalLayout left = new VerticalLayout();
    private VerticalLayout right = new VerticalLayout();

    public ExtensionsForm(ResourceBundle bundle) {
        tableFields = fillFields(bundle);

        tableData = createTableData(bundle);

        initLayout();
        initAddressList(bundle);
    }

    //fill tables fields
    private LinkedList<String> fillFields(ResourceBundle bundle) {
        LinkedList<String> tableFields = new LinkedList<String>();

        tableFields.add(bundle.getString("wavilon.settings.services.phoneNumbers.mobile"));
        tableFields.add("name");

        tableFields.add("email");
        tableFields.add("url");
        tableFields.add("phoneNumber");
        tableFields.add("extensionNumber");

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

        Object[] col = { tableFields.get(0)};

        phoneNumbers.setContainerDataSource(tableData);
        phoneNumbers.setVisibleColumns(col);
        phoneNumbers.setSelectable(true);
        phoneNumbers.setImmediate(true);

        phoneNumbers.addListener(new Property.ValueChangeListener() {
            public void valueChange(Property.ValueChangeEvent event) {
                Object id = phoneNumbers.getValue();

                right.removeAllComponents();

                TextField name = new TextField();
                name.setValue(id == null ? null : phoneNumbers.getItem(id));
                name.setReadOnly(true);

                TextField number = new TextField("Phone number");
                number.setValue(id == null ? null : phoneNumbers.getItem(id));
                number.setRequired(true);
                number.setRequiredError(bundle.getString("wavilon.settings.services.error.phoneNumbers.mobile.empty"));
                number.addValidator(new RegexpValidator("[+][0-9]{10}", bundle.getString("wavilon.settings.services.error.phoneNumbers.mobile.incorrect")));


                TextField extensionNumber = new TextField("Extension number");
                extensionNumber.setValue(id == null ? null : phoneNumbers.getItem(id));
                extensionNumber.setReadOnly(true);

                entityEditor.removeAllProperties();
                entityEditor.addField("name", name);
                entityEditor.addField("phone number", number);
                entityEditor.addField("extension number", extensionNumber);


                Button change = new Button(bundle.getString("wavilon.settings.services.phoneNumbers.change"), entityEditor, "commit");
                change.setClickShortcut(ShortcutAction.KeyCode.ENTER);

                //add form and button
                right.addComponent(entityEditor);
                right.addComponent(change);
            }
        });

        return tableFields;
    }

    //fill table
//    private IndexedContainer createTableData(ResourceBundle bundle) {
//        IndexedContainer ic = new IndexedContainer();
//        List<String> numbers = getNumbers();
//
//        for (String field : tableFields) {
//            ic.addContainerProperty(field, String.class, "");
//        }
//
//        for (String number : numbers) {
//            Object object = ic.addItem();
//            ic.getContainerProperty(object, bundle.getString("wavilon.settings.services.phoneNumbers.mobile")).setValue(number);
//        }
//
//        return ic;
//    }

//    private static List<String> getNumbers() {
//        List<String> numbers = new LinkedList<String>();
//
//        numbers.add("extension 1");
//        numbers.add("extension 2");
//        numbers.add("extension 3");
//        numbers.add("extension 4");
//        numbers.add("extension 5");
//
//        return numbers;
//    }

    private static List<ContactModel> getTestModel() {
        List<ContactModel> contactModels = new LinkedList<ContactModel>();
        for (int i = 1; i < 5; i++) {
            ContactModel model = new ContactModel();
            model.setName("name"+i);
            model.setEmail("email"+i);
            model.setExtensionNumber(i+1111);
            model.setPhoneNumber("number"+i);
            model.setUrl("url"+i);

            contactModels.add(model);
        }
        return contactModels;
    }

     private IndexedContainer createTableData(ResourceBundle bundle) {
        IndexedContainer ic = new IndexedContainer();
        List<ContactModel> numbers = getTestModel();

        for (String field : tableFields) {
            ic.addContainerProperty(field, String.class, "");
        }

        for (ContactModel number : numbers) {
            Object object = ic.addItem();
            ic.getContainerProperty(object, bundle.getString("wavilon.settings.services.phoneNumbers.mobile")).setValue(number.getName()+ " "+number.getExtensionNumber());
            ic.getContainerProperty(object, "name").setValue(number.getName());
            ic.getContainerProperty(object, "email").setValue(number.getEmail());
            ic.getContainerProperty(object, "url").setValue(number.getUrl());
            ic.getContainerProperty(object, "phoneNumber").setValue(number.getPhoneNumber());
            ic.getContainerProperty(object, "extensionNumber").setValue(number.getExtensionNumber());
        }

        return ic;
    }
}
