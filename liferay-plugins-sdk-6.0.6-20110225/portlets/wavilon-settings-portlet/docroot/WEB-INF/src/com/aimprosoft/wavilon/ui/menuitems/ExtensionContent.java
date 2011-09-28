package com.aimprosoft.wavilon.ui.menuitems;

import com.aimprosoft.wavilon.application.GenericPortletApplication;
import com.aimprosoft.wavilon.model.Extension;
import com.aimprosoft.wavilon.service.ExtensionDatabaseService;
import com.aimprosoft.wavilon.spring.ObjectFactory;
import com.aimprosoft.wavilon.ui.menuitems.forms.ConfirmingRemove;
import com.aimprosoft.wavilon.ui.menuitems.forms.ExtensionForm;
import com.liferay.portal.util.PortalUtil;
import com.vaadin.data.Property;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.*;

import javax.portlet.PortletRequest;
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

        setHeight(100, Sizeable.UNITS_PERCENTAGE);
        setWidth(100, Sizeable.UNITS_PERCENTAGE);
        setSizeUndefined();
        initLayout();
        initExtension();
    }

    private void initLayout() {
        HorizontalLayout head = createHead();
        setWidth(100, Sizeable.UNITS_PERCENTAGE);
        addComponent(head);

        table.setContainerDataSource(tableData);
        table.setWidth(100, Sizeable.UNITS_PERCENTAGE);
        
        table.addStyleName("tableCustom");
        addComponent(table);
    }

    private HorizontalLayout createHead() {
       HorizontalLayout head = new HorizontalLayout();
        head.setWidth(100, Sizeable.UNITS_PERCENTAGE);

        Label headLabel = new Label("Extensions");
        head.addComponent(headLabel);
        head.setMargin(false);
        head.addStyleName("headLine");
        headLabel.addStyleName("tableHeader");
        headLabel.addStyleName("extensionHeader");

        HorizontalLayout addRemoveButtons = createButtons();
        head.addComponent(addRemoveButtons);

        head.setComponentAlignment(headLabel, Alignment.TOP_LEFT);
        head.setComponentAlignment(addRemoveButtons, Alignment.MIDDLE_RIGHT);

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
                    String extensionID = (String) table.getItem(id).getItemProperty("extensionIdBase").getValue();

                    ConfirmingRemove confirmingRemove = new ConfirmingRemove(bundle);
                    getWindow().addWindow(confirmingRemove);
                    confirmingRemove.init(extensionID, table);
                    confirmingRemove.center();
                    confirmingRemove.setWidth("420px");
                    confirmingRemove.setHeight("180px");
                } else {
                    getWindow().showNotification("Select Extension]");
                }
            }
        }));
        return addRemoveButtons;
    }

    private List<String> initExtension() {

        List<String> col = hiddenFields;
        col.remove(4);

        table.setContainerDataSource(tableData);
        table.setVisibleColumns(col.toArray());
        table.setSelectable(true);
        table.setImmediate(true);

        table.addListener(new Property.ValueChangeListener() {
            public void valueChange(Property.ValueChangeEvent event) {
                if (extensionForm != null && extensionForm.getParent() != null) {
                    getWindow().showNotification("Form is already open");
                } else {
                    Object id = table.getValue();
                    if (null != id) {
                        getForm((String) table.getItem(id).getItemProperty("extensionIdBase").getValue());
                    }
                }
            }
        });

        return tableFields;
    }

    private LinkedList<String> fillFields() {
        LinkedList<String> tableFields = new LinkedList<String>();

        tableFields.add("id");
        tableFields.add("name");
        tableFields.add("extension type");
        tableFields.add("destination");
        tableFields.add("extensionIdBase");

        return tableFields;
    }

    private IndexedContainer createTableData() {
        IndexedContainer ic = new IndexedContainer();
        List<Extension> extensions = getExtension();

        for (String field : hiddenFields) {
            if ("id".equals(field)) {
                ic.addContainerProperty(field, Integer.class, "");

            }else {
                ic.addContainerProperty(field, String.class, "");
            }
        }

        if (!extensions.isEmpty()) {

            for (Extension extension : extensions) {
                Object object = ic.addItem();
                ic.getContainerProperty(object, "extensionIdBase").setValue(extension.getId());
                ic.getContainerProperty(object, "id").setValue(extension.getLiferayOrganizationId());
                ic.getContainerProperty(object, "name").setValue(extension.getName());
                ic.getContainerProperty(object, "extension type").setValue(extension.getExtensionType());

                String extensionType = extension.getExtensionType();

                if ("Phone number".equals(extensionType)) {
                    ic.getContainerProperty(object, "destination").setValue(extension.getPhoneNumber());

                }else if ("SIP".equals(extensionType)) {
                    ic.getContainerProperty(object, "destination").setValue(extension.getSipURL());

                }else if ("Gtalk".equals(extensionType)) {
                    ic.getContainerProperty(object, "destination").setValue(extension.getgTalk());
                }
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
        tableFields.add("name");
        tableFields.add("extension type");
        tableFields.add("destination");
        tableFields.add("extensionIdBase");

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
