package com.aimprosoft.wavilon.ui.menuitems;

import com.aimprosoft.wavilon.application.GenericPortletApplication;
import com.aimprosoft.wavilon.model.VirtualNumber;
import com.aimprosoft.wavilon.service.VirtualNumberDatabaseService;
import com.aimprosoft.wavilon.spring.ObjectFactory;
import com.aimprosoft.wavilon.ui.menuitems.forms.VirtualNumbersForm;
import com.liferay.portal.util.PortalUtil;
import com.vaadin.data.Property;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.*;

import javax.portlet.PortletRequest;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class VirtualNumbersContent extends VerticalLayout {
    private IndexedContainer tableData;
    private List<String> tableFields;
    private ResourceBundle bundle;
    private Table virtualNumbers = new Table();
    private PortletRequest request;
    private VirtualNumberDatabaseService service = (VirtualNumberDatabaseService) ObjectFactory.getBean(VirtualNumberDatabaseService.class);
    private List<String> hiddenFields;
    private String column = "";
    private VirtualNumbersForm virtualNumbersForm;

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

        tableFields.add(this.column);

        return tableFields;
    }

    private void initLayout() {
        HorizontalLayout head = createHead();
        setWidth(100, Sizeable.UNITS_PERCENTAGE);
        addComponent(head);

        this.virtualNumbers.setContainerDataSource(this.tableData);
        this.virtualNumbers.setWidth(100, Sizeable.UNITS_PERCENTAGE);
        this.virtualNumbers.setStyleName("virtualNumbers");
        addComponent(this.virtualNumbers);
    }

    private List<String> initVirtualNumbers() {
        this.virtualNumbers.setContainerDataSource(this.tableData);
        this.virtualNumbers.setVisibleColumns(this.tableFields.toArray());
        this.virtualNumbers.setSelectable(true);
        this.virtualNumbers.setImmediate(true);

        this.virtualNumbers.addListener(new Property.ValueChangeListener() {
            public void valueChange(Property.ValueChangeEvent event) {
                Object id = VirtualNumbersContent.this.virtualNumbers.getValue();
                if (null != id) {
                    VirtualNumbersContent.this.getForm((String) VirtualNumbersContent.this.virtualNumbers.getItem(id).getItemProperty("id").getValue());
                }
            }
        });
        return this.tableFields;
    }

    private IndexedContainer createTableData() {
        IndexedContainer ic = new IndexedContainer();
        List<VirtualNumber> numbers = getNumbers();

        for (String field : this.hiddenFields) {
            ic.addContainerProperty(field, String.class, "");
        }

        for (VirtualNumber number : numbers) {
            Object object = ic.addItem();
            ic.getContainerProperty(object, this.column).setValue(number.getName());
            ic.getContainerProperty(object, "id").setValue(number.getId());
        }

        return ic;
    }

    private List<VirtualNumber> getNumbers() {
        try {
            return service.getAllVirtualNumbersByUser(PortalUtil.getUserId(request), PortalUtil.getScopeGroupId(request));
        } catch (Exception ignored) {
        }
        return Collections.emptyList();
    }

    public HorizontalLayout createHead() {
        HorizontalLayout head = new HorizontalLayout();
        head.setWidth(100, Sizeable.UNITS_PERCENTAGE);
        Label headLabel = new Label("Virtual Numbers");
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
                VirtualNumbersContent.this.getForm("-1");
            }
        }));
        addRemoveButtons.addComponent(new Button("-", new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                Object id = VirtualNumbersContent.this.virtualNumbers.getValue();
                if (null != id) {
                    String virtualNumbersID = (String) VirtualNumbersContent.this.virtualNumbers.getItem(id).getItemProperty("id").getValue();
                    try {
                        service.removeVirtualNumber(virtualNumbersID);
                    } catch (IOException ignored) {
                    }
                    VirtualNumbersContent.this.virtualNumbers.removeItem(VirtualNumbersContent.this.virtualNumbers.getValue());
                    VirtualNumbersContent.this.virtualNumbers.select(null);
                } else {
                    VirtualNumbersContent.this.getWindow().showNotification("Select Virtual Number");
                }
            }
        }));
        return addRemoveButtons;
    }

    private void getForm(String id) {
        virtualNumbersForm = new VirtualNumbersForm(this.bundle, this.virtualNumbers);
        virtualNumbersForm.setWidth("400px");
        virtualNumbersForm.setHeight("300px");
        virtualNumbersForm.center();
        virtualNumbersForm.setModal(true);

        getWindow().addWindow(virtualNumbersForm);
        virtualNumbersForm.init(id);
    }

    private List<String> fillHiddenFields() {
        List<String> tableFields = new LinkedList<String>();

        tableFields.add(this.column);
        tableFields.add("id");

        return tableFields;
    }
}