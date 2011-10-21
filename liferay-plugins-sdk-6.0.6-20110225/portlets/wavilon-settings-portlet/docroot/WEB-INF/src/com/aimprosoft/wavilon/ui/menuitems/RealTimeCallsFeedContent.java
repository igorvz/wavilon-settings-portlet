package com.aimprosoft.wavilon.ui.menuitems;

import com.aimprosoft.wavilon.application.GenericPortletApplication;
import com.vaadin.terminal.FileResource;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.*;

import javax.portlet.PortletRequest;
import java.io.File;
import java.util.ResourceBundle;

public class RealTimeCallsFeedContent extends VerticalLayout {
    private ResourceBundle bundle;
    private PortletRequest request;

    public RealTimeCallsFeedContent(ResourceBundle bundle) {
        this.bundle = bundle;
    }

    public void init() {
        request = ((GenericPortletApplication) getApplication()).getPortletRequest();
        setWidth(100, Sizeable.UNITS_PERCENTAGE);

        initLayout();
    }

    private void initLayout() {
        Label headLabel = new Label(bundle.getString("wavilon.activity.menuitem.real.time.calls.feed"));
        addComponent(headLabel);
        headLabel.setStyleName("head");

        HorizontalLayout listViewPart = createListViewPart();
        addComponent(listViewPart);
        setComponentAlignment(listViewPart, Alignment.TOP_RIGHT);

        createMainContent();
    }

    private void createMainContent() {
        VerticalLayout mainContent = new VerticalLayout();
        addComponent(mainContent);

        //todo iteration adding cells from DB
        for (int i = 0; i < 2; i++) {
            DialogCell dialogCell = new DialogCell();
            mainContent.addComponent(dialogCell);
            dialogCell.init();
        }
    }

    private HorizontalLayout createListViewPart() {
        HorizontalLayout listViewPart = new HorizontalLayout();

        Label listViewLabel = new Label(bundle.getString("wavilon.activity.label.list.view"));

        Button fullView = new Button("img block", new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {

                getWindow().executeJavaScript("showImgBox()");

            }
        });


        Button shortView = new Button("img none", new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {

                getWindow().executeJavaScript("hideImgBox()");

            }
        });


        listViewPart.addComponent(listViewLabel);
        listViewPart.addComponent(fullView);
        listViewPart.addComponent(shortView);

        return listViewPart;
    }

    private static class DialogCell extends VerticalLayout {


        public DialogCell() {
        }

        public void init() {
            initLayout();
        }

        private void initLayout() {
            setWidth(100, Sizeable.UNITS_PERCENTAGE);

            HorizontalLayout info = new HorizontalLayout();
            addComponent(info);
            info.setWidth(100, Sizeable.UNITS_PERCENTAGE);
            fillInfoLayout(info);

            VerticalLayout chat = new VerticalLayout();
            chat.setStyleName("chat");
            addComponent(chat);
            fillChat(chat);


            HorizontalLayout testArea = createTextArea();
            addComponent(testArea);
        }

        private void fillInfoLayout(HorizontalLayout info) {
            Embedded image = createImageColumn();
            image.addStyleName("imgColumn");

            VerticalLayout textInfo = createHead();

            info.addComponent(image);
            info.addComponent(textInfo);

            info.setExpandRatio(image, 1);
            info.setExpandRatio(textInfo, 9);
        }


        private void fillChat(VerticalLayout chat) {

            //todo iteration adding cells from DB
            for (int i = 0; i < 3; i++) {
                VerticalLayout body = createBody();
                chat.addComponent(body);
                body.addStyleName("messagesBody");
            }

        }

        private HorizontalLayout createTextArea() {
            HorizontalLayout textArea = new HorizontalLayout();
            textArea.setWidth(100, Sizeable.UNITS_PERCENTAGE);

            TextArea area = new TextArea();
            area.setWidth(100, Sizeable.UNITS_PERCENTAGE);
            Button addNote = new Button("add note");

            textArea.addComponent(area);
            textArea.addComponent(addNote);

            textArea.setComponentAlignment(addNote, Alignment.TOP_RIGHT);

            return textArea;
        }

        private VerticalLayout createBody() {
            VerticalLayout bodyContent = new VerticalLayout();
            HorizontalLayout node = new HorizontalLayout();

            Label nodeFromAgent = new Label("node from agent");
            nodeFromAgent.addStyleName("caption");

            Button nodeBookButton = new Button();
            Button closeButton = new Button("-");

            node.addComponent(nodeFromAgent);
            node.addComponent(nodeBookButton);
            node.addComponent(closeButton);

            Label message = new Label("Message form agent");

            bodyContent.addComponent(node);
            bodyContent.addComponent(message);

            return bodyContent;
        }

        private Embedded createImageColumn() {
            File file = new File("deactivated_clo.png");

            Embedded image = new Embedded(null, new FileResource(file, getApplication()));
            image.setHeight("150px");
            image.setWidth("150px");

            return image;
        }

        private VerticalLayout createHead() {

            VerticalLayout headContent = new VerticalLayout();

            HorizontalLayout nameRow = new HorizontalLayout();
            nameRow.setWidth(100, Sizeable.UNITS_PERCENTAGE);

            HorizontalLayout nameOngoing = createNameOngoing();
            HorizontalLayout numberNodeAndNodesRow = createNumberNodeAndNodes();
            HorizontalLayout categoriesRow = createCategoriesRow();

            nameRow.addComponent(nameOngoing);
            nameRow.addComponent(numberNodeAndNodesRow);

            nameRow.setComponentAlignment(nameOngoing, Alignment.TOP_LEFT);
            nameRow.setComponentAlignment(numberNodeAndNodesRow, Alignment.TOP_RIGHT);

            Label time = new Label("Time");

            headContent.addComponent(nameRow);
            headContent.addComponent(categoriesRow);
            headContent.addComponent(time);

            return headContent;
        }

        private HorizontalLayout createCategoriesRow() {
            HorizontalLayout categorySupportButton = new HorizontalLayout();
            Label category = new Label("Categories & Labels");

            Button support = new Button();
            support.setStyleName("link");
            support.setCaption("Support");

            Button problematic = new Button();
            problematic.setStyleName("link");
            problematic.setCaption("Problematic customer");

            Button addButton = new Button("+");

            categorySupportButton.addComponent(category);
            categorySupportButton.addComponent(problematic);
            categorySupportButton.addComponent(addButton);

            return categorySupportButton;
        }

        private HorizontalLayout createNumberNodeAndNodes() {
            HorizontalLayout numberNodeAndNodes = new HorizontalLayout();
            Label numberNode = new Label("number");
            Label nodes = new Label("Nodes");
            Button button = new Button(" ^ ");

            numberNodeAndNodes.addComponent(numberNode);
            numberNodeAndNodes.addComponent(nodes);
            numberNodeAndNodes.addComponent(button);

            return numberNodeAndNodes;
        }

        private HorizontalLayout createNameOngoing() {
            HorizontalLayout nameOngoing = new HorizontalLayout();
            Label name = new Label("name");
            Label ongoing = new Label("ongoing...");

            nameOngoing.addComponent(name);
            nameOngoing.addComponent(ongoing);

            return nameOngoing;
        }


    }

}
