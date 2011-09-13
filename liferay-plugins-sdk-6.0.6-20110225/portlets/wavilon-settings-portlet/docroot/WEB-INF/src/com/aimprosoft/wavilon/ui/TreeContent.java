package com.aimprosoft.wavilon.ui;

import com.aimprosoft.wavilon.ui.menuitems.settings.PhoneNumbersContent;
import com.aimprosoft.wavilon.ui.menuitems.settings.VirtualNumbersContent;
import com.vaadin.ui.VerticalLayout;

import java.util.ResourceBundle;

public class TreeContent extends VerticalLayout {

    public TreeContent(String contentName, ResourceBundle bundle) {
        if (bundle.getString("wavilon.settings.services.phoneNumbers").equals(contentName)) {
            addComponent(new PhoneNumbersContent(bundle));
        } else if (bundle.getString("wavilon.settings.services.virtualNumbers").equals(contentName)) {
            addComponent(new VirtualNumbersContent());
        }
    }
}
