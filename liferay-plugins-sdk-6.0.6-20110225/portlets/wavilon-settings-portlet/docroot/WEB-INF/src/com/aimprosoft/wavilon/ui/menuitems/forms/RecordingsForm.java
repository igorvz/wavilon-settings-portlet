package com.aimprosoft.wavilon.ui.menuitems.forms;

import com.aimprosoft.wavilon.couch.Attachment;
import com.aimprosoft.wavilon.couch.CouchModel;
import com.aimprosoft.wavilon.couch.CouchModelLite;
import com.aimprosoft.wavilon.couch.CouchTypes;
import com.aimprosoft.wavilon.model.Recording;
import com.aimprosoft.wavilon.service.RecordingDatabaseService;
import com.aimprosoft.wavilon.spring.ObjectFactory;
import com.aimprosoft.wavilon.util.CouchModelUtil;
import com.aimprosoft.wavilon.util.LayoutUtil;
import com.vaadin.terminal.UserError;
import com.vaadin.ui.*;

import java.net.URLDecoder;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class RecordingsForm extends GeneralForm {
    private RecordingDatabaseService service = ObjectFactory.getBean(RecordingDatabaseService.class);
    private Recording recording = null;
    private RecordingUploader recordingUploader = null;

    public RecordingsForm(final ResourceBundle bundle, Table table) {
        super(bundle, table);
    }

    public void init(String id, final Object itemId) {
        super.init(id, itemId);
        model = createCoucModel(id, service, CouchTypes.recording);
        recording = createRecording(model);

        if ("-1".equals(id)) {
            setCaption(bundle.getString("wavilon.form.recordings.new.recording"));
        } else {
            setCaption(bundle.getString("wavilon.form.recordings.edit.recording"));
        }

        VerticalLayout content = new VerticalLayout();
        content.addStyleName("formRegion");

        addComponent(content);

        final Form form = createForm();
        content.addComponent(form);

        HorizontalLayout uploadLayout = new HorizontalLayout();

        recordingUploader = createRecordingUpload();
        uploadLayout.addComponent(recordingUploader);

        content.addComponent(uploadLayout);
        recordingUploader.init(model, recording, form);

        createSaveCancelButtons(bundle, content, this, new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                try {
                    form.commit();

                    String name = (String) form.getField("name").getValue();
                    String forwardId = null;
                    if (null != form.getField("forward").getValue()) {
                        CouchModelLite forwardModel = (CouchModelLite) form.getField("forward").getValue();
                        forwardId = forwardModel.getId();
                        recording.setForwardTo(forwardId);
                    }

                    model.setType(CouchTypes.recording);

                    recording.setName(name);


                    if (model.getAttachments() == null) {

                        UserError userError = new UserError(bundle.getString("wavilon.error.massage.recordings.upload.empty"));
                        form.setComponentError(userError);
                    } else {

                        service.addRecording(recording, model);

                        final Object object = table.addItem();

                        if (null != model.getRevision()) {
                            table.removeItem(itemId);
                            table.select(null);
                        }

                        Map<String, Attachment> attachmentMap = model.getAttachments();
                        for (Map.Entry<String, Attachment> entry : attachmentMap.entrySet()) {

                            String fileName = URLDecoder.decode(entry.getKey(), "UTF-8");

                            table.getContainerProperty(object, bundle.getString("wavilon.table.recordings.column.name")).setValue(recording.getName());
                            table.getContainerProperty(object, bundle.getString("wavilon.table.recordings.column.forward.to.on.end")).setValue(CouchModelUtil.getCouchModelLite(forwardId, bundle).getName());
                            table.getContainerProperty(object, bundle.getString("wavilon.table.recordings.column.media.file")).setValue(fileName);
                            table.getContainerProperty(object, "id").setValue(model.getId());

                            HorizontalLayout buttons = LayoutUtil.createTablesEditRemoveButtons(table, object, model, bundle, null, application.getMainWindow());
                            table.getContainerProperty(object, "").setValue(buttons);

                            LayoutUtil.setTableBackground(table, CouchTypes.recording);

                        }


                        getParent().getWindow().showNotification(bundle.getString("wavilon.well.done"));
                        close();
                    }
                } catch (Exception ignored) {
                }
            }
        });
    }

    private Recording createRecording(CouchModel model) {
        if (null == model.getRevision()) {
            return newRecording();
        }
        try {
            return getModel(model, service, Recording.class);
        } catch (Exception e) {
            return newRecording();
        }
    }

    private Recording newRecording() {
        Recording newRecording = new Recording();
        newRecording.setName("");
        return newRecording;
    }

    private Form createForm() {
        Form form = new Form();
        form.addStyleName("labelField");

        TextField name = new TextField(bundle.getString("wavilon.form.name"));
        name.setRequired(true);
        name.setRequiredError(bundle.getString("wavilon.error.massage.recordings.name.empty"));
        form.addField("name", name);

        List<CouchModelLite> forwardsLites = getForwards();

        final ComboBox forwardComboBox = new ComboBox(bundle.getString("wavilon.form.recordings.forward.to"));
        forwardComboBox.addItem(bundle.getString("wavilon.form.select"));

        for (CouchModelLite forward : forwardsLites) {
            forwardComboBox.addItem(forward);
        }
        forwardComboBox.setNullSelectionItemId(bundle.getString("wavilon.form.select"));
        form.addField("forward", forwardComboBox);

        if (null != model.getRevision() && !"".equals(model.getRevision())) {
            name.setValue(model.getProperties().get("name"));
        }
        return form;
    }

    private RecordingUploader createRecordingUpload() {
        recordingUploader = new RecordingUploader(bundle);
        recordingUploader.addStyleName("recordingUploader");
        recordingUploader.attach();

        return recordingUploader;
    }
}
