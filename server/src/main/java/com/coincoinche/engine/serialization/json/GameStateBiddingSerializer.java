package com.coincoinche.engine.serialization.json;

import com.coincoinche.engine.GameStateBidding;
import com.coincoinche.engine.contracts.Contract;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;

/** Custom Serializer for GameStateBidding objects. */
public class GameStateBiddingSerializer extends GameStateSerializer<GameStateBidding> {

  private static final long serialVersionUID = 2626334539845781071L;
  private static final String PHASE_NAME = "bidding";

  public GameStateBiddingSerializer() {
    this(null);
  }

  public GameStateBiddingSerializer(Class<GameStateBidding> t) {
    super(t);
  }

  @Override
  public void serialize(GameStateBidding value, JsonGenerator gen, SerializerProvider provider)
      throws IOException {
    gen.writeStartObject();
    gen.writeStringField("phase", PHASE_NAME);
    gen.writeStringField("currentPlayer", value.getCurrentPlayer().getUsername());
    int multiplier = 1;
    if (value.isSurcoinched()) {
      multiplier = 4;
    } else if (value.isCoinched()) {
      multiplier = 2;
    }
    Contract highestBidding = value.getHighestBidding();
    if (highestBidding != null) {
      writeHighestBidding(highestBidding, gen);
      gen.writeNumberField("multiplier", multiplier);
    }
    gen.writeEndObject();
  }
}
