package com.aimprosoft.wavilon.ui.menuitems;

import com.aimprosoft.wavilon.application.GenericPortletApplication;
import com.aimprosoft.wavilon.model.Attachment;
import com.aimprosoft.wavilon.service.AvatarService;
import com.aimprosoft.wavilon.spring.ObjectFactory;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.Reindeer;

import javax.portlet.PortletRequest;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;

public class RealTimeCallsFeedContent extends Panel {
    private AvatarService avatarService = ObjectFactory.getBean(AvatarService.class);
    private Map<String, Attachment> avatarsMap;
    private ResourceBundle bundle;
    private PortletRequest request;
    private VerticalLayout mainLayout;
    private VerticalLayout itemContent;

    public RealTimeCallsFeedContent(ResourceBundle bundle) {
        this.bundle = bundle;
    }

    public void init() {
        request = ((GenericPortletApplication) getApplication()).getPortletRequest();
        avatarsMap = avatarService.getAvatars();

        setSizeFull();
        setStyleName(Reindeer.PANEL_LIGHT);
        setScrollable(true);

        initLayout();
    }

    private void initLayout() {
        mainLayout = new VerticalLayout();
        setContent(mainLayout);

        Label headLabel = new Label(bundle.getString("wavilon.activity.menuitem.real.time.calls.feed"));
        mainLayout.addComponent(headLabel);
        headLabel.setStyleName("head");

        VerticalLayout listViewLayout = new VerticalLayout();
        listViewLayout.setWidth(100, Sizeable.UNITS_PERCENTAGE);
        mainLayout.addComponent(listViewLayout);
        listViewLayout.setStyleName("listViewLayout");


        HorizontalLayout listViewButtons = createListViewPart();
        listViewLayout.addComponent(listViewButtons);
        listViewLayout.setComponentAlignment(listViewButtons, Alignment.TOP_RIGHT);

        createMainContent();
    }

    private void createMainContent() {
        itemContent = new VerticalLayout();
        mainLayout.addComponent(itemContent);

        //todo iteration adding cells from DB
        for (int i = 0; i < 2; i++) {
            DialogCell dialogCell = new DialogCell(bundle);
            itemContent.addComponent(dialogCell);
            dialogCell.init(avatarsMap);
        }
    }

    private HorizontalLayout createListViewPart() {
        HorizontalLayout listViewPart = new HorizontalLayout();
        Label listViewLabel = new Label(bundle.getString("wavilon.activity.label.list.view"));


        final Button avatarButton = new NativeButton();
        final Button nonAvatarButton = new NativeButton();
        avatarButton.setStyleName("avatarButtonSelect");
        nonAvatarButton.setStyleName("nonAvatarButtonNonSelect");


        Button.ClickListener avatarButtonListener = new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {

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

            }
        };

        Button.ClickListener nonAvatarButtonListener = new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {

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
            }
        };


        avatarButton.addListener(avatarButtonListener);
        nonAvatarButton.addListener(nonAvatarButtonListener);


        listViewPart.addComponent(listViewLabel);
        listViewPart.addComponent(avatarButton);
        listViewPart.addComponent(nonAvatarButton);

        return listViewPart;
    }

}
