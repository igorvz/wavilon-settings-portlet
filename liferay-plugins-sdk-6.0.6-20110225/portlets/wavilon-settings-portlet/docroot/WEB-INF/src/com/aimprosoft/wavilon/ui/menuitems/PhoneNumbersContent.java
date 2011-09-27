package com.aimprosoft.wavilon.ui.menuitems;

import com.aimprosoft.wavilon.application.GenericPortletApplication;
import com.aimprosoft.wavilon.model.PhoneNumber;
import com.aimprosoft.wavilon.service.PhoneNumberDatabaseService;
import com.aimprosoft.wavilon.spring.ObjectFactory;
import com.aimprosoft.wavilon.ui.menuitems.forms.PhoneNumbersForm;
import com.liferay.portal.util.PortalUtil;
import com.vaadin.data.Property;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.*;

import javax.portlet.PortletRequest;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class PhoneNumbersContent extends VerticalLayout {
    private IndexedContainer tableData;
    private List<String> tableFields;
    private ResourceBundle bundle;
    private Table phoneNumbers = new Table();
    private static PortletRequest request;
    private static PhoneNumberDatabaseService service = (PhoneNumberDatabaseService) ObjectFactory.getBean(PhoneNumberDatabaseService.class);
    private List<String> hiddenFields;
    private String column = "<=>";

    public PhoneNumbersContent(ResourceBundle bundle) {
        this.bundle = bundle;
    }

    public void init() {
        request = ((GenericPortletApplication) getApplication()).getPortletRequest();
        this.hiddenFields = fillHiddenFields();
        this.tableFields = fillFields();
        this.tableData = createTableData(this.bundle);

        setHeight(100.0F, 8);
        setWidth(100.0F, 8);
        setSizeUndefined();
        initLayout();
        initPhoneNumbers();
    }

    private List<String> fillFields() {
        List<String> tableFields = new LinkedList<String>();

        tableFields.add(this.column);

        return tableFields;
    }

    private void initLayout() {
        HorizontalLayout head = createHead();
        setWidth(100.0F, 8);
        addComponent(head);

        this.phoneNumbers.setContainerDataSource(this.tableData);
        this.phoneNumbers.setWidth(100.0F, 8);
        this.phoneNumbers.setStyleName("phoneNumbers");
        addComponent(this.phoneNumbers);
    }

    private List<String> initPhoneNumbers() {
        this.phoneNumbers.setContainerDataSource(this.tableData);
        this.phoneNumbers.setVisibleColumns(this.tableFields.toArray());
        this.phoneNumbers.setSelectable(true);
        this.phoneNumbers.setImmediate(true);

        this.phoneNumbers.addListener(new Property.ValueChangeListener() {
            public void valueChange(Property.ValueChangeEvent event) {
                Object id = PhoneNumbersContent.this.phoneNumbers.getValue();

                if (null != id)
                    PhoneNumbersContent.this.getForm((String) PhoneNumbersContent.this.phoneNumbers.getItem(id).getItemProperty("id").getValue());
            }
        });
        return this.tableFields;
    }

    private IndexedContainer createTableData(ResourceBundle bundle) {
        IndexedContainer ic = new IndexedContainer();
        List<PhoneNumber> numbers = getNumbers();

        for (String field : this.hiddenFields) {
            ic.addContainerProperty(field, String.class, "");
        }

        for (PhoneNumber number : numbers) {
            Object object = ic.addItem();
            ic.getContainerProperty(object, this.column).setValue(number.getName());
            ic.getContainerProperty(object, "id").setValue(number.getId());
        }

        return ic;
    }

    private static List<PhoneNumber> getNumbers() {
        try {
            return service.getAllPhoneNumbersByUser(PortalUtil.getUserId(request), PortalUtil.getScopeGroupId(request));
        } catch (Exception ignored) {
        }
        return Collections.emptyList();
    }

    public HorizontalLayout createHead() {
        HorizontalLayout head = new HorizontalLayout();
        head.setWidth(100.0F, 8);
        Label headLabel = new Label("Phone Numbers");
        head.addComponent(headLabel);

        HorizontalLayout addRemoveButtons = createButtons();
        head.addComponent(addRemoveButtons);

        head.setComponentAlignment(headLabel, Alignment.TOP_LEFT);
        head.setComponentAlignment(addRemoveButtons, Alignment.TOP_RIGHT);

        return head;
    }

    private HorizontalLayout createButtons() {
        HorizontalLayout addRemoveButtons = new HorizontalLayout();
        addRemoveButtons.addComponent(new Button("+", new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                PhoneNumbersContent.this.getForm("-1");
            }
        }));
        addRemoveButtons.addComponent(new Button("-", new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                Object id = PhoneNumbersContent.this.phoneNumbers.getValue();
                if (null != id) {
                    String phoneNumbersID = (String) PhoneNumbersContent.this.phoneNumbers.getItem(id).getItemProperty("id").getValue();
                    try {
                        PhoneNumbersContent.service.removePhoneNumber(phoneNumbersID);
                    } catch (IOException ignored) {
                    }
                    PhoneNumbersContent.this.phoneNumbers.removeItem(PhoneNumbersContent.this.phoneNumbers.getValue());
                    PhoneNumbersContent.this.phoneNumbers.select(null);
                } else {
                    PhoneNumbersContent.this.getWindow().showNotification("Select Phone Number");
                }
            }
        }));
        return addRemoveButtons;
    }

    private void getForm(String id) {
        PhoneNumbersForm phoneNumbersForm = new PhoneNumbersForm(this.bundle, this.phoneNumbers);
        phoneNumbersForm.setWidth(330.0F, 0);
        phoneNumbersForm.setHeight(300.0F, 0);

        getWindow().addWindow(phoneNumbersForm);
        phoneNumbersForm.init(id);
    }

    private List<String> fillHiddenFields() {
        List<String> tableFields = new LinkedList<String>();

        tableFields.add(this.column);
        tableFields.add("id");

        return tableFields;
    }
}