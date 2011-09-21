package com.aimprosoft.wavilon.ui.menuitems;

import com.aimprosoft.wavilon.model.Extension;
import com.aimprosoft.wavilon.service.ExtensionDatabaseService;
import com.aimprosoft.wavilon.spring.ObjectFactory;
import com.aimprosoft.wavilon.ui.menuitems.forms.ExtensionForm;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class ExtensionContent extends VerticalLayout {
    private ResourceBundle bundle;
    private static ExtensionDatabaseService extensionService = ObjectFactory.getBean(ExtensionDatabaseService.class);
    private List<String> hiddenFields;
    private HorizontalLayout main = new HorizontalLayout();
    private VerticalLayout left = new VerticalLayout();
    private VerticalLayout right = new VerticalLayout();

    private Table table = new Table();
    private List<String> tableFields;
    private IndexedContainer tableData;

    private HorizontalLayout bottomLeftCorner = new HorizontalLayout();

    public ExtensionContent(ResourceBundle bundle, String menuName) {
        this.bundle = bundle;
        tableFields = fillFields();
        hiddenFields = fillHiddenFields();
        tableData = createTableData();

        initLayout();
        initAddressList(menuName);

        main.setExpandRatio(left, 1);
        main.setExpandRatio(right, 3);
        main.setSizeUndefined();
    }

    private void initLayout() {
        main.setSizeFull();
        addComponent(main);
        main.addComponent(left);
        main.addComponent(right);

        table.setContainerDataSource(tableData);
        table.setHeight(330, Sizeable.UNITS_PIXELS);
        table.setWidth(200, Sizeable.UNITS_PIXELS);
        table.addStyleName("phoneNumbers");
        left.addComponent(table);

        bottomLeftCorner.setWidth(100, Sizeable.UNITS_PIXELS);
        left.addComponent(bottomLeftCorner);
        left.setComponentAlignment(bottomLeftCorner, Alignment.BOTTOM_LEFT);
    }

    private List<String> initAddressList(final String menuName) {
        Object[] col = {tableFields.get(0)};

        table.setContainerDataSource(tableData);
        table.setVisibleColumns(col);
        table.setSelectable(true);
        table.setImmediate(true);

        table.addListener(new Property.ValueChangeListener() {
            public void valueChange(Property.ValueChangeEvent event) {
                Object id = table.getValue();

                if (id != null) {
                    viewRightColumnContent(table.getItem(id), menuName);
                }
            }
        });

        return tableFields;
    }

    private LinkedList<String> fillFields() {
        LinkedList<String> tableFields = new LinkedList<String>();

        tableFields.add("firstName");
        tableFields.add("email");
        tableFields.add("sip");
        tableFields.add("phoneNumber");
        tableFields.add("extensionNumber");

        return tableFields;
    }

    private IndexedContainer createTableData() {
        IndexedContainer ic = new IndexedContainer();
        List<Extension> extensions = getExtension();

        for (String field : hiddenFields) {
            ic.addContainerProperty(field, String.class, "");
        }

        if (!extensions.isEmpty()) {

            for (Extension extension : extensions) {
                Object object = ic.addItem();
                ic.getContainerProperty(object, "firstName").setValue(extension.getFirstName());
                ic.getContainerProperty(object, "email").setValue(extension.getEmail());
                ic.getContainerProperty(object, "sipURL").setValue(extension.getSipURL());
                ic.getContainerProperty(object, "phoneNumber").setValue(extension.getPhoneNumber());
                ic.getContainerProperty(object, "extensionNumber").setValue(extension.getExtensionNumber());
                ic.getContainerProperty(object, "id").setValue(extension.getId());
            }
        }
        return ic;
    }

    private static List<Extension> getExtension() {
        try {
            return extensionService.getAllExtension();
        } catch (IOException e) {
            return null;
        }
    }

    private void viewRightColumnContent(Object id, String menuName) {
        Item item = id == null ? null : (Item) id;

        right.removeAllComponents();
        right.addStyleName("formRegion");
         right.setMargin(false, true, false, true);
        right.addComponent(new ExtensionForm(bundle, item, right, table, menuName));
    }

    private List<String> fillHiddenFields() {
        LinkedList<String> tableFields = new LinkedList<String>();

        tableFields.add("id");
        tableFields.add("firstName");
        tableFields.add("email");
        tableFields.add("sipURL");
        tableFields.add("phoneNumber");
        tableFields.add("extensionNumber");

        return tableFields;
    }
}
