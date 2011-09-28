package com.aimprosoft.wavilon.ui.menuitems.forms;

import com.aimprosoft.wavilon.application.GenericPortletApplication;
import com.aimprosoft.wavilon.model.VirtualNumber;
import com.aimprosoft.wavilon.service.VirtualNumberDatabaseService;
import com.aimprosoft.wavilon.spring.ObjectFactory;
import com.liferay.portal.util.PortalUtil;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.*;

import javax.portlet.PortletRequest;
import java.util.ResourceBundle;
import java.util.UUID;

public class VirtualNumbersForm extends Window {
    private VirtualNumberDatabaseService service = ObjectFactory.getBean(VirtualNumberDatabaseService.class);
    private ResourceBundle bundle;
    private PortletRequest request;
    private Table table;
    private VirtualNumber virtualNumber;


    public VirtualNumbersForm(ResourceBundle bundle, Table table) {
        this.bundle = bundle;
        this.table = table;
    }

    public void init(String id) {
        request = ((GenericPortletApplication) getApplication()).getPortletRequest();
        virtualNumber = createVirtualNumber(id);

        if ("-1".equals(id)) {
            setCaption("New Virtual Number");
        } else {
            setCaption("Edit Virtual Number");
        }

        VerticalLayout content = new VerticalLayout();
        content.addStyleName("formRegion");

        addComponent(content);

        Label headerForm = createHeader(id, virtualNumber);
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
                    String number = (String)  form.getField("number").getValue();

                    virtualNumber.setName(name);
                    virtualNumber.setNumber(number);

                    service.addVirtualNumber(virtualNumber);

                    if (null != virtualNumber.getRevision()) {
                        table.removeItem(table.getValue());
                        table.select(null);
                    }

                    Object object = table.addItem();
                    table.getContainerProperty(object, "NUMBER").setValue(virtualNumber.getNumber());
                    table.getContainerProperty(object, "NAME").setValue(virtualNumber.getName());
                    table.getContainerProperty(object, "id").setValue(virtualNumber.getId());

                    getWindow().showNotification("Well done");
                    close();
                } catch (Exception ignored) {
                }
            }
        });
        buttons.addComponent(save);
    }

    private VirtualNumber createVirtualNumber(String id) {
        if ("-1".equals(id)) {
            return newVirtualNumber();
        }
        try {
            return service.getVirtualNumber(id);
        } catch (Exception e) {
            return newVirtualNumber();
        }

    }

    private VirtualNumber newVirtualNumber() {
        VirtualNumber newVirtualNumber = new VirtualNumber();

        try {
            newVirtualNumber.setId(UUID.randomUUID().toString());
            newVirtualNumber.setLiferayUserId(PortalUtil.getUserId(request));
            newVirtualNumber.setLiferayOrganizationId(PortalUtil.getScopeGroupId(request));
            newVirtualNumber.setLiferayPortalId(PortalUtil.getCompany(request).getWebId());
        } catch (Exception ignored) {
        }

        newVirtualNumber.setName("");
        newVirtualNumber.setNumber("");
        return newVirtualNumber;
    }

    private Form createForm() {
        Form form = new Form();
        form.addStyleName("labelField");

        TextField name = new TextField("Name");
        name.setRequired(true);
        name.setRequiredError("Empty field \"Name\"");

        TextField number = new TextField("Number");
        number.setRequired(true);
        number.setRequiredError("Empty field \"Number\"");
        number.addValidator(new RegexpValidator("[+][0-9]{10}", "<div align=\"center\">Number must be numeric, begin with + <br/>and consist of 10 digit</div>"));


        if (null != virtualNumber.getRevision() && !"".equals(virtualNumber.getRevision())) {
            name.setValue(virtualNumber.getName());
            number.setValue(virtualNumber.getNumber());
        }
        form.addField("name", name);
        form.addField("number", number);

        return form;
    }

    private Label createHeader(String id, VirtualNumber virtualNumber) {
        Label headerForm = new Label("-1".equals(id) ? "New Virtual Number" : virtualNumber.getName());

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
