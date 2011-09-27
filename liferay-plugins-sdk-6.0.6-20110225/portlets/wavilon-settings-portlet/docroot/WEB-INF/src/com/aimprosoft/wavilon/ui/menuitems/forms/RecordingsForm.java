package com.aimprosoft.wavilon.ui.menuitems.forms;

import com.aimprosoft.wavilon.application.GenericPortletApplication;
import com.aimprosoft.wavilon.model.Recording;
import com.aimprosoft.wavilon.service.RecordingDatabaseService;
import com.aimprosoft.wavilon.spring.ObjectFactory;
import com.liferay.portal.util.PortalUtil;
import com.vaadin.data.Item;
import com.vaadin.event.ShortcutAction;
import com.vaadin.terminal.Sizeable;
import com.vaadin.terminal.UserError;
import com.vaadin.ui.*;

import javax.portlet.PortletRequest;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;

public class RecordingsForm extends Window {
    private ResourceBundle bundle;
    private Item item;
    private static RecordingDatabaseService service = ObjectFactory.getBean(RecordingDatabaseService.class);
    private static PortletRequest request;
    private List<String> extensions = new LinkedList<String>();
    private Recording recording = null;
    private RecordingUploader recordingUploader = null;
    private Table table;

    public RecordingsForm(final ResourceBundle bundle, Table table) {
        this.bundle = bundle;
        this.table = table;
        setCaption("Edit recording");
    }

    public void init(String id) {
        request = ((GenericPortletApplication) getApplication()).getPortletRequest();
        recording = createRecording(id);

        VerticalLayout content = new VerticalLayout();
        content.addStyleName("formRegion");

        addComponent(content);

        Label headerForm = createHeader(id, recording);
        content.addComponent(headerForm);

        final Form form = createForm();
        content.addComponent(form);

        recordingUploader = createRecordingUpload();
        content.addComponent(recordingUploader);
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
                    recording.setFirstName(name);

                    if (recording.getAttachments() == null) {

                        UserError userError = new UserError("You must select file");
                        form.setComponentError(userError);
                    } else {

                        service.addRecording(recording);

                        if (null != recording.getRevision()) {
                            table.removeItem(table.getValue());
                            table.select(null);
                        }

                        Object object = table.addItem();
                        table.getContainerProperty(object, "").setValue(recording.getFirstName());
                        table.getContainerProperty(object, "id").setValue(recording.getId());

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

        TextField name = new TextField("First Name");
        name.setRequired(true);
        name.setRequiredError("Empty field First Name");

        if (null != recording.getRevision() && !"".equals(recording.getRevision())) {
            name.setValue(recording.getFirstName());
        }
        form.addField("name", name);

        return form;
    }

    private RecordingUploader createRecordingUpload() {
        recordingUploader = new RecordingUploader();
        recordingUploader.attach();

        return recordingUploader;
    }

    private Label createHeader(String id, Recording recording) {
        Label headerForm = new Label("-1".equals(id) ? "New recording" : recording.getFirstName());

        headerForm.setHeight(27, Sizeable.UNITS_PIXELS);
        headerForm.setWidth("100%");
        headerForm.addStyleName("headerForm");

        return headerForm;
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

        recording.setFirstName("");
        return recording;
    }

}
