import com.google.gson.JsonParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ScrubberProviderTest {

    @Test
    public void handleRequest_multipleJsonFiles_allFilesAreScrubbed() throws IOException {
        String element1 = JsonParser.parseReader(new FileReader(new File(".").getCanonicalPath() +  "\\src\\main\\java\\samplejsondata\\exchangerate.json")).toString();
        String element2 = JsonParser.parseReader(new FileReader(new File(".").getCanonicalPath() +  "\\src\\main\\java\\samplejsondata\\foodfacts.json")).toString();
        String element3 = JsonParser.parseReader(new FileReader(new File(".").getCanonicalPath() +  "\\src\\main\\java\\samplejsondata\\json.json")).toString();
        String element4 = JsonParser.parseReader(new FileReader(new File(".").getCanonicalPath() +  "\\src\\main\\java\\samplejsondata\\large-file.json")).toString();
        String element5 = JsonParser.parseReader(new FileReader(new File(".").getCanonicalPath() +  "\\src\\main\\java\\samplejsondata\\reddit.json")).toString();
        String element6 = JsonParser.parseReader(new FileReader(new File(".").getCanonicalPath() +  "\\src\\main\\java\\samplejsondata\\statecodes.json")).toString();
        String element7 = JsonParser.parseReader(new FileReader(new File(".").getCanonicalPath() +  "\\src\\main\\java\\samplejsondata\\streetcrimedates.json")).toString();

        List<String> keywords = List.of("provider", "terms", "update_key", "status", "body", "uuid", "id", "type", "url", "width", "state_name", "date");

        String replacementValue = "*****";
        ScrubRequest request1 = new ScrubRequest(replacementValue, keywords, element1);
        ScrubRequest request2 = new ScrubRequest(replacementValue, keywords, element2);
        ScrubRequest request3 = new ScrubRequest(replacementValue, keywords, element3);
        ScrubRequest request4 = new ScrubRequest(replacementValue, keywords, element4);
        ScrubRequest request5 = new ScrubRequest(replacementValue, keywords, element5);
        ScrubRequest request6 = new ScrubRequest(replacementValue, keywords, element6);
        ScrubRequest request7 = new ScrubRequest(replacementValue, keywords, element7);

        List<ScrubRequest> requests = List.of(request1, request2, request3, request4, request5, request6, request7);

        List<ScrubResult> providerResults = new ArrayList<>();

        ScrubResult result = null;

        // providerRequests
        for (ScrubRequest request : requests) {
            ScrubbingActivityProvider provider = new ScrubbingActivityProvider();
            result = provider.handleRequest(request, null);
            providerResults.add(result);
            Assertions.assertNotEquals(request.getJsonElement().toString(), result.getJsonElement().toString());
        }

        List<Double> providerProcessTimes = new ArrayList<>();

        for (int i = 0; i < requests.size(); i++) {
            ScrubResult provided = providerResults.get(i);

            providerProcessTimes.add(provided.getStatistics().getProcessTimeInMicroSeconds());

            System.out.println("*".repeat(200));
            System.out.println("provided request  --- " + provided.getStatistics());
        }

        double providerTotalProcessTime = providerProcessTimes.stream().reduce(Double::sum).get();
        double providerAverageProcessTime = providerTotalProcessTime / providerProcessTimes.size();

        System.out.println();
        System.out.println("total process time:    --- " + providerTotalProcessTime + " microseconds.");
        System.out.println("average process time:  --- " + providerAverageProcessTime + " microseconds.");
        System.out.println();
    }
}
