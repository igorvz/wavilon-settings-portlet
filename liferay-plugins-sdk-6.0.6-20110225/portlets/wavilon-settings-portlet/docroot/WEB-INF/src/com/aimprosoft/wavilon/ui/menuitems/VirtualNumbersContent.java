package com.aimprosoft.wavilon.ui.menuitems;

import com.aimprosoft.wavilon.application.GenericPortletApplication;
import com.aimprosoft.wavilon.couch.CouchModel;
import com.aimprosoft.wavilon.couch.CouchModelLite;
import com.aimprosoft.wavilon.service.CouchModelLiteDatabaseService;
import com.aimprosoft.wavilon.service.VirtualNumberDatabaseService;
import com.aimprosoft.wavilon.spring.ObjectFactory;
import com.aimprosoft.wavilon.ui.menuitems.forms.ConfirmingRemove;
import com.aimprosoft.wavilon.ui.menuitems.forms.VirtualNumbersForm;
import com.liferay.portal.util.PortalUtil;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.*;

import javax.portlet.PortletRequest;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class VirtualNumbersContent extends VerticalLayout {
    private ResourceBundle bundle;

    private IndexedContainer tableData;
    private List<String> tableFields;
    private List<String> hiddenFields;
    private Table virtualNumbers = new Table();

    private PortletRequest request;
    private VirtualNumberDatabaseService service = (VirtualNumberDatabaseService) ObjectFactory.getBean(VirtualNumberDatabaseService.class);
    private CouchModelLiteDatabaseService liteService = ObjectFactory.getBean(CouchModelLiteDatabaseService.class);

    public VirtualNumbersContent(ResourceBundle bundle) {
        this.bundle = bundle;
    }

    public void init() {
        request = ((GenericPortletApplication) getApplication()).getPortletRequest();

        this.hiddenFields = fillHiddenFields();
        this.tableFields = fillFields();
        this.tableData = createTableData();

        setHeight(100, Sizeable.UNITS_PERCENTAGE);
        setWidth(100, Sizeable.UNITS_PERCENTAGE);
        setSizeUndefined();
        initLayout();
        initVirtualNumbers();
    }

    private List<String> fillFields() {
        List<String> tableFields = new LinkedList<String>();

        tableFields.add("NUMBER");
        tableFields.add("NAME");
        tableFields.add("FORWARD CALLS TO");
        tableFields.add("");

        return tableFields;
    }

    private void initLayout() {
        HorizontalLayout head = createHead();
        setWidth(100, Sizeable.UNITS_PERCENTAGE);
        addComponent(head);

        this.virtualNumbers.setContainerDataSource(this.tableData);
        this.virtualNumbers.setWidth(100, Sizeable.UNITS_PERCENTAGE);
        this.virtualNumbers.setStyleName("virtualNumbers");
        this.virtualNumbers.setStyleName("tableCustom");
        addComponent(this.virtualNumbers);
    }

    private void initVirtualNumbers() {
        this.virtualNumbers.setVisibleColumns(this.tableFields.toArray());
        this.virtualNumbers.setSelectable(true);
        this.virtualNumbers.setImmediate(true);

        this.virtualNumbers.addListener(new ItemClickEvent.ItemClickListener() {
            public void itemClick(ItemClickEvent event) {
                if (event.isDoubleClick()) {
                    Item item = event.getItem();
                    if (null != item) {
                        getForm((String) event.getItem().getItemProperty("id").getValue(), event.getItemId());
                    }
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
                final Object object = ic.addItem();

                CouchModelLite extensionModel = getExtension((String) couchModel.getOutputs().get("extension"));

                ic.getContainerProperty(object, "NUMBER").setValue(couchModel.getProperties().get("locator"));
                ic.getContainerProperty(object, "NAME").setValue(couchModel.getProperties().get("name"));
                ic.getContainerProperty(object, "id").setValue(couchModel.getId());
                ic.getContainerProperty(object, "FORWARD CALLS TO").setValue(extensionModel.getName());
                ic.getContainerProperty(object, "").setValue(new Button("-", new Button.ClickListener() {
                    public void buttonClick(Button.ClickEvent event) {
                        virtualNumbers.select(object);
                        ConfirmingRemove confirmingRemove = new ConfirmingRemove(bundle);
                        getWindow().addWindow(confirmingRemove);
                        confirmingRemove.init(couchModel.getId(), virtualNumbers);
                        confirmingRemove.center();
                        confirmingRemove.setWidth("300px");
                        confirmingRemove.setHeight("180px");
                    }
                }));
            }
        }
        return ic;
    }

    private CouchModelLite getExtension(String id) {
        try {
            return liteService.getCouchLiteModel(id);
        } catch (Exception e) {
            return (CouchModelLite) Collections.emptyList();
        }
    }

    private List<CouchModel> getCouchModels() {
        try {
            return service.getAllUsersCouchModelToVirtualNumber(PortalUtil.getUserId(request), PortalUtil.getScopeGroupId(request));
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public HorizontalLayout createHead() {
        HorizontalLayout head = new HorizontalLayout();
        head.setWidth(100, Sizeable.UNITS_PERCENTAGE);
        Label headLabel = new Label("Virtual Numbers");
        head.addComponent(headLabel);
        head.setMargin(false);
        head.addStyleName("headLine");
        headLabel.addStyleName("tableHeader");
        headLabel.addStyleName("virtualHeader");

        HorizontalLayout addRemoveButtons = createButtons();
        head.addComponent(addRemoveButtons);

        head.setComponentAlignment(headLabel, Alignment.TOP_LEFT);
        head.setComponentAlignment(addRemoveButtons, Alignment.MIDDLE_RIGHT);

        return head;
    }

    private HorizontalLayout createButtons() {
        HorizontalLayout addButton = new HorizontalLayout();
        addButton.addComponent(new Button("Add", new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                VirtualNumbersContent.this.getForm("-1", "-1");
            }
        }));
        return addButton;
    }

    private void getForm(String id, Object itemId) {
        VirtualNumbersForm virtualNumbersForm = new VirtualNumbersForm(this.bundle, this.virtualNumbers);
        virtualNumbersForm.setWidth("400px");
        virtualNumbersForm.setHeight("300px");
        virtualNumbersForm.center();
        virtualNumbersForm.setModal(true);

        getWindow().addWindow(virtualNumbersForm);
        virtualNumbersForm.init(id, itemId);
    }

    private List<String> fillHiddenFields() {
        List<String> hiddenFields = new LinkedList<String>();

        hiddenFields.add("NUMBER");
        hiddenFields.add("NAME");
        hiddenFields.add("id");
        hiddenFields.add("FORWARD CALLS TO");
        hiddenFields.add("");

        return hiddenFields;
    }
}