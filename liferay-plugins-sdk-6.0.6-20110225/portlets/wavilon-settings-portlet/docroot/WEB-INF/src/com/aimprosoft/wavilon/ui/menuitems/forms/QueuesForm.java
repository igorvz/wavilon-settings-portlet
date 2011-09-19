package com.aimprosoft.wavilon.ui.menuitems.forms;

import com.vaadin.data.Item;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.data.validator.IntegerValidator;
import com.vaadin.ui.*;

import java.util.ResourceBundle;

public class QueuesForm extends VerticalLayout {
    private ResourceBundle bundle;
    private Item item;

    public QueuesForm(ResourceBundle bundle, Item item) {
        this.bundle = bundle;
        this.item = item;


        Form form = new Form();

        form.addField("firstName", new TextField(bundle.getString("wavilon.extentions.validation.form.firstName")));
        form.getField("firstName").setRequired(true);
        form.getField("firstName").setRequiredError(bundle.getString("wavilon.extentions.validation.form.error.firstName"));

        form.addField("lastName", new TextField(bundle.getString("wavilon.extentions.validation.form.lastName")));
        form.getField("lastName").setRequired(true);
        form.getField("lastName").setRequiredError(bundle.getString("wavilon.extentions.validation.form.error.lastName"));

        TextField age = new TextField(bundle.getString("wavilon.extentions.validation.form.age"));
        age.setRequired(true);
        age.setRequiredError("Age must not be empty");
        age.addValidator(new IntegerValidator(bundle.getString("wavilon.extentions.validation.form.error.age")));

        form.addField("age", age);

        TextField email = new TextField(bundle.getString("wavilon.extentions.validation.form.email"));
        email.setRequired(true);
        email.setRequiredError(bundle.getString("wavilon.extentions.validation.form.error.email.empty"));
        email.addValidator(new EmailValidator(bundle.getString("wavilon.extentions.validation.form.error.email.valid")));

        form.addField("email", email);

        form.addField("login", new TextField(bundle.getString("wavilon.extentions.validation.form.login")));
        form.getField("login").setRequired(true);
        form.getField("login").setRequiredError(bundle.getString("wavilon.extentions.validation.form.error.login"));

        TextField password = new TextField(bundle.getString("wavilon.extentions.validation.form.password"));
        password.setRequired(true);
        password.setRequiredError(bundle.getString("wavilon.extentions.validation.form.error.password"));
        password.setSecret(true);

        form.addField("password", password);

        Button commit = new Button(bundle.getString("wavilon.extentions.validation.form.button.save"), form, "commit");
        addComponent(form);
        addComponent(commit);


    }
}
