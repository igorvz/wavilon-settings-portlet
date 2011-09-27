package com.aimprosoft.wavilon.ui.menuitems.forms;

import com.aimprosoft.wavilon.model.Attachment;
import com.aimprosoft.wavilon.model.Recording;
import com.vaadin.terminal.FileResource;
import com.vaadin.terminal.Sizeable;
import com.vaadin.terminal.StreamResource;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Label;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class RecordingUploader extends VerticalLayout{

    private File file;

    public RecordingUploader() {
    }

    public void init(final Recording recording) {

        final Upload upload = new Upload("Upload file", null);
        upload.setButtonCaption("Start Upload");

        final Embedded image = new Embedded("Uploaded Image");
        image.setVisible(false);
        image.setWidth(200, Sizeable.UNITS_PIXELS);

        if (recording.getAttachments() != null) {

            Map<String, Attachment> attachmentMap = recording.getAttachments();
            for (Map.Entry<String, Attachment> entry : attachmentMap.entrySet()) {

                final Attachment attachment = entry.getValue();
                String contentType = attachment.getContentType();
                if (contentType.startsWith("image")) {
                    StreamResource.StreamSource source = new StreamResource.StreamSource() {
                        public InputStream getStream() {
                            return new ByteArrayInputStream(attachment.getData());
                        }
                    };
                    image.setSource(new StreamResource(source, entry.getKey(), getApplication()));
                    image.setVisible(true);
                }
            }
        }

        class ImageUploader implements Upload.Receiver, Upload.SucceededListener {
            public File file;
            Attachment attachments = new Attachment();

            public OutputStream receiveUpload(String filename, String mimeType) {

                // Create upload stream
                FileOutputStream fos = null; // Output stream to write to

                attachments.setContentType(mimeType);

                if (!mimeType.startsWith("image")) {
                    image.setVisible(false);
                } else image.setVisible(true);

                try {
                    // Open the file for writing.
                    file = new File(filename);
                    fos = new FileOutputStream(file);

                } catch (final java.io.FileNotFoundException e) {
                    addComponent(new Label("Can not write file"));
                    return null;
                }
                return fos; // Return the output stream to write to
            }

            public void uploadSucceeded(Upload.SucceededEvent event) {
                // Show the uploaded file in the image viewer
                image.setSource(new FileResource(file, getApplication()));

                try {
                    attachments.setData(FileUtils.readFileToByteArray(file));

                    Map<String, Attachment> uploadedFile = new HashMap<String, Attachment>();
                    uploadedFile.put(file.getName(), attachments);

                    recording.setAttachments(uploadedFile);
                } catch (IOException e) {
                }
            }
        }

        final ImageUploader uploader = new ImageUploader();

        upload.setReceiver(uploader);
        upload.addListener(uploader);

        addComponent(upload);
        addComponent(image);
    }
};
