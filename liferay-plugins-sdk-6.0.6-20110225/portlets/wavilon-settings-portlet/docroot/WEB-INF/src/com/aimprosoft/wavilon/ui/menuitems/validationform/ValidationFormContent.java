package com.aimprosoft.wavilon.ui.menuitems.validationform;

import com.vaadin.data.validator.EmailValidator;
import com.vaadin.data.validator.IntegerValidator;
import com.vaadin.ui.Button;
import com.vaadin.ui.Form;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import java.util.ResourceBundle;

public class ValidationFormContent extends VerticalLayout{
    ResourceBundle bundle;

    public ValidationFormContent(ResourceBundle bundle) {
        this.bundle = bundle;

        Form form = new Form();

        form.addField("firstName", new TextField(bundle.getString("wavilon.settings.validation.form.firstName")));
        form.getField("firstName").setRequired(true);
        form.getField("firstName").setRequiredError(bundle.getString("wavilon.settings.validation.form.error.firstName"));

        form.addField("lastName", new TextField(bundle.getString("wavilon.settings.validation.form.lastName")));
        form.getField("lastName").setRequired(true);
        form.getField("lastName").setRequiredError(bundle.getString("wavilon.settings.validation.form.error.lastName"));

        TextField age = new TextField(bundle.getString("wavilon.settings.validation.form.age"));
        age.setRequired(true);
        age.setRequiredError("Age must not be empty");
        age.addValidator(new IntegerValidator(bundle.getString("wavilon.settings.validation.form.error.age")));

        form.addField("age", age);

        TextField email = new TextField(bundle.getString("wavilon.settings.validation.form.email"));
        email.setRequired(true);
        email.setRequiredError(bundle.getString("wavilon.settings.validation.form.error.email.empty"));
        email.addValidator(new EmailValidator(bundle.getString("wavilon.settings.validation.form.error.email.valid")));

        form.addField("email", email);

        form.addField("login", new TextField(bundle.getString("wavilon.settings.validation.form.login")));
        form.getField("login").setRequired(true);
        form.getField("login").setRequiredError(bundle.getString("wavilon.settings.validation.form.error.login"));

        TextField password = new TextField(bundle.getString("wavilon.settings.validation.form.password"));
        password.setRequired(true);
        password.setRequiredError(bundle.getString("wavilon.settings.validation.form.error.password"));
        password.setSecret(true);

        form.addField("password", password);

        Button commit = new Button(bundle.getString("wavilon.settings.validation.form.button.save"), form, "commit");
        addComponent(form);
        addComponent(commit);
    }
}
