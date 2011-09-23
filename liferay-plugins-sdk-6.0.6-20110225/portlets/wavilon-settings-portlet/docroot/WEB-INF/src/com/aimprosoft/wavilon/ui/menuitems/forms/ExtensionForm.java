package com.aimprosoft.wavilon.ui.menuitems.forms;

import com.aimprosoft.wavilon.model.Extension;
import com.aimprosoft.wavilon.service.ExtensionDatabaseService;
import com.aimprosoft.wavilon.spring.ObjectFactory;
import com.vaadin.data.Item;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.*;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class ExtensionForm extends VerticalLayout {
    private ResourceBundle bundle;
    private Item item;
    private static ExtensionDatabaseService service = ObjectFactory.getBean(ExtensionDatabaseService.class);
    private List<String> extensions = new LinkedList<String>();
    Extension extension = null;

    public ExtensionForm(final ResourceBundle bundle, Item item, final VerticalLayout right, Table table, String menuName) {
        this.bundle = bundle;
        this.item = item;

        String id = (String) item.getItemProperty("id").getValue();
        try {
            extension = service.getExtension(id);
        } catch (IOException ignored) {
        }


        final TextField field = new TextField();
        field.setRequired(true);

        String fieldName = "";
        String columnName = "";

        if ("Phone Numbers".equals(menuName)) {
            fieldName = "Phone Number";
            columnName = "phoneNumber";
            field.addValidator(new RegexpValidator("[+][0-9]{10}", "Incorrect phone number. Must begin with a + ..."));
        }

        if ("SIP".equals(menuName)) {
            fieldName = "URL sip";
            columnName = "sipURL";
            field.addValidator(new RegexpValidator("(http|https)://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?", "Incorrect sip URL"));


        } else if ("Gtalk".equals(menuName)) {
            fieldName = "E-mail";
            columnName = "email";
            field.addValidator(new EmailValidator("Incorrect email address"));
        }

        final Form form = new Form();

        Label name = new Label();
        name.setValue(item.getItemProperty("firstName").toString());
        name.addStyleName("headerForm");

        field.setCaption(fieldName);
        field.setValue(item.getItemProperty(columnName));
        field.setRequiredError(fieldName + " must be not empty");

        TextField extensionNumber = new TextField("Extension number");
        extensionNumber.setValue(extension.getExtensionNumber());
        extensionNumber.setReadOnly(true);

        form.addField("fieldChange", field);
        form.addField("extension number", extensionNumber);

        form.addStyleName("labelField");

        Button change = new Button(bundle.getString("wavilon.settings.services.phoneNumbers.change"), form, "commit");
        change.setClickShortcut(ShortcutAction.KeyCode.ENTER);

        final String finalColumnName = columnName;
        change.addListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                try {

                    form.commit();

                    String fieldName = (String) form.getField("fieldChange").getValue();
                    if ("email".equals(finalColumnName)) {
                        extension.setEmail(fieldName);

                    } else if ("sipURL".equals(finalColumnName)) {
                        extension.setSipURL(fieldName);

                    } else if ("phoneNumber".equals(finalColumnName)) {
                        extension.setPhoneNumber(fieldName);
                    }
                    service.updateExtension(extension);

                    getWindow().showNotification("Well done");
                    right.removeAllComponents();

                } catch (Exception ignored) {
                }
            }


        });
        addComponent(name);
        addComponent(form);
        addComponent(change);
    }
}
