import {
  Event,
  EventType,
  MoveType,
  PlayerBadeEvent,
  RoundPhaseStartedEvent,
  RoundStartedEvent,
  BiddingTurnStartedEvent, PlayingTurnStartedEvent
} from "./types";
import {ContractBiddingMove, SpecialBiddingMove, Suit} from "../../game-engine/gameStateTypes";

const suitLetterParser = (suitLetter: string): Suit => {
  if (suitLetter === 's') {
    return Suit.SPADES;
  }

  if (suitLetter === 'h') {
    return Suit.HEARTS;
  }

  if (suitLetter === 'c') {
    return Suit.CLUBS;
  }

  if (suitLetter === 'd') {
    return Suit.DIAMONDS;
  }
  throw new Error('Invalid suit letter');
};

export const inboundGameEventParser: { [type in EventType]?: (event: any) => Event } = {
  [EventType.PLAYER_BADE]: (event: any): PlayerBadeEvent => {
    const { value, suit, moveType, special } = event;
    if (moveType === MoveType.CONTRACT_BIDDING) {
      return {
        type: EventType.PLAYER_BADE,
        moveType: MoveType.CONTRACT_BIDDING,
        value: value.toString(),
        suit: suit.toLowerCase(),
      }
    }

    if (moveType === MoveType.SPECIAL_BIDDING) {
      return {
        type: EventType.PLAYER_BADE,
        moveType: MoveType.SPECIAL_BIDDING,
        bidding: special,
      }
    }

    throw new Error('Invalid move type');
  },

  [EventType.ROUND_STARTED]: (event: any): RoundStartedEvent => {
    const { playerCards } = event;
    return {
      type: EventType.ROUND_STARTED,
      playerCards,
    }
  },

  [EventType.ROUND_PHASE_STARTED]: (event: any): RoundPhaseStartedEvent => {
    const { phase } = event;
    return {
      type: EventType.ROUND_PHASE_STARTED,
      phase,
    }
  },

  [EventType.BIDDING_TURN_STARTED]: (event: any): BiddingTurnStartedEvent => {
    const { legalMoves: jsonLegalMoves } = event;
    const legalMoves = jsonLegalMoves.map((jsonMove: string) => {
      const move = JSON.parse(jsonMove);
      const { value, suit, moveType, special } = move;
      if (moveType === MoveType.CONTRACT_BIDDING) {
        return {
          ...move,
          value: value.toString(),
          suit: suitLetterParser(suit),
        }
      }

      if (moveType === MoveType.SPECIAL_BIDDING) {
        return {
          ...move,
          bidding: special,
        }
      }

      throw new Error('Invalid move type');
    });
    return {
      type: EventType.BIDDING_TURN_STARTED,
      legalMoves,
    }
  },

  [EventType.PLAYING_TURN_STARTED]: (event: any): PlayingTurnStartedEvent => {
    const { legalMoves } = event;
    return {
      type: EventType.PLAYING_TURN_STARTED,
      legalMoves,
    }
  },
};

export const outboundGameEventConverter = {
  [EventType.PLAYER_BADE]: (event: PlayerBadeEvent) => {
    const { moveType } = event;
    if (moveType === MoveType.CONTRACT_BIDDING) {
      const { value, suit } = event as ContractBiddingMove;
      return {
        ...event,
        value: parseInt(value),
        suit: suit.toUpperCase(),
      }
    }

    if (moveType === MoveType.SPECIAL_BIDDING) {
      const { bidding } = event as SpecialBiddingMove;
      return {
        ...event,
        special: bidding,
      };
    }

    throw new Error('Invalid move type');
  },
};