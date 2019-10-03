package com.coincoinche.engine;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import com.coincoinche.engine.cards.Suit;

import org.junit.Test;

/**
 * Unit tests for the contract.
 */
public class ContractTest {

  private class TestCase extends CommonTestHelper.TestCase {
    private Contract contractOne;
    private Contract contractTwo;
    private boolean expected;

    TestCase(String name, Contract contractOne, Contract contractTwo, boolean expected) {
      super(name);
      this.contractOne = contractOne;
      this.contractTwo = contractTwo;
      this.expected = expected;
    }

    @Override
    protected void runAssertions() {
      assertThat(contractOne.equals(contractTwo)).as("Check contract equality").isEqualTo(expected);
    }

  }

  @Test
  public void equals() {
    List<TestCase> testCases = new ArrayList<>(List.of(
      new TestCase(
        "capot heart - capot heart",
        Contract.capotContract(Suit.HEARTS),
        Contract.capotContract(Suit.HEARTS),
        true
      ),
      new TestCase(
        "generale clubs - generale clubs",
        Contract.generaleContract(Suit.CLUBS),
        Contract.generaleContract(Suit.CLUBS),
        true
      ),
      new TestCase(
        "capot heart - capot spades",
        Contract.capotContract(Suit.HEARTS),
        Contract.capotContract(Suit.SPADES),
        false
      ),
      new TestCase(
        "capot heart - generale heart",
        Contract.capotContract(Suit.HEARTS),
        Contract.generaleContract(Suit.HEARTS),
        false
      ),
      new TestCase(
        "generale heart - generale diamonds",
        Contract.generaleContract(Suit.HEARTS),
        Contract.generaleContract(Suit.DIAMONDS),
        false
      ),
      new TestCase(
        "90 heart - 90 heart",
        Contract.pointsContract(90, Suit.HEARTS),
        Contract.pointsContract(90, Suit.HEARTS),
        true
      ),
      new TestCase(
        "90 heart - 130 heart",
        Contract.pointsContract(90, Suit.HEARTS),
        Contract.pointsContract(130, Suit.HEARTS),
        false
      ),
      new TestCase(
        "110 diamonds - 110 spades",
        Contract.pointsContract(110, Suit.DIAMONDS),
        Contract.pointsContract(110, Suit.SPADES),
        false
      ),
      new TestCase(
        "capot heart - 100 heart",
        Contract.capotContract(Suit.HEARTS),
        Contract.pointsContract(100, Suit.HEARTS),
        false
      ),
      new TestCase(
        "generale heart - 140 heart",
        Contract.generaleContract(Suit.HEARTS),
        Contract.pointsContract(140, Suit.HEARTS),
        false
      )
    ));
    testCases.stream().forEach(TestCase::run);
  }

}