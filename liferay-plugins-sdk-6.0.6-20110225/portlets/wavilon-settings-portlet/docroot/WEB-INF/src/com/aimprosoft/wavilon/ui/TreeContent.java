package com.aimprosoft.wavilon.ui;

import com.aimprosoft.wavilon.ui.menuitems.settings.PhoneNumbersContent;
import com.aimprosoft.wavilon.ui.menuitems.settings.VirtualNumbersContent;
import com.vaadin.ui.VerticalLayout;

public class TreeContent extends VerticalLayout {

    public TreeContent(String contentName) {
        if ("Phone Numbers".equals(contentName)) {
            addComponent(new PhoneNumbersContent());
        } else if ("Virtual Numbers".equals(contentName)) {
            addComponent(new VirtualNumbersContent());
        }
    }
}
