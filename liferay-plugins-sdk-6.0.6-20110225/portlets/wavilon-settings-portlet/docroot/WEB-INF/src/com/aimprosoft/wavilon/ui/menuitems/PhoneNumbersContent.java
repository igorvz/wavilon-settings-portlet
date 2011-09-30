package com.aimprosoft.wavilon.ui.menuitems;

import com.aimprosoft.wavilon.application.GenericPortletApplication;
import com.aimprosoft.wavilon.model.PhoneNumber;
import com.aimprosoft.wavilon.service.PhoneNumberDatabaseService;
import com.aimprosoft.wavilon.spring.ObjectFactory;
import com.aimprosoft.wavilon.ui.menuitems.forms.ConfirmingRemove;
import com.aimprosoft.wavilon.ui.menuitems.forms.PhoneNumbersForm;
import com.liferay.portal.util.PortalUtil;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.*;

import javax.portlet.PortletRequest;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class PhoneNumbersContent extends VerticalLayout {
    private IndexedContainer tableData;
    private List<String> tableFields;
    private ResourceBundle bundle;
    private Table phoneNumbers = new Table();
    private PortletRequest request;
    private PhoneNumberDatabaseService service = (PhoneNumberDatabaseService) ObjectFactory.getBean(PhoneNumberDatabaseService.class);
    private List<String> hiddenFields;
    private PhoneNumbersForm phoneNumbersForm;

    public PhoneNumbersContent(ResourceBundle bundle) {
        this.bundle = bundle;
    }

    public void init() {
        request = ((GenericPortletApplication) getApplication()).getPortletRequest();
        this.hiddenFields = fillHiddenFields();
        this.tableFields = fillFields();
        this.tableData = createTableData();

        setWidth(100, Sizeable.UNITS_PERCENTAGE);
        setSizeUndefined();
        initLayout();
        initPhoneNumbers();
    }

    private List<String> fillFields() {
        List<String> tableFields = new LinkedList<String>();

        tableFields.add("NUMBER");
        tableFields.add("NAME");

        return tableFields;
    }

    private void initLayout() {
        HorizontalLayout head = createHead();
        setWidth(100, Sizeable.UNITS_PERCENTAGE);
        addComponent(head);

        this.phoneNumbers.setContainerDataSource(this.tableData);
        this.phoneNumbers.setWidth(100, Sizeable.UNITS_PERCENTAGE);

        this.phoneNumbers.addStyleName("tableCustom");
        addComponent(this.phoneNumbers);
    }

    private void initPhoneNumbers() {
        this.phoneNumbers.setContainerDataSource(this.tableData);
        this.phoneNumbers.setVisibleColumns(this.tableFields.toArray());
        this.phoneNumbers.setSelectable(true);
        this.phoneNumbers.setImmediate(true);

        this.phoneNumbers.addListener(new ItemClickEvent.ItemClickListener() {
            public void itemClick(ItemClickEvent event) {
                if (event.isDoubleClick()) {
                    Item item = event.getItem();
                    if (null != item) {
                        getForm((String) event.getItem().getItemProperty("id").getValue());
                    }
                }
            }
        });

    }

    private IndexedContainer createTableData() {
        IndexedContainer ic = new IndexedContainer();
        List<PhoneNumber> numbers = getNumbers();

        for (String field : this.hiddenFields) {
            ic.addContainerProperty(field, String.class, "");
        }

        for (PhoneNumber number : numbers) {
            Object object = ic.addItem();
            ic.getContainerProperty(object, "NUMBER").setValue(number.getNumber());
            ic.getContainerProperty(object, "NAME").setValue(number.getName());
            ic.getContainerProperty(object, "id").setValue(number.getId());
        }

        return ic;
    }

    private List<PhoneNumber> getNumbers() {
        try {
            return service.getAllPhoneNumbersByUser(PortalUtil.getUserId(request), PortalUtil.getScopeGroupId(request));
        } catch (Exception ignored) {
        }
        return Collections.emptyList();
    }

    public HorizontalLayout createHead() {
        HorizontalLayout head = new HorizontalLayout();
        head.setWidth(100, Sizeable.UNITS_PERCENTAGE);
        Label headLabel = new Label("Phone Numbers");
        head.addComponent(headLabel);
        head.setMargin(false);
        head.addStyleName("headLine");
        headLabel.addStyleName("phoneHeader");
        headLabel.addStyleName("tableHeader");

        HorizontalLayout addRemoveButtons = createButtons();
        head.addComponent(addRemoveButtons);

        head.setComponentAlignment(headLabel, Alignment.TOP_LEFT);
        head.setComponentAlignment(addRemoveButtons, Alignment.MIDDLE_RIGHT);

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

                    ConfirmingRemove confirmingRemove = new ConfirmingRemove(bundle);
                    getWindow().addWindow(confirmingRemove);
                    confirmingRemove.init(phoneNumbersID, phoneNumbers);
                    confirmingRemove.center();
                    confirmingRemove.setWidth("300px");
                    confirmingRemove.setHeight("180px");
                } else {
                    PhoneNumbersContent.this.getWindow().showNotification("Select Phone Number");
                }
            }
        }));
        return addRemoveButtons;
    }

    private void getForm(String id) {
        phoneNumbersForm = new PhoneNumbersForm(this.bundle, this.phoneNumbers);
        phoneNumbersForm.setWidth("400px");
        phoneNumbersForm.setHeight("300px");
        phoneNumbersForm.center();
        phoneNumbersForm.setModal(true);

        getWindow().addWindow(phoneNumbersForm);
        phoneNumbersForm.init(id);
    }

    private List<String> fillHiddenFields() {
        List<String> hiddenFields = new LinkedList<String>();

        hiddenFields.add("NUMBER");
        hiddenFields.add("NAME");
        hiddenFields.add("id");

        return hiddenFields;
    }
}