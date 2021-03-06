import _ from 'lodash';

import {
  NewStateMessage,
  Message,
  MessageType,
} from '../websocket/messages/types';
import {GameRoundPhase, GameState, LegalBiddingMove, Contract, LegalPlayingMove, Trick} from "./gameStateTypes";
import {CardValue} from "../assets/cards";
import {positionFromUsername} from "./playerPositionning";

class InvalidEventError extends Error {}

export class GameStateModifier {
  gameState: GameState;

  constructor(initialGameState: GameState) {
    this.gameState = _.cloneDeep(initialGameState);
  }

  setCurrentPlayer = (playerName: string) => {
    this.gameState.currentPlayer = positionFromUsername(
      playerName,
      this.gameState.usernamesByPosition
    );
    return this;
  };

  setCurrentPhase = (phase: GameRoundPhase) => {
    this.gameState.currentPhase = phase;
    return this;
  };

  setLegalMoves = (legalMoves: LegalBiddingMove[] | LegalPlayingMove[]) => {
    this.gameState.legalMoves = legalMoves;
    return this;
  }

  setCurrentlySelectedContract = (contract: Partial<LegalBiddingMove | null>) => {
    this.gameState.currentlySelectedContract = contract;
    return this;
  };

  setPlayersCards = (cards: {[username: string]: CardValue[]}) => {
    this.gameState.cards = cards;
    return this;
  };

  setHighestBidding = (contract: Contract | null) => {
    this.gameState.highestBidding = contract;
    return this;
  }

  setMultiplier = (multiplier: number) => {
    this.gameState.multiplier = multiplier;
    return this;
  }

  setScores = (scores: {you: number, them: number}) => {
    this.gameState.scores = scores;
    return this;
  }

  updateCurrentTrick = (trick: Trick) => {
    this.gameState.currentTrick = trick;
    return this;
  }

  updatePreviousTrick = (trick: Trick) => {
    this.gameState.previousTrick = trick;
    return this;
  }

  setShowPreviousTrick = (b: boolean) => {
    this.gameState.showPreviousTrick = b;
    return this;
  }

  retrieveNewState = () => this.gameState;
}

const applyNewStateMessage = (msg: NewStateMessage, gameState: GameState): GameState => {
  const gameStateModifier = new GameStateModifier(gameState)
    .setPlayersCards(msg.content.cards)
    .setLegalMoves(msg.content.moves)
    .setCurrentPlayer(msg.content.state.currentPlayer)
    .setMultiplier(msg.content.state.multiplier)
    .setScores(msg.content.scores)
    // @ts-ignore
    .setCurrentPhase(GameRoundPhase[msg.content.state.phase.toUpperCase()])
    .setCurrentlySelectedContract(null)
  if (msg.content.state.highestBidding) {
    gameStateModifier.setHighestBidding(msg.content.state.highestBidding);
  }
  if (msg.content.state.currentTrick) {
    gameStateModifier.updateCurrentTrick(msg.content.state.currentTrick);
  }
  if (msg.content.state.previousTrick) {
    gameStateModifier.updatePreviousTrick(msg.content.state.previousTrick);
  }
  return gameStateModifier.retrieveNewState();
}

export const applyMessage = (msg: Message, gameState: GameState): GameState => {
  switch (msg.message) {
    case MessageType.NEW_STATE:
      return applyNewStateMessage(msg, gameState);
    default:
      throw new InvalidEventError('Unknown message type.');
  }
};

export const showPreviousTrick = (show: boolean, gameState: GameState): GameState => {
  return new GameStateModifier(gameState).setShowPreviousTrick(show).retrieveNewState();
}
