import {MessageType} from "./types";

export const makeJoinLobbyMessage = (username: string | undefined) => ({
  message: MessageType.JOIN_LOBBY,
  username
});

export const makeQuitLobbyMessage = (username: string | undefined) => ({
  message: MessageType.QUIT_LOBBY,
  username
});
