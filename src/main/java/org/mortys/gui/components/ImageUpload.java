package org.mortys.gui.components;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FileResource;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.*;
import org.mortys.model.dao.UserDAO;
import org.mortys.model.objects.dto.Student;
import org.mortys.model.objects.dto.User;
import org.mortys.process.control.UserControl;
import org.mortys.process.control.exception.DatabaseException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

// Component ist reusable falls nötig. Für implementierung ask mm

public class ImageUpload extends CustomComponent {
    String avatar = "";
    User user = null;
    public ImageUpload(User user){
        this.avatar = user.getAvatar();
        this.user = user;
        HorizontalLayout layout = new HorizontalLayout();
        layout(layout, avatar);
        setCompositionRoot(layout);
    }

    public void layout(HorizontalLayout layout, String avatar) {
        Image image = new Image();
        image.setStyleName("profile_picRa");

        class ImageReceiver implements Upload.Receiver, Upload.SucceededListener {

            public File file;
            public File folder;

            public OutputStream receiveUpload(String filename,
                                              String mimeType) {
                // Create upload stream
                FileOutputStream fos = null; // Stream to write to
                try {
                    // Open the file for writing.
                    String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
                    folder = new File(basepath + "/tmp/uploads/");
                    if (!folder.exists() && !folder.mkdirs()) {
                        new Notification("ERROR: Could not create upload dir, please contact administrator").show(Page.getCurrent());
                    }
                    file = new File(basepath + "/tmp/uploads/" + filename);
                    fos = new FileOutputStream(file);
                } catch (final java.io.FileNotFoundException e) {
                    new Notification("Could not open file<br/>",
                            e.getMessage(),
                            Notification.Type.ERROR_MESSAGE)
                            .show(Page.getCurrent());
                    return null;
                }
                return fos; // Return the output stream to write to
            }

            public void uploadSucceeded(Upload.SucceededEvent event) {
                image.setSource(new FileResource(file));
                String asd = file.getAbsoluteFile().toString();
                try {
                    UserDAO.getInstance().setAvatar(user,VaadinService.getCurrent().getBaseDirectory().getAbsolutePath()+"/tmp/uploads/"+event.getFilename());
                    UserControl.loadUserToSession(user.getEmail());
                } catch (DatabaseException e) {
                    e.printStackTrace();
                }
            }
        }
        ImageReceiver receiver = new ImageReceiver();

        Upload upload = new Upload(null, receiver);
        upload.setButtonCaption("Switch image");
        upload.setStyleName("profile_upload");
        upload.addSucceededListener(receiver);

        long UPLOAD_LIMIT = 3000000l;
        upload.addStartedListener(new Upload.StartedListener() {
            @Override
            public void uploadStarted(Upload.StartedEvent event) {
                if (event.getContentLength() > UPLOAD_LIMIT) {
                    Notification.show("Datei zu groß!",
                            Notification.Type.ERROR_MESSAGE);
                    upload.interruptUpload();
                }
            }
        });

        upload.addProgressListener(new Upload.ProgressListener() {
            @Override
            public void updateProgress(long readBytes, long contentLength) {
                if (readBytes > UPLOAD_LIMIT) {
                    Notification.show("Datei zu groß!",
                            Notification.Type.ERROR_MESSAGE);
                    upload.interruptUpload();
                }
            }
        });


        layout.setStyleName("profile_imageUpload");
        Label usrBg = new Label();
        usrBg.setStyleName("profile_usrbg");
        if(avatar == null) {usrBg.setIcon(VaadinIcons.USER);}
        layout.addComponent(usrBg);
        layout.addComponent(image);
        layout.addComponent(upload);

        Label kindOfUser = new Label(user instanceof Student ? "Student" : "Unternehmer");
        kindOfUser.setStyleName("profile_kindOfUser");
        layout.addComponent(kindOfUser);
        if(avatar != null) {image.setSource(new FileResource(new File(user.getAvatar())));}
    }


    }


