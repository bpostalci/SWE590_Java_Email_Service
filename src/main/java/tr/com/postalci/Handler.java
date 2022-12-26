package tr.com.postalci;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;

public class Handler implements RequestHandler<Object, String> {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public String handleRequest(Object input, Context context) {

        HashMap<String, Object> inputMap = getInputMap(input);
        LambdaLogger logger = context.getLogger();

        // raw event logs
        logger.log("EVENT: " + gson.toJson(inputMap));
        logger.log("EVENT TYPE: " + input.getClass());

        // process input
        String body = (String) inputMap.get("body");
        logger.log("BODY: " + body);
        HashMap<String, String> bodyMap = gson.fromJson(body, HashMap.class);

        EmailData emailData = EmailData.create(bodyMap);
        EmailSender.send(emailData, logger);
        // process input

        return "successfully send email to " + bodyMap.get("recipients");
    }

    private HashMap<String, Object> getInputMap(Object input) {
        if (input instanceof String) {
            return gson.fromJson((String) input, HashMap.class);
        }
        return (HashMap<String, Object>) input;
    }


}
