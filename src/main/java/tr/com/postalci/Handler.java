package tr.com.postalci;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;

public class Handler implements RequestHandler<Object, String> {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public String handleRequest(Object input, Context context) {

        HashMap<String, String> inputMap = getInputMap(input);
        LambdaLogger logger = context.getLogger();

        // raw event logs
        logger.log("EVENT: " + gson.toJson(inputMap));
        logger.log("EVENT TYPE: " + input.getClass());

        // process input
        Map<String, String> response = new HashMap<>();

        try {
            EmailData emailData = EmailData.create(inputMap);
            EmailSender.send(emailData, logger);
        } catch (Exception e) {
            return "cannot send email " + e.getMessage();
        }
        // process input

        return "successfully sent email to " + inputMap.get("recipients");
    }

    private HashMap<String, String> getInputMap(Object input) {
        if (input instanceof String) {
            return gson.fromJson((String) input, HashMap.class);
        }
        return (HashMap<String, String>) input;
    }
}
