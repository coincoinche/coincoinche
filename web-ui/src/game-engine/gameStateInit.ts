import {GamePhase, GameState, Position} from "./gameStateTypes";

// TODO: unify null vs undefined along properties in GameState.
export const gameStateInit = (): GameState => ({
  usernames: [],
  currentPlayer: Position.bottom,
  cardsInHand: [],
  currentPhase: GamePhase.bidding,
  currentlySelectedContract: null,
  lastBiddingContract: {},
  legalMoves: []
});