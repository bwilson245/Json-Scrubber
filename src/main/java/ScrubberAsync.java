
import com.google.gson.*;

import java.util.Date;
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
    private int totalElements;
    private int totalObjects;
    private int totalArrays;
    private int totalPrimitives;
    private int totalScubbedElements;

    public ScrubberAsync(ScrubRequest request) {
        this.element = request.getJsonElement().deepCopy();
        this.VALUE = JsonParser.parseString(request.getReplacementValue()).getAsJsonPrimitive();
        this.keywords = request.getKeywords();
        this.totalElements = 0;
        this.totalObjects = 0;
        this.totalArrays = 0;
        this.totalPrimitives = 0;
        this.totalScubbedElements = 0;
    }

    /**
     * This method will modify and return the JsonElement contained withing the ScrubberAsync Object based on the
     * parameters provided to the ScrubberAsync object at creation.
     * @return - a JsonElement object with the desired values scrubbed.
     * @throws Exception
     */

    public ScrubResult handleRequest() {
        this.service = Executors.newCachedThreadPool();
        long now = new Date().getTime();
        try {
            element = scrub(element);
        } catch (Exception e) {
            e.printStackTrace();
        }
        service.shutdown();
        while (!service.isTerminated()) {
        }
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

    private JsonElement scrub(JsonElement element) throws Exception {
        totalElements++;
        if (element.isJsonObject()) {
            totalObjects++;
            element.getAsJsonObject().entrySet().forEach(entry -> {
                Callable<JsonElement> task;
                if (keywords.contains(entry.getKey())) {
                    task =  () -> entry.setValue(scrubAll(entry.getValue()));
                    try {
                        task.call();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    task = () -> entry.setValue(scrub(entry.getValue()));
                    try {
                        task.call();
                    } catch (Exception e) {
                        throw new RuntimeException("Error during scrubbing", e.getCause());
                    }
                }
            });
        } else if (element.isJsonArray()) {
            totalArrays++;
            JsonArray array = element.getAsJsonArray();
            for (int i = 0; i < element.getAsJsonArray().size(); i++) {
                JsonElement e = array.get(i);
                int finalI = i;
                Callable<JsonElement> task = () -> array.set(finalI, scrub(e));
                task.call();
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

    private JsonElement scrubAll(JsonElement element) throws Exception {
        totalElements++;
        if (element.isJsonObject()) {
            totalObjects++;
            element.getAsJsonObject().entrySet().forEach(entry -> {
                Callable<JsonElement> task;
                task = () -> entry.setValue(scrubAll(entry.getValue()));
                try {
                    task.call();
                } catch (Exception e) {
                    throw new RuntimeException("Error during scrubbing", e.getCause());
                }
            });
        } else if (element.isJsonArray()) {
            totalArrays++;
            JsonArray array = element.getAsJsonArray();
            for (int i = 0; i < element.getAsJsonArray().size(); i++) {
                JsonElement e = array.get(i);
                Callable<JsonElement> task;
                int finalI = i;
                task = () -> array.set(finalI, scrubAll(e));
                task.call();
            }
        } else if (element.isJsonPrimitive()) {
            totalPrimitives++;
            totalScubbedElements++;
            element = VALUE;
        }
        return element;
    }

}
