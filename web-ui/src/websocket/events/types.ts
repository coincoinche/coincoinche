import {CardValue} from "../../assets/cards";
import {GameRoundPhase, LegalBiddingMove} from "../../game-engine/gameStateTypes";

export enum TopicTemplate {
  LOBBY='/topic/lobby',
  GAME='/topic/game/{gameId}/player/{username}',
  GAME_BROADCAST='/topic/game/{gameId}',
}

export enum SocketEndpoint {
  JOIN_LOBBY = '/app/lobby/join',
}

export enum EventType {
  JOIN_LOBBY = 'PLAYER_JOINED_LOBBY',
  GAME_STARTED = 'GAME_STARTED',
  GAME_FINISHED = 'GAME_FINISHED',
  ROUND_STARTED = 'ROUND_STARTED',
  ROUND_PHASE_STARTED = 'ROUND_PHASE_STARTED',
  CLIENT_READY = 'CLIENT_READY',
  BIDDING_TURN_STARTED = 'BIDDING_TURN_STARTED',
  PLAYING_TURN_STARTED = 'PLAYING_TURN_STARTED',
  PLAYER_BADE = 'PLAYER_BADE',
  CARD_PLAYED = 'CARD_PLAYED',
}

export enum MoveType {
  SPECIAL_BIDDING = 'SPECIAL_BIDDING',
  CONTRACT_BIDDING = 'CONTRACT_BIDDING',
}

export type SocketMessage = {
  type: EventType;
};

export type GameStartedEvent = {
  type: EventType.GAME_STARTED,
  gameId: string,
  usernames: string[]
}

export type GameFinishedEvent = {
  type: EventType.GAME_FINISHED,
  winning: boolean,
}

export type PlayerBadeEvent = {
  type: EventType.PLAYER_BADE,
} & LegalBiddingMove;

export type CardPlayedEvent = {
  type: EventType.CARD_PLAYED,
  card: CardValue,
};

export type RoundStartedEvent = {
  type: EventType.ROUND_STARTED,
  playerCards: CardValue[]
}

export type RoundPhaseStartedEvent = {
  type: EventType.ROUND_PHASE_STARTED,
  phase: GameRoundPhase,
}

export type BiddingTurnStartedEvent = {
  type: EventType.BIDDING_TURN_STARTED,
  legalBiddingMoves: LegalBiddingMove[],
  playerIndex: number,
}

export type PlayingTurnStartedEvent = {
  type: EventType.PLAYING_TURN_STARTED,
  legalPlayingMoves: CardValue[],
  playerIndex: number,
}

export type Event =
  PlayerBadeEvent |
  CardPlayedEvent |
  RoundStartedEvent |
  BiddingTurnStartedEvent |
  RoundPhaseStartedEvent |
  PlayingTurnStartedEvent |
  GameFinishedEvent;
