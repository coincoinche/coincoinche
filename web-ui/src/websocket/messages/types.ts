import {CardValue} from "../../assets/cards";
import {LegalBiddingMove, Player, Contract, LegalPlayingMove, Trick} from "../../game-engine/gameStateTypes";

export enum TopicTemplate {
  LOBBY='/topic/lobby',
  GAME='/topic/game/{gameId}/player/{username}',
  GAME_BROADCAST='/topic/game/{gameId}',
}

export enum SocketEndpoint {
  JOIN_LOBBY = '/app/lobby/join',
  QUIT_LOBBY = '/app/lobby/quit',
}

export enum MessageType {
  JOIN_LOBBY = 'JOINED_LOBBY',
  QUIT_LOBBY = 'QUIT_LOBBY',
  GAME_STARTED = 'GAME_STARTED',
  NEW_STATE = 'NEW_STATE',
  MOVE = 'MOVE',
  MOVE_BID = 'MOVE_BID',
  MOVE_CARD = 'MOVE_CARD',
  GAME_FINISHED = 'GAME_FINISHED',
  CLIENT_READY = 'CLIENT_READY',
}

export enum MoveType {
  SPECIAL_BIDDING = 'SPECIAL_BIDDING',
  CONTRACT_BIDDING = 'CONTRACT_BIDDING',
}

export type SocketMessage = {
  message?: MessageType,
  type?: MessageType,
  bid?: string | null,
  card?: string | null,
};

export type GameStartedMessage = {
  message: MessageType.GAME_STARTED,
  content: {
    gameId: string,
    users: Player[]
  }
};

export type NewStateMessage = {
  message: MessageType.NEW_STATE,
  content: {
    state: {
      phase: string,
      currentPlayer: string,
      highestBidding?: Contract
      currentTrick?: Trick
      multiplier: number
    },
    cards: {
      [username: string]: CardValue[]
    },
    scores: {
      you: number,
      them: number
    }
    moves: LegalBiddingMove[] | LegalPlayingMove[];
  }
}

export type GameFinishedEvent = {
  message: MessageType.GAME_FINISHED,
  winning: boolean,
}

export type PlayerBadeEvent = {
  type: MessageType.MOVE_BID,
  playerIndex: number,
  move: LegalBiddingMove
}

export type CardPlayedEvent = {
  message: MessageType.MOVE_CARD,
  playerIndex: number,
  move: LegalPlayingMove,
};

export type Message = NewStateMessage;
