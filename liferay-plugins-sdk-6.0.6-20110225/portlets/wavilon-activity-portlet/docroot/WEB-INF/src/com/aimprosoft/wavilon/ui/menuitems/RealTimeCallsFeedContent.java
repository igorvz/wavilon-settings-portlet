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
//        for (int i = 0; i < 2; i++) {
            DialogCell dialogCell = new DialogCell();
            mainContent.addComponent(dialogCell);
            dialogCell.init();
//        }
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
            info.addStyleName("infoLayout");

            VerticalLayout chat = new VerticalLayout();
            addComponent(chat);
            chat.setStyleName("chat");

            HorizontalLayout textArea = new HorizontalLayout();
            addComponent(textArea);

            fillLayouts(info, chat, textArea);
        }

        private void fillLayouts(HorizontalLayout info, final VerticalLayout chat, final HorizontalLayout textArea) {
            Embedded image = createImageColumn();
            image.addStyleName("imgColumn");

            VerticalLayout textInfo = new VerticalLayout();

            HorizontalLayout nameRow = new HorizontalLayout();
            nameRow.setWidth(100, Sizeable.UNITS_PERCENTAGE);

            HorizontalLayout nameOngoing = createNameOngoing();

            HorizontalLayout numberNodeAndNodesRow = new HorizontalLayout();
            fillNumberNodeAndNodesRow(numberNodeAndNodesRow);

            Button hideChatButton = new Button(" ^ ");
            numberNodeAndNodesRow.addComponent(hideChatButton);

            HorizontalLayout categoriesRow = createCategoriesRow();

            nameRow.addComponent(nameOngoing);
            nameRow.addComponent(numberNodeAndNodesRow);

            nameRow.setComponentAlignment(nameOngoing, Alignment.TOP_LEFT);
            nameRow.setComponentAlignment(numberNodeAndNodesRow, Alignment.TOP_LEFT);

            Label time = new Label("Time");

            textInfo.addComponent(nameRow);
            textInfo.addComponent(categoriesRow);
            textInfo.addComponent(time);

            info.addComponent(image);
            info.addComponent(textInfo);

            info.setExpandRatio(image, 1);
            info.setExpandRatio(textInfo, 9);

            fillChatLayout(chat);
            fillTextAreaLayout(textArea);

            Button.ClickListener hideChatListener = new Button.ClickListener() {
                public void buttonClick(Button.ClickEvent event) {
                      if (chat.isVisible()) {
                        chat.setVisible(false);
                        textArea.setVisible(false);
                    } else {
                        chat.setVisible(true);
                        textArea.setVisible(true);
                    }

                }
            };
            hideChatButton.addListener(hideChatListener);

        }

        private void fillNumberNodeAndNodesRow(HorizontalLayout numberNodeAndNodesRow) {
            Label numberNode = new Label("number");
            Label nodes = new Label("Nodes");

            numberNodeAndNodesRow.addComponent(numberNode);
            numberNodeAndNodesRow.addComponent(nodes);
        }

        private void fillChatLayout(VerticalLayout chat) {
            //todo iteration adding cells from DB
            for (int i = 0; i < 2; i++) {
                VerticalLayout body = createBody();
                chat.addComponent(body);
                body.addStyleName("messagesBody");
            }
        }

        private void fillTextAreaLayout(HorizontalLayout textArea) {
            TextArea area = new TextArea();
            area.setWidth(100, Sizeable.UNITS_PERCENTAGE);
            Button addNote = new Button("add note");

            textArea.addComponent(area);
            textArea.addComponent(addNote);

            textArea.setComponentAlignment(addNote, Alignment.TOP_RIGHT);
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


        private HorizontalLayout createNameOngoing() {
            HorizontalLayout nameOngoing = new HorizontalLayout();
            Label name = new Label("name");
            Label ongoing = new Label("\t ongoing...");

            nameOngoing.addComponent(name);
            nameOngoing.addComponent(ongoing);

            return nameOngoing;
        }


    }

}
