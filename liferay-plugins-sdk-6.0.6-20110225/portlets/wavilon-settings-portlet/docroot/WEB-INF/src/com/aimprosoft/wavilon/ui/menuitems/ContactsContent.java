package com.aimprosoft.wavilon.ui.menuitems;

import com.aimprosoft.wavilon.couch.CouchModel;
import com.aimprosoft.wavilon.couch.CouchModelLite;
import com.aimprosoft.wavilon.couch.CouchTypes;
import com.aimprosoft.wavilon.model.PhoneNumber;
import com.aimprosoft.wavilon.service.ContactDatabaseService;
import com.aimprosoft.wavilon.service.impl.ContactDatabaseServiceImpl;
import com.aimprosoft.wavilon.spring.ObjectFactory;
import com.aimprosoft.wavilon.ui.menuitems.forms.PhoneNumbersForm;
import com.aimprosoft.wavilon.util.CouchModelUtil;
import com.aimprosoft.wavilon.util.LayoutUtil;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

import java.util.List;
import java.util.ResourceBundle;

public class ContactsContent extends GenerelContent {
    private ContactDatabaseService service = ObjectFactory.getBean(ContactDatabaseServiceImpl.class);

    public ContactsContent(ResourceBundle bundle) {
        super(bundle);
    }

    @Override
    public void init() {
        super.init();
        fillVisibleFields();
        fillHiddenFields();
        createTableData();

        initLayout(CouchTypes.contact);
    }

    private void fillVisibleFields() {
        visibleFields.add(bundle.getString("wavilon.table.contacts.column.name"));
        visibleFields.add(bundle.getString("wavilon.table.contacts.column.name.email"));
        visibleFields.add(bundle.getString("wavilon.table.contacts.column.name.phonenumber"));
        visibleFields.add("");
    }

    private void createTableData() {
        tableData = new IndexedContainer();
        List<CouchModel> couchModels = getCouchModels(service, CouchTypes.contact);

        LayoutUtil.addContainerProperties(hiddenFields, tableData);

        if (!couchModels.isEmpty()) {

            for (final CouchModel couchModel : couchModels) {
                final Object object = tableData.addItem();
                final PhoneNumber phoneNumber = getModel(couchModel, service, PhoneNumber.class);
                CouchModelLite forward = CouchModelUtil.getCouchModelLite((String) couchModel.getOutputs().get("startnode"), bundle);

                tableData.getContainerProperty(object, bundle.getString("wavilon.table.phonenumbers.column.number")).setValue(phoneNumber.getLocator());
                tableData.getContainerProperty(object, bundle.getString("wavilon.table.phonenumbers.column.name")).setValue(phoneNumber.getName());
                tableData.getContainerProperty(object, "id").setValue(couchModel.getId());
                tableData.getContainerProperty(object, bundle.getString("wavilon.table.phonenumbers.column.forward.calls.to")).setValue(forward);

                HorizontalLayout buttons = LayoutUtil.createTablesEditRemoveButtons(table, object, couchModel, bundle, phoneNumber.getLocator(), getWindow());
                tableData.getContainerProperty(object, "").setValue(buttons);
            }
        }
    }


}
