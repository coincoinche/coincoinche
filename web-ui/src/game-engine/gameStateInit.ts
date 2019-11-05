import {GameRoundPhase, GameState, Position} from "./gameStateTypes";
import {buildUsernamesByPosition} from "./playerPositionning";

export const gameStateInit = (usernames: string[], bottomPlayerIndex: number): GameState => ({
  usernames,
  usernamesByPosition: buildUsernamesByPosition(usernames, bottomPlayerIndex),
  currentPlayer: Position.bottom,
  cardsInHand: [],
  currentPhase: GameRoundPhase.BIDDING,
  currentlySelectedContract: null,
  lastBiddingContract: {},
  legalMoves: []
});