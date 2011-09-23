package com.aimprosoft.wavilon.ui.menuitems.settings;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.terminal.Sizeable;
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

        this.setWidth(100, Sizeable.UNITS_PERCENTAGE);
        this.setSizeFull();
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

        phoneNumbers.setHeight(330, Sizeable.UNITS_PIXELS);
        phoneNumbers.setStyleName("phoneNumbers");

        //add lift and right parts
        this.addComponent(left);
        left.addStyleName("leftSide");
        left.setWidth(Sizeable.SIZE_UNDEFINED, 0);
        this.addComponent(right);
        right.addStyleName("rightSide");
        right.setWidth(100, Sizeable.UNITS_PERCENTAGE);
        this.setExpandRatio(left, 0.5f);
        this.setExpandRatio(right, 9.0f);
    }

    private void fillForm() {

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

                Label headerForm = new Label(id == null ? null : phoneNumbers.getItem(id).toString());
                //styles
                headerForm.setHeight(27, Sizeable.UNITS_PIXELS);
                headerForm.setWidth("100%");
                headerForm.addStyleName("headerForm");

                TextField field = new TextField("Phone Number");
                field.setValue(id == null ? null : phoneNumbers.getItem(id));
                field.setRequired(true);

                field.setRequiredError(bundle.getString("wavilon.settings.services.error.phoneNumbers.mobile.empty"));
                field.addValidator(new RegexpValidator("[+][0-9]{10}", bundle.getString("wavilon.settings.services.error.phoneNumbers.mobile.incorrect")));

                entityEditor.removeAllProperties();
                entityEditor.addField("mobile", field);
                //styles
                entityEditor.addStyleName("labelField");
                entityEditor.setHeight(33, Sizeable.UNITS_PIXELS);
                entityEditor.setWidth("100%");

                Button change = new Button(bundle.getString("wavilon.settings.services.phoneNumbers.change"), entityEditor, "commit");
                change.setClickShortcut(KeyCode.ENTER);
                change.addStyleName("buttonForm");

                //add form and button
                right.addComponent(headerForm);
                right.addComponent(entityEditor);
                right.addComponent(change);
                //styles
                right.addStyleName("formRegion");
                right.setMargin(false, true, false, true);
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

        numbers.add("+3805045658");
        numbers.add("+3809965354");
        numbers.add("+3806611114");
        numbers.add("+3809755455");
        numbers.add("+3805054556");

        return numbers;
    }

}
