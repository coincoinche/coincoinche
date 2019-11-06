import {Position, UsernamesByPosition} from "./gameStateTypes";

export const buildUsernamesByPosition = (usernames: string[], bottomPlayerIndex: number): UsernamesByPosition => ({
  [Position.bottom]: usernames[bottomPlayerIndex],
  [Position.left]: usernames[(bottomPlayerIndex + 1) % usernames.length],
  [Position.top]: usernames[(bottomPlayerIndex + 2) % usernames.length],
  [Position.right]: usernames[(bottomPlayerIndex + 3) % usernames.length],
});

export const positionFromUsername = (username: string, usernamesByPosition: UsernamesByPosition): Position =>
  Object.keys(usernamesByPosition).find((position) => usernamesByPosition[position as Position] === username) as Position;

export const playerIndexFromPosition = (position: Position, usernamesByPosition: UsernamesByPosition, usernames: string[]) =>
  usernames.indexOf(usernamesByPosition[position]);

export const positionFromPlayerIndex = (playerIndex: number, usernamesByPosition: UsernamesByPosition, usernames: string[]) =>
  positionFromUsername(usernames[playerIndex], usernamesByPosition);