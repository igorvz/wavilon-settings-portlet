package com.aimprosoft.wavilon.ui.menuitems;

import com.aimprosoft.wavilon.application.GenericPortletApplication;
import com.vaadin.terminal.FileResource;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.Reindeer;

import javax.portlet.PortletRequest;
import java.io.File;
import java.util.Iterator;
import java.util.ResourceBundle;

public class FilterCallsByLabels extends Panel {
    private ResourceBundle bundle;
    private PortletRequest request;
    private VerticalLayout mainLayout;
    private VerticalLayout itemContent;

    public FilterCallsByLabels(ResourceBundle bundle) {
        this.bundle = bundle;
    }

    public void init() {
        request = ((GenericPortletApplication) getApplication()).getPortletRequest();

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
        listViewLayout.setStyleName("item");


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
            DialogCell dialogCell = new DialogCell();
            itemContent.addComponent(dialogCell);
            dialogCell.init();
        }
    }

    private HorizontalLayout createListViewPart() {
        HorizontalLayout listViewPart = new HorizontalLayout();

        Label listViewLabel = new Label(bundle.getString("wavilon.activity.label.list.view"));

        Button fullView = new Button("Avatar", new Button.ClickListener() {
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

            }
        });


        Button shortView = new Button("None", new Button.ClickListener() {
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


            }
        });


        listViewPart.addComponent(listViewLabel);
        listViewPart.addComponent(fullView);
        listViewPart.addComponent(shortView);

        return listViewPart;
    }

    private class DialogCell extends HorizontalLayout {
        private Embedded avatar;
        private GridLayout mainContent;

        public DialogCell() {
        }

        public void init() {
            initLayout();
        }

        private void initLayout() {
            setStyleName("item");
            setWidth(100, Sizeable.UNITS_PERCENTAGE);

            createAvatar();
            addComponent(avatar);

            mainContent = new GridLayout(9, 7);
            addComponent(mainContent);
            mainContent.setWidth(100, Sizeable.UNITS_PERCENTAGE);
            mainContent.setStyleName("mainItemContent");
            createInfoPanel();

            setExpandRatio(avatar, 1);
            setExpandRatio(mainContent, 40);

        }

        private void createInfoPanel() {
            //first row
            Label name = new Label("Kathleen  Byrne ");
            mainContent.addComponent(name, 0, 0, 1, 0);

            Label ongoing = new Label("_ ongoing...");
            mainContent.addComponent(ongoing, 2, 0, 3, 0);

            Label timerTop;
            timerTop = new Label("9:24");
            mainContent.addComponent(timerTop, 4, 0);
            timerTop.addStyleName("itemTimerTop");


            Label count = new Label("2");
            mainContent.addComponent(count, 7, 0);

            final Button hideChatButton = new Button();
            mainContent.addComponent(hideChatButton, 8, 0);
            mainContent.setComponentAlignment(hideChatButton, Alignment.TOP_RIGHT);


            //second row
            Label categoriesAndLabels = new Label("Categories & Labels");
            mainContent.addComponent(categoriesAndLabels, 0, 1, 2, 1);

            Label category = new Label("Support");
            mainContent.addComponent(category, 3, 1, 4, 1);

            Button addCategoryButton = new Button("+");
            mainContent.addComponent(addCategoryButton, 5, 1);

            //third row
            Label timerBottom;
            timerBottom = new Label("9:24");
            mainContent.addComponent(timerBottom, 0, 2);
            timerBottom.addStyleName("itemTimerBottom");


            //
            final VerticalLayout chat = new VerticalLayout();
            chat.setStyleName("chat");
            mainContent.addComponent(chat, 0, 3, 8, 3);
            fillChatLayout(chat);

            final TextArea textArea = new TextArea();
            mainContent.addComponent(textArea, 0, 4, 7, 6);
            textArea.setWidth(100, Sizeable.UNITS_PERCENTAGE);


            final Button addNoteButton = new Button("Add Note");
            mainContent.addComponent(addNoteButton, 8, 6);
            mainContent.setComponentAlignment(addNoteButton, Alignment.BOTTOM_RIGHT);


            Button.ClickListener hideChatListener = new Button.ClickListener() {
                public void buttonClick(Button.ClickEvent event) {
                    if (chat.isVisible()) {
                        chat.setVisible(false);
                        textArea.setVisible(false);
                        addNoteButton.setVisible(false);
                        event.getButton().setCaption("Notes v ");
                    } else {
                        chat.setVisible(true);
                        textArea.setVisible(true);
                        addNoteButton.setVisible(true);
                        event.getButton().setCaption("Notes ^ ");
                    }

                }
            };
            hideChatButton.addListener(hideChatListener);


            chat.setVisible(false);
            textArea.setVisible(false);
            addNoteButton.setVisible(false);
            hideChatButton.setCaption("Notes v ");

        }

        private void createAvatar() {


            File file = new File("deactivated_clo.png");
            avatar = new Embedded(null, new FileResource(file, getApplication()));
            avatar.setHeight("100px");
            avatar.setWidth("100px");
            avatar.addStyleName("imgColumn");
        }

        private void fillChatLayout(VerticalLayout chat) {
            //todo iteration adding cells from DB
            for (int i = 0; i < 2; i++) {
                VerticalLayout note = createNote();
                chat.addComponent(note);
                note.addStyleName("note");
            }
        }


        private VerticalLayout createNote() {

            VerticalLayout noteContent = new VerticalLayout();
            HorizontalLayout node = new HorizontalLayout();

            Label nodeFromAgent = new Label("note from agent");
            nodeFromAgent.addStyleName("caption");

            Button nodeBookButton = new Button();
            Button closeButton = new Button("-");

            node.addComponent(nodeFromAgent);
            node.addComponent(nodeBookButton);
            node.addComponent(closeButton);

            Label message = new Label("Message form agent");

            noteContent.addComponent(node);
            noteContent.addComponent(message);

            return noteContent;
        }

    }

}
