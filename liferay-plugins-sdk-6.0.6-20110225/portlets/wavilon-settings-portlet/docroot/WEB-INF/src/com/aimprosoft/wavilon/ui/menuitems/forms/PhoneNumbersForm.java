package com.aimprosoft.wavilon.ui.menuitems.forms;

import com.aimprosoft.wavilon.application.GenericPortletApplication;
import com.aimprosoft.wavilon.model.PhoneNumber;
import com.aimprosoft.wavilon.service.PhoneNumberDatabaseService;
import com.aimprosoft.wavilon.spring.ObjectFactory;
import com.liferay.portal.util.PortalUtil;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.*;

import javax.portlet.PortletRequest;
import java.util.ResourceBundle;
import java.util.UUID;

public class PhoneNumbersForm extends Window {
    private static PhoneNumberDatabaseService service = ObjectFactory.getBean(PhoneNumberDatabaseService.class);
    private ResourceBundle bundle;
    private PortletRequest request;
    private Table table;
    private PhoneNumber phoneNumber;


    public PhoneNumbersForm(ResourceBundle bundle, Table table) {
        this.bundle = bundle;
        this.table = table;
        setCaption("Edit Phone Number");
    }

    public void init(String id) {
        request = ((GenericPortletApplication) getApplication()).getPortletRequest();
        phoneNumber = createPhoneNumber(id);

        VerticalLayout content = new VerticalLayout();
        content.addStyleName("formRegion");

        content.setWidth(280, Sizeable.UNITS_PIXELS);
        content.setHeight(200, Sizeable.UNITS_PIXELS);
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
                    phoneNumber.setName(name);
                    service.addPhoneNumber(phoneNumber);

                    if (null != phoneNumber.getRevision()){
                        table.removeItem(table.getValue());
                        table.select(null);
                    }

                    Object object = table.addItem();
                    table.getContainerProperty(object, "<=>").setValue(phoneNumber.getName());
                    table.getContainerProperty(object, "id").setValue(phoneNumber.getId());

                    getWindow().showNotification("Well done");
                    close();
                } catch (Exception ignored) {
                }
            }
        });
        buttons.addComponent(save);
    }

    private PhoneNumber createPhoneNumber(String id) {
        if ("-1".equals(id)) {
            return newPhoneNumber();
        }
        try {
            return service.getPhoneNumber(id);
        } catch (Exception e) {
            return newPhoneNumber();
        }

    }

    private PhoneNumber newPhoneNumber() {
        PhoneNumber newPhoneNumber = new PhoneNumber();

        try {
            newPhoneNumber.setId(UUID.randomUUID().toString());
            newPhoneNumber.setLiferayUserId(PortalUtil.getUserId(request));
            newPhoneNumber.setLiferayOrganizationId(PortalUtil.getScopeGroupId(request));
            newPhoneNumber.setLiferayPortalId(PortalUtil.getCompany(request).getWebId());
        } catch (Exception ignored) {
        }

        newPhoneNumber.setName("");
        return newPhoneNumber;
    }

    private Form createForm() {
        Form form = new Form();
        form.addStyleName("labelField");

        TextField name = new TextField("Name");
//        name.setRequired(true);
//        name.setRequiredError(bundle.getString("Empty field name"));
//        name.addValidator(new RegexpValidator("[+][0-9]{10}", "Mobile must be numeric and begin with +"));

        if (null != phoneNumber.getRevision() && !"".equals(phoneNumber.getRevision())) {
            name.setValue(phoneNumber.getName());
        }
        form.addField("name", name);

        return form;
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
        content.setComponentAlignment(buttons, Alignment.BOTTOM_RIGHT);

        return buttons;
    }

}
