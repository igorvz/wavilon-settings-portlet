package com.aimprosoft.wavilon.ui.menuitems;

import com.vaadin.ui.VerticalLayout;
import org.vaadin.artur.icepush.ICEPush;

public class BackgroundThread extends VerticalLayout implements Runnable {

    private DialogCell dialogCell;
    private ICEPush icePush;

    public BackgroundThread(DialogCell dialogCell) {
        this.dialogCell = dialogCell;
    }

    public void init() {
        icePush = new ICEPush();

        getApplication().getMainWindow().addComponent(icePush);

        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        while (getApplication() != null) {

            dialogCell.repaint();

            if (getApplication() == null) {

                break;
            }

            icePush.push();
        }
    }
}
