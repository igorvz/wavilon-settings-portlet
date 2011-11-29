package com.aimprosoft.wavilon.ui.menuitems.forms;

import com.aimprosoft.wavilon.couch.CouchModel;
import com.aimprosoft.wavilon.couch.CouchModelLite;
import com.aimprosoft.wavilon.couch.CouchTypes;
import com.aimprosoft.wavilon.model.VirtualNumber;
import com.aimprosoft.wavilon.service.AllPhoneNumbersDatabaseService;
import com.aimprosoft.wavilon.service.VirtualNumberDatabaseService;
import com.aimprosoft.wavilon.spring.ObjectFactory;
import com.aimprosoft.wavilon.util.CouchModelUtil;
import com.aimprosoft.wavilon.util.LayoutUtil;
import com.vaadin.ui.*;

import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

public class VirtualNumbersForm extends GeneralForm {
    private VirtualNumberDatabaseService service = ObjectFactory.getBean(VirtualNumberDatabaseService.class);
    private AllPhoneNumbersDatabaseService allPhonesService = ObjectFactory.getBean(AllPhoneNumbersDatabaseService.class);
    private VirtualNumber virtualNumber;

    public VirtualNumbersForm(ResourceBundle bundle, Table table) {
        super(bundle, table);
    }

    @Override
    public void init(String id, final Object itemId) {
        super.init(id, itemId);
        model = createCoucModel(id, service, CouchTypes.startnode);
        virtualNumber = createVirtualNumber(model);

        if ("-1".equals(id)) {
            setCaption(bundle.getString("wavilon.form.virtualnumbers.new.phone.number"));
        } else {
            setCaption(bundle.getString("wavilon.form.virtualnumbers.edit.phone.number"));
        }
        final Form form = createForm();

        initForm(form, new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                try {
                    form.commit();

                    String name = (String) form.getField("name").getValue();
                    String number = (String) form.getField("number").getValue();
                    Boolean recordCalls = (Boolean) form.getField("recordCalls").getValue();
                    CouchModelLite forwardCallTo = null;
                    String forwardId = null;

                    if (null != form.getField("forwardCallTo").getValue()) {
                        forwardCallTo = ((CouchModelLite) form.getField("forwardCallTo").getValue());
                        forwardId = forwardCallTo.getId();
                    }

                    if (!recordCalls) {
                        virtualNumber.setRecordCalls(null);
                    }

                    virtualNumber.setName(name);
                    virtualNumber.setLocator(number);
                    virtualNumber.setForwardTo(forwardId);


                    service.addVirtualNumber(virtualNumber, model);

                    try {
                        String phoneModelDocId = allPhonesService.getVirtualNumbersDocumentId(number);
                        allPhonesService.updateModelsAllocationDate(model.getLiferayOrganizationId(), phoneModelDocId);
                    } catch (Exception ignored) {
                    }

                    if (null != model.getRevision()) {
                        table.removeItem(itemId);
                        table.select(null);
                    }

                    final Object object = table.addItem();

                    table.getContainerProperty(object, bundle.getString("wavilon.table.virtualnumbers.column.number")).setValue(model.getProperties().get("locator"));
                    table.getContainerProperty(object, bundle.getString("wavilon.table.virtualnumbers.column.name")).setValue(virtualNumber.getName());
                    table.getContainerProperty(object, "id").setValue(model.getId());
                    table.getContainerProperty(object, bundle.getString("wavilon.table.virtualnumbers.column.forward.calls.to")).setValue(CouchModelUtil.getCouchModelLite(forwardId, bundle));
                    table.getContainerProperty(object, "").setValue(createTablesEditRemoveButtons(table, object, model, virtualNumber.getLocator()));

                    LayoutUtil.setTableBackground(table, CouchTypes.startnode);

                    getParent().getWindow().showNotification(bundle.getString("wavilon.well.done"));
                    close();

                } catch (Exception ignored) {
                }
            }
        });
    }

    private VirtualNumber createVirtualNumber(CouchModel model) {
        if (null == model.getRevision()) {
            return newVirtualNumber();
        }
        try {
            return getModel(model, service, VirtualNumber.class);
        } catch (Exception e) {
            return newVirtualNumber();
        }
    }

    private VirtualNumber newVirtualNumber() {
        VirtualNumber virtualNumber = new VirtualNumber();
        virtualNumber.setName("");
        return virtualNumber;
    }

    private Form createForm() {
        Form form = new Form();
        form.addStyleName("labelField");

        TextField name = new TextField(bundle.getString("wavilon.form.name"));
        name.setRequired(true);
        name.setRequiredError(bundle.getString("wavilon.error.massage.virtualnumbers.name.empty"));
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

        if ((null != model.getRevision() && !"".equals(model.getRevision())) || null != model.getProperties()) {
            name.setValue(virtualNumber.getName());

            TextField number = new TextField(bundle.getString("wavilon.form.number"));
            number.setValue(virtualNumber.getLocator());
            number.setReadOnly(true);
            number.setRequiredError(bundle.getString("wavilon.error.massage.phonenumbers.number.empty"));

            if (null != virtualNumber.getRecordCalls()) {
                recordCalls.setValue(true);
            }

            if (null != virtualNumber.getForwardTo()) {
                forwardCallTo.setValue(CouchModelUtil.getCouchModelLite(virtualNumber.getForwardTo(), bundle));
            }

            form.addField("number", number);
            form.addField("forwardCallTo", forwardCallTo);
        } else {
            List<String> virtualNumbers = createVirtualNumbers();
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
        }


        TextField cost = new TextField(bundle.getString("wavilon.form.virtualnumbers.cost"));
        cost.setValue("0.10 $");
        cost.setReadOnly(true);

        form.addField("cost", cost);
        form.addField("recordCalls", recordCalls);

        return form;
    }

    private List<String> createVirtualNumbers() {
        try {
            return allPhonesService.getOnlyVirtualNumbers(CouchModelUtil.getOrganizationId(request));
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }


}
