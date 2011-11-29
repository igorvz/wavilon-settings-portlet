package com.aimprosoft.wavilon.ui.menuitems.forms;

import com.aimprosoft.wavilon.application.GenericPortletApplication;
import com.aimprosoft.wavilon.couch.CouchModel;
import com.aimprosoft.wavilon.couch.CouchModelLite;
import com.aimprosoft.wavilon.couch.CouchTypes;
import com.aimprosoft.wavilon.model.BaseModel;
import com.aimprosoft.wavilon.service.GeneralService;
import com.aimprosoft.wavilon.util.CouchModelUtil;
import com.aimprosoft.wavilon.util.LayoutUtil;
import com.vaadin.Application;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.*;

import javax.portlet.PortletRequest;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

public class GeneralForm extends Window {
    protected ResourceBundle bundle;
    protected Table table;
    protected PortletRequest request;
    protected Application application;
    protected CouchModel model;

    public GeneralForm(ResourceBundle bundle, Table table) {
        this.bundle = bundle;
        this.table = table;
    }

    public void init(String id, final Object itemId) {
        removeAllComponents();
        application = getApplication();
        request = ((GenericPortletApplication) application).getPortletRequest();


    }

    protected void initForm(final Form form, Button.ClickListener saveButtonListener) {
        VerticalLayout content = new VerticalLayout();
        content.addStyleName("formRegion");
        content.setSizeFull();
        addComponent(content);

        content.addComponent(form);

        createSaveCancelButtons(bundle, content, this, saveButtonListener);
    }

    protected void createSaveCancelButtons(ResourceBundle bundle, VerticalLayout formContent, final GeneralForm formWindow, Button.ClickListener saveButtonListener) {
        HorizontalLayout buttons = new HorizontalLayout();
        formContent.addComponent(buttons);
        buttons.addStyleName("buttonsPanel");
        formContent.setComponentAlignment(buttons, Alignment.BOTTOM_RIGHT);

        Button save = new Button(bundle.getString("wavilon.button.save"), saveButtonListener);
        save.setHeight("40px");
        save.addStyleName("saveButton");

        Button cancel = new Button(bundle.getString("wavilon.button.cancel"), new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                formWindow.close();
            }
        });
        cancel.setHeight("40px");
        cancel.addStyleName("cancelButton");

        buttons.addComponent(save);
        buttons.addComponent(cancel);

        save.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        cancel.setClickShortcut(ShortcutAction.KeyCode.ESCAPE);
    }

    protected List<CouchModelLite> getForwards() {
        try {
            return CouchModelUtil.getForwards(CouchModelUtil.getOrganizationId(request));
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    protected CouchModel createCoucModel(String id, GeneralService service, CouchTypes type) {
        if ("-1".equals(id)) {
            return CouchModelUtil.newCouchModel(request, type);
        }
        try {
            return service.getModel(id);
        } catch (Exception e) {
            return CouchModelUtil.newCouchModel(request, type);
        }
    }

    protected <T> T getModel(CouchModel model, GeneralService service, Class<T> modelClass) throws IOException {
        return service.getModel(model, modelClass);
    }

    protected HorizontalLayout createTablesEditRemoveButtons(Table table, Object object, CouchModel couchModel, String phoneNumber) {
       return LayoutUtil.createTablesEditRemoveButtons(table, object, couchModel, bundle, phoneNumber, application.getMainWindow());
    }

    @Override
    public void close() {
        super.close();
    }
}
