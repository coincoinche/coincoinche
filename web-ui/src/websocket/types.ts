export enum TOPIC {
  GREETINGS = '/topic/greetings'
}

export enum SOCKET_ENDPOINT {
  HELLO = '/app/hello'
}

export enum MESSAGE_TYPE {
  HELLO = 'HELLO'
}

export type SocketMessage = {
  type: MESSAGE_TYPE;
  from: string;
  content: string;
};
