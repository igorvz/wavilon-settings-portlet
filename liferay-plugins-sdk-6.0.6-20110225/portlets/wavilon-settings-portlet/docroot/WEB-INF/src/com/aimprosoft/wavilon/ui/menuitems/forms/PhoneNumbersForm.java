package com.aimprosoft.wavilon.ui.menuitems.forms;

import com.aimprosoft.wavilon.application.GenericPortletApplication;
import com.aimprosoft.wavilon.couch.CouchModel;
import com.aimprosoft.wavilon.couch.CouchModelLite;
import com.aimprosoft.wavilon.couch.CouchTypes;
import com.aimprosoft.wavilon.model.PhoneNumber;
import com.aimprosoft.wavilon.service.AllPhoneNumbersDatabaseService;
import com.aimprosoft.wavilon.service.PhoneNumberDatabaseService;
import com.aimprosoft.wavilon.service.VirtualNumberDatabaseService;
import com.aimprosoft.wavilon.spring.ObjectFactory;
import com.aimprosoft.wavilon.util.CouchModelUtil;
import com.liferay.portal.util.PortalUtil;
import com.vaadin.Application;
import com.vaadin.ui.*;

import javax.portlet.PortletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class PhoneNumbersForm extends Window {
    private PhoneNumberDatabaseService service = ObjectFactory.getBean(PhoneNumberDatabaseService.class);
    private VirtualNumberDatabaseService virtualNumberDatabaseService = ObjectFactory.getBean(VirtualNumberDatabaseService.class);
    private AllPhoneNumbersDatabaseService allPhonesService = ObjectFactory.getBean(AllPhoneNumbersDatabaseService.class);
    private ResourceBundle bundle;
    private PortletRequest request;
    private Table table;
    private PhoneNumber phoneNumber;
    private Application application;
    private CouchModel model;
    private String anotherPhoneId = "";


    public PhoneNumbersForm(ResourceBundle bundle, Table table) {
        this.bundle = bundle;
        this.table = table;
    }

    public void init(String id, final Object itemId) {
        request = ((GenericPortletApplication) getApplication()).getPortletRequest();
        application = getApplication();
        model = createModel(id);
        phoneNumber = new PhoneNumber();
        if ("-1".equals(id)) {
            setCaption(bundle.getString("wavilon.form.phonenumbers.new.phone.number"));
        } else {
            setCaption(bundle.getString("wavilon.form.phonenumbers.edit.phone.number"));
        }

        VerticalLayout content = new VerticalLayout();
        content.addStyleName("formRegion");

        content.setSizeFull();
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
                    CouchModelLite forwardCallTo = ((CouchModelLite) form.getField("forwardCallTo").getValue());
                    try {
                        CouchModel virtualNumber = ((CouchModel) form.getField("number").getValue());
                        phoneNumber.setLocator((String) virtualNumber.getProperties().get("locator"));

                    } catch (Exception e) {
                        String virtualNumber = (String) form.getField("number").getValue();
                        phoneNumber.setLocator(virtualNumber);

                    }


                    phoneNumber.setName(name);

                    model.setType(CouchTypes.service);

                    service.addPhoneNumber(phoneNumber, model, forwardCallTo.getId());

                    if (!"".equals(anotherPhoneId)) {
                        allPhonesService.updateModel(model.getLiferayOrganizationId(), anotherPhoneId);
                    }

                    if (null != model.getRevision() || !"".equals(anotherPhoneId)) {
                        table.removeItem(itemId);
                        table.select(null);
                    }

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

                    table.getContainerProperty(object, bundle.getString("wavilon.table.phonenumbers.column.number")).setValue(phoneNumber.getLocator());
                    table.getContainerProperty(object, bundle.getString("wavilon.table.phonenumbers.column.name")).setValue(phoneNumber.getName());
                    table.getContainerProperty(object, bundle.getString("wavilon.table.phonenumbers.column.forward.calls.to")).setValue(forwardCallTo.getName());
                    table.getContainerProperty(object, "id").setValue(model.getId());
                    Button removeButton = new Button("", listener);
                    removeButton.addStyleName("removeButton");
                    table.getContainerProperty(object, "").setValue(removeButton);

                    getWindow().showNotification(bundle.getString("wavilon.well.done"));
                    close();
                } catch (Exception ignored) {
                }
            }
        }
        );
        save.addStyleName("saveButton");
        buttons.addComponent(save);
    }

    private Form createForm() {
        Form form = new Form();
        form.addStyleName("labelField");

        TextField name = new TextField(bundle.getString("wavilon.form.name"));
        name.setRequired(true);
        name.setRequiredError(bundle.getString("wavilon.error.massage.phonenumbers.name.empty"));
        form.addField("name", name);


        List<CouchModelLite> forwards = createForwards();
        ComboBox forwardCallTo = new ComboBox(bundle.getString("wavilon.form.phonenumbers.forward.calls.to"));
        forwardCallTo.addItem(bundle.getString("wavilon.form.select"));
        for (CouchModelLite forward : forwards) {
            forwardCallTo.addItem(forward);
        }
        forwardCallTo.setNullSelectionItemId(bundle.getString("wavilon.form.select"));
        forwardCallTo.setRequired(true);
        forwardCallTo.setRequiredError(bundle.getString("wavilon.error.massage.phonenumbers.forward.empty"));

        if ((null != this.model.getRevision() && !"".equals(this.model.getRevision())) || null!= model.getProperties()) {
            name.setValue(model.getProperties().get("name"));

            TextField number = new TextField(bundle.getString("wavilon.form.number"));
            number.setValue(model.getProperties().get("locator"));
            number.setReadOnly(true);
            number.setRequiredError(bundle.getString("wavilon.error.massage.phonenumbers.number.empty"));

            form.addField("number", number);

            form.addField("forwardCallTo", forwardCallTo);

        } else {
            List<CouchModel> virtualNumbers = createVirtualNumbers();
            ComboBox numbers = new ComboBox(bundle.getString("wavilon.form.number"));
            numbers.addItem(bundle.getString("wavilon.form.select"));

            for (CouchModel number : virtualNumbers) {
                numbers.addItem(number);
            }
            numbers.setNullSelectionItemId(bundle.getString("wavilon.form.select"));
            numbers.setRequired(true);
            numbers.setRequiredError(bundle.getString("wavilon.error.massage.phonenumbers.number.empty"));

            form.addField("number", numbers);

            form.addField("forwardCallTo", forwardCallTo);


            String noteString = bundle.getString("wavilon.form.phonenumbers.note.part.first") +
                    "\u20ac " + bundle.getString("wavilon.form.phonenumbers.note.part.second") +
                    "\u20ac " + bundle.getString("wavilon.form.phonenumbers.note.part.three");
            TextArea note = new TextArea("Note: ", noteString);
            note.setReadOnly(true);

            form.addField("note", note);
        }

        return form;
    }

    private List<CouchModel> createVirtualNumbers() {

        return getVirtualNumbers();
    }

    private List<CouchModelLite> createForwards() {
        return getForwards();
    }

    private HorizontalLayout createButtons(VerticalLayout content) {
        HorizontalLayout buttons = new HorizontalLayout();
        content.addComponent(buttons);
        buttons.addStyleName("buttonsPanel");
        content.setComponentAlignment(buttons, Alignment.BOTTOM_RIGHT);

        return buttons;
    }

    private CouchModel createModel(String id) {
        if ("-1".equals(id)) {
            return CouchModelUtil.newCouchModel(request, CouchTypes.startnode);
        }
        try {
            return service.getModel(id);
        } catch (Exception e) {
            try {
                CouchModel anotherCouchModel = allPhonesService.getPhoneNumber(id);
                anotherPhoneId = anotherCouchModel.getId();
                CouchModel nativeCouchModel = CouchModelUtil.newCouchModel(request, CouchTypes.startnode);
                Map<String, Object> props = anotherCouchModel.getProperties();
                props.put("name", "");
                nativeCouchModel.setProperties(props);
                nativeCouchModel.setOutputs(null);

                return nativeCouchModel;

            } catch (Exception ignored) {
                return CouchModelUtil.newCouchModel(request, CouchTypes.startnode);
            }
        }
    }

    private List<CouchModelLite> getForwards() {
        try {

            return CouchModelUtil.getForwards(PortalUtil.getUserId(request), PortalUtil.getScopeGroupId(request));
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    private List<CouchModel> getVirtualNumbers() {
        try {
            return virtualNumberDatabaseService.getAllUsersCouchModelToVirtualNumber(PortalUtil.getUserId(request), PortalUtil.getScopeGroupId(request));
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}
