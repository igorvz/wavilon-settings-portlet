package com.aimprosoft.wavilon.ui.menuitems;

import com.aimprosoft.wavilon.couch.Attachment;
import com.aimprosoft.wavilon.couch.CouchModel;
import com.aimprosoft.wavilon.couch.CouchModelLite;
import com.aimprosoft.wavilon.couch.CouchTypes;
import com.aimprosoft.wavilon.model.Recording;
import com.aimprosoft.wavilon.service.RecordingDatabaseService;
import com.aimprosoft.wavilon.spring.ObjectFactory;
import com.aimprosoft.wavilon.util.CouchModelUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;
import java.util.ResourceBundle;

public class RecordingsContent extends GenerelContent {
    private RecordingDatabaseService service = ObjectFactory.getBean(RecordingDatabaseService.class);


    public RecordingsContent(ResourceBundle bundle) {
        super(bundle);

    }

    public void init() {
        super.init();
        fillVisibleFields();
        fillHiddenFields();
        createTableData(service, CouchTypes.recording);
        fillTable();
        initLayout(CouchTypes.recording, ratioMap(1,2,2));
    }

    private void fillTable() {
        if (!couchModels.isEmpty()) {
            for (final CouchModel couchModel : couchModels) {
                Object object = tableData.addItem();
                Recording recording = getModel(couchModel, service, Recording.class);
                String fileName = "";

                Map<String, Attachment> attachmentMap = couchModel.getAttachments();
                for (Map.Entry<String, Attachment> entry : attachmentMap.entrySet()) {

                    try {
                        fileName = URLDecoder.decode(entry.getKey(), "UTF-8");
                    } catch (UnsupportedEncodingException ignore) {
                    }
                }


                CouchModelLite forwardModel = CouchModelUtil.getCouchModelLite(recording.getForwardTo(), bundle);

                tableData.getContainerProperty(object, bundle.getString("wavilon.table.recordings.column.name")).setValue(recording.getName());
                tableData.getContainerProperty(object, bundle.getString("wavilon.table.recordings.column.forward.to.on.end")).setValue(forwardModel.getName());
                tableData.getContainerProperty(object, bundle.getString("wavilon.table.recordings.column.media.file")).setValue(fileName);
                tableData.getContainerProperty(object, "id").setValue(couchModel.getId());
                tableData.getContainerProperty(object, "").setValue(createTablesEditRemoveButtons(object, couchModel, null));

            }
        }
    }

    private void fillVisibleFields() {
        visibleFields.add(bundle.getString("wavilon.table.recordings.column.name"));
        visibleFields.add(bundle.getString("wavilon.table.recordings.column.forward.to.on.end"));
        visibleFields.add(bundle.getString("wavilon.table.recordings.column.media.file"));
    }
}
