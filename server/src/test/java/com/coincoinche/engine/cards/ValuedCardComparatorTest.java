package com.coincoinche.engine.cards;

import static org.assertj.core.api.Assertions.assertThat;

import com.coincoinche.engine.GameEngineTestHelper;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

/** Unit tests for valued cards comparison. */
public class ValuedCardComparatorTest extends GameEngineTestHelper {

  class TestCase extends GameEngineTestHelper.ComparisonTestCase<ValuedCard> {
    private Suit desiredSuit;

    TestCase(String name, Suit desiredSuit, ValuedCard c1, ValuedCard c2, boolean expected) {
      super(name, c1, c2, expected);
      this.desiredSuit = desiredSuit;
    }

    @Override
    protected void runAssertions() {
      ValuedCardComparator comparator = new ValuedCardComparator(desiredSuit);
      if (expected) {
        assertThat(comparator.compare(o1, o2)).as("Check greater than").isGreaterThan(0);
      } else {
        assertThat(comparator.compare(o1, o2)).as("Check less than").isLessThan(0);
      }
    }
  }

  @Test
  public void compare() {
    List<TestCase> testCases =
        new ArrayList<>(
            List.of(
                new TestCase(
                    "Same suit, desired, no trump - A > J",
                    Suit.HEARTS,
                    ValuedCard.fromCard(stringToCard("Ah"), false),
                    ValuedCard.fromCard(stringToCard("Jh"), false),
                    true),
                new TestCase(
                    "Same suit, desired, no trump - J < A",
                    Suit.HEARTS,
                    ValuedCard.fromCard(stringToCard("Jh"), false),
                    ValuedCard.fromCard(stringToCard("Ah"), false),
                    false),
                new TestCase(
                    "Same suit, non-desired, no trump - T > 9",
                    Suit.SPADES,
                    ValuedCard.fromCard(stringToCard("Tc"), false),
                    ValuedCard.fromCard(stringToCard("9c"), false),
                    true),
                new TestCase(
                    "Same suit, non-desired, no trump - 9 < T",
                    Suit.SPADES,
                    ValuedCard.fromCard(stringToCard("9c"), false),
                    ValuedCard.fromCard(stringToCard("Tc"), false),
                    false),
                new TestCase(
                    "Same suit, desired, trump - A < J",
                    Suit.HEARTS,
                    ValuedCard.fromCard(stringToCard("Ah"), true),
                    ValuedCard.fromCard(stringToCard("Jh"), true),
                    false),
                new TestCase(
                    "Same suit, desired, trump - J > A",
                    Suit.DIAMONDS,
                    ValuedCard.fromCard(stringToCard("Ad"), true),
                    ValuedCard.fromCard(stringToCard("Jd"), true),
                    false),
                new TestCase(
                    "Same suit, non-desired, trump - T < 9",
                    Suit.CLUBS,
                    ValuedCard.fromCard(stringToCard("Th"), true),
                    ValuedCard.fromCard(stringToCard("9h"), true),
                    false),
                new TestCase(
                    "Same suit, non-desired, trump - 9 > T",
                    Suit.CLUBS,
                    ValuedCard.fromCard(stringToCard("9h"), true),
                    ValuedCard.fromCard(stringToCard("Th"), true),
                    true),
                new TestCase(
                    "Desired H, Trump C, 7h > Qd",
                    Suit.HEARTS,
                    ValuedCard.fromCard(stringToCard("7h"), false),
                    ValuedCard.fromCard(stringToCard("Qd"), false),
                    true),
                new TestCase(
                    "Desired S, Trump H, As < 8h",
                    Suit.SPADES,
                    ValuedCard.fromCard(stringToCard("As"), false),
                    ValuedCard.fromCard(stringToCard("8h"), true),
                    false),
                new TestCase(
                    "Desired D, Trump D, Ad > Jc",
                    Suit.DIAMONDS,
                    ValuedCard.fromCard(stringToCard("Ad"), true),
                    ValuedCard.fromCard(stringToCard("8h"), false),
                    true),
                new TestCase(
                    "Desired D, Trump D, Td > Ac",
                    Suit.DIAMONDS,
                    ValuedCard.fromCard(stringToCard("Td"), true),
                    ValuedCard.fromCard(stringToCard("Ac"), false),
                    true),
                new TestCase(
                    "Desired S, Trump H, Ah < 9h",
                    Suit.SPADES,
                    ValuedCard.fromCard(stringToCard("Ah"), true),
                    ValuedCard.fromCard(stringToCard("9h"), true),
                    false),
                new TestCase(
                    "Desired S, Trump H, Th > 8h",
                    Suit.SPADES,
                    ValuedCard.fromCard(stringToCard("Th"), true),
                    ValuedCard.fromCard(stringToCard("8h"), true),
                    true)));
    testCases.forEach(TestCase::run);
  }
}
