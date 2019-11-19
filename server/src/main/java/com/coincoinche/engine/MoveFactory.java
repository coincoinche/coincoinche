package com.coincoinche.engine;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

/** Factory for moves. */
public class MoveFactory {

  /**
   * Create a move by deserializing its JSON representation. Both bidding moves and playing moves
   * are supported.
   *
   * @param jsonString is the JSON representation of a move.
   * @return the newly created move.
   * @throws IllegalMoveException if a move can't be created from the JSON string.
   */
  public static Move createMove(String jsonString) throws IllegalMoveException {
    if (jsonString.contains("\"bid\"")) {
      try {
        return new ObjectMapper().readValue(jsonString, MoveBidding.class);
      } catch (JsonMappingException e) {
        throw new IllegalMoveException("Invalid JSON structure for bidding move", e);
      } catch (JsonParseException e) {
        throw new IllegalMoveException("Invalid JSON content", e);
      } catch (IOException e) {
        throw new IllegalMoveException("Low-level I/O problem", e);
      }
    }
    if (jsonString.contains("\"card\"")) {
      try {
        return new ObjectMapper().readValue(jsonString, MovePlaying.class);
      } catch (JsonMappingException e) {
        throw new IllegalMoveException("Invalid JSON structure for playing move", e);
      } catch (JsonParseException e) {
        throw new IllegalMoveException("Invalid JSON content", e);
      } catch (IOException e) {
        throw new IllegalMoveException("Low-level I/O problem", e);
      }
    }
    throw new IllegalMoveException("Unable to create any legal move from JSON");
  }
}
