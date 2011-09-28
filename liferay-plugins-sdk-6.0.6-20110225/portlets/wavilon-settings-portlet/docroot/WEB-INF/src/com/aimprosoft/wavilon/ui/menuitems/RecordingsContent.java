package com.aimprosoft.wavilon.ui.menuitems;

import com.aimprosoft.wavilon.application.GenericPortletApplication;
import com.aimprosoft.wavilon.model.Recording;
import com.aimprosoft.wavilon.service.RecordingDatabaseService;
import com.aimprosoft.wavilon.spring.ObjectFactory;
import com.aimprosoft.wavilon.ui.menuitems.forms.RecordingsForm;
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

public class RecordingsContent extends VerticalLayout {
    private ResourceBundle bundle;
    private static PortletRequest request;
    private RecordingDatabaseService service = ObjectFactory.getBean(RecordingDatabaseService.class);
    private List<String> hiddenFields;
    private RecordingsForm recordingsForm;

    private Table table = new Table();
    private List<String> tableFields;
    private IndexedContainer tableData;

    public RecordingsContent(ResourceBundle bundle) {
        this.bundle = bundle;
    }

    public void init() {
        request = ((GenericPortletApplication) getApplication()).getPortletRequest();
        tableFields = fillFields();
        hiddenFields = fillHiddenFields();
        tableData = createTableData();

        initLayout();
        initRecording();
    }

    private void initLayout() {
        HorizontalLayout head = createHead();
        setWidth(100, Sizeable.UNITS_PERCENTAGE);
        addComponent(head);

        table.setContainerDataSource(tableData);
        table.setWidth(100, Sizeable.UNITS_PERCENTAGE);
        table.setStyleName("tableCustom");
        addComponent(table);
    }

    private HorizontalLayout createHead() {
        HorizontalLayout head = new HorizontalLayout();
        head.setWidth(100, Sizeable.UNITS_PERCENTAGE);

        Label headLabel = new Label("Recordings");
        head.addComponent(headLabel);
        head.setMargin(false);
        head.addStyleName("headLine");
        headLabel.addStyleName("tableHeader");
        headLabel.addStyleName("recordingsHeader");

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
                    String recordingID = (String) table.getItem(id).getItemProperty("id").getValue();
                    try {
                        service.removeRecording(recordingID);
                    } catch (IOException ignored) {
                    }
                    table.removeItem(table.getValue());
                    table.select(null);
                } else {
                    getWindow().showNotification("Select Recording");
                }
            }
        }));

        return addRemoveButtons;
    }

    private void getForm(String id) {
        recordingsForm = new RecordingsForm(bundle, table);
        recordingsForm.setWidth(410.0F, 0);
        recordingsForm.setHeight(440.0F, 0);
        recordingsForm.center();
        recordingsForm.setModal(true);

        getWindow().addWindow(recordingsForm);
        recordingsForm.init(id);
    }

    private List<String> initRecording() {
        table.setContainerDataSource(tableData);
        table.setVisibleColumns(tableFields.toArray());
        table.setSelectable(true);
        table.setImmediate(true);

        table.addListener(new Property.ValueChangeListener() {
            public void valueChange(Property.ValueChangeEvent event) {
                if (recordingsForm != null && recordingsForm.getParent() != null) {
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

        List<Recording> recordings = getRecordings();

        for (String field : hiddenFields) {
            ic.addContainerProperty(field, String.class, "");
        }

        if (!recordings.isEmpty()) {
            for (Recording recording : recordings) {
                Object object = ic.addItem();
                ic.getContainerProperty(object, "").setValue(recording.getFirstName());
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

    private List<String> fillHiddenFields() {
        LinkedList<String> tableFields = new LinkedList<String>();

        tableFields.add("");
        tableFields.add("id");

        return tableFields;
    }

}
