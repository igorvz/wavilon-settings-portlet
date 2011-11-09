package com.aimprosoft.wavilon.ui;

import com.aimprosoft.wavilon.application.GenericPortletApplication;
import com.aimprosoft.wavilon.ui.menuitems.CallsContent;
import com.aimprosoft.wavilon.ui.menuitems.CategoryFilter;
import com.aimprosoft.wavilon.util.CouchModelUtil;
import com.liferay.portal.util.PortalUtil;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.Reindeer;
import org.icepush.PushContext;
import org.vaadin.artur.icepush.ICEPush;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import java.util.Iterator;
import java.util.ResourceBundle;

public class ActivityPage extends VerticalLayout {
    private ResourceBundle bundle;
    private VerticalLayout leftColumn;
    private VerticalLayout detailsContent;


    public ActivityPage(final ResourceBundle bundle) {
        this.bundle = bundle;
    }

    public void init() {
        addStyleName("settingsPanel");

        checkPush();


        HorizontalSplitPanel panel = new HorizontalSplitPanel();
        panel.setSplitPosition(250, Sizeable.UNITS_PIXELS);
        panel.addStyleName(Reindeer.SPLITPANEL_SMALL);
        panel.setLocked(false);
        panel.setHeight(550, Sizeable.UNITS_PIXELS);
        panel.setWidth("100%");
        addComponent(panel);

        leftColumn = new VerticalLayout();
        leftColumn.setStyleName("leftcolumn");
        panel.setFirstComponent(leftColumn);

        VerticalLayout rightColumn = new VerticalLayout();
        rightColumn.setStyleName("rightcolumn");
        rightColumn.setMargin(false);
        rightColumn.setSizeFull();
        panel.setSecondComponent(rightColumn);

        VerticalLayout detailsBox = new VerticalLayout();
        detailsBox.setSizeFull();
        setSides(detailsBox);

        detailsContent = new VerticalLayout();
        detailsContent.setSizeFull();
        detailsBox.addComponent(detailsContent);

        rightColumn.addComponent(detailsBox);

        addButtons();

        detailsContent.removeAllComponents();
        CallsContent callsContent = new CallsContent(bundle);
        detailsContent.addComponent(callsContent);
        callsContent.init(null, bundle.getString("wavilon.activity.menuitem.real.time.calls.feed"));

    }

    private void addButtons() {

        Button realTimeCallsFeed = new NativeButton(bundle.getString("wavilon.activity.menuitem.real.time.calls.feed"));
        realTimeCallsFeed.addStyleName("button");
        realTimeCallsFeed.addListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                Button button = event.getButton();

                assignActiveButton(button);
                detailsContent.removeAllComponents();
                CallsContent callsContent = new CallsContent(bundle);
                detailsContent.addComponent(callsContent);
                callsContent.init(null, bundle.getString("wavilon.activity.menuitem.real.time.calls.feed"));

            }
        });

        Button filterCallsByLabels = new NativeButton(bundle.getString("wavilon.activity.menuitem.filter.calls.by.labels"));
        filterCallsByLabels.addStyleName("button");
        filterCallsByLabels.addListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                Button button = event.getButton();

                assignActiveButton(button);
                detailsContent.removeAllComponents();
                CallsContent callsContent = new CallsContent(bundle);
                detailsContent.addComponent(callsContent);
                CategoryFilter categoryFilter = new CategoryFilter(bundle);
                callsContent.init(categoryFilter, bundle.getString("wavilon.activity.menuitem.filter.calls.by.labels"));

            }
        });

        leftColumn.addComponent(realTimeCallsFeed);
        leftColumn.addComponent(filterCallsByLabels);
    }

    private void checkPush() {
        GenericPortletApplication application = (GenericPortletApplication) getApplication();
        PortletRequest request = application.getPortletRequest();
        PortletResponse response = application.getPortletResponse();
        PushContext pushContext = ICEPush.getPushContext(application.getContext());

        ICEPush icePush = new ICEPush();
        getApplication().getMainWindow().addComponent(icePush);

        String organizationId = CouchModelUtil.getOrganizationId(request).toString();
        String pushId = pushContext.createPushId(PortalUtil.getHttpServletRequest(request), PortalUtil.getHttpServletResponse(response));
        pushContext = ICEPush.getPushContext(getApplication().getContext());
        pushContext.addGroupMember(organizationId, pushId);
    }

    private void assignActiveButton(Button button) {
        removeButtonSelection();
        button.addStyleName("buttonSelect");

    }

    //remove selection for all buttons
    private void removeButtonSelection() {
        Iterator<Component> componentIterator = leftColumn.getComponentIterator();
        while (componentIterator.hasNext()) {
            Component component = componentIterator.next();
            //make sure component is button
            component.removeStyleName("buttonSelect");

        }
    }


    private void setSides(Component component) {
        component.setWidth(100, Sizeable.UNITS_PERCENTAGE);
        component.setHeight(100, Sizeable.UNITS_PERCENTAGE);
    }
}