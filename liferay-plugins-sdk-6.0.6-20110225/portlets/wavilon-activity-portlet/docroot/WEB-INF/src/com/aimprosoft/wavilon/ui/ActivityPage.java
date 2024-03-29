package com.aimprosoft.wavilon.ui;

import com.aimprosoft.wavilon.ui.menuitems.CallsContent;
import com.aimprosoft.wavilon.ui.menuitems.CategoryFilter;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.Reindeer;

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
        getWindow().executeJavaScript("bindBlockUI()");
        getWindow().executeJavaScript("bindRemoveBlockUI()");

        HorizontalSplitPanel panel = new HorizontalSplitPanel();
        panel.setSplitPosition(250, Sizeable.UNITS_PIXELS);
        panel.addStyleName(Reindeer.SPLITPANEL_SMALL);
        panel.setLocked(false);
        panel.setHeight(550, Sizeable.UNITS_PIXELS);
        panel.setWidth("100%");
        addComponent(panel);

        leftColumn = new VerticalLayout();
        leftColumn.addStyleName("leftcolumn");
        panel.setFirstComponent(leftColumn);

        VerticalLayout rightColumn = new VerticalLayout();
        rightColumn.addStyleName("rightcolumn");
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

        getWindow().executeJavaScript("blockPage()");

        detailsContent.removeAllComponents();
        CallsContent callsContent = new CallsContent(bundle);
        detailsContent.addComponent(callsContent);
        callsContent.init(null, bundle.getString("wavilon.activity.menuitem.real.time.calls.feed"));

        getWindow().executeJavaScript("unblockPage()");

    }

    private void addButtons() {

        Button realTimeCallsFeed = new NativeButton(bundle.getString("wavilon.activity.menuitem.real.time.calls.feed"));
        realTimeCallsFeed.addStyleName("buttonSelect");
        realTimeCallsFeed.addListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {

                Button button = event.getButton();
                assignActiveButton(button);
                detailsContent.removeAllComponents();
                CallsContent callsContent = new CallsContent(bundle);
                detailsContent.addComponent(callsContent);
                callsContent.init(null, bundle.getString("wavilon.activity.menuitem.real.time.calls.feed"));

                getWindow().executeJavaScript("unblockPage()");
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

                getWindow().executeJavaScript("unblockPage()");
            }
        });

        leftColumn.addComponent(realTimeCallsFeed);
        leftColumn.addComponent(filterCallsByLabels);
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