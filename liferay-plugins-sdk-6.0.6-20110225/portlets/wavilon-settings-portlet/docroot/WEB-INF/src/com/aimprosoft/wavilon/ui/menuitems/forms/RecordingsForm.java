package com.aimprosoft.wavilon.ui.menuitems.forms;

import com.aimprosoft.wavilon.application.GenericPortletApplication;
import com.aimprosoft.wavilon.couch.Attachment;
import com.aimprosoft.wavilon.couch.CouchModel;
import com.aimprosoft.wavilon.couch.CouchModelLite;
import com.aimprosoft.wavilon.couch.CouchTypes;
import com.aimprosoft.wavilon.model.Recording;
import com.aimprosoft.wavilon.service.CouchModelLiteDatabaseService;
import com.aimprosoft.wavilon.service.RecordingDatabaseService;
import com.aimprosoft.wavilon.spring.ObjectFactory;
import com.aimprosoft.wavilon.util.CouchModelUtil;
import com.liferay.portal.util.PortalUtil;
import com.vaadin.Application;
import com.vaadin.data.Item;
import com.vaadin.event.ShortcutAction;
import com.vaadin.terminal.UserError;
import com.vaadin.ui.*;

import javax.portlet.PortletRequest;
import java.net.URLDecoder;
import java.util.*;

public class RecordingsForm extends Window {
    private ResourceBundle bundle;

    private RecordingDatabaseService service = ObjectFactory.getBean(RecordingDatabaseService.class);
    private CouchModelLiteDatabaseService modelLiteService = ObjectFactory.getBean(CouchModelLiteDatabaseService.class);

    private static PortletRequest request;
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
            setCaption("Edit Recording");

        } else {
            setCaption("New Recording");
        }

        HorizontalLayout uploadLayout = new HorizontalLayout();

        recordingUploader = createRecordingUpload();
        uploadLayout.addComponent(recordingUploader);

        content.addComponent(uploadLayout);
        recordingUploader.init(model);

        HorizontalLayout buttons = createButtons(content);

        Button cancel = new Button("Cancel", new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                close();
            }
        });
        buttons.addComponent(cancel);

        Button save = new Button(bundle.getString("wavilon.settings.validation.form.button.save"));
        save.addListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                try {
                    form.commit();

                    String name = (String) form.getField("name").getValue();
                    CouchModelLite extensionModel = (CouchModelLite) form.getField("extension").getValue();

                    model.setType(CouchTypes.recording);

                    Map<String, Object> extensionOut = new HashMap<String, Object>();
                    extensionOut.put("extension", extensionModel.getId());

                    model.setType(CouchTypes.recording);
                    model.setOutputs(extensionOut);
                    recording.setName(name);

                    if (model.getAttachments() == null) {

                        UserError userError = new UserError("You must select file");
                        form.setComponentError(userError);
                    } else {

                        service.addRecording(recording, model);

                        Object object = table.addItem();
                        Button delete = new Button("-");
                        delete.setData(object);

                        if (null != model.getRevision()) {
                            table.removeItem(itemId);
                            table.select(null);
                        }

                        Map<String, Attachment> attachmentMap = model.getAttachments();
                        for (Map.Entry<String, Attachment> entry : attachmentMap.entrySet()) {

                            String fileName = URLDecoder.decode(entry.getKey(), "UTF-8");

                            table.getContainerProperty(object, "NAME").setValue(recording.getName());
                            table.getContainerProperty(object, "FORWARD TO ON END").setValue(extensionModel.getName());
                            table.getContainerProperty(object, "MEDIA FILE").setValue(fileName);
                            table.getContainerProperty(object, "id").setValue(model.getId());
                            table.getContainerProperty(object, "").setValue(delete);
                            delete.addListener(new Button.ClickListener() {
                                public void buttonClick(Button.ClickEvent event) {
                                    String id = model.getId();
                                    Object object = event.getButton().getData();

                                    if (null != id) {
                                        ConfirmingRemove confirmingRemove = new ConfirmingRemove(bundle);

                                        application.getWindow("settingWindow").addWindow(confirmingRemove);
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
                        getWindow().showNotification("Well done");
                        close();
                    }
                } catch (Exception ignored) {
                }
            }
        });
        save.setClickShortcut(ShortcutAction.KeyCode.ENTER);
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

        TextField name = new TextField("Name");
        name.setRequired(true);
        name.setRequiredError("Name must be not empty");
        form.addField("name", name);

        List<CouchModelLite> extensionsLites = getLiteExtensions();

        final ComboBox extensionCombo = new ComboBox("Forward to");
        extensionCombo.addItem("Select . . .");

        for (CouchModelLite extension : extensionsLites) {
            extensionCombo.addItem(extension);
        }
        extensionCombo.setImmediate(true);
        extensionCombo.setNullSelectionAllowed(false);
        extensionCombo.setNullSelectionItemId("Select . . .");
        extensionCombo.setRequired(true);
        extensionCombo.setRequiredError("Select Extension");
        form.addField("extension", extensionCombo);

        if (null != model.getRevision() && !"".equals(model.getRevision())) {
            name.setValue(model.getProperties().get("name"));
        }
        return form;
    }

    private RecordingUploader createRecordingUpload() {
        recordingUploader = new RecordingUploader();
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

    private List<CouchModelLite> getLiteExtensions() {
        try {
            return modelLiteService.getAllCouchModelsLite(PortalUtil.getUserId(request), PortalUtil.getScopeGroupId(request), CouchTypes.extension);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}
