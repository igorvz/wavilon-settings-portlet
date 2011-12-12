package com.aimprosoft.wavilon.ui.menuitems;

import com.aimprosoft.wavilon.application.GenericPortletApplication;
import com.aimprosoft.wavilon.model.CdrModel;
import com.aimprosoft.wavilon.service.CdrEktorpDatabaseService;
import com.aimprosoft.wavilon.spring.ObjectFactory;
import com.liferay.portal.model.User;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.util.PortalUtil;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.Reindeer;

import javax.portlet.PortletRequest;
import java.io.IOException;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.UUID;

public class CallsContent extends Panel {
    private CdrEktorpDatabaseService cdrService = ObjectFactory.getBean(CdrEktorpDatabaseService.class);
    private ResourceBundle bundle;
    private PortletRequest request;
    private VerticalLayout mainLayout;
    private VerticalLayout itemContent;
    private CategoryFilter categoryFilter;
    private String headCaption;
    private CdrModel model = null;

    public CallsContent(ResourceBundle bundle) {
        this.bundle = bundle;
    }

    public void init(CategoryFilter categoryFilter, String headCaption) {
        request = ((GenericPortletApplication) getApplication()).getPortletRequest();
        this.categoryFilter = categoryFilter;
        this.headCaption = headCaption;

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

        PushThread thread = new PushThread(bundle);
        mainLayout.addComponent(thread);

        thread.init();
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
