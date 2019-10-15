import {CardValue} from "../assets/cards";
import {MoveType} from "../websocket/events/types";

export enum Position {
  top = 'top',
  right = 'right',
  bottom = 'bottom',
  left = 'left',
}

export enum GamePhase {
  bidding = 'bidding',
  main = 'main',
}

export type Trick = {
  [Position.top]?: CardValue;
  [Position.left]?: CardValue;
  [Position.right]?: CardValue;
  [Position.bottom]?: CardValue;
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

export enum ContractValue {
  EIGHTY = '80',
  NINETY = '90',
  HUNDRED = '100',
  HUNDRED_TEN = '110',
  HUNDRED_TWENTY = '120',
  HUNDRED_THIRTY = '130',
  HUNDRED_FOURTY = '140',
  HUNDRED_FIFTY = '150',
  HUNDRED_SIXTY = '160',
  CAPOT = '250',
  GENERALE = '500',
}

export type ContractBiddingMove = {
  moveType: MoveType.CONTRACT_BIDDING,
  value: ContractValue,
  suit: Suit,
}

export type LegalBiddingMove = SpecialBiddingMove | ContractBiddingMove;

export type GameState = {
  usernames: string[];
  currentPlayer: Position;
  cardsInHand: CardValue[];
} & ({
  currentPhase: GamePhase.bidding;
  currentlySelectedContract: Partial<LegalBiddingMove> | null;
  lastBiddingContract: Partial<LegalBiddingMove>;
  legalMoves: LegalBiddingMove[];
} | {
  currentPhase: GamePhase.main;
  currentTrick: Trick;
  legalMoves: CardValue[];
});
