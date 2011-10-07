package com.aimprosoft.wavilon.ui.menuitems;

import com.aimprosoft.wavilon.application.GenericPortletApplication;
import com.aimprosoft.wavilon.couch.CouchModel;
import com.aimprosoft.wavilon.model.Extension;
import com.aimprosoft.wavilon.service.CouchModelLiteDatabaseService;
import com.aimprosoft.wavilon.service.ExtensionDatabaseService;
import com.aimprosoft.wavilon.spring.ObjectFactory;
import com.aimprosoft.wavilon.ui.menuitems.forms.ConfirmingRemove;
import com.aimprosoft.wavilon.ui.menuitems.forms.ExtensionForm;
import com.liferay.portal.util.PortalUtil;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.*;

import javax.portlet.PortletRequest;
import java.util.*;

public class ExtensionContent extends VerticalLayout {
    private ResourceBundle bundle;
    private static PortletRequest request;
    private ExtensionDatabaseService extensionService = ObjectFactory.getBean(ExtensionDatabaseService.class);
    private CouchModelLiteDatabaseService modelLiteService = ObjectFactory.getBean(CouchModelLiteDatabaseService.class);
    private List<String> hiddenFields;

    private Table table = new Table();
    private List<String> tableFields;
    private IndexedContainer tableData;
    private Object item;

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

        HorizontalLayout addRemoveButtons = createButton();
        head.addComponent(addRemoveButtons);

        head.setComponentAlignment(headLabel, Alignment.TOP_LEFT);
        head.setComponentAlignment(addRemoveButtons, Alignment.MIDDLE_RIGHT);

        return head;
    }

    private HorizontalLayout createButton() {
        HorizontalLayout addButton = new HorizontalLayout();
        addButton.addComponent(new Button("Add", new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                getForm("-1");
            }
        }));
        return addButton;
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
                        getForm((String) event.getItem().getItemProperty("extensionId").getValue());
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

            for (final CouchModel couchModel : couchModels) {
                Extension extension = getExtension(couchModel);
                final Object object = ic.addItem();

                ic.getContainerProperty(object, "extensionId").setValue(couchModel.getId());
                ic.getContainerProperty(object, "ID").setValue(couchModel.getLiferayOrganizationId());
                ic.getContainerProperty(object, "NAME").setValue(extension.getName());
                ic.getContainerProperty(object, "EXTENSION TYPE").setValue(extension.getChannel());
                ic.getContainerProperty(object, "DESTINATION").setValue(extension.getDestination());
                ic.getContainerProperty(object, "").setValue(new Button("-", new Button.ClickListener() {
                    public void buttonClick(Button.ClickEvent event) {
                        table.select(object);
                        ConfirmingRemove confirmingRemove = new ConfirmingRemove(bundle);
                        getWindow().addWindow(confirmingRemove);
                        confirmingRemove.init(couchModel.getId(), table);
                        confirmingRemove.center();
                        confirmingRemove.setWidth("300px");
                        confirmingRemove.setHeight("180px");
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
            return extensionService.getAllUsersCouchModelToExtension(PortalUtil.getUserId(request), PortalUtil.getScopeGroupId(request));
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    private void getForm(String id) {
        ExtensionForm extensionForm = new ExtensionForm(bundle, this.table);
        extensionForm.setItem(this.item);
        extensionForm.setWidth("400px");
        extensionForm.setHeight("300px");
        extensionForm.center();
        extensionForm.setModal(true);

        getWindow().addWindow(extensionForm);
        extensionForm.init(id);
    }

    private List<String> fillHiddenFields() {
        LinkedList<String> tableFields = new LinkedList<String>();

        tableFields.add("extensionId");
        tableFields.add("ID");
        tableFields.add("NAME");
        tableFields.add("EXTENSION TYPE");
        tableFields.add("DESTINATION");
        tableFields.add("");

        return tableFields;
    }

    private LinkedList<String> fillFields() {
        LinkedList<String> tableFields = new LinkedList<String>();

        tableFields.add("ID");
        tableFields.add("NAME");
        tableFields.add("EXTENSION TYPE");
        tableFields.add("DESTINATION");
        tableFields.add("");

        return tableFields;
    }
}
