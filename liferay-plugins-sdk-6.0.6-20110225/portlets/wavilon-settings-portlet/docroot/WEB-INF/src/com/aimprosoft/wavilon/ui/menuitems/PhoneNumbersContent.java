package com.aimprosoft.wavilon.ui.menuitems;

import com.aimprosoft.wavilon.application.GenericPortletApplication;
import com.aimprosoft.wavilon.couch.CouchModel;
import com.aimprosoft.wavilon.couch.CouchModelLite;
import com.aimprosoft.wavilon.couch.CouchTypes;
import com.aimprosoft.wavilon.model.PhoneNumber;
import com.aimprosoft.wavilon.service.PhoneNumberDatabaseService;
import com.aimprosoft.wavilon.spring.ObjectFactory;
import com.aimprosoft.wavilon.ui.menuitems.forms.ConfirmingRemove;
import com.aimprosoft.wavilon.ui.menuitems.forms.PhoneNumbersForm;
import com.aimprosoft.wavilon.util.CouchModelUtil;
import com.aimprosoft.wavilon.util.LayoutUtil;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.Button;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

import javax.portlet.PortletRequest;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class PhoneNumbersContent extends VerticalLayout {
    private IndexedContainer tableData;
    private List<String> tableFields;
    private ResourceBundle bundle;
    private Table phoneNumbersTable = new Table();
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

        setSizeFull();
        initLayout();
        initPhoneNumbers();
    }

    private void initLayout() {
        VerticalLayout head = LayoutUtil.createHead(bundle, phoneNumbersTable, CouchTypes.service, getWindow());
        setWidth(100, Sizeable.UNITS_PERCENTAGE);
        addComponent(head);

        this.phoneNumbersTable.setColumnWidth("", 60);
        this.phoneNumbersTable.setColumnExpandRatio(bundle.getString("wavilon.table.phonenumbers.column.number"), 1);
        this.phoneNumbersTable.setColumnExpandRatio(bundle.getString("wavilon.table.phonenumbers.column.name"), 1);
        this.phoneNumbersTable.setColumnExpandRatio(bundle.getString("wavilon.table.phonenumbers.column.forward.calls.to"), 1);
        this.phoneNumbersTable.setContainerDataSource(this.tableData);
        this.phoneNumbersTable.setWidth(100, Sizeable.UNITS_PERCENTAGE);
        this.phoneNumbersTable.setHeight("555px");
        this.phoneNumbersTable.setFooterVisible(false);
        this.phoneNumbersTable.addStyleName("tableCustom");
        addComponent(this.phoneNumbersTable);
    }

    private void initPhoneNumbers() {
        this.phoneNumbersTable.setVisibleColumns(this.tableFields.toArray());
        this.phoneNumbersTable.setSelectable(true);
        this.phoneNumbersTable.setImmediate(true);

        this.phoneNumbersTable.addListener(new ItemClickEvent.ItemClickListener() {
            public void itemClick(ItemClickEvent event) {
                if (event.isDoubleClick()) {
                    Item item = event.getItem();
                    if (null != item) {
                        LayoutUtil.getForm((String) event.getItem().getItemProperty("id").getValue(), event.getItemId(), getWindow(), new PhoneNumbersForm(bundle, phoneNumbersTable));
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
                final PhoneNumber phoneNumber = getPhoneNumber(couchModel);
                CouchModelLite forward = CouchModelUtil.getCouchModelLite((String) couchModel.getOutputs().get("startnode"), bundle);

                ic.getContainerProperty(object, bundle.getString("wavilon.table.phonenumbers.column.number")).setValue(phoneNumber.getLocator());
                ic.getContainerProperty(object, bundle.getString("wavilon.table.phonenumbers.column.name")).setValue(phoneNumber.getName());
                ic.getContainerProperty(object, "id").setValue(couchModel.getId());
                ic.getContainerProperty(object, bundle.getString("wavilon.table.phonenumbers.column.forward.calls.to")).setValue(forward);
                Button removeButton = new Button("", new Button.ClickListener() {
                    public void buttonClick(Button.ClickEvent event) {
                        phoneNumbersTable.select(object);
                        ConfirmingRemove confirmingRemove = new ConfirmingRemove(bundle);
                        getWindow().addWindow(confirmingRemove);
                        confirmingRemove.setNumbersLocator(phoneNumber.getLocator(), CouchTypes.service);
                        confirmingRemove.init(couchModel.getId(), phoneNumbersTable);
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

    private List<String> fillHiddenFields() {
        List<String> hiddenFields = new LinkedList<String>();

        hiddenFields.add(bundle.getString("wavilon.table.phonenumbers.column.number"));
        hiddenFields.add(bundle.getString("wavilon.table.phonenumbers.column.name"));
        hiddenFields.add("id");
        hiddenFields.add(bundle.getString("wavilon.table.phonenumbers.column.forward.calls.to"));
        hiddenFields.add("");

        return hiddenFields;
    }

    private List<String> fillFields() {
        List<String> tableFields = new LinkedList<String>();

        tableFields.add(bundle.getString("wavilon.table.phonenumbers.column.number"));
        tableFields.add(bundle.getString("wavilon.table.phonenumbers.column.name"));
        tableFields.add(bundle.getString("wavilon.table.phonenumbers.column.forward.calls.to"));
        tableFields.add("");

        return tableFields;
    }

    private List<CouchModel> getCouchModels() {
        List<CouchModel> couchModelList = new LinkedList<CouchModel>();

        try {
            couchModelList.addAll(service.getAllUsersCouchModelToPhoneNumber(CouchModelUtil.getOrganizationId(request)));
        } catch (Exception ignored) {
        }

        return couchModelList;
    }

}