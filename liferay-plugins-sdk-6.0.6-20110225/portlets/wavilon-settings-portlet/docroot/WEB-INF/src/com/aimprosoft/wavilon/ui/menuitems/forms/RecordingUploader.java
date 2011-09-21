package com.aimprosoft.wavilon.ui.menuitems.forms;

import com.aimprosoft.wavilon.model.Recording;
import com.vaadin.terminal.FileResource;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Label;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class RecordingUploader extends VerticalLayout {
    File file;

    RecordingUploader(Recording recording) {
        basic(recording);
    }

    private void basic(final Recording recording) {

        final Upload upload = new Upload("Upload file", null);
        upload.setButtonCaption("Start Upload");

        final Embedded image = new Embedded("Uploaded Image");
        image.setVisible(false);
        image.setHeight(150, Sizeable.UNITS_PIXELS);
        image.setWidth(200, Sizeable.UNITS_PIXELS);

        class ImageUploader implements Upload.Receiver, Upload.SucceededListener {
            public File file;

            public OutputStream receiveUpload(String filename, String mimeType) {
                // Create upload stream
                FileOutputStream fos = null; // Output stream to write to

                if (!mimeType.startsWith("image")) {
                    image.setVisible(false);
                }
                else image.setVisible(true);

                try {
                    // Open the file for writing.
                    file = new File(filename);
                    fos = new FileOutputStream(file);

                    recording.setImg(file);
                } catch (final java.io.FileNotFoundException e) {
                    addComponent(new Label("Can not write file"));
                    return null;
                }
                return fos; // Return the output stream to write to
            }

            public void uploadSucceeded(Upload.SucceededEvent event) {
                // Show the uploaded file in the image viewer
                image.setSource(new FileResource(file, getApplication()));
            }
        }

        final ImageUploader uploader = new ImageUploader();

        upload.setReceiver(uploader);
        upload.addListener(uploader);

        addComponent(upload);
        addComponent(image);
    }
};
