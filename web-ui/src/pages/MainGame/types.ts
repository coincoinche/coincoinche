import { CardValue } from "../../assets/cards";

export type Player = {
  authorisedPlays: boolean[];
  cardsInHand: CardValue[];
};

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

export type Players = {
  [Position.top]: Player;
  [Position.left]: Player;
  [Position.right]: Player;
  [Position.bottom]: Player;
};

export type Trick = {
  [Position.top]?: CardValue;
  [Position.left]?: CardValue;
  [Position.right]?: CardValue;
  [Position.bottom]?: CardValue;
};

export type GameState = {
  players: Players;
  currentPlayer: Position;
  currentTrick: Trick;
  currentPhase: GamePhase;
  contract: Contract | null;
};

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
  CAPOT = 'CAPOT',
  GENERALE = 'GENERALE',
}

export enum SpecialBidding {
  COINCHE = 'COINCHE',
  SURCOINCHE = 'SURCOINCHE',
  PASS = 'PASS',
}

export enum Suit {
  SPADE = 'spade',
  HEART = 'heart',
  CLUB = 'club',
  DIAMOND = 'diamond',
}

export type Contract = {
  value: ContractValue;
  suit: Suit;
  specialBidding?: SpecialBidding;
}