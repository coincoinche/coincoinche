package com.coincoinche.engine.serialization.json;

import com.coincoinche.engine.MovePlaying;
import com.coincoinche.engine.cards.Card;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.TextNode;
import java.io.IOException;

/** Custom Deserializer for MovePlaying objects. */
public class MovePlayingDeserializer extends StdDeserializer<MovePlaying> {

  private static final long serialVersionUID = -8662373508456473506L;

  public MovePlayingDeserializer() {
    this(null);
  }

  public MovePlayingDeserializer(Class<?> vc) {
    super(vc);
  }

  @Override
  public MovePlaying deserialize(JsonParser p, DeserializationContext ctxt)
      throws IOException, JsonProcessingException {
    JsonNode node = p.getCodec().readTree(p);
    String shortName = ((TextNode) node.get("card")).textValue();
    return MovePlaying.cardMove(Card.valueOfByShortName(shortName));
  }
}
