export enum TOPIC {
  LOBBY='/topic/lobby',
}

export enum SOCKET_ENDPOINT {
  JOIN_LOBBY = '/app/lobby/join',
}

export enum MESSAGE_TYPE {
  JOIN_LOBBY = 'JOIN_LOBBY',
  GAME_START = 'GAME_START'
}

export type SocketMessage = {
  type: MESSAGE_TYPE;
};
