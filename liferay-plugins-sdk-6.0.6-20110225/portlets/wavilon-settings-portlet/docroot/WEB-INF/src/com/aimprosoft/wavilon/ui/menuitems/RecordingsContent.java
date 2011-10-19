package com.aimprosoft.wavilon.ui.menuitems;

import com.aimprosoft.wavilon.application.GenericPortletApplication;
import com.aimprosoft.wavilon.couch.Attachment;
import com.aimprosoft.wavilon.couch.CouchModel;
import com.aimprosoft.wavilon.couch.CouchModelLite;
import com.aimprosoft.wavilon.couch.CouchTypes;
import com.aimprosoft.wavilon.model.Recording;
import com.aimprosoft.wavilon.service.RecordingDatabaseService;
import com.aimprosoft.wavilon.spring.ObjectFactory;
import com.aimprosoft.wavilon.ui.menuitems.forms.ConfirmingRemove;
import com.aimprosoft.wavilon.ui.menuitems.forms.RecordingsForm;
import com.aimprosoft.wavilon.util.CouchModelUtil;
import com.aimprosoft.wavilon.util.LayoutUtil;
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

    private List<String> hiddenFields;

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
        HorizontalLayout head = LayoutUtil.createHead(bundle, table, CouchTypes.recording, getWindow());
        setWidth(100, Sizeable.UNITS_PERCENTAGE);
        addComponent(head);

        table.setColumnExpandRatio(bundle.getString("wavilon.table.recordings.column.name"), 1);
        table.setColumnExpandRatio(bundle.getString("wavilon.table.recordings.column.forward.to.on.end"), 2);
        table.setColumnExpandRatio(bundle.getString("wavilon.table.recordings.column.media.file"), 2);

        table.setContainerDataSource(tableData);
        table.setWidth(100, Sizeable.UNITS_PERCENTAGE);
        table.setColumnWidth("", 60);
        table.setHeight("555px");
        table.addStyleName("tableCustom");
        addComponent(table);
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
                        LayoutUtil.getForm((String) event.getItem().getItemProperty("id").getValue(), event.getItemId(), getWindow(), new RecordingsForm(bundle,table));

                    }
                }
            }
        });
    }

    private IndexedContainer createTableData() {
        IndexedContainer ic = new IndexedContainer();
        List<CouchModel> recordingModelLiteList = getAllRecordingLite();
        for (String field : tableFields) {
            if ("".equals(field)) {
                ic.addContainerProperty(field, Component.class, null);
            }
            ic.addContainerProperty(field, String.class, "");
        }

        if (!recordingModelLiteList.isEmpty()) {

            for (final CouchModel couchModel : recordingModelLiteList) {
                Recording recording = getRecording(couchModel);

                final Object object = ic.addItem();
                String fileName = "";

                Map<String, Attachment> attachmentMap = couchModel.getAttachments();
                for (Map.Entry<String, Attachment> entry : attachmentMap.entrySet()) {

                    try {
                        fileName = URLDecoder.decode(entry.getKey(), "UTF-8");
                    } catch (UnsupportedEncodingException ignore) {
                    }
                }
                Map<String, Object> param = new HashMap<String, Object>();

                param.put("id", couchModel.getId());
                param.put("object", object);
                Button delete = new Button("");
                delete.setData(param);

                CouchModelLite forwardModel = createForward(couchModel);

                ic.getContainerProperty(object, bundle.getString("wavilon.table.recordings.column.name")).setValue(recording.getName());
                ic.getContainerProperty(object, bundle.getString("wavilon.table.recordings.column.forward.to.on.end")).setValue(forwardModel.getName());
                ic.getContainerProperty(object, bundle.getString("wavilon.table.recordings.column.media.file")).setValue(fileName);
                ic.getContainerProperty(object, "id").setValue(couchModel.getId());
                ic.getContainerProperty(object, "").setValue(delete);

                delete.addListener(new Button.ClickListener() {
                    public void buttonClick(Button.ClickEvent event) {
                        table.select(object);
                        ConfirmingRemove confirmingRemove = new ConfirmingRemove(bundle);
                        getWindow().addWindow(confirmingRemove);
                        confirmingRemove.init(couchModel.getId(), table);
                    }
                });
            }
        }
        return ic;
    }

    private CouchModelLite createForward(CouchModel couchModel) {
        return getForward((String) couchModel.getProperties().get("forward_to"));
    }

    private Recording getRecording(CouchModel couchModel) {
        try {
            return service.getRecording(couchModel);
        } catch (Exception e) {
            return new Recording();
        }
    }

    private CouchModelLite getForward(String id) {
        try {
            return CouchModelUtil.getCouchModelLite(id, bundle);
        } catch (Exception e) {
            return (CouchModelLite) Collections.emptyList();
        }
    }

    private List<CouchModel> getAllRecordingLite() {
        try {
            return service.getAllUsersCouchModelToRecording(CouchModelUtil.getOrganizationId(request), false);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    private List<String> fillHiddenFields() {
        LinkedList<String> tableFields = new LinkedList<String>();

        tableFields.add(bundle.getString("wavilon.table.recordings.column.name"));
        tableFields.add(bundle.getString("wavilon.table.recordings.column.forward.to.on.end"));
        tableFields.add(bundle.getString("wavilon.table.recordings.column.media.file"));
        tableFields.add("");

        return tableFields;
    }

    private LinkedList<String> fillFields() {
        LinkedList<String> tableFields = new LinkedList<String>();

        tableFields.add(bundle.getString("wavilon.table.recordings.column.name"));
        tableFields.add(bundle.getString("wavilon.table.recordings.column.forward.to.on.end"));
        tableFields.add(bundle.getString("wavilon.table.recordings.column.media.file"));
        tableFields.add("");
        tableFields.add("id");

        return tableFields;
    }
}
