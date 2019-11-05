package com.coincoinche.engine.contracts;

import com.coincoinche.engine.cards.Suit;
import java.util.ArrayList;
import java.util.List;

/** Factory to create contracts. */
public class ContractFactory {

  static Contract createPointsContract(int value, Suit suit) throws IllegalArgumentException {
    return new ContractPoints(value, suit);
  }

  static Contract createCapotContract(Suit suit) {
    return new ContractCapot(suit);
  }

  static Contract createGeneraleContract(Suit suit) {
    return new ContractGenerale(suit);
  }

  /**
   * Generate every possible contract in the game.
   *
   * @return the list of all possible contracts.
   */
  public static List<Contract> createAllContracts() {
    List<Contract> allContracts = new ArrayList<>();
    for (Suit suit : Suit.values()) {
      for (int value : ContractPoints.getLegalvalues()) {
        allContracts.add(createPointsContract(value, suit));
      }
      allContracts.add(createCapotContract(suit));
      allContracts.add(createGeneraleContract(suit));
    }
    return allContracts;
  }

  /**
   * Create a contract from a value and a suit.
   *
   * <ul>
   *   <li>80 <= value <= 160: create a points contract
   *   <li>value == 250: create a capot contract
   *   <li>value == 500: create a generale contract
   * </ul>
   *
   * @param value is an integer representing the contract.
   * @param suit is the trump suit of the contract.
   * @return the newly created contract.
   * @throws IllegalArgumentException if the value is not legal.
   */
  public static Contract createContract(int value, Suit suit) throws IllegalArgumentException {
    if (value == 250) {
      return createCapotContract(suit);
    }
    if (value == 500) {
      return createGeneraleContract(suit);
    }
    return createPointsContract(value, suit);
  }
}
