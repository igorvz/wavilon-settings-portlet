package com.aimprosoft.wavilon.ui.menuitems;

import com.vaadin.ui.*;

import javax.portlet.PortletRequest;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class CategoryFilter extends HorizontalLayout {
    private ResourceBundle bundle;
    private PortletRequest request;
    private List<String> category;

    public CategoryFilter(ResourceBundle bundle) {
        this.bundle = bundle;
    }

    public void init(PortletRequest request) {
        this.request = request;
        fillCategory();
        initLayout();
    }

    private void initLayout() {
        setSizeUndefined();
        setStyleName("categoryFilterLayout");

        Label label = new Label("Labels:");
        addComponent(label);
        label.setStyleName("labelCategory");


        VerticalLayout availableLabels = new VerticalLayout();
        addComponent(availableLabels);
        availableLabels.setStyleName("availableLabels");
        fillLabels(availableLabels);
    }

    private void fillLabels(VerticalLayout availableLabels) {
        HorizontalLayout categoriesRow = null;
        int counter = 0;

        for (String s : category) {
            if (0 == counter) {
                categoriesRow = new HorizontalLayout();
                availableLabels.addComponent(categoriesRow);
                categoriesRow.setSizeUndefined();
            }


            final Button categoryButton = new NativeButton(s);
            categoriesRow.addComponent(categoryButton);

            Button.ClickListener clickListener = new Button.ClickListener() {
                public void buttonClick(Button.ClickEvent event) {
                    if ("categoryButtonSelect".equals(categoryButton.getStyleName())) {
                        categoryButton.removeStyleName("categoryButtonSelect");
                        categoryButton.setStyleName("categoryButtonUnSelect");
                    } else {
                        categoryButton.removeStyleName("categoryButtonUnSelect");
                        categoryButton.setStyleName("categoryButtonSelect");
                    }
                }
            };
            categoryButton.addListener(clickListener);

            categoryButton.setStyleName("categoryButtonSelect");


            counter+=s.length();
            if (counter > 60) {
                counter = 0;
            }

        }

    }


    private void fillCategory() {
        category = new LinkedList<String>();
        category.add("Support");
        category.add("Problematic customer");
        category.add("Human Resources");
        category.add("Design Agency");
        category.add("Designers");
        category.add("Delivery");
        category.add("Fashion");
        category.add("Software");
        category.add("Web Apps");
        category.add("Magazines");
        category.add("Music");
        category.add("Portfolio");
    }
}
