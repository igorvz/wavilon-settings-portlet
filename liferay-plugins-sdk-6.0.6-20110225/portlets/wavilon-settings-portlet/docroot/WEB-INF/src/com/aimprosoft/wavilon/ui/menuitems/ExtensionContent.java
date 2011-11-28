package com.aimprosoft.wavilon.ui.menuitems;

import com.aimprosoft.wavilon.application.GenericPortletApplication;
import com.aimprosoft.wavilon.couch.CouchModel;
import com.aimprosoft.wavilon.couch.CouchTypes;
import com.aimprosoft.wavilon.model.Extension;
import com.aimprosoft.wavilon.service.ExtensionDatabaseService;
import com.aimprosoft.wavilon.spring.ObjectFactory;
import com.aimprosoft.wavilon.ui.menuitems.forms.ExtensionForm;
import com.aimprosoft.wavilon.util.CouchModelUtil;
import com.aimprosoft.wavilon.util.LayoutUtil;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

import javax.portlet.PortletRequest;
import java.util.*;

public class ExtensionContent extends VerticalLayout {
    private ResourceBundle bundle;
    private static PortletRequest request;
    private ExtensionDatabaseService extensionService = ObjectFactory.getBean(ExtensionDatabaseService.class);
    private List<String> hiddenFields;
    private Table table = new Table();
    private List<String> tableFields;
    private IndexedContainer tableData;

    public ExtensionContent(ResourceBundle bundle) {
        this.bundle = bundle;
    }

    public void init() {
        removeAllComponents();
        request = ((GenericPortletApplication) getApplication()).getPortletRequest();
        tableFields = fillFields();
        hiddenFields = fillHiddenFields();
        tableData = createTableData();

        setSizeUndefined();
        initLayout();
        initExtension();
    }

    private void initLayout() {
        VerticalLayout head = LayoutUtil.createHead(bundle, table, CouchTypes.extension, getWindow());
        setWidth(100, Sizeable.UNITS_PERCENTAGE);
        addComponent(head);

        table.setContainerDataSource(tableData);
        table.setHeight("555px");
        table.addStyleName("tableCustom");
        addComponent(table);
    }

    private void initExtension() {
        table.setContainerDataSource(tableData);
        table.setVisibleColumns(tableFields.toArray());
        table.setSelectable(true);
        table.setImmediate(true);
        LayoutUtil.setTableWidth(table, CouchTypes.extension, new HashMap<String, Integer>() {{
            put(bundle.getString("wavilon.table.extensions.column.extension.type"), 2);
            put(bundle.getString("wavilon.table.extensions.column.destination"), 2);
        }});
        table.setColumnWidth(bundle.getString("wavilon.table.extensions.column.code"), 90);

        table.addListener(new ItemClickEvent.ItemClickListener() {
            public void itemClick(ItemClickEvent event) {
                if (event.isDoubleClick()) {
                    Item item = event.getItem();
                    if (null != item) {
                        LayoutUtil.getForm((String) event.getItem().getItemProperty("extensionId").getValue(), event.getItemId(), getWindow(), new ExtensionForm(bundle, table));
                    }
                }
            }
        });
    }

    private IndexedContainer createTableData() {
        IndexedContainer ic = new IndexedContainer();
        List<CouchModel> couchModels = getCouchModels();

        LayoutUtil.addContainerProperties(hiddenFields, ic);

        if (!couchModels.isEmpty()) {

            Map<String, String> extensionTypeMap = CouchModelUtil.extensionTypeMapEject(bundle);

            for (final CouchModel couchModel : couchModels) {
                Extension extension = getExtension(couchModel);
                final Object object = ic.addItem();

                ic.getContainerProperty(object, "extensionId").setValue(couchModel.getId());
                ic.getContainerProperty(object, bundle.getString("wavilon.table.extensions.column.code")).setValue(extension.getCode());
                ic.getContainerProperty(object, bundle.getString("wavilon.table.extensions.column.name")).setValue(extension.getName());
                ic.getContainerProperty(object, bundle.getString("wavilon.table.extensions.column.extension.type")).setValue(extensionTypeMap.get(extension.getChannel()));
                ic.getContainerProperty(object, bundle.getString("wavilon.table.extensions.column.destination")).setValue(extension.getDestination());

                HorizontalLayout buttons = LayoutUtil.createTablesEditRemoveButtons(table, object, couchModel, bundle, null, getWindow(), new ExtensionForm(bundle, table));
                ic.getContainerProperty(object, "").setValue(buttons);
            }
        }
        return ic;
    }

    private Extension getExtension(CouchModel couchModel) {
        try {
            return extensionService.getExtension(couchModel);
        } catch (Exception e) {
            return new Extension();
        }
    }

    private List<CouchModel> getCouchModels() {
        try {
            return extensionService.getAllUsersCouchModelToExtension(CouchModelUtil.getOrganizationId(request));
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    private List<String> fillHiddenFields() {
        LinkedList<String> tableFields = new LinkedList<String>();

        tableFields.add("extensionId");
        tableFields.add(bundle.getString("wavilon.table.extensions.column.code"));
        tableFields.add(bundle.getString("wavilon.table.extensions.column.name"));
        tableFields.add(bundle.getString("wavilon.table.extensions.column.extension.type"));
        tableFields.add(bundle.getString("wavilon.table.extensions.column.destination"));
        tableFields.add("");

        return tableFields;
    }

    private LinkedList<String> fillFields() {
        LinkedList<String> tableFields = new LinkedList<String>();

        tableFields.add(bundle.getString("wavilon.table.extensions.column.code"));
        tableFields.add(bundle.getString("wavilon.table.extensions.column.name"));
        tableFields.add(bundle.getString("wavilon.table.extensions.column.extension.type"));
        tableFields.add(bundle.getString("wavilon.table.extensions.column.destination"));
        tableFields.add("");

        return tableFields;
    }
}
