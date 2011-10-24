package com.aimprosoft.wavilon.ui;

import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import java.util.ResourceBundle;

public class PleaseSignInPage extends VerticalLayout{
    private ResourceBundle bundle;

    public PleaseSignInPage(ResourceBundle bundle) {

        this.bundle = bundle;

        this.addStyleName("singInPanel");

        this.addComponent(new Label(bundle.getString("wavilon.login.please") + " <a href='/c/portal/login'>" +
                                    bundle.getString("wavilon.login.signIn") + "</a>", Label.CONTENT_XHTML));
    }
}
