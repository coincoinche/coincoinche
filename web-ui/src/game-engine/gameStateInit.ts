import {GameRoundPhase, GameState, Position} from "./gameStateTypes";

export const gameStateInit = (): GameState => ({
  usernames: [],
  currentPlayer: Position.bottom,
  cardsInHand: [],
  currentPhase: GameRoundPhase.BIDDING,
  currentlySelectedContract: null,
  lastBiddingContract: {},
  legalMoves: []
});