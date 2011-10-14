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
import java.util.Map;
import java.util.ResourceBundle;

public class ExtensionForm extends Window {
    private ExtensionDatabaseService service = ObjectFactory.getBean(ExtensionDatabaseService.class);
    private ResourceBundle bundle;
    private PortletRequest request;
    private Table table;
    private Object item;
    private Validator mobileValidator = null;
    private Validator emailValidator = null;
    private Application application;
    private Map<String, String> extensionTypeMap;

    private CouchModel model;
    private Extension extension;

    public ExtensionForm(ResourceBundle bundle, Table table) {
        this.bundle = bundle;
        this.table = table;
        mobileValidator = new RegexpValidator("[+][0-9]{10}", bundle.getString("wavilon.error.massage.extensions.phonenumber.wrong"));
        emailValidator = new EmailValidator(bundle.getString("wavilon.error.massage.extensions.email.wrong"));
    }

    public void init(String id) {
        application = getApplication();
        request = ((GenericPortletApplication) application).getPortletRequest();

        model = createModel(id);
        extension = createExtension(model);

        if (!"".equals(extension.getName())) {
            setCaption(bundle.getString("wavilon.form.extensions.edit.extension"));
        } else {
            setCaption(bundle.getString("wavilon.form.extensions.new.extension"));
        }

        VerticalLayout content = new VerticalLayout();
        content.addStyleName("formRegion");

        addComponent(content);

        final Form form = createForm();
        content.addComponent(form);

        HorizontalLayout buttons = createButtons(content);

        Button cancel = new Button(bundle.getString("wavilon.button.cancel"), new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                close();
            }
        });
        buttons.addComponent(cancel);

        Button save = new Button(bundle.getString("wavilon.button.save"), new Button.ClickListener() {
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
                            confirmingRemove.setModal(true);
                            confirmingRemove.setWidth("330px");
                            confirmingRemove.setHeight("180px");
                        }
                    };

                    extension.setChannel(extensionTypeMap.get(extensionType));
                    extension.setName(name);
                    extension.setDestination(destination);

                    service.addExtension(extension, model);

                    if (null != model.getRevision()) {
                        table.removeItem(item);
                        table.select(null);
                    }

                    table.getContainerProperty(object, "extensionId").setValue(model.getId());
                    table.getContainerProperty(object, bundle.getString("wavilon.table.extensions.column.id")).setValue(model.getLiferayOrganizationId());
                    table.getContainerProperty(object, bundle.getString("wavilon.table.extensions.column.name")).setValue(extension.getName());
                    table.getContainerProperty(object, bundle.getString("wavilon.table.extensions.column.extension.type")).setValue(CouchModelUtil.extensionTypeMapEject(bundle).get(extension.getChannel()));
                    table.getContainerProperty(object, bundle.getString("wavilon.table.extensions.column.destination")).setValue(extension.getDestination());
                    table.getContainerProperty(object, "").setValue(new Button("", listener));

                    getWindow().showNotification(bundle.getString("wavilon.well.done"));
                    close();
                } catch (Exception ignored) {
                }
            }
        });
        save.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        save.addStyleName("saveButton");
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

        TextField extensionId = new TextField(bundle.getString("wavilon.form.extensions.extension.id"));
        extensionId.setValue(model.getLiferayOrganizationId());
        extensionId.setReadOnly(true);


        TextField name = new TextField(bundle.getString("wavilon.form.name"));
        name.setRequired(true);
        name.setRequiredError(bundle.getString("wavilon.error.massage.extensions.name.empty"));

        final TextField destination = new TextField();
        destination.setImmediate(true);
        destination.setVisible(false);

        extensionTypeMap = CouchModelUtil.extensionTypeMapPut(bundle);

        ComboBox extensionType = new ComboBox(bundle.getString("wavilon.form.extensions.extension.type"));
        extensionType.addItem(bundle.getString("wavilon.form.select"));
        extensionType.setRequired(true);
        extensionType.setRequiredError(bundle.getString("wavilon.error.massage.extensions.extension.type.empty"));
        extensionType.setImmediate(true);

        for (String s : extensionTypeMap.keySet()) {
            extensionType.addItem(s);
            if (null != extension.getChannel() && extension.getChannel().equals(extensionTypeMap.get(s))) {
                extensionType.setValue(s);
            }
        }

        extensionType.setNullSelectionItemId(bundle.getString("wavilon.form.select"));
        extensionType.addListener(new ComboBox.ValueChangeListener() {
            public void valueChange(Property.ValueChangeEvent event) {
                String type = (String) event.getProperty().getValue();
                changeDestinationValidator(type, destination, form);
            }
        });

        if (null != model.getRevision() && !"".equals(model.getRevision())) {
            name.setValue(extension.getName());
            destination.setValue(extension.getDestination());
            changeDestinationValidator(CouchModelUtil.extensionTypeMapEject(bundle).get(extension.getChannel()), destination, form);
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
            destination.setRequiredError(type + " " + bundle.getString("wavilon.error.massage.extensions.type"));

            if (bundle.getString("wavilon.form.extensions.type.phone.number").equals(type)) {
                destination.addValidator(mobileValidator);
            } else if (bundle.getString("wavilon.form.extensions.type.gtalk").equals(type)) {
                destination.addValidator(emailValidator);
            }

        } else {
            destination.setVisible(false);
        }
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
