package com.aimprosoft.wavilon.ui.menuitems.forms;

import com.aimprosoft.wavilon.application.GenericPortletApplication;
import com.aimprosoft.wavilon.model.Extension;
import com.aimprosoft.wavilon.service.ExtensionDatabaseService;
import com.aimprosoft.wavilon.spring.ObjectFactory;
import com.liferay.portal.util.PortalUtil;
import com.vaadin.data.Property;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.*;

import javax.portlet.PortletRequest;
import java.util.LinkedList;
import java.util.List;
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
    }

    public void init(String id) {

        request = ((GenericPortletApplication) getApplication()).getPortletRequest();
        extension = createExtension(id);

        if (!"".equals(extension.getName())) {
            setCaption("Edit Extension");
        } else {
            setCaption("New Extension");
        }

        VerticalLayout content = new VerticalLayout();
        content.addStyleName("formRegion");

        addComponent(content);

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
                    String extensionType = (String) form.getField("extensionType").getValue();
                    String changeField = (String) form.getField("changeField").getValue();

                    Object object = table.addItem();

                    if ("Gtalk".equals(extensionType)) {
                        extension.setgTalk(changeField);
                    }

                    if ("SIP".equals(extensionType)) {
                        extension.setSipURL(changeField);
                    }

                    if ("Phone Number".equals(extensionType)) {
                        extension.setPhoneNumber(changeField);
                    }

                    extension.setExtensionType(extensionType);
                    extension.setName(name);
                    service.addExtension(extension);

                    if (null != extension.getRevision()) {
                        table.removeItem(table.getValue());
                        table.select(null);
                    }

                    table.getContainerProperty(object, "extensionIdBase").setValue(extension.getId());
                    table.getContainerProperty(object, "id").setValue(extension.getLiferayOrganizationId());
                    table.getContainerProperty(object, "name").setValue(extension.getName());
                    table.getContainerProperty(object, "extension type").setValue(extension.getExtensionType());
                    table.getContainerProperty(object, "destination").setValue(setDestination(extensionType));

                    getWindow().showNotification("Well done");
                    close();
                } catch (Exception ignored) {
                }
            }
        });
        save.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        buttons.addComponent(save);
    }

    private String setDestination(String extensionType) {
        if ("Phone Number".equals(extensionType)) {
            return extension.getPhoneNumber();
        }
        if ("SIP".equals(extensionType)) {
            return extension.getSipURL();
        }
        if ("Gtalk".equals(extensionType)) {
            return extension.getgTalk();
        } else return "";
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

        extension.setName("");
        extension.setgTalk("");
        extension.setSipURL("");
        extension.setPhoneNumber("");
        return extension;
    }

    private Form createForm() {
        final Form form = new Form();
        form.addStyleName("labelField");

        TextField extensionId = new TextField("Extension id");

        TextField name = new TextField("Name");
        name.setRequired(true);
        name.setRequiredError("Empty field First Name");

        List<String> extensionTypeList = createExtensionType();
        ComboBox extensionType = new ComboBox("Extension type");
        extensionType.setImmediate(true);
        for (String s : extensionTypeList) {
            extensionType.addItem(s);
            if(null != extension.getExtensionType() && extension.getExtensionType().equals(s)){
                extensionType.setValue(s);
            }
        }
        extensionType.setNullSelectionItemId("Select...");

        extensionType.addListener(new ComboBox.ValueChangeListener() {
            public void valueChange(Property.ValueChangeEvent event) {
                String fieldName = (String) event.getProperty().getValue();

                form.removeItemProperty("changeField");
                final TextField changeField = new TextField();
                changeField.setRequired(true);

                if ("Gtalk".equals(fieldName)) {
                    changeField.setCaption("Gtalk email");

                    if (null != extension.getRevision() && !"".equals(extension.getRevision())) {
                        changeField.setValue(extension.getgTalk());
                    }
                    changeField.setRequiredError("Gtalk email must be not empty");
                    changeField.addValidator(new EmailValidator("Wrong format email address"));
                }

                if ("SIP".equals(fieldName)) {
                    changeField.setCaption("SIP URI");

                    if (null != extension.getRevision() && !"".equals(extension.getRevision())) {
                        changeField.setValue(extension.getSipURL());

                    }
                    changeField.setRequiredError("SIP URI email must be not empty");
                }

                if ("Phone Number".equals(fieldName)) {
                    changeField.setCaption("Phone number");

                    if (null != extension.getRevision() && !"".equals(extension.getRevision())) {
                        changeField.setValue(extension.getPhoneNumber());
                    }
                    changeField.setRequiredError("Phone number email must be not empty");
                    changeField.addValidator(new RegexpValidator("[+][0-9]{10}", "Phone number must begin with +..."));
                }
                form.addField("changeField", changeField);
            }
        });

        if (null != extension.getRevision() && !"".equals(extension.getRevision())) {
            name.setValue(extension.getName());
        }
        try {
            extensionId.setValue(PortalUtil.getScopeGroupId(request));
            extensionId.setReadOnly(true);
        } catch (Exception ignored) {
        }

        form.addField("extensionId", extensionId);
        form.addField("name", name);
        form.addField("extensionType", extensionType);

        return form;
    }

    private List<String> createExtensionType() {
        List<String> extensionTypeList = new LinkedList<String>();
        extensionTypeList.add("Phone Number");
        extensionTypeList.add("Gtalk");
        extensionTypeList.add("SIP");
        extensionTypeList.add(0, "Select...");

        return extensionTypeList;
    }

    private HorizontalLayout createButtons(VerticalLayout content) {
        HorizontalLayout buttons = new HorizontalLayout();
        buttons.addStyleName("buttonsPanel");
        content.addComponent(buttons);
        content.setComponentAlignment(buttons, Alignment.BOTTOM_RIGHT);

        return buttons;
    }

}
