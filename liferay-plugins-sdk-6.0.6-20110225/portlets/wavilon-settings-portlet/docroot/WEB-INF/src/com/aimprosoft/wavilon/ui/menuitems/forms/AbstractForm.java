package com.aimprosoft.wavilon.ui.menuitems.forms;

import com.vaadin.ui.Table;
import com.vaadin.ui.Window;

import java.util.ResourceBundle;

public abstract class AbstractForm extends Window {

    public AgentsForm createAgentsForm(ResourceBundle bundle, Table table) {
        return new AgentsForm(bundle, table);
    }

    public ExtensionForm createExtensionForm(ResourceBundle bundle, Table table) {
        return new ExtensionForm(bundle, table);
    }

    public QueuesForm createQueuesForm(ResourceBundle bundle, Table table) {
        return new QueuesForm(bundle, table);
    }

    public RecordingsForm createRecordingsForm(ResourceBundle bundle, Table table) {
        return new RecordingsForm(bundle, table);
    }

    public VirtualNumbersForm createVirtualNumbersForm(ResourceBundle bundle, Table table) {
        return new VirtualNumbersForm(bundle, table);
    }

    public PhoneNumbersForm createPhoneNumbersForm(ResourceBundle bundle, Table table) {
        return new PhoneNumbersForm(bundle, table);
    }

    public abstract void init(String id, Object itemId);
}
