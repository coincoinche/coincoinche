package com.coincoinche.engine.serialization.json;

import static org.assertj.core.api.Assertions.assertThat;

import com.coincoinche.engine.GameEngineTestHelper;
import com.coincoinche.engine.MovePlaying;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

/** Unit tests for MovePlaying JSON serialization. */
public class MovePlayingSerializerTest extends GameEngineTestHelper {

  @Test
  public void serialize() {

    class TestCase extends GameEngineTestHelper.TestCase {
      private MovePlaying move;
      private String expected;

      public TestCase(String name, MovePlaying move, String expected) {
        super(name);
        this.move = move;
        this.expected = expected;
      }

      @Override
      protected void runAssertions() {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(MovePlaying.class, new MovePlayingSerializer());
        mapper.registerModule(module);
        try {
          String actual = mapper.writeValueAsString(move);
          assertThat(actual).as("Check serialization is as expected").isEqualTo(expected);
        } catch (JsonProcessingException e) {
          throw new RuntimeException(
              String.format("%s: JsonProcessingException wan't expected", name));
        }
      }
    }

    List<TestCase> testCases =
        new ArrayList<>(
            List.of(
                new TestCase("7h", MovePlaying.cardMove(stringToCard("7h")), "{\"card\":\"7h\"}"),
                new TestCase("As", MovePlaying.cardMove(stringToCard("As")), "{\"card\":\"As\"}"),
                new TestCase("Jc", MovePlaying.cardMove(stringToCard("Jc")), "{\"card\":\"Jc\"}"),
                new TestCase("9d", MovePlaying.cardMove(stringToCard("9d")), "{\"card\":\"9d\"}"),
                new TestCase("Ks", MovePlaying.cardMove(stringToCard("Ks")), "{\"card\":\"Ks\"}"),
                new TestCase("Qc", MovePlaying.cardMove(stringToCard("Qc")), "{\"card\":\"Qc\"}"),
                new TestCase("Jd", MovePlaying.cardMove(stringToCard("Jd")), "{\"card\":\"Jd\"}"),
                new TestCase("8s", MovePlaying.cardMove(stringToCard("8s")), "{\"card\":\"8s\"}"),
                new TestCase("7c", MovePlaying.cardMove(stringToCard("7c")), "{\"card\":\"7c\"}")));
    testCases.forEach(TestCase::run);
  }
}
