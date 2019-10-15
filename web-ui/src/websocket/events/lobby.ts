import {EventType} from "./types";

export const makeJoinLobbyMessage = (username: string) => ({
  type: EventType.JOIN_LOBBY,
  username
});
