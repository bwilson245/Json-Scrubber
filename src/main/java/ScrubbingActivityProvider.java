import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class ScrubbingActivityProvider implements RequestHandler<ScrubRequest, Object> {

    public ScrubbingActivityProvider(){}

    /**
     * This is the entry point for the Lambda function. Accepts a ScrubRequest object. If the size of the json object
     * inside the ScrubRequest object has a size greater that 25 million characters, it will use the Async version of
     * the Scrubber to make it faster. In actuality, the speed of the process is determined by the number of elements
     * in the json object. More Keys with Short values will run slower than Less keys with longer values, especially if
     * the json file character length is less than 25 million.
     * @param input - The ScrubRequest object that contains the paraments for the scrub.
     * @param context - Allows for access to information about the lambda enviroment. Not used.
     * @return - Returns a JsonElement converted to an Object.
     */

    @Override
    public ScrubResult handleRequest(ScrubRequest input, Context context) {
        try {
            if (input.getJsonElement().toString().length() > 25000000) {
                return new ScrubberAsync(input).handleRequest();
            } else {
                return new ScrubberNoAsync(input).handleRequest();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * This is Test data to see the difference between the Async call and NonAsync call. Replace the file paths with
     * your own file paths after cloning the project. Prints out total times and average times for each process.
     * You will notice the method calls ran through the handleRequest method are drastically slower. This is due to the
     * extra method call.
     * Uncomment the method and define your own file paths if you want to run this locally on some sample data.
     * @param args
     * @throws Exception
     */


    public static void main(String[] args) throws Exception {
        // Replace these file paths with your own file path.
        JsonElement element1 = JsonParser.parseReader(new FileReader("C:\\exchangerate.json"));
        JsonElement element2 = JsonParser.parseReader(new FileReader("C:\\foodfacts.json"));
        JsonElement element3 = JsonParser.parseReader(new FileReader("C:\\json.json"));
        JsonElement element4 = JsonParser.parseReader(new FileReader("C:\\large-file.json"));
        JsonElement element5 = JsonParser.parseReader(new FileReader("C:\\reddit.json"));
        JsonElement element6 = JsonParser.parseReader(new FileReader("C:\\statecodes.json"));
        JsonElement element7 = JsonParser.parseReader(new FileReader("C:\\streetcrimedates.json"));

        List<String> keywords = List.of("date", "type", "name", "uuid", "state", "status", "update_key", "height", "width", "locked", "provider", "terms", "created_at");

        ScrubbingActivityProvider provider = new ScrubbingActivityProvider();
        ScrubberNoAsync scrubberNoAsync;
        ScrubberAsync scrubberAsync;

        ScrubRequest request1 = new ScrubRequest("*****", keywords, element1);
        ScrubRequest request2 = new ScrubRequest("*****", keywords, element2);
        ScrubRequest request3 = new ScrubRequest("*****", keywords, element3);
        ScrubRequest request4 = new ScrubRequest("*****", keywords, element4);
        ScrubRequest request5 = new ScrubRequest("*****", keywords, element5);
        ScrubRequest request6 = new ScrubRequest("*****", keywords, element6);
        ScrubRequest request7 = new ScrubRequest("*****", keywords, element7);

        List<ScrubRequest> requests = List.of(request1, request2, request3, request4, request5, request6, request7);

        List<ScrubResult> asyncResults = new ArrayList<>();
        List<ScrubResult> nonAsyncResults = new ArrayList<>();

        ScrubResult result = null;


        // non-asyncRequests
        for (ScrubRequest request : requests) {
            scrubberNoAsync = new ScrubberNoAsync(request);
            result = scrubberNoAsync.handleRequest();
            nonAsyncResults.add(result);
        }

        // asyncRequests
        for (ScrubRequest request : requests) {
            scrubberAsync = new ScrubberAsync(request);
            result = scrubberAsync.handleRequest();
            asyncResults.add(result);
        }

        List<Double> asyncProcessTimes = new ArrayList<>();
        List<Double> nonAsyncProcessTimes = new ArrayList<>();
        for (int i = 0; i < requests.size(); i++) {
            ScrubResult nonAsync = nonAsyncResults.get(i);
            ScrubResult async = asyncResults.get(i);

            asyncProcessTimes.add(async.getStatistics().getProcessTime());
            nonAsyncProcessTimes.add(nonAsync.getStatistics().getProcessTime());

            System.out.println();
            System.out.println("*".repeat(100));
            System.out.println("nonAsync: --- " + nonAsync.getStatistics());
            System.out.println("async:    --- " + async.getStatistics());

            if (nonAsync.getStatistics().getTotalArrays() == async.getStatistics().getTotalArrays() &&
                nonAsync.getStatistics().getTotalObjects() == async.getStatistics().getTotalObjects() &&
                nonAsync.getStatistics().getTotalPrimitives() == async.getStatistics().getTotalPrimitives() &&
                nonAsync.getStatistics().getTotalElements() == async.getStatistics().getTotalElements() &&
                nonAsync.getStatistics().getTotalScrubbedElements() == async.getStatistics().getTotalScrubbedElements()) {
                System.out.println("EQUAL");
            } else {
                System.out.println("NOT EQUAL");
            }

            if (nonAsync.getStatistics().getProcessTime() < async.getStatistics().getProcessTime()) {
                System.out.println("nonAsync is faster by " + (async.getStatistics().getProcessTime() - nonAsync.getStatistics().getProcessTime()) + " milliseconds.");
            } else {
                System.out.println("async is faster by " + (nonAsync.getStatistics().getProcessTime() - async.getStatistics().getProcessTime()) + " milliseconds.");
            }
            System.out.println("*".repeat(100));
        }

        double asyncTotalProcessTime = asyncProcessTimes.stream().reduce(Double::sum).get();
        double nonAsyncTotalProcessTime = nonAsyncProcessTimes.stream().reduce(Double::sum).get();
        double asyncAverageProcessTime = (double) Math.round(asyncTotalProcessTime / asyncProcessTimes.size() * 100) / 100;
        double nonAsyncAverageProcessTime = (double) Math.round(nonAsyncTotalProcessTime / nonAsyncProcessTimes.size() * 100) / 100;
        System.out.println();
        System.out.println("Async total process time:       --- " + asyncTotalProcessTime);
        System.out.println("Async average process time:     --- " + asyncAverageProcessTime);
        System.out.println();
        System.out.println("Non async total process time:   --- " + nonAsyncTotalProcessTime);
        System.out.println("Non async average process time: --- " + nonAsyncAverageProcessTime);
    }
}
