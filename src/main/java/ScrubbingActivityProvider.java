import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.core.JsonParseException;
import com.google.gson.Gson;

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
    public Object handleRequest(ScrubRequest input, Context context) {
        try {
            if (input.getJsonElement().toString().length() > 25000000) {
                return new Gson().fromJson(new ScrubberAsync(input).handleRequest(), Object.class);
            } else {
                return new Gson().fromJson(new ScrubberNoAsync(input).handleRequest(), Object.class);
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


//    public static void main(String[] args) throws Exception {
//        JsonElement element1 = JsonParser.parseReader(new FileReader("C:\\exchangerate.json"));
//        JsonElement element2 = JsonParser.parseReader(new FileReader("C:\\foodfacts.json"));
//        JsonElement element3 = JsonParser.parseReader(new FileReader("C:\\json.json"));
//        JsonElement element4 = JsonParser.parseReader(new FileReader("C:\\statecodes.json"));
//        JsonElement element5 = JsonParser.parseReader(new FileReader("C:\\streetcrimedates.json"));
//        JsonElement element6 = JsonParser.parseReader(new FileReader("C:\\reddit.json"));
//        JsonElement element7 = JsonParser.parseReader(new FileReader("C:\\large-file.json"));
//
//        List<String> keywords = List.of("date", "type", "name", "uuid", "state", "status", "update_key", "height", "width", "locked", "provider", "terms", "created_at");
//
//        ScrubbingActivityProvider provider = new ScrubbingActivityProvider();
//        ScrubberNoAsync scrubberNoAsync;
//        ScrubberAsync scrubberAsync;
//
//        ScrubRequest request1 = new ScrubRequest("*****", keywords, element1);
//        ScrubRequest request2 = new ScrubRequest("*****", keywords, element2);
//        ScrubRequest request3 = new ScrubRequest("*****", keywords, element3);
//        ScrubRequest request4 = new ScrubRequest("*****", keywords, element4);
//        ScrubRequest request5 = new ScrubRequest("*****", keywords, element5);
//        ScrubRequest request6 = new ScrubRequest("*****", keywords, element6);
//        ScrubRequest request7 = new ScrubRequest("*****", keywords, element7);
//
//        List<ScrubRequest> requests = List.of(request1, request2, request3, request4, request5, request6, request7);
//        List<Long> async = new ArrayList<>();
//        List<Long> nonAsync = new ArrayList<>();
//
//        // non-async
//        long time;
//        for (ScrubRequest request : requests) {
//            scrubberNoAsync = new ScrubberNoAsync(request);
//            time = new Date().getTime();
//            scrubberNoAsync.scrub();
//            long diff = new Date().getTime() - time;
//            nonAsync.add(diff);
//        }
//
//        // async
//        for (ScrubRequest request : requests) {
//            scrubberAsync = new ScrubberAsync(request);
//            time = new Date().getTime();
//            scrubberAsync.scrub();
//            long diff = new Date().getTime() - time;
//            async.add(diff);
//        }
//
//
//        time = new Date().getTime();
//        for (ScrubRequest request : requests) {
//            provider.handleRequest(request, null);
//        }
//        System.out.println("provider total execution time: " + (new Date().getTime() - time));
//
//        double nonAsyncTime = nonAsync.stream().reduce(0L, Long::sum);
//        double nonAsyncAvgTime = nonAsyncTime / nonAsync.size();
//        double asyncTime = async.stream().reduce(0L, Long::sum);
//        double asyncAvgTime = asyncTime / async.size();
//
//        System.out.println("non-async average execution time: " + (double) Math.round(nonAsyncAvgTime * 100)/100 + " ----- total execution time: " + nonAsyncTime);
//        System.out.println("async average execution time: " + (double) Math.round(asyncAvgTime * 100)/100 + " ----- total execution time: " + asyncTime);
//    }
}
