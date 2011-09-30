package com.aimprosoft.wavilon.ui.menuitems.forms;

import com.aimprosoft.wavilon.model.Attachment;
import com.aimprosoft.wavilon.model.Recording;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.*;
import com.vaadin.ui.Upload.Receiver;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class RecordingUploader extends VerticalLayout {

    private ProgressIndicator pi = new ProgressIndicator();

    private UploadReceiver receiver = new UploadReceiver();

    private Upload upload = new Upload(null, receiver);

    public RecordingUploader() {
    }

    public void init(final Recording recording) {
        setSpacing(true);

        receiver.setSlow(true);
        upload.setImmediate(false);

        HorizontalLayout uploadLabel = new HorizontalLayout();
        Label uploadFile = new Label("Upload file");

        uploadLabel.addComponent(uploadFile);
        uploadLabel.addComponent(upload);

        addComponent(uploadLabel);

        final Label progress = new Label("Progress");
        progress.setVisible(false);
        pi.setVisible(false);

        final HorizontalLayout progressRow = new HorizontalLayout();
        progressRow.addComponent(progress);

        VerticalLayout progressMessageLayout = new VerticalLayout();

        HorizontalLayout progressLayoutCancelButton = new HorizontalLayout();

        final Label succeededMessage = new Label();
        succeededMessage.setVisible(false);

        progressMessageLayout.addComponent(progressLayoutCancelButton);
        progressMessageLayout.addComponent(succeededMessage);

        progressRow.addComponent(progressMessageLayout);

        addComponent(progressRow);

        TextArea area = new TextArea();
        area.setWidth(500, Sizeable.UNITS_PIXELS);
        area.setValue("You can upload a custom file in the WAV or MP3 format.\n" +
                "Please do not upload any copyrighted files without permission");
        area.setReadOnly(true);
        addComponent(area);

        final Button cancelProcessing = new Button("Cancel");
        cancelProcessing.addListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                upload.interruptUpload();
                progress.setVisible(false);
                pi.setVisible(false);
                cancelProcessing.setVisible(false);
                succeededMessage.setVisible(false);
            }
        });

        cancelProcessing.setVisible(false);

        progressLayoutCancelButton.addComponent(pi);
        progressLayoutCancelButton.addComponent(cancelProcessing);

        upload.addListener(new Upload.StartedListener() {
            public void uploadStarted(Upload.StartedEvent event) {
                // This method gets called immediatedly after upload is started
                progress.setVisible(true);
                upload.setVisible(true);
                succeededMessage.setVisible(false);
                pi.setValue(0f);
                pi.setPollingInterval(500);
                pi.setVisible(true);
                cancelProcessing.setVisible(true);
            }
        });

        upload.addListener(new Upload.ProgressListener() {
            public void updateProgress(long readBytes, long contentLength) {
                // This method gets called several times during the update
                pi.setValue(new Float(readBytes / (float) contentLength));
            }
        });

        upload.addListener(new Upload.SucceededListener() {
            public void uploadSucceeded(Upload.SucceededEvent event) {
                // This method gets called when the upload finished successfully

                succeededMessage.setValue("Uploading file \"" + event.getFilename()
                        + "\" succeeded");
                succeededMessage.setVisible(true);

                pi.setVisible(false);
                cancelProcessing.setVisible(false);

                Attachment attachment = new Attachment();
                attachment.setContentType(event.getMIMEType());

                File file = new File(event.getFilename());
                try {
                    attachment.setData(FileUtils.readFileToByteArray(file));
                } catch (IOException e) {
                }

                Map<String, Attachment> data = new HashMap<String, Attachment>();
                data.put(event.getFilename(), attachment);
                recording.setAttachments(data);
            }
        });
    }

    public static class UploadReceiver implements Receiver {

        private String fileName;
        private String mtype;
        private boolean sleep;
        private int total = 0;

        public OutputStream receiveUpload(String filename, String mimetype) {
            fileName = filename;
            mtype = mimetype;
            return new OutputStream() {
                @Override
                public void write(int b) throws IOException {
                    total++;
                    if (sleep && total % 10000 == 0) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
            };
        }

        public String getFileName() {
            return fileName;
        }

        public String getMimeType() {
            return mtype;
        }

        public void setSlow(boolean value) {
            sleep = value;
        }
    }
};