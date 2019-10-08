package com.coincoinche.engine;

import com.coincoinche.engine.teams.Player;
import java.util.ArrayList;
import java.util.List;

/** TODO nockty add detailed documentation here. */
public class BiddingGameState implements GameStateInterface {

  private Player currentPlayer;
  private Contract highestBidding;
  private boolean coinched;

  BiddingGameState(
      Player currentPlayer, Contract highestBidding, boolean coinched, boolean surcoinched) {
    this.currentPlayer = currentPlayer;
    this.highestBidding = highestBidding;
    this.coinched = coinched;
  }

  /**
   * TODO nockty add documentation here.
   *
   * @param move is the move that should be applied.
   */
  public void applyMove(BiddingMove move) {
    // TODO nockty implement this
  }

  /**
   * Return legal moves given the state of the bidding game. The order of the returned moves is
   * guaranteed.
   */
  @Override
  public List<MoveInterface> getLegalMoves() {
    List<MoveInterface> legalMoves = getUnsortedLegalMoves();
    legalMoves.sort(null);
    return legalMoves;
  }

  private List<MoveInterface> getUnsortedLegalMoves() {
    List<MoveInterface> legalMoves = new ArrayList<>();
    // Can always pass
    legalMoves.add(BiddingMove.passMove());
    // only surcoinche is legal if there is a coinche
    if (coinched) {
      if (currentPlayer.isTeamMate(highestBidding.getPlayer())) {
        legalMoves.add(BiddingMove.surcoincheMove());
      }
      return legalMoves;
    }
    // contracts strictly better than current contract are legal
    for (Contract contract : Contract.generateAllContracts()) {
      if (contract.isHigherThan(highestBidding)) {
        legalMoves.add(BiddingMove.contractMove(contract));
      }
    }
    // coinche is legal if an opponent has the highest bidding
    if (highestBidding != null && !currentPlayer.isTeamMate(highestBidding.getPlayer())) {
      legalMoves.add(BiddingMove.coincheMove());
    }
    return legalMoves;
  }
}
