import {GamePhase, GameState, Position} from "./gameStateTypes";

export const gameStateInit = (): GameState => ({
  usernames: [],
  currentPlayer: Position.bottom,
  cardsInHand: [],
  currentPhase: GamePhase.bidding,
  currentlySelectedContract: null,
  lastBiddingContract: {},
  legalMoves: []
});