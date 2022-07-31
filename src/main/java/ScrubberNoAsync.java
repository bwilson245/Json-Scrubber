import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.*;

import java.util.*;

/**
 * This is the NonAsync version. Uses an iterative approach to scrub the data.
 */
public class ScrubberNoAsync {
    private JsonElement element;
    private final List<String> keywords;
    private final JsonPrimitive VALUE;
    private int totalElements;
    private int totalObjects;
    private int totalArrays;
    private int totalPrimitives;
    private int totalScubbedElements;


    public ScrubberNoAsync(ScrubRequest request) {
        this.element = request.getJsonElement();
        this.VALUE = JsonParser.parseString(request.getReplacementValue()).getAsJsonPrimitive();
        this.keywords = request.getKeywords();
        this.totalElements = 0;
        this.totalObjects = 0;
        this.totalArrays = 0;
        this.totalPrimitives = 0;
        this.totalScubbedElements = 0;
    }

    /**
     * This method will modify and return the JsonElement contained withing the ScrubberNoAsync object based on the
     * parameters provided to the ScrubberNoAsync object at creation.
     * @return - a JsonElement object with the desired values scrubbed.
     * @throws Exception
     */


    public ScrubResult handleRequest() {
        long now = new Date().getTime();
        element = scrub(element);
        Statistics statistics = Statistics.builder()
                .withProcessTime((double) Math.round((new Date().getTime() - now) * 100) / 100)
                .withTotalElements(totalElements)
                .withTotalObjects(totalObjects)
                .withTotalArrays(totalArrays)
                .withTotalPrimitives(totalPrimitives)
                .withTotalScrubbedElements(totalScubbedElements)
                .build();
        return new ScrubResult(element, statistics);
    }

    private JsonElement scrub(JsonElement element) {
        totalElements++;
        if (element.isJsonObject()) {
            totalObjects++;
            for (Map.Entry<String, JsonElement> entry : element.getAsJsonObject().entrySet()) {
                if (keywords.contains(entry.getKey())) {
                    entry.setValue(scrubAll(entry.getValue()));
                } else {
                    entry.setValue(scrub(entry.getValue()));
                }
            }
        } else if (element.isJsonArray()) {
            totalArrays++;
            for (int i = 0; i < element.getAsJsonArray().size(); i++) {
                element.getAsJsonArray().set(i, scrub(element.getAsJsonArray().get(i)));
            }
        } else if (element.isJsonPrimitive()) {
            totalPrimitives++;
            if (keywords.contains(element.getAsJsonPrimitive().toString())) {
                totalScubbedElements++;
                element = VALUE;
            }
        }
        return element;
    }

    private JsonElement scrubAll(JsonElement element) {
        totalElements++;
        if (element.isJsonObject()) {
            totalObjects++;
            for (Map.Entry<String, JsonElement> entry : element.getAsJsonObject().entrySet()) {
                entry.setValue(scrubAll(entry.getValue()));
            }
        } else if (element.isJsonArray()) {
            totalArrays++;
            for (int i = 0; i < element.getAsJsonArray().size(); i++) {
                element.getAsJsonArray().set(i, scrubAll(element.getAsJsonArray().get(i)));
            }
        } else if (element.isJsonPrimitive()) {
            totalPrimitives++;
            totalScubbedElements++;
            element = VALUE;
        }
        return element;
    }

}
