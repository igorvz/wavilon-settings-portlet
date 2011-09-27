package com.aimprosoft.wavilon.ui.menuitems;

import com.aimprosoft.wavilon.application.GenericPortletApplication;
import com.aimprosoft.wavilon.model.Recording;
import com.aimprosoft.wavilon.service.RecordingDatabaseService;
import com.aimprosoft.wavilon.spring.ObjectFactory;
import com.aimprosoft.wavilon.ui.menuitems.forms.RecordingsForm;
import com.liferay.portal.util.PortalUtil;
import com.vaadin.data.Item;
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

public class RecordingsContent extends VerticalLayout {
    private ResourceBundle bundle;
    private Long userId;
    private static PortletRequest request;
    private RecordingDatabaseService service = ObjectFactory.getBean(RecordingDatabaseService.class);
    private List<String> hiddenFields;
    private HorizontalLayout main = new HorizontalLayout();
    private VerticalLayout left = new VerticalLayout();
    private VerticalLayout right = new VerticalLayout();

    private Table table = new Table();
    private List<String> tableFields;
    private IndexedContainer tableData;

    private HorizontalLayout bottomLeftCorner = new HorizontalLayout();
    private Button contactRemovalButton;

    public RecordingsContent(ResourceBundle bundle) {
        this.bundle = bundle;
    }

    public void init() {
        request = ((GenericPortletApplication) getApplication()).getPortletRequest();
        tableFields = fillFields();
        hiddenFields = fillHiddenFields();
        tableData = createTableData();

        initLayout();
        initTableAddRemoveButtons();
        initAddressList();
    }

    private void initLayout() {
        main.setHeight(400, Sizeable.UNITS_PIXELS);
        main.setSizeUndefined();
        addComponent(main);
        main.addComponent(left);
        main.addComponent(right);

        table.setContainerDataSource(tableData);
        table.setHeight(330, Sizeable.UNITS_PIXELS);
        table.setWidth(200, Sizeable.UNITS_PIXELS);
        left.addComponent(table);

        bottomLeftCorner.setWidth(100, Sizeable.UNITS_PIXELS);
        left.addComponent(bottomLeftCorner);
        left.setComponentAlignment(bottomLeftCorner, Alignment.BOTTOM_LEFT);
    }

    private List<String> initAddressList() {
        table.setContainerDataSource(tableData);
        table.setVisibleColumns(tableFields.toArray());
        table.setSelectable(true);
        table.setImmediate(true);

        table.addListener(new Property.ValueChangeListener() {
            public void valueChange(Property.ValueChangeEvent event) {
                Object id = table.getValue();

                contactRemovalButton.setVisible(id != null);

                if (id != null) {
                    viewRightColumnContent(table.getItem(id));
                }

            }
        });

        return tableFields;
    }

    private void initTableAddRemoveButtons() {
        // New item button
        bottomLeftCorner.addComponent(new Button("+",
                new Button.ClickListener() {
                    public void buttonClick(Button.ClickEvent event) {
                        viewRightColumnContent(null);
                    }
                }));

        // Remove item button
        contactRemovalButton = new Button("-", new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                Object id = table.getValue();

                String recordingID = (String) table.getItem(id).getItemProperty("id").getValue();

                try {
                    service.removeRecording(recordingID);
                } catch (IOException ignored) {
                }

                table.removeItem(table.getValue());
                table.select(null);
                right.removeAllComponents();

            }
        });
        contactRemovalButton.setVisible(false);
        bottomLeftCorner.addComponent(contactRemovalButton);
    }

    private LinkedList<String> fillFields() {
        LinkedList<String> tableFields = new LinkedList<String>();

        tableFields.add("name");

        return tableFields;
    }

    private IndexedContainer createTableData() {
        IndexedContainer ic = new IndexedContainer();

        List<Recording> recordings = getRecordings();

        for (String field : hiddenFields) {
            ic.addContainerProperty(field, String.class, "");
        }

        if (!recordings.isEmpty()) {
            for (Recording recording : recordings) {
                Object object = ic.addItem();
                ic.getContainerProperty(object, "name").setValue(recording.getFirstName());
                ic.getContainerProperty(object, "id").setValue(recording.getId());
            }
        }

        return ic;
    }

    private List<Recording> getRecordings() {
        try {
            return service.getAllRecordingsByUserId(PortalUtil.getUserId(request), PortalUtil.getScopeGroupId(request));
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    private void viewRightColumnContent(Object id) {
        Item item = id == null ? null : (Item) id;

        right.removeAllComponents();
        right.setMargin(false, true, false, true);
        RecordingsForm recordingsForm = new RecordingsForm(bundle);
        right.addComponent(recordingsForm);
        recordingsForm.init(item, right, tableData);
    }

    private List<String> fillHiddenFields() {
        LinkedList<String> tableFields = new LinkedList<String>();

        tableFields.add("name");
        tableFields.add("id");

        return tableFields;
    }
}
