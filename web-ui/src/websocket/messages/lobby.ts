import {MessageType} from "./types";

export const makeJoinLobbyMessage = (username: string) => ({
  message: MessageType.JOIN_LOBBY,
  username
});
