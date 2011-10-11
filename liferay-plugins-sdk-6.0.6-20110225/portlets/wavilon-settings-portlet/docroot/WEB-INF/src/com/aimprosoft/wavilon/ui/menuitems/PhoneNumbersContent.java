package com.aimprosoft.wavilon.ui.menuitems;

import com.aimprosoft.wavilon.application.GenericPortletApplication;
import com.aimprosoft.wavilon.couch.CouchModel;
import com.aimprosoft.wavilon.couch.CouchModelLite;
import com.aimprosoft.wavilon.model.PhoneNumber;
import com.aimprosoft.wavilon.service.PhoneNumberDatabaseService;
import com.aimprosoft.wavilon.spring.ObjectFactory;
import com.aimprosoft.wavilon.ui.menuitems.forms.ConfirmingRemove;
import com.aimprosoft.wavilon.ui.menuitems.forms.PhoneNumbersForm;
import com.aimprosoft.wavilon.util.CouchModelUtil;
import com.liferay.portal.util.PortalUtil;
import com.vaadin.data.Item;
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
    private PhoneNumberDatabaseService service = ObjectFactory.getBean(PhoneNumberDatabaseService.class);
    private List<String> hiddenFields;

    public PhoneNumbersContent(ResourceBundle bundle) {
        this.bundle = bundle;
    }

    public void init() {
        request = ((GenericPortletApplication) getApplication()).getPortletRequest();
        this.hiddenFields = fillHiddenFields();
        this.tableFields = fillFields();
        this.tableData = createTableData();

//        setWidth(100, Sizeable.UNITS_PERCENTAGE);
        setSizeFull();
        initLayout();
        initPhoneNumbers();
    }

    private void initLayout() {
        HorizontalLayout head = createHead();
        setWidth(100, Sizeable.UNITS_PERCENTAGE);
        addComponent(head);

        this.phoneNumbers.setContainerDataSource(this.tableData);
        this.phoneNumbers.setWidth(100, Sizeable.UNITS_PERCENTAGE);
        this.phoneNumbers.setFooterVisible(false);
        this.phoneNumbers.addStyleName("tableCustom");
        addComponent(this.phoneNumbers);
    }

    private void initPhoneNumbers() {
        this.phoneNumbers.setVisibleColumns(this.tableFields.toArray());
        this.phoneNumbers.setSelectable(true);
        this.phoneNumbers.setImmediate(true);

        this.phoneNumbers.addListener(new ItemClickEvent.ItemClickListener() {
            public void itemClick(ItemClickEvent event) {
                if (event.isDoubleClick()) {
                    Item item = event.getItem();
                    if (null != item) {
                        getForm((String) event.getItem().getItemProperty("id").getValue(), event.getItemId());
                    }
                }
            }
        });
    }

    private IndexedContainer createTableData() {
        IndexedContainer ic = new IndexedContainer();
        List<CouchModel> couchModels = getCouchModels();

        for (String field : hiddenFields) {
            if ("".equals(field)) {
                ic.addContainerProperty(field, Button.class, "");
            } else {
                ic.addContainerProperty(field, String.class, "");
            }
        }

        if (!couchModels.isEmpty()) {

            for (final CouchModel couchModel : couchModels) {
                final Object object = ic.addItem();
                PhoneNumber phoneNumber = getPhoneNumber(couchModel);
                CouchModelLite forward = CouchModelUtil.getCouchModelLite((String) couchModel.getOutputs().get("startnode"));

                ic.getContainerProperty(object, "NUMBER").setValue(phoneNumber.getLocator());
                ic.getContainerProperty(object, "NAME").setValue(phoneNumber.getName());
                ic.getContainerProperty(object, "id").setValue(couchModel.getId());
                ic.getContainerProperty(object, "FORWARD CALLS TO").setValue(forward);
                Button removeButton = new Button("", new Button.ClickListener() {
                    public void buttonClick(Button.ClickEvent event) {
                        phoneNumbers.select(object);
                        ConfirmingRemove confirmingRemove = new ConfirmingRemove(bundle);
                        getWindow().addWindow(confirmingRemove);
                        confirmingRemove.init(couchModel.getId(), phoneNumbers);
                        confirmingRemove.center();
                        confirmingRemove.setWidth("300px");
                        confirmingRemove.setHeight("180px");
                    }
                });
                ic.getContainerProperty(object, "").setValue(removeButton);
            }
        }
        return ic;
    }

    private PhoneNumber getPhoneNumber(CouchModel couchModel) {
        try {
            return service.getPhoneNumber(couchModel);
        } catch (Exception e) {
            return new PhoneNumber();
        }
    }

    public HorizontalLayout createHead() {
        HorizontalLayout head = new HorizontalLayout();
        head.setWidth(100, Sizeable.UNITS_PERCENTAGE);
        Label headLabel = new Label("Phone Numbers");
        head.addComponent(headLabel);
        head.setMargin(false);
        head.addStyleName("head");
        headLabel.addStyleName("label");

        HorizontalLayout addButton = createButton();
        head.addComponent(addButton);

        head.setComponentAlignment(headLabel, Alignment.TOP_LEFT);
        head.setComponentAlignment(addButton, Alignment.MIDDLE_RIGHT);

        return head;
    }

    private HorizontalLayout createButton() {
        HorizontalLayout addButton = new HorizontalLayout();
        addButton.addComponent(new Button("Add", new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                getForm("-1", "-1");
            }
        }));
        return addButton;
    }

    private void getForm(String id, Object itemId) {
        PhoneNumbersForm phoneNumbersForm = new PhoneNumbersForm(this.bundle, this.phoneNumbers);
        phoneNumbersForm.setWidth("400px");
        phoneNumbersForm.setHeight("300px");
        phoneNumbersForm.center();
        phoneNumbersForm.setModal(true);

        getWindow().addWindow(phoneNumbersForm);
        phoneNumbersForm.init(id, itemId);
    }

    private List<String> fillHiddenFields() {
        List<String> hiddenFields = new LinkedList<String>();

        hiddenFields.add("NUMBER");
        hiddenFields.add("NAME");
        hiddenFields.add("id");
        hiddenFields.add("FORWARD CALLS TO");
        hiddenFields.add("");

        return hiddenFields;
    }

    private List<String> fillFields() {
        List<String> tableFields = new LinkedList<String>();

        tableFields.add("NUMBER");
        tableFields.add("NAME");
        tableFields.add("FORWARD CALLS TO");
        tableFields.add("");

        return tableFields;
    }

    private List<CouchModel> getCouchModels() {
        try {
            return service.getAllUsersCouchModelToPhoneNumber(PortalUtil.getUserId(request), PortalUtil.getScopeGroupId(request));
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

}