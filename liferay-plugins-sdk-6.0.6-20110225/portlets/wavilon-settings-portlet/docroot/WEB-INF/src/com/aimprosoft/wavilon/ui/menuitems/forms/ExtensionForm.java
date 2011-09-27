package com.aimprosoft.wavilon.ui.menuitems.forms;

import com.aimprosoft.wavilon.application.GenericPortletApplication;
import com.aimprosoft.wavilon.model.Extension;
import com.aimprosoft.wavilon.service.ExtensionDatabaseService;
import com.aimprosoft.wavilon.spring.ObjectFactory;
import com.liferay.portal.util.PortalUtil;
import com.vaadin.event.ShortcutAction;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.*;

import javax.portlet.PortletRequest;
import java.util.ResourceBundle;
import java.util.UUID;

public class ExtensionForm extends Window {
    private ExtensionDatabaseService service = ObjectFactory.getBean(ExtensionDatabaseService.class);
    private ResourceBundle bundle;
    private PortletRequest request;
    private Table table;
    private Extension extension;

    public ExtensionForm(ResourceBundle bundle, Table table) {
        this.bundle = bundle;
        this.table = table;

        setCaption("Edit Extensions");
    }

    public void init(String id) {
        request = ((GenericPortletApplication) getApplication()).getPortletRequest();
        extension = createExtension(id);

        VerticalLayout content = new VerticalLayout();
        content.addStyleName("formRegion");

        addComponent(content);

        Label headerForm = createHeader(id, extension);
        content.addComponent(headerForm);

        final Form form = createForm();
        content.addComponent(form);

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
                    extension.setFirstName(name);
                    service.addExtension(extension);

                    if (null != extension.getRevision()){
                        table.removeItem(table.getValue());
                        table.select(null);
                    }

                    Object object = table.addItem();
                    table.getContainerProperty(object, "").setValue(extension.getFirstName());
                    table.getContainerProperty(object, "id").setValue(extension.getId());

                    getWindow().showNotification("Well done");
                    close();
                } catch (Exception ignored) {
                }
            }
        });
        save.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        buttons.addComponent(save);
    }

    private Extension createExtension(String id) {
        if ("-1".equals(id)) {
            return newExtension();
        }
        try {
            return service.getExtension(id);
        } catch (Exception e) {
            return newExtension();
        }
    }

    private Extension newExtension() {
        Extension extension = new Extension();

        try {
            extension.setId(UUID.randomUUID().toString());
            extension.setLiferayUserId(PortalUtil.getUserId(request));
            extension.setLiferayOrganizationId(PortalUtil.getScopeGroupId(request));
            extension.setLiferayPortalId(PortalUtil.getCompany(request).getWebId());
        } catch (Exception ignored) {
        }

        extension.setFirstName("");
        return extension;
    }

    private Form createForm() {
        Form form = new Form();
        form.addStyleName("labelField");

        TextField name = new TextField("First Name");
        name.setRequired(true);
        name.setRequiredError("Empty field First Name");

        if (null != extension.getRevision() && !"".equals(extension.getRevision())) {
            name.setValue(extension.getFirstName());
        }
        form.addField("name", name);

        return form;
    }

    private Label createHeader(String id, Extension extension) {
        Label headerForm = new Label("-1".equals(id) ? "New extension" : extension.getFirstName());

        headerForm.setHeight(27, Sizeable.UNITS_PIXELS);
        headerForm.setWidth("100%");
        headerForm.addStyleName("headerForm");

        return headerForm;
    }

    private HorizontalLayout createButtons(VerticalLayout content) {
        HorizontalLayout buttons = new HorizontalLayout();
        buttons.addStyleName("buttonsPanel");
        content.addComponent(buttons);
        content.setComponentAlignment(buttons, Alignment.BOTTOM_RIGHT);

        return buttons;
    }

}
