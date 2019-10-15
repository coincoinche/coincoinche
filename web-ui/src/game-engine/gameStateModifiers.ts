import _ from 'lodash';

import {Event, EventType, RoundStartedEvent, TurnStartedEvent} from '../websocket/events/types';
import {GamePhase, GameState, LegalBiddingMove, Position, Trick} from "./gameStateTypes";
import {MoveType, PlayerBadeEvent} from "../websocket/events/types";
import {CardValue} from "../assets/cards";

class IllegalStateModificationError extends Error {}
class InvalidEventError extends Error {}

const getNextPlayer = (currentPlayer: Position): Position => {
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

export class GameStateModifier {
  gameState: GameState;

  constructor(initialGameState: GameState) {
    this.gameState = _.cloneDeep(initialGameState);
  }

  rotateCurrentPlayer = () => {
    this.gameState.currentPlayer = getNextPlayer(this.gameState.currentPlayer);
    return this;
  };

  setLegalMoves = (legalMoves: LegalBiddingMove[]) => {
    if (this.gameState.currentPhase !== GamePhase.bidding) {
      throw new IllegalStateModificationError('Can only set legal moves during the bidding phase');
    }
    this.gameState.legalMoves = legalMoves;
    return this;
  };

  setLastBiddingContract = (contract: LegalBiddingMove) => {
    if (this.gameState.currentPhase !== GamePhase.bidding) {
      throw new IllegalStateModificationError('Can only set legal moves during the bidding phase');
    }
    this.gameState.lastBiddingContract = contract;
    return this;
  };

  setCurrentlySelectedContract = (contract: Partial<LegalBiddingMove | null>) => {
    if (this.gameState.currentPhase !== GamePhase.bidding) {
      throw new IllegalStateModificationError('Can only set legal moves during the bidding phase');
    }
    this.gameState.currentlySelectedContract = contract;
    return this;
  };

  setPlayerCards = (playerCards: CardValue[]) => {
    this.gameState.cardsInHand = playerCards;
    return this;
  };

  playCardFromHand = (cardPlayed: CardValue) => {
    this.gameState.cardsInHand = this.gameState.cardsInHand.filter(cardInHand => cardInHand !== cardPlayed);
    return this;
  };

  resetCurrentTrick = () => {
    if (this.gameState.currentPhase !== GamePhase.main) {
      throw new IllegalStateModificationError('Can only update the current trick during the main game phase');
    }
    this.gameState.currentTrick = {};
    return this;
  };

  addCardToCurrentTrick = (position: Position, card: CardValue) => {
    if (this.gameState.currentPhase !== GamePhase.main) {
      throw new IllegalStateModificationError('Can only update the current trick during the main game phase');
    }
    this.gameState.currentTrick = {
      ...this.gameState.currentTrick,
      [position]: card,
    };
    return this;
  };

  retrieveNewState = () => this.gameState;
}

const applyPlayerBadeEvent = (event: PlayerBadeEvent, gameState: GameState): GameState => {
  const gameStateModifier = new GameStateModifier(gameState)
    .setCurrentlySelectedContract(null)
    .setLegalMoves([])
    .rotateCurrentPlayer();

  if (event.moveType === MoveType.SPECIAL_BIDDING) {
    return gameStateModifier
      .setLastBiddingContract({
        moveType: MoveType.SPECIAL_BIDDING,
        bidding: event.bidding,
      })
      .retrieveNewState();
  }

  if (event.moveType === MoveType.CONTRACT_BIDDING) {
    return gameStateModifier
      .setLastBiddingContract({
        moveType: MoveType.CONTRACT_BIDDING,
        suit: event.suit,
        value: event.value,
      })
      .retrieveNewState();
  }

  throw new InvalidEventError('Invalid move type');
};

const applyRoundStartedEvent = ({ playerCards }: RoundStartedEvent, gameState: GameState): GameState => {
  return new GameStateModifier(gameState)
    .setPlayerCards(playerCards)
    .retrieveNewState();
};

const applyTurnStartedEvent = ({ legalMoves }: TurnStartedEvent, gameState: GameState): GameState => {
  return new GameStateModifier(gameState)
    .setLegalMoves(legalMoves)
    .retrieveNewState();
};

export const applyEvent = (event: Event, gameState: GameState): GameState => {
  switch (event.type) {
    case EventType.PLAYER_BADE:
      return applyPlayerBadeEvent(event, gameState);
    case EventType.ROUND_STARTED:
      return applyRoundStartedEvent(event, gameState);
    case EventType.TURN_STARTED:
      return applyTurnStartedEvent(event, gameState);
    default:
      throw new InvalidEventError('Unknown event type.');
  }
};