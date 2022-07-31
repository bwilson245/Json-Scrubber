import com.google.gson.Gson;
import com.google.gson.JsonElement;

public class ScrubResult {
    private Object jsonElement;
    private Statistics statistics;

    public ScrubResult(JsonElement jsonElement, Statistics statistics) {
        this.jsonElement = new Gson().fromJson(jsonElement, Object.class);
        this.statistics = statistics;
    }

    public Object getJsonElement() {
        return jsonElement;
    }
    public void setJsonElement(Object jsonElement) {
        this.jsonElement = jsonElement;
    }
    public Statistics getStatistics() {
        return statistics;
    }
    public void setStatistics(Statistics statistics) {
        this.statistics = statistics;
    }
}
