package com.aimprosoft.wavilon.ui.menuitems.forms;

import com.aimprosoft.wavilon.application.GenericPortletApplication;
import com.aimprosoft.wavilon.model.PhoneNumber;
import com.aimprosoft.wavilon.model.VirtualNumber;
import com.aimprosoft.wavilon.service.PhoneNumberDatabaseService;
import com.aimprosoft.wavilon.service.VirtualNumberDatabaseService;
import com.aimprosoft.wavilon.spring.ObjectFactory;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.util.PortalUtil;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.*;

import javax.portlet.PortletRequest;
import java.io.IOException;
import java.util.*;

public class PhoneNumbersForm extends Window {
    private PhoneNumberDatabaseService service = ObjectFactory.getBean(PhoneNumberDatabaseService.class);
    private VirtualNumberDatabaseService VNService = ObjectFactory.getBean(VirtualNumberDatabaseService.class);
    private ResourceBundle bundle;
    private PortletRequest request;
    private Table table;
    private PhoneNumber phoneNumber;


    public PhoneNumbersForm(ResourceBundle bundle, Table table) {
        this.bundle = bundle;
        this.table = table;
    }

    public void init(String id) {
        request = ((GenericPortletApplication) getApplication()).getPortletRequest();
        phoneNumber = createPhoneNumber(id);

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


                    if (null != phoneNumber.getRevision()) {
                        table.removeItem(table.getValue());
                        table.select(null);
                    }   else {
                        String number = (String) form.getField("numbers").getValue();
                        phoneNumber.setNumber(number);
                    }

                    phoneNumber.setName(name);
                    service.addPhoneNumber(phoneNumber);
                    Object object = table.addItem();
                    table.getContainerProperty(object, "NUMBER").setValue(phoneNumber.getNumber());
                    table.getContainerProperty(object, "NAME").setValue(phoneNumber.getName());
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
        newPhoneNumber.setNumber("");
        return newPhoneNumber;
    }

    private Form createForm() {
        Form form = new Form();
        form.addStyleName("labelField");

        TextField name = new TextField("Name");
        name.setRequired(true);
        name.setRequiredError("Empty field name");
        form.addField("name", name);

        if (null != this.phoneNumber.getRevision() && !"".equals(this.phoneNumber.getRevision())) {
            name.setValue(phoneNumber.getName());

            TextField number = new TextField("Number");
            number.setValue(this.phoneNumber.getNumber());
            number.setReadOnly(true);

            form.addField("phoneNumber", number);





        } else {
            List<String> virtualNumbers = createVirtualNumbers();
            ComboBox numbers = new ComboBox("Number", virtualNumbers);
            numbers.setImmediate(true);
            numbers.setNullSelectionAllowed(false);
            numbers.setNullSelectionItemId("Select . . .");
            numbers.setRequired(true);
            name.setRequiredError("Empty field \"Number\"");

            form.addField("numbers", numbers);



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

    private List<String> createVirtualNumbers() {
        List<String> numbers = new LinkedList<String>();
        List<VirtualNumber> virtualNumbers = getVirtualNumbers();

        if(!virtualNumbers.isEmpty()){
            for (VirtualNumber virtualNumber : virtualNumbers) {
                numbers.add(virtualNumber.getNumber());
            }
        }
        Collections.sort(numbers);
        numbers.add(0, "Select . . .");
        return numbers;
    }

    private List<VirtualNumber> getVirtualNumbers() {
        try {
            return VNService.getAllVirtualNumbersByUser(PortalUtil.getUserId(request), PortalUtil.getScopeGroupId(request));
        } catch (Exception e) {
            return Collections.emptyList();
        }

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

}
