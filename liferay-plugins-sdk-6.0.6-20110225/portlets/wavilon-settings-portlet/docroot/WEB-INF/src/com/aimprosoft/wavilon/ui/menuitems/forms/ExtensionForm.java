package com.aimprosoft.wavilon.ui.menuitems.forms;

import com.aimprosoft.wavilon.application.GenericPortletApplication;
import com.aimprosoft.wavilon.model.Extension;
import com.aimprosoft.wavilon.service.ExtensionDatabaseService;
import com.aimprosoft.wavilon.spring.ObjectFactory;
import com.liferay.portal.util.PortalUtil;
import com.vaadin.Application;
import com.vaadin.data.Property;
import com.vaadin.data.Validator;
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
    private Object item;
    private Validator mobileValidator = new RegexpValidator("[+][0-9]{10}", "<div align=\"center\">Phone Number must be numeric, begin with + <br/>and consist of 10 digit</div>");
    private Validator emailValidator = new EmailValidator("Wrong format email address");
    private Application application;
    private Extension extension;

    public ExtensionForm(ResourceBundle bundle, Table table) {
        this.bundle = bundle;
        this.table = table;

    }

    public void init(String id) {

        request = ((GenericPortletApplication) getApplication()).getPortletRequest();
        application = (GenericPortletApplication) getApplication();
        extension = createExtension(id);

        if (!"".equals(extension.getExtensionName())) {
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
                    String destination = (String) form.getField("destination").getValue();

                    Object object = table.addItem();

                    Button delete = new Button("-");
                    delete.setData(object);
                    extension.setExtensionType(extensionType);
                    extension.setExtensionName(name);
                    extension.setExtensionDestination(destination);
                    service.addExtension(extension);

                    if (null != extension.getRevision()) {
                        table.removeItem(item);
                        table.select(null);
                    }

                    table.getContainerProperty(object, "extensionId").setValue(extension.getId());
                    table.getContainerProperty(object, "ID").setValue(extension.getLiferayOrganizationId());
                    table.getContainerProperty(object, "NAME").setValue(extension.getExtensionName());
                    table.getContainerProperty(object, "EXTENSION TYPE").setValue(extension.getExtensionType());
                    table.getContainerProperty(object, "DESTINATION").setValue(extension.getExtensionDestination());
                    table.getContainerProperty(object, "").setValue(delete);

                    delete.addListener(new Button.ClickListener() {
                                public void buttonClick(Button.ClickEvent event) {
                                    String id = extension.getId();
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

        extension.setExtensionName("");
        extension.setExtensionType("");
        extension.setExtensionDestination("");
        return extension;
    }

    private Form createForm() {
        final Form form = new Form();
        form.addStyleName("labelField");

        TextField extensionId = new TextField("Extension id");
        extensionId.setValue(extension.getLiferayOrganizationId());
        extensionId.setReadOnly(true);


        TextField name = new TextField("Name");
        name.setRequired(true);
        name.setRequiredError("Empty field First \"Name\"");

        final TextField destination = new TextField();
        destination.setImmediate(true);
        destination.setVisible(false);

        List<String> extensionTypeList = createExtensionType();
        ComboBox extensionType = new ComboBox("Extension type");
        extensionType.setRequired(true);
        extensionType.setRequiredError("Empty field \"Extension type\"");
        extensionType.setImmediate(true);
        for (String s : extensionTypeList) {
            extensionType.addItem(s);
            if (null != extension.getExtensionType() && extension.getExtensionType().equals(s)) {
                extensionType.setValue(s);
            }
        }
        extensionType.setNullSelectionItemId("Select...");
        extensionType.addListener(new ComboBox.ValueChangeListener() {
            public void valueChange(Property.ValueChangeEvent event) {
                String type = (String) event.getProperty().getValue();
                changeDestinationValidator(type, destination, form);
            }
        });

        if (null != extension.getRevision() && !"".equals(extension.getRevision())) {
            name.setValue(extension.getExtensionName());
            destination.setValue(extension.getExtensionDestination());
            changeDestinationValidator(extension.getExtensionType(), destination, form);
        }

        form.addField("extensionId", extensionId);
        form.addField("name", name);
        form.addField("extensionType", extensionType);
        form.addField("destination", destination);

        return form;
    }

    private void changeDestinationValidator(String type, TextField destination, Form form) {
        form.setComponentError(null);

        if (null != type) {
            destination.removeValidator(emailValidator);
            destination.removeValidator(mobileValidator);

            destination.setVisible(true);
            destination.setCaption(type);

            destination.setRequired(true);
            destination.setRequiredError("Empty field \"" + type + "\"");

            if ("Phone Number".equals(type)) {
                destination.addValidator(mobileValidator);
            } else if ("Gtalk".equals(type)) {
                destination.addValidator(emailValidator);
            }

        } else {
            destination.setVisible(false);
        }
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

    public void setItem(Object item) {
        this.item = item;
    }
}
