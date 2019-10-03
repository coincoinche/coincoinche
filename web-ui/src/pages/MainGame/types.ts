import { CardValue } from "../../assets/cards";

export type Player = {
  authorisedPlays: CardValue[];
  cardsInHand: CardValue[];
};

export enum Position {
  top = 'top',
  right = 'right',
  bottom = 'bottom',
  left = 'left',
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
};
