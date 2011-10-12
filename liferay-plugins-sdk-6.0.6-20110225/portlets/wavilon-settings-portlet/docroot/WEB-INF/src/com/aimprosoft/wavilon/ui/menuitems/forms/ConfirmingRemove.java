package com.aimprosoft.wavilon.ui.menuitems.forms;

import com.aimprosoft.wavilon.service.ExtensionDatabaseService;
import com.aimprosoft.wavilon.spring.ObjectFactory;
import com.vaadin.event.ShortcutAction;
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
        setCaption("Information");

        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setWidth(290, Sizeable.UNITS_PIXELS);
        mainLayout.setHeight(90, Sizeable.UNITS_PIXELS);
        mainLayout.addStyleName("formRegion");
        addComponent(mainLayout);

        String messageContent1 = "Do you really want to permanently delete item?";
        String messageContent2 = "This operation can't be undone!";
        mainLayout.addComponent(new Label(messageContent1));
        mainLayout.addComponent(new Label(messageContent2));

        HorizontalLayout buttons = createButtons(id, table);
        mainLayout.addComponent(buttons);
        buttons.addStyleName("buttonsPanel");
        mainLayout.setComponentAlignment(buttons, Alignment.BOTTOM_RIGHT);

    }


    private HorizontalLayout createButtons(final String id, final Table table, final Object object) {
        HorizontalLayout buttons = new HorizontalLayout();

        Button cancel = new Button("Cancel", new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                close();
            }
        });
        buttons.addComponent(cancel);

        Button ok = new Button("Ok");
        ok.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        ok.setData(object);
        ok.addListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                try {
                    service.removeExtension(id);
                } catch (Exception ignored) {
                }

                table.removeItem(event.getButton().getData());
                table.select(null);

                close();
            }
        });

        buttons.addComponent(cancel);
        buttons.addComponent(ok);

        return buttons;
    }

    private HorizontalLayout createButtons(final String id, final Table table) {
        HorizontalLayout buttons = new HorizontalLayout();

        Button cancel = new Button("Cancel", new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                close();
            }
        });
        buttons.addComponent(cancel);

        Button ok = new Button("Ok");
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

    public void initConfirm(String id, Table table, Object object) {
        setCaption("Information");

        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setWidth(290, Sizeable.UNITS_PIXELS);
        mainLayout.setHeight(90, Sizeable.UNITS_PIXELS);
        mainLayout.addStyleName("formRegion");
        addComponent(mainLayout);

        String messageContent1 = "Do you really want to permanently delete item?";
        String messageContent2 = "This operation can't be undone!";
        mainLayout.addComponent(new Label(messageContent1));
        mainLayout.addComponent(new Label(messageContent2));

        HorizontalLayout buttons = createButtons(id, table, object);
        mainLayout.addComponent(buttons);
        buttons.addStyleName("buttonsPanel");
        mainLayout.setComponentAlignment(buttons, Alignment.BOTTOM_RIGHT);
    }
}
