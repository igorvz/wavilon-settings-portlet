package com.aimprosoft.wavilon.ui.menuitems.forms;

import com.aimprosoft.wavilon.service.ExtensionDatabaseService;
import com.aimprosoft.wavilon.spring.ObjectFactory;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.*;

import java.util.ResourceBundle;

public class ConfirmingRemove extends Window {
    private ExtensionDatabaseService service = ObjectFactory.getBean(ExtensionDatabaseService.class);
    ResourceBundle bundle;

    public ConfirmingRemove(ResourceBundle bundle) {
        this.bundle = bundle;
    }

    public void init(String id, Table table) {
        setCaption(bundle.getString("wavilon.confirming.remove.information"));

        setModal(true);
        center();
        setWidth("300px");
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
                } catch (Exception ignored) {
                }

                table.removeItem(table.getValue());
                table.select(null);

                close();
            }
        });

        buttons.addComponent(cancel);
        buttons.addComponent(ok);

        return buttons;
    }

}
