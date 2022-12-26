package tr.com.postalci;

import com.amazonaws.services.lambda.runtime.LambdaLogger;
import software.amazon.awssdk.services.sesv2.SesV2Client;
import software.amazon.awssdk.services.sesv2.model.*;

public class EmailSender {
    public static void send(EmailData emailData, LambdaLogger logger) {

        Destination destination = Destination.builder()
                .toAddresses(emailData.getRecipients())
                .bccAddresses(emailData.getBccAddresses())
                .ccAddresses(emailData.getCcAddresses())
                .build();

        Content content = Content.builder()
                .data(emailData.getBodyHTML())
                .build();

        Content sub = Content.builder()
                .data(emailData.getSubject())
                .build();

        Body body = Body.builder()
                .html(content)
                .build();

        Message msg = Message.builder()
                .subject(sub)
                .body(body)
                .build();

        EmailContent emailContent = EmailContent.builder()
                .simple(msg)
                .build();

        SendEmailRequest emailRequest = SendEmailRequest.builder()
                .destination(destination)
                .content(emailContent)
                .fromEmailAddress(emailData.getSender())
                .build();

        try (SesV2Client client = SesV2Client.create()) {
            logger.log("Attempting to send an email through Amazon SES using the AWS SDK for Java...");
            client.sendEmail(emailRequest);
            logger.log("email was sent");

        } catch (SesV2Exception e) {
            logger.log(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
}
