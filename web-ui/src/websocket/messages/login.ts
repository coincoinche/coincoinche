import {MessageType} from "./types";

export const makeLogInMessage = (username: string, password:string) => ({
  message: MessageType.LOG_IN,
  username,
  password,
});
