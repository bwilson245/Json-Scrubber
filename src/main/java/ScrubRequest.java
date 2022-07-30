import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.List;

public class ScrubRequest {
    private String replacementValue;
    private List<String> keywords;
    private JsonElement jsonElement;

    public ScrubRequest(String replacementValue, List<String> keywords, Object jsonElement) {
        this.replacementValue = replacementValue;
        this.keywords = keywords;
        this.jsonElement = JsonParser.parseString(new Gson().toJson(jsonElement));
    }


    public String getReplacementValue() {
        return replacementValue;
    }
    public List<String> getKeywords() {
        return keywords;
    }
    public JsonElement getJsonElement() {
        return jsonElement.deepCopy();
    }

    @Override
    public String toString() {
        return "ScrubRequest{" + "replacementValue='" + replacementValue + '\'' + ", keywords=" + keywords + ", jsonElement=" + jsonElement + '}';
    }
}
