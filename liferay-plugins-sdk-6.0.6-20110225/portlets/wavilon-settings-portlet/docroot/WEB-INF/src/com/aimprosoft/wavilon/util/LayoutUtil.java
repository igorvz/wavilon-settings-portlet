package com.aimprosoft.wavilon.util;

import com.aimprosoft.wavilon.couch.CouchTypes;
import com.aimprosoft.wavilon.ui.menuitems.forms.*;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.*;

import java.util.ResourceBundle;

public class LayoutUtil {

    public static HorizontalLayout createHead(ResourceBundle bundle, Table table, CouchTypes types, Window window) {
        HorizontalLayout head = new HorizontalLayout();
        head.setWidth(100, Sizeable.UNITS_PERCENTAGE);
        Label headLabel = null;
        AbstractForm form = null;

        if (types.toString().equals("agent")) {
            headLabel = new Label(bundle.getString("wavilon.menuitem.agents"));
            form = new AgentsForm(bundle, table);

        } else if (types.toString().equals("extension")) {
            headLabel = new Label(bundle.getString("wavilon.menuitem.extensions"));
            form = new ExtensionForm(bundle, table);

        } else if (types.toString().equals("recording")) {
            headLabel = new Label(bundle.getString("wavilon.menuitem.recordings"));
            form = new RecordingsForm(bundle, table);

        } else if (types.toString().equals("queue")) {
            headLabel = new Label(bundle.getString("wavilon.menuitem.queues"));
            form = new QueuesForm(bundle, table);

        } else if (types.toString().equals("startnode")) {
            headLabel = new Label(bundle.getString("wavilon.menuitem.virtualnumbers"));
            form = new VirtualNumbersForm(bundle, table);

        } else if (types.toString().equals("service")) {
            headLabel = new Label(bundle.getString("wavilon.menuitem.phonenumbers"));
            form = new PhoneNumbersForm(bundle, table);
        }

        head.addComponent(headLabel);
        head.setMargin(false);
        head.addStyleName("head");
        headLabel.addStyleName("label");

        HorizontalLayout addButton = createButton(bundle, window, form);
        head.addComponent(addButton);

        head.setComponentAlignment(headLabel, Alignment.TOP_LEFT);
        head.setComponentAlignment(addButton, Alignment.MIDDLE_RIGHT);

        return head;
    }

    public static void getForm(String id, Object itemId, Window window, AbstractForm form) {

        form.setWidth("450px");

        if (form instanceof AgentsForm || form instanceof ExtensionForm) {
            form.setHeight("320px");

        } else if (form instanceof QueuesForm) {
            form.setHeight("400px");
            form.setWidth("480px");

        } else if (form instanceof RecordingsForm) {
            form.setHeight("400px");

        } else if (form instanceof PhoneNumbersForm) {
            form.setHeight("350px");

        } else form.setHeight("300px");

        form.center();
        form.setModal(true);
        window.addWindow(form);
        form.init(id, itemId);
    }

    private static HorizontalLayout createButton(final ResourceBundle bundle, final Window window, final AbstractForm form) {
        HorizontalLayout addButton = new HorizontalLayout();
        addButton.addComponent(new Button(bundle.getString("wavilon.button.add"), new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                getForm("-1", "-1", window, form);
            }
        }));
        return addButton;
    }
}
