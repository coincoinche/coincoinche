package com.coincoinche.engine.serialization.json;

import static org.assertj.core.api.Assertions.assertThat;

import com.coincoinche.engine.GameEngineTestHelper;
import com.coincoinche.engine.MoveBidding;
import com.coincoinche.engine.cards.Suit;
import com.coincoinche.engine.contracts.ContractFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

/** Unit tests for MoveBidding JSON serialization. */
public class MoveBiddingSerializerTest {

  @Test
  public void serialize() {

    class TestCase extends GameEngineTestHelper.TestCase {
      private MoveBidding move;
      private String expected;

      public TestCase(String name, MoveBidding move, String expected) {
        super(name);
        this.move = move;
        this.expected = expected;
      }

      @Override
      protected void runAssertions() {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(MoveBidding.class, new MoveBiddingSerializer());
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
                new TestCase("pass", MoveBidding.passMove(), "{\"bid\":\"pass\"}"),
                new TestCase("coinche", MoveBidding.coincheMove(), "{\"bid\":\"coinche\"}"),
                new TestCase(
                    "surcoinche", MoveBidding.surcoincheMove(), "{\"bid\":\"surcoinche\"}"),
                new TestCase(
                    "80h",
                    MoveBidding.contractMove(ContractFactory.createContract(80, Suit.HEARTS)),
                    "{\"bid\":\"80h\"}"),
                new TestCase(
                    "110s",
                    MoveBidding.contractMove(ContractFactory.createContract(110, Suit.SPADES)),
                    "{\"bid\":\"110s\"}"),
                new TestCase(
                    "110c",
                    MoveBidding.contractMove(ContractFactory.createContract(110, Suit.CLUBS)),
                    "{\"bid\":\"110c\"}"),
                new TestCase(
                    "150h",
                    MoveBidding.contractMove(ContractFactory.createContract(150, Suit.HEARTS)),
                    "{\"bid\":\"150h\"}"),
                new TestCase(
                    "150d",
                    MoveBidding.contractMove(ContractFactory.createContract(150, Suit.DIAMONDS)),
                    "{\"bid\":\"150d\"}"),
                new TestCase(
                    "CAPh",
                    MoveBidding.contractMove(ContractFactory.createContract(250, Suit.HEARTS)),
                    "{\"bid\":\"250h\"}"),
                new TestCase(
                    "CAPs",
                    MoveBidding.contractMove(ContractFactory.createContract(250, Suit.SPADES)),
                    "{\"bid\":\"250s\"}"),
                new TestCase(
                    "GENd",
                    MoveBidding.contractMove(ContractFactory.createContract(500, Suit.DIAMONDS)),
                    "{\"bid\":\"500d\"}"),
                new TestCase(
                    "GENc",
                    MoveBidding.contractMove(ContractFactory.createContract(500, Suit.CLUBS)),
                    "{\"bid\":\"500c\"}")));
    testCases.forEach(TestCase::run);
  }
}
