package com.aimprosoft.wavilon.ui.menuitems;

import com.aimprosoft.wavilon.application.GenericPortletApplication;
import com.aimprosoft.wavilon.couch.CouchModel;
import com.aimprosoft.wavilon.couch.CouchModelLite;
import com.aimprosoft.wavilon.model.VirtualNumber;
import com.aimprosoft.wavilon.service.VirtualNumberDatabaseService;
import com.aimprosoft.wavilon.spring.ObjectFactory;
import com.aimprosoft.wavilon.ui.menuitems.forms.ConfirmingRemove;
import com.aimprosoft.wavilon.ui.menuitems.forms.VirtualNumbersForm;
import com.aimprosoft.wavilon.util.CouchModelUtil;
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
    private VirtualNumberDatabaseService service = ObjectFactory.getBean(VirtualNumberDatabaseService.class);

    public VirtualNumbersContent(ResourceBundle bundle) {
        this.bundle = bundle;
    }

    public void init() {
        request = ((GenericPortletApplication) getApplication()).getPortletRequest();

        this.hiddenFields = fillHiddenFields();
        this.tableFields = fillFields();
        this.tableData = createTableData();

        setSizeUndefined();
        initLayout();
        initVirtualNumbers();
    }

    private List<String> fillFields() {
        List<String> tableFields = new LinkedList<String>();

        tableFields.add(bundle.getString("wavilon.table.virtualnumbers.column.number"));
        tableFields.add(bundle.getString("wavilon.table.virtualnumbers.column.name"));
        tableFields.add(bundle.getString("wavilon.table.virtualnumbers.column.forward.calls.to"));
        tableFields.add("");

        return tableFields;
    }

    private void initLayout() {
        HorizontalLayout head = createHead();
        setWidth(100, Sizeable.UNITS_PERCENTAGE);
        addComponent(head);

        this.virtualNumbers.setColumnWidth("", 60);

        this.virtualNumbers.setColumnExpandRatio(bundle.getString("wavilon.table.virtualnumbers.column.number"), 1);
        this.virtualNumbers.setColumnExpandRatio(bundle.getString("wavilon.table.virtualnumbers.column.name"), 1);
        this.virtualNumbers.setColumnExpandRatio(bundle.getString("wavilon.table.virtualnumbers.column.forward.calls.to"), 1);

        this.virtualNumbers.setContainerDataSource(this.tableData);
        this.virtualNumbers.setWidth(100, Sizeable.UNITS_PERCENTAGE);
        this.virtualNumbers.setHeight("555px");
        this.virtualNumbers.addStyleName("tableCustom");
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
                VirtualNumber virtualNumber  = getVirtualNumber(couchModel);
                CouchModelLite forward = CouchModelUtil.getCouchModelLite((String) couchModel.getProperties().get("forward_to"));

                ic.getContainerProperty(object, bundle.getString("wavilon.table.virtualnumbers.column.number")).setValue(virtualNumber.getLocator());
                ic.getContainerProperty(object, bundle.getString("wavilon.table.virtualnumbers.column.name")).setValue(virtualNumber.getName());
                ic.getContainerProperty(object, "id").setValue(couchModel.getId());
                ic.getContainerProperty(object, bundle.getString("wavilon.table.virtualnumbers.column.forward.calls.to")).setValue(forward);
                ic.getContainerProperty(object, "").setValue(new Button("", new Button.ClickListener() {
                    public void buttonClick(Button.ClickEvent event) {
                        virtualNumbers.select(object);
                        ConfirmingRemove confirmingRemove = new ConfirmingRemove(bundle);
                        getWindow().addWindow(confirmingRemove);
                        confirmingRemove.init(couchModel.getId(), virtualNumbers);
                        confirmingRemove.center();
                        confirmingRemove.setModal(true);
                        confirmingRemove.setWidth("330px");
                        confirmingRemove.setHeight("180px");
                    }
                }));
            }
        }
        return ic;
    }

    private VirtualNumber getVirtualNumber(CouchModel couchModel) {
        try {
            return service.getVirtualNumber(couchModel);
        } catch (Exception ignored) {
            return new VirtualNumber();
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
        Label headLabel = new Label(bundle.getString("wavilon.menuitem.virtualnumbers"));
        head.addComponent(headLabel);
        head.setMargin(false);
        head.addStyleName("head");
        headLabel.addStyleName("label");

        HorizontalLayout addRemoveButtons = createButtons();
        head.addComponent(addRemoveButtons);

        head.setComponentAlignment(headLabel, Alignment.TOP_LEFT);
        head.setComponentAlignment(addRemoveButtons, Alignment.MIDDLE_RIGHT);

        return head;
    }

    private HorizontalLayout createButtons() {
        HorizontalLayout addButton = new HorizontalLayout();
        addButton.addComponent(new Button(bundle.getString("wavilon.button.add"), new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                getForm("-1", "-1");
            }
        }));
        return addButton;
    }

    private void getForm(String id, Object itemId) {
        VirtualNumbersForm virtualNumbersForm = new VirtualNumbersForm(this.bundle, this.virtualNumbers);
        virtualNumbersForm.setWidth("450px");
        virtualNumbersForm.setHeight("300px");
        virtualNumbersForm.center();
        virtualNumbersForm.setModal(true);

        getWindow().addWindow(virtualNumbersForm);
        virtualNumbersForm.init(id, itemId);
    }

    private List<String> fillHiddenFields() {
        List<String> hiddenFields = new LinkedList<String>();

        hiddenFields.add(bundle.getString("wavilon.table.virtualnumbers.column.number"));
        hiddenFields.add(bundle.getString("wavilon.table.virtualnumbers.column.name"));
        hiddenFields.add("id");
        hiddenFields.add(bundle.getString("wavilon.table.virtualnumbers.column.forward.calls.to"));
        hiddenFields.add("");

        return hiddenFields;
    }
}