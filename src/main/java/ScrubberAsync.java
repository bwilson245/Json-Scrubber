import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.*;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This is the Async version. Uses ExecutorService to provide multiple threads.
 */
public class ScrubberAsync {
    private JsonElement element;
    private final List<String> keywords;
    private final JsonPrimitive VALUE;
    private ExecutorService service;

    public ScrubberAsync(ScrubRequest request) {
        this.element = request.getJsonElement().deepCopy();
        this.VALUE = JsonParser.parseString(request.getReplacementValue()).getAsJsonPrimitive();
        this.keywords = request.getKeywords();
    }

    /**
     * This method will modify and return the JsonElement contained withing the ScrubberAsync Object based on the
     * parameters provided to the ScrubberAsync object at creation.
     * @return - a JsonElement object with the desired values scrubbed.
     * @throws Exception
     */

    public JsonElement handleRequest() {
        this.service = Executors.newCachedThreadPool();
        try {
            scrub(element);
        } catch (Exception e) {
            e.printStackTrace();
        }
        service.shutdown();
        while (!service.isTerminated()) {
        }
        return element;
    }

    private JsonElement scrub(JsonElement element) throws Exception {
        if (element.isJsonObject()) {
            element.getAsJsonObject().entrySet().forEach(entry -> {
                Callable<JsonElement> task;
                if (keywords.contains(entry.getKey())) {
                    task =  () -> entry.setValue(scrubAll(entry.getValue()));
                    try {
                        task.call();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (entry.getValue().isJsonObject() || entry.getValue().isJsonArray()) {
                    task = () -> entry.setValue(scrub(entry.getValue()));
                    try {
                        task.call();
                    } catch (Exception e) {
                        throw new RuntimeException("Error during scrubbing", e.getCause());
                    }
                } else if (entry.getValue().isJsonPrimitive() && keywords.contains(entry.getValue().toString())) {
                    entry.setValue(VALUE);
                }
            });
        } else if (element.isJsonArray()) {
            JsonArray array = element.getAsJsonArray();
            for (int i = 0; i < element.getAsJsonArray().size(); i++) {
                JsonElement e = array.get(i);
                if (e.isJsonObject() || e.isJsonArray()) {
                    int finalI = i;
                    Callable<JsonElement> task = () -> array.set(finalI, scrub(e));
                    task.call();
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

    private JsonElement scrubAll(JsonElement element) throws Exception {
        if (element.isJsonObject()) {
            element.getAsJsonObject().entrySet().forEach(entry -> {
                Callable<JsonElement> task;
                if (entry.getValue().isJsonObject() || entry.getValue().isJsonArray()) {
                    task = () -> entry.setValue(scrubAll(entry.getValue()));

                    try {
                        task.call();
                    } catch (Exception e) {
                        throw new RuntimeException("Error during scrubbing", e.getCause());
                    }
                } else if (entry.getValue().isJsonPrimitive()) {
                    entry.setValue(VALUE);
                }
            });
        } else if (element.isJsonArray()) {
            JsonArray array = element.getAsJsonArray();
            for (int i = 0; i < element.getAsJsonArray().size(); i++) {
                JsonElement e = array.get(i);
                Callable<JsonElement> task;
                if (e.isJsonArray() || e.isJsonObject()) {
                    int finalI = i;
                    task = () -> array.set(finalI, scrubAll(e));
                    task.call();
                } else if (e.isJsonPrimitive()) {
                    array.set(i, VALUE);
                }
            }
        } else if (element.isJsonPrimitive()) {
            element = VALUE;
        }
        return element;
    }

}
