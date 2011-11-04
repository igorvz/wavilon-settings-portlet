package com.aimprosoft.wavilon.ui.menuitems;

import com.aimprosoft.wavilon.couch.PushModel;
import com.aimprosoft.wavilon.model.IcePushModel;
import com.aimprosoft.wavilon.service.IcePushDatabaseService;
import com.aimprosoft.wavilon.spring.ObjectFactory;
import com.vaadin.ui.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class IcePushContent extends VerticalLayout {

    private IcePushDatabaseService service = ObjectFactory.getBean(IcePushDatabaseService.class);
//    private ICEPush pusher;

    public IcePushContent() {
//    public IcePushContent(ICEPush pusher) {
//        this.pusher = pusher;
    }

    public void init() {
        initLayout();
//        addComponent(pusher);
    }

    private void initLayout() {
        HorizontalLayout chat = new HorizontalLayout();

        final TextArea area = createTextArea();


        Button addNote = new Button("Add note");
        addNote.addListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                PushModel model = new PushModel();
                model.setId(UUID.randomUUID().toString());
                model.setType("push");

                IcePushModel icePushModel = new IcePushModel();
                icePushModel.setNote((String) area.getValue());

                try {
                    service.addIcePushMode(icePushModel, model);
                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }

                area.setValue("");
                Label newNote = new Label();
                newNote.setValue(icePushModel.getNote());
                addComponent(newNote);

                new ComputeThread().start();
            }
        });
        chat.addComponent(area);
        chat.addComponent(addNote);

//        VerticalLayout noteList = createNoteList();

        addComponent(chat);

    }

    private VerticalLayout createNoteList() {
        VerticalLayout noteList = new VerticalLayout();

        List<PushModel> pushModels = createPushModel();

        for (PushModel model : pushModels) {
            Label note = new Label();
//            note.setValue(model.getNote());

            noteList.addComponent(note);
        }
        return noteList;
    }

    private List<PushModel> createPushModel() {
        return null;  //To change body of created methods use File | Settings | File Templates.
    }

    private TextArea createTextArea() {
        TextArea area = new TextArea();

        area.setHeight("100px");
        area.setWidth("500px");

        return area;
    }

    private class ComputeThread extends Thread {
        @Override
        public void run() {
            final Label time = new Label();
            addComponent(time);
            DateFormat format = new SimpleDateFormat("HH:mm:ss");
            while (true) {

                Date date = new Date();
                time.setValue(format.format(date));


//                pusher.push();

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        }
    }
}
