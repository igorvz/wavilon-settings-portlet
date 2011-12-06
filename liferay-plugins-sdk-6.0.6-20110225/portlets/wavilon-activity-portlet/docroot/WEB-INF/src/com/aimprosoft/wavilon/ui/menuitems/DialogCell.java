package com.aimprosoft.wavilon.ui.menuitems;

import com.aimprosoft.wavilon.application.GenericPortletApplication;
import com.aimprosoft.wavilon.couch.CouchModel;
import com.aimprosoft.wavilon.model.Note;
import com.aimprosoft.wavilon.model.Person;
import com.aimprosoft.wavilon.service.AvatarService;
import com.aimprosoft.wavilon.service.NoteDatabaseService;
import com.aimprosoft.wavilon.service.impl.NoteEktorpDatabaseImpl;
import com.aimprosoft.wavilon.spring.ObjectFactory;
import com.aimprosoft.wavilon.util.CouchModelUtil;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.*;
import org.vaadin.artur.icepush.ICEPush;
import org.vaadin.imagefilter.Image;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class DialogCell extends HorizontalLayout {
    private NoteDatabaseService noteService = ObjectFactory.getBean(NoteEktorpDatabaseImpl.class);
    private AvatarService avatarService = ObjectFactory.getBean(AvatarService.class);
    private Embedded avatar;
    private GridLayout mainContent;
    private ResourceBundle bundle;
    private PortletResponse response;
    private PortletRequest request;
    private List<CouchModel> notes;
    private Label count;
    private Person person;
    private VerticalLayout chat;
    private ICEPush icePush;
    private static Boolean pushMonitor = true;
    private static boolean condition = true;

    public DialogCell(ResourceBundle bundle) {
        this.bundle = bundle;
    }

    public void init(Person person) {
        this.person = person;
        request = ((GenericPortletApplication) PushThread.threadLocal.get()).getPortletRequest();

        icePush = new ICEPush();
        ((GenericPortletApplication) PushThread.threadLocal.get()).getMainWindow().addComponent(icePush);

        initLayout();
        new BackgroundThread().start();
    }

    private void initLayout() {

        setStyleName("item");
        setWidth(100, Sizeable.UNITS_PERCENTAGE);

        createAvatar();
        addComponent(avatar);
        setComponentAlignment(avatar, Alignment.TOP_CENTER);

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
        HorizontalLayout firstRow = new HorizontalLayout();
        mainContent.addComponent(firstRow, 0, 0);
        firstRow.setStyleName("firstRow");

        Label name = new Label(person.getName() + " " + person.getSurname());
        firstRow.addComponent(name);
        name.setStyleName("callerName");

        Label callStatus = new Label("ongoing ...");
        firstRow.addComponent(callStatus);
        callStatus.setStyleName("callStatus");

        String time = person.getTime();
        Label timerTop = new Label(time);
        mainContent.addComponent(timerTop, 1, 0);
        timerTop.setStyleName("itemTimerTop");

        HorizontalLayout displayNotes = new HorizontalLayout();
        mainContent.addComponent(displayNotes, 7, 0, 8, 0);
        mainContent.setComponentAlignment(displayNotes, Alignment.MIDDLE_RIGHT);


        count = new Label();
        displayNotes.addComponent(count);
        count.setStyleName("itemMessagesCounter");

        final Button hideChatButton = new NativeButton("Notes");
        displayNotes.addComponent(hideChatButton);
        mainContent.setComponentAlignment(hideChatButton, Alignment.TOP_RIGHT);

        //second row
        HorizontalLayout secondRow = new HorizontalLayout();
        mainContent.addComponent(secondRow, 0, 1, 6, 1);

        Label categoriesAndLabels = new Label("Categories & Labels:");
        secondRow.addComponent(categoriesAndLabels);
        categoriesAndLabels.setStyleName("categoriesAndLabelsCaption");


        for (String category : person.getCategories()) {
            Label categoryLabel = new Label(category);
            secondRow.addComponent(categoryLabel);
            categoryLabel.setStyleName("category");
        }


        Button addCategoryButton = new NativeButton();
        secondRow.addComponent(addCategoryButton);
        addCategoryButton.setStyleName("itemAddCategoryButton");

        secondRow.addStyleName("categoryNLabelsStyle");


        //third row
        Label timerBottom;
        timerBottom = new Label(time);
        mainContent.addComponent(timerBottom, 0, 2);
        timerBottom.setStyleName("itemTimerBottom");


        final VerticalLayout chatLayout = new VerticalLayout();
        mainContent.addComponent(chatLayout, 0, 3, 8, 3);
        chatLayout.setStyleName("chat");

        chat = new VerticalLayout();
        chatLayout.addComponent(chat);
        chat.setWidth(95, Sizeable.UNITS_PERCENTAGE);
        fillChatLayout(chat);

        GridLayout newNote = new GridLayout(5, 3);
        chatLayout.addComponent(newNote);
        newNote.setWidth(100, Sizeable.UNITS_PERCENTAGE);

        final TextArea textArea = new TextArea();
        newNote.addComponent(textArea, 0, 0, 3, 2);
        textArea.setWidth(99, Sizeable.UNITS_PERCENTAGE);
        textArea.setHeight(50, Sizeable.UNITS_PIXELS);
        textArea.setStyleName("itemTextAreaNote");

        final Button addNoteButton = new NativeButton("Add Note", new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {

                String noteContent = textArea.toString();
                Note note = new Note();
                note.setName("Boris");
                note.setContent(noteContent);

                Calendar cal = Calendar.getInstance();
                note.setUpdateDate(cal.getTime());

                CouchModel couchModel = CouchModelUtil.newCouchModel(request, "note");
                couchModel.setCdrId(person.getId());

                try {
                    noteService.addNote(note, couchModel);
                    notes.add(noteService.getModel(couchModel.getId()));
                } catch (IOException ignored) {
                }

                textArea.setValue("");

                changeCondition();
            }
        });
        newNote.addComponent(addNoteButton, 4, 2);
        newNote.setComponentAlignment(addNoteButton, Alignment.BOTTOM_RIGHT);
        addNoteButton.setStyleName("itemAddNoteButton");

        Button.ClickListener hideChatListener = new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                Button button = event.getButton();
                hideChat(chat.isVisible(), button, chatLayout);
            }
        };
        hideChatButton.addListener(hideChatListener);

        hideChat(chat.isVisible(), hideChatButton, chatLayout);
    }

    private void changeCondition() {
        synchronized (pushMonitor) {

            condition = false;
            pushMonitor.notifyAll();
        }
    }

    private void hideChat(boolean flag, Button button, VerticalLayout chat) {
        chat.setVisible(!flag);

        if (flag) {
            button.removeStyleName("itemHideChatButtonUp");
            button.setStyleName("itemHideChatButtonDown");
        } else {
            button.removeStyleName("itemHideChatButtonDown");
            button.setStyleName("itemHideChatButtonUp");
        }
    }

    private void createAvatar() {
        InputStream avatarInputStream = null;
        try {
            avatarInputStream = avatarService.getAvatar(person.getAvatarName());
            avatar = new Image(avatarInputStream);
        } finally {
            if (null != avatarInputStream) {
                try {
                    avatarInputStream.close();
                } catch (Exception ignored) {
                }
            }
        }
        avatar.setHeight("61px");
        avatar.setWidth("61px");
        avatar.setStyleName("imgColumn");
    }

    private void fillChatLayout(VerticalLayout chat) {
        notes = getAllNotes();
        count.setValue(String.valueOf(notes.size()));


//      todo iteration adding cells from DB
        for (CouchModel noteCouchModel : notes) {
            createNoteLayout(chat, noteCouchModel);
        }

    }

    private void createNoteLayout(VerticalLayout chat, CouchModel noteCouchModel) {
        VerticalLayout note = createNote(noteCouchModel);
        chat.addComponent(note);
        note.setStyleName("note");
    }

    private List<CouchModel> getAllNotes() {
        try {
            List<CouchModel> couchModelList = noteService.getAllUsersCouchModelNote(CouchModelUtil.getOrganizationId(request));

            if (1 < couchModelList.size()) {
                Collections.sort(couchModelList, new Comparator<CouchModel>() {
                    public int compare(CouchModel o1, CouchModel o2) {
                        Date d1 = new Date((Long) o1.getProperties().get("updateDate"));
                        Date d2 = new Date((Long) o2.getProperties().get("updateDate"));

                        return d1.compareTo(d2);
                    }
                });
            }

            return couchModelList;
        } catch (Exception e) {
            return new LinkedList<CouchModel>();
        }
    }

    private VerticalLayout createNote(final CouchModel noteCouchModel) {
        VerticalLayout note = new VerticalLayout();
        Note noteModel = getNoteModel(noteCouchModel);

        HorizontalLayout noteInfo = new HorizontalLayout();
        noteInfo.setStyleName("noteInfo");

        Label nodeFrom = new Label("Note from ");
        nodeFrom.setStyleName("wordSeparator");

        Label agentName = new Label(noteModel.getName());
        agentName.setStyleName("wordSeparator");

        Label at = new Label(" at ");
        at.setStyleName("wordSeparator");

        Label date = new Label(noteModel.getUpdateDate().toString());
        date.setStyleName("wordSeparator");

        Button editNoteButton = new NativeButton();
        editNoteButton.setStyleName("editNoteButton");

        Button removeNoteButton = new NativeButton("", new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                try {
                    noteService.removeNote(noteCouchModel.getId());
                    event.getButton().getParent().getParent().setVisible(false);
                    notes.remove(notes.indexOf(noteCouchModel));
                    count.setValue(String.valueOf(notes.size()));

                } catch (IOException ignored) {
                }


            }
        });
        removeNoteButton.setStyleName("removeNoteButton");


        noteInfo.addComponent(nodeFrom);
        noteInfo.addComponent(agentName);
        noteInfo.addComponent(at);
        noteInfo.addComponent(date);
        noteInfo.addComponent(editNoteButton);
        noteInfo.addComponent(removeNoteButton);


        Label message = new Label(noteModel.getContent());
        message.setStyleName("message");

        note.addComponent(noteInfo);
        note.addComponent(message);

        return note;
    }

    private Note getNoteModel(CouchModel noteCouchModel) {
        try {
            return noteService.getNote(noteCouchModel);
        } catch (IOException e) {
            return null;
        }
    }

    public class BackgroundThread extends Thread {

        @Override
        public void run() {
            while (true) {

                repaint();

                icePush.push();
            }
        }
    }

    public void repaint() {
        synchronized (pushMonitor) {

            while (condition) {
                try {
                    pushMonitor.wait();
                } catch (InterruptedException e) {
                }
            }
        }

        chat.removeAllComponents();
        fillChatLayout(chat);

        condition = true;
    }
}
