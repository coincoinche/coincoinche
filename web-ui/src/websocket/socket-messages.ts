import {MESSAGE_TYPE, SocketMessage} from "./types";

export const joinLobbyMessage: SocketMessage = {
  type: MESSAGE_TYPE.JOIN_LOBBY,
};
