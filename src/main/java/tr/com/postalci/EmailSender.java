package tr.com.postalci;

import com.amazonaws.services.lambda.runtime.LambdaLogger;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sesv2.SesV2Client;
import software.amazon.awssdk.services.sesv2.model.*;

public class EmailSender {
    public static void send(EmailData emailData, LambdaLogger logger) {

        Destination.Builder destinationBuilder = Destination.builder()
                .toAddresses(emailData.getRecipients());

        if (emailData.getBccAddresses() != null && emailData.getBccAddresses().length > 0) {
            destinationBuilder.bccAddresses(emailData.getBccAddresses());
        }

        if (emailData.getCcAddresses() != null && emailData.getCcAddresses().length > 0) {
            destinationBuilder.ccAddresses(emailData.getCcAddresses());
        }

        Destination destination = destinationBuilder.build();

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

        try (SesV2Client client = SesV2Client.builder()
                .region(Region.US_EAST_1)
                .build()) {
            logger.log("Attempting to send an email through Amazon SES using the AWS SDK for Java...");
            client.sendEmail(emailRequest);
            logger.log("email was sent");

        } catch (SesV2Exception e) {
            logger.log(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
}
