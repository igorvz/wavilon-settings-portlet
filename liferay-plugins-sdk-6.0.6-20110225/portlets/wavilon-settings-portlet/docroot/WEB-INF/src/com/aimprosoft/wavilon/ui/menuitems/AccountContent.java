package com.aimprosoft.wavilon.ui.menuitems;

import com.aimprosoft.wavilon.couch.CouchTypes;
import com.aimprosoft.wavilon.service.AccountDatabaseService;
import com.aimprosoft.wavilon.service.ContactDatabaseService;
import com.aimprosoft.wavilon.service.impl.AccountDatabaseServiceImpl;
import com.aimprosoft.wavilon.service.impl.ContactDatabaseServiceImpl;
import com.aimprosoft.wavilon.spring.ObjectFactory;

import java.util.ResourceBundle;

public class AccountContent extends GenerelContent {
    private AccountDatabaseService service = ObjectFactory.getBean(AccountDatabaseServiceImpl.class);

    public AccountContent(ResourceBundle bundle) {
        super(bundle);
    }

    @Override
    public void init() {
        super.init();
        fillVisibleFields();
        fillHiddenFields();
        createTableData(service, CouchTypes.account);
        fillTable();
        initLayout(CouchTypes.account);

    }

    private void fillVisibleFields() {
        visibleFields.add(bundle.getString("wavilon.table.contacts.column.name"));
        visibleFields.add(bundle.getString("wavilon.table.contacts.column.name.email"));
        visibleFields.add(bundle.getString("wavilon.table.contacts.column.name.phonenumber"));
    }

    private void fillTable() {

    }


}
