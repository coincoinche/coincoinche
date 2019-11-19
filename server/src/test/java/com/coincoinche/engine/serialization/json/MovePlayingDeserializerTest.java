package com.coincoinche.engine.serialization.json;

import static org.assertj.core.api.Assertions.assertThat;

import com.coincoinche.engine.GameEngineTestHelper;
import com.coincoinche.engine.MovePlaying;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

/** Unit tests for MovePlaying JSON deserialization. */
public class MovePlayingDeserializerTest extends GameEngineTestHelper {

  @Test
  public void deserialize() {

    class TestCase extends GameEngineTestHelper.TestCase {
      private String serialized;
      private MovePlaying expected;

      public TestCase(String name, String serialized, MovePlaying expected) {
        super(name);
        this.serialized = serialized;
        this.expected = expected;
      }

      @Override
      protected void runAssertions() {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(MovePlaying.class, new MovePlayingDeserializer());
        mapper.registerModule(module);
        try {
          MovePlaying actual = mapper.readValue(serialized, MovePlaying.class);
          assertThat(actual).as("Check deserialization is as expected").isEqualTo(expected);
        } catch (IOException e) {
          throw new RuntimeException(String.format("%s: Exception wan't expected", name), e);
        }
      }
    }

    List<TestCase> testCases =
        new ArrayList<>(
            List.of(
                new TestCase("7h", "{\"card\":\"7h\"}", MovePlaying.cardMove(stringToCard("7h"))),
                new TestCase("As", "{\"card\":\"As\"}", MovePlaying.cardMove(stringToCard("As"))),
                new TestCase("Jc", "{\"card\":\"Jc\"}", MovePlaying.cardMove(stringToCard("Jc"))),
                new TestCase("9d", "{\"card\":\"9d\"}", MovePlaying.cardMove(stringToCard("9d"))),
                new TestCase("Ks", "{\"card\":\"Ks\"}", MovePlaying.cardMove(stringToCard("Ks"))),
                new TestCase("Qc", "{\"card\":\"Qc\"}", MovePlaying.cardMove(stringToCard("Qc"))),
                new TestCase("Jd", "{\"card\":\"Jd\"}", MovePlaying.cardMove(stringToCard("Jd"))),
                new TestCase("8s", "{\"card\":\"8s\"}", MovePlaying.cardMove(stringToCard("8s"))),
                new TestCase("7c", "{\"card\":\"7c\"}", MovePlaying.cardMove(stringToCard("7c")))));
    testCases.forEach(TestCase::run);
  }
}
