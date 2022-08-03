import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ScrubRequestTests {
    private ScrubRequest request;
    private final String validJson = "{\n" + "\"key1\": \"value1\",\n" + "\"key2\": \"value2\",\n" + "\"key3\": \"value3\",\n" + "\"key4\":\n" + "[\n" + "{\n" + "\"key4\": \"value4\",\n" + "\"key5\": \"value5\",\n" + "\"key6\": \"value6\",\n" + "\"key7\": \"value7\",\n" + "\"key8\": \n" + "[\n" + "{\n" + "\"key9\": \"value9\",\n" + "\"key10\": \"value10\"\n" + "}\n" + "]\n" + "}\n" + "]\n" + "}";
    private final String invalidJson = "{\n" + "\"value1\",\n" + "\"key2\": \"value2\",\n" + "\"key3\": \"value3\",\n" + "\"key4\":\n" + "[\n" + "{\n" + "\"key4\": \"value4\",\n" + "\"key5\": \"value5\",\n" + "\"key6\": \"value6\",\n" + "\"key7\": \"value7\",\n" + "\"key8\": \n" + "[\n" + "{\n" + "\"key9\": \"value9\",\n" + "\"key10\": \"value10\"\n" + "}\n" + "]\n" + "}\n" + "]\n" + "}";
    private final String replacementValue = "*****";
    private List<String> keywords;


    @Test
    public void createScrubRequest_validJson_createsScrubRequest() {
        // GIVEN
        keywords = List.of("key1", "key2", "key5", "key8");

        // WHEN
        request = new ScrubRequest(replacementValue, keywords, validJson);
        // THEN

        Assertions.assertEquals(JsonParser.parseString(validJson), request.getJsonElement(), "Expected JsonElements to be equal.");
        Assertions.assertEquals(keywords, request.getKeywords(), "Expected keywords to be equal.");
        Assertions.assertEquals(replacementValue, request.getReplacementValue(), "Expected replacementValues to be equal.");
    }

    @Test
    public void createScrubRequest_invalidJson_ThrowsException() {
        // GIVEN
        keywords = List.of("key1", "key2", "key5", "key8");

        // WHEN
        // THEN
        Assertions.assertThrows(JsonParseException.class, () -> request = new ScrubRequest(replacementValue, keywords, invalidJson), "Expected JsonParseException to be thrown. - ");
    }

}
