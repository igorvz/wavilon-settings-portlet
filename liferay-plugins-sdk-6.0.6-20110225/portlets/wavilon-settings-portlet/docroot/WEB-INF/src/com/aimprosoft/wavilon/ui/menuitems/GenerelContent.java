package com.aimprosoft.wavilon.ui.menuitems;

import com.aimprosoft.wavilon.application.GenericPortletApplication;
import com.aimprosoft.wavilon.couch.CouchModel;
import com.aimprosoft.wavilon.couch.CouchTypes;
import com.aimprosoft.wavilon.service.GeneralService;
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
import java.io.IOException;
import java.util.*;

public class GenerelContent extends VerticalLayout {
    protected PortletRequest request;
    protected ResourceBundle bundle;
    protected Table table = new Table();
    protected IndexedContainer tableData;
    protected List<String> hiddenFields = new LinkedList<String>();
    protected List<String> visibleFields = new LinkedList<String>();
    protected List<CouchModel> couchModels;

    public GenerelContent(ResourceBundle bundle) {
        this.bundle = bundle;
    }

    public void init() {
        setSizeFull();
        request = ((GenericPortletApplication) getApplication()).getPortletRequest();


    }


    protected void fillHiddenFields() {
        visibleFields.add("");
        hiddenFields.addAll(visibleFields);
        hiddenFields.add("id");
    }

    private void getCouchModels(GeneralService service, CouchTypes type) {
        couchModels = new LinkedList<CouchModel>();

        try {
            couchModels.addAll(service.getAvailableCouchModels(CouchModelUtil.getOrganizationId(request), type));
        } catch (Exception ignored) {
        }

    }

    protected <T> T getModel(CouchModel model, GeneralService service, Class<T> modelClass) {
        try {
            return service.getModel(model, modelClass);
        } catch (IOException ignored) {
            return null;
        }
    }

    protected HorizontalLayout createTablesEditRemoveButtons(final Object object, final CouchModel couchModel, final String phoneNumber) {
        return LayoutUtil.createTablesEditRemoveButtons(table, object, couchModel, bundle, phoneNumber, getWindow());
    }

    protected void initLayout(CouchTypes type) {
        initLayout(type, null);
    }

    protected void initLayout(final CouchTypes type, Map<String, Integer> nonstandardColumn) {
        table.setContainerDataSource(tableData);
        if (CouchTypes.queue != type) {
            table.setHeight("555px");
        } else {
            table.setHeight("207px");
        }
        table.setFooterVisible(false);
        table.addStyleName("tableCustom");
        table.setVisibleColumns(visibleFields.toArray());
        table.setSelectable(true);
        table.setImmediate(true);

        LayoutUtil.setTableWidth(table, type, nonstandardColumn);

        table.addListener(new ItemClickEvent.ItemClickListener() {
            public void itemClick(ItemClickEvent event) {
                if (event.isDoubleClick()) {
                    Item item = event.getItem();
                    if (null != item) {
                        LayoutUtil.getForm((String) event.getItem().getItemProperty("id").getValue(), event.getItemId(), getWindow(), LayoutUtil.getGeneralForm(type, bundle, table));
                    }
                }
            }
        });

        VerticalLayout head = LayoutUtil.createHead(bundle, table, type, getWindow());
        setWidth(100, Sizeable.UNITS_PERCENTAGE);
        addComponent(head);
        addComponent(table);

    }

    protected void createTableData(GeneralService service, CouchTypes type) {
        tableData = new IndexedContainer();
        getCouchModels(service, type);
        LayoutUtil.addContainerProperties(hiddenFields, tableData);
    }

    protected Map<String, Integer> ratioMap(Integer... ratioUnits) {
        if (null != ratioUnits && ratioUnits.length > 0) {
            Map<String, Integer> ratioMap = new LinkedHashMap<String, Integer>();
            for (int i = 0; i < ratioUnits.length; i++) {
                ratioMap.put(visibleFields.get(i), ratioUnits[i]);
            }
            return ratioMap;
        } else return null;
    }

}
