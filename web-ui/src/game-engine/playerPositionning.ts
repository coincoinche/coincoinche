import {Position, UsernamesByPosition} from "./gameStateTypes";

export const getNextPlayer = (currentPlayer: Position): Position => {
  if (currentPlayer === Position.bottom) {
    return Position.left;
  }

  if (currentPlayer === Position.left) {
    return Position.top;
  }

  if (currentPlayer === Position.top) {
    return Position.right;
  }

  if (currentPlayer === Position.right) {
    return Position.bottom;
  }
  throw new Error('Unknown position');
};

export const buildUsernamesByPosition = (usernames: string[], bottomPlayerIndex: number): UsernamesByPosition => ({
  [Position.bottom]: usernames[bottomPlayerIndex],
  [Position.left]: usernames[(bottomPlayerIndex + 1) % usernames.length],
  [Position.top]: usernames[(bottomPlayerIndex + 2) % usernames.length],
  [Position.right]: usernames[(bottomPlayerIndex + 3) % usernames.length],
});

export const positionFromUsername = (username: string, usernamesByPosition: UsernamesByPosition): Position =>
  Object.keys(usernamesByPosition).find((position) => usernamesByPosition[position as Position] === username) as Position;