package tr.com.postalci;

import com.amazonaws.services.lambda.runtime.LambdaLogger;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sesv2.SesV2Client;
import software.amazon.awssdk.services.sesv2.model.*;

public class EmailSender {
    public static void send(
            String sender,
            String recipient,
            String subject,
            String bodyHTML,
            LambdaLogger logger
    ) {


        Destination destination = Destination.builder()
                .toAddresses(recipient)
                .build();

        Content content = Content.builder()
                .data(bodyHTML)
                .build();

        Content sub = Content.builder()
                .data(subject)
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
                .fromEmailAddress(sender)
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

//    public static void main(String[] args) {
//        send(
//                "bpostalciaws@gmail.com",
//                "bpostalciaws2@gmail.com",
//                "Hello",
//                "merhaba",
//                null
//        );
//    }
}
