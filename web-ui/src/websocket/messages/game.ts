import {
  MessageType,
  PlayerBadeEvent,
  CardPlayedEvent, NewStateMessage, Message, MoveType
} from "./types";
import { Suit, Contract, LegalBiddingMove, SpecialBidding, LegalPlayingMove} from "../../game-engine/gameStateTypes";
import { CardValue } from "../../assets/cards";

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

const legalBiddingMoveToString = (m: LegalBiddingMove): string | null => {
  if (m.moveType === MoveType.SPECIAL_BIDDING) {
    return m.bidding.toLowerCase();
  }
  return m.contract.value.toString() + m.contract.suit.substring(0,1).toLowerCase();
}

const legalPlayingMoveToString = (m: LegalPlayingMove): string | null => {
  return m.card.toString();
}

const legalBiddingMoveFromString = (s: string): LegalBiddingMove | null => {
  const contract = contractFromString(s);
  if (contract) {
    return {
      moveType: MoveType.CONTRACT_BIDDING,
      contract: contract
    }
  }
  return {
    moveType: MoveType.SPECIAL_BIDDING,
    // @ts-ignore
    bidding: SpecialBidding[s.toUpperCase()]
  }
}

const LegalPlayingMoveFromString = (s: string): LegalPlayingMove | null => {
  return {
    // @ts-ignore
    card: CardValue[s.toLowerCase()]
  }
}

const legalMoveFromString = (p: string, m: string): LegalBiddingMove | LegalPlayingMove | null => {
  switch (p.toLowerCase()) {
    case "bidding":
      return legalBiddingMoveFromString(m);
    case "main":
      return LegalPlayingMoveFromString(m);
    default:
      return null;
  }
}

const contractFromString = (s: string): Contract | null => {
  const match = /^(\d+)([s,h,c,d])$/.exec(s);
  if (match) {
    return {
      suit: suitLetterParser(match[2]),
      value: parseInt(match[1])
    }
  }
  return null;
}

export const inboundGameMessageParser: { [type in MessageType]?: (msg: any) => Message } = {
  [MessageType.NEW_STATE]: (msg: any): NewStateMessage => {
    const newStateMessage = {
      message: MessageType.NEW_STATE,
      content: {
        state: {
          phase: msg.content.state.phase,
          currentPlayer: msg.content.state.currentPlayer,
          highestBidding: msg.content.state.highestBidding,
          multiplier: msg.content.state.multiplier,
          currentTrick: {
            no: msg.content.state.currentTrick ? msg.content.state.currentTrick.no : 1,
            cards: {}
          },
          previousTrick: {
            no: msg.content.state.previousTrick ? msg.content.state.previousTrick.no : 0,
            cards: {}
          }
        },
        scores: {
          you: msg.content.scores.you,
          them: msg.content.scores.them
        },
        cards: {},
        moves: msg.content.moves.map((s: string) => legalMoveFromString(msg.content.state.phase, s))
      }
    }
    for (const username in msg.content.cards) {
      if (Object.prototype.hasOwnProperty.call(msg.content.cards, username)) {
        // @ts-ignore
        newStateMessage.content.cards[username] = msg.content.cards[username].map(
        // @ts-ignore
        (c: string) => CardValue[c.toLowerCase()]
        );
      }
    }
    if (msg.content.state.currentTrick) {
      for (const username in msg.content.state.currentTrick.cards) {
        if (Object.prototype.hasOwnProperty.call(msg.content.state.currentTrick.cards, username)) {
        // @ts-ignore
        newStateMessage.content.state.currentTrick.cards[username] =
          // @ts-ignore
          CardValue[msg.content.state.currentTrick.cards[username].toLowerCase()];
        }
      }
    }
    if (msg.content.state.previousTrick) {
      for (const username in msg.content.state.previousTrick.cards) {
        if (Object.prototype.hasOwnProperty.call(msg.content.state.previousTrick.cards, username)) {
        // @ts-ignore
        newStateMessage.content.state.previousTrick.cards[username] =
          // @ts-ignore
          CardValue[msg.content.state.previousTrick.cards[username].toLowerCase()];
        }
      }
    }
    // @ts-ignore
    return newStateMessage;
  }
};

export const outboundGameEventConverter = {
  [MessageType.MOVE_BID]: (event: PlayerBadeEvent) => {
    return {
      bid: legalBiddingMoveToString(event.move)
    }
  },
  [MessageType.MOVE_CARD]: (event: CardPlayedEvent) => {
    return {
      card: legalPlayingMoveToString(event.move)
    }
  }
}
