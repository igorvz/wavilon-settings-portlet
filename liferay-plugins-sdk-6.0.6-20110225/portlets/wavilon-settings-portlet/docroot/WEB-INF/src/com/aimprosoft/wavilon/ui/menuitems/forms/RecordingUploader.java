package com.aimprosoft.wavilon.ui.menuitems.forms;

import com.aimprosoft.wavilon.couch.Attachment;
import com.aimprosoft.wavilon.couch.CouchModel;
import com.aimprosoft.wavilon.model.Recording;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.*;
import com.vaadin.ui.Upload.Receiver;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class RecordingUploader extends VerticalLayout {
    private ResourceBundle bundle;

    private ProgressIndicator pi = new ProgressIndicator();
    private UploadReceiver receiver = new UploadReceiver();
    private Upload upload = new Upload(null, receiver);
    private File file;

    public RecordingUploader(final ResourceBundle bundle) {
        this.bundle = bundle;
    }

    public void init(final CouchModel model, final Recording recording, final Form form) {
        setSpacing(true);

        upload.setImmediate(false);
        upload.setButtonCaption(bundle.getString("wavilon.button.upload"));

        HorizontalLayout uploadLabel = new HorizontalLayout();
        Label uploadFile = new Label(bundle.getString("wavilon.form.recordings.upload.file"));

        uploadLabel.addComponent(uploadFile);
        uploadLabel.addComponent(upload);

        addComponent(uploadLabel);

        final Label progress = new Label(bundle.getString("wavilon.form.recordings.progress"));
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
        area.setWidth(330, Sizeable.UNITS_PIXELS);
        area.setValue(bundle.getString("wavilon.form.recordings.massage.part.first") + "\n" +
                bundle.getString("wavilon.form.recordings.massage.part.second"));
        area.setReadOnly(true);
        addComponent(area);

        final Button cancelProcessing = new Button(bundle.getString("wavilon.button.cancel"));
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
                try {
                    Thread.sleep(1);
                } catch (InterruptedException ignore) {
                }
            }
        });

        upload.addListener(new Upload.SucceededListener() {
            public void uploadSucceeded(Upload.SucceededEvent event) {
                // This method gets called when the upload finished successfully

                succeededMessage.setValue(bundle.getString("wavilon.form.recordings.progress.uploading.file"));
                succeededMessage.setVisible(true);
                succeededMessage.addStyleName("succeededMessage");

                pi.setVisible(false);
                cancelProcessing.setVisible(false);

                String fileType = event.getFilename().substring(event.getFilename().indexOf(".") + 1);
                String fileName = event.getFilename().substring(0, event.getFilename().indexOf("."));

                recording.setFileName(fileName);
                recording.setFileType(fileType);

                if (recording.getVersion() == null) {
                    recording.setVersion(0);
                }

                recording.setVersion(recording.getVersion()+1);

                Attachment attachment = new Attachment();
                attachment.setContentType(event.getMIMEType());

                if (form.getComponentError() != null) {
                    form.setComponentError(null);
                }

                file = new File(event.getFilename());
                try {
                    attachment.setData(FileUtils.readFileToByteArray(file));
                } catch (IOException ignored) {
                }

                try {
                    fileName = URLEncoder.encode(event.getFilename(), "UTF-8");

                } catch (UnsupportedEncodingException ignore) {
                }
                Map<String, Attachment> data = new HashMap<String, Attachment>();
                data.put(fileName, attachment);

                model.setAttachments(data);
            }
        });
    }

    public class UploadReceiver implements Receiver {
        public OutputStream receiveUpload(String filename, String mimetype) {
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(filename);
            } catch (FileNotFoundException ignore) {
            }
            return fileOutputStream;
        }
    }
}