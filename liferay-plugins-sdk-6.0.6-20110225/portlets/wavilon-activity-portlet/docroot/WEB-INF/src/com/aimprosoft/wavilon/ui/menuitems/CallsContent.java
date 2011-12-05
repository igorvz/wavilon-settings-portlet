package com.aimprosoft.wavilon.ui.menuitems;

import com.aimprosoft.wavilon.application.GenericPortletApplication;
import com.aimprosoft.wavilon.model.CdrModel;
import com.aimprosoft.wavilon.model.Person;
import com.aimprosoft.wavilon.service.AvatarService;
import com.aimprosoft.wavilon.service.CdrEktorpDatabaseService;
import com.aimprosoft.wavilon.spring.ObjectFactory;
import com.liferay.portal.model.User;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.util.PortalUtil;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.Reindeer;
import org.vaadin.artur.icepush.ICEPush;

import javax.portlet.PortletRequest;
import java.io.IOException;
import java.util.*;

public class CallsContent extends Panel {
    private AvatarService avatarService = ObjectFactory.getBean(AvatarService.class);
    private CdrEktorpDatabaseService cdrService = ObjectFactory.getBean(CdrEktorpDatabaseService.class);
    private ResourceBundle bundle;
    private PortletRequest request;
    private VerticalLayout mainLayout;
    private VerticalLayout itemContent;
    private CategoryFilter categoryFilter;
    private String headCaption;
    private ICEPush icePush;
    private CdrModel model = null;
    private List<String> globalStoreId = new ArrayList<String>();

    //todo remove
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

    private Person createPerson() {
        Person person = new Person();
        person.setName(fnames[1]);
        person.setSurname(lnames[1]);
        person.setTime(times[1]);
        person.setAvatarName(avatarNames[1]);

        List<String> personsCategories = new LinkedList<String>();
        for (int i = 0; i < 4; i++) {
            personsCategories.add(categories[i + 1]);
        }
        person.setCategories(personsCategories);

        return person;
    }

    public CallsContent(ResourceBundle bundle) {
        this.bundle = bundle;
    }

    public void init(CategoryFilter categoryFilter, String headCaption) {
        request = ((GenericPortletApplication) getApplication()).getPortletRequest();
        this.categoryFilter = categoryFilter;
        this.headCaption = headCaption;

        icePush = new ICEPush();
        getApplication().getMainWindow().addComponent(icePush);

        setSizeFull();
        setStyleName(Reindeer.PANEL_LIGHT);
        setScrollable(true);

        initLayout();
    }

    private void initLayout() {
        mainLayout = new VerticalLayout();
        setContent(mainLayout);

        Label headLabel = new Label(headCaption);
        mainLayout.addComponent(headLabel);
        headLabel.setStyleName("head");

        if (null != categoryFilter) {
            addComponent(categoryFilter);
            categoryFilter.init(request);
        }

        VerticalLayout listViewLayout = new VerticalLayout();
        listViewLayout.setWidth(100, Sizeable.UNITS_PERCENTAGE);
        mainLayout.addComponent(listViewLayout);
        listViewLayout.setStyleName("listViewLayout");


        HorizontalLayout listViewButtons = createListViewPart();
        listViewLayout.addComponent(listViewButtons);
        listViewLayout.setComponentAlignment(listViewButtons, Alignment.TOP_RIGHT);

        new PushThread().start();
    }

    private void createMainContent(String id) {
        itemContent = new VerticalLayout();
        mainLayout.addComponent(itemContent);

        //todo iteration adding cells from DB
        DialogCell dialogCell = new DialogCell(bundle);
        itemContent.addComponent(dialogCell);
        itemContent.addStyleName("itemStyle");
        dialogCell.init(createRandomPerson(id));

    }

    private HorizontalLayout createListViewPart() {
        HorizontalLayout listViewPart = new HorizontalLayout();
        Label listViewLabel = new Label(bundle.getString("wavilon.activity.label.list.view"));

        final Button createCdrModel = new Button("Create cdr model");
        final Button avatarButton = new NativeButton();
        final Button nonAvatarButton = new NativeButton();
        avatarButton.setStyleName("avatarButtonSelect");
        nonAvatarButton.setStyleName("nonAvatarButtonNonSelect");


        Button.ClickListener avatarButtonListener = new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {

                try {
                    getWindow().executeJavaScript("showImgBox()");

                    Iterator<Component> componentIterator = itemContent.getComponentIterator();
                    while (componentIterator.hasNext()) {
                        DialogCell dialogCell = (DialogCell) componentIterator.next();
                        dialogCell.setWidth(100, Sizeable.UNITS_PERCENTAGE);
                        Iterator<Component> dialogCellComponentIterator = dialogCell.getComponentIterator();
                        while (dialogCellComponentIterator.hasNext()) {
                            Component component = dialogCellComponentIterator.next();
                            if (component instanceof GridLayout) {
                                component.setWidth(100, Sizeable.UNITS_PERCENTAGE);
                            }
                        }
                    }

                    nonAvatarButton.removeStyleName("nonAvatarButtonSelect");
                    avatarButton.removeStyleName("avatarButtonNonSelect");
                    nonAvatarButton.setStyleName("nonAvatarButtonNonSelect");
                    avatarButton.setStyleName("avatarButtonSelect");
                } catch (Exception ignore) {
                }
            }
        };

        Button.ClickListener nonAvatarButtonListener = new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                try {
                    getWindow().executeJavaScript("hideImgBox()");

                    Iterator<Component> componentIterator = itemContent.getComponentIterator();
                    while (componentIterator.hasNext()) {
                        DialogCell dialogCell = (DialogCell) componentIterator.next();
                        dialogCell.setWidth(100, Sizeable.UNITS_PERCENTAGE);
                        Iterator<Component> dialogCellComponentIterator = dialogCell.getComponentIterator();
                        while (dialogCellComponentIterator.hasNext()) {
                            Component component = dialogCellComponentIterator.next();
                            if (component instanceof GridLayout) {
                                component.setWidth(100, Sizeable.UNITS_PERCENTAGE);
                            }
                        }
                    }

                    nonAvatarButton.removeStyleName("nonAvatarButtonNonSelect");
                    avatarButton.removeStyleName("avatarButtonSelect");
                    nonAvatarButton.setStyleName("nonAvatarButtonSelect");
                    avatarButton.setStyleName("avatarButtonNonSelect");
                } catch (Exception ignore) {
                }
            }
        };

        createCdrModel.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                model = createCdrModelTest();
                try {
                    cdrService.addCdrModel(model);
                } catch (IOException e) {
                }
            }
        });

        avatarButton.addListener(avatarButtonListener);
        nonAvatarButton.addListener(nonAvatarButtonListener);

        listViewPart.addComponent(listViewLabel);
        listViewPart.setComponentAlignment(listViewLabel, Alignment.MIDDLE_CENTER);
        listViewPart.addComponent(avatarButton);
        listViewPart.addComponent(nonAvatarButton);

        listViewPart.addComponent(createCdrModel);

        return listViewPart;
    }

    private CdrModel createCdrModelTest() {

        CdrModel model = new CdrModel();
        model.setId(UUID.randomUUID().toString());

        while (model.getLiferayPortalId() == null || model.getLiferayUserId() == null || model.getLiferayOrganizationId() == null) {
            try {
                model.setLiferayUserId(PortalUtil.getUserId(request));
                model.setLiferayOrganizationId(getOrganizationId(request));
                model.setLiferayPortalId(PortalUtil.getCompany(request).getWebId());
            } catch (Exception ignore) {
            }
        }

        model.setType("cdr");
        model.setCalldate("2011-11-03 19:49:41+01");
        model.setClidnum(916595811);
        model.setDuration(515.325853);
        model.setRecorded(false);
        model.setUniqueid("arcas-1320346181.8545");

        return model;
    }

    public class PushThread extends Thread {
        @Override
        public void run() {

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

                    icePush.push();
                }

                try {
                    Thread.sleep(7000);
                } catch (InterruptedException e) {
                }
            }
        }
    }

    public static Long getOrganizationId(PortletRequest request) {
        try {
            Long userId = PortalUtil.getUserId(request);
            Long companyId = PortalUtil.getDefaultCompanyId();
            User currentUser = UserLocalServiceUtil.getUserById(companyId, userId);
            long organizationIds[] = currentUser.getOrganizationIds();

            if (organizationIds.length != 0) {
                return organizationIds[0];
            } else {
                return -1l;
            }
        } catch (Exception ignored) {
            return -1l;
        }
    }
}
