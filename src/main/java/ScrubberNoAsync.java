import com.google.gson.*;

import java.util.*;

/**
 * This is the NonAsync version. Uses an iterative approach to scrub the data.
 */
public class ScrubberNoAsync {
    private JsonElement element;
    private final List<String> keywords;
    private final JsonPrimitive VALUE;

    public ScrubberNoAsync(ScrubRequest request) {
        this.element = request.getJsonElement();
        this.VALUE = JsonParser.parseString(request.getReplacementValue()).getAsJsonPrimitive();
        this.keywords = request.getKeywords();
    }

    /**
     * This method will modify and return the JsonElement contained withing the ScrubberAsync Object based on the
     * parameters provided to the ScrubberAsync object at creation.
     * @return - a JsonElement object with the desired values scrubbed.
     * @throws Exception
     */

    public JsonElement scrub() {
        return scrub(element);
    }

    private JsonElement scrub(JsonElement element) {
        if (element.isJsonObject()) {
            for (Map.Entry<String, JsonElement> entry : element.getAsJsonObject().entrySet()) {
                if (keywords.contains(entry.getKey())) {
                    entry.setValue(scrubAll(entry.getValue()));
                }
                if (entry.getValue().isJsonObject() || entry.getValue().isJsonArray()) {
                    entry.setValue(scrub(entry.getValue()));
                }
                if (entry.getValue().isJsonPrimitive()) {
                    if (keywords.contains(entry.getValue().toString())) {
                        entry.setValue(VALUE);
                    }
                }
            }
        } else if (element.isJsonArray()) {
            for (int i = 0; i < element.getAsJsonArray().size(); i++) {
                JsonElement e = element.getAsJsonArray().get(i);
                if (e.isJsonObject() || e.isJsonArray()) {
                    element.getAsJsonArray().set(i, scrub(e));
                }
                if (e.isJsonPrimitive()) {
                    if (keywords.contains(e.getAsJsonPrimitive().toString())) {
                        element.getAsJsonArray().set(i, VALUE);
                    }
                }
            }
        } else if (element.isJsonPrimitive()) {
            if (keywords.contains(element.getAsJsonPrimitive().toString())) {
                element = VALUE;
            }
        }

        return element;
    }

    private JsonElement scrubAll(JsonElement element) {
        if (element.isJsonObject()) {
            for (Map.Entry<String, JsonElement> entry : element.getAsJsonObject().entrySet()) {
                entry.setValue(scrubAll(entry.getValue()));
            }
        } else if (element.isJsonArray()) {
            for (int i = 0; i < element.getAsJsonArray().size(); i++) {
                JsonElement e = element.getAsJsonArray().get(i);
                element.getAsJsonArray().set(i, scrubAll(e));
            }
        } else if (element.isJsonPrimitive()) {
            element = VALUE;
        }
        return element;
    }
}
