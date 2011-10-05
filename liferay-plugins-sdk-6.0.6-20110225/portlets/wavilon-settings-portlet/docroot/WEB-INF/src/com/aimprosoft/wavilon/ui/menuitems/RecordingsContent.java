package com.aimprosoft.wavilon.ui.menuitems;

import com.aimprosoft.wavilon.application.GenericPortletApplication;
import com.aimprosoft.wavilon.model.Attachment;
import com.aimprosoft.wavilon.model.Extension;
import com.aimprosoft.wavilon.model.Recording;
import com.aimprosoft.wavilon.service.ExtensionDatabaseService;
import com.aimprosoft.wavilon.service.RecordingDatabaseService;
import com.aimprosoft.wavilon.spring.ObjectFactory;
import com.aimprosoft.wavilon.ui.menuitems.forms.ConfirmingRemove;
import com.aimprosoft.wavilon.ui.menuitems.forms.RecordingsForm;
import com.liferay.portal.util.PortalUtil;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.*;

import javax.portlet.PortletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

public class RecordingsContent extends VerticalLayout {
    private ResourceBundle bundle;
    private static PortletRequest request;
    private RecordingDatabaseService service = ObjectFactory.getBean(RecordingDatabaseService.class);
    private ExtensionDatabaseService serviceExtension = ObjectFactory.getBean(ExtensionDatabaseService.class);
    private List<String> hiddenFields;
    private RecordingsForm recordingsForm;

    private Table table = new Table();
    private List<String> tableFields;
    private IndexedContainer tableData;
    private Extension extension = null;

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
        return addRemoveButtons;
    }

    private void getForm(String id) {
        recordingsForm = new RecordingsForm(bundle, table);
        recordingsForm.setWidth(410.0F, 0);
        recordingsForm.setHeight(350.0F, 0);
        recordingsForm.center();
        recordingsForm.setModal(true);

        getWindow().addWindow(recordingsForm);
        recordingsForm.init(id);
    }

    private void initRecording() {

        table.setContainerDataSource(tableData);
        table.setVisibleColumns(hiddenFields.toArray());
        table.setSelectable(true);
        table.setImmediate(true);

        table.addListener(new ItemClickEvent.ItemClickListener() {
            public void itemClick(ItemClickEvent event) {
                if (event.isDoubleClick()) {
                    Item item = event.getItem();
                    if (null != item) {
                        getForm((String) event.getItem().getItemProperty("id").getValue());
                    }
                }
            }
        });
    }

    private LinkedList<String> fillFields() {
        LinkedList<String> tableFields = new LinkedList<String>();

        tableFields.add("NAME");
        tableFields.add("FORWARD TO ON END");
        tableFields.add("MEDIA FILE");
        tableFields.add("");
        tableFields.add("id");

        return tableFields;
    }

    private IndexedContainer createTableData() {
        IndexedContainer ic = new IndexedContainer();

        List<Recording> recordings = getRecordings();

        for (String field : tableFields) {
            if ("".equals(field)) {
                ic.addContainerProperty(field, Component.class, "");
            }
            ic.addContainerProperty(field, String.class, "");
        }

        if (!recordings.isEmpty()) {

            for (Recording recording : recordings) {
                final Object object = ic.addItem();
                String fileName = "";

                Map<String, Attachment> attachmentMap = recording.getAttachments();
                for (Map.Entry<String, Attachment> entry : attachmentMap.entrySet()) {

                    try {
                        fileName = URLDecoder.decode(entry.getKey(), "UTF-8");
                    } catch (UnsupportedEncodingException ignore) {
                    }
                }
                Map<String, Object> param = new HashMap<String, Object>();
                param.put("id", recording.getId());
                param.put("object", object);
                Button delete = new Button("-");
                delete.setData(param);

                try {

                    extension = serviceExtension.getExtension(recording.getExtensionId());

                } catch (Exception ignore) {
                    extension = new Extension();
                    extension.setExtensionName("");
                }

                ic.getContainerProperty(object, "NAME").setValue(recording.getName());
                ic.getContainerProperty(object, "FORWARD TO ON END").setValue(extension.getExtensionName());
                ic.getContainerProperty(object, "MEDIA FILE").setValue(fileName);
                ic.getContainerProperty(object, "id").setValue(recording.getId());
                ic.getContainerProperty(object, "").setValue(delete);

                delete.addListener(new Button.ClickListener() {
                    public void buttonClick(Button.ClickEvent event) {
                        Map<String, Object> paramMap = (Map<String, Object>) event.getButton().getData();
                        String id = (String) paramMap.get("id");
                        Object object = paramMap.get("object");

                        if (null != id) {

                            ConfirmingRemove confirmingRemove = new ConfirmingRemove(bundle);
                            getWindow().addWindow(confirmingRemove);
                            confirmingRemove.initConfirm(id, table, object);
                            confirmingRemove.center();
                            confirmingRemove.setWidth("420px");
                            confirmingRemove.setHeight("180px");
                        } else {
                            getWindow().showNotification("Select Recording");
                        }
                    }
                });
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

        tableFields.add("NAME");
        tableFields.add("FORWARD TO ON END");
        tableFields.add("MEDIA FILE");
        tableFields.add("");

        return tableFields;
    }
}
