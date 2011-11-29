package com.aimprosoft.wavilon.ui.menuitems;

import com.aimprosoft.wavilon.couch.CouchModel;
import com.aimprosoft.wavilon.couch.CouchModelLite;
import com.aimprosoft.wavilon.couch.CouchTypes;
import com.aimprosoft.wavilon.model.VirtualNumber;
import com.aimprosoft.wavilon.service.VirtualNumberDatabaseService;
import com.aimprosoft.wavilon.spring.ObjectFactory;
import com.aimprosoft.wavilon.util.CouchModelUtil;

import java.util.ResourceBundle;

public class VirtualNumbersContent extends GenerelContent {
    private VirtualNumberDatabaseService service = ObjectFactory.getBean(VirtualNumberDatabaseService.class);

    public VirtualNumbersContent(ResourceBundle bundle) {
        super(bundle);
    }

    public void init() {
        super.init();
        fillVisibleFields();
        fillHiddenFields();
        createTableData(service, CouchTypes.startnode);
        fillTable();
        initLayout(CouchTypes.startnode, ratioMap(1,2,2));
    }

    private void fillVisibleFields() {
        visibleFields.add(bundle.getString("wavilon.table.virtualnumbers.column.number"));
        visibleFields.add(bundle.getString("wavilon.table.virtualnumbers.column.name"));
        visibleFields.add(bundle.getString("wavilon.table.virtualnumbers.column.forward.calls.to"));
    }

    private void fillTable() {
        if (!couchModels.isEmpty()) {
            for (CouchModel couchModel : couchModels) {
                Object object = tableData.addItem();
                VirtualNumber virtualNumber = getModel(couchModel, service, VirtualNumber.class);
                CouchModelLite forward = CouchModelUtil.getCouchModelLite((String) couchModel.getProperties().get("forward_to"), bundle);

                tableData.getContainerProperty(object, "id").setValue(couchModel.getId());
                tableData.getContainerProperty(object, bundle.getString("wavilon.table.virtualnumbers.column.number")).setValue(virtualNumber.getLocator());
                tableData.getContainerProperty(object, bundle.getString("wavilon.table.virtualnumbers.column.name")).setValue(virtualNumber.getName());
                tableData.getContainerProperty(object, bundle.getString("wavilon.table.virtualnumbers.column.forward.calls.to")).setValue(forward);
                tableData.getContainerProperty(object, "").setValue(createTablesEditRemoveButtons(object, couchModel, virtualNumber.getLocator()));
            }
        }
    }
}