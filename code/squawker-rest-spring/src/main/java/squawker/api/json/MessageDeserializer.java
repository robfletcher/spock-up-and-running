package squawker.api.json;

import java.io.IOException;
import java.time.Instant;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import squawker.Message;
import squawker.User;

public class MessageDeserializer extends JsonDeserializer<Message> {
  @Override public Class<?> handledType() {
    return Message.class;
  }

  @Override
  public Message deserialize(JsonParser parser, DeserializationContext context) throws IOException, JsonProcessingException {
    ObjectCodec oc = parser.getCodec();
    JsonNode node = oc.readTree(parser);
    return new Message(
      node.get("id").asLong(),
      deserializeUser(node.get("postedBy")),
      node.get("text").asText(),
      deserializeInstant(node.get("postedAt"))
    );
  }

  private User deserializeUser(JsonNode node) {
    return new User(
      node.get("id").asLong(),
      node.get("username").asText(),
      deserializeInstant(node.get("registered"))
    );
  }

  private Instant deserializeInstant(JsonNode node) {
    return Instant.parse(node.asText());
  }
}
