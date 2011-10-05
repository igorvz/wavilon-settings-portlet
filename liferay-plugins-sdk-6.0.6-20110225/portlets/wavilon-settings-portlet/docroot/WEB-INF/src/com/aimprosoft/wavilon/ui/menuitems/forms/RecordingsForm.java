package com.aimprosoft.wavilon.ui.menuitems.forms;

import com.aimprosoft.wavilon.application.GenericPortletApplication;
import com.aimprosoft.wavilon.model.Attachment;
import com.aimprosoft.wavilon.model.Extension;
import com.aimprosoft.wavilon.model.Recording;
import com.aimprosoft.wavilon.service.ExtensionDatabaseService;
import com.aimprosoft.wavilon.service.RecordingDatabaseService;
import com.aimprosoft.wavilon.spring.ObjectFactory;
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
    private Item item;
    private RecordingDatabaseService service = ObjectFactory.getBean(RecordingDatabaseService.class);
    private ExtensionDatabaseService extensionService = ObjectFactory.getBean(ExtensionDatabaseService.class);
    private static PortletRequest request;
    private Recording recording = null;
    private RecordingUploader recordingUploader = null;
    private Table table;
    private Application application;

    public RecordingsForm(final ResourceBundle bundle, Table table) {
        this.bundle = bundle;
        this.table = table;
    }

    public void init(String id) {
        request = ((GenericPortletApplication) getApplication()).getPortletRequest();
        recording = createRecording(id);
        application = (GenericPortletApplication) getApplication();

        VerticalLayout content = new VerticalLayout();
        content.addStyleName("formRegion");

        addComponent(content);

        final Form form = createForm();
        content.addComponent(form);


        if (!"".equals(recording.getName())) {
            setCaption("Edit Recording");

        } else {
            setCaption("New Recording");
        }

        HorizontalLayout uploadLayout = new HorizontalLayout();

        recordingUploader = createRecordingUpload();
        uploadLayout.addComponent(recordingUploader);

        content.addComponent(uploadLayout);
        recordingUploader.init(recording);

        HorizontalLayout buttons = createButtons(content);

        Button cancel = new Button("Cancel", new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                close();
            }
        });
        buttons.addComponent(cancel);

        Button save = new Button(bundle.getString("wavilon.settings.validation.form.button.save"), new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                try {
                    form.commit();

                    String name = (String) form.getField("name").getValue();
                    Extension extension = (Extension) form.getField("extension").getValue();

                    recording.setName(name);
                    recording.setExtensionId(extension.getId());

                    if (recording.getAttachments() == null) {

                        UserError userError = new UserError("You must select file");
                        form.setComponentError(userError);
                    } else {

                        service.addRecording(recording);

                        Object object = table.addItem();
                        Button delete = new Button("-");
                        delete.setData(object);

                        if (null != recording.getRevision()) {
                            Integer itemId = (Integer) object;
                            table.removeItem(itemId-1);
                            table.select(null);
                        }

                        Map<String, Attachment> attachmentMap = recording.getAttachments();
                        for (Map.Entry<String, Attachment> entry : attachmentMap.entrySet()) {

                            String fileName = URLDecoder.decode(entry.getKey(), "UTF-8");

                            table.getContainerProperty(object, "NAME").setValue(recording.getName());
                            table.getContainerProperty(object, "FORWARD TO ON END").setValue(extension.getExtensionName());
                            table.getContainerProperty(object, "MEDIA FILE").setValue(fileName);
                            table.getContainerProperty(object, "id").setValue(recording.getId());
                            table.getContainerProperty(object, "").setValue(delete);
                            delete.addListener(new Button.ClickListener() {
                                public void buttonClick(Button.ClickEvent event) {
                                    String id = recording.getId();
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

        List<Extension> extensions = getExtensions();
        final ComboBox extensionCombo = new ComboBox("Forward to");
        extensionCombo.addItem("Select . . .");
        for (Extension extension : extensions) {
            extensionCombo.addItem(extension);
        }
        extensionCombo.setImmediate(true);
        extensionCombo.setNullSelectionAllowed(false);
        extensionCombo.setNullSelectionItemId("Select . . .");
        extensionCombo.setRequired(true);
        extensionCombo.setRequiredError("Select Extension");
        form.addField("extension", extensionCombo);

        if (null != recording.getRevision() && !"".equals(recording.getRevision())) {
            name.setValue(recording.getName());
        }
        return form;
    }

    private RecordingUploader createRecordingUpload() {
        recordingUploader = new RecordingUploader();
        recordingUploader.attach();

        return recordingUploader;
    }

    private Recording createRecording(String id) {
        if ("-1".equals(id)) {
            return newRecording();
        }
        try {
            return service.getRecording(id);
        } catch (Exception e) {
            return newRecording();
        }
    }

    private Recording newRecording() {
        Recording recording = new Recording();

        try {
            recording.setId(UUID.randomUUID().toString());
            recording.setLiferayUserId(PortalUtil.getUserId(request));
            recording.setLiferayOrganizationId(PortalUtil.getScopeGroupId(request));
            recording.setLiferayPortalId(PortalUtil.getCompany(request).getWebId());
        } catch (Exception ignored) {
        }

        recording.setName("");
        return recording;
    }

    private List<Extension> getExtensions() {
        try {
            return extensionService.getAllExtensionByUserId(PortalUtil.getUserId(request), PortalUtil.getScopeGroupId(request));
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}
