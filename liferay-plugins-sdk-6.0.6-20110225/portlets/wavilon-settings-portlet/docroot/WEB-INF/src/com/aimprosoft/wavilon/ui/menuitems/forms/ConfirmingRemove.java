package com.aimprosoft.wavilon.ui.menuitems.forms;

import com.aimprosoft.wavilon.service.AllPhoneNumbersDatabaseService;
import com.aimprosoft.wavilon.service.ExtensionDatabaseService;
import com.aimprosoft.wavilon.spring.ObjectFactory;
import com.liferay.portal.kernel.util.StringUtil;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.*;
import org.apache.commons.lang.StringUtils;

import java.util.ResourceBundle;

public class ConfirmingRemove extends Window {
    private ExtensionDatabaseService service = ObjectFactory.getBean(ExtensionDatabaseService.class);
    private ResourceBundle bundle;
    private String phoneNumbersId;

    public ConfirmingRemove(ResourceBundle bundle) {
        this.bundle = bundle;
    }



    public void init(String id, Table table) {
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

        HorizontalLayout buttons = createButtons(id, table);
        mainLayout.addComponent(buttons);
        buttons.addStyleName("buttonsPanel");
        mainLayout.setComponentAlignment(buttons, Alignment.BOTTOM_RIGHT);

    }

    private HorizontalLayout createButtons(final String id, final Table table) {
        HorizontalLayout buttons = new HorizontalLayout();

        Button cancel = new Button(bundle.getString("wavilon.button.cancel"), new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                close();
            }
        });
        buttons.addComponent(cancel);

        Button ok = new Button(bundle.getString("wavilon.button.ok"));
        ok.addListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                try {
                    service.removeExtension(id);

                    if(null != phoneNumbersId){
                        AllPhoneNumbersDatabaseService allPhonesService = ObjectFactory.getBean(AllPhoneNumbersDatabaseService.class);
                        String docId = allPhonesService.getDocumentId(phoneNumbersId);
                        allPhonesService.updateModel(docId);
                    }

                } catch (Exception ignored) {
                }

                table.removeItem(table.getValue());
                table.select(null);

                getParent().getWindow().showNotification(bundle.getString("wavilon.well.done.romove"));
                close();
            }
        });

        buttons.addComponent(cancel);
        ok.addStyleName("saveButton");
        buttons.addComponent(ok);

        return buttons;
    }

    public void setPhoneNumbersId(String phoneNumbersId) {
        this.phoneNumbersId = phoneNumbersId;
    }
}
