package com.aimprosoft.wavilon.ui.menuitems.forms;

import com.aimprosoft.wavilon.couch.CouchModel;
import com.aimprosoft.wavilon.couch.CouchModelLite;
import com.aimprosoft.wavilon.couch.CouchTypes;
import com.aimprosoft.wavilon.model.PhoneNumber;
import com.aimprosoft.wavilon.service.AllPhoneNumbersDatabaseService;
import com.aimprosoft.wavilon.service.PhoneNumberDatabaseService;
import com.aimprosoft.wavilon.spring.ObjectFactory;
import com.aimprosoft.wavilon.util.CouchModelUtil;
import com.aimprosoft.wavilon.util.LayoutUtil;
import com.vaadin.ui.*;

import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

public class PhoneNumbersForm extends GeneralForm {
    private PhoneNumberDatabaseService service = ObjectFactory.getBean(PhoneNumberDatabaseService.class);
    private AllPhoneNumbersDatabaseService allPhonesService = ObjectFactory.getBean(AllPhoneNumbersDatabaseService.class);
    private PhoneNumber phoneNumber;

    public PhoneNumbersForm(ResourceBundle bundle, Table table) {
        super(bundle, table);
    }

    @Override
    public void init(String id, final Object itemId) {
        super.init(id, itemId);
        model = createCoucModel(id, service, CouchTypes.service);
        phoneNumber = createPhoneNumber(model);

        if ("-1".equals(id)) {
            setCaption(bundle.getString("wavilon.form.phonenumbers.new.phone.number"));
        } else {
            setCaption(bundle.getString("wavilon.form.phonenumbers.edit.phone.number"));
        }
        final Form form = createForm();

        initForm(form, new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                try {
                    form.commit();

                    String name = (String) form.getField("name").getValue();
                    String number = (String) form.getField("number").getValue();
                    Boolean recordCalls = (Boolean) form.getField("recordCalls").getValue();

                    String forwardId = null;
                    if (null != form.getField("forwardCallTo").getValue()) {
                        CouchModelLite forwardCallTo = ((CouchModelLite) form.getField("forwardCallTo").getValue());
                        forwardId = forwardCallTo.getId();
                    }

                    if (!recordCalls) {
                        phoneNumber.setRecordCalls(null);
                    }else {
                        phoneNumber.setRecordCalls("yes");
                    }

                    phoneNumber.setLocator(number);
                    phoneNumber.setName(name);


                    service.addPhoneNumber(phoneNumber, model, forwardId);


                    String phoneModelDocId = allPhonesService.getPhoneNumbersDocumentId(number);
                    allPhonesService.updateModelsAllocationDate(model.getLiferayOrganizationId(), phoneModelDocId);


                    if (null != model.getRevision()) {
                        table.removeItem(itemId);
                        table.select(null);
                    }

                    final Object object = table.addItem();

                    table.getContainerProperty(object, bundle.getString("wavilon.table.phonenumbers.column.number")).setValue(phoneNumber.getLocator());
                    table.getContainerProperty(object, bundle.getString("wavilon.table.phonenumbers.column.name")).setValue(phoneNumber.getName());
                    table.getContainerProperty(object, bundle.getString("wavilon.table.phonenumbers.column.forward.calls.to")).setValue(CouchModelUtil.getCouchModelLite(forwardId, bundle));
                    table.getContainerProperty(object, "id").setValue(model.getId());
                    table.getContainerProperty(object, "").setValue(createTablesEditRemoveButtons(table, object, model, phoneNumber.getLocator()));

                    LayoutUtil.setTableBackground(table, CouchTypes.service);

                    getParent().getWindow().showNotification(bundle.getString("wavilon.well.done"));
                    close();
                } catch (Exception ignored) {
                }
            }
        });
    }

    private PhoneNumber createPhoneNumber(CouchModel model) {
        if (null == model.getRevision()) {
            return newPhoneNumber();
        }
        try {
            return getModel(model, service, PhoneNumber.class);
        } catch (Exception e) {
            return newPhoneNumber();
        }
    }

    private PhoneNumber newPhoneNumber() {
        PhoneNumber phoneNumber = new PhoneNumber();
        phoneNumber.setName("");
        return phoneNumber;
    }

    private Form createForm() {
        Form form = new Form();
        form.addStyleName("labelField");

        TextField name = new TextField(bundle.getString("wavilon.form.name"));
        name.setRequired(true);
        name.setRequiredError(bundle.getString("wavilon.error.massage.phonenumbers.name.empty"));
        form.addField("name", name);

        CheckBox recordCalls = new CheckBox(bundle.getString("wavilon.form.record.calls"));
        recordCalls.addStyleName("recordCalls");

        List<CouchModelLite> forwards = getForwards();
        ComboBox forwardCallTo = new ComboBox(bundle.getString("wavilon.form.phonenumbers.forward.calls.to"));
        forwardCallTo.addItem(bundle.getString("wavilon.form.select"));
        for (CouchModelLite forward : forwards) {
            forwardCallTo.addItem(forward);
        }
        forwardCallTo.setNullSelectionItemId(bundle.getString("wavilon.form.select"));

        if ((null != this.model.getRevision() && !"".equals(this.model.getRevision())) || null != model.getProperties()) {
            name.setValue(phoneNumber.getName());

            TextField number = new TextField(bundle.getString("wavilon.form.number"));
            number.setValue(phoneNumber.getLocator());
            number.setReadOnly(true);
            number.setRequiredError(bundle.getString("wavilon.error.massage.phonenumbers.number.empty"));

            form.addField("number", number);

            form.addField("forwardCallTo", forwardCallTo);

            if (null != phoneNumber.getRecordCalls()) {
                recordCalls.setValue(true);
            }

            if (null != model.getOutputs()) {
                if (null != model.getOutputs().get("startnode")) {
                    String forwardsId = (String) model.getOutputs().get("startnode");
                    forwardCallTo.setValue(CouchModelUtil.getCouchModelLite(forwardsId, bundle));
                }
            }


        } else {
            List<String> virtualNumbers = createGeoNumbers();
            ComboBox numbers = new ComboBox(bundle.getString("wavilon.form.number"));
            numbers.addItem(bundle.getString("wavilon.form.select"));

            for (String number : virtualNumbers) {
                numbers.addItem(number);
            }
            numbers.setNullSelectionItemId(bundle.getString("wavilon.form.select"));
            numbers.setRequired(true);
            numbers.setRequiredError(bundle.getString("wavilon.error.massage.phonenumbers.number.empty"));

            form.addField("number", numbers);

            form.addField("forwardCallTo", forwardCallTo);

            TextArea note = new TextArea("Note: ", bundle.getString("wavilon.form.phonenumbers.note.message"));
            note.setReadOnly(true);

            form.addField("note", note);
        }

        form.addField("recordCalls", recordCalls);

        return form;
    }

    private List<String> createGeoNumbers() {
        try {
            return allPhonesService.getOnlyPhoneNumbers(CouchModelUtil.getOrganizationId(request));
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

}
