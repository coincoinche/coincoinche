package com.coincoinche.engine;

import com.coincoinche.engine.teams.Player;
import com.coincoinche.engine.teams.Team;
import java.util.ArrayList;
import java.util.List;

/** TODO nockty add detailed documentation here. */
public class GameStateBidding implements GameStateTerminal {

  private Player currentPlayer;
  private Contract highestBidding;
  private boolean coinched;
  private boolean surcoinched;

  GameStateBidding(
      Player currentPlayer, Contract highestBidding, boolean coinched, boolean surcoinched) {
    this.currentPlayer = currentPlayer;
    this.highestBidding = highestBidding;
    this.coinched = coinched;
    this.surcoinched = surcoinched;
  }

  public static GameStateBidding InitialGameStateBidding(Player firstPlayer) {
    return new GameStateBidding(firstPlayer, null, false, false);
  }

  /**
   * Return legal moves given the state of the bidding game. The order of the returned moves is
   * guaranteed.
   */
  @Override
  public List<Move> getLegalMoves() {
    List<Move> legalMoves = getUnsortedLegalMoves();
    legalMoves.sort(null);
    return legalMoves;
  }

  private List<Move> getUnsortedLegalMoves() {
    List<Move> legalMoves = new ArrayList<>();
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

  /**
   * Return a boolean indicating if the game state must change, i.e. if the game gets to a state
   * other than the bidding state. This method makes sense if it is called after a move was applied
   * to the game.
   */
  @Override
  public boolean mustChange() {
    return surcoinched || currentPlayer.equals(highestBidding.getPlayer());
  }

  @Override
  public Team getWinnerTeam() {
    return highestBidding.getPlayer().getTeam();
  }

  @Override
  public int getWinnerPoints() {
    return highestBidding.getPoints();
  }

  public void setCoinched(boolean coinched) {
    this.coinched = coinched;
  }

  public void setSurcoinched(boolean surcoinched) {
    this.surcoinched = surcoinched;
  }

  public void setHighestBidding(Contract highestBidding) {
    this.highestBidding = highestBidding;
  }

  // TODO nockty see if we can use a default method here
  @Override
  public void setCurrentPlayer(Player currentPlayer) {
    this.currentPlayer = currentPlayer;
  }

  // TODO nockty see if we can use a default method here
  @Override
  public Player getCurrentPlayer() {
    return currentPlayer;
  }
}
