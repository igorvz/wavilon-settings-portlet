package com.aimprosoft.wavilon.ui.menuitems.forms;

import com.aimprosoft.wavilon.application.GenericPortletApplication;
import com.aimprosoft.wavilon.couch.CouchModel;
import com.aimprosoft.wavilon.couch.CouchTypes;
import com.aimprosoft.wavilon.model.PhoneNumber;
import com.aimprosoft.wavilon.service.ExtensionDatabaseService;
import com.aimprosoft.wavilon.service.PhoneNumberDatabaseService;
import com.aimprosoft.wavilon.service.VirtualNumberDatabaseService;
import com.aimprosoft.wavilon.spring.ObjectFactory;
import com.aimprosoft.wavilon.util.CouchModelUtil;
import com.liferay.portal.util.PortalUtil;
import com.vaadin.Application;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.*;

import javax.portlet.PortletRequest;
import java.util.*;

public class PhoneNumbersForm extends Window {
    private PhoneNumberDatabaseService service = ObjectFactory.getBean(PhoneNumberDatabaseService.class);
    private VirtualNumberDatabaseService virtualNumberService = ObjectFactory.getBean(VirtualNumberDatabaseService.class);
    private ExtensionDatabaseService extensionService = ObjectFactory.getBean(ExtensionDatabaseService.class);
    private ResourceBundle bundle;
    private PortletRequest request;
    private Table table;
    private PhoneNumber phoneNumber;
    private Application application;
    private CouchModel model;


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
            setCaption("New Phone Number");
        } else {
            setCaption("Edit Phone Number");
        }

        VerticalLayout content = new VerticalLayout();
        content.addStyleName("formRegion");

        content.setSizeFull();
        addComponent(content);

        Label headerForm = createHeader(id, phoneNumber);
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
                    CouchModel forwardCallTo = ((CouchModel) form.getField("forwardCallTo").getValue());
                    CouchModel virtualNumber = ((CouchModel) form.getField("numbers").getValue());

                    if (null != model.getRevision()) {
                        table.removeItem(itemId);
                        table.select(null);
                    }

                    Map<String, Object> extensionOut = new HashMap<String, Object>();
                    extensionOut.put("extension", forwardCallTo.getId());

                    phoneNumber.setLocator((String) virtualNumber.getProperties().get("locator"));
                    phoneNumber.setName(name);

                    model.setType(CouchTypes.service);
                    model.setOutputs(extensionOut);

                    service.addPhoneNumber(phoneNumber, model);

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

                    table.getContainerProperty(object, "NUMBER").setValue(phoneNumber.getLocator());
                    table.getContainerProperty(object, "NAME").setValue(phoneNumber.getName());
                    table.getContainerProperty(object, "FORWARD CALLS TO").setValue(forwardCallTo.getProperties().get("name"));
                    table.getContainerProperty(object, "id").setValue(model.getId());
                    table.getContainerProperty(object, "").setValue(new Button("-", listener));

                    getWindow().showNotification("Well done");
                    close();
                } catch (Exception ignored) {
                }
            }
        }

        );
        buttons.addComponent(save);
    }

    private Form createForm() {
        Form form = new Form();
        form.addStyleName("labelField");

        TextField name = new TextField("Name");
        name.setRequired(true);
        name.setRequiredError("Empty field name");
        form.addField("name", name);


        List<CouchModel> forwards = createExtensions();
        ComboBox forwardCallTo = new ComboBox("Forward calls to");
        forwardCallTo.addItem("Select . . .");
        for (CouchModel forward : forwards) {
            forwardCallTo.addItem(forward);
        }
        forwardCallTo.setImmediate(true);
        forwardCallTo.setNullSelectionItemId("Select . . .");
        forwardCallTo.setRequired(true);
        name.setRequiredError("Empty field \"Forward calls to\"");

        if (null != this.model.getRevision() && !"".equals(this.model.getRevision())) {
            name.setValue(model.getProperties().get("name"));

            TextField number = new TextField("Number");
            number.setValue(model.getProperties().get("locator"));
            number.setReadOnly(true);

            form.addField("phoneNumber", number);

            form.addField("forwardCallTo", forwardCallTo);

        } else {
            List<CouchModel> virtualNumbers = createVirtualNumbers();
            ComboBox numbers = new ComboBox("Number", virtualNumbers);
            numbers.setImmediate(true);
            numbers.setNullSelectionAllowed(false);
            numbers.setNullSelectionItemId("Select . . .");
            numbers.setRequired(true);
            name.setRequiredError("Empty field \"Number\"");

            form.addField("numbers", numbers);

            form.addField("forwardCallTo", forwardCallTo);


            String noteString = "The selected phone number has a" +
                    "monthly cost of 0.00€ Clicking save" +
                    "you are accepting that the amount" +
                    "of 0.00€ will be charged monthly ets.";
            TextArea note = new TextArea("Note", noteString);
            note.setReadOnly(true);

            form.addField("note", note);
        }


        return form;
    }

    private List<CouchModel> createVirtualNumbers() {

        return getVirtualNumbers();
    }

    private List<CouchModel> createExtensions() {
        return getExtensions();
    }

    private Label createHeader(String id, PhoneNumber phoneNumber) {
        Label headerForm = new Label("-1".equals(id) ? "New Phone Number" : phoneNumber.getName());

        headerForm.setHeight(27, Sizeable.UNITS_PIXELS);
        headerForm.setWidth("100%");
        headerForm.addStyleName("headerForm");

        return headerForm;
    }

    private HorizontalLayout createButtons(VerticalLayout content) {
        HorizontalLayout buttons = new HorizontalLayout();
        content.addComponent(buttons);
        buttons.addStyleName("buttonsPanel");
        content.setComponentAlignment(buttons, Alignment.BOTTOM_RIGHT);

        return buttons;
    }

    private CouchModel createModel(String id) {
        try {
            return service.getModel(id);
        } catch (Exception e) {
            return CouchModelUtil.newCouchModel(request, CouchTypes.service);
        }
    }

    private List<CouchModel> getExtensions() {
        try {

            return extensionService.getAllUsersCouchModelToExtension(PortalUtil.getUserId(request), PortalUtil.getScopeGroupId(request));
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    private List<CouchModel> getVirtualNumbers() {
        try {

            return virtualNumberService.getAllUsersCouchModelToVirtualNumber(PortalUtil.getUserId(request), PortalUtil.getScopeGroupId(request));
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}
