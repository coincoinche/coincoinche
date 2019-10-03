export type Player = {
  authorisedPlays: string[];
  cardsInHand: string[];
};

export enum Position {
  top = 'top',
  right = 'right',
  bottom = 'bottom',
  left = 'left',
}

export type Players = {
  top: Player;
  left: Player;
  right: Player;
  bottom: Player;
};

export type Trick = {
  topCard?: string;
  leftCard?: string;
  rightCard?: string;
  bottomCard?: string;
};

export type GameState = {
  players: Players;
  currentPlayer: Position;
  currentTrick: Trick;
};
