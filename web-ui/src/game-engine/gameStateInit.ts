import {GameRoundPhase, GameState, Position, Player} from "./gameStateTypes";
import {buildUsernamesByPosition} from "./playerPositionning";

export const gameStateInit = (users: Player[], bottomPlayerIndex: number): GameState => ({
  users,
  usernamesByPosition: buildUsernamesByPosition(users.map(u => u.username), bottomPlayerIndex),
  currentPlayer: Position.bottom,
  cards: {},

  currentPhase: GameRoundPhase.BIDDING,
  currentlySelectedContract: null,
  legalMoves: [],
  currentTrick: {
    no: 1,
    cards: {}
  },
});