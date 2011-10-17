package com.aimprosoft.wavilon.ui.menuitems;

import com.aimprosoft.wavilon.application.GenericPortletApplication;
import com.aimprosoft.wavilon.couch.CouchModel;
import com.aimprosoft.wavilon.couch.CouchTypes;
import com.aimprosoft.wavilon.model.Extension;
import com.aimprosoft.wavilon.service.ExtensionDatabaseService;
import com.aimprosoft.wavilon.spring.ObjectFactory;
import com.aimprosoft.wavilon.ui.menuitems.forms.ConfirmingRemove;
import com.aimprosoft.wavilon.ui.menuitems.forms.ExtensionForm;
import com.aimprosoft.wavilon.util.CouchModelUtil;
import com.aimprosoft.wavilon.util.LayoutUtil;
import com.liferay.portal.util.PortalUtil;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.Button;
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
    private Object item;

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
        HorizontalLayout head = LayoutUtil.createHead(bundle, table, CouchTypes.extension, getWindow());
        setWidth(100, Sizeable.UNITS_PERCENTAGE);
        addComponent(head);

        table.setColumnWidth(bundle.getString("wavilon.table.extensions.column.id"), 60);
        table.setColumnWidth("", 60);

        table.setColumnExpandRatio(bundle.getString("wavilon.table.extensions.column.name"), 1);
        table.setColumnExpandRatio(bundle.getString("wavilon.table.extensions.column.extension.type"), 2);
        table.setColumnExpandRatio(bundle.getString("wavilon.table.extensions.column.destination"), 2);

        table.setContainerDataSource(tableData);
        table.setWidth(100, Sizeable.UNITS_PERCENTAGE);
        table.setHeight("555px");
        table.addStyleName("tableCustom");
        addComponent(table);
    }

    private void initExtension() {

        table.setContainerDataSource(tableData);
        table.setVisibleColumns(tableFields.toArray());
        table.setSelectable(true);
        table.setImmediate(true);

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
        table.addListener(new Property.ValueChangeListener() {
            public void valueChange(Property.ValueChangeEvent event) {
                if (null != event.getProperty().getValue()) {
                    item = event.getProperty().getValue();
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

            Map<String, String> extensionTypeMap = CouchModelUtil.extensionTypeMapEject(bundle);

            for (final CouchModel couchModel : couchModels) {
                Extension extension = getExtension(couchModel);
                final Object object = ic.addItem();

                ic.getContainerProperty(object, "extensionId").setValue(couchModel.getId());
                ic.getContainerProperty(object, bundle.getString("wavilon.table.extensions.column.id")).setValue(couchModel.getLiferayOrganizationId());
                ic.getContainerProperty(object, bundle.getString("wavilon.table.extensions.column.name")).setValue(extension.getName());
                ic.getContainerProperty(object, bundle.getString("wavilon.table.extensions.column.extension.type")).setValue(extensionTypeMap.get(extension.getChannel()));
                ic.getContainerProperty(object, bundle.getString("wavilon.table.extensions.column.destination")).setValue(extension.getDestination());
                ic.getContainerProperty(object, "").setValue(new Button("", new Button.ClickListener() {
                    public void buttonClick(Button.ClickEvent event) {
                        table.select(object);
                        ConfirmingRemove confirmingRemove = new ConfirmingRemove(bundle);
                        getWindow().addWindow(confirmingRemove);
                        confirmingRemove.init(couchModel.getId(), table);
                    }
                }));
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
            return extensionService.getAllUsersCouchModelToExtension(PortalUtil.getScopeGroupId(request));
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    private List<String> fillHiddenFields() {
        LinkedList<String> tableFields = new LinkedList<String>();

        tableFields.add("extensionId");
        tableFields.add(bundle.getString("wavilon.table.extensions.column.id"));
        tableFields.add(bundle.getString("wavilon.table.extensions.column.name"));
        tableFields.add(bundle.getString("wavilon.table.extensions.column.extension.type"));
        tableFields.add(bundle.getString("wavilon.table.extensions.column.destination"));
        tableFields.add("");

        return tableFields;
    }

    private LinkedList<String> fillFields() {
        LinkedList<String> tableFields = new LinkedList<String>();

        tableFields.add(bundle.getString("wavilon.table.extensions.column.id"));
        tableFields.add(bundle.getString("wavilon.table.extensions.column.name"));
        tableFields.add(bundle.getString("wavilon.table.extensions.column.extension.type"));
        tableFields.add(bundle.getString("wavilon.table.extensions.column.destination"));
        tableFields.add("");

        return tableFields;
    }
}
