package ru.everybit.bzkpd_bsk.application.service.attachment;

import com.vaadin.server.FileResource;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import ru.everybit.bzkpd_bsk.application.service.attachment.data.BpmAttachment;
import ru.everybit.bzkpd_bsk.application.service.attachment.data.BpmAttachmentDao;
import ru.everybit.bzkpd_bsk.application.service.attachment.data.BpmAttachmentFile;
import ru.everybit.bzkpd_bsk.application.service.attachment.data.BpmAttachmentFileDao;

import java.io.File;
import java.util.Date;

@Component
public class BpmAttachmentService implements ApplicationContextAware {

    @Value("${BPM_ATTACHMENTS_FOLDER}")
    private String attachmentFolder;

    private static BpmAttachmentService singleton;

    private static BpmAttachmentDao attachmentDao;
    private static BpmAttachmentFileDao attachmentFileDao;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        attachmentDao = applicationContext.getBean(BpmAttachmentDao.class);
        attachmentFileDao = applicationContext.getBean(BpmAttachmentFileDao.class);
        singleton = applicationContext.getBean(BpmAttachmentService.class);
    }


    public static BpmAttachmentFile addAttachment(Long attachmentId, String fileName) {
        BpmAttachmentFile attachmentFile;

        if (attachmentId == null) {
            attachmentFile = createAttachment(fileName);
            attachmentId = attachmentFile.getAttachment().getId();
        } else {
            attachmentFile = updateAttachment(attachmentId, fileName);
        }
        return attachmentFile;
    }

    private static BpmAttachmentFile createAttachment(String fileName) {
        BpmAttachment attachment = new BpmAttachment();
        attachmentDao.save(attachment);
        return createAttachmentFile(attachment, fileName);
    }

    private static BpmAttachmentFile updateAttachment(Long attachmentId, String fileName) {
        BpmAttachment attachment = attachmentDao.findOne(attachmentId);
        return createAttachmentFile(attachment, fileName);
    }

    private static BpmAttachmentFile createAttachmentFile(BpmAttachment attachment, String fileName) {
        BpmAttachmentFile attachmentFile = new BpmAttachmentFile();
        attachmentFile.setAttachment(attachment);
        attachmentFile.setFileName(fileName);
        attachmentFile.setCreateDate(new Date());
        attachmentFileDao.save(attachmentFile);

        return attachmentFile;
    }

    /**
     * Получить последнюю загруженную версию
     *
     * @param attachmentId
     * @return BpmAttachmentFile
     */
    public static BpmAttachmentFile getAttachmentFileLastVersion(Long attachmentId) {
        BpmAttachment attachment = attachmentDao.findOne(attachmentId);
        return attachmentFileDao.findByAttachmentOrderByCreateDateDesc(attachment).get(0);
    }

    public static BpmAttachmentDao getBpmAttachmentDao() {
        return attachmentDao;
    }

    public static BpmAttachmentService getInstance() {
        return singleton;
    }

    public String getAttachmentFolder() {
        return attachmentFolder;
    }

    public static FileResource getAttachmentFileResource(Long attachmentId) {
        final BpmAttachmentFile attachmentFile = BpmAttachmentService.getAttachmentFileLastVersion(attachmentId);
        File file = new File(BpmAttachmentService.getInstance().getAttachmentFolder(), String.valueOf(attachmentFile.getId()));
        FileResource resource = new FileResource(file) {
            private static final long serialVersionUID = 8141041570906047359L;

            @Override
            public String getFilename() {
                return attachmentFile.getFileName();
            }
        };
        return resource;
    }
}
