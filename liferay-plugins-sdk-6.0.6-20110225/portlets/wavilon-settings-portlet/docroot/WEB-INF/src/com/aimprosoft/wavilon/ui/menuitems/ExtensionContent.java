package com.aimprosoft.wavilon.ui.menuitems;

import com.aimprosoft.wavilon.application.GenericPortletApplication;
import com.aimprosoft.wavilon.model.Extension;
import com.aimprosoft.wavilon.service.ExtensionDatabaseService;
import com.aimprosoft.wavilon.spring.ObjectFactory;
import com.aimprosoft.wavilon.ui.menuitems.forms.ExtensionForm;
import com.liferay.portal.util.PortalUtil;
import com.vaadin.data.Property;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.*;

import javax.portlet.PortletRequest;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class ExtensionContent extends VerticalLayout {
    private ResourceBundle bundle;
    private static PortletRequest request;
    private ExtensionDatabaseService extensionService = ObjectFactory.getBean(ExtensionDatabaseService.class);
    private List<String> hiddenFields;
    private ExtensionForm extensionForm;

    private Table table = new Table();
    private List<String> tableFields;
    private IndexedContainer tableData;

    public ExtensionContent(ResourceBundle bundle) {
        this.bundle = bundle;
    }

    public void init() {
        request = ((GenericPortletApplication) getApplication()).getPortletRequest();
        tableFields = fillFields();
        hiddenFields = fillHiddenFields();
        tableData = createTableData();

        setHeight(100.0F, 8);
        setWidth(100.0F, 8);
        setSizeUndefined();
        initLayout();
        initExtension();
    }

    private void initLayout() {
        HorizontalLayout head = createHead();
        setWidth(100.0F, 8);
        addComponent(head);

        table.setContainerDataSource(tableData);
        table.setWidth(100.0F, 8);
        table.setStyleName("phoneNumbers");
        addComponent(table);
    }

    private HorizontalLayout createHead() {
       HorizontalLayout head = new HorizontalLayout();
        head.setWidth(100.0F, 8);

        Label headLabel = new Label("Extensions");
        head.addComponent(headLabel);
        head.setMargin(true);
        head.addStyleName("headLine");
        headLabel.addStyleName("phoneHeader");

        HorizontalLayout addRemoveButtons = createButtons();
        head.addComponent(addRemoveButtons);

        head.setComponentAlignment(headLabel, Alignment.TOP_LEFT);
        head.setComponentAlignment(addRemoveButtons, Alignment.TOP_RIGHT);

        return head;
    }

    private HorizontalLayout createButtons() {
        HorizontalLayout addRemoveButtons = new HorizontalLayout();
        addRemoveButtons.addComponent(new Button("+", new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                getForm("-1");
            }
        }));
        addRemoveButtons.addComponent(new Button("-", new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                Object id = table.getValue();
                if (null != id) {
                    String extensionID = (String) table.getItem(id).getItemProperty("id").getValue();
                    try {
                        extensionService.removeExtension(extensionID);
                    } catch (IOException ignored) {
                    }
                    table.removeItem(table.getValue());
                    table.select(null);
                } else {
                    getWindow().showNotification("Select Extension");
                }
            }
        }));
        return addRemoveButtons;
    }

    private List<String> initExtension() {
        Object[] col = {tableFields.get(0)};

        table.setContainerDataSource(tableData);
        table.setVisibleColumns(col);
        table.setSelectable(true);
        table.setImmediate(true);

        table.addListener(new Property.ValueChangeListener() {
            public void valueChange(Property.ValueChangeEvent event) {
                if (extensionForm != null && extensionForm.getParent() != null) {
                    getWindow().showNotification("Form is already open");
                } else {
                    Object id = table.getValue();
                    if (null != id) {
                        getForm((String) table.getItem(id).getItemProperty("id").getValue());
                    }
                }
            }
        });

        return tableFields;
    }

    private LinkedList<String> fillFields() {
        LinkedList<String> tableFields = new LinkedList<String>();

        tableFields.add("");

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
                ic.getContainerProperty(object, "").setValue(extension.getFirstName());
                ic.getContainerProperty(object, "id").setValue(extension.getId());
            }
        }
        return ic;
    }

    private List<Extension> getExtension() {
        try {
            return extensionService.getAllExtensionByUserId(PortalUtil.getUserId(request), PortalUtil.getScopeGroupId(request));
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    private List<String> fillHiddenFields() {
        LinkedList<String> tableFields = new LinkedList<String>();

        tableFields.add("id");
        tableFields.add("");

        return tableFields;
    }

    private void getForm(String id) {
        extensionForm = new ExtensionForm(bundle, table);
        extensionForm.setWidth("400px");
        extensionForm.setHeight("300px");
        extensionForm.center();
        extensionForm.setModal(true);

        getWindow().addWindow(extensionForm);
        extensionForm.init(id);
    }
}
