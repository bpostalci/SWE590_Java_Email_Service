package tr.com.postalci;

import java.lang.reflect.Array;
import java.util.Map;

public class EmailData {
    private final String sender;
    private final String[] recipients;
    private final String[] ccAddresses;
    private final String[] bccAddresses;
    private final String subject;
    private final String bodyHTML;

    public String[] getCcAddresses() {
        return ccAddresses;
    }

    public String[] getBccAddresses() {
        return bccAddresses;
    }

    public static EmailData create(Map<String, String> bodyMap) {
        String sender = bodyMap.get("sender");
        String[] recipients = bodyMap.get("recipients").split(",");
        String[] ccAddresses = bodyMap.get("cc_addresses") != null ? bodyMap.get("cc_addresses").split(",") : new String[]{};
        String[] bccAddresses = bodyMap.get("bcc_addresses") != null ? bodyMap.get("bcc_addresses").split(",") : new String[]{};
        String subject = bodyMap.get("subject");
        String bodyHTML = bodyMap.get("bodyHTML");

        // TODO: validate body html syntax

        return new EmailData(sender, recipients, ccAddresses, bccAddresses, subject, bodyHTML);
    }

    private EmailData(String sender,
                      String[] recipients,
                      String[] ccAddresses,
                      String[] bccAddresses,
                      String subject,
                      String bodyHTML) {
        this.sender = sender;
        this.recipients = recipients;
        this.ccAddresses = ccAddresses;
        this.bccAddresses = bccAddresses;
        this.subject = subject;
        this.bodyHTML = bodyHTML;
    }

    public String getSender() {
        return sender;
    }

    public String[] getRecipients() {
        return recipients;
    }

    public String getSubject() {
        return subject;
    }

    public String getBodyHTML() {
        return bodyHTML;
    }


}
