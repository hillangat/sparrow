package com.techmaster.sparrow.data;

import com.techmaster.sparrow.constants.SparrowURLConstants;
import com.techmaster.sparrow.entities.misc.MediaObj;
import com.techmaster.sparrow.entities.email.EmailAttachment;
import com.techmaster.sparrow.entities.email.EmailTemplate;
import com.techmaster.sparrow.util.SparrowUtil;
import com.techmaster.sparrow.xml.XMLService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

public class EmailTemplates {

    private static Logger logger = LoggerFactory.getLogger(EmailTemplates.class);

    public static List<EmailTemplate> createTemplates(List<MediaObj> mediaObjs) {

        logger.debug("Creating email templates....");

        List<String> templateNames = new ArrayList<>();
        templateNames.add("mlk_dream.xml");

        List<EmailTemplate> templates = new ArrayList<>();

        templateNames.forEach(templateName -> {
            String path = SparrowURLConstants.EMAIL_TEMPLATES_PATH + templateName;
            XMLService xml = SparrowUtil.getXMLServiceForFileLocation(path);

            String desc = getText(context("description"), xml);
            String name = getText(context("name"), xml);
            String from = getText(context("from"), xml);
            String subject = getText(context("subject"), xml);
            String toList = getText(context("toList"), xml);

            String ccList = getText(context("ccList"), xml);
            String contentType = getText(context("contentType"), xml);
            boolean exclusive = Boolean.valueOf(getText(context("exclusive"), xml));
            boolean isAllBcc = Boolean.valueOf(getText(context("isAllBcc"), xml));
            String contentKey = getText(context("contentKey"), xml);

            String template = getText(key("template"), xml);
            String content = getText(key("content"), xml);
            String disclaimer = getText("disclaimer", xml);

            EmailTemplate templateBean = SparrowUtil.addAuditInfo(new EmailTemplate(), "admin");
            templateBean.setContentBody(SparrowUtil.getStringBlob(content));
            templateBean.setCss(null);
            templateBean.setDisclaimer(SparrowUtil.getStringBlob(disclaimer));
            templateBean.setTemplateDescription(desc);
            templateBean.setTemplateName(name);
            templateBean.setFrom(from);
            templateBean.setSubject(subject);
            templateBean.setToList(SparrowUtil.getStringBlob(toList));
            templateBean.setCcList(SparrowUtil.getStringBlob(ccList));
            templateBean.setContentType(contentType);
            templateBean.setExclusive(exclusive);
            templateBean.setAllBcc(isAllBcc);
            templateBean.setContentKey(contentKey);
            templateBean.setTemplate(SparrowUtil.getStringBlob(template));

            List<EmailAttachment> emailTemplates = getAttachments(xml, mediaObjs);
            templateBean.setAttachments(emailTemplates);

            templates.add(templateBean);
        });

        logger.debug("Successfully created email templates.... size = " + templates.size());
        return templates;
    }

    private static String key(String key) {
        return "templates/template/" + key;
    }

    private static String context(String key) {
        return key( "context/" + key );
    }

    private static String getText(String key, XMLService xml) {
        NodeList nodes = xml.getNodeListForPathUsingJavax( key );
        if (nodes != null && nodes.getLength() > 0) {
            Node node = nodes.item(0);
            if (node != null && node.getTextContent() != null) {
                String content = node.getTextContent();
                logger.debug("content return for : " + key + ", \t\t" + content);
                return content;
            }
        }
        return null;
    }

    private static List<EmailAttachment> getAttachments (XMLService xml, List<MediaObj> mediaObjs) {

        logger.debug("Creating attachments....");
        List<EmailAttachment> attachments = new ArrayList<>();
        String path = "templates/attachments/attachment";
        NodeList nodes = xml.getNodeListForPathUsingJavax( path );
        if (SparrowUtil.isNodeListNotEmptpy(nodes)) {
            for( int i = 0; i < nodes.getLength(); i++ ) {

                Node node = nodes.item(i);
                NodeList fields = node.getChildNodes();

                EmailAttachment attachment = SparrowUtil.addAuditInfo(new EmailAttachment(), "admin");
                attachment.setAttachmentId(0);

                for( int j = 0; j < fields.getLength(); j++ ) {
                    Node field = fields.item(j);
                    if (SparrowUtil.isElement(field)) {
                        short type = field.getNodeType();
                        setValues(field, attachment);
                        attachment.setMediaObj(mediaObjs.get(i % 2 == 0 ? 0 : 1));
                    }
                }

                attachments.add(attachment);
            }
        }

        logger.debug("Successfully created attachments....size = " + attachments.size());
        return attachments;
    }

    private static void setValues ( Node node, EmailAttachment attachment ) {
        String content = node.getTextContent();
        switch (node.getNodeName()) {
            case "key" : attachment.setKey( content ); break;
            case "embedded" : attachment.setEmbedded( Boolean.valueOf(content) ); break;
            case "desc" : attachment.setDescription( content ); break;
            case "width" : attachment.setWidth( _int(content) ); break;
            case "height" : attachment.setHeight (_int(content) ); break;
            case "unit" : attachment.setUnit( content ); break;
            case "mediaRef" : attachment.setMediaRef( content ); break;
            default: break;
        }
    }

    private static int _int( String i ) {
        return !SparrowUtil.notNullNotEmpty(i) ? 0 : Integer.parseInt(i);
    }

}
