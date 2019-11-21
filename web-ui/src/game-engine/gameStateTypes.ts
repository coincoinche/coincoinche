import {CardValue} from "../assets/cards";
import {MoveType} from "../websocket/messages/types";

export type Player = {
  username: string,
  rating: number
}

export enum Position {
  top = 'top',
  right = 'right',
  bottom = 'bottom',
  left = 'left',
}

export type UsernamesByPosition = {
  [position in Position]: string; // username.
};

export enum GameRoundPhase {
  BIDDING = 'bidding',
  MAIN = 'main',
}

export type Trick = {
  no: number,
  cards: {
    [username: string]: CardValue
  }
};

export enum SpecialBidding {
  COINCHE = 'COINCHE',
  SURCOINCHE = 'SURCOINCHE',
  PASS = 'PASS',
}

export enum Suit {
  SPADES = 'spades',
  HEARTS = 'hearts',
  CLUBS = 'clubs',
  DIAMONDS = 'diamonds',
}

export type SpecialBiddingMove = {
  moveType: MoveType.SPECIAL_BIDDING,
  bidding: SpecialBidding,
}

export type ContractBiddingMove = {
  moveType: MoveType.CONTRACT_BIDDING,
  contract: Contract,
}

export type LegalBiddingMove = SpecialBiddingMove | ContractBiddingMove;

export type LegalPlayingMove = {
  card: CardValue
}

export type Contract = {
  owner?: string,
  suit: Suit,
  value: number
}

export type GameState = {
  users: Player[],
  usernamesByPosition: UsernamesByPosition,
  currentPlayer: Position,
  cards: {
    [username: string]: CardValue[]
  },
  multiplier: number,
  currentPhase: GameRoundPhase,
  currentlySelectedContract: Partial<LegalBiddingMove> | null,
  highestBidding: Contract | null,
  legalMoves: LegalBiddingMove[] | LegalPlayingMove[];
  currentTrick: Trick;
};
