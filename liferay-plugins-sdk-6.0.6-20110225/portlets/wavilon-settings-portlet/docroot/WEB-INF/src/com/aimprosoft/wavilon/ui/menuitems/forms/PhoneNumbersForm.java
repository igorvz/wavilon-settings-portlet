package com.aimprosoft.wavilon.ui.menuitems.forms;

import com.aimprosoft.wavilon.application.GenericPortletApplication;
import com.aimprosoft.wavilon.couch.CouchModel;
import com.aimprosoft.wavilon.couch.CouchModelLite;
import com.aimprosoft.wavilon.couch.CouchTypes;
import com.aimprosoft.wavilon.model.PhoneNumber;
import com.aimprosoft.wavilon.service.PhoneNumberDatabaseService;
import com.aimprosoft.wavilon.service.VirtualNumberDatabaseService;
import com.aimprosoft.wavilon.spring.ObjectFactory;
import com.aimprosoft.wavilon.util.CouchModelUtil;
import com.liferay.portal.util.PortalUtil;
import com.vaadin.Application;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.*;

import javax.portlet.PortletRequest;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

public class PhoneNumbersForm extends Window {
    private PhoneNumberDatabaseService service = ObjectFactory.getBean(PhoneNumberDatabaseService.class);
    private VirtualNumberDatabaseService virtualNumberDatabaseService = ObjectFactory.getBean(VirtualNumberDatabaseService.class);
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

//        Label headerForm = createHeader(id, phoneNumber);
//        content.addComponent(headerForm);

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
                    CouchModelLite forwardCallTo = ((CouchModelLite) form.getField("forwardCallTo").getValue());
                    try {

                        CouchModel virtualNumber = ((CouchModel) form.getField("number").getValue());
                        phoneNumber.setLocator((String) virtualNumber.getProperties().get("locator"));

                    } catch (Exception e) {
                        String virtualNumber = (String) form.getField("number").getValue();
                        phoneNumber.setLocator(virtualNumber);

                    }
                    if (null != model.getRevision()) {
                        table.removeItem(itemId);
                        table.select(null);
                    }

                    phoneNumber.setName(name);

                    model.setType(CouchTypes.service);

                    service.addPhoneNumber(phoneNumber, model, forwardCallTo.getId());

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
                    table.getContainerProperty(object, "FORWARD CALLS TO").setValue(forwardCallTo.getName());
                    table.getContainerProperty(object, "id").setValue(model.getId());
                    Button removeButton = new Button("", listener);
                    removeButton.addStyleName("removeButton");
                    table.getContainerProperty(object, "").setValue(removeButton);

                    getWindow().showNotification("Well done");
                    close();
                } catch (Exception ignored) {
                }
            }
        });
        buttons.addComponent(save);
    }

    private Form createForm() {
        Form form = new Form();
        form.addStyleName("labelField");

        TextField name = new TextField("Name");
        name.setRequired(true);
        name.setRequiredError("Empty field name");
        form.addField("name", name);


        List<CouchModelLite> forwards = createForwards();
        ComboBox forwardCallTo = new ComboBox("Forward calls to");
        forwardCallTo.addItem("Select . . .");
        for (CouchModelLite forward : forwards) {
            forwardCallTo.addItem(forward);
        }
        forwardCallTo.setNullSelectionItemId("Select . . .");
        forwardCallTo.setRequired(true);
        name.setRequiredError("Empty field \"Forward calls to\"");

        if (null != this.model.getRevision() && !"".equals(this.model.getRevision())) {
            name.setValue(model.getProperties().get("name"));

            TextField number = new TextField("Number");
            number.setValue(model.getProperties().get("locator"));
            number.setReadOnly(true);

            form.addField("number", number);

            form.addField("forwardCallTo", forwardCallTo);

        } else {
            List<CouchModel> virtualNumbers = createVirtualNumbers();
            ComboBox numbers = new ComboBox("Number");
            numbers.addItem("Select . . .");

            for (CouchModel number : virtualNumbers) {
                numbers.addItem(number);
            }
            numbers.setNullSelectionItemId("Select . . .");
            numbers.setRequired(true);
            name.setRequiredError("Empty field \"Number\"");

            form.addField("number", numbers);

            form.addField("forwardCallTo", forwardCallTo);


            String noteString = "The selected phone number has a " +
                    "monthly cost of 0.00\u20ac Clicking save " +
                    "you are accepting that the amount " +
                    "of 0.00\u20ac will be charged monthly ets.";
            TextArea note = new TextArea("Note: ",  noteString);
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
