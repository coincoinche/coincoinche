package com.coincoinche.engine.contracts;

import static com.coincoinche.engine.contracts.ContractFactory.createCapotContract;
import static com.coincoinche.engine.contracts.ContractFactory.createGeneraleContract;
import static com.coincoinche.engine.contracts.ContractFactory.createPointsContract;
import static org.assertj.core.api.Assertions.assertThat;

import com.coincoinche.engine.GameEngineTestHelper;
import com.coincoinche.engine.cards.Suit;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

/** Unit tests for the contract. */
public class ContractTest {

  @Test
  public void isHigherThan() {
    class TestCase extends GameEngineTestHelper.ComparisonTestCase<Contract> {

      TestCase(String name, Contract c1, Contract c2, boolean expected) {
        super(name, c1, c2, expected);
      }

      @Override
      protected void runAssertions() {
        assertThat(o1.isHigherThan(o2)).as("Check contract is higher").isEqualTo(expected);
      }
    }

    List<TestCase> testCases =
        new ArrayList<>(
            List.of(
                new TestCase("capot hearts - null", createCapotContract(Suit.HEARTS), null, true),
                new TestCase(
                    "capot hearts - capot hearts",
                    createCapotContract(Suit.HEARTS),
                    createCapotContract(Suit.HEARTS),
                    false),
                new TestCase(
                    "generale clubs - generale clubs",
                    createGeneraleContract(Suit.CLUBS),
                    createGeneraleContract(Suit.CLUBS),
                    false),
                new TestCase(
                    "capot hearts - capot spades",
                    createCapotContract(Suit.HEARTS),
                    createCapotContract(Suit.SPADES),
                    false),
                new TestCase(
                    "capot hearts - generale hearts",
                    createCapotContract(Suit.HEARTS),
                    createGeneraleContract(Suit.HEARTS),
                    false),
                new TestCase(
                    "generale hearts - capot diamonds",
                    createGeneraleContract(Suit.HEARTS),
                    createCapotContract(Suit.DIAMONDS),
                    true),
                new TestCase(
                    "90 hearts - 90 hearts",
                    createPointsContract(90, Suit.HEARTS),
                    createPointsContract(90, Suit.HEARTS),
                    false),
                new TestCase(
                    "130 hearts - 90 hearts",
                    createPointsContract(130, Suit.HEARTS),
                    createPointsContract(90, Suit.HEARTS),
                    true),
                new TestCase(
                    "100 spades - 140 clubs",
                    createPointsContract(100, Suit.SPADES),
                    createPointsContract(140, Suit.CLUBS),
                    false),
                new TestCase(
                    "110 diamonds - 80 spades",
                    createPointsContract(110, Suit.DIAMONDS),
                    createPointsContract(80, Suit.SPADES),
                    true),
                new TestCase(
                    "capot hearts - 100 hearts",
                    createCapotContract(Suit.HEARTS),
                    createPointsContract(100, Suit.HEARTS),
                    true),
                new TestCase(
                    "generale hearts - 140 hearts",
                    createGeneraleContract(Suit.HEARTS),
                    createPointsContract(140, Suit.HEARTS),
                    true)));
    testCases.forEach(TestCase::run);
  }

  @Test
  public void equals() {
    class TestCase extends GameEngineTestHelper.ComparisonTestCase<Contract> {

      TestCase(String name, Contract c1, Contract c2, boolean expected) {
        super(name, c1, c2, expected);
      }

      @Override
      protected void runAssertions() {
        assertThat(o1.equals(o2)).as("Check contract equality").isEqualTo(expected);
      }
    }

    List<TestCase> testCases =
        new ArrayList<>(
            List.of(
                new TestCase(
                    "capot hearts - capot hearts",
                    createCapotContract(Suit.HEARTS),
                    createCapotContract(Suit.HEARTS),
                    true),
                new TestCase(
                    "generale clubs - generale clubs",
                    createGeneraleContract(Suit.CLUBS),
                    createGeneraleContract(Suit.CLUBS),
                    true),
                new TestCase(
                    "capot hearts - capot spades",
                    createCapotContract(Suit.HEARTS),
                    createCapotContract(Suit.SPADES),
                    false),
                new TestCase(
                    "capot hearts - generale hearts",
                    createCapotContract(Suit.HEARTS),
                    createGeneraleContract(Suit.HEARTS),
                    false),
                new TestCase(
                    "generale hearts - generale diamonds",
                    createGeneraleContract(Suit.HEARTS),
                    createGeneraleContract(Suit.DIAMONDS),
                    false),
                new TestCase(
                    "90 hearts - 90 hearts",
                    createPointsContract(90, Suit.HEARTS),
                    createPointsContract(90, Suit.HEARTS),
                    true),
                new TestCase(
                    "90 hearts - 130 hearts",
                    createPointsContract(90, Suit.HEARTS),
                    createPointsContract(130, Suit.HEARTS),
                    false),
                new TestCase(
                    "110 diamonds - 110 spades",
                    createPointsContract(110, Suit.DIAMONDS),
                    createPointsContract(110, Suit.SPADES),
                    false),
                new TestCase(
                    "capot hearts - 100 hearts",
                    createCapotContract(Suit.HEARTS),
                    createPointsContract(100, Suit.HEARTS),
                    false),
                new TestCase(
                    "generale hearts - 140 hearts",
                    createGeneraleContract(Suit.HEARTS),
                    createPointsContract(140, Suit.HEARTS),
                    false)));
    testCases.forEach(TestCase::run);
  }
}
