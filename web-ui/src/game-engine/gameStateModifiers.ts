import _ from 'lodash';

import {
  BiddingTurnStartedEvent,
  CardPlayedEvent,
  Event,
  EventType,
  MoveType,
  PlayerBadeEvent,
  PlayingTurnStartedEvent,
  RoundPhaseStartedEvent,
  RoundStartedEvent
} from '../websocket/events/types';
import {GameRoundPhase, GameState, LegalBiddingMove, Position} from "./gameStateTypes";
import {CardValue} from "../assets/cards";
import {positionFromPlayerIndex, positionFromUsername} from "./playerPositionning";

class InvalidEventError extends Error {}

export class GameStateModifier {
  gameState: GameState;

  constructor(initialGameState: GameState) {
    this.gameState = _.cloneDeep(initialGameState);
  }

  setCurrentPlayer = (playerIndex: number) => {
    this.gameState.currentPlayer = positionFromUsername(
      this.gameState.usernames[playerIndex],
      this.gameState.usernamesByPosition
    );
    return this;
  };

  setCurrentPhase = (phase: GameRoundPhase) => {
    this.gameState.currentPhase = phase;
    return this;
  };

  setLegalBiddingMoves = (legalMoves: LegalBiddingMove[]) => {
    this.gameState.legalBiddingMoves = legalMoves;
    return this;
  };

  setLegalPlayingMoves = (legalMoves: CardValue[]) => {
    this.gameState.legalPlayingMoves = legalMoves;
    return this;
  };

  setLastBiddingContract = (contract: LegalBiddingMove) => {
    this.gameState.lastBiddingContract = contract;
    return this;
  };

  setCurrentlySelectedContract = (contract: Partial<LegalBiddingMove | null>) => {
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
    this.gameState.currentTrick = {};
    return this;
  };

  addCardToCurrentTrick = (playerIndex: number, card: CardValue) => {
    this.gameState.currentTrick = {
      ...this.gameState.currentTrick,
      [positionFromPlayerIndex(playerIndex, this.gameState.usernamesByPosition, this.gameState.usernames)]: card,
    };
    return this;
  };

  retrieveNewState = () => this.gameState;
}

const applyPlayerBadeEvent = (event: PlayerBadeEvent, gameState: GameState): GameState => {
  const gameStateModifier = new GameStateModifier(gameState)
    .setCurrentlySelectedContract(null)
    .setLegalBiddingMoves([]);

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

const applyCardPlayedEvent = (event: CardPlayedEvent, gameState: GameState): GameState => {
  return new GameStateModifier(gameState)
    .addCardToCurrentTrick(event.playerIndex, event.card)
    .retrieveNewState();
};

const applyRoundStartedEvent = ({ playerCards }: RoundStartedEvent, gameState: GameState): GameState => {
  return new GameStateModifier(gameState)
    .setPlayerCards(playerCards)
    .retrieveNewState();
};

const applyRoundPhaseStartedEvent = ({ phase }: RoundPhaseStartedEvent, gameState: GameState): GameState => {
  const modifier = new GameStateModifier(gameState)
    .setCurrentPhase(phase);

  if (phase === GameRoundPhase.MAIN) {
    return modifier
      .resetCurrentTrick()
      .setLegalPlayingMoves([])
      .retrieveNewState();
  }

  return modifier
    .setLegalBiddingMoves([])
    .retrieveNewState();
};

const applyBiddingTurnStartedEvent = ({ legalBiddingMoves, playerIndex }: BiddingTurnStartedEvent, gameState: GameState): GameState => {
  return new GameStateModifier(gameState)
    .setLegalBiddingMoves(legalBiddingMoves)
    .setCurrentPlayer(playerIndex)
    .retrieveNewState();
};

const applyPlayingTurnStartedEvent = ({ legalPlayingMoves, playerIndex }: PlayingTurnStartedEvent, gameState: GameState): GameState => {
  return new GameStateModifier(gameState)
    .setLegalPlayingMoves(legalPlayingMoves)
    .setCurrentPlayer(playerIndex)
    .retrieveNewState();
};

export const applyEvent = (event: Event, gameState: GameState): GameState => {
  switch (event.type) {
    case EventType.PLAYER_BADE:
      return applyPlayerBadeEvent(event, gameState);
    case EventType.CARD_PLAYED:
      return applyCardPlayedEvent(event, gameState);
    case EventType.ROUND_STARTED:
      return applyRoundStartedEvent(event, gameState);
    case EventType.ROUND_PHASE_STARTED:
      return applyRoundPhaseStartedEvent(event, gameState);
    case EventType.BIDDING_TURN_STARTED:
      return applyBiddingTurnStartedEvent(event, gameState);
    case EventType.PLAYING_TURN_STARTED:
      return applyPlayingTurnStartedEvent(event, gameState);
    default:
      throw new InvalidEventError('Unknown event type.');
  }
};