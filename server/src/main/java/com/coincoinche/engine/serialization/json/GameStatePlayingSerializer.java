package com.coincoinche.engine.serialization.json;

import com.coincoinche.engine.GameStatePlaying;
import com.coincoinche.engine.cards.Trick;
import com.coincoinche.engine.contracts.Contract;
import com.coincoinche.engine.teams.Player;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.util.List;

/** Custom Serializer for GameStatePlaying objects. */
public class GameStatePlayingSerializer extends GameStateSerializer<GameStatePlaying> {

  private static final long serialVersionUID = -7864585594588432088L;
  private static final String PHASE_NAME = "main";

  public GameStatePlayingSerializer() {
    this(null);
  }

  public GameStatePlayingSerializer(Class<GameStatePlaying> t) {
    super(t);
  }

  private void writeTrick(int trickNumber, Trick trick, JsonGenerator gen) throws IOException {
    gen.writeStartObject();
    gen.writeNumberField("no", trickNumber);
    gen.writeFieldName("cards");
    gen.writeStartObject();
    for (Player player : trick.getPlayers()) {
      gen.writeStringField(player.getUsername(), trick.getPlayerCard(player).getShortName());
    }
    gen.writeEndObject();
    gen.writeEndObject();
  }

  private void writePreviousTrick(GameStatePlaying state, JsonGenerator gen) throws IOException {
    List<Trick> trickHistory = state.getTrickHistory();
    gen.writeFieldName("previousTrick");
    if (trickHistory.isEmpty()) {
      gen.writeNull();
      return;
    }
    writeTrick(state.getCurrentTrickNumber() - 1, trickHistory.get(trickHistory.size() - 1), gen);
  }

  private void writeCurrentTrick(GameStatePlaying state, JsonGenerator gen) throws IOException {
    gen.writeFieldName("currentTrick");
    writeTrick(state.getCurrentTrickNumber(), state.getCurrentTrick(), gen);
  }

  @Override
  public void serialize(GameStatePlaying value, JsonGenerator gen, SerializerProvider provider)
      throws IOException {
    gen.writeStartObject();
    gen.writeStringField("phase", PHASE_NAME);
    gen.writeStringField("currentPlayer", value.getCurrentPlayer().getUsername());
    int multiplier = value.getMultiplier();
    Contract highestBidding = value.getContract();
    if (highestBidding != null) {
      writeHighestBidding(highestBidding, gen);
      gen.writeNumberField("multiplier", multiplier);
    }
    writeCurrentTrick(value, gen);
    writePreviousTrick(value, gen);
    gen.writeEndObject();
  }
}
