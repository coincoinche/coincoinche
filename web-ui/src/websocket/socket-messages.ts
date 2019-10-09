import {MESSAGE_TYPE, SocketMessage} from "./types";

export const makeJoinLobbyMessage = (username: string) => ({
  type: MESSAGE_TYPE.JOIN_LOBBY,
  username
});
