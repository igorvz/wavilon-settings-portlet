package com.aimprosoft.wavilon.ui.menuitems;

import com.aimprosoft.wavilon.couch.CouchModel;
import com.aimprosoft.wavilon.couch.CouchModelLite;
import com.aimprosoft.wavilon.couch.CouchTypes;
import com.aimprosoft.wavilon.model.PhoneNumber;
import com.aimprosoft.wavilon.service.PhoneNumberDatabaseService;
import com.aimprosoft.wavilon.spring.ObjectFactory;
import com.aimprosoft.wavilon.util.CouchModelUtil;

import java.util.ResourceBundle;

public class PhoneNumbersContent extends GenerelContent {
    private PhoneNumberDatabaseService service = ObjectFactory.getBean(PhoneNumberDatabaseService.class);

    public PhoneNumbersContent(ResourceBundle bundle) {
        super(bundle);
    }

    public void init() {
        super.init();
        fillVisibleFields();
        fillHiddenFields();
        createTableData(service, CouchTypes.service);
        fillTable();
        initLayout(CouchTypes.service, ratioMap(1,2,2));
    }

    private void fillVisibleFields() {
        visibleFields.add(bundle.getString("wavilon.table.phonenumbers.column.number"));
        visibleFields.add(bundle.getString("wavilon.table.phonenumbers.column.name"));
        visibleFields.add(bundle.getString("wavilon.table.phonenumbers.column.forward.calls.to"));
    }

    private void fillTable() {
        if (!couchModels.isEmpty()) {
            for (CouchModel couchModel : couchModels) {
                Object object = tableData.addItem();
                PhoneNumber phoneNumber = getModel(couchModel, service, PhoneNumber.class);
                CouchModelLite forward = CouchModelUtil.getCouchModelLite((String) couchModel.getOutputs().get("startnode"), bundle);

                tableData.getContainerProperty(object, "id").setValue(couchModel.getId());
                tableData.getContainerProperty(object, bundle.getString("wavilon.table.phonenumbers.column.number")).setValue(phoneNumber.getLocator());
                tableData.getContainerProperty(object, bundle.getString("wavilon.table.phonenumbers.column.name")).setValue(phoneNumber.getName());
                tableData.getContainerProperty(object, bundle.getString("wavilon.table.phonenumbers.column.forward.calls.to")).setValue(forward);
                tableData.getContainerProperty(object, "").setValue(createTablesEditRemoveButtons(object, couchModel, phoneNumber.getLocator()));
            }
        }
    }

}