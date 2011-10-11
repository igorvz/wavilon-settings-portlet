package com.aimprosoft.wavilon.ui.menuitems.forms;

import com.aimprosoft.wavilon.application.GenericPortletApplication;
import com.aimprosoft.wavilon.couch.CouchModel;
import com.aimprosoft.wavilon.couch.CouchModelLite;
import com.aimprosoft.wavilon.couch.CouchTypes;
import com.aimprosoft.wavilon.model.VirtualNumber;
import com.aimprosoft.wavilon.service.VirtualNumberDatabaseService;
import com.aimprosoft.wavilon.spring.ObjectFactory;
import com.aimprosoft.wavilon.util.CouchModelUtil;
import com.liferay.portal.util.PortalUtil;
import com.vaadin.Application;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.*;

import javax.portlet.PortletRequest;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

public class VirtualNumbersForm extends Window {
    private VirtualNumberDatabaseService service = ObjectFactory.getBean(VirtualNumberDatabaseService.class);
    private ResourceBundle bundle;
    private PortletRequest request;
    private Table table;
    private VirtualNumber virtualNumber;
    private Application application;
    private CouchModel model;


    public VirtualNumbersForm(ResourceBundle bundle, Table table) {
        this.bundle = bundle;
        this.table = table;
    }

    public void init(String id, final Object itemId) {
        request = ((GenericPortletApplication) getApplication()).getPortletRequest();
        virtualNumber = new VirtualNumber();
        model = createModel(id);

        application = getApplication();

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
                    String number = (String) form.getField("number").getValue();
                    CouchModelLite forwardCallTo = (CouchModelLite) form.getField("forwardCallTo").getValue();
                    virtualNumber.setName(name);
                    virtualNumber.setLocator(number);
                    virtualNumber.setForwardTo(forwardCallTo.getId());

                    model.setType(CouchTypes.startnode);

                    service.addVirtualNumber(virtualNumber, model);

                    if (null != model.getRevision()) {
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
                            confirmingRemove.setWidth("300px");
                            confirmingRemove.setHeight("180px");
                        }
                    };

                    table.getContainerProperty(object, "NUMBER").setValue(model.getProperties().get("locator"));
                    table.getContainerProperty(object, "NAME").setValue(virtualNumber.getName());
                    table.getContainerProperty(object, "id").setValue(model.getId());
                    table.getContainerProperty(object, "FORWARD CALLS TO").setValue(forwardCallTo.getName());
                    table.getContainerProperty(object, "").setValue(new Button("", listener));

                    getWindow().showNotification("Well done");
                    close();
                } catch (Exception ignored) {
                }
            }
        });
        buttons.addComponent(save);
    }

    private CouchModel createModel(String id) {
        try {
            return service.getModel(id);
        } catch (Exception e) {
            return CouchModelUtil.newCouchModel(request, CouchTypes.startnode);
        }
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


        List<CouchModelLite> forwards = getForward();
        ComboBox forwardCallTo = new ComboBox("Forward calls to");
        forwardCallTo.addItem("Select . . .");
        for (CouchModelLite forward : forwards) {
            forwardCallTo.addItem(forward);
        }
        forwardCallTo.setNullSelectionItemId("Select . . .");
        forwardCallTo.setRequired(true);
        name.setRequiredError("Empty field \"Forward calls to\"");

        if (null != model.getRevision() && !"".equals(model.getRevision())) {
            name.setValue(model.getProperties().get("name"));
            number.setValue(model.getProperties().get("locator"));
            number.setReadOnly(true);
        }
        form.addField("name", name);
        form.addField("number", number);
        form.addField("forwardCallTo", forwardCallTo);

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

    private List<CouchModelLite> getForward() {

        try {
            return CouchModelUtil.getForwards(PortalUtil.getUserId(request), PortalUtil.getScopeGroupId(request));
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

}
