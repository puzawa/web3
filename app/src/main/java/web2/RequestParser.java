package web2;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;

public class RequestParser {

    private static HashMap<String, String> parseRequestBody(String requestBody) throws IllegalArgumentException {
        if (requestBody == null || requestBody.isEmpty()) {
            throw new IllegalArgumentException("Request body is empty");
        }

        try {
            Gson gson = new Gson();

            HashMap<String, String> parsedValues = gson.fromJson(requestBody, HashMap.class);
            if (!parsedValues.containsKey("x") ||
                    !parsedValues.containsKey("y") ||
                    !parsedValues.containsKey("r")) {
                throw new IllegalArgumentException("Missing required parameters");
            }

            return parsedValues;
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid JSON: " + e.getMessage());
        }
    }

    public static String getRequestBody(HttpServletRequest request) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }
    public static HashMap<String, String> parseRequest(HttpServletRequest request) throws IOException, IllegalArgumentException {


        return parseRequestBody(getRequestBody(request));
    }
}
