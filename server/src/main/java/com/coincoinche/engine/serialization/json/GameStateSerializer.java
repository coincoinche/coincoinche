package com.coincoinche.engine.serialization.json;

import com.coincoinche.engine.contracts.Contract;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;

/** Abstract serializer for objects implementing GameState. */
abstract class GameStateSerializer<T> extends StdSerializer<T> {

  private static final long serialVersionUID = 3419570214822633802L;

  protected GameStateSerializer() {
    this(null);
  }

  protected GameStateSerializer(Class<T> t) {
    super(t);
  }

  protected void writeHighestBidding(Contract highestBidding, JsonGenerator gen)
      throws IOException {
    if (highestBidding == null) {
      return;
    }
    gen.writeFieldName("highestBidding");
    gen.writeStartObject();
    if (highestBidding.getPlayer() != null) {
      gen.writeStringField("owner", highestBidding.getPlayer().getUsername());
    }
    gen.writeStringField("suit", highestBidding.getSuit().getShortName());
    gen.writeStringField("value", highestBidding.getValueShortName());
    gen.writeEndObject();
  }
}
