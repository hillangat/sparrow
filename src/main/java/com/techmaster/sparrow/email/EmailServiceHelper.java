package com.techmaster.sparrow.email;

import com.techmaster.sparrow.entities.email.EmailAttachment;
import com.techmaster.sparrow.entities.email.EmailContent;
import com.techmaster.sparrow.entities.email.EmailReceiver;
import com.techmaster.sparrow.entities.email.EmailTemplate;
import com.techmaster.sparrow.entities.misc.MediaObj;
import com.techmaster.sparrow.enums.EmailReceiverType;
import com.techmaster.sparrow.rules.abstracts.RuleResultBean;
import com.techmaster.sparrow.util.SparrowUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import javax.activation.DataHandler;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class EmailServiceHelper {

    private static Logger logger = LoggerFactory.getLogger(EmailServiceHelper.class);

    public static RuleResultBean setReceivers(SimpleMailMessage message,
                                              EmailContent emailContent, RuleResultBean resultBean) {

        List<EmailReceiver> receivers = emailContent.getReceivers();

        String[] fromReceivers = getReceivers(receivers, EmailReceiverType.FROM);
        String[] toReceivers = getReceivers(receivers, EmailReceiverType.TO);
        String[] bccReceivers = getReceivers(receivers, EmailReceiverType.BCC);
        String[] ccReceivers = getReceivers(receivers, EmailReceiverType.CC);

        if (fromReceivers.length == 0) {
            resultBean.setError("receivers", "From email must be set");
        }

        if (toReceivers.length == 0) {
            resultBean.setError("receivers", "There must be at least one email receiver");
        }

        if (resultBean.isSuccess()) {
            message.setFrom(fromReceivers[0]);
            message.setTo(toReceivers);
            message.setBcc(bccReceivers);
            message.setCc(ccReceivers);
        }

        return resultBean;
    }

    public static String[] getReceivers(List<EmailReceiver> receivers, EmailReceiverType receiverType) {
        switch (receiverType) {
            case TO: return filterReceivers(receivers, EmailReceiverType.TO);
            case CC: return filterReceivers(receivers, EmailReceiverType.CC);
            case BCC: return filterReceivers(receivers, EmailReceiverType.BCC);
            case FROM: return filterReceivers(receivers, EmailReceiverType.FROM);
            default: return new String[0];
        }
    }

    public static String[] filterReceivers(List<EmailReceiver> receivers, EmailReceiverType receiverType) {
        return receivers.stream()
                .filter(a -> a.getReceiverType().equals(receiverType))
                .map(r -> r.getEmail())
                .toArray(r -> new String[r]);
    }


    public static void setSubject(SimpleMailMessage message, EmailContent emailContent) {
        message.setSubject(emailContent.getTemplate().getSubject());
    }

    public static String prepareSimpleContent(EmailContent emailContent, RuleResultBean resultBean) {
        EmailTemplate template = emailContent.getTemplate();
        return SparrowUtil.getBlobStr(template.getContentBody());
    }

    public static Message prepareMimeMsg(JavaMailSender javaMailSender,
                                         EmailContent emailContent, RuleResultBean resultBean) {

        logger.debug("Preparing mime message...");

        Message message = javaMailSender.createMimeMessage();

        setReceivers(message, emailContent, EmailReceiverType.FROM, resultBean);
        setReceivers(message, emailContent, EmailReceiverType.TO, resultBean);
        setReceivers(message, emailContent, EmailReceiverType.BCC, resultBean);
        setReceivers(message, emailContent, EmailReceiverType.CC, resultBean);

        String subject = emailContent.getSubject() == null
                ? emailContent.getTemplate().getSubject() : emailContent.getSubject();

        try {
            message.setSubject(subject);
            MimeMultipart multipart = prepareMultiPart(emailContent, resultBean);
            message.setContent(multipart);
        } catch (MessagingException e) {
            SparrowUtil.logException(logger, e, "Application error occurred while setting subject: " + subject);
            resultBean.setApplicationError(e);
        }

        return  message;
    }

    public static void setReceivers(Message message, EmailContent emailContent,
                                    EmailReceiverType receiverType, RuleResultBean resultBean ) {

        logger.debug("Setting email receivers to mime message. size : " + emailContent.getReceivers().size());

        String[] receivers = getReceivers(emailContent.getReceivers(), receiverType);

        Arrays.stream(receivers).forEach(r -> {
            try {
                if (receiverType.equals(EmailReceiverType.FROM)) {
                    message.setFrom(new InternetAddress(emailContent.getTemplate().getFrom()));
                } else {
                    Message.RecipientType recipientType = getJavaMailType(receiverType);
                    if (recipientType != null){
                        message.setRecipient(recipientType, new InternetAddress(r));
                    } else {
                        logger.warn("Cannot find corresponding type for : " + receiverType);
                    }
                }
            } catch (MessagingException e) {
                SparrowUtil.logException(logger, e, "Application error occurred while setting receiver: " + r);
                resultBean.setApplicationError(e);
            }
        });

        logger.debug("Successfully set email receivers to mime message!!");
    }

    public static Message.RecipientType getJavaMailType( EmailReceiverType receiverType ) {
        switch (receiverType) {
            case TO: return Message.RecipientType.TO;
            case CC: return Message.RecipientType.CC;
            case BCC: return Message.RecipientType.BCC;
            default: return null;
        }
    }

    public static MimeMultipart prepareMultiPart(EmailContent emailContent, RuleResultBean resultBean) {

        logger.debug("Preparing email body part...");

        EmailTemplate template = emailContent.getTemplate();
        MimeMultipart mimeMultipart = new MimeMultipart("related"); // TODO: find out why this is required

        try {

            logger.debug("Adding main email body content...");
            BodyPart messageBodyPart = new MimeBodyPart();
            String htmlText = SparrowUtil.getBlobStr(template.getContentBody());
            messageBodyPart.setContent(htmlText, "text/html");
            mimeMultipart.addBodyPart(messageBodyPart);

            for (EmailAttachment attachment : template.getAttachments()) {
                if (attachment.isEmbedded()) {
                    logger.debug("Preparing embedded attachment: " + attachment.getKey());
                    messageBodyPart.setHeader("Content-ID", attachment.getKey());
                } else {
                    logger.debug("Preparing non-embedded attachment: " + attachment.getKey());
                }
                DataHandler dataHandler = getDataHandlerForMediaObj(attachment.getMediaObj(), resultBean);
                if (dataHandler != null) {
                    messageBodyPart.setDataHandler(dataHandler);
                    mimeMultipart.addBodyPart(messageBodyPart);
                } else {
                    logger.error("Could not create datahandler for attachment : " + attachment.getDescription());
                }
            }

        } catch (MessagingException e) {
            SparrowUtil.logException(logger, e, "Application error occurred while preparing email body contents");
            resultBean.setApplicationError(e);
        }

        return mimeMultipart;

    }

    public static DataHandler getDataHandlerForMediaObj( MediaObj mediaObj, RuleResultBean resultBean ) {

        Blob blob = mediaObj.getContent();
        byte[] bytearray = null;

        if (blob != null) {

            ByteArrayOutputStream bao = null;
            BufferedInputStream bis = null;

            try {
                bis = new BufferedInputStream(blob.getBinaryStream());
                bao = new ByteArrayOutputStream();
                byte[] buffer = new byte[4096];
                int length;
                while ((length = bis.read(buffer)) != -1) {
                    bao.write(buffer, 0, length);
                }
                bytearray = bao.toByteArray();
            } catch ( SQLException | IOException e ) {
                SparrowUtil.logException(logger, e, "Application error occurred while trying to create attachment from Blob!");
                resultBean.setApplicationError(e);
            } finally {
                try {
                    bao.close();
                    bis.close();
                } catch (IOException e) {
                    SparrowUtil.logException(logger, e, "Application error occurred while trying to close input and output streams!");
                    resultBean.setApplicationError(e);
                }
            }

        }

        BufferedDataSource bds = new BufferedDataSource(bytearray, mediaObj.getOriginalName());
        DataHandler dataHandler = new DataHandler(bds);

        return dataHandler;

    }


}
