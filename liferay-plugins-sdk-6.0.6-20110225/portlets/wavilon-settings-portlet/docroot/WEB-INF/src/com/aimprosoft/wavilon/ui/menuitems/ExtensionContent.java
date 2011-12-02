package com.aimprosoft.wavilon.ui.menuitems;

import com.aimprosoft.wavilon.couch.CouchModel;
import com.aimprosoft.wavilon.couch.CouchTypes;
import com.aimprosoft.wavilon.model.Extension;
import com.aimprosoft.wavilon.service.ExtensionDatabaseService;
import com.aimprosoft.wavilon.spring.ObjectFactory;
import com.aimprosoft.wavilon.util.CouchModelUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class ExtensionContent extends GenerelContent {
    private ExtensionDatabaseService service = ObjectFactory.getBean(ExtensionDatabaseService.class);

    public ExtensionContent(ResourceBundle bundle) {
        super(bundle);
    }

    public void init() {
        super.init();
        fillVisibleFields();
        fillHiddenFields();
        createTableData(service, CouchTypes.extension);
        fillTable();
        initLayout(CouchTypes.extension, new HashMap<String, Integer>() {{
            put(bundle.getString("wavilon.table.extensions.column.extension.type"), 2);
            put(bundle.getString("wavilon.table.extensions.column.destination"), 2);
        }});
        table.setColumnWidth(bundle.getString("wavilon.table.extensions.column.code"), 90);
    }

    private void fillVisibleFields() {
        visibleFields.add(bundle.getString("wavilon.table.extensions.column.code"));
        visibleFields.add(bundle.getString("wavilon.table.extensions.column.name"));
        visibleFields.add(bundle.getString("wavilon.table.extensions.column.extension.type"));
        visibleFields.add(bundle.getString("wavilon.table.extensions.column.destination"));
    }

    private void fillTable() {
        if (!couchModels.isEmpty()) {
            Map<String, String> extensionTypeMap = CouchModelUtil.extensionTypeMapEject(bundle);

            for (final CouchModel couchModel : couchModels) {
                Extension extension = getModel(couchModel, service, Extension.class);
                final Object object = tableData.addItem();

                tableData.getContainerProperty(object, "id").setValue(couchModel.getId());
                tableData.getContainerProperty(object, bundle.getString("wavilon.table.extensions.column.code")).setValue(extension.getCode());
                tableData.getContainerProperty(object, bundle.getString("wavilon.table.extensions.column.name")).setValue(extension.getName());
                tableData.getContainerProperty(object, bundle.getString("wavilon.table.extensions.column.extension.type")).setValue(extensionTypeMap.get(extension.getChannel()));
                tableData.getContainerProperty(object, bundle.getString("wavilon.table.extensions.column.destination")).setValue(extension.getDestination());
                tableData.getContainerProperty(object, "").setValue(createTablesEditRemoveButtons(object, couchModel, null));
            }
        }
    }
}
