package com.aimprosoft.wavilon.ui.menuitems.forms;

import com.aimprosoft.wavilon.application.GenericPortletApplication;
import com.aimprosoft.wavilon.model.Recording;
import com.aimprosoft.wavilon.service.RecordingDatabaseService;
import com.aimprosoft.wavilon.spring.ObjectFactory;
import com.liferay.portal.util.PortalUtil;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.ShortcutAction;
import com.vaadin.terminal.UserError;
import com.vaadin.ui.Button;
import com.vaadin.ui.Form;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import javax.portlet.PortletRequest;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;

public class RecordingsForm extends VerticalLayout {
    private ResourceBundle bundle;
    private Item item;
    private static RecordingDatabaseService service = ObjectFactory.getBean(RecordingDatabaseService.class);
    private List<String> extensions = new LinkedList<String>();
    private Recording recording = null;
    private RecordingUploader recordingUploader = null;

    public RecordingsForm(final ResourceBundle bundle) {
        this.bundle = bundle;
    }

    public void init(final Item item, final VerticalLayout right, final IndexedContainer tableData) {
        this.item = item;

        if (item != null) {
            String id = (String) item.getItemProperty("id").getValue();
            try {
                recording = service.getRecording(id);
            } catch (IOException ignored) {
            }
        } else {

            recording = new Recording();
            recording.setFirstName("");
        }

        final Form form = new Form();

        TextField firstName = new TextField("First name");
        firstName.setValue(recording.getFirstName());
        firstName.setRequired(true);
        firstName.setRequiredError(bundle.getString("wavilon.settings.validation.form.error.firstName"));
        form.addField("firstName", firstName);

        final Button select = new Button("Select", form, "commit");
        select.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        select.addListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                try {
                    form.commit();

                    if (recording.getId() == null) {
                        recording.setId(UUID.randomUUID().toString());
                        PortletRequest request = ((GenericPortletApplication) getApplication()).getPortletRequest();
                        recording.setLiferayUserId(PortalUtil.getUserId(request));
                        recording.setLiferayOrganizationId(PortalUtil.getScopeGroupId(request));
                        recording.setLiferayPortalId(PortalUtil.getCompany(request).getWebId());
                    }

                    String firstName = (String) form.getField("firstName").getValue();

                    recording.setFirstName(firstName);

                    if (recording.getAttachments() == null) {

                        UserError userError = new UserError("You must select file");
                        form.setComponentError(userError);
                    }
                    else {
                        service.updateRecording(recording);

                        getWindow().showNotification("Well done");
                        right.removeAllComponents();

                        Object object = tableData.addItem();
                        tableData.getContainerProperty(object, "id").setValue(recording.getId());
                        tableData.getContainerProperty(object, "name").setValue(recording.getFirstName());
                    }
                } catch (Exception ignored) {
                }
            }
        });

        recordingUploader = new RecordingUploader();
        recordingUploader.attach();
        addComponent(form);

        addComponent(recordingUploader);
        recordingUploader.init(recording);
        addComponent(select);
    }

}
