import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

public class ScrubRequest {
    private String replacementValue;
    private List<String> keywords;
    private JsonElement jsonElement;

    public ScrubRequest() {}

    public ScrubRequest(String replacementValue, List<String> keywords, String jsonElement) {
        this.replacementValue = replacementValue;
        this.keywords = keywords;
        this.jsonElement = JsonParser.parseString(jsonElement);
    }

    public String getReplacementValue() {
        return replacementValue;
    }
    public void setReplacementValue(String replacementValue) {
        this.replacementValue = replacementValue;
    }
    public List<String> getKeywords() {
        return new ArrayList<>(keywords);
    }
    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }
    public JsonElement getJsonElement() {
        return jsonElement.deepCopy();
    }
    public void setJsonElement(Object jsonElement) throws JsonProcessingException {
        ObjectMapper  mapper = new ObjectMapper();
        this.jsonElement = JsonParser.parseString(mapper.writeValueAsString(jsonElement));
    }
    @Override
    public String toString() {
        return "ScrubRequest{" + "replacementValue='" + replacementValue + '\'' + ", keywords=" + keywords + ", jsonElement=" + jsonElement + '}';
    }
}
