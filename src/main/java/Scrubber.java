import com.google.gson.*;

import java.text.DecimalFormat;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * This is the Async version. Uses ExecutorService to provide multiple threads.
 */
public class Scrubber {
    public JsonElement element;
    public List<String> keywords;
    public JsonPrimitive VALUE;
    public int totalElements;
    public int totalObjects;
    public int totalArrays;
    public int totalPrimitives;
    public int totalScrubbedElements;
    private final ExecutorService service;

    public Scrubber(ScrubRequest request) {
        this.service = Executors.newCachedThreadPool();
        this.element = request.getJsonElement();
        this.keywords = request.getKeywords();
        this.VALUE = JsonParser.parseString(new Gson().toJson(request.getReplacementValue())).getAsJsonPrimitive();
        this.totalElements = 0;
        this.totalObjects = 0;
        this.totalArrays = 0;
        this.totalPrimitives = 0;
        this.totalScrubbedElements = 0;
    }
    /**
     * This method will modify and return the JsonElement contained withing the Scrubber Object based on the
     * parameters provided to the Scrubber object at creation.
     * @return - a JsonElement object with the desired values scrubbed.
     * @throws Exception
     */

    public ScrubResult handleRequest() {
        long now = System.nanoTime();
        try {
            element = scrub(element);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.service.shutdown();
        while (!this.service.isTerminated()) {
        }
        long totalTimeInMicroSeconds = (System.nanoTime() - now);
        totalTimeInMicroSeconds = TimeUnit.MICROSECONDS.convert(totalTimeInMicroSeconds, TimeUnit.NANOSECONDS);
        Statistics statistics = Statistics.builder()
                .withProcessTime(totalTimeInMicroSeconds)
                .withTotalElements(totalElements)
                .withTotalObjects(totalObjects)
                .withTotalArrays(totalArrays)
                .withTotalPrimitives(totalPrimitives)
                .withTotalScrubbedElements(totalScrubbedElements)
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
                totalScrubbedElements++;
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
            totalScrubbedElements++;
            element = VALUE;
        }
        return element;
    }

}
