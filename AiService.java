import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
public class AiService {
    private static final String apiKey = System.getenv("GEMINI_API_KEY");
    private static final String ENDPOINT="https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=" +apiKey;
    public static String generateCode(String prompt){
        try{
            String systemInstruction="Generate only the requested code block.Do not include markdown code intro and outro ticks";
            String jsonPayload="{\"contents\": [{\"parts\": [{\"text\": \"" + systemInstruction + " Prompt: " + prompt + "\"}]}]}";
            HttpClient client=HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(ENDPOINT))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return parseAiresponse(response.body());

        }
        catch(Exception e){
            return "Error communicating with ai service"+e.getMessage();
        }
    }
    private static String parseAiresponse(String json){
        int textIndex=json.indexOf("\"text\": \"");
        if (textIndex == -1) return json;
        int startIndex = textIndex + 9;
        int endIndex = json.indexOf("\"", startIndex);
        return json.substring(startIndex,endIndex).replace("\\n", "\n").replace("\\\"", "\"");
    }
}