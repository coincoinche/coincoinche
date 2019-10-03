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

  abstract class TwoContractsTestCase extends GameEngineTestHelper.TestCase {
    protected Contract contractOne;
    protected Contract contractTwo;
    protected boolean expected;

    TwoContractsTestCase(String name, Contract contractOne, Contract contractTwo, boolean expected) {
      super(name);
      this.contractOne = contractOne;
      this.contractTwo = contractTwo;
      this.expected = expected;
    }
  }

  @Test
  public void isHigherThan() {
    class TestCase extends TwoContractsTestCase {

      TestCase(String name, Contract contractOne, Contract contractTwo, boolean expected) {
        super(name, contractOne, contractTwo, expected);
      }

      @Override
      protected void runAssertions() {
        assertThat(contractOne.isHigherThan(contractTwo)).as("Check contract is higher").isEqualTo(expected);
      }
    }

    List<TestCase> testCases = new ArrayList<>(List.of(
      new TestCase(
        "capot hearts - null",
        Contract.capotContract(Suit.HEARTS),
        null,
        true
      ),
      new TestCase(
        "capot hearts - capot hearts",
        Contract.capotContract(Suit.HEARTS),
        Contract.capotContract(Suit.HEARTS),
        false
      ),
      new TestCase(
        "generale clubs - generale clubs",
        Contract.generaleContract(Suit.CLUBS),
        Contract.generaleContract(Suit.CLUBS),
        false
      ),
      new TestCase(
        "capot hearts - capot spades",
        Contract.capotContract(Suit.HEARTS),
        Contract.capotContract(Suit.SPADES),
        false
      ),
      new TestCase(
        "capot hearts - generale hearts",
        Contract.capotContract(Suit.HEARTS),
        Contract.generaleContract(Suit.HEARTS),
        false
      ),
      new TestCase(
        "generale hearts - capot diamonds",
        Contract.generaleContract(Suit.HEARTS),
        Contract.capotContract(Suit.DIAMONDS),
        true
      ),
      new TestCase(
        "90 hearts - 90 hearts",
        Contract.pointsContract(90, Suit.HEARTS),
        Contract.pointsContract(90, Suit.HEARTS),
        false
      ),
      new TestCase(
        "130 hearts - 90 hearts",
        Contract.pointsContract(130, Suit.HEARTS),
        Contract.pointsContract(90, Suit.HEARTS),
        true
      ),
      new TestCase(
        "100 spades - 140 clubs",
        Contract.pointsContract(100, Suit.SPADES),
        Contract.pointsContract(140, Suit.CLUBS),
        false
      ),
      new TestCase(
        "110 diamonds - 80 spades",
        Contract.pointsContract(110, Suit.DIAMONDS),
        Contract.pointsContract(80, Suit.SPADES),
        true
      ),
      new TestCase(
        "capot hearts - 100 hearts",
        Contract.capotContract(Suit.HEARTS),
        Contract.pointsContract(100, Suit.HEARTS),
        true
      ),
      new TestCase(
        "generale hearts - 140 hearts",
        Contract.generaleContract(Suit.HEARTS),
        Contract.pointsContract(140, Suit.HEARTS),
        true
      )
    ));
    testCases.forEach(TestCase::run);
  }

  @Test
  public void equals() {
    class TestCase extends TwoContractsTestCase {

      TestCase(String name, Contract contractOne, Contract contractTwo, boolean expected) {
        super(name, contractOne, contractTwo, expected);
      }

      @Override
      protected void runAssertions() {
        assertThat(contractOne.equals(contractTwo)).as("Check contract equality").isEqualTo(expected);
      }

    }

    List<TestCase> testCases = new ArrayList<>(List.of(
      new TestCase(
        "capot hearts - capot hearts",
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
        "capot hearts - capot spades",
        Contract.capotContract(Suit.HEARTS),
        Contract.capotContract(Suit.SPADES),
        false
      ),
      new TestCase(
        "capot hearts - generale hearts",
        Contract.capotContract(Suit.HEARTS),
        Contract.generaleContract(Suit.HEARTS),
        false
      ),
      new TestCase(
        "generale hearts - generale diamonds",
        Contract.generaleContract(Suit.HEARTS),
        Contract.generaleContract(Suit.DIAMONDS),
        false
      ),
      new TestCase(
        "90 hearts - 90 hearts",
        Contract.pointsContract(90, Suit.HEARTS),
        Contract.pointsContract(90, Suit.HEARTS),
        true
      ),
      new TestCase(
        "90 hearts - 130 hearts",
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
        "capot hearts - 100 hearts",
        Contract.capotContract(Suit.HEARTS),
        Contract.pointsContract(100, Suit.HEARTS),
        false
      ),
      new TestCase(
        "generale hearts - 140 hearts",
        Contract.generaleContract(Suit.HEARTS),
        Contract.pointsContract(140, Suit.HEARTS),
        false
      )
    ));
    testCases.forEach(TestCase::run);
  }

}