package com.aimprosoft.wavilon.ui.menuitems.forms;

import com.aimprosoft.wavilon.couch.CouchModel;
import com.aimprosoft.wavilon.couch.CouchModelLite;
import com.aimprosoft.wavilon.couch.CouchTypes;
import com.aimprosoft.wavilon.model.Extension;
import com.aimprosoft.wavilon.service.CouchModelLiteDatabaseService;
import com.aimprosoft.wavilon.service.ExtensionDatabaseService;
import com.aimprosoft.wavilon.spring.ObjectFactory;
import com.aimprosoft.wavilon.util.CouchModelUtil;
import com.aimprosoft.wavilon.util.LayoutUtil;
import com.vaadin.data.Property;
import com.vaadin.data.Validator;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.terminal.UserError;
import com.vaadin.ui.*;

import java.io.IOException;
import java.util.*;

public class ExtensionForm extends GeneralForm {
    private ExtensionDatabaseService service = ObjectFactory.getBean(ExtensionDatabaseService.class);
    private CouchModelLiteDatabaseService modelLiteService = ObjectFactory.getBean(CouchModelLiteDatabaseService.class);
    private Validator mobileValidator = null;
    private Validator emailValidator = null;
    private Map<String, String> extensionTypeMap;
    private Extension extension;

    public ExtensionForm(ResourceBundle bundle, Table table) {
        super(bundle, table);

        mobileValidator = new RegexpValidator("^([+])?+([0-9])+$", bundle.getString("wavilon.error.massage.extensions.phonenumber.wrong"));
        emailValidator = new EmailValidator(bundle.getString("wavilon.error.massage.extensions.email.wrong"));
    }

    @Override
    public void init(String id, final Object itemId) {
        super.init(id, itemId);
        model = createCoucModel(id, service, CouchTypes.extension);
        extension = createExtension(model);

        if (!"".equals(extension.getName())) {
            setCaption(bundle.getString("wavilon.form.extensions.edit.extension"));
        } else {
            setCaption(bundle.getString("wavilon.form.extensions.new.extension"));
        }

        final Form form = createForm();

        initForm(form, new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                try {
                    form.commit();

                    String name = (String) form.getField("name").getValue();
                    Integer code = Integer.parseInt((String) form.getField("code").getValue());
                    String jumpIfBusyComboBox = null;
                    String jumpIfNoAnswerComboBox = null;

                    String extensionType = null;
                    String destination = null;


                    if (null != form.getField("jumpIfBusyComboBox").getValue()) {
                        jumpIfBusyComboBox = ((CouchModelLite) form.getField("jumpIfBusyComboBox").getValue()).getId();
                    }
                    if (null != form.getField("jumpIfNoAnswerComboBox").getValue()) {
                        jumpIfNoAnswerComboBox = ((CouchModelLite) form.getField("jumpIfNoAnswerComboBox").getValue()).getId();
                    }


                    if (null != form.getField("extensionType").getValue()) {
                        extensionType = (String) form.getField("extensionType").getValue();
                        destination = (String) form.getField("destination").getValue();
                    }

                    if (checkCode(code)) {

                        code = createCode();

                        UserError userError = new UserError(bundle.getString("wavilon.error.massage.extensions.code.exist") + " " + code);
                        form.setComponentError(userError);
                        form.getField("code").setValue(String.valueOf(code));

                    } else {

                        final Object object = table.addItem();

                        extension.setJumpIfBusy(jumpIfBusyComboBox);
                        extension.setJumpIfNoAnswer(jumpIfNoAnswerComboBox);
                        extension.setChannel(extensionTypeMap.get(extensionType));
                        extension.setName(name);
                        extension.setDestination(destination);

                        extension.setCode(code);

                        service.addExtension(extension, model);

                        if (null != model.getRevision()) {
                            table.removeItem(itemId);
                            table.select(null);
                        }

                        table.getContainerProperty(object, "extensionId").setValue(model.getId());
                        table.getContainerProperty(object, bundle.getString("wavilon.table.extensions.column.code")).setValue(extension.getCode());
                        table.getContainerProperty(object, bundle.getString("wavilon.table.extensions.column.name")).setValue(extension.getName());
                        table.getContainerProperty(object, bundle.getString("wavilon.table.extensions.column.extension.type")).setValue(CouchModelUtil.extensionTypeMapEject(bundle).get(extension.getChannel()));
                        table.getContainerProperty(object, bundle.getString("wavilon.table.extensions.column.destination")).setValue(extension.getDestination());
                        HorizontalLayout buttons = LayoutUtil.createTablesEditRemoveButtons(table, object, model, bundle, null, application.getMainWindow());
                        table.getContainerProperty(object, "").setValue(buttons);

                        LayoutUtil.setTableBackground(table, CouchTypes.extension);

                        getParent().getWindow().showNotification(bundle.getString("wavilon.well.done"));
                        close();
                    }

                } catch (Exception ignored) {
                }
            }
        });
    }

    private Extension createExtension(CouchModel model) {
        if (null == model.getRevision()) {
            return newExtension();
        }
        try {
            return getModel(model, service, Extension.class);
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

        TextField name = new TextField(bundle.getString("wavilon.form.name"));
        name.setRequired(true);
        name.setRequiredError(bundle.getString("wavilon.error.massage.extensions.name.empty"));

        TextField code = new TextField(bundle.getString("wavilon.error.massage.extensions.code"));
        code.setRequired(true);
        code.setRequiredError(bundle.getString("wavilon.error.massage.extensions.code.empty"));
        code.addValidator(new RegexpValidator("[1-9]{1}[0-9]{4}", bundle.getString("wavilon.error.massage.extensions.code.wrong")));

        final TextField destination = new TextField();
        destination.setImmediate(true);
        destination.setVisible(false);

        ComboBox jumpIfBusyComboBox = createJumpComboBox(bundle.getString("wavilon.form.extensions.extension.jump.if.busy"));
        ComboBox jumpIfNoAnswerComboBox = createJumpComboBox(bundle.getString("wavilon.form.extensions.extension.jump.if.no.answer"));


        extensionTypeMap = CouchModelUtil.extensionTypeMapPut(bundle);

        ComboBox extensionType = createExtensionTypeComboBox(form, destination);

        if (null != model.getRevision() && !"".equals(model.getRevision())) {
            name.setValue(extension.getName());
            destination.setValue(extension.getDestination());
            changeDestinationValidator(CouchModelUtil.extensionTypeMapEject(bundle).get(extension.getChannel()), destination, form);
            code.setValue(String.valueOf(extension.getCode()));
        } else {
            code.setValue(String.valueOf(createCode()));
        }

        form.addField("name", name);
        form.addField("code", code);
        form.addField("jumpIfBusyComboBox", jumpIfBusyComboBox);
        form.addField("jumpIfNoAnswerComboBox", jumpIfNoAnswerComboBox);
        form.addField("extensionType", extensionType);
        form.addField("destination", destination);

        return form;
    }

    private ComboBox createExtensionTypeComboBox(final Form form, final TextField destination) {
        ComboBox extensionType = new ComboBox(bundle.getString("wavilon.form.extensions.extension.type"));
        addNullPosition(extensionType);
        extensionType.setImmediate(true);

        for (String s : extensionTypeMap.keySet()) {
            extensionType.addItem(s);
            if (null != extension.getChannel() && extension.getChannel().equals(extensionTypeMap.get(s))) {
                extensionType.setValue(s);
            }
        }

        extensionType.addListener(new ComboBox.ValueChangeListener() {
            public void valueChange(Property.ValueChangeEvent event) {
                String type = (String) event.getProperty().getValue();
                changeDestinationValidator(type, destination, form);
            }
        });
        return extensionType;
    }

    private ComboBox createJumpComboBox(String string) {
        ComboBox jumpComboBox = new ComboBox(string);
        addNullPosition(jumpComboBox);
        List<CouchModelLite> jumpModelList = getJumps();

        for (CouchModelLite couchJumpModel : jumpModelList) {
            jumpComboBox.addItem(couchJumpModel);
        }

        return jumpComboBox;
    }

    private void addNullPosition(ComboBox comboBox) {
        comboBox.addItem(bundle.getString("wavilon.form.select"));
        comboBox.setNullSelectionItemId(bundle.getString("wavilon.form.select"));
    }

    private List<CouchModelLite> getJumps() {
        try {
            return modelLiteService.getAllCouchModelsLite(CouchModelUtil.getOrganizationId(request), CouchTypes.extension);
        } catch (Exception e) {
            return Collections.emptyList();
        }
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
            destination.removeValidator(emailValidator);
            destination.removeValidator(mobileValidator);
            destination.setRequired(false);
            destination.setVisible(false);
        }
    }

    private boolean checkCode(Integer code) {
        try {
            return service.checkCode(model.getId(), model.getLiferayOrganizationId(), code);
        } catch (IOException e) {
            return true;
        }
    }

    private Integer createCode() {
        Random random = new Random();
        Integer code = random.nextInt(99999);

        while ((code < 9999) || checkCode(code)) {
            code = random.nextInt(99999);
        }
        return code;
    }

}
