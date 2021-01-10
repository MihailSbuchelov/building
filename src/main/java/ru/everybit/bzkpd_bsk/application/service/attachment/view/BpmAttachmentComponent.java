package ru.everybit.bzkpd_bsk.application.service.attachment.view;

import com.vaadin.server.FileDownloader;
import com.vaadin.server.FileResource;
import com.vaadin.ui.*;
import com.vaadin.ui.Upload.FinishedEvent;
import ru.everybit.bzkpd_bsk.application.service.attachment.BpmAttachmentService;
import ru.everybit.bzkpd_bsk.application.service.attachment.data.BpmAttachmentFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * UI Компонент для сохранения / загрузки файлов
 */
public class BpmAttachmentComponent {

    public static class UploadReciever implements Upload.Receiver {
        private static final long serialVersionUID = -2193307789130611437L;

        protected BpmAttachmentComponent attachmentComponent;

        public UploadReciever(BpmAttachmentComponent attachmentComponent) {
            this.attachmentComponent = attachmentComponent;
        }

        @Override
        public OutputStream receiveUpload(String fileName, String mimeType) {
            try {
                File file = new File(BpmAttachmentService.getInstance().getAttachmentFolder());
                if (!file.exists()) {
                    if (!file.mkdirs()) {
                        throw new RuntimeException();
                    }
                }
                BpmAttachmentFile attachmentFile = BpmAttachmentService.addAttachment(attachmentComponent.getAttachmentId(), fileName);
                attachmentComponent.setAttachmentId(attachmentFile.getAttachment().getId());
                return new FileOutputStream(new File(file, String.valueOf(attachmentFile.getId())));
            } catch (FileNotFoundException e) {
                return null;
            }
        }
    }

    public static class UploadFinishedListener implements Upload.FinishedListener {
        private static final long serialVersionUID = -7926283908279514799L;

        protected BpmAttachmentComponent bpmAttachmentComponent;

        public UploadFinishedListener(BpmAttachmentComponent bpmAttachmentComponent) {
            this.bpmAttachmentComponent = bpmAttachmentComponent;
        }

        @Override
        public void uploadFinished(FinishedEvent event) {
            bpmAttachmentComponent.updateDownloadButton();
        }

    }

    // bo
    private final String fieldLabel;

    // Обработчик загрузки файла
    private Upload.Receiver uploadReceiver;
    // Обработчик завершения загрузки файла
    private Upload.FinishedListener uploadFinishListener;

    // Идентификатор файла
    private Long attachmentId;

    // ui
    private HorizontalLayout downloadLayout;

    private Upload upload;


    public BpmAttachmentComponent(String fieldLabel, Long attachmentId) {
        // bo
        this.fieldLabel = fieldLabel;
        this.attachmentId = attachmentId;

    }


    public HorizontalLayout buildUi() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setSpacing(true);

        Label label = new Label(fieldLabel);
        layout.addComponent(label);
        layout.setComponentAlignment(label, Alignment.MIDDLE_LEFT);

        setUpload(new Upload());
        getUpload().setButtonCaption("Прикрепить");
        getUpload().setImmediate(true);
        getUpload().setReceiver(getUploadReceiver());
        getUpload().addFinishedListener(getUploadFinishListener());
        layout.addComponent(getUpload());

        downloadLayout = new HorizontalLayout();
        layout.addComponent(downloadLayout);
        updateDownloadButton();
        return layout;
    }

    private void updateDownloadButton() {
        downloadLayout.removeAllComponents();
//        Long attachmentId = (Long) BpmProcessService.getProcessVariable(processInstanceId, attachmentVariableName);
        if (attachmentId != null) {
            Button downloadButton = new Button("Скачать");
            FileResource resource = BpmAttachmentService.getAttachmentFileResource(attachmentId);
            FileDownloader fd = new FileDownloader(resource);

            fd.extend(downloadButton);
            downloadLayout.addComponent(downloadButton);
        }
    }


    public void setAttachmentId(Long attachmentId) {
        this.attachmentId = attachmentId;
    }

    public Long getAttachmentId() {
        return attachmentId;
    }


    public Upload.Receiver getUploadReceiver() {
        if (uploadReceiver == null) {
            uploadReceiver = new BpmAttachmentComponent.UploadReciever(this);
        }
        return uploadReceiver;
    }


    public void setUploadReceiver(Upload.Receiver uploadReceiver) {
        this.uploadReceiver = uploadReceiver;
    }


    public Upload.FinishedListener getUploadFinishListener() {
        if (uploadFinishListener == null) {
            uploadFinishListener = new BpmAttachmentComponent.UploadFinishedListener(this);
        }
        return uploadFinishListener;
    }


    public void setUploadFinishListener(Upload.FinishedListener uploadFinishListener) {
        this.uploadFinishListener = uploadFinishListener;
    }


    public Upload getUpload() {
        return upload;
    }


    public void setUpload(Upload upload) {
        this.upload = upload;
    }
}
