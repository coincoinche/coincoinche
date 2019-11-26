import {MessageType} from "./types";

export const makeSignUpMessage = (username: string, password:string) => ({
  message: MessageType.SIGN_UP,
  username,
  password,
});
