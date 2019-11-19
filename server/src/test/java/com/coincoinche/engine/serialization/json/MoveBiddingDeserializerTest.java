package com.coincoinche.engine.serialization.json;

import static org.assertj.core.api.Assertions.assertThat;

import com.coincoinche.engine.GameEngineTestHelper;
import com.coincoinche.engine.MoveBidding;
import com.coincoinche.engine.cards.Suit;
import com.coincoinche.engine.contracts.ContractFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

/** Unit tests for MoveBidding JSON deserialization. */
public class MoveBiddingDeserializerTest {

  @Test
  public void deserialize() {
    class TestCase extends GameEngineTestHelper.TestCase {
      private String serialized;
      private MoveBidding expected;

      public TestCase(String name, String serialized, MoveBidding expected) {
        super(name);
        this.serialized = serialized;
        this.expected = expected;
      }

      @Override
      protected void runAssertions() {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(MoveBidding.class, new MoveBiddingDeserializer());
        mapper.registerModule(module);
        try {
          MoveBidding actual = mapper.readValue(serialized, MoveBidding.class);
          assertThat(actual).as("Check deserialization is as expected").isEqualTo(expected);
        } catch (IOException e) {
          throw new RuntimeException(String.format("%s: IOException wan't expected", name), e);
        } catch (Exception e) {
          throw new RuntimeException(String.format("%s: Exception wan't expected", name), e);
        }
      }
    }

    List<TestCase> testCases =
        new ArrayList<>(
            List.of(
                new TestCase("pass", "{\"bid\":\"pass\"}", MoveBidding.passMove()),
                new TestCase("coinche", "{\"bid\":\"coinche\"}", MoveBidding.coincheMove()),
                new TestCase(
                    "surcoinche", "{\"bid\":\"surcoinche\"}", MoveBidding.surcoincheMove()),
                new TestCase(
                    "80h",
                    "{\"bid\":\"80h\"}",
                    MoveBidding.contractMove(ContractFactory.createContract(80, Suit.HEARTS))),
                new TestCase(
                    "110s",
                    "{\"bid\":\"110s\"}",
                    MoveBidding.contractMove(ContractFactory.createContract(110, Suit.SPADES))),
                new TestCase(
                    "110c",
                    "{\"bid\":\"110c\"}",
                    MoveBidding.contractMove(ContractFactory.createContract(110, Suit.CLUBS))),
                new TestCase(
                    "150h",
                    "{\"bid\":\"150h\"}",
                    MoveBidding.contractMove(ContractFactory.createContract(150, Suit.HEARTS))),
                new TestCase(
                    "150d",
                    "{\"bid\":\"150d\"}",
                    MoveBidding.contractMove(ContractFactory.createContract(150, Suit.DIAMONDS))),
                new TestCase(
                    "CAPh",
                    "{\"bid\":\"250h\"}",
                    MoveBidding.contractMove(ContractFactory.createContract(250, Suit.HEARTS))),
                new TestCase(
                    "CAPs",
                    "{\"bid\":\"250s\"}",
                    MoveBidding.contractMove(ContractFactory.createContract(250, Suit.SPADES))),
                new TestCase(
                    "GENd",
                    "{\"bid\":\"500d\"}",
                    MoveBidding.contractMove(ContractFactory.createContract(500, Suit.DIAMONDS))),
                new TestCase(
                    "GENc",
                    "{\"bid\":\"500c\"}",
                    MoveBidding.contractMove(ContractFactory.createContract(500, Suit.CLUBS)))));
    testCases.forEach(TestCase::run);
  }
}
