package com.aimprosoft.wavilon.ui.menuitems.forms;

import com.aimprosoft.wavilon.application.GenericPortletApplication;
import com.aimprosoft.wavilon.couch.CouchModel;
import com.aimprosoft.wavilon.couch.CouchModelLite;
import com.aimprosoft.wavilon.couch.CouchTypes;
import com.aimprosoft.wavilon.model.VirtualNumber;
import com.aimprosoft.wavilon.service.AllPhoneNumbersDatabaseService;
import com.aimprosoft.wavilon.service.VirtualNumberDatabaseService;
import com.aimprosoft.wavilon.spring.ObjectFactory;
import com.aimprosoft.wavilon.util.CouchModelUtil;
import com.liferay.portal.util.PortalUtil;
import com.vaadin.Application;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.ui.*;

import javax.portlet.PortletRequest;
import java.io.IOException;
import java.util.*;

public class VirtualNumbersForm extends AbstractForm {
    private VirtualNumberDatabaseService service = ObjectFactory.getBean(VirtualNumberDatabaseService.class);
    private AllPhoneNumbersDatabaseService allPhonesService = ObjectFactory.getBean(AllPhoneNumbersDatabaseService.class);
    private ResourceBundle bundle;
    private PortletRequest request;
    private Table table;
    private VirtualNumber virtualNumber;
    private Application application;
    private CouchModel model;
    private String anotherPhoneId = "";

    public VirtualNumbersForm(ResourceBundle bundle, Table table) {
        this.bundle = bundle;
        this.table = table;
    }

    public void init(String id, final Object itemId) {
        removeAllComponents();
        request = ((GenericPortletApplication) getApplication()).getPortletRequest();
        virtualNumber = new VirtualNumber();
        model = createModel(id);

        application = getApplication();

        if ("-1".equals(id)) {
            setCaption(bundle.getString("wavilon.form.virtualnumbers.new.phone.number"));
        } else {
            setCaption(bundle.getString("wavilon.form.virtualnumbers.edit.phone.number"));
        }

        VerticalLayout content = new VerticalLayout();
        content.addStyleName("formRegion");

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
                    String number = (String) form.getField("number").getValue();
                    String forwardCallTo = (String) form.getField("forwardCallTo").getValue();
//                    CouchModelLite forwardCallTo = (CouchModelLite) form.getField("forwardCallTo").getValue();
                    virtualNumber.setName(name);
                    virtualNumber.setLocator(number);
                    virtualNumber.setForwardTo(forwardCallTo);

                    model.setType(CouchTypes.startnode);

                    service.addVirtualNumber(virtualNumber, model);

//                    if (!"".equals(anotherPhoneId)) {
//                        allPhonesService.updateModel(model.getLiferayOrganizationId(), anotherPhoneId);
//                    }

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
                        }
                    };

                    table.getContainerProperty(object, bundle.getString("wavilon.table.virtualnumbers.column.number")).setValue(model.getProperties().get("locator"));
                    table.getContainerProperty(object, bundle.getString("wavilon.table.virtualnumbers.column.name")).setValue(virtualNumber.getName());
                    table.getContainerProperty(object, "id").setValue(model.getId());
                    table.getContainerProperty(object, bundle.getString("wavilon.table.virtualnumbers.column.forward.calls.to")).setValue(forwardCallTo);
                    table.getContainerProperty(object, "").setValue(new Button("", listener));

                    getWindow().showNotification(bundle.getString("wavilon.well.done"));
                    close();
                } catch (Exception ignored) {
                }
            }
        });
        save.addStyleName("saveButton");
        buttons.addComponent(save);
    }

    private CouchModel createModel(String id) {
        if ("-1".equals(id)) {
            return CouchModelUtil.newCouchModel(request, CouchTypes.startnode);
        }
        try {
            return service.getModel(id);
        } catch (Exception e) {
            try {
                CouchModel anotherCouchModel = allPhonesService.getVirtualNumber(id);
                anotherPhoneId = anotherCouchModel.getId();
                CouchModel nativeCouchModel = CouchModelUtil.newCouchModel(request, CouchTypes.startnode);
                Map<String, Object> props = anotherCouchModel.getProperties();
                props.put("name", "");
                nativeCouchModel.setProperties(props);

                return nativeCouchModel;

            } catch (Exception ignored) {
                return CouchModelUtil.newCouchModel(request, CouchTypes.startnode);
            }
        }
    }

    private Form createForm() {
        Form form = new Form();
        form.addStyleName("labelField");

        TextField name = new TextField(bundle.getString("wavilon.form.name"));
        name.setRequired(true);
        name.setRequiredError(bundle.getString("wavilon.error.massage.virtualnumbers.name.empty"));

        TextField number = new TextField(bundle.getString("wavilon.form.number"));
        number.setRequired(true);
        number.setRequiredError(bundle.getString("wavilon.error.massage.virtualnumbers.number.empty"));
//        number.addValidator(new RegexpValidator("[+][0-9]{10}", bundle.getString("wavilon.error.massage.virtualnumbers.wrong")));
        number.addValidator(new RegexpValidator("[0-9]{11}", bundle.getString("wavilon.error.massage.virtualnumbers.wrong")));


//        List<CouchModelLite> forwards = getForward();
        ComboBox forwardCallTo = new ComboBox(bundle.getString("wavilon.form.virtualnumbers.forward.calls.to"));
        forwardCallTo.addItem(bundle.getString("wavilon.form.select"));
//        for (CouchModelLite forward : forwards) {
//            forwardCallTo.addItem(forward);
//        }
        List<String> virtualNumbers = createVirtualNumbers();
        for (String virtualNumber : virtualNumbers) {
            forwardCallTo.addItem(virtualNumber);
        }
        forwardCallTo.setNullSelectionItemId(bundle.getString("wavilon.form.select"));
        forwardCallTo.setRequired(true);
        forwardCallTo.setRequiredError(bundle.getString("wavilon.error.massage.virtualnumbers.forward.empty"));

        if ((null != model.getRevision() && !"".equals(model.getRevision())) || null != model.getProperties()) {
            name.setValue(model.getProperties().get("name"));
            number.setValue(model.getProperties().get("locator"));
            number.setReadOnly(true);
        }
        form.addField("name", name);
        form.addField("number", number);
        form.addField("forwardCallTo", forwardCallTo);

        return form;
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
            return CouchModelUtil.getForwards(PortalUtil.getScopeGroupId(request));
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    private List<String> createVirtualNumbers() {
        List virtualNumbersList = new LinkedList<String>();
        List<CouchModel> couchModelList = getAllVirtualNumbers();

        for (CouchModel model : couchModelList) {
            virtualNumbersList.add(model.getProperties().get("locator"));
        }
        return virtualNumbersList;
    }

    private List<CouchModel> getAllVirtualNumbers() {
        try {
            return allPhonesService.getVirtualNumbers();
        } catch (IOException e) {
            return Collections.emptyList();
        }
    }

}
