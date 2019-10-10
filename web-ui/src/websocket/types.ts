export enum TOPIC_TEMPLATE {
  LOBBY='/topic/lobby',
  GAME='/topic/game/{gameId}/player/{username}'
}

export enum SOCKET_ENDPOINT {
  JOIN_LOBBY = '/app/lobby/join',
}

export enum MESSAGE_TYPE {
  JOIN_LOBBY = 'PLAYER_JOINED_LOBBY',
  GAME_STARTED = 'GAME_STARTED',
  ROUND_STARTED = 'ROUND_STARTED',
  CLIENT_READY = 'CLIENT_READY'
}

export type SocketMessage = {
  type: MESSAGE_TYPE;
};
