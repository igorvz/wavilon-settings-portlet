package com.aimprosoft.wavilon.util;

import com.aimprosoft.wavilon.couch.CouchTypes;
import com.aimprosoft.wavilon.ui.menuitems.forms.*;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.Runo;

import java.util.ResourceBundle;

public class LayoutUtil {

    public static VerticalLayout createHead(ResourceBundle bundle, Table table, CouchTypes types, Window window) {
        table.addStyleName(Runo.TABLE_BORDERLESS);
        VerticalLayout head = new VerticalLayout();
        head.setWidth(100, Sizeable.UNITS_PERCENTAGE);
        Label headLabel = null;
        AbstractForm form = null;
        StringBuilder addButtonCaption = new StringBuilder(bundle.getString("wavilon.button.add")).append(" ");

        if (types.toString().equals("agent")) {
            headLabel = new Label(bundle.getString("wavilon.menuitem.agents"));
            addButtonCaption.append(bundle.getString("wavilon.menuitem.agent"));
            form = new AgentsForm(bundle, table);

        } else if (types.toString().equals("extension")) {
            headLabel = new Label(bundle.getString("wavilon.menuitem.extensions"));
            addButtonCaption.append(bundle.getString("wavilon.menuitem.extension"));
            form = new ExtensionForm(bundle, table);

        } else if (types.toString().equals("recording")) {
            headLabel = new Label(bundle.getString("wavilon.menuitem.recordings"));
            addButtonCaption.append(bundle.getString("wavilon.menuitem.recording"));
            form = new RecordingsForm(bundle, table);

        } else if (types.toString().equals("queue")) {
            headLabel = new Label(bundle.getString("wavilon.menuitem.queues"));
            addButtonCaption.append(bundle.getString("wavilon.menuitem.queue"));
            form = new QueuesForm(bundle, table);

        } else if (types.toString().equals("startnode")) {
            headLabel = new Label(bundle.getString("wavilon.menuitem.virtualnumbers"));
            addButtonCaption.append(bundle.getString("wavilon.menuitem.virtualnumber"));
            form = new VirtualNumbersForm(bundle, table);

        } else if (types.toString().equals("service")) {
            headLabel = new Label(bundle.getString("wavilon.menuitem.phonenumbers"));
            addButtonCaption.append(bundle.getString("wavilon.menuitem.phonenumber"));
            form = new PhoneNumbersForm(bundle, table);
        }

        head.addComponent(headLabel);
        head.setMargin(false);
        head.addStyleName("head");
        headLabel.addStyleName("label");

        head.setComponentAlignment(headLabel, Alignment.TOP_LEFT);


        HorizontalLayout secondRow = createSecondRow(bundle, table, window, form, addButtonCaption.toString());
        head.addComponent(secondRow);

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

    private static HorizontalLayout createSecondRow(final ResourceBundle bundle, Table table, final Window window, final AbstractForm form, String addButtonCaption) {
        HorizontalLayout secondRow = new HorizontalLayout();
        secondRow.setWidth(100, Sizeable.UNITS_PERCENTAGE);

//        SearchField searchField = new SearchField(new SearchField.ResultDisplayer() {
//            @Override
//            public Layout processSearch(SearchField.SearchEvent searchEvent) {
//                final Layout cssLayout = new CssLayout();
//                for (String s : searchEvent.getKeywords()) {
//                    cssLayout.addComponent(new Label(s));
//                }
//                return cssLayout;
//            }
//        });
//        secondRow.addComponent(searchField);

        HorizontalLayout headButtons = createHeadButtons(addButtonCaption, table, window, form);


        secondRow.addComponent(headButtons);
//        secondRow.setComponentAlignment(searchField, Alignment.MIDDLE_LEFT);
        secondRow.setComponentAlignment(headButtons, Alignment.MIDDLE_RIGHT);
        return secondRow;
    }

    private static HorizontalLayout createHeadButtons(String addButtonCaption, final Table table, final Window window, final AbstractForm form) {
        HorizontalLayout headButtons = new HorizontalLayout();

        Button addButton = new Button(addButtonCaption, new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                getForm("-1", "-1", window, form);
            }
        });
        addButton.addStyleName("addButton");
        headButtons.addComponent(addButton);

        Button exportButton = new Button("Export", new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {

            }
        });
        exportButton.addStyleName("exportButton");
        headButtons.addComponent(exportButton);


        return headButtons;
    }

}
