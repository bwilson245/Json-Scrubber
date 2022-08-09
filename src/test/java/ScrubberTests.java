import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class ScrubberTests {
    private ScrubRequest request;
    private Scrubber scrubber;
    private ScrubResult result;
    private final String validJson = "{\n" + "\"key1\": \"value1\",\n" + "\"key2\": null,\n" + "\"key3\": \"value3\",\n" + "\"key4\":\n" + "[\n" + "{\n" + "\"key4\": \"value4\",\n" + "\"key5\": \"value5\",\n" + "\"key6\": \"value6\",\n" + "\"key7\": \"value7\",\n" + "\"key8\": \n" + "[\n" + "{\n" + "\"key9\": \"value9\",\n" + "\"key10\": \"value10\"\n" + "}\n" + "]\n" + "}\n" + "]\n" + "}";
    private final String replacementValue = "*****";
    private List<String> keywords;

    @Test
    public void handleRequest_validRequest_scrubsElements() {
        // GIVEN
        keywords = List.of("key1", "key2", "key5", "key8");
        request = new ScrubRequest(replacementValue, keywords, validJson);
        scrubber = new Scrubber(request);
        int expectedElements = 13;
        int expectedObjects = 2;
        int expectedArrays = 2;
        int expectedPrimitives = 8;
        int expectedNull = 1;
        int expectedScrubbed = 5;

        // WHEN
        result = scrubber.handleRequest();

        // THEN
        Assertions.assertNotEquals(JsonParser.parseString(validJson), result.getJsonElement(), "Expected elements to be scrubbed");
        Assertions.assertEquals(expectedElements, result.getStatistics().getTotalElements(), "Expected totalElements to be " + expectedElements);
        Assertions.assertEquals(expectedObjects, result.getStatistics().getTotalObjects(), "Expected totalObjects to be " + expectedObjects);
        Assertions.assertEquals(expectedArrays,result.getStatistics().getTotalArrays(), "Expected totalArrays to be " + expectedArrays);
        Assertions.assertEquals(expectedPrimitives,result.getStatistics().getTotalPrimitives(), "Expected totalPrimitives to be " + expectedPrimitives);
        Assertions.assertEquals(expectedNull, result.getStatistics().getTotalNull(), "Expected totalNull to be " + expectedNull);
        Assertions.assertEquals(expectedScrubbed,result.getStatistics().getTotalScrubbedElements(), "Expected totalScrubbedElements to be " + expectedScrubbed);
    }

    @Test
    public void handleRequest_noKeywords_noChange() {
        // GIVEN
        keywords = List.of("key1", "key2", "key5", "key8");
        request = new ScrubRequest(replacementValue, keywords, validJson);
        scrubber = new Scrubber(request);

        // WHEN
        result = scrubber.handleRequest();

        // THEN
        Assertions.assertEquals(JsonParser.parseString(validJson), JsonParser.parseString(request.getJsonElement().toString()), "Expected no change to be made.");
    }

    @Test
    public void handleRequest_validJson_valuesReplaced() {
        // GIVEN
        keywords = List.of("key1", "key2", "key5", "key8");
        request = new ScrubRequest(replacementValue, keywords, validJson);
        scrubber = new Scrubber(request);

        // WHEN
        result = scrubber.handleRequest();


        // THEN
        Assertions.assertEquals(replacementValue, JsonParser.parseString(result.getJsonElement().toString()).getAsJsonObject().get("key1").getAsString());
        Assertions.assertEquals(replacementValue, JsonParser.parseString(result.getJsonElement().toString()).getAsJsonObject().get("key2").getAsString());
        Assertions.assertEquals(replacementValue, JsonParser.parseString(result.getJsonElement().toString()).getAsJsonObject().get("key4").getAsJsonArray().get(0).getAsJsonObject().get("key5").getAsString());

        JsonArray array = JsonParser.parseString(result.getJsonElement().toString()).getAsJsonObject().get("key4").getAsJsonArray().get(0).getAsJsonObject().get("key8").getAsJsonArray();
        IntStream.range(0, array.size()).forEach(index -> Assertions.assertEquals(replacementValue, array.get(index).getAsJsonObject().entrySet().stream().map(Map.Entry::getValue).findFirst().get().getAsString()));
    }
}
