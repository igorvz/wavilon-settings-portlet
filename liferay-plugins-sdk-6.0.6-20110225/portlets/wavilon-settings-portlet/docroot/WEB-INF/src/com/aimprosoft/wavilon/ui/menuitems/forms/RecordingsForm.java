package com.aimprosoft.wavilon.ui.menuitems.forms;

import com.aimprosoft.wavilon.application.GenericPortletApplication;
import com.aimprosoft.wavilon.couch.Attachment;
import com.aimprosoft.wavilon.couch.CouchModel;
import com.aimprosoft.wavilon.couch.CouchModelLite;
import com.aimprosoft.wavilon.couch.CouchTypes;
import com.aimprosoft.wavilon.model.Recording;
import com.aimprosoft.wavilon.service.RecordingDatabaseService;
import com.aimprosoft.wavilon.spring.ObjectFactory;
import com.aimprosoft.wavilon.util.CouchModelUtil;
import com.vaadin.Application;
import com.vaadin.event.ShortcutAction;
import com.vaadin.terminal.UserError;
import com.vaadin.ui.*;

import javax.portlet.PortletRequest;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class RecordingsForm extends AbstractForm {
    private ResourceBundle bundle;

    private RecordingDatabaseService service = ObjectFactory.getBean(RecordingDatabaseService.class);

    private PortletRequest request;
    private Recording recording = null;
    private CouchModel model = null;
    private RecordingUploader recordingUploader = null;
    private Table table;
    private Application application;

    public RecordingsForm(final ResourceBundle bundle, Table table) {
        this.bundle = bundle;
        this.table = table;
    }

    public void init(String id, final Object itemId) {
        removeAllComponents();
        request = ((GenericPortletApplication) getApplication()).getPortletRequest();
        application = getApplication();

        model = createCouchModel(id);
        recording = new Recording();

        VerticalLayout content = new VerticalLayout();
        content.addStyleName("formRegion");

        addComponent(content);

        final Form form = createForm();
        content.addComponent(form);

        if (model.getRevision() != null) {
            setCaption(bundle.getString("wavilon.form.recordings.edit.recording"));

        } else {
            setCaption(bundle.getString("wavilon.form.recordings.new.recording"));
        }

        HorizontalLayout uploadLayout = new HorizontalLayout();

        recordingUploader = createRecordingUpload();
        uploadLayout.addComponent(recordingUploader);

        content.addComponent(uploadLayout);
        recordingUploader.init(model, recording, form);

        HorizontalLayout buttons = createButtons(content);

        Button cancel = new Button("Cancel", new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                close();
            }
        });
        buttons.addComponent(cancel);
        cancel.setClickShortcut(ShortcutAction.KeyCode.ESCAPE);

        Button save = new Button(bundle.getString("wavilon.button.save"));
        save.addListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                try {
                    form.commit();

                    String name = (String) form.getField("name").getValue();
                    String forwardId = null;
                    if (null != form.getField("forward").getValue()){
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

                        Button delete = new Button("");
                        delete.setData(object);
                        delete.addListener(new Button.ClickListener() {
                            public void buttonClick(Button.ClickEvent event) {

                                table.select(object);
                                String phoneNumbersID = (String) table.getItem(object).getItemProperty("id").getValue();
                                ConfirmingRemove confirmingRemove = new ConfirmingRemove(bundle);
                                application.getMainWindow().addWindow(confirmingRemove);
                                confirmingRemove.init(phoneNumbersID, table);
                            }
                        });

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
                            table.getContainerProperty(object, "").setValue(delete);
                        }
                        getParent().getWindow().showNotification(bundle.getString("wavilon.well.done"));
                        close();
                    }
                } catch (Exception ignored) {
                }
            }
        });
        save.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        save.addStyleName("saveButton");
        buttons.addComponent(save);
    }

    private HorizontalLayout createButtons(VerticalLayout content) {
        HorizontalLayout buttons = new HorizontalLayout();
        buttons.addStyleName("buttonsPanel");
        content.addComponent(buttons);
        content.setComponentAlignment(buttons, Alignment.BOTTOM_RIGHT);

        return buttons;
    }

    private Form createForm() {
        Form form = new Form();
        form.addStyleName("labelField");

        TextField name = new TextField(bundle.getString("wavilon.form.name"));
        name.setRequired(true);
        name.setRequiredError(bundle.getString("wavilon.error.massage.recordings.name.empty"));
        form.addField("name", name);

        List<CouchModelLite> forwardsLites = getLiteForward();

        final ComboBox forwardComboBox = new ComboBox(bundle.getString("wavilon.form.recordings.forward.to"));
        forwardComboBox.addItem(bundle.getString("wavilon.form.select"));

        for (CouchModelLite forward : forwardsLites) {
            forwardComboBox.addItem(forward);
        }
        forwardComboBox.setNullSelectionItemId(bundle.getString("wavilon.form.select"));
//        forwardComboBox.setRequired(true);
//        forwardComboBox.setRequiredError(bundle.getString("wavilon.error.massage.recordings.forward.empty"));
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

    private CouchModel createCouchModel(String id) {
        if ("-1".equals(id)) {
            return CouchModelUtil.newCouchModel(request, CouchTypes.recording);
        }
        try {
            return service.getModel(id);
        } catch (Exception e) {
            return CouchModelUtil.newCouchModel(request, CouchTypes.recording);
        }
    }

    private List<CouchModelLite> getLiteForward() {
        try {
            return CouchModelUtil.getForwards(CouchModelUtil.getOrganizationId(request));
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}
