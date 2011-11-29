package com.aimprosoft.wavilon.ui.menuitems;

import com.aimprosoft.wavilon.couch.CouchTypes;
import com.aimprosoft.wavilon.service.ContactDatabaseService;
import com.aimprosoft.wavilon.service.impl.ContactDatabaseServiceImpl;
import com.aimprosoft.wavilon.spring.ObjectFactory;

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
        createTableData(service, CouchTypes.contact);
        fillTable();
        initLayout(CouchTypes.contact);

    }

    private void fillVisibleFields() {
        visibleFields.add(bundle.getString("wavilon.table.contacts.column.name"));
        visibleFields.add(bundle.getString("wavilon.table.contacts.column.name.email"));
        visibleFields.add(bundle.getString("wavilon.table.contacts.column.name.phonenumber"));
    }

    private void fillTable() {

    }


}
