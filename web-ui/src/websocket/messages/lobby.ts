import {MessageType} from "./types";

export const makeJoinLobbyMessage = (username: string) => ({
  message: MessageType.JOIN_LOBBY,
  username
});

export const makeQuitLobbyMessage = (username: string) => ({
  message: MessageType.QUIT_LOBBY,
  username
});
