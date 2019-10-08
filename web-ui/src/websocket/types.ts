export enum TOPIC {
  GREETINGS = '/topic/greetings',
  LOBBY='/topic/lobby',
}

export enum SOCKET_ENDPOINT {
  HELLO = '/app/hello',
  JOIN_LOBBY = '/app/lobby/join',
}

export enum MESSAGE_TYPE {
  HELLO = 'HELLO',
  JOIN_LOBBY = 'JOIN_LOBBY',
  GAME_START = 'GAME_START'
}

export type SocketMessage = {
  type: MESSAGE_TYPE;
};

export type HelloMessage = SocketMessage & {
  from: string;
  content: string;
}
