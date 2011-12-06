package com.aimprosoft.wavilon.ui.menuitems;

import com.aimprosoft.wavilon.application.GenericPortletApplication;
import com.aimprosoft.wavilon.model.Person;
import com.aimprosoft.wavilon.service.CdrEktorpDatabaseService;
import com.aimprosoft.wavilon.spring.ObjectFactory;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.artur.icepush.ICEPush;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class PushThread extends VerticalLayout implements Runnable {
    public static ThreadLocal threadLocal = new ThreadLocal();
    private List<String> globalStoreId = new ArrayList<String>();
    private CdrEktorpDatabaseService cdrService = ObjectFactory.getBean(CdrEktorpDatabaseService.class);
    private ICEPush icePush;
    private ResourceBundle bundle;

    private String[] fnames = {"Peter", "Alice", "Joshua", "Mike", "Olivia",
            "Nina", "Alex", "Rita", "Dan", "Umberto", "Henrik", "Rene",
            "Lisa", "Marge"};
    private String[] lnames = {"Smith", "Gordon", "Simpson", "Brown", "Clavel",
            "Simons", "Verne", "Scott", "Allison", "Gates", "Rowling",
            "Barks", "Ross", "Schneider", "Tate"};

    private String[] categories = {"Support", "Problematic customer", "Human Resources", "Design Agency",
            "Designers", "Delivery", "Fashion", "Software", "Web Apps", "Magazines", "Music", "Portfolio"};

    private String[] times = {"9:24", "8:52", "6:35", "4:65",
            "9:56", "18:22", "23:01", "14:40", "15;45"};

    private String[] avatarNames = {"face1.png", "face2.png", "face3.png", "face4.png"};


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

         threadLocal.set(getApplication());

        while (true) {

            List<String> idAllModels = cdrService.getModelsId();

            if (!globalStoreId.containsAll(idAllModels)) {

                List<String> localStore = new ArrayList<String>();

                localStore.addAll(globalStoreId);

                idAllModels.removeAll(globalStoreId);

                globalStoreId.clear();
                globalStoreId.addAll(localStore);
                globalStoreId.addAll(idAllModels);

                for (String id : idAllModels) {
                    createMainContent(id);
                }

                localStore.clear();
                idAllModels.clear();

                if (getApplication() == null) {
                    ((GenericPortletApplication) threadLocal.get()).getMainWindow().addComponent(icePush);
                }

                icePush.push();
            }

            try {
                Thread.sleep(7000);
            } catch (InterruptedException e) {
            }
        }
    }

    private void createMainContent(String id) {

        DialogCell dialogCell = new DialogCell(bundle);
        addComponent(dialogCell);
        addStyleName("itemStyle");
        dialogCell.init(createRandomPerson(id));
    }

    private Person createRandomPerson(String id) {
        Person person = new Person();
        person.setName(fnames[(int) (fnames.length * Math.random())]);
        person.setSurname(lnames[(int) (lnames.length * Math.random())]);
        person.setTime(times[(int) (times.length * Math.random())]);
        person.setAvatarName(avatarNames[(int) (avatarNames.length * Math.random())]);
        person.setId(id);

        List<String> personsCategories = new LinkedList<String>();
        for (int i = 0; i < 4; i++) {
            personsCategories.add(categories[(int) (categories.length * Math.random())]);
        }
        person.setCategories(personsCategories);

        return person;
    }
}
