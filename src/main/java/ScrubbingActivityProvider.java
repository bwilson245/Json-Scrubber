import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class ScrubbingActivityProvider implements RequestHandler<ScrubRequest, Object> {

    public ScrubbingActivityProvider(){}

    /**
     * This is the entry point for the Lambda function. Accepts a ScrubRequest object.
     * @param input - The ScrubRequest object that contains the paraments for the scrub.
     * @param context - Allows for access to information about the lambda enviroment. Not used.
     * @return - Returns a JsonElement converted to an Object.
     */


    @Override
    public ScrubResult handleRequest(ScrubRequest input, Context context) {
        return new Scrubber(input).handleRequest();
    }
}
