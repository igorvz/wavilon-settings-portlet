package com.aimprosoft.wavilon.ui.menuitems;

import com.aimprosoft.wavilon.application.GenericPortletApplication;
import com.vaadin.Application;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.artur.icepush.ICEPush;

public class BackgroundThread extends VerticalLayout implements Runnable {

    private DialogCell dialogCell;
    private ICEPush icePush;
    private Application application;

    public BackgroundThread(DialogCell dialogCell) {
        this.dialogCell = dialogCell;
    }

    public void init() {
        icePush = new ICEPush();

        if (getApplication() == null) {

            ((GenericPortletApplication)PushThread.threadLocal.get()).getMainWindow().addComponent(icePush);
            application = (GenericPortletApplication)PushThread.threadLocal.get();

        }else{
            getApplication().getMainWindow().addComponent(icePush);
            application = getApplication();

        }

        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {

        while (true) {

            dialogCell.repaint();

            if (getApplication() == null) {
                application.getMainWindow().addComponent(icePush);
            }
            icePush.push();
        }
    }
}
