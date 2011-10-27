package com.aimprosoft.wavilon.ui.menuitems;

import com.aimprosoft.wavilon.application.GenericPortletApplication;
import com.aimprosoft.wavilon.model.Attachment;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.*;
import org.vaadin.imagefilter.Image;

import javax.portlet.PortletRequest;
import java.util.*;

public class DialogCell extends HorizontalLayout {
    private Embedded avatar;
    private GridLayout mainContent;
    private Map<String, Attachment> avatarsMap;
    private ResourceBundle bundle;
    private PortletRequest request;


    public DialogCell(ResourceBundle bundle) {
        this.bundle = bundle;

    }

    public void init(Map<String, Attachment> avatarsMap) {
        request = ((GenericPortletApplication) getApplication()).getPortletRequest();
        this.avatarsMap = avatarsMap;
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
        name.addStyleName("callerName");

        Label callStatus = new Label("ongoing ...");
        mainContent.addComponent(callStatus, 2, 0, 3, 0);
        callStatus.setStyleName("callStatus");

        Label timerTop;
        timerTop = new Label("9:24");
        mainContent.addComponent(timerTop, 4, 0);
        timerTop.addStyleName("itemTimerTop");


        Label count = new Label("2");
        mainContent.addComponent(count, 7, 0);
        count.addStyleName("itemMessagesCounter");

        final Button hideChatButton = new NativeButton("Notes");
        mainContent.addComponent(hideChatButton, 8, 0);
        mainContent.setComponentAlignment(hideChatButton, Alignment.TOP_RIGHT);

        //second row
        Label categoriesAndLabels = new Label("Categories & Labels:");
        mainContent.addComponent(categoriesAndLabels, 0, 1, 2, 1);
        categoriesAndLabels.setStyleName("categoriesAndLabelsCaption");

        Label category = new Label("Support");
        mainContent.addComponent(category, 3, 1, 4, 1);
        category.addStyleName("category");

        Button addCategoryButton = new NativeButton();
        mainContent.addComponent(addCategoryButton, 5, 1);
        addCategoryButton.addStyleName("itemAddCategoryButton");


        //third row
        Label timerBottom;
        timerBottom = new Label("9:24");
        mainContent.addComponent(timerBottom, 0, 2);
        timerBottom.addStyleName("itemTimerBottom");



        final VerticalLayout chat = new VerticalLayout();
        mainContent.addComponent(chat, 0, 3, 8, 3);
        chat.setStyleName("chat");
        fillChatLayout(chat);


        GridLayout newNote = new GridLayout(5, 3);
        chat.addComponent(newNote);
        newNote.setWidth(100, Sizeable.UNITS_PERCENTAGE);


        final TextArea textArea = new TextArea();
        newNote.addComponent(textArea, 0 ,0, 3, 2);
        textArea.setWidth(100, Sizeable.UNITS_PERCENTAGE);

        final Button addNoteButton = new NativeButton("Add Note");
        newNote.addComponent(addNoteButton, 4, 2);
        addNoteButton.setStyleName("itemAddNoteButton");

        Button.ClickListener hideChatListener = new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                Button button = event.getButton();
                hideChat(chat.isVisible(), button, chat);
            }
        };
        hideChatButton.addListener(hideChatListener);

        hideChat(chat.isVisible(), hideChatButton, chat);
    }

    private void hideChat(boolean flag, Button button, VerticalLayout chat) {
        chat.setVisible(!flag);

        if (flag) {
            button.removeStyleName("itemHideChatButtonUp");
            button.addStyleName("itemHideChatButtonDown");
        } else {
            button.removeStyleName("itemHideChatButtonDown");
            button.addStyleName("itemHideChatButtonUp");
        }
    }

    private void createAvatar() {
        avatar = new Image(avatarsMap.get("crying.png").getData(), true);
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
        VerticalLayout note = new VerticalLayout();


        HorizontalLayout noteInfo = new HorizontalLayout();
        noteInfo.addStyleName("noteInfo");

        Label nodeFrom = new Label("Note from ");

        Label agentName = new Label("Agent Test");

        Label at = new Label(" at ");

        Label date = new Label(" 2011-10-19 10:39:42");

        Button editNoteButton = new NativeButton();
        editNoteButton.addStyleName("editNoteButton");

        Button removeNoteButton = new NativeButton();
        removeNoteButton.addStyleName("removeNoteButton");


        noteInfo.addComponent(nodeFrom);
        noteInfo.addComponent(agentName);
        noteInfo.addComponent(at);
        noteInfo.addComponent(date);
        noteInfo.addComponent(editNoteButton);
        noteInfo.addComponent(removeNoteButton);


        Label message = new Label("Message form agent");
        message.setStyleName("message");

        note.addComponent(noteInfo);
        note.addComponent(message);

        return note;
    }

}
