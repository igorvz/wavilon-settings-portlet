package com.aimprosoft.wavilon.ui.menuitems.forms;

import com.aimprosoft.wavilon.couch.CouchTypes;
import com.aimprosoft.wavilon.service.AllPhoneNumbersDatabaseService;
import com.aimprosoft.wavilon.service.ExtensionDatabaseService;
import com.aimprosoft.wavilon.spring.ObjectFactory;
import com.aimprosoft.wavilon.util.LayoutUtil;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.*;

import java.util.ResourceBundle;

public class ConfirmingRemove extends Window {
    private ExtensionDatabaseService service = ObjectFactory.getBean(ExtensionDatabaseService.class);
    private ResourceBundle bundle;
    private String phoneNumbersLocator;
    private String virtualNumbersLocator;

    public ConfirmingRemove(ResourceBundle bundle) {
        this.bundle = bundle;
    }


    public void init(String id, Table table, Object type) {
        setCaption(bundle.getString("wavilon.confirming.remove.information"));

        setModal(true);
        center();
        setWidth("320px");
        setHeight("180px");

        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setWidth(290, Sizeable.UNITS_PIXELS);
        mainLayout.setHeight(90, Sizeable.UNITS_PIXELS);
        mainLayout.addStyleName("formRegion");
        addComponent(mainLayout);

        mainLayout.addComponent(new Label(bundle.getString("wavilon.confirming.remove.part.first")));
        mainLayout.addComponent(new Label(bundle.getString("wavilon.confirming.remove.part.second")));

        HorizontalLayout buttons = createButtons(id, table, type);
        mainLayout.addComponent(buttons);
        buttons.addStyleName("buttonsPanel");
        mainLayout.setComponentAlignment(buttons, Alignment.BOTTOM_RIGHT);

    }

    private HorizontalLayout createButtons(final String id, final Table table, final Object type) {
        HorizontalLayout buttons = new HorizontalLayout();

        Button cancel = new Button(bundle.getString("wavilon.button.cancel"), new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                close();
            }
        });
        buttons.addComponent(cancel);
        cancel.setHeight(40, Sizeable.UNITS_PIXELS);

        Button ok = new Button(bundle.getString("wavilon.button.ok"));
        ok.addListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                try {
                    service.removeExtension(id);

                    if (null != phoneNumbersLocator || null != virtualNumbersLocator) {
                        AllPhoneNumbersDatabaseService allPhonesService = ObjectFactory.getBean(AllPhoneNumbersDatabaseService.class);

                        String docId;

                        if (null == phoneNumbersLocator) {
                            docId = allPhonesService.getVirtualNumbersDocumentId(virtualNumbersLocator);
                        } else {
                            docId = allPhonesService.getPhoneNumbersDocumentId(phoneNumbersLocator);
                        }

                        allPhonesService.updateModelsLiberationDate(docId);
                    }

                } catch (Exception ignored) {
                }

                table.removeItem(table.getValue());
                table.select(null);
                LayoutUtil.setTableBackground(table, type);

                getParent().getWindow().showNotification(bundle.getString("wavilon.well.done.romove"));
                close();
            }
        });

        buttons.addComponent(cancel);
        ok.addStyleName("saveButton");
        ok.setHeight(40, Sizeable.UNITS_PIXELS);

        buttons.addComponent(ok);

        return buttons;
    }

    public void setNumbersLocator(String numbersId, Object type) {
        if (type.equals(CouchTypes.startnode)) {
            this.virtualNumbersLocator = numbersId;
        } else if ((type.equals(CouchTypes.service))) {
            this.phoneNumbersLocator = numbersId;
        }
    }
}
