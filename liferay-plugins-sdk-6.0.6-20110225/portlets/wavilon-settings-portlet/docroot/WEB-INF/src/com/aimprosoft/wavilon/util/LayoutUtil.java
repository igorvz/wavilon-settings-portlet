package com.aimprosoft.wavilon.util;

import com.aimprosoft.wavilon.couch.CouchModel;
import com.aimprosoft.wavilon.couch.CouchTypes;
import com.aimprosoft.wavilon.service.ExportCSVService;
import com.aimprosoft.wavilon.service.impl.ExportCSVServiceImpl;
import com.aimprosoft.wavilon.spring.ObjectFactory;
import com.aimprosoft.wavilon.ui.menuitems.forms.*;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.terminal.Sizeable;
import com.vaadin.terminal.StreamResource;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.Runo;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class LayoutUtil {
    private static ExportCSVService exportCSVService = ObjectFactory.getBean(ExportCSVServiceImpl.class);

    public static VerticalLayout createHead(ResourceBundle bundle, Table table, CouchTypes types, Window window) {
        table.addStyleName(Runo.TABLE_BORDERLESS);
        VerticalLayout head = new VerticalLayout();
        head.setWidth(100, Sizeable.UNITS_PERCENTAGE);
        Label headLabel;
        GeneralForm form = null;
        StringBuilder addButtonCaption = new StringBuilder(bundle.getString("wavilon.button.add")).append(" ");
        String tableCaption = "";

        if (types.toString().equals("agent")) {
            tableCaption = bundle.getString("wavilon.menuitem.agents");
            addButtonCaption.append(bundle.getString("wavilon.menuitem.agent"));
            form = new AgentsForm(bundle, table);

        } else if (types.toString().equals("extension")) {
            tableCaption = (bundle.getString("wavilon.menuitem.extensions"));
            addButtonCaption.append(bundle.getString("wavilon.menuitem.extension"));
            form = new ExtensionForm(bundle, table);

        } else if (types.toString().equals("recording")) {
            tableCaption = bundle.getString("wavilon.menuitem.recordings");
            addButtonCaption.append(bundle.getString("wavilon.menuitem.recording"));
            form = new RecordingsForm(bundle, table);

        } else if (types.toString().equals("queue")) {
            tableCaption = bundle.getString("wavilon.menuitem.queues");
            addButtonCaption.append(bundle.getString("wavilon.menuitem.queue"));
            form = new QueuesForm(bundle, table);

        } else if (types.toString().equals("startnode")) {
            tableCaption = bundle.getString("wavilon.menuitem.virtualnumbers");
            addButtonCaption.append(bundle.getString("wavilon.menuitem.virtualnumber"));
            form = new VirtualNumbersForm(bundle, table);

        } else if (types.toString().equals("contact")) {
            tableCaption = bundle.getString("wavilon.menuitem.contacts");
            addButtonCaption.append(bundle.getString("wavilon.menuitem.contacts"));
            form = new ContactsForm(bundle, table);
        } else if (types.toString().equals("service")) {
            tableCaption = bundle.getString("wavilon.menuitem.phonenumbers");
            addButtonCaption.append(bundle.getString("wavilon.menuitem.phonenumber"));
            form = new PhoneNumbersForm(bundle, table);
        }

        headLabel = new Label(tableCaption);
        head.addComponent(headLabel);
        head.setMargin(false);
        head.addStyleName("head");
        headLabel.addStyleName("label");

        head.setComponentAlignment(headLabel, Alignment.TOP_LEFT);

        HorizontalLayout secondRow = createSecondRow(bundle, table, window, form, addButtonCaption.toString(), tableCaption);
        head.addComponent(secondRow);

        return head;
    }

    public static void getForm(String id, Object itemId, Window window, GeneralForm form) {

        form.setWidth("450px");

        if (form instanceof AgentsForm) {
            form.setHeight("320px");

        } else if (form instanceof QueuesForm || form instanceof ExtensionForm) {
            form.setHeight("400px");
            form.setWidth("480px");

        } else if (form instanceof RecordingsForm) {
            form.setHeight("400px");

        } else if (form instanceof PhoneNumbersForm) {
            form.setHeight("380px");

        } else form.setHeight("300px");

        form.center();
        form.setModal(true);
        window.addWindow(form);
        form.init(id, itemId);
    }

    public static void addContainerProperties(List<String> hiddenFields, IndexedContainer ic) {
        for (String field : hiddenFields) {
            if ("".equals(field)) {
                ic.addContainerProperty(field, HorizontalLayout.class, "");
            } else {
                ic.addContainerProperty(field, String.class, "");
            }
        }
    }

    public static HorizontalLayout createTablesEditRemoveButtons(final Table table, final Object object, final CouchModel couchModel, final ResourceBundle bundle, final String phoneNumber, final Window mainWindow) {
        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setSizeFull();

        Button editButton = new Button("", new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                table.select(object);
                LayoutUtil.getForm(couchModel.getId(), object, mainWindow, new PhoneNumbersForm(bundle, table));
            }
        });

        Button removeButton = new Button("", new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                table.select(object);
                ConfirmingRemove confirmingRemove = new ConfirmingRemove(bundle);
                mainWindow.addWindow(confirmingRemove);
                confirmingRemove.setNumbersLocator(phoneNumber, couchModel.getType());
                confirmingRemove.init(couchModel.getId(), table, couchModel.getType());
            }
        });

        editButton.addStyleName("editButton");
        editButton.setWidth("20px");
        editButton.setDescription(bundle.getString("wavilon.button.edit"));
        removeButton.addStyleName("removeButton");
        removeButton.setWidth("20px");
        removeButton.setDescription(bundle.getString("wavilon.button.delete"));

        buttons.addComponent(editButton);
        buttons.setComponentAlignment(editButton, Alignment.MIDDLE_LEFT);
        buttons.addComponent(removeButton);
        buttons.setComponentAlignment(removeButton, Alignment.MIDDLE_LEFT);

        buttons.addStyleName("buttonsContainer");

        return buttons;
    }

    private static HorizontalLayout createSecondRow(final ResourceBundle bundle, Table table, final Window window, final GeneralForm form, String addButtonCaption, String tableCaption) {
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

        HorizontalLayout headButtons = createHeadButtons(addButtonCaption, table, window, form, tableCaption, bundle);


        secondRow.addComponent(headButtons);
        secondRow.setHeight("69px");
//        secondRow.setComponentAlignment(searchField, Alignment.MIDDLE_LEFT);
        secondRow.setComponentAlignment(headButtons, Alignment.MIDDLE_RIGHT);
        return secondRow;
    }

    private static HorizontalLayout createHeadButtons(String addButtonCaption, final Table table, final Window window, final GeneralForm form, final String tableCaption, final ResourceBundle resourceBundle) {
        HorizontalLayout headButtons = new HorizontalLayout();

        Button addButton = new Button(addButtonCaption, new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                getForm("-1", "-1", window, form);
            }
        });
        addButton.addStyleName("addButton");
        addButton.setHeight("40px");
        headButtons.addComponent(addButton);

        Button exportButton = new Button("Export", new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                String fileName = tableCaption + "_" + new Date(System.currentTimeMillis()) + ".csv";
                StringWriter stringWriter = new StringWriter();

                try {
                    exportCSVService.exportTableData(table, stringWriter, resourceBundle);
                } catch (IOException ignored) {
                }

                window.open(new StreamResource(new StringWriterStreamResource(stringWriter), fileName, window.getApplication()));
            }
        });
        exportButton.addStyleName("exportButton");
        exportButton.setHeight("40px");
        headButtons.addComponent(exportButton);

        return headButtons;
    }

    private static class StringWriterStreamResource implements StreamResource.StreamSource {
        private StringWriter stringWriter;

        private StringWriterStreamResource(StringWriter stringWriter) {
            this.stringWriter = stringWriter;
        }

        @Override
        public InputStream getStream() {
            return IOUtils.toInputStream(stringWriter.toString());
        }
    }

    public static boolean setTableBackground(Table table, Object contentType) {
        if (0 == table.getContainerDataSource().getItemIds().size()) {
            if (contentType.equals(CouchTypes.agent) || contentType.equals(CouchTypes.agent.toString())) {
                table.addStyleName("agentTable");
            } else if (contentType.equals(CouchTypes.extension) || contentType.equals(CouchTypes.extension.toString())) {
                table.addStyleName("extensionTable");
            } else if (contentType.equals(CouchTypes.recording) || contentType.equals(CouchTypes.recording.toString())) {
                table.addStyleName("recordingTable");
            } else if (contentType.equals(CouchTypes.queue) || contentType.equals(CouchTypes.queue.toString())) {
                table.addStyleName("queueTable");
            } else if (contentType.equals(CouchTypes.startnode) || contentType.equals(CouchTypes.startnode.toString())) {
                table.addStyleName("virtualNumberTable");
            } else if (contentType.equals(CouchTypes.service) || contentType.equals(CouchTypes.service.toString())) {
                table.addStyleName("phoneNumberTable");
            } else if (contentType.equals(CouchTypes.contact) || contentType.equals(CouchTypes.contact.toString())) {
                table.addStyleName("contactTable");
            }

            return true;

        } else {
            table.removeStyleName("agentTable");
            table.removeStyleName("extensionTable");
            table.removeStyleName("recordingTable");
            table.removeStyleName("queueTable");
            table.removeStyleName("contactTable");
            table.removeStyleName("phoneNumberTable");
            table.removeStyleName("virtualNumberTable");

            return false;
        }
    }

    public static void setTableWidth(Table table, Object contentType) {
        setTableWidth(table, contentType, null);
    }

    public static void setTableWidth(Table table, Object contentType, Map<String, Integer> nonstandardColumn) {
        for (Object o : table.getVisibleColumns()) {
            if ("".equals(o)) {
                table.setColumnWidth(o, 60);
            } else if (null != nonstandardColumn && 0 != nonstandardColumn.size()) {
                for (Map.Entry<String, Integer> entry : nonstandardColumn.entrySet()) {
                    if (o.equals(entry.getKey())) {
                        table.setColumnExpandRatio(entry.getKey(), entry.getValue());
                    }
                }
            } else {
                table.setColumnExpandRatio(o, 1);
            }
        }
        table.setWidth(100, Sizeable.UNITS_PERCENTAGE);
        setTableBackground(table, contentType);
    }

}
