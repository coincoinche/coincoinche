import {ContractValue} from "../pages/MainGame/types";
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
  legalMoves: boolean[];
});
