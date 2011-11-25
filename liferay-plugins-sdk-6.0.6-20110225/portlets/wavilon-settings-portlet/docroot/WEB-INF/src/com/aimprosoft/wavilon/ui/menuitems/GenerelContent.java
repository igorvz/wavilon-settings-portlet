package com.aimprosoft.wavilon.ui.menuitems;

import com.aimprosoft.wavilon.application.GenericPortletApplication;
import com.aimprosoft.wavilon.couch.CouchModel;
import com.aimprosoft.wavilon.couch.CouchTypes;
import com.aimprosoft.wavilon.service.GeneralService;
import com.aimprosoft.wavilon.ui.menuitems.forms.ConfirmingRemove;
import com.aimprosoft.wavilon.ui.menuitems.forms.PhoneNumbersForm;
import com.aimprosoft.wavilon.util.CouchModelUtil;
import com.aimprosoft.wavilon.util.LayoutUtil;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.*;

import javax.portlet.PortletRequest;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class GenerelContent extends VerticalLayout {
    protected PortletRequest request;
    protected ResourceBundle bundle;
    protected Table table;
    protected IndexedContainer tableData;
    protected List<String> hiddenFields = new LinkedList<String>();
    protected List<String> visibleFields = new LinkedList<String>();

    public GenerelContent(ResourceBundle bundle) {
        this.bundle = bundle;
    }

    public void init() {
        setSizeFull();
        request = ((GenericPortletApplication) getApplication()).getPortletRequest();


    }


    protected void fillHiddenFields() {
        hiddenFields.addAll(visibleFields);
        hiddenFields.add("id");
    }

    protected List<CouchModel> getCouchModels(GeneralService service, CouchTypes type) {
        List<CouchModel> couchModelList = new LinkedList<CouchModel>();

        try {
            couchModelList.addAll(service.getAvailableCouchModels(CouchModelUtil.getOrganizationId(request), type));
        } catch (Exception ignored) {
        }

        return couchModelList;
    }

    protected <T> T getModel(CouchModel model, GeneralService service, Class<T> modelClass) {
        try {
            return service.getModel(model, modelClass);
        } catch (IOException ignored) {
            return null;
        }
    }

    protected HorizontalLayout createTablesEditRemoveButtons(final Table table, final Object object, final CouchModel couchModel, final ResourceBundle bundle, final String phoneNumber, final Window mainWindow) {
        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setSizeFull();

        Button editButton = new Button("", new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                table.select(object);
                LayoutUtil.getForm(couchModel.getId(), object, mainWindow, new PhoneNumbersForm(bundle, table));
            }
        });

        Button removeButton = new Button("", new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                table.select(object);
                ConfirmingRemove confirmingRemove = new ConfirmingRemove(bundle);
                mainWindow.addWindow(confirmingRemove);
                confirmingRemove.setNumbersLocator(phoneNumber, couchModel.getType());
                confirmingRemove.init(couchModel.getId(), table, couchModel.getType());
            }
        });

        editButton.addStyleName("editButton");
        editButton.setWidth("20px");
        editButton.setDescription(bundle.getString("wavilon.button.edit"));
        removeButton.addStyleName("removeButton");
        removeButton.setWidth("20px");
        removeButton.setDescription(bundle.getString("wavilon.button.delete"));

        buttons.addComponent(editButton);
        buttons.setComponentAlignment(editButton, Alignment.MIDDLE_LEFT);
        buttons.addComponent(removeButton);
        buttons.setComponentAlignment(removeButton, Alignment.MIDDLE_LEFT);

        buttons.addStyleName("buttonsContainer");

        return buttons;
    }

    protected void initLayout( CouchTypes type) {
        table = new Table();
        table.setContainerDataSource(tableData);
        table.setHeight("555px");
        table.setFooterVisible(false);
        table.addStyleName("tableCustom");
        table.setVisibleColumns(visibleFields.toArray());
        table.setSelectable(true);
        table.setImmediate(true);
        LayoutUtil.setTableWidth(table, CouchTypes.contact);

        table.addListener(new ItemClickEvent.ItemClickListener() {
            public void itemClick(ItemClickEvent event) {
                if (event.isDoubleClick()) {
                    Item item = event.getItem();
                    if (null != item) {
                        LayoutUtil.getForm((String) event.getItem().getItemProperty("id").getValue(), event.getItemId(), getWindow(), new PhoneNumbersForm(bundle, table));
                    }
                }
            }
        });

        VerticalLayout head = LayoutUtil.createHead(bundle, table, type, getWindow());
        setWidth(100, Sizeable.UNITS_PERCENTAGE);
        addComponent(head);
        addComponent(table);

    }


}
