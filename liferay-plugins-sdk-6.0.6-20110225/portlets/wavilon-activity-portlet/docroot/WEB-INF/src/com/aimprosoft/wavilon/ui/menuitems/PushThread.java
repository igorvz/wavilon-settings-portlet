package com.aimprosoft.wavilon.ui.menuitems;

import com.aimprosoft.wavilon.service.CdrEktorpDatabaseService;
import com.aimprosoft.wavilon.spring.ObjectFactory;
import com.aimprosoft.wavilon.util.PersonUtil;
import com.vaadin.ui.VerticalLayout;
import org.ektorp.changes.ChangesFeed;
import org.ektorp.changes.DocumentChange;
import org.vaadin.artur.icepush.ICEPush;

import java.util.ResourceBundle;

public class PushThread extends VerticalLayout implements Runnable {
    private CdrEktorpDatabaseService cdrService = ObjectFactory.getBean(CdrEktorpDatabaseService.class);
    private ICEPush icePush;
    private ResourceBundle bundle;

    public PushThread(ResourceBundle bundle) {
        this.bundle = bundle;
    }

    public void init() {
        icePush = new ICEPush();
        getApplication().getMainWindow().addComponent(icePush);

        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {

        showChanges();
    }

    private void showChanges() {

        ChangesFeed feed = cdrService.getChangesFeed();

        while (feed.isAlive()) {

            DocumentChange change = null;

            try {
                change = feed.next();

                if (getApplication() == null) {

                    feed.cancel();
                    break;
                }

            } catch (InterruptedException e) {
            }

            if (change.getRevision().startsWith("1") && !change.isDeleted()){

                String changeId = change.getId();

                createMainContent(changeId);

                icePush.push();
            }
        }
    }

    public void createMainContent(String id) {

        DialogCell dialogCell = new DialogCell(bundle);
        addComponent(dialogCell);
        addStyleName("itemStyle");
        dialogCell.init(PersonUtil.createRandomPerson(id));
    }
}
