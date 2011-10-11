package com.aimprosoft.wavilon.ui.menuitems.forms;

import com.aimprosoft.wavilon.application.GenericPortletApplication;
import com.aimprosoft.wavilon.couch.CouchModel;
import com.aimprosoft.wavilon.couch.CouchTypes;
import com.aimprosoft.wavilon.model.Extension;
import com.aimprosoft.wavilon.service.ExtensionDatabaseService;
import com.aimprosoft.wavilon.spring.ObjectFactory;
import com.aimprosoft.wavilon.util.CouchModelUtil;
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

public class ExtensionForm extends Window {
    private ExtensionDatabaseService service = ObjectFactory.getBean(ExtensionDatabaseService.class);
    private ResourceBundle bundle;
    private PortletRequest request;
    private Table table;
    private Object item;
    private Validator mobileValidator = new RegexpValidator("[+][0-9]{10}", "<div align=\"center\">Phone Number must be numeric, begin with + <br/>and consist of 10 digit</div>");
    private Validator emailValidator = new EmailValidator("Wrong format email address");
    private Application application;

    private CouchModel model;
    private Extension extension;

    public ExtensionForm(ResourceBundle bundle, Table table) {
        this.bundle = bundle;
        this.table = table;
    }

    public void init(String id) {
        application = getApplication();
        request = ((GenericPortletApplication) application).getPortletRequest();

        model = createModel(id);
        extension = createExtension(model);


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
                    String destination = (String) form.getField("destination").getValue();

                    final Object object = table.addItem();

                    Button.ClickListener listener = new Button.ClickListener() {
                        public void buttonClick(Button.ClickEvent event) {

                            table.select(object);
                            String phoneNumbersID = (String) table.getItem(object).getItemProperty("id").getValue();
                            ConfirmingRemove confirmingRemove = new ConfirmingRemove(bundle);
                            application.getMainWindow().addWindow(confirmingRemove);
                            confirmingRemove.init(phoneNumbersID, table);
                            confirmingRemove.center();
                            confirmingRemove.setWidth("300px");
                            confirmingRemove.setHeight("180px");
                        }
                    };

                    extension.setChannel(extensionType);
                    extension.setName(name);
                    extension.setDestination(destination);

                    service.addExtension(extension, model);

                    if (null != model.getRevision()) {
                        table.removeItem(item);
                        table.select(null);
                    }

                    table.getContainerProperty(object, "extensionId").setValue(model.getId());
                    table.getContainerProperty(object, "ID").setValue(model.getLiferayOrganizationId());
                    table.getContainerProperty(object, "NAME").setValue(extension.getName());
                    table.getContainerProperty(object, "EXTENSION TYPE").setValue(extension.getChannel());
                    table.getContainerProperty(object, "DESTINATION").setValue(extension.getDestination());
                    table.getContainerProperty(object, "").setValue(new Button("", listener));



                    getWindow().showNotification("Well done");
                    close();
                } catch (Exception ignored) {
                }
            }
        });
        save.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        buttons.addComponent(save);
    }

    private CouchModel createModel(String id) {
        try {
            return service.getModel(id);
        } catch (Exception e) {
            return CouchModelUtil.newCouchModel(request, CouchTypes.extension);
        }
    }

    private Extension createExtension(CouchModel model) {
        if (null == model.getRevision()) {
            return newExtension();
        }
        try {
            return service.getExtension(model);
        } catch (Exception e) {
            return newExtension();
        }
    }

    private Extension newExtension() {
        Extension extension = new Extension();

        extension.setName("");
        extension.setChannel("");
        extension.setDestination("");

        return extension;
    }

    private Form createForm() {
        final Form form = new Form();
        form.addStyleName("labelField");

        TextField extensionId = new TextField("Extension id");
        extensionId.setValue(model.getLiferayOrganizationId());
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
            if (null != extension.getChannel() && extension.getChannel().equals(s)) {
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

        if (null != model.getRevision() && !"".equals(model.getRevision())) {
            name.setValue(extension.getName());
            destination.setValue(extension.getDestination());
            changeDestinationValidator(extension.getChannel(), destination, form);
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
